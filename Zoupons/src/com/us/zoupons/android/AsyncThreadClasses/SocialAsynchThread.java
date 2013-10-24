/**
 * 
 */
package com.us.zoupons.android.AsyncThreadClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.StoreLike_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

/**
 * Class to get status and like of the store fb like.
 *
 */
public class SocialAsynchThread extends AsyncTask<String, String, String>{

	Context mContext;
	String TAG="SocialAsynchThread";
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	private ProgressDialog progressdialog=null;
	private TextView FBLikeCount;
	private String mFBLikeCount="0";
	ImageView mFBImage;

	public SocialAsynchThread(Context context,TextView SocialFBLikeCount,ImageView fbimage){
		this.mContext=context;
		this.mFBImage = fbimage;
		this.FBLikeCount = SocialFBLikeCount;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.mContext);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPreExecute() {
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);

		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
				mGetResponse=zouponswebservice.checkStoreLikeStatus(params[0]);	
				
				if(!mGetResponse.equals("")){
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParsingResponse=parsingclass.parseStoreLike(mGetResponse);
						if(mParsingResponse.equalsIgnoreCase("success")){
							Log.i(TAG,"Like List Size: "+WebServiceStaticArrays.mStoreLikeList.size());
							for(int i=0;i<WebServiceStaticArrays.mStoreLikeList.size();i++){
								final StoreLike_ClassVariables parsedobjectvalues = (StoreLike_ClassVariables) WebServiceStaticArrays.mStoreLikeList.get(i);
								if(!parsedobjectvalues.mSocialFBShareStatus.equals("")){
									
									if(params[0].equalsIgnoreCase("status")){
										if(parsedobjectvalues.mSocialFBShareStatus.equalsIgnoreCase("Yes")){
											mFBLikeCount=parsedobjectvalues.mSocialFBShareCount;
											result ="yes";
										}else if(parsedobjectvalues.mSocialFBShareStatus.equalsIgnoreCase("No")){
											mFBLikeCount=parsedobjectvalues.mSocialFBShareCount;
											result="no";
										}
									}
								}else{
									result="Response Error.";
								}
							}
						}else if(mParsingResponse.equalsIgnoreCase("failure")||mParsingResponse.equalsIgnoreCase("norecords")){
							Log.i(TAG,"Error");
							result="Response Error.";
						}
					}else{
						result="Response Error.";
					}
				}else{
					result="no data";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			Log.i(TAG,"Thread Error");
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		Log.i(TAG,"result: "+result);
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("no data")){
			alertBox_service("Information", "No data available");
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equals("success")){
			Log.i(TAG,"Success: "+result);
			alertBox_service("Information", "You have successfully liked this store.");
		}else if(result.equals("failure")){
			alertBox_service("Information", "Failed to like this store.");
		}else if(result.equals("yes")){
			mFBLikeCount = (mFBLikeCount.equalsIgnoreCase(""))?"0":mFBLikeCount;
			mFBImage.setEnabled(false);
			mFBImage.getBackground().setAlpha(120);
			
			if(Integer.parseInt(mFBLikeCount)>0){
				FBLikeCount.setVisibility(View.VISIBLE);
				FBLikeCount.setText(mFBLikeCount+" Shares");
			}else{
				FBLikeCount.setVisibility(View.VISIBLE);
				FBLikeCount.setText("0 Shares");
			}
		}else if(result.equals("no")){
			mFBLikeCount = (mFBLikeCount.equalsIgnoreCase(""))?"0":mFBLikeCount;
			mFBImage.setEnabled(true);
			mFBImage.getBackground().setAlpha(255);
			
			if(Integer.parseInt(mFBLikeCount)>0){
				FBLikeCount.setVisibility(View.VISIBLE);
				FBLikeCount.setText(mFBLikeCount+" Shares");
			}else{
				FBLikeCount.setVisibility(View.VISIBLE);
				FBLikeCount.setText("0 Shares");
			}
		}
		progressdialog.dismiss();
		super.onPostExecute(result);
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
