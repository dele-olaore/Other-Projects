package com.dexter.navlg.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>Role</strong> is the model/entity class that represents a user role.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class Role implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private String type; // global, warehouse or ship
	
	public Role()
	{}
	
	public Role(String name, String type)
	{
		this.name = name;
		this.type = type;
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
	@Size(min = 1, max = 30)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Pattern(regexp = "^(Global|Warehouse|Ship)?$", message = "not a valid type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
