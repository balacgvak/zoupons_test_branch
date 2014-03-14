package com.us.zoupons.notification;

import android.view.View;
import android.widget.Button;

import com.us.zoupons.collections.WebServiceStaticArrays;

/**
 * 
 * To change notification read status internally
 *
 */


public class NotificationStatus {
	
	private String mNotificationType,mRowID="";
	private Button mNotificationCount;
		
	public NotificationStatus(String notificationType,Button notification_count) {
		// TODO Auto-generated constructor stub
		this.mNotificationType = notificationType;
		this.mNotificationCount = notification_count;
	}

	public NotificationStatus(String notificationType, Button notification_count,String row_id) {
		// TODO Auto-generated constructor stub
    	this.mNotificationType = notificationType;
		this.mNotificationCount = notification_count;
		this.mRowID = row_id;
	}

	// To change notification status from unread to read
	public void changeNotificationStatus(){
		for(int i=0;i<WebServiceStaticArrays.mAllNotifications.size();i++){
			NotificationDetails mNotificationDetail = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(i);

			if(mNotificationType.equalsIgnoreCase("store_message")&& mNotificationDetail.notification_type.equalsIgnoreCase(mNotificationType) && mRowID.equalsIgnoreCase(mNotificationDetail.notification_locationId) && mNotificationDetail.status.equalsIgnoreCase("unread")){ // If message from store to customer
				NotificationDetails.notificationcount = NotificationDetails.notificationcount-1; // To reduce unread message status count
				mNotificationDetail.status="read";
				WebServiceStaticArrays.mAllNotifications.set(i, mNotificationDetail);
			}else if(mNotificationType.equalsIgnoreCase("customer_message")&& mNotificationDetail.notification_type.equalsIgnoreCase(mNotificationType) && mRowID.equalsIgnoreCase(mNotificationDetail.mCustomerID) &&  mNotificationDetail.status.equalsIgnoreCase("unread")){ // If message from customer to store
				NotificationDetails.notificationcount = NotificationDetails.notificationcount-1; // To reduce unread message status count
				mNotificationDetail.status="read";
				WebServiceStaticArrays.mAllNotifications.set(i, mNotificationDetail);
			}else if((mNotificationType.equalsIgnoreCase("zoupons_message") || 
					mNotificationType.equalsIgnoreCase("invoice_received") || 
					mNotificationType.equalsIgnoreCase("giftcard_received") || 
					mNotificationType.equalsIgnoreCase("private_coupon_recieved")) 
					&& mNotificationDetail.notification_type.equalsIgnoreCase(mNotificationType) && mNotificationDetail.status.equalsIgnoreCase("unread")){ //If for customer to  Zoupons message
				NotificationDetails.notificationcount = NotificationDetails.notificationcount-1; // To reduce unread message status count
				mNotificationDetail.status="read";
				WebServiceStaticArrays.mAllNotifications.set(i, mNotificationDetail);
			}
		}
		if(NotificationDetails.notificationcount > 0 ) {
			mNotificationCount.setVisibility(View.VISIBLE);
			mNotificationCount.setText(String.valueOf(NotificationDetails.notificationcount));	
		}else{
			mNotificationCount.setVisibility(View.GONE);
		}
		 
	}
	
	
}
