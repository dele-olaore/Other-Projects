package com.dexter.nipost.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * <strong>LocalGovtArea</strong> is the model/entity class that represents a local govt area.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
public class LocalGovtArea implements Serializable
{
	private static final long serialVersionUID = 2015691676099970444L;

	@Id
    @NotNull
    @GeneratedValue
	private Long id;
	
	private String name;
	@ManyToOne
	private State state;
	
	public LocalGovtArea()
	{}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
}
