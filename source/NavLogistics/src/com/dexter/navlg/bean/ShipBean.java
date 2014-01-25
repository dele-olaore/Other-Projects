package com.dexter.navlg.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.international.status.Messages;
import org.jboss.solder.logging.Logger;

import com.dexter.navlg.i18n.AppBundleKey;
import com.dexter.navlg.model.Item;
import com.dexter.navlg.model.Ship;
import com.dexter.navlg.model.ShipItemInventory;
import com.dexter.navlg.model.ShipItems;
import com.dexter.navlg.model.ShipItemsUse;
import com.dexter.navlg.model.ShipItemsUseItem;
import com.dexter.navlg.model.ShipRequest;
import com.dexter.navlg.model.ShipRequestItems;
import com.dexter.navlg.model.ShipSparepart;
import com.dexter.navlg.model.SparepartStatus;
import com.dexter.navlg.model.User;
import com.dexter.navlg.model.Warehouse;
import com.dexter.navlg.model.WarehouseItemInventory;
import com.dexter.navlg.model.WarehouseItems;
import com.dexter.navlg.model.WarehouseSpareparts;
import com.dexter.navlg.security.Authenticated;
import com.dexter.navlg.util.EmailNotifier;
import com.dexter.navlg.util.SMSNotifier;

@Stateful
@SessionScoped
@Named("shipBean")
public class ShipBean
{
	@Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private Messages messages;
    
    @Inject
    @Authenticated
    private User user;
    
    private Ship ship;
    private ArrayList<Ship> ships;
    private ShipItems setting;
    private ArrayList<ShipItems> settings;
    
    private ShipItemInventory itemInventory;
    private ArrayList<ShipItemInventory> shipInventory;
    private ArrayList<ShipSparepart> shipItemSpareparts;
    private Date useDate;
    private String useRemark;
    
    private ShipRequest shipRequest;
    private Long item_id;
    private Long requestedAmount;
    private String foc_username;
    private ShipRequestItems shipRequestItem;
    private ArrayList<ShipRequestItems> shipRequestItems, selRequestItems;
    private ShipRequestItems selItemRequest;
    private ArrayList<ShipRequest> shipRequests;
    private ArrayList<ShipRequest> focPendingRequests, focRequests, spPendingRequests, spRequests, spsPendingRequests, spsRequests;
    private String issuer_username;
    
    private Long warehouse_id;
    private Item item;
    private ArrayList<WarehouseSpareparts> warehouseParts;
    private Hashtable<Long, Hashtable<Long, ArrayList<WarehouseSpareparts>>> itemSpareparts;
    
    private ArrayList<ShipSparepart> receiveSpareparts;
    
    private Date useDate1, useDate2;
    private ArrayList<ShipItemsUseItem> shipItemsUseItems;
    private ArrayList<ShipItemsUse> shipItemsUseList;
    
    public void UsageSearch()
    {
    	if(getUseDate1() != null && getUseDate2() != null)
    	{
    		Query q = em.createQuery("Select e from ShipItemsUse e where usedate between :dt1 and :dt2");
    		q.setParameter("dt1", getUseDate1());
    		q.setParameter("dt2", getUseDate2());
    		
    		setShipItemsUseList((ArrayList<ShipItemsUse>)q.getResultList());
    	}
    }
    
    public String movetoshipitemsusagereport()
    {
    	setShipItemsUseList(null);
    	
    	return "ship_itemsusagereport";
    }
    
    public String SaveUse()
    {
    	long count = 0;
    	
    	ArrayList<ShipItemsUseItem> shipItemsUseItems = new ArrayList<ShipItemsUseItem>();
    	
    	for(ShipSparepart e : getShipItemSpareparts())
    	{
    		if(e.isSelected())
    		{
    			e.setActive(false);
    			SparepartStatus stat = new SparepartStatus();
    			stat.setDate(getUseDate());
    			stat.setRemark(getUseRemark());
    			stat.setSparepart(e);
    			stat.setStatus("USED");
    			
    			em.merge(e);
    			em.persist(stat);
    			
    			ShipItemsUseItem shipItemsUseItem = new ShipItemsUseItem();
    			shipItemsUseItem.setShipSparepart(e);
    			shipItemsUseItems.add(shipItemsUseItem);
    			
    			count++;
    		}
    	}
    	
    	if(count > 0)
    	{
    		// Now persist the ship usage details
    		ShipItemsUse shipItemsUse = new ShipItemsUse();
    		shipItemsUse.setShip(getShipItemSpareparts().get(0).getShip()); // just to be sure I'm using the right ship
    		shipItemsUse.setItem(getShipItemSpareparts().get(0).getItem()); // just to be sure I'm using the right item
    		shipItemsUse.setUsedate(getUseDate());
    		shipItemsUse.setRemark(getUseRemark());
    		shipItemsUse.setItemsCount(count);
    		shipItemsUse.setUser(user);
    		
    		em.persist(shipItemsUse);
    		
    		for(ShipItemsUseItem e : shipItemsUseItems)
    		{
    			e.setShipItemsUse(shipItemsUse);
    			em.persist(e);
    		}
    		
    		ShipInventoryCheck(getShip(), getShipItemSpareparts().get(0).getItem());
    		setUseDate(null);
    		setUseRemark(null);
    		setShipItemSpareparts(null);
    		setItemInventory(null);
        	setShipInventory(null);
    		messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("Process successful!");
    	}
    	return "shipinventory";
    }
    
    public String movetoshipspareparts(Long id)
    {
    	for(ShipItemInventory e : getShipInventory())
    	{
    		if(id.longValue() == e.getId().longValue())
    		{
    			setItemInventory(e);
    			break;
    		}
    	}
    	setShipItemSpareparts(null);
    	
    	return "ship_itemspareparts";
    }
    
    public ArrayList<ShipRequest> getShipRequestForPDF()
    {
    	ArrayList<ShipRequest> list = new ArrayList<ShipRequest>();
    	list.add(getShipRequest());
    	return list;
    }
    
    public String pdfform(Long id)
    {
    	//if(!(getShipRequest().getId() != null && getShipRequest().getId().longValue() == id))
    	setShipRequest(em.find(ShipRequest.class, id));
    	
    	setSelRequestItems(null);
    	
    	return "shiprequest_table";
    }
    
    public String movetospareissue(Long ship_req_item_id)
    {
    	//setSelItemRequest(em.find(ShipRequestItems.class, ship_req_item_id));
    	for(ShipRequestItems e : getSelRequestItems())
    	{
    		if(e.getId() == ship_req_item_id)
    		{
    			setSelItemRequest(e);
    			break;
    		}
    	}
    	
    	//setItem(em.find(Item.class, getSelItemRequest().getItem()));
    	setItem(getSelItemRequest().getItem());
    	setWarehouseParts(null);
    	setWarehouse_id(null);
    	return "issuespareparts";
    }
    
    public void setSelectRequest(Long id)
    {
    	setShipRequest(em.find(ShipRequest.class, id));
    	setSelRequestItems(null);
    }
    
    public String setSelectRequest(Long id, String page)
    {
    	setShipRequest(em.find(ShipRequest.class, id));
    	setSelRequestItems(null);
    	
    	return page;
    }
    
    public String movetoissueitems()
    {
    	return "issueitems";
    }
    
    public String setSpsSelectRequest(Long id)
    {
    	if(!(getShipRequest().getId() != null && getShipRequest().getId().longValue() == id))
    		setShipRequest(em.find(ShipRequest.class, id));
    	
    	setReceiveSpareparts(null);
    	
    	return "receiveitems";
    }
    
    public String setSpSelectRequest(Long id)
    {
    	if(!(getShipRequest().getId() != null && getShipRequest().getId().longValue() == id))
    		setShipRequest(em.find(ShipRequest.class, id));
    	
    	setSelRequestItems(null);
    	setItem(null);
    	setWarehouseParts(null);
    	setWarehouse_id(null);
    	
    	return "issueitems";
    }
    
    public String movetofocpending()
    {
    	setFocRequests(null);
    	setFocPendingRequests(null);
    	setSpRequests(null);
    	setSpPendingRequests(null);
    	return "mngshipsrequests";
    }
    
    public String movetorequest(Long ship_id)
    {
    	setShip(em.find(Ship.class, ship_id));
    	setShipRequest(null);
    	setShipRequestItems(null);
    	setItem_id(null);
		setRequestedAmount(null);
    	return "shiprequest";
    }
    
    public String SaveRequestReceived()
    {
    	if(getShipRequest().getId() != null)
    	{
    		Hashtable<Long, Long> items_count = new Hashtable<Long, Long>();
    		for(ShipSparepart e : getReceiveSpareparts())
        	{
    			em.merge(e);
    			Long prevcount = 0L;
    			if(items_count.containsKey(e.getItem().getId()))
    			{
    				prevcount = items_count.get(e.getItem().getId());
    			}
    			prevcount += 1;
    			items_count.put(e.getItem().getId(), prevcount);
        	}
    		
    		Enumeration<Long> items_id = items_count.keys();
    		while(items_id.hasMoreElements())
    		{
    			Long item_id = items_id.nextElement();
    			Item t = em.find(Item.class, item_id);
    			
    			Query q = em.createQuery("SELECT e FROM ShipItemInventory e where ship=:ship and item=:item");
				q.setParameter("ship", getShipRequest().getShip());
				q.setParameter("item", t);
				ShipItemInventory stInven = null;
				try
				{
					stInven = (ShipItemInventory)q.getSingleResult();
				}
				catch(Exception ex)
				{}
				
				if(stInven == null)
				{
					ShipItems settings = new ShipItems();
					settings.setItem(t);
					settings.setShip(getShipRequest().getShip());
					settings.setMax_lvl(0L);
					settings.setMin_lvl(0L);
					settings.setReorder_lvl(0L);
					
					em.persist(settings);
					
					stInven = new ShipItemInventory();
					stInven.setItem(t);
					stInven.setSettings(settings);
					stInven.setShip(getShipRequest().getShip());
					stInven.setStock_lvl(0L);
					
					em.persist(stInven);
				}
				
				stInven.setStock_lvl(stInven.getStock_lvl() + items_count.get(item_id));
				
				em.merge(stInven);
    		}
    		
    		getShipRequest().setReceivedDate(new Date());
    		getShipRequest().setReceivedBy(user);
    		em.merge(getShipRequest());
    		
    		setReceiveSpareparts(null);
    		setShipRequest(null);
    		setSpsPendingRequests(null);
    		
    		messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    		return "mngshipsrequests";
    	}
    	
    	return "receiveitems";
    }
    
    public void AddRequestItem()
    {
    	if(getItem_id() != null && getItem_id() > 0 && getRequestedAmount() != null
    			&& getRequestedAmount() > 0)
    	{
    		Item it = em.find(Item.class, getItem_id());
    		ShipRequestItems shipRequestItem = new ShipRequestItems();
    		shipRequestItem.setItem(it);
    		shipRequestItem.setRequestedAmount(getRequestedAmount());
    		
    		setItem_id(null);
    		setRequestedAmount(null);
    		
    		getShipRequestItems().add(shipRequestItem);
    		messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    	}
    }
    
    public String SaveRequestIssued()
    {
    	String ret = "mngshipsrequests";
    	if(getShipRequest().getId() != null)
    	{
    		for(ShipRequestItems e : getSelRequestItems())
        	{
    			if(getItemSpareparts().containsKey(e.getItem().getId()))
    			{
    				try
    				{
		    			Hashtable<Long, ArrayList<WarehouseSpareparts>> warehouses = getItemSpareparts().get(e.getItem().getId());
						Enumeration<Long> w_id_enum = warehouses.keys();
						while(w_id_enum.hasMoreElements())
						{
							Long w_id = w_id_enum.nextElement();
							ArrayList<WarehouseSpareparts> w_items = warehouses.get(w_id);
							for(WarehouseSpareparts wsp : w_items)
							{
								if(wsp.isSelected())
								{
									ShipSparepart ssp = new ShipSparepart();
									ssp.setActive(false);
									ssp.setWarehouse_sp(wsp);
									ssp.setCustomized_number(wsp.getCustomized_number());
									ssp.setItem(wsp.getItem());
									ssp.setSerial_number(wsp.getSerial_number());
									ssp.setShip(getShipRequest().getShip());
									ssp.setShipRequest(getShipRequest());
									
									em.persist(ssp);
									
									wsp.setActive(false);
									em.merge(wsp);
								}
							}
							
							Warehouse w = em.find(Warehouse.class, w_id);
							WarehouseInventoryCheck(w, e.getItem());
						}
    				}
    				catch(Exception ex)
    				{
    					ex.printStackTrace();
    				}
    			}
    			
    			em.merge(e);
        	}
    		
    		getShipRequest().setIssuedDate(new Date());
    		em.merge(getShipRequest());
    		
    		final StringBuilder body2Builder = new StringBuilder();
    		final StringBuilder sms2bodyBuilder = new StringBuilder();
    		
    		body2Builder.append("<html><body>");
    		body2Builder.append("<p>Dear " + getShipRequest().getRequestedBy().getFirstname() + "</p><br/>");
    		body2Builder.append("<p>Your request has been issued by " + user.getFirstname() + " " + user.getLastname() +".</p><br/>");
    		body2Builder.append("<br/>");
    		body2Builder.append("<p>Please remember to mark as received when the items arrive.</p>");
    		body2Builder.append("</body></html>");
    		
    		sms2bodyBuilder.append("Your request is issued " + user.getFirstname() + " " + user.getLastname() + ".");
    		
    		final String to = getShipRequest().getRequestedBy().getEmail();
			final String phone = getShipRequest().getRequestedBy().getPhone();
    		Thread t = new Thread()
    		{
    			public void run()
    			{
    				try
    				{
    					EmailNotifier.sendEmail("donotreply@navlg.com", to, "Issue notification", body2Builder.toString());
    				}
    				catch(Exception ex)
    				{
    					ex.printStackTrace();
    				}
    			}
    		};
    		
    		Thread t2 = new Thread()
    		{
    			public void run()
    			{
    				try
    				{
    					SMSNotifier.SendSMS3(phone, sms2bodyBuilder.toString());
    				}
    				catch(Exception ex)
    				{
    					ex.printStackTrace();
    				}
    			}
    		};
    		
    		t.start();
    		t2.start();
    		
    		setItemSpareparts(null);
    		setSelRequestItems(null);
    		setShipRequest(null);
    		setSpPendingRequests(null);
    		
    		messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    	}
    	return ret;
    }
    
    public String movetosettings(Long ship_id)
    {
    	setShip(em.find(Ship.class, ship_id));
    	setSetting(null);
    	setSettings(null);
    	return "shipsettings";
    }
    
    public String movetoinventory(Long ship_id)
    {
    	setShip(em.find(Ship.class, ship_id));
    	setItemInventory(null);
    	setShipInventory(null);
    	return "shipinventory";
    }
    
    public void SaveApproval()
    {
    	if(getShipRequest().getApprovalRemark() != null)
    	{
    		getShipRequest().setApprovalDate(new Date());
    		
    		final StringBuilder bodyBuilder = new StringBuilder();
    		final StringBuilder body2Builder = new StringBuilder();
    		final StringBuilder smsbodyBuilder = new StringBuilder();
    		final StringBuilder sms2bodyBuilder = new StringBuilder();
    		
    		if(getShipRequest().getApprovalFlag()) // if approved
    		{
    			User issuer = em.find(User.class, getIssuer_username());
    			getShipRequest().setIssuedBy(issuer);
    			
    			bodyBuilder.append("<html><body>");
        		bodyBuilder.append("<p>Dear " + issuer.getFirstname() + "</p><br/>");
        		bodyBuilder.append("<p>A request for items as been approved by " + user.getFirstname() + " " + user.getLastname() + " and sent to you.</p><br/>");
        		bodyBuilder.append("<br/>");
        		bodyBuilder.append("<p>Please login to the Navy Logistics site to attend to this request approval.</p>");
        		bodyBuilder.append("</body></html>");
        		
        		smsbodyBuilder.append("You have a new request to attend to from " + user.getFirstname() + " " + user.getLastname() + ".");
        		
        		body2Builder.append("<html><body>");
        		body2Builder.append("<p>Dear " + getShipRequest().getRequestedBy().getFirstname() + "</p><br/>");
        		body2Builder.append("<p>Your request has been approved and sent to " + issuer.getFirstname() + " " + issuer.getLastname() +".</p><br/>");
        		body2Builder.append("<br/>");
        		body2Builder.append("<p>Please follow up with the above personnel for issue and delivery.</p>");
        		body2Builder.append("</body></html>");
        		
        		sms2bodyBuilder.append("Your request is approved by " + user.getFirstname() + " " + user.getLastname() + " and forwarded to " + issuer.getFirstname() + " " + issuer.getLastname() + ".");
    		}
    		else
    		{
    			body2Builder.append("<html><body>");
        		body2Builder.append("<p>Dear " + getShipRequest().getRequestedBy().getFirstname() + "</p><br/>");
        		body2Builder.append("<p>Your request has been dis-approved.</p><br/>");
        		body2Builder.append("<br/>");
        		body2Builder.append("Remark: " + getShipRequest().getApprovalRemark() + "<br/><br/>");
        		body2Builder.append("<p>Please review the remark above to know what next to do.</p>");
        		body2Builder.append("</body></html>");
        		
        		sms2bodyBuilder.append("Your request is dis-approved.");
    		}
    		
    		em.merge(getShipRequest());
    		
    		// TODO Send notification to the issuer and the requester about the latest progress of the request
    		if(getShipRequest().getApprovalFlag()) // if approved
    		{
    			final String to = getShipRequest().getIssuedBy().getEmail();
    			final String phone = getShipRequest().getIssuedBy().getPhone();
	    		Thread t = new Thread()
	    		{
	    			public void run()
	    			{
	    				try
	    				{
	    					EmailNotifier.sendEmail("donotreply@navlg.com", to, "Request notification", bodyBuilder.toString());
	    				}
	    				catch(Exception ex)
	    				{
	    					ex.printStackTrace();
	    				}
	    			}
	    		};
	    		
	    		Thread t2 = new Thread()
	    		{
	    			public void run()
	    			{
	    				try
	    				{
	    					SMSNotifier.SendSMS3(phone, smsbodyBuilder.toString());
	    				}
	    				catch(Exception ex)
	    				{
	    					ex.printStackTrace();
	    				}
	    			}
	    		};
	    		
	    		t.start();
	    		t2.start();
    		}
    		
    		final String to = getShipRequest().getRequestedBy().getEmail();
			final String phone = getShipRequest().getRequestedBy().getPhone();
    		Thread t = new Thread()
    		{
    			public void run()
    			{
    				try
    				{
    					EmailNotifier.sendEmail("donotreply@navlg.com", to, "Approval notification", body2Builder.toString());
    				}
    				catch(Exception ex)
    				{
    					ex.printStackTrace();
    				}
    			}
    		};
    		
    		Thread t2 = new Thread()
    		{
    			public void run()
    			{
    				try
    				{
    					SMSNotifier.SendSMS3(phone, sms2bodyBuilder.toString());
    				}
    				catch(Exception ex)
    				{
    					ex.printStackTrace();
    				}
    			}
    		};
    		
    		t.start();
    		t2.start();
    		
    		setShipRequest(null);
    		setSelRequestItems(null);
    		setFocPendingRequests(null);
    		
    		messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    	}
    }
    
    public String SaveRequest()
    {
    	if(getShipRequestItems().size() > 0)
    	{
    		getShipRequest().setDate(new Date());
    		getShipRequest().setRequestedBy(user);
    		getShipRequest().setShip(getShip());
    		
    		final User foc = em.find(User.class, getFoc_username());
    		getShipRequest().setApprovedBy(foc);
    		
    		em.persist(getShipRequest());
    		
    		for(ShipRequestItems e : getShipRequestItems())
    		{
    			e.setRequest(getShipRequest());
    			em.persist(e);
    		}
    		
    		// TODO: Send request notification to the FOC that it was sent to.
    		
    		final StringBuilder bodyBuilder = new StringBuilder();
    		bodyBuilder.append("<html><body>");
    		bodyBuilder.append("<p>Dear " + foc.getFirstname() + "</p><br/>");
    		bodyBuilder.append("<p>A request for items as been submitted with the details below: -</p><br/>");
    		bodyBuilder.append("<p>");
    		bodyBuilder.append("<table border=\"0\">");
    		bodyBuilder.append("<tr><td>Ship</td><td>" + getShip().getName() + "(" + getShip().getCode() + ")</td></tr>");
    		bodyBuilder.append("<tr><td>Date</td><td>" + getShipRequest().getDate() + "</td></tr>");
    		bodyBuilder.append("<tr><td>Submitted by</td><td>" + user.getFirstname() + " " + user.getLastname() + "(" + user.getDesignation() + ")" + "</td></tr>");
    		bodyBuilder.append("</table>");
    		bodyBuilder.append("</p><br/>");
    		bodyBuilder.append("<p>Please login to the Navy Logistics site to attend to this request.</p>");
    		bodyBuilder.append("</body></html>");
    		
    		final StringBuilder smsbodyBuilder = new StringBuilder();
    		smsbodyBuilder.append("You have a new request to attend to from ship: " + getShip().getName() + "(" + getShip().getCode() + ").");
    		
    		movetorequest(getShip().getId());
    		setSpsPendingRequests(null);
    		
    		Thread t = new Thread()
    		{
    			public void run()
    			{
    				EmailNotifier.sendEmail("donotreply@navlg.com", foc.getEmail(), "Request notification", bodyBuilder.toString());
    			}
    		};
    		
    		Thread t2 = new Thread()
    		{
    			public void run()
    			{
    				SMSNotifier.SendSMS3(foc.getPhone(), smsbodyBuilder.toString());
    			}
    		};
    		
    		t.start();
    		t2.start();
    		
    		messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    	}
    	
    	return "shiprequest";
    }
    
    public void SaveUpdate()
    {
    	if(getShip().getName() != null)
    	{
    		if(getShip().getId() != null && getShip().getId() > 0)
    			em.merge(getShip());
    		else
    			em.persist(getShip());
    		
    		setShip(null);
    		setShips(null);
    		
    		messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    	}
    }
    
    public void SaveUpdateSetting()
    {
    	if(getSetting().getItem().getId() != null)
    	{
    		Item it = em.find(Item.class, getSetting().getItem().getId());
    		getSetting().setItem(it);
    		if(getSetting().getShip().getName() == null)
    			getSetting().setShip(getShip());
    		
    		Query q = em.createQuery("Select e from ShipItems e where ship=:ship and item = :item");
			q.setParameter("ship", getShip());
			q.setParameter("item", it);
			
			try
			{
				ShipItems setting = (ShipItems) q.getSingleResult();
				if(setting != null)
					getSetting().setId(setting.getId());
			}
			catch(Exception ex)
			{}
    		
    		if(getSetting().getId() != null && getSetting().getId() > 0)
    			em.merge(getSetting());
    		else
    			em.persist(getSetting());
    		
    		q = em.createQuery("Select e from ShipItemInventory e where ship=:ship and item = :item");
			q.setParameter("ship", getShip());
			q.setParameter("item", it);
    		
    		ShipItemInventory stInven = null;
			try
			{
				stInven = (ShipItemInventory)q.getSingleResult();
			}
			catch(Exception ig)
			{}
			
			if(stInven == null)
			{
				stInven = new ShipItemInventory();
				stInven.setItem(it);
				stInven.setShip(getShip());
				stInven.setSettings(getSetting());
				stInven.setStock_lvl(0L);
				
				em.persist(stInven);
			}
			else
			{
				stInven.setSettings(getSetting());
				em.merge(stInven);
			}
    		
    		setSetting(null);
    		setSettings(null);
    		
    		messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    	}
    }

	public Ship getShip() {
		if(ship == null)
			ship = new Ship();
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public ArrayList<Ship> getShips() {
		if(ships == null)
		{
			String curUserType = user.getRole().getType();
			if(curUserType.equals("Global") || curUserType.equals("Ship"))
			{
				Query q = null;
				if(curUserType.equals("Global"))
					q = em.createQuery("SELECT e FROM Ship e");
				else
				{
					q = em.createQuery("SELECT e FROM Ship e where id=:id");
					q.setParameter("id", user.getShip().getId());
				}
				ships = (ArrayList<Ship>)q.getResultList();
				if(ships == null)
					ships = new ArrayList<Ship>();
			}
			else
				ships = new ArrayList<Ship>();
		}
		return ships;
	}

	public void setShips(ArrayList<Ship> ships) {
		this.ships = ships;
	}

	public ShipItems getSetting() {
		if(setting == null)
			setting = new ShipItems();
		return setting;
	}

	public void setSetting(ShipItems setting) {
		this.setting = setting;
	}

	public ArrayList<ShipItems> getSettings() {
		if(settings == null)
		{
			if(getShip().getId() != null && getShip().getId() > 0)
			{
				Query q = em.createQuery("SELECT e FROM ShipItems e where ship=:ship");
				q.setParameter("ship", getShip());
				settings = (ArrayList<ShipItems>) q.getResultList();
				if(settings == null)
					settings = new ArrayList<ShipItems>();
			}
			else
				settings = new ArrayList<ShipItems>();
		}
		return settings;
	}

	public void setSettings(ArrayList<ShipItems> settings) {
		this.settings = settings;
	}

	public ShipItemInventory getItemInventory() {
		if(itemInventory == null)
			itemInventory = new ShipItemInventory();
		return itemInventory;
	}

	public void setItemInventory(ShipItemInventory itemInventory) {
		this.itemInventory = itemInventory;
	}

	public ArrayList<ShipItemInventory> getShipInventory() {
		if(shipInventory == null)
		{
			if(getShip().getId() != null && getShip().getId() > 0)
			{
				Query q = em.createQuery("SELECT e FROM ShipItemInventory e where ship=:ship");
				q.setParameter("ship", getShip());
				shipInventory = (ArrayList<ShipItemInventory>) q.getResultList();
				if(shipInventory == null)
					shipInventory = new ArrayList<ShipItemInventory>();
			}
			else
				shipInventory = new ArrayList<ShipItemInventory>();
		}
		return shipInventory;
	}

	public void setShipInventory(ArrayList<ShipItemInventory> shipInventory) {
		this.shipInventory = shipInventory;
	}

	public ShipRequest getShipRequest() {
		if(shipRequest == null)
			shipRequest = new ShipRequest();
		return shipRequest;
	}

	public void setShipRequest(ShipRequest shipRequest) {
		this.shipRequest = shipRequest;
	}

	public ArrayList<ShipRequest> getShipRequests() {
		if(shipRequests == null)
			shipRequests = new ArrayList<ShipRequest>();
		return shipRequests;
	}

	public void setShipRequests(ArrayList<ShipRequest> shipRequests) {
		this.shipRequests = shipRequests;
	}

	public ArrayList<ShipRequest> getFocPendingRequests() {
		if(focPendingRequests == null)
		{
			Query q = em.createQuery("SELECT e FROM ShipRequest e where approvedBy=:approvedBy and approvalDate = null");
			q.setParameter("approvedBy", user);
			focPendingRequests = (ArrayList<ShipRequest>) q.getResultList();
			if(focPendingRequests == null)
				focPendingRequests = new ArrayList<ShipRequest>();
		}
		return focPendingRequests;
	}

	public void setFocPendingRequests(ArrayList<ShipRequest> focPendingRequests) {
		this.focPendingRequests = focPendingRequests;
	}

	public ArrayList<ShipRequest> getFocRequests() {
		if(focRequests == null)
		{
			Query q = em.createQuery("SELECT e FROM ShipRequest e where approvedBy=:approvedBy");
			q.setParameter("approvedBy", user);
			focRequests = (ArrayList<ShipRequest>) q.getResultList();
			if(focRequests == null)
				focRequests = new ArrayList<ShipRequest>();
		}
		return focRequests;
	}

	public void setFocRequests(ArrayList<ShipRequest> focRequests) {
		this.focRequests = focRequests;
	}

	public ArrayList<ShipRequest> getSpPendingRequests() {
		if(spPendingRequests == null)
		{
			Query q = em.createQuery("SELECT e FROM ShipRequest e where issuedBy=:issuedBy and approvalDate != null and issuedDate = null");
			q.setParameter("issuedBy", user);
			spPendingRequests = (ArrayList<ShipRequest>) q.getResultList();
			if(spPendingRequests == null)
				spPendingRequests = new ArrayList<ShipRequest>();
		}
		return spPendingRequests;
	}

	public void setSpPendingRequests(ArrayList<ShipRequest> spPendingRequests) {
		this.spPendingRequests = spPendingRequests;
	}

	public ArrayList<ShipRequest> getSpsPendingRequests() {
		if(spsPendingRequests == null)
		{
			Query q = em.createQuery("SELECT e FROM ShipRequest e where ship=:ship and receivedDate = null");
			q.setParameter("ship", user.getShip());
			spsPendingRequests = (ArrayList<ShipRequest>) q.getResultList();
			if(spsPendingRequests == null)
				spsPendingRequests = new ArrayList<ShipRequest>();
		}
		return spsPendingRequests;
	}

	public void setSpsPendingRequests(ArrayList<ShipRequest> spsPendingRequests) {
		this.spsPendingRequests = spsPendingRequests;
	}

	public ArrayList<ShipRequest> getSpRequests() {
		if(spRequests == null)
		{
			Query q = em.createQuery("SELECT e FROM ShipRequest e where issuedBy=:issuedBy");
			q.setParameter("issuedBy", user);
			spRequests = (ArrayList<ShipRequest>) q.getResultList();
			if(spRequests == null)
				spRequests = new ArrayList<ShipRequest>();
		}
		return spRequests;
	}

	public void setSpRequests(ArrayList<ShipRequest> spRequests) {
		this.spRequests = spRequests;
	}

	public ArrayList<ShipRequest> getSpsRequests() {
		if(spsRequests == null)
		{
			Query q = em.createQuery("SELECT e FROM ShipRequest e where ship=:ship");
			q.setParameter("ship", user.getShip());
			spsRequests = (ArrayList<ShipRequest>) q.getResultList();
			if(spsRequests == null)
				spsRequests = new ArrayList<ShipRequest>();
		}
		return spsRequests;
	}

	public void setSpsRequests(ArrayList<ShipRequest> spsRequests) {
		this.spsRequests = spsRequests;
	}

	public ShipRequestItems getShipRequestItem() {
		if(shipRequestItem == null)
			shipRequestItem = new ShipRequestItems();
		return shipRequestItem;
	}

	public void setShipRequestItem(ShipRequestItems shipRequestItem) {
		this.shipRequestItem = shipRequestItem;
	}

	public ArrayList<ShipRequestItems> getShipRequestItems() {
		if(shipRequestItems == null)
			shipRequestItems = new ArrayList<ShipRequestItems>();
		return shipRequestItems;
	}

	public void setShipRequestItems(ArrayList<ShipRequestItems> shipRequestItems) {
		this.shipRequestItems = shipRequestItems;
	}

	public ArrayList<ShipRequestItems> getSelRequestItems() {
		if(selRequestItems == null)
			selRequestItems = new ArrayList<ShipRequestItems>();
		
		boolean doSearch = true;
		for(ShipRequestItems e : selRequestItems)
		{
			if(getShipRequest().getId() != null && e.getRequest().getId().longValue() == getShipRequest().getId().longValue())
			{
				doSearch=false;
				break;
			}
		}
		
		if(doSearch)
		{
			if(getShipRequest().getId() != null)
			{
				Query q = em.createQuery("SELECT e FROM ShipRequestItems e where request=:request");
				q.setParameter("request", getShipRequest());
				selRequestItems = (ArrayList<ShipRequestItems>) q.getResultList();
				if(selRequestItems == null)
					selRequestItems = new ArrayList<ShipRequestItems>();
			}
		}
		
		return selRequestItems;
	}

	public void setSelRequestItems(ArrayList<ShipRequestItems> selRequestItems) {
		this.selRequestItems = selRequestItems;
	}

	public Long getItem_id() {
		return item_id;
	}

	public void setItem_id(Long item_id) {
		this.item_id = item_id;
	}

	public Long getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(Long requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

	public String getFoc_username() {
		return foc_username;
	}

	public void setFoc_username(String foc_username) {
		this.foc_username = foc_username;
	}

	public String getIssuer_username() {
		return issuer_username;
	}

	public void setIssuer_username(String issuer_username) {
		this.issuer_username = issuer_username;
	}

	public Long getWarehouse_id() {
		return warehouse_id;
	}

	public void setWarehouse_id(Long warehouse_id) {
		this.warehouse_id = warehouse_id;
	}

	public ArrayList<WarehouseSpareparts> getWarehouseParts() {
		if(warehouseParts == null)
			warehouseParts = new ArrayList<WarehouseSpareparts>();
		
		return warehouseParts;
	}

	public void setWarehouseParts(ArrayList<WarehouseSpareparts> warehouseParts) {
		this.warehouseParts = warehouseParts;
	}
	
	public void loadItems()
	{
		boolean doSearch = true;
		
		Hashtable<Long, ArrayList<WarehouseSpareparts>> warehouseHashParts = null;
		if(getItemSpareparts().get(getItem().getId()) != null)
		{
			warehouseHashParts = getItemSpareparts().get(getItem().getId());
		}
		else
		{
			warehouseHashParts = new Hashtable<Long, ArrayList<WarehouseSpareparts>>();
		}
		
		if(getWarehouse_id() != null && warehouseHashParts.get(getWarehouse_id()) != null)
		{
			warehouseParts = warehouseHashParts.get(getWarehouse_id());
			doSearch = false;
		}
		
		if(doSearch && getWarehouse_id() != null && getItem().getId() != null)
		{
			for(WarehouseSpareparts e : warehouseParts)
			{
				if(e.getWarehouse().getId().longValue() == getWarehouse_id().longValue())
					doSearch = false;
			}
			
			if(doSearch)
			{
				Warehouse w = em.find(Warehouse.class, getWarehouse_id());
				Query q = em.createQuery("SELECT e FROM WarehouseSpareparts e where item=:item and warehouse=:warehouse and active=1");
				q.setParameter("item", getItem());
				q.setParameter("warehouse", w);
				warehouseParts = (ArrayList<WarehouseSpareparts>) q.getResultList();
				if(warehouseParts == null)
					warehouseParts = new ArrayList<WarehouseSpareparts>();
			}
		}
	}
	
	public void addItems()
	{
		Hashtable<Long, ArrayList<WarehouseSpareparts>> warehouseHashParts = null;
		if(getItemSpareparts().get(getItem().getId()) != null)
		{
			warehouseHashParts = getItemSpareparts().get(getItem().getId());
		}
		else
		{
			warehouseHashParts = new Hashtable<Long, ArrayList<WarehouseSpareparts>>();
		}
		
		warehouseHashParts.put(getWarehouse_id(), warehouseParts);
		
		getItemSpareparts().put(getItem().getId(), warehouseHashParts);
	}
	
	public ArrayList<String[]> getItemWarehousesAddedCount()
	{
		ArrayList<String[]> list = new ArrayList<String[]>();
		
		if(getItem().getId() != null)
		{
			if(getItemSpareparts().get(getItem().getId()) != null)
			{
				Hashtable<Long, ArrayList<WarehouseSpareparts>> warehouses = getItemSpareparts().get(getItem().getId());
				getSelItemRequest().setIssuedAmount(0L);
				Enumeration<Long> w_id_enum = warehouses.keys();
				while(w_id_enum.hasMoreElements())
				{
					String[] rec = new String[2];
					
					Long w_id = w_id_enum.nextElement();
					
					Warehouse w = em.find(Warehouse.class, w_id);
					rec[0] = w.getName() + "(" + w.getCode() + ")";
					ArrayList<WarehouseSpareparts> w_items = warehouses.get(w_id);
					Long selCount = 0L;
					for(WarehouseSpareparts e : w_items)
					{
						if(e.isSelected())
							selCount++;
					}
					rec[1] = ""+selCount;
					getSelItemRequest().setIssuedAmount(getSelItemRequest().getIssuedAmount() + selCount);
					for(ShipRequestItems e : getSelRequestItems())
			    	{
			    		if(e.getId().longValue() == getSelItemRequest().getId().longValue())
			    		{
			    			e.setIssuedAmount(getSelItemRequest().getIssuedAmount());
			    			break;
			    		}
			    	}
					list.add(rec);
				}
				
			}
		}
		
		return list;
	}

	public Hashtable<Long, Hashtable<Long, ArrayList<WarehouseSpareparts>>> getItemSpareparts() {
		if(itemSpareparts == null)
			itemSpareparts = new Hashtable<Long, Hashtable<Long, ArrayList<WarehouseSpareparts>>>();
		return itemSpareparts;
	}

	public void setItemSpareparts(
			Hashtable<Long, Hashtable<Long, ArrayList<WarehouseSpareparts>>> itemSpareparts) {
		this.itemSpareparts = itemSpareparts;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ShipRequestItems getSelItemRequest() {
		if(selItemRequest == null)
			selItemRequest = new ShipRequestItems();
		return selItemRequest;
	}

	public void setSelItemRequest(ShipRequestItems selItemRequest) {
		this.selItemRequest = selItemRequest;
	}
	
	private void WarehouseInventoryCheck(Warehouse w, Item t)
	{
		Query q = em.createQuery("SELECT COUNT(e) FROM WarehouseSpareparts e where item=:item and warehouse=:warehouse and active=1");
		q.setParameter("item", t);
		q.setParameter("warehouse", w);
		Long count = (Long)q.getSingleResult();
		
		q = em.createQuery("SELECT e FROM WarehouseItemInventory e where item=:item and warehouse=:warehouse");
		q.setParameter("item", t);
		q.setParameter("warehouse", w);
		
		WarehouseItemInventory wiInven = (WarehouseItemInventory)q.getSingleResult();
		wiInven.setStock_lvl(count);
		
		em.merge(wiInven);
		
		q = em.createQuery("SELECT e FROM WarehouseItems e where item=:item and warehouse=:warehouse");
		q.setParameter("item", t);
		q.setParameter("warehouse", w);
		
		try
		{
			WarehouseItems setting = (WarehouseItems)q.getSingleResult();
			
			boolean reorder = false, min_reach = false;
			if(count.longValue() <= setting.getReorder_lvl().longValue())
				reorder = true;
			
			if(count.longValue() <= setting.getMin_lvl().longValue())
				min_reach = true;
			
			if(reorder)
			{
				final StringBuilder bodyBuilder = new StringBuilder();
	    		bodyBuilder.append("<html><body>");
	    		bodyBuilder.append("<p>Dear " + user.getFirstname() + "</p><br/>");
	    		bodyBuilder.append("<p>The item: " + t.getName() + "(" + t.getCode() + ") in warehouse: " + w.getName() + "(" + w.getCode() + ") stock level has either reached or passed its re-order level.</p><br/>");
	    		if(min_reach)
	    			bodyBuilder.append("<p>This item has also reached or passed its min level in the warehouse.");
	    		
	    		bodyBuilder.append("<p>");
	    		bodyBuilder.append("<table border=\"0\">");
	    		bodyBuilder.append("<caption>Item Stock Level</caption>");
	    		bodyBuilder.append("<tr><td>Item</td><td>" + t.getName() + "</td></tr>");
	    		bodyBuilder.append("<tr><td>Warehouse</td><td>" + w.getName() + "</td></tr>");
	    		bodyBuilder.append("<tr><td>Stock Level</td><td>" + count + "</td></tr>");
	    		bodyBuilder.append("</table>");
	    		bodyBuilder.append("</p><br/>");
	    		bodyBuilder.append("<p>Please login to the Navy Logistics site to attend to this.</p>");
	    		bodyBuilder.append("</body></html>");
	    		
	    		final StringBuilder smsbodyBuilder = new StringBuilder();
	    		smsbodyBuilder.append("The item: " + t.getName() + "(" + t.getCode() + ") in warehouse: " + w.getName() + "(" + w.getCode() + ") stock level has either reached or passed its re-order " + ((min_reach) ? " and min " : "") + "level.");
	    		
	    		Thread trd1 = new Thread()
	    		{
	    			public void run()
	    			{
	    				EmailNotifier.sendEmail("donotreply@navlg.com", user.getEmail(), "Request notification", bodyBuilder.toString());
	    			}
	    		};
	    		
	    		Thread trd2 = new Thread()
	    		{
	    			public void run()
	    			{
	    				SMSNotifier.SendSMS3(user.getPhone(), smsbodyBuilder.toString());
	    			}
	    		};
	    		
	    		trd1.start();
	    		trd2.start();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public ArrayList<ShipSparepart> getReceiveSpareparts() {
		if(receiveSpareparts == null)
		{
			if(getShipRequest().getId() != null)
			{
				Query q = em.createQuery("SELECT e FROM ShipSparepart e where shipRequest=:shipRequest and active=0");
				q.setParameter("shipRequest", getShipRequest());
				
				receiveSpareparts = (ArrayList<ShipSparepart>)q.getResultList();
			}
			if(receiveSpareparts == null)
				receiveSpareparts = new ArrayList<ShipSparepart>();
		}
		return receiveSpareparts;
	}

	public void setReceiveSpareparts(ArrayList<ShipSparepart> receiveSpareparts) {
		this.receiveSpareparts = receiveSpareparts;
	}

	public ArrayList<ShipSparepart> getShipItemSpareparts() {
		if(shipItemSpareparts == null)
		{
			if(getItemInventory().getId() != null)
			{
				Query q = em.createQuery("SELECT e FROM ShipSparepart e where item=:item and ship=:ship and active=1");
				q.setParameter("item", getItemInventory().getItem());
				q.setParameter("ship", getItemInventory().getShip());
				
				shipItemSpareparts = (ArrayList<ShipSparepart>)q.getResultList();
			}
			if(shipItemSpareparts == null)
				shipItemSpareparts = new ArrayList<ShipSparepart>();
		}
		return shipItemSpareparts;
	}

	public void setShipItemSpareparts(ArrayList<ShipSparepart> shipItemSpareparts) {
		this.shipItemSpareparts = shipItemSpareparts;
	}

	public Date getUseDate() {
		return useDate;
	}

	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}

	public String getUseRemark() {
		return useRemark;
	}

	public void setUseRemark(String useRemark) {
		this.useRemark = useRemark;
	}
	
	private void ShipInventoryCheck(Ship s, Item t)
	{
		Query q = em.createQuery("SELECT COUNT(e) FROM ShipSparepart e where item=:item and ship=:ship and active=1");
		q.setParameter("item", t);
		q.setParameter("ship", s);
		Long count = (Long)q.getSingleResult();
		
		q = em.createQuery("SELECT e FROM ShipItemInventory e where item=:item and ship=:ship");
		q.setParameter("item", t);
		q.setParameter("ship", s);
		
		ShipItemInventory siInven = (ShipItemInventory)q.getSingleResult();
		siInven.setStock_lvl(count);
		
		em.merge(siInven);
		
		q = em.createQuery("SELECT e FROM ShipItems e where item=:item and ship=:ship");
		q.setParameter("item", t);
		q.setParameter("ship", s);
		
		try
		{
			ShipItems setting = (ShipItems)q.getSingleResult();
			
			boolean reorder = false, min_reach = false;
			if(count.longValue() <= setting.getReorder_lvl().longValue())
				reorder = true;
			
			if(count.longValue() <= setting.getMin_lvl().longValue())
				min_reach = true;
			
			if(reorder)
			{
				final StringBuilder bodyBuilder = new StringBuilder();
	    		bodyBuilder.append("<html><body>");
	    		bodyBuilder.append("<p>Dear " + user.getFirstname() + "</p><br/>");
	    		bodyBuilder.append("<p>The item: " + t.getName() + "(" + t.getCode() + ") in ship: " + s.getName() + "(" + s.getCode() + ") stock level has either reached or passed its re-order level.</p><br/>");
	    		if(min_reach)
	    			bodyBuilder.append("<p>This item has also reached or passed its min level in the ship.");
	    		
	    		bodyBuilder.append("<p>");
	    		bodyBuilder.append("<table border=\"0\">");
	    		bodyBuilder.append("<caption>Item Stock Level</caption>");
	    		bodyBuilder.append("<tr><td>Item</td><td>" + t.getName() + "</td></tr>");
	    		bodyBuilder.append("<tr><td>Ship</td><td>" + s.getName() + "</td></tr>");
	    		bodyBuilder.append("<tr><td>Stock Level</td><td>" + count + "</td></tr>");
	    		bodyBuilder.append("</table>");
	    		bodyBuilder.append("</p><br/>");
	    		bodyBuilder.append("<p>Please login to the Navy Logistics site to attend to this.</p>");
	    		bodyBuilder.append("</body></html>");
	    		
	    		final StringBuilder smsbodyBuilder = new StringBuilder();
	    		smsbodyBuilder.append("The item: " + t.getName() + "(" + t.getCode() + ") in ship: " + s.getName() + "(" + s.getCode() + ") stock level has either reached or passed its re-order " + ((min_reach) ? " and min " : "") + "level.");
	    		
	    		Thread trd1 = new Thread()
	    		{
	    			public void run()
	    			{
	    				EmailNotifier.sendEmail("donotreply@navlg.com", user.getEmail(), "Request notification", bodyBuilder.toString());
	    			}
	    		};
	    		
	    		Thread trd2 = new Thread()
	    		{
	    			public void run()
	    			{
	    				SMSNotifier.SendSMS3(user.getPhone(), smsbodyBuilder.toString());
	    			}
	    		};
	    		
	    		trd1.start();
	    		trd2.start();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public Date getUseDate1() {
		return useDate1;
	}

	public void setUseDate1(Date useDate1) {
		this.useDate1 = useDate1;
	}

	public Date getUseDate2() {
		return useDate2;
	}

	public void setUseDate2(Date useDate2) {
		this.useDate2 = useDate2;
	}

	public ArrayList<ShipItemsUseItem> getShipItemsUseItems() {
		if(shipItemsUseItems == null)
			shipItemsUseItems = new ArrayList<ShipItemsUseItem>();
		return shipItemsUseItems;
	}

	public void setShipItemsUseItems(ArrayList<ShipItemsUseItem> shipItemsUseItems) {
		this.shipItemsUseItems = shipItemsUseItems;
	}

	public ArrayList<ShipItemsUse> getShipItemsUseList() {
		if(shipItemsUseList == null)
			shipItemsUseList = new ArrayList<ShipItemsUse>();
		return shipItemsUseList;
	}

	public void setShipItemsUseList(ArrayList<ShipItemsUse> shipItemsUseList) {
		this.shipItemsUseList = shipItemsUseList;
	}
	
}
