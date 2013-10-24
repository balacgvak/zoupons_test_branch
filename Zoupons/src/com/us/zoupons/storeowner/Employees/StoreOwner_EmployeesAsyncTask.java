package com.us.zoupons.storeowner.Employees;

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
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

public class StoreOwner_EmployeesAsyncTask extends AsyncTask<String, String, String>{
	Context mContext;
	NetworkCheck mConnectionAvailabilityChecking=null;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	ProgressDialog progressdialog=null;
	String mProgressStatus;
	ListView mListView;
	private String TAG="StoreOwner_EmployeesAsyncTask";
	private String mCheckRefresh="",mStoreId="";
	
	public StoreOwner_EmployeesAsyncTask(Context context,String progressStatus,ListView listview) {
		this.mContext = context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		this.mProgressStatus = progressStatus;
		this.mListView=listview;
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		mCheckRefresh = params[0];
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
				if(StoreOwner_Employees.mEmployeeStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.StoreEmloyeesList.clear();
				}
				// to get location id from preference
				SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", mContext.MODE_PRIVATE);
				mStoreId = mPrefs.getString("store_id", "");
				String mEmployeeListResponse = zouponswebservice.getStoreEmployees(mStoreId, StoreOwner_Employees.mEmployeeStart);	
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
			Log.i("Task", "onPOstExecute");		

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

				StoreOwner_Employees.mEmployeeStart = StoreOwner_Employees.mEmployeeEndLimit;
				StoreOwner_Employees.mEmployeeEndLimit = String.valueOf(Integer.parseInt(StoreOwner_Employees.mEmployeeEndLimit)+20);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onPreExecute() {
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			//Start a status dialog
			progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		}
		super.onPreExecute();
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}
	
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
