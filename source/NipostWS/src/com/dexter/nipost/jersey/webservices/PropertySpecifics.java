package com.dexter.nipost.jersey.webservices;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dexter.nipost.bean.PropertyBean;
import com.dexter.nipost.model.Property;
import com.dexter.nipost.response.BasicDataResponse;

@Path("/propertySpecifics")
public class PropertySpecifics
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public BasicDataResponse process(@FormParam("id") String id, @FormParam("building_type") String building_type,
			@FormParam("interior_photo") String interior_photo, @FormParam("exterior_photo") String exterior_photo,
			@FormParam("building_ownership") String building_ownership, @FormParam("land_ownership") String land_ownership, 
			@FormParam("building_year") String building_year,
			@FormParam("land_m2") String land_m2, @FormParam("outline_m2") String outline_m2, @FormParam("mailspace_m2") String mailspace_m2,
			@FormParam("ccspace_m2") String ccspace_m2, @FormParam("waiting_m2") String waiting_m2, @FormParam("backoffice_m2") String backoffice_m2,
			@FormParam("num_counters_p") String num_counters_p, @FormParam("num_counters_f") String num_counters_f)
	{
		BasicDataResponse ret = new BasicDataResponse();
		
		PropertyBean pb = new PropertyBean();
		Property p = pb.getProperty(id);
		
		p.setBuilding_type(building_type);
		try
		{
			p.setInterior_photo(interior_photo.getBytes());
		}
		catch(Exception ig){}
		try
		{
			p.setExterior_photo(exterior_photo.getBytes());
		}
		catch(Exception ig){}
		
		p.setBuilding_ownership(building_ownership);
		p.setLand_ownership(land_ownership);
		p.setBuilding_year(building_year);
		p.setLand_m2(land_m2);
		p.setOutline_m2(outline_m2);
		p.setMailspace_m2(mailspace_m2);
		p.setCcspace_m2(ccspace_m2);
		p.setWaiting_m2(waiting_m2);
		p.setBackoffice_m2(backoffice_m2);
		try
		{
			p.setNum_counters_p(Integer.parseInt(num_counters_p));
		}
		catch(Exception ig){}
		try
		{
			p.setNum_counters_f(Integer.parseInt(num_counters_f));
		}
		catch(Exception ig){}
		
		pb.updateProperty(p);
		
		ret.setStatus(true);
		ret.setMessage("success");
		
		return ret;
	}
}
