package com.us.zoupons.storeowner.invoice;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Window;

import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

public class GetAllInvoiceListTask extends AsyncTask<String, String, ArrayList<Object>>{
	
	private InvoiceCenter mContext;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String mEventFlag="";
	private String mSelectedFromDate="",mSelectedToDate="";
	
		
	// To get all store location details
	public GetAllInvoiceListTask(InvoiceCenter context,String eventFlag, String SelectedFromDate, String SelectedToDate) {
		this.mContext = context;
		this.mEventFlag = eventFlag;
		this.mSelectedFromDate = SelectedFromDate;
		this.mSelectedToDate = SelectedToDate;
	    zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
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
			String mLocationId = mPrefs.getString("location_id", "");
			String mResponse=zouponswebservice.getAllInvoices(mLocationId,mEventFlag,mSelectedFromDate,mSelectedToDate);
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					return parsingclass.parseInvoiceList(mResponse);
				}else{ // service issues
					return null;
				}
			}else { // failure
				return null;
			}
		}catch (Exception e) {
			// TODO: hande exception
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		super.onPostExecute(result);
		if(progressdialog.isShowing()){
			progressdialog.dismiss();
		}
		if(result!=null && result.size() >0){
			mContext.setInvoiceListDetails(result,mEventFlag);
		}else if(result!=null && result.size() == 0){
			alertBox_service("Information", "No data available");
		}else{
			alertBox_service("Information", "Unable to reach service.");
		}
	}
	
	private void alertBox_service(String title,final String msg) {
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