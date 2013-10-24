package com.us.zoupons.storeowner.DashBoard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

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

public class StoreOwnerDashBoard extends Activity{

	public static String TAG="StoreOwnerDashBoard";
	public static String DASHBOARD_FLAG; 
	public static MyHorizontalScrollView scrollView;
	View app;

	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	
	View[] children;
	Header header;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;
	View mLeftMenu;
	int mMenuFlag;
	StoreOwner_LeftMenu storeowner_leftmenu;
	StoreOwner_LeftMenu storeowner_storeinfo_leftmenu;
	
	ImageView mRightMenuHolder;
	Button mStoreOwnerHomePageFreezeView;
	ListView mStoreOwnerHomePageListView;
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

        	app = inflater.inflate(R.layout.storeowner_dashboard, null);

        	ViewGroup mTitleBar = (ViewGroup) app.findViewById(R.id.storeowner_titlebar);
        	ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.middleview);

        	mRightMenuHolder = (ImageView) mTitleBar.findViewById(R.id.rightmenu_holder);
        	mStoreOwnerHomePageFreezeView = (Button) app.findViewById(R.id.freezeview);
        	mStoreOwnerHomePageListView = (ListView) mMiddleView.findViewById(R.id.storeownerhomepageListView);

        	if(getIntent().getExtras().getString("classname").equalsIgnoreCase("Locations")){
        		StoreOwnerDashBoard.DASHBOARD_FLAG="Locations";
        		mRightMenuHolder.setVisibility(View.VISIBLE);
        		storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwnerDashBoard.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerHomePageFreezeView, TAG);
        		mRightMenu = storeowner_rightmenu.intializeInflater();
        		storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwnerDashBoard.this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerHomePageFreezeView, TAG);
        		mLeftMenu = storeowner_leftmenu.intializeInflater();

        		storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
        		storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);

        		/* Header Tab Bar which contains logout,notification and home buttons*/
        		header = (Header) app.findViewById(R.id.header);
        		header.intializeInflater(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerHomePageFreezeView, TAG);
        		header.mTabBarHomeBtn.setAlpha(100);
        		header.mTabBarHomeBtn.setEnabled(false);
        		children = new View[] { mLeftMenu, app, mRightMenu };
        	}

        	// Scroll to app (view[1]) when layout finished.
        	int scrollToViewIdx = 1;
        	scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));

        	mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwnerHomePageFreezeView, TAG));
        	mStoreOwnerHomePageFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerHomePageFreezeView, TAG));

        	StoreOwnerDashBoardAsynchTask mStoreOwnerHomePage = new StoreOwnerDashBoardAsynchTask(StoreOwnerDashBoard.this,"PROGRESS",mStoreOwnerHomePageListView);
        	mStoreOwnerHomePage.execute();
        }catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwnerHomePageListView.setVisibility(View.VISIBLE);
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
		mNotificationSync = new ScheduleNotificationSync(StoreOwnerDashBoard.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwnerDashBoard.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwnerDashBoard.this);
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
		new RefreshZoupons().isApplicationGoneBackground(StoreOwnerDashBoard.this);
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
