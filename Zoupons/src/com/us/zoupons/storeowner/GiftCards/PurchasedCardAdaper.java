package com.us.zoupons.storeowner.GiftCards;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.R;
import com.us.zoupons.cards.ImageLoader;
import com.us.zoupons.screendensity.GetScreenDensity;

public class PurchasedCardAdaper extends BaseAdapter {

	Activity mContext;
	LayoutInflater mPurchasedCardInflater;
	private int mListRowLayout;
	ImageLoader imageLoader;
	GetScreenDensity getscreendensity;
	int size[];
	String mClassName;
	String[] username = {"rouzbeh","maryam","scott","jacob","ratheesh","vinod","bala"};
	String[] facevalue = {"5.00","10.00","20.00","10.00","5.00","25.00","10.00"};
	String[] remaining = {"35","10","24","13","15","6","9"};
	
	public PurchasedCardAdaper(Activity context, int cardlistRow,String classname) {
		this.mContext = context;
		this.mListRowLayout = cardlistRow;
		this.mClassName = classname;
		imageLoader = new ImageLoader(context);
		getscreendensity = new GetScreenDensity(mContext);
		size = getscreendensity.getListViewSize();
		
		this.mPurchasedCardInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return username.length;
	}

	@Override
	public Object getItem(int position) {
		return username[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			convertView = mPurchasedCardInflater.inflate(this.mListRowLayout, null);
			holder = new ViewHolder();
			
			holder.mPurchasedFaceValue = (TextView) convertView.findViewById(R.id.purchased_facevalueId);
			holder.mPurchasedUserName = (TextView) convertView.findViewById(R.id.purchased_username);
			holder.mPurchasedRemainingBalance = (TextView) convertView.findViewById(R.id.purchased_remaining);
			holder.mUseButton = (Button) convertView.findViewById(R.id.purchased_usebuttonId);
			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.mPurchasedUserName.setText(username[position]);
		holder.mPurchasedFaceValue.setText(facevalue[position]);
		holder.mPurchasedRemainingBalance.setText(remaining[position]);
		
		holder.mUseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mClassName.equalsIgnoreCase("StoreOwnerPurchased_GiftCards")){
					Toast.makeText(mContext, "GiftCards : Point Of Sale", Toast.LENGTH_SHORT).show();
				}else if(mClassName.equalsIgnoreCase("StoreOwnerPurchased_DealCards")){
					Toast.makeText(mContext, "DealCards : Point Of Sale", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return convertView;
	}

	static class ViewHolder {
		TextView mPurchasedFaceValue,mPurchasedUserName,mPurchasedRemainingBalance;
		Button mUseButton;
	}
}
