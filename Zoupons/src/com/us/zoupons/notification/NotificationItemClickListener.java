package com.us.zoupons.notification;

import android.content.Intent;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.us.zoupons.CustomerService.customercenter;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.storeowner.Photos.StoreOwner_Photos;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;

public class NotificationItemClickListener implements OnItemClickListener{

	String TAG="NotificationItemClickListener";

	@Override
	public void onItemClick(AdapterView<?> adapterview, View arg1, int arg2, long arg3) {
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

			if(MainMenuActivity.storePhotoLoaderAsyncTask!=null){
				if(MainMenuActivity.storePhotoLoaderAsyncTask.getStatus()==Status.RUNNING){
					MainMenuActivity.storePhotoLoaderAsyncTask.cancel(true);
					MainMenuActivity.storePhotoLoaderAsyncTask=null;
				}
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

		NotificationDetails mNotificationDetails = (NotificationDetails) adapterview.getItemAtPosition(arg2);
		if(!mNotificationDetails.notification_shortmessage.equals("")){
			Intent intent_customer_service = new Intent().setClass(arg1.getContext(),customercenter.class);
			arg1.getContext().startActivity(intent_customer_service);
		}
	}
}
