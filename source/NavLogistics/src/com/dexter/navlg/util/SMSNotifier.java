package com.dexter.navlg.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import com.vanso.sxmp.Account;
import com.vanso.sxmp.MobileAddress;
import com.vanso.sxmp.Response;
import com.vanso.sxmp.SubmitRequest;
import com.vanso.sxmp.SxmpErrorCode;
import com.vanso.sxmp.SxmpSender;

public class SMSNotifier
{
	public static final String API_KEY = "ecd89db4";
    public static final String API_SECRET = "12068eba";
    
    public static final String SMS_FROM = "NAVY-LG";
    
    public static void SendSMS3(String SMS_TO, String SMS_TEXT)
    {
    	//final String server = "somewhere.ontheinter.net";

        URL url = null;
        try {
            url = new URL("https://rest.nexmo.com/sms/json");
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        HttpURLConnection urlConn = null;
        try {
            // URL connection channel.
            urlConn = (HttpURLConnection) url.openConnection();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Let the run-time system (RTS) know that we want input.
        urlConn.setDoInput (true);

        // Let the RTS know that we want to do output.
        urlConn.setDoOutput (true);

        // No caching, we want the real thing.
        urlConn.setUseCaches (false);

        try {
            urlConn.setRequestMethod("POST");
        } catch (ProtocolException ex) {
            ex.printStackTrace();
        }

        try {
            urlConn.connect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        DataOutputStream output = null;
        DataInputStream input = null;

        try {
            output = new DataOutputStream(urlConn.getOutputStream());
        } catch (IOException ex) {
        	ex.printStackTrace();
        }

        // Specify the content type if needed.
        //urlConn.setRequestProperty("Content-Type",
        //  "application/x-www-form-urlencoded");

        // Construct the POST data.
        // String SMS_TEXT_ENCD = URLEncoder.encode(SMS_TEXT);
    	// String SMS_FROM_ENCD = URLEncoder.encode(SMS_FROM);
        // ?api_key=" + API_KEY + "&api_secret=" + API_SECRET + "&from=" + SMS_FROM_ENCD + "&to=" + SMS_TO + "&text=" + SMS_TEXT_ENCD
        String content =
          "api_key=" + URLEncoder.encode(API_KEY) +
          "&api_secret=" + URLEncoder.encode(API_SECRET) + 
          "&from=" + URLEncoder.encode(SMS_FROM) + 
          "&to=" + URLEncoder.encode(SMS_TO) + 
          "&text=" + URLEncoder.encode(SMS_TEXT);

        // Send the request data.
        try {
            output.writeBytes(content);
            output.flush();
            output.close();
            System.out.println("sms sent");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
     // Get response data.
        String str = null;
        try {
            input = new DataInputStream (urlConn.getInputStream());
            while (null != ((str = input.readLine()))) {
                System.out.println(str);
            }
            input.close ();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
	
	public static boolean SendSMS(String mobile, String message)
	{
		boolean ret = false;
		
		try
		{
			SubmitRequest submitRequest = new SubmitRequest();
			SxmpSender ss = new SxmpSender("http://sxmp.gw1.vanso.com");        
			submitRequest.setDestinationAddress(new MobileAddress(MobileAddress.Type.INTERNATIONAL, mobile)); // Number in International format - + sign, country code without leading 0
			submitRequest.setAccount(new Account("222.444.2221", "mTtNFoYd")); // For testing, account Id = 222.444.2221, Password = mTtNFoYd       
			submitRequest.setText(message);
			//submitRequest.setMessageType(new FlashOption());
			submitRequest.setSourceAddress(new MobileAddress(MobileAddress.Type.ALPHANUMERIC, "Notification"));
			
			Response response = ss.sendSMS(submitRequest);
			if(response.getErrorCode().intValue() == SxmpErrorCode.OK.getIntValue())
			{
				ret = true;
				System.out.println("Message sent");
			}
			else
			{
				ret = false;
				System.out.println("Message not sent");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			ret = false;
		}
		
		return ret;
	}
}
