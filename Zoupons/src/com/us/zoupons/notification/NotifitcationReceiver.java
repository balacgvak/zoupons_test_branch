package com.us.zoupons.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

/**
 * 
 * Broadcast Receiver which 
 *
 */

public class NotifitcationReceiver extends BroadcastReceiver{

	private Button mNotificationCountButton;
    	
	public NotifitcationReceiver(Button notificationcountbutton) {
		this.mNotificationCountButton = notificationcountbutton;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// Have to check remove notification for customer center 
		try {
			if (intent.hasExtra("FromNotification")) {
				if (NotificationDetails.notificationcount > 0) {
					mNotificationCountButton.setVisibility(View.VISIBLE);
					mNotificationCountButton.setText(String.valueOf(NotificationDetails.notificationcount));
					if(ManageNotificationWindow.mNotitificationAdapter != null){ // To refresh notification adapter
						ManageNotificationWindow.mNotitificationAdapter.notifyDataSetChanged();
					}
				} else {
					mNotificationCountButton.setVisibility(View.GONE);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
