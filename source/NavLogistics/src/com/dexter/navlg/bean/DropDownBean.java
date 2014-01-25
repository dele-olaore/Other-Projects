package com.dexter.navlg.bean;

import java.util.ArrayList;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.solder.logging.Logger;

import com.dexter.navlg.model.Item;
import com.dexter.navlg.model.Location;
import com.dexter.navlg.model.Role;
import com.dexter.navlg.model.Ship;
import com.dexter.navlg.model.Supplier;
import com.dexter.navlg.model.User;
import com.dexter.navlg.model.Warehouse;
import com.dexter.navlg.security.Authenticated;

@Stateful
@SessionScoped
@Named("dropdownBean")
public class DropDownBean
{
	@Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    @Authenticated
    private User user;
    
    @Inject
    ShipBean shipBean;
    
    public ArrayList<Location> getLocations()
    {
    	try
    	{
    	Query q = em.createQuery("SELECT e FROM Location e");
    	return (ArrayList<Location>) q.getResultList();
    	}
    	catch(Exception ex)
    	{
    		return new ArrayList<Location>();
    	}
    }
    
    public ArrayList<Warehouse> getWarehouses()
    {
    	try
    	{
    	Query q = em.createQuery("SELECT e FROM Warehouse e");
    	return (ArrayList<Warehouse>) q.getResultList();
    	}
    	catch(Exception ex)
    	{
    		return new ArrayList<Warehouse>();
    	}
    }
    
    public ArrayList<Warehouse> getAllWarehouses()
    {
    	try
    	{
    		Query q = em.createQuery("SELECT e FROM Warehouse e");
    		return (ArrayList<Warehouse>) q.getResultList();
    	}
    	catch(Exception ex)
    	{
    		return new ArrayList<Warehouse>();
    	}
    }
    
    public ArrayList<Warehouse> warehouses(Long loc_id)
    {
    	try
    	{
    		Location location = em.find(Location.class, loc_id);
    		Query q = em.createQuery("SELECT e FROM Warehouse e where location =:location");
    		q.setParameter("location", location);
    		return (ArrayList<Warehouse>) q.getResultList();
    	}
    	catch(Exception ex)
    	{
    		return new ArrayList<Warehouse>();
    	}
    }
    
    public ArrayList<Supplier> getSuppliers()
    {
    	try
    	{
    	Query q = em.createQuery("SELECT e FROM Supplier e");
    	return (ArrayList<Supplier>) q.getResultList();
    	}
    	catch(Exception ex)
    	{
    		return new ArrayList<Supplier>();
    	}
    }
    
    public ArrayList<Item> getItems()
    {
    	try
    	{
    	Query q = em.createQuery("SELECT e FROM Item e");
    	return (ArrayList<Item>) q.getResultList();
    	}
    	catch(Exception ex)
    	{
    		return new ArrayList<Item>();
    	}
    }
    
    public ArrayList<Ship> getShips()
    {
    	try
    	{
    	Query q = em.createQuery("SELECT e FROM Ship e");
    	return (ArrayList<Ship>) q.getResultList();
    	}
    	catch(Exception ex)
    	{
    		return new ArrayList<Ship>();
    	}
    }
    
    public SelectItem[] getLocationsSelect()
    {
    	ArrayList<Location> list = getLocations();
    	SelectItem[] options = new SelectItem[list.size() + 1];  
    	  
        options[0] = new SelectItem("", "Select");  
        for(int i = 0; i < list.size(); i++) {
        	options[i+1] = new SelectItem(list.get(i).getName(), list.get(i).getName());
        }
        
        return options;
    }
    
    public SelectItem[] getUserTypes()
    {
    	String[] types = {"Global", "Warehouse", "Ship"};
    	SelectItem[] options = new SelectItem[types.length + 1];  
  	  
        options[0] = new SelectItem("", "Select");  
        for(int i = 0; i < types.length; i++) {
        	options[i+1] = new SelectItem(types[i], types[i]);
        }
        
        return options;
    }
    
    public SelectItem[] getUserRolesByType(String type)
    {
    	SelectItem[] options = new SelectItem[0];
    	
    	Query q = em.createQuery("SELECT e FROM Role e where type = :type");
    	q.setParameter("type", type);
    	try
    	{
    		ArrayList<Role> roles = (ArrayList<Role>)q.getResultList();
    		options = new SelectItem[roles.size()];  
    		
            for(int i = 0; i < roles.size(); i++) {
            	options[i] = new SelectItem(roles.get(i).getId(), roles.get(i).getName());
            }
    	}
    	catch(Exception ex)
    	{}
    	
    	return options;
    }
    
    public SelectItem[] getFocUsers()
    {
    	SelectItem[] options = new SelectItem[0];
    	
    	Query q = em.createQuery("SELECT e FROM Role e where type = :type and name = :name");
    	q.setParameter("type", "Global");
    	q.setParameter("name", "FOC");
    	Role r = (Role)q.getSingleResult();
    	
    	if(r != null)
    	{
    		q = em.createQuery("SELECT e FROM User e where role = :role");
    		q.setParameter("role", r);
    		
    		ArrayList<User> list = (ArrayList<User>)q.getResultList();
    		options = new SelectItem[list.size()];
    		
            for(int i = 0; i < list.size(); i++) {
            	options[i] = new SelectItem(list.get(i).getUsername(), list.get(i).getFirstname() + " " + list.get(i).getLastname());
            }
    	}
    	
    	return options;
    }
    
    public SelectItem[] getIssuers()
    {
    	SelectItem[] options = new SelectItem[0];
    	
    	Query q = em.createQuery("SELECT e FROM User e where role in (select r from Role r where type = :type and (name = :name1 or name = :name2))");
		q.setParameter("type", "Global");
    	q.setParameter("name1", "Logistics");
    	q.setParameter("name2", "Store Personnel");
		
		ArrayList<User> list = (ArrayList<User>)q.getResultList();
		options = new SelectItem[list.size()];
		
        for(int i = 0; i < list.size(); i++) {
        	options[i] = new SelectItem(list.get(i).getUsername(), list.get(i).getFirstname() + " " + list.get(i).getLastname());
        }
    	
    	return options;
    }
}
