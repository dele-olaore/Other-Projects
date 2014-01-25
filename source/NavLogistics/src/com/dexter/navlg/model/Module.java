package com.dexter.navlg.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>Module</strong> is the model/entity class that represents a module which a is collection of functions.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class Module implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private Module parent;
	
	public Module()
	{}
	
	public Module(String name)
	{
		this.name = name;
	}
	
	public Module(String name, Module parent)
	{
		this.name = name;
		this.parent = parent;
	}

	@Id
    @NotNull
    @GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@Size(min = 1, max = 30)
	@Column(unique=true, nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	public Module getParent() {
		return parent;
	}

	public void setParent(Module parent) {
		try
		{
			if(parent == null || (parent != null && parent.getParent() == null))
				this.parent = parent;
			else
				throw new Exception("Parent module '" + parent.getName() + "' is already a sub-module!");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
