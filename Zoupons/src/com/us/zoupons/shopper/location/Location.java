/**
 * 
 */
package com.us.zoupons.shopper.location;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuBarListClickListener;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.NotificationTabImageVisibility;
import com.us.zoupons.R;
import com.us.zoupons.android.asyncthread_class.StoreLocatorTask;
import com.us.zoupons.android.mapviewcallouts.CustomMapCalloutClickListener;
import com.us.zoupons.android.mapviewcallouts.CustomMapViewCallout;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.CurrentLocation;
import com.us.zoupons.classvariables.MapViewCenter;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.mobilepay.NormalPaymentAsyncTask;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;

/**
 * Class to hold location of store name in listview and mapview
 */
public class Location extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener,OnCameraChangeListener{

	// Initializing views and variables
	private String mTAG="Location";
	public static MyHorizontalScrollView sLocationscrollView;
	public static View sLocationsleftMenu,sLocationrightMenu;
	private View mApp;
	private Typeface mZouponsFont;
	private NetworkCheck mConnectionAvailabilityChecking;
	private RelativeLayout mBtnLogout,mMapViewContainer; 
	private ImageView mNotificationImage,mCalloutTriangleImage,mRighMenuHolder,mMenuBarListImage,mTabBarLoginChoice;
	private Button mNotificationCount;
	private int MenuFlag_Locations=0;
	//LeftMenu
	public LinearLayout mBtnSlide,mListView,mHome,mZpay,mInvoiceCenter,mGiftcards,mManageCards,mReceipts,mMyFavourites,mMyFriends,mChat,mRewards,mSettings,mLogout;
	public TextView mHomeText,mZpayText,mInvoiceCenterText,mGiftCardsText,mManageCardsText,mReceiptsText,mMyFavoritesText,mMyFriendsText,mChatText,mRewardsText,mSettingsText,mLogoutText;
	public ImageView mHomeImage,mZpayImage,mInvoiceCenterImage,mGiftCardsImage,mManageCardsImage,mReceiptsImage,mMyFavoritesImage,mMyFriendsImage,mChatImage,mRewardsImage,mSettingsImage;
	//RightMenu
	public static LinearLayout mInfo_RightMenu,mMobilePay_RightMenu,mGiftCards_RightMenu,mDeals_RightMenu,mCoupons_RightMenu,mSocial_RightMenu,mReviews_RightMenu,mLocations_RightMenu,mPhotos_RightMenu,mVideos_RightMenu,mContactStore_RightMenu,mFavorite_RightMenu;
	public static TextView mInfoText_RightMenu,mMobilePayText_RightMenu,mGiftCardsText_RightMenu,mDealsText_RightMenu,mCouponsText_RightMenu,mSocialText_RightMenu,mReviewsText_RightMenu,mLocationsText_RightMenu,mPhotosText_RightMenu,mVideosText_RightMenu,mContactStoreText_RightMenu,mFavoriteText_RightMenu,mStoreName_RightMenu,mStoreLocation_RightMenu;
	public static ImageView mInfoImage_RightMenu,mMobilePayImage_RightMenu,mGiftCardsImage_RightMenu,mDealsImage_RightMenu,mCouponsImage_RightMenu,mSocialImage_RightMenu,mReviewsImage_RightMenu,mLocationsImage_RightMenu,mPhotosImage_RightMenu,mVideosImage_RightMenu,mContactStoreImage_RightMenu,mFavoriteImage_RightMenu;
	public static LinearLayout mMenuBarContainer,mMenuList;
	public static Button mLocationPageFreezeView;
	private TextView mMenuBarListText,mHeaderStoreName;
	private ViewGroup mTabBarContainer,mTitleBar,mMenuBar,mMiddleView,mLeftMenuItems,mRightMenuItems;
	public static ListView sLocationListView;
	//Notification Pop up layout declaration
	private ScheduleNotificationSync mNotificationSync;
	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	//Get ScrollDistance Variables
	private LatLng mBeforScrollCenter,mAfterScrollCenter;
	private boolean mIsFirstTimeMapScroll;
	private float mCurrentZoom=0;
	private double mDistance = 0;
	public static ScrollView sLocationsleftMenuScrollView,sLocationrightMenuScrollView;
	private ProgressDialog mProgressDialog=null;
	private GoogleMap mGoogleMap;
	private UiSettings mGoogleMapUISettings;
	private LocationClient mLocationClient;
	private Dialog mUpdateGooglePlayServicesDialog = null;
	private final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1;
	private StoreLocatorTask mStoreLocator;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		sLocationscrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(sLocationscrollView);
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();
		sLocationsleftMenu = inflater.inflate(R.layout.shopper_leftmenu, null);
		sLocationrightMenu = inflater.inflate(R.layout.rightmenu, null);
		mApp = inflater.inflate(R.layout.location, null);
		mTabBarContainer = (ViewGroup) mApp.findViewById(R.id.tabBar);
		mTitleBar = (ViewGroup) mApp.findViewById(R.id.titlebar);
		mMenuBar = (ViewGroup) mApp.findViewById(R.id.menubar);
		mMiddleView = (ViewGroup) mApp.findViewById(R.id.middleview);
		mLeftMenuItems = (ViewGroup) sLocationsleftMenu.findViewById(R.id.menuitems);
		mRightMenuItems = (ViewGroup) sLocationrightMenu.findViewById(R.id.rightmenuitems);
		sLocationsleftMenuScrollView = (ScrollView) sLocationsleftMenu.findViewById(R.id.leftmenu_scrollview);
		sLocationsleftMenuScrollView.fullScroll(View.FOCUS_UP);
		sLocationsleftMenuScrollView.pageScroll(View.FOCUS_UP);
		sLocationrightMenuScrollView = (ScrollView) sLocationrightMenu.findViewById(R.id.rightmenu_scrollview);
		sLocationrightMenuScrollView.fullScroll(View.FOCUS_UP);
		sLocationrightMenuScrollView.pageScroll(View.FOCUS_UP);
		mBtnSlide = (LinearLayout) mTabBarContainer.findViewById(R.id.BtnSlide);
		// Notification and log out variables
		mBtnLogout = (RelativeLayout) mTabBarContainer.findViewById(R.id.zoupons_logout_container);
		mNotificationImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_notificationImageId);
		mBtnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(Location.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) mBtnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) mBtnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(Location.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Check whether to show Notification image in zoupons header
		new NotificationTabImageVisibility(Location.this, mNotificationImage).checkNotificationVisibility();
		mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
		mNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mTabBarContainer, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		mNotificationCount.setOnClickListener(new ManageNotificationWindow(this,mTabBarContainer, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		mRighMenuHolder = (ImageView) mTitleBar.findViewById(R.id.location_rightmenu);
		mHeaderStoreName = (TextView) mTitleBar.findViewById(R.id.store_title_textId);
		mHeaderStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);
		final View[] children = new View[] { sLocationsleftMenu, mApp, sLocationrightMenu };
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sLocationscrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(mBtnSlide));
		//sLeftmenu layouts
		mHome = (LinearLayout) mLeftMenuItems.findViewById(R.id.mainmenu_home);
		mZpay = (LinearLayout) mLeftMenuItems.findViewById(R.id.mainmenu_zpay);
		mInvoiceCenter = (LinearLayout) mLeftMenuItems.findViewById(R.id.mainmenu_invoicecenter);
		mGiftcards = (LinearLayout) mLeftMenuItems.findViewById(R.id.mainmenu_zgiftcards);
		mManageCards = (LinearLayout) mLeftMenuItems.findViewById(R.id.mainmenu_managecards);
		mReceipts = (LinearLayout) mLeftMenuItems.findViewById(R.id.mainmenu_receipts);
		mMyFavourites = (LinearLayout) mLeftMenuItems.findViewById(R.id.mainmenu_myfavourites);
		mMyFriends = (LinearLayout) mLeftMenuItems.findViewById(R.id.mainmenu_myfriends);
		mChat = (LinearLayout) mLeftMenuItems.findViewById(R.id.mainmenu_customercenter);
		mRewards = (LinearLayout) mLeftMenuItems.findViewById(R.id.mainmenu_rewards);
		mSettings = (LinearLayout) mLeftMenuItems.findViewById(R.id.mainmenu_settings);
		mLogout = (LinearLayout) mLeftMenuItems.findViewById(R.id.mainmenu_logout);
		//sRightmenu layouts
		mInfo_RightMenu = (LinearLayout) mRightMenuItems.findViewById(R.id.rightmenu_info);
		mMobilePay_RightMenu = (LinearLayout) mRightMenuItems.findViewById(R.id.rightmenu_mobilepay);
		mGiftCards_RightMenu = (LinearLayout) mRightMenuItems.findViewById(R.id.rightmenu_giftcards);
		mDeals_RightMenu=(LinearLayout) mRightMenuItems.findViewById(R.id.rightmenu_deals);
		mCoupons_RightMenu = (LinearLayout) mRightMenuItems.findViewById(R.id.rightmenu_coupons);
		mSocial_RightMenu = (LinearLayout) mRightMenuItems.findViewById(R.id.rightmenu_social);
		mReviews_RightMenu = (LinearLayout) mRightMenuItems.findViewById(R.id.rightmenu_reviews);
		mLocations_RightMenu = (LinearLayout) mRightMenuItems.findViewById(R.id.rightmenu_locations);
		mPhotos_RightMenu = (LinearLayout) mRightMenuItems.findViewById(R.id.rightmenu_photos);
		mVideos_RightMenu = (LinearLayout) mRightMenuItems.findViewById(R.id.rightmenu_videos);
		mContactStore_RightMenu = (LinearLayout) mRightMenuItems.findViewById(R.id.rightmenu_contactstore);
		mFavorite_RightMenu = (LinearLayout) mRightMenuItems.findViewById(R.id.rightmenu_favorite);
		mStoreName_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_storename);
		mStoreName_RightMenu.setTypeface(mZouponsFont);
		mStoreLocation_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_storelocation);
		mStoreLocation_RightMenu.setTypeface(mZouponsFont);
		mInfoText_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_infotext);
		mInfoText_RightMenu.setTypeface(mZouponsFont);
		mMobilePayText_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_mobilepaytext);
		mMobilePayText_RightMenu.setTypeface(mZouponsFont);
		mGiftCardsText_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_giftcardstext);
		mGiftCardsText_RightMenu.setTypeface(mZouponsFont);
		mDealsText_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_dealstext);
		mDealsText_RightMenu.setTypeface(mZouponsFont);
		mCouponsText_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_couponstext);
		mCouponsText_RightMenu.setTypeface(mZouponsFont);
		mSocialText_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_socialtext);
		mSocialText_RightMenu.setTypeface(mZouponsFont);
		mReviewsText_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_reviewstext);
		mReviewsText_RightMenu.setTypeface(mZouponsFont);
		mLocationsText_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_locationstext);
		mLocationsText_RightMenu.setTypeface(mZouponsFont);
		mPhotosText_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_photostext);
		mPhotosText_RightMenu.setTypeface(mZouponsFont);
		mVideosText_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_videostext);
		mVideosText_RightMenu.setTypeface(mZouponsFont);
		mContactStoreText_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_contactstoretext);
		mContactStoreText_RightMenu.setTypeface(mZouponsFont);
		mFavoriteText_RightMenu = (TextView) mRightMenuItems.findViewById(R.id.rightmenu_favoritetext);
		mFavoriteText_RightMenu.setTypeface(mZouponsFont);
		mInfoImage_RightMenu = (ImageView) mRightMenuItems.findViewById(R.id.rightmenu_infoimage);
		mMobilePayImage_RightMenu = (ImageView) mRightMenuItems.findViewById(R.id.rightmenu_zpayimage);
		mGiftCardsImage_RightMenu = (ImageView) mRightMenuItems.findViewById(R.id.rightmenu_giftcardsimage);
		mDealsImage_RightMenu = (ImageView) mRightMenuItems.findViewById(R.id.rightmenu_dealsimage);
		mCouponsImage_RightMenu = (ImageView) mRightMenuItems.findViewById(R.id.rightmenu_couponsimage);
		mSocialImage_RightMenu = (ImageView) mRightMenuItems.findViewById(R.id.rightmenu_socialimage);
		mReviewsImage_RightMenu = (ImageView) mRightMenuItems.findViewById(R.id.rightmenu_reviewsimage);
		mLocationsImage_RightMenu = (ImageView) mRightMenuItems.findViewById(R.id.rightmenu_locationsimage);
		mPhotosImage_RightMenu = (ImageView) mRightMenuItems.findViewById(R.id.rightmenu_photosimage);
		mVideosImage_RightMenu = (ImageView) mRightMenuItems.findViewById(R.id.rightmenu_videoimage);
		mContactStoreImage_RightMenu = (ImageView) mRightMenuItems.findViewById(R.id.rightmenu_contactstoreimage);
		mFavoriteImage_RightMenu = (ImageView) mRightMenuItems.findViewById(R.id.rightmenu_favoriteimage);
		mHomeText = (TextView) mLeftMenuItems.findViewById(R.id.menuHome);
		mHomeText.setTypeface(mZouponsFont);
		mZpayText = (TextView) mLeftMenuItems.findViewById(R.id.menuZpay);
		mZpayText.setTypeface(mZouponsFont);
		mInvoiceCenterText = (TextView) mLeftMenuItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
		mInvoiceCenterText.setTypeface(mZouponsFont);
		mGiftCardsText = (TextView) mLeftMenuItems.findViewById(R.id.menuGiftCards);
		mGiftCardsText.setTypeface(mZouponsFont);
		mManageCardsText = (TextView) mLeftMenuItems.findViewById(R.id.menuManageCards);
		mManageCardsText.setTypeface(mZouponsFont);
		mReceiptsText = (TextView) mLeftMenuItems.findViewById(R.id.menuReceipts);
		mReceiptsText.setTypeface(mZouponsFont);
		mMyFavoritesText = (TextView) mLeftMenuItems.findViewById(R.id.menufavorites);
		mMyFavoritesText.setTypeface(mZouponsFont);
		mMyFriendsText = (TextView) mLeftMenuItems.findViewById(R.id.menuMyFriends);
		mMyFriendsText.setTypeface(mZouponsFont);
		mChatText = (TextView) mLeftMenuItems.findViewById(R.id.menuCustomerCenter);
		mChatText.setTypeface(mZouponsFont);
		mRewardsText = (TextView) mLeftMenuItems.findViewById(R.id.menuRewards);
		mRewardsText.setTypeface(mZouponsFont);
		mSettingsText = (TextView) mLeftMenuItems.findViewById(R.id.menuSettings);
		mSettingsText.setTypeface(mZouponsFont);
		mLogoutText = (TextView) mLeftMenuItems.findViewById(R.id.menuLogout);
		mLogoutText.setTypeface(mZouponsFont);
		mHomeImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuHomeimage);
		mZpayImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuZpayimage);
		mInvoiceCenterImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuInvoiceCenterimage);
		mGiftCardsImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuGiftcardsimage);
		mManageCardsImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuManageCardsimage);
		mReceiptsImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuReceiptsimage);
		mMyFavoritesImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuFavoriteimage);
		mMyFriendsImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuFriendsimage);
		mChatImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuCustomerCenterimage);
		mRewardsImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuRewardsimage);
		mSettingsImage = (ImageView) mLeftMenuItems.findViewById(R.id.menuSettingsimage);
		mMenuBarContainer=(LinearLayout) mMenuBar.findViewById(R.id.menubarcontainer);
		mMenuList=(LinearLayout) mMenuBar.findViewById(R.id.menubar_list);
		mMenuBarListImage=(ImageView) mMenuBar.findViewById(R.id.menubar_list_image);
		mMenuBarListText=(TextView) mMenuBar.findViewById(R.id.menubar_list_text);
		mMenuBarListText.setTypeface(mZouponsFont);
		mMapViewContainer=(RelativeLayout) mMiddleView.findViewById(R.id.location_mapview);
		mLocationPageFreezeView=(Button) mApp.findViewById(R.id.freezeview);
		mListView=(LinearLayout) mMiddleView.findViewById(R.id.locationList);
		sLocationListView = (ListView) mMiddleView.findViewById(R.id.locationListView);
		mProgressDialog=new ProgressDialog(Location.this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage("Loading ...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
		mGoogleMap = null;

		mHome.setOnClickListener(new MenuItemClickListener(mLeftMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mZpay.setOnClickListener(new MenuItemClickListener(mLeftMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mInvoiceCenter.setOnClickListener(new MenuItemClickListener(mLeftMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mGiftcards.setOnClickListener(new MenuItemClickListener(mLeftMenuItems,Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mManageCards.setOnClickListener(new MenuItemClickListener(mLeftMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mReceipts.setOnClickListener(new MenuItemClickListener(mLeftMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mMyFavourites.setOnClickListener(new MenuItemClickListener(mLeftMenuItems,Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mMyFriends.setOnClickListener(new MenuItemClickListener(mLeftMenuItems,Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mChat.setOnClickListener(new MenuItemClickListener(mLeftMenuItems,Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mRewards.setOnClickListener(new MenuItemClickListener(mLeftMenuItems,Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mSettings.setOnClickListener(new MenuItemClickListener(mLeftMenuItems,Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));

		/*ClickListeners for all sRightmenu items*/
		mInfo_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mMobilePay_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mGiftCards_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mDeals_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mCoupons_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mSocial_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mReviews_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mLocations_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mPhotos_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mVideos_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mContactStore_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		mFavorite_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=mTAG,sLocationListView));
		//Function to set right menu items status
		SetRightMenuStatus(Location.this);
		mBtnSlide.setOnClickListener(new ClickListenerForScrolling(sLocationscrollView, sLocationsleftMenu, sLocationrightMenu, MenuFlag_Locations=1, mLocationPageFreezeView));
		mRighMenuHolder.setOnClickListener(new ClickListenerForScrolling(sLocationscrollView, sLocationsleftMenu, sLocationrightMenu, MenuFlag_Locations=2, mLocationPageFreezeView));
		mLocationPageFreezeView.setOnClickListener(new ClickListenerForScrolling(sLocationscrollView, sLocationsleftMenu, sLocationrightMenu, MenuFlag_Locations, mLocationPageFreezeView));
		mMenuList.setOnClickListener(new MenuBarListClickListener(Location.this,mMapViewContainer,mListView,mMiddleView,mMenuBarListText,mMenuBarListImage,0,"Location"));

		mLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(NormalPaymentAsyncTask.mCountDownTimer!=null){
					NormalPaymentAsyncTask.mCountDownTimer.cancel();
					NormalPaymentAsyncTask.mCountDownTimer = null;
					Log.i(mTAG,"Timer Stopped Successfully");
				}

				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(Location.this,"FromManualLogOut").execute();
			}
		});
	}

	// Handler class to update UI items
	Handler handler_response = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			mProgressDialog.dismiss();
			if(msg.obj!=null && msg.obj.toString().equalsIgnoreCase("currentlocation")){
				if(msg.getData() != null){
					MapViewCenter.LoginCurrentLocationLatitude=msg.getData().getDouble("latitude");
					MapViewCenter.LoginCurrentLocationLongitude=msg.getData().getDouble("longitude");
					StoreLocatorTask mStoreLocator = new StoreLocatorTask(Location.this,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude,/*EventFlag,*/RightMenuStoreId_ClassVariables.mStoreID,mGoogleMap,sLocationListView,"location","location",
							mMapViewContainer,mListView,mMiddleView,mMenuBarListText,mMenuBarListImage,0,"PROGRESS");
					mStoreLocator.execute(RightMenuStoreId_ClassVariables.mSelectedStore_lat,RightMenuStoreId_ClassVariables.mSelectedStore_long);
				}
			}
		}
	};

	//Get Screen width
	public int getScreenWidth(){
		int Measuredwidth = 0;  
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Measuredwidth = display.getWidth(); 
		return Measuredwidth;
	}

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's width.
	 */
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

			sLocationsleftMenuScrollView.fullScroll(View.FOCUS_UP);
			sLocationsleftMenuScrollView.pageScroll(View.FOCUS_UP);

			sLocationrightMenuScrollView.fullScroll(View.FOCUS_UP);
			sLocationrightMenuScrollView.pageScroll(View.FOCUS_UP);

			Context context = leftMenu.getContext();
			int menuWidth = leftMenu.getMeasuredWidth();

			InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

			// Ensure menu is visible
			leftMenu.setVisibility(View.VISIBLE);
			if (!MenuOutClass.LOCATION_MENUOUT) {
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
			MenuOutClass.LOCATION_MENUOUT = !MenuOutClass.LOCATION_MENUOUT;
		}
	}

	// Function to set left menu enable disable status based upon user type 

	public void setLeftMenuItemStatus(Context context){
		SharedPreferences mUserDetailsPrefs = context.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");
		if(user_login_type.equalsIgnoreCase("customer")){  // Customer
			// Zpay LeftMenu 
			/*mZpay.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mZpay.setEnabled(true);
			mZpayText.setTextColor(Color.WHITE);
			mZpayImage.setAlpha(255);*/
			mZpay.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mZpay.setEnabled(false);
			mZpayText.setTextColor(Color.GRAY);
			mZpayImage.setAlpha(100);
			// InvoiceCenter LeftMenu 
			/*mInvoiceCenter.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mInvoiceCenter.setEnabled(true);
			mInvoiceCenterText.setTextColor(Color.WHITE);
			mInvoiceCenterImage.setAlpha(255); */
			mInvoiceCenter.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mInvoiceCenter.setEnabled(false);
			mInvoiceCenterText.setTextColor(Color.GRAY);
			mInvoiceCenterImage.setAlpha(100); 
			// Gitcards LeftMenu 
			/*mGiftcards.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mGiftcards.setEnabled(true);
			mGiftCardsText.setTextColor(Color.WHITE);
			mGiftCardsImage.setAlpha(255);*/
			mGiftcards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mGiftcards.setEnabled(false);
			mGiftCardsText.setTextColor(Color.GRAY);
			mGiftCardsImage.setAlpha(100);
			// ManageCards LeftMenu 
		/*	mManageCards.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mManageCards.setEnabled(true);
			mManageCardsText.setTextColor(Color.WHITE);
			mManageCardsImage.setAlpha(255);*/
			mManageCards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mManageCards.setEnabled(false);
			mManageCardsText.setTextColor(Color.GRAY);
			mManageCardsImage.setAlpha(100);
			// Receipts LeftMenu 
			/*mReceipts.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mReceipts.setEnabled(true);
			mReceiptsText.setTextColor(Color.WHITE);
			mReceiptsImage.setAlpha(255);*/
			mReceipts.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mReceipts.setEnabled(false);
			mReceiptsText.setTextColor(Color.GRAY);
			mReceiptsImage.setAlpha(100);
			// Favorites LeftMenu 
			mMyFavourites.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mMyFavourites.setEnabled(true);
			mMyFavoritesText.setTextColor(Color.WHITE);
			mMyFavoritesImage.setAlpha(255);
			// Friends LeftMenu 
			mMyFriends.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mMyFriends.setEnabled(true);
			mMyFriendsText.setTextColor(Color.WHITE);
			mMyFriendsImage.setAlpha(255);
			// CustomerCenter LeftMenu 
			mChat.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mChat.setEnabled(true);
			mChatText.setTextColor(Color.WHITE);
			mChatImage.setAlpha(255);
			// ReferStore LeftMenu 
			mRewards.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mRewards.setEnabled(true);
			mRewardsText.setTextColor(Color.WHITE);
			mRewardsImage.setAlpha(255);
			// Settings LeftMenu 
			mSettings.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mSettings.setEnabled(true);
			mSettingsText.setTextColor(Color.WHITE);
			mSettingsImage.setAlpha(255);
		}else{  // Guest
			// Zpay LeftMenu
			mZpay.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mZpay.setEnabled(false);
			mZpayText.setTextColor(Color.GRAY);
			mZpayImage.setAlpha(100);
			// InvoiceCenter LeftMenu
			mInvoiceCenter.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mInvoiceCenter.setEnabled(false);
			mInvoiceCenterText.setTextColor(Color.GRAY);
			mInvoiceCenterImage.setAlpha(100);
			// Gitcards LeftMenu
			mGiftcards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mGiftcards.setEnabled(false);
			mGiftCardsText.setTextColor(Color.GRAY);
			mGiftCardsImage.setAlpha(100);
			// ManageCards LeftMenu
			mManageCards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mManageCards.setEnabled(false);
			mManageCardsText.setTextColor(Color.GRAY);
			mManageCardsImage.setAlpha(100);
			// Receipts LeftMenu
			mReceipts.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mReceipts.setEnabled(false);
			mReceiptsText.setTextColor(Color.GRAY);
			mReceiptsImage.setAlpha(100);
			// Favorites LeftMenu 
			mMyFavourites.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mMyFavourites.setEnabled(false);
			mMyFavoritesText.setTextColor(Color.GRAY);
			mMyFavoritesImage.setAlpha(100);
			// Friends LeftMenu
			mMyFriends.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mMyFriends.setEnabled(false);
			mMyFriendsText.setTextColor(Color.GRAY);
			mMyFriendsImage.setAlpha(100);
			// CustomerCenter LeftMenu 
			mChat.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mChat.setEnabled(false);
			mChatText.setTextColor(Color.GRAY);
			mChatImage.setAlpha(100);
			// ReferStore LeftMenu 
			mRewards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mRewards.setEnabled(false);
			mRewardsText.setTextColor(Color.GRAY);
			mRewardsImage.setAlpha(100);
			// Settings LeftMenu
			mSettings.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mSettings.setEnabled(false);
			mSettingsText.setTextColor(Color.GRAY);
			mSettingsImage.setAlpha(100);
		}
	}

	/**
	 * Helper that remembers the width of the 'slide' button, so that the 'slide' button remains in view, even when the menu is
	 * showing.
	 */
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sLocationscrollView = null;
		sLocationsleftMenu = null;
		sLocationrightMenu = null;
		mInfo_RightMenu= null;mMobilePay_RightMenu= null;mGiftCards_RightMenu= null;mDeals_RightMenu= null;mCoupons_RightMenu= null;mSocial_RightMenu= null;mReviews_RightMenu= null;mLocations_RightMenu= null;mPhotos_RightMenu= null;mVideos_RightMenu= null;mContactStore_RightMenu= null;mFavorite_RightMenu= null;
		mInfoText_RightMenu= null;mMobilePayText_RightMenu= null;mGiftCardsText_RightMenu= null;mDealsText_RightMenu= null;mCouponsText_RightMenu= null;mSocialText_RightMenu= null;mReviewsText_RightMenu= null;mLocationsText_RightMenu= null;mPhotosText_RightMenu= null;mVideosText_RightMenu= null;mContactStoreText_RightMenu= null;mFavoriteText_RightMenu= null;mStoreName_RightMenu= null;mStoreLocation_RightMenu= null;
		mInfoImage_RightMenu= null;mMobilePayImage_RightMenu= null;mGiftCardsImage_RightMenu= null;mDealsImage_RightMenu= null;mCouponsImage_RightMenu= null;mSocialImage_RightMenu= null;mReviewsImage_RightMenu= null;mLocationsImage_RightMenu= null;mPhotosImage_RightMenu= null;mVideosImage_RightMenu= null;mContactStoreImage_RightMenu= null;mFavoriteImage_RightMenu= null;
		mMenuBarContainer= null;mMenuList= null;
		mLocationPageFreezeView= null;
		sLocationListView= null;
		sLocationsleftMenuScrollView= null;sLocationrightMenuScrollView= null;
		mRightMenuItems = null;
		// To notify  system that its time to run garbage collector service
		System.gc();
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
		new RefreshZoupons().isApplicationGoneBackground(Location.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}


	@Override
	protected void onResume() {
		super.onResume();
		if(mConnectionAvailabilityChecking.ConnectivityCheck(Location.this)){
			if(mGoogleMap == null && checkPlayServices()){
				if(mUpdateGooglePlayServicesDialog != null && mUpdateGooglePlayServicesDialog.isShowing())
					mUpdateGooglePlayServicesDialog.dismiss();
				initilizeGoogleMap();
				getCurrentLocation();
			}
		}else{
			Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
		}
		if(!getUserType().equalsIgnoreCase("Guest")){
			registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
			// To start notification sync
			mNotificationSync = new ScheduleNotificationSync(Location.this,ZouponsConstants.sCustomerModuleFlag);
			mNotificationSync.setRecurringAlarm();
		}
		new CheckUserSession(Location.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(Location.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	protected void onActivityResult(int requestcode, int resultcode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestcode, resultcode, arg2);
		switch (requestcode) {
		case REQUEST_CODE_RECOVER_PLAY_SERVICES : 
			if(checkPlayServices()){
				if(mUpdateGooglePlayServicesDialog != null && mUpdateGooglePlayServicesDialog.isShowing())
					mUpdateGooglePlayServicesDialog.dismiss();
				initilizeGoogleMap();
				getCurrentLocation();
			}else{
				Toast.makeText(this, "Google Play Services must be installed.",Toast.LENGTH_SHORT).show();
				//finish();	
			}
			break;
		default:
		}
	}

	/**
	 * function to load map
	 * */
	private void initilizeGoogleMap() {
		if (mGoogleMap == null) {
			mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.zouponsHomePageMapView)).getMap();
			mGoogleMapUISettings = mGoogleMap.getUiSettings();
			mGoogleMapUISettings.setZoomControlsEnabled(false);
			mGoogleMapUISettings.setMyLocationButtonEnabled(false);
			mGoogleMapUISettings.setCompassEnabled(false);
			mGoogleMapUISettings.setRotateGesturesEnabled(false);
			mGoogleMap.setInfoWindowAdapter(new CustomMapViewCallout(Location.this));
			mGoogleMap.setOnInfoWindowClickListener(new CustomMapCalloutClickListener(Location.this));
			mGoogleMap.setMyLocationEnabled(true);
			mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker arg0) {
					mGoogleMap.setOnCameraChangeListener(null);
					return false;
				}
			});

			// check if map is created successfully or not
			if (mGoogleMap == null) {
				Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}
	}

	//To get user current location    
	public void getCurrentLocation(){
		int response =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(response == ConnectionResult.SUCCESS){
			Toast.makeText(getApplicationContext(), "Please Wait...GPS fetching Current Location", Toast.LENGTH_SHORT).show();
			mLocationClient = new LocationClient(this,this,this);
			mLocationClient.connect();
		}else{
			Toast.makeText(this, "Please update google play application", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		if(mGoogleMap!= null)
			mGoogleMap.setOnCameraChangeListener(this);
		Log.i("user interaction", "detected");
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
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
	public void onBackPressed() {
		//super.onBackPressed();
	}

	// To enable or disable right menu
	public static void SetRightMenuStatus(Context context){
		// To set store name in right menu
		mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
		mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);

		if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("no")){  
			Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
		}else{
			Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
		}

		SharedPreferences mUserDetailsPrefs = context.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");

		if(user_login_type.equalsIgnoreCase("customer")){
			/*
			 * RightMenu Zpay Enable or disable
			 * */
			if(RightMenuStoreId_ClassVariables.rightmenu_aboutus_flag.equalsIgnoreCase("no")){
				Location.mInfo_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mInfo_RightMenu.setEnabled(false);
				Location.mInfoText_RightMenu.setTextColor(Color.GRAY);
				Location.mInfoImage_RightMenu.setAlpha(100);
			}else{
				//Location.mInfo_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mInfo_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mInfo_RightMenu.setEnabled(true);
				Location.mInfoText_RightMenu.setTextColor(Color.WHITE);
				Location.mInfoImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_pointofsale_flag.equalsIgnoreCase("no")){  
				Location.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mMobilePay_RightMenu.setEnabled(false);
				Location.mMobilePayText_RightMenu.setTextColor(Color.GRAY);
				Location.mMobilePayImage_RightMenu.setAlpha(100);
			}else{
				//Location.mMobilePay_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mMobilePay_RightMenu.setEnabled(true);
				Location.mMobilePayText_RightMenu.setTextColor(Color.WHITE);
				Location.mMobilePayImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_giftcards_flag.equalsIgnoreCase("no")){  
				Location.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mGiftCards_RightMenu.setEnabled(false);
				Location.mGiftCardsText_RightMenu.setTextColor(Color.GRAY);
				Location.mGiftCardsImage_RightMenu.setAlpha(100);
			}else{
				//Location.mGiftCards_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mGiftCards_RightMenu.setEnabled(true);
				Location.mGiftCardsText_RightMenu.setTextColor(Color.WHITE);
				Location.mGiftCardsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_zcards_flag.equalsIgnoreCase("no")){
				Location.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mDeals_RightMenu.setEnabled(false);
				Location.mDealsText_RightMenu.setTextColor(Color.GRAY);
				Location.mDealsImage_RightMenu.setAlpha(100);
			}else{
				//Location.mDeals_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mDeals_RightMenu.setEnabled(true);
				Location.mDealsText_RightMenu.setTextColor(Color.WHITE);
				Location.mDealsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_coupons_flag.equalsIgnoreCase("no")){
				Location.mCoupons_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mCoupons_RightMenu.setEnabled(false);
				Location.mCouponsText_RightMenu.setTextColor(Color.GRAY);
				Location.mCouponsImage_RightMenu.setAlpha(100);
			}else{
				//Location.mCoupons_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mCoupons_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mCoupons_RightMenu.setEnabled(true);
				Location.mCouponsText_RightMenu.setTextColor(Color.WHITE);
				Location.mCouponsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_reviewandrating_flag.equalsIgnoreCase("no")){
				Location.mReviews_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mReviews_RightMenu.setEnabled(false);
				Location.mReviewsText_RightMenu.setTextColor(Color.GRAY);
				Location.mReviewsImage_RightMenu.setAlpha(100);
			}else{
				//Location.mReviews_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mReviews_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mReviews_RightMenu.setEnabled(true);
				Location.mReviewsText_RightMenu.setTextColor(Color.WHITE);
				Location.mReviewsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_location_flag.equalsIgnoreCase("no")){
				Location.mLocations_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mLocations_RightMenu.setEnabled(false);
				Location.mLocationsText_RightMenu.setTextColor(Color.GRAY);
				Location.mLocationsImage_RightMenu.setAlpha(100);
			}else{
				//Location.mLocations_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mLocations_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mLocations_RightMenu.setEnabled(true);
				Location.mLocationsText_RightMenu.setTextColor(Color.WHITE);
				Location.mLocationsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_photos_flag.equalsIgnoreCase("no")){
				Location.mPhotos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mPhotos_RightMenu.setEnabled(false);
				Location.mPhotosText_RightMenu.setTextColor(Color.GRAY);
				Location.mPhotosImage_RightMenu.setAlpha(100);
			}else{
				//Location.mPhotos_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mPhotos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mPhotos_RightMenu.setEnabled(true);
				Location.mPhotosText_RightMenu.setTextColor(Color.WHITE);
				Location.mPhotosImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_videos_flag.equalsIgnoreCase("no")){
				Location.mVideos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mVideos_RightMenu.setEnabled(false);
				Location.mVideosText_RightMenu.setTextColor(Color.GRAY);
				Location.mVideosImage_RightMenu.setAlpha(100);
			}else{
				//Location.mVideos_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mVideos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mVideos_RightMenu.setEnabled(true);
				Location.mVideosText_RightMenu.setTextColor(Color.WHITE);
				Location.mVideosImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_contactstore_flag.equalsIgnoreCase("no")){
				Location.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mContactStore_RightMenu.setEnabled(false);
				Location.mContactStoreText_RightMenu.setTextColor(Color.GRAY);
				Location.mContactStoreImage_RightMenu.setAlpha(100);
			}else{
				//Location.mContactStore_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mContactStore_RightMenu.setEnabled(true);
				Location.mContactStoreText_RightMenu.setTextColor(Color.WHITE);
				Location.mContactStoreImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_share_flag.equalsIgnoreCase("no")){
				Location.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mSocial_RightMenu.setEnabled(false);
				Location.mSocialText_RightMenu.setTextColor(Color.GRAY);
				Location.mSocialImage_RightMenu.setAlpha(100);
			}else{
				//Location.mSocial_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mSocial_RightMenu.setEnabled(true);
				Location.mSocialText_RightMenu.setTextColor(Color.WHITE);
				Location.mSocialImage_RightMenu.setAlpha(255);
			}
		}else{
			// Mobile Pay
			Location.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			Location.mMobilePay_RightMenu.setEnabled(false);
			Location.mMobilePayText_RightMenu.setTextColor(Color.GRAY);
			Location.mMobilePayImage_RightMenu.setAlpha(100);
			// MyGiftCards
			Location.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			Location.mGiftCards_RightMenu.setEnabled(false);
			Location.mGiftCardsText_RightMenu.setTextColor(Color.GRAY);
			Location.mGiftCardsImage_RightMenu.setAlpha(100);
			// DealCards
			Location.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			Location.mDeals_RightMenu.setEnabled(false);
			Location.mDealsText_RightMenu.setTextColor(Color.GRAY);
			Location.mDealsImage_RightMenu.setAlpha(100);
			// Share
			Location.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			Location.mSocial_RightMenu.setEnabled(false);
			Location.mSocialText_RightMenu.setTextColor(Color.GRAY);
			Location.mSocialImage_RightMenu.setAlpha(100);
			// Contact store
			Location.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			Location.mContactStore_RightMenu.setEnabled(false);
			Location.mContactStoreText_RightMenu.setTextColor(Color.GRAY);
			Location.mContactStoreImage_RightMenu.setAlpha(100);
			// Favorite
			Location.mFavorite_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			Location.mFavorite_RightMenu.setEnabled(false);
			Location.mFavoriteText_RightMenu.setTextColor(Color.GRAY);
			Location.mFavoriteImage_RightMenu.setAlpha(100);
		}
	}

	// To get distance between center and north west corner of google map current focus area 
	public double getMapViewRadius(LatLng center){
		android.location.Location mCurrentMapViewCenter = new android.location.Location(LocationManager.GPS_PROVIDER);
		mCurrentMapViewCenter.setLatitude(center.latitude);
		mCurrentMapViewCenter.setLongitude(center.longitude);
		LatLng mNorthWestCoordinates= GetMapViewNorthWestGeoPoint();
		//To get NorthWest from the mapview center
		android.location.Location mCurrentMapViewNorthWestEdge = new android.location.Location(LocationManager.GPS_PROVIDER);
		mCurrentMapViewNorthWestEdge.setLatitude(mNorthWestCoordinates.latitude);
		mCurrentMapViewNorthWestEdge.setLongitude(mNorthWestCoordinates.longitude);
		double mDistanceBtwnCenterAndNorthWest = center.longitude - mNorthWestCoordinates.longitude;
		return mDistanceBtwnCenterAndNorthWest;
	}

	// To get distance of google map scroll
	public double GetScrolledDistance(LatLng beforescrollcenter,LatLng afterscrollcenter){
		//Before Scroll MapView Center Lat&Long
		android.location.Location mBeforeScrollMapViewCenter = new android.location.Location(LocationManager.GPS_PROVIDER);
		mBeforeScrollMapViewCenter.setLatitude(beforescrollcenter.latitude);
		mBeforeScrollMapViewCenter.setLongitude(beforescrollcenter.longitude);
		//After Scroll MapView Center Lat&Long
		android.location.Location mAfterScrollMapViewCenter = new android.location.Location(LocationManager.GPS_PROVIDER);
		mAfterScrollMapViewCenter.setLatitude(afterscrollcenter.latitude);
		mAfterScrollMapViewCenter.setLongitude(afterscrollcenter.longitude);
		float mDistanceMetres = mBeforeScrollMapViewCenter.distanceTo(mAfterScrollMapViewCenter);
		double mDistanceMiles = mDistanceMetres*0.000621371;
		return mDistanceMiles;
	}

	// To get Northwest latlng(geopoint) in the map
	private LatLng GetMapViewNorthWestGeoPoint() {
		LatLng mNorthwestCoordinates = null;
		try{
			com.google.android.gms.maps.Projection projection = mGoogleMap.getProjection();
			int y = 1; 
			int x = 1;
			Point mPoint = new Point(x, y);
			mNorthwestCoordinates = projection.fromScreenLocation(mPoint);
		}catch(Exception e){
			e.printStackTrace();
		}
		return mNorthwestCoordinates;
	}

	public boolean mapviewstoreloadingconstraint(int zoomlevel,double scrolleddistance){
		double needDistance=mDistance/2; 
		return conditionCheck(scrolleddistance,needDistance);
	}

	public boolean conditionCheck(double scrolleddistance,double needdistance){
		boolean conditionCheckFlag = false;
		if(scrolleddistance>needdistance){
			conditionCheckFlag = true;
		}
		return conditionCheckFlag;
	}

	@Override
	public void onCameraChange(CameraPosition cameraposition) {
		// TODO Auto-generated method stub
		//To know the mapview distance in pixels 
		MapViewCenter.mMapViewCenter_GeoPoint = cameraposition.target;
		float zoom_during_scroll = cameraposition.zoom;
		boolean flag;
		if(!mIsFirstTimeMapScroll){
			mIsFirstTimeMapScroll=true;
			mAfterScrollCenter=MapViewCenter.mMapViewCenter_GeoPoint;
		}else{
			mBeforScrollCenter=mAfterScrollCenter;
			mAfterScrollCenter=MapViewCenter.mMapViewCenter_GeoPoint;
		}

		if(mCurrentZoom != zoom_during_scroll){
			mCurrentZoom =  zoom_during_scroll;
			Log.i("zoom check","varies");
			flag = true;
			mDistance=getMapViewRadius(MapViewCenter.mMapViewCenter_GeoPoint);
		}else{
			Log.i("zoom check","same");
			//Logic to differentiate the first scroll and next scrolls of mapview
			//Function to calculate distance between MapView Before scroll and After scroll
			double mScrolledDistance=GetScrolledDistance(mBeforScrollCenter, mAfterScrollCenter);
			mDistance=getMapViewRadius(MapViewCenter.mMapViewCenter_GeoPoint);
			//Check the user scrolled distance is greater than our condition
			flag=mapviewstoreloadingconstraint((int)mGoogleMap.getCameraPosition().zoom,mScrolledDistance);
		}

		if(flag){
			RightMenuStoreId_ClassVariables.mSelectedStore_lat=String.valueOf(MapViewCenter.mMapViewCenter_GeoPoint.latitude);
			RightMenuStoreId_ClassVariables.mSelectedStore_long=String.valueOf(MapViewCenter.mMapViewCenter_GeoPoint.longitude);

			if(mStoreLocator != null && (mStoreLocator.getStatus() == AsyncTask.Status.RUNNING || mStoreLocator.getStatus() == AsyncTask.Status.PENDING))
				mStoreLocator.cancel(true);
			mStoreLocator = new StoreLocatorTask(Location.this,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude,RightMenuStoreId_ClassVariables.mStoreID,mGoogleMap,sLocationListView,"location","location",
					mMapViewContainer,mListView,mMiddleView,mMenuBarListText,mMenuBarListImage,0,"NOPROGRESS");
			mStoreLocator.execute(RightMenuStoreId_ClassVariables.mSelectedStore_lat,RightMenuStoreId_ClassVariables.mSelectedStore_long);
		}else{

		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Log.i("location client","on Connection failed");
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Log.i("location client","on Connected");
		if(mLocationClient != null && mLocationClient.getLastLocation() != null){
			android.location.Location mCurrentLocation = mLocationClient.getLastLocation();
			Message msg = new Message();
			msg.obj = "currentlocation";
			Bundle mLocationdetails = new Bundle();
			mLocationdetails.putDouble("latitude", mCurrentLocation.getLatitude());
			mLocationdetails.putDouble("longitude",mCurrentLocation.getLongitude());
			msg.setData(mLocationdetails);
			handler_response.sendMessage(msg);
			mLocationClient.disconnect();
		}else{
			Log.i("location client","on Connection issue");
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}	

	// Function to check if Google Playservices installed in device or not
	private boolean checkPlayServices() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (status != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
				showErrorDialog(status);
			} else {
				Toast.makeText(this, "This device is not supported.", Toast.LENGTH_LONG).show();
				finish();
			}
			return false;
		}
		return true;
	} 

	// To show error dialog if encounters during google map loading
	void showErrorDialog(int code) {
		mUpdateGooglePlayServicesDialog = GooglePlayServicesUtil.getErrorDialog(code, this,REQUEST_CODE_RECOVER_PLAY_SERVICES);
		mUpdateGooglePlayServicesDialog.show();
	}

	private String getUserType(){
		SharedPreferences mPrefs = getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		return mPrefs.getString("user_login_type", "");
	}

}