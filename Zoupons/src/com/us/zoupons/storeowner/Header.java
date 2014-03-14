package com.us.zoupons.storeowner;

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

import com.us.zoupons.R;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.storeowner.customercenter.StoreOwner_chatsupport;
import com.us.zoupons.storeowner.dashBoard.StoreOwnerDashBoard;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.homepage.StoreOwner_HomePage;

/**
 * 
 * General class to manage zoupons header
 *
 */

public class Header extends RelativeLayout{

	private Context mContext;
	private HorizontalScrollView mScrollView;
	public ImageView mLeftMenuBtnSlide,mTabBarLogoutBtn,mTabBarNotificationImage,mTabBarHomeBtn,mTabBarLoginChoice,mTabBarNotificationTriangle;
	public FrameLayout mTabBarNotificationContainer;
	public Button mTabBarNotificationCountBtn;
	private View mLeftMenu;
	private int mMenuFlag;
	private Button mFreezeView;
	private String mTag;
	
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
	
	public void intializeInflater(HorizontalScrollView scrollView, View leftmenu, /*View sRightmenu,*/int menuflag,Button freezeview,String tag){
		headerInflater(scrollView, leftmenu, /*sRightmenu,*/ menuflag, freezeview, tag);
	}
	
	// Initializing header
	private void headerInflater(HorizontalScrollView scrollView, View leftmenu, /*View sRightmenu,*/int menuflag,Button freezeview, String tag){
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
		mTabBarNotificationImage = (ImageView) findViewById(R.id.store_header_notificationImageId);
		mTabBarHomeBtn = (ImageView) findViewById(R.id.store_header_home);
		mTabBarLoginChoice = (ImageView) findViewById(R.id.store_header_loginchoice);
		mTabBarNotificationTriangle = (ImageView) findViewById(R.id.store_header_notification_triangle);
		mTabBarNotificationCountBtn = (Button) findViewById(R.id.store_header_notification_count);
		
		mLeftMenuBtnSlide.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag));
	    
	    mTabBarHomeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent_storeinfo = new Intent().setClass(mContext,StoreOwnerDashBoard.class);
				intent_storeinfo.putExtra("classname", "Locations");
				intent_storeinfo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent_storeinfo);
				stopTimerIfRunning();
			}
		});
	    
	    mTabBarLoginChoice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent login_intent = new Intent(mContext,StoreOwner_HomePage.class);
				login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				login_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				mContext.startActivity(login_intent);
				stopTimerIfRunning();
			}
		});
	    
	    mTabBarLogoutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// AsyncTask to call the logout webservice to end the login session
        		new LogoutSessionTask(mContext,"FromManualLogOut").execute();
			}
		});
	    
	    mTabBarNotificationImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(mContext, "Notification", Toast.LENGTH_SHORT).show();
			}
		});
	    
	}
	
	// Function to stop chat timer if it's running
	private void stopTimerIfRunning(){
		if(StoreOwner_chatsupport.sCommunicationTimer!=null){ // To cancel chat timer
			StoreOwner_chatsupport.sCommunicationTimer.cancel();
			StoreOwner_chatsupport.sCommunicationTimer=null;
		}if(StoreOwner_chatsupport.sCommunicationTimerTask!=null){
			StoreOwner_chatsupport.sCommunicationTimerTask.cancel();
			StoreOwner_chatsupport.sCommunicationTimerTask=null;
		}
	}
		
}
