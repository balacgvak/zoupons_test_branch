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
import com.us.zoupons.ClassVariables.StoresQRCode_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;

/**
 * Adapter to load store from qrcode value and load in mapview
 *
 */

public class ShopQrCodeList_Adapter extends BaseAdapter{

	private LayoutInflater shopQrCodeListInflater;
	Context ctx;
	String TAG="ShopQrCodeList_Adapter";
	
	public ShopQrCodeList_Adapter(Context context) {
		shopQrCodeListInflater=LayoutInflater.from(context);
		this.ctx=context;
	}
	
	@Override
	public int getCount() {
		return WebServiceStaticArrays.mStoresQRCode.size();
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
		StoresQRCode_ClassVariables obj = (StoresQRCode_ClassVariables) WebServiceStaticArrays.mStoresQRCode.get(position);
		if(convertView==null){
			convertView = shopQrCodeListInflater.inflate(R.layout.shoplistview, null);
			holder=new ViewHolder();
			holder.storeImage=(ImageView) convertView.findViewById(R.id.shoplistview_storeimage);
			holder.storeName=(TextView) convertView.findViewById(R.id.storename);
			holder.storeAddress=(TextView) convertView.findViewById(R.id.storeaddress);
			holder.storeAddress1=(TextView) convertView.findViewById(R.id.storeaddress1);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		Log.i(TAG,"StoreName: "+obj.storeName);
		Log.i(TAG,"Address: "+obj.addressLine1);
		Log.i(TAG,"Address: "+obj.addressLine2);
		
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
