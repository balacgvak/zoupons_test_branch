package com.us.zoupons.storeowner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.us.zoupons.R;
import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.storeowner.DashBoard.StoreOwnerDashBoard;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.HomePage.StoreOwner_HomePage;

public class Header extends RelativeLayout{

	public ImageView mLeftMenuBtnSlide,mTabBarLogoutBtn,mTabBarNotificationBtn,mTabBarHomeBtn,mTabBarLoginChoice,mNotificationTriangle;
	public FrameLayout mTabBarNotificationContainer;
	public Button mTabBarNotificationCountBtn;
	Context mContext;
	
	HorizontalScrollView mScrollView;
	View mLeftMenu;
	int mMenuFlag;
	Button mFreezeView;
	String mTag;
	
	public Header(Context context) {
		super(context);
		this.mContext=context;
	}
	
	public Header(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    this.mContext=context;
	}

	public Header(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    this.mContext=context;
	}
	
	public void intializeInflater(HorizontalScrollView scrollView, View leftmenu, /*View rightmenu,*/int menuflag,Button freezeview,String tag){
		headerInflater(scrollView, leftmenu, /*rightmenu,*/ menuflag, freezeview, tag);
	}
	
	private void headerInflater(HorizontalScrollView scrollView, View leftmenu, /*View rightmenu,*/int menuflag,Button freezeview, String tag){
		
		this.mScrollView=scrollView;
		this.mLeftMenu=leftmenu;
		this.mMenuFlag=menuflag;
		this.mFreezeView=freezeview;
		this.mTag=tag;
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.header, this);
		
		mLeftMenuBtnSlide = (ImageView) findViewById(R.id.store_header_BtnSlide);
		mTabBarLogoutBtn = (ImageView) findViewById(R.id.store_header_logout_btn);
		mTabBarNotificationContainer = (FrameLayout) findViewById(R.id.store_header_notification_container);
		mTabBarNotificationBtn = (ImageView) findViewById(R.id.store_header_notificationImageId);
		mTabBarHomeBtn = (ImageView) findViewById(R.id.store_header_home);
		mTabBarLoginChoice = (ImageView) findViewById(R.id.store_header_loginchoice);
		mNotificationTriangle = (ImageView) findViewById(R.id.store_header_notification_triangle);
		mTabBarNotificationCountBtn = (Button) findViewById(R.id.store_header_notification_count);
		
		mLeftMenuBtnSlide.setOnClickListener(
				new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag)
		);
	    
	    mTabBarHomeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(StoreOwnerDashBoard.DASHBOARD_FLAG.equalsIgnoreCase("StoreInformation")){
					Intent intent_storeinfo = new Intent().setClass(mContext,StoreOwnerDashBoard.class);
					intent_storeinfo.putExtra("classname", "StoreInformation");
					mContext.startActivity(intent_storeinfo);
				}else{
					Intent intent_storeinfo = new Intent().setClass(mContext,StoreOwnerDashBoard.class);
					intent_storeinfo.putExtra("classname", "Locations");
					mContext.startActivity(intent_storeinfo);
				}
			}
		});
	    
	    mTabBarLoginChoice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent login_intent = new Intent(mContext,StoreOwner_HomePage.class);
				login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				login_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				mContext.startActivity(login_intent);
			}
		});
	    
	    mTabBarLogoutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// AsyncTask to call the logout webservice to end the login session
        		new LogoutSessionTask(mContext).execute();
				Intent intent_logout = new Intent(mContext,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				((Activity) mContext).finish();
				mContext.startActivity(intent_logout);
			}
		});
	    
	    mTabBarNotificationBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "Notification", Toast.LENGTH_SHORT).show();
			}
		});
	    
	}
		
}
