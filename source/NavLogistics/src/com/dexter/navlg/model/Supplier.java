package com.dexter.navlg.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>Supplier</strong> is the model/entity class that represents a supplier of items.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class Supplier implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String code;
	private String address;
	private String phone;
	private String email;
	
	public Supplier()
	{}

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
	@Column(unique=true, nullable=false)
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
