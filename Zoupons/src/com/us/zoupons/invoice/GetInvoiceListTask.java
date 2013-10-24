package com.us.zoupons.invoice;

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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.SlidingView;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.receipts.Receipts;

public class GetInvoiceListTask extends AsyncTask<String, String, String>{

	String TAG=GetInvoiceListTask.class.getSimpleName();
	Context ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	String  mGetResponse="",mParseResponse="";
	String TODO="";
	private ListView mInvoiceList;	
	private ViewGroup mInvoiceContainer;
	private ScrollView mViewInvoiceContainer;

	public GetInvoiceListTask(Context context,ListView invoicelist, ViewGroup InvoiceContainer, ScrollView ViewInvoiceContainer) {
		// TODO Auto-generated constructor stub
		this.ctx = context;
		this.mInvoiceContainer = InvoiceContainer;
		this.mViewInvoiceContainer = ViewInvoiceContainer;
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		mInvoiceList = invoicelist;
	}

	public GetInvoiceListTask(Context context) {
		this.ctx = context;
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog.setMessage("Loading...");
		progressdialog.show();
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... param) {
		String result ="";	
		TODO = param[0];
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){			
				if(TODO.equalsIgnoreCase("LIST")){
					mGetResponse = zouponswebservice.mGetInvoiceListStatus(UserDetails.mServiceUserId);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= parsingclass.mParseInvoiceListResponse(mGetResponse);
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
				}else if(TODO.equalsIgnoreCase("REJECTINVOICE")){
					mGetResponse = zouponswebservice.mGetRejectInvoiceStatus(param[1]);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= parsingclass.mParseRejectInvoiceResponse(mGetResponse);
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
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
			}

			if(result.equals("nonetwork")){
				Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equals("Response Error.")){
				if(ctx.getClass().getClass().getSimpleName().equalsIgnoreCase("InvoiceApproval")){
					alertBox_service("Information", "Unable to reach service.");	
				}
			}else if(result.equals("No Records")){
				if(ctx.getClass().getClass().getSimpleName().equalsIgnoreCase("InvoiceApproval")){
					//alertBox_service("Information", result);
					alertBox_service("Information", "There is currently no invoice to display.");
				}
			}else if(result.equals("Thread Error")){
				if(ctx.getClass().getClass().getSimpleName().equalsIgnoreCase("InvoiceApproval")){
					alertBox_service("Information", "Unable to process.");
				}
			}else{
				Log.i(TAG, "Success");
				if(TODO.equalsIgnoreCase("LIST")){
					if(InvoiceApproval.mInvoiceListMessage.length()>1){
						alertBox_service("Information", InvoiceApproval.mInvoiceListMessage);
					}else{
						mInvoiceList.setAdapter(new CustomInvoiceAdapter(ctx, WebServiceStaticArrays.mInvoiceArrayList));
					}
				}else if(TODO.equalsIgnoreCase("REJECTINVOICE")){
					if(ctx.getClass().getClass().getSimpleName().equalsIgnoreCase("InvoiceApproval")){
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

	private void alertBox_service(String title, final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
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
					GetInvoiceListTask mInvoiceTask = new GetInvoiceListTask(ctx,mInvoiceList,mInvoiceContainer,mViewInvoiceContainer);
					mInvoiceTask.execute("LIST");	
				}else if(msg.equalsIgnoreCase("The payment process has been destructed successfully.")){
					ctx.startActivity(new Intent(ctx,SlidingView.class));
				}else if(msg.equalsIgnoreCase("Payment has been already processed successfully")){
					ctx.startActivity(new Intent(ctx,Receipts.class));
				}else{}
			}
		});
		service_alert.show();
	}
}