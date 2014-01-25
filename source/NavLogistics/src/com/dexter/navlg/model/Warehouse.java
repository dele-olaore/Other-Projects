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
 * <strong>Warehouse</strong> is the model/entity class that represents a warehouse in a location.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class Warehouse implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String code;
	private String address;
	private Location location;
	
	public Warehouse()
	{}
	
	public Warehouse(String name, String address)
	{
		this.name = name;
		this.address = address;
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
	@Column(unique=false, nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Size(min = 3, max = 3)
	@Column(unique=true, nullable=false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ManyToOne
	public Location getLocation() {
		if(location == null)
			location = new Location();
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
