package com.us.zoupons.exceptionhandling;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Intent;
import android.util.Log;

import com.us.zoupons.login.ZouponsLogin;

public class CustomException implements UncaughtExceptionHandler{

  private ZouponsLogin mContext;	
  
   public CustomException(ZouponsLogin Context) {
		// TODO Auto-generated constructor stub
		this.mContext  = Context;
		
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		Log.i("thread handler", "uncaught exception"+" "+arg1.getMessage());
		mContext.startActivity(new Intent(mContext,ReportActivity.class));
	}

}
