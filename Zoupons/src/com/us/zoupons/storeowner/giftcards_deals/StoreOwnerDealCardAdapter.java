package com.us.zoupons.storeowner.giftcards_deals;

import org.w3c.dom.Text;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.classvariables.POJOCardDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.screendensity.GetScreenDensity;


/**
 * 
 * Custom adapter class which populates store deal card details
 *
 */

public class StoreOwnerDealCardAdapter extends BaseAdapter{

	private Activity mContext;
	private LayoutInflater mStoreOwnerDealCardsInflater;
	private int mListRowLayout;
	private GetScreenDensity getscreendensity;
	private int size[];
	private LinearLayout mDealsStatusInfoContainer;
	private RelativeLayout mDealsListHeaderContainer;
	private ScrollView mDealsDetailContainer;
	private ListView mDealsListView;
	private TextView mFooterText,mStoreOwnerGiftCardsAvailableFooter,mStoreOwnerGiftCardsPurchasedFooter,mDealcardFaceValue;
	private EditText mChargeValue,mCountPerWeek;
	
	public StoreOwnerDealCardAdapter(Activity context, int cardlistRow, LinearLayout dealsStatusInfoContainer, RelativeLayout dealsListHeaderContainer, ListView dealsListView, ScrollView dealsDetailContainer, TextView footerText, TextView storeOwnerGiftCardsAvailable, LinearLayout menuSplitter2, TextView storeOwnerGiftCardsPurchased,EditText chargeValue,EditText count_per_week,TextView facevalue) {
		this.mContext = context;
		this.mListRowLayout = cardlistRow;
		this.mDealsStatusInfoContainer = dealsStatusInfoContainer;
		this.mDealsListHeaderContainer = dealsListHeaderContainer;
		this.mDealsListView = dealsListView;
		this.mDealsDetailContainer = dealsDetailContainer;
		this.mFooterText = footerText;
		this.mStoreOwnerGiftCardsPurchasedFooter = storeOwnerGiftCardsPurchased;
		this.mStoreOwnerGiftCardsAvailableFooter = storeOwnerGiftCardsAvailable;
		this.mChargeValue = chargeValue;
		this.mCountPerWeek = count_per_week;
		this.mDealcardFaceValue = facevalue;
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
				int facevalue_padding_in_dp = 120 ;
			    final float scale = mContext.getResources().getDisplayMetrics().density;
			    int facevalue_padding_in_px = (int) (facevalue_padding_in_dp * scale + 0.5f);
			    holder.mCardFaceValue.setPadding(0, 0, facevalue_padding_in_px, 0);
			    convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			// Get card details from collection's respective position 
			final POJOCardDetails mCardDetailsObject = (POJOCardDetails) WebServiceStaticArrays.mStoreZouponsDealCardDetails.get(position);
			if(mCardDetailsObject != null) {
				Log.i("sell price", mCardDetailsObject.sellprice);
				mCardDetailsObject.facevalue=String.format("%.2f", Double.parseDouble(mCardDetailsObject.facevalue));
				mCardDetailsObject.sellprice=String.format("%2.2f", Double.parseDouble(mCardDetailsObject.sellprice));
				holder.mCardFaceValue.setText("$"+mCardDetailsObject.facevalue);
				holder.mCardPrice.setVisibility(View.VISIBLE);
				holder.mCardPrice.setText("$"+mCardDetailsObject.sellprice);
				holder.mCardRemaining.setVisibility(View.VISIBLE);
				holder.mCardRemaining.setText(mCardDetailsObject.remaining_count+"/"+mCardDetailsObject.actual_count_per_week);
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
					try{
						mDealsListView.setVisibility(View.GONE);
						mDealsListHeaderContainer.setVisibility(View.GONE);
						mDealsStatusInfoContainer.setVisibility(View.GONE);
						mDealsDetailContainer.setVisibility(View.VISIBLE);
						mFooterText.setText("Back");
						mFooterText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.back, 0, 0);
						mFooterText.setBackgroundResource(R.drawable.header_2);
						if(holder.mBuyButton.getText().toString().equalsIgnoreCase("Edit")){ // Edit deals details 
							mStoreOwnerGiftCardsAvailableFooter.setText("Inactivate");
							mStoreOwnerGiftCardsAvailableFooter.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.back, 0, 0);
							// Adding text change listener
							mChargeValue.setText(mCardDetailsObject.sellprice);
							// Adding text change listener
							//mChargeValue.addTextChangedListener(mAmountWatcher);
							mCountPerWeek.setText(mCardDetailsObject.actual_count_per_week);
						}else{ // Activate deal deals
							mChargeValue.getText().clear();
							mCountPerWeek.getText().clear();
							mStoreOwnerGiftCardsAvailableFooter.setVisibility(View.INVISIBLE);
						}
						mDealcardFaceValue.setText("$"+mCardDetailsObject.facevalue);
						mChargeValue.setTag(mCardDetailsObject.facevalue); // To use for validation during save
						mStoreOwnerGiftCardsPurchasedFooter.setTag(mCardDetailsObject.id);// can be used while saving 
						mStoreOwnerGiftCardsPurchasedFooter.setText("Save");
						mStoreOwnerGiftCardsPurchasedFooter.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.save, 0, 0);
						mChargeValue.requestFocus();
						InputMethodManager inputMethodManager=(InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					    inputMethodManager.toggleSoftInputFromWindow(mChargeValue.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
					}catch(Exception e){
						e.printStackTrace();
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
