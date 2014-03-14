package com.us.zoupons.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.flagclasses.AccountLoginFlag;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.homepage.StoreOwner_HomePage;

public class GetRecentUserTypeTask extends AsyncTask<Void, Void, String>{

	private Activity mContext;
	private ProgressDialog mProgressdialog=null;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse="",mParsingResponse="";

	public GetRecentUserTypeTask(Activity context) {
		this.mContext = context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
	}


	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		//Start a status dialog
		mProgressdialog.setMessage("Checking user type");
		mProgressdialog.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				SharedPreferences mPrefs = mContext.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
				String user_id = mPrefs.getString("user_id", "");
				mGetResponse=mZouponswebservice.checkUserType(user_id);
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=mParsingclass.parseUserTypeResponse(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						result = mParsingResponse;
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						result="Response Error.";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						result="No stores Available.";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result.equalsIgnoreCase("success")){
			SharedPreferences mPrefs = mContext.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
			String user_id = mPrefs.getString("user_id", "");
			String user_type = mPrefs.getString("user_type", "");
            String is_mobile_verified = mPrefs.getString("mobile_verified", "");
            String first_name = mPrefs.getString("first_name", "");
            String last_name = mPrefs.getString("last_name", "");
            String profile_url = mPrefs.getString("profile_url", "");
			
            if(is_mobile_verified.equalsIgnoreCase("no")){
            	Intent intent_registration = new Intent(mContext,Registration.class);
				intent_registration.putExtra("class_name", "ZouponsLogin");
				intent_registration.putExtra("during_user_type_check", true);
				intent_registration.putExtra("mobile only not verified", true);
				intent_registration.putExtra("user_id", user_id);
				intent_registration.putExtra("first_name", first_name);
				intent_registration.putExtra("last_name", last_name);
				intent_registration.putExtra("profile_image", profile_url);
				mContext.startActivity(intent_registration);
            }else if(user_type.equalsIgnoreCase("store_owner") || user_type.equalsIgnoreCase("store_employee")){
				Intent login_intent = new Intent(mContext,StoreOwner_HomePage.class);
				login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(login_intent);
				mContext.finish();
			}else{ // Shopper
				AccountLoginFlag.accountUserTypeflag="Customer";
				Intent login_intent = new Intent(mContext,ShopperHomePage.class);
				login_intent.putExtra("fromlogin", true);
				login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(login_intent);
				mContext.finish();
			}
		}else if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(mContext, "unable to update user type", Toast.LENGTH_SHORT).show();
		}
		if(mProgressdialog.isShowing()) // To dismiss progress indicator dialog
			mProgressdialog.dismiss();

	}

}
