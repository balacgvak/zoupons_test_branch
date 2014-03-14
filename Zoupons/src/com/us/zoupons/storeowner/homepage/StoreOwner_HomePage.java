package com.us.zoupons.storeowner.homepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.us.zoupons.R;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.AccountLoginFlag;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.storeowner.dashBoard.StoreOwnerDashBoard;

/**
 * 
 * Activity to display login choice details and location list
 *
 */

public class StoreOwner_HomePage extends Activity {

	// Initializing views and variables
	private final String mTAG="StoreOwner_HomePage";
	private	ListView mHomePageListView;
	private Button mHomePageStoreInformation,mHomePageCustomerAccount;
	private String mStoreId="",mUserType="",mUserId="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.storeowner_homepage);
			mHomePageListView=(ListView) findViewById(R.id.storeowner_homepage_listview);
			mHomePageStoreInformation=(Button) findViewById(R.id.storeowner_homepage_storeinformation);
			mHomePageCustomerAccount=(Button) findViewById(R.id.storeowner_homepage_customeraccount);
			// To get store and user details from Preferences
			SharedPreferences mPrefs = getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
			mStoreId = mPrefs.getString("store_id", "");
			mUserId = mPrefs.getString("user_id", "");
			mUserType = mPrefs.getString("user_type", "");
            // Setting notification flag to initial_load, to load notification from initial position
			SharedPreferences mPreference = getSharedPreferences("NotificationDetailsPrefences", Context.MODE_PRIVATE);
			Editor mPrefEditor = mPreference.edit();
			mPrefEditor.putString("notification_flag", "initial_load");
			mPrefEditor.commit();
			// To flush notification arraylist and count variables
			WebServiceStaticArrays.mAllNotifications.clear();
			NotificationDetails.notificationcount = 0 ;
			
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
					Intent login_intent = new Intent(StoreOwner_HomePage.this,ShopperHomePage.class);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					login_intent.putExtra("fromlogin", true);
					startActivity(login_intent);
					finish();
				}
			});
			
			if(mUserType.equalsIgnoreCase("Store_owner")){ // To list all location under his store
				mHomePageStoreInformation.setVisibility(View.GONE);
				StoreOwnerHomePageAsynchTask mStoreOwnerHomePage = new StoreOwnerHomePageAsynchTask(StoreOwner_HomePage.this,"PROGRESS",mHomePageListView,mTAG);
				mStoreOwnerHomePage.execute(mStoreId,"");
			}else{ // To list authorized location for store employee 
				// To hide store location button
				mHomePageStoreInformation.setVisibility(View.GONE);
				findViewById(R.id.storeowner_homepage_storeinfo_container).setVisibility(View.GONE);
				StoreOwnerHomePageAsynchTask mStoreOwnerHomePage = new StoreOwnerHomePageAsynchTask(StoreOwner_HomePage.this,"PROGRESS",mHomePageListView,mTAG);
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
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_HomePage.this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// To notify  system that its time to run garbage collector service
		System.gc();
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
