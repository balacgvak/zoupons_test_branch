package com.us.zoupons.storeowner.homepage;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.ListView;

import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.classvariables.LoginUser_ClassVariables;
import com.us.zoupons.storeowner.employees.StoreOwner_AddEmployee;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task to fetch location and permission details for store owner/store employee
 *
 */

public class StoreOwnerHomePageAsynchTask extends AsyncTask<String, String,ArrayList<Object>>{

	private Context mContext;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String mProgressStatus;
	private ListView mListView;
	private String mStoreId,mUserId,mUserType;
	

   // Constructor for home page
	public StoreOwnerHomePageAsynchTask(Context context,String progressStatus,ListView listview,String classname) {
		this.mContext = context;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		this.mProgressStatus = progressStatus;
		this.mListView=listview;
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	// Constructor for Add Store employee
	public StoreOwnerHomePageAsynchTask(Context context, String progressStatus) {
		this.mContext = context;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		this.mProgressStatus = progressStatus;
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected ArrayList<Object> doInBackground(String... params) {
	    mStoreId = params[0];
	    try{
	    	String mLocationResponse=zouponswebservice.getStoreLocations(mStoreId,params[1]);
	    	if(!mLocationResponse.equals("")){
	    		if(!mLocationResponse.equals("failure") && !mLocationResponse.equals("noresponse")){ // Success
	    			if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_AddEmployee")){ // For add employee 
	    				return parsingclass.parseStoreLocations(mLocationResponse);
	    			}else{ // For home page , call store types for Store owner and permission for store employee 
	    				SharedPreferences mPrefss = mContext.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
	    				mUserId = mPrefss.getString("user_id", "");
	    				mUserType = mPrefss.getString("user_type", "");
	    				String mResponse= zouponswebservice.mGetStorePermissions(mStoreId, mUserId,mUserType);
	    				if(!mResponse.equals("") && !mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
	    					ArrayList<Object> result = parsingclass.parseStorePermissions(mResponse);
	    					if(result.size() > 0){
	    						SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
	    						LoginUser_ClassVariables obj = (LoginUser_ClassVariables) result.get(0);
	    						Editor editor = mPrefs.edit();
	    						editor.putString("store_id",mStoreId);
	    						editor.putString("user_id",mUserId);
	    						editor.putString("user_type",mUserType);
	    						editor.putString("user_name",obj.firstName);
	    						editor.putString("information_access",obj.information_access);
	    						editor.putString("gift_cards_access",obj.gift_cards_access);
	    						editor.putString("deal_cards_access",obj.deal_cards_access);
	    						editor.putString("coupons_access",obj.coupons_access);
	    						editor.putString("reviews_access",obj.reviews_access);
	    						editor.putString("photos_access",obj.photos_access);
	    						editor.putString("videos_access",obj.videos_access);
	    						editor.putString("dashboard_access",obj.dashboard_access);
	    						editor.putString("point_of_sale_access",obj.point_of_sale_access);
	    						editor.putString("invoice_center_access",obj.invoice_center_access);
	    						editor.putString("refund_access",obj.refund_access);
	    						editor.putString("batch_sales_access",obj.batch_sales_access);
	    						editor.putString("customer_center_access",obj.customer_center_access);
	    						editor.putString("communication_access",obj.communication_access);
	    						editor.putString("locations_access",obj.location_access);
	    						editor.putString("employees_access",obj.employee_access);
	    						editor.putString("billing_access",obj.billing_access);
	    						editor.commit();
	    					}
	    				}
	    				return parsingclass.parseStoreLocations(mLocationResponse);	
	    				}
	    			}else{ // service issues
	    				return null;
	    			}
	    		}else { // failure
	    			return null;
	    		}
	    	}catch(Exception e){
	    		e.printStackTrace();
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
		try{
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}
		if(result!=null && result.size() >0){
			if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_AddEmployee")){
				((StoreOwner_AddEmployee) mContext).SetStoreLocationsArray(result);	
			}else{
				MenuUtilityClass.StoreOwnerHomePageListView(mContext, mListView, "Locations",result);	
			}
		}else if(result!=null && result.size() == 0){
			alertBox_service("Information", "No store locations available");
		}else{
			alertBox_service("Information", "Unable to reach service");
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
			//Start a status dialog
			progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	//To show alert box with respective message
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
