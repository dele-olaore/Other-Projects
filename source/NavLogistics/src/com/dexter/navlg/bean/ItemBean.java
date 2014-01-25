package com.dexter.navlg.bean;

import java.util.ArrayList;
import java.util.Random;

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
import com.dexter.navlg.model.ItemReport;
import com.dexter.navlg.model.ItemSupply;
import com.dexter.navlg.model.Supplier;
import com.dexter.navlg.model.User;
import com.dexter.navlg.model.Warehouse;
import com.dexter.navlg.model.WarehouseItemInventory;
import com.dexter.navlg.security.Authenticated;

@Stateful
@SessionScoped
@Named("itemBean")
public class ItemBean
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
    
    private Item item;
    private ArrayList<Item> items;
    
    private Long warehouse_id;
    private ArrayList<ItemReport> report;
    
    public String movetoitemsreport()
    {
    	setReport(null);
    	
    	return "items_report";
    }
    
    public void generateBarcode()
    {
    	if(getItem().getSupplier().getId() != null && getItem().getSupplier().getId() > 0 &&
    			getItem().getCode() != null && getItem().getCode().length() == 3)
    	{
    		int[] digits = new int[]{0,1,2,3,4,5,6,7,8,9};
    		
    		Supplier sp = em.find(Supplier.class, getItem().getSupplier().getId());
    		
    		Random rd = new Random();
    		String str = "";
    		for(int i=0; i<4; i++)
    		{
    			str += ""+digits[rd.nextInt(digits.length)];
    		}
    		
    		String barcode = sp.getCode() + "-" + getItem().getCode() + "-" + str;
        	getItem().setBarcode(barcode);
        	
        	messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    	}
    	else
    	{
    		messages.error(new AppBundleKey("record.invalid")).defaults("Invalid data entered!");
    	}
    }
    
    public void SaveUpdate()
    {
    	if(getItem().getName() != null)
    	{
    		Supplier sp = em.find(Supplier.class, getItem().getSupplier().getId());
    		getItem().setSupplier(sp);
    		
    		if(getItem().getId() != null && getItem().getId() > 0)
    			em.merge(getItem());
    		else
    			em.persist(getItem());
    		
    		setItem(null);
    		setItems(null);
    		
    		messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    	}
    	else
    	{
    		messages.error(new AppBundleKey("record.invalid")).defaults("Invalid data entered!");
    	}
    }

	public Item getItem() {
		if(item == null)
			item = new Item();
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ArrayList<Item> getItems() {
		if(items == null)
		{
			Query q = em.createQuery("SELECT e FROM Item e");
			items = (ArrayList<Item>) q.getResultList();
			if(items == null)
				items = new ArrayList<Item>();
		}
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public Long getWarehouse_id() {
		return warehouse_id;
	}

	public void setWarehouse_id(Long warehouse_id) {
		this.warehouse_id = warehouse_id;
	}

	public ArrayList<ItemReport> getReport() {
		if(report == null)
		{
			if(getWarehouse_id() != null && getWarehouse_id() > 0)
			{
				Warehouse w = em.find(Warehouse.class, getWarehouse_id());
				
				Query q = em.createQuery("Select e from WarehouseItemInventory e where warehouse = :warehouse");
				q.setParameter("warehouse", w);
				try
				{
					report = new ArrayList<ItemReport>();
					
					ArrayList<WarehouseItemInventory> list = (ArrayList<WarehouseItemInventory>)q.getResultList();
					for(WarehouseItemInventory wi : list)
					{
						ItemReport ir = new ItemReport();
						ir.setItem(wi.getItem());
						ir.setSettings(wi.getSettings());
						
						q = em.createQuery("Select MAX(e) from ItemSupply e where warehouse = :warehouse and item = :item");
						q.setParameter("warehouse", w);
						q.setParameter("item", ir.getItem());
						ItemSupply supply = (ItemSupply)q.getSingleResult();
						ir.setSupply(supply);
						
						q = em.createQuery("Select COUNT(e) from WarehouseSpareparts e where warehouse = :warehouse and item = :item and active = 0");
						q.setParameter("warehouse", w);
						q.setParameter("item", ir.getItem());
						Long qtyIssued = (Long)q.getSingleResult();
						ir.setQtyIssued(qtyIssued);
						
						report.add(ir);
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		return report;
	}

	public void setReport(ArrayList<ItemReport> report) {
		this.report = report;
	}
	
}
