package com.us.zoupons.shopper.invoice;

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
 * Custom Adapter to populate invoice list 
 *
 */


public class CustomInvoiceAdapter extends BaseAdapter {

	private Context context;
	private ImageLoader imageLoader; 
	private ArrayList<Object> mInvoiceDetails;

	public CustomInvoiceAdapter(Context context, ArrayList<Object> SearchedReceiptDetails) {
		this.context = context;
		this.mInvoiceDetails = SearchedReceiptDetails;
		this.imageLoader=new ImageLoader(context);
	}

	public int getCount() {
		System.out.println("Sample");
		return mInvoiceDetails.size();
	}

	public Object getItem(int position) {
		return mInvoiceDetails.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
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
		// To get invoice details for each list item
		POJOInvoiceList mInvoiceInformation = (POJOInvoiceList) mInvoiceDetails.get(position);
		if(mInvoiceInformation != null) {
			holder.mStoreName.setText(mInvoiceInformation.StoreName);
			imageLoader.DisplayImage(mInvoiceInformation.LogoPath,holder.mStoreImage);
			holder.mReceiptsDate.setText(mInvoiceInformation.CreatedDate);
			holder.mReceiptsTotalAmount.setText("$"+mInvoiceInformation.Amount);
		}
		return view;
	}

	static class ViewHolder {
		TextView mStoreName,mReceiptsDate,mReceiptsTotalAmount;
		ImageView mStoreImage;
	}
}