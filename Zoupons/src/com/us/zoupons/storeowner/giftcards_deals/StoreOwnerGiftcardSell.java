package com.us.zoupons.storeowner.giftcards_deals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Patterns;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MobileNumberFilter;
import com.us.zoupons.MobileNumberTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.CardId_ClassVariable;
import com.us.zoupons.classvariables.CardOnFiles_ClassVariables;
import com.us.zoupons.classvariables.EditCardDetails_ClassVariables;
import com.us.zoupons.classvariables.ManageCardAddPin_ClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.shopper.wallet.AddCreditCard;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;
import com.us.zoupons.storeowner.invoice.CheckZouponsCustomerTask;
import com.us.zoupons.storeowner.pointofsale.ValidateCustomerUsingMobileNumber;

/**
 * 
 * Activity to host gc/dc sell to customer
 *
 */

public class StoreOwnerGiftcardSell extends Activity{

	// Initializing views and variables
	private  String mTAG="StoreOwnerGiftCardsPurchase";
	private MyHorizontalScrollView mScrollView;
	private Header mZouponsheader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private View mRightMenu,mLeftMenu,mApp;
	private int mMenuFlag;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private ImageView mRightMenuHolder,mGiftcardPurchaseCustomerImage,mGiftcardPurchaseChoosecardContextImage;
	private Button mStoreOwnerGiftCardsPurchaseFreezeView;
	private RelativeLayout mStoreInfoHeader,mZouponsCustomerDetailsLayout;
	private LinearLayout mGiftcardPurchaseMobileNumberLayout,mGiftCardPurchaseEmployeePinLayout,mGiftCardPurchaseCustomerPinLayout,mGiftCardPurchaseChooseCardLayout,mFooterView,
	mGiftcardPurchaseAddCardContainer,mGiftcardPurchaseListCardContainer,mNonZouponsCustomerDetailsLayout,mNonZouponsCustomerCardDetailsContainer,mNonZouponsCustomerAddedCardMaskContainer;
	private EditText mGiftcardPurchaseMobileNumber,mGiftcardPurchaseEmployeePin,mGiftcardPurchaseCustomerPin,mGiftcardPurchaseChoosedCard,mGiftcardPurchaseCardUserPin,
	mNonZouponsCustomerFirstName,mNonZouponsCustomerLastName,mNonZouponsCustomerEmailAddress;
	private TextView mBackMenu,mGiftcardPurchaseFaceValue,mGiftcardPurchaseCustomerFirstName,mGiftcardPurchaseCustomerLastName,mNonZouponsCustomerCardMask;
	private Button mMobileNumberSubmitButton,mPurchaseGiftcardEmployeePinButton,mPurchaseGiftcardCustomerPinButton,mPurchaseGiftcardProcessOrderButton,mGiftcardPurchaseAddCard,
	mNonZouponsCustomerAddCardButton;
	private ScrollView mGiftcardPurchaseCustomerDetails;
	private String mCardType="",mCardId="",mCardFaceValue="",mCardValue="",mChoosedCreditcardId="";
	private String mNonZouponsMemberUserId="",mNonZouponsMemberCardID="",mNonZouponsMemberCardName="",mNonZouponsMemberCardMask="",mNonZouponsMemberCardCVV="",mNonZouponsMemberCardexpiryYear="",mNonZouponsMemberCardexpiryMonth="",mNonZouponsMemberStreetAddress="",mNonZouponsMemberZipcode="";
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(mScrollView);
		mApp = inflater.inflate(R.layout.storeowner_giftcards_sell, null);
		ViewGroup mMiddleView = (ViewGroup) mApp.findViewById(R.id.storeownergifcards_purchase_container);
		mFooterView = (LinearLayout) mApp.findViewById(R.id.storeownergifcards_purchase_footerLayoutId);
		mStoreInfoHeader = (RelativeLayout) mMiddleView.findViewById(R.id.storeownergifcards_purchase_storename_header);
		mRightMenuHolder = (ImageView) mStoreInfoHeader.findViewById(R.id.storeownergifcards_purchase_rightmenu);
		mStoreOwnerGiftCardsPurchaseFreezeView = (Button) mApp.findViewById(R.id.storeownergifcards_purchase_freeze);
		mStoreownerRightmenu = new StoreOwner_RightMenu(StoreOwnerGiftcardSell.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerGiftCardsPurchaseFreezeView, mTAG);
		mRightMenu = mStoreownerRightmenu.intializeInflater();
		mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreOwnerGiftcardSell.this,mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwnerGiftCardsPurchaseFreezeView, mTAG);
		mLeftMenu = mStoreownerLeftmenu.intializeInflater();
		mStoreownerLeftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
		mStoreownerRightmenu.clickListener(mLeftMenu,mRightMenu);
		/* Header Tab Bar which contains logout,notification and home buttons*/
		mZouponsheader = (Header) mApp.findViewById(R.id.storeownergifcards_purchase_header);
		mZouponsheader.intializeInflater(mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwnerGiftCardsPurchaseFreezeView, mTAG);
		final View[] children = new View[] { mLeftMenu, mApp};
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsheader.mLeftMenuBtnSlide));
		mNotificationReceiver = new NotifitcationReceiver(mZouponsheader.mTabBarNotificationCountBtn);
		// Notitification pop up layout declaration
		mZouponsheader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsheader,mZouponsheader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
		mZouponsheader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsheader,mZouponsheader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
		// Footer layout view initialization
		mBackMenu = (TextView) mFooterView.findViewById(R.id.storeownergifcards_purchase_BackId);
		// Initial mobile number view declaration
		mGiftcardPurchaseMobileNumberLayout = (LinearLayout) mMiddleView.findViewById(R.id.storeownergifcards_purchase_mobilenumber_layout);
		mGiftcardPurchaseMobileNumber =  (EditText) mGiftcardPurchaseMobileNumberLayout.findViewById(R.id.storeownergifcards_purchase_mobilenumber);
		mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwnerGiftCardsPurchaseFreezeView, mTAG,mGiftcardPurchaseMobileNumber, false));
		mStoreOwnerGiftCardsPurchaseFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerGiftCardsPurchaseFreezeView, mTAG,mGiftcardPurchaseMobileNumber, true));
		mMobileNumberSubmitButton = (Button) mGiftcardPurchaseMobileNumberLayout.findViewById(R.id.storeownergifcards_purchase_mobileNumber_Submit);
		// Customer details view declaration
		mGiftcardPurchaseCustomerDetails = (ScrollView) mMiddleView.findViewById(R.id.storeownergifcards_purchase_scrollview);
		mZouponsCustomerDetailsLayout = (RelativeLayout) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_ActivecustomerDetails);
		mGiftcardPurchaseCustomerImage = (ImageView) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_customerImage);
		mGiftcardPurchaseCustomerFirstName = (TextView) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_customerFirstName);
		mGiftcardPurchaseCustomerLastName = (TextView) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_customerLastName);
		mGiftcardPurchaseFaceValue = (TextView) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_GiftcardFaceValue);
		// Non Zoupons customer details
		mNonZouponsCustomerDetailsLayout = (LinearLayout) findViewById(R.id.storeownergifcards_purchase_nonzouponscustomer_container);
		mNonZouponsCustomerFirstName = (EditText) findViewById(R.id.storeownergifcards_purchase_customerFirstNameValue);
		mNonZouponsCustomerLastName = (EditText) findViewById(R.id.storeownergifcards_purchase_customerLastNameValue);
		mNonZouponsCustomerEmailAddress = (EditText) findViewById(R.id.storeownergifcards_purchase_customerEmailAddressValue);
		mNonZouponsCustomerCardDetailsContainer = (LinearLayout) findViewById(R.id.storeownergifcards_purchase_customerCardDetailsContainer);
		mNonZouponsCustomerAddedCardMaskContainer = (LinearLayout) findViewById(R.id.storeownergifcards_purchase_customerAddedCardContainer);
		mNonZouponsCustomerCardMask = (TextView) findViewById(R.id.storeownergifcards_purchase_customerCreditcardMask);
		mNonZouponsCustomerAddCardButton = (Button) findViewById(R.id.storeownergifcards_purchase_customerAddCardButton);
		// Employee Pin layout
		mGiftCardPurchaseEmployeePinLayout =(LinearLayout) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_EmployeePinLayout);
		mGiftcardPurchaseEmployeePin = (EditText) mGiftCardPurchaseEmployeePinLayout.findViewById(R.id.storeownergifcards_purchase_EmployeePin);
		// Customer Pin layout
		mGiftCardPurchaseCustomerPinLayout = (LinearLayout) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_customerPinLayout);
		mGiftcardPurchaseCustomerPin = (EditText) mGiftCardPurchaseCustomerPinLayout.findViewById(R.id.storeownergifcards_purchase_customerPin);
		// Choose card to purchase layout
		mGiftCardPurchaseChooseCardLayout = (LinearLayout) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_cardchooseLayout);
		mGiftcardPurchaseAddCardContainer = (LinearLayout) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_addcardContainer);
		mGiftcardPurchaseListCardContainer = (LinearLayout) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_cardchooseContainer);
		mGiftcardPurchaseAddCard = (Button) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_addcard);
		mGiftcardPurchaseChoosedCard =  (EditText) mGiftCardPurchaseChooseCardLayout.findViewById(R.id.storeownergifcards_purchase_choosecard);
		mGiftcardPurchaseChoosedCard.setLongClickable(false); // To disable cut/copy/paste options
		mGiftcardPurchaseChoosecardContextImage = 	(ImageView) mGiftCardPurchaseChooseCardLayout.findViewById(R.id.storeownergifcards_purchase_cardcontextmenu);
		registerForContextMenu(mGiftcardPurchaseChoosecardContextImage);
		mGiftcardPurchaseCardUserPin =(EditText) mGiftCardPurchaseChooseCardLayout.findViewById(R.id.storeownergifcards_purchase_userPin);
		mPurchaseGiftcardEmployeePinButton = (Button) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_employeepinbutton);
		mPurchaseGiftcardCustomerPinButton = (Button) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_customerpinbutton);
		mPurchaseGiftcardProcessOrderButton = (Button) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_processorderbutton);
		// To validate Mobile Number field
		mGiftcardPurchaseMobileNumber.addTextChangedListener(new MobileNumberTextWatcher());
		mGiftcardPurchaseMobileNumber.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});
		if(getIntent().hasExtra("card_id") && getIntent().hasExtra("card_type") && getIntent().hasExtra("facevalue") && getIntent().hasExtra("cardvalue")){
			mCardType = getIntent().getExtras().getString("card_type");
			mCardId = getIntent().getExtras().getString("card_id");
			mCardFaceValue = getIntent().getExtras().getString("facevalue");
			mCardValue = getIntent().getExtras().getString("cardvalue");
			if(mCardType.equalsIgnoreCase("Regular"))
				mGiftcardPurchaseFaceValue.setText("Face value: $"+String.format("%.2f",Double.parseDouble(mCardFaceValue)));
			else
				mGiftcardPurchaseFaceValue.setText("Face value: $"+String.format("%.2f",Double.parseDouble(mCardFaceValue))+" You pay: $"+String.format("%.2f",Double.parseDouble(mCardValue)));
		}

		mMobileNumberSubmitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mGiftcardPurchaseMobileNumber.getText().toString().trim().length() == 0){
					alertBox_service("Information","Please enter mobile number",mGiftcardPurchaseMobileNumber);
				}else if(mGiftcardPurchaseMobileNumber.getText().toString().trim().length() != 12){
					alertBox_service("Information","Please enter valid mobile number",mGiftcardPurchaseMobileNumber);						
				}else{
					if(new NetworkCheck().ConnectivityCheck(StoreOwnerGiftcardSell.this)){
						CheckZouponsCustomerTask customerTask = new CheckZouponsCustomerTask(StoreOwnerGiftcardSell.this, mGiftcardPurchaseMobileNumberLayout, mGiftcardPurchaseCustomerDetails, mGiftcardPurchaseCustomerImage, mFooterView,mStoreInfoHeader,mGiftcardPurchaseCustomerFirstName,mGiftcardPurchaseCustomerLastName,mZouponsCustomerDetailsLayout,mNonZouponsCustomerDetailsLayout,mNonZouponsCustomerCardDetailsContainer,mGiftCardPurchaseEmployeePinLayout);
						customerTask.execute(mGiftcardPurchaseMobileNumber.getText().toString());
					}else{
						Toast.makeText(StoreOwnerGiftcardSell.this, "Network connection not available", Toast.LENGTH_SHORT).show();	
					}
				}
			}
		});

		mPurchaseGiftcardEmployeePinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mGiftcardPurchaseEmployeePin.getText().toString().trim().length()==0){
					alertBox_service("Information", "Please enter pin to proceed the payment",mGiftcardPurchaseEmployeePin);
				}else if(mGiftcardPurchaseEmployeePin.getText().toString().trim().length()<4){
					alertBox_service("Information", "Please enter four digit PIN to proceed the payment",mGiftcardPurchaseEmployeePin);
				}else{
					boolean isForNonActiveCustomer = false;
					if(mNonZouponsCustomerDetailsLayout.getVisibility() == View.VISIBLE)
						isForNonActiveCustomer = true;
					SecurityPinCheckTask mCheckPinTask = new SecurityPinCheckTask(StoreOwnerGiftcardSell.this, mGiftCardPurchaseEmployeePinLayout, mGiftCardPurchaseCustomerPinLayout,isForNonActiveCustomer,"");
					mCheckPinTask.execute(mGiftcardPurchaseEmployeePin.getText().toString());	
				}
			}
		});

		mPurchaseGiftcardCustomerPinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mGiftcardPurchaseCustomerPin.getText().toString().trim().length()==0){
					alertBox_service("Information", "Please enter pin to proceed the payment",mGiftcardPurchaseCustomerPin);
				}else if(mGiftcardPurchaseCustomerPin.getText().toString().trim().length()<4){
					alertBox_service("Information", "Please enter four digit PIN to proceed the payment",mGiftcardPurchaseCustomerPin);
				}else{
					SecurityPinCheckTask mCheckPinTask = new SecurityPinCheckTask(StoreOwnerGiftcardSell.this, mGiftCardPurchaseCustomerPinLayout, mGiftCardPurchaseChooseCardLayout,true);
					mCheckPinTask.execute(mGiftcardPurchaseCustomerPin.getText().toString(),mGiftcardPurchaseCustomerFirstName.getTag().toString(),"shopper");
				}
			}
		});

		mPurchaseGiftcardProcessOrderButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) { 
				if(mGiftcardPurchaseChoosedCard.getText().toString().equalsIgnoreCase("")){
					alertBox_service("Information", "Please choose credit card for payment",null);
				}else if(mGiftcardPurchaseCardUserPin.getText().toString().trim().length()==0){
					alertBox_service("Information", "Please enter pin to proceed the payment",mGiftcardPurchaseCardUserPin);
				}else if(mGiftcardPurchaseCardUserPin.getText().toString().trim().length()<4){
					alertBox_service("Information", "Please enter four digit PIN to proceed the payment",mGiftcardPurchaseCardUserPin);
				}else{
					SecurityPinCheckTask mCheckPinTask = new SecurityPinCheckTask(StoreOwnerGiftcardSell.this, mGiftCardPurchaseCustomerPinLayout, mGiftCardPurchaseChooseCardLayout,mGiftcardPurchaseCustomerFirstName.getTag().toString(),mCardType,mChoosedCreditcardId,mCardId,mCardValue,mCardFaceValue);
					mCheckPinTask.execute(mGiftcardPurchaseCardUserPin.getText().toString(),mGiftcardPurchaseCustomerFirstName.getTag().toString(),"shopper");
				}
			}
		});

		mBackMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mGiftcardPurchaseMobileNumberLayout.getVisibility() == View.VISIBLE){
					finish();
				}else if(mGiftCardPurchaseEmployeePinLayout.getVisibility() == View.VISIBLE  || mNonZouponsCustomerDetailsLayout.getVisibility()== View.VISIBLE){
					mGiftcardPurchaseMobileNumberLayout.setVisibility(View.VISIBLE);
					mGiftcardPurchaseMobileNumber.getText().clear();
					mGiftcardPurchaseCustomerDetails.setVisibility(View.GONE);
					mGiftcardPurchaseEmployeePin.getText().clear();
					mStoreInfoHeader.setVisibility(View.GONE);
					mNonZouponsCustomerFirstName.getText().clear();
					mNonZouponsCustomerLastName.getText().clear();
					mNonZouponsCustomerEmailAddress.getText().clear();
					mNonZouponsCustomerAddedCardMaskContainer.setVisibility(View.GONE);
					mNonZouponsCustomerAddCardButton.setText("Add Card");
					mNonZouponsCustomerFirstName.setFocusable(true);
					mNonZouponsCustomerFirstName.setFocusableInTouchMode(true);
					mNonZouponsCustomerFirstName.setEnabled(true);
					mNonZouponsCustomerLastName.setFocusableInTouchMode(true);
					mNonZouponsCustomerLastName.setFocusable(true);
					mNonZouponsCustomerLastName.setEnabled(true);
					mNonZouponsCustomerEmailAddress.setFocusableInTouchMode(true);
					mNonZouponsCustomerEmailAddress.setFocusable(true);
					mNonZouponsCustomerEmailAddress.setEnabled(true);

				}else if(mGiftCardPurchaseCustomerPinLayout.getVisibility() == View.VISIBLE){
					mGiftcardPurchaseCustomerDetails.setVisibility(View.VISIBLE);
					mGiftCardPurchaseCustomerPinLayout.setVisibility(View.GONE);
					mGiftcardPurchaseCustomerPin.getText().clear();
					mGiftcardPurchaseEmployeePin.getText().clear();
					mGiftCardPurchaseEmployeePinLayout.setVisibility(View.VISIBLE);
				}else if(mGiftCardPurchaseChooseCardLayout.getVisibility() == View.VISIBLE){
					mGiftCardPurchaseChooseCardLayout.setVisibility(View.GONE);
					mGiftcardPurchaseCardUserPin.getText().clear();
					mGiftcardPurchaseCustomerPin.getText().clear();
					mGiftCardPurchaseCustomerPinLayout.setVisibility(View.VISIBLE);
				}else{

				}
			}
		});

		mGiftcardPurchaseAddCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ManageCardAddPin_ClassVariables.mEditCardFlag="false";
				ManageCardAddPin_ClassVariables.mAddPinFlag="true";
				WebServiceStaticArrays.mCardOnFiles.clear();
				Intent intent_addcard = new Intent(StoreOwnerGiftcardSell.this,AddCreditCard.class);
				intent_addcard.putExtra("class_name", "StoreOwner_PointOfSale");
				intent_addcard.putExtra("user_id", mGiftcardPurchaseCustomerFirstName.getTag().toString());
				startActivityForResult(intent_addcard,100);
			}
		});

		mGiftcardPurchaseChoosecardContextImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openContextMenu(v);	
			}
		});

		mGiftcardPurchaseChoosedCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openContextMenu(mGiftcardPurchaseChoosecardContextImage);
			}
		});

		mNonZouponsCustomerAddCardButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mNonZouponsCustomerFirstName.getText().toString().length()==0){
					alertBox_service("Information", "Please enter first name", mNonZouponsCustomerFirstName);
				}else if(mNonZouponsCustomerLastName.getText().toString().length()==0){
					alertBox_service("Information", "Please enter last name", mNonZouponsCustomerLastName);
				}else if(mNonZouponsCustomerEmailAddress.getText().toString().length()==0){
					alertBox_service("Information", "Please enter email id", mNonZouponsCustomerEmailAddress);
				}else if(!Patterns.EMAIL_ADDRESS.matcher(mNonZouponsCustomerEmailAddress.getText().toString()).matches()){
					alertBox_service("Information", "Please enter valid email address", mNonZouponsCustomerEmailAddress);
				}else{
					if(mNonZouponsCustomerAddCardButton.getText().toString().equalsIgnoreCase("Edit card")){
						ManageCardAddPin_ClassVariables.mEditCardFlag="true";
						ManageCardAddPin_ClassVariables.mAddPinFlag="false";
						CardId_ClassVariable.cardid = mNonZouponsMemberCardID;
						EditCardDetails_ClassVariables.cardName = mNonZouponsMemberCardName;
						EditCardDetails_ClassVariables.cardNumber = mNonZouponsMemberCardMask;
						EditCardDetails_ClassVariables.cvv = mNonZouponsMemberCardCVV;
						EditCardDetails_ClassVariables.expiryYear = mNonZouponsMemberCardexpiryYear;
						EditCardDetails_ClassVariables.expiryMonth = mNonZouponsMemberCardexpiryMonth;
						EditCardDetails_ClassVariables.StreetAddress = mNonZouponsMemberStreetAddress;
						EditCardDetails_ClassVariables.zipCode = mNonZouponsMemberZipcode;

						Intent intent_addcard = new Intent(StoreOwnerGiftcardSell.this,AddCreditCard.class);
						intent_addcard.putExtra("class_name", "StoreOwner_PointOfSale");
						intent_addcard.putExtra("user_id", mNonZouponsMemberUserId);
						startActivityForResult(intent_addcard,300);
					}else{
						ManageCardAddPin_ClassVariables.mEditCardFlag="false";
						ManageCardAddPin_ClassVariables.mAddPinFlag="true";
						ValidateCustomerUsingMobileNumber mUpdateUserTask = new ValidateCustomerUsingMobileNumber(StoreOwnerGiftcardSell.this,mGiftcardPurchaseMobileNumber.getText().toString(),mNonZouponsCustomerFirstName.getText().toString(), mNonZouponsCustomerLastName.getText().toString(), mNonZouponsCustomerEmailAddress.getText().toString(),"");	
						mUpdateUserTask.execute("update_userdetails");	
					}

				}	

			}
		});

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if(WebServiceStaticArrays.mCardOnFiles.size()>0){
			menu.setHeaderTitle("Select card");
			for(int i=0;i<WebServiceStaticArrays.mCardOnFiles.size();i++){
				final CardOnFiles_ClassVariables parsedobjectvalues = (CardOnFiles_ClassVariables) WebServiceStaticArrays.mCardOnFiles.get(i);
				if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("Search type or term not available !")*/){
					alertBox_service("Information","Please add a credit card for payment.",null);
				}else{
					menu.add(0, Integer.parseInt(parsedobjectvalues.cardId), 0, parsedobjectvalues.cardMask);
				}
			}
		}else{
			Toast.makeText(getApplicationContext(), "No Credit Cards Available", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getGroupId() == 0){
			mGiftcardPurchaseChoosedCard.setText(item.getTitle());
			mChoosedCreditcardId = String.valueOf(item.getItemId());
		}
		return super.onContextItemSelected(item);
	}

	// To update credit cards layout
	public void updateCardLayout(boolean isCardAvailable) {
		mGiftCardPurchaseChooseCardLayout.setVisibility(View.VISIBLE);
		mGiftCardPurchaseCustomerPinLayout.setVisibility(View.GONE);
		if(isCardAvailable){
			mGiftcardPurchaseAddCardContainer.setVisibility(View.GONE);
			mGiftcardPurchaseListCardContainer.setVisibility(View.VISIBLE);
			if(WebServiceStaticArrays.mCardOnFiles.size()==1){
				final CardOnFiles_ClassVariables parsedobjectvalues = (CardOnFiles_ClassVariables) WebServiceStaticArrays.mCardOnFiles.get(0);
				mGiftcardPurchaseChoosedCard.setText(parsedobjectvalues.cardMask);
				mChoosedCreditcardId = String.valueOf(parsedobjectvalues.cardId);
			}
		}else{
			mGiftcardPurchaseAddCardContainer.setVisibility(View.VISIBLE);
			mGiftcardPurchaseListCardContainer.setVisibility(View.GONE);
		}
	}

	public void PurchaseCardForNewCustomer() {
		// TODO Auto-generated method stub
		SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
		String location_id = mPrefs.getString("location_id", "");
		String store_id = mPrefs.getString("store_id", "");
		NonZouponsCustomerGiftcardSellTask mSellCardTask = new NonZouponsCustomerGiftcardSellTask(StoreOwnerGiftcardSell.this,mNonZouponsMemberUserId,mNonZouponsMemberCardID,mCardId,mCardValue,store_id , mCardType,location_id, mCardFaceValue);
		mSellCardTask.execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 100){ // After adding credit card
			mGiftcardPurchaseAddCardContainer.setVisibility(View.GONE);
			mGiftcardPurchaseListCardContainer.setVisibility(View.VISIBLE);
			mChoosedCreditcardId = data.getExtras().getString("card_id");
			mGiftcardPurchaseChoosedCard.setText(data.getExtras().getString("card_masknumber"));
			// Adding to arraylist to load it in context menu			
			CardOnFiles_ClassVariables mCardDetails = new CardOnFiles_ClassVariables();
			mCardDetails.cardId = mChoosedCreditcardId;
			mCardDetails.cardMask = data.getExtras().getString("card_masknumber");
			WebServiceStaticArrays.mCardOnFiles.add(mCardDetails);
		}else if(requestCode == 300 && resultCode == RESULT_OK){ // From Add card 
			// Returning values for use in edit card details..
			mNonZouponsMemberUserId = data.getExtras().getString("user_id");
			mNonZouponsMemberCardID = data.getExtras().getString("card_id");
			mNonZouponsMemberCardName = data.getExtras().getString("card_name");
			mNonZouponsMemberCardMask = data.getExtras().getString("card_masknumber");
			mNonZouponsMemberCardexpiryMonth = data.getExtras().getString("card_expirymonth");
			mNonZouponsMemberCardexpiryYear = data.getExtras().getString("card_expiryyear");
			mNonZouponsMemberCardCVV = data.getExtras().getString("card_cvv");
			mNonZouponsMemberStreetAddress = data.getExtras().getString("streetnumber");
			mNonZouponsMemberZipcode = data.getExtras().getString("zipcode");
			mNonZouponsCustomerAddedCardMaskContainer.setVisibility(View.VISIBLE);
			mNonZouponsCustomerCardMask.setText(mNonZouponsMemberCardMask);
			mNonZouponsCustomerAddCardButton.setText("Edit Card");
			mGiftCardPurchaseEmployeePinLayout.setVisibility(View.VISIBLE);
			mNonZouponsCustomerFirstName.setFocusable(false);
			mNonZouponsCustomerFirstName.setFocusableInTouchMode(false);
			mNonZouponsCustomerFirstName.setEnabled(false);
			mNonZouponsCustomerLastName.setFocusableInTouchMode(false);
			mNonZouponsCustomerLastName.setFocusable(false);
			mNonZouponsCustomerLastName.setEnabled(false);
			mNonZouponsCustomerEmailAddress.setFocusableInTouchMode(false);
			mNonZouponsCustomerEmailAddress.setFocusable(false);
			mNonZouponsCustomerEmailAddress.setEnabled(false);

		}
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
		// TODO Auto-generated method stub
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwnerGiftcardSell.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}	

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		new CheckUserSession(StoreOwnerGiftcardSell.this).checkIfSessionExpires();
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwnerGiftcardSell.this,ZouponsConstants.sStoreModuleFlag);
		mNotificationSync.setRecurringAlarm();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwnerGiftcardSell.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
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

	/* To show alert pop up with respective message */
	private void alertBox_service(String title, final String msg,final EditText editText) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwnerGiftcardSell.this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setCancelable(true);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(editText != null)
					editText.requestFocus();
			}
		});
		service_alert.show();
	}

	


}


