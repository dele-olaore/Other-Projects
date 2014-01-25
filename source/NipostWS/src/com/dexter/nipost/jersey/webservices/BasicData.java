package com.dexter.nipost.jersey.webservices;

import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dexter.nipost.bean.PropertyBean;
import com.dexter.nipost.model.Property;
import com.dexter.nipost.response.BasicDataResponse;

@Path("/basicData")
public class BasicData
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public BasicDataResponse process(@FormParam("state") String state, @FormParam("lg") String lg, @FormParam("addr") String addr,
			@FormParam("type") String type, @FormParam("postalcode") String postalcode,
			@FormParam("lon") String lon, @FormParam("lat") String lat, @FormParam("rural") String rural,
			@FormParam("place_exists") String place_exists, @FormParam("remarks") String remarks, @FormParam("usr_id") String usr_id)
	{
		BasicDataResponse ret = new BasicDataResponse();
		
		Property property = new Property();
		property.setState(state);
		property.setLg(lg);
		property.setAddr(addr);
		property.setType(type);
		property.setPostalcode(postalcode);
		property.setLon(lon);
		property.setLat(lat);
		property.setRural(rural);
		property.setPlace_exists(place_exists);
		property.setRemarks(remarks);
		property.setUser_id(usr_id);
		property.setCrt_dt(new Date());
		
		String r = new PropertyBean().persistProperty(property);
		if(r.equals("success"))
		{
			ret.setId(property.getId());
			ret.setStatus(true);
			ret.setMessage("success");
		}
		else
		{
			ret.setStatus(false);
			ret.setMessage("error");
		}
		
		return ret;
	}
}
