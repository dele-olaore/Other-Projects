package com.dexter.navlg.model;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.solder.core.Veto;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 * <p>
 * <strong>Item</strong> is the model/entity class that represents an item on the ship.
 * </p>
 *
 * @author Dele Olaore
 * */
@Entity
@Veto
public class Item implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String code;
	private String description;
	private String img_type;
	private byte[] image;
	private String barcode;
	private Supplier supplier;
	
	private UploadedFile imageFile;
	private StreamedContent graphicImage;
	
	private String customized_part_number;
	private String serial_number;
	private String sparepart_part_number;
	private String remark;
	
	public Item()
	{}
	
	public Item(String name)
	{
		this.name = name;
	}

	@Id
    @NotNull
    @GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@Size(min = 1, max = 30)
	@Column(unique=true, nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull
	@Size(min = 3, max = 3)
	@Column(unique=true, nullable=false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImg_type() {
		return img_type;
	}

	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}

	@Column(length=100000)
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	@ManyToOne
	public Supplier getSupplier() {
		if(supplier == null)
			supplier = new Supplier();
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Transient
	public UploadedFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(UploadedFile imageFile) {
		this.imageFile = imageFile;
		if(imageFile != null && imageFile.getSize() > 0)
		{
			setImage(imageFile.getContents());
			setImg_type(imageFile.getContentType());
		}
	}

	@Transient
	public StreamedContent getGraphicImage() {
		if(graphicImage == null)
		{
			try
			{
				graphicImage = new DefaultStreamedContent(new ByteArrayInputStream(getImage()), getImg_type());
				System.out.println("in getGraphicImage: " + graphicImage.getContentType() + ", image length: " + getImage().length);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return graphicImage;
	}

	public String getCustomized_part_number() {
		return customized_part_number;
	}

	public void setCustomized_part_number(String customized_part_number) {
		this.customized_part_number = customized_part_number;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getSparepart_part_number() {
		return sparepart_part_number;
	}

	public void setSparepart_part_number(String sparepart_part_number) {
		this.sparepart_part_number = sparepart_part_number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
