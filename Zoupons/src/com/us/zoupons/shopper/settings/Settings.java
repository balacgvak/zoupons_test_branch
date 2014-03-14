package com.us.zoupons.shopper.settings;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

import com.us.zoupons.DecodeImageWithRotation;
import com.us.zoupons.EncryptionClass;
import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.KeyboardUtil;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MobileNumberFilter;
import com.us.zoupons.MobileNumberTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.android.asyncthread_class.EditPinAsynchThread;
import com.us.zoupons.android.asyncthread_class.EditSecurityDetailsTask;
import com.us.zoupons.android.asyncthread_class.GetUpdateUserProfileTask;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.LoginUser_ClassVariables;
import com.us.zoupons.classvariables.MobileCarriers_ClassVariables;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.SecurityQuestions_ClassVariables;
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
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

public class Settings extends Activity {

	// Initializing views and variables
	public static MyHorizontalScrollView sScrollView;    
	public static View sLeftMenu;
	private View mApp;
	private ImageView mBtnSlide;
	private RelativeLayout mBtnLogout,mVerificationPopupLayout; 
	private ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	private Button mNotificationCount;
	public static Button sSettingsFreezeView;
	private Typeface mZouponsFont;
	private LinearLayout mSettingsHome,mSettingsZpay,mSettingsInvoiceCenter,mSettingsManageCards,mSettingsGiftcards,mSettingsReceipts,mSettingsMyFavourites,mSettingsMyFriends,mSettingsChat,mSettingsRewards,mSettingsSettings,mSettingsLogout,mSettingsLoginLayout;
	private TextView mSettingsHomeText,mSettingsZpayText,mSettingsInvoiceCenterText,mSettingsManageCardsText,mSettingsGiftCardsText,mSettingsReceiptsText,mSettingsMyFavoritesText,mSettingsMyFriendsText,mSettingsChatText,mSettingsRewardsText,mSettingsSettingsText,mSettingsLogoutText;
	public String mTAG = "Settings";
	private LinearLayout mNotification,mContactInfo,mEditSecurityDetails,mSecurity,mChangePasswordFields,mChangeSecurityQuestionsField,
	mContactInfoFields;
	private ScrollView mContactInfoView,mEditPINDetailsContainer,mEditSecurityDetailsContainer;
	private RelativeLayout mSettingsSecurityEditButtonLayout,mEditPinLayout;
	private LinearLayout mAddImageLayout;
	private TextView mUserExistingPINText,mUserAddImageText;
	private Button mSettingsLoginButton,mContactInfoSaveButton;
	private EditText mUserNameValue,mPasswordValue,mUserFirstName,mUserLastName,mUserEmail,mUserMobileNumber,mUserMobileCarrier,
	mUserNewPassword,mUserConfirmPassword,mUserSecurityQuestion1,mUserSecurityQuestion2,mUserSecurityAnswer1,mUserSecurityAnswer2,
	mUserExistingPIN,mUserNewPIN,mUserReEnterPIN,mVerificationCodeValue;
	private ImageView mUserProfileImage,mMobileCarrierImage,mSecurityQuestion1Image;
	private Button mEditSecurityDetailsSaveButton,mEditSecurityDetailsBackButton,mEditPinSaveButton,mEditPinBackButton,mVerificationOkButton,mVerificationCancelButton;
	private int mScreenWidth;
	private double mSettingsMenuWidth;
	private ViewGroup settingsmenubarcontainer;
	private CheckBox mEditPasswordBox,mEditSecurityQuestionsBox;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private ProgressDialog mProgressDialog= null;
	private Handler mHandler;
	public static String mProfilePhoto;
	private String mUserPassword;
	public static String mSqId1,mSqId2;
	private static final String PREFENCES_FILE = "UserNamePrefences";
	public String mMobileCarrierId;
	private ScheduleNotificationSync mNotificationSync;
	private EncryptionClass mEncryption;
	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	private ImageView mTabBarLoginChoice;
	// For storeowner settings
	private	Header mZouponsheader;
	private StoreOwner_RightMenu mStoreowner_rightmenu;
	private View mRightMenu;
	private View mLeftMenu;
	private int mMenuFlag;
	private StoreOwner_LeftMenu mStoreowner_leftmenu;
	public static ScrollView sLeftMenuScrollView;
	private int mMenuBarFlag=1;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		sScrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(sScrollView);
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice = new ZouponsWebService(Settings.this);
		mParsingclass = new ZouponsParsingClass(getApplicationContext());
		mEncryption = new EncryptionClass();
		mProgressDialog=new ProgressDialog(this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
		mApp = inflater.inflate(R.layout.settings, null);
		sSettingsFreezeView =(Button) mApp.findViewById(R.id.settings_freeze);
		ViewGroup settingscontainer = (ViewGroup) mApp.findViewById(R.id.settings_container);
		settingsmenubarcontainer = (ViewGroup) mApp.findViewById(R.id.settings_menubarcontainer);
		mSettingsLoginLayout = (LinearLayout) settingscontainer.findViewById(R.id.settings_loginLayoutId);
		mNotification=(LinearLayout) settingsmenubarcontainer.findViewById(R.id.settings_menubar_notification);
		mNotification.setLayoutParams(new LinearLayout.LayoutParams((int)mSettingsMenuWidth,LayoutParams.FILL_PARENT,1f));
		mContactInfo=(LinearLayout) settingsmenubarcontainer.findViewById(R.id.settings_menubar_contactinfo);
		mContactInfo.setLayoutParams(new LinearLayout.LayoutParams((int)mSettingsMenuWidth,LayoutParams.FILL_PARENT,1f));
		mEditSecurityDetails=(LinearLayout) settingsmenubarcontainer.findViewById(R.id.settings_menubar_managecards);
		mEditSecurityDetails.setLayoutParams(new LinearLayout.LayoutParams((int)mSettingsMenuWidth,LayoutParams.FILL_PARENT,1f));
		mSecurity=(LinearLayout) settingsmenubarcontainer.findViewById(R.id.settings_menubar_security);
		mSecurity.setLayoutParams(new LinearLayout.LayoutParams((int)mSettingsMenuWidth,LayoutParams.FILL_PARENT,1f));
		mContactInfoView=(ScrollView) settingscontainer.findViewById(R.id.settings_contactinfo_scrollview);
		mEditSecurityDetailsContainer=(ScrollView) settingscontainer.findViewById(R.id.settings_security_scrollview);
		mEditPINDetailsContainer = (ScrollView) settingscontainer.findViewById(R.id.settings_editpin_scrollview);
		mEditPinLayout = (RelativeLayout) mEditPINDetailsContainer.findViewById(R.id.settings_editPin_ItemsId); 
		// Login field varaible initialization
		mUserNameValue = (EditText) mSettingsLoginLayout.findViewById(R.id.settings_loginUserNameId);
		final ViewGroup tabBar = (ViewGroup) mApp.findViewById(R.id.settings_tabBar);
		/* Header Tab Bar which contains logout,notification and home buttons*/
		mZouponsheader = (Header) mApp.findViewById(R.id.storeowner_settings_header);

		if(getIntent().hasExtra("Store_Owner_Settings")){ // for store_owner settings
			mStoreowner_rightmenu = new StoreOwner_RightMenu(Settings.this,sScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, sSettingsFreezeView, mTAG);
			mRightMenu = mStoreowner_rightmenu.intializeInflater();
			mStoreowner_leftmenu = new StoreOwner_LeftMenu(Settings.this,sScrollView, mLeftMenu, mMenuFlag=1, sSettingsFreezeView, mTAG);
			mLeftMenu = mStoreowner_leftmenu.intializeInflater();
			mStoreowner_leftmenu.clickListener(mLeftMenu);
			mZouponsheader.intializeInflater(sScrollView, mLeftMenu, mMenuFlag=1, sSettingsFreezeView, mTAG);
			mStoreowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
			tabBar.setVisibility(View.GONE);
			mZouponsheader.setVisibility(View.VISIBLE);
			mNotification.setVisibility(View.GONE);
			final View[] children = new View[] { mLeftMenu, mApp };
			/* Scroll to app (view[1]) when layout finished. */
			int scrollToViewIdx = 1;
			sScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsheader.mLeftMenuBtnSlide));
			sSettingsFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(sScrollView, mLeftMenu, mMenuFlag, sSettingsFreezeView, mTAG));
			mNotificationReceiver = new NotifitcationReceiver(mZouponsheader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsheader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsheader,mZouponsheader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsheader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsheader,mZouponsheader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
		}else{ // Shopper settings
			sLeftMenu = inflater.inflate(R.layout.shopper_leftmenu, null);
			tabBar.setVisibility(View.VISIBLE);
			mZouponsheader.setVisibility(View.GONE);
			mBtnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide);
			mBtnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sSettingsFreezeView));
			sSettingsFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sSettingsFreezeView));
			ViewGroup leftMenuItems = (ViewGroup) sLeftMenu.findViewById(R.id.menuitems);
			sLeftMenuScrollView = (ScrollView) sLeftMenu.findViewById(R.id.leftmenu_scrollview);
			sLeftMenuScrollView.fullScroll(View.FOCUS_UP);
			sLeftMenuScrollView.pageScroll(View.FOCUS_UP);
			//LeftMenu
			mSettingsHome = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_home);
			mSettingsZpay = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zpay);
			mSettingsInvoiceCenter = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_invoicecenter);
			mSettingsManageCards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_managecards);
			mSettingsGiftcards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zgiftcards);
			mSettingsReceipts = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_receipts);
			mSettingsMyFavourites = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfavourites);
			mSettingsMyFriends = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfriends);
			mSettingsChat = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_customercenter);
			mSettingsRewards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_rewards);
			mSettingsSettings = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_settings);
			mSettingsLogout = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_logout);
			mSettingsHomeText = (TextView) leftMenuItems.findViewById(R.id.menuHome);
			mSettingsHomeText.setTypeface(mZouponsFont);
			mSettingsZpayText = (TextView) leftMenuItems.findViewById(R.id.menuZpay);
			mSettingsZpayText.setTypeface(mZouponsFont);
			mSettingsInvoiceCenterText = (TextView) leftMenuItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
			mSettingsInvoiceCenterText.setTypeface(mZouponsFont);
			mSettingsManageCardsText = (TextView) leftMenuItems.findViewById(R.id.menuManageCards);
			mSettingsManageCardsText.setTypeface(mZouponsFont);
			mSettingsGiftCardsText = (TextView) leftMenuItems.findViewById(R.id.menuGiftCards);
			mSettingsGiftCardsText.setTypeface(mZouponsFont);
			mSettingsReceiptsText = (TextView) leftMenuItems.findViewById(R.id.menuReceipts);
			mSettingsReceiptsText.setTypeface(mZouponsFont);
			mSettingsMyFavoritesText = (TextView) leftMenuItems.findViewById(R.id.menufavorites);
			mSettingsMyFavoritesText.setTypeface(mZouponsFont);
			mSettingsMyFriendsText = (TextView) leftMenuItems.findViewById(R.id.menuMyFriends);
			mSettingsMyFriendsText.setTypeface(mZouponsFont);
			mSettingsChatText = (TextView) leftMenuItems.findViewById(R.id.menuCustomerCenter);
			mSettingsChatText.setTypeface(mZouponsFont);
			mSettingsRewardsText = (TextView) leftMenuItems.findViewById(R.id.menuRewards);
			mSettingsRewardsText.setTypeface(mZouponsFont);
			mSettingsSettingsText = (TextView) leftMenuItems.findViewById(R.id.menuSettings);
			mSettingsSettingsText.setTypeface(mZouponsFont);
			mSettingsLogoutText = (TextView) leftMenuItems.findViewById(R.id.menuLogout);
			mSettingsLogoutText.setTypeface(mZouponsFont);
			// Notification and log out variables
			mBtnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
			mLogoutImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_logout_btn);
			mNotificationImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_notificationImageId);
			mBtnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(Settings.this));//ClickListener for Header Home image
			mCalloutTriangleImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_callout_triangle);
			mNotificationCount = (Button) mBtnLogout.findViewById(R.id.zoupons_notification_count);
			mTabBarLoginChoice = (ImageView) mBtnLogout.findViewById(R.id.store_header_loginchoice);
			new LoginChoiceTabBarImage(Settings.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
			final View[] children = new View[] { sLeftMenu, mApp };
			// Notitification pop up layout declaration
			mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
			mNotificationImage.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
			mNotificationCount.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(mBtnSlide));
			//Left Menu Items click Listeners
			mSettingsHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, Settings.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mSettingsZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, Settings.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mSettingsInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, Settings.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mSettingsGiftcards.setOnClickListener(new MenuItemClickListener(leftMenuItems,Settings.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mSettingsManageCards.setOnClickListener(new MenuItemClickListener(leftMenuItems, Settings.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mSettingsReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, Settings.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mSettingsMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,Settings.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mSettingsMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,Settings.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mSettingsChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,Settings.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mSettingsRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,Settings.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mSettingsSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,Settings.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mSettingsLogout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setBackgroundResource(R.drawable.gradient_bg_hover);

					if(NormalPaymentAsyncTask.mCountDownTimer!=null){
						NormalPaymentAsyncTask.mCountDownTimer.cancel();
						NormalPaymentAsyncTask.mCountDownTimer = null;
						Log.i(mTAG,"Timer Stopped Successfully");
					}

					// AsyncTask to call the logout webservice to end the login session
					new LogoutSessionTask(Settings.this,"FromManualLogOut").execute();
				}
			});

			mSettingsHome.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setBackgroundResource(R.drawable.gradient_bg_hover);
					Intent intent_home = new Intent(Settings.this,ShopperHomePage.class);
					Settings.this.finish();
					startActivity(intent_home);
				}
			});

			mLogoutImage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setBackgroundResource(R.drawable.gradient_bg_hover);
					// AsyncTask to call the logout webservice to end the login session
					new LogoutSessionTask(Settings.this,"FromManualLogOut").execute();
				}
			});
		}

		SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
		if(!mPrefs.getString("username", "").equals("")) {
			mUserNameValue.setText(mPrefs.getString("username", "").trim());
		}else{
			mUserNameValue.setText(UserDetails.mServiceEmail);
		}

		mPasswordValue = (EditText) mSettingsLoginLayout.findViewById(R.id.settings_loginPasswordId);
		mSettingsLoginButton = (Button) mSettingsLoginLayout.findViewById(R.id.settings_loginButtonId);
		mEditPasswordBox = (CheckBox) mEditSecurityDetailsContainer.findViewById(R.id.settings_chagepasswordBoxId);
		mEditSecurityQuestionsBox = (CheckBox) mEditSecurityDetailsContainer.findViewById(R.id.settings_chagequestionsBoxId);
		mChangePasswordFields = (LinearLayout) mEditSecurityDetailsContainer.findViewById(R.id.settings_changepassword_items);
		mChangeSecurityQuestionsField = (LinearLayout) mEditSecurityDetailsContainer.findViewById(R.id.settings_security_items); 
		mSettingsSecurityEditButtonLayout = (RelativeLayout) mEditSecurityDetailsContainer.findViewById(R.id.settings_securityEditButtonLayout);
		// Contact info fields
		mContactInfoFields = (LinearLayout) mContactInfoView.findViewById(R.id.settings_contactinfo);
		mUserFirstName = (EditText) mContactInfoFields.findViewById(R.id.edtFirstName);
		mUserLastName = (EditText) mContactInfoFields.findViewById(R.id.edtLastName);
		mUserEmail = (EditText) mContactInfoFields.findViewById(R.id.edtEmail);
		mUserMobileNumber = (EditText) mContactInfoFields.findViewById(R.id.edtMobileNumber);
		mUserMobileNumber.addTextChangedListener(new MobileNumberTextWatcher());
		mUserMobileNumber.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});
		mUserMobileCarrier = (EditText) mContactInfoFields.findViewById(R.id.user_mobilecarrier_value);
		mAddImageLayout = (LinearLayout) mContactInfoFields.findViewById(R.id.settings_layout_addimage);
		mUserAddImageText = (TextView) mContactInfoFields.findViewById(R.id.settings_contactaddimage);
		mUserProfileImage = (ImageView) mContactInfoFields.findViewById(R.id.settings_contactImageId);
		mMobileCarrierImage = (ImageView) mContactInfoFields.findViewById(R.id.user_mobilecarrier_contextmenu);
		mContactInfoSaveButton = (Button) mContactInfoFields.findViewById(R.id.btnContactInfoSave);
		registerForContextMenu(mUserMobileCarrier);

		// Verification pop up layout
		mVerificationPopupLayout = (RelativeLayout) settingscontainer.findViewById(R.id.verificationpopup_container);
		mVerificationCodeValue = (EditText) mVerificationPopupLayout.findViewById(R.id.mobileverificationcodevalue);
		mVerificationOkButton = (Button) mVerificationPopupLayout.findViewById(R.id.popup_verify);
		mVerificationCancelButton = (Button) mVerificationPopupLayout.findViewById(R.id.popup_cancel);

		// Edit Password Fields
		mUserNewPassword = (EditText) mChangePasswordFields.findViewById(R.id.settings_passwordChangeId);
		mUserConfirmPassword = (EditText) mChangePasswordFields.findViewById(R.id.settings_confirmPasswordChangeId);

		mUserNewPassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(mUserNewPassword.getText().toString().length()<6&&mEditPasswordBox.isChecked()&&mMenuBarFlag==3){
						alertBox_validation("Password must contain a minimum of 6 characters");
					}	
				}
			}
		});

		mEditSecurityDetailsBackButton = (Button) mSettingsSecurityEditButtonLayout.findViewById(R.id.btnSecurityBack);
		mEditSecurityDetailsSaveButton = (Button) mSettingsSecurityEditButtonLayout.findViewById(R.id.btnSecuritySave);
		// Edit Security Questions Details
		mUserSecurityQuestion1 = (EditText) mChangeSecurityQuestionsField.findViewById(R.id.settings_questions1);
		mUserSecurityQuestion2 = (EditText) mChangeSecurityQuestionsField.findViewById(R.id.settings_questions2);
		mUserSecurityAnswer1 = (EditText) mChangeSecurityQuestionsField.findViewById(R.id.edtAnswer1); 
		mUserSecurityAnswer2 = (EditText) mChangeSecurityQuestionsField.findViewById(R.id.edtAnswer2);
		mSecurityQuestion1Image =  (ImageView) mChangeSecurityQuestionsField.findViewById(R.id.settings_questions1ImageId);
		//mSecurityQuestion2Image =   (ImageView) mChangeSecurityQuestionsField.findViewById(R.id.settings_questions2ImageId);
		registerForContextMenu(mUserSecurityQuestion1);
		registerForContextMenu(mUserSecurityQuestion2);

		// Edit PIN Layout :
		mUserExistingPIN = (EditText) mEditPinLayout.findViewById(R.id.edtExistingPin);

		/**
		 * Below EditText Code to show number keypad in password mode
		 * */
		mUserNewPIN = (EditText) mEditPinLayout.findViewById(R.id.edtNewPin);
		mUserNewPIN.setInputType(InputType.TYPE_CLASS_NUMBER);
		mUserNewPIN.setTransformationMethod(PasswordTransformationMethod.getInstance());	
		mUserReEnterPIN = (EditText) mEditPinLayout.findViewById(R.id.edtReEnterPin);
		mUserReEnterPIN.setInputType(InputType.TYPE_CLASS_NUMBER);
		mUserReEnterPIN.setTransformationMethod(PasswordTransformationMethod.getInstance());

		mUserNewPIN.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					if(mUserNewPIN.getText().toString().length()<4&&mMenuBarFlag==4){
						alertBox_validation("PIN Number should contain 4 numbers");
					}	
				}
			}
		});

		mEditPinSaveButton =  (Button) mEditPinLayout.findViewById(R.id.btnEditPinSave);
		mEditPinBackButton = (Button) mEditPinLayout.findViewById(R.id.btnEditPinBack);
		mUserExistingPINText = (TextView) mEditPinLayout.findViewById(R.id.txtPin);
		//Call function to get width of the screen
		mScreenWidth=getScreenWidth(); 	
		if(mScreenWidth>0){	//To fix Home Page menubar items width
			mSettingsMenuWidth=mScreenWidth/4;
			Log.i(mTAG,"ScreenWidth : "+mScreenWidth+"\n"+"MenuItemWidth : "+mSettingsMenuWidth);
		}
		mNotification.setEnabled(false);
		mContactInfo.setEnabled(false);
		mEditSecurityDetails.setEnabled(false);
		mSecurity.setEnabled(false);

		// Handler class to update UI items
		mHandler = new Handler(){
			public void handleMessage(Message msg) {
				if(msg.obj.equals("yes")){
					mSettingsLoginLayout.setVisibility(View.GONE);
					settingsmenubarcontainer.setVisibility(View.VISIBLE);
					settingsmenubarcontainer.setBackgroundResource(R.drawable.header_2);
					mNotification.setEnabled(true);
					mContactInfo.setEnabled(true);
					mEditSecurityDetails.setEnabled(true);
					mSecurity.setEnabled(true);
					mContactInfoView.setVisibility(View.VISIBLE);
					mContactInfo.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mUserPassword = mPasswordValue.getText().toString();
					mPasswordValue.setText("");
					GetUpdateUserProfileTask mUserDetials = new GetUpdateUserProfileTask(Settings.this,"GetUserInfo",mUserProfileImage,mUserFirstName,mUserLastName,mUserEmail,mUserMobileNumber,mUserMobileCarrier);
					mUserDetials.execute();
				}else if(msg.obj.equals("no")){
					alertBox_validation("Invalid password");
					mPasswordValue.setText("");
				}else{
					alertBox_validation("Unable to Login please try after some time.");
					mPasswordValue.setText("");
				}
			};
		};

		mSettingsLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mConnectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
					if(mPasswordValue.getText().toString().equals("")){
						alertBox_validation("Enter password");
						mPasswordValue.requestFocus();
					}else{
						mProgressDialog.show();
						final Thread syncThread = new Thread(new Runnable() {

							@Override
							public void run() {
								String mGetResponse=null;
								try{
									if(mConnectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
										mGetResponse=mZouponswebservice.login(mUserNameValue.getText().toString().trim(),mEncryption.md5(mPasswordValue.getText().toString()));	//Get login response values from webservice
										if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
											String mParsingResponse=mParsingclass.parseLoginXmlData(mGetResponse,mTAG);
											if(mParsingResponse.equalsIgnoreCase("success")){
												for(int i=0;i<WebServiceStaticArrays.mLoginUserList.size();i++){
													LoginUser_ClassVariables parsedobjectvalues = (LoginUser_ClassVariables) WebServiceStaticArrays.mLoginUserList.get(i);
													if(parsedobjectvalues.mFlag.equals("no")){
														Message failure_message = new Message();
														failure_message.obj=parsedobjectvalues.mFlag;
														mHandler.sendMessage(failure_message);
													}else if(parsedobjectvalues.mFlag.equals("yes") ){
														if(UserDetails.mServiceUserId.equalsIgnoreCase(parsedobjectvalues.userID)){
															Message success_message = new Message();
															success_message.obj=parsedobjectvalues.mFlag;
															mHandler.sendMessage(success_message);
														}else{
															Message failure_message = new Message();
															failure_message.obj="no";
															mHandler.sendMessage(failure_message);
														}
													}
												}
											}else{
												Message failure_message = new Message();
												failure_message.obj="failure";
												mHandler.sendMessage(failure_message);	//send update to handler
											}
										}else{
											Message failure_message = new Message();
											failure_message.obj="failure";
											mHandler.sendMessage(failure_message);	//send update to handler about login response
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
						//startActivity(new Intent(ZouponsLogin.this,ShopperHomePage.class));
					}
				}else{
					Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
					mUserNameValue.requestFocus();
				}

			}
		});

		mAddImageLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try{
					contextMenuOpen(mAddImageLayout);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		mContactInfoSaveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mUserFirstName.getText().toString().trim().equalsIgnoreCase("")){
					alertBox_validation("Please enter first name");
					mUserFirstName.requestFocus();
				}else if(mUserLastName.getText().toString().trim().equalsIgnoreCase("")){
					alertBox_validation("Please enter last name");
					mUserLastName.requestFocus();
				}else if(mUserMobileNumber.getText().toString().trim().equalsIgnoreCase("")){
					alertBox_validation("Please enter mobile number");
					mUserMobileNumber.requestFocus();
				}else if(mUserMobileNumber.getText().toString().length()!=12){
					alertBox_validation("Please enter valid mobile number");
					mUserMobileNumber.requestFocus();
				}else{
					// To remove hipen while sending mobile number to webservice
					String mobilenumber = mUserMobileNumber.getText().toString().replaceAll("-", "");
					GetUpdateUserProfileTask mUserDetials = new GetUpdateUserProfileTask(Settings.this,mUserFirstName.getText().toString(), mUserLastName.getText().toString(),mobilenumber, /*mUserMobileCarrier.getText().toString()*/mMobileCarrierId, mProfilePhoto, "UpdateUserInfo","update","",mContactInfoView,mVerificationPopupLayout,mVerificationCodeValue);
					mUserDetials.execute();
				}
			}
		});

		mEditPasswordBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked){
					mUserNewPassword.getText().clear();
					mUserConfirmPassword.getText().clear();
					if(mEditSecurityQuestionsBox.isChecked()){
						mEditSecurityQuestionsBox.setChecked(false);
					}
					if(mChangePasswordFields.getVisibility() == View.GONE){
						mChangePasswordFields.setVisibility(View.VISIBLE);
						mSettingsSecurityEditButtonLayout.setVisibility(View.VISIBLE);
					}
					mChangeSecurityQuestionsField.setVisibility(View.GONE);
				}else{
					if(!mEditSecurityQuestionsBox.isChecked() && mChangePasswordFields.getVisibility() == View.VISIBLE){
						mEditPasswordBox.setOnCheckedChangeListener(null);
						mEditPasswordBox.setChecked(true);
						mEditPasswordBox.setOnCheckedChangeListener(this);
					}
				}
			}
		});

		mVerificationOkButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mVerificationCodeValue.getText().toString().trim().length() == 0){
					alertBox_validation("Please enter verification code");
				}else{
					String mobilenumber = mUserMobileNumber.getText().toString().replaceAll("-", ""); 
					GetUpdateUserProfileTask mUserDetials = new GetUpdateUserProfileTask(Settings.this,mUserFirstName.getText().toString(), mUserLastName.getText().toString(),mobilenumber, /*mUserMobileCarrier.getText().toString()*/mMobileCarrierId, mProfilePhoto, "UpdateUserInfo","verify",mVerificationCodeValue.getText().toString().trim(),mContactInfoView,mVerificationPopupLayout,mVerificationCodeValue);
					mUserDetials.execute();
				}
			}
		});

		mVerificationCancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mVerificationPopupLayout.setVisibility(View.GONE);
				mContactInfoView.setVisibility(View.VISIBLE);
			}
		});

		mEditSecurityQuestionsBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//Function to hide keyboard
				new KeyboardUtil().hideKeyboard(Settings.this);
				if(isChecked){
					mUserSecurityQuestion1.getText().clear();
					mUserSecurityQuestion2.getText().clear();
					mUserSecurityAnswer1.getText().clear();
					mUserSecurityAnswer2.getText().clear();
					if(mEditPasswordBox.isChecked()){
						mEditPasswordBox.setChecked(false);

					}
					if(mChangeSecurityQuestionsField.getVisibility() == View.GONE){
						mChangeSecurityQuestionsField.setVisibility(View.VISIBLE);
						mSettingsSecurityEditButtonLayout.setVisibility(View.VISIBLE);
					}
					mChangePasswordFields.setVisibility(View.GONE);
					EditSecurityDetailsTask mSecurityTask = new EditSecurityDetailsTask(Settings.this,mUserSecurityQuestion1,mUserSecurityQuestion2,mUserSecurityAnswer1,mUserSecurityAnswer2);
					mSecurityTask.execute("GetSecurityQuestions");
				}else{
					if(!mEditPasswordBox.isChecked() && mChangeSecurityQuestionsField.getVisibility() == View.VISIBLE){
						mEditSecurityQuestionsBox.setOnCheckedChangeListener(null);
						mEditSecurityQuestionsBox.setChecked(true);
						mEditSecurityQuestionsBox.setOnCheckedChangeListener(this);
					}
				}
			}
		});

		mEditSecurityDetailsBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mNotification.setBackgroundResource(R.drawable.header_2);
				mContactInfo.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mEditSecurityDetails.setBackgroundResource(R.drawable.header_2);
				mSecurity.setBackgroundResource(R.drawable.header_2);

				if(mContactInfoView.getVisibility()==View.INVISIBLE||mContactInfoView.getVisibility()==View.GONE){
					mContactInfoView.setVisibility(View.VISIBLE);
				}
				mEditPINDetailsContainer.setVisibility(View.GONE);
				mEditSecurityDetailsContainer.setVisibility(View.GONE);	
			}
		});

		mEditSecurityDetailsSaveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//Function to close keyboard
				new KeyboardUtil().hideKeyboard(Settings.this);

				if(mChangePasswordFields.getVisibility() == View.VISIBLE){ // For changing Password
					if(mUserNewPassword.getText().toString().equalsIgnoreCase("")){
						alertBox_validation("Enter new password");
						mUserNewPassword.requestFocus();
					}else if(mUserNewPassword.getText().toString().length()<6&&mEditPasswordBox.isChecked()&&mMenuBarFlag==3){
						mUserNewPassword.requestFocus();
						alertBox_validation("Password must contain a minimum of 6 characters");
					}else if(mUserConfirmPassword.getText().toString().equalsIgnoreCase("")){
						alertBox_validation("Confirm password");
						mUserConfirmPassword.requestFocus();
					}else if(!mUserNewPassword.getText().toString().equalsIgnoreCase(mUserConfirmPassword.getText().toString())){
						alertBox_validation("The password you typed does not matched");
					}else{
						EditSecurityDetailsTask mSecurityTask = new EditSecurityDetailsTask(Settings.this,mContactInfo,mEditSecurityDetails,mContactInfoView,mEditSecurityDetailsContainer);
						mSecurityTask.execute("ChangePassword",mUserPassword,mUserConfirmPassword.getText().toString());
					}
				}else{ // For security questions
					if(mSqId1.equalsIgnoreCase("")){
						alertBox_validation("Select Security Question");
					}else if(mUserSecurityAnswer1.getText().toString().trim().equalsIgnoreCase("")){
						alertBox_validation("Enter answer to security question");
						mUserSecurityAnswer1.requestFocus();
					}else{
						EditSecurityDetailsTask mSecurityTask = new EditSecurityDetailsTask(Settings.this,mContactInfo,mEditSecurityDetails,mContactInfoView,mEditSecurityDetailsContainer);
						mSecurityTask.execute("ChangeSecurityQuestions",mSqId1,mSqId2,mUserSecurityAnswer1.getText().toString(),mUserSecurityAnswer1.getText().toString());
					}
				}
			}
		});

		mUserSecurityQuestion1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openSecurityDialog(1);
			}
		});

		mSecurityQuestion1Image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openSecurityDialog(1);
			}
		});

		mUserSecurityQuestion2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openSecurityDialog(2);
			}
		});

		mEditPinBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mNotification.setBackgroundResource(R.drawable.header_2);
				mContactInfo.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mEditSecurityDetails.setBackgroundResource(R.drawable.header_2);
				mSecurity.setBackgroundResource(R.drawable.header_2);
				if(mContactInfoView.getVisibility()==View.INVISIBLE||mContactInfoView.getVisibility()==View.GONE){
					mContactInfoView.setVisibility(View.VISIBLE);
				}
				mEditPINDetailsContainer.setVisibility(View.GONE);
				mEditSecurityDetailsContainer.setVisibility(View.GONE);	
			}
		});

		mEditPinSaveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mUserNewPIN.getText().toString().trim().equalsIgnoreCase("")){
					alertBox_validation("Please enter new PIN Number");
					mUserNewPIN.requestFocus();
				}else if(mUserNewPIN.getText().toString().length()<4&&mMenuBarFlag==4){
					alertBox_validation("PIN Number should contain 4 numbers");
				}else if(mUserReEnterPIN.getText().toString().trim().equalsIgnoreCase("")){
					alertBox_validation("Please re-enter PIN Number");
					mUserReEnterPIN.requestFocus();
				}else if(!mUserNewPIN.getText().toString().equalsIgnoreCase(mUserReEnterPIN.getText().toString())){
					alertBox_validation("The PIN Number you typed do not match");
				}else{				
					EditPinAsynchThread mEditPinTask = new EditPinAsynchThread(Settings.this, mUserNewPIN, mUserReEnterPIN,true,mContactInfo,mSecurity,mContactInfoView,mEditPINDetailsContainer);
					mEditPinTask.execute(mUserNewPIN.getText().toString());
				}
			}
		});

		mNotification.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mMenuBarFlag=1;
				startActivity(new Intent(Settings.this,NotificationSettings.class));
			}
		});

		mContactInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mMenuBarFlag=2;
				mNotification.setBackgroundResource(R.drawable.header_2);
				mContactInfo.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mEditSecurityDetails.setBackgroundResource(R.drawable.header_2);
				mSecurity.setBackgroundResource(R.drawable.header_2);
				if(mContactInfoView.getVisibility()==View.INVISIBLE||mContactInfoView.getVisibility()==View.GONE){
					mContactInfoView.setVisibility(View.VISIBLE);
				}
				mEditPINDetailsContainer.setVisibility(View.GONE);
				mEditSecurityDetailsContainer.setVisibility(View.GONE);
			}
		});

		mEditSecurityDetails.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mMenuBarFlag=3;
				mNotification.setBackgroundResource(R.drawable.header_2);
				mContactInfo.setBackgroundResource(R.drawable.header_2);
				mEditSecurityDetails.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mSecurity.setBackgroundResource(R.drawable.header_2);
				if(mEditSecurityDetailsContainer.getVisibility()==View.INVISIBLE||mEditSecurityDetailsContainer.getVisibility()==View.GONE){
					mEditSecurityDetailsContainer.setVisibility(View.VISIBLE);
					mEditSecurityDetailsContainer.bringToFront();
				}
				mChangePasswordFields.setVisibility(View.GONE);
				mChangeSecurityQuestionsField.setVisibility(View.GONE);
				mSettingsSecurityEditButtonLayout.setVisibility(View.GONE);
				mContactInfoView.setVisibility(View.GONE);
				mEditPINDetailsContainer.setVisibility(View.GONE);
				mEditPasswordBox.setChecked(false);
				mEditSecurityQuestionsBox.setChecked(false);
			}
		});

		mSecurity.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mMenuBarFlag=4;
				mNotification.setBackgroundResource(R.drawable.header_2);
				mContactInfo.setBackgroundResource(R.drawable.header_2);
				mEditSecurityDetails.setBackgroundResource(R.drawable.header_2);
				mSecurity.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mContactInfoView.setVisibility(View.GONE);
				mEditSecurityDetailsContainer.setVisibility(View.GONE);
				mUserExistingPIN.getText().clear();
				mUserNewPIN.getText().clear();
				mUserReEnterPIN.getText().clear();

				if(mEditPINDetailsContainer.getVisibility()==View.INVISIBLE||mEditPINDetailsContainer.getVisibility()==View.GONE){
					mEditPINDetailsContainer.setVisibility(View.VISIBLE);
					mUserExistingPIN.setVisibility(View.GONE);
					mUserExistingPINText.setVisibility(View.GONE);
				}
			}
		});

		mUserMobileCarrier.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openContextMenu(mMobileCarrierImage);
			}
		});

	}

	private void contextMenuOpen(View sender){
		sender.setLongClickable(false);
		registerForContextMenu(sender);
		openContextMenu(sender);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100 && resultCode == RESULT_OK){
			setScaledBitmap(data);
		}else if(requestCode == 200 && resultCode == RESULT_OK){
			setScaledBitmap(data);
		}else{
			if(mUserProfileImage.getVisibility()==View.VISIBLE){
				mUserProfileImage.setVisibility(View.VISIBLE);
				mUserAddImageText.setVisibility(View.VISIBLE);
			}
		}
	}

	// To scale Bitmap to required size and set the same
	public void setScaledBitmap(Intent data){
		try{
			Uri uri=data.getData();
			// specifying column(data) for retrieval
			String[] file_path_columns={MediaStore.Images.Media.DATA};
			// querying content provider to get particular image
			Cursor cursor=getContentResolver().query(uri, file_path_columns, null, null, null);
			cursor.moveToFirst();
			// getting col_index from string file_path_columns[0]--> Data column 
			int column_index=cursor.getColumnIndex(file_path_columns[0]);
			// getting the path from result as /sdcard/DCIM/100ANDRO/file_name
			String selected_file_path=cursor.getString(column_index);
			cursor.close();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(selected_file_path, options);
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, mUserProfileImage.getWidth(), mUserProfileImage.getHeight());
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap mSelectedImage = BitmapFactory.decodeFile(selected_file_path, options);
			mSelectedImage = new DecodeImageWithRotation().decodeImage(selected_file_path, mSelectedImage, mUserProfileImage.getWidth(), mUserProfileImage.getHeight());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);    
			byte[] imagedata = baos.toByteArray();
			mProfilePhoto = com.us.zoupons.Base64.encodeBytes(imagedata);
			//mSelectedImage=Bitmap.createScaledBitmap(mSelectedImage, mSetImageWidth, mSetImageHeight, true);
			BitmapDrawable drawable = new BitmapDrawable(mSelectedImage);
			if(mUserProfileImage.getVisibility()==View.VISIBLE){
				mUserProfileImage.setVisibility(View.GONE);
				mUserAddImageText.setVisibility(View.GONE);
			}
			mAddImageLayout.setBackgroundDrawable(drawable);
		}catch (Exception e) {
			e.printStackTrace();
			if(mUserProfileImage.getVisibility()==View.VISIBLE){
				mUserProfileImage.setVisibility(View.VISIBLE);
				mUserAddImageText.setVisibility(View.VISIBLE);
			}
			Toast.makeText(getApplicationContext(), "Please select valid image.", Toast.LENGTH_SHORT).show();
		}
	}

	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if(v.equals(mMobileCarrierImage)==true){
			menu.setHeaderTitle("Mobile Carriers");
			for(int i=0;i<WebServiceStaticArrays.mMobileCarriersList.size();i++){
				MobileCarriers_ClassVariables obj = (MobileCarriers_ClassVariables) WebServiceStaticArrays.mMobileCarriersList.get(i);
				menu.add(0, Integer.parseInt(obj.mId), 0, obj.mName);
			}
		}else if(v.equals(mUserSecurityQuestion1)==true){
			securityQuestions(menu,v,1);
		}else if(v.equals(mUserSecurityQuestion2)==true){	
			securityQuestions(menu,v,2);
		}else if(v.equals(mAddImageLayout) == true){
			menu.setHeaderTitle("Choose From");
			menu.add(3, 1, 0, "Take Picture");
			menu.add(3, 2, 1, "Gallery");
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	private void securityQuestions(ContextMenu menu,View v,int groupid){
		menu.setHeaderTitle("Security Questions");
		if(groupid==1){
			for(int i=0;i<WebServiceStaticArrays.mSecurityQuestionsList.size();i++){
				SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(i);
				if(obj.mIsSelected.equals("no")){
					menu.add(1, v.getId(), 0, obj.mSecurityQuestions_securityquestion);
				}
			}
		}else if(groupid==2){
			for(int i=0;i<WebServiceStaticArrays.mSecurityQuestionsList.size();i++){
				SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(i);
				if(obj.mIsSelected.equals("no")){
					menu.add(2, v.getId(), 0, obj.mSecurityQuestions_securityquestion);
				}
			}
		}
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getGroupId()){
		case 0:
			mMobileCarrierId=String.valueOf(item.getItemId());
			mUserMobileCarrier.setText(item.getTitle());
			return true;
		case 1:
			String selectedquestion1 = mUserSecurityQuestion1.getText().toString().trim();
			for(int j=0;j<WebServiceStaticArrays.mSecurityQuestionsList.size();j++){
				SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(j);

				if(selectedquestion1.equals(obj.mSecurityQuestions_securityquestion)){
					obj.mIsSelected="no";
					WebServiceStaticArrays.mSecurityQuestionsList.set(j, obj);
				}

				if(item.getTitle().equals(obj.mSecurityQuestions_securityquestion)){
					obj.mIsSelected="yes";
					mUserSecurityQuestion1.setText(item.getTitle());
					mSqId1=obj.mSecurityQuestios_securityquestionid;
					WebServiceStaticArrays.mSecurityQuestionsList.set(j, obj);
				}
			}
			return true;
		case 2:
			String selectedquestion2 = mUserSecurityQuestion2.getText().toString().trim();
			for(int k=0;k<WebServiceStaticArrays.mSecurityQuestionsList.size();k++){
				SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(k);

				if(selectedquestion2.equals(obj.mSecurityQuestions_securityquestion)){
					obj.mIsSelected="no";
					WebServiceStaticArrays.mSecurityQuestionsList.set(k, obj);
				}

				if(item.getTitle().equals(obj.mSecurityQuestions_securityquestion)){
					obj.mIsSelected="yes";
					mUserSecurityQuestion2.setText(item.getTitle());
					mSqId2=obj.mSecurityQuestios_securityquestionid;
					WebServiceStaticArrays.mSecurityQuestionsList.set(k,obj);
				}
			}
			return true;
		case 3:
			if(item.getItemId() == 1){
				Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent_camera, 200);
			}else if(item.getItemId() == 2){
				Intent mGalleryIntent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(mGalleryIntent, 100);
			} 
			return true;
		default :
			return super.onContextItemSelected(item);	
		}
	}

	// Custom Dialog to show security questions
	private void openSecurityDialog(final int position){
		final Dialog mSecurityQuestionsDialog = new Dialog(Settings.this);
		mSecurityQuestionsDialog.setTitle("Security Questions");
		mSecurityQuestionsDialog.setContentView(R.layout.card_buybutton_menu);
		ListView mQuestionsList = (ListView) mSecurityQuestionsDialog.findViewById(R.id.lists);
		ArrayList<String> mQuestions = new ArrayList<String>();
		for(int i=0;i<WebServiceStaticArrays.mSecurityQuestionsList.size();i++){
			SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(i);
			if(obj.mIsSelected.equals("no")){
				mQuestions.add(obj.mSecurityQuestions_securityquestion);
			}
		}	 
		mQuestionsList.setAdapter(new ArrayAdapter<String>(Settings.this, R.layout.card_buy_button_dialog, R.id.optiontext,mQuestions));

		mQuestionsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String selectedquestion = "";
				if(position == 1){
					selectedquestion = mUserSecurityQuestion1.getText().toString().trim();	
				}else{
					selectedquestion = mUserSecurityQuestion2.getText().toString().trim();
				}

				for(int j=0;j<WebServiceStaticArrays.mSecurityQuestionsList.size();j++){
					SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(j);

					if(selectedquestion.equals(obj.mSecurityQuestions_securityquestion)){
						obj.mIsSelected="no";
						WebServiceStaticArrays.mSecurityQuestionsList.set(j, obj);
					}

					if(arg0.getAdapter().getItem(arg2).equals(obj.mSecurityQuestions_securityquestion)){
						obj.mIsSelected="yes";
						if(position == 1){
							mUserSecurityQuestion1.setText(arg0.getAdapter().getItem(arg2).toString());
							// To clear previous security Question's answer if available
							mUserSecurityAnswer1.getText().clear();
							mSqId1=obj.mSecurityQuestios_securityquestionid;
						}else{
							mUserSecurityQuestion2.setText(arg0.getAdapter().getItem(arg2).toString());
							// To clear previous security Question's answer if available
							mUserSecurityAnswer2.getText().clear();
							mSqId2=obj.mSecurityQuestios_securityquestionid;
						}
						WebServiceStaticArrays.mSecurityQuestionsList.set(j, obj);
					}
				}
				mSecurityQuestionsDialog.dismiss();
			}
		});
		mSecurityQuestionsDialog.show();
	}

	// Funtion to show alert pop up with respective message
	private void alertBox_validation(final String msg) {
		AlertDialog.Builder validation_alert = new AlertDialog.Builder(this);
		validation_alert.setTitle("Information");
		validation_alert.setMessage(msg);
		validation_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equalsIgnoreCase("Password must contain a minimum of 6 characters")){
					mUserNewPassword.getText().clear();
					mUserNewPassword.requestFocus();
				}else if(msg.equalsIgnoreCase("The passwords you typed do not match")){
					mUserConfirmPassword.getText().clear();
					mUserConfirmPassword.requestFocus();
				}else if(msg.equalsIgnoreCase("PIN Number should contain 4 numbers")){
					mUserNewPIN.getText().clear();
					mUserNewPIN.requestFocus();
				}else if(msg.equalsIgnoreCase("The PIN Number you typed do not match")){
					mUserReEnterPIN.getText().clear();
					mUserReEnterPIN.requestFocus();
				}
			}
		});
		validation_alert.show();
	}



	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's width.
	 */
	class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View menu;
		Button mFreezeView;
		/**
		 * Menu must NOT be out/shown to start with.
		 */

		public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu, Button freezeview) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
			this.mFreezeView=freezeview;
		}

		@Override
		public void onClick(View v) {

			sLeftMenuScrollView.fullScroll(View.FOCUS_UP);
			sLeftMenuScrollView.pageScroll(View.FOCUS_UP);

			Context context = menu.getContext();
			String msg = "Slide " + new Date();
			System.out.println(msg);

			int menuWidth = menu.getMeasuredWidth();
			Log.i(mTAG,"Menu Width : "+ menuWidth);

			// Ensure menu is visible
			menu.setVisibility(View.VISIBLE);

			InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

			if (!MenuOutClass.SETTINGS_MENUOUT) {
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
			MenuOutClass.SETTINGS_MENUOUT = !MenuOutClass.SETTINGS_MENUOUT;
		}
	}

	/**
	 * 
	 * Class to calculate menu size
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
		new RefreshZoupons().isApplicationGoneBackground(Settings.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background

	}

	@Override
	protected void onResume() {
		super.onResume();
		POJOMainMenuActivityTAG.TAG = mTAG;
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		if(getIntent().hasExtra("Store_Owner_Settings")){ 
			mNotificationSync = new ScheduleNotificationSync(Settings.this,ZouponsConstants.sStoreModuleFlag);	
		}else{
			mNotificationSync = new ScheduleNotificationSync(Settings.this,ZouponsConstants.sCustomerModuleFlag);
		}
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(Settings.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(Settings.this);
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
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	public void onRestart() {
		super.onRestart();

	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	protected void onDestroy() {
		super.onDestroy();
		sScrollView = null ; sLeftMenu = null ; sSettingsFreezeView = null;sLeftMenuScrollView = null;
		// To notify  system that its time to run garbage collector service
		System.gc();

	};


}
