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
 * <strong>Employment</strong> is the model/entity class that represents an employment.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class Employment implements Serializable
{
	private static final long serialVersionUID = 5153324388777123674L;
	
	private long id;
	private String employer;
	private double salary;
	
	public Employment()
	{}
	
	public Employment(final String employer, final double salary)
	{
		this.employer = employer;
		this.salary = salary;
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
	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	@NotNull
	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}
	

}
