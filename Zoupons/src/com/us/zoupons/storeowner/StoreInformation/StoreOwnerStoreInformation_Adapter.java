package com.us.zoupons.storeowner.StoreInformation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;

public class StoreOwnerStoreInformation_Adapter extends BaseAdapter{
	Context mContext;
	LayoutInflater mStoreOwnerStoreInformationInflater;
	String[] Values;
	String mClassName;
	
	public StoreOwnerStoreInformation_Adapter(Context ctx,String classname){
		this.mStoreOwnerStoreInformationInflater=LayoutInflater.from(ctx);
		this.mContext=ctx;
		this.mClassName=classname;
		if(!this.mClassName.equalsIgnoreCase("StoreOwner_StoreInformation")){
			Values = new String[10];
			Values[0] = "2030 Main St., Suite 325";
			Values[1] = "720 Washington Avenue Southeast";
			Values[2] = "375 Longwood Avenue";
			Values[3] = "5879 Kanan Road";
			Values[4] = "132 North Tejon Street";
			Values[5] = "501 East Kennedy Boulevard";
			Values[6] = "221 Spring Street";
			Values[7] = "1801 East Carson Street";
			Values[8] = "2387 West Brooklyn Road";
			Values[9] = "1040 Main St., Vivekanandar Road";
		}else{
			Values = new String[6];
			Values[0] = "General Info";
			Values[1] = "Store Owner Settings";
			Values[2] = "Employees";
			Values[3] = "Locations";
			Values[4] = "Billing";
			Values[5] = "Deal Cards";
		}
	}
	@Override
	public int getCount() {
		return Values.length;
	}

	@Override
	public Object getItem(int position) {
		return Values[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			if(this.mClassName.equalsIgnoreCase("StoreOwner_StoreInformation")){
				convertView=mStoreOwnerStoreInformationInflater.inflate(R.layout.storeowner_storeinformation_listrow, null);
			}else{
				convertView=mStoreOwnerStoreInformationInflater.inflate(R.layout.storeowner_locations_listrow, null);
			}
			holder=new ViewHolder();
			holder.mListItemImage=(ImageView) convertView.findViewById(R.id.listitem_image);
			holder.mListItem=(TextView) convertView.findViewById(R.id.listitem_id);
			holder.mListItemCount=(TextView) convertView.findViewById(R.id.listitemcount_id);
			holder.mListItemArrow=(ImageView) convertView.findViewById(R.id.listitem_arrow_Id);
			holder.mListItemColor=(ImageView) convertView.findViewById(R.id.listitem_roundimage_Id);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		if(this.mClassName.equalsIgnoreCase("StoreOwner_StoreInformation")){
			holder.mListItem.setText(Values[position]);
			holder.mListItemArrow.setVisibility(View.GONE);
		}else{
			holder.mListItem.setText(Values[position]);
			holder.mListItemArrow.setVisibility(View.VISIBLE);
			switch(position){
			case 1:
				holder.mListItemImage.setImageResource(R.drawable.red_callout);
				break;
			case 2:
				holder.mListItemImage.setImageResource(R.drawable.green_callout);
				break;
			case 3:
				holder.mListItemImage.setImageResource(R.drawable.orange_callout);
				break;
			case 4:
				holder.mListItemImage.setImageResource(R.drawable.green_callout);
				break;	
			case 5:
				holder.mListItemImage.setImageResource(R.drawable.orange_callout);
				break;
			case 6:
				holder.mListItemImage.setImageResource(R.drawable.red_callout);
				break;
			case 7:
				holder.mListItemImage.setImageResource(R.drawable.green_callout);
				break;
			case 8:
				holder.mListItemImage.setImageResource(R.drawable.orange_callout);
				break;
			case 9:
				holder.mListItemImage.setImageResource(R.drawable.red_callout);
				break;
			case 10:
				holder.mListItemImage.setImageResource(R.drawable.orange_callout);
				break;	
			default:
				holder.mListItemImage.setImageResource(R.drawable.green_callout);
				break;
			}
		}
		return convertView;
	}

	static class ViewHolder{
		private TextView mListItem,mListItemCount;
		private ImageView mListItemImage,mListItemArrow,mListItemColor;
	}
}
