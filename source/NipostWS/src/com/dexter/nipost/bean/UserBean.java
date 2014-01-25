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

import com.dexter.nipost.model.User;
import com.dexter.nipost.util.Hasher;

public class UserBean
{
	private static final String PERSISTENCE_UNIT_NAME = "nipost";
    private static EntityManagerFactory factory;
    private EntityManager em;
	
    private EntityTransaction utx;
    
    final Logger logger = Logger.getLogger("NipostWS-UserBean");
    
	public UserBean()
	{
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
	}
	
	/**
	 * Gets all users.
	 * */
	public ArrayList<User> getUsers()
	{
		ArrayList<User> list = new ArrayList<User>();
		
		try
		{
			Query q = em.createQuery("SELECT e from User e");
			
			List ret = q.getResultList();
			for(Object e : ret)
			{
				if(e instanceof User)
					list.add((User)e);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * Updates a user.
	 * */
	public String updateUser(User user)
	{
		String ret = "";
		
		try
		{
			utx = em.getTransaction();
            utx.begin();
            em.merge(user);
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
			logger.log(Level.SEVERE, "Persist failed for user. " + ex);
			ret = "error";
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
	 * Saves a user object to the db.
	 * */
	public String persistUser(User user)
	{
		String ret = "";
		
		try
		{
			utx = em.getTransaction();
            utx.begin();
            em.persist(user);
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
			logger.log(Level.SEVERE, "Persist failed for user. " + ex);
			ret = "error";
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
	 * Gets a user object based on the supplied parameters.
	 * */
	public User authenticateUser(String email, String password)
	{
		User ret = null;
		
		try
		{
			Query q = em.createQuery("SELECT e from User e where e.email = :email");
			q.setParameter("email", email);
			
			Object obj = q.getSingleResult();
			if(obj != null && obj instanceof User)
			{
				User u = (User)obj;
				if(Hasher.getHashValue(password).equals(u.getPassword()))
				{
					ret = u;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return ret;
	}
}
