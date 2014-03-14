package com.us.zoupons.shopper.coupons;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.notification.NotificationStatus;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * AsyncTask to communicate with web server to list store coupons 
 *
 */

public class CouponsTask extends AsyncTask<String, String, String>{

	private Context ctx;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponswebservice = null;
 	private ZouponsParsingClass mParsingclass = null;
 	private NetworkCheck mConnectivityChecking;
 	private String mCouponResponse,mParseCouponResponse;
    private String mProgressStatus="";
    private String mCheckRefresh="";
    private Button mNotificationCount;
    
	public CouponsTask(Context context,String progressStatus,Button notificationbutton) {
		this.ctx = context;
		this.mProgressStatus = progressStatus;
		this.mNotificationCount = notificationbutton;
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.ctx);
		mConnectivityChecking = new NetworkCheck();
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		MainMenuActivity.mIsLoadMore = true;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			//Start a status dialog
			mProgressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		}
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		mCheckRefresh = params[0];
		try{
			if(mConnectivityChecking.ConnectivityCheck(this.ctx)){
				if(MainMenuActivity.mCouponStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.mStaticCouponsArrayList.clear();
				}
				mCouponResponse = mZouponswebservice.mGetCoupons(MainMenuActivity.mCouponStart,"",RightMenuStoreId_ClassVariables.mLocationId);	
				if(!mCouponResponse.equals("failure") && !mCouponResponse.equals("noresponse")){
					mParseCouponResponse = mParsingclass.mParseCouponResponse(mCouponResponse);
					if(mParseCouponResponse.equalsIgnoreCase("success")){			
						result ="success";				
					}else if(mParseCouponResponse.equalsIgnoreCase("failure")){
						result="Response Error.";
					}else if(mParseCouponResponse.equalsIgnoreCase("norecords")){
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
		MainMenuActivity.mIsLoadMore = false;
		if(mProgressdialog != null && mProgressdialog.isShowing()){
			mProgressdialog.dismiss();
		}
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			// To change status of notification
			new NotificationStatus("private_coupon_recieved",mNotificationCount).changeNotificationStatus();
			((MainMenuActivity) ctx).SetCouponListAdatpter(WebServiceStaticArrays.mStoreRegularCardDetails,mCheckRefresh);
			MainMenuActivity.mCouponStart = MainMenuActivity.mCouponEndLimit;
			MainMenuActivity.mCouponEndLimit= String.valueOf(Integer.parseInt(MainMenuActivity.mCouponEndLimit)+20);
		}
	}
	
	/* To show alert pop up with respective message */
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
