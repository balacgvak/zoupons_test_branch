package com.us.zoupons.shopper.coupons;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * AsyncTask to communicate with web server to check or add Favorite coupon 
 *
 */

public class CheckFavoriteTask extends AsyncTask<String,String, String>{

	private Context mContext;
	private TextView mFavoriteText;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private NetworkCheck mConnectivityChecking;
	private String mCheckCouponFavoriteResponse,mParsedCheckCouponFavoriteResponse,mCouponID;

	public CheckFavoriteTask(Context context,TextView mMenuBarFavoriteText, String mCouponId) {
		this.mContext = context;
		this.mFavoriteText = mMenuBarFavoriteText;
		this.mCouponID = mCouponId;
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mConnectivityChecking = new NetworkCheck();
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		CouponDetail.mIsFavoriteCoupons ="";
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectivityChecking.ConnectivityCheck(this.mContext)){
				mCheckCouponFavoriteResponse = mZouponswebservice.mCheckIsFavoriteCoupon(UserDetails.mServiceUserId,mCouponID);	
				if(!mCheckCouponFavoriteResponse.equals("failure") && !mCheckCouponFavoriteResponse.equals("noresponse")){
					mParsedCheckCouponFavoriteResponse= mParsingclass.mParseCheckIsFavoriteCoupon(mCheckCouponFavoriteResponse);
					if(mParsedCheckCouponFavoriteResponse.equalsIgnoreCase("success")){			
						result ="success";				
					}else if(mParsedCheckCouponFavoriteResponse.equalsIgnoreCase("failure")){
						result="Response Error.";
					}else if(mParsedCheckCouponFavoriteResponse.equalsIgnoreCase("norecords")){
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
			result="Thread Error";
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(mProgressdialog != null && mProgressdialog.isShowing()){
			mProgressdialog.dismiss();
		}
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
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

	/* To show alert pop up with respective message */
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
