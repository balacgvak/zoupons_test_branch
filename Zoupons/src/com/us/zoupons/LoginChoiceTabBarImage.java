package com.us.zoupons;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.storeowner.HomePage.StoreOwner_HomePage;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;

public class LoginChoiceTabBarImage {

	Context mContext;
	ImageView mTabBarLoginChoice;
	String TAG="LoginChoiceTabBarImage";

	public LoginChoiceTabBarImage(Context context,ImageView loginchoice){
		this.mTabBarLoginChoice=loginchoice;
		this.mContext=context;
	}

	public void setTabBarLoginChoiceImageVisibility(){
		if(UserDetails.mUserType.equalsIgnoreCase("store_owner") || UserDetails.mUserType.equalsIgnoreCase("store_employee")){
			this.mTabBarLoginChoice.setVisibility(View.VISIBLE);

			this.mTabBarLoginChoice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					//To cancel the QR Code timer thread
					if(NormalPaymentAsyncTask.mCountDownTimer!=null){
						NormalPaymentAsyncTask.mCountDownTimer.cancel();
						NormalPaymentAsyncTask.mCountDownTimer = null;
						Log.i(TAG,"Timer Stopped Successfully");
					}
					// To cancel Rewards advertisement task
					if(MainMenuActivity.mTimer!=null){
						MainMenuActivity.mTimer.cancel();
						MainMenuActivity.mTimer = null;
						Log.i(TAG,"Rewards Timer Stopped Successfully");
					}

					if(MainMenuActivity.mCommunicationTimer!=null){
						MainMenuActivity.mCommunicationTimer.cancel();
						MainMenuActivity.mCommunicationTimer=null;
					}if(MainMenuActivity.mCommunicationTimerTask!=null){
						MainMenuActivity.mCommunicationTimerTask.cancel();
						MainMenuActivity.mCommunicationTimerTask=null;
					}

					Intent login_intent = new Intent(mContext,StoreOwner_HomePage.class);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					mContext.startActivity(login_intent);
				}
			});
		}else{
			this.mTabBarLoginChoice.setVisibility(View.GONE);
		}

	}
}
