package com.us.zoupons.storeowner.Locations;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MobileNumberFilter;
import com.us.zoupons.MobileNumberTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
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

public class StoreOwner_AddLocations extends Activity {

	public static String TAG="StoreOwner_AddLocations";

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
	Button mStoreOwner_AddLocations_FreezeView,mStoreOwner_AddLocations_Save;
	LinearLayout mStoreOwner_AddLocations_Back,mSplitter,mStoreOwner_AddLocations_InActivate;
	private EditText mAddLocationAddress1,mAddLocationAddress2,mAddLocationCity,mAddLocationState,mAddLocationZipcode,mAddLocationMobileNumber;
	private ImageView mStateContextMenuImage;
	private TextView mAddLocationAddress1Text,mAddLocationAddress2Text,mAddLocationCityText,mAddLocationStateText,mAddLocationZipcodeText,mAddLocationMobileNumberText,mRightFooterText;
	private TextView mAddLocationAddress1Header,mAddLocationCityHeader,mAddLocationStateHeader,mAddLocationZipcodeHeader,mAddLocationMobileNumberHeader;
	private FrameLayout mStateContainer;
	private String[] statecodes = new String[]{"AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"};
	private String mSelectedLocationid="",mLocationStatus="";
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

			app = inflater.inflate(R.layout.storeowner_addlocations, null);

			ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.storeowner_addlocations_container);
			ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeowner_addlocations_footer);

			mStoreNameText = (TextView) app.findViewById(R.id.storeowner_addlocations_storename);
			mRightMenuHolder = (ImageView) app.findViewById(R.id.storeowner_addlocations_rightmenu);
			mStoreOwner_AddLocations_FreezeView = (Button) app.findViewById(R.id.storeowner_addlocations_freeze);
			mStoreOwner_AddLocations_Back = (LinearLayout) mFooterView.findViewById(R.id.storeowner_addlocations_back);
			mSplitter = (LinearLayout) mFooterView.findViewById(R.id.menubar_splitter3);
			mStoreOwner_AddLocations_InActivate = (LinearLayout) mFooterView.findViewById(R.id.storeowner_addlocations_inactivate); 
			mRightFooterText = (TextView) mFooterView.findViewById(R.id.storeowner_addlocations_inactivate_text);
			mStoreOwner_AddLocations_Save = (Button) mMiddleView.findViewById(R.id.storeowner_addlocations_Save);
			mAddLocationAddress1 = (EditText) app.findViewById(R.id.storeowner_addlocations_address1_value);
			mAddLocationAddress2 = (EditText) app.findViewById(R.id.storeowner_addlocations_address2_value);
			mAddLocationCity = (EditText) app.findViewById(R.id.storeowner_addlocations_city_value);
			mAddLocationState = (EditText) app.findViewById(R.id.storeowner_addlocations_state_value);
			mStateContextMenuImage = (ImageView) app.findViewById(R.id.storeowner_addlocations_selectstate_contextmenu);
			mAddLocationZipcode = (EditText) app.findViewById(R.id.storeowner_addlocations_zipcode_value);
			mAddLocationMobileNumber = (EditText) app.findViewById(R.id.storeowner_addlocations_mobilenumber_value);
			mAddLocationAddress1Text = (TextView) app.findViewById(R.id.storeowner_addlocations_address1_text);
			mAddLocationAddress2Text = (TextView) app.findViewById(R.id.storeowner_addlocations_address2_text);
			mAddLocationCityText = (TextView) app.findViewById(R.id.storeowner_addlocations_city_text);
			mAddLocationStateText = (TextView) app.findViewById(R.id.storeowner_addlocations_state_text);
			mAddLocationZipcodeText = (TextView) app.findViewById(R.id.storeowner_addlocations_zipcode_text);
			mAddLocationMobileNumberText = (TextView) app.findViewById(R.id.storeowner_addlocations_mobilenumber_text);
			mAddLocationAddress1Header = (TextView) app.findViewById(R.id.address1_text);
			mAddLocationCityHeader = (TextView) app.findViewById(R.id.city_text);
			mAddLocationStateHeader = (TextView) app.findViewById(R.id.state_text);
			mAddLocationZipcodeHeader = (TextView) app.findViewById(R.id.zipcode_text);
			mAddLocationMobileNumberHeader = (TextView) app.findViewById(R.id.mobilenumber_text);
			mStateContainer = (FrameLayout) app.findViewById(R.id.storeowner_addlocations_selecttype_container);
			mAddLocationZipcode.setInputType(InputType.TYPE_CLASS_NUMBER);
			// To validate mobile number and setting the limit...
			mAddLocationMobileNumber.addTextChangedListener(new MobileNumberTextWatcher());
			mAddLocationMobileNumber.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});
			if(getIntent().getExtras().getString("footerflag").equalsIgnoreCase("no")){
				mSplitter.setVisibility(View.GONE);
				mStoreOwner_AddLocations_InActivate.setVisibility(View.INVISIBLE);
				mRightFooterText.setText("");
			}else{
				mSplitter.setVisibility(View.VISIBLE);
				mStoreOwner_AddLocations_InActivate.setVisibility(View.VISIBLE);
			}
			if(getIntent().hasExtra("address1")){ // For viewing location details
				mSelectedLocationid = getIntent().getExtras().getString("location_id");
				mAddLocationAddress1Text.setVisibility(View.VISIBLE);
				mAddLocationAddress1Text.setText(getIntent().getExtras().getString("address1"));
				mAddLocationAddress1.setVisibility(View.GONE);
				mAddLocationAddress2Text.setVisibility(View.VISIBLE);
				mAddLocationAddress2Text.setText(getIntent().getExtras().getString("address2"));
				mAddLocationAddress2.setVisibility(View.GONE);
				mAddLocationCityText.setVisibility(View.VISIBLE);
				mAddLocationCityText.setText(getIntent().getExtras().getString("city"));
				mAddLocationCity.setVisibility(View.GONE);
				mAddLocationStateText.setVisibility(View.VISIBLE);
				mAddLocationStateText.setText(getIntent().getExtras().getString("state"));
				mStateContainer.setVisibility(View.GONE);
				mAddLocationZipcodeText.setVisibility(View.VISIBLE);
				mAddLocationZipcodeText.setText(getIntent().getExtras().getString("zipcode"));
				mAddLocationZipcode.setVisibility(View.GONE);
				mAddLocationMobileNumberText.setVisibility(View.VISIBLE);
				mAddLocationMobileNumberText.setText(getIntent().getExtras().getString("mobile_number"));
				mAddLocationMobileNumber.setVisibility(View.GONE);
				mStoreOwner_AddLocations_Save.setVisibility(View.GONE);
				if(getIntent().getExtras().getString("status").equalsIgnoreCase("active")){
					mRightFooterText.setText("Inactivate Location");
					mLocationStatus = "Inactive";
				}else if(getIntent().getExtras().getString("status").equalsIgnoreCase("inactive")){
					mRightFooterText.setText("Activate Location");
					mLocationStatus = "Active";
				}
			}else{ // For adding new locations
				mAddLocationAddress1Header.setTypeface(mZouponsFont);
				mAddLocationCityHeader.setTypeface(mZouponsFont);
				mAddLocationStateHeader.setTypeface(mZouponsFont);
				mAddLocationZipcodeHeader.setTypeface(mZouponsFont);
				mAddLocationMobileNumberHeader.setTypeface(mZouponsFont);
			}

			storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwner_AddLocations.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_AddLocations_FreezeView, TAG);
			mRightMenu = storeowner_rightmenu.intializeInflater();
			storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_AddLocations.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_AddLocations_FreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();

			storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);

			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeowner_addlocations_header);
			header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_AddLocations_FreezeView, TAG);
			final View[] children = new View[] { mLeftMenu, app,mRightMenu};

			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));

			mStoreOwner_AddLocations_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_AddLocations_FreezeView, TAG));
			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2,mStoreOwner_AddLocations_FreezeView , TAG));

			registerForContextMenu(mStateContextMenuImage);
			// To set StoreName
			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
			String mStoreName = mPrefs.getString("store_name", "");
			mStoreNameText.setText(mStoreName);

			mStoreOwner_AddLocations_Back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});

			mAddLocationState.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openContextMenu(mStateContextMenuImage);
				}
			});

			mStateContextMenuImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openContextMenu(mStateContextMenuImage);
				}
			});

			mStoreOwner_AddLocations_InActivate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(new NetworkCheck().ConnectivityCheck(StoreOwner_AddLocations.this)){
						StoreLocationsAsyncTask mAddLocation = new StoreLocationsAsyncTask(StoreOwner_AddLocations.this,mSelectedLocationid,mLocationStatus);
						mAddLocation.execute();
					}else{
						Toast.makeText(StoreOwner_AddLocations.this, "Network connection not available", Toast.LENGTH_SHORT).show();
					}
				}

			});

			mStoreOwner_AddLocations_Save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mAddLocationAddress1.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter location address1", mAddLocationAddress1);
					}else if(mAddLocationCity.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter city", mAddLocationCity);
					}else if(mAddLocationState.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter state", mAddLocationState);
					}else if(mAddLocationZipcode.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter zipcode", mAddLocationZipcode);
					}else if(mAddLocationMobileNumber.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter mobile number", mAddLocationMobileNumber);
					}else if(mAddLocationMobileNumber.getText().toString().trim().length()!=12){
						alertBox_service("Information", "Please enter valid mobile number", mAddLocationMobileNumber);
					}else{
						if(new NetworkCheck().ConnectivityCheck(StoreOwner_AddLocations.this)){
							StoreLocationsAsyncTask mAddLocation = new StoreLocationsAsyncTask(StoreOwner_AddLocations.this,mAddLocationAddress1.getText().toString(),mAddLocationAddress2.getText().toString(),mAddLocationCity.getText().toString(),mAddLocationState.getText().toString(),mAddLocationZipcode.getText().toString(),mAddLocationMobileNumber.getText().toString());
							mAddLocation.execute();
						}else{
							Toast.makeText(StoreOwner_AddLocations.this, "Network connection not available", Toast.LENGTH_SHORT).show();
						}

					}
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Choose Any State");
		for(int i=0 ; i< statecodes.length ;i++){
			menu.add(0, v.getId(), 0, statecodes[i]);	
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getGroupId()){
		case 0:
			mAddLocationState.setText(item.getTitle());
		}
		return super.onContextItemSelected(item);
	}

	private void alertBox_service(String title, final String msg,final EditText editText) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwner_AddLocations.this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setCancelable(true);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				editText.requestFocus();
			}
		});
		service_alert.show();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mAddLocationAddress1.setFocusable(true);
		mAddLocationAddress1.setFocusableInTouchMode(true);
		mAddLocationCity.setFocusable(true);
		mAddLocationCity.setFocusableInTouchMode(true);
		mAddLocationZipcode.setFocusable(true);
		mAddLocationZipcode.setFocusableInTouchMode(true);
		mAddLocationMobileNumber.setFocusable(true);
		mAddLocationMobileNumber.setFocusableInTouchMode(true);
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
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_AddLocations.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwner_AddLocations.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwner_AddLocations.this);
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
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_AddLocations.this);
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
