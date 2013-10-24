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

public class LocationShopList_Adapter extends BaseAdapter{

	private LayoutInflater LocationShopListInflater;
	Context ctx;
	String TAG="LocationShopList_Adapter";
	
	public LocationShopList_Adapter(Context context) {
		LocationShopListInflater=LayoutInflater.from(context);
		this.ctx=context;
	}
	
	@Override
	public int getCount() {
		return WebServiceStaticArrays.mStoreLocatorList.size();
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
		StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(position);
		if(convertView==null){
			convertView = LocationShopListInflater.inflate(R.layout.shoplistview, null);
			holder=new ViewHolder();
			holder.storeImage=(ImageView) convertView.findViewById(R.id.shoplistview_storeimage);
			holder.storeName=(TextView) convertView.findViewById(R.id.storename);
			holder.storeAddress=(TextView) convertView.findViewById(R.id.storeaddress);
			holder.storeLikeCount=(TextView) convertView.findViewById(R.id.storelikeid);
			holder.storeDistance=(TextView) convertView.findViewById(R.id.storedistance);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.storeName.setText(obj.addressLine1);
		holder.storeAddress.setText(obj.city+" , "+obj.state+" , "+obj.zipcode);
		holder.storeLikeCount.setText(obj.like_count);
		
		if(obj.deviceDistance.equalsIgnoreCase("-1")){
			holder.storeDistance.setText("");
			holder.storeLikeCount.setVisibility(View.INVISIBLE);
		}else{
			if(obj.rightmenu_pointofsale_flag.equalsIgnoreCase("Yes")){
				holder.storeLikeCount.setVisibility(View.VISIBLE);
			}else {
				holder.storeLikeCount.setVisibility(View.INVISIBLE);
			}
			holder.storeDistance.setText(String.format("%.2f", Double.valueOf(obj.deviceDistance))+"m");
		}
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView storeImage,distanceBalloon;
		private TextView storeName,storeAddress,storeLikeCount,storeDistance;
	}
}