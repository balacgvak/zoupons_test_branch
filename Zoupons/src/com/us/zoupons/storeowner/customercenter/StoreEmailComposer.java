package com.us.zoupons.storeowner.customercenter;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.KeyboardUtil;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;

public class StoreEmailComposer extends Activity implements OnClickListener{

	public static String TAG="StoreOwner_EmailComposer";

	public MyHorizontalScrollView scrollView;
	View app;
	LinearLayout btnSlide;
	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	Header header;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;
	View mLeftMenu;
	View customer_leftmenu;
	int mMenuFlag;
	StoreOwner_LeftMenu storeowner_leftmenu;
	ScrollView mMiddleView;
	Button mStoreOwner_EmailComposerFreezeView,mStoreOwner_EmailComposerCancelButton,mStoreOwner_EmailComposerSendButton;
	LinearLayout mStoreOwnerEmailComposerBack;
	TextView mStoreOwnerEmailComposerHeaderText;
	ImageView mStoreOwnerEmailComposerRightMenuImage;
	EditText mStoreOwnerEmailComposerSubject,mStoreOwnerEmailComposerBody;
	public static ScrollView leftMenuScrollView;

	public RelativeLayout btnLogout; 
    public ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
    public Button mNotificationCount;
    //Notification Pop up layout declaration
    private View mPopUpLayout;
    private ListView mNotificationList;
    private ScheduleNotificationSync mNotificationSync;
    CheckLogoutSession mLogoutSession;
    public ImageView mTabBarLoginChoice;
    
    public static LinearLayout mStoreEmailComposerHome,mStoreEmailComposerZpay,mStoreEmailComposerInvoiceCenter,mStoreEmailComposer,mStoreEmailComposerGiftcards,mStoreEmailComposerReceipts,mStoreEmailComposerMyFavourites,mStoreEmailComposerMyFriends,mStoreEmailComposerChat,mStoreEmailComposerRewards,mStoreEmailComposerSettings,mStoreEmailComposerLogout;
    public static TextView mStoreEmailComposerHomeText,mStoreEmailComposerZpayText,mStoreEmailComposerInvoiceCenterText,mStoreEmailComposerText,mStoreEmailComposerGiftCardsText,mStoreEmailComposerReceiptsText,mStoreEmailComposerMyFavoritesText,mStoreEmailComposerMyFriendsText,mStoreEmailComposerChatText,mStoreEmailComposerRewardsText,mStoreEmailComposerSettingsText,mStoreEmailComposerLogoutText;
   
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		mConnectionAvailabilityChecking = new NetworkCheck();

		app = inflater.inflate(R.layout.store_emailcomposer, null);
		mMiddleView = (ScrollView) app.findViewById(R.id.emailcomposer_middleview); 

		mStoreOwnerEmailComposerHeaderText = (TextView) app.findViewById(R.id.store_emailcomposer_storename);
		mStoreOwnerEmailComposerRightMenuImage = (ImageView) app.findViewById(R.id.store_emailcomposer_rightmenu);
		mStoreOwner_EmailComposerFreezeView = (Button) app.findViewById(R.id.store_emailcomposer_freeze);
		mStoreOwnerEmailComposerBack = (LinearLayout) app.findViewById(R.id.store_emailcomposer_footer_back);
		mStoreOwnerEmailComposerSubject = (EditText) app.findViewById(R.id.store_emailcomposer_subject_value);
		mStoreOwnerEmailComposerBody = (EditText) app.findViewById(R.id.store_emailcomposer_body_value);
		mStoreOwner_EmailComposerCancelButton = (Button) app.findViewById(R.id.store_emailcomposer_cancel);
		mStoreOwner_EmailComposerSendButton = (Button) app.findViewById(R.id.store_emailcomposer_send);

		if(getIntent().getExtras().getString("classname").equalsIgnoreCase("StoreEmailComposer")){
			storeowner_leftmenu = new StoreOwner_LeftMenu(StoreEmailComposer.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_EmailComposerFreezeView, TAG);
			storeowner_rightmenu = new StoreOwner_RightMenu(StoreEmailComposer.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_EmailComposerFreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();
			storeowner_leftmenu.clickListener( mLeftMenu /*,mRightMenu*/ );
			mRightMenu = storeowner_rightmenu.intializeCustomerCenterInflater();
			storeowner_rightmenu.customercentermenuClickListener(mLeftMenu,mRightMenu);

			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.store_emailcomposer_header);
			header.setVisibility(View.VISIBLE);
			header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_EmailComposerFreezeView, TAG);
			header.mTabBarNotificationContainer.setVisibility(View.VISIBLE);
			final View[] children = new View[] { mLeftMenu, app,mRightMenu};
			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
			mStoreOwnerEmailComposerRightMenuImage.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag=2, mStoreOwner_EmailComposerFreezeView, TAG,mStoreOwnerEmailComposerSubject,mStoreOwnerEmailComposerBody,false));
			mStoreOwner_EmailComposerFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_EmailComposerFreezeView, TAG,mStoreOwnerEmailComposerSubject,mStoreOwnerEmailComposerBody,true));
			mStoreOwner_EmailComposerCancelButton.setOnClickListener(this);
			mStoreOwner_EmailComposerSendButton.setOnClickListener(this);
			mStoreOwnerEmailComposerBack.setOnClickListener(this);
			mStoreOwnerEmailComposerHeaderText.setText(StoreOwner_RightMenu.mCustomer_FirstName+" "+StoreOwner_RightMenu.mCustomer_LastName);
		}else{
			if(header!=null){
				header.setVisibility(View.GONE);
			}
			
			customer_leftmenu = inflater.inflate(R.layout.horz_scroll_menu, null);
			ViewGroup leftMenuItems = (ViewGroup) customer_leftmenu.findViewById(R.id.menuitems);
			
			leftMenuScrollView = (ScrollView) customer_leftmenu.findViewById(R.id.leftmenu_scrollview);
			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);

			/* Header Tab Bar which contains logout,notification and home buttons*/
			final ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.customer_emailcomposer_tabbar);
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			p.addRule(RelativeLayout.BELOW, R.id.customer_emailcomposer_tabbar);
			mMiddleView.setLayoutParams(p);

			// Notification and log out variables
			btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
			mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
			mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
			btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(StoreEmailComposer.this));//ClickListener for Header Home image
			mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
			mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);   
			
			mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
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
	        
	        btnSlide = (LinearLayout) tabBar.findViewById(R.id.BtnSlide);
	        btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, customer_leftmenu, mStoreOwner_EmailComposerFreezeView,StoreEmailComposer.this));
	        mStoreOwner_EmailComposerFreezeView.setOnClickListener(new ClickListenerForScrolling(scrollView, customer_leftmenu, mStoreOwner_EmailComposerFreezeView,StoreEmailComposer.this));
	        
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
						Log.i(TAG,"Timer Stopped Successfully");
					}
					
					// AsyncTask to call the logout webservice to end the login session
	        		new LogoutSessionTask(StoreEmailComposer.this).execute();
					Intent intent_logout = new Intent(StoreEmailComposer.this,ZouponsLogin.class);
					intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent_logout);
					finish();
				}
			});
	        
	        final View[] children = new View[] { customer_leftmenu, app };
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

	        // Scroll to app (view[1]) when layout finished.
	        int scrollToViewIdx = 1;
	        scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
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
    static class ClickListenerForScrolling implements OnClickListener {
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
        	
        	leftMenuScrollView.fullScroll(View.FOCUS_UP);
    		leftMenuScrollView.pageScroll(View.FOCUS_UP);
    		
            Context context = leftmenu.getContext();
            String msg = "Slide " + new Date();
            System.out.println(msg);

            int menuWidth = leftmenu.getMeasuredWidth();
            

            // Ensure menu is visible
            leftmenu.setVisibility(View.VISIBLE);

            if(mStoreEmailComposer.getCurrentFocus().hasFocus()){
            	mStoreEmailComposer.getCurrentFocus().clearFocus();
            }
			KeyboardUtil.hideKeyboard(mStoreEmailComposer);
			
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
		unregisterReceiver(mReceiver);
		if(mNotificationSync!=null){
			mNotificationSync.cancelAlarm();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreEmailComposer.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreEmailComposer.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreEmailComposer.this);
		mLogoutSession.setLogoutTimerAlarm();
	}
	
	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(StoreEmailComposer.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
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
						if(header != null){
							header.mTabBarNotificationCountBtn.setVisibility(View.VISIBLE);
							header.mTabBarNotificationCountBtn.setText(String.valueOf(NotificationDetails.notificationcount));	
						}else{
							mNotificationCount.setVisibility(View.VISIBLE);
							mNotificationCount.setText(String.valueOf(NotificationDetails.notificationcount));
						}
						
					}else{
						if(header != null){
							header.mTabBarNotificationCountBtn.setVisibility(View.GONE);	
						}else{
							mNotificationCount.setVisibility(View.VISIBLE);
							mNotificationCount.setText(String.valueOf(NotificationDetails.notificationcount));
						}
						Log.i("Notification result", "No new notification");
					}
				}					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};
}
