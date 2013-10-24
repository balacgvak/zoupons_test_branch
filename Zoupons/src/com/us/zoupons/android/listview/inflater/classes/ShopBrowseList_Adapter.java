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

/**
 * Adapter to load store from browse categories while home page loading or click current location button over mapview
 *
 */
public class ShopBrowseList_Adapter extends BaseAdapter{

	private LayoutInflater shopBrowseListInflater;
	Context ctx;
	String TAG="ShopNearestList_Adapter";
	
	public ShopBrowseList_Adapter(Context context) {
		shopBrowseListInflater=LayoutInflater.from(context);
		this.ctx=context;
	}
	
	@Override
	public int getCount() {
		return WebServiceStaticArrays.mStoresLocator.size();
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
		StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(position);
		if(convertView==null){
			convertView = shopBrowseListInflater.inflate(R.layout.shoplistview, null);
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
		holder.storeAddress.setText(obj.addressLine1);
		holder.storeAddress1.setText(obj.addressLine2);
		return convertView;
	}
	
	static class ViewHolder{
		private ImageView storeImage,distanceBalloon;
		private TextView storeName,storeAddress,storeAddress1;
	}
}