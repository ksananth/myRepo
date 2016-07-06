package com.college.virtualattendancesystem.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.college.collegeAttendancemanagement.Config;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncT extends AsyncTask<Void, Void, Void> {
	String responseServer;
	String url;
	String flag;
	JSONObject jsonReq;
	ResponseCallback callback;
	ProgressDialog pd;
	Context ctx;
	
	public AsyncT(Context ctx,ResponseCallback callback, String url,JSONObject jsonReq,String flag) {
		this.url=url;
		this.jsonReq=jsonReq;
		this.callback=callback;
		this.flag=flag;
		this.ctx=ctx;
    } 
	
    @Override
    protected Void doInBackground(Void... voids) {
        //HttpClient httpclient = new DefaultHttpClient();
        //HttpPost httppost = new HttpPost(this.url);

        try {
        	System.out.println("url--------------->"+this.url);
        	System.out.println("jsonReq--------------->"+this.jsonReq);
            

            //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            //nameValuePairs.add(new BasicNameValuePair("req", this.jsonReq.toString()));

            //Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());

          // Use UrlEncodedFormEntity to send in proper format which we need
            //httppost.setEntity(new StringEntity(this.jsonReq.toString()));

            // Execute HTTP Post Request
            //HttpResponse response = httpclient.execute(httppost);
            
            //Log.e("response", "response -----" + responseServer);
            
            URL url;
            try {
            	
                url = new URL(this.url);
                
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("invalid url: " + this.url);
            }
            
            
            
            String body = jsonReq.toString();
            
            Log.v(Config.TAG, "Posting '" + body + "' to " + url);
            
            byte[] bytes = body.getBytes();
            
            HttpURLConnection conn = null;
            try {
            	
            	Log.e("URL", "> " + url);
            	
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setFixedLengthStreamingMode(bytes.length);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type",
                        "application/json;charset=UTF-8");
                // post the request
                OutputStream out = conn.getOutputStream();
                out.write(bytes);
                out.close();
                
                // handle the response
                int status = conn.getResponseCode();
                InputStream inputStream = conn.getInputStream();
                responseServer = getStringFromInputStream(inputStream);
                
                // If response is not success
                if (status != 200) {
                	
                  throw new IOException("Post failed with error code " + status);
                }
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    protected void onPreExecute() {
    	pd = new ProgressDialog(ctx);
    	pd.setMessage("Logging in ....");
    	pd.show();
    
    	super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        callback.callbackCall(responseServer,flag);
        pd.dismiss();
       // txt.setText(responseServer);
    }
    
    String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
    
    

}



