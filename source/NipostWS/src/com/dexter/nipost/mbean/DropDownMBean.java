package com.dexter.nipost.mbean;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.dexter.nipost.bean.RefBean;
import com.dexter.nipost.model.LocalGovtArea;
import com.dexter.nipost.model.State;

@ManagedBean(name = "dropdownMBean")
@RequestScoped
public class DropDownMBean
{
	final Logger logger = Logger.getLogger("NipostWS-UserBean");
	
	public ArrayList<State> getStates()
	{
		ArrayList<State> list = new RefBean().getStates();
		return list;
	}
	
	public ArrayList<LocalGovtArea> getLgs(String state)
	{
		ArrayList<LocalGovtArea> list = new RefBean().getLGs(state);
		return list;
	}
}
