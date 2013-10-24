package com.us.zoupons.storeowner.PointOfSale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;

public class PointOfsalePart1Adapter extends BaseAdapter {

	Context mContext;
	String[] name = {"Rouzbeh Aminpour","Maryam Aminpour","Super Star"};
	String[] amount={"50.00","100.00","150.00"};
	String[] tipamount={"5.00","10.00","15.00"};
	String[] tippercentage={"(5%)","(10%)","(15%)"};
	String[] totalamount={"45.00","90.00","135.00"};
	
	public PointOfsalePart1Adapter(Context context){
		this.mContext=context;
	}
	
	@Override
	public int getCount() {
		return amount.length;
	}

	@Override
	public Object getItem(int position) {
		return amount[position];
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.storeowner_pointofsale_part1_listrow, null);
			holder=new ViewHolder();
			
			holder.mListImage=(ImageView) convertView.findViewById(R.id.listview_image);
			holder.mListName=(TextView) convertView.findViewById(R.id.listview_name);
			holder.mListAmount=(TextView) convertView.findViewById(R.id.listview_amount);
			holder.mListTip=(TextView) convertView.findViewById(R.id.listview_tip);
			holder.mTipPercentage=(TextView) convertView.findViewById(R.id.listview_tippercentage);
			holder.mTotalAmount=(TextView) convertView.findViewById(R.id.listview_totalamount);
			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
       
		holder.mListName.setText(name[position]);
		holder.mListAmount.setText(amount[position]);
		holder.mListTip.setText(tipamount[position]);
        holder.mTipPercentage.setText(tippercentage[position]);
        holder.mTotalAmount.setText(totalamount[position]);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	static class ViewHolder{
		private TextView mListName,mListAmount,mListTip,mTipPercentage,mTotalAmount;
		private ImageView mListImage;
	}

}
