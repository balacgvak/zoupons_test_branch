package com.us.zoupons.Communication;

import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ListView;

import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.storeowner.communication.StoreOwner_ContactStore;

public class CommunicationTimerTask extends TimerTask {

	Context mContext;
	String mClassName;
	ViewGroup mMenuBar;
	ListView mListView;
	boolean mIsNotification;
	String mModuleFlag;
	String TAG = "CommunicationTimerTask",mLastMessageId="";
	private String mStoreId="",mStoreLocationId="",mUserId="",mUserType="",mCustomerUserId="";
	
	
	public CommunicationTimerTask(Context context,String classname, ViewGroup menubar, ListView listview, boolean mIsFromNotifications, String moduleflag){
		mContext = context;
		mClassName = classname;
		mMenuBar = menubar;
		mListView = listview;
		mIsNotification = mIsFromNotifications;
		mModuleFlag = moduleflag;
		if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore")){
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
		Log.i(TAG,"Chat Refreshed");
		Message mServiceCall = new Message();
		mServiceCall.obj = "service_call";
		mHandler.sendMessage(mServiceCall);
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			try{
				if(msg.obj != null && msg.obj.equals("service_call")){
					if(mListView.getAdapter()!= null){
						POJOContactUsResponse mLastChatMessageDetails = (POJOContactUsResponse) mListView.getAdapter().getItem(mListView.getAdapter().getCount()-1);
						mLastMessageId = mLastChatMessageDetails.mResponseId;
					}else{
						mLastMessageId = "";
					}
					Log.i("last chat details","ID --" +mLastMessageId);
					if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore")){
						if(mClassName.equalsIgnoreCase("ContactStore")){
							ContactUsResponseTask contactusresponse = new ContactUsResponseTask(mContext,mMenuBar,mListView,mIsNotification,"store_customer",false,true,"refresh",mLastMessageId);
							contactusresponse.execute("ContactStore",mStoreId,mCustomerUserId,mStoreLocationId,mUserType);
						}else{
							ContactUsResponseTask contactusresponse = new ContactUsResponseTask(mContext,mMenuBar,mListView,mIsNotification,"store_zoupons",false,true,"refresh",mLastMessageId);
							contactusresponse.execute("TalkToUs",mStoreId,mUserId,mStoreLocationId,mUserType);
						}
					}else{
						if(mClassName.equalsIgnoreCase("ContactStore")){
							ContactUsResponseTask contactusresponse = new ContactUsResponseTask(mContext,mMenuBar,mListView,mIsNotification,mModuleFlag,false,true,"refresh",mLastMessageId);
							contactusresponse.execute("ContactStore",RightMenuStoreId_ClassVariables.mStoreID,UserDetails.mServiceUserId,RightMenuStoreId_ClassVariables.mLocationId,"shopper");
						}else{
							ContactUsResponseTask contactusresponse = new ContactUsResponseTask(mContext,mMenuBar,mListView,mIsNotification,mModuleFlag,false,true,"refresh",mLastMessageId);
							contactusresponse.execute("TalkToUs","",UserDetails.mServiceUserId,"","shopper");
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
