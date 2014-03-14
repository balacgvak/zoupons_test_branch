package com.us.zoupons.shopper.chatsupport;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.shopper.cards.ImageLoader;
import com.us.zoupons.storeowner.customercenter.StoreOwner_chatsupport;

/**
 * 
 * Custom adapter to  list recently communicated stores
 *
 */

public class ChatSupportListAdapter extends BaseAdapter{

	private Context mContext;
	public ImageLoader mImageLoader; 

	public ChatSupportListAdapter(Context context) {
		Log.i("adapter", "constructor");
		this.mContext = context;
		mImageLoader=new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return WebServiceStaticArrays.mCustomerService.size();
	}

	@Override
	public Object getItem(int arg0) {
		return WebServiceStaticArrays.mCustomerService.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		int StoreMessageCount=0;
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.customercenter_listrow, null);
			holder = new ViewHolder();
			holder.mStoreImage = (ImageView) convertView.findViewById(R.id.customercenter_storeImageId);
			holder.mStoreName = (TextView) convertView.findViewById(R.id.customercenter_storeNameId);
			holder.mStoreMessageCount = (TextView) convertView.findViewById(R.id.customercenter_storemessagecount);
			holder.mStoreAddressLine1 = (TextView) convertView.findViewById(R.id.customercenter_storeAddress1);
			holder.mStoreAddressLine2 = (TextView) convertView.findViewById(R.id.customercenter_storeAddress2);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
        // To get chat details for each list row
		ChatSupportClassVariables customerservice = (ChatSupportClassVariables) WebServiceStaticArrays.mCustomerService.get(position);
		for(int i=0;i<WebServiceStaticArrays.mAllNotifications.size();i++){
			NotificationDetails notificationdetails = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(i);
			if(notificationdetails!=null && notificationdetails.notification_type.equalsIgnoreCase("store_message") && notificationdetails.notification_locationId.equalsIgnoreCase(customerservice.mStoreLocationId) && notificationdetails.status.equalsIgnoreCase("unread")){
				StoreMessageCount= Integer.valueOf(notificationdetails.chat_group_count);
				break;
			}
		}
		holder.mStoreName.setText(customerservice.mStoreName);
		mImageLoader.DisplayImage(customerservice.mStoreLogoPath+"&w="+90+"&h="+70+"&zc=0",holder.mStoreImage);
		holder.mStoreMessageCount.setText(String.valueOf(StoreMessageCount));
		holder.mStoreAddressLine1.setText(customerservice.mStoreAddressLine1);
		holder.mStoreAddressLine2.setText(customerservice.mStoreCity+" , "+customerservice.mStoreState+" "+customerservice.mStoreZipCode);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChatSupportClassVariables customerserviceclassvariables = (ChatSupportClassVariables) WebServiceStaticArrays.mCustomerService.get(position);
				RightMenuStoreId_ClassVariables.mStoreID=customerserviceclassvariables.mStoreId;
				RightMenuStoreId_ClassVariables.mStoreName = customerserviceclassvariables.mStoreName;
				RightMenuStoreId_ClassVariables.mLocationId = customerserviceclassvariables.mStoreLocationId;
				RightMenuStoreId_ClassVariables.mSelectedStore_lat = customerserviceclassvariables.mStoreLatitude;
				RightMenuStoreId_ClassVariables.mSelectedStore_long = customerserviceclassvariables.mStoreLongitude;
				RightMenuStoreId_ClassVariables.mStoreLocation = customerserviceclassvariables.mStoreLocation;
				if(customerserviceclassvariables.mStoreDistance.equalsIgnoreCase("-1")){
					RightMenuStoreId_ClassVariables.mStoreLocation = "online store";
				}else{
					RightMenuStoreId_ClassVariables.mStoreLocation = customerserviceclassvariables.mStoreAddressLine1 + "\n" + customerserviceclassvariables.mStoreCity+", "+customerserviceclassvariables.mStoreState+" "+customerserviceclassvariables.mStoreZipCode;	
				}
				
				holder.mStoreMessageCount.setText("0");
				
				for(int i=0;i<WebServiceStaticArrays.mAllNotifications.size();i++){
					NotificationDetails notificationdetails = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(i);
					if(notificationdetails!=null && notificationdetails.notification_type.equalsIgnoreCase("store_message") && notificationdetails.notification_locationId.equalsIgnoreCase(customerserviceclassvariables.mStoreLocationId)){
						notificationdetails.status = "read";
						WebServiceStaticArrays.mAllNotifications.set(i, notificationdetails);
						break;
					}
				}
				
				if(!mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_Communication")){
					Intent intent_contactstore = new Intent(mContext,ContactStore.class);
					intent_contactstore.putExtra("isback", "true");
					mContext.startActivity(intent_contactstore);
				}else{
					Intent intent_contactstore = new Intent(mContext,StoreOwner_chatsupport.class);
					intent_contactstore.putExtra("isback", "true");
					mContext.startActivity(intent_contactstore);
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView mStoreImage;
		TextView mStoreName,mStoreMessageCount,mStoreAddressLine1,mStoreAddressLine2;
	}
}
