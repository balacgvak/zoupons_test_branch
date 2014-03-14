package com.us.zoupons.storeowner.homepage;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.classvariables.POJOStoreInfo;

/**
 * 
 * Custom adapter to populate store location list in Homepage for store owner or store employee
 *
 */

public class StoreOwnerHomePage_Adapter extends BaseAdapter{
	
	private LayoutInflater mStoreOwnerHomePageInflater;
	private ArrayList<Object> storelocations;
		
	public StoreOwnerHomePage_Adapter(Context ctx, ArrayList<Object> result){
		this.storelocations = result;
		this.mStoreOwnerHomePageInflater=LayoutInflater.from(ctx);
	}

	@Override
	public int getCount() {
		return storelocations.size();
	}

	@Override
	public Object getItem(int position) {
		return storelocations.get(position);
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
				convertView=mStoreOwnerHomePageInflater.inflate(R.layout.storeowner_homepage_listrow, null);
				holder=new ViewHolder();
				holder.mListItemAddress=(TextView) convertView.findViewById(R.id.listview_address);
				holder.mListItemCity=(TextView) convertView.findViewById(R.id.listview_city);
				holder.mListItemState=(TextView) convertView.findViewById(R.id.listview_state);
				holder.mListItemZipCode=(TextView) convertView.findViewById(R.id.listview_zipcode);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			// To get location details for each list item position
			POJOStoreInfo mStoreDetails = (POJOStoreInfo) storelocations.get(position);
			holder.mListItemAddress.setText(mStoreDetails.address_line1);
			holder.mListItemCity.setText(mStoreDetails.city+",");
			holder.mListItemState.setText(mStoreDetails.state);
			holder.mListItemZipCode.setText(mStoreDetails.zip_code);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	static class ViewHolder{
		private TextView mListItemAddress,mListItemCity,mListItemState,mListItemZipCode;
	}
}
