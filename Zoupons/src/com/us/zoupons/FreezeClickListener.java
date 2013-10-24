package com.us.zoupons;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.FlagClasses.ZPayFlag;
import com.us.zoupons.android.AsyncThreadClasses.StoreNearCurrentLocationAsyncThread;
import com.us.zoupons.zpay.zpay_step1;

public class FreezeClickListener implements OnClickListener {

	private static String TAG="FreezeClickListener";
	private Activity mContext;
	AnimationSet shrinkSearch;
	HorizontalScrollView scrollView;
	View leftMenu,rightMenu;
	ViewGroup searchBar;
	int homepageFlag;
	LinearLayout menubarsearch;
	RelativeLayout mSearchBoxContainer;
	AutoCompleteTextView mSearchBox;
	Button mClearStoreName;
	Button mFreezeHomePage;
	private GoogleMap mGoogleMap;
	

	/**
	 * Intialize this constructor from SlidingView
	 * */
	public FreezeClickListener(HorizontalScrollView scrollView, View leftMenu, View rightMenu,ViewGroup searchbar,AutoCompleteTextView searchbox,AnimationSet shrinksearch,int homeflag,
			Activity context,LinearLayout menusearch,Button freezehomepage,Button clearstorename,RelativeLayout searchboxcontainer,GoogleMap googlemap){
		super();
		this.scrollView = scrollView;
		this.leftMenu = leftMenu;
		this.rightMenu = rightMenu;
		this.searchBar = searchbar;
		this.mSearchBox=searchbox;
		this.shrinkSearch=shrinksearch;
		this.homepageFlag=homeflag;
		this.mContext=context;
		this.menubarsearch=menusearch;
		this.mFreezeHomePage=freezehomepage;
		this.mClearStoreName=clearstorename;
		this.mSearchBoxContainer=searchboxcontainer;
		
		this.mGoogleMap = googlemap;
	}

	/**
	 * Intialize this constructor from zpay_step1
	 * */
	public FreezeClickListener(HorizontalScrollView scrollView, View leftMenu, ViewGroup searchbar,AutoCompleteTextView searchbox,AnimationSet shrinksearch,
			Activity context, Button clearstorename,RelativeLayout searchboxcontainer){
		super();
		this.scrollView = scrollView;
		this.leftMenu = leftMenu;
		this.searchBar = searchbar;
		this.mSearchBox=searchbox;
		this.shrinkSearch=shrinksearch;
		this.mContext=context;
		this.mClearStoreName=clearstorename;
		this.mSearchBoxContainer=searchboxcontainer;
	}

	@Override
	public void onClick(View v) {

		this.menubarsearch.setBackgroundResource(R.drawable.header_2);

		if(MenuBarSearchClickListener.CLICKFLAG==true&&MenuOutClass.HOMEPAGE_MENUOUT==false){

			if(this.mSearchBox.getVisibility()==View.VISIBLE){
				this.mSearchBox.setFocusable(true);
				this.mSearchBox.setFocusableInTouchMode(true);
				this.mSearchBox.clearFocus();
			}

			//search out
			InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			softkeyboardevent.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);

			mFreezeHomePage.setVisibility(View.GONE);
		}else if(MenuBarSearchClickListener.CLICKFLAG==false&&MenuOutClass.HOMEPAGE_MENUOUT==true){

			//To add focus from searchbox in homepage
			if(this.mSearchBox.getVisibility()==View.VISIBLE){
				this.mSearchBox.setFocusableInTouchMode(true);
				this.mSearchBox.clearFocus();
			}

			//menu out
			Context context = leftMenu.getContext();
			String msg = Messages.getString("Slide") + new Date(); //$NON-NLS-1$
			System.out.println(msg);
			int menuWidth = leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			scrollView.smoothScrollTo(left, 0);
			MenuOutClass.HOMEPAGE_MENUOUT=false;
			mFreezeHomePage.setVisibility(View.GONE);
		}else if(MenuBarSearchClickListener.CLICKFLAG==true&&MenuOutClass.HOMEPAGE_MENUOUT==true){

			//search out
			InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			softkeyboardevent.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);

			Context context = leftMenu.getContext();
			String msg = Messages.getString("Slide") + new Date(); //$NON-NLS-1$
			System.out.println(msg);
			int menuWidth = leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			scrollView.smoothScrollTo(left, 0);
			MenuOutClass.HOMEPAGE_MENUOUT=false;
			mFreezeHomePage.setVisibility(View.GONE);

			//To add focus from searchbox in homepage
			if(this.mSearchBox.getVisibility()==View.VISIBLE){
				this.mSearchBox.setFocusable(true);
				this.mSearchBox.requestFocus();
				this.mSearchBox.setFocusableInTouchMode(true);
			}
		}

		if(mContext.getClass().getSimpleName().equalsIgnoreCase("SlidingView")){
			if(SlidingView.mMapViewOnScrollViewFlag.equalsIgnoreCase("qrcode")){
				StoreNearCurrentLocationAsyncThread storenearThread = new StoreNearCurrentLocationAsyncThread(mContext,SlidingView.mGoogleMap,SlidingView.mShopListView,SlidingView.distance,"SlidingView",SlidingView.mFooterLayout,"PROGRESS","SlidingView",SlidingView.mDeviceCurrentLocationLatitude,SlidingView.mDeviceCurrentLocationLongitude,true);
				storenearThread.execute(SlidingView.mDeviceCurrentLocationLatitude,SlidingView.mDeviceCurrentLocationLongitude);
			}
		}else if(mContext.getClass().getSimpleName().equalsIgnoreCase("zpay_step1")){
			if(zpay_step1.mMapViewOnScrollViewFlag.equalsIgnoreCase("qrcode")){
			}
		}
	}

	public String closeSearchBox(String classname){
		String rtn="";
		if(classname.equals("SlidingView")){

			this.menubarsearch.setBackgroundResource(R.drawable.header_2);

			if(MenuBarSearchClickListener.CLICKFLAG==true&&MenuOutClass.HOMEPAGE_MENUOUT==false){
				//search out

				if(SlidingView.CheckKeyboardVisibility(SlidingView.app)){
					InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					softkeyboardevent.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
				}else{
					InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					softkeyboardevent.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);
				}

				if(ZPayFlag.getFlag()!=1){
					ScaleAnimation shrink = new ScaleAnimation(
							1f, 1f, 
							1f, 0f,
							Animation.RELATIVE_TO_PARENT, 100,
							Animation.RELATIVE_TO_PARENT, 0);
					shrink.setStartOffset(100);
					shrink.setDuration(500);
					shrinkSearch.addAnimation(shrink);
					shrinkSearch.setFillAfter(true);
					shrinkSearch.setInterpolator(new AccelerateInterpolator(1.0f));

					this.mSearchBoxContainer.startAnimation(shrinkSearch);
					this.mSearchBoxContainer.postDelayed(new Runnable() {
						@Override
						public void run() {
							mSearchBox.setText("");
							mSearchBox.setVisibility(View.GONE);
							mClearStoreName.setVisibility(View.GONE);
							mSearchBoxContainer.setVisibility(View.GONE);
							mFreezeHomePage.setVisibility(View.GONE);
							if(SlidingView.CheckKeyboardVisibility(SlidingView.app)){
								InputMethodManager softkeyboardevent = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
								softkeyboardevent.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
							}
						}
					}, 400);
				}else{
					ScaleAnimation shrink = new ScaleAnimation(
							1f, 1f, 
							1f, 0f,
							Animation.RELATIVE_TO_PARENT, 100,
							Animation.RELATIVE_TO_PARENT, 0);
					shrink.setStartOffset(100);
					shrink.setDuration(500);
					shrinkSearch.addAnimation(shrink);
					shrinkSearch.setFillAfter(true);
					shrinkSearch.setInterpolator(new AccelerateInterpolator(1.0f));

					//this.mSearchBox.startAnimation(shrinkSearch);
					this.mSearchBoxContainer.startAnimation(shrinkSearch);
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

				MenuBarSearchClickListener.CLICKFLAG=false;
			}else if(MenuBarSearchClickListener.CLICKFLAG==false&&MenuOutClass.HOMEPAGE_MENUOUT==true){
				//menu out
				Context context = leftMenu.getContext();
				String msg = Messages.getString("Slide") + new Date(); //$NON-NLS-1$
				System.out.println(msg);
				int menuWidth = leftMenu.getMeasuredWidth();

				//To add focus from searchbox in homepage
				if(this.mSearchBox.getVisibility()==View.VISIBLE){
					this.mSearchBox.clearFocus();
				}

				// Ensure menu is visible
				leftMenu.setVisibility(View.VISIBLE);
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);
				MenuOutClass.HOMEPAGE_MENUOUT=false;
				mFreezeHomePage.setVisibility(View.GONE);
			}else if(MenuBarSearchClickListener.CLICKFLAG==true&&MenuOutClass.HOMEPAGE_MENUOUT==true){
				Context context = leftMenu.getContext();
				String msg = Messages.getString("Slide") + new Date(); //$NON-NLS-1$
				System.out.println(msg);
				int menuWidth = leftMenu.getMeasuredWidth();

				//To add focus from searchbox in homepage
				if(this.mSearchBox.getVisibility()==View.VISIBLE){
					this.mSearchBox.clearFocus();
				}

				// Ensure menu is visible
				leftMenu.setVisibility(View.VISIBLE);
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);
				MenuOutClass.HOMEPAGE_MENUOUT=false;
				mFreezeHomePage.setVisibility(View.GONE);
			}
		}else if(classname.equals("zpay_step1")){

			if(SlidingView.CheckKeyboardVisibility(zpay_step1.app)){
				InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				softkeyboardevent.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
			}else{
				InputMethodManager softkeyboardevent = (InputMethodManager)this.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				softkeyboardevent.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);
			}

			ScaleAnimation shrink = new ScaleAnimation(
					1f, 1f, 
					1f, 0f,
					Animation.RELATIVE_TO_PARENT, 100,
					Animation.RELATIVE_TO_PARENT, 0);
			shrink.setStartOffset(100);
			shrink.setDuration(500);
			shrinkSearch.addAnimation(shrink);
			shrinkSearch.setFillAfter(true);
			shrinkSearch.setInterpolator(new AccelerateInterpolator(1.0f));

			this.mSearchBoxContainer.startAnimation(shrinkSearch);
			this.mSearchBoxContainer.postDelayed(new Runnable() {
				@Override
				public void run() {
					mSearchBox.setText("");
					mSearchBox.setVisibility(View.GONE);
					mClearStoreName.setVisibility(View.GONE);
					mSearchBoxContainer.setVisibility(View.GONE);
				}
			}, 400);
			MenuBarSearchClickListener.ZPAYFLAG=false;
		}
		return rtn="success";
	}
}