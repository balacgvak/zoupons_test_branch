package com.us.zoupons.storeowner.Coupons;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.Coupons.POJOCouponsList;

public class StoreOwnerPublicCouponsAdapter extends BaseAdapter{
	
	private LayoutInflater mCouponsInflater;
	Context ctx;
	public ArrayList<Object> mCouponsList;
	boolean isShowNoCouponsText = false;
	
	public StoreOwnerPublicCouponsAdapter(Context context,ArrayList<Object> couponlist,boolean showNoCouponsText) {
		mCouponsInflater = LayoutInflater.from(context);
		this.ctx = context;
		this.mCouponsList = couponlist;
		this.isShowNoCouponsText = showNoCouponsText;
	}
	
	@Override
	public int getCount() {
		if(mCouponsList.size() > 0){
			return mCouponsList.size();	
		}else if(isShowNoCouponsText){ // True if service return no coupons
			return 1;
		}else{ // False if we switch between coupon type dont show no coupons available text
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
				convertview = mCouponsInflater.inflate(R.layout.coupons_public_list,null);
				holder = new ViewHolder();
				holder.mLogoImage = (ImageView) convertview.findViewById(R.id.coupons_image);
				holder.mItemName = (TextView) convertview.findViewById(R.id.mCouponsheader);
				holder.mCouponCode = (TextView) convertview.findViewById(R.id.mCouponscode);
				holder.mCouponStatus = (TextView) convertview.findViewById(R.id.mCouponsstatus);
				convertview.setTag(holder);
			}else{
				holder = (ViewHolder) convertview.getTag();
			}
			if(mCouponsList.size() >0){
				POJOCouponsList obj = (POJOCouponsList) mCouponsList.get(position);
				holder.mLogoImage.setBackgroundResource(R.drawable.couponslistimage);
				holder.mItemName.setText(obj.mCouponTitle);
				holder.mCouponCode.setText(obj.mCouponCode);
				holder.mCouponStatus.setText(obj.mCouponStatus);
			}else{
				holder.mItemName.setText("No Coupons available...");	
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return convertview;
	}
	
	static class ViewHolder{
		private ImageView mLogoImage;
		private TextView mItemName,mCouponCode,mCouponStatus;
	}
}
