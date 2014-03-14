package com.us.zoupons.shopper.chatsupport;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotificationStatus;
import com.us.zoupons.notification.UnreadToReadMessage;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.customercenter.StoreOwner_chatsupport;

/**
 * 
 * AsyncTask to communicate with web server for fetching communication list 
 *
 */

public class ContactUsResponseTask extends AsyncTask<String,String, String>{

	private Context mContext;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	private String  mGetResponse="",mParseResponse="",FLAGACT=""; 	
	private ViewGroup mMenuBar;
	private ListView mListView;
	private boolean mIsFromNotifications;
	private String mModuleFlag="";
	private boolean mShowProgress;
	private boolean mRefresh;
	private String mFlag="",mNotificationId="",mSenderFlag="",mStoreLocationId="",mCustomerId="";
	private Button mNotificationCountButton;

	public ContactUsResponseTask(Context context,ViewGroup menubar,ListView listview, boolean mIsFromNotifications,String moduleflag, boolean showprogress,boolean refresh,String flag,String notification_id,String senderFlag,Button notificationButton) {
		this.mContext = context;			
		this.mMenuBar = menubar;
		this.mListView = listview;
		this.mIsFromNotifications = mIsFromNotifications;
		this.mModuleFlag = moduleflag;
		this.mFlag = flag;
		this.mNotificationId = notification_id;
		this.mSenderFlag = senderFlag;
		this.mNotificationCountButton = notificationButton;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice = new ZouponsWebService(context);
		mParsingclass = new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(false);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		this.mShowProgress = showprogress;
		mRefresh = refresh;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		if(mShowProgress){
			//Start a status dialog
			mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		}
		if(!mFlag.equalsIgnoreCase("refresh"))
			//Used To Check If Already running or not
			MainMenuActivity.mIsLoadMore = true;
	}
	@Override
	protected String doInBackground(String... params) {
		String result="";
		FLAGACT=params[0];
		mCustomerId = params[2];
		mStoreLocationId = params[3];
		try{			
			if(!isCancelled()){
				if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){	// Network connectivity check		
					mGetResponse = mZouponswebservice.mContactToUsResponse(FLAGACT,params[2],params[1],params[3],params[4],mModuleFlag,mFlag,mNotificationId,mSenderFlag);				
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						if(mFlag.equalsIgnoreCase("normal")) // Have to clear arraylist during first time launch only
							WebServiceStaticArrays.mContactUsResponse.clear();
						mParseResponse =  mParsingclass.mParseContactUsStoreResponse(mGetResponse,mFlag);					
						if(mParseResponse.equalsIgnoreCase("success")){						
							result ="success";
						}else if(mParseResponse.equalsIgnoreCase("failure")){
							result="Response Error.";
						}else if(mParseResponse.equalsIgnoreCase("norecords")){
							result="No Records";
						}
					}else{
						result="Response Error.";
					}
				}else{
					result="nonetwork";
				}
			}
		}catch (Exception e) {			
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		if(mProgressdialog.isShowing())
			mProgressdialog.dismiss();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		this.mMenuBar.setVisibility(View.VISIBLE);
		this.mListView.setVisibility(View.VISIBLE);
		//Used To Check If Already running or not
		if(!mFlag.equalsIgnoreCase("refresh"))
			MainMenuActivity.mIsLoadMore = false;
		if(result.equals("nonetwork")){
			if(!mFlag.equalsIgnoreCase("refresh")){
				Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
				CustomChatScrollListner.mMessageIdDuringScroll = "error";
				hideListviewHeader();
			}
		}else if(result.equals("Response Error.")){
			if(mFlag.equalsIgnoreCase("normal"))
				alertBox_service("Information", "Unable to reach service.");
			else if(mFlag.equalsIgnoreCase("scroll")){
				Toast.makeText(mContext, "response error", Toast.LENGTH_SHORT).show();
				hideListviewHeader();
				CustomChatScrollListner.mMessageIdDuringScroll = "error";
			}
		}else if(result.equals("No Records")){
			//alertBox_service("Information", result);
			hideListviewHeader();
		}else if(result.equals("Thread Error")){
			if(mFlag.equalsIgnoreCase("normal"))
				alertBox_service("Information", "Unable to process.");
			else if(mFlag.equalsIgnoreCase("scroll")){
				Toast.makeText(mContext, "Unable to process", Toast.LENGTH_SHORT).show();
				CustomChatScrollListner.mMessageIdDuringScroll = "error";
				hideListviewHeader();
			}
		}else{
			// To change status of notification
			if(mFlag.equalsIgnoreCase("normal")){
				if(!mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport") && mModuleFlag.equalsIgnoreCase("store_customer")){ // Customer to store chat
					new NotificationStatus("store_message",mNotificationCountButton,mStoreLocationId).changeNotificationStatus();
				}else if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport") && mModuleFlag.equalsIgnoreCase("store_customer")){ // store to customer chat
					new NotificationStatus("customer_message",mNotificationCountButton,mCustomerId).changeNotificationStatus();
				}else if(!mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport") && mModuleFlag.equalsIgnoreCase("zoupons_customer")){
					new NotificationStatus("zoupons_message",mNotificationCountButton).changeNotificationStatus();
				}else if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport") && mModuleFlag.equalsIgnoreCase("store_zoupons")){
					new NotificationStatus("zoupons_message",mNotificationCountButton).changeNotificationStatus();
				}
			}
			if(FLAGACT.equalsIgnoreCase("ZouponsSupport")){
				if( mListView.getAdapter() != null && mListView.getAdapter().getCount() >0)
					MenuUtilityClass.TalkToUs_ContactStoreListView(this.mContext, this.mListView,!mShowProgress,"ZouponsSupport",mMenuBar,mFlag,mNotificationCountButton);
				else
					MenuUtilityClass.TalkToUs_ContactStoreListView(this.mContext, this.mListView,false,"ZouponsSupport",mMenuBar,mFlag,mNotificationCountButton);
				// To make notification message to read status
				StringBuilder sb = new StringBuilder();
				ArrayList<Object> mNotifications = new ArrayList<Object>();
				for(int i=0 ;i < WebServiceStaticArrays.mAllNotifications.size();i++){
					NotificationDetails mNotificationDetails = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(i);
					if(mNotificationDetails.notification_type.equalsIgnoreCase("zoupons_message")){
						sb.append(mNotificationDetails.id+","); // value with comma
						mNotifications.add(mNotificationDetails); 
					}
				}
				if(mIsFromNotifications && sb.length()>0){
					sb.delete(sb.length()-1, sb.length()); // remove last character, which is the comma.
					String notificationIds = sb.toString(); // get the result string.
					UnreadToReadMessage unreadtoreadtask = new UnreadToReadMessage(mContext);
					unreadtoreadtask.execute(notificationIds);
				}
			}else{
				StringBuilder sb = new StringBuilder();
				ArrayList<Object> mNotifications = new ArrayList<Object>();
				// To make notification message to read status
				for(int i=0 ;i < WebServiceStaticArrays.mAllNotifications.size();i++){
					NotificationDetails mNotificationDetails = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(i);
					if(mNotificationDetails.notification_type.equalsIgnoreCase("store_message")){
						sb.append(mNotificationDetails.id+","); // value with comma
						mNotifications.add(mNotificationDetails); 
					}
				}
				if(mIsFromNotifications && sb.length()>0){
					sb.delete(sb.length()-1, sb.length()); // remove last character, which is the comma.
					String notificationIds = sb.toString(); // get the result string.
					UnreadToReadMessage unreadtoreadtask = new UnreadToReadMessage(mContext);
					unreadtoreadtask.execute(notificationIds);	
				}
				if( mListView.getAdapter() != null && mListView.getAdapter().getCount() >0)
					MenuUtilityClass.TalkToUs_ContactStoreListView(this.mContext, this.mListView,!mShowProgress,"ContactStore",mMenuBar,mFlag,mNotificationCountButton);
				else
					MenuUtilityClass.TalkToUs_ContactStoreListView(this.mContext, this.mListView,false,"ContactStore",mMenuBar,mFlag,mNotificationCountButton);
			}
		}
		try{
			if(!mRefresh){
				// To schedule Timer task which refreshes for 2 seconds
				if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport")){
					if(StoreOwner_chatsupport.sCommunicationTimer != null && mFlag.equalsIgnoreCase("normal")){
						StoreOwner_chatsupport.sCommunicationTimer.schedule(StoreOwner_chatsupport.sCommunicationTimerTask, 0, 2000);
					}else if(StoreOwner_chatsupport.sCommunicationTimer == null && !StoreOwner_chatsupport.TAG.equalsIgnoreCase("")){
						if(FLAGACT.equalsIgnoreCase("ContactStore"))
							StoreOwner_chatsupport.sCommunicationTimerTask = new CommunicationTimerTask(this.mContext, "ContactStore", mMenuBar, mListView, mIsFromNotifications, "store_customer",mNotificationCountButton);
						else
							StoreOwner_chatsupport.sCommunicationTimerTask = new CommunicationTimerTask(this.mContext, "ZouponsSupport", mMenuBar, mListView, mIsFromNotifications, "store_zoupons",mNotificationCountButton);
						StoreOwner_chatsupport.sCommunicationTimer = new Timer();
						StoreOwner_chatsupport.sCommunicationTimer.schedule(StoreOwner_chatsupport.sCommunicationTimerTask, 0, 2000);
					}
				}else{
					if(MainMenuActivity.mCommunicationTimer != null && mFlag.equalsIgnoreCase("normal")){
						// To check whether this chat activity goes background or not 
						POJOMainMenuActivityTAG.ChatFlag = POJOMainMenuActivityTAG.TAG; 
						MainMenuActivity.mCommunicationTimer.schedule(MainMenuActivity.mCommunicationTimerTask, 0, 2000);
					}else if(MainMenuActivity.mCommunicationTimer == null && MainMenuActivity.mCommunicationTimerTask==null && !POJOMainMenuActivityTAG.ChatFlag.equalsIgnoreCase("")){
						if(FLAGACT.equalsIgnoreCase("ContactStore")){
							MainMenuActivity.mCommunicationTimerTask = new CommunicationTimerTask(this.mContext, FLAGACT, mMenuBar, mListView, mIsFromNotifications, "store_customer",mNotificationCountButton);
						}else{
							MainMenuActivity.mCommunicationTimerTask = new CommunicationTimerTask(this.mContext, FLAGACT, mMenuBar, mListView, mIsFromNotifications, "zoupons_customer",mNotificationCountButton);
						}
						MainMenuActivity.mCommunicationTimer = new Timer();
						MainMenuActivity.mCommunicationTimer.schedule(MainMenuActivity.mCommunicationTimerTask, 0, 2000);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(mProgressdialog != null && mProgressdialog.isShowing()){
			mProgressdialog.dismiss();
		}
	}

	// To hide loading listview header
	private void hideListviewHeader(){
		if(!mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport") && mFlag.equalsIgnoreCase("scroll")){
			MainMenuActivity.mHeaderLoadingText.setVisibility(View.GONE);
			MainMenuActivity.mHeaderProgressBar.setVisibility(View.GONE);
		}else if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport")  && mFlag.equalsIgnoreCase("scroll")){
			StoreOwner_chatsupport.sHeaderLoadingText.setVisibility(View.GONE);
			StoreOwner_chatsupport.sHeaderProgressBar.setVisibility(View.GONE);
		}
	}

	/* To show alert pop up with respective message */
	private void alertBox_service(String title, String msg) {
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