package com.us.zoupons.storeowner.employees;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
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
 * Activity which lists store employees
 *
 */

public class StoreOwner_Employees extends Activity {

	// Initializing views and variables
	private  String mTAG="StoreOwner_Employees";
	private MyHorizontalScrollView mScrollView;
	private	View mApp;
	private	Header mHeader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private View mRightMenu;
	private View mLeftMenu;
	private int mMenuFlag;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private TextView mStoreNameText;
	private ImageView mRightMenuHolder;
	private Button mStoreOwner_Employees_FreezeView;
	private ListView mStoreOwner_Employees_ListView;
	private LinearLayout mStoreOwner_Employees_Add;
	public static boolean mIsLoadMore;
	public static String sEmployeeStart="0",sEmployeeEndLimit="20",sEmployeeTotalCount = "0";	
	private View mFooterLayout;
	private TextView mFooterText;
	private StoreOwnerEmployees_Adapter mStoreOwnerEmployees_Adapter;
	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			LayoutInflater inflater = LayoutInflater.from(this);
			mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(mScrollView);
			mApp = inflater.inflate(R.layout.storeowner_employees, null);
			ViewGroup mMiddleView = (ViewGroup) mApp.findViewById(R.id.storeowneremployees_middleview);
			ViewGroup mFooterView = (ViewGroup) mApp.findViewById(R.id.storeowneremployees_footer);
			mStoreOwner_Employees_FreezeView = (Button) mApp.findViewById(R.id.storeowneremployees_freezeview);
			mStoreOwner_Employees_ListView = (ListView) mMiddleView.findViewById(R.id.storeowneremployees_ListView);
			mStoreOwner_Employees_Add = (LinearLayout) mFooterView.findViewById(R.id.storeowneremployees_footer_add); 
			mStoreNameText = (TextView) mApp.findViewById(R.id.storeowner_employee_storename);
			mRightMenuHolder = (ImageView) mApp.findViewById(R.id.storeowner_employee_rightmenu);
			mStoreownerRightmenu = new StoreOwner_RightMenu(StoreOwner_Employees.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_Employees_FreezeView, mTAG);
			mRightMenu = mStoreownerRightmenu.intializeInflater();
			mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreOwner_Employees.this,mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Employees_FreezeView, mTAG);
			mLeftMenu = mStoreownerLeftmenu.intializeInflater();
			mStoreownerLeftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			mStoreownerRightmenu.clickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mHeader = (Header) mApp.findViewById(R.id.storeowneremployees_header);
			mHeader.intializeInflater(mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Employees_FreezeView, mTAG);

			final View[] children = new View[] { mLeftMenu, mApp ,mRightMenu};
			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mHeader.mLeftMenuBtnSlide));
			mStoreOwner_Employees_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_Employees_FreezeView, mTAG));
			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwner_Employees_FreezeView, mTAG));
			mNotificationReceiver = new NotifitcationReceiver(mHeader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mHeader,mHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mHeader,mHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			// To clear static variables
			mIsLoadMore = false;
			sEmployeeStart="0";
			sEmployeeEndLimit="20";
			sEmployeeTotalCount = "0";
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
					if(Integer.parseInt(sEmployeeTotalCount) > lastInScreen && Integer.parseInt(sEmployeeTotalCount)>20){ // If scrolled position is greater than 20					
						if(lastInScreen == totalItemCount && !mIsLoadMore){												
							if(mStoreOwner_Employees_ListView.getFooterViewsCount() == 0){
								mStoreOwner_Employees_ListView.addFooterView(mFooterLayout);
							}
							if(Integer.parseInt(sEmployeeTotalCount) > Integer.parseInt(sEmployeeEndLimit)){
								mFooterText.setText("Loading "+sEmployeeEndLimit+" of "+"("+sEmployeeTotalCount+")");
							}else{
								mFooterText.setText("Loading "+sEmployeeTotalCount);
							}
							StoreOwner_EmployeesAsyncTask mStoreOwnerEmployees = new StoreOwner_EmployeesAsyncTask(StoreOwner_Employees.this,"NOPROGRESS",mStoreOwner_Employees_ListView);
							mStoreOwnerEmployees.execute("RefreshAdapter");
						}
					}else{
						if(sEmployeeTotalCount.equalsIgnoreCase("0")){ 
						}else if(mFooterLayout != null && mStoreOwner_Employees_ListView.getFooterViewsCount() !=0 && mStoreOwner_Employees_ListView.getAdapter() != null){ //check to remove footer loading view
							if(lastInScreen!= totalItemCount){
								mStoreOwner_Employees_ListView.removeFooterView(mFooterLayout);	
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

	// To set and refresh employee list
	public void SetStoreOwner_EmployeesListAdatpter(String mRefreshAdapterStatus){
		if(mRefreshAdapterStatus.equalsIgnoreCase("RefreshAdapter") ){
			mStoreOwnerEmployees_Adapter.notifyDataSetChanged();
		}else{
			mStoreOwnerEmployees_Adapter = new StoreOwnerEmployees_Adapter(StoreOwner_Employees.this);
			mStoreOwner_Employees_ListView.setAdapter(mStoreOwnerEmployees_Adapter);
		}
		if(mFooterLayout != null && mStoreOwner_Employees_ListView.getFooterViewsCount() !=0 &&mStoreOwner_Employees_ListView.getAdapter() != null){
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
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_Employees.this,ZouponsConstants.sStoreModuleFlag);
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
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_Employees.this);
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
