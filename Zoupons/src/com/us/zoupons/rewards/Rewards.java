package com.us.zoupons.rewards;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.MainMenu.MainMenuActivity;

public class Rewards extends MainMenuActivity {
	
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
