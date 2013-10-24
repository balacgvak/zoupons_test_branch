package com.us.zoupons.StoreInfo;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.us.zoupons.ClassVariables.POJOStorePhoto;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;

public class StorePhotoLoaderAsyncTask extends AsyncTask<Integer, Integer, String> {

	Context mContext;
	String TAG = "StorePhotoLoaderAsyncTask";
	int mWidth,mHeight;
	
	public StorePhotoLoaderAsyncTask(Context context,int imagewidth,int imageheight){
		this.mContext = context;
		this.mWidth = imagewidth;
		this.mHeight = imageheight;
	}
	
	@Override
	protected String doInBackground(Integer... params) {
		WebServiceStaticArrays.mFullSizeStorePhoto.clear();
		for(int i=0;i<WebServiceStaticArrays.mStorePhoto.size();i++){
			if(isCancelled()){
				break;
			}else{
				POJOStorePhoto obj = (POJOStorePhoto) WebServiceStaticArrays.mStorePhoto.get(i);
				obj.fullsize_storePhoto = getBitmapFromURL(obj.full_size+"&w="+mWidth+"&h="+mHeight+"&zc=0");
				WebServiceStaticArrays.mFullSizeStorePhoto.add(obj.fullsize_storePhoto);
				publishProgress(i);
			}
		}
		return "success";
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onCancelled(String result) {
		super.onCancelled(result);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result.equalsIgnoreCase("success")){
			Log.i(TAG,"Successfully loaded all images in full size.");
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.i(TAG,"Started download photo");
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if(values[0]!=null && !isCancelled()){
			StorePhotoAdapter.refresh();
		}
	}

	private Bitmap getBitmapFromURL(String src) {
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
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
}
