package com.us.zoupons.android.AsyncThreadClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.Settings;
import com.us.zoupons.ClassVariables.MobileCarriers_ClassVariables;
import com.us.zoupons.ClassVariables.POJOUserProfile;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.cards.ImageLoader;

public class GetUpdateUserProfileTask extends AsyncTask<String,String, String>{
    String TAG =GetUpdateUserProfileTask.this.getClass().getSimpleName().toString();
	Context ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
 	private ZouponsParsingClass parsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	String mGetUserProfileResponse="",mParseUserProfileResponse="",mGetMobileCarriersResponse="",mParseMobileCarriersResponse=""; 	
 	String mFlagString="";
 	
 	//For User Profile Update
 	String mUserFirstName="",mUserLastName="",mUserEmail="",mUserMobileNumber="",mUserMobileCarrier="",mUserImage="";
 	String mUpdateUserInfoResponse="",mParseUpdateUserInfo="",mEventFlag="",mVerificationCode="";
 	private EditText mUserFirstNameField,mUserLastNameField,mUserEmailField,mUserMobileNumberField,mUserMobileCarrierField,mVerificationCodeValue;
    private ImageView mUserProfileImageField;
 	ImageLoader imageLoader;
 	private ScrollView ContactInfoView;
 	private RelativeLayout VerificationPopupLayout;
    
	public GetUpdateUserProfileTask(Context context,
			String mFlag, ImageView UserProfileImage, EditText UserFirstName, EditText UserLastName, EditText UserEmail, EditText UserMobileNumber, EditText UserMobileCarrier) {
		this.ctx = context;	
		this.mFlagString = mFlag;
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		mUserFirstNameField = UserFirstName;
		mUserLastNameField = UserLastName;
		mUserEmailField = UserEmail;
		mUserMobileNumberField = UserMobileNumber;
		mUserMobileCarrierField = UserMobileCarrier;
		mUserProfileImageField = UserProfileImage;
	}
	
	public GetUpdateUserProfileTask(Context context,String mFirstName, String mLastName,
			String mMobileNumber, String mMobileCarrier, String mPhoto,
			String mFlagUpdateUserInfo,String eventFlag,String verification_code, ScrollView ContactInfoView, RelativeLayout VerificationPopupLayout, EditText VerificationCodeValue) {
		this.ctx = context;		
		this.mFlagString = mFlagUpdateUserInfo;
		this.mUserFirstName = mFirstName;
		this.mUserLastName = mLastName;		
		this.mUserMobileNumber =mMobileNumber;
		this.mUserMobileCarrier = mMobileCarrier;
		this.mUserImage = mPhoto;
		this.mEventFlag = eventFlag;
		this.mVerificationCode = verification_code;
		this.ContactInfoView = ContactInfoView;
		this.VerificationPopupLayout = VerificationPopupLayout;
		this.mVerificationCodeValue = VerificationCodeValue;
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}
	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{			
		     if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){
		    	 if(mFlagString.equalsIgnoreCase("GetUserInfo")){
			    	 mGetUserProfileResponse = zouponswebservice.mGetUserProfile(UserDetails.mServiceUserId);	
			    	 if(!mGetUserProfileResponse.equals("failure") && !mGetUserProfileResponse.equals("noresponse")){
						mParseUserProfileResponse= parsingclass.mParseUserProfile(mGetUserProfileResponse);
						if(mParseUserProfileResponse.equalsIgnoreCase("success")){	
							for(int i=0;i<WebServiceStaticArrays.mUserProfileList.size();i++){
								POJOUserProfile mUserProfile = (POJOUserProfile) WebServiceStaticArrays.mUserProfileList.get(i);
								if(mUserProfile.mMessage.length()>2){
									result = mUserProfile.mMessage;
									break;
								}else{
									result="success";
								}
							}
						}else if(mParseUserProfileResponse.equalsIgnoreCase("failure")){
							Log.i(TAG,"Error");
							result="Response Error.";
						}else if(mParseUserProfileResponse.equalsIgnoreCase("norecords")){
							Log.i(TAG,"No Records");
							result="No Records";
						}					
					}else{
						result="Response Error.";
					}
		    	 }else if(mFlagString.equalsIgnoreCase("UpdateUserInfo")){
		    		 mUpdateUserInfoResponse = zouponswebservice.mUpdateUserProfile(UserDetails.mServiceUserId,mUserFirstName,
		    				 mUserLastName,mUserMobileNumber,mUserMobileCarrier,mUserImage,mEventFlag,mVerificationCode);	
					  if(!mUpdateUserInfoResponse.equals("failure") && !mUpdateUserInfoResponse.equals("noresponse")){
						mParseUpdateUserInfo= parsingclass.mParseUserProfile(mUpdateUserInfoResponse);
						Log.i("parsing response", mParseUpdateUserInfo);
						if(mParseUpdateUserInfo.equalsIgnoreCase("success")){	
							for(int i=0;i<WebServiceStaticArrays.mUserProfileList.size();i++){
								POJOUserProfile mUserProfile = (POJOUserProfile) WebServiceStaticArrays.mUserProfileList.get(i);
								if(mUserProfile.mMessage.length()>2){
									if(mUserProfile.mMessage.equalsIgnoreCase("Successfully Updated Profile") ||mUserProfile.mMessage.equalsIgnoreCase("Successfully Updated Profile with new Mobile Number")){
										result="success";
									}else if(mUserProfile.mMessage.equalsIgnoreCase("Successfully sent Verification Code")){
										result = mUserProfile.mMessage;
									}else if(mUserProfile.mMessage.equalsIgnoreCase("Invalid verification code")){
										result = mUserProfile.mMessage;
									}								
									break;
								}
							}			
						}else if(mParseUpdateUserInfo.equalsIgnoreCase("failure")){
							Log.i(TAG,"Error");
							result="Response Error.";
						}else if(mParseUpdateUserInfo.equalsIgnoreCase("norecords")){
							Log.i(TAG,"No Records");
							result="No Records";
						}					

					}else{
						result="Response Error.";
					}
		    	 }
		     }else{
		    	 result="nonetwork";
		     }
		}catch (Exception e) {
			Log.i(TAG,"Thread Error");
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}	
		Log.i("Task", "onPostExecute");			
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.") || result.equals("failure")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equals("No User Profile Available")){
			alertBox_service("Information", result);
		}else if(result.equals("Successfully sent Verification Code")){
			alertBox_service("Information", "Verification code has been sent to your mobile number for verification.");
		}else if(result.equalsIgnoreCase("Invalid verification code")){
			alertBox_service("Information", "Invalid verification code.");
		}else{
			Log.i(TAG,"Success: ");		
			 if(mFlagString.equalsIgnoreCase("GetUserInfo")){
				POJOUserProfile mUserProfile = (POJOUserProfile) WebServiceStaticArrays.mUserProfileList.get(0);
				Settings.mProfilePhoto =  mUserProfile.mUserImage;
			    imageLoader = new ImageLoader(ctx);
			    imageLoader.DisplayImage(mUserProfile.mUserImage+"&w="+120+"&h="+135+"&zc=0", mUserProfileImageField);
			    
			    mUserFirstNameField.setText(mUserProfile.mUserFirstName);
			    mUserLastNameField.setText(mUserProfile.mUserLastName);
			    mUserEmailField.setText(mUserProfile.mUserEmail);
			    // To append hipen for mobile number because input filter does not accept number without "-"
			    if(mUserProfile.mUserMobile.length()==10){
			    	String mobilenumber = new StringBuilder(mUserProfile.mUserMobile).insert(3, '-').toString();
				    mobilenumber = new StringBuilder(mobilenumber).insert(7, '-').toString();
				    mUserMobileNumberField.setText(mobilenumber);
			    }else{
			    	mUserMobileNumberField.setText(mUserProfile.mUserMobile);
			    }
			    if(WebServiceStaticArrays.mMobileCarriersList.size()>0){
			      for(int i=0;i<WebServiceStaticArrays.mMobileCarriersList.size();i++){
			    	  MobileCarriers_ClassVariables mMobileCarriers = (MobileCarriers_ClassVariables) WebServiceStaticArrays.mMobileCarriersList.get(i);
			    	  if(mMobileCarriers.mId.equalsIgnoreCase(mUserProfile.mUserMobileCarrier)){
			    		  mUserMobileCarrierField.setText(mMobileCarriers.mName);
			    		  break;
			    	  }
			      }
			    }
			 }else if(mFlagString.equalsIgnoreCase("UpdateUserInfo")){
				 alertBox_service("Information", "Profile information has been updated successfully");
			 }
		}
	}
	
	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equalsIgnoreCase("Verification code has been sent to your mobile number for verification.")){
					ContactInfoView.setVisibility(View.GONE);
					VerificationPopupLayout.setVisibility(View.VISIBLE);
					mVerificationCodeValue.requestFocus();
					InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
				}else if(msg.equalsIgnoreCase("Profile Updated Successfully")){
					ContactInfoView.setVisibility(View.VISIBLE);
					VerificationPopupLayout.setVisibility(View.GONE);
				}
			}
		});
		service_alert.show();
	}
}