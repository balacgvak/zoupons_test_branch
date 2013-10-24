package com.us.zoupons.notification;

import java.util.ArrayList;

import android.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.cards.ImageLoader;

public class CustomNotificationAdapter extends BaseAdapter{

	private LayoutInflater NotificationInflater;
	private Context mContext;
	private ArrayList<Object> mFavouriteStoreList;
	public ImageLoader imageLoader; 
	public String mClassName;
	public String mPageFlag;
	
	//Constructor for Favorites page
	public CustomNotificationAdapter(Context context){
		NotificationInflater = LayoutInflater.from(context);
		this.mContext = context;
	}
		
	@Override
	public int getCount() {
		return WebServiceStaticArrays.mAllNotifications.size();
	}

	@Override
	public Object getItem(int position) {
		return WebServiceStaticArrays.mAllNotifications.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Log.i("favourites", "getview");
		if(convertView==null){
			convertView=NotificationInflater.inflate(android.R.layout.simple_list_item_1, null);
			holder=new ViewHolder();
			holder.mNotificationMessage=(TextView) convertView.findViewById(android.R.id.text1);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		NotificationDetails mNotificationDetails = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(position);
		holder.mNotificationMessage.setTextColor(mContext.getResources().getColor(R.color.black));
		holder.mNotificationMessage.setTextSize(14);
		holder.mNotificationMessage.setPadding(10,10, 10, 10);
		holder.mNotificationMessage.setText(mNotificationDetails.notification_shortmessage);
		return convertView;
	}

	static class ViewHolder{
		private TextView mNotificationMessage;
		
	}
}
