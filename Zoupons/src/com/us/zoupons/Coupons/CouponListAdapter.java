package com.us.zoupons.Coupons;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;

public class CouponListAdapter extends BaseAdapter{
	
	private LayoutInflater mCouponsInflater;
	Context ctx;

	public CouponListAdapter(Context context) {
		mCouponsInflater = LayoutInflater.from(context);
		this.ctx = context;
	}

	@Override
	public int getCount() {
		
		if(WebServiceStaticArrays.mStaticCouponsArrayList.size() >0){
			return WebServiceStaticArrays.mStaticCouponsArrayList.size();
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
	public View getView(int position, View convertview, ViewGroup parent) {
		ViewHolder holder = null;		
		if(convertview == null){
			Log.i("Holder", "Not Null");
			convertview = mCouponsInflater.inflate(R.layout.coupon_list_item,null);
			holder = new ViewHolder();
			holder.mLogoImage = (ImageView) convertview.findViewById(R.id.coupons_image);
			holder.mStoreName = (TextView) convertview.findViewById(R.id.mCoupons_storename);
			holder.mItemName = (TextView) convertview.findViewById(R.id.mCouponsListItem);
			holder.mDummy = (TextView) convertview.findViewById(R.id.mCouponsDummy);
			convertview.setTag(holder);
		}else{
			holder = (ViewHolder) convertview.getTag();
		}
		if(WebServiceStaticArrays.mStaticCouponsArrayList.size() >0){
			final POJOCouponsList mPList = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(position);
			holder.mLogoImage.setBackgroundResource(R.drawable.couponslistimage);
			if(!mPList.mCouponTitle.equals("")){
				holder.mStoreName.setVisibility(View.GONE);
				holder.mDummy.setVisibility(View.GONE);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) LayoutParams.FILL_PARENT, (int) LayoutParams.WRAP_CONTENT);
				params.setMargins(5, 0, 0, 0);
				params.addRule(RelativeLayout.RIGHT_OF, R.id.coupons_image);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				holder.mItemName.setLayoutParams(params);
				holder.mItemName.setText(mPList.mCouponTitle);
			}else if(!mPList.mFavorite_CouponTitle.equals("")){
				holder.mStoreName.setVisibility(View.VISIBLE);
				holder.mDummy.setVisibility(View.VISIBLE);
				holder.mStoreName.setText(mPList.mFavorite_StoreName);
				holder.mItemName.setText(mPList.mFavorite_CouponTitle);
			}
		}else{
			holder.mLogoImage.setBackgroundResource(R.drawable.couponslistimage);
			holder.mLogoImage.setVisibility(View.INVISIBLE);
			holder.mStoreName.setVisibility(View.GONE);
			holder.mDummy.setVisibility(View.GONE);
			holder.mItemName.setText("No Coupons available...");	
		}
		return convertview;
	}
	
	static class ViewHolder{
		
		private ImageView mLogoImage;
		private TextView mItemName,mStoreName,mDummy;
	}
}