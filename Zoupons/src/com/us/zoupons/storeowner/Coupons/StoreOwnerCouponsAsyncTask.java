package com.us.zoupons.storeowner.Coupons;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.MultiSelectListView.ChoosedCoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class StoreOwnerCouponsAsyncTask extends AsyncTask<String, String, String>{

	Activity mContext;
	private ProgressDialog progressdialog;
	private ZouponsParsingClass parsingclass;
	private ZouponsWebService zouponsWebService;
	public NetworkCheck mConnectionAvailabilityChecking;
	String mProgressStatus = "";
	private String mCouponResponse="";
	ListView mStoreOwnerCoupons;
	ArrayList<ChoosedCoupons> mChoosedCoupons;
	ListAdapter mChoosedCoupon_ListAdapter;
	String mCheckRefresh="",mCouponType="";
	
	public StoreOwnerCouponsAsyncTask(Activity context,String ProgressStatus,ListView listview) {
		this.mContext = context;
		this.mProgressStatus = ProgressStatus;
		this.mStoreOwnerCoupons = listview;
		zouponsWebService = new ZouponsWebService(context);
	    parsingclass= new ZouponsParsingClass(context);
	    mConnectionAvailabilityChecking= new NetworkCheck();
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		//Used To Check If Already running or not
		StoreOwnerCoupons.mIsLoadMore = true;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="",mLocationId="";
		try{
			mCheckRefresh = params[0];
			mCouponType = params[1];
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				if(StoreOwnerCoupons.mCouponStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.mStaticCouponsArrayList.clear();
				}
				// to get location id from preference
				SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				mLocationId = mPrefs.getString("location_id", "");
				mCouponResponse = zouponsWebService.mGetCoupons(StoreOwnerCoupons.mCouponStart,params[1],mLocationId);	
				if(!mCouponResponse.equals("failure") && !mCouponResponse.equals("noresponse")){
					String mParseCouponResponse = parsingclass.mParseCouponResponse(mCouponResponse);
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
	protected void onCancelled() {
		super.onCancelled();
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try{
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}
		StoreOwnerCoupons.mIsLoadMore = false;
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			//Set List Adapter Here
			((StoreOwnerCoupons) mContext).SetArrayStoreOwnerCouponsListAdatpter(mCheckRefresh,mCouponType);
			StoreOwnerCoupons.mCouponStart = StoreOwnerCoupons.mCouponEndLimit;
			StoreOwnerCoupons.mCouponEndLimit = String.valueOf(Integer.parseInt(StoreOwnerCoupons.mCouponEndLimit)+20);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			progressdialog = ProgressDialog.show(mContext,"Loading coupons...","Please Wait!",true);
		}else{
			Log.i("TAG", "Don't Show Progress here");
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
