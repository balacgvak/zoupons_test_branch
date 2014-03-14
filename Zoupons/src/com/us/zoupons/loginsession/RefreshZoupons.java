package com.us.zoupons.loginsession;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * Generic class to save the current time when app goes background, useful for validating user session 
 *
 */

public class RefreshZoupons {
	// To Check the application gone background
	private SharedPreferences mLogoutSessionPreferences;

	public void isApplicationGoneBackground(Context context){
		mLogoutSessionPreferences = context.getSharedPreferences("LogoutSessionPreferences", Context.MODE_PRIVATE);
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> mRunningTaskInfo = mActivityManager.getRunningTasks(1);
		if(!mRunningTaskInfo.isEmpty()){
			ComponentName mComponentName = mRunningTaskInfo.get(0).topActivity;
			if(!mComponentName.getPackageName().equals(context.getPackageName())){
				// To save session values in Preference
				Editor mUpdatePreference = mLogoutSessionPreferences.edit();
				mUpdatePreference.putBoolean("WakeupApplication", true);
				mUpdatePreference.putLong("TimeBeforeGoneBackGround", System.currentTimeMillis());
				mUpdatePreference.commit();
			}
		}
	}

}

