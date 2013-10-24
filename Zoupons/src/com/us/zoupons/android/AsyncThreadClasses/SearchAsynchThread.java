/**
 * 
 */
package com.us.zoupons.android.AsyncThreadClasses;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.Search_ClassVariables;
import com.us.zoupons.StaticArrays.SearchStaticArrayList;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

/**
 * Class to load search store values from webservice
 */
public class SearchAsynchThread extends AsyncTask<String, String, String>{

	String TAG="SearchAsynchThread";
	Context ctx;
	public NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	AutoCompleteTextView mSearchBox;
	private ProgressDialog progressdialog=null;
	
	public SearchAsynchThread(Context context,AutoCompleteTextView searchbox){
		this.ctx=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		mSearchBox=searchbox;
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	@Override
	protected void onCancelled() {
		SearchStaticArrayList.list_storeid.clear();
		SearchStaticArrayList.list_storename.clear();
		SearchStaticArrayList.list_storelogo.clear();
		super.onCancelled();
	}

	@Override
	protected void onPreExecute() {
		SearchStaticArrayList.list_storeid.clear();
		SearchStaticArrayList.list_storename.clear();
		SearchStaticArrayList.list_storelogo.clear();
		
		Log.i(TAG,"onPreExecute");
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected String doInBackground(String... params) {
		Log.i(TAG,"doInBackground");
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				mGetResponse=zouponswebservice.search("store", params[0],"");	//check
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					if(mParsingResponse.equalsIgnoreCase("success")){
						Log.i(TAG,"SearchStore List Size: "+WebServiceStaticArrays.mSearchStore.size());
						Log.i(TAG,"isCancelled: "+isCancelled());
						if(!isCancelled()){
							for(int i=0;i<WebServiceStaticArrays.mSearchStore.size();i++){
								final Search_ClassVariables parsedobjectvalues = (Search_ClassVariables) WebServiceStaticArrays.mSearchStore.get(i);
								if(!parsedobjectvalues.message.equals("")&&parsedobjectvalues.message.equals("Search type or term not available !")){
									result="No Stores Available.";
								}else{
									SearchStaticArrayList.list_storeid.add(parsedobjectvalues.storeId);
									SearchStaticArrayList.list_storename.add(parsedobjectvalues.storeName);
									result="success";
								}
							}
						}else{
							SearchStaticArrayList.list_storeid.clear();
							SearchStaticArrayList.list_storename.clear();
							SearchStaticArrayList.list_storelogo.clear();
						}
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						Log.i(TAG,"Error");
						result="Response Error.";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						Log.i(TAG,"No Records");
						result="No Stores Available.";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			Log.i(TAG,"Thread Error");
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		Log.i(TAG,"onPostExecute");
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			Toast.makeText(ctx, "Unable to reach service.", Toast.LENGTH_SHORT).show();
		}else if(result.equals("No Stores Available.")){
			Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
		}else if(result.equals("success")){
			mSearchBox.setAdapter(new ArrayAdapter<String>(ctx, android.R.layout.simple_dropdown_item_1line,SearchStaticArrayList.list_storename));
			if(mSearchBox.getVisibility()==View.VISIBLE){
				mSearchBox.showDropDown();
			}else if(mSearchBox.getVisibility()==View.INVISIBLE||mSearchBox.getVisibility()==View.GONE){
				mSearchBox.dismissDropDown();
			}
		}
		super.onPostExecute(result);
	}
	
	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx.getApplicationContext());
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
