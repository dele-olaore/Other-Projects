package com.dexter.navlg.model;

import java.io.Serializable;

import org.jboss.solder.core.Veto;

/**
 * A generic class to handle some data transfer from controller to view.
 * */
@Veto
public class Ref implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String type;
	private Object data;
	
	public Ref()
	{}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
