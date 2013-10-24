package com.us.zoupons.storeowner.communication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.CustomerService.GetCustomerCommunicatedStoreAsyncTask;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class StoreOwner_Communication extends Activity {

	public static String TAG = "StoreOwner_Communication";

	public static MyHorizontalScrollView scrollView;
	View app;
	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	Header header;
	StoreOwner_LeftMenu storeowner_leftmenu;
	View mLeftMenu;
	int mMenuFlag;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;

	private Button mFreezeView,mNotification,mCustomerStore;
	private ListView mListView;

	public static boolean mIsLoadMore;
	public static String mCustomerCenterStart="0",mCustomerCenterEndLimit="20",mCustomerCenterTotalCount = "0";

	private LinearLayout mCommunication_Notifications,mCommunication_CustomerStore;
	private TextView mCommunication_NotificationCount,mCommunication_CustomerStoreCount;
	private ListView mCommunication_CustomerListView;
	public View mFooterLayout;
	private TextView mFooterText;
	LayoutInflater mInflater;
	private TextView mStoreNameText,mNotificationsFooter,mContactSupportFooter;
	private ImageView mRightMenuHolder;
	private LinearLayout mCommunicationFooterLayout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);

		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();
		app = inflater.inflate(R.layout.storeowner_communication, null);

		ViewGroup mWholeContainer = (ViewGroup) app.findViewById(R.id.communication_container);
		ViewGroup mCommunication_container1 = (ViewGroup) app.findViewById(R.id.communication_container1);
		ViewGroup mCommunication_container2 = (ViewGroup) app.findViewById(R.id.communication_container2);
		mCommunicationFooterLayout = (LinearLayout) app.findViewById(R.id.storeownercommunication_footerLayoutId);
		mNotificationsFooter = (TextView) app.findViewById(R.id.storeownercommunication_notifications);
		mContactSupportFooter = 	(TextView) app.findViewById(R.id.storeownercommunication_contactsupport);
		mStoreNameText = (TextView) app.findViewById(R.id.storeowner_communication_storename);
		mRightMenuHolder = (ImageView) app.findViewById(R.id.storeowner_communication_rightmenu);
		mFreezeView = (Button) app.findViewById(R.id.communication_freeze);
		storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_Communication.this,scrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);
		mLeftMenu = storeowner_leftmenu.intializeInflater();
		storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwner_Communication.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mFreezeView, TAG);
		mRightMenu = storeowner_rightmenu.intializeInflater();
		storeowner_leftmenu.clickListener(mLeftMenu);
		storeowner_rightmenu.clickListener(mLeftMenu, mRightMenu);
		/* Header Tab Bar which contains logout,notification and home buttons*/
		header = (Header) app.findViewById(R.id.communication_header);
		header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);

		final View[] children = new View[] { mLeftMenu, app,mRightMenu};
		/* Scroll to app (view[1]) when layout finished.*/
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));

		mFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag, mFreezeView, TAG));
		mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mFreezeView, TAG));
		mIsLoadMore = false;
		mCustomerCenterStart ="0";
		mCustomerCenterEndLimit ="20";
		mCustomerCenterTotalCount ="0";

		mCommunication_Notifications = (LinearLayout) mCommunication_container1.findViewById(R.id.communication_notifications);
		mCommunication_CustomerStore = (LinearLayout) mCommunication_container1.findViewById(R.id.communication_customerstore);
		mCommunication_NotificationCount = (TextView) mCommunication_container1.findViewById(R.id.communication_notificationcount);
		mCommunication_CustomerStoreCount = (TextView) mCommunication_container1.findViewById(R.id.communication_customerstorecount);
		mCommunication_CustomerListView = (ListView) mCommunication_container2.findViewById(R.id.communication_customerlistview);

		//For Footer Layout
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
		mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		mCommunication_CustomerListView.addFooterView(mFooterLayout);
		// To set StoreName
		SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
		String mStoreName = mPrefs.getString("store_name", "");
		mStoreNameText.setText(mStoreName);
		GetCustomerCommunicatedStoreAsyncTask customerserviceasynctask = new GetCustomerCommunicatedStoreAsyncTask(StoreOwner_Communication.this, mCommunication_CustomerListView, "PROGRESS",mCommunication_NotificationCount,mCommunication_CustomerStoreCount);
		customerserviceasynctask.execute("");

		mContactSupportFooter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent_talktous = new Intent(StoreOwner_Communication.this,StoreOwner_ContactStore.class);
				intent_talktous.putExtra("source", "talk_to_us");
				intent_talktous.putExtra("class_name", "talk_to_us");
				startActivity(intent_talktous);
			}
		});


		mCommunication_CustomerListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				//Check the bottom item is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;	
						

				if(Integer.parseInt(mCustomerCenterTotalCount) > lastInScreen && Integer.parseInt(mCustomerCenterTotalCount)>20){					
					if(lastInScreen == totalItemCount && !mIsLoadMore){												
						
						if(mCommunication_CustomerListView.getFooterViewsCount() == 0){
							mCommunication_CustomerListView.addFooterView(mFooterLayout);
						}
						if(Integer.parseInt(mCustomerCenterTotalCount) > Integer.parseInt(mCustomerCenterEndLimit)){
							mFooterText.setText("Loading "+mCustomerCenterEndLimit+" of "+"("+mCustomerCenterTotalCount+")");
						}else{
							mFooterText.setText("Loading "+mCustomerCenterTotalCount);
						}

						
						GetCustomerCommunicatedStoreAsyncTask customerserviceasynctask = new GetCustomerCommunicatedStoreAsyncTask(StoreOwner_Communication.this, mCommunication_CustomerListView, "NOPROGRESS",mCommunication_NotificationCount,mCommunication_CustomerStoreCount);
						customerserviceasynctask.execute("RefreshAdapter");
					}
				}else{
					if(mCustomerCenterTotalCount.equalsIgnoreCase("0")){
						Log.i(TAG, "Currently No List Item");
					}else if(mFooterLayout != null && mCommunication_CustomerListView.getFooterViewsCount() !=0 &&mCommunication_CustomerListView.getAdapter() != null){
						Log.i(TAG, "Remove Footer");

						if(lastInScreen!= totalItemCount){
							Log.i(TAG, "Remove Footer success");
							mCommunication_CustomerListView.removeFooterView(mFooterLayout);	
						}else{
							Log.i(TAG, "Remove Footer wait");
						}
					}
				}

			}
		});

		mCommunication_Notifications.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCommunication_NotificationCount.setText("0");
				startActivity(new Intent(StoreOwner_Communication.this,StoreOwner_Notifications.class));
			}
		});

		mCommunication_CustomerStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!mCommunication_CustomerStoreCount.getText().toString().trim().equalsIgnoreCase("0")){
					mCommunication_CustomerStoreCount.setText("0");
				}
				Intent intent_talktous = new Intent(StoreOwner_Communication.this,StoreOwner_ContactStore.class);
				intent_talktous.putExtra("source", "talk_to_us");
				startActivity(intent_talktous);
			}
		});
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
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
	}

	@Override
	public void onWindowAttributesChanged(LayoutParams params) {
		super.onWindowAttributesChanged(params);
	}
}
