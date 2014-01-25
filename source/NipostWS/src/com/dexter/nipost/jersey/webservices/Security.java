package com.dexter.nipost.jersey.webservices;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dexter.nipost.bean.PropertyBean;
import com.dexter.nipost.model.Property;
import com.dexter.nipost.response.BasicDataResponse;

@Path("/security")
public class Security
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public BasicDataResponse process(@FormParam("id") String id, @FormParam("secstaff_count") String secstaff_count,
			@FormParam("sec_provider") String sec_provider, @FormParam("sec_provider_other") String sec_provider_other,
			@FormParam("strongroom") String strongroom, @FormParam("safes_count") String safes_count,
			@FormParam("safes_brand") String safes_brand)
	{
		BasicDataResponse ret = new BasicDataResponse();
		
		PropertyBean pb = new PropertyBean();
		Property p = pb.getProperty(id);
		
		try
		{
			p.setSecstaff_count(Integer.parseInt(secstaff_count));
		}
		catch(Exception ig){}
		p.setSec_provider(sec_provider);
		p.setSec_provider_other(sec_provider_other);
		try
		{
			p.setStrongroom(Boolean.parseBoolean(strongroom));
		}
		catch(Exception ig){}
		try
		{
			p.setSafes_count(Integer.parseInt(safes_count));
		}
		catch(Exception ig){}
		p.setSafes_brand(safes_brand);
		
		pb.updateProperty(p);
		
		ret.setStatus(true);
		ret.setMessage("success");
		
		return ret;
	}
}
