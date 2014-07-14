package ws.ison.android.mainz.wohnheim.loginmanager;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class LoginManagerService extends Service {

	private String username;
	private String password;
	
	private Timer timer = new Timer();
	private int delay = 5000; // 1000 = 1sec, 5000 = 5sec
	private int period = 15000; // 60000 = 60sec (900000 = 15min)
	
	private SharedPreferences preferences;
	private NotificationManager mNotificationManager;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//Toast.makeText(getApplicationContext(), "Service wurde gestartet", Toast.LENGTH_LONG).show();
		Log.i("LoginManager", "Service wurde gestartet!");
	
		// Timer
    	TimerTask task = new ServiceTask(this);
    	timer.schedule(task, delay, period);
    	
    	
    	mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	showNotification(false, 1, R.string.service_started);
	}

	@Override
	public void onDestroy() {
		timer.cancel();
		mNotificationManager.cancelAll();
		//Toast.makeText(getApplicationContext(), "Service wurde beendet", Toast.LENGTH_LONG).show();
		Log.i("LoginManager", "Service wurde beendet!");
		super.onDestroy();
	}
	
	@Override 
	public int onStartCommand(Intent intent, int flags, int startId) { 
		Log.i("LoginManager", "Service: onStartCommand wird aufgerufen!");
		
		// http://developer.android.com/guide/topics/ui/settings.html
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		this.username = preferences.getString(helper.USERNAME, "");
		Log.i("LoginManager", "Service USERNAME: "+username);
		this.password = preferences.getString(helper.PASSWORD, "");
		//Log.i("LoginManager", "Service PASSWORD: "+password);

		return START_STICKY;
	}
	
	
	/*
	 * Notification
	 * http://android-er.blogspot.de/2013/06/start-activity-once-notification-clicked.html
	 * http://www.javacodegeeks.com/2013/08/android-notification-with-sound-and-icon-tutorial.html
	 */
		
	private void showNotification(boolean nTemporary, int nID, String notificationText) {
		
		if (nTemporary == true) {			
			// define sound URI, the sound to be played when there's a notification
			Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			
			// notify
			Notification noti = new Notification.Builder(this)
					.setContentTitle(getText(R.string.app_name))
					.setContentText(notificationText)
					.setSound(soundUri)
					.setAutoCancel(false)
					.setOngoing(true)
					.setSmallIcon(R.drawable.ic_launcher).build();
			mNotificationManager.notify(nID, noti);
		}
		
		if (nTemporary == false) {
			// link notify with KlimaActivity
			Intent myIntent = new Intent(this, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(
					this, 
					0, 
					myIntent, 
					Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// notify
			Notification noti = new Notification.Builder(this)
				.setContentTitle(getText(R.string.app_name))
				.setContentText(notificationText)
				.setContentIntent(pendingIntent)
				.setAutoCancel(false)
				.setOngoing(true)
				.setSmallIcon(R.drawable.ic_launcher).build();
			mNotificationManager.notify(nID, noti);
		}
	}
	
	// wrapper (example: Text from string.xml)
	private void showNotification(boolean nTemporary, int nID, int ressourceId) {
		showNotification(nTemporary, nID,  getText(ressourceId).toString());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
