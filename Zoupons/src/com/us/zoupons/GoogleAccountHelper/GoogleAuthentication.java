package com.us.zoupons.GoogleAccountHelper;


import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.MainMenu.MainMenuActivity;


public class GoogleAuthentication extends MainMenuActivity {

	private WebView mWebview;
	private TextView mLoadingTextView;
	private GetAccessTokenClass mGetAccesToken;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GoogleAuthentication(R.layout.activity_google_authentication,getIntent().getStringExtra("buttontext"));
	}
}
