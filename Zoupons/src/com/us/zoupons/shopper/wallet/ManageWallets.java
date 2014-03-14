/**
 * 
 */
package com.us.zoupons.shopper.wallet;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.EncryptionClass;
import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.android.asyncthread_class.CardOnFilesAsynchThread;
import com.us.zoupons.android.asyncthread_class.EditPinAsynchThread;
import com.us.zoupons.android.asyncthread_class.RemoveCardAsynchThread;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.CardId_ClassVariable;
import com.us.zoupons.classvariables.LoginUser_ClassVariables;
import com.us.zoupons.classvariables.ManageCardAddPin_ClassVariables;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.mobilepay.NormalPaymentAsyncTask;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

/**
 * Class to show,edit and remove credit cards and we can add credit card and we can edit our zoupons pin also.
 */
public class ManageWallets extends Activity{
	
	// Initializing views and variables
	public static MyHorizontalScrollView sWalletScrollView;
    public static View sWalletleftMenu;
    private View mApp;
    private LinearLayout mBtnSlide;
    private Typeface mZouponsFont;
	private String mTAG="ManageWallets";
	// Initializing left manu views
	private LinearLayout mManageCardsHome,mManageCardsZpay,mManageCardsInvoiceCenter,mManageCards,mManageCardsGiftcards,mManageCardsReceipts,mManageCardsMyFavourites,mManageCardsMyFriends,mManageCardsChat,mManageCardsRewards,mManageCardsSettings,mManageCardsLogout;
    private TextView mManageCardsHomeText,mManageCardsZpayText,mManageCardsInvoiceCenterText,mManageCardsText,mManageCardsGiftCardsText,mManageCardsReceiptsText,mManageCardsMyFavoritesText,mManageCardsMyFriendsText,mManageCardsChatText,mManageCardsRewardsText,mManageCardsSettingsText,mManageCardsLogoutText;
    public static LinearLayout mManageCards_CardListContainer,mManageCards_LoginCredentialsContainer,mManageCards_EditPinContainer;
    private LinearLayout mManageCards_MenuBarEditPin,mManageCards_MenuBarAddPin,mManageCards_Invisible1,mManageCards_Invisible2,mCustomerManageCardsHeader;
    private TextView mManageCards_ManageCardsText;
    private ListView mManageCards_CardsListView;
    private TextView mManageCards_Login_UserNameHeader,mManageCards_Login_PasswordHeader;
    private TextView mManageCards_Login_UserNameValue;
    public static EditText mManageCards_Login_PasswordValue;
    private Button mManageCards_Login_Cancel,mManageCards_Login_Ok;
    private TextView mManageCards_EditPin_ExistingPinHeader,mManageCards_EditPin_EnterPinHeader,mManageCards_EditPin_ReEnterPinHeader;
    private EditText mManageCards_EditPin_ExistingPinValue;
    private EditText mManageCards_EditPin_EnterPinValue,mManageCards_EditPin_ReEnterPinValue;
    private Button mManageCards_EditPin_Cancel,mManageCards_EditPin_Ok;
    private RelativeLayout mBtnLogout; 
    private ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
    private Button mNotificationCount;
    private ScheduleNotificationSync mNotificationSync;
    public static Button mManageCardsFreezeView;
    private int mScreenWidth;
    private double mMenuWidth;
    private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private ProgressDialog mProgressDialog=null;
	public static String mAuthenticateFlag;
	private ImageView mTabBarLoginChoice;
	/**
	 * For store owner Billings header variables declaration
	 */
	private Header mZouponsheader;
	private StoreOwner_LeftMenu mStoreowner_leftmenu;
	private StoreOwner_RightMenu mStoreowner_rightmenu;
	private View mLeftMenu,mRightMenu;
	private int mMenuFlag;
	private  ImageView mRightMenuHolder;
	private TextView mStoreOwnerStoreNameText;
	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	public static ScrollView leftMenuScrollView;
	private int mEditPinCancelFlag=0;
	public static final String PREFENCES_FILE = "UserNamePrefences";
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notification
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		sWalletScrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(sWalletScrollView);
		ManageCardAddPin_ClassVariables.mAddPinFlag="false";
		ManageCardAddPin_ClassVariables.mEditCardFlag="false";
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice = new ZouponsWebService(ManageWallets.this);
		mParsingclass = new ZouponsParsingClass(getApplicationContext());
		mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(100);
        //code for hiding the keyboard while the activity is loading.
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mApp = inflater.inflate(R.layout.wallets, null);
		ViewGroup mTitleBar = (ViewGroup) mApp.findViewById(R.id.storeownerbilling_storename_header);
		ViewGroup managecardscontainer = (ViewGroup) mApp.findViewById(R.id.managecards_container);
		ViewGroup managecardsmenubarcontainer = (ViewGroup) mApp.findViewById(R.id.managecards_menubarcontainer);
		mCustomerManageCardsHeader  =(LinearLayout) mApp.findViewById(R.id.customer_header);	
        mManageCards_CardListContainer=(LinearLayout) managecardscontainer.findViewById(R.id.managecards_listviewholder);
        mManageCards_LoginCredentialsContainer=(LinearLayout) managecardscontainer.findViewById(R.id.manageCards_editpin_loginholder);
        mManageCards_EditPinContainer=(LinearLayout) managecardscontainer.findViewById(R.id.manageCards_editpinholder);
        mManageCards_MenuBarEditPin=(LinearLayout) managecardsmenubarcontainer.findViewById(R.id.menubar_editpin);
        mManageCards_MenuBarEditPin.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
        mManageCards_Invisible1=(LinearLayout) managecardsmenubarcontainer.findViewById(R.id.menubar_invisible1);
        mManageCards_Invisible1.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
        mManageCards_Invisible2=(LinearLayout) managecardsmenubarcontainer.findViewById(R.id.menubar_invisible2);
        mManageCards_Invisible2.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
        mManageCards_MenuBarAddPin=(LinearLayout) managecardsmenubarcontainer.findViewById(R.id.menubar_addcards);
        mManageCards_MenuBarAddPin.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
        mManageCards_ManageCardsText=(TextView) managecardscontainer.findViewById(R.id.managecreditcards);
        mManageCards_ManageCardsText.setTypeface(mZouponsFont);
        mManageCards_CardsListView=(ListView) managecardscontainer.findViewById(R.id.cardListItem);
        mManageCards_Login_UserNameHeader=(TextView) managecardscontainer.findViewById(R.id.managecards_login_usernameheader);
        mManageCards_Login_UserNameHeader.setTypeface(mZouponsFont);
        mManageCards_Login_PasswordHeader=(TextView) managecardscontainer.findViewById(R.id.managecards_login_passwordheader);
        mManageCards_Login_PasswordHeader.setTypeface(mZouponsFont);
        mManageCards_Login_UserNameValue=(TextView) managecardscontainer.findViewById(R.id.managecards_login_username);
        mManageCards_Login_PasswordValue=(EditText) managecardscontainer.findViewById(R.id.managecards_login_password);
        mManageCards_Login_Cancel=(Button) managecardscontainer.findViewById(R.id.managecards_login_cancel);
        mManageCards_Login_Ok=(Button) managecardscontainer.findViewById(R.id.managecards_login_ok);
        mManageCards_EditPin_ExistingPinHeader=(TextView) managecardscontainer.findViewById(R.id.managecards_editpin_existingpinheader);
        mManageCards_EditPin_ExistingPinHeader.setTypeface(mZouponsFont);
        mManageCards_EditPin_EnterPinHeader=(TextView) managecardscontainer.findViewById(R.id.managecards_editpin_enterpinheader);
        mManageCards_EditPin_EnterPinHeader.setTypeface(mZouponsFont);
        mManageCards_EditPin_ReEnterPinHeader=(TextView) managecardscontainer.findViewById(R.id.managecards_editpin_reenterpinheader);
        mManageCards_EditPin_ReEnterPinHeader.setTypeface(mZouponsFont);
        mManageCards_EditPin_ExistingPinValue=(EditText) managecardscontainer.findViewById(R.id.managecards_editpin_existingpinvalue);
        mManageCards_EditPin_ExistingPinValue.setText(UserDetails.mServicePin);
        mManageCards_EditPin_EnterPinValue=(EditText) managecardscontainer.findViewById(R.id.managecards_editpin_enterpinvalue);
        mManageCards_EditPin_ReEnterPinValue=(EditText) managecardscontainer.findViewById(R.id.managecards_editpin_reenterpinvalue);
        mManageCards_EditPin_Cancel=(Button) managecardscontainer.findViewById(R.id.managecards_editpin_cancel);
        mManageCards_EditPin_Ok=(Button) managecardscontainer.findViewById(R.id.managecards_editpin_ok);
        mManageCardsFreezeView = (Button) mApp.findViewById(R.id.managecards_freezeview); 
       
        // To set user name if remember me is checked during login
        SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
        if(!mPrefs.getString("username", "").equals("")) {
        	mManageCards_Login_UserNameValue.setText(mPrefs.getString("username", "").trim());
		}else{
			mManageCards_Login_UserNameValue.setText(UserDetails.mServiceEmail);
		}
               
        if(getIntent().hasExtra("FromStoreOwnerBilling")){ // for store_owner point of sale
        	mStoreowner_leftmenu = new StoreOwner_LeftMenu(ManageWallets.this,sWalletScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mManageCardsFreezeView, mTAG);
			mLeftMenu = mStoreowner_leftmenu.intializeInflater();
			mStoreowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			mStoreowner_rightmenu = new StoreOwner_RightMenu(ManageWallets.this,sWalletScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mManageCardsFreezeView, mTAG);
		    mRightMenu = mStoreowner_rightmenu.intializeInflater();
		    mStoreowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsheader = (Header) mApp.findViewById(R.id.storeowner_billings_header);
			mZouponsheader.setVisibility(View.VISIBLE);
			mCustomerManageCardsHeader.setVisibility(View.GONE);
			mTitleBar.setVisibility(View.VISIBLE);
			mZouponsheader.intializeInflater(sWalletScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mManageCardsFreezeView, mTAG);
			final View[] children = new View[] { mLeftMenu, mApp,mRightMenu};
			/* Scroll to app (view[1]) when layout finished.*/
			int scrollToViewIdx = 1;
			sWalletScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsheader.mLeftMenuBtnSlide));
			mManageCardsFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(sWalletScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mManageCardsFreezeView, mTAG));
			mRightMenuHolder = (ImageView) mTitleBar.findViewById(R.id.storeownerbilling_rightmenu);
		    mStoreOwnerStoreNameText = (TextView) mTitleBar.findViewById(R.id.storeownerbilling_title_textId);
		    mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(sWalletScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mManageCardsFreezeView, mTAG));
		    SharedPreferences mStoreDetailsPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
			String mStoreName = mStoreDetailsPrefs.getString("store_name", "");
			mStoreOwnerStoreNameText.setText(mStoreName);
			findViewById(R.id.creditcards_activationInfo).setVisibility(View.VISIBLE);
			mNotificationReceiver = new NotifitcationReceiver(mZouponsheader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsheader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsheader,mZouponsheader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsheader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsheader,mZouponsheader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
		}else{ // For customer module
			mTitleBar.setVisibility(View.GONE);
			sWalletleftMenu = inflater.inflate(R.layout.shopper_leftmenu, null);
			ViewGroup leftMenuItems = (ViewGroup) sWalletleftMenu.findViewById(R.id.menuitems);
			final ViewGroup tabBar = (ViewGroup) mApp.findViewById(R.id.managecards_tabBar);
			leftMenuScrollView = (ScrollView) sWalletleftMenu.findViewById(R.id.leftmenu_scrollview);
			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);
			// Notification and log out variables
			mBtnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
			mLogoutImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_logout_btn);
			mNotificationImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_notificationImageId);
			mBtnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(ManageWallets.this));//ClickListener for Header Home image
			mCalloutTriangleImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_callout_triangle);
			mNotificationCount = (Button) mBtnLogout.findViewById(R.id.zoupons_notification_count);   
			mTabBarLoginChoice = (ImageView) mBtnLogout.findViewById(R.id.store_header_loginchoice);
			new LoginChoiceTabBarImage(ManageWallets.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
			mManageCardsHome = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_home);
	        mManageCardsZpay = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zpay);
	        mManageCardsInvoiceCenter = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_invoicecenter);
	        mManageCards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_managecards);
	        mManageCardsGiftcards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zgiftcards);
	        mManageCardsReceipts = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_receipts);
	        mManageCardsMyFavourites = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfavourites);
	        mManageCardsMyFriends = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfriends);
	        mManageCardsChat = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_customercenter);
	        mManageCardsRewards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_rewards);
	        mManageCardsSettings = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_settings);
	        mManageCardsLogout = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_logout);
			mManageCardsHomeText = (TextView) leftMenuItems.findViewById(R.id.menuHome);
	        mManageCardsHomeText.setTypeface(mZouponsFont);
	        mManageCardsZpayText = (TextView) leftMenuItems.findViewById(R.id.menuZpay);
	        mManageCardsZpayText.setTypeface(mZouponsFont);
	        mManageCardsInvoiceCenterText = (TextView) leftMenuItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
	        mManageCardsInvoiceCenterText.setTypeface(mZouponsFont);
	        mManageCardsText = (TextView) leftMenuItems.findViewById(R.id.menuManageCards);
	        mManageCardsText.setTypeface(mZouponsFont);
	        mManageCardsGiftCardsText = (TextView) leftMenuItems.findViewById(R.id.menuGiftCards);
	        mManageCardsGiftCardsText.setTypeface(mZouponsFont);
	        mManageCardsReceiptsText = (TextView) leftMenuItems.findViewById(R.id.menuReceipts);
	        mManageCardsReceiptsText.setTypeface(mZouponsFont);
	        mManageCardsMyFavoritesText = (TextView) leftMenuItems.findViewById(R.id.menufavorites);
	        mManageCardsMyFavoritesText.setTypeface(mZouponsFont);
	        mManageCardsMyFriendsText = (TextView) leftMenuItems.findViewById(R.id.menuMyFriends);
	        mManageCardsMyFriendsText.setTypeface(mZouponsFont);
	        mManageCardsChatText = (TextView) leftMenuItems.findViewById(R.id.menuCustomerCenter);
	        mManageCardsChatText.setTypeface(mZouponsFont);
	        mManageCardsRewardsText = (TextView) leftMenuItems.findViewById(R.id.menuRewards);
	        mManageCardsRewardsText.setTypeface(mZouponsFont);
	        mManageCardsSettingsText = (TextView) leftMenuItems.findViewById(R.id.menuSettings);
	        mManageCardsSettingsText.setTypeface(mZouponsFont);
	        mManageCardsLogoutText = (TextView) leftMenuItems.findViewById(R.id.menuLogout);
	        mManageCardsLogoutText.setTypeface(mZouponsFont);
	        mBtnSlide = (LinearLayout) tabBar.findViewById(R.id.BtnSlide);
	        mBtnSlide.setOnClickListener(new ClickListenerForScrolling(sWalletScrollView, sWalletleftMenu,mManageCardsFreezeView));
	        mManageCardsFreezeView.setOnClickListener(new ClickListenerForScrolling(sWalletScrollView, sWalletleftMenu, mManageCardsFreezeView));
	        mManageCards.setOnClickListener(new ClickListenerForScrolling(sWalletScrollView, sWalletleftMenu, mManageCardsFreezeView));
			
	        mManageCardsHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, ManageWallets.this,POJOMainMenuActivityTAG.TAG=mTAG));
	        mManageCardsZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, ManageWallets.this,POJOMainMenuActivityTAG.TAG=mTAG));
	        mManageCardsInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, ManageWallets.this,POJOMainMenuActivityTAG.TAG=mTAG));
	        mManageCardsGiftcards.setOnClickListener(new MenuItemClickListener(leftMenuItems,ManageWallets.this,POJOMainMenuActivityTAG.TAG=mTAG));
	        mManageCardsReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, ManageWallets.this,POJOMainMenuActivityTAG.TAG=mTAG));
	        mManageCardsMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,ManageWallets.this,POJOMainMenuActivityTAG.TAG=mTAG));
	        mManageCardsMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,ManageWallets.this,POJOMainMenuActivityTAG.TAG=mTAG));
	        mManageCardsChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,ManageWallets.this,POJOMainMenuActivityTAG.TAG=mTAG));
	        mManageCardsRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,ManageWallets.this,POJOMainMenuActivityTAG.TAG=mTAG));
	        mManageCardsSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,ManageWallets.this,POJOMainMenuActivityTAG.TAG=mTAG));
	        mManageCardsLogout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					v.setBackgroundResource(R.drawable.gradient_bg_hover);
					
					if(NormalPaymentAsyncTask.mCountDownTimer!=null){
						NormalPaymentAsyncTask.mCountDownTimer.cancel();
						NormalPaymentAsyncTask.mCountDownTimer = null;
						Log.i(mTAG,"Timer Stopped Successfully");
					}
					
					// AsyncTask to call the logout webservice to end the login session
	        		new LogoutSessionTask(ManageWallets.this,"FromManualLogOut").execute();
				}
			});
	        
	        final View[] children = new View[] { sWalletleftMenu, mApp };
	        // Notitification pop up layout declaration
	        mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
	    	mNotificationImage.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
	    	mNotificationCount.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));

	        // Scroll to app (view[1]) when layout finished.
	        int scrollToViewIdx = 1;
	        sWalletScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(mBtnSlide));
	        
	        mLogoutImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// AsyncTask to call the logout webservice to end the login session
	        		new LogoutSessionTask(ManageWallets.this,"FromManualLogOut").execute();
				}
			});
		}
        
        //Call function to get width of the screen
        mScreenWidth=getScreenWidth(); 	
        if(mScreenWidth>0){	//To fix Home Page menubar items width
        	mMenuWidth=mScreenWidth/4;
        }
        
        if(getIntent().hasExtra("FromStoreOwnerBilling")){ // Store Billing
        	CardOnFilesAsynchThread cardonfilesasynchthread =new CardOnFilesAsynchThread(ManageWallets.this,mManageCards_CardsListView,mManageCards_MenuBarEditPin,"store_creditcards");
            cardonfilesasynchthread.execute("","","");
        }else{ // Customer Billing
        	CardOnFilesAsynchThread cardonfilesasynchthread =new CardOnFilesAsynchThread(ManageWallets.this,mManageCards_CardsListView,mManageCards_MenuBarEditPin,"customer_creditcards");
            cardonfilesasynchthread.execute("","","");	
        }
        
        mManageCards_EditPin_EnterPinValue.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(mManageCards_EditPin_EnterPinValue.getText().toString().length()<4 && mManageCards_EditPin_ReEnterPinValue.isFocused() && mEditPinCancelFlag>0){
						alertBox_validation("PIN Number should contain 4 numbers");
					}
				}
			}
		});
        
        mManageCards_MenuBarEditPin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mEditPinCancelFlag=1;
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mAuthenticateFlag="editpin";
				mManageCards_Login_PasswordValue.setText("");
				mManageCards_CardListContainer.setVisibility(View.GONE);
				mManageCards_LoginCredentialsContainer.setVisibility(View.VISIBLE);
			}
		});
        
        mManageCards_MenuBarAddPin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mEditPinCancelFlag=2;
				mManageCards_MenuBarEditPin.setBackgroundResource(R.drawable.header_2);
				ManageCardAddPin_ClassVariables.mAddPinFlag="true";
				ManageCardAddPin_ClassVariables.mEditCardFlag="false";
				Intent intent_addcard = new Intent(ManageWallets.this,AddCreditCard.class);
				if(getIntent().hasExtra("FromStoreOwnerBilling")){
					intent_addcard.putExtra("class_name", "StoreOwner_Billing");	
				}else{
					intent_addcard.putExtra("class_name", mTAG);	
				}
				intent_addcard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_addcard);
			}
		});
        
        mManageCards_Login_Cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mEditPinCancelFlag=0;
				mManageCards_MenuBarEditPin.setBackgroundResource(R.drawable.header_2);
				if(mManageCards_Login_PasswordValue.getText().toString().length()>0){
					mManageCards_Login_PasswordValue.getText().clear();
				}
				mManageCards_CardListContainer.setVisibility(View.VISIBLE);
				mManageCards_LoginCredentialsContainer.setVisibility(View.GONE);
				mManageCards_EditPin_EnterPinValue.getText().clear();
				mManageCards_EditPin_ReEnterPinValue.getText().clear();
			}
		});
        
        mManageCards_Login_Ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginValidation();
			}
		});

        mManageCards_EditPin_Cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mEditPinCancelFlag=0;
				if(mManageCards_EditPin_EnterPinValue.getText().toString().trim().length()>0){
					mManageCards_EditPin_EnterPinValue.getText().clear();
				}if(mManageCards_EditPin_ReEnterPinValue.getText().toString().trim().length()>0){
					mManageCards_EditPin_ReEnterPinValue.getText().clear();
				}
				
				mManageCards_MenuBarEditPin.setBackgroundResource(R.drawable.header_2);
				mManageCards_CardListContainer.setVisibility(View.VISIBLE);
				mManageCards_LoginCredentialsContainer.setVisibility(View.GONE);
				mManageCards_EditPinContainer.setVisibility(View.GONE);
			}
		});
        
        mManageCards_EditPin_Ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mManageCards_CardListContainer.setVisibility(View.GONE);
				mManageCards_LoginCredentialsContainer.setVisibility(View.GONE);
				mManageCards_EditPinContainer.setVisibility(View.VISIBLE);
				if(mManageCards_EditPin_EnterPinValue.getText().toString().trim().equals("")){
					alertBox_validation("Enter Pin Number");
					mManageCards_EditPin_EnterPinValue.requestFocus();
				}else if(mManageCards_EditPin_EnterPinValue.getText().toString().length()<4 && mEditPinCancelFlag>0){
					alertBox_validation("PIN Number should contain 4 numbers");
					mManageCards_EditPin_EnterPinValue.requestFocus();
				}else if(mManageCards_EditPin_ReEnterPinValue.getText().toString().trim().equals("")){
					alertBox_validation("ReEnter Pin Number");
					mManageCards_EditPin_ReEnterPinValue.requestFocus();
				}else if(!mManageCards_EditPin_EnterPinValue.getText().toString().trim().contentEquals(mManageCards_EditPin_ReEnterPinValue.getText().toString().trim())){
					alertBox_validation("Enter the Correct Pin to confirm");
					mManageCards_EditPin_ReEnterPinValue.setText("");
					mManageCards_EditPin_ReEnterPinValue.requestFocus();
				}else{
					new EditPinAsynchThread(ManageWallets.this,mManageCards_EditPin_EnterPinValue,mManageCards_EditPin_ReEnterPinValue,mManageCards_MenuBarEditPin).execute(mManageCards_EditPin_ReEnterPinValue.getText().toString().trim(),"","");
				}
			}
		});
	}

    //Get Screen width
    public int getScreenWidth(){
    	int Measuredwidth = 0;  
    	Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	Measuredwidth = display.getWidth(); 
    	return Measuredwidth;
    }
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sWalletScrollView = null;
	    sWalletleftMenu = null;
	    mManageCards_CardListContainer = null ;mManageCards_LoginCredentialsContainer = null;mManageCards_EditPinContainer = null;
	    mManageCardsFreezeView = null;mManageCards_Login_PasswordValue= null;leftMenuScrollView = null;
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
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(ManageWallets.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	protected void onResume() {
		super.onResume();
		POJOMainMenuActivityTAG.TAG = mTAG;
		// To listen for broadcast receiver to receive notification message
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		 if(getIntent().hasExtra("FromStoreOwnerBilling")){ // for store_owner point of sale
			 mNotificationSync = new ScheduleNotificationSync(ManageWallets.this,ZouponsConstants.sStoreModuleFlag);
		 }else{
			 mNotificationSync = new ScheduleNotificationSync(ManageWallets.this,ZouponsConstants.sCustomerModuleFlag);	 
		 }
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(ManageWallets.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(ManageWallets.this);
		mLogoutSession.setLogoutTimerAlarm();

	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
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
	}
	
    @Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	/**
     * Helper for examples with a HSV that should be scrolled by a menu View's width.
     */
    class ClickListenerForScrolling implements OnClickListener {
        HorizontalScrollView scrollView;
        View leftmenu;
        Button freezeView;
        /**
         * Menu must NOT be out/shown to start with.
         */

        public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu,Button freezeview) {
            super();
            this.scrollView = scrollView;
            this.leftmenu = menu;
            this.freezeView=freezeview;
        }

        @Override
        public void onClick(View v) {
        	
        	leftMenuScrollView.fullScroll(View.FOCUS_UP);
    		leftMenuScrollView.pageScroll(View.FOCUS_UP);
    		
            Context context = leftmenu.getContext();
            String msg = "Slide " + new Date();
            System.out.println(msg);

            int menuWidth = leftmenu.getMeasuredWidth();
            Log.i(mTAG,"Menu Width : "+ menuWidth);

            // Ensure menu is visible
            leftmenu.setVisibility(View.VISIBLE);

            InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
			
            if (!MenuOutClass.MANAGECARDS_MENUOUT) {
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
            MenuOutClass.MANAGECARDS_MENUOUT = !MenuOutClass.MANAGECARDS_MENUOUT;
        }
    }
    
    /**
     * 
     * Helper class to calculate menu width 
     *
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
    
    // To check for valid user
    public void loginValidation(){
    	if(mConnectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
    		if(mManageCards_Login_PasswordValue.getText().toString().equals("")){
				alertBox_validation("Enter Password");
				mManageCards_Login_PasswordValue.requestFocus();
			}else{
				mProgressDialog.show();
				final Thread syncThread = new Thread(new Runnable() {

					@Override
					public void run() {
						Bundle bundle = new Bundle();
						String mGetResponse=null;
						Message msg_response = new Message();
						
						try{
							if(mConnectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
								mGetResponse=mZouponswebservice.login(mManageCards_Login_UserNameValue.getText().toString().trim(), new EncryptionClass().md5(mManageCards_Login_PasswordValue.getText().toString()));	//Get login response values from webservice
								if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
									String mParsingResponse=mParsingclass.parseLoginXmlData(mGetResponse,mTAG);
									if(mParsingResponse.equalsIgnoreCase("success")){
										for(int i=0;i<WebServiceStaticArrays.mLoginUserList.size();i++){
											LoginUser_ClassVariables parsedobjectvalues = (LoginUser_ClassVariables) WebServiceStaticArrays.mLoginUserList.get(i);
											if(!parsedobjectvalues.message.equalsIgnoreCase("Your account still not activated")){
												if(parsedobjectvalues.mFlag.equals("no")){
													updateHandler(bundle, msg_response, "Invalid User");	//send update to handler
												}else if(parsedobjectvalues.mFlag.equals("yes")){
													updateHandler(bundle, msg_response, "Valid User");
												}
											}else{
												updateHandler(bundle, msg_response, "InActive User");
											}
										}
									}else if(mParsingResponse.equalsIgnoreCase("failure")){
										updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
										Log.i(mTAG,"Error");
									}else if(mParsingResponse.equalsIgnoreCase("norecords")){
										updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
										Log.i(mTAG,"No Records");
									}
								}else{
									updateHandler(bundle, msg_response, mGetResponse);	//send update to handler about login response
								}
							}else{
								Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
							}
						}catch(Exception e){
							Log.i(mTAG,"Thread Error");
							e.printStackTrace();
						}
						mProgressDialog.setProgress(100);
						mProgressDialog.dismiss();
					}
				});syncThread.start();
			}
		}else{
			Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
		}
    }
    
	// To send message to handler to update UI elements
    private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse){
		bundle.putString("returnResponse", mGetResponse);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}
	
    // Handler to update UI elements
	Handler handler_response = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			String key = b.getString("returnResponse");
			if(key.trim().equals("failure")){
				alertBox_validation("Unable to reach Service.");
				mManageCards_Login_PasswordValue.setText("");
			}else if(key.trim().equals("noresponse")||key.trim().equals("norecords")){
				alertBox_validation("No Data Available");
				mManageCards_MenuBarEditPin.setBackgroundResource(R.drawable.header_2);
			}else if(key.trim().equals("Invalid User")){
				alertBox_validation("Invalid User");
				mManageCards_Login_PasswordValue.setText("");
			}else if(key.trim().equals("Valid User")){
				if(mAuthenticateFlag.equals("editpin")){
					mManageCards_CardListContainer.setVisibility(View.GONE);
					mManageCards_LoginCredentialsContainer.setVisibility(View.GONE);
					mManageCards_EditPinContainer.setVisibility(View.VISIBLE);
				}else if(mAuthenticateFlag.equals("editcard")){
					Intent intent_addcard = new Intent(ManageWallets.this,AddCreditCard.class);
					if(getIntent().hasExtra("FromStoreOwnerBilling")){
						intent_addcard.putExtra("class_name", "StoreOwner_Billing");	
					}else{
						intent_addcard.putExtra("class_name", mTAG);	
					}
					intent_addcard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent_addcard);
				}else if(mAuthenticateFlag.equals("removecard")){
					if(getIntent().hasExtra("FromStoreOwnerBilling")){   
						new RemoveCardAsynchThread(ManageWallets.this,mManageCards_CardsListView,mManageCards_MenuBarEditPin,"store_creditcards").execute(CardId_ClassVariable.cardid,"","");	
					}else{
						new RemoveCardAsynchThread(ManageWallets.this,mManageCards_CardsListView,mManageCards_MenuBarEditPin,"customer_creditcards").execute(CardId_ClassVariable.cardid,"","");
					}
				}
			}else {
				alertBox_validation("No Data Available");
			}
		}
	};
	
	// Funtion to show alert pop up with respective message
    private void alertBox_validation(final String msg) {
		AlertDialog.Builder validation_alert = new AlertDialog.Builder(this);
		validation_alert.setTitle("Information");
		validation_alert.setMessage(msg);
		validation_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equalsIgnoreCase("PIN Number should contain 4 numbers")){
					mManageCards_EditPin_EnterPinValue.getText().clear();
					mManageCards_EditPin_EnterPinValue.requestFocus();
				}
				dialog.dismiss();
			}
		});
		validation_alert.show();
	}
}