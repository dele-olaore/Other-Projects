package com.dexter.nipost.mbean;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;

import com.dexter.nipost.bean.UserBean;
import com.dexter.nipost.common.UserAgentInfo;
import com.dexter.nipost.model.User;
import com.dexter.nipost.util.Hasher;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean
{
	private static final String PERSISTENCE_UNIT_NAME = "nipost";
    private static EntityManagerFactory factory;
    private EntityManager em;
	
    private EntityTransaction utx;
    
    final Logger logger = Logger.getLogger("NipostWS-UserBean");
	
	private FacesMessage msg = null;
	
	private String email;
	private String password;
	
	private User loggedInUser;
	
	public LoginBean()
	{
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
	}

	public String login()
	{
		String ret = "";
		
		loggedInUser = new UserBean().authenticateUser(getEmail(), getPassword());
		if(loggedInUser == null)
		{
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email or Password in invalid", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			if(!isMobile())
				ret = "login";
			else
				ret = "mlogin";
		}
		else
		{
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", loggedInUser);
			if(!isMobile())
				ret = "home";
			else
				ret = "mhome";
		}
		
		return ret;
	}
	
	public String logout()
	{
		String ret = "login";
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
		msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Log out successful!", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		return ret;
	}
	
	public void create()
	{
		try
		{
			User user = new User();
			user.setEmail("dele.olaore@gmail.com");
			user.setPassword(Hasher.getHashValue("dexter"));
			user.setFullname("Dele Olaore");
			user.setActive(true);
			
			User user2 = new User();
			user2.setEmail("victorokere@gmail.com");
			user2.setPassword(Hasher.getHashValue("dexter"));
			user2.setFullname("Victor Okere");
			user2.setActive(true);
			
			User user3 = new User();
			user3.setEmail("demo@nipost.com");
			user3.setPassword(Hasher.getHashValue("123456"));
			user3.setFullname("Nipost Demo");
			user3.setActive(true);
			
			utx = em.getTransaction();
            utx.begin();
            em.persist(user);
            em.persist(user2);
            em.persist(user3);
            try
            {
                em.flush();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            utx.commit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			logger.log(Level.SEVERE, "Persist failed for user. " + ex);
            try
            {
                if (utx != null && utx.isActive())
                {
                    try
                    {
                        utx.rollback();
                    }
                    catch (Exception rbe)
                    {
                        logger.log(Level.SEVERE, "Error rolling back transaction", rbe);
                    }
                }
            }
            catch (Exception se)
            {}
		}
		finally
		{}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
	
	private boolean isMobile()
	{
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		String userAgent = request.getHeader("user-agent");
        String accept = request.getHeader("Accept");

        boolean isMobile = false;
        if (userAgent != null && accept != null)
        {
            UserAgentInfo agent = new UserAgentInfo(userAgent, accept);
            if (agent.isMobileDevice())
            {
            	isMobile = true;
            }
        }
        
        return isMobile;
	}
}
