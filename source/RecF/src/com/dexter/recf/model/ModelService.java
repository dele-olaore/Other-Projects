package com.dexter.recf.model;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
public class ModelService
{
	@PersistenceContext
    private EntityManager em;
	
	public Object find(Class cls, long id)
	{
		return em.find(cls, id);
	}
}
