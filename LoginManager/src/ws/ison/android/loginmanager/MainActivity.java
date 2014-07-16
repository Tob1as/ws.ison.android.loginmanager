package ws.ison.android.loginmanager;

import ws.ison.android.loginmanager.R;
import ws.ison.android.loginmanager.general.Helper;
import ws.ison.android.loginmanager.service.LoginService;
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
	
	// http://developer.android.com/guide/components/activities.html
	
	// variables
	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button buttonSave = (Button) findViewById(R.id.buttonSave);
		Button buttonService = (Button) findViewById(R.id.buttonService);
		final EditText username = (EditText) findViewById(R.id.username);
		final EditText password = (EditText) findViewById(R.id.password);
		
		// http://developer.android.com/guide/topics/ui/settings.html
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		if (!preferences.getString(Helper.USERNAME, "").equals("") && !preferences.getString(Helper.PASSWORD, "").equals("")){
			username.setText(preferences.getString(Helper.USERNAME, ""));
			password.setText(preferences.getString(Helper.PASSWORD, ""));
			Log.i("LoginManager-MainActivity", "Set USERNAME '"+preferences.getString(Helper.USERNAME, "")+"' and PASSWORD into the EditText!");
		}
		
		// Button: save
		buttonSave.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	
		    	// TODO use strings.xml				
		    	if (!username.getText().toString().equals("") && !password.getText().toString().equals("")){
			    	Editor prefEditor = preferences.edit();
					prefEditor.putString(Helper.USERNAME, username.getText().toString());
					prefEditor.putString(Helper.PASSWORD, password.getText().toString());
					prefEditor.commit();
					
					Log.i("LoginManager-MainActivity", "Save USERNAME '"+username+"' and PASSWORD!");
			    	Toast.makeText(getApplicationContext(), R.string.account_data_save, Toast.LENGTH_SHORT).show();
		    	} else {
		    		Toast.makeText(getApplicationContext(), R.string.account_data_enter, Toast.LENGTH_SHORT).show();
		    	}
		    }
		});
		
		// Button: Service
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
	
	@Override
	public void onBackPressed() {
		//Log.i("LoginManager", "onBackPressed");
		finish();
	    super.onBackPressed();
	}
	
	/*
	 * Service start/stop
	 * http://www.jm-blog.de/service-entwickeln-fur-android-tutorial/
	 */
	private void service(){
		final Intent service = new Intent(MainActivity.this, LoginService.class);
		if (!preferences.getString(Helper.USERNAME, "").equals("") && !preferences.getString(Helper.PASSWORD, "").equals("") && !isServiceRunning()) {
			startService(service);
			Log.i("LoginManager-MainActivity", "Service enable!");
			Toast.makeText(getApplicationContext(), R.string.service_enable, Toast.LENGTH_SHORT).show();
		} else if (isServiceRunning()){
			stopService(service);
			Log.i("LoginManager-MainActivity", "Service disable!");
			Toast.makeText(getApplicationContext(), R.string.service_disable, Toast.LENGTH_SHORT).show();
		} else if (preferences.getString(Helper.USERNAME, "").equals("") || preferences.getString(Helper.PASSWORD, "").equals("")){
			Log.i("LoginManager-MainActivity", "Enter the account data!");
			Toast.makeText(getApplicationContext(), R.string.account_data_enter, Toast.LENGTH_SHORT).show();
		}
	}
	
	/*
	 * Check Service is running
	 * http://stackoverflow.com/questions/13819890/compile-error-on-method-to-check-if-service-is-running
	 */
	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (LoginService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
}