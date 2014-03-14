package com.us.zoupons.storeowner.giftcards_deals;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.cards.StoreCardDetails;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task to communicate with server to fetch store deal cards list and update the same..
 *
 */

public class StoreOwnerDealCardsAsyncTask extends AsyncTask<String, String, String>{
	private Activity mContext;
	private ProgressDialog progressdialog;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private NetworkCheck mConnectionAvailabilityChecking;
	private String mProgressStatus = "";
	private String mResponseStatus="";
	private String mCheckRefresh="",mStoreId="",mEventFlag="",dealcard_id,sellpirce,count_per_week;

	// To list all store 
	public StoreOwnerDealCardsAsyncTask(Activity context,String ProgressStatus,ListView listview, String eventFlag) {
		this.mContext = context;
		this.mProgressStatus = ProgressStatus;
		this.mEventFlag = eventFlag;
		this.dealcard_id = "";
		this.sellpirce = "";
		this.count_per_week = "";
		zouponswebservice = new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(context);
		mConnectionAvailabilityChecking= new NetworkCheck();
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	// To update deals or inactivate deals
	public StoreOwnerDealCardsAsyncTask(Activity context,String ProgressStatus,ListView listview, String eventFlag,String dealcard_id,String sellpirce,String count_per_week) {
		this.mContext = context;
		this.mProgressStatus = ProgressStatus;
		this.mEventFlag = eventFlag;
		this.dealcard_id = dealcard_id;
		this.sellpirce = sellpirce;
		this.count_per_week = count_per_week;
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
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		}
	}


	@Override
	protected String doInBackground(String... params) {
		try{
			String result="";
			mCheckRefresh = params[0];
			try{
				if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
					if(StoreOwnerGiftCards.mDealsStart.equalsIgnoreCase("0") && mEventFlag.equalsIgnoreCase("list")){
						WebServiceStaticArrays.mStoreZouponsDealCardDetails.clear();
					}
					// to get location id from preference
					SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
					mStoreId = mPrefs.getString("store_id", "");
					String user_id = mPrefs.getString("user_id", "");
					String mDealsListResponse = zouponswebservice.getZouponsDeals(mStoreId,user_id,StoreCardDetails.mDealStart,mEventFlag,dealcard_id,sellpirce,count_per_week);	
					if(!mDealsListResponse.equals("failure") && !mDealsListResponse.equals("noresponse")){
						String mParseCouponResponse = parsingclass.parseStoreDeals(mDealsListResponse,mEventFlag);
						if(mParseCouponResponse.equalsIgnoreCase("failure")){
							result="Response Error.";
						}else if(mParseCouponResponse.equalsIgnoreCase("norecords")){
							result="No Records";
						}else{
							result=mParseCouponResponse;
						}
					}else{
						result="Response Error.";
					}
				}else{
					result="nonetwork";
				}
			}catch(Exception e){
				e.printStackTrace();
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
			if(progressdialog != null && progressdialog.isShowing())
				progressdialog.dismiss();
			if(result.equals("nonetwork")){
				Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equals("Response Error.")){
				alertBox_service("Information", "Unable to reach service.");
			}else if(result.equals("No Records")){
				alertBox_service("Information", result);
			}else if(result.equals("Thread Error")){
				alertBox_service("Information", "Unable to process.");
			}else{
				if(mEventFlag.equalsIgnoreCase("list")){
					//Set List Adapter Here
					((StoreOwnerGiftCards) mContext).SetArrayStoreOwnerDealCardsListAdatpter(mCheckRefresh);
					StoreOwnerGiftCards.mDealsStart = StoreOwnerGiftCards.mDealsEndLimit;
					StoreOwnerGiftCards.mDealsEndLimit = String.valueOf(Integer.parseInt(StoreOwnerGiftCards.mDealsEndLimit)+20);
				}else{
					alertBox_service("Information", result);
				}

			}
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
				if(msg.equalsIgnoreCase("Deal inserted successfully") || msg.equalsIgnoreCase("Deal updated successfully") || msg.equalsIgnoreCase("Deal inactivated successfully")){
					((StoreOwnerGiftCards) mContext).refreshDealsList();
				}else{

				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}
