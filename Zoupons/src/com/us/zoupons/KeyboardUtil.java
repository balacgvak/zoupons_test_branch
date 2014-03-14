package com.us.zoupons;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Helper class to close keypad 
 */

public class KeyboardUtil{
	// To hide keyboard 
	public String hideKeyboard(Activity activity){
		String return_string=null;
		try{
			if(activity.getCurrentFocus()!=null){
				InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				return_string="success";
			}else{
				return_string="nofocus";
			}
		}catch (Exception e){
			// Ignore exceptions if any
			return_string = e.toString();
		}
		return return_string;
	}
}
