package com.us.zoupons;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;

public class HeaderHomeClickListener implements OnClickListener{

	public Context mCtx;
	String TAG="HeaderHomeClickListener";

	public HeaderHomeClickListener(Context context){
		super();
		this.mCtx=context;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zoupons_home:
			try{
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

			}catch(Exception e){
				e.printStackTrace();
			}
			Intent intent_home = new Intent().setClass(mCtx.getApplicationContext(),SlidingView.class);
			mCtx.startActivity(intent_home);
			break;
		default:
			break;
		}
	}
}
