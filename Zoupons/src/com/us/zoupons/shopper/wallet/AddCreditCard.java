/**
 * 
 */
package com.us.zoupons.shopper.wallet;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.DateValidator;
import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.CardId_ClassVariable;
import com.us.zoupons.classvariables.CardOnFiles_ClassVariables;
import com.us.zoupons.classvariables.EditCardDetails_ClassVariables;
import com.us.zoupons.classvariables.FirstDataGlobalpayment_ClassVariables;
import com.us.zoupons.classvariables.ManageCardAddPin_ClassVariables;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.AccountLoginFlag;
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
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

/**
 * Class to add credit card information of zoupons customer
 */
public class AddCreditCard extends Activity implements TextWatcher {
	
	// Initialization of views and variables
	private String mTAG = "AddCreditCard",mUserID,mAlertMsg,mNonZouponsMemberCardName = "",	mNonZouponsMemberCardMask = "", mNonZouponsMemberCardCVV = "",
			mNonZouponsMemberCardexpiryYear = "",mNonZouponsMemberCardexpiryMonth = "",mNonZouponsMemberStreetNumber = "", mNonZouponsMemberZipcode = "",mMessage;
	private MyHorizontalScrollView mScrollView;
	private View mLeftMenu,mApp;
	private Button mAddCardFreezeView,mSkip,mNotificationCount, mScanButton;
	private ImageView mBtnSlideImage,mLogoutImage, mNotificationImage, mCalloutTriangleImage,mHeaderHomeImage,mTabBarLoginChoice;
	private RelativeLayout mBtnLogout;
	private ViewGroup mTabBarContainer;
	//LeftMenu Items Initialization
	private LinearLayout mAddCardHome, mAddCardZpay,mAddCardInvoiceCenter, mAddCardGiftCards, mAddCardManageCards,mAddCardReceipts, mAddCardMyFavourites, mAddCardMyFriends,
	mAddCardChat, mAddCardRewards, mAddCardSettings, mAddCardLogout,mAddCardMenuBar, mAddCardBack,mSave;
	private TextView mAddCardHomeText, mAddCardZpayText,mAddCardInvoiceCenterText, mAddCardManageCardsText,mAddCardReceiptsText, mAddCardGiftCardsText,mAddCardMyFavoritesText, mAddCardMyFriendsText, mAddCardChatText,
	mAddCardRewardsText, mAddCardSettingsText, mAddCardLogoutText,mCardNo, mExpirationDate,mCVVNo, mStreetNumber,mZipCode;
	private Typeface mZouponsFont;
	private EditText mCardNoValue1,mCardNoValue2, mCardNoValue3, mCardNoValue4, mCVVNoValue,mStreetNumberValue, mZipCodeValue,mExpirationDateValue;
	private ScheduleNotificationSync mNotificationSync;
	private int mValidateFlag = 0,mChoosedMonth = 0,mMenuFlag;
	private char mValidateResult;
	private ProgressDialog mProgressDialog = null;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	//For store owner header variables declaration
	private StoreOwner_LeftMenu mStoreowner_leftmenu;
	private StoreOwner_LeftMenu mStoreowner_pointofsale_leftmenu;
	private Header mZouponsHeader;
	// Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	private ScrollView mLeftMenuScrollView;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Initializing parent scroll view and its children
		LayoutInflater inflater = LayoutInflater.from(this);
		mScrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(mScrollView);
		mApp = inflater.inflate(R.layout.addcardinformation, null);
		mAddCardFreezeView = (Button) mApp.findViewById(R.id.addcard_freezeview);
        // Differentiating customer and store module and initializing respective views.
		if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_Billing")||getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_PointOfSale")){ // for store_owner module header initialization
			if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_Billing")){
				mStoreowner_leftmenu = new StoreOwner_LeftMenu(AddCreditCard.this,mScrollView, mLeftMenu, mMenuFlag=1, mAddCardFreezeView, mTAG);
				mLeftMenu = mStoreowner_leftmenu.intializeInflater();
				mStoreowner_leftmenu.clickListener(mLeftMenu);
			}else{
				mStoreowner_pointofsale_leftmenu = new StoreOwner_LeftMenu(AddCreditCard.this,mScrollView, mLeftMenu, mMenuFlag=1, mAddCardFreezeView, mTAG);
				mLeftMenu = mStoreowner_pointofsale_leftmenu.intializeInflater();
				mStoreowner_pointofsale_leftmenu.clickListener(mLeftMenu);
			}
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsHeader = (Header) mApp.findViewById(R.id.storeowneraddcard_header);
			mZouponsHeader.intializeInflater(mScrollView, mLeftMenu, mMenuFlag=1, mAddCardFreezeView, mTAG);
			mZouponsHeader.mTabBarNotificationContainer.setVisibility(View.VISIBLE);
			final View[] children = new View[] { mLeftMenu, mApp };
			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsHeader.mLeftMenuBtnSlide));
			mAddCardFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mAddCardFreezeView, mTAG));
			mNotificationReceiver = new NotifitcationReceiver(mZouponsHeader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(AddCreditCard.this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(AddCreditCard.this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
		}else{ // for customer module header initialisation...
			mLeftMenu = inflater.inflate(R.layout.shopper_leftmenu, null);
			ViewGroup leftMenuItems = (ViewGroup) mLeftMenu.findViewById(R.id.menuitems);
			mTabBarContainer = (ViewGroup) mApp.findViewById(R.id.addcard_tabBar);
			mLeftMenuScrollView = (ScrollView) mLeftMenu.findViewById(R.id.leftmenu_scrollview);
			mLeftMenuScrollView.fullScroll(View.FOCUS_UP);
			mLeftMenuScrollView.pageScroll(View.FOCUS_UP);
			mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);	//code for hiding the keyboard while the activity is loading.//LeftMenu
			mAddCardHome = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_home);
			mAddCardZpay = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zpay);
			mAddCardInvoiceCenter = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_invoicecenter);
			mAddCardManageCards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_managecards);
			mAddCardReceipts = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_receipts);
			mAddCardGiftCards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zgiftcards);
			mAddCardMyFavourites = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfavourites);
			mAddCardMyFriends = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfriends);
			mAddCardChat = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_customercenter);
			mAddCardRewards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_rewards);
			mAddCardSettings = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_settings);
			mAddCardLogout = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_logout);
			mAddCardHomeText = (TextView) leftMenuItems.findViewById(R.id.menuHome);
			mAddCardHomeText.setTypeface(mZouponsFont);
			mAddCardZpayText = (TextView) leftMenuItems.findViewById(R.id.menuZpay);
			mAddCardZpayText.setTypeface(mZouponsFont);
			mAddCardInvoiceCenterText = (TextView) leftMenuItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
			mAddCardInvoiceCenterText.setTypeface(mZouponsFont);
			mAddCardManageCardsText = (TextView) leftMenuItems.findViewById(R.id.menuManageCards);
			mAddCardManageCardsText.setTypeface(mZouponsFont);
			mAddCardReceiptsText = (TextView) leftMenuItems.findViewById(R.id.menuReceipts);
			mAddCardReceiptsText.setTypeface(mZouponsFont);
			mAddCardGiftCardsText = (TextView) leftMenuItems.findViewById(R.id.menuGiftCards);
			mAddCardGiftCardsText.setTypeface(mZouponsFont);
			mAddCardMyFavoritesText = (TextView) leftMenuItems.findViewById(R.id.menufavorites);
			mAddCardMyFavoritesText.setTypeface(mZouponsFont);
			mAddCardMyFriendsText = (TextView) leftMenuItems.findViewById(R.id.menuMyFriends);
			mAddCardMyFriendsText.setTypeface(mZouponsFont);
			mAddCardChatText = (TextView) leftMenuItems.findViewById(R.id.menuCustomerCenter);
			mAddCardChatText.setTypeface(mZouponsFont);
			mAddCardRewardsText = (TextView) leftMenuItems.findViewById(R.id.menuRewards);
			mAddCardRewardsText.setTypeface(mZouponsFont);
			mAddCardSettingsText = (TextView) leftMenuItems.findViewById(R.id.menuSettings);
			mAddCardSettingsText.setTypeface(mZouponsFont);
			mAddCardLogoutText = (TextView) leftMenuItems.findViewById(R.id.menuLogout);
			mAddCardLogoutText.setTypeface(mZouponsFont);
			mBtnSlideImage = (ImageView) mTabBarContainer.findViewById(R.id.BtnSlide);
			mBtnSlideImage.setOnClickListener(new ClickListenerForScrolling(mScrollView, mLeftMenu, mAddCardFreezeView));
			mBtnLogout = (RelativeLayout) mTabBarContainer.findViewById(R.id.zoupons_logout_container);
			mLogoutImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_logout_btn);
			mNotificationImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_notificationImageId);
			mHeaderHomeImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_home);
			mHeaderHomeImage.setOnClickListener(new HeaderHomeClickListener(AddCreditCard.this));//ClickListener for Header Home image
			mCalloutTriangleImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_callout_triangle);
			mNotificationCount = (Button) mBtnLogout.findViewById(R.id.zoupons_notification_count);
			mTabBarLoginChoice = (ImageView) mBtnLogout.findViewById(R.id.store_header_loginchoice);
			new LoginChoiceTabBarImage(AddCreditCard.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
			// Notitification pop up layout declaration
			mNotificationImage.setOnClickListener(new ManageNotificationWindow(AddCreditCard.this,mTabBarContainer, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
			mNotificationCount.setOnClickListener(new ManageNotificationWindow(AddCreditCard.this,mTabBarContainer, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
			// Click listener for sLeftmenu items and freeze view.
			mAddCardFreezeView.setOnClickListener(new ClickListenerForScrolling(mScrollView, mLeftMenu, mAddCardFreezeView));
			mAddCardHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, AddCreditCard.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mAddCardZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, AddCreditCard.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mAddCardInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, AddCreditCard.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mAddCardGiftCards.setOnClickListener(new MenuItemClickListener(leftMenuItems,AddCreditCard.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mAddCardManageCards.setOnClickListener(new MenuItemClickListener(leftMenuItems, AddCreditCard.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mAddCardReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, AddCreditCard.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mAddCardMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,AddCreditCard.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mAddCardMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,AddCreditCard.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mAddCardChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,AddCreditCard.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mAddCardRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,AddCreditCard.this,POJOMainMenuActivityTAG.TAG=mTAG));
			mAddCardSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,AddCreditCard.this,POJOMainMenuActivityTAG.TAG=mTAG));
			
			mAddCardLogout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setBackgroundResource(R.drawable.gradient_bg_hover);
					if(NormalPaymentAsyncTask.mCountDownTimer!=null){
						NormalPaymentAsyncTask.mCountDownTimer.cancel();
						NormalPaymentAsyncTask.mCountDownTimer = null;
					}
					new LogoutSessionTask(AddCreditCard.this,"FromManualLogOut").execute();
					
				}
			});
			
			mLogoutImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// AsyncTask to call the logout webservice to end the login session
					new LogoutSessionTask(AddCreditCard.this,"FromManualLogOut").execute();
				}
			});
			final View[] children = new View[] { mLeftMenu, mApp};
			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(mBtnSlideImage));
			mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
		}
        // Initializing progress dialog for webservice calls
		mProgressDialog=new ProgressDialog(this);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
		mZouponswebservice = new ZouponsWebService(AddCreditCard.this);
		mParsingclass = new ZouponsParsingClass(getApplicationContext());
		mConnectionAvailabilityChecking = new NetworkCheck();
		mSkip=(Button) mApp.findViewById(R.id.addcardinformation_skip);
		// Condition check for skip button visibility
		if(ManageCardAddPin_ClassVariables.mAddPinFlag.equals("true")||ManageCardAddPin_ClassVariables.mEditCardFlag.equals("true"))			//Condition True when page load from managecards
			mSkip.setVisibility(View.GONE);
		mSave=(LinearLayout) mApp.findViewById(R.id.addcardinformation_save);
		mCardNo=(TextView) mApp.findViewById(R.id.addcardinformation_cardno);
		mCardNo.setTypeface(mZouponsFont);
		mExpirationDate=(TextView) mApp.findViewById(R.id.addcardinformation_expirationdate);
		mExpirationDate.setTypeface(mZouponsFont);
		mCVVNo=(TextView) mApp.findViewById(R.id.addcardinformation_cvvno);
		mCVVNo.setTypeface(mZouponsFont);
		mStreetNumber=(TextView) mApp.findViewById(R.id.addcardinformation_streetnumber);
		mStreetNumber.setTypeface(mZouponsFont);
		mZipCode=(TextView) mApp.findViewById(R.id.addcardinformation_zipcode);
		mZipCode.setTypeface(mZouponsFont);
		mCardNoValue1=(EditText) mApp.findViewById(R.id.addcardinformation_card_value1);
		mCardNoValue2=(EditText) mApp.findViewById(R.id.addcardinformation_card_value2);
		mCardNoValue3=(EditText) mApp.findViewById(R.id.addcardinformation_card_value3);
		mCardNoValue4=(EditText) mApp.findViewById(R.id.addcardinformation_card_value4);
		mCardNoValue4.setNextFocusDownId(R.id.addcardinformation_expirationdate_value);
		mExpirationDateValue = (EditText) mApp.findViewById(R.id.addcardinformation_expirationdate_value);
		mExpirationDateValue.setNextFocusDownId(R.id.addcardinformation_cvvno_value);
		mCVVNoValue=(EditText) mApp.findViewById(R.id.addcardinformation_cvvno_value);
		mCVVNoValue.setNextFocusDownId(R.id.addcardinformation_streetnumber_value);
		mStreetNumberValue = (EditText) mApp.findViewById(R.id.addcardinformation_streetnumber_value);
		mStreetNumberValue.setNextFocusDownId(R.id.addcardinformation_zipcode_value);
		mZipCodeValue=(EditText) mApp.findViewById(R.id.addcardinformation_zipcode_value);
		mAddCardBack=(LinearLayout) mApp.findViewById(R.id.addcardinformation_menubar_back);
		mAddCardMenuBar=(LinearLayout) mApp.findViewById(R.id.addcardinformation_menubarcontainer);
		mScanButton = (Button) mApp.findViewById(R.id.scan_card_numberButton);
		// Condition to check we have to show skip buttton
		if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("ManageWallets")||getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_Billing")||getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_PointOfSale")||getIntent().getExtras().getString("class_name").equalsIgnoreCase("MobilePay")){
			mAddCardMenuBar.setVisibility(View.VISIBLE);
			mSkip.setVisibility(View.GONE);
			if(mTabBarContainer!= null){
				mTabBarContainer.setVisibility(View.VISIBLE);	
			}else{
				mZouponsHeader.setVisibility(View.VISIBLE);
			}
		}

		if(!getIntent().getExtras().getString("class_name").equalsIgnoreCase("MobilePay")){
			if((ManageCardAddPin_ClassVariables.mAddPinFlag.equals("true")||ManageCardAddPin_ClassVariables.mEditCardFlag.equals("true"))){
				// Intent from ManageWallets
				mAddCardMenuBar.setVisibility(View.VISIBLE);
			}	
		}else{ // Intent from zpay step2 -- remove skip button and show back menu bar.
			mSkip.setVisibility(View.GONE);
			mAddCardMenuBar.setVisibility(View.VISIBLE);
		}

		mExpirationDateValue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mExpirationDateValue.setSelection(mExpirationDateValue.getText().toString().length());	
			}
		});
				
		mAddCardBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("ManageWallets")){
					Intent intent_managecards = new Intent(getApplicationContext(),ManageWallets.class);
					intent_managecards.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent_managecards);
				}else if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_Billing")){
					Intent intent_managecards = new Intent(getApplicationContext(),ManageWallets.class);
					intent_managecards.putExtra("FromStoreOwnerBilling", true);
					intent_managecards.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent_managecards);
				}else if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_PointOfSale")){
					finish();
				}else if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("MobilePay")){
					finish();
				}
			}
		});

		mCardNoValue1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()==4){
					mCardNoValue2.requestFocus(View.FOCUS_DOWN);
				}
			}
		});

		mCardNoValue2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()==4){
					mCardNoValue3.requestFocus(View.FOCUS_DOWN);
				}
			}
		});

		mCardNoValue3.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()==4){
					mCardNoValue4.requestFocus(View.FOCUS_DOWN);
				}
			}
		});

		mCardNoValue4.addTextChangedListener(this);

		mSkip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AddCreditCard.this,ShopperHomePage.class));
			}
		});

		mExpirationDateValue.addTextChangedListener(new DateValidator(AddCreditCard.this, mExpirationDateValue,mCVVNoValue));

		mExpirationDateValue.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(mExpirationDateValue.getText().toString().length() != 7){
					mExpirationDateValue.setTextColor(getResources().getColor(R.color.red));
					mExpirationDateValue.setTag("invalid");
				}
			}
		});

		if(ManageCardAddPin_ClassVariables.mEditCardFlag.equals("true")){
			try{
				mScanButton.setVisibility(View.GONE);
				String[] splittedCardValue=null;
				if(!EditCardDetails_ClassVariables.cardNumber.equals("")){
					if(EditCardDetails_ClassVariables.cardNumber.length()==19){
						try{
							splittedCardValue=splitBySplitter(EditCardDetails_ClassVariables.cardNumber);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				try{
					mCardNoValue1.setText(splittedCardValue[0]);
					mCardNoValue2.setText(splittedCardValue[1]);
					mCardNoValue3.setText(splittedCardValue[2]);
					mCardNoValue4.setText(splittedCardValue[3]);
				}catch(Exception e){
					e.printStackTrace();
				}
				mCardNoValue1.setEnabled(false);
				mCardNoValue1.setBackgroundColor(getResources().getColor(R.color.searchbordercolor));
				mCardNoValue2.setEnabled(false);
				mCardNoValue2.setBackgroundColor(getResources().getColor(R.color.searchbordercolor));
				mCardNoValue3.setEnabled(false);
				mCardNoValue3.setBackgroundColor(getResources().getColor(R.color.searchbordercolor));
				mCardNoValue4.setEnabled(false);
				mCardNoValue4.setBackgroundColor(getResources().getColor(R.color.searchbordercolor));
				mCVVNoValue.setText(EditCardDetails_ClassVariables.cvv);
				mExpirationDateValue.setText(EditCardDetails_ClassVariables.expiryMonth +" / "+EditCardDetails_ClassVariables.expiryYear);
				mStreetNumberValue.setText(EditCardDetails_ClassVariables.StreetAddress);
				mZipCodeValue.setText(EditCardDetails_ClassVariables.zipCode);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			CardId_ClassVariable.cardid="";
			EditCardDetails_ClassVariables.cardName ="";
			EditCardDetails_ClassVariables.cardNumber ="";
			EditCardDetails_ClassVariables.cvv ="";
			EditCardDetails_ClassVariables.expiryYear ="";
			EditCardDetails_ClassVariables.expiryMonth ="";
			EditCardDetails_ClassVariables.StreetAddress ="";
			EditCardDetails_ClassVariables.zipCode ="";
		}

		mScanButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent scanIntent = new Intent(AddCreditCard.this, CardIOActivity.class);
				// required for authentication with card.io
				scanIntent.putExtra(CardIOActivity.EXTRA_APP_TOKEN, "e84db94d4fac49709305dcf04a0fd153");
				// customize these values to suit our needs.
				scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: true
				scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
				scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
				scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false);
				scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, false);
				scanIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivityForResult(scanIntent, 100);
			}
		});

		mSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(mCardNoValue1.getText().toString().trim().length()<4||mCardNoValue2.getText().toString().trim().length()<4
						||mCardNoValue3.getText().toString().trim().length()<4||mCardNoValue4.getText().toString().trim().length()==0){
					setvalidation("Enter 16 digit card number.", 3, 'N');
				}else if(mCardNoValue4.getText().toString().trim().length()!=1&&mCardNoValue4.getText().toString().trim().length()!=3
						&&mCardNoValue4.getText().toString().trim().length()!=4){
					setvalidation("Enter 16 digit card number.", 7, 'N');
				}else if(mExpirationDateValue.getText().toString().equalsIgnoreCase("")){
					setvalidation("Please Enter expiration date", 8, 'N');
				}else if(mExpirationDateValue.getTag().toString().equalsIgnoreCase("invalid")){
					setvalidation("Please Enter valid expiration date", 8, 'N');
				}else if(mStreetNumberValue.getText().toString().trim().equals("")){
					setvalidation("Please Enter Street Number Value", 1, 'N');
				}else if(mZipCodeValue.getText().toString().trim().equals("")){
					setvalidation("Please Enter Zipcode", 15, 'N');
				}else if(mZipCodeValue.getText().toString().trim().length()<5){
					setvalidation("Please Enter Valid Zipcode", 20, 'N');
				}else if(mCVVNoValue.getText().toString().trim().equals("")){
					setvalidation("Please Enter CVV Value", 10, 'N');
				}else if(mCVVNoValue.getText().toString().trim().length()<3){
					setvalidation("Please Enter Correct CVV Value", 11, 'N');
				}else if(checkingDuplicateCard(mCardNoValue4.getText().toString().trim()) && !ManageCardAddPin_ClassVariables.mEditCardFlag.equals("true")){
					setvalidation("Duplicate card number, Anyhow would you like to proceed.", 21, 'N');
				}else{
					mValidateResult='Y';
				}

				if(mValidateResult=='N'){
					alertBox_validation(mAlertMsg, mValidateFlag);
				}else{
					//Calling funtion to save fields
					saveFields();
				}
			}
		});
	}

	private void saveFields() {
		/* Get User ID from signup arraylist */
		final String user_type;
		if (getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_PointOfSale")) { // for store_owner module header initialisation
			mUserID = getIntent().getExtras().getString("user_id");
			user_type = "Customer";
		} else {
			mUserID = UserDetails.mServiceUserId;
			user_type = AccountLoginFlag.accountUserTypeflag;
		}
		mProgressDialog.show();
		final Thread syncThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Bundle bundle = new Bundle();
				String mGetResponse = null;
				Message msg_response = new Message();
				Bundle bundle_pin = new Bundle();
				Message msg_pin = new Message();

				try {
					if (mConnectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())) {
						String CardName = "";
						String UserName = "";
						String CardNumber = mCardNoValue1.getText().toString().trim()+ mCardNoValue2.getText().toString().trim()+ mCardNoValue3.getText().toString().trim()+ mCardNoValue4.getText().toString().trim();
						String ZipCode = mZipCodeValue.getText().toString().trim();
						String StreetAddress = mStreetNumberValue.getText().toString().trim();
						String CvvNumber = mCVVNoValue.getText().toString().trim();
						mChoosedMonth = Integer.parseInt(mExpirationDateValue.getText().toString().substring(0, 2));
						String Month = String.format("%02d", mChoosedMonth);
						String Year = mExpirationDateValue.getText().toString().substring(mExpirationDateValue.getText().length()-2, mExpirationDateValue.getText().length());
						if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_Billing")){
							SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
							String mStoreId = mPrefs.getString("store_id", "");
							mGetResponse = mZouponswebservice.Getfirst_data_global_payment(mUserID, UserName, CardNumber, CvvNumber, Month, Year, StreetAddress, ZipCode, CardName, user_type,mStoreId,true);
						}else{
							mGetResponse = mZouponswebservice.Getfirst_data_global_payment(mUserID, UserName, CardNumber, CvvNumber, Month, Year, StreetAddress, ZipCode, CardName, user_type,"",false);	
						}
						if (!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")) {
							String mParsingResponse = mParsingclass.parsefirstdataglobalpayment(mGetResponse);
							if (mParsingResponse.equalsIgnoreCase("success")) {
								for (int i = 0; i < WebServiceStaticArrays.mFirstDataGlobalPaymentList.size(); i++) {
									FirstDataGlobalpayment_ClassVariables parsedobjectvalues = (FirstDataGlobalpayment_ClassVariables) WebServiceStaticArrays.mFirstDataGlobalPaymentList.get(i);
									mMessage = parsedobjectvalues.mMessage;
									if (parsedobjectvalues.mPin.equalsIgnoreCase("Yes")) {
										bundle_pin.putString("updateui", "yes");
										msg_pin.setData(bundle_pin);
										handler_updateUI.sendMessage(msg_pin);
									}
								}
								updateHandler(bundle, msg_response, mMessage);
							} else if (mParsingResponse.equalsIgnoreCase("failure")) {
								updateHandler(bundle, msg_response, mParsingResponse); // send update to handler
							} else if (mParsingResponse.equalsIgnoreCase("norecords")) {
								updateHandler(bundle, msg_response, mParsingResponse); // send update to handler
							}
						} else {
							updateHandler(bundle, msg_response, mGetResponse); // send update to handler about emailid validation response
						}
					} else {
						Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				mProgressDialog.setProgress(100);
				mProgressDialog.dismiss();
			}
		});
		syncThread.start();
	}

	// Function to clear all fields during adding credit card
	private void clearFields() {
		mCardNoValue1.setText("");
		mCardNoValue1.setHint("-");
		mCardNoValue2.setText("");
		mCardNoValue2.setHint("-");
		mCardNoValue3.setText("");
		mCardNoValue3.setHint("-");
		mCardNoValue4.setText("");
		mCardNoValue4.setHint("-");
		mZipCodeValue.setText("");
		mZipCodeValue.setHint("Zipcode");
		mCVVNoValue.setText("");
		mCVVNoValue.setHint("CVV");
	}

	// Function to clear card number fields 
	private void clearCreditCardFields() {
		mCardNoValue1.setText("");
		mCardNoValue1.setHint("-");
		mCardNoValue2.setText("");
		mCardNoValue2.setHint("-");
		mCardNoValue3.setText("");
		mCardNoValue3.setHint("-");
		mCardNoValue4.setText("");
		mCardNoValue4.setHint("-");
		mCardNoValue1.requestFocus();
	}

	// Function to send message to handler to update UI elements
	private void updateHandler(Bundle bundle, Message msg_response,	String mGetResponse) {
		bundle.putString("returnResponse", mGetResponse);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}
	
	// Handler class to update UI items
	Handler handler_response = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String key = b.getString("returnResponse");
			if (key.trim().equals("success")) {
				clearFields(); // Function to clear all fields
			} else if (key.trim().equals("failure")) {
				alertBox_service("Information", "Response Error");
			} else if (key.trim().equals("noresponse")
					|| key.trim().equals("norecords")) {
				alertBox_service("Information", "No Data Available");
			} else if (key.trim().equals("Successfully Pre Authorized")) {
				// Assigning to use it in edit card details from POS non member editcard
				mNonZouponsMemberCardName = "";
				mNonZouponsMemberCardMask = "****-****-****-"+ mCardNoValue4.getText().toString();
				mNonZouponsMemberCardCVV = mCVVNoValue.getText().toString();
				mChoosedMonth = Integer.parseInt(mExpirationDateValue.getText().toString().substring(0, 2));
				mNonZouponsMemberCardexpiryMonth = String.format("%02d",mChoosedMonth);
				mNonZouponsMemberCardexpiryYear =  mExpirationDateValue.getText().toString().substring(mExpirationDateValue.getText().length()-2, mExpirationDateValue.getText().length());
				mNonZouponsMemberStreetNumber = mStreetNumberValue.getText().toString();
				mNonZouponsMemberZipcode = mZipCodeValue.getText().toString();
				clearFields(); // Function to clear all fields
				alertBox_service("Information", "Successfully Pre Authorized");
			} else {
				clearCreditCardFields(); // Function to clear credit card fields
				alertBox_service("Information", key.toString().trim());
			}
		}
	};

	// Handler class to update UI items
	Handler handler_updateUI = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String key = b.getString("updateui");
			clearCreditCardFields();
			if (key.trim().equals("yes")) {
				//mZouponsPinContainer.setVisibility(View.GONE);
			}
		}
	};
	
	// Funtion to show alert pop up with respective message	
	private void alertBox_service(final String title, final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this);
		service_alert.setTitle(title);
		if (msg.equalsIgnoreCase("Successfully Pre Authorized")) {
			service_alert
			.setMessage("The credit card information is securely saved");
		} else {
			service_alert.setMessage(msg);
		}
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (msg.equals("Successfully Pre Authorized")) {
					if (getIntent().getExtras().getString("class_name").equalsIgnoreCase("ZouponsLogin")|| getIntent().getExtras().getString("class_name").equalsIgnoreCase("Registration")) {
						startActivity(new Intent(AddCreditCard.this,ShopperHomePage.class));
					} else if (getIntent().getExtras().getString("class_name").equalsIgnoreCase("ManageWallets")) {
						startActivity(new Intent(AddCreditCard.this,ManageWallets.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
					} else if (getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_Billing")) {
						Intent intent_rightmenuBilling = new Intent(AddCreditCard.this,ManageWallets.class);
						intent_rightmenuBilling.putExtra("FromStoreOwnerBilling", true);
						intent_rightmenuBilling.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent_rightmenuBilling);
					} else if (getIntent().getExtras().getString("class_name").equalsIgnoreCase("MobilePay")) {
						setResult(RESULT_OK); // to move control to step2 managecards on activity result
						finish();
					} else if (getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_PointOfSale")) {
						if (WebServiceStaticArrays.mFirstDataGlobalPaymentList.size() > 0) {
							FirstDataGlobalpayment_ClassVariables mFirstData_details = (FirstDataGlobalpayment_ClassVariables) WebServiceStaticArrays.mFirstDataGlobalPaymentList.get(0);
							Intent card_intent = new Intent();
							card_intent.putExtra("user_id", getIntent().getExtras().getString("user_id"));
							card_intent.putExtra("card_id",	mFirstData_details.mCardId);
							card_intent.putExtra("card_name",mNonZouponsMemberCardName);
							card_intent.putExtra("card_masknumber",	mNonZouponsMemberCardMask);
							card_intent.putExtra("card_expirymonth", mNonZouponsMemberCardexpiryMonth);
							card_intent.putExtra("card_expiryyear",	mNonZouponsMemberCardexpiryYear);
							card_intent.putExtra("card_cvv", mNonZouponsMemberCardCVV);
							card_intent.putExtra("address", "");
							card_intent.putExtra("zipcode",	mNonZouponsMemberZipcode);
							card_intent.putExtra("streetnumber",mNonZouponsMemberStreetNumber);
							ManageCardAddPin_ClassVariables.mEditCardFlag = "false";
							ManageCardAddPin_ClassVariables.mAddPinFlag = "true";
							setResult(RESULT_OK, card_intent); // to move control to step2 managecards on activity result or  POS part2
						}
						finish();
					}
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}

	// Function to set corresponding validation with message
	private void setvalidation(String msg, int validateflag, char validateresult) {
		mAlertMsg = msg;
		mValidateFlag = validateflag;
		mValidateResult = validateresult;
	}

	// Funtion to show alert pop up with respective message
	private void alertBox_validation(String msg, final int validateflag) {
		AlertDialog.Builder validation_alert = new AlertDialog.Builder(this);
		validation_alert.setTitle("Information");
		validation_alert.setMessage(msg);
		if (validateflag != 21) {
			validation_alert.setNeutralButton("OK",	new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (validateflag != 0) {
						if (validateflag == 1) {
							mStreetNumberValue.requestFocus();
						}else if (validateflag == 3) {
							mCardNoValue1.setText("");
							mCardNoValue2.setText("");
							mCardNoValue3.setText("");
							mCardNoValue4.setText("");
							mCardNoValue1.requestFocus();
						} else if (validateflag == 7) {
							mCardNoValue4.setText("");
							mCardNoValue4.requestFocus();
						} else if (validateflag == 8) {
							mExpirationDateValue.requestFocus();
						} else if (validateflag == 9) {
							mExpirationDateValue.requestFocus();
						} else if (validateflag == 10) {
							mCVVNoValue.requestFocus();
						} else if (validateflag == 11) {
							mCVVNoValue.setText("");
							mCVVNoValue.requestFocus();
						} else if (validateflag == 15) {
							mZipCodeValue.requestFocus();
						} else if (validateflag == 19) {
							mCardNoValue1.getText().clear();
							mCardNoValue2.getText().clear();
							mCardNoValue3.getText().clear();
							mCardNoValue4.getText().clear();
						} else if (validateflag == 20) {
							mZipCodeValue.getText().clear();
							mZipCodeValue.requestFocus();
						}
					}
					dialog.dismiss();
				}
			});
		} else {
			validation_alert.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Calling funtion to save fields
					saveFields();
				}
			});

			validation_alert.setNegativeButton("No",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		}
		validation_alert.show();
	}

	// Function to split credit card number to four sub strings
	public String[] splitBySplitter(String s) {
		String[] returnArray = new String[4];
		returnArray = s.split("-");
		return returnArray;
	}

	/**
	 * Checking Duplicate Credit card
	 */
	public boolean checkingDuplicateCard(String cardvalue4) {
		boolean rtn = false;
		for (int i = 0; i < WebServiceStaticArrays.mCardOnFiles.size(); i++) {
			CardOnFiles_ClassVariables obj = (CardOnFiles_ClassVariables) WebServiceStaticArrays.mCardOnFiles.get(i);
			try {
				String[] mCardMask = obj.cardMask.split("-");
				if (mCardMask[3].trim().equalsIgnoreCase(cardvalue4)) {
					rtn = true;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return rtn;
	}

	/**
	 * Helper for HorizontalScrollView that should be scrolled by a menu View's width.
	 */
	
	public class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View menu;
		Button mFreezeView;
		/**
		 * Menu must NOT be out/shown to start with.
		 */
		public ClickListenerForScrolling(HorizontalScrollView scrollView,View menu, Button freezeview) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
			this.mFreezeView = freezeview;
		}

		@Override
		public void onClick(View v) {
			mLeftMenuScrollView.fullScroll(View.FOCUS_UP);
			mLeftMenuScrollView.pageScroll(View.FOCUS_UP);
			Context context = menu.getContext();
			int menuWidth = menu.getMeasuredWidth();
			// Ensure menu is visible
			menu.setVisibility(View.VISIBLE);
			InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

			if (!MenuOutClass.ADDCARD_MENUOUT) {
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
			MenuOutClass.ADDCARD_MENUOUT = !MenuOutClass.ADDCARD_MENUOUT;
		}
	}

	/**
	 * Helper class to calculate the menu width at runtime
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
			btnWidth = (int) (btnSlide.getMeasuredWidth() - 10);
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
	protected void onDestroy() {
		super.onDestroy();
		// To notify  system that its time to run garbage collector service
		System.gc();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mNotificationReceiver);
		if (mNotificationSync != null) {
			mNotificationSync.cancelAlarm();
		}
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(AddCreditCard.this);
		mLogoutSession.cancelAlarm(); // To cancel alarm when application goes background
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_Billing")||getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_PointOfSale")){ // for store_owner module header initialization
			mNotificationSync = new ScheduleNotificationSync(AddCreditCard.this,ZouponsConstants.sStoreModuleFlag);	
		}else{
			mNotificationSync = new ScheduleNotificationSync(AddCreditCard.this,ZouponsConstants.sCustomerModuleFlag);
		}
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(AddCreditCard.this).checkIfSessionExpires();
		// To start Logout session
		mLogoutSession = new CheckLogoutSession(AddCreditCard.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
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
		// super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
				try {
					CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
					String[] splittedCardValue = null;
					splittedCardValue = scanResult.getFormattedCardNumber().split(" ");
					mCardNoValue1.setText(splittedCardValue[0]);
					mCardNoValue2.setText(splittedCardValue[1]);
					mCardNoValue3.setText(splittedCardValue[2]);
					mCardNoValue4.removeTextChangedListener(AddCreditCard.this);
					mCardNoValue4.setText(splittedCardValue[3]);
					mCardNoValue4.addTextChangedListener(AddCreditCard.this);
					mExpirationDateValue.setText(String.format("%02d", scanResult.expiryMonth) +" / "+String.valueOf(scanResult.expiryYear).substring(2,4));
					mCVVNoValue.setText(scanResult.cvv);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// alertBox_service("Information","unable to scan credit card");
			}
		} else {
			
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() == 4) {
			String CardNumber = mCardNoValue1.getText().toString().trim() + mCardNoValue2.getText().toString().trim() + mCardNoValue3.getText().toString().trim() + mCardNoValue4.getText().toString().trim();
			if (CardNumber.matches("^4[0-9]{12}(?:[0-9]{3})?$")) {// visa card
				
			} else if (CardNumber.matches("^5[1-5][0-9]{14}$")) {// master card
				
			} else if (CardNumber.matches("^6(?:011|5[0-9]{2})[0-9]{12}$")) {// Discover card
				
			} else {
				if (!ManageCardAddPin_ClassVariables.mEditCardFlag .equals("true")) {
					alertBox_validation("Only 'VISA','MASTER' & 'DISCOVER' cards are acceptable. Please add any one of these cards.",19);
				}
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

}
