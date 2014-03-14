package com.us.zoupons.loginsession;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.ClearArrayList;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.login.ZouponsLogin;
import com.us.zoupons.mobilepay.NormalPaymentAsyncTask;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynctask to communicate to server that user session is ended
 *
 */

public class LogoutSessionTask extends AsyncTask<Void, Void, Void>{
	
	private Context mContext;
	private NetworkCheck mConnectivityNetworkCheck;
	private ZouponsWebService mZouponswebservice;
	private String mLoggoutType;
	
	public LogoutSessionTask(Context context,String logout_type) {
		this.mContext = context;		
		this.mLoggoutType = logout_type; // from_timer/manual_logout
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		if(NormalPaymentAsyncTask.mCountDownTimer!=null){
			NormalPaymentAsyncTask.mCountDownTimer.cancel();
			NormalPaymentAsyncTask.mCountDownTimer = null;
		}
		
		// To cancel Rewards advertisement task
		if(MainMenuActivity.mTimer!=null){
			MainMenuActivity.mTimer.cancel();
			MainMenuActivity.mTimer = null;
		}
		
		ClearArrayList.cleararraylist();	// Clear All static array list
		
		// To launch Login activity
		Intent loginIntent= new Intent(context,ZouponsLogin.class);
		loginIntent.putExtra(mLoggoutType, true);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(loginIntent);
			
	}

	@Override
	protected Void doInBackground(Void... params) {
		try{
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){
				SharedPreferences mPrefs = mContext.getSharedPreferences(ZouponsLogin.PREFENCES_FILE, Context.MODE_PRIVATE);
				// Call to webservice to end the login session
				mZouponswebservice.endLoginSession(UserDetails.mServiceUserId,mPrefs.getString("device_id", ""));	
			}else{}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		}
}