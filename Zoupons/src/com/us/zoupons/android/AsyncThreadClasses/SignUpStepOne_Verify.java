package com.us.zoupons.android.AsyncThreadClasses;

import java.util.Timer;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.AdvertisementTimerTask;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.Registration;
import com.us.zoupons.ClassVariables.SendMobileVerfication_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class SignUpStepOne_Verify extends AsyncTask<String, String, String>{

	Registration mContext;
	String TAG="SendMobileVerficationAsyncTask";
	private ProgressDialog progressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	String mEventFlag;
	public EditText mMobileNumberValue/*,mMobileActivationCodeValue*/,mEmailIdValue,mVerificationCode;
	public TextView mMobileActivationCode,mPopUpHeader;
	public LinearLayout mEmailIdContainer,mContactInformationStep1,mContactInformationStep2;
	public ScrollView mSignUpStepOne;
	public RelativeLayout mVerificationPopUp;
	public Button mNext,mCancel;
	public LinearLayout mRegistrationStep1,mRegistrationStep2;
	public RelativeLayout mMobileNumberContainer;
	ImageView mAdvertisement_image;
	ProgressBar mAd_ProgressBar;
	AdvertisementTimerTask mTimerTask;
	Timer mTimer;
	
	public SignUpStepOne_Verify(Registration context,String eventflag,EditText mobilenumbervalue_edt,TextView mobileactivationcode_txt,/*EditText mobileactivationcodevalue_edt,*/EditText emailidvalue_edt,LinearLayout emailidcontainer,ScrollView signupstepone,RelativeLayout verificationpopup,Button next,LinearLayout registrationstep1,LinearLayout registrationstep2
			,TextView popupheadertext,EditText verificationcode,Button step1cancel,RelativeLayout mobilenumbercontainer,ImageView Advertisement_image,
				ProgressBar Ad_ProgressBar,LinearLayout contactInfoStep1,LinearLayout contactInfoStep2,AdvertisementTimerTask timertask,Timer timer){
		this.mContext=context;
		this.mEventFlag=eventflag;
		this.mMobileNumberValue=mobilenumbervalue_edt;
		this.mMobileActivationCode=mobileactivationcode_txt;
		this.mEmailIdValue=emailidvalue_edt;
		this.mEmailIdContainer=emailidcontainer;
		this.mSignUpStepOne=signupstepone;
		this.mContactInformationStep1 = contactInfoStep1;
		this.mContactInformationStep2 = contactInfoStep2;
		this.mVerificationPopUp = verificationpopup;
		this.mNext=next;
		this.mRegistrationStep1=registrationstep1;
		this.mRegistrationStep2=registrationstep2;
		this.mVerificationCode=verificationcode;
		this.mPopUpHeader=popupheadertext;
		this.mCancel=step1cancel;
		this.mMobileNumberContainer=mobilenumbercontainer;
		this.mAd_ProgressBar = Ad_ProgressBar;
		this.mAdvertisement_image = Advertisement_image;
		this.mTimerTask = timertask;
		this.mTimer = timer;
		
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.mContext);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPreExecute() {
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);

		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				mGetResponse=zouponswebservice.getSignUpStep1_Verify(this.mEventFlag, 
						params[0], 
						params[1], 
						params[2], 
						params[3], 
						params[4], 
						params[5], 
						params[6]);	//check
				
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=parsingclass.parseSignUpStepOne(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						Log.i(TAG,"MobileVerificationCode List Size: "+WebServiceStaticArrays.mSendMobileVerificationCode.size());
						for(int i=0;i<WebServiceStaticArrays.mSendMobileVerificationCode.size();i++){
							final SendMobileVerfication_ClassVariables parsedobjectvalues = (SendMobileVerfication_ClassVariables) WebServiceStaticArrays.mSendMobileVerificationCode.get(i);
							if(!parsedobjectvalues.mMessage.equals("")){
								if(!parsedobjectvalues.mUserId.equals("")){
									Registration.mUserId=parsedobjectvalues.mUserId;
								}else{
									Log.i(TAG,"NO USER ID");
								}
								result =parsedobjectvalues.mMessage;
							}
						}
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						Log.i(TAG,"Error");
						result="Response Error.";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						Log.i(TAG,"No Records");
						result="No Stores Available.";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			Log.i(TAG,"Thread Error");
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(String result) {
		
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else {
			if(result.equals("Successfully Verified Mobile")){
				alertBox_service("Information", "Successfully Verified Mobile");
			}else if(result.equals("Successfully sent Temporary Password")){
				alertBox_service("Information", "A temporary password has been sent to your email address.  Please validate your email account");
			}else if(result.equals("Failure")){
				alertBox_service("Information", "Unable to reach service.");
			}else{
				alertBox_service("Information", result);
			}
		}
		progressdialog.dismiss();
		super.onPostExecute(result);
	}
	
	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(msg.equalsIgnoreCase("Successfully Verified Mobile")){
					mVerificationPopUp.setVisibility(View.GONE);
					mSignUpStepOne.setVisibility(View.VISIBLE);
					
					mContactInformationStep1.setVisibility(View.GONE);
					mContactInformationStep2.setVisibility(View.VISIBLE);
					mCancel.setVisibility(View.VISIBLE);
					mNext.setVisibility(View.VISIBLE);
					RelativeLayout.LayoutParams mCancelParams = new RelativeLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.cancelbuttonwidth), mContext.getResources().getDimensionPixelSize(R.dimen.cancelbuttonheight));
					mCancelParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					mCancel.setLayoutParams(mCancelParams);
					
					mNext.setText("Save");
					 //To get security question for step2
					mContext.progress_SecurityQuestions();
					
					// To cancel advertisement timer and task
					if(mTimer != null){
						mTimer.cancel();
						mTimer = null;
					}
					if(mTimerTask != null){
						mTimerTask.cancel();
					}
				}else if(msg.equalsIgnoreCase("A temporary password has been sent to your email address.  Please validate your email account")){
					mSignUpStepOne.setVisibility(View.GONE);
					mVerificationPopUp.setVisibility(View.VISIBLE);
					// To give focus to verification code editText and open keyboard
					mVerificationCode.requestFocus();
					InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
					
					mPopUpHeader.setText("Enter Email Verification Code");
					mVerificationCode.setHint("Enter Email Verification Code");
					if(mTimer == null){
						mTimer = new Timer();
						mTimerTask = new AdvertisementTimerTask(mContext, mAdvertisement_image, mAd_ProgressBar,"email_activation");
						mTimer.schedule(mTimerTask, 0, 4000);
					}else{
						mTimer.schedule(mTimerTask, 0, 4000);	
					}
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}
