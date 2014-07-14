package ws.ison.android.mainz.wohnheim.loginmanager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class ServiceAsyncTask extends AsyncTask<String, Integer, Double>{

	// http://developer.android.com/reference/android/os/AsyncTask.html
	
	@Override
	protected Double doInBackground(String... params) {
		// TODO Auto-generated method stub
		String username = params[0];
		String password = params[1];
		//Log.i("LoginManager", "ServiceAsyncTask: Username: " +username+" Password: " +password);
		Log.i("LoginManager", "ServiceAsyncTask: LOGIN wird aufgerufen!");
		
		// http://sarangasl.blogspot.de/2011/06/android-login-screen-using-httpclient.html
        
		// Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        /* login.php returns true if username and password is equal to saranga */
        HttpPost httppost = new HttpPost("https://login.wohnheim.uni-mainz.de/cgi-bin/login-cgi");
		
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("user", username));
            nameValuePairs.add(new BasicNameValuePair("pass", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
            // Execute HTTP Post Request
            httpclient.execute(httppost);
            
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public void postData(String valueIWantToSend) {
		// Create a new HttpClient and Post Header
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		// Status
	}
	
	@Override
	protected void onPostExecute(Double result) {
		super.onPostExecute(result);
		//Log.i("LoginManager", "ServiceAsyncTask: onPostExecute");
	}
	
}
