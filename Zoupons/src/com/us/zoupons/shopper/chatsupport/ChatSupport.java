package com.us.zoupons.shopper.chatsupport;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;

/**
 * 
 * Activity to list all types of communications
 *
 */

public class ChatSupport extends MainMenuActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCustomerCenter(R.layout.customercenter);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
}
