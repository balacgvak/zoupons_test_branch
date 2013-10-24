package com.us.zoupons.storeowner.GiftCards;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.POJOCardDetails;
import com.us.zoupons.cards.ImageLoader;
import com.us.zoupons.screendensity.GetScreenDensity;

public class StoreOwnerGiftCardAdapter extends BaseAdapter{

	Activity mContext;
	LayoutInflater mStoreOwnerGiftCardsInflater;
	private int mListRowLayout;
	ImageLoader imageLoader;
	GetScreenDensity getscreendensity;
	int size[];
	String mClassName;
	private ArrayList<Object> cardList;

	public StoreOwnerGiftCardAdapter(Activity context, int cardlistRow,String classname,ArrayList<Object> cardlist) {
		this.mContext = context;
		this.mListRowLayout = cardlistRow;
		this.mClassName = classname;
		imageLoader = new ImageLoader(context);
		getscreendensity = new GetScreenDensity(mContext);
		size = getscreendensity.getListViewSize();
		this.cardList = cardlist;
		this.mStoreOwnerGiftCardsInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return cardList.size();
	}

	@Override
	public Object getItem(int position) {
		return cardList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			convertView = mStoreOwnerGiftCardsInflater.inflate(this.mListRowLayout, null);
			holder = new ViewHolder();
			holder.mCardFaceValue = (TextView)convertView.findViewById(R.id.store_card_facevalueId);
			holder.mCardPrice = (TextView)convertView.findViewById(R.id.store_card_sellpriceId);
			holder.mCardRemaining = (TextView)convertView.findViewById(R.id.store_card_remainingId);
			holder.mBuyButton = (Button) convertView.findViewById(R.id.buybuttonId);
			holder.mCardImage = (ImageView) convertView.findViewById(R.id.store_card_listitemimage);
			holder.mCardFaceValueOverImage = (TextView) convertView.findViewById(R.id.store_card_facevalueoverimage);
			holder.mRemainingText = (TextView) convertView.findViewById(R.id.remainingtextId);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		// To set layout params for sell button.
		RelativeLayout.LayoutParams mBuybuttonLayoutParams = new RelativeLayout.LayoutParams(size[0]-10, size[1]);
		mBuybuttonLayoutParams.leftMargin=30;
		mBuybuttonLayoutParams.rightMargin=5;
		mBuybuttonLayoutParams.bottomMargin=10;
		mBuybuttonLayoutParams.topMargin=10;
		mBuybuttonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		mBuybuttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		holder.mBuyButton.setLayoutParams(mBuybuttonLayoutParams);
		// Get card details from collection's respective position 
		final POJOCardDetails mCardDetailsObject = (POJOCardDetails) cardList.get(position);
		if(mClassName.equalsIgnoreCase("Regular")){
			holder.mCardFaceValue.setVisibility(View.GONE);
			holder.mCardPrice.setVisibility(View.VISIBLE);
			holder.mCardRemaining.setVisibility(View.GONE);
			holder.mCardRemaining.setVisibility(View.GONE);
			holder.mBuyButton.setVisibility(View.VISIBLE);
			holder.mBuyButton.setText("Sell");
			holder.mRemainingText.setVisibility(View.GONE);
			holder.mCardPrice.setPadding(0, 0, 0, 0);
			holder.mCardPrice.setText("$"+String.format("%.2f", Double.parseDouble(mCardDetailsObject.facevalue)));
			imageLoader.DisplayImage(mCardDetailsObject.imagepath+"&w="+size[0]+"&h="+size[1]+"&zc=0", holder.mCardImage);
			holder.mCardFaceValueOverImage.setText("$"+String.format("%.2f", Double.parseDouble(mCardDetailsObject.facevalue)));
		}else{
			holder.mCardFaceValue.setText("$"+String.format("%.2f", Double.parseDouble(mCardDetailsObject.facevalue)));
			holder.mCardPrice.setText("$"+String.format("%.2f", Double.parseDouble(mCardDetailsObject.sellprice)));
			holder.mCardRemaining.setVisibility(View.VISIBLE);
			holder.mCardRemaining.setText(mCardDetailsObject.remaining_count);
			imageLoader.DisplayImage(mCardDetailsObject.imagepath+"&w="+size[0]+"&h="+size[1]+"&zc=0", holder.mCardImage);
			holder.mCardFaceValueOverImage.setText("$"+String.format("%.2f", Double.parseDouble(mCardDetailsObject.facevalue)));
			if(Integer.parseInt(mCardDetailsObject.remaining_count)==0){
				holder.mBuyButton.setVisibility(View.GONE);
				holder.mRemainingText.setVisibility(View.VISIBLE);
			}else{
				holder.mBuyButton.setVisibility(View.VISIBLE);
				holder.mBuyButton.setText("Sell");
				holder.mRemainingText.setVisibility(View.GONE);
			}
		}

		holder.mBuyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent giftcard_purchase =  new Intent(mContext,StoreOwnerGiftcardSell.class);
				if(mClassName.equalsIgnoreCase("Regular")){
					giftcard_purchase.putExtra("card_type", "Regular");
					giftcard_purchase.putExtra("card_id", mCardDetailsObject.id);
					giftcard_purchase.putExtra("cardvalue", mCardDetailsObject.facevalue);
				}else {
					giftcard_purchase.putExtra("card_type", "ZCard");
					giftcard_purchase.putExtra("card_id", mCardDetailsObject.card_id);
					giftcard_purchase.putExtra("cardvalue", mCardDetailsObject.sellprice);
				}
				giftcard_purchase.putExtra("facevalue", mCardDetailsObject.facevalue);
				mContext.startActivity(giftcard_purchase);
			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView mCardFaceValue,mCardPrice,mCardFaceValueOverImage,mCardRemaining,mRemainingText;
		ImageView mCardImage;
		Button mBuyButton;
	}
}
