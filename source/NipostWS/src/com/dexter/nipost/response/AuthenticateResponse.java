package com.dexter.nipost.response;

import java.io.Serializable;

public class AuthenticateResponse implements Serializable
{
	private static final long serialVersionUID = 2769577094277080353L;
	
	private String message;
	private boolean success;
	
	private String user_id;
	private String fullname;
	
	public AuthenticateResponse()
	{}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean status) {
		this.success = status;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
}
