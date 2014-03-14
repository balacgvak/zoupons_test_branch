/**
 * 
 */
package com.us.zoupons.shopper.giftcards_deals;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
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
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.flagclasses.ZPayFlag;
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
import com.us.zoupons.shopper.friends.Friends;

/**
 * Activity to list users giftcards/deal cards
 */
public class MyGiftCards extends Activity {

	// Initializing views and variables
	public static MyHorizontalScrollView sScrollView;
	public static View sLeftMenu,sRightMenu;
	private View app;
	private ImageView btnSlide;
	private RelativeLayout btnLogout;
	public static Button mGiftCardsFreezeView;
	public static LinearLayout mGiftCardsHome,mGiftCardsZpay,mGiftCardsInvoiceCenter,mGiftCardsManageCards,mGiftCardsReceipts,mGiftCardsGiftcards,mGiftCardsMyFavourites,mGiftCardsMyFriends,mGiftCardsChat,mGiftCardsRewards,mGiftCardsSettings,mGiftCardsLogout;
	public static TextView mGiftCardsHomeText,mGiftCardsZpayText,mGiftCardsInvoiceCenterText,mGiftCardsManageCardsText,mGiftCardsReceiptsText,mGiftCardsGiftCardsText,mGiftCardsMyFavoritesText,mGiftCardsMyFriendsText,mGiftCardsChatText,mGiftCardsRewardsText,mGiftCardsSettingsText,mGiftCardsLogoutText;
	public static LinearLayout mGiftCardsSendToFriend,mGiftCardsSelfUse,mGiftCardsTransactionhistory;
	public static TextView mGiftCardsSendToFriendTxt,mGiftCardsSelfUseTxt,mGiftCardsTransactionhistoryTxt;
	public static ImageView mGiftCardsSendToFriendImage,mGiftCardsSelfUseImage,mGiftCardsTransactionhistoryImage;
	private Typeface mZouponsFont;
	private ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	private Button mNotificationCount;
	public static String TAG = "MyGiftCards";
	private int mScreenWidth;
	private double mGiftCardsMenuWidth;
	private LinearLayout mMenuBarMyGiftCards,mMenuBarPurchase,mMenuBarRedeem;
	private TextView mMenuBarMyGiftCardsText,mMenuBarPurchaseText,mMenuBarRedeemText;
	private ListView mGiftCardsListView;
	private Button mRedeem_Cancel,mRedeem_Send;
	private int GiftCards_ZpayViewFlag=1;
	//Notification Pop up layout declaration
	private ScheduleNotificationSync mNotificationSync;
	//Logout without user interaction after 5 minute
	private CheckLogoutSession mLogoutSession;
	private ImageView mTabBarLoginChoice;
	public static ScrollView leftMenuScrollView;
	private View mFooterLayout;
	private TextView mFooterText;
	public static boolean mIsLoadMore;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Referencing view from layout file
		LayoutInflater inflater = LayoutInflater.from(this);
		sScrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(sScrollView);
		mIsLoadMore = false;
		POJOLimit.mStartLimit="0";
		POJOLimit.mEndLimit="20";
		POJOLimit.mTotalCount="0";
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		sLeftMenu = inflater.inflate(R.layout.shopper_leftmenu, null);
		sRightMenu = inflater.inflate(R.layout.giftcards_rightmenu, null);
		app = inflater.inflate(R.layout.giftcards, null);
		ViewGroup leftMenuItems = (ViewGroup) sLeftMenu.findViewById(R.id.menuitems);
		ViewGroup rightMenuItems = (ViewGroup) sRightMenu.findViewById(R.id.giftcards_rightmenuitems);
		final ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.giftcards_tabBar);
		ViewGroup giftcardscontainer = (ViewGroup) app.findViewById(R.id.giftcards_container);
		ViewGroup giftcardsmenubarcontainer = (ViewGroup) app.findViewById(R.id.menubarcontainer_gifcards);
		final ViewGroup giftcardslistviewparent = (ViewGroup) giftcardscontainer.findViewById(R.id.giftcards_listview_parent);
		ViewGroup giftcardslistviewholder = (ViewGroup) giftcardscontainer.findViewById(R.id.giftcardslistviewholder);
		final ViewGroup giftcardsredeemholder = (ViewGroup) giftcardscontainer.findViewById(R.id.giftcards_redeem);
		leftMenuScrollView = (ScrollView) sLeftMenu.findViewById(R.id.leftmenu_scrollview);
		leftMenuScrollView.fullScroll(View.FOCUS_UP);
		leftMenuScrollView.pageScroll(View.FOCUS_UP);
		mGiftCardsFreezeView = (Button) app.findViewById(R.id.giftcards_freezeview);
		mGiftCardsFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, mGiftCardsFreezeView));
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, mGiftCardsFreezeView));
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MyGiftCards.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count); 
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MyGiftCards.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Notitification pop up layout declaration
		mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
		mNotificationImage.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		mNotificationCount.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		final View[] children = new View[] { sLeftMenu, app, sRightMenu};
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
		//Call function to get width of the screen
		mScreenWidth=getScreenWidth(); 	
		if(mScreenWidth>0){	//To fix Home Page menubar items width
			mGiftCardsMenuWidth=mScreenWidth/3;
		}
		mRedeem_Cancel = (Button) giftcardsredeemholder.findViewById(R.id.giftcards_redeem_cancel);
		mRedeem_Cancel.setTypeface(mZouponsFont);
		mRedeem_Send = (Button) giftcardsredeemholder.findViewById(R.id.giftcards_redeem_send);
		mRedeem_Send.setTypeface(mZouponsFont);
		//LeftMenu
		mGiftCardsHome = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_home);
		mGiftCardsZpay = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zpay);
		mGiftCardsInvoiceCenter = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_invoicecenter);
		mGiftCardsManageCards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_managecards);
		mGiftCardsReceipts = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_receipts);
		mGiftCardsGiftcards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zgiftcards);
		mGiftCardsMyFavourites = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfavourites);
		mGiftCardsMyFriends = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfriends);
		mGiftCardsChat = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_customercenter);
		mGiftCardsRewards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_rewards);
		mGiftCardsSettings = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_settings);
		mGiftCardsLogout = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_logout);

		mGiftCardsHomeText = (TextView) leftMenuItems.findViewById(R.id.menuHome);
		mGiftCardsHomeText.setTypeface(mZouponsFont);
		mGiftCardsZpayText = (TextView) leftMenuItems.findViewById(R.id.menuZpay);
		mGiftCardsZpayText.setTypeface(mZouponsFont);
		mGiftCardsInvoiceCenterText = (TextView) leftMenuItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
		mGiftCardsInvoiceCenterText.setTypeface(mZouponsFont);
		mGiftCardsManageCardsText = (TextView) leftMenuItems.findViewById(R.id.menuManageCards);
		mGiftCardsManageCardsText.setTypeface(mZouponsFont);
		mGiftCardsReceiptsText = (TextView) leftMenuItems.findViewById(R.id.menuReceipts);
		mGiftCardsReceiptsText.setTypeface(mZouponsFont);
		mGiftCardsGiftCardsText = (TextView) leftMenuItems.findViewById(R.id.menuGiftCards);
		mGiftCardsGiftCardsText.setTypeface(mZouponsFont);
		mGiftCardsMyFavoritesText = (TextView) leftMenuItems.findViewById(R.id.menufavorites);
		mGiftCardsMyFavoritesText.setTypeface(mZouponsFont);
		mGiftCardsMyFriendsText = (TextView) leftMenuItems.findViewById(R.id.menuMyFriends);
		mGiftCardsMyFriendsText.setTypeface(mZouponsFont);
		mGiftCardsChatText = (TextView) leftMenuItems.findViewById(R.id.menuCustomerCenter);
		mGiftCardsChatText.setTypeface(mZouponsFont);
		mGiftCardsRewardsText = (TextView) leftMenuItems.findViewById(R.id.menuRewards);
		mGiftCardsRewardsText.setTypeface(mZouponsFont);
		mGiftCardsSettingsText = (TextView) leftMenuItems.findViewById(R.id.menuSettings);
		mGiftCardsSettingsText.setTypeface(mZouponsFont);
		mGiftCardsLogoutText = (TextView) leftMenuItems.findViewById(R.id.menuLogout);
		mGiftCardsLogoutText.setTypeface(mZouponsFont);
		//RightMenuItems
		mGiftCardsSendToFriend = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_sendtofriend);
		mGiftCardsSelfUse = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_selfuse);
		mGiftCardsTransactionhistory = (LinearLayout) rightMenuItems.findViewById(R.id.rightmenu_transactionhistory);

		mGiftCardsSendToFriendTxt = (TextView) rightMenuItems.findViewById(R.id.rightmenu_sendtofriendTextId);
		mGiftCardsSendToFriendTxt.setTypeface(mZouponsFont);
		mGiftCardsSelfUseTxt = (TextView) rightMenuItems.findViewById(R.id.rightmenu_selfuseTextId);
		mGiftCardsSelfUseTxt.setTypeface(mZouponsFont);
		mGiftCardsTransactionhistoryTxt = (TextView) rightMenuItems.findViewById(R.id.rightmenu_transactionhistoryTextId);
		mGiftCardsTransactionhistoryTxt.setTypeface(mZouponsFont);

		mGiftCardsSendToFriendImage = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_sendtofriendImageId);
		mGiftCardsSelfUseImage = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_selfuseImageId);
		mGiftCardsTransactionhistoryImage = (ImageView) rightMenuItems.findViewById(R.id.rightmenu_transactionhistoryImageId);

		mMenuBarMyGiftCards=(LinearLayout) giftcardsmenubarcontainer.findViewById(R.id.menubar_mygiftcards);
		mMenuBarMyGiftCards.setLayoutParams(new LinearLayout.LayoutParams((int)mGiftCardsMenuWidth,LayoutParams.FILL_PARENT,1f));
		mMenuBarPurchase=(LinearLayout) giftcardsmenubarcontainer.findViewById(R.id.menubar_purchase);
		mMenuBarPurchase.setLayoutParams(new LinearLayout.LayoutParams((int)mGiftCardsMenuWidth,LayoutParams.FILL_PARENT,1f));
		mMenuBarRedeem=(LinearLayout) giftcardsmenubarcontainer.findViewById(R.id.menubar_redeem);
		mMenuBarRedeem.setLayoutParams(new LinearLayout.LayoutParams((int)mGiftCardsMenuWidth,LayoutParams.FILL_PARENT,1f));

		mMenuBarMyGiftCardsText=(TextView) giftcardsmenubarcontainer.findViewById(R.id.menubar_mygiftcards_text);
		mMenuBarMyGiftCardsText.setTypeface(mZouponsFont);
		mMenuBarPurchaseText=(TextView) giftcardsmenubarcontainer.findViewById(R.id.menubar_purchase_text);
		mMenuBarPurchaseText.setTypeface(mZouponsFont);
		mMenuBarRedeemText=(TextView) giftcardsmenubarcontainer.findViewById(R.id.menubar_redeem_text);
		mMenuBarRedeemText.setTypeface(mZouponsFont);

		mGiftCardsListView=(ListView) giftcardslistviewholder.findViewById(R.id.giftcardslistview);

		mGiftCardsHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, MyGiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, MyGiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, MyGiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsGiftcards.setOnClickListener(new MenuItemClickListener(leftMenuItems,MyGiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsManageCards.setOnClickListener(new MenuItemClickListener(leftMenuItems, MyGiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, MyGiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,MyGiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,MyGiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,MyGiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,MyGiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,MyGiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));

		mGiftCardsSendToFriend.setOnClickListener(new MenuItemClickListener(rightMenuItems, MyGiftCards.this, POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsSelfUse.setOnClickListener(new MenuItemClickListener(rightMenuItems, MyGiftCards.this, POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsTransactionhistory.setOnClickListener(new MenuItemClickListener(rightMenuItems, MyGiftCards.this, POJOMainMenuActivityTAG.TAG=TAG));
		LayoutInflater mFooterInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mFooterInflater.inflate(R.layout.footerlayout, null, false);
		mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		mGiftCardsListView.addFooterView(mFooterLayout);

		mLogoutImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(MyGiftCards.this,"FromManualLogOut").execute();
			}
		});

		mGiftCardsLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);

				if(NormalPaymentAsyncTask.mCountDownTimer!=null){
					NormalPaymentAsyncTask.mCountDownTimer.cancel();
					NormalPaymentAsyncTask.mCountDownTimer = null;
				}
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(MyGiftCards.this,"FromManualLogOut").execute();
			}
		});

		mMenuBarMyGiftCards.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mMenuBarPurchase.setBackgroundResource(R.drawable.header_2);
				mMenuBarRedeem.setBackgroundResource(R.drawable.header_2);
				if(giftcardslistviewparent.getVisibility()!=View.VISIBLE){
					giftcardslistviewparent.setVisibility(View.VISIBLE);
					giftcardsredeemholder.setVisibility(View.GONE);
				}
			}
		});

		mMenuBarPurchase.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);

				/*
				 * To Clear FB friend information after sending giftcard to friend
				 * **/
				Friends.friendName=""; 
				Friends.friendId="";
				Friends.isZouponsFriend="";
				Friends.friendEmailId= "";

				mMenuBarMyGiftCards.setBackgroundResource(R.drawable.header_2);
				mMenuBarRedeem.setBackgroundResource(R.drawable.header_2);
				//ZPayFlag.setZpayViewFlag(GiftCards_ZpayViewFlag);	//set 1
				ZPayFlag.setFlag(GiftCards_ZpayViewFlag);			//set 1
				ZpayStep2SearchEnable.searchEnableFlag=true;	//flag to enable search header text in zpay step2 page
				Intent intent_zpaypage = new Intent(MyGiftCards.this,CardPurchase.class);
				//MenuOutClass.HOMEPAGE_MENUOUT=false;
				intent_zpaypage.putExtra("pageflag", "giftcard");
				startActivity(intent_zpaypage);
				//MyGiftCards.this.finish();
			}
		});

		// To list My gitcards list
		GetAllMyGiftCardTask mGetAllMyGiftCardTask = new GetAllMyGiftCardTask(MyGiftCards.this,mGiftCardsListView,"PROGRESS",mFooterLayout,mNotificationCount);
		mGetAllMyGiftCardTask.execute("");

		mRedeem_Cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMenuBarMyGiftCards.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mMenuBarPurchase.setBackgroundResource(R.drawable.header_2);
				mMenuBarRedeem.setBackgroundResource(R.drawable.header_2);
				if(giftcardslistviewparent.getVisibility()!=View.VISIBLE){
					giftcardslistviewparent.setVisibility(View.VISIBLE);
					giftcardsredeemholder.setVisibility(View.GONE);
				}

			}
		});

		mGiftCardsListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastInScreen = firstVisibleItem + visibleItemCount;
				if(Integer.parseInt(POJOLimit.mTotalCount) > lastInScreen && Integer.parseInt(POJOLimit.mTotalCount) >20){					
					if(lastInScreen == totalItemCount && !mIsLoadMore){	
						if(mGiftCardsListView.getFooterViewsCount() == 0){
							mGiftCardsListView.addFooterView(mFooterLayout);
						}
						if(Integer.parseInt(POJOLimit.mTotalCount) > Integer.parseInt(POJOLimit.mEndLimit)){
							mFooterText.setText("Loading "+POJOLimit.mEndLimit+" of "+"("+POJOLimit.mTotalCount+")");
						}else{
							mFooterText.setText("Loading "+POJOLimit.mTotalCount);
						}
						GetAllMyGiftCardTask mGetAllMyGiftCardTask = new GetAllMyGiftCardTask(MyGiftCards.this,mGiftCardsListView,"NOPROGRESS",mFooterLayout,mNotificationCount);
						mGetAllMyGiftCardTask.execute("REFRESHADAPTER");
					}
				}else{
					if(POJOLimit.mTotalCount.equalsIgnoreCase("0")){
					}else if(mFooterLayout != null && mGiftCardsListView.getFooterViewsCount() !=0 && mGiftCardsListView.getAdapter() != null){
						if(lastInScreen!= totalItemCount){
							mGiftCardsListView.removeFooterView(mFooterLayout);	
						}else{

						}
					}
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's width.
	 */
	public class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View menu;
		Button mFreezeView;
		/**
		 * Menu must NOT be out/shown to start with.
		 */

		public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu,Button freezeview) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
			this.mFreezeView=freezeview;
		}

		@Override
		public void onClick(View v) {

			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);

			Context context = menu.getContext();
			String msg = "Slide " + new Date();
			System.out.println(msg);

			int menuWidth = menu.getMeasuredWidth();

			// Ensure menu is visible
			menu.setVisibility(View.VISIBLE);

			InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

			if (!MenuOutClass.GIFTCARDS_MENUOUT) {
				// Scroll to 0 to reveal menu
				int left = 0;
				scrollView.smoothScrollTo(left, 0);
				this.mFreezeView.setVisibility(View.VISIBLE);
			} else {
				// Scroll to menuWidth so menu isn't on screen.
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);
				this.mFreezeView.setVisibility(View.GONE);
			}
			MenuOutClass.GIFTCARDS_MENUOUT = !MenuOutClass.GIFTCARDS_MENUOUT;
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
		new RefreshZoupons().isApplicationGoneBackground(MyGiftCards.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	protected void onResume() {
		super.onResume();
		POJOMainMenuActivityTAG.TAG=TAG;
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(MyGiftCards.this,ZouponsConstants.sCustomerModuleFlag);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(MyGiftCards.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(MyGiftCards.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// To close right menu when we move from transaction history to giftcards using back button.
		if(sScrollView != null && sLeftMenu != null){
			sScrollView.smoothScrollTo(sLeftMenu.getMeasuredWidth(), 0);
		}
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sScrollView = null; sLeftMenu = null; sRightMenu = null;
		mGiftCardsFreezeView= null;mGiftCardsHome= null;mGiftCardsZpay= null;mGiftCardsInvoiceCenter= null;mGiftCardsManageCards= null;mGiftCardsReceipts= null;mGiftCardsGiftcards= null;mGiftCardsMyFavourites= null;mGiftCardsMyFriends= null;mGiftCardsChat= null;mGiftCardsRewards= null;mGiftCardsSettings= null;mGiftCardsLogout = null;
		mGiftCardsHomeText= null;mGiftCardsZpayText= null;mGiftCardsInvoiceCenterText= null;mGiftCardsManageCardsText= null;mGiftCardsReceiptsText= null;mGiftCardsGiftCardsText= null;mGiftCardsMyFavoritesText= null;mGiftCardsMyFriendsText= null;mGiftCardsChatText= null;mGiftCardsRewardsText= null;mGiftCardsSettingsText= null;mGiftCardsLogoutText = null;
		mGiftCardsSendToFriend= null;mGiftCardsSelfUse= null;mGiftCardsTransactionhistory = null;
		mGiftCardsSendToFriendTxt= null;mGiftCardsSelfUseTxt= null;mGiftCardsTransactionhistoryTxt=null;
		// To notify system that its time to run garbage collector service
		System.gc();
	}


	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}
}
