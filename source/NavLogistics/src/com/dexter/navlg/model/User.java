package com.dexter.navlg.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>User</strong> is the model/entity class that represents a user who may be an administrator or basic staff.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class User implements Serializable
{
	private static final long serialVersionUID = -602733026033932730L;
	
	private String username;
    private String password;
    
    private String firstname;
    private String lastname;
    private String title;
    private String designation;
    private String phone;
    private String email;
    
    private Role role;
    
    private Ship ship;
    private Warehouse warehouse;
    
    public User()
    {}
    
    public User(String username, String password)
    {
    	this.username = username;
    	this.password = password;
    }
    
    @Id
    @NotNull
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^\\w*$", message = "not a valid username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@NotNull
    @Size(min = 5, max = 15)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
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

	@ManyToOne
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@ManyToOne
	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	@ManyToOne
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
    
}
