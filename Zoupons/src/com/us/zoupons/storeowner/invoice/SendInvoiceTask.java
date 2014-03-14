package com.us.zoupons.storeowner.invoice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.shopper.invoice.InvoiceApproval;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.customercenter.CustomerCenter;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task to communicate with server to raise invoice for active customers 
 *
 */

public class SendInvoiceTask extends AsyncTask<String, String, String>{

	private InvoiceCenter mContext;
	private NetworkCheck mConnectionAvailabilityChecking=null;
	private StoreownerWebserivce mZouponswebservice=null;
	private StoreownerParsingclass mParsingclass=null;
	private ZouponsWebService Customerzouponswebservice = null;
	private ZouponsParsingClass Customerparsingclass = null;
	private ProgressDialog progressdialog=null;
	private String TAG="StoreOwnerInvoiceAsynchTask",mEventFlag="",mClassName="";
	private LinearLayout mTelephoneLayout;
	private ScrollView mAddInvoiceLayout;
	
	public SendInvoiceTask(InvoiceCenter context,LinearLayout telephoneLayout,ScrollView addInvoice, String EventFlag,String classname) {
		this.mContext = context;
		this.mTelephoneLayout = telephoneLayout;
		this.mAddInvoiceLayout = addInvoice;
		this.mEventFlag = EventFlag;
		this.mClassName = classname;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new StoreownerWebserivce(context);
		mParsingclass= new StoreownerParsingclass(this.mContext);
		Customerzouponswebservice= new ZouponsWebService(context);
		Customerparsingclass= new ZouponsParsingClass(context);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected String doInBackground(String... params) { // params[0] --> PIN // params[1] --> customer_id //params[2] -->amount //params[3] --> notes //params[4]--> coupon code
		String result="",mResponse="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
				if(mEventFlag.equalsIgnoreCase("List")){  // For invoice list
					SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
					String location_id = mPrefs.getString("location_id", "");
					String user_id = mPrefs.getString("user_id", "");
					String user_type = mPrefs.getString("user_type", "");
					if(params[5].equalsIgnoreCase("ZouponsCustomer")){
						mResponse=mZouponswebservice.raise_Invoice(user_id, params[0], user_type, params[1], location_id, params[2], params[3],params[4]);	
					}else{
						mResponse=mZouponswebservice.raise_Invoice(user_id, params[0], user_type, params[1], location_id, params[2], params[3],params[4]);
					}
					if(!mResponse.equalsIgnoreCase("noresponse") && !mResponse.equalsIgnoreCase("failure")){
						String mParsingResponse = mParsingclass.parseRaiseInvoice(mResponse);
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
				}else{ // To reject Invoice
					mResponse = Customerzouponswebservice.mGetRejectInvoiceStatus(params[0]);	
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){
						mResponse= Customerparsingclass.mParseRejectInvoiceResponse(mResponse);
						if(mResponse.equalsIgnoreCase("success")){						
							result ="success";				
						}else if(mResponse.equalsIgnoreCase("failure")){
							Log.i(TAG,"Error");
							result="Response Error.";
						}else if(mResponse.equalsIgnoreCase("norecords")){
							Log.i(TAG,"No Records");
							result="No Records";
						}
					}else{
						result="Response Error.";
					}
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
			if(result.equals("nonetwork")){
				Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equals("Response Error.")){
				alertBox_service("Information", "Unable to reach service.");
			}else if(result.equals("No Records")){
				alertBox_service("Information", result);
			}else if(result.equals("Thread Error")){
				alertBox_service("Information", "Unable to process.");
			}else if(result.equalsIgnoreCase("Success")){
				Log.i(TAG,"Success: ");
				if(mEventFlag.equalsIgnoreCase("List")){ 
					mContext.clearDatas();
					alertBox_service("Information", "Invoice has been successfully raised to customer");	
				}else{
					if(InvoiceApproval.mRejectInvoiceMessage.equalsIgnoreCase("Failure")){
						alertBox_service("Information", "Failed to Reject Invoice.");
					}else if(InvoiceApproval.mRejectInvoiceMessage.equalsIgnoreCase("Invoice Rejected")){
						alertBox_service("Information", "Invoice Rejected Sucessfully.");
					}else{
						alertBox_service("Information", InvoiceApproval.mRejectInvoiceMessage);
					}
				}
			}else{
				alertBox_service("Information", result);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	// To show alert pop up with respective message
	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
                if(msg.equalsIgnoreCase("Invoice has been successfully raised to customer")){
                	if(mClassName.equalsIgnoreCase("Invoice")){
                		mTelephoneLayout.setVisibility(View.VISIBLE);
                    	mAddInvoiceLayout.setVisibility(View.GONE);	
                	}else if(mClassName.equalsIgnoreCase("CustomerCenter")){
                		Intent intent_rightmenuCustomercenter = new Intent().setClass(mContext,CustomerCenter.class);
        				intent_rightmenuCustomercenter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        				mContext.startActivity(intent_rightmenuCustomercenter);	
                	}else{
                		mContext.finish();
                	}
                }else if(msg.equalsIgnoreCase("Invoice Rejected Sucessfully.")){
                	mContext.updateViews();
                }
			}
		});
		service_alert.show();
	}
}
