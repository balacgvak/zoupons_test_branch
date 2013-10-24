


package com.us.zoupons.storeowner.HomePage;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.storeowner.AddEmployee.StoreOwner_AddEmployee;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

public class StoreOwnerHomePageAsynchTask extends AsyncTask<String, String,ArrayList<Object>>{

	Context mContext;
	StoreownerWebserivce zouponswebservice=null;
	StoreownerParsingclass parsingclass=null;
	ProgressDialog progressdialog=null;
	String mProgressStatus,mClassName;
	ListView mListView;
	private String TAG="StoreOwnerHomePageAsynchTask";
	

   // Constructor for home page
	public StoreOwnerHomePageAsynchTask(Context context,String progressStatus,ListView listview,String classname) {
		this.mContext = context;
		this.mClassName = classname;
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
		String mStoreId = params[0];
		try{
			String mResponse=zouponswebservice.getStoreLocations(mStoreId,params[1]);
			Log.i("response", mResponse);
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					return parsingclass.parseStoreLocations(mResponse);
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
			alertBox_service("Information", "Sorry no data available");
		}else{
			alertBox_service("Information", "Unable to reach service");
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPreExecute() {
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			//Start a status dialog
			progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		}
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

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
