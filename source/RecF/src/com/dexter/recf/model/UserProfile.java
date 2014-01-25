package com.dexter.recf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>UserProfile</strong> is the model/entity class that represents a user profile information.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class UserProfile implements Serializable
{
	private static final long serialVersionUID = -2121423783918127806L;
	
	private long id;
	private String firstname;
	private String middlename;
	private String lastname;
	private String testid;
	private Date dateofbirth;
	private State stateoforigin;
	private LocalGovtArea localgovtarea;
	private String nyscyear;
	private String nyscnumber;
	private long yearsofexp;
	private String gender;
	private String email;
	private String mobileno1;
	private String mobileno2;
	private long status;
	
	private String address;
	private String city;
	private State state;
	private LocalGovtArea lgarea;
	
	private String passport_name;
    private String passport_type;
    private long passport_length;
    private byte[] passport_data;
    
    private String cv_name;
    private String cv_type;
    private long cv_length;
    private byte[] cv_data;
    
	private User user;
	
	@Transient
	private List<UserEducationQualification> educationQualifications;
	
	@Transient
	private ArrayList<EducationQualification> eduQualificationsList;
	
	@Transient
	private List<UserEmployment> employments;
	
	@Transient
	private ArrayList<Employment> employmentList;
	
	public UserProfile()
	{}

	@Id
    @NotNull
    @GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	@NotNull
	public String getTestid() {
		return testid;
	}

	public void setTestid(String testid) {
		this.testid = testid;
	}

	@Temporal(TemporalType.DATE)
	public Date getDateofbirth() {
		return dateofbirth;
	}

	public void setDateofbirth(Date dateofbirth) {
		this.dateofbirth = dateofbirth;
	}

	@NotNull
	@ManyToOne
	public State getStateoforigin() {
		if(stateoforigin == null)
			stateoforigin = new State();
		return stateoforigin;
	}

	public void setStateoforigin(State stateoforigin) {
		this.stateoforigin = stateoforigin;
	}

	@NotNull
	@ManyToOne
	public LocalGovtArea getLocalgovtarea() {
		if(localgovtarea == null)
			localgovtarea = new LocalGovtArea();
		return localgovtarea;
	}

	public void setLocalgovtarea(LocalGovtArea localgovtarea) {
		this.localgovtarea = localgovtarea;
	}

	public String getNyscyear() {
		return nyscyear;
	}

	public void setNyscyear(String nyscyear) {
		this.nyscyear = nyscyear;
	}

	public String getNyscnumber() {
		return nyscnumber;
	}

	public void setNyscnumber(String nyscnumber) {
		this.nyscnumber = nyscnumber;
	}

	@Transient
	public List<UserEducationQualification> getEducationQualifications() {
		if(educationQualifications == null)
			educationQualifications = new ArrayList<UserEducationQualification>();
		return educationQualifications;
	}

	public void setEducationQualifications(
			List<UserEducationQualification> educationQualifications) {
		this.educationQualifications = educationQualifications;
	}

	@Transient
	public List<UserEmployment> getEmployments() {
		if(employments == null)
			employments = new ArrayList<UserEmployment>();
		return employments;
	}

	public void setEmployments(List<UserEmployment> employments) {
		this.employments = employments;
	}

	public long getYearsofexp() {
		return yearsofexp;
	}

	public void setYearsofexp(long yearsofexp) {
		this.yearsofexp = yearsofexp;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@ManyToOne
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@ManyToOne
	public LocalGovtArea getLgarea() {
		return lgarea;
	}

	public void setLgarea(LocalGovtArea lgarea) {
		this.lgarea = lgarea;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobileno1() {
		return mobileno1;
	}

	public void setMobileno1(String mobileno1) {
		this.mobileno1 = mobileno1;
	}

	public String getMobileno2() {
		return mobileno2;
	}

	public void setMobileno2(String mobileno2) {
		this.mobileno2 = mobileno2;
	}

	@NotNull
	@OneToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	//@Email
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassport_type() {
		return passport_type;
	}

	public void setPassport_type(String type) {
		this.passport_type = type;
	}

	@Column(length=60000)
	public byte[] getPassport_data() {
        return passport_data;
    }

    public void setPassport_data(byte[] data) {
        this.passport_data = data;
    }

    public String getPassport_name() {
        return passport_name;
    }

    public void setPassport_name(String name) {
    	this.passport_name = name;
    }
    
    public String getCv_type() {
		return cv_type;
	}

	public void setCv_type(String type) {
		this.cv_type = type;
	}

	@Column(length=60000)
	public byte[] getCv_data() {
        return cv_data;
    }

    public void setCv_data(byte[] data) {
        this.cv_data = data;
    }

    public String getCv_name() {
        return cv_name;
    }

    public long getPassport_length() {
		return passport_length;
	}

	public void setPassport_length(long passport_length) {
		this.passport_length = passport_length;
	}

	public long getCv_length() {
		return cv_length;
	}

	public void setCv_length(long cv_length) {
		this.cv_length = cv_length;
	}

	public void setCv_name(String name) {
    	this.cv_name = name;
    }

	@Transient
	public ArrayList<EducationQualification> getEduQualificationsList() {
		if(eduQualificationsList == null)
			eduQualificationsList = new ArrayList<EducationQualification>();
		return eduQualificationsList;
	}

	public void setEduQualificationsList(
			ArrayList<EducationQualification> eduQualificationsList) {
		this.eduQualificationsList = eduQualificationsList;
	}

	@Transient
	public ArrayList<Employment> getEmploymentList() {
		if(employmentList == null)
			employmentList = new ArrayList<Employment>();
		return employmentList;
	}

	public void setEmploymentList(ArrayList<Employment> employmentList) {
		this.employmentList = employmentList;
	}
	
}
