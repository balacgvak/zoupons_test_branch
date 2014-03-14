package com.us.zoupons.android.asyncthread_class;

import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.us.zoupons.classvariables.SendMobileVerfication_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.AccountLoginFlag;
import com.us.zoupons.login.Registration;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.homepage.StoreOwner_HomePage;

/**
 * 
 * Asynchronous task to communicate with webservice for mobile verfication and for sending temp password
 *
 */

public class SignUpStepOne_Verify extends AsyncTask<String, String, String>{

	private Registration mContext;
	private ProgressDialog progressdialog=null;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse;
	private String mEventFlag;
	public EditText mMobileNumberValue/*,mMobileActivationCodeValue*/,mEmailIdValue,mVerificationCode;
	public TextView mMobileActivationCode,mPopUpHeader,mVerifyEmailStepText,mVerifyMobileStepText,mChoosePasswordStepText;
	public LinearLayout mEmailIdContainer,mContactInformationStep1,mContactInformationStep2;
	private ScrollView mSignUpStepOne;
	private RelativeLayout mVerificationPopUp;
	private Button mNext,mCancel;
	public LinearLayout mRegistrationStep1,mRegistrationStep2;
	public RelativeLayout mMobileNumberContainer;
	private ImageView mAdvertisement_image;
	private ProgressBar mAd_ProgressBar;
	private AdvertisementTimerTask mTimerTask;
	private Timer mTimer;
	private boolean mShouldNavigateToHomePage;

	public SignUpStepOne_Verify(Registration context,String eventflag,EditText mobilenumbervalue_edt,TextView mobileactivationcode_txt,/*EditText mobileactivationcodevalue_edt,*/EditText emailidvalue_edt,LinearLayout emailidcontainer,ScrollView signupstepone,RelativeLayout verificationpopup,Button next,LinearLayout registrationstep1,LinearLayout registrationstep2
			,TextView popupheadertext,EditText verificationcode,Button step1cancel,RelativeLayout mobilenumbercontainer,ImageView Advertisement_image,
			ProgressBar Ad_ProgressBar,LinearLayout contactInfoStep1,LinearLayout contactInfoStep2,AdvertisementTimerTask timertask,Timer timer,TextView emailstep,TextView mobilestep,TextView choosepasswordstep,boolean should_navigate_to_homepage){
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
		this.mVerifyEmailStepText = emailstep;
		this.mVerifyMobileStepText = mobilestep;
		this.mChoosePasswordStepText = choosepasswordstep;
		this.mShouldNavigateToHomePage = should_navigate_to_homepage;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
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
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				mGetResponse=mZouponswebservice.getSignUpStep1_Verify(this.mEventFlag, 
						params[0], 
						params[1], 
						params[2], 
						params[3], 
						params[4], 
						params[5], 
						params[6]);	//check

				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=mParsingclass.parseSignUpStepOne(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						for(int i=0;i<WebServiceStaticArrays.mSendMobileVerificationCode.size();i++){
							final SendMobileVerfication_ClassVariables parsedobjectvalues = (SendMobileVerfication_ClassVariables) WebServiceStaticArrays.mSendMobileVerificationCode.get(i);
							if(!parsedobjectvalues.mMessage.equals("")){
								if(!parsedobjectvalues.mUserId.equals("")){
									Registration.sUserId=parsedobjectvalues.mUserId;
								}else{
								}
								result =parsedobjectvalues.mMessage;
							}
						}
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						result="Response Error.";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						result="No Stores Available.";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
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
		super.onPostExecute(result);
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else {
			if(result.equals("Successfully Verified Mobile")){
				
				if(mShouldNavigateToHomePage){ // If Mobile number only not verified
					SharedPreferences mPrefs = mContext.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
					String user_type = mPrefs.getString("user_type", "");
					if(user_type.equalsIgnoreCase("store_owner") || user_type.equalsIgnoreCase("store_employee")){
						Intent login_intent = new Intent(mContext,StoreOwner_HomePage.class);
						login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						mContext.startActivity(login_intent);
						mContext.finish();
					}else{ // Shopper
						AccountLoginFlag.accountUserTypeflag="Customer";
						Intent login_intent = new Intent(mContext,ShopperHomePage.class);
						login_intent.putExtra("fromlogin", true);
						login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						mContext.startActivity(login_intent);
						mContext.finish();
					}
				}else{ // Fresh sign up...
					mVerifyEmailStepText.setTextColor(mContext.getResources().getColor(R.color.guest_login_selector));
					mVerifyMobileStepText.setTextColor(mContext.getResources().getColor(R.color.guest_login_selector));
					mChoosePasswordStepText.setTextColor(mContext.getResources().getColor(R.color.black));
					mVerificationPopUp.setVisibility(View.GONE);
					mSignUpStepOne.setVisibility(View.VISIBLE);
					mContactInformationStep1.setVisibility(View.GONE);
					mContactInformationStep2.setVisibility(View.VISIBLE);
					mCancel.setVisibility(View.GONE);
					mNext.setVisibility(View.VISIBLE);
				    mNext.setText("Take Me Home");
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
				}
			}else if(result.equals("Successfully sent Temporary Password")){
				mSignUpStepOne.setVisibility(View.GONE);
				mVerificationPopUp.setVisibility(View.VISIBLE);
				// To give focus to verification code editText and open keyboard
				mVerificationCode.requestFocus();
				InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

				mPopUpHeader.setText("We just Emailed you a Verification code");
				mVerificationCode.setHint("Enter Code Here");
				if(mTimer == null){
					mTimer = new Timer();
					mTimerTask = new AdvertisementTimerTask(mContext, mAdvertisement_image, mAd_ProgressBar,"email_activation");
					mTimer.schedule(mTimerTask, 0, 4000);
				}else{
					mTimer.schedule(mTimerTask, 0, 4000);	
				}
				//alertBox_service("Information", "A temporary password has been sent to your email address.  Please validate your email account");
			}else if(result.equals("Failure")){
				alertBox_service("Information", "Unable to reach service.");
			}else{
				alertBox_service("Information", result);
			}
		}
		progressdialog.dismiss();
	}
	
	/* To show alert pop up with respective message */
	public void alertBox_service(final String title,final String msg){
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
