package com.us.zoupons.shopper.refer_store;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * Asyntask to communicate with web server to check whether refer store name already exists in Zoupons 
 * 
 */

public class CheckStoreNameTask extends AsyncTask<String, String,String>{

	private Context mContext;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponswebservice = null;
 	private ZouponsParsingClass mParsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	private EditText mStoreName;
 	private TextView mRightFooter,mZouponsTermsAndConditions;
 	private String  mGetResponse="",mParseResponse="",FLAGACT="";
 	
	public CheckStoreNameTask(Context context,TextView termsandcondtions) {
		this.mContext = context;	
		this.mZouponsTermsAndConditions=termsandcondtions;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	
	public CheckStoreNameTask(Context context,EditText storeName, TextView mRightFooterView) {
		this.mContext = context;			
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		mStoreName = storeName;
		mRightFooter = mRightFooterView;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		FLAGACT=params[0];
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){
				if(FLAGACT.equalsIgnoreCase("CheckStore")){ // Check availability of store name
					mGetResponse = mZouponswebservice.mCheckStoreName(params[1]);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= mParsingclass.mParseCheckStoreName(mGetResponse);
						if(mParseResponse.equalsIgnoreCase("success")){						
							result ="success";				
						}else if(mParseResponse.equalsIgnoreCase("failure")){
							result="Response Error.";
						}else if(mParseResponse.equalsIgnoreCase("norecords")){
							result="No Records";
						}
					}else{
						result="Response Error.";
					}
				}else if(FLAGACT.equalsIgnoreCase("TermsConditions")){ 
					mGetResponse = mZouponswebservice.mGetTermsConditions();	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= mParsingclass.mParseTermsCondition(mGetResponse);
						if(mParseResponse.equalsIgnoreCase("success")){						
							result ="success";				
						}else if(mParseResponse.equalsIgnoreCase("failure")){
							result="Response Error.";
						}else if(mParseResponse.equalsIgnoreCase("norecords")){
							result="No Records";
						}
					}else{
						result="Response Error.";
					}
				}
			}else{
				result="nonetwork";
			}
		}catch (Exception e) {
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mProgressdialog.dismiss();		
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			if(FLAGACT.equalsIgnoreCase("CheckStore")){
				if(MainMenuActivity.mCheckStoreMessage.equalsIgnoreCase("Yes")){
					mStoreName.getText().clear();
					mStoreName.requestFocus();
					mRightFooter.setEnabled(false);
					mRightFooter.setBackgroundColor(Color.parseColor("#81BEF7"));
					alertBox_service("Information", "This Store is already under Zoupons.");
					//alertBox_service("Information", "The store you are referring is already a Zoupons member.  Please refer a non-member store");
				}else if(MainMenuActivity.mCheckStoreMessage.equalsIgnoreCase("No")){
					mRightFooter.setEnabled(true);
					mRightFooter.setBackgroundDrawable(null);
				}else if(MainMenuActivity.mCheckStoreMessage.equalsIgnoreCase("Invalid Store Name")){
					mStoreName.getText().clear();
					mStoreName.requestFocus();
					mRightFooter.setEnabled(false);
					mRightFooter.setBackgroundColor(Color.parseColor("#81BEF7"));
					alertBox_service("Information", "Invalid Store Name");
				}
			}else if(FLAGACT.equalsIgnoreCase("TermsConditions")){
               if(MainMenuActivity.mTermsConditionsMessage.length()>2){
            	   mZouponsTermsAndConditions.setText(MainMenuActivity.mTermsConditionsMessage);
               }else{
               }
			}

		}
	}
	
	/* To show alert pop up with respective message */
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
