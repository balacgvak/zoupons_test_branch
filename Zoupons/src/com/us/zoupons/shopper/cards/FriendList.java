package com.us.zoupons.shopper.cards;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.us.zoupons.R;
import com.us.zoupons.generic_activity.MainMenuActivity;

/**
 * 
 * Activity to list Contact friend List
 *
 */


public class FriendList extends MainMenuActivity{
	
	public static Handler finish_handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fbFriendList(R.layout.fb_friendlist);
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
