package com.us.zoupons.storeowner.coupons;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MobileNumberFilter;
import com.us.zoupons.MobileNumberTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.shopper.coupons.POJOCouponsList;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;
import com.us.zoupons.storeowner.invoice.CheckZouponsCustomerTask;

/**
 * 
 * Activity which displays add and edit store coupons details
 *
 */

public class StoreOwnerAdd_EditCoupon extends Activity {

	// Initializing views and variables
	private String mTAG="StoreOwnerAddCoupon";
	private MyHorizontalScrollView mScrollView;
	private View mApp;
	private ImageView mAddCouponRightMenuImage,mAddCouponTypeContextImage,mAddCouponBarcodeImage,mAddCouponActivationDateContextImage,mAddCouponExpirationDateContextImage,mAddCouponCodeImage;
	private EditText mAddCouponType,mAddCouponTitle,mAddCouponCode,mAddCouponDescription,mAddCouponActivationDate,mAddCouponExpirationDate,mAddCouponMobileNumber;
	private CheckBox mAddCouponUsageType;
	private Header mZouponsHeader;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private View mLeftMenu;
	private int mMenuFlag;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private View mRightMenu;
	private Button mStoreOwnerAddCouponFreezeView/*,mStoreOwnerAddCouponCancel,mStoreOwnerAddCouponSave*/;
	private LinearLayout mStoreOwnerAddCouponBack,mStoreOwnerAddCouponSave,mAddCouponMobileContainer,mMenuSplitter;
	private FrameLayout mSelectTypeContainer,mCouponBarcodeContainer;
	private String mPreviousMobileNumber="",mUpdatedMobileNumber="",mIsCouponOneTimeUse="No",mLocationId="",mActivationDate="",mExpirationDate="",mSelectedCouponId="";
	public static  String sCustomerUserId="";
	private ProgressBar ProgressView;
	private String mClass_name="";
	private RelativeLayout mStoreownerAddCouponHeaderContainer;
	private TextView mAddCouponHeaderText,mEditCouponType;
	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private String mFirstname="",mLastname="",mEmail="";
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			LayoutInflater inflater = LayoutInflater.from(this);
			mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(mScrollView);
			mApp = inflater.inflate(R.layout.storeowner_addcoupon, null);
			ViewGroup mMiddleView = (ViewGroup) mApp.findViewById(R.id.storeowneraddcoupon_container);
			ViewGroup mFooterView = (ViewGroup) mApp.findViewById(R.id.storeowneraddcoupon_footercontainer);
			mStoreOwnerAddCouponFreezeView = (Button) mApp.findViewById(R.id.storeowneraddcoupon_freeze);
			mStoreownerAddCouponHeaderContainer = (RelativeLayout) mMiddleView.findViewById(R.id.storeowneraddcoupon_header_container);
			mAddCouponRightMenuImage = (ImageView) mMiddleView.findViewById(R.id.storeowneraddcoupon_rightmenu);
			mAddCouponHeaderText = (TextView) mMiddleView.findViewById(R.id.storeowneraddcoupon_header);
			mSelectTypeContainer = (FrameLayout) mMiddleView.findViewById(R.id.addcoupon_selecttype_container);
			mEditCouponType = (TextView) mMiddleView.findViewById(R.id.edit_coupon_typeId);
			mStoreOwnerAddCouponBack = (LinearLayout) mFooterView.findViewById(R.id.storeowneraddcoupon_footer_back);
			mCouponBarcodeContainer = (FrameLayout) mMiddleView.findViewById(R.id.couponbarcode_container);
			mMenuSplitter = (LinearLayout) mFooterView.findViewById(R.id.menubar_splitter1);
			mStoreOwnerAddCouponSave = (LinearLayout) mFooterView.findViewById(R.id.storeowneraddcoupon_footer_save);
			mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreOwnerAdd_EditCoupon.this,mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerAddCouponFreezeView, mTAG);
			mLeftMenu = mStoreownerLeftmenu.intializeInflater();
			mStoreownerLeftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			mStoreownerRightmenu = new StoreOwner_RightMenu(StoreOwnerAdd_EditCoupon.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerAddCouponFreezeView, mTAG);
			mRightMenu = mStoreownerRightmenu.intializeCustomerCenterInflater();
			mStoreownerRightmenu.customercentermenuClickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsHeader = (Header) mApp.findViewById(R.id.storeowneraddcoupon_header);
			mZouponsHeader.intializeInflater(mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwnerAddCouponFreezeView, mTAG);
			mZouponsHeader.mTabBarHomeBtn.setVisibility(View.VISIBLE);
			mZouponsHeader.mTabBarNotificationImage.setVisibility(View.VISIBLE);
			final View[] children;
			if(getIntent().hasExtra("FromCustomerCenter")){ // Add customer center right menu 
				children = new View[] { mLeftMenu, mApp, mRightMenu};
			}else{
				children = new View[] { mLeftMenu, mApp};	
			}
			/* Scroll to app (view[1]) when layout finished.*/
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsHeader.mLeftMenuBtnSlide));
			mStoreOwnerAddCouponFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mStoreOwnerAddCouponFreezeView, mTAG));
			mAddCouponHeaderText.setText(StoreOwner_RightMenu.mCustomer_FirstName+" "+StoreOwner_RightMenu.mCustomer_LastName);
			mAddCouponRightMenuImage.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag=2, mStoreOwnerAddCouponFreezeView, mTAG));
			mNotificationReceiver = new NotifitcationReceiver(mZouponsHeader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			if(getIntent().hasExtra("FromCustomerCenter") || getIntent().hasExtra("FromContactStore")){ // To differentiate flow from customer center or not
				mSelectTypeContainer.setVisibility(View.GONE);
				mEditCouponType.setVisibility(View.VISIBLE);
				mEditCouponType.setText("Private");
				if(getIntent().hasExtra("FromCustomerCenter"))
					mStoreownerAddCouponHeaderContainer.setVisibility(View.VISIBLE);
			}else{
				mStoreownerAddCouponHeaderContainer.setVisibility(View.GONE);
				mSelectTypeContainer.setVisibility(View.VISIBLE);
			}
			// Initialising views
			mAddCouponType = (EditText) mMiddleView.findViewById(R.id.addcoupon_selecttype_value);
			mAddCouponType.setLongClickable(false); // To disable cut/copy/paste options
			mAddCouponTypeContextImage = (ImageView) mMiddleView.findViewById(R.id.addcoupon_selecttype_contextmenu);
			mAddCouponMobileContainer = (LinearLayout) mMiddleView.findViewById(R.id.mobile_number_container);
			mAddCouponMobileNumber = (EditText) mMiddleView.findViewById(R.id.addcoupon_mobilenumber_value);
			mAddCouponMobileNumber.addTextChangedListener(new MobileNumberTextWatcher());
			mAddCouponMobileNumber.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});
			mAddCouponTitle = (EditText) mMiddleView.findViewById(R.id.addcoupon_title_value);
			mAddCouponCode = (EditText) mMiddleView.findViewById(R.id.addcoupon_couponcode_value);
			mAddCouponCodeImage = (ImageView) mMiddleView.findViewById(R.id.addcoupon_couponcode_image);
			ProgressView = (ProgressBar) mMiddleView.findViewById(R.id.progess_bar);
			mAddCouponBarcodeImage = (ImageView) mMiddleView.findViewById(R.id.add_coupon_barcode);
			mAddCouponActivationDate = (EditText) mMiddleView.findViewById(R.id.addcoupon_activationdate_value);
			mAddCouponActivationDateContextImage = (ImageView) mMiddleView.findViewById(R.id.addcoupon_activationdate_contextmenu);
			mAddCouponExpirationDate = (EditText) mMiddleView.findViewById(R.id.addcoupon_expirationdate_value);
			mAddCouponExpirationDateContextImage = (ImageView) mMiddleView.findViewById(R.id.addcoupon_expirationdate_contextmenu);
			mAddCouponDescription = (EditText) mMiddleView.findViewById(R.id.addcoupon_Description_value);
			mAddCouponUsageType = (CheckBox) mMiddleView.findViewById(R.id.addcoupon_usagetypeId);
			sCustomerUserId = "";
			
			// Registering context menu
			registerForContextMenu(mAddCouponTypeContextImage);
			registerForContextMenu(mAddCouponActivationDateContextImage);
			registerForContextMenu(mAddCouponExpirationDateContextImage);

			if(getIntent().hasExtra("FromCustomerCenter")){
				mClass_name = "customer_center";
				mStoreOwnerAddCouponBack.setVisibility(View.INVISIBLE);
				mSelectTypeContainer.setVisibility(View.GONE);
				mMenuSplitter.setVisibility(View.GONE);
			}else if(getIntent().hasExtra("FromContactStore")){
				mClass_name = "contact_store";
				mStoreOwnerAddCouponBack.setVisibility(View.VISIBLE);
				mMenuSplitter.setVisibility(View.VISIBLE);
			}else{
				mClass_name = "coupons";
				mStoreOwnerAddCouponBack.setVisibility(View.VISIBLE);
				mMenuSplitter.setVisibility(View.VISIBLE);
			}
			// To differentiate add coupon or for edit coupon details
			if(getIntent().hasExtra("choosed_position")){ // Edit coupon details , to set choosed coupon details
				int choosed_position = getIntent().getExtras().getInt("choosed_position");
				POJOCouponsList mSelectedCouponDetail = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(choosed_position);
				mSelectedCouponId = mSelectedCouponDetail.mCouponId;
				sCustomerUserId = mSelectedCouponDetail.mPrivateCustomerId;
				mAddCouponType.setText(mSelectedCouponDetail.mCouponType);
				mSelectTypeContainer.setVisibility(View.GONE);
				mAddCouponMobileContainer.setVisibility(View.GONE);
				mEditCouponType.setVisibility(View.VISIBLE);
				mEditCouponType.setText(mSelectedCouponDetail.mCouponType);
				if(mSelectedCouponDetail.mCouponType.equalsIgnoreCase("private")){
					mUpdatedMobileNumber = mSelectedCouponDetail.mCustomerPhone;
				}
				mAddCouponTitle.setText(mSelectedCouponDetail.mCouponTitle);
				mAddCouponTitle.setSelection(mSelectedCouponDetail.mCouponTitle.length());
				mAddCouponCode.setText(mSelectedCouponDetail.mCouponCode);
				ProgressView.setVisibility(View.VISIBLE);
				LoadCouponQRcodeTask loadQr = new LoadCouponQRcodeTask(mAddCouponBarcodeImage, ProgressView);
				loadQr.execute(mSelectedCouponDetail.mCouponBarCodeImage);
				// Formatting date
				String[] activation_date = mSelectedCouponDetail.mActivationDate.split("-");
				if(activation_date.length == 3){
					mAddCouponActivationDate.setText(activation_date[1]+"/"+activation_date[2]+"/"+activation_date[0]);	
				}
				mActivationDate = activation_date[0]+"/"+activation_date[1]+"/"+activation_date[2];
				String[] expiration_date = mSelectedCouponDetail.mCouponExpires.split("/");
				if(expiration_date.length==3){
					mExpirationDate = expiration_date[2]+"/"+expiration_date[0]+"/"+expiration_date[1];	
				}
				mAddCouponExpirationDate.setText(mSelectedCouponDetail.mCouponExpires);	
				mAddCouponDescription.setText(mSelectedCouponDetail.mCouponDescription);
				if(mSelectedCouponDetail.mOneTimeUse.equalsIgnoreCase("Yes")){
					mAddCouponUsageType.setChecked(true);	
				}else{
					mAddCouponUsageType.setChecked(false);
				}
			}else{
				sCustomerUserId = "";
				ProgressView.setVisibility(View.GONE);
				mAddCouponCodeImage.setVisibility(View.GONE);
				findViewById(R.id.barcode_above_space).setVisibility(View.GONE);
				mCouponBarcodeContainer.setVisibility(View.GONE);
			}
			// to get location id from preference
			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences",MODE_PRIVATE);
			mLocationId = mPrefs.getString("location_id", "");

			mAddCouponUsageType.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						mIsCouponOneTimeUse = "Yes";
					}else{
						mIsCouponOneTimeUse = "No";
					}
				}
			});

			mAddCouponType.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openContextMenu(mAddCouponTypeContextImage);		
				}
			});

			mAddCouponTypeContextImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openContextMenu(mAddCouponTypeContextImage);
				}
			});

			mAddCouponActivationDate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openDatePicker("ActivationDate");		
				}
			});

			mAddCouponActivationDateContextImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openDatePicker("ActivationDate");		
				}
			});


			mAddCouponExpirationDate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mAddCouponActivationDate.getText().toString().length()>0)
						openDatePicker("ExpirationDate");	
					else{
						AlertDialog.Builder mAlertBox = new AlertDialog.Builder(StoreOwnerAdd_EditCoupon.this);
						mAlertBox.setTitle("Information");
						mAlertBox.setMessage("Please choose Activation date first");
						mAlertBox.setPositiveButton("ok", null);
						mAlertBox.show();
					}

				}
			});


			mAddCouponExpirationDateContextImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mAddCouponActivationDate.getText().toString().length()>0)
						openDatePicker("ExpirationDate");	
					else{
						AlertDialog.Builder mAlertBox = new AlertDialog.Builder(StoreOwnerAdd_EditCoupon.this);
						mAlertBox.setTitle("Information");
						mAlertBox.setMessage("Please choose Activation date first");
						mAlertBox.setPositiveButton("ok", null);
						mAlertBox.show();
					}
				}
			});


			mStoreOwnerAddCouponBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});

			mAddCouponCodeImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mAddCouponCode.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter coupon code",mAddCouponCode);
					}else{
						if(new NetworkCheck().ConnectivityCheck(StoreOwnerAdd_EditCoupon.this)){
							Add_EditCouponAsyncTask mAddCoupon = new Add_EditCouponAsyncTask(StoreOwnerAdd_EditCoupon.this,mAddCouponBarcodeImage,mClass_name,mAddCouponMobileNumber,mAddCouponCode);
							mAddCoupon.execute("generate_qrcode",mLocationId,mAddCouponCode.getText().toString());		
						}else{
							Toast.makeText(StoreOwnerAdd_EditCoupon.this, "No Network Connection", Toast.LENGTH_SHORT).show();
						}
					}
				}
			});

			mStoreOwnerAddCouponSave.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if(mSelectTypeContainer.getVisibility() == View.VISIBLE && mAddCouponType.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please choose coupon type",mAddCouponType);
					}else if(mAddCouponMobileContainer.getVisibility() == View.VISIBLE && mAddCouponMobileNumber.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter customer phone number",mAddCouponMobileNumber);
					}else if(mAddCouponMobileContainer.getVisibility() == View.VISIBLE && mAddCouponMobileNumber.getText().toString().trim().length()!=12){
						alertBox_service("Information", "Please enter valid phone number",mAddCouponMobileNumber);
					}else if(mAddCouponTitle.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter coupon title",mAddCouponTitle);
					}else if(mAddCouponCode.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter coupon code",mAddCouponCode);
					}else  if(mAddCouponActivationDate.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please choose coupon activation date",mAddCouponActivationDate);
					}else if(mAddCouponExpirationDate.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please choose coupon expiration date",mAddCouponExpirationDate);
					}else if(mAddCouponDescription.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter coupon description",mAddCouponDescription);
					}else{
						if(new NetworkCheck().ConnectivityCheck(StoreOwnerAdd_EditCoupon.this)){
							if(getIntent().hasExtra("choosed_position")){ // For edit coupon details
								Add_EditCouponAsyncTask mAddCoupon = new Add_EditCouponAsyncTask(StoreOwnerAdd_EditCoupon.this,mAddCouponBarcodeImage,mClass_name,mAddCouponMobileNumber,mAddCouponCode);
								mAddCoupon.execute("update_coupon",mAddCouponType.getText().toString(),mLocationId,sCustomerUserId,mAddCouponTitle.getText().toString(),mAddCouponCode.getText().toString(),
										mActivationDate,mExpirationDate,mIsCouponOneTimeUse,mAddCouponDescription.getText().toString(),mFirstname,mLastname,mEmail,mUpdatedMobileNumber,mSelectedCouponId);	
							}else{ // for adding Coupons
								String couponType= "";
								if(getIntent().hasExtra("FromCustomerCenter") || getIntent().hasExtra("FromContactStore")){
									couponType = "Private";
									sCustomerUserId = getIntent().getExtras().getString("customer_id");
								}else{
									couponType = mAddCouponType.getText().toString();
								}
								Add_EditCouponAsyncTask mAddCoupon = new Add_EditCouponAsyncTask(StoreOwnerAdd_EditCoupon.this,mAddCouponBarcodeImage,mClass_name,mAddCouponMobileNumber,mAddCouponCode);
								mAddCoupon.execute("create_coupon",couponType,mLocationId,sCustomerUserId,mAddCouponTitle.getText().toString(),mAddCouponCode.getText().toString(),
										mActivationDate,mExpirationDate,mIsCouponOneTimeUse,mAddCouponDescription.getText().toString(),mFirstname,mLastname,mEmail,mAddCouponMobileNumber.getText().toString(),""); // Last paramater is coupon id (only needed for edit coupons)
							}

						}else{
							Toast.makeText(StoreOwnerAdd_EditCoupon.this, "No Network Connection", Toast.LENGTH_SHORT).show();
						}
					}
				}
			});

			mAddCouponMobileNumber.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus){
						if(mAddCouponMobileNumber.getText().toString().trim().length() == 12){
							mPreviousMobileNumber = mAddCouponMobileNumber.getText().toString();
							if(!mAddCouponMobileNumber.getText().toString().trim().equalsIgnoreCase("") && (!mPreviousMobileNumber.equalsIgnoreCase(mUpdatedMobileNumber))){
								mUpdatedMobileNumber = mAddCouponMobileNumber.getText().toString();
								if(new NetworkCheck().ConnectivityCheck(StoreOwnerAdd_EditCoupon.this)){
									InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
									inputMethodManager.hideSoftInputFromWindow(mAddCouponMobileNumber.getWindowToken(), 0);
									CheckZouponsCustomerTask mCheckCustomer = new CheckZouponsCustomerTask(StoreOwnerAdd_EditCoupon.this,mAddCouponMobileNumber);
									mCheckCustomer.execute(mUpdatedMobileNumber);	
								}else{
									Toast.makeText(StoreOwnerAdd_EditCoupon.this, "No Network Connection", Toast.LENGTH_SHORT).show();
								}

							}							
						}else{
							alertBox_service("Information", "Please enter valid mobile number",mAddCouponMobileNumber);
						}

					}
				}
			});

			mAddCouponCode.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {}

				@Override
				public void afterTextChanged(Editable s) {
					String result = s.toString().replaceAll(" ", "");
					if (!s.toString().equals(result)) {
						mAddCouponCode.setText(result);
						mAddCouponCode.setSelection(result.length());
					}
				}
			});

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mAddCouponCode.setFocusable(true);
		mAddCouponCode.setFocusableInTouchMode(true);
		mAddCouponDescription.setFocusable(true);
		mAddCouponDescription.setFocusableInTouchMode(true);
		mAddCouponTitle.setFocusable(true);
		mAddCouponTitle.setFocusableInTouchMode(true);
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if(v.equals(mAddCouponTypeContextImage)){
			menu.setHeaderTitle("Choose Coupon Type");
			menu.add(0, 1, 0, "Public");
			menu.add(0, 2, 0, "Private");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getGroupId()){
		case 0:
			mAddCouponType.setText(item.getTitle().toString());
			if(item.getTitle().toString().equalsIgnoreCase("Public")){ //  public type
				mAddCouponMobileContainer.setVisibility(View.GONE);
			}else{ // private 
				mAddCouponMobileContainer.setVisibility(View.VISIBLE);
				mAddCouponMobileNumber.getText().clear();
			}
			break;
		}
		return super.onContextItemSelected(item);
	}

	// To set inactive customer details to local variable
	public void setInactiveCustomerDetails(String firstname,String lastname,String email){
		mFirstname= firstname;
		mLastname= lastname;
		mEmail= email;
	}

	// To open date picker dialog 
	public void openDatePicker(final String dateType){
		DatePickerDialog mDatePicker = new DatePickerDialog(StoreOwnerAdd_EditCoupon.this,new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				try{
					if(dateType.equalsIgnoreCase("ActivationDate")){ // set selected date in activation date field
						Date mSelectedDate = new Date(year, monthOfYear, dayOfMonth);
						mAddCouponActivationDate.setText((mSelectedDate.getMonth()+1)+"/"+mSelectedDate.getDate()+"/"+mSelectedDate.getYear());	
						mActivationDate = mSelectedDate.getYear()+"/"+(mSelectedDate.getMonth()+1)+"/"+mSelectedDate.getDate();
						if(!mAddCouponExpirationDate.getText().toString().equalsIgnoreCase("")){
							String mExpirationDateString = mAddCouponExpirationDate.getText().toString();
							int mExpirationDay = Integer.parseInt(mExpirationDateString.split("/")[1]); 
							int mExpirationMonth = Integer.parseInt(mExpirationDateString.split("/")[0]);
							int mExpirationYear = Integer.parseInt(mExpirationDateString.split("/")[2]);	
							Date mExpirationDate = new Date(mExpirationYear, mExpirationMonth-1, mExpirationDay);
							if(mSelectedDate.compareTo(mExpirationDate) > 0)
								mAddCouponExpirationDate.getText().clear();
						}
					 }else{  // set selected date in exiration date field
							String mActivationDate = mAddCouponActivationDate.getText().toString();
							int mDate = Integer.parseInt(mActivationDate.split("/")[1]); 
							int mMonth = Integer.parseInt(mActivationDate.split("/")[0]);
							int mYear = Integer.parseInt(mActivationDate.split("/")[2]);	
							Date mCurrentDate = new Date(mYear,mMonth-1,mDate);
							Date mSelectedDate = new Date(year, monthOfYear, dayOfMonth);
							if(getIntent().hasExtra("choosed_position")){ // Edit coupon details , No date validation needed
								mAddCouponExpirationDate.setText((mSelectedDate.getMonth()+1)+"/"+mSelectedDate.getDate()+"/"+mSelectedDate.getYear());
								mExpirationDate = mSelectedDate.getYear()+"/"+(mSelectedDate.getMonth()+1)+"/"+mSelectedDate.getDate();
							}else{
								if(mCurrentDate.compareTo(mSelectedDate) <= 0){
									mAddCouponExpirationDate.setText((mSelectedDate.getMonth()+1)+"/"+mSelectedDate.getDate()+"/"+mSelectedDate.getYear());
									mExpirationDate = mSelectedDate.getYear()+"/"+(mSelectedDate.getMonth()+1)+"/"+mSelectedDate.getDate();
								}else{
									AlertDialog.Builder mAlertBox = new AlertDialog.Builder(StoreOwnerAdd_EditCoupon.this);
									mAlertBox.setTitle("Information");
									mAlertBox.setMessage("Please choose future date than Activation date");
									mAlertBox.setPositiveButton("ok", null);
									mAlertBox.show();
								}
							}
						}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		mDatePicker.show();
	}

	// Asynchronous task to load coupon QR code
	public class LoadCouponQRcodeTask extends AsyncTask<String, Void, Void>{
		private Bitmap mCouponQrcode;
		private ImageView mImageView;
		private ProgressBar mProgressView;

		public LoadCouponQRcodeTask(ImageView imageview, ProgressBar ProgressView) {
			this.mImageView = imageview;
			mProgressView = ProgressView;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressView.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(String... params) {
			mCouponQrcode = getBitmapFromURL(params[0]);
			return null;
		}


		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			try{
				mProgressView.setVisibility(View.GONE);
				if(mCouponQrcode != null){
					mImageView.setVisibility(View.VISIBLE);
					mImageView.setImageBitmap(mCouponQrcode);
				}else{
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	// To get Bitmap from specified URL
	public Bitmap getBitmapFromURL(String src) {
		try {
			Bitmap mBitmap;	
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(src);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(params, 60000); // 1 minute
			request.setParams(params);
			HttpResponse response = httpClient.execute(request);
			byte[] image = EntityUtils.toByteArray(response.getEntity());
			mBitmap =   BitmapFactory.decodeByteArray(image, 0,image.length);
			image = null;
			return mBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}	

	// To show alert pop up box with message
	private void alertBox_service(String title, final String msg,final EditText editText) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwnerAdd_EditCoupon.this);
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
	protected void onDestroy() {
		super.onDestroy();
		// To notify system that its time to run garbage collector service
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
		mNotificationSync = new ScheduleNotificationSync(StoreOwnerAdd_EditCoupon.this,ZouponsConstants.sStoreModuleFlag);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwnerAdd_EditCoupon.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwnerAdd_EditCoupon.this);
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
		//super.onBackPressed();
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwnerAdd_EditCoupon.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	public void clearMobileNumber() {
		// TODO Auto-generated method stub
		mUpdatedMobileNumber = "";
	}


}
