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
 * <strong>SparepartStatus</strong> is the model/entity class that represents status of a sparepart in ship.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class SparepartStatus implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private ShipSparepart sparepart;
	private String status;
	private String remark;
	private Date date;
	
	public SparepartStatus()
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
	public ShipSparepart getSparepart() {
		if(sparepart == null)
			sparepart = new ShipSparepart();
		return sparepart;
	}

	public void setSparepart(ShipSparepart sparepart) {
		this.sparepart = sparepart;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
