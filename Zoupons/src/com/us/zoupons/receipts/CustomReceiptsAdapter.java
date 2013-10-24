package com.us.zoupons.receipts;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.cards.ImageLoader;

public class CustomReceiptsAdapter extends BaseAdapter {
    
    private Context context;
    public ImageLoader imageLoader; 
    private ArrayList<Object> mSearchedReceiptDetails;
        
    public CustomReceiptsAdapter(Context context, ArrayList<Object> SearchedReceiptDetails) {
       Log.i("adapter", "constructor");
       this.context = context;
       this.mSearchedReceiptDetails = SearchedReceiptDetails;
       imageLoader=new ImageLoader(context);
    }

    public int getCount() {
        return mSearchedReceiptDetails.size();
    }

    public Object getItem(int position) {
        return mSearchedReceiptDetails.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	Log.i("view", "getview");
    	View view = convertView;
    	ViewHolder holder;
    	if(view == null) {
    		LayoutInflater inflater = LayoutInflater.from(context);
    		view = inflater.inflate(R.layout.receipts_listrow, null);
    		holder = new ViewHolder();
    		holder.mStoreName = (TextView)view.findViewById(R.id.receipts_store_nameId);
    		holder.mReceiptsDate = (TextView)view.findViewById(R.id.receipts_dateId);
    		holder.mReceiptsTotalAmount = (TextView)view.findViewById(R.id.receipts_amountId);
    		holder.mStoreImage = (ImageView) view.findViewById(R.id.receipts_store_imageId);
    		view.setTag(holder);
    	}else{
    		holder=(ViewHolder) convertView.getTag();
    	}
	     
	     POJOReceiptsDetails mReceiptInformation = (POJOReceiptsDetails) mSearchedReceiptDetails.get(position);
	     if(mReceiptInformation != null) {
	         holder.mStoreName.setText(mReceiptInformation.store_name);
	         imageLoader.DisplayImage(mReceiptInformation.store_logo+"&w="+90+"&h="+70+"&zc=0",holder.mStoreImage);
	         holder.mReceiptsDate.setText(mReceiptInformation.transaction_date);
	         holder.mReceiptsTotalAmount.setText("$"+mReceiptInformation.total_amount);
	     }
        return view;
    }
    
    static class ViewHolder {
		TextView mStoreName,mReceiptsDate,mReceiptsTotalAmount;
		ImageView mStoreImage;
    }
    
    
    
    
    
    
       
}