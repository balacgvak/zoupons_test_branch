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
import com.us.zoupons.shopper.receipts.Receipts;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchrnous thread to call webservice for paying invoice using Credit card / Giftcard
 *
 */

public class InvoiceNormalPaymentAsyncTask extends AsyncTask<String, String, String>{
	
	private Context mContext;
	private String TAG="CheckFavoriteAsynchThread";
	private ProgressDialog mProgressdialog=null;
	private NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse;
	private String mCreditcardId="",mChoosedGiftCardPurchaseId="";
	private String mInvoiceId="",mInvoiceRaisedBy="",mInvoiceRaisedByType="",mStoreID="",mTipAmount="",mTotalAmount,mStoreLocationId="";
	private String mAmountOnGiftCard,mAmountOnCreditCard,mPaymentNote;
	
	// GiftCard Invoice Approval
	public InvoiceNormalPaymentAsyncTask(Context context, String InvoiceId, String TipAmount,String TotalAmount,String ChoosedGiftCardPurchaseID,String InvoiceRaisedBy,String InvoiceRaisedByType,String StoreID,String emptyvalue ,String storelocationid,String payment_note){
		this.mContext=context;
		this.mInvoiceId = InvoiceId;
		this.mInvoiceRaisedBy=InvoiceRaisedBy;
		this.mInvoiceRaisedByType=InvoiceRaisedByType;
		this.mStoreID=StoreID;
		this.mStoreLocationId = storelocationid;
		this.mChoosedGiftCardPurchaseId = ChoosedGiftCardPurchaseID;
		this.mTipAmount = TipAmount;
		this.mTotalAmount = TotalAmount;
		this.mPaymentNote = payment_note;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	
	//CreditCard Invoice Approval
	public InvoiceNormalPaymentAsyncTask(Context context,String InvoiceId, String InvoiceRaisedBy,String InvoiceRaisedByType, String SelectedCreditCardId,
			String StoreID, String TipAmount, String TotalAmount,String storelocationid,String payment_note) {
		this.mContext=context;
		this.mInvoiceId = InvoiceId;
		this.mInvoiceRaisedBy = InvoiceRaisedBy;
		this.mInvoiceRaisedByType = InvoiceRaisedByType;
		this.mCreditcardId = SelectedCreditCardId;
		this.mStoreID = StoreID;
		this.mStoreLocationId = storelocationid;
		this.mTipAmount = TipAmount;
		this.mTotalAmount = TotalAmount;
		this.mPaymentNote = payment_note;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}

	//Both Invoice Approval
	public InvoiceNormalPaymentAsyncTask(Context context,String InvoiceId, String InvoiceRaisedBy,String InvoiceRaisedByType,String SelectedCreditCardId,String AmountonCreditCard, String StoreID, /*String ChoosedGiftcardID,*/
			String AmountonGiftcard, String TipAmount,String ChoosedGiftCardPurchaseID,String TotalAmount,String storelocationid,String payment_note) {
		this.mContext=context;
		this.mInvoiceId = InvoiceId;
		this.mInvoiceRaisedBy = InvoiceRaisedBy;
		this.mInvoiceRaisedByType = InvoiceRaisedByType;
		this.mCreditcardId = SelectedCreditCardId;
		this.mStoreID = StoreID;
		this.mStoreLocationId = storelocationid;
		this.mTipAmount = TipAmount;
		this.mAmountOnCreditCard = AmountonCreditCard;
		this.mAmountOnGiftCard = AmountonGiftcard;
		this.mTotalAmount = TotalAmount;
		this.mPaymentNote = payment_note;
		this.mChoosedGiftCardPurchaseId = ChoosedGiftCardPurchaseID;
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
	protected String doInBackground(String... params) { // params[0] --> entered user pin // params[1] --> payment type 
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				PaymentStatusVariables.purchase_message = "";
				PaymentStatusVariables.message = "";
				mGetResponse = mZouponswebservice.checkUserPIN(params[0],UserDetails.mServiceUserId,AccountLoginFlag.accountUserTypeflag);
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse = mParsingclass.parseCheckPIN(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						if(CheckPINClassVariables.message.equalsIgnoreCase("Success")){
							if(params[1].equalsIgnoreCase("creditcard")){ // payment from invoice by using only Credit Card
								mGetResponse=mZouponswebservice.payInvoiceUsingCreditCard(mInvoiceId,mInvoiceRaisedBy,mInvoiceRaisedByType,mCreditcardId,mStoreID,mTipAmount,mTotalAmount,mStoreLocationId,mPaymentNote);	
							}else if(params[1].equalsIgnoreCase("giftcard")){  // payment from invoice by using only giftcard
								mGetResponse=mZouponswebservice.payInvoiceUsingGiftcard(mInvoiceId,mTipAmount,mTotalAmount,mChoosedGiftCardPurchaseId,mInvoiceRaisedBy,mInvoiceRaisedByType,mStoreID,mStoreLocationId,mPaymentNote);
							}else if(params[1].equalsIgnoreCase("both")){ // Both card 
								mGetResponse=mZouponswebservice.payInvoiceUsingBothCard(mInvoiceId,mInvoiceRaisedBy,mInvoiceRaisedByType,mCreditcardId,mAmountOnCreditCard,mStoreID/*,mChoosedGiftcardId*/,mAmountOnGiftCard,mTipAmount,mChoosedGiftCardPurchaseId,mTotalAmount,mStoreLocationId,mPaymentNote);
							}
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
		mProgressdialog.dismiss();
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("Invalid PIN")){
			alertBox_service("Information", "Entered PIN is incorrect, Please enter correct PIN to proceed the payment");
		}else {
			if(PaymentStatusVariables.message.equalsIgnoreCase("Payment Completed")){
				alertBox_service("Information", "Payment successfully completed , ThankYou for using Zoupons");
			}else{
				alertBox_service("Information", PaymentStatusVariables.message);
			}
		}
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
					mContext.startActivity(new Intent(mContext,Receipts.class));
				}
			}
		});
		service_alert.show();
	}
	
}

