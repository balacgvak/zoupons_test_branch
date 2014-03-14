package com.us.zoupons.shopper.giftcards_deals;

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

import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * AsyncTask to communicate with web server to list gitcard transaction history
 *
 */

public class GiftCardTransactionHistoryTask  extends AsyncTask<String, String, String>{

	private String TAG=GiftCardTransactionHistoryTask.class.getSimpleName();
	private Context mContext;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	public static String GIFT_CARD_TRANSACTION_HISTORY_MESSAGE="";
	private String  mGetResponse="",mParseResponse="";
	private ListView mListView;

	public GiftCardTransactionHistoryTask(Context context,ListView listview) {
		this.mContext = context;			
		this.mListView=listview;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
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
		//Start a status dialog
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}

	@Override
	protected String doInBackground(String... param) {
		String result ="";		
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){					
				mGetResponse = mZouponswebservice.mGetGiftCardTransactionHistory(UserDetails.mServiceUserId,param[0]);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParseResponse= mParsingclass.mParseGiftCardTransactionHistoryResponse(mGetResponse);
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
		super.onPostExecute(result);
		if(mProgressdialog != null && mProgressdialog.isShowing()){
			mProgressdialog.dismiss();
		}		
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			POJOGiftCardTransactionHistory mTransActionHistoryDetails = new POJOGiftCardTransactionHistory();
			mTransActionHistoryDetails.mTransactionDate = MenuUtilityClass.sGiftcardPurchaseDate;
			mTransActionHistoryDetails.mBalanceAmount=MenuUtilityClass.sGiftCardBalanceAmount;
			mTransActionHistoryDetails.mTotalAmount = String.format("%.2f",MenuUtilityClass.sGiftcardFaceValue);
			mTransActionHistoryDetails.mStatus="NotAvailable"; // To use it in adapter getview for setting the text color.
			WebServiceStaticArrays.mGiftCardTransactionHistory.add(mTransActionHistoryDetails);
			MenuUtilityClass.TransactionHistoryListView(this.mContext,this.mListView);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			if(WebServiceStaticArrays.mGiftCardTransactionHistory.size()>0){
				for(int i=0;i<WebServiceStaticArrays.mGiftCardTransactionHistory.size();i++){
					POJOGiftCardTransactionHistory obj = (POJOGiftCardTransactionHistory) WebServiceStaticArrays.mGiftCardTransactionHistory.get(i);
					/*if(obj.mStatus.equalsIgnoreCase("pending") || obj.mStatus.equalsIgnoreCase("approved")){
						obj.mBalanceAmount = MenuUtilityClass.sGiftcardFaceValue - Double.valueOf(obj.mTotalAmount);
						MenuUtilityClass.sGiftcardFaceValue = obj.mBalanceAmount;
						WebServiceStaticArrays.mGiftCardTransactionHistory.set(i, obj);
					}else if(obj.mStatus.equalsIgnoreCase("rejected")){
						obj.mBalanceAmount = MenuUtilityClass.sGiftcardFaceValue + Double.valueOf(obj.mTotalAmount);
						MenuUtilityClass.sGiftcardFaceValue = obj.mBalanceAmount;
						WebServiceStaticArrays.mGiftCardTransactionHistory.set(i, obj);
					}*/
					if(i!=0){
						if(obj.mStatus.equalsIgnoreCase("Refunded")){
							obj.mBalanceAmount = MenuUtilityClass.sGiftcardFaceValue - Double.valueOf(obj.mTotalAmount);;
							MenuUtilityClass.sGiftcardFaceValue = obj.mBalanceAmount;	
						}else if(obj.mStatus.equalsIgnoreCase("Approved") || obj.mStatus.equalsIgnoreCase("Pending")){
							obj.mBalanceAmount = MenuUtilityClass.sGiftcardFaceValue + Double.valueOf(obj.mTotalAmount);
							MenuUtilityClass.sGiftcardFaceValue = obj.mBalanceAmount;
						}
						//obj.mStatus = "Approved";
					}else{
						obj.mBalanceAmount = Double.valueOf(obj.mTotalAmount);
						MenuUtilityClass.sGiftcardFaceValue = obj.mBalanceAmount;
						//obj.mStatus = "NotAvailable";
				    }
					WebServiceStaticArrays.mGiftCardTransactionHistory.set(i, obj);
				}
				MenuUtilityClass.TransactionHistoryListView(this.mContext,this.mListView);
			}
		}
	}
	
	/* To show alert pop up with respective message */
	private void alertBox_service(String title, String msg) {
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