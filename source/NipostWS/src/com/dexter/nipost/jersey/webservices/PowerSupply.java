package com.dexter.nipost.jersey.webservices;

import java.math.BigDecimal;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dexter.nipost.bean.PropertyBean;
import com.dexter.nipost.model.Property;
import com.dexter.nipost.response.BasicDataResponse;

@Path("/powerSupply")
public class PowerSupply
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public BasicDataResponse process(@FormParam("id") String id, @FormParam("phcn") String phcn,
			@FormParam("solar") String solar, @FormParam("solar_kwhs") String solar_kwhs,
			@FormParam("generators") String generators, @FormParam("generator_kwhs") String generator_kwhs,
			@FormParam("needed_kwhs") String needed_kwhs, @FormParam("provided_kwhs") String provided_kwhs)
	{
		BasicDataResponse ret = new BasicDataResponse();
		
		PropertyBean pb = new PropertyBean();
		Property p = pb.getProperty(id);
		
		try
		{
			if(phcn != null && phcn.equalsIgnoreCase("true"))
				p.setPhcn(true);
			else if(phcn != null && phcn.equalsIgnoreCase("false"))
				p.setPhcn(false);
		}
		catch(Exception ig){}
		try
		{
			if(solar != null && solar.equalsIgnoreCase("true"))
				p.setSolar(true);
			else if(solar != null && solar.equalsIgnoreCase("false"))
				p.setSolar(false);
		}
		catch(Exception ig){}
		try
		{
			p.setSolar_kwhs(new BigDecimal(Double.parseDouble(solar_kwhs)));
		}
		catch(Exception ig){}
		try
		{
			p.setGenerators(Boolean.parseBoolean(generators));
		}
		catch(Exception ig){}
		try
		{
			p.setGenerator_kwhs(new BigDecimal(Double.parseDouble(generator_kwhs)));
		}
		catch(Exception ig){}
		try
		{
			p.setNeeded_kwhs(new BigDecimal(Double.parseDouble(needed_kwhs)));
		}
		catch(Exception ig){}
		try
		{
			p.setProvided_kwhs(new BigDecimal(Double.parseDouble(provided_kwhs)));
		}
		catch(Exception ig){}
		
		pb.updateProperty(p);
		
		ret.setStatus(true);
		ret.setMessage("success");
		
		return ret;
	}
}
