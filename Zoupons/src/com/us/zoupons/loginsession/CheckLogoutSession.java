package com.us.zoupons.loginsession;

import java.util.Calendar;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


/**
 * 
 * Generic class to set alarm for user session
 *
 */

public class CheckLogoutSession {

	private AlarmManager mLogoutSessionAlarm;
	private PendingIntent mPendingIntent;
	private Context mContext;

	public CheckLogoutSession(Context context) {
		this.mContext = context;
	}

	public void setLogoutTimerAlarm() {
		mLogoutSessionAlarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		Intent mLogoutBroadcast =  new Intent(mContext,LogoutReceiver.class);
		mPendingIntent = PendingIntent.getBroadcast(mContext, 100, mLogoutBroadcast, PendingIntent.FLAG_UPDATE_CURRENT);
		// get a Calendar object with current time
		Calendar cal = Calendar.getInstance();
		// add 5 minutes to the calendar object
		cal.add(Calendar.MINUTE, 5);
		//cal.add(Calendar.SECOND, 20);
		mLogoutSessionAlarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), mPendingIntent);
	}

	public void cancelAlarm(){
		if(mLogoutSessionAlarm!=null){
			mLogoutSessionAlarm.cancel(mPendingIntent);	
			mPendingIntent.cancel();
		}
	}
}