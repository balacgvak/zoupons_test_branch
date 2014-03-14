package com.us.zoupons.android.asyncthread_class;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.us.zoupons.EncryptionClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.ChangePwd_ClassVariables;
import com.us.zoupons.classvariables.POJOUserSecurityDetails;
import com.us.zoupons.classvariables.SecurityQuestions_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.shopper.settings.Settings;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to communicate with webservice to change security details
 *
 */

public class EditSecurityDetailsTask extends AsyncTask<String, String, String>{
	
	private ProgressDialog mProgressView;
	private Context mContext;
	private ZouponsWebService mZouponswebservice;
	private ZouponsParsingClass mParsingclass;
	private NetworkCheck mConnectivityCheck;
	private String mFunctionType;
	private EditText mUserSecurityQuestion1,mUserSecurityQuestion2,mUserSecurityAnswer1,mUserSecurityAnswer2;
	private LinearLayout mMenuBarContactInfo,mMenuBarEditPassword;
	private ScrollView mContactInfoContainer,mEditPasswordContainer;

	//Constructor for update password and security questions
	public EditSecurityDetailsTask(Context context,LinearLayout menubarcontactinfo,LinearLayout menubareditpassword,ScrollView contactinfocontainer,ScrollView editpasswordcontainer) {
		this.mContext = context;
		mProgressView = new ProgressDialog(context);
		mProgressView.setTitle("Loading");
		mProgressView.setMessage("please wait..");
		mZouponswebservice = new ZouponsWebService(context);
		mParsingclass = new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
		mMenuBarContactInfo=menubarcontactinfo;
		mMenuBarEditPassword=menubareditpassword;
		mContactInfoContainer=contactinfocontainer;
		mEditPasswordContainer=editpasswordcontainer;
	}

	//Constructor to getsecurityquestions
	public EditSecurityDetailsTask(Context context,EditText UserSecurityQuestion1, EditText UserSecurityQuestion2,EditText UserSecurityAnswer1, EditText UserSecurityAnswer2) {
		this.mContext = context;
		mUserSecurityQuestion1 = UserSecurityQuestion1;
		mUserSecurityQuestion2 = UserSecurityQuestion2;
		mUserSecurityAnswer1 = UserSecurityAnswer1;
		mUserSecurityAnswer2 = UserSecurityAnswer2;
		mProgressView = new ProgressDialog(context);
		mProgressView.setTitle("Loading");
		mProgressView.setMessage("please wait..");
		mZouponswebservice = new ZouponsWebService(context);
		mParsingclass = new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressView.show();		
	}

	@Override
	protected String doInBackground(String... params) {
		String mresult = "", mParsingResponse = "";
		try {
			if (mConnectivityCheck.ConnectivityCheck(mContext)) {
				mFunctionType = params[0];
				if(mFunctionType.equalsIgnoreCase("ChangePassword")){
					String mResponse = mZouponswebservice.Change_Password(UserDetails.mServiceUserId,/*new EncryptionClass().md5(params[1])params[1],*/new EncryptionClass().md5(params[2]));
					if (!mResponse.equals("")) {
						if (!mResponse.equals("failure")&& !mResponse.equals("noresponse")) {
							mParsingResponse = mParsingclass.parseChangePwdXmlData(mResponse);
							if (mParsingResponse.equalsIgnoreCase("success")) {
								mresult = "success";
							} else if (mParsingResponse.equalsIgnoreCase("Failure")) {
								mresult = "failure";
							} else if (mParsingResponse.equalsIgnoreCase("norecords")) {
								mresult = "norecords";
							}
						} else {
							mresult = "Response Error.";
						}
					} else {
						mresult = "nodata";
					}
				}else if(mFunctionType.equalsIgnoreCase("GetSecurityQuestions")){
					String mResponse = mZouponswebservice.securityQuestions();
					if (!mResponse.equals("")) {
						if (!mResponse.equals("failure")&& !mResponse.equals("noresponse")) {
							mParsingResponse = mParsingclass.parseSecurityQuestionsXmlData(mResponse);
							if (mParsingResponse.equalsIgnoreCase("success")) {
								String userSecurityDetails = mZouponswebservice.GetUserSecurityQuestions(UserDetails.mServiceUserId);
								if (!userSecurityDetails.equals("")) {
									if (!userSecurityDetails.equals("failure")&& !userSecurityDetails.equals("noresponse")) {
										mParsingResponse = mParsingclass.mParseUserSecurityQuestions(userSecurityDetails);
										if (mParsingResponse.equalsIgnoreCase("success")) {
											mresult = "success";
										} else if (mParsingResponse.equalsIgnoreCase("Failure")) {
											mresult = "failure";
										} else if (mParsingResponse.equalsIgnoreCase("norecords")) {
											mresult = "norecords";
										}
									} else {
										mresult = "Response Error.";
									}
								} else {
									mresult = "nodata";
								}


							} else if (mParsingResponse.equalsIgnoreCase("Failure")) {
								mresult = "failure";
							} else if (mParsingResponse.equalsIgnoreCase("norecords")) {
								mresult = "norecords";
							}
						} else {
							mresult = "Response Error.";
						}
					} else {
						mresult = "nodata";
					}

				}else if(mFunctionType.equalsIgnoreCase("ChangeSecurityQuestions")){
					String mResponse = mZouponswebservice.UpdateUserSecurityQuestions(params[1],params[2], params[3], params[4]);
					if (!mResponse.equals("")) {
						if (!mResponse.equals("failure")&& !mResponse.equals("noresponse")) {
							mParsingResponse = mParsingclass.parseChangePwdXmlData(mResponse);
							if (mParsingResponse.equalsIgnoreCase("success")) {
								mresult = "success";
							} else if (mParsingResponse.equalsIgnoreCase("Failure")) {
								mresult = "failure";
							} else if (mParsingResponse.equalsIgnoreCase("norecords")) {
								mresult = "norecords";
							}
						} else {
							mresult = "Response Error.";
						}
					} else {
						mresult = "nodata";
					}	
				}
			} else {
				mresult = "nonetwork";
			}
		} catch (Exception e) {

		}
		return mresult;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mProgressView.dismiss();
		if (result.equalsIgnoreCase("nonetwork")) {
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		} else if (result.equalsIgnoreCase("Response Error.")) {
			alertBox_service("Information", "Unable to reach service.");
		} else if (result.equalsIgnoreCase("norecords")) {
			alertBox_service("Information", "Data no avilable Please try after some time");
		} else if (result.equalsIgnoreCase("Failure")) {
			alertBox_service("Information", "Unable to process.");
		} else if (result.equalsIgnoreCase("success")) {
			if(mFunctionType.equalsIgnoreCase("ChangePassword")){
				if(WebServiceStaticArrays.mChangePasswordList.size() > 0){
					ChangePwd_ClassVariables passworddetails = (ChangePwd_ClassVariables) WebServiceStaticArrays.mChangePasswordList.get(0); 
					if(passworddetails.mMessage.equalsIgnoreCase("Successfully Changed Password")){
						alertBox_service("Information", "Password updated");			
					}else{
						alertBox_service("Information", "Unable to update the password");
					}
				}
			}else if(mFunctionType.equalsIgnoreCase("GetSecurityQuestions")){
				if(WebServiceStaticArrays.mUserSecurityQuestionsList.size() > 0){
					POJOUserSecurityDetails mSecurityDetails = (POJOUserSecurityDetails) WebServiceStaticArrays.mUserSecurityQuestionsList.get(0);
					mUserSecurityAnswer1.setText(mSecurityDetails.answer1);
					mUserSecurityAnswer2.setText(mSecurityDetails.answer2);

					for(int i=0;i<WebServiceStaticArrays.mSecurityQuestionsList.size();i++){
						SecurityQuestions_ClassVariables obj = (SecurityQuestions_ClassVariables) WebServiceStaticArrays.mSecurityQuestionsList.get(i);
						if(obj.mSecurityQuestios_securityquestionid.equalsIgnoreCase(mSecurityDetails.question1)){
							mUserSecurityQuestion1.setText(obj.mSecurityQuestions_securityquestion);
							obj.mIsSelected = "yes";
							WebServiceStaticArrays.mSecurityQuestionsList.set(i, obj);
							Settings.mSqId1 = obj.mSecurityQuestios_securityquestionid;
						}else if(obj.mSecurityQuestios_securityquestionid.equalsIgnoreCase(mSecurityDetails.question2)){
							mUserSecurityQuestion2.setText(obj.mSecurityQuestions_securityquestion);
							obj.mIsSelected = "yes";
							WebServiceStaticArrays.mSecurityQuestionsList.set(i, obj);
							Settings.mSqId2 = obj.mSecurityQuestios_securityquestionid;
						}
					}
				}

			}else if(mFunctionType.equalsIgnoreCase("ChangeSecurityQuestions")){
				if(WebServiceStaticArrays.mChangePasswordList.size() > 0){
					ChangePwd_ClassVariables passworddetails = (ChangePwd_ClassVariables) WebServiceStaticArrays.mChangePasswordList.get(0); 
					if(passworddetails.mMessage.equalsIgnoreCase("Successfully Changed Security Questions or Answers")){
						alertBox_service("Information", "Security question updated");			
					}else{
						alertBox_service("Information", "Unable to change the Security Questions");
					}
				}
			}
		}
	}

	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(msg.equalsIgnoreCase("Password updated")){
					/*mMenuBarContactInfo.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mMenuBarEditPassword.setBackgroundResource(R.drawable.header_2);
					if(mContactInfoContainer.getVisibility()==View.INVISIBLE||mContactInfoContainer.getVisibility()==View.GONE){
						mContactInfoContainer.setVisibility(View.VISIBLE);
					}
					mEditPasswordContainer.setVisibility(View.GONE);*/
					// AsyncTask to call the logout webservice to end the login session
					new LogoutSessionTask(mContext,"FromManualLogOut").execute();

				}else if(msg.equalsIgnoreCase("Security question updated")){
					mMenuBarContactInfo.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mMenuBarEditPassword.setBackgroundResource(R.drawable.header_2);
					if(mContactInfoContainer.getVisibility()==View.INVISIBLE||mContactInfoContainer.getVisibility()==View.GONE){
						mContactInfoContainer.setVisibility(View.VISIBLE);
					}
					mEditPasswordContainer.setVisibility(View.GONE);

				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}