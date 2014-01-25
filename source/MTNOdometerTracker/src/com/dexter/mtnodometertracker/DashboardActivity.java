package com.dexter.mtnodometertracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends Activity
{
	Button signOutBtn;
	Button saveBtn;
	
	private View mDashboardView;
	private View mSyncStatusView;
	private TextView mSyncStatusMessageView;
	
	private SaveTask mAuthTask = null;
	
	private App app = App.app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		mDashboardView = findViewById(R.id.dashboardLayout);
		mSyncStatusView = findViewById(R.id.sync_status);
		mSyncStatusMessageView = (TextView) findViewById(R.id.sync_status_message);
		
		signOutBtn = (Button) findViewById(R.id.signOutBtn);
		signOutBtn.setOnClickListener(new View.OnClickListener()
		{
            public void onClick(View v)
            {
            	launchMainView();
            	finish();
            }
		});
		
		saveBtn = (Button)findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(new View.OnClickListener()
		{
            public void onClick(View v)
            {
            	attemptSave();
            }
		});
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    // Handle item selection
	    switch (item.getItemId())
	    {
	    	/*case R.id.action_sync:
	    	{
	    		// Here we call the web services to sync: get both the states and localgovt data, upload properties filled
	    		attemptSave();
	    		return true;
	    	}*/
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
     * Launches the MainActivity activity for the user to either login again or close app.
     */
    protected void launchMainView()
    {
    	Intent i = new Intent(this, MainActivity.class);
    	startActivity(i);
    }
    
    /**
	 * Attempts to save the values specified by the form.
	 * If there are form errors (missing fields, etc.), the
	 * errors are presented and no actual save attempt is made.
	 */
	public void attemptSave()
	{
		if (mAuthTask != null)
		{
			return;
		}
		
		// Show a progress spinner, and kick off a background task to
		// perform the user login attempt.
		mSyncStatusMessageView.setText("Saving...");
		showProgress(true);
		mAuthTask = new SaveTask();
		mAuthTask.execute((Void) null);
	}
	
	/**
	 * Shows the progress UI and hides the form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show)
	{
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
		{
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mSyncStatusView.setVisibility(View.VISIBLE);
			mSyncStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter()
					{
						@Override
						public void onAnimationEnd(Animator animation)
						{
							mSyncStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			mDashboardView.setVisibility(View.VISIBLE);
			mDashboardView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter()
					{
						@Override
						public void onAnimationEnd(Animator animation)
						{
							mDashboardView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		}
		else
		{
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mSyncStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mDashboardView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous saving task used to save data.
	 */
	public class SaveTask extends AsyncTask<Void, Void, Boolean>
	{
		private String URL = "http://" + app.getRemoteIP() + ":" + app.getRemotePort() + "/fmr/rest/driverRecord?"; // 173.201.44.90:8098
		private String message = "";
		int count = 0;
		
		@Override
		protected Boolean doInBackground(Void... params)
		{
			boolean ret = false;
			try
			{
				// for states...
				mSyncStatusMessageView.post(new Runnable()
	        	{
					@Override
					public void run()
					{
						mSyncStatusMessageView.setText("Saving...");
					}
				});
				
				String initFuelLvl = ((EditText)findViewById(R.id.initFuelLvlText)).getText().toString();
				String quantity = ((EditText)findViewById(R.id.quantityText)).getText().toString();
				String finalFuelLvl = ((EditText)findViewById(R.id.finalFuelLvlText)).getText().toString();
				String odometer = ((EditText)findViewById(R.id.odometerText)).getText().toString();
				String fuelRate = ((EditText)findViewById(R.id.fuelRateText)).getText().toString();
				String cost = ((EditText)findViewById(R.id.costText)).getText().toString();
				String refNum = ((EditText)findViewById(R.id.refNumText)).getText().toString();
				String location = ((EditText)findViewById(R.id.locationText)).getText().toString();
				
				HttpPost post = new HttpPost(URL);
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				
				SimpleDateFormat dtFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				
				nameValuePairs.add(new BasicNameValuePair("initFuelLvl", initFuelLvl));
				nameValuePairs.add(new BasicNameValuePair("quantity", quantity));
				nameValuePairs.add(new BasicNameValuePair("finalFuelLvl", finalFuelLvl));
				nameValuePairs.add(new BasicNameValuePair("odometer", odometer));
				nameValuePairs.add(new BasicNameValuePair("fuelRate", fuelRate));
				nameValuePairs.add(new BasicNameValuePair("cost", cost));
				nameValuePairs.add(new BasicNameValuePair("user_id", app.getUser_id()));
				nameValuePairs.add(new BasicNameValuePair("tranTime", dtFormater.format(new java.util.Date())));
				nameValuePairs.add(new BasicNameValuePair("location", location));
				nameValuePairs.add(new BasicNameValuePair("refNum", refNum));
				
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				JSONObject json = new JSONUtil().getJSON(post);
				if(json != null)
				{
					try
					{
						ret = json.getBoolean("success");
						if(ret)
						{
							message = json.getString("message");
						}
					}
					catch(Exception ex)
					{
						message = "Error: " + ex.getMessage();
					}
				}
				else
				{
					message = "Error: Invalid return from server!";
				}
			}
			catch(Exception ex)
			{
				message = "Severe: " + ex.getMessage();
			}
			return ret;
		}
		
		@Override
		protected void onPostExecute(final Boolean success)
		{
			mAuthTask = null;
			showProgress(false);

			Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
			if(success)
			{
				// display success message here
				((EditText)findViewById(R.id.initFuelLvlText)).setText("");
				((EditText)findViewById(R.id.quantityText)).setText("");
				((EditText)findViewById(R.id.finalFuelLvlText)).setText("");
				((EditText)findViewById(R.id.odometerText)).setText("");
				((EditText)findViewById(R.id.fuelRateText)).setText("");
				((EditText)findViewById(R.id.costText)).setText("");
			}
			else
			{
				// display error message here
			}
		}

		@Override
		protected void onCancelled()
		{
			mAuthTask = null;
			showProgress(false);
		}
	}
}
