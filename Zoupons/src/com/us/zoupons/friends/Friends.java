package com.us.zoupons.friends;

import java.util.ArrayList;

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

import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.POJOFBfriendListData;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.GoogleAccountHelper.BackGroundAsyncTaskFlagClass;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.android.AsyncThreadClasses.GetSocialFriendsTask;
import com.us.zoupons.android.AsyncThreadClasses.ShareStoreAsynchThread;
import com.us.zoupons.android.listview.inflater.classes.CustomFBFriendListAdapter;
import com.us.zoupons.notification.CustomNotificationAdapter;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotificationItemClickListener;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.social.SearchFriendsTextWatcher;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;


public class Friends extends Activity{

	public static MyHorizontalScrollView scrollView;
	public static View leftmenu,rightmenu;
	View app;
	ImageView btnSlide;
	public static Button FriendsListFreezeButton;
	public Typeface mZouponsFont;
	public static LinearLayout mFriendsHome,mFriendsZpay,mFriendsInvoiceCenter,mFriendsGiftcards,mFriendsManageCards,mFriendsReceipts,mFriendsMyFavourites,mFriendsMyFriends,mFriendsChat,mFriendsRewards,mFriendsSettings,
	mFriendsLogout,mFriendsLoginLayout,mSendGiftCards,mInviteFriend;
	public static TextView mFriendsHomeText,mFriendsZpayText,mFriendsInvoiceCenterText,mFriendsGiftCardsText,mFriendsManageCardsText,mFriendsReceiptsText,mFriendsMyFavoritesText,mFriendsMyFriendsText,mFriendsChatText,mFriendsRewardsText,
	mFriendsSettingsText,mFriendsLogoutText,mSendGiftCardText,mInviteGiftCardText;
	public RelativeLayout btnLogout; 
	public ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	public Button mNotificationCount;
	ViewGroup friendListContainer,friendsfooterContainer,SocialfriendsOptionsContainer;
	int btnWidth;
	public int mScreenWidth;
	public double mFriendsMenuWidth;
	public static String TAG = "Friends";
	public NetworkCheck connectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private ProgressDialog mProgressDialog= null;
	private ListView mFriendList,mSocialTypesList;
	public static EditText mFriendNameSearch;
	private Button mSearchCancel;
	private TextView mBackButton,mImportFriendMenu;
	private static String APP_ID = "319327104838055";
	private SharedPreferences mPrefs;
	public static String friendName="",friendEmailId="",invitestatus;
	public static String friendId="",isZouponsFriend="",friendTimeZone="";
	//Notification Pop up layout declaration
	private View mPopUpLayout,backmenusplitter,importmenusplitter;
	private ListView mNotificationList;
	private ScheduleNotificationSync mNotificationSync;
	public static ScrollView leftMenuScrollView;
	
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	public ImageView mTabBarLoginChoice;
	
	Bundle bundle = new Bundle();
	Message msg_response = new Message();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		connectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice = new ZouponsWebService(Friends.this);
		parsingclass = new ZouponsParsingClass(getApplicationContext());

		mProgressDialog=new ProgressDialog(this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage("Loading Friends...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);

		leftmenu = inflater.inflate(R.layout.horz_scroll_menu, null);
		rightmenu = inflater.inflate(R.layout.friends_rightmenu, null);
		app = inflater.inflate(R.layout.fb_friendlist, null);

		final ViewGroup mFriendsHeader = (ViewGroup) app.findViewById(R.id.store_info_tabbar);
		friendListContainer = (ViewGroup) app.findViewById(R.id.store_info_container); 
		friendsfooterContainer = (ViewGroup) app.findViewById(R.id.friends_footerLayoutId);
		SocialfriendsOptionsContainer = (ViewGroup) app.findViewById(R.id.social_import_friend_container);
		btnSlide = (ImageView)mFriendsHeader.findViewById(R.id.BtnSlide_storeinfo);
		btnLogout = (RelativeLayout) mFriendsHeader.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(Friends.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(Friends.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		
		final View[] children = new View[] { leftmenu, app ,rightmenu};
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx,new SizeCallbackForMenu(btnSlide));
		//Call function to get width of the screen
		mScreenWidth=getScreenWidth(); 	
		if(mScreenWidth>0){	//To fix Home Page menubar items width
			mFriendsMenuWidth=mScreenWidth/4;
			Log.i(TAG,"ScreenWidth : "+mScreenWidth+"\n"+"MenuItemWidth : "+mFriendsMenuWidth);
		}
		mFriendList = (ListView) friendListContainer.findViewById(R.id.fb_friendlistId);
		mFriendNameSearch = (EditText) friendListContainer.findViewById(R.id.fb_friendlist_searchId);  
		mSearchCancel = (Button) friendListContainer.findViewById(R.id.fb_friendsearch_buttonId);
		mBackButton = (TextView) friendsfooterContainer.findViewById(R.id.friends_leftFooterText);
		mImportFriendMenu = (TextView) friendsfooterContainer.findViewById(R.id.friends_import_friends_menuId);
		mSocialTypesList = (ListView) SocialfriendsOptionsContainer.findViewById(R.id.social_listId);
		FriendsListFreezeButton = (Button) app.findViewById(R.id.store_info_freeze);

		mBackButton.setVisibility(View.INVISIBLE);
		backmenusplitter = friendsfooterContainer.findViewById(R.id.friendlist_backmenusplitter);
		importmenusplitter = friendsfooterContainer.findViewById(R.id.friendlist_importmenusplitter);
		backmenusplitter.setVisibility(View.INVISIBLE);
		
		ViewGroup menuLeftItems = (ViewGroup) leftmenu.findViewById(R.id.menuitems);
		ViewGroup menuRightItems = (ViewGroup) rightmenu.findViewById(R.id.rightmenuitems);

		leftMenuScrollView = (ScrollView) leftmenu.findViewById(R.id.leftmenu_scrollview);
		leftMenuScrollView.fullScroll(View.FOCUS_UP);
		leftMenuScrollView.pageScroll(View.FOCUS_UP);
		
		mFriendsHome = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_home);
		mFriendsZpay = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_zpay);
		mFriendsInvoiceCenter = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_invoicecenter);
		mFriendsGiftcards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_zgiftcards);
		mFriendsManageCards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_managecards);
		mFriendsReceipts = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_receipts);
		mFriendsMyFavourites = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_myfavourites);
		mFriendsMyFriends = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_myfriends);
		mFriendsChat = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_customercenter);
		mFriendsRewards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_rewards);
		mFriendsSettings = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_settings);
		mFriendsLogout = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_logout);

		mFriendsHomeText = (TextView) menuLeftItems.findViewById(R.id.menuHome);
		mFriendsHomeText.setTypeface(mZouponsFont);
		mFriendsZpayText = (TextView) menuLeftItems.findViewById(R.id.menuZpay);
		mFriendsZpayText.setTypeface(mZouponsFont);
		mFriendsInvoiceCenterText = (TextView) menuLeftItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
		mFriendsInvoiceCenterText.setTypeface(mZouponsFont);
		mFriendsGiftCardsText = (TextView) menuLeftItems.findViewById(R.id.menuGiftCards);
		mFriendsGiftCardsText.setTypeface(mZouponsFont);
		mFriendsManageCardsText = (TextView) menuLeftItems.findViewById(R.id.menuManageCards);
		mFriendsManageCardsText.setTypeface(mZouponsFont);
		mFriendsReceiptsText = (TextView) menuLeftItems.findViewById(R.id.menuReceipts);
		mFriendsReceiptsText.setTypeface(mZouponsFont);
		mFriendsMyFavoritesText = (TextView) menuLeftItems.findViewById(R.id.menufavorites);
		mFriendsMyFavoritesText.setTypeface(mZouponsFont);
		mFriendsMyFriendsText = (TextView) menuLeftItems.findViewById(R.id.menuMyFriends);
		mFriendsMyFriendsText.setTypeface(mZouponsFont);
		mFriendsChatText = (TextView) menuLeftItems.findViewById(R.id.menuCustomerCenter);
		mFriendsChatText.setTypeface(mZouponsFont);
		mFriendsRewardsText = (TextView) menuLeftItems.findViewById(R.id.menuRewards);
		mFriendsRewardsText.setTypeface(mZouponsFont);
		mFriendsSettingsText = (TextView) menuLeftItems.findViewById(R.id.menuSettings);
		mFriendsSettingsText.setTypeface(mZouponsFont);
		mFriendsLogoutText = (TextView) menuLeftItems.findViewById(R.id.menuLogout);
		mFriendsLogoutText.setTypeface(mZouponsFont);

		mSendGiftCards = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_sendGiftcard);
		mInviteFriend = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_inviteId);

		mSendGiftCardText = (TextView) menuRightItems.findViewById(R.id.rightmenu_sendGiftcardTextId);
		mInviteGiftCardText = (TextView) menuRightItems.findViewById(R.id.rightmenu_inviteTextId);

		FriendsListFreezeButton.setOnClickListener(new ClickListenerForScrolling(Friends.this,scrollView, leftmenu, rightmenu, 0, FriendsListFreezeButton));
		btnSlide.setOnClickListener(new ClickListenerForScrolling(Friends.this,scrollView, leftmenu, rightmenu, 1, FriendsListFreezeButton));
		MenuUtilityClass.fbFriendListView(Friends.this, mFriendList, scrollView, leftmenu, rightmenu,FriendsListFreezeButton);
		
		if(!BackGroundAsyncTaskFlagClass.BackGroundAsyncTaskFlag){
			/*To get Friend list*/
			GetSocialFriendsTask mFriendListTask = new GetSocialFriendsTask(Friends.this, mFriendList,friendListContainer,SocialfriendsOptionsContainer,mImportFriendMenu,importmenusplitter,"friends");
			mFriendListTask.execute();
		}else{
			mProgressDialog.show();
			Thread importingThread = new Thread(){
				@Override
				public void run(){
					try{
						while(BackGroundAsyncTaskFlagClass.BackGroundAsyncTaskFlag){
							sleep(100);
							if(!BackGroundAsyncTaskFlagClass.BackGroundAsyncTaskFlag){
								updateHandler(bundle, msg_response, "GetSocialFriendsTask");
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					mProgressDialog.dismiss();
				}
			};
			importingThread.start();
		}

		mFriendNameSearch.setFocusable(false);
		mFriendNameSearch.setFocusableInTouchMode(false);
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
		// To add text watcher for searching friend list
		mFriendNameSearch.addTextChangedListener(new SearchFriendsTextWatcher(Friends.this, mFriendList, "Friends"));

		mSearchCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WebServiceStaticArrays.mSearchedFriendList.clear();
				mFriendNameSearch.getText().clear();
				mFriendList.setAdapter(new CustomFBFriendListAdapter(Friends.this,WebServiceStaticArrays.mSocialNetworkFriendList,"friends"));
				// To hide soft keyboard
				InputMethodManager softkeyboardevent = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				softkeyboardevent.hideSoftInputFromWindow(mFriendNameSearch.getWindowToken(), 0);
			}
		}); 

		/*mSendGiftCards.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("menu click", "send gift card");
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				ZpayStep2SearchEnable.searchEnableFlag=true;	//flag to enable search header text in zpay step2 page
				Intent intent_sendgiftcard=new Intent(Friends.this,zpay_step1.class);
				intent_sendgiftcard.putExtra("pageflag", "giftcard");
				intent_sendgiftcard.putExtra("FriendName", friendName);
				intent_sendgiftcard.putExtra("FriendId", friendId);
				intent_sendgiftcard.putExtra("IsZouponsFriend", isZouponsFriend);
				startActivity(intent_sendgiftcard);
			}
		});*/

		mFriendsHome.setOnClickListener(new MenuItemClickListener(menuLeftItems, Friends.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFriendsZpay.setOnClickListener(new MenuItemClickListener(menuLeftItems, Friends.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFriendsInvoiceCenter.setOnClickListener(new MenuItemClickListener(menuLeftItems, Friends.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFriendsGiftcards.setOnClickListener(new MenuItemClickListener(menuLeftItems, Friends.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFriendsManageCards.setOnClickListener(new MenuItemClickListener(menuLeftItems, Friends.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFriendsReceipts.setOnClickListener(new MenuItemClickListener(menuLeftItems, Friends.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFriendsMyFavourites.setOnClickListener(new MenuItemClickListener(menuLeftItems,Friends.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFriendsMyFriends.setOnClickListener(new MenuItemClickListener(menuLeftItems,Friends.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFriendsChat.setOnClickListener(new MenuItemClickListener(menuLeftItems,Friends.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFriendsRewards.setOnClickListener(new MenuItemClickListener(menuLeftItems,Friends.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFriendsSettings.setOnClickListener(new MenuItemClickListener(menuLeftItems,Friends.this,POJOMainMenuActivityTAG.TAG=TAG));
		mFriendsLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				
				if(NormalPaymentAsyncTask.mCountDownTimer!=null){
					NormalPaymentAsyncTask.mCountDownTimer.cancel();
					NormalPaymentAsyncTask.mCountDownTimer = null;
					Log.i(TAG,"Timer Stopped Successfully");
				}
				
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(Friends.this).execute();
				Intent intent_logout = new Intent(Friends.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_logout);
				finish();
			}
		});


		mInviteFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("menu click", "invite friend");
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				InviteFriendAsyncTask mInviteFriendTask = new InviteFriendAsyncTask(Friends.this);
				mInviteFriendTask.execute(friendEmailId);
			}
		});

		mNotificationImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Inflating custom Layout for PopUpWindow.
				mCalloutTriangleImage.setVisibility(View.VISIBLE);
				// Initializing PopUpWindow
				int popupheight = getWindowManager().getDefaultDisplay().getHeight()/2;
				// Initializing PopUpWindow
				final PopupWindow mPopUpWindow = new PopupWindow(mPopUpLayout,android.view.WindowManager.LayoutParams.WRAP_CONTENT,popupheight,true);
				mPopUpWindow.setWidth(mFriendsHeader.getWidth()-20);
				mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopUpWindow.setOutsideTouchable(false);
				mPopUpWindow.showAsDropDown(mFriendsHeader, 0, 20);
				mNotificationList.setAdapter(new CustomNotificationAdapter(Friends.this));
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

		mLogoutImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(Friends.this).execute();
				Intent intent_logout = new Intent(Friends.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				finish();
				startActivity(intent_logout);
			}
		});

		mImportFriendMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				friendListContainer.setVisibility(View.GONE);
				SocialfriendsOptionsContainer.setVisibility(View.VISIBLE);
				mBackButton.setVisibility(View.VISIBLE);
				backmenusplitter.setVisibility(View.VISIBLE);
				mImportFriendMenu.setVisibility(View.INVISIBLE);
				importmenusplitter.setVisibility(View.INVISIBLE);
				ArrayList<Object> mSocialDetails = new ArrayList<Object>(2);
				// To display social networking types with count..
				int google_friends_count=0,yahoo_friends_count=0;
				String google_friends_last_updated="",yahoo_friends_last_updated="";
				for(int i=0;i<WebServiceStaticArrays.mSocialNetworkFriendList.size();i++){
					POJOFBfriendListData mFriendData = (POJOFBfriendListData) WebServiceStaticArrays.mSocialNetworkFriendList.get(i);
					if(mFriendData.friend_provider.equalsIgnoreCase("Google")){
						google_friends_count = google_friends_count + 1;
					}else if(mFriendData.friend_provider.equalsIgnoreCase("Yahoo")){
						// count for other social networking friends...
						yahoo_friends_count = yahoo_friends_count + 1;
					}
					google_friends_last_updated = mFriendData.google_friends_updated_time;
				}
				SocialNetworkingDetails mGoogleSocialNetworkDetails = new SocialNetworkingDetails();
				mGoogleSocialNetworkDetails.mFriendsCount = String.valueOf(google_friends_count);
				mGoogleSocialNetworkDetails.mLastUpdatedDate = google_friends_last_updated;
				mSocialDetails.add(0,mGoogleSocialNetworkDetails);
				mSocialTypesList.setAdapter(new SocialNetworkingAdapter(Friends.this, mSocialDetails));		
			}
		});
		
		mBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(friendListContainer.getVisibility() == View.VISIBLE){
					finish();
				}else{
					SocialfriendsOptionsContainer.setVisibility(View.GONE);
					friendListContainer.setVisibility(View.VISIBLE);
					mImportFriendMenu.setVisibility(View.VISIBLE);
					importmenusplitter.setVisibility(View.VISIBLE);
					mBackButton.setVisibility(View.INVISIBLE);
					backmenusplitter.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse){
		bundle.putString("returnResponse", "GetSocialFriendsTask");
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}
	
	Handler handler_response = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.getData().getString("returnResponse").toString().equalsIgnoreCase("GetSocialFriendsTask")){
				GetSocialFriendsTask mFriendListTask = new GetSocialFriendsTask(Friends.this, mFriendList,friendListContainer,SocialfriendsOptionsContainer,mImportFriendMenu,importmenusplitter,"friends");
				mFriendListTask.execute();
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("Activity result", requestCode +" "+resultCode);
		if(requestCode == 700 && resultCode == RESULT_OK){
			if(data.hasExtra("import_status") && data.getExtras().getBoolean("import_status")){
				SocialfriendsOptionsContainer.setVisibility(View.GONE);
				friendListContainer.setVisibility(View.VISIBLE);
				mImportFriendMenu.setVisibility(View.VISIBLE);
				importmenusplitter.setVisibility(View.VISIBLE);
				mBackButton.setVisibility(View.INVISIBLE);
				backmenusplitter.setVisibility(View.INVISIBLE);
				GetSocialFriendsTask mFriendListTask = new GetSocialFriendsTask(Friends.this, mFriendList,friendListContainer,SocialfriendsOptionsContainer,mImportFriendMenu,importmenusplitter,"friends");
				mFriendListTask.execute();	
			}else if(data.hasExtra("import_status") && !data.getExtras().getBoolean("import_status")){
				SocialfriendsOptionsContainer.setVisibility(View.GONE);
				friendListContainer.setVisibility(View.VISIBLE);
				mImportFriendMenu.setVisibility(View.VISIBLE);
				mBackButton.setVisibility(View.INVISIBLE);
				backmenusplitter.setVisibility(View.INVISIBLE);
				importmenusplitter.setVisibility(View.VISIBLE);
			}
		}
	}

	private Handler handler_facebook = new Handler(){
		public void handleMessage(Message msg) {
			Log.i("handle","Message: "+msg);
			if(mProgressDialog.isShowing()){
				mProgressDialog.dismiss();
			}
			if(msg.obj.equals("cancel")){
				alertBox_service("Information", "You have to loggin to Facebook for further process.");
			}else if(msg.obj.equals("friendlist")){
				GetSocialFriendsTask mFriendTask = new GetSocialFriendsTask(Friends.this,mFriendList,friendListContainer,SocialfriendsOptionsContainer,mImportFriendMenu,importmenusplitter,"friends");
				mFriendTask.execute();
			}else if(msg.obj.equals("sharestore")){
				ShareStoreAsynchThread sharestoreasynchthread = new ShareStoreAsynchThread(Friends.this);
				sharestoreasynchthread.execute("FB","JustLogged");
			}else if(msg.obj.equals("sharecoupon")){
				Log.i(TAG,"Coupon ShareByFB: "+"Work in Progress");
			}
		}
	};

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Log.i("method", "on focus changed");
		mFriendNameSearch.setFocusable(true);
		mFriendNameSearch.setFocusableInTouchMode(true);
		if((scrollView.getLeft() != app.getLeft()) && (scrollView.getTop() != app.getTop()) ){
			Log.i("focus", app.getLeft()+" "+app.getTop());
			scrollView.smoothScrollTo(app.getLeft(), app.getTop());
		}else{
			Log.i("focus", "already set");
		}
	}

	static class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View leftMenu,rightMenu;
		int menuFlag;
		Button mFreezeView;
		Context mContext;
		/**
		 * Menu must NOT be out/shown to start with.
		 */

		public ClickListenerForScrolling(Context context,HorizontalScrollView scrollView, View leftmenu, View rightmenu,int menuflag,Button freezeview) {
			super();
			this.mContext = context;
			this.scrollView = scrollView;
			this.leftMenu = leftmenu;
			this.rightMenu = rightmenu;
			this.menuFlag=menuflag;
			this.mFreezeView = freezeview;
		}

		@Override
		public void onClick(View v) {
			/*InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);*/
			InputMethodManager softkeyboardevent = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			softkeyboardevent.hideSoftInputFromWindow(Friends.mFriendNameSearch.getWindowToken(), 0);

			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);
			
			Context context = leftMenu.getContext();
			int menuWidth = leftmenu.getMeasuredWidth();
			Log.i(TAG,"Left Menu Width : "+ menuWidth+" "+menuFlag+" "+MenuOutClass.WHOLE_MENUOUT);
			
			mFriendNameSearch.clearFocus();
			mFriendNameSearch.setFocusable(false);
			mFriendNameSearch.setFocusableInTouchMode(false);
			
			
			
			if (!MenuOutClass.FRIENDS_MENUOUT) {
				if(menuFlag==1){
					// Scroll to open left menu
					final int left = 0;
					// Ensure menu is visible
					leftMenu.setVisibility(View.VISIBLE);
					mFreezeView.setVisibility(View.VISIBLE);
					
					final Bundle bundle = new Bundle();
					final Message msg_response = new Message();
					
					Thread leftMenuOpenThread = new Thread(){
						@Override
						public void run(){
							try{
								sleep(100);
								bundle.putInt("leftmenu", left);
								bundle.putString("open", "yes");
								msg_response.setData(bundle);
								handler_openleftmenu.sendMessage(msg_response);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					};leftMenuOpenThread.start();
					
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
				mFriendNameSearch.setFocusable(true);
				mFriendNameSearch.setFocusableInTouchMode(true);
				mFriendNameSearch.requestFocus();
			}
			MenuOutClass.FRIENDS_MENUOUT = !MenuOutClass.FRIENDS_MENUOUT;
		}
	}

	static Handler handler_openleftmenu = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg!=null){
				Bundle b = msg.getData();
				String key = b.getString("open");
				int leftmenu = b.getInt("leftmenu");
				if(key.equalsIgnoreCase("yes")){
					Friends.scrollView.smoothScrollTo(leftmenu, 0);
				}
			}
		}
	};
	
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

	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(Friends.this);
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
	}

	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(Friends.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}

	@Override
	protected void onResume() {
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(Friends.this);
		mNotificationSync.setRecurringAlarm();

		new CheckUserSession(Friends.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(Friends.this);
		mLogoutSession.setLogoutTimerAlarm();

		super.onResume();
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
}