package com.us.zoupons.shopper.home;


import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.us.zoupons.DoneOnEditorActionListener;
import com.us.zoupons.FreezeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuBarBrowseClickListener;
import com.us.zoupons.MenuBarListClickListener;
import com.us.zoupons.MenuBarQRCodeClickListener;
import com.us.zoupons.MenuBarSearchClickListener;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.NotificationTabImageVisibility;
import com.us.zoupons.R;
import com.us.zoupons.android.asyncthread_class.StoreNearCurrentLocationAsyncThread;
import com.us.zoupons.android.asyncthread_class.StoresLocationAsynchThread;
import com.us.zoupons.android.asyncthread_class.StoresQRCodeAsynchThread;
import com.us.zoupons.android.lib.integrator.IntentIntegrator;
import com.us.zoupons.android.lib.integrator.IntentResult;
import com.us.zoupons.android.mapviewcallouts.CustomMapCalloutClickListener;
import com.us.zoupons.android.mapviewcallouts.CustomMapViewCallout;
import com.us.zoupons.animation.CustomAnimationListener;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.Categories_ClassVariables;
import com.us.zoupons.classvariables.CurrentLocation;
import com.us.zoupons.classvariables.MapViewCenter;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.Search_ClassVariables;
import com.us.zoupons.classvariables.StoreCategories_ClassVariables;
import com.us.zoupons.classvariables.StoreLocator_ClassVariables;
import com.us.zoupons.classvariables.SubCategories_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.flagclasses.ZPayFlag;
import com.us.zoupons.listview_inflater_classes.CustomStoreAdapter;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.mobilepay.NormalPaymentAsyncTask;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 *  Zoupons home page to load stores
 */

public class ShopperHomePage extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener,OnCameraChangeListener{

	// Initializing views and variables
	public static MyHorizontalScrollView sScrollView;
	public static View sLeftMenu,sRightMenu,mApp;
	private Typeface mZouponsFont; 
	private LinearLayout mBtnSlide,mMenuQRCode,mMenuList,mMenuBrowse,mMenuSearch,mMenuBarContainer,mListView,mZpayBar;
	private RelativeLayout mBtnLogout,mSearchBoxContainer,mMapViewContainer; 
	//LeftMenu
	public static LinearLayout mHome,mZpay,mInvoiceCenter,mGiftcards,mManageCards,mReceipts,mMyFavourites,mMyFriends,mChat,mRewards,mSettings,mLogout;
	public static TextView mHomeText,mZpayText,mInvoiceCenterText,mGiftCardsText,mManageCardsText,mReceiptsText,mMyFavoritesText,mMyFriendsText,mChatText,mRewardsText,mSettingsText,mLogoutText;
	public static ImageView mHomeImage,mZpayImage,mInvoiceCenterImage,mGiftCardsImage,mManageCardsImage,mReceiptsImage,mMyFavoritesImage,mMyFriendsImage,mChatImage,mRewardsImage,mSettingsImage;
	//RightMenu
	public static LinearLayout mInfo_RightMenu,mMobilePay_RightMenu,mGiftCards_RightMenu,mDeals_RightMenu,mCoupons_RightMenu,mSocial_RightMenu,mReviews_RightMenu,mLocations_RightMenu,mPhotos_RightMenu,mVideos_RightMenu,mContactStore_RightMenu,mFavorite_RightMenu;
	public static TextView mInfoText_RightMenu,mMobilePayText_RightMenu,mGiftCardsText_RightMenu,mDealsText_RightMenu,mCouponsText_RightMenu,mSocialText_RightMenu,mReviewsText_RightMenu,mLocationsText_RightMenu,mPhotosText_RightMenu,mVideosText_RightMenu,mContactStoreText_RightMenu,mFavoriteText_RightMenu,mStoreName_RightMenu,mStoreLocation_RightMenu;
	public static ImageView mInfoImage_RightMenu,mMobilePayImage_RightMenu,mGiftCardsImage_RightMenu,mDealsImage_RightMenu,mCouponsImage_RightMenu,mSocialImage_RightMenu,mReviewsImage_RightMenu,mLocationsImage_RightMenu,mPhotosImage_RightMenu,mVideosImage_RightMenu,mContactStoreImage_RightMenu,mFavoriteImage_RightMenu;
	public static AutoCompleteTextView sSearchStoreName;
	private  Button mCancelStoreName,mCurrentLocation,mNotificationCount,mZpayQRCode,mZpayFavorites,mZpaySearch,mZpayList,mCategoriesCancel;
	public static Button sHomePageFreezeView;
	private ImageView mMenuBarListImage,mLogoutImage,mNotificationImage,mCalloutTriangleImage,mTabBarLoginChoice,mZouponsHomePage;
	private TextView mMenuBarBrowseText,mMenuBarQRCodeText,mMenuBarSearchText,mMenuBarListText;
	private ViewGroup mTabBar,mMenuBar,mMiddleView,mLeftMenuItems,mRightMenuItems;
	public static ScrollView sLeftMenuScrollView, sRightMenuScrollView;;
	private  ViewGroup mSearchBar;
	private AnimationSet mShrinkSearch;
	private ListView mShopListView;
	//zpay bar
	private TextView mZpayStep1_Search,mZpayStep2_ChooseCard,mZpayStep3_Approve;
	Handler handler = new Handler();
	private String TAG = "ShopperHomePage"; 
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	//public static TapControlledMapView mMapView; // use the custom TapControlledMapView
	private GoogleMap mGoogleMap;
	private UiSettings mGoogleMapUISettings;
	private LocationClient mLocationClient;
	public static LatLng mLocationGeoCordinates;
	private int mStoreCount;
	private double mLatitude,mLongitude,mMenuWidth;
	private int mCategoriesViewFlag;
	private ProgressDialog mProgressDialog=null;
	private boolean mMessageFlag,mMessageFlag1;
	private CustomStoreAdapter mStoreAdapter;
	public static boolean sKeyboardstate;
	public static int searchcloseflag=0;
	static int sLeft=0;
	// for store details list view offset
	public static String sStoreDetailsStart="0",sStoreDetailsEndLimit="20",sStoreDetailsCount = "0";	
	public static boolean sIsLoadMore = false;
	public static View sFooterLayout;
	private LayoutInflater mInflater;
	public static double sDistance = 0;
	private String mStore_categoriesId = "";
	private String mSearch_storeId="";
	public static String sMapViewOnScrollViewFlag="";
	//Get ScrollDistance Variables
	private LatLng mBeforScrollCenter,mAfterScrollCenter;
	private boolean mIsFirstTimeMapScroll;
	// For categories header view
	private RelativeLayout mCategoriesHeader;
	private TextView mCategoriesText,mSubCategoritesText;
	private String mSelectedcategories,mSelectedSubCategories;
	private ArrayList<Object> mSubcategoriesData;
	private boolean mIsSubCategoriesSelected = false;
	private Menu mSubCategoriesMenu;
	private ScheduleNotificationSync mNotificationSync;
	private float mCurrentZoom=0;
	public static String sDeviceCurrentLocationLatitude="",sDeviceCurrentLocationLongitude="";
	public static boolean sIsActivity_live=true;
	private final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1;
	private Dialog mUpdateGooglePlayServicesDialog = null;
	// set this to true to stop the thread
	volatile boolean shutdown = false;
	private StoreNearCurrentLocationAsyncThread mStoreNearThread;
	private StoresLocationAsynchThread mStoresLocationAsynchThread;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation
	private CheckLogoutSession mLogoutSession;
	private LocationManager mLocationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Intantiating views and viewgroups
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mProgressDialog=new ProgressDialog(ShopperHomePage.this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage("Loading Stores...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
		mZouponswebservice = new ZouponsWebService(ShopperHomePage.this);
		mParsingclass = new ZouponsParsingClass(ShopperHomePage.this);
		LayoutInflater inflater = LayoutInflater.from(this);
		sScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(sScrollView);
		mConnectionAvailabilityChecking = new NetworkCheck();
		mShrinkSearch = new AnimationSet(true);
		SharedPreferences mPrefs = getSharedPreferences("UserNamePrefences", MODE_PRIVATE);
		if(!mPrefs.getString("user_login_type", "").equalsIgnoreCase("Guest")){ // If user_type is not Guest , assign user details from preferences
			UserDetails.mUserType = mPrefs.getString("user_type", "");
			UserDetails.mServiceUserId = mPrefs.getString("user_id", "");
			UserDetails.mServiceFbId = mPrefs.getString("fb_id", "");
		}else{ // Flush user details if user_type is Guest
			UserDetails.mUserType = "";
			UserDetails.mServiceUserId = "0";
			UserDetails.mServiceFbId = "";
			// To flush preferences values
			Editor mSharedPreferenceEditor = mPrefs.edit();
			mSharedPreferenceEditor.putString("user_id", "0");
			mSharedPreferenceEditor.commit();
			// Clear notification data
			WebServiceStaticArrays.mAllNotifications.clear();
			NotificationDetails.notificationcount = 0;
		}

		/*
		 * Condition to intialize the search box to false state
		 **/
		if(MenuBarSearchClickListener.sCLICKFLAG==true){
			MenuBarSearchClickListener.sCLICKFLAG=false;
		}
		sLeftMenu = inflater.inflate(R.layout.shopper_leftmenu, null);
		sRightMenu = inflater.inflate(R.layout.rightmenu, null);
		mApp = inflater.inflate(R.layout.shopper_homepage, null);

		mTabBar = (ViewGroup) mApp.findViewById(R.id.tabBar);
		mSearchBar = (ViewGroup) mApp.findViewById(R.id.searchbar);
		mMenuBar = (ViewGroup) mApp.findViewById(R.id.menubar);
		mMiddleView = (ViewGroup) mApp.findViewById(R.id.middleview);
		mLeftMenuItems = (ViewGroup) sLeftMenu.findViewById(R.id.menuitems);
		mRightMenuItems = (ViewGroup) sRightMenu.findViewById(R.id.rightmenuitems);

		sLeftMenuScrollView = (ScrollView) sLeftMenu.findViewById(R.id.leftmenu_scrollview);
		sLeftMenuScrollView.fullScroll(View.FOCUS_UP);
		sLeftMenuScrollView.pageScroll(View.FOCUS_UP);

		sRightMenuScrollView = (ScrollView) sRightMenu.findViewById(R.id.rightmenu_scrollview);
		sRightMenuScrollView.fullScroll(View.FOCUS_UP);
		sRightMenuScrollView.pageScroll(View.FOCUS_UP);

		mBtnSlide = (LinearLayout) mTabBar.findViewById(R.id.BtnSlide);
		mBtnLogout = (RelativeLayout) mTabBar.findViewById(R.id.zoupons_homepage_logout_container);
		// For notification and logout
		mLogoutImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_homepage_logout_btn);
		mNotificationImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_notificationImageId);
		mCalloutTriangleImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_homepage_callout_triangle);
		mNotificationCount = (Button) mBtnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) mBtnLogout.findViewById(R.id.store_header_loginchoice);
		mZouponsHomePage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_home);
		mZouponsHomePage.setAlpha(100);
		mLocationManager = (LocationManager)getApplicationContext().getSystemService( Context.LOCATION_SERVICE );
		// Check whether to show login choice image in zoupons header
		new LoginChoiceTabBarImage(ShopperHomePage.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Check whether to show Notification image in zoupons header
		new NotificationTabImageVisibility(ShopperHomePage.this, mNotificationImage).checkNotificationVisibility();
		final View[] children = new View[] { sLeftMenu, mApp, sRightMenu };

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(mBtnSlide));

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

		/*Right Navigation*/
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
		sSearchStoreName = (AutoCompleteTextView) mSearchBar.findViewById(R.id.search_storename);
		mCancelStoreName = (Button) mSearchBar.findViewById(R.id.clear_storename);
		mCancelStoreName.setTypeface(mZouponsFont);
		mSearchBoxContainer=(RelativeLayout) mSearchBar.findViewById(R.id.searchbox_container);
		mZpayBar=(LinearLayout) mSearchBar.findViewById(R.id.zpaybar);
		mZpayStep1_Search=(TextView) mSearchBar.findViewById(R.id.step1_txt);
		mZpayStep1_Search.setTypeface(mZouponsFont);
		mZpayStep2_ChooseCard=(TextView) mSearchBar.findViewById(R.id.step2_txt);
		mZpayStep2_ChooseCard.setTypeface(mZouponsFont);
		mZpayStep3_Approve=(TextView) mSearchBar.findViewById(R.id.step3_txt);
		mZpayStep3_Approve.setTypeface(mZouponsFont);
		mZpayQRCode=(Button) mSearchBar.findViewById(R.id.qrcode_zpay);
		mZpayQRCode.setTypeface(mZouponsFont);
		mZpayFavorites=(Button) mSearchBar.findViewById(R.id.favorites_zpay);
		mZpayFavorites.setTypeface(mZouponsFont);
		mZpaySearch=(Button) mSearchBar.findViewById(R.id.search_zpay);
		mZpaySearch.setTypeface(mZouponsFont);
		mZpayList=(Button) mSearchBar.findViewById(R.id.listview_zpay);
		mZpayList.setTypeface(mZouponsFont);

		mMenuBarContainer=(LinearLayout) mMenuBar.findViewById(R.id.menubarcontainer);
		mMenuBrowse=(LinearLayout) mMenuBar.findViewById(R.id.menubar_browse);
		mMenuBrowse.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
		mMenuQRCode=(LinearLayout) mMenuBar.findViewById(R.id.menubar_qrcode);
		mMenuQRCode.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
		mMenuSearch=(LinearLayout) mMenuBar.findViewById(R.id.menubar_search);
		mMenuSearch.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
		mMenuList=(LinearLayout) mMenuBar.findViewById(R.id.menubar_list);
		mMenuList.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
		mMenuBarListImage=(ImageView) mMenuBar.findViewById(R.id.menubar_list_image);

		mMenuBarBrowseText=(TextView) mMenuBar.findViewById(R.id.menubar_browse_text);
		mMenuBarBrowseText.setTypeface(mZouponsFont);
		mMenuBarQRCodeText=(TextView) mMenuBar.findViewById(R.id.menubar_qrcode_text);
		mMenuBarQRCodeText.setTypeface(mZouponsFont);
		mMenuBarSearchText=(TextView) mMenuBar.findViewById(R.id.menubar_search_text);
		mMenuBarSearchText.setTypeface(mZouponsFont);
		mMenuBarListText=(TextView) mMenuBar.findViewById(R.id.menubar_list_text);
		mMenuBarListText.setTypeface(mZouponsFont);

		mMapViewContainer=(RelativeLayout) mMiddleView.findViewById(R.id.mapview);
		mCurrentLocation=(Button) mMiddleView.findViewById(R.id.googlemaps_current_location);
		sHomePageFreezeView=(Button) mApp.findViewById(R.id.freezeview);
		sHomePageFreezeView.setVisibility(View.VISIBLE);

		mListView=(LinearLayout) mMiddleView.findViewById(R.id.shopList);
		mShopListView = (ListView) mMiddleView.findViewById(R.id.shopListView);

		// Categories header layout initialization
		mCategoriesHeader = (RelativeLayout) mSearchBar.findViewById(R.id.categories_container);
		LinearLayout mCategoriesTextLayout = (LinearLayout) mCategoriesHeader.findViewById(R.id.categoriestextlayout); 
		mCategoriesText = (TextView) mCategoriesTextLayout.findViewById(R.id.categories_textId);
		mSubCategoritesText = (TextView) mCategoriesTextLayout.findViewById(R.id.subcategories_textId);
		mCategoriesCancel = (Button) mCategoriesHeader.findViewById(R.id.categories_cancelId);
		mGoogleMap = null;

		sIsLoadMore = false;
		sStoreDetailsStart="0";
		sStoreDetailsEndLimit="20";
		sStoreDetailsCount = "0";

		// add footer view to list view
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
		/*if(mShopListView.getFooterViewsCount() == 0){
			mShopListView.addFooterView(mFooterLayout);	
		}*/

		// For Notification popup
		mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
		mNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mTabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		mNotificationCount.setOnClickListener(new ManageNotificationWindow(this,mTabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));

		mBtnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, mSearchBar, sSearchStoreName, mShrinkSearch, 0, 
				getApplicationContext(),mMenuSearch,mMenuBarContainer,/*mZpayMenuBarContainer,*/mZpayBar,sHomePageFreezeView,mCancelStoreName,mSearchBoxContainer));

		/*ClickListeners for all left items*/
		mHome.setOnClickListener(new MenuItemClickListener(sLeftMenu,ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mZpay.setOnClickListener(new MenuItemClickListener(sLeftMenu,ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mInvoiceCenter.setOnClickListener(new MenuItemClickListener(sLeftMenu,ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mGiftcards.setOnClickListener(new MenuItemClickListener(sLeftMenu,ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mManageCards.setOnClickListener(new MenuItemClickListener(sLeftMenu, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mReceipts.setOnClickListener(new MenuItemClickListener(sLeftMenu, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mMyFavourites.setOnClickListener(new MenuItemClickListener(sLeftMenu,ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mMyFriends.setOnClickListener(new MenuItemClickListener(sLeftMenu,ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mChat.setOnClickListener(new MenuItemClickListener(sLeftMenu,ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mRewards.setOnClickListener(new MenuItemClickListener(sLeftMenu,ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mSettings.setOnClickListener(new MenuItemClickListener(sLeftMenu,ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));

		/*ClickListeners for all sRightmenu items*/
		mInfo_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mMobilePay_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mGiftCards_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mDeals_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mCoupons_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mSocial_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mReviews_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mLocations_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mPhotos_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mVideos_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mContactStore_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mFavorite_RightMenu.setOnClickListener(new MenuItemClickListener(mRightMenuItems, ShopperHomePage.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		sSearchStoreName.setOnEditorActionListener(new DoneOnEditorActionListener(ShopperHomePage.this, sSearchStoreName));
		mMenuQRCode.setOnClickListener(new MenuBarQRCodeClickListener(ShopperHomePage.this,ShopperHomePage.this,mMenuBrowse,mMenuSearch,mMenuList,0));
		mMenuBrowse.setOnClickListener(new MenuBarBrowseClickListener(ShopperHomePage.this,mMenuBarBrowseText,mMenuQRCode,mMenuSearch,mMenuList));
		mMenuSearch.setOnClickListener(new MenuBarSearchClickListener(ShopperHomePage.this,mSearchBar,sSearchStoreName,mMenuList,mMenuBrowse,mMenuQRCode,0,sHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mCategoriesHeader,mCategoriesText));
		mMenuList.setOnClickListener(new MenuBarListClickListener(ShopperHomePage.this,mMiddleView,mMenuBarListText,mMenuBarListImage,mZpayList,mMenuBrowse,mMenuQRCode,mMenuSearch,0,"ShopperHomePage",mListView,mMapViewContainer));

		//Zpay click Listener Class
		mZpayQRCode.setOnClickListener(new MenuBarQRCodeClickListener(ShopperHomePage.this,ShopperHomePage.this,mMenuBrowse,mMenuSearch,mMenuList,1));
		mZpaySearch.setOnClickListener(new MenuBarSearchClickListener(ShopperHomePage.this,mSearchBar,sSearchStoreName,mMenuList,mMenuBrowse,mMenuQRCode,1,sHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mCategoriesHeader,mCategoriesText));
		mZpayList.setOnClickListener(new MenuBarListClickListener(ShopperHomePage.this,mMiddleView,mMenuBarListText,mMenuBarListImage,mZpayList,mMenuBrowse,mMenuQRCode,mMenuSearch,1,"ShopperHomePage",mListView,mMapViewContainer));

		// To show app intro pop up
		if(getIntent().hasExtra("fromlogin") || getIntent().hasExtra("fromSignUp")){  
			boolean should_show_appIntro = mPrefs.getBoolean("app_intro_skip", true);
			String user_type = mPrefs.getString("user_login_type", "");
			if((should_show_appIntro && user_type.equalsIgnoreCase("Guest")) || getIntent().hasExtra("fromSignUp")){
				showAppIntroPopUp();
			}else{
				if(mConnectionAvailabilityChecking.ConnectivityCheck(ShopperHomePage.this)){
					if(mGoogleMap == null && checkPlayServices()){
						sMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
						if(mUpdateGooglePlayServicesDialog != null && mUpdateGooglePlayServicesDialog.isShowing())
							mUpdateGooglePlayServicesDialog.dismiss();
						initilizeGoogleMap();
						getCurrentLocation();
					}
				}else{
					Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
				}
			}
		}else{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ShopperHomePage.this)){
				if(mGoogleMap == null && checkPlayServices()){
					sMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
					if(mUpdateGooglePlayServicesDialog != null && mUpdateGooglePlayServicesDialog.isShowing())
						mUpdateGooglePlayServicesDialog.dismiss();
					initilizeGoogleMap();
					getCurrentLocation();
				}
			}else{
				Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
			}
		}


		mCurrentLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// To remove map scroll listener from google map
				mGoogleMap.setOnCameraChangeListener(null);

				sStoreDetailsStart="0";
				sStoreDetailsEndLimit="20";
				sStoreDetailsCount = "0";	
				sIsLoadMore = false;
				/*if(mShopListView.getFooterViewsCount() == 0){
					mShopListView.addFooterView(mFooterLayout);	
				}*/

				if(mStoreNearThread!=null&&mStoreNearThread.getStatus()==Status.RUNNING){
					mStoreNearThread.cancel(true);
				}

				try{
					//Load Current Location while activity loading
					if(mConnectionAvailabilityChecking.ConnectivityCheck(ShopperHomePage.this)){
						if(sSearchStoreName.getVisibility()!=View.VISIBLE && mCategoriesHeader.getVisibility()!=View.VISIBLE){
							sMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
							getCurrentLocation();		// To Get Current Location
						}else if(sSearchStoreName.getVisibility()==View.VISIBLE&&sSearchStoreName.getText().toString().trim().length()>0){
							mGoogleMap.clear();
							sMapViewOnScrollViewFlag ="search";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
							boolean mCheckStoreName=false;
							for(int i=0;i<WebServiceStaticArrays.mSearchStore.size();i++){
								final Search_ClassVariables obj = (Search_ClassVariables) WebServiceStaticArrays.mSearchStore.get(i);
								if(sSearchStoreName.getText().toString().equals(obj.storeName)){
									mCheckStoreName=true;
									sStoreDetailsStart="0";
									sStoreDetailsEndLimit="20";
									sStoreDetailsCount = "0";	
									sIsLoadMore = false;
									/*if(mShopListView.getFooterViewsCount() == 0){
										mShopListView.addFooterView(mFooterLayout);	
									}*/
									LatLng lastKnownCenterLocation = new LatLng(mGoogleMap.getCameraPosition().target.latitude+0.001, mGoogleMap.getCameraPosition().target.latitude+0.001);
									mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnownCenterLocation));
									mLocationGeoCordinates =  new LatLng((Double.parseDouble(sDeviceCurrentLocationLatitude)), (Double.parseDouble(sDeviceCurrentLocationLongitude)));
									mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mLocationGeoCordinates),new CancelableCallback() {

										@Override
										public void onFinish() {
											// TODO Auto-generated method stub
											sDistance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
											StoresLocationAsynchThread storeslocationasynchthread = new StoresLocationAsynchThread(ShopperHomePage.this,mMenuSearch,mShrinkSearch,sSearchStoreName,mSearchBar,sHomePageFreezeView,mGoogleMap,mShopListView,mCurrentLocation,sDistance,"ShopperHomePage",mCancelStoreName,mSearchBoxContainer,"",sFooterLayout,"PROGRESS",true,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude);
											storeslocationasynchthread.execute(obj.storeId,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude);
										}

										@Override
										public void onCancel() {
											// TODO Auto-generated method stub

										}
									});
								}
							}
							if(!mCheckStoreName){
								Toast.makeText(getApplicationContext(), "Please Enter Valid Store Name.", Toast.LENGTH_SHORT).show();
							}
						}else if(sSearchStoreName.getVisibility()==View.VISIBLE&&sSearchStoreName.getText().toString().trim().length()==0){
							//close searchbox and disable freezeview
							String returnValue = new FreezeClickListener(sScrollView, sLeftMenu, sRightMenu,mSearchBar, sSearchStoreName, mShrinkSearch, 0,
									ShopperHomePage.this,mMenuSearch,sHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mGoogleMap,mCurrentLocation,mShopListView).closeSearchBox(TAG);
							if(returnValue.equalsIgnoreCase("success")){
								sMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
								getCurrentLocation();		// To Get Current Location
							}
						}else if(mCategoriesHeader.getVisibility() == View.VISIBLE){
							mGoogleMap.clear();
							sStoreDetailsStart="0";
							sStoreDetailsEndLimit="20";
							sStoreDetailsCount = "0";	
							sIsLoadMore = false;
							/*if(mShopListView.getFooterViewsCount() == 0){
								mShopListView.addFooterView(mFooterLayout);	
							}*/
							LatLng lastKnownCenterLocation = new LatLng(mGoogleMap.getCameraPosition().target.latitude+0.001, mGoogleMap.getCameraPosition().target.latitude+0.001);
							mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnownCenterLocation));
							mLocationGeoCordinates =  new LatLng((Double.parseDouble(sDeviceCurrentLocationLatitude)), (Double.parseDouble(sDeviceCurrentLocationLongitude)));
							mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mLocationGeoCordinates),new CancelableCallback() {

								@Override
								public void onFinish() {
									// TODO Auto-generated method stub
									storecategories(mStore_categoriesId,true,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude);
								}

								@Override
								public void onCancel() {
									// TODO Auto-generated method stub
									Log.i("animation listner google,map", "on cancelled called");
								}
							});
						}
					}else{
						Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		mLogoutImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MenuOutClass.HOMEPAGE_MENUOUT=false;
				ZPayFlag.setFlag(0);
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(ShopperHomePage.this,"FromManualLogOut").execute();
			}
		});

		mLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				if(NormalPaymentAsyncTask.mCountDownTimer!=null){
					NormalPaymentAsyncTask.mCountDownTimer.cancel();
					NormalPaymentAsyncTask.mCountDownTimer = null;
					Log.i(TAG,"Timer Stopped Successfully");
				}

				MenuOutClass.HOMEPAGE_MENUOUT=false;
				ZPayFlag.setFlag(0);
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(ShopperHomePage.this,"FromManualLogOut").execute();
			}
		});

		mStoreAdapter = new CustomStoreAdapter(ShopperHomePage.this, android.R.layout.simple_dropdown_item_1line,"home");
		sSearchStoreName.setAdapter(mStoreAdapter);

		sSearchStoreName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				CustomStoreAdapter.isSearchStringChanged = true;
				if(!sSearchStoreName.isPopupShowing()){
					Log.i("text changed", "hide");
					sSearchStoreName.dismissDropDown();	
				}else{
					Log.i("text changed", "dont hide");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				Log.i("text changed", "before text changed");
			}

			@Override
			public void afterTextChanged(Editable s) {}
		});

		mCancelStoreName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mGoogleMap.setOnCameraChangeListener(null);

				MenuBarSearchClickListener.sCLICKFLAG=true;
				//mGoogleMap.clear();
				if(sSearchStoreName.getText().toString().length()>0&&sMapViewOnScrollViewFlag.equalsIgnoreCase("search")){

					//Set menu bar background to default
					mMenuBrowse.setBackgroundResource(R.drawable.header_2);
					mMenuQRCode.setBackgroundResource(R.drawable.header_2);
					mMenuSearch.setBackgroundResource(R.drawable.header_2);
					mMenuList.setBackgroundResource(R.drawable.header_2);

					//Set MenuSearchBarClickListener CLICKFLAG to true 
					// close searchbox and its items in map and list view and reload store near current location stores and disable freezeview
					if(new FreezeClickListener(sScrollView, sLeftMenu, sRightMenu,mSearchBar, sSearchStoreName, mShrinkSearch, 0,
							ShopperHomePage.this,mMenuSearch,sHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mGoogleMap,mCurrentLocation,mShopListView).closeSearchBox(TAG).equals("success")){
						sMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
						sStoreDetailsStart="0";
						sStoreDetailsEndLimit="20";
						sStoreDetailsCount = "0";	
						sIsLoadMore = false;
						/*if(mShopListView.getFooterViewsCount() == 0){
							mShopListView.addFooterView(mFooterLayout);	
						}*/
						LatLng lastKnownCenterLocation = new LatLng(mGoogleMap.getCameraPosition().target.latitude+0.001, mGoogleMap.getCameraPosition().target.latitude+0.001);
						mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnownCenterLocation));
						getCurrentLocation();
					}
				}else{
					//close searchbox and disable freezeview
					if(new FreezeClickListener(sScrollView, sLeftMenu, sRightMenu,mSearchBar, sSearchStoreName, mShrinkSearch, 0,
							ShopperHomePage.this,mMenuSearch,sHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mGoogleMap,mCurrentLocation,mShopListView).closeSearchBox(TAG).equalsIgnoreCase("success")){
						if(sMapViewOnScrollViewFlag.equalsIgnoreCase("browse")){
							//Set selection for browse menu bar background
							mMenuBrowse.setBackgroundResource(R.drawable.footer_dark_blue_new);
							mMenuQRCode.setBackgroundResource(R.drawable.header_2);
							mMenuSearch.setBackgroundResource(R.drawable.header_2);
							mMenuList.setBackgroundResource(R.drawable.header_2);
							showCategoriesHeaderView();
						}else if(sMapViewOnScrollViewFlag.equalsIgnoreCase("qrcode")){
							//Set selection for qrcode menu bar background
							mMenuBrowse.setBackgroundResource(R.drawable.header_2);
							mMenuQRCode.setBackgroundResource(R.drawable.footer_dark_blue_new);
							mMenuSearch.setBackgroundResource(R.drawable.header_2);
							mMenuList.setBackgroundResource(R.drawable.header_2);
						}
					}
				}
			}
		});

		sSearchStoreName.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// clear  list view offset variables
				try{
					InputMethodManager softkeyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					softkeyboard.hideSoftInputFromWindow(sSearchStoreName.getWindowToken(), 0);
					sStoreDetailsStart="0";
					sStoreDetailsEndLimit="20";
					sStoreDetailsCount = "0";	
					sIsLoadMore = false;
					mGoogleMap.clear();
					sMapViewOnScrollViewFlag ="search";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
					Search_ClassVariables obj = (Search_ClassVariables) mStoreAdapter.getItem(position);
					sSearchStoreName.setThreshold(1000);
					sSearchStoreName.setText(obj.storeName);
					sSearchStoreName.setThreshold(1);
					mSearch_storeId = obj.storeId;

					sDistance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
					mGoogleMap.setOnCameraChangeListener(null);
					mStoresLocationAsynchThread = new StoresLocationAsynchThread(ShopperHomePage.this,mMenuSearch,mShrinkSearch,sSearchStoreName,mSearchBar,sHomePageFreezeView,mGoogleMap,mShopListView,mCurrentLocation,sDistance,"ShopperHomePage",mCancelStoreName,mSearchBoxContainer,"",sFooterLayout,"PROGRESS",true,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude);
					mStoresLocationAsynchThread.execute(obj.storeId,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);

				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		// Click listener for categories header text
		mCategoriesText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("categories", "click");
				contextMenuOpen(mCategoriesText, 1);
			}
		});

		mSubCategoritesText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("Subcategories", "click");
				if(!mSubCategoritesText.getText().toString().equalsIgnoreCase("")){
					contextMenuOpen(mSubCategoritesText, 2);	
				}else{
					Log.i("Subcategories", "no item found");
				}

			}
		});

		mCategoriesCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// set menu bar to default state
				mMenuBrowse.setBackgroundResource(R.drawable.header_2);
				mMenuQRCode.setBackgroundResource(R.drawable.header_2);
				mMenuSearch.setBackgroundResource(R.drawable.header_2);
				mMenuList.setBackgroundResource(R.drawable.header_2);
				mCategoriesHeader.setVisibility(View.INVISIBLE);
				Animation custom_animation = AnimationUtils.loadAnimation(ShopperHomePage.this, R.anim.slideup);
				CustomAnimationListener customAnimationListener = new CustomAnimationListener(mCategoriesHeader, "InVisible");
				custom_animation.setAnimationListener(customAnimationListener);
				mCategoriesHeader.startAnimation(custom_animation);
				sIsLoadMore = false;
				sStoreDetailsStart="0";
				sStoreDetailsEndLimit="20";
				sStoreDetailsCount = "0";
				mGoogleMap.setOnCameraChangeListener(null);
				mCategoriesText.setText("Categories"); // Set to default , checked in Oncontextmenuclose function  
				sMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
				LatLng lastKnownCenterLocation = new LatLng(mGoogleMap.getCameraPosition().target.latitude+0.001, mGoogleMap.getCameraPosition().target.latitude+0.001);
				mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnownCenterLocation));
				getCurrentLocation();
			}
		});

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	/*To get user current location*/    
	public void getCurrentLocation(){
		/*// For testing
		MapViewCenter.LoginCurrentLocationLatitude = 0.0;
		MapViewCenter.LoginCurrentLocationLongitude = 0.0;*/
		Log.i("current location","get current location");
		/*set initial map view center to check it in async task while scroll*/
		sMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
		if(MapViewCenter.LoginCurrentLocationLatitude != 0.0 && MapViewCenter.LoginCurrentLocationLongitude != 0.0){
			//If current location is successfully fetched during login means ...
			Log.i("current location source", "during login");
			//locationoverlay.disableMyLocation();
			Message msg = new Message();
			msg.obj = "currentlocation";
			Bundle mLocationdetails = new Bundle();
			mLocationdetails.putDouble("latitude", MapViewCenter.LoginCurrentLocationLatitude);
			mLocationdetails.putDouble("longitude", MapViewCenter.LoginCurrentLocationLongitude);
			msg.setData(mLocationdetails);
			handler_response.sendMessage(msg);
			MapViewCenter.LoginCurrentLocationLatitude = 0.0;
			MapViewCenter.LoginCurrentLocationLongitude = 0.0;
		}else{
			Log.i("current location source", "using play services");
			int response =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
			if(response == ConnectionResult.SUCCESS){
				//ShopperHomePage.mHomePageFreezeView.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "Please Wait...GPS fetching Current Location", Toast.LENGTH_SHORT).show();
				mLocationClient = new LocationClient(this,this,this);
				mLocationClient.connect();
			}else{
				Toast.makeText(this, "Please update google play application", Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * function to load map
	 * */
	private void initilizeGoogleMap() {
		if (mGoogleMap == null) {
			Log.i("Google map", "initializing google maps");
			mGoogleMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.zouponsHomePageMapView)).getMap();
			// To set google map to last known location
			SharedPreferences mPrefs = getSharedPreferences("UserLocationPreferences", MODE_PRIVATE);
			Double mLastKnownLatitude = Double.valueOf(mPrefs.getString("last_known_latitude", "0"));
			Double mLastKnownLongitude = Double.valueOf(mPrefs.getString("last_known_longitude", "0"));
			if(mLastKnownLatitude != 0 && mLastKnownLongitude != 0){
				Log.i("last known lati and longi", mLastKnownLatitude + " " +mLastKnownLongitude);
				LatLng mLastKnownCoordinates = new LatLng(mLastKnownLatitude, mLastKnownLongitude);
				CameraPosition myLastKnownPosition = new CameraPosition.Builder().target(mLastKnownCoordinates).zoom(13).build();
				mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(myLastKnownPosition));
			}else{
				Log.i("last known lati and longi", "nothing");
			}
			mGoogleMapUISettings = mGoogleMap.getUiSettings();
			mGoogleMapUISettings.setZoomControlsEnabled(false);
			mGoogleMapUISettings.setMyLocationButtonEnabled(false);
			mGoogleMapUISettings.setCompassEnabled(false);
			mGoogleMapUISettings.setRotateGesturesEnabled(false);
			mGoogleMap.setInfoWindowAdapter(new CustomMapViewCallout(ShopperHomePage.this));
			mGoogleMap.setOnInfoWindowClickListener(new CustomMapCalloutClickListener(ShopperHomePage.this));
			mGoogleMap.setMyLocationEnabled(true);
			mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker arg0) {
					// TODO Auto-generated method stub
					mGoogleMap.setOnCameraChangeListener(null);
					return false;
				}
			});

			// check if map is created successfully or not
			if (mGoogleMap == null) {
				Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
			}else{
				sHomePageFreezeView.setOnClickListener(new FreezeClickListener(sScrollView, sLeftMenu, sRightMenu,mSearchBar, sSearchStoreName, mShrinkSearch, 0,
						ShopperHomePage.this,mMenuSearch,sHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mGoogleMap,mCurrentLocation,mShopListView));	
			}


		}
	}

	// To show app introduction pop up dialog
	public void showAppIntroPopUp(){
		final Dialog mCustomerDetailsDialog = new Dialog(this);
		mCustomerDetailsDialog.setTitle("A Quick Tip");
		mCustomerDetailsDialog.setContentView(R.layout.app_intro_popup);
		mCustomerDetailsDialog.setCancelable(false); 
		WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
		lWindowParams.copyFrom(mCustomerDetailsDialog.getWindow().getAttributes());
		lWindowParams.width = WindowManager.LayoutParams.FILL_PARENT; 
		lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mCustomerDetailsDialog.getWindow().setAttributes(lWindowParams);
		mCustomerDetailsDialog.show();	
		Button mAppIntroSkipButton = (Button) mCustomerDetailsDialog.findViewById(R.id.app_intro_skip_button);
		CheckBox mAppIntroSkipCheckBox = (CheckBox) mCustomerDetailsDialog.findViewById(R.id.app_intro_skip_checkboxId);

		if(getIntent().hasExtra("fromSignUp")){ // To align "GotIt" button to center and hide dont show again check box
			mAppIntroSkipCheckBox.setVisibility(View.GONE);
			mCustomerDetailsDialog.findViewById(R.id.app_intro_skip_textId).setVisibility(View.GONE);
			RelativeLayout.LayoutParams mGotItButtonLayoutParams = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, getResources().getDimensionPixelSize(R.dimen.pop_up_button_width));
			mGotItButtonLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			mAppIntroSkipButton.setLayoutParams(mGotItButtonLayoutParams);
		}

		mAppIntroSkipButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCustomerDetailsDialog.dismiss();
				if(mConnectionAvailabilityChecking.ConnectivityCheck(ShopperHomePage.this)){
					if(mGoogleMap == null && checkPlayServices()){
						sMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
						if(mUpdateGooglePlayServicesDialog != null && mUpdateGooglePlayServicesDialog.isShowing())
							mUpdateGooglePlayServicesDialog.dismiss();
						initilizeGoogleMap();
						getCurrentLocation();
					}
				}else{
					Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
				}
			}
		});

		mAppIntroSkipCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				SharedPreferences mPrefs = getSharedPreferences("UserNamePrefences", MODE_PRIVATE);
				if(isChecked){
					Editor editor = mPrefs.edit();
					editor.putBoolean("app_intro_skip", false);
					editor.commit();	
				}else{
					Editor editor = mPrefs.edit();
					editor.putBoolean("app_intro_skip",true);
					editor.commit();
				}
			}
		});

	}


	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's width.
	 */
	class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View leftMenu,rightMenu;
		ViewGroup searchBar;
		AnimationSet shrinkSearch;
		int homepageFlag;
		LinearLayout menubarsearch,menubarcontainer;
		Context ClickListenerForScrollingContext;
		AutoCompleteTextView mSearchBox;
		Button mFreezeHomePage;
		LinearLayout /*menubar_zpaycontainer,*/mZpayBar;
		InputMethodManager softkeyboardevent;
		Button mClearStoreName;
		RelativeLayout mSearchBoxContainer;

		/**
		 * Menu must NOT be out/shown to start with.
		 */
		public ClickListenerForScrolling(HorizontalScrollView scrollView, View leftMenu, View rightMenu,ViewGroup searchbar,AutoCompleteTextView searchbox,AnimationSet shrinksearch,int homeflag,
				Context context, LinearLayout menusearch, LinearLayout menucontainer, /*LinearLayout zpaymenubarcontainer,*/LinearLayout zpaybar,Button freezehomepage,Button clearstorename,RelativeLayout searchboxcontainer) {
			super();
			this.scrollView = scrollView;
			this.leftMenu = leftMenu;
			this.rightMenu = rightMenu;
			this.searchBar = searchbar;
			this.mSearchBox=searchbox;
			this.shrinkSearch=shrinksearch;
			this.homepageFlag=homeflag;
			this.ClickListenerForScrollingContext=context;
			this.menubarsearch=menusearch;
			this.menubarcontainer=menucontainer;
			this.mZpayBar=zpaybar;
			this.mFreezeHomePage=freezehomepage;
			this.mClearStoreName=clearstorename;
			this.mSearchBoxContainer=searchboxcontainer;
			//this.mMapView=mapview;
		} 

		public ClickListenerForScrolling(){}

		@Override
		public void onClick(View v) {

			sLeftMenuScrollView.fullScroll(View.FOCUS_UP);
			sLeftMenuScrollView.pageScroll(View.FOCUS_UP);

			if(MenuBarSearchClickListener.sCLICKFLAG==true){
				softkeyboardevent = (InputMethodManager)ClickListenerForScrollingContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				softkeyboardevent.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);
			}

			// To enable/disable left menu items based upon user login type [Customer or Guest]
			setLeftMenuItemStatus(ClickListenerForScrollingContext);

			int menuWidth = leftMenu.getMeasuredWidth();

			switch(this.homepageFlag){
			case 0:
				if (!MenuOutClass.HOMEPAGE_MENUOUT) {
					if(MenuBarSearchClickListener.sCLICKFLAG==true){
						mSearchBox.clearFocus();
						mSearchBox.setFocusable(false);
						ShopperHomePage.searchcloseflag=1;
						scrollView.smoothScrollTo(sLeft, 0);
					}else{
						ShopperHomePage.searchcloseflag=1;
						scrollView.smoothScrollTo(sLeft, 0);
					}
					mFreezeHomePage.setVisibility(View.VISIBLE);
				}else {
					// Scroll to menuWidth so menu isn't on screen.
					int left = menuWidth;
					scrollView.smoothScrollTo(left, 0);
					mFreezeHomePage.setVisibility(View.GONE);
				}
				MenuOutClass.HOMEPAGE_MENUOUT = !MenuOutClass.HOMEPAGE_MENUOUT;
				break;
			case 1:	// case when click home
				// Scroll to menuWidth so menu isn't on screen when click on home button in main menu.
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				ZPayFlag.setFlag(0);	//set zpay flag false
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);
				MenuOutClass.HOMEPAGE_MENUOUT=false;
				this.menubarcontainer.setVisibility(View.VISIBLE);
				mFreezeHomePage.setVisibility(View.GONE);

				break;
			case 2:	// case when click zpay
				// Scroll to menuWidth so menu isn't on screen when click on ZPay button in main menu.
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				ZPayFlag.setFlag(1);	//set zpay flag true
				int left_new = menuWidth;
				scrollView.smoothScrollTo(left_new, 0);
				MenuOutClass.HOMEPAGE_MENUOUT=false;
				this.menubarcontainer.setVisibility(View.GONE);
				this.mSearchBox.setVisibility(View.GONE);
				this.mClearStoreName.setVisibility(View.GONE);
				this.searchBar.setVisibility(View.VISIBLE);
				this.mZpayBar.setVisibility(View.VISIBLE);
				mFreezeHomePage.setVisibility(View.GONE);
				mMapViewContainer.setVisibility(View.GONE);
				mMapViewContainer.clearFocus();
				mMapViewContainer.clearAnimation();
				mListView.setVisibility(View.VISIBLE);
				mListView.bringToFront();
				mListView.requestFocus();
				mListView.clearAnimation();
				break;
			case 3:	// case when open right menu has called while clicking on mapview store callout
				if (!MenuOutClass.HOMEPAGE_MENUOUT) {

					// Scroll to 0 to reveal menu
					int right = menuWidth+menuWidth;

					if(MenuBarSearchClickListener.sCLICKFLAG==true){

						if(ZPayFlag.getFlag()!=1){
							ScaleAnimation shrink = new ScaleAnimation(
									1f, 1f, 
									1f, 0f,
									Animation.RELATIVE_TO_PARENT, 100,
									Animation.RELATIVE_TO_PARENT, 0);
							shrink.setStartOffset(100);
							shrink.setDuration(500);
							shrinkSearch.addAnimation(shrink);
							shrinkSearch.setFillAfter(true);
							shrinkSearch.setInterpolator(new AccelerateInterpolator(1.0f));

							this.mSearchBox.startAnimation(shrinkSearch);
							this.mSearchBox.postDelayed(new Runnable() {

								@Override
								public void run() {
									mSearchBox.setText("");
									mSearchBox.setVisibility(View.GONE);
									mClearStoreName.setVisibility(View.GONE);
									mSearchBoxContainer.setVisibility(View.GONE);
									searchBar.setVisibility(View.GONE);
								}
							}, 350);
						}else{
							ScaleAnimation shrink = new ScaleAnimation(
									1f, 1f, 
									1f, 0f,
									Animation.RELATIVE_TO_PARENT, 100,
									Animation.RELATIVE_TO_PARENT, 0);
							shrink.setStartOffset(100);
							shrink.setDuration(500);
							shrinkSearch.addAnimation(shrink);
							shrinkSearch.setFillAfter(true);
							shrinkSearch.setInterpolator(new AccelerateInterpolator(1.0f));

							this.mSearchBox.startAnimation(shrinkSearch);
							this.mSearchBox.postDelayed(new Runnable() {

								@Override
								public void run() {
									mSearchBox.setText("");
									mSearchBox.setVisibility(View.GONE);
									mClearStoreName.setVisibility(View.GONE);
								}
							}, 350);            				
						}
						MenuBarSearchClickListener.sCLICKFLAG=false;
						searchcloseflag=1;
					}else{

						searchcloseflag=1;
					}
					if(searchcloseflag==1){
						scrollView.smoothScrollTo(right, 0);
					}
					mFreezeHomePage.setVisibility(View.VISIBLE);
				}else {

					// Scroll to menuWidth so menu isn't on screen.

					int right = menuWidth;
					scrollView.smoothScrollTo(right, 0);

					mFreezeHomePage.setVisibility(View.GONE);
				}
				MenuOutClass.HOMEPAGE_MENUOUT = !MenuOutClass.HOMEPAGE_MENUOUT;
				break;
			}
			// Ensure menu is visible
			leftMenu.setVisibility(View.VISIBLE);
		}
	}

	// To enable or diasble left menu items based upon login type[Guest or Zoupons customer login] 
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
			mManageCards.setEnabled(false);
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
			System.out.println("btnWidth=" + btnWidth); 
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE: //1
			if(resultCode==RESULT_OK){

				mMenuQRCode.setBackgroundResource(R.drawable.header_2);

				ShopperHomePage.sMapViewOnScrollViewFlag="qrcode";
				IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
				if (scanResult == null) {
					return;
				}
				final String result = scanResult.getContents();
				if (result != null) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							String rtn="";
							mGoogleMap.clear();	//To hide loaded call outs in mapview

							// clear  list view offset variables
							sStoreDetailsStart="0";
							sStoreDetailsEndLimit="20";
							sStoreDetailsCount = "0";

							if(sSearchStoreName.getVisibility()==View.VISIBLE){
								//Set MenuSearchBarClickListener CLICKFLAG to true 
								MenuBarSearchClickListener.sCLICKFLAG=true;

								//close searchbox and disable freezeview
								rtn = new FreezeClickListener(sScrollView, sLeftMenu, sRightMenu,mSearchBar, sSearchStoreName, mShrinkSearch, 0,
										ShopperHomePage.this,mMenuSearch,sHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mGoogleMap,mCurrentLocation,mShopListView).closeSearchBox(TAG);
								if(rtn.equals("success")){
									StoresQRCodeAsynchThread storeqrcode = new StoresQRCodeAsynchThread(ShopperHomePage.this, mGoogleMap, mShopListView,getMapViewRadius(mGoogleMap.getCameraPosition().target),"ShopperHomePage","ShopperHomePage",sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude);
									storeqrcode.execute(result);
								}
							}else if(mCategoriesHeader.getVisibility()==View.VISIBLE){

								mCategoriesHeader.setVisibility(View.INVISIBLE);
								Animation custom_animation = AnimationUtils.loadAnimation(ShopperHomePage.this, R.anim.slideup);
								CustomAnimationListener customAnimationListener = new CustomAnimationListener(mCategoriesHeader, "InVisible");
								custom_animation.setAnimationListener(customAnimationListener);
								mCategoriesHeader.startAnimation(custom_animation);
								mCategoriesText.setText("Categories");

								StoresQRCodeAsynchThread storeqrcode = new StoresQRCodeAsynchThread(ShopperHomePage.this, mGoogleMap, mShopListView,getMapViewRadius(mGoogleMap.getCameraPosition().target),"ShopperHomePage","ShopperHomePage",sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude);
								storeqrcode.execute(result);
							}else{
								StoresQRCodeAsynchThread storeqrcode = new StoresQRCodeAsynchThread(ShopperHomePage.this, mGoogleMap,  mShopListView,getMapViewRadius(mGoogleMap.getCameraPosition().target),"ShopperHomePage","ShopperHomePage",sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude);
								storeqrcode.execute(result);
							}
						}
					});
				}
			}else if(resultCode==RESULT_CANCELED){
				if(sMapViewOnScrollViewFlag.equalsIgnoreCase("browse")){
					//Set selection for browse menu bar background
					mMenuBrowse.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mMenuQRCode.setBackgroundResource(R.drawable.header_2);
					mMenuSearch.setBackgroundResource(R.drawable.header_2);
					mMenuList.setBackgroundResource(R.drawable.header_2);
				}else if(sMapViewOnScrollViewFlag.equalsIgnoreCase("search")){
					//Set selection for search menu bar background
					mMenuBrowse.setBackgroundResource(R.drawable.header_2);
					mMenuQRCode.setBackgroundResource(R.drawable.header_2);
					mMenuSearch.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mMenuList.setBackgroundResource(R.drawable.header_2);
				}else {
					mMenuBrowse.setBackgroundResource(R.drawable.header_2);
					mMenuQRCode.setBackgroundResource(R.drawable.header_2);
					mMenuSearch.setBackgroundResource(R.drawable.header_2);
					mMenuList.setBackgroundResource(R.drawable.header_2);
				}
			}
			break;
		case REQUEST_CODE_RECOVER_PLAY_SERVICES : 
			if(checkPlayServices()){
				if(mUpdateGooglePlayServicesDialog != null && mUpdateGooglePlayServicesDialog.isShowing())
					mUpdateGooglePlayServicesDialog.dismiss();
				sMapViewOnScrollViewFlag ="currentlocation";
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch(item.getGroupId()){
		case 0:
			for(int i=0;i<WebServiceStaticArrays.mCategoriesList.size();i++){
				final Categories_ClassVariables obj = (Categories_ClassVariables) WebServiceStaticArrays.mCategoriesList.get(i);
				if(item.getTitle().equals(obj.categoryName)){
					// To set selected categories in header view........
					mSelectedcategories = obj.categoryName;
					mIsSubCategoriesSelected = false;
					if(Integer.parseInt(obj.subcategoryCount)>0){
						//Here we have to call subcategories webservice to load sub categories
						mProgressDialog.show();
						final Thread syncThread = new Thread(new Runnable() {

							@Override
							public void run() {
								Bundle bundle = new Bundle();
								String mGetResponse=null;
								Message msg_response = new Message();

								try{
									if(mConnectionAvailabilityChecking.ConnectivityCheck(ShopperHomePage.this)){
										mGetResponse=mZouponswebservice.subcategories(obj.id);	
										if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
											String mParsingResponse=mParsingclass.parseSubCategories(mGetResponse);
											if(mParsingResponse.equalsIgnoreCase("success")){
												for(int i=0;i<WebServiceStaticArrays.mSubCategoriesList.size();i++){
													SubCategories_ClassVariables parsedobjectvalues = (SubCategories_ClassVariables) WebServiceStaticArrays.mSubCategoriesList.get(i);
													if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("No Categories Available")*/){
														mMessageFlag=true;
														updateHandler(bundle, msg_response, "No Categories Available","2");	//send update to handlera
													}
												}
												if(!mMessageFlag){
													updateHandler(bundle, msg_response, mParsingResponse,"2");	//send update to handler
												}
											}else if(mParsingResponse.equalsIgnoreCase("failure")){
												updateHandler(bundle, msg_response, mParsingResponse,"2");	//send update to handler
												Log.i(TAG,"Error");
											}else if(mParsingResponse.equalsIgnoreCase("norecords")){
												updateHandler(bundle, msg_response, mParsingResponse,"2");	//send update to handler
												Log.i(TAG,"No Records");
											}
										}else{
											updateHandler(bundle, msg_response, mGetResponse,"2");	//send update to handler about emailid validation response
										}
									}else{
										Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
									}
								}catch(Exception e){
									Log.i(TAG,"Thread Error");
									e.printStackTrace();
								}
								//mProgressDialog.setProgress(100);
								mProgressDialog.dismiss();
							}
						});syncThread.start();
					}else{
						mSelectedSubCategories="";
						//Here we have to call storecategories webservice to load store categories
						mStore_categoriesId = obj.id;
						sMapViewOnScrollViewFlag ="browse";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
						CurrentLocation.CurrentLocation_Latitude=String.valueOf(mGoogleMap.getCameraPosition().target.latitude);
						CurrentLocation.CurrentLocation_Longitude=String.valueOf(mGoogleMap.getCameraPosition().target.longitude);
						storecategories(obj.id,true,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);

					}
				}
			}
			return true;
		case 1:
			mIsSubCategoriesSelected = true;
			// Caching sub categories list for displaying it from header view
			mSubcategoriesData = new ArrayList<Object>(WebServiceStaticArrays.mSubCategoriesList.size());
			for(int i=0;i<WebServiceStaticArrays.mSubCategoriesList.size();i++){
				final SubCategories_ClassVariables obj = (SubCategories_ClassVariables) WebServiceStaticArrays.mSubCategoriesList.get(i);
				if(item.getTitle().equals(obj.categoryName)){
					// To set selected subcategories in header view........
					mSelectedSubCategories = obj.categoryName;
					//Here we have to call storecategories webservice to load store categories
					mStore_categoriesId = obj.id;
					sMapViewOnScrollViewFlag ="browse";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
					CurrentLocation.CurrentLocation_Latitude=String.valueOf(mGoogleMap.getCameraPosition().target.latitude);
					CurrentLocation.CurrentLocation_Longitude=String.valueOf(mGoogleMap.getCameraPosition().target.longitude);
					storecategories(obj.id,true,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
				}
				mSubcategoriesData.add(obj);
			}
			return true;
		case 2:
			return true;
		default :
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if(mCategoriesViewFlag==1){				// Load Categories Menu Items
			Log.i(TAG,"Categories");
			menu.setHeaderTitle("Categories");
			for(int i=0;i<WebServiceStaticArrays.mCategoriesList.size();i++){
				Categories_ClassVariables obj = (Categories_ClassVariables) WebServiceStaticArrays.mCategoriesList.get(i);
				menu.add(0, v.getId(), 0, obj.categoryName);
			}
		}else if(mCategoriesViewFlag==2){		// Load SubCategories Menu Items
			Log.i(TAG,"SubCategories");
			menu.setHeaderTitle("Sub Categories");
			mSubCategoriesMenu = menu;
			for(int i=0;i<WebServiceStaticArrays.mSubCategoriesList.size();i++){
				SubCategories_ClassVariables obj = (SubCategories_ClassVariables) WebServiceStaticArrays.mSubCategoriesList.get(i);
				menu.add(1, v.getId(), 0, obj.categoryName);
			}
		}else if(mCategoriesViewFlag==3){		// Load StoreCategories Menu Items
			Log.i(TAG,"StoreCategories");
			menu.setHeaderTitle("Store Categories");
			for(int i=0;i<WebServiceStaticArrays.mStoreCategoriesList.size();i++){
				StoreCategories_ClassVariables obj = (StoreCategories_ClassVariables) WebServiceStaticArrays.mStoreCategoriesList.get(i);
				menu.add(2, v.getId(), 0, obj.storeName);
			}
		}

	}

	@Override
	public void onContextMenuClosed(Menu menu) {
		super.onContextMenuClosed(menu);
		if(mCategoriesHeader.getVisibility() != View.VISIBLE)
			 mMenuBrowse.setBackgroundResource(R.drawable.header_2);
		
		if(menu.equals(mSubCategoriesMenu)){		

			if(!mCategoriesText.getText().toString().equalsIgnoreCase("Categories") && !mIsSubCategoriesSelected){
				Log.i("categories", "inside set text");
				mSelectedcategories = mCategoriesText.getText().toString();	
				mIsSubCategoriesSelected = false;
			}
			if(mSubcategoriesData!=null){
				WebServiceStaticArrays.mSubCategoriesList.clear();
				for(int i=0;i<mSubcategoriesData.size();i++){
					SubCategories_ClassVariables sub_category_details = (SubCategories_ClassVariables) mSubcategoriesData.get(i);
					WebServiceStaticArrays.mSubCategoriesList.add(sub_category_details);
				}
			}
		}

	}

	// To register and open context menu 
	public void contextMenuOpen(View sender,int viewflag){
		mCategoriesViewFlag=viewflag;
		sender.setLongClickable(false);
		registerForContextMenu(sender);
		openContextMenu(sender);

		// clearing values for browse 
		sStoreDetailsStart="0";
		sStoreDetailsEndLimit="20";
		sStoreDetailsCount = "0";	
		sIsLoadMore = false;
	}

	// To send message to handler to update UI elements
	private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse,String categories){
		bundle.putString("returnResponse", mGetResponse);
		bundle.putString("categoriesflag", categories);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}

	// Handler class to update UI items
	Handler handler_response = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.obj!=null && msg.obj.toString().equalsIgnoreCase("currentlocation")){
				sMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
				if(msg.getData() != null){
					mLatitude=msg.getData().getDouble("latitude");
					mLongitude=msg.getData().getDouble("longitude");	
				}

				CurrentLocation.CurrentLocation_Latitude=String.valueOf(mLatitude);
				CurrentLocation.CurrentLocation_Longitude=String.valueOf(mLongitude);

				/*CurrentLocation.CurrentLocation_Latitude="33.5319";
				CurrentLocation.CurrentLocation_Longitude="-117.703";*/

				sDeviceCurrentLocationLatitude=CurrentLocation.CurrentLocation_Latitude;
				sDeviceCurrentLocationLongitude=CurrentLocation.CurrentLocation_Longitude;

				if(mMapViewContainer.getVisibility()==View.VISIBLE){
					Log.i("Load stores", "called for mapview visible");
					mLocationGeoCordinates = new LatLng(Double.parseDouble(CurrentLocation.CurrentLocation_Latitude), Double.parseDouble(CurrentLocation.CurrentLocation_Longitude));
					CameraPosition myCurentLocationPosition = new CameraPosition.Builder().target(mLocationGeoCordinates).zoom(13).build();
					mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(myCurentLocationPosition),new CancelableCallback() {

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							MapViewCenter.mMapViewCenter_GeoPoint = mGoogleMap.getCameraPosition().target;
							mGoogleMap.clear();
							sDistance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
							mBeforScrollCenter=mGoogleMap.getCameraPosition().target;
							mCurrentZoom = mGoogleMap.getCameraPosition().zoom;
							if(mStoreNearThread!=null){
								if(mStoreNearThread.getStatus()==AsyncTask.Status.RUNNING){
									mStoreNearThread.cancel(true);
									mStoreNearThread = new StoreNearCurrentLocationAsyncThread(ShopperHomePage.this,mGoogleMap,mShopListView,mCurrentLocation,sDistance,"ShopperHomePage",sFooterLayout,"PROGRESS",TAG,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude,true);
									if(Build.VERSION.SDK_INT >= 11)
										mStoreNearThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
									else
										mStoreNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
								}else{
									mStoreNearThread = new StoreNearCurrentLocationAsyncThread(ShopperHomePage.this,mGoogleMap,mShopListView,mCurrentLocation,sDistance,"ShopperHomePage",sFooterLayout,"PROGRESS",TAG,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude,true);
									if(Build.VERSION.SDK_INT >= 11)
										mStoreNearThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
									else
										mStoreNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
								}	
							}else{
								mStoreNearThread = new StoreNearCurrentLocationAsyncThread(ShopperHomePage.this,mGoogleMap,mShopListView,mCurrentLocation,sDistance,"ShopperHomePage",sFooterLayout,"PROGRESS",TAG,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude,true);
								if(Build.VERSION.SDK_INT >= 11)
									mStoreNearThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
								else
									mStoreNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
							}
						}

						@Override
						public void onCancel() {
							// TODO Auto-generated method stub
						}
					});
				}else{
					Log.i("Load stores", "called for mapview not visible");
					mLocationGeoCordinates = new LatLng(Double.parseDouble(sDeviceCurrentLocationLatitude), Double.parseDouble(sDeviceCurrentLocationLongitude));
					CameraPosition myPosition = new CameraPosition.Builder().target(mLocationGeoCordinates).zoom(13).build();
					mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
					sDistance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
					if(sDistance == 0)
						sDistance = 0.0525;
					mBeforScrollCenter=myPosition.target;
					mCurrentZoom = mGoogleMap.getCameraPosition().zoom;
					if(mStoreNearThread!=null){
						if(mStoreNearThread.getStatus()==AsyncTask.Status.RUNNING){
							mStoreNearThread.cancel(true);
							mStoreNearThread = new StoreNearCurrentLocationAsyncThread(ShopperHomePage.this,mGoogleMap,mShopListView,mCurrentLocation,sDistance,"ShopperHomePage",sFooterLayout,"PROGRESS",TAG,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude,true);
							if(Build.VERSION.SDK_INT >= 11)
								mStoreNearThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
							else
								mStoreNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
						}else{
							mStoreNearThread = new StoreNearCurrentLocationAsyncThread(ShopperHomePage.this,mGoogleMap,mShopListView,mCurrentLocation,sDistance,"ShopperHomePage",sFooterLayout,"PROGRESS",TAG,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude,true);
							if(Build.VERSION.SDK_INT >= 11)
								mStoreNearThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
							else
								mStoreNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
						}
					}else{
						mStoreNearThread = new StoreNearCurrentLocationAsyncThread(ShopperHomePage.this,mGoogleMap,mShopListView,mCurrentLocation,sDistance,"ShopperHomePage",sFooterLayout,"PROGRESS",TAG,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude,true);
						if(Build.VERSION.SDK_INT >= 11)
							mStoreNearThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
						else
							mStoreNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
					}
				}
			}else{
				Bundle b = msg.getData();
				String key = b.getString("returnResponse");
				String CategoryFlag = b.getString("categoriesflag");
				sIsLoadMore = false;

				mCurrentLocation.setEnabled(true);

				if(key.trim().equals("success") ){
					Log.i(TAG,"Response Get Successfully");
					if(CategoryFlag.equals("2")){
						mMenuBrowse.setBackgroundResource(R.drawable.footer_dark_blue_new); // To show Filter menu as Highlighted
						contextMenuOpen(mMenuBrowse,Integer.parseInt(CategoryFlag));
					}else {	
						// for listview offset
						/*if(mFooterLayout != null && mShopListView.getFooterViewsCount() == 0){
							mShopListView.addFooterView(mFooterLayout);
						}*/
						mGoogleMap.clear();

						showCategoriesHeaderView();
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								WebServiceStaticArrays.mStoresLocator.clear();
								for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
									StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(ShopperHomePage.sStoreDetailsStart)+i,mStoreinfo);
								}	

							}
						});

						if(ShopperHomePage.sStoreDetailsStart.equalsIgnoreCase("0")){
							MenuUtilityClass.shopListView(ShopperHomePage.this, mShopListView, "currentlocationnearstore","ShopperHomePage","FromCategories",false,TAG);	//false for adding fresh adapter
						}else{
							MenuUtilityClass.shopListView(ShopperHomePage.this, mShopListView, "currentlocationnearstore","ShopperHomePage","FromCategories",true,TAG);	//true refreshing adapter
						}
						if(sFooterLayout != null && mShopListView.getFooterViewsCount() !=0 &&mShopListView.getAdapter() != null){
							Log.i(TAG, "Remove Footer View From shop List");
							mShopListView.removeFooterView(sFooterLayout);
						}

						ArrayList<Double> minDistance = new ArrayList<Double>();
						minDistance.clear();
						for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
							//Here we have to load store details from store_categories but that sevice does't have lat&lon so, for temporary we have load stores from storenearcurrentlocation(StoreLocator_ClassVariables)
							StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
							if(obj.storeCoordinates!=null){
								mStoreCount=mStoreCount+1;
								Log.i("Adding marker", mStoreCount+" ");
								MarkerOptions mGoogleMapMarkerOptions =  new MarkerOptions();
								mGoogleMapMarkerOptions.position(obj.storeCoordinates);
								mGoogleMapMarkerOptions.title(obj.storeName);
								// Adding position to marker to differentiate while tapping marker
								mGoogleMapMarkerOptions.snippet(String.valueOf(i)+",,"+obj.addressLine1);
								// for differentiating preffered and un preferred stores
								if(obj.rightmenu_pointofsale_flag.equalsIgnoreCase("yes")){
									BitmapDescriptor marker_icon= BitmapDescriptorFactory.fromResource(R.drawable.ant3);
									mGoogleMapMarkerOptions.icon(marker_icon);
								}else{
									BitmapDescriptor marker_icon= BitmapDescriptorFactory.fromResource(R.drawable.dot);
									mGoogleMapMarkerOptions.icon(marker_icon);
								}
								mGoogleMap.addMarker(mGoogleMapMarkerOptions);
								minDistance.add(Double.parseDouble(obj.deviceDistance));
							}else{
								Log.i(TAG,"No GeoPoint StoreName: "+obj.storeName);
							}
						}

						//To get least distance of store geopoint from current location to animate
						if(minDistance.size()>0&&minDistance!=null){
							Object object = Collections.min(minDistance);

							for(int j=0;j<WebServiceStaticArrays.mStoresLocator.size();j++){
								StoreLocator_ClassVariables obj1 = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(j);
								if(obj1.distance.equalsIgnoreCase(String.valueOf(object))){
									//***********************************
									//gp=obj1.geoPoint;
									//***********************************
									//Log.i(TAG,"Min GeoPoint: "+obj1.geoPoint);
								}
							}
						}

						Log.i(TAG,"StoreCount: "+mStoreCount);
						if(mStoreCount>1){
							//here we have to calculate and get nearest store from our current location to shown it in center of the mapview

							/***************************
							 * MapOverlays.mapOverlays.clear();
				    			MapOverlays.mapOverlays.add(itemizedOverlay);
							 * 
							 */

							//mMapView.invalidate();
						}else if(mStoreCount==1){
							/***************************
							 * MapOverlays.mapOverlays.clear();
				    			MapOverlays.mapOverlays.add(itemizedOverlay);
							 * 
							 */
							//mMapView.invalidate();
						}else if(mStoreCount==0){
							//MapOverlays.mapOverlays.clear();
							//mMapView.invalidate();
						}
						if(Integer.parseInt(ShopperHomePage.sStoreDetailsCount)>20){
							sStoreDetailsStart = sStoreDetailsEndLimit;
							sStoreDetailsEndLimit = String.valueOf(Integer.parseInt(sStoreDetailsEndLimit)+20);
						}
						//Clone original storelocator arraylist to dummy store locator arraylist to fix the issue while tapping callout
						WebServiceStaticArrays.mDummyStoresLocator.clear();
						for(StoreLocator_ClassVariables obj : WebServiceStaticArrays.mStoresLocator){
							try {
								WebServiceStaticArrays.mDummyStoresLocator.add(obj.clone());
							} catch (CloneNotSupportedException e) {
								e.printStackTrace();
							}
						}
					}
				}else if(key.trim().equals("No Categories Available")){
					Toast.makeText(ShopperHomePage.this, "No Categories Available", Toast.LENGTH_SHORT).show();
				}else if(key.trim().equals("No stores Available")){
					mMenuBrowse.setBackgroundResource(R.drawable.footer_dark_blue_new); // To show Filter menu as Highlighted
					mGoogleMap.clear();
					mSearchBar.setVisibility(View.VISIBLE);
					mCategoriesHeader.setVisibility(View.INVISIBLE);
					Animation custom_animation = AnimationUtils.loadAnimation(ShopperHomePage.this, R.anim.search_in);
					CustomAnimationListener customAnimationListener = new CustomAnimationListener(mCategoriesHeader, "Visible");
					custom_animation.setAnimationListener(customAnimationListener);
					mCategoriesHeader.startAnimation(custom_animation);
					mCategoriesText.setText(mSelectedcategories);
					mSubCategoritesText.setText(mSelectedSubCategories);

					Toast.makeText(ShopperHomePage.this, "No stores Available", Toast.LENGTH_SHORT).show();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							WebServiceStaticArrays.mStoresLocator.clear();
							for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
								StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
								WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(ShopperHomePage.sStoreDetailsStart)+i,mStoreinfo);
							}	

						}
					});

					if(ShopperHomePage.sStoreDetailsStart.equalsIgnoreCase("0")){
						MenuUtilityClass.shopListView(ShopperHomePage.this, mShopListView, "currentlocationnearstore","ShopperHomePage","FromCategories",false,TAG);	//false for adding fresh adapter
					}else{
						MenuUtilityClass.shopListView(ShopperHomePage.this, mShopListView, "currentlocationnearstore","ShopperHomePage","FromCategories",true,TAG);	//true refreshing adapter
					}
					if(sFooterLayout != null && mShopListView.getFooterViewsCount() !=0 &&mShopListView.getAdapter() != null){
						Log.i(TAG, "Remove Footer View From shop List");
						mShopListView.removeFooterView(sFooterLayout);
					}
				}else if(key.trim().equals("failure")){
					Toast.makeText(ShopperHomePage.this, "Response Error", Toast.LENGTH_SHORT).show();
				}else if(key.trim().equals("noresponse")||key.trim().equals("norecords") ){
					Toast.makeText(ShopperHomePage.this, "No Data Available", Toast.LENGTH_SHORT).show();
				}	
			}
		}
	};

	// Funtion to show alert pop up with respective message
	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(ShopperHomePage.this);
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

	// To load categories and subcategories 
	private void storecategories(final String id,boolean showprogress,final String latitude,final String longitude){

		sIsLoadMore = true;
		if(sStoreDetailsCount.equalsIgnoreCase("0")&&showprogress==true){
			mProgressDialog.show();	
		}

		//Check whether search box is open or not
		if(sSearchStoreName.getVisibility()==View.VISIBLE){
			MenuBarSearchClickListener.sCLICKFLAG=true;
			new FreezeClickListener(sScrollView, sLeftMenu, sRightMenu,mSearchBar, sSearchStoreName, mShrinkSearch, 0,
					ShopperHomePage.this,mMenuSearch,sHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mGoogleMap,mCurrentLocation,mShopListView).closeSearchBox(TAG);
		}

		final Thread syncThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Bundle bundle = new Bundle();
				String mGetResponse=null;
				Message msg_response = new Message();
				try{
					if(mConnectionAvailabilityChecking.ConnectivityCheck(ShopperHomePage.this)){
						if(ShopperHomePage.sStoreDetailsStart.equalsIgnoreCase("0")){
							WebServiceStaticArrays.mStoresLocator.clear();
							//distance = getMapViewRadius(mMapView.getMapCenter());
							mGetResponse=mZouponswebservice.store_locator(latitude, longitude, "", id, "",sDistance,"",sStoreDetailsStart,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude);	//new	
						}else{
							mGetResponse=mZouponswebservice.store_locator(latitude, longitude, "", id, "",sDistance,"",sStoreDetailsStart,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude);	//new
						}

						if(!Thread.currentThread().isInterrupted()){
							if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
								String mParsingResponse=mParsingclass.parseStore_Locator(mGetResponse,"ShopperHomePage"); 	//new
								Log.i(TAG,"mParsingResponse: "+mParsingResponse);
								if(mParsingResponse.equalsIgnoreCase("success")){
									for(int i=0;i<WebServiceStaticArrays.mOffsetStoreDetails.size();i++){
										StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
										if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("No Stores Available")*/){
											mMessageFlag1=true;
										}else{
											mMessageFlag1=false;
										}
									}
									if(!mMessageFlag1){
										updateHandler(bundle, msg_response, mParsingResponse, "3");	//send update to handler
									}else{
										updateHandler(bundle, msg_response, "No stores Available", "3");	//send update to handlera
									}
								}else if(mParsingResponse.equalsIgnoreCase("failure")){
									updateHandler(bundle, msg_response, mParsingResponse, "3");	//send update to handler
									Log.i(TAG,"Error");
								}else if(mParsingResponse.equalsIgnoreCase("norecords")){
									updateHandler(bundle, msg_response, mParsingResponse, "3");	//send update to handler
									Log.i(TAG,"No Records");
								}
							}else{
								updateHandler(bundle, msg_response, mGetResponse, "3");	//send update to handler about emailid validation response
							}
						}
					}else{
						Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
					}
				}catch(Exception e){
					Log.i(TAG,"Thread Error");
					e.printStackTrace();
				}
				mProgressDialog.dismiss();
			}
		});syncThread.start();
	}

	// To get distance between center and north west coordinates in google map
	public double getMapViewRadius(LatLng center){
		android.location.Location mCurrentMapViewCenter = new android.location.Location(LocationManager.GPS_PROVIDER);
		mCurrentMapViewCenter.setLatitude(center.latitude);
		mCurrentMapViewCenter.setLongitude(center.longitude);

		LatLng mNorthWestCoordinates= getMapViewNorthWestGeoPoint();

		//To get NorthWest from the mapview center
		android.location.Location mCurrentMapViewNorthWestEdge = new android.location.Location(LocationManager.GPS_PROVIDER);
		mCurrentMapViewNorthWestEdge.setLatitude(mNorthWestCoordinates.latitude);
		mCurrentMapViewNorthWestEdge.setLongitude(mNorthWestCoordinates.longitude);
		double mDistanceBtwnCenterAndNorthWest = center.longitude - mNorthWestCoordinates.longitude;

		/*float mDistanceMetres = mCurrentMapViewCenter.distanceTo(mCurrentMapViewNorthWestEdge);
		double mDistanceBtwnCenterAndNorthWest = mDistanceMetres*0.000621371;
		Log.i(TAG,"Scrolled Distance: "+mDistanceBtwnCenterAndNorthWest);*/
		return mDistanceBtwnCenterAndNorthWest;
	}

	// To get map scrolled distance 
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
		//int mDistance =(int) mDistanceMiles;

		return mDistanceMiles;
	}


	private LatLng getMapViewNorthWestGeoPoint() {
		LatLng mNorthwestCoordinates = null;
		try{
			Projection projection = mGoogleMap.getProjection();
			int y = 1; 
			int x = 1;
			//mGeoPoint = projection.fromPixels(x, y);
			Point mPoint = new Point(x, y);
			mNorthwestCoordinates = projection.fromScreenLocation(mPoint);
		}catch(Exception e){
			e.printStackTrace();
		}
		return mNorthwestCoordinates;
	}

	// To check whether keyboard visible or not
	public static boolean CheckKeyboardVisibility(final View activityRootView){

		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				Rect r = new Rect();
				//r will be populated with the coordinates of your view that area still visible.
				activityRootView.getWindowVisibleDisplayFrame(r);
				int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
				if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
					sKeyboardstate=true;
				}else{
					sKeyboardstate=false;
				}
			}
		});
		return sKeyboardstate; 
	}

	public boolean mapviewstoreloadingconstraint(int zoomlevel,double scrolleddistance){
		double needDistance=sDistance/2; 
		Log.i(TAG,"NeedDistance: "+needDistance);
		return conditionCheck(scrolleddistance,needDistance);
	}

	public boolean conditionCheck(double scrolleddistance,double needdistance){
		boolean conditionCheckFlag = false;
		if(scrolleddistance>needdistance){
			conditionCheckFlag = true;
		}
		return conditionCheckFlag;
	}

	// To show categories header with Animation
	public void showCategoriesHeaderView(){
		// To show selected categories and sub categories in header view
		mMenuBrowse.setBackgroundResource(R.drawable.footer_dark_blue_new);
		mSearchBar.setVisibility(View.VISIBLE);
		mCategoriesHeader.setVisibility(View.INVISIBLE);
		Animation custom_animation = AnimationUtils.loadAnimation(ShopperHomePage.this, R.anim.search_in);
		CustomAnimationListener customAnimationListener = new CustomAnimationListener(mCategoriesHeader, "Visible");
		custom_animation.setAnimationListener(customAnimationListener);
		mCategoriesHeader.startAnimation(custom_animation);
		mCategoriesText.setText(mSelectedcategories);
		mSubCategoritesText.setText(mSelectedSubCategories);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// To notify  system that its time to run garbage collector service
		System.gc();
		finish();
	}

	@Override
	public void onPause() {
		super.onPause();
		sIsActivity_live = false;
		if(!getUserType().equalsIgnoreCase("Guest")){
			unregisterReceiver(mNotificationReceiver);
			if(mNotificationSync!=null)
				mNotificationSync.cancelAlarm();
		}
		
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		Log.i("activity call back methods", "On user leave hint");
		new RefreshZoupons().isApplicationGoneBackground(ShopperHomePage.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG,"onResume"); 
		sIsActivity_live = true;
		if(!getUserType().equalsIgnoreCase("Guest")){
			registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
			// To start notification sync
			mNotificationSync = new ScheduleNotificationSync(ShopperHomePage.this,ZouponsConstants.sCustomerModuleFlag);
			mNotificationSync.setRecurringAlarm();
		}
		new CheckUserSession(ShopperHomePage.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(ShopperHomePage.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		if(mGoogleMap != null){
			Log.i("Google map", "camera change listner set");
			mGoogleMap.setOnCameraChangeListener(ShopperHomePage.this);
		}
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		Log.i(TAG,"onRestart"); 
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i(TAG,"onStart"); 
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i(TAG,"onStop"); 
		SharedPreferences mPrefs = getSharedPreferences("UserLocationPreferences", MODE_PRIVATE);
		Editor mPreferenceEditor = mPrefs.edit();
		if(mGoogleMap != null){
			LatLng mLastKnownLatLngValues = getMapViewNorthWestGeoPoint();
			mPreferenceEditor.putString("last_known_latitude",(String.valueOf(mLastKnownLatLngValues.latitude)));
			mPreferenceEditor.putString("last_known_longitude",(String.valueOf(mLastKnownLatLngValues.longitude)));
			mPreferenceEditor.commit();
		}
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Log.i("location client","on Connection failed" + " " + arg0.getErrorCode());
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Log.i("location client","on Connected");
		if(mLocationClient != null && mLocationClient.getLastLocation() != null){
			Location mCurrentLocation = mLocationClient.getLastLocation();
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
			//Toast.makeText(ShopperHomePage.this, "Please wait fo", duration)
			mLocationClient.registerConnectionCallbacks(this);
		}

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Log.i("location client","on DisConnected");
	}

	@Override
	public void onCameraChange(CameraPosition cameraposition) {
		// TODO Auto-generated method stub
		try{
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
				sDistance=getMapViewRadius(MapViewCenter.mMapViewCenter_GeoPoint);
			}else{
				Log.i("zoom check","same");
				//Logic to differentiate the first scroll and next scrolls of mapview
				//Function to calculate distance between MapView Before scroll and After scroll
				double mScrolledDistance=GetScrolledDistance(mBeforScrollCenter, mAfterScrollCenter);
				sDistance=getMapViewRadius(MapViewCenter.mMapViewCenter_GeoPoint);
				//Check the user scrolled distance is greater than our condition
				flag=mapviewstoreloadingconstraint((int)mGoogleMap.getCameraPosition().zoom,mScrolledDistance);
			}
			if(flag){
				CurrentLocation.CurrentLocation_Latitude=String.valueOf(MapViewCenter.mMapViewCenter_GeoPoint.latitude);
				CurrentLocation.CurrentLocation_Longitude=String.valueOf(MapViewCenter.mMapViewCenter_GeoPoint.longitude);
				if(ShopperHomePage.sMapViewOnScrollViewFlag.equals("currentlocation")){
					sStoreDetailsStart="0";
					sStoreDetailsEndLimit="20";
					sStoreDetailsCount = "0";	
					sIsLoadMore = false;
					/*if(mShopListView.getFooterViewsCount() == 0){
						mShopListView.addFooterView(mFooterLayout);	
					}*/

					if(mStoreNearThread.getStatus()==AsyncTask.Status.RUNNING){
						mStoreNearThread.cancel(true);
						mStoreNearThread = new StoreNearCurrentLocationAsyncThread(ShopperHomePage.this,mGoogleMap,mShopListView,mCurrentLocation,sDistance,"ShopperHomePage",sFooterLayout,"NOPROGRESS",TAG,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude,false);
						mStoreNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
					}else{
						mStoreNearThread = new StoreNearCurrentLocationAsyncThread(ShopperHomePage.this,mGoogleMap,mShopListView,mCurrentLocation,sDistance,"ShopperHomePage",sFooterLayout,"NOPROGRESS",TAG,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude,false);
						mStoreNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
					}
				}else if(ShopperHomePage.sMapViewOnScrollViewFlag.equals("search")){
					//Toast.makeText(ShopperHomePage.this, "Loading...", Toast.LENGTH_SHORT).show();
					//Set MenuSearchBarClickListener CLICKFLAG to true 
					MenuBarSearchClickListener.sCLICKFLAG=true;
					sStoreDetailsStart="0";
					sStoreDetailsEndLimit="20";
					sStoreDetailsCount = "0";	
					sIsLoadMore = false;
					/*if(mShopListView.getFooterViewsCount() == 0){
						mShopListView.addFooterView(mFooterLayout);	
					}*/
					mStoresLocationAsynchThread = new StoresLocationAsynchThread(ShopperHomePage.this,mMenuSearch,mShrinkSearch,sSearchStoreName,mSearchBar,sHomePageFreezeView,mGoogleMap,mShopListView,mCurrentLocation,sDistance,"ShopperHomePage",mCancelStoreName,mSearchBoxContainer,"",sFooterLayout,"NOPROGRESS",false,sDeviceCurrentLocationLatitude,sDeviceCurrentLocationLongitude);
					if(mStoresLocationAsynchThread.getStatus()==AsyncTask.Status.RUNNING){
						Log.i(TAG,"Search Store Thread Canceled");
						mStoresLocationAsynchThread.cancel(true);
						mStoresLocationAsynchThread.execute(mSearch_storeId,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
					}else{
						mStoresLocationAsynchThread.execute(mSearch_storeId,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
					}
				}else if(ShopperHomePage.sMapViewOnScrollViewFlag.equals("browse")){
					//Toast.makeText(ShopperHomePage.this, "Loading...", Toast.LENGTH_SHORT).show();
					sStoreDetailsStart="0";
					sStoreDetailsEndLimit="20";
					sStoreDetailsCount = "0";	
					sIsLoadMore = false;
					/*if(mShopListView.getFooterViewsCount() == 0){
						mShopListView.addFooterView(mFooterLayout);	
					}*/
					storecategories(mStore_categoriesId,false,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
				}
			}else{
				Log.i(TAG,"Sorry Not Loading");
			}	
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	// To check whether google play services available or not
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

	// To show corresponding error dialog while loading google map 
	void showErrorDialog(int code) {
		mUpdateGooglePlayServicesDialog = GooglePlayServicesUtil.getErrorDialog(code, this,REQUEST_CODE_RECOVER_PLAY_SERVICES);
		mUpdateGooglePlayServicesDialog.show();
	}
	
	private String getUserType(){
		SharedPreferences mPrefs = getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		return mPrefs.getString("user_login_type", "");
	}


}