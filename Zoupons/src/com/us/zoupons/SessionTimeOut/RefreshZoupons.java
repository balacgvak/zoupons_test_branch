package com.us.zoupons.SessionTimeOut;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

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
					Log.i("Application", "Gone Background");
					//Toast.makeText(context, "Application Gone Background", Toast.LENGTH_SHORT).show();
					//mGetTimeBeforeGoneBackGround = System.currentTimeMillis();
					Editor mUpdatePreference = mLogoutSessionPreferences.edit();
					mUpdatePreference.putBoolean("WakeupApplication", true);
					mUpdatePreference.putLong("TimeBeforeGoneBackGround", System.currentTimeMillis());
					mUpdatePreference.commit();
					//WakeUpApplication = true;
				}
			}
		}

	}

