package com.us.zoupons.shopper.coupons;

import java.util.EnumMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NotificationTabImageVisibility;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;

/**
 * 
 * Activity to display coupon detail 
 *
 */
public class CouponDetail extends Activity {

	// Initializing views and variables
	private MyHorizontalScrollView mScrollView;
	private View mLeftmenu,mRightmenu,mApp;
	private ImageView mBtnSlide;
	private Button mCouponsDetailsFreezeButton;
	private Typeface mZouponsFont;
	private LinearLayout mCouponsDetailsHome,mCouponsDetailsZpay,mCouponsDetailsInvoiceCenter,mCouponsDetailsGiftcards,mCouponsDetailsManageCards,mCouponsDetailsReceipts,mCouponsDetailsMyFavourites,mCouponsDetailsMyFriends,mCouponsDetailsChat,mCouponsDetailsRewards,mCouponsDetailsSettings,
	mCouponsDetailsLogout;
	private TextView mCouponsDetailsHomeText,mCouponsDetailsZpayText,mCouponsDetailsInvoiceCenterText,mCouponsDetailsGiftCardsText,mCouponsDetailsManageCardsText,mCouponsDetailsReceiptsText,mCouponsDetailsMyFavoritesText,mCouponsDetailsMyFriendsText,mCouponsDetailsChatText,mCouponsDetailsRewardsText,
	mCouponsDetailsSettingsText,mCouponsDetailsLogoutText;
	private ImageView mCouponsDetailsZpayImage,mCouponsDetailsInvoiceCenterImage,mCouponsDetailsGiftCardsImage,mCouponsDetailsManageCardsImage,mCouponsDetailsReceiptsImage,mCouponsDetailsMyFavoritesImage,mCouponsDetailsMyFriendsImage,mCouponsDetailsChatImage,mCouponsDetailsRewardsImage,
	mCouponsDetailsSettingsImage;
	private RelativeLayout btnLogout; 
	private ImageView mNotificationImage,mCalloutTriangleImage;
	private Button mNotificationCount;
	private int mScreenWidth;
	private String TAG = "CouponDetail";
	private ProgressDialog mProgressDialog= null;
	private EditText mCouponDescription;
	private TextView mCouponDetailStoreName,mCouponDetailName,mCouponDetailCode,mCouponDetailExpires,mNoBarCodeImage,mMenuBarFavCouponsText;
	private ImageView mMenuBarFavCouponsImage;
	private Button mCouponDetailPrev,mCouponDetailNext;
	private ImageView mCouponBarCode;
	private LinearLayout mCouponMenubarShare,mCouponMenubarFavorite,mCouponMenubarClose;
	public static int mCouponPosition;
	public static String mCouponId;
	private String mActivity="";
	private double mSettingsMenuWidth;
	private ScheduleNotificationSync mNotificationSync;
	public static String mIsCouponAddedAsFavotire,mIsFavoriteCoupons;
	private ScrollView leftMenuScrollView;
	//Logout without user interaction after 5 minute
	private CheckLogoutSession mLogoutSession;
	private ImageView mTabBarLoginChoice;
	private ProgressBar mProgressBar;
	private LoadCouponQRcodeTask mLoadCoupon;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Referencing view from layout XML file
		LayoutInflater inflater = LayoutInflater.from(this);
		mScrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(mScrollView);
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mProgressDialog=new ProgressDialog(this);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
		mLeftmenu = inflater.inflate(R.layout.shopper_leftmenu, null);
		mRightmenu = inflater.inflate(R.layout.rightmenu, null);
		mApp = inflater.inflate(R.layout.coupon_detail, null);
		mCouponPosition = Integer.parseInt(getIntent().getStringExtra("CouponPosition"));
		//Check the Is from Favorite
		if(getIntent().getStringExtra("Activity").equalsIgnoreCase("Favorite")){
			mActivity = "Favorite";
		}
		ViewGroup tabBar = (ViewGroup) mApp.findViewById(R.id.coupon_detail_tabbar);
		ViewGroup couponsmenubarcontainer = (ViewGroup) mApp.findViewById(R.id.coupondetail_menubarcontainer);
		ViewGroup menuLeftItems = (ViewGroup) mLeftmenu.findViewById(R.id.menuitems);
		leftMenuScrollView = (ScrollView) mLeftmenu.findViewById(R.id.leftmenu_scrollview);
		leftMenuScrollView.fullScroll(View.FOCUS_UP);
		leftMenuScrollView.pageScroll(View.FOCUS_UP);
		mCouponsDetailsHome = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_home);
		mCouponsDetailsZpay = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_zpay);
		mCouponsDetailsInvoiceCenter = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_invoicecenter);
		mCouponsDetailsGiftcards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_zgiftcards);
		mCouponsDetailsManageCards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_managecards);
		mCouponsDetailsReceipts = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_receipts);
		mCouponsDetailsMyFavourites = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_myfavourites);
		mCouponsDetailsMyFriends = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_myfriends);
		mCouponsDetailsChat = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_customercenter);
		mCouponsDetailsRewards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_rewards);
		mCouponsDetailsSettings = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_settings);
		mCouponsDetailsLogout = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_logout);
		mCouponsDetailsZpayImage = (ImageView) menuLeftItems.findViewById(R.id.menuZpayimage);
		mCouponsDetailsInvoiceCenterImage = (ImageView) menuLeftItems.findViewById(R.id.menuInvoiceCenterimage);
		mCouponsDetailsGiftCardsImage = (ImageView) menuLeftItems.findViewById(R.id.menuGiftcardsimage);
		mCouponsDetailsManageCardsImage = (ImageView) menuLeftItems.findViewById(R.id.menuManageCardsimage);
		mCouponsDetailsReceiptsImage = (ImageView) menuLeftItems.findViewById(R.id.menuReceiptsimage);
		mCouponsDetailsMyFavoritesImage = (ImageView) menuLeftItems.findViewById(R.id.menuFavoriteimage);
		mCouponsDetailsMyFriendsImage = (ImageView) menuLeftItems.findViewById(R.id.menuFriendsimage);
		mCouponsDetailsChatImage = (ImageView) menuLeftItems.findViewById(R.id.menuCustomerCenterimage);
		mCouponsDetailsRewardsImage = (ImageView) menuLeftItems.findViewById(R.id.menuRewardsimage);
		mCouponsDetailsSettingsImage = (ImageView) menuLeftItems.findViewById(R.id.menuSettingsimage);
		mCouponsDetailsHomeText = (TextView) menuLeftItems.findViewById(R.id.menuHome);
		mCouponsDetailsHomeText.setTypeface(mZouponsFont);
		mCouponsDetailsInvoiceCenterText = (TextView) menuLeftItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
		mCouponsDetailsInvoiceCenterText.setTypeface(mZouponsFont);
		mCouponsDetailsZpayText = (TextView) menuLeftItems.findViewById(R.id.menuZpay);
		mCouponsDetailsZpayText.setTypeface(mZouponsFont);
		mCouponsDetailsGiftCardsText = (TextView) menuLeftItems.findViewById(R.id.menuGiftCards);
		mCouponsDetailsGiftCardsText.setTypeface(mZouponsFont);
		mCouponsDetailsManageCardsText = (TextView) menuLeftItems.findViewById(R.id.menuManageCards);
		mCouponsDetailsManageCardsText.setTypeface(mZouponsFont);
		mCouponsDetailsReceiptsText = (TextView) menuLeftItems.findViewById(R.id.menuReceipts);
		mCouponsDetailsReceiptsText.setTypeface(mZouponsFont);
		mCouponsDetailsMyFavoritesText = (TextView) menuLeftItems.findViewById(R.id.menufavorites);
		mCouponsDetailsMyFavoritesText.setTypeface(mZouponsFont);
		mCouponsDetailsMyFriendsText = (TextView) menuLeftItems.findViewById(R.id.menuMyFriends);
		mCouponsDetailsMyFriendsText.setTypeface(mZouponsFont);
		mCouponsDetailsChatText = (TextView) menuLeftItems.findViewById(R.id.menuCustomerCenter);
		mCouponsDetailsChatText.setTypeface(mZouponsFont);
		mCouponsDetailsRewardsText = (TextView) menuLeftItems.findViewById(R.id.menuRewards);
		mCouponsDetailsRewardsText.setTypeface(mZouponsFont);
		mCouponsDetailsSettingsText = (TextView) menuLeftItems.findViewById(R.id.menuSettings);
		mCouponsDetailsSettingsText.setTypeface(mZouponsFont);
		mCouponsDetailsLogoutText = (TextView) menuLeftItems.findViewById(R.id.menuLogout);
		mCouponsDetailsLogoutText.setTypeface(mZouponsFont);
		mCouponsDetailsFreezeButton=(Button) mApp.findViewById(R.id.coupon_detail_freeze);
		mBtnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_coupon_detail);
		mCouponsDetailsFreezeButton.setOnClickListener(new ClickListenerForScrolling(mScrollView, mLeftmenu, mRightmenu, 0, mCouponsDetailsFreezeButton));
		mBtnSlide.setOnClickListener(new ClickListenerForScrolling(mScrollView, mLeftmenu, mRightmenu, 1, mCouponsDetailsFreezeButton));
		View[] children = new View[] { mLeftmenu, mApp};
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		//mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(CouponDetail.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(CouponDetail.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Check whether to show Notification image in zoupons header
		new NotificationTabImageVisibility(CouponDetail.this, mNotificationImage).checkNotificationVisibility();
		// Initialising receiver
		mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
		mNotificationImage.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		mNotificationCount.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		// Scroll to mApp (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		mScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(mBtnSlide));
		mCouponsDetailsHome.setOnClickListener(new MenuItemClickListener(menuLeftItems, CouponDetail.this,POJOMainMenuActivityTAG.TAG=TAG));
		mCouponsDetailsInvoiceCenter.setOnClickListener(new MenuItemClickListener(menuLeftItems, CouponDetail.this,POJOMainMenuActivityTAG.TAG=TAG));
		mCouponsDetailsZpay.setOnClickListener(new MenuItemClickListener(menuLeftItems, CouponDetail.this,POJOMainMenuActivityTAG.TAG=TAG));
		mCouponsDetailsGiftcards.setOnClickListener(new MenuItemClickListener(menuLeftItems, CouponDetail.this,POJOMainMenuActivityTAG.TAG=TAG));
		mCouponsDetailsManageCards.setOnClickListener(new MenuItemClickListener(menuLeftItems, CouponDetail.this,POJOMainMenuActivityTAG.TAG=TAG));
		mCouponsDetailsReceipts.setOnClickListener(new MenuItemClickListener(menuLeftItems, CouponDetail.this,POJOMainMenuActivityTAG.TAG=TAG));
		mCouponsDetailsMyFavourites.setOnClickListener(new MenuItemClickListener(menuLeftItems,CouponDetail.this,POJOMainMenuActivityTAG.TAG=TAG));
		mCouponsDetailsMyFriends.setOnClickListener(new MenuItemClickListener(menuLeftItems,CouponDetail.this,POJOMainMenuActivityTAG.TAG=TAG));
		mCouponsDetailsChat.setOnClickListener(new MenuItemClickListener(menuLeftItems,CouponDetail.this,POJOMainMenuActivityTAG.TAG=TAG));
		mCouponsDetailsRewards.setOnClickListener(new MenuItemClickListener(menuLeftItems,CouponDetail.this,POJOMainMenuActivityTAG.TAG=TAG));
		mCouponsDetailsSettings.setOnClickListener(new MenuItemClickListener(menuLeftItems,CouponDetail.this,POJOMainMenuActivityTAG.TAG=TAG));

		mCouponsDetailsLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(CouponDetail.this,"FromManualLogOut").execute();
			}
		});

		mCouponDetailStoreName = (TextView) mApp.findViewById(R.id.store_title_textId);
		mCouponDetailName = (TextView) mApp.findViewById(R.id.coupons_name);
		mCouponDetailCode = (TextView) mApp.findViewById(R.id.coupon_code_text);
		mCouponDetailExpires = (TextView) mApp.findViewById(R.id.coupon_expires_text);	
		mCouponDescription = (EditText) mApp.findViewById(R.id.coupon_description);
		mCouponBarCode = (ImageView) mApp.findViewById(R.id.coupon_barcode);
		mProgressBar = (ProgressBar) mApp.findViewById(R.id.mProgress);
		mCouponDetailNext = (Button) mApp.findViewById(R.id.next_button);
		mCouponDetailPrev = (Button) mApp.findViewById(R.id.previous_button);
		mNoBarCodeImage = (TextView) mApp.findViewById(R.id.mNoBarCode);
		mCouponDescription.setKeyListener(null);

		//Menu bar Items
		mCouponMenubarShare = (LinearLayout) couponsmenubarcontainer.findViewById(R.id.menubar_share);
		mCouponMenubarFavorite = (LinearLayout) couponsmenubarcontainer.findViewById(R.id.menubar_addtofavorite);
		mCouponMenubarClose = (LinearLayout) couponsmenubarcontainer.findViewById(R.id.menubar_close);

		mMenuBarFavCouponsText = (TextView) couponsmenubarcontainer.findViewById(R.id.menubar_favorite_text);
		mMenuBarFavCouponsImage = (ImageView) couponsmenubarcontainer.findViewById(R.id.menubar_favorite_image);

		//Call function to get width of the screen
		mScreenWidth=getScreenWidth(); 	
		if(mScreenWidth>0){	//To fix Home Page menubar items width
			mSettingsMenuWidth=mScreenWidth/3;
		}

		mCouponMenubarShare.setLayoutParams(new LinearLayout.LayoutParams((int)mSettingsMenuWidth,LayoutParams.FILL_PARENT,1f));
		mCouponMenubarFavorite.setLayoutParams(new LinearLayout.LayoutParams((int)mSettingsMenuWidth,LayoutParams.FILL_PARENT,1f));
		mCouponMenubarClose.setLayoutParams(new LinearLayout.LayoutParams((int)mSettingsMenuWidth,LayoutParams.FILL_PARENT,1f));

		SharedPreferences mUserDetailsPrefs = getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");
		if(user_login_type.equalsIgnoreCase("customer")){  // Customer
			mCouponMenubarFavorite.setVisibility(View.VISIBLE);
		}else{
			mCouponMenubarFavorite.setVisibility(View.INVISIBLE);
		}

		POJOCouponsList mSelectedCouponDetail = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(mCouponPosition);

		if(mActivity.equalsIgnoreCase("Favorite")){
			mCouponDetailStoreName.setText(mSelectedCouponDetail.mFavorite_StoreName);
			mCouponId=mSelectedCouponDetail.mFavorite_CouponId;
			mCouponDetailName.setText(mSelectedCouponDetail.mFavorite_CouponTitle);		
			mCouponDetailCode.setText(mSelectedCouponDetail.mFavorite_CouponCode);		
			mCouponDetailExpires.setText(mSelectedCouponDetail.mFavorite_CouponExpires);		
			mCouponDescription.setText(mSelectedCouponDetail.mFavorite_CouponDescription);
			if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
				mLoadCoupon.cancel(true);	
			mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
			mLoadCoupon.execute(mSelectedCouponDetail.mFavorite_CouponBarCodeImage);
		}else{
			mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);
			mCouponId=mSelectedCouponDetail.mCouponId;
			mCouponDetailName.setText(mSelectedCouponDetail.mCouponTitle);		
			mCouponDetailCode.setText(mSelectedCouponDetail.mCouponCode);		
			mCouponDetailExpires.setText(mSelectedCouponDetail.mCouponExpires);		
			mCouponDescription.setText(mSelectedCouponDetail.mCouponDescription);
			if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
				mLoadCoupon.cancel(true);
			mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
			mLoadCoupon.execute(mSelectedCouponDetail.mCouponBarCodeImage);
		}
		//Based on the coupon enable prev/next button
		if(mCouponPosition == 0){
			mCouponDetailPrev.setEnabled(false);
			mCouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
			mCouponDetailPrev.getBackground().setAlpha(100);
			mCouponDetailNext.getBackground().setAlpha(255);
		}else if(mCouponPosition == WebServiceStaticArrays.mStaticCouponsArrayList.size()-1){
			mCouponDetailNext.setEnabled(false);
			mCouponDetailNext.setBackgroundResource(R.drawable.next_circles);
			mCouponDetailNext.getBackground().setAlpha(100);
			mCouponDetailPrev.getBackground().setAlpha(255);
		}else{
			mCouponDetailPrev.setEnabled(true);
			mCouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
			mCouponDetailPrev.getBackground().setAlpha(255);
			mCouponDetailNext.setEnabled(true);
			mCouponDetailNext.setBackgroundResource(R.drawable.next_circles);
			mCouponDetailNext.getBackground().setAlpha(255);
		}

		//If the Coupons list has only one item the disable the prev/next button
		if(WebServiceStaticArrays.mStaticCouponsArrayList.size()==1){
			mCouponDetailPrev.setEnabled(false);
			mCouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
			mCouponDetailPrev.getBackground().setAlpha(100);
			mCouponDetailNext.setEnabled(false);
			mCouponDetailNext.setBackgroundResource(R.drawable.next_circles);
			mCouponDetailNext.getBackground().setAlpha(100);
		}

		mMenuBarFavCouponsText.setText("Favorite");
		//If it's from Favorite No need to Check Coupon is Favorite or Not
		if(mActivity.equalsIgnoreCase("Favorite")){
			mMenuBarFavCouponsImage.setImageResource(R.drawable.remove_from_favorite);
			mMenuBarFavCouponsImage.setTag("Remove Favorite");
		}else{
			POJOCouponsList mSelectedCouponItem = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(mCouponPosition);
			if(mSelectedCouponItem.mCouponFavorite.equalsIgnoreCase("No")){				
				mMenuBarFavCouponsImage.setImageResource(R.drawable.add_to_favorite);
				mMenuBarFavCouponsImage.setTag("Add Favorite");
			}else{
				mMenuBarFavCouponsImage.setImageResource(R.drawable.remove_from_favorite);
				mMenuBarFavCouponsImage.setTag("Remove Favorite");
			}
		}

		
		mCouponMenubarFavorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCouponMenubarFavorite.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mCouponMenubarClose.setBackgroundResource(R.drawable.header_2);
				final POJOCouponsList mCouponDetail = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(mCouponPosition);
				
				if((mActivity.equalsIgnoreCase("Favorite") && mCouponDetail.mFavorite_CouponType.equalsIgnoreCase("private"))|| (mMenuBarFavCouponsImage.getTag()!= null &&  mMenuBarFavCouponsImage.getTag().toString().equalsIgnoreCase("Remove Favorite") && mCouponDetail.mCouponType.equalsIgnoreCase("private"))){
					AlertDialog.Builder service_alert = new AlertDialog.Builder(CouponDetail.this);
					service_alert.setTitle("Information");
					service_alert.setMessage("Confirm removing coupon from favorites list");
					service_alert.setPositiveButton("yes",new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							add_remove_FavoriteCoupon(mCouponDetail);
						}
					});
					service_alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							mCouponMenubarFavorite.setBackgroundResource(R.drawable.header_2);
						}
					});
					
					service_alert.show();	
				}else{
					add_remove_FavoriteCoupon(mCouponDetail);
				}
				
			}
		});

		mCouponMenubarClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mCouponMenubarClose.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mCouponMenubarFavorite.setBackgroundResource(R.drawable.header_2);
				finish();
			}
		});

		mCouponDetailNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				mCouponMenubarClose.setBackgroundResource(R.drawable.header_2);
				mCouponMenubarFavorite.setBackgroundResource(R.drawable.header_2);				
				mCouponPosition = mCouponPosition+1;	
				POJOCouponsList mList = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(mCouponPosition);

				//If it's from Favorite No need to Check Coupon is Favorite or Not
				if(mActivity.equalsIgnoreCase("Favorite")){
					mCouponId=mList.mFavorite_CouponId;
					mMenuBarFavCouponsImage.setImageResource(R.drawable.remove_from_favorite);
					mMenuBarFavCouponsImage.setTag("Remove Favorite");
				}else{
					mCouponId=mList.mCouponId;
					if(mList.mCouponFavorite.equalsIgnoreCase("No")){				
						mMenuBarFavCouponsImage.setImageResource(R.drawable.add_to_favorite);
						mMenuBarFavCouponsImage.setTag("Add Favorite");
					}else{
						mMenuBarFavCouponsImage.setImageResource(R.drawable.remove_from_favorite);
						mMenuBarFavCouponsImage.setTag("Remove Favorite");
					}
				}
				if(mCouponPosition == 0){
					mCouponDetailPrev.setEnabled(false);
					mCouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
					mCouponDetailPrev.getBackground().setAlpha(100);
					mCouponDetailNext.setEnabled(true);
					mCouponDetailNext.setBackgroundResource(R.drawable.next_circles);
					mCouponDetailNext.getBackground().setAlpha(255);
					if(mActivity.equalsIgnoreCase("Favorite")){
						mCouponDetailStoreName.setText(mList.mFavorite_StoreName);						
						mCouponDetailName.setText(mList.mFavorite_CouponTitle);		
						mCouponDetailCode.setText(mList.mFavorite_CouponCode);		
						mCouponDetailExpires.setText(mList.mFavorite_CouponExpires);		
						mCouponDescription.setText(mList.mFavorite_CouponDescription);
						if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
							mLoadCoupon.cancel(true);
						mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);						
						mCouponDetailName.setText(mList.mCouponTitle);		
						mCouponDetailCode.setText(mList.mCouponCode);		
						mCouponDetailExpires.setText(mList.mCouponExpires);		
						mCouponDescription.setText(mList.mCouponDescription);
						if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
							mLoadCoupon.cancel(true);
						mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mCouponBarCodeImage);
					}
				}else if(mCouponPosition == WebServiceStaticArrays.mStaticCouponsArrayList.size()-1){
					mCouponDetailNext.setEnabled(false);
					mCouponDetailNext.setBackgroundResource(R.drawable.next_circles);
					mCouponDetailNext.getBackground().setAlpha(100);
					mCouponDetailPrev.setEnabled(true);
					mCouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
					mCouponDetailPrev.getBackground().setAlpha(255);

					if(mActivity.equalsIgnoreCase("Favorite")){
						mCouponDetailStoreName.setText(mList.mFavorite_StoreName);						
						mCouponDetailName.setText(mList.mFavorite_CouponTitle);		
						mCouponDetailCode.setText(mList.mFavorite_CouponCode);		
						mCouponDetailExpires.setText(mList.mFavorite_CouponExpires);		
						mCouponDescription.setText(mList.mFavorite_CouponDescription);
						if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
							mLoadCoupon.cancel(true);
						mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);						
						mCouponDetailName.setText(mList.mCouponTitle);		
						mCouponDetailCode.setText(mList.mCouponCode);		
						mCouponDetailExpires.setText(mList.mCouponExpires);		
						mCouponDescription.setText(mList.mCouponDescription);
						if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
							mLoadCoupon.cancel(true);
						mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mCouponBarCodeImage);
					}
				}else{					
					if(mActivity.equalsIgnoreCase("Favorite")){
						mCouponDetailStoreName.setText(mList.mFavorite_StoreName);						
						mCouponDetailName.setText(mList.mFavorite_CouponTitle);		
						mCouponDetailCode.setText(mList.mFavorite_CouponCode);		
						mCouponDetailExpires.setText(mList.mFavorite_CouponExpires);		
						mCouponDescription.setText(mList.mFavorite_CouponDescription);
						if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
							mLoadCoupon.cancel(true);
						mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);						
						mCouponDetailName.setText(mList.mCouponTitle);		
						mCouponDetailCode.setText(mList.mCouponCode);		
						mCouponDetailExpires.setText(mList.mCouponExpires);		
						mCouponDescription.setText(mList.mCouponDescription);
						if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
							mLoadCoupon.cancel(true);
						LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mCouponBarCodeImage);
					}
					mCouponDetailNext.setBackgroundResource(R.drawable.next_circles);
					mCouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
					mCouponDetailPrev.setEnabled(true);
					mCouponDetailNext.setEnabled(true);
					mCouponDetailNext.getBackground().setAlpha(255);
					mCouponDetailPrev.getBackground().setAlpha(255);
				}
			}
		});

		mCouponDetailPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mCouponMenubarClose.setBackgroundResource(R.drawable.header_2);
				mCouponMenubarFavorite.setBackgroundResource(R.drawable.header_2);				
				mCouponPosition = mCouponPosition-1;
				POJOCouponsList mList = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(mCouponPosition);
				//If it's from Favorite No need to Check Coupon is Favorite or Not
				if(mActivity.equalsIgnoreCase("Favorite")){
					mCouponId=mList.mFavorite_CouponId;
					mMenuBarFavCouponsImage.setImageResource(R.drawable.remove_from_favorite);
					mMenuBarFavCouponsImage.setTag("Remove Favorite");
				}else{
					mCouponId=mList.mCouponId;
					if(mList.mCouponFavorite.equalsIgnoreCase("No")){				
						mMenuBarFavCouponsImage.setImageResource(R.drawable.add_to_favorite);
						mMenuBarFavCouponsImage.setTag("Add Favorite");
					}else{
						mMenuBarFavCouponsImage.setImageResource(R.drawable.remove_from_favorite);
						mMenuBarFavCouponsImage.setTag("Remove Favorite");
					}
				}
				if(mCouponPosition == 0){
					mCouponDetailPrev.setEnabled(false);
					mCouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
					mCouponDetailPrev.getBackground().setAlpha(100);
					mCouponDetailNext.setEnabled(true);
					mCouponDetailNext.setBackgroundResource(R.drawable.next_circles);
					mCouponDetailNext.getBackground().setAlpha(255);
					if(mActivity.equalsIgnoreCase("Favorite")){
						mCouponDetailStoreName.setText(mList.mFavorite_StoreName);						
						mCouponDetailName.setText(mList.mFavorite_CouponTitle);		
						mCouponDetailCode.setText(mList.mFavorite_CouponCode);		
						mCouponDetailExpires.setText(mList.mFavorite_CouponExpires);		
						mCouponDescription.setText(mList.mFavorite_CouponDescription);
						if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
							mLoadCoupon.cancel(true);
						mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);						
						mCouponDetailName.setText(mList.mCouponTitle);		
						mCouponDetailCode.setText(mList.mCouponCode);		
						mCouponDetailExpires.setText(mList.mCouponExpires);		
						mCouponDescription.setText(mList.mCouponDescription);
						if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
							mLoadCoupon.cancel(true);
						mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mCouponBarCodeImage);
					}
				}else if(mCouponPosition == WebServiceStaticArrays.mStaticCouponsArrayList.size()-1){
					mCouponDetailNext.setEnabled(false);
					mCouponDetailNext.setBackgroundResource(R.drawable.next_circles);
					mCouponDetailNext.getBackground().setAlpha(100);
					mCouponDetailPrev.setEnabled(true);
					mCouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
					mCouponDetailPrev.getBackground().setAlpha(255);

					if(mActivity.equalsIgnoreCase("Favorite")){
						mCouponDetailStoreName.setText(mList.mFavorite_StoreName);					
						mCouponDetailName.setText(mList.mFavorite_CouponTitle);		
						mCouponDetailCode.setText(mList.mFavorite_CouponCode);		
						mCouponDetailExpires.setText(mList.mFavorite_CouponExpires);		
						mCouponDescription.setText(mList.mFavorite_CouponDescription);
						if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
							mLoadCoupon.cancel(true);
						mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);						
						mCouponDetailName.setText(mList.mCouponTitle);		
						mCouponDetailCode.setText(mList.mCouponCode);		
						mCouponDetailExpires.setText(mList.mCouponExpires);		
						mCouponDescription.setText(mList.mCouponDescription);
						if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
							mLoadCoupon.cancel(true);
						mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mCouponBarCodeImage);
					}
				}else{
					if(mActivity.equalsIgnoreCase("Favorite")){
						mCouponDetailStoreName.setText(mList.mFavorite_StoreName);						
						mCouponDetailName.setText(mList.mFavorite_CouponTitle);		
						mCouponDetailCode.setText(mList.mFavorite_CouponCode);		
						mCouponDetailExpires.setText(mList.mFavorite_CouponExpires);		
						mCouponDescription.setText(mList.mFavorite_CouponDescription);
						if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
							mLoadCoupon.cancel(true);
						mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);						
						mCouponDetailName.setText(mList.mCouponTitle);		
						mCouponDetailCode.setText(mList.mCouponCode);		
						mCouponDetailExpires.setText(mList.mCouponExpires);		
						mCouponDescription.setText(mList.mCouponDescription);
						if(mLoadCoupon != null && !(mLoadCoupon.getStatus() == AsyncTask.Status.FINISHED))
							mLoadCoupon.cancel(true);
						mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mCouponBarCodeImage);
					}

					mCouponDetailNext.setBackgroundResource(R.drawable.next_circles);
					mCouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
					mCouponDetailPrev.setEnabled(true);
					mCouponDetailNext.setEnabled(true);
					mCouponDetailNext.getBackground().setAlpha(255);
					mCouponDetailPrev.getBackground().setAlpha(255);
				}

			}
		});

		mCouponDescription.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				return true;
			}
		});

		mCouponDescription.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {

				if(view.getId() == R.id.coupon_description){
					view.getParent().requestDisallowInterceptTouchEvent(true);
					switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_UP:
						view.getParent().requestDisallowInterceptTouchEvent(false);						
						break;
					}
				}
				return false;
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Share");
		menu.add(1, v.getId(), 0, "Share By Mail");
		menu.add(1, v.getId(), 0, "Share By FaceBook");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getTitle().equals("Share By Mail")){
			ShareCouponAsynchThread sharecouponasynchthread = new ShareCouponAsynchThread(CouponDetail.this);
			sharecouponasynchthread.execute("Email","",mCouponId);
		}else{
			if(!UserDetails.mServiceFbId.equals("")){
				ShareCouponAsynchThread sharecouponasynchthread = new ShareCouponAsynchThread(CouponDetail.this);
				sharecouponasynchthread.execute("FB","AlreadyLogged",mCouponId);
			}else{
				//Call Fb interface to loggin
			}
		}
		return super.onContextItemSelected(item);
	}

	private void add_remove_FavoriteCoupon(POJOCouponsList mCouponDetail){
		//If it's from Favorite Only Remove Favorite and Show Next Values
		if(mActivity.equalsIgnoreCase("Favorite")){
			AddFavoriteCouponTask mCheckFavorite = new AddFavoriteCouponTask(CouponDetail.this,mCouponDetail.mFavorite_CouponId,
					mMenuBarFavCouponsText,mCouponMenubarFavorite,mCouponDetailName,mCouponDetailCode,
					mCouponDetailExpires,mCouponDescription,mCouponBarCode,mActivity,
					mCouponPosition,mCouponDetailNext,mCouponDetailPrev,mNoBarCodeImage,mMenuBarFavCouponsImage,mProgressBar);
			mCheckFavorite.execute();
		}else{
			//If it's From Store Coupons can ADD/REMOVE To/From Favorite
			AddFavoriteCouponTask mCheckFavorite = new AddFavoriteCouponTask(CouponDetail.this,mCouponDetail.mCouponId,mMenuBarFavCouponsText,mCouponMenubarFavorite,mMenuBarFavCouponsImage);
			mCheckFavorite.execute();
		}
	}
	
	
	
	class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View leftMenu,rightMenu;
		int menuFlag;
		Button mFreezeView;
		/**
		 * Menu must NOT be out/shown to start with.
		 */

		public ClickListenerForScrolling(HorizontalScrollView scrollView, View leftmenu, View rightmenu,int menuflag,Button freezeview) {
			super();
			this.scrollView = scrollView;
			this.leftMenu = leftmenu;
			this.rightMenu = rightmenu;
			this.menuFlag=menuflag;
			this.mFreezeView = freezeview;
		}

		@Override
		public void onClick(View v) {

			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);

			Context context = leftMenu.getContext();
			int menuWidth = mLeftmenu.getMeasuredWidth();
			// Ensure menu is visible
			leftMenu.setVisibility(View.VISIBLE);

			InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


			if (!MenuOutClass.FRIENDS_MENUOUT) {
				if(menuFlag==1){
					setLeftMenuItemStatus(context);
					// Scroll to open left menu
					int left = 0;
					scrollView.smoothScrollTo(left, 0);
					mFreezeView.setVisibility(View.VISIBLE);
				}else if(menuFlag==2){
					//scroll to open right menu
					int right = menuWidth+menuWidth;
					scrollView.smoothScrollTo(right, 0);
					mFreezeView.setVisibility(View.VISIBLE);
				}
			} else {
				// Scroll to menuWidth so menu isn't on screen.
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);
				mFreezeView.setVisibility(View.GONE);
			}
			MenuOutClass.FRIENDS_MENUOUT = !MenuOutClass.FRIENDS_MENUOUT;
		}
	}

	public void setLeftMenuItemStatus(Context context){
		SharedPreferences mUserDetailsPrefs = context.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");
		if(user_login_type.equalsIgnoreCase("customer")){  // Customer
			// Zpay LeftMenu 
			/*mCouponsDetailsZpay.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mCouponsDetailsZpay.setEnabled(true);
			mCouponsDetailsZpayText.setTextColor(Color.WHITE);
			mCouponsDetailsZpayImage.setAlpha(255);*/
			mCouponsDetailsZpay.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsZpay.setEnabled(false);
			mCouponsDetailsZpayText.setTextColor(Color.GRAY);
			mCouponsDetailsZpayImage.setAlpha(100);
			// InvoiceCenter LeftMenu 
			/*mCouponsDetailsInvoiceCenter.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mCouponsDetailsInvoiceCenter.setEnabled(true);
			mCouponsDetailsInvoiceCenterText.setTextColor(Color.WHITE);
			mCouponsDetailsInvoiceCenterImage.setAlpha(255);*/
			mCouponsDetailsInvoiceCenter.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsInvoiceCenter.setEnabled(false);
			mCouponsDetailsInvoiceCenterText.setTextColor(Color.GRAY);
			mCouponsDetailsInvoiceCenterImage.setAlpha(100);
			// Gitcards LeftMenu 
			/*mCouponsDetailsGiftcards.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mCouponsDetailsGiftcards.setEnabled(true);
			mCouponsDetailsGiftCardsText.setTextColor(Color.WHITE);
			mCouponsDetailsGiftCardsImage.setAlpha(255);*/
			mCouponsDetailsGiftcards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsGiftcards.setEnabled(false);
			mCouponsDetailsGiftCardsText.setTextColor(Color.GRAY);
			mCouponsDetailsGiftCardsImage.setAlpha(100);
			// ManageCards LeftMenu 
			/*mCouponsDetailsManageCards.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mCouponsDetailsManageCards.setEnabled(true);
			mCouponsDetailsManageCardsText.setTextColor(Color.WHITE);
			mCouponsDetailsManageCardsImage.setAlpha(255);*/
			mCouponsDetailsManageCards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsManageCards.setEnabled(false);
			mCouponsDetailsManageCardsText.setTextColor(Color.GRAY);
			mCouponsDetailsManageCardsImage.setAlpha(100);
			// Receipts LeftMenu 
			/*mCouponsDetailsReceipts.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mCouponsDetailsReceipts.setEnabled(true);
			mCouponsDetailsReceiptsText.setTextColor(Color.WHITE);
			mCouponsDetailsReceiptsImage.setAlpha(255);*/
			mCouponsDetailsReceipts.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsReceipts.setEnabled(false);
			mCouponsDetailsReceiptsText.setTextColor(Color.GRAY);
			mCouponsDetailsReceiptsImage.setAlpha(100);
			// Favorites LeftMenu 
			mCouponsDetailsMyFavourites.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mCouponsDetailsMyFavourites.setEnabled(true);
			mCouponsDetailsMyFavoritesText.setTextColor(Color.WHITE);
			mCouponsDetailsMyFavoritesImage.setAlpha(255);
			// Friends LeftMenu 
			mCouponsDetailsMyFriends.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mCouponsDetailsMyFriends.setEnabled(true);
			mCouponsDetailsMyFriendsText.setTextColor(Color.WHITE);
			mCouponsDetailsMyFriendsImage.setAlpha(255);
			// CustomerCenter LeftMenu 
			mCouponsDetailsChat.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mCouponsDetailsChat.setEnabled(true);
			mCouponsDetailsChatText.setTextColor(Color.WHITE);
			mCouponsDetailsChatImage.setAlpha(255);
			// ReferStore LeftMenu 
			mCouponsDetailsRewards.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mCouponsDetailsRewards.setEnabled(true);
			mCouponsDetailsRewardsText.setTextColor(Color.WHITE);
			mCouponsDetailsRewardsImage.setAlpha(255);
			// Settings LeftMenu 
			mCouponsDetailsSettings.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mCouponsDetailsSettings.setEnabled(true);
			mCouponsDetailsSettingsText.setTextColor(Color.WHITE);
			mCouponsDetailsSettingsImage.setAlpha(255);
		}else{  // Guest
			// Zpay LeftMenu
			mCouponsDetailsZpay.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsZpay.setEnabled(false);
			mCouponsDetailsZpayText.setTextColor(Color.GRAY);
			mCouponsDetailsZpayImage.setAlpha(100);
			// InvoiceCenter LeftMenu
			mCouponsDetailsInvoiceCenter.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsInvoiceCenter.setEnabled(false);
			mCouponsDetailsInvoiceCenterText.setTextColor(Color.GRAY);
			mCouponsDetailsInvoiceCenterImage.setAlpha(100);
			// Gitcards LeftMenu
			mCouponsDetailsGiftcards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsGiftcards.setEnabled(false);
			mCouponsDetailsGiftCardsText.setTextColor(Color.GRAY);
			mCouponsDetailsGiftCardsImage.setAlpha(100);
			// ManageCards LeftMenu
			mCouponsDetailsManageCards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsManageCards.setEnabled(false);
			mCouponsDetailsManageCardsText.setTextColor(Color.GRAY);
			mCouponsDetailsManageCardsImage.setAlpha(100);
			// Receipts LeftMenu
			mCouponsDetailsReceipts.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsReceipts.setEnabled(false);
			mCouponsDetailsReceiptsText.setTextColor(Color.GRAY);
			mCouponsDetailsReceiptsImage.setAlpha(100);
			// Favorites LeftMenu 
			mCouponsDetailsMyFavourites.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsMyFavourites.setEnabled(false);
			mCouponsDetailsMyFavoritesText.setTextColor(Color.GRAY);
			mCouponsDetailsMyFavoritesImage.setAlpha(100);
			// Friends LeftMenu
			mCouponsDetailsMyFriends.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsMyFriends.setEnabled(false);
			mCouponsDetailsMyFriendsText.setTextColor(Color.GRAY);
			mCouponsDetailsMyFriendsImage.setAlpha(100);
			// CustomerCenter LeftMenu 
			mCouponsDetailsChat.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsChat.setEnabled(false);
			mCouponsDetailsChatText.setTextColor(Color.GRAY);
			mCouponsDetailsChatImage.setAlpha(100);
			// ReferStore LeftMenu 
			mCouponsDetailsRewards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsRewards.setEnabled(false);
			mCouponsDetailsRewardsText.setTextColor(Color.GRAY);
			mCouponsDetailsRewardsImage.setAlpha(100);
			// Settings LeftMenu
			mCouponsDetailsSettings.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mCouponsDetailsSettings.setEnabled(false);
			mCouponsDetailsSettingsText.setTextColor(Color.GRAY);
			mCouponsDetailsSettingsImage.setAlpha(100);
		}
	}

	class SizeCallbackForMenu implements SizeCallback {
		int btnWidth;
		View btnSlide;

		public SizeCallbackForMenu(View btnSlide) {
			super();
			this.btnSlide = btnSlide;
		}

		@Override
		public void onGlobalLayout() {
			btnWidth = (int) (btnSlide.getMeasuredWidth()-10);
			System.out.println("btnWidth=" + btnWidth); //$NON-NLS-1$
		}

		@Override
		public void getViewSize(int idx, int w, int h, int[] dims) {
			dims[0] = w;
			dims[1] = h;
			final int menuIdx = 0;
			if (idx == menuIdx) {
				dims[0] = w - btnWidth;
			}
		}
	}

	//Get Screen width
	public int getScreenWidth(){
		int Measuredwidth = 0;  
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Measuredwidth = display.getWidth(); 
		return Measuredwidth;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

	}

	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(CouponDetail.this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		service_alert.show();
	}



	@Override
	protected void onPause() {
		super.onPause();
		if(!getUserType().equalsIgnoreCase("Guest")){
			unregisterReceiver(mNotificationReceiver);
			if(mNotificationSync!=null)
				mNotificationSync.cancelAlarm();
		}
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		new RefreshZoupons().isApplicationGoneBackground(CouponDetail.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!getUserType().equalsIgnoreCase("Guest")){
			registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
			// To start notification sync
			mNotificationSync = new ScheduleNotificationSync(CouponDetail.this,ZouponsConstants.sCustomerModuleFlag);
			mNotificationSync.setRecurringAlarm();	
		}

		new CheckUserSession(CouponDetail.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(CouponDetail.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	protected void onDestroy() {
		super.onDestroy();   	
		// To notify system that its time to run garbage collector service
		System.gc();
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	private String getUserType(){
		SharedPreferences mPrefs = getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		return mPrefs.getString("user_login_type", "");
	}
	
	
	
	
}