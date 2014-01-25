package com.dexter.navlg.bean;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

@Stateful
@SessionScoped
@Named("dashboardBean")
public class DashboardBean implements Serializable
{
	private DashboardModel model;
	
	public String gotoPage(String page)
	{
		return page;
	}
	
	public DashboardBean()
	{
		model = new DefaultDashboardModel();
		DashboardColumn column1 = new DefaultDashboardColumn();
		DashboardColumn column2 = new DefaultDashboardColumn();
		//DashboardColumn column3 = new DefaultDashboardColumn();
		
		column1.addWidget("warehouses");
		column1.addWidget("ships");
		column1.addWidget("users");
		
		model.addColumn(column1);
		model.addColumn(column2);
		//model.addColumn(column3);
	}
	
	public DashboardModel getModel() {  
        return model;  
    }
}
