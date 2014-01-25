package com.dexter.nipost.mbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import com.dexter.nipost.bean.PropertyBean;
import com.dexter.nipost.model.Property;
import com.dexter.nipost.model.User;

@ManagedBean(name = "propertyMBean")
@SessionScoped
public class PropertyManagementBean implements Serializable
{
	private static final long serialVersionUID = 5573954364893170524L;
	
	final Logger logger = Logger.getLogger("NipostWS-UserManagementBean");
	
	private FacesMessage msg = null;
	
	private Property property;
	private String state;
	private String lg;
	
	private UploadedFile interiorImageFile;
	private StreamedContent interiorImage;
	private boolean interiorChanged;
	
	private UploadedFile exteriorImageFile;
	private StreamedContent exteriorImage;
	private boolean exteriorChanged;
	
	private UploadedFile ccspaceImageFile;
	private StreamedContent ccspaceImage;
	private boolean ccspaceChanged;
	
	private UploadedFile buildImageFile;
	private StreamedContent buildImage;
	private boolean buildChanged;
	
	private ArrayList<Property> properties;
	
	private String cenCoords;
	private MapModel simpleModel;
	private Marker marker;
	
	public String getRandomNumber()
	{
		return ""+new Random().nextInt(10000);
	}
	
	public String createProperty()
	{
		if(getInteriorImageFile() != null)
		{
			getProperty().setInterior_photo(getInteriorImageFile().getContents());
		}
		
		if(getExteriorImageFile() != null)
		{
			getProperty().setExterior_photo(getExteriorImageFile().getContents());
		}
		
		if(getCcspaceImageFile() != null)
		{
			getProperty().setCcspace_photo(getCcspaceImageFile().getContents());
		}
		
		if(getBuildImageFile() != null)
		{
			getProperty().setBuilding_photo(getBuildImageFile().getContents());
		}
		
		getProperty().setUser_id(""+getLoggedUser().getId());
		
		String ret = new PropertyBean().persistProperty(getProperty());
		if(ret.equalsIgnoreCase("success"))
		{
			setInteriorImageFile(null);
			setExteriorImageFile(null);
			setCcspaceImageFile(null);
			setBuildImageFile(null);
			setProperty(null);
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Property created successfully!");
		}
		else
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Property create failed. Please ensure all fields are filled properly. Extra: " + ret);
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		return "insert";
	}
	
	public String searchProperties()
	{
		setProperties(null);
		
		if(getLoggedUser().getEmail().equalsIgnoreCase("dele.olaore@gmail.com") || getLoggedUser().getEmail().equalsIgnoreCase("victorokere@gmail.com"))
			setProperties(new PropertyBean().getProperties(getState(), getLg()));
		else
			setProperties(new PropertyBean().getProperties(getState(), getLg(), getLoggedUser()));
		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Search", getProperties().size() + " record(s) found!");
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		return "search";
	}
	
	public String view2(Long id)
	{
		String ret = "viewProperty";
		
		setInteriorChanged(false);
		setExteriorChanged(false);
		setCcspaceChanged(false);
		setBuildChanged(false);
		setInteriorImageFile(null);
		setExteriorImageFile(null);
		setCcspaceImageFile(null);
		setBuildImageFile(null);
		setProperty(null);
		
		Property p = new PropertyBean().getProperty(""+id);
		if(p != null)
		{
			setProperty(p);
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "View", "Viewing property.");
		}
		else
		{
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Property not found!.");
			ret = "search"; 
		}
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return ret;
	}
	
	public String view(Long id)
	{
		String ret = "update";
		
		setInteriorChanged(false);
		setExteriorChanged(false);
		setCcspaceChanged(false);
		setBuildChanged(false);
		setInteriorImageFile(null);
		setExteriorImageFile(null);
		setCcspaceImageFile(null);
		setBuildImageFile(null);
		setProperty(null);
		
		Property p = new PropertyBean().getProperty(""+id);
		if(p != null)
		{
			setProperty(p);
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "View", "Viewing property, you can make changes to the record and save it.");
		}
		else
		{
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Property not found!.");
			ret = "search"; 
		}
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return ret;
	}
	
	public String updateProperty()
	{
		if(getInteriorImageFile() != null && isInteriorChanged())
		{
			getProperty().setInterior_photo(getInteriorImageFile().getContents());
		}
		
		if(getExteriorImageFile() != null && isExteriorChanged())
		{
			getProperty().setExterior_photo(getExteriorImageFile().getContents());
		}
		
		if(getCcspaceImageFile() != null && isCcspaceChanged())
		{
			getProperty().setCcspace_photo(getCcspaceImageFile().getContents());
		}
		
		if(getBuildImageFile() != null && isBuildChanged())
		{
			getProperty().setBuilding_photo(getBuildImageFile().getContents());
		}
		
		getProperty().setUser_id(""+getLoggedUser().getId());
		
		String ret = new PropertyBean().updateProperty(getProperty());
		if(ret.equalsIgnoreCase("success"))
		{
			setInteriorChanged(false);
			setExteriorChanged(false);
			setCcspaceChanged(false);
			setBuildChanged(false);
			setInteriorImageFile(null);
			setExteriorImageFile(null);
			setCcspaceImageFile(null);
			setBuildImageFile(null);
			setProperty(null);
			setProperties(null);
			
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Property updated successfully!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return searchProperties();
		}
		else
		{
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Property update failed. Please ensure all fields are filled properly. Extra: " + ret);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "update";
		}
	}
	
	public String delete(Long id)
	{
		String ret = new PropertyBean().deleteProperty(""+id);
		if(ret.equalsIgnoreCase("success"))
		{
			searchProperties();
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Property deleted successfully!");
		}
		else
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Property delete failed. Please ensure property exists. Extra: " + ret);
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		return "search";
	}

	public Property getProperty() {
		if(property == null)
			property = new Property();
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLg() {
		return lg;
	}

	public void setLg(String lg) {
		this.lg = lg;
	}

	public UploadedFile getInteriorImageFile() {
		return interiorImageFile;
	}

	public void setInteriorImageFile(UploadedFile interiorImageFile)
	{
		this.interiorImageFile = interiorImageFile;
		if(interiorImageFile != null)
		{
			setInteriorChanged(true);
			/*byte[] imgBytes = interiorImageFile.getContents();
			if(imgBytes != null)
			{
				try
				{
					setInteriorImage(new DefaultStreamedContent(new ByteArrayInputStream(imgBytes)));
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}*/
		}
	}

	public StreamedContent getInteriorImage() {
		return interiorImage;
	}

	public void setInteriorImage(StreamedContent interiorImage) {
		this.interiorImage = interiorImage;
	}

	public boolean isInteriorChanged() {
		return interiorChanged;
	}

	public void setInteriorChanged(boolean interiorChanged) {
		this.interiorChanged = interiorChanged;
	}

	public UploadedFile getExteriorImageFile() {
		return exteriorImageFile;
	}

	public void setExteriorImageFile(UploadedFile exteriorImageFile)
	{
		this.exteriorImageFile = exteriorImageFile;
		if(exteriorImageFile != null)
		{
			setExteriorChanged(true);
			/*byte[] imgBytes = exteriorImageFile.getContents();
			if(imgBytes != null)
			{
				try
				{
					setExteriorImage(new DefaultStreamedContent(new ByteArrayInputStream(imgBytes)));
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}*/
		}
	}

	public StreamedContent getExteriorImage() {
		return exteriorImage;
	}

	public void setExteriorImage(StreamedContent exteriorImage) {
		this.exteriorImage = exteriorImage;
	}

	public boolean isExteriorChanged() {
		return exteriorChanged;
	}

	public void setExteriorChanged(boolean exteriorChanged) {
		this.exteriorChanged = exteriorChanged;
	}

	public UploadedFile getCcspaceImageFile() {
		return ccspaceImageFile;
	}

	public void setCcspaceImageFile(UploadedFile ccspaceImageFile)
	{
		this.ccspaceImageFile = ccspaceImageFile;
		if(ccspaceImageFile != null)
		{
			setCcspaceChanged(true);
			/*byte[] imgBytes = ccspaceImageFile.getContents();
			if(imgBytes != null)
			{
				try
				{
					setCcspaceImage(new DefaultStreamedContent(new ByteArrayInputStream(imgBytes)));
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}*/
		}
	}

	public StreamedContent getCcspaceImage() {
		return ccspaceImage;
	}

	public void setCcspaceImage(StreamedContent ccspaceImage) {
		this.ccspaceImage = ccspaceImage;
	}

	public boolean isCcspaceChanged() {
		return ccspaceChanged;
	}

	public void setCcspaceChanged(boolean ccspaceChanged) {
		this.ccspaceChanged = ccspaceChanged;
	}

	public UploadedFile getBuildImageFile() {
		return buildImageFile;
	}

	public void setBuildImageFile(UploadedFile buildImageFile)
	{
		this.buildImageFile = buildImageFile;
		if(buildImageFile != null)
		{
			setBuildChanged(true);
			/*byte[] imgBytes = buildImageFile.getContents();
			if(imgBytes != null)
			{
				try
				{
					setBuildImage(new DefaultStreamedContent(new ByteArrayInputStream(imgBytes)));
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}*/
		}
	}

	public StreamedContent getBuildImage() {
		return buildImage;
	}

	public void setBuildImage(StreamedContent buildImage) {
		this.buildImage = buildImage;
	}

	public boolean isBuildChanged() {
		return buildChanged;
	}

	public void setBuildChanged(boolean buildChanged) {
		this.buildChanged = buildChanged;
	}

	public ArrayList<Property> getProperties() {
		if(properties == null)
			properties = new ArrayList<Property>();
		return properties;
	}

	public void setProperties(ArrayList<Property> properties) {
		this.properties = properties;
	}
	
	public void onMarkerSelect(OverlaySelectEvent event)
	{
        marker = (Marker) event.getOverlay();
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker", "Selected: " + marker.getTitle());
        
        setProperty(null);
        ArrayList<Property> allProperties = new PropertyBean().getProperties((getState() != null && getState().trim().length() > 0) ? getState() : null, (getLg() != null && getLg().trim().length() > 0) ? getLg() : null);
		for(Property e : allProperties)
		{
			if(e.getAddr().equals(marker.getTitle()))
			{
				setProperty(e);
				break;
			}
		}
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
		if(getProperty().getAddr() != null)
		{
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("viewProperty.xhtml");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
    }

	public String getCenCoords() {
		if(cenCoords == null)
			cenCoords = "6.427887,3.4287645";
		return cenCoords;
	}

	public void setCenCoords(String cenCoords) {
		this.cenCoords = cenCoords;
	}

	public void resetMap()
	{
		setSimpleModel(null);
	}
	
	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public MapModel getSimpleModel()
	{
		if(simpleModel == null)
		{
			simpleModel = new DefaultMapModel();
			boolean centSet = false;
			ArrayList<Property> allProperties = new PropertyBean().getProperties((getState() != null && getState().trim().length() > 0) ? getState() : null, (getLg() != null && getLg().trim().length() > 0) ? getLg() : null);
			for(Property e : allProperties)
			{
		        //Shared coordinates
				try
				{
		        LatLng coord1 = new LatLng(Double.parseDouble(e.getLat()), Double.parseDouble(e.getLon()));
		        //Basic marker
		        simpleModel.addOverlay(new Marker(coord1, e.getAddr()));
		        if(!centSet)
		        {
		        	setCenCoords(e.getLat()+","+e.getLon());
		        	centSet = true;
		        }
				}
				catch(Exception ex)
				{}
			}
		}
		return simpleModel;
	}
	
	public User getLoggedUser()
	{
		return (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}
}
