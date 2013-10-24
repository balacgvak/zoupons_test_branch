package com.us.zoupons.storeowner.HomePage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.us.zoupons.Messages;
import com.us.zoupons.R;
import com.us.zoupons.SlidingView;
import com.us.zoupons.ClassVariables.LoginUser_ClassVariables;
import com.us.zoupons.FlagClasses.AccountLoginFlag;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.storeowner.DashBoard.StoreOwnerDashBoard;

public class StoreOwner_HomePage extends Activity {

	public final String TAG="StoreOwner_HomePage";
	
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;

	ListView mHomePageListView;
	Button mHomePageStoreInformation,mHomePageCustomerAccount;
	public final String PREFENCES_FILE = "StoreDetailsPrefences";
	private String mStoreId="",mUserType="",mUserId="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.storeowner_homepage);
			mHomePageListView=(ListView) findViewById(R.id.storeowner_homepage_listview);
			mHomePageStoreInformation=(Button) findViewById(R.id.storeowner_homepage_storeinformation);
			mHomePageCustomerAccount=(Button) findViewById(R.id.storeowner_homepage_customeraccount);
			// To save store_id in preference
			SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
			if(WebServiceStaticArrays.mLoginUserList.size() > 0){
				LoginUser_ClassVariables obj = (LoginUser_ClassVariables) WebServiceStaticArrays.mLoginUserList.get(0);
				Editor editor = mPrefs.edit();
				mStoreId = obj.mStoreId;
				mUserType = obj.usertype;
				mUserId = obj.userID;
				editor.putString("store_id",obj.mStoreId);
				editor.putString("user_id",obj.userID);
				editor.putString("user_type",obj.usertype);
				editor.putString("user_name",obj.firstName);
				editor.putString("information_access",obj.information_access);
				editor.putString("gift_cards_access",obj.gift_cards_access);
				editor.putString("deal_cards_access",obj.deal_cards_access);
				editor.putString("coupons_access",obj.coupons_access);
				editor.putString("reviews_access",obj.reviews_access);
				editor.putString("photos_access",obj.photos_access);
				editor.putString("videos_access",obj.videos_access);
				editor.putString("dashboard_access",obj.dashboard_access);
				editor.putString("point_of_sale_access",obj.point_of_sale_access);
				editor.putString("invoice_center_access",obj.invoice_center_access);
				editor.putString("refund_access",obj.refund_access);
				editor.putString("batch_sales_access",obj.batch_sales_access);
				editor.putString("customer_center_access",obj.customer_center_access);
				editor.putString("communication_access",obj.communication_access);
				editor.putString("locations_access",obj.location_access);
				editor.putString("employees_access",obj.employee_access);
				editor.putString("billing_access",obj.billing_access);
				editor.commit();			
			}else{
				Log.i("login", "size is empty");
			}

			mHomePageStoreInformation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent_storeinfo = new Intent().setClass(StoreOwner_HomePage.this,StoreOwnerDashBoard.class);
					intent_storeinfo.putExtra("classname", "StoreInformation");
					startActivity(intent_storeinfo);
				}
			});

			mHomePageCustomerAccount.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// To update user type based upon login choice
					AccountLoginFlag.accountUserTypeflag="Customer";

					Intent login_intent = new Intent(StoreOwner_HomePage.this,SlidingView.class);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					login_intent.putExtra("fromlogin", true);
					startActivity(login_intent);
					finish();
				}
			});
			Log.i("user type", mUserType);
			if(mUserType.equalsIgnoreCase("Store_owner")){ // To list all location under his store
				mHomePageStoreInformation.setVisibility(View.GONE);
				StoreOwnerHomePageAsynchTask mStoreOwnerHomePage = new StoreOwnerHomePageAsynchTask(StoreOwner_HomePage.this,"PROGRESS",mHomePageListView,TAG);
				mStoreOwnerHomePage.execute(mStoreId,"");
			}else{ // To list authorized location for store employee 
				// To hide store location button
				mHomePageStoreInformation.setVisibility(View.GONE);
				findViewById(R.id.storeowner_homepage_storeinfo_container).setVisibility(View.GONE);
				StoreOwnerHomePageAsynchTask mStoreOwnerHomePage = new StoreOwnerHomePageAsynchTask(StoreOwner_HomePage.this,"PROGRESS",mHomePageListView,TAG);
				mStoreOwnerHomePage.execute(mStoreId,mUserId);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	protected void onUserLeaveHint() {
		Log.i(TAG,Messages.getString("onUserLeaveHint"));
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_HomePage.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		new CheckUserSession(StoreOwner_HomePage.this).checkIfSessionExpires();
		
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwner_HomePage.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
