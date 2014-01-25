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
 * <strong>ShipRequestItems</strong> is the model/entity class that represents a ship's request items.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class ShipRequestItems implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private ShipRequest request;
	private Item item;
	private Long requestedAmount;
	private Long issuedAmount;
	private String remark;
	
	public ShipRequestItems()
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
	public ShipRequest getRequest() {
		return request;
	}

	public void setRequest(ShipRequest request) {
		this.request = request;
	}

	@ManyToOne
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Long getRequestedAmount() {
		return requestedAmount;
	}

	public void setRequestedAmount(Long requestedAmount) {
		this.requestedAmount = requestedAmount;
	}

	public Long getIssuedAmount() {
		return issuedAmount;
	}

	public void setIssuedAmount(Long issuedAmount) {
		this.issuedAmount = issuedAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
