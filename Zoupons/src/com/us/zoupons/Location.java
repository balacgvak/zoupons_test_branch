/**
 * 
 */
package com.us.zoupons;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.CurrentLocation;
import com.us.zoupons.ClassVariables.MapViewCenter;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.android.AsyncThreadClasses.StoreLocatorTask;
import com.us.zoupons.android.mapviewcallouts.CustomMapCalloutClickListener;
import com.us.zoupons.android.mapviewcallouts.CustomMapViewCallout;
import com.us.zoupons.notification.CustomNotificationAdapter;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotificationItemClickListener;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;

/**
 * Class to hold location of store name in listview and mapview
 */
public class Location extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener,OnCameraChangeListener{

	public static String TAG="Location";

	public static MyHorizontalScrollView scrollView;
	public static View leftMenu;
	public static View rightMenu;
	View app;

	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	public LinearLayout btnSlide;
	public RelativeLayout btnLogout; 
	public ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	public Button mNotificationCount;
	private int MenuFlag_Locations=0;

	//LeftMenu
	public static LinearLayout mHome,mZpay,mInvoiceCenter,mGiftcards,mManageCards,mReceipts,mMyFavourites,mMyFriends,mChat,mRewards,mSettings,mLogout;
	public static TextView mHomeText,mZpayText,mInvoiceCenterText,mGiftCardsText,mManageCardsText,mReceiptsText,mMyFavoritesText,mMyFriendsText,mChatText,mRewardsText,mSettingsText,mLogoutText;

	//RightMenu
	public static LinearLayout mInfo_RightMenu,mMobilePay_RightMenu,mGiftCards_RightMenu,mDeals_RightMenu,mCoupons_RightMenu,mSocial_RightMenu,mReviews_RightMenu,mLocations_RightMenu,mPhotos_RightMenu,mVideos_RightMenu,mContactStore_RightMenu,mFavorite_RightMenu;
	public static TextView mInfoText_RightMenu,mMobilePayText_RightMenu,mGiftCardsText_RightMenu,mDealsText_RightMenu,mCouponsText_RightMenu,mSocialText_RightMenu,mReviewsText_RightMenu,mLocationsText_RightMenu,mPhotosText_RightMenu,mVideosText_RightMenu,mContactStoreText_RightMenu,mFavoriteText_RightMenu,mStoreName_RightMenu,mStoreLocation_RightMenu;
	/*public static ImageView mStoreImage_RightMenu;*/
	public static ImageView mInfoImage_RightMenu,mMobilePayImage_RightMenu,mGiftCardsImage_RightMenu,mDealsImage_RightMenu,mCouponsImage_RightMenu,mSocialImage_RightMenu,mReviewsImage_RightMenu,mLocationsImage_RightMenu,mPhotosImage_RightMenu,mVideosImage_RightMenu,mContactStoreImage_RightMenu,mFavoriteImage_RightMenu;
	public static LinearLayout mMenuBarContainer,mMenuList;
	public static Button mLocationPageFreezeView;
	public ImageView mRighMenuHolder;
	public ImageView mMenuBarListImage;
	public TextView mMenuBarListText;

	public ViewGroup tabBar,titleBar,menuBar,middleView,leftMenuItems,rightMenuItems;
	public int mScreenWidth;
	public double mMenuWidth;
	public static ListView mLocationListView;
	public LinearLayout mListView;
	public RelativeLayout mMapViewContainer;
	public TextView mHeaderStoreName;
	Drawable drawable;
	int storeCount;
	//Notification Pop up layout declaration
	private View mPopUpLayout;
	private ListView mNotificationList;
	private ScheduleNotificationSync mNotificationSync;
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	public ImageView mTabBarLoginChoice;

	//Get ScrollDistance Variables
	public LatLng mBeforScrollCenter,mAfterScrollCenter;
	public boolean isFirstTimeMapScroll;
	private float mCurrentZoom=0;
	double distance = 0;
	//public static MyLocationOverlay locationOverlay;
	public static ScrollView leftMenuScrollView,rightMenuScrollView;
	private ProgressDialog mProgressDialog=null;
	LocationManager manager;
	private GoogleMap mGoogleMap;
	public UiSettings mGoogleMapUISettings;
	private LocationClient mLocationClient;
	private Dialog mUpdateGooglePlayServicesDialog = null;
	private final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1;
	//MyCurrentLocation mMyCurrentLocation;
	//public static boolean useGPSAlertFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);

		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();

		leftMenu = inflater.inflate(R.layout.horz_scroll_menu, null);
		rightMenu = inflater.inflate(R.layout.rightmenu, null);
		app = inflater.inflate(R.layout.location, null);

		tabBar = (ViewGroup) app.findViewById(R.id.tabBar);
		titleBar = (ViewGroup) app.findViewById(R.id.titlebar);
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
		// Notification and log out variables

		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(Location.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);

		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(Location.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();

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
		mRighMenuHolder = (ImageView) titleBar.findViewById(R.id.location_rightmenu);
		mHeaderStoreName = (TextView) titleBar.findViewById(R.id.store_title_textId);
		mHeaderStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);

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

		mMenuBarContainer=(LinearLayout) menuBar.findViewById(R.id.menubarcontainer);
		mMenuList=(LinearLayout) menuBar.findViewById(R.id.menubar_list);

		mMenuBarListImage=(ImageView) menuBar.findViewById(R.id.menubar_list_image);
		mMenuBarListText=(TextView) menuBar.findViewById(R.id.menubar_list_text);
		mMenuBarListText.setTypeface(mZouponsFont);

		mMapViewContainer=(RelativeLayout) middleView.findViewById(R.id.location_mapview);
		//mMapView=(TapControlledMapView) middleView.findViewById(R.id.zouponsLocationsMapView);
		mLocationPageFreezeView=(Button) app.findViewById(R.id.freezeview);

		mListView=(LinearLayout) middleView.findViewById(R.id.locationList);
		mLocationListView = (ListView) middleView.findViewById(R.id.locationListView);

		mProgressDialog=new ProgressDialog(Location.this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage("Loading ...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
		mGoogleMap = null;
		/*// Current location overlay declaration..
		locationOverlay = new MyLocationOverlay(Location.this, mMapView);
		mMapView.getOverlays().add(locationOverlay);
		mMapView.postInvalidate();*/

		//Current Location declaration
		//mMyCurrentLocation = new MyCurrentLocation();
		/*manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			AlertMessageNoGps();
		}else{
			mProgressDialog.show();
			locationOverlay.runOnFirstFix(new Runnable() {

				@Override
				public void run() {
					Log.i("current location source", "location from first fix");
					Message msg = new Message();
					msg.obj = "currentlocation";
					Bundle mLocationdetails = new Bundle();
					mLocationdetails.putDouble("latitude", locationOverlay.getMyLocation().getLatitudeE6()/1E6);
					mLocationdetails.putDouble("longitude", locationOverlay.getMyLocation().getLongitudeE6()/1E6);
					msg.setData(mLocationdetails);
					handler_response.sendMessage(msg);
				}
			});
		}*/

		/*mMapView.setBuiltInZoomControls(false);
		drawable = getResources().getDrawable(R.drawable.ant3);
		itemizedOverlay = new MapViewItemizedOverlay(drawable, mMapView,TAG,"location");
		MapController mapcontroller = mMapView.getController();
		mapcontroller.setZoom(15);*/

		mHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftcards.setOnClickListener(new MenuItemClickListener(leftMenuItems,Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mManageCards.setOnClickListener(new MenuItemClickListener(leftMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);

				if(NormalPaymentAsyncTask.mCountDownTimer!=null){
					NormalPaymentAsyncTask.mCountDownTimer.cancel();
					NormalPaymentAsyncTask.mCountDownTimer = null;
					Log.i(TAG,"Timer Stopped Successfully");
				}

				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(Location.this).execute();
				Intent intent_logout = new Intent(Location.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_logout);
				finish();
			}
		});

		/*ClickListeners for all rightmenu items*/

		mInfo_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mMobilePay_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCards_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mDeals_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mCoupons_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mSocial_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mReviews_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mLocations_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mPhotos_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mVideos_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mContactStore_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFavorite_RightMenu.setOnClickListener(new MenuItemClickListener(rightMenuItems, Location.this,POJOMainMenuActivityTAG.TAG=TAG));

		//Function to set right menu items status
		SetRightMenuStatus(Location.this);

		/*mMapView.setOnSingleTapListener(new OnSingleTapListener() {

			@Override
			public boolean onSingleTap(MotionEvent e) {
				itemizedOverlay.hideAllCallOuts();
				return false;
			}
		});*/

		btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, rightMenu, MenuFlag_Locations=1, mLocationPageFreezeView));
		mRighMenuHolder.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, rightMenu, MenuFlag_Locations=2, mLocationPageFreezeView));
		mLocationPageFreezeView.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, rightMenu, MenuFlag_Locations, mLocationPageFreezeView));
		mMenuList.setOnClickListener(new MenuBarListClickListener(Location.this,mMapViewContainer,mListView,middleView,mMenuBarListText,mMenuBarListImage,0,"Location"));

		//MapViewCenter.mMapViewCenter_GeoPoint=mMapView.getMapCenter();

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
				mNotificationList.setAdapter(new CustomNotificationAdapter(Location.this));
				mNotificationList.setOnItemClickListener(new NotificationItemClickListener());

				mPopUpWindow.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						mCalloutTriangleImage.setVisibility(View.INVISIBLE);
					}
				});
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

		mLogoutImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(Location.this).execute();
				Intent intent_logout = new Intent(Location.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				finish();
				startActivity(intent_logout);
			}
		});
	}

	
	Handler handler_response = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			mProgressDialog.dismiss();
			if(msg.obj!=null && msg.obj.toString().equalsIgnoreCase("currentlocation")){
				if(msg.getData() != null){
					MapViewCenter.LoginCurrentLocationLatitude=msg.getData().getDouble("latitude");
					MapViewCenter.LoginCurrentLocationLongitude=msg.getData().getDouble("longitude");
					StoreLocatorTask mStoreLocator = new StoreLocatorTask(Location.this,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude,/*EventFlag,*/RightMenuStoreId_ClassVariables.mStoreID,mGoogleMap,mLocationListView,"location","location",
							mMapViewContainer,mListView,middleView,mMenuBarListText,mMenuBarListImage,0,"PROGRESS");
					mStoreLocator.execute(RightMenuStoreId_ClassVariables.mSelectedStore_lat,RightMenuStoreId_ClassVariables.mSelectedStore_long);
				}
			}
		}
	};
	
	/*public LocationResult mLocationResult = new LocationResult() {

		@Override
		public void gotLocation(android.location.Location location) {
			Log.i(TAG,"Location Value : "+location);
			if(location != null){
				MapViewCenter.LoginCurrentLocationLatitude=location.getLatitude();
				MapViewCenter.LoginCurrentLocationLongitude=location.getLongitude();
			}
		}
	};*/

	private void AlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
		.setCancelable(true)
		.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog,final int id) {
				//useGPSAlertFlag=true;
				startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				dialog.cancel();
			}
		})
		.setNegativeButton("Enable wireless networks", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				if(!manager.isProviderEnabled( LocationManager.NETWORK_PROVIDER )){
					startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				}else{
					Toast.makeText(getApplicationContext(), "Wireless networks are already enabled.", Toast.LENGTH_SHORT).show();
				}
				//useGPSAlertFlag=true;
				dialog.cancel();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
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

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's width.
	 */
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

			rightMenuScrollView.fullScroll(View.FOCUS_UP);
			rightMenuScrollView.pageScroll(View.FOCUS_UP);

			Context context = leftMenu.getContext();
			int menuWidth = leftMenu.getMeasuredWidth();

			InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

			// Ensure menu is visible
			leftMenu.setVisibility(View.VISIBLE);
			if (!MenuOutClass.LOCATION_MENUOUT) {
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
			MenuOutClass.LOCATION_MENUOUT = !MenuOutClass.LOCATION_MENUOUT;
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
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		if(mNotificationSync!=null){
			mNotificationSync.cancelAlarm();
		}
		//locationOverlay.disableCompass();
	}

	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(Location.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}


	@Override
	protected void onResume() {
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
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
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(Location.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(Location.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(Location.this);
		mLogoutSession.setLogoutTimerAlarm();
		super.onResume();
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
		//mMyCurrentLocation.removeUpdates(); 	//Function to remove GPS Updates
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	public static void SetRightMenuStatus(Context context){
		// To set store name in right menu
		mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
		mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);

		if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("no")){  
			Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
		}else{
			Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
		}
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
	}

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
		/*double mDistanceMiles = mDistanceMetres*0.000621371;
		Log.i(TAG,"Scrolled Distance: "+mDistanceMiles);*/
	    float mDistanceMetres = mBeforeScrollMapViewCenter.distanceTo(mAfterScrollMapViewCenter);
		double mDistanceMiles = mDistanceMetres*0.000621371;
		//Log.i(TAG,"Scrolled Distance: "+mDistanceMiles);
		//int mDistance =(int) mDistanceMiles;
      	return mDistanceMiles;
	}

	private LatLng GetMapViewNorthWestGeoPoint() {
		LatLng mNorthwestCoordinates = null;
		try{
			com.google.android.gms.maps.Projection projection = mGoogleMap.getProjection();
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

	public boolean mapviewstoreloadingconstraint(int zoomlevel,double scrolleddistance){
		double needDistance=distance/2; 
		return conditionCheck(scrolleddistance,needDistance);
	}

	public boolean conditionCheck(double scrolleddistance,double needdistance){
		boolean conditionCheckFlag = false;
		if(scrolleddistance>needdistance){
			conditionCheckFlag = true;
		}
		return conditionCheckFlag;
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
				}else{ //Loading Stores while mapview scroll
					/*//To know the mapview distance in pixels 
					Log.i(TAG,"MapView ZoomLevel: "+mMapView.getZoomLevel());
					boolean flag;
					if(!isFirstTimeMapScroll){
						isFirstTimeMapScroll=true;
						mAfterScrollCenter=MapViewCenter.mMapViewCenter_GeoPoint;
					}else{
						mBeforScrollCenter=mAfterScrollCenter;
						mAfterScrollCenter=MapViewCenter.mMapViewCenter_GeoPoint;
						Log.i(TAG,"mBeforScrollCenter: Latitude-"+mBeforScrollCenter.getLatitudeE6()+",Longitude-"+mBeforScrollCenter.getLongitudeE6());
					}
					Log.i(TAG,"mAfterScrollCenter: Latitude-"+mAfterScrollCenter.getLatitudeE6()+",Longitude-"+mAfterScrollCenter.getLongitudeE6());

					if(mCurrentZoom != intent.getExtras().getInt("zoom_level")){
						mCurrentZoom =  intent.getExtras().getInt("zoom_level");
						Log.i("zoom check","varies");
						flag = true;
						distance=getMapViewRadius(MapViewCenter.mMapViewCenter_GeoPoint);
					}else{
						Log.i("zoom check","same");
						//Logic to differentiate the first scroll and next scrolls of mapview
						//Function to calculate distance between MapView Before scroll and After scroll
						double mScrolledDistance=GetScrolledDistance(mBeforScrollCenter, mAfterScrollCenter);
						gp=MapViewCenter.mMapViewCenter_GeoPoint;
						distance=getMapViewRadius(MapViewCenter.mMapViewCenter_GeoPoint);
						//Check the user scrolled distance is greater than our condition
						flag=mapviewstoreloadingconstraint(mMapView.getZoomLevel(),mScrolledDistance);
					}

					if(flag){
						itemizedOverlay.hideAllCallOuts();

						RightMenuStoreId_ClassVariables.mSelectedStore_lat=String.valueOf((double)MapViewCenter.mMapViewCenter_GeoPoint.getLatitudeE6() / (double)1E6);
						RightMenuStoreId_ClassVariables.mSelectedStore_long=String.valueOf((double)MapViewCenter.mMapViewCenter_GeoPoint.getLongitudeE6() / (double)1E6);

						StoreLocatorTask mStoreLocator = new StoreLocatorTask(Location.this,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude,EventFlag,RightMenuStoreId_ClassVariables.mStoreID,itemizedOverlay,mMapView,mLocationListView,"location","location",
								mMapViewContainer,mListView,middleView,mMenuBarListText,mMenuBarListImage,0,"NOPROGRESS");
						mStoreLocator.execute(RightMenuStoreId_ClassVariables.mSelectedStore_lat,RightMenuStoreId_ClassVariables.mSelectedStore_long);
					}else{
						Log.i(TAG,"Location Scroll Store Not Loading");
					}*/
				}					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};
	@Override
	public void onCameraChange(CameraPosition cameraposition) {
		// TODO Auto-generated method stub
		//To know the mapview distance in pixels 
		MapViewCenter.mMapViewCenter_GeoPoint = cameraposition.target;
		float zoom_during_scroll = cameraposition.zoom;
		boolean flag;
		if(!isFirstTimeMapScroll){
			isFirstTimeMapScroll=true;
			mAfterScrollCenter=MapViewCenter.mMapViewCenter_GeoPoint;
		}else{
			mBeforScrollCenter=mAfterScrollCenter;
			mAfterScrollCenter=MapViewCenter.mMapViewCenter_GeoPoint;
			//Log.i(TAG,"mBeforScrollCenter: Latitude-"+mBeforScrollCenter.latitude+",Longitude-"+mBeforScrollCenter.longitude);
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
			RightMenuStoreId_ClassVariables.mSelectedStore_lat=String.valueOf(MapViewCenter.mMapViewCenter_GeoPoint.latitude);
			RightMenuStoreId_ClassVariables.mSelectedStore_long=String.valueOf(MapViewCenter.mMapViewCenter_GeoPoint.longitude);

			StoreLocatorTask mStoreLocator = new StoreLocatorTask(Location.this,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude,RightMenuStoreId_ClassVariables.mStoreID,mGoogleMap,mLocationListView,"location","location",
					mMapViewContainer,mListView,middleView,mMenuBarListText,mMenuBarListImage,0,"NOPROGRESS");
			mStoreLocator.execute(RightMenuStoreId_ClassVariables.mSelectedStore_lat,RightMenuStoreId_ClassVariables.mSelectedStore_long);
		}else{
			Log.i(TAG,"Location Scroll Store Not Loading");
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