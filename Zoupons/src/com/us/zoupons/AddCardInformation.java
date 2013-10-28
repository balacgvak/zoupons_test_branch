/**
 * 
 */
package com.us.zoupons;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.Toast;

import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.CardOnFiles_ClassVariables;
import com.us.zoupons.ClassVariables.EditCardDetails_ClassVariables;
import com.us.zoupons.ClassVariables.FirstDataGlobalpayment_ClassVariables;
import com.us.zoupons.ClassVariables.ManageCardAddPin_ClassVariables;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.FlagClasses.AccountLoginFlag;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.notification.CustomNotificationAdapter;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotificationItemClickListener;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;

/**
 * Class to add credit card informaion
 */
public class AddCardInformation extends Activity implements TextWatcher {

	
	public static final String TAG = "AddCardInformation";
	public static MyHorizontalScrollView scrollView;
	public static View leftMenu;
	public static Button mAddCardFreezeView;
	private View app;
	private ImageView btnSlide;
	public RelativeLayout btnLogout;
	public ViewGroup tabBar;
	/*
	 * LeftMenu Items Intialization *
	 */
	public static LinearLayout mAddCardHome, mAddCardZpay,mAddCardInvoiceCenter, mAddCardGiftCards, mAddCardManageCards,mAddCardReceipts, mAddCardMyFavourites, mAddCardMyFriends,
	mAddCardChat, mAddCardRewards, mAddCardSettings, mAddCardLogout;
	public static TextView mAddCardHomeText, mAddCardZpayText,mAddCardInvoiceCenterText, mAddCardManageCardsText,mAddCardReceiptsText, mAddCardGiftCardsText,mAddCardMyFavoritesText, mAddCardMyFriendsText, mAddCardChatText,
	mAddCardRewardsText, mAddCardSettingsText, mAddCardLogoutText,mPageInfoText;
	private Typeface mZouponsFont;
	private Button mSkip;
	private TextView /* mFirstName,mLastName, */mCardNo, mExpirationDate,
	mCVVNo, mStreetNumber,/*mStreetAddress,*//* mCity,mState, */mZipCode /*mEnterPin,
			mReEnterPin*/;
	private EditText /* mCardNameValue,mCardHolderNameValue, */mCardNoValue1,
	mCardNoValue2, mCardNoValue3, mCardNoValue4, mCVVNoValue,
	/*mStreetAddressValue,*/ mStreetNumberValue, mZipCodeValue
	/*mEnterPinValue, mReEnterPinValue*/;
	/*private EditText mMonthValue, mYearValue mStateValue */;
	/*private ImageView mMonthContextMenu, mYearContextMenu, mStateContextMenu*/;
	private LinearLayout /*mZouponsPinContainer,*/ mAddCardMenuBar, mAddCardBack,mSave;

	public ImageView mLogoutImage, mNotificationImage, mCalloutTriangleImage,
	mHeaderHomeImage;
	public Button mNotificationCount, mScanButton;
	// Notification Pop up layout declaration
	private View mPopUpLayout;
	private ListView mNotificationList;
	private ScheduleNotificationSync mNotificationSync;

	private Calendar c;
	private int cur_year;
	private String mAlertMsg;
	private char validateResult;
	private int validateFlag = 0;
	private String mUserID;

	private ProgressDialog mProgressDialog = null;
	private NetworkCheck connectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;

	private static String mPinFlag = "no";
	private static String mMessage;
	private int mChoosedMonth = 0;

	/**
	 * For store owner header variables declaration
	 */
	StoreOwner_LeftMenu storeowner_leftmenu;
	StoreOwner_LeftMenu storeowner_pointofsale_leftmenu;
	Header header;
	int mMenuFlag;

	// Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	public ImageView mTabBarLoginChoice;
	private String mNonZouponsMemberCardName = "",
			mNonZouponsMemberCardMask = "", mNonZouponsMemberCardCVV = "",
			mNonZouponsMemberCardexpiryYear = "",
			mNonZouponsMemberCardexpiryMonth = "",
			/*mNonZouponsMemberStreetAddress = "",*/
			mNonZouponsMemberStreetNumber = "", mNonZouponsMemberZipcode = "";

	public static ScrollView leftMenuScrollView;
	private EditText mExpirationDateValue;
	boolean mIsForRemove;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i("changes", "updating changes in test");
		
		
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		app = inflater.inflate(R.layout.addcardinformation, null);

		mAddCardFreezeView = (Button) app.findViewById(R.id.addcard_freezeview);

		if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_Billing")||getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_PointOfSale")){ // for store_owner module header initialisation
			if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_Billing")){
				storeowner_leftmenu = new StoreOwner_LeftMenu(AddCardInformation.this,scrollView, leftMenu, mMenuFlag=1, mAddCardFreezeView, TAG);
				leftMenu = storeowner_leftmenu.intializeInflater();
				storeowner_leftmenu.clickListener(leftMenu);
			}else{
				storeowner_pointofsale_leftmenu = new StoreOwner_LeftMenu(AddCardInformation.this,scrollView, leftMenu, mMenuFlag=1, mAddCardFreezeView, TAG);
				leftMenu = storeowner_pointofsale_leftmenu.intializeInflater();
				storeowner_pointofsale_leftmenu.clickListener(leftMenu);
			}

			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeowneraddcard_header);
			header.intializeInflater(scrollView, leftMenu, mMenuFlag=1, mAddCardFreezeView, TAG);
			header.mTabBarNotificationContainer.setVisibility(View.VISIBLE);

			final View[] children = new View[] { leftMenu, app };

			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));

			mAddCardFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, leftMenu, /*mRightMenu,*/ mMenuFlag, mAddCardFreezeView, TAG));

		}else{ // for customer module header initialisation...
			leftMenu = inflater.inflate(R.layout.horz_scroll_menu, null);
			ViewGroup leftMenuItems = (ViewGroup) leftMenu.findViewById(R.id.menuitems);
			tabBar = (ViewGroup) app.findViewById(R.id.addcard_tabBar);

			leftMenuScrollView = (ScrollView) leftMenu.findViewById(R.id.leftmenu_scrollview);
			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);

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
			btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide);
			btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, mAddCardFreezeView));
			btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
			mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
			mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
			mHeaderHomeImage = (ImageView) btnLogout.findViewById(R.id.zoupons_home);
			mHeaderHomeImage.setOnClickListener(new HeaderHomeClickListener(AddCardInformation.this));//ClickListener for Header Home image
			mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
			mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);

			mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
			new LoginChoiceTabBarImage(AddCardInformation.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
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

			mAddCardFreezeView.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, mAddCardFreezeView));

			mAddCardHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, AddCardInformation.this,POJOMainMenuActivityTAG.TAG=TAG));
			mAddCardZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, AddCardInformation.this,POJOMainMenuActivityTAG.TAG=TAG));
			mAddCardInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, AddCardInformation.this,POJOMainMenuActivityTAG.TAG=TAG));
			mAddCardGiftCards.setOnClickListener(new MenuItemClickListener(leftMenuItems,AddCardInformation.this,POJOMainMenuActivityTAG.TAG=TAG));
			mAddCardManageCards.setOnClickListener(new MenuItemClickListener(leftMenuItems, AddCardInformation.this,POJOMainMenuActivityTAG.TAG=TAG));
			mAddCardReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, AddCardInformation.this,POJOMainMenuActivityTAG.TAG=TAG));
			mAddCardMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,AddCardInformation.this,POJOMainMenuActivityTAG.TAG=TAG));
			mAddCardMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,AddCardInformation.this,POJOMainMenuActivityTAG.TAG=TAG));
			mAddCardChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,AddCardInformation.this,POJOMainMenuActivityTAG.TAG=TAG));
			mAddCardRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,AddCardInformation.this,POJOMainMenuActivityTAG.TAG=TAG));
			mAddCardSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,AddCardInformation.this,POJOMainMenuActivityTAG.TAG=TAG));
			mAddCardLogout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setBackgroundResource(R.drawable.gradient_bg_hover);

					if(NormalPaymentAsyncTask.mCountDownTimer!=null){
						NormalPaymentAsyncTask.mCountDownTimer.cancel();
						NormalPaymentAsyncTask.mCountDownTimer = null;
						Log.i(TAG,"Timer Stopped Successfully");
					}

					// AsyncTask to call the logout webservice to end the login session
					new LogoutSessionTask(AddCardInformation.this).execute();
					Intent intent_logout = new Intent(AddCardInformation.this,ZouponsLogin.class);
					intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent_logout);
					finish();
				}
			});

			/*
			 * TabBar Logout
			 **/
			mLogoutImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// AsyncTask to call the logout webservice to end the login session
					new LogoutSessionTask(AddCardInformation.this).execute();
					Intent intent_logout = new Intent(AddCardInformation.this,ZouponsLogin.class);
					intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					finish();
					startActivity(intent_logout);
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
					mNotificationList.setAdapter(new CustomNotificationAdapter(AddCardInformation.this));
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

			final View[] children = new View[] { leftMenu, app};

			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
		}

		mProgressDialog=new ProgressDialog(this);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);

		zouponswebservice = new ZouponsWebService(AddCardInformation.this);
		parsingclass = new ZouponsParsingClass(getApplicationContext());
		connectionAvailabilityChecking = new NetworkCheck();

		mSkip=(Button) app.findViewById(R.id.addcardinformation_skip);
		if(ManageCardAddPin_ClassVariables.mAddPinFlag.equals("true")||ManageCardAddPin_ClassVariables.mEditCardFlag.equals("true")){			//Condition True when page load from managecards
			mSkip.setVisibility(View.GONE);
		}

		mSave=(LinearLayout) app.findViewById(R.id.addcardinformation_save);

		mCardNo=(TextView) app.findViewById(R.id.addcardinformation_cardno);
		mCardNo.setTypeface(mZouponsFont);
		mExpirationDate=(TextView) app.findViewById(R.id.addcardinformation_expirationdate);
		mExpirationDate.setTypeface(mZouponsFont);
		mCVVNo=(TextView) app.findViewById(R.id.addcardinformation_cvvno);
		mCVVNo.setTypeface(mZouponsFont);
		mStreetNumber=(TextView) app.findViewById(R.id.addcardinformation_streetnumber);
		mStreetNumber.setTypeface(mZouponsFont);
		mZipCode=(TextView) app.findViewById(R.id.addcardinformation_zipcode);
		mZipCode.setTypeface(mZouponsFont);
		
		mCardNoValue1=(EditText) app.findViewById(R.id.addcardinformation_card_value1);
		mCardNoValue2=(EditText) app.findViewById(R.id.addcardinformation_card_value2);
		mCardNoValue3=(EditText) app.findViewById(R.id.addcardinformation_card_value3);
		mCardNoValue4=(EditText) app.findViewById(R.id.addcardinformation_card_value4);
		mCardNoValue4.setNextFocusDownId(R.id.addcardinformation_expirationdate_value);
		mExpirationDateValue = (EditText) app.findViewById(R.id.addcardinformation_expirationdate_value);
		mExpirationDateValue.setNextFocusDownId(R.id.addcardinformation_cvvno_value);
		mCVVNoValue=(EditText) app.findViewById(R.id.addcardinformation_cvvno_value);
		mCVVNoValue.setNextFocusDownId(R.id.addcardinformation_streetnumber_value);
		mStreetNumberValue = (EditText) app.findViewById(R.id.addcardinformation_streetnumber_value);
		mStreetNumberValue.setNextFocusDownId(R.id.addcardinformation_zipcode_value);
		mZipCodeValue=(EditText) app.findViewById(R.id.addcardinformation_zipcode_value);
		
		mAddCardBack=(LinearLayout) app.findViewById(R.id.addcardinformation_menubar_back);
		mAddCardMenuBar=(LinearLayout) app.findViewById(R.id.addcardinformation_menubarcontainer);
		mScanButton = (Button) app.findViewById(R.id.scan_card_numberButton);

		mExpirationDateValue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mExpirationDateValue.setSelection(mExpirationDateValue.getText().toString().length());	
			}
		});


		if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("ManageCards")||getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_Billing")||getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_PointOfSale")||getIntent().getExtras().getString("class_name").equalsIgnoreCase("Step2_ManageCards")){

			mAddCardMenuBar.setVisibility(View.VISIBLE);
			mSkip.setVisibility(View.GONE);

			if(tabBar!= null){
				tabBar.setVisibility(View.VISIBLE);	
			}else{
				header.setVisibility(View.VISIBLE);
			}
		}

		if(!getIntent().getExtras().getString("class_name").equalsIgnoreCase("Step2_ManageCards")){
			if((ManageCardAddPin_ClassVariables.mAddPinFlag.equals("true")||ManageCardAddPin_ClassVariables.mEditCardFlag.equals("true"))){
				// Intent from Managecards
				mAddCardMenuBar.setVisibility(View.VISIBLE);
			}	
		}else{ // Intent from zpay step2 -- remove skip button and show back menu bar.
			mSkip.setVisibility(View.GONE);
			mAddCardMenuBar.setVisibility(View.VISIBLE);
		}

		mAddCardBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("ManageCards")){
					Intent intent_managecards = new Intent(getApplicationContext(),ManageCards.class);
					startActivity(intent_managecards);
				}else if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_Billing")){
					Intent intent_managecards = new Intent(getApplicationContext(),ManageCards.class);
					intent_managecards.putExtra("FromStoreOwnerBilling", true);
					startActivity(intent_managecards);
				}else if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("StoreOwner_PointOfSale")){
					finish();
				}else if(getIntent().getExtras().getString("class_name").equalsIgnoreCase("Step2_ManageCards")){
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
				startActivity(new Intent(AddCardInformation.this,SlidingView.class));
			}
		});

		mExpirationDateValue.addTextChangedListener(new DateValidator(AddCardInformation.this, mExpirationDateValue,mCVVNoValue));

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
				mStreetNumberValue.setText(EditCardDetails_ClassVariables.StreatAddress);
				mZipCodeValue.setText(EditCardDetails_ClassVariables.zipCode);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		mScanButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent scanIntent = new Intent(AddCardInformation.this, CardIOActivity.class);
				// required for authentication with card.io
				scanIntent.putExtra(CardIOActivity.EXTRA_APP_TOKEN, "e84db94d4fac49709305dcf04a0fd153");
				// customize these values to suit your needs.
				scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: true
				scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
				scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
				scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false);
				scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, false);
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
					validateResult='Y';
				}

				if(validateResult=='N'){
					alertBox_validation(mAlertMsg, validateFlag);
				}else{
					//Calling funtion to save fields
					saveFields();
				}
			}
		});
	}

	private void saveFields() {
		Log.i(TAG, "Validation Success");

		/* Get User ID from signup arraylist */
		final String user_type;
		if (getIntent().getExtras().getString("class_name")
				.equalsIgnoreCase("StoreOwner_PointOfSale")) { // for
			// store_owner
			// module header
			// initialisation
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
					if (connectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())) {
						
						String CardName = "";
						String UserName = "";
						String CardNumber = mCardNoValue1.getText().toString()
								.trim()
								+ mCardNoValue2.getText().toString().trim()
								+ mCardNoValue3.getText().toString().trim()
								+ mCardNoValue4.getText().toString().trim();
						String ZipCode = mZipCodeValue.getText().toString()
								.trim();
						String StreetAddress = mStreetNumberValue.getText()
								.toString().trim()/*
								 * +"|"+mZipCodeValue.getText().
								 * toString().trim()
								 */;
						String CvvNumber = mCVVNoValue.getText().toString().trim();

						mChoosedMonth = Integer.parseInt(mExpirationDateValue.getText().toString().substring(0, 2));

						String Month = String.format("%02d", mChoosedMonth);

						String Year = mExpirationDateValue.getText().toString().substring(mExpirationDateValue.getText().length()-2, mExpirationDateValue.getText().length());

						mGetResponse = zouponswebservice.Getfirst_data_global_payment(mUserID, UserName, CardNumber, CvvNumber, Month, Year, StreetAddress, ZipCode, CardName, user_type);

						if (!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")) {
							String mParsingResponse = parsingclass.parsefirstdataglobalpayment(mGetResponse);
							if (mParsingResponse.equalsIgnoreCase("success")) {
								for (int i = 0; i < WebServiceStaticArrays.mFirstDataGlobalPaymentList.size(); i++) {
									FirstDataGlobalpayment_ClassVariables parsedobjectvalues = (FirstDataGlobalpayment_ClassVariables) WebServiceStaticArrays.mFirstDataGlobalPaymentList.get(i);
									mMessage = parsedobjectvalues.mMessage;

									if (parsedobjectvalues.mPin.equalsIgnoreCase("Yes")) {
										mPinFlag = "yes";
										bundle_pin.putString("updateui", "yes");
										msg_pin.setData(bundle_pin);
										handler_updateUI.sendMessage(msg_pin);
									}
								}
								updateHandler(bundle, msg_response, mMessage);
							} else if (mParsingResponse.equalsIgnoreCase("failure")) {
								updateHandler(bundle, msg_response, mParsingResponse); // send update to
								// handler
								Log.i(TAG, "Error");
							} else if (mParsingResponse.equalsIgnoreCase("norecords")) {
								updateHandler(bundle, msg_response, mParsingResponse); // send update to
								// handler
								Log.i(TAG, "No Records");
							}
						} else {
							updateHandler(bundle, msg_response, mGetResponse); // send update to handler about emailid validation response
						}
					} else {
						Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Log.i(TAG, "Thread Error");
					e.printStackTrace();
				}
				mProgressDialog.setProgress(100);
				mProgressDialog.dismiss();
			}
		});
		syncThread.start();
	}

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

	private void updateHandler(Bundle bundle, Message msg_response,
			String mGetResponse) {
		bundle.putString("returnResponse", mGetResponse);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}

	Handler handler_response = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String key = b.getString("returnResponse");

			if (key.trim().equals("success")) {
				clearFields(); // Function to clear all fields
				Log.i(TAG, "Response Get Successfully");
			} else if (key.trim().equals("failure")) {
				alertBox_service("Information", "Response Error");
			} else if (key.trim().equals("noresponse")
					|| key.trim().equals("norecords")) {
				alertBox_service("Information", "No Data Available");
			} else if (key.trim().equals("Successfully Pre Authorized")) {
				// Assigning to use it in edit card details from POS non member
				// editcard
				mNonZouponsMemberCardName = "";
				mNonZouponsMemberCardMask = "****-****-****-"
						+ mCardNoValue4.getText().toString();
				mNonZouponsMemberCardCVV = mCVVNoValue.getText().toString();

				mChoosedMonth = Integer.parseInt(mExpirationDateValue.getText().toString().substring(0, 2));
				String Month = String.format("%02d", mChoosedMonth);
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

	private void alertBox_service(final String title, final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this);
		service_alert.setTitle(title);
		if (msg.equalsIgnoreCase("Successfully Pre Authorized")) {
			service_alert
			.setMessage("The credit card information is securely saved");
		} else {
			service_alert.setMessage(msg);
		}
		service_alert.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (msg.equals("Successfully Pre Authorized")) {
					if (getIntent().getExtras().getString("class_name")
							.equalsIgnoreCase("ZouponsLogin")
							|| getIntent().getExtras()
							.getString("class_name")
							.equalsIgnoreCase("Registration")) {
						startActivity(new Intent(
								AddCardInformation.this,
								SlidingView.class));
					} else if (getIntent().getExtras()
							.getString("class_name")
							.equalsIgnoreCase("ManageCards")) {
						startActivity(new Intent(
								AddCardInformation.this,
								ManageCards.class));
					} else if (getIntent().getExtras()
							.getString("class_name")
							.equalsIgnoreCase("StoreOwner_Billing")) {
						Intent intent_rightmenuBilling = new Intent(
								AddCardInformation.this,
								ManageCards.class);
						intent_rightmenuBilling.putExtra(
								"FromStoreOwnerBilling", true);
						startActivity(intent_rightmenuBilling);
					} else if (getIntent().getExtras()
							.getString("class_name")
							.equalsIgnoreCase("Step2_ManageCards")) {
						setResult(RESULT_OK); // to move control to
						// step2 managecards on
						// activity result
						finish();
					} else if (getIntent().getExtras()
							.getString("class_name")
							.equalsIgnoreCase("StoreOwner_PointOfSale")) {
						if (WebServiceStaticArrays.mFirstDataGlobalPaymentList
								.size() > 0) {
							FirstDataGlobalpayment_ClassVariables mFirstData_details = (FirstDataGlobalpayment_ClassVariables) WebServiceStaticArrays.mFirstDataGlobalPaymentList
									.get(0);
							Intent card_intent = new Intent();
							card_intent.putExtra("user_id", getIntent()
									.getExtras().getString("user_id"));
							card_intent.putExtra("card_id",
									mFirstData_details.mCardId);
							card_intent.putExtra("card_name",
									mNonZouponsMemberCardName);
							card_intent.putExtra("card_masknumber",
									mNonZouponsMemberCardMask);
							card_intent.putExtra("card_expirymonth",
									mNonZouponsMemberCardexpiryMonth);
							card_intent.putExtra("card_expiryyear",
									mNonZouponsMemberCardexpiryYear);
							card_intent.putExtra("card_cvv",
									mNonZouponsMemberCardCVV);
							card_intent.putExtra("address", "");
							card_intent.putExtra("zipcode",
									mNonZouponsMemberZipcode);
							card_intent.putExtra("streetnumber",
									mNonZouponsMemberStreetNumber);
							ManageCardAddPin_ClassVariables.mEditCardFlag = "false";
							ManageCardAddPin_ClassVariables.mAddPinFlag = "true";
							setResult(RESULT_OK, card_intent); // to
							// move
							// control
							// to
							// step2
							// managecards
							// on
							// activity
							// result
							// or
							// POS
							// part2
						}
						finish();
					}
				} else if (mPinFlag.equals("no")) {
					// Toast.makeText(AddCardInformation.this,
					// "Your Pin number is not valid",
					// Toast.LENGTH_LONG).show();
				}

				dialog.dismiss();
			}
		});
		service_alert.show();
	}

	private void contextMenuOpen(View sender) {
		sender.setLongClickable(false);
		registerForContextMenu(sender);
		openContextMenu(sender);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getGroupId()) {
		case 1:
			mChoosedMonth = item.getItemId();
			/*mMonthValue.setText(item.getTitle());
			mYearValue.getText().clear();*/
			return true;
		case 2:

			//mYearValue.setText(item.getTitle());

			Calendar mCurrentTime = Calendar.getInstance();
			int mMonth = mCurrentTime.get(Calendar.MONTH) + 1;
			int mYear = mCurrentTime.get(Calendar.YEAR);

			

			/*if (mYearValue.getText().toString().trim()
					.equalsIgnoreCase(String.valueOf(mYear))) {
				if (mChoosedMonth >= mMonth) {
					mYearValue.setText(item.getTitle());
				} else {
					mYearValue.setText("");
					alertBox_service("Information",
							"The date you entered has passed.  Please enter a valid date for expiration");
				}
			}*/
			return true;
		case 3:
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		Log.i(TAG, "onCreateContextMenu");
		/*if (v.equals(mMonthContextMenu) == true) {
			Log.i(TAG, "View : " + mMonthContextMenu);
			contextMenuItems(menu, v, 1);
		} else if (v.equals(mYearContextMenu) == true) {
			Log.i(TAG, "View : " + mYearContextMenu);
			contextMenuItems(menu, v, 2);
		}*/
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	private void contextMenuItems(ContextMenu menu, View v, int groupid) {
		if (groupid == 1) {
			menu.setHeaderTitle("Month");
			menu.add(1, 1, 0, "January");
			menu.add(1, 2, 0, "February");
			menu.add(1, 3, 0, "March");
			menu.add(1, 4, 0, "April");
			menu.add(1, 5, 0, "May");
			menu.add(1, 6, 0, "June");
			menu.add(1, 7, 0, "July");
			menu.add(1, 8, 0, "August");
			menu.add(1, 9, 0, "September");
			menu.add(1, 10, 0, "October");
			menu.add(1, 11, 0, "November");
			menu.add(1, 12, 0, "December");
		} else if (groupid == 2) {
			c = Calendar.getInstance();
			cur_year = c.get(Calendar.YEAR);
			menu.setHeaderTitle("year");
			menu.add(2, v.getId(), 0, String.valueOf(cur_year));
			for (int i = 1; i < 30; i++) {
				cur_year = cur_year + 1;
				menu.add(2, v.getId(), 0, String.valueOf(cur_year));
			}
		}
	}

	private void setvalidation(String msg, int validateflag, char validateresult) {
		mAlertMsg = msg;
		validateFlag = validateflag;
		validateResult = validateresult;
	}

	private void alertBox_validation(String msg, final int validateflag) {
		AlertDialog.Builder validation_alert = new AlertDialog.Builder(this);
		validation_alert.setTitle("Information");
		validation_alert.setMessage(msg);
		if (validateflag != 21) {
			validation_alert.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (validateflag != 0) {
						if (validateflag == 1) {
							mStreetNumberValue.requestFocus();
						} else if (validateflag == 2) {
							// mCardHolderNameValue.requestFocus();
						} else if (validateflag == 3) {
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
							//contextMenuOpen(mMonthContextMenu);
						} else if (validateflag == 9) {
							mExpirationDateValue.requestFocus();
							//contextMenuOpen(mYearContextMenu);
						} else if (validateflag == 10) {
							mCVVNoValue.requestFocus();
						} else if (validateflag == 11) {
							mCVVNoValue.setText("");
							mCVVNoValue.requestFocus();
						} else if (validateflag == 12) {
							/*mStreetAddressValue.requestFocus();*/
						} else if (validateflag == 14) {
							/*contextMenuOpen(mStateContextMenu);*/
						} else if (validateflag == 15) {
							mZipCodeValue.requestFocus();
						} else if (validateflag == 16) {
							/*mEnterPinValue.setText("");
							mEnterPinValue.requestFocus();*/
						} else if (validateflag == 17) {
							/*mReEnterPinValue.setText("");
							mReEnterPinValue.requestFocus();*/
						} else if (validateflag == 18) {
							/*mReEnterPinValue.setText("");
							mReEnterPinValue.requestFocus();*/
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

	public static String[] splitByLength(String s, int chunkSize) {
		int arraySize = (int) Math.ceil((double) s.length() / chunkSize);

		String[] returnArray = new String[arraySize];

		int index = 0;
		for (int i = 0; i < s.length(); i = i + chunkSize) {
			if (s.length() - i < chunkSize) {
				returnArray[index++] = s.substring(i);
			} else {
				returnArray[index++] = s.substring(i, i + chunkSize);
			}
		}
		return returnArray;
	}

	public String[] splitBySplitter(String s) {
		String[] returnArray = new String[4];
		returnArray = s.split("-");
		return returnArray;
	}

	public String monthName(String month) {
		String result = "";
		if (month.equals("01")) {
			result = "January";
		} else if (month.equals("02")) {
			result = "Feburary";
		} else if (month.equals("03")) {
			result = "March";
		} else if (month.equals("04")) {
			result = "April";
		} else if (month.equals("05")) {
			result = "May";
		} else if (month.equals("06")) {
			result = "June";
		} else if (month.equals("07")) {
			result = "July";
		} else if (month.equals("08")) {
			result = "August";
		} else if (month.equals("09")) {
			result = "September";
		} else if (month.equals("10")) {
			result = "October";
		} else if (month.equals("11")) {
			result = "November";
		} else if (month.equals("12")) {
			result = "December";
		} else if (month.equals("January")) {
			result = "1";
		} else if (month.equals("Feburary")) {
			result = "2";
		} else if (month.equals("March")) {
			result = "3";
		} else if (month.equals("April")) {
			result = "4";
		} else if (month.equals("May")) {
			result = "5";
		} else if (month.equals("June")) {
			result = "6";
		} else if (month.equals("July")) {
			result = "7";
		} else if (month.equals("August")) {
			result = "8";
		} else if (month.equals("September")) {
			result = "9";
		} else if (month.equals("October")) {
			result = "10";
		} else if (month.equals("November")) {
			result = "11";
		} else if (month.equals("December")) {
			result = "12";
		}
		return result;
	}

	/**
	 * Checking Duplicate function
	 */
	public boolean checkingDuplicateCard(String cardvalue4) {
		boolean rtn = false;
		for (int i = 0; i < WebServiceStaticArrays.mCardOnFiles.size(); i++) {
			CardOnFiles_ClassVariables obj = (CardOnFiles_ClassVariables) WebServiceStaticArrays.mCardOnFiles
					.get(i);
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
	 * Helper for examples with a HSV that should be scrolled by a menu View's
	 * width.
	 */
	public static class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View menu;
		Button mFreezeView;

		/**
		 * Menu must NOT be out/shown to start with.
		 */

		public ClickListenerForScrolling(HorizontalScrollView scrollView,
				View menu, Button freezeview) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
			this.mFreezeView = freezeview;
		}

		@Override
		public void onClick(View v) {

			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);

			Context context = menu.getContext();
			String msg = "Slide " + new Date();
			System.out.println(msg);

			int menuWidth = menu.getMeasuredWidth();
			Log.i(TAG, "Menu Width : " + menuWidth);

			// Ensure menu is visible
			menu.setVisibility(View.VISIBLE);

			InputMethodManager inputManager = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

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

	static class SizeCallbackForMenu implements SizeCallback {
		int btnWidth;
		View btnSlide;

		public SizeCallbackForMenu(View btnSlide) {
			super();
			this.btnSlide = btnSlide;
		}

		@Override
		public void onGlobalLayout() {
			btnWidth = (int) (btnSlide.getMeasuredWidth() - 10);
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
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		if (mNotificationSync != null) {
			mNotificationSync.cancelAlarm();
		}
	}

	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons()
		.isApplicationGoneBackground(AddCardInformation.this);
		mLogoutSession.cancelAlarm(); // To cancel alarm when application goes
		// background
		super.onUserLeaveHint();
	}

	@Override
	protected void onResume() {
		registerReceiver(mReceiver, new IntentFilter(
				BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(
				AddCardInformation.this);
		mNotificationSync.setRecurringAlarm();

		new CheckUserSession(AddCardInformation.this).checkIfSessionExpires();

		// To start Logout session
		mLogoutSession = new CheckLogoutSession(AddCardInformation.this);
		mLogoutSession.setLogoutTimerAlarm();

		super.onResume();
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
					CreditCard scanResult = data
							.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
					String[] splittedCardValue = null;
					splittedCardValue = scanResult.getFormattedCardNumber()
							.split(" ");
					mCardNoValue1.setText(splittedCardValue[0]);
					mCardNoValue2.setText(splittedCardValue[1]);
					mCardNoValue3.setText(splittedCardValue[2]);
					mCardNoValue4
					.removeTextChangedListener(AddCardInformation.this);
					mCardNoValue4.setText(splittedCardValue[3]);
					mCardNoValue4
					.addTextChangedListener(AddCardInformation.this);
					/*mMonthValue.setText(monthName(String.format("%02d",
							scanResult.expiryMonth)));
					mYearValue.setText(String.valueOf(scanResult.expiryYear));*/
					mExpirationDateValue.setText(String.format("%02d", scanResult.expiryMonth) +" / "+String.valueOf(scanResult.expiryYear).substring(2,4));
					mCVVNoValue.setText(scanResult.cvv);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// alertBox_service("Information","unable to scan credit card");
			}
		} else {
			Log.i("scan result", "failed");
		}
		// else handle other activity results
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			try {
				Log.i(TAG, "OnReceive");
				if (intent.hasExtra("FromNotification")) {
					if (NotificationDetails.notificationcount > 0) {
						mNotificationCount.setVisibility(View.VISIBLE);
						mNotificationCount
						.setText(String
								.valueOf(NotificationDetails.notificationcount));
					} else {
						mNotificationCount.setVisibility(View.GONE);
						Log.i("Notification result", "No new notification");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() == 4) {
			String CardNumber = mCardNoValue1.getText().toString().trim()
					+ mCardNoValue2.getText().toString().trim()
					+ mCardNoValue3.getText().toString().trim()
					+ mCardNoValue4.getText().toString().trim();
			if (CardNumber.matches("^4[0-9]{12}(?:[0-9]{3})?$")) {// visa card
				Log.i(TAG, "Visa Card");
			} else if (CardNumber.matches("^5[1-5][0-9]{14}$")) {// master card
				Log.i(TAG, "Master Card");
			} else if (CardNumber.matches("^6(?:011|5[0-9]{2})[0-9]{12}$")) {// Discover card
				Log.i(TAG, "Discover Card");
			} else {
				if (!ManageCardAddPin_ClassVariables.mEditCardFlag
						.equals("true")) {
					alertBox_validation(
							"Only 'VISA','MASTER' & 'DISCOVER' cards are acceptable. Please add any one of these cards.",
							19);
				}
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

}
