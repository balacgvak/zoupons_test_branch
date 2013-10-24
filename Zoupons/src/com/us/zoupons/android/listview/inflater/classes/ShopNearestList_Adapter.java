/**
 * 
 */
package com.us.zoupons.android.listview.inflater.classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.cards.ImageLoader;

/**
 * Adapter to load store nearest current location while home page loading or click current location button over mapview
 */
public class ShopNearestList_Adapter extends BaseAdapter{

	private LayoutInflater shopNearestListInflater;
	Context ctx;
	String TAG="ShopNearestList_Adapter";
	ImageLoader imageLoader;
	
	public ShopNearestList_Adapter(Context context) {
		shopNearestListInflater=LayoutInflater.from(context);
		this.ctx=context;
		imageLoader = new ImageLoader(context);
	}
	
	@Override
	public int getCount() {
		if(WebServiceStaticArrays.mStoresLocator.size()>0){
			return WebServiceStaticArrays.mStoresLocator.size();
		}else{
			return 1;
		}
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView=shopNearestListInflater.inflate(R.layout.favoriteslistview, null);
			holder=new ViewHolder();
			holder.mStoreName=(TextView) convertView.findViewById(R.id.favourite_storeNameId);
			holder.mStoreCategory=(TextView) convertView.findViewById(R.id.favourite_storecategoryId);
			holder.mStoreType=(TextView) convertView.findViewById(R.id.favourite_storeTypeId);
			holder.mStoreDistance=(TextView) convertView.findViewById(R.id.favourite_storeDistanceId);
			holder.mStoreImage = (ImageView) convertView.findViewById(R.id.favourite_storeImageId);
			holder.mStoreLikeCount = (TextView) convertView.findViewById(R.id.favourite_storeLikesId);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		Log.i("size", ""+WebServiceStaticArrays.mStoresLocator.size());
		if(WebServiceStaticArrays.mStoresLocator.size()>0){
			StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(position);
			Log.i(TAG,"Store Name : "+obj.storeName);
			if(!obj.storeName.equalsIgnoreCase("")){
				convertView.setClickable(false);
				holder.mStoreName.setText(obj.storeName);
				holder.mStoreLikeCount.setText(obj.like_count);
				holder.mStoreCategory.setText(obj.category+" / "+obj.subcategory);

				if(obj.deviceDistance.equalsIgnoreCase("-1")){
					holder.mStoreType.setText(obj.invoice_center_ordering);
					holder.mStoreDistance.setText("");
					holder.mStoreLikeCount.setVisibility(View.INVISIBLE);
				}else{
					if(obj.rightmenu_pointofsale_flag.equalsIgnoreCase("Yes")){
						holder.mStoreLikeCount.setVisibility(View.VISIBLE);
						holder.mStoreType.setText(obj.invoice_center_ordering);
					}else {
						holder.mStoreLikeCount.setVisibility(View.INVISIBLE);
						holder.mStoreType.setText(obj.invoice_center_ordering);
					}
					holder.mStoreDistance.setText(String.format("%.2f", Double.valueOf(obj.deviceDistance))+"m");
				}
				imageLoader.DisplayImage(obj.logoPath+"&w="+90+"&h="+70+"&zc=0", holder.mStoreImage);
			}else{
				convertView.setClickable(true);
				holder.mStoreImage.setVisibility(View.INVISIBLE);
				holder.mStoreName.setText("No Stores Available");
				holder.mStoreLikeCount.setVisibility(View.GONE);
			}
		}else{
			convertView.setClickable(true);
			holder.mStoreImage.setVisibility(View.INVISIBLE);
			holder.mStoreName.setText("No Stores Available");
			holder.mStoreLikeCount.setVisibility(View.GONE);
		}
		return convertView;
	}
	static class ViewHolder{
		private TextView mStoreName,mStoreCategory,mStoreType,mStoreDistance,mStoreLikeCount;
		private ImageView mStoreImage;
	}
}