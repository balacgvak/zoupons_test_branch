package com.us.zoupons.storeowner.store_info;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Window;

import com.us.zoupons.classvariables.POJOStoreInfo;
import com.us.zoupons.classvariables.POJOStoreTiming;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task to communicate with server for get/set store informations
 *
 */

public class Get_UpdateStoreGeneralInfoTask extends AsyncTask<String, String, ArrayList<Object>>{

	private StoreOwner_Info mContext;
	private StoreownerWebserivce mZouponswebservice=null;
	private StoreownerParsingclass mParsingclass=null;
	private ZouponsWebService mCustomerZouponsWebservice = null;
	private ZouponsParsingClass mCustomerParsingClass = null;
	private ProgressDialog mProgressdialog=null;
	private String mEvent_flag="",mWebAddress="",mLogoData="",mAboutStore="";
	
	// To get store info details or update storeTimings 
	public Get_UpdateStoreGeneralInfoTask(StoreOwner_Info context, String event_flag) {
		this.mContext = context;
		this.mEvent_flag = event_flag;
		mZouponswebservice= new StoreownerWebserivce(context);
		mParsingclass= new StoreownerParsingclass(this.mContext);
		mProgressdialog=new ProgressDialog(this.mContext);
		mCustomerZouponsWebservice = new ZouponsWebService(mContext);
		mCustomerParsingClass = new ZouponsParsingClass(mContext);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	
	// To set store info
	public Get_UpdateStoreGeneralInfoTask(StoreOwner_Info context, String event_flag,String web_address,String logo_data,String about_store) {
		this.mContext = context;
		this.mEvent_flag = event_flag;
		this.mLogoData = logo_data;
		this.mWebAddress = web_address;
		this.mAboutStore = about_store;
		mCustomerZouponsWebservice = new ZouponsWebService(mContext);
		mCustomerParsingClass = new ZouponsParsingClass(mContext);
		mZouponswebservice= new StoreownerWebserivce(context);
		mParsingclass= new StoreownerParsingclass(this.mContext);
		mProgressdialog=new ProgressDialog(this.mContext);
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
	protected ArrayList<Object> doInBackground(String... params) {
		try{
			String mResponse="",result="";
			ArrayList<Object> mStoreDetailsList = new ArrayList<Object>();
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String mStoreLocation_id = mPrefs.getString("location_id", "");
			String mStoreId = mPrefs.getString("store_id", "");
			if(mEvent_flag.equalsIgnoreCase("Get")){ // Get general info
				mResponse= mCustomerZouponsWebservice.mStoreInformation(mStoreId,mStoreLocation_id);
			}else if(mEvent_flag.equalsIgnoreCase("Get_Timings")){ // get timings
				mResponse= mCustomerZouponsWebservice.mStoreTimings(mStoreId, mStoreLocation_id);
			}else if(mEvent_flag.equalsIgnoreCase("set")){ // set general info
				mResponse= mZouponswebservice.updateStoreInfo("generalinfo",mStoreId,mStoreLocation_id,mWebAddress,mLogoData,mAboutStore);
			}else if(mEvent_flag.equalsIgnoreCase("set_Aboutstore")){  // set about store details
				mResponse= mZouponswebservice.updateStoreInfo("aboutus",mStoreId,mStoreLocation_id,mWebAddress,mLogoData,mAboutStore);
			}else{ // For updating business Hour
				mResponse=mZouponswebservice.update_StoreTimings(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],params[10],params[11],params[12],params[13],params[14],params[15],params[16],params[17],params[18],params[19],params[20],mStoreLocation_id);
			}
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					if(mEvent_flag.equalsIgnoreCase("Get")){ // Get general info
						result =  mCustomerParsingClass.mParseStoreInfo(mResponse);
						if(result.equalsIgnoreCase("success") && WebServiceStaticArrays.mStaticStoreInfo.size()>0){
							POJOStoreInfo mStoreInformation =  (POJOStoreInfo) WebServiceStaticArrays.mStaticStoreInfo.get(0);
							mStoreDetailsList.add(mStoreInformation);
							WebServiceStaticArrays.mStaticStoreInfo.clear();
							return mStoreDetailsList;
						}else if(result.equalsIgnoreCase("norecords")){ 
							return mStoreDetailsList;
						}else { // failure
							return null;
						}	
					}else if(mEvent_flag.equalsIgnoreCase("Get_Timings")){ // get timings
						result =  mCustomerParsingClass.parseTimeInfo(mResponse);
						if(result.equalsIgnoreCase("success") && WebServiceStaticArrays.mStoreTiming.size()>0){
							POJOStoreTiming mStoreInformation =  (POJOStoreTiming) WebServiceStaticArrays.mStoreTiming.get(0);
							mStoreDetailsList.add(mStoreInformation);
							WebServiceStaticArrays.mStoreTiming.clear();
							return mStoreDetailsList;
						}else if(result.equalsIgnoreCase("norecords")){
							return mStoreDetailsList;
						}else { // failure
							return null;
						}
					}else { 
					     return mParsingclass.parseupdateStoreDetails(mResponse,mEvent_flag); 
					}
					
				}else{ // service issues
					return null;
				}
			}else { // failure
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}

	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		super.onPostExecute(result);
		try{
			if(mProgressdialog != null && mProgressdialog.isShowing()){
				mProgressdialog.dismiss();
			}
			if(result!=null && result.size() >0){ // Success
				if(mEvent_flag.equalsIgnoreCase("Get")){ 
					mContext.updateViews(result); // To update general information views
				}else if(mEvent_flag.equalsIgnoreCase("Get_Timings")){
					mContext.updateBusinessHoursView(result);
				}else{
					mLogoData = null;
					String message = (String) result.get(0);
					alertBox_service("Information", message);
				}
			}else if(result!=null && result.size() == 0){ // Failure
				alertBox_service("Information", "Store information not available");
			}else{
				alertBox_service("Information", "Unable to reach service.");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	// To show alert pop up box with respective message
	private void alertBox_service(String title, final String msg) {
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


