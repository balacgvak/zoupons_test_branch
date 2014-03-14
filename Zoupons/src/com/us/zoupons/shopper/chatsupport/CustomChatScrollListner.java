package com.us.zoupons.shopper.chatsupport;

import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.storeowner.customercenter.StoreOwner_chatsupport;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * 
 * Helper class to detect scroll in communication list view.. 
 *
 */

public class CustomChatScrollListner implements OnScrollListener{

	private ListView mListView;
	private String classname;
	private Context context;
	private boolean mIsFromNotifications;
	private ViewGroup mMenuBarContainer;
	private String mStoreId="",mStoreLocationId="",mUserId="",mUserType="",mCustomerUserId="";
	public static String mMessageIdDuringScroll="";
	private Button mNotificationCountButton;
		
	public CustomChatScrollListner(Context context,ListView listview,String classname,boolean mIsFromNotification,ViewGroup menubar,Button notificationCountButton){
		this.mListView = listview;
		this.classname = classname;
		this.context = context;
		this.mIsFromNotifications = mIsFromNotification;
		this.mMenuBarContainer = menubar;
		this.mNotificationCountButton = notificationCountButton;
		mMessageIdDuringScroll = "";
		if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport")){
			SharedPreferences mPrefs = context.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			mStoreId = mPrefs.getString("store_id", "");
			mStoreLocationId = mPrefs.getString("location_id", "");
			mUserId = mPrefs.getString("user_id", "");
			mUserType = mPrefs.getString("user_type", ""); 
			mCustomerUserId = mPrefs.getString("customer_id", "");
		}
	}
		
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if(view.getAdapter() != null)
			//Log.i("adapter count",""+WebServiceStaticArrays.mContactUsResponse.size());
		if(view.getAdapter() != null && firstVisibleItem == 0 && !MainMenuActivity.mIsLoadMore && mListView.getAdapter().getCount() >= 20){
			// Get last visible notifcation ID in list
			POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponse.get(0);
			//Log.i("first chat id", obj.mResponseId+"");
			String mFirstMessageId= obj.mResponseId;
			if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport") && (!mFirstMessageId.equalsIgnoreCase(mMessageIdDuringScroll)) && (!mMessageIdDuringScroll.equalsIgnoreCase("error")) && !StoreOwner_chatsupport.TAG.equalsIgnoreCase("")){
				mMessageIdDuringScroll = mFirstMessageId;
				if(StoreOwner_chatsupport.sCommunicationTimer!=null){
					StoreOwner_chatsupport.sCommunicationTimer.cancel();
					StoreOwner_chatsupport.sCommunicationTimer=null;
				}if(StoreOwner_chatsupport.sCommunicationTimerTask!=null){
					StoreOwner_chatsupport.sCommunicationTimerTask.cancel();
					StoreOwner_chatsupport.sCommunicationTimerTask=null;
				}
							
				if(StoreOwner_chatsupport.sHeaderProgressBar!= null && StoreOwner_chatsupport.sHeaderProgressBar.getVisibility() == View.GONE){
					StoreOwner_chatsupport.sHeaderProgressBar.setVisibility(View.VISIBLE);
					StoreOwner_chatsupport.sHeaderLoadingText.setVisibility(View.VISIBLE);
				}
				if(classname.equalsIgnoreCase("ContactStore")){
					ContactUsResponseTask contactusresponse = new ContactUsResponseTask(context,mMenuBarContainer,mListView,mIsFromNotifications,"store_customer",false,false,"scroll",obj.mResponseId,"store",mNotificationCountButton);
					contactusresponse.execute("ContactStore",mStoreId,mCustomerUserId,mStoreLocationId,mUserType);
				}else{
					ContactUsResponseTask contactusresponse = new ContactUsResponseTask(context,mMenuBarContainer,mListView,mIsFromNotifications,"store_zoupons",false,false,"scroll",obj.mResponseId,"Store",mNotificationCountButton);
					contactusresponse.execute("ZouponsSupport",mStoreId,mUserId,mStoreLocationId,mUserType);
				}
			}else if(!context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport") && !mFirstMessageId.equalsIgnoreCase(mMessageIdDuringScroll) && (!mMessageIdDuringScroll.equalsIgnoreCase("error") && !POJOMainMenuActivityTAG.ChatFlag.equalsIgnoreCase(""))){
				mMessageIdDuringScroll = mFirstMessageId;
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
				
				if(MainMenuActivity.mHeaderProgressBar!= null && MainMenuActivity.mHeaderProgressBar.getVisibility() == View.GONE){
					MainMenuActivity.mHeaderProgressBar.setVisibility(View.VISIBLE);
					MainMenuActivity.mHeaderLoadingText.setVisibility(View.VISIBLE);
				}
				
				if(classname.equalsIgnoreCase("ContactStore")){
					ContactUsResponseTask contactusresponse = new ContactUsResponseTask(context,mMenuBarContainer,mListView,mIsFromNotifications,"store_customer",false,false,"scroll",obj.mResponseId,"customer",mNotificationCountButton);
					contactusresponse.execute("ContactStore",RightMenuStoreId_ClassVariables.mStoreID,UserDetails.mServiceUserId,RightMenuStoreId_ClassVariables.mLocationId,"shopper");
				}else{
					ContactUsResponseTask contactusresponse = new ContactUsResponseTask(context,mMenuBarContainer,mListView,mIsFromNotifications,"zoupons_customer",false,false,"scroll",obj.mResponseId,"customer",mNotificationCountButton);
					contactusresponse.execute("ZouponsSupport","",UserDetails.mServiceUserId,"","shopper");
				}
			}else{
				if(mMessageIdDuringScroll.equalsIgnoreCase("error"))
					mMessageIdDuringScroll="";
			}
		}else if(view.getAdapter() != null && view.getAdapter().getCount()<=20){
			// To hide header view
			//Log.i("Custom scroll lisntener", "Hide loading when count is less than 20");
			if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport")){
				if(StoreOwner_chatsupport.sHeaderView != null && mListView.getHeaderViewsCount() !=0 &&mListView.getAdapter() != null){
					//mListView.removeHeaderView(StoreOwner_chatsupport.mHeaderView);
					StoreOwner_chatsupport.sHeaderProgressBar.setVisibility(View.GONE);
					StoreOwner_chatsupport.sHeaderLoadingText.setVisibility(View.GONE);
				}
			}else{
				if(MainMenuActivity.mHeaderView != null && mListView.getHeaderViewsCount() !=0 &&mListView.getAdapter() != null){
					//mListView.removeHeaderView(MainMenuActivity.mHeaderView);
					MainMenuActivity.mHeaderProgressBar.setVisibility(View.GONE);
					MainMenuActivity.mHeaderLoadingText.setVisibility(View.GONE);
				}
			}
			
		}else if(view.getAdapter() != null && view.getAdapter().getCount() > 20 && !MainMenuActivity.mIsLoadMore){
			//Log.i("Custom scroll lisntener", "Hide loading when count is greter than than 20 not loading in progress");
			if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_chatsupport")){
				if(StoreOwner_chatsupport.sHeaderView != null && mListView.getHeaderViewsCount() !=0 &&mListView.getAdapter() != null){
					//mListView.removeHeaderView(mHeaderView);
					StoreOwner_chatsupport.sHeaderProgressBar.setVisibility(View.GONE);
					StoreOwner_chatsupport.sHeaderLoadingText.setVisibility(View.GONE);
				}
			}else{
				if(MainMenuActivity.mHeaderView != null && mListView.getHeaderViewsCount() !=0 &&mListView.getAdapter() != null){
					//mListView.removeHeaderView(mHeaderView);
					MainMenuActivity.mHeaderProgressBar.setVisibility(View.GONE);
					MainMenuActivity.mHeaderLoadingText.setVisibility(View.GONE);
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

}
