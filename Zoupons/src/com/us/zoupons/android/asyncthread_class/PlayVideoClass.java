package com.us.zoupons.android.asyncthread_class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.classvariables.POJOVideoURL;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.shopper.cards.ImageLoader;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchonous task to communicate with webservice to fetch store video
 *
 */

public class PlayVideoClass extends AsyncTask<String,String,String>{
	
	private ImageView mImageView,mPlayVideoImage;
	private Bitmap mBitmap = null;
	private Context mContext;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private ProgressDialog mProgressdialog=null;
	private String mVideoResponse="",mStoreId,mVideoParsingResponse;
	private String mVideoImageViewWidth,mVideoImageViewHeight,mLocation_id;
	private ImageLoader mImageLoader;
	private String mImageUrl;
	
	//Constructor
	public PlayVideoClass(Context context,TextView StoreName, String StoreId, ImageView mVideoImage,
			ImageView mVideoPlayImage,String imageviewwidth,String imageviewheight,String location_id) {
		this.mContext = context;
		this.mStoreId = StoreId;
		this.mLocation_id = location_id;
		this.mImageView = mVideoImage;
		this.mPlayVideoImage = mVideoPlayImage;
		this.mVideoImageViewWidth=imageviewwidth;
		this.mVideoImageViewHeight=imageviewheight;
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);		
		MainMenuActivity.VIDEOURLVALUE = "";
		mImageLoader = new ImageLoader(context);
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
			//Get Video Response
			mVideoResponse = mZouponswebservice.mGetVideo(mStoreId,mLocation_id);					
			if(!mVideoResponse.equals("failure") && !mVideoResponse.equals("noresponse")){
				//Parse the Video Response
				mVideoParsingResponse = mParsingclass.mParseVideoResponse(mVideoResponse);				
				if(mVideoParsingResponse.equalsIgnoreCase("success")){
					//Check the Array List Values to Check Video availability
					if(WebServiceStaticArrays.mVideoList.size() != 0){
						POJOVideoURL obj = (POJOVideoURL) WebServiceStaticArrays.mVideoList.get(0);	
						//If The Message is Present then assume no video available
						if(obj.Message.length()>2){
							result ="No Video";
						}else{
							result ="success";
							mBitmap = mParsingclass.getBitmapFromURL(obj.VideoThumbNail);
							mImageUrl = obj.VideoThumbNail;
							MainMenuActivity.VIDEOURLVALUE =  ZouponsWebService.getUrlVideoRTSP(obj.VideoURL);
						}
					}				
				}else if(mVideoParsingResponse.equalsIgnoreCase("failure")){
					result="Response Error.";
				}else if(mVideoParsingResponse.equalsIgnoreCase("norecords")){
					result="No Records";
				}
			}else{
				result="Response Error.";
			}		
		}catch (Exception e) {
			result="Thread Error";
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
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equalsIgnoreCase("No Video")){
			mPlayVideoImage.setVisibility(View.GONE);
			alertBox_service("Information", "No video available for this store");
		}else{
			if(MainMenuActivity.VIDEOURLVALUE.length()>1){
				mPlayVideoImage.setVisibility(View.VISIBLE);
			}else{
				mPlayVideoImage.setVisibility(View.GONE);
			}
			//Check the Bitmap and set to Image View
			if(mBitmap != null){
				mImageLoader = new ImageLoader(mContext);
				mImageLoader.DisplayImage(mImageUrl+"&w="+mVideoImageViewWidth+"&h="+mVideoImageViewHeight+"&zc=0", mImageView);
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