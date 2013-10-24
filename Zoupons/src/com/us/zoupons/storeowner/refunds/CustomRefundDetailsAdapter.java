package com.us.zoupons.storeowner.refunds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;

public class CustomRefundDetailsAdapter extends BaseAdapter{
	Context mContext;
	String[] Date = {"1-05-2013 2:30 pm","1-05-2013 3:30 pm","1-05-2013 4:30 pm"};
	String[] purchaseamount={"$50.00","$100.00","$150.00"};
	String[] refundamount={"$5.00","$10.00","$15.00"};
	String[] netamount={"$45.00","$90.00","$135.00"};
	String flag;
	
	public CustomRefundDetailsAdapter(Context context,String flag){
		this.mContext=context;
		this.flag = flag;
	}
	
	@Override
	public int getCount() {
		return purchaseamount.length;
	}

	@Override
	public Object getItem(int position) {
		return purchaseamount[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.storeowner_refunds_listrow, null);
			holder=new ViewHolder();
			
			holder.mListArrow=(ImageView) convertView.findViewById(R.id.listitem_arrow_Id);
			holder.mPurchasedDate=(TextView) convertView.findViewById(R.id.refund_purchasedate);
			holder.mRefundAmount=(TextView) convertView.findViewById(R.id.refund_totalrefunded);
			holder.mTotalPaid=(TextView) convertView.findViewById(R.id.refund_totalpaid);
			holder.mNetAmount=(TextView) convertView.findViewById(R.id.refund_netamount);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
        if(flag.equalsIgnoreCase("refunds_list")){
        	holder.mNetAmount.setVisibility(View.GONE);
        }else if(flag.equalsIgnoreCase("refunds_details")){
        	holder.mNetAmount.setVisibility(View.VISIBLE);
        	holder.mListArrow.setVisibility(View.GONE);
        	convertView.setPadding(0, 10, 0, 10);
        }
		holder.mPurchasedDate.setText(Date[position]);
		holder.mTotalPaid.setText(purchaseamount[position]);
		holder.mRefundAmount.setText(refundamount[position]);
        holder.mNetAmount.setText(netamount[position]);
		return convertView;
	}
	
	static class ViewHolder{
		private TextView mPurchasedDate,mTotalPaid,mRefundAmount,mNetAmount;
		private ImageView mListArrow;
	}
}