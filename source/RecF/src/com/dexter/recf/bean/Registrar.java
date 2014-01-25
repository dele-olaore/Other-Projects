package com.dexter.recf.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagementType;
import javax.ejb.TransactionManagement;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.international.status.Messages;
import org.jboss.solder.logging.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import com.dexter.recf.i18n.RecFBundleKey;
import com.dexter.recf.model.EducationQualification;
import com.dexter.recf.model.Employment;
import com.dexter.recf.model.LocalGovtArea;
import com.dexter.recf.model.State;
import com.dexter.recf.model.UploadedDocument;
import com.dexter.recf.model.User;
import com.dexter.recf.model.UserEducationQualification;
import com.dexter.recf.model.UserEmployment;
import com.dexter.recf.model.UserProfile;
import com.dexter.recf.security.Authenticated;

@Stateful
@SessionScoped
@Named("registrar")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Registrar
{
	@PersistenceContext
    private EntityManager em;
	
	@Resource
    private SessionContext context;
	
	@Inject
	HttpServletRequest request;
	
	@Inject
    private Logger log;

    @Inject
    private Messages messages;

    @Inject
    private FacesContext facesContext;
    
    private UserProfile userProfile;
    private long sog_id, addrst_id;
    private long lgaog_id, addrlg_id;
    private UploadedDocument passport, cv;
    private EducationQualification eduQualification;
    private ArrayList<EducationQualification> eduQualificationsList;
    private Employment employment;
    private ArrayList<Employment> employmentList;
    private int currentQIndex;
    
    @Inject
    @Authenticated
    private User user;
    
    private boolean registrationValid, formValid;
    
    public void paint(OutputStream stream, Object object) throws IOException
    {
    	if(passport != null)
    	{
    		stream.write(passport.getData());
    		stream.flush();
    		stream.close();
    	}
    }
    
    public void passportlistener(FileUploadEvent event) throws Exception
    {
        UploadedFile item = event.getUploadedFile();
        passport = new UploadedDocument();
        passport.setLength(item.getData().length);
        passport.setName(item.getName());
        passport.setData(item.getData());
        passport.setType("PASSPORT");
    }
    
    public void cvlistener(FileUploadEvent event) throws Exception
    {
        UploadedFile item = event.getUploadedFile();
        cv = new UploadedDocument();
        cv.setLength(item.getData().length);
        cv.setName(item.getName());
        cv.setData(item.getData());
        cv.setType("CV");
    }
    
    /**
     * Adds a qualification to the list of qualifications.
     * */
    public void AddQualification()
    {
    	if(getEduQualification().getSchoolname() != null && getEduQualification().getQualification() != null && 
    			getEduQualification().getGradyr() > 0)
    	{
    		boolean duplicate = false;
    		for(EducationQualification e : getEduQualificationsList())
    		{
    			if(e.getSchoolname().equalsIgnoreCase(getEduQualification().getSchoolname()) &&
    					e.getQualification().equalsIgnoreCase(getEduQualification().getQualification()))
    			{
    	    		duplicate = true;
    				break;
    			}
    		}
    		if(!duplicate)
    		{
	    		getEduQualificationsList().add(getEduQualification());
	    		setEduQualification(null);
    		}
    		else
    		{
    			messages.info(new RecFBundleKey("record.duplicate"))
    				.defaults("Duplicate record detected! Please ensure you have not already added this entry.");
    		}
    	}
    	else
    	{
    		messages.info(new RecFBundleKey("record.invalid"))
    			.defaults("Invalid data entered! Please check your data.");
    	}
    }
    
    /**
     * Removes a qualification from the list.
     * */
    public void RemoveQualification()
    {
    	try
    	{
    		getEduQualificationsList().remove(getCurrentQIndex());
    	}
    	catch(Exception ig)
    	{}
    }
    
    /**
     * Adds an employment to the list of employments.
     * */
    public void AddEmployment()
    {
    	if(getEmployment().getEmployer() != null && getEmployment().getSalary() > 0)
    	{
    		getEmploymentList().add(getEmployment());
    		setEmployment(null);
    	}
    }
    
    /**
     * Removes an employment from the list.
     * */
    public void RemoveEmployment()
    {
    	try
    	{
    		getEmploymentList().remove(getCurrentQIndex());
    	}
    	catch(Exception ig)
    	{}
    }
    
    /**
     * Provides preview of information entered
     * */
    public void Preview()
    {
    	String ret = "form-preview";
    	
    	formValid = true;
    	
    	if(passport != null)
		{
			if(passport.getLength() > 60000)
			{
				ret = "fill-form";
				formValid = false;
				
				messages.info(new RecFBundleKey("password_invalid"))
        		.defaults("Passport too large. Passport size is a maximum of 60kb.");
				log.info("Passport too large. Passport size is a maximum of 60kb.");
			}
		}
    	
    	if(cv != null)
		{
    		if(cv.getLength() > 60000)
			{
				ret = "fill-form";
				formValid = false;
				
				messages.info(new RecFBundleKey("cv_invalid"))
        		.defaults("CV too large. CV size is a maximum of 60kb.");
				log.info("CV too large. CV size is a maximum of 60kb.");
			}
		}
    	
    	System.out.println("ret: " + ret);
    	
    	//return ret;
    }
    
    /**
     * Submits a user profile.
     * */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean register()
    {
    	//String ret = "form-preview";
    	registrationValid = false;
    	
    	if(user != null)
    	{
    		// set the user object
    		// getUserProfile().setUser(user);
    		
    		try
    		{
    			// utx.begin(); // start transaction block
    			
    			if(passport != null)
    			{
    				//em.persist(passport);
    				//em.flush();
    				
    				if(passport.getLength() > 60000)
    				{
    					messages.info(new RecFBundleKey("password_invalid"))
                		.defaults("Passport too large. Passport size is a maximum of 60kb.");
    					log.info("Passport too large. Passport size is a maximum of 60kb.");
    					try
    					{
    						context.setRollbackOnly();
    					}
    					catch(Exception ig){}
    					
    					return registrationValid;
    				}
    				
    				getUserProfile().setPassport_data(passport.getData());
    				getUserProfile().setPassport_length(passport.getLength());
    				getUserProfile().setPassport_name(passport.getName());
    				getUserProfile().setPassport_type(passport.getType());
    			}
    			
    			if(cv != null)
    			{
    				//em.persist(cv);
    				//em.flush();
    				
    				if(cv.getLength() > 60000)
    				{
    					messages.info(new RecFBundleKey("cv_invalid"))
                		.defaults("CV too large. CV size is a maximum of 60kb.");
    					log.info("CV too large. CV size is a maximum of 60kb.");
    					try
    					{
    						context.setRollbackOnly();
    					}
    					catch(Exception ig){}
    					
    					return registrationValid;
    				}
    				
    				getUserProfile().setCv_data(cv.getData());
    				getUserProfile().setCv_length(cv.getLength());
    				getUserProfile().setCv_name(cv.getName());
    				getUserProfile().setCv_type(cv.getType());
    			}
    			
    			for(EducationQualification e : getEduQualificationsList())
    			{
    				em.persist(e);
        			em.flush();
    				
    				UserEducationQualification ueq = new UserEducationQualification();
    				ueq.setEduQualification(e);
    				ueq.setUser(user);
    				
    				em.persist(ueq);
        			em.flush();
    			}
    			
    			for(Employment e : getEmploymentList())
    			{
    				em.persist(e);
        			em.flush();
    				
    				UserEmployment ue = new UserEmployment();
    				ue.setEmployment(e);
    				ue.setUser(user);
    				
    				em.persist(ue);
        			em.flush();
    			}
    			getUserProfile().setStatus(1); // submitted
    			em.merge(getUserProfile());
    			em.flush();
    			
    			// commit transaction at this point
    			// utx.commit();
    			
    			//ret = "logout";
    			registrationValid = true;
    			setUserProfile(null);
    			setCv(null);
    			setPassport(null);
    			setEmployment(null);
    			setEmploymentList(null);
    			setEduQualification(null);
    			setEduQualificationsList(null);
    			
    			request.getSession().invalidate();
    			
    			messages.info(new RecFBundleKey("registration_registered"))
                	.defaults("You have been successfully registered with the information provided. Thank you!");
    			
    			try
    			{
    				Query q = em.createQuery("SELECT e FROM UserProfile e WHERE user = :nm");
        	    	q.setParameter("nm", user);
    				setUserProfile((UserProfile)q.getSingleResult());
    				
    				log.info("UserProfile ID: " + getUserProfile().getId());
    				//log.info("UserProfile Address ID: " + getUserProfile().getAddress().getId() + ", Address: " + getUserProfile().getAddress().getAddress());
    			}
    			catch(Exception ex)
    			{
    				ex.printStackTrace();
    			}
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
    			messages.info(new RecFBundleKey("registration_invalid"))
            		.defaults("Invalid registration. Please correct the errors and try again. Message: " + ex.getMessage());
    			try
				{
    				context.setRollbackOnly();
				}
    			catch(Exception ig){}
    		}
    	}
    	else
    	{
    		messages.info(new RecFBundleKey("identity_invalidSession"))
            	.defaults("Your session has expired. Please sign in again to continue.");
    	}
    	
    	return registrationValid;
    }

    @Produces
    @Named("userProfile")
	public UserProfile getUserProfile()
	{
		if(userProfile == null)
		{
			try
        	{
	        	Query q = em.createQuery("SELECT e FROM UserProfile e WHERE user = :nm");
		    	q.setParameter("nm", user);
	        	UserProfile up = (UserProfile)q.getSingleResult();
	        	
	        	if(up != null)
	        	{
	        		userProfile = up;
	        	}
	        	else
	        		userProfile = new UserProfile();
        	}
        	catch(Exception ex)
        	{
        		ex.printStackTrace();
        		userProfile = new UserProfile();
        	}
		}
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile)
	{
		this.userProfile = userProfile;
	}

	public boolean getRegistrationValid() {
		return registrationValid;
	}

	public boolean getFormValid() {
		return formValid;
	}

	public void setFormValid(boolean formValid) {
		this.formValid = formValid;
	}

	public void setRegistrationValid(boolean registrationValid) {
		this.registrationValid = registrationValid;
	}

	public long getSog_id() {
		return sog_id;
	}

	public void setSog_id(long sog_id) {
		try
		{
			if(sog_id > 0)
			{
				try
				{
					getUserProfile().setStateoforigin((State) em.find(State.class, sog_id));
				}
				catch(Exception ex)
				{
					getUserProfile().setStateoforigin(null);
				}
			}
			else
				getUserProfile().setStateoforigin(null);
			this.sog_id = sog_id;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public long getLgaog_id() {
		return lgaog_id;
	}

	public void setLgaog_id(long lgaog_id) {
		try
		{
			if(lgaog_id > 0)
			{
				try
				{
					getUserProfile().setLocalgovtarea((LocalGovtArea) em.find(LocalGovtArea.class, lgaog_id));
				}
				catch(Exception ex)
				{
					getUserProfile().setLocalgovtarea(null);
				}
			}
			else
				getUserProfile().setLocalgovtarea(null);
			this.lgaog_id = lgaog_id;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public long getAddrst_id() {
		return addrst_id;
	}

	public void setAddrst_id(long addrst_id) {
		try
		{
			if(addrst_id > 0)
			{
				try
				{
					getUserProfile().setState((State) em.find(State.class, addrst_id));
				}
				catch(Exception ex)
				{
					getUserProfile().setState(null);
				}
			}
			else
				getUserProfile().setState(null);
			this.addrst_id = addrst_id;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}

	public long getAddrlg_id() {
		return addrlg_id;
	}

	public void setAddrlg_id(long addrlg_id) {
		try
		{
			if(addrlg_id > 0)
			{
				try
				{
					getUserProfile().setLgarea((LocalGovtArea) em.find(LocalGovtArea.class, addrlg_id));
				}
				catch(Exception ex)
				{
					getUserProfile().setLgarea(null);
				}
			}
			else
				getUserProfile().setLgarea(null);
			this.addrlg_id = addrlg_id;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public EducationQualification getEduQualification() {
		if(eduQualification == null)
			eduQualification = new EducationQualification();
		return eduQualification;
	}

	public void setEduQualification(EducationQualification eduQualification) {
		this.eduQualification = eduQualification;
	}

	public ArrayList<EducationQualification> getEduQualificationsList() {
		if(eduQualificationsList == null)
			eduQualificationsList = new ArrayList<EducationQualification>();
		return eduQualificationsList;
	}

	public void setEduQualificationsList(
			ArrayList<EducationQualification> eduQualificationsList) {
		this.eduQualificationsList = eduQualificationsList;
	}

	public Employment getEmployment() {
		if(employment == null)
			employment = new Employment();
		return employment;
	}

	public void setEmployment(Employment employment) {
		this.employment = employment;
	}

	public ArrayList<Employment> getEmploymentList() {
		if(employmentList == null)
			employmentList = new ArrayList<Employment>();
		return employmentList;
	}

	public void setEmploymentList(ArrayList<Employment> employmentList) {
		this.employmentList = employmentList;
	}

	public int getCurrentQIndex() {
		return currentQIndex;
	}

	public void setCurrentQIndex(int currentQIndex) {
		this.currentQIndex = currentQIndex;
	}
    
	public long getTimeStamp() {
        return System.currentTimeMillis();
    }

	public UploadedDocument getPassport() {
		return passport;
	}

	public void setPassport(UploadedDocument passport) {
		this.passport = passport;
	}

	public UploadedDocument getCv() {
		return cv;
	}

	public void setCv(UploadedDocument cv) {
		this.cv = cv;
	}
	
}
