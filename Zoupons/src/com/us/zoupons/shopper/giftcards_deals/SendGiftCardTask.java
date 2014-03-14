package com.us.zoupons.shopper.giftcards_deals;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynctask to send giftcard to friend
 *
 */

public class SendGiftCardTask extends AsyncTask<String, String,String>{

	private Activity mContext;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponsWebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	private String  mGetResponse="",mParseResponse="";
	private String mTimeZone="";
	
	public SendGiftCardTask(Activity context,String timezone) {
		this.mContext = context;
		this.mTimeZone = timezone;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponsWebservice= new ZouponsWebService(context);
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
		String result ="";		
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){					
				mGetResponse = mZouponsWebservice.mSendGiftCard(UserDetails.mServiceUserId,
						params[0],params[1],params[2],params[3],params[4],mTimeZone,params[5]);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParseResponse= mParsingclass.mParseSendGiftCardResponse(mGetResponse);
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
		if(mProgressdialog != null && mProgressdialog.isShowing()){
			mProgressdialog.dismiss();
		}		
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			if(ZouponsParsingClass.SEND_GIFT_CARD_MESSAGE.equalsIgnoreCase("Giftcard has been sent to your friend")){
				alertBox_service("Information", "The information is sent successfully. And the Giftcard will reach to your friend on the specified date.");
			}else{
				alertBox_service("Information", "Gift Card Failed Added to Send Queue.");
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
				if(msg.equalsIgnoreCase("The information is sent successfully. And the Giftcard will reach to your friend on the specified date.")){
					Intent intent_zgiftcards = new Intent(mContext,MyGiftCards.class);
					mContext.startActivity(intent_zgiftcards);
					mContext.finish();
				}
				dialog.dismiss();

			}
		});
		service_alert.show();
	}
}