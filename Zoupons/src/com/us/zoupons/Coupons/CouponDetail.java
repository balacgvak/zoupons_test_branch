package com.us.zoupons.Coupons;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.cards.ImageLoader;
import com.us.zoupons.notification.CustomNotificationAdapter;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotificationItemClickListener;
import com.us.zoupons.notification.ScheduleNotificationSync;


public class CouponDetail extends Activity {

	public static MyHorizontalScrollView scrollView;
	public static View leftmenu,rightmenu;
	View app;
	ImageView btnSlide;
	public static Button CouponsDetailsFreezeButton;
	public Typeface mZouponsFont;
	public static LinearLayout mCouponsDetailsHome,mCouponsDetailsZpay,mCouponsDetailsInvoiceCenter,mCouponsDetailsGiftcards,mCouponsDetailsManageCards,mCouponsDetailsReceipts,mCouponsDetailsMyFavourites,mCouponsDetailsMyFriends,mCouponsDetailsChat,mCouponsDetailsRewards,mCouponsDetailsSettings,
	mCouponsDetailsLogout,mCouponsDetailsLoginLayout;
	public static TextView mCouponsDetailsHomeText,mCouponsDetailsZpayText,mCouponsDetailsInvoiceCenterText,mCouponsDetailsGiftCardsText,mCouponsDetailsManageCardsText,mCouponsDetailsReceiptsText,mCouponsDetailsMyFavoritesText,mCouponsDetailsMyFriendsText,mCouponsDetailsChatText,mCouponsDetailsRewardsText,
	mCouponsDetailsSettingsText,mCouponsDetailsLogoutText,mSendGiftCardText,mInviteGiftCardText;
	public RelativeLayout btnLogout; 
	public ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	public Button mNotificationCount;
	int btnWidth;
	public int mScreenWidth;

	public static String TAG = "CouponDetail";
	public NetworkCheck connectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private ProgressDialog mProgressDialog= null;
	private EditText mCouponDescription;
	private TextView mCouponDetailStoreName,mCouponDetailName,mCouponDetailCode,mCouponDetailExpires,mMenubarFavoriteText,mMenubarShareText,mMenubarCloseText,mNoBarCodeImage,mMenuBarFavCouponsText;
	private ImageView mMenuBarFavCouponsImage;
	private Button mCouponDetailPrev,mCouponDetailNext;
	private ImageView mCouponBarCode;
	private ImageLoader mImageLoader;
	private LinearLayout mCouponMenubarShare,mCouponMenubarFavorite,mCouponMenubarClose;
	public static int mCouponPosition;
	public static String mCouponId;
	private String mActivity="";
	public double mSettingsMenuWidth;
	private View mPopUpLayout;
	private ListView mNotificationList;
	private ScheduleNotificationSync mNotificationSync;
	private static String APP_ID = "319327104838055";
	private SharedPreferences mPrefs;
	public static String mIsCouponAddedAsFavotire,mIsFavoriteCoupons;
	static ScrollView leftMenuScrollView;
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	public ImageView mTabBarLoginChoice;
	private ProgressBar mProgressBar;

	int mCouponWidth,mCouponHeight; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);

		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		connectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice = new ZouponsWebService(CouponDetail.this);
		parsingclass = new ZouponsParsingClass(getApplicationContext());
		mImageLoader= new ImageLoader(getApplicationContext()); 

		mProgressDialog=new ProgressDialog(this);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);

		leftmenu = inflater.inflate(R.layout.horz_scroll_menu, null);
		rightmenu = inflater.inflate(R.layout.rightmenu, null);
		app = inflater.inflate(R.layout.coupon_detail, null);
		int MenuFlag =0;		

		mCouponPosition = Integer.parseInt(getIntent().getStringExtra("CouponPosition"));
		//Check the Is from Favorite
		if(getIntent().getStringExtra("Activity").equalsIgnoreCase("Favorite")){
			mActivity = "Favorite";
		}

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.coupon_detail_tabbar);
		ViewGroup couponscontainer = (ViewGroup) app.findViewById(R.id.coupon_detail_container);
		ViewGroup couponsmenubarcontainer = (ViewGroup) app.findViewById(R.id.coupondetail_menubarcontainer);
		ViewGroup menuLeftItems = (ViewGroup) leftmenu.findViewById(R.id.menuitems);

		leftMenuScrollView = (ScrollView) leftmenu.findViewById(R.id.leftmenu_scrollview);
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
		CouponsDetailsFreezeButton=(Button) app.findViewById(R.id.coupon_detail_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_coupon_detail);
		CouponsDetailsFreezeButton.setOnClickListener(new ClickListenerForScrolling(scrollView, leftmenu, rightmenu, 0, CouponsDetailsFreezeButton));
		btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, leftmenu, rightmenu, 1, CouponsDetailsFreezeButton));
		
		View[] children = new View[] { leftmenu, app};
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(CouponDetail.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);

		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(CouponDetail.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();

		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
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
				new LogoutSessionTask(CouponDetail.this).execute();
				Intent intent_logout = new Intent(CouponDetail.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_logout);
				finish();
			}
		});
		mCouponDetailStoreName = (TextView) app.findViewById(R.id.store_title_textId);
		mCouponDetailName = (TextView) app.findViewById(R.id.coupons_name);
		mCouponDetailCode = (TextView) app.findViewById(R.id.coupon_code_text);
		mCouponDetailExpires = (TextView) app.findViewById(R.id.coupon_expires_text);	
		mCouponDescription = (EditText) app.findViewById(R.id.coupon_description);
		mCouponBarCode = (ImageView) app.findViewById(R.id.coupon_barcode);
		mProgressBar = (ProgressBar) app.findViewById(R.id.mProgress);
		mCouponDetailNext = (Button) app.findViewById(R.id.next_button);
		mCouponDetailPrev = (Button) app.findViewById(R.id.previous_button);
		mNoBarCodeImage = (TextView) app.findViewById(R.id.mNoBarCode);
		mCouponDescription.setKeyListener(null);

		//Menu bar Items
		mCouponMenubarShare = (LinearLayout) couponsmenubarcontainer.findViewById(R.id.menubar_share);
		mCouponMenubarFavorite = (LinearLayout) couponsmenubarcontainer.findViewById(R.id.menubar_addtofavorite);
		mCouponMenubarClose = (LinearLayout) couponsmenubarcontainer.findViewById(R.id.menubar_close);

		mMenubarShareText = (TextView) couponsmenubarcontainer.findViewById(R.id.menubar_share_text);		
		mMenuBarFavCouponsText = (TextView) couponsmenubarcontainer.findViewById(R.id.menubar_favorite_text);
		mMenuBarFavCouponsImage = (ImageView) couponsmenubarcontainer.findViewById(R.id.menubar_favorite_image);
		mMenubarCloseText = (TextView) couponsmenubarcontainer.findViewById(R.id.menubar_close_text);

		//Call function to get width of the screen
		mScreenWidth=getScreenWidth(); 	
		if(mScreenWidth>0){	//To fix Home Page menubar items width
			mSettingsMenuWidth=mScreenWidth/3;
		}

		mCouponMenubarShare.setLayoutParams(new LinearLayout.LayoutParams((int)mSettingsMenuWidth,LayoutParams.FILL_PARENT,1f));
		mCouponMenubarFavorite.setLayoutParams(new LinearLayout.LayoutParams((int)mSettingsMenuWidth,LayoutParams.FILL_PARENT,1f));
		mCouponMenubarClose.setLayoutParams(new LinearLayout.LayoutParams((int)mSettingsMenuWidth,LayoutParams.FILL_PARENT,1f));

		POJOCouponsList mList = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(mCouponPosition);
		
		if(mActivity.equalsIgnoreCase("Favorite")){
			mCouponDetailStoreName.setText(mList.mFavorite_StoreName);
			mCouponId=mList.mFavorite_CouponId;
			mCouponDetailName.setText(mList.mFavorite_CouponTitle);		
			mCouponDetailCode.setText(mList.mFavorite_CouponCode);		
			mCouponDetailExpires.setText(mList.mFavorite_CouponExpires);		
			mCouponDescription.setText(mList.mFavorite_CouponDescription);
			LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
			mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
			
		}else{
			mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);
			mCouponId=mList.mCouponId;
			mCouponDetailName.setText(mList.mCouponTitle);		
			mCouponDetailCode.setText(mList.mCouponCode);		
			mCouponDetailExpires.setText(mList.mCouponExpires);		
			mCouponDescription.setText(mList.mCouponDescription);
			LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
			mLoadCoupon.execute(mList.mCouponBarCodeImage);
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

		//If it's from Favorite No need to Check Coupon is Favorite or Not
		if(mActivity.equalsIgnoreCase("Favorite")){
			mMenuBarFavCouponsText.setText("Favorite");
			mMenuBarFavCouponsImage.setImageResource(R.drawable.remove_from_favorite);
		}else{
			POJOCouponsList mSelectedCouponItem = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(mCouponPosition);
			if(mSelectedCouponItem.mCouponFavorite.equalsIgnoreCase("No")){				
				mMenuBarFavCouponsText.setText("Favorite");
				mMenuBarFavCouponsImage.setImageResource(R.drawable.add_to_favorite);
			}else{
				mMenuBarFavCouponsText.setText("Favorite");
				mMenuBarFavCouponsImage.setImageResource(R.drawable.remove_from_favorite);
			}
		}

		mCouponMenubarFavorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.i("Favorite", "Menu Clicked");
				mCouponMenubarFavorite.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mCouponMenubarClose.setBackgroundResource(R.drawable.header_2);
				POJOCouponsList mList = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(mCouponPosition);				
				//If it's from Favorite Only Remove Favorite and Show Next Values
				if(mActivity.equalsIgnoreCase("Favorite")){
					Log.i(TAG, "Coupon Detail Screen Favorite");
					
					AddFavoriteCouponTask mCheckFavorite = new AddFavoriteCouponTask(CouponDetail.this,mList.mFavorite_CouponId,
							mMenuBarFavCouponsText,mCouponMenubarFavorite,mCouponDetailName,mCouponDetailCode,
							mCouponDetailExpires,mCouponDescription,mCouponBarCode,mActivity,
							mCouponPosition,mCouponDetailNext,mCouponDetailPrev,mNoBarCodeImage,mMenuBarFavCouponsImage,mProgressBar);
					mCheckFavorite.execute();
				}else{
					//If it's From Store Coupons can ADD/REMOVE To/From Favorite
					Log.i(TAG, "Coupon Detail Screen");
					AddFavoriteCouponTask mCheckFavorite = new AddFavoriteCouponTask(CouponDetail.this,mList.mCouponId,mMenuBarFavCouponsText,mCouponMenubarFavorite,mMenuBarFavCouponsImage);
					mCheckFavorite.execute();
				}
			}
		});

		mCouponMenubarClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.i("Close", "Menu Clicked");
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
					mMenuBarFavCouponsText.setText("Remove From Favs");
					mMenuBarFavCouponsImage.setImageResource(R.drawable.remove_from_favorite);
				}else{
					mCouponId=mList.mCouponId;
					if(mList.mCouponFavorite.equalsIgnoreCase("No")){				
						mMenuBarFavCouponsText.setText("Add To Favs");
						mMenuBarFavCouponsImage.setImageResource(R.drawable.add_to_favorite);
					}else{
						mMenuBarFavCouponsText.setText("Remove From Favs");
						mMenuBarFavCouponsImage.setImageResource(R.drawable.remove_from_favorite);
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
						LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);						
						mCouponDetailName.setText(mList.mCouponTitle);		
						mCouponDetailCode.setText(mList.mCouponCode);		
						mCouponDetailExpires.setText(mList.mCouponExpires);		
						mCouponDescription.setText(mList.mCouponDescription);
						LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
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
						LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);						
						mCouponDetailName.setText(mList.mCouponTitle);		
						mCouponDetailCode.setText(mList.mCouponCode);		
						mCouponDetailExpires.setText(mList.mCouponExpires);		
						mCouponDescription.setText(mList.mCouponDescription);
						LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mCouponBarCodeImage);
					}
				}else{					
					if(mActivity.equalsIgnoreCase("Favorite")){
						mCouponDetailStoreName.setText(mList.mFavorite_StoreName);						
						mCouponDetailName.setText(mList.mFavorite_CouponTitle);		
						mCouponDetailCode.setText(mList.mFavorite_CouponCode);		
						mCouponDetailExpires.setText(mList.mFavorite_CouponExpires);		
						mCouponDescription.setText(mList.mFavorite_CouponDescription);
						LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);						
						mCouponDetailName.setText(mList.mCouponTitle);		
						mCouponDetailCode.setText(mList.mCouponCode);		
						mCouponDetailExpires.setText(mList.mCouponExpires);		
						mCouponDescription.setText(mList.mCouponDescription);
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
					mMenuBarFavCouponsText.setText("Remove From Favs");
					mMenuBarFavCouponsImage.setImageResource(R.drawable.remove_from_favorite);
				}else{
					mCouponId=mList.mCouponId;
					if(mList.mCouponFavorite.equalsIgnoreCase("No")){				
						mMenuBarFavCouponsText.setText("Add To Favs");
						mMenuBarFavCouponsImage.setImageResource(R.drawable.add_to_favorite);
					}else{
						mMenuBarFavCouponsText.setText("Remove From Favs");
						mMenuBarFavCouponsImage.setImageResource(R.drawable.remove_from_favorite);
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
						LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);						
						mCouponDetailName.setText(mList.mCouponTitle);		
						mCouponDetailCode.setText(mList.mCouponCode);		
						mCouponDetailExpires.setText(mList.mCouponExpires);		
						mCouponDescription.setText(mList.mCouponDescription);
						LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
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
						LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);						
						mCouponDetailName.setText(mList.mCouponTitle);		
						mCouponDetailCode.setText(mList.mCouponCode);		
						mCouponDetailExpires.setText(mList.mCouponExpires);		
						mCouponDescription.setText(mList.mCouponDescription);
						LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mCouponBarCodeImage);
					}
				}else{
					if(mActivity.equalsIgnoreCase("Favorite")){
						mCouponDetailStoreName.setText(mList.mFavorite_StoreName);						
						mCouponDetailName.setText(mList.mFavorite_CouponTitle);		
						mCouponDetailCode.setText(mList.mFavorite_CouponCode);		
						mCouponDetailExpires.setText(mList.mFavorite_CouponExpires);		
						mCouponDescription.setText(mList.mFavorite_CouponDescription);
						LoadCouponQRcodeTask mLoadCoupon = new LoadCouponQRcodeTask(mCouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						mCouponDetailStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);						
						mCouponDetailName.setText(mList.mCouponTitle);		
						mCouponDetailCode.setText(mList.mCouponCode);		
						mCouponDetailExpires.setText(mList.mCouponExpires);		
						mCouponDescription.setText(mList.mCouponDescription);
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
				Log.i("facebook", "fresh Login");
			}
		}
		return super.onContextItemSelected(item);
	}

	private Handler handler_facebook = new Handler(){
		public void handleMessage(Message msg) {
			if(mProgressDialog.isShowing()){
				mProgressDialog.dismiss();
			}
			if(msg.obj.equals("cancel")){
				alertBox_service("Information", "You have to loggin to Facebook for further process.");
			}else if(msg.obj.equals("sharecoupon")){
				Log.i(TAG,"Coupon ShareByFB: "+"Work in Progress");
				ShareCouponAsynchThread sharecouponasynchthread = new ShareCouponAsynchThread(CouponDetail.this);
				sharecouponasynchthread.execute("FB","JustLogged",mCouponId);
			}
		}
	};


	static class ClickListenerForScrolling implements OnClickListener {
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
			int menuWidth = leftmenu.getMeasuredWidth();
			// Ensure menu is visible
			leftMenu.setVisibility(View.VISIBLE);

			InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


			if (!MenuOutClass.FRIENDS_MENUOUT) {
				if(menuFlag==1){
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

	static class SizeCallbackForMenu implements SizeCallback {
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
		int Measuredheight = 0;  
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Measuredwidth = display.getWidth(); 
		Measuredheight = display.getHeight();
		return Measuredwidth;
	}

	private void InitializePopUpLayout(final View tabBar){
		// Notitification pop up layout declaration
		LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopUpLayout = mInflater.inflate(R.layout.notification, (ViewGroup) findViewById(R.id.mPopUpParentLayout));
		mNotificationList = (ListView) mPopUpLayout.findViewById(R.id.notification_list);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		int popupLeftPadding = 0;
		if(getWindowManager().getDefaultDisplay().getWidth()<1000){
			popupLeftPadding = 30;
		}else{
			popupLeftPadding = 400;
		}
		mLayoutParams.leftMargin = popupLeftPadding;
		mNotificationList.setLayoutParams(mLayoutParams);
		mNotificationImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Inflating custom Layout for PopUpWindow.
				mCalloutTriangleImage.setVisibility(View.VISIBLE);
				// Initializing PopUpWindow
				int popupheight = getWindowManager().getDefaultDisplay().getHeight()/2;
				final PopupWindow mPopUpWindow = new PopupWindow(mPopUpLayout,android.view.WindowManager.LayoutParams.WRAP_CONTENT,popupheight,true);
				mPopUpWindow.setWidth(tabBar.getWidth()-20);
				mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopUpWindow.setOutsideTouchable(false);
				mPopUpWindow.showAsDropDown(tabBar, 0, 20);
				mNotificationList.setAdapter(new CustomNotificationAdapter(CouponDetail.this));
				mNotificationList.setOnItemClickListener(new NotificationItemClickListener());

				mPopUpWindow.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						mCalloutTriangleImage.setVisibility(View.INVISIBLE);
					}
				});
				// To dismiss popup window when touch outside..
				final Rect listviewRect = new Rect();
				mNotificationList.getHitRect(listviewRect);
				mPopUpLayout.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (!listviewRect.contains((int)event.getX(), (int)event.getY())) {
							Log.i("touch", "dismiss pop up");
							mPopUpWindow.dismiss();	
						}else{
							Log.i("touch", "dont dismiss");
						}
						return false;
					}
				});
			}
		});

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mCouponWidth=mCouponBarCode.getWidth();
		mCouponHeight=mCouponBarCode.getHeight();
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
		unregisterReceiver(mReceiver);
		if(mNotificationSync!=null){
			mNotificationSync.cancelAlarm();
		}
	}

	@Override
	protected void onUserLeaveHint() {
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		new RefreshZoupons().isApplicationGoneBackground(CouponDetail.this);
		super.onUserLeaveHint();
	}

	@Override
	protected void onResume() {
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(CouponDetail.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(CouponDetail.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(CouponDetail.this);
		mLogoutSession.setLogoutTimerAlarm();

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
	
	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			try{
				Log.i(TAG,"OnReceive");
				if(intent.hasExtra("FromNotification")){
					if(NotificationDetails.notificationcount>0){
						mNotificationCount.setVisibility(View.VISIBLE);
						mNotificationCount.setText(String.valueOf(NotificationDetails.notificationcount));
					}else{
						mNotificationCount.setVisibility(View.GONE);
						Log.i("Notification result", "No new notification");
					}
				}					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
}