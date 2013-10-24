package com.us.zoupons.storeowner.DashBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.us.zoupons.R;

public class StoreOwnerDashBoard_Adapter extends BaseAdapter{

	Context mContext;
	LayoutInflater mStoreOwnerHomePageInflater;
	String[] Values = {"Active Coupons","Daily ZCard Sales","Daily GiftCard Sales","GiftCard Balances","Daily ZPay Sales","New Messages","Store Rating"};

	public StoreOwnerDashBoard_Adapter(Context ctx){
		this.mStoreOwnerHomePageInflater=LayoutInflater.from(ctx);
		this.mContext=ctx;
	}

	@Override
	public int getCount() {
		return Values.length;
	}

	@Override
	public Object getItem(int position) {
		return Values[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			convertView=mStoreOwnerHomePageInflater.inflate(R.layout.storeowner_dashboard_listrow, null);
			holder=new ViewHolder();
			holder.mListItem=(TextView) convertView.findViewById(R.id.listitem_id);
			holder.mListItemCount=(TextView) convertView.findViewById(R.id.listitemcount_id);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		holder.mListItem.setText(Values[position]);

		return convertView;
	}

	static class ViewHolder{
		private TextView mListItem,mListItemCount;
	}
}
