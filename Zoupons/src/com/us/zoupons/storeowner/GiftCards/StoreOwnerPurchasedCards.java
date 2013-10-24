package com.us.zoupons.storeowner.GiftCards;

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
import android.widget.EditText;
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

public class StoreOwnerPurchasedCards extends Activity {

	public static String TAG;
	
	public static MyHorizontalScrollView scrollView;
    View app;
    
    public Typeface mZouponsFont;
    public NetworkCheck mConnectionAvailabilityChecking;
	
	Header header;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;
	View mLeftMenu;
	int mMenuFlag;
	StoreOwner_LeftMenu storeowner_leftmenu;
	
	ImageView mRightMenuHolder;
	Button mPurchasedFreezeView;
	ListView mPurchasedListView;
	TextView mPurchasedAvailable,mPurchasedPurchased;
	public int mScreenWidth;
	public double mMenuWidth;
	
	public static String mPurchasedStart="0",mPurchasedEndLimit="20";
	public static String mPurchasedTotalCount = "0";
	
	PurchasedCardAdaper mPurchasedAdapter=null;
	
	TextView mPurchasedUserName,mPurchasedFaceValue,mPurchasedRemainingBalance;
	EditText mPurchasedSearchValue;
	Button mPurchasedSearchCancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();
		
		if(getIntent().getExtras().getString("classname").equalsIgnoreCase("StoreOwnerPurchased_GiftCards")){
			TAG="StoreOwnerPurchased_GiftCards";
		}else if(getIntent().getExtras().getString("classname").equalsIgnoreCase("StoreOwnerPurchased_DealCards")){
			TAG="StoreOwnerPurchased_DealCards";
		}
		
		//Call function to get width of the screen
		mScreenWidth=getScreenWidth(); 	
		if(mScreenWidth>0){	//To fix Home Page menubar items width
			mMenuWidth=mScreenWidth/2;
		}
				
		app = inflater.inflate(R.layout.storeowner_giftcards_deals_purchased, null);
		
		ViewGroup mTitleBar = (ViewGroup) app.findViewById(R.id.purchased_storename_header);
	    ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.purchased_container);
	    ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.purchased_footerLayoutId);
	    
	    mRightMenuHolder = (ImageView) mTitleBar.findViewById(R.id.purchased_rightmenu);
	    mPurchasedFreezeView = (Button) app.findViewById(R.id.purchased_freeze);
	    mPurchasedFaceValue = (TextView) mMiddleView.findViewById(R.id.purchased_facevaluetextId);
	    mPurchasedUserName = (TextView) mMiddleView.findViewById(R.id.purchased_username);
	    mPurchasedRemainingBalance = (TextView) mMiddleView.findViewById(R.id.purchased_remaining);
	    mPurchasedListView = (ListView) mMiddleView.findViewById(R.id.purchased_listId);
	    mPurchasedSearchValue = (EditText) mMiddleView.findViewById(R.id.purchased_searchId);
	    mPurchasedSearchCancel = (Button) mMiddleView.findViewById(R.id.purchased_search_buttonId);
	    
	    mPurchasedAvailable = (TextView) mFooterView.findViewById(R.id.purchased_availabletextId);
	    mPurchasedAvailable.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
	    mPurchasedPurchased = (TextView) mFooterView.findViewById(R.id.purchased_purchasedtextId);
	    mPurchasedPurchased.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
	    
	    mPurchasedAvailable.setBackgroundResource(R.drawable.header_2);
	    mPurchasedPurchased.setBackgroundResource(R.drawable.footer_dark_blue_new);
	    
		storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwnerPurchasedCards.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mPurchasedFreezeView, TAG);
	    mRightMenu = storeowner_rightmenu.intializeInflater();
	    storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwnerPurchasedCards.this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mPurchasedFreezeView, TAG);
	    mLeftMenu = storeowner_leftmenu.intializeInflater();
	    
	    storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
	    storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
	    
	    /* Header Tab Bar which contains logout,notification and home buttons*/
	    header = (Header) app.findViewById(R.id.purchased_header);
	    header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mPurchasedFreezeView, TAG);
	    
	    final View[] children = new View[] { mLeftMenu, app, mRightMenu };
	    
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
		
		mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag=2, mPurchasedFreezeView, TAG, mPurchasedSearchValue, false));
		mPurchasedFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag, mPurchasedFreezeView, TAG, mPurchasedSearchValue, true));
		
		PurchasedAsyncTask mPurchasedAsyncTask = new PurchasedAsyncTask(StoreOwnerPurchasedCards.this,"PROGRESS",mPurchasedListView,TAG);
		mPurchasedAsyncTask.execute();
		
		mPurchasedAvailable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
			    mPurchasedPurchased.setBackgroundResource(R.drawable.header_2);
				finish();
			}
		});
		
		mPurchasedSearchCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPurchasedSearchValue.getText().clear();
			}
		});
		
	}

	public void SetArrayPurchasedListAdatpter(String mRefreshAdapterStatus){
		if(!mRefreshAdapterStatus.equalsIgnoreCase("")){
			mPurchasedAdapter.notifyDataSetChanged();
		}else{
			mPurchasedAdapter = new PurchasedCardAdaper(StoreOwnerPurchasedCards.this,R.layout.purchased_list_row,TAG);
			mPurchasedListView.setAdapter(mPurchasedAdapter);
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
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mPurchasedListView.setVisibility(View.VISIBLE);
		mPurchasedSearchValue.setFocusable(true);
		mPurchasedSearchValue.setFocusableInTouchMode(true);
	}

}
