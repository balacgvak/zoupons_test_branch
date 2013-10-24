package com.us.zoupons.zpay;

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
import com.us.zoupons.SlidingView;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.FlagClasses.AccountLoginFlag;
import com.us.zoupons.GiftCards.GiftCards;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.friends.Friends;
import com.us.zoupons.receipts.Receipts;

public class PurchaseGiftCardAsyncTask extends AsyncTask<String, String, String>{
	
	Context ctx;
	String TAG="CheckFavoriteAsynchThread";
	private ProgressDialog progressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	String mPageFlag;	//flag to differentiate homepage and mainmenu page call
	String mCreditcardId="",mGiftcardId="",mCardvalue="",mFacebookId="",mFriendNotes="",mDate="",mTimeZone="",mIsZouponsfriend="";
	String mInvoiceId="",mInvoiceRaisedBy="",mInvoiceRaisedByType="",mStoreID="",mInvoiceAmount="",mStoreLocationID="";

	String mInvoiceNote="";
	String mCardType="",mFaceValue="";
	
	// For self purchase giftcard
	public PurchaseGiftCardAsyncTask(Context context,String creditcardId,String giftcardId,String cardvalue,String invoicenote, String storeid, String cardtype,String storelocationid,String facevalue){
		this.ctx=context;
		this.mCreditcardId = creditcardId;
		this.mGiftcardId = giftcardId;
		this.mCardvalue = cardvalue;
		this.mInvoiceNote = invoicenote;
		this.mStoreID = storeid;
		this.mCardType = cardtype;
		this.mStoreLocationID =  storelocationid;
		this.mFaceValue = facevalue;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	// For send to friend giftcard
	public PurchaseGiftCardAsyncTask(Context context,String creditcardId,String giftcardId,String cardvalue,String facebookId,String friendNotes,String date,String timezone,String isZouponsfriend, String invoicenote, String storeid, String cardtype,String storelocationid,String facevalue){
		this.ctx=context;
		this.mCreditcardId = creditcardId;
		this.mGiftcardId = giftcardId;
		this.mCardvalue = cardvalue;
		this.mFacebookId = facebookId;
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
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
	
	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);

		//Start a status dialog
		progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}
	@Override
	protected String doInBackground(String... params) { // params[0] --> entered user pin
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				mGetResponse = zouponswebservice.checkUserPIN(params[0],UserDetails.mServiceUserId,AccountLoginFlag.accountUserTypeflag);
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse = parsingclass.parseCheckPIN(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						if(CheckPINClassVariables.message.equalsIgnoreCase("Success")){
							mGetResponse=zouponswebservice.purchaseGiftcard(UserDetails.mServiceUserId,AccountLoginFlag.accountUserTypeflag,mCreditcardId,mGiftcardId,mCardvalue,mFacebookId,mFriendNotes,mDate,mTimeZone,mIsZouponsfriend,mInvoiceNote,mStoreID,mCardType,mStoreLocationID,mFaceValue);	
							
							if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
								mParsingResponse=parsingclass.parsePurcahseGiftcardPaymentDetails(mGetResponse);
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
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
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
		progressdialog.dismiss();
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			   if(PaymentStatusVariables.message.equalsIgnoreCase("Payment Completed")){
					if(PaymentStatusVariables.purchase_message.equalsIgnoreCase("Giftcard Added to your Wallet")||PaymentStatusVariables.purchase_message.equalsIgnoreCase("Dealcard Added to your Wallet")){
						ctx.startActivity(new Intent(ctx,GiftCards.class));
					}else if(mFacebookId!=null && !mFacebookId.equals("")){
						ctx.startActivity(new Intent(ctx,SlidingView.class));
					}else{
						ctx.startActivity(new Intent(ctx,Receipts.class));
					}
				   Friends.friendName=""; 
				   Friends.friendId="";
				   Friends.isZouponsFriend="";
			   }
			}
		});
		service_alert.show();
	}
	
}
