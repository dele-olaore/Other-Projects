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
 * <strong>UserEducationQualification</strong> is the model/entity class that represents an education qualification mapping to a candidate.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class UserEducationQualification implements Serializable
{
	private static final long serialVersionUID = 7025506105074567232L;
	
	private long id;
	private User user;
	private EducationQualification eduQualification;
	
	public UserEducationQualification()
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
	@OneToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@NotNull
	@OneToOne
	public EducationQualification getEduQualification() {
		return eduQualification;
	}

	public void setEduQualification(EducationQualification eduQualification) {
		this.eduQualification = eduQualification;
	}
	
}
