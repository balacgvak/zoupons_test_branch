package com.us.zoupons.storeowner.PointOfSale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.android.lib.integrator.IntentIntegrator;
import com.us.zoupons.android.lib.qrcode.config.QRCodeLibConfig;
import com.us.zoupons.zpay.CheckPINClassVariables;
import com.us.zoupons.zpay.Step2_ManageCards;

public class CheckUserPINTask extends AsyncTask<String, String, String>{

	Activity ctx;
	String TAG="CheckFavoriteAsynchThread";
	private ProgressDialog progressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse,mEnteredAmount,mCouponCode,mStore_notes,mEmployeePIN,mCustomerUserId,mCustomerUserType,mEventFlag;
	
	//checking employee pin from storeowner_pointofsale_part1
	public CheckUserPINTask(Activity context,String eventFlag,String amount, String couponcode,String store_notes) {
		this.ctx=context;
		this.mEventFlag = eventFlag;
		this.mEnteredAmount = amount;
		this.mCouponCode  = couponcode;
		this.mStore_notes = store_notes;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

    //checking customer pin from storeowner_pointofsale_part2
	public CheckUserPINTask(Activity context) {
		this.ctx=context;
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
		progressdialog = ProgressDialog.show(ctx,"Validating PIN","Please Wait!...",true);
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) { // params[0] --> entered user pin 
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				if(ctx.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_PointOfSale_Part1")){ // POS part1
					SharedPreferences mPrefs = ctx.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
					String mUser_Id = mPrefs.getString("user_id", "");
					String mUserType = mPrefs.getString("user_type", "");
					mEmployeePIN = params[0];
					mGetResponse = zouponswebservice.checkUserPIN(mEmployeePIN,mUser_Id,mUserType);
				}else{ // POS part2
					this.mCustomerUserId = params[1];
					this.mCustomerUserType = params[2];
					mGetResponse = zouponswebservice.checkUserPIN(params[0],params[1],params[2]);
				}
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse = parsingclass.parseCheckPIN(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						if(CheckPINClassVariables.message.equalsIgnoreCase("Success")){
							result="success";	
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
		try{
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("Invalid PIN")){
			alertBox_service("Information", "Entered PIN is incorrect, Please enter correct PIN to proceed the payment");
		}else if(result.equals("success")){
			if(ctx.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_PointOfSale_Part1")){
				if(mEventFlag.equalsIgnoreCase("qr")){ // For Qr code POS Empoyee pin code check
					QRCodeLibConfig qrcodeLibConfig = new QRCodeLibConfig();
					qrcodeLibConfig.useFrontLight = true;
					StoreOwner_PointOfSale_Part1.mQRScannerType = "QRCode";
					IntentIntegrator.initiateScan(ctx, qrcodeLibConfig);
				}else{
					Intent mPaymnetByMobileNumber = new Intent(ctx,StoreOwner_PointOfSale_Part2.class);
					mPaymnetByMobileNumber.putExtra("amount",mEnteredAmount);
					mPaymnetByMobileNumber.putExtra("coupon_code",mCouponCode);
					mPaymnetByMobileNumber.putExtra("store_notes",mStore_notes);
					mPaymnetByMobileNumber.putExtra("employee_pin",mEmployeePIN);
					ctx.startActivity(mPaymnetByMobileNumber);
				}
			}else{
				Intent intent_unlock = new Intent(ctx,Step2_ManageCards.class);
				intent_unlock.putExtra("FromPointOfSale", "pointofsale");
				intent_unlock.putExtra("datasourcename", "point_of_sale");
				intent_unlock.putExtra("user_id", mCustomerUserId);
				intent_unlock.putExtra("user_type", mCustomerUserType);
				SharedPreferences mPrefs = ctx.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				String mLocationId = mPrefs.getString("location_id", "");
				String mStore_Name = mPrefs.getString("store_name", "");
				intent_unlock.putExtra("payment_storeid", "mLocationId");
				intent_unlock.putExtra("payment_locationid", mLocationId);
				intent_unlock.putExtra("payment_storename", mStore_Name);
				ctx.startActivity(intent_unlock);
			}
		}
		progressdialog.dismiss();
		
		}catch (Exception e) {
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
				
			}
		});
		service_alert.show();
	}
}