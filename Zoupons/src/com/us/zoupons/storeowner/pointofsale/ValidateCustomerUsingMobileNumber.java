package com.us.zoupons.storeowner.pointofsale;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import com.us.zoupons.classvariables.ManageCardAddPin_ClassVariables;
import com.us.zoupons.classvariables.POJOUserProfile;
import com.us.zoupons.classvariables.SendMobileVerfication_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.wallet.AddCreditCard;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynctask to communicate with webservice to check customer using mobile number 
 *
 */

public class ValidateCustomerUsingMobileNumber extends AsyncTask<String, String,ArrayList<Object>> {
	
	private Activity mContext;
	private StoreownerWebserivce mStoreOwnerwebservice=null;
	private ZouponsWebService mCustomerwebservice=null;
	private ZouponsParsingClass mCustomerParsingclass=null;
	private StoreownerParsingclass mStoreOwnerParsingclass=null;
	private ProgressDialog mProgressdialog=null;
	private String TAG="PointOfSalePart2AsyncTask";
	private String mMobileNumber,mFirstName,mLastName,mEmail,mPhoto,mEventFlag;
	
	public ValidateCustomerUsingMobileNumber(Activity context,String mobile_number) {
		this.mContext = context;
		this.mMobileNumber = mobile_number;
		mStoreOwnerwebservice= new StoreownerWebserivce(context);
		mStoreOwnerParsingclass= new StoreownerParsingclass(this.mContext);
		mCustomerwebservice = new ZouponsWebService(context);
		mCustomerParsingclass = new ZouponsParsingClass(mContext);
		mProgressdialog=new ProgressDialog(this.mContext);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	
	// From POS part 2 (Payment for inactive customers)  or gc/dc sell for inactive customer
	public ValidateCustomerUsingMobileNumber(Activity context,String mobile_number,String firstname,String lastname,String email,String photo) {
		this.mContext = context;
		this.mFirstName = firstname;
		this.mLastName =  lastname;
		this.mEmail = email;
	    this.mPhoto = photo;
		this.mMobileNumber = mobile_number;
		mStoreOwnerwebservice= new StoreownerWebserivce(context);
		mStoreOwnerParsingclass= new StoreownerParsingclass(this.mContext);
		mCustomerwebservice = new ZouponsWebService(context);
		mCustomerParsingclass = new ZouponsParsingClass(mContext);
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
		mProgressdialog = ProgressDialog.show(mContext,"Fetching user details","Please Wait!",true);
	}
	
	@Override
	protected ArrayList<Object> doInBackground(String... params) {
		try{
			String mResponse="",mMessage="",mUserId="";
			mEventFlag = params[0];
			ArrayList<Object> userDetailsList = null;
			if(params[0].equalsIgnoreCase("update_userdetails")){
				mResponse = mCustomerwebservice.getSignUpStep1_Verify("verify_email","",mEmail,"",mFirstName,mLastName,mPhoto,mMobileNumber);
			}else{
				mResponse = mStoreOwnerwebservice.mGetUserProfile(mMobileNumber,"");
			}
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					if(params[0]!= null && params[0].equalsIgnoreCase("update_userdetails")){
						String result = mCustomerParsingclass.parseSignUpStepOne(mResponse);
						if(result.equalsIgnoreCase("success")){
							for(int i=0;i<WebServiceStaticArrays.mSendMobileVerificationCode.size();i++){
								final SendMobileVerfication_ClassVariables parsedobjectvalues = (SendMobileVerfication_ClassVariables) WebServiceStaticArrays.mSendMobileVerificationCode.get(i);
								if(!parsedobjectvalues.mMessage.equals("")){
									Log.i(TAG,"message : "+parsedobjectvalues.mMessage);
									mUserId = parsedobjectvalues.mUserId;
									mMessage = parsedobjectvalues.mMessage;
								}
							}
							POJOUserProfile mUserDetails = new POJOUserProfile();
							mUserDetails.mMessage = mMessage;
							mUserDetails.mUserId = mUserId;
							userDetailsList = new ArrayList<Object>();
							userDetailsList.add(mUserDetails);
						}else{ 
							// Failure or no records null will return
						}
						return userDetailsList;

					}else{
						return mStoreOwnerParsingclass.parseUserProfile(mResponse,"");
					}

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
			if(result!=null && result.size() >0){
				if(mEventFlag.equalsIgnoreCase("update_userdetails")){
					POJOUserProfile mUserDetails = (POJOUserProfile) result.get(0);
					if(mUserDetails.mMessage.equalsIgnoreCase("Successfully sent Temporary Password")){
						WebServiceStaticArrays.mCardOnFiles.clear();
						ManageCardAddPin_ClassVariables.mEditCardFlag="false";
						ManageCardAddPin_ClassVariables.mAddPinFlag="true";
						WebServiceStaticArrays.mCardOnFiles.clear();
						Intent intent_addcard = new Intent(mContext,AddCreditCard.class);
						intent_addcard.putExtra("class_name", "StoreOwner_PointOfSale");
						intent_addcard.putExtra("user_id", mUserDetails.mUserId);
						mContext.startActivityForResult(intent_addcard,300);
					}else{
						alertBox_service("Information", mUserDetails.mMessage);
					}
				}else{
					((StoreOwner_PointOfSale_Part2) mContext).updateViews(result);	
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
	
	/* To show alert pop up with respective message */
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

