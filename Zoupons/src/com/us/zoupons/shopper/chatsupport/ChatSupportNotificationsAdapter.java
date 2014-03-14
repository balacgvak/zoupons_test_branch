package com.us.zoupons.shopper.chatsupport;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.shopper.giftcards_deals.MyGiftCards;
import com.us.zoupons.shopper.invoice.InvoiceApproval;

/**
 * 
 * Custom adapter to populate notifications
 *
 */

public class ChatSupportNotificationsAdapter extends BaseAdapter{

	private Activity context;
	private ArrayList<Object> mNotifications;

	public ChatSupportNotificationsAdapter(Activity context,ArrayList<Object> Notifications) {
		this.context = context;
		this.mNotifications = Notifications;
	}

	@Override
	public int getCount() {
		return mNotifications.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mNotifications.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		final NotificationDetails mNotificationDetails;
		if(view == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.customercenter_notification_listrow, null);
			holder = new ViewHolder();
			holder.mNotificationDate = (TextView) view.findViewById(R.id.customercenter_notification_timeId);
			holder.mNotificationMessage = (TextView) view.findViewById(R.id.customercenter_notification_messageId);
			holder.mNotificationViewDetails = (TextView) view.findViewById(R.id.customercenter_notification_viewdetailsId);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag(); 
		}
		mNotificationDetails =  (NotificationDetails) getItem(position);
		holder.mNotificationDate.setText(mNotificationDetails.created);
		holder.mNotificationMessage.setText(mNotificationDetails.notification_longmessage);

		if(mNotificationDetails.notification_type.equalsIgnoreCase("giftcard_received")||mNotificationDetails.notification_type.equalsIgnoreCase("invoice_received")){
			holder.mNotificationViewDetails.setVisibility(View.VISIBLE);
		}else{
			holder.mNotificationViewDetails.setVisibility(View.GONE);
		}

		holder.mNotificationViewDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mNotificationDetails.notification_type.equalsIgnoreCase("giftcard_received")){
					Intent intent_zgiftcards = new Intent().setClass(context,MyGiftCards.class);
					context.startActivity(intent_zgiftcards);
				}else if(mNotificationDetails.notification_type.equalsIgnoreCase("invoice_received")){
					Intent intent_approveinvoice = new Intent().setClass(context,InvoiceApproval.class);
					intent_approveinvoice.putExtra("nofooterview", "true");
					context.startActivity(intent_approveinvoice);
				}  
			}
		});
		return view;
	}

	static class ViewHolder {
		TextView mNotificationMessage,mNotificationDate,mNotificationViewDetails;
	}
}

