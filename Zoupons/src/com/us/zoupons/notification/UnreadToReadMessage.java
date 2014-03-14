package com.us.zoupons.notification;

import android.content.Context;
import android.os.AsyncTask;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynctask to communicate with server to change unread notification to read.
 *
 */

public class UnreadToReadMessage extends AsyncTask<String, String, String> {

	private ZouponsWebService mZouponswebservice;
	private NetworkCheck mConnectivityCheck;
	private Context mContext;
	
	public UnreadToReadMessage(Context context){
		this.mContext=context;
		mZouponswebservice = new ZouponsWebService(context);
		mConnectivityCheck = new NetworkCheck();
	}

	@Override
	protected String doInBackground(String... params) {
		String mresult="";
		if (mConnectivityCheck.ConnectivityCheck(mContext)) {
			String mResponse = mZouponswebservice.UpdateNotificationStatus(params[0]);
			if (!mResponse.equals("")) {
				if (!mResponse.equals("failure")&& !mResponse.equals("noresponse")) {
					mresult = "success";
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
	}

	
}
