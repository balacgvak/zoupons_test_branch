package com.us.zoupons.storeowner.employees;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task used to fetch employee list from server
 *
 */


public class StoreOwner_EmployeesAsyncTask extends AsyncTask<String, String, String>{
	private Context mContext;
	private NetworkCheck mConnectionAvailabilityChecking=null;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String mProgressStatus;
	private String TAG="StoreOwner_EmployeesAsyncTask";
	private String mCheckRefresh="",mStoreId="";
	
	public StoreOwner_EmployeesAsyncTask(Context context,String progressStatus,ListView listview) {
		this.mContext = context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		this.mProgressStatus = progressStatus;
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
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			//Start a status dialog
			progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		}
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		mCheckRefresh = params[0];
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
				if(StoreOwner_Employees.sEmployeeStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.StoreEmloyeesList.clear();
				}
				// to get location id from preference
				SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				mStoreId = mPrefs.getString("store_id", "");
				String mEmployeeListResponse = zouponswebservice.getStoreEmployees(mStoreId, StoreOwner_Employees.sEmployeeStart);	
				if(!mEmployeeListResponse.equals("failure") && !mEmployeeListResponse.equals("noresponse")){
					String mParseCouponResponse = parsingclass.parseStoreEmployees(mEmployeeListResponse);
					if(mParseCouponResponse.equalsIgnoreCase("success")){			
						result ="success";				
					}else if(mParseCouponResponse.equalsIgnoreCase("failure")){
						result="Response Error.";
					}else if(mParseCouponResponse.equalsIgnoreCase("norecords")){
						result="No Records";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			e.printStackTrace();
			Log.i(TAG,"Thread Error");
			result="Thread Error";
		}
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try{
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			if(result.equals("nonetwork")){
				Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equals("Response Error.")){
				alertBox_service("Information", "Unable to reach service.");
			}else if(result.equals("No Records")){
				alertBox_service("Information", "Store Employees not available.");
			}else if(result.equals("Thread Error")){
				alertBox_service("Information", "Unable to process.");
			}else{
				Log.i(TAG,"Success: ");
				//Set List Adapter Here
				((StoreOwner_Employees) mContext).SetStoreOwner_EmployeesListAdatpter(mCheckRefresh);
				StoreOwner_Employees.sEmployeeStart = StoreOwner_Employees.sEmployeeEndLimit;
				StoreOwner_Employees.sEmployeeEndLimit = String.valueOf(Integer.parseInt(StoreOwner_Employees.sEmployeeEndLimit)+20);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
		
	// To show alert box with specified message
	private void alertBox_service(String title, String msg) {
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
