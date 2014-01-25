package com.dexter.recf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.jboss.solder.core.Veto;

/**
 * <p>
 * <strong>UploadedDocument</strong> is the model/entity class that represents an uploaded document.
 * </p>
 *
 * @author Dele Olaore
 * */
//@Entity
//@Veto
public class UploadedDocument implements Serializable
{
    private static final long serialVersionUID = -8192553629588066292L;
    
    private long id;
    private String name;
    private String type;
    
    private long length;
    private byte[] data;

    public UploadedDocument()
    {}
    
    @Transient
    private String mime;
    
    @Id
    @NotNull
    @GeneratedValue
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(length=60000)
	public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        int extDot = name.lastIndexOf('.');
        if (extDot > 0) {
            String extension = name.substring(extDot + 1);
            if ("bmp".equals(extension)) {
                mime = "image/bmp";
            } else if ("jpg".equals(extension)) {
                mime = "image/jpeg";
            } else if ("gif".equals(extension)) {
                mime = "image/gif";
            } else if ("png".equals(extension)) {
                mime = "image/png";
            } else if ("doc".equals(extension) || "docx".equals(extension)) {
                mime = "application/msword";
            } else if ("pdf".equals(extension)) {
                mime = "application/acrobat";
            } else {
                mime = "application/unknown";
            }
        }
        this.name = name;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Transient
    public String getMime() {
        return mime;
    }
}
