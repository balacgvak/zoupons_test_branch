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
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.AddFavorite_ClassVariables;
import com.us.zoupons.classvariables.POJOStoreInfo;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.StoreLocator_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.location.Location;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * Thread class to set the corresponding store to add favorite or remove favorite 
 */

public class CheckFavoriteAsynchThread extends AsyncTask<String, String, String>{

	private Context mContext;
	private ProgressDialog mProgressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse;
	private String pageFlag;	//flag to differentiate homepage and mainmenu page call
	
	public CheckFavoriteAsynchThread(Context context){
		this.mContext=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}

	public CheckFavoriteAsynchThread(Context context, String logoPath,
			ImageView mStoreImage_RightMenu, String status) {
		this.mContext=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
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
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			pageFlag=params[2];
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				mGetResponse=mZouponswebservice.checkFavorites(params[0], params[1]);	//check
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=mParsingclass.parseCheckFavoriteXmlData(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						for(int i=0;i<WebServiceStaticArrays.mStoresCheckFavorite.size();i++){
							final AddFavorite_ClassVariables parsedobjectvalues = (AddFavorite_ClassVariables) WebServiceStaticArrays.mStoresCheckFavorite.get(i);
							if(!parsedobjectvalues.mMessage.equals("")){
								//result="No Stores Available.";
								result =parsedobjectvalues.mMessage;
							}
						}
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						result="Response Error.";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						result="No stores Available.";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
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
		super.onPostExecute(result);
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No stores Available.")){
			alertBox_service("Information", result);
		}else {
			if(result.equals("Yes")){
				// Assigning for use it in other right menu items
				RightMenuStoreId_ClassVariables.rightmenu_favourite_status = "yes";
				if(pageFlag.equals("location")){
					for(int i=0;i<WebServiceStaticArrays.mStoreLocatorList.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(i);
						// Internally changing the favorite store value
						if(obj.location_id==RightMenuStoreId_ClassVariables.mLocationId){
							obj.favorite_store="yes";
							WebServiceStaticArrays.mStoreLocatorList.set(i, obj);
						}
					}
					Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
				}else if(pageFlag.equals("homepage")){
					// Internally changing the favorite store value
					for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
						if(obj.location_id==RightMenuStoreId_ClassVariables.mLocationId){
							obj.favorite_store="yes";
							WebServiceStaticArrays.mStoreLocatorList.set(i, obj);
						}
					}
					
					ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
				}else{
					// Internally changing the favorite store value
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
					// Internally changing the favorite store value
					for(int i=0;i<WebServiceStaticArrays.mStoreLocatorList.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(i);
						if(obj.location_id==RightMenuStoreId_ClassVariables.mLocationId){
							obj.favorite_store="no";
							WebServiceStaticArrays.mStoreLocatorList.set(i, obj);
						}
					}
					
					Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
				}else if(pageFlag.equals("homepage")){
					// Internally changing the favorite store value
					for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
						if(obj.location_id==RightMenuStoreId_ClassVariables.mLocationId){
							obj.favorite_store="no";
							WebServiceStaticArrays.mStoreLocatorList.set(i, obj);
						}
					}
					
					ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
				}else{
					// Internally changing the favorite store value
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
		mProgressdialog.dismiss();
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