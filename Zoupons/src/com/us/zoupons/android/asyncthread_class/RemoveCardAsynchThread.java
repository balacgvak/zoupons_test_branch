package com.us.zoupons.android.asyncthread_class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.shopper.wallet.ManageWallets;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to communicate with webservice to remove credit card from user account
 *
 */

public class RemoveCardAsynchThread extends AsyncTask<String, String, String>{

	private Context mContext;
	private NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse,mEventFlag;
	private ProgressDialog mProgressdialog=null;
	private ListView mManageCardsListView;
	private LinearLayout mManageCards_MenuBarEditPin;
		
	public RemoveCardAsynchThread(Context context,ListView lv,LinearLayout editpin,String eventFlag){
		this.mContext=context;
		this.mManageCardsListView=lv;
		this.mManageCards_MenuBarEditPin=editpin;
		this.mEventFlag = eventFlag;
		mConnectionAvailabilityChecking= new NetworkCheck();
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
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				mGetResponse=mZouponswebservice.RemoveCard(params[0]);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=mParsingclass.parseRemoveCard(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("Removed")){
						result="Credit Card removed successfully";
					}else if(mParsingResponse.equalsIgnoreCase("Failure")){
						result="Problem in credit card remove.Please Try again later.";
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						result="Response Error.";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						result="Problem in credit card remove.Please Try again later.";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			result="Thread Error";
			e.printStackTrace();
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
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equals("Credit Card removed successfully")){
			alertBox_service("Information", result);
		}else if(result.equals("Problem in credit card remove.Please Try again later.")){
			alertBox_service("Information", result);
		}
		mProgressdialog.dismiss();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	/* To show alert pop up with respective message */
	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equals("Credit Card removed successfully")){
					ManageWallets.mManageCards_LoginCredentialsContainer.setVisibility(View.GONE);
					new CardOnFilesAsynchThread(mContext, mManageCardsListView,mManageCards_MenuBarEditPin,mEventFlag).execute("","","");
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}