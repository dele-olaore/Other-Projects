package com.dexter.mtnodometertracker;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.widget.TextView;

public class SettingsActivity extends PreferenceActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		try
		{
			addPreferencesFromResource(R.xml.preferences);
		}
		catch(Exception ex)
		{
			setContentView(R.layout.activity_settings);
			TextView errorTV = (TextView)findViewById(R.id.errorTV);
			errorTV.setText(ex.getMessage());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
