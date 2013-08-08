package com.example.mapssamplefor247.host;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;

import com.example.mapssamplefor247.utils.HelperTool;
import com.example.mapssamplefor247.utils.Log;

public class HttpPostHandler  {

	private static HttpPostHandler instance = null;
	
	int timeoutConnection = 180000, timeoutSocket = 180000;
	
	private Handler timerHandler;
	private Runnable timerRunnable;
	
	private CountDownTimer timer;
	
	public static HttpPostHandler getInstance()
	{
		if (instance == null )
		{
			instance = new HttpPostHandler();
		}
		
		return instance;
	}
	
	public static String postData(String url, List<NameValuePair> data) {
		//http://stackoverflow.com/questions/2938502/sending-post-data-in-android
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	    try {
	    	httppost.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
	    	
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	        return readStream(response.getEntity().getContent());
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	    return null;
	}
	public static String doHTTPRequest (String url){
		
		try {
		  URL url2 = new URL(url);
		  HttpURLConnection con = (HttpURLConnection) url2
		    .openConnection();
		  return readStream(con
				  .getInputStream());
		  } catch (Exception e) {
		  e.printStackTrace();
		}
		return null;
	}
	public static Bitmap doHTTPImageRequest(String url){
		try {
		  URL url2 = new URL(url);
		  HttpURLConnection con = (HttpURLConnection) url2
		    .openConnection();
		  return BitmapFactory.decodeStream(con.getInputStream());
		} catch (Exception e) {
		  e.printStackTrace();
		}
		return null;
	}
	private static String readStream(InputStream in) {
	  BufferedReader reader = null;
	  StringBuilder sb = new StringBuilder();
	  
	  
	  try {
	    reader = new BufferedReader(new InputStreamReader(in));
	    String line = "";
	    while ((line = reader.readLine()) != null) {
	      System.out.println(line);
	      sb.append(line);
	    }
	  } catch (IOException e) {
	    e.printStackTrace();
	  } finally {
	    if (reader != null) {
	      try {
	    	  reader.close();
	      } catch (IOException e) {
	    	  e.printStackTrace();
	      }
	    }
	  }
	  return sb.toString();
	} 
	
}