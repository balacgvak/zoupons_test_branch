package com.us.zoupons;

import android.content.Context;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class DoneOnEditorActionListener implements OnEditorActionListener {

	private Context mContext;
	AnimationSet mShrinkSearch;
	ViewGroup mSearchBar;
	AutoCompleteTextView mSearchBox;
	Button mFreezePage,mClearStoreName;
	RelativeLayout mSearchBoxContainer;
	LinearLayout mMenuBarBrowse,mMenuBarQRCode,mMenuBarSearch,mMenuBarList;
	EditText mMobileNumber_Edt,mEmailId_Edt;
	TextView mMobileActivation_Txt;
	String mClassName="",mFirstName,mLastName,mProfileImage,mMobileNumberValue,mMobileActivationCodeValue,mUserId;
	
	/*
	 * SlidingView Constructor
	 * */
	public DoneOnEditorActionListener(Context context,AnimationSet shrinksearch,ViewGroup searchbar,AutoCompleteTextView searchbox,/*TextView menubarfourthtext,*/Button freezehomepage,Button clearstorename,RelativeLayout searchboxcontainer,
			LinearLayout menubarbrowse,LinearLayout menubarqrcode,LinearLayout menubarsearch,LinearLayout menubarlist){
		this.mContext=context;
		this.mShrinkSearch=shrinksearch;
		this.mSearchBar=searchbar;
		this.mSearchBox=searchbox;
		this.mFreezePage=freezehomepage;
		this.mClearStoreName=clearstorename;
		this.mSearchBoxContainer=searchboxcontainer;
		
		this.mMenuBarBrowse=menubarbrowse;
		this.mMenuBarQRCode=menubarqrcode;
		this.mMenuBarSearch=menubarsearch;
		this.mMenuBarList=menubarlist;
	}
	
	/*
	 * Zpay Constructor
	 * */
	public DoneOnEditorActionListener(Context context,AnimationSet shrinksearch,ViewGroup searchbar,AutoCompleteTextView searchbox,/*TextView menubarfourthtext,*/Button freezehomepage,Button clearstorename,RelativeLayout searchboxcontainer){
		this.mContext=context;
		this.mShrinkSearch=shrinksearch;
		this.mSearchBar=searchbar;
		this.mSearchBox=searchbox;
		this.mFreezePage=freezehomepage;
		this.mClearStoreName=clearstorename;
		this.mSearchBoxContainer=searchboxcontainer;
	}
	
	/*
	 * Registration Constructor
	 * */
	public DoneOnEditorActionListener(Context context,String classname,EditText mobilenumbervalue_edt,TextView mobileactivationcode_txt,/*EditText mobileactivationcodevalue_edt,*/EditText emailidvalue_edt,String userid,String firstname,String lastname,
			String profileimage,String mobilenumbervalue,String mobileactivationcodevalue){
		this.mContext=context;
		this.mClassName=classname;
		this.mMobileNumber_Edt=mobilenumbervalue_edt;
		this.mMobileActivation_Txt=mobileactivationcode_txt;
		this.mEmailId_Edt=emailidvalue_edt;
		this.mUserId=userid;
		this.mFirstName=firstname;
		this.mLastName=lastname;
		this.mProfileImage=profileimage;
		this.mMobileNumberValue=mobilenumbervalue;
		this.mMobileActivationCodeValue=mobileactivationcodevalue;
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
