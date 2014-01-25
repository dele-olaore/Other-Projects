package com.dexter.recf.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>UserEmployment</strong> is the model/entity class that represents an employment mapping to a candidate.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class UserEmployment implements Serializable
{
	private static final long serialVersionUID = 1856568881781165403L;

	private long id;
	private User user;
	private Employment employment;
	
	public UserEmployment()
	{}
	
	public UserEmployment(final User user, final Employment employment)
	{
		this.user = user;
		this.employment = employment;
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
	@OneToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@NotNull
	@OneToOne
	public Employment getEmployment() {
		return employment;
	}

	public void setEmployment(Employment employment) {
		this.employment = employment;
	}
	
}
