package com.us.zoupons.zpay;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.us.zoupons.DoneOnEditorActionListener;
import com.us.zoupons.FreezeClickListener;
import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuBarListClickListener;
import com.us.zoupons.MenuBarQRCodeClickListener;
import com.us.zoupons.MenuBarSearchClickListener;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.Messages;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.CurrentLocation;
import com.us.zoupons.ClassVariables.MapViewCenter;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.ClassVariables.Search_ClassVariables;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.android.AsyncThreadClasses.FavouritesTaskLoader;
import com.us.zoupons.android.AsyncThreadClasses.StoreNearCurrentLocationAsyncThread;
import com.us.zoupons.android.AsyncThreadClasses.StoresLocationAsynchThread;
import com.us.zoupons.android.AsyncThreadClasses.StoresQRCodeAsynchThread;
import com.us.zoupons.android.lib.integrator.IntentIntegrator;
import com.us.zoupons.android.lib.integrator.IntentResult;
import com.us.zoupons.android.listview.inflater.classes.CustomStoreAdapter;
import com.us.zoupons.android.mapviewcallouts.CustomMapCalloutClickListener;
import com.us.zoupons.android.mapviewcallouts.CustomMapViewCallout;
import com.us.zoupons.invoice.InvoiceApproval;
import com.us.zoupons.notification.CustomNotificationAdapter;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotificationItemClickListener;
import com.us.zoupons.notification.ScheduleNotificationSync;

public class zpay_step1 extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener,OnCameraChangeListener{

	public static MyHorizontalScrollView scrollView;
	public static View leftMenu;
	public static View app;
	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	public LinearLayout btnSlide;
	public RelativeLayout btnLogout; 
	public ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	public Button mNotificationCount;
	public static String TAG = "zpay_step1";
	Handler handler = new Handler();
	//LeftMenu
	public static LinearLayout mHome,mZpay,mInvoiceCenter,mGiftcards,mManageCards,mReceipts,mMyFavourites,mMyFriends,mChat,mRewards,mSettings,mLogout;
	public static TextView mHomeText,mZpayText,mInvoiceCenterText,mGiftCardsText,mManageCardsText,mReceiptsText,mMyFavoritesText,mMyFriendsText,mChatText,mRewardsText,mSettingsText,mLogoutText;

	public double mLatitude,mLongitude,mMenuWidth,mZpayMenuWidth;
	public int mScreenWidth;
	public ViewGroup tabBar,menuBar,middleView,leftMenuItems;
	public static ViewGroup searchBar;
	public static Button mZpayStep1FreezeView,mZpayStep1IntialFreezeView;
	public static int MenuFlag_ZpayStep1=0;
	public static LinearLayout mZpayMenuBarContainer;
	public LinearLayout mZpayMenuBarZsend,mZpayMenuBarZapprove;
	public static AutoCompleteTextView mSearchStoreName;
	public Button mZpayQRCode,mZpayFavorites,mZpaySearch;
	public static Button mZpayList;
	public static AnimationSet shrinkSearch;

	public RelativeLayout mMapViewContainer;
	public static Button mCurrentLocation;
	public LinearLayout mListViewContainer;
	public ListView mShopListView;
	//TapControlledMapView mMapView; // use the custom TapControlledMapView
	Drawable drawable;
	//MapViewItemizedOverlay itemizedOverlay;
	//GeoPoint gp,singleStore;
	private boolean CurrentLocationFlag=false;
	CustomStoreAdapter adapter;
	//For Dynamic List Update
	private View mFooterLayout;
	private TextView mFooterText;
	public static boolean mIsZPAYLoadMore;
	LayoutInflater mInflater;
	public Button mCancelStoreName;
	public RelativeLayout mSearchBoxContainer;
	public String mPageFlag;
	public static int left=0;

	public static String mZPAYStoreDetailsStart="0",mZPAYStoreDetailsEndLimit="20",mZPAYStoreDetailsCount = "0";	
	double distance = 0;
	private String search_storeId="";
	private String QRCoderesult="";

	//Get ScrollDistance Variables
	public LatLng mBeforScrollCenter,mAfterScrollCenter,mLocationGeoCordinates;
	public boolean isFirstTimeMapScroll;
	public static String mMapViewOnScrollViewFlag;
	//Notification Pop up layout declaration
	private View mPopUpLayout;
	private ListView mNotificationList;
	private ScheduleNotificationSync mNotificationSync;
	private float mCurrentZoom=0;

	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	public ImageView mTabBarLoginChoice;
	private String mDeviceCurrentLocationLatitude="",mDeviceCurrentLocationLongitude="";
	public static ScrollView leftMenuScrollView;
	private GoogleMap mGoogleMap;
	public UiSettings mGoogleMapUISettings;
	private LocationClient mLocationClient;
	private Dialog mUpdateGooglePlayServicesDialog = null;
	private final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1;
	StoreNearCurrentLocationAsyncThread storenearThread;
	StoresLocationAsynchThread storeslocationasynchthread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);

		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();
		shrinkSearch = new AnimationSet(true);

		//Call function to get width of the screen
		mScreenWidth=getScreenWidth(); 
		if(mScreenWidth>0){	//To fix ZPay menubar items width
			mZpayMenuWidth=mScreenWidth/2;
		}

		//Flag to differentiate the control from zpay or giftcard purchase.
		mPageFlag=getIntent().getExtras().getString("pageflag")!=null?getIntent().getExtras().getString("pageflag"):"";

		leftMenu = inflater.inflate(R.layout.horz_scroll_menu, null);
		app = inflater.inflate(R.layout.zpay_step1, null);

		tabBar = (ViewGroup) app.findViewById(R.id.step1_tabBar);
		searchBar = (ViewGroup) app.findViewById(R.id.step1_searchbar);
		menuBar = (ViewGroup) app.findViewById(R.id.step1_menubar);
		middleView = (ViewGroup) app.findViewById(R.id.step1_middleview);
		leftMenuItems = (ViewGroup) leftMenu.findViewById(R.id.menuitems);

		leftMenuScrollView = (ScrollView) leftMenu.findViewById(R.id.leftmenu_scrollview);
		leftMenuScrollView.fullScroll(View.FOCUS_UP);
		leftMenuScrollView.pageScroll(View.FOCUS_UP);

		btnSlide = (LinearLayout) tabBar.findViewById(R.id.step1_BtnSlide);
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(zpay_step1.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);        

		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(zpay_step1.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();

		final View[] children = new View[] { leftMenu, app};

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

		mZpayStep1FreezeView=(Button) app.findViewById(R.id.step1_freezeview);
		mZpayStep1IntialFreezeView=(Button) app.findViewById(R.id.step1_intial_freezeview);
		mZpayStep1IntialFreezeView.setVisibility(View.VISIBLE);
		mSearchStoreName = (AutoCompleteTextView) searchBar.findViewById(R.id.step1_search_storename);
		mCancelStoreName = (Button) searchBar.findViewById(R.id.step1_clear_storename);
		mCancelStoreName.setTypeface(mZouponsFont);
		mSearchBoxContainer=(RelativeLayout) searchBar.findViewById(R.id.step1_searchbar_container);

		mZpayQRCode=(Button) searchBar.findViewById(R.id.step1_qrcodebtn);
		mZpayQRCode.setTypeface(mZouponsFont);
		mZpayFavorites=(Button) searchBar.findViewById(R.id.step1_favoritesbtn);
		mZpayFavorites.setTypeface(mZouponsFont);
		mZpaySearch=(Button) searchBar.findViewById(R.id.step1_searchbtn);
		mZpaySearch.setTypeface(mZouponsFont);
		mZpayList=(Button) searchBar.findViewById(R.id.step1_listviewbtn);
		mZpayList.setTypeface(mZouponsFont);

		mZpayMenuBarContainer=(LinearLayout) menuBar.findViewById(R.id.step1_menubar_zpaycontainer);
		mZpayMenuBarZsend=(LinearLayout) menuBar.findViewById(R.id.step1_menubar_zpay_zsend);
		mZpayMenuBarZsend.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
		mZpayMenuBarZapprove=(LinearLayout) menuBar.findViewById(R.id.step1_menubar_zpay_zapprove);
		mZpayMenuBarZapprove.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
		mZpayMenuBarZapprove.setVisibility(View.INVISIBLE);

		mMapViewContainer=(RelativeLayout) middleView.findViewById(R.id.step1_mapviewContainer);
		//mMapView=(TapControlledMapView) middleView.findViewById(R.id.step1_MapView);
		mCurrentLocation=(Button) middleView.findViewById(R.id.step1_googlemaps_current_location);

		mListViewContainer=(LinearLayout) middleView.findViewById(R.id.step1_shopListcontainer);
		mShopListView = (ListView) middleView.findViewById(R.id.step1_shopListView);

		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
		mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		//mShopListView.addFooterView(mFooterLayout);

		drawable = getResources().getDrawable(R.drawable.ant3);
		//itemizedOverlay = new MapViewItemizedOverlay(drawable, mMapView,TAG,mPageFlag);

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
		
		mIsZPAYLoadMore = false;
		mZPAYStoreDetailsStart="0";
		mZPAYStoreDetailsEndLimit="20";
		mZPAYStoreDetailsCount = "0";
		/*if(mShopListView.getFooterViewsCount() == 0){
			mShopListView.addFooterView(mFooterLayout);	
		}*/
		mGoogleMap = null;
		/*try{
			//Load Current Location while activity loading
			if(mConnectionAvailabilityChecking.ConnectivityCheck(zpay_step1.this)){
				mMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
				getCurrentLocation();		// To Get Current Location
			}else{
				Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
			}
		}catch(Exception e){
			e.printStackTrace();
		}*/

		if(!mPageFlag.equals("")&&mPageFlag.equals("giftcard")){
			mZpayMenuBarContainer.setVisibility(View.GONE);
		}else{
			mZpayMenuBarContainer.setVisibility(View.VISIBLE);
		}

		mHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, zpay_step1.this,POJOMainMenuActivityTAG.TAG=TAG));
		mZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, zpay_step1.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, zpay_step1.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftcards.setOnClickListener(new MenuItemClickListener(leftMenuItems,zpay_step1.this,POJOMainMenuActivityTAG.TAG=TAG));
		mManageCards.setOnClickListener(new MenuItemClickListener(leftMenuItems, zpay_step1.this,POJOMainMenuActivityTAG.TAG=TAG));
		mReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, zpay_step1.this,POJOMainMenuActivityTAG.TAG=TAG));
		mMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,zpay_step1.this,POJOMainMenuActivityTAG.TAG=TAG));
		mMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,zpay_step1.this,POJOMainMenuActivityTAG.TAG=TAG));
		mChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,zpay_step1.this,POJOMainMenuActivityTAG.TAG=TAG));
		mRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,zpay_step1.this,POJOMainMenuActivityTAG.TAG=TAG));
		mSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,zpay_step1.this,POJOMainMenuActivityTAG.TAG=TAG));
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
				new LogoutSessionTask(zpay_step1.this).execute();
				Intent intent_logout = new Intent(zpay_step1.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_logout);
				finish();
			}
		});

		btnSlide.setOnClickListener(new ClickListenerForScrolling(zpay_step1.this,scrollView, leftMenu, MenuFlag_ZpayStep1=1, mZpayStep1FreezeView,mSearchStoreName,shrinkSearch,mCancelStoreName,mSearchBoxContainer));
		mSearchStoreName.setOnEditorActionListener(new DoneOnEditorActionListener(zpay_step1.this, shrinkSearch, searchBar, mSearchStoreName, mZpayStep1FreezeView,mCancelStoreName,mSearchBoxContainer));

		mZpayStep1FreezeView.setOnClickListener(new ClickListenerForScrolling(zpay_step1.this,scrollView, leftMenu, MenuFlag_ZpayStep1, mZpayStep1FreezeView,mSearchStoreName,shrinkSearch,mCancelStoreName,mSearchBoxContainer));
		mZpayQRCode.setOnClickListener(new MenuBarQRCodeClickListener(zpay_step1.this,zpay_step1.this));
		mZpayFavorites.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMapViewOnScrollViewFlag="favorites";
				String result="";
				if(mSearchStoreName.getVisibility()==View.VISIBLE){
					//Set MenuSearchBarClickListener CLICKFLAG to true 
					MenuBarSearchClickListener.ZPAYFLAG=true;
					//itemizedOverlay.hideAllCallOuts();	//To hide loaded call outs in mapview
					mGoogleMap.clear();	//To hide loaded call outs in mapview

					//close searchbox and disable freezeview
					result = new FreezeClickListener(scrollView, leftMenu,searchBar, mSearchStoreName, shrinkSearch,
							zpay_step1.this,mCancelStoreName,mSearchBoxContainer).closeSearchBox(TAG);

					if(result.equals("success")){
						MainMenuActivity.mFavouritesStart="0";
						FavouritesTaskLoader mFavouritesTask = new FavouritesTaskLoader(zpay_step1.this,mShopListView,mFooterLayout,"PROGRESS","zpay_step1",mGoogleMap,getMapViewRadius(mGoogleMap.getCameraPosition().target),mPageFlag);
						mFavouritesTask.execute("FavouriteStore","");
					}
				}else{
					MainMenuActivity.mFavouritesStart="0";
					FavouritesTaskLoader mFavouritesTask = new FavouritesTaskLoader(zpay_step1.this,mShopListView,mFooterLayout,"PROGRESS","zpay_step1",mGoogleMap,getMapViewRadius(mGoogleMap.getCameraPosition().target),mPageFlag);
					mFavouritesTask.execute("FavouriteStore","");
				}
			}
		});
		mZpaySearch.setOnClickListener(new MenuBarSearchClickListener(zpay_step1.this,searchBar,mSearchStoreName,mZpayStep1FreezeView,mCancelStoreName,mSearchBoxContainer));
		mZpayList.setOnClickListener(new MenuBarListClickListener(zpay_step1.this,mMapViewContainer,mListViewContainer,middleView,mZpayList,"zpay_step1"));

		adapter = new CustomStoreAdapter(zpay_step1.this, android.R.layout.simple_dropdown_item_1line,mPageFlag);
		mSearchStoreName.setAdapter(adapter);

		mCurrentLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mZPAYStoreDetailsStart="0";
				mZPAYStoreDetailsEndLimit="20";
				mZPAYStoreDetailsCount = "0";	
				mIsZPAYLoadMore = false;
				//itemizedOverlay.hideAllCallOuts();	//To hide loaded call outs in mapview
				
				// To remove map scroll listener from google map
				mGoogleMap.setOnCameraChangeListener(null);
				
				/*if(mShopListView.getFooterViewsCount() == 0){
					mShopListView.addFooterView(mFooterLayout);	
				}*/
				//Toast.makeText(getApplicationContext(), "Current Location", Toast.LENGTH_SHORT).show();
				try{
					//Load Current Location while activity loading
					if(mConnectionAvailabilityChecking.ConnectivityCheck(zpay_step1.this)){
						if(mSearchStoreName.getVisibility()!=View.VISIBLE){
							mMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
							getCurrentLocation();		// To Get Current Location
						}else if(mSearchStoreName.getVisibility()==View.VISIBLE&&mSearchStoreName.getText().toString().trim().length()>0){
							mGoogleMap.clear();
							mMapViewOnScrollViewFlag ="search";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
							for(int i=0;i<WebServiceStaticArrays.mSearchStore.size();i++){
								final Search_ClassVariables obj = (Search_ClassVariables) WebServiceStaticArrays.mSearchStore.get(i);
								if(mSearchStoreName.getText().toString().equals(obj.storeName)){
									mZPAYStoreDetailsStart="0";
									mZPAYStoreDetailsEndLimit="20";
									mZPAYStoreDetailsCount = "0";	
									mIsZPAYLoadMore = false;
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
											StoresLocationAsynchThread storeslocationasynchthread = new StoresLocationAsynchThread(zpay_step1.this,shrinkSearch,mSearchStoreName,searchBar,mZpayStep1FreezeView,mGoogleMap,mShopListView,distance,"zpay_step1",mCancelStoreName,mSearchBoxContainer,mPageFlag,mFooterLayout,"PROGRESS",true,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
											storeslocationasynchthread.execute(obj.storeId,mDeviceCurrentLocationLatitude,CurrentLocation.CurrentLocation_Longitude);
										}

										@Override
										public void onCancel() {
											// TODO Auto-generated method stub

										}
									});
									/*// To move map to current location
									mMapView.getController().animateTo(gp,new Runnable() {

										@Override
										public void run() {
											distance = getMapViewRadius(mMapView.getMapCenter());
											StoresLocationAsynchThread storeslocationasynchthread = new StoresLocationAsynchThread(zpay_step1.this,shrinkSearch,mSearchStoreName,searchBar,mZpayStep1FreezeView,mapOverlays,itemizedOverlay,mMapView,mShopListView,distance,"zpay_step1",mCancelStoreName,mSearchBoxContainer,mPageFlag,mFooterLayout,"PROGRESS",gp,true,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
											storeslocationasynchthread.execute(obj.storeId,mDeviceCurrentLocationLatitude,CurrentLocation.CurrentLocation_Longitude);
										}
									});*/

								}else{
									Toast.makeText(getApplicationContext(), "Please Enter Valid Store Name.", Toast.LENGTH_SHORT).show();
								}
							}
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
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(zpay_step1.this).execute();
				Intent intent_logout = new Intent(zpay_step1.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_logout);
				zpay_step1.this.finish();
			}
		});

		mSearchStoreName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				CustomStoreAdapter.isSearchStringChanged = true;
				Log.i("text changed", "" + mSearchStoreName.isPopupShowing());
				if(!mSearchStoreName.isPopupShowing()){
					Log.i("text changed", "hide");
					mSearchStoreName.dismissDropDown();	
				}else{
					Log.i("text changed", "dont hide");
				}
				Log.i("text changed", "" + mSearchStoreName.isPopupShowing());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				Log.i("text changed", "before text changed");
			}

			@Override
			public void afterTextChanged(Editable s) {
				Log.i("text changed", "after text changed" );
			}
		});

		mCancelStoreName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Set MenuSearchBarClickListener CLICKFLAG to true 
				MenuBarSearchClickListener.ZPAYFLAG=true;
				//itemizedOverlay.hideAllCallOuts();	//To hide loaded call outs in mapview
				mGoogleMap.setOnCameraChangeListener(null);
				if(mSearchStoreName.getText().toString().length()>0&&mMapViewOnScrollViewFlag.equalsIgnoreCase("search")){
					// close searchbox and its items in map and list view and reload store near current location stores and disable freezeview
					if(new FreezeClickListener(scrollView, leftMenu, searchBar, mSearchStoreName, shrinkSearch,
							zpay_step1.this,mCancelStoreName,mSearchBoxContainer).closeSearchBox(TAG).equals("success")){
						mMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
						mZPAYStoreDetailsStart="0";
						mZPAYStoreDetailsEndLimit="20";
						mZPAYStoreDetailsCount = "0";	
						mIsZPAYLoadMore = false;
						/*if(mShopListView.getFooterViewsCount() == 0){
							mShopListView.addFooterView(mFooterLayout);	
						}*/
						getCurrentLocation();
					}
				}else{
					//close searchbox and disable freezeview
					new FreezeClickListener(scrollView, leftMenu,searchBar, mSearchStoreName, shrinkSearch,
							zpay_step1.this,mCancelStoreName,mSearchBoxContainer).closeSearchBox(TAG);
				}
			}
		});

		mSearchStoreName.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try{
					InputMethodManager softkeyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					softkeyboard.hideSoftInputFromWindow(mSearchStoreName.getWindowToken(), 0);
					mZPAYStoreDetailsStart="0";
					mZPAYStoreDetailsEndLimit="20";
					mZPAYStoreDetailsCount = "0";	
					mIsZPAYLoadMore = false;
					/*if(mShopListView.getFooterViewsCount() == 0){
						mShopListView.addFooterView(mFooterLayout);	
					}*/
					//itemizedOverlay.hideAllCallOuts();	//To hide loaded call outs in mapview
					
					mGoogleMap.clear(); //To hide loaded call outs in mapview
					
					mMapViewOnScrollViewFlag ="search";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview

					//Toast.makeText(getApplicationContext(), SearchStaticArrayList.list_storename.get(position), Toast.LENGTH_SHORT).show();
					Search_ClassVariables obj = (Search_ClassVariables) adapter.getItem(position);
					mSearchStoreName.setThreshold(1000);
					mSearchStoreName.setText(obj.storeName);
					mSearchStoreName.setThreshold(1);
					search_storeId = obj.storeId;
					distance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
					mGoogleMap.setOnCameraChangeListener(null);
					storeslocationasynchthread = new StoresLocationAsynchThread(zpay_step1.this,shrinkSearch,mSearchStoreName,searchBar,mZpayStep1FreezeView,/*mapOverlays,*/mGoogleMap,mShopListView,distance,"zpay_step1",mCancelStoreName,mSearchBoxContainer,mPageFlag,mFooterLayout,"PROGRESS",true,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
					storeslocationasynchthread.execute(obj.storeId,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		mZpayMenuBarZapprove.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mZpayMenuBarZsend.setBackgroundResource(R.drawable.header_2);
				Intent intent_menubarzsend = new Intent(zpay_step1.this,InvoiceApproval.class);
				startActivity(intent_menubarzsend);
			}
		});

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
				mNotificationList.setAdapter(new CustomNotificationAdapter(zpay_step1.this));
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

	//To get user current location    
	public void getCurrentLocation(){
		boolean flag;
		int response =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(response == ConnectionResult.SUCCESS){
			Toast.makeText(getApplicationContext(), "Please Wait...GPS fetching Current Location", Toast.LENGTH_SHORT).show();
			mLocationClient = new LocationClient(this,this,this);
			mLocationClient.connect();
		}else{
			Toast.makeText(this, "Please update google play application", Toast.LENGTH_LONG).show();
		}
		/*// If current location is not successfully fetched during login or navigationg from other screens ...
		if(SlidingView.locationoverlay.getLastFix() == null){
			// If current location overlay does not have last fix means wait for first fix
			SlidingView.locationoverlay.runOnFirstFix(new Runnable() {

				@Override
				public void run() {
					Log.i("current location source", "location from first fix");
					Message msg = new Message();
					msg.obj = "currentlocation";
					Bundle mLocationdetails = new Bundle();
					mLocationdetails.putDouble("latitude", SlidingView.locationoverlay.getMyLocation().getLatitudeE6()/1E6);
					mLocationdetails.putDouble("longitude", SlidingView.locationoverlay.getMyLocation().getLongitudeE6()/1E6);
					msg.setData(mLocationdetails);
					handler_response.sendMessage(msg);
				}
			});	
		}else{
			// If current location overlay already have fix means....
			Log.i("current location source", "location from last fix");
			Message msg = new Message();
			msg.obj = "currentlocation";
			Bundle mLocationdetails = new Bundle();
			mLocationdetails.putDouble("latitude", SlidingView.locationoverlay.getMyLocation().getLatitudeE6()/1E6);
			mLocationdetails.putDouble("longitude", SlidingView.locationoverlay.getMyLocation().getLongitudeE6()/1E6);
			msg.setData(mLocationdetails);
			handler_response.sendMessage(msg);
		}*/
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
				
				/*mDeviceCurrentLocationLatitude="33.5319";
				mDeviceCurrentLocationLongitude="-117.703";*/

				if(mMapViewContainer.getVisibility()==View.VISIBLE){
					/*gp=new GeoPoint((int)(Double.parseDouble(CurrentLocation.CurrentLocation_Latitude)*1E6),(int) (Double.parseDouble(CurrentLocation.CurrentLocation_Longitude)*1E6));
					mMapView.getController().animateTo(gp,new Runnable() {

						@Override
						public void run() {
							mMapView.getController().animateTo(gp);
							distance = getMapViewRadius(mMapView.getMapCenter());
							mBeforScrollCenter=mMapView.getMapCenter();
							mCurrentZoom = mMapView.getZoomLevel();
							Log.i(TAG,"Distance: "+distance);
							
							//**************************************
							
							StoreNearCurrentLocationAsyncThread storenearThread = new StoreNearCurrentLocationAsyncThread(zpay_step1.this,itemizedOverlay,mMapView,mShopListView,gp,getMapViewRadius(mMapView.getMapCenter()),mPageFlag,mFooterLayout,"PROGRESS",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
							storenearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
							
							//**************************************
						}
					});*/
					
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
							storenearThread = new StoreNearCurrentLocationAsyncThread(zpay_step1.this,mGoogleMap,mShopListView,distance,mPageFlag,mFooterLayout,"PROGRESS",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,true);
							storenearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
						}

						@Override
						public void onCancel() {
							// TODO Auto-generated method stub

						}
					});
					
				}else{
				/*	gp=new GeoPoint((int)(Double.parseDouble(mDeviceCurrentLocationLatitude)*1E6),(int) (Double.parseDouble(mDeviceCurrentLocationLongitude)*1E6));
					mMapView.getController().animateTo(gp);
					distance = getMapViewRadius(mMapView.getMapCenter());
					mBeforScrollCenter=mMapView.getMapCenter();
					mCurrentZoom = mMapView.getZoomLevel();
					Log.i(TAG,"Distance: "+distance);
					
					//**************************************
					
					StoreNearCurrentLocationAsyncThread storenearThread = new StoreNearCurrentLocationAsyncThread(zpay_step1.this,itemizedOverlay,mMapView,mShopListView,gp,getMapViewRadius(mMapView.getMapCenter()),mPageFlag,mFooterLayout,"PROGRESS",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
					storenearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
					
					//**************************************
*/					
					mLocationGeoCordinates = new LatLng(Double.parseDouble(mDeviceCurrentLocationLatitude), Double.parseDouble(mDeviceCurrentLocationLongitude));
					CameraPosition myPosition = new CameraPosition.Builder().target(mLocationGeoCordinates).zoom(13).build();
					mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));					
					//distance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
					distance = 0.0525; // Since map is not visible, static distance is added
					mBeforScrollCenter=myPosition.target;  // Since map is not visible, center is 0.0
					mCurrentZoom = mGoogleMap.getCameraPosition().zoom;
					storenearThread = new StoreNearCurrentLocationAsyncThread(zpay_step1.this,mGoogleMap,mShopListView,distance,mPageFlag,mFooterLayout,"PROGRESS",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,true);
					storenearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
				}
			}
		}
	};

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's width.
	 */
	static class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View leftMenu;
		int menuFlag;
		Button mFreezeView,mClearStoreName;
		Context context;
		InputMethodManager softkeyboardevent;
		AutoCompleteTextView mSearchBox;
		AnimationSet shrinkSearch;
		RelativeLayout mSearchBoxContainer;

		/**
		 * Menu must NOT be out/shown to start with.
		 */
		public ClickListenerForScrolling(Context context,HorizontalScrollView scrollView, View leftmenu, int menuflag,Button freezeview,AutoCompleteTextView searchbox
				,AnimationSet shrinksearch,Button clearstorename,RelativeLayout searchboxcontainer) {
			super();
			this.context=context;
			this.scrollView = scrollView;
			this.leftMenu = leftmenu;
			this.menuFlag=menuflag;
			this.mFreezeView = freezeview;
			this.mSearchBox=searchbox;
			this.shrinkSearch=shrinksearch;
			this.mClearStoreName=clearstorename;
			this.mSearchBoxContainer=searchboxcontainer;
		}

		@Override
		public void onClick(View v) {

			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);

			Context context = leftMenu.getContext();
			int menuWidth = leftMenu.getMeasuredWidth();
			
			if(MenuBarSearchClickListener.ZPAYFLAG==true){
				softkeyboardevent = (InputMethodManager)this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
				softkeyboardevent.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
			}else{	//newly added
				InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
			}

			
			// Ensure menu is visible
			leftMenu.setVisibility(View.VISIBLE);
			if (!MenuOutClass.ZPAY_MENUOUT) {
				if(menuFlag==1){
					
					if(MenuBarSearchClickListener.ZPAYFLAG==true){

						mSearchBox.clearFocus();
						mSearchBox.setFocusable(false);
						scrollView.smoothScrollTo(left, 0);
						MenuBarSearchClickListener.ZPAYFLAG=false;
					}else{
						Log.i(TAG,Messages.getString("Search Box Is Not in Open State")); //$NON-NLS-1$
						scrollView.smoothScrollTo(left, 0);
					}
					mFreezeView.setVisibility(View.VISIBLE);
				}
			} else {
				// Scroll to menuWidth so menu isn't on screen.
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);
				mFreezeView.setVisibility(View.GONE);
			}
			MenuOutClass.ZPAY_MENUOUT = !MenuOutClass.ZPAY_MENUOUT;
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

	//Get Screen width
	public int getScreenWidth(){
		int Measuredwidth = 0;  
		int Measuredheight = 0;  
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Measuredwidth = display.getWidth(); 
		Measuredheight = display.getHeight();
		return Measuredwidth;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		zpay_step1.mMapViewOnScrollViewFlag="qrcode";
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE: //1
			if(resultCode==RESULT_OK){
				mMapViewOnScrollViewFlag="qrcode";
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
							mZPAYStoreDetailsStart="0";
							mZPAYStoreDetailsEndLimit="20";
							mZPAYStoreDetailsCount = "0";

							if(mSearchStoreName.getVisibility()==View.VISIBLE){
								//Set MenuSearchBarClickListener CLICKFLAG to true 
								MenuBarSearchClickListener.ZPAYFLAG=true;

								//close searchbox and disable freezeview
								rtn = new FreezeClickListener(scrollView, leftMenu,searchBar, mSearchStoreName, shrinkSearch,
										zpay_step1.this,mCancelStoreName,mSearchBoxContainer).closeSearchBox(TAG);
								if(rtn.equals("success")){
									//Toast.makeText(getApplicationContext(),"QR-CODE: "+result , Toast.LENGTH_SHORT).show();
									StoresQRCodeAsynchThread storeqrcode = new StoresQRCodeAsynchThread(zpay_step1.this, mGoogleMap, mShopListView,getMapViewRadius(mGoogleMap.getCameraPosition().target),"zpay_step1",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
									storeqrcode.execute(result);
								}
							}else{
								Toast.makeText(getApplicationContext(),"QR-CODE: "+result , Toast.LENGTH_SHORT).show();
								StoresQRCodeAsynchThread storeqrcode = new StoresQRCodeAsynchThread(zpay_step1.this, mGoogleMap, mShopListView,getMapViewRadius(mGoogleMap.getCameraPosition().target),"zpay_step1",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
								storeqrcode.execute(result);
							}
						}
					});
				}
			}else if(resultCode==RESULT_CANCELED){
				Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
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


	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			try{
				if(intent.hasExtra("FromNotification")){
					if(NotificationDetails.notificationcount>0){
						mNotificationCount.setVisibility(View.VISIBLE);
						mNotificationCount.setText(String.valueOf(NotificationDetails.notificationcount));
					}else{
						mNotificationCount.setVisibility(View.GONE);
						Log.i("Notification result", "No new notification");
					}
				}else{
					/*boolean flag;
					//To know the mapview distance in pixels 
					Log.i(TAG,"MapView ZoomLevel: "+mMapView.getZoomLevel());

					//Logic to differentiate the first scroll and next scrolls of mapview
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
						CurrentLocation.CurrentLocation_Latitude=String.valueOf((double)MapViewCenter.mMapViewCenter_GeoPoint.getLatitudeE6() / (double)1E6);
						CurrentLocation.CurrentLocation_Longitude=String.valueOf((double)MapViewCenter.mMapViewCenter_GeoPoint.getLongitudeE6() / (double)1E6);
						if(zpay_step1.mMapViewOnScrollViewFlag.equals("currentlocation")){
							Toast.makeText(zpay_step1.this, "Loading...", Toast.LENGTH_SHORT).show();
							mZPAYStoreDetailsStart="0";
							mZPAYStoreDetailsEndLimit="20";
							mZPAYStoreDetailsCount = "0";	
							mIsZPAYLoadMore = false;
							if(mShopListView.getFooterViewsCount() == 0){
								mShopListView.addFooterView(mFooterLayout);	
							}
							StoreNearCurrentLocationAsyncThread storenearThread = new StoreNearCurrentLocationAsyncThread(zpay_step1.this,itemizedOverlay,mMapView,mShopListView,gp,getMapViewRadius(mMapView.getMapCenter()),mPageFlag,mFooterLayout,"NOPROGRESS",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
							storenearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
						}else if(zpay_step1.mMapViewOnScrollViewFlag.equals("search")){
							Toast.makeText(zpay_step1.this, "Loading...", Toast.LENGTH_SHORT).show();
							//Set MenuSearchBarClickListener CLICKFLAG to true 
							MenuBarSearchClickListener.ZPAYFLAG=true;
							mZPAYStoreDetailsStart="0";
							mZPAYStoreDetailsEndLimit="20";
							mZPAYStoreDetailsCount = "0";	
							mIsZPAYLoadMore = false;
							if(mShopListView.getFooterViewsCount() == 0){
								mShopListView.addFooterView(mFooterLayout);	
							}
							StoresLocationAsynchThread storeslocationasynchthread = new StoresLocationAsynchThread(zpay_step1.this,shrinkSearch,mSearchStoreName,searchBar,mZpayStep1FreezeView,mapOverlays,itemizedOverlay,mMapView,mShopListView,distance,"zpay_step1",mCancelStoreName,mSearchBoxContainer,mPageFlag,mFooterLayout,"NOPROGRESS",gp,false,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
							storeslocationasynchthread.execute(search_storeId,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mGoogleMap = null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		/*// To disable current location overlay
		SlidingView.locationoverlay.disableMyLocation();
		SlidingView.locationoverlay.disableMyLocation();*/
		unregisterReceiver(mReceiver);
		if(mNotificationSync!=null){
			mNotificationSync.cancelAlarm();
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// To enable current location overlay
		//SlidingView.locationoverlay.enableMyLocation();
		if(mConnectionAvailabilityChecking.ConnectivityCheck(zpay_step1.this)){
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
 		
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(zpay_step1.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(zpay_step1.this).checkIfSessionExpires();
		mZpayMenuBarZsend.setBackgroundColor(R.drawable.footer_dark_blue_new);
		mZpayMenuBarZapprove.setBackgroundResource(R.drawable.header_2);

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(zpay_step1.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		if(mGoogleMap != null){
			Log.i("Google map", "camera change listner set");
			mGoogleMap.setOnCameraChangeListener(zpay_step1.this);
		}
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
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
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onUserLeaveHint() {
		Log.i(TAG,Messages.getString("onUserLeaveHint"));
		new RefreshZoupons().isApplicationGoneBackground(zpay_step1.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}

	/**
     * function to load map
     * */
    private void initilizeGoogleMap() {
        if (mGoogleMap == null) {
        	Log.i("Google map", "initializing google maps");
        	mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.step1_MapView)).getMap();
            mGoogleMapUISettings = mGoogleMap.getUiSettings();
            mGoogleMapUISettings.setZoomControlsEnabled(false);
    		mGoogleMapUISettings.setMyLocationButtonEnabled(false);
    		mGoogleMapUISettings.setCompassEnabled(false);
            mGoogleMap.setInfoWindowAdapter(new CustomMapViewCallout(zpay_step1.this));
            mGoogleMap.setOnInfoWindowClickListener(new CustomMapCalloutClickListener(zpay_step1.this,mPageFlag));
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
	
	
	@Override
	public void onCameraChange(CameraPosition cameraposition) {
		// TODO Auto-generated method stub
		boolean flag;
		MapViewCenter.mMapViewCenter_GeoPoint = cameraposition.target;
		float zoom_during_scroll = cameraposition.zoom;
		Log.i(TAG,"MapView ZoomLevel: "+zoom_during_scroll);
		//Logic to differentiate the first scroll and next scrolls of mapview
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
			zpay_step1.mCurrentLocation.setEnabled(false);
			
			CurrentLocation.CurrentLocation_Latitude=String.valueOf(MapViewCenter.mMapViewCenter_GeoPoint.latitude);
			CurrentLocation.CurrentLocation_Longitude=String.valueOf(MapViewCenter.mMapViewCenter_GeoPoint.longitude);
			if(zpay_step1.mMapViewOnScrollViewFlag.equals("currentlocation")){
				/*Toast.makeText(zpay_step1.this, "Loading...", Toast.LENGTH_SHORT).show();*/
				mZPAYStoreDetailsStart="0";
				mZPAYStoreDetailsEndLimit="20";
				mZPAYStoreDetailsCount = "0";	
				mIsZPAYLoadMore = false;
				/*if(mShopListView.getFooterViewsCount() == 0){
					mShopListView.addFooterView(mFooterLayout);	
				}*/
				storenearThread = new StoreNearCurrentLocationAsyncThread(zpay_step1.this,mGoogleMap,mShopListView,getMapViewRadius(mGoogleMap.getCameraPosition().target),mPageFlag,mFooterLayout,"NOPROGRESS",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,false);
				storenearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
			}else if(zpay_step1.mMapViewOnScrollViewFlag.equals("search")){
				/*Toast.makeText(zpay_step1.this, "Loading...", Toast.LENGTH_SHORT).show();*/
				//Set MenuSearchBarClickListener CLICKFLAG to true 
				MenuBarSearchClickListener.ZPAYFLAG=true;
				mZPAYStoreDetailsStart="0";
				mZPAYStoreDetailsEndLimit="20";
				mZPAYStoreDetailsCount = "0";	
				mIsZPAYLoadMore = false;
				/*if(mShopListView.getFooterViewsCount() == 0){
					mShopListView.addFooterView(mFooterLayout);	
				}*/
				storeslocationasynchthread = new StoresLocationAsynchThread(zpay_step1.this,shrinkSearch,mSearchStoreName,searchBar,mZpayStep1FreezeView,mGoogleMap,mShopListView,distance,"zpay_step1",mCancelStoreName,mSearchBoxContainer,mPageFlag,mFooterLayout,"NOPROGRESS",false,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
				storeslocationasynchthread.execute(search_storeId,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
			}
		}else{
			Log.i(TAG,"Sorry Not Loading");
		}
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
			mLocationClient = null;
		}else{
			Log.i("location client","on Connection issue");
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Log.i("location client","on DisConnected");
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
