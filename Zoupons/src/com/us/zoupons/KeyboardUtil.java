package com.us.zoupons;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil{
	public static String hideKeyboard(Activity activity){
		String rtn=null;
		try{
			if(activity.getCurrentFocus()!=null){
				InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				rtn="success";
			}else{
				rtn="nofocus";
			}
		}catch (Exception e){
			// Ignore exceptions if any
			rtn = e.toString();
		}
		return rtn;
	}
}
