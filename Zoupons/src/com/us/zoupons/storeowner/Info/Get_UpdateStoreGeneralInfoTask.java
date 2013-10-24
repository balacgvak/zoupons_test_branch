package com.us.zoupons.storeowner.Info;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.ClassVariables.POJOStoreTiming;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

public class Get_UpdateStoreGeneralInfoTask extends AsyncTask<String, String, ArrayList<Object>>{

	private StoreOwner_Info mContext;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ZouponsWebService customerZouponsWebservice = null;
	private ZouponsParsingClass customerParsingClass = null;
	private ProgressDialog progressdialog=null;
	private String TAG="Get_UpdateStoreGeneralInfoTask",event_flag="",mWebAddress="",mLogoData="",mAboutStore="";
	
	// To get store info details or update storeTimings 
	public Get_UpdateStoreGeneralInfoTask(StoreOwner_Info context, String event_flag) {
		this.mContext = context;
		this.event_flag = event_flag;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		customerZouponsWebservice = new ZouponsWebService(mContext);
		customerParsingClass = new ZouponsParsingClass(mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	// To set store info
	public Get_UpdateStoreGeneralInfoTask(StoreOwner_Info context, String event_flag,String web_address,String logo_data,String about_store) {
		this.mContext = context;
		this.event_flag = event_flag;
		this.mLogoData = logo_data;
		this.mWebAddress = web_address;
		this.mAboutStore = about_store;
		customerZouponsWebservice = new ZouponsWebService(mContext);
		customerParsingClass = new ZouponsParsingClass(mContext);
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	
		
	@Override
	protected ArrayList<Object> doInBackground(String... params) {
		try{
			String mResponse="",result="";
			ArrayList<Object> mStoreDetailsList = new ArrayList<Object>();
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String mStoreLocation_id = mPrefs.getString("location_id", "");
			String mStoreId = mPrefs.getString("store_id", "");
			if(event_flag.equalsIgnoreCase("Get")){
				mResponse= customerZouponsWebservice.mStoreInformation(mStoreId,mStoreLocation_id);
			}else if(event_flag.equalsIgnoreCase("Get_Timings")){
				mResponse= customerZouponsWebservice.mStoreTimings(mStoreId, mStoreLocation_id);
			}else if(event_flag.equalsIgnoreCase("set")){ 
				mResponse= zouponswebservice.updateStoreInfo("generalinfo",mStoreId,mStoreLocation_id,mWebAddress,mLogoData,mAboutStore);
			}else if(event_flag.equalsIgnoreCase("set_Aboutstore")){ 
				mResponse= zouponswebservice.updateStoreInfo("aboutus",mStoreId,mStoreLocation_id,mWebAddress,mLogoData,mAboutStore);
			}else{ // For updating business Hour
				mResponse=zouponswebservice.update_StoreTimings(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],params[10],params[11],params[12],params[13],params[14],params[15],params[16],params[17],params[18],params[19],params[20],mStoreLocation_id);
			}
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					if(event_flag.equalsIgnoreCase("Get")){
						result =  customerParsingClass.mParseStoreInfo(mResponse);
						if(result.equalsIgnoreCase("success") && WebServiceStaticArrays.mStaticStoreInfo.size()>0){
							POJOStoreInfo mStoreInformation =  (POJOStoreInfo) WebServiceStaticArrays.mStaticStoreInfo.get(0);
							mStoreDetailsList.add(mStoreInformation);
							WebServiceStaticArrays.mStaticStoreInfo.clear();
							return mStoreDetailsList;
						}else if(result.equalsIgnoreCase("norecords")){
							return mStoreDetailsList;
						}else {
							return null;
						}	
					}else if(event_flag.equalsIgnoreCase("Get_Timings")){
						result =  customerParsingClass.parseTimeInfo(mResponse);
						if(result.equalsIgnoreCase("success") && WebServiceStaticArrays.mStoreTiming.size()>0){
							POJOStoreTiming mStoreInformation =  (POJOStoreTiming) WebServiceStaticArrays.mStoreTiming.get(0);
							mStoreDetailsList.add(mStoreInformation);
							WebServiceStaticArrays.mStoreTiming.clear();
							return mStoreDetailsList;
						}else if(result.equalsIgnoreCase("norecords")){
							return mStoreDetailsList;
						}else {
							return null;
						}
					}else { 
					     return parsingclass.parseupdateStoreDetails(mResponse,event_flag); 
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
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			Log.i("Task", "onPostExecute");		
			if(result!=null && result.size() >0){
				if(event_flag.equalsIgnoreCase("Get")){
					mContext.updateViews(result);
				}else if(event_flag.equalsIgnoreCase("Get_Timings")){
					mContext.updateBusinessHoursView(result);
				}else{
					String message = (String) result.get(0);
					alertBox_service("Information", message);
				}
			}else if(result!=null && result.size() == 0){
				alertBox_service("Information", "Store information not available");
			}else{
				alertBox_service("Information", "Unable to reach service.");
			}
		}catch (Exception e) {
			e.printStackTrace();
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


