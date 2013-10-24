package com.us.zoupons.storeowner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask.Status;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.Settings;
import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.storeowner.DashBoard.StoreOwnerDashBoard;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GiftCards.StoreOwnerGiftCards;
import com.us.zoupons.storeowner.Photos.StoreOwner_Photos;
import com.us.zoupons.storeowner.PointOfSale.StoreOwner_PointOfSale_Part1;
import com.us.zoupons.storeowner.batchsales.BatchSalesDetails;
import com.us.zoupons.storeowner.customercenter.CustomerCenter;
import com.us.zoupons.storeowner.invoice.InvoiceCenter;
import com.us.zoupons.storeowner.refunds.RefundDetails;

public class StoreOwner_LeftMenu {

	Context mContext;
	HorizontalScrollView mScrollView;
	View mLeftMenu,mMainView;
	int mMenuFlag;
	Button mFreezeView;
	String mTag;

	LinearLayout mStoreLeftMenu_DashBoard,mStoreLeftMenu_Customercenter,mStoreLeftMenu_InvoiceCenter,mStoreLeftMenu_PointOfSale,mStoreLeftMenu_Refund,mStoreLeftMenu_BatchSales,mStoreLeftMenu_Giftcards,mStoreLeftMenu_Settings,mStoreLeftMenu_DealCards,mStoreLeftMenu_LogOut;
	TextView mStoreLeftMenu_DashBoardText,mStoreLeftMenu_CustomercenterText,mStoreLeftMenu_InvoiceCenterText,mStoreLeftMenu_PointOfSaleText,mStoreLeftMenu_RefundText,mStoreLeftMenu_BatchSalesText,mStoreLeftMenu_GiftcardsText,mStoreLeftMenu_SettingsText,mStoreLeftMenu_LogOutText,mStoreLeftMenu_DealcardsText;
	ImageView mStoreLeftMenu_DashBoardImage,mStoreLeftMenu_CustomercenterImage,mStoreLeftMenu_InvoiceCenterImage,mStoreLeftMenu_PointOfSaleImage,mStoreLeftMenu_RefundImage,mStoreLeftMenu_BatchSalesImage,mStoreLeftMenu_GiftcardsImage,mStoreLeftMenu_SettingsImage,mStoreLeftMenu_LogOutImage,mStoreLeftMenu_DealcardsImage; 
	Typeface mZouponsFont;

	public StoreOwner_LeftMenu(Context context,HorizontalScrollView scrollView, View leftmenu, /*View rightmenu,*/int menuflag,Button freezeview,String tag){
		this.mContext=context;
		this.mScrollView=scrollView;
		this.mLeftMenu=leftmenu;
		this.mMenuFlag=menuflag;
		this.mFreezeView=freezeview;
		this.mTag=tag;
		
		if(StoreOwner_Photos.storePhotoLoaderAsyncTask!=null){
			if(StoreOwner_Photos.storePhotoLoaderAsyncTask.getStatus()==Status.RUNNING){
				StoreOwner_Photos.storePhotoLoaderAsyncTask.cancel(true);
				StoreOwner_Photos.storePhotoLoaderAsyncTask=null;
			}
		}
	}

	public View intializeInflater(){
		return leftMenuInflater();
	}

	private View leftMenuInflater(){

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mLeftMenuItems = inflater.inflate(R.layout.leftmenu_store, null);
		try{
			mStoreLeftMenu_DashBoard= (LinearLayout) mLeftMenuItems.findViewById(R.id.store_leftmenu_dashboard);
			mStoreLeftMenu_Customercenter= (LinearLayout) mLeftMenuItems.findViewById(R.id.store_leftmenu_CustomerCenter);
			mStoreLeftMenu_InvoiceCenter= (LinearLayout) mLeftMenuItems.findViewById(R.id.store_leftmenu_invoicecenter);
			mStoreLeftMenu_PointOfSale= (LinearLayout) mLeftMenuItems.findViewById(R.id.store_leftmenu_pointofsale);
			mStoreLeftMenu_Refund= (LinearLayout) mLeftMenuItems.findViewById(R.id.store_leftmenu_refund);
			mStoreLeftMenu_BatchSales= (LinearLayout) mLeftMenuItems.findViewById(R.id.store_leftmenu_batchsales);
			mStoreLeftMenu_Giftcards= (LinearLayout) mLeftMenuItems.findViewById(R.id.store_leftmenu_giftcards);
			mStoreLeftMenu_Settings= (LinearLayout) mLeftMenuItems.findViewById(R.id.store_leftmenu_settings);
			mStoreLeftMenu_DealCards= (LinearLayout) mLeftMenuItems.findViewById(R.id.store_leftmenu_dealcards);
			mStoreLeftMenu_LogOut= (LinearLayout) mLeftMenuItems.findViewById(R.id.store_leftmenu_logout);

			mStoreLeftMenu_DashBoardText = (TextView) mLeftMenuItems.findViewById(R.id.menuDashboard);
			mStoreLeftMenu_CustomercenterText= (TextView) mLeftMenuItems.findViewById(R.id.store_leftmenu_CustomerCentertext);
			mStoreLeftMenu_InvoiceCenterText= (TextView) mLeftMenuItems.findViewById(R.id.menuInvoiceCenter);
			mStoreLeftMenu_PointOfSaleText= (TextView) mLeftMenuItems.findViewById(R.id.menuPointofsale);
			mStoreLeftMenu_RefundText= (TextView) mLeftMenuItems.findViewById(R.id.menuRefund);
			mStoreLeftMenu_BatchSalesText= (TextView) mLeftMenuItems.findViewById(R.id.menuBatchSales);
			mStoreLeftMenu_GiftcardsText= (TextView) mLeftMenuItems.findViewById(R.id.store_rightmenu_giftcardstext);
			mStoreLeftMenu_DealcardsText = (TextView) mLeftMenuItems.findViewById(R.id.menudealcards);

			mStoreLeftMenu_DashBoardImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuimage);
			mStoreLeftMenu_CustomercenterImage = (ImageView) mLeftMenuItems.findViewById(R.id.store_leftmenu_CustomerCenterimage);
			mStoreLeftMenu_InvoiceCenterImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuInvoiceCenterimage);
			mStoreLeftMenu_PointOfSaleImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuPointofsaleimage);
			mStoreLeftMenu_RefundImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuRefundimage);
			mStoreLeftMenu_BatchSalesImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuBatchSalesimage);
			mStoreLeftMenu_GiftcardsImage = (ImageView) mLeftMenuItems.findViewById(R.id.store_leftmenu_giftcardsImage);
			mStoreLeftMenu_DealcardsImage = (ImageView) mLeftMenuItems.findViewById(R.id.dealcardsmenuimage);
			
			mZouponsFont=Typeface.createFromAsset(mContext.getAssets(), "helvetica.ttf");
			mStoreLeftMenu_DashBoardText.setTypeface(mZouponsFont);
			mStoreLeftMenu_CustomercenterText.setTypeface(mZouponsFont);
			mStoreLeftMenu_InvoiceCenterText.setTypeface(mZouponsFont);
			mStoreLeftMenu_PointOfSaleText.setTypeface(mZouponsFont);
			mStoreLeftMenu_RefundText.setTypeface(mZouponsFont);
			mStoreLeftMenu_BatchSalesText.setTypeface(mZouponsFont);
			mStoreLeftMenu_GiftcardsText.setTypeface(mZouponsFont);
			mStoreLeftMenu_DealcardsText.setTypeface(mZouponsFont);
			// to get user type from preference
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String user_type = mPrefs.getString("user_type", "");
			if(user_type.equalsIgnoreCase("store_employee")){
				enable_disableMenu(mContext, mStoreLeftMenu_Customercenter,mStoreLeftMenu_CustomercenterText,mStoreLeftMenu_CustomercenterImage ,mPrefs.getString("customer_center_access", ""));
				enable_disableMenu(mContext, mStoreLeftMenu_InvoiceCenter,mStoreLeftMenu_InvoiceCenterText,mStoreLeftMenu_InvoiceCenterImage ,mPrefs.getString("invoice_center_access", ""));
				enable_disableMenu(mContext, mStoreLeftMenu_PointOfSale,mStoreLeftMenu_PointOfSaleText,mStoreLeftMenu_PointOfSaleImage ,mPrefs.getString("point_of_sale_access", ""));
				enable_disableMenu(mContext, mStoreLeftMenu_Refund, mStoreLeftMenu_RefundText,mStoreLeftMenu_RefundImage ,mPrefs.getString("refund_access", ""));
				enable_disableMenu(mContext, mStoreLeftMenu_BatchSales,mStoreLeftMenu_BatchSalesText,mStoreLeftMenu_BatchSalesImage ,mPrefs.getString("batch_sales_access", ""));
				enable_disableMenu(mContext, mStoreLeftMenu_Giftcards, mStoreLeftMenu_GiftcardsText, mStoreLeftMenu_GiftcardsImage,mPrefs.getString("gift_cards_access", ""));
				enable_disableMenu(mContext, mStoreLeftMenu_DealCards,mStoreLeftMenu_DealcardsText, mStoreLeftMenu_DealcardsImage,mPrefs.getString("deal_cards_access", ""));
			}else{}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mLeftMenuItems;
	}

	public void clickListener (View leftmenu){

		this.mLeftMenu=leftmenu;

		mStoreLeftMenu_DashBoard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerDashBoard")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else{
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_home = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerDashBoard.class);
					intent_home.putExtra("classname", "Locations");
					mContext.startActivity(intent_home);
				}
			}
		});

		mStoreLeftMenu_Customercenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_CustomerCenter")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/*, mRightMenu, */mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_rightmenuCustomercenter = new Intent().setClass(mContext.getApplicationContext(),CustomerCenter.class);
					mContext.startActivity(intent_rightmenuCustomercenter);
				}
			}
		});

		mStoreLeftMenu_InvoiceCenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerInvoiceCenter")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_invoicecenter = new Intent().setClass(mContext.getApplicationContext(),InvoiceCenter.class);
					mContext.startActivity(intent_invoicecenter);
				}
			}
		});

		mStoreLeftMenu_PointOfSale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_PointOfSale_Part1")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_pointofsale = new Intent().setClass(mContext.getApplicationContext(),StoreOwner_PointOfSale_Part1.class);
					mContext.startActivity(intent_pointofsale);
				}
			}


		});

		mStoreLeftMenu_Refund.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerRefunds")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_refunds = new Intent().setClass(mContext.getApplicationContext(),RefundDetails.class);
					mContext.startActivity(intent_refunds);
				}
			}
		});

		mStoreLeftMenu_BatchSales.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerBatchSales")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_batchsales = new Intent().setClass(mContext.getApplicationContext(),BatchSalesDetails.class);
					mContext.startActivity(intent_batchsales);
				}
			}
		});

		mStoreLeftMenu_Giftcards.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerGiftCards")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_rightmenugiftcards = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerGiftCards.class);
					intent_rightmenugiftcards.putExtra("classname", "StoreOwnerGiftCards");
					mContext.startActivity(intent_rightmenugiftcards);
				}
			}
		});

		mStoreLeftMenu_Settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_Settings")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_settings = new Intent().setClass(mContext.getApplicationContext(),Settings.class);
					intent_settings.putExtra("Store_Owner_Settings", true);
					mContext.startActivity(intent_settings);
				}
			}
		});

		mStoreLeftMenu_DealCards.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerDealCards")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					Intent intent_rightmenudeals = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerGiftCards.class);
					intent_rightmenudeals.putExtra("classname", "StoreOwnerDealCards");
					mContext.startActivity(intent_rightmenudeals);
				}
			}
		});

		mStoreLeftMenu_LogOut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(mContext).execute();
				Intent intent_logout = new Intent(mContext,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				((Activity) mContext).finish();
				mContext.startActivity(intent_logout);
			}
		});
	}

	public void enable_disableMenu(Context context,LinearLayout menu,TextView text,ImageView image,String result){

		if(result.equalsIgnoreCase("no")){
			menu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			text.setTextColor(Color.GRAY);
			image.setAlpha(100);
			menu.setEnabled(false);
		}
	}

}
