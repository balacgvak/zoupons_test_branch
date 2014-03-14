package com.us.zoupons.storeowner.giftcards_deals;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.mobilepay.PaymentStatusVariables;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynctask to communicate with server to sell giftcard to non zoupons customer
 *
 */

public class NonZouponsCustomerGiftcardSellTask extends AsyncTask<String, String, String>{

	private Activity mContext;
	private ProgressDialog mProgressDialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse=null,mParsingResponse,mCustomerId="",mCreditcardId="",mGiftcardId="",mCardvalue="",mStoreID="",mStoreLocationID="",mCardType="",mFaceValue="";

	// For self purchase giftcard
	public NonZouponsCustomerGiftcardSellTask(Activity context,String customer_id,String creditcardId,String giftcardId,String cardvalue,String storeid, String cardtype,String storelocationid,String facevalue){
		this.mContext=context;
		this.mCustomerId= customer_id;
		this.mCreditcardId = creditcardId;
		this.mGiftcardId = giftcardId;
		this.mCardvalue = cardvalue;
		this.mStoreID = storeid;
		this.mCardType = cardtype;
		this.mStoreLocationID =  storelocationid;
		this.mFaceValue = facevalue;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressDialog=new ProgressDialog(context);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressDialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected String doInBackground(String... params) { 
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				PaymentStatusVariables.purchase_message = "";
				PaymentStatusVariables.message = "";
				mGetResponse=mZouponswebservice.purchaseGiftcard(mCustomerId,"Shopper",mCreditcardId,mGiftcardId,mCardvalue,"","","","","","","",mStoreID,mCardType,mStoreLocationID,mFaceValue);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=mParsingclass.parsePurcahseGiftcardPaymentDetails(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						result="success";
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						result="Response Error.";
					}
				}else{
					result = "Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else{
			if(PaymentStatusVariables.message.equalsIgnoreCase("Payment Completed")){
				if(PaymentStatusVariables.purchase_message.equalsIgnoreCase("Giftcard added to your wallet")){
					alertBox_service("Information", "Thank you for using zoupons.Your wallet is now securely closed and giftcard added to your wallet");	
				}else{
					alertBox_service("Information", "Thank you for using zoupons.Your wallet is now securely closed and Dealcard added to your wallet");
				}
			}else{
				alertBox_service("Information", PaymentStatusVariables.message);
			}
		}
		mProgressDialog.dismiss();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	/* To show alert pop up with respective message */
	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equalsIgnoreCase("Thank you for using zoupons.Your wallet is now securely closed and giftcard added to your wallet")){
	            	  Intent intent_rightmenugiftcards = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerGiftCards.class);
	            	  intent_rightmenugiftcards.putExtra("classname", "StoreOwnerGiftCards");  
					  mContext.startActivity(intent_rightmenugiftcards);
					  mContext.finish();
	              }else if(msg.equalsIgnoreCase("Thank you for using zoupons.Your wallet is now securely closed and Dealcard added to your wallet")){
	            	  Intent intent_rightmenugiftcards = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerGiftCards.class);
	           		  intent_rightmenugiftcards.putExtra("classname", "StoreOwnerDealCards");
					  mContext.startActivity(intent_rightmenugiftcards);
					  mContext.finish();
	              }
			}
		});
		service_alert.show();
	}

}
