package com.us.zoupons.storeowner.coupons;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
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
 * Activity which lists public and private coupons
 *
 */

public class StoreOwnerCoupons extends Activity implements OnCheckedChangeListener{

	// Initializing views and variables
	private String mTAG="StoreOwnerCoupons";
	private MyHorizontalScrollView mScrollView;
	private View mApp;
	private Header mZouponsheader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private View mRightMenu;
	private View mLeftMenu;
	private int mMenuFlag;
	private ImageView mRightMenuHolder;
	private Button mStoreOwnerCouponsFreezeView;
	private ListView mStoreOwnerCouponsListView;
	private TextView mStoreOwnerCouponsInvisible1,mStoreOwnerCouponsInvisible2,mStoreOwnerCouponsAddCoupon,mStoreOwnerStoreNameText;
	private int mScreenWidth;
	private double mMenuWidth;
	private StoreOwnerPublicCouponsAdapter mStoreOwnerPublicCouponsAdapter=null;
	private StoreOwnerPrivateCouponsAdapter mStoreOwnerPrivateCouponsAdapter=null;
	private CheckBox mPublicCouponType,mPrivateCouponType;
	public static boolean mIsLoadMore;
	public static String sCouponStart="0",sCouponEndLimit="20",sCouponTotalCount = "0";	
	private LayoutInflater mInflater;
	public View mFooterLayout;
	private TextView mFooterText;
	private ProgressBar mFooterProgressBar;
	private String mCoupontype="Public";
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
			//Call function to get width of the screen
			mScreenWidth=getScreenWidth(); 	
			if(mScreenWidth>0){	//To fix Home Page menubar items width
				mMenuWidth=mScreenWidth/3;
			}
			mApp = inflater.inflate(R.layout.storeowner_coupons, null);
			ViewGroup mTitleBar = (ViewGroup) mApp.findViewById(R.id.storeownercoupons_storename_header);
			ViewGroup mMiddleView = (ViewGroup) mApp.findViewById(R.id.storeownercoupons_container);
			ViewGroup mFooterView = (ViewGroup) mApp.findViewById(R.id.storeownercoupons_footerLayoutId);
			mRightMenuHolder = (ImageView) mTitleBar.findViewById(R.id.storeownercoupons_rightmenu);
			mStoreOwnerStoreNameText = (TextView) mMiddleView.findViewById(R.id.storeownercoupons_title_textId);
			mStoreOwnerCouponsFreezeView = (Button) mApp.findViewById(R.id.storeownercoupons_freeze);
			mStoreOwnerCouponsListView = (ListView) mMiddleView.findViewById(R.id.storeownercoupons_CouponsList);
			mStoreOwnerCouponsInvisible1 = (TextView) mFooterView.findViewById(R.id.storeownercoupons_availabletextId);
			mStoreOwnerCouponsInvisible1.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
			mStoreOwnerCouponsInvisible2 = (TextView) mFooterView.findViewById(R.id.storeownercoupons_purchasedtextId);
			mStoreOwnerCouponsInvisible2.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
			mStoreOwnerCouponsAddCoupon = (TextView) mFooterView.findViewById(R.id.storeownercoupons_addcoupontextId);
			mStoreOwnerCouponsAddCoupon.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
			mStoreownerRightmenu = new StoreOwner_RightMenu(StoreOwnerCoupons.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerCouponsFreezeView, mTAG);
			mRightMenu = mStoreownerRightmenu.intializeInflater();
			mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreOwnerCoupons.this,mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerCouponsFreezeView, mTAG);
			mLeftMenu = mStoreownerLeftmenu.intializeInflater();
			mStoreownerLeftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			mStoreownerRightmenu.clickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsheader = (Header) mApp.findViewById(R.id.storeownercoupons_header);
			mZouponsheader.intializeInflater(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerCouponsFreezeView, mTAG);
			final View[] children = new View[] { mLeftMenu, mApp, mRightMenu };
			/* Scroll to app (view[1]) when layout finished.*/
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsheader.mLeftMenuBtnSlide));
			mNotificationReceiver = new NotifitcationReceiver(mZouponsheader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsheader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsheader,mZouponsheader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsheader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsheader,mZouponsheader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mPublicCouponType = (CheckBox) mMiddleView.findViewById(R.id.public_coupons_checkboxId);
			mPrivateCouponType = (CheckBox) mMiddleView.findViewById(R.id.private_coupons_checkboxId);
			mIsLoadMore = false;
			sCouponStart="0";
			sCouponEndLimit="20";
			sCouponTotalCount = "0";
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
			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwnerCouponsFreezeView, mTAG));
			mStoreOwnerCouponsFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerCouponsFreezeView, mTAG));
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
						if(Integer.parseInt(sCouponTotalCount) > lastInScreen && Integer.parseInt(sCouponTotalCount)>20){					
							if(lastInScreen == totalItemCount && !mIsLoadMore){												
								if(mStoreOwnerCouponsListView.getFooterViewsCount() == 0){
									mStoreOwnerCouponsListView.addFooterView(mFooterLayout);
								}
								if(mFooterProgressBar.getVisibility() != View.VISIBLE){
									mFooterProgressBar.setVisibility(View.VISIBLE);
								}
								
								if(Integer.parseInt(sCouponTotalCount) > Integer.parseInt(sCouponEndLimit)){
									mFooterText.setText("Loading "+sCouponEndLimit+" of "+"("+sCouponTotalCount+")");
								}else{
									mFooterText.setText("Loading "+sCouponTotalCount);
								}
								
								Log.i(mTAG, "Runn AynckTask To Add More");
								StoreOwnerCouponsAsyncTask mStoreOwnerCoupons = new StoreOwnerCouponsAsyncTask(StoreOwnerCoupons.this,"NOPROGRESS",mStoreOwnerCouponsListView);
								mStoreOwnerCoupons.execute("RefreshAdapter",mCoupontype);
							}
						}else{
							if(sCouponTotalCount.equalsIgnoreCase("0")){
								Log.i(mTAG, "Currently No List Item");
							}else if(mFooterLayout != null && mStoreOwnerCouponsListView.getFooterViewsCount() !=0 && mStoreOwnerCouponsListView.getAdapter() != null){
								Log.i(mTAG, "Remove Footer");
								if(lastInScreen!= totalItemCount){
									Log.i(mTAG, "Remove Footer success");
									mStoreOwnerCouponsListView.removeFooterView(mFooterLayout);	
								}else{
									Log.i(mTAG, "Remove Footer wait");
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
						Log.i(mTAG, "No coupons Available");
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
				sCouponStart="0";
				sCouponEndLimit="20";
				sCouponTotalCount = "0";
				if(mStoreOwnerCouponsListView.getFooterViewsCount() == 0){
					mStoreOwnerCouponsListView.addFooterView(mFooterLayout);
					mFooterText.setText("");
					mFooterProgressBar.setVisibility(View.INVISIBLE);
				}
				if(data.getExtras().getString("coupon_type").equalsIgnoreCase("public")){
					mCoupontype = "Public";
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
					mCoupontype = "Private";
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
			Log.i(mTAG, "Remove Footer View From Coupon List");
			mStoreOwnerCouponsListView.removeFooterView(mFooterLayout);
		}
	}

	/*Get Screen width*/
	public int getScreenWidth(){
		int Measuredwidth = 0;  
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Measuredwidth = display.getWidth(); 
		return Measuredwidth;
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
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwnerCoupons.this,ZouponsConstants.sStoreModuleFlag);
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
					sCouponStart="0";
					sCouponEndLimit="20";
					sCouponTotalCount = "0";
					mCoupontype = "Public";
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
					sCouponStart="0";
					sCouponEndLimit="20";
					sCouponTotalCount = "0";
					mCoupontype = "Private";
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
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwnerCoupons.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}
		
}
