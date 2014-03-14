package com.us.zoupons.android.asyncthread_class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to call webservice to fetch store photos
 *
 */

public class PhotoSwitcherAsynchThread extends AsyncTask<String, String, String>{

	private Context mContext;
	private String mPhotoResponse = null;
	private String mParsingPhotoResponse;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice;
	private ZouponsParsingClass mParsingclass;
	private ProgressDialog mProgressdialog;
	private int mViewPagerWidth,mViewPagerHeight;
	
	public PhotoSwitcherAsynchThread(Context context,int imagewidth,int imageheight) {
		this.mContext = context;
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mConnectionAvailabilityChecking = new NetworkCheck();
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		this.mViewPagerWidth = imagewidth;
		this.mViewPagerHeight = imageheight;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
				//Call Photo web service
				if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_Photos")){ // From store owner photos
					SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
					String mStoreLocation_id = mPrefs.getString("location_id", "");
					String mStoreId = mPrefs.getString("store_id", "");
					mPhotoResponse = mZouponswebservice.mStorePhotos(mStoreId,mStoreLocation_id);	
				}else{
					mPhotoResponse = mZouponswebservice.mStorePhotos(RightMenuStoreId_ClassVariables.mStoreID,RightMenuStoreId_ClassVariables.mLocationId);
				}
				if(!mPhotoResponse.equals("")){
					if(!mPhotoResponse.equals("failure") && !mPhotoResponse.equals("noresponse")){
						mParsingPhotoResponse =	mParsingclass.mParseStorePhoto(mPhotoResponse,mViewPagerWidth,mViewPagerHeight);
						if(!mParsingPhotoResponse.equals("failure") && !mParsingPhotoResponse.equals("norecords")){
							result = "success";
						}else if(mParsingPhotoResponse.equalsIgnoreCase("failure")){
							result = "failure";
						}else if(mParsingPhotoResponse.equalsIgnoreCase("norecords")){
							result="norecords";
						}
					}else{
						result="Response error";
					}
				}else{
					result="no data";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			result="failure";
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
		if(result.equalsIgnoreCase("success")){
		}else if(result.equalsIgnoreCase("failure")||result.equalsIgnoreCase("noresponse")){
			alertBox_service("Information", "Unable to reach service.",mContext);
		}else if(result.equalsIgnoreCase("nonetwork")){
			Toast.makeText(this.mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equalsIgnoreCase("norecords")){
			alertBox_service("Information", "Be the first to upload photos to store",mContext);
		}else if(result.equalsIgnoreCase("Response error")){
			alertBox_service("Information", "Unable to reach service.Please try again later.",mContext);
		}

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	/* To show alert pop up with respective message */
	public void alertBox_service(final String title,final String msg,Context context){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(context);
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