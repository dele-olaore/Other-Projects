package com.dexter.navlg.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>ShipRequest</strong> is the model/entity class that represents a ship request for items from its warehouse.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class ShipRequest implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Ship ship;
	private Date date;
	private String requesting_unit;
	private String equipment;
	private String selno;
	private String issno;
	private String serialno;
	private User requestedBy;
	private User approvedBy;
	private boolean approvalFlag; // - approved or disapproved
	private Date approvalDate;
	private String approvalRemark;
	private User issuedBy;
	private Date issuedDate;
	private User receivedBy;
	private Date receivedDate;
	
	public ShipRequest()
	{}

	@Id
    @NotNull
    @GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRequesting_unit() {
		return requesting_unit;
	}

	public void setRequesting_unit(String requesting_unit) {
		this.requesting_unit = requesting_unit;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getSelno() {
		return selno;
	}

	public void setSelno(String selno) {
		this.selno = selno;
	}

	public String getIssno() {
		return issno;
	}

	public void setIssno(String issno) {
		this.issno = issno;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	@ManyToOne
	public User getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(User requestedBy) {
		this.requestedBy = requestedBy;
	}

	@ManyToOne
	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public boolean isApprovalFlag() {
		return approvalFlag;
	}
	
	public boolean getApprovalFlag() {
		return approvalFlag;
	}

	public void setApprovalFlag(boolean approvalFlag) {
		this.approvalFlag = approvalFlag;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprovalRemark() {
		return approvalRemark;
	}

	public void setApprovalRemark(String approvalRemark) {
		this.approvalRemark = approvalRemark;
	}

	@ManyToOne
	public User getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(User issuedBy) {
		this.issuedBy = issuedBy;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	@ManyToOne
	public User getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(User receivedBy) {
		this.receivedBy = receivedBy;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
}
