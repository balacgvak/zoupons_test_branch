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
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.us.zoupons.Location;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.SlidingView;
import com.us.zoupons.ClassVariables.AddFavorite_ClassVariables;
import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

/**
 * Thread class to set the corresponding store to add favorite or remove favorite 
 */
public class CheckFavoriteAsynchThread extends AsyncTask<String, String, String>{

	Context ctx;
	String TAG="CheckFavoriteAsynchThread";
	private ProgressDialog progressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	String pageFlag;	//flag to differentiate homepage and mainmenu page call

	String IsLogoAvailable="",mLogoPathValue="";
	String mStoreId="",mUserId="";

	public CheckFavoriteAsynchThread(Context context){
		this.ctx=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	public CheckFavoriteAsynchThread(Context context, String logoPath,
			ImageView mStoreImage_RightMenu, String status) {
		this.ctx=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);

		this.mLogoPathValue = logoPath;
		this.IsLogoAvailable = status;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
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
		String result="";
		try{
			pageFlag=params[2];
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				mStoreId=params[0];
				mUserId=params[1];
				int width=170;
				int height=60;
				mGetResponse=zouponswebservice.checkFavorites(params[0], params[1]);	//check

				if(IsLogoAvailable.equalsIgnoreCase("ShowLogo")){
					if(!mLogoPathValue.equals("")){						
					}else{
						Log.i(TAG, "NO Image Available");
					}
				}

				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=parsingclass.parseCheckFavoriteXmlData(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						for(int i=0;i<WebServiceStaticArrays.mStoresCheckFavorite.size();i++){
							final AddFavorite_ClassVariables parsedobjectvalues = (AddFavorite_ClassVariables) WebServiceStaticArrays.mStoresCheckFavorite.get(i);
							if(!parsedobjectvalues.mMessage.equals("")){
								//result="No Stores Available.";
								result =parsedobjectvalues.mMessage;
							}
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
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Stores Available.")){
			alertBox_service("Information", result);
		}else {
			Log.i(TAG,"Success: "+result);
			if(result.equals("Yes")){
				// Assigning for use it in other right menu items
				RightMenuStoreId_ClassVariables.rightmenu_favourite_status = "yes";
				if(pageFlag.equals("location")){
					for(int i=0;i<WebServiceStaticArrays.mStoreLocatorList.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(i);
						
						if(obj.location_id==RightMenuStoreId_ClassVariables.mLocationId){
							obj.favorite_store="yes";
							WebServiceStaticArrays.mStoreLocatorList.set(i, obj);
						}
					}
					Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
				}else if(pageFlag.equals("homepage")){
					
					for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
						if(obj.location_id==RightMenuStoreId_ClassVariables.mLocationId){
							obj.favorite_store="yes";
							WebServiceStaticArrays.mStoreLocatorList.set(i, obj);
						}
					}
					
					SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
				}else{
					for(int i=0;i<WebServiceStaticArrays.mStaticStoreInfo.size();i++){
						POJOStoreInfo obj = (POJOStoreInfo) WebServiceStaticArrays.mStaticStoreInfo.get(i);
						if(obj.location_id==RightMenuStoreId_ClassVariables.mLocationId){
							obj.favorite_store="yes";
							WebServiceStaticArrays.mStaticStoreInfo.set(i, obj.favorite_store);
						}
					}
					
					MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
				}
			}else if(result.equals("No")){
				//Assigning for use it in other right menu items
				RightMenuStoreId_ClassVariables.rightmenu_favourite_status = "no";
				if(pageFlag.equals("location")){
					
					for(int i=0;i<WebServiceStaticArrays.mStoreLocatorList.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(i);
						if(obj.location_id==RightMenuStoreId_ClassVariables.mLocationId){
							obj.favorite_store="no";
							WebServiceStaticArrays.mStoreLocatorList.set(i, obj);
						}
					}
					
					Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
				}else if(pageFlag.equals("homepage")){
					for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
						if(obj.location_id==RightMenuStoreId_ClassVariables.mLocationId){
							obj.favorite_store="no";
							WebServiceStaticArrays.mStoreLocatorList.set(i, obj);
						}
					}
					
					SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
				}else{
					
					for(int i=0;i<WebServiceStaticArrays.mStaticStoreInfo.size();i++){
						POJOStoreInfo obj = (POJOStoreInfo) WebServiceStaticArrays.mStaticStoreInfo.get(i);
						if(obj.location_id==RightMenuStoreId_ClassVariables.mLocationId){
							obj.favorite_store="no";
							WebServiceStaticArrays.mStaticStoreInfo.set(i, obj.favorite_store);
						}
					}
					
					MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
				}
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