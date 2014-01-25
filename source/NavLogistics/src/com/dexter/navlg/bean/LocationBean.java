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
import com.dexter.navlg.model.Location;
import com.dexter.navlg.model.User;
import com.dexter.navlg.security.Authenticated;

@Stateful
@SessionScoped
@Named("locationBean")
public class LocationBean
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
    
    private Location location;
    private ArrayList<Location> locations;
    
    public String movetoupdate(Long id)
    {
    	for(Location e : getLocations())
    	{
    		if(e.getId().longValue() == id.longValue())
    		{
    			setLocation(e);
    			break;
    		}
    	}
    	return "location_edit";
    }
    
    public void SaveUpdate()
    {
    	if(getLocation().getName() != null)
    	{
    		try
    		{
    			if(getLocation().getWarehouseCount() == null)
    				getLocation().setWarehouseCount(0L);
    			
	    		if(getLocation().getId() != null && getLocation().getId() > 0)
	    			em.merge(getLocation());
	    		else
	    			em.persist(getLocation());
    		
    			setLocation(null);
    			setLocations(null);
    			
    			messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
    		}
    	}
    }

	public Location getLocation() {
		if(location == null)
			location = new Location();
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ArrayList<Location> getLocations() {
		if(locations == null)
		{
			Query q = em.createQuery("SELECT e FROM Location e");
	    	locations = (ArrayList<Location>) q.getResultList();
	    	if(locations == null)
	    		locations = new ArrayList<Location>();
		}
		return locations;
	}

	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}
    
}
