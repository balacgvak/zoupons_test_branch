package com.us.zoupons.storeowner.customercenter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.shopper.cards.ImageLoader;

/**
 * 
 * Custom adapter to populate store favorite customer's list
 *
 */

public class CustomFavouriteUserAdapter extends BaseAdapter{
	private LayoutInflater mStoreFavouriteUserInflater;
	private ImageLoader mImageLoader;
	private ArrayList<Object> mcustomerlist;
	
	public CustomFavouriteUserAdapter(Context ctx,ArrayList<Object> customerlist){
		this.mStoreFavouriteUserInflater=LayoutInflater.from(ctx);
		this.mcustomerlist = customerlist;
		this.mImageLoader = new ImageLoader(ctx);
	}
	
	@Override
	public int getCount() {
		return mcustomerlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mcustomerlist.get(position);
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
			convertView=mStoreFavouriteUserInflater.inflate(R.layout.storeowner_storeinformation_listrow, null);
			holder=new ViewHolder();
			holder.mListItemImage=(ImageView) convertView.findViewById(R.id.listitem_image);
			holder.mFavoriteUserName=(TextView) convertView.findViewById(R.id.listitem_id);
			holder.mListItemArrow=(ImageView) convertView.findViewById(R.id.listitem_arrow_Id);
			holder.mFavouriteImage =(ImageView) convertView.findViewById(R.id.listitem_roundimage_Id);
			holder.mTotalTransaction_amount = (TextView) convertView.findViewById(R.id.customer_transaction_amount_Id);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		FavouriteCustomerDetails mCustomerDetails =   (FavouriteCustomerDetails) mcustomerlist.get(position);
		holder.mFavoriteUserName.setText(mCustomerDetails.mCustomerName);
		holder.mListItemArrow.setVisibility(View.VISIBLE);
		mImageLoader.DisplayImage(mCustomerDetails.mCustomerProfileImage, holder.mListItemImage);
		holder.mTotalTransaction_amount.setVisibility(View.VISIBLE);
		holder.mTotalTransaction_amount.setText("$"+String.format("%.2f",Double.parseDouble(mCustomerDetails.mTransactionAmount)));
		if(mCustomerDetails.mIsFavouriteStoreRemoved.equalsIgnoreCase("yes")){
			holder.mFavouriteImage.setVisibility(View.VISIBLE);
			holder.mFavouriteImage.setImageResource(R.drawable.store_like);
		}else{
			holder.mFavouriteImage.setVisibility(View.INVISIBLE);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class ViewHolder{
		private TextView mFavoriteUserName,mTotalTransaction_amount;
		private ImageView mListItemImage,mListItemArrow,mFavouriteImage;
	}
}
