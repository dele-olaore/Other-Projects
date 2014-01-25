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

@Path("/ict")
public class Ict
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public BasicDataResponse process(@FormParam("id") String id, @FormParam("functioning_comp") String functioning_comp,
			@FormParam("newest_ramsize") String newest_ramsize, @FormParam("newest_processor") String newest_processor,
			@FormParam("datacon_type") String datacon_type, @FormParam("datacon_speed") String datacon_speed,
			@FormParam("land_phones") String land_phones, @FormParam("mobile_phones") String mobile_phones,
			@FormParam("fax_count") String fax_count, @FormParam("photocopy_count") String photocopy_count,
			@FormParam("printer_count") String printer_count)
	{
		BasicDataResponse ret = new BasicDataResponse();
		
		PropertyBean pb = new PropertyBean();
		Property p = pb.getProperty(id);
		
		try
		{
			p.setFunctioning_comp(Integer.parseInt(functioning_comp));
		}
		catch(Exception ig){}
		try
		{
			p.setNewest_ramsize(Integer.parseInt(newest_ramsize));
		}
		catch(Exception ig){}
		p.setNewest_processor(newest_processor);
		p.setDatacon_type(datacon_type);
		try
		{
			p.setDatacon_speed(new BigDecimal(Double.parseDouble(datacon_speed)));
		}
		catch(Exception ig){}
		try{
			p.setLand_phones(Integer.parseInt(land_phones));
		}
		catch(Exception ig){}
		try{
			p.setMobile_phones(Integer.parseInt(mobile_phones));
				}
				catch(Exception ig){}
		try{
			p.setFax_count(Integer.parseInt(fax_count));
		}
		catch(Exception ig){}
		try{
			p.setPhotocopy_count(Integer.parseInt(photocopy_count));
		}
		catch(Exception ig){}
		try{
			p.setPrinter_count(Integer.parseInt(printer_count));
		}
		catch(Exception ig){}
		
		pb.updateProperty(p);
		
		ret.setStatus(true);
		ret.setMessage("success");
		
		return ret;
	}
}
