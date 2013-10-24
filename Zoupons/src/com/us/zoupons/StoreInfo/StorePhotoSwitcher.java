package com.us.zoupons.StoreInfo;


import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.MainMenu.MainMenuActivity;

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
