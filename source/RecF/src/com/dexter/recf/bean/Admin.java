package com.dexter.recf.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.jboss.seam.international.status.Messages;
import org.jboss.solder.logging.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import com.dexter.recf.i18n.RecFBundleKey;
import com.dexter.recf.model.LocalGovtArea;
import com.dexter.recf.model.Role;
import com.dexter.recf.model.State;
import com.dexter.recf.model.UploadedDocument;
import com.dexter.recf.model.User;
import com.dexter.recf.model.UserEducationQualification;
import com.dexter.recf.model.UserEmployment;
import com.dexter.recf.model.UserProfile;

import jxl.*;

@Stateful
@SessionScoped
@Named("admin")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Admin
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
    
    private String[] letters = new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","1","2","3","4","5","6","7","8","9","0","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private UploadedFile item;
    private int success, failed;
    
    private String firstname, middlename, lastname;
    private long sog_id, lgaog_id, yearsofexp, addrst_id, addrlg_id, status_id;
    private ArrayList<UserProfile> matchingProfiles;
    private UserProfile viewingProfile;
    
    private String mimetype;
    private String filename;
    private byte[] data;
    
    private long pending;
    private long total;
    
    public void excelListener(FileUploadEvent event) throws Exception
    {
        item = event.getUploadedFile();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void loadUsers()
    {
    	if(item != null)
    	{
    		// Here we read the content of the excel and load each row to the user table.
    		try
    		{
    			Workbook wk = Workbook.getWorkbook(new ByteArrayInputStream(item.getData()));
    			Sheet sht = wk.getSheet(0);
            	if(sht != null)
            	{
            		List<UserProfile> profiles = new ArrayList<UserProfile>();
            	
            		Query q = em.createQuery("SELECT e FROM Role e WHERE name = :nm");
        	    	q.setParameter("nm", "Candidate");
        	    	Role cadRole = (Role) q.getSingleResult();
            		
            		int rows = sht.getRows();
            		for(int i=0; i<rows; i++)
            		{
            			String test_id = sht.getCell(0, i).getContents();
            			String lastn = sht.getCell(1, i).getContents();
            			String firstn = sht.getCell(2, i).getContents();
            			String middlen = sht.getCell(3, i).getContents();
            			String email = sht.getCell(4, i).getContents();
            			String tell = sht.getCell(5, i).getContents();
            			String state = sht.getCell(6, i).getContents();
            			String gender = sht.getCell(7, i).getContents();
            			String pwd = sht.getCell(8, i).getContents();
            			
            			q = em.createQuery("SELECT e FROM State e WHERE name = :nm");
            	    	q.setParameter("nm", "N/A");
            	    	State na_st = null;
            	    	try
            	    	{
            	    		na_st = (State) q.getSingleResult();
            	    	}
            	    	catch(Exception ex)
            	    	{}
            	    	
            	    	q = em.createQuery("SELECT e FROM LocalGovtArea e WHERE name = :nm");
            	    	q.setParameter("nm", "N/A");
            	    	LocalGovtArea na_lg = null;
            	    	try
            	    	{
            	    		na_lg = (LocalGovtArea) q.getSingleResult();
            	    	}
            	    	catch(Exception ex)
            	    	{}
            			
            			UserProfile up = new UserProfile();
            			up.setTestid(test_id);
            			up.setLastname(lastn);
            			up.setFirstname(firstn);
            			up.setMiddlename(middlen);
            			up.setEmail(email);
            			up.setMobileno1(tell);
            			up.setGender(gender);
            			up.setLocalgovtarea(na_lg);
            			up.setState(na_st);
            			up.setLgarea(na_lg);
            			up.setStateoforigin(na_st);
            			
            			q = em.createQuery("SELECT e FROM State e WHERE name = :nm");
            	    	q.setParameter("nm", state);
            	    	try
            	    	{
            	    		State st = (State) q.getSingleResult();
            	    		if(st != null)
            	    			up.setStateoforigin(st);
            	    	}
            	    	catch(Exception ex)
            	    	{}
            	    	
            	    	User u = new User();
            	    	u.setUsername(test_id);
            	    	u.setRole(cadRole);
            	    	u.setPhone("");
            	    	if(pwd == null)
            	    	{
	            	    	String password = "";
	            	    	for(int a=0; a<8; a++)
							{
								Random ran = new Random();
								String l = letters[ran.nextInt(letters.length)];
								password = password + l;
							}
	            	    	u.setPassword(password);
            	    	}
            	    	else
            	    	{
            	    		u.setPassword(pwd);
            	    	}
            	    	up.setUser(u);
            	    	
            	    	profiles.add(up);
            		}
            		
            		wk.close();
            		
            		success = 0;
            		failed = 0;
            		persist(profiles);
            		
            		messages.info(new RecFBundleKey("admin_loadUsersMessage"), rows, success, failed).defaults("Total record(s) processed {0}. Total success {1}. Total failed {2}.")
                    .params(rows, success, failed);
            		
            		item = null;
            		success = 0;
            		failed = 0;
            		profiles.clear();
            		profiles = null;
            	}
    		}
    		catch(Exception ex)
    		{
    			ex.printStackTrace();
    		}
    	}
    }
    
    private void persist(List<?> entities)
    {
        for (Object e : entities)
        {
        	if(e instanceof UserProfile)
        	{
        		UserProfile up = (UserProfile)e;
        		
        		User u = up.getUser();
        		persist(u);
        		up.setUser(u);
        		persist(up);
        	}
        	else
        	{
        		persist(e);
        	}
        }
    }

    private void persist(Object entity)
    {
        try
        {
            em.persist(entity);
            em.flush();
            success++;
        }
        catch(ConstraintViolationException e)
        {
        	failed++;
            throw new PersistenceException("Cannot persist invalid entity: " + entity);
        }
        catch(PersistenceException e)
        {
        	failed++;
            throw new PersistenceException("Error persisting entity: " + entity, e);
        }
    }
    
    public void paint(OutputStream stream, Object object) throws IOException
    {
    	if(getViewingProfile().getPassport_data() != null)
    	{
    		stream.write(getViewingProfile().getPassport_data());
    		stream.close();
    	}
    }
    
    public void search()
    {
    	String qstr = "SELECT e FROM UserProfile e WHERE str(firstname) like :f_nm and str(middlename) like :m_nm and str(lastname) like :l_nm";
    	
    	State st = null, st2 = null;
    	LocalGovtArea lg = null, lg2 = null;
    	if(sog_id > 0)
    	{
    		st = em.find(State.class, sog_id);
    		if(st != null)
    		{
    			qstr += " and stateoforigin = :st_org";
    		}
    	}
    	if(lgaog_id > 0)
    	{
    		lg = em.find(LocalGovtArea.class, lgaog_id);
    		if(lg != null)
    		{
    			qstr += " and localgovtarea = :lg_org";
    		}
    	}
    	if(addrst_id > 0)
    	{
    		st2 = em.find(State.class, addrst_id);
    		if(st2 != null)
    		{
    			qstr += " and state = :st_a";
    		}
    	}
    	if(addrlg_id > 0)
    	{
    		lg2 = em.find(LocalGovtArea.class, addrlg_id);
    		if(lg2 != null)
    		{
    			qstr += " and lgarea = :lg_a";
    		}
    	}
    	if(yearsofexp > 0)
    		qstr += " and yearsofexp >= :yr_nysc";
    	if(status_id >= 0)
    		qstr += " and status = :stat";
    	
    	Query q = em.createQuery(qstr);
    	q.setParameter("f_nm", "%"+getFirstname()+"%");
    	q.setParameter("m_nm", "%"+getMiddlename()+"%");
    	q.setParameter("l_nm", "%"+getLastname()+"%");
    	
    	if(st != null)
    	{
    		q.setParameter("st_org", st);
    	}
    	
    	if(lg != null)
    	{
    		q.setParameter("lg_org", lg);
    	}
    	
    	if(st2 != null)
    	{
    		q.setParameter("st_a", st2);
    	}
    	
    	if(lg2 != null)
    	{
    		q.setParameter("lg_a", lg2);
    	}
    	
    	if(yearsofexp > 0)
    	{
    		q.setParameter("yr_nysc", yearsofexp);
    	}
    	
    	if(status_id >= 0)
    	{
    		q.setParameter("stat", status_id);
    	}
    	List result = q.getResultList();
    	setMatchingProfiles((ArrayList<UserProfile>) result);
    	
    	for(UserProfile e : getMatchingProfiles())
    	{
    		q = em.createQuery("SELECT e from UserEducationQualification e where user = :usr");
    		q.setParameter("usr", e.getUser());
    		ArrayList<UserEducationQualification> uedus = (ArrayList<UserEducationQualification>)q.getResultList();
    		for(UserEducationQualification ue : uedus)
    		{
    			e.getEduQualificationsList().add(ue.getEduQualification());
    		}
    		
    		q = em.createQuery("SELECT e from UserEmployment e where user = :usr");
    		q.setParameter("usr", e.getUser());
    		ArrayList<UserEmployment> uemps = (ArrayList<UserEmployment>)q.getResultList();
    		for(UserEmployment ue : uemps)
    		{
    			e.getEmploymentList().add(ue.getEmployment());
    		}
    	}
    	
    	messages.info(new RecFBundleKey("admin_search"), getMatchingProfiles().size()).defaults("{0} records found!")
        .params(getMatchingProfiles().size());
    }

	public UploadedFile getItem() {
		return item;
	}

	public void setItem(UploadedFile item) {
		this.item = item;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public long getSog_id() {
		return sog_id;
	}

	public void setSog_id(long sog_id) {
		this.sog_id = sog_id;
	}

	public long getLgaog_id() {
		return lgaog_id;
	}

	public void setLgaog_id(long lgaog_id) {
		this.lgaog_id = lgaog_id;
	}

	public long getYearsofexp() {
		return yearsofexp;
	}

	public void setYearsofexp(long yearsofexp) {
		this.yearsofexp = yearsofexp;
	}

	public long getAddrst_id() {
		return addrst_id;
	}

	public void setAddrst_id(long addrst_id) {
		this.addrst_id = addrst_id;
	}

	public long getAddrlg_id() {
		return addrlg_id;
	}

	public void setAddrlg_id(long addrlg_id) {
		this.addrlg_id = addrlg_id;
	}

	public long getStatus_id() {
		return status_id;
	}

	public void setStatus_id(long status_id) {
		this.status_id = status_id;
	}

	public ArrayList<UserProfile> getMatchingProfiles() {
		if(matchingProfiles == null)
			matchingProfiles = new ArrayList<UserProfile>();
		return matchingProfiles;
	}

	public void setMatchingProfiles(ArrayList<UserProfile> matchingProfiles) {
		this.matchingProfiles = matchingProfiles;
	}

	public UserProfile getViewingProfile() {
		if(viewingProfile == null)
			viewingProfile = new UserProfile();
		return viewingProfile;
	}

	public void setViewingProfile(UserProfile viewingProfile) {
		this.viewingProfile = viewingProfile;
	}
    
	public long getTimeStamp() {
        return System.currentTimeMillis();
    }
	
	public String getMimetype()
	{
		// String mimetype = "application/vnd.ms-excel";
		return mimetype;
	}
	
	public void setMimetype(String mimetype)
	{
		this.mimetype = mimetype;
	}
	
	public String getFilename()
	{
		return filename;
	}
	
	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public long getPending() {
		String qstr = "SELECT count(e) FROM UserProfile e WHERE status = :stat";
		try
		{
			Query q = em.createQuery(qstr);
			q.setParameter("stat", 0L);
			
			pending = (Long)q.getSingleResult();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return pending;
	}

	public void setPending(long pending) {
		this.pending = pending;
	}

	public long getTotal() {
		String qstr = "SELECT count(e) FROM UserProfile e";
		try
		{
			Query q = em.createQuery(qstr);
			
			total = (Long)q.getSingleResult();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	
}
