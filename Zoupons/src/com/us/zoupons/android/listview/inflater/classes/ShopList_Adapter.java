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
import com.us.zoupons.ClassVariables.StoresLocation_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;

public class ShopList_Adapter extends BaseAdapter {

	private LayoutInflater shoplistInflater;
	Context ShopListContext;
	String TAG="ShopList_Adapter";
	
	public ShopList_Adapter(Context context) {
		shoplistInflater=LayoutInflater.from(context);
		this.ShopListContext=context;
	}

	@Override
	public int getCount() {
		return WebServiceStaticArrays.mStoresLocation.size();
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
		StoresLocation_ClassVariables obj = (StoresLocation_ClassVariables) WebServiceStaticArrays.mStoresLocation.get(position);
		if(convertView==null){
			convertView = shoplistInflater.inflate(R.layout.shoplistview, null);
			holder=new ViewHolder();
			holder.storeImage=(ImageView) convertView.findViewById(R.id.shoplistview_storeimage);
			holder.storeName=(TextView) convertView.findViewById(R.id.storename);
			holder.storeAddress=(TextView) convertView.findViewById(R.id.storeaddress);
			holder.storeAddress1=(TextView) convertView.findViewById(R.id.storeaddress1);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.storeName.setText(obj.storeName);
		holder.storeAddress.setText(obj.addresssLine1);
		holder.storeAddress1.setText(obj.city+","+obj.state+","+obj.country);
		return convertView;
	}
	static class ViewHolder{
		private ImageView storeImage,distanceBalloon;
		//private LinearLayout storeImage;
		private TextView storeName,storeAddress,storeAddress1;
	}
}