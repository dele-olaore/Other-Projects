package com.dexter.navlg.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>Function</strong> is the model/entity class that represents a function.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class Function implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private Module module;
	
	public Function()
	{}
	
	public Function(String name, Module module)
	{
		this.name = name;
		this.module = module;
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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}
}
