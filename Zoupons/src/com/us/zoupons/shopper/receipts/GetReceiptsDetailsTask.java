package com.us.zoupons.shopper.receipts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynctask to communicate with server to get receipt details.
 *
 */

public class GetReceiptsDetailsTask extends AsyncTask<String, String, String>{

	private Context mContext;
	private ProgressDialog mProgressView;
	private ZouponsWebService zouponswebservice;
	private ZouponsParsingClass parsingclass ;
	private NetworkCheck mConnectivityCheck;
	private String mResponse;
	//To Dynamic List Update
	private String mCheckAndRefresh ="";
	private String mProgressStatus = "";

	public GetReceiptsDetailsTask(Context context, ListView mReceiptsList,String progressStatus) {
		this.mContext = context;
		this.mProgressStatus = progressStatus;
		mProgressView = new ProgressDialog(context);
		mProgressView.setTitle("Loading");
		mProgressView.setMessage("please wait..");
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(mProgressStatus.equalsIgnoreCase("NOPROGRESS")){
			
		}else{
			mProgressView.setCancelable(false);
			mProgressView.show();
		}
		Receipts.mIsLoadMore = true;
	}

	@Override
	protected String doInBackground(String... params) {
		String mresult ="",mParsingResponse="";
		mCheckAndRefresh = params[0];
		try{
			if(mConnectivityCheck.ConnectivityCheck(mContext)){
				mResponse = zouponswebservice.GetReceiptsDetails(Receipts.mReceiptStart,params[1],params[2]);
				if(Receipts.mReceiptStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.mReceiptsDetails.clear();
				}
				if(!mResponse.equalsIgnoreCase("noresponse") && !mResponse.equalsIgnoreCase("failure")){
					mParsingResponse = parsingclass.parseReceiptsList(mResponse);
					if(!mParsingResponse.equals("failure") && !mParsingResponse.equals("norecords")){
						mresult = "success";
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						mresult = "failure";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						mresult="norecords";
					}
				}else {
					mresult="Response Error.";
				}
			}else{
				mresult="nonetwork";
			}
		}catch(Exception e){
			mresult ="failure";
		}
		return mresult;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mProgressView.dismiss();
		Receipts.mIsLoadMore = false;
		if(result.equalsIgnoreCase("success")){
			Receipts.mReceiptStart = Receipts.mReceiptEndLimit;
			Receipts.mReceiptEndLimit = String.valueOf(Integer.parseInt(Receipts.mReceiptEndLimit)+20);
			//Set List Adapter Here
			((Receipts) mContext).SetReceiptsArrayListAdatpter(mCheckAndRefresh);
		}else if(result.equalsIgnoreCase("failure")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equalsIgnoreCase("norecords")){
			alertBox_service("Information", "No Receipts Available");
		}else if(result.equalsIgnoreCase("Response Error.")){
			alertBox_service("Information", "No Receipts Available");
		}else if(result.equalsIgnoreCase("no data")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equalsIgnoreCase("nonetwork")){
			Toast.makeText(this.mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
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
			}
		});
		service_alert.show();
	}

}
