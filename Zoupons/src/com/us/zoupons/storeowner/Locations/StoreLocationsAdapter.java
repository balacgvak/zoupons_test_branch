package com.us.zoupons.storeowner.Locations;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.POJOStoreInfo;

public class StoreLocationsAdapter extends BaseAdapter{
	Context mContext;
	LayoutInflater mStoreLocationsInflater;
	String[] Values;
	String mClassName;
	ArrayList<Object> mStoreLocationList;

	public StoreLocationsAdapter(Context ctx,ArrayList<Object> storeLocationList){
		this.mStoreLocationsInflater=LayoutInflater.from(ctx);
		this.mStoreLocationList = storeLocationList;
		this.mContext=ctx;
	}
	@Override
	public int getCount() {
		return mStoreLocationList.size();
	}

	@Override
	public Object getItem(int position) {
		return mStoreLocationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try{
			ViewHolder holder;
			if(convertView==null){
				convertView=mStoreLocationsInflater.inflate(R.layout.storeowner_homepage_listrow, null);
				holder=new ViewHolder();
				holder.mListItemAddress=(TextView) convertView.findViewById(R.id.listview_address);
				holder.mListItemCity=(TextView) convertView.findViewById(R.id.listview_city);
				holder.mListItemState=(TextView) convertView.findViewById(R.id.listview_state);
				holder.mListItemZipCode=(TextView) convertView.findViewById(R.id.listview_zipcode);
				holder.mListItemImage=(ImageView) convertView.findViewById(R.id.listitem_image);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			POJOStoreInfo mStoreLocationDetails = (POJOStoreInfo) mStoreLocationList.get(position);
			holder.mListItemAddress.setText(mStoreLocationDetails.address_line1);
			holder.mListItemCity.setText(mStoreLocationDetails.city+",");
			holder.mListItemState.setText(mStoreLocationDetails.state);
			holder.mListItemZipCode.setText(mStoreLocationDetails.zip_code);

			if(mStoreLocationDetails.status.equalsIgnoreCase("active")){
				holder.mListItemImage.setImageResource(R.drawable.green_callout);
			}else if(mStoreLocationDetails.status.equalsIgnoreCase("inactive")){
				holder.mListItemImage.setImageResource(R.drawable.red_callout);
			}else if(mStoreLocationDetails.status.equalsIgnoreCase("pending")){
				holder.mListItemImage.setImageResource(R.drawable.orange_callout);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	static class ViewHolder{
		private TextView mListItemAddress,mListItemCity,mListItemState,mListItemZipCode;
		private ImageView mListItemImage,mListItemArrow,mListItemColor;
	}
}
