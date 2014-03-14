package com.us.zoupons.shopper.chatsupport;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;

/**
 * 
 * Activity to list notification details
 *
 */

public class ChatSupportNotificationDetails extends MainMenuActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		customercenternotifications(R.layout.customercenter_notifications);
	}
}
