package com.us.zoupons.storeowner.customercenter;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.KeyboardUtil;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
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
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

/**
 * 
 * Activity to display Email composer
 *
 */

public class StoreEmailComposer extends Activity implements OnClickListener{

	// Initialization of views,viewgroups and variables
	private String mTAG="StoreOwner_EmailComposer";
	private MyHorizontalScrollView mScrollView;
	private View mApp;
	private LinearLayout mButtonSlide;
	private Typeface mZouponsFont;
	private Header mZouponsHeader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private View mRightMenu;
	private View mLeftMenu;
	private View mCustomerLeftmenu;
	private int mMenuFlag;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private ScrollView mMiddleView;
	private Button mStoreOwner_EmailComposerFreezeView,mStoreOwner_EmailComposerCancelButton,mStoreOwner_EmailComposerSendButton;
	private LinearLayout mStoreOwnerEmailComposerBack;
	private TextView mStoreOwnerEmailComposerHeaderText;
	private ImageView mStoreOwnerEmailComposerRightMenuImage;
	private EditText mStoreOwnerEmailComposerSubject,mStoreOwnerEmailComposerBody;
	private  ScrollView mLeftMenuScrollView;
	private RelativeLayout mLogoutButton; 
    private Button mNotificationCount;
    private ScheduleNotificationSync mNotificationSync;
    private CheckLogoutSession mLogoutSession;
    private ImageView mTabBarLoginChoice,mNotificationImage,mCalloutTriangleImage;
    private LinearLayout mStoreEmailComposerHome,mStoreEmailComposerZpay,mStoreEmailComposerInvoiceCenter,mStoreEmailComposer,mStoreEmailComposerGiftcards,mStoreEmailComposerReceipts,mStoreEmailComposerMyFavourites,mStoreEmailComposerMyFriends,mStoreEmailComposerChat,mStoreEmailComposerRewards,mStoreEmailComposerSettings,mStoreEmailComposerLogout;
    private TextView mStoreEmailComposerHomeText,mStoreEmailComposerZpayText,mStoreEmailComposerInvoiceCenterText,mStoreEmailComposerText,mStoreEmailComposerGiftCardsText,mStoreEmailComposerReceiptsText,mStoreEmailComposerMyFavoritesText,mStoreEmailComposerMyFriendsText,mStoreEmailComposerChatText,mStoreEmailComposerRewardsText,mStoreEmailComposerSettingsText,mStoreEmailComposerLogoutText;
    private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation
      
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			LayoutInflater inflater = LayoutInflater.from(this);
			mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(mScrollView);
			mApp = inflater.inflate(R.layout.store_emailcomposer, null);
			mMiddleView = (ScrollView) mApp.findViewById(R.id.emailcomposer_middleview); 
			mStoreOwnerEmailComposerHeaderText = (TextView) mApp.findViewById(R.id.store_emailcomposer_storename);
			mStoreOwnerEmailComposerRightMenuImage = (ImageView) mApp.findViewById(R.id.store_emailcomposer_rightmenu);
			mStoreOwner_EmailComposerFreezeView = (Button) mApp.findViewById(R.id.store_emailcomposer_freeze);
			mStoreOwnerEmailComposerBack = (LinearLayout) mApp.findViewById(R.id.store_emailcomposer_footer_back);
			mStoreOwnerEmailComposerSubject = (EditText) mApp.findViewById(R.id.store_emailcomposer_subject_value);
			mStoreOwnerEmailComposerBody = (EditText) mApp.findViewById(R.id.store_emailcomposer_body_value);
			mStoreOwner_EmailComposerCancelButton = (Button) mApp.findViewById(R.id.store_emailcomposer_cancel);
			mStoreOwner_EmailComposerSendButton = (Button) mApp.findViewById(R.id.store_emailcomposer_send);

			if(getIntent().getExtras().getString("classname").equalsIgnoreCase("StoreEmailComposer")){ // From store owner module..
				mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreEmailComposer.this,mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_EmailComposerFreezeView, mTAG);
				mStoreownerRightmenu = new StoreOwner_RightMenu(StoreEmailComposer.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_EmailComposerFreezeView, mTAG);
				mLeftMenu = mStoreownerLeftmenu.intializeInflater();
				mStoreownerLeftmenu.clickListener( mLeftMenu /*,mRightMenu*/ );
				mRightMenu = mStoreownerRightmenu.intializeCustomerCenterInflater();
				mStoreownerRightmenu.customercentermenuClickListener(mLeftMenu,mRightMenu);
				/* Header Tab Bar which contains logout,notification and home buttons*/
				mZouponsHeader = (Header) mApp.findViewById(R.id.store_emailcomposer_header);
				mZouponsHeader.setVisibility(View.VISIBLE);
				mZouponsHeader.intializeInflater(mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_EmailComposerFreezeView, mTAG);
				mZouponsHeader.mTabBarNotificationContainer.setVisibility(View.VISIBLE);
				final View[] children = new View[] { mLeftMenu, mApp,mRightMenu};
				// Scroll to app (view[1]) when layout finished.
				int scrollToViewIdx = 1;
				mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsHeader.mLeftMenuBtnSlide));
				mStoreOwnerEmailComposerRightMenuImage.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag=2, mStoreOwner_EmailComposerFreezeView, mTAG,mStoreOwnerEmailComposerSubject,mStoreOwnerEmailComposerBody,false));
				mStoreOwner_EmailComposerFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_EmailComposerFreezeView, mTAG,mStoreOwnerEmailComposerSubject,mStoreOwnerEmailComposerBody,true));
				mStoreOwner_EmailComposerCancelButton.setOnClickListener(this);
				mStoreOwner_EmailComposerSendButton.setOnClickListener(this);
				mStoreOwnerEmailComposerBack.setOnClickListener(this);
				mStoreOwnerEmailComposerHeaderText.setText(StoreOwner_RightMenu.mCustomer_FirstName+" "+StoreOwner_RightMenu.mCustomer_LastName);
				mNotificationReceiver = new NotifitcationReceiver(mZouponsHeader.mTabBarNotificationCountBtn);
				// Notitification pop up layout declaration
				mZouponsHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
				mZouponsHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			}else{
				if(mZouponsHeader!=null){
					mZouponsHeader.setVisibility(View.GONE);
				}
				mCustomerLeftmenu = inflater.inflate(R.layout.shopper_leftmenu, null);
    			ViewGroup leftMenuItems = (ViewGroup) mCustomerLeftmenu.findViewById(R.id.menuitems);
				mLeftMenuScrollView = (ScrollView) mCustomerLeftmenu.findViewById(R.id.leftmenu_scrollview);
				mLeftMenuScrollView.fullScroll(View.FOCUS_UP);
				mLeftMenuScrollView.pageScroll(View.FOCUS_UP);
				/* Header Tab Bar which contains logout,notification and home buttons*/
				final ViewGroup tabBar = (ViewGroup) mApp.findViewById(R.id.customer_emailcomposer_tabbar);
				RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				p.addRule(RelativeLayout.BELOW, R.id.customer_emailcomposer_tabbar);
				mMiddleView.setLayoutParams(p);

				// Notification and log out variables
				mLogoutButton = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
				mLogoutButton.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(StoreEmailComposer.this));//ClickListener for Header Home image
				mNotificationCount = (Button) mLogoutButton.findViewById(R.id.zoupons_notification_count); 
				mNotificationImage = (ImageView) mLogoutButton.findViewById(R.id.zoupons_notificationImageId);
				mCalloutTriangleImage = (ImageView) mLogoutButton.findViewById(R.id.zoupons_callout_triangle);
				mTabBarLoginChoice = (ImageView) mLogoutButton.findViewById(R.id.store_header_loginchoice);
				new LoginChoiceTabBarImage(StoreEmailComposer.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();

				mStoreEmailComposerHome = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_home);
				mStoreEmailComposerZpay = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zpay);
				mStoreEmailComposerInvoiceCenter = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_invoicecenter);
				mStoreEmailComposer = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_managecards);
				mStoreEmailComposerGiftcards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zgiftcards);
				mStoreEmailComposerReceipts = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_receipts);
				mStoreEmailComposerMyFavourites = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfavourites);
				mStoreEmailComposerMyFriends = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfriends);
				mStoreEmailComposerChat = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_customercenter);
				mStoreEmailComposerRewards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_rewards);
				mStoreEmailComposerSettings = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_settings);
				mStoreEmailComposerLogout = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_logout);

				mStoreEmailComposerHomeText = (TextView) leftMenuItems.findViewById(R.id.menuHome);
				mStoreEmailComposerHomeText.setTypeface(mZouponsFont);
				mStoreEmailComposerZpayText = (TextView) leftMenuItems.findViewById(R.id.menuZpay);
				mStoreEmailComposerZpayText.setTypeface(mZouponsFont);
				mStoreEmailComposerInvoiceCenterText = (TextView) leftMenuItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
				mStoreEmailComposerInvoiceCenterText.setTypeface(mZouponsFont);
				mStoreEmailComposerText = (TextView) leftMenuItems.findViewById(R.id.menuManageCards);
				mStoreEmailComposerText.setTypeface(mZouponsFont);
				mStoreEmailComposerGiftCardsText = (TextView) leftMenuItems.findViewById(R.id.menuGiftCards);
				mStoreEmailComposerGiftCardsText.setTypeface(mZouponsFont);
				mStoreEmailComposerReceiptsText = (TextView) leftMenuItems.findViewById(R.id.menuReceipts);
				mStoreEmailComposerReceiptsText.setTypeface(mZouponsFont);
				mStoreEmailComposerMyFavoritesText = (TextView) leftMenuItems.findViewById(R.id.menufavorites);
				mStoreEmailComposerMyFavoritesText.setTypeface(mZouponsFont);
				mStoreEmailComposerMyFriendsText = (TextView) leftMenuItems.findViewById(R.id.menuMyFriends);
				mStoreEmailComposerMyFriendsText.setTypeface(mZouponsFont);
				mStoreEmailComposerChatText = (TextView) leftMenuItems.findViewById(R.id.menuCustomerCenter);
				mStoreEmailComposerChatText.setTypeface(mZouponsFont);
				mStoreEmailComposerRewardsText = (TextView) leftMenuItems.findViewById(R.id.menuRewards);
				mStoreEmailComposerRewardsText.setTypeface(mZouponsFont);
				mStoreEmailComposerSettingsText = (TextView) leftMenuItems.findViewById(R.id.menuSettings);
				mStoreEmailComposerSettingsText.setTypeface(mZouponsFont);
				mStoreEmailComposerLogoutText = (TextView) leftMenuItems.findViewById(R.id.menuLogout);
				mStoreEmailComposerLogoutText.setTypeface(mZouponsFont);

				mButtonSlide = (LinearLayout) tabBar.findViewById(R.id.BtnSlide);
				mButtonSlide.setOnClickListener(new ClickListenerForScrolling(mScrollView, mCustomerLeftmenu, mStoreOwner_EmailComposerFreezeView,StoreEmailComposer.this));
				mStoreOwner_EmailComposerFreezeView.setOnClickListener(new ClickListenerForScrolling(mScrollView, mCustomerLeftmenu, mStoreOwner_EmailComposerFreezeView,StoreEmailComposer.this));

				mStoreEmailComposerHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, StoreEmailComposer.this,POJOMainMenuActivityTAG.TAG="Customer_EmailComposer"));
				mStoreEmailComposerZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, StoreEmailComposer.this,POJOMainMenuActivityTAG.TAG="Customer_EmailComposer"));
				mStoreEmailComposerInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, StoreEmailComposer.this,POJOMainMenuActivityTAG.TAG="Customer_EmailComposer"));
				mStoreEmailComposerGiftcards.setOnClickListener(new MenuItemClickListener(leftMenuItems,StoreEmailComposer.this,POJOMainMenuActivityTAG.TAG="Customer_EmailComposer"));
				mStoreEmailComposer.setOnClickListener(new MenuItemClickListener(leftMenuItems, StoreEmailComposer.this,POJOMainMenuActivityTAG.TAG="Customer_EmailComposer"));
				mStoreEmailComposerReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, StoreEmailComposer.this,POJOMainMenuActivityTAG.TAG="Customer_EmailComposer"));
				mStoreEmailComposerMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,StoreEmailComposer.this,POJOMainMenuActivityTAG.TAG="Customer_EmailComposer"));
				mStoreEmailComposerMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,StoreEmailComposer.this,POJOMainMenuActivityTAG.TAG="Customer_EmailComposer"));
				mStoreEmailComposerChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,StoreEmailComposer.this,POJOMainMenuActivityTAG.TAG="Customer_EmailComposer"));
				mStoreEmailComposerRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,StoreEmailComposer.this,POJOMainMenuActivityTAG.TAG="Customer_EmailComposer"));
				mStoreEmailComposerSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,StoreEmailComposer.this,POJOMainMenuActivityTAG.TAG="Customer_EmailComposer"));
				mStoreEmailComposerLogout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						v.setBackgroundResource(R.drawable.gradient_bg_hover);

						if(NormalPaymentAsyncTask.mCountDownTimer!=null){
							NormalPaymentAsyncTask.mCountDownTimer.cancel();
							NormalPaymentAsyncTask.mCountDownTimer = null;
							Log.i(mTAG,"Timer Stopped Successfully");
						}

						// AsyncTask to call the logout webservice to end the login session
						new LogoutSessionTask(StoreEmailComposer.this,"FromManualLogOut").execute();
					}
				});

				final View[] children = new View[] { mCustomerLeftmenu, mApp };
				// Notitification pop up layout declaration
				mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
				mNotificationImage.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
				mNotificationCount.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
				// Scroll to app (view[1]) when layout finished.
				int scrollToViewIdx = 1;
				mScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(mButtonSlide));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		if(v.equals(mStoreOwner_EmailComposerCancelButton)){
			if(getIntent().getExtras().getString("classname").equalsIgnoreCase("StoreEmailComposer")){
				Intent intent_rightmenuCustomercenter = new Intent().setClass(getApplicationContext(),CustomerCenter.class);
				intent_rightmenuCustomercenter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_rightmenuCustomercenter);
			}else{
				finish();
			}
		}else if(v.equals(mStoreOwnerEmailComposerBack)){
			finish();
		}else{
			if(mStoreOwnerEmailComposerBody.getText().toString().trim().length() == 0){
				alertBox_service("Information","Please enter email message",mStoreOwnerEmailComposerBody);
			}else{
				if(getIntent().getExtras().getString("classname").equalsIgnoreCase("StoreEmailComposer")){
					SendEmailTask mSendEmail = new SendEmailTask(StoreEmailComposer.this,StoreOwner_RightMenu.mCustomer_id, mStoreOwnerEmailComposerSubject.getText().toString(), mStoreOwnerEmailComposerBody.getText().toString());
					mSendEmail.execute();
				}else{
					
				}
			}

		}
	}

	/**
     * Helper for examples with a HSV that should be scrolled by a menu View's width.
     */
    class ClickListenerForScrolling implements OnClickListener {
        HorizontalScrollView scrollView;
        View leftmenu;
        Button freezeView;
        Activity mStoreEmailComposer;
        /**
         * Menu must NOT be out/shown to start with.
         */
        
        public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu,Button freezeview,Activity storeemailcomposer) {
            super();
            this.scrollView = scrollView;
            this.leftmenu = menu;
            this.freezeView = freezeview;
            this.mStoreEmailComposer = storeemailcomposer;
        }

        @Override
        public void onClick(View v) {
        	
        	mLeftMenuScrollView.fullScroll(View.FOCUS_UP);
    		mLeftMenuScrollView.pageScroll(View.FOCUS_UP);
    		
            String msg = "Slide " + new Date();
            System.out.println(msg);

            int menuWidth = leftmenu.getMeasuredWidth();
            

            // Ensure menu is visible
            leftmenu.setVisibility(View.VISIBLE);

            if(mStoreEmailComposer.getCurrentFocus().hasFocus()){
            	mStoreEmailComposer.getCurrentFocus().clearFocus();
            }
            new KeyboardUtil().hideKeyboard(mStoreEmailComposer);
			
			if (!MenuOutClass.EMAILCOMPOSER_MENUOUT) {
                // Scroll to 0 to reveal menu
            	int left = 0;
                scrollView.smoothScrollTo(left, 0);
                freezeView.setVisibility(View.VISIBLE);
            } else {
                // Scroll to menuWidth so menu isn't on screen.
            	int left = menuWidth;
                scrollView.smoothScrollTo(left, 0);
                freezeView.setVisibility(View.GONE);
            }
            MenuOutClass.EMAILCOMPOSER_MENUOUT = !MenuOutClass.EMAILCOMPOSER_MENUOUT;
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
    
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwnerEmailComposerSubject.setFocusable(true);
		mStoreOwnerEmailComposerSubject.setFocusableInTouchMode(true);
		mStoreOwnerEmailComposerBody.setFocusable(true);
		mStoreOwnerEmailComposerBody.setFocusableInTouchMode(true);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	// To show alert box with respective message
	private void alertBox_service(String title, final String msg,final EditText editText) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreEmailComposer.this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setCancelable(true);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				editText.requestFocus();
			}
		});
		service_alert.show();
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
	protected void onResume() {
		super.onResume();
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreEmailComposer.this,ZouponsConstants.sStoreModuleFlag);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreEmailComposer.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreEmailComposer.this);
		mLogoutSession.setLogoutTimerAlarm();
	}
	
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreEmailComposer.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	
	}
	
	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}
	
	protected void onDestroy() {
		super.onDestroy();
		// To notify system that its time to run garbage collector service
		System.gc();
	}
	
}
