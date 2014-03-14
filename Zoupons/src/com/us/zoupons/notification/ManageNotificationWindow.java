package com.us.zoupons.notification;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.collections.WebServiceStaticArrays;

/**
 * 
 * Generic class to show notification pop up window and manage the same
 *
 */

public class ManageNotificationWindow implements OnClickListener{

	private Activity mContext;
	private ImageView mCalloutTriangleImage;
	private ListView mNotificationList;
	private ViewGroup mTabBarContainer;
	public static CustomNotificationAdapter mNotitificationAdapter;
	private String mModuleFlag;

	public ManageNotificationWindow(Activity context,ViewGroup tabBarContainer,ImageView calloutTriangleImage,String moduleFlag) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mCalloutTriangleImage = calloutTriangleImage;
		this.mTabBarContainer = tabBarContainer;
		this.mModuleFlag = moduleFlag;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mPopUpLayout = mInflater.inflate(R.layout.notification, (ViewGroup) mContext.findViewById(R.id.mPopUpParentLayout));
		mNotificationList = (ListView) mPopUpLayout.findViewById(R.id.notification_list);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		mNotificationList.setLayoutParams(mLayoutParams);
		mCalloutTriangleImage.setVisibility(View.VISIBLE);
		// Initializing PopUpWindow
		final PopupWindow mPopUpWindow = new PopupWindow(mPopUpLayout,android.view.WindowManager.LayoutParams.FILL_PARENT,android.view.WindowManager.LayoutParams.FILL_PARENT,true);     			
		mPopUpWindow.setWidth(mTabBarContainer.getWidth());
		mPopUpWindow.setBackgroundDrawable(new ColorDrawable(R.color.notification_unread));
		mPopUpWindow.setOutsideTouchable(false);
		mPopUpWindow.showAsDropDown(mTabBarContainer, 0, 0);
		LayoutInflater mFooterInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View mFooterLayout = mFooterInflater.inflate(R.layout.footerlayout, null, false);
		final TextView mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		final ProgressBar mFooterProgress = (ProgressBar) mFooterLayout.findViewById(R.id.mProgressBar);
		mFooterProgress.setTag("LoadMoreNotifications"); // To Load more notifications initially but during scroll if there is no notifications then tag is changed in Asynctask and depeding upon it scroll listener is managed 
		mNotificationList.addFooterView(mFooterLayout);
		NotificationDetails.isLoadMore = false; // To reset notification static variables
		mNotitificationAdapter = new CustomNotificationAdapter(mContext);
		mNotificationList.setAdapter(mNotitificationAdapter);
		mNotificationList.setOnItemClickListener(new NotificationItemClickListener(mModuleFlag));
		//For Footer Layout
		mPopUpWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				mCalloutTriangleImage.setVisibility(View.INVISIBLE);
			}
		});

		// To dismiss popup window when touch outside..
		final Rect listviewRect = new Rect();
		mNotificationList.getHitRect(listviewRect);
		mPopUpLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!listviewRect.contains((int)event.getX(), (int)event.getY())) {
					mPopUpWindow.dismiss();	
				}
				return false;
			}
		});
		
		mNotificationList.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				int lastInScreen = firstVisibleItem + visibleItemCount;	
				if(mNotificationList.getAdapter()!= null && mNotificationList.getAdapter().getCount() >=20){					
					if(lastInScreen == totalItemCount && !NotificationDetails.isLoadMore && mFooterProgress.getTag()!=null && !mFooterProgress.getTag().toString().equalsIgnoreCase("DontCallServiceAnyMore")){												
						if(mNotificationList.getFooterViewsCount() == 0){
							mNotificationList.addFooterView(mFooterLayout);
						}else{
						}
						mFooterText.setVisibility(View.VISIBLE);
						mFooterProgress.setVisibility(View.VISIBLE);
						NotificationDetails mNotificationDetail = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(WebServiceStaticArrays.mAllNotifications.size()-1);
						GetNotificationTask mNotificationTask = new GetNotificationTask(mContext,mModuleFlag,"scroll",mFooterText,mFooterProgress,mNotificationDetail.id);
						mNotificationTask.execute();
					}
				}else{
					if(mNotificationList.getAdapter()!= null && mNotificationList.getAdapter().getCount() == 0){
					}else if(mFooterLayout != null && mNotificationList.getFooterViewsCount() !=0 && mNotificationList.getAdapter() != null){
						if(lastInScreen!= totalItemCount && !NotificationDetails.isLoadMore){
							mNotificationList.removeFooterView(mFooterLayout);	
						}
					}
				}
				
			}
		});
	}
}


