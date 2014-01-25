package com.dexter.mtnodometertracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity
{
	Button loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		loginButton = (Button) findViewById(R.id.btnSignIn);
		loginButton.setOnClickListener(new View.OnClickListener()
		{
            public void onClick(View v)
            {
            	launchSignIn();
            	finish();
            }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    // Handle item selection
	    switch (item.getItemId())
	    {
	    	case R.id.action_settings:
	        {
	        	//Toast.makeText(getBaseContext(), "Under construction!", Toast.LENGTH_SHORT).show();
	        	launchSettings();
	        	return true;
	        }
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	protected void launchSettings()
    {
		Intent settingsActivity = new Intent(this, SettingsActivity.class);
		startActivity(settingsActivity);
    }
	
	/**
     * Launches the LoginActivity activity for the user to sign in.
     */
    protected void launchSignIn()
    {
    	Intent i = new Intent(this, LoginActivity.class);
    	startActivity(i);
    }
}
