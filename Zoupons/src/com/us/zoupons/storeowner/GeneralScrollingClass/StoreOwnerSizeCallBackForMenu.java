package com.us.zoupons.storeowner.GeneralScrollingClass;

import android.view.View;

import com.us.zoupons.MyHorizontalScrollView.SizeCallback;

/**
 * Helper that remembers the width of the 'slide' button, so that the 'slide' button remains in view, even when the menu is
 * showing.
 */
public class StoreOwnerSizeCallBackForMenu implements SizeCallback{
	int btnWidth;
	View btnSlide;

	public StoreOwnerSizeCallBackForMenu(View btnSlide) {
		super();
		this.btnSlide = btnSlide;
	}

	@Override
	public void onGlobalLayout() {
		btnWidth = (int) (btnSlide.getMeasuredWidth()-10);
	}

	@Override
	public void getViewSize(int idx, int w, int h, int[] dims) {
		dims[0] = w;
		dims[1] = h;
		final int menuIdx = 0;
		if (idx == menuIdx) {
			dims[0] = w - btnWidth;
		}
	}

}
