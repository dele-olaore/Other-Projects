package com.dexter.recf.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>EducationQualification</strong> is the model/entity class that represents an education qualification by a candidate.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class EducationQualification implements Serializable
{
	private static final long serialVersionUID = -2492118702073269090L;

	private long id;
	private String schoolname;
	private String qualification;
	private long gradyr;
	
	public EducationQualification()
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

	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public long getGradyr() {
		return gradyr;
	}

	public void setGradyr(long gradyr) {
		this.gradyr = gradyr;
	}
	
}
