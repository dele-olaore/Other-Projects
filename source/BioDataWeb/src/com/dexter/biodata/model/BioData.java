package com.dexter.biodata.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class BioData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	@Column(unique=true)
	private String identificationcode;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date crt_dt;
	
	@ManyToOne
	private User createdByUser;
	
	@ManyToOne
	private Sector sector;
	@ManyToOne
	private State state;
	
	private String firstname;
	private String middlename;
	private String lastname;
	private String maidenname;
	private String title;
	private String gender;
	private String maritalStatus;
	
	private String stateOfOrigin;
	private String lgOfOrigin;
	
	private String monthOfBirth;
	private String dayOfBirth;
	private String yearOfBirth;
	private String placeOfBirth;
	
	private String town;
	private String religion;
	private String nationality;
	
	private String addr1;
	private String addr2;
	private String addrHm1;
	private String addrHm2;
	private String mobile1;
	private String mobile2;
	private String mobile3;
	private String email;
	
	private String gradeLvl;
	private String workStation;
	private String nis;
	private String rank;
	private String stationLocation;
	private String profession;
	
	private String nok_lastname;
	private String nok_middlename;
	private String nok_firstname;
	private String nok_relatioship;
	private String nok_addr1;
	private String nok_addr2;
	private String nok_mobile;
	
	private int noOfChildren;
	
	private String child1_name;
	private String child1_dob;
	private String child2_name;
	private String child2_dob;
	private String child3_name;
	private String child3_dob;
	private String child4_name;
	private String child4_dob;
	
	private String doa;
	private String doc;
	private String moe;
	
	private String cert1;
	private String cert1_date;
	private String cert2;
	private String cert2_date;
	private String cert3;
	private String cert3_date;
	
	private String othercert1;
	private String othercert1_date;
	private String othercert2;
	private String othercert2_date;
	private String othercert3;
	private String othercert3_date;
	
	private String rank_on_last_pro;
	private String monthly;
	private String annual;
	
	private String proposedRetDate;
	
	private String authName;
	private String authDate;
	private String authRemarks;
	private String checkedBy;
	
	@Column(length=500000)
	private byte[] photo;
	@Column(length=500000)
	private byte[] fingerprint;
	
	public BioData()
	{}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificationcode() {
		return identificationcode;
	}

	public void setIdentificationcode(String identificationcode) {
		this.identificationcode = identificationcode;
	}

	public Date getCrt_dt() {
		return crt_dt;
	}

	public void setCrt_dt(Date crt_dt) {
		this.crt_dt = crt_dt;
	}

	public User getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(User createdByUser) {
		this.createdByUser = createdByUser;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
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

	public String getMaidenname() {
		return maidenname;
	}

	public void setMaidenname(String maidenname) {
		this.maidenname = maidenname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getStateOfOrigin() {
		return stateOfOrigin;
	}

	public void setStateOfOrigin(String stateOfOrigin) {
		this.stateOfOrigin = stateOfOrigin;
	}

	public String getLgOfOrigin() {
		return lgOfOrigin;
	}

	public void setLgOfOrigin(String lgOfOrigin) {
		this.lgOfOrigin = lgOfOrigin;
	}

	public String getMonthOfBirth() {
		return monthOfBirth;
	}

	public void setMonthOfBirth(String monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}

	public String getDayOfBirth() {
		return dayOfBirth;
	}

	public void setDayOfBirth(String dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

	public String getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(String yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public String getAddrHm1() {
		return addrHm1;
	}

	public void setAddrHm1(String addrHm1) {
		this.addrHm1 = addrHm1;
	}

	public String getAddrHm2() {
		return addrHm2;
	}

	public void setAddrHm2(String addrHm2) {
		this.addrHm2 = addrHm2;
	}

	public String getMobile1() {
		return mobile1;
	}

	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}

	public String getMobile2() {
		return mobile2;
	}

	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	public String getMobile3() {
		return mobile3;
	}

	public void setMobile3(String mobile3) {
		this.mobile3 = mobile3;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGradeLvl() {
		return gradeLvl;
	}

	public void setGradeLvl(String gradeLvl) {
		this.gradeLvl = gradeLvl;
	}

	public String getWorkStation() {
		return workStation;
	}

	public void setWorkStation(String workStation) {
		this.workStation = workStation;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getStationLocation() {
		return stationLocation;
	}

	public void setStationLocation(String stationLocation) {
		this.stationLocation = stationLocation;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getNok_lastname() {
		return nok_lastname;
	}

	public void setNok_lastname(String nok_lastname) {
		this.nok_lastname = nok_lastname;
	}

	public String getNok_middlename() {
		return nok_middlename;
	}

	public void setNok_middlename(String nok_middlename) {
		this.nok_middlename = nok_middlename;
	}

	public String getNok_firstname() {
		return nok_firstname;
	}

	public void setNok_firstname(String nok_firstname) {
		this.nok_firstname = nok_firstname;
	}

	public String getNok_relatioship() {
		return nok_relatioship;
	}

	public void setNok_relatioship(String nok_relatioship) {
		this.nok_relatioship = nok_relatioship;
	}

	public String getNok_addr1() {
		return nok_addr1;
	}

	public void setNok_addr1(String nok_addr1) {
		this.nok_addr1 = nok_addr1;
	}

	public String getNok_addr2() {
		return nok_addr2;
	}

	public void setNok_addr2(String nok_addr2) {
		this.nok_addr2 = nok_addr2;
	}

	public String getNok_mobile() {
		return nok_mobile;
	}

	public void setNok_mobile(String nok_mobile) {
		this.nok_mobile = nok_mobile;
	}

	public int getNoOfChildren() {
		return noOfChildren;
	}

	public void setNoOfChildren(int noOfChildren) {
		this.noOfChildren = noOfChildren;
	}

	public String getChild1_name() {
		return child1_name;
	}

	public void setChild1_name(String child1_name) {
		this.child1_name = child1_name;
	}

	public String getChild1_dob() {
		return child1_dob;
	}

	public void setChild1_dob(String child1_dob) {
		this.child1_dob = child1_dob;
	}

	public String getChild2_name() {
		return child2_name;
	}

	public void setChild2_name(String child2_name) {
		this.child2_name = child2_name;
	}

	public String getChild2_dob() {
		return child2_dob;
	}

	public void setChild2_dob(String child2_dob) {
		this.child2_dob = child2_dob;
	}

	public String getChild3_name() {
		return child3_name;
	}

	public void setChild3_name(String child3_name) {
		this.child3_name = child3_name;
	}

	public String getChild3_dob() {
		return child3_dob;
	}

	public void setChild3_dob(String child3_dob) {
		this.child3_dob = child3_dob;
	}

	public String getChild4_name() {
		return child4_name;
	}

	public void setChild4_name(String child4_name) {
		this.child4_name = child4_name;
	}

	public String getChild4_dob() {
		return child4_dob;
	}

	public void setChild4_dob(String child4_dob) {
		this.child4_dob = child4_dob;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public byte[] getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(byte[] fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String getNis() {
		return nis;
	}

	public void setNis(String nis) {
		this.nis = nis;
	}

	public String getDoa() {
		return doa;
	}

	public void setDoa(String doa) {
		this.doa = doa;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public String getMoe() {
		return moe;
	}

	public void setMoe(String moe) {
		this.moe = moe;
	}

	public String getCert1() {
		return cert1;
	}

	public void setCert1(String cert1) {
		this.cert1 = cert1;
	}

	public String getCert1_date() {
		return cert1_date;
	}

	public void setCert1_date(String cert1_date) {
		this.cert1_date = cert1_date;
	}

	public String getCert2() {
		return cert2;
	}

	public void setCert2(String cert2) {
		this.cert2 = cert2;
	}

	public String getCert2_date() {
		return cert2_date;
	}

	public void setCert2_date(String cert2_date) {
		this.cert2_date = cert2_date;
	}

	public String getCert3() {
		return cert3;
	}

	public void setCert3(String cert3) {
		this.cert3 = cert3;
	}

	public String getCert3_date() {
		return cert3_date;
	}

	public void setCert3_date(String cert3_date) {
		this.cert3_date = cert3_date;
	}

	public String getOthercert1() {
		return othercert1;
	}

	public void setOthercert1(String othercert1) {
		this.othercert1 = othercert1;
	}

	public String getOthercert1_date() {
		return othercert1_date;
	}

	public void setOthercert1_date(String othercert1_date) {
		this.othercert1_date = othercert1_date;
	}

	public String getOthercert2() {
		return othercert2;
	}

	public void setOthercert2(String othercert2) {
		this.othercert2 = othercert2;
	}

	public String getOthercert2_date() {
		return othercert2_date;
	}

	public void setOthercert2_date(String othercert2_date) {
		this.othercert2_date = othercert2_date;
	}

	public String getOthercert3() {
		return othercert3;
	}

	public void setOthercert3(String othercert3) {
		this.othercert3 = othercert3;
	}

	public String getOthercert3_date() {
		return othercert3_date;
	}

	public void setOthercert3_date(String othercert3_date) {
		this.othercert3_date = othercert3_date;
	}

	public String getRank_on_last_pro() {
		return rank_on_last_pro;
	}

	public void setRank_on_last_pro(String rank_on_last_pro) {
		this.rank_on_last_pro = rank_on_last_pro;
	}

	public String getMonthly() {
		return monthly;
	}

	public void setMonthly(String monthly) {
		this.monthly = monthly;
	}

	public String getAnnual() {
		return annual;
	}

	public void setAnnual(String annual) {
		this.annual = annual;
	}

	public String getProposedRetDate() {
		return proposedRetDate;
	}

	public void setProposedRetDate(String proposedRetDate) {
		this.proposedRetDate = proposedRetDate;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getAuthDate() {
		return authDate;
	}

	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}

	public String getAuthRemarks() {
		return authRemarks;
	}

	public void setAuthRemarks(String authRemarks) {
		this.authRemarks = authRemarks;
	}

	public String getCheckedBy() {
		return checkedBy;
	}

	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}
	
}
