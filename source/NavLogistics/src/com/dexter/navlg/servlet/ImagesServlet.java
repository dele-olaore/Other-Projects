package com.dexter.navlg.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dexter.navlg.model.Item;

/**
 * Servlet implementation class ImagesServlet
 */
@WebServlet(description = "Servlet to render images", urlPatterns = { "/imageservlet/*" })
public class ImagesServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
    private EntityManager em;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImagesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String itemId = String.valueOf(request.getPathInfo().substring(1)); // Gets string that goes after "/images/".
		try
		{
			Long id = Long.parseLong(itemId);
			Item item = em.find(Item.class, id);
			if(item != null)
			{
				response.setHeader("Content-Type", getServletContext().getMimeType(item.getImg_type()));
		        response.setHeader("Content-Disposition", "inline; filename=\"" + item.getName() + "\"");

		        BufferedInputStream input = null;
		        BufferedOutputStream output = null;

		        try {
		            input = new BufferedInputStream(new ByteArrayInputStream(item.getImage())); // Creates buffered input stream.
		            output = new BufferedOutputStream(response.getOutputStream());
		            byte[] buffer = new byte[8192];
		            for (int length = 0; (length = input.read(buffer)) > 0;) {
		                output.write(buffer, 0, length);
		            }
		        } finally {
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
