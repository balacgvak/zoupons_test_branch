package com.us.zoupons.storeowner.Deals;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.POJOCardDetails;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.cards.ImageLoader;
import com.us.zoupons.screendensity.GetScreenDensity;

public class StoreOwnerDealCardAdapter extends BaseAdapter{

	Activity mContext;
	LayoutInflater mStoreOwnerDealCardsInflater;
	private int mListRowLayout;
	ImageLoader imageLoader;
	GetScreenDensity getscreendensity;
	int size[];
	private LinearLayout mDealsStatusInfoContainer,mMenuSplitter2;
	private RelativeLayout mDealsListHeaderContainer,mDealsDetailContainer;
	private ListView mDealsListView;
	private TextView mFooterText,mStoreOwnerGiftCardsAvailableFooter,mStoreOwnerGiftCardsPurchasedFooter;

	public StoreOwnerDealCardAdapter(Activity context, int cardlistRow, LinearLayout dealsStatusInfoContainer, RelativeLayout dealsListHeaderContainer, ListView dealsListView, RelativeLayout dealsDetailContainer, TextView footerText, TextView storeOwnerGiftCardsAvailable, LinearLayout menuSplitter2, TextView storeOwnerGiftCardsPurchased) {
		this.mContext = context;
		this.mListRowLayout = cardlistRow;
		this.mDealsStatusInfoContainer = dealsStatusInfoContainer;
		this.mDealsListHeaderContainer = dealsListHeaderContainer;
		this.mDealsListView = dealsListView;
		this.mDealsDetailContainer = dealsDetailContainer;
		this.mFooterText = footerText;
		this.mStoreOwnerGiftCardsPurchasedFooter = storeOwnerGiftCardsPurchased;
		this.mMenuSplitter2 = menuSplitter2;
		this.mStoreOwnerGiftCardsAvailableFooter = storeOwnerGiftCardsAvailable;
		imageLoader = new ImageLoader(context);
		getscreendensity = new GetScreenDensity(mContext);
		size = getscreendensity.getListViewSize();
		this.mStoreOwnerDealCardsInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return WebServiceStaticArrays.mStoreZouponsDealCardDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return WebServiceStaticArrays.mStoreZouponsDealCardDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try{
			final ViewHolder holder;
			if(convertView == null) {
				convertView = mStoreOwnerDealCardsInflater.inflate(this.mListRowLayout, null);
				holder = new ViewHolder();
				holder.mCardFaceValue = (TextView)convertView.findViewById(R.id.store_card_facevalueId);
				holder.mCardPrice = (TextView)convertView.findViewById(R.id.store_card_sellpriceId);
				holder.mBuyButton = (Button) convertView.findViewById(R.id.buybuttonId);
				holder.mCardImage = (ImageView) convertView.findViewById(R.id.store_card_listitemimage);
				holder.mCardFaceValueOverImage = (TextView) convertView.findViewById(R.id.store_card_facevalueoverimage);
				holder.mCardRemaining = (TextView) convertView.findViewById(R.id.store_card_remainingId);
				holder.mRemainingText = (TextView) convertView.findViewById(R.id.remainingtextId);
				holder.mCardStatus = (ImageView) convertView.findViewById(R.id.cardstatusImageId);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			
			// Get card details from collection's respective position 
			POJOCardDetails mCardDetailsObject = (POJOCardDetails) WebServiceStaticArrays.mStoreZouponsDealCardDetails.get(position);
			if(mCardDetailsObject != null) {
				mCardDetailsObject.facevalue=String.format("%.2f", Double.parseDouble(mCardDetailsObject.facevalue));
				mCardDetailsObject.sellprice=String.format("%.2f", Double.parseDouble(mCardDetailsObject.sellprice));
				holder.mCardFaceValue.setText("$"+mCardDetailsObject.facevalue);
				holder.mCardPrice.setVisibility(View.VISIBLE);
				holder.mCardPrice.setText("$"+mCardDetailsObject.sellprice);
				holder.mCardRemaining.setVisibility(View.VISIBLE);
				holder.mCardRemaining.setText(mCardDetailsObject.remaining_count);
				holder.mRemainingText.setVisibility(View.GONE);
				holder.mCardFaceValueOverImage.setVisibility(View.GONE);
				holder.mBuyButton.setVisibility(View.VISIBLE);
				RelativeLayout.LayoutParams mBuybuttonLayoutParams = new RelativeLayout.LayoutParams(size[0]+10, size[1]+10);
				mBuybuttonLayoutParams.leftMargin=5;
				mBuybuttonLayoutParams.rightMargin=5;
				mBuybuttonLayoutParams.topMargin=5;
				mBuybuttonLayoutParams.bottomMargin=5;
				mBuybuttonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
				mBuybuttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				holder.mBuyButton.setLayoutParams(mBuybuttonLayoutParams);
				if(mCardDetailsObject.status.equalsIgnoreCase("active")){
					holder.mCardImage.setImageResource(R.drawable.green_callout);
					holder.mCardStatus.setImageResource(R.drawable.green_circle);
					holder.mBuyButton.setText("Edit");
				}else{
					holder.mCardImage.setImageResource(R.drawable.red_callout);
					holder.mBuyButton.setText("Activate");
					
				}
			}	
			holder.mBuyButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mDealsListView.setVisibility(View.GONE);
					mDealsListHeaderContainer.setVisibility(View.GONE);
					mDealsStatusInfoContainer.setVisibility(View.GONE);
					mDealsDetailContainer.setVisibility(View.VISIBLE);
					mFooterText.setText("Back");
					mFooterText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.back, 0, 0);
					mStoreOwnerGiftCardsPurchasedFooter.setVisibility(View.INVISIBLE);
					mMenuSplitter2.setVisibility(View.INVISIBLE);
					mStoreOwnerGiftCardsAvailableFooter.setVisibility(View.INVISIBLE);
					if(holder.mBuyButton.getText().toString().equalsIgnoreCase("Edit")){ // Edit deals details 
						
					}else{
						
					}
				}
			});
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	static class ViewHolder {
		TextView mCardFaceValue,mCardPrice,mCardFaceValueOverImage,mCardRemaining,mRemainingText;
		ImageView mCardImage,mCardStatus;
		Button mBuyButton;
	}
}
