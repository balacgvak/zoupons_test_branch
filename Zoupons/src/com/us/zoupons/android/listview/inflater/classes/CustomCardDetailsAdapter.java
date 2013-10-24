package com.us.zoupons.android.listview.inflater.classes;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.POJOCardDetails;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.cards.FBFriendList;
import com.us.zoupons.cards.FBNotesDescription;
import com.us.zoupons.cards.ImageLoader;
import com.us.zoupons.friends.Friends;
import com.us.zoupons.screendensity.GetScreenDensity;
import com.us.zoupons.zpay.Step2_ManageCards;

/*
 * Custom Adapter class for both regular and Zcard detail list.
 */
public class CustomCardDetailsAdapter extends BaseAdapter{
	String TAG="CustomCardDetailsAdapter";
	private Activity mContext;
	private int mListRowLayout;
	private ArrayList<Object> mCardDetails;
	private String serviceFunction;
	private String[] mBuyOptions = new String[]{"  Self Use","  Send to Friend"};
	ImageLoader imageLoader;
	GetScreenDensity getscreendensity;
	int size[];
	private String FriendName;
	private String FriendId,mIsZouponsFriend;
	public static String mSendGiftCardType="";
	
	public CustomCardDetailsAdapter(Activity context, int cardlistRow, String ServiceFunction, ArrayList<Object> mStoreRegularCardDetails, String friendName, String FriendId, String IsZouponsFriend/*,boolean fromzpaystep1*/) {
		this.mContext = context;
		this.mListRowLayout = cardlistRow;
		this.mCardDetails = mStoreRegularCardDetails;
		this.serviceFunction = ServiceFunction;
		this.FriendName = friendName;
		this.FriendId = FriendId;
		this.mIsZouponsFriend = IsZouponsFriend;
		imageLoader = new ImageLoader(context);
		getscreendensity = new GetScreenDensity(mContext);
		size = getscreendensity.getListViewSize();
		mSendGiftCardType = ServiceFunction;	//Assing ServiceFunction("Regular","ZCard") to use in mobilepay step3 while purchasing giftcard
	}

	@Override
	public int getCount() {
		return mCardDetails.size();
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
		         holder.mBuyButton = (Button) view.findViewById(R.id.buybuttonId);
		         holder.mCardImage = (ImageView) view.findViewById(R.id.store_card_listitemimage);
		         holder.mCardFaceValueOverImage = (TextView) view.findViewById(R.id.store_card_facevalueoverimage);
		         holder.mCardRemaining = (TextView) view.findViewById(R.id.store_card_remainingId);
		         holder.mRemainingText = (TextView) view.findViewById(R.id.remainingtextId);
		         view.setTag(holder);
		     }else{
		    	 holder=(ViewHolder) view.getTag();
		     }
		     if(holder.mBuyButton.getVisibility()==View.INVISIBLE||holder.mBuyButton.getVisibility()==View.GONE){
		    	 holder.mBuyButton.setVisibility(View.VISIBLE);
		     }
		     // Get card details from collection's respective position 
		     POJOCardDetails mCardDetailsObject = (POJOCardDetails) mCardDetails.get(position);
		     if(mCardDetailsObject != null) {
		    	 mCardDetailsObject.facevalue=String.format("%.2f", Double.parseDouble(mCardDetailsObject.facevalue));
		    	 mCardDetailsObject.sellprice=String.format("%.2f", Double.parseDouble(mCardDetailsObject.sellprice));
		    	 holder.mCardFaceValue.setText("$"+mCardDetailsObject.facevalue);
		    	 if(serviceFunction.equalsIgnoreCase("Regular")){	//GiftCard
		    		 holder.mCardPrice.setText("$"+mCardDetailsObject.facevalue);
		    		 holder.mCardRemaining.setVisibility(View.GONE); 	
    			 }else{	//ZCard
    				 holder.mCardRemaining.setVisibility(View.VISIBLE);
    				 holder.mCardRemaining.setText(mCardDetailsObject.remaining_count);
    				 holder.mCardPrice.setText("$"+mCardDetailsObject.sellprice);
    			 }
		    	 String imagepath=mCardDetailsObject.imagepath+"&w="+size[0]+"&h="+size[1]+"&zc=0";
		    	 Log.i(TAG,"ImagePath: "+imagepath);
		    	 imageLoader.DisplayImage(imagepath, holder.mCardImage);
		    	 holder.mCardFaceValueOverImage.setText("$"+mCardDetailsObject.facevalue);
		    	 
		    	 if(serviceFunction.equalsIgnoreCase("Regular")){
		    		 RelativeLayout.LayoutParams mBuybuttonLayoutParams = new RelativeLayout.LayoutParams(size[0]-10, size[1]);
		    		 mBuybuttonLayoutParams.leftMargin=30;
		    		 mBuybuttonLayoutParams.rightMargin=5;
		    		 mBuybuttonLayoutParams.bottomMargin=10;
		    		 mBuybuttonLayoutParams.topMargin=10;
		    		 mBuybuttonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		    		 mBuybuttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		    		 holder.mBuyButton.setLayoutParams(mBuybuttonLayoutParams);
		    		 holder.mRemainingText.setVisibility(View.GONE);
		    	 }else{
		    		 if(Integer.parseInt(mCardDetailsObject.remaining_count)==0){
		    			 holder.mBuyButton.setVisibility(View.GONE);
		    			 holder.mRemainingText.setVisibility(view.VISIBLE);
		    		 }else{
		    			 holder.mRemainingText.setVisibility(View.GONE);
		    			 RelativeLayout.LayoutParams mBuybuttonLayoutParams = new RelativeLayout.LayoutParams(size[0]-10, size[1]);
			    		 mBuybuttonLayoutParams.leftMargin=30;
			    		 mBuybuttonLayoutParams.rightMargin=5;
			    		 mBuybuttonLayoutParams.bottomMargin=10;
			    		 mBuybuttonLayoutParams.topMargin=10;
			    		 mBuybuttonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
			    		 mBuybuttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			    		 holder.mBuyButton.setLayoutParams(mBuybuttonLayoutParams); 
		    		 }
		    	 }
                 
		    	 holder.mBuyButton.setOnClickListener(new OnClickListener() {

		    		 @Override
		    		 public void onClick(View v) {

		    			 final POJOCardDetails mSelectedCardDetails;
		    			 if(serviceFunction.equalsIgnoreCase("Regular")){//GiftCard
		    				 mSelectedCardDetails = (POJOCardDetails) WebServiceStaticArrays.mStoreRegularCardDetails.get(position);	
		    			 }else{//ZCard
		    				 mSelectedCardDetails = (POJOCardDetails) WebServiceStaticArrays.mStoreZouponsDealCardDetails.get(position);
		    			 }
		    			 // If friend Already choosed means navigate to notes page else show options
		    			 if(FriendId.equalsIgnoreCase("")){
		    				 final Dialog mBuyOptionsDialog = new Dialog(mContext);
		    				 mBuyOptionsDialog.setTitle("Buy Options");
		    				 mBuyOptionsDialog.setContentView(R.layout.card_buybutton_menu);
		    				 ListView mMenuList = (ListView) mBuyOptionsDialog.findViewById(R.id.lists);
		    				 mMenuList.setAdapter(new ArrayAdapter<String>(mContext, R.layout.card_buy_button_dialog, R.id.optiontext, mBuyOptions));
		    				 mMenuList.setOnItemClickListener(new OnItemClickListener() {

		    					 @Override
		    					 public void onItemClick(AdapterView<?> arg0,View arg1, int position, long arg3) {
		    						 mBuyOptionsDialog.dismiss();
		    						 if(mBuyOptions[position].equalsIgnoreCase("  Send to Friend")){
		    							 Intent intent_friendlist = new Intent(mContext,FBFriendList.class);
		    							 intent_friendlist.putExtra("classname", "SendGiftCard");	//Dummy classname for friends note description page for both rightmenu giftcard and deals
		    							
		    							 if(serviceFunction.equalsIgnoreCase("Regular")){
		    								 intent_friendlist.putExtra("card_id", mSelectedCardDetails.id); // selected giftcard id
		    								 intent_friendlist.putExtra("cardvalue", mSelectedCardDetails.facevalue);	//Selected GiftCard facevalue
		    							 }else{
		    								 intent_friendlist.putExtra("card_id", mSelectedCardDetails.card_id); // selected giftcard id
		    								 intent_friendlist.putExtra("cardvalue", mSelectedCardDetails.sellprice); // selected zcard sale price value
		    							 }
		    							 intent_friendlist.putExtra("facevalue", mSelectedCardDetails.facevalue);	//Selected GiftCard facevalue
		    							 mContext.startActivity(intent_friendlist);
		    						 }else{
		    							 Intent intent_step2 = new Intent(mContext,Step2_ManageCards.class);
		    							 intent_step2.putExtra("datasourcename", "cards_selfuse"); //flag to set control from cards self use option.
		    							 
		    							 if(serviceFunction.equalsIgnoreCase("Regular")){
		    								 intent_step2.putExtra("card_id", mSelectedCardDetails.id); // selected giftcard id
		    								 intent_step2.putExtra("cardvalue", mSelectedCardDetails.facevalue);	//Selected GiftCard facevalue
		    							 }else{
		    								 intent_step2.putExtra("card_id", mSelectedCardDetails.card_id); // selected giftcard id
		    								 intent_step2.putExtra("cardvalue", mSelectedCardDetails.sellprice); // selected zcard sale price value
		    							 }
		    							 intent_step2.putExtra("facevalue", mSelectedCardDetails.facevalue);	//Selected GiftCard facevalue
		    							 intent_step2.putExtra("giftcard_locationid", RightMenuStoreId_ClassVariables.mLocationId);
		    							 intent_step2.putExtra("giftcard_storeid", RightMenuStoreId_ClassVariables.mStoreID);
		    							 intent_step2.putExtra("giftcard_storename", RightMenuStoreId_ClassVariables.mStoreName);
		    							 mContext.startActivity(intent_step2);
		    							 mContext.finish();
		    						 }
		    					 }

		    				 });
		    				 mBuyOptionsDialog.show();
		    			 }else{
		    				Intent friendnotecall = new Intent(mContext,FBNotesDescription.class);
		    				friendnotecall.putExtra("name",FriendName);
		    				friendnotecall.putExtra("classname","SendGiftCard");	//Dummy classname for friends note description page for both rightmenu giftcard and deals
		    				friendnotecall.putExtra("sendto", FriendId);
		    				friendnotecall.putExtra("eventflag", mIsZouponsFriend);
		    				friendnotecall.putExtra("card_id", mSelectedCardDetails.id); // selected giftcard id
		    				friendnotecall.putExtra("facevalue", mSelectedCardDetails.facevalue);	//Selected GiftCard facevalue
		    				friendnotecall.putExtra("timezone", Friends.friendTimeZone);
							 if(serviceFunction.equalsIgnoreCase("Regular")){
								 friendnotecall.putExtra("card_id", mSelectedCardDetails.id); // selected giftcard id
								 friendnotecall.putExtra("cardvalue", mSelectedCardDetails.facevalue);	//Selected GiftCard facevalue
							 }else{
								 friendnotecall.putExtra("card_id", mSelectedCardDetails.card_id); // selected giftcard id
								 friendnotecall.putExtra("cardvalue", mSelectedCardDetails.sellprice); // selected zcard sale price value
							 }
		    				mContext.startActivity(friendnotecall);
		    			 }
		    		 }
		    	 });
		     }
		    return view;
		 }
	static class ViewHolder {
		TextView mCardFaceValue,mCardPrice,mCardFaceValueOverImage,mCardRemaining,mRemainingText;
		ImageView mCardImage;
		Button mBuyButton;
	}
}