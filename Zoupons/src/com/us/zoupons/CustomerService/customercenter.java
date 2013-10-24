package com.us.zoupons.CustomerService;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.MainMenu.MainMenuActivity;

public class customercenter extends MainMenuActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCustomerCenter(R.layout.customercenter);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
}
