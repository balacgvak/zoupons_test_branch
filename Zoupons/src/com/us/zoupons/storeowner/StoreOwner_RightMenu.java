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
import com.us.zoupons.shopper.receipts.Receipts;
import com.us.zoupons.shopper.wallet.ManageWallets;
import com.us.zoupons.storeowner.coupons.StoreOwnerAdd_EditCoupon;
import com.us.zoupons.storeowner.coupons.StoreOwnerCoupons;
import com.us.zoupons.storeowner.customercenter.StoreEmailComposer;
import com.us.zoupons.storeowner.customercenter.StoreOwner_chatsupport;
import com.us.zoupons.storeowner.employees.StoreOwner_Employees;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.invoice.InvoiceCenter;
import com.us.zoupons.storeowner.locations.StoreOwner_Locations;
import com.us.zoupons.storeowner.photos.StoreOwner_Photos;
import com.us.zoupons.storeowner.reviews.StoreOwnerReviews;
import com.us.zoupons.storeowner.store_info.StoreOwner_Info;
import com.us.zoupons.storeowner.videos.VideosDetails;

/**
 * 
 * Class to manage store right menu 
 *
 */

public class StoreOwner_RightMenu {

	// Initializing views,view groups and variables
	private LinearLayout mStoreRightMenu_Info,mStoreRightMenu_Locations,mStoreRightMenu_Employees,mStoreRightMenu_Coupons,mStoreRightMenu_Reviews,mStoreRightMenu_Communication,mStoreRightMenu_Photos,mStoreRightMenu_Videos,mStoreRightMenu_Billing;
	private TextView mStoreRightMenu_InfoText,mStoreRightMenu_LocationsText,mStoreRightMenu_EmployeesText,mStoreRightMenu_CouponsText,mStoreRightMenu_ReviewsText,mStoreRightMenu_CommunicationText,mStoreRightMenu_PhotosText,mStoreRightMenu_VideosText,mStoreRightMenu_BillingText,
	mStoreCustomerCenterRightMenu_ChatText,mStoreCustomerCenterRightMenu_CouponText,mStoreCustomerCenterRightMenu_InvoiceText,mStoreCustomerCenterRightMenu_TransactionHistoryText;
	private ImageView mStoreRightMenu_InfoImage,mStoreRightMenu_LocationsImage,mStoreRightMenu_EmployeesImage,mStoreRightMenu_CouponsImage,mStoreRightMenu_ReviewsImage,mStoreRightMenu_CommunicationImage,mStoreRightMenu_PhotosImage,mStoreRightMenu_VideosImage,mStoreRightMenu_BillingImage,
	mStoreCustomerCenterRightMenu_CouponImage,mStoreCustomerCenterRightMenu_InvoiceImage,mStoreCustomerCenterRightMenu_TransactionHistoryImage;
	private Activity mContext;
	private HorizontalScrollView mScrollView;
	private View mLeftMenu;
	private int mMenuFlag;
	private Button mFreezeView;
	private String mTag;
	private LinearLayout mStoreCustomerCenterRightMenu_Chat,mStoreCustomerCenterRightMenu_Coupon,mStoreCustomerCenterRightMenu_Invoice,mStoreCustomerCenterRightMenu_TransactionHistory;
	public static TextView mStoreRightMenu_EmailText;
	public static ImageView mStoreRightMenu_EmailImage;
	public static LinearLayout mStoreCustomerCenterRightMenu_Mail;
	public static String mCustomer_id="",mCustomer_FirstName="",mCustomer_LastName="",mCustomer_ProfileImage="",IsFavouriteStoreRemoved="";
	private TextView mStoreName,mStoreLocationName;
	private Typeface mZouponsFont;

	public StoreOwner_RightMenu(Activity context,HorizontalScrollView scrollView, View leftmenu, View rightmenu,int menuflag,Button freezeview,String tag) {
		this.mContext = context;
		this.mScrollView=scrollView;
		this.mLeftMenu=leftmenu;
		this.mMenuFlag=menuflag;
		this.mFreezeView=freezeview;
		this.mTag=tag;
	}

	public View intializeInflater(){
		return rightMenuInflater();
	}

	public View intializeCustomerCenterInflater(){
		return rightCustomerCenterInflater();
	}

	// Initializing right menu items
	private View rightMenuInflater(){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mRightMenuItems = inflater.inflate(R.layout.rightmenu_store, null);
		try{
			mStoreName = (TextView) mRightMenuItems.findViewById(R.id.store_rightmenu_storename);
			mStoreLocationName = (TextView) mRightMenuItems.findViewById(R.id.store_rightmenu_storelocation);
			mStoreRightMenu_Info = (LinearLayout) mRightMenuItems.findViewById(R.id.store_rightmenu_info);
			mStoreRightMenu_Locations = (LinearLayout) mRightMenuItems.findViewById(R.id.store_rightmenu_locations);
			mStoreRightMenu_Coupons = (LinearLayout) mRightMenuItems.findViewById(R.id.store_rightmenu_coupons);
			mStoreRightMenu_Reviews = (LinearLayout) mRightMenuItems.findViewById(R.id.store_rightmenu_reviews);
			mStoreRightMenu_Communication = (LinearLayout) mRightMenuItems.findViewById(R.id.store_rightmenu_communication);
			mStoreRightMenu_Employees = (LinearLayout) mRightMenuItems.findViewById(R.id.store_rightmenu_Employees);
			mStoreRightMenu_Photos = (LinearLayout) mRightMenuItems.findViewById(R.id.store_rightmenu_photos);
			mStoreRightMenu_Videos = (LinearLayout) mRightMenuItems.findViewById(R.id.store_rightmenu_videos);
			mStoreRightMenu_Billing = (LinearLayout) mRightMenuItems.findViewById(R.id.store_rightmenu_Billing);
			mStoreRightMenu_InfoText = (TextView) mRightMenuItems.findViewById(R.id.store_rightmenu_infotext);
			mStoreRightMenu_LocationsText = (TextView) mRightMenuItems.findViewById(R.id.store_rightmenu_locationtext);
			mStoreRightMenu_CouponsText = (TextView) mRightMenuItems.findViewById(R.id.store_rightmenu_couponstext);
			mStoreRightMenu_CommunicationText = (TextView) mRightMenuItems.findViewById(R.id.store_rightmenu_communicationstext);
			mStoreRightMenu_ReviewsText = (TextView) mRightMenuItems.findViewById(R.id.store_rightmenu_reviewstext);
			mStoreRightMenu_EmployeesText = (TextView) mRightMenuItems.findViewById(R.id.store_rightmenu_Employeestext);
			mStoreRightMenu_PhotosText = (TextView) mRightMenuItems.findViewById(R.id.store_rightmenu_photostext);
			mStoreRightMenu_VideosText = (TextView) mRightMenuItems.findViewById(R.id.store_rightmenu_videostext);
			mStoreRightMenu_BillingText = (TextView) mRightMenuItems.findViewById(R.id.store_rightmenu_Billingtext);
			mStoreRightMenu_InfoImage = (ImageView) mRightMenuItems.findViewById(R.id.store_rightmenu_infoimage);
			mStoreRightMenu_LocationsImage = (ImageView) mRightMenuItems.findViewById(R.id.store_rightmenu_locationimage);
			mStoreRightMenu_CouponsImage = (ImageView) mRightMenuItems.findViewById(R.id.store_rightmenu_couponsimage);
			mStoreRightMenu_CommunicationImage = (ImageView) mRightMenuItems.findViewById(R.id.store_rightmenu_communicationimage);
			mStoreRightMenu_ReviewsImage = (ImageView) mRightMenuItems.findViewById(R.id.store_rightmenu_reviewsimage);
			mStoreRightMenu_EmployeesImage = (ImageView) mRightMenuItems.findViewById(R.id.store_rightmenu_Employeesimage);
			mStoreRightMenu_PhotosImage = (ImageView) mRightMenuItems.findViewById(R.id.store_rightmenu_photosimage);
			mStoreRightMenu_VideosImage = (ImageView) mRightMenuItems.findViewById(R.id.store_rightmenu_videoimage);
			mStoreRightMenu_BillingImage = (ImageView) mRightMenuItems.findViewById(R.id.store_rightmenu_Billingimage);
			mZouponsFont=Typeface.createFromAsset(mContext.getAssets(), "helvetica.ttf");
			mStoreRightMenu_InfoText.setTypeface(mZouponsFont);
			mStoreRightMenu_LocationsText.setTypeface(mZouponsFont);
			mStoreRightMenu_CouponsText.setTypeface(mZouponsFont);
			mStoreRightMenu_CommunicationText.setTypeface(mZouponsFont);
			mStoreRightMenu_ReviewsText.setTypeface(mZouponsFont);
			mStoreRightMenu_EmployeesText.setTypeface(mZouponsFont);
			mStoreRightMenu_PhotosText.setTypeface(mZouponsFont);
			mStoreRightMenu_VideosText.setTypeface(mZouponsFont);
			mStoreRightMenu_BillingText.setTypeface(mZouponsFont);
			// to get user type from preference
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			mStoreName.setText(mPrefs.getString("store_name", ""));
			mStoreLocationName.setText(mPrefs.getString("store_location_address", ""));
			// Enabling and disabling based upon store type and employee permission 
			enable_disableMenu(mContext, mStoreRightMenu_Info, mStoreRightMenu_InfoText,mStoreRightMenu_InfoImage,mPrefs.getString("information_access", ""));
			enable_disableMenu(mContext, mStoreRightMenu_Locations, mStoreRightMenu_LocationsText,mStoreRightMenu_LocationsImage,mPrefs.getString("locations_access", ""));
			enable_disableMenu(mContext, mStoreRightMenu_Coupons, mStoreRightMenu_CouponsText,mStoreRightMenu_CouponsImage,mPrefs.getString("coupons_access", ""));
			enable_disableMenu(mContext, mStoreRightMenu_Reviews, mStoreRightMenu_ReviewsText,mStoreRightMenu_ReviewsImage,mPrefs.getString("reviews_access", ""));
			enable_disableMenu(mContext, mStoreRightMenu_Communication, mStoreRightMenu_CommunicationText,mStoreRightMenu_CommunicationImage,mPrefs.getString("communication_access", ""));
			enable_disableMenu(mContext, mStoreRightMenu_Employees, mStoreRightMenu_EmployeesText,mStoreRightMenu_EmployeesImage,mPrefs.getString("employees_access", ""));
			enable_disableMenu(mContext, mStoreRightMenu_Photos,mStoreRightMenu_PhotosText,mStoreRightMenu_PhotosImage,mPrefs.getString("photos_access", ""));
			enable_disableMenu(mContext, mStoreRightMenu_Videos, mStoreRightMenu_VideosText,mStoreRightMenu_VideosImage,mPrefs.getString("videos_access", ""));
			enable_disableMenu(mContext, mStoreRightMenu_Billing, mStoreRightMenu_BillingText,mStoreRightMenu_BillingImage,mPrefs.getString("billing_access", ""));
		}catch(Exception e){
			e.printStackTrace();
		}
		return mRightMenuItems;
	}

	/* Customercenter right menu initialization */
	private View rightCustomerCenterInflater(){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mRightMenuItems = inflater.inflate(R.layout.storeowner_customercenter_rightmenu, null);
		mStoreCustomerCenterRightMenu_Chat = (LinearLayout) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_initiatechat);
		mStoreCustomerCenterRightMenu_Mail = (LinearLayout) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_sendEmail);
		mStoreRightMenu_EmailText = (TextView) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_sendEmailText);
		mStoreCustomerCenterRightMenu_Coupon = (LinearLayout) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_SendCoupon);
		mStoreCustomerCenterRightMenu_Invoice = (LinearLayout) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_SendInvoice);
		mStoreCustomerCenterRightMenu_TransactionHistory= (LinearLayout) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_TransactionHistory);
		mStoreCustomerCenterRightMenu_ChatText = (TextView) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_initiatechatText);
		mStoreRightMenu_EmailText = (TextView) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_sendEmailText);
		mStoreCustomerCenterRightMenu_CouponText = (TextView) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_SendCouponText);
		mStoreCustomerCenterRightMenu_InvoiceText = (TextView) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_SendInvoiceText);
		mStoreCustomerCenterRightMenu_TransactionHistoryText = (TextView) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_TransactionHistoryText);
		mStoreRightMenu_EmailImage = (ImageView) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_sendEmailImage);
		mStoreCustomerCenterRightMenu_CouponImage = (ImageView) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_SendCouponImage);
		mStoreCustomerCenterRightMenu_InvoiceImage = (ImageView) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_SendInvoiceImage);
		mStoreCustomerCenterRightMenu_TransactionHistoryImage = (ImageView) mRightMenuItems.findViewById(R.id.customercenter_rightmenu_TransactionHistoryImage);

		mZouponsFont=Typeface.createFromAsset(mContext.getAssets(), "helvetica.ttf");
		mStoreCustomerCenterRightMenu_ChatText.setTypeface(mZouponsFont);
		mStoreRightMenu_EmailText.setTypeface(mZouponsFont);
		mStoreCustomerCenterRightMenu_CouponText.setTypeface(mZouponsFont);
		mStoreCustomerCenterRightMenu_InvoiceText.setTypeface(mZouponsFont);
		mStoreCustomerCenterRightMenu_TransactionHistoryText.setTypeface(mZouponsFont);
		// to get email enable status from preference
		SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
		String email_enable_status = mPrefs.getString("email_status", "");
		if(email_enable_status.equalsIgnoreCase("no")){ 
			mStoreCustomerCenterRightMenu_Mail.setBackgroundColor(mContext.getResources().getColor(R.color.translucent_white));
			mStoreRightMenu_EmailText.setTextColor(Color.GRAY);
			mStoreRightMenu_EmailImage.setAlpha(100);
			mStoreCustomerCenterRightMenu_Mail.setEnabled(false);
		}else{
			mStoreCustomerCenterRightMenu_Mail.setBackgroundResource(R.drawable.gradient_bg);
			mStoreRightMenu_EmailText.setTextColor(Color.WHITE);
			mStoreRightMenu_EmailImage.setAlpha(255);
			mStoreCustomerCenterRightMenu_Mail.setEnabled(true);
		}
		enable_disableMenu(mContext, mStoreCustomerCenterRightMenu_Coupon, mStoreCustomerCenterRightMenu_CouponText,mStoreCustomerCenterRightMenu_CouponImage,mPrefs.getString("coupons_access", ""));
		enable_disableMenu(mContext, mStoreCustomerCenterRightMenu_Invoice, mStoreCustomerCenterRightMenu_InvoiceText,mStoreCustomerCenterRightMenu_InvoiceImage,mPrefs.getString("invoice_center_access", ""));
		enable_disableMenu(mContext, mStoreCustomerCenterRightMenu_TransactionHistory, mStoreCustomerCenterRightMenu_TransactionHistoryText,mStoreCustomerCenterRightMenu_TransactionHistoryImage,mPrefs.getString("point_of_sale_access", ""));
		return mRightMenuItems;
	}

	public void clickListener(View leftmenu,View rightmenu){

		this.mLeftMenu=leftmenu;

		mStoreRightMenu_Info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				stopChatTimerIfRunning(); // To stop communication timer if running
				if(mTag.equalsIgnoreCase("StoreOwner_Info")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {  // Launch store informatin activity if we click INFO from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_rightmenuinfo = new Intent().setClass(mContext.getApplicationContext(),StoreOwner_Info.class);
					intent_rightmenuinfo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_rightmenuinfo);
					mContext.finish();
				}
				
			}
		});

		mStoreRightMenu_Locations.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopChatTimerIfRunning(); // To stop communication timer if running
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_Locations")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch locations activity if we click Locations from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_locations = new Intent().setClass(mContext.getApplicationContext(),StoreOwner_Locations.class);
					intent_locations.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_locations);
					mContext.finish();
				}
				
			}
		});


		mStoreRightMenu_Coupons.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopChatTimerIfRunning(); // To stop communication timer if running
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerCoupons")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch Coupons activity if we click COUPONS from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_rightmenucoupons = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerCoupons.class);
					intent_rightmenucoupons.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_rightmenucoupons);
					mContext.finish();
				}
			}
		});

		mStoreRightMenu_Reviews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopChatTimerIfRunning(); // To stop communication timer if running
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerReviews")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch StoreReviews activity if we click REVIEWS from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_rightmenureviews = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerReviews.class);
					intent_rightmenureviews.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_rightmenureviews);
					mContext.finish();
				}
				
			}
		});

		mStoreRightMenu_Communication.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_chatsupport") || mTag.equalsIgnoreCase("StoreOwner_TalkToUs")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu/*, mRightMenu*/, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch zoupons support activity if we click ZOUPONS SUPPORT from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_talktous = new Intent(mContext,StoreOwner_chatsupport.class);
					intent_talktous.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					intent_talktous.putExtra("source", "talk_to_us");
					intent_talktous.putExtra("class_name", "talk_to_us");
					mContext.startActivity(intent_talktous);
					stopChatTimerIfRunning(); // To stop communication timer if running
					mContext.finish();
				}
				
			}
		});

		mStoreRightMenu_Employees.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				stopChatTimerIfRunning(); // To stop communication timer if running
				if(mTag.equalsIgnoreCase("StoreOwner_Employees")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch employee activity if we click EMPLOYEE from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_employees = new Intent().setClass(mContext.getApplicationContext(),StoreOwner_Employees.class);
					intent_employees.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_employees);
					mContext.finish();
				}
				
			}
		});

		mStoreRightMenu_Photos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				stopChatTimerIfRunning(); // To stop communication timer if running
				if(mTag.equalsIgnoreCase("StoreOwner_Photos")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch store photos activity if we click PHOTOS from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_photos = new Intent(mContext,StoreOwner_Photos.class);
					intent_photos.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_photos);
					mContext.finish();
				}
				
			}
		});

		mStoreRightMenu_Videos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				stopChatTimerIfRunning(); // To stop communication timer if running
				if(mTag.equalsIgnoreCase("StoreOwnerVideos")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/* mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch store video activity if we click VIDEOS from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_videos = new Intent(mContext,VideosDetails.class);
					intent_videos.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent_videos);
					mContext.finish();
				}
				
			}
		});

		mStoreRightMenu_Billing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				stopChatTimerIfRunning(); // To stop communication timer if running
				if(mTag.equalsIgnoreCase("ManageWallets")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else { // Launch Billings activity if we click BILLINGS from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_rightmenuBilling = new Intent(mContext,ManageWallets.class);
					intent_rightmenuBilling.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					intent_rightmenuBilling.putExtra("FromStoreOwnerBilling", true);
					mContext.startActivity(intent_rightmenuBilling);
					mContext.finish();
				}
				
			}
		});

	}

	public void customercentermenuClickListener(View leftmenu,View rightmenu){

		this.mLeftMenu=leftmenu;

		mStoreCustomerCenterRightMenu_Chat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_chatsupport")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/* mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else{ // Launch Contact customer activity if we click INITIATE CHAT from other activitys
					if(StoreOwner_chatsupport.sCommunicationTimer!=null){ // To cancel chat timer
						StoreOwner_chatsupport.sCommunicationTimer.cancel();
						StoreOwner_chatsupport.sCommunicationTimer=null;
					}if(StoreOwner_chatsupport.sCommunicationTimerTask!=null){
						StoreOwner_chatsupport.sCommunicationTimerTask.cancel();
						StoreOwner_chatsupport.sCommunicationTimerTask=null;
					}
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_talktous = new Intent(mContext,StoreOwner_chatsupport.class);
					intent_talktous.putExtra("class_name","Contact_store");
					intent_talktous.putExtra("source", "customercenter");
					intent_talktous.putExtra("customer_id",StoreOwner_RightMenu.mCustomer_id);
					mContext.startActivity(intent_talktous);
					mContext.finish();
				}
				
			}
		});

		mStoreCustomerCenterRightMenu_Mail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_EmailComposer")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/* mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else{ // Launch Email composer activity if we click SEND EMAIL from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT ;
					Intent intent_sendEmail = new Intent(mContext,StoreEmailComposer.class);
					intent_sendEmail.putExtra("classname", "StoreEmailComposer");
					mContext.startActivity(intent_sendEmail);
					mContext.finish();
				}
				stopChatTimerIfRunning(); // To stop communication timer if running
			}
		});

		mStoreCustomerCenterRightMenu_Coupon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerAddCoupon")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/* mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else{ // Launch send coupon activity if we click SEND COUPON from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_sendCoupon = new Intent(mContext,StoreOwnerAdd_EditCoupon.class);
					intent_sendCoupon.putExtra("FromCustomerCenter", true);
					intent_sendCoupon.putExtra("customer_id",StoreOwner_RightMenu.mCustomer_id);
					mContext.startActivity(intent_sendCoupon);
					mContext.finish();
				}
				stopChatTimerIfRunning(); // To stop communication timer if running
			}
		});

		mStoreCustomerCenterRightMenu_Invoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerInvoiceCenter")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/* mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else{  // Launch Invoice center activity if we click SEND INVOICE from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_invoicecenter = new Intent(mContext,InvoiceCenter.class);
					intent_invoicecenter.putExtra("FromCustomerCenter", true);
					mContext.startActivity(intent_invoicecenter);
					mContext.finish();
				}
				stopChatTimerIfRunning(); // To stop communication timer if running
			}
		});

		mStoreCustomerCenterRightMenu_TransactionHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mTag.equalsIgnoreCase("Receipts")){ // To close sliding menu
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/* mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else{  // Launch transaction history activity if we click TRANSACTION HISTORY from other activitys
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					v.setBackgroundResource(R.drawable.gradient_bg_hover);
					Intent intent_receipts = new Intent(mContext,Receipts.class);
					intent_receipts.putExtra("FromStoreOwner_TransactionHistory", true);
					mContext.startActivity(intent_receipts);
					mContext.finish();
				}
				stopChatTimerIfRunning(); // To stop communication timer if running
			}
		});
	}

	// To enable or disable sliding menu items based upon store type and employee permissions 
	public void enable_disableMenu(Context context,LinearLayout menu,TextView menutext,ImageView menuimage, String result){
		if(result.equalsIgnoreCase("no")){
			menu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			menutext.setTextColor(Color.GRAY);
			menuimage.setAlpha(100);
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
