package com.us.zoupons.mobilepay;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.android.asyncthread_class.FavouritesTaskLoader;
import com.us.zoupons.android.asyncthread_class.StoreNearCurrentLocationAsyncThread;
import com.us.zoupons.android.asyncthread_class.StoresLocationAsynchThread;
import com.us.zoupons.android.asyncthread_class.StoresQRCodeAsynchThread;
import com.us.zoupons.android.lib.integrator.IntentIntegrator;
import com.us.zoupons.android.lib.integrator.IntentResult;
import com.us.zoupons.android.mapviewcallouts.CustomMapCalloutClickListener;
import com.us.zoupons.android.mapviewcallouts.CustomMapViewCallout;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.CurrentLocation;
import com.us.zoupons.classvariables.MapViewCenter;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.Search_ClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.listview_inflater_classes.CustomStoreAdapter;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.shopper.invoice.InvoiceApproval;

/**
 * 
 * Activity which displays stores to buy/sell giftcards
 *
 */

public class CardPurchase extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener,OnCameraChangeListener{

	// Initializing views and variables
	public static MyHorizontalScrollView sHorizScrollView;
	public static View sLeftMenu,sApp;
	private Typeface mZouponsFont;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage,mTabBarLoginChoice;
	Handler handler = new Handler();
	//LeftMenu
	public static LinearLayout mHome,mZpay,mInvoiceCenter,mGiftcards,mManageCards,mReceipts,mMyFavourites,mMyFriends,mChat,mRewards,mSettings,mLogout;
	public static TextView mHomeText,mZpayText,mInvoiceCenterText,mGiftCardsText,mManageCardsText,mReceiptsText,mMyFavoritesText,mMyFriendsText,mChatText,mRewardsText,mSettingsText,mLogoutText;
	private double mLatitude,mLongitude,mMenuWidth;
	private ViewGroup tabBar,menuBar,middleView,leftMenuItems,searchBar;
	public static int MenuFlag_ZpayStep1=0;
	private LinearLayout mZpayMenuBarContainer,mZpayMenuBarZsend,mZpayMenuBarZapprove,mBtnSlide,mListViewContainer;
	private AutoCompleteTextView mSearchStoreName;
	private Button mZpayQRCode,mZpayFavorites,mZpaySearch,mNotificationCount,mCancelStoreName;
	private static Button mCurrentLocation;
	public static Button mZpayStep1FreezeView,mZpayStep1IntialFreezeView,mZpayList;
	public static AnimationSet shrinkSearch;
	private RelativeLayout mMapViewContainer,mBtnLogout,mSearchBoxContainer;
	private ListView mShopListView;
	private CustomStoreAdapter adapter;
	//For Dynamic List Update
	private View mFooterLayout;
	public static boolean mIsZPAYLoadMore;
	private LayoutInflater mInflater;
	public static int left=0;
	public static String mZPAYStoreDetailsStart="0",mZPAYStoreDetailsEndLimit="20",mZPAYStoreDetailsCount = "0",mMapViewOnScrollViewFlag="";	
	double distance = 0;
	//Get ScrollDistance Variables
	private LatLng mBeforScrollCenter,mAfterScrollCenter,mLocationGeoCordinates;
	private boolean isFirstTimeMapScroll;
	private float mCurrentZoom=0;
	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	private String TAG = "CardPurchase",mDeviceCurrentLocationLatitude="",mDeviceCurrentLocationLongitude="",mPageFlag,search_storeId="";
	public static ScrollView leftMenuScrollView;
	private GoogleMap mGoogleMap;
	private UiSettings mGoogleMapUISettings;
	private LocationClient mLocationClient;
	private Dialog mUpdateGooglePlayServicesDialog = null;
	private final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1;
	private StoreNearCurrentLocationAsyncThread storenearThread;
	private StoresLocationAsynchThread storeslocationasynchthread;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notification
	private ScheduleNotificationSync mNotificationSync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Referencing view from layout XML file
		LayoutInflater inflater = LayoutInflater.from(this);
		sHorizScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(sHorizScrollView);
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();
		shrinkSearch = new AnimationSet(true);
		//Flag to differentiate the control from zpay or giftcard purchase.
		mPageFlag=getIntent().getExtras().getString("pageflag")!=null?getIntent().getExtras().getString("pageflag"):"";
		sLeftMenu = inflater.inflate(R.layout.shopper_leftmenu, null);
		sApp = inflater.inflate(R.layout.zpay_step1, null);
		tabBar = (ViewGroup) sApp.findViewById(R.id.step1_tabBar);
		searchBar = (ViewGroup) sApp.findViewById(R.id.step1_searchbar);
		menuBar = (ViewGroup) sApp.findViewById(R.id.step1_menubar);
		middleView = (ViewGroup) sApp.findViewById(R.id.step1_middleview);
		leftMenuItems = (ViewGroup) sLeftMenu.findViewById(R.id.menuitems);
		leftMenuScrollView = (ScrollView) sLeftMenu.findViewById(R.id.leftmenu_scrollview);
		leftMenuScrollView.fullScroll(View.FOCUS_UP);
		leftMenuScrollView.pageScroll(View.FOCUS_UP);
		// Header views declaration 
		mBtnSlide = (LinearLayout) tabBar.findViewById(R.id.step1_BtnSlide);
		mBtnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_notificationImageId);
		mBtnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(CardPurchase.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) mBtnLogout.findViewById(R.id.zoupons_notification_count);        
		mTabBarLoginChoice = (ImageView) mBtnLogout.findViewById(R.id.store_header_loginchoice);
		// To check visibility of zoupons login choice (Man) button
		new LoginChoiceTabBarImage(CardPurchase.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		final View[] children = new View[] { sLeftMenu, sApp};
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sHorizScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(mBtnSlide));
		//sLeftmenu layouts
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
		// Setting Zoupons font
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

		mZpayStep1FreezeView=(Button) sApp.findViewById(R.id.step1_freezeview);
		mZpayStep1IntialFreezeView=(Button) sApp.findViewById(R.id.step1_intial_freezeview);
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
		mCurrentLocation=(Button) middleView.findViewById(R.id.step1_googlemaps_current_location);
		mListViewContainer=(LinearLayout) middleView.findViewById(R.id.step1_shopListcontainer);
		mShopListView = (ListView) middleView.findViewById(R.id.step1_shopListView);
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
		//mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		//mShopListView.addFooterView(mFooterLayout);
		// Notitification pop up layout declaration
		mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
		mNotificationImage.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		mNotificationCount.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));

		mIsZPAYLoadMore = false;
		mZPAYStoreDetailsStart="0";
		mZPAYStoreDetailsEndLimit="20";
		mZPAYStoreDetailsCount = "0";
		/*if(mShopListView.getFooterViewsCount() == 0){
			mShopListView.addFooterView(mFooterLayout);	
		}*/
		mGoogleMap = null;
		if(!mPageFlag.equals("")&&mPageFlag.equals("giftcard")){
			mZpayMenuBarContainer.setVisibility(View.GONE);
		}else{
			mZpayMenuBarContainer.setVisibility(View.VISIBLE);
		}
		// Registering click listener for left menu items
		mHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, CardPurchase.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, CardPurchase.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, CardPurchase.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mGiftcards.setOnClickListener(new MenuItemClickListener(leftMenuItems,CardPurchase.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mManageCards.setOnClickListener(new MenuItemClickListener(leftMenuItems, CardPurchase.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, CardPurchase.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,CardPurchase.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,CardPurchase.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,CardPurchase.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,CardPurchase.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
		mSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,CardPurchase.this,POJOMainMenuActivityTAG.TAG=TAG,mShopListView));
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
				new LogoutSessionTask(CardPurchase.this,"FromManualLogOut").execute();
			}
		});
		// Registering click listener 
		mBtnSlide.setOnClickListener(new ClickListenerForScrolling(CardPurchase.this,sHorizScrollView, sLeftMenu, MenuFlag_ZpayStep1=1, mZpayStep1FreezeView,mSearchStoreName,shrinkSearch,mCancelStoreName,mSearchBoxContainer));
		mSearchStoreName.setOnEditorActionListener(new DoneOnEditorActionListener(CardPurchase.this,mSearchStoreName));
		mZpayStep1FreezeView.setOnClickListener(new ClickListenerForScrolling(CardPurchase.this,sHorizScrollView, sLeftMenu, MenuFlag_ZpayStep1, mZpayStep1FreezeView,mSearchStoreName,shrinkSearch,mCancelStoreName,mSearchBoxContainer));
		mZpayQRCode.setOnClickListener(new MenuBarQRCodeClickListener(CardPurchase.this,CardPurchase.this));
		mZpaySearch.setOnClickListener(new MenuBarSearchClickListener(CardPurchase.this,searchBar,mSearchStoreName,mZpayStep1FreezeView,mCancelStoreName,mSearchBoxContainer));
		mZpayList.setOnClickListener(new MenuBarListClickListener(CardPurchase.this,mMapViewContainer,mListViewContainer,middleView,mZpayList,"CardPurchase"));
		// Setting customer adapter to autocomplete editText
		adapter = new CustomStoreAdapter(CardPurchase.this, android.R.layout.simple_dropdown_item_1line,mPageFlag);
		mSearchStoreName.setAdapter(adapter);

		mZpayFavorites.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMapViewOnScrollViewFlag="favorites";
				String result="";
				if(mSearchStoreName.getVisibility()==View.VISIBLE){  // If search box is visible, close search box and load favorite store
					//Set MenuSearchBarClickListener CLICKFLAG to true 
					MenuBarSearchClickListener.sZPAYFLAG=true;
					mGoogleMap.clear();	//To hide loaded call outs in mapview
					//close searchbox and disable freezeview
					result = new FreezeClickListener(sHorizScrollView, sLeftMenu,searchBar, mSearchStoreName, shrinkSearch,
							CardPurchase.this,mCancelStoreName,mSearchBoxContainer,mCurrentLocation).closeSearchBox(TAG);

					if(result.equals("success")){
						MainMenuActivity.mFavouritesStart="0";
						FavouritesTaskLoader mFavouritesTask = new FavouritesTaskLoader(CardPurchase.this,mShopListView,mFooterLayout,"PROGRESS","CardPurchase",mGoogleMap,getMapViewRadius(mGoogleMap.getCameraPosition().target),mPageFlag);
						mFavouritesTask.execute("FavouriteStore","");
					}
				}else{ // IF not visble just call Favorite stores
					MainMenuActivity.mFavouritesStart="0";
					FavouritesTaskLoader mFavouritesTask = new FavouritesTaskLoader(CardPurchase.this,mShopListView,mFooterLayout,"PROGRESS","CardPurchase",mGoogleMap,getMapViewRadius(mGoogleMap.getCameraPosition().target),mPageFlag);
					mFavouritesTask.execute("FavouriteStore","");
				}
			}
		});

		mCurrentLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mZPAYStoreDetailsStart="0";
				mZPAYStoreDetailsEndLimit="20";
				mZPAYStoreDetailsCount = "0";	
				mIsZPAYLoadMore = false;
				// To remove map scroll listener from google map
				mGoogleMap.setOnCameraChangeListener(null);
				/*if(mShopListView.getFooterViewsCount() == 0){
					mShopListView.addFooterView(mFooterLayout);	
				}*/
				try{
					//Load Current Location while activity loading
					if(mConnectionAvailabilityChecking.ConnectivityCheck(CardPurchase.this)){ // network connectivity check
						if(mSearchStoreName.getVisibility()!=View.VISIBLE){ // If search box not visible
							mMapViewOnScrollViewFlag ="currentlocation";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
							getCurrentLocation();		// To Get Current Location
						}else if(mSearchStoreName.getVisibility()==View.VISIBLE&&mSearchStoreName.getText().toString().trim().length()>0){ // Search box is visible and also has search string
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
									// To animate google map camera to current position
									mLocationGeoCordinates =  new LatLng(Double.parseDouble(mDeviceCurrentLocationLatitude), Double.parseDouble(mDeviceCurrentLocationLongitude));
									mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mLocationGeoCordinates),new CancelableCallback() {

										@Override
										public void onFinish() {
											// TODO Auto-generated method stub
											distance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
											StoresLocationAsynchThread storeslocationasynchthread = new StoresLocationAsynchThread(CardPurchase.this,shrinkSearch,mSearchStoreName,searchBar,mZpayStep1FreezeView,mGoogleMap,mShopListView,mCurrentLocation,distance,"CardPurchase",mCancelStoreName,mSearchBoxContainer,mPageFlag,mFooterLayout,"PROGRESS",true,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
											storeslocationasynchthread.execute(obj.storeId,mDeviceCurrentLocationLatitude,CurrentLocation.CurrentLocation_Longitude);
										}

										@Override
										public void onCancel() {
											// TODO Auto-generated method stub
										}
									});
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
				new LogoutSessionTask(CardPurchase.this,"FromManualLogOut").execute();
			}
		});

		mSearchStoreName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				CustomStoreAdapter.isSearchStringChanged = true;
				if(!mSearchStoreName.isPopupShowing()){  // To dismiss dropdown list
					mSearchStoreName.dismissDropDown();	
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		mCancelStoreName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Set MenuSearchBarClickListener CLICKFLAG to true 
				MenuBarSearchClickListener.sZPAYFLAG=true;
				//itemizedOverlay.hideAllCallOuts();	//To hide loaded call outs in mapview
				mGoogleMap.setOnCameraChangeListener(null);
				if(mSearchStoreName.getText().toString().length()>0&&mMapViewOnScrollViewFlag.equalsIgnoreCase("search")){ 
					// close searchbox and its items in map and list view and reload store near current location stores and disable freezeview
					if(new FreezeClickListener(sHorizScrollView, sLeftMenu, searchBar, mSearchStoreName, shrinkSearch,
							CardPurchase.this,mCancelStoreName,mSearchBoxContainer,mCurrentLocation).closeSearchBox(TAG).equals("success")){
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
					new FreezeClickListener(sHorizScrollView, sLeftMenu,searchBar, mSearchStoreName, shrinkSearch,
							CardPurchase.this,mCancelStoreName,mSearchBoxContainer,mCurrentLocation).closeSearchBox(TAG);
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
					mGoogleMap.clear(); //To hide loaded call outs in mapview
					mMapViewOnScrollViewFlag ="search";	//Flag to differentiate the asynch thread calling in broadcastreciever while scrolling mapview
					Search_ClassVariables obj = (Search_ClassVariables) adapter.getItem(position);
					mSearchStoreName.setThreshold(1000);
					mSearchStoreName.setText(obj.storeName);
					mSearchStoreName.setThreshold(1);
					search_storeId = obj.storeId;
					distance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
					mGoogleMap.setOnCameraChangeListener(null);
					// Call webservice
					storeslocationasynchthread = new StoresLocationAsynchThread(CardPurchase.this,shrinkSearch,mSearchStoreName,searchBar,mZpayStep1FreezeView,/*mapOverlays,*/mGoogleMap,mShopListView,mCurrentLocation,distance,"CardPurchase",mCancelStoreName,mSearchBoxContainer,mPageFlag,mFooterLayout,"PROGRESS",true,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
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
				Intent intent_menubarzsend = new Intent(CardPurchase.this,InvoiceApproval.class);
				startActivity(intent_menubarzsend);
			}
		});

	}

	//To get user current location    
	public void getCurrentLocation(){
		int response =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(response == ConnectionResult.SUCCESS){ // IF google play service is installed and also supported version
			Toast.makeText(getApplicationContext(), "Please Wait...GPS fetching Current Location", Toast.LENGTH_SHORT).show();
			mLocationClient = new LocationClient(this,this,this);
			mLocationClient.connect();
		}else{
			Toast.makeText(this, "Please update google play application", Toast.LENGTH_LONG).show();
		}
	}

	// Handler to update UI Items
	Handler handler_response = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.obj!=null && msg.obj.toString().equalsIgnoreCase("currentlocation")){
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

				if(mMapViewContainer.getVisibility()==View.VISIBLE){ // If Google map view is visible
					// To navigate google map to current location
					mLocationGeoCordinates = new LatLng(Double.parseDouble(CurrentLocation.CurrentLocation_Latitude), Double.parseDouble(CurrentLocation.CurrentLocation_Longitude));
					CameraPosition myCurentLocationPosition = new CameraPosition.Builder().target(mLocationGeoCordinates).zoom(13).build();
					mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(myCurentLocationPosition),new CancelableCallback() {

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							MapViewCenter.mMapViewCenter_GeoPoint = mGoogleMap.getCameraPosition().target;
							mGoogleMap.clear();
							// Calulating distance and assing google map state
							distance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
							mBeforScrollCenter=mGoogleMap.getCameraPosition().target;
							mCurrentZoom = mGoogleMap.getCameraPosition().zoom;
							storenearThread = new StoreNearCurrentLocationAsyncThread(CardPurchase.this,mGoogleMap,mShopListView,mCurrentLocation,distance,mPageFlag,mFooterLayout,"PROGRESS",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,true);
							storenearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
						}

						@Override
						public void onCancel() {
							// TODO Auto-generated method stub

						}
					});

				}else{
					// To navigate google map to specified location
					mLocationGeoCordinates = new LatLng(Double.parseDouble(mDeviceCurrentLocationLatitude), Double.parseDouble(mDeviceCurrentLocationLongitude));
					CameraPosition myPosition = new CameraPosition.Builder().target(mLocationGeoCordinates).zoom(13).build();
					mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));					
					//distance = getMapViewRadius(mGoogleMap.getCameraPosition().target);
					distance = 0.0525; // Since map is not visible, static distance is added
					mBeforScrollCenter=myPosition.target;  // Since map is not visible, center is 0.0
					mCurrentZoom = mGoogleMap.getCameraPosition().zoom;
					storenearThread = new StoreNearCurrentLocationAsyncThread(CardPurchase.this,mGoogleMap,mShopListView,mCurrentLocation,distance,mPageFlag,mFooterLayout,"PROGRESS",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,true);
					storenearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
				}
			}
		}
	};

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's width.
	 */
	class ClickListenerForScrolling implements OnClickListener {
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

			if(MenuBarSearchClickListener.sZPAYFLAG==true){
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

					if(MenuBarSearchClickListener.sZPAYFLAG==true){

						mSearchBox.clearFocus();
						mSearchBox.setFocusable(false);
						scrollView.smoothScrollTo(left, 0);
						MenuBarSearchClickListener.sZPAYFLAG=false;
					}else{
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE: //1
			if(resultCode==RESULT_OK){ // Store QR code  
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

							if(mSearchStoreName.getVisibility()==View.VISIBLE){ // If search box is visible
								//Set MenuSearchBarClickListener CLICKFLAG to true 
								MenuBarSearchClickListener.sZPAYFLAG=true;

								//close searchbox and disable freezeview
								rtn = new FreezeClickListener(sHorizScrollView, sLeftMenu,searchBar, mSearchStoreName, shrinkSearch,
										CardPurchase.this,mCancelStoreName,mSearchBoxContainer,mCurrentLocation).closeSearchBox(TAG);
								if(rtn.equals("success")){
									StoresQRCodeAsynchThread storeqrcode = new StoresQRCodeAsynchThread(CardPurchase.this, mGoogleMap, mShopListView,getMapViewRadius(mGoogleMap.getCameraPosition().target),"CardPurchase",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
									storeqrcode.execute(result);
								}
							}else{
								StoresQRCodeAsynchThread storeqrcode = new StoresQRCodeAsynchThread(CardPurchase.this, mGoogleMap, mShopListView,getMapViewRadius(mGoogleMap.getCameraPosition().target),"CardPurchase",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
								storeqrcode.execute(result);
							}
						}
					});
				}
			}else if(resultCode==RESULT_CANCELED){
				Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
			}
			break;
		case REQUEST_CODE_RECOVER_PLAY_SERVICES :  // After install of google play services 
			if(checkPlayServices()){
				if(mUpdateGooglePlayServicesDialog != null && mUpdateGooglePlayServicesDialog.isShowing())
					mUpdateGooglePlayServicesDialog.dismiss();
				mMapViewOnScrollViewFlag ="currentlocation";
				initilizeGoogleMap();
				getCurrentLocation();
			}else{
				Toast.makeText(this, "Google Play Services must be installed.",Toast.LENGTH_SHORT).show();
			}
			break;	
		default:
		}
	}

	// To get google mapview radius
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

	// To get scrolled distance of map
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

	// TO get google map north east LatLng
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

	// Constraint to load store during scroll
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
		// To notify  system that its time to run garbage collector service
		System.gc();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mNotificationReceiver);
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
		if(mConnectionAvailabilityChecking.ConnectivityCheck(CardPurchase.this)){
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
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(CardPurchase.this,ZouponsConstants.sCustomerModuleFlag);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(CardPurchase.this).checkIfSessionExpires();
		mZpayMenuBarZsend.setBackgroundColor(R.drawable.footer_dark_blue_new);
		mZpayMenuBarZapprove.setBackgroundResource(R.drawable.header_2);
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(CardPurchase.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		if(mGoogleMap != null){
			Log.i("Google map", "camera change listner set");
			mGoogleMap.setOnCameraChangeListener(CardPurchase.this);
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
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(CardPurchase.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	/**
	 * function to load map
	 * */
	private void initilizeGoogleMap() {
		if (mGoogleMap == null) {
			mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.step1_MapView)).getMap();
			mGoogleMapUISettings = mGoogleMap.getUiSettings();
			mGoogleMapUISettings.setZoomControlsEnabled(false);
			mGoogleMapUISettings.setMyLocationButtonEnabled(false);
			mGoogleMapUISettings.setCompassEnabled(false);
			mGoogleMapUISettings.setRotateGesturesEnabled(false);
			mGoogleMap.setInfoWindowAdapter(new CustomMapViewCallout(CardPurchase.this));
			mGoogleMap.setOnInfoWindowClickListener(new CustomMapCalloutClickListener(CardPurchase.this,mPageFlag));
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
			distance=getMapViewRadius(MapViewCenter.mMapViewCenter_GeoPoint);
			//Check the user scrolled distance is greater than our condition
			flag=mapviewstoreloadingconstraint((int)mGoogleMap.getCameraPosition().zoom,mScrolledDistance);
		}

		if(flag){ // If scroll constraint is success
			CardPurchase.mCurrentLocation.setEnabled(false);
			CurrentLocation.CurrentLocation_Latitude=String.valueOf(MapViewCenter.mMapViewCenter_GeoPoint.latitude);
			CurrentLocation.CurrentLocation_Longitude=String.valueOf(MapViewCenter.mMapViewCenter_GeoPoint.longitude);
			if(CardPurchase.mMapViewOnScrollViewFlag.equals("currentlocation")){
				mZPAYStoreDetailsStart="0";
				mZPAYStoreDetailsEndLimit="20";
				mZPAYStoreDetailsCount = "0";	
				mIsZPAYLoadMore = false;
				/*if(mShopListView.getFooterViewsCount() == 0){
					mShopListView.addFooterView(mFooterLayout);	
				}*/
				storenearThread = new StoreNearCurrentLocationAsyncThread(CardPurchase.this,mGoogleMap,mShopListView,mCurrentLocation,getMapViewRadius(mGoogleMap.getCameraPosition().target),mPageFlag,mFooterLayout,"NOPROGRESS",mPageFlag,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude,false);
				storenearThread.execute(CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
			}else if(CardPurchase.mMapViewOnScrollViewFlag.equals("search")){
				//Set MenuSearchBarClickListener CLICKFLAG to true 
				MenuBarSearchClickListener.sZPAYFLAG=true;
				mZPAYStoreDetailsStart="0";
				mZPAYStoreDetailsEndLimit="20";
				mZPAYStoreDetailsCount = "0";	
				mIsZPAYLoadMore = false;
				/*if(mShopListView.getFooterViewsCount() == 0){
					mShopListView.addFooterView(mFooterLayout);	
				}*/
				storeslocationasynchthread = new StoresLocationAsynchThread(CardPurchase.this,shrinkSearch,mSearchStoreName,searchBar,mZpayStep1FreezeView,mGoogleMap,mShopListView,mCurrentLocation,distance,"CardPurchase",mCancelStoreName,mSearchBoxContainer,mPageFlag,mFooterLayout,"NOPROGRESS",false,mDeviceCurrentLocationLatitude,mDeviceCurrentLocationLongitude);
				storeslocationasynchthread.execute(search_storeId,CurrentLocation.CurrentLocation_Latitude,CurrentLocation.CurrentLocation_Longitude);
			}
		}else{
			Log.i(TAG,"Sorry Not Loading");
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
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
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}

	// To check availability of google play services with supportable version
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

	// Error dialog to show if google play services not installed/unsupportable version
	void showErrorDialog(int code) {
		mUpdateGooglePlayServicesDialog = GooglePlayServicesUtil.getErrorDialog(code, this,REQUEST_CODE_RECOVER_PLAY_SERVICES);
		mUpdateGooglePlayServicesDialog.show();
	}

}
