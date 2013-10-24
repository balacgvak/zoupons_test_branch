package com.us.zoupons.storeowner.GiftCards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
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

import com.us.zoupons.AddCardInformation;
import com.us.zoupons.MobileNumberFilter;
import com.us.zoupons.MobileNumberTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.CardOnFiles_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;
import com.us.zoupons.storeowner.invoice.CheckZouponsCustomerTask;

public class StoreOwnerGiftcardSell extends Activity{

	public static String TAG="StoreOwnerGiftCardsPurchase";
	public static MyHorizontalScrollView scrollView;
	View app;
	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	Header header;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;
	View mLeftMenu;
	int mMenuFlag;
	StoreOwner_LeftMenu storeowner_leftmenu;
	ImageView mRightMenuHolder,mGiftcardPurchaseCustomerImage,mGiftcardPurchaseImage,mGiftcardPurchaseChoosecardContextImage;
	Button mStoreOwnerGiftCardsPurchaseFreezeView;
	private RelativeLayout mStoreInfoHeader;
	private LinearLayout mGiftcardPurchaseMobileNumberLayout,mGiftCardPurchaseEmployeePinLayout,mGiftCardPurchaseCustomerPinLayout,mGiftCardPurchaseChooseCardLayout,mMenuSplitter,mFooterView,
							mGiftcardPurchaseAddCardContainer,mGiftcardPurchaseListCardContainer;
	private EditText mGiftcardPurchaseMobileNumber,mGiftcardPurchaseEmployeePin,mGiftcardPurchaseCustomerPin,mGiftcardPurchaseChoosedCard,mGiftcardPurchaseCardUserPin;
	private TextView mBackMenu,mGiftcardPurchaseStoreName,mGiftcardPurchaseFaceValue,mGiftcardPurchaseCustomerFirstName,mGiftcardPurchaseCustomerLastName;
	private Button mMobileNumberSubmitButton/*,mPurchaseGiftcardSubmitButton*/,mPurchaseGiftcardEmployeePinButton,mPurchaseGiftcardCustomerPinButton,mPurchaseGiftcardProcessOrderButton,mGiftcardPurchaseAddCard;
	private ScrollView mGiftcardPurchaseCustomerDetails;
    private String mCardType="",mCardId="",mCardFaceValue="",mCardValue="",mChoosedCreditcardId="";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();
		app = inflater.inflate(R.layout.storeowner_giftcards_sell, null);
		ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.storeownergifcards_purchase_container);
		mFooterView = (LinearLayout) app.findViewById(R.id.storeownergifcards_purchase_footerLayoutId);
		mStoreInfoHeader = (RelativeLayout) mMiddleView.findViewById(R.id.storeownergifcards_purchase_storename_header);
		mRightMenuHolder = (ImageView) mStoreInfoHeader.findViewById(R.id.storeownergifcards_purchase_rightmenu);
		mStoreOwnerGiftCardsPurchaseFreezeView = (Button) app.findViewById(R.id.storeownergifcards_purchase_freeze);
		storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwnerGiftcardSell.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerGiftCardsPurchaseFreezeView, TAG);
		mRightMenu = storeowner_rightmenu.intializeInflater();
		storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwnerGiftcardSell.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwnerGiftCardsPurchaseFreezeView, TAG);
		mLeftMenu = storeowner_leftmenu.intializeInflater();
		storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
		storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
		/* Header Tab Bar which contains logout,notification and home buttons*/
		header = (Header) app.findViewById(R.id.storeownergifcards_purchase_header);
		header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwnerGiftCardsPurchaseFreezeView, TAG);
		final View[] children = new View[] { mLeftMenu, app};
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
		// Footer layout view initialization
		mBackMenu = (TextView) mFooterView.findViewById(R.id.storeownergifcards_purchase_BackId);
		mMenuSplitter = (LinearLayout) mFooterView.findViewById(R.id.menubar_splitter1);
		// Store name and right menu holder layout initialization
		mGiftcardPurchaseStoreName = (TextView) mStoreInfoHeader.findViewById(R.id.storeownergifcards_purchase_storetitle_textId); 
		// Initial mobile number view declaration
		mGiftcardPurchaseMobileNumberLayout = (LinearLayout) mMiddleView.findViewById(R.id.storeownergifcards_purchase_mobilenumber_layout);
		mGiftcardPurchaseMobileNumber =  (EditText) mGiftcardPurchaseMobileNumberLayout.findViewById(R.id.storeownergifcards_purchase_mobilenumber);
		mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwnerGiftCardsPurchaseFreezeView, TAG,mGiftcardPurchaseMobileNumber, false));
		mStoreOwnerGiftCardsPurchaseFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerGiftCardsPurchaseFreezeView, TAG,mGiftcardPurchaseMobileNumber, true));
		mMobileNumberSubmitButton = (Button) mGiftcardPurchaseMobileNumberLayout.findViewById(R.id.storeownergifcards_purchase_mobileNumber_Submit);
		// Customer details view declaration
		mGiftcardPurchaseCustomerDetails = (ScrollView) mMiddleView.findViewById(R.id.storeownergifcards_purchase_scrollview);
		mGiftcardPurchaseCustomerImage = (ImageView) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_customerImage);
		mGiftcardPurchaseCustomerFirstName = (TextView) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_customerFirstName);
		mGiftcardPurchaseCustomerLastName = (TextView) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_customerLastName);
		mGiftcardPurchaseImage = (ImageView) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_GiftcardImage);
		mGiftcardPurchaseFaceValue = (TextView) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_GiftcardFaceValue);
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
		mGiftcardPurchaseChoosecardContextImage = 	(ImageView) mGiftCardPurchaseChooseCardLayout.findViewById(R.id.storeownergifcards_purchase_cardcontextmenu);
		registerForContextMenu(mGiftcardPurchaseChoosecardContextImage);
		mGiftcardPurchaseCardUserPin =(EditText) mGiftCardPurchaseChooseCardLayout.findViewById(R.id.storeownergifcards_purchase_userPin);
		mPurchaseGiftcardEmployeePinButton = (Button) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_employeepinbutton);
		mPurchaseGiftcardCustomerPinButton = (Button) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_customerpinbutton);
		mPurchaseGiftcardProcessOrderButton = (Button) mGiftcardPurchaseCustomerDetails.findViewById(R.id.storeownergifcards_purchase_processorderbutton);
		// To validate Mobile Number field
		mGiftcardPurchaseMobileNumber.addTextChangedListener(new MobileNumberTextWatcher());
		mGiftcardPurchaseMobileNumber.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});
		if(getIntent().hasExtra("card_id") && getIntent().hasExtra("facevalue") && getIntent().hasExtra("facevalue") && getIntent().hasExtra("facevalue")){
			mCardType = getIntent().getExtras().getString("card_type");
			mCardId = getIntent().getExtras().getString("card_id");
			mCardFaceValue = getIntent().getExtras().getString("facevalue");
			mCardValue = getIntent().getExtras().getString("cardvalue");
			mGiftcardPurchaseFaceValue.setText(String.format("%.2f",Double.parseDouble(mCardFaceValue)));
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
						CheckZouponsCustomerTask customerTask = new CheckZouponsCustomerTask(StoreOwnerGiftcardSell.this, mGiftcardPurchaseMobileNumberLayout, mGiftcardPurchaseCustomerDetails, mGiftcardPurchaseCustomerImage, mFooterView,mStoreInfoHeader,mGiftcardPurchaseCustomerFirstName,mGiftcardPurchaseCustomerLastName);
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
					SecurityPinCheckTask mCheckPinTask = new SecurityPinCheckTask(StoreOwnerGiftcardSell.this, mGiftCardPurchaseEmployeePinLayout, mGiftCardPurchaseCustomerPinLayout);
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
				if(mGiftcardPurchaseCardUserPin.getText().toString().trim().length()==0){
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
				}else if(mGiftCardPurchaseEmployeePinLayout.getVisibility() == View.VISIBLE){
					mGiftcardPurchaseMobileNumberLayout.setVisibility(View.VISIBLE);
					mGiftcardPurchaseMobileNumber.getText().clear();
					mGiftcardPurchaseCustomerDetails.setVisibility(View.GONE);
					mStoreInfoHeader.setVisibility(View.GONE);
				}else if(mGiftCardPurchaseCustomerPinLayout.getVisibility() == View.VISIBLE){
					mGiftcardPurchaseCustomerDetails.setVisibility(View.VISIBLE);
					mGiftCardPurchaseCustomerPinLayout.setVisibility(View.GONE);
					mGiftCardPurchaseEmployeePinLayout.setVisibility(View.VISIBLE);
				}else if(mGiftCardPurchaseChooseCardLayout.getVisibility() == View.VISIBLE){
					mGiftCardPurchaseChooseCardLayout.setVisibility(View.GONE);
					mGiftCardPurchaseCustomerPinLayout.setVisibility(View.VISIBLE);
				}else if(mGiftcardPurchaseMobileNumberLayout.getVisibility() == View.VISIBLE){
					finish();
				}
			}
		});
		
		mGiftcardPurchaseAddCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent_addcard = new Intent(StoreOwnerGiftcardSell.this,AddCardInformation.class);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 100){
			mGiftcardPurchaseAddCardContainer.setVisibility(View.GONE);
			mGiftcardPurchaseListCardContainer.setVisibility(View.VISIBLE);
			mChoosedCreditcardId = data.getExtras().getString("card_id");
			mGiftcardPurchaseChoosedCard.setText(data.getExtras().getString("card_masknumber"));
			// Adding to arraylist to load it in context menu			
			CardOnFiles_ClassVariables mCardDetails = new CardOnFiles_ClassVariables();
			mCardDetails.cardId = mChoosedCreditcardId;
			mCardDetails.cardMask = data.getExtras().getString("card_masknumber");
			WebServiceStaticArrays.mCardOnFiles.add(mCardDetails);
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
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


