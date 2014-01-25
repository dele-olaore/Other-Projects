package com.dexter.nipost.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dexter.nipost.mbean.PropertyManagementBean;
import com.dexter.nipost.model.Property;

/**
 * Servlet implementation class ImagesServlet
 */
@WebServlet(description = "Servlet to render images", urlPatterns = { "/imageservlet/*" })
public class ImagesServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	private static final String PERSISTENCE_UNIT_NAME = "nipost";
    private static EntityManagerFactory factory;
    private EntityManager em;
    
    final Logger logger = Logger.getLogger("NipostWS-ImagesServlet");
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImagesServlet()
    {
        super();
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		em = factory.createEntityManager();
		
		String details = String.valueOf(request.getPathInfo().substring(1)); // Gets string that goes after "/imageservlet/".
		
		String property_id = details.split(":")[0];
		String photo = details.split(":")[1];
		try
		{
			Long id = Long.parseLong(property_id);
			Property p = em.find(Property.class, id);
			if(p != null)
			{
				response.setHeader("Content-Type", getServletContext().getMimeType("image/jpeg"));
		        response.setHeader("Content-Disposition", "inline; filename=\"photo\"");

		        BufferedInputStream input = null;
		        BufferedOutputStream output = null;

		        try
		        {
		        	if(photo.equalsIgnoreCase("interior"))
		        		input = new BufferedInputStream(new ByteArrayInputStream(p.getInterior_photo())); // Creates buffered input stream.
		        	else if(photo.equalsIgnoreCase("exterior"))
		        		input = new BufferedInputStream(new ByteArrayInputStream(p.getExterior_photo())); // Creates buffered input stream.
		        	else if(photo.equalsIgnoreCase("ccspace"))
		        		input = new BufferedInputStream(new ByteArrayInputStream(p.getCcspace_photo())); // Creates buffered input stream.
		        	else if(photo.equalsIgnoreCase("building"))
		        		input = new BufferedInputStream(new ByteArrayInputStream(p.getBuilding_photo())); // Creates buffered input stream.
		        	
		            output = new BufferedOutputStream(response.getOutputStream());
		            byte[] buffer = new byte[8192];
		            for (int length = 0; (length = input.read(buffer)) > 0;) {
		                output.write(buffer, 0, length);
		            }
		        }
		        finally
		        {
		            if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
		            if (input != null) try { input.close(); } catch (IOException logOrIgnore) {}
		        }
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
