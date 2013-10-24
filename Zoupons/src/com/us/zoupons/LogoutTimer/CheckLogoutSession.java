package com.us.zoupons.LogoutTimer;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class CheckLogoutSession {

	private AlarmManager mLogoutSessionAlarm;
	private PendingIntent mPendingIntent;
	private Context mContext;

	public CheckLogoutSession(Context context) {
		this.mContext = context;
	}

	public void setLogoutTimerAlarm() {
		Log.i("alarm","set");
		mLogoutSessionAlarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		Intent mLogoutBroadcast =  new Intent(mContext,LogoutReceiver.class);
		//mPendingIntent = PendingIntent.getService(mContext, 100, mLogoutBroadcast,PendingIntent.FLAG_UPDATE_CURRENT);
		mPendingIntent = PendingIntent.getBroadcast(mContext, 100, mLogoutBroadcast, PendingIntent.FLAG_UPDATE_CURRENT);
		// get a Calendar object with current time
		 Calendar cal = Calendar.getInstance();
		 
		 // add 5 minutes to the calendar object
		 cal.add(Calendar.MINUTE, 5);
		 
		mLogoutSessionAlarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), mPendingIntent);
	}

	public void cancelAlarm(){
		if(mLogoutSessionAlarm!=null){
			Log.i("alarm","cancelled");
			mLogoutSessionAlarm.cancel(mPendingIntent);	
		}else{
			Log.i("alarm","not started");
		}
	}
}