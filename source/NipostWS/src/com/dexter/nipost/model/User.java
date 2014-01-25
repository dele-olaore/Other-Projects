package com.dexter.nipost.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * <strong>User</strong> is the model/entity class that represents a user.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Table(name="USER_TB")
public class User implements Serializable
{
	private static final long serialVersionUID = -6569624778469169314L;
	
	@Id
    @NotNull
    @GeneratedValue
	private Long id;
	
	@Column(unique=true)
	private String email;
	
	private String password;
	private String fullname;
	private boolean active;
	
	public User()
	{}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
