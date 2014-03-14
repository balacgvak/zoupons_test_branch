package com.us.zoupons.shopper.reviews;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynctask to communicate with server to get review status [liked or disliked] previously
 *
 */

public class GetReviewRatesStatusTask extends AsyncTask<String, String, String>{

	private Context mContext;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	private String  mGetResponse="",mParseResponse="";
	private ImageView mViewReviewStarOne,mViewReviewStarTwo,mViewReviewStarThree,mViewReviewStarFour,mViewReviewStarFive,mLikeImage,mDislikeImage;
	private TextView mViewReviewName,mViewReviewPostedDate,mLikeCount,mDislikeCount,mRightFooterView;
	private EditText mMessage;
	private int position;

	public GetReviewRatesStatusTask(Context context, TextView ViewReviewName, TextView ViewReviewPostedDate, EditText ViewReviewMessage, TextView LikeCount, TextView DislikeCount,ImageView likeImage, ImageView DislikeImage, ImageView ViewReviewStarOne, ImageView ViewReviewStarTwo, ImageView ViewReviewStarThree, ImageView ViewReviewStarFour, ImageView ViewReviewStarFive,TextView RightFooterView, int position) {
		this.mContext = context;
		mViewReviewStarOne = ViewReviewStarOne;
		mViewReviewStarTwo = ViewReviewStarTwo;
		mViewReviewStarThree = ViewReviewStarThree;
		mViewReviewStarFour = ViewReviewStarFour;
		mViewReviewStarFive = ViewReviewStarFive;
		mLikeImage = likeImage;
		mDislikeImage = DislikeImage;
		mViewReviewName = ViewReviewName; 
		mViewReviewPostedDate = ViewReviewPostedDate;
		mLikeCount = LikeCount;
		mDislikeCount = DislikeCount;
		mMessage = ViewReviewMessage;
		this.mRightFooterView = RightFooterView;
		this.position =  position;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		if(WebServiceStaticArrays.GetReviewStatusList.size()==0){
			mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		}
	}

	@Override
	protected String doInBackground(String... params) {
		String result ="";		
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){
				if(WebServiceStaticArrays.GetReviewStatusList.size()==0){
					mGetResponse = mZouponswebservice.mGetReviewStatus(UserDetails.mServiceUserId,params[0]);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= mParsingclass.mParseReviewStatusResponse(mGetResponse);
						if(mParseResponse.equalsIgnoreCase("success")){						
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
					result ="success";
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
		if(mProgressdialog != null && mProgressdialog.isShowing()){
			mProgressdialog.dismiss();
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
			POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(position);
			mViewReviewName.setText(mReviewDetails.user_name);
			mViewReviewPostedDate.setText(mReviewDetails.posted_date);
			mMessage.setText(mReviewDetails.message);
			mLikeCount.setText(mReviewDetails.user_likes);
			mDislikeCount.setText(mReviewDetails.user_dislikes);
			mLikeImage.setEnabled(true);
			mDislikeImage.setEnabled(true);
			mLikeImage.setImageResource(R.drawable.thumbsupgray);
			mDislikeImage.setImageResource(R.drawable.thumbsdowngray);
			switch(Integer.parseInt(mReviewDetails.rating)){
			case 1:
				mViewReviewStarOne.setVisibility(View.VISIBLE);
				mViewReviewStarTwo.setVisibility(View.INVISIBLE);
				mViewReviewStarThree.setVisibility(View.INVISIBLE);
				mViewReviewStarFour.setVisibility(View.INVISIBLE);
				mViewReviewStarFive.setVisibility(View.INVISIBLE);
				break;
			case 2:
				mViewReviewStarOne.setVisibility(View.VISIBLE);
				mViewReviewStarTwo.setVisibility(View.VISIBLE);
				mViewReviewStarThree.setVisibility(View.INVISIBLE);
				mViewReviewStarFour.setVisibility(View.INVISIBLE);
				mViewReviewStarFive.setVisibility(View.INVISIBLE);
				break;
			case 3:
				mViewReviewStarOne.setVisibility(View.VISIBLE);
				mViewReviewStarTwo.setVisibility(View.VISIBLE);
				mViewReviewStarThree.setVisibility(View.VISIBLE);
				mViewReviewStarFour.setVisibility(View.INVISIBLE);
				mViewReviewStarFive.setVisibility(View.INVISIBLE);
				break;
			case 4:
				mViewReviewStarOne.setVisibility(View.VISIBLE);
				mViewReviewStarTwo.setVisibility(View.VISIBLE);
				mViewReviewStarThree.setVisibility(View.VISIBLE);
				mViewReviewStarFour.setVisibility(View.VISIBLE);
				mViewReviewStarFive.setVisibility(View.INVISIBLE);
				break;
			case 5:
				mViewReviewStarOne.setVisibility(View.VISIBLE);
				mViewReviewStarTwo.setVisibility(View.VISIBLE);
				mViewReviewStarThree.setVisibility(View.VISIBLE);
				mViewReviewStarFour.setVisibility(View.VISIBLE);
				mViewReviewStarFive.setVisibility(View.VISIBLE);
				break;
			}
			for(int i=0;i<WebServiceStaticArrays.GetReviewStatusList.size();i++){
				POJOReviewStatus obj = (POJOReviewStatus)WebServiceStaticArrays.GetReviewStatusList.get(i);
				if(!obj.Message.equals("")){
					break;
				}else{

					if(mReviewDetails.review_id.equalsIgnoreCase(obj.ReviewId)){
						if(obj.Rate.equalsIgnoreCase("up")){ // Since Previous status is like ,Enable Dislike
							mLikeImage.setEnabled(false);
							mLikeImage.setImageResource(R.drawable.thumbsupgreen);
							mDislikeImage.setEnabled(true);
							mDislikeImage.setImageResource(R.drawable.thumbsdowngray);
						}else if(obj.Rate.equalsIgnoreCase("down")){ // Since Previous status is Dislike ,Enable like
							mDislikeImage.setEnabled(false);
							mDislikeImage.setImageResource(R.drawable.thumbsdownred);
							mLikeImage.setEnabled(true);
							mLikeImage.setImageResource(R.drawable.thumbsupgray);
						}
					}
				}

			}
			SharedPreferences mUserDetailsPrefs = mContext.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
			String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");
			if(user_login_type.equalsIgnoreCase("customer") && mReviewDetails.user_id.equalsIgnoreCase(UserDetails.mServiceUserId)){ // Same User
				mLikeImage.setEnabled(false);
				mDislikeImage.setEnabled(false);
				mLikeImage.setImageResource(R.drawable.thumbsupgray);
				mDislikeImage.setImageResource(R.drawable.thumbsdowngray);
				mRightFooterView.setVisibility(View.GONE);
			}else if(user_login_type.equalsIgnoreCase("Guest")){ // Disable both like and dislike buttons
				mRightFooterView.setVisibility(View.GONE);
				mLikeImage.setEnabled(false);
				mLikeImage.setImageResource(R.drawable.thumbsupgray);
				mDislikeImage.setEnabled(false);
				mDislikeImage.setImageResource(R.drawable.thumbsdowngray);
			}else{
				mRightFooterView.setVisibility(View.VISIBLE);
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
