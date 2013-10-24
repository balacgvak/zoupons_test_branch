package com.us.zoupons.zpay;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class GetUrlImageAsyncTask extends AsyncTask<String, String, Bitmap> {

	ImageView mUrlImage;
	Context mContext;
	Bitmap bmp=null;
	ProgressBar mProgressBar;
	
	public GetUrlImageAsyncTask(Context context,ImageView urlimage,ProgressBar progressbar){
		this.mContext = context;
		mUrlImage = urlimage;
		this.mProgressBar = progressbar;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		String src = params[0];
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(src);
			HttpParams httpparams = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(httpparams, 60000); // 1 minute
			request.setParams(httpparams);
			HttpResponse response = httpClient.execute(request);
			byte[] image = EntityUtils.toByteArray(response.getEntity());
			bmp =   BitmapFactory.decodeByteArray(image, 0,image.length);
			image = null;
			return bmp;
		} catch (IOException e) {
			e.printStackTrace();
			return bmp;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return bmp;
		}catch(Exception e){
			e.printStackTrace();
			return bmp;
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onCancelled(Bitmap result) {
		super.onCancelled(result);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		try{
			if(this.bmp!=null){
				mProgressBar.setVisibility(View.GONE);
				mUrlImage.setVisibility(View.VISIBLE);
				this.mUrlImage.setImageBitmap(bmp);
			}else{
				mProgressBar.setVisibility(View.GONE);
				alertBox_service("Information","Unable to load image.Please try again later.");
			}
		}catch (Exception e) {
			mProgressBar.setVisibility(View.GONE);
			mUrlImage.setVisibility(View.GONE);
			alertBox_service("Information", "");
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(mProgressBar!=null&&mUrlImage!=null){
			mProgressBar.setVisibility(View.VISIBLE);
			mUrlImage.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}
	
	public void alertBox_service(final String title,final String msg){
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
