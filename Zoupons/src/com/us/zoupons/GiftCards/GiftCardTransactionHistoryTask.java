package com.us.zoupons.GiftCards;

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
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class GiftCardTransactionHistoryTask  extends AsyncTask<String, String, String>{

	String TAG=GiftCardTransactionHistoryTask.class.getSimpleName();
	Context ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	public static String GIFT_CARD_TRANSACTION_HISTORY_MESSAGE="";
	String  mGetResponse="",mParseResponse="";
	ListView mListView;

	public GiftCardTransactionHistoryTask(Context context,ListView listview) {
		this.ctx = context;			
		this.mListView=listview;
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
		progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... param) {
		String result ="";		
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){					
				mGetResponse = zouponswebservice.mGetGiftCardTransactionHistory(UserDetails.mServiceUserId,param[0]);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParseResponse= parsingclass.mParseGiftCardTransactionHistoryResponse(mGetResponse);
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
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}		
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			POJOGiftCardTransactionHistory mTransActionHistoryDetails = new POJOGiftCardTransactionHistory();
			mTransActionHistoryDetails.mTransactionDate = MenuUtilityClass.mGiftcardPurchaseDate;
			mTransActionHistoryDetails.mBalanceAmount=MenuUtilityClass.mGiftCardBalanceAmount;
			mTransActionHistoryDetails.mTotalAmount = String.format("%.2f",MenuUtilityClass.mGiftcardFaceValue);
			mTransActionHistoryDetails.mStatus="NotAvailable"; // To use it in adapter getview for setting the text color.
			WebServiceStaticArrays.mGiftCardTransactionHistory.add(mTransActionHistoryDetails);
			MenuUtilityClass.TransactionHistoryListView(this.ctx,this.mListView);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			if(WebServiceStaticArrays.mGiftCardTransactionHistory.size()>0){
				for(int i=0;i<WebServiceStaticArrays.mGiftCardTransactionHistory.size();i++){
					POJOGiftCardTransactionHistory obj = (POJOGiftCardTransactionHistory) WebServiceStaticArrays.mGiftCardTransactionHistory.get(i);
					if(obj.mStatus.equalsIgnoreCase("pending") || obj.mStatus.equalsIgnoreCase("approved")){
						obj.mBalanceAmount = MenuUtilityClass.mGiftcardFaceValue - Double.valueOf(obj.mTotalAmount);
						MenuUtilityClass.mGiftcardFaceValue = obj.mBalanceAmount;
						WebServiceStaticArrays.mGiftCardTransactionHistory.set(i, obj);
					}else if(obj.mStatus.equalsIgnoreCase("rejected")){
						obj.mBalanceAmount = MenuUtilityClass.mGiftcardFaceValue + Double.valueOf(obj.mTotalAmount);
						MenuUtilityClass.mGiftcardFaceValue = obj.mBalanceAmount;
						WebServiceStaticArrays.mGiftCardTransactionHistory.set(i, obj);
					}
				}
				MenuUtilityClass.TransactionHistoryListView(this.ctx,this.mListView);
			}
		}
	}
	private void alertBox_service(String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
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