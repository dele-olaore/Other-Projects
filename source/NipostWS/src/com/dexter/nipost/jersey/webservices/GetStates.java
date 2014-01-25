package com.dexter.nipost.jersey.webservices;

import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dexter.nipost.bean.RefBean;
import com.dexter.nipost.model.State;

@Path("/getStates")
public class GetStates
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<State> process()
	{
		ArrayList<State> list = new RefBean().getStates();
		if(list == null)
			list = new ArrayList<State>();
		
		return list;
	}
}
