package com.us.zoupons;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MyCurrentLocation.LocationResult;
import com.us.zoupons.ClassVariables.ActivateUser_ClassVariables;
import com.us.zoupons.ClassVariables.ChangePwd_ClassVariables;
import com.us.zoupons.ClassVariables.LoginUser_ClassVariables;
import com.us.zoupons.ClassVariables.MapViewCenter;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.FlagClasses.AccountLoginFlag;
import com.us.zoupons.FlagClasses.SignUpFlag;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.notification.NotificationService;
import com.us.zoupons.storeowner.HomePage.StoreOwner_HomePage;

public class ZouponsLogin extends Activity {

	public static String TAG="ZouponsLogin";
	public Button mLoginButton;
	public Typeface mZouponsFont;
	public ScrollView mDefaultLoginPage;
	public LinearLayout mChangePassword;
	public TextView mLoginHeader,mLoginUsername,mLoginPassword,mLoginForgotPassword,mLoginStaticText,mLoginLinkText,mLoginOr;
	public TextView mCustomerLoginText,mStoreLoginText;
	public EditText mUserNameValue,mPasswordValue;
	public static CheckBox mRememberMe;
	public LinearLayout mCustomerLogin,mStoreLogin,mBottomContainer,mZouponsLoginSocialNetwork,mZouponsLoginSignUp;
	public ImageView mFacebookLogin;
	public ImageView mZPAccountLogin;
	public NetworkCheck connectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private ProgressDialog mProgressDialog=null;

	private TextView mChangePasswordHeader,mChangePasswordNewPassword,mChangePasswordConfirmPassword;
	private EditText mChangePasswordNewPassword_Value,mChangePasswordConfirmPassword_Value;
	private Button mChangePasswordSave;

	public String activationCodeValue="";
	public static final String PREFENCES_FILE = "UserNamePrefences";
	public static final String TOKENID_PREFENCES_FILE = "TokenIdPrefences";
	private final int HONEYCOMB = 3;
	LocationManager manager;
	MyCurrentLocation mMyCurrentLocation;
	//public static boolean useGPSAlertFlag;
	EncryptionClass encryption;
	CheckLogoutSession checklogoutsession;

	String key,IsMobileVerified;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		connectionAvailabilityChecking= new NetworkCheck();
		parsingclass = new ZouponsParsingClass(getApplicationContext());
		encryption= new EncryptionClass(); 

		mProgressDialog=new ProgressDialog(this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage("Validating User...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);

		//code for hiding the keyboard while the activity is loading.
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		mDefaultLoginPage=(ScrollView) findViewById(R.id.defaultloginpage);
		mChangePassword=(LinearLayout) findViewById(R.id.changepassword);

		mLoginHeader=(TextView) findViewById(R.id.zoupons_login_longinheader);
		mLoginHeader.setTypeface(mZouponsFont);
		mLoginUsername=(TextView) findViewById(R.id.zoupons_login_usernameheader);
		mLoginUsername.setTypeface(mZouponsFont);
		mLoginPassword=(TextView) findViewById(R.id.zoupons_login_passwordheader);
		mLoginPassword.setTypeface(mZouponsFont);
		mLoginForgotPassword=(TextView) findViewById(R.id.zoupons_login_forgotpasswordheader);
		mLoginForgotPassword.setTypeface(mZouponsFont);
		mLoginStaticText=(TextView) findViewById(R.id.zoupons_login_statictext);
		mLoginStaticText.setTypeface(mZouponsFont);
		mLoginLinkText=(TextView) findViewById(R.id.zoupons_login_linktext);
		mLoginLinkText.setTypeface(mZouponsFont);
		mLoginOr=(TextView) findViewById(R.id.zoupons_login_or);
		mLoginOr.setTypeface(mZouponsFont);
		mLoginOr.setVisibility(View.INVISIBLE);

		mUserNameValue=(EditText) findViewById(R.id.zoupons_login_username);
		mUserNameValue.setTypeface(mZouponsFont);
		mPasswordValue=(EditText) findViewById(R.id.zoupons_login_password);
		mPasswordValue.setTypeface(mZouponsFont);

		mBottomContainer=(LinearLayout) findViewById(R.id.zoupons_login_bottomcontainer);

		mRememberMe = (CheckBox) findViewById(R.id.zoupons_login_rememberme);
		mRememberMe.setTypeface(mZouponsFont);
		SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
		if(!mPrefs.getString("username", "").equals("")) {
			mRememberMe.setClickable(true);
			mUserNameValue.setText(mPrefs.getString("username", "").trim());
			mRememberMe.setChecked(true);
		}else{
			mRememberMe.setClickable(false);
		}
		// To check the version for secure ssl webservice call
		String mOSVersion = android.os.Build.VERSION.RELEASE;
		
		if(mOSVersion != null && mOSVersion.contains(".")){
			int version = Integer.valueOf(mOSVersion.substring(0,mOSVersion.indexOf(".")));
			if(version >= HONEYCOMB){
				Editor editor = mPrefs.edit();
				editor.putString("ssl_compatable","yes");
				editor.commit();		
			}else{
				Editor editor = mPrefs.edit();
				editor.putString("ssl_compatable","no");
				editor.commit();
			}
		}else if(mOSVersion.length()>0){
			int version = Integer.valueOf(mOSVersion);
			if(version >= HONEYCOMB){
				Editor editor = mPrefs.edit();
				editor.putString("ssl_compatable","yes");
				editor.commit();		
			}else{
				Editor editor = mPrefs.edit();
				editor.putString("ssl_compatable","no");
				editor.commit();
			}
		}
		// To get device id 
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String IMEIstring = telephonyManager.getDeviceId(); 
		// To store device id in shared preference to use it in logout webservice
		if(IMEIstring != null){
			Editor editor = mPrefs.edit();
			editor.putString("device_id",IMEIstring);
			editor.commit();	
		}

		// check if notification service running
		if(isMyServiceRunning()){
			Intent mIntent = new Intent(ZouponsLogin.this,NotificationService.class);
			stopService(mIntent);
		}else{
			Log.i("service", "not runnning");
		}

		mLoginButton=(Button) findViewById(R.id.zoupons_login_button);
		mLoginButton.setTypeface(mZouponsFont);

		mZouponsLoginSocialNetwork = (LinearLayout) findViewById(R.id.zoupons_login_layout_socialnetwork);
		mZouponsLoginSignUp = (LinearLayout) findViewById(R.id.zoupons_login_signup);
		mFacebookLogin = (ImageView) findViewById(R.id.zoupons_login_facebooklogin);
		mZPAccountLogin = (ImageView) findViewById(R.id.zoupons_login_zplogin);

		/* Intialization of change password view*/
		mChangePasswordHeader=(TextView) findViewById(R.id.changepassword_header);
		mChangePasswordHeader.setTypeface(mZouponsFont);
		mChangePasswordNewPassword=(TextView) findViewById(R.id.changepassword_newpassword);
		mChangePasswordNewPassword.setTypeface(mZouponsFont);
		mChangePasswordConfirmPassword=(TextView) findViewById(R.id.changepassword_confirm_newpassword);
		mChangePasswordConfirmPassword.setTypeface(mZouponsFont);

		mChangePasswordNewPassword_Value=(EditText) findViewById(R.id.changepassword_newpassword_value);
		mChangePasswordNewPassword_Value.setTypeface(mZouponsFont);
		mChangePasswordConfirmPassword_Value=(EditText) findViewById(R.id.changepassword_confirm_newpassword_value);
		mChangePasswordConfirmPassword_Value.setTypeface(mZouponsFont);

		mChangePasswordSave=(Button) findViewById(R.id.changepassword_save);
		mChangePasswordSave.setTypeface(mZouponsFont);
		zouponswebservice = new ZouponsWebService(ZouponsLogin.this);

		//Code to cancel alarm
		checklogoutsession = new CheckLogoutSession(ZouponsLogin.this);
		checklogoutsession.setLogoutTimerAlarm();
		checklogoutsession.cancelAlarm();

		// To reset session variables in preferences
		
		SharedPreferences mLogoutSessionPreferences = getSharedPreferences("LogoutSessionPreferences", Context.MODE_PRIVATE);
		Editor mUpdatePreference = mLogoutSessionPreferences.edit();
		mUpdatePreference.putBoolean("WakeupApplication", false);
		mUpdatePreference.putLong("TimeBeforeGoneBackGround", 0);
		mUpdatePreference.commit();
		
		//Current Location declaration
		mMyCurrentLocation = new MyCurrentLocation();

		manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			AlertMessageNoGps();
		}else{
			boolean flag=mMyCurrentLocation.getLocation(ZouponsLogin.this, mLocationResult);
			if(!flag){
				Toast.makeText(getApplicationContext(), "No Provider Available", Toast.LENGTH_SHORT).show();
			}
		}

		
		mUserNameValue.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.length()>0){
					mRememberMe.setClickable(true);
				}else{
					mRememberMe.setClickable(false);
					mRememberMe.setChecked(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				mRememberMe.setClickable(false);
			}

			@Override
			public void afterTextChanged(Editable s) {}
		});

		mChangePasswordNewPassword_Value.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(mChangePasswordNewPassword_Value.getText().toString().trim().length()<6){
					alertBox_validation("Password must contain a minimum of 6 characters");
				}
			}
		});

		mChangePasswordSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mChangePasswordNewPassword_Value.getText().toString().trim().equals("")){
					alertBox_validation("Please enter the password.");
				}else if(mChangePasswordConfirmPassword_Value.getText().toString().trim().equals("")){
					alertBox_validation("The passwords you typed do not match");
				}else if(!mChangePasswordNewPassword_Value.getText().toString().trim().contentEquals(mChangePasswordConfirmPassword_Value.getText().toString().trim())){
					alertBox_validation("The passwords you typed do not match");
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

									mGetResponse=zouponswebservice.Change_Password(UserDetails.mServiceUserId, encryption.md5(mChangePasswordConfirmPassword_Value.getText().toString().trim()));

									if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
										String mParsingResponse=parsingclass.parseChangePwdXmlData(mGetResponse);
										if(mParsingResponse.equalsIgnoreCase("success")){
											for(int i=0;i<WebServiceStaticArrays.mChangePasswordList.size();i++){
												ChangePwd_ClassVariables parsedobjectvalues = (ChangePwd_ClassVariables) WebServiceStaticArrays.mChangePasswordList.get(i);

												if(parsedobjectvalues.mMessage.equals("Successfully Changed Password")){
													updateHandler(bundle, msg_response, "Your password has been successfully changed");	//send update to handler
												}else {
													updateHandler(bundle, msg_response, "Problem in changing Password.Please try again later");
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

		mRememberMe.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
					Editor editor = mPrefs.edit();
					editor.putString("username", mUserNameValue.getText().toString().trim());
					editor.commit();
				}else{
					SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
					Editor editor = mPrefs.edit();
					editor.putString("username", "");
					editor.commit();
				}
			}
		});

		mLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(mRememberMe.isChecked()){
					SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
					Editor editor = mPrefs.edit();
					editor.putString("username", mUserNameValue.getText().toString().trim());
					editor.commit();
				}else{
					SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
					Editor editor = mPrefs.edit();
					editor.putString("username", "");
					editor.commit();
				}

				//To close keyboard if it is in open state
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mPasswordValue.getWindowToken(), 0);

				if(connectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
					if(mUserNameValue.getText().toString().trim().equals("")){
						alertBox_validation("Please enter username.");
						mUserNameValue.requestFocus();
					}else if(mPasswordValue.getText().toString().trim().equals("")){
						alertBox_validation("Please enter password.");
						mPasswordValue.requestFocus();
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

										mGetResponse=zouponswebservice.login(mUserNameValue.getText().toString().trim(), encryption.md5(mPasswordValue.getText().toString().trim()));	//Get login response values from webservice

										if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
											String mParsingResponse=parsingclass.parseLoginXmlData(mGetResponse,TAG);
											if(mParsingResponse.equalsIgnoreCase("success")){
												for(int i=0;i<WebServiceStaticArrays.mLoginUserList.size();i++){
													LoginUser_ClassVariables parsedobjectvalues = (LoginUser_ClassVariables) WebServiceStaticArrays.mLoginUserList.get(i);

													
													SharedPreferences mPrefs = getSharedPreferences(TOKENID_PREFENCES_FILE, MODE_PRIVATE);
													Editor editor = mPrefs.edit();
													editor.putString("tokenid", parsedobjectvalues.authToken);
													editor.commit();

													UserDetails.mServiceUserId=parsedobjectvalues.userID;
													UserDetails.mServiceFirstName=parsedobjectvalues.firstName;
													UserDetails.mServiceLastName=parsedobjectvalues.lastName;
													UserDetails.mServiceEmail=mUserNameValue.getText().toString().trim();
													UserDetails.mServiceFbId=parsedobjectvalues.fbID;
													UserDetails.mServicePin=parsedobjectvalues.pin;
													UserDetails.mServiceForgotPwdFlag=parsedobjectvalues.forgotPwdFlag;
													UserDetails.mServiceMessage=parsedobjectvalues.message;
													UserDetails.mUserType=parsedobjectvalues.usertype;

													
													if(!parsedobjectvalues.message.equalsIgnoreCase("Your account still not activated")){
														if(parsedobjectvalues.mFlag.equals("no")){
															updateHandler(bundle, msg_response, "Invalid User");	//send update to handler
														}else if(parsedobjectvalues.mFlag.equals("yes")){
															updateHandler(bundle, msg_response, "Valid User");
														}
													}else{
														updateHandler(bundle, msg_response, "InActive User",parsedobjectvalues.isMobileVerified);
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
											updateHandler(bundle, msg_response, mGetResponse);	//send update to handler about login response
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
				}else{
					Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
					mUserNameValue.requestFocus();
				}
			}
		});

		mLoginForgotPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(ZouponsLogin.this,ForgotPassword.class));
			}
		});

		mLoginLinkText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SignUpFlag.SIGNUPFLAG=1;
				AccountLoginFlag.accountsignupflag="ZP";
				Intent intent_registration = new Intent(ZouponsLogin.this,Registration.class);
				startActivity(intent_registration);
			}
		});

		mZPAccountLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SignUpFlag.SIGNUPFLAG=1;
				AccountLoginFlag.accountsignupflag="ZP";
				Intent intent_registration = new Intent(ZouponsLogin.this,Registration.class);
				startActivity(intent_registration);
			}
		});

		mFacebookLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SignUpFlag.SIGNUPFLAG=2;
				AccountLoginFlag.accountsignupflag="FB";
				Intent intent_registration = new Intent(ZouponsLogin.this,Registration.class);
				startActivity(intent_registration);
			}
		});
	}

	public LocationResult mLocationResult = new LocationResult() {

		@Override
		public void gotLocation(android.location.Location location) {
			if(location != null){
				MapViewCenter.LoginCurrentLocationLatitude=location.getLatitude();
				MapViewCenter.LoginCurrentLocationLongitude=location.getLongitude();
			}
		}
	};

	private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse){
		bundle.putString("returnResponse", mGetResponse);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}

	private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse,String isMobileVerified){
		bundle.putString("returnResponse", mGetResponse);
		bundle.putString("ismobileverified", isMobileVerified);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}

	Handler handler_response = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Bundle b = msg.getData();

			key = b.getString("returnResponse");
			IsMobileVerified = b.getString("ismobileverified")!=null?b.getString("ismobileverified"):"";
			if(key.trim().equals("success")){
				Log.i(TAG,"Response Get Successfully");	
			}else if(key.trim().equals("failure")){
				alertBox_service("Information","Unable to reach Service.Please try again later.");
				mPasswordValue.setText("");
				mPasswordValue.requestFocus();
			}else if(key.trim().equals("noresponse")||key.trim().equals("norecords")){
				alertBox_service("Information","No Data Available");
			}else if(key.trim().equals("Invalid User")){
				alertBox_service("Information","Invalid User");
				mPasswordValue.setText("");
				mUserNameValue.requestFocus();
			}else if(key.trim().equals("Valid User")){
				if(Integer.parseInt(UserDetails.mServiceForgotPwdFlag)!=0){
					mUserNameValue.setText("");
					mPasswordValue.setText("");
					mDefaultLoginPage.setVisibility(View.GONE);
					mChangePassword.setVisibility(View.VISIBLE);
				}else if(UserDetails.mUserType.equalsIgnoreCase("store_owner") || UserDetails.mUserType.equalsIgnoreCase("store_employee")){
					Intent login_intent = new Intent(ZouponsLogin.this,StoreOwner_HomePage.class);
					startActivity(login_intent);
					finish();
				}else{
					AccountLoginFlag.accountUserTypeflag="Customer";
					Intent login_intent = new Intent(ZouponsLogin.this,SlidingView.class);
					login_intent.putExtra("fromlogin", true);
					startActivity(login_intent);
					finish();
				}
			}else if(key.trim().equals("Your password has been successfully changed")){
				alertBox_service("Information", "Your password has been successfully changed");
			}else if(key.trim().equals("InActive User")){
				//To move to step-2 signup
				alertBox_service("Information", "Your account is not active please update the security information",IsMobileVerified);
			}else if(key.trim().equals("Wrong Activation Code")){
				alertBox_service("Information", "Your have entered a Wrong Activation Code. Please try again.");
			}else if(key.trim().equals("Correct Activation Code")){
				Intent intent_addcard = new Intent(ZouponsLogin.this,AddCardInformation.class);
				intent_addcard.putExtra("class_name", TAG);
				startActivity(intent_addcard);
			}else {
				alertBox_service("Information","No Data Available");
			}
		}
	};

	private void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equals("Your password has been successfully changed")){
					mChangePassword.setVisibility(View.GONE);
					mDefaultLoginPage.setVisibility(View.VISIBLE);
				}else if(msg.equalsIgnoreCase("your account is not active please update the security information")){
					Intent intent_addcard = new Intent(ZouponsLogin.this,Registration.class);
					intent_addcard.putExtra("class_name", TAG);
					startActivity(intent_addcard);
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}

	private void alertBox_service(final String title,final String msg,final String ismobileverified){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equalsIgnoreCase("your account is not active please update the security information")){
					Intent intent_addcard = new Intent(ZouponsLogin.this,Registration.class);
					intent_addcard.putExtra("class_name", TAG);
					startActivity(intent_addcard);
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}

	private void alertBox_validation(final String msg) {
		AlertDialog.Builder validation_alert = new AlertDialog.Builder(this);
		validation_alert.setTitle("Information");
		validation_alert.setMessage(msg);

		validation_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equals("Please enter the password.")){
					mChangePasswordNewPassword_Value.requestFocus();
				}else if(msg.equals("The passwords you typed do not match")){
					mChangePasswordConfirmPassword_Value.setText("");
					mChangePasswordConfirmPassword_Value.requestFocus();
				}
				dialog.dismiss();
			}
		});
		validation_alert.show();
	}

	protected void getActivationCode() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View lyt_activationcode = factory.inflate(R.layout.accountactivation,null);

		final AlertDialog.Builder getactivationcode = new AlertDialog.Builder(this);
		getactivationcode.setTitle("Login status");
		getactivationcode.setMessage("Your account still not activated.Please Check your mail and enter the verification code.");

		getactivationcode.setView(lyt_activationcode);
		AlertDialog activationcodePrompt = getactivationcode.create();
		final EditText alt_activationcode = (EditText) lyt_activationcode.findViewById(R.id.getActivationCode);

		getactivationcode.setPositiveButton("Ok",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				activationCodeValue=alt_activationcode.getText().toString().trim();
				if(!activationCodeValue.equals("")){
					userActivation();
				}else{
					Toast.makeText(ZouponsLogin.this, "Please Enter Activation Code.", Toast.LENGTH_LONG).show();
					getActivationCode();
					alt_activationcode.requestFocus();
				}
			}
		});

		getactivationcode.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		getactivationcode.show();
	}

	public void userActivation(){
		mProgressDialog.show();
		final Thread syncThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Bundle bundle = new Bundle();
				String mGetResponse=null;
				Message msg_response = new Message();

				try{
					if(connectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){

						mGetResponse=zouponswebservice.activateUser(UserDetails.mServiceUserId, activationCodeValue);

						if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
							String mParsingResponse=parsingclass.parseActivateuser(mGetResponse);
							if(mParsingResponse.equalsIgnoreCase("success")){
								for(int i=0;i<WebServiceStaticArrays.mActivateUser.size();i++){
									ActivateUser_ClassVariables parsedobjectvalues = (ActivateUser_ClassVariables) WebServiceStaticArrays.mActivateUser.get(i);

									
									if(!parsedobjectvalues.message.equals("Wrong Activation Code")){
										updateHandler(bundle, msg_response, "Correct Activation Code");	//send update to handler
									}else {
										updateHandler(bundle, msg_response, "Wrong Activation Code");
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void AlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
		.setCancelable(true)
		.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog,final int id) {
				//useGPSAlertFlag=true;
				startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				dialog.cancel();
			}
		})
		.setNegativeButton("Enable wireless networks", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				if(!manager.isProviderEnabled( LocationManager.NETWORK_PROVIDER )){
					startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				}else{
					Toast.makeText(getApplicationContext(), "Wireless networks are already enabled.", Toast.LENGTH_SHORT).show();
				}
				//useGPSAlertFlag=true;
				dialog.cancel();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		finish();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onRestart() {
		super.onRestart();
	}

	@Override
	public void onResume() {
		super.onResume();
		boolean flag=mMyCurrentLocation.getLocation(ZouponsLogin.this, mLocationResult);
		if(!flag){
			Toast.makeText(getApplicationContext(), "No Provider Available", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		mMyCurrentLocation.removeUpdates(); 	//Function to remove GPS Updates
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
		return;
	}

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (NotificationService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}