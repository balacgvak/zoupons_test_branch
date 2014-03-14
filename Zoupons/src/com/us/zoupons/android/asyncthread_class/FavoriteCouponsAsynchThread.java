package com.us.zoupons.android.asyncthread_class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.notification.NotificationStatus;
import com.us.zoupons.shopper.coupons.POJOCouponsList;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to communicate with webservice to load favorite coupons
 *
 */

public class FavoriteCouponsAsynchThread extends AsyncTask<String, String, String>{
	
	private Context mContext;
	private ListView mFavorite_CouponList;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
 	private ZouponsParsingClass parsingclass = null;
 	private NetworkCheck mConnectivityChecking;
 	private String mFavoriteCouponResponse,mParseCouponResponse;
    private String mProgressStatus="";
    private String mCheckRefresh="";
    private Button mNotificationCount;
	
	public FavoriteCouponsAsynchThread(Context ctx, ListView favoritecouponlist,String progressStatus,Button notificationcount) {
		this.mContext=ctx;
		this.mFavorite_CouponList = favoritecouponlist;
		MainMenuActivity.mIsLoadMore = true;
		this.mProgressStatus = progressStatus;
		this.mNotificationCount = notificationcount;
		zouponswebservice= new ZouponsWebService(ctx);
		parsingclass= new ZouponsParsingClass(this.mContext);
		mConnectivityChecking = new NetworkCheck();
		progressdialog=new ProgressDialog(mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			//Start a status dialog
			progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		}
		
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		mCheckRefresh = params[0];
		try{
			if(mConnectivityChecking.ConnectivityCheck(this.mContext)){
				if(MainMenuActivity.mCouponStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.mStaticCouponsArrayList.clear();
				}
				mFavoriteCouponResponse = zouponswebservice.FavoriteCoupons(MainMenuActivity.mCouponStart);	
				if(!mFavoriteCouponResponse.equals("failure") && !mFavoriteCouponResponse.equals("noresponse")){
					mParseCouponResponse = parsingclass.parseFavourite_Coupons(mFavoriteCouponResponse);
					if(mParseCouponResponse.equalsIgnoreCase("success")){
						for(int i=0;i<WebServiceStaticArrays.mStaticCouponsArrayList.size();i++){
							POJOCouponsList parsedobjectvalues = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(i);
							if(parsedobjectvalues.mMessage.length()>2){
								result =parsedobjectvalues.mMessage;
								break;
							}else{
								result ="success";
							}
						}	
						
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
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			WebServiceStaticArrays.mStaticCouponsArrayList.clear();
			mFavorite_CouponList.setVisibility(View.VISIBLE);
			((MainMenuActivity) mContext).SetFavouriteCouponListAdatpter(mCheckRefresh);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equals("No Favourite Coupons Available")){
			WebServiceStaticArrays.mStaticCouponsArrayList.clear();
			mFavorite_CouponList.setVisibility(View.VISIBLE);
			((MainMenuActivity) mContext).SetFavouriteCouponListAdatpter(mCheckRefresh);
		}else{
			// To change status of notification
			new NotificationStatus("private_coupon_recieved",mNotificationCount).changeNotificationStatus();
			mFavorite_CouponList.setVisibility(View.VISIBLE);
			((MainMenuActivity) mContext).SetFavouriteCouponListAdatpter(mCheckRefresh);
			MainMenuActivity.mCouponStart = MainMenuActivity.mCouponEndLimit;
			MainMenuActivity.mCouponEndLimit= String.valueOf(Integer.parseInt(MainMenuActivity.mCouponEndLimit)+20);
		}
	}
	
	/* To show alert pop up with respective message */
	private void alertBox_service(String title, final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equals("No Favorite Coupons are Available.")){
					mFavorite_CouponList.setAdapter(null);
					mFavorite_CouponList.setBackgroundResource(0);
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}