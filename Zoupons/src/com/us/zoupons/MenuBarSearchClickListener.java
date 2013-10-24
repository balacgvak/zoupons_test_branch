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

import com.us.zoupons.FlagClasses.MenuBarFlag;
import com.us.zoupons.animation.CustomAnimationListener;

public class MenuBarSearchClickListener implements OnClickListener {
	
	Context MenuBarContext;
	ViewGroup searchBar;
	public static String TAG="MenuBarSearchClickListener";
	public static boolean CLICKFLAG=false;
	public static boolean ZPAYFLAG=false;
	public AnimationSet expandSearch,shrinkSearch;
	public AutoCompleteTextView searchBox;
	public Button mClearStoreName;
	public RelativeLayout mSearchBarContainer;
	public Button mFreezePage;

	public LinearLayout mList,mBrowse,mQRCode;
	private int setBackgroundResource;
	public static String loadingFlag="";  
	private RelativeLayout mCategoriesHeader;
	private TextView mCategoriesText;
	public MenuBarSearchClickListener(Context context,ViewGroup searchbar,AutoCompleteTextView searchbox,LinearLayout menulist,LinearLayout menubrowse,LinearLayout menuqrcode,int backgroundresource,Button freezehomepage,Button clearstorename,RelativeLayout searchbarcontainer, RelativeLayout CategoriesHeader, TextView CategoriesText){
		this.MenuBarContext=context;
		this.searchBar=searchbar;
		this.expandSearch=new AnimationSet(true);
		this.shrinkSearch=new AnimationSet(true);
		this.searchBox= searchbox;
		this.mClearStoreName=clearstorename;
		this.mList=menulist;
		this.mBrowse=menubrowse;
		this.mQRCode=menuqrcode;
		this.setBackgroundResource=backgroundresource;
		this.mFreezePage=freezehomepage;
		this.loadingFlag="home";
		this.mSearchBarContainer=searchbarcontainer;
		this.mCategoriesHeader = CategoriesHeader;
		this.mCategoriesText = CategoriesText;
	}
	
	//Constructor for new Zpay_step1 page
	public MenuBarSearchClickListener(Context context,ViewGroup searchbar,AutoCompleteTextView searchbox,Button freezezpaystep1page,Button clearstorename,RelativeLayout searchboxcontainer){
		this.MenuBarContext=context;
		this.searchBar=searchbar;
		this.searchBox=searchbox;
		this.mFreezePage=freezezpaystep1page;
		this.loadingFlag="zpay";
		this.expandSearch=new AnimationSet(true);
		this.shrinkSearch=new AnimationSet(true);
		this.mClearStoreName=clearstorename;
		this.mSearchBarContainer=searchboxcontainer;
	}
	
	@Override
	public void onClick(View v) {
		MenuBarFlag.mMenuBarFlag=3;
		if(this.loadingFlag.equals("home")){
			if(mCategoriesHeader != null && mCategoriesHeader.getVisibility() == View.VISIBLE){
				mCategoriesHeader.setVisibility(View.INVISIBLE);
				Animation custom_animation = AnimationUtils.loadAnimation(this.MenuBarContext, R.anim.slideup);
				CustomAnimationListener customAnimationListener = new CustomAnimationListener(mCategoriesHeader, "InVisible");
				custom_animation.setAnimationListener(customAnimationListener);
				mCategoriesHeader.startAnimation(custom_animation);
				mCategoriesText.setText("Categories"); // Setting it to default for check in context menu close function..
			}
			
			if(this.setBackgroundResource==0){
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
			}
			this.mList.setBackgroundResource(R.drawable.header_2);
			this.mBrowse.setBackgroundResource(R.drawable.header_2);
			this.mQRCode.setBackgroundResource(R.drawable.header_2);

			final InputMethodManager softkeyboardevent = (InputMethodManager)MenuBarContext.getSystemService(Context.INPUT_METHOD_SERVICE);

			if(CLICKFLAG==false){
				Log.i(TAG,"Search in");

				this.mSearchBarContainer.startAnimation(AnimationUtils.loadAnimation(MenuBarContext, R.anim.search_in));
				this.mSearchBarContainer.bringToFront();
				this.mSearchBarContainer.postDelayed(new Runnable() {

					@Override
					public void run() {
						softkeyboardevent.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
						mSearchBarContainer.setVisibility(View.VISIBLE);
						searchBar.setVisibility(View.VISIBLE);
						searchBox.setVisibility(View.VISIBLE);
						mClearStoreName.setVisibility(View.VISIBLE);
						searchBox.requestFocus();
					}
				}, 400);
				CLICKFLAG=true;
			}else{
				Log.i(TAG,"Search Gone");

				ScaleAnimation shrink = new ScaleAnimation(
						1f, 1f, 
						1f, 0f,
						Animation.RELATIVE_TO_PARENT, 100,
						Animation.RELATIVE_TO_PARENT, 0);
				shrink.setStartOffset(0);
				shrink.setDuration(400);
				shrinkSearch.addAnimation(shrink);
				shrinkSearch.setFillAfter(true);
				shrinkSearch.setFillEnabled(true);
				shrinkSearch.setInterpolator(new AccelerateInterpolator(1.0f));

				mSearchBarContainer.startAnimation(shrinkSearch);

				searchBar.setVisibility(View.GONE);
				searchBox.setVisibility(View.GONE);
				mClearStoreName.setVisibility(View.GONE);
				mSearchBarContainer.setVisibility(View.GONE);
				
				softkeyboardevent.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

				CLICKFLAG=false;
				v.setBackgroundResource(R.drawable.header_2);
			}
		}else if(this.loadingFlag.equals("zpay")){
			
			final InputMethodManager softkeyboardevent = (InputMethodManager)MenuBarContext.getSystemService(Context.INPUT_METHOD_SERVICE);

			if(ZPAYFLAG==false){
				Log.i(TAG,"Search in");

				this.mSearchBarContainer.startAnimation(AnimationUtils.loadAnimation(MenuBarContext, R.anim.search_in));
				this.mSearchBarContainer.bringToFront();
				this.mSearchBarContainer.postDelayed(new Runnable() {

					@Override
					public void run() {
						//softkeyboardevent.showSoftInput(searchBox, InputMethodManager.SHOW_FORCED);
						softkeyboardevent.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
						searchBar.setVisibility(View.VISIBLE);
						mSearchBarContainer.setVisibility(View.VISIBLE);
						searchBox.setVisibility(View.VISIBLE);
						mClearStoreName.setVisibility(View.VISIBLE);
						searchBox.requestFocus();
					}
				}, 400);
				ZPAYFLAG=true;
			}else{
				Log.i(TAG,"Search out");

				ScaleAnimation shrink = new ScaleAnimation(
						1f, 1f, 
						1f, 0f,
						Animation.RELATIVE_TO_PARENT, 100,
						Animation.RELATIVE_TO_PARENT, 0);
				shrink.setStartOffset(0);
				shrink.setDuration(400);
				shrinkSearch.addAnimation(shrink);
				shrinkSearch.setFillAfter(true);
				shrinkSearch.setFillEnabled(true);
				shrinkSearch.setInterpolator(new AccelerateInterpolator(1.0f));

				mSearchBarContainer.startAnimation(shrinkSearch);
				
				searchBox.setVisibility(View.GONE);
				mClearStoreName.setVisibility(View.GONE);
				mSearchBarContainer.setVisibility(View.GONE);

				softkeyboardevent.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

				ZPAYFLAG=false;
			}
		}
	}
}