package com.us.zoupons;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.us.zoupons.android.asyncthread_class.StoreNearCurrentLocationAsyncThread;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.flagclasses.ZPayFlag;
import com.us.zoupons.mobilepay.CardPurchase;
import com.us.zoupons.shopper.home.ShopperHomePage;

/**
 * Class to handle Freeze view click [Just to close menu or to close menu and load stores near current location] 
 */

public class FreezeClickListener implements OnClickListener {

	// Initializing views and variables
	private Activity mContext;
	private AnimationSet mShrinkSearch;
	private HorizontalScrollView mScrollView;
	private View mLeftMenu;
    private LinearLayout mMenubarsearchContainer;
	private RelativeLayout mSearchBoxContainer;
	private AutoCompleteTextView mSearchBox;
	private Button mClearStoreName,mFreezeHomePage,mCurrentLocationButton;
	private ListView mListView;
	private GoogleMap mGoogleMap;

	/**
	 * Intialize this constructor from ShopperHomePage
	 * */
	public FreezeClickListener(HorizontalScrollView scrollView, View leftMenu, View rightMenu,ViewGroup searchbar,AutoCompleteTextView searchbox,AnimationSet shrinksearch,int homeflag,
			Activity context,LinearLayout menusearch,Button freezehomepage,Button clearstorename,RelativeLayout searchboxcontainer,GoogleMap googlemap,Button Currentlocation,ListView mListView){
		super();
		this.mScrollView = scrollView;
		this.mLeftMenu = leftMenu;
		this.mSearchBox=searchbox;
		this.mShrinkSearch=shrinksearch;
		this.mContext=context;
		this.mMenubarsearchContainer=menusearch;
		this.mFreezeHomePage=freezehomepage;
		this.mClearStoreName=clearstorename;
		this.mSearchBoxContainer=searchboxcontainer;
		this.mCurrentLocationButton = Currentlocation;
		this.mListView = mListView;
		this.mGoogleMap = googlemap;
	}

	/**
	 * Intialize this constructor from CardPurchase
	 * */
	public FreezeClickListener(HorizontalScrollView scrollView, View leftMenu, ViewGroup searchbar,AutoCompleteTextView searchbox,AnimationSet shrinksearch,
			Activity context, Button clearstorename,RelativeLayout searchboxcontainer,Button currentlocation){
		super();
		this.mScrollView = scrollView;
		this.mLeftMenu = leftMenu;
		this.mSearchBox=searchbox;
		this.mShrinkSearch=shrinksearch;
		this.mContext=context;
		this.mClearStoreName=clearstorename;
		this.mSearchBoxContainer=searchboxcontainer;
		this.mCurrentLocationButton = currentlocation;
	}

	@Override
	public void onClick(View v) {
		//this.mMenubarsearchContainer.setBackgroundResource(R.drawable.header_2);
		if(MenuBarSearchClickListener.sCLICKFLAG==true&&MenuOutClass.HOMEPAGE_MENUOUT==false){
			if(this.mSearchBox.getVisibility()==View.VISIBLE){
				this.mSearchBox.setFocusable(true);
				this.mSearchBox.setFocusableInTouchMode(true);
				this.mSearchBox.clearFocus();
			}
			//search out
			InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			softkeyboardevent.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);
			mFreezeHomePage.setVisibility(View.GONE);
		}else if(MenuBarSearchClickListener.sCLICKFLAG==false&&MenuOutClass.HOMEPAGE_MENUOUT==true){
			//To add focus from searchbox in homepage
			if(this.mSearchBox.getVisibility()==View.VISIBLE){
				this.mSearchBox.setFocusableInTouchMode(true);
				this.mSearchBox.clearFocus();
			}
			//menu out
			int menuWidth = mLeftMenu.getMeasuredWidth();
			// Ensure menu is visible
			mLeftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			mScrollView.smoothScrollTo(left, 0);
			MenuOutClass.HOMEPAGE_MENUOUT=false;
			mFreezeHomePage.setVisibility(View.GONE);
		}else if(MenuBarSearchClickListener.sCLICKFLAG==true&&MenuOutClass.HOMEPAGE_MENUOUT==true){
			//search out
			InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			softkeyboardevent.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);
			int menuWidth = mLeftMenu.getMeasuredWidth();
			// Ensure menu is visible
			mLeftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			mScrollView.smoothScrollTo(left, 0);
			MenuOutClass.HOMEPAGE_MENUOUT=false;
			mFreezeHomePage.setVisibility(View.GONE);
			//To add focus from searchbox in homepage
			if(this.mSearchBox.getVisibility()==View.VISIBLE){
				this.mSearchBox.setFocusable(true);
				this.mSearchBox.requestFocus();
				this.mSearchBox.setFocusableInTouchMode(true);
			}
		}
		// To show stores in current location if user tap freeze view 
		if(mContext.getClass().getSimpleName().equalsIgnoreCase("ShopperHomePage")){ 
			if(ShopperHomePage.sMapViewOnScrollViewFlag.equalsIgnoreCase("qrcode") || (ShopperHomePage.sMapViewOnScrollViewFlag.equalsIgnoreCase("search") && RightMenuStoreId_ClassVariables.mStoreLocation.equalsIgnoreCase("online store"))){
				if(ShopperHomePage.sMapViewOnScrollViewFlag.equalsIgnoreCase("search")){
					MenuBarSearchClickListener.sCLICKFLAG=true; 
					closeSearchBox("ShopperHomePage");
				}
				StoreNearCurrentLocationAsyncThread storenearThread = new StoreNearCurrentLocationAsyncThread(mContext,mGoogleMap,mListView,mCurrentLocationButton,ShopperHomePage.sDistance,"ShopperHomePage",ShopperHomePage.sFooterLayout,"PROGRESS","ShopperHomePage",ShopperHomePage.sDeviceCurrentLocationLatitude,ShopperHomePage.sDeviceCurrentLocationLongitude,true);
				storenearThread.execute(ShopperHomePage.sDeviceCurrentLocationLatitude,ShopperHomePage.sDeviceCurrentLocationLongitude);
			}
		}else if(mContext.getClass().getSimpleName().equalsIgnoreCase("CardPurchase")){
			if(CardPurchase.mMapViewOnScrollViewFlag.equalsIgnoreCase("qrcode")){
			}
		}
	}

	// To close search box container
	public String closeSearchBox(String classname){
		
		if(classname.equals("ShopperHomePage")){
			this.mMenubarsearchContainer.setBackgroundResource(R.drawable.header_2);
			if(MenuBarSearchClickListener.sCLICKFLAG==true&&MenuOutClass.HOMEPAGE_MENUOUT==false){
				//search out
				if(ShopperHomePage.CheckKeyboardVisibility(ShopperHomePage.mApp)){
					InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					softkeyboardevent.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
				}else{
					InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					softkeyboardevent.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);
				}
				if(ZPayFlag.getFlag()!=1){
					ScaleAnimation shrink = new ScaleAnimation(	1f, 1f,	1f, 0f,	Animation.RELATIVE_TO_PARENT, 100,Animation.RELATIVE_TO_PARENT, 0);
					shrink.setStartOffset(100);
					shrink.setDuration(500);
					mShrinkSearch.addAnimation(shrink);
					mShrinkSearch.setFillAfter(true);
					mShrinkSearch.setInterpolator(new AccelerateInterpolator(1.0f));
					this.mSearchBoxContainer.startAnimation(mShrinkSearch);
					this.mSearchBoxContainer.postDelayed(new Runnable() {
						@Override
						public void run() {
							mSearchBox.setText("");
							mSearchBox.setVisibility(View.GONE);
							mClearStoreName.setVisibility(View.GONE);
							mSearchBoxContainer.setVisibility(View.GONE);
							mFreezeHomePage.setVisibility(View.GONE);
							if(ShopperHomePage.CheckKeyboardVisibility(ShopperHomePage.mApp)){
								InputMethodManager softkeyboardevent = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
								softkeyboardevent.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
							}
						}
					}, 400);
				}else{
					ScaleAnimation shrink = new ScaleAnimation(	1f, 1f,	1f, 0f,	Animation.RELATIVE_TO_PARENT, 100,Animation.RELATIVE_TO_PARENT, 0);
					shrink.setStartOffset(100);
					shrink.setDuration(500);
					mShrinkSearch.addAnimation(shrink);
					mShrinkSearch.setFillAfter(true);
					mShrinkSearch.setInterpolator(new AccelerateInterpolator(1.0f));
					this.mSearchBoxContainer.startAnimation(mShrinkSearch);
					this.mSearchBoxContainer.postDelayed(new Runnable() {
						@Override
						public void run() {
							mSearchBox.setText("");
							mSearchBox.setVisibility(View.GONE);
							mClearStoreName.setVisibility(View.GONE);
							mSearchBoxContainer.setVisibility(View.GONE);
							mFreezeHomePage.setVisibility(View.GONE);
						}
					}, 400);
				} 
				MenuBarSearchClickListener.sCLICKFLAG=false;
			}else if(MenuBarSearchClickListener.sCLICKFLAG==false&&MenuOutClass.HOMEPAGE_MENUOUT==true){
				//menu out
				int menuWidth = mLeftMenu.getMeasuredWidth();
				//To add focus from searchbox in homepage
				if(this.mSearchBox.getVisibility()==View.VISIBLE){
					this.mSearchBox.clearFocus();
				}
				// Ensure menu is visible
				mLeftMenu.setVisibility(View.VISIBLE);
				int left = menuWidth;
				mScrollView.smoothScrollTo(left, 0);
				MenuOutClass.HOMEPAGE_MENUOUT=false;
				mFreezeHomePage.setVisibility(View.GONE);
			}else if(MenuBarSearchClickListener.sCLICKFLAG==true&&MenuOutClass.HOMEPAGE_MENUOUT==true){
				int menuWidth = mLeftMenu.getMeasuredWidth();
				//To add focus from searchbox in homepage
				if(this.mSearchBox.getVisibility()==View.VISIBLE){
					this.mSearchBox.clearFocus();
				}
				// Ensure menu is visible
				mLeftMenu.setVisibility(View.VISIBLE);
				int left = menuWidth;
				mScrollView.smoothScrollTo(left, 0);
				MenuOutClass.HOMEPAGE_MENUOUT=false;
				mFreezeHomePage.setVisibility(View.GONE);
			}
		}else if(classname.equals("CardPurchase")){
			if(ShopperHomePage.CheckKeyboardVisibility(CardPurchase.sApp)){
				InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				softkeyboardevent.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
			}else{
				InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				softkeyboardevent.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);
			}
			ScaleAnimation shrink = new ScaleAnimation(	1f, 1f,	1f, 0f, Animation.RELATIVE_TO_PARENT, 100,Animation.RELATIVE_TO_PARENT, 0);
			shrink.setStartOffset(100);
			shrink.setDuration(500);
			mShrinkSearch.addAnimation(shrink);
			mShrinkSearch.setFillAfter(true);
			mShrinkSearch.setInterpolator(new AccelerateInterpolator(1.0f));
			this.mSearchBoxContainer.startAnimation(mShrinkSearch);
			this.mSearchBoxContainer.postDelayed(new Runnable() {
				@Override
				public void run() {
					mSearchBox.setText("");
					mSearchBox.setVisibility(View.GONE);
					mClearStoreName.setVisibility(View.GONE);
					mSearchBoxContainer.setVisibility(View.GONE);
				}
			}, 400);
			MenuBarSearchClickListener.sZPAYFLAG=false;
		}
		return "success";
	}
}