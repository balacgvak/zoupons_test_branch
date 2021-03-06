package com.us.zoupons.storeowner.invoice;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.AmountTextWatcher;
import com.us.zoupons.Base64;
import com.us.zoupons.DecodeImageWithRotation;
import com.us.zoupons.MobileNumberFilter;
import com.us.zoupons.MobileNumberTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.android.lib.integrator.IntentIntegrator;
import com.us.zoupons.android.lib.integrator.IntentResult;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.shopper.cards.ImageLoader;
import com.us.zoupons.shopper.invoice.POJOInvoiceList;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

/**
 * 
 * Activity to host raise invoice to customer functionalities, listing outstanding and processed invoice
 *
 */

public class InvoiceCenter extends Activity implements OnClickListener{

	// Initializing views and variables
	public String TAG="StoreOwnerInvoiceCenter";
	private MyHorizontalScrollView mScrollView;
	private View mApp;
	private Header mZouponsHeader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private View mLeftMenu;
	private int mMenuFlag;
	private View mRightMenu;
	private LinearLayout mStoreOwnerInvoiceTelephoneLayout,mStoreOwnerCustomerInvoiceEmailLayout,mStoreOwnerListViewHeaderLayout;
	private LinearLayout mFooterAddInvoiceLayout,mFooterOutstandingInvoice,mFooterProcessedInvoice,mInvoiceDetailsLayout,mInvoiceDetailsNotesLayout,mCreditCardAmountLayout,mGiftCardUsedLayout,mGiftCardAmountLayout,mInvoiceDetailsSingleCardUsedLayout,mInvoiceListDateContainer;
	private TextView mFooterBackText,mRightFooterText,mAddNotes,mInvoiceListHeader,mInvoiceDetailsCustomerFirstName,mInvoiceDetailsCustomerLastName,mStoreOwnerCustomerInvoiceFirstNameText,mStoreOwnerCustomerInvoiceLastNameText;
	private ImageView mInvoiceCenterRightMenuImage,mFooterBackImage,mRightFooterImage,mInvoiceDetailsNotesCloseImage;
	private ScrollView mStoreOwnerInvoiceCustomerDetailsLayout;
	private Button mStoreOwnerInvoiceCenterFreezeView,mStoreOwnerInvoiceTelephoneSubmit,mStoreOwnerCustomerInvoiceSubmit,mInvoiceSearchCancelButton,mInvoiceDetailsNotesButton;
	private EditText mStoreOwnerInvoiceTelephoneValue,mStoreOwnerCustomerInvoiceFirstName,mStoreOwnerCustomerInvoiceLastName,mStoreOwnerCustomerInvoiceEmail,mStoreOwnerCustomerInvoiceAmount,mStoreOwnerCustomerInvoiceCouponCode,/*mStoreOwnerCustomerInvoiceNotes,*/mStoreOwnerCustomerInvoicePinCode,
	mInvoiceSearchValue,mStoreInvoiceNotes,mCustomerInvoiceNotes,mInvoiceListFromDate,mInvoiceListToDate;
	private ImageView mStoreOwnerInvoiceCustomerImage,mStoreOwnerCustomerInvoiceCouponImage,mInvoiceDetailsCustomerImage,mInvoiceListFromDateContextImage,mInvoiceListToDateContextImage;
	private String mProfilePhoto,mStoreOwnerNotes="",mSelectedFromDate="",mSelectedToDate="",mClassname="";
	private ListView mStoreOwnerInvoiceListView;
	private View mFirstCardBorder,mSecondCardBorder;
	private RelativeLayout mInvoiceSearchContainer,mViewInvoiceDetailsContainer;
	private TextView mInvoiceStoreName,mInvoiceDate,mInvoiceDateLabel,mInvoiceTotalAmount,mInvoiceTip,mInvoiceAmount,mInvoiceCardUsed,mInvoiceCreditCardAmount,mInvoiceGiftCardUsed,mInvoiceGiftCardAmount;
	private ArrayList<Object> mTempInvoiceList,mSearchedInvoiceList;
	private RelativeLayout mInvoiceCenterHeaderContainer,mInvoiceCustomerDetailsContainer;
	private TextView mInvoiceCenterHeaderText;
	//Logout without user interaction after 5 minute
	CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			LayoutInflater inflater = LayoutInflater.from(this);
			mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(mScrollView);
			mApp = inflater.inflate(R.layout.store_owner_invoice, null);
			ViewGroup mMiddleView = (ViewGroup) mApp.findViewById(R.id.storeownerinvoice_middleview);
			final ViewGroup mFooterView = (ViewGroup) mApp.findViewById(R.id.storeownerinvoice_footer);
			mInvoiceCenterHeaderContainer = (RelativeLayout) mMiddleView.findViewById(R.id.storeownerinvoice_header_container);
			mInvoiceCenterRightMenuImage = (ImageView) mMiddleView.findViewById(R.id.storeownerinvoice_rightmenu);
			mInvoiceCenterHeaderText = (TextView) mMiddleView.findViewById(R.id.storeownerinvoice_headerText);
			mStoreOwnerInvoiceCenterFreezeView = (Button) mApp.findViewById(R.id.storeownerinvoice_freezeview);
			mStoreownerLeftmenu = new StoreOwner_LeftMenu(InvoiceCenter.this,mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerInvoiceCenterFreezeView, TAG);
			mLeftMenu = mStoreownerLeftmenu.intializeInflater();
			mStoreownerLeftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			mStoreownerRightmenu = new StoreOwner_RightMenu(InvoiceCenter.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerInvoiceCenterFreezeView, TAG);
			mRightMenu = mStoreownerRightmenu.intializeCustomerCenterInflater();
			mStoreownerRightmenu.customercentermenuClickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsHeader = (Header) mApp.findViewById(R.id.storeownerinvoice_header);
			mZouponsHeader.intializeInflater(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerInvoiceCenterFreezeView, TAG);
			final View[] children;
			if(getIntent().hasExtra("FromCustomerCenter")){
				children = new View[] { mLeftMenu, mApp,mRightMenu};	
			}else{
				children = new View[] { mLeftMenu, mApp};
			}
			/* Scroll to app (view[1]) when layout finished.*/
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsHeader.mLeftMenuBtnSlide));
			mInvoiceCenterRightMenuImage.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag=2, mStoreOwnerInvoiceCenterFreezeView, TAG,mStoreOwnerCustomerInvoiceAmount,mStoreOwnerCustomerInvoicePinCode,false));
			mStoreOwnerInvoiceCenterFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerInvoiceCenterFreezeView, TAG,mStoreOwnerCustomerInvoiceAmount,mStoreOwnerCustomerInvoicePinCode,true));
			mNotificationReceiver = new NotifitcationReceiver(mZouponsHeader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mTempInvoiceList = new ArrayList<Object>();
			mSearchedInvoiceList = new ArrayList<Object>();
			// Initialization of invoice center initial telephone entry views
			mStoreOwnerInvoiceTelephoneLayout = (LinearLayout) mMiddleView.findViewById(R.id.initial_telephone_layout);
			mStoreOwnerInvoiceTelephoneValue = (EditText) mStoreOwnerInvoiceTelephoneLayout.findViewById(R.id.storeowner_invoice_phoneNumberId);
			mStoreOwnerInvoiceTelephoneSubmit = (Button) mStoreOwnerInvoiceTelephoneLayout.findViewById(R.id.storeowner_invoice_phoneNumber_submit);
			// Initialization of invoice center customer details views
			mStoreOwnerInvoiceCustomerDetailsLayout = (ScrollView) mMiddleView.findViewById(R.id.storeowner_customer_invoice_scrollview);
			mInvoiceCustomerDetailsContainer = (RelativeLayout) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_invoice_customer_details_container);
			mStoreOwnerCustomerInvoiceFirstName = (EditText) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_FirstName);
			mStoreOwnerCustomerInvoiceFirstNameText =  (TextView) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_FirstNameText);
			mStoreOwnerCustomerInvoiceLastName = (EditText) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_LastName);
			mStoreOwnerCustomerInvoiceLastNameText = (TextView) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_LastNameText);
			mStoreOwnerCustomerInvoiceEmailLayout = (LinearLayout) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_EmailLayout);
			mStoreOwnerCustomerInvoiceEmail = (EditText) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_EmailAddress);
			mStoreOwnerCustomerInvoiceAmount = (EditText) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_Amount);
			mStoreOwnerCustomerInvoiceAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
			//mAmountValue.setFilters(new InputFilter[]{new com.us.zoupons.DecimalDigitsInputFilter(10, 2)});
			mStoreOwnerCustomerInvoiceAmount.addTextChangedListener(new AmountTextWatcher(mStoreOwnerCustomerInvoiceAmount));
			mStoreOwnerCustomerInvoiceCouponCode = (EditText) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_couponCode);
			//mStoreOwnerCustomerInvoiceNotes = (EditText) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_Notes);
			mAddNotes =  (TextView) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.invoice_add_notes);
			mStoreOwnerCustomerInvoicePinCode = (EditText) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_pincode);
			mStoreOwnerCustomerInvoicePinCode.setInputType(InputType.TYPE_CLASS_NUMBER);
			mStoreOwnerCustomerInvoicePinCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
			mStoreOwnerInvoiceCustomerImage = (ImageView) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_ImageId);
			mStoreOwnerCustomerInvoiceCouponImage =  (ImageView) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_couponcode_scanner);
			mStoreOwnerCustomerInvoiceSubmit = (Button) mStoreOwnerInvoiceCustomerDetailsLayout.findViewById(R.id.storeowner_customer_invoice_submit);
			//Invoice List Date contanier
			mInvoiceListDateContainer = (LinearLayout) mMiddleView.findViewById(R.id.invoicelist_date_container);
			mInvoiceListFromDate = (EditText) mMiddleView.findViewById(R.id.invoicelist_fromdate_value);
			mInvoiceListFromDate.setTag("FromDate");
			mInvoiceListFromDate.setOnClickListener(this);
			mInvoiceListToDate = (EditText) mMiddleView.findViewById(R.id.invoicelist_Todate_value);
			mInvoiceListToDate.setTag("ToDate");
			mInvoiceListToDate.setOnClickListener(this);
			mInvoiceListFromDateContextImage = (ImageView) mMiddleView.findViewById(R.id.invoicelist_fromdate_contextImage);
			mInvoiceListFromDateContextImage.setTag("FromDate");
			mInvoiceListFromDateContextImage.setOnClickListener(this);
			mInvoiceListToDateContextImage = (ImageView) mMiddleView.findViewById(R.id.invoicelist_Todate_contextImage);
			mInvoiceListToDateContextImage.setTag("ToDate");
			mInvoiceListToDateContextImage.setOnClickListener(this);
			// Invoice Search Container
			mInvoiceSearchContainer = (RelativeLayout) mMiddleView.findViewById(R.id.invoice_search_layout);
			mInvoiceSearchValue = (EditText) mInvoiceSearchContainer.findViewById(R.id.invoice_searchId);
			mInvoiceSearchCancelButton = (Button) mInvoiceSearchContainer.findViewById(R.id.invoice_search_buttonId);
			// Invoice processed and outstanding listview
			mStoreOwnerListViewHeaderLayout = (LinearLayout) mMiddleView.findViewById(R.id.listview_header_layout);
			mInvoiceListHeader = (TextView) mMiddleView.findViewById(R.id.invoice_listview_header_text);
			mStoreOwnerInvoiceListView = (ListView) mMiddleView.findViewById(R.id.storeowneinvoice_ListView);
			// Invoice details views initialisation 
			mViewInvoiceDetailsContainer = (RelativeLayout) mApp.findViewById(R.id.view_invoicedetails_container);
			mInvoiceDetailsCustomerImage = (ImageView) mViewInvoiceDetailsContainer.findViewById(R.id.invoice_details_customer_ImageId);
			mInvoiceDetailsCustomerFirstName = (TextView) mViewInvoiceDetailsContainer.findViewById(R.id.invoice_details_customer_firstNameId);
			mInvoiceDetailsCustomerLastName = (TextView) mViewInvoiceDetailsContainer.findViewById(R.id.invoice_details_customer_lastNameId);
			mInvoiceStoreName = (TextView) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_storenameId);
			mInvoiceDetailsLayout = (LinearLayout) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoice_details_layout);
			mInvoiceDate = (TextView) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_dateId);
			mInvoiceDateLabel = (TextView) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_dateLabelId);
			mInvoiceAmount = (TextView) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_AmountId);
			mInvoiceTip = (TextView) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_tipId);
			mInvoiceTotalAmount = (TextView) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_totalChargeId);
			mInvoiceDetailsSingleCardUsedLayout = (LinearLayout) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_singlecardTypeLayoutId);
			mInvoiceCardUsed = (TextView) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_cardTypeId);
			mFirstCardBorder = mViewInvoiceDetailsContainer.findViewById(R.id.firstcardtype_borderId);
			mSecondCardBorder = mViewInvoiceDetailsContainer.findViewById(R.id.Secondcardtype_borderId);
			mCreditCardAmountLayout =(LinearLayout) mViewInvoiceDetailsContainer.findViewById(R.id.creditcardAmountLayoutId);
			mGiftCardUsedLayout = (LinearLayout) mViewInvoiceDetailsContainer.findViewById(R.id.giftcardUsedLayoutId);
			mGiftCardAmountLayout =(LinearLayout) mViewInvoiceDetailsContainer.findViewById(R.id.giftcardAmountLayoutId);
			mInvoiceCreditCardAmount = (TextView) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_creditCardAmountId);
			mInvoiceGiftCardUsed = (TextView) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_GiftCardId);
			mInvoiceGiftCardAmount = (TextView) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_GiftCardAmountId);
			mInvoiceDetailsNotesLayout = (LinearLayout) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_notes_container);
			mInvoiceDetailsNotesButton = (Button) mViewInvoiceDetailsContainer.findViewById(R.id.invoicedetails_viewnotes);
			mInvoiceDetailsNotesCloseImage = (ImageView) mViewInvoiceDetailsContainer.findViewById(R.id.view_invoicedetails_notes_closeImageId);
			mCustomerInvoiceNotes = (EditText) mViewInvoiceDetailsContainer.findViewById(R.id.customer_invoicedetails_descriptionId);
			mStoreInvoiceNotes = (EditText) mViewInvoiceDetailsContainer.findViewById(R.id.store_invoicedetails_descriptionId);
			// Footer views 
			mFooterAddInvoiceLayout = (LinearLayout) mFooterView.findViewById(R.id.storeownerinvoice_footer_addinvoice);
			mFooterOutstandingInvoice = (LinearLayout) mFooterView.findViewById(R.id.storeownerinvoice_footer_outstandingInvoice);
			mFooterProcessedInvoice = (LinearLayout) mFooterView.findViewById(R.id.storeownerinvoice_footer_processed);
			mFooterBackImage = (ImageView) mFooterView.findViewById(R.id.storeownerinvoice_footer_back_image);
			mFooterBackText = (TextView) mFooterView.findViewById(R.id.storeownerinvoice_footer_back_text);
			mRightFooterText = (TextView) mFooterView.findViewById(R.id.storeownerinvoice_footer_processed_text);
			mRightFooterImage = (ImageView) mFooterView.findViewById(R.id.storeownerinvoice_footer_processed_image);
			mFooterAddInvoiceLayout.setBackgroundResource(R.drawable.footer_dark_blue_new);
			mFooterProcessedInvoice.setBackgroundResource(R.drawable.header_2); 
			// To set default image data
			Bitmap mBitmapProfileImage = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage); 
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			mBitmapProfileImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
			byte[] Photo=stream.toByteArray();
			mProfilePhoto = Base64.encodeBytes(Photo);
			// To validate Mobile Number field
			mStoreOwnerInvoiceTelephoneValue.addTextChangedListener(new MobileNumberTextWatcher());
			mStoreOwnerInvoiceTelephoneValue.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});

			if(getIntent().hasExtra("FromCustomerCenter")){ // Navigation from customer center
				mClassname = "CustomerCenter"; 
				mInvoiceCenterHeaderContainer.setVisibility(View.VISIBLE);
				mInvoiceCenterHeaderText.setText(StoreOwner_RightMenu.mCustomer_FirstName+" "+StoreOwner_RightMenu.mCustomer_LastName);
				mStoreOwnerInvoiceTelephoneLayout.setVisibility(View.GONE);
				mStoreOwnerInvoiceCustomerDetailsLayout.setVisibility(View.VISIBLE);
				mFooterView.setVisibility(View.VISIBLE);
				mStoreOwnerCustomerInvoiceEmailLayout.setVisibility(View.GONE);
				mStoreOwnerCustomerInvoiceFirstNameText.setVisibility(View.VISIBLE);
				mStoreOwnerCustomerInvoiceLastNameText.setVisibility(View.VISIBLE);
				mStoreOwnerCustomerInvoiceFirstName.setVisibility(View.GONE);
				mStoreOwnerCustomerInvoiceLastName.setVisibility(View.GONE);
				mStoreOwnerCustomerInvoiceFirstName.setTag(StoreOwner_RightMenu.mCustomer_id);  // To use it during submit invoice....
				mStoreOwnerCustomerInvoiceFirstNameText.setText(StoreOwner_RightMenu.mCustomer_FirstName);
				mStoreOwnerCustomerInvoiceLastNameText.setText(StoreOwner_RightMenu.mCustomer_LastName);
				mStoreOwnerCustomerInvoiceAmount.requestFocus();
				unregisterForContextMenu(mStoreOwnerInvoiceCustomerImage);
				ImageLoader imageLoader = new ImageLoader(InvoiceCenter.this);
				imageLoader.DisplayImage(StoreOwner_RightMenu.mCustomer_ProfileImage+"&w="+120+"&h="+135+"&zc=0", mStoreOwnerInvoiceCustomerImage);
				mFooterView.setVisibility(View.GONE);
			}else{ // From Invoice Center in Sliding menu
				mInvoiceCenterHeaderContainer.setVisibility(View.GONE);
				mFooterView.setVisibility(View.VISIBLE);
				mClassname = "Invoice";
			}

			mStoreOwnerInvoiceTelephoneSubmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mStoreOwnerInvoiceTelephoneValue.getText().toString().trim().length() == 0){
						alertBox_service("Information","Please enter mobile number",mStoreOwnerInvoiceTelephoneValue);
					}else if(mStoreOwnerInvoiceTelephoneValue.getText().toString().trim().length() != 12){
						alertBox_service("Information","Please enter valid mobile number",mStoreOwnerInvoiceTelephoneValue);						
					}else{
						if(new NetworkCheck().ConnectivityCheck(InvoiceCenter.this)){ // Network connectivity check
							CheckZouponsCustomerTask customerTask = new CheckZouponsCustomerTask(InvoiceCenter.this, mStoreOwnerInvoiceTelephoneLayout, mStoreOwnerInvoiceCustomerDetailsLayout,mInvoiceCustomerDetailsContainer,mStoreOwnerInvoiceCustomerImage,mStoreOwnerCustomerInvoiceFirstName,mStoreOwnerCustomerInvoiceLastName,mStoreOwnerCustomerInvoiceFirstNameText,mStoreOwnerCustomerInvoiceLastNameText,mStoreOwnerCustomerInvoiceEmailLayout,mFooterView,mStoreOwnerCustomerInvoiceAmount);
							customerTask.execute(mStoreOwnerInvoiceTelephoneValue.getText().toString());
						}else{ 
							Toast.makeText(InvoiceCenter.this, "Network connection not available", Toast.LENGTH_SHORT).show();	
						}
					}
				}
			});

			mStoreOwnerCustomerInvoiceSubmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					boolean isToCheckNonZouponsCustomer = false;
					
					if(mStoreOwnerCustomerInvoiceEmailLayout.getVisibility() == View.VISIBLE){ // For Non Zoupons Customer
						if(mStoreOwnerCustomerInvoiceFirstName.getText().toString().length()==0){
							alertBox_service("Information", "Please enter first name", mStoreOwnerCustomerInvoiceFirstName);
						}else if(mStoreOwnerCustomerInvoiceLastName.getText().toString().length()==0){
							alertBox_service("Information", "Please enter last name", mStoreOwnerCustomerInvoiceLastName);
						}else if(mStoreOwnerCustomerInvoiceEmail.getText().toString().length()==0){
							alertBox_service("Information", "Please enter email id", mStoreOwnerCustomerInvoiceEmail);
						}else if(! Patterns.EMAIL_ADDRESS.matcher(mStoreOwnerCustomerInvoiceEmail.getText().toString()).matches()){
							alertBox_service("Information", "Please enter valid email id", mStoreOwnerCustomerInvoiceEmail);
						}else{
							isToCheckNonZouponsCustomer = true;	
						}
					}else{
						isToCheckNonZouponsCustomer = true;
					}
					
					if(isToCheckNonZouponsCustomer){
						if(mStoreOwnerCustomerInvoiceAmount.getText().toString().length() == 0){
							alertBox_service("Information", "Please enter invoice amount", mStoreOwnerCustomerInvoiceAmount);
						}else if(mStoreOwnerCustomerInvoiceAmount.getText().toString().equalsIgnoreCase("0.00")){
							alertBox_service("Information", "Please enter valid invoice amount", mStoreOwnerCustomerInvoiceAmount);
						}else if(mStoreOwnerCustomerInvoicePinCode.getText().toString().trim().length() == 0){
							alertBox_service("Information", "Please enter secret PIN to proceed", mStoreOwnerCustomerInvoicePinCode);
						}else if(mStoreOwnerCustomerInvoicePinCode.getText().toString().trim().length() < 4){
							alertBox_service("Information", "Please enter four digit secret PIN to proceed", mStoreOwnerCustomerInvoicePinCode);
						}else{// params[0] --> PIN // params[1] --> customer_id //params[2] -->amount //params[3] --> notes //params[4]--> coupon code
							if(mStoreOwnerCustomerInvoiceEmailLayout.getVisibility() == View.VISIBLE){ // For Non Zoupons Customer
									String mCustomerid = mStoreOwnerCustomerInvoiceFirstName.getTag().toString();
									String mCustomerStatus = mStoreOwnerCustomerInvoiceLastName.getTag().toString();
									NonMemberRaiseInvoiceTask mNonMenmberInvoice = new NonMemberRaiseInvoiceTask(InvoiceCenter.this,mStoreOwnerInvoiceTelephoneLayout,mStoreOwnerInvoiceCustomerDetailsLayout);
									mNonMenmberInvoice.execute(mStoreOwnerCustomerInvoiceAmount.getText().toString(),mStoreOwnerCustomerInvoiceCouponCode.getText().toString(),mStoreOwnerInvoiceTelephoneValue.getText().toString(),mCustomerid,mCustomerStatus,mStoreOwnerCustomerInvoiceFirstName.getText().toString(),mStoreOwnerCustomerInvoiceLastName.getText().toString(),mStoreOwnerCustomerInvoiceEmail.getText().toString(),mProfilePhoto,mStoreOwnerNotes);
							}else{ // For Zoupons Customer
								String mCustomerid = mStoreOwnerCustomerInvoiceFirstName.getTag().toString();
								SendInvoiceTask mSendInvoice = new SendInvoiceTask(InvoiceCenter.this,mStoreOwnerInvoiceTelephoneLayout,mStoreOwnerInvoiceCustomerDetailsLayout,"List",mClassname);
								mSendInvoice.execute(mStoreOwnerCustomerInvoicePinCode.getText().toString(),mCustomerid,mStoreOwnerCustomerInvoiceAmount.getText().toString(),mStoreOwnerNotes,mStoreOwnerCustomerInvoiceCouponCode.getText().toString(),"NonZouponsCustomer");	
							}
						}
					}else{
						
					}
					
					
				}
			});

			mAddNotes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Custom Dialog to show Store Note Details
					final Dialog mNotesDetails = new Dialog(InvoiceCenter.this);
					mNotesDetails.setTitle("Note Details");
					mNotesDetails.setContentView(R.layout.registration_mobiledetails);
					WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
					lWindowParams.copyFrom(mNotesDetails.getWindow().getAttributes());
					lWindowParams.width = WindowManager.LayoutParams.FILL_PARENT;
					lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
					mNotesDetails.getWindow().setAttributes(lWindowParams);
					final EditText mDialogNotesValue = (EditText) mNotesDetails.findViewById(R.id.notes_value);
					mDialogNotesValue.setText(mStoreOwnerNotes); // To set previous note details value
					mDialogNotesValue.setSelection(mStoreOwnerNotes.trim().length());
					Button mOkButton = (Button)mNotesDetails.findViewById(R.id.notes_save_buttonId);
					mNotesDetails.show();
					mOkButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mStoreOwnerNotes = mDialogNotesValue.getText().toString().trim();
							mNotesDetails.dismiss();
						}
					});
				}
			});

			mInvoiceDetailsNotesButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mInvoiceDetailsLayout.setVisibility(View.GONE);
					mInvoiceDetailsNotesLayout.setVisibility(View.VISIBLE); 
				}
			});

			// To close receipts notes details view and open amount details
			mInvoiceDetailsNotesCloseImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mInvoiceDetailsLayout.setVisibility(View.VISIBLE);
					mInvoiceDetailsNotesLayout.setVisibility(View.GONE);
				}
			}); 

			mFooterAddInvoiceLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mFooterBackText.getText().toString().equalsIgnoreCase("Back")){ // Back Menu
						if(getIntent().hasExtra("FromCustomerCenter")){
							finish();
						}else{
							mFooterBackText.setText("Add Invoice");
							mFooterBackImage.setImageResource(R.drawable.add_invoice);
							mRightFooterText.setText("Processed");
							mRightFooterImage.setImageResource(R.drawable.processed_invoice);
							mFooterOutstandingInvoice.setVisibility(View.VISIBLE);
							mFooterProcessedInvoice.setVisibility(View.VISIBLE);
							if(mStoreOwnerInvoiceListView.getTag().toString().equalsIgnoreCase("outstanding")){
								mFooterProcessedInvoice.setBackgroundResource(R.drawable.header_2);
								mFooterOutstandingInvoice.setBackgroundResource(R.drawable.footer_dark_blue_new);
							}else{
								mFooterOutstandingInvoice.setBackgroundResource(R.drawable.header_2);	
								mFooterProcessedInvoice.setBackgroundResource(R.drawable.footer_dark_blue_new);
							}
							mStoreOwnerInvoiceTelephoneLayout.setVisibility(View.GONE);
							mStoreOwnerInvoiceCustomerDetailsLayout.setVisibility(View.GONE);
							mInvoiceSearchContainer.setVisibility(View.VISIBLE);
							mInvoiceListDateContainer.setVisibility(View.VISIBLE);
							setDefaultInvoiceListDate(); 
							mStoreOwnerInvoiceListView.setVisibility(View.VISIBLE);
							mStoreOwnerListViewHeaderLayout.setVisibility(View.VISIBLE);
							mViewInvoiceDetailsContainer.setVisibility(View.GONE);
						}
					}else{ // Add Invoice Menu		 		
						v.setBackgroundResource(R.drawable.footer_dark_blue_new);
						mFooterOutstandingInvoice.setBackgroundResource(R.drawable.header_2);
						mFooterProcessedInvoice.setBackgroundResource(R.drawable.header_2);
						mStoreOwnerInvoiceTelephoneLayout.setVisibility(View.VISIBLE);
						mStoreOwnerInvoiceCustomerDetailsLayout.setVisibility(View.GONE);
						mInvoiceSearchContainer.setVisibility(View.GONE);
						mInvoiceListDateContainer.setVisibility(View.GONE);
						mStoreOwnerInvoiceListView.setVisibility(View.GONE);
						mStoreOwnerListViewHeaderLayout.setVisibility(View.GONE);
						clearDatas();
						mStoreOwnerInvoiceTelephoneValue.getText();
					}
				}
			});

			mFooterOutstandingInvoice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/*if(!mStoreOwnerInvoiceListView.getTag().toString().equalsIgnoreCase("outstanding")){*/
					v.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mFooterAddInvoiceLayout.setBackgroundResource(R.drawable.header_2);
					mFooterProcessedInvoice.setBackgroundResource(R.drawable.header_2);
					mStoreOwnerInvoiceTelephoneLayout.setVisibility(View.GONE);
					mStoreOwnerInvoiceCustomerDetailsLayout.setVisibility(View.GONE);
					mInvoiceSearchContainer.setVisibility(View.VISIBLE);
					mInvoiceListDateContainer.setVisibility(View.VISIBLE);
					mStoreOwnerInvoiceListView.setTag("outstanding");
					setDefaultInvoiceListDate();
					mStoreOwnerInvoiceListView.setVisibility(View.VISIBLE);
					mStoreOwnerListViewHeaderLayout.setVisibility(View.VISIBLE);
					mInvoiceListHeader.setText("Days Outstanding");
					mInvoiceSearchValue.setText("");
					mStoreOwnerInvoiceListView.setAdapter(new CustomInvoiceListAdapter(InvoiceCenter.this,new ArrayList<Object>(),"outstanding"));
					if(new NetworkCheck().ConnectivityCheck(InvoiceCenter.this)){ // Network connectivity check
						GetAllInvoiceListTask mGetInvoices = new GetAllInvoiceListTask(InvoiceCenter.this, "outstanding",mSelectedFromDate,mSelectedToDate);
						mGetInvoices.execute();		
					}else{
						Toast.makeText(InvoiceCenter.this, "Network connection not available", Toast.LENGTH_SHORT).show();
					}	
					/*}*/
				}
			});


			mFooterProcessedInvoice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setBackgroundResource(R.drawable.footer_dark_blue_new);
					if(mRightFooterText.getText().toString().equalsIgnoreCase("Reject Invoice")){ // Reject Invoice menu
						final AlertDialog.Builder mAlertBox = new AlertDialog.Builder(InvoiceCenter.this);
						mAlertBox.setTitle("Information");
						mAlertBox.setMessage("Sure you want to reject the invoice");
						mAlertBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								SendInvoiceTask mRejectInvoice = new SendInvoiceTask(InvoiceCenter.this, mStoreOwnerInvoiceTelephoneLayout,mStoreOwnerInvoiceCustomerDetailsLayout,"RejectInvoice",mClassname);
								mRejectInvoice.execute(mInvoiceDate.getTag().toString());  // Invoice id set while choosing invoice from list	
							}
						});
						mAlertBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						mAlertBox.show();
					}else {/*if(!mStoreOwnerInvoiceListView.getTag().toString().equalsIgnoreCase("processed")){ // Processed Invoice list
					 */						mFooterAddInvoiceLayout.setBackgroundResource(R.drawable.header_2);
					 mFooterOutstandingInvoice.setBackgroundResource(R.drawable.header_2);
					 mStoreOwnerInvoiceTelephoneLayout.setVisibility(View.GONE);
					 mStoreOwnerInvoiceCustomerDetailsLayout.setVisibility(View.GONE);
					 mInvoiceSearchContainer.setVisibility(View.VISIBLE);
					 mInvoiceListDateContainer.setVisibility(View.VISIBLE);
					 mStoreOwnerInvoiceListView.setTag("processed");
					 setDefaultInvoiceListDate();
					 mStoreOwnerInvoiceListView.setVisibility(View.VISIBLE);
					 mStoreOwnerListViewHeaderLayout.setVisibility(View.VISIBLE);
					 mInvoiceListHeader.setText("Status");
					 mInvoiceSearchValue.setText("");
					 mStoreOwnerInvoiceListView.setAdapter(new CustomInvoiceListAdapter(InvoiceCenter.this,new ArrayList<Object>(),"processed"));
					 if(new NetworkCheck().ConnectivityCheck(InvoiceCenter.this)){
						 GetAllInvoiceListTask mGetInvoices = new GetAllInvoiceListTask(InvoiceCenter.this, "processed",mSelectedFromDate,mSelectedToDate);
						 mGetInvoices.execute();		
					 }else{
						 Toast.makeText(InvoiceCenter.this, "Network connection not available", Toast.LENGTH_SHORT).show();
					 }
					}
				}
			});

			mStoreOwnerInvoiceListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					POJOInvoiceList mInvoiceDetails = (POJOInvoiceList) arg0.getItemAtPosition(arg2);

					if(!mInvoiceDetails.Status.equalsIgnoreCase("Rejected")){  // Not rejected..
						mStoreOwnerInvoiceTelephoneLayout.setVisibility(View.GONE);
						mStoreOwnerInvoiceCustomerDetailsLayout.setVisibility(View.GONE);
						mInvoiceSearchContainer.setVisibility(View.GONE);
						mInvoiceListDateContainer.setVisibility(View.GONE);
						mStoreOwnerInvoiceListView.setVisibility(View.GONE);
						mStoreOwnerListViewHeaderLayout.setVisibility(View.GONE);
						mViewInvoiceDetailsContainer.setVisibility(View.VISIBLE);
						mInvoiceDetailsLayout.setVisibility(View.VISIBLE);
						mInvoiceDetailsNotesLayout.setVisibility(View.GONE);
						mFooterBackText.setText("Back");
						mFooterBackImage.setImageResource(R.drawable.back);
						mFooterOutstandingInvoice.setVisibility(View.INVISIBLE);
						// To set Store Name
						SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
						String mStoreName = mPrefs.getString("store_name", "");
						mInvoiceStoreName.setText(mStoreName);
						ImageLoader imageLoader = new ImageLoader(InvoiceCenter.this);
						imageLoader.DisplayImage(mInvoiceDetails.Customer_Image, mInvoiceDetailsCustomerImage);
						mInvoiceDetailsCustomerFirstName.setText(mInvoiceDetails.Customer_FirstName); 
						mInvoiceDetailsCustomerLastName.setText(mInvoiceDetails.Customer_LastName);			
						if(mStoreOwnerInvoiceListView.getTag().toString().equalsIgnoreCase("outstanding")){
							mInvoiceDateLabel.setText("Posted Date:");
							mInvoiceDate.setText(mInvoiceDetails.CreatedDate);
							mInvoiceDate.setTag(mInvoiceDetails.InvoiceId); // To use it in reject invoice
							if(!mInvoiceDetails.Amount.equalsIgnoreCase("")){
								mInvoiceAmount.setText("$"+String.format("%.2f",Double.valueOf(mInvoiceDetails.Amount)));
								mInvoiceTotalAmount.setText("$"+String.format("%.2f",Double.valueOf(mInvoiceDetails.Amount)));
							}else{
								mInvoiceAmount.setText("$"+"0.00");
								mInvoiceTotalAmount.setText("$"+"0.00");
							}
							mInvoiceTip.setText("$0.00");
							mStoreInvoiceNotes.setText(mInvoiceDetails.StoreNote);
							mCustomerInvoiceNotes.setText("");
							mFirstCardBorder.setVisibility(View.GONE);
							mSecondCardBorder.setVisibility(View.GONE);
							mCreditCardAmountLayout.setVisibility(View.GONE);
							mGiftCardUsedLayout.setVisibility(View.GONE);
							mGiftCardAmountLayout.setVisibility(View.GONE);
							mInvoiceDetailsSingleCardUsedLayout.setVisibility(View.GONE);
							mFooterProcessedInvoice.setVisibility(View.VISIBLE);
							mRightFooterText.setText("Reject Invoice");
							mRightFooterImage.setImageResource(R.drawable.sendinvoice_menubar);
						}else{
							mInvoiceDateLabel.setText("Processed Date:");
							mFooterProcessedInvoice.setVisibility(View.INVISIBLE);
							mInvoiceDate.setText(mInvoiceDetails.TransactionDate);
							mInvoiceAmount.setText("$"+mInvoiceDetails.ActualAmount);
							mInvoiceTip.setText("$"+mInvoiceDetails.TipAmount);
							mInvoiceTotalAmount.setText("$"+mInvoiceDetails.TotalAmount);
							mStoreInvoiceNotes.setText(mInvoiceDetails.StoreNote);
							mCustomerInvoiceNotes.setText(mInvoiceDetails.CustomerNote);
							if(mInvoiceDetails.TransactionType.equalsIgnoreCase("Both") && mInvoiceDetails.Status.equalsIgnoreCase("Approved")){
								mFirstCardBorder.setVisibility(View.VISIBLE);
								mSecondCardBorder.setVisibility(View.VISIBLE);
								mCreditCardAmountLayout.setVisibility(View.VISIBLE);
								mGiftCardUsedLayout.setVisibility(View.VISIBLE);
								mGiftCardAmountLayout.setVisibility(View.VISIBLE);
								mInvoiceDetailsSingleCardUsedLayout.setVisibility(View.VISIBLE);
								mInvoiceCardUsed.setText(mInvoiceDetails.CreditCardMask);
								mInvoiceCreditCardAmount.setText("$"+mInvoiceDetails.CreditCardAmount);
								mInvoiceGiftCardUsed.setText("Giftcard-$"+mInvoiceDetails.GiftcardFaceValue);
								mInvoiceGiftCardAmount.setText("$"+mInvoiceDetails.GiftcardAmount);

							}else if(mInvoiceDetails.TransactionType.equalsIgnoreCase("Credit card") && mInvoiceDetails.Status.equalsIgnoreCase("Approved")){
								mFirstCardBorder.setVisibility(View.GONE);
								mSecondCardBorder.setVisibility(View.GONE);
								mCreditCardAmountLayout.setVisibility(View.GONE);
								mGiftCardUsedLayout.setVisibility(View.GONE);
								mGiftCardAmountLayout.setVisibility(View.GONE);
								mInvoiceDetailsSingleCardUsedLayout.setVisibility(View.VISIBLE);
								mInvoiceCardUsed.setText(mInvoiceDetails.CreditCardMask);
							}else if(mInvoiceDetails.Status.equalsIgnoreCase("Approved")){ // giftcard type
								mFirstCardBorder.setVisibility(View.GONE);
								mSecondCardBorder.setVisibility(View.GONE);
								mCreditCardAmountLayout.setVisibility(View.GONE);
								mGiftCardUsedLayout.setVisibility(View.GONE);
								mGiftCardAmountLayout.setVisibility(View.GONE);
								mInvoiceDetailsSingleCardUsedLayout.setVisibility(View.VISIBLE);
								mInvoiceCardUsed.setText("Giftcard-$"+mInvoiceDetails.GiftcardFaceValue);					}
						}	
					}else{

					}


				}
			});

			mInvoiceSearchValue.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(!mInvoiceSearchValue.getText().toString().trim().equals("")){
						String searchString=s.toString();
						mSearchedInvoiceList.clear();
						for(int i=0;i<mTempInvoiceList.size();i++)
						{
							POJOInvoiceList InvoiceData = (POJOInvoiceList) mTempInvoiceList.get(i);

							if(InvoiceData.Customer_Name.toLowerCase().contains(searchString.toLowerCase())){
								mSearchedInvoiceList.add(InvoiceData);
							} 
						}
						mStoreOwnerInvoiceListView.setAdapter(new CustomInvoiceListAdapter(InvoiceCenter.this,mSearchedInvoiceList,mStoreOwnerInvoiceListView.getTag().toString()));
					}  	
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {}

				@Override
				public void afterTextChanged(Editable s) {}
			});

			mInvoiceSearchCancelButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mInvoiceSearchValue.setText("");
					mStoreOwnerInvoiceListView.setAdapter(new CustomInvoiceListAdapter(InvoiceCenter.this,mTempInvoiceList,mStoreOwnerInvoiceListView.getTag().toString()));
				}
			});


			mStoreOwnerCustomerInvoiceCouponImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					IntentIntegrator.initiateScan(InvoiceCenter.this);
				}
			});

			mStoreOwnerInvoiceCustomerImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openContextMenu(v);
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		openDatePicker(v.getTag().toString());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Choose From");
		menu.add(1, 0, 0, "Take Picture");
		menu.add(1, 1, 1, "Gallery");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getGroupId()){
		case 1:
			if(item.getItemId() == 0){
				Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent_camera, 2);
			}else if(item.getItemId() == 1){
				Intent mGalleryIntent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(mGalleryIntent, 1);
			} 
			return true;
		default:

		}
		return super.onContextItemSelected(item);
	}

	public void clearDatas() {
		mStoreOwnerInvoiceTelephoneValue.getText().clear();
		mStoreOwnerCustomerInvoiceFirstName.getText().clear();
		mStoreOwnerCustomerInvoiceLastName.getText().clear();
		mStoreOwnerCustomerInvoiceEmail.getText().clear();
		mStoreOwnerCustomerInvoiceAmount.setText("00.00");
		mStoreOwnerCustomerInvoiceCouponCode.getText().clear();
		mStoreOwnerNotes="";
		mStoreOwnerCustomerInvoicePinCode.getText().clear();
		mStoreOwnerInvoiceCustomerImage.setImageResource(R.drawable.profileimage);
	}

	public void updateViews() {
		mFooterBackText.setText("Add Invoice");
		mFooterBackImage.setImageResource(R.drawable.add_invoice);
		mRightFooterText.setText("Processed");
		mRightFooterImage.setImageResource(R.drawable.processed_invoice);
		mFooterOutstandingInvoice.setVisibility(View.VISIBLE);
		mFooterProcessedInvoice.setVisibility(View.VISIBLE);
		mFooterProcessedInvoice.setBackgroundResource(R.drawable.header_2);
		mStoreOwnerInvoiceTelephoneLayout.setVisibility(View.GONE);
		mStoreOwnerInvoiceCustomerDetailsLayout.setVisibility(View.GONE);
		mInvoiceSearchContainer.setVisibility(View.VISIBLE);
		mInvoiceSearchValue.getText().clear();
		mInvoiceListDateContainer.setVisibility(View.VISIBLE);
		setDefaultInvoiceListDate(); 
		mStoreOwnerInvoiceListView.setVisibility(View.VISIBLE);
		mStoreOwnerListViewHeaderLayout.setVisibility(View.VISIBLE);
		mViewInvoiceDetailsContainer.setVisibility(View.GONE);
		mStoreOwnerInvoiceListView.setAdapter(new CustomInvoiceListAdapter(InvoiceCenter.this, new ArrayList<Object>(),""));
		if(new NetworkCheck().ConnectivityCheck(InvoiceCenter.this)){
			GetAllInvoiceListTask mGetInvoices = new GetAllInvoiceListTask(InvoiceCenter.this, mStoreOwnerInvoiceListView.getTag().toString(),mSelectedFromDate,mSelectedToDate);
			mGetInvoices.execute();		
		}else{
			Toast.makeText(InvoiceCenter.this, "Network connection not available", Toast.LENGTH_SHORT).show();
		}
	}


	// To set/refresh invoice listview after fetching data from server
	public void setInvoiceListDetails(ArrayList<Object> result,String mEventFlag) {
		POJOInvoiceList mInvoiceDetails = (POJOInvoiceList) result.get(0);
		if(mInvoiceDetails.message.equalsIgnoreCase("")){  // success
			mTempInvoiceList = result;
			if(mEventFlag.equalsIgnoreCase("outstanding")){  // Outstanding invoices
				mStoreOwnerInvoiceListView.setTag("outstanding");
				mStoreOwnerInvoiceListView.setAdapter(new CustomInvoiceListAdapter(InvoiceCenter.this, result,mEventFlag));
			}else{ // Processed Invoices
				mStoreOwnerInvoiceListView.setTag("processed");
				mStoreOwnerInvoiceListView.setAdapter(new CustomInvoiceListAdapter(InvoiceCenter.this, result,mEventFlag));
			}	
		}else{
			alertBox_service("Information", "Invoice list not found please change From date and To Date", null);
			mStoreOwnerInvoiceListView.setAdapter(new CustomInvoiceListAdapter(InvoiceCenter.this, new ArrayList<Object>(),mEventFlag));
		}
	}

	// To Open Date picker view
	public void openDatePicker(final String dateType){
		Calendar mChoosedCalander;
		if(dateType.equalsIgnoreCase("FromDate")){  // From date
			mChoosedCalander = Calendar.getInstance();
			mChoosedCalander.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mInvoiceListFromDate.getText().toString().split("/")[1]));
			mChoosedCalander.set(Calendar.MONTH, Integer.parseInt(mInvoiceListFromDate.getText().toString().split("/")[0])-1);
			mChoosedCalander.set(Calendar.YEAR, Integer.parseInt(mInvoiceListFromDate.getText().toString().split("/")[2]));
		}else if(mInvoiceListToDate.getText().toString().trim().length()>0){ // To date 
			mChoosedCalander = Calendar.getInstance();
			mChoosedCalander.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mInvoiceListToDate.getText().toString().split("/")[1]));
			mChoosedCalander.set(Calendar.MONTH, Integer.parseInt(mInvoiceListToDate.getText().toString().split("/")[0])-1);
			mChoosedCalander.set(Calendar.YEAR, Integer.parseInt(mInvoiceListToDate.getText().toString().split("/")[2]));
		}else{ // Empty To date
			mChoosedCalander = Calendar.getInstance();
		}
		DatePickerDialog mDatePicker = new DatePickerDialog(InvoiceCenter.this,new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

				if(dateType.equalsIgnoreCase("FromDate")){ // set selected date in From date field
					Date mSelectedDate = new Date(year, monthOfYear, dayOfMonth);
					if(!isFutureDateSelected(year, monthOfYear, dayOfMonth)){
						mInvoiceListFromDate.setText(String.format("%02d",(mSelectedDate.getMonth()+1))+"/"+String.format("%02d",mSelectedDate.getDate())+"/"+mSelectedDate.getYear());
						mInvoiceListToDate.getText().clear();
						mSelectedFromDate = mSelectedDate.getYear()+"-"+String.format("%02d",(mSelectedDate.getMonth()+1))+"-"+String.format("%02d",mSelectedDate.getDate());
						// To clear list
						mStoreOwnerInvoiceListView.setAdapter(new CustomInvoiceListAdapter(InvoiceCenter.this, new ArrayList<Object>(),""));
						// To set current date in To field
						Calendar originalDateCalendar = Calendar.getInstance();
						mSelectedToDate = originalDateCalendar.get(Calendar.YEAR)+"-"+String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"-"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH));
						mInvoiceListToDate.setText(String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"/"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH))+"/"+originalDateCalendar.get(Calendar.YEAR));
						if(new NetworkCheck().ConnectivityCheck(InvoiceCenter.this)){
							GetAllInvoiceListTask mGetInvoices = new GetAllInvoiceListTask(InvoiceCenter.this, mStoreOwnerInvoiceListView.getTag().toString(),mSelectedFromDate,mSelectedToDate);
							mGetInvoices.execute();		
						}else{
							Toast.makeText(InvoiceCenter.this, "Network connection not available", Toast.LENGTH_SHORT).show();
						}
					}else{
						AlertDialog.Builder mAlertBox = new AlertDialog.Builder(InvoiceCenter.this);
						mAlertBox.setTitle("Information");
						mAlertBox.setMessage("Please choose previous date");
						mAlertBox.setPositiveButton("ok", null);
						mAlertBox.show();
					}
				}else{  // set selected date in To date field
					if(mInvoiceListFromDate.getText().toString().length()>0){
						if(isFutureDateSelected(year, monthOfYear, dayOfMonth)){
							AlertDialog.Builder mAlertBox = new AlertDialog.Builder(InvoiceCenter.this);
							mAlertBox.setTitle("Information");
							mAlertBox.setMessage("Please choose previous date");
							mAlertBox.setPositiveButton("ok", null);
							mAlertBox.show();	
						}else{
							String mFromDate = mInvoiceListFromDate.getText().toString();
							int mDate = Integer.parseInt(mFromDate.split("/")[1]); 
							int mMonth = Integer.parseInt(mFromDate.split("/")[0]);
							int mYear = Integer.parseInt(mFromDate.split("/")[2]);	
							Date mSelectedFromDateObject = new Date(mYear,mMonth-1,mDate);
							Date mSelectedDate = new Date(year, monthOfYear, dayOfMonth);
							if(mSelectedFromDateObject.compareTo(mSelectedDate) <= 0){
								mInvoiceListToDate.setText(String.format("%02d",(mSelectedDate.getMonth()+1))+"/"+String.format("%02d",mSelectedDate.getDate())+"/"+mSelectedDate.getYear());
								mSelectedToDate = mSelectedDate.getYear()+"-"+String.format("%02d",(mSelectedDate.getMonth()+1))+"-"+String.format("%02d",mSelectedDate.getDate());
								if(new NetworkCheck().ConnectivityCheck(InvoiceCenter.this)){
									GetAllInvoiceListTask mGetInvoices = new GetAllInvoiceListTask(InvoiceCenter.this, mStoreOwnerInvoiceListView.getTag().toString(),mSelectedFromDate,mSelectedToDate);
									mGetInvoices.execute();		
								}else{
									Toast.makeText(InvoiceCenter.this, "Network connection not available", Toast.LENGTH_SHORT).show();
								}
							}else{
								AlertDialog.Builder mAlertBox = new AlertDialog.Builder(InvoiceCenter.this);
								mAlertBox.setTitle("Information");
								mAlertBox.setMessage("Please choose date greater than Invoice From date");
								mAlertBox.setPositiveButton("ok", null);
								mAlertBox.show();
							}
						}
					}else{
						AlertDialog.Builder mAlertBox = new AlertDialog.Builder(InvoiceCenter.this);
						mAlertBox.setTitle("Information");
						mAlertBox.setMessage("Please choose Invoice From date first");
						mAlertBox.setPositiveButton("ok", null);
						mAlertBox.show();
					}
				}	
			}
		}, mChoosedCalander.get(Calendar.YEAR), mChoosedCalander.get(Calendar.MONTH), mChoosedCalander.get(Calendar.DAY_OF_MONTH));
		mDatePicker.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE: //1
			if(resultCode==RESULT_OK){
				IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
				if (scanResult == null) {
					return;
				}
				final String result = scanResult.getContents();
				if(result.contains("-") && result.split("-").length >0){
					mStoreOwnerCustomerInvoiceCouponCode.setText(result.split("-")[1]);	
				}
			}else if(resultCode==RESULT_CANCELED){
				Toast.makeText(InvoiceCenter.this, "Unable to scan QR", Toast.LENGTH_SHORT).show();
			}
			break;
		case 1: //1
			if(resultCode == RESULT_OK){
				setImageBitmap(data);
			}
			break;
		case 2: //1
			if(resultCode == RESULT_OK){
				setImageBitmap(data);
			}
			break;
		default:
		}
	}

	private boolean isFutureDateSelected(int year,int month,int day){
		Calendar mCurrentTime = Calendar.getInstance();
		int mDate = mCurrentTime.get(Calendar.DAY_OF_MONTH);
		int mMonth = mCurrentTime.get(Calendar.MONTH);
		int mYear = mCurrentTime.get(Calendar.YEAR);	
		Date mCurrentDate = new Date(mYear,mMonth,mDate);
		Date mSelectedDate = new Date(year, month, day);
		if(mCurrentDate.compareTo(mSelectedDate) >= 0){ // Not future date
			return false;
		}else{ // Future date
			return true;	
		}
	}

	// To set Profile image from Content Provider
	public void setImageBitmap(Intent data){
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
			options.inSampleSize = calculateInSampleSize(options, 120,135);
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap mSelectedImage = BitmapFactory.decodeFile(selected_file_path, options);
			mSelectedImage = new DecodeImageWithRotation().decodeImage(selected_file_path, mSelectedImage, 120, 135);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);    
			byte[] imagedata = baos.toByteArray();
			mProfilePhoto = com.us.zoupons.Base64.encodeBytes(imagedata);
			//mUserProfileImage.setImageBitmap(mSelectedImage);
			mSelectedImage = Bitmap.createScaledBitmap(mSelectedImage, 120,135 , true);
			BitmapDrawable drawable = new BitmapDrawable(mSelectedImage);
			mStoreOwnerInvoiceCustomerImage.setImageDrawable(drawable);
			//mAddImageText.setVisibility(View.GONE);
		}catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Please select valid image.", Toast.LENGTH_SHORT).show();
		}
	}

	// To calculate sample size to avoid OOM exception
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

	private void alertBox_service(String title, final String msg,final EditText editText) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(InvoiceCenter.this);
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

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwnerCustomerInvoiceAmount.setFocusable(true);
		mStoreOwnerCustomerInvoiceAmount.setFocusableInTouchMode(true);
		mStoreOwnerCustomerInvoicePinCode.setFocusable(true);
		mStoreOwnerCustomerInvoicePinCode.setFocusableInTouchMode(true);
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
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(InvoiceCenter.this,ZouponsConstants.sStoreModuleFlag);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(InvoiceCenter.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(InvoiceCenter.this);
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

	// To set One Month difference in TO and FROM Date as Default
	public void setDefaultInvoiceListDate(){
		// Setting default values to from date and To date
		Calendar originalDateCalendar = Calendar.getInstance();
		mSelectedToDate = mSelectedFromDate = originalDateCalendar.get(Calendar.YEAR)+"-"+String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"-"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH));
		mInvoiceListFromDate.setText(String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"/"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH))+"/"+originalDateCalendar.get(Calendar.YEAR));
		mInvoiceListToDate.setText(String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"/"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH))+"/"+originalDateCalendar.get(Calendar.YEAR));
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(InvoiceCenter.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

}