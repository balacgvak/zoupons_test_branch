package com.us.zoupons.storeowner.PointOfSale;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import com.us.zoupons.AddCardInformation;
import com.us.zoupons.ClassVariables.POJOUserProfile;
import com.us.zoupons.ClassVariables.SendMobileVerfication_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

public class ValidateCustomerUsingMobileNumber extends AsyncTask<String, String,ArrayList<Object>> {
	
	private StoreOwner_PointOfSale_Part2 mContext;
	private StoreownerWebserivce StoreOwnerwebservice=null;
	private ZouponsWebService Customerwebservice=null;
	private ZouponsParsingClass CustomerParsingclass=null;
	private StoreownerParsingclass StoreOwnerParsingclass=null;
	private ProgressDialog progressdialog=null;
	private String TAG="PointOfSalePart2AsyncTask";
	private String mobile_number,Customeruser_id,user_status,firstname,lastname,email,photo,event_flag;
	
	public ValidateCustomerUsingMobileNumber(StoreOwner_PointOfSale_Part2 context,String mobile_number) {
		this.mContext = context;
		this.mobile_number = mobile_number;
		StoreOwnerwebservice= new StoreownerWebserivce(context);
		StoreOwnerParsingclass= new StoreownerParsingclass(this.mContext);
		Customerwebservice = new ZouponsWebService(context);
		CustomerParsingclass = new ZouponsParsingClass(mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	public ValidateCustomerUsingMobileNumber(StoreOwner_PointOfSale_Part2 context,String mobile_number,String user_id,String user_status,String firstname,String lastname,String email,String photo) {
		this.mContext = context;
		this.Customeruser_id = user_id;
		this.user_status = user_status;
		this.firstname = firstname;
		this.lastname =  lastname;
		this.email = email ;
	    this.photo = photo;
		this.mobile_number = mobile_number;
		StoreOwnerwebservice= new StoreownerWebserivce(context);
		StoreOwnerParsingclass= new StoreownerParsingclass(this.mContext);
		Customerwebservice = new ZouponsWebService(context);
		CustomerParsingclass = new ZouponsParsingClass(mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	
	@Override
	protected ArrayList<Object> doInBackground(String... params) {
		try{
			String mResponse="",mMessage="",mUserId="";
			event_flag = params[0];
			ArrayList<Object> userDetailsList = null;
			if(params[0].equalsIgnoreCase("update_userdetails")){
				SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				String mLocationId = mPrefs.getString("location_id", "");
				String mUser_Id = mPrefs.getString("user_id", "");
				String mUserType = mPrefs.getString("user_type", "");
				mResponse = Customerwebservice.getSignUpStep1_Verify("verify_email","",email,"",firstname,lastname,photo,mobile_number);
			}else{
				mResponse = StoreOwnerwebservice.mGetUserProfile(mobile_number,"");
			}
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					if(params[0]!= null && params[0].equalsIgnoreCase("update_userdetails")){
						String result = CustomerParsingclass.parseSignUpStepOne(mResponse);
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
						return StoreOwnerParsingclass.parseUserProfile(mResponse,"");
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
	protected void onCancelled() {
		super.onCancelled();
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
				if(event_flag.equalsIgnoreCase("update_userdetails")){
					POJOUserProfile mUserDetails = (POJOUserProfile) result.get(0);
					if(mUserDetails.mMessage.equalsIgnoreCase("Successfully sent Temporary Password")){
						Intent intent_addcard = new Intent(mContext,AddCardInformation.class);
						intent_addcard.putExtra("class_name", "StoreOwner_PointOfSale");
						intent_addcard.putExtra("user_id", mUserDetails.mUserId);
						mContext.startActivityForResult(intent_addcard,300);
					}else{
						alertBox_service("Information", mUserDetails.mMessage);
					}
				}else{
					mContext.updateViews(result);	
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
		progressdialog = ProgressDialog.show(mContext,"fetching user details","Please Wait!",true);
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

