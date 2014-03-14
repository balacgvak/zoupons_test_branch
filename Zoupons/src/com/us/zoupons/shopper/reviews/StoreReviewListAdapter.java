package com.us.zoupons.shopper.reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.collections.WebServiceStaticArrays;

/**
 * 
 * Custom adapter to populate store review with details
 *
 */

public class StoreReviewListAdapter extends BaseAdapter {
    
    private Context mContext;
    
    public StoreReviewListAdapter(Context context) {
       this.mContext = context;
    }

    public int getCount() {
        return WebServiceStaticArrays.mStoreReviewsList.size();
    }

    public Object getItem(int position) {
        return WebServiceStaticArrays.mStoreReviewsList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	View view = convertView;
	     if(view == null) {
	         LayoutInflater inflater = LayoutInflater.from(mContext);
	         view = inflater.inflate(R.layout.reviews_listrow, null);
             ViewHolder holder = new ViewHolder();
	         holder.mReviewFirstRateImage = (ImageView) view.findViewById(R.id.reviewFirstRatingImageId);
	         holder.mReviewSecondRateImage = (ImageView) view.findViewById(R.id.reviewSecondRatingImageId);
	         holder.mReviewThirdRateImage = (ImageView) view.findViewById(R.id.reviewThirdRatingImageId);
	         holder.mReviewFourthRateImage = (ImageView) view.findViewById(R.id.reviewFourthRatingImageId);
	         holder.mReviewFifthRateImage = (ImageView) view.findViewById(R.id.reviewFifthRatingImageId);
	         holder.mReviewUserName = (TextView) view.findViewById(R.id.review_user_name);
	         holder.mReviewDate = (TextView) view.findViewById(R.id.review_date);
	         holder.mReviewMessage = (TextView) view.findViewById(R.id.review_commentId);
	         view.setTag(holder);
	     }
	     POJOStoreReviewDetails reviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(position);
	     ViewHolder holder = (ViewHolder)view.getTag();
	     holder.mReviewUserName.setText(reviewDetails.user_name);
	     holder.mReviewDate.setText(reviewDetails.posted_date);
         holder.mReviewMessage.setText(reviewDetails.message);
         //Set review star based upon rating
         switch(Integer.parseInt(reviewDetails.rating)){
         case 0:
        	 holder.mReviewFirstRateImage.setVisibility(View.INVISIBLE);
        	 holder.mReviewSecondRateImage.setVisibility(View.INVISIBLE);
        	 holder.mReviewThirdRateImage.setVisibility(View.INVISIBLE);
        	 holder.mReviewFourthRateImage.setVisibility(View.INVISIBLE);
        	 holder.mReviewFifthRateImage.setVisibility(View.INVISIBLE);
        	 break;
         case 1:
        	 holder.mReviewFirstRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewSecondRateImage.setVisibility(View.INVISIBLE);
        	 holder.mReviewThirdRateImage.setVisibility(View.INVISIBLE);
        	 holder.mReviewFourthRateImage.setVisibility(View.INVISIBLE);
        	 holder.mReviewFifthRateImage.setVisibility(View.INVISIBLE);
        	 break;
         case 2:
        	 holder.mReviewFirstRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewSecondRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewThirdRateImage.setVisibility(View.INVISIBLE);
        	 holder.mReviewFourthRateImage.setVisibility(View.INVISIBLE);
        	 holder.mReviewFifthRateImage.setVisibility(View.INVISIBLE);
        	 break;
         case 3:
        	 holder.mReviewFirstRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewSecondRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewThirdRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewFourthRateImage.setVisibility(View.INVISIBLE);
        	 holder.mReviewFifthRateImage.setVisibility(View.INVISIBLE);
        	 break;
         case 4:
        	 holder.mReviewFirstRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewSecondRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewThirdRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewFourthRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewFifthRateImage.setVisibility(View.INVISIBLE);
        	 break;
         case 5:
        	 holder.mReviewFirstRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewSecondRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewThirdRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewFourthRateImage.setVisibility(View.VISIBLE);
        	 holder.mReviewFifthRateImage.setVisibility(View.VISIBLE);
             break;
          }
         return view;
    }
    
    static class ViewHolder {
		ImageView mReviewFirstRateImage,mReviewSecondRateImage,mReviewThirdRateImage,mReviewFourthRateImage,mReviewFifthRateImage;
		TextView mReviewUserName,mReviewDate,mReviewMessage;
    }
}