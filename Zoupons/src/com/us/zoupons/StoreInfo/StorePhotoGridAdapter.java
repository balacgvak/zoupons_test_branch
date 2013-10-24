package com.us.zoupons.StoreInfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.us.zoupons.R;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.cards.ImageLoader;

public class StorePhotoGridAdapter extends BaseAdapter{

	private Context mContext;
	private ImageLoader mPhotosLoader;
	private int imagewidth,width,height;
	
	public StorePhotoGridAdapter(Context context,int width) {
		mContext = context;
		mPhotosLoader = new ImageLoader(mContext);
	    imagewidth =  width;
	}
	
	@Override
	public int getCount() {
		return WebServiceStaticArrays.mFullSizeStorePhotoUrl.size();
	}

	@Override
	public Object getItem(int position) {
		return WebServiceStaticArrays.mFullSizeStorePhotoUrl.get(position);
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
				LinearLayout.LayoutParams mGridImageViewParams = new LinearLayout.LayoutParams(imagewidth, imagewidth);
				holder.mStoreImage.setLayoutParams(mGridImageViewParams);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			mPhotosLoader.DisplayImage((String)getItem(position)+"&w="+width+"&h="+width+"&zc=0",holder.mStoreImage);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView mStoreImage;
	}

}
