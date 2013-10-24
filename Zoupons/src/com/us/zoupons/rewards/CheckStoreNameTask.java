package com.us.zoupons.rewards;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class CheckStoreNameTask extends AsyncTask<String, String,String>{

	String TAG=CheckStoreNameTask.class.getSimpleName();
	Context ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
 	private ZouponsParsingClass parsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	private EditText mStoreName;
 	private TextView mRightFooter,mZouponsTermsAndConditions;
 	String  mGetResponse="",mParseResponse="",FLAGACT="";
	public CheckStoreNameTask(Context context,TextView termsandcondtions) {
		this.ctx = context;	
		this.mZouponsTermsAndConditions=termsandcondtions;
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	public CheckStoreNameTask(Context context,EditText storeName, TextView mRightFooterView) {
		this.ctx = context;			
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		mStoreName = storeName;
		mRightFooter = mRightFooterView;
	}

	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		FLAGACT=params[0];
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){
				if(FLAGACT.equalsIgnoreCase("CheckStore")){
					mGetResponse = zouponswebservice.mCheckStoreName(params[1]);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= parsingclass.mParseCheckStoreName(mGetResponse);
						if(mParseResponse.equalsIgnoreCase("success")){						
							result ="success";				
						}else if(mParseResponse.equalsIgnoreCase("failure")){
							Log.i(TAG,"Error");
							result="Response Error.";
						}else if(mParseResponse.equalsIgnoreCase("norecords")){
							Log.i(TAG,"No Records");
							result="No Records";
						}
					}else{
						result="Response Error.";
					}
				}else if(FLAGACT.equalsIgnoreCase("TermsConditions")){
					mGetResponse = zouponswebservice.mGetTermsConditions();	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= parsingclass.mParseTermsCondition(mGetResponse);
						if(mParseResponse.equalsIgnoreCase("success")){						
							result ="success";				
						}else if(mParseResponse.equalsIgnoreCase("failure")){
							Log.i(TAG,"Error");
							result="Response Error.";
						}else if(mParseResponse.equalsIgnoreCase("norecords")){
							Log.i(TAG,"No Records");
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
			Log.i(TAG,"Thread Error");
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressdialog.dismiss();		
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
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
					Log.i(TAG, "New Store name");
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
            	   Log.i(TAG, "No Message from Terms and Conditions");
               }
			}

		}
	}
	
	private void alertBox_service(String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
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
