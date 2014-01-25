package com.dexter.nipost.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.dexter.nipost.model.Property;
import com.dexter.nipost.model.User;

public class PropertyBean
{
	private static final String PERSISTENCE_UNIT_NAME = "nipost";
    private static EntityManagerFactory factory;
    private EntityManager em;
	
    private EntityTransaction utx;
    
    final Logger logger = Logger.getLogger("NipostWS-PropertyBean");
    
	public PropertyBean()
	{
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList<Property> getProperties(String state, String lg)
	{
		ArrayList<Property> list = new ArrayList<Property>();
		
		try
		{
			String query = "Select e from Property e ";
			
			boolean whereClauseAdded = false;
			if(state != null && state.trim().length() > 0)
			{
				whereClauseAdded = true;
				query += "where e.state = '" + state + "'";
			}
			
			if(lg != null && lg.trim().length() > 0)
			{
				if(whereClauseAdded)
					query += " and e.lg = '" + lg + "'";
				else
					query += "where e.lg = '" + lg + "'";
			}
			
			Query q = em.createQuery(query.trim());
			
			List ret = q.getResultList();
			for(Object e : ret)
			{
				if(e instanceof Property)
					list.add((Property)e);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList<Property> getProperties(String state, String lg, User user)
	{
		ArrayList<Property> list = new ArrayList<Property>();
		
		try
		{
			String query = "Select e from Property e ";
			
			boolean whereClauseAdded = false;
			if(state != null && state.trim().length() > 0)
			{
				whereClauseAdded = true;
				query += "where e.state = '" + state + "'";
			}
			
			if(lg != null && lg.trim().length() > 0)
			{
				if(whereClauseAdded)
					query += " and e.lg = '" + lg + "'";
				else
					query += "where e.lg = '" + lg + "'";
			}
			
			if(whereClauseAdded)
				query += " and e.user = :user";
			else
				query += "where e.user = :user";
			
			Query q = em.createQuery(query.trim());
			q.setParameter("user", user);
			
			List ret = q.getResultList();
			for(Object e : ret)
			{
				if(e instanceof Property)
					list.add((Property)e);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return list;
	}
	
	public Property getProperty(String id)
	{
		return em.find(Property.class, Long.parseLong(id));
	}
	
	public String deleteProperty(String id)
	{
		String ret = "";
		
		Property p = em.find(Property.class, Long.parseLong(id));
		if(p != null)
		{
			try
			{
				utx = em.getTransaction();
				utx.begin();
				em.remove(p);
				em.flush();
				utx.commit();
				ret = "success";
			}
			catch(Exception ex)
			{
				logger.log(Level.SEVERE, "Delete failed for property. " + ex);
				ret = "Error: " + ex.getMessage();
				try
	            {
	                if (utx != null && utx.isActive())
	                {
	                    try
	                    {
	                        utx.rollback();
	                    }
	                    catch (Exception rbe)
	                    {
	                        logger.log(Level.SEVERE, "Error rolling back transaction", rbe);
	                    }
	                }
	            }
	            catch (Exception se)
	            {}
			}
		}
		else
			ret = "Property not found!";
		
		return ret;
	}
	
	public String updateProperty(Property property)
	{
		String ret = "";
		
		try
		{
			utx = em.getTransaction();
            utx.begin();
            em.merge(property);
            try
            {
                em.flush();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            utx.commit();
            ret = "success";
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			logger.log(Level.SEVERE, "Persist failed for property. " + ex);
            try
            {
                if (utx != null && utx.isActive())
                {
                    try
                    {
                        utx.rollback();
                    }
                    catch (Exception rbe)
                    {
                        logger.log(Level.SEVERE, "Error rolling back transaction", rbe);
                    }
                }
            }
            catch (Exception se)
            {}
		}
		finally
		{}
		
		return ret;
	}
	
	/**
	 * Saves a property object to the db.
	 * */
	public String persistProperty(Property property)
	{
		String ret = "";
		
		try
		{
			User u = em.find(User.class, Long.parseLong(property.getUser_id()));
			if(u != null)
			{
				property.setUser(u);
			}
			else
			{
				ret = "invalid user";
				return ret;
			}
			utx = em.getTransaction();
            utx.begin();
            em.persist(property);
            try
            {
                em.flush();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            utx.commit();
            ret = "success";
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			logger.log(Level.SEVERE, "Persist failed for property. " + ex);
            try
            {
                if (utx != null && utx.isActive())
                {
                    try
                    {
                        utx.rollback();
                    }
                    catch (Exception rbe)
                    {
                        logger.log(Level.SEVERE, "Error rolling back transaction", rbe);
                    }
                }
            }
            catch (Exception se)
            {}
		}
		finally
		{}
		
		return ret;
	}
}
