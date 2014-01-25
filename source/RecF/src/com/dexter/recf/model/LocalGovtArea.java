package com.dexter.recf.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>LocalGovtArea</strong> is the model/entity class that represents a local government area.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class LocalGovtArea implements Serializable
{
	private static final long serialVersionUID = 6570931768408106155L;

	private long id;
	private String name;
	
	private State state;
	
	public LocalGovtArea()
	{}
	
	public LocalGovtArea(final String name)
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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	@Override
    public String toString() {
		return name;
	}
}
