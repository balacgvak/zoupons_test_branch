package com.us.zoupons;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.us.zoupons.animation.CustomAnimationListener;
import com.us.zoupons.flagclasses.MenuBarFlag;

/**
 * 
 * Helper class to listen when Search Button is Tapped 
 *
 */

public class MenuBarSearchClickListener implements OnClickListener {
	
	private Context mMenuBarContext;
	private ViewGroup mSearchBar;
	public String TAG="MenuBarSearchClickListener";
	public static boolean sCLICKFLAG=false;
	public static boolean sZPAYFLAG=false;
	private AnimationSet mShrinkSearch;
	private AutoCompleteTextView mSearchBox;
	private Button mClearStoreName;
	private RelativeLayout mSearchBarContainer;
	private LinearLayout mList,mBrowse,mQRCode;
	private int mSetBackgroundResource;
	private String mLoadingFlag="";  
	private RelativeLayout mCategoriesHeader;
	private TextView mCategoriesText;
	
	public MenuBarSearchClickListener(Context context,ViewGroup searchbar,AutoCompleteTextView searchbox,LinearLayout menulist,LinearLayout menubrowse,LinearLayout menuqrcode,int backgroundresource,Button freezehomepage,Button clearstorename,RelativeLayout searchbarcontainer, RelativeLayout CategoriesHeader, TextView CategoriesText){
		this.mMenuBarContext=context;
		this.mSearchBar=searchbar;
		this.mShrinkSearch=new AnimationSet(true);
		this.mSearchBox= searchbox;
		this.mClearStoreName=clearstorename;
		this.mList=menulist;
		this.mBrowse=menubrowse;
		this.mQRCode=menuqrcode;
		this.mSetBackgroundResource=backgroundresource;
		this.mLoadingFlag="home";
		this.mSearchBarContainer=searchbarcontainer;
		this.mCategoriesHeader = CategoriesHeader;
		this.mCategoriesText = CategoriesText;
	}
	
	//Constructor for new Zpay_step1 page
	public MenuBarSearchClickListener(Context context,ViewGroup searchbar,AutoCompleteTextView searchbox,Button freezezpaystep1page,Button clearstorename,RelativeLayout searchboxcontainer){
		this.mMenuBarContext=context;
		this.mSearchBar=searchbar;
		this.mSearchBox=searchbox;
		this.mLoadingFlag="zpay";
		this.mShrinkSearch=new AnimationSet(true);
		this.mClearStoreName=clearstorename;
		this.mSearchBarContainer=searchboxcontainer;
	}
	
	@Override
	public void onClick(View v) {
		MenuBarFlag.mMenuBarFlag=3;
		if(this.mLoadingFlag.equals("home")){
			if(mCategoriesHeader != null && mCategoriesHeader.getVisibility() == View.VISIBLE){
				mCategoriesHeader.setVisibility(View.INVISIBLE);
				Animation custom_animation = AnimationUtils.loadAnimation(this.mMenuBarContext, R.anim.slideup);
				CustomAnimationListener customAnimationListener = new CustomAnimationListener(mCategoriesHeader, "InVisible");
				custom_animation.setAnimationListener(customAnimationListener);
				mCategoriesHeader.startAnimation(custom_animation);
				mCategoriesText.setText("Categories"); // Setting it to default for check in context menu close function..
			}
			
			if(this.mSetBackgroundResource==0){
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
			}
			this.mList.setBackgroundResource(R.drawable.header_2);
			this.mBrowse.setBackgroundResource(R.drawable.header_2);
			this.mQRCode.setBackgroundResource(R.drawable.header_2);

			final InputMethodManager softkeyboardevent = (InputMethodManager)mMenuBarContext.getSystemService(Context.INPUT_METHOD_SERVICE);

			if(sCLICKFLAG==false){
				Log.i(TAG,"Search in");

				this.mSearchBarContainer.startAnimation(AnimationUtils.loadAnimation(mMenuBarContext, R.anim.search_in));
				this.mSearchBarContainer.bringToFront();
				this.mSearchBarContainer.postDelayed(new Runnable() {

					@Override
					public void run() {
						softkeyboardevent.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
						mSearchBarContainer.setVisibility(View.VISIBLE);
						mSearchBar.setVisibility(View.VISIBLE);
						mSearchBox.setVisibility(View.VISIBLE);
						mClearStoreName.setVisibility(View.VISIBLE);
						mSearchBox.requestFocus();
					}
				}, 400);
				sCLICKFLAG=true;
			}else{
				Log.i(TAG,"Search Gone");

				ScaleAnimation shrink = new ScaleAnimation(
						1f, 1f, 
						1f, 0f,
						Animation.RELATIVE_TO_PARENT, 100,
						Animation.RELATIVE_TO_PARENT, 0);
				shrink.setStartOffset(0);
				shrink.setDuration(400);
				mShrinkSearch.addAnimation(shrink);
				mShrinkSearch.setFillAfter(true);
				mShrinkSearch.setFillEnabled(true);
				mShrinkSearch.setInterpolator(new AccelerateInterpolator(1.0f));

				mSearchBarContainer.startAnimation(mShrinkSearch);

				mSearchBar.setVisibility(View.GONE);
				mSearchBox.setVisibility(View.GONE);
				mClearStoreName.setVisibility(View.GONE);
				mSearchBarContainer.setVisibility(View.GONE);
				
				softkeyboardevent.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

				sCLICKFLAG=false;
				v.setBackgroundResource(R.drawable.header_2);
			}
		}else if(this.mLoadingFlag.equals("zpay")){
			
			final InputMethodManager softkeyboardevent = (InputMethodManager)mMenuBarContext.getSystemService(Context.INPUT_METHOD_SERVICE);

			if(sZPAYFLAG==false){
				Log.i(TAG,"Search in");

				this.mSearchBarContainer.startAnimation(AnimationUtils.loadAnimation(mMenuBarContext, R.anim.search_in));
				this.mSearchBarContainer.bringToFront();
				this.mSearchBarContainer.postDelayed(new Runnable() {

					@Override
					public void run() {
						//softkeyboardevent.showSoftInput(searchBox, InputMethodManager.SHOW_FORCED);
						softkeyboardevent.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
						mSearchBar.setVisibility(View.VISIBLE);
						mSearchBarContainer.setVisibility(View.VISIBLE);
						mSearchBox.setVisibility(View.VISIBLE);
						mClearStoreName.setVisibility(View.VISIBLE);
						mSearchBox.requestFocus();
					}
				}, 400);
				sZPAYFLAG=true;
			}else{
				Log.i(TAG,"Search out");

				ScaleAnimation shrink = new ScaleAnimation(
						1f, 1f, 
						1f, 0f,
						Animation.RELATIVE_TO_PARENT, 100,
						Animation.RELATIVE_TO_PARENT, 0);
				shrink.setStartOffset(0);
				shrink.setDuration(400);
				mShrinkSearch.addAnimation(shrink);
				mShrinkSearch.setFillAfter(true);
				mShrinkSearch.setFillEnabled(true);
				mShrinkSearch.setInterpolator(new AccelerateInterpolator(1.0f));

				mSearchBarContainer.startAnimation(mShrinkSearch);
				
				mSearchBox.setVisibility(View.GONE);
				mClearStoreName.setVisibility(View.GONE);
				mSearchBarContainer.setVisibility(View.GONE);

				softkeyboardevent.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

				sZPAYFLAG=false;
			}
		}
	}
}