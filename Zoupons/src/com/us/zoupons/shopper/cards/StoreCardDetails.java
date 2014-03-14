package com.us.zoupons.shopper.cards;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.us.zoupons.R;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.generic_activity.MainMenuActivity;

/**
 * 
 * Activity to list Store Giftcards and Deal cards
 *
 */
public class StoreCardDetails extends MainMenuActivity{

	public static Handler finish_handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent().getExtras().getString("CardType").equalsIgnoreCase("Regular")){ // Giftcards
			POJOMainMenuActivityTAG.TAG = "MainMenuActivity_Giftcards";
			GiftCardDetails(R.layout.store_giftcards);
		}else{ // Deal cards
			POJOMainMenuActivityTAG.TAG = "MainMenuActivity_Dealcards";
			GiftCardDetails(R.layout.store_dealcards);	
		}
		//Set Static Handler to finish this activity from anywhere in our application
		finish_handler = new Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch(msg.what) {
				case 0:
					finish();
					break;
				}
			}
		};
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
}
