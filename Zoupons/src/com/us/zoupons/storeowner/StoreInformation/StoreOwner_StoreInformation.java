package com.us.zoupons.storeowner.StoreInformation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;
import com.us.zoupons.storeowner.HomePage.StoreOwner_HomePage;

public class StoreOwner_StoreInformation extends Activity {
	
	public static String TAG="StoreOwner_StoreInformation";
	
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
	
	Button mStoreOwner_StoreInformation_FreezeView;
	ListView mStoreOwner_StoreInformation_ListView;
	LinearLayout mBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();
		
		app = inflater.inflate(R.layout.storeowner_storeinformation, null);
		
		ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.middleview);
		ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeownerstoreinformation_footer);
		
		mStoreOwner_StoreInformation_FreezeView = (Button) app.findViewById(R.id.freezeview);
		mStoreOwner_StoreInformation_ListView = (ListView) mMiddleView.findViewById(R.id.storeownerstoreinformationListView);
	    mBack = (LinearLayout) mFooterView.findViewById(R.id.storeownerstoreinformation_footer_back);
		
		storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwner_StoreInformation.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_StoreInformation_FreezeView, TAG);
	    mRightMenu = storeowner_rightmenu.intializeInflater();
	    storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_StoreInformation.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_StoreInformation_FreezeView, TAG);
	    mLeftMenu = storeowner_leftmenu.intializeInflater();
	    
	    storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
	    storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
	    
	    /* Header Tab Bar which contains logout,notification and home buttons*/
	    header = (Header) app.findViewById(R.id.header);
	    header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_StoreInformation_FreezeView, TAG);
	    
	    final View[] children = new View[] { mLeftMenu, app, mRightMenu };

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
		
		mStoreOwner_StoreInformation_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_StoreInformation_FreezeView, TAG));
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				Intent intent_storeownerhomepage = new Intent(StoreOwner_StoreInformation.this,StoreOwner_HomePage.class);
				startActivity(intent_storeownerhomepage);
			}
		});
		
		StoreOwnerStoreInformationAsynchTask mStoreOwnerStoreInformation = new StoreOwnerStoreInformationAsynchTask(StoreOwner_StoreInformation.this,"PROGRESS",mStoreOwner_StoreInformation_ListView,TAG);
		mStoreOwnerStoreInformation.execute();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwner_StoreInformation_ListView.setVisibility(View.VISIBLE);
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
