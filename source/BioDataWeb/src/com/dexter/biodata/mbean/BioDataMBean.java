package com.dexter.biodata.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Query;

import com.dexter.biodata.model.BioData;
import com.dexter.biodata.model.Sector;
import com.dexter.biodata.model.State;
import com.dexter.biodata.model.User;
import com.dexter.biodata.util.DBUtil;

@ManagedBean(name = "biodataBean")
@SessionScoped
public class BioDataMBean implements Serializable
{
	private static final long serialVersionUID = 1L;

	final Logger logger = Logger.getLogger("BioDataWeb-BioDataMBean");
	
	private FacesMessage msg = null;
	
	private User curUser;
	private String username;
	private String password;
	
	private String fname;
	private String lname;
	private String nis;
	private Long state_id;
	private Long sector_id;
	private Date start_dt;
	private Date end_dt;
	
	private BioData biodata;
	private Vector<BioData> biodataList;
	
	private Vector<State> states;
	private Vector<Sector> sectors;
	
	public BioDataMBean()
	{}
	
	public void logout()
	{
		setCurUser(null);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success: ", "Log out successful.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public String getRandomNumber()
	{
		return ""+new Random().nextInt(10000);
	}
	
	@SuppressWarnings("unchecked")
	public void login()
	{
		if(getUsername() != null && getPassword() != null)
		{
			DBUtil db = new DBUtil();
			
			Hashtable<String, Object> params = new Hashtable<String, Object>();
			params.put("email", getUsername());
			
			Object uObj = db.search("User", params);
			if(uObj != null)
			{
				ArrayList<User> uObjList = (ArrayList<User>)uObj;
				if(uObjList != null && uObjList.size() > 0)
				{
					User u = uObjList.get(0);
					if(u.getPassword().equals(getPassword()))
					{
						setCurUser(u);
						msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Authentication successful!");
					}
					else
						msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed: ", "Authentication error!");
				}
				else
					msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed: ", "User does not exist!");
			}
			else
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed: ", "User does not exist!");
			db.destroy();
		}
		else
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed: ", "Username and password are required!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	@SuppressWarnings("unchecked")
	public void search()
	{
		biodataList = null;
		
		State st = null;
		Sector sec = null;
		
		DBUtil db = new DBUtil();
		
		if(getState_id() != null && getState_id() > 0)
		{
			st = (State)db.find(State.class, getState_id());
		}
		if(getSector_id() != null && getSector_id() > 0)
		{
			sec = (Sector)db.find(Sector.class, getSector_id());
		}
		
		StringBuilder str = new StringBuilder("Select e from BioData e where e.id > 0");
		
		if(fname != null && fname.trim().length() > 0)
		{
			str.append(" and e.firstname like :fname");
		}
		if(lname != null && lname.trim().length() > 0)
		{
			str.append(" and e.lastname like :lname");
		}
		if(nis != null && nis.trim().length() > 0)
		{
			str.append(" and e.nis like :nis");
		}
		if(st != null)
		{
			str.append(" and e.state = :st");
		}
		if(sec != null)
		{
			str.append(" and e.sector = :sec");
		}
		if(start_dt != null && end_dt != null)
		{
			if(end_dt.after(start_dt))
			{
				str.append(" and e.crt_dt between :start_dt and :end_dt");
			}
		}
		
		Query q = db.createQuery(str.toString());
		if(fname != null && fname.trim().length() > 0)
			q.setParameter("fname", "%"+fname+"%");
		if(lname != null && lname.trim().length() > 0)
			q.setParameter("lname", "%"+lname+"%");
		if(nis != null && nis.trim().length() > 0)
			q.setParameter("nis", "%"+nis+"%");
		if(st != null)
			q.setParameter("st", st);
		if(sec != null)
			q.setParameter("sec", sec);
		if(start_dt != null && end_dt != null)
		{
			if(end_dt.after(start_dt))
			{
				q.setParameter("start_dt", start_dt);
				q.setParameter("end_dt", end_dt);
			}
		}
		
		Object result = db.search(q);
		if(result != null)
		{
			biodataList = (Vector<BioData>)result;
		}
		
		if(biodataList != null)
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success: ", biodataList.size() + " record(s) found!");
		else
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed: ", "No record found!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getNis() {
		return nis;
	}

	public void setNis(String nis) {
		this.nis = nis;
	}

	public Long getState_id() {
		return state_id;
	}

	public void setState_id(Long state_id) {
		this.state_id = state_id;
	}

	public Long getSector_id() {
		return sector_id;
	}

	public void setSector_id(Long sector_id) {
		this.sector_id = sector_id;
	}

	public Date getStart_dt() {
		return start_dt;
	}

	public void setStart_dt(Date start_dt) {
		this.start_dt = start_dt;
	}

	public Date getEnd_dt() {
		return end_dt;
	}

	public void setEnd_dt(Date end_dt) {
		this.end_dt = end_dt;
	}

	public BioData getBiodata() {
		if(biodata == null)
			biodata = new BioData();
		return biodata;
	}

	public void setBiodata(BioData biodata) {
		this.biodata = biodata;
	}

	public Vector<BioData> getBiodataList() {
		if(biodataList == null)
			biodataList = new Vector<BioData>();
		return biodataList;
	}

	public void setBiodataList(Vector<BioData> biodataList) {
		this.biodataList = biodataList;
	}

	@SuppressWarnings("unchecked")
	public Vector<State> getStates() {
		if(states == null)
		{
			DBUtil db = new DBUtil();
			Object objl = db.findAll("State");
			if(objl != null)
				states = (Vector<State>)objl;
		}
		return states;
	}

	public void setStates(Vector<State> states) {
		this.states = states;
	}

	@SuppressWarnings("unchecked")
	public Vector<Sector> getSectors() {
		if(sectors == null)
		{
			DBUtil db = new DBUtil();
			Object objl = db.findAll("Sector");
			if(objl != null)
				sectors = (Vector<Sector>)objl;
		}
		return sectors;
	}

	public void setSectors(Vector<Sector> sectors) {
		this.sectors = sectors;
	}

	public User getCurUser() {
		return curUser;
	}

	public void setCurUser(User curUser) {
		this.curUser = curUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
