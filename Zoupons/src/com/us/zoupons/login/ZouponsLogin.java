package com.us.zoupons.login;

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
import android.util.Patterns;
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

import com.us.zoupons.EncryptionClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.backgroundservices.ContactsLoaderService;
import com.us.zoupons.backgroundservices.NotificationService;
import com.us.zoupons.classvariables.ActivateUser_ClassVariables;
import com.us.zoupons.classvariables.ChangePwd_ClassVariables;
import com.us.zoupons.classvariables.LoginUser_ClassVariables;
import com.us.zoupons.classvariables.MapViewCenter;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.AccountLoginFlag;
import com.us.zoupons.flagclasses.SignUpFlag;
import com.us.zoupons.login.MyCurrentLocation.LocationResult;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.wallet.AddCreditCard;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.homepage.StoreOwner_HomePage;

/*
 * Activity which displays login page and handle its functionalities
 * 
 */

public class ZouponsLogin extends Activity{

	// Initializing views,viewgroups and variables
	private String mTAG="ZouponsLogin";
	private Button mLoginButton,mGuestLoginButton;
	private Typeface mZouponsFont;
	private ScrollView mDefaultLoginPage;
	private LinearLayout mChangePassword;
	private TextView mLoginHeader,mLoginUsername,mLoginPassword,mLoginForgotPassword,mLoginStaticText,mLoginLinkText,mLoginOr;
	private EditText mUserNameValue,mPasswordValue;
	private CheckBox mRememberMe;
	private ImageView mFacebookLogin;
	private ImageView mZPAccountLogin;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private ProgressDialog mProgressDialog=null;
	private TextView mChangePasswordHeader,mChangePasswordNewPassword,mChangePasswordConfirmPassword;
	private EditText mChangePasswordNewPassword_Value,mChangePasswordConfirmPassword_Value;
	private Button mChangePasswordSave;
	private String mActivationCodeValue="";
	public static final String PREFENCES_FILE = "UserNamePrefences";
	public static final String TOKENID_PREFENCES_FILE = "TokenIdPrefences";
	private LocationManager mLocationManager;
	//private MyCurrentLocation mMyCurrentLocation;
	private EncryptionClass mEncryption;
	private CheckLogoutSession mChecklogoutsession;
	private String mKey,mIsMobileVerified;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		// Zoupons font for all text fields
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking= new NetworkCheck();
		mParsingclass = new ZouponsParsingClass(getApplicationContext());
		mEncryption= new EncryptionClass(); 
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
		((Button) findViewById(R.id.login_choice_customer_buttonId)).setTypeface(mZouponsFont,Typeface.BOLD);
		mGuestLoginButton = (Button) findViewById(R.id.login_choice_guest_buttonId);
		mGuestLoginButton.setTypeface(mZouponsFont,Typeface.BOLD);
		mUserNameValue=(EditText) findViewById(R.id.zoupons_login_username);
		mUserNameValue.setTypeface(mZouponsFont);
		mPasswordValue=(EditText) findViewById(R.id.zoupons_login_password);
		mPasswordValue.setTypeface(mZouponsFont);
		mRememberMe = (CheckBox) findViewById(R.id.zoupons_login_rememberme);
		mRememberMe.setTypeface(mZouponsFont);

		//Thread.setDefaultUncaughtExceptionHandler(new CustomException(ZouponsLogin.this));

		SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
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

		}

		mLoginButton=(Button) findViewById(R.id.zoupons_login_button);
		mLoginButton.setTypeface(mZouponsFont);
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
		mZouponswebservice = new ZouponsWebService(ZouponsLogin.this);
		//Code to cancel alarm
		mChecklogoutsession = new CheckLogoutSession(ZouponsLogin.this);
		mChecklogoutsession.setLogoutTimerAlarm();
		mChecklogoutsession.cancelAlarm();
		// To reset session variables in preferences
		SharedPreferences mLogoutSessionPreferences = getSharedPreferences("LogoutSessionPreferences", Context.MODE_PRIVATE);
		Editor mUpdatePreference = mLogoutSessionPreferences.edit();
		mUpdatePreference.putBoolean("WakeupApplication", false);
		mUpdatePreference.putLong("TimeBeforeGoneBackGround", 0);
		mUpdatePreference.commit();

		if(!mPrefs.getString("username", "").equals("")) { // If user name saved in Preference file
			mRememberMe.setClickable(true);
			mUserNameValue.setText(mPrefs.getString("username", "").trim());
			mPasswordValue.requestFocus();
			
			if((getIntent().hasExtra("FromTimerSession") || !getIntent().hasExtra("FromManualLogOut")) && mPrefs.getBoolean("should_maintain_session", false)){ // To maintain session, if login session time expires then move to respective activity based upon user type 
				launchHomePage(); // To launch home page..
			}else{ // Remember me not checked
				Editor editor = mPrefs.edit();
				editor.putBoolean("should_maintain_session", false);
				editor.commit();
			}
		}else if((!getIntent().hasExtra("FromTimerSession") || !getIntent().hasExtra("FromManualLogOut"))){ // App kill by Android OS
		}else{
			mRememberMe.setClickable(false);
		}

		mLocationManager = (LocationManager)getApplicationContext().getSystemService( Context.LOCATION_SERVICE );
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
				if(mChangePasswordNewPassword_Value.getText().toString().length()<6){
					alertBox_validation("Password must contain a minimum of 6 characters");
				}
			}
		});

		mChangePasswordSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mChangePasswordNewPassword_Value.getText().toString().equals("")){
					alertBox_validation("Please enter the password.");
				}else if(mChangePasswordConfirmPassword_Value.getText().toString().equals("")){
					alertBox_validation("The passwords you typed do not match");
				}else if(!mChangePasswordNewPassword_Value.getText().toString().contentEquals(mChangePasswordConfirmPassword_Value.getText().toString())){
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
								if(mConnectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
									mGetResponse=mZouponswebservice.Change_Password(UserDetails.mServiceUserId, mEncryption.md5(mChangePasswordConfirmPassword_Value.getText().toString()));
									if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
										String mParsingResponse=mParsingclass.parseChangePwdXmlData(mGetResponse);
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
										}else if(mParsingResponse.equalsIgnoreCase("norecords")){
											updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
										}
									}else{
										updateHandler(bundle, msg_response, mGetResponse);	//send update to handler about emailid validation response
									}
								}else{
									Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
								}
							}catch(Exception e){
								e.printStackTrace();
							}
							mProgressDialog.setProgress(100);
							mProgressDialog.dismiss();
						}
					});syncThread.start();
				}
			}
		});

		mLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//To close keyboard if it is in open state
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mPasswordValue.getWindowToken(), 0);

				if(mConnectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
					if(mUserNameValue.getText().toString().trim().equals("")){
						alertBox_validation("Please enter Email address.");
						mUserNameValue.requestFocus();
					}else if(!Patterns.EMAIL_ADDRESS.matcher(mUserNameValue.getText().toString().trim()).matches()){
						alertBox_validation("Please enter valid Email address.");
						mUserNameValue.requestFocus();
					}else if(mPasswordValue.getText().toString().equals("")){
						alertBox_validation("Please enter password.");
						mPasswordValue.requestFocus();
					}else if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
						AlertMessageNoGps();
					}else{
						if(mRememberMe.isChecked()){ // If checked, save user name in preference file 
							SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
							Editor editor = mPrefs.edit();
							editor.putString("username", mUserNameValue.getText().toString().trim());
							editor.putString("user_login_type", "customer");
							editor.commit();
						}else{// If not checked, clear user name in preference file 
							SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
							Editor editor = mPrefs.edit();
							//editor.putString("username", "");
							editor.putString("user_login_type", "customer");
							editor.commit();
						}
						mProgressDialog.show();
						final Thread syncThread = new Thread(new Runnable() {

							@Override
							public void run() {
								Bundle bundle = new Bundle();
								String mGetResponse=null;
								Message msg_response = new Message();
								try{
									if(mConnectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
										mGetResponse=mZouponswebservice.login(mUserNameValue.getText().toString().trim(), mEncryption.md5(mPasswordValue.getText().toString()));	//Get login response values from webservice
										if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
											String mParsingResponse=mParsingclass.parseLoginXmlData(mGetResponse,mTAG);
											if(mParsingResponse.equalsIgnoreCase("success")){
												for(int i=0;i<WebServiceStaticArrays.mLoginUserList.size();i++){
													LoginUser_ClassVariables parsedobjectvalues = (LoginUser_ClassVariables) WebServiceStaticArrays.mLoginUserList.get(i);
													// Save token id in preference for future use in each webservice call.. 
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
													if(parsedobjectvalues.message.equalsIgnoreCase("Mobile is not verified")){
														updateHandler(bundle, msg_response, "mobile not verified");	//send update to handler
													}else if(!parsedobjectvalues.message.equalsIgnoreCase("Your account still not activated")){
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

											}else if(mParsingResponse.equalsIgnoreCase("norecords")){
												updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler

											}
										}else{
											updateHandler(bundle, msg_response, mGetResponse);	//send update to handler about login response
										}
									}else{
										Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
									}
								}catch(Exception e){

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

		mGuestLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Flushing static user variables 
				//To close keyboard if it is in open state
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				
				UserDetails.mServiceUserId="0";
				UserDetails.mServiceFirstName="";
				UserDetails.mServiceLastName="";
				UserDetails.mServiceEmail="";
				UserDetails.mServiceFbId="";
				UserDetails.mServicePin="";
				UserDetails.mServiceForgotPwdFlag="";
				UserDetails.mServiceMessage="";
				UserDetails.mUserType="";
				AccountLoginFlag.accountUserTypeflag="Customer";
				// Clear username in preference
				if ( !mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
					AlertMessageNoGps();
				}else{
					SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
					Editor editor = mPrefs.edit();
					editor.putString("user_login_type", "Guest");
					editor.commit();
					resetNotificationPreferences();
					Intent login_intent = new Intent(ZouponsLogin.this,ShopperHomePage.class);  // To launch Home page
					login_intent.putExtra("fromlogin", true);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(login_intent);
					finish();			    	
				}
			}
		});

	}

	private void launchHomePage() {
		// TODO Auto-generated method stub
		startFetchingContactsService();
		// To check user type when user session is not expired
		GetRecentUserTypeTask mGetUserType = new GetRecentUserTypeTask(ZouponsLogin.this);
		mGetUserType.execute();
			
	}

	// Initilazing LocationResult to get curent location coordinates
	public LocationResult mLocationResult = new LocationResult() {

		@Override
		public void gotLocation(android.location.Location location) {
			if(location != null){
				MapViewCenter.LoginCurrentLocationLatitude=location.getLatitude();
				MapViewCenter.LoginCurrentLocationLongitude=location.getLongitude();
			}
		}
	};

	// To send message to handler to update UI elements
	private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse){
		bundle.putString("returnResponse", mGetResponse);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}

	// To send message to handler to update UI elements
	private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse,String isMobileVerified){
		bundle.putString("returnResponse", mGetResponse);
		bundle.putString("ismobileverified", isMobileVerified);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}

	// Handler class to update UI items
	Handler handler_response = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			mKey = b.getString("returnResponse");
			mIsMobileVerified = b.getString("ismobileverified")!=null?b.getString("ismobileverified"):"";
			if(mKey.trim().equals("success")){

			}else if(mKey.trim().equals("failure")){
				alertBox_service("Information","Unable to reach Service.Please try again later.");
				mPasswordValue.setText("");
				mPasswordValue.requestFocus();
			}else if(mKey.trim().equals("noresponse")||mKey.trim().equals("norecords")){
				alertBox_service("Information","No Data Available");
			}else if(mKey.trim().equals("Invalid User")){
				alertBox_service("Information","Invalid User");
				mPasswordValue.setText("");
				mUserNameValue.requestFocus();
			}else if(mKey.trim().equals("Valid User")){
				// Saving user credentials in preference file..
				SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
				Editor editor = mPrefs.edit();
				LoginUser_ClassVariables parsedobjectvalues = (LoginUser_ClassVariables) WebServiceStaticArrays.mLoginUserList.get(0);
				if(mRememberMe.isChecked()){
					editor.putBoolean("should_maintain_session", true);
				}else{
					editor.putBoolean("should_maintain_session", false);
				}
				editor.putString("user_id", parsedobjectvalues.userID);
				editor.putString("store_id", parsedobjectvalues.mStoreId);
				editor.putString("user_type", parsedobjectvalues.usertype);
				editor.putString("fb_id", parsedobjectvalues.fbID);
				editor.commit();
				// To sync Device Contact with Zoupons server
				startFetchingContactsService();
				
				if(Integer.parseInt(UserDetails.mServiceForgotPwdFlag)!=0){
					mUserNameValue.setText("");
					mPasswordValue.setText("");
					mDefaultLoginPage.setVisibility(View.GONE);
					mChangePassword.setVisibility(View.VISIBLE);
				}else if(UserDetails.mUserType.equalsIgnoreCase("store_owner") || UserDetails.mUserType.equalsIgnoreCase("store_employee")){
					Intent login_intent = new Intent(ZouponsLogin.this,StoreOwner_HomePage.class);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(login_intent);
					finish();
				}else{ // Shopper
					resetNotificationPreferences();
					AccountLoginFlag.accountUserTypeflag="Customer";
					Intent login_intent = new Intent(ZouponsLogin.this,ShopperHomePage.class);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					login_intent.putExtra("fromlogin", true);
					startActivity(login_intent);
					finish();
				}
			}else if(mKey.trim().equals("Your password has been successfully changed")){
				alertBox_service("Information", "Your password has been successfully changed");
			}else if(mKey.trim().equals("InActive User")){
				//To move to step-2 signup
				//alertBox_service("Information", "Your account is not active please update the security information",mIsMobileVerified);
				Intent intent_addcard = new Intent(ZouponsLogin.this,Registration.class);
				intent_addcard.putExtra("class_name", mTAG);
				startActivity(intent_addcard);
			}else if(mKey.trim().equalsIgnoreCase("mobile not verified")){
				SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
				Editor editor = mPrefs.edit();
				LoginUser_ClassVariables parsedobjectvalues = (LoginUser_ClassVariables) WebServiceStaticArrays.mLoginUserList.get(0);
				if(mRememberMe.isChecked()){
					editor.putBoolean("should_maintain_session", true);
				}else{
					editor.putBoolean("should_maintain_session", false);
				}
				editor.putString("user_id", parsedobjectvalues.userID);
				editor.putString("store_id", parsedobjectvalues.mStoreId);
				editor.putString("user_type", parsedobjectvalues.usertype);
				editor.putString("fb_id", parsedobjectvalues.fbID);
				editor.commit();
				Intent intent_addcard = new Intent(ZouponsLogin.this,Registration.class);
				intent_addcard.putExtra("class_name", mTAG);
				intent_addcard.putExtra("mobile only not verified", true);
				startActivity(intent_addcard);
			}else if(mKey.trim().equals("Wrong Activation Code")){
				alertBox_service("Information", "Your have entered a Wrong Activation Code. Please try again.");
			}else if(mKey.trim().equals("Correct Activation Code")){
				Intent intent_addcard = new Intent(ZouponsLogin.this,AddCreditCard.class);
				intent_addcard.putExtra("class_name", mTAG);
				startActivity(intent_addcard);
			}else {
				alertBox_service("Information","No Data Available");
			}
		}
	};

	// Funtion to show alert pop up with respective message
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
					intent_addcard.putExtra("class_name", mTAG);
					startActivity(intent_addcard);
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}

	// Funtion to show alert pop up with respective message
	private void alertBox_service(final String title,final String msg,final String ismobileverified){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equalsIgnoreCase("your account is not active please update the security information")){
					Intent intent_registration = new Intent(ZouponsLogin.this,Registration.class);
					intent_registration.putExtra("class_name", mTAG);
					startActivity(intent_registration);
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}

	// Funtion to show alert pop up with respective message
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

	// To activate User
	public void userActivation(){
		mProgressDialog.show();
		final Thread syncThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Bundle bundle = new Bundle();
				String mGetResponse=null;
				Message msg_response = new Message();
				try{
					if(mConnectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
						mGetResponse=mZouponswebservice.activateUser(UserDetails.mServiceUserId, mActivationCodeValue);
						if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
							String mParsingResponse=mParsingclass.parseActivateuser(mGetResponse);
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

							}else if(mParsingResponse.equalsIgnoreCase("norecords")){
								updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler

							}
						}else{
							updateHandler(bundle, msg_response, mGetResponse);	//send update to handler about emailid validation response
						}
					}else{
						Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
					}
				}catch(Exception e){

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

	// Funtion to show alert pop up with respective message
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
				if(!mLocationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER )){
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
		mGuestLoginButton.setBackgroundDrawable(null);
		mLocationManager = null;
		//mMyCurrentLocation = null;
		mLocationResult = null;
		mDefaultLoginPage = null;
		// To notify  system that its time to run garbage collector service
		System.gc();
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
		
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		//mMyCurrentLocation.removeUpdates(); 	//Function to remove GPS Updates
	}

	@Override
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		super.onUserLeaveHint();
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
		return;
	}

	private void startFetchingContactsService(){
		Intent mIntent = new Intent(this,ContactsLoaderService.class);
		startService(mIntent);
	}

	private void resetNotificationPreferences(){
		// Setting notification flag to initial_load, to load notification from initial position
		SharedPreferences mPreference = getSharedPreferences("NotificationDetailsPrefences", Context.MODE_PRIVATE);
		Editor mPrefEditor = mPreference.edit();
		mPrefEditor.putString("notification_flag", "initial_load");
		mPrefEditor.commit();
		// To flush notification arraylist and count variables
		WebServiceStaticArrays.mAllNotifications.clear();
		NotificationDetails.notificationcount = 0 ;
	}


	// To query OS to check whether notifcation service running
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