package com.dexter.nipost.bean;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.dexter.nipost.model.LocalGovtArea;
import com.dexter.nipost.model.State;

public class RefBean
{
	private static final String PERSISTENCE_UNIT_NAME = "nipost";
    private static EntityManagerFactory factory;
    private EntityManager em;
	
    //private EntityTransaction utx;
    
    final Logger logger = Logger.getLogger("NipostWS-UserBean");
    
	public RefBean()
	{
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<State> getStates()
	{
		ArrayList<State> list = new ArrayList<State>();;
		try
		{
			Query q = em.createQuery("SELECT e from State e");
			
			Vector<State> vec = (Vector<State>) q.getResultList();
			for(State e : vec)
			{
				list.add(e);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<LocalGovtArea> getLGs()
	{
		ArrayList<LocalGovtArea> list = new ArrayList<LocalGovtArea>();;
		try
		{
			Query q = em.createQuery("SELECT e from LocalGovtArea e");
			
			Vector<LocalGovtArea> vec = (Vector<LocalGovtArea>) q.getResultList();
			for(LocalGovtArea e : vec)
			{
				list.add(e);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<LocalGovtArea> getLGs(String state)
	{
		ArrayList<LocalGovtArea> list = new ArrayList<LocalGovtArea>();;
		try
		{
			Query q = em.createQuery("SELECT e from State e where e.name = :name");
			q.setParameter("name", state);
			
			Object stateObj = q.getSingleResult();
			if(stateObj != null && stateObj instanceof State)
			{
				State st = (State) stateObj;
				q = em.createQuery("SELECT e from LocalGovtArea e where e.state = :state");
				q.setParameter("state", st);
				
				Vector<LocalGovtArea> vec = (Vector<LocalGovtArea>) q.getResultList();
				for(LocalGovtArea e : vec)
				{
					list.add(e);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return list;
	}
}
