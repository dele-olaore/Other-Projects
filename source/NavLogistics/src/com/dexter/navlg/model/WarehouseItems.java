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
 * <strong>WarehouseItems</strong> is the model/entity class that represents a mapping of the items a warehouse should have.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class WarehouseItems implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Warehouse warehouse;
	private Item item;
	private Long max_lvl;
	private Long min_lvl;
	private Long reorder_lvl;
	
	public WarehouseItems()
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

	public Long getMax_lvl() {
		return max_lvl;
	}

	public void setMax_lvl(Long max_lvl) {
		this.max_lvl = max_lvl;
	}

	public Long getMin_lvl() {
		return min_lvl;
	}

	public void setMin_lvl(Long min_lvl) {
		this.min_lvl = min_lvl;
	}

	public Long getReorder_lvl() {
		return reorder_lvl;
	}

	public void setReorder_lvl(Long reorder_lvl) {
		this.reorder_lvl = reorder_lvl;
	}
}
