package com.us.zoupons.shopper.chatsupport;

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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.customercenter.StoreOwner_chatsupport;

/**
 * 
 * AsyncTask to communicate with web server for sending communication.. 
 *
 */

public class ContactUsTask extends AsyncTask<String, String, String>{

	private Context mContext;
	private ProgressDialog mProgressDialog=null;
	private ZouponsWebService mZouponsWebservice = null;
 	private ZouponsParsingClass mParsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	private String  mGetResponse="",mParseResponse="",FLAGACT="";
	private ListView mListView;
	private ViewGroup mMenuBar;
 	private EditText mNewMessage;
	private boolean mIsFromNotifications;
	private String mEmployeeID,mModuleFlag;
	private Button mNotificationCountButton;
	
	/**
	 * Constructor for customer contact store
	 * */
	public ContactUsTask(Context context,ViewGroup menubar,ListView listview,EditText newMessage,boolean isfromnotifications,String employeeid,String moduleflag,Button notificationCountButton) {
		this.mContext = context;			
		this.mListView = listview;
		this.mMenuBar = menubar;
		this.mNewMessage = newMessage;
		this.mIsFromNotifications = isfromnotifications;
		this.mEmployeeID = employeeid;
		this.mModuleFlag = moduleflag;
		this.mNotificationCountButton = notificationCountButton;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponsWebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressDialog=new ProgressDialog(context);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
	}

	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressDialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		FLAGACT = params[0];
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){
				if(FLAGACT.equalsIgnoreCase("ZouponsSupport")){
					mGetResponse = mZouponsWebservice.mTalkToUs(params[5],params[1],params[2],mModuleFlag,params[4],params[3]);	
				}else{
					mGetResponse = mZouponsWebservice.mContactToUs(params[5],params[1],params[2],params[3],params[4],mModuleFlag,mEmployeeID);
				}
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParseResponse = mParsingclass.mParseContactUsResponse(mGetResponse);
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
		}catch (Exception e) {			
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(mProgressDialog != null && mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
		}		
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			if(POJOContactUsResponse.mContactUsMessage.equalsIgnoreCase("Success")){
				if(FLAGACT.equalsIgnoreCase("ZouponsSupport")){
					if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport")){
						SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
						POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponseList.get(0);
						obj.mFromUserId=mPrefs.getString("user_id", "");
						obj.mPostedTime=obj.mContactUsPostedTime;
						obj.mQuery=obj.mContactUsChatMessage;
						obj.mResponseId=obj.mResponseId;
						obj.mToStoreId="0";
						obj.mUserType= obj.mUserType;
						obj.mUserName = obj.mUserName;
						WebServiceStaticArrays.mContactUsResponse.add(obj);
					}else{
						POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponseList.get(0);
						obj.mFromUserId=UserDetails.mServiceUserId;
						obj.mPostedTime=obj.mContactUsPostedTime;
						obj.mQuery=obj.mContactUsChatMessage;
						obj.mResponseId=obj.mResponseId;
						obj.mToStoreId="0";
						obj.mUserType="shopper";
						WebServiceStaticArrays.mContactUsResponse.add(obj);
					}
					if(mListView.getAdapter() == null){
						MenuUtilityClass.TalkToUs_ContactStoreListView(this.mContext, this.mListView,false,"TalkToUS",mMenuBar,"refresh",mNotificationCountButton);	
					}else{
						MenuUtilityClass.TalkToUs_ContactStoreListView(this.mContext, this.mListView,true,"TalkToUS",mMenuBar,"refresh",mNotificationCountButton);
					}
					
					this.mNewMessage.clearFocus();
					this.mNewMessage.setText("");
				}else{
					if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport")){
						SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
						POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponseList.get(0);
						obj.mFromUserId=mPrefs.getString("user_id", "");
						obj.mPostedTime=obj.mContactUsPostedTime;
						obj.mQuery=obj.mContactUsChatMessage;
						obj.mResponseId=obj.mResponseId;
						obj.mToStoreId="";
						obj.mFromStoreId=mPrefs.getString("store_id", "");
						obj.mUserType=obj.mUserType;
						obj.mUserName = obj.mUserName;
						WebServiceStaticArrays.mContactUsResponse.add(obj);
					}else{
						POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponseList.get(0);
						obj.mFromUserId=UserDetails.mServiceUserId;
						obj.mFromStoreId="";
						obj.mPostedTime=obj.mContactUsPostedTime;
						obj.mQuery=obj.mContactUsChatMessage;
						obj.mResponseId=obj.mResponseId;
						obj.mToStoreId=RightMenuStoreId_ClassVariables.mLocationId;
						obj.mUserType="shopper";
						WebServiceStaticArrays.mContactUsResponse.add(obj);
					}
					if(mListView.getAdapter() == null){
						MenuUtilityClass.TalkToUs_ContactStoreListView(this.mContext, this.mListView,false,"ContactStore",mMenuBar,"refresh",mNotificationCountButton);	
					}else{
						MenuUtilityClass.TalkToUs_ContactStoreListView(this.mContext, this.mListView,true,"ContactStore",mMenuBar,"refresh",mNotificationCountButton);
					}
					
					this.mNewMessage.clearFocus();
					this.mNewMessage.setText("");
				}
				
			}else{
				Toast.makeText(this.mContext, "Message Sending Failed", Toast.LENGTH_LONG).show();
			}
		    
		}
		try{
			// To schedule Timer task which refreshes for 2 seconds
			if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport")){
				if(StoreOwner_chatsupport.sCommunicationTimer != null && mListView.getAdapter() == null){
					StoreOwner_chatsupport.sCommunicationTimer.schedule(StoreOwner_chatsupport.sCommunicationTimerTask, 0, 2000);
				}else{
					if(FLAGACT.equalsIgnoreCase("ContactStore") && !StoreOwner_chatsupport.TAG.equalsIgnoreCase(""))
						StoreOwner_chatsupport.sCommunicationTimerTask = new CommunicationTimerTask(this.mContext, "ContactStore", mMenuBar, mListView, mIsFromNotifications, "store_customer",mNotificationCountButton);
					else
						StoreOwner_chatsupport.sCommunicationTimerTask = new CommunicationTimerTask(this.mContext, "ZouponsSupport", mMenuBar, mListView, mIsFromNotifications, "store_zoupons",mNotificationCountButton);
					StoreOwner_chatsupport.sCommunicationTimer = new Timer();
					StoreOwner_chatsupport.sCommunicationTimer.schedule(StoreOwner_chatsupport.sCommunicationTimerTask, 0, 2000);
				}
			}else{
				if(MainMenuActivity.mCommunicationTimer != null && mListView.getAdapter() == null){
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
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/*To get time format*/
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
	
	/* For formatting string */
	public static String pad(int c){
		if(c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
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
