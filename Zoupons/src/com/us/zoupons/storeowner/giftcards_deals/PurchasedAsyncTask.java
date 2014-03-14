package com.us.zoupons.storeowner.giftcards_deals;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Window;

import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task to fetch purchased gc/dc card details from server
 *
 */

public class PurchasedAsyncTask extends AsyncTask<String, String, ArrayList<Object>> {
	
	private Activity mContext;
	private ProgressDialog mProgressdialog;
	private StoreownerParsingclass mParsingclass;
	private StoreownerWebserivce mZouponsWebService;
	private String mResponseStatus="",mCardType,mStartDate,mEndDate;
	
	public PurchasedAsyncTask(Activity context,String classname, String selectedFromDate, String selectedToDate) {
		this.mContext = context;
		if(classname.equalsIgnoreCase("StoreOwnerPurchased_GiftCards")){ // From giftcards
			mCardType = "Regular";
		}else{ // From deal cards
			mCardType = "ZCard";
		}
		this.mStartDate = selectedFromDate;
		this.mEndDate = selectedToDate;
		mZouponsWebService = new StoreownerWebserivce(context);
	    mParsingclass= new StoreownerParsingclass(context);
	    mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	
	@Override
	protected ArrayList<Object> doInBackground(String... params) {
		try{
			// To get storeid from preferences
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String mStoreId = mPrefs.getString("store_id", "");
			mResponseStatus = mZouponsWebService.getPurchasedCardList(mCardType,mStoreId,mStartDate,mEndDate);
			if(!mResponseStatus.equals("failure") && !mResponseStatus.equals("noresponse")){ // Success
				return  mParsingclass.parsePurchased_gc_dc_response(mResponseStatus);
			}else{ // service issues
                return null;
			}
		}catch(Exception e){
			return null;
		}
		
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
	
	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		super.onPostExecute(result);
		if(mProgressdialog != null && mProgressdialog.isShowing()){
			mProgressdialog.dismiss();
		}
		if(result!=null && result.size() >0){ //Success 
			//Set List Adapter Here
			((StoreOwnerPurchasedCards) mContext).SetArrayPurchasedListAdatpter(result); // To set adapter
		}else if(result!=null && result.size() == 0){
			alertBox_service("Information", "Purchased Card list not available for this selected date range");
			((StoreOwnerPurchasedCards) mContext).SetArrayPurchasedListAdatpter(result); // To set adapter
		}else{
			alertBox_service("Information", "Unable to reach service.");
		}
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}
	
	// To show alert box with specified message..
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
