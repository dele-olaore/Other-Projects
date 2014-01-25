package com.dexter.recf.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>Address</strong> is the model/entity class that represents an address.
 * </p>
 *
 * @author Dele Olaore
 * */
//@Entity
//@Veto
public class Address implements Serializable
{
	private static final long serialVersionUID = -5305506735070650134L;

	private long id;
	private String address;
	private String city;
	private State state;
	private LocalGovtArea localgovtarea;
	
	public Address()
	{}

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
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@NotNull
	@ManyToOne
	public State getState() {
		if(state == null)
			state = new State();
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@NotNull
	@ManyToOne
	public LocalGovtArea getLocalgovtarea() {
		if(localgovtarea == null)
			localgovtarea = new LocalGovtArea();
		return localgovtarea;
	}

	public void setLocalgovtarea(LocalGovtArea localgovtarea) {
		this.localgovtarea = localgovtarea;
	}
	
}
