package com.dexter.navlg.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>ShipSparepart</strong> is the model/entity class that represents an actual item in the ship.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class ShipSparepart implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Item item;
	private String serial_number;
	private String customized_number;
	private boolean active;
	private Ship ship;
	private WarehouseSpareparts warehouse_sp;
	private ShipRequest shipRequest;
	
	private boolean selected;
	
	public ShipSparepart()
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
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getCustomized_number() {
		return customized_number;
	}

	public void setCustomized_number(String customized_number) {
		this.customized_number = customized_number;
	}

	public boolean isActive() {
		return active;
	}
	
	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@ManyToOne
	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	@ManyToOne
	public WarehouseSpareparts getWarehouse_sp() {
		return warehouse_sp;
	}

	public void setWarehouse_sp(WarehouseSpareparts warehouse_sp) {
		this.warehouse_sp = warehouse_sp;
	}

	@ManyToOne
	public ShipRequest getShipRequest() {
		return shipRequest;
	}

	public void setShipRequest(ShipRequest shipRequest) {
		this.shipRequest = shipRequest;
	}

	@Transient
	public boolean isSelected() {
		return selected;
	}

	@Transient
	public boolean getSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
