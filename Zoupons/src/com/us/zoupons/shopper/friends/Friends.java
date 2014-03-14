package com.us.zoupons.shopper.friends;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.R;
import com.us.zoupons.android.asyncthread_class.GetSocialFriendsTask;
import com.us.zoupons.backgroundservices.ContactListService;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.listview_inflater_classes.CustomFBFriendListAdapter;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.mobilepay.CardPurchase;
import com.us.zoupons.mobilepay.NormalPaymentAsyncTask;
import com.us.zoupons.mobilepay.ZpayStep2SearchEnable;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.shopper.share.SearchFriendsTextWatcher;

/**
 * 
 * Activity to list friend details
 *
 */

public class Friends extends Activity{

	// Initializing views and variables
	public static MyHorizontalScrollView sScrollView;       
	public static View sLeftmenu,sRightmenu;
	private View app;
	private ImageView btnSlide;
	public static Button FriendsListFreezeButton;
	private Typeface mZouponsFont;
	public static LinearLayout mFriendsHome,mFriendsZpay,mFriendsInvoiceCenter,mFriendsGiftcards,mFriendsManageCards,mFriendsReceipts,mFriendsMyFavourites,mFriendsMyFriends,mFriendsChat,mFriendsRewards,mFriendsSettings,
	mFriendsLogout,mFriendsLoginLayout,mSendGiftCards,mInviteFriend;
	public static TextView mFriendsHomeText,mFriendsZpayText,mFriendsInvoiceCenterText,mFriendsGiftCardsText,mFriendsManageCardsText,mFriendsReceiptsText,mFriendsMyFavoritesText,mFriendsMyFriendsText,mFriendsChatText,mFriendsRewardsText,
	mFriendsSettingsText,mFriendsLogoutText,mSendGiftCardText,mInviteFriendText;
	public static ImageView mSendGiftCardImage,mInviteFriendImage;
	private ImageView mFriendsZpayImage,mFriendsInvoiceCenterImage,mFriendsGiftCardsImage,mFriendsManageCardsImage,mFriendsReceiptsImage,mFriendsMyFavoritesImage,mFriendsMyFriendsImage,mFriendsChatImage,mFriendsRewardsImage,
	mFriendsSettingsImage;
	private RelativeLayout btnLogout; 
	private ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	private Button mNotificationCount;
	private ViewGroup friendListContainer,friendsfooterContainer,SocialfriendsOptionsContainer;
	private String TAG = "Friends";
	private ProgressDialog mProgressDialog= null;
	private ListView mFriendList;
	public static EditText mFriendNameSearch;
	private Button mSearchCancel;
	private TextView mBackButton,mImportFriendMenu;
	public static String friendName="",friendEmailId="",invitestatus;
	public static String friendId="",isZouponsFriend="",friendTimeZone="";
	//Notification Pop up layout declaration
	private View backmenusplitter,importmenusplitter;
	private ScheduleNotificationSync mNotificationSync;
	public static ScrollView leftMenuScrollView;
	//Logout without user interaction after 5 minute
	private CheckLogoutSession mLogoutSession;
	private ImageView mTabBarLoginChoice;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Referencing view from layout file
		LayoutInflater inflater = LayoutInflater.from(this);
		sScrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(sScrollView);
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mProgressDialog=new ProgressDialog(this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage("Loading Friends...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
		sLeftmenu = inflater.inflate(R.layout.shopper_leftmenu, null);
		sRightmenu = inflater.inflate(R.layout.friends_rightmenu, null);
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

		final View[] children = new View[] { sLeftmenu, app ,sRightmenu};
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx,new SizeCallbackForMenu(btnSlide));
		mFriendList = (ListView) friendListContainer.findViewById(R.id.fb_friendlistId);
		mFriendNameSearch = (EditText) friendListContainer.findViewById(R.id.fb_friendlist_searchId);  
		mSearchCancel = (Button) friendListContainer.findViewById(R.id.fb_friendsearch_buttonId);
		mBackButton = (TextView) friendsfooterContainer.findViewById(R.id.friends_leftFooterText);
		mImportFriendMenu = (TextView) friendsfooterContainer.findViewById(R.id.friends_import_friends_menuId);
		FriendsListFreezeButton = (Button) app.findViewById(R.id.store_info_freeze);

		mBackButton.setVisibility(View.INVISIBLE);
		backmenusplitter = friendsfooterContainer.findViewById(R.id.friendlist_backmenusplitter);
		importmenusplitter = friendsfooterContainer.findViewById(R.id.friendlist_importmenusplitter);
		backmenusplitter.setVisibility(View.INVISIBLE);

		ViewGroup menuLeftItems = (ViewGroup) sLeftmenu.findViewById(R.id.menuitems);
		ViewGroup menuRightItems = (ViewGroup) sRightmenu.findViewById(R.id.rightmenuitems);

		leftMenuScrollView = (ScrollView) sLeftmenu.findViewById(R.id.leftmenu_scrollview);
		leftMenuScrollView.fullScroll(View.FOCUS_UP);
		leftMenuScrollView.pageScroll(View.FOCUS_UP);
		// Sliding left menu items initialisation 
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
		mInviteFriendText = (TextView) menuRightItems.findViewById(R.id.rightmenu_inviteTextId);

		mSendGiftCardImage = (ImageView) menuRightItems.findViewById(R.id.rightmenu_sendgiftImageId);
		mInviteFriendImage = (ImageView) menuRightItems.findViewById(R.id.rightmenu_inviteImageId);

		FriendsListFreezeButton.setOnClickListener(new ClickListenerForScrolling(Friends.this,sScrollView, sLeftmenu, sRightmenu, 0, FriendsListFreezeButton));
		btnSlide.setOnClickListener(new ClickListenerForScrolling(Friends.this,sScrollView, sLeftmenu, sRightmenu, 1, FriendsListFreezeButton));
		MenuUtilityClass.fbFriendListView(Friends.this, mFriendList, sScrollView, sLeftmenu, sRightmenu,FriendsListFreezeButton);

		if(WebServiceStaticArrays.mSocialNetworkFriendList.size()>0){ // If Friends list is not cleared
			mFriendList.setAdapter(new CustomFBFriendListAdapter(this,WebServiceStaticArrays.mSocialNetworkFriendList,"friends"));
		}else{
			// To load contact friend list
			ContactListService mGetContacts = new ContactListService(this,mFriendList,"friends");
			mGetContacts.execute();
		}
		mFriendNameSearch.setFocusable(false);
		mFriendNameSearch.setFocusableInTouchMode(false);
		// Notitification pop up layout declaration
		mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
		mNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mFriendsHeader, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		mNotificationCount.setOnClickListener(new ManageNotificationWindow(this,mFriendsHeader, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
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

		mSendGiftCards.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*v.setBackgroundResource(R.drawable.gradient_bg_hover);
				ZpayStep2SearchEnable.searchEnableFlag=true;	//flag to enable search header text in zpay step2 page
				Intent intent_sendgiftcard=new Intent(Friends.this,CardPurchase.class);
				intent_sendgiftcard.putExtra("pageflag", "giftcard");
				intent_sendgiftcard.putExtra("FriendName", friendName);
				intent_sendgiftcard.putExtra("FriendId", friendId);
				intent_sendgiftcard.putExtra("IsZouponsFriend", isZouponsFriend);
				startActivity(intent_sendgiftcard);*/
			}
		});

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
				}

				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(Friends.this,"FromManualLogOut").execute();
			}
		});


		mInviteFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				InviteFriendAsyncTask mInviteFriendTask = new InviteFriendAsyncTask(Friends.this);
				mInviteFriendTask.execute(friendEmailId);
			}
		});

		mLogoutImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(Friends.this,"FromManualLogOut").execute();
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

	// Handler class to update UI items
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

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mFriendNameSearch.setFocusable(true);
		mFriendNameSearch.setFocusableInTouchMode(true);
		if((sScrollView.getLeft() != app.getLeft()) && (sScrollView.getTop() != app.getTop()) ){
			sScrollView.smoothScrollTo(app.getLeft(), app.getTop());
		}else{
		}
	}

	class ClickListenerForScrolling implements OnClickListener {
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
			InputMethodManager softkeyboardevent = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			softkeyboardevent.hideSoftInputFromWindow(Friends.mFriendNameSearch.getWindowToken(), 0);

			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);

			int menuWidth = sLeftmenu.getMeasuredWidth();
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
								bundle.putInt("sLeftmenu", left);
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

	/*	private void setLeftMenuItemStatus(Context context) {
			// TODO Auto-generated method stub
			SharedPreferences mUserDetailsPrefs = context.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
			String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");
			if(user_login_type.equalsIgnoreCase("customer")){  // Customer
				// Zpay LeftMenu 
				mFriendsZpay.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				mFriendsZpay.setEnabled(true);
				mFriendsZpayText.setTextColor(Color.WHITE);
				mFriendsZpayImage.setAlpha(255);
				mFriendsZpay.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsZpay.setEnabled(false);
				mFriendsZpayText.setTextColor(Color.GRAY);
				mFriendsZpayImage.setAlpha(100);
				// InvoiceCenter LeftMenu 
				mFriendsInvoiceCenter.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				mFriendsInvoiceCenter.setEnabled(true);
				mFriendsInvoiceCenterText.setTextColor(Color.WHITE);
				mFriendsInvoiceCenterImage.setAlpha(255);
				mFriendsInvoiceCenter.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsInvoiceCenter.setEnabled(false);
				mFriendsInvoiceCenterText.setTextColor(Color.GRAY);
				mFriendsInvoiceCenterImage.setAlpha(100);
				// Gitcards LeftMenu 
				mFriendsGiftcards.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				mFriendsGiftcards.setEnabled(true);
				mFriendsGiftCardsText.setTextColor(Color.WHITE);
				mFriendsGiftCardsImage.setAlpha(255);
				mFriendsGiftcards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsGiftcards.setEnabled(false);
				mFriendsGiftCardsText.setTextColor(Color.GRAY);
				mFriendsGiftCardsImage.setAlpha(100);
				// ManageCards LeftMenu 
				mFriendsManageCards.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				mFriendsManageCards.setEnabled(true);
				mFriendsManageCardsText.setTextColor(Color.WHITE);
				mFriendsManageCardsImage.setAlpha(255);
				// Receipts LeftMenu 
				mFriendsReceipts.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				mFriendsReceipts.setEnabled(true);
				mFriendsReceiptsText.setTextColor(Color.WHITE);
				mFriendsReceiptsImage.setAlpha(255);
				mFriendsReceipts.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsReceipts.setEnabled(false);
				mFriendsReceiptsText.setTextColor(Color.GRAY);
				mFriendsReceiptsImage.setAlpha(100);
				// Favorites LeftMenu 
				mFriendsMyFavourites.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				mFriendsMyFavourites.setEnabled(true);
				mFriendsMyFavoritesText.setTextColor(Color.WHITE);
				mFriendsMyFavoritesImage.setAlpha(255);
				// Friends LeftMenu 
				mFriendsMyFriends.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				mFriendsMyFriends.setEnabled(true);
				mFriendsMyFriendsText.setTextColor(Color.WHITE);
				mFriendsMyFriendsImage.setAlpha(255);
				// CustomerCenter LeftMenu 
				mFriendsChat.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				mFriendsChat.setEnabled(true);
				mFriendsChatText.setTextColor(Color.WHITE);
				mFriendsChatImage.setAlpha(255);
				// ReferStore LeftMenu 
				mFriendsRewards.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				mFriendsRewards.setEnabled(true);
				mFriendsRewardsText.setTextColor(Color.WHITE);
				mFriendsRewardsImage.setAlpha(255);
				// Settings LeftMenu 
				mFriendsSettings.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				mFriendsSettings.setEnabled(true);
				mFriendsSettingsText.setTextColor(Color.WHITE);
				mFriendsSettingsImage.setAlpha(255);
			}else{  // Guest
				// Zpay LeftMenu
				mFriendsZpay.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsZpay.setEnabled(false);
				mFriendsZpayText.setTextColor(Color.GRAY);
				mFriendsZpayImage.setAlpha(100);
				// InvoiceCenter LeftMenu
				mFriendsInvoiceCenter.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsInvoiceCenter.setEnabled(false);
				mFriendsInvoiceCenterText.setTextColor(Color.GRAY);
				mFriendsInvoiceCenterImage.setAlpha(100);
				// Gitcards LeftMenu
				mFriendsGiftcards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsGiftcards.setEnabled(false);
				mFriendsGiftCardsText.setTextColor(Color.GRAY);
				mFriendsGiftCardsImage.setAlpha(100);
				// ManageCards LeftMenu
				mFriendsManageCards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsManageCards.setEnabled(false);
				mFriendsManageCardsText.setTextColor(Color.GRAY);
				mFriendsManageCardsImage.setAlpha(100);
				// Receipts LeftMenu
				mFriendsReceipts.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsReceipts.setEnabled(false);
				mFriendsReceiptsText.setTextColor(Color.GRAY);
				mFriendsReceiptsImage.setAlpha(100);
				// Favorites LeftMenu 
				mFriendsMyFavourites.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsMyFavourites.setEnabled(false);
				mFriendsMyFavoritesText.setTextColor(Color.GRAY);
				mFriendsMyFavoritesImage.setAlpha(100);
				// Friends LeftMenu
				mFriendsMyFriends.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsMyFriends.setEnabled(false);
				mFriendsMyFriendsText.setTextColor(Color.GRAY);
				mFriendsMyFriendsImage.setAlpha(100);
				// CustomerCenter LeftMenu 
				mFriendsChat.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsChat.setEnabled(false);
				mFriendsChatText.setTextColor(Color.GRAY);
				mFriendsChatImage.setAlpha(100);
				// ReferStore LeftMenu 
				mFriendsRewards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsRewards.setEnabled(false);
				mFriendsRewardsText.setTextColor(Color.GRAY);
				mFriendsRewardsImage.setAlpha(100);
				// Settings LeftMenu
				mFriendsSettings.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				mFriendsSettings.setEnabled(false);
				mFriendsSettingsText.setTextColor(Color.GRAY);
				mFriendsSettingsImage.setAlpha(100);
			}
		}*/
	}

	// Handler class to update UI items
	Handler handler_openleftmenu = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg!=null){
				Bundle b = msg.getData();
				String key = b.getString("open");
				int leftmenu = b.getInt("sLeftmenu");
				if(key.equalsIgnoreCase("yes")){
					Friends.sScrollView.smoothScrollTo(leftmenu, 0);
				}
			}
		}
	};

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
		sScrollView = null; sLeftmenu= null;sRightmenu= null;FriendsListFreezeButton= null;mFriendsHome= null;mFriendsZpay= null;mFriendsInvoiceCenter= null;mFriendsGiftcards= null;mFriendsManageCards= null;mFriendsReceipts= null;mFriendsMyFavourites= null;mFriendsMyFriends= null;mFriendsChat= null;mFriendsRewards= null;mFriendsSettings= null;
		mFriendsLogout= null;mFriendsLoginLayout= null;mSendGiftCards= null;mInviteFriend = null;
		mFriendsHomeText= null;mFriendsZpayText= null;mFriendsInvoiceCenterText= null;mFriendsGiftCardsText= null;mFriendsManageCardsText= null;mFriendsReceiptsText= null;mFriendsMyFavoritesText= null;mFriendsMyFriendsText= null;mFriendsChatText= null;mFriendsRewardsText= null;
		mFriendsSettingsText= null;mFriendsLogoutText= null;mSendGiftCardText= null;mInviteFriendText = null;
		mSendGiftCardImage = null;mInviteFriendImage = null;
		mFriendNameSearch = null; leftMenuScrollView = null;
		// To notify system that its time to run garbage collector service
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
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(Friends.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	protected void onResume() {
		super.onResume();
		POJOMainMenuActivityTAG.TAG = "Friends";
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(Friends.this,ZouponsConstants.sCustomerModuleFlag);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(Friends.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(Friends.this);
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

	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

}