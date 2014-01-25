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
import com.dexter.navlg.model.Supplier;
import com.dexter.navlg.model.User;
import com.dexter.navlg.security.Authenticated;

@Stateful
@SessionScoped
@Named("supplierBean")
public class SupplierBean
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
    
    private Supplier supplier;
    private ArrayList<Supplier> suppliers;
    
    public String movetoupdate(Long id)
    {
    	for(Supplier e : getSuppliers())
    	{
    		if(e.getId().longValue() == id.longValue())
    		{
    			setSupplier(e);
    			break;
    		}
    	}
    	return "supplier_edit";
    }
    
    public void SaveUpdate()
    {
    	if(getSupplier().getName() != null)
    	{
    		if(getSupplier().getId() != null && getSupplier().getId() > 0)
    			em.merge(getSupplier());
    		else
    			em.persist(getSupplier());
    		
    		setSupplier(null);
    		setSuppliers(null);
    		
    		messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    	}
    }

	public Supplier getSupplier() {
		if(supplier == null)
			supplier = new Supplier();
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public ArrayList<Supplier> getSuppliers() {
		if(suppliers == null)
		{
			Query q = em.createQuery("SELECT e FROM Supplier e");
			suppliers = (ArrayList<Supplier>)q.getResultList();
			if(suppliers == null)
				suppliers = new ArrayList<Supplier>();
		}
		return suppliers;
	}

	public void setSuppliers(ArrayList<Supplier> suppliers) {
		this.suppliers = suppliers;
	}
    
}
