package com.us.zoupons.notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Async task to communicate with Web server to get all notifications
 *
 */

public class GetNotificationTask extends AsyncTask<String, String, String>{

	private ZouponsWebService zouponswebservice;
	private ZouponsParsingClass parsingclass;
	private NetworkCheck mConnectivityCheck;
	private Context mContext;
	private String mNotify_to_type,mFlag="",mRecentNotificationId="",mLocationId="";
	private TextView mFooterText;
	private ProgressBar mFooterProgress;

	public GetNotificationTask(Context context, String notify_to_type) {
		zouponswebservice = new ZouponsWebService(context);
		parsingclass = new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
		this.mContext = context;
		this.mNotify_to_type = notify_to_type;
	}

	public GetNotificationTask(Context context, String notify_to_type,String flag, TextView footerText, ProgressBar footerProgress, String notification_id) {
		zouponswebservice = new ZouponsWebService(context);
		parsingclass = new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
		this.mContext = context;
		this.mNotify_to_type = notify_to_type;
		this.mFlag = flag;
		this.mFooterProgress = footerProgress;
		this.mFooterText = footerText;
		this.mRecentNotificationId = notification_id;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(mFlag.equalsIgnoreCase("scroll"))
			NotificationDetails.isLoadMore = true;
	}

	@Override
	protected String doInBackground(String... params) {
		String mresult="";
		if (mConnectivityCheck.ConnectivityCheck(mContext)) {
			SharedPreferences mPreference = mContext.getSharedPreferences("NotificationDetailsPrefences", Context.MODE_PRIVATE);
			String mNotification_flag = mPreference.getString("notification_flag", "");
			if(!mFlag.equalsIgnoreCase("")){ // For Scroll
				mNotification_flag = mFlag;
			}else if(mNotification_flag.equalsIgnoreCase("initial_load")){ // First time load
				mNotification_flag = ""; // For first time load..
				mRecentNotificationId = "";
			}else if((mNotification_flag.equalsIgnoreCase("refresh")) && WebServiceStaticArrays.mAllNotifications.size() > 0){ // Refresh 
				NotificationDetails mNotificationDetail = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(0);
				mRecentNotificationId = mNotificationDetail.id;
			}
			if(mNotify_to_type.equalsIgnoreCase("store")){
				SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				mLocationId = mPrefs.getString("location_id", "");
			}
			String mResponse = zouponswebservice.getNotification(mNotify_to_type,mNotify_to_type,mNotification_flag,mRecentNotificationId,mLocationId);
			if (!mResponse.equals("")) {
				if (!mResponse.equals("failure")&& !mResponse.equals("noresponse")) {
					String mParsingResponse = parsingclass.parseNotifications(mResponse,mNotification_flag);
					if (mParsingResponse.equalsIgnoreCase("success")) {
						mresult = "success";
					}else{
						mresult = mParsingResponse;
					}
				} else {
					mresult = "Response Error.";
				}
			} else {
				mresult = "nodata";
			}
		} else {
			mresult = "nonetwork";
		}
		return mresult;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.i("result", result);
		if(result.equalsIgnoreCase("success")){ 
			// To Broadcast result to all activities which are registered 
			Intent mIntent =new Intent(BroadCastActionClassVariables.ACTION);
			mIntent.putExtra("FromNotification", true);
			mContext.sendBroadcast(mIntent);
			if(mFooterProgress!=null){
				NotificationDetails.isLoadMore = false;
				mFooterProgress.setVisibility(View.GONE);
				mFooterText.setVisibility(View.GONE);
			}
		}else if(result.equalsIgnoreCase("norecords")){
			// To Broadcast result to all activities which are registered
			Intent mIntent =new Intent(BroadCastActionClassVariables.ACTION);
			mIntent.putExtra("FromNotification", true);
			mContext.sendBroadcast(mIntent);
			if(mFlag.equalsIgnoreCase("scroll")){ 
				mFooterProgress.setTag("DontCallServiceAnyMore");
				NotificationDetails.isLoadMore = false;
				mFooterProgress.setVisibility(View.GONE);
				mFooterText.setVisibility(View.GONE);
			}
		}

	}
}