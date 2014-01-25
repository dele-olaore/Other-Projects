package com.dexter.biodata.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Config implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String identificationcode;
	
	public Config()
	{}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificationcode() {
		return identificationcode;
	}

	public void setIdentificationcode(String identificationcode) {
		this.identificationcode = identificationcode;
	}
	
}
