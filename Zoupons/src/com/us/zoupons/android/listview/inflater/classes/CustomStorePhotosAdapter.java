package com.us.zoupons.android.listview.inflater.classes;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.POJOStorePhoto;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;

public class CustomStorePhotosAdapter extends BaseAdapter
{
	  Context contex;
	  int width;
	  
	public CustomStorePhotosAdapter(Context ctx,int width) {
		this.contex=ctx;
		this.width=width;
	}

	@Override
	public int getCount() {

		return 4;
	}

	@Override
	public Object getItem(int position) {
		return WebServiceStaticArrays.mStorePhoto.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		 
	     int size = contex.getResources().getDimensionPixelSize(R.dimen.gridview);
	    
	     if(position ==0){
	    	RelativeLayout mRelativeLayout = new RelativeLayout(contex);
	    	mRelativeLayout.setLayoutParams(new GridView.LayoutParams(this.width, size));
	    	TextView mAddText = new TextView(contex);
	    	RelativeLayout.LayoutParams mTextViewParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    	mTextViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);
	    	mTextViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	    	mAddText.setLayoutParams(mTextViewParams);
	    	mAddText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
	    	mAddText.setText("Add a Photo");
	    	mAddText.setTextColor(contex.getResources().getColor(R.color.black));
	    	mRelativeLayout.setBackgroundColor(contex.getResources().getColor(R.color.darkgrey));
	    	mRelativeLayout.addView(mAddText);
	    	ImageView imageView = new ImageView(this.contex);
		    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		    RelativeLayout.LayoutParams mImageViewParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		    mImageViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		    imageView.setLayoutParams(mImageViewParams);
		    imageView.setImageResource(R.drawable.store_addphoto);
		    mRelativeLayout.addView(imageView);
	    	view = mRelativeLayout;
	     }else{
	    	 ImageView imageView = new ImageView(this.contex);
		     imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		     imageView.setLayoutParams(new GridView.LayoutParams(this.width, size));
		     POJOStorePhoto mPOJO =(POJOStorePhoto) WebServiceStaticArrays.mStorePhoto.get(position-1);
	    	 imageView.setImageBitmap(mPOJO.thumb);	 
	    	 view = imageView;
	     }
	     return view;
	}
}