package com.dexter.navlg.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>FunctionRole</strong> is the model/entity class that represents a function-role mapping.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class FunctionRole implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private long id;
	private Role role;
	private Function function;
	
	public FunctionRole()
	{}
	
	public FunctionRole(Role role, Function function)
	{
		this.role = role;
		this.function = function;
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
	@ManyToOne
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@NotNull
	@ManyToOne
	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}
	
}
