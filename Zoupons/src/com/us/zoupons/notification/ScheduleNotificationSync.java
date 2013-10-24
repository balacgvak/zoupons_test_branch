package com.us.zoupons.notification;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ScheduleNotificationSync {

	private Context mContext;
	private AlarmManager mRecurringAlarm;
	private PendingIntent mPendingIntent;
	
	public ScheduleNotificationSync(Context context) {
		this.mContext = context;
	}
	
	public void setRecurringAlarm() {
		mRecurringAlarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		Intent mServiceCall_intent = new Intent(mContext,NotificationService.class);
		mPendingIntent = PendingIntent.getService(mContext, 100, mServiceCall_intent,PendingIntent.FLAG_UPDATE_CURRENT);
		mRecurringAlarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),30000,mPendingIntent );
	}
	
	public void cancelAlarm(){
		mRecurringAlarm.cancel(mPendingIntent);
	}

}
