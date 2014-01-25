package com.dexter.nipost.jersey.webservices;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dexter.nipost.bean.PropertyBean;
import com.dexter.nipost.model.Property;
import com.dexter.nipost.response.BasicDataResponse;

@Path("/productsOffered")
public class ProductsOffered
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public BasicDataResponse process(@FormParam("id") String id, @FormParam("post_cash") String post_cash,
			@FormParam("stamps") String stamps, @FormParam("letter_box") String letter_box,
			@FormParam("po_box") String po_box, @FormParam("ems") String ems,
			@FormParam("parcels") String parcels, @FormParam("philately") String philately,
			@FormParam("public_internet") String public_internet, @FormParam("public_phone") String public_phone, 
			@FormParam("customs") String customs, @FormParam("netpost") String netpost,
			@FormParam("cards_office_supplies") String cards_office_supplies, @FormParam("atm") String atm, 
			@FormParam("others_offered") String others_offered)
	{
		BasicDataResponse ret = new BasicDataResponse();
		
		PropertyBean pb = new PropertyBean();
		Property p = pb.getProperty(id);
		
		try
		{
			p.setPost_cash(Boolean.parseBoolean(post_cash));
		}
		catch(Exception ig){}
		try
		{
			p.setStamps(Boolean.parseBoolean(stamps));
		}
		catch(Exception ig){}
		try
		{
			p.setLetter_box(Boolean.parseBoolean(letter_box));
		}
		catch(Exception ig){}
		try
		{
			p.setPo_box(Boolean.parseBoolean(po_box));
		}
		catch(Exception ig){}
		try
		{
			p.setEms(Boolean.parseBoolean(ems));
		}
		catch(Exception ig){}
		try
		{
			p.setParcels(Boolean.parseBoolean(parcels));
		}
		catch(Exception ig){}
		try
		{
			p.setPhilately(Boolean.parseBoolean(philately));
		}
		catch(Exception ig){}
		try
		{
			p.setPublic_internet(Boolean.parseBoolean(public_internet));
		}
		catch(Exception ig){}
		try
		{
			p.setPublic_phone(Boolean.parseBoolean(public_phone));
		}
		catch(Exception ig){}
		try
		{
			p.setCustoms(Boolean.parseBoolean(customs));
		}
		catch(Exception ig){}
		try
		{
			p.setNetpost(Boolean.parseBoolean(netpost));
		}
		catch(Exception ig){}
		try
		{
			p.setCards_office_supplies(Boolean.parseBoolean(cards_office_supplies));
		}
		catch(Exception ig){}
		try
		{
			p.setAtm(Boolean.parseBoolean(atm));
		}
		catch(Exception ig){}
		p.setOthers_offered(others_offered);
		
		pb.updateProperty(p);
		
		ret.setStatus(true);
		ret.setMessage("success");
		
		return ret;
	}
}
