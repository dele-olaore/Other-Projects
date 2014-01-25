package com.dexter.nipost.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.dexter.nipost.bean.UserBean;
import com.dexter.nipost.common.UserAgentInfo;
import com.dexter.nipost.model.User;
import com.dexter.nipost.util.Hasher;

@ManagedBean(name = "userMBean")
@SessionScoped
public class UserManagementBean implements Serializable
{
	private static final long serialVersionUID = 1533253630500069084L;

	final Logger logger = Logger.getLogger("NipostWS-UserManagementBean");
	
	private FacesMessage msg = null;
	
	private User userObj;
	private ArrayList<User> usersList;
	
	private String confirmPassword;
	
	public String createUser()
	{
		if(getUserObj().getEmail() != null && getUserObj().getPassword() != null)
		{
			if(getUserObj().getPassword().equals(getConfirmPassword()))
			{
				String hashPassword = Hasher.getHashValue(getUserObj().getPassword());
				getUserObj().setPassword(hashPassword);
				
				String ret = new UserBean().persistUser(getUserObj());
				if(ret.equals("success"))
				{
					setUserObj(null);
					setUsersList(null);
					msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User created successfully!");
				}
				else
					msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "User create failed. Please ensure all fields are filled and record does not exist.");
				
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
		
		return "users";
	}

	public User getUserObj() {
		if(userObj == null)
			userObj = new User();
		return userObj;
	}

	public void setUserObj(User userObj) {
		this.userObj = userObj;
	}

	public ArrayList<User> getUsersList() {
		if(usersList == null)
			usersList = new UserBean().getUsers();
		
		return usersList;
	}

	public void setUsersList(ArrayList<User> usersList) {
		this.usersList = usersList;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	private boolean isMobile()
	{
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		String userAgent = request.getHeader("user-agent");
        String accept = request.getHeader("Accept");

        boolean isMobile = true;
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
