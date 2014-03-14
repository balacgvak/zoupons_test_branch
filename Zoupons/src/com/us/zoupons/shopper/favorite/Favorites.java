/**
 * 
 */
package com.us.zoupons.shopper.favorite;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;


/*
 * Class to list Favorite stores,coupons and friends favorite stores
 */
public class Favorites extends MainMenuActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Function which lists favorite store,coupons and related functionalities
		favorites(R.layout.favorites); 
	}

    @Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
	
}
