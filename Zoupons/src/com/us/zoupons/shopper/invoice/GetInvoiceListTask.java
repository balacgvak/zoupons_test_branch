package com.us.zoupons.shopper.invoice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.notification.NotificationStatus;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.receipts.Receipts;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynctask to communicate with server to get invoice list
 *
 */

public class GetInvoiceListTask extends AsyncTask<String, String, String>{

	private String TAG=GetInvoiceListTask.class.getSimpleName();
	private Context mContext;
	private ProgressDialog mProgressDialog=null;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	private String  mGetResponse="",mParseResponse="";
	private String TODO="";
	private ListView mInvoiceList;	
	private ViewGroup mInvoiceContainer;
	private ScrollView mViewInvoiceContainer;
	private Button mNotificationCountButton;

	public GetInvoiceListTask(Context context,ListView invoicelist, ViewGroup InvoiceContainer, ScrollView ViewInvoiceContainer,Button notification_count) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mInvoiceContainer = InvoiceContainer;
		this.mViewInvoiceContainer = ViewInvoiceContainer;
		this.mNotificationCountButton = notification_count;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressDialog=new ProgressDialog(context);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
		mInvoiceList = invoicelist;
	}

	public GetInvoiceListTask(Context context) {
		this.mContext = context;
		mConnectivityNetworkCheck = new NetworkCheck();
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
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.show();
	}

	@Override
	protected String doInBackground(String... param) {
		String result ="";	
		TODO = param[0];
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){			
				if(TODO.equalsIgnoreCase("LIST")){ // Listing invoice
					mGetResponse = mZouponswebservice.mGetInvoiceListStatus(UserDetails.mServiceUserId);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= mParsingclass.mParseInvoiceListResponse(mGetResponse);
						if(mParseResponse.equalsIgnoreCase("success")){						
							result ="success";
						}else if(mParseResponse.equalsIgnoreCase("failure")){
							Log.i(TAG,"Error");
							result="Response Error.";
						}else if(mParseResponse.equalsIgnoreCase("norecords")){
							Log.i(TAG,"No Records");
							result="No Records";
						}
					}else{
						result="Response Error.";
					}

				}else if(TODO.equalsIgnoreCase("REJECTINVOICE")){ // To reject invoice
					mGetResponse = mZouponswebservice.mGetRejectInvoiceStatus(param[1]);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= mParsingclass.mParseRejectInvoiceResponse(mGetResponse);
						if(mParseResponse.equalsIgnoreCase("success")){						
							result ="success";				
						}else if(mParseResponse.equalsIgnoreCase("failure")){
							Log.i(TAG,"Error");
							result="Response Error.";
						}else if(mParseResponse.equalsIgnoreCase("norecords")){
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
		}catch (Exception e) {
			Log.i(TAG,"Thread Error");
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		try{
			if(mProgressDialog != null && mProgressDialog.isShowing()){
				mProgressDialog.dismiss();
			}
			if(result.equals("nonetwork")){
				Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equals("Response Error.")){
				if(mContext.getClass().getClass().getSimpleName().equalsIgnoreCase("InvoiceApproval")){
					alertBox_service("Information", "Unable to reach service.");	
				}
			}else if(result.equals("No Records")){
				if(mContext.getClass().getClass().getSimpleName().equalsIgnoreCase("InvoiceApproval")){
					alertBox_service("Information", "There is currently no invoice to display.");
				}
			}else if(result.equals("Thread Error")){
				if(mContext.getClass().getClass().getSimpleName().equalsIgnoreCase("InvoiceApproval")){
					alertBox_service("Information", "Unable to process.");
				}
			}else{
				if(TODO.equalsIgnoreCase("LIST")){ // Invoice List
					if(InvoiceApproval.mInvoiceListMessage.length()>1){
						alertBox_service("Information", InvoiceApproval.mInvoiceListMessage);
					}else{
						// To change status of notification
						new NotificationStatus("invoice_received",mNotificationCountButton).changeNotificationStatus();
						mInvoiceList.setAdapter(new CustomInvoiceAdapter(mContext, WebServiceStaticArrays.mInvoiceArrayList));
					}
				}else if(TODO.equalsIgnoreCase("REJECTINVOICE")){ // Reject Invoice
					if(mContext.getClass().getClass().getSimpleName().equalsIgnoreCase("InvoiceApproval")){ 
						if(InvoiceApproval.mRejectInvoiceMessage.equalsIgnoreCase("Failure")){
							alertBox_service("Information", "Failed to Reject Invoice.");
						}else if(InvoiceApproval.mRejectInvoiceMessage.equalsIgnoreCase("Invoice Rejected")){
							alertBox_service("Information", "Invoice Rejected Sucessfully.");
						}else{
							alertBox_service("Information", InvoiceApproval.mRejectInvoiceMessage);
						}
					}else{ 
						if(InvoiceApproval.mRejectInvoiceMessage.equalsIgnoreCase("Failure")){
							alertBox_service("Information", "Payment may be processed or qrcode expires");
						}else if(InvoiceApproval.mRejectInvoiceMessage.equalsIgnoreCase("Invoice Rejected")||InvoiceApproval.mRejectInvoiceMessage.equalsIgnoreCase("Expired Invoice")){
							alertBox_service("Information", "The payment process has been destructed successfully.");
						}else if(InvoiceApproval.mRejectInvoiceMessage.equalsIgnoreCase("Already Approved Invoice")){
							alertBox_service("Information", "Payment has been already processed successfully");
						}else {
							alertBox_service("Information", InvoiceApproval.mRejectInvoiceMessage);
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* To show alert pop up with respective message */
	private void alertBox_service(String title, final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setCancelable(true);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equalsIgnoreCase("Invoice Rejected Sucessfully.")){
					mInvoiceList.setAdapter(null);
					mViewInvoiceContainer.setVisibility(View.GONE);
					mInvoiceContainer.setVisibility(View.VISIBLE);
					GetInvoiceListTask mInvoiceTask = new GetInvoiceListTask(mContext,mInvoiceList,mInvoiceContainer,mViewInvoiceContainer,mNotificationCountButton);
					mInvoiceTask.execute("LIST");	
				}else if(msg.equalsIgnoreCase("The payment process has been destructed successfully.")){
					mContext.startActivity(new Intent(mContext,ShopperHomePage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
				}else if(msg.equalsIgnoreCase("Payment has been already processed successfully")){
					mContext.startActivity(new Intent(mContext,Receipts.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
				}
			}
		});
		service_alert.show();
	}
}