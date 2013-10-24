package com.us.zoupons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.us.zoupons.FlagClasses.AccountLoginFlag;
import com.us.zoupons.storeowner.HomePage.StoreOwner_HomePage;

public class ZouponsLoginChoice extends Activity{
	
	private Button mCustomerLogin,mBusinessLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_choice);
		mCustomerLogin = (Button) findViewById(R.id.customer_login);
		mBusinessLogin = (Button) findViewById(R.id.business_login);
		
		mCustomerLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// To update user type based upon login choice
				AccountLoginFlag.accountUserTypeflag="Customer";
				
				Intent login_intent = new Intent(ZouponsLoginChoice.this,SlidingView.class);
				login_intent.putExtra("fromlogin", true);
				startActivity(login_intent);
				finish();
			}
		});
		
		mBusinessLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent login_intent = new Intent(ZouponsLoginChoice.this,StoreOwner_HomePage.class);
				startActivity(login_intent);
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
}
