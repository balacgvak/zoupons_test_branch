package com.us.zoupons.Communication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.storeowner.communication.StoreOwner_ContactStore;

public class TalkToUs_ContactStore_Adapter extends BaseAdapter{
	
	private LayoutInflater mTalkToUs_ContactStore_Inflater;
	Context context;
	String TAG="TalkToUs_ContactStore_Adapter";
	
	public TalkToUs_ContactStore_Adapter(Context context){
		this.context=context;
		this.mTalkToUs_ContactStore_Inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return WebServiceStaticArrays.mContactUsResponse.size();
	}

	@Override
	public Object getItem(int position) {
		return WebServiceStaticArrays.mContactUsResponse.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if(convertView==null){
			convertView=mTalkToUs_ContactStore_Inflater.inflate(R.layout.talktous_contatctstore_listview, null);
			holder=new ViewHolder();
			holder.mStoreMessageContainer=(LinearLayout) convertView.findViewById(R.id.talktous_contactstore_storecontainer);
			holder.mUserMessageContainer=(LinearLayout) convertView.findViewById(R.id.talktous_contactstore_usercontainer);
			holder.mStoreMessage_header=(TextView) convertView.findViewById(R.id.talktous_store);
			holder.mUserMessage_header=(TextView) convertView.findViewById(R.id.talktous_me);
			holder.mMessageTimeStore=(TextView) convertView.findViewById(R.id.talktous_contactstore_messagetime_store);
			holder.mMessageTimeUser=(TextView) convertView.findViewById(R.id.talktous_contactstore_messagetime_customer);
			holder.mStoreMessage=(TextView) convertView.findViewById(R.id.talktous_contactstore_message_store);
			holder.mUserMessage=(TextView) convertView.findViewById(R.id.talktous_contactstore_message_customer);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponse.get(position);
		
		
		if(obj.mUserType.equalsIgnoreCase("shopper") && !obj.mToStoreId.equalsIgnoreCase("0")){ //From Customer to Store after customer has sent message to store and default list of all chat messages
			holder.mUserMessageContainer.setVisibility(View.VISIBLE);
			holder.mStoreMessageContainer.setVisibility(View.GONE);
			String customerheader="";
			if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore")){
				customerheader = StoreOwner_ContactStore.mCustomerName+" : ";	
				holder.mUserMessageContainer.setBackgroundColor(context.getResources().getColor(R.color.chatblue));
			}else{
				customerheader = "ME : ";
			}
			String customermessage = obj.mQuery;
			
			holder.mUserMessage_header.setText(customerheader);
			holder.mMessageTimeUser.setText(obj.mPostedTime);
			holder.mUserMessage.setText(customermessage);
		}else if(obj.mUserType.equalsIgnoreCase("shopper") && obj.mToStoreId.equalsIgnoreCase("0")){//Customer to zoupons after customer has sent message to zoupons and default list of all chat messages
			holder.mUserMessageContainer.setVisibility(View.VISIBLE);
			holder.mStoreMessageContainer.setVisibility(View.GONE);
			holder.mUserMessageContainer.setBackgroundColor(context.getResources().getColor(R.color.white));
			
			String customerheader = "ME : ";
			String customermessage = obj.mQuery;
			
			holder.mUserMessage_header.setText(customerheader);
			holder.mMessageTimeUser.setText(obj.mPostedTime);
			holder.mUserMessage.setText(customermessage);
		}else if((obj.mUserType.equalsIgnoreCase("admin") || obj.mUserType.equalsIgnoreCase("admin_employee")) && obj.mFromStoreId.equalsIgnoreCase("0")){// zoupons sent message to customer and default list all chat message
			holder.mStoreMessageContainer.setVisibility(View.VISIBLE);
			holder.mUserMessageContainer.setVisibility(View.GONE);
			holder.mStoreMessageContainer.setBackgroundColor(context.getResources().getColor(R.color.chatblue));
			String zouponsheader = "ZOUPONS("+obj.mUserName+"): ";
			String zouponsmessage = obj.mQuery;
			
			holder.mStoreMessage_header.setText(zouponsheader);
			holder.mMessageTimeStore.setText(obj.mPostedTime);
			holder.mStoreMessage.setText(zouponsmessage);
		}else{
			holder.mStoreMessageContainer.setVisibility(View.VISIBLE);
			holder.mUserMessageContainer.setVisibility(View.GONE);
			if(context.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_ContactStore")){
				holder.mStoreMessageContainer.setBackgroundColor(context.getResources().getColor(R.color.white));
			}
			String storeheader = "STORE("+obj.mUserName+"): ";
			String storemessage = obj.mQuery;
			
			holder.mStoreMessage_header.setText(storeheader);
			holder.mMessageTimeStore.setText(obj.mPostedTime);
			holder.mStoreMessage.setText(storemessage);
		}
		return convertView;
	}

	static class ViewHolder{
		private TextView mMessageTimeStore,mMessageTimeUser,mStoreMessage_header,mUserMessage_header,mStoreMessage,mUserMessage;
		private LinearLayout mStoreMessageContainer,mUserMessageContainer;
	}

}