package com.dexter.recf.bean;

import java.util.Collection;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.dexter.recf.model.LocalGovtArea;
import com.dexter.recf.model.State;

@Named("dropdown")
public class DropDown
{
	@PersistenceContext
    private EntityManager em;
	
	@Inject
    private FacesContext facesContext;
	
	public SelectItem[] getStates()
	{
		SelectItem[] list = null;
		
		try
		{
			Query query = em.createQuery("SELECT e FROM State e");
			Collection<State> states = (Collection<State>) query.getResultList();
			
			if(states != null)
			{
				list = new SelectItem[states.size()];
				int i = 0;
				for(State e : states)
				{
					SelectItem it = new SelectItem(e.getId(), e.getName());
					list[i] = it;
					i++;
				}
			}
		}
		catch(Exception ex)
		{
			list = new SelectItem[0];
		}
		
		return list;
	}
	
	public SelectItem[] getLocalGovtAreas(long state_id)
	{
		SelectItem[] list = null;
		
		try
		{
			State state = em.find(State.class, state_id);
			
			if(state != null)
			{
				Query query = em.createQuery("SELECT e FROM LocalGovtArea e WHERE state = :st");
				query.setParameter("st", state);
				Collection<LocalGovtArea> lgas = (Collection<LocalGovtArea>) query.getResultList();
				
				if(lgas != null)
				{
					list = new SelectItem[lgas.size()];
					int i = 0;
					for(LocalGovtArea e : lgas)
					{
						SelectItem it = new SelectItem(e.getId(), e.getName());
						list[i] = it;
						i++;
					}
				}
			}
			else
			{
				list = new SelectItem[0];
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			list = new SelectItem[0];
		}
		
		return list;
	}
}
