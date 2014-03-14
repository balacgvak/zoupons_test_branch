package com.us.zoupons.shopper.video;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;

/**
 * 
 * To show store video Thumbnail to start Buffer
 *
 */

public class StoreVideos extends MainMenuActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		playVideo(R.layout.playvideo_new);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

}
