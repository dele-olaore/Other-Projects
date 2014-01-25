package com.dexter.biodata;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	
	private BioDataForm dataForm;
	
	public LoginDialog(BioDataForm dataForm)
	{
		super(dataForm, "Login", true);
		this.dataForm = dataForm;
		
		txtUsername = new JTextField();
		txtPassword = new JPasswordField();
		
		JPanel topPanel = new JPanel(new GridLayout(4, 1));
		
		topPanel.add(new JLabel("Username"));
		topPanel.add(txtUsername);
		topPanel.add(new JLabel("Password"));
		topPanel.add(txtPassword);
		
		JButton btnLogin = new JButton("Sign in"), btnExit = new JButton("Exit");
		
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bottomPanel.add(btnLogin);
		bottomPanel.add(btnExit);
		
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				exit();
			}
		});
		
		btnExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				exit();
			}
		});
		
		btnLogin.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});
		
		setSize(250, 150);
		setLayout(new BorderLayout());
		
		add(topPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private void exit()
	{
		if(JOptionPane.showConfirmDialog(LoginDialog.this, "Are you sure you want to Exit?", "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
		{
			dataForm.destroy();
			System.exit(0);
		}
	}
}
