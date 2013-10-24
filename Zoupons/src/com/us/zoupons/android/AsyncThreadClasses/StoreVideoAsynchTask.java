package com.us.zoupons.android.AsyncThreadClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Window;

import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.VideoPlay.VideoDialogActivity;
import com.us.zoupons.WebService.ZouponsWebService;

public class StoreVideoAsynchTask extends AsyncTask<String,String, String>{

	Context ctx;
	String VideoId;
	String VideoThumbnailURL;	
 	private ProgressDialog progressdialog=null;
	
	public StoreVideoAsynchTask(Context context,
			String videoURL) {
		this.ctx = context;
		this.VideoId = videoURL;	
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
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
		MainMenuActivity.VIDEOURLVALUE =  ZouponsWebService.getUrlVideoRTSP("http://www.youtube.com/watch?v="+VideoId);
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}
		if(MainMenuActivity.VIDEOURLVALUE.length()>2){
			Intent i= new Intent(ctx,VideoDialogActivity.class);
			i.putExtra("VIDEO", MainMenuActivity.VIDEOURLVALUE);
			ctx.startActivity(i);
		}
	}
}