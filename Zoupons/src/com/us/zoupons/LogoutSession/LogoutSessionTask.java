package com.us.zoupons.LogoutSession;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.ClearArrayList;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;

public class LogoutSessionTask extends AsyncTask<Void, Void, Void>{
	
	private Context context;
	private NetworkCheck mConnectivityNetworkCheck;
	private ZouponsWebService zouponswebservice;
	
	public LogoutSessionTask(Context context) {
		this.context = context;		
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		if(NormalPaymentAsyncTask.mCountDownTimer!=null){
			NormalPaymentAsyncTask.mCountDownTimer.cancel();
			NormalPaymentAsyncTask.mCountDownTimer = null;
		}
		
		// To cancel Rewards advertisement task
		if(MainMenuActivity.mTimer!=null){
			MainMenuActivity.mTimer.cancel();
			MainMenuActivity.mTimer = null;
		}
		
		if(MainMenuActivity.storePhotoLoaderAsyncTask!=null){
			if(MainMenuActivity.storePhotoLoaderAsyncTask.getStatus()==Status.RUNNING)
				MainMenuActivity.storePhotoLoaderAsyncTask.cancel(true);
				MainMenuActivity.storePhotoLoaderAsyncTask=null;
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		try{
			ClearArrayList.cleararraylist();	// Clear All static array list
			if(mConnectivityNetworkCheck.ConnectivityCheck(context)){
				SharedPreferences mPrefs = context.getSharedPreferences(ZouponsLogin.PREFENCES_FILE, context.MODE_PRIVATE);
				// Call to webservice to end the login session
				zouponswebservice.endLoginSession(UserDetails.mServiceUserId,mPrefs.getString("device_id", ""));	
			}else{}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}