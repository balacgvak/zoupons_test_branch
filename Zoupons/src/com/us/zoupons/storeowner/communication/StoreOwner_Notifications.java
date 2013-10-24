package com.us.zoupons.storeowner.communication;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.CustomerService.CustomercenterNotificationsAdapter;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class StoreOwner_Notifications extends Activity {

	public static String TAG = "StoreOwner_ContactStore";

	public static MyHorizontalScrollView scrollView;
	View app;
	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;
	View mLeftMenu;
	int mMenuFlag;
	StoreOwner_LeftMenu storeowner_leftmenu;
	ImageView btnSlide;

	private Button mFreezeView,mNotification,mCustomerStore;
	private ListView mListView;

	public ImageView mContactStore_RightMenuOpen,mLogoutImage,mNotificationImage,mCalloutTriangleImage,mTabBarLoginChoice;
	public ViewGroup menubarcontainer;
	public EditText mAddMessage;
	public TextView mTalkToUsContactStoreBack;
	public Button mSend,mNotificationCount;
	RelativeLayout btnLogout;
	View[] children;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);

		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();

		app = inflater.inflate(R.layout.customercenter_notifications, null);

		storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwner_Notifications.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mFreezeView, TAG);
		mRightMenu = storeowner_rightmenu.intializeInflater();
		storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_Notifications.this,scrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);
		mLeftMenu = storeowner_leftmenu.intializeInflater();

		storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
		storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.customercenter_notifications_tabBar);
		ViewGroup customercenter_notifications_footer = (ViewGroup) app.findViewById(R.id.customercenter_notifications_footerLayoutId);

		mListView = (ListView) app.findViewById(R.id.customercenter_notifications_listview);
		
		// Notification and log out variables
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(StoreOwner_Notifications.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(StoreOwner_Notifications.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		TextView mCusotmercenterNotificationBack = (TextView) customercenter_notifications_footer.findViewById(R.id.customercenter_notifications_backtextId);

		mListView = (ListView) app.findViewById(R.id.customercenter_notifications_listview);
		mFreezeView=(Button) app.findViewById(R.id.customercenter_notifications_freeze);

		mFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag, mFreezeView, TAG));

		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_customercenter_notifications);
		btnSlide.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag, mFreezeView, TAG));

		children = new View[] { mLeftMenu, app };
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(btnSlide));

		ArrayList<Object> mNotifications = new ArrayList<Object>();
		for(int i=0 ;i < WebServiceStaticArrays.mAllNotifications_CustomerService.size();i++){
			NotificationDetails mNotificationDetails = (NotificationDetails) WebServiceStaticArrays.mAllNotifications_CustomerService.get(i);
			if(!mNotificationDetails.notification_type.equalsIgnoreCase("store_message") && !mNotificationDetails.notification_type.equalsIgnoreCase("zoupon_message")&& !mNotificationDetails.notification_type.equalsIgnoreCase("") ){
				mNotifications.add(mNotificationDetails); 
			}
		}

		mListView.setAdapter(new CustomercenterNotificationsAdapter(StoreOwner_Notifications.this,mNotifications));

		// click listener for back button
		mCusotmercenterNotificationBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

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
	public void onBackPressed() {
		//super.onBackPressed();
	}

}
