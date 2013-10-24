package com.us.zoupons.GiftCards;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;

public class TransactionHistory_Inflater extends BaseAdapter{
	Context mContext;
	LayoutInflater mTransactionHistoryInflater;
	String TAG="TransactionHistory_Inflater";
	
	public TransactionHistory_Inflater(Context ctx){
		this.mTransactionHistoryInflater=LayoutInflater.from(ctx);
		this.mContext=ctx;
	}

	@Override
	public int getCount() {
		return WebServiceStaticArrays.mGiftCardTransactionHistory.size();
	}

	@Override
	public Object getItem(int position) {
		return WebServiceStaticArrays.mGiftCardTransactionHistory.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			convertView=mTransactionHistoryInflater.inflate(R.layout.transaction_list_row, null);
			holder=new ViewHolder();
			holder.mDate=(TextView) convertView.findViewById(R.id.transaction_dateId);
			holder.mTransactionAmount=(TextView) convertView.findViewById(R.id.transaction_amountdeductedId);
			holder.mBalance=(TextView) convertView.findViewById(R.id.transaction_balanceId);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		POJOGiftCardTransactionHistory obj = (POJOGiftCardTransactionHistory) getItem(position);
		
		holder.mDate.setText(obj.mTransactionDate);
		double transactionamount = Double.parseDouble(obj.mTotalAmount);
		if(obj.mStatus.equalsIgnoreCase("pending")){
			holder.mTransactionAmount.setTextColor(this.mContext.getResources().getColor(R.color.red));
			holder.mTransactionAmount.setText("$"+String.format("%.2f",transactionamount)+"(P)");
			holder.mBalance.setText("$"+String.format("%.2f", obj.mBalanceAmount));
		}else if(obj.mStatus.equalsIgnoreCase("approved")){
			holder.mTransactionAmount.setTextColor(this.mContext.getResources().getColor(R.color.red));
			holder.mTransactionAmount.setText("$"+String.format("%.2f",transactionamount)+"(A)");
			holder.mBalance.setText("$"+String.format("%.2f", obj.mBalanceAmount));
		}else if(obj.mStatus.equalsIgnoreCase("rejected")){
			holder.mTransactionAmount.setTextColor(this.mContext.getResources().getColor(R.color.green_font));
			holder.mTransactionAmount.setText("$"+String.format("%.2f",transactionamount)+"(D)");
			holder.mBalance.setText("$"+String.format("%.2f", obj.mBalanceAmount));
		}else if(obj.mStatus.equalsIgnoreCase("NotAvailable")){
			holder.mTransactionAmount.setTextColor(this.mContext.getResources().getColor(R.color.black));
			holder.mTransactionAmount.setText("$"+String.format("%.2f",transactionamount));
			holder.mBalance.setText("$"+String.format("%.2f", transactionamount));
		}
		
		return convertView;
	}
	
	
	static class ViewHolder{
		private TextView mDate,mTransactionAmount,mBalance;
	}
}
