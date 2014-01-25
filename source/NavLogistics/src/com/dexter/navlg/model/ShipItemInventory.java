package com.dexter.navlg.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>ShipItemInventory</strong> is the model/entity class that represents inventory of items within the ship.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class ShipItemInventory implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Ship ship;
	private Item item;
	private Long stock_lvl;
	private ShipItems settings;
	
	public ShipItemInventory()
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
		if(ship == null)
			ship = new Ship();
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	@ManyToOne
	public Item getItem() {
		if(item == null)
			item = new Item();
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Long getStock_lvl() {
		return stock_lvl;
	}

	public void setStock_lvl(Long stock_lvl) {
		this.stock_lvl = stock_lvl;
	}

	@ManyToOne
	public ShipItems getSettings() {
		if(settings == null)
			settings = new ShipItems();
		return settings;
	}

	public void setSettings(ShipItems settings) {
		this.settings = settings;
	}
	
}
