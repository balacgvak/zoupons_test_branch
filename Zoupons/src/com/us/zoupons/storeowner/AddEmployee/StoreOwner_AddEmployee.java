package com.us.zoupons.storeowner.AddEmployee;

import java.util.ArrayList;

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
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.MobileNumberFilter;
import com.us.zoupons.MobileNumberTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.ClassVariables.POJOUserProfile;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.cards.ImageLoader;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.Employees.StoreOwnerEmployeeDetails_Adapter;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;
import com.us.zoupons.storeowner.HomePage.StoreOwnerHomePageAsynchTask;

public class StoreOwner_AddEmployee extends Activity {

	public static String TAG="StoreOwner_AddEmployee";
	
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
	
	Button mStoreOwner_AddEmployee_FreezeView;
	ListView mStoreOwner_AddEmployee_ListView,mStoreOwner_AddPermissionListView;
	private ViewGroup mAddEmployeeMobileNumber,mAddEmployeeEmployeeCode,mAddEmployeeModulePermission,mAddEmployeeLocationPermission;
	LinearLayout mStoreOwner_AddEmployee_Back;
	Button mStoreOwner_AddEmployee_MobileNumber_Submit,mStoreOwner_AddEmployee_EmployeeCode_Submit,mStoreOwner_AddEmployee_ModulePermission_LocationPermissions,mStoreOwner_AddEmployee_LocationPermission_ActivatePermissions;
	private ArrayList<Object> mStoreLocations,mEmployeePermissionList;
	private EditText mTelephoneNumber,mStoreEmployeeFirstName,mStoreEmployeeLastName,mStoreEmployeeCode;
	private ImageView mStoreEmployeeProfileImage;
	private String mEmployeeUserId="",mChoosedModules="",mChoosedLocations="";
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
		mStoreLocations = new ArrayList<Object>();
		mEmployeePermissionList = new ArrayList<Object>();
		app = inflater.inflate(R.layout.storeowner_addemployee, null);
		// Initialising view and view groups
		mAddEmployeeMobileNumber = (ViewGroup) app.findViewById(R.id.addemployee_employeemobilenumber);
		mAddEmployeeEmployeeCode = (ViewGroup) app.findViewById(R.id.addemployee_employeecode_container);
		mAddEmployeeModulePermission = (ViewGroup) app.findViewById(R.id.addemployee_employeemodule_permission);
		mAddEmployeeLocationPermission = (ViewGroup) app.findViewById(R.id.addemployee_employeelocation_permission);
		ViewGroup mAddEmployeeFooter = (ViewGroup) app.findViewById(R.id.storeowner_addemployee_footer);
		mStoreOwner_AddEmployee_FreezeView = (Button) app.findViewById(R.id.storeowner_addemployee_freeze);
		mStoreOwner_AddEmployee_ListView = (ListView) mAddEmployeeLocationPermission.findViewById(R.id.addemployee_employeelocation_multiselectlistview);
		mStoreOwner_AddEmployee_Back =(LinearLayout) mAddEmployeeFooter.findViewById(R.id.storeowner_addemployee_permission_footer_back);
		mTelephoneNumber = (EditText) mAddEmployeeMobileNumber.findViewById(R.id.addemployee_employeemobilenumber_value);
		mStoreOwner_AddEmployee_MobileNumber_Submit= (Button) mAddEmployeeMobileNumber.findViewById(R.id.employeemobilenumber_submit);
		mStoreEmployeeFirstName = (EditText) mAddEmployeeEmployeeCode.findViewById(R.id.addemployee_edtFirstName);
		mStoreEmployeeLastName = (EditText) mAddEmployeeEmployeeCode.findViewById(R.id.addemployee_edtLastName);
		mStoreEmployeeProfileImage = (ImageView) mAddEmployeeEmployeeCode.findViewById(R.id.addemployee_profileimage);
		mStoreEmployeeCode = (EditText) mAddEmployeeEmployeeCode.findViewById(R.id.addemployee_employeecode_value);
		mStoreEmployeeCode.setInputType(InputType.TYPE_CLASS_NUMBER);
		mStoreOwner_AddEmployee_EmployeeCode_Submit= (Button) mAddEmployeeEmployeeCode.findViewById(R.id.employeecode_submit);
		mStoreOwner_AddPermissionListView = (ListView) mAddEmployeeModulePermission.findViewById(R.id.addemployee_employeePermission_multiselectlistview);
		mStoreOwner_AddEmployee_ModulePermission_LocationPermissions= (Button) mAddEmployeeModulePermission.findViewById(R.id.addemployee_employeemodule_locationpermission);
		mStoreOwner_AddEmployee_LocationPermission_ActivatePermissions= (Button) mAddEmployeeLocationPermission.findViewById(R.id.addemployee_employeelocation_locationpermission);
		storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwner_AddEmployee.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_AddEmployee_FreezeView, TAG);
	    mRightMenu = storeowner_rightmenu.intializeInflater();
	    storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_AddEmployee.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_AddEmployee_FreezeView, TAG);
	    mLeftMenu = storeowner_leftmenu.intializeInflater();
	    storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
	    storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
	    /* Header Tab Bar which contains logout,notification and home buttons*/
	    header = (Header) app.findViewById(R.id.storeowner_addemployee_header);
	    header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_AddEmployee_FreezeView, TAG);
	    header.mTabBarNotificationContainer.setVisibility(View.VISIBLE);
	    //header.mTabBarLoginChoice.setVisibility(View.VISIBLE);
	    final View[] children = new View[] { mLeftMenu, app };
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
		mStoreOwner_AddEmployee_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_AddEmployee_FreezeView, TAG));
		// To validate mobile number and setting the limit...
		mTelephoneNumber.addTextChangedListener(new MobileNumberTextWatcher());
		mTelephoneNumber.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});
		// To fetch store id from preferences
		SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
		String mStoreId = mPrefs.getString("store_id", "");
		StoreOwnerHomePageAsynchTask mStoreLocationsTask = new StoreOwnerHomePageAsynchTask(StoreOwner_AddEmployee.this, "PROGRESS");
		mStoreLocationsTask.execute(mStoreId,"");
		
		mStoreOwner_AddEmployee_Back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mAddEmployeeMobileNumber.getVisibility()==View.VISIBLE){
					finish();
				}else if(mAddEmployeeEmployeeCode.getVisibility()==View.VISIBLE){
					mAddEmployeeEmployeeCode.setVisibility(View.GONE);
					mAddEmployeeMobileNumber.setVisibility(View.VISIBLE);
				}else if(mAddEmployeeModulePermission.getVisibility()==View.VISIBLE){
					mAddEmployeeModulePermission.setVisibility(View.GONE);
					mAddEmployeeEmployeeCode.setVisibility(View.VISIBLE);
				}else if(mAddEmployeeLocationPermission.getVisibility()==View.VISIBLE){
					mAddEmployeeLocationPermission.setVisibility(View.GONE);
					mAddEmployeeModulePermission.setVisibility(View.VISIBLE);
				}
			}
		});
		
		mStoreOwner_AddEmployee_MobileNumber_Submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mTelephoneNumber.getText().toString().trim().length() == 0){
					alertBox_service("Information","Please enter mobile number",mTelephoneNumber);
				}else if(mTelephoneNumber.getText().toString().trim().length() != 12){
					alertBox_service("Information","Please enter valid mobile number",mTelephoneNumber);						
				}else{
					if(new NetworkCheck().ConnectivityCheck(StoreOwner_AddEmployee.this)){
					Add_EmployeeTask mEmployeeTask = new Add_EmployeeTask(StoreOwner_AddEmployee.this,"check_employee",mTelephoneNumber.getText().toString());
					mEmployeeTask.execute();
					}else{
						Toast.makeText(StoreOwner_AddEmployee.this, "No Network Connection", Toast.LENGTH_SHORT).show();	
					}
				}
				
			}
		});
		
		mStoreOwner_AddEmployee_EmployeeCode_Submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mStoreEmployeeCode.getText().toString().equalsIgnoreCase("")){
					alertBox_service("Information", "Please enter employee code received in mail", mStoreEmployeeCode);
				}else{
					if(new NetworkCheck().ConnectivityCheck(StoreOwner_AddEmployee.this)){
						Add_EmployeeTask mEmployeeTask = new Add_EmployeeTask(StoreOwner_AddEmployee.this,"verify_employeecode",mStoreEmployeeCode.getText().toString(),mEmployeeUserId);
						mEmployeeTask.execute();
					}else{
						Toast.makeText(StoreOwner_AddEmployee.this, "No Network Connection", Toast.LENGTH_SHORT).show();		
					}
				}
			}
		});
		
		mStoreOwner_AddEmployee_ModulePermission_LocationPermissions.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/* To get checked store module permissions */
				ArrayList<EmployeePermissionClassVariables> selectedModules = new ArrayList<EmployeePermissionClassVariables>();
				final SparseBooleanArray checkedItems = mStoreOwner_AddPermissionListView.getCheckedItemPositions();
				int checkedItemsCount = checkedItems.size();
				for (int i = 0; i < checkedItemsCount; ++i) {
					// Item position in adapter
					int position = checkedItems.keyAt(i);
					if(checkedItems.valueAt(i))
						selectedModules.add((EmployeePermissionClassVariables) mStoreOwner_AddPermissionListView.getAdapter().getItem(position));
				}
				/* To format string of modules seperated by comma..*/
				for(int i=0;i<selectedModules.size();i++){
					EmployeePermissionClassVariables mObj = selectedModules.get(i);
					mChoosedModules += mObj.mPermissionid;
					if(i!=selectedModules.size()-1){
						mChoosedModules+=",";
					}
				}
				if(!mChoosedModules.equalsIgnoreCase("")){
					mAddEmployeeModulePermission.setVisibility(View.GONE);
					mAddEmployeeLocationPermission.setVisibility(View.VISIBLE);
					mStoreOwner_AddEmployee_ListView.setAdapter(new StoreOwnerEmployeeDetails_Adapter(StoreOwner_AddEmployee.this,R.layout.chooselocations_listrrow,mStoreLocations));	
				}else{
					alertBox_service("Information","Please choose any modules for employee to access", null);
				}
				
			}
		});
		
		mStoreOwner_AddEmployee_LocationPermission_ActivatePermissions.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// To get checked location permission list
				ArrayList<POJOStoreInfo> selectedLocations = new ArrayList<POJOStoreInfo>();
				final SparseBooleanArray checkedItems = mStoreOwner_AddEmployee_ListView.getCheckedItemPositions();
				int checkedItemsCount = checkedItems.size();
				for (int i = 0; i < checkedItemsCount; ++i) {
					// Item position in adapter
					int position = checkedItems.keyAt(i);
					if(checkedItems.valueAt(i))
						selectedLocations.add((POJOStoreInfo) mStoreOwner_AddEmployee_ListView.getAdapter().getItem(position));
				}
				// To format string of location id seperated by comma..
				for(int i=0;i<selectedLocations.size();i++){
					POJOStoreInfo mObj = selectedLocations.get(i);
					mChoosedLocations += mObj.location_id;
					if(i!=selectedLocations.size()-1){
						mChoosedLocations+=",";
					}
				}
				
				if(new NetworkCheck().ConnectivityCheck(StoreOwner_AddEmployee.this)){
					if(!mChoosedLocations.equalsIgnoreCase("")){
						Add_EmployeeTask mEmployeeTask = new Add_EmployeeTask(StoreOwner_AddEmployee.this,"activate_employee",mEmployeeUserId,mChoosedModules,mChoosedLocations);
						mEmployeeTask.execute();	
					}else{
						alertBox_service("Information","Please choose any location for employee to access", null);
					}
					
				}else{
					Toast.makeText(StoreOwner_AddEmployee.this, "No Network Connection", Toast.LENGTH_SHORT).show();
				}
			}
		});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void updateViews(ArrayList<Object> result, String mEventFlag) {
		if(mEventFlag.equalsIgnoreCase("check_employee")){ // For check for employee
			POJOUserProfile mUserDetails = (POJOUserProfile) result.get(0);
			mStoreEmployeeFirstName.setText(mUserDetails.mUserFirstName);
			mStoreEmployeeLastName.setText(mUserDetails.mUserLastName);
			ImageLoader mProfileImageLoader = new ImageLoader(StoreOwner_AddEmployee.this);
			mProfileImageLoader.DisplayImage(mUserDetails.mUserImage, mStoreEmployeeProfileImage);
			mAddEmployeeMobileNumber.setVisibility(View.GONE);
			mAddEmployeeEmployeeCode.setVisibility(View.VISIBLE);
			// Assiging user id
			mEmployeeUserId = mUserDetails.mUserId;
		}else if(mEventFlag.equalsIgnoreCase("verify_employeecode")){ // For Verify employee code
			mEmployeePermissionList = result;
			mStoreOwner_AddPermissionListView.setAdapter(new StoreEmployeePermissionListAdapter(StoreOwner_AddEmployee.this,R.layout.chooselocations_listrrow,mEmployeePermissionList));
			mAddEmployeeEmployeeCode.setVisibility(View.GONE);
			mAddEmployeeModulePermission.setVisibility(View.VISIBLE);
			
		}else{

		}

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mReceiver);
		if(mNotificationSync!=null){
			mNotificationSync.cancelAlarm();
		}
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_AddEmployee.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwner_AddEmployee.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwner_AddEmployee.this);
		mLogoutSession.setLogoutTimerAlarm();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwner_AddEmployee_ListView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

    // Function to set store location details to local array list
	public void SetStoreLocationsArray(ArrayList<Object> result) {
		// TODO Auto-generated method stub
		mStoreLocations = result;
	}
	
	private void alertBox_service(String title, final String msg,final EditText editText) {
		// TODO Auto-generated method stub
		AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwner_AddEmployee.this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setCancelable(true);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(editText != null)
				 editText.requestFocus();
			}
		});
		service_alert.show();
	}

	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_AddEmployee.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}
	
	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
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
