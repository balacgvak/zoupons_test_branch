package com.us.zoupons.Coupons;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.MainMenu.MainMenuActivity;

public class CouponsActivity extends MainMenuActivity{

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
