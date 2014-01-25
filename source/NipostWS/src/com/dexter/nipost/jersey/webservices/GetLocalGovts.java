package com.dexter.nipost.jersey.webservices;

import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dexter.nipost.bean.RefBean;
import com.dexter.nipost.model.LocalGovtArea;

@Path("/getLGs")
public class GetLocalGovts
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<LocalGovtArea> process()
	{
		ArrayList<LocalGovtArea> list = new RefBean().getLGs();
		if(list == null)
			list = new ArrayList<LocalGovtArea>();
		
		return list;
	}
}
