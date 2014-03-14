package com.us.zoupons.storeowner.giftcards_deals;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.CardOnFiles_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.mobilepay.CheckPINClassVariables;
import com.us.zoupons.mobilepay.PaymentStatusVariables;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to communicate with server to check user PIN 
 *
 */


public class SecurityPinCheckTask extends AsyncTask<String, String, String>{

	private StoreOwnerGiftcardSell mContext;
	private NetworkCheck mConnectionAvailabilityChecking=null;
	private ZouponsWebService zouponswebservice=null;
	private ZouponsParsingClass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String TAG="StoreOwnerInvoiceAsynchTask",mCustomerId,mSelectedCreditCardId,mGiftcardId,mCardValue,mFaceValue,mCardType;
	private LinearLayout mEmployeePinLayout,mCustomerPinLayout;
	private boolean mIsForCreditcardList=false,mIsForProcesspayment=false,mIsForNonActiveZouponsCustomer;

    // Constructor to validate employee pin
	public SecurityPinCheckTask(StoreOwnerGiftcardSell context,LinearLayout EmployeePinLayout,LinearLayout CustomerPinLayout,boolean isForNonActiveCustomer,String empty) {
		this.mContext = context;
		this.mEmployeePinLayout = EmployeePinLayout;
		this.mCustomerPinLayout = CustomerPinLayout;
		this.mIsForNonActiveZouponsCustomer = isForNonActiveCustomer;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		mIsForCreditcardList=false; // both false since check for storeowner/employee pin check only
		mIsForProcesspayment=false;
	}
	
	// Constructor to validate customer pin and get credit card list of customer
	public SecurityPinCheckTask(StoreOwnerGiftcardSell context,LinearLayout CustomerPinLayout,LinearLayout ChooseCardLayout,boolean isForCreditcardList) {
		this.mContext = context;
		this.mCustomerPinLayout = CustomerPinLayout;
		this.mIsForCreditcardList=isForCreditcardList;
		this.mIsForProcesspayment= false;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	// Constructor to validate customer pin and proceed the payment
	public SecurityPinCheckTask(StoreOwnerGiftcardSell context,LinearLayout CustomerPinLayout,LinearLayout ChooseCardLayout,String customer_id,String cardtype,String creditcardId,String giftcardId,String cardvalue,String facevalue) {
		this.mContext = context;
		this.mCustomerPinLayout = CustomerPinLayout;
		this.mSelectedCreditCardId = creditcardId;
		this.mGiftcardId = giftcardId;
		this.mCustomerId = customer_id;
		this.mCardValue = cardvalue;
		this.mFaceValue = facevalue; 
		this.mCardType = cardtype;
		this.mIsForProcesspayment = true;
		this.mIsForCreditcardList = true;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="",mGetResponse="",mParsingResponse="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				String mUser_Id = mPrefs.getString("user_id", "");
				String mUserType = mPrefs.getString("user_type", "");
				String mStoreId = mPrefs.getString("store_id", "");
				String mStoreLocationId = mPrefs.getString("location_id", "");
				if(!mIsForCreditcardList){ // For validating employee PIN
					mGetResponse = zouponswebservice.checkUserPIN(params[0],mUser_Id,mUserType);
				}else{ // For validating Customer PIN
					mGetResponse = zouponswebservice.checkUserPIN(params[0],params[1],params[2]);
				}
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse = parsingclass.parseCheckPIN(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						if(CheckPINClassVariables.message.equalsIgnoreCase("Success")){
							if(!mIsForCreditcardList && !mIsForProcesspayment){ // For validating employee PIN
								result="success";
							}else{
								if(mIsForCreditcardList && !mIsForProcesspayment){ // For calling customer credit card list
									mGetResponse=zouponswebservice.cardOnFiles(params[1],"Customer","customer_creditcards");
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
								}else{ // For processing payment...
									mGetResponse=zouponswebservice.purchaseGiftcard(mCustomerId,"shopper",mSelectedCreditCardId,mGiftcardId,mCardValue,"","","","","","","",mStoreId,mCardType,mStoreLocationId,mFaceValue);
									if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
										mParsingResponse=parsingclass.parsePurcahseGiftcardPaymentDetails(mGetResponse);
										if(mParsingResponse.equalsIgnoreCase("success")){
											result="success";
										}else if(mParsingResponse.equalsIgnoreCase("failure")){
											Log.i(TAG,"Error");
											result="Response Error.";
										}
									}else{
										result="Response Error.";
									}
								}
							}
						}else{
							result = "Invalid PIN";
						}
					}else{
						result = "Response Error.";
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
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}
		Log.i("Task", "onPOstExecute");		
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equals("Invalid PIN")){
			alertBox_service("Information", "Entered PIN is incorrect, Please enter correct PIN to proceed the payment");
		}else if(result.equals("No Cards Available")){
			mContext.updateCardLayout(false);
		}else{
			if(!mIsForCreditcardList && mEmployeePinLayout != null){  // To validate employee Pin 
			   	if(mIsForNonActiveZouponsCustomer){
					mContext.PurchaseCardForNewCustomer();
				}else{
					 mEmployeePinLayout.setVisibility(View.GONE);
					alertBox_service("Information", "Please hand phone to customer for payment.Please confirm from asking customer for pin");
				}
			    		
			}else if(mIsForCreditcardList && !mIsForProcesspayment){ // To validate customer pin and get customer credit card list
				mContext.updateCardLayout(true);
			}else{ // To validate customer pin and proceed the payment
				if(PaymentStatusVariables.message.equalsIgnoreCase("Payment Completed")){
					if(PaymentStatusVariables.purchase_message.equalsIgnoreCase("Giftcard Added to your Wallet")){
						alertBox_service("Information", "Thank you for using zoupons.Your wallet is now securely closed and giftcard added to your wallet");	
					}else{
						alertBox_service("Information", "Thank you for using zoupons.Your wallet is now securely closed and Dealcard added to your wallet");
					}
				}else{
					alertBox_service("Information", PaymentStatusVariables.message);
				}
			}
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	// To show alert box with respective message
	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setCancelable(false);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
              if(msg.equalsIgnoreCase("Please hand phone to customer for payment.Please confirm from asking customer for pin")){
            	  mCustomerPinLayout.setVisibility(View.VISIBLE);
              }else if(msg.equalsIgnoreCase("Thank you for using zoupons.Your wallet is now securely closed and giftcard added to your wallet")){
            	  Intent intent_rightmenugiftcards = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerGiftCards.class);
            	  intent_rightmenugiftcards.putExtra("classname", "StoreOwnerGiftCards");  
				  mContext.startActivity(intent_rightmenugiftcards);
				  mContext.finish();
              }else if(msg.equalsIgnoreCase("Thank you for using zoupons.Your wallet is now securely closed and Dealcard added to your wallet")){
            	  Intent intent_rightmenugiftcards = new Intent().setClass(mContext.getApplicationContext(),StoreOwnerGiftCards.class);
           		  intent_rightmenugiftcards.putExtra("classname", "StoreOwnerDealCards");
				  mContext.startActivity(intent_rightmenugiftcards);
				  mContext.finish();
              }
			}
		});
		service_alert.show();
	}
}



