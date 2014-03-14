package com.us.zoupons.shopper.chatsupport;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to communicate with webservice to get customer's recently communicated store
 *
 */

public class GetCustomerCommunicatedStoreAsyncTask extends AsyncTask<String, String, String>{

	private Context mContext;
	private ProgressDialog mProgressView;
	private ZouponsWebService zouponswebservice;
	private ZouponsParsingClass parsingclass ;
	private NetworkCheck mConnectivityCheck;
	private String mResponse;
	private TextView  mCustomerStoreCount_txt;
	private int CustomerStoreCount=0;
	//To Dynamic List Update
	private String mCheckAndRefresh ="";
	private String mProgressStatus = "";

	public GetCustomerCommunicatedStoreAsyncTask(Context context, ListView customerservicelist,String progressStatus,TextView customerstorecount) {
		this.mContext = context;
		this.mProgressStatus = progressStatus;
		this.mCustomerStoreCount_txt = customerstorecount;
		mProgressView = new ProgressDialog(context);
		mProgressView.setTitle("Loading");
		mProgressView.setMessage("please wait..");
		mProgressView.setCancelable(false);
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(!mProgressStatus.equalsIgnoreCase("NOPROGRESS"))
			mProgressView.show();
		MainMenuActivity.mIsLoadMore = true;
	}

	@Override
	protected String doInBackground(String... params) {
		String mresult ="",mParsingResponse="";
		mCheckAndRefresh = params[0];
		try{
			if (mConnectivityCheck.ConnectivityCheck(mContext)) {
				//Get CustomerService webservice 
				mResponse = zouponswebservice.getCustomerCommunicatedStores(MainMenuActivity.mCustomerCenterStart);
				if(!mResponse.equals("")){
					if(MainMenuActivity.mCustomerCenterStart.equalsIgnoreCase("0")){
						WebServiceStaticArrays.mCustomerService.clear();
					}
					if(!mResponse.equalsIgnoreCase("noresponse") && !mResponse.equalsIgnoreCase("failure")){
						mParsingResponse = parsingclass.parseCustomerService(mResponse);
						if(!mParsingResponse.equals("failure") && !mParsingResponse.equals("norecords") && WebServiceStaticArrays.mCustomerService.size()>0){
							mresult = "success";
						}else if(mParsingResponse.equalsIgnoreCase("failure")){
							mresult = "failure";
						}else if(mParsingResponse.equalsIgnoreCase("norecords")){
							mresult="norecords";
						}
					}else {
						mresult="Response Error.";
					}
				}else{
					mresult = "nodata";
				}
			} else {
				mresult = "nonetwork";
			}
		}catch(Exception e){
			mresult ="failure";
		}
		return mresult;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mProgressView.dismiss();
		MainMenuActivity.mIsLoadMore = false;
		if(result.equalsIgnoreCase("success")){
			if(mContext.getClass().getSimpleName().equalsIgnoreCase("ChatSupport")){
				MainMenuActivity.mCustomerCenterStart = MainMenuActivity.mCustomerCenterEndLimit;
				MainMenuActivity.mCustomerCenterEndLimit= String.valueOf(Integer.parseInt(MainMenuActivity.mCustomerCenterEndLimit)+20);

				for(int i=0;i<WebServiceStaticArrays.mAllNotifications.size();i++){
					NotificationDetails mNotificationDetails = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(i);
					if(mNotificationDetails.notification_type.equalsIgnoreCase("zoupons_message")&&mNotificationDetails.status.equalsIgnoreCase("unread")){//Zoupons message type to talktous
						CustomerStoreCount=CustomerStoreCount+1;
					}else if(mNotificationDetails.notification_type.equalsIgnoreCase("store_message")){// store message type to contact store

					}
				}
				mCustomerStoreCount_txt.setText(String.valueOf(CustomerStoreCount));
				if(((ChatSupportClassVariables)WebServiceStaticArrays.mCustomerService.get(0)).mMessage.equalsIgnoreCase(""))
					//Set List Adapter Here
					((MainMenuActivity) mContext).SetCustomerServiceArrayListAdatpter(mCheckAndRefresh);				
			}else{}

		}else if(result.equalsIgnoreCase("getmessage")){
			
		}else if(result.equalsIgnoreCase("failure")||result.equalsIgnoreCase("Notificationsfailure")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equalsIgnoreCase("norecords")){
			alertBox_service("Information", "No data available");
		}else if(result.equalsIgnoreCase("Response Error.")||result.equalsIgnoreCase("NotificationsResponse Error.")){
			alertBox_service("Information", "Response is not properly return.");
		}else if(result.equalsIgnoreCase("no data")||result.equalsIgnoreCase("Notificationsnodata")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equalsIgnoreCase("nonetwork")){
			Toast.makeText(this.mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}
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
