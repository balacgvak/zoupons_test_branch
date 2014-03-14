package com.us.zoupons.loginsession;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * 
 * Receiver which listens for user login session expiration
 *
 */

public class LogoutReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// AsyncTask to call the logout webservice to end the login session
		new LogoutSessionTask(context,"FromTimerSession").execute();
	}
}
