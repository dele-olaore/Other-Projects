package com.dexter.navlg.bean;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.solder.logging.Logger;

import com.dexter.navlg.model.Function;
import com.dexter.navlg.model.FunctionRole;
import com.dexter.navlg.model.Module;
import com.dexter.navlg.model.Ref;
import com.dexter.navlg.model.Role;
import com.dexter.navlg.model.User;
import com.dexter.navlg.security.Authenticated;

@Stateful
@SessionScoped
@Named("permManager")
public class PermissionManager
{
	@Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    @Authenticated
    private User user;
    
    private ArrayList<Ref> modules;
    private Hashtable<Long, ArrayList<Ref>> functions;
    private Hashtable<Long, ArrayList<Ref>> sm_functions;
    
    public PermissionManager()
    {}
    
    public void loadFunctions()
    {
    	// TODO: now we get the user's functions based on its role
        Role role = user.getRole();
        Query q = em.createQuery("SELECT e FROM FunctionRole e WHERE role = :role");
        q.setParameter("role", role);
        List<FunctionRole> functions = (List<FunctionRole>) q.getResultList();
        System.out.println("functions size: " + functions.size());
        for(FunctionRole e : functions)
        {
        	Function f = e.getFunction();
        	Module m = f.getModule();
        	
        	if(m.getParent() == null) // a function of a main module
        	{
        		if(getFunctions().containsKey(m.getId()))
            	{
            		Ref obj = new Ref();
            		obj.setId(f.getId());
            		obj.setType("F");
            		obj.setData(f);
            		getFunctions().get(m.getId()).add(obj);
            		System.out.println("here 1");
            	}
            	else
            	{
            		Ref obj = new Ref();
        			obj.setId(m.getId());
        			obj.setType("M");
        			obj.setData(m);
        			getModules().add(obj);
        			
        			ArrayList<Ref> list = new ArrayList<Ref>();
        			
        			Ref objf = new Ref();
            		objf.setId(f.getId());
            		objf.setType("F");
            		objf.setData(f);
            		list.add(objf);
            		getFunctions().put(m.getId(), list);
            		System.out.println("here 2");
            	}
        	}
        	else // a function of a sub-module
        	{
        		if(getFunctions().containsKey(m.getParent().getId()))
        		{
        			boolean exists = false;
        			
        			for(Ref ref : getFunctions().get(m.getParent().getId()))
        			{
        				if(ref.getId() == m.getId() && ref.getType().equalsIgnoreCase("M"))
        				{
        					exists = true;
        					break;
        				}
        			}
        			
        			if(!exists)
	        		{
	        			Ref obj = new Ref();
	        			obj.setId(m.getId());
	        			obj.setType("M");
	        			obj.setData(m);
	        			getFunctions().get(m.getParent().getId()).add(obj);
        			}
        		}
        		else
        		{
        			Ref obj = new Ref();
        			obj.setId(m.getParent().getId());
        			obj.setType("M");
        			obj.setData(m.getParent());
        			getModules().add(obj);
        			
        			ArrayList<Ref> list = new ArrayList<Ref>();
        			
        			Ref mobj = new Ref();
        			mobj.setId(m.getId());
        			mobj.setType("M");
        			mobj.setData(m);
        			list.add(mobj);
            		getFunctions().put(m.getParent().getId(), list);
        		}
        		
        		if(getSm_functions().containsKey(m.getId()))
            	{
            		Ref obj = new Ref();
            		obj.setId(f.getId());
            		obj.setType("F");
            		obj.setData(f);
            		getSm_functions().get(m.getId()).add(obj);
            	}
            	else
            	{
            		ArrayList<Ref> list = new ArrayList<Ref>();
        			
        			Ref objf = new Ref();
            		objf.setId(f.getId());
            		objf.setType("F");
            		objf.setData(f);
            		list.add(objf);
            		getSm_functions().put(m.getId(), list);
            	}
        	}
        }
    }
    
    public ArrayList<Ref> getModuleFunctions(long id)
    {
    	if(getFunctions().containsKey(id))
    		return getFunctions().get(id);
    	else
    		return new ArrayList<Ref>();
    }
    
    public ArrayList<Ref> getSubModuleFunctions(long id)
    {
    	if(getSm_functions().containsKey(id))
    		return getSm_functions().get(id);
    	else
    		return new ArrayList<Ref>();
    }
    
    public ArrayList<Ref> getModules() {
		if(modules == null)
			modules = new ArrayList<Ref>();
		System.out.println("modules size: " + modules.size());
		return modules;
	}

	public void setModules(ArrayList<Ref> modules) {
		this.modules = modules;
	}

	public Hashtable<Long, ArrayList<Ref>> getFunctions() {
		if(functions == null)
			functions = new Hashtable<Long, ArrayList<Ref>>();
		return functions;
	}

	public void setFunctions(Hashtable<Long, ArrayList<Ref>> functions) {
		this.functions = functions;
	}
	
	public Hashtable<Long, ArrayList<Ref>> getSm_functions() {
		if(sm_functions == null)
			sm_functions = new Hashtable<Long, ArrayList<Ref>>();
		return sm_functions;
	}
	
	public void setSm_functions(Hashtable<Long, ArrayList<Ref>> sm_functions) {
		this.sm_functions = sm_functions;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
