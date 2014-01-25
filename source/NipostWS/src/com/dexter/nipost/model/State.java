package com.dexter.nipost.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * <strong>State</strong> is the model/entity class that represents a state.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
public class State implements Serializable
{
	private static final long serialVersionUID = 1065101987581959475L;
	
	@Id
    @NotNull
    @GeneratedValue
	private Long id;
	
	@Column(unique=true)
	private String name;
	
	public State()
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
	

}
