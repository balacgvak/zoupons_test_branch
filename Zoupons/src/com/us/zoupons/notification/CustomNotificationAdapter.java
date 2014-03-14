package com.us.zoupons.notification;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.cards.ImageLoader;

/**
 * 
 * Custom Adapter to populate notification in pop up layout
 *
 */

public class CustomNotificationAdapter extends BaseAdapter{

	private LayoutInflater NotificationInflater;
	private Context mContext;
	private ImageLoader mImageLoader;

	//Constructor for Favorites page
	public CustomNotificationAdapter(Context context){
		NotificationInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mImageLoader = new ImageLoader(context);
		Log.i("notification size", " " + WebServiceStaticArrays.mAllNotifications.size());
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
		if(convertView==null){
			convertView=NotificationInflater.inflate(R.layout.notification_listrow, null);
			holder=new ViewHolder();
			holder.mListItemImage=(ImageView) convertView.findViewById(R.id.listitem_image);
			holder.mNotificationMessage=(TextView) convertView.findViewById(R.id.listitem_id);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		// To get notification detail for each notification list row
		NotificationDetails mNotificationDetails = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(position);
		holder.mNotificationMessage.setText(mNotificationDetails.notification_shortmessage);
		if(mNotificationDetails.id.equalsIgnoreCase("")){
			holder.mListItemImage.setVisibility(View.INVISIBLE);
		}else if(mNotificationDetails.notification_type.equalsIgnoreCase("giftcard_received") || mNotificationDetails.notification_type.equalsIgnoreCase("customer_message")){
			mImageLoader.DisplayImage(mNotificationDetails.friend_image_path+"&w="+60+"&h="+60+"&zc=0", holder.mListItemImage);
		}else{
			mImageLoader.DisplayImage(mNotificationDetails.store_logo_path+"&w="+60+"&h="+60+"&zc=0", holder.mListItemImage);
		}
		if(mNotificationDetails.status.equalsIgnoreCase("read"))
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.notification_read));
		else
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.notification_unread));
		return convertView;
	}

	static class ViewHolder{
		private TextView mNotificationMessage;
		private ImageView mListItemImage;
	}
}
