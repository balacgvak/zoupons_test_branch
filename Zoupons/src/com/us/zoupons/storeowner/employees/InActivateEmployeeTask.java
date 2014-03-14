package com.us.zoupons.storeowner.employees;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Window;

import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task to communicate with server to inactivate employee.
 *
 */

public class InActivateEmployeeTask extends AsyncTask<String,String, String>{

	private Context context;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String mEmployeeId="";
	
	public InActivateEmployeeTask(Context context,String employeeId) {
		this.context = context;
		this.mEmployeeId = employeeId;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.context);
		progressdialog=new ProgressDialog(this.context);
		progressdialog.setCancelable(true);
	}
		
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) context).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(context,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String mresult="";
		try{
			String mResponse = zouponswebservice.inactivateEmployee(mEmployeeId); 
            if(!mResponse.equalsIgnoreCase("noresponse") && !mResponse.equalsIgnoreCase("failure")){
				String mParsingResponse = parsingclass.parseInactivateEmployee(mResponse);
				if(!mParsingResponse.equals("failure") && !mParsingResponse.equals("no records")){
					mresult = mParsingResponse;
				}else if(mParsingResponse.equalsIgnoreCase("failure")){
					mresult = "failure";
				}else if(mParsingResponse.equalsIgnoreCase("no records")){
					mresult="norecords";
				}
			}else {
				mresult="Response Error.";
			}
		}catch(Exception e){
			mresult = "failure";
		}
		return mresult;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try{
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			if(result.equalsIgnoreCase("norecords")){
				alertBox_service("Information", "No data available");
			}else if(result.equalsIgnoreCase("Response Error.")){
				alertBox_service("Information", "No data available");
			}else if(result.equalsIgnoreCase("no data")){
				alertBox_service("Information", "Unable to reach service.");
			}else{ // success
				alertBox_service("Information", result);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// To show alert box with respective message
	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.context);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
                if(msg.equalsIgnoreCase("Successfully Inactivated")){
                	Intent intent_employee = new Intent(context,StoreOwner_Employees.class);
                	context.startActivity(intent_employee);
                }
			}
		});
		service_alert.show();
	}

}
