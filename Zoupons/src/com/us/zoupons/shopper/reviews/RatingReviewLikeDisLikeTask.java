package com.us.zoupons.shopper.reviews;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynctask to communicate with server to like or dislike a review
 *
 */

public class RatingReviewLikeDisLikeTask extends AsyncTask<String, String, String>{
	private String mRateType;
	private Context mContext;
	private ProgressDialog mProgressDialog=null;
	private ZouponsWebService mZouponsWebservice = null;
 	private ZouponsParsingClass mParsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	private String  mGetResponse="",mParseResponse="",reviewId="";
 	private TextView mLikeCount,mDislikeCount;
 	private ImageView mLikeImage,mDislikeImage;
 	
 	public RatingReviewLikeDisLikeTask(Context context, TextView LikeCount, TextView DislikeCount, ImageView LikeImage, ImageView DislikeImage) {
		this.mContext = context;			
		this.mLikeCount = LikeCount;
		this.mDislikeCount = DislikeCount;
		this.mLikeImage = LikeImage;
		this.mDislikeImage = DislikeImage;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponsWebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressDialog=new ProgressDialog(context);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressDialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result ="";		
		mRateType = params[2];
		reviewId=params[1];
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){					
					mGetResponse = mZouponsWebservice.ReviewLikeDisLike(UserDetails.mServiceUserId,params[0],params[1],params[2]);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= mParsingclass.mParsePostUpdateReviewResponse(mGetResponse);
						if(mParseResponse.equalsIgnoreCase("success")){	
							mGetResponse = mZouponsWebservice.mGetReviewStatus(UserDetails.mServiceUserId,RightMenuStoreId_ClassVariables.mStoreID);	
							if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
								mParseResponse= mParsingclass.mParseReviewStatusResponse(mGetResponse);
								if(mParseResponse.equalsIgnoreCase("success")){						
									result ="success";				
								}else{
									WebServiceStaticArrays.GetReviewStatusList.clear();
								}
							}else{
								WebServiceStaticArrays.GetReviewStatusList.clear();
							}
							result ="success";
						}else if(mParseResponse.equalsIgnoreCase("failure")){
							result="Response Error.";
						}else if(mParseResponse.equalsIgnoreCase("norecords")){
							result="No Records";
						}
					}else{
						result="Response Error.";
					}
			}else{
				result="nonetwork";
			}
		}catch (Exception e) {
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(mProgressDialog != null && mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
		}		
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			if(StoreReviews.STORE_REVIEW_MESSAGE.equalsIgnoreCase("Success")){
				if(mRateType.equalsIgnoreCase("up")){ 
					if(mLikeImage.isEnabled() && mDislikeImage.isEnabled()){ // If user likes for the first time
						int likecount = Integer.parseInt(mLikeCount.getText().toString());
						likecount += 1;
						mLikeCount.setText(String.valueOf(likecount));
						mLikeImage.setEnabled(false);
						mLikeImage.setImageResource(R.drawable.thumbsupgreen);
					}else{ // If user likes other than first time
						int likecount = Integer.parseInt(mLikeCount.getText().toString());
						int dislikecount = Integer.parseInt(mDislikeCount.getText().toString());
						likecount += 1; 
						mLikeCount.setText(String.valueOf(likecount));
						if(dislikecount>0){
							dislikecount -=1;
							mDislikeCount.setText(String.valueOf(dislikecount));	
						}
						mLikeImage.setEnabled(false);
						mLikeImage.setImageResource(R.drawable.thumbsupgreen);
						mDislikeImage.setEnabled(true);
						mDislikeImage.setImageResource(R.drawable.thumbsdowngray);	
					}
				}else{
					if(mLikeImage.isEnabled() && mDislikeImage.isEnabled()){ // If user likes for the first time
						int dislikecount = Integer.parseInt(mDislikeCount.getText().toString());
						dislikecount += 1; 
						mDislikeCount.setText(String.valueOf(dislikecount));
						mDislikeImage.setEnabled(false);
						mDislikeImage.setImageResource(R.drawable.thumbsdownred);
					}else{
						int likecount = Integer.parseInt(mLikeCount.getText().toString());
						int dislikecount = Integer.parseInt(mDislikeCount.getText().toString());
						dislikecount += 1; 
						mDislikeCount.setText(String.valueOf(dislikecount));
						if(likecount>0){
							likecount -=1;
							mLikeCount.setText(String.valueOf(likecount));	
						}
						mLikeImage.setEnabled(true);
						mLikeImage.setImageResource(R.drawable.thumbsupgray);
						mDislikeImage.setEnabled(false);
						mDislikeImage.setImageResource(R.drawable.thumbsdownred);	
					}
				}
				// For internal purpose chaging like count and status
				for(int i=0; i<WebServiceStaticArrays.mStoreReviewsList.size(); i++){
					POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(i);
					if(mReviewDetails.review_id.equalsIgnoreCase(reviewId)){
						mReviewDetails.company_id = mReviewDetails.company_id;
						mReviewDetails.review_id = mReviewDetails.review_id;
						mReviewDetails.user_id = mReviewDetails.user_id;
						mReviewDetails.message = mReviewDetails.message;
						mReviewDetails.rating = mReviewDetails.rating;
						mReviewDetails.status = mReviewDetails.status;
						mReviewDetails.visitor_id = mReviewDetails.visitor_id;
						mReviewDetails.posted_date = mReviewDetails.posted_date;
						mReviewDetails.user_name = mReviewDetails.user_name;
						mReviewDetails.user_likes = mLikeCount.getText().toString();
						mReviewDetails.user_dislikes = mDislikeCount.getText().toString();
						mReviewDetails.totalcount = mReviewDetails.totalcount;
						WebServiceStaticArrays.mStoreReviewsList.set(i, mReviewDetails);
						break;
					}
				}
			}else{
				alertBox_service("Information", "Review Not Updated.");
			}
		}
	}
	
	/* To show alert pop up with respective message */
	private void alertBox_service(String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		service_alert.show();
	}
}
