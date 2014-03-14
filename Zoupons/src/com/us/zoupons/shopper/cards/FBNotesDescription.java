package com.us.zoupons.shopper.cards;


import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;

/**
 * 
 * Activity which allows customer to enter friends notes while sending gc/dc 
 *
 */

public class FBNotesDescription extends MainMenuActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FBNotesDescription(R.layout.fb_friends_notes);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
	
}
