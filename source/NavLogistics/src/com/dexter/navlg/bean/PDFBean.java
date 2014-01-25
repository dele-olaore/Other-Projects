package com.dexter.navlg.bean;

import java.io.IOException;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.solder.logging.Logger;

import com.dexter.navlg.model.ShipRequest;
import com.dexter.navlg.model.User;
import com.dexter.navlg.security.Authenticated;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Table;

@Stateful
@SessionScoped
@Named("pdfBean")
public class PDFBean 
{
	@Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    @Authenticated
    private User user;
    
    private ShipRequest shipRequest;
    
    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException
    {
    	Document pdf = (Document) document;  
    	pdf.open();  
    	pdf.setPageSize(PageSize.A4);
    	
    	Table tb = new Table(4, 1);
    	
    	Table tb1 = new Table(1, 3);
    	tb1.addCell("Requesting unit");
    	tb1.addCell("Spareparts Request");
    	tb1.addCell("Date");
    	Cell cell1 = new Cell();
    	cell1.add(tb1);
    	tb.addCell(cell1);
    	
    	pdf.add(tb);
    }

	public ShipRequest getShipRequest() {
		return shipRequest;
	}

	public void setShipRequest(ShipRequest shipRequest) {
		this.shipRequest = shipRequest;
	}
    
}
