package com.dexter.nipost.jersey.webservices;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dexter.nipost.bean.UserBean;
import com.dexter.nipost.model.User;
import com.dexter.nipost.response.AuthenticateResponse;
import com.dexter.nipost.util.Secure;

@Path("/authenticate")
public class AuthenticateUser
{
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public AuthenticateResponse process(@FormParam("email") String email, @FormParam("password") String password)
	{
		AuthenticateResponse ret = new AuthenticateResponse();
		
		/*System.out.println("Encrypted : email: " + email + ", password: " + password);
		
		Secure secure = new Secure();
		String emailPlain = secure.Decrypt(email);
		String passwordPlain = secure.Decrypt(password);
		
		System.out.println("email: " + emailPlain + ", password: " + passwordPlain);*/
		
		User u = new UserBean().authenticateUser(email, password);
		if(u != null)
		{
			ret.setSuccess(true);
			ret.setUser_id(""+u.getId());
			ret.setFullname(u.getFullname());
		}
		else
		{
			ret.setSuccess(false);
			ret.setMessage("Invalid credentials! Please try again.");
		}
		
		return ret;
	}
}
