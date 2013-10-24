package com.us.zoupons.storeowner.PointOfSale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.zpay.PaymentStatusVariables;

public class NonMemberPaymentTask extends AsyncTask<String, String, String>{

	Context ctx;
	String TAG="NormalPaymentAsynchThread";
	private ProgressDialog progressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	String mPageFlag;	//flag to differentiate homepage and mainmenu page call
	String mCreditcardId="",mChoosedGiftcardId="";
	String mChoosedPurchaseId="";
	String mStoreID="",mTipAmount="",mTotalAmount,mStoreLocationID="";
	String mNote;
	String mActualAmount;
	String pageFlag="";
	String mNonZouponMemerUserId="";

	//For payment using CreditCard only
	public NonMemberPaymentTask(Context context,String SelectedCreditCardId, String StoreID, String TipAmount,String TotalAmount,String note,String actualamount,String storelocationid,String nonzoupons_customerId){
		this.ctx=context;
		this.mCreditcardId = SelectedCreditCardId;
		this.mStoreID = StoreID;
		this.mStoreLocationID = storelocationid;
		this.mTipAmount = TipAmount;
		this.mTotalAmount = TotalAmount;
		this.mNote = note;
		this.mActualAmount = actualamount;
		this.mNonZouponMemerUserId = nonzoupons_customerId;
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
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				mGetResponse=zouponswebservice.NormalPaymnetUsingCreditCard("from_storemodule",mNonZouponMemerUserId,mCreditcardId, mStoreID, mTipAmount, mTotalAmount, mNote, mActualAmount,mStoreLocationID,100,100);	
				
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=parsingclass.parseNormalPayment(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						Log.i(TAG,"Message status"+ PaymentStatusVariables.message);
						result="success";
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						Log.i(TAG,"Error");
						result="Response Error.";
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
		Log.i(TAG,"result: "+result);
		try{
			if(result.equals("nonetwork")){
				Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equals("Response Error.")){
				alertBox_service("Information", "Unable to reach service.");
			}else if(result.equals("success")){
				if(PaymentStatusVariables.message.equalsIgnoreCase("Success")){
					//alertBox_service("Information", "Payment successfully processed, ThankYou for using Zoupons");
					SharedPreferences mPrefs = ctx.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
					String mLocationId = mPrefs.getString("location_id", "");
					String mUser_Id = mPrefs.getString("user_id", "");
					String mUserType = mPrefs.getString("user_type", "");
					ApprovePaymentUsingMobileNumber mPaymentTask = new ApprovePaymentUsingMobileNumber(ctx,mLocationId,mUser_Id,StoreOwner_PointOfSale_Part1.mAmount,StoreOwner_PointOfSale_Part1.mCouponCode, "mobile_pay_member",PaymentStatusVariables.Invoice_id ,StoreOwner_PointOfSale_Part1.mStoreOwnerNotes, mUserType,mNonZouponMemerUserId);
					mPaymentTask.execute();
				}
			}else{
				alertBox_service("Information", "Unable to process the payment, please try after some time");
			}
			progressdialog.dismiss();
			super.onPostExecute(result);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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
				if(msg.equalsIgnoreCase("Payment successfully processed, ThankYou for using Zoupons")){

				}
			}
		});
		service_alert.show();
	}
}