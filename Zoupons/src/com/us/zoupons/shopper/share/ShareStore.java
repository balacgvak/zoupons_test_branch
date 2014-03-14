/**
 * 
 */
package com.us.zoupons.shopper.share;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;


/**
 * Class to add like for a store and share store in facebook.
 * 
 */

public class ShareStore extends MainMenuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		social(R.layout.store_share,savedInstanceState);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

}
