package com.us.zoupons.storeowner.Coupons;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.Coupons.POJOCouponsList;

public class StoreOwnerPrivateCouponsAdapter extends BaseAdapter{
	
	private LayoutInflater mCouponsInflater;
	Context ctx;
	public ArrayList<Object> mCouponsList;
	public boolean isShowNoCouponsText;
		
	public StoreOwnerPrivateCouponsAdapter(Context context,ArrayList<Object> couponsList,boolean showNoCouponsText) {
		mCouponsInflater = LayoutInflater.from(context);
		this.ctx = context;
		this.mCouponsList = couponsList;
		this.isShowNoCouponsText = showNoCouponsText;
	}
	
	@Override
	public int getCount() {
		if(mCouponsList.size()>0){
			return mCouponsList.size();	
		}else if(isShowNoCouponsText){ // True if service return no coupons 
			return 1;
		}else{// False if we switch between coupon type dont show no coupons available text
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return mCouponsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		try{
			ViewHolder holder = null;		
			if(convertview == null){
				convertview = mCouponsInflater.inflate(R.layout.coupons_private_list,null);
				holder = new ViewHolder();
				holder.mItemName = (TextView) convertview.findViewById(R.id.mCouponsHeader);
				holder.mCouponCustomerName = (TextView) convertview.findViewById(R.id.mCoupons_customer);
				convertview.setTag(holder);
			}else{
				holder = (ViewHolder) convertview.getTag();
			}
			if(mCouponsList.size() >0){
				POJOCouponsList obj = (POJOCouponsList) mCouponsList.get(position);
				holder.mItemName.setText(obj.mCouponTitle);
				holder.mCouponCustomerName.setVisibility(View.VISIBLE);
				holder.mCouponCustomerName.setText(obj.mCouponCustomerName);
			}else{
				holder.mCouponCustomerName.setVisibility(View.GONE);
				holder.mItemName.setText("No Coupons available...");	
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return convertview;
	}
	
	static class ViewHolder{
		private TextView mItemName,mCouponCustomerName;
	}
}
