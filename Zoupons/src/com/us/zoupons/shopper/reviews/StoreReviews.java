package com.us.zoupons.shopper.reviews;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;

/**
 * 
 * Activity to display and control review details of store
 *
 */


public class StoreReviews extends MainMenuActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setReviews(R.layout.reviews);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
}
