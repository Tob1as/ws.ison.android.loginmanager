package ws.ison.android.loginmanager.service;

import java.util.Timer;
import java.util.TimerTask;
import ws.ison.android.loginmanager.MainActivity;
import ws.ison.android.loginmanager.R;
import ws.ison.android.loginmanager.general.Helper;
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

public class LoginService extends Service {

	// http://developer.android.com/guide/components/services.html
	// http://www.jm-blog.de/service-entwickeln-fur-android-tutorial/
	// http://www.mkyong.com/java/jdk-timer-scheduler-example/
	
	// variables
	private String username;
	private String password;
	
	private Timer timer = new Timer();
	private int delay = 5000; // 1000 = 1sec, 5000 = 5sec
	private int period = 30000; // 60000 = 60sec (900000 = 15min)
	
	private SharedPreferences preferences;
	private NotificationManager mNotificationManager;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//Toast.makeText(getApplicationContext(), "Service start!", Toast.LENGTH_LONG).show();
		Log.i("LoginManager-Service", "Service start!");
	
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
		//Toast.makeText(getApplicationContext(), "Service stop!", Toast.LENGTH_LONG).show();
		Log.i("LoginManager-Service", "Service stop!");
		super.onDestroy();
	}
	
	@Override 
	public int onStartCommand(Intent intent, int flags, int startId) { 
		
		// http://developer.android.com/guide/topics/ui/settings.html
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		this.username = preferences.getString(Helper.USERNAME, "");
		this.password = preferences.getString(Helper.PASSWORD, "");
		Log.i("LoginManager-Service", "Read USERNAME '"+username+"' and PASSWORD from settings and write into variables!");

		return START_STICKY;
	}
	
	/*
	 * Notification
	 * http://developer.android.com/guide/topics/ui/notifiers/notifications.html
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
			// link notify with Activity
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
