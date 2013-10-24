package com.us.zoupons.storeowner.Coupons;

import java.io.ByteArrayOutputStream;
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
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

import com.us.zoupons.Base64;
import com.us.zoupons.DecodeImageWithRotation;
import com.us.zoupons.MobileNumberFilter;
import com.us.zoupons.MobileNumberTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.Settings;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.Coupons.POJOCouponsList;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;
import com.us.zoupons.storeowner.invoice.CheckZouponsCustomerTask;

public class StoreOwnerAdd_EditCoupon extends Activity {

	public static String TAG="StoreOwnerAddCoupon";

	public static MyHorizontalScrollView scrollView;
	View app;

	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ImageView mAddCouponRightMenuImage,mAddCouponTypeContextImage,mAddCouponBarcodeImage,mAddCouponActivationDateContextImage,mAddCouponExpirationDateContextImage,mAddCouponCodeImage,mAddCouponCustomerProfileImage;
	private EditText mAddCouponType,mAddCouponTitle,mAddCouponCode,mAddCouponDescription,mAddCouponActivationDate,mAddCouponExpirationDate,mAddCouponMobileNumber,mAddCouponCustomerFirstName,mAddCouponCustomerLastName,mAddCouponEmailAddress;
	private CheckBox mAddCouponUsageType;
	private Header header;
	private FrameLayout mAddCouponTypeContainer,mAddCouponActivationDateContainer,mAddCouponExpirationDateContainer;
	StoreOwner_LeftMenu storeowner_leftmenu;
	View mLeftMenu;
	int mMenuFlag;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;
	private Button mStoreOwnerAddCouponFreezeView/*,mStoreOwnerAddCouponCancel,mStoreOwnerAddCouponSave*/;
	private LinearLayout mStoreOwnerAddCouponBack,mStoreOwnerAddCouponSave,mSelectTypeContainer,mAddCouponMobileContainer,mMenuSplitter,mStoreOwnerAddCouponCustomerDetailsContainer;
	private String mPreviousMobileNumber="",mUpdatedMobileNumber="",mIsCouponOneTimeUse="No",mLocationId="",mActivationDate="",mExpirationDate="",mSelectedCouponId="";
	public static String mCustomerUserId="";
	private ProgressBar ProgressView;
	private String class_name="",mProfilePhoto="";
	private RelativeLayout mStoreownerAddCouponHeaderContainer;
	private TextView mAddCouponHeaderText;
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			LayoutInflater inflater = LayoutInflater.from(this);
			scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(scrollView);
			mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
			mConnectionAvailabilityChecking = new NetworkCheck();
			app = inflater.inflate(R.layout.storeowner_addcoupon, null);
			ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.storeowneraddcoupon_container);
			ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeowneraddcoupon_footercontainer);
			mStoreOwnerAddCouponFreezeView = (Button) app.findViewById(R.id.storeowneraddcoupon_freeze);
			mStoreownerAddCouponHeaderContainer = (RelativeLayout) mMiddleView.findViewById(R.id.storeowneraddcoupon_header_container);
			mAddCouponRightMenuImage = (ImageView) mMiddleView.findViewById(R.id.storeowneraddcoupon_rightmenu);
			mAddCouponHeaderText = (TextView) mMiddleView.findViewById(R.id.storeowneraddcoupon_header);
			mSelectTypeContainer = (LinearLayout) mMiddleView.findViewById(R.id.selecttype_container);
			mStoreOwnerAddCouponBack = (LinearLayout) mFooterView.findViewById(R.id.storeowneraddcoupon_footer_back);
			mMenuSplitter = (LinearLayout) mFooterView.findViewById(R.id.menubar_splitter1);
			mStoreOwnerAddCouponSave = (LinearLayout) mFooterView.findViewById(R.id.storeowneraddcoupon_footer_save);
			storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwnerAdd_EditCoupon.this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerAddCouponFreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();
			storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwnerAdd_EditCoupon.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerAddCouponFreezeView, TAG);
			mRightMenu = storeowner_rightmenu.intializeCustomerCenterInflater();
			storeowner_rightmenu.customercentermenuClickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeowneraddcoupon_header);
			header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwnerAddCouponFreezeView, TAG);
			header.mTabBarHomeBtn.setVisibility(View.VISIBLE);
			header.mTabBarNotificationBtn.setVisibility(View.VISIBLE);
			final View[] children;
			if(getIntent().hasExtra("FromCustomerCenter")){ // Add customer center right menu 
				children = new View[] { mLeftMenu, app, mRightMenu};
			}else{
				children = new View[] { mLeftMenu, app};	
			}
			/* Scroll to app (view[1]) when layout finished.*/
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
			mStoreOwnerAddCouponFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag, mStoreOwnerAddCouponFreezeView, TAG));
			mAddCouponHeaderText.setText(StoreOwner_RightMenu.mCustomer_FirstName+" "+StoreOwner_RightMenu.mCustomer_LastName);
			mAddCouponRightMenuImage.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag=2, mStoreOwnerAddCouponFreezeView, TAG));
			if(getIntent().hasExtra("FromCustomerCenter") || getIntent().hasExtra("FromContactStore")){ // To differentiate flow from customer center or not
				mSelectTypeContainer.setVisibility(View.GONE);
				if(getIntent().hasExtra("FromCustomerCenter"))
					mStoreownerAddCouponHeaderContainer.setVisibility(View.VISIBLE);
			}else{
				mStoreownerAddCouponHeaderContainer.setVisibility(View.GONE);
				mSelectTypeContainer.setVisibility(View.VISIBLE);
			}
			// Initialising views
			mAddCouponTypeContainer = (FrameLayout) mMiddleView.findViewById(R.id.addcoupon_selecttype_container);
			mAddCouponType = (EditText) mMiddleView.findViewById(R.id.addcoupon_selecttype_value);
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
			mAddCouponActivationDateContainer = (FrameLayout) mMiddleView.findViewById(R.id.addcoupon_activationdate_framecontainer);
			mAddCouponActivationDate = (EditText) mMiddleView.findViewById(R.id.addcoupon_activationdate_value);
			mAddCouponActivationDateContextImage = (ImageView) mMiddleView.findViewById(R.id.addcoupon_activationdate_contextmenu);
			mAddCouponExpirationDateContainer = (FrameLayout) mMiddleView.findViewById(R.id.addcoupon_expirationdate_framecontainer);
			mAddCouponExpirationDate = (EditText) mMiddleView.findViewById(R.id.addcoupon_expirationdate_value);
			mAddCouponExpirationDateContextImage = (ImageView) mMiddleView.findViewById(R.id.addcoupon_expirationdate_contextmenu);
			mAddCouponDescription = (EditText) mMiddleView.findViewById(R.id.addcoupon_Description_value);
			mAddCouponUsageType = (CheckBox) mMiddleView.findViewById(R.id.addcoupon_usagetypeId);
			mStoreOwnerAddCouponCustomerDetailsContainer = (LinearLayout) mMiddleView.findViewById(R.id.addcoupon_customer_info_container);
			mAddCouponCustomerFirstName = (EditText) mMiddleView.findViewById(R.id.addcoupon_customer_firstname_value);
			mAddCouponCustomerLastName = (EditText) mMiddleView.findViewById(R.id.addcoupon_customer_lastname_value);
			mAddCouponEmailAddress = (EditText) mMiddleView.findViewById(R.id.addcoupon_customer_emailaddress_value);
			mAddCouponCustomerProfileImage = (ImageView) mMiddleView.findViewById(R.id.addcoupon_customer_profile_imageId);
			
			// Registering context menu
			registerForContextMenu(mAddCouponTypeContextImage);
			registerForContextMenu(mAddCouponActivationDateContextImage);
			registerForContextMenu(mAddCouponExpirationDateContextImage);
			registerForContextMenu(mAddCouponCustomerProfileImage);
			
			if(getIntent().hasExtra("FromCustomerCenter")){
				class_name = "customer_center";
				mStoreOwnerAddCouponBack.setVisibility(View.INVISIBLE);
				mMenuSplitter.setVisibility(View.GONE);
			}else if(getIntent().hasExtra("FromContactStore")){
				class_name = "contact_store";
				mStoreOwnerAddCouponBack.setVisibility(View.VISIBLE);
				mMenuSplitter.setVisibility(View.VISIBLE);
			}else{
				class_name = "coupons";
				mStoreOwnerAddCouponBack.setVisibility(View.VISIBLE);
				mMenuSplitter.setVisibility(View.VISIBLE);
			}
			// To differentiate add coupon or for edit coupon details
			if(getIntent().hasExtra("choosed_position")){ // Edit coupon details , to set choosed coupon details
				int choosed_position = getIntent().getExtras().getInt("choosed_position");
				Log.i("choosed position", choosed_position+"");
				POJOCouponsList mSelectedCouponDetail = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(choosed_position);
				mSelectedCouponId = mSelectedCouponDetail.mCouponId;
				mCustomerUserId = mSelectedCouponDetail.mPrivateCustomerId;
				mAddCouponType.setText(mSelectedCouponDetail.mCouponType);
				if(mSelectedCouponDetail.mCouponType.equalsIgnoreCase("public")){
					mAddCouponMobileContainer.setVisibility(View.GONE);
				}else{
					mAddCouponMobileContainer.setVisibility(View.VISIBLE);
					mSelectedCouponDetail.mCustomerPhone = mSelectedCouponDetail.mCustomerPhone.substring(0, 3)+"-"+mSelectedCouponDetail.mCustomerPhone.substring(3, 6)+"-"+mSelectedCouponDetail.mCustomerPhone.substring(6, 10);
					mAddCouponMobileNumber.setText(mSelectedCouponDetail.mCustomerPhone);
					mAddCouponMobileNumber.setSelection(mSelectedCouponDetail.mCustomerPhone.length());
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
				mCustomerUserId = "";
				ProgressView.setVisibility(View.GONE);
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
					openDatePicker("ExpirationDate");	
				}
			});


			mAddCouponExpirationDateContextImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openDatePicker("ExpirationDate");		
				}
			});


			mStoreOwnerAddCouponBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			
			mAddCouponCustomerProfileImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openContextMenu(v);
				}
			});

			mAddCouponCodeImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mAddCouponCode.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter coupon code",mAddCouponCode);
					}else{
						if(new NetworkCheck().ConnectivityCheck(StoreOwnerAdd_EditCoupon.this)){
							Add_EditCouponAsyncTask mAddCoupon = new Add_EditCouponAsyncTask(StoreOwnerAdd_EditCoupon.this,mAddCouponBarcodeImage,class_name);
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
					}else if(mStoreOwnerAddCouponCustomerDetailsContainer.getVisibility() == View.VISIBLE && mAddCouponCustomerFirstName.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter customer first name",mAddCouponCustomerFirstName);
					}else if(mStoreOwnerAddCouponCustomerDetailsContainer.getVisibility() == View.VISIBLE && mAddCouponCustomerLastName.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter customer last name",mAddCouponCustomerLastName);
					}else if(mStoreOwnerAddCouponCustomerDetailsContainer.getVisibility() == View.VISIBLE && mAddCouponEmailAddress.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter customer email address",mAddCouponEmailAddress);
					}else if(mStoreOwnerAddCouponCustomerDetailsContainer.getVisibility() == View.VISIBLE && !Patterns.EMAIL_ADDRESS.matcher(mAddCouponEmailAddress.getText().toString()).matches()){
						alertBox_service("Information", "Please enter valid customer email address",mAddCouponEmailAddress);
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
								Add_EditCouponAsyncTask mAddCoupon = new Add_EditCouponAsyncTask(StoreOwnerAdd_EditCoupon.this,mAddCouponBarcodeImage,class_name);
								mAddCoupon.execute("update_coupon",mAddCouponType.getText().toString(),mLocationId,mCustomerUserId,mAddCouponTitle.getText().toString(),mAddCouponCode.getText().toString(),
										mActivationDate,mExpirationDate,mIsCouponOneTimeUse,mAddCouponDescription.getText().toString(),mSelectedCouponId);	
							}else{ // for adding Coupons
								String couponType= "";
								if(getIntent().hasExtra("FromCustomerCenter") || getIntent().hasExtra("FromContactStore")){
									couponType = "Private";
									mCustomerUserId = getIntent().getExtras().getString("customer_id");
								}else{
									couponType = mAddCouponType.getText().toString();
								}
								Add_EditCouponAsyncTask mAddCoupon = new Add_EditCouponAsyncTask(StoreOwnerAdd_EditCoupon.this,mAddCouponBarcodeImage,class_name);
								mAddCoupon.execute("create_coupon",couponType,mLocationId,mCustomerUserId,mAddCouponTitle.getText().toString(),mAddCouponCode.getText().toString(),
										mActivationDate,mExpirationDate,mIsCouponOneTimeUse,mAddCouponDescription.getText().toString(),mProfilePhoto,mAddCouponCustomerFirstName.getText().toString(),mAddCouponCustomerLastName.getText().toString(),mAddCouponEmailAddress.getText().toString(),mAddCouponMobileNumber.getText().toString(),""); // Last paramater is coupon id (only needed for edit coupons)
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
						mPreviousMobileNumber = mAddCouponMobileNumber.getText().toString();
						if(!mAddCouponMobileNumber.getText().toString().trim().equalsIgnoreCase("") && (!mPreviousMobileNumber.equalsIgnoreCase(mUpdatedMobileNumber))){
							mUpdatedMobileNumber = mAddCouponMobileNumber.getText().toString();
							if(new NetworkCheck().ConnectivityCheck(StoreOwnerAdd_EditCoupon.this)){
								InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
								inputMethodManager.hideSoftInputFromWindow(mAddCouponMobileNumber.getWindowToken(), 0);
								CheckZouponsCustomerTask mCheckCustomer = new CheckZouponsCustomerTask(StoreOwnerAdd_EditCoupon.this,mAddCouponMobileNumber,mStoreOwnerAddCouponCustomerDetailsContainer);
								mCheckCustomer.execute(mUpdatedMobileNumber);	
							}else{
								Toast.makeText(StoreOwnerAdd_EditCoupon.this, "No Network Connection", Toast.LENGTH_SHORT).show();
							}

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
		}else if(v.equals(mAddCouponCustomerProfileImage)){
			menu.setHeaderTitle("Choose From");
			menu.add(1, 1, 0, "Take Picture");
			menu.add(1, 2, 0, "Gallery");
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
		case 1:
			if(item.getItemId() == 1){
				Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent_camera, 2);
			}else if(item.getItemId() == 2){
				Intent mGalleryIntent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(mGalleryIntent, 1);
			} 
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG,"RequestCode: "+requestCode);
		Log.i(TAG,"ResultCode: "+resultCode);
		switch (requestCode) {
		 case 1: //1
			if(resultCode == RESULT_OK){
				setScaledBitmap(data);
			}
			break;
		case 2: //1
			if(resultCode == RESULT_OK){
				setScaledBitmap(data);
			}
			break;
		default:
		}
	}
	
	public void setScaledBitmap(Intent data){
		try{
			Uri uri=data.getData();
			Log.i("uri"," "+uri);
			// specifying column(data) for retrieval
			String[] file_path_columns={MediaStore.Images.Media.DATA};
			// querying content provider to get particular image
			Cursor cursor=getContentResolver().query(uri, file_path_columns, null, null, null);
			cursor.moveToFirst();
			// getting col_index from string file_path_columns[0]--> Data column 
			int column_index=cursor.getColumnIndex(file_path_columns[0]);
			// getting the path from result as /sdcard/DCIM/100ANDRO/file_name
			String selected_file_path=cursor.getString(column_index);
			Log.i("selected path"," "+selected_file_path);	
			cursor.close();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(selected_file_path, options);
			// Calculate inSampleSize
			options.inSampleSize = Settings.calculateInSampleSize(options, 120,135 );
			Log.i("sample size", options.inSampleSize+" ");
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap mSelectedImage = BitmapFactory.decodeFile(selected_file_path, options);
			mSelectedImage = new DecodeImageWithRotation().decodeImage(selected_file_path, mSelectedImage, 120,135);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);    
			byte[] imagedata = baos.toByteArray();
			mProfilePhoto = com.us.zoupons.Base64.encodeBytes(imagedata);
			//mUserProfileImage.setImageBitmap(mSelectedImage);
			mSelectedImage = Bitmap.createScaledBitmap(mSelectedImage, 120,135 , true);
			BitmapDrawable drawable = new BitmapDrawable(mSelectedImage);
			mAddCouponCustomerProfileImage.setImageDrawable(drawable);
			//mAddImageText.setVisibility(View.GONE);
		}catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Please select valid image.", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	public void openDatePicker(final String dateType){
		DatePickerDialog mDatePicker = new DatePickerDialog(StoreOwnerAdd_EditCoupon.this,new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				try{
					if(dateType.equalsIgnoreCase("ActivationDate")){ // set selected date in activation date field
						Calendar mCurrentTime = Calendar.getInstance();
						int mDate = mCurrentTime.get(Calendar.DAY_OF_MONTH);
						int mMonth = mCurrentTime.get(Calendar.MONTH);
						int mYear = mCurrentTime.get(Calendar.YEAR);	
						Date mCurrentDate = new Date(mYear,mMonth+1,mDate);
						Date mSelectedDate = new Date(year, monthOfYear+1, dayOfMonth);
						Log.i("current date", mCurrentDate.getYear()+"-"+mCurrentDate.getMonth()+"/"+mCurrentDate.getDay());
						Log.i("selected date", mSelectedDate.getYear()+"/"+mSelectedDate.getMonth()+"/"+mSelectedDate.getDay());
						mAddCouponActivationDate.setText(mSelectedDate.getMonth()+"/"+mSelectedDate.getDate()+"/"+mSelectedDate.getYear());	
						mActivationDate = mSelectedDate.getYear()+"/"+mSelectedDate.getMonth()+"/"+mSelectedDate.getDate();
						
					}else{  // set selected date in exiration date field
						if(mAddCouponActivationDate.getText().toString().length()>0){
							String mActivationDate = mAddCouponActivationDate.getText().toString();
							int mDate = Integer.parseInt(mActivationDate.split("/")[1]); 
							int mMonth = Integer.parseInt(mActivationDate.split("/")[0]);
							int mYear = Integer.parseInt(mActivationDate.split("/")[2]);	
							Date mCurrentDate = new Date(mYear,mMonth,mDate);
							Date mSelectedDate = new Date(year, monthOfYear+1, dayOfMonth);
							Log.i("current date", mCurrentDate.getYear()+"-"+mCurrentDate.getMonth()+"/"+mCurrentDate.getDay());
							Log.i("selected date", mSelectedDate.getYear()+"/"+mSelectedDate.getMonth()+"/"+mSelectedDate.getDay());
							if(getIntent().hasExtra("choosed_position")){ // Edit coupon details , No date validation needed
								mAddCouponExpirationDate.setText(mSelectedDate.getMonth()+"/"+mSelectedDate.getDate()+"/"+mSelectedDate.getYear());
								mExpirationDate = mSelectedDate.getYear()+"/"+mSelectedDate.getMonth()+"/"+mSelectedDate.getDate();
							}else{
								if(mCurrentDate.compareTo(mSelectedDate) <= 0){
									Log.i("selected date", mSelectedDate.getDate()+"/"+mSelectedDate.getMonth()+"/"+mSelectedDate.getYear());
									mAddCouponExpirationDate.setText(mSelectedDate.getMonth()+"/"+mSelectedDate.getDate()+"/"+mSelectedDate.getYear());
									mExpirationDate = mSelectedDate.getYear()+"/"+mSelectedDate.getMonth()+"/"+mSelectedDate.getDate();
								}else{
									AlertDialog.Builder mAlertBox = new AlertDialog.Builder(StoreOwnerAdd_EditCoupon.this);
									mAlertBox.setTitle("Information");
									mAlertBox.setMessage("Please choose future date than Activation date");
									mAlertBox.setPositiveButton("ok", null);
									mAlertBox.show();
								}
							}
							
							
						}else{
							AlertDialog.Builder mAlertBox = new AlertDialog.Builder(StoreOwnerAdd_EditCoupon.this);
							mAlertBox.setTitle("Information");
							mAlertBox.setMessage("Please choose Activation date first");
							mAlertBox.setPositiveButton("ok", null);
							mAlertBox.show();
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		mDatePicker.show();
	}

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
			Log.i("image view height and width",mImageView.getWidth() +" "+mImageView.getHeight());
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

	public Bitmap getBitmapFromURL(String src) {
		try {
			Log.i(TAG,"Coupon Image URL : "+src);
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
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwnerAdd_EditCoupon.this);
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
		new RefreshZoupons().isApplicationGoneBackground(StoreOwnerAdd_EditCoupon.this);
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
						header.mTabBarNotificationCountBtn.setVisibility(View.VISIBLE);
						header.mTabBarNotificationCountBtn.setText(String.valueOf(NotificationDetails.notificationcount));
					}else{
						header.mTabBarNotificationCountBtn.setVisibility(View.GONE);
						Log.i("Notification result", "No new notification");
					}
				}					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};


}
