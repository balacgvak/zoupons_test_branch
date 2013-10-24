package com.us.zoupons.storeowner.storedeals;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.cards.ImageLoader;
import com.us.zoupons.screendensity.GetScreenDensity;

public class CustomStoreDealsAdapter extends BaseAdapter{
	String TAG="CustomCardDetailsAdapter";
	private Activity mContext;
	private int mListRowLayout;
	private ArrayList<Object> mCardDetails;
	ImageLoader imageLoader;
	GetScreenDensity getscreendensity;
	int size[];
	public static String mSendGiftCardType="";

	public CustomStoreDealsAdapter(Activity context, int cardlistRow, ArrayList<Object> mStoreRegularCardDetails) {
		this.mContext = context;
		this.mListRowLayout = cardlistRow;
		this.mCardDetails = mStoreRegularCardDetails;
		imageLoader = new ImageLoader(context);
		getscreendensity = new GetScreenDensity(mContext);
		size = getscreendensity.getListViewSize();

	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int position) {
		return mCardDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder;
		if(view == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			view = inflater.inflate(this.mListRowLayout, null);
			holder = new ViewHolder();
			holder.mCardFaceValue = (TextView)view.findViewById(R.id.store_card_facevalueId);
			holder.mCardPrice = (TextView)view.findViewById(R.id.store_card_sellpriceId);
			holder.mActivateButton = (Button) view.findViewById(R.id.buybuttonId);
			holder.mCardImage = (ImageView) view.findViewById(R.id.store_card_listitemimage);
			holder.mCardFaceValueOverImage = (TextView) view.findViewById(R.id.store_card_facevalueoverimage);
			holder.mCardRemaining = (TextView) view.findViewById(R.id.store_card_remainingId);
			holder.mRemainingText = (TextView) view.findViewById(R.id.remainingtextId);
			holder.mStatusImage = (ImageView) view.findViewById(R.id.cardstatusImageId);
			view.setTag(holder);
		}else{
			holder=(ViewHolder) view.getTag();
		}
		// To hide unwanted fields from customer deals and giftcards
		holder.mCardPrice.setVisibility(View.GONE);
		holder.mCardRemaining.setVisibility(View.GONE);
		holder.mRemainingText.setVisibility(View.GONE);
		holder.mCardFaceValue.setPadding(0, 0, 0, 0);
		//To set values
		holder.mActivateButton.setVisibility(View.VISIBLE);
		holder.mActivateButton.setText("Activate");
		String imagepath=" https://www.zoupons.com/demo3/assets/thumb/timthumb.php?src=https://www.zoupons.com/demo3/assets/images/gift_app1.png"+"&w="+size[0]+"&h="+size[1]+"&zc=0";
		imageLoader.DisplayImage(imagepath, holder.mCardImage);
		holder.mCardFaceValueOverImage.setText("$"+"100");
		// for test
		if(position == 4){
			holder.mActivateButton.setVisibility(View.GONE);
			holder.mStatusImage.setVisibility(View.VISIBLE);
		}else{
			holder.mActivateButton.setVisibility(View.VISIBLE);
			holder.mStatusImage.setVisibility(View.GONE);
		}
		return view;
	}
	static class ViewHolder {
		TextView mCardFaceValue,mCardPrice,mCardFaceValueOverImage,mCardRemaining,mRemainingText;
		ImageView mCardImage,mStatusImage;
		Button mActivateButton;
	}
}