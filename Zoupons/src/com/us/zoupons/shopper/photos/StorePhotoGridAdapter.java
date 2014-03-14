package com.us.zoupons.shopper.photos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.us.zoupons.R;
import com.us.zoupons.classvariables.POJOStorePhoto;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.cards.ImageLoader;

/**
 * 
 * Custom adapter to populate store photos in grid view
 *
 */

public class StorePhotoGridAdapter extends BaseAdapter{

	private Context mContext;
	private ImageLoader mPhotosLoader;
	private int mImagewidth;

	public StorePhotoGridAdapter(Context context,int width) {
		mContext = context;
		mPhotosLoader = new ImageLoader(mContext);
		mImagewidth =  width;
	}

	@Override
	public int getCount() {
		return WebServiceStaticArrays.mStorePhoto.size();
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
		ViewHolder holder = null;
		try{
			if(convertView==null){
				convertView=LayoutInflater.from(mContext).inflate(R.layout.store_photo_grid_imageview, parent, false);
				holder=new ViewHolder();
				holder.mStoreImage = (ImageView) convertView.findViewById(R.id.store_photo_grid_imageviewId);
				LinearLayout.LayoutParams mGridImageViewParams = new LinearLayout.LayoutParams(mImagewidth, mImagewidth);
				holder.mStoreImage.setLayoutParams(mGridImageViewParams);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			POJOStorePhoto mPhotoDetails = (POJOStorePhoto) getItem(position);
			mPhotosLoader.DisplayImage(mPhotoDetails.full_size+"&w="+mImagewidth+"&h="+mImagewidth+"&zc=0",holder.mStoreImage);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	static class ViewHolder{
		private ImageView mStoreImage;
	}

}
