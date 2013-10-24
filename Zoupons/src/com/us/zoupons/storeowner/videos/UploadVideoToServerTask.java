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
import android.util.Log;

import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

public class UploadVideoToServerTask extends AsyncTask<String, Long, String>{

	private Activity mContext;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog = null;
	private String TAG="StoreOwnerVideosAsynchTask",mEventFlag="",mUploadFilePath="";
	private File file;
	private long totalsize;
	private String mFileSource="";
	
	UploadVideoToServerTask(Activity context,File file,String filesource){
		this.mContext = context;
		this.file = file;
		this.mFileSource = filesource;
		this.totalsize = file.length();
		Log.i("size", (totalsize/1024/1024)+"");
		parsingclass= new StoreownerParsingclass(this.mContext);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressdialog = new ProgressDialog(mContext);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressdialog.setMessage("Uploading Video...");
		progressdialog.setProgressNumberFormat("%1d B of %2d B");
		progressdialog.setMax((int)totalsize);
		progressdialog.setCancelable(false);
		progressdialog.show();
	}
		
	@Override
	protected String doInBackground(String... params) {
		String mReturnValue="";
		try {
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String location_id = mPrefs.getString("location_id", "");
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(StoreownerWebserivce.URL+StoreownerWebserivce.UPLOAD_VIDEO);
			FileBody filebodyVideo = new FileBody(file);
			StringBody location = null;
			location = new StringBody(location_id, "text/plain", Charset.forName("UTF-8"));
			CustomMultiPartEntity reqEntity = new CustomMultiPartEntity(new CustomMultiPartEntity.ProgressListener() {
				
				@Override
				public void transferred(long num) {
					publishProgress(num);
				}
			});
			reqEntity.addPart("location_id", new StringBody(location_id, "text/plain", Charset.forName("UTF-8")));
			reqEntity.addPart("uploadedfile", filebodyVideo);
			httppost.setEntity(reqEntity);
			System.out.println( "executing request " + httppost.getRequestLine( ) );
			System.out.println( "executing location id param " + location.getCharset() + " "+location.getMimeType());
			String content = EntityUtils.toString(httpclient.execute(httppost).getEntity(), "UTF-8");
			if(!content.equals("")){
				mReturnValue = parsingclass.parseUploadVideoStatus(content);
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
			progressdialog.dismiss();
			if(result.equalsIgnoreCase("success")){
				if(mFileSource.equalsIgnoreCase("Camera"))
				file.delete();
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
		progressdialog.setProgress(values[0].intValue());
	}
	
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
