package com.us.zoupons.storeowner.Coupons;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class StoreOwnerCoupons extends Activity implements OnCheckedChangeListener{

	public static String TAG="StoreOwnerCoupons";

	public static MyHorizontalScrollView scrollView;
	View app;

	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;

	Header header;
	StoreOwner_RightMenu storeowner_rightmenu;
	StoreOwner_LeftMenu storeowner_leftmenu;
	View mRightMenu;
	View mLeftMenu;
	int mMenuFlag;

	ImageView mRightMenuHolder;
	Button mStoreOwnerCouponsFreezeView;
	ListView mStoreOwnerCouponsListView;
	TextView mStoreOwnerCouponsInvisible1,mStoreOwnerCouponsInvisible2,mStoreOwnerCouponsAddCoupon,mStoreOwnerStoreNameText;
	public int mScreenWidth;
	public double mMenuWidth;

	StoreOwnerPublicCouponsAdapter mStoreOwnerPublicCouponsAdapter=null;
	StoreOwnerPrivateCouponsAdapter mStoreOwnerPrivateCouponsAdapter=null;
	private CheckBox mPublicCouponType,mPrivateCouponType;
	public static boolean mIsLoadMore;
	public static String mCouponStart="0",mCouponEndLimit="20",mCouponTotalCount = "0";	
	LayoutInflater mInflater;
	public View mFooterLayout;
	private TextView mFooterText;
	private ProgressBar mFooterProgressBar;
	private String coupontype="Public";

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

			//Call function to get width of the screen
			mScreenWidth=getScreenWidth(); 	
			if(mScreenWidth>0){	//To fix Home Page menubar items width
				mMenuWidth=mScreenWidth/3;
				
			}

			app = inflater.inflate(R.layout.storeowner_coupons, null);

			ViewGroup mTitleBar = (ViewGroup) app.findViewById(R.id.storeownercoupons_storename_header);
			ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.storeownercoupons_container);
			ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeownercoupons_footerLayoutId);

			mRightMenuHolder = (ImageView) mTitleBar.findViewById(R.id.storeownercoupons_rightmenu);
			mStoreOwnerStoreNameText = (TextView) mMiddleView.findViewById(R.id.storeownercoupons_title_textId);
			mStoreOwnerCouponsFreezeView = (Button) app.findViewById(R.id.storeownercoupons_freeze);
			mStoreOwnerCouponsListView = (ListView) mMiddleView.findViewById(R.id.storeownercoupons_CouponsList);
			mStoreOwnerCouponsInvisible1 = (TextView) mFooterView.findViewById(R.id.storeownercoupons_availabletextId);
			mStoreOwnerCouponsInvisible1.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
			mStoreOwnerCouponsInvisible2 = (TextView) mFooterView.findViewById(R.id.storeownercoupons_purchasedtextId);
			mStoreOwnerCouponsInvisible2.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
			mStoreOwnerCouponsAddCoupon = (TextView) mFooterView.findViewById(R.id.storeownercoupons_addcoupontextId);
			mStoreOwnerCouponsAddCoupon.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));

			storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwnerCoupons.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerCouponsFreezeView, TAG);
			mRightMenu = storeowner_rightmenu.intializeInflater();
			storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwnerCoupons.this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerCouponsFreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();

			storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);

			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeownercoupons_header);
			header.intializeInflater(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerCouponsFreezeView, TAG);

			final View[] children = new View[] { mLeftMenu, app, mRightMenu };

			/* Scroll to app (view[1]) when layout finished.*/
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));

			mPublicCouponType = (CheckBox) mMiddleView.findViewById(R.id.public_coupons_checkboxId);
			mPrivateCouponType = (CheckBox) mMiddleView.findViewById(R.id.private_coupons_checkboxId);

			mIsLoadMore = false;
			mCouponStart="0";
			mCouponEndLimit="20";
			mCouponTotalCount = "0";

			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
			String mStoreName = mPrefs.getString("store_name", "");
			mStoreOwnerStoreNameText.setText(mStoreName);
			// Default checked to share via social...
			mPublicCouponType.setChecked(true);
			//Checked change listener for public coupons type..
			mPublicCouponType.setOnCheckedChangeListener(this);
			//Checked change listener for private coupon type
			mPrivateCouponType.setOnCheckedChangeListener(this);

			mStoreOwnerCouponsAddCoupon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent_addcoupon = new Intent(StoreOwnerCoupons.this,StoreOwnerAdd_EditCoupon.class);
					startActivityForResult(intent_addcoupon,100);
				}
			});	

			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwnerCouponsFreezeView, TAG));
			mStoreOwnerCouponsFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerCouponsFreezeView, TAG));

			//For Footer Layout
			mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
			mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
			mFooterProgressBar = (ProgressBar) mFooterLayout.findViewById(R.id.mProgressBar);
			mStoreOwnerCouponsListView.addFooterView(mFooterLayout);

			StoreOwnerCouponsAsyncTask mStoreOwnerCoupons = new StoreOwnerCouponsAsyncTask(StoreOwnerCoupons.this,"PROGRESS",mStoreOwnerCouponsListView);
			mStoreOwnerCoupons.execute("","Public");

			mStoreOwnerCouponsListView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					try{
						//Check the bottom item is visible
						int lastInScreen = firstVisibleItem + visibleItemCount;	
						if(Integer.parseInt(mCouponTotalCount) > lastInScreen && Integer.parseInt(mCouponTotalCount)>20){					
							if(lastInScreen == totalItemCount && !mIsLoadMore){												
								Log.i(TAG, "Set Text In The Footer");
								if(mStoreOwnerCouponsListView.getFooterViewsCount() == 0){
									mStoreOwnerCouponsListView.addFooterView(mFooterLayout);
								}
								if(mFooterProgressBar.getVisibility() != View.VISIBLE){
									mFooterProgressBar.setVisibility(View.VISIBLE);
								}
								
								if(Integer.parseInt(mCouponTotalCount) > Integer.parseInt(mCouponEndLimit)){
									mFooterText.setText("Loading "+mCouponEndLimit+" of "+"("+mCouponTotalCount+")");
								}else{
									mFooterText.setText("Loading "+mCouponTotalCount);
								}
								
								Log.i(TAG, "Runn AynckTask To Add More");
								StoreOwnerCouponsAsyncTask mStoreOwnerCoupons = new StoreOwnerCouponsAsyncTask(StoreOwnerCoupons.this,"NOPROGRESS",mStoreOwnerCouponsListView);
								mStoreOwnerCoupons.execute("RefreshAdapter",coupontype);
							}
						}else{
							if(mCouponTotalCount.equalsIgnoreCase("0")){
								Log.i(TAG, "Currently No List Item");
							}else if(mFooterLayout != null && mStoreOwnerCouponsListView.getFooterViewsCount() !=0 && mStoreOwnerCouponsListView.getAdapter() != null){
								Log.i(TAG, "Remove Footer");
								if(lastInScreen!= totalItemCount){
									Log.i(TAG, "Remove Footer success");
									mStoreOwnerCouponsListView.removeFooterView(mFooterLayout);	
								}else{
									Log.i(TAG, "Remove Footer wait");
								}
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});

			mStoreOwnerCouponsListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View arg1, int position,
						long arg3) {
					if(WebServiceStaticArrays.mStaticCouponsArrayList.size() > 0){
						Intent intent_editcoupon = new Intent(StoreOwnerCoupons.this,StoreOwnerAdd_EditCoupon.class);
						intent_editcoupon.putExtra("choosed_position", position);
						startActivityForResult(intent_editcoupon,100);	
					}else{
						Log.i(TAG, "No coupons Available");
					}

				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try{
			if(resultCode == RESULT_OK && requestCode == 100){
				mIsLoadMore = false;
				mCouponStart="0";
				mCouponEndLimit="20";
				mCouponTotalCount = "0";
				if(mStoreOwnerCouponsListView.getFooterViewsCount() == 0){
					mStoreOwnerCouponsListView.addFooterView(mFooterLayout);
					mFooterText.setText("");
					mFooterProgressBar.setVisibility(View.INVISIBLE);
				}
				if(data.getExtras().getString("coupon_type").equalsIgnoreCase("public")){
					coupontype = "Public";
					mPublicCouponType.setOnCheckedChangeListener(null);
					mPublicCouponType.setChecked(true);
					mPublicCouponType.setOnCheckedChangeListener(StoreOwnerCoupons.this);
					mPrivateCouponType.setOnCheckedChangeListener(null);
					mPrivateCouponType.setChecked(false);
					mPrivateCouponType.setOnCheckedChangeListener(StoreOwnerCoupons.this);
					// To clear Previous adapter from list
					ArrayList<Object> clearList = new ArrayList<Object>();
					mStoreOwnerCouponsListView.setAdapter(new StoreOwnerPublicCouponsAdapter(StoreOwnerCoupons.this,clearList,false));
					StoreOwnerCouponsAsyncTask mStoreOwnerCoupons = new StoreOwnerCouponsAsyncTask(StoreOwnerCoupons.this,"PROGRESS",mStoreOwnerCouponsListView);
					mStoreOwnerCoupons.execute("","Public");
				}else{
					coupontype = "Private";
					mPrivateCouponType.setOnCheckedChangeListener(null);
					mPrivateCouponType.setChecked(true);
					mPrivateCouponType.setOnCheckedChangeListener(StoreOwnerCoupons.this);
					mPublicCouponType.setOnCheckedChangeListener(null);
					mPublicCouponType.setChecked(false);
					mPublicCouponType.setOnCheckedChangeListener(StoreOwnerCoupons.this);
					mStoreOwnerCouponsListView.setAdapter(null);
					// To clear Previous adapter from list
					ArrayList<Object> clearList = new ArrayList<Object>();
					mStoreOwnerCouponsListView.setAdapter(new StoreOwnerPrivateCouponsAdapter(StoreOwnerCoupons.this,clearList,false));
					StoreOwnerCouponsAsyncTask mStoreOwnerCoupons = new StoreOwnerCouponsAsyncTask(StoreOwnerCoupons.this,"PROGRESS",mStoreOwnerCouponsListView);
					mStoreOwnerCoupons.execute("","Private");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public void SetArrayStoreOwnerCouponsListAdatpter(String mRefreshAdapterStatus, String mCouponType){
		if(mRefreshAdapterStatus.equalsIgnoreCase("RefreshAdapter") ){
			if(mCouponType.equalsIgnoreCase("Public") && mStoreOwnerPublicCouponsAdapter!=null){
				mStoreOwnerPublicCouponsAdapter.notifyDataSetChanged();	
			}else if(mCouponType.equalsIgnoreCase("Private") && mStoreOwnerPrivateCouponsAdapter!=null){
				mStoreOwnerPrivateCouponsAdapter.notifyDataSetChanged();
			}

		}else{
			if(mCouponType.equalsIgnoreCase("Public")){
				mStoreOwnerPublicCouponsAdapter = new StoreOwnerPublicCouponsAdapter(StoreOwnerCoupons.this,WebServiceStaticArrays.mStaticCouponsArrayList,true);
				mStoreOwnerCouponsListView.setAdapter(mStoreOwnerPublicCouponsAdapter);
			}else{
				mStoreOwnerPrivateCouponsAdapter = new StoreOwnerPrivateCouponsAdapter(StoreOwnerCoupons.this,WebServiceStaticArrays.mStaticCouponsArrayList,true);
				mStoreOwnerCouponsListView.setAdapter(mStoreOwnerPrivateCouponsAdapter);
			}
		}
		if(mFooterLayout != null && mStoreOwnerCouponsListView.getFooterViewsCount() !=0 &&mStoreOwnerCouponsListView.getAdapter() != null){
			Log.i(TAG, "Remove Footer View From Coupon List");
			mStoreOwnerCouponsListView.removeFooterView(mFooterLayout);
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
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwnerCoupons.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwnerCoupons.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwnerCoupons.this);
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
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwnerCouponsListView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		try{
			if(buttonView.equals(mPublicCouponType)){
				if(isChecked){
					mIsLoadMore = false;
					mCouponStart="0";
					mCouponEndLimit="20";
					mCouponTotalCount = "0";
					coupontype = "Public";
					if(mStoreOwnerCouponsListView.getFooterViewsCount() == 0){
						mStoreOwnerCouponsListView.addFooterView(mFooterLayout);
						mFooterText.setText("");
						mFooterProgressBar.setVisibility(View.INVISIBLE);
					}
					mPrivateCouponType.setOnCheckedChangeListener(null);
					mPrivateCouponType.setChecked(false);
					mPrivateCouponType.setOnCheckedChangeListener(this);
					// To clear Previous adapter from list
					ArrayList<Object> clearList = new ArrayList<Object>();
					mStoreOwnerCouponsListView.setAdapter(new StoreOwnerPublicCouponsAdapter(StoreOwnerCoupons.this,clearList,false));
					StoreOwnerCouponsAsyncTask mStoreOwnerCoupons = new StoreOwnerCouponsAsyncTask(StoreOwnerCoupons.this,"PROGRESS",mStoreOwnerCouponsListView);
					mStoreOwnerCoupons.execute("","Public");
				}else{
					if(!mPrivateCouponType.isChecked()){
						mPublicCouponType.setOnCheckedChangeListener(null);
						mPublicCouponType.setChecked(true);
						mPublicCouponType.setOnCheckedChangeListener(this);
					}
				}
			}else{
				if(isChecked){
					mIsLoadMore = false;
					mCouponStart="0";
					mCouponEndLimit="20";
					mCouponTotalCount = "0";
					coupontype = "Private";
					if(mStoreOwnerCouponsListView.getFooterViewsCount() == 0){
						mStoreOwnerCouponsListView.addFooterView(mFooterLayout);
						mFooterText.setText("");
						mFooterProgressBar.setVisibility(View.INVISIBLE);
					}
					mPublicCouponType.setOnCheckedChangeListener(null);
					mPublicCouponType.setChecked(false);
					mPublicCouponType.setOnCheckedChangeListener(this);
					// To clear Previous adapter from list
					ArrayList<Object> clearList = new ArrayList<Object>();
					mStoreOwnerCouponsListView.setAdapter(new StoreOwnerPrivateCouponsAdapter(StoreOwnerCoupons.this,clearList,false));
					StoreOwnerCouponsAsyncTask mStoreOwnerCoupons = new StoreOwnerCouponsAsyncTask(StoreOwnerCoupons.this,"PROGRESS",mStoreOwnerCouponsListView);
					mStoreOwnerCoupons.execute("","Private");				
				}else{
					if(!mPublicCouponType.isChecked()){
						mPrivateCouponType.setOnCheckedChangeListener(null);
						mPrivateCouponType.setChecked(true);
						mPrivateCouponType.setOnCheckedChangeListener(this);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(StoreOwnerCoupons.this);
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
