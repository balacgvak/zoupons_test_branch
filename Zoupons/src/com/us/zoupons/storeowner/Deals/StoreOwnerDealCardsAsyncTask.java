package com.us.zoupons.storeowner.Deals;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.cards.StoreGiftCardDetails;
import com.us.zoupons.storeowner.Employees.StoreOwner_Employees;
import com.us.zoupons.storeowner.GiftCards.StoreOwnerGiftCards;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

public class StoreOwnerDealCardsAsyncTask extends AsyncTask<String, String, String>{
	Activity mContext;
	private ProgressDialog progressdialog;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	public NetworkCheck mConnectionAvailabilityChecking;
	String mProgressStatus = "";
	private String mResponseStatus="";
	ListView mStoreOwnerGiftCards;
	private String TAG="StoreOwner_DealsAsyncTask";
	private String mCheckRefresh="",mStoreId="";
	
	public StoreOwnerDealCardsAsyncTask(Activity context,String ProgressStatus,ListView listview) {
		this.mContext = context;
		this.mProgressStatus = ProgressStatus;
		this.mStoreOwnerGiftCards = listview;
		zouponswebservice = new StoreownerWebserivce(context);
	    parsingclass= new StoreownerParsingclass(context);
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
			String result="";
			mCheckRefresh = params[0];
			try{
				if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
					if(StoreOwnerGiftCards.mDealsStart.equalsIgnoreCase("0")){
						WebServiceStaticArrays.mStoreZouponsDealCardDetails.clear();
					}
					// to get location id from preference
					SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
					mStoreId = mPrefs.getString("store_id", "");
					String user_id = mPrefs.getString("user_id", "");
					String mDealsListResponse = zouponswebservice.getZouponsDeals(mStoreId,user_id,StoreGiftCardDetails.mDealStart);	
					if(!mDealsListResponse.equals("failure") && !mDealsListResponse.equals("noresponse")){
						String mParseCouponResponse = parsingclass.parseStoreDeals(mDealsListResponse);
						if(mParseCouponResponse.equalsIgnoreCase("success")){			
							result ="success";				
						}else if(mParseCouponResponse.equalsIgnoreCase("failure")){
							result="Response Error.";
						}else if(mParseCouponResponse.equalsIgnoreCase("norecords")){
							result="No Records";
						}
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
			}else{
				//Set List Adapter Here
				((StoreOwnerGiftCards) mContext).SetArrayStoreOwnerDealCardsListAdatpter(mCheckRefresh);

				StoreOwnerDealCards.mStoreOwnerDealCardsStart = StoreOwnerDealCards.mStoreOwnerDealCardsEndLimit;
				StoreOwnerDealCards.mStoreOwnerDealCardsEndLimit = String.valueOf(Integer.parseInt(StoreOwnerDealCards.mStoreOwnerDealCardsEndLimit)+20);
			}
		}catch (Exception e) {
			e.printStackTrace();
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
