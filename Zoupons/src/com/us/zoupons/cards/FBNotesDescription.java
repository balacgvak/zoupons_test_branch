package com.us.zoupons.cards;


import android.os.Bundle;

import com.us.zoupons.R;
import com.us.zoupons.MainMenu.MainMenuActivity;

public class FBNotesDescription extends MainMenuActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FBNotesDescription(R.layout.fb_friends_notes);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
	
}
