package com.us.zoupons.storeowner.customercenter;

import java.util.ArrayList;

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

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

public class GetCustomerListTask extends AsyncTask<String, String, ArrayList<Object>>{
	CustomerCenter mContext;
	NetworkCheck mConnectionAvailabilityChecking=null;
	StoreownerWebserivce zouponswebservice=null;
	StoreownerParsingclass parsingclass=null;
	ProgressDialog progressdialog=null;
	String mProgressStatus,mEventFlag;
	ListView mListView;
	private String TAG="StoreOwner_CustomCenterAsyncTask";
	
	public GetCustomerListTask(CustomerCenter context,String progressStatus,ListView listview,String eventFlag) {
		this.mContext = context;
		this.mEventFlag = eventFlag;
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
	protected ArrayList<Object> doInBackground(String... params) {
		try{
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String mLocationId = mPrefs.getString("location_id", "");
			String mUsedId = mPrefs.getString("user_id", "");
			String mResponse = "";
			if(mEventFlag.equalsIgnoreCase("customer_list")){ // Customer list
				mResponse=zouponswebservice.getRecentCommunicatedUsers(mLocationId);	
			}else{ // add customer
				mResponse=zouponswebservice.addStoreCustomer(mUsedId,mLocationId,params[0],params[1],params[2]);
			}
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					if(mEventFlag.equalsIgnoreCase("customer_list")){ // Customer list
						return parsingclass.parseFavoriteCustomerDetails(mResponse);	
					}else{
						return parsingclass.parseAddFavouriteUser(mResponse);
					}
				}else{ // service issues
					return null;
				}
			}else { // failure
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
	
	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		super.onPostExecute(result);
		try{
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			Log.i("Task", "onPOstExecute");		
			if(result!=null && result.size() >0){
				mContext.updateViews(result,mEventFlag);
			}else if(result!=null && result.size() == 0){
				alertBox_service("Information", "No data available");
			}else{
				alertBox_service("Information", "Unable to reach service.");
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
