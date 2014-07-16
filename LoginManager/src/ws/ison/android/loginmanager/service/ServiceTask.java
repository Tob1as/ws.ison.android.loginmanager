package ws.ison.android.loginmanager.service;

import java.util.TimerTask;
import ws.ison.android.loginmanager.general.Helper;
import ws.ison.android.loginmanager.general.LoginAsyncTask;
import android.util.Log;

public class ServiceTask extends TimerTask {
	
	// http://developer.android.com/reference/java/util/TimerTask.html
	
	private LoginService lmService;
	
	public ServiceTask(LoginService lmService) {
		this.lmService = lmService;
	}

	@Override
	public void run() {
		Log.i("LoginManager-ServiceTask", "I've been called");

		String username = lmService.getUsername();
		String password = lmService.getPassword();
		
		// check if login-server reachable and no connection to outside, then login
		if (Helper.checkHostReachable("login.wohnheim.uni-mainz.de") && !Helper.checkHostReachable("www.uni-mainz.de")){
			//Log.i("LoginManager-ServiceTask", "Call ServiceAsyncTask");
			LoginAsyncTask sat = new LoginAsyncTask();
			sat.execute(username, password);
		}	
	}
	
}