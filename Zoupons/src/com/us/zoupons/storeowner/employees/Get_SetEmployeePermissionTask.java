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
import android.view.Window;

import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task to communicate with server to get/set employee details
 *
 */

public class Get_SetEmployeePermissionTask extends AsyncTask<String, String, ArrayList<Object>>{
	
	private StoreOwner_EmployeeDetails mContext;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String mEventFlag="",mEmployeeId="",mSelectedModules="",mSelectedLocations="";
		
	// To get employee permission details
	public Get_SetEmployeePermissionTask(StoreOwner_EmployeeDetails context,String eventFlag,String EmployeeId) {
		this.mContext = context;
		this.mEventFlag = eventFlag;
		this.mEmployeeId = EmployeeId;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	// To set employee permission details
	public Get_SetEmployeePermissionTask(StoreOwner_EmployeeDetails context,String eventFlag,String EmployeeId,String selectedModules,String selectedLocations) {
		this.mContext = context;
		this.mEventFlag = eventFlag;
		this.mEmployeeId = EmployeeId;
		this.mSelectedModules = selectedModules;
		this.mSelectedLocations = selectedLocations;
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
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected ArrayList<Object> doInBackground(String... params) {
		try{
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String mStoreId = mPrefs.getString("store_id", "");
			String mUserId = mPrefs.getString("user_id", "");
			String mResponse=zouponswebservice.get_setEmployeesPermission(mStoreId, mUserId, mEmployeeId, mEventFlag,mSelectedModules,mSelectedLocations);
			//Log.i("response", mResponse);
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					return parsingclass.parseEmployeePermisssions(mResponse);
				}else{ // service issues
					return null;
				}
			}else { // failure
				return null;
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		super.onPostExecute(result);
		try{
			if(progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			if(result!=null && result.size() >0){ // Success
				if(mEventFlag.equalsIgnoreCase("get")){  
					EmployeePermissionClassVariables mUserDetails = (EmployeePermissionClassVariables) result.get(0);
					if(mUserDetails.mMessage.equalsIgnoreCase("")){
						mContext.updateViews(result,mEventFlag);	
					}else{ 
						alertBox_service("Information",mUserDetails.mMessage);	
					}
				}else if(mEventFlag.equalsIgnoreCase("set")){ 
					EmployeePermissionClassVariables mAddEmployeeResult = (EmployeePermissionClassVariables) result.get(0);
					alertBox_service("Information", mAddEmployeeResult.mMessage);
				}
			}else if(result!=null && result.size() == 0){
				alertBox_service("Information", "No data available");
			}else{
				alertBox_service("Information", "Unable to reach service.");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// To show alert box with respective message
	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equalsIgnoreCase("Permission updated successfully")){
					mContext.finish();
					Intent intent_employee = new Intent(mContext,StoreOwner_Employees.class);
					mContext.startActivity(intent_employee);
				}

			}
		});
		service_alert.show();
	}
	
}
