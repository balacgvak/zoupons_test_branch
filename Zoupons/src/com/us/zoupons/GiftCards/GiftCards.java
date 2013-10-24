/**
 * 
 */
package com.us.zoupons.GiftCards;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.R;
import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.FlagClasses.ZPayFlag;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.friends.Friends;
import com.us.zoupons.notification.CustomNotificationAdapter;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotificationItemClickListener;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;
import com.us.zoupons.zpay.ZpayStep2SearchEnable;
import com.us.zoupons.zpay.zpay_step1;

/**
 * GiftCards Class
 */
public class GiftCards extends Activity {

	public static MyHorizontalScrollView scrollView;
	public static View leftMenu,rightMenu;
	View app;
	ImageView btnSlide;
	public RelativeLayout btnLogout;
	public static Button mGiftCardsFreezeView;

	public static LinearLayout mGiftCardsHome,mGiftCardsZpay,mGiftCardsInvoiceCenter,mGiftCardsManageCards,mGiftCardsReceipts,mGiftCardsGiftcards,mGiftCardsMyFavourites,mGiftCardsMyFriends,mGiftCardsChat,mGiftCardsRewards,mGiftCardsSettings,mGiftCardsLogout;
	public static TextView mGiftCardsHomeText,mGiftCardsZpayText,mGiftCardsInvoiceCenterText,mGiftCardsManageCardsText,mGiftCardsReceiptsText,mGiftCardsGiftCardsText,mGiftCardsMyFavoritesText,mGiftCardsMyFriendsText,mGiftCardsChatText,mGiftCardsRewardsText,mGiftCardsSettingsText,mGiftCardsLogoutText;
	public static LinearLayout mGiftCardsSendToFriend,mGiftCardsSelfUse,mGiftCardsTransactionhistory;
	public static TextView mGiftCardsSendToFriendTxt,mGiftCardsSelfUseTxt,mGiftCardsTransactionhistoryTxt;
	public Typeface mZouponsFont;
	public ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	public Button mNotificationCount;
	public static String TAG = "GiftCards";

	public int mScreenWidth;
	public double mGiftCardsMenuWidth;

	private LinearLayout mMenuBarMyGiftCards,mMenuBarPurchase,mMenuBarRedeem;
	private TextView mMenuBarMyGiftCardsText,mMenuBarPurchaseText,mMenuBarRedeemText;
	private ListView mGiftCardsListView;
	public EditText mVerificationCode;
	public Button mRedeem_Cancel,mRedeem_Send;
	private int GiftCards_ZpayViewFlag=1;
	//Notification Pop up layout declaration
	private View mPopUpLayout;
	private ListView mNotificationList;
	private ScheduleNotificationSync mNotificationSync;

	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	public ImageView mTabBarLoginChoice;
	public static ScrollView leftMenuScrollView;
	public View mFooterLayout;
	private TextView mFooterText;
	public static boolean mIsLoadMore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);

		mIsLoadMore = false;
		POJOLimit.mStartLimit="0";
		POJOLimit.mEndLimit="20";
		POJOLimit.mTotalCount="0";
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");

		leftMenu = inflater.inflate(R.layout.horz_scroll_menu, null);
		rightMenu = inflater.inflate(R.layout.giftcards_rightmenu, null);
		app = inflater.inflate(R.layout.giftcards, null);

		ViewGroup leftMenuItems = (ViewGroup) leftMenu.findViewById(R.id.menuitems);
		ViewGroup rightMenuItems = (ViewGroup) rightMenu.findViewById(R.id.giftcards_rightmenuitems);
		final ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.giftcards_tabBar);
		ViewGroup giftcardscontainer = (ViewGroup) app.findViewById(R.id.giftcards_container);
		ViewGroup giftcardsmenubarcontainer = (ViewGroup) app.findViewById(R.id.menubarcontainer_gifcards);
		final ViewGroup giftcardslistviewparent = (ViewGroup) giftcardscontainer.findViewById(R.id.giftcards_listview_parent);
		ViewGroup giftcardslistviewholder = (ViewGroup) giftcardscontainer.findViewById(R.id.giftcardslistviewholder);
		final ViewGroup giftcardsredeemholder = (ViewGroup) giftcardscontainer.findViewById(R.id.giftcards_redeem);

		leftMenuScrollView = (ScrollView) leftMenu.findViewById(R.id.leftmenu_scrollview);
		leftMenuScrollView.fullScroll(View.FOCUS_UP);
		leftMenuScrollView.pageScroll(View.FOCUS_UP);

		mGiftCardsFreezeView = (Button) app.findViewById(R.id.giftcards_freezeview);
		mGiftCardsFreezeView.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, mGiftCardsFreezeView));

		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, mGiftCardsFreezeView));
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(GiftCards.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count); 
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(GiftCards.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();

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

		final View[] children = new View[] { leftMenu, app, rightMenu};

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

		//Call function to get width of the screen
		mScreenWidth=getScreenWidth(); 	
		if(mScreenWidth>0){	//To fix Home Page menubar items width
			mGiftCardsMenuWidth=mScreenWidth/3;
		}

		//Redeem View Items
		mVerificationCode = (EditText) giftcardsredeemholder.findViewById(R.id.giftcards_redeem_verificationcodeEdt);
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

		mGiftCardsHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, GiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, GiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, GiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsGiftcards.setOnClickListener(new MenuItemClickListener(leftMenuItems,GiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsManageCards.setOnClickListener(new MenuItemClickListener(leftMenuItems, GiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, GiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,GiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,GiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,GiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,GiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,GiftCards.this,POJOMainMenuActivityTAG.TAG=TAG));

		mGiftCardsSendToFriend.setOnClickListener(new MenuItemClickListener(rightMenuItems, GiftCards.this, POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsSelfUse.setOnClickListener(new MenuItemClickListener(rightMenuItems, GiftCards.this, POJOMainMenuActivityTAG.TAG=TAG));
		mGiftCardsTransactionhistory.setOnClickListener(new MenuItemClickListener(rightMenuItems, GiftCards.this, POJOMainMenuActivityTAG.TAG=TAG));
		LayoutInflater mFooterInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mFooterInflater.inflate(R.layout.footerlayout, null, false);
		mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		mGiftCardsListView.addFooterView(mFooterLayout);

		mLogoutImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(GiftCards.this).execute();
				Intent intent_logout = new Intent(GiftCards.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				GiftCards.this.finish();
				startActivity(intent_logout);
			}
		});

		mGiftCardsLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);

				if(NormalPaymentAsyncTask.mCountDownTimer!=null){
					NormalPaymentAsyncTask.mCountDownTimer.cancel();
					NormalPaymentAsyncTask.mCountDownTimer = null;
					Log.i(TAG,"Timer Stopped Successfully");
				}

				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(GiftCards.this).execute();
				Intent intent_logout = new Intent(GiftCards.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_logout);
				finish();
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

				mMenuBarMyGiftCards.setBackgroundResource(R.drawable.header_2);
				mMenuBarRedeem.setBackgroundResource(R.drawable.header_2);
				//ZPayFlag.setZpayViewFlag(GiftCards_ZpayViewFlag);	//set 1
				ZPayFlag.setFlag(GiftCards_ZpayViewFlag);			//set 1
				ZpayStep2SearchEnable.searchEnableFlag=true;	//flag to enable search header text in zpay step2 page
				Intent intent_zpaypage = new Intent(GiftCards.this,zpay_step1.class);
				//MenuOutClass.HOMEPAGE_MENUOUT=false;
				intent_zpaypage.putExtra("pageflag", "giftcard");
				startActivity(intent_zpaypage);
				//GiftCards.this.finish();
			}
		});

		GetAllMyGiftCardTask mGetAllMyGiftCardTask = new GetAllMyGiftCardTask(GiftCards.this,mGiftCardsListView,"PROGRESS",mFooterLayout);
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

		mRedeem_Send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!mVerificationCode.getText().toString().trim().equals("")){
					RedeemGiftCardTask mRedeemGiftCardTask = new RedeemGiftCardTask(GiftCards.this,mVerificationCode,mMenuBarMyGiftCards,mMenuBarRedeem,giftcardslistviewparent,giftcardsredeemholder);
					mRedeemGiftCardTask.execute(mVerificationCode.getText().toString().trim());	//Dummy Verification code
				}else {
					alertBox_service("Information", "Please Enter Verification code.");
				}
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
				mNotificationList.setAdapter(new CustomNotificationAdapter(GiftCards.this));
				mNotificationList.setOnItemClickListener(new NotificationItemClickListener());

				mPopUpWindow.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						// TODO Auto-generated method stub
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
						GetAllMyGiftCardTask mGetAllMyGiftCardTask = new GetAllMyGiftCardTask(GiftCards.this,mGiftCardsListView,"NOPROGRESS",mFooterLayout);
						mGetAllMyGiftCardTask.execute("REFRESHADAPTER");
					}
				}else{
					if(POJOLimit.mTotalCount.equalsIgnoreCase("0")){
					}else if(mFooterLayout != null && mGiftCardsListView.getFooterViewsCount() !=0 && mGiftCardsListView.getAdapter() != null){
						Log.i(TAG, "Removed Based on Total Count Footer");
						if(lastInScreen!= totalItemCount){
							Log.i(TAG, "Remove Footer success");
							mGiftCardsListView.removeFooterView(mFooterLayout);	
						}else{
							Log.i(TAG, "Remove Footer wait");
						}
					}
				}
			}
		});
	}

	private void alertBox_service(String title, final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(GiftCards.this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equals("Please Enter Verification code.")){
					mVerificationCode.setText("");
				}
			}
		});
		service_alert.show();
	}

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's width.
	 */
	public static class ClickListenerForScrolling implements OnClickListener {
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
			Log.i(TAG,"Menu Width : "+ menuWidth);

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
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		if(mNotificationSync!=null){
			mNotificationSync.cancelAlarm();
		}
	}

	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(GiftCards.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}

	@Override
	protected void onResume() {
		POJOMainMenuActivityTAG.TAG=TAG;
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(GiftCards.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(GiftCards.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(GiftCards.this);
		mLogoutSession.setLogoutTimerAlarm();

		super.onResume();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// To close right menu when we move from transaction history to giftcards using back button.
		if(scrollView != null && leftMenu != null){
			scrollView.smoothScrollTo(leftMenu.getMeasuredWidth(), 0);
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
