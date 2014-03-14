package com.us.zoupons;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.mobilepay.NormalPaymentAsyncTask;
import com.us.zoupons.shopper.home.ShopperHomePage;

/**
 * Class to listen click event for Home button in Zoupons Header 
 */

public class HeaderHomeClickListener implements OnClickListener{

	private Context mCtx;
	private String TAG="HeaderHomeClickListener";

	public HeaderHomeClickListener(Context context){
		super();
		this.mCtx=context;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zoupons_home:
			try{
				// To cancel QR code expiration timer task in Mpay
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
				// To cancel Chat timer task in Communication
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			Intent intent_home = new Intent().setClass(mCtx.getApplicationContext(),ShopperHomePage.class);
			intent_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			mCtx.startActivity(intent_home);
			break;
		default:
			break;
		}
	}
}
