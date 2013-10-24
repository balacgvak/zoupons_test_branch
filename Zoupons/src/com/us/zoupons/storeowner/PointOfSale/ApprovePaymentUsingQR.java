package com.us.zoupons.storeowner.PointOfSale;

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
import com.us.zoupons.storeowner.DashBoard.StoreOwnerDashBoard;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;
import com.us.zoupons.zpay.PaymentStatusVariables;

public class ApprovePaymentUsingQR extends AsyncTask<String, String, String> {

	private Context mContext;
	private NetworkCheck mConnectionAvailabilityChecking=null;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String TAG="PointOfSalePart1AsyncTask";
	private String store_locationId,employeeId,amount,couponcode,eventFlag,qrcode,store_note,raised_by_type;

	public ApprovePaymentUsingQR(Context context,String store_locationId,String employeeId,String amount,String couponcode,String eventFlag,String qrcode,String store_note,String raised_by_type) {
		this.mContext = context;
		this.store_locationId  = store_locationId;
		this.employeeId = employeeId;
		this.amount = amount;
		this.couponcode =couponcode;
		this.eventFlag = eventFlag;
		this.qrcode = qrcode;
		this.store_note = store_note;
		this.raised_by_type = raised_by_type;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected String doInBackground(String... params) {
		String result="",mresponse="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
				mresponse = zouponswebservice.approvebyQR(store_locationId, employeeId, amount, couponcode, eventFlag, qrcode, store_note, raised_by_type);
				if(!mresponse.equalsIgnoreCase("noresponse") && !mresponse.equalsIgnoreCase("failure")){
					String mParsingResponse = parsingclass.parsePaymentResponse(mresponse);
					if(!mParsingResponse.equals("failure") && !mParsingResponse.equals("no records")){
						result = mParsingResponse;
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						result = "failure";
					}else if(mParsingResponse.equalsIgnoreCase("no records")){
						result="norecords";
					}
				}else {
					result="Response Error.";
				}			
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			e.printStackTrace();
			Log.i(TAG,"Thread Error");
			result="Thread Error";
		}
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try{
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			Log.i("Task", "onPOstExecute");		

			if(result.equals("nonetwork")){
				Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equals("Response Error.")){
				alertBox_service("Information", "Unable to reach service.","Neutral_AlertBox");
			}else if(result.equals("No Records")){
				alertBox_service("Information", result,"Neutral_AlertBox");
			}else if(result.equals("Thread Error")){
				alertBox_service("Information", "Unable to process.","Neutral_AlertBox");
			}else{
				Log.i(TAG,"Success: ");
				if(PaymentStatusVariables.message.equalsIgnoreCase("Payment Completed")){
					alertBox_service("Information", "Payment successfully processed, ThankYou for using Zoupons","Neutral_AlertBox");
				}else if(PaymentStatusVariables.message.equalsIgnoreCase("Coupon Expired") || PaymentStatusVariables.message.equalsIgnoreCase("Coupon not belongs to this User") || PaymentStatusVariables.message.equalsIgnoreCase("Coupon Used") || PaymentStatusVariables.message.equalsIgnoreCase("Invalid Coupon") || PaymentStatusVariables.message.equalsIgnoreCase("Coupon is currently not active")){
					alertBox_service("Information", PaymentStatusVariables.message+".Do you want to proceed the payment without coupon","Positive_Negative_AlertBox");
				}else{
					alertBox_service("Information", PaymentStatusVariables.message,"Neutral_AlertBox");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPreExecute() {
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Processing payment","Please Wait!",true);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	private void alertBox_service(String title,final String msg,String alertType) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		if(alertType.equalsIgnoreCase("Neutral_AlertBox")){
			service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if(msg.equalsIgnoreCase("Payment successfully processed, ThankYou for using Zoupons")){
						Intent intent_home = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerDashBoard.class);
						intent_home.putExtra("classname", "Locations");
						mContext.startActivity(intent_home);
					}
				}
			});
		}else{
			service_alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					ApprovePaymentUsingQR mPaymentTask = new ApprovePaymentUsingQR(mContext,store_locationId,employeeId,amount,"","qr",qrcode,store_note,raised_by_type);
					mPaymentTask.execute();
				}
			});
			service_alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

				}
			});
		}

		service_alert.show();
	}
}
