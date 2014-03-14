package com.us.zoupons;

import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * Helper class to hide keyboard when editor action done is encountered
 */

public class DoneOnEditorActionListener implements OnEditorActionListener {

	private Context mContext;
	private AutoCompleteTextView mSearchBox;
	private EditText mEmailId_Edt;
	private String mClassName="";
	
	/*
	 * ShopperHomePage Constructor / Zpay Constructor
	 * */
	public DoneOnEditorActionListener(Context context,AutoCompleteTextView searchbox){
		this.mContext=context;
		this.mSearchBox=searchbox;
	}
	
	/*
	 * Registration Constructor
	 * */
	public DoneOnEditorActionListener(Context context,String classname,EditText emailidvalue_edt){
		this.mContext=context;
		this.mClassName=classname;
		this.mEmailId_Edt=emailidvalue_edt;
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_DONE){
			InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			if(this.mClassName.equals("")){
				softkeyboardevent.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);
			}else if(this.mClassName.equals("Registration")){	//From Registration emailid edittext box
				softkeyboardevent.hideSoftInputFromWindow(this.mEmailId_Edt.getWindowToken(), 0);
			}
		}
		return false;
	}
}
