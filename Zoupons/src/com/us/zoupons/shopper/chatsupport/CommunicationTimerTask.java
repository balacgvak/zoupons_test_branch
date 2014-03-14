package com.us.zoupons.shopper.chatsupport;

import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.storeowner.customercenter.StoreOwner_chatsupport;

/**
 * 
 * Timer for communication between customer,store and zoupons admin 
 *
 */

public class CommunicationTimerTask extends TimerTask {

	private Context mContext;
	private String mClassName;
	private ViewGroup mMenuBar;
	private ListView mListView;
	private boolean mIsNotification;
	private	String mModuleFlag;
	private String mLastMessageId="";
	private String mStoreId="",mStoreLocationId="",mUserId="",mUserType="",mCustomerUserId="";
	private Button mNotificationCount;
	
	
	public CommunicationTimerTask(Context context,String classname, ViewGroup menubar, ListView listview, boolean mIsFromNotifications, String moduleflag,Button notificationCountButton){
		mContext = context;
		mClassName = classname;
		mMenuBar = menubar;
		mListView = listview;
		mIsNotification = mIsFromNotifications;
		mModuleFlag = moduleflag;
		mNotificationCount = notificationCountButton;
		if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport")){
			// To get store and customer details from shared preferences
			SharedPreferences mPrefs = context.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			mStoreId = mPrefs.getString("store_id", "");
			mStoreLocationId = mPrefs.getString("location_id", "");
			mUserId = mPrefs.getString("user_id", "");
			mUserType = mPrefs.getString("user_type", ""); 
			mCustomerUserId = mPrefs.getString("customer_id", "");
		}
	}
	
	@Override
	public void run() {
		Message mServiceCall = new Message();
		mServiceCall.obj = "service_call";
		mHandler.sendMessage(mServiceCall);
	}
	
	//To update UI elements 
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			try{
				if(msg.obj != null && msg.obj.equals("service_call")){
					if(mListView.getAdapter() != null && mListView.getAdapter().getItem(mListView.getAdapter().getCount()-1) == null){
						if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport")){
							// Stop chat timer task in store module
							if(StoreOwner_chatsupport.sCommunicationTimer!=null){
								StoreOwner_chatsupport.sCommunicationTimer.cancel();
								StoreOwner_chatsupport.sCommunicationTimer=null;
							}if(StoreOwner_chatsupport.sCommunicationTimerTask!=null){
								StoreOwner_chatsupport.sCommunicationTimerTask.cancel();
								StoreOwner_chatsupport.sCommunicationTimerTask=null;
							}
						}else{
							// Stop chat timer task in customer module
							if(MainMenuActivity.mCommunicationTimer!=null){
								MainMenuActivity.mCommunicationTimer.cancel();
								MainMenuActivity.mCommunicationTimer=null;
							}if(MainMenuActivity.mCommunicationTimerTask!=null){
								MainMenuActivity.mCommunicationTimerTask.cancel();
								MainMenuActivity.mCommunicationTimerTask=null;
							}
						}
						mLastMessageId = null;
					}else if(mListView.getAdapter()!= null){
						// Recent Notification message ID
						POJOContactUsResponse mLastChatMessageDetails = (POJOContactUsResponse) mListView.getAdapter().getItem(mListView.getAdapter().getCount()-1);
						mLastMessageId = mLastChatMessageDetails.mResponseId;
					}else{
						mLastMessageId = "";
					}
					if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport") && mLastMessageId!= null && !StoreOwner_chatsupport.TAG.equalsIgnoreCase("")){
						if(mClassName.equalsIgnoreCase("ContactStore")){
							ContactUsResponseTask contactusresponse = new ContactUsResponseTask(mContext,mMenuBar,mListView,mIsNotification,"store_customer",false,true,"refresh",mLastMessageId,"store",mNotificationCount);
							if(Build.VERSION.SDK_INT >= 11)
							    contactusresponse.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"ContactStore",mStoreId,mCustomerUserId,mStoreLocationId,mUserType);
							else
								contactusresponse.execute("ContactStore",mStoreId,mCustomerUserId,mStoreLocationId,mUserType);
						}else{
							ContactUsResponseTask contactusresponse = new ContactUsResponseTask(mContext,mMenuBar,mListView,mIsNotification,"store_zoupons",false,true,"refresh",mLastMessageId,"Store",mNotificationCount);
							if(Build.VERSION.SDK_INT >= 11)
								contactusresponse.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"ZouponsSupport",mStoreId,mUserId,mStoreLocationId,mUserType);
							else	
							    contactusresponse.execute("ZouponsSupport",mStoreId,mUserId,mStoreLocationId,mUserType);
						}
					}else if(!mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport") && mLastMessageId!= null && !POJOMainMenuActivityTAG.ChatFlag.equalsIgnoreCase("")){
						if(mClassName.equalsIgnoreCase("ContactStore")){
							ContactUsResponseTask contactusresponse = new ContactUsResponseTask(mContext,mMenuBar,mListView,mIsNotification,mModuleFlag,false,true,"refresh",mLastMessageId,"customer",mNotificationCount);
							if(Build.VERSION.SDK_INT >= 11)
								contactusresponse.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"ContactStore",RightMenuStoreId_ClassVariables.mStoreID,UserDetails.mServiceUserId,RightMenuStoreId_ClassVariables.mLocationId,"shopper");
							else
								contactusresponse.execute("ContactStore",RightMenuStoreId_ClassVariables.mStoreID,UserDetails.mServiceUserId,RightMenuStoreId_ClassVariables.mLocationId,"shopper");
						}else{
							ContactUsResponseTask contactusresponse = new ContactUsResponseTask(mContext,mMenuBar,mListView,mIsNotification,mModuleFlag,false,true,"refresh",mLastMessageId,"customer",mNotificationCount);
							if(Build.VERSION.SDK_INT >= 11)
								contactusresponse.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"ZouponsSupport","",UserDetails.mServiceUserId,"","shopper");
							else
								contactusresponse.execute("ZouponsSupport","",UserDetails.mServiceUserId,"","shopper");
						}	
					}
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		};
	};
}
