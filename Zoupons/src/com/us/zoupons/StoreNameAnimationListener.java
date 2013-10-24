package com.us.zoupons;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;

public class StoreNameAnimationListener implements AnimationListener{

	private TextView mStoreDetails;
	private String mInformationType;

	public StoreNameAnimationListener(TextView StoreDetails,String info_type){
		Log.i("animation", "constructor called");
		this.mStoreDetails = StoreDetails;
		this.mInformationType = info_type;
		
	}


	@Override
	public void onAnimationEnd(Animation animation) {
		Log.i("Animation","On Animation end");
		
		if(mInformationType.equalsIgnoreCase("store_name")){
			SlidingView.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
		}else{
			SlidingView.mStoreName_RightMenu.setText("address goes here");
		}
		
		Animation custom_animation = AnimationUtils.loadAnimation(SlidingView.rightMenu.getContext(), R.anim.search_in);
		StoreNameAnimationListener customAnimationListener = new StoreNameAnimationListener(SlidingView.mStoreName_RightMenu, "store_name");
		custom_animation.setStartOffset(0);
		custom_animation.setAnimationListener(customAnimationListener);
		
		Animation custom_animation_out = AnimationUtils.loadAnimation(SlidingView.rightMenu.getContext(), R.anim.slideup);
		StoreNameAnimationListener customAnimationListener_out = new StoreNameAnimationListener(SlidingView.mStoreName_RightMenu, "store_address");
		custom_animation_out.setAnimationListener(customAnimationListener_out);
		
		AnimationSet as = new AnimationSet(true);
		as.addAnimation(custom_animation_out);
		custom_animation.setStartOffset(5000);
		as.addAnimation(custom_animation);

		SlidingView.mStoreName_RightMenu.startAnimation(as);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		Log.i("Animation","On Animation repeat");
	}

	
	
	@Override
	public void onAnimationStart(Animation animation) {
		Log.i("Animation","On Animation start");
	}
}