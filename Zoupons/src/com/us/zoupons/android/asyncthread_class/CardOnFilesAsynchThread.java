/**
 * 
 */
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

import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.CardOnFiles_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.AccountLoginFlag;
import com.us.zoupons.mobilepay.MobilePay;
import com.us.zoupons.shopper.wallet.ManageWallets;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * Asynch Thread to load card on files data into listview.
 *
 */
public class CardOnFilesAsynchThread extends AsyncTask<String, String, String>{

	private Context mContext;
	public NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse,mEventFlag="";
	private ProgressDialog progressdialog=null;
	private ListView listview=null;
	
	//Constructor for manageCards
	public CardOnFilesAsynchThread(Context context,ListView lv,LinearLayout editPin,String eventFlag){
		this.mContext=context;
		this.listview=lv;
		this.mEventFlag = eventFlag;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(false);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	//Constructor for zpaystep2 validation
	public CardOnFilesAsynchThread(Context context,String eventFlag){
		this.mContext=context;
		this.mEventFlag = eventFlag;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
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
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog.setMessage("Loading...");
		progressdialog.show();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				if(params[0] != null && params[0].equalsIgnoreCase("point_of_sale")){ // From storeowner_point of sale
					mGetResponse=mZouponswebservice.cardOnFiles(params[1],"Customer",mEventFlag);	
				}else{
					mGetResponse=mZouponswebservice.cardOnFiles(UserDetails.mServiceUserId,AccountLoginFlag.accountUserTypeflag,mEventFlag);
				}
					
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=mParsingclass.parseCard_On_Files(mGetResponse);
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
						result="Response Error.";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						result="No Cards Available";
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
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Cards Available")){
			if(this.listview!=null){
				alertBox_service("Information", result);
				WebServiceStaticArrays.mCardOnFiles.clear();
				MenuUtilityClass.cardsOnFilesListView(this.mContext, listview,mEventFlag);
			}else{
				MobilePay.ViewHandler();
			}
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			if(this.listview!=null){
				if(mContext.getClass().getSimpleName().equalsIgnoreCase("ManageWallets")){ // For manage cards
					if(ManageWallets.mManageCards_CardListContainer.getVisibility()!=View.VISIBLE){
						ManageWallets.mManageCards_CardListContainer.setVisibility(View.VISIBLE);
					}
					MenuUtilityClass.cardsOnFilesListView(this.mContext, listview,mEventFlag);	
				}else{ // for store owner billing 
					MenuUtilityClass.cardsOnFilesListView(this.mContext, listview,mEventFlag);
				}
			}else{
				MobilePay.ViewHandler();
			}
		}
		progressdialog.dismiss();
		
	}

	/* To show alert pop up with respective message */
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
