package ws.ison.android.loginmanager.general;

import java.net.InetAddress;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

public class Helper {
	
	// variables
	public static String USERNAME = "username";
	public static String PASSWORD = "password";
	static Context context;
	
	/*
	 * get current ssid
	 * http://stackoverflow.com/questions/8811315/how-to-get-current-wifi-connection-info-in-android
	 */
	private static String getCurrentSsid(Context context) {
		String ssid = null;
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo.isConnected()) {
			final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
			if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
				ssid = connectionInfo.getSSID();
			}
		}
		return ssid;
	}
	
	/*
	 * compare wlan ssid
	 */
	public static boolean compareWLAN(String wlan){
		String ssid = "";
		ssid = getCurrentSsid(context);
		Log.i("LoginManager-Helper", "WLAN SSID: Compare current "+ssid+" with "+wlan);
		
		String wlan1 = wlan;
		String wlan2 = "\""+wlan+"\"";
		
		if (ssid != null && (ssid.equals(wlan1) || ssid.equals(wlan2))) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * check IP/Host reachable
	 * http://developer.android.com/reference/java/net/InetAddress.html
	 * http://stackoverflow.com/questions/16441620/android-inetaddress-getbynameip-isreachabletimeout-always-returns-false
	 * http://stackoverflow.com/questions/11506321/java-code-to-ping-an-ip-address
	 * http://www.tutorials.de/threads/java-ping-absetzen-und-rueckgabe-einlesen.255782/
	 */
	public static boolean checkHostReachable(String host){
		boolean reachable = false;
		try {
			reachable = InetAddress.getByName(host).isReachable(5000);
			Log.i("LoginManager-Helper", "Host "+host+" is reachable "+reachable);
		} catch (Exception e) {
			//e.printStackTrace();
			Log.e("LoginManager-Helper", "Host check failed!");
        }
		return reachable;
	}
}