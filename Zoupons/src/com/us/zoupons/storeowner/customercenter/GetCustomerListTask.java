package com.us.zoupons.storeowner.customercenter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.ListView;

import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task to fetch favorite customer list from web server
 *
 */

public class GetCustomerListTask extends AsyncTask<String, String, ArrayList<Object>>{
	
	private CustomerCenter mContext;
 	private StoreownerWebserivce zouponswebservice=null;
 	private StoreownerParsingclass parsingclass=null;
 	private ProgressDialog progressdialog=null;
 	private String mProgressStatus,mEventFlag;
	
	public GetCustomerListTask(CustomerCenter context,String progressStatus,ListView listview,String eventFlag) {
		this.mContext = context;
		this.mEventFlag = eventFlag;
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
			if(result!=null && result.size() >0){ // Success
				mContext.updateViews(result,mEventFlag);
			}else if(result!=null && result.size() == 0){
				if(mEventFlag.equalsIgnoreCase("customer_list")){ // Customer list
					alertBox_service("Information", "Favorite customer list not available");
				}else{ // add customer
					alertBox_service("Information", "Unable to add user,please try after some time");
				}
			}else{
				alertBox_service("Information", "Unable to reach service.");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	// To show alert box with respective message
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
