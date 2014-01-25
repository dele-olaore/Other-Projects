package com.dexter.navlg.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>ShipItemsUseItem</strong> is the model/entity class that represents an item used in a ship items usage.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class ShipItemsUseItem implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private ShipItemsUse shipItemsUse;
	private ShipSparepart shipSparepart;
	
	public ShipItemsUseItem()
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
	public ShipItemsUse getShipItemsUse() {
		return shipItemsUse;
	}

	public void setShipItemsUse(ShipItemsUse shipItemsUse) {
		this.shipItemsUse = shipItemsUse;
	}

	@ManyToOne
	public ShipSparepart getShipSparepart() {
		return shipSparepart;
	}

	public void setShipSparepart(ShipSparepart shipSparepart) {
		this.shipSparepart = shipSparepart;
	}
	
}
