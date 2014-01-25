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

@Path("/customerService")
public class CustomerService
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public BasicDataResponse process(@FormParam("id") String id, @FormParam("week_workhours") String week_workhours,
			@FormParam("open_time") String open_time, @FormParam("close_time") String close_time,
			@FormParam("cus_perday") String cus_perday, @FormParam("cus_perweek") String cus_perweek,
			@FormParam("trans_perweek") String trans_perweek)
	{
		BasicDataResponse ret = new BasicDataResponse();
		
		PropertyBean pb = new PropertyBean();
		Property p = pb.getProperty(id);
		
		try
		{
			p.setWeek_workhours(new BigDecimal(Double.parseDouble(week_workhours)));
		}
		catch(Exception ex){}
		p.setOpen_time(open_time);
		p.setClose_time(close_time);
		try
		{
			p.setCus_perday(Integer.parseInt(cus_perday));
		}
		catch(Exception ex){}
		try
		{
			p.setCus_perweek(Integer.parseInt(cus_perweek));
		}
		catch(Exception ex){}
		try
		{
			p.setTrans_perweek(Integer.parseInt(trans_perweek));
		}
		catch(Exception ex){}
		
		pb.updateProperty(p);
		
		ret.setStatus(true);
		ret.setMessage("success");
		
		return ret;
	}
}
