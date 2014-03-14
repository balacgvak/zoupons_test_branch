package com.us.zoupons.storeowner.employees;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import com.us.zoupons.classvariables.POJOUserProfile;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * AsyncTask to communicate with Server for verifying and adding employee
 *
 */

public class Add_EmployeeTask extends AsyncTask<String, String, ArrayList<Object>>{

	private StoreOwner_AddEmployee mContext;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String mEventFlag="",mMobileNumber="",mEmployeeCode="",mEmployee_id="",mChoosedModules="",mChoosedLocations="";
		
	// For Check for zoupons customer for adding employee
	public Add_EmployeeTask(StoreOwner_AddEmployee context,String EventFlag,String data){
		this.mContext = context;
		this.mEventFlag = EventFlag;
		this.mMobileNumber = data;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	// For verification of employee code
	public Add_EmployeeTask(StoreOwner_AddEmployee context,String EventFlag,String employee_code,String employee_id){
		this.mContext = context;
		this.mEventFlag = EventFlag;
		this.mEmployeeCode = employee_code;
		this.mEmployee_id = employee_id;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	// For Activating employee
	public Add_EmployeeTask(StoreOwner_AddEmployee context,String EventFlag, String employee_id, String mChoosedModules,String mChoosedLocations) {
		this.mContext = context;
		this.mEventFlag = EventFlag;
		this.mEmployee_id = employee_id;
		this.mChoosedLocations = mChoosedLocations;
		this.mChoosedModules = mChoosedModules;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	@Override
	protected void onPreExecute() {
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}
		
    @Override
	protected ArrayList<Object> doInBackground(String... params) {
		try{
			if(mEventFlag.equalsIgnoreCase("check_employee")){ // For check for employee
				String mResponse=zouponswebservice.mGetUserProfile(mMobileNumber,"Employee");
				if(!mResponse.equals("")){
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
						return parsingclass.parseUserProfile(mResponse,"");
					}else{ // service issues
						return null;
					}
				}else { // failure
					return null;
				}
			}else if(mEventFlag.equalsIgnoreCase("verify_employeecode")){ // For Verify employee code
				// to get store id from preference
				SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				String mStoreId = mPrefs.getString("store_id", "");
				String mResponse=zouponswebservice.verifyEmployeeCode(mEmployeeCode,mEmployee_id,mStoreId);
				Log.i("response", mResponse);
				if(!mResponse.equals("")){
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
						return parsingclass.parseVerifyEmployeeCode(mResponse);
					}else{ // service issues
						return null;
					}
				}else { // failure
					return null;
				}
				
			}else{ // For Adding employee details
				SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				String mStoreId = mPrefs.getString("store_id", "");
				String mUserId = mPrefs.getString("user_id", "");
				String mResponse=zouponswebservice.activateEmployee(mUserId, mEmployee_id, mStoreId, mChoosedLocations, mChoosedModules);
				Log.i("response", mResponse);
				if(!mResponse.equals("")){
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
						return parsingclass.parseActivateEmployee(mResponse);
					}else{ // service issues
						return null;
					}
				}else { // failure
					return null;
				}
			}
			
		}catch(Exception e){
			return null;
		}
	}

	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		super.onPostExecute(result);
		try{
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			Log.i("Task", "onPostExecute");		
			if(result!=null && result.size() >0){
				if(mEventFlag.equalsIgnoreCase("check_employee")){ // For check for employee
					POJOUserProfile mUserDetails = (POJOUserProfile) result.get(0);
					if(mUserDetails.mMessage.equalsIgnoreCase("")){ // User profile exists
						mContext.updateViews(result,mEventFlag);	
					}else{ // User profile Not exits
						alertBox_service("Information",mUserDetails.mMessage);	
					}
				}else if(mEventFlag.equalsIgnoreCase("verify_employeecode")){ // For Verify employee code
					EmployeePermissionClassVariables mAddEmployeeResult = (EmployeePermissionClassVariables) result.get(0);
					if(mAddEmployeeResult.mMessage.equalsIgnoreCase("")){ // Success
						mContext.updateViews(result,mEventFlag);
					}else{// Invalid employee code
						alertBox_service("Information", "Please enter valid employee code received in mail");
					}

				}else{ // For Add Employee
					String Message = (String) result.get(0);
					alertBox_service("Information", Message);
				}
			}else if(result!=null && result.size() == 0){
				alertBox_service("Information", "No data available");
			}else{
				alertBox_service("Information", "Unable to reach service.");
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}
	
	// Funtion to show alert pop up with respective message
	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equalsIgnoreCase("Successfully activated")){
					mContext.finish();
					Intent intent_employee = new Intent(mContext,StoreOwner_Employees.class);
					mContext.startActivity(intent_employee);
				}

			}
		});
		service_alert.show();
	}
}


