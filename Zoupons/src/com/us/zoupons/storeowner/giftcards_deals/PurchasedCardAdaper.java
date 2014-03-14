package com.us.zoupons.storeowner.giftcards_deals;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.mobilepay.MobilePay;
import com.us.zoupons.screendensity.GetScreenDensity;

/**
 * 
 * Custom adapter to populate puchased card details list
 *
 */

public class PurchasedCardAdaper extends BaseAdapter {

	private Activity mContext;
	private LayoutInflater mPurchasedCardInflater;
	private int mListRowLayout;
	private int size[];
	private GetScreenDensity mGetscreendensity;
	private ArrayList<Object> mPurchasedCardList;
	
	public PurchasedCardAdaper(Activity context, int cardlistRow, ArrayList<Object> purchased_gc_dc_list) {
		this.mContext = context;
		this.mListRowLayout = cardlistRow;
		this.mPurchasedCardList = purchased_gc_dc_list;
		this.mPurchasedCardInflater = LayoutInflater.from(mContext);
		mGetscreendensity = new GetScreenDensity(mContext);
		size = mGetscreendensity.getListViewSize();
	}

	@Override
	public int getCount() {
		return mPurchasedCardList.size();
	}

	@Override
	public Object getItem(int position) {
		return mPurchasedCardList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			convertView = mPurchasedCardInflater.inflate(this.mListRowLayout, null);
			holder = new ViewHolder();
			holder.mPurchasedFaceValue = (TextView) convertView.findViewById(R.id.purchased_facevalueId);
			holder.mPurchasedUserFirstName = (TextView) convertView.findViewById(R.id.purchased_userfirstname);
			holder.mPurchasedUserLastName = (TextView) convertView.findViewById(R.id.purchased_userlastname);
			holder.mPurchasedRemainingBalance = (TextView) convertView.findViewById(R.id.purchased_remaining);
			holder.mUseButton = (Button) convertView.findViewById(R.id.purchased_usebuttonId);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		// To get card details for each list item row
		final PurchaseCardDetails mPurchasedCard = (PurchaseCardDetails) mPurchasedCardList.get(position);
		holder.mPurchasedUserFirstName.setText(mPurchasedCard.customer_first_name);
		holder.mPurchasedUserLastName.setText(mPurchasedCard.customer_last_name);
		holder.mPurchasedFaceValue.setText("$"+String.format("%.2f",Double.parseDouble(mPurchasedCard.face_value)));
		holder.mPurchasedRemainingBalance.setText("$"+String.format("%.2f",Double.parseDouble(mPurchasedCard.remaining_balance)));
		RelativeLayout.LayoutParams mBuybuttonLayoutParams = new RelativeLayout.LayoutParams(size[0]-10, size[1]);
		mBuybuttonLayoutParams.leftMargin=30;
		mBuybuttonLayoutParams.rightMargin=5;
		mBuybuttonLayoutParams.bottomMargin=10;
		mBuybuttonLayoutParams.topMargin=10;
		mBuybuttonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		mBuybuttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		holder.mUseButton.setLayoutParams(mBuybuttonLayoutParams);
		holder.mUseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					if(!(mPurchasedCard.remaining_balance.equalsIgnoreCase("0") | mPurchasedCard.remaining_balance.equalsIgnoreCase("0.0") |mPurchasedCard.remaining_balance.equalsIgnoreCase("0.00") | mPurchasedCard.remaining_balance.equalsIgnoreCase("00.00"))){ 
						// If remaining balance not equals 0
						SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
						String mStoreId = mPrefs.getString("store_id", "");
						String mLocationId = mPrefs.getString("location_id", "");
						String mStoreName = mPrefs.getString("store_name", "");
						Intent intent_step2 = new Intent(mContext,MobilePay.class);
						intent_step2.putExtra("FromPurchasedCardUse", true); 
						intent_step2.putExtra("datasourcename", "zpay"); // flag to set control from giftcard self use menu
						intent_step2.putExtra("card_id", mPurchasedCard.card_id); // selected giftcard id
						intent_step2.putExtra("card_purchase_id", mPurchasedCard.purchase_card_id); // selected giftcard purchase id
						intent_step2.putExtra("cardvalue", mPurchasedCard.remaining_balance);	//Selected GiftCard balance amount
						intent_step2.putExtra("giftcard_storeid", mStoreId);
						intent_step2.putExtra("giftcard_locationid", mLocationId);
						intent_step2.putExtra("giftcard_storename", mStoreName);
						intent_step2.putExtra("user_id", mPurchasedCard.customer_id);
						intent_step2.putExtra("user_type", "Customer");
						intent_step2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						mContext.startActivity(intent_step2);
					}else{ // if balance 0 show alert message
						AlertDialog.Builder mNoBalanceDialog = new AlertDialog.Builder(mContext);
						mNoBalanceDialog.setTitle("Information");
						mNoBalanceDialog.setMessage("Balance is nill so you can use this card for payment");
						mNoBalanceDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
							 	// TODO Auto-generated method stub
							   dialog.dismiss();	
							 }
						});
						mNoBalanceDialog.show();
					}	
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView mPurchasedFaceValue,mPurchasedUserFirstName,mPurchasedRemainingBalance,mPurchasedUserLastName;
		Button mUseButton;
	}
}
