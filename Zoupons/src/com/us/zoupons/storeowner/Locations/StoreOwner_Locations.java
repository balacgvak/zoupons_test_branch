package com.us.zoupons.storeowner.Locations;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;


public class StoreOwner_Locations extends Activity {

	public static String TAG="StoreOwner_Locations";

	public static MyHorizontalScrollView scrollView;
	View app;

	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;

	Header header;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;
	View mLeftMenu;
	int mMenuFlag;
	StoreOwner_LeftMenu storeowner_leftmenu;
	private TextView mStoreNameText;
	private ImageView mRightMenuHolder;
	Button mStoreOwner_Locations_FreezeView;
	ListView mStoreOwner_Locations_ListView;
	LinearLayout mStoreOwner_Locations_Back,mSplitter,mStoreOwner_Locations_AddLocation,mStoreOwner_Locations_InfoTab,mStoreOwner_Locations_InfoTabBelowLine;
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			LayoutInflater inflater = LayoutInflater.from(this);
			scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(scrollView);

			mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
			mConnectionAvailabilityChecking = new NetworkCheck();

			app = inflater.inflate(R.layout.storeowner_storeinformation, null);

			ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.middleview);
			ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeownerstoreinformation_footer);

			mStoreNameText = (TextView) mMiddleView.findViewById(R.id.storeowner_locations_storename);
			mRightMenuHolder = (ImageView) mMiddleView.findViewById(R.id.storeowner_locations_rightmenu);

			mStoreOwner_Locations_InfoTab = (LinearLayout) mMiddleView.findViewById(R.id.infotab);
			mStoreOwner_Locations_InfoTab.setVisibility(View.VISIBLE);
			mStoreOwner_Locations_InfoTabBelowLine = (LinearLayout) mMiddleView.findViewById(R.id.infotabbelowline);
			mStoreOwner_Locations_InfoTabBelowLine.setVisibility(View.VISIBLE);
			mStoreOwner_Locations_FreezeView = (Button) app.findViewById(R.id.freezeview);
			mStoreOwner_Locations_ListView = (ListView) mMiddleView.findViewById(R.id.storeownerstoreinformationListView);
			mStoreOwner_Locations_Back = (LinearLayout) mFooterView.findViewById(R.id.storeownerstoreinformation_footer_back);
			mStoreOwner_Locations_AddLocation = (LinearLayout) mFooterView.findViewById(R.id.storeownerstoreinformation_footer_add);
			mStoreOwner_Locations_AddLocation.setVisibility(View.VISIBLE);
			mSplitter = (LinearLayout) mFooterView.findViewById(R.id.menubar_splitter3);
			mSplitter.setVisibility(View.VISIBLE);

			storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwner_Locations.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_Locations_FreezeView, TAG);
			mRightMenu = storeowner_rightmenu.intializeInflater();
			storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_Locations.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Locations_FreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();

			storeowner_leftmenu.clickListener( mLeftMenu /*,mRightMenu*/ );
			storeowner_rightmenu.clickListener( mLeftMenu, mRightMenu);

			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeownerinformation_header);
			header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Locations_FreezeView, TAG);
			final View[] children = new View[] { mLeftMenu, app,mRightMenu};

			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));

			mStoreOwner_Locations_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_Locations_FreezeView, TAG));
			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwner_Locations_FreezeView, TAG));

			if(new NetworkCheck().ConnectivityCheck(StoreOwner_Locations.this)){
				StoreLocationsAsyncTask mLocationTask = new StoreLocationsAsyncTask(StoreOwner_Locations.this,mStoreOwner_Locations_ListView);
				mLocationTask.execute();
			}else{
				Toast.makeText(StoreOwner_Locations.this, "Network connection not available", Toast.LENGTH_SHORT).show();
			}
			// To set StoreName
			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
			String mStoreName = mPrefs.getString("store_name", "");
			mStoreNameText.setText(mStoreName);

			mStoreOwner_Locations_Back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				}
			});

			mStoreOwner_Locations_AddLocation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent_addlocations = new Intent(StoreOwner_Locations.this,StoreOwner_AddLocations.class);
					intent_addlocations.putExtra("footerflag", "no");
					startActivity(intent_addlocations);
				}
			});

			mStoreOwner_Locations_ListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					Intent addlocations_intent = new Intent(StoreOwner_Locations.this,StoreOwner_AddLocations.class);
					POJOStoreInfo mLocationDetails = (POJOStoreInfo) arg0.getItemAtPosition(arg2);
					if(!mLocationDetails.status.equalsIgnoreCase("pending")){
						addlocations_intent.putExtra("footerflag", "yes");	
					}else{
						addlocations_intent.putExtra("footerflag", "no");	
					}
					addlocations_intent.putExtra("location_id", mLocationDetails.location_id);
					addlocations_intent.putExtra("status", mLocationDetails.status);
					addlocations_intent.putExtra("address1", mLocationDetails.address_line1);
					addlocations_intent.putExtra("address2", mLocationDetails.address_line2);
					addlocations_intent.putExtra("city", mLocationDetails.city);
					addlocations_intent.putExtra("state", mLocationDetails.state);
					addlocations_intent.putExtra("zipcode", mLocationDetails.zip_code);
					addlocations_intent.putExtra("mobile_number", mLocationDetails.phone);
					startActivity(addlocations_intent);
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		if(mNotificationSync!=null){
			mNotificationSync.cancelAlarm();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_Locations.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwner_Locations.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwner_Locations.this);
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

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwner_Locations_ListView.setVisibility(View.VISIBLE);
	}
	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_Locations.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			try{
				Log.i(TAG,"OnReceive");
				if(intent.hasExtra("FromNotification")){
					if(NotificationDetails.notificationcount>0){
						header.mTabBarNotificationCountBtn.setVisibility(View.VISIBLE);
						header.mTabBarNotificationCountBtn.setText(String.valueOf(NotificationDetails.notificationcount));
					}else{
						header.mTabBarNotificationCountBtn.setVisibility(View.GONE);
						Log.i("Notification result", "No new notification");
					}
				}					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};
}
