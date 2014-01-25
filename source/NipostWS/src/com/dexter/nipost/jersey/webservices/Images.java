package com.dexter.nipost.jersey.webservices;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dexter.nipost.bean.PropertyBean;
import com.dexter.nipost.model.Property;
import com.dexter.nipost.response.BasicDataResponse;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.multipart.MultiPart;

@Path("/images")
public class Images
{
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public BasicDataResponse process(@FormDataParam("photo_content") InputStream source, 
			@FormDataParam("photo_content") FormDataContentDisposition desc,
			@FormDataParam("p_id") String p_id, @FormDataParam("photo") String photo)
	{
		BasicDataResponse ret = new BasicDataResponse();
		
		try
		{
			/*String p_id =  multiPart.getBodyParts().get(0).getEntityAs(String.class);
			
			BodyPartEntity bpe = (BodyPartEntity)multiPart.getBodyParts().get(1).getEntity();
			InputStream source = bpe.getInputStream();*/
			
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int read;
			byte[] input = new byte[4096];
			while(-1 != (read = source.read(input)))
			{
			    buffer.write(input, 0, read);
			}
			byte[] photo_content = buffer.toByteArray();
			
			//String photo =  multiPart.getBodyParts().get(2).getEntityAs(String.class);
			
			PropertyBean pBean = new PropertyBean();
			
			Property p = pBean.getProperty(p_id);
			if(p != null)
			{
				if(photo.equalsIgnoreCase("interior"))
					p.setInterior_photo(photo_content);
				else if(photo.equalsIgnoreCase("exterior"))
					p.setExterior_photo(photo_content);
				else if(photo.equalsIgnoreCase("ccspace"))
					p.setCcspace_photo(photo_content);
				else if(photo.equalsIgnoreCase("building"))
					p.setBuilding_photo(photo_content);
				
				String r = pBean.updateProperty(p);
				if(r.equalsIgnoreCase("success"))
				{
					ret.setStatus(true);
					ret.setMessage("success");
				}
				else
				{
					ret.setStatus(false);
					ret.setMessage(r);
				}
			}
			else
			{
				ret.setStatus(false);
				ret.setMessage("Property not found!");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			ret.setStatus(false);
			ret.setMessage(ex.getMessage());
		}
		return ret;
	}

}
