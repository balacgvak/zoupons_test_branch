package com.us.zoupons.storeowner.GiftCards;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class PurchasedAsyncTask extends AsyncTask<String, String, String> {
	
	Activity mContext;
	private ProgressDialog progressdialog;
	private ZouponsParsingClass parsingclass;
	private ZouponsWebService zouponsWebService;
	public NetworkCheck mConnectionAvailabilityChecking;
	String mProgressStatus = "";
	private String mResponseStatus="";
	ListView mStoreOwnerGiftCards;
	String mClassName;
	
	public PurchasedAsyncTask(Activity context,String ProgressStatus,ListView listview,String classname) {
		this.mContext = context;
		this.mProgressStatus = ProgressStatus;
		this.mStoreOwnerGiftCards = listview;
		this.mClassName = classname;
		zouponsWebService = new ZouponsWebService(context);
	    parsingclass= new ZouponsParsingClass(context);
	    mConnectionAvailabilityChecking= new NetworkCheck();
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	@Override
	protected String doInBackground(String... params) {
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				if(mResponseStatus.equals("")){
					mResponseStatus="success";
				}else{
					mResponseStatus="Response Error.";
				}
			}else{
				mResponseStatus="nonetwork";
			}
		}catch(Exception e){
			mResponseStatus ="failure";
		}
		return mResponseStatus;
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
		
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			//Sen List Adapter Here
			((StoreOwnerPurchasedCards) mContext).SetArrayPurchasedListAdatpter("");
			
			StoreOwnerPurchasedCards.mPurchasedStart = StoreOwnerPurchasedCards.mPurchasedEndLimit;
			StoreOwnerPurchasedCards.mPurchasedEndLimit = String.valueOf(Integer.parseInt(StoreOwnerPurchasedCards.mPurchasedEndLimit)+20);
		}
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);

		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){

			progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		}else{
			Log.i("TAG", "Don't Show Progress here");
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}
	
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
