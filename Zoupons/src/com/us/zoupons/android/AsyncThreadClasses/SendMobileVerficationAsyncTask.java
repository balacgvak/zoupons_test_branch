package com.us.zoupons.android.AsyncThreadClasses;

import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.AdvertisementTimerTask;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.Registration;
import com.us.zoupons.ClassVariables.SendMobileVerfication_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class SendMobileVerficationAsyncTask extends AsyncTask<String, String, String>{

	Context mContext;
	String TAG="SendMobileVerficationAsyncTask";
	private ProgressDialog progressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	String mEventFlag;
	EditText mMobileVerificationCode,mPopUpVerifcationcode;
	ScrollView mSignUpStepOne;
	RelativeLayout mVerificationPopUp;
	// View pager for advertisement sliding image
	ImageView mAdvertisement_image;
	ProgressBar mAd_ProgressBar;
	TextView mPopUpHeader;
	Dialog mMobileDetailsDialog;
	AdvertisementTimerTask mTimerTask;
	Timer mTimer;

	public SendMobileVerficationAsyncTask(Context context,String eventflag,EditText mobileverificationcode,ScrollView signupstepone,RelativeLayout verificationpopup,TextView verificationheader,EditText  popupverification_code ,ImageView ad_imageview,ProgressBar ad_progressbar, Dialog MobileDetailsDialog,AdvertisementTimerTask timertask,Timer timer){
		this.mContext=context;
		this.mEventFlag=eventflag;
		this.mMobileVerificationCode=mobileverificationcode;
		this.mSignUpStepOne=signupstepone;
		this.mVerificationPopUp=verificationpopup;
		this.mAdvertisement_image = ad_imageview;
		this.mAd_ProgressBar = ad_progressbar;
		this.mPopUpHeader = verificationheader;
		this.mPopUpVerifcationcode = popupverification_code;
		this.mMobileDetailsDialog = MobileDetailsDialog;
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
				mGetResponse=zouponswebservice.getSignUpStep1_Sms(this.mEventFlag, params[0], params[1], params[2], params[3], params[4]);	//check
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=parsingclass.parseSignUpStepOne(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
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
		}else if(result.equals("No Stores Available.")){
			alertBox_service("Information", result);
		}else if(result.equals("Mobile Number already exists")){
			alertBox_service("Information", result);
		}
		else {
			Log.i(TAG,"Success: "+result);
			if(result.equals("Successfully sent Verification Code")){
				alertBox_service("Information", "A verification code has been sent to your mobile. Please enter that code to proceed.");

			}else if(result.equals("Failure")){
				alertBox_service("Information", "Unable to reach service.");
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
				if(msg.equals("A verification code has been sent to your mobile. Please enter that code to proceed.")){
					mSignUpStepOne.setVisibility(View.GONE);
					if(mMobileDetailsDialog != null){
						mMobileDetailsDialog.dismiss();
					}
					mVerificationPopUp.setVisibility(View.VISIBLE);
					// To give focus to verification code editText and open keyboard
					mMobileVerificationCode.requestFocus();
					////////////////////////
					mPopUpHeader.setText("Enter Mobile Verification Code");
					mPopUpVerifcationcode.setHint("Enter Mobile Verification Code");
					///////////////////////
					InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
					if(mTimer == null){
						Log.i("Timer for mobile verification ad", "null");
					}else{
						Log.i("Timer for mobile verification ad", "not null");
						mTimer.schedule(mTimerTask, 0, 4000);	
					}
					
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}