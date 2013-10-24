package com.us.zoupons.storeowner.Deals;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class StoreOwnerDealCards extends Activity {

	public static String TAG="StoreOwnerDealCards";
	
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
	Button mStoreOwnerDealCardsFreezeView;
	ListView mStoreOwnerDealCardsListView;
	TextView mStoreOwnerDealCardsAvailable,mStoreOwnerDealCardsPurchased,mStoreOwnerDealCardsHistory;
	public int mScreenWidth;
	public double mMenuWidth;
	
	public static String mStoreOwnerDealCardsStart="0",mStoreOwnerDealCardsEndLimit="20";
	public static String mStoreOwnerDealCardsTotalCount = "0";
	
	StoreOwnerDealCardAdapter mStoreOwnerDealCardsAdapter=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();
		
		//Call function to get width of the screen
		mScreenWidth=getScreenWidth(); 	
		if(mScreenWidth>0){	//To fix Home Page menubar items width
			mMenuWidth=mScreenWidth/3;
			Log.i(TAG,"ScreenWidth : "+mScreenWidth+"\n"+"MenuItemWidth : "+mMenuWidth);
		}
				
		app = inflater.inflate(R.layout.storeowner_dealcards, null);
		
		ViewGroup mTitleBar = (ViewGroup) app.findViewById(R.id.storeownerdealcards_storename_header);
	    ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.storeownerdealcards_container);
	    ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeownerdealcards_footerLayoutId);
	    
	    mRightMenuHolder = (ImageView) mTitleBar.findViewById(R.id.storeownerdealcards_rightmenu);
	    mStoreOwnerDealCardsFreezeView = (Button) app.findViewById(R.id.storeownerdealcards_freeze);
	    mStoreOwnerDealCardsListView = (ListView) mMiddleView.findViewById(R.id.storeownerdealcards_listId);
	    mStoreOwnerDealCardsAvailable = (TextView) mFooterView.findViewById(R.id.storeownerdealcards_availabletextId);
	    mStoreOwnerDealCardsAvailable.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
	    mStoreOwnerDealCardsPurchased = (TextView) mFooterView.findViewById(R.id.storeownerdealcards_purchasedtextId);
	    mStoreOwnerDealCardsPurchased.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
	    mStoreOwnerDealCardsHistory = (TextView) mFooterView.findViewById(R.id.storeownerdealcards_historytextId);
	    mStoreOwnerDealCardsHistory.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
	    
		storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwnerDealCards.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerDealCardsFreezeView, TAG);
	    mRightMenu = storeowner_rightmenu.intializeInflater();
	    storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwnerDealCards.this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerDealCardsFreezeView, TAG);
	    mLeftMenu = storeowner_leftmenu.intializeInflater();
	    
	    storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
	    storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
	    
	    /* Header Tab Bar which contains logout,notification and home buttons*/
	    header = (Header) app.findViewById(R.id.storeownerdealcards_header);
	    header.intializeInflater(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerDealCardsFreezeView, TAG);
	     final View[] children = new View[] { mLeftMenu, app, mRightMenu };
	    
		/* Scroll to app (view[1]) when layout finished.*/
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
		
		mStoreOwnerDealCardsAvailable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mStoreOwnerDealCardsPurchased.setBackgroundResource(R.drawable.header_2);
				mStoreOwnerDealCardsHistory.setBackgroundResource(R.drawable.header_2);
			}
		});
		
		mStoreOwnerDealCardsPurchased.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mStoreOwnerDealCardsHistory.setBackgroundResource(R.drawable.header_2);
				mStoreOwnerDealCardsAvailable.setBackgroundResource(R.drawable.header_2);
			}
		});

		mStoreOwnerDealCardsHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mStoreOwnerDealCardsAvailable.setBackgroundResource(R.drawable.header_2);
				mStoreOwnerDealCardsPurchased.setBackgroundResource(R.drawable.header_2);
			}
		});	
		
		mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwnerDealCardsFreezeView, TAG));
		mStoreOwnerDealCardsFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerDealCardsFreezeView, TAG));
		
		StoreOwnerDealCardsAsyncTask mStoreOwnerDealCards = new StoreOwnerDealCardsAsyncTask(StoreOwnerDealCards.this,"PROGRESS",mStoreOwnerDealCardsListView);
		mStoreOwnerDealCards.execute();
	}
	
	public void SetArrayStoreOwnerDealCardsListAdatpter(String mRefreshAdapterStatus){
		if(!mRefreshAdapterStatus.equalsIgnoreCase("")){
			mStoreOwnerDealCardsAdapter.notifyDataSetChanged();
		}else{
		/*	mStoreOwnerDealCardsAdapter = new StoreOwnerDealCardAdapter(StoreOwnerDealCards.this,R.layout.cardlist_row);
			mStoreOwnerDealCardsListView.setAdapter(mStoreOwnerDealCardsAdapter);*/
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
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwnerDealCardsListView.setVisibility(View.VISIBLE);
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
}
