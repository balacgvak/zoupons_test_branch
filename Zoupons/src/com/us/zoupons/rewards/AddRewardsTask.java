package com.us.zoupons.rewards;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class AddRewardsTask extends AsyncTask<String, String, String>{
	String TAG=AddRewardsTask.class.getSimpleName();
	Activity ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
 	private ZouponsParsingClass parsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	String  mGetResponse="",mParseResponse="";
	public AddRewardsTask(Activity context) {
		this.ctx = context;			
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
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
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){
				mGetResponse = zouponswebservice.mAddReward(params[0],params[1],params[2],params[3],params[4],
						params[5],params[6],params[7],params[8],params[9],params[10],params[11],params[12]);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParseResponse= parsingclass.mParseAddRewardResponse(mGetResponse);
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
		   if(MainMenuActivity.mAddRewardMessage.equalsIgnoreCase("Successfully Added")){
			   alertBox_service("Information", "Store Information is successfully sent to Zoupons.");
		   }else{
			   alertBox_service("Information",MainMenuActivity.mAddRewardMessage);
		   }
		}
	}

	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {
	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equalsIgnoreCase("Store Information is successfully sent to Zoupons.")){
					Intent intent_rewards = new Intent(ctx,Rewards.class);
					ctx.startActivity(intent_rewards);
					ctx.finish();
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
	
}
