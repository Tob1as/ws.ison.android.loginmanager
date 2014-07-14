package ws.ison.android.mainz.wohnheim.loginmanager;

import java.util.TimerTask;
import android.util.Log;

public class ServiceTask extends TimerTask {
	
	// http://developer.android.com/reference/java/util/TimerTask.html
	
	private LoginManagerService lmService;
	
	public ServiceTask(LoginManagerService lmService) {
		this.lmService = lmService;
	}

	@Override
	public void run() {
		Log.i("LoginManager", "ServiceTask: i've been called");

		String username = lmService.getUsername();
		String password = lmService.getPassword();
		
		// CHECK ob im internen Netz (WLAN, IP Loginserver) und eingeloggt, wenn nicht dann logge ein! 134.93.178.2
		if (helper.checkIPreachable("134.93.48.86") && !helper.checkIPreachable("134.93.178.2")){
			//Log.i("LoginManager", "ServiceTask: LOGIN!!!!!!!!!");
			ServiceAsyncTask sat = new ServiceAsyncTask();
			sat.execute(username, password);
		}
		
		// evt. Service stoppen, falls Loginserver nicht erreichbar, weil Nutzer nicht im internen Netz??!
		
	}
}