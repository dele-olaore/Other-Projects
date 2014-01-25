package com.dexter.nipost.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * This class represents the property as a whole.
 * @author oladele
 * */
@Entity
public class Property implements Serializable
{
	private static final long serialVersionUID = 8628997603398179991L;
	
	@Id
    @NotNull
    @GeneratedValue
	private Long id;
	
	private String state;
	private String lg;
	private String addr;
	private String type;
	private String postalcode;
	private String lon;
	private String lat;
	private String rural;
	private String place_exists;
	private String remarks;
	
	//property specifics
	private String building_type;
	private byte[] interior_photo;
	private byte[] exterior_photo;
	private String building_ownership;
	private String land_ownership;
	private String building_year;
	private String land_m2;
	private String outline_m2;
	private String mailspace_m2;
	private String ccspace_m2;
	private String waiting_m2;
	private String backoffice_m2;
	private int num_counters_p;
	private int num_counters_f;
	
	//building condition
	private String ccspace_cond;
	private String ccspace_cond2;
	private byte[] ccspace_photo;
	private String building_cond;
	private String building_cond2;
	private byte[] building_photo;
	private BigDecimal renovation_cost;
	
	//power supply
	private boolean phcn;
	private boolean solar;
	private BigDecimal solar_kwhs;
	private boolean generators;
	private BigDecimal generator_kwhs;
	private BigDecimal needed_kwhs;
	private BigDecimal provided_kwhs;
	
	//ICT
	private int functioning_comp;
	private int newest_ramsize;
	private String newest_processor;
	private String datacon_type;
	private BigDecimal datacon_speed;
	private int land_phones;
	private int mobile_phones;
	private int fax_count;
	private int photocopy_count;
	private int printer_count;
	
	//Security
	private int secstaff_count;
	private String sec_provider;
	private String sec_provider_other;
	private boolean strongroom;
	private int safes_count;
	private String safes_brand;
	
	//Customer service
	private BigDecimal week_workhours;
	private String open_time;
	private String close_time;
	private int cus_perday;
	private int cus_perweek;
	private int trans_perweek;
	
	//product service offered
	private boolean post_cash;
	private boolean stamps;
	private boolean letter_box;
	private boolean po_box;
	private boolean ems;
	private boolean parcels;
	private boolean philately;
	private boolean public_internet;
	private boolean public_phone;
	private boolean customs;
	private boolean netpost;
	private boolean cards_office_supplies;
	private boolean atm;
	private String others_offered;
	
	//Staffs
	private int staffs_count;
	private int clerks_count;
	private int staff_with_diploma_count;
	private String institute;
	
	@ManyToOne
	private User user;
	
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date crt_dt;
	
	@Transient
	private String user_id;
	
	public Property()
	{}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLg() {
		return lg;
	}

	public void setLg(String lg) {
		this.lg = lg;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getRural() {
		return rural;
	}

	public void setRural(String rural) {
		this.rural = rural;
	}

	public String getPlace_exists() {
		return place_exists;
	}

	public void setPlace_exists(String place_exists) {
		this.place_exists = place_exists;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBuilding_type() {
		return building_type;
	}

	public void setBuilding_type(String building_type) {
		this.building_type = building_type;
	}

	public byte[] getInterior_photo() {
		return interior_photo;
	}

	public void setInterior_photo(byte[] interior_photo) {
		this.interior_photo = interior_photo;
	}

	public byte[] getExterior_photo() {
		return exterior_photo;
	}

	public void setExterior_photo(byte[] exterior_photo) {
		this.exterior_photo = exterior_photo;
	}

	public String getBuilding_ownership() {
		return building_ownership;
	}

	public void setBuilding_ownership(String building_ownership) {
		this.building_ownership = building_ownership;
	}

	public String getLand_ownership() {
		return land_ownership;
	}

	public void setLand_ownership(String land_ownership) {
		this.land_ownership = land_ownership;
	}

	public String getBuilding_year() {
		return building_year;
	}

	public void setBuilding_year(String building_year) {
		this.building_year = building_year;
	}

	public String getLand_m2() {
		return land_m2;
	}

	public void setLand_m2(String land_m2) {
		this.land_m2 = land_m2;
	}

	public String getOutline_m2() {
		return outline_m2;
	}

	public void setOutline_m2(String outline_m2) {
		this.outline_m2 = outline_m2;
	}

	public String getMailspace_m2() {
		return mailspace_m2;
	}

	public void setMailspace_m2(String mailspace_m2) {
		this.mailspace_m2 = mailspace_m2;
	}

	public String getCcspace_m2() {
		return ccspace_m2;
	}

	public void setCcspace_m2(String ccspace_m2) {
		this.ccspace_m2 = ccspace_m2;
	}

	public String getWaiting_m2() {
		return waiting_m2;
	}

	public void setWaiting_m2(String waiting_m2) {
		this.waiting_m2 = waiting_m2;
	}

	public String getBackoffice_m2() {
		return backoffice_m2;
	}

	public void setBackoffice_m2(String backoffice_m2) {
		this.backoffice_m2 = backoffice_m2;
	}

	public int getNum_counters_p() {
		return num_counters_p;
	}

	public void setNum_counters_p(int num_counters) {
		this.num_counters_p = num_counters;
	}
	
	public int getNum_counters_f() {
		return num_counters_f;
	}

	public void setNum_counters_f(int num_counters) {
		this.num_counters_f = num_counters;
	}

	public String getCcspace_cond() {
		return ccspace_cond;
	}

	public void setCcspace_cond(String ccspace_cond) {
		this.ccspace_cond = ccspace_cond;
	}
	
	public String getCcspace_cond2() {
		return ccspace_cond2;
	}

	public void setCcspace_cond2(String ccspace_cond) {
		this.ccspace_cond2 = ccspace_cond;
	}

	public byte[] getCcspace_photo() {
		return ccspace_photo;
	}

	public void setCcspace_photo(byte[] ccspace_photo) {
		this.ccspace_photo = ccspace_photo;
	}

	public String getBuilding_cond() {
		return building_cond;
	}

	public void setBuilding_cond(String building_cond) {
		this.building_cond = building_cond;
	}
	
	public String getBuilding_cond2() {
		return building_cond2;
	}

	public void setBuilding_cond2(String building_cond) {
		this.building_cond2 = building_cond;
	}

	public byte[] getBuilding_photo() {
		return building_photo;
	}

	public void setBuilding_photo(byte[] building_photo) {
		this.building_photo = building_photo;
	}

	public BigDecimal getRenovation_cost() {
		return renovation_cost;
	}

	public void setRenovation_cost(BigDecimal renovation_cost) {
		this.renovation_cost = renovation_cost;
	}

	public boolean isPhcn() {
		return phcn;
	}

	public void setPhcn(boolean phcn) {
		this.phcn = phcn;
	}

	public boolean isSolar() {
		return solar;
	}

	public void setSolar(boolean solar) {
		this.solar = solar;
	}

	public BigDecimal getSolar_kwhs() {
		return solar_kwhs;
	}

	public void setSolar_kwhs(BigDecimal solar_kwhs) {
		this.solar_kwhs = solar_kwhs;
	}

	public boolean isGenerators() {
		return generators;
	}

	public void setGenerators(boolean generators) {
		this.generators = generators;
	}

	public BigDecimal getGenerator_kwhs() {
		return generator_kwhs;
	}

	public void setGenerator_kwhs(BigDecimal generator_kwhs) {
		this.generator_kwhs = generator_kwhs;
	}

	public BigDecimal getNeeded_kwhs() {
		return needed_kwhs;
	}

	public void setNeeded_kwhs(BigDecimal needed_kwhs) {
		this.needed_kwhs = needed_kwhs;
	}

	public BigDecimal getProvided_kwhs() {
		return provided_kwhs;
	}

	public void setProvided_kwhs(BigDecimal provided_kwhs) {
		this.provided_kwhs = provided_kwhs;
	}

	public int getFunctioning_comp() {
		return functioning_comp;
	}

	public void setFunctioning_comp(int functioning_comp) {
		this.functioning_comp = functioning_comp;
	}

	public int getNewest_ramsize() {
		return newest_ramsize;
	}

	public void setNewest_ramsize(int newest_ramsize) {
		this.newest_ramsize = newest_ramsize;
	}

	public String getNewest_processor() {
		return newest_processor;
	}

	public void setNewest_processor(String newest_processor) {
		this.newest_processor = newest_processor;
	}

	public String getDatacon_type() {
		return datacon_type;
	}

	public void setDatacon_type(String datacon_type) {
		this.datacon_type = datacon_type;
	}

	public BigDecimal getDatacon_speed() {
		return datacon_speed;
	}

	public void setDatacon_speed(BigDecimal datacon_speed) {
		this.datacon_speed = datacon_speed;
	}

	public int getLand_phones() {
		return land_phones;
	}

	public void setLand_phones(int land_phones) {
		this.land_phones = land_phones;
	}

	public int getMobile_phones() {
		return mobile_phones;
	}

	public void setMobile_phones(int mobile_phones) {
		this.mobile_phones = mobile_phones;
	}

	public int getFax_count() {
		return fax_count;
	}

	public void setFax_count(int fax_count) {
		this.fax_count = fax_count;
	}

	public int getPhotocopy_count() {
		return photocopy_count;
	}

	public void setPhotocopy_count(int photocopy_count) {
		this.photocopy_count = photocopy_count;
	}

	public int getPrinter_count() {
		return printer_count;
	}

	public void setPrinter_count(int printer_count) {
		this.printer_count = printer_count;
	}

	public int getSecstaff_count() {
		return secstaff_count;
	}

	public void setSecstaff_count(int secstaff_count) {
		this.secstaff_count = secstaff_count;
	}

	public String getSec_provider() {
		return sec_provider;
	}

	public void setSec_provider(String sec_provider) {
		this.sec_provider = sec_provider;
	}

	public String getSec_provider_other() {
		return sec_provider_other;
	}

	public void setSec_provider_other(String sec_provider_other) {
		this.sec_provider_other = sec_provider_other;
	}

	public boolean isStrongroom() {
		return strongroom;
	}

	public void setStrongroom(boolean strongroom) {
		this.strongroom = strongroom;
	}

	public int getSafes_count() {
		return safes_count;
	}

	public void setSafes_count(int safes_count) {
		this.safes_count = safes_count;
	}

	public String getSafes_brand() {
		return safes_brand;
	}

	public void setSafes_brand(String safes_brand) {
		this.safes_brand = safes_brand;
	}

	public BigDecimal getWeek_workhours() {
		return week_workhours;
	}

	public void setWeek_workhours(BigDecimal week_workhours) {
		this.week_workhours = week_workhours;
	}

	public String getOpen_time() {
		return open_time;
	}

	public void setOpen_time(String open_time) {
		this.open_time = open_time;
	}

	public String getClose_time() {
		return close_time;
	}

	public void setClose_time(String close_time) {
		this.close_time = close_time;
	}

	public int getCus_perday() {
		return cus_perday;
	}

	public void setCus_perday(int cus_perday) {
		this.cus_perday = cus_perday;
	}

	public int getCus_perweek() {
		return cus_perweek;
	}

	public void setCus_perweek(int cus_perweek) {
		this.cus_perweek = cus_perweek;
	}

	public int getTrans_perweek() {
		return trans_perweek;
	}

	public void setTrans_perweek(int trans_perweek) {
		this.trans_perweek = trans_perweek;
	}

	public boolean isPost_cash() {
		return post_cash;
	}

	public void setPost_cash(boolean post_cash) {
		this.post_cash = post_cash;
	}

	public boolean isStamps() {
		return stamps;
	}

	public void setStamps(boolean stamps) {
		this.stamps = stamps;
	}

	public boolean isLetter_box() {
		return letter_box;
	}

	public void setLetter_box(boolean letter_box) {
		this.letter_box = letter_box;
	}

	public boolean isPo_box() {
		return po_box;
	}

	public void setPo_box(boolean po_box) {
		this.po_box = po_box;
	}

	public boolean isEms() {
		return ems;
	}

	public void setEms(boolean ems) {
		this.ems = ems;
	}

	public boolean isParcels() {
		return parcels;
	}

	public void setParcels(boolean parcels) {
		this.parcels = parcels;
	}

	public boolean isPhilately() {
		return philately;
	}

	public void setPhilately(boolean philately) {
		this.philately = philately;
	}

	public boolean isPublic_internet() {
		return public_internet;
	}

	public void setPublic_internet(boolean public_internet) {
		this.public_internet = public_internet;
	}

	public boolean isPublic_phone() {
		return public_phone;
	}

	public void setPublic_phone(boolean public_phone) {
		this.public_phone = public_phone;
	}

	public boolean isCustoms() {
		return customs;
	}

	public void setCustoms(boolean customs) {
		this.customs = customs;
	}

	public boolean isNetpost() {
		return netpost;
	}

	public void setNetpost(boolean netpost) {
		this.netpost = netpost;
	}

	public boolean isCards_office_supplies() {
		return cards_office_supplies;
	}

	public void setCards_office_supplies(boolean cards_office_supplies) {
		this.cards_office_supplies = cards_office_supplies;
	}

	public boolean isAtm() {
		return atm;
	}

	public void setAtm(boolean atm) {
		this.atm = atm;
	}

	public String getOthers_offered() {
		return others_offered;
	}

	public void setOthers_offered(String others_offered) {
		this.others_offered = others_offered;
	}

	public int getStaffs_count() {
		return staffs_count;
	}

	public void setStaffs_count(int staffs_count) {
		this.staffs_count = staffs_count;
	}

	public int getClerks_count() {
		return clerks_count;
	}

	public void setClerks_count(int clerks_count) {
		this.clerks_count = clerks_count;
	}

	public int getStaff_with_diploma_count() {
		return staff_with_diploma_count;
	}

	public void setStaff_with_diploma_count(int staff_with_diploma_count) {
		this.staff_with_diploma_count = staff_with_diploma_count;
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCrt_dt() {
		return crt_dt;
	}

	public void setCrt_dt(Date crt_dt) {
		this.crt_dt = crt_dt;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
}
