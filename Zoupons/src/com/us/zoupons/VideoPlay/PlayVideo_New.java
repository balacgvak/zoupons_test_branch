package com.us.zoupons.VideoPlay;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.MainMenu.MainMenuActivity;

public class PlayVideo_New extends MainMenuActivity{
	
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
