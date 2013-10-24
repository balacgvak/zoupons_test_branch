/**
 * 
 */
package com.us.zoupons;

import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.ClassVariables.GetForgotPwd_ClassVariables;
import com.us.zoupons.ClassVariables.GetSecurityQuestions_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

/**
 * @author Zoupons
 *
 */
public class ForgotPassword extends Activity {

	private String TAG="ForgotPassword";
	private Typeface mZouponsFont;
	private ScrollView mScrollView;
	private LinearLayout mLinearLayout;
	private TextView mSecurityQuestion1,mSecurityQuestion2;
	private EditText mAnswer1,mAnswer2;
	
	private TextView mEmailVerificationCode;
	private EditText mEmailVerificationCodeValue;
	
	private Button mUserNameSkip,mUserNameOk,mSkip,mSave;
	
	public NetworkCheck connectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private ProgressDialog mProgressDialog=null;
	
	public String[] mSecurityQuestions={"",""},mSecurityQuestionsID={"",""},mSecurityQuestionNumber={"",""};
	public String mUserID,mQuestion1ID,mQuestion2ID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);
		
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		connectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice = new ZouponsWebService(ForgotPassword.this);
		parsingclass = new ZouponsParsingClass(getApplicationContext());
		
		mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(100);
        
		mScrollView=(ScrollView) findViewById(R.id.scroll_forgotpassword);
		mLinearLayout=(LinearLayout) findViewById(R.id.linear_forgotpassword);
		
		mEmailVerificationCode=(TextView) findViewById(R.id.signup_verification_EmailVerificationPasscode);
		mEmailVerificationCode.setTypeface(mZouponsFont);
		mEmailVerificationCodeValue=(EditText) findViewById(R.id.signup_verification_EmailVerificationPasscode_value);
		mEmailVerificationCodeValue.setTypeface(mZouponsFont);
		
		mUserNameSkip=(Button) findViewById(R.id.forgotpassword_username_skip);
		mUserNameSkip.setTypeface(mZouponsFont);
		mUserNameOk=(Button) findViewById(R.id.forgotpassword_username_ok);
		mUserNameOk.setTypeface(mZouponsFont);
		
		mSecurityQuestion1=(TextView) findViewById(R.id.registration_securityquestion1);
		mSecurityQuestion2=(TextView) findViewById(R.id.registration_securityquestion2);
		
		mAnswer1=(EditText) findViewById(R.id.registration_answer1_value);
		mAnswer2=(EditText) findViewById(R.id.registration_answer2_value);
		
		mSkip=(Button) findViewById(R.id.forgotpassword_skip);
		mSkip.setTypeface(mZouponsFont);
		mSave=(Button) findViewById(R.id.forgotpassword_save);
		mSave.setTypeface(mZouponsFont);
		
		mUserNameSkip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent_skip = new Intent(ForgotPassword.this,ZouponsLogin.class);
				intent_skip.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_skip);
			}
		});
		
		mUserNameOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mEmailVerificationCodeValue.getText().toString().trim().equals("")){
					alertBox_validation("Please enter username.");
					mEmailVerificationCodeValue.requestFocus();
				}else {
					
					if(!mEmailVerificationCodeValue.getText().toString().trim().equalsIgnoreCase("")){
						if(isValidEmail(mEmailVerificationCodeValue.getText().toString().trim())==true){
							mProgressDialog.show();
							final Thread syncThread = new Thread(new Runnable() {

								@Override
								public void run() {
									Bundle bundle = new Bundle();
									String mGetResponse=null;
									Message msg_response = new Message();
									
									try{
										if(connectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){

											mGetResponse=zouponswebservice.getSecurityQuestions(mEmailVerificationCodeValue.getText().toString().trim(), 1);	//Get login response values from webservice

											if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
												String mParsingResponse=parsingclass.parseGetSecurityQuestionsXmlDataNew(mGetResponse);
												if(mParsingResponse.equalsIgnoreCase("success")){
													for(int i=0;i<WebServiceStaticArrays.mGetSecurityQuestionsEmailValidationList.size();i++){
														GetSecurityQuestions_ClassVariables parsedobjectvalues = (GetSecurityQuestions_ClassVariables) WebServiceStaticArrays.mGetSecurityQuestionsEmailValidationList.get(i);
														mUserID=parsedobjectvalues.mGetSecurityQuestions_UserId;
														if(!parsedobjectvalues.mGetSecurityQuestions_EmailValidation.equals("")&&parsedobjectvalues.mGetSecurityQuestions_EmailValidation.equals("Invalid User")){
															updateHandler(bundle, msg_response, "Invalid User");	//send update to handler
														}else if(!parsedobjectvalues.mGetSecurityQuestions_EmailValidation.equals("")&&parsedobjectvalues.mGetSecurityQuestions_EmailValidation.equals("No Security Questions Available")){
															updateHandler(bundle, msg_response, "No Security Questions Available");	//send update to handler
														}else if(!parsedobjectvalues.mGetSecurityQuestions_EmailValidation.equals("")&&parsedobjectvalues.mGetSecurityQuestions_EmailValidation.equals("Your account still not activated")){
															updateHandler(bundle, msg_response, "Your account still not activated");	//send update to handler
														}else{
															updateHandler(bundle, msg_response, "getsecurityquestions");
														}
													}
													for(int j=0;j<WebServiceStaticArrays.mGetSecurityQuestionsAndIds.size();j++){
														GetSecurityQuestions_ClassVariables parsedobjectvaluesNew = (GetSecurityQuestions_ClassVariables) WebServiceStaticArrays.mGetSecurityQuestionsAndIds.get(j);
													}
												}else if(mParsingResponse.equalsIgnoreCase("failure")){
													updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
													Log.i(TAG,"Error");
												}else if(mParsingResponse.equalsIgnoreCase("norecords")){
													updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
													Log.i(TAG,"No Records");
												}
											}else{
												updateHandler(bundle, msg_response, mGetResponse);	//send update to handler about emailid validation response
											}
										}else{
											Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
										}
									}catch(Exception e){
										Log.i(TAG,"Thread Error");
										e.printStackTrace();
									}
									mProgressDialog.setProgress(100);
									mProgressDialog.dismiss();
								}
							});syncThread.start();
						}else{
							alertBox_validation("Enter email in this format - 'username@domainname.com");
							mEmailVerificationCodeValue.setText("");
							mEmailVerificationCodeValue.requestFocus();
						}
					}
				}
			}
		});
		
		mSkip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent_skip = new Intent(ForgotPassword.this,ZouponsLogin.class);
				intent_skip.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_skip);
			}
		});
		
		mSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mAnswer1.getText().toString().trim().equals("")){
					alertBox_validation("Enter Answer");
					mAnswer1.requestFocus();
				}else{
					mProgressDialog.show();
					final Thread syncThread = new Thread(new Runnable() {

						@Override
						public void run() {
							Bundle bundle = new Bundle();
							String mGetResponse=null;
							Message msg_response = new Message();
							
							try{
								if(connectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
									
									
									//Changes --> pass same security question 1 and answer as security question 2 to webservice 
									mGetResponse=zouponswebservice.getforgot_pwd(mEmailVerificationCodeValue.getText().toString().trim(), mQuestion1ID, mQuestion1ID, mAnswer1.getText().toString().trim(), mAnswer1.getText().toString().trim());	//Get forgot password response values from webservice
									
									if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
										String mParsingResponse=parsingclass.parseGetForgotPwdXmlData(mGetResponse);
										if(mParsingResponse.equalsIgnoreCase("success")){
											for(int i=0;i<WebServiceStaticArrays.mForgotPasswordList.size();i++){
												GetForgotPwd_ClassVariables parsedobjectvalues = (GetForgotPwd_ClassVariables) WebServiceStaticArrays.mForgotPasswordList.get(i);
												
												if(parsedobjectvalues.mFlag.equals("no")){
													updateHandler(bundle, msg_response, "wronganswer");	//send update to handler
												}else if(parsedobjectvalues.mFlag.equals("yes")){
													updateHandler(bundle, msg_response, "sentemail");
												}
											}
										}else if(mParsingResponse.equalsIgnoreCase("failure")){
											updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
											Log.i(TAG,"Error");
										}else if(mParsingResponse.equalsIgnoreCase("norecords")){
											updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
											Log.i(TAG,"No Records");
										}
									}else{
										updateHandler(bundle, msg_response, mGetResponse);	//send update to handler about emailid validation response
									}
								}else{
									Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
								}
							}catch(Exception e){
								Log.i(TAG,"Thread Error");
								e.printStackTrace();
							}
							mProgressDialog.setProgress(100);
							mProgressDialog.dismiss();
						}
					});syncThread.start();
				}
			}
		});
	}

	protected final static boolean isValidEmail(String mailid){
		try{
			Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
					"\\@" +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
					"(" +
					"\\." +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
			")+");
			//return android.util.Patterns.EMAIL_ADDRESS.matcher(mailid).matches();
			return EMAIL_ADDRESS_PATTERN.matcher(mailid).matches();
		}catch(Exception e){
			return false;
		}
	}
	
	private void alertBox_validation(String msg) {
		AlertDialog.Builder validation_alert = new AlertDialog.Builder(this);
		validation_alert.setTitle("Information");
		validation_alert.setMessage(msg);
		validation_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		validation_alert.show();
	}
	
	private void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equalsIgnoreCase("A temporary password has been emailed to your account")){
					Intent intent_forgotpwd = new Intent(ForgotPassword.this,ZouponsLogin.class);
					intent_forgotpwd.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent_forgotpwd);
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
	
	private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse){
		//send update to handler about emailid validation response
		bundle.putString("returnResponse", mGetResponse);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}
	
	Handler handler_response = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			String key = b.getString("returnResponse");
			if(key.trim().equals("success")){
				Log.i(TAG,"Response Get Successfully");	
			}else if(key.trim().equals("failure")){
				alertBox_service("Information","Response Error");
			}else if(key.trim().equals("noresponse")||key.trim().equals("norecords")){
				alertBox_service("Information","No Data Available");
			}else if(key.trim().equals("Invalid User")){
				alertBox_service("Information","Invalid User Name");
				mEmailVerificationCodeValue.setText("");
				mEmailVerificationCodeValue.requestFocus();
			}else if(key.trim().equalsIgnoreCase("Your account still not activated")||key.trim().equalsIgnoreCase("No Security Questions Available")){
				alertBox_service("Information", key.toString().trim());
				mEmailVerificationCodeValue.setText("");
				mEmailVerificationCodeValue.requestFocus();
			}
			else if(key.trim().equals("getsecurityquestions")){
				//String mQuestion[]={"",""};
				mLinearLayout.setVisibility(View.GONE);
				mScrollView.setVisibility(View.VISIBLE);
				for(int j=0;j<WebServiceStaticArrays.mGetSecurityQuestionsAndIds.size();j++){
					GetSecurityQuestions_ClassVariables parsedobjectvaluesNew = (GetSecurityQuestions_ClassVariables) WebServiceStaticArrays.mGetSecurityQuestionsAndIds.get(j);
					mSecurityQuestionsID[j]=parsedobjectvaluesNew.mGetSecurityQuestions_QuestionId;
					mSecurityQuestions[j]=parsedobjectvaluesNew.mGetSecurityQuestions_Question;
					mSecurityQuestionNumber[j]=parsedobjectvaluesNew.mGetSecurityQuestions_QuestionNumber;
					if(mSecurityQuestionNumber[j].equals("1")){
						mQuestion1ID=mSecurityQuestionsID[j];
						mSecurityQuestion1.setText(mSecurityQuestions[j]);
					}else if(mSecurityQuestionNumber[j].equals("2")){
						mQuestion2ID=mSecurityQuestionsID[j];
						mSecurityQuestion2.setText(mSecurityQuestions[j]);
					}
				}
			}else if(key.trim().equals("wronganswer")){
				alertBox_service("Information","The security answer you entered does not match our records");
				mAnswer1.setText("");
				mAnswer2.setText("");
				mAnswer1.requestFocus();
			}else if(key.trim().equals("sentemail")){
				alertBox_service("Information","A temporary password has been emailed to your account");
			}else {
				alertBox_service("Information","No Data Available");
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

}
