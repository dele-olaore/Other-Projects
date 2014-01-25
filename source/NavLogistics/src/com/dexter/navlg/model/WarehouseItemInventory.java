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
 * <strong>WarehouseItemInventory</strong> is the model/entity class that represents inventory of items within the warehouse.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class WarehouseItemInventory implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Warehouse warehouse;
	private Item item;
	private Long stock_lvl;
	private WarehouseItems settings;
	
	public WarehouseItemInventory()
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
	public Warehouse getWarehouse() {
		if(warehouse == null)
			warehouse = new Warehouse();
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
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
	public WarehouseItems getSettings() {
		if(settings == null)
			settings = new WarehouseItems();
		return settings;
	}

	public void setSettings(WarehouseItems settings) {
		this.settings = settings;
	}
	
}
