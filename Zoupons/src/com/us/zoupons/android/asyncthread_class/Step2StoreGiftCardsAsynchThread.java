package com.us.zoupons.android.asyncthread_class;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.AccountLoginFlag;
import com.us.zoupons.mobilepay.MyGiftCards_ClassVariables;
import com.us.zoupons.shopper.giftcards_deals.POJOLimit;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous thread to call webservice to fetch giftcards
 *
 */

public class Step2StoreGiftCardsAsynchThread extends AsyncTask<String, String, String>{

	private Context mContext;
	private String mNavigationFlag;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse,mLocationId,mUserId,mCustomerFromPOSUserId="",mCustomerFromPOSUserType="";
	private ProgressDialog mProgressdialog=null;
	private boolean hasGiftCardId; 
	
	//Constructor for manageCards
	public Step2StoreGiftCardsAsynchThread(Context context,String locationId,String userId,String storeId,String navigationflag,boolean hasgiftcardid){
		this.mContext=context;
		this.mLocationId = locationId;
		this.mUserId=userId;
		this.mNavigationFlag = navigationflag;
		this.hasGiftCardId=hasgiftcardid;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	
	//Constructor from point of sale to step2
	public Step2StoreGiftCardsAsynchThread(Context context,String locationId,String userId,String storeId,String navigationflag,boolean hasgiftcardid,String usertype){
		this.mContext=context;
		this.mLocationId = locationId;
		this.mCustomerFromPOSUserId=userId;
		this.mNavigationFlag = navigationflag;
		this.hasGiftCardId=hasgiftcardid;
		this.mCustomerFromPOSUserType = usertype;
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
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressdialog.setMessage("Loading...");
		mProgressdialog.show();
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				if(mNavigationFlag.equalsIgnoreCase("invoice_approval")||mNavigationFlag.equalsIgnoreCase("invoice_source")||mNavigationFlag.equalsIgnoreCase("normal_payment")||(mNavigationFlag.equalsIgnoreCase("zpay")&&hasGiftCardId==true)|| mNavigationFlag.equalsIgnoreCase("point_of_sale")){
					if(mNavigationFlag.equalsIgnoreCase("point_of_sale")){
						mGetResponse=mZouponswebservice.myGiftCards(mLocationId,mCustomerFromPOSUserId,mCustomerFromPOSUserType);
					}else{
						mGetResponse=mZouponswebservice.myGiftCards(mLocationId,UserDetails.mServiceUserId,AccountLoginFlag.accountUserTypeflag);
					}
				}else{//condition true if sLeftmenu or sRightmenu mobilepay
					mGetResponse=mZouponswebservice.mGetAllGiftCards(mUserId,POJOLimit.mStartLimit);
				}
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					if(mNavigationFlag.equalsIgnoreCase("invoice_approval")||mNavigationFlag.equalsIgnoreCase("invoice_source")||mNavigationFlag.equalsIgnoreCase("normal_payment")||(mNavigationFlag.equalsIgnoreCase("zpay")&&hasGiftCardId==true) || mNavigationFlag.equalsIgnoreCase("point_of_sale")){
						mParsingResponse=mParsingclass.parseMy_Gift_cards(mGetResponse);
					}else{
						WebServiceStaticArrays.mAllGiftCardList.clear();	
						mParsingResponse= mParsingclass.mParseAllGiftCardResponse(mGetResponse);
					}
					if(mParsingResponse.equalsIgnoreCase("success")){
						if(mNavigationFlag.equalsIgnoreCase("invoice_approval")||mNavigationFlag.equalsIgnoreCase("invoice_source")||mNavigationFlag.equalsIgnoreCase("normal_payment")||(mNavigationFlag.equalsIgnoreCase("zpay")&&hasGiftCardId==true)|| mNavigationFlag.equalsIgnoreCase("point_of_sale")){
							for(int i=0;i<WebServiceStaticArrays.mMyGiftCards.size();i++){
								final MyGiftCards_ClassVariables parsedobjectvalues = (MyGiftCards_ClassVariables) WebServiceStaticArrays.mMyGiftCards.get(i);
								if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("Search type or term not available !")*/){
									/*result ="No Cards Available";*/
									result = parsedobjectvalues.message;
								}else{
									result="success";
								}
							}
						}else{
							result="success";
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
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mProgressdialog.dismiss();
		if(result.equals("nonetwork")){
			Toast.makeText(this.mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			Toast.makeText(this.mContext, "Unable to reach service.", Toast.LENGTH_SHORT).show();
		}/*else if(result.equals("No Cards Available")){
			Toast.makeText(this.context, result, Toast.LENGTH_SHORT).show();
		}*/else if(result.equals("Thread Error")){
			Toast.makeText(this.mContext, "Unable to process.", Toast.LENGTH_SHORT).show();
		}
		
		if(mNavigationFlag.equalsIgnoreCase("point_of_sale")){ // IF from Store module Point of sale
			//Start Credit Card availability check asynchthread
			CardOnFilesAsynchThread cardonfilesasynchthread = new CardOnFilesAsynchThread(this.mContext,"customer_creditcards");
			cardonfilesasynchthread.execute("point_of_sale",mCustomerFromPOSUserId,mCustomerFromPOSUserType);
		}else{
			//Start Credit Card availability check asynchthread
			CardOnFilesAsynchThread cardonfilesasynchthread = new CardOnFilesAsynchThread(this.mContext,"customer_creditcards");
			cardonfilesasynchthread.execute("","","");	
		}
		
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

}
