package com.dexter.navlg.bean;

import java.util.ArrayList;

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
import com.dexter.navlg.model.ItemSupply;
import com.dexter.navlg.model.Location;
import com.dexter.navlg.model.ShipItems;
import com.dexter.navlg.model.User;
import com.dexter.navlg.model.Warehouse;
import com.dexter.navlg.model.WarehouseItemInventory;
import com.dexter.navlg.model.WarehouseItems;
import com.dexter.navlg.model.WarehouseSpareparts;
import com.dexter.navlg.security.Authenticated;

@Stateful
@SessionScoped
@Named("warehouseBean")
public class WarehouseBean
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
    
    private Long location_id;
    private Location location;
    private Warehouse warehouse;
    private ArrayList<Warehouse> warehousesList;
    
    private WarehouseItems setting;
    private ArrayList<WarehouseItems> settings;
    
    private WarehouseItemInventory itemInventory;
    private ArrayList<WarehouseItemInventory> warehouseItemsInventory;
    
    private ItemSupply itemSupply;
    private ArrayList<Item> selectedItems;
    private WarehouseItemInventory selItemInventorySettings;
    
    private Item item;
    private ArrayList<WarehouseSpareparts> itemSpareparts;
    
    public String movetoupdate(Long id)
    {
    	for(Warehouse e : getWarehousesList())
    	{
    		if(e.getId().longValue() == id.longValue())
    		{
    			setWarehouse(e);
    			break;
    		}
    	}
    	setLocation_id(getWarehouse().getLocation().getId());
    	return "warehouse_edit";
    }
    
    public String movetosettings(Long warehouse_id)
    {
    	setWarehouse(em.find(Warehouse.class, warehouse_id));
    	setSetting(null);
    	setSettings(null);
    	return "warehousesettings";
    }
    
    public String movetoinventory(Long warehouse_id)
    {
    	setWarehouse(em.find(Warehouse.class, warehouse_id));
    	setItemInventory(null);
    	setWarehouseItemsInventory(null);
    	return "warehouseinventory";
    }
    
    public void SaveUpdateSupply()
    {
    	if((getItemSupply().getItem().getId() != null && getItemSupply().getItem().getId() > 0)  || 
    			(getItemSupply().getItem().getBarcode() != null && !getItemSupply().getItem().getBarcode().isEmpty()))
    	{
    		Item itm = null;
			if(getItemSupply().getItem().getId() != null && getItemSupply().getItem().getId() > 0)
			{
				itm = em.find(Item.class, getItemSupply().getItem().getId());
			}
			else if(getItemSupply().getItem().getBarcode() != null && !getItemSupply().getItem().getBarcode().isEmpty())
			{
				Query q = em.createQuery("Select e from Item e where barcode = :barcode");
				q.setParameter("barcode", getItemSupply().getItem().getBarcode());
				itm = (Item) q.getSingleResult();
			}
			
			if(itm != null)
			{
				getItemSupply().setItem(itm);
				
				Warehouse w = em.find(Warehouse.class, getItemSupply().getWarehouse().getId());
				getItemSupply().setWarehouse(w);
				getItemSupply().setUser(user);
				
				Query q = em.createQuery("Select e from WarehouseItemInventory e where warehouse =:warehouse and item = :item");
				q.setParameter("warehouse", w);
				q.setParameter("item", itm);
				
				WarehouseItemInventory wtInven = new WarehouseItemInventory();
				try
				{
					wtInven = (WarehouseItemInventory)q.getSingleResult();
				}
				catch(Exception ig)
				{
					wtInven = new WarehouseItemInventory();
					wtInven.setItem(itm);
					wtInven.setWarehouse(w);
				}
				
				if(wtInven.getSettings().getId() == null)
				{
					q = em.createQuery("Select e from WarehouseItems e where warehouse =:warehouse and item = :item");
					q.setParameter("warehouse", w);
					q.setParameter("item", itm);
					
					WarehouseItems wtSettings = null;
					try
					{
						wtSettings = (WarehouseItems)q.getSingleResult();
					}
					catch(Exception ig){}
					if(wtSettings != null)
						wtInven.setSettings(wtSettings);
				}
				
				try
				{
					wtInven.setStock_lvl((wtInven.getStock_lvl() != null) ? wtInven.getStock_lvl()+getItemSupply().getAmount() : getItemSupply().getAmount());
					em.persist(getItemSupply());
					
					if(wtInven.getId() != null)
						em.merge(wtInven);
					else
						em.persist(wtInven);
					
					// now we insert the spare parts. These are the supposed individual items that we supplied.
					for(int i=0; i<getItemSupply().getAmount(); i++)
					{
						WarehouseSpareparts wsp = new WarehouseSpareparts();
						wsp.setActive(true);
						wsp.setItem(itm);
						wsp.setWarehouse(w);
						wsp.setItemSupply(getItemSupply());
						
						em.persist(wsp);
						
						wsp.setSerial_number("" + wsp.getId().longValue());
						wsp.setCustomized_number(itm.getBarcode() + "-" + wsp.getSerial_number());
						
						em.merge(wsp);
					}
					
					setSelItemInventorySettings(null);
					setSelectedItems(null);
					setItemSupply(null);
					setWarehousesList(null);
					
					messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
    	}
    }
    
    public void SaveUpdateSetting()
    {
    	if(getSetting().getItem().getId() != null)
    	{
    		Item it = em.find(Item.class, getSetting().getItem().getId());
    		getSetting().setItem(it);
    		if(getSetting().getWarehouse().getName() == null)
    			getSetting().setWarehouse(getWarehouse());
    		
    		Query q = em.createQuery("Select e from WarehouseItems e where warehouse=:warehouse and item = :item");
			q.setParameter("warehouse", getWarehouse());
			q.setParameter("item", it);
			
			try
			{
				WarehouseItems setting = (WarehouseItems) q.getSingleResult();
				if(setting != null)
					getSetting().setId(setting.getId());
			}
			catch(Exception ex)
			{}
    		
    		if(getSetting().getId() != null && getSetting().getId() > 0)
    			em.merge(getSetting());
    		else
    			em.persist(getSetting());
    			
    		q = em.createQuery("Select e from WarehouseItemInventory e where warehouse =:warehouse and item = :item");
			q.setParameter("warehouse", getWarehouse());
			q.setParameter("item", it);
			
			WarehouseItemInventory wtInven = null;
			try
			{
				wtInven = (WarehouseItemInventory)q.getSingleResult();
			}
			catch(Exception ig)
			{}
			
			if(wtInven == null)
			{
				wtInven = new WarehouseItemInventory();
				wtInven.setItem(it);
				wtInven.setWarehouse(getWarehouse());
				wtInven.setSettings(getSetting());
				wtInven.setStock_lvl(0L);
				
				em.persist(wtInven);
			}
			else
			{
				wtInven.setSettings(getSetting());
				em.merge(wtInven);
			}
    		
    		setSetting(null);
    		setSettings(null);
    		
    		messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    	}
    }
    
    public void SaveUpdate()
    {
    	if(getLocation_id() != null)
    	{
    		Location l = em.find(Location.class, getLocation_id());
    		getWarehouse().setLocation(l);
    	}
    	
    	if(getWarehouse().getName() != null && getWarehouse().getLocation() != null)
    	{
    		try
    		{
	    		if(getWarehouse().getId() != null && getWarehouse().getId() > 0)
	    			em.merge(getWarehouse());
	    		else
	    		{
	    			em.persist(getWarehouse());
	    			getWarehouse().getLocation().setWarehouseCount(getWarehouse().getLocation().getWarehouseCount() + 1);
	    			em.merge(getWarehouse().getLocation());
	    		}
	    		
    			setLocation(null);
    			setWarehouse(null);
    			setWarehousesList(null);
    			
    			messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
    		}
    	}
    	else
    	{
    		messages.error(new AppBundleKey("record.invalid")).defaults("Invalid data entered!");
    	}
    }

	public Long getLocation_id() {
		return location_id;
	}

	public void setLocation_id(Long location_id) {
		this.location_id = location_id;
	}

	public Location getLocation() {
		if(location == null)
			location = new Location();
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Warehouse getWarehouse() {
		if(warehouse == null)
			warehouse = new Warehouse();
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public ArrayList<Warehouse> getWarehousesList() {
		if(warehousesList == null)
		{
			Query q = null;
			if(getLocation().getId() == null) // get all
			{
				String curUserType = user.getRole().getType();
				if(curUserType.equals("Global") || curUserType.equals("Warehouse"))
				{
					if(curUserType.equals("Global"))
						q = em.createQuery("SELECT e FROM Warehouse e");
					else
					{
						q = em.createQuery("SELECT e FROM Warehouse e where id=:id");
						q.setParameter("id", user.getWarehouse().getId());
					}
					warehousesList = (ArrayList<Warehouse>) q.getResultList();
				}
				else
					warehousesList = new ArrayList<Warehouse>();
			}
			else // get by location
			{
				String curUserType = user.getRole().getType();
				if(curUserType.equals("Global") || curUserType.equals("Warehouse"))
				{
					if(curUserType.equals("Global"))
					{
						q = em.createQuery("SELECT e FROM Warehouse e where location=:location");
						q.setParameter("location", getLocation());
					}
					else
					{
						q = em.createQuery("SELECT e FROM Warehouse e where location=:location and id=:id");
						q.setParameter("location", getLocation());
						q.setParameter("id", user.getWarehouse().getId());
					}
					
					warehousesList = (ArrayList<Warehouse>) q.getResultList();
				}
				else
					warehousesList = new ArrayList<Warehouse>();
			}
			if(warehousesList == null)
				warehousesList = new ArrayList<Warehouse>();
		}
		return warehousesList;
	}

	public void setWarehousesList(ArrayList<Warehouse> warehousesList) {
		this.warehousesList = warehousesList;
	}

	public WarehouseItems getSetting() {
		if(setting == null)
			setting = new WarehouseItems();
		return setting;
	}

	public void setSetting(WarehouseItems setting) {
		this.setting = setting;
	}

	public ArrayList<WarehouseItems> getSettings() {
		if(settings == null)
		{
			if(getWarehouse().getId() != null && getWarehouse().getId() > 0)
			{
				Query q = em.createQuery("SELECT e FROM WarehouseItems e where warehouse=:warehouse");
				q.setParameter("warehouse", getWarehouse());
				settings = (ArrayList<WarehouseItems>) q.getResultList();
				if(settings == null)
					settings = new ArrayList<WarehouseItems>();
			}
			else
				settings = new ArrayList<WarehouseItems>();
		}
		return settings;
	}

	public void setSettings(ArrayList<WarehouseItems> settings) {
		this.settings = settings;
	}

	public WarehouseItemInventory getItemInventory() {
		if(itemInventory == null)
			itemInventory = new WarehouseItemInventory();
		return itemInventory;
	}

	public void setItemInventory(WarehouseItemInventory itemInventory) {
		this.itemInventory = itemInventory;
	}

	public ArrayList<WarehouseItemInventory> getWarehouseItemsInventory() {
		if(warehouseItemsInventory == null)
		{
			if(getWarehouse().getId() != null && getWarehouse().getId() > 0)
			{
				Query q = em.createQuery("SELECT e FROM WarehouseItemInventory e where warehouse=:warehouse");
				q.setParameter("warehouse", getWarehouse());
				warehouseItemsInventory = (ArrayList<WarehouseItemInventory>) q.getResultList();
				if(warehouseItemsInventory == null)
					warehouseItemsInventory = new ArrayList<WarehouseItemInventory>();
			}
			else
				warehouseItemsInventory = new ArrayList<WarehouseItemInventory>();
		}
		return warehouseItemsInventory;
	}

	public void setWarehouseItemsInventory(
			ArrayList<WarehouseItemInventory> warehouseItemsInventory) {
		this.warehouseItemsInventory = warehouseItemsInventory;
	}

	public ItemSupply getItemSupply() {
		if(itemSupply == null)
			itemSupply = new ItemSupply();
		return itemSupply;
	}

	public void setItemSupply(ItemSupply itemSupply) {
		this.itemSupply = itemSupply;
	}

	public ArrayList<Item> getSelectedItems() {
		boolean doSearch = true;
		if(selectedItems != null && selectedItems.size() > 0)
		{
			if(getItemSupply().getItem().getId() != null && selectedItems.get(0).getId().longValue() == getItemSupply().getItem().getId().longValue())
			{
				doSearch = false;
			}
			else if(getItemSupply().getItem().getBarcode() != null && selectedItems.get(0).getBarcode().equals(getItemSupply().getItem().getBarcode()))
			{
				doSearch = false;
			}
		}
		
		if(doSearch)
		{
			selectedItems = new ArrayList<Item>();
			
			if(getItemSupply().getItem().getBarcode() != null || getItemSupply().getItem().getId() != null)
			{
				Item itm = null;
				if(getItemSupply().getItem().getId() != null && getItemSupply().getItem().getId() > 0)
				{
					itm = em.find(Item.class, getItemSupply().getItem().getId());
				}
				else if(getItemSupply().getItem().getBarcode() != null && !getItemSupply().getItem().getBarcode().isEmpty())
				{
					Query q = em.createQuery("Select e from Item e where barcode = :barcode");
					q.setParameter("barcode", getItemSupply().getItem().getBarcode());
					itm = (Item) q.getSingleResult();
				}
				
				if(itm != null)
				{
					selectedItems.add(itm);
					Warehouse w = em.find(Warehouse.class, getItemSupply().getWarehouse().getId());
					Query q = em.createQuery("Select e from WarehouseItemInventory e where warehouse=:warehouse and item=:item");
					q.setParameter("warehouse", w);
					q.setParameter("item", itm);
					try
					{
						selItemInventorySettings = (WarehouseItemInventory)q.getSingleResult();
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
						selItemInventorySettings = new WarehouseItemInventory();
					}
				}
			}
		}
		return selectedItems;
	}

	public void setSelectedItems(ArrayList<Item> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public WarehouseItemInventory getSelItemInventorySettings() {
		if(selItemInventorySettings == null)
			selItemInventorySettings = new WarehouseItemInventory();
		return selItemInventorySettings;
	}

	public void setSelItemInventorySettings(
			WarehouseItemInventory selItemInventorySettings) {
		this.selItemInventorySettings = selItemInventorySettings;
	}
	
	public String selectItemForSpareparts(Long item_id)
	{
		setItem(em.find(Item.class, item_id));
		setItemSpareparts(null);
		return "warehousespareparts";
	}

	public Item getItem() {
		if(item == null)
			item = new Item();
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ArrayList<WarehouseSpareparts> getItemSpareparts() {
		if(itemSpareparts == null)
		{
			Query q = em.createQuery("Select e from WarehouseSpareparts e where warehouse =:warehouse and item = :item and active=:active");
			q.setParameter("warehouse", getWarehouse());
			q.setParameter("item", getItem());
			q.setParameter("active", Boolean.TRUE);
			
			try
			{
				itemSpareparts = (ArrayList<WarehouseSpareparts>)q.getResultList();
			}
			catch(Exception ex)
			{}
		}
		return itemSpareparts;
	}

	public void setItemSpareparts(ArrayList<WarehouseSpareparts> itemSpareparts) {
		this.itemSpareparts = itemSpareparts;
	}
    
}
