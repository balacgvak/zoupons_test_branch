package com.us.zoupons.storeowner.customercenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

public class SendEmailTask extends AsyncTask<String, String, String>{

	private Activity mContext;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String TAG="StoreOwnerSendEmailTask",mUserID="",mSubject="",mEmailBody="";
	public NetworkCheck mConnectionAvailabilityChecking;
	
		
	// For Sending email
	public SendEmailTask(Activity context,String user_id,String subject,String email_body) {
		this.mContext = context;
		this.mUserID = user_id;
		this.mSubject = subject;
		this.mEmailBody = email_body;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String mresult="";
	    try{
	    	if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
	    		SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				String mLocationId = mPrefs.getString("location_id", "");
	    		String mResponse=zouponswebservice.sendEmail(mUserID,mLocationId,mSubject,mEmailBody);
	    		if(!mResponse.equalsIgnoreCase("noresponse") && !mResponse.equalsIgnoreCase("failure")){
	    			String mParsingResponse = parsingclass.parseSendMailResponse(mResponse);
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
	    	}else{
	    		mresult="nonetwork";
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
			
			if(result.equalsIgnoreCase("failure")){
				alertBox_service("Information", "Unable to reach service.");
			}else if(result.equalsIgnoreCase("norecords")){
				alertBox_service("Information", "No data available");
			}else if(result.equalsIgnoreCase("Response Error.")){
				alertBox_service("Information", "No data available");
			}else if(result.equalsIgnoreCase("no data")){
				alertBox_service("Information", "Unable to reach service.");
			}else if(result.equalsIgnoreCase("nonetwork")){
				Toast.makeText(this.mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else{
				alertBox_service("Information", result);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	
    }

	@Override
	protected void onPreExecute() {
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading","Please Wait!",true);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
                if(msg.equalsIgnoreCase("Sent Mail to Customer")){
                	mContext.finish();
                }
                }
		});
		service_alert.show();
	}
}