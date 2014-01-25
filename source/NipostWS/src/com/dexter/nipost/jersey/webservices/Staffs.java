package com.dexter.nipost.jersey.webservices;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dexter.nipost.bean.PropertyBean;
import com.dexter.nipost.model.Property;
import com.dexter.nipost.response.BasicDataResponse;

@Path("/staffs")
public class Staffs
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public BasicDataResponse process(@FormParam("id") String id, @FormParam("staffs_count") String staffs_count,
			@FormParam("clerks_count") String clerks_count, @FormParam("staff_with_diploma_count") String staff_with_diploma_count,
			@FormParam("institute") String institute)
	{
		BasicDataResponse ret = new BasicDataResponse();
		
		PropertyBean pb = new PropertyBean();
		Property p = pb.getProperty(id);
		
		try
		{
			p.setStaffs_count(Integer.parseInt(staffs_count));
		}
		catch(Exception ig){}
		try
		{
			p.setClerks_count(Integer.parseInt(clerks_count));
		}
		catch(Exception ig){}
		try
		{
			p.setStaff_with_diploma_count(Integer.parseInt(staff_with_diploma_count));
		}
		catch(Exception ig){}
		p.setInstitute(institute);
		
		pb.updateProperty(p);
		
		ret.setStatus(true);
		ret.setMessage("success");
		
		return ret;
	}
}
