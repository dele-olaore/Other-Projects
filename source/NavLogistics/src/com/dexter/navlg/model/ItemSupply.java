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
 * <strong>ItemSupply</strong> is the model/entity class that represents a supply of items to a warehouse.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class ItemSupply implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Item item;
	private Long amount;
	private Date date;
	private Warehouse warehouse;
	private User user;
	private String container_number;
	private String rack;
	private String rack_number;
	
	public ItemSupply()
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
		if(item == null)
			item = new Item();
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
	public User getUser() {
		if(user == null)
			user = new User();
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContainer_number() {
		return container_number;
	}

	public void setContainer_number(String container_number) {
		this.container_number = container_number;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public String getRack_number() {
		return rack_number;
	}

	public void setRack_number(String rack_number) {
		this.rack_number = rack_number;
	}
}
