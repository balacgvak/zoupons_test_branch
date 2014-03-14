package com.us.zoupons.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

/**
 * 
 * Custom Animation listner for header view in home page while we browse store
 *
 */

public class CustomAnimationListener implements AnimationListener{

	private RelativeLayout mHeaderLayout;
	private String mVisibiltyMode;
	public CustomAnimationListener(RelativeLayout HeaderLayout,String Visibilitymode) {
		this.mHeaderLayout = HeaderLayout;
		mVisibiltyMode = Visibilitymode;
	}
		
	@Override
	public void onAnimationEnd(Animation animation) {
		if(mVisibiltyMode.equalsIgnoreCase("Visible")){
			mHeaderLayout.setVisibility(View.VISIBLE);
		}else{
			mHeaderLayout.setVisibility(View.GONE);
		}
		mHeaderLayout.clearAnimation();

	}

	@Override
	public void onAnimationRepeat(Animation animation) {}

	@Override
	public void onAnimationStart(Animation animation) {}

}
