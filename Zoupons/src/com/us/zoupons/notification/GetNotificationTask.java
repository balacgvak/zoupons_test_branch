package com.us.zoupons.notification;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class GetNotificationTask extends AsyncTask<String, String, String>{

	private ZouponsWebService zouponswebservice;
	private ZouponsParsingClass parsingclass;
	private NetworkCheck mConnectivityCheck;
	private Context mContext;

	public GetNotificationTask(Context context) {
		zouponswebservice = new ZouponsWebService(context);
		parsingclass = new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
		this.mContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		String mresult="";
		if (mConnectivityCheck.ConnectivityCheck(mContext)) {
			String mResponse = zouponswebservice.getNotification();
			if (!mResponse.equals("")) {
				if (!mResponse.equals("failure")&& !mResponse.equals("noresponse")) {
					String mParsingResponse = parsingclass.parseNotifications(mResponse);
					if (mParsingResponse.equalsIgnoreCase("success")) {
						mresult = "success";
					}else{
						mresult = "failure";
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
		if(result.equalsIgnoreCase("success")){
			ArrayList<Object> mTempNotificationObject = new ArrayList<Object>();
			if(POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_ContactStore")){
				mTempNotificationObject.clear();
				for(int i=0; i<WebServiceStaticArrays.mAllNotifications.size(); i++){
					NotificationDetails obj = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(i);
					if(obj.notification_type.equalsIgnoreCase("store_message")){
						mTempNotificationObject.add(obj);
					}
				}
				Intent mIntent =new Intent(BroadCastActionClassVariables.ACTION);
				mIntent.putExtra("FromNotification", true);
				//mIntent.putExtra("storeparcelable", mTempNotificationObject);
				mIntent.putExtra("storeparcelable", mTempNotificationObject);
				mContext.sendBroadcast(mIntent);
			}else if(POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_TalkToUs")){
				mTempNotificationObject.clear();
				for(int i=0; i<WebServiceStaticArrays.mAllNotifications.size(); i++){
					NotificationDetails obj = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(i);
					if(obj.notification_type.equalsIgnoreCase("zoupons_message")){
						mTempNotificationObject.add(obj);
					}
				}
				Intent mIntent =new Intent(BroadCastActionClassVariables.ACTION);
				mIntent.putExtra("FromNotification", true);
				mIntent.putExtra("zouponsparcelable", mTempNotificationObject);
				mContext.sendBroadcast(mIntent);
			}else{
				Intent mIntent =new Intent(BroadCastActionClassVariables.ACTION);
				mIntent.putExtra("FromNotification", true);
				mContext.sendBroadcast(mIntent);
			}
		}
			
	}
}