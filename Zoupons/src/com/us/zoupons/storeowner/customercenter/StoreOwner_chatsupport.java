package com.us.zoupons.storeowner.customercenter;

import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.shopper.chatsupport.CommunicationTimerTask;
import com.us.zoupons.shopper.chatsupport.ContactUsResponseTask;
import com.us.zoupons.shopper.chatsupport.ContactUsTask;
import com.us.zoupons.shopper.chatsupport.CustomChatScrollListner;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.coupons.StoreOwnerAdd_EditCoupon;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

/**
 * 
 * Activity which displays communiction between store to customer and store to zoupons
 *
 */


public class StoreOwner_chatsupport extends Activity {

	// Initialization of views and view groups
	public static String TAG = "StoreOwner_chatsupport";
	private MyHorizontalScrollView mScrollView;
	private View mApp;
	private Header mHeader;
	private StoreOwner_RightMenu mStoreowner_rightmenu;
	private View mRightMenu,mLeftMenu;
	private int mMenuFlag;
	private StoreOwner_LeftMenu mStoreowner_leftmenu;
	private Button mFreezeView;
	private ListView mListView;
	private ImageView mContactStore_RightMenuOpen;
	private ViewGroup menubarcontainer;
	private EditText mAddMessage;
	private TextView mTalkToUsContactStoreBack,mTalkToUsContactStoreName,mContactStoreSendCouponFooter;
	private Button mSend;
	private View[] mChildrens;
	private String mStoreId="",mStoreName="",mCustomerUserId="",mStoreLocationId="",mUserId="",mUserType="";
	public static String sCustomerName="";
	public static Timer sCommunicationTimer;
	public static CommunicationTimerTask sCommunicationTimerTask;
	public static boolean sIsLoadMore;
	public static View sHeaderView;
	public static ProgressBar sHeaderProgressBar;
	public static TextView sHeaderLoadingText;
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("Contact_store")){ // For Contact Store (store to customer)
			TAG="StoreOwner_chatsupport";
			mCustomerUserId = getIntent().getExtras().getString("customer_id");
		}else{  // For Zoupons support (store to zoupons support)
 			TAG="StoreOwner_TalkToUs";
		}
		// Initializing views and variables
		LayoutInflater inflater = LayoutInflater.from(this);
		mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(mScrollView);
		mApp = inflater.inflate(R.layout.talktous_contactstore, null);
		mFreezeView = (Button) mApp.findViewById(R.id.talktous_contactstore_freeze);
		mStoreowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_chatsupport.this,mScrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);
		mLeftMenu = mStoreowner_leftmenu.intializeInflater();
		mStoreowner_leftmenu.clickListener(mLeftMenu);
		mStoreowner_rightmenu = new StoreOwner_RightMenu(StoreOwner_chatsupport.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mFreezeView, TAG);
		// Check for default store right menu or customer center specific right menu
		if(getIntent().getExtras().getString("source").equalsIgnoreCase("reviews") || getIntent().getExtras().getString("source").equalsIgnoreCase("talk_to_us")){
			mRightMenu = mStoreowner_rightmenu.intializeInflater();
			mStoreowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
		}else{
			mRightMenu = mStoreowner_rightmenu.intializeCustomerCenterInflater();
			mStoreowner_rightmenu.customercentermenuClickListener(mLeftMenu,mRightMenu);
		}
		/* Header Tab Bar which contains logout,notification and home buttons*/
		mHeader = (Header) mApp.findViewById(R.id.storeownercontact_talk_tous_header);
		mHeader.intializeInflater(mScrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);
		ViewGroup tabBar = (ViewGroup) mApp.findViewById(R.id.talktous_contactstore_tabBar);
		tabBar.setVisibility(View.GONE);
		mHeader.setVisibility(View.VISIBLE);
		ViewGroup talktous_contactstore_container = (ViewGroup) mApp.findViewById(R.id.talktous_contactstore_container);
		ViewGroup talktous_contactstore_righmenuholder = (ViewGroup) mApp.findViewById(R.id.talktous_contactstore_rightmenuholder);
		menubarcontainer =(ViewGroup) talktous_contactstore_container.findViewById(R.id.menubarcontainer_talktous_contactstore);
		ViewGroup talktous_contactstore_footer = (ViewGroup) mApp.findViewById(R.id.talktous_contactstore_footerLayoutId);
		mContactStoreSendCouponFooter =  (TextView) talktous_contactstore_footer.findViewById(R.id.talktous_contactstore_sendPrivateCoupontextId); 
		mListView = (ListView) mApp.findViewById(R.id.talktous_contactstore_listview);
		mAddMessage = (EditText) menubarcontainer.findViewById(R.id.talktous_contactstore_newmsg);
		mSend = (Button) menubarcontainer.findViewById(R.id.talktous_contactstore_send);
		mTalkToUsContactStoreName = (TextView) talktous_contactstore_righmenuholder.findViewById(R.id.talktous_contactstore_storename_textId);
		// Notification and log out variables
		mTalkToUsContactStoreBack = (TextView) talktous_contactstore_footer.findViewById(R.id.talktous_contactstore_backtextId);
		SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
		mStoreName = mPrefs.getString("store_name", "");
		mStoreId = mPrefs.getString("store_id", "");
		mStoreLocationId = mPrefs.getString("location_id", "");
		mUserId = mPrefs.getString("user_id", "");
		mUserType = mPrefs.getString("user_type", "");	
		// To save customer id in preference to use it while scrolling listview
		Editor editor = mPrefs.edit();
		editor.putString("customer_id",mCustomerUserId);
		editor.commit();
		MainMenuActivity.mIsLoadMore = false;
		LayoutInflater  mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sHeaderView = mInflater.inflate(R.layout.footerlayout, null, false);
		sHeaderLoadingText = (TextView) sHeaderView.findViewById(R.id.footerText);
		sHeaderProgressBar = (ProgressBar) sHeaderView.findViewById(R.id.mProgressBar);
		mListView.addHeaderView(sHeaderView);
		mNotificationReceiver = new NotifitcationReceiver(mHeader.mTabBarNotificationCountBtn);
		// Notitification pop up layout declaration
		mHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mHeader,mHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
		mHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mHeader,mHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
		
		final boolean mIsFromNotifications = getIntent().hasExtra("isFromNotifications") ? true : false;
		if(StoreOwner_chatsupport.sCommunicationTimer!=null){ // To cancel chat timer
			StoreOwner_chatsupport.sCommunicationTimer.cancel();
			StoreOwner_chatsupport.sCommunicationTimer=null;
		}if(StoreOwner_chatsupport.sCommunicationTimerTask!=null){ // To cancel chat timer task
			Log.i("timer task not null", "cancelled in contact store");
			StoreOwner_chatsupport.sCommunicationTimerTask.cancel();
			StoreOwner_chatsupport.sCommunicationTimerTask=null;
		}
		sCommunicationTimer = new Timer();
		if(TAG.equalsIgnoreCase("StoreOwner_chatsupport"))   // Communication timer for contact store 
			sCommunicationTimerTask = new CommunicationTimerTask(StoreOwner_chatsupport.this, "ContactStore", menubarcontainer, mListView, mIsFromNotifications, "store_customer",mHeader.mTabBarNotificationCountBtn);
		else
			sCommunicationTimerTask = new CommunicationTimerTask(StoreOwner_chatsupport.this, "ZouponsSupport", menubarcontainer, mListView, mIsFromNotifications, "store_zoupons",mHeader.mTabBarNotificationCountBtn);
				
		if(TAG.equalsIgnoreCase("StoreOwner_chatsupport")){ // Store and customer communication
			talktous_contactstore_righmenuholder.setVisibility(View.VISIBLE);
			if(getIntent().hasExtra("is_back")){
				talktous_contactstore_footer.setVisibility(View.VISIBLE);
			}else{
				talktous_contactstore_footer.setVisibility(View.GONE);
			}
			mContactStore_RightMenuOpen=(ImageView) talktous_contactstore_container.findViewById(R.id.talktous_contactstore_rightmenu);
			mContactStore_RightMenuOpen.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag=2, mFreezeView, TAG,mAddMessage,false));
			ContactUsResponseTask contactusresponse = new ContactUsResponseTask(StoreOwner_chatsupport.this,menubarcontainer,mListView,mIsFromNotifications,"store_customer",true,false,"normal","0","store",mHeader.mTabBarNotificationCountBtn);
			contactusresponse.execute("ContactStore",mStoreId,mCustomerUserId,mStoreLocationId,mUserType);
			if(getIntent().getExtras().getString("source").equalsIgnoreCase("reviews")){
				mContactStoreSendCouponFooter.setVisibility(View.VISIBLE);
				mTalkToUsContactStoreName.setText(sCustomerName);
			}else{
				mTalkToUsContactStoreName.setText(StoreOwner_RightMenu.mCustomer_FirstName+" "+StoreOwner_RightMenu.mCustomer_LastName);
			}
		}else{ // Store and Zoupons communication
			talktous_contactstore_footer.setVisibility(View.GONE);
			talktous_contactstore_righmenuholder.setVisibility(View.VISIBLE);
			mContactStore_RightMenuOpen=(ImageView) talktous_contactstore_container.findViewById(R.id.talktous_contactstore_rightmenu);
			mContactStore_RightMenuOpen.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag=2, mFreezeView, TAG,mAddMessage,false));
			mTalkToUsContactStoreName.setText(mStoreName);
			ContactUsResponseTask contactusresponse = new ContactUsResponseTask(StoreOwner_chatsupport.this,menubarcontainer,mListView,mIsFromNotifications,"store_zoupons",true,false,"normal","0","store",mHeader.mTabBarNotificationCountBtn);
			contactusresponse.execute("ZouponsSupport",mStoreId,mUserId,mStoreLocationId,mUserType);
		}
		mFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, TAG,mAddMessage,true));
		if(TAG.equalsIgnoreCase("StoreOwner_chatsupport")){
			mChildrens = new View[] { mLeftMenu, mApp, mRightMenu };
		}else{
			mChildrens = new View[] { mLeftMenu, mApp ,mRightMenu};
		}
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		mScrollView.initViews(mChildrens, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mHeader.mLeftMenuBtnSlide));

		mSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(!mAddMessage.getText().toString().equalsIgnoreCase("")){ //If chat message is empty

					if(sCommunicationTimer!=null){
						sCommunicationTimer.cancel();
						sCommunicationTimer=null;
					}if(sCommunicationTimerTask!=null){
						sCommunicationTimerTask.cancel();
						sCommunicationTimerTask=null;
					}
					// To hide keyboard
					InputMethodManager softkeyboardevent = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					softkeyboardevent.hideSoftInputFromWindow(mAddMessage.getWindowToken(), 0);

					if(TAG.equalsIgnoreCase("StoreOwner_chatsupport")){
						ContactUsTask contactustask = new ContactUsTask(StoreOwner_chatsupport.this,menubarcontainer,mListView,mAddMessage,mIsFromNotifications,mUserId,"store",mHeader.mTabBarNotificationCountBtn);
						contactustask.execute("ContactStore",mStoreId,mAddMessage.getText().toString().trim(),mStoreLocationId,mUserType,mCustomerUserId);
					}else{
						ContactUsTask contactustask = new ContactUsTask(StoreOwner_chatsupport.this,menubarcontainer,mListView,mAddMessage,mIsFromNotifications,mUserId,"store",mHeader.mTabBarNotificationCountBtn);
						contactustask.execute("ZouponsSupport","0",mAddMessage.getText().toString().trim(),mStoreLocationId,mUserType,mUserId);
					}
				}
			}
		});

		mTalkToUsContactStoreBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(sCommunicationTimer!=null){
					sCommunicationTimer.cancel();
					sCommunicationTimer=null;
				}if(sCommunicationTimerTask!=null){
					sCommunicationTimerTask.cancel();
					sCommunicationTimerTask=null;
				}
				finish();
			}
		});

		mContactStoreSendCouponFooter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent_sendCoupon = new Intent(StoreOwner_chatsupport.this,StoreOwnerAdd_EditCoupon.class);
				intent_sendCoupon.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent_sendCoupon.putExtra("FromContactStore", true);
				intent_sendCoupon.putExtra("customer_id",mCustomerUserId);
				startActivity(intent_sendCoupon);
			}
		});
		
		if(TAG.equalsIgnoreCase("StoreOwner_chatsupport")){
			mListView.setOnScrollListener(new CustomChatScrollListner(StoreOwner_chatsupport.this,mListView,"ContactStore", mIsFromNotifications, menubarcontainer,mHeader.mTabBarNotificationCountBtn));	
		}else{
			mListView.setOnScrollListener(new CustomChatScrollListner(StoreOwner_chatsupport.this,mListView,"ZouponsSupport", mIsFromNotifications, menubarcontainer,mHeader.mTabBarNotificationCountBtn));
		}
		
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// To notify system that its time to run garbage collector service
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
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_chatsupport.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		new CheckUserSession(StoreOwner_chatsupport.this).checkIfSessionExpires();
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_chatsupport.this,ZouponsConstants.sStoreModuleFlag);
		mNotificationSync.setRecurringAlarm();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwner_chatsupport.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
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
		mAddMessage.setFocusable(true);
		mAddMessage.setFocusableInTouchMode(true);
	}

}
