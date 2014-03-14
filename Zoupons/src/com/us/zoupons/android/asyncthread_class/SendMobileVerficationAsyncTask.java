package com.us.zoupons.android.asyncthread_class;

import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import com.us.zoupons.classvariables.SendMobileVerfication_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.login.Registration;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to call webservice to send mobile verification code
 *
 */

public class SendMobileVerficationAsyncTask extends AsyncTask<String, String, String>{

	private Context mContext;
	private ProgressDialog mProgressdialog=null;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse;
	private String mEventFlag;
	private EditText mMobileVerificationCode,mPopUpVerifcationcode;
	private ScrollView mSignUpStepOne;
	private RelativeLayout mVerificationPopUp;
	// View pager for advertisement sliding image
	private TextView mPopUpHeader;
	private Dialog mMobileDetailsDialog;
	private AdvertisementTimerTask mTimerTask;
	private Timer mTimer;
	
	public SendMobileVerficationAsyncTask(Context context,String eventflag,EditText mobileverificationcode,ScrollView signupstepone,RelativeLayout verificationpopup,TextView verificationheader,EditText  popupverification_code ,ImageView ad_imageview,ProgressBar ad_progressbar, Dialog MobileDetailsDialog,AdvertisementTimerTask timertask,Timer timer){
		this.mContext=context;
		this.mEventFlag=eventflag;
		this.mMobileVerificationCode=mobileverificationcode;
		this.mSignUpStepOne=signupstepone;
		this.mVerificationPopUp=verificationpopup;
		this.mPopUpHeader = verificationheader;
		this.mPopUpVerifcationcode = popupverification_code;
		this.mMobileDetailsDialog = MobileDetailsDialog;
		this.mTimerTask = timertask;
		this.mTimer = timer;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
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
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				mGetResponse=mZouponswebservice.getSignUpStep1_Sms(this.mEventFlag, params[0], params[1], params[2], params[3], params[4]);	//check
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=mParsingclass.parseSignUpStepOne(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						for(int i=0;i<WebServiceStaticArrays.mSendMobileVerificationCode.size();i++){
							final SendMobileVerfication_ClassVariables parsedobjectvalues = (SendMobileVerfication_ClassVariables) WebServiceStaticArrays.mSendMobileVerificationCode.get(i);
							if(!parsedobjectvalues.mMessage.equals("")){
								if(!parsedobjectvalues.mUserId.equals("")){
									Registration.sUserId=parsedobjectvalues.mUserId;
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
		}else if(result.equals("Mobile Number already exists")){
			alertBox_service("Information", result);
		}
		else {
			if(result.equals("Successfully sent Verification Code")){
				//alertBox_service("Information", "A verification code has been sent to your mobile. Please enter that code to proceed.");
				mSignUpStepOne.setVisibility(View.GONE);
				if(mMobileDetailsDialog != null){
					mMobileDetailsDialog.dismiss();
				}
				mVerificationPopUp.setVisibility(View.VISIBLE);
				// To give focus to verification code editText and open keyboard
				mMobileVerificationCode.requestFocus();
				mPopUpHeader.setText("We just Text you a Verification code");
				mPopUpVerifcationcode.setHint("Enter Code Here");
				// To show keypad forcly
				InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
				if(mTimer == null){
				}else{
					mTimer.schedule(mTimerTask, 0, 4000);	
				}

			}else if(result.equals("Failure")){
				alertBox_service("Information", "Unable to reach service.");
			}
		}
		mProgressdialog.dismiss();
		
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