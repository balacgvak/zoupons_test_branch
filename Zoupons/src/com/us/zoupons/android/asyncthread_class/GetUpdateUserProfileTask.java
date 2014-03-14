package com.us.zoupons.android.asyncthread_class;

import java.io.IOException;

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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.MobileCarriers_ClassVariables;
import com.us.zoupons.classvariables.POJOUserProfile;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.cards.ImageLoader;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to update user profile details
 *
 */

public class GetUpdateUserProfileTask extends AsyncTask<String,String, String>{
	
	private Context mContext;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponswebservice = null;
 	private ZouponsParsingClass mParsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	private String mGetUserProfileResponse="",mParseUserProfileResponse=""; 	
 	private String mFlagString="";
 	//For User Profile Update
 	private String mUserFirstName="",mUserLastName="",mUserMobileNumber="",mUserMobileCarrier="",mUserImage="";
 	private String mUpdateUserInfoResponse="",mParseUpdateUserInfo="",mEventFlag="",mVerificationCode="";
 	private EditText mUserFirstNameField,mUserLastNameField,mUserEmailField,mUserMobileNumberField,mUserMobileCarrierField,mVerificationCodeValue;
    private ImageView mUserProfileImageField;
    private ImageLoader imageLoader;
 	private ScrollView ContactInfoView;
 	private RelativeLayout VerificationPopupLayout;
    
	public GetUpdateUserProfileTask(Context context,
			String mFlag, ImageView UserProfileImage, EditText UserFirstName, EditText UserLastName, EditText UserEmail, EditText UserMobileNumber, EditText UserMobileCarrier) {
		this.mContext = context;	
		this.mFlagString = mFlag;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
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
		this.mContext = context;		
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
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
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
	protected String doInBackground(String... params) {
		String result="";
		try{			
		     if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){ // Network connectivity check
		    	 if(mFlagString.equalsIgnoreCase("GetUserInfo")){
			    	 mGetUserProfileResponse = mZouponswebservice.mGetUserProfile(UserDetails.mServiceUserId);	
			    	 if(!mGetUserProfileResponse.equals("failure") && !mGetUserProfileResponse.equals("noresponse")){
						mParseUserProfileResponse= mParsingclass.mParseUserProfile(mGetUserProfileResponse);
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
							result="Response Error.";
						}else if(mParseUserProfileResponse.equalsIgnoreCase("norecords")){
							result="No Records";
						}					
					}else{
						result="Response Error.";
					}
		    	 }else if(mFlagString.equalsIgnoreCase("UpdateUserInfo")){
		    		 mUpdateUserInfoResponse = mZouponswebservice.mUpdateUserProfile(UserDetails.mServiceUserId,mUserFirstName,
		    				 mUserLastName,mUserMobileNumber,mUserMobileCarrier,mUserImage,mEventFlag,mVerificationCode);	
					  if(!mUpdateUserInfoResponse.equals("failure") && !mUpdateUserInfoResponse.equals("noresponse")){
						mParseUpdateUserInfo= mParsingclass.mParseUserProfile(mUpdateUserInfoResponse);
						if(mParseUpdateUserInfo.equalsIgnoreCase("success")){	
							for(int i=0;i<WebServiceStaticArrays.mUserProfileList.size();i++){
								POJOUserProfile mUserProfile = (POJOUserProfile) WebServiceStaticArrays.mUserProfileList.get(i);
								if(mUserProfile.mMessage.length()>2){
									if(mUserProfile.mMessage.equalsIgnoreCase("Successfully Updated Profile") ||mUserProfile.mMessage.equalsIgnoreCase("Successfully Updated Profile with new Mobile Number")){
										result= mUserProfile.mMessage;
									}else if(mUserProfile.mMessage.equalsIgnoreCase("Successfully sent Verification Code")){
										result = mUserProfile.mMessage;
									}else if(mUserProfile.mMessage.equalsIgnoreCase("Invalid verification code")){
										result = mUserProfile.mMessage;
									}else if(mUserProfile.mMessage.equalsIgnoreCase("Mobile number already exists")){
										result = mUserProfile.mMessage;
									}
									break;
								}
							}			
						}else if(mParseUpdateUserInfo.equalsIgnoreCase("failure")){
							result="Response Error.";
						}else if(mParseUpdateUserInfo.equalsIgnoreCase("norecords")){
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
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(mProgressdialog != null && mProgressdialog.isShowing()){
			mProgressdialog.dismiss();
		}	
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.") || result.equals("failure")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equals("No User Profile Available")){
			alertBox_service("Information", result);
		}else if(result.equals("Successfully sent Verification Code")){
			alertBox_service("Information", "We just text you a mobile verification code.  Enter code to proceed.");
		}else if(result.equalsIgnoreCase("Invalid verification code")){
			alertBox_service("Information", "Invalid verification code.");
		}else if(result.equalsIgnoreCase("Mobile number already exists")){
			alertBox_service("Information", "Mobile number already exists.");
		}else{
			 if(mFlagString.equalsIgnoreCase("GetUserInfo")){
				POJOUserProfile mUserProfile = (POJOUserProfile) WebServiceStaticArrays.mUserProfileList.get(0);
				imageLoader = new ImageLoader(mContext);
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
				 if(result.equalsIgnoreCase("Successfully Updated Profile")){
					 alertBox_service("Information", "Successfully updated the profile");	 
				 }else{
					 alertBox_service("Information", "Successfully updated mobile number");
				 }
				 
			 }
		}
	}
	
	public Bitmap getBitmapFromURL(String src) {
		Bitmap rtn = null;
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
			rtn = mBitmap;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		return rtn;
	}
	
	
	/* To show alert pop up with respective message */
	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equalsIgnoreCase("We just text you a mobile verification code.  Enter code to proceed.")){
					ContactInfoView.setVisibility(View.GONE);
					VerificationPopupLayout.setVisibility(View.VISIBLE);
					mVerificationCodeValue.getText().clear();
					mVerificationCodeValue.requestFocus();
					InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
				}else if(msg.equalsIgnoreCase("Profile Updated Successfully") || msg.equalsIgnoreCase("Profile information has been updated successfully")){
					ContactInfoView.setVisibility(View.VISIBLE);
					VerificationPopupLayout.setVisibility(View.GONE);
				}
			}
		});
		service_alert.show();
	}
}