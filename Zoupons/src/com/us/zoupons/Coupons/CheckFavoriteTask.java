package com.us.zoupons.Coupons;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class CheckFavoriteTask extends AsyncTask<String,String, String>{
    String TAG="CheckFavoriteTask";
	Context ctx;
	TextView mFavoriteText;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
 	private ZouponsParsingClass parsingclass = null;
 	private NetworkCheck mConnectivityChecking;
	private String mCheckCouponFavoriteResponse,mParsedCheckCouponFavoriteResponse;
	String CouponID;
	

	public CheckFavoriteTask(Context context,TextView mMenuBarFavoriteText, String mCouponId) {
		this.ctx = context;
		this.mFavoriteText = mMenuBarFavoriteText;
		this.CouponID = mCouponId;
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		mConnectivityChecking = new NetworkCheck();
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		CouponDetail.mIsFavoriteCoupons ="";
	}

	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectivityChecking.ConnectivityCheck(this.ctx)){
				mCheckCouponFavoriteResponse = zouponswebservice.mCheckIsFavoriteCoupon(UserDetails.mServiceUserId,CouponID);	
				if(!mCheckCouponFavoriteResponse.equals("failure") && !mCheckCouponFavoriteResponse.equals("noresponse")){
					mParsedCheckCouponFavoriteResponse= parsingclass.mParseCheckIsFavoriteCoupon(mCheckCouponFavoriteResponse);
					if(mParsedCheckCouponFavoriteResponse.equalsIgnoreCase("success")){			
						result ="success";				
					}else if(mParsedCheckCouponFavoriteResponse.equalsIgnoreCase("failure")){
						Log.i(TAG,"Error");
						result="Response Error.";
					}else if(mParsedCheckCouponFavoriteResponse.equalsIgnoreCase("norecords")){
						Log.i(TAG,"No Records");
						result="No Records";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			e.printStackTrace();
			Log.i(TAG,"Thread Error");
			result="Thread Error";
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}
		Log.i("Task", "onPOstExecute");			
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			if(CouponDetail.mIsFavoriteCoupons.equalsIgnoreCase("No")){				
				mFavoriteText.setText("Add To Favorite");
			}else{
				mFavoriteText.setText("Remove From Favorite");
			}
		}
	}

	private void alertBox_service(String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
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
