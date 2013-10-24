package com.us.zoupons.android.AsyncThreadClasses;


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
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.storeowner.GiftCards.StoreOwnerGiftCards;

public class StoreDetailsAsyncTask extends AsyncTask<String, String, String>{

	private Context context;
	private ProgressDialog progressdialog;
	private ZouponsParsingClass parsingclass;
	private ZouponsWebService zouponsWebService;
	public NetworkCheck mConnectionAvailabilityChecking;
	private String mResponseStatus,mServiceFunction;
	private ListView mStoreCardDetailsList;
	//To Dynamic List Update
	String mCheckAndRefresh ="";
	String mProgressStatus = "";
	String mResponse; 
	File mPhotoFile;


	public StoreDetailsAsyncTask(Context context,String Service,ListView carddetailsList,String ProgressStatus) {
		this.context = context;
		this.mServiceFunction = Service;
		this.mStoreCardDetailsList = carddetailsList;
		this.mProgressStatus = ProgressStatus;
		zouponsWebService = new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(context);
		mConnectionAvailabilityChecking= new NetworkCheck();
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	public StoreDetailsAsyncTask(Context context,String Service,ListView carddetailsList,String ProgressStatus, File photofile) {
		Log.i("task", "constructor");
		this.context = context;
		this.mServiceFunction = Service;
		this.mStoreCardDetailsList = carddetailsList;
		this.mProgressStatus = ProgressStatus;
		zouponsWebService = new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(context);
		mConnectionAvailabilityChecking= new NetworkCheck();
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		this.mPhotoFile = photofile;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) context).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			progressdialog = ProgressDialog.show(context,"Loading...","Please Wait!",true);
		}else{
			Log.i("TAG", "Don't Show Progress here");
		}
		//Used To Check If Already running or not
		MainMenuActivity.mIsLoadMore = true;
	}

	@Override
	protected String doInBackground(String... params) {
		mCheckAndRefresh = params[0];
		String mResponse="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(context)){
				if(mServiceFunction.equalsIgnoreCase("AddPhoto")){
					if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_Photos")){
						SharedPreferences mPrefs = context.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
						String mStoreLocation_id = mPrefs.getString("location_id", "");
						String mStoreId = mPrefs.getString("store_id", "");
						mResponse = zouponsWebService.AddStorePhoto_Customer(mStoreId,mStoreLocation_id,this.mPhotoFile);	
					}else{
						//mResponse = zouponsWebService.AddStorePhoto(RightMenuStoreId_ClassVariables.mStoreID,RightMenuStoreId_ClassVariables.mLocationId);
						mResponse = zouponsWebService.AddStorePhoto_Customer(RightMenuStoreId_ClassVariables.mStoreID,RightMenuStoreId_ClassVariables.mLocationId,this.mPhotoFile);
					}
					if(!mResponse.equalsIgnoreCase("noresponse") && !mResponse.equalsIgnoreCase("failure")){
						mResponseStatus = parsingclass.parseAddStorePhotoResponse(mResponse);
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
					Log.i("task", "GiftCardDeal");		
					if(mServiceFunction.equalsIgnoreCase("Regular")){
						POJOMainMenuActivityTAG.TAG="MainMenuActivity_GiftCardDetails";
						if(MainMenuActivity.mGiftCardStart.equalsIgnoreCase("0")){
							WebServiceStaticArrays.mStoreRegularCardDetails.clear();
						}
						Log.i("Task", "GiftCard");
						if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwnerGiftCards")){
							SharedPreferences mPrefs = context.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
							String mStore_id = mPrefs.getString("store_id", "");
							mResponse= zouponsWebService.GetStoreCardDetails(mStore_id, mServiceFunction,MainMenuActivity.mGiftCardStart);	
						}else{
							mResponse= zouponsWebService.GetStoreCardDetails(RightMenuStoreId_ClassVariables.mStoreID, mServiceFunction,MainMenuActivity.mGiftCardStart);
						}

					}else{
						POJOMainMenuActivityTAG.TAG="MainMenuActivity_deals";
						if(MainMenuActivity.mDealStart.equalsIgnoreCase("0")){
							WebServiceStaticArrays.mStoreZouponsDealCardDetails.clear();
						}
						Log.i("Task", "Deal");
						if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwnerGiftCards")){
							SharedPreferences mPrefs = context.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
							String mStore_id = mPrefs.getString("store_id", "");
							mResponse= zouponsWebService.GetStoreCardDetails(mStore_id, mServiceFunction,MainMenuActivity.mDealStart);
						}else{
							mResponse= zouponsWebService.GetStoreCardDetails(RightMenuStoreId_ClassVariables.mStoreID, mServiceFunction,MainMenuActivity.mDealStart);
						}

					}
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){
						mResponseStatus = parsingclass.parseCardDetails(mResponse,mServiceFunction);
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
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}
		if(result.equalsIgnoreCase("success") && !mServiceFunction.equalsIgnoreCase("AddPhoto")){
			if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwnerGiftCards")){
				Log.i("List", "Set New Array List");
				if(mServiceFunction.equalsIgnoreCase("Regular")){
					//Set List Adapter Here
					((StoreOwnerGiftCards) context).SetArrayStoreOwnerGiftCardListAdatpter(mServiceFunction,mCheckAndRefresh);

					MainMenuActivity.mGiftCardStart = MainMenuActivity.mGiftCardEndLimit;
					MainMenuActivity.mGiftCardEndLimit = String.valueOf(Integer.parseInt(MainMenuActivity.mGiftCardEndLimit)+20);
				}else{
					((StoreOwnerGiftCards) context).SetArrayStoreOwnerGiftCardListAdatpter(mServiceFunction,mCheckAndRefresh);
					MainMenuActivity.mDealStart = MainMenuActivity.mDealEndLimit;
					MainMenuActivity.mDealEndLimit = String.valueOf(Integer.parseInt(MainMenuActivity.mDealEndLimit)+20);
				}	
			}else{
				Log.i("List", "Set New Array List");
				if(mServiceFunction.equalsIgnoreCase("Regular")){
					//Set List Adapter Here
					((MainMenuActivity) context).SetArrayGiftCardDealListAdatpter(mServiceFunction,WebServiceStaticArrays.mStoreRegularCardDetails,mCheckAndRefresh);

					MainMenuActivity.mGiftCardStart = MainMenuActivity.mGiftCardEndLimit;
					MainMenuActivity.mGiftCardEndLimit = String.valueOf(Integer.parseInt(MainMenuActivity.mGiftCardEndLimit)+20);
				}else{
					((MainMenuActivity) context).SetArrayGiftCardDealListAdatpter(mServiceFunction,WebServiceStaticArrays.mStoreRegularCardDetails,mCheckAndRefresh);
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
			Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equalsIgnoreCase("norecords")){
			alertBox_service("Information", "No Cards Available.");
		}
	}

	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.context);
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
