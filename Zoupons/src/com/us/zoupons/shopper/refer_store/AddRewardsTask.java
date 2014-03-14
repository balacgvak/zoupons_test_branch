package com.us.zoupons.shopper.refer_store;

/**
 * 
 * Asynctask to communicate with web server to refer a store
 * 
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

public class AddRewardsTask extends AsyncTask<String, String, String>{
	
	private Activity mContext;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponswebservice = null;
 	private ZouponsParsingClass mParsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	private String  mGetResponse="",mParseResponse="";
 	
	public AddRewardsTask(Activity context) {
		this.mContext = context;			
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
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
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){
				mGetResponse = mZouponswebservice.mAddReward(params[0],params[1],params[2],params[3],params[4],
						params[5],params[6],params[7],params[8],params[9],params[10],params[11],params[12]);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParseResponse= mParsingclass.mParseAddRewardResponse(mGetResponse);
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
		   if(MainMenuActivity.mAddRewardMessage.equalsIgnoreCase("Successfully Added")){
			   alertBox_service("Information", "Store Information is successfully sent to Zoupons.");
		   }else{ // Failure Message
			   alertBox_service("Information",MainMenuActivity.mAddRewardMessage);
		   }
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
				if(msg.equalsIgnoreCase("Store Information is successfully sent to Zoupons.")){
					Intent intent_rewards = new Intent(mContext,ReferStore.class);
					mContext.startActivity(intent_rewards);
					mContext.finish();
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
	
}
