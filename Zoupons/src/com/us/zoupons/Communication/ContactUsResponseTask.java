package com.us.zoupons.Communication;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.UnreadToReadMessage;
import com.us.zoupons.storeowner.communication.StoreOwner_ContactStore;

public class ContactUsResponseTask extends AsyncTask<String,String, String>{
	
	String TAG=ContactUsResponseTask.class.getSimpleName();
	Context ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	String  mGetResponse="",mParseResponse="",FLAGACT=""; 	
	private ViewGroup mMenuBar;
	public TextView[] mMessageTime,mMessageUserHeader,mMessageStoreHeader,mStoreMessage,mUserMessage;
	public LinearLayout[] mMessageStoreContainer,mMessageUserContainer,mMessageStoreBackground,mMessageUserBackground;
	public ListView mListView;
	private boolean mIsFromNotifications;
	private String mModuleFlag="";
	private boolean mShowProgress;
	private boolean mRefresh;
	private String mFlag="",mNotificationId="";
	/*private Timer mCommunicationTimer;
	private TimerTask mCommunicationTimerTask;*/
	
	public ContactUsResponseTask(Context context,ViewGroup menubar,ListView listview, boolean mIsFromNotifications,String moduleflag, boolean showprogress,boolean refresh,String flag,String notification_id) {
		this.ctx = context;			
		this.mMenuBar = menubar;
		this.mListView = listview;
		this.mIsFromNotifications = mIsFromNotifications;
		this.mModuleFlag = moduleflag;
		this.mFlag = flag;
		this.mNotificationId = notification_id;
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice = new ZouponsWebService(context);
		parsingclass = new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(false);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		this.mShowProgress = showprogress;
		mRefresh = refresh;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		if(mShowProgress){
			//Start a status dialog
			progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		}
		if(!mFlag.equalsIgnoreCase("refresh"))
		//Used To Check If Already running or not
		MainMenuActivity.mIsLoadMore = true;
	}
	@Override
	protected String doInBackground(String... params) {
		String result="";
		FLAGACT=params[0];
		try{			
			if(!isCancelled()){
				if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){			
					mGetResponse = zouponswebservice.mContactToUsResponse(FLAGACT,params[2],params[1],params[3],params[4],mModuleFlag,mFlag,mNotificationId);				
					Log.i("response", mGetResponse);
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						if(mFlag.equalsIgnoreCase("normal")) // Have to clear arraylist during first time launch only
							WebServiceStaticArrays.mContactUsResponse.clear();
						mParseResponse =  parsingclass.mParseContactUsStoreResponse(mGetResponse,mFlag);					
						if(mParseResponse.equalsIgnoreCase("success")){						
							result ="success";				
						}else if(mParseResponse.equalsIgnoreCase("failure")){
							Log.i(TAG,"Error");
							result="Response Error.";
						}else if(mParseResponse.equalsIgnoreCase("norecords")){
							Log.i(TAG,"No Records");
							result="No Records";
						}
					}else{
						result="Response Error.";
					}
				}else{
					result="nonetwork";
				}
			}else{
				
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
		if(progressdialog.isShowing())
			progressdialog.dismiss();
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		this.mMenuBar.setVisibility(View.VISIBLE);
		this.mListView.setVisibility(View.VISIBLE);
		//Used To Check If Already running or not
		if(!mFlag.equalsIgnoreCase("refresh"))
		MainMenuActivity.mIsLoadMore = false;
		Log.i("response size", WebServiceStaticArrays.mContactUsResponse.size()+"");
		if(result.equals("nonetwork")){
			if(mFlag.equalsIgnoreCase("normal"))
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			if(mFlag.equalsIgnoreCase("normal"))
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			//alertBox_service("Information", result);
			if(!ctx.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore") &&  MainMenuActivity.mHeaderProgressBar != null && MainMenuActivity.mHeaderProgressBar.getVisibility() == View.VISIBLE && mFlag.equalsIgnoreCase("scroll")){
				MainMenuActivity.mHeaderLoadingText.setVisibility(View.GONE);
				MainMenuActivity.mHeaderProgressBar.setVisibility(View.GONE);
			}else if(ctx.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore") &&  StoreOwner_ContactStore.mHeaderProgressBar != null && StoreOwner_ContactStore.mHeaderProgressBar.getVisibility() == View.VISIBLE && mFlag.equalsIgnoreCase("scroll")){ 
				StoreOwner_ContactStore.mHeaderLoadingText.setVisibility(View.GONE);
				StoreOwner_ContactStore.mHeaderProgressBar.setVisibility(View.GONE);
			}
		}else if(result.equals("Thread Error")){
			if(mFlag.equalsIgnoreCase("normal"))
			alertBox_service("Information", "Unable to process.");
		}else{
			if(FLAGACT.equalsIgnoreCase("TalkToUs")){
				
				if(WebServiceStaticArrays.mContactUsResponse.size()>0 && mListView.getAdapter() != null)
					MenuUtilityClass.TalkToUs_ContactStoreListView(this.ctx, this.mListView,!mShowProgress,"TalkToUs",mMenuBar,mFlag);
				else
					MenuUtilityClass.TalkToUs_ContactStoreListView(this.ctx, this.mListView,false,"TalkToUs",mMenuBar,mFlag);
				
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
					UnreadToReadMessage unreadtoreadtask = new UnreadToReadMessage(ctx);
					unreadtoreadtask.execute(notificationIds);
				}
				Log.i(TAG, "Message From Talk To Us");
			}else{
				StringBuilder sb = new StringBuilder();
				ArrayList<Object> mNotifications = new ArrayList<Object>();
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
					
					UnreadToReadMessage unreadtoreadtask = new UnreadToReadMessage(ctx);
					unreadtoreadtask.execute(notificationIds);	
				}

				/*for(int i=0;i<WebServiceStaticArrays.mContactUsResponse.size();i++){
					POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponse.get(i);
					if(!obj.mMessage.equals("")){
						WebServiceStaticArrays.mContactUsResponse.clear();
						//MenuUtilityClass.TalkToUs_ContactStoreListView(this.ctx, this.mListView,false);
						MenuUtilityClass.TalkToUs_ContactStoreListView(this.ctx, this.mListView,!mShowProgress);
						break;
					}else{
						MenuUtilityClass.TalkToUs_ContactStoreListView(this.ctx, this.mListView,!mShowProgress);
						break;	
					}
				}*/
				if( mListView.getAdapter() != null && mListView.getAdapter().getCount() >0)
				     MenuUtilityClass.TalkToUs_ContactStoreListView(this.ctx, this.mListView,!mShowProgress,"ContactStore",mMenuBar,mFlag);
				else
					MenuUtilityClass.TalkToUs_ContactStoreListView(this.ctx, this.mListView,false,"ContactStore",mMenuBar,mFlag);
			}
		}
		try{
			if(!mRefresh){
				if(ctx.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore")){
					if(StoreOwner_ContactStore.mCommunicationTimer != null && mFlag.equalsIgnoreCase("normal")){
						StoreOwner_ContactStore.mCommunicationTimer.schedule(StoreOwner_ContactStore.mCommunicationTimerTask, 0, 2000);
					}else if(StoreOwner_ContactStore.mCommunicationTimer == null){
						if(FLAGACT.equalsIgnoreCase("ContactStore"))
							StoreOwner_ContactStore.mCommunicationTimerTask = new CommunicationTimerTask(this.ctx, "ContactStore", mMenuBar, mListView, mIsFromNotifications, "store_customer");
						else
							StoreOwner_ContactStore.mCommunicationTimerTask = new CommunicationTimerTask(this.ctx, "TalkToUs", mMenuBar, mListView, mIsFromNotifications, "store_zoupons");
						StoreOwner_ContactStore.mCommunicationTimer = new Timer();
						StoreOwner_ContactStore.mCommunicationTimer.schedule(StoreOwner_ContactStore.mCommunicationTimerTask, 0, 2000);
					}
				}else{
					if(MainMenuActivity.mCommunicationTimer != null && mFlag.equalsIgnoreCase("normal")){
						MainMenuActivity.mCommunicationTimer.schedule(MainMenuActivity.mCommunicationTimerTask, 0, 2000);
					}else if(MainMenuActivity.mCommunicationTimer == null){
						if(FLAGACT.equalsIgnoreCase("ContactStore")){
							MainMenuActivity.mCommunicationTimerTask = new CommunicationTimerTask(this.ctx, FLAGACT, mMenuBar, mListView, mIsFromNotifications, "store_customer");
						}else{
							MainMenuActivity.mCommunicationTimerTask = new CommunicationTimerTask(this.ctx, FLAGACT, mMenuBar, mListView, mIsFromNotifications, "zoupons_customer");
						}
						MainMenuActivity.mCommunicationTimer = new Timer();
						MainMenuActivity.mCommunicationTimer.schedule(MainMenuActivity.mCommunicationTimerTask, 0, 2000);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}
	}

	private void alertBox_service(String title, String msg) {
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