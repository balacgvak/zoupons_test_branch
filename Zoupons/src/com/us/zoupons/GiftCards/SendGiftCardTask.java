package com.us.zoupons.GiftCards;

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
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class SendGiftCardTask extends AsyncTask<String, String,String>{

	String TAG=GetAllMyGiftCardTask.class.getSimpleName();
	Activity ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	String  mGetResponse="",mParseResponse="";
	String mTimeZone="";
	
	public SendGiftCardTask(Activity context,String timezone) {
		this.ctx = context;
		this.mTimeZone = timezone;
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
		String result ="";		
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){					
				mGetResponse = zouponswebservice.mSendGiftCard(UserDetails.mServiceUserId,
						params[0],params[1],params[2],params[3],params[4],mTimeZone);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParseResponse= parsingclass.mParseSendGiftCardResponse(mGetResponse);
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
			Log.i(TAG,"Thread Error");
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}		
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			Log.i(TAG, "Success");
			if(ZouponsParsingClass.SEND_GIFT_CARD_MESSAGE.equalsIgnoreCase("Giftcard has been sent to your friend")){
				alertBox_service("Information", "The information is sent successfully. And the Giftcard will reach to your friend on the specified date.");
			}else{
				alertBox_service("Information", "Gift Card Failed Added to Send Queue.");
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
				if(msg.equalsIgnoreCase("The information is sent successfully. And the Giftcard will reach to your friend on the specified date.")){
					Intent intent_zgiftcards = new Intent(ctx,GiftCards.class);
					ctx.startActivity(intent_zgiftcards);
					ctx.finish();
				}
				dialog.dismiss();

			}
		});
		service_alert.show();
	}
}