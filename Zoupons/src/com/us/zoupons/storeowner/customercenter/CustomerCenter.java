package com.us.zoupons.storeowner.customercenter;

import java.util.ArrayList;
import java.util.regex.Pattern;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.FlagClasses.MenuOutClass;
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

public class CustomerCenter extends Activity implements TextWatcher {

	public static String TAG="StoreOwner_CustomerCenter";

	public static MyHorizontalScrollView scrollView;
	View app;

	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;

	Header header;
	View mLeftMenu;
	int mMenuFlag;
	public int mScreenWidth;
	public double mMenuWidth;
	StoreOwner_LeftMenu storeowner_leftmenu;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;

	private TextView mStoreOwner_CustomerCenter_CusomterListMenu,mStoreOwner_CustomerCenter_CusomterInvisibleMenu,mStoreOwner_CustomerCenter_AddCustomerMenu;
	private LinearLayout mCustomerCenterAddCustomerFields;
	private Button mStoreOwner_CustomerCenter_FreezeView,mStoreOwner_CustomerCenter_AddCustomer_Submit,mStoreOwner_CustomerCenter_customerSearchButton;
	private ViewGroup mCustomerCenterFooter,mCustomerCenterMiddleView,mCustomerCenterCustomerListcontainer,mCustomerCenterAddCustomerContainer;
	private EditText mCustomerCenterCustomerSearchText,mAddCustomerFirstName,mAddCustomerLastName,mAddCustomerEmailAddress;
	private ListView mCustomerCenterCustomerList;
	private CheckBox mAddCustomer_list,mAddCustomer_Individual;
	private TextView mStoreNameText;
	private ArrayList<Object> mSearchedCustomerList,mTempCustomerList;
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
			app = inflater.inflate(R.layout.storeowner_customercenter, null);

			mCustomerCenterFooter = (ViewGroup) app.findViewById(R.id.storeowner_customercenter_footer);
			mCustomerCenterMiddleView = (ViewGroup) app.findViewById(R.id.storeowner_customercenter_middleview);
			mCustomerCenterCustomerListcontainer = (ViewGroup) mCustomerCenterMiddleView.findViewById(R.id.storeowner_customercenter_customerlist_container);
			mCustomerCenterAddCustomerContainer = (ViewGroup) mCustomerCenterMiddleView.findViewById(R.id.storeowner_customercenter_addCustomer_container);
			mStoreOwner_CustomerCenter_FreezeView = (Button) app.findViewById(R.id.storeowner_customercenter_freezeview);
			mStoreNameText = (TextView) app.findViewById(R.id.storeowner_customercenter_storename);
			storeowner_rightmenu = new StoreOwner_RightMenu(CustomerCenter.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_CustomerCenter_FreezeView, TAG);
			mRightMenu = storeowner_rightmenu.intializeCustomerCenterInflater();
			MenuOutClass.STOREOWNER_MENUOUT = false;
			mSearchedCustomerList = new ArrayList<Object>();
			storeowner_leftmenu = new StoreOwner_LeftMenu(CustomerCenter.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_CustomerCenter_FreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();

			storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			storeowner_rightmenu.customercentermenuClickListener(mLeftMenu, mRightMenu);

			/* Header Tab Bar which contains logout,notification and home buttons*/

			header = (Header) app.findViewById(R.id.storeowner_customercenter_header);
			header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_CustomerCenter_FreezeView, TAG);

			final View[] children = new View[] { mLeftMenu, app, mRightMenu };
			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));

			//Call function to get width of the screen
			mScreenWidth=getScreenWidth(); 	
			if(mScreenWidth>0){	//To fix Home Page menubar items width
				mMenuWidth=mScreenWidth/3;
				Log.i(TAG,"ScreenWidth : "+mScreenWidth+"\n"+"MenuItemWidth : "+mMenuWidth);
			}

			// Initialization of customer list views
			mCustomerCenterCustomerSearchText = (EditText) mCustomerCenterCustomerListcontainer.findViewById(R.id.storeowner_customercenter_searchText);
			mCustomerCenterCustomerSearchText.addTextChangedListener(this);
			mStoreOwner_CustomerCenter_customerSearchButton = (Button) mCustomerCenterCustomerListcontainer.findViewById(R.id.storeowner_customercenter_search_buttonId);
			mCustomerCenterCustomerList = (ListView) mCustomerCenterCustomerListcontainer.findViewById(R.id.storeowner_customercenter_ListView);
			// Initialization of Add customer list views

			mAddCustomer_list = (CheckBox) mCustomerCenterAddCustomerContainer.findViewById(R.id.storeowner_customercenter_addCustomer_openlist);
			mAddCustomer_Individual = (CheckBox) mCustomerCenterAddCustomerContainer.findViewById(R.id.storeowner_customercenter_addCustomer_addindividual);
			mCustomerCenterAddCustomerFields = (LinearLayout) mCustomerCenterAddCustomerContainer.findViewById(R.id.customer_center_addcustomerfields);
			mAddCustomerFirstName = (EditText) mCustomerCenterAddCustomerFields.findViewById(R.id.storeowner_customercenter_addCustomer_firstName);
			mAddCustomerLastName = (EditText) mCustomerCenterAddCustomerFields.findViewById(R.id.storeowner_customercenter_addCustomer_lastName);
			mAddCustomerEmailAddress = (EditText) mCustomerCenterAddCustomerFields.findViewById(R.id.storeowner_customercenter_addCustomer_emailAddress);
			mStoreOwner_CustomerCenter_AddCustomer_Submit = (Button) mCustomerCenterAddCustomerFields.findViewById(R.id.storeowner_customercenter_addCustomer_submit);
			mAddCustomer_Individual.setChecked(true);

			// Initialization of footer view
			mStoreOwner_CustomerCenter_CusomterListMenu = (TextView) mCustomerCenterFooter.findViewById(R.id.storeowner_customercenter_footer_customerlist);
			mStoreOwner_CustomerCenter_CusomterListMenu.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
			mStoreOwner_CustomerCenter_CusomterInvisibleMenu = (TextView) mCustomerCenterFooter.findViewById(R.id.storeowner_customercenter_footer_invisible);
			mStoreOwner_CustomerCenter_CusomterInvisibleMenu.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
			mStoreOwner_CustomerCenter_AddCustomerMenu = (TextView) mCustomerCenterFooter.findViewById(R.id.storeowner_customercenter_footer_addcustomer);
			mStoreOwner_CustomerCenter_AddCustomerMenu.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
			mStoreOwner_CustomerCenter_CusomterListMenu.setBackgroundResource(R.drawable.footer_dark_blue_new);

			mTempCustomerList = new ArrayList<Object>();
			// Async task to get customer list
			GetCustomerListTask mGetCustomer = new GetCustomerListTask(CustomerCenter.this,"progress",mCustomerCenterCustomerList,"customer_list");
			mGetCustomer.execute();

			// To set StoreName
			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
			String mStoreName = mPrefs.getString("store_name", "");
			mStoreNameText.setText(mStoreName);
			mStoreOwner_CustomerCenter_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag, mStoreOwner_CustomerCenter_FreezeView, TAG,mCustomerCenterCustomerSearchText,true));
			mCustomerCenterCustomerList.setOnItemClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu,mMenuFlag=2, mStoreOwner_CustomerCenter_FreezeView, TAG,mCustomerCenterCustomerSearchText,false));
			mStoreOwner_CustomerCenter_CusomterListMenu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mStoreOwner_CustomerCenter_CusomterListMenu.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mStoreOwner_CustomerCenter_AddCustomerMenu.setBackgroundResource(R.drawable.header_2);
					mCustomerCenterCustomerListcontainer.setVisibility(View.VISIBLE);
					mCustomerCenterAddCustomerContainer.setVisibility(View.GONE);
				}
			});

			mStoreOwner_CustomerCenter_AddCustomerMenu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mStoreOwner_CustomerCenter_AddCustomerMenu.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mStoreOwner_CustomerCenter_CusomterListMenu.setBackgroundResource(R.drawable.header_2);
					mCustomerCenterCustomerListcontainer.setVisibility(View.GONE);
					mCustomerCenterAddCustomerContainer.setVisibility(View.VISIBLE);
					mAddCustomerFirstName.getText().clear();
					mAddCustomerLastName.getText().clear();
					mAddCustomerEmailAddress.getText().clear();
					mAddCustomer_Individual.setChecked(true);
				}
			});

			mAddCustomer_list.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						mAddCustomer_Individual.setChecked(false);
						mCustomerCenterAddCustomerFields.setVisibility(View.GONE);	
					}else{

					}

				}
			});

			mAddCustomer_Individual.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						mAddCustomer_list.setChecked(false);
						mCustomerCenterAddCustomerFields.setVisibility(View.VISIBLE);
					}else{

					}
				}
			});

			mStoreOwner_CustomerCenter_customerSearchButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mCustomerCenterCustomerSearchText.removeTextChangedListener(CustomerCenter.this);
					mCustomerCenterCustomerSearchText.getText().clear();
					mCustomerCenterCustomerSearchText.addTextChangedListener(CustomerCenter.this);
					mCustomerCenterCustomerList.setAdapter(new CustomFavouriteUserAdapter(CustomerCenter.this, mTempCustomerList));
				}
			});

			mStoreOwner_CustomerCenter_AddCustomer_Submit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mAddCustomerFirstName.getText().toString().trim().length() == 0){
						alertBox_service("Information", "Please enter first name", mAddCustomerFirstName);
					}else if(mAddCustomerLastName.getText().toString().trim().length() == 0){
						alertBox_service("Information", "Please enter last name", mAddCustomerLastName);
					}else if(mAddCustomerEmailAddress.getText().toString().trim().length() == 0){
						alertBox_service("Information", "Please enter customer email address", mAddCustomerEmailAddress);
					}else if(!isValidEmail(mAddCustomerEmailAddress.getText().toString())){
						alertBox_service("Information", "Please enter valid email address",mAddCustomerEmailAddress);
					}else{
						GetCustomerListTask mGetCustomer = new GetCustomerListTask(CustomerCenter.this,"progress",mCustomerCenterCustomerList,"add_customer");
						mGetCustomer.execute(mAddCustomerFirstName.getText().toString(),mAddCustomerLastName.getText().toString(),mAddCustomerEmailAddress.getText().toString());
					}
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateViews(ArrayList<Object> result, String mEventFlag) {
		if(mEventFlag.equalsIgnoreCase("customer_list")){
			// Assigning for use in searching user name
			mTempCustomerList = result;
			mCustomerCenterCustomerList.setAdapter(new CustomFavouriteUserAdapter(CustomerCenter.this, result));
		}else{
			String message = (String) result.get(0);
			if(message.equalsIgnoreCase("User Added to Customer List")){
				alertBox_service("Information","Customer has been added successfully", null);
			}else{
				alertBox_service("Information",message, null);
			}
		}
	}

	/*Get Screen width*/
	public int getScreenWidth(){
		int Measuredwidth = 0;  
		int Measuredheight = 0;  
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Measuredwidth = display.getWidth(); 
		Measuredheight = display.getHeight();
		return Measuredwidth;
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
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(CustomerCenter.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(CustomerCenter.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(CustomerCenter.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("restart", "true");
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
		mCustomerCenterCustomerSearchText.setFocusable(true);
		mCustomerCenterCustomerSearchText.setFocusableInTouchMode(true);
		mCustomerCenterCustomerList.setVisibility(View.VISIBLE);

	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	public void afterTextChanged(Editable s) {}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		try{
			String searchString=s.toString();
			mSearchedCustomerList.clear();
			for(int i=0;i<mTempCustomerList.size();i++)
			{
				FavouriteCustomerDetails customerdetails = (FavouriteCustomerDetails) mTempCustomerList.get(i);

				if(customerdetails.mCustomerName.toLowerCase().contains(searchString.toLowerCase())){
					mSearchedCustomerList.add(customerdetails);
				} 
			}
			mCustomerCenterCustomerList.setAdapter(new CustomFavouriteUserAdapter(CustomerCenter.this, mSearchedCustomerList));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected final static boolean isValidEmail(String mailid){
		try{
			Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
					"\\@" +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
					"(" +
					"\\." +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
					")+");
			//return android.util.Patterns.EMAIL_ADDRESS.matcher(mailid).matches();
			return EMAIL_ADDRESS_PATTERN.matcher(mailid).matches();
		}catch(Exception e){
			return false;
		}
	}

	private void alertBox_service(String title, final String msg,final EditText editText) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(CustomerCenter.this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setCancelable(true);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(editText != null)
					editText.requestFocus();
				if(msg.equalsIgnoreCase("Customer has been added successfully")){
					mStoreOwner_CustomerCenter_AddCustomerMenu.setBackgroundResource(R.drawable.header_2);
					mStoreOwner_CustomerCenter_CusomterListMenu.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mCustomerCenterCustomerListcontainer.setVisibility(View.VISIBLE);
					mCustomerCenterAddCustomerContainer.setVisibility(View.GONE);
					mTempCustomerList = new ArrayList<Object>();
					// Async task to get customer list
					GetCustomerListTask mGetCustomer = new GetCustomerListTask(CustomerCenter.this,"progress",mCustomerCenterCustomerList,"customer_list");
					mGetCustomer.execute();
				}
			}
		});
		service_alert.show();
	}

	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(CustomerCenter.this);
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