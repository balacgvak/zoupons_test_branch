/**
 * 
 */
package com.us.zoupons;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.ClassVariables.LoginUser_ClassVariables;
import com.us.zoupons.ClassVariables.MobileCarriers_ClassVariables;
import com.us.zoupons.ClassVariables.Registration_ClassVariables;
import com.us.zoupons.ClassVariables.SecurityQuestions_ClassVariables;
import com.us.zoupons.ClassVariables.SignUp_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.FlagClasses.AccountLoginFlag;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.android.AsyncThreadClasses.SendMobileVerficationAsyncTask;
import com.us.zoupons.android.AsyncThreadClasses.SignUpStepOne_Verify;
import com.us.zoupons.cards.ImageLoader;
import com.us.zoupons.rewards.TermsAndConditions;

/**
 * Class to register user information.
 * Call forgotpassword webservice to validate emailid. 
 */
public class Registration extends Activity {

	private String TAG="Registration";

	private Typeface mZouponsFont;
	EncryptionClass encryption;
	private ProgressDialog mProgressDialog=null;
	private LinearLayout mContactInformationStep1,mContactInformationStep2;
	private TextView mContactInformation,mAddImage,mFirstName,mLastName,mEmaiID,mMobileNumber/*,mMobileActivationCode*//*,mMobileCarrier*/;
	private TextView mPassword,mConfirmPassword,mSecurityQuestion1,mAnswer1,mSecurityQuestion2,mAnswer2;
	private EditText mFirstNameValue,mLastNameValue,mEmailIDValue,mMobileNumberValue/*mMobileActivationCodeValue*//*,mMobileCarrierValue*/;
	private EditText mPasswordValue,mConfirmPasswordValue,mSecurityQuestion1Value,mAnswer1Value,mSecurityQuestion2Value,mAnswer2Value,mAddPinValue,mReEnterPinValue;
	private Button mCancel,mNext,mMobileActivationCode,mCancel_email;
	private LinearLayout mAddUserImage,mAddPinLayout;
	private ImageView mDropDownMobileCarrier,mDropDownSecurityQuestion1,mDropDownSecurityQuestion2;
	private ImageView mDefaultAddImage;
	private FrameLayout mMobileCarrierContainer;
	private RelativeLayout mMobileNumberWholeContainer;

	private NetworkCheck connectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private Registration_ClassVariables registrationClassVariables = null;

	private int mSetImageWidth,mSetImageHeight;
	public static int mAdSlotImageWidth;
	private String mAlertMsg;
	private char validateResult;
	private int validateFlag=0;
	public Bitmap mBitmapProfileImage;
	public byte[] mProfileImage;
	public String mSqId1,mSqId2;
	public static int ONACTIVITYRESULTFLAG=0;

	//Facebook App ID
	private static String APP_ID = "319327104838055";

	//Instance of Facebook Class
	private SharedPreferences mPrefs;
	private Bitmap fb_image;
	private String fb_id="",fb_firstname="",fb_lastname="",fb_email="",fb_accesstoken="";
	
	// Terms and conditions variables
	private CheckBox mTermsAndConditionsCheckBox;
	private TextView mTermsAndConditions;

	public String mMobileCarriersId;
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;

	public static String mUserId="0";

	public ScrollView mSignUpStepOneScrollView;
	public RelativeLayout mVerificationPopUp;

	public TextView mPopUpHeader;
	public ImageView mAnimateImageView;
	public EditText mVerificationCode;
	public Button mPopUpCancel,mPopUpVerify;
	public LinearLayout mEmailIdContainer;
	
	private ProgressBar mAd_ProgressBar;
	public Timer mTimer;
	public AdvertisementTimerTask mTimerTask;
	
	public static final String PREFENCES_FILE = "UserNamePrefences";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);

		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		encryption= new EncryptionClass();

		mBitmapProfileImage = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage); 
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		mBitmapProfileImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
		mProfileImage=stream.toByteArray();

		mProgressDialog=new ProgressDialog(this);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);

		mContactInformationStep1=(LinearLayout) findViewById(R.id.registration_page1container);
		mContactInformationStep2=(LinearLayout) findViewById(R.id.registration_page2container);

		mAddUserImage =(LinearLayout) findViewById(R.id.registration_layout_addimage);
		mMobileNumberWholeContainer = (RelativeLayout) findViewById(R.id.registration_mobilenumber_whole_container);
		mEmailIdContainer = (LinearLayout) findViewById(R.id.registration_emailid_container); 

		mSignUpStepOneScrollView=(ScrollView) findViewById(R.id.registration_scrollview);
		mVerificationPopUp=(RelativeLayout) findViewById(R.id.registration_verificationpopup_container);

		mPopUpHeader=(TextView) findViewById(R.id.registration_popup_header);
		mAnimateImageView=(ImageView) findViewById(R.id.registration_animateimage);
		mAd_ProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
		mVerificationCode=(EditText) findViewById(R.id.registration_mobileverificationcodevalue);
		mPopUpCancel=(Button) findViewById(R.id.registration_popup_cancel);
		mPopUpVerify=(Button) findViewById(R.id.registration_popup_verify);

		mMobileCarrierContainer=(FrameLayout) findViewById(R.id.registration_mobilecarrier_container);

		mDropDownMobileCarrier=(ImageView) findViewById(R.id.registration_mobilecarrier_contextmenu);
		mDropDownSecurityQuestion1=(ImageView) findViewById(R.id.registration_securityquestion1_contextmenu);
		mDropDownSecurityQuestion2=(ImageView) findViewById(R.id.registration_securityquestion2_contextmenu);
		mDefaultAddImage=(ImageView) findViewById(R.id.registration_add_userimage);

		mContactInformation=(TextView) findViewById(R.id.registration_contactinformation);
		mContactInformation.setTypeface(mZouponsFont);
		mAddImage=(TextView) findViewById(R.id.registration_addimage);
		mFirstName=(TextView) findViewById(R.id.registration_firstname);
		mFirstName.setTypeface(mZouponsFont);
		mLastName=(TextView) findViewById(R.id.registration_lastname);
		mLastName.setTypeface(mZouponsFont);
		mEmaiID=(TextView) findViewById(R.id.registration_emailid);
		mEmaiID.setTypeface(mZouponsFont);
		mMobileNumber=(TextView) findViewById(R.id.registration_mobilenumber);
		mMobileNumber.setTypeface(mZouponsFont);
		mMobileActivationCode = (Button) findViewById(R.id.registration_sendmobileactivationcode);
		mMobileActivationCode.setTypeface(mZouponsFont);

		mFirstNameValue=(EditText) findViewById(R.id.registration_firstname_value);
		mFirstNameValue.setTypeface(mZouponsFont);
		mLastNameValue=(EditText) findViewById(R.id.registration_lastname_value);
		mLastNameValue.setTypeface(mZouponsFont);
		mEmailIDValue=(EditText) findViewById(R.id.registration_emailid_value);
		mEmailIDValue.setTypeface(mZouponsFont);
		mMobileNumberValue=(EditText) findViewById(R.id.registration_mobilenumber_value);
		mMobileNumberValue.setTypeface(mZouponsFont);

		mPassword=(TextView) findViewById(R.id.registration_password);
		mPassword.setTypeface(mZouponsFont);
		mConfirmPassword=(TextView) findViewById(R.id.registration_confirmpassword);
		mConfirmPassword.setTypeface(mZouponsFont);
		mSecurityQuestion1=(TextView) findViewById(R.id.registration_securityquestion1);
		mSecurityQuestion1.setTypeface(mZouponsFont);
		mAnswer1=(TextView) findViewById(R.id.registration_answer1);
		mAnswer1.setTypeface(mZouponsFont);
		mSecurityQuestion2=(TextView) findViewById(R.id.registration_securityquestion2);
		mSecurityQuestion2.setTypeface(mZouponsFont);
		mAnswer2=(TextView) findViewById(R.id.registration_answer2);
		mAnswer2.setTypeface(mZouponsFont);

		mPasswordValue=(EditText) findViewById(R.id.registration_password_value);
		mPasswordValue.setTypeface(mZouponsFont);
		mConfirmPasswordValue=(EditText) findViewById(R.id.registration_confirmpassword_value);
		mConfirmPasswordValue.setTypeface(mZouponsFont);
		mSecurityQuestion1Value=(EditText) findViewById(R.id.registration_securityquestion1_value);
		mSecurityQuestion1Value.setTypeface(mZouponsFont);
		mAnswer1Value=(EditText) findViewById(R.id.registration_answer1_value);
		mAnswer1Value.setTypeface(mZouponsFont);
		mSecurityQuestion2Value=(EditText) findViewById(R.id.registration_securityquestion2_value);
		mSecurityQuestion2Value.setTypeface(mZouponsFont);
		mAnswer2Value=(EditText) findViewById(R.id.registration_answer2_value);
		mAnswer2Value.setTypeface(mZouponsFont);
		//Add pin layout declarations
		mAddPinValue=(EditText) findViewById(R.id.registration_enterpin_value);
		mAddPinValue.setTypeface(mZouponsFont);
		mAddPinValue.setInputType(InputType.TYPE_CLASS_NUMBER);
		mReEnterPinValue=(EditText) findViewById(R.id.registration_reenterpin_value);
		mReEnterPinValue.setTypeface(mZouponsFont);
		mReEnterPinValue.setInputType(InputType.TYPE_CLASS_NUMBER);
		// Terms and conditions variable initializations
		mTermsAndConditionsCheckBox = (CheckBox) findViewById(R.id.zoupons_termsandconditions_Checkbox);
		mTermsAndConditions = (TextView) findViewById(R.id.zoupons_termsandconditionsId);
		mTermsAndConditions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Registration.this,TermsAndConditions.class));		
			}
		});

		mCancel=(Button) findViewById(R.id.registration_cancel);
		mCancel.setTypeface(mZouponsFont);
		mCancel_email=(Button) findViewById(R.id.registration_email_cancel);
		mCancel_email.setTypeface(mZouponsFont);
		mNext=(Button) findViewById(R.id.registration_next);
		mNext.setTypeface(mZouponsFont);

		zouponswebservice = new ZouponsWebService(Registration.this);
		parsingclass = new ZouponsParsingClass(getApplicationContext());
		connectionAvailabilityChecking = new NetworkCheck();
		registrationClassVariables = new Registration_ClassVariables();
		registrationClassVariables.profileimage=Base64.encodeBytes(mProfileImage);	

		if(getIntent().hasExtra("class_name")){   // From login page
			if(WebServiceStaticArrays.mLoginUserList.size() > 0){
				LoginUser_ClassVariables login_obj = (LoginUser_ClassVariables) WebServiceStaticArrays.mLoginUserList.get(0);
				if(login_obj.isMobileVerified.equalsIgnoreCase("yes")){
					Registration.mUserId = UserDetails.mServiceUserId;

					mContactInformationStep1.setVisibility(View.GONE);
					mContactInformationStep2.setVisibility(View.VISIBLE);
					mCancel.setVisibility(View.VISIBLE);
					mNext.setVisibility(View.VISIBLE);
					RelativeLayout.LayoutParams mCancelParams = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.cancelbuttonwidth),getResources().getDimensionPixelSize(R.dimen.cancelbuttonheight));
					mCancelParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					mCancel.setLayoutParams(mCancelParams);
					mNext.setText("Save");
					// To call security questions...
					progress_SecurityQuestions();
				}else{
					// variable for advertisement timer task
					mTimer = new Timer();
					mTimerTask = new AdvertisementTimerTask(Registration.this, mAnimateImageView, mAd_ProgressBar,"mobile_activation");

					Registration.mUserId = UserDetails.mServiceUserId;
					mEmailIdContainer.setVisibility(View.GONE);
					mMobileNumberWholeContainer.setVisibility(View.VISIBLE);
					mCancel.setVisibility(View.VISIBLE);
					mNext.setVisibility(View.VISIBLE);
					RelativeLayout.LayoutParams mCancelParams = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.cancelbuttonwidth),getResources().getDimensionPixelSize(R.dimen.cancelbuttonheight));
					mCancelParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					mCancel.setLayoutParams(mCancelParams);
					mFirstNameValue.setText(login_obj.firstName);
					mLastNameValue.setText(login_obj.lastName);
					mAddImage.setVisibility(View.GONE);
					ImageLoader mProfileImageLoader = new ImageLoader(Registration.this);
					mProfileImageLoader.DisplayImage(login_obj.mUserProfilePath,mDefaultAddImage);
				}
			}else{ // if login list is empty
				Log.i("registration", "login list empty");
			}
		}else{ // Fresh sign up
			Log.i("registration", "fresh sign up");
			// variable fro advertisement timer task
			
			mTimer = new Timer();
			mTimerTask = new AdvertisementTimerTask(Registration.this, mAnimateImageView, mAd_ProgressBar,"email_activation");
		}

		mAddUserImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try{
					contextMenuOpen(mAddUserImage);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		mMobileNumberValue.addTextChangedListener(new MobileNumberTextWatcher());
		mMobileNumberValue.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});

		mMobileActivationCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				registrationClassVariables.firstName=mFirstNameValue.getText().toString().trim();
				registrationClassVariables.lastName=mLastNameValue.getText().toString().trim();
				registrationClassVariables.emailId=mEmailIDValue.getText().toString().trim();

				if(registrationClassVariables.firstName.equalsIgnoreCase("")){
					setvalidation("Please Enter First Name",1,'N');
				}else if(registrationClassVariables.lastName.equalsIgnoreCase("")){
					setvalidation("Please Enter Last Name",2,'N');
				}else if(registrationClassVariables.emailId.equalsIgnoreCase("")){
					setvalidation("Please Enter Email Id",3,'N');
				}else if(!isValidEmail(registrationClassVariables.emailId)){
					setvalidation("Please Enter Valid MailId",6,'N');
				}else{
					validateResult='Y';
				}

				if(validateResult=='N'){
					alertBox_validation(mAlertMsg, validateFlag);
				}else{
					UserDetails.mServiceEmail = mEmailIDValue.getText().toString().trim();
					
					SignUpStepOne_Verify signupstepone_verify = new SignUpStepOne_Verify(Registration.this, "verify_email", mMobileNumberValue, mMobileActivationCode, mEmailIDValue, mEmailIdContainer,mSignUpStepOneScrollView,mVerificationPopUp,mNext,mContactInformationStep1,mContactInformationStep2,mPopUpHeader,mVerificationCode,mCancel,mMobileNumberWholeContainer,mAnimateImageView,mAd_ProgressBar,mContactInformationStep1,mContactInformationStep2,mTimerTask,mTimer);
					signupstepone_verify.execute(Registration.mUserId,mEmailIDValue.getText().toString().trim(),"",mFirstNameValue.getText().toString().trim(),mLastNameValue.getText().toString().trim(),registrationClassVariables.profileimage,"");
				}
			}
		});

		mPopUpCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mVerificationCode.getText().clear();
				
				// To cancel advertisement timer and task
				if(mTimer != null){
					Log.i("Pop up cancel", "Timer not null");
					mTimer.cancel();
					mTimer = null;
				}
				if(mTimerTask != null){
					Log.i("Pop up cancel", "Timer task not null");
					mTimerTask.cancel();
				}
				
				if(mPopUpHeader.getText().toString().trim().equals("Enter Mobile Verification Code")){
					finish();
				}else if(mPopUpHeader.getText().toString().trim().equals("Enter Email Verification Code")){
					finish();
				}
			}
		});

		mPopUpVerify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!mVerificationCode.getText().toString().trim().equals("")){
					if(mPopUpHeader.getText().toString().trim().equals("Enter Mobile Verification Code")){
						SignUpStepOne_Verify signupstepone_verify = new SignUpStepOne_Verify(Registration.this, "verify_mobile", mMobileNumberValue, mMobileActivationCode, mEmailIDValue, mEmailIdContainer, mSignUpStepOneScrollView, mVerificationPopUp, mNext, mContactInformationStep1, mContactInformationStep2, mPopUpHeader, mVerificationCode, mCancel, mMobileNumberWholeContainer, mAnimateImageView, mAd_ProgressBar, mContactInformationStep1, mContactInformationStep2, mTimerTask, mTimer);
						signupstepone_verify.execute(Registration.mUserId,"",mVerificationCode.getText().toString().trim(),mFirstNameValue.getText().toString().trim(),mLastNameValue.getText().toString().trim(),registrationClassVariables.profileimage,registrationClassVariables.mobileNumber);
					}else if(mPopUpHeader.getText().toString().trim().equals("Enter Email Verification Code")){

						mProgressDialog.show();
						final Thread syncThread = new Thread(new Runnable() {

							@Override
							public void run() {
								Bundle bundle = new Bundle();
								String mGetResponse=null;
								Message msg_response = new Message();

								try{
									if(connectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){

										mGetResponse=zouponswebservice.login(mEmailIDValue.getText().toString().trim(), encryption.md5(mVerificationCode.getText().toString().trim()));	//Get login response values from webservice

										if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
											String mParsingResponse=parsingclass.parseLoginXmlData(mGetResponse,TAG);
											if(mParsingResponse.equalsIgnoreCase("success")){
												for(int i=0;i<WebServiceStaticArrays.mLoginUserList.size();i++){
													LoginUser_ClassVariables parsedobjectvalues = (LoginUser_ClassVariables) WebServiceStaticArrays.mLoginUserList.get(i);

													UserDetails.mServiceUserId=parsedobjectvalues.userID;
													UserDetails.mServiceFirstName=parsedobjectvalues.firstName;
													UserDetails.mServiceLastName=parsedobjectvalues.lastName;
													UserDetails.mServiceEmail=mEmailIDValue.getText().toString().trim();
													UserDetails.mServiceFbId=parsedobjectvalues.fbID;
													UserDetails.mServicePin=parsedobjectvalues.pin;
													UserDetails.mServiceForgotPwdFlag=parsedobjectvalues.forgotPwdFlag;
													UserDetails.mServiceMessage=parsedobjectvalues.message;
													UserDetails.mUserType=parsedobjectvalues.usertype;

													
													if(!parsedobjectvalues.message.equalsIgnoreCase("Your account still not activated")){
														if(parsedobjectvalues.mFlag.equals("no")){
															updateHandler_Login(bundle, msg_response, "Invalid User");	//send update to handler
														}else if(parsedobjectvalues.mFlag.equals("yes")){
															updateHandler_Login(bundle, msg_response, "Valid User");
														}
													}else{
														updateHandler_Login(bundle, msg_response, "InActive User");
													}
												}
											}else if(mParsingResponse.equalsIgnoreCase("failure")){
												updateHandler_Login(bundle, msg_response, mParsingResponse);	//send update to handler
												Log.i(TAG,"Error");
											}else if(mParsingResponse.equalsIgnoreCase("norecords")){
												updateHandler_Login(bundle, msg_response, mParsingResponse);	//send update to handler
												Log.i(TAG,"No Records");
											}
										}else{
											updateHandler_Login(bundle, msg_response, mGetResponse);	//send update to handler about login response
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
						// To cancel advertisement timer and task
					}
				}else{
					if(mPopUpHeader.getText().toString().trim().equals("Enter Mobile Verification Code")){
						alertBox_validation("Enter Mobile Verification Code", 5);
					}else {
						alertBox_validation("Enter Email Verification Code", 5);
					}
				}
			}
		});

		mPasswordValue.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(mPasswordValue.getText().toString().length()<6){
						alertBox_validation("Password should contain minimum of 6 characters", 18);
					}
				}
			}
		});
		
		mAddPinValue.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(mAddPinValue.getText().toString().length()<4){
						alertBox_validation("Add Pin should contain minimum of 4 characters", 19);
					}
				}
			}
		});
		
		mSecurityQuestion1Value.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openSecurityDialog(1);
			}
		});

		mDropDownSecurityQuestion1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openSecurityDialog(1);
			}
		});
		
		mSecurityQuestion2Value.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openSecurityDialog(2);
			}
		});

		mDropDownSecurityQuestion2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openSecurityDialog(2);
			}
		});
		
		mNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(mNext.getText().toString().equalsIgnoreCase("Next")){

					mVerificationCode.getText().clear();

					registrationClassVariables.firstName=mFirstNameValue.getText().toString().trim();
					registrationClassVariables.lastName=mLastNameValue.getText().toString().trim();
					registrationClassVariables.emailId=mEmailIDValue.getText().toString().trim();
					registrationClassVariables.mobileNumber=mMobileNumberValue.getText().toString().trim();
					registrationClassVariables.mobileCarrierId=mMobileCarriersId;

					if(registrationClassVariables.firstName.equalsIgnoreCase("")){
						setvalidation("Please Enter First Name",1,'N');
					}else if(registrationClassVariables.lastName.equalsIgnoreCase("")){
						setvalidation("Please Enter Last Name",2,'N');
					}else if(registrationClassVariables.mobileNumber.equalsIgnoreCase("")){
						setvalidation("Please Enter Mobile Number",4,'N');
					}else if(registrationClassVariables.mobileNumber.trim().length() != 12){
						setvalidation("Please Enter Valid MobileNumber",4,'N');
					}else{
						validateResult = 'Y';
					}

					if(validateResult=='N'){
						alertBox_validation(mAlertMsg, validateFlag);
					}else{
						mTimer = new Timer();
						mTimerTask = new AdvertisementTimerTask(Registration.this,  mAnimateImageView, mAd_ProgressBar,"mobile_activation");
						SendMobileVerficationAsyncTask sendmobileverification = new SendMobileVerficationAsyncTask(Registration.this,"sms",mVerificationCode,mSignUpStepOneScrollView,mVerificationPopUp,mPopUpHeader,mVerificationCode,mAnimateImageView,mAd_ProgressBar,null,mTimerTask,mTimer);
						sendmobileverification.execute(Registration.mUserId,registrationClassVariables.firstName,registrationClassVariables.lastName,registrationClassVariables.profileimage,registrationClassVariables.mobileNumber);
					}
				}else if(mNext.getText().toString().equalsIgnoreCase("Save")){

					registrationClassVariables.password=mPasswordValue.getText().toString();
					registrationClassVariables.confirmpassword=mConfirmPasswordValue.getText().toString();
					registrationClassVariables.securityquestion1=mSecurityQuestion1Value.getText().toString().trim();
					registrationClassVariables.answer1=mAnswer1Value.getText().toString().trim();
					// Changes
					registrationClassVariables.securityquestion2 = registrationClassVariables.securityquestion1;
					registrationClassVariables.answer2 = registrationClassVariables.answer1;

					registrationClassVariables.AddPin=mAddPinValue.getText().toString();
					registrationClassVariables.Re_enterPin=mReEnterPinValue.getText().toString();
					if(registrationClassVariables.password.equalsIgnoreCase("")){
						setvalidation("Enter Password",7,'N');	
					}else if(registrationClassVariables.confirmpassword.equalsIgnoreCase("")){
						setvalidation("Enter Confirm Password",8,'N');
					}else if(!registrationClassVariables.confirmpassword.contentEquals(registrationClassVariables.password)){
						setvalidation("Enter Correct Confirm Password",9,'N');
					}else if(registrationClassVariables.securityquestion1.equalsIgnoreCase("")){
						setvalidation("Select Security Question 1",10,'N');
					}else if(registrationClassVariables.answer1.equalsIgnoreCase("")){
						setvalidation("Answer Security Question 1",11,'N');
					}else if(registrationClassVariables.AddPin.equalsIgnoreCase("")){
						setvalidation("Please enter security PIN number",15,'N');
					}else if(registrationClassVariables.Re_enterPin.equalsIgnoreCase("")){
						setvalidation("Please re-enter security PIN number",16,'N');
					}else if(!registrationClassVariables.AddPin.equalsIgnoreCase(registrationClassVariables.Re_enterPin)){
						setvalidation("PIN mismatched , please enter correct PIN numbers",17,'N');
					}else if(mTermsAndConditionsCheckBox.isChecked() == false){
						setvalidation("Please accept Zoupons terms and conditions to proceed",14,'N');
					}else{
						validateResult='Y';
					}

					if(validateResult=='N'){
						alertBox_validation(mAlertMsg, validateFlag);
					}else{
						mProgressDialog.show();
						final Thread syncThread = new Thread(new Runnable() {

							@Override
							public void run() {
								Bundle bundle = new Bundle();
								String mGetResponse=null;
								Message msg_response = new Message();
								String mParsedIdValue=null,mParsedMessageValue=null;
								try{
									if(connectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
										// Removing hiphen while sending to webservice
										String mobilenumber =  registrationClassVariables.mobileNumber;
										if(mobilenumber.contains("-")){
											mobilenumber = mobilenumber.replaceAll("-", "");
										}
										// Webservice call for sign up step-2
										mGetResponse=zouponswebservice.signup_Step2(mUserId,new EncryptionClass().md5(registrationClassVariables.password),mSqId1,registrationClassVariables.answer1,mSqId1,registrationClassVariables.answer2,new EncryptionClass().md5(registrationClassVariables.AddPin));	//Get signup response values from webservice

										if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
											String mParsingResponse=parsingclass.parseSignUp(mGetResponse);
											if(mParsingResponse.equalsIgnoreCase("success")){
												for(int i=0;i<WebServiceStaticArrays.mSignUpList.size();i++){
													SignUp_ClassVariables parsedobjectvalues = (SignUp_ClassVariables) WebServiceStaticArrays.mSignUpList.get(i);
													mParsedIdValue = parsedobjectvalues.mId;
													mParsedMessageValue = parsedobjectvalues.mMessage;
													SharedPreferences mPrefs = getSharedPreferences("TokenIdPrefences", MODE_PRIVATE);
													Editor editor = mPrefs.edit();
													editor.putString("tokenid", parsedobjectvalues.mAuthToken);
													editor.commit();
												}
												bundle.putString("updateui", "signup");
												msg_response.setData(bundle);
												handler_updateUI.sendMessage(msg_response);
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
			}
		});

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WebServiceStaticArrays.mGetSecurityQuestionsEmailValidationList.clear();
				WebServiceStaticArrays.mMobileCarriersList.clear();
				WebServiceStaticArrays.mSecurityQuestionsList.clear();
				WebServiceStaticArrays.mSignUpList.clear();
				Registration.this.finish();

				Intent intent_cancel = new Intent(Registration.this,ZouponsLogin.class);
				intent_cancel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_cancel);
			}
		});
		
		mCancel_email.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WebServiceStaticArrays.mGetSecurityQuestionsEmailValidationList.clear();
				WebServiceStaticArrays.mMobileCarriersList.clear();
				WebServiceStaticArrays.mSecurityQuestionsList.clear();
				WebServiceStaticArrays.mSignUpList.clear();
				Registration.this.finish();

				Intent intent_cancel = new Intent(Registration.this,ZouponsLogin.class);
				intent_cancel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_cancel);
			}
		});
	}

	private void setvalidation(String msg,int validateflag,char validateresult){
		mAlertMsg=msg;
		validateFlag=validateflag;
		validateResult=validateresult;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mSetImageWidth=mAddUserImage.getWidth();
		mSetImageHeight=mAddUserImage.getHeight();
		mAdSlotImageWidth = mAnimateImageView.getWidth();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(ONACTIVITYRESULTFLAG==1){
			if(resultCode!=0){
				if(requestCode==1){
					setScaledBitmap(data);
				}else if(requestCode == 2){
					setScaledBitmap(data);
				}
			}else{
				//To Remove Default Image
				if(mDefaultAddImage.getVisibility()==View.VISIBLE){
					mDefaultAddImage.setVisibility(View.VISIBLE);
					mAddImage.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	
	public void setScaledBitmap(Intent data){
		try{
			Uri uri=data.getData();
			// specifying column(data) for retrieval
			String[] file_path_columns={MediaStore.Images.Media.DATA};
			// querying content provider to get particular image
			Cursor cursor=getContentResolver().query(uri, file_path_columns, null, null, null);
			cursor.moveToFirst();
			// getting col_index from string file_path_columns[0]--> Data column 
			int column_index=cursor.getColumnIndex(file_path_columns[0]);
			// getting the path from result as /sdcard/DCIM/100ANDRO/file_name
			String selected_file_path=cursor.getString(column_index);
			Log.i("selected path"," "+selected_file_path);	
			cursor.close();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(selected_file_path, options);
			// Calculate inSampleSize
			options.inSampleSize = Settings.calculateInSampleSize(options, mAddUserImage.getWidth(), mAddUserImage.getHeight());
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap mSelectedImage = BitmapFactory.decodeFile(selected_file_path, options);
			mSelectedImage = new DecodeImageWithRotation().decodeImage(selected_file_path, mSelectedImage, mSetImageWidth, mSetImageHeight);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);    
			byte[] imagedata = baos.toByteArray();
			registrationClassVariables.profileimage = com.us.zoupons.Base64.encodeBytes(imagedata);
			Bitmap userimage=Bitmap.createScaledBitmap(mSelectedImage, mSetImageWidth, mSetImageHeight, true);
			BitmapDrawable drawable = new BitmapDrawable(userimage);
			//To Remove Default Image
			if(mDefaultAddImage.getVisibility()==View.VISIBLE){
				mDefaultAddImage.setVisibility(View.GONE);
				mAddImage.setVisibility(View.GONE);
			}
			mAddUserImage.setBackgroundDrawable(drawable);
		}catch(Exception e){
			e.printStackTrace();
			if(mDefaultAddImage.getVisibility()==View.VISIBLE){
				mDefaultAddImage.setVisibility(View.VISIBLE);
				mAddImage.setVisibility(View.VISIBLE);
			}
			Toast.makeText(getApplicationContext(), "Please select valid image.", Toast.LENGTH_SHORT).show();
		}
	}
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.i(TAG,"Get GroupId : "+item.getGroupId());

		switch(item.getGroupId()){
		case 0:
			return true;
		case 1:
			String selectedquestion1 = mSecurityQuestion1Value.getText().toString().trim();
			for(int j=0;j<WebServiceStaticArrays.mSecurityQuestionsList.size();j++){
				SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(j);

				if(selectedquestion1.equals(obj.mSecurityQuestions_securityquestion)){
					obj.mIsSelected="no";
					WebServiceStaticArrays.mSecurityQuestionsList.set(j, obj);
				}

				if(item.getTitle().equals(obj.mSecurityQuestions_securityquestion)){
					obj.mIsSelected="yes";
					mSecurityQuestion1Value.setText(item.getTitle());
					mSqId1=obj.mSecurityQuestios_securityquestionid;
					WebServiceStaticArrays.mSecurityQuestionsList.set(j, obj);
				}
			}
			return true;
		case 2:
			String selectedquestion2 = mSecurityQuestion2Value.getText().toString().trim();
			for(int k=0;k<WebServiceStaticArrays.mSecurityQuestionsList.size();k++){
				SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(k);

				if(selectedquestion2.equals(obj.mSecurityQuestions_securityquestion)){
					obj.mIsSelected="no";
					WebServiceStaticArrays.mSecurityQuestionsList.set(k, obj);
				}

				if(item.getTitle().equals(obj.mSecurityQuestions_securityquestion)){
					obj.mIsSelected="yes";
					mSecurityQuestion2Value.setText(item.getTitle());
					mSqId2=obj.mSecurityQuestios_securityquestionid;
					WebServiceStaticArrays.mSecurityQuestionsList.set(k,obj);
				}
			}
			return true;
		case 3:  // for adding user image from camera or gallery
			if(item.getItemId() == 0){
				ONACTIVITYRESULTFLAG=1;
				Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent_camera, 1);
			}else if(item.getItemId() == 1){
				ONACTIVITYRESULTFLAG=1;
				Intent mGalleryIntent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(mGalleryIntent, 2);
			}
			return true;	
		default :
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if(v.equals(mDropDownMobileCarrier)==true){
			menu.setHeaderTitle("Mobile Carriers");
			for(int i=0;i<WebServiceStaticArrays.mMobileCarriersList.size();i++){
				MobileCarriers_ClassVariables obj = (MobileCarriers_ClassVariables) WebServiceStaticArrays.mMobileCarriersList.get(i);
				menu.add(0, Integer.parseInt(obj.mId), 0, obj.mName);
			}
		}else if(v.equals(mSecurityQuestion1Value)==true){
			securityQuestions(menu,v,1);
		}else if(v.equals(mSecurityQuestion2Value)==true){	
			securityQuestions(menu,v,2);
		}else if(v.equals(mAddUserImage)){
			menu.setHeaderTitle("Choose From");
			menu.add(3, 0, 0, "Take Picture");
			menu.add(3, 1, 1, "Gallery");
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	private void securityQuestions(ContextMenu menu,View v,int groupid){
		menu.setHeaderTitle("Security Questions");
		if(groupid==1){
			for(int i=0;i<WebServiceStaticArrays.mSecurityQuestionsList.size();i++){
				SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(i);
				if(obj.mIsSelected.equals("no")){
					menu.add(1, v.getId(), 0, obj.mSecurityQuestions_securityquestion);
				}
			}
		}else if(groupid==2){
			for(int i=0;i<WebServiceStaticArrays.mSecurityQuestionsList.size();i++){
				SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(i);
				if(obj.mIsSelected.equals("no")){
					menu.add(2, v.getId(), 0, obj.mSecurityQuestions_securityquestion);
				}
			}
		}
	}

	private String getSecurityQuestions(Bundle bundle,Message msg){
		String mGetResponse/*,mParsedValue1,mParsedValue2,mParsedValue3,mParsedValue4,mParsedValue5*/;
		String rtnValue="";
		if(connectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){

			mGetResponse=zouponswebservice.securityQuestions();	//Get SecurityQuestions response values from webservice

			if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
				String mParsingResponse=parsingclass.parseSecurityQuestionsXmlData(mGetResponse);
				if(mParsingResponse.equalsIgnoreCase("success")){
					for(int i=0;i<WebServiceStaticArrays.mSecurityQuestionsList.size();i++){
						SecurityQuestions_ClassVariables parsedobjectvalues = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(i);
						rtnValue="success";
					}
				}else if(mParsingResponse.equalsIgnoreCase("failure")){
					updateHandler(bundle, msg, mParsingResponse);	//send update to handler
					Log.i(TAG,"Error");
				}else if(mParsingResponse.equalsIgnoreCase("norecords")){
					updateHandler(bundle, msg, mParsingResponse);	//send update to handler
					Log.i(TAG,"No Records");
				}
			}else{
				updateHandler(bundle, msg, mGetResponse);	//send update to handler about emailid validation response
			}
		}else{
			Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
		}
		return rtnValue;
	}

	private void openSecurityDialog(final int position){
		final Dialog mSecurityQuestionsDialog = new Dialog(Registration.this);
		mSecurityQuestionsDialog.setTitle("Security Questions");
		mSecurityQuestionsDialog.setContentView(R.layout.card_buybutton_menu);
		ListView mQuestionsList = (ListView) mSecurityQuestionsDialog.findViewById(R.id.lists);

		ArrayList<String> mQuestions = new ArrayList<String>();
		for(int i=0;i<WebServiceStaticArrays.mSecurityQuestionsList.size();i++){
			SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(i);
			if(obj.mIsSelected.equals("no")){
				mQuestions.add(obj.mSecurityQuestions_securityquestion);
			}
		}
		
		mQuestionsList.setAdapter(new ArrayAdapter<String>(Registration.this, R.layout.card_buy_button_dialog, R.id.optiontext,mQuestions));
		
		mQuestionsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				String selectedquestion = "";
				if(position == 1){
					selectedquestion = mSecurityQuestion1Value.getText().toString().trim();	
				}else{
					selectedquestion = mSecurityQuestion2Value.getText().toString().trim();
				}
				for(int j=0;j<WebServiceStaticArrays.mSecurityQuestionsList.size();j++){
					SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(j);

					if(selectedquestion.equals(obj.mSecurityQuestions_securityquestion)){
						obj.mIsSelected="no";
						WebServiceStaticArrays.mSecurityQuestionsList.set(j, obj);
					}

					if(arg0.getAdapter().getItem(arg2).equals(obj.mSecurityQuestions_securityquestion)){
						obj.mIsSelected="yes";
						if(position == 1){
							mSecurityQuestion1Value.setText(arg0.getAdapter().getItem(arg2).toString());
							mSqId1=obj.mSecurityQuestios_securityquestionid;
						}else{
							mSecurityQuestion2Value.setText(arg0.getAdapter().getItem(arg2).toString());
							mSqId2=obj.mSecurityQuestios_securityquestionid;
						}
						WebServiceStaticArrays.mSecurityQuestionsList.set(j, obj);
					}
				}
				mSecurityQuestionsDialog.dismiss();
			}
		});
		mSecurityQuestionsDialog.show();
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

	private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse){
		//send update to handler about emailid validation response
		bundle.putString("returnResponse", mGetResponse);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}

	private void updateHandler_Login(Bundle bundle,Message msg_response,String mGetResponse){
		bundle.putString("returnResponse", mGetResponse);
		msg_response.setData(bundle);
		handler_response_login.sendMessage(msg_response);
	}

	private void alertBox_validation(String msg,final int validateflag) {
		AlertDialog.Builder validation_alert = new AlertDialog.Builder(this);
		validation_alert.setTitle("Information");
		validation_alert.setMessage(msg);
		validation_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(validateflag!=0){
					if(validateflag==1){
						mFirstNameValue.requestFocus();
					}else if(validateflag==2){
						mLastNameValue.requestFocus();
					}else if(validateflag==3){
						mEmailIDValue.requestFocus();
					}else if(validateflag==4){
						mMobileNumberValue.requestFocus();
					}else if(validateflag==5){
						mVerificationCode.requestFocus();
					}else if(validateflag==6){
						mEmailIDValue.setText("");
						mEmailIDValue.requestFocus();
					}else if(validateflag==7){
						mPasswordValue.requestFocus();
					}else if(validateflag==8){
						mConfirmPasswordValue.requestFocus();
					}else if(validateflag==9){
						mConfirmPasswordValue.setText("");
						mConfirmPasswordValue.requestFocus();
					}else if(validateflag==10){
						//contextMenuOpen(mSecurityQuestion1Value);
						openSecurityDialog(1);
					}else if(validateflag==11){
						mAnswer1Value.requestFocus();
					}else if(validateflag==12){
						//contextMenuOpen(mSecurityQuestion2Value);
						openSecurityDialog(2);
					}else if(validateflag==13){
						mAnswer2Value.requestFocus();
					}else if(validateflag==15){
						mAddPinValue.requestFocus();
					}else if(validateflag==16){
						mReEnterPinValue.requestFocus();
					}else if(validateflag==17){
						mReEnterPinValue.getText().clear();
						mReEnterPinValue.requestFocus();
					}else if(validateflag==18){
						mPasswordValue.getText().clear();
						mPasswordValue.requestFocus();
					}else if(validateflag==19){
						mAddPinValue.requestFocus();
					}
				}
				dialog.dismiss();
			}
		});
		validation_alert.show();
	}

	private void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this);
		service_alert.setTitle(title);
		if(msg.equals("Successfully Registered")){
			service_alert.setMessage("You account has been successfully activated.");
		}else{
			service_alert.setMessage(msg);
		}
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equals("Successfully Registered")){

					AccountLoginFlag.accountUserTypeflag="Customer";

					ZouponsLogin.mRememberMe.setChecked(false);
					
					Intent login_intent = new Intent(Registration.this,SlidingView.class);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					login_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					login_intent.putExtra("fromlogin", true);
					startActivity(login_intent);
					finish();
				}else if(msg.equals("Email verification is completed successfully.")){

					mVerificationPopUp.setVisibility(View.GONE);
					mSignUpStepOneScrollView.setVisibility(View.VISIBLE);

					mEmailIdContainer.setVisibility(View.GONE);
					mMobileNumberWholeContainer.setVisibility(View.VISIBLE);
					mCancel.setVisibility(View.VISIBLE);
					mNext.setVisibility(View.VISIBLE);
					RelativeLayout.LayoutParams mCancelParams = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.cancelbuttonwidth), getResources().getDimensionPixelSize(R.dimen.cancelbuttonheight));
					mCancelParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					mCancel.setLayoutParams(mCancelParams);

					// To cancel advertisement timer and task
					if(mTimer != null){
						mTimer.cancel();
						mTimer = null;
					}
					if(mTimerTask != null){
						mTimerTask.cancel();
					}
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}

	public void progress_SecurityQuestions(){
		/* To get security question for step2 */				
		mProgressDialog.show();
		final Thread syncThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Bundle bundle = new Bundle();
				String mGetResponse=null;
				Message msg_response = new Message();
				String mParsedValue=null;
				try{
					if(connectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){

						String mRtnValue = getSecurityQuestions(bundle,msg_response);	// Get Security Questions
						if(!mRtnValue.equals("")){
							bundle.putString("updateui", "success");
							msg_response.setData(bundle);
							handler_updateUI.sendMessage(msg_response);
						}else{
							updateHandler(bundle, msg_response,"nosecurityquestions"); //send update to handler
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


	private void contextMenuOpen(View sender){
		sender.setLongClickable(false);
		registerForContextMenu(sender);
		openContextMenu(sender);
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
			}else if(key.trim().equals("validuser")){
				alertBox_service("Sign up status","User already exists. Please change the emailID.");
				mEmailIDValue.setText("");
				mEmailIDValue.requestFocus();
			}else if(key.trim().equals("nosecurityquestions")){
				alertBox_service("Sign up status","Problem in getting security questions, Please try again later.");
			}else {
				alertBox_service("Information","No Data Available");
			}
		}
	};

	Handler handler_response_login = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			String key = b.getString("returnResponse");
			if(key.trim().equals("success")){
				Log.i(TAG,"Response Get Successfully");	
			}else if(key.trim().equals("failure")){
				alertBox_service("Information","Unable to reach Service.Please try again later.");
			}else if(key.trim().equals("noresponse")||key.trim().equals("norecords")){
				alertBox_service("Information","No Data Available");
			}else if(key.trim().equals("Invalid User")){
				alertBox_service("Information","Enter the correct email verification code.");
			}else if(key.trim().equals("Valid User")){
				Log.i(TAG,"Valid User");
			}else if(key.trim().equals("Your password has been successfully changed")){
				alertBox_service("Information", "Your password has been successfully changed");
			}else if(key.trim().equals("InActive User")){
				Log.i(TAG,"InActive User");
				alertBox_service("Information", "Email verification is completed successfully.");
			}else if(key.trim().equals("Wrong Activation Code")){
				alertBox_service("Information", "Your have entered a Wrong Activation Code. Please try again.");
			}else if(key.trim().equals("Correct Activation Code")){
				Log.i(TAG,"Correct Activation Code");
			}else if(key.trim().equals("Your account still not activated")){
				alertBox_service("Information", "Email verification is completed successfully.");
			}else {
				alertBox_service("Information","No Data Available");
			}
		}
	};

	Handler handler_updateUI = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			String key = b.getString("updateui");
			if(key.trim().equals("success")){
				Log.i(TAG,"Security questions success");
			}else if(key.trim().equals("mobilecarrierslist")){
				contextMenuOpen(mDropDownMobileCarrier);
			}else if(key.trim().equals("signup")){
				alertBox_service("Successfully Registered", "Successfully Registered");
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("activity call backs", "on destroy");
		// To cancel advertisement timer and task
		if(mTimer != null){
			mTimer.cancel();
			mTimer = null;
		}else{
			Log.i("Timer", "Timer null");
		}
		if(mTimerTask != null){
			mTimerTask.cancel();
		}else{
			Log.i("Timer", "Timer task null");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}