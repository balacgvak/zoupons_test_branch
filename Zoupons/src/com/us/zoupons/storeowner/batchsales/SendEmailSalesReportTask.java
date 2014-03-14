package com.us.zoupons.storeowner.batchsales;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;



public class SendEmailSalesReportTask extends AsyncTask<String,String, String>{

	private Context mContext;
	private StoreownerWebserivce mZouponswebservice=null;
	private StoreownerParsingclass mParsingclass=null;
	private ProgressDialog mProgressDialog=null;
	private String mEventFlag="";
	private String mSelectedFromDate="",mSelectedToDate="";
	
	public SendEmailSalesReportTask(Context context,String eventFlag,String selectedFromDate, String selectedToDate) {
		this.mContext = context;
		this.mEventFlag = eventFlag;
		this.mSelectedFromDate = selectedFromDate;
		this.mSelectedToDate = selectedToDate;
		mZouponswebservice= new StoreownerWebserivce(context);
		mParsingclass= new StoreownerParsingclass(this.mContext);
		mProgressDialog=new ProgressDialog(this.mContext);
		mProgressDialog.setCancelable(false);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//Start a status dialog
		mProgressDialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected String doInBackground(String... params) {
		try{
			// To get store details from preference
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String mLocationId = mPrefs.getString("location_id", "");
			String mUserId = mPrefs.getString("user_id", "");
			String mResponse=mZouponswebservice.getBatchsales(mLocationId,mEventFlag,mSelectedFromDate,mSelectedToDate,mUserId);
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					return mParsingclass.parseSendEmailSalesStatus(mResponse);
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
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
		if(result != null && result.equalsIgnoreCase("success")){
			alertBox_service("Information", "Report sent successfully");
		}else{
			alertBox_service("Information", "Unable to send report,please try after some time");
		}
	}
	
	/* To show alert pop up with respective message */
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
