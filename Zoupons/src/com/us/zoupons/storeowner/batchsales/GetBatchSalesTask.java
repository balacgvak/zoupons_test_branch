package com.us.zoupons.storeowner.batchsales;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynctask to get batch sales details from webservice
 *
 */

public class GetBatchSalesTask extends AsyncTask<String, String, ArrayList<Object>>{

	private BatchSalesDetails mContext;
	private StoreownerWebserivce mZouponswebservice=null;
	private StoreownerParsingclass mParsingclass=null;
	private ProgressDialog mProgressdialog=null;
	private String mEventFlag="";
	private String mSelectedFromDate="",mSelectedToDate="";
	private double mTotalTransactionAmount,mTotalTip,mTotalZouponsFee,mTotalNetAmount;
	private TextView mTotalAmountText,mTotalTipText,mTotalZouponsFeeText,mTotalNetAmountText;
	private ListView mBatchSalesList;

	 // To get Batch sales List
	public GetBatchSalesTask(BatchSalesDetails context,String eventFlag,String selectedFromDate, String selectedToDate,TextView totalAmountText, TextView totalTipText, TextView totalZouponsFeeText, TextView totalNetAmountText,ListView batchsalesList) {
		this.mContext = context;
		this.mEventFlag = eventFlag;
		this.mSelectedFromDate = selectedFromDate;
		this.mSelectedToDate = selectedToDate;
		this.mTotalAmountText = totalAmountText;
		this.mTotalTipText = totalTipText;
		this.mTotalZouponsFeeText = totalZouponsFeeText;
		this.mTotalNetAmountText = totalNetAmountText;
		this.mBatchSalesList = batchsalesList;
		mZouponswebservice= new StoreownerWebserivce(context);
		mParsingclass= new StoreownerParsingclass(this.mContext);
		mProgressdialog=new ProgressDialog(this.mContext);
		mProgressdialog.setCancelable(false);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}

	@Override
	protected ArrayList<Object> doInBackground(String... params) {
		try{
			// To get store information from preferences
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String mLocationId = mPrefs.getString("location_id", "");
			String mResponse=mZouponswebservice.getBatchsales(mLocationId,mEventFlag,mSelectedFromDate,mSelectedToDate,"");
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					ArrayList<Object> result = mParsingclass.parseBatchSalesDetais(mResponse); 
					if(result!= null){
						for(int i=0;i<result.size();i++){
							BatchDetailsClassVariables mBatchsalesdetails = (BatchDetailsClassVariables) result.get(i);
							mTotalTransactionAmount = mTotalTransactionAmount + Double.parseDouble(mBatchsalesdetails.amount);
							mTotalTip = mTotalTip + Double.parseDouble(mBatchsalesdetails.tip);
							mTotalZouponsFee = mTotalZouponsFee + Double.parseDouble(mBatchsalesdetails.zouponsfee);
							mTotalNetAmount = mTotalNetAmount + Double.parseDouble(mBatchsalesdetails.net_amount);
						}
						return result;	
					}else{
						return null;
					}
				}else{ // service issues
					return null;
				}
			}else { // failure
				return null;
			}
		}catch (Exception e) {
			// TODO: hande exception
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
		if(mProgressdialog != null && mProgressdialog.isShowing())
			mProgressdialog.dismiss();
		if(result!=null && result.size() >0){
			mTotalAmountText.setText("$"+String.format("%.2f",mTotalTransactionAmount));
			mTotalTipText.setText("$"+String.format("%.2f",mTotalTip));
			mTotalZouponsFeeText.setText("$"+String.format("%.2f",mTotalZouponsFee));
			mTotalNetAmountText.setText("$"+String.format("%.2f",mTotalNetAmount));
			CustomBatchSalesAdapter mBatchsalesListAdapter = new CustomBatchSalesAdapter(mContext, result, mEventFlag);
			mBatchSalesList.setAdapter(mBatchsalesListAdapter);	
		}else if(result!=null && result.size() == 0){
			mBatchSalesList.setAdapter(new CustomBatchSalesAdapter(mContext, new ArrayList<Object>(), mEventFlag));
			alertBox_service("Information", "Batchsales report not available");
		}else{
			alertBox_service("Information", "Unable to reach service.");
		}
	}

	/* To show alert pop up with respective message */
	private void alertBox_service(String title,final String msg) {
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

