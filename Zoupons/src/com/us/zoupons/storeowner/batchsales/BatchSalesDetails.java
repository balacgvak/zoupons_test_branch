package com.us.zoupons.storeowner.batchsales;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class BatchSalesDetails extends Activity {

	public static String TAG="StoreOwnerBatchSales";
	public static MyHorizontalScrollView scrollView;
    View app;
    public Typeface mZouponsFont;
    public NetworkCheck mConnectionAvailabilityChecking;
	Header header;
	StoreOwner_LeftMenu storeowner_leftmenu;
	View mLeftMenu;
	int mMenuFlag;
	private Button mStoreOwnerBatchSaleFreezeView;
	private ImageView mStartDateContextImage,mEndDateContextImage;
	private ListView mBatchSalesList;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		mConnectionAvailabilityChecking = new NetworkCheck();
		app = inflater.inflate(R.layout.storeowner_batchsales, null);
		ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.storeowner_batchsales_middleview);
	    ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeowner_batchsales_footerLayoutId);
	    mStoreOwnerBatchSaleFreezeView = (Button) app.findViewById(R.id.storeowner_batchsale_freezeview);
	    storeowner_leftmenu = new StoreOwner_LeftMenu(BatchSalesDetails.this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerBatchSaleFreezeView, TAG);
	    mLeftMenu = storeowner_leftmenu.intializeInflater();
	    storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
	    /* Header Tab Bar which contains logout,notification and home buttons*/
	    header = (Header) app.findViewById(R.id.storeowner_batchsales_header);
	    header.intializeInflater(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerBatchSaleFreezeView, TAG);
	    final View[] children = new View[] { mLeftMenu, app};
	    /* Scroll to app (view[1]) when layout finished.*/
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
		mStoreOwnerBatchSaleFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerBatchSaleFreezeView, TAG));
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
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


