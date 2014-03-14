 package com.us.zoupons.shopper.chatsupport;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;

/**
 * 
 * Activity which displays and maintains contact between zoupons contact support and customer
 *
 */

public class ZouponsSupport extends MainMenuActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ContactStore(R.layout.talktous_contactstore,"ZouponsSupport");
	}
	
    @Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

 }
