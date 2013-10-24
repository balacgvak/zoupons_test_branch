package com.us.zoupons.android.AsyncThreadClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.FlagClasses.AccountLoginFlag;
import com.us.zoupons.GiftCards.POJOLimit;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.zpay.MyGiftCards_ClassVariables;

public class Step2StoreGiftCardsAsynchThread extends AsyncTask<String, String, String>{

	Context context;
	String TAG="SearchAsynchThread";
	String mNavigationFlag;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse,mLocationId,mUserId,mStoreId,mCustomerFromPOSUserId="",mCustomerFromPOSUserType="";
	private ProgressDialog progressdialog=null;
	private boolean hasGiftCardId; 
	
	//Constructor for manageCards
	public Step2StoreGiftCardsAsynchThread(Context context,String locationId,String userId,String storeId,String navigationflag,boolean hasgiftcardid){
		this.context=context;
		this.mLocationId = locationId;
		this.mUserId=userId;
		this.mStoreId=storeId;
		this.mNavigationFlag = navigationflag;
		this.hasGiftCardId=hasgiftcardid;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.context);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	//Constructor from point of sale to step2
	public Step2StoreGiftCardsAsynchThread(Context context,String locationId,String userId,String storeId,String navigationflag,boolean hasgiftcardid,String usertype){
		this.context=context;
		this.mLocationId = locationId;
		this.mCustomerFromPOSUserId=userId;
		this.mStoreId=storeId;
		this.mNavigationFlag = navigationflag;
		this.hasGiftCardId=hasgiftcardid;
		this.mCustomerFromPOSUserType = usertype;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.context);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(context)){

				if(mNavigationFlag.equalsIgnoreCase("invoice_approval")||mNavigationFlag.equalsIgnoreCase("invoice_source")||mNavigationFlag.equalsIgnoreCase("normal_payment")||(mNavigationFlag.equalsIgnoreCase("zpay")&&hasGiftCardId==true)|| mNavigationFlag.equalsIgnoreCase("point_of_sale")){
					if(mNavigationFlag.equalsIgnoreCase("point_of_sale")){
						mGetResponse=zouponswebservice.myGiftCards(mLocationId,mCustomerFromPOSUserId,mCustomerFromPOSUserType);
					}else{
						mGetResponse=zouponswebservice.myGiftCards(mLocationId,UserDetails.mServiceUserId,AccountLoginFlag.accountUserTypeflag);
					}
				}else{//condition true if leftmenu or rightmenu mobilepay
					mGetResponse=zouponswebservice.mGetAllGiftCards(mUserId,POJOLimit.mStartLimit);
				}

				
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){

					if(mNavigationFlag.equalsIgnoreCase("invoice_approval")||mNavigationFlag.equalsIgnoreCase("invoice_source")||mNavigationFlag.equalsIgnoreCase("normal_payment")||(mNavigationFlag.equalsIgnoreCase("zpay")&&hasGiftCardId==true) || mNavigationFlag.equalsIgnoreCase("point_of_sale")){
						mParsingResponse=parsingclass.parseMy_Gift_cards(mGetResponse);
					}else{
						WebServiceStaticArrays.mAllGiftCardList.clear();	
						mParsingResponse= parsingclass.mParseAllGiftCardResponse(mGetResponse);
					}

					if(mParsingResponse.equalsIgnoreCase("success")){
						if(mNavigationFlag.equalsIgnoreCase("invoice_approval")||mNavigationFlag.equalsIgnoreCase("invoice_source")||mNavigationFlag.equalsIgnoreCase("normal_payment")||(mNavigationFlag.equalsIgnoreCase("zpay")&&hasGiftCardId==true)|| mNavigationFlag.equalsIgnoreCase("point_of_sale")){
							Log.i(TAG,"MyGiftCards List Size: "+WebServiceStaticArrays.mMyGiftCards.size());
							for(int i=0;i<WebServiceStaticArrays.mMyGiftCards.size();i++){
								final MyGiftCards_ClassVariables parsedobjectvalues = (MyGiftCards_ClassVariables) WebServiceStaticArrays.mMyGiftCards.get(i);
								if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("Search type or term not available !")*/){
									Log.i(TAG,"message : "+parsedobjectvalues.message);
									/*result ="No Cards Available";*/
									result = parsedobjectvalues.message;
								}else{
									Log.i(TAG,"Id : "+parsedobjectvalues.id);
									Log.i(TAG,"facevalue : "+parsedobjectvalues.face_value);
									Log.i(TAG,"balanceamount : "+parsedobjectvalues.balance_amount);
									result="success";
								}
							}
						}else{
							result="success";
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
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		progressdialog.dismiss();
		Log.i(TAG,"result: "+result);
		if(result.equals("nonetwork")){
			Toast.makeText(this.context, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			Toast.makeText(this.context, "Unable to reach service.", Toast.LENGTH_SHORT).show();
		}/*else if(result.equals("No Cards Available")){
			Toast.makeText(this.context, result, Toast.LENGTH_SHORT).show();
		}*/else if(result.equals("Thread Error")){
			Toast.makeText(this.context, "Unable to process.", Toast.LENGTH_SHORT).show();
		}else if(result.equals("success")){
			Log.i(TAG,"Success: "+result);
		}else{
			//Toast.makeText(this.context, result, Toast.LENGTH_SHORT).show();
			Log.i(TAG,"No Cards : "+result);
		}
		if(mNavigationFlag.equalsIgnoreCase("point_of_sale")){
			//Start Credit Card availability check asynchthread
			CardOnFilesAsynchThread cardonfilesasynchthread = new CardOnFilesAsynchThread(this.context);
			cardonfilesasynchthread.execute("point_of_sale",mCustomerFromPOSUserId,mCustomerFromPOSUserType);
		}else{
			//Start Credit Card availability check asynchthread
			CardOnFilesAsynchThread cardonfilesasynchthread = new CardOnFilesAsynchThread(this.context);
			cardonfilesasynchthread.execute("","","");	
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		((Activity) context).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);

		//Start a status dialog
		progressdialog.setMessage("Loading...");
		progressdialog.show();
		
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

}
