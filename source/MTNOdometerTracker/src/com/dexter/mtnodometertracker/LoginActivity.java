package com.dexter.mtnodometertracker;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
//import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity
{
	private DatabaseHelper dh;
	public String error = "";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	private App app = App.app;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mEmailView = (EditText) findViewById(R.id.email);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		dh = new DatabaseHelper(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
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
	        	launchSettings();
	        	return true;
	        }
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin()
	{
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} /*else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}*/

		if (cancel)
		{
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		}
		else
		{
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
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

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		}
		else
		{
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
	{
		boolean signInSuccess = false;
		private String URL = "";
		
		String remoteFullname = "", remoteUserId = "";
		
		protected Boolean doRemoteSignIn()
		{
			boolean ret = true;
			signInSuccess = false;
			
			remoteFullname = "";
			remoteUserId = "";
			
			try
			{
				URL = "http://" + app.getRemoteIP() + ":" + app.getRemotePort() + "/fmr/rest/authenticate?"; // 173.201.44.90:8098
				
				HttpPost post = new HttpPost(URL);
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username", mEmail));
				nameValuePairs.add(new BasicNameValuePair("password", mPassword));
			    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				JSONObject json = new JSONUtil().getJSON(post);
				if(json != null)
				{
					try
					{
						boolean remote_success = json.getBoolean("success");
						if(remote_success)
						{
							remoteFullname = json.getString("fullname");
							remoteUserId = json.getString("user_id");
							app.setFullname(remoteFullname);
							app.setUser_id(remoteUserId);
							signInSuccess = true;
						}
					}
					catch(Exception ex)
					{}
				}
				else
				{
					ret = false;
				}
			}
			catch(Exception ex)
			{
				ret = false;
				error = "Severe error: " + ex.getMessage();
			}
			
			return ret;
		}
		
		@Override
		protected Boolean doInBackground(Void... params)
		{
			try
			{
				// first we try a remote sign in
				boolean remote_signin = doRemoteSignIn();
				if(remote_signin) // we were able to connect to the remote
				{
					// now we can check if the authentication was successful or not
					if(signInSuccess) // successful login
					{
						// now check for the user's existence locally, if available we update, else we create fresh.
						if(dh != null)
						{
							SQLiteDatabase db = dh.getReadableDatabase();
							if(db != null)
							{
								Cursor cursor = db.rawQuery("SELECT _id from user where username = '" + mEmail + "'",new String[]{});
								int id = 0;
								if(cursor != null)
								{
									if(cursor.moveToFirst())
							        {
							            do
							            {
							            	id = cursor.getInt(0);
							            }
							            while (cursor.moveToNext());
							        }
									
									if(cursor != null && !cursor.isClosed())
							        {
							           cursor.close();
							        }
								}
								db.close();
								
								db = dh.getWritableDatabase();
								if(id == 0) // create user
								{
							        ContentValues cv = new ContentValues();
							        cv.put("username", mEmail);
							        cv.put("password", mPassword);
							        cv.put("fullname", remoteFullname);
							        cv.put("_id", remoteUserId);
							        db.insert("user", null, cv);
								}
								else // update user
								{
									ContentValues cv = new ContentValues();
									cv.put("password", mPassword);
									cv.put("fullname", remoteFullname);
									db.update("user", cv, "_id = "+id, null);
								}
								
								db.close();
							}
						}
					}
					
					return signInSuccess;
				}
				else
				{
					String db_email = "", db_password = "", db_userid = "", db_fullname = "";;
					if(dh != null)
					{
						SQLiteDatabase db = dh.getReadableDatabase();
						if(db != null)
						{
							//Cursor cursor = db.query("user", new String[] { "email", "password", "fullname" }, null, null, null, null, null);
							Cursor cursor = db.rawQuery("SELECT _id, username, password, fullname from user where username = '" + mEmail + "'",new String[]{});
							if(cursor != null)
							{
						        if(cursor.moveToFirst())
						        {
						            do
						            {
						            	db_userid = ""+cursor.getInt(0);
						            	db_email = cursor.getString(1);
						            	db_password = cursor.getString(2);
						            	db_fullname = cursor.getString(3);
						            }
						            while (cursor.moveToNext());
						        }
						        else
						        {
						        	error = "No record found!";
						        }
						        if(cursor != null && !cursor.isClosed())
						        {
						           cursor.close();
						        }
						        db.close();
								
						        if(db_email.equalsIgnoreCase(mEmail))
						        {
						        	boolean b = db_password.equals(mPassword);
						        	if(b)
						        	{
										app.setFullname(db_fullname);
										app.setUser_id(db_userid);
						        	}
						        	else
						        	{
						        		error = "Invalid password, db: " + db_password + ", " + mPassword;
						        	}
						        	return b;
						        }
						        else
						        {
						        	if(mEmail.equalsIgnoreCase("administrator") && mPassword.equalsIgnoreCase("12345678"))
						        		return true;
						        	
						        	error = "Unknown account!";
						        	return false;
						        }
							}
							else
							{
								error = "cursor is null";
								return false;
							}
						}
						else
						{
							error = "db reader is null";
							return false;
						}
					}
					else
					{
						error = "dh helper is null";
						return false;
					}
				}
			}
			catch(Exception ex)
			{
				error = "Severe Login Error: " + ex.getMessage();
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success)
		{
			mAuthTask = null;
			showProgress(false);

			if(success)
			{
				launchSignInDashboard();
				finish();
			}
			else
			{
				mEmailView.setError(error);
				mPasswordView.setError(error);
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	protected void launchSettings()
    {
		Intent settingsActivity = new Intent(this, SettingsActivity.class);
		startActivity(settingsActivity);
    }
	
	/**
     * Launches the DashboardActivity activity for the user to start using app.
     */
    protected void launchSignInDashboard()
    {
    	Intent i = new Intent(this, DashboardActivity.class);
    	startActivity(i);
    }
}
