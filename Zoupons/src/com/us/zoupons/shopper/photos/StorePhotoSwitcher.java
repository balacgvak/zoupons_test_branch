package com.us.zoupons.shopper.photos;


import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;

/**
 * 
 * Activity to display sliding store location photos
 *
 */

public class StorePhotoSwitcher extends MainMenuActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewStorePhotos(R.layout.store_photos_switcher);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
}
