package com.dexter.biodata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;
import java.util.Vector;

import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageReaderWriterSpi;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import veridis.biometric.BiometricImage;
import veridis.biometric.JBiometricPanel;

import com.dexter.biodata.model.BioData;
import com.dexter.biodata.model.Config;
import com.dexter.biodata.model.Sector;
import com.dexter.biodata.model.State;
import com.dexter.biodata.model.User;
import com.dexter.biodata.util.DBUtil;
import com.dexter.biodata.util.SpringUtilities;
import com.dexter.biodata.util.Util;

public class BioDataForm extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	//LoginDialog loginDialog;
	private User curUser;
	
	private Util fingerprintSDK;
	private DBUtil dbUtil;
	
	private JScrollPane logScrollPane = null;
	private JTextArea logTextArea = null;
	
	private BufferedImage fingerprintImage = null, photoImage = null;
	
	private JPanel photoViewPanel = null;
	
	private JTabbedPane contentTabs;
	private JScrollPane enrollScrollPane;
	
	JPanel centerPanel = new JPanel(new BorderLayout());
	JPanel wizard1, wizard2, wizard3, wizard4, wizard5, preview;
	
	JButton btnBack = new JButton("|<< Back");
	JButton btnNext = new JButton("Next >>|");
	JButton btnSave = new JButton("Save");
	
	JButton btnCancelUpdate = new JButton("Cancel Update");
	
	private int screen = 1;
	
	private JComboBox cbState, cbSector;
	private JLabel lbState, lbSector;
	
	private JComboBox cbTitle, cbGender, cbMaritalStatus, cbStateOrigin;
	private JLabel lbTitle, lbGender, lbMaritalStatus, lbStateOrigin;
	private JComboBox cbMonth, cbDay;
	private JLabel lbMonth, lbDay;
	private JTextField txtSurName, txtFirstName, txtMiddleName, txtMaidenName;
	private JLabel lbSurName, lbFirstName, lbMiddleName, lbMaidenName;
	
	private JTextField txtBirthPlace, txtYear;
	private JLabel lbBirthPlace, lbYear;
	private JTextField txtLgOrigin, txtTown, txtReligion, txtNationality;
	private JLabel lbLgOrigin, lbTown, lbReligion, lbNationality;
	
	private JTextField txtAddr1, txtAddr2, txtAddrHm1, txtAddrHm2, txtMobile1, txtMobile2, txtMobile3, txtEmail;
	private JLabel lbAddr1, lbAddr2, lbAddrHm1, lbAddrHm2, lbMobile1, lbMobile2, lbMobile3, lbEmail;
	
	private JTextField txtGradeLvl, txtWorkStation, txtNis, txtRank, txtStationLocation, txtProfession;
	private JLabel lbGradeLvl, lbWorkStation, lbNis, lbRank, lbStationLocation, lbProfession;
	
	private JTextField txtNextSurname, txtNextMiddlename, txtNextFirstname, txtNextRelationship, txtNextAddr1, txtNextAddr2, txtNextMobile;
	private JLabel lbNextSurname, lbNextMiddlename, lbNextFirstname, lbNextRelationship, lbNextAddr1, lbNextAddr2, lbNextMobile;
	private JComboBox cbNextNoOfChild;
	private JLabel lbNextNoOfChild;
	private JTextField txtChildName1, txtChildName2, txtChildName3, txtChildName4;
	private JLabel lbChildName1, lbChildName2, lbChildName3, lbChildName4;
	private JTextField txtChildDOB1, txtChildDOB2, txtChildDOB3, txtChildDOB4;
	private JLabel lbChildDOB1, lbChildDOB2, lbChildDOB3, lbChildDOB4;
	
	private JComboBox cbMoe;
	private JLabel lbMoe;
	private JTextField txtDoa, txtDoc;
	private JLabel lbDoa, lbDoc;
	private JTextField txtCert1, txtCert1Date, txtCert2, txtCert2Date, txtCert3, txtCert3Date;
	private JLabel lbCert1, lbCert1Date, lbCert2, lbCert2Date, lbCert3, lbCert3Date;
	private JTextField txtOtherCert1, txtOtherCert1Date, txtOtherCert2, txtOtherCert2Date, txtOtherCert3, txtOtherCert3Date;
	private JLabel lbOtherCert1, lbOtherCert1Date, lbOtherCert2, lbOtherCert2Date, lbOtherCert3, lbOtherCert3Date;
	private JTextField txtRankLastPro, txtMonthly, txtAnnual, txtRetDate, txtAuthName, txtAuthRemark, txtAuthDate, txtCheckedBy;
	private JLabel lbRankLastPro, lbMonthly, lbAnnual, lbRetDate, lbAuthName, lbAuthRemark, lbAuthDate, lbCheckedBy;
	
	private List<State> states;
	private List<Sector> sectors;
	
	private JLabel sensorName = new JLabel("No sensor detected!");
	
	private JBiometricPanel imagePanel = new JBiometricPanel();
	// Veridis Free SDK: "0000-019A-C4CF-DBCD-405B"
	
	private SearchPanel searchPanel;
	
	BioData data = null;
	
	public BioDataForm()
	{
		super("Enrolment Form - Nigeria Immigration Service");
		
		init();
		
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent paramWindowEvent)
			{
				BioDataForm.this.destroy();
				System.exit(0);
			}
		});
	}
	
	public void init()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception localException)
		{}
		
		setSize(800, 700);
		
		fingerprintSDK = new Util(this);
		
		dbUtil = new DBUtil();
		
		loadInitDataFromDB();
		
		//setJMenuBar(createMenuBar());
		setContentPane(createContentPane());
		
		//loginDialog = new LoginDialog(this);
	}
	
	public void destroy()
	{
		fingerprintSDK.destroy();
		
		dbUtil.destroy();
	}
	
	private void loadInitDataFromDB()
	{
		try
		{
		states = (List<State>)dbUtil.findAll("State");
		}
		catch(Exception ex){}
		try
		{
		sectors = (List<Sector>)dbUtil.findAll("Sector");
		}
		catch(Exception ex){}
	}
	
	private void save()
	{
		if(JOptionPane.showConfirmDialog(this, "Are you sure?", "Save", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
			return;
		
		if(data == null)
			data = new BioData();
		
		Object config = dbUtil.find(Config.class, 1L);
		if(config != null)
		{
			String id_code = ((Config)config).getIdentificationcode();
			
			id_code = id_code + "-" + UUID.randomUUID().toString();
			
			data.setIdentificationcode(id_code);
		}
		
		Object usersObj = dbUtil.findAll("User");
		if(usersObj != null)
		{
			List<User> users = (List<User>)usersObj;
			if(users.size() > 0)
			{
				data.setCreatedByUser(users.get(0));
			}
		}
		
		String title = null;
		if(cbTitle.getSelectedIndex() >= 0)
			title = cbTitle.getSelectedItem().toString();
		data.setTitle(title);
		
		String gender = null;
		if(cbGender.getSelectedIndex() >= 0)
			gender = cbGender.getSelectedItem().toString();
		data.setGender(gender);
		
		String msStatus = null;
		if(cbMaritalStatus.getSelectedIndex() >= 0)
			msStatus = cbMaritalStatus.getSelectedItem().toString();
		data.setMaritalStatus(msStatus);
		
		String stateOfOrigin = null;
		if(cbStateOrigin.getSelectedIndex() >= 0)
			stateOfOrigin = cbStateOrigin.getSelectedItem().toString();
		data.setStateOfOrigin(stateOfOrigin);
		
		String dobM = null;
		if(cbMonth.getSelectedIndex() >= 0)
			dobM = cbMonth.getSelectedItem().toString();
		data.setMonthOfBirth(dobM);
		
		String dobD = null;
		if(cbDay.getSelectedIndex() >= 0)
			dobD = cbDay.getSelectedItem().toString();
		data.setDayOfBirth(dobD);
		
		String lastname = txtSurName.getText();
		data.setLastname(lastname);
		String firstname = txtFirstName.getText();
		data.setFirstname(firstname);
		String middlename = txtMiddleName.getText();
		data.setMiddlename(middlename);
		String maidenname = txtMaidenName.getText();
		data.setMaidenname(maidenname);
		
		if((lastname == null || (lastname != null && lastname.trim().length() == 0)) ||
				(firstname == null) || (firstname != null && firstname.trim().length() == 0))
		{
			writeLog("Error: First and Last name fields are required");
			JOptionPane.showMessageDialog(BioDataForm.this, "First and Last name fields are required", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String birthPlace = txtBirthPlace.getText();
		data.setPlaceOfBirth(birthPlace);
		String birthYear = txtYear.getText();
		data.setYearOfBirth(birthYear);
		if(birthYear != null && (birthYear.trim().length() != 4))
		{
			writeLog("Error: Year of birth value is not valid");
			JOptionPane.showMessageDialog(BioDataForm.this, "Year of birth value is not valid", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String lgOfOrigin = txtLgOrigin.getText();
		data.setLgOfOrigin(lgOfOrigin);
		String town = txtTown.getText();
		data.setTown(town);
		String religion = txtReligion.getText();
		data.setReligion(religion);
		String nationality = txtNationality.getText();
		data.setNationality(nationality);
		
		String addr1 = txtAddr1.getText();
		data.setAddr1(addr1);
		String addr2 = txtAddr2.getText();
		data.setAddr2(addr2);
		String addrHm1 = txtAddrHm1.getText();
		data.setAddrHm1(addrHm1);
		String addrHm2 = txtAddrHm2.getText();
		data.setAddrHm2(addrHm2);
		String mobile1 = txtMobile1.getText();
		data.setMobile1(mobile1);
		String mobile2 = txtMobile2.getText();
		data.setMobile2(mobile2);
		String mobile3 = txtMobile3.getText();
		data.setMobile3(mobile3);
		String email = txtEmail.getText();
		data.setEmail(email);
		
		String gradeLvl = txtGradeLvl.getText();
		data.setGradeLvl(gradeLvl);
		String workStation = txtWorkStation.getText();
		data.setWorkStation(workStation);
		String nis = txtNis.getText();
		data.setNis(nis);
		String rank = txtRank.getText();
		data.setRank(rank);
		String stationLocation = txtStationLocation.getText();
		data.setStationLocation(stationLocation);
		String profession = txtProfession.getText();
		data.setProfession(profession);
		
		String nok_lastname = txtNextSurname.getText();
		data.setNok_lastname(nok_lastname);
		String nok_middlename = txtNextMiddlename.getText();
		data.setNok_middlename(nok_middlename);
		String nok_firstname = txtNextFirstname.getText();
		data.setNok_firstname(nok_firstname);
		String nok_relationship = txtNextRelationship.getText();
		data.setNok_relatioship(nok_relationship);
		String nok_addr1 = txtNextAddr1.getText();
		data.setNok_addr1(nok_addr1);
		String nok_addr2 = txtNextAddr2.getText();
		data.setNok_addr2(nok_addr2);
		String nok_mobile = txtNextMobile.getText();
		data.setNok_mobile(nok_mobile);
		
		String noOfChildren = cbNextNoOfChild.getSelectedItem().toString();
		
		int noc = 0;
		try
		{
			noc = Integer.parseInt(noOfChildren);
			data.setNoOfChildren(noc);
		}
		catch(Exception ex)
		{
			writeLog("Error: No of children value is not valid");
			JOptionPane.showMessageDialog(BioDataForm.this, "No of children value is not valid", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(noc >= 1)
		{
		String child1Name = txtChildName1.getText();
		data.setChild1_name(child1Name);
		String child1DOB = txtChildDOB1.getText();
		data.setChild1_dob(child1DOB);
		}
		if(noc >= 2)
		{
		String child2Name = txtChildName2.getText();
		data.setChild2_name(child2Name);
		String child2DOB = txtChildDOB2.getText();
		data.setChild2_dob(child2DOB);
		}
		if(noc >= 3)
		{
		String child3Name = txtChildName3.getText();
		data.setChild3_name(child3Name);
		String child3DOB = txtChildDOB3.getText();
		data.setChild3_dob(child3DOB);
		}
		if(noc >= 4)
		{
		String child4Name = txtChildName4.getText();
		data.setChild4_name(child4Name);
		String child4DOB = txtChildDOB4.getText();
		data.setChild4_dob(child4DOB);
		}
		
		if(cbState.getSelectedIndex() >= 0)
		{
			State st = states.get(cbState.getSelectedIndex());
			data.setState(st);
		}
		
		if(cbSector.getSelectedIndex() >= 0)
		{
			Sector sec = sectors.get(cbSector.getSelectedIndex());
			data.setSector(sec);
		}
		
		try
		{
			if(fingerprintImage != null)
				data.setFingerprint(Util.bufferedImageToByteArray(fingerprintImage));
			else if(JOptionPane.showConfirmDialog(this, "No fingerprint captured. Are you sure you want to save with out a fingerprint capture?", "Save", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
				return;
				
		}
		catch(Exception ex)
		{
			writeLog("Error: Error occured during fingerprint conversion.\n\nMessage: " + ex.getMessage());
			JOptionPane.showMessageDialog(BioDataForm.this, "Error occured during fingerprint conversion.\n\nMessage: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try
		{
			if(photoImage != null)
				data.setPhoto(Util.bufferedImageToByteArray(photoImage));
			else if(JOptionPane.showConfirmDialog(this, "No photo selected. Are you sure you want to save with out a photo?", "Save", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
				return;
		}
		catch(Exception ex)
		{
			writeLog("Error: Error occured during photo conversion.\n\nMessage: " + ex.getMessage());
			JOptionPane.showMessageDialog(BioDataForm.this, "Error occured during photo conversion.\n\nMessage: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		data.setMoe((cbMoe.getSelectedIndex() >= 0) ? cbMoe.getSelectedItem().toString() : "");
		data.setDoa(txtDoa.getText());
		data.setDoc(txtDoc.getText());
		
		data.setCert1(txtCert1.getText());
		data.setCert2(txtCert2.getText());
		data.setCert3(txtCert3.getText());
		data.setCert1_date(txtCert1Date.getText());
		data.setCert2_date(txtCert2Date.getText());
		data.setCert3_date(txtCert3Date.getText());
		
		data.setOthercert1(txtOtherCert1.getText());
		data.setOthercert2(txtOtherCert2.getText());
		data.setOthercert3(txtOtherCert3.getText());
		data.setOthercert1_date(txtOtherCert1Date.getText());
		data.setOthercert2_date(txtOtherCert2Date.getText());
		data.setOthercert3_date(txtOtherCert3Date.getText());
		
		data.setRank_on_last_pro(txtRankLastPro.getText());
		data.setMonthly(txtMonthly.getText());
		data.setAnnual(txtAnnual.getText());
		data.setProposedRetDate(txtRetDate.getText());
		data.setAuthName(txtAuthName.getText());
		data.setAuthDate(txtAuthDate.getText());
		data.setAuthRemarks(txtAuthRemark.getText());
		data.setCheckedBy(txtCheckedBy.getText());
		
		data.setCrt_dt(new Date());
		
		dbUtil.startTransaction();
		
		if(data.getId() == null)
		{
			if(dbUtil.save(data))
			{
				dbUtil.commit();
				
				data = null;
				clearFields();
				preview();
				writeLog("Success: Save successful");
				JOptionPane.showMessageDialog(BioDataForm.this, "Save successful", "Success", JOptionPane.INFORMATION_MESSAGE);
				
				screen = 1;
				switchScreen();
			}
			else
			{
				dbUtil.rollback();
				writeLog("Error: Error occured during data save. Message: " + dbUtil.getMessage());
				JOptionPane.showMessageDialog(BioDataForm.this, "Error occured during data save.\n\nMessage: " + dbUtil.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(data.getId() > 0)
		{
			if(dbUtil.update(data))
			{
				dbUtil.commit();
				
				data = null;
				clearFields();
				preview();
				writeLog("Success: Save successful");
				JOptionPane.showMessageDialog(BioDataForm.this, "Save successful", "Success", JOptionPane.INFORMATION_MESSAGE);
				
				screen = 1;
				switchScreen();
			}
			else
			{
				dbUtil.rollback();
				writeLog("Error: Error occured during data save. Message: " + dbUtil.getMessage());
				JOptionPane.showMessageDialog(BioDataForm.this, "Error occured during data save.\n\nMessage: " + dbUtil.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void setFields()
	{
		if(data != null)
		{
			btnCancelUpdate.setEnabled(true);
			
			try
			{
				int pos = -1;
				if(data.getState() != null)
				{
					for(State e : states)
					{
						pos += 1;
						if(e.getId() == data.getState().getId())
						{
							break;
						}
					}
				}
				cbState.setSelectedIndex(pos);
			}
			catch(Exception ig){}
			
			try
			{
				int pos = -1;
				if(data.getSector() != null)
				{
					for(Sector e : sectors)
					{
						pos += 1;
						if(e.getId() == data.getSector().getId())
						{
							break;
						}
					}
				}
				cbSector.setSelectedIndex(pos);
			}
			catch(Exception ig){}
			
			cbTitle.setSelectedItem(data.getTitle());
			cbGender.setSelectedItem(data.getGender());
			cbMaritalStatus.setSelectedItem(data.getMaritalStatus());
			
			if(data.getStateOfOrigin() != null)
			{
				cbStateOrigin.setSelectedItem(data.getStateOfOrigin());
			}
			
			if(data.getMonthOfBirth() != null)
				cbMonth.setSelectedItem(data.getMonthOfBirth());
			if(data.getDayOfBirth() != null)
				cbDay.setSelectedItem(data.getDayOfBirth());
			
			txtSurName.setText(data.getLastname());
			txtFirstName.setText(data.getFirstname());
			txtMiddleName.setText(data.getMiddlename());
			txtMaidenName.setText(data.getMaidenname());
			
			txtBirthPlace.setText(data.getPlaceOfBirth());
			txtYear.setText(data.getYearOfBirth());
			txtLgOrigin.setText(data.getLgOfOrigin());
			txtTown.setText(data.getTown());
			txtReligion.setText(data.getReligion());
			txtNationality.setText(data.getNationality());
			
			txtAddr1.setText(data.getAddr1());
			txtAddr2.setText(data.getAddr2());
			txtAddrHm1.setText(data.getAddrHm1());
			txtAddrHm2.setText(data.getAddrHm2());
			txtMobile1.setText(data.getMobile1());
			txtMobile2.setText(data.getMobile2());
			txtMobile3.setText(data.getMobile3());
			txtEmail.setText(data.getEmail());
			
			txtGradeLvl.setText(data.getGradeLvl());
			txtWorkStation.setText(data.getWorkStation());
			txtNis.setText(data.getNis());
			txtRank.setText(data.getRank());
			txtStationLocation.setText(data.getStationLocation());
			txtProfession.setText(data.getProfession());
			
			txtNextSurname.setText(data.getNok_lastname());
			txtNextMiddlename.setText(data.getNok_middlename());
			txtNextFirstname.setText(data.getNok_firstname());
			txtNextRelationship.setText(data.getNok_relatioship());
			txtNextAddr1.setText(data.getNok_addr1());
			txtNextAddr2.setText(data.getNok_addr2());
			txtNextMobile.setText(data.getNok_mobile());
			if(data.getNoOfChildren()>=0)
				cbNextNoOfChild.setSelectedItem(""+data.getNoOfChildren());
			txtChildName1.setText(data.getChild1_name());
			txtChildName2.setText(data.getChild2_name());
			txtChildName3.setText(data.getChild3_name());
			txtChildName4.setText(data.getChild4_name());
			txtChildDOB1.setText(data.getChild1_dob());
			txtChildDOB2.setText(data.getChild2_dob());
			txtChildDOB3.setText(data.getChild3_dob());
			txtChildDOB4.setText(data.getChild4_dob());
			
			if(data.getMoe() != null)
			cbMoe.setSelectedItem(data.getMoe());
			txtDoa.setText(data.getDoa());
			txtDoc.setText(data.getDoc());
			
			txtCert1.setText(data.getCert1());
			txtCert2.setText(data.getCert2());
			txtCert3.setText(data.getCert3());
			txtCert1Date.setText(data.getCert1_date());
			txtCert2Date.setText(data.getCert2_date());
			txtCert3Date.setText(data.getCert3_date());
			
			txtOtherCert1.setText(data.getOthercert1());
			txtOtherCert2.setText(data.getOthercert2());
			txtOtherCert3.setText(data.getOthercert3());
			txtOtherCert1Date.setText(data.getOthercert1_date());
			txtOtherCert2Date.setText(data.getOthercert2_date());
			txtOtherCert3Date.setText(data.getOthercert3_date());
			
			txtRankLastPro.setText(data.getRank_on_last_pro());
			txtMonthly.setText(data.getMonthly());
			txtAnnual.setText(data.getAnnual());
			txtRetDate.setText(data.getProposedRetDate());
			txtAuthName.setText(data.getAuthName());
			txtAuthDate.setText(data.getAuthDate());
			txtAuthRemark.setText(data.getAuthRemarks());
			txtCheckedBy.setText(data.getCheckedBy());
			
			if(data.getPhoto() != null)
			{
				try
				{
					showPhotoImage(Util.byteArrayToBufferedImage(data.getPhoto()));
				}
				catch(Exception ig)
				{
					ig.printStackTrace();
				}
			}
			
			if(data.getFingerprint() != null)
			{
				BiometricImage bimg = new BiometricImage(new ImageIcon(data.getFingerprint()).getImage(), 500);
				showFingerprintImage(bimg);
			}
			
			screen = 1;
			switchScreen();
			contentTabs.setSelectedIndex(0);
		}
	}
	
	private void clearFields()
	{
		try
		{
			cbState.setSelectedIndex(0);
		}
		catch(Exception ig){}
		
		try
		{
			cbSector.setSelectedIndex(0);
		}
		catch(Exception ig){}
		
		cbTitle.setSelectedIndex(0);
		cbGender.setSelectedIndex(0);
		cbMaritalStatus.setSelectedIndex(0);
		
		try
		{
			cbStateOrigin.setSelectedIndex(0);
		}
		catch(Exception ig){}
		
		cbMonth.setSelectedIndex(0);
		cbDay.setSelectedIndex(0);
		
		txtSurName.setText("");
		txtFirstName.setText("");
		txtMiddleName.setText("");
		txtMaidenName.setText("");
		
		txtBirthPlace.setText("");
		txtYear.setText("");
		txtLgOrigin.setText("");
		txtTown.setText("");
		txtReligion.setText("");
		txtNationality.setText("");
		
		txtAddr1.setText("");
		txtAddr2.setText("");
		txtAddrHm1.setText("");
		txtAddrHm2.setText("");
		txtMobile1.setText("");
		txtMobile2.setText("");
		txtMobile3.setText("");
		txtEmail.setText("");
		
		txtGradeLvl.setText("");
		txtWorkStation.setText("");
		txtNis.setText("");
		txtRank.setText("");
		txtStationLocation.setText("");
		txtProfession.setText("");
		
		txtNextSurname.setText("");
		txtNextMiddlename.setText("");
		txtNextFirstname.setText("");
		txtNextRelationship.setText("");
		txtNextAddr1.setText("");
		txtNextAddr2.setText("");
		txtNextMobile.setText("");
		cbNextNoOfChild.setSelectedIndex(0);
		txtChildName1.setText("");
		txtChildName2.setText("");
		txtChildName3.setText("");
		txtChildName4.setText("");
		txtChildDOB1.setText("");
		txtChildDOB2.setText("");
		txtChildDOB3.setText("");
		txtChildDOB4.setText("");
		
		cbMoe.setSelectedIndex(0);
		txtDoa.setText("");
		txtDoc.setText("");
		
		txtCert1.setText("");
		txtCert2.setText("");
		txtCert3.setText("");
		txtCert1Date.setText("");
		txtCert2Date.setText("");
		txtCert3Date.setText("");
		
		txtOtherCert1.setText("");
		txtOtherCert2.setText("");
		txtOtherCert3.setText("");
		txtOtherCert1Date.setText("");
		txtOtherCert2Date.setText("");
		txtOtherCert3Date.setText("");
		
		txtRankLastPro.setText("");
		txtMonthly.setText("");
		txtAnnual.setText("");
		txtRetDate.setText("");
		txtAuthName.setText("");
		txtAuthDate.setText("");
		txtAuthRemark.setText("");
		txtCheckedBy.setText("");
		
		showPhotoImage(null);
		
		showFingerprintImage(null);
	}
	
	private JComponent createContentPane()
	{
		JPanel localJPanel = new JPanel(new BorderLayout());
		
		localJPanel.add(createHeader(), "North");
		
		localJPanel.add(createLogArea(), "South");
		
		localJPanel.add(createTabContentArea(), "Center");
		
		return localJPanel;
	}
	
	private JComponent createHeader()
	{
		JPanel headerPanel = new JPanel(new BorderLayout());
		
		JLabel title = new JLabel("NIGERIA IMMIGRATION SERVICE");
		title.setFont(new Font("arial", Font.BOLD, 28));
		JPanel titlePanel = new JPanel();
		titlePanel.add(title);
		
		headerPanel.add(titlePanel, BorderLayout.CENTER);
		
		ImageIcon myIcon2 = new ImageIcon(BioDataForm.class.getResource("coatArmNig2.gif"));
		Image img = myIcon2.getImage();
		Image newimg = img.getScaledInstance(100, 90, java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		
		JLabel armsLogo = new JLabel(newIcon);
		JPanel logoPanel = new JPanel();
		logoPanel.add(armsLogo);
		
		headerPanel.add(logoPanel, BorderLayout.WEST);
		
		return headerPanel;
	}
	
	private JComponent createTabContentArea()
	{
		contentTabs = new JTabbedPane(JTabbedPane.TOP);
		
		contentTabs.addTab("Enroll", createEnrolFormArea());
		contentTabs.addTab("Search", createSearchFormArea());
		
		return contentTabs;
	}
	
	private JComponent createEnrolFormArea()
	{
		JPanel enrolFormPanel = new JPanel(new BorderLayout());
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		
		rightPanel.add(new JLabel("Photo"));
		rightPanel.add(Box.createRigidArea(new Dimension(0,3)));
		rightPanel.add(createPhotoViewPanel());
		rightPanel.add(Box.createRigidArea(new Dimension(0,3)));
		
		JButton btnPhotoBrowse = new JButton("Browse...");
		btnPhotoBrowse.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				BioDataForm.this.loadFromFile();
			}
		});
		
		JButton btnClearPhoto = new JButton("Clear");
		btnClearPhoto.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(photoImage != null)
				{
					if(JOptionPane.showConfirmDialog(BioDataForm.this, "Are you sure?", "Clear photo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					{
						showPhotoImage(null);
					}
				}
			}
		});
		
		JPanel photoBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 4));
		photoBtnPanel.add(btnPhotoBrowse);
		photoBtnPanel.add(btnClearPhoto);
		
		rightPanel.add(photoBtnPanel);
		
		rightPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		rightPanel.add(new JLabel("Finger print"));
		rightPanel.add(Box.createRigidArea(new Dimension(0,3)));
		rightPanel.add(createFingerprintViewPanel());
		rightPanel.add(Box.createRigidArea(new Dimension(0,3)));
		sensorName.setToolTipText("The connected finger print reader");
		rightPanel.add(sensorName);
		rightPanel.add(Box.createRigidArea(new Dimension(0,3)));
		
		final JButton btnCapture = new JButton("Capture...");
		btnCapture.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(!fingerprintSDK.isCaptureOn)
				{
					fingerprintSDK.startCapture();
				}
			}
		});
		
		JButton btnClearFingerprint = new JButton("Clear");
		btnClearFingerprint.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(fingerprintImage != null)
				{
					if(JOptionPane.showConfirmDialog(BioDataForm.this, "Are you sure?", "Clear Fingerprint", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						showFingerprintImage(null);
				}
			}
		});
		
		JPanel fingerprintBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 4));
		fingerprintBtnPanel.add(btnCapture);
		fingerprintBtnPanel.add(btnClearFingerprint);
		
		rightPanel.add(fingerprintBtnPanel);
		
		//rightPanel.add(Box.createRigidArea(new Dimension(3,0)));
		//rightPanel.add(new JButton("Browse..."));
		
		Dimension minSize = new Dimension(5, 50);
		Dimension prefSize = new Dimension(5, 50);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, 100);
		rightPanel.add(new Box.Filler(minSize, prefSize, maxSize));
		
		rightPanel.setMinimumSize(new Dimension(200, 100));
		rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		enrolFormPanel.add(rightPanel, BorderLayout.EAST);
		
		wizard1 = new JPanel(new SpringLayout());
		wizard2 = new JPanel(new SpringLayout());
		wizard3 = new JPanel(new SpringLayout());
		wizard4 = new JPanel(new SpringLayout());
		wizard5 = new JPanel(new SpringLayout());
		preview = new JPanel(new SpringLayout());
		
		JLabel secA = new JLabel("SECTION A: PERSONAL DATA", JLabel.CENTER), secA2 = new JLabel("SECTION A: PERSONAL DATA", JLabel.CENTER);
		secA.setFont(new Font("arial", Font.BOLD, 22));
		secA2.setFont(new Font("arial", Font.BOLD, 22));
		
		wizard1.add(new JLabel());
		wizard1.add(secA);
		preview.add(new JLabel());
		preview.add(secA2);
		
		wizard1.add(new JLabel("Title", JLabel.TRAILING));
		cbTitle = new JComboBox(new String[]{"MR", "MRS", "MISS", "DR", "CHIEF"});
		wizard1.add(cbTitle);
		preview.add(new JLabel("Title", JLabel.TRAILING));
		lbTitle = new JLabel();
		preview.add(lbTitle);
		
		wizard1.add(new JLabel("Sur name", JLabel.TRAILING));
		txtSurName = new JTextField(30);
		wizard1.add(txtSurName);
		preview.add(new JLabel("Sur name", JLabel.TRAILING));
		lbSurName = new JLabel();
		preview.add(lbSurName);
		
		wizard1.add(new JLabel("First name", JLabel.TRAILING));
		txtFirstName = new JTextField(30);
		wizard1.add(txtFirstName);
		preview.add(new JLabel("First name", JLabel.TRAILING));
		lbFirstName = new JLabel();
		preview.add(lbFirstName);
		
		wizard1.add(new JLabel("Middle name", JLabel.TRAILING));
		txtMiddleName = new JTextField(30);
		wizard1.add(txtMiddleName);
		preview.add(new JLabel("Middle name", JLabel.TRAILING));
		lbMiddleName = new JLabel();
		preview.add(lbMiddleName);
		
		wizard1.add(new JLabel("Maiden name", JLabel.TRAILING));
		txtMaidenName = new JTextField(30);
		wizard1.add(txtMaidenName);
		preview.add(new JLabel("Maiden name", JLabel.TRAILING));
		lbMaidenName = new JLabel();
		preview.add(lbMaidenName);
		
		wizard1.add(new JLabel("Sex", JLabel.TRAILING));
		cbGender = new JComboBox(new String[]{"MALE", "FEMALE"});
		wizard1.add(cbGender);
		preview.add(new JLabel("Sex", JLabel.TRAILING));
		lbGender = new JLabel();
		preview.add(lbGender);
		
		wizard1.add(new JLabel("Marital Status", JLabel.TRAILING));
		cbMaritalStatus = new JComboBox(new String[]{"SINGLE", "MARRIED", "DIVORCED", "WIDOWED"});
		wizard1.add(cbMaritalStatus);
		preview.add(new JLabel("Marital Status", JLabel.TRAILING));
		lbMaritalStatus = new JLabel();
		preview.add(lbMaritalStatus);
		
		wizard1.add(new JLabel("Date of Birth", JLabel.TRAILING));
		txtYear = new JTextField(4);
		String[] months = new String[12];
		Calendar c = Calendar.getInstance();
		for(int i=0; i<months.length; i++)
		{
			c.set(Calendar.MONTH, i);
			String m = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
			months[i] = m;
		}
		cbMonth = new JComboBox(months);
		String[] days = new String[31];
		for(int i=0; i<days.length; i++)
		{
			days[i] = ""+(i+1);
		}
		cbDay = new JComboBox(days);
		JPanel pDate = new JPanel(new FlowLayout(FlowLayout.LEFT)), pDate2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pDate.add(new JLabel("Year: "));pDate.add(txtYear);
		pDate.add(new JLabel("Month: "));pDate.add(cbMonth);
		pDate.add(new JLabel("Day: "));pDate.add(cbDay);
		wizard1.add(pDate);
		preview.add(new JLabel("Date of Birth", JLabel.TRAILING));
		lbYear = new JLabel();lbDay = new JLabel();lbMonth = new JLabel();
		pDate2.add(new JLabel("Year: "));pDate2.add(lbYear);
		pDate2.add(new JLabel("Month: "));pDate2.add(lbMonth);
		pDate2.add(new JLabel("Day: "));pDate2.add(lbDay);
		preview.add(pDate2);
		
		wizard1.add(new JLabel("Place of Birth", JLabel.TRAILING));
		txtBirthPlace = new JTextField(30);
		wizard1.add(txtBirthPlace);
		preview.add(new JLabel("Place of Birth", JLabel.TRAILING));
		lbBirthPlace = new JLabel();
		preview.add(lbBirthPlace);
		
		wizard1.add(new JLabel("LGA of Origin", JLabel.TRAILING));
		txtLgOrigin = new JTextField(30);
		wizard1.add(txtLgOrigin);
		preview.add(new JLabel("LGA of Origin", JLabel.TRAILING));
		lbLgOrigin = new JLabel();
		preview.add(lbLgOrigin);
		
		wizard1.add(new JLabel("Town", JLabel.TRAILING));
		txtTown = new JTextField(30);
		wizard1.add(txtTown);
		preview.add(new JLabel("Town", JLabel.TRAILING));
		lbTown = new JLabel();
		preview.add(lbTown);
		
		wizard1.add(new JLabel("Religion", JLabel.TRAILING));
		txtReligion = new JTextField(30);
		wizard1.add(txtReligion);
		preview.add(new JLabel("Religion", JLabel.TRAILING));
		lbReligion = new JLabel();
		preview.add(lbReligion);
		
		wizard1.add(new JLabel("State of Origin", JLabel.TRAILING));
		Vector v3 = new Vector();
		if(states != null)
		{
			for(State e : states)
			{
				v3.addElement(e.getName());
			}
		}
		//String[] states = new String[]{"Abia", "Adamawa", "Akwaibom", "Anambra", "Bauchi"};
		cbStateOrigin = new JComboBox(v3);
		wizard1.add(cbStateOrigin);
		preview.add(new JLabel("State of Origin", JLabel.TRAILING));
		lbStateOrigin = new JLabel();
		preview.add(lbStateOrigin);
		
		wizard1.add(new JLabel("Nationality", JLabel.TRAILING));
		txtNationality = new JTextField(30);
		wizard1.add(txtNationality);
		preview.add(new JLabel("Nationality", JLabel.TRAILING));
		lbNationality = new JLabel();
		preview.add(lbNationality);
		
		wizard1.add(new JLabel("Sector", JLabel.TRAILING));
		Vector v = new Vector();
		if(sectors != null)
		{
			for(Sector e : sectors)
			{
				v.addElement(e.getName() + " (" + e.getType().getName() + ")");
			}
		}
		cbSector = new JComboBox(v);
		wizard1.add(cbSector);
		preview.add(new JLabel("Sector", JLabel.TRAILING));
		lbSector = new JLabel();
		preview.add(lbSector);
		
		wizard1.add(new JLabel("State", JLabel.TRAILING));
		Vector v2 = new Vector();
		if(states != null)
		{
			for(State e : states)
			{
				v2.addElement(e.getName());
			}
		}
		cbState = new JComboBox(v2);
		wizard1.add(cbState);
		preview.add(new JLabel("State", JLabel.TRAILING));
		lbState = new JLabel();
		preview.add(lbState);
		
		SpringUtilities.makeCompactGrid(wizard1,
                17, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		
		JLabel secB = new JLabel("SECTION B: CONTACT DETAILS", JLabel.CENTER), secB2 = new JLabel("SECTION B: CONTACT DETAILS", JLabel.CENTER);
		secB.setFont(new Font("arial", Font.BOLD, 22));
		secB2.setFont(new Font("arial", Font.BOLD, 22));
		
		wizard2.add(new JLabel());
		wizard2.add(secB);
		preview.add(new JLabel());
		preview.add(secB2);
		
		wizard2.add(new JLabel("Residential Address", JLabel.TRAILING));
		txtAddr1 = new JTextField(30);
		wizard2.add(txtAddr1);
		preview.add(new JLabel("Residential Address", JLabel.TRAILING));
		lbAddr1 = new JLabel();
		preview.add(lbAddr1);
		
		wizard2.add(new JLabel());
		txtAddr2 = new JTextField(30);
		wizard2.add(txtAddr2);
		preview.add(new JLabel());
		lbAddr2 = new JLabel();
		preview.add(lbAddr2);
		
		wizard2.add(new JLabel("Home Town Address", JLabel.TRAILING));
		txtAddrHm1 = new JTextField(30);
		wizard2.add(txtAddrHm1);
		preview.add(new JLabel("Home Town Address", JLabel.TRAILING));
		lbAddrHm1 = new JLabel();
		preview.add(lbAddrHm1);
		
		wizard2.add(new JLabel());
		txtAddrHm2 = new JTextField(30);
		wizard2.add(txtAddrHm2);
		preview.add(new JLabel());
		lbAddrHm2 = new JLabel();
		preview.add(lbAddrHm2);
		
		wizard2.add(new JLabel("Mobile a.", JLabel.TRAILING));
		txtMobile1 = new JTextField(30);
		wizard2.add(txtMobile1);
		preview.add(new JLabel("Mobile a.", JLabel.TRAILING));
		lbMobile1 = new JLabel();
		preview.add(lbMobile1);
		
		wizard2.add(new JLabel("b.", JLabel.TRAILING));
		txtMobile2 = new JTextField(30);
		wizard2.add(txtMobile2);
		preview.add(new JLabel("b.", JLabel.TRAILING));
		lbMobile2 = new JLabel();
		preview.add(lbMobile2);
		
		wizard2.add(new JLabel("c.", JLabel.TRAILING));
		txtMobile3 = new JTextField(30);
		wizard2.add(txtMobile3);
		preview.add(new JLabel("c.", JLabel.TRAILING));
		lbMobile3 = new JLabel();
		preview.add(lbMobile3);
		
		wizard2.add(new JLabel("Email", JLabel.TRAILING));
		txtEmail = new JTextField(30);
		wizard2.add(txtEmail);
		preview.add(new JLabel("Email", JLabel.TRAILING));
		lbEmail = new JLabel();
		preview.add(lbEmail);
		
		SpringUtilities.makeCompactGrid(wizard2,
                9, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		
		JLabel secC = new JLabel("SECTION C: EMPLOYMENT DETAILS", JLabel.CENTER), secC2 = new JLabel("SECTION C: EMPLOYMENT DETAILS", JLabel.CENTER);
		secC.setFont(new Font("arial", Font.BOLD, 22));
		secC2.setFont(new Font("arial", Font.BOLD, 22));
		
		wizard3.add(new JLabel());
		wizard3.add(secC);
		preview.add(new JLabel());
		preview.add(secC2);
		
		wizard3.add(new JLabel("Grade Level / Step", JLabel.TRAILING));
		txtGradeLvl = new JTextField(30);
		wizard3.add(txtGradeLvl);
		preview.add(new JLabel("Grade Level / Step", JLabel.TRAILING));
		lbGradeLvl = new JLabel();
		preview.add(lbGradeLvl);
		
		wizard3.add(new JLabel("Work Station", JLabel.TRAILING));
		txtWorkStation = new JTextField(30);
		wizard3.add(txtWorkStation);
		preview.add(new JLabel("Work Station", JLabel.TRAILING));
		lbWorkStation = new JLabel();
		preview.add(lbWorkStation);
		
		wizard3.add(new JLabel("NIS", JLabel.TRAILING));
		txtNis = new JTextField(30);
		wizard3.add(txtNis);
		preview.add(new JLabel("NIS", JLabel.TRAILING));
		lbNis = new JLabel();
		preview.add(lbNis);
		
		wizard3.add(new JLabel("Rank", JLabel.TRAILING));
		txtRank = new JTextField(30);
		wizard3.add(txtRank);
		preview.add(new JLabel("Rank", JLabel.TRAILING));
		lbRank = new JLabel();
		preview.add(lbRank);
		
		wizard3.add(new JLabel("Location of Station", JLabel.TRAILING));
		txtStationLocation = new JTextField(30);
		wizard3.add(txtStationLocation);
		preview.add(new JLabel("Location of Station", JLabel.TRAILING));
		lbStationLocation = new JLabel();
		preview.add(lbStationLocation);
		
		wizard3.add(new JLabel("Profession", JLabel.TRAILING));
		txtProfession = new JTextField(30);
		wizard3.add(txtProfession);
		preview.add(new JLabel("Profession", JLabel.TRAILING));
		lbProfession = new JLabel();
		preview.add(lbProfession);
		
		SpringUtilities.makeCompactGrid(wizard3,
                7, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
		
		JLabel secD = new JLabel("SECTION D: FAMILY DATA", JLabel.CENTER), secD22 = new JLabel("SECTION D: FAMILY DATA", JLabel.CENTER);
		secD.setFont(new Font("arial", Font.BOLD, 22));
		secD22.setFont(new Font("arial", Font.BOLD, 22));
		
		JLabel secD2 = new JLabel("RELATIVE INFORMATION", JLabel.CENTER), secD222 = new JLabel("RELATIVE INFORMATION", JLabel.CENTER);
		secD2.setFont(new Font("arial", Font.BOLD, 16));
		secD222.setFont(new Font("arial", Font.BOLD, 16));
		
		wizard4.add(new JLabel());
		wizard4.add(secD);
		
		wizard4.add(new JLabel());
		wizard4.add(secD2);
		
		preview.add(new JLabel());
		preview.add(secD22);
		
		preview.add(new JLabel());
		preview.add(secD222);
		
		wizard4.add(new JLabel("Next of Kin", JLabel.TRAILING));
		JPanel next1Panel = new JPanel(new GridLayout(2, 1)), next1Panel2 = new JPanel(new GridLayout(2, 1));
		next1Panel.add(new JLabel("SURNAME"));
		txtNextSurname = new JTextField(30);
		next1Panel.add(txtNextSurname);
		wizard4.add(next1Panel);
		next1Panel2.add(new JLabel("SURNAME"));
		lbNextSurname = new JLabel();
		next1Panel2.add(lbNextSurname);
		preview.add(new JLabel("Next of Kin", JLabel.TRAILING));
		preview.add(next1Panel2);
		
		wizard4.add(new JLabel());
		JPanel next2Panel = new JPanel(new GridLayout(2, 1)), next2Panel2 = new JPanel(new GridLayout(2, 1));
		next2Panel.add(new JLabel("MIDDLE NAME"));
		txtNextMiddlename = new JTextField(30);
		next2Panel.add(txtNextMiddlename);
		wizard4.add(next2Panel);
		next2Panel2.add(new JLabel("MIDDLE NAME"));
		lbNextMiddlename = new JLabel();
		next2Panel2.add(lbNextMiddlename);
		preview.add(new JLabel());
		preview.add(next2Panel2);
		
		wizard4.add(new JLabel());
		JPanel next3Panel = new JPanel(new GridLayout(2, 1)), next3Panel2 = new JPanel(new GridLayout(2, 1));
		next3Panel.add(new JLabel("FIRST NAME"));
		txtNextFirstname = new JTextField(30);
		next3Panel.add(txtNextFirstname);
		wizard4.add(next3Panel);
		next3Panel2.add(new JLabel("FIRST NAME"));
		lbNextFirstname = new JLabel();
		next3Panel2.add(lbNextFirstname);
		preview.add(new JLabel());
		preview.add(next3Panel2);
		
		wizard4.add(new JLabel("Relationship with Next of Kin", JLabel.TRAILING));
		txtNextRelationship = new JTextField(30);
		wizard4.add(txtNextRelationship);
		preview.add(new JLabel("Relationship with Next of Kin", JLabel.TRAILING));
		lbNextRelationship = new JLabel();
		preview.add(lbNextRelationship);
		
		wizard4.add(new JLabel("Address", JLabel.TRAILING));
		txtNextAddr1 = new JTextField(30);
		wizard4.add(txtNextAddr1);
		preview.add(new JLabel("Address", JLabel.TRAILING));
		lbNextAddr1 = new JLabel();
		preview.add(lbNextAddr1);
		
		wizard4.add(new JLabel());
		txtNextAddr2 = new JTextField(30);
		wizard4.add(txtNextAddr2);
		preview.add(new JLabel());
		lbNextAddr2 = new JLabel();
		preview.add(lbNextAddr2);
		
		wizard4.add(new JLabel("Phone Number", JLabel.TRAILING));
		txtNextMobile = new JTextField(30);
		wizard4.add(txtNextMobile);
		preview.add(new JLabel("Phone Number", JLabel.TRAILING));
		lbNextMobile = new JLabel();
		preview.add(lbNextMobile);
		
		wizard4.add(new JLabel("Number of Children", JLabel.TRAILING));
		cbNextNoOfChild = new JComboBox(new String[] {"0", "1", "2", "3", "4"});
		wizard4.add(cbNextNoOfChild);
		preview.add(new JLabel("Number of Children", JLabel.TRAILING));
		lbNextNoOfChild = new JLabel();
		preview.add(lbNextNoOfChild);
		
		JPanel childrenNamePanel = new JPanel(new SpringLayout()), childrenNamePanel2 = new JPanel(new SpringLayout());
		
		JLabel childNames = new JLabel("NAMES OF CHILDREN", JLabel.CENTER), childNames2 = new JLabel("NAMES OF CHILDREN", JLabel.CENTER);
		childNames.setFont(new Font("arial", Font.BOLD, 14));
		childNames2.setFont(new Font("arial", Font.BOLD, 14));
		
		childrenNamePanel.add(new JLabel());
		childrenNamePanel.add(childNames);
		childrenNamePanel2.add(new JLabel());
		childrenNamePanel2.add(childNames2);
		
		childrenNamePanel.add(new JLabel("a.", JLabel.TRAILING));
		txtChildName1 = new JTextField(30);
		childrenNamePanel.add(txtChildName1);
		childrenNamePanel2.add(new JLabel("a.", JLabel.TRAILING));
		lbChildName1 = new JLabel();
		childrenNamePanel2.add(lbChildName1);
		
		childrenNamePanel.add(new JLabel("b.", JLabel.TRAILING));
		txtChildName2 = new JTextField(30);
		childrenNamePanel.add(txtChildName2);
		childrenNamePanel2.add(new JLabel("b.", JLabel.TRAILING));
		lbChildName2 = new JLabel();
		childrenNamePanel2.add(lbChildName2);
		
		childrenNamePanel.add(new JLabel("c.", JLabel.TRAILING));
		txtChildName3 = new JTextField(30);
		childrenNamePanel.add(txtChildName3);
		childrenNamePanel2.add(new JLabel("c.", JLabel.TRAILING));
		lbChildName3 = new JLabel();
		childrenNamePanel2.add(lbChildName3);
		
		childrenNamePanel.add(new JLabel("d.", JLabel.TRAILING));
		txtChildName4 = new JTextField(30);
		childrenNamePanel.add(txtChildName4);
		childrenNamePanel2.add(new JLabel("d.", JLabel.TRAILING));
		lbChildName4 = new JLabel();
		childrenNamePanel2.add(lbChildName4);
		
		JPanel childrenDOBPanel = new JPanel(new SpringLayout()), childrenDOBPanel2 = new JPanel(new SpringLayout());
		
		JLabel childDOBs = new JLabel("DATE OF BIRTH", JLabel.CENTER), childDOBs2 = new JLabel("DATE OF BIRTH", JLabel.CENTER);
		childDOBs.setFont(new Font("arial", Font.BOLD, 14));
		childDOBs2.setFont(new Font("arial", Font.BOLD, 14));
		
		childrenDOBPanel.add(childDOBs);
		childrenDOBPanel2.add(childDOBs2);
		
		txtChildDOB1 = new JTextField(30);
		childrenDOBPanel.add(txtChildDOB1);
		lbChildDOB1 = new JLabel();
		childrenDOBPanel2.add(lbChildDOB1);
		
		txtChildDOB2 = new JTextField(30);
		childrenDOBPanel.add(txtChildDOB2);
		lbChildDOB2 = new JLabel();
		childrenDOBPanel2.add(lbChildDOB2);
		
		txtChildDOB3 = new JTextField(30);
		childrenDOBPanel.add(txtChildDOB3);
		lbChildDOB3 = new JLabel();
		childrenDOBPanel2.add(lbChildDOB3);
		
		txtChildDOB4 = new JTextField(30);
		childrenDOBPanel.add(txtChildDOB4);
		lbChildDOB4 = new JLabel();
		childrenDOBPanel2.add(lbChildDOB4);
		
		JPanel childrenPanel = new JPanel(new GridLayout(1, 2)), childrenPanel2 = new JPanel(new GridLayout(1, 2));
		childrenPanel.add(childrenNamePanel);
		childrenPanel.add(childrenDOBPanel);
		childrenPanel2.add(childrenNamePanel2);
		childrenPanel2.add(childrenDOBPanel2);
		
		wizard4.add(new JLabel());
		wizard4.add(childrenPanel);
		preview.add(new JLabel());
		preview.add(childrenPanel2);
		
		SpringUtilities.makeCompactGrid(childrenNamePanel, 
				5, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		SpringUtilities.makeCompactGrid(childrenDOBPanel, 
				5, 1, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		SpringUtilities.makeCompactGrid(childrenNamePanel2, 
				5, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		SpringUtilities.makeCompactGrid(childrenDOBPanel2, 
				5, 1, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		SpringUtilities.makeCompactGrid(wizard4, 
				11, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		
		JLabel secE = new JLabel("SECTION E: APPOINTMENT INFORMATION", JLabel.CENTER), secE2 = new JLabel("SECTION E: APPOINTMENT INFORMATION", JLabel.CENTER);
		secE.setFont(new Font("arial", Font.BOLD, 22));
		secE2.setFont(new Font("arial", Font.BOLD, 22));
		
		wizard5.add(new JLabel());
		wizard5.add(secE);
		preview.add(new JLabel());
		preview.add(secE2);
		
		wizard5.add(new JLabel("Date of 1st Appointment: (DD/MM/YYYY)", JLabel.TRAILING));
		txtDoa = new JTextField(30);
		wizard5.add(txtDoa);
		preview.add(new JLabel("Date of 1st Appointment: (DD/MM/YYYY)", JLabel.TRAILING));
		lbDoa = new JLabel();
		preview.add(lbDoa);
		
		wizard5.add(new JLabel("Date of Confirmation: (DD/MM/YYYY)", JLabel.TRAILING));
		txtDoc = new JTextField(30);
		wizard5.add(txtDoc);
		preview.add(new JLabel("Date of Confirmation: (DD/MM/YYYY)", JLabel.TRAILING));
		lbDoc = new JLabel();
		preview.add(lbDoc);
		
		wizard5.add(new JLabel("Method of Entry into Service", JLabel.TRAILING));
		cbMoe = new JComboBox(new String[]{"DIRECT APPOINTMENT", "SECONDMENT", "TRANSFER OF SERVICE"});
		wizard5.add(cbMoe);
		preview.add(new JLabel("Method of Entry into Service", JLabel.TRAILING));
		lbMoe = new JLabel();
		preview.add(lbMoe);
		
		JPanel certPanel = new JPanel(new SpringLayout()), certPanel2 = new JPanel(new SpringLayout());
		
		JLabel certTitle = new JLabel("CERTIFICATE OBTAINED", JLabel.CENTER), certTitle2 = new JLabel("CERTIFICATE OBTAINED", JLabel.CENTER);
		certTitle.setFont(new Font("arial", Font.BOLD, 14));
		certTitle2.setFont(new Font("arial", Font.BOLD, 14));
		
		certPanel.add(new JLabel());
		certPanel.add(certTitle);
		certPanel2.add(new JLabel());
		certPanel2.add(certTitle2);
		
		certPanel.add(new JLabel("a.", JLabel.TRAILING));
		txtCert1 = new JTextField(30);
		certPanel.add(txtCert1);
		certPanel2.add(new JLabel("a.", JLabel.TRAILING));
		lbCert1 = new JLabel();
		certPanel2.add(lbCert1);
		
		certPanel.add(new JLabel("b.", JLabel.TRAILING));
		txtCert2 = new JTextField(30);
		certPanel.add(txtCert2);
		certPanel2.add(new JLabel("b.", JLabel.TRAILING));
		lbCert2 = new JLabel();
		certPanel2.add(lbCert2);
		
		certPanel.add(new JLabel("c.", JLabel.TRAILING));
		txtCert3 = new JTextField(30);
		certPanel.add(txtCert3);
		certPanel2.add(new JLabel("c.", JLabel.TRAILING));
		lbCert3 = new JLabel();
		certPanel2.add(lbCert3);
		
		JPanel certDPanel = new JPanel(new SpringLayout()), certDPanel2 = new JPanel(new SpringLayout());
		
		JLabel certDs = new JLabel("DATE", JLabel.CENTER), certDs2 = new JLabel("DATE", JLabel.CENTER);
		certDs.setFont(new Font("arial", Font.BOLD, 14));
		certDs2.setFont(new Font("arial", Font.BOLD, 14));
		
		certDPanel.add(certDs);
		certDPanel2.add(certDs2);
		
		txtCert1Date = new JTextField(30);
		certDPanel.add(txtCert1Date);
		lbCert1Date = new JLabel();
		certDPanel2.add(lbCert1Date);
		
		txtCert2Date = new JTextField(30);
		certDPanel.add(txtCert2Date);
		lbCert2Date = new JLabel();
		certDPanel2.add(lbCert2Date);
		
		txtCert3Date = new JTextField(30);
		certDPanel.add(txtCert3Date);
		lbCert3Date = new JLabel();
		certDPanel2.add(lbCert3Date);
		
		JPanel certsPanel = new JPanel(new GridLayout(1, 2)), certsPanel2 = new JPanel(new GridLayout(1, 2));
		certsPanel.add(certPanel);
		certsPanel.add(certDPanel);
		certsPanel2.add(certPanel2);
		certsPanel2.add(certDPanel2);
		
		SpringUtilities.makeCompactGrid(certPanel, 
				4, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		SpringUtilities.makeCompactGrid(certDPanel, 
				4, 1, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		SpringUtilities.makeCompactGrid(certPanel2, 
				4, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		SpringUtilities.makeCompactGrid(certDPanel2, 
				4, 1, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		
		wizard5.add(new JLabel("Qualifiction on Entry in to Service with Dates", JLabel.TRAILING));
		wizard5.add(new JLabel());
		preview.add(new JLabel("Qualifiction on Entry in to Service with Dates", JLabel.TRAILING));
		preview.add(new JLabel());
		
		wizard5.add(new JLabel());
		wizard5.add(certsPanel);
		preview.add(new JLabel());
		preview.add(certsPanel2);
		
		JPanel othercertPanel = new JPanel(new SpringLayout()), othercertPanel2 = new JPanel(new SpringLayout());
		
		JLabel othercertTitle = new JLabel("CERTIFICATE OBTAINED", JLabel.CENTER), othercertTitle2 = new JLabel("CERTIFICATE OBTAINED", JLabel.CENTER);
		othercertTitle.setFont(new Font("arial", Font.BOLD, 14));
		othercertTitle2.setFont(new Font("arial", Font.BOLD, 14));
		
		othercertPanel.add(new JLabel());
		othercertPanel.add(othercertTitle);
		othercertPanel2.add(new JLabel());
		othercertPanel2.add(othercertTitle2);
		
		othercertPanel.add(new JLabel("a.", JLabel.TRAILING));
		txtOtherCert1 = new JTextField(30);
		othercertPanel.add(txtOtherCert1);
		othercertPanel2.add(new JLabel("a.", JLabel.TRAILING));
		lbOtherCert1 = new JLabel();
		othercertPanel2.add(lbOtherCert1);
		
		othercertPanel.add(new JLabel("b.", JLabel.TRAILING));
		txtOtherCert2 = new JTextField(30);
		othercertPanel.add(txtOtherCert2);
		othercertPanel2.add(new JLabel("b.", JLabel.TRAILING));
		lbOtherCert2 = new JLabel();
		othercertPanel2.add(lbOtherCert2);
		
		othercertPanel.add(new JLabel("c.", JLabel.TRAILING));
		txtOtherCert3 = new JTextField(30);
		othercertPanel.add(txtOtherCert3);
		othercertPanel2.add(new JLabel("c.", JLabel.TRAILING));
		lbOtherCert3 = new JLabel();
		othercertPanel2.add(lbOtherCert3);
		
		JPanel othercertDPanel = new JPanel(new SpringLayout()), othercertDPanel2 = new JPanel(new SpringLayout());
		
		JLabel othercertDs = new JLabel("DATE", JLabel.CENTER), othercertDs2 = new JLabel("DATE", JLabel.CENTER);
		othercertDs.setFont(new Font("arial", Font.BOLD, 14));
		othercertDs2.setFont(new Font("arial", Font.BOLD, 14));
		
		othercertDPanel.add(othercertDs);
		othercertDPanel2.add(othercertDs2);
		
		txtOtherCert1Date = new JTextField(30);
		othercertDPanel.add(txtOtherCert1Date);
		lbOtherCert1Date = new JLabel();
		othercertDPanel2.add(lbOtherCert1Date);
		
		txtOtherCert2Date = new JTextField(30);
		othercertDPanel.add(txtOtherCert2Date);
		lbOtherCert2Date = new JLabel();
		othercertDPanel2.add(lbOtherCert2Date);
		
		txtOtherCert3Date = new JTextField(30);
		othercertDPanel.add(txtOtherCert3Date);
		lbOtherCert3Date = new JLabel();
		othercertDPanel2.add(lbOtherCert3Date);
		
		SpringUtilities.makeCompactGrid(othercertPanel, 
				4, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		SpringUtilities.makeCompactGrid(othercertDPanel, 
				4, 1, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		SpringUtilities.makeCompactGrid(othercertPanel2, 
				4, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		SpringUtilities.makeCompactGrid(othercertDPanel2, 
				4, 1, //rows, cols
                6, 6,        //initX, initY
                6, 6);
		
		JPanel othercertsPanel = new JPanel(new GridLayout(1, 2)), othercertsPanel2 = new JPanel(new GridLayout(1, 2));
		othercertsPanel.add(othercertPanel);
		othercertsPanel.add(othercertDPanel);
		othercertsPanel2.add(othercertPanel2);
		othercertsPanel2.add(othercertDPanel2);
		
		wizard5.add(new JLabel("Other Qualifiction with Dates", JLabel.TRAILING));
		wizard5.add(new JLabel());
		preview.add(new JLabel("Other Qualifiction with Dates", JLabel.TRAILING));
		preview.add(new JLabel());
		
		wizard5.add(new JLabel());
		wizard5.add(othercertsPanel);
		preview.add(new JLabel());
		preview.add(othercertsPanel2);
		
		wizard5.add(new JLabel("Rank on last promotion with Date", JLabel.TRAILING));
		txtRankLastPro = new JTextField(30);
		wizard5.add(txtRankLastPro);
		preview.add(new JLabel("Rank on last promotion with Date", JLabel.TRAILING));
		lbRankLastPro = new JLabel();
		preview.add(lbRankLastPro);
		
		wizard5.add(new JLabel("Monthly Salary", JLabel.TRAILING));
		txtMonthly = new JTextField(30);
		wizard5.add(txtMonthly);
		preview.add(new JLabel("Monthly Salary", JLabel.TRAILING));
		lbMonthly = new JLabel();
		preview.add(lbMonthly);
		
		wizard5.add(new JLabel("Annual Salary", JLabel.TRAILING));
		txtAnnual = new JTextField(30);
		wizard5.add(txtAnnual);
		preview.add(new JLabel("Annual Salary", JLabel.TRAILING));
		lbAnnual = new JLabel();
		preview.add(lbAnnual);
		
		wizard5.add(new JLabel("Proposed Date of Retirement from Service", JLabel.TRAILING));
		txtRetDate = new JTextField(30);
		wizard5.add(txtRetDate);
		preview.add(new JLabel("Proposed Date of Retirement from Service", JLabel.TRAILING));
		lbRetDate = new JLabel();
		preview.add(lbRetDate);
		
		wizard5.add(new JLabel("Authentication by Head of Unit", JLabel.TRAILING));
		wizard5.add(new JLabel());
		preview.add(new JLabel("Authentication by Head of Unit", JLabel.TRAILING));
		preview.add(new JLabel());
		
		wizard5.add(new JLabel("Name/Rank in full", JLabel.TRAILING));
		txtAuthName = new JTextField(30);
		wizard5.add(txtAuthName);
		preview.add(new JLabel("Name/Rank in full", JLabel.TRAILING));
		lbAuthName = new JLabel();
		preview.add(lbAuthName);
		
		wizard5.add(new JLabel("Date", JLabel.TRAILING));
		txtAuthDate = new JTextField(30);
		wizard5.add(txtAuthDate);
		preview.add(new JLabel("Date", JLabel.TRAILING));
		lbAuthDate = new JLabel();
		preview.add(lbAuthDate);
		
		wizard5.add(new JLabel("Remark", JLabel.TRAILING));
		txtAuthRemark = new JTextField(30);
		wizard5.add(txtAuthRemark);
		preview.add(new JLabel("Remark", JLabel.TRAILING));
		lbAuthRemark = new JLabel();
		preview.add(lbAuthRemark);
		
		wizard5.add(new JLabel("Checked By", JLabel.TRAILING));
		txtCheckedBy = new JTextField(30);
		wizard5.add(txtCheckedBy);
		preview.add(new JLabel("Checked By", JLabel.TRAILING));
		lbCheckedBy = new JLabel();
		preview.add(lbCheckedBy);
		
		//Lay out the panel.
		SpringUtilities.makeCompactGrid(wizard5,
		                                17, 2, //rows, cols
		                                6, 6,        //initX, initY
		                                6, 6);       //xPad, yPad
		
		//Lay out the panel.
		SpringUtilities.makeCompactGrid(preview,
		                                61, 2, //rows, cols
		                                6, 6,        //initX, initY
		                                6, 6);       //xPad, yPad
		
		centerPanel.add(wizard1);
		btnBack.setEnabled(false);
		btnSave.setEnabled(false);
		btnNext.setEnabled(true);
		btnCancelUpdate.setEnabled(false);
		
		enrollScrollPane = new JScrollPane(centerPanel);
		
		enrolFormPanel.add(enrollScrollPane, BorderLayout.CENTER);
		
		JPanel bottomBtnPanel = new JPanel(new BorderLayout());
		
		JPanel saveBtnPanel = new JPanel();
		saveBtnPanel.add(btnSave);
		bottomBtnPanel.add(saveBtnPanel, BorderLayout.CENTER);
		
		JPanel backBtnPanel = new JPanel();
		backBtnPanel.add(btnBack);
		bottomBtnPanel.add(backBtnPanel, BorderLayout.WEST);
		
		JPanel nextBtnPanel = new JPanel();
		nextBtnPanel.add(btnNext);
		nextBtnPanel.add(btnCancelUpdate);
		bottomBtnPanel.add(nextBtnPanel, BorderLayout.EAST);
		
		enrolFormPanel.add(bottomBtnPanel, BorderLayout.SOUTH);
		
		btnBack.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				moveBackward();
			}
		});
		
		btnNext.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				moveForward();
			}
		});
		
		btnCancelUpdate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(JOptionPane.showConfirmDialog(BioDataForm.this, "Are you sure you want to cancel the update?", "Update", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					setData(null);
					clearFields();
					screen = 1;
					switchScreen();
					
					btnCancelUpdate.setEnabled(false);
				}
			}
		});
		
		btnSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				save();
			}
		});
		
		return enrolFormPanel;
	}
	
	private void preview()
	{
		lbState.setText((cbState.getSelectedIndex() >= 0) ? cbState.getSelectedItem().toString() : "");
		lbSector.setText((cbSector.getSelectedIndex() >= 0) ? cbSector.getSelectedItem().toString() : "");
		
		lbTitle.setText((cbTitle.getSelectedIndex() >= 0) ? cbTitle.getSelectedItem().toString() : "");
		lbGender.setText((cbGender.getSelectedIndex() >= 0) ? cbGender.getSelectedItem().toString() : "");
		lbMaritalStatus.setText((cbMaritalStatus.getSelectedIndex() >= 0) ? cbMaritalStatus.getSelectedItem().toString() : "");
		lbStateOrigin.setText((cbStateOrigin.getSelectedIndex() >= 0) ? cbStateOrigin.getSelectedItem().toString() : "");
		
		lbMonth.setText((cbMonth.getSelectedIndex() >= 0) ? cbMonth.getSelectedItem().toString() : "");
		lbDay.setText((cbDay.getSelectedIndex() >= 0) ? cbDay.getSelectedItem().toString() : "");
		
		lbSurName.setText(txtSurName.getText());
		lbFirstName.setText(txtFirstName.getText());
		lbMiddleName.setText(txtMiddleName.getText());
		lbMaidenName.setText(txtMaidenName.getText());
		
		lbBirthPlace.setText(txtBirthPlace.getText());
		lbYear.setText(txtYear.getText());
		
		lbLgOrigin.setText(txtLgOrigin.getText());
		lbTown.setText(txtTown.getText());
		lbReligion.setText(txtReligion.getText());
		lbNationality.setText(txtNationality.getText());
		
		lbAddr1.setText(txtAddr1.getText());
		lbAddr2.setText(txtAddr2.getText());
		lbAddrHm1.setText(txtAddrHm1.getText());
		lbAddrHm2.setText(txtAddrHm2.getText());
		lbMobile1.setText(txtMobile1.getText());
		lbMobile2.setText(txtMobile2.getText());
		lbMobile3.setText(txtMobile3.getText());
		lbEmail.setText(txtEmail.getText());
		
		lbGradeLvl.setText(txtGradeLvl.getText());
		lbWorkStation.setText(txtWorkStation.getText());
		lbNis.setText(txtNis.getText());
		lbRank.setText(txtRank.getText());
		lbStationLocation.setText(txtStationLocation.getText());
		lbProfession.setText(txtProfession.getText());
		
		lbNextSurname.setText(txtNextSurname.getText());
		lbNextMiddlename.setText(txtNextMiddlename.getText());
		lbNextFirstname.setText(txtNextFirstname.getText());
		lbNextRelationship.setText(txtNextRelationship.getText());
		lbNextAddr1.setText(txtNextAddr1.getText());
		lbNextAddr2.setText(txtNextAddr2.getText());
		lbNextMobile.setText(txtNextMobile.getText());
		lbNextNoOfChild.setText((cbNextNoOfChild.getSelectedIndex() >= 0) ? cbNextNoOfChild.getSelectedItem().toString() : "");
		lbChildName1.setText(txtChildName1.getText());
		lbChildName2.setText(txtChildName2.getText());
		
		lbChildName3.setText(txtChildName3.getText());
		lbChildName4.setText(txtChildName4.getText());
		
		lbChildDOB1.setText(txtChildDOB1.getText());
		lbChildDOB2.setText(txtChildDOB2.getText());
		lbChildDOB3.setText(txtChildDOB3.getText());
		lbChildDOB4.setText(txtChildDOB4.getText());
		lbChildName2.setText(txtChildName2.getText());
		lbChildName2.setText(txtChildName2.getText());
		lbChildName2.setText(txtChildName2.getText());
		lbChildName2.setText(txtChildName2.getText());
		
		lbMoe.setText((cbMoe.getSelectedIndex() >= 0) ? cbMoe.getSelectedItem().toString() : "");
		lbDoa.setText(txtDoa.getText());
		lbDoc.setText(txtDoc.getText());
		lbCert1.setText(txtCert1.getText());
		lbCert1Date.setText(txtCert1Date.getText());
		lbCert2.setText(txtCert2.getText());
		lbCert2Date.setText(txtCert2Date.getText());
		lbCert3.setText(txtCert3.getText());
		lbCert3Date.setText(txtCert3Date.getText());
		lbOtherCert1.setText(txtOtherCert1.getText());
		
		lbOtherCert1Date.setText(txtOtherCert1Date.getText());
		lbOtherCert2.setText(txtOtherCert2.getText());
		lbOtherCert2Date.setText(txtOtherCert2Date.getText());
		lbOtherCert3.setText(txtOtherCert3.getText());
		lbOtherCert3Date.setText(txtOtherCert3Date.getText());
		
		lbRankLastPro.setText(txtRankLastPro.getText());
		lbMonthly.setText(txtMonthly.getText());
		lbAnnual.setText(txtAnnual.getText());
		lbRetDate.setText(txtRetDate.getText());
		lbAuthName.setText(txtAuthName.getText());
		lbAuthRemark.setText(txtAuthRemark.getText());
		lbAuthDate.setText(txtAuthDate.getText());
		lbCheckedBy.setText(txtCheckedBy.getText());
	}
	
	private void moveBackward()
	{
		screen -= 1;
		if(screen < 1)
			screen = 1;
		
		switchScreen();
	}
	
	private void moveForward()
	{
		screen += 1;
		if(screen > 6)
			screen = 6;
		
		switchScreen();
	}
	
	private void switchScreen()
	{
		switch(screen)
		{
			case 1:
			{
				centerPanel.removeAll();
				centerPanel.add(wizard1);
				btnBack.setEnabled(false);
				btnSave.setEnabled(false);
				
				btnNext.setText("Next >>|");
				btnNext.setEnabled(true);
				
				repaint();
				break;
			}
			case 2:
			{
				centerPanel.removeAll();
				centerPanel.add(wizard2);
				btnBack.setEnabled(true);
				btnSave.setEnabled(false);
				
				btnNext.setText("Next >>|");
				btnNext.setEnabled(true);
				
				repaint();
				break;
			}
			case 3:
			{
				centerPanel.removeAll();
				centerPanel.add(wizard3);
				btnBack.setEnabled(true);
				btnSave.setEnabled(false);
				
				btnNext.setText("Next >>|");
				btnNext.setEnabled(true);
				
				repaint();
				break;
			}
			case 4:
			{
				centerPanel.removeAll();
				centerPanel.add(wizard4);
				btnBack.setEnabled(true);
				btnSave.setEnabled(false);
				
				btnNext.setText("Next >>|");
				btnNext.setEnabled(true);
				
				repaint();
				break;
			}
			case 5:
			{
				centerPanel.removeAll();
				centerPanel.add(wizard5);
				btnBack.setEnabled(true);
				btnSave.setEnabled(true);
				
				btnNext.setText("Preview");
				btnNext.setEnabled(true);
				
				repaint();
				break;
			}
			case 6:
			{
				preview();
				centerPanel.removeAll();
				centerPanel.add(preview);
				btnBack.setEnabled(true);
				btnSave.setEnabled(true);
				
				btnNext.setEnabled(false);
				
				repaint();
				break;
			}
			default:
			{
				JOptionPane.showMessageDialog(this, "Invalid screen position");
				break;
			}
		}
	}
	
	private JComponent createSearchFormArea()
	{
		searchPanel = new SearchPanel(this);
		
		return searchPanel;
	}
	
	private JComponent createLogArea()
	{
		logTextArea = new JTextArea();
		logTextArea.setEditable(false);
		logTextArea.setLineWrap(true);
		logTextArea.setFont(Font.decode("arial-11"));
		
		logScrollPane = new JScrollPane(logTextArea, 20, 31);
		
		logScrollPane.setPreferredSize(new Dimension(0, 130));
		
		logScrollPane.setAutoscrolls(true);
		
		logScrollPane.setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2), new BevelBorder(1)));
		
		return logScrollPane;
	}
	
	private JComponent createFingerprintViewPanel()
	{
		/* Creates the image panel, where the fingerprints will be displayed */
		imagePanel.setPreferredSize(new Dimension(200, 200));
		
		imagePanel.setSize(150, 200);
		imagePanel.setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2), new BevelBorder(1)));
		
		return imagePanel;
	}
	
	private JComponent createPhotoViewPanel()
	{
		photoViewPanel = new JPanel()
		{
			public void paintComponent(Graphics paramGraphics)
			{
				super.paintComponent(paramGraphics);
				
				if(BioDataForm.this.photoImage != null)
				{
					Insets localInsets = getInsets();
					int i = localInsets.left;
					int j = localInsets.top;
					int k = getWidth() - getInsets().right - getInsets().left;
					int m = getHeight() - getInsets().bottom - getInsets().top;
					
					paramGraphics.drawImage(BioDataForm.this.photoImage, i, j, k, m, null);
				}
			}
			
			@Override
	        public Dimension getPreferredSize() {
	            return getComponentCount() == 0 ? new Dimension(200, 200) : super.getPreferredSize();
	        }
		};
		
		photoViewPanel.setSize(150, 200);
		photoViewPanel.setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2), new BevelBorder(1)));
		
		return photoViewPanel;
	}
	
	public void showFingerprintImage(BiometricImage image)
	{
		if(image != null)
		{
			fingerprintImage = image.getSubimage(0, 0, image.getWidth(), image.getHeight());
			imagePanel.setImage(image);
		}
		else
		{
			fingerprintImage = null;
			imagePanel.setImage(null);
		}
		repaint();
	}
	
	public void showPhotoImage(BufferedImage paramBufferedImage)
	{
		photoImage = paramBufferedImage;
		repaint();
	}
	
	public void setSensorName(final String paramString)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				sensorName.setText(paramString);
			}
		});
	}
	
	public void writeLog(String paramString)
	{
		logTextArea.append(paramString + "\n");
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JScrollBar localJScrollBar = BioDataForm.this.logScrollPane.getVerticalScrollBar();
				localJScrollBar.setValue(localJScrollBar.getMaximum());
			}
		});
	}
	
	private void loadFromFile()
	{
		JFileChooser localJFileChooser = new JFileChooser();
		localJFileChooser.setAcceptAllFileFilterUsed(false);
		
		Iterator localIterator = IIORegistry.getDefaultInstance().getServiceProviders(ImageReaderSpi.class, false);
		while (localIterator.hasNext())
		{
			localJFileChooser.setFileFilter(new ImageFileFilter((ImageReaderSpi)localIterator.next()));
		}
		
		if (localJFileChooser.showOpenDialog(this.rootPane) == 0)
		{
			ImageReaderSpi localImageReaderSpi = (ImageReaderSpi)((ImageFileFilter)localJFileChooser.getFileFilter()).getSpi();
			
			fingerprintSDK.loadPhotoFile(localJFileChooser.getSelectedFile(), localImageReaderSpi);
		}
	}
	
	private class ImageFileFilter extends FileFilter
	{
		private ImageReaderWriterSpi spi;
		
		public ImageFileFilter(ImageReaderWriterSpi arg2)
		{
			this.spi = arg2;
		}
		
		public ImageReaderWriterSpi getSpi()
		{
			return this.spi;
		}
		
		public boolean accept(File paramFile)
		{
			if (paramFile.isDirectory())
			{
				return true;
			}
			
			for(int i = 0; i < this.spi.getFileSuffixes().length; i++)
			{
				if(paramFile.getName().toLowerCase().endsWith(spi.getFileSuffixes()[i].toLowerCase()))
				{
					return true;
				}
			}
			
			return false;
		}
		
		public String getDescription()
		{
			String str = spi.getDescription(Locale.getDefault());
			
			str = str + " (";
			for (int i = 0; i < spi.getFileSuffixes().length; i++)
			{
				str = str + "*." + spi.getFileSuffixes()[i];
				
				if (i < spi.getFileSuffixes().length - 1)
					str = str + ", ";
			}
			str = str + ")";
			return str;
		}
	}
	
	public static void main(String[] paramArrayOfString)
	{
		// TODO: Load this path from a config properties file
		Properties config = new Properties();
		
		try
		{
			config.load(BioDataForm.class.getResourceAsStream("config.properties"));
		}
		catch(Exception ex)
		{}
		
		String str = new File(".").getAbsolutePath();
		if (paramArrayOfString.length > 0)
		{
			str = paramArrayOfString[0];
		}
		
		if(config.containsKey("sdk.native.dir"))
		{
			str = config.getProperty("sdk.native.dir");
			System.out.println("loaded path: " + str + " from config");
		}
		
		BioDataForm b = new BioDataForm();
		
		b.setVisible(true);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		b.setBounds((d.width / 2) - (b.getWidth() / 2), (d.height / 2) - (b.getHeight() / 2), b.getWidth(), b.getHeight());
		
		b.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		//b.loginDialog.setVisible(true);
		
		//b.loginDialog.setBounds((d.width / 2) - (b.loginDialog.getWidth() / 2), (d.height / 2) - (b.loginDialog.getHeight() / 2), b.loginDialog.getWidth(), b.loginDialog.getHeight());
	}

	public DBUtil getDbUtil() {
		return dbUtil;
	}

	public List<State> getStates() {
		return states;
	}

	public List<Sector> getSectors() {
		return sectors;
	}

	public BioData getData() {
		return data;
	}

	public void setData(BioData data) {
		this.data = data;
	}
	
}
