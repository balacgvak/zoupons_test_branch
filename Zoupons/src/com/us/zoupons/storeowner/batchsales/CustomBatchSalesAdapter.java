package com.us.zoupons.storeowner.batchsales;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.us.zoupons.R;

/**
 * 
 * Custom adapter class to display batch sales details
 *
 */

public class CustomBatchSalesAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<Object> mBatchDetailsList;
	private String mEventFlag;

	public CustomBatchSalesAdapter(Context context, ArrayList<Object> InvoiceDetails, String EventFlag) {
		this.mContext = context;
		this.mBatchDetailsList = InvoiceDetails;
		this.mEventFlag = EventFlag;
	}

	public int getCount() {
		return mBatchDetailsList.size();
	}

	public Object getItem(int position) {
		return mBatchDetailsList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		try{
			ViewHolder holder;
			if(view == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				view = inflater.inflate(R.layout.storeowner_batchsales_listrow, null);
				holder = new ViewHolder();
				holder.mUserName = (TextView)view.findViewById(R.id.batchsales_listuser_nameId);
				holder.mAmount = (TextView)view.findViewById(R.id.batchsales_amountId);
				holder.mTip = (TextView)view.findViewById(R.id.batchsales_tipId);
				holder.mZouponsFeeAmount = (TextView)view.findViewById(R.id.batchsales_zouponsfeeId);
				holder.mNetAmount = (TextView) view.findViewById(R.id.batchsales_netamountId);
				holder.mTransactionDate = (TextView) view.findViewById(R.id.batchsales_listdateId);
				view.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			// To get Batch Sales details from collection for each list row
			BatchDetailsClassVariables mBatchSalesDetails = (BatchDetailsClassVariables) mBatchDetailsList.get(position);
			if(mEventFlag.equalsIgnoreCase("list")){ // Sort by date
				holder.mUserName.setText(mBatchSalesDetails.customer_name);
				holder.mTransactionDate.setText(mBatchSalesDetails.transaction_date);
			}else{ // Sort by Employee
				holder.mUserName.setText(mBatchSalesDetails.employee_name);
				holder.mTransactionDate.setVisibility(View.GONE);
			}
			holder.mAmount.setText("$"+String.format("%.2f", Double.parseDouble(mBatchSalesDetails.amount)));
			holder.mTip.setText("$"+String.format("%.2f", Double.parseDouble(mBatchSalesDetails.tip)));
			holder.mZouponsFeeAmount.setText("$"+String.format("%.2f", Double.parseDouble(mBatchSalesDetails.zouponsfee)));
			holder.mNetAmount.setText("$"+String.format("%.2f", Double.parseDouble(mBatchSalesDetails.net_amount)));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	static class ViewHolder {
		TextView mUserName,mTransactionDate,mAmount,mTip,mNetAmount,mZouponsFeeAmount;
	}
}
