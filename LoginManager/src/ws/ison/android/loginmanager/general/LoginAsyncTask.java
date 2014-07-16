package ws.ison.android.loginmanager.general;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class LoginAsyncTask extends AsyncTask<String, Integer, Double>{

	// http://developer.android.com/reference/android/os/AsyncTask.html
	
	@Override
	protected Double doInBackground(String... params) {
		
		String username = params[0];
		String password = params[1];
		//Log.i("LoginManager-ServiceAsyncTask", "Read USERNAME "+username+" and PASSWORD!");
		
		/*
		 * HTTP Client
		 * http://sarangasl.blogspot.de/2011/06/android-login-screen-using-httpclient.html
		 */
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://login.wohnheim.uni-mainz.de/cgi-bin/login-cgi");
		
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("user", username));
            nameValuePairs.add(new BasicNameValuePair("pass", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
            // Execute HTTP Post Request
			Log.i("LoginManager-ServiceAsyncTask", "Login is called!");
            httpclient.execute(httppost);
             
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
			Log.e("LoginManager-ServiceAsyncTask", "UnsupportedEncodingException!");
		} catch (ClientProtocolException e) {
			//e.printStackTrace();
			Log.e("LoginManager-ServiceAsyncTask", "ClientProtocolException!");
		} catch (IOException e) {
			//e.printStackTrace();
			Log.e("LoginManager-ServiceAsyncTask", "IOException!");
		}
		
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		//Log.i("LoginManager-ServiceAsyncTask", "onProgressUpdate");
		// Status
	}
	
	@Override
	protected void onPostExecute(Double result) {
		super.onPostExecute(result);
		//Log.i("LoginManager-ServiceAsyncTask", "onPostExecute");
		// Result
	}
	
}