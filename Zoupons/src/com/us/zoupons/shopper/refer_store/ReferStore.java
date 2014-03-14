package com.us.zoupons.shopper.refer_store;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;

/**
 * 
 * Activity to display refer store details
 *
 */

public class ReferStore extends MainMenuActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRewardsView(R.layout.rewards);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
}
