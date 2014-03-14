package com.us.zoupons.storeowner.locations;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
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
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
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
 * Activity to display add store location details 
 *
 */

public class StoreOwner_AddLocations extends Activity {

	// Initializing views and variables
	private String TAG="StoreOwner_AddLocations";
	private MyHorizontalScrollView mScrollView;
	private View mApp;
	private Typeface mZouponsFont;
	private Header mZouponsHeader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private View mRightMenu;
	private View mLeftMenu;
	private int mMenuFlag;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private TextView mStoreNameText;
	private ImageView mRightMenuHolder;
	private Button mStoreOwner_AddLocations_FreezeView,mStoreOwner_AddLocations_Save;
	private LinearLayout mStoreOwner_AddLocations_Back,mSplitter,mStoreOwner_AddLocations_InActivate;
	private EditText mAddLocationAddress1,mAddLocationAddress2,mAddLocationCity,mAddLocationState,mAddLocationZipcode,mAddLocationMobileNumber;
	private ImageView mStateContextMenuImage;
	private TextView mAddLocationAddress1Text,mAddLocationAddress2Text,mAddLocationCityText,mAddLocationStateText,mAddLocationZipcodeText,mAddLocationMobileNumberText,mRightFooterText;
	private TextView mAddLocationAddress1Header,mAddLocationCityHeader,mAddLocationStateHeader,mAddLocationZipcodeHeader,mAddLocationMobileNumberHeader;
	private FrameLayout mStateContainer;
	private String[] statecodes = new String[]{"AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"};
	private String mSelectedLocationid="",mLocationStatus="";
	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			// Referencing view from layout XML file
			LayoutInflater inflater = LayoutInflater.from(this);
			mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(mScrollView);
			mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
			mApp = inflater.inflate(R.layout.storeowner_addlocations, null);
			ViewGroup mMiddleView = (ViewGroup) mApp.findViewById(R.id.storeowner_addlocations_container);
			ViewGroup mFooterView = (ViewGroup) mApp.findViewById(R.id.storeowner_addlocations_footer);
			mStoreNameText = (TextView) mApp.findViewById(R.id.storeowner_addlocations_storename);
			mRightMenuHolder = (ImageView) mApp.findViewById(R.id.storeowner_addlocations_rightmenu);
			mStoreOwner_AddLocations_FreezeView = (Button) mApp.findViewById(R.id.storeowner_addlocations_freeze);
			mStoreOwner_AddLocations_Back = (LinearLayout) mFooterView.findViewById(R.id.storeowner_addlocations_back);
			mSplitter = (LinearLayout) mFooterView.findViewById(R.id.menubar_splitter3);
			mStoreOwner_AddLocations_InActivate = (LinearLayout) mFooterView.findViewById(R.id.storeowner_addlocations_inactivate); 
			mRightFooterText = (TextView) mFooterView.findViewById(R.id.storeowner_addlocations_inactivate_text);
			mStoreOwner_AddLocations_Save = (Button) mMiddleView.findViewById(R.id.storeowner_addlocations_Save);
			mAddLocationAddress1 = (EditText) mApp.findViewById(R.id.storeowner_addlocations_address1_value);
			mAddLocationAddress2 = (EditText) mApp.findViewById(R.id.storeowner_addlocations_address2_value);
			mAddLocationCity = (EditText) mApp.findViewById(R.id.storeowner_addlocations_city_value);
			mAddLocationState = (EditText) mApp.findViewById(R.id.storeowner_addlocations_state_value);
			mAddLocationState.setLongClickable(false); // To disable cut/copy/paste options..
			mStateContextMenuImage = (ImageView) mApp.findViewById(R.id.storeowner_addlocations_selectstate_contextmenu);
			mAddLocationZipcode = (EditText) mApp.findViewById(R.id.storeowner_addlocations_zipcode_value);
			mAddLocationMobileNumber = (EditText) mApp.findViewById(R.id.storeowner_addlocations_mobilenumber_value);
			mAddLocationAddress1Text = (TextView) mApp.findViewById(R.id.storeowner_addlocations_address1_text);
			mAddLocationAddress2Text = (TextView) mApp.findViewById(R.id.storeowner_addlocations_address2_text);
			mAddLocationCityText = (TextView) mApp.findViewById(R.id.storeowner_addlocations_city_text);
			mAddLocationStateText = (TextView) mApp.findViewById(R.id.storeowner_addlocations_state_text);
			mAddLocationZipcodeText = (TextView) mApp.findViewById(R.id.storeowner_addlocations_zipcode_text);
			mAddLocationMobileNumberText = (TextView) mApp.findViewById(R.id.storeowner_addlocations_mobilenumber_text);
			mAddLocationAddress1Header = (TextView) mApp.findViewById(R.id.address1_text);
			mAddLocationCityHeader = (TextView) mApp.findViewById(R.id.city_text);
			mAddLocationStateHeader = (TextView) mApp.findViewById(R.id.state_text);
			mAddLocationZipcodeHeader = (TextView) mApp.findViewById(R.id.zipcode_text);
			mAddLocationMobileNumberHeader = (TextView) mApp.findViewById(R.id.mobilenumber_text);
			mStateContainer = (FrameLayout) mApp.findViewById(R.id.storeowner_addlocations_selecttype_container);
			mAddLocationZipcode.setInputType(InputType.TYPE_CLASS_NUMBER);
			// To validate mobile number and setting the limit...
			mAddLocationMobileNumber.addTextChangedListener(new MobileNumberTextWatcher());
			mAddLocationMobileNumber.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});
			if(getIntent().getExtras().getString("footerflag").equalsIgnoreCase("no")){ // For InActive location
				mSplitter.setVisibility(View.GONE);
				mStoreOwner_AddLocations_InActivate.setVisibility(View.INVISIBLE);
				mRightFooterText.setText("");
			}else{ // For active location
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

			mStoreownerRightmenu = new StoreOwner_RightMenu(StoreOwner_AddLocations.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_AddLocations_FreezeView, TAG);
			mRightMenu = mStoreownerRightmenu.intializeInflater();
			mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreOwner_AddLocations.this,mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_AddLocations_FreezeView, TAG);
			mLeftMenu = mStoreownerLeftmenu.intializeInflater();

			mStoreownerLeftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			mStoreownerRightmenu.clickListener(mLeftMenu,mRightMenu);

			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsHeader = (Header) mApp.findViewById(R.id.storeowner_addlocations_header);
			mZouponsHeader.intializeInflater(mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_AddLocations_FreezeView, TAG);
			final View[] children = new View[] { mLeftMenu, mApp,mRightMenu};

			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsHeader.mLeftMenuBtnSlide));

			mStoreOwner_AddLocations_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_AddLocations_FreezeView, TAG));
			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2,mStoreOwner_AddLocations_FreezeView , TAG));
		 	mNotificationReceiver = new NotifitcationReceiver(mZouponsHeader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
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
					if(new NetworkCheck().ConnectivityCheck(StoreOwner_AddLocations.this)){ // Network check
						// To call webservice to request for location status change
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
					}else if(mAddLocationZipcode.getText().toString().trim().length()!=5){
						alertBox_service("Information", "Please enter valid zipcode", mAddLocationZipcode);
					}else if(mAddLocationMobileNumber.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter mobile number", mAddLocationMobileNumber);
					}else if(mAddLocationMobileNumber.getText().toString().trim().length()!=12){
						alertBox_service("Information", "Please enter valid mobile number", mAddLocationMobileNumber);
					}else{
						if(new NetworkCheck().ConnectivityCheck(StoreOwner_AddLocations.this)){
							// To call webservice to add new store location
							StoreLocationsAsyncTask mAddLocation = new StoreLocationsAsyncTask(StoreOwner_AddLocations.this,mAddLocationAddress1.getText().toString().trim(),mAddLocationAddress2.getText().toString().trim(),mAddLocationCity.getText().toString().trim(),mAddLocationState.getText().toString().trim(),mAddLocationZipcode.getText().toString().trim(),mAddLocationMobileNumber.getText().toString().trim());
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

	/* To show alert pop up with respective message */
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
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_AddLocations.this,ZouponsConstants.sStoreModuleFlag);
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
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_AddLocations.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}
	
}
