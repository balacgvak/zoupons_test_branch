package com.us.zoupons.storeowner.Employees;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
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
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.cards.ImageLoader;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.AddEmployee.EmployeePermissionClassVariables;
import com.us.zoupons.storeowner.AddEmployee.StoreEmployeePermissionListAdapter;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class StoreOwner_EmployeeDetails extends Activity {

	public static String TAG="StoreOwner_EmployeeDetails";
	
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
	
	Button mStoreOwner_EmployeeDetails_FreezeView;
	ListView mStoreOwner_EmployeeLocationPermission_ListView,mStoreOwner_EmployeeModulePermission_ListView;
	
	LinearLayout mStoreOwner_EmployeeDetails_Back,mStoreOwner_EmployeeDetails_Permission,mStoreOwner_EmployeeDetails_InActivateEmployee;
	LinearLayout mStoreOwner_EmployeePermissions_Back,mStoreOwner_EmployeePermissions_Save,mStoreOwner_EmployeePermissions_Splitter3;
	Button mStoreOwner_EmployeeDetails_Save,mStoreOwner_EmployeeDetails_ModulePermission;
	
	StoreOwnerEmployeeDetails_Adapter mChoosedLocation_ListAdapter;
	ImageView mEmployeeProfileImage;
	EditText mStoreEmployeeFirstName,mStoreEmployeeLastName,mStoreEmployeeEmail,mStoreEmployeePhone;
	private String EmployeeId="",mChoosedModules="",mChoosedLocations="";
	private ViewGroup mEmployeeDetailsMiddleView,mEmployeeDetailsFooterView,mEmployeePermissionMiddleView,mEmployeeLocationPermissionMiddleView,mEmployeeModulePermissionMiddleView,mEmployeePermissionFooterView;
	private ArrayList<Object> mStorePermissionList,mLocationPermissionList;
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
		mStorePermissionList = new ArrayList<Object>();
		mLocationPermissionList = new ArrayList<Object>();
		app = inflater.inflate(R.layout.storeowner_employeedetails, null);
		// Initialising views and view groups
		mEmployeeDetailsMiddleView = (ViewGroup) app.findViewById(R.id.employeedetails_employeeprofile);
		mEmployeeDetailsFooterView = (ViewGroup) app.findViewById(R.id.storeowner_employeedetails_default_footer);
		mEmployeePermissionMiddleView = (ViewGroup) app.findViewById(R.id.employeedetails_permission_container);
		mEmployeeLocationPermissionMiddleView = (ViewGroup) mEmployeePermissionMiddleView.findViewById(R.id.employeedetails_employeelocation_permission);
		mEmployeeModulePermissionMiddleView = (ViewGroup) mEmployeePermissionMiddleView.findViewById(R.id.employeedetails_employeemodule_permission);
		mEmployeePermissionFooterView = (ViewGroup) app.findViewById(R.id.storeowner_employeedetails_permission_footer);
		mStoreOwner_EmployeeDetails_FreezeView = (Button) app.findViewById(R.id.storeowner_employeedetails_freeze);
		mStoreOwner_EmployeeLocationPermission_ListView = (ListView) mEmployeePermissionMiddleView.findViewById(R.id.employeedetails_multiselectlistview);
		mStoreOwner_EmployeeModulePermission_ListView = (ListView) mEmployeePermissionMiddleView.findViewById(R.id.employeepermissiondetails_multiselectlistview);
		mStoreEmployeeFirstName = (EditText) mEmployeeDetailsMiddleView.findViewById(R.id.employeedetails_edtFirstName);
		mStoreEmployeeLastName = (EditText) mEmployeeDetailsMiddleView.findViewById(R.id.employeedetails_edtLastName);
		mStoreEmployeeEmail = (EditText) mEmployeeDetailsMiddleView.findViewById(R.id.employeedetails_edtEmail);
		mStoreEmployeePhone = (EditText) mEmployeeDetailsMiddleView.findViewById(R.id.employeedetails_mobilenumber_value);
		// To validate mobile number and setting the limit...
		mStoreEmployeePhone.addTextChangedListener(new MobileNumberTextWatcher());
		mStoreEmployeePhone.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});
		mEmployeeProfileImage = (ImageView) mEmployeeDetailsMiddleView.findViewById(R.id.employeedetails_profileimage);
		/* Default Footer View*/
		mStoreOwner_EmployeeDetails_Back = (LinearLayout) mEmployeeDetailsFooterView.findViewById(R.id.storeowner_employeedetails_default_footer_back);
		mStoreOwner_EmployeeDetails_Permission = (LinearLayout) mEmployeeDetailsFooterView.findViewById(R.id.storeowner_employeedetails_default_footer_permission);
		mStoreOwner_EmployeeDetails_InActivateEmployee = (LinearLayout) mEmployeeDetailsFooterView.findViewById(R.id.storeowner_employeedetails_default_footer_inactiveemployee);
	    /* Employee Permission Footer View*/
		mStoreOwner_EmployeePermissions_Back = (LinearLayout) mEmployeePermissionFooterView.findViewById(R.id.storeowner_employeedetails_permission_footer_back);
		mStoreOwner_EmployeePermissions_Splitter3 = (LinearLayout) mEmployeePermissionFooterView.findViewById(R.id.menubar_splitter3);
		mStoreOwner_EmployeePermissions_Save = (LinearLayout) mEmployeePermissionFooterView.findViewById(R.id.employeedetails_permission_footer_save);
		mStoreOwner_EmployeeDetails_Save = (Button) mEmployeeDetailsMiddleView.findViewById(R.id.employeedetails_Save);
		mStoreOwner_EmployeeDetails_ModulePermission = (Button) mEmployeeLocationPermissionMiddleView.findViewById(R.id.employeedetails_employeelocation_modulepermission); 
		storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwner_EmployeeDetails.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_EmployeeDetails_FreezeView, TAG);
	    mRightMenu = storeowner_rightmenu.intializeInflater();
	    storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_EmployeeDetails.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_EmployeeDetails_FreezeView, TAG);
	    mLeftMenu = storeowner_leftmenu.intializeInflater();
	    storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
	    storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
	    /* Header Tab Bar which contains logout,notification and home buttons*/
	    header = (Header) app.findViewById(R.id.storeowner_employeedetails_header);
	    header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_EmployeeDetails_FreezeView, TAG);
	    final View[] children = new View[] { mLeftMenu, app };
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
		mStoreOwner_EmployeeDetails_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_EmployeeDetails_FreezeView, TAG));
				
		if(getIntent().hasExtra("choosed_position")){ // To set selected employee details
			StoreEmployeesClassVariables mEmployeeDetails = (StoreEmployeesClassVariables) WebServiceStaticArrays.StoreEmloyeesList.get(getIntent().getExtras().getInt("choosed_position"));
			EmployeeId = mEmployeeDetails.mEmployeeId;
			mStoreEmployeeFirstName.setText(mEmployeeDetails.mEmployeeFirstName);
			mStoreEmployeeLastName.setText(mEmployeeDetails.mEmployeeLastName);
			mStoreEmployeeEmail.setText(mEmployeeDetails.mEmailAddress);
			mStoreEmployeePhone.setText(mEmployeeDetails.mMobileNumber.substring(0, 3)+"-"+mEmployeeDetails.mMobileNumber.substring(3, 6)+"-"+mEmployeeDetails.mMobileNumber.substring(6, 10));
			ImageLoader mImageLoader = new ImageLoader(StoreOwner_EmployeeDetails.this);
			mImageLoader.DisplayImage(mEmployeeDetails.mEmployeeProfileImage,mEmployeeProfileImage);
		}else{
			
		}		
		
		mStoreOwner_EmployeeDetails_Back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mStoreOwner_EmployeeDetails_Permission.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(new NetworkCheck().ConnectivityCheck(StoreOwner_EmployeeDetails.this)){
				Get_SetEmployeePermissionTask mGetPermissionTask = new Get_SetEmployeePermissionTask(StoreOwner_EmployeeDetails.this,"get", EmployeeId);
				mGetPermissionTask.execute();
				}else{
					Toast.makeText(StoreOwner_EmployeeDetails.this, "No Network Connection", Toast.LENGTH_SHORT).show();	
				}
			}
		});
		
		mStoreOwner_EmployeeDetails_InActivateEmployee.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwner_EmployeeDetails.this);
				service_alert.setTitle("Information");
				service_alert.setMessage("Are you sure want to deactivate employee");
				service_alert.setNegativeButton("cancel", null);
				service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if(new NetworkCheck().ConnectivityCheck(StoreOwner_EmployeeDetails.this)){
							InActivateEmployeeTask mInactivateEmployee = new InActivateEmployeeTask(StoreOwner_EmployeeDetails.this, EmployeeId);
							mInactivateEmployee.execute();
						}else{
							Toast.makeText(StoreOwner_EmployeeDetails.this, "No Network Connection", Toast.LENGTH_SHORT).show();
						}
					}
				});
				
				service_alert.show();
			}
		});
		
		mStoreOwner_EmployeePermissions_Back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mEmployeeLocationPermissionMiddleView.getVisibility()==View.VISIBLE){
					mEmployeeDetailsMiddleView.setVisibility(View.VISIBLE);
					mEmployeeDetailsFooterView.setVisibility(View.VISIBLE);
					mEmployeePermissionMiddleView.setVisibility(View.GONE);
					mEmployeePermissionFooterView.setVisibility(View.INVISIBLE);
					mStoreOwner_EmployeePermissions_Splitter3.setVisibility(View.INVISIBLE);
					mStoreOwner_EmployeePermissions_Save.setVisibility(View.INVISIBLE);
				}else{
					mStoreOwner_EmployeePermissions_Splitter3.setVisibility(View.INVISIBLE);
					mStoreOwner_EmployeePermissions_Save.setVisibility(View.INVISIBLE);
					mEmployeeModulePermissionMiddleView.setVisibility(View.GONE);
					mEmployeeLocationPermissionMiddleView.setVisibility(View.VISIBLE);
				}
			}
		});
		
		mStoreOwner_EmployeeDetails_ModulePermission.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mEmployeeLocationPermissionMiddleView.getVisibility()==View.VISIBLE){
					// To get selected location permissions
					ArrayList<EmployeePermissionClassVariables> selectedLocations = new ArrayList<EmployeePermissionClassVariables>();
					final SparseBooleanArray checkedItems = mStoreOwner_EmployeeLocationPermission_ListView.getCheckedItemPositions();
					int checkedItemsCount = checkedItems.size();
					for (int i = 0; i < checkedItemsCount; ++i) {
						// Item position in adapter
						int position = checkedItems.keyAt(i);
						if(checkedItems.valueAt(i))
							selectedLocations.add((EmployeePermissionClassVariables) mStoreOwner_EmployeeLocationPermission_ListView.getAdapter().getItem(position));
					}
					for(int i=0;i<selectedLocations.size();i++){
						EmployeePermissionClassVariables mObj = selectedLocations.get(i);
						mChoosedLocations += mObj.mLocationPermissionId;
						if(i!=selectedLocations.size()-1){
							mChoosedLocations+=",";
						}
					}
					if(!mChoosedLocations.equalsIgnoreCase("")){
						mEmployeeLocationPermissionMiddleView.setVisibility(View.GONE);
						mEmployeeModulePermissionMiddleView.setVisibility(View.VISIBLE);
						mStoreOwner_EmployeePermissions_Splitter3.setVisibility(View.VISIBLE);
						mStoreOwner_EmployeePermissions_Save.setVisibility(View.VISIBLE);
						mStoreOwner_EmployeeModulePermission_ListView.setAdapter(new StoreEmployeePermissionListAdapter(StoreOwner_EmployeeDetails.this,R.layout.chooselocations_listrrow,mStorePermissionList));
						// To set permission already authorized by store owner...
						String[] mSelectedModulePermissionsId =  EmployeePermissionClassVariables.mStorePermissionIds.split(",");
						for(int i=0;i<mStorePermissionList.size();i++){
							EmployeePermissionClassVariables mLocationPermissions = (EmployeePermissionClassVariables) mStorePermissionList.get(i);
							for(int j =0; j < mSelectedModulePermissionsId.length; j++)
					        {
					            if(mLocationPermissions.mPermissionid.equalsIgnoreCase(mSelectedModulePermissionsId[j]))
					            {
					            	mStoreOwner_EmployeeModulePermission_ListView.setItemChecked(i, true); 
					            }
					        }
						}
					}else{
						AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwner_EmployeeDetails.this);
						service_alert.setTitle("Information");
						service_alert.setMessage("Please choose any location for employee to access");
						service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						service_alert.show();
					}
					
					
				}
			}
		});
		
		mStoreOwner_EmployeePermissions_Save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// To get selected module permissions
				ArrayList<EmployeePermissionClassVariables> selectedModules = new ArrayList<EmployeePermissionClassVariables>();
				final SparseBooleanArray checkedItems = mStoreOwner_EmployeeModulePermission_ListView.getCheckedItemPositions();
				int checkedItemsCount = checkedItems.size();
				for (int i = 0; i < checkedItemsCount; ++i) {
					// Item position in adapter
					int position = checkedItems.keyAt(i);
					// Add team if item is checked == TRUE!
					if(checkedItems.valueAt(i))
						selectedModules.add((EmployeePermissionClassVariables) mStoreOwner_EmployeeModulePermission_ListView.getAdapter().getItem(position));
				}
				for(int i=0;i<selectedModules.size();i++){
					EmployeePermissionClassVariables mObj = selectedModules.get(i);
					mChoosedModules += mObj.mPermissionid;
					if(i!=selectedModules.size()-1){
						mChoosedModules+=",";
					}
				}
				if(new NetworkCheck().ConnectivityCheck(StoreOwner_EmployeeDetails.this)){
					if(!mChoosedModules.equalsIgnoreCase("")){
						Get_SetEmployeePermissionTask mSetPermissionTask = new Get_SetEmployeePermissionTask(StoreOwner_EmployeeDetails.this, "set", EmployeeId,mChoosedModules,mChoosedLocations);
						mSetPermissionTask.execute();	
					}else{
						AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwner_EmployeeDetails.this);
						service_alert.setTitle("Information");
						service_alert.setMessage("Please choose any module for employee to access");
						service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						service_alert.show();
					}

				}else{
					Toast.makeText(StoreOwner_EmployeeDetails.this, "No Network Connection", Toast.LENGTH_SHORT).show();
				}
                
			}
		});
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateViews(ArrayList<Object> result, String mEventFlag) {
		// TODO Auto-generated method stub
		try{
			if(mEventFlag.equalsIgnoreCase("get")){
				mStorePermissionList =  new ArrayList<Object>();
				mLocationPermissionList = new ArrayList<Object>();
				mEmployeeDetailsMiddleView.setVisibility(View.GONE);
				mEmployeeDetailsFooterView.setVisibility(View.INVISIBLE);
				mEmployeePermissionMiddleView.setVisibility(View.VISIBLE);
				mEmployeePermissionFooterView.setVisibility(View.VISIBLE);
				mEmployeeLocationPermissionMiddleView.setVisibility(View.VISIBLE);
				mEmployeeModulePermissionMiddleView.setVisibility(View.GONE);
				if(EmployeePermissionClassVariables.mStorePermissionTotalIds !=0){ // based upon EmployeePermissionClassVariables.mStorePermissionTotalIds total splitting array to module and location permissions 
					for(int i=0;i<result.size() ;i++){
						if(i<EmployeePermissionClassVariables.mStorePermissionTotalIds){ // For store Permission list
							mStorePermissionList.add(result.get(i));	
						}else{ // For location permission list
							mLocationPermissionList.add(result.get(i));
						}
					}
				}
				
				mStoreOwner_EmployeeLocationPermission_ListView.setAdapter(new StoreOwnerEmployeeDetails_Adapter(StoreOwner_EmployeeDetails.this,R.layout.chooselocations_listrrow,mLocationPermissionList));
				// To set location permission.....
				String[] mSelectedLocationPermissionsId =  EmployeePermissionClassVariables.mLocationPermissionIds.split(",");
				for(int i=0;i<mLocationPermissionList.size();i++){
					EmployeePermissionClassVariables mLocationPermissions = (EmployeePermissionClassVariables) mLocationPermissionList.get(i);
					for(int j =0; j < mSelectedLocationPermissionsId.length; j++)
					{
						if(mLocationPermissions.mLocationPermissionId.equalsIgnoreCase(mSelectedLocationPermissionsId[j]))
						{
							mStoreOwner_EmployeeLocationPermission_ListView.setItemChecked(i, true); 
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
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
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_EmployeeDetails.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwner_EmployeeDetails.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwner_EmployeeDetails.this);
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
		mStoreOwner_EmployeeLocationPermission_ListView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_EmployeeDetails.this);
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
