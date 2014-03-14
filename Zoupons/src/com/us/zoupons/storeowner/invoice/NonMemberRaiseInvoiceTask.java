package com.us.zoupons.storeowner.invoice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.SendMobileVerfication_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/***
 * 
 * Asynchronous task to communicate with server to raise invoice to non zoupons customer
 *
 */

public class NonMemberRaiseInvoiceTask extends AsyncTask<String, String, String>{

	private InvoiceCenter mContext;
	private ProgressDialog mProgressdialog=null;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mCustomerwebservice=null;
	private ZouponsParsingClass mCustomerParsingclass=null;
	private String mGetResponse=null;
	private LinearLayout mTelephoneLayout;
	private ScrollView mAddInvoiceLayout;
	
	//For payment using CreditCard only
	public NonMemberRaiseInvoiceTask(InvoiceCenter context,LinearLayout telephoneLayout,ScrollView addInvoice){
		this.mContext=context;
		this.mTelephoneLayout = telephoneLayout;
		this.mAddInvoiceLayout = addInvoice;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mCustomerwebservice = new ZouponsWebService(context);
		mCustomerParsingclass = new ZouponsParsingClass(context);
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
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				String mLocationId = mPrefs.getString("location_id", "");
				String mUser_Id = mPrefs.getString("user_id", "");
				mGetResponse = mCustomerwebservice.getSignUpStep1_Verify("verify_email","",params[7],"",params[5],params[6],params[8],params[2],mUser_Id,mLocationId,params[0],params[1],params[9]);
				if(!mGetResponse.equals("")){
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){ // Success
						result = mCustomerParsingclass.parseSignUpStepOne(mGetResponse);
					 }else{
						result = "failure";
					}
				}else{ 
					result="Response Error.";
				}
			}else { 
				result="nonetwork";
			}

		}catch(Exception e){
			result="Response Error.";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try{
			if(result.equals("nonetwork")){
				Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equals("Response Error.")){
				alertBox_service("Information", "Unable to reach service.");
			}else if(result.equals("success")){
				if(WebServiceStaticArrays.mSendMobileVerificationCode.size()>0){
					final SendMobileVerfication_ClassVariables parsedobjectvalues = (SendMobileVerfication_ClassVariables) WebServiceStaticArrays.mSendMobileVerificationCode.get(0);
					if(parsedobjectvalues.mMessage.equalsIgnoreCase("Successfully sent Temporary Password")){
						mContext.clearDatas();
						alertBox_service("Information", "Invoice has been successfully raised to Non-Zoupons customer along with temporary password");			
					}else{
						alertBox_service("Information", parsedobjectvalues.mMessage);
					}
				}else{
					alertBox_service("Information", "Unable to raise invoice to non zoupons customer please try after some time");
				}
			}else{
				alertBox_service("Information", "Unable to raise invoice to non zoupons customer please try after some time");
			}
			mProgressdialog.dismiss();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	// To show alert box with respective message
	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equalsIgnoreCase("Invoice has been successfully raised to Non-Zoupons customer along with temporary password")){
					mTelephoneLayout.setVisibility(View.VISIBLE);
	            	mAddInvoiceLayout.setVisibility(View.GONE);		
				}
			}
		});
		service_alert.show();
	}
}