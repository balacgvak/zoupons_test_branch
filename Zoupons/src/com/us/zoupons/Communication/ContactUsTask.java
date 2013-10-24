package com.us.zoupons.Communication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.storeowner.communication.StoreOwner_ContactStore;

public class ContactUsTask extends AsyncTask<String, String, String>{

	
	String TAG=ContactUsTask.class.getSimpleName();
	Context ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
 	private ZouponsParsingClass parsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	String  mGetResponse="",mParseResponse="",FLAGACT="";
	public ListView mListView;
	public String mSentMessage="";
 	public ViewGroup mMenuBar;
 	public EditText mNewMessage;
	boolean mIsFromNotifications;
	
	String mEmployeeID,mModuleFlag;
	
	/**
	 * Constructor for customer contact store
	 * */
	public ContactUsTask(Context context,ViewGroup menubar,ListView listview,EditText newMessage,boolean isfromnotifications,String employeeid,String moduleflag) {
		this.ctx = context;			
		this.mListView = listview;
		this.mMenuBar = menubar;
		this.mNewMessage = newMessage;
		this.mIsFromNotifications = isfromnotifications;
		this.mEmployeeID = employeeid;
		this.mModuleFlag = moduleflag;
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	
	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		FLAGACT = params[0];
		mSentMessage = params[2];
		
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){
				if(FLAGACT.equalsIgnoreCase("TalkToUs")){
					mGetResponse = zouponswebservice.mTalkToUs(params[5],params[1],params[2],mModuleFlag,params[4],params[3]);	
				}else{
					mGetResponse = zouponswebservice.mContactToUs(params[5],params[1],params[2],params[3],params[4],mModuleFlag,mEmployeeID);
				}
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParseResponse = parsingclass.mParseContactUsResponse(mGetResponse);
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
		}catch (Exception e) {			
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}		
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			if(POJOContactUsResponse.mContactUsMessage.equalsIgnoreCase("Success")){
				if(FLAGACT.equalsIgnoreCase("TalkToUs")){
					Log.i(TAG, "Talk To Us Message Sent");
					
					if(ctx.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore")){
						SharedPreferences mPrefs = ctx.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
						POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponseList.get(0);
						obj.mFromUserId=mPrefs.getString("user_id", "");
						obj.mPostedTime=obj.mContactUsPostedTime;
						obj.mQuery=obj.mContactUsChatMessage;
						obj.mResponseId="";
						obj.mToStoreId="0";
						obj.mUserType=mPrefs.getString("user_type", "");
						obj.mUserName = mPrefs.getString("user_name", "");
						WebServiceStaticArrays.mContactUsResponse.add(obj);
					}else{
						POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponseList.get(0);
						obj.mFromUserId=UserDetails.mServiceUserId;
						obj.mPostedTime=obj.mContactUsPostedTime;
						obj.mQuery=obj.mContactUsChatMessage;
						obj.mResponseId="";
						obj.mToStoreId="0";
						obj.mUserType="shopper";
						WebServiceStaticArrays.mContactUsResponse.add(obj);
					}
					if(mListView.getAdapter() == null){
						MenuUtilityClass.TalkToUs_ContactStoreListView(this.ctx, this.mListView,false,"TalkToUS",mMenuBar,"refresh");	
					}else{
						MenuUtilityClass.TalkToUs_ContactStoreListView(this.ctx, this.mListView,true,"TalkToUS",mMenuBar,"refresh");
					}
					
					this.mNewMessage.clearFocus();
					this.mNewMessage.setText("");
				}else{
					Log.i(TAG, "Contact Us Store Message Sent");
					if(ctx.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore")){
						SharedPreferences mPrefs = ctx.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
						POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponseList.get(0);
						obj.mFromUserId=mPrefs.getString("user_id", "");
						obj.mPostedTime=obj.mContactUsPostedTime;
						obj.mQuery=obj.mContactUsChatMessage;
						obj.mResponseId="";
						obj.mToStoreId="";
						obj.mFromStoreId=mPrefs.getString("store_id", "");
						obj.mUserType=mPrefs.getString("user_type", "");
						obj.mUserName = mPrefs.getString("user_name", "");
						WebServiceStaticArrays.mContactUsResponse.add(obj);
					}else{
						POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponseList.get(0);
						obj.mFromUserId=UserDetails.mServiceUserId;
						obj.mFromStoreId="";
						obj.mPostedTime=obj.mContactUsPostedTime;
						obj.mQuery=obj.mContactUsChatMessage;
						obj.mResponseId="";
						obj.mToStoreId=RightMenuStoreId_ClassVariables.mLocationId;
						obj.mUserType="shopper";
						WebServiceStaticArrays.mContactUsResponse.add(obj);
					}
					if(mListView.getAdapter() == null){
						MenuUtilityClass.TalkToUs_ContactStoreListView(this.ctx, this.mListView,false,"ContactStore",mMenuBar,"refresh");	
					}else{
						MenuUtilityClass.TalkToUs_ContactStoreListView(this.ctx, this.mListView,true,"ContactStore",mMenuBar,"refresh");
					}
					
					this.mNewMessage.clearFocus();
					this.mNewMessage.setText("");
				}
				
			}else{
				Log.i(TAG, "Message Not Sent");
				Toast.makeText(this.ctx, "Message Sending Failed", Toast.LENGTH_LONG).show();
			}
		    
		}
		
		if(ctx.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore")){
			if(StoreOwner_ContactStore.mCommunicationTimer != null){
				StoreOwner_ContactStore.mCommunicationTimer.schedule(StoreOwner_ContactStore.mCommunicationTimerTask, 0, 2000);
			}else{
				if(FLAGACT.equalsIgnoreCase("ContactStore"))
					StoreOwner_ContactStore.mCommunicationTimerTask = new CommunicationTimerTask(this.ctx, "ContactStore", mMenuBar, mListView, mIsFromNotifications, "store_customer");
				else
					StoreOwner_ContactStore.mCommunicationTimerTask = new CommunicationTimerTask(this.ctx, "TalkToUs", mMenuBar, mListView, mIsFromNotifications, "store_zoupons");
				StoreOwner_ContactStore.mCommunicationTimer = new Timer();
				StoreOwner_ContactStore.mCommunicationTimer.schedule(StoreOwner_ContactStore.mCommunicationTimerTask, 0, 2000);
			}
		}else{
			if(MainMenuActivity.mCommunicationTimer != null){
				MainMenuActivity.mCommunicationTimer.schedule(MainMenuActivity.mCommunicationTimerTask, 0, 2000);
			}else{
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

	public static String gettimeformat(){
		String currentdatetime="";
		final Calendar c = Calendar.getInstance();
		SimpleDateFormat month_date = new SimpleDateFormat("MMM");
		String month_name = month_date.format(c.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("K:mm a");
		String hour_min = sdf.format(c.getTime());

		String year ="" + c.get(Calendar.YEAR);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String currentdate = month_name+" "+pad(Integer.parseInt(day))+","+year+",";
		currentdatetime=currentdate+" "+hour_min;
		return currentdatetime;
	}
	
	public static String pad(int c){
		if(c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
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
