package com.us.zoupons.mobilepay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.flagclasses.AccountLoginFlag;
import com.us.zoupons.shopper.friends.Friends;
import com.us.zoupons.shopper.giftcards_deals.MyGiftCards;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.receipts.Receipts;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous thread to call webservice for giftcard/deal card purchase
 *
 */

public class PurchaseGiftCardAsyncTask extends AsyncTask<String, String, String>{

	private Context mContext;
	private String TAG="CheckFavoriteAsynchThread";
	private ProgressDialog mProgressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse;
	private String mCreditcardId="",mGiftcardId="",mCardvalue="",mFacebookId="",mFriendEmailAddress="",mFriendNotes="",mDate="",mTimeZone="",mIsZouponsfriend="";
	private String  mStoreID="",mStoreLocationID="";
	private String mInvoiceNote="";
	private String mCardType="",mFaceValue="";

	// For self purchase giftcard
	public PurchaseGiftCardAsyncTask(Context context,String creditcardId,String giftcardId,String cardvalue,String invoicenote, String storeid, String cardtype,String storelocationid,String facevalue){
		this.mContext=context;
		this.mCreditcardId = creditcardId;
		this.mGiftcardId = giftcardId;
		this.mCardvalue = cardvalue;
		this.mInvoiceNote = invoicenote;
		this.mStoreID = storeid;
		this.mCardType = cardtype;
		this.mStoreLocationID =  storelocationid;
		this.mFaceValue = facevalue;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}

	// For send to friend giftcard
	public PurchaseGiftCardAsyncTask(Context context,String creditcardId,String giftcardId,String cardvalue,String facebookId,String friend_emailaddress,String friendNotes,String date,String timezone,String isZouponsfriend, String invoicenote, String storeid, String cardtype,String storelocationid,String facevalue){
		this.mContext=context;
		this.mCreditcardId = creditcardId;
		this.mGiftcardId = giftcardId;
		this.mCardvalue = cardvalue;
		this.mFacebookId = facebookId;
		this.mFriendEmailAddress = friend_emailaddress;
		this.mFriendNotes = friendNotes;
		this.mDate = date;
		this.mTimeZone = timezone;
		this.mIsZouponsfriend = isZouponsfriend;
		this.mInvoiceNote = invoicenote;
		this.mStoreID = storeid;
		this.mStoreLocationID =  storelocationid;
		this.mCardType = cardtype;
		this.mFaceValue = facevalue;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}


	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		
	}
	@Override
	protected String doInBackground(String... params) { // params[0] --> entered user pin
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				PaymentStatusVariables.purchase_message = "";
				PaymentStatusVariables.message = "";
				mGetResponse = mZouponswebservice.checkUserPIN(params[0],UserDetails.mServiceUserId,AccountLoginFlag.accountUserTypeflag);
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){ 
					mParsingResponse = mParsingclass.parseCheckPIN(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){ // If secret PIN is Correct
						if(CheckPINClassVariables.message.equalsIgnoreCase("Success")){ 
							mGetResponse=mZouponswebservice.purchaseGiftcard(UserDetails.mServiceUserId,AccountLoginFlag.accountUserTypeflag,mCreditcardId,mGiftcardId,mCardvalue,mFacebookId,mFriendEmailAddress,mFriendNotes,mDate,mTimeZone,mIsZouponsfriend,mInvoiceNote,mStoreID,mCardType,mStoreLocationID,mFaceValue);	

							if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
								mParsingResponse=mParsingclass.parsePurcahseGiftcardPaymentDetails(mGetResponse);
								if(mParsingResponse.equalsIgnoreCase("success")){
									result="success";
								}else if(mParsingResponse.equalsIgnoreCase("failure")){
									Log.i(TAG,"Error");
									result="Response Error.";
								}
							}else{
								result="Response Error.";
							}			
						}else{
							result = "Invalid PIN";
						}
					}else{
						result = "Response Error.";
					}
				}else{
					result="Response Error.";
				}

			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			Log.i(TAG,"Thread Error");
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
		}else if(result.equals("Invalid PIN")){
			alertBox_service("Information", "Entered PIN is incorrect, Please enter correct PIN to proceed the payment");
		}else {
			if(PaymentStatusVariables.message.equalsIgnoreCase("Payment Completed")){
				alertBox_service("Information", PaymentStatusVariables.purchase_message);
			}else{
				alertBox_service("Information", PaymentStatusVariables.message);
			}
		}
		mProgressdialog.dismiss();
		
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
				if(PaymentStatusVariables.message.equalsIgnoreCase("Payment Completed")){
					if(PaymentStatusVariables.purchase_message.equalsIgnoreCase("Giftcard Added to your Wallet")||PaymentStatusVariables.purchase_message.equalsIgnoreCase("Dealcard Added to your Wallet")){
						mContext.startActivity(new Intent(mContext,MyGiftCards.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
					}else if(PaymentStatusVariables.purchase_message.equalsIgnoreCase("Giftcard has been sent to your friend")||PaymentStatusVariables.purchase_message.equalsIgnoreCase("Dealcard has been sent to your friend") || PaymentStatusVariables.purchase_message.equalsIgnoreCase("Giftcard will be send to your friend on specified date") || PaymentStatusVariables.purchase_message.equalsIgnoreCase("Dealcard will be send to your friend on specified date")){
						mContext.startActivity(new Intent(mContext,ShopperHomePage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
					}else{
						mContext.startActivity(new Intent(mContext,Receipts.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
					}
					// Flush friend information after sending to friend
					Friends.friendName=""; 
					Friends.friendId="";
					Friends.isZouponsFriend="";
					Friends.friendEmailId ="";
				}
			}
		});
		service_alert.show();
	}

}
