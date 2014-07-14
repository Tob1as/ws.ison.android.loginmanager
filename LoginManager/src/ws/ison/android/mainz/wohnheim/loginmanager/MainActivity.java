package ws.ison.android.mainz.wohnheim.loginmanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String username;
	private String password;
	
	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button buttonSave = (Button) findViewById(R.id.buttonSave);
		Button buttonService = (Button) findViewById(R.id.buttonService);
		final EditText usernameTextField = (EditText) findViewById(R.id.username);
		final EditText passwordTextField = (EditText) findViewById(R.id.password);
		
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		if (!preferences.getString(helper.USERNAME, "").equals("") && !preferences.getString(helper.PASSWORD, "").equals("")){
			usernameTextField.setText(preferences.getString(helper.USERNAME, ""));
			passwordTextField.setText(preferences.getString(helper.PASSWORD, ""));
		}
		
		buttonSave.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	username = usernameTextField.getText().toString();
		    	password = passwordTextField.getText().toString();
		    	//Log.i("LoginManager", "SAVE: " + username);
				
		    	Editor prefEditor = preferences.edit();
				prefEditor.putString(helper.USERNAME, username);
				prefEditor.putString(helper.PASSWORD, password);
				prefEditor.commit();
				
		    	Toast.makeText(getApplicationContext(), "Save!", Toast.LENGTH_SHORT).show();
		    }
		});

		buttonService.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	service();
		    }
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.service){
			service();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void saveAccountData(){
		Editor prefEditor = preferences.edit();
		prefEditor.putString(helper.USERNAME, username);
		prefEditor.putString(helper.PASSWORD, password);
		prefEditor.commit();
	}
	
	private void service(){
		// http://www.jm-blog.de/service-entwickeln-fur-android-tutorial/
		final Intent service = new Intent(MainActivity.this, LoginManagerService.class);
		if (!preferences.getString(helper.USERNAME, "").equals("") && !preferences.getString(helper.PASSWORD, "").equals("") && !isServiceRunning()) {
			startService(service);
			Log.i("LoginManager", "LoginManager enable!");
			Toast.makeText(getApplicationContext(), "LoginManager enable!", Toast.LENGTH_SHORT).show();
		} else if (isServiceRunning()){
			stopService(service);
			Log.i("LoginManager", "LoginManager disable!");
			Toast.makeText(getApplicationContext(), "LoginManager disable!", Toast.LENGTH_SHORT).show();
		} else if (!isServiceRunning()){
			Log.i("LoginManager", "Enter Login-Account!");
			Toast.makeText(getApplicationContext(), "Enter Login-Account!", Toast.LENGTH_SHORT).show();
		}
	}
	
	/*
	 * Check Service is running
	 * http://stackoverflow.com/questions/13819890/compile-error-on-method-to-check-if-service-is-running
	 */
	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (LoginManagerService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
