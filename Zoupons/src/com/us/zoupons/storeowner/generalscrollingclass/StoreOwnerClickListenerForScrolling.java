package com.us.zoupons.storeowner.generalscrollingclass;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;

import com.us.zoupons.R;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.customercenter.FavouriteCustomerDetails;
import com.us.zoupons.storeowner.customercenter.StoreOwner_chatsupport;

/**
 * Helper for HSV that should be scrolled by a menu View's width.
 */
public class StoreOwnerClickListenerForScrolling implements OnClickListener, OnItemClickListener{

	private	HorizontalScrollView mScrollView;
	private View mLeftMenu;	
	private int mMenuFlag;
	private Button mFreezeView;
	private EditText mFocusEdittext,mFocusEdittext2;
	private boolean mFreezeViewFlag;
	
	/**
	 * Menu must NOT be out/shown to start with.
	 */
	public StoreOwnerClickListenerForScrolling(HorizontalScrollView scrollView, View leftmenu, /*View sRightmenu,*/int menuflag,Button freezeview,String tag) {
		super();
		this.mScrollView = scrollView;
		this.mLeftMenu = leftmenu;
		this.mMenuFlag=menuflag;
		this.mFreezeView = freezeview;
	}
	
	// Constuctor to remove focus for edit text if exists
	public StoreOwnerClickListenerForScrolling(HorizontalScrollView scrollView, View leftmenu, /*View sRightmenu,*/int menuflag,Button freezeview,String tag,EditText mFocusEditText,boolean freezeviewflag ) {
		super();
		this.mScrollView = scrollView;
		this.mLeftMenu = leftmenu;
		this.mMenuFlag=menuflag;
		this.mFreezeView = freezeview;
		this.mFreezeViewFlag = freezeviewflag;
		mFocusEdittext = mFocusEditText;
	}
	
	// Constuctor to remove focus for edit text if exists
	public StoreOwnerClickListenerForScrolling(HorizontalScrollView scrollView, View leftmenu, /*View sRightmenu,*/int menuflag,Button freezeview,String tag,EditText mFocusEditText,EditText mFocusEditText2,boolean freezeviewflag ) {
		super();
		this.mScrollView = scrollView;
		this.mLeftMenu = leftmenu;
		this.mMenuFlag=menuflag;
		this.mFreezeView = freezeview;
		this.mFreezeViewFlag = freezeviewflag;
		mFocusEdittext = mFocusEditText;
		mFocusEdittext2 = mFocusEditText2;
	}

	@Override
	public void onClick(View v) {

		if(StoreOwner_chatsupport.sHeaderProgressBar != null && StoreOwner_chatsupport.sHeaderLoadingText != null){ // To hide loading progress in header view in communication while opening menu
			StoreOwner_chatsupport.sHeaderProgressBar.setVisibility(View.GONE);
			StoreOwner_chatsupport.sHeaderLoadingText.setVisibility(View.GONE);
		}
				
		// To remove focus of edittext so that right menu close completely...
		if(mFocusEdittext != null){
			if(mFocusEdittext.isFocusable()){
				mFocusEdittext.setFocusable(false);
				mFocusEdittext.setFocusableInTouchMode(false);
				mFocusEdittext.clearFocus();
			}
			if(mFreezeViewFlag==true){
				mFocusEdittext.setFocusable(true);
				mFocusEdittext.setFocusableInTouchMode(true);
			}
		}
		if(mFocusEdittext2 != null){
			if(mFocusEdittext2.isFocusable()){
				mFocusEdittext2.setFocusable(false);
				mFocusEdittext2.setFocusableInTouchMode(false);
				mFocusEdittext2.clearFocus();
			}
			if(mFreezeViewFlag==true){
				mFocusEdittext2.setFocusable(true);
				mFocusEdittext2.setFocusableInTouchMode(true);
			}
		}
		Context context = mLeftMenu.getContext();
		int menuWidth = mLeftMenu.getMeasuredWidth();
		//To hide keyboard when any one of the menu's goint to open.
		InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
		// Ensure menu is visible
		mLeftMenu.setVisibility(View.VISIBLE);
		if (!MenuOutClass.STOREOWNER_MENUOUT) {
			if(mMenuFlag==1){
				// Scroll to open left menu
				int left = 0;
				mScrollView.smoothScrollTo(left, 0);
				mFreezeView.setVisibility(View.VISIBLE);
			}else if(mMenuFlag==2){
				//scroll to open right menu
				int right = menuWidth+menuWidth;
				mScrollView.smoothScrollTo(right, 0);
				mFreezeView.setVisibility(View.VISIBLE);
			}
		} else {
			// Scroll to menuWidth so menu isn't on screen.
			int left = menuWidth;
			mScrollView.smoothScrollTo(left, 0);
			mFreezeView.setVisibility(View.GONE);
		}
		MenuOutClass.STOREOWNER_MENUOUT = !MenuOutClass.STOREOWNER_MENUOUT;
	}

	// To close sliding menu
	public void toCloseMenu(){
		int menuWidth = mLeftMenu.getMeasuredWidth();
		// Ensure menu is visible
		mLeftMenu.setVisibility(View.VISIBLE);
		// Scroll to menuWidth so menu isn't on screen.
		int left = menuWidth;
		mScrollView.smoothScrollTo(left, 0);

		mFreezeView.setVisibility(View.GONE);
		MenuOutClass.STOREOWNER_MENUOUT = !MenuOutClass.STOREOWNER_MENUOUT;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//Changing menu visibilty according to right menu
		// setting user id and name to use it in contact store
		FavouriteCustomerDetails mCustomerDetails = (FavouriteCustomerDetails) arg0.getItemAtPosition(arg2);
		StoreOwner_RightMenu.mCustomer_id = mCustomerDetails.mCustomerId;
		StoreOwner_chatsupport.sCustomerName = mCustomerDetails.mCustomerName;
		StoreOwner_RightMenu.mCustomer_FirstName = mCustomerDetails.mCustomerFirstName;
		StoreOwner_RightMenu.mCustomer_LastName = mCustomerDetails.mCustomerLastName;
		StoreOwner_RightMenu.mCustomer_ProfileImage = mCustomerDetails.mCustomerProfileImage;
		if(mCustomerDetails.mIsFavouriteStoreRemoved.equalsIgnoreCase("no")){
			StoreOwner_RightMenu.mStoreCustomerCenterRightMenu_Mail.setBackgroundColor(arg1.getContext().getResources().getColor(R.color.translucent_white));
			StoreOwner_RightMenu.mStoreRightMenu_EmailText.setTextColor(Color.GRAY);
			StoreOwner_RightMenu.mStoreRightMenu_EmailImage.setAlpha(100);
			StoreOwner_RightMenu.mStoreCustomerCenterRightMenu_Mail.setEnabled(false);
		}else{
			StoreOwner_RightMenu.mStoreCustomerCenterRightMenu_Mail.setBackgroundResource(R.drawable.gradient_bg);
			StoreOwner_RightMenu.mStoreRightMenu_EmailText.setTextColor(Color.WHITE);
			StoreOwner_RightMenu.mStoreRightMenu_EmailImage.setAlpha(255);
			StoreOwner_RightMenu.mStoreCustomerCenterRightMenu_Mail.setEnabled(true);
		}
		// To save email enable status and use it in other pages while opening customercenter right menu...
		SharedPreferences mPrefs = arg1.getContext().getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
		Editor editor = mPrefs.edit();
		editor.putString("email_status",mCustomerDetails.mIsFavouriteStoreRemoved);
		editor.commit();
			
		Context context = mLeftMenu.getContext();
		int menuWidth = mLeftMenu.getMeasuredWidth();

		//To hide keyboard when any one of the menu's going to open.
		InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(arg1.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

		// Ensure menu is visible
		mLeftMenu.setVisibility(View.VISIBLE);
		if (!MenuOutClass.STOREOWNER_MENUOUT) {
			if(mMenuFlag==1){
				// Scroll to open left menu
				int left = 0;
				mScrollView.smoothScrollTo(left, 0);
				mFreezeView.setVisibility(View.VISIBLE);
			}else if(mMenuFlag==2){
				//scroll to open right menu
				int right = menuWidth+menuWidth;
				mScrollView.smoothScrollTo(right, 0);
				mFreezeView.setVisibility(View.VISIBLE);
			}
		} else {
			// Scroll to menuWidth so menu isn't on screen.
			int left = menuWidth;
			mScrollView.smoothScrollTo(left, 0);
			mFreezeView.setVisibility(View.GONE);
		}

		MenuOutClass.STOREOWNER_MENUOUT = !MenuOutClass.STOREOWNER_MENUOUT;
	}
}
