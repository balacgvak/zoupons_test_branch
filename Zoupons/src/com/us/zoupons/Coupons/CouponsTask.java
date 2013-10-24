package com.us.zoupons.Coupons;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class CouponsTask extends AsyncTask<String, String, String>{

	Context ctx;
	ListView mCouponList;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
 	private ZouponsParsingClass parsingclass = null;
 	private NetworkCheck mConnectivityChecking;
 	String mCouponResponse,mParseCouponResponse;
    //String mStoreId ="3852745";
    private String TAG="Coupons Task";
	
    String mProgressStatus="";
    String mCheckRefresh="";
    
	public CouponsTask(Context context, ListView couponsList, String progressStatus) {
		this.ctx = context;
		this.mCouponList = couponsList;
		this.mProgressStatus = progressStatus;
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		mConnectivityChecking = new NetworkCheck();
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		MainMenuActivity.mIsLoadMore = true;
	}

	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			//Start a status dialog
			progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		}
		super.onPreExecute();
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
				mCouponResponse = zouponswebservice.mGetCoupons(MainMenuActivity.mCouponStart,"",RightMenuStoreId_ClassVariables.mLocationId);	
				if(!mCouponResponse.equals("failure") && !mCouponResponse.equals("noresponse")){
					mParseCouponResponse = parsingclass.mParseCouponResponse(mCouponResponse);
					if(mParseCouponResponse.equalsIgnoreCase("success")){			
						result ="success";				
					}else if(mParseCouponResponse.equalsIgnoreCase("failure")){
						Log.i(TAG,"Error");
						result="Response Error.";
					}else if(mParseCouponResponse.equalsIgnoreCase("norecords")){
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
		MainMenuActivity.mIsLoadMore = false;
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
			Log.i(TAG,"Success: ");
			((MainMenuActivity) ctx).SetCouponListAdatpter(WebServiceStaticArrays.mStoreRegularCardDetails,mCheckRefresh);
			MainMenuActivity.mCouponStart = MainMenuActivity.mCouponEndLimit;
			MainMenuActivity.mCouponEndLimit= String.valueOf(Integer.parseInt(MainMenuActivity.mCouponEndLimit)+20);
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
