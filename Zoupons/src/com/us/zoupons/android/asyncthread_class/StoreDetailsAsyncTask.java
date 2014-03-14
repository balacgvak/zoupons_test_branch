package com.us.zoupons.android.asyncthread_class;


import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.giftcards_deals.StoreOwnerGiftCards;

/**
 * 
 * Asynchronous task to communicate with webservice to add store photo and fetch gc/dc list
 *
 */

public class StoreDetailsAsyncTask extends AsyncTask<String, String, String>{

	private Context mContext;
	private ProgressDialog mProgressdialog;
	private ZouponsParsingClass mParsingclass;
	private ZouponsWebService mZouponsWebService;
	public NetworkCheck mConnectionAvailabilityChecking;
	private String mResponseStatus,mServiceFunction;
	//To Dynamic List Update
	private String mCheckAndRefresh ="";
	private String mProgressStatus = "";
	private File mPhotoFile;

	public StoreDetailsAsyncTask(Context context,String Service,ListView carddetailsList,String ProgressStatus) {
		this.mContext = context;
		this.mServiceFunction = Service;
		this.mProgressStatus = ProgressStatus;
		mZouponsWebService = new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(context);
		mConnectionAvailabilityChecking= new NetworkCheck();
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}

	public StoreDetailsAsyncTask(Context context,String Service,ListView carddetailsList,String ProgressStatus, File photofile) {
		this.mContext = context;
		this.mServiceFunction = Service;
		this.mProgressStatus = ProgressStatus;
		mZouponsWebService = new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(context);
		mConnectionAvailabilityChecking= new NetworkCheck();
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		this.mPhotoFile = photofile;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		}else{
		}
		//Used To Check If Already running or not
		MainMenuActivity.mIsLoadMore = true;
	}

	@Override
	protected String doInBackground(String... params) {
		mCheckAndRefresh = params[0];
		String mResponse="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				if(mServiceFunction.equalsIgnoreCase("AddPhoto")){
					if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_Photos")){
						SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
						String mStoreLocation_id = mPrefs.getString("location_id", "");
						String mStoreId = mPrefs.getString("store_id", "");
						mResponse = mZouponsWebService.AddStorePhoto_Customer(mStoreId,mStoreLocation_id,this.mPhotoFile);	
					}else{
						mResponse = mZouponsWebService.AddStorePhoto_Customer(RightMenuStoreId_ClassVariables.mStoreID,RightMenuStoreId_ClassVariables.mLocationId,this.mPhotoFile);
					}
					if(!mResponse.equalsIgnoreCase("noresponse") && !mResponse.equalsIgnoreCase("failure")){
						mResponseStatus = mParsingclass.parseAddStorePhotoResponse(mResponse);
						if(!mResponseStatus.equals("failure") && !mResponseStatus.equals("norecords")){
							mResponseStatus = "success";
						}else if(mResponseStatus.equalsIgnoreCase("failure")){
							mResponseStatus = "failure";
						}else if(mResponseStatus.equalsIgnoreCase("norecords")){
							mResponseStatus="norecords";
						}
					}else {
						mResponseStatus="failure";
					}
				}else{
					if(mServiceFunction.equalsIgnoreCase("Regular")){
						POJOMainMenuActivityTAG.TAG="MainMenuActivity_GiftCardDetails";
						if(MainMenuActivity.mGiftCardStart.equalsIgnoreCase("0")){
							WebServiceStaticArrays.mStoreRegularCardDetails.clear();
						}
						if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwnerGiftCards")){
							SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
							String mStore_id = mPrefs.getString("store_id", "");
							mResponse= mZouponsWebService.GetStoreCardDetails(mStore_id, mServiceFunction,MainMenuActivity.mGiftCardStart);	
						}else{
							mResponse= mZouponsWebService.GetStoreCardDetails(RightMenuStoreId_ClassVariables.mStoreID, mServiceFunction,MainMenuActivity.mGiftCardStart);
						}

					}else{
						POJOMainMenuActivityTAG.TAG="MainMenuActivity_deals";
						if(MainMenuActivity.mDealStart.equalsIgnoreCase("0")){
							WebServiceStaticArrays.mStoreZouponsDealCardDetails.clear();
						}
						if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwnerGiftCards")){
							SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
							String mStore_id = mPrefs.getString("store_id", "");
							mResponse= mZouponsWebService.GetStoreCardDetails(mStore_id, mServiceFunction,MainMenuActivity.mDealStart);
						}else{
							mResponse= mZouponsWebService.GetStoreCardDetails(RightMenuStoreId_ClassVariables.mStoreID, mServiceFunction,MainMenuActivity.mDealStart);
						}

					}
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){
						mResponseStatus = mParsingclass.parseCardDetails(mResponse,mServiceFunction);
						if(!mResponseStatus.equals("failure") && !mResponseStatus.equals("norecords")){
							mResponseStatus = "success";
						}else if(mResponseStatus.equalsIgnoreCase("failure")){
							mResponseStatus = "failure";
						}else if(mResponseStatus.equalsIgnoreCase("norecords")){
							mResponseStatus="norecords";
						}
					}else{
						mResponseStatus = "failure";	
					}
				}
			}else{
				mResponseStatus="nonetwork";
			}
		}catch(Exception e){
			mResponseStatus ="failure";
		}
		return mResponseStatus;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		MainMenuActivity.mIsLoadMore = false;
		if(mProgressdialog != null && mProgressdialog.isShowing()){
			mProgressdialog.dismiss();
		}
		if(result.equalsIgnoreCase("success") && !mServiceFunction.equalsIgnoreCase("AddPhoto")){
			if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwnerGiftCards")){
				if(mServiceFunction.equalsIgnoreCase("Regular")){
					//Set List Adapter Here
					((StoreOwnerGiftCards) mContext).SetArrayStoreOwnerGiftCardListAdatpter(mServiceFunction,mCheckAndRefresh);

					MainMenuActivity.mGiftCardStart = MainMenuActivity.mGiftCardEndLimit;
					MainMenuActivity.mGiftCardEndLimit = String.valueOf(Integer.parseInt(MainMenuActivity.mGiftCardEndLimit)+20);
				}else{
					((StoreOwnerGiftCards) mContext).SetArrayStoreOwnerGiftCardListAdatpter(mServiceFunction,mCheckAndRefresh);
					MainMenuActivity.mDealStart = MainMenuActivity.mDealEndLimit;
					MainMenuActivity.mDealEndLimit = String.valueOf(Integer.parseInt(MainMenuActivity.mDealEndLimit)+20);
				}	
			}else{
				if(mServiceFunction.equalsIgnoreCase("Regular")){
					//Set List Adapter Here
					((MainMenuActivity) mContext).SetArrayGiftCardDealListAdatpter(mServiceFunction,WebServiceStaticArrays.mStoreRegularCardDetails,mCheckAndRefresh);

					MainMenuActivity.mGiftCardStart = MainMenuActivity.mGiftCardEndLimit;
					MainMenuActivity.mGiftCardEndLimit = String.valueOf(Integer.parseInt(MainMenuActivity.mGiftCardEndLimit)+20);
				}else{
					((MainMenuActivity) mContext).SetArrayGiftCardDealListAdatpter(mServiceFunction,WebServiceStaticArrays.mStoreRegularCardDetails,mCheckAndRefresh);
					MainMenuActivity.mDealStart = MainMenuActivity.mDealEndLimit;
					MainMenuActivity.mDealEndLimit = String.valueOf(Integer.parseInt(MainMenuActivity.mDealEndLimit)+20);
				}	
			}
		}else if(result.equalsIgnoreCase("success") && mServiceFunction.equalsIgnoreCase("AddPhoto")){
			alertBox_service("Information", "Photo has been successfully submitted to Zoupons for review");
		}else if(!result.equalsIgnoreCase("success") && mServiceFunction.equalsIgnoreCase("AddPhoto")){
			alertBox_service("Information", "Problem in loading image to the store, Please try again later.");
		}else if(result.equalsIgnoreCase("failure")||result.equalsIgnoreCase("noresponse")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equalsIgnoreCase("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equalsIgnoreCase("norecords")){
			alertBox_service("Information", "No Cards Available.");
		}
	}

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
