package com.us.zoupons;

import java.io.IOException;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.rewards.RewardsAdvertisementDetails;

public class AdvertisementTimerTask extends TimerTask{

	private Context context;
	private ImageView mAd_ImageView;
	private ProgressBar mAd_progressBar;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	String  mGetResponse="",mParseResponse="",slot_type="";
	private Bitmap mAdvertisementBitmap;
	
	public AdvertisementTimerTask(Context context,ImageView advertisementView , ProgressBar ad_progress,String slot_type) {
		this.mAd_ImageView = advertisementView;
		this.context = context;
		this.mAd_progressBar = ad_progress;
		this.slot_type = slot_type;
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(context);
	}
	
	@Override
	public void run() {
		Message mServiceCall = new Message();
		mServiceCall.obj = "service_call";
		mHandler.sendMessage(mServiceCall);
		
		String result;
		if(mConnectivityNetworkCheck.ConnectivityCheck(context)){			
			SharedPreferences mPrefs =context.getSharedPreferences(ZouponsLogin.PREFENCES_FILE, Context.MODE_PRIVATE);
			// Webservice call to get advertisement images...
			if(this.context.getClass().getSimpleName().equalsIgnoreCase("Registration")){
				mGetResponse=zouponswebservice.GetAdvertisement(mPrefs.getString("device_id",""), slot_type,Registration.mUserId);
			}else{// From Refer a store page
				mGetResponse=zouponswebservice.GetAdvertisement(mPrefs.getString("device_id",""), slot_type,UserDetails.mServiceUserId);
			}
			//Log.i("mGetReponse: ",mGetResponse);
			if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
				String 	mParsingResponse = parsingclass.ParseRewardsAdvertisementDetails(mGetResponse);
				if(mParsingResponse.equalsIgnoreCase("success")){
					result = "Success";
					if(WebServiceStaticArrays.mRewardsAdvertisements.size() > 0 ){
						RewardsAdvertisementDetails ad_details = (RewardsAdvertisementDetails) WebServiceStaticArrays.mRewardsAdvertisements.get(0);
						if(ad_details.message.equalsIgnoreCase("") && ad_details.AdType.equalsIgnoreCase("Image")){
							if(this.context.getClass().getSimpleName().equalsIgnoreCase("Registration")){
								mAdvertisementBitmap = getBitmapFromURL(ad_details.AdURL+"&w="+Registration.mAdSlotImageWidth+"&h=150&zc=0");
							}else{
								mAdvertisementBitmap = getBitmapFromURL(ad_details.AdURL+"&w="+MainMenuActivity.mAdSlotImageViewWidth+"&h=150&zc=0");
							}
						}
					}
				}else{
					result = "advertisement error";
				}
			}else{
				result = "advertisement error";
			}
		}else{
			result="nonetwork";
		}
		Message mResponseStatus = new Message();
		mResponseStatus.obj = result;
		mHandler.sendMessage(mResponseStatus);
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.obj.equals("Success")){
				mAd_progressBar.setVisibility(View.GONE);
				mAd_ImageView.setVisibility(View.VISIBLE);
				if(mAdvertisementBitmap != null){
					mAd_ImageView.setImageBitmap(mAdvertisementBitmap);
				}else{
					mAd_ImageView.setImageBitmap(null);
				}
			}else if(msg.obj.equals("service_call")){
				mAd_progressBar.setVisibility(View.GONE);
			}
		};
	};
	
	public Bitmap getBitmapFromURL(String src) {
		// TODO Auto-generated method stub
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
