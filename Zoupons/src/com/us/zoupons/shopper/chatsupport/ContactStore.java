package com.us.zoupons.shopper.chatsupport;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;


/**
 * Class to view user and store messages in textview with 9patch image format
 */
public class ContactStore extends MainMenuActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ContactStore(R.layout.talktous_contactstore,"ContactStore");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
	
}
