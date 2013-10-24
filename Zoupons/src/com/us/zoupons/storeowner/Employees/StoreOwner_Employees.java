package com.us.zoupons.storeowner.Employees;

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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.us.zoupons.storeowner.AddEmployee.StoreOwner_AddEmployee;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class StoreOwner_Employees extends Activity {

	public static String TAG="StoreOwner_Employees";

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
	Button mStoreOwner_Employees_FreezeView;
	ListView mStoreOwner_Employees_ListView;
	LinearLayout mStoreOwner_Employees_Back,mStoreOwner_Employees_Add;
	public static boolean mIsLoadMore;
	public static String mEmployeeStart="0",mEmployeeEndLimit="20",mEmployeeTotalCount = "0";	
	public View mFooterLayout;
	public TextView mFooterText;
	StoreOwnerEmployees_Adapter mStoreOwnerEmployees_Adapter;
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
			app = inflater.inflate(R.layout.storeowner_employees, null);
			ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.storeowneremployees_middleview);
			ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeowneremployees_footer);
			mStoreOwner_Employees_FreezeView = (Button) app.findViewById(R.id.storeowneremployees_freezeview);
			mStoreOwner_Employees_ListView = (ListView) mMiddleView.findViewById(R.id.storeowneremployees_ListView);
			mStoreOwner_Employees_Back = (LinearLayout) mFooterView.findViewById(R.id.storeowneremployees_footer_back);
			mStoreOwner_Employees_Add = (LinearLayout) mFooterView.findViewById(R.id.storeowneremployees_footer_add); 
			mStoreNameText = (TextView) app.findViewById(R.id.storeowner_employee_storename);
			mRightMenuHolder = (ImageView) app.findViewById(R.id.storeowner_employee_rightmenu);
			storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwner_Employees.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_Employees_FreezeView, TAG);
			mRightMenu = storeowner_rightmenu.intializeInflater();
			storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_Employees.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Employees_FreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();
			storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeowneremployees_header);
			header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Employees_FreezeView, TAG);

			final View[] children = new View[] { mLeftMenu, app ,mRightMenu};
			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
			mStoreOwner_Employees_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_Employees_FreezeView, TAG));
			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwner_Employees_FreezeView, TAG));
			mIsLoadMore = false;
			mEmployeeStart="0";
			mEmployeeEndLimit="20";
			mEmployeeTotalCount = "0";
			LayoutInflater mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
			mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
			mStoreOwner_Employees_ListView.addFooterView(mFooterLayout);
			// To set StoreName
			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
			String mStoreName = mPrefs.getString("store_name", "");
			mStoreNameText.setText(mStoreName);		
			StoreOwner_EmployeesAsyncTask mStoreOwnerEmployees = new StoreOwner_EmployeesAsyncTask(StoreOwner_Employees.this,"PROGRESS",mStoreOwner_Employees_ListView);
			mStoreOwnerEmployees.execute("");

			mStoreOwner_Employees_Add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent_addemployee = new Intent(StoreOwner_Employees.this,StoreOwner_AddEmployee.class);
					startActivity(intent_addemployee);
				}
			});

			mStoreOwner_Employees_ListView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					//Check the bottom item is visible
					int lastInScreen = firstVisibleItem + visibleItemCount;	
							

					if(Integer.parseInt(mEmployeeTotalCount) > lastInScreen && Integer.parseInt(mEmployeeTotalCount)>20){					
						if(lastInScreen == totalItemCount && !mIsLoadMore){												
							Log.i(TAG, "Set Text In The Footer");
							if(mStoreOwner_Employees_ListView.getFooterViewsCount() == 0){
								mStoreOwner_Employees_ListView.addFooterView(mFooterLayout);
							}

							if(Integer.parseInt(mEmployeeTotalCount) > Integer.parseInt(mEmployeeEndLimit)){
								mFooterText.setText("Loading "+mEmployeeEndLimit+" of "+"("+mEmployeeTotalCount+")");
							}else{
								mFooterText.setText("Loading "+mEmployeeTotalCount);
							}

							Log.i(TAG, "Runn AynckTask To Add More");
							StoreOwner_EmployeesAsyncTask mStoreOwnerEmployees = new StoreOwner_EmployeesAsyncTask(StoreOwner_Employees.this,"NOPROGRESS",mStoreOwner_Employees_ListView);
							mStoreOwnerEmployees.execute("RefreshAdapter");

						}
					}else{
						if(mEmployeeTotalCount.equalsIgnoreCase("0")){
							Log.i(TAG, "Currently No List Item");
						}else if(mFooterLayout != null && mStoreOwner_Employees_ListView.getFooterViewsCount() !=0 && mStoreOwner_Employees_ListView.getAdapter() != null){
							Log.i(TAG, "Remove Footer");
							if(lastInScreen!= totalItemCount){
								Log.i(TAG, "Remove Footer success");
								mStoreOwner_Employees_ListView.removeFooterView(mFooterLayout);	
							}else{
								Log.i(TAG, "Remove Footer wait");
							}
						}
					}

				}
			});

			mStoreOwner_Employees_ListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					Intent employee_details = new Intent(StoreOwner_Employees.this,StoreOwner_EmployeeDetails.class);
					employee_details.putExtra("choosed_position",arg2);
					startActivity(employee_details);
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void SetStoreOwner_EmployeesListAdatpter(String mRefreshAdapterStatus){
		if(mRefreshAdapterStatus.equalsIgnoreCase("RefreshAdapter") ){
			mStoreOwnerEmployees_Adapter.notifyDataSetChanged();
		}else{
			mStoreOwnerEmployees_Adapter = new StoreOwnerEmployees_Adapter(StoreOwner_Employees.this);
			mStoreOwner_Employees_ListView.setAdapter(mStoreOwnerEmployees_Adapter);
		}
		if(mFooterLayout != null && mStoreOwner_Employees_ListView.getFooterViewsCount() !=0 &&mStoreOwner_Employees_ListView.getAdapter() != null){
			Log.i(TAG, "Remove Footer View From employee List");
			mStoreOwner_Employees_ListView.removeFooterView(mFooterLayout);
		}
	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwner_Employees_ListView.setVisibility(View.VISIBLE);
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
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_Employees.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwner_Employees.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwner_Employees.this);
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
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_Employees.this);
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
