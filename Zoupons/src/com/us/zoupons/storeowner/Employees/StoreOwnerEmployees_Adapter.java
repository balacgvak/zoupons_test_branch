package com.us.zoupons.storeowner.Employees;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.cards.ImageLoader;

public class StoreOwnerEmployees_Adapter extends BaseAdapter{
	Context mContext;
	LayoutInflater mStoreOwnerEmployeesInflater;
	ImageLoader mImageLoader;
	
	public StoreOwnerEmployees_Adapter(Context ctx){
		this.mStoreOwnerEmployeesInflater=LayoutInflater.from(ctx);
		this.mContext=ctx;
		this.mImageLoader = new ImageLoader(ctx);
	}
	
	@Override
	public int getCount() {
		return WebServiceStaticArrays.StoreEmloyeesList.size();
	}

	@Override
	public Object getItem(int position) {
		return WebServiceStaticArrays.StoreEmloyeesList.get(position);
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
				convertView=mStoreOwnerEmployeesInflater.inflate(R.layout.storeowner_storeinformation_listrow, null);
				holder=new ViewHolder();
				holder.mListItemImage=(ImageView) convertView.findViewById(R.id.listitem_image);
				holder.mListItem=(TextView) convertView.findViewById(R.id.listitem_id);
				holder.mListItemCount=(TextView) convertView.findViewById(R.id.listitemcount_id);
				holder.mListItemArrow=(ImageView) convertView.findViewById(R.id.listitem_arrow_Id);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			StoreEmployeesClassVariables mEmployeeDetails =   (StoreEmployeesClassVariables) WebServiceStaticArrays.StoreEmloyeesList.get(position);
			holder.mListItem.setText(mEmployeeDetails.mEmployeeName);
			holder.mListItemArrow.setVisibility(View.VISIBLE);
			mImageLoader.DisplayImage(mEmployeeDetails.mEmployeeProfileImage, holder.mListItemImage);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class ViewHolder{
		private TextView mListItem,mListItemCount;
		private ImageView mListItemImage,mListItemArrow;
	}
}
