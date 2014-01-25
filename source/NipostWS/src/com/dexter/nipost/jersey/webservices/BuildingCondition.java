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

@Path("/buildingCondition")
public class BuildingCondition
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public BasicDataResponse process(@FormParam("id") String id, @FormParam("ccspace_cond") String ccspace_cond,
			@FormParam("ccspace_cond2") String ccspace_cond2,
			@FormParam("ccspace_photo") String ccspace_photo, @FormParam("building_cond") String building_cond,
			@FormParam("building_cond2") String building_cond2,
			@FormParam("building_photo") String building_photo, @FormParam("renovation_cost") String renovation_cost)
	{
		BasicDataResponse ret = new BasicDataResponse();
		
		PropertyBean pb = new PropertyBean();
		Property p = pb.getProperty(id);
		
		p.setCcspace_cond(ccspace_cond);
		p.setCcspace_cond2(ccspace_cond2);
		try
		{
			p.setCcspace_photo(ccspace_photo.getBytes());
		}
		catch(Exception ex)
		{}
		p.setBuilding_cond(building_cond);
		p.setBuilding_cond2(building_cond2);
		try
		{
			p.setBuilding_photo(building_photo.getBytes());
		}
		catch(Exception ex)
		{}
		try
		{
			p.setRenovation_cost(new BigDecimal(Double.parseDouble(renovation_cost)));
		}
		catch(Exception ex)
		{}
		
		pb.updateProperty(p);
		
		ret.setStatus(true);
		ret.setMessage("success");
		
		return ret;
	}
}
