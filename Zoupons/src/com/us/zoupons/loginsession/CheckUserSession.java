package com.us.zoupons.loginsession;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 
 * Generic class to validate user session for duration of 5 mins.
 *
 */

public class CheckUserSession {

	private Activity mContext;

	public CheckUserSession(Activity context) {
		mContext = context;
	}

	public void checkIfSessionExpires(){
		// To get session values from preferences
		SharedPreferences mLogoutPreferences = mContext.getSharedPreferences("LogoutSessionPreferences", Context.MODE_PRIVATE);
		boolean mIsWakeUpApplication = mLogoutPreferences.getBoolean("WakeupApplication", false);
		long mTimeWhenAppGoesBackgound = mLogoutPreferences.getLong("TimeBeforeGoneBackGround", 0 );
		if(mIsWakeUpApplication){
			Editor mEditPreferences = mLogoutPreferences.edit();
			mEditPreferences.putBoolean("WakeupApplication", false);
			mEditPreferences.commit();
			long mCurrentTimeInMill = System.currentTimeMillis() - mTimeWhenAppGoesBackgound;
			if(mCurrentTimeInMill>300000){ // If time difference greater than 5 Minutes
			//if(mCurrentTimeInMill>30000){ // If time difference greater than 30 seconds
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(mContext,"FromTimerSession").execute();
			}
		}
	}

}
