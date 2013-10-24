package com.us.zoupons;


import java.util.ArrayList;
import java.util.Collections;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
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
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.Categories_ClassVariables;
import com.us.zoupons.ClassVariables.CurrentLocation;
import com.us.zoupons.ClassVariables.MapViewCenter;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.ClassVariables.Search_ClassVariables;
import com.us.zoupons.ClassVariables.StoreCategories_ClassVariables;
import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;
import com.us.zoupons.ClassVariables.SubCategories_ClassVariables;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.FlagClasses.ZPayFlag;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.android.AsyncThreadClasses.SearchAsynchThread;
import com.us.zoupons.android.AsyncThreadClasses.StoreNearCurrentLocationAsyncThread;
import com.us.zoupons.android.AsyncThreadClasses.StoresLocationAsynchThread;
import com.us.zoupons.android.AsyncThreadClasses.StoresQRCodeAsynchThread;
import com.us.zoupons.android.lib.integrator.IntentIntegrator;
import com.us.zoupons.android.lib.integrator.IntentResult;
import com.us.zoupons.android.listview.inflater.classes.CustomStoreAdapter;
import com.us.zoupons.android.listview.inflater.classes.ShopNearestList_Adapter;
import com.us.zoupons.android.mapviewcallouts.CustomMapCalloutClickListener;
import com.us.zoupons.android.mapviewcallouts.CustomMapViewCallout;
import com.us.zoupons.animation.CustomAnimationListener;
import com.us.zoupons.notification.CustomNotificationAdapter;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotificationItemClickListener;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;

/**
 * This demo uses a custom HorizontalScrollView that ignores touch events, and therefore does NOT allow manual scrolling.
 * 
 * The only scrolling allowed is scrolling in code triggered by the menu button.
 * 
 * When the button is pressed, both the menu and the app will scroll. So the menu isn't revealed from beneath the app, it
 * adjoins the app and moves with the app.
 */

public class SlidingView extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener,OnCameraChangeListener{

	static MyHorizontalScrollView scrollView;
	static View leftMenu;
	public static View rightMenu;
	static View app;
	public Typeface mZouponsFont; 
	public static LinearLayout btnSlide;
	public RelativeLayout btnLogout; 

	//LeftMenu
	public static LinearLayout mHome,mZpay,mInvoiceCenter,mGiftcards,mManageCards,mReceipts,mMyFavourites,mMyFriends,mChat,mRewards,mSettings,mLogout;
	public static TextView mHomeText,mZpayText,mInvoiceCenterText,mGiftCardsText,mManageCardsText,mReceiptsText,mMyFavoritesText,mMyFriendsText,mChatText,mRewardsText,mSettingsText,mLogoutText;

	//RightMenu
	public static LinearLayout mInfo_RightMenu,mMobilePay_RightMenu,mGiftCards_RightMenu,mDeals_RightMenu,mCoupons_RightMenu,mSocial_RightMenu,mReviews_RightMenu,mLocations_RightMenu,mPhotos_RightMenu,mVideos_RightMenu,mContactStore_RightMenu,mFavorite_RightMenu;
	public static TextView mInfoText_RightMenu,mMobilePayText_RightMenu,mGiftCardsText_RightMenu,mDealsText_RightMenu,mCouponsText_RightMenu,mSocialText_RightMenu,mReviewsText_RightMenu,mLocationsText_RightMenu,mPhotosText_RightMenu,mVideosText_RightMenu,mContactStoreText_RightMenu,mFavoriteText_RightMenu,mStoreName_RightMenu,mStoreLocation_RightMenu;
	/*public static ImageView mStoreImage_RightMenu;*/
	public static ImageView mInfoImage_RightMenu,mMobilePayImage_RightMenu,mGiftCardsImage_RightMenu,mDealsImage_RightMenu,mCouponsImage_RightMenu,mSocialImage_RightMenu,mReviewsImage_RightMenu,mLocationsImage_RightMenu,mPhotosImage_RightMenu,mVideosImage_RightMenu,mContactStoreImage_RightMenu,mFavoriteImage_RightMenu;

	public EditText mSearchZipcode;
	public static AutoCompleteTextView mSearchStoreName;
	public static Button mCancelStoreName;
	public static RelativeLayout mSearchBoxContainer;
	public LinearLayout mMenuQRCode,mMenuList,mMenuBrowse,mMenubarSplitter;
	public static LinearLayout mMenuSearch,mMenuBarContainer/*,mZpayMenuBarContainer*/;
	public static Button mHomePageFreezeView;
	public static RelativeLayout mMapViewContainer;
	public static Button mCurrentLocation,mNotificationCount;
	public static LinearLayout mListView;
	public ImageView mMenuBarBrowseImage,mMenuBarQRCodeImage,mMenuBarSearchImage,mMenuBarListImage,mLogoutImage,mNotificationImage,mCalloutTriangleImage,mTabBarLoginChoice,mZouponsHomePage;
	public TextView mMenuBarBrowseText,mMenuBarQRCodeText,mMenuBarSearchText,mMenuBarListText;
	public TextView mMenuBar_ZsendText,mMenuBar_ZapproveText,mMenuBar_ZpayManageReceiptsText,mMenuBar_ZpayManageCardsText;
	public ViewGroup tabBar,menuBar,middleView,leftMenuItems,rightMenuItems;
	public static ScrollView leftMenuScrollView, rightMenuScrollView;;
	public static ViewGroup searchBar;
	public static AnimationSet shrinkSearch;
	public static SlidingView newContext; 
	public static ListView mShopListView;

	//zpay bar
	public static LinearLayout mZpayBar;
	public ImageView mZpayStep1,mZpayStep2,mZpayStep3;
	public TextView mZpayStep1_Search,mZpayStep2_ChooseCard,mZpayStep3_Approve;
	public Button mZpayQRCode,mZpayFavorites,mZpaySearch;
	public static Button mZpayList;

	boolean mRotateAnimation = false;
	Handler handler = new Handler();
	int btnWidth;
	public static String TAG = "SlidingView"; //$NON-NLS-1$
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;

	//public static TapControlledMapView mMapView; // use the custom TapControlledMapView

	public static GoogleMap mGoogleMap;
	public UiSettings mGoogleMapUISettings;
	public LocationClient mLocationClient;
	public static LatLng mLocationGeoCordinates;

	public Drawable drawable;
	public int storeCount;
	public double mLatitude,mLongitude,mMenuWidth,mZpayMenuWidth;
	public int mScreenWidth;
	public Bitmap bmp;
	public int categoriesViewFlag;
	private ProgressDialog mProgressDialog=null;
	public static String searchText=null;
	private boolean CurrentLocationFlag=false;
	private boolean messageFlag,messageFlag1;
	SearchAsynchThread searchasynchtask=null;


	CustomStoreAdapter adapter;
	public static boolean keyboardstate;
	public static int searchcloseflag=0;
	static int left=0;

	// for store details list view offset
	public static String mStoreDetailsStart="0",mStoreDetailsEndLimit="20",mStoreDetailsCount = "0";	
	public static boolean mIsLoadMore = false;
	public static View mFooterLayout;
	private TextView mFooterText;
	LayoutInflater mInflater;
	static double distance = 0;
	public String store_categoriesId = "";
	public String search_storeId="";
	public String QRCoderesult="";
	public static String mMapViewOnScrollViewFlag;

	//Get ScrollDistance Variables
	public LatLng mBeforScrollCenter,mAfterScrollCenter;
	public boolean isFirstTimeMapScroll;

	// For categories header view
	public RelativeLayout mCategoriesHeader;
	public TextView mCategoriesText,mSubCategoritesText;
	public Button mCategoriesCancel;
	private String mSelectedcategories,mSelectedSubCategories;
	private ArrayList<Object> mSubcategoriesData;
	private boolean mIsSubCategoriesSelected = false;
	private Menu mSubCategoriesMenu;
	private View mPopUpLayout;
	private ListView mNotificationList;
	ScheduleNotificationSync mNotificationSync;
	private float mCurrentZoom=0;

	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	public static String mDeviceCurrentLocationLatitude="",mDeviceCurrentLocationLongitude="";

	public static boolean isActivity_live=true;
	private static int mapTime=2000,waited;
	private final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1;

	private Dialog mUpdateGooglePlayServicesDialog = null;

	// set this to true to stop the thread
	volatile boolean shutdown = false;
	StoreNearCurrentLocationAsyncThread storeNearThread;
	StoresLocationAsynchThread storesLocationAsynchThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		newContext=SlidingView.this;

		mProgressDialog=new ProgressDialog(SlidingView.this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage("Loading Stores...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);

		zouponswebservice = new ZouponsWebService(SlidingView.this);
		parsingclass = new ZouponsParsingClass(SlidingView.this);

		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);

		mConnectionAvailabilityChecking = new NetworkCheck();
		shrinkSearch = new AnimationSet(true);

		/*
		 * Condition to intialize the search box to false state
		 **/
		if(MenuBarSearchClickListener.CLICKFLAG==true){
			MenuBarSearchClickListener.CLICKFLAG=false;
		}

		//Call function to get width of the screen
		mScreenWidth=getScreenWidth(); 	
		if(mScreenWidth>0){	//To fix Home Page menubar items width
			mMenuWidth=mScreenWidth/4;
		}

		if(mScreenWidth>0){	//To fix ZPay menubar items width
			mZpayMenuWidth=mScreenWidth/3;
		}

		leftMenu = inflater.inflate(R.layout.horz_scroll_menu, null);
		rightMenu = inflater.inflate(R.layout.rightmenu, null);
		app = inflater.inflate(R.layout.horz_scroll_app, null);

		tabBar = (ViewGroup) app.findViewById(R.id.tabBar);
		searchBar = (ViewGroup) app.findViewById(R.id.searchbar);
		menuBar = (ViewGroup) app.findViewById(R.id.menubar);
		middleView = (ViewGroup) app.findViewById(R.id.middleview);
		leftMenuItems = (ViewGroup) leftMenu.findViewById(R.id.menuitems);
		rightMenuItems = (ViewGroup) rightMenu.findViewById(R.id.rightmenuitems);

		leftMenuScrollView = (ScrollView) leftMenu.findViewById(R.id.leftmenu_scrollview);
		leftMenuScrollView.fullScroll(View.FOCUS_UP);
		leftMenuScrollView.pageScroll(View.FOCUS_UP);

		rightMenuScrollView = (ScrollView) rightMenu.findViewById(R.id.rightmenu_scrollview);
		rightMenuScrollView.fullScroll(View.FOCUS_UP);
		rightMenuScrollView.pageScroll(View.FOCUS_UP);

		btnSlide = (LinearLayout) tabBar.findViewById(R.id.BtnSlide);
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_homepage_logout_container);
		// For notification and logout
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_homepage_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_homepage_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		mZouponsHomePage = (ImageView) btnLogout.findViewById(R.id.zoupons_home);
		mZouponsHomePage.setAlpha(100);

		new LoginChoiceTabBarImage(SlidingView.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		final View[] children = new View[] { leftMenu, app, rightMenu };

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

		//leftmenu layouts
		mHome = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_home);
		mZpay = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zpay);
		mInvoiceCenter = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_invoicecenter);
		mGiftcards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zgiftcards);
		mManageCards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_managecards);
		mReceipts = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_receipts);
		mMyFavourites = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfavourites);
		mMyFriends = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfriends);
		mChat = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_customercenter);
		mRewards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_rewards);
		mSettings = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_settings);
		mLogout = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_logout);

		/*Right Navigation*/
		/*mStoreImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_storeimage);*/
		mInfo_RightMenu = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_info);
		mMobilePay_RightMenu = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_mobilepay);
		mGiftCards_RightMenu = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_giftcards);
		mDeals_RightMenu=(LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_deals);
		mCoupons_RightMenu = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_coupons);
		mSocial_RightMenu = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_social);
		mReviews_RightMenu = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_reviews);
		mLocations_RightMenu = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_locations);
		mPhotos_RightMenu = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_photos);
		mVideos_RightMenu = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_videos);
		mContactStore_RightMenu = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_contactstore);
		mFavorite_RightMenu = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_favorite);

		mStoreName_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_storename);
		mStoreName_RightMenu.setTypeface(mZouponsFont);
		mStoreLocation_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_storelocation);
		mStoreLocation_RightMenu.setTypeface(mZouponsFont);

		mInfoText_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_infotext);
		mInfoText_RightMenu.setTypeface(mZouponsFont);
		mMobilePayText_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_mobilepaytext);
		mMobilePayText_RightMenu.setTypeface(mZouponsFont);
		mGiftCardsText_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_giftcardstext);
		mGiftCardsText_RightMenu.setTypeface(mZouponsFont);
		mDealsText_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_dealstext);
		mDealsText_RightMenu.setTypeface(mZouponsFont);
		mCouponsText_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_couponstext);
		mCouponsText_RightMenu.setTypeface(mZouponsFont);
		mSocialText_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_socialtext);
		mSocialText_RightMenu.setTypeface(mZouponsFont);
		mReviewsText_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_reviewstext);
		mReviewsText_RightMenu.setTypeface(mZouponsFont);
		mLocationsText_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_locationstext);
		mLocationsText_RightMenu.setTypeface(mZouponsFont);
		mPhotosText_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_photostext);
		mPhotosText_RightMenu.setTypeface(mZouponsFont);
		mVideosText_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_videostext);
		mVideosText_RightMenu.setTypeface(mZouponsFont);
		mContactStoreText_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_contactstoretext);
		mContactStoreText_RightMenu.setTypeface(mZouponsFont);
		mFavoriteText_RightMenu = (TextView) rightMenuItems.findViewById(R.id.rightmenu_favoritetext);
		mFavoriteText_RightMenu.setTypeface(mZouponsFont);

		mInfoImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_infoimage);
		mMobilePayImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_zpayimage);
		mGiftCardsImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_giftcardsimage);
		mDealsImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_dealsimage);
		mCouponsImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_couponsimage);
		mSocialImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_socialimage);
		mReviewsImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_reviewsimage);
		mLocationsImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_locationsimage);
		mPhotosImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_photosimage);
		mVideosImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_videoimage);
		mContactStoreImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_contactstoreimage);
		mFavoriteImage_RightMenu = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_favoriteimage);

		mHomeText = (TextView) leftMenuItems.findViewById(R.id.menuHome);
		mHomeText.setTypeface(mZouponsFont);
		mZpayText = (TextView) leftMenuItems.findViewById(R.id.menuZpay);
		mZpayText.setTypeface(mZouponsFont);
		mInvoiceCenterText = (TextView) leftMenuItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
		mInvoiceCenterText.setTypeface(mZouponsFont);
		mGiftCardsText = (TextView) leftMenuItems.findViewById(R.id.menuGiftCards);
		mGiftCardsText.setTypeface(mZouponsFont);
		mManageCardsText = (TextView) leftMenuItems.findViewById(R.id.menuManageCards);
		mManageCardsText.setTypeface(mZouponsFont);
		mReceiptsText = (TextView) leftMenuItems.findViewById(R.id.menuReceipts);
		mReceiptsText.setTypeface(mZouponsFont);
		mMyFavoritesText = (TextView) leftMenuItems.findViewById(R.id.menufavorites);
		mMyFavoritesText.setTypeface(mZouponsFont);
		mMyFriendsText = (TextView) leftMenuItems.findViewById(R.id.menuMyFriends);
		mMyFriendsText.setTypeface(mZouponsFont);
		mChatText = (TextView) leftMenuItems.findViewById(R.id.menuCustomerCenter);
		mChatText.setTypeface(mZouponsFont);
		mRewardsText = (TextView) leftMenuItems.findViewById(R.id.menuRewards);
		mRewardsText.setTypeface(mZouponsFont);
		mSettingsText = (TextView) leftMenuItems.findViewById(R.id.menuSettings);
		mSettingsText.setTypeface(mZouponsFont);
		mLogoutText = (TextView) leftMenuItems.findViewById(R.id.menuLogout);
		mLogoutText.setTypeface(mZouponsFont);

		mSearchStoreName = (AutoCompleteTextView) searchBar.findViewById(R.id.search_storename);
		mCancelStoreName = (Button) searchBar.findViewById(R.id.clear_storename);
		mCancelStoreName.setTypeface(mZouponsFont);
		mSearchBoxContainer=(RelativeLayout) searchBar.findViewById(R.id.searchbox_container);

		mZpayBar=(LinearLayout) searchBar.findViewById(R.id.zpaybar);
		mZpayStep1=(ImageView) searchBar.findViewById(R.id.step1_zpay);
		mZpayStep2=(ImageView) searchBar.findViewById(R.id.step2_zpay);
		mZpayStep3=(ImageView) searchBar.findViewById(R.id.step3_zpay);
		mZpayStep1_Search=(TextView) searchBar.findViewById(R.id.step1_txt);
		mZpayStep1_Search.setTypeface(mZouponsFont);
		mZpayStep2_ChooseCard=(TextView) searchBar.findViewById(R.id.step2_txt);
		mZpayStep2_ChooseCard.setTypeface(mZouponsFont);
		mZpayStep3_Approve=(TextView) searchBar.findViewById(R.id.step3_txt);
		mZpayStep3_Approve.setTypeface(mZouponsFont);
		mZpayQRCode=(Button) searchBar.findViewById(R.id.qrcode_zpay);
		mZpayQRCode.setTypeface(mZouponsFont);
		mZpayFavorites=(Button) searchBar.findViewById(R.id.favorites_zpay);
		mZpayFavorites.setTypeface(mZouponsFont);
		mZpaySearch=(Button) searchBar.findViewById(R.id.search_zpay);
		mZpaySearch.setTypeface(mZouponsFont);
		mZpayList=(Button) searchBar.findViewById(R.id.listview_zpay);
		mZpayList.setTypeface(mZouponsFont);

		mMenuBarContainer=(LinearLayout) menuBar.findViewById(R.id.menubarcontainer);
		mMenuBrowse=(LinearLayout) menuBar.findViewById(R.id.menubar_browse);
		mMenuBrowse.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
		mMenuQRCode=(LinearLayout) menuBar.findViewById(R.id.menubar_qrcode);
		mMenuQRCode.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
		mMenuSearch=(LinearLayout) menuBar.findViewById(R.id.menubar_search);
		mMenuSearch.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
		mMenuList=(LinearLayout) menuBar.findViewById(R.id.menubar_list);
		mMenuList.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
		mMenubarSplitter=(LinearLayout) menuBar.findViewById(R.id.menubar_splitter2);

		mMenuBarBrowseImage=(ImageView) menuBar.findViewById(R.id.menubar_browse_image);
		mMenuBarQRCodeImage=(ImageView) menuBar.findViewById(R.id.menubar_qrcode_image);
		mMenuBarSearchImage=(ImageView) menuBar.findViewById(R.id.menubar_search_image);
		mMenuBarListImage=(ImageView) menuBar.findViewById(R.id.menubar_list_image);

		mMenuBarBrowseText=(TextView) menuBar.findViewById(R.id.menubar_browse_text);
		mMenuBarBrowseText.setTypeface(mZouponsFont);
		mMenuBarQRCodeText=(TextView) menuBar.findViewById(R.id.menubar_qrcode_text);
		mMenuBarQRCodeText.setTypeface(mZouponsFont);
		mMenuBarSearchText=(TextView) menuBar.findViewById(R.id.menubar_search_text);
		mMenuBarSearchText.setTypeface(mZouponsFont);
		mMenuBarListText=(TextView) menuBar.findViewById(R.id.menubar_list_text);
		mMenuBarListText.setTypeface(mZouponsFont);

		mMapViewContainer=(RelativeLayout) middleView.findViewById(R.id.mapview);
		//mMapView=(TapControlledMapView) middleView.findViewById(R.id.zouponsHomePageMapView);
		mCurrentLocation=(Button) middleView.findViewById(R.id.googlemaps_current_location);
		mHomePageFreezeView=(Button) app.findViewById(R.id.freezeview);
		mHomePageFreezeView.setVisibility(View.VISIBLE);

		mListView=(LinearLayout) middleView.findViewById(R.id.shopList);
		mShopListView = (ListView) middleView.findViewById(R.id.shopListView);

		// Categories header layout initialization
		mCategoriesHeader = (RelativeLayout) searchBar.findViewById(R.id.categories_container);
		LinearLayout mCategoriesTextLayout = (LinearLayout) mCategoriesHeader.findViewById(R.id.categoriestextlayout); 
		mCategoriesText = (TextView) mCategoriesTextLayout.findViewById(R.id.categories_textId);
		mSubCategoritesText = (TextView) mCategoriesTextLayout.findViewById(R.id.subcategories_textId);
		mCategoriesCancel = (Button) mCategoriesHeader.findViewById(R.id.categories_cancelId);
		mGoogleMap = null;

		mIsLoadMore = false;
		mStoreDetailsStart="0";
		mStoreDetailsEndLimit="20";
		mStoreDetailsCount = "0";

		// add footer view to list view
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
		mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		/*if(mShopListView.getFooterViewsCount() == 0){
			mShopListView.addFooterView(mFooterLayout);	
		}*/

		// For Notification popup
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

		btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, rightMenu, searchBar, mSearchStoreName, shrinkSearch, 0, 
				getApplicationContext(),mMenuSearch,mMenuBarContainer,/*mZpayMenuBarContainer,*/mZpayBar,mHomePageFreezeView,mCancelStoreName,mSearchBoxContainer));

		mHome.setOnClickListener(new MenuItemClickListener(leftMenu,SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));

		mZpay.setOnClickListener(new MenuItemClickListener(leftMenu,SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenu,SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftcards.setOnClickListener(new MenuItemClickListener(leftMenu,SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mManageCards.setOnClickListener(new MenuItemClickListener(leftMenu, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mReceipts.setOnClickListener(new MenuItemClickListener(leftMenu, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenu,SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mMyFriends.setOnClickListener(new MenuItemClickListener(leftMenu,SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mChat.setOnClickListener(new MenuItemClickListener(leftMenu,SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mRewards.setOnClickListener(new MenuItemClickListener(leftMenu,SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mSettings.setOnClickListener(new MenuItemClickListener(leftMenu,SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));

		/*ClickListeners for all rightmenu items*/
		mInfo_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mMobilePay_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCards_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mDeals_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mCoupons_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mSocial_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mReviews_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mLocations_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mPhotos_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mVideos_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mContactStore_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFavorite_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, SlidingView.this,POJOMainMenuActivityTAG.TAG=TAG));

		mSearchStoreName.setOnEditorActionListener(new DoneOnEditorActionListener(SlidingView.this, shrinkSearch, searchBar, mSearchStoreName, /*mMenuBarListText,*/mHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mMenuBrowse,mMenuQRCode,mMenuSearch,mMenuList));

		mMenuQRCode.setOnClickListener(new MenuBarQRCodeClickListener(SlidingView.this,SlidingView.this,mMenuBrowse,mMenuSearch,mMenuList,0));
		mMenuBrowse.setOnClickListener(new MenuBarBrowseClickListener(SlidingView.this,mMenuBarBrowseText,mMenuQRCode,mMenuSearch,mMenuList));
		mMenuSearch.setOnClickListener(new MenuBarSearchClickListener(SlidingView.this,searchBar,mSearchStoreName,mMenuList,mMenuBrowse,mMenuQRCode,0,mHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mCategoriesHeader,mCategoriesText));
		mMenuList.setOnClickListener(new MenuBarListClickListener(SlidingView.this,middleView,mMenuBarListText,mMenuBarListImage,mZpayList,mMenuBrowse,mMenuQRCode,mMenuSearch,0,"SlidingView"));

		mHomePageFreezeView.setOnClickListener(new FreezeClickListener(scrollView, leftMenu, rightMenu,searchBar, mSearchStoreName, shrinkSearch, 0,
				SlidingView.this,mMenuSearch,mHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mGoogleMap));

		//Zpay click Listener Class
		mZpayQRCode.setOnClickListener(new MenuBarQRCodeClickListener(SlidingView.this,SlidingView.this,mMenuBrowse,mMenuSearch,mMenuList,1));
		mZpaySearch.setOnClickListener(new MenuBarSearchClickListener(SlidingView.this,searchBar,mSearchStoreName,mMenuList,mMenuBrowse,mMenuQRCode,1,mHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mCategoriesHeader,mCategoriesText));
		mZpayList.setOnClickListener(new MenuBarListClickListener(SlidingView.this,middleView,mMenuBarListText,mMenuBarListImage,mZpayList,mMenuBrowse,mMenuQRCode,mMenuSearch,1,"SlidingView"));

		mCurrentLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// To remove map scroll listener from google map
				mGoogleMap.setOnCameraChangeListener(null);

				mStoreDetailsStart="0";
				mStoreDetailsEndLimit="20";
				mStoreDetailsCount = "0";	
				mIsLoadMore = false;
				/*if(mShopListView.getFooterViewsCount() == 0){
					mShopListView.addFooterView(mFooterLayout);	
				}*/

				if(storeNearThread!=null&&storeNearThread.getStatus()==Status.RUNNING){
					storeNearThread.cancel(true);
				}
				
				try{

					//Load Current Location while activity loading
					if(mConnectionAvailabilityChecking.ConnectivityCheck(SlidingView.this)){
						if(mSearchStoreName.getVisibility()!=View.VISIBLE && mCategoriesHeader.getVisibility()!=View.VISIBLE){
							mMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
							getCurrentLocation();		// To Get Current Location
						}else if(mSearchStoreName.getVisibility()==View.VISIBLE&&mSearchStoreName.getText().toString().trim().length()>0){
							mGoogleMap.clear();
							mMapViewOnScrollViewFlag ="search";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
							boolean mCheckStoreName=false;
							for(int i=0;i<WebServiceStaticArrays.mSearchStore.size();i++){
								final Search_ClassVariables obj = (Search_ClassVariables) WebServiceStaticArrays.mSearchStore.get(i);
								if(mSearchStoreName.getText().toString().equals(obj.storeName)){
									mCheckStoreName=true;
									mStoreDetailsStart="0";
									mStoreDetailsEndLimit="20";
									mStoreDetailsCount = "0";	
									mIsLoadMore = false;
									/*if(mShopListView.getFooterViewsCount() == 0){
										mShopListView.addFooterView(mFooterLayout);	
									}*/

									//gp=new GeoPoint((int)(Double.parseDouble(mDeviceCurrentLocationLatitude)*1E6),(int) (Double.parseDouble(mDeviceCurrentLocationLongitude)*1E6));

									mLocationGeoCordinates =  new LatLng(Double.parseDouble(mDeviceCurrentLocationLatitude), Double.parseDouble(mDeviceCurrentLocationLongitude));

									mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mLocationGeoCordinates),new CancelableCallback() {

										@Override
										public void onFinish() {
											// TODO Auto-generated method stub
											distance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
											StoresLocationAsynchThread storeslocationasynchthread = new StoresLocationAsynchThread(SlidingView.this,mMenuSearch,shrinkSearch,mSearchStoreName,searchBar,mHomePageFreezeView,mGoogleMap,mShopListView,distance,"SlidingView",mCancelStoreName,mSearchBoxContainer,"",mFooterLayout,"PROGRESS",true,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
											storeslocationasynchthread.execute(obj.storeId,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
										}

										@Override
										public void onCancel() {
											// TODO Auto-generated method stub

										}
									});

									//mGoogleMap.animateCamera(arg0, arg1)

									// To move map to current location
									/*mMapView.getController().animateTo(gp,new Runnable() {

										@Override
										public void run() {
											distance = getMapViewRadius(mMapView.getMapCenter());
											StoresLocationAsynchThread storeslocationasynchthread = new StoresLocationAsynchThread(SlidingView.this,mMenuSearch,shrinkSearch,mSearchStoreName,searchBar,mHomePageFreezeView,mapOverlays,itemizedOverlay,mMapView,mShopListView,distance,"SlidingView",mCancelStoreName,mSearchBoxContainer,"",mFooterLayout,"PROGRESS",gp,true,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
											storeslocationasynchthread.execute(obj.storeId,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
										}
									});*/

								}
							}
							if(!mCheckStoreName){
								Toast.makeText(getApplicationContext(), "Please Enter Valid Store Name.", Toast.LENGTH_SHORT).show();
							}
						}else if(mSearchStoreName.getVisibility()==View.VISIBLE&&mSearchStoreName.getText().toString().trim().length()==0){
							//close searchbox and disable freezeview
							String returnValue = new FreezeClickListener(scrollView, leftMenu, rightMenu,searchBar, mSearchStoreName, shrinkSearch, 0,
									SlidingView.this,mMenuSearch,mHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mGoogleMap).closeSearchBox(TAG);
							if(returnValue.equalsIgnoreCase("success")){
								mMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
								getCurrentLocation();		// To Get Current Location
							}
						}else if(mCategoriesHeader.getVisibility() == View.VISIBLE){
							mGoogleMap.clear();
							mStoreDetailsStart="0";
							mStoreDetailsEndLimit="20";
							mStoreDetailsCount = "0";	
							mIsLoadMore = false;
							/*if(mShopListView.getFooterViewsCount() == 0){
								mShopListView.addFooterView(mFooterLayout);	
							}*/
							mLocationGeoCordinates =  new LatLng(Double.parseDouble(mDeviceCurrentLocationLatitude), Double.parseDouble(mDeviceCurrentLocationLongitude));
							mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mLocationGeoCordinates),new CancelableCallback() {

								@Override
								public void onFinish() {
									// TODO Auto-generated method stub
									storecategories(store_categoriesId,true,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
								}

								@Override
								public void onCancel() {
									// TODO Auto-generated method stub

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
				new LogoutSessionTask(SlidingView.this).execute();
				Intent intent_logout = new Intent(SlidingView.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				SlidingView.this.finish();
				startActivity(intent_logout);
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
				new LogoutSessionTask(SlidingView.this).execute();
				Intent intent_logout = new Intent(SlidingView.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				SlidingView.this.finish();
				startActivity(intent_logout);
			}
		});

		adapter = new CustomStoreAdapter(SlidingView.this, android.R.layout.simple_dropdown_item_1line,"home");
		mSearchStoreName.setAdapter(adapter);

		mSearchStoreName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				CustomStoreAdapter.isSearchStringChanged = true;


				if(!mSearchStoreName.isPopupShowing()){
					Log.i("text changed", "hide");
					mSearchStoreName.dismissDropDown();	
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

				MenuBarSearchClickListener.CLICKFLAG=true;
				//mGoogleMap.clear();
				if(mSearchStoreName.getText().toString().length()>0&&mMapViewOnScrollViewFlag.equalsIgnoreCase("search")){

					//Set menu bar background to default
					mMenuBrowse.setBackgroundResource(R.drawable.header_2);
					mMenuQRCode.setBackgroundResource(R.drawable.header_2);
					mMenuSearch.setBackgroundResource(R.drawable.header_2);
					mMenuList.setBackgroundResource(R.drawable.header_2);

					//Set MenuSearchBarClickListener CLICKFLAG to true 
					// close searchbox and its items in map and list view and reload store near current location stores and disable freezeview
					if(new FreezeClickListener(scrollView, leftMenu, rightMenu,searchBar, mSearchStoreName, shrinkSearch, 0,
							SlidingView.this,mMenuSearch,mHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mGoogleMap).closeSearchBox(TAG).equals("success")){
						mMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
						mStoreDetailsStart="0";
						mStoreDetailsEndLimit="20";
						mStoreDetailsCount = "0";	
						mIsLoadMore = false;
						/*if(mShopListView.getFooterViewsCount() == 0){
							mShopListView.addFooterView(mFooterLayout);	
						}*/
						getCurrentLocation();
					}
				}else{
					//close searchbox and disable freezeview
					if(new FreezeClickListener(scrollView, leftMenu, rightMenu,searchBar, mSearchStoreName, shrinkSearch, 0,
							SlidingView.this,mMenuSearch,mHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mGoogleMap).closeSearchBox(TAG).equalsIgnoreCase("success")){
						if(mMapViewOnScrollViewFlag.equalsIgnoreCase("browse")){
							//Set selection for browse menu bar background
							mMenuBrowse.setBackgroundResource(R.drawable.footer_dark_blue_new);
							mMenuQRCode.setBackgroundResource(R.drawable.header_2);
							mMenuSearch.setBackgroundResource(R.drawable.header_2);
							mMenuList.setBackgroundResource(R.drawable.header_2);
							showCategoriesHeaderView();
						}else if(mMapViewOnScrollViewFlag.equalsIgnoreCase("qrcode")){
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

		mSearchStoreName.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// clear  list view offset variables
				try{
					InputMethodManager softkeyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					softkeyboard.hideSoftInputFromWindow(mSearchStoreName.getWindowToken(), 0);
					mStoreDetailsStart="0";
					mStoreDetailsEndLimit="20";
					mStoreDetailsCount = "0";	
					mIsLoadMore = false;
					mGoogleMap.clear();
					/*if(mShopListView.getFooterViewsCount() == 0){
						mShopListView.addFooterView(mFooterLayout);	
					}*/
					mMapViewOnScrollViewFlag ="search";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
					Search_ClassVariables obj = (Search_ClassVariables) adapter.getItem(position);
					mSearchStoreName.setThreshold(1000);
					mSearchStoreName.setText(obj.storeName);
					mSearchStoreName.setThreshold(1);
					search_storeId = obj.storeId;

					distance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
					mGoogleMap.setOnCameraChangeListener(null);
					storesLocationAsynchThread = new StoresLocationAsynchThread(SlidingView.this,mMenuSearch,shrinkSearch,mSearchStoreName,searchBar,mHomePageFreezeView,mGoogleMap,mShopListView,distance,"SlidingView",mCancelStoreName,mSearchBoxContainer,"",mFooterLayout,"PROGRESS",true,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
					storesLocationAsynchThread.execute(obj.storeId,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);

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
				Animation custom_animation = AnimationUtils.loadAnimation(SlidingView.this, R.anim.slideup);
				CustomAnimationListener customAnimationListener = new CustomAnimationListener(mCategoriesHeader, "InVisible");
				custom_animation.setAnimationListener(customAnimationListener);
				mCategoriesHeader.startAnimation(custom_animation);

				mIsLoadMore = false;
				mStoreDetailsStart="0";
				mStoreDetailsEndLimit="20";
				mStoreDetailsCount = "0";

				/*if(mShopListView.getFooterViewsCount() == 0){
					mShopListView.addFooterView(mFooterLayout);	
				}*/
				mGoogleMap.setOnCameraChangeListener(null);
				mCategoriesText.setText("Categories"); // Set to default , checked in Oncontextmenuclose function  
				mMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
				getCurrentLocation();
			}
		});

		mNotificationImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MenuOutClass.HOMEPAGE_MENUOUT=false;
				ZPayFlag.setFlag(0);
				OpenMenu.toCloseMenu("SlidingView");
				// Inflating custom Layout for PopUpWindow.
				mCalloutTriangleImage.setVisibility(View.VISIBLE);
				int popupheight = getWindowManager().getDefaultDisplay().getHeight()/2;
				// Initializing PopUpWindow
				final PopupWindow mPopUpWindow = new PopupWindow(mPopUpLayout,android.view.WindowManager.LayoutParams.WRAP_CONTENT,popupheight,true);
				mPopUpWindow.setWidth(tabBar.getWidth()-20);
				mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopUpWindow.setOutsideTouchable(false);
				mPopUpWindow.showAsDropDown(tabBar, 0, 10);
				mNotificationList.setAdapter(new CustomNotificationAdapter(SlidingView.this));
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

	/*private boolean isMyServiceRunning(){
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for(RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
			if(SearchService.class.getName().equals(service.service.getClassName())){
				return true;
			}
		}
		return false;
	}*/

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	/*Get Screen width*/
	public int getScreenWidth(){
		int Measuredwidth = 0;  
		int Measuredheight = 0;  
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Measuredwidth = display.getWidth(); 
		Measuredheight = display.getHeight();
		return Measuredwidth;
	}

	/*To get user current location*/    
	public void getCurrentLocation(){
		/*// For testing
		MapViewCenter.LoginCurrentLocationLatitude = 0.0;
		MapViewCenter.LoginCurrentLocationLongitude = 0.0;*/
		Log.i("current location","get current location");
		/*set initial map view center to check it in async task while scroll*/
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
				//SlidingView.mHomePageFreezeView.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "Please Wait...GPS fetching Current Location", Toast.LENGTH_SHORT).show();
				mLocationClient = new LocationClient(this,this,this);
				mLocationClient.connect();
			}else{
				Toast.makeText(this, "Please update google play application", Toast.LENGTH_LONG).show();
			}
			/*//If current location is not successfully fetched during login or navigationg from other screens ...
			if(locationoverlay.getLastFix() == null){
				locationoverlay.enableMyLocation();	
				Toast.makeText(getApplicationContext(), "Please Wait...GPS fetching Current Location", Toast.LENGTH_SHORT).show();

				//If current location overlay does not have last fix means wait for first fix
				locationoverlay.runOnFirstFix(new Runnable() {

					@Override
					public void run() {
						Log.i("current location source", "location from first fix");
						Message msg = new Message();
						msg.obj = "currentlocation";
						Bundle mLocationdetails = new Bundle();
						mLocationdetails.putDouble("latitude", locationoverlay.getMyLocation().getLatitudeE6()/1E6);
						mLocationdetails.putDouble("longitude", locationoverlay.getMyLocation().getLongitudeE6()/1E6);
						msg.setData(mLocationdetails);
						handler_response.sendMessage(msg);
					}
				});	
			}else{
				locationoverlay.enableMyLocation();
				//If current location overlay already have fix means....
				Message msg = new Message();
				msg.obj = "currentlocation";
				Bundle mLocationdetails = new Bundle();
				mLocationdetails.putDouble("latitude", locationoverlay.getMyLocation().getLatitudeE6()/1E6);
				mLocationdetails.putDouble("longitude", locationoverlay.getMyLocation().getLongitudeE6()/1E6);
				msg.setData(mLocationdetails);
				handler_response.sendMessage(msg);
			}*/
		}
	}

	/**
	 * function to load map
	 * */
	private void initilizeGoogleMap() {
		if (mGoogleMap == null) {
			Log.i("Google map", "initializing google maps");
			mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.zouponsHomePageMapView)).getMap();
			mGoogleMapUISettings = mGoogleMap.getUiSettings();
			mGoogleMapUISettings.setZoomControlsEnabled(false);
			mGoogleMapUISettings.setMyLocationButtonEnabled(false);
			mGoogleMapUISettings.setCompassEnabled(false);
			mGoogleMap.setInfoWindowAdapter(new CustomMapViewCallout(SlidingView.this));
			mGoogleMap.setOnInfoWindowClickListener(new CustomMapCalloutClickListener(SlidingView.this));
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
			}
		}
	}


	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's width.
	 */
	static class ClickListenerForScrolling implements OnClickListener {
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

			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);

			if(MenuBarSearchClickListener.CLICKFLAG==true){
				softkeyboardevent = (InputMethodManager)ClickListenerForScrollingContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				softkeyboardevent.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);
			}

			Context context = leftMenu.getContext();

			int menuWidth = leftMenu.getMeasuredWidth();

			switch(this.homepageFlag){
			case 0:
				if (!MenuOutClass.HOMEPAGE_MENUOUT) {
					Log.i(TAG,Messages.getString("MenuOut :")+MenuOutClass.HOMEPAGE_MENUOUT); //$NON-NLS-1$

					if(MenuBarSearchClickListener.CLICKFLAG==true){
						mSearchBox.clearFocus();
						mSearchBox.setFocusable(false);
						SlidingView.searchcloseflag=1;
						scrollView.smoothScrollTo(left, 0);
					}else{
						Log.i(TAG,Messages.getString("Search Box Is Not in Open State")); //$NON-NLS-1$
						SlidingView.searchcloseflag=1;
						scrollView.smoothScrollTo(left, 0);
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
					Log.i(TAG,Messages.getString("MenuOut :")+MenuOutClass.HOMEPAGE_MENUOUT); //$NON-NLS-1$
					// Scroll to 0 to reveal menu
					int right = menuWidth+menuWidth;

					if(MenuBarSearchClickListener.CLICKFLAG==true){

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
						MenuBarSearchClickListener.CLICKFLAG=false;
						searchcloseflag=1;
					}else{
						Log.i(TAG,Messages.getString("Search Box Is Not in Open State")); //$NON-NLS-1$
						searchcloseflag=1;
					}
					if(searchcloseflag==1){
						scrollView.smoothScrollTo(right, 0);
					}
					mFreezeHomePage.setVisibility(View.VISIBLE);
				}else {
					Log.i(TAG,Messages.getString("MenuOut : ")+MenuOutClass.HOMEPAGE_MENUOUT); //$NON-NLS-1$
					// Scroll to menuWidth so menu isn't on screen.
					Log.i(TAG,Messages.getString("2")); //$NON-NLS-1$
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

	/**
	 * Helper that remembers the width of the 'slide' button, so that the 'slide' button remains in view, even when the menu is
	 * showing.
	 */
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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE: //1
			if(resultCode==RESULT_OK){

				mMenuQRCode.setBackgroundResource(R.drawable.header_2);

				SlidingView.mMapViewOnScrollViewFlag="qrcode";
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
							mStoreDetailsStart="0";
							mStoreDetailsEndLimit="20";
							mStoreDetailsCount = "0";

							if(mSearchStoreName.getVisibility()==View.VISIBLE){
								//Set MenuSearchBarClickListener CLICKFLAG to true 
								MenuBarSearchClickListener.CLICKFLAG=true;

								//close searchbox and disable freezeview
								rtn = new FreezeClickListener(scrollView, leftMenu, rightMenu,searchBar, mSearchStoreName, shrinkSearch, 0,
										SlidingView.this,mMenuSearch,mHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mGoogleMap).closeSearchBox(TAG);
								if(rtn.equals("success")){
									StoresQRCodeAsynchThread storeqrcode = new StoresQRCodeAsynchThread(SlidingView.this, mGoogleMap, mShopListView,getMapViewRadius(mGoogleMap.getCameraPosition().target),"SlidingView","SlidingView",mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
									storeqrcode.execute(result);
								}
							}else if(mCategoriesHeader.getVisibility()==View.VISIBLE){

								mCategoriesHeader.setVisibility(View.INVISIBLE);
								Animation custom_animation = AnimationUtils.loadAnimation(SlidingView.this, R.anim.slideup);
								CustomAnimationListener customAnimationListener = new CustomAnimationListener(mCategoriesHeader, "InVisible");
								custom_animation.setAnimationListener(customAnimationListener);
								mCategoriesHeader.startAnimation(custom_animation);
								mCategoriesText.setText("Categories");

								StoresQRCodeAsynchThread storeqrcode = new StoresQRCodeAsynchThread(SlidingView.this, mGoogleMap, mShopListView,getMapViewRadius(mGoogleMap.getCameraPosition().target),"SlidingView","SlidingView",mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
								storeqrcode.execute(result);
							}else{
								StoresQRCodeAsynchThread storeqrcode = new StoresQRCodeAsynchThread(SlidingView.this, mGoogleMap,  mShopListView,getMapViewRadius(mGoogleMap.getCameraPosition().target),"SlidingView","SlidingView",mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
								storeqrcode.execute(result);
							}
						}
					});
				}
			}else if(resultCode==RESULT_CANCELED){
				if(mMapViewOnScrollViewFlag.equalsIgnoreCase("browse")){
					//Set selection for browse menu bar background
					mMenuBrowse.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mMenuQRCode.setBackgroundResource(R.drawable.header_2);
					mMenuSearch.setBackgroundResource(R.drawable.header_2);
					mMenuList.setBackgroundResource(R.drawable.header_2);
				}else if(mMapViewOnScrollViewFlag.equalsIgnoreCase("search")){
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
				mMapViewOnScrollViewFlag ="currentlocation";
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
									if(mConnectionAvailabilityChecking.ConnectivityCheck(SlidingView.this)){

										mGetResponse=zouponswebservice.subcategories(obj.id);	

										if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
											String mParsingResponse=parsingclass.parseSubCategories(mGetResponse);
											if(mParsingResponse.equalsIgnoreCase("success")){
												for(int i=0;i<WebServiceStaticArrays.mSubCategoriesList.size();i++){
													SubCategories_ClassVariables parsedobjectvalues = (SubCategories_ClassVariables) WebServiceStaticArrays.mSubCategoriesList.get(i);
													if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("No Categories Available")*/){
														messageFlag=true;
														updateHandler(bundle, msg_response, "No Categories Available","2");	//send update to handlera
													}else{

													}
												}
												if(!messageFlag){
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
						store_categoriesId = obj.id;
						mMapViewOnScrollViewFlag ="browse";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
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
					store_categoriesId = obj.id;
					mMapViewOnScrollViewFlag ="browse";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
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
		if(categoriesViewFlag==1){				// Load Categories Menu Items
			Log.i(TAG,"Categories");
			menu.setHeaderTitle("Categories");
			for(int i=0;i<WebServiceStaticArrays.mCategoriesList.size();i++){
				Categories_ClassVariables obj = (Categories_ClassVariables) WebServiceStaticArrays.mCategoriesList.get(i);
				menu.add(0, v.getId(), 0, obj.categoryName);
			}
		}else if(categoriesViewFlag==2){		// Load SubCategories Menu Items
			Log.i(TAG,"SubCategories");
			menu.setHeaderTitle("Sub Categories");
			mSubCategoriesMenu = menu;
			for(int i=0;i<WebServiceStaticArrays.mSubCategoriesList.size();i++){
				SubCategories_ClassVariables obj = (SubCategories_ClassVariables) WebServiceStaticArrays.mSubCategoriesList.get(i);
				menu.add(1, v.getId(), 0, obj.categoryName);
			}
		}else if(categoriesViewFlag==3){		// Load StoreCategories Menu Items
			Log.i(TAG,"StoreCategories");
			menu.setHeaderTitle("Store Categories");
			for(int i=0;i<WebServiceStaticArrays.mStoreCategoriesList.size();i++){
				StoreCategories_ClassVariables obj = (StoreCategories_ClassVariables) WebServiceStaticArrays.mStoreCategoriesList.get(i);
				menu.add(2, v.getId(), 0, obj.storeName);
			}
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onContextMenuClosed(Menu menu) {
		super.onContextMenuClosed(menu);
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

	public void contextMenuOpen(View sender,int viewflag){
		Log.i("context menu", "open");
		categoriesViewFlag=viewflag;
		sender.setLongClickable(false);
		registerForContextMenu(sender);
		openContextMenu(sender);

		// clearing values for browse 
		mStoreDetailsStart="0";
		mStoreDetailsEndLimit="20";
		mStoreDetailsCount = "0";	
		mIsLoadMore = false;
	}

	private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse,String categories){
		bundle.putString("returnResponse", mGetResponse);
		bundle.putString("categoriesflag", categories);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}

	Handler handler_response = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.obj!=null && msg.obj.toString().equalsIgnoreCase("currentlocation")){
				CurrentLocationFlag=true;
				if(msg.getData() != null){
					mLatitude=msg.getData().getDouble("latitude");
					mLongitude=msg.getData().getDouble("longitude");	
				}

				CurrentLocation.CurrentLocation_Latitude=String.valueOf(mLatitude);
				CurrentLocation.CurrentLocation_Longitude=String.valueOf(mLongitude);

				/*CurrentLocation.CurrentLocation_Latitude="33.5319";
				CurrentLocation.CurrentLocation_Longitude="-117.703";*/

				mDeviceCurrentLocationLatitude=String.valueOf(mLatitude);
				mDeviceCurrentLocationLongitude=String.valueOf(mLongitude);

				/*Log.i(TAG,"Latitude : "+mLatitude);
				Log.i(TAG,"Longitude : "+mLongitude);*/

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
							distance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
							mBeforScrollCenter=mGoogleMap.getCameraPosition().target;
							mCurrentZoom = mGoogleMap.getCameraPosition().zoom;
							if(storeNearThread!=null){
								if(storeNearThread.getStatus()==AsyncTask.Status.RUNNING){
									storeNearThread.cancel(true);
									storeNearThread = new StoreNearCurrentLocationAsyncThread(SlidingView.this,mGoogleMap,mShopListView,distance,"SlidingView",mFooterLayout,"PROGRESS",TAG,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,true);
									if(Build.VERSION.SDK_INT >= 11)
										storeNearThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
									else
										storeNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
								}else{
									storeNearThread = new StoreNearCurrentLocationAsyncThread(SlidingView.this,mGoogleMap,mShopListView,distance,"SlidingView",mFooterLayout,"PROGRESS",TAG,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,true);
									if(Build.VERSION.SDK_INT >= 11)
										storeNearThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
									else
										storeNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
								}	
							}else{
								storeNearThread = new StoreNearCurrentLocationAsyncThread(SlidingView.this,mGoogleMap,mShopListView,distance,"SlidingView",mFooterLayout,"PROGRESS",TAG,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,true);
								if(Build.VERSION.SDK_INT >= 11)
									storeNearThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
								else
									storeNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
							}
						}

						@Override
						public void onCancel() {
							// TODO Auto-generated method stub
						}
					});
				}else{
					Log.i("Load stores", "called for mapview not visible");
					mLocationGeoCordinates = new LatLng(Double.parseDouble(mDeviceCurrentLocationLatitude), Double.parseDouble(mDeviceCurrentLocationLongitude));
					CameraPosition myPosition = new CameraPosition.Builder().target(mLocationGeoCordinates).zoom(13).build();
					mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
					distance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
					if(distance == 0)
						distance = 0.0525;
					mBeforScrollCenter=myPosition.target;
					mCurrentZoom = mGoogleMap.getCameraPosition().zoom;
					if(storeNearThread!=null){
						if(storeNearThread.getStatus()==AsyncTask.Status.RUNNING){
							storeNearThread.cancel(true);
							storeNearThread = new StoreNearCurrentLocationAsyncThread(SlidingView.this,mGoogleMap,mShopListView,distance,"SlidingView",mFooterLayout,"PROGRESS",TAG,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,true);
							if(Build.VERSION.SDK_INT >= 11)
								storeNearThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
							else
								storeNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
						}else{
							storeNearThread = new StoreNearCurrentLocationAsyncThread(SlidingView.this,mGoogleMap,mShopListView,distance,"SlidingView",mFooterLayout,"PROGRESS",TAG,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,true);
							if(Build.VERSION.SDK_INT >= 11)
								storeNearThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
							else
								storeNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
						}
					}else{
						storeNearThread = new StoreNearCurrentLocationAsyncThread(SlidingView.this,mGoogleMap,mShopListView,distance,"SlidingView",mFooterLayout,"PROGRESS",TAG,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,true);
						if(Build.VERSION.SDK_INT >= 11)
							storeNearThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
						else
							storeNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
					}
				}
			}else{
				Bundle b = msg.getData();
				String key = b.getString("returnResponse");
				String CategoryFlag = b.getString("categoriesflag");
				mIsLoadMore = false;
				
				SlidingView.mCurrentLocation.setEnabled(true);
				
				if(key.trim().equals("success") ){
					Log.i(TAG,"Response Get Successfully");
					if(CategoryFlag.equals("2")){
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
									WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(SlidingView.mStoreDetailsStart)+i,mStoreinfo);
								}	

							}
						});

						if(SlidingView.mStoreDetailsStart.equalsIgnoreCase("0")){
							MenuUtilityClass.shopListView(SlidingView.this, mShopListView, "currentlocationnearstore","SlidingView","FromCategories",false,TAG);	//false for adding fresh adapter
						}else{
							MenuUtilityClass.shopListView(SlidingView.this, mShopListView, "currentlocationnearstore","SlidingView","FromCategories",true,TAG);	//true refreshing adapter
						}
						if(mFooterLayout != null && mShopListView.getFooterViewsCount() !=0 &&mShopListView.getAdapter() != null){
							Log.i(TAG, "Remove Footer View From shop List");
							mShopListView.removeFooterView(mFooterLayout);
						}

						ArrayList<Double> minDistance = new ArrayList<Double>();
						minDistance.clear();
						for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
							//Here we have to load store details from store_categories but that sevice does't have lat&lon so, for temporary we have load stores from storenearcurrentlocation(StoreLocator_ClassVariables)
							StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
							if(obj.storeCoordinates!=null){
								storeCount=storeCount+1;
								Log.i("Adding marker", storeCount+" ");
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

						Log.i(TAG,"StoreCount: "+storeCount);
						if(storeCount>1){
							//here we have to calculate and get nearest store from our current location to shown it in center of the mapview

							/***************************
							 * MapOverlays.mapOverlays.clear();
				    			MapOverlays.mapOverlays.add(itemizedOverlay);
							 * 
							 */

							//mMapView.invalidate();
						}else if(storeCount==1){
							/***************************
							 * MapOverlays.mapOverlays.clear();
				    			MapOverlays.mapOverlays.add(itemizedOverlay);
							 * 
							 */
							//mMapView.invalidate();
						}else if(storeCount==0){
							//MapOverlays.mapOverlays.clear();
							//mMapView.invalidate();
						}
						if(Integer.parseInt(SlidingView.mStoreDetailsCount)>20){
							mStoreDetailsStart = mStoreDetailsEndLimit;
							mStoreDetailsEndLimit = String.valueOf(Integer.parseInt(mStoreDetailsEndLimit)+20);
						}
						// If current location overlay cleared again add to it
						/*mMapView.getOverlays().add(locationoverlay);
				    		mMapView.postInvalidate();*/

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
				}else if(key.trim().equals("No Categories Available")  ){
					Toast.makeText(SlidingView.this, "No Categories Available", Toast.LENGTH_SHORT).show();
				}else if(key.trim().equals("No Stores Available")){
					mGoogleMap.clear();
					searchBar.setVisibility(View.VISIBLE);
					mCategoriesHeader.setVisibility(View.INVISIBLE);
					Animation custom_animation = AnimationUtils.loadAnimation(SlidingView.this, R.anim.search_in);
					CustomAnimationListener customAnimationListener = new CustomAnimationListener(mCategoriesHeader, "Visible");
					custom_animation.setAnimationListener(customAnimationListener);
					mCategoriesHeader.startAnimation(custom_animation);
					mCategoriesText.setText(mSelectedcategories);
					mSubCategoritesText.setText(mSelectedSubCategories);

					Toast.makeText(SlidingView.this, "No Stores Available", Toast.LENGTH_SHORT).show();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							WebServiceStaticArrays.mStoresLocator.clear();
							for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
								StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
								WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(SlidingView.mStoreDetailsStart)+i,mStoreinfo);
							}	

						}
					});

					if(SlidingView.mStoreDetailsStart.equalsIgnoreCase("0")){
						MenuUtilityClass.shopListView(SlidingView.this, mShopListView, "currentlocationnearstore","SlidingView","FromCategories",false,TAG);	//false for adding fresh adapter
					}else{
						MenuUtilityClass.shopListView(SlidingView.this, mShopListView, "currentlocationnearstore","SlidingView","FromCategories",true,TAG);	//true refreshing adapter
					}
					if(mFooterLayout != null && mShopListView.getFooterViewsCount() !=0 &&mShopListView.getAdapter() != null){
						Log.i(TAG, "Remove Footer View From shop List");
						mShopListView.removeFooterView(mFooterLayout);
					}
				}else if(key.trim().equals("failure")  ){
					Toast.makeText(SlidingView.this, "Response Error", Toast.LENGTH_SHORT).show();
				}else if(key.trim().equals("noresponse")||key.trim().equals("norecords") ){
					Toast.makeText(SlidingView.this, "No Data Available", Toast.LENGTH_SHORT).show();
				}	
			}
		}
	};

	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(SlidingView.this);
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

	private void storecategories(final String id,boolean showprogress,final String latitude,final String longitude){

		mIsLoadMore = true;
		if(mStoreDetailsCount.equalsIgnoreCase("0")&&showprogress==true){
			mProgressDialog.show();	
		}

		//Check whether search box is open or not
		if(mSearchStoreName.getVisibility()==View.VISIBLE){
			MenuBarSearchClickListener.CLICKFLAG=true;
			new FreezeClickListener(scrollView, leftMenu, rightMenu,searchBar, mSearchStoreName, shrinkSearch, 0,
					SlidingView.this,mMenuSearch,mHomePageFreezeView,mCancelStoreName,mSearchBoxContainer,mGoogleMap).closeSearchBox(TAG);
		}

		final Thread syncThread = new Thread(new Runnable() {

			@Override
			public void run() {

				Bundle bundle = new Bundle();
				String mGetResponse=null;
				Message msg_response = new Message();

				try{
					if(mConnectionAvailabilityChecking.ConnectivityCheck(SlidingView.this)){
						if(SlidingView.mStoreDetailsStart.equalsIgnoreCase("0")){
							WebServiceStaticArrays.mStoresLocator.clear();
							//distance = getMapViewRadius(mMapView.getMapCenter());
							mGetResponse=zouponswebservice.store_locator(latitude, longitude, "", id, "",distance,"",mStoreDetailsStart,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);	//new	
						}else{
							mGetResponse=zouponswebservice.store_locator(latitude, longitude, "", id, "",distance,"",mStoreDetailsStart,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);	//new
						}

						if(!Thread.currentThread().isInterrupted()){
							if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
								String mParsingResponse=parsingclass.parseStore_Locator(mGetResponse,"SlidingView"); 	//new
								Log.i(TAG,"mParsingResponse: "+mParsingResponse);
								if(mParsingResponse.equalsIgnoreCase("success")){
									for(int i=0;i<WebServiceStaticArrays.mOffsetStoreDetails.size();i++){
										StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
										if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("No Stores Available")*/){
											messageFlag1=true;
										}else{
											messageFlag1=false;
										}
									}
									if(!messageFlag1){
										updateHandler(bundle, msg_response, mParsingResponse, "3");	//send update to handler
									}else{
										updateHandler(bundle, msg_response, "No Stores Available", "3");	//send update to handlera
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

	/**
	 * Function to change the menu bar (List or Map) view button to "MAP VIEW" when navigate from zpay view to other pages.
	 * **/
	public static void setZpayMenuButtonChange(){
		if(SlidingView.mZpayList.getText().toString().equals("LIST VIEW")){
			SlidingView.mZpayList.setText("MAP VIEW");
			Context context = SlidingView.newContext;
			Drawable drawableTop = context.getResources().getDrawable(R.drawable.map_view_brown);
			SlidingView.mZpayList.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
			if(MenuBarListClickListener.clickFlag==true){
				MenuBarListClickListener.clickFlag=false;
			}else{
				MenuBarListClickListener.clickFlag=true;
			}
		}
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
				}else{
					//To know the mapview distance in pixels 
					//Log.i(TAG,"MapView ZoomLevel: "+mMapView.getZoomLevel());
					/*boolean flag = false;
					if(!isFirstTimeMapScroll){
						isFirstTimeMapScroll=true;
						mAfterScrollCenter=MapViewCenter.mMapViewCenter_GeoPoint;
					}else{
						mBeforScrollCenter=mAfterScrollCenter;
						mAfterScrollCenter=MapViewCenter.mMapViewCenter_GeoPoint;
					}
					Log.i(TAG,"mBeforScrollCenter: Latitude-"+mBeforScrollCenter.getLatitudeE6()+",Longitude-"+mBeforScrollCenter.getLongitudeE6());
					Log.i(TAG,"mAfterScrollCenter: Latitude-"+mAfterScrollCenter.getLatitudeE6()+",Longitude-"+mAfterScrollCenter.getLongitudeE6());

					if(mCurrentZoom != intent.getExtras().getInt("zoom_level")){
						mCurrentZoom =  intent.getExtras().getInt("zoom_level");
						Log.i("zoom check","varies");
						flag = true;
						//distance=getMapViewRadius(MapViewCenter.mMapViewCenter_GeoPoint);
					}else{
						Log.i("zoom check","same");
						//Logic to differentiate the first scroll and next scrolls of mapview
						//Function to calculate distance between MapView Before scroll and After scroll
						double mScrolledDistance=GetScrolledDistance(mBeforScrollCenter, mAfterScrollCenter);
						gp=MapViewCenter.mMapViewCenter_GeoPoint;
						//distance=getMapViewRadius(MapViewCenter.mMapViewCenter_GeoPoint);
						//Check the user scrolled distance is greater than our condition
						//flag=mapviewstoreloadingconstraint(mMapView.getZoomLevel(),mScrolledDistance);
					}

					if(flag){
						itemizedOverlay.hideAllCallOuts();

						CurrentLocation.CurrentLocation_Latitude=String.valueOf((double)MapViewCenter.mMapViewCenter_GeoPoint.getLatitudeE6() / (double)1E6);
						CurrentLocation.CurrentLocation_Longitude=String.valueOf((double)MapViewCenter.mMapViewCenter_GeoPoint.getLongitudeE6() / (double)1E6);
						if(SlidingView.mMapViewOnScrollViewFlag.equals("currentlocation")){
							Toast.makeText(SlidingView.this, "Loading...", Toast.LENGTH_SHORT).show();
							mStoreDetailsStart="0";
							mStoreDetailsEndLimit="20";
							mStoreDetailsCount = "0";	
							mIsLoadMore = false;
							if(mShopListView.getFooterViewsCount() == 0){
								mShopListView.addFooterView(mFooterLayout);	
							}
							StoreNearCurrentLocationAsyncThread storenearThread = new StoreNearCurrentLocationAsyncThread(SlidingView.this,itemizedOverlay,mMapView,mShopListView,gp,distance,"SlidingView",mFooterLayout,"NOPROGRESS",TAG,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
							storenearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
						}else if(SlidingView.mMapViewOnScrollViewFlag.equals("search")){
							Toast.makeText(SlidingView.this, "Loading...", Toast.LENGTH_SHORT).show();
							//Set MenuSearchBarClickListener CLICKFLAG to true 
							MenuBarSearchClickListener.CLICKFLAG=true;
							mStoreDetailsStart="0";
							mStoreDetailsEndLimit="20";
							mStoreDetailsCount = "0";	
							mIsLoadMore = false;
							if(mShopListView.getFooterViewsCount() == 0){
								mShopListView.addFooterView(mFooterLayout);	
							}
							StoresLocationAsynchThread storeslocationasynchthread = new StoresLocationAsynchThread(SlidingView.this,mMenuSearch,shrinkSearch,mSearchStoreName,searchBar,mHomePageFreezeView,itemizedOverlay,mMapView,mShopListView,distance,"SlidingView",mCancelStoreName,mSearchBoxContainer,"",mFooterLayout,"NOPROGRESS",gp,false,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
							storeslocationasynchthread.execute(search_storeId,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
						}else if(SlidingView.mMapViewOnScrollViewFlag.equals("browse")){
							Toast.makeText(SlidingView.this, "Loading...", Toast.LENGTH_SHORT).show();
							mStoreDetailsStart="0";
							mStoreDetailsEndLimit="20";
							mStoreDetailsCount = "0";	
							mIsLoadMore = false;
							if(mShopListView.getFooterViewsCount() == 0){
								mShopListView.addFooterView(mFooterLayout);	
							}
							storecategories(store_categoriesId,false,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
						}
					}else{
						Log.i(TAG,"Sorry Not Loading");
					}*/
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};	

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

		/*float mDistanceMetres = mCurrentMapViewCenter.distanceTo(mCurrentMapViewNorthWestEdge);
		double mDistanceBtwnCenterAndNorthWest = mDistanceMetres*0.000621371;
		Log.i(TAG,"Scrolled Distance: "+mDistanceBtwnCenterAndNorthWest);*/
		return mDistanceBtwnCenterAndNorthWest;
	}

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

	private LatLng GetMapViewNorthWestGeoPoint() {
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

	public static boolean CheckKeyboardVisibility(final View activityRootView){

		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				Rect r = new Rect();
				//r will be populated with the coordinates of your view that area still visible.
				activityRootView.getWindowVisibleDisplayFrame(r);
				int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
				if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
					keyboardstate=true;
				}else{
					keyboardstate=false;
				}
			}
		});
		return keyboardstate; 
	}

	public boolean mapviewstoreloadingconstraint(int zoomlevel,double scrolleddistance){
		double needDistance=distance/2; 
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

	public void showCategoriesHeaderView(){
		// To show selected categories and sub categories in header view
		searchBar.setVisibility(View.VISIBLE);
		mCategoriesHeader.setVisibility(View.INVISIBLE);
		Animation custom_animation = AnimationUtils.loadAnimation(SlidingView.this, R.anim.search_in);
		CustomAnimationListener customAnimationListener = new CustomAnimationListener(mCategoriesHeader, "Visible");
		custom_animation.setAnimationListener(customAnimationListener);
		mCategoriesHeader.startAnimation(custom_animation);
		mCategoriesText.setText(mSelectedcategories);
		mSubCategoritesText.setText(mSelectedSubCategories);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		finish();
		mGoogleMap = null;
		Log.i(TAG,"onDestroy"); //$NON-NLS-1$
	}

	@Override
	public void onPause() {
		super.onPause();
		isActivity_live = false;
		Log.i("sliding view", "on pause");
		// To disable current location overlay
		//locationoverlay.disableMyLocation();
		unregisterReceiver(mReceiver);
		Log.i(TAG,"onPause"); //$NON-NLS-1$
		if(mNotificationSync!=null){
			mNotificationSync.cancelAlarm();
		}
	}

	@Override
	protected void onUserLeaveHint() {
		Log.i(TAG,Messages.getString("onUserLeaveHint"));
		new RefreshZoupons().isApplicationGoneBackground(SlidingView.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG,"onResume"); //$NON-NLS-1$
		/*if(!getIntent().hasExtra("fromlogin")){
			// To enable current location overlay
			locationoverlay.enableMyLocation();
		}*/
		if(mConnectionAvailabilityChecking.ConnectivityCheck(SlidingView.this)){
			if(mGoogleMap == null && checkPlayServices()){
				mMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
				if(mUpdateGooglePlayServicesDialog != null && mUpdateGooglePlayServicesDialog.isShowing())
					mUpdateGooglePlayServicesDialog.dismiss();
				initilizeGoogleMap();
				getCurrentLocation();
			}
		}else{
			Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
		}

		isActivity_live = true;
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		new CheckUserSession(SlidingView.this).checkIfSessionExpires();
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(SlidingView.this);
		mNotificationSync.setRecurringAlarm();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(SlidingView.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		if(mGoogleMap != null){
			Log.i("Google map", "camera change listner set");
			mGoogleMap.setOnCameraChangeListener(SlidingView.this);
		}
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		Log.i(TAG,"onRestart"); //$NON-NLS-1$
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i(TAG,"onStart"); //$NON-NLS-1$
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i(TAG,"onStop"); //$NON-NLS-1$
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
		MapViewCenter.mMapViewCenter_GeoPoint = cameraposition.target;
		float zoom_during_scroll = cameraposition.zoom;
		boolean flag;
		if(!isFirstTimeMapScroll){
			isFirstTimeMapScroll=true;
			mAfterScrollCenter=MapViewCenter.mMapViewCenter_GeoPoint;
		}else{
			mBeforScrollCenter=mAfterScrollCenter;
			mAfterScrollCenter=MapViewCenter.mMapViewCenter_GeoPoint;
		}

		if(mCurrentZoom != zoom_during_scroll){
			mCurrentZoom =  zoom_during_scroll;
			Log.i("zoom check","varies");
			flag = true;
			distance=getMapViewRadius(MapViewCenter.mMapViewCenter_GeoPoint);
		}else{
			Log.i("zoom check","same");
			//Logic to differentiate the first scroll and next scrolls of mapview
			//Function to calculate distance between MapView Before scroll and After scroll
			double mScrolledDistance=GetScrolledDistance(mBeforScrollCenter, mAfterScrollCenter);
			//gp=MapViewCenter.mMapViewCenter_GeoPoint;
			distance=getMapViewRadius(MapViewCenter.mMapViewCenter_GeoPoint);
			//Check the user scrolled distance is greater than our condition
			flag=mapviewstoreloadingconstraint((int)mGoogleMap.getCameraPosition().zoom,mScrolledDistance);
		}
		if(flag){
			
			//mCurrentLocation.setEnabled(false);
			
			WebServiceStaticArrays.mStoresLocator.clear();
			mShopListView.setAdapter(new ShopNearestList_Adapter(SlidingView.this));
			
			CurrentLocation.CurrentLocation_Latitude=String.valueOf(MapViewCenter.mMapViewCenter_GeoPoint.latitude);
			CurrentLocation.CurrentLocation_Longitude=String.valueOf(MapViewCenter.mMapViewCenter_GeoPoint.longitude);
			if(SlidingView.mMapViewOnScrollViewFlag.equals("currentlocation")){
				//Toast.makeText(SlidingView.this, "Loading...", Toast.LENGTH_SHORT).show();
				mStoreDetailsStart="0";
				mStoreDetailsEndLimit="20";
				mStoreDetailsCount = "0";	
				mIsLoadMore = false;
				/*if(mShopListView.getFooterViewsCount() == 0){
					mShopListView.addFooterView(mFooterLayout);	
				}*/
				
				if(storeNearThread.getStatus()==AsyncTask.Status.RUNNING){
					storeNearThread.cancel(true);
					storeNearThread = new StoreNearCurrentLocationAsyncThread(SlidingView.this,mGoogleMap,mShopListView,distance,"SlidingView",mFooterLayout,"NOPROGRESS",TAG,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,false);
					storeNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
				}else{
					storeNearThread = new StoreNearCurrentLocationAsyncThread(SlidingView.this,mGoogleMap,mShopListView,distance,"SlidingView",mFooterLayout,"NOPROGRESS",TAG,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,false);
					storeNearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
				}
			}else if(SlidingView.mMapViewOnScrollViewFlag.equals("search")){
				//Toast.makeText(SlidingView.this, "Loading...", Toast.LENGTH_SHORT).show();
				//Set MenuSearchBarClickListener CLICKFLAG to true 
				MenuBarSearchClickListener.CLICKFLAG=true;
				mStoreDetailsStart="0";
				mStoreDetailsEndLimit="20";
				mStoreDetailsCount = "0";	
				mIsLoadMore = false;
				/*if(mShopListView.getFooterViewsCount() == 0){
					mShopListView.addFooterView(mFooterLayout);	
				}*/
				
				storesLocationAsynchThread = new StoresLocationAsynchThread(SlidingView.this,mMenuSearch,shrinkSearch,mSearchStoreName,searchBar,mHomePageFreezeView,mGoogleMap,mShopListView,distance,"SlidingView",mCancelStoreName,mSearchBoxContainer,"",mFooterLayout,"NOPROGRESS",false,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
				if(storesLocationAsynchThread.getStatus()==AsyncTask.Status.RUNNING){
					Log.i(TAG,"Search Store Thread Canceled");
					storesLocationAsynchThread.cancel(true);
					storesLocationAsynchThread.execute(search_storeId,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
				}else{
					storesLocationAsynchThread.execute(search_storeId,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
				}
			}else if(SlidingView.mMapViewOnScrollViewFlag.equals("browse")){
				//Toast.makeText(SlidingView.this, "Loading...", Toast.LENGTH_SHORT).show();
				mStoreDetailsStart="0";
				mStoreDetailsEndLimit="20";
				mStoreDetailsCount = "0";	
				mIsLoadMore = false;
				/*if(mShopListView.getFooterViewsCount() == 0){
					mShopListView.addFooterView(mFooterLayout);	
				}*/
				storecategories(store_categoriesId,false,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
			}
		}else{
			Log.i(TAG,"Sorry Not Loading");
		}

	}

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

	void showErrorDialog(int code) {
		mUpdateGooglePlayServicesDialog = GooglePlayServicesUtil.getErrorDialog(code, this,REQUEST_CODE_RECOVER_PLAY_SERVICES);
		mUpdateGooglePlayServicesDialog.show();
	}


}