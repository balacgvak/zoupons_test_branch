package com.us.zoupons.SessionTimeOut;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.LogoutSession.LogoutSessionTask;

public class CheckUserSession {
	
	private Context mContext;
	
	public CheckUserSession(Context context) {
		mContext = context;
    }
	
	public void checkIfSessionExpires(){
		SharedPreferences mLogoutPreferences = mContext.getSharedPreferences("LogoutSessionPreferences", Context.MODE_PRIVATE);
		boolean mIsWakeUpApplication = mLogoutPreferences.getBoolean("WakeupApplication", false);
		long mTimeWhenAppGoesBackgound = mLogoutPreferences.getLong("TimeBeforeGoneBackGround", 0);
        if(mIsWakeUpApplication){
        	Editor mEditPreferences = mLogoutPreferences.edit();
        	mEditPreferences.putBoolean("WakeupApplication", false);
        	mEditPreferences.commit();
			//RefreshZoupons.WakeUpApplication = false;
			long mCurrentTimeInMill = System.currentTimeMillis() - mTimeWhenAppGoesBackgound;
			if(mCurrentTimeInMill>300000){
				//Toast.makeText(getApplicationContext(), "60 Second Gone",Toast.LENGTH_SHORT).show();
				// AsyncTask to call the logout webservice to end the login session
        				new LogoutSessionTask(mContext).execute();
				Intent loginIntent= new Intent(mContext,ZouponsLogin.class);
				loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(loginIntent);
			}
		}
	}

}
