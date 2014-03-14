package com.us.zoupons.listview_inflater_classes;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.mobilepay.Step2AvailableCards;

public class CustomContextMenuAdapter extends BaseAdapter{

	private ArrayList<Object> mTotalCardsList;
	private LayoutInflater mTotalCarsInflator;
	
	public CustomContextMenuAdapter(Context context,ArrayList<Object> totalcards) {
		// TODO Auto-generated constructor stub
		this.mTotalCardsList = totalcards;
		mTotalCarsInflator = LayoutInflater.from(context);
	}
		
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mTotalCardsList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mTotalCardsList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View converView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(converView==null){
			converView=mTotalCarsInflator.inflate(R.layout.card_buy_button_dialog, null);
			holder=new ViewHolder();
			holder.mCardValue=(TextView) converView.findViewById(R.id.optiontext);
			converView.setTag(holder);
		}else{
			holder=(ViewHolder) converView.getTag();
		}
		final Step2AvailableCards parsedobjectvalues = (Step2AvailableCards) mTotalCardsList.get(arg0);
		if(!parsedobjectvalues.mCreditCardMask.equalsIgnoreCase("")){
			holder.mCardValue.setText(parsedobjectvalues.mCreditCardMask);
		}else{
			if(!parsedobjectvalues.mGiftCardBalanceAmount.equalsIgnoreCase("0")){
				holder.mCardValue.setText(parsedobjectvalues.mGiftCard);
			}
		}		
		return converView;
	}

	static class ViewHolder{
		private TextView mCardValue;
	}
	
	
}
