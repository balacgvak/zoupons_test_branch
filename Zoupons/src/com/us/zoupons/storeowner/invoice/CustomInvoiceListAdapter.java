package com.us.zoupons.storeowner.invoice;

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
import com.us.zoupons.shopper.cards.ImageLoader;
import com.us.zoupons.shopper.invoice.POJOInvoiceList;

/**
 * 
 * Custom adapter class to populate invoice details
 *
 */

public class CustomInvoiceListAdapter extends BaseAdapter {
    
    private Context mContext;
    private ImageLoader mImageLoader; 
    private ArrayList<Object> mInvoiceDetailsList;
    private String mEventFlag;
        
    public CustomInvoiceListAdapter(Context context, ArrayList<Object> InvoiceDetails, String EventFlag) {
       this.mContext = context;
       this.mInvoiceDetailsList = InvoiceDetails;
       this.mEventFlag = EventFlag;
       mImageLoader=new ImageLoader(context);
    }

    public int getCount() {
        return mInvoiceDetailsList.size();
    }

    public Object getItem(int position) {
        return mInvoiceDetailsList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	Log.i("view", "getview");
    	View view = convertView;
    	try{
    		ViewHolder holder;
    		if(view == null) {
    			LayoutInflater inflater = LayoutInflater.from(mContext);
    			view = inflater.inflate(R.layout.invoice_list_row, null);
    			holder = new ViewHolder();
    			holder.mCustomerName = (TextView)view.findViewById(R.id.invoice_customerNameId);
    			holder.mInvoiceDate = (TextView)view.findViewById(R.id.invoice_dateId);
    			holder.mInvoiceAmount = (TextView)view.findViewById(R.id.invoice_amountId);
    			holder.mInvoiceDays_Status = (TextView)view.findViewById(R.id.invoice_daysId);
    			holder.mCustomerImage = (ImageView) view.findViewById(R.id.invoice_CustomerImageId);
    			view.setTag(holder);
    		}else{
    			holder=(ViewHolder) convertView.getTag();
    		}
            // To get invoice details from collection for each list row
    		POJOInvoiceList mInvoiceDetails = (POJOInvoiceList) mInvoiceDetailsList.get(position);
    		if(mInvoiceDetails != null) {
    			holder.mCustomerImage.setVisibility(View.GONE);
    			holder.mCustomerName.setText(mInvoiceDetails.Customer_Name);
    			mImageLoader.DisplayImage(mInvoiceDetails.Customer_Image+"&w="+70+"&h="+70+"&zc=0",holder.mCustomerImage);
    			if(mEventFlag.equalsIgnoreCase("outstanding")){ // Outstanding invoices
    				holder.mInvoiceDays_Status.setText(mInvoiceDetails.Days);
    				holder.mInvoiceAmount.setText("$"+mInvoiceDetails.Amount);
    				holder.mInvoiceDate.setText(mInvoiceDetails.CreatedDate);
    			}else{ // processed invoices
    				holder.mInvoiceDays_Status.setText(mInvoiceDetails.Status);
    				holder.mInvoiceAmount.setText("$"+mInvoiceDetails.ActualAmount);
    				holder.mInvoiceDate.setText(mInvoiceDetails.TransactionDate);
    			}
    		}
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
        return view;
    }
    
    static class ViewHolder {
		TextView mCustomerName,mInvoiceDate,mInvoiceAmount,mInvoiceDays_Status;
		ImageView mCustomerImage;
    }
}
