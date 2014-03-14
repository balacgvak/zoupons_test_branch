package com.us.zoupons.storeowner.refunds;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

public class IssueRefundTask extends AsyncTask<String, String, String>{

	private Context mContext;
	private NetworkCheck mConnectionAvailabilityChecking=null;
	private ZouponsWebService zouponswebservice=null;
	private ZouponsParsingClass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String TAG="StoreOwnerRefundsAsynchTask";
	private String mRefundAmount;
	private ListView mRefundDetailsList;

	public IssueRefundTask(Context context,String refundAmount,ListView RefundDetailsList) {
		this.mContext = context;
		this.mRefundAmount = refundAmount;
		this.mRefundDetailsList = RefundDetailsList;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
				if(result.equals("")){
					result="success";
				}else{
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
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}
		Log.i("Task", "onPOstExecute");		

		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			Log.i(TAG,"Success: ");
			alertBox_service("Information", "Amount has been successfully refunded");
		}
	}

	@Override
	protected void onPreExecute() {
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
                if(msg.equalsIgnoreCase("Amount has been successfully refunded")){
                	mRefundDetailsList.setAdapter(new CustomRefundDetailsAdapter(mContext, "refunds_details"));
                }
			}
		});
		service_alert.show();
	}
}

