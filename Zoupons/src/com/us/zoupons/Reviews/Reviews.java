package com.us.zoupons.Reviews;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.MainMenu.MainMenuActivity;

public class Reviews extends MainMenuActivity{
	
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
