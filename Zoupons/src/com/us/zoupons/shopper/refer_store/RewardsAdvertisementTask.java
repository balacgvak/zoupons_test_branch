package com.us.zoupons.shopper.refer_store;

import java.io.IOException;
import java.util.Timer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.us.zoupons.AdvertisementTimerTask;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynctask to get refer store advertisement details
 *
 */

public class RewardsAdvertisementTask extends AsyncTask<String, String, String>{

	private Context mContext;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	private String  mGetResponse="",mParseResponse="";
	private Bitmap mAdvertisementBitmap;
	private ImageView mVideoThumbnail,mVideoPlayButton;
	private int mImageViewWidth,mImageViewHeight;
	private AdvertisementTimerTask mTimerTask;
	private Timer mTimer;
		
	public RewardsAdvertisementTask(Context context,ImageView videoThumbnail,ImageView videoPlayButton,int imageviewwidth,int imageviewheight,AdvertisementTimerTask timertask,Timer timer,ProgressBar progressbar) {
		this.mContext = context;			
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(false);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		mVideoThumbnail = videoThumbnail;
		mVideoPlayButton = videoPlayButton;
		mImageViewWidth = imageviewwidth;
		mImageViewHeight = imageviewheight;
		mTimerTask = timertask;
		mTimer = timer;
		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		mProgressdialog.dismiss();
	}

	@Override
	protected String doInBackground(String... params) {
		String result ="";	
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){			
				mGetResponse = mZouponswebservice.GetAdvertisement(params[0],"rewards",UserDetails.mServiceUserId);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParseResponse= mParsingclass.ParseRewardsAdvertisementDetails(mGetResponse);
					if(mParseResponse.equalsIgnoreCase("success")){						
						result ="success";
						if(WebServiceStaticArrays.mRewardsAdvertisements.size() > 0 ){
							RewardsAdvertisementDetails ad_details = (RewardsAdvertisementDetails) WebServiceStaticArrays.mRewardsAdvertisements.get(0);
							if(ad_details.message.equalsIgnoreCase("") && ad_details.AdType.equalsIgnoreCase("Image")){
								mAdvertisementBitmap = getBitmapFromURL(ad_details.AdURL+"&w="+mImageViewWidth+"&h="+mImageViewHeight+"&zc=0");	
							}else if(ad_details.message.equalsIgnoreCase("") && ad_details.AdType.equalsIgnoreCase("Video")){
								//Method to Get RTSP video url from youtube video id and assigned to static variable
								MainMenuActivity.mRewardsVideoUrl =  ZouponsWebService.getUrlVideoRTSP(ad_details.AdURL);
							}
						}
					}else if(mParseResponse.equalsIgnoreCase("failure")){
						result="Response Error.";
					}else if(mParseResponse.equalsIgnoreCase("norecords")){
						result="No Records";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch (Exception e) {
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mProgressdialog.dismiss();
		if(result.equalsIgnoreCase("nonetwork")){
			Toast.makeText(mContext, "network connection not available", Toast.LENGTH_SHORT).show();
		}else if(result.equalsIgnoreCase("Response Error.") || result.equalsIgnoreCase("Thread Error")){
			Toast.makeText(mContext, "Unable to reach service", Toast.LENGTH_SHORT).show();
		}else if(result.equalsIgnoreCase("No Records")){
			Toast.makeText(mContext, "Currently No Advertisement Found", Toast.LENGTH_SHORT).show();
		}else if(result.equalsIgnoreCase("success")){
			if(WebServiceStaticArrays.mRewardsAdvertisements.size() > 0){
				RewardsAdvertisementDetails ad_details = (RewardsAdvertisementDetails) WebServiceStaticArrays.mRewardsAdvertisements.get(0);
				if(ad_details.message.equalsIgnoreCase("") && ad_details.AdType.equalsIgnoreCase("Image")){ // If Ad type is Image
					mVideoPlayButton.setVisibility(View.GONE);
					try{
						if(mTimer != null){
							mTimer.schedule(mTimerTask, 0, 2000);
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}else if(ad_details.message.equalsIgnoreCase("") && ad_details.AdType.equalsIgnoreCase("Video")){ // IF video
					mVideoPlayButton.setVisibility(View.VISIBLE);
					mVideoThumbnail.setImageBitmap(mAdvertisementBitmap);
				}else if(!ad_details.message.equalsIgnoreCase("")){
					mVideoPlayButton.setVisibility(View.GONE);
					Toast.makeText(mContext, ad_details.message, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	/* To download Bitmap from URL */
	public Bitmap getBitmapFromURL(String src) {
		try {
			Bitmap mBitmap;	
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(src);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(params, 60000); // 1 minute
			request.setParams(params);
			HttpResponse response = httpClient.execute(request);
			byte[] image = EntityUtils.toByteArray(response.getEntity());
			mBitmap =   BitmapFactory.decodeByteArray(image, 0,image.length);
			image = null;
			return mBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
}