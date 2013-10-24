package com.us.zoupons.Communication;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class CustomScrollDetectListener implements OnScrollListener{

	
	int currentVisibleItemCount ;
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		currentVisibleItemCount = visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(currentVisibleItemCount > 0 && scrollState == OnScrollListener.SCROLL_STATE_IDLE){
			Log.i("state", "idle");
		}else{
			Log.i("state", "not idle");
		}
	}

}
