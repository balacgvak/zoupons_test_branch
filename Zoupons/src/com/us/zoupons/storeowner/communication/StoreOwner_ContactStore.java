package com.us.zoupons.storeowner.communication;

import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.Communication.CommunicationTimerTask;
import com.us.zoupons.Communication.ContactUsResponseTask;
import com.us.zoupons.Communication.ContactUsTask;
import com.us.zoupons.Communication.CustomChatScrollListner;
import com.us.zoupons.Communication.POJOContactUsResponse;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.Coupons.StoreOwnerAdd_EditCoupon;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class StoreOwner_ContactStore extends Activity {

	public static String TAG = "StoreOwner_ContactStore";

	public static MyHorizontalScrollView scrollView;
	View app;
	Header header;
	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;
	View mLeftMenu;
	int mMenuFlag;
	StoreOwner_LeftMenu storeowner_leftmenu;
	private Button mFreezeView,mNotification,mCustomerStore;
	private ListView mListView;
	public ImageView mContactStore_RightMenuOpen,mLogoutImage,mNotificationImage,mCalloutTriangleImage,mTabBarLoginChoice;
	public ViewGroup menubarcontainer;
	public EditText mAddMessage;
	public TextView mTalkToUsContactStoreBack,mTalkToUsContactStoreName,mContactStoreSendCouponFooter;
	public Button mSend,mNotificationCount;
	RelativeLayout btnLogout;
	View[] children;
	String mStoreId="",mStoreName="",mCustomerUserId="",mStoreLocationId="",mUserId="",mUserType="";
	public static String mCustomerName="";
	public static Timer mCommunicationTimer;
	public static CommunicationTimerTask mCommunicationTimerTask;
	public static boolean mIsLoadMore;
	public static View mHeaderView;
	public static ProgressBar mHeaderProgressBar;
	public static TextView mHeaderLoadingText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("Contact_store")){
			TAG="StoreOwner_ContactStore";
			mCustomerUserId = getIntent().getExtras().getString("customer_id");
		}else{
			TAG="StoreOwner_TalkToUs";
		}
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();
		app = inflater.inflate(R.layout.talktous_contactstore, null);
		mFreezeView = (Button) app.findViewById(R.id.talktous_contactstore_freeze);
		storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_ContactStore.this,scrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);
		mLeftMenu = storeowner_leftmenu.intializeInflater();
		storeowner_leftmenu.clickListener(mLeftMenu);
		storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwner_ContactStore.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mFreezeView, TAG);
		if(getIntent().getExtras().getString("source").equalsIgnoreCase("reviews") || getIntent().getExtras().getString("source").equalsIgnoreCase("talk_to_us")){
			mRightMenu = storeowner_rightmenu.intializeInflater();
			storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
		}else{
			mRightMenu = storeowner_rightmenu.intializeCustomerCenterInflater();
			storeowner_rightmenu.customercentermenuClickListener(mLeftMenu,mRightMenu);
		}
		/* Header Tab Bar which contains logout,notification and home buttons*/
		header = (Header) app.findViewById(R.id.storeownercontact_talk_tous_header);
		header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);
		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.talktous_contactstore_tabBar);
		tabBar.setVisibility(View.GONE);
		header.setVisibility(View.VISIBLE);
		ViewGroup talktous_contactstore_container = (ViewGroup) app.findViewById(R.id.talktous_contactstore_container);
		ViewGroup talktous_contactstore_righmenuholder = (ViewGroup) app.findViewById(R.id.talktous_contactstore_rightmenuholder);
		menubarcontainer =(ViewGroup) talktous_contactstore_container.findViewById(R.id.menubarcontainer_talktous_contactstore);
		ViewGroup talktous_contactstore_footer = (ViewGroup) app.findViewById(R.id.talktous_contactstore_footerLayoutId);
		mContactStoreSendCouponFooter =  (TextView) talktous_contactstore_footer.findViewById(R.id.talktous_contactstore_sendPrivateCoupontextId); 
		mListView = (ListView) app.findViewById(R.id.talktous_contactstore_listview);
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
		mHeaderView = mInflater.inflate(R.layout.footerlayout, null, false);
		mHeaderLoadingText = (TextView) mHeaderView.findViewById(R.id.footerText);
		mHeaderProgressBar = (ProgressBar) mHeaderView.findViewById(R.id.mProgressBar);
		mListView.addHeaderView(mHeaderView);
		
		final boolean mIsFromNotifications = getIntent().hasExtra("isFromNotifications") ? true : false;
		
		mCommunicationTimer = new Timer();
		if(TAG.equalsIgnoreCase("StoreOwner_ContactStore"))
			mCommunicationTimerTask = new CommunicationTimerTask(StoreOwner_ContactStore.this, "ContactStore", menubarcontainer, mListView, mIsFromNotifications, "store_customer");
		else
			mCommunicationTimerTask = new CommunicationTimerTask(StoreOwner_ContactStore.this, "TalkToUs", menubarcontainer, mListView, mIsFromNotifications, "store_zoupons");
		
		
		if(TAG.equalsIgnoreCase("StoreOwner_ContactStore")){
			talktous_contactstore_righmenuholder.setVisibility(View.VISIBLE);
			if(getIntent().hasExtra("is_back")){
				talktous_contactstore_footer.setVisibility(View.VISIBLE);
			}else{
				talktous_contactstore_footer.setVisibility(View.GONE);
			}
			mContactStore_RightMenuOpen=(ImageView) talktous_contactstore_container.findViewById(R.id.talktous_contactstore_rightmenu);
			mContactStore_RightMenuOpen.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag=2, mFreezeView, TAG,mAddMessage,false));
			ContactUsResponseTask contactusresponse = new ContactUsResponseTask(StoreOwner_ContactStore.this,menubarcontainer,mListView,mIsFromNotifications,"store_customer",true,false,"normal","0");
			contactusresponse.execute("ContactStore",mStoreId,mCustomerUserId,mStoreLocationId,mUserType);
			if(getIntent().getExtras().getString("source").equalsIgnoreCase("reviews")){
				mContactStoreSendCouponFooter.setVisibility(View.VISIBLE);
				mTalkToUsContactStoreName.setText(mStoreName);
			}else{
				mTalkToUsContactStoreName.setText(StoreOwner_RightMenu.mCustomer_FirstName+" "+StoreOwner_RightMenu.mCustomer_LastName);
			}
		}else{
			talktous_contactstore_footer.setVisibility(View.GONE);
			talktous_contactstore_righmenuholder.setVisibility(View.GONE);
			ContactUsResponseTask contactusresponse = new ContactUsResponseTask(StoreOwner_ContactStore.this,menubarcontainer,mListView,mIsFromNotifications,"store_zoupons",true,false,"normal","0");
			contactusresponse.execute("TalkToUs",mStoreId,mUserId,mStoreLocationId,mUserType);
		}
	
		mFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag, mFreezeView, TAG,mAddMessage,true));

		if(TAG.equalsIgnoreCase("StoreOwner_ContactStore")){
			children = new View[] { mLeftMenu, app, mRightMenu };
		}else{
			children = new View[] { mLeftMenu, app };
		}

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));

		mSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(mCommunicationTimer!=null){
					mCommunicationTimer.cancel();
					mCommunicationTimer=null;
				}if(mCommunicationTimerTask!=null){
					mCommunicationTimerTask.cancel();
					mCommunicationTimerTask=null;
				}
				
				InputMethodManager softkeyboardevent = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				softkeyboardevent.hideSoftInputFromWindow(mAddMessage.getWindowToken(), 0);

				if(TAG.equalsIgnoreCase("StoreOwner_ContactStore")){
					ContactUsTask contactustask = new ContactUsTask(StoreOwner_ContactStore.this,menubarcontainer,mListView,mAddMessage,mIsFromNotifications,mUserId,"store");
					contactustask.execute("ContactStore",mStoreId,mAddMessage.getText().toString().trim(),mStoreLocationId,mUserType,mCustomerUserId);
				}else{
					ContactUsTask contactustask = new ContactUsTask(StoreOwner_ContactStore.this,menubarcontainer,mListView,mAddMessage,mIsFromNotifications,mUserId,"store");
					contactustask.execute("TalkToUs","0",mAddMessage.getText().toString().trim(),mStoreLocationId,mUserType,mUserId);
				}
			}
		});

		mTalkToUsContactStoreBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mContactStoreSendCouponFooter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent_sendCoupon = new Intent(StoreOwner_ContactStore.this,StoreOwnerAdd_EditCoupon.class);
				intent_sendCoupon.putExtra("FromContactStore", true);
				intent_sendCoupon.putExtra("customer_id",mCustomerUserId);
				startActivity(intent_sendCoupon);
			}
		});
		
		mListView.setOnScrollListener(new CustomChatScrollListner(StoreOwner_ContactStore.this,mListView,TAG, mIsFromNotifications, menubarcontainer));
		
		/*mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {

				Log.i("Scroll position", firstVisibleItem+" "+ mIsLoadMore);
				if(view.getAdapter() != null && firstVisibleItem == 0 && view.getAdapter().getCount()>20 && !mIsLoadMore){
					Log.i("total count", firstVisibleItem+ " " + view.getAdapter().getCount());
					POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponse.get(0);
					Log.i("first chat id", obj.mResponseId+"");
					if(mListView.getHeaderViewsCount() == 0){
						mListView.addHeaderView(mHeaderView);
					}
					if(TAG.equalsIgnoreCase("StoreOwner_ContactStore")){
						ContactUsResponseTask contactusresponse = new ContactUsResponseTask(StoreOwner_ContactStore.this,menubarcontainer,mListView,mIsFromNotifications,"store_customer",true,false,"scroll",obj.mResponseId);
						contactusresponse.execute("ContactStore",mStoreId,mCustomerUserId,mStoreLocationId,mUserType);
					}else{
						ContactUsResponseTask contactusresponse = new ContactUsResponseTask(StoreOwner_ContactStore.this,menubarcontainer,mListView,mIsFromNotifications,"store_zoupons",true,false,"scroll",obj.mResponseId);
						contactusresponse.execute("TalkToUs",mStoreId,mUserId,mStoreLocationId,mUserType);
					}
				}else if(view.getAdapter() != null && view.getAdapter().getCount()<=20){
					if(mHeaderView != null && mListView.getHeaderViewsCount() !=0 &&mListView.getAdapter() != null){
						Log.i(TAG, "Remove header View From Chat List");
						//mListView.removeHeaderView(mHeaderView);
						mHeaderProgressBar.setVisibility(View.GONE);
						mHeaderLoadingText.setVisibility(View.GONE);
					}
				}else{
					Log.i("Scroll position", "failed"+ " " +totalItemCount);
				}

			}
		});*/
		
		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
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
