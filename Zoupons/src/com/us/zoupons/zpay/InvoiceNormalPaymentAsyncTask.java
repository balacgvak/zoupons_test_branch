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
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.FlagClasses.AccountLoginFlag;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.receipts.Receipts;

public class InvoiceNormalPaymentAsyncTask extends AsyncTask<String, String, String>{
	
	Context ctx;
	String TAG="CheckFavoriteAsynchThread";
	private ProgressDialog progressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	String mPageFlag;	//flag to differentiate homepage and mainmenu page call
	String mCreditcardId="",mChoosedGiftcardId="",mCardvalue="",mFacebookId="",mFriendNotes="",mDate="",mIsZouponsfriend="",mChoosedGiftCardPurchaseId="";
	String mInvoiceId="",mInvoiceRaisedBy="",mInvoiceRaisedByType="",mStoreID="",mInvoiceAmount="",mTipAmount="",mTotalAmount,mStoreLocationId="";
	String mAmountOnGiftCard,mAmountOnCreditCard,mPaymentNote;
	
	// GiftCard Invoice Approval
	public InvoiceNormalPaymentAsyncTask(Context context, String InvoiceId, String TipAmount,String TotalAmount,String ChoosedGiftCardPurchaseID,String InvoiceRaisedBy,String InvoiceRaisedByType,String StoreID,String emptyvalue ,String storelocationid,String payment_note){
		this.ctx=context;
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
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	//CreditCard Invoice Approval
	public InvoiceNormalPaymentAsyncTask(Context context,String InvoiceId, String InvoiceRaisedBy,String InvoiceRaisedByType, String SelectedCreditCardId,
			String StoreID, String TipAmount, String TotalAmount,String storelocationid,String payment_note) {
		this.ctx=context;
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
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	//Both Invoice Approval
	public InvoiceNormalPaymentAsyncTask(Context context,String InvoiceId, String InvoiceRaisedBy,String InvoiceRaisedByType,String SelectedCreditCardId,String AmountonCreditCard, String StoreID, /*String ChoosedGiftcardID,*/
			String AmountonGiftcard, String TipAmount,String ChoosedGiftCardPurchaseID,String TotalAmount,String storelocationid,String payment_note) {
		this.ctx=context;
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
	protected String doInBackground(String... params) { // params[0] --> entered user pin // params[1] --> payment type 
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				mGetResponse = zouponswebservice.checkUserPIN(params[0],UserDetails.mServiceUserId,AccountLoginFlag.accountUserTypeflag);
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse = parsingclass.parseCheckPIN(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						if(CheckPINClassVariables.message.equalsIgnoreCase("Success")){
							if(params[1].equalsIgnoreCase("creditcard")){ // payment from invoice by using only Credit Card
								mGetResponse=zouponswebservice.payInvoiceUsingCreditCard(mInvoiceId,mInvoiceRaisedBy,mInvoiceRaisedByType,mCreditcardId,mStoreID,mTipAmount,mTotalAmount,mStoreLocationId,mPaymentNote);	
							}else if(params[1].equalsIgnoreCase("giftcard")){  // payment from invoice by using only giftcard
								mGetResponse=zouponswebservice.payInvoiceUsingGiftcard(mInvoiceId,mTipAmount,mTotalAmount,mChoosedGiftCardPurchaseId,mInvoiceRaisedBy,mInvoiceRaisedByType,mStoreID,mStoreLocationId,mPaymentNote);
							}else if(params[1].equalsIgnoreCase("both")){
								mGetResponse=zouponswebservice.payInvoiceUsingBothCard(mInvoiceId,mInvoiceRaisedBy,mInvoiceRaisedByType,mCreditcardId,mAmountOnCreditCard,mStoreID/*,mChoosedGiftcardId*/,mAmountOnGiftCard,mTipAmount,mChoosedGiftCardPurchaseId,mTotalAmount,mStoreLocationId,mPaymentNote);
							}
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
				alertBox_service("Information", "Payment successfully completed , ThankYou for using Zoupons");
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
					ctx.startActivity(new Intent(ctx,Receipts.class));
				}
			}
		});
		service_alert.show();
	}
	
}

