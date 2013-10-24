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

import com.us.zoupons.ManageCards;
import com.us.zoupons.R;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.receipts.Receipts;
import com.us.zoupons.storeowner.Coupons.StoreOwnerAdd_EditCoupon;
import com.us.zoupons.storeowner.Coupons.StoreOwnerCoupons;
import com.us.zoupons.storeowner.Employees.StoreOwner_Employees;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.Info.StoreOwner_Info;
import com.us.zoupons.storeowner.Locations.StoreOwner_Locations;
import com.us.zoupons.storeowner.Photos.StoreOwner_Photos;
import com.us.zoupons.storeowner.Reviews.StoreOwnerReviews;
import com.us.zoupons.storeowner.communication.StoreOwner_Communication;
import com.us.zoupons.storeowner.communication.StoreOwner_ContactStore;
import com.us.zoupons.storeowner.customercenter.StoreEmailComposer;
import com.us.zoupons.storeowner.invoice.InvoiceCenter;
import com.us.zoupons.storeowner.videos.VideosDetails;

public class StoreOwner_RightMenu {

	public LinearLayout mStoreRightMenu_Info,mStoreRightMenu_Locations,mStoreRightMenu_Employees,mStoreRightMenu_Coupons,mStoreRightMenu_Customercenter,mStoreRightMenu_Reviews,mStoreRightMenu_Communication,mStoreRightMenu_Photos,mStoreRightMenu_Videos,mStoreRightMenu_Billing;
	public TextView mStoreRightMenu_InfoText,mStoreRightMenu_LocationsText,mStoreRightMenu_EmployeesText,mStoreRightMenu_CouponsText,mStoreRightMenu_CustomercenterText,mStoreRightMenu_ReviewsText,mStoreRightMenu_CommunicationText,mStoreRightMenu_PhotosText,mStoreRightMenu_VideosText,mStoreRightMenu_BillingText,mStoreRightMenu_StoreName,
	mStoreCustomerCenterRightMenu_ChatText,mStoreCustomerCenterRightMenu_CouponText,mStoreCustomerCenterRightMenu_InvoiceText,mStoreCustomerCenterRightMenu_TransactionHistoryText;
	public ImageView mStoreRightMenu_InfoImage,mStoreRightMenu_LocationsImage,mStoreRightMenu_EmployeesImage,mStoreRightMenu_CouponsImage,mStoreRightMenu_CustomercenterImage,mStoreRightMenu_ReviewsImage,mStoreRightMenu_CommunicationImage,mStoreRightMenu_PhotosImage,mStoreRightMenu_VideosImage,mStoreRightMenu_BillingImage,
	mStoreCustomerCenterRightMenu_ChatImage,mStoreCustomerCenterRightMenu_CouponImage,mStoreCustomerCenterRightMenu_InvoiceImage,mStoreCustomerCenterRightMenu_TransactionHistoryImage;
	Activity mContext;
	HorizontalScrollView mScrollView;
	View mLeftMenu,mRightMenu,mMainView;
	int mMenuFlag;
	Button mFreezeView;
	String mTag;
	public LinearLayout mStoreCustomerCenterRightMenu_Chat,mStoreCustomerCenterRightMenu_Coupon,mStoreCustomerCenterRightMenu_Invoice,mStoreCustomerCenterRightMenu_TransactionHistory;
	public LinearLayout mStoreRightMenuContainer,mStoreCustomerCenterRightMenuContainer;
	public static TextView mStoreRightMenu_EmailText;
	public static ImageView mStoreRightMenu_EmailImage;
	public static LinearLayout mStoreCustomerCenterRightMenu_Mail;
	public static String mCustomer_id="",mCustomer_FirstName="",mCustomer_LastName="",mCustomer_ProfileImage="",IsFavouriteStoreRemoved="";
	private TextView mStoreName,mStoreLocationName;
	Typeface mZouponsFont;

	public StoreOwner_RightMenu(Activity context,HorizontalScrollView scrollView, View leftmenu, View rightmenu,int menuflag,Button freezeview,String tag) {
		this.mContext = context;
		this.mScrollView=scrollView;
		this.mLeftMenu=leftmenu;
		this.mRightMenu=rightmenu;
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
		return rightMenuInflater();
	}

	public View intializeCustomerCenterInflater(){
		return rightCustomerCenterInflater();
	}

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
			mStoreRightMenuContainer = (LinearLayout) mRightMenuItems.findViewById(R.id.store_rightmenu_container);
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
			String user_type = mPrefs.getString("user_type", "");
			mStoreName.setText(mPrefs.getString("store_name", ""));
			mStoreLocationName.setText(mPrefs.getString("store_location_address", ""));
			if(user_type.equalsIgnoreCase("store_employee")){
				enable_disableMenu(mContext, mStoreRightMenu_Info, mStoreRightMenu_InfoText,mStoreRightMenu_InfoImage,mPrefs.getString("information_access", ""));
				enable_disableMenu(mContext, mStoreRightMenu_Locations, mStoreRightMenu_LocationsText,mStoreRightMenu_LocationsImage,mPrefs.getString("locations_access", ""));
				enable_disableMenu(mContext, mStoreRightMenu_Coupons, mStoreRightMenu_CouponsText,mStoreRightMenu_CouponsImage,mPrefs.getString("coupons_access", ""));
				enable_disableMenu(mContext, mStoreRightMenu_Reviews, mStoreRightMenu_ReviewsText,mStoreRightMenu_ReviewsImage,mPrefs.getString("reviews_access", ""));
				enable_disableMenu(mContext, mStoreRightMenu_Communication, mStoreRightMenu_CommunicationText,mStoreRightMenu_CommunicationImage,mPrefs.getString("communication_access", ""));
				enable_disableMenu(mContext, mStoreRightMenu_Employees, mStoreRightMenu_EmployeesText,mStoreRightMenu_EmployeesImage,mPrefs.getString("employees_access", ""));
				enable_disableMenu(mContext, mStoreRightMenu_Photos,mStoreRightMenu_PhotosText,mStoreRightMenu_PhotosImage,mPrefs.getString("photos_access", ""));
				enable_disableMenu(mContext, mStoreRightMenu_Videos, mStoreRightMenu_VideosText,mStoreRightMenu_VideosImage,mPrefs.getString("videos_access", ""));
				enable_disableMenu(mContext, mStoreRightMenu_Billing, mStoreRightMenu_BillingText,mStoreRightMenu_BillingImage,mPrefs.getString("billing_access", ""));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mRightMenuItems;
	}

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
		String user_type = mPrefs.getString("user_type", "");
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

		if(user_type.equalsIgnoreCase("store_employee")){
			enable_disableMenu(mContext, mStoreCustomerCenterRightMenu_Coupon, mStoreCustomerCenterRightMenu_CouponText,mStoreCustomerCenterRightMenu_CouponImage,mPrefs.getString("coupons_access", ""));
			enable_disableMenu(mContext, mStoreCustomerCenterRightMenu_Invoice, mStoreCustomerCenterRightMenu_InvoiceText,mStoreCustomerCenterRightMenu_InvoiceImage,mPrefs.getString("invoice_center_access", ""));
			enable_disableMenu(mContext, mStoreCustomerCenterRightMenu_TransactionHistory, mStoreCustomerCenterRightMenu_TransactionHistoryText,mStoreCustomerCenterRightMenu_TransactionHistoryImage,mPrefs.getString("point_of_sale_access", ""));
		}
		return mRightMenuItems;
	}

	public void clickListener(View leftmenu,View rightmenu){

		this.mLeftMenu=leftmenu;
		this.mRightMenu=rightmenu;

		mStoreRightMenu_Info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_Info")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_rightmenuinfo = new Intent().setClass(mContext.getApplicationContext(),StoreOwner_Info.class);
					mContext.startActivity(intent_rightmenuinfo);
				}
			}
		});

		mStoreRightMenu_Locations.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_Locations")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_locations = new Intent().setClass(mContext.getApplicationContext(),StoreOwner_Locations.class);
					mContext.startActivity(intent_locations);
				}
			}
		});


		mStoreRightMenu_Coupons.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerCoupons")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_rightmenucoupons = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerCoupons.class);
					mContext.startActivity(intent_rightmenucoupons);
				}
			}
		});

		mStoreRightMenu_Reviews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerReviews")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_rightmenureviews = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerReviews.class);
					mContext.startActivity(intent_rightmenureviews);
				}
			}
		});

		mStoreRightMenu_Communication.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_Communication")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu/*, mRightMenu*/, mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_talktous = new Intent(mContext,StoreOwner_ContactStore.class);
					intent_talktous.putExtra("source", "talk_to_us");
					intent_talktous.putExtra("class_name", "talk_to_us");
					mContext.startActivity(intent_talktous);
				}
			}
		});

		mStoreRightMenu_Employees.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_Employees")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_employees = new Intent().setClass(mContext.getApplicationContext(),StoreOwner_Employees.class);
					mContext.startActivity(intent_employees);
				}
			}
		});

		mStoreRightMenu_Photos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_Photos")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_photos = new Intent(mContext,StoreOwner_Photos.class);
					mContext.startActivity(intent_photos);
				}
			}
		});

		mStoreRightMenu_Videos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerVideos")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/* mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_videos = new Intent(mContext,VideosDetails.class);
					mContext.startActivity(intent_videos);
				}
			}
		});

		mStoreRightMenu_Billing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_Billing")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else {
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_rightmenuBilling = new Intent(mContext,ManageCards.class);
					intent_rightmenuBilling.putExtra("FromStoreOwnerBilling", true);
					mContext.startActivity(intent_rightmenuBilling);
				}
			}
		});

	}

	public void customercentermenuClickListener(View leftmenu,View rightmenu){

		this.mLeftMenu=leftmenu;
		this.mRightMenu=rightmenu;

		mStoreCustomerCenterRightMenu_Chat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_ContactStore")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/* mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else{
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_talktous = new Intent(mContext,StoreOwner_ContactStore.class);
					intent_talktous.putExtra("class_name","Contact_store");
					intent_talktous.putExtra("source", "customercenter");
					intent_talktous.putExtra("customer_id",StoreOwner_RightMenu.mCustomer_id);
					mContext.startActivity(intent_talktous);
				}
			}
		});

		mStoreCustomerCenterRightMenu_Mail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwner_EmailComposer")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/* mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else{
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_sendEmail = new Intent(mContext,StoreEmailComposer.class);
					intent_sendEmail.putExtra("classname", "StoreEmailComposer");
					mContext.startActivity(intent_sendEmail);
				}
			}
		});

		mStoreCustomerCenterRightMenu_Coupon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerAddCoupon")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/* mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else{
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_sendCoupon = new Intent(mContext,StoreOwnerAdd_EditCoupon.class);
					intent_sendCoupon.putExtra("FromCustomerCenter", true);
					intent_sendCoupon.putExtra("customer_id",StoreOwner_RightMenu.mCustomer_id);
					mContext.startActivity(intent_sendCoupon);
				}
			}
		});

		mStoreCustomerCenterRightMenu_Invoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(mTag.equalsIgnoreCase("StoreOwnerInvoiceCenter")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/* mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else{
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					Intent intent_invoicecenter = new Intent(mContext,InvoiceCenter.class);
					intent_invoicecenter.putExtra("FromCustomerCenter", true);
					mContext.startActivity(intent_invoicecenter);
				}
			}
		});

		mStoreCustomerCenterRightMenu_TransactionHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mTag.equalsIgnoreCase("Receipts")){
					new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu,/* mRightMenu,*/ mMenuFlag, mFreezeView, mTag).toCloseMenu();
				}else{
					MenuOutClass.STOREOWNER_MENUOUT=!MenuOutClass.STOREOWNER_MENUOUT;
					v.setBackgroundResource(R.drawable.gradient_bg_hover);
					Intent intent_receipts = new Intent(mContext,Receipts.class);
					intent_receipts.putExtra("FromStoreOwner_TransactionHistory", true);
					mContext.startActivity(intent_receipts);
				}
			}
		});
	}

	public void enable_disableMenu(Context context,LinearLayout menu,TextView menutext,ImageView menuimage, String result){

		if(result.equalsIgnoreCase("no")){
			menu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			menutext.setTextColor(Color.GRAY);
			menuimage.setAlpha(100);
			menu.setEnabled(false);
		}
	}
}
