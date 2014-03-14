package com.us.zoupons.notification;

import java.util.Calendar;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.us.zoupons.backgroundservices.NotificationService;

/**
 * 
 * Generic class to set alarm for 30 seconds which is used to fetch notification from server
 *
 */

public class ScheduleNotificationSync {

	private Context mContext;
	private AlarmManager mRecurringAlarm;
	private PendingIntent mPendingIntent;
	private String mNotify_to_type="";

	public ScheduleNotificationSync(Context context,String notify_to_type) {
		this.mContext = context;
		this.mNotify_to_type = notify_to_type;
	}

	/*to set repeating alam to fetch new notification message*/
	public void setRecurringAlarm() {
		try{
			mRecurringAlarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
			Intent mServiceCall_intent = new Intent(mContext,NotificationService.class);
			mServiceCall_intent.putExtra("notify_to_type", mNotify_to_type);
			mPendingIntent = PendingIntent.getService(mContext, 100, mServiceCall_intent,PendingIntent.FLAG_UPDATE_CURRENT);
			mRecurringAlarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),30000,mPendingIntent);
			//mRecurringAlarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),60000,mPendingIntent);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/* To cancel Alarm */
	public void cancelAlarm(){
		mRecurringAlarm.cancel(mPendingIntent);
		if(isMyServiceRunning(mContext)){ // To stop notification service
			Intent mIntent = new Intent(mContext,NotificationService.class);
			mContext.stopService(mIntent);
			ManageNotificationWindow.mNotitificationAdapter = null;
		}
	}

	// To query OS to check whether notifcation service running
	private boolean isMyServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (NotificationService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}
