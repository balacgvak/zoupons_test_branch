package com.us.zoupons.storeowner.videos;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

public class GetAllStoreVideos extends AsyncTask<String, String, ArrayList<Object>>{

	private VideosDetails mContext;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog = null;
	private String TAG="StoreOwnerVideosAsynchTask",mEventFlag="",mUploadFilePath="";
	private ListView mStoreVideosList;


	public GetAllStoreVideos(VideosDetails context,ListView VideosList,String EventFlag) {
		this.mContext = context;
		this.mEventFlag = EventFlag;
		this.mStoreVideosList = VideosList;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	public GetAllStoreVideos(VideosDetails context,ListView VideosList,String UploadFile,String EventFlag) {
		this.mContext = context;
		this.mEventFlag = EventFlag;
		this.mStoreVideosList = VideosList;
		this.mUploadFilePath = UploadFile;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected ArrayList<Object> doInBackground(String... params) {
		try{
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String location_id = mPrefs.getString("location_id", "");
			String store_id = mPrefs.getString("store_id", "");
			if(mEventFlag.equalsIgnoreCase("video_list")){
				String mResponse=zouponswebservice.getAllStoreVideos(location_id);
				if(!mResponse.equals("")){
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
						return parsingclass.parseStoreVideos(mResponse);
					}else{ // service issues
						return null;
					}
				}else { // failure
					return null;
				}
			}else if(mEventFlag.equalsIgnoreCase("upload_video")){
				File file = new File(mUploadFilePath);
				String mResponse=zouponswebservice.sendFile(location_id,file);
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success

				}else{

				}
				return null;
			}else{ // To activate video
				String mResponse=zouponswebservice.getAllStoreVideos(location_id);
				if(!mResponse.equals("")){
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
						return parsingclass.parseStoreVideos(mResponse);
					}else{ // service issues
						return null;
					}
				}else { // failure
					return null;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		super.onPostExecute(result);
		try{
			Log.i("Task", "onPOstExecute");
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			if(result!=null && result.size() >0){
				if(mEventFlag.equalsIgnoreCase("video_list")){
					mContext.populateList(result);
				}else if(mEventFlag.equalsIgnoreCase("upload_video")){

				}else{ // To activate video

				}
			}else if(result!=null && result.size() == 0){
				alertBox_service("Information", "Store information not available");
			}else{
				alertBox_service("Information", "Unable to reach service.");
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@Override
	protected void onPreExecute() {
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}


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
