 package com.us.zoupons;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.zoupons.MainMenu.MainMenuActivity;

public class TalkToUs extends MainMenuActivity {

	public static MyHorizontalScrollView scrollView;
	public static View leftMenu;
    View app;
    ImageView btnSlide;
    private Typeface mZouponsFont;
    public static LinearLayout mTalkToUsHome,mTalkToUsZpay,mTalkToUsManageCards,mTalkToUsGiftcards,mTalkToUsMyFavourites,mTalkToUsMyFriends,mTalkToUsChat,mTalkToUsRewards,mTalkToUsSettings,mTalkToUsLogout;
    public static TextView mTalkToUsHomeText,mTalkToUsZpayText,mTalkToUsManageCardsText,mTalkToUsGiftCardsText,mTalkToUsMyFavoritesText,mTalkToUsMyFriendsText,mTalkToUsChatText,mTalkToUsRewardsText,mTalkToUsSettingsText,mTalkToUsLogoutText;
    private static String TAG = "TalkToUs";
	
    private TextView mTalkToUsTextHeader,mTalkToUsTextPost;
	public static Button mPost,mTalkToUsFreezeView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ContactStore(R.layout.talktous_contactstore,"TalkToUs");
	}
	
    @Override
	public void onBackPressed() {
		//super.onBackPressed();
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
}
