package com.us.zoupons.LogoutTimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;

public class LogoutReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("logout receiver", "successfully logged out");
		// AsyncTask to call the logout webservice to end the login session
		new LogoutSessionTask(context).execute();
		Intent loginIntent= new Intent(context,ZouponsLogin.class);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(loginIntent);
		// To destruct qr code timer 
		if(NormalPaymentAsyncTask.mCountDownTimer!=null){
			NormalPaymentAsyncTask.mCountDownTimer.cancel();
			NormalPaymentAsyncTask.mCountDownTimer = null;
			Log.i("logout receiver","Timer Stopped Successfully");
		}
	}
}
