package com.us.zoupons.exceptionhandling;

import com.us.zoupons.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class ReportActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_intro_popup);
		AlertDialog.Builder mCustomAlertDialog = new AlertDialog.Builder(ReportActivity.this);
		mCustomAlertDialog.setTitle("ANR");
		mCustomAlertDialog.setMessage("Custom Message");
		mCustomAlertDialog.setPositiveButton("FORCE CLOSE", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Log.i("button", "force close");
			}
		});
        mCustomAlertDialog.setNegativeButton("SEND REPORT", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Log.i("button", "send report");
			}
		});
        mCustomAlertDialog.show();
	}
	
	
}
