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
import com.dexter.navlg.model.Role;
import com.dexter.navlg.model.Ship;
import com.dexter.navlg.model.User;
import com.dexter.navlg.model.Warehouse;
import com.dexter.navlg.security.Authenticated;

@Stateful
@SessionScoped
@Named("userBean")
public class UserBean
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
    
    private ArrayList<Role> roles;
    
    private User userObj;
    private String conPassword;
    private String type; // global, warehouse, ship
    private Long role_id, warehouse_id, ship_id;
    private ArrayList<User> users;
    
    public void Save()
    {
    	if(getUserObj().getUsername() != null)
    	{
    		if(getUserObj().getPassword().equals(getConPassword()))
    		{
    			if(getType().equals("Warehouse"))
    			{
    				Warehouse w = em.find(Warehouse.class, getWarehouse_id());
    				getUserObj().setWarehouse(w);
    			}
    			else if(getType().equals("Ship"))
    			{
    				Ship s = em.find(Ship.class, getShip_id());
    				getUserObj().setShip(s);
    			}
    			
    			Role r = em.find(Role.class, getRole_id());
    			getUserObj().setRole(r);
    			
    			em.persist(getUserObj());
    			setUserObj(null);
    			setUsers(null);
    			
    			messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    		}
    	}
    }
    
    public void Update()
    {
    	if(getUserObj().getUsername() != null)
    	{
    		if(getUserObj().getPassword().equals(getConPassword()))
    		{
    			em.merge(getUserObj());
    			setUserObj(null);
    			setUsers(null);
    			
    			messages.info(new AppBundleKey("success")).detail(new AppBundleKey("success_proccessDone")).defaults("");
    		}
    	}
    }

	public ArrayList<Role> getRoles() {
		if(roles == null)
		{
			Query q = em.createQuery("SELECT e FROM Role e");
			try
			{
				roles = (ArrayList<Role>)q.getResultList();
				if(roles == null)
					roles = new ArrayList<Role>();
			}
			catch(Exception ex)
			{
				roles = new ArrayList<Role>();
			}
		}
		return roles;
	}

	public void setRoles(ArrayList<Role> roles) {
		this.roles = roles;
	}

	public User getUserObj() {
		if(userObj == null)
			userObj = new User();
		return userObj;
	}

	public void setUserObj(User userObj) {
		this.userObj = userObj;
	}

	public String getConPassword() {
		return conPassword;
	}

	public void setConPassword(String conPassword) {
		this.conPassword = conPassword;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	public Long getWarehouse_id() {
		return warehouse_id;
	}

	public void setWarehouse_id(Long warehouse_id) {
		this.warehouse_id = warehouse_id;
	}

	public Long getShip_id() {
		return ship_id;
	}

	public void setShip_id(Long ship_id) {
		this.ship_id = ship_id;
	}

	public ArrayList<User> getUsers() {
		if(users == null)
		{
			Query q = em.createQuery("SELECT e FROM User e");
			try
			{
				users = (ArrayList<User>)q.getResultList();
				if(users == null)
					users = new ArrayList<User>();
			}
			catch(Exception ex)
			{
				users = new ArrayList<User>();
			}
		}
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
    
}
