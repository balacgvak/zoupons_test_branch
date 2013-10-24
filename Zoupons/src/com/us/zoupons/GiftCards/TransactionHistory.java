package com.us.zoupons.GiftCards;

import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.MainMenu.MainMenuActivity;

public class TransactionHistory extends MainMenuActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTransactionDetails(R.layout.transaction_history);
	}

}
