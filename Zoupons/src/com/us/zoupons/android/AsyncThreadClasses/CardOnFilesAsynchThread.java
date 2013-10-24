/**
 * 
 */
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
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.CardOnFiles_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.FlagClasses.AccountLoginFlag;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.zpay.Step2_ManageCards;

/**
 * Asynch Thread to load card on files data into listview.
 *
 */
public class CardOnFilesAsynchThread extends AsyncTask<String, String, String>{

	String TAG="SearchAsynchThread";
	Context ctx;
	public NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse,mEventFlag="";
	private ProgressDialog progressdialog=null;
	private ListView listview=null;
	private LinearLayout ManageCards_MenuBarEditPin;
	
	//Constructor for manageCards
	public CardOnFilesAsynchThread(Context context,ListView lv,LinearLayout editPin,String eventFlag){
		this.ctx=context;
		this.listview=lv;
		this.ManageCards_MenuBarEditPin=editPin;
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
	
	//Constructor for zpaystep2 validation
	public CardOnFilesAsynchThread(Context context){
		this.ctx=context;
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
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);

		//Start a status dialog
		progressdialog.setMessage("Loading...");
		progressdialog.show();
		
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				if(params[0] != null && params[0].equalsIgnoreCase("point_of_sale")){ // From storeowner_point of sale
					mGetResponse=zouponswebservice.cardOnFiles(params[1],"Customer");	
				}else{
					mGetResponse=zouponswebservice.cardOnFiles(UserDetails.mServiceUserId,AccountLoginFlag.accountUserTypeflag);
				}
					
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=parsingclass.parseCard_On_Files(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						for(int i=0;i<WebServiceStaticArrays.mCardOnFiles.size();i++){
							final CardOnFiles_ClassVariables parsedobjectvalues = (CardOnFiles_ClassVariables) WebServiceStaticArrays.mCardOnFiles.get(i);
							if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("Search type or term not available !")*/){
								result ="No Cards Available";
							}else{
								
								result="success";
							}
						}
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						Log.i(TAG,"Error");
						result="Response Error.";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						Log.i(TAG,"No Records");
						result="No Cards Available";
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
	protected void onPostExecute(String result) {
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Cards Available")){
			if(this.listview!=null){
				alertBox_service("Information", result);
			}else{
				Step2_ManageCards.ViewHandler();
			}
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			if(this.listview!=null){
				if(ctx.getClass().getSimpleName().equalsIgnoreCase("ManageCards")){ // For manage cards
					if(ManageCards.mManageCards_CardListContainer.getVisibility()!=View.VISIBLE){
						ManageCards.mManageCards_CardListContainer.setVisibility(View.VISIBLE);
					}
					MenuUtilityClass.cardsOnFilesListView(this.ctx, listview,mEventFlag);	
				}else{ // for store owner billing 
					MenuUtilityClass.cardsOnFilesListView(this.ctx, listview,mEventFlag);
				}
			}else{
				Step2_ManageCards.ViewHandler();
			}
		}
		progressdialog.dismiss();
		super.onPostExecute(result);
	}

	public void alertBox_service(final String title,final String msg){
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
