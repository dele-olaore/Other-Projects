package com.dexter.recf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    private String phone;

    private Role role;
    
    public User()
    {}

    public User(final String username, final String password, final String phone)
    {
    	this.username = username;
        this.password = password;
        this.phone = phone;
    }
    
    public User(final String username, final String password, final String phone, Role role)
    {
    	this(username, password, phone);
        this.role = role;
    }

    @NotNull
    @Size(min = 5, max = 15)
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Id
    @NotNull
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^\\w*$", message = "not a valid username")
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Column(nullable=true)
    @Size(min = 0, max = 20)
    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@NotNull
	@ManyToOne
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
    public String toString() {
        return "User(" + username + ")";
    }
}
