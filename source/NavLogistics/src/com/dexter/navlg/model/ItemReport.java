package com.dexter.navlg.model;

import java.io.Serializable;

import org.jboss.solder.core.Veto;

/**
 * A generic class to handle some data transfer from controller to view.
 * */
@Veto
public class ItemReport implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Item item;
	private WarehouseItems settings;
	private ItemSupply supply;
	private Long qtyIssued;
	
	public ItemReport()
	{}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public WarehouseItems getSettings() {
		return settings;
	}

	public void setSettings(WarehouseItems settings) {
		this.settings = settings;
	}

	public ItemSupply getSupply() {
		return supply;
	}

	public void setSupply(ItemSupply supply) {
		this.supply = supply;
	}

	public Long getQtyIssued() {
		return qtyIssued;
	}

	public void setQtyIssued(Long qtyIssued) {
		this.qtyIssued = qtyIssued;
	}
	
}
