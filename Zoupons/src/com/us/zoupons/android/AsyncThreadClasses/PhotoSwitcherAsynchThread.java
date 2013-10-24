package com.us.zoupons.android.AsyncThreadClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.StoreInfo.UnderlinePageIndicator;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class PhotoSwitcherAsynchThread extends AsyncTask<String, String, String>{

	String TAG ="Store Information";
	Context ctx;
	String mGetResponse=null,mPhotoResponse = null;
	String mParsingResponse,mParsingPhotoResponse;
	public NetworkCheck mConnectionAvailabilityChecking;
	public ZouponsWebService zouponswebservice;
	public ZouponsParsingClass parsingclass;
	public ProgressDialog progressdialog;
	public ViewPager mImagePager;
	public UnderlinePageIndicator mIndicator;
	int mViewPagerWidth,mViewPagerHeight;
	
	public PhotoSwitcherAsynchThread(Context context,int imagewidth,int imageheight) {
		this.ctx = context;
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		mConnectionAvailabilityChecking = new NetworkCheck();
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		this.mViewPagerWidth = imagewidth;
		this.mViewPagerHeight = imageheight;
	}
	
	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(this.ctx)){
				//Call Photo web service
				if(ctx.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_Photos")){ // From store owner photos
					SharedPreferences mPrefs = ctx.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
					String mStoreLocation_id = mPrefs.getString("location_id", "");
					String mStoreId = mPrefs.getString("store_id", "");
					mPhotoResponse = zouponswebservice.mStorePhotos(mStoreId,mStoreLocation_id);	
				}else{
					mPhotoResponse = zouponswebservice.mStorePhotos(RightMenuStoreId_ClassVariables.mStoreID,RightMenuStoreId_ClassVariables.mLocationId);
				}
				if(!mPhotoResponse.equals("")){
					if(!mPhotoResponse.equals("failure") && !mPhotoResponse.equals("noresponse")){
						mParsingPhotoResponse =	parsingclass.mParseStorePhoto(mPhotoResponse,mViewPagerWidth,mViewPagerHeight);
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
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}
		if(result.equalsIgnoreCase("success")){
		}else if(result.equalsIgnoreCase("failure")||result.equalsIgnoreCase("noresponse")){
			alertBox_service("Information", "Unable to reach service.",ctx);
		}else if(result.equalsIgnoreCase("nonetwork")){
			Toast.makeText(this.ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equalsIgnoreCase("norecords")){
			alertBox_service("Information", "Be the first person to upload photos to the store",ctx);
		}else if(result.equalsIgnoreCase("Response error")){
			alertBox_service("Information", "Unable to reach service.Please try again later.",ctx);
		}
		
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
	
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