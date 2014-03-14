package com.us.zoupons;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.mobilepay.NormalPaymentAsyncTask;
import com.us.zoupons.storeowner.homepage.StoreOwner_HomePage;

/**
 * 
 * Class to listen click event for map image in zoupons header
 *
 */

public class LoginChoiceTabBarImage {

	private Context mContext;
	private ImageView mTabBarLoginChoice;
	private String mTAG="LoginChoiceTabBarImage";

	public LoginChoiceTabBarImage(Context context,ImageView loginchoice){
		this.mTabBarLoginChoice=loginchoice;
		this.mContext=context;
	}

	// Check for whether to show or hide loginchoice image
	public void setTabBarLoginChoiceImageVisibility(){
		// To get user type from preference file
		SharedPreferences mPrefs = mContext.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		String mUser_type = mPrefs.getString("user_type", "");
		String user_login_type = mPrefs.getString("user_login_type", "");
		if((mUser_type.equalsIgnoreCase("store_owner") || mUser_type.equalsIgnoreCase("store_employee")) && !user_login_type.equalsIgnoreCase("Guest")){
			this.mTabBarLoginChoice.setVisibility(View.VISIBLE);
			this.mTabBarLoginChoice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					//To cancel the QR Code timer thread
					if(NormalPaymentAsyncTask.mCountDownTimer!=null){
						NormalPaymentAsyncTask.mCountDownTimer.cancel();
						NormalPaymentAsyncTask.mCountDownTimer = null;
						Log.i(mTAG,"Timer Stopped Successfully");
					}
					// To cancel Rewards advertisement task
					if(MainMenuActivity.mTimer!=null){
						MainMenuActivity.mTimer.cancel();
						MainMenuActivity.mTimer = null;
						Log.i(mTAG,"Rewards Timer Stopped Successfully");
					}
					// To cancel chat timer in communications 
					if(MainMenuActivity.mCommunicationTimer!=null){
						MainMenuActivity.mCommunicationTimer.cancel();
						MainMenuActivity.mCommunicationTimer=null;
					}if(MainMenuActivity.mCommunicationTimerTask!=null){
						MainMenuActivity.mCommunicationTimerTask.cancel();
						MainMenuActivity.mCommunicationTimerTask=null;
					}
                    // To launch store home page
					Intent login_intent = new Intent(mContext,StoreOwner_HomePage.class);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					mContext.startActivity(login_intent);
				}
			});
		}else{
			this.mTabBarLoginChoice.setVisibility(View.GONE);
		}

	}
}
