package com.dexter.mtnodometertracker;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class App extends Application
{
	private String user_id;
	private String fullname;
	
	private String remoteIP;
	private String remotePort;
	
	public static App app;
	
	public App()
	{
		this.app = this;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		// Get the xml/preferences.xml preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        remoteIP = prefs.getString("remoteServerIP", "173.201.44.90");
        remotePort = prefs.getString("remoteServerPort", "8098");
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getRemoteIP() {
		return remoteIP;
	}

	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	public String getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
	}
	
}
