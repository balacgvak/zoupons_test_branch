package com.us.zoupons.GiftCards;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class RedeemGiftCardTask extends AsyncTask<String, String, String> {

	String TAG=RedeemGiftCardTask.class.getSimpleName();
	Context ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	String  mGetResponse="",mParseResponse="";
	public static String REED_GIFT_CARD_MESSAGE="";
	public EditText mVerificationCode;
	public LinearLayout mMenuBarGiftCards,mMenuBarRedeem;
	public ViewGroup mGiftCardsListViewParent,mGiftCardsRedeemParent;

	public RedeemGiftCardTask(Context context,EditText verificationcode,LinearLayout menubargiftcards,LinearLayout menubarredeem,ViewGroup giftcardslistview,ViewGroup giftcardsredeem) {

		this.ctx = context;			
		this.mVerificationCode=verificationcode;
		this.mGiftCardsListViewParent = giftcardslistview;
		this.mGiftCardsRedeemParent = giftcardsredeem;
		this.mMenuBarGiftCards = menubargiftcards;
		this.mMenuBarRedeem = menubarredeem;
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
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
		String result ="";		
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){					
				mGetResponse = zouponswebservice.mRedeemGiftCard(UserDetails.mServiceUserId,params[0]);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParseResponse= parsingclass.mParseRedeenGiftResponse(mGetResponse);
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
			if(REED_GIFT_CARD_MESSAGE.equalsIgnoreCase("Success")){
				Log.i(TAG, "Redeem Gift Card Success");
				alertBox_service("Information", "Successfully this giftcard send to your wallet");
			}else{
				Log.i(TAG, "Failure");
				alertBox_service("Information", "Unfortunately your verfication code is not successfully send.Please try again later.");
			}
		}	
	}

	private void alertBox_service(String title, final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equals("Successfully this giftcard send to your wallet")){
					mMenuBarGiftCards.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mMenuBarRedeem.setBackgroundResource(R.drawable.header_2);
					if(mGiftCardsListViewParent.getVisibility()!=View.VISIBLE){
						mGiftCardsListViewParent.setVisibility(View.VISIBLE);
						mGiftCardsRedeemParent.setVisibility(View.GONE);
					}
				}else if(msg.equals("Unfortunately your verfication code is not successfully send.Please try again later.")){
					mVerificationCode.setText("");
				}
			}
		});
		service_alert.show();
	}
}
