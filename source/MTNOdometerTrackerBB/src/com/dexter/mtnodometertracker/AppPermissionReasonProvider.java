package com.dexter.mtnodometertracker;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ReasonProvider;

public class AppPermissionReasonProvider implements ReasonProvider
{
	/**
	 * @see net.rim.device.api.applicationcontrol.ReasonProvider#getMessage(int)
	 */
	public String getMessage(int permissionID) 
	{        
	    // General message for other permissions
	    String message = "MTN Driver's Odometer Tracker recieved permissionID: " + permissionID;
	    
	    // Set specific messages for specific permission IDs
	    switch (permissionID)
	    {
	        case ApplicationPermissions.PERMISSION_INPUT_SIMULATION:
	            message = "MTN Driver's Odometer Tracker needs to have access to input simulation to function properly"; break;
	            
	        case ApplicationPermissions.PERMISSION_WIFI:
	            message = "MTN Driver's Odometer Tracker needs to have access to your wifi connections to function properly"; break;
	        
	        case ApplicationPermissions.PERMISSION_INTERNET:
	            message = "MTN Driver's Odometer Tracker needs to have access to your internet connections to function properly"; break;
	    }
	    
	    return message;
	}
}
