package com.us.zoupons.android.AsyncThreadClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.ClassVariables.POJOVideoURL;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.cards.ImageLoader;

public class PlayVideoClass extends AsyncTask<String,String,String>{ 
	String TAG ="PlayVideoClass";	
	TextView mStoreName;
	String URLValue ="";
	ImageView mImageView,mPlayVideoImage;
	Bitmap mBitmap = null;
	Context ctx;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private ProgressDialog progressdialog=null;
	String mVideoResponse="",mStoreId,mVideoParsingResponse;
	String mVideoImageViewWidth,mVideoImageViewHeight,mLocation_id;
	//Constructor
	ImageLoader imageLoader;
	String mImageUrl;


	public PlayVideoClass(Context context,TextView StoreName, String StoreId, ImageView mVideoImage,
			ImageView mVideoPlayImage,String imageviewwidth,String imageviewheight,String location_id) {
		this.ctx = context;
		this.mStoreName = StoreName;		
		this.mStoreId = StoreId;
		this.mLocation_id = location_id;
		this.mImageView = mVideoImage;
		this.mPlayVideoImage = mVideoPlayImage;
		this.mVideoImageViewWidth=imageviewwidth;
		this.mVideoImageViewHeight=imageviewheight;
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);		
		MainMenuActivity.VIDEOURLVALUE = "";
		imageLoader = new ImageLoader(context);
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

			
			//Get Video Response
			mVideoResponse = zouponswebservice.mGetVideo(mStoreId,mLocation_id);					
			if(!mVideoResponse.equals("failure") && !mVideoResponse.equals("noresponse")){
				//Parse the Video Response
				mVideoParsingResponse = parsingclass.mParseVideoResponse(mVideoResponse);				
				if(mVideoParsingResponse.equalsIgnoreCase("success")){
					//Check the Array List Values to Check Video availability
					if(WebServiceStaticArrays.mVideoList.size() != 0){
						POJOVideoURL obj = (POJOVideoURL) WebServiceStaticArrays.mVideoList.get(0);	
						//If The Message is Present then assume no video available
						if(obj.Message.length()>2){
							result ="No Video";
						}else{
							result ="success";
							mBitmap = parsingclass.getBitmapFromURL(obj.VideoThumbNail);
							mImageUrl = obj.VideoThumbNail;
							MainMenuActivity.VIDEOURLVALUE =  ZouponsWebService.getUrlVideoRTSP(obj.VideoURL);
						}
					}				
				}else if(mVideoParsingResponse.equalsIgnoreCase("failure")){
					Log.i(TAG,"Error");
					result="Response Error.";
				}else if(mVideoParsingResponse.equalsIgnoreCase("norecords")){
					Log.i(TAG,"No Records");
					result="No Records";
				}
			}else{
				result="Response Error.";
			}		
			Log.i(TAG, "RTSP :"+URLValue);
		}catch (Exception e) {
			result="Thread Error";
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
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equalsIgnoreCase("No Video")){
			mPlayVideoImage.setVisibility(View.GONE);
			alertBox_service("Information", "Currently no videos are available to play.");
		}else{
			Log.i(TAG,"Success: ");

			if(MainMenuActivity.VIDEOURLVALUE.length()>1){
				Log.i(TAG, "URL Found");
				mPlayVideoImage.setVisibility(View.VISIBLE);
			}else{
				Log.i(TAG, "URL Not Found");
				mPlayVideoImage.setVisibility(View.GONE);
			}

			//Check the Bitmap and set to Image View
			if(mBitmap != null){

				imageLoader = new ImageLoader(ctx);
				imageLoader.DisplayImage(mImageUrl+"&w="+mVideoImageViewWidth+"&h="+mVideoImageViewHeight+"&zc=0", mImageView);
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