package com.us.zoupons.shopper.giftcards_deals;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;

/**
 * 
 * Activity to display giftcard transaction details
 *
 */

public class TransactionHistory extends MainMenuActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTransactionDetails(R.layout.transaction_history);
	}

}
