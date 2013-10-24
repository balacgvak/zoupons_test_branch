package com.us.zoupons.Communication;

import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.storeowner.communication.StoreOwner_ContactStore;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class CustomChatScrollListner implements OnScrollListener{

	private ListView mListView;
	private String classname;
	private Context context;
	private boolean mIsFromNotifications;
	private ViewGroup mMenuBarContainer;
	private String mStoreId="",mStoreLocationId="",mUserId="",mUserType="",mCustomerUserId="";
	
	
	public CustomChatScrollListner(Context context,ListView listview,String classname,boolean mIsFromNotification,ViewGroup menubar){
		this.mListView = listview;
		this.classname = classname;
		this.context = context;
		this.mIsFromNotifications = mIsFromNotification;
		this.mMenuBarContainer = menubar;
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
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		Log.i("Scroll position", firstVisibleItem+" "+ MainMenuActivity.mIsLoadMore);
		if(view.getAdapter() != null)
			Log.i("adapter count",""+WebServiceStaticArrays.mContactUsResponse.size());
		if(view.getAdapter() != null && firstVisibleItem == 0 && !MainMenuActivity.mIsLoadMore && WebServiceStaticArrays.mContactUsResponse.size() >= 20){
			Log.i("total count", firstVisibleItem+ " " + view.getAdapter().getCount());
			POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponse.get(0);
			//Log.i("first chat id", obj.mResponseId+"");
			
			if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore")){
				
				if(StoreOwner_ContactStore.mCommunicationTimer!=null){
					StoreOwner_ContactStore.mCommunicationTimer.cancel();
					StoreOwner_ContactStore.mCommunicationTimer=null;
				}if(StoreOwner_ContactStore.mCommunicationTimerTask!=null){
					StoreOwner_ContactStore.mCommunicationTimerTask.cancel();
					StoreOwner_ContactStore.mCommunicationTimerTask=null;
				}
							
				if(StoreOwner_ContactStore.mHeaderProgressBar!= null && StoreOwner_ContactStore.mHeaderProgressBar.getVisibility() == View.GONE){
					StoreOwner_ContactStore.mHeaderProgressBar.setVisibility(View.VISIBLE);
					StoreOwner_ContactStore.mHeaderLoadingText.setVisibility(View.VISIBLE);
				}
				if(classname.equalsIgnoreCase("StoreOwner_ContactStore")){
					ContactUsResponseTask contactusresponse = new ContactUsResponseTask(context,mMenuBarContainer,mListView,mIsFromNotifications,"store_customer",false,false,"scroll",obj.mResponseId);
					contactusresponse.execute("ContactStore",mStoreId,mCustomerUserId,mStoreLocationId,mUserType);
				}else{
					ContactUsResponseTask contactusresponse = new ContactUsResponseTask(context,mMenuBarContainer,mListView,mIsFromNotifications,"store_zoupons",false,false,"scroll",obj.mResponseId);
					contactusresponse.execute("TalkToUs",mStoreId,mUserId,mStoreLocationId,mUserType);
				}
			}else{
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
				/*MainMenuActivity.mHeaderProgressBar.setVisibility(View.VISIBLE);
				MainMenuActivity.mHeaderLoadingText.setVisibility(View.VISIBLE);*/
				if(classname.equalsIgnoreCase("ContactStore")){
					ContactUsResponseTask contactusresponse = new ContactUsResponseTask(context,mMenuBarContainer,mListView,mIsFromNotifications,"store_customer",false,false,"scroll",obj.mResponseId);
					contactusresponse.execute("ContactStore",RightMenuStoreId_ClassVariables.mStoreID,UserDetails.mServiceUserId,RightMenuStoreId_ClassVariables.mLocationId,"shopper");
				}else{
					ContactUsResponseTask contactusresponse = new ContactUsResponseTask(context,mMenuBarContainer,mListView,mIsFromNotifications,"zoupons_customer",false,false,"scroll",obj.mResponseId);
					contactusresponse.execute("TalkToUs","",UserDetails.mServiceUserId,"","shopper");
				}
			}
		}else if(view.getAdapter() != null && view.getAdapter().getCount()<=20){
			Log.i("Custom scroll lisntener", "Hide loading when count is less than 20");
			if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore")){
				if(StoreOwner_ContactStore.mHeaderView != null && mListView.getHeaderViewsCount() !=0 &&mListView.getAdapter() != null){
					//mListView.removeHeaderView(StoreOwner_ContactStore.mHeaderView);
					StoreOwner_ContactStore.mHeaderProgressBar.setVisibility(View.GONE);
					StoreOwner_ContactStore.mHeaderLoadingText.setVisibility(View.GONE);
				}
			}else{
				if(MainMenuActivity.mHeaderView != null && mListView.getHeaderViewsCount() !=0 &&mListView.getAdapter() != null){
					//mListView.removeHeaderView(MainMenuActivity.mHeaderView);
					MainMenuActivity.mHeaderProgressBar.setVisibility(View.GONE);
					MainMenuActivity.mHeaderLoadingText.setVisibility(View.GONE);
				}
			}
			
		}else if(view.getAdapter() != null && view.getAdapter().getCount() > 20 && !MainMenuActivity.mIsLoadMore){
			Log.i("Custom scroll lisntener", "Hide loading when count is greter than than 20 not loading in progress");
			if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore")){
				if(StoreOwner_ContactStore.mHeaderView != null && mListView.getHeaderViewsCount() !=0 &&mListView.getAdapter() != null){
					//mListView.removeHeaderView(mHeaderView);
					StoreOwner_ContactStore.mHeaderProgressBar.setVisibility(View.GONE);
					StoreOwner_ContactStore.mHeaderLoadingText.setVisibility(View.GONE);
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
