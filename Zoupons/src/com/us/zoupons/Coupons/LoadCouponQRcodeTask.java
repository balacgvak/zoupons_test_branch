package com.us.zoupons.Coupons;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadCouponQRcodeTask extends AsyncTask<String, Void, Void>{

	private Bitmap mCouponQrcode;
	private ImageView mImageView;
	private ProgressBar mProgressView;
	private TextView mCouponNoQRCodeText;
	String TAG = "LoadCouponQRcodeTask";
	
	public LoadCouponQRcodeTask(ImageView imageview, ProgressBar ProgressView,TextView coupontext) {
		this.mImageView = imageview;
		mProgressView = ProgressView;
		mCouponNoQRCodeText = coupontext;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressView.setVisibility(View.VISIBLE);
		mImageView.setVisibility(View.INVISIBLE);
		mCouponNoQRCodeText.setVisibility(View.GONE);
	}

	@Override
	protected Void doInBackground(String... params) {
		if(!isCancelled()) {
			mCouponQrcode = getBitmapFromURL(params[0]);
		}
		return null;
	}


	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		mProgressView.setVisibility(View.GONE);
		if(mCouponQrcode != null){
			mCouponNoQRCodeText.setVisibility(View.GONE);
			mImageView.setVisibility(View.VISIBLE);
			mImageView.setImageBitmap(mCouponQrcode);
		}else{
			mCouponNoQRCodeText.setVisibility(View.VISIBLE);
		}
	}
	
	public Bitmap getBitmapFromURL(String src) {
		Bitmap rtn = null;
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
			rtn = mBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			//mImageView.setVisibility(View.GONE);
			//mCouponNoQRCodeText.setVisibility(View.VISIBLE);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			//mImageView.setVisibility(View.GONE);
			//mCouponNoQRCodeText.setVisibility(View.VISIBLE);
		} catch(Exception e){
			e.printStackTrace();
			//mImageView.setVisibility(View.GONE);
			//mCouponNoQRCodeText.setVisibility(View.VISIBLE);
		}
		return rtn;
	}
}