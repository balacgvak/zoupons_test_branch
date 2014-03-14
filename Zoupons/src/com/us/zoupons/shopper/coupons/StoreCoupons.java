package com.us.zoupons.shopper.coupons;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;

/**
 * 
 * Activity to list coupons 
 *
 */

public class StoreCoupons extends MainMenuActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCouponsActivity(R.layout.couponsact);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
}
