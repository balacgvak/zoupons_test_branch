package com.us.zoupons.storeowner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.shopper.settings.Settings;
import com.us.zoupons.storeowner.batchsales.BatchSalesDetails;
import com.us.zoupons.storeowner.customercenter.CustomerCenter;
import com.us.zoupons.storeowner.customercenter.StoreOwner_chatsupport;
import com.us.zoupons.storeowner.dashBoard.StoreOwnerDashBoard;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.giftcards_deals.StoreOwnerGiftCards;
import com.us.zoupons.storeowner.invoice.InvoiceCenter;
import com.us.zoupons.storeowner.pointofsale.StoreOwner_PointOfSale_Part1;
import com.us.zoupons.storeowner.refunds.RefundDetails;

/**
 * 
 * Class to manage store module left menu 
 *
 */

public class StoreOwner_LeftMenu {

	// Initializing views and variables
	private Activity mContext;
	private HorizontalScrollView mScrollView;
	private View mLeftMenu;
	private int mMenuFlag;
	private Button mFreezeView;
	private String mTag;
	private LinearLayout mStoreLeftMenu_DashBoard,mStoreLeftMenu_Customercenter,mStoreLeftMenu_InvoiceCenter,mStoreLeftMenu_PointOfSale,mStoreLeftMenu_Refund,mStoreLeftMenu_BatchSales,mStoreLeftMenu_Giftcards,mStoreLeftMenu_Settings,mStoreLeftMenu_DealCards,mStoreLeftMenu_LogOut;
	private TextView mStoreLeftMenu_DashBoardText,mStoreLeftMenu_CustomercenterText,mStoreLeftMenu_InvoiceCenterText,mStoreLeftMenu_PointOfSaleText,mStoreLeftMenu_RefundText,mStoreLeftMenu_BatchSalesText,mStoreLeftMenu_GiftcardsText,mStoreLeftMenu_SettingsText,mStoreLeftMenu_LogOutText,mStoreLeftMenu_DealcardsText;
	private ImageView mStoreLeftMenu_CustomercenterImage,mStoreLeftMenu_InvoiceCenterImage,mStoreLeftMenu_PointOfSaleImage,mStoreLeftMenu_RefundImage,mStoreLeftMenu_BatchSalesImage,mStoreLeftMenu_GiftcardsImage,mStoreLeftMenu_DealcardsImage; 
	private Typeface mZouponsFont;

	public StoreOwner_LeftMenu(Activity context,HorizontalScrollView scrollView, View leftmenu,int menuflag,Button freezeview,String tag){
		this.mContext=context;
		this.mScrollView=scrollView;
		this.mLeftMenu=leftmenu;
		this.mMenuFlag=menuflag;
		this.mFreezeView=freezeview;
		this.mTag=tag;
	}

	public View intializeInflater(){
		return leftMenuInflater();
	}

	// Initializing left menu items
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
			mStoreLeftMenu_SettingsText = (TextView) mLeftMenuItems.findViewById(R.id.menuSettings);
			mStoreLeftMenu_LogOutText = (TextView) mLeftMenuItems.findViewById(R.id.menuLogout);

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
			mStoreLeftMenu_SettingsText.setTypeface(mZouponsFont);
			mStoreLeftMenu_LogOutText.setTypeface(mZouponsFont);
			
			// to enable and disable sliding menu based upon store types from preference file
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			enable_disableMenu(mContext, mStoreLeftMenu_Customercenter,mStoreLeftMenu_CustomercenterText,mStoreLeftMenu_CustomercenterImage ,mPrefs.getString("customer_center_access", ""));
			enable_disableMenu(mContext, mStoreLeftMenu_InvoiceCenter,mStoreLeftMenu_InvoiceCenterText,mStoreLeftMenu_InvoiceCenterImage ,mPrefs.getString("invoice_center_access", ""));
			enable_disableMenu(mContext, mStoreLeftMenu_PointOfSale,mStoreLeftMenu_PointOfSaleText,mStoreLeftMenu_PointOfSaleImage ,mPrefs.getString("point_of_sale_access", ""));
			enable_disableMenu(mContext, mStoreLeftMenu_Refund, mStoreLeftMenu_RefundText,mStoreLeftMenu_RefundImage ,mPrefs.getString("refund_access", ""));
			enable_disableMenu(mContext, mStoreLeftMenu_BatchSales,mStoreLeftMenu_BatchSalesText,mStoreLeftMenu_BatchSalesImage ,mPrefs.getString("batch_sales_access", ""));
			enable_disableMenu(mContext, mStoreLeftMenu_Giftcards, mStoreLeftMenu_GiftcardsText, mStoreLeftMenu_GiftcardsImage,mPrefs.getString("gift_cards_access", ""));
			enable_disableMenu(mContext, mStoreLeftMenu_DealCards,mStoreLeftMenu_DealcardsText, mStoreLeftMenu_DealcardsImage,mPrefs.getString("deal_cards_access", ""));
			
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
				stopChatTimerIfRunning(); // To Stop communication timer
				if(mTag.equalsIgnoreCase("StoreOwnerDashBoard")){  // Close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else{ // Launch DashBoard activity if we click DASHBOARD from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_home = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerDashBoard.class);
					intent_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					intent_home.putExtra("classname", "Locations");
					mContext.startActivity(intent_home);
					mContext.finish();
				}
				
			}
		});

		mStoreLeftMenu_Customercenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				stopChatTimerIfRunning(); // To Stop communication timer
				if(mTag.equalsIgnoreCase("StoreOwner_CustomerCenter")){ // Close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/*, mRightMenu, */mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch Customercenter activity if we click CUSTOMER CENTER from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_rightmenuCustomercenter = new Intent().setClass(mContext.getApplicationContext(),CustomerCenter.class);
					intent_rightmenuCustomercenter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_rightmenuCustomercenter);
					mContext.finish();
				}
				
			}
		});

		mStoreLeftMenu_InvoiceCenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				stopChatTimerIfRunning(); // To Stop communication timer
				if(mTag.equalsIgnoreCase("StoreOwnerInvoiceCenter")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch Invoice center activity if we click INVOICE CENTER from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_invoicecenter = new Intent().setClass(mContext.getApplicationContext(),InvoiceCenter.class);
					intent_invoicecenter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_invoicecenter);
					mContext.finish();
				}
				
			}
		});

		mStoreLeftMenu_PointOfSale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				stopChatTimerIfRunning(); // To Stop communication timer
				if(mTag.equalsIgnoreCase("StoreOwner_PointOfSale_Part1")){ // To close Sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch Point of sale activity if we click POS from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_pointofsale = new Intent().setClass(mContext.getApplicationContext(),StoreOwner_PointOfSale_Part1.class);
					intent_pointofsale.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_pointofsale);
					mContext.finish();
				}
			}
		});

		mStoreLeftMenu_Refund.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopChatTimerIfRunning(); // To Stop communication timer
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerRefunds")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch REFUND activity if we click REFUND from other activitys
					/*MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_refunds = new Intent().setClass(mContext.getApplicationContext(),RefundDetails.class);
					intent_refunds.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_refunds);
					mContext.finish();*/
				}
				
			}
		});

		mStoreLeftMenu_BatchSales.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				stopChatTimerIfRunning(); // To Stop communication timer
				if(mTag.equalsIgnoreCase("StoreOwnerBatchSales")){ // To Close Sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch Batch sales activity if we click BATCH SALES from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT; 
					Intent intent_batchsales = new Intent().setClass(mContext.getApplicationContext(),BatchSalesDetails.class);
					intent_batchsales.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_batchsales);
					mContext.finish();
				}
				
			}
		});

		mStoreLeftMenu_Giftcards.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopChatTimerIfRunning(); // To Stop communication timer
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerGiftCards")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch Giftcards activity if we click GIFTCARDS from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_rightmenugiftcards = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerGiftCards.class);
					intent_rightmenugiftcards.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					intent_rightmenugiftcards.putExtra("classname", "StoreOwnerGiftCards");
					mContext.startActivity(intent_rightmenugiftcards);
					mContext.finish();
				}
				
			}
		});

		mStoreLeftMenu_Settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopChatTimerIfRunning(); // To Stop communication timer
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_Settings")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch Setting activity if we click SETTINGS from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_settings = new Intent().setClass(mContext.getApplicationContext(),Settings.class);
					intent_settings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					intent_settings.putExtra("Store_Owner_Settings", true);
					mContext.startActivity(intent_settings);
					mContext.finish();
				}
				
			}
		});

		mStoreLeftMenu_DealCards.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				stopChatTimerIfRunning(); // To Stop communication timer
				if(mTag.equalsIgnoreCase("StoreOwnerDealCards")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch Deals activity if we click DEAL CARDS from other activitys
					Intent intent_rightmenudeals = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerGiftCards.class);
					intent_rightmenudeals.putExtra("classname", "StoreOwnerDealCards");
					intent_rightmenudeals.putExtra("show_deals", false);
					intent_rightmenudeals.putExtra("show_available_deals", true);
					intent_rightmenudeals.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_rightmenudeals);
					mContext.finish();
				}
				
			}
		});

		mStoreLeftMenu_LogOut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(mContext,"FromManualLogOut").execute();
				stopChatTimerIfRunning(); // To Stop communication timer
			}
		});
	}

	// To enable or disable sliding menu based upon store_type and permission type
	public void enable_disableMenu(Context context,LinearLayout menu,TextView text,ImageView image,String result){
		if(result.equalsIgnoreCase("no")){
			menu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			text.setTextColor(Color.GRAY);
			image.setAlpha(100);
			menu.setEnabled(false);
		}
	}
	
	// To stop communication timer if running
	private void stopChatTimerIfRunning(){
		if(StoreOwner_chatsupport.sCommunicationTimer!=null){ // To cancel chat timer
			StoreOwner_chatsupport.sCommunicationTimer.cancel();
			StoreOwner_chatsupport.sCommunicationTimer=null;
		}if(StoreOwner_chatsupport.sCommunicationTimerTask!=null){
			StoreOwner_chatsupport.sCommunicationTimerTask.cancel();
			StoreOwner_chatsupport.sCommunicationTimerTask=null;
		}
	}

}
