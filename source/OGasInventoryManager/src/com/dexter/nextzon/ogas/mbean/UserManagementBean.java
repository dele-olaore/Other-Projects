package com.dexter.nextzon.ogas.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.dexter.nextzon.ogas.bean.UserBean;
import com.dexter.nextzon.ogas.common.UserAgentInfo;
import com.dexter.nextzon.ogas.model.Function;
import com.dexter.nextzon.ogas.model.Role;
import com.dexter.nextzon.ogas.model.RoleFunction;
import com.dexter.nextzon.ogas.model.User;
import com.dexter.nextzon.ogas.util.Hasher;

@ManagedBean(name = "userMBean")
@SessionScoped
public class UserManagementBean implements Serializable
{
	private static final long serialVersionUID = 1533253630500069084L;

	final Logger logger = Logger.getLogger("OGas-UserManagementBean");
	
	private FacesMessage msg = null;
	
	private User loggedInUser;
	private Vector<RoleFunction> curUserFunctions;
	
	private Role roleObj, selectedRoleObj;
	private Vector<Role> rolesList;
	
	private Vector<Function> functions, selectedRoleFunctions;
	
	private User userObj;
	private ArrayList<User> usersList;
	
	private String confirmPassword;
	
	public void createRole()
	{
		
	}
	
	public void editRole()
	{
		
	}
	
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

	public Role getRoleObj() {
		if(roleObj == null)
			roleObj = new Role();
		return roleObj;
	}

	public void setRoleObj(Role roleObj) {
		this.roleObj = roleObj;
	}

	public Role getSelectedRoleObj() {
		return selectedRoleObj;
	}

	public void setSelectedRoleObj(Role selectedRoleObj) {
		this.selectedRoleObj = selectedRoleObj;
		if(selectedRoleObj != null)
		{
			selectedRoleFunctions = new Vector<Function>();
			for(RoleFunction e : selectedRoleObj.getFunctions())
			{
				Function f = e.getFunction();
				f.setSelected(true);
				selectedRoleFunctions.add(f);
			}
		}
		else
			selectedRoleFunctions = null;
	}

	public Vector<Role> getRolesList() {
		if(rolesList == null)
		{
			UserBean ub = new UserBean();
			rolesList = ub.getRoles();
			for(Role e : rolesList)
			{
				e.setFunctions(ub.getRoleFunctions(e));
			}
		}
		return rolesList;
	}

	public void setRolesList(Vector<Role> rolesList) {
		this.rolesList = rolesList;
	}

	public Vector<Function> getFunctions() {
		if(functions == null)
			functions = new UserBean().getFunctions();
		return functions;
	}

	public void setFunctions(Vector<Function> functions) {
		this.functions = functions;
	}

	public Vector<Function> getSelectedRoleFunctions() {
		return selectedRoleFunctions;
	}

	public void setSelectedRoleFunctions(Vector<Function> selectedRoleFunctions) {
		this.selectedRoleFunctions = selectedRoleFunctions;
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
	
	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
		
		setCurUserFunctions(new UserBean().getRoleFunctions(this.loggedInUser.getRole()));
	}

	public Vector<RoleFunction> getCurUserFunctions() {
		if(curUserFunctions == null)
			curUserFunctions = new Vector<RoleFunction>();
		return curUserFunctions;
	}

	public void setCurUserFunctions(Vector<RoleFunction> curUserFunctions) {
		this.curUserFunctions = curUserFunctions;
	}
	
	public boolean isModuleAvailableForUser(String module)
	{
		boolean ret = false;
		Vector<RoleFunction> el = getCurUserFunctions();
		for(RoleFunction e : el)
		{
			if(e.getFunction().getModule().equalsIgnoreCase(module))
			{
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public boolean isFunctionAvailableForUser(String module, String function)
	{
		boolean ret = false;
		ArrayList<Function> functions = getFunctionsForModuleForCurUser(module);
		for(Function e : functions)
		{
			if(e.getName().equalsIgnoreCase(function))
			{
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public ArrayList<Function> getFunctionsForModuleForCurUser(String module)
	{
		ArrayList<Function> list = new ArrayList<Function>();
		Vector<RoleFunction> el = getCurUserFunctions();
		for(RoleFunction e : el)
		{
			if(e.getFunction().getModule().equalsIgnoreCase(module))
			{
				list.add(e.getFunction());
			}
		}
		
		return list;
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
