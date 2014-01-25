package com.dexter.nipost.response;

import java.io.Serializable;

public class BasicDataResponse implements Serializable
{
	private static final long serialVersionUID = 5637048174632438379L;
	
	private String message;
	private boolean status;
	
	private Long id;
	
	public BasicDataResponse()
	{}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
