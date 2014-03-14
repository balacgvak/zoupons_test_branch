package com.us.zoupons.storeowner.locations;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.POJOStoreInfo;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

/**
 * 
 * Activity to list store locations with Status 
 *
 */

public class StoreOwner_Locations extends Activity {

	// Initializing views and variables
	private String TAG="StoreOwner_Locations";
	private MyHorizontalScrollView mScrollView;
	private	View mApp;
	private Header mZouponsheader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private View mRightMenu;
	private View mLeftMenu;
	private int mMenuFlag;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private TextView mStoreNameText;
	private ImageView mRightMenuHolder;
	private Button mStoreOwner_Locations_FreezeView;
	private ListView mStoreOwner_Locations_ListView;
	private LinearLayout mSplitter,mStoreOwner_Locations_AddLocation,mStoreOwner_Locations_InfoTab,mStoreOwner_Locations_InfoTabBelowLine;
	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			// Referencing view from layout file
			LayoutInflater inflater = LayoutInflater.from(this);
			mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(mScrollView);
			mApp = inflater.inflate(R.layout.storeowner_storeinformation, null);
			ViewGroup mMiddleView = (ViewGroup) mApp.findViewById(R.id.middleview);
			ViewGroup mFooterView = (ViewGroup) mApp.findViewById(R.id.storeownerstoreinformation_footer);
			mStoreNameText = (TextView) mMiddleView.findViewById(R.id.storeowner_locations_storename);
			mRightMenuHolder = (ImageView) mMiddleView.findViewById(R.id.storeowner_locations_rightmenu);
            mStoreOwner_Locations_InfoTab = (LinearLayout) mMiddleView.findViewById(R.id.infotab);
			mStoreOwner_Locations_InfoTab.setVisibility(View.VISIBLE);
			mStoreOwner_Locations_InfoTabBelowLine = (LinearLayout) mMiddleView.findViewById(R.id.infotabbelowline);
			mStoreOwner_Locations_InfoTabBelowLine.setVisibility(View.VISIBLE);
			mStoreOwner_Locations_FreezeView = (Button) mApp.findViewById(R.id.freezeview);
			mStoreOwner_Locations_ListView = (ListView) mMiddleView.findViewById(R.id.storeownerstoreinformationListView);
			mStoreOwner_Locations_AddLocation = (LinearLayout) mFooterView.findViewById(R.id.storeownerstoreinformation_footer_add);
			mStoreOwner_Locations_AddLocation.setVisibility(View.VISIBLE);
			mSplitter = (LinearLayout) mFooterView.findViewById(R.id.menubar_splitter3);
			mSplitter.setVisibility(View.VISIBLE);
			mStoreownerRightmenu = new StoreOwner_RightMenu(StoreOwner_Locations.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_Locations_FreezeView, TAG);
			mRightMenu = mStoreownerRightmenu.intializeInflater();
			mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreOwner_Locations.this,mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Locations_FreezeView, TAG);
			mLeftMenu = mStoreownerLeftmenu.intializeInflater();
			mStoreownerLeftmenu.clickListener( mLeftMenu /*,mRightMenu*/ );
			mStoreownerRightmenu.clickListener( mLeftMenu, mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsheader = (Header) mApp.findViewById(R.id.storeownerinformation_header);
			mZouponsheader.intializeInflater(mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Locations_FreezeView, TAG);
			final View[] children = new View[] { mLeftMenu, mApp,mRightMenu};
			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsheader.mLeftMenuBtnSlide));
			mStoreOwner_Locations_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_Locations_FreezeView, TAG));
			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwner_Locations_FreezeView, TAG));
			mNotificationReceiver = new NotifitcationReceiver(mZouponsheader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsheader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsheader,mZouponsheader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsheader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsheader,mZouponsheader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			// To set StoreName
			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
			String mStoreName = mPrefs.getString("store_name", "");
			mStoreNameText.setText(mStoreName);
			
			if(new NetworkCheck().ConnectivityCheck(StoreOwner_Locations.this)){ // network check..
				// To load store location list
				StoreLocationsAsyncTask mLocationTask = new StoreLocationsAsyncTask(StoreOwner_Locations.this,mStoreOwner_Locations_ListView);
				mLocationTask.execute();
			}else{
				Toast.makeText(StoreOwner_Locations.this, "Network connection not available", Toast.LENGTH_SHORT).show();
			}
			
			mStoreOwner_Locations_AddLocation.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// To launch add location activity
					Intent intent_addlocations = new Intent(StoreOwner_Locations.this,StoreOwner_AddLocations.class);
					intent_addlocations.putExtra("footerflag", "no");
					startActivity(intent_addlocations);
				}
			});

			mStoreOwner_Locations_ListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// To launch location detail activity
					Intent addlocations_intent = new Intent(StoreOwner_Locations.this,StoreOwner_AddLocations.class);
					POJOStoreInfo mLocationDetails = (POJOStoreInfo) arg0.getItemAtPosition(arg2);
					if(mLocationDetails.status.equalsIgnoreCase("pending_active") || mLocationDetails.status.equalsIgnoreCase("pending_inactive")){
						addlocations_intent.putExtra("footerflag", "no");
					}else{
						addlocations_intent.putExtra("footerflag", "yes");
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
		// To notify  system that its time to run garbage collector service
		System.gc();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mNotificationReceiver);
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
		// To listen for broadcast receiver to receive notification message
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_Locations.this,ZouponsConstants.sStoreModuleFlag);
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
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_Locations.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}
	
}
