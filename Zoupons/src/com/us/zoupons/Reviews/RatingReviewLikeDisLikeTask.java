package com.us.zoupons.Reviews;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class RatingReviewLikeDisLikeTask extends AsyncTask<String, String, String>{
	String TAG=PostReviewTask.class.getSimpleName(),mRateType;
	Context ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
 	private ZouponsParsingClass parsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	String  mGetResponse="",mParseResponse="",reviewId="";
 	private TextView mLikeCount,mDislikeCount;
 	private ImageView mLikeImage,mDislikeImage;
 	
 	public RatingReviewLikeDisLikeTask(Context context, TextView LikeCount, TextView DislikeCount, ImageView LikeImage, ImageView DislikeImage) {
		this.ctx = context;			
		this.mLikeCount = LikeCount;
		this.mDislikeCount = DislikeCount;
		this.mLikeImage = LikeImage;
		this.mDislikeImage = DislikeImage;
		mConnectivityNetworkCheck = new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result ="";		
		mRateType = params[2];
		reviewId=params[1];
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){					
					mGetResponse = zouponswebservice.ReviewLikeDisLike(UserDetails.mServiceUserId,params[0],params[1],params[2]);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= parsingclass.mParsePostUpdateReviewResponse(mGetResponse);
						if(mParseResponse.equalsIgnoreCase("success")){	
							mGetResponse = zouponswebservice.mGetReviewStatus(UserDetails.mServiceUserId,RightMenuStoreId_ClassVariables.mStoreID);	
							if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
								mParseResponse= parsingclass.mParseReviewStatusResponse(mGetResponse);
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
							Log.i(TAG,"Error");
							result="Response Error.";
						}else if(mParseResponse.equalsIgnoreCase("norecords")){
							Log.i(TAG,"No Records");
							result="No Records";
						}
					}else{
						result="Response Error.";
					}
			}else{
				result="nonetwork";
			}
		}catch (Exception e) {
			Log.i(TAG,"Thread Error");
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(progressdialog != null && progressdialog.isShowing()){
			progressdialog.dismiss();
		}		
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			Log.i(TAG, "Success");
			if(Reviews.STORE_REVIEW_MESSAGE.equalsIgnoreCase("Success")){
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
				for(int i=0; i<WebServiceStaticArrays.mStoreReviewsList.size(); i++){
					POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(i);
				}

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
				for(int i=0; i<WebServiceStaticArrays.mStoreReviewsList.size(); i++){
					POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(i);
				}
			}else{
				alertBox_service("Information", "Review Not Updated.");
			}
		}
	}
	private void alertBox_service(String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
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
