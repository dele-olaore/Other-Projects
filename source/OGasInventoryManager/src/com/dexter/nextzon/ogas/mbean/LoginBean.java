package com.dexter.nextzon.ogas.mbean;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;

import com.dexter.nextzon.ogas.bean.UserBean;
import com.dexter.nextzon.ogas.common.UserAgentInfo;
import com.dexter.nextzon.ogas.model.Function;
import com.dexter.nextzon.ogas.model.Organization;
import com.dexter.nextzon.ogas.model.Role;
import com.dexter.nextzon.ogas.model.RoleFunction;
import com.dexter.nextzon.ogas.model.User;
import com.dexter.nextzon.ogas.util.Hasher;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean
{
	private static final String PERSISTENCE_UNIT_NAME = "ogas";
    private static EntityManagerFactory factory;
    private EntityManager em;
	
    private EntityTransaction utx;
    
    final Logger logger = Logger.getLogger("OGas-LoginBean");
	
	private FacesMessage msg = null;
	
	private String email;
	private String password;
	
	private User loggedInUser;
	
	@ManagedProperty(value="#{userMBean}")
	private UserManagementBean umBean; 
	
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
			
			umBean.setLoggedInUser(loggedInUser);
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
			Role rAdmin = new Role();
			rAdmin.setName("Admin");
			rAdmin.setDescription("Role for administrators of their respective organizations");
			
			Role rMd = new Role();
			rMd.setName("MD");
			rMd.setDescription("Managing directors role for their respective organizations");
			
			Role rSp = new Role();
			rSp.setName("Supervisor");
			rSp.setDescription("Supervisor role for their respective organizations");
			
			Role rOp = new Role();
			rOp.setName("Operator");
			rOp.setDescription("Operator role for their respective organizations");
			
			Organization nextzon = new Organization();
			nextzon.setName("Nextzon Limited");
			nextzon.setType("NEXTZON");
			
			Organization oando = new Organization();
			oando.setName("Oando Plc");
			oando.setType("OANDO");
			
			Function mngRole = new Function();
			mngRole.setModule("Maintenance");
			mngRole.setName("Manage Role");
			mngRole.setViewUrl("manage_role.xhtml");
			
			Function mngMFB = new Function();
			mngMFB.setModule("Maintenance");
			mngMFB.setName("Manage MFB");
			mngMFB.setViewUrl("manage_mfbs.xhtml");
			
			Function mngRet = new Function();
			mngRet.setModule("Maintenance");
			mngRet.setName("Manage Retailer");
			mngRet.setViewUrl("manage_rets.xhtml");
			
			Function mngSta = new Function();
			mngSta.setModule("Maintenance");
			mngSta.setName("Manage Station");
			mngSta.setViewUrl("manage_stats.xhtml");
			
			Function mngUsr = new Function();
			mngUsr.setModule("Maintenance");
			mngUsr.setName("Manage User");
			mngUsr.setViewUrl("manage_users.xhtml");
			
			User user = new User();
			user.setEmail("dele.olaore@gmail.com");
			user.setPassword(Hasher.getHashValue("dexter"));
			user.setFullname("Dele Olaore");
			user.setActive(true);
			user.setMobile1("08026966835");
			
			User user2 = new User();
			user2.setEmail("victorokere@gmail.com");
			user2.setPassword(Hasher.getHashValue("dexter"));
			user2.setFullname("Victor Okere");
			user2.setActive(true);
			user2.setMobile1("07062888566");
			
			User user3 = new User();
			user3.setEmail("segun.olukoya@gmail.com");
			user3.setPassword(Hasher.getHashValue("123456"));
			user3.setFullname("Segun Olukoya");
			user3.setActive(true);
			user3.setMobile1("");
			
			utx = em.getTransaction();
            utx.begin();
            
            em.persist(rAdmin);
            em.persist(rMd);
            em.persist(rSp);
            em.persist(rOp);
            
            em.persist(nextzon);
            em.persist(oando);
            
            em.persist(mngRole);
            em.persist(mngMFB);
            em.persist(mngRet);
            em.persist(mngSta);
            em.persist(mngUsr);
            
            RoleFunction rf = new RoleFunction();
            rf.setRole(rAdmin);
            rf.setFunction(mngRole);
            em.persist(rf);
            rf = new RoleFunction();
            rf.setRole(rAdmin);
            rf.setFunction(mngMFB);
            em.persist(rf);
            rf = new RoleFunction();
            rf.setRole(rAdmin);
            rf.setFunction(mngRet);
            em.persist(rf);
            rf = new RoleFunction();
            rf.setRole(rAdmin);
            rf.setFunction(mngSta);
            em.persist(rf);
            rf = new RoleFunction();
            rf.setRole(rAdmin);
            rf.setFunction(mngUsr);
            em.persist(rf);
            
            user.setOrganization(nextzon);
            user.setRole(rAdmin);
            
            user2.setOrganization(oando);
            user2.setRole(rAdmin);
            
            user3.setOrganization(oando);
            user3.setRole(rMd);
            
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

	public UserManagementBean getUmBean() {
		return umBean;
	}

	public void setUmBean(UserManagementBean umBean) {
		this.umBean = umBean;
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
