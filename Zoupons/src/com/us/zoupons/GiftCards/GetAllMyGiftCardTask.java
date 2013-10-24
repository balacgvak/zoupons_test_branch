package com.us.zoupons.GiftCards;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.friends.Friends;
import com.us.zoupons.zpay.ZpayStep2SearchEnable;
import com.us.zoupons.zpay.zpay_step1;

public class GetAllMyGiftCardTask extends AsyncTask<String,String, String>{

	String TAG=GetAllMyGiftCardTask.class.getSimpleName();
	Activity ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
 	private ZouponsParsingClass parsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	String  mGetResponse="",mParseResponse="";
	public ListView mListView;
	//To Dynamic List Update
	String mCheckAndRefresh ="";
	String mProgressStatus = "";
	private View footerview;
	
	public GetAllMyGiftCardTask(Activity context,ListView listview,String ProgressStatus,View footerview) {
		this.ctx = context;		
		this.mListView = listview;
		this.mProgressStatus = ProgressStatus;
		this.footerview  = footerview;
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(false);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		if(mProgressStatus.equalsIgnoreCase("PROGRESS"))
		progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		//Used To Check If Already running or not
		GiftCards.mIsLoadMore = true;
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		String result ="";	
		mCheckAndRefresh = arg0[0];
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){
				mGetResponse = zouponswebservice.mGetAllGiftCards(UserDetails.mServiceUserId,POJOLimit.mStartLimit);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					//Clear the Array List at First Time
					if(POJOLimit.mStartLimit.equalsIgnoreCase("0")){
						WebServiceStaticArrays.mAllGiftCardList.clear();	
					}
					mParseResponse= parsingclass.mParseAllGiftCardResponse(mGetResponse);
					if(mParseResponse.equalsIgnoreCase("success")){						
						result ="success";
					}else if(mParseResponse.equalsIgnoreCase("failure")){
						Log.i(TAG,"Error");
						result="Response Error.";
					}else if(mParseResponse.equalsIgnoreCase("norecords")){
						Log.i(TAG,"No Records");
						result="No Records";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch (Exception e) {
			Log.i(TAG,"Thread Error");
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
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
			Log.i(TAG, "Success");
			if(POJOAllGiftCards.GET_ALL_GIFT_CARDS_MESSAGE.length()>1){
				alertBox_purchasegiftcard("Information",POJOAllGiftCards.GET_ALL_GIFT_CARDS_MESSAGE+".You will now be directed to the prepaid card purchase tab to start your Zoupons experience.");				
			}else{
				MenuUtilityClass.giftcardsListView(this.ctx , mListView,mCheckAndRefresh,footerview);	// Call ListView Adapter class to inflate list item
				this.mListView.setVisibility(View.VISIBLE);
				POJOLimit.mStartLimit = POJOLimit.mEndLimit;
				POJOLimit.mEndLimit = String.valueOf(Integer.parseInt(POJOLimit.mEndLimit)+20);
			}
		}	
	}
	private void alertBox_service(String title,String msg) {
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

	private void alertBox_purchasegiftcard(String title,final String msg) {
		AlertDialog.Builder purchasegiftcard_alert = new AlertDialog.Builder(this.ctx);
		purchasegiftcard_alert.setTitle(title);
		purchasegiftcard_alert.setMessage(msg);
		purchasegiftcard_alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		purchasegiftcard_alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				/*
				 * To Clear FB friend information for differentiating in giftcards/dealcards buy button
				 * **/
				Friends.friendName=""; 
				Friends.friendId="";
				Friends.isZouponsFriend="";
				
				ZpayStep2SearchEnable.searchEnableFlag=true;	//flag to enable search header text in zpay step2 page
				Intent intent_zpaystep1 = new Intent(ctx,zpay_step1.class);
				intent_zpaystep1.putExtra("pageflag", "giftcard");
				ctx.startActivity(intent_zpaystep1);
				dialog.dismiss();
			}
		});
		purchasegiftcard_alert.show();
	}
}
