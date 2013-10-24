package com.us.zoupons.rewards;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;

public class TermsAndConditions extends Activity{

	Button mBack;
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	TextView mZouponsTermsAndConditions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.termsconditions);
		mZouponsTermsAndConditions=(TextView) findViewById(R.id.mTermsConditionContext);
		CheckStoreNameTask mTermsAndConditionsTask = new CheckStoreNameTask(TermsAndConditions.this,mZouponsTermsAndConditions);
		mTermsAndConditionsTask.execute("TermsConditions");
		mBack = (Button) findViewById(R.id.mBack);
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(TermsAndConditions.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}
	
	@Override
	protected void onResume() {
		new CheckUserSession(TermsAndConditions.this).checkIfSessionExpires();
		
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(TermsAndConditions.this);
		mLogoutSession.setLogoutTimerAlarm();
				
		super.onResume();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
}
