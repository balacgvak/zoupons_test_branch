package com.us.zoupons.android.AsyncThreadClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.ManageCards;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class RemoveCardAsynchThread extends AsyncTask<String, String, String>{

	String TAG="EditPinAsynchThread";
	Context ctx;
	public NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse,mEventFlag;
	private ProgressDialog progressdialog=null;
	private ListView mManageCardsListView;
	private LinearLayout ManageCards_MenuBarEditPin;
	
	
	public RemoveCardAsynchThread(Context context,ListView lv,LinearLayout editpin,String eventFlag){
		this.ctx=context;
		this.mManageCardsListView=lv;
		this.ManageCards_MenuBarEditPin=editpin;
		this.mEventFlag = eventFlag;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(false);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				mGetResponse=zouponswebservice.RemoveCard(params[0]);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=parsingclass.parseRemoveCard(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("Removed")){
						result="Credit Card removed successfully";
					}else if(mParsingResponse.equalsIgnoreCase("Failure")){
						result="Problem in credit card remove.Please Try again later.";
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						Log.i(TAG,"Error");
						result="Response Error.";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						Log.i(TAG,"No Records");
						result="Problem in credit card remove.Please Try again later.";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			Log.i(TAG,"Thread Error");
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
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equals("Credit Card removed successfully")){
			alertBox_service("Information", result);
		}else if(result.equals("Problem in credit card remove.Please Try again later.")){
			alertBox_service("Information", result);
		}
		progressdialog.dismiss();
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);

		//Start a status dialog
		progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equals("Credit Card removed successfully")){
					ManageCards.mManageCards_LoginCredentialsContainer.setVisibility(View.GONE);
					new CardOnFilesAsynchThread(ctx, mManageCardsListView,ManageCards_MenuBarEditPin,mEventFlag).execute("","","");
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}