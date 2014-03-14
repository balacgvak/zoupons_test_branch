package com.us.zoupons.mobilepay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.shopper.invoice.GetInvoiceListTask;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.pointofsale.ApprovePaymentUsingMobileNumber;
import com.us.zoupons.storeowner.pointofsale.StoreOwner_PointOfSale_Part1;

/**
 * 
 * Asynchronous thread to call webservice for proceeding normal payment [not for GC/dc purchase]
 *
 */

public class NormalPaymentAsyncTask extends AsyncTask<String, String, String>{

	private Context mContext;
	private String TAG="CheckFavoriteAsynchThread";
	private ProgressDialog mProgressdialog=null;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse=null,mParsingResponse,mPageFlag;	//flag to differentiate homepage and mainmenu page call
	private String mCreditcardId="",mChoosedGiftcardId="",mChoosedPurchaseId="",mStoreID="",mTipAmount="",mTotalAmount,mStoreLocationID="",mAmountOnGiftCard,mAmountOnCreditCard;
	private String mNote,mActualAmount;
	private ScrollView mStep3Container;
	private RelativeLayout mQRCodecontainer;
	private ImageView mQRCodeImage;
	public static CountDownTimer mCountDownTimer;
	/*private LinearLayout mHeaderLayout;*/
	private TextView mExpiryCountDownText/*,mStoreNameTextView*/;
	private ViewGroup mFooterLayout;
	private int mQRCodeImageWidth=0,mQRCodeImageheight=0;
	private String mCustomerId="";
	private ProgressBar mProgressBar;

	//For payment using CreditCard only
	public NormalPaymentAsyncTask(Context context,String SelectedCreditCardId, String StoreID, String TipAmount,String TotalAmount,String note,String actualamount,String storelocationid,String empty,ScrollView Step3Container,RelativeLayout QRCodecontainer,ImageView mQRCodeImage,/*LinearLayout HeaderLayout,*/TextView ExpiryCountDownText,ViewGroup mfooterMenu/*,TextView mStoreName*/,int imageviewwidth,int imageviewheight,ProgressBar progressbar) {
		this.mContext=context;
		this.mCreditcardId = SelectedCreditCardId;
		this.mStoreID = StoreID;
		this.mStoreLocationID = storelocationid;
		this.mTipAmount = TipAmount;
		this.mTotalAmount = TotalAmount;
		this.mNote = note;
		this.mActualAmount = actualamount;
		this.mExpiryCountDownText = ExpiryCountDownText;
		this.mStep3Container =Step3Container;
		this.mQRCodecontainer = QRCodecontainer;
		this.mQRCodeImage = mQRCodeImage;
		this.mFooterLayout = mfooterMenu;
		mQRCodeImageWidth=imageviewwidth;
		mQRCodeImageheight=imageviewheight;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		this.mProgressBar = progressbar;
	}

	//For Payment only giftcard
	public NormalPaymentAsyncTask(Context context,String ChoosedGiftcardID, String TipAmount, String TotalAmount,String ChoosedGiftCardPurchaseID,String emptyvalue,String note,String actualAmount,ScrollView Step3Container,RelativeLayout QRCodecontainer,ImageView mQRCodeImage,/*LinearLayout HeaderLayout,*/TextView ExpiryCountDownText,ViewGroup mfooterMenu/*,TextView mStoreName*/,int imageviewwidth,int imageviewheight,ProgressBar progressbar) {
		this.mContext=context;
		this.mChoosedGiftcardId = ChoosedGiftcardID;
		this.mChoosedPurchaseId = ChoosedGiftCardPurchaseID;
		this.mTipAmount = TipAmount;
		this.mTotalAmount = TotalAmount;
		this.mNote = note;
		this.mActualAmount = actualAmount;
		this.mStep3Container =Step3Container;
		this.mQRCodecontainer = QRCodecontainer;
		this.mQRCodeImage = mQRCodeImage;
		this.mExpiryCountDownText = ExpiryCountDownText;
		this.mFooterLayout = mfooterMenu;
		mQRCodeImageWidth=imageviewwidth;
		mQRCodeImageheight=imageviewheight;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		this.mProgressBar = progressbar;
	}

	//For payment both cards
	public NormalPaymentAsyncTask(Context context, String SelectedCreditCardId, String StoreID, String AmountonCreditcard, String ChoosedGiftcardID, String AmountOnGiftCard, String TipAmount,
			String TotalAmount, String ChoosedGiftCardPurchaseID, String note, String actualAmount,String storelocationid,ScrollView Step3Container,RelativeLayout QRCodecontainer,ImageView mQRCodeImage,/*LinearLayout HeaderLayout,*/TextView ExpiryCountDownText,ViewGroup mfooterMenu,/*TextView mStoreName,*/int imageviewwidth,int imageviewheight,ProgressBar progressbar) {
		this.mContext=context;
		this.mCreditcardId = SelectedCreditCardId;
		this.mStoreID = StoreID;
		this.mStoreLocationID = storelocationid;
		this.mAmountOnCreditCard = AmountonCreditcard;
		this.mChoosedGiftcardId = ChoosedGiftcardID;
		this.mChoosedPurchaseId = ChoosedGiftCardPurchaseID;
		this.mAmountOnGiftCard = AmountOnGiftCard;
		this.mTipAmount = TipAmount;
		this.mTotalAmount = TotalAmount;
		this.mNote = note;
		this.mActualAmount = actualAmount;
		this.mStep3Container =Step3Container;
		this.mQRCodecontainer = QRCodecontainer;
		this.mQRCodeImage = mQRCodeImage;
		this.mExpiryCountDownText = ExpiryCountDownText;
		this.mFooterLayout = mfooterMenu;
		mQRCodeImageWidth=imageviewwidth;
		mQRCodeImageheight=imageviewheight;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		this.mProgressBar = progressbar;
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
	protected String doInBackground(String... params) { // params[0] --> entered user pin // params[1] --> payment type // params[2] --> user_id // params[3] --> user_type // params[4] --> page flag[ customer/StoreModule]
		String result="";
		mPageFlag = params[4];
		mCustomerId = params[2];
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				PaymentStatusVariables.purchase_message = "";
				PaymentStatusVariables.message = "";
				mGetResponse = mZouponswebservice.checkUserPIN(params[0],params[2],params[3]);
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse = mParsingclass.parseCheckPIN(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						if(CheckPINClassVariables.message.equalsIgnoreCase("Success")){
							if(params[1].equalsIgnoreCase("creditcard")){ // Credit card Payment
								mGetResponse=mZouponswebservice.NormalPaymnetUsingCreditCard(mPageFlag,mCustomerId,mCreditcardId, mStoreID, mTipAmount, mTotalAmount, mNote, mActualAmount,mStoreLocationID,mQRCodeImageWidth,mQRCodeImageheight);	
							}else if(params[1].equalsIgnoreCase("giftcard")){  // payment from invoice by using only giftcard
								mGetResponse=mZouponswebservice.NormalPaymnetUsingGiftcard(mPageFlag,mCustomerId,mChoosedGiftcardId, mTipAmount, mTotalAmount,mChoosedPurchaseId, mNote, mActualAmount,mQRCodeImageWidth,mQRCodeImageheight);
							}else if(params[1].equalsIgnoreCase("both")){
								mGetResponse=mZouponswebservice.NormalPaymnetUsingBothCard(mPageFlag,mCustomerId,mCreditcardId, mAmountOnCreditCard, mStoreID, mChoosedGiftcardId, mAmountOnGiftCard, mTipAmount, mTotalAmount,mChoosedPurchaseId, mNote, mActualAmount,mStoreLocationID,mQRCodeImageWidth,mQRCodeImageheight);
							}

							if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
								mParsingResponse=mParsingclass.parseNormalPayment(mGetResponse);
								if(mParsingResponse.equalsIgnoreCase("success")){
									result="success";
								}else if(mParsingResponse.equalsIgnoreCase("failure")){
									Log.i(TAG,"Error");
									result="Response Error.";
								}
							}else{
								result="Response Error.";
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
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("Invalid PIN")){
			alertBox_service("Information", "Entered PIN is incorrect, Please enter correct PIN to proceed the payment");
		}else if(result.equals("success")){
			if(PaymentStatusVariables.message.equalsIgnoreCase("Success")){
				if(mPageFlag.equalsIgnoreCase("from_customer")){ // payment from customer module
					try{
						mStep3Container.setVisibility(View.GONE);
						mFooterLayout.setVisibility(View.GONE);
						mQRCodecontainer.setVisibility(View.VISIBLE);
						GetUrlImageAsyncTask geturlimagetask = new GetUrlImageAsyncTask(this.mContext,mQRCodeImage,mProgressBar);
						geturlimagetask.execute(PaymentStatusVariables.Qr_url_string);

						mCountDownTimer = new CountDownTimer(600000,1000) {

							@Override
							public void onTick(long millisUntilFinished) {
								mExpiryCountDownText.setText(formatTime(millisUntilFinished));
							}

							@Override
							public void onFinish() {
								Log.i("Timer", "Finished");
								if(mQRCodecontainer!=null && mQRCodecontainer.getVisibility()==View.VISIBLE){
									GetInvoiceListTask mInvoiceTask = new GetInvoiceListTask(mContext);
									mInvoiceTask.execute("REJECTINVOICE",PaymentStatusVariables.Invoice_id);							
								}
							}
						};

						mCountDownTimer.start();
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{ // payment from store owner module
					SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
					String mLocationId = mPrefs.getString("location_id", "");
					String mUser_Id = mPrefs.getString("user_id", "");
					String mUserType = mPrefs.getString("user_type", "");
					ApprovePaymentUsingMobileNumber mPaymentTask = new ApprovePaymentUsingMobileNumber(mContext,mLocationId,mUser_Id,StoreOwner_PointOfSale_Part1.mAmount,StoreOwner_PointOfSale_Part1.mCouponCode, "mobile_pay_member",PaymentStatusVariables.Invoice_id ,StoreOwner_PointOfSale_Part1.mStoreOwnerNotes, mUserType,mCustomerId);
					mPaymentTask.execute();
				}

			}else{
				alertBox_service("Information", "Unable to process the payment, please try after some time");
			}
		}
		mProgressdialog.dismiss();

	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	/* to format time for QR Expiration*/
	public String formatTime(long millis) {  
		String output = "00:00";  
		long seconds = millis / 1000;  
		long minutes = seconds / 60;  
		seconds = seconds % 60;  
		minutes = minutes % 60;  
		String sec = String.valueOf(seconds);  
		String min = String.valueOf(minutes);  
		if (seconds < 10)  
			sec = "0" + seconds;  
		if (minutes < 10)  
			min= "0" + minutes;  
		output = min + " : " + sec;  
		return output;
	}//formatTime


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

