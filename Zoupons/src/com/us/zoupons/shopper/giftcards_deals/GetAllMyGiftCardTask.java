package com.us.zoupons.shopper.giftcards_deals;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.mobilepay.CardPurchase;
import com.us.zoupons.mobilepay.ZpayStep2SearchEnable;
import com.us.zoupons.notification.NotificationStatus;
import com.us.zoupons.shopper.friends.Friends;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynctask to communicate with web server to list user's giftcards
 *
 */

public class GetAllMyGiftCardTask extends AsyncTask<String,String, String>{

	private Activity mContext;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponswebservice = null;
 	private ZouponsParsingClass mParsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	private String  mGetResponse="",mParseResponse="";
 	private ListView mListView;
	//To Dynamic List Update
 	private String mCheckAndRefresh ="";
 	private String mProgressStatus = "";
	private View mFooterview;
	private Button mNotificationCount;
	
	public GetAllMyGiftCardTask(Activity context,ListView listview,String ProgressStatus,View footerview,Button notificationCount) {
		this.mContext = context;		
		this.mListView = listview;
		this.mProgressStatus = ProgressStatus;
		this.mFooterview  = footerview;
		this.mNotificationCount = notificationCount;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(false);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		if(mProgressStatus.equalsIgnoreCase("PROGRESS"))
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		//Used To Check If Already running or not
		MyGiftCards.mIsLoadMore = true;
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		String result ="";	
		mCheckAndRefresh = arg0[0];
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){
				mGetResponse = mZouponswebservice.mGetAllGiftCards(UserDetails.mServiceUserId,POJOLimit.mStartLimit);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					//Clear the Array List at First Time
					if(POJOLimit.mStartLimit.equalsIgnoreCase("0")){
						WebServiceStaticArrays.mAllGiftCardList.clear();	
					}
					mParseResponse= mParsingclass.mParseAllGiftCardResponse(mGetResponse);
					if(mParseResponse.equalsIgnoreCase("success")){						
						result ="success";
					}else if(mParseResponse.equalsIgnoreCase("failure")){
						result="Response Error.";
					}else if(mParseResponse.equalsIgnoreCase("norecords")){
						result="No Records";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch (Exception e) {
			result="Thread Error";
			e.printStackTrace();
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
			// To change status of notification
			new NotificationStatus("giftcard_received",mNotificationCount).changeNotificationStatus();
			if(POJOAllGiftCards.GET_ALL_GIFT_CARDS_MESSAGE.length()>1){
				alertBox_purchasegiftcard("Information",POJOAllGiftCards.GET_ALL_GIFT_CARDS_MESSAGE+".You will now be directed to the prepaid card purchase tab to start your Zoupons experience.");				
			}else{
				MenuUtilityClass.giftcardsListView(this.mContext , mListView,mCheckAndRefresh,mFooterview);	// Call ListView Adapter class to inflate list item
				this.mListView.setVisibility(View.VISIBLE);
				POJOLimit.mStartLimit = POJOLimit.mEndLimit;
				POJOLimit.mEndLimit = String.valueOf(Integer.parseInt(POJOLimit.mEndLimit)+20);
			}
		}	
	}
	
	/* To show alert pop up with respective message */	
	private void alertBox_service(String title,String msg) {
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

	/* To show alert pop up with respective message */
	private void alertBox_purchasegiftcard(String title,final String msg) {
		AlertDialog.Builder purchasegiftcard_alert = new AlertDialog.Builder(this.mContext);
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
				Friends.friendEmailId = "";
				
				ZpayStep2SearchEnable.searchEnableFlag=true;	//flag to enable search header text in zpay step2 page
				Intent intent_zpaystep1 = new Intent(mContext,CardPurchase.class);
				intent_zpaystep1.putExtra("pageflag", "giftcard");
				mContext.startActivity(intent_zpaystep1);
				dialog.dismiss();
			}
		});
		purchasegiftcard_alert.show();
	}
}
