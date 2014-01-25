package com.dexter.mtnodometertracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtil
{
	public JSONUtil()
	{}
	
	public JSONObject getJSON(HttpPost post) throws Exception
	{
		JSONObject ret = null;
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(post);
		StatusLine statusLine = response.getStatusLine();
		if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.getEntity().writeTo(out);
			out.close();
			String responseString = out.toString();
			try
			{
				ret = new JSONObject(responseString);
			}
			catch(Exception ex)
			{}
		}
		else
		{
			//Close the connection.
			response.getEntity().getContent().close();
			throw new IOException(statusLine.getReasonPhrase());
		}
		
		return ret;
	}
	
	public JSONObject getJSON(String url) throws Exception
	{
		JSONObject ret = null;
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response = httpclient.execute(post);
		StatusLine statusLine = response.getStatusLine();
		if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.getEntity().writeTo(out);
			out.close();
			String responseString = out.toString();
			try
			{
				ret = new JSONObject(responseString);
			}
			catch(Exception ex)
			{}
		}
		else
		{
			//Close the connection.
			response.getEntity().getContent().close();
			throw new IOException(statusLine.getReasonPhrase());
		}
		
		return ret;
	}
	
	public JSONArray getJSONArray(HttpPost post) throws Exception
	{
		JSONArray ret = null;
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(post);
		StatusLine statusLine = response.getStatusLine();
		if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.getEntity().writeTo(out);
			out.close();
			String responseString = out.toString();
			try
			{
				ret = new JSONArray(responseString);
			}
			catch(Exception ex)
			{}
		}
		else
		{
			//Close the connection.
			response.getEntity().getContent().close();
			throw new IOException(statusLine.getReasonPhrase());
		}
		
		return ret;
	}
	
	public JSONArray getJSONArray(String url) throws Exception
	{
		JSONArray ret = null;
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response = httpclient.execute(post);
		StatusLine statusLine = response.getStatusLine();
		if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.getEntity().writeTo(out);
			out.close();
			String responseString = out.toString();
			try
			{
				ret = new JSONArray(responseString);
			}
			catch(Exception ex)
			{}
		}
		else
		{
			//Close the connection.
			response.getEntity().getContent().close();
			throw new IOException(statusLine.getReasonPhrase());
		}
		
		return ret;
	}
}
