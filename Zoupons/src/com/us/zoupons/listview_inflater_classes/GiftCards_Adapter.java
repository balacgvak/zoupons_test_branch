/**
 * 
 */
package com.us.zoupons.listview_inflater_classes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.screendensity.GetScreenDensity;
import com.us.zoupons.shopper.cards.ImageLoader;
import com.us.zoupons.shopper.giftcards_deals.POJOAllGiftCards;

/**
 * MyGiftCards Adapter
 *
 */
public class GiftCards_Adapter extends BaseAdapter {
	
	private LayoutInflater GiftCardsInflater;
	private Activity GiftCardsContext;
	private ImageLoader imageLoader;
	private GetScreenDensity getscreendensity;
	private int size[];
	
	public GiftCards_Adapter(Activity context){
		GiftCardsInflater=LayoutInflater.from(context);
		this.GiftCardsContext=context;
		imageLoader = new ImageLoader(context);
		getscreendensity = new GetScreenDensity(GiftCardsContext);
		size = getscreendensity.getListViewSize_MyGiftCards();
	}
	
	@Override
	public int getCount() {
		return WebServiceStaticArrays.mAllGiftCardList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView=GiftCardsInflater.inflate(R.layout.giftcardslistview, null);
			holder=new ViewHolder();
			holder.GiftCardImage=(ImageView) convertView.findViewById(R.id.listitem_giftcardimage);
			holder.GiftCardFaceValue=(TextView) convertView.findViewById(R.id.listitem_value);
			holder.GiftCardBalance=(TextView) convertView.findViewById(R.id.listitem_balance);
			holder.GiftCardsFaceValueOverGiftCard=(TextView) convertView.findViewById(R.id.listitem_facevalueoverimage);
			holder.GiftCardsStoreName=(TextView) convertView.findViewById(R.id.listitem_storename);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		POJOAllGiftCards obj = (POJOAllGiftCards) WebServiceStaticArrays.mAllGiftCardList.get(position);
		obj.mFaceValue=String.format("%.2f", Double.parseDouble(obj.mFaceValue));
		obj.mBalanceAmount=String.format("%.2f", Double.parseDouble(obj.mBalanceAmount));
		holder.GiftCardsStoreName.setText(obj.mStoreName);
		holder.GiftCardFaceValue.setText("$"+obj.mFaceValue);
		holder.GiftCardBalance.setText("$"+obj.mBalanceAmount);
		String imagepath=obj.mGiftCardImage+"&w="+size[0]+"&h="+size[1]+"&zc=0";
		imageLoader.DisplayImage(imagepath, holder.GiftCardImage);
		holder.GiftCardsFaceValueOverGiftCard.setText("$"+obj.mFaceValue);
		return convertView;
	}

	static class ViewHolder{
		private ImageView GiftCardImage/*,NavigationImage*/;
		private TextView GiftCardFaceValue,GiftCardBalance,GiftCardsFaceValueOverGiftCard,GiftCardsStoreName;
	}
}
