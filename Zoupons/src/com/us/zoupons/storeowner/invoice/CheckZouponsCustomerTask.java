package com.us.zoupons.storeowner.invoice;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.classvariables.POJOUserProfile;
import com.us.zoupons.mobilepay.GetUrlImageAsyncTask;
import com.us.zoupons.shopper.cards.ImageLoader;
import com.us.zoupons.storeowner.coupons.StoreOwnerAdd_EditCoupon;
import com.us.zoupons.storeowner.refunds.CustomRefundDetailsAdapter;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task to communicate with server to validate secret PIN 
 *
 */

public class CheckZouponsCustomerTask extends AsyncTask<String, String, ArrayList<Object>>{

	private Activity mContext;
	private StoreownerWebserivce mZouponswebservice=null;
	private StoreownerParsingclass mParsingclass=null;
	private ProgressDialog mProgressdialog=null;
	private ScrollView mCustomerDetailsLayout;
	private LinearLayout mTelephoneLayout,mEmailField,mBackLayout,mRefundCustomerDetailsLayout,mNonZouponsCustomerDetailsContainer,mNonZouponsCustomerCardDetailsContainer,mEmployeePinContainer;
	private ImageView mCustomerImage;
	private RelativeLayout mStoreInfoHeader,mCustomerDetailsContainer,mZouponsCustomerDetails;
	private ListView mRefundsList;
	private EditText mAddCouponMobileNumber,mCustomerFirstName,mCustomerLastName,mInvoiceAmountValue;
	private TextView mCustomerFirstNameText,mCustomerLastNameText;
	private ViewGroup mFooterLayout;

	// For Invoice
	public CheckZouponsCustomerTask(Activity context,LinearLayout TelephoneLayout,ScrollView customerdetails,RelativeLayout customer_details_container,ImageView mCustomerImage,EditText StoreOwnerCustomerInvoiceFirstName, EditText StoreOwnerCustomerInvoiceLastName,TextView StoreOwnerCustomerInvoiceFirstNameText, TextView StoreOwnerCustomerInvoiceLastNameText, LinearLayout mEmailField, ViewGroup footerLayout,EditText storeOwnerCustomerInvoiceAmount) {
		this.mContext = context;
		this.mTelephoneLayout = TelephoneLayout;
		this.mCustomerDetailsContainer = customer_details_container;
		this.mCustomerDetailsLayout = customerdetails;
		this.mEmailField = mEmailField;
		this.mCustomerImage = mCustomerImage;
		this.mFooterLayout = footerLayout;
		this.mCustomerFirstName = StoreOwnerCustomerInvoiceFirstName;
		this.mCustomerLastName = StoreOwnerCustomerInvoiceLastName;
		this.mCustomerFirstNameText = StoreOwnerCustomerInvoiceFirstNameText;
		this.mCustomerLastNameText = StoreOwnerCustomerInvoiceLastNameText;
		this.mInvoiceAmountValue = storeOwnerCustomerInvoiceAmount;
		mZouponswebservice= new StoreownerWebserivce(context);
		mParsingclass= new StoreownerParsingclass(this.mContext);
		mProgressdialog=new ProgressDialog(this.mContext);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	// For Giftcards
	public CheckZouponsCustomerTask(Activity context,LinearLayout TelephoneLayout,ScrollView customerdetails,ImageView mCustomerImage, LinearLayout BackLayout,RelativeLayout storeinfoheader, TextView GiftcardPurchaseCustomerFirstName, TextView GiftcardPurchaseCustomerLastName,RelativeLayout zouponscustomerdetails, LinearLayout nonZouponsCustomerDetailsContainer,LinearLayout nonZouponsCustomerCardDetailscontainer,LinearLayout employeePinContainer) {
		this.mContext = context;
		this.mTelephoneLayout = TelephoneLayout;
		this.mCustomerDetailsLayout = customerdetails;
		this.mCustomerImage = mCustomerImage;
		this.mBackLayout = BackLayout;
		this.mStoreInfoHeader = storeinfoheader;
		this.mCustomerFirstNameText = GiftcardPurchaseCustomerFirstName;
		this.mCustomerLastNameText = GiftcardPurchaseCustomerLastName;
		this.mZouponsCustomerDetails = zouponscustomerdetails;
		this.mNonZouponsCustomerDetailsContainer = nonZouponsCustomerDetailsContainer;
		this.mNonZouponsCustomerCardDetailsContainer = nonZouponsCustomerCardDetailscontainer;
		this.mEmployeePinContainer = employeePinContainer;
		mZouponswebservice= new StoreownerWebserivce(context);
		mParsingclass= new StoreownerParsingclass(this.mContext);
		mProgressdialog=new ProgressDialog(this.mContext);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	
	// For refund Details
	public CheckZouponsCustomerTask(Activity context,LinearLayout StoreOwnerRefundTelephoneLayout,LinearLayout StoreOwnerRefundCustomerDetailsContainer,ImageView StoreOwnerRefundCustomerImage,ListView  refundsList,LinearLayout BackLayout) {
		this.mContext = context;
		this.mTelephoneLayout = StoreOwnerRefundTelephoneLayout;
		this.mRefundCustomerDetailsLayout = StoreOwnerRefundCustomerDetailsContainer;
		this.mCustomerImage = StoreOwnerRefundCustomerImage;
		this.mBackLayout = BackLayout;
		this.mRefundsList = refundsList;
		mZouponswebservice= new StoreownerWebserivce(context);
		mParsingclass= new StoreownerParsingclass(this.mContext);
		mProgressdialog=new ProgressDialog(this.mContext);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	// For Add coupons
	public CheckZouponsCustomerTask(Activity context, EditText addCouponMobileNumber) {
		this.mContext = context;
		this.mAddCouponMobileNumber = addCouponMobileNumber;
		mZouponswebservice= new StoreownerWebserivce(context);
		mParsingclass= new StoreownerParsingclass(this.mContext);
		mProgressdialog=new ProgressDialog(this.mContext);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected ArrayList<Object> doInBackground(String... params) {
		String mMobileNumber = params[0];
		try{
			String mResponse=mZouponswebservice.mGetUserProfile(mMobileNumber,"");
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					return mParsingclass.parseUserProfile(mResponse,"");
				}else{ // service issues
					return null;
				}
			}else { // failure
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}

	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		super.onPostExecute(result);
		try{
			if(mProgressdialog != null && mProgressdialog.isShowing()){
				mProgressdialog.dismiss();
			}
			Log.i("Task", "onPostExecute");		
			if(result!=null && result.size() >0){ // success response
				if(mContext.getClass().getSimpleName().equalsIgnoreCase("RefundDetails")){ // For refunds
					mTelephoneLayout.setVisibility(View.GONE);
					mRefundCustomerDetailsLayout.setVisibility(View.VISIBLE);
					mBackLayout.setVisibility(View.VISIBLE);
					mRefundsList.setAdapter(new CustomRefundDetailsAdapter(mContext, "refunds_list"));
				}else if(mContext.getClass().getSimpleName().equalsIgnoreCase("InvoiceCenter")){ // For invoice 
					POJOUserProfile userprofile = (POJOUserProfile) result.get(0);
					mTelephoneLayout.setVisibility(View.GONE);
					mCustomerDetailsLayout.setVisibility(View.VISIBLE);
					mFooterLayout.setVisibility(View.VISIBLE);
					if(userprofile.mStatus.equalsIgnoreCase("Active")){ // Existing zoupons customer
						mEmailField.setVisibility(View.GONE);
						mCustomerFirstNameText.setVisibility(View.VISIBLE);
						mCustomerLastNameText.setVisibility(View.VISIBLE);
						mCustomerFirstName.setVisibility(View.GONE);
						mCustomerLastName.setVisibility(View.GONE);
						mCustomerFirstName.setTag(userprofile.mUserId);  // To use it during submit invoice....
						mCustomerFirstNameText.setText(userprofile.mUserFirstName);
						mCustomerLastNameText.setText(userprofile.mUserLastName);
						mInvoiceAmountValue.requestFocus();
						mCustomerImage.setVisibility(View.VISIBLE);
						mCustomerDetailsContainer.setBackgroundResource(R.drawable.settings_contactborder);
						mContext.unregisterForContextMenu(mCustomerImage);
						// To show keyboard
						InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
						ImageLoader imageLoader = new ImageLoader(mContext);
						imageLoader.DisplayImage(userprofile.mUserImage+"&w="+120+"&h="+135+"&zc=0", mCustomerImage);
					}else{ // New Customer
						mEmailField.setVisibility(View.VISIBLE);
						mCustomerFirstNameText.setVisibility(View.GONE);
						mCustomerLastNameText.setVisibility(View.GONE);
						mCustomerFirstName.setVisibility(View.VISIBLE);
						mCustomerLastName.setVisibility(View.VISIBLE);
						mCustomerFirstName.setTag(userprofile.mUserId);  // To use it during submit invoice....
						mCustomerLastName.setTag(userprofile.mStatus); // To use it during submit invoice....
						mCustomerImage.setVisibility(View.GONE);
						mCustomerDetailsContainer.setBackgroundResource(0);
						mCustomerFirstName.requestFocus();
						//mContext.registerForContextMenu(mCustomerImage);
					}

				}else if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwnerAdd_EditCoupon")){ // For coupons
					POJOUserProfile userprofile = (POJOUserProfile) result.get(0);
					Log.i("success", userprofile.mUserEmail);
					Log.i("success", userprofile.mMessage);
					Log.i("url", userprofile.mUserImage);
					if(userprofile.mStatus.equalsIgnoreCase("Active")){
						StoreOwnerAdd_EditCoupon.sCustomerUserId = userprofile.mUserId;
						// Custom dialog with user details to verify customer
						final Dialog mCustomerDetailsDialog = new Dialog(mContext);
						mCustomerDetailsDialog.setTitle("Customer Details");
						mCustomerDetailsDialog.setContentView(R.layout.storeowner_customer_details_dialog);
						mCustomerDetailsDialog.setCancelable(false);
						WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
						lWindowParams.copyFrom(mCustomerDetailsDialog.getWindow().getAttributes());
						lWindowParams.width = WindowManager.LayoutParams.FILL_PARENT; 
						lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
						mCustomerDetailsDialog.getWindow().setAttributes(lWindowParams);
						mCustomerDetailsDialog.show();	
						ImageView mProfileImage = (ImageView) mCustomerDetailsDialog.findViewById(R.id.customer_profileImageId);
						TextView mProfileName = (TextView) mCustomerDetailsDialog.findViewById(R.id.customer_nameId);
						Button mCancelButton = (Button) mCustomerDetailsDialog.findViewById(R.id.customer_info_cancel_buttonId);
						Button mProceedButton = (Button) mCustomerDetailsDialog.findViewById(R.id.customer_info_proceed_buttonId);
						ProgressBar mProgressBar = (ProgressBar) mCustomerDetailsDialog.findViewById(R.id.customer_progressbar);
                        // To load user profile image
						GetUrlImageAsyncTask geturlimagetask = new GetUrlImageAsyncTask(mContext, mProfileImage, mProgressBar);
						geturlimagetask.execute(userprofile.mUserImage);
						mProfileName.setText(userprofile.mUserFirstName+" "+userprofile.mUserLastName);
						
						mCancelButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mCustomerDetailsDialog.dismiss();
								mAddCouponMobileNumber.getText().clear();
								mAddCouponMobileNumber.requestFocus();
								((StoreOwnerAdd_EditCoupon)mContext).clearMobileNumber();
							}
						});
						mProceedButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mCustomerDetailsDialog.dismiss();
								((StoreOwnerAdd_EditCoupon)mContext).setInactiveCustomerDetails("","","");
							}
						});
					}else{
						StoreOwnerAdd_EditCoupon.sCustomerUserId = userprofile.mUserId;
						// Custom dialog with user details to verify customer
						final Dialog mCustomerDetailsDialog = new Dialog(mContext);
						mCustomerDetailsDialog.setTitle("Customer Details");
						mCustomerDetailsDialog.setContentView(R.layout.storeowner_inactive_customer_dialog);
						mCustomerDetailsDialog.setCancelable(false);
						WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
						lWindowParams.copyFrom(mCustomerDetailsDialog.getWindow().getAttributes());
						lWindowParams.width = WindowManager.LayoutParams.FILL_PARENT; 
						lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
						mCustomerDetailsDialog.getWindow().setAttributes(lWindowParams);
						mCustomerDetailsDialog.show();	
						final EditText mFirstName = (EditText) mCustomerDetailsDialog.findViewById(R.id.inactive_user_firstname_value);
						final EditText mLastName = (EditText) mCustomerDetailsDialog.findViewById(R.id.inactive_user_lastname_value);
						final EditText mEmailAddress = (EditText) mCustomerDetailsDialog.findViewById(R.id.inactive_user_email_value);
						Button mCancelButton = (Button) mCustomerDetailsDialog.findViewById(R.id.inactive_details_cancel);
						Button mProceedButton = (Button) mCustomerDetailsDialog.findViewById(R.id.inactive_details_submit);
												
						mCancelButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mCustomerDetailsDialog.dismiss();
								mAddCouponMobileNumber.getText().clear();
								mAddCouponMobileNumber.requestFocus();
								((StoreOwnerAdd_EditCoupon)mContext).clearMobileNumber();
							}
						});
						
						mProceedButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if(mFirstName.getText().toString().equalsIgnoreCase("")){
									alertBox_service("Information", "Please enter first name");
								}else if(mLastName.getText().toString().equalsIgnoreCase("")){
									alertBox_service("Information", "Please enter last name");
								}else  if(mEmailAddress.getText().toString().equalsIgnoreCase("")){
									alertBox_service("Information", "Please enter email address");
								}else if(!Patterns.EMAIL_ADDRESS.matcher(mEmailAddress.getText().toString()).matches()){
									alertBox_service("Information", "Please enter valid email address");
								}else{
									((StoreOwnerAdd_EditCoupon)mContext).setInactiveCustomerDetails(mFirstName.getText().toString(), mLastName.getText().toString(), mEmailAddress.getText().toString());
									 mCustomerDetailsDialog.dismiss();	
								}
															
							}
						});
					}
				}else{  // For giftcards
					POJOUserProfile userprofile = (POJOUserProfile) result.get(0);
					if(userprofile.mStatus.equalsIgnoreCase("Active")){
						mTelephoneLayout.setVisibility(View.GONE);
						mCustomerDetailsLayout.setVisibility(View.VISIBLE);
						mZouponsCustomerDetails.setVisibility(View.VISIBLE);
						mNonZouponsCustomerDetailsContainer.setVisibility(View.GONE);
						mNonZouponsCustomerCardDetailsContainer.setVisibility(View.GONE);
						mEmployeePinContainer.setVisibility(View.VISIBLE);
						mBackLayout.setVisibility(View.VISIBLE);
						mStoreInfoHeader.setVisibility(View.GONE);
						mCustomerFirstNameText.setTag(userprofile.mUserId);  // To use it during selling giftcard
						mCustomerFirstNameText.setText(userprofile.mUserFirstName);
						mCustomerLastNameText.setText(userprofile.mUserLastName);
						ImageLoader imageLoader = new ImageLoader(mContext);
						imageLoader.DisplayImage(userprofile.mUserImage+"&w="+120+"&h="+135+"&zc=0", mCustomerImage);
					}else{
						//alertBox_service("Information", "Entered mobile number does not match with any customer.");
						mTelephoneLayout.setVisibility(View.GONE);
						mCustomerDetailsLayout.setVisibility(View.VISIBLE);
						mZouponsCustomerDetails.setVisibility(View.GONE);
						mEmployeePinContainer.setVisibility(View.GONE);
						mNonZouponsCustomerCardDetailsContainer.setVisibility(View.VISIBLE);
						mBackLayout.setVisibility(View.VISIBLE);
						mStoreInfoHeader.setVisibility(View.GONE);
						mNonZouponsCustomerDetailsContainer.setVisibility(View.VISIBLE);
						mNonZouponsCustomerDetailsContainer.setTag(userprofile.mUserId);  // To use it during selling giftcard
					}
				}
			}else if(result!=null && result.size() == 0){
				alertBox_service("Information", "No data available");
			}else{
				alertBox_service("Information", "Unable to reach service.");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

    // To show alert box with respective message 
	private void alertBox_service(String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}


