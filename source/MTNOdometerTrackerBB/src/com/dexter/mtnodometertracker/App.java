package com.dexter.mtnodometertracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class App extends UiApplication
{
	public static final String APP_TITLE = "MTN Driver's Odometer Tracker";
	
	private boolean isPermissioGranted;
	
	private String user_id;
	private String fullname;
	
	private String remoteIP;
	private String remotePort;
	
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.
        App theApp = new App();       
        theApp.enterEventDispatcher();
    }
    
    /**
     * Creates a new App object
     */
    public App()
    {
    	checkPermissions();
    	
    	remoteIP = "173.201.44.90";
    	remotePort = "8098";
    	
        // Push a screen onto the UI stack for rendering.
        pushScreen(new HomeScreen(this));
    }
    
    /**
     * This method showcases the ability to check the current permissions for
     * the application. If the permissions are insufficient, the user will be
     * prompted to increase the level of permissions. You may want to restrict
     * permissions for the ApplicationPermissionsDemo.cod module beforehand in
     * order to demonstrate this sample effectively. This can be done in
     * Options/Advanced Options/Applications/(menu)Modules.Highlight
     * 'ApplicationPermissionsDemo' in the Modules list and select 'Edit
     * Permissions' from the menu.
     */
    public void checkPermissions()
    {
        // Capture the current state of permissions and check against the requirements
        ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
        ApplicationPermissions original = apm.getApplicationPermissions();

        // Set up and attach a reason provider
        AppPermissionReasonProvider csrp = new AppPermissionReasonProvider();
        apm.addReasonProvider(ApplicationDescriptor.currentApplicationDescriptor(), csrp);

        if(original.getPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION) == ApplicationPermissions.VALUE_ALLOW &&
           original.getPermission(ApplicationPermissions.PERMISSION_WIFI) == ApplicationPermissions.VALUE_ALLOW &&
           original.getPermission(ApplicationPermissions.PERMISSION_INTERNET) == ApplicationPermissions.VALUE_ALLOW)
        {
            // All of the necessary permissions are currently available
        	setPermissioGranted(true);
        	return;
        }

        // Create a permission request for each of the permissions your application
        // needs. Note that you do not want to list all of the possible permission
        // values since that provides little value for the application or the user.  
        // Please only request the permissions needed for your application.
        ApplicationPermissions permRequest = new ApplicationPermissions();
        permRequest.addPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION);
        permRequest.addPermission(ApplicationPermissions.PERMISSION_WIFI);
        permRequest.addPermission(ApplicationPermissions.PERMISSION_INTERNET);

        boolean acceptance = ApplicationPermissionsManager.getInstance().invokePermissionsRequest(permRequest);

        if(acceptance)
        {
            // User has accepted all of the permissions
        	setPermissioGranted(true);
        }
        else
        {
        	original = apm.getApplicationPermissions();
        	if(original.getPermission(ApplicationPermissions.PERMISSION_WIFI) != ApplicationPermissions.VALUE_ALLOW || original.getPermission(ApplicationPermissions.PERMISSION_INTERNET) != ApplicationPermissions.VALUE_ALLOW)
        	{
        		setPermissioGranted(false);
        	}
        	else
        	{
        		// other permission are not really needed
        		setPermissioGranted(true);
        	}
        	
            // The user has only accepted some or none of the permissions 
            // requested. In this sample, we will not perform any additional 
            // actions based on this information. However, there are several 
            // scenarios where this information could be used. For example,
            // if the user denied networking capabilities then the application 
            // could disable that functionality if it was not core to the 
            // operation of the application.
        	
        }
    }

	public boolean isPermissioGranted() {
		return isPermissioGranted;
	}

	public void setPermissioGranted(boolean isPermissioGranted) {
		this.isPermissioGranted = isPermissioGranted;
	}
	
	/**
     * Prevent the save dialog from being displayed
     * 
     * @see net.rim.device.api.ui.container.MainScreen#onSavePrompt()
     */
    public boolean onSavePrompt()
    {
        return true;
    }
    
	/**
     * Presents a dialog to the user with a given message
     * @param message The text to display
     */
    public static void errorDialog(final String message)
    {
        UiApplication.getUiApplication().invokeLater(new Runnable()
        {
            public void run()
            {
                Dialog.alert(message);
            }
        });
    }
    
    public static JSONObject getJSON(String url, String requestJSON) throws IOException, JSONException
    {
    	HttpConnection c = (HttpConnection)Connector.open(url);
    	
        // Set the request method and headers
        c.setRequestMethod(HttpConnection.POST);
        c.setRequestProperty("Content-Type", "application/json");

        // Getting the output stream may flush the headers
        OutputStream os = c.openOutputStream();
        os.write(requestJSON.getBytes());
        os.flush(); // Optional, getResponseCode will flush

        // Getting the response code will open the connection,
        // send the request, and read the HTTP response headers.
        // The headers are stored until requested.
        int rc = c.getResponseCode();
        if(rc != HttpConnection.HTTP_OK)
        {
            throw new IOException("HTTP response code: " + rc);
        }
        else
        {
        	InputStream in = c.openInputStream();
        	ByteArrayOutputStream out = new ByteArrayOutputStream();
        	int line = -1;
        	while((line = in.read()) > -1)
        	{
        		out.write(line);
        	}
        	in.close();
        	String responseJSON = out.toString();
        	return new JSONObject(responseJSON);
        }
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
