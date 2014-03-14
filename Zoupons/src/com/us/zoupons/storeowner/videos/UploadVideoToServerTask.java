package com.us.zoupons.storeowner.videos;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task to upload recorded video or from gallery to webservice.
 *
 */

public class UploadVideoToServerTask extends AsyncTask<String, Long, String>{

	private Activity mContext;
	private StoreownerParsingclass mParsingclass=null;
	private ProgressDialog mProgressdialog = null;
	private File mUploadFile;
	private long mTotalsize;
	private String mFileSource="";
	
	UploadVideoToServerTask(Activity context,File file,String filesource){
		this.mContext = context;
		this.mUploadFile = file;
		this.mFileSource = filesource;
		this.mTotalsize = file.length();
		mParsingclass= new StoreownerParsingclass(this.mContext);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// Horizontal progress bar with buffer details
		mProgressdialog = new ProgressDialog(mContext);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressdialog.setMessage("Uploading Video...");
		mProgressdialog.setProgressNumberFormat("%1d B of %2d B");
		mProgressdialog.setMax((int)mTotalsize);
		mProgressdialog.setCancelable(false);
		mProgressdialog.show();
	}
		
	@Override
	protected String doInBackground(String... params) {
		String mReturnValue="";
		try {
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String location_id = mPrefs.getString("location_id", "");
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(StoreownerWebserivce.URL+StoreownerWebserivce.UPLOAD_VIDEO);
			FileBody filebodyVideo = new FileBody(mUploadFile);
			CustomMultiPartEntity reqEntity = new CustomMultiPartEntity(new CustomMultiPartEntity.ProgressListener() {
				
				@Override
				public void transferred(long num) {
					publishProgress(num);
				}
			});
			reqEntity.addPart("location_id", new StringBody(location_id, "text/plain", Charset.forName("UTF-8")));
			reqEntity.addPart("uploadedfile", filebodyVideo);
			httppost.setEntity(reqEntity);
			String content = EntityUtils.toString(httpclient.execute(httppost).getEntity(), "UTF-8");
			if(!content.equals("")){
				mReturnValue = mParsingclass.parseUploadVideoStatus(content);
			}else { // failure
				mReturnValue = "failure";
			}
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mReturnValue;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try{
			mProgressdialog.dismiss();
			if(result.equalsIgnoreCase("success")){
				if(mFileSource.equalsIgnoreCase("Camera"))
				mUploadFile.delete();
				alertBox_service("Information","Video has been successfully submitted to Zoupons for review");
			}else{
				alertBox_service("Information","unable to upload video please try after some time");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onProgressUpdate(Long... values) {
		super.onProgressUpdate(values);
		mProgressdialog.setProgress(values[0].intValue()); // To update progress dialog with transferred bytes
	}
	
	// To show alert pop up with respective message
	private void alertBox_service(String title,final String msg) {
		// TODO Auto-generated method stub
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
                if(msg.equalsIgnoreCase("Video has been successfully submitted to Zoupons for review") && mContext.getClass().getSimpleName().equalsIgnoreCase("CustomVideoRecorder")){
                	mContext.finish();
                }
			}
		});
		service_alert.show();
	}
	
}
