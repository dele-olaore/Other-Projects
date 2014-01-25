package com.dexter.recf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>Role</strong> is the model/entity class that represents a user role that the program use to decide functions available.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class Role implements Serializable
{
	private static final long serialVersionUID = 6517987380728383733L;
	
	private long id;
	private String name;
	
	public Role()
	{}
	
	public Role(final String name)
	{
		this.name = name;
	}

	@Id
    @NotNull
    @GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NotNull
    @Size(min = 1, max = 100)
	@Column(unique=true, nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
    public String toString() {
		return name;
	}
}
