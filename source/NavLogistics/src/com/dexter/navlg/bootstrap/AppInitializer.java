package com.dexter.navlg.bootstrap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolationException;

import org.jboss.solder.logging.Logger;
import org.jboss.solder.servlet.WebApplication;
import org.jboss.solder.servlet.event.Initialized;

import com.dexter.navlg.model.Function;
import com.dexter.navlg.model.FunctionRole;
import com.dexter.navlg.model.Module;
import com.dexter.navlg.model.Role;
import com.dexter.navlg.model.User;

/**
 * An alternative bean used to import seed data into the database when the application is being initialized.
 *
 * @author Dele Olaore
 * */
@Alternative
public class AppInitializer
{
	@PersistenceContext
    private EntityManager entityManager;

    @Inject
    private UserTransaction utx;

    @Inject
    private Logger log;
    
    private final List<Role> roles = new ArrayList<Role>();
    private final List<Module> modules = new ArrayList<Module>();
    private final List<Function> m_functions = new ArrayList<Function>();
    private final List<Function> o_functions = new ArrayList<Function>();
    private final List<FunctionRole> funcroles = new ArrayList<FunctionRole>();
    private final List<User> users = new ArrayList<User>();
    
    public AppInitializer()
    {
    	roles.addAll(Arrays.asList(
    		new Role("Administrator", "Global"),
    		new Role("Logistics", "Global"),
    		new Role("Logistics", "Warehouse"),
    		new Role("Logistics", "Ship"),
    		new Role("Store Personnel", "Global"),
    		new Role("Store Personnel", "Warehouse"),
    		new Role("Store Personnel", "Ship"),
    		new Role("FOC", "Global"),
    		new Role("Standard", "Global"),
    		new Role("Standard", "Warehouse"),
    		new Role("Standard", "Ship")
    		));
    	/*modules.addAll(Arrays.asList(
    		new Module("Maintenance"),
    		new Module("Others")
    		));
    	m_functions.addAll(Arrays.asList(
    		new Function("Manage Roles", null),
    		new Function("Manage Modules", null),
    		new Function("Manage Functions", null),
    		new Function("Manage Function-Role Mappings", null),
    		new Function("Manage Users", null)
    		));
    	o_functions.addAll(Arrays.asList(
    		new Function("Manage Locations", null),
    		new Function("Manage Warehouses", null),
    		new Function("Manage Ships", null),
    		new Function("Manage Items", null),
    		new Function("Manage Suppliers", null)
    		));*/
    }
    
    /**
     * Import seed data when Seam Servlet fires an event notifying observers that the web application is being initialized.
     */
    public void importData(@Observes @Initialized WebApplication webapp)
    {
        log.info("Importing seed data for application " + webapp.getName());
        // use manual transaction control since this is a managed bean
        try
        {
        	/*utx.begin();
            // AS7-2045
        	entityManager.createQuery("delete from User").executeUpdate();
        	entityManager.createQuery("delete from FunctionRole").executeUpdate();
        	entityManager.createQuery("delete from Function").executeUpdate();
        	entityManager.createQuery("delete from Module where not parent = null").executeUpdate();
        	entityManager.createQuery("delete from Module").executeUpdate();
        	entityManager.createQuery("delete from Role").executeUpdate();
        	
        	persist(roles);*/
        	
        	/*persist(modules.get(0));
        	modules.get(1).setParent(modules.get(0));
        	persist(modules.get(1));
        	
        	for(Function e : m_functions)
        	{
        		e.setModule(modules.get(0));
        	}
        	persist(m_functions);
        	
        	for(Function e : o_functions)
        	{
        		e.setModule(modules.get(1));
        	}
        	persist(o_functions);
        	
        	for(Function e : m_functions)
        	{
        		funcroles.add(new FunctionRole(roles.get(0), e));
        	}
        	for(Function e : o_functions)
        	{
        		funcroles.add(new FunctionRole(roles.get(0), e));
        	}
        	persist(funcroles);*/
        	
        	/*User admUser = new User("dele", "dexter");
        	admUser.setFirstname("Dele");
        	admUser.setLastname("Olaore");
        	admUser.setRole(roles.get(0));
        	
        	users.addAll(Arrays.asList(admUser));
        	
            persist(users);
            
            utx.commit();*/
            log.info("Seed data successfully imported");
        }
        catch (Exception e)
        {
            /*log.error("Import failed. Seed data will not be available.", e);
            try
            {
                if (utx.getStatus() == Status.STATUS_ACTIVE)
                {
                    try
                    {
                        utx.rollback();
                    }
                    catch (Exception rbe)
                    {
                        log.error("Error rolling back transaction", rbe);
                    }
                }
            }
            catch (Exception se)
            {}*/
        }
    }

    private void persist(List<?> entities)
    {
        for (Object e : entities)
        {
        	persist(e);
        }
    }

    private void persist(Object entity)
    {
        // use a try-catch block here so we can capture identity
        // of entity that fails to persist
        try
        {
            entityManager.persist(entity);
            entityManager.flush();
        }
        catch(ConstraintViolationException e)
        {
            throw new PersistenceException("Cannot persist invalid entity: " + entity);
        }
        catch(PersistenceException e)
        {
            throw new PersistenceException("Error persisting entity: " + entity, e);
        }
    }
}
