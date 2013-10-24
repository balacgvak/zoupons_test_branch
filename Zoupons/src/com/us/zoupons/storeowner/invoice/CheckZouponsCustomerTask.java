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
import com.us.zoupons.ClassVariables.POJOUserProfile;
import com.us.zoupons.cards.ImageLoader;
import com.us.zoupons.storeowner.Coupons.StoreOwnerAdd_EditCoupon;
import com.us.zoupons.storeowner.refunds.CustomRefundDetailsAdapter;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;
import com.us.zoupons.zpay.GetUrlImageAsyncTask;

public class CheckZouponsCustomerTask extends AsyncTask<String, String, ArrayList<Object>>{

	private Activity mContext;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String TAG="StoreOwnerInvoiceAsynchTask";
	private ScrollView mCustomerDetailsLayout;
	private LinearLayout mTelephoneLayout,mEmailField,mBackLayout,mMenuSplitter,mRefundCustomerDetailsLayout,mStoreOwnerAddCouponCustomerDetailsContainer;
	private ImageView mCustomerImage;
	private RelativeLayout mStoreInfoHeader;
	private ListView mRefundsList;
	private EditText mAddCouponMobileNumber,mCustomerFirstName,mCustomerLastName,mInvoiceAmountValue;
	private TextView mCustomerFirstNameText,mCustomerLastNameText;
	private ViewGroup mFooterLayout;

	// For Invoice
	public CheckZouponsCustomerTask(Activity context,LinearLayout TelephoneLayout,ScrollView customerdetails,ImageView mCustomerImage,EditText StoreOwnerCustomerInvoiceFirstName, EditText StoreOwnerCustomerInvoiceLastName,TextView StoreOwnerCustomerInvoiceFirstNameText, TextView StoreOwnerCustomerInvoiceLastNameText, LinearLayout mEmailField, ViewGroup footerLayout,EditText storeOwnerCustomerInvoiceAmount) {
		this.mContext = context;
		this.mTelephoneLayout = TelephoneLayout;
		this.mCustomerDetailsLayout = customerdetails;
		this.mEmailField = mEmailField;
		this.mCustomerImage = mCustomerImage;
		this.mFooterLayout = footerLayout;
		this.mCustomerFirstName = StoreOwnerCustomerInvoiceFirstName;
		this.mCustomerLastName = StoreOwnerCustomerInvoiceLastName;
		this.mCustomerFirstNameText = StoreOwnerCustomerInvoiceFirstNameText;
		this.mCustomerLastNameText = StoreOwnerCustomerInvoiceLastNameText;
		this.mInvoiceAmountValue = storeOwnerCustomerInvoiceAmount;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	// For Giftcards
	public CheckZouponsCustomerTask(Activity context,LinearLayout TelephoneLayout,ScrollView customerdetails,ImageView mCustomerImage, LinearLayout BackLayout,RelativeLayout storeinfoheader, TextView GiftcardPurchaseCustomerFirstName, TextView GiftcardPurchaseCustomerLastName) {
		this.mContext = context;
		this.mTelephoneLayout = TelephoneLayout;
		this.mCustomerDetailsLayout = customerdetails;
		this.mCustomerImage = mCustomerImage;
		this.mBackLayout = BackLayout;
		this.mStoreInfoHeader = storeinfoheader;
		this.mCustomerFirstNameText = GiftcardPurchaseCustomerFirstName;
		this.mCustomerLastNameText = GiftcardPurchaseCustomerLastName;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	// For refund Details
	public CheckZouponsCustomerTask(Activity context,LinearLayout StoreOwnerRefundTelephoneLayout,LinearLayout StoreOwnerRefundCustomerDetailsContainer,ImageView StoreOwnerRefundCustomerImage,ListView  refundsList,LinearLayout BackLayout) {
		this.mContext = context;
		this.mTelephoneLayout = StoreOwnerRefundTelephoneLayout;
		this.mRefundCustomerDetailsLayout = StoreOwnerRefundCustomerDetailsContainer;
		this.mCustomerImage = StoreOwnerRefundCustomerImage;
		this.mBackLayout = BackLayout;
		this.mRefundsList = refundsList;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	// For Add coupons
	public CheckZouponsCustomerTask(Activity context, EditText addCouponMobileNumber, LinearLayout storeOwnerAddCouponCustomerDetailsContainer) {
		this.mContext = context;
		this.mAddCouponMobileNumber = addCouponMobileNumber;
		this.mStoreOwnerAddCouponCustomerDetailsContainer = storeOwnerAddCouponCustomerDetailsContainer;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	@Override
	protected ArrayList<Object> doInBackground(String... params) {
		String mMobileNumber = params[0];
		try{
			String mResponse=zouponswebservice.mGetUserProfile(mMobileNumber,"");
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					return parsingclass.parseUserProfile(mResponse,"");
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
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			Log.i("Task", "onPostExecute");		
			if(result!=null && result.size() >0){
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
						mCustomerLastName.setTag(userprofile.mStatus);
						mContext.registerForContextMenu(mCustomerImage);
					}

				}else if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwnerAdd_EditCoupon")){
					POJOUserProfile userprofile = (POJOUserProfile) result.get(0);
					Log.i("success", userprofile.mUserEmail);
					Log.i("success", userprofile.mMessage);
					Log.i("url", userprofile.mUserImage);
					StoreOwnerAdd_EditCoupon.mCustomerUserId = userprofile.mUserId;
					if(userprofile.mStatus.equalsIgnoreCase("Active")){
						mStoreOwnerAddCouponCustomerDetailsContainer.setVisibility(View.GONE);
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

						GetUrlImageAsyncTask geturlimagetask = new GetUrlImageAsyncTask(mContext, mProfileImage, mProgressBar);
						geturlimagetask.execute(userprofile.mUserImage);

						mProfileName.setText(userprofile.mUserFirstName+" "+userprofile.mUserLastName);
						mCancelButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mCustomerDetailsDialog.dismiss();
								mAddCouponMobileNumber.getText().clear();
							}
						});
						mProceedButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								mCustomerDetailsDialog.dismiss();
							}
						});
					}else{
						mAddCouponMobileNumber.getText().clear();
						alertBox_service("Information", "Entered mobile number does not match with any customer.");
						//mStoreOwnerAddCouponCustomerDetailsContainer.setVisibility(View.VISIBLE);
					}
				}else{  // For giftcards
					POJOUserProfile userprofile = (POJOUserProfile) result.get(0);
					if(userprofile.mStatus.equalsIgnoreCase("Active")){
						mTelephoneLayout.setVisibility(View.GONE);
						mCustomerDetailsLayout.setVisibility(View.VISIBLE);
						mBackLayout.setVisibility(View.VISIBLE);
						mStoreInfoHeader.setVisibility(View.GONE);
						mCustomerFirstNameText.setTag(userprofile.mUserId);  // To use it during submit invoice....
						mCustomerFirstNameText.setText(userprofile.mUserFirstName);
						mCustomerLastNameText.setText(userprofile.mUserLastName);
						ImageLoader imageLoader = new ImageLoader(mContext);
						imageLoader.DisplayImage(userprofile.mUserImage+"&w="+120+"&h="+135+"&zc=0", mCustomerImage);
					}else{
						alertBox_service("Information", "Entered mobile number does not match with any customer.");
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
	protected void onPreExecute() {
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}


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


