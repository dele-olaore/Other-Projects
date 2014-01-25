package com.dexter.mtnodometertracker;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.io.URI;
import net.rim.device.api.io.messaging.BlockingSenderDestination;
import net.rim.device.api.io.messaging.Context;
import net.rim.device.api.io.messaging.DestinationFactory;
import net.rim.device.api.io.messaging.Message;
import net.rim.device.api.io.parser.json.JSONMessageProcessor;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.component.StandardTitleBar;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.FlowFieldManager;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.MainScreen;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class HomeScreen extends MainScreen
{
	TextField txtUsername;
	PasswordEditField txtPassword;
	ButtonField btnLogin;
	
	private App app;
	
    /**
     * Creates a new MainScreen object
     */
    public HomeScreen(App app)
    {
    	super(MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR);
    	
    	this.app = app;
    	
    	// Set the displayed title of the screen
        StandardTitleBar myTitleBar = new StandardTitleBar()
        .addIcon("img/icon.png")
        .addTitle(App.APP_TITLE)
        .addClock()
        .addNotifications()
        .addSignalIndicator();
        
        myTitleBar.setPropertyValue(StandardTitleBar.PROPERTY_BATTERY_VISIBILITY,
        StandardTitleBar.BATTERY_VISIBLE_LOW_OR_CHARGING);
        setTitleBar(myTitleBar);
        
        txtUsername = new TextField();
        txtPassword = new PasswordEditField();
        
        GridFieldManager g1 = new GridFieldManager(2, 2, GridFieldManager.AUTO_SIZE);
		g1.add(new LabelField("Username")
		{
			public int getPreferredWidth()
			{
				return 150;
			}
		});
		g1.add(txtUsername);
		g1.add(new LabelField("Password")
		{
			public int getPreferredWidth()
			{
				return 150;
			}
		});
		g1.add(txtPassword);
		
		add(g1);
		
		FlowFieldManager flowStatus = new FlowFieldManager(FlowFieldManager.FIELD_HCENTER);
		
		btnLogin = new ButtonField("Sign In", ButtonField.FIELD_HCENTER);
		btnLogin.setCommand(new Command(new CommandHandler()
        {
			public void execute(ReadOnlyCommandMetadata metadata, Object context)
			{
				Thread t = new Thread(new Runnable()
				{
					public void run()
	                {
						doSignIn();
	                }
				});
				t.start();
			}
		}));
		flowStatus.add(btnLogin);
		
		setStatus(flowStatus);
    }
    
    private String doSignIn()
    {
    	String ret = "";
    	
    	String URL = "http://" + app.getRemoteIP() + ":" + app.getRemotePort() + "/fmr/rest/authenticate?";
    	
    	/*BlockingSenderDestination bsd = null;
        try
        {
            bsd = (BlockingSenderDestination)
                        DestinationFactory.getSenderDestination
                            ("MTNOdometerTrackerBB", URI.create(URL));
            if(bsd == null)
            {
                bsd =
                  DestinationFactory.createBlockingSenderDestination
                      (new Context("MTNOdometerTrackerBB"),
                       URI.create(URL), new JSONMessageProcessor());
            }
            Message request = bsd.createByteMessage();
            request.setMessageProperty("username", txtUsername.getText());
            request.setMessageProperty("password", txtPassword.getText());
            
            Message response = bsd.sendReceive();
            JSONObject responseJSON = (JSONObject)response.getObjectPayload();
            if(responseJSON != null)
    		{
    			boolean success = responseJSON.getBoolean("success");
    			if(success)
    			{
    				app.setUser_id(responseJSON.getString("user_id"));
    				app.setFullname(responseJSON.getString("fullname"));
    				
    				app.invokeLater(new Runnable()
    				{
    					public void run() 
                        {
    						app.pushScreen(new FormScreen(app));
    						close();
                        }
    				});
    			}
    			else
    			{
    				String message = responseJSON.getString("message");
    				App.errorDialog(message);
    			}
    		}
        }
        catch(Exception ex)
        {
        	App.errorDialog("Severe: " + ex.getMessage());
        }*/
    	
    	JSONObject requestJSON = new JSONObject();
    	try
    	{
			requestJSON.put("username", txtUsername.getText());
			requestJSON.put("password", txtPassword.getText());
    	}
    	catch (JSONException e)
    	{
    		e.printStackTrace();
		}
    	
    	try
    	{
    		JSONObject responseJSON = App.getJSON(URL, requestJSON.toString());
    		if(responseJSON != null)
    		{
    			boolean success = responseJSON.getBoolean("success");
    			if(success)
    			{
    				app.setUser_id(responseJSON.getString("user_id"));
    				app.setFullname(responseJSON.getString("fullname"));
    				
    				app.invokeLater(new Runnable()
    				{
    					public void run() 
                        {
    						app.pushScreen(new FormScreen(app));
    						close();
                        }
    				});
    			}
    			else
    			{
    				String message = responseJSON.getString("message");
    				App.errorDialog(message);
    			}
    		}
    	}
    	catch(Exception ex)
    	{
    		App.errorDialog("Severe: " + ex.getMessage());
    	}
    	
    	return ret;
    }
}
