package com.us.zoupons.Reviews;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class GetReviewRatesStatusTask extends AsyncTask<String, String, String>{

	String TAG=PostReviewTask.class.getSimpleName();
	Context ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	String  mGetResponse="",mParseResponse="";
	private ImageView mViewReviewStarOne,mViewReviewStarTwo,mViewReviewStarThree,mViewReviewStarFour,mViewReviewStarFive,mLikeImage,mDislikeImage;
	private TextView mViewReviewName,mViewReviewPostedDate,mLikeCount,mDislikeCount,mRightFooterView;
	private EditText mMessage;
	private int position;

	public GetReviewRatesStatusTask(Context context, TextView ViewReviewName, TextView ViewReviewPostedDate, EditText ViewReviewMessage, TextView LikeCount, TextView DislikeCount,ImageView likeImage, ImageView DislikeImage, ImageView ViewReviewStarOne, ImageView ViewReviewStarTwo, ImageView ViewReviewStarThree, ImageView ViewReviewStarFour, ImageView ViewReviewStarFive,TextView RightFooterView, int position) {

		this.ctx = context;
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
		if(WebServiceStaticArrays.GetReviewStatusList.size()==0){
			progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		String result ="";		
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){
				if(WebServiceStaticArrays.GetReviewStatusList.size()==0){
					mGetResponse = zouponswebservice.mGetReviewStatus(UserDetails.mServiceUserId,params[0]);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= parsingclass.mParseReviewStatusResponse(mGetResponse);
						if(mParseResponse.equalsIgnoreCase("success")){						
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
					result ="success";
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
						if(obj.Rate.equalsIgnoreCase("up")){
							mLikeImage.setEnabled(false);
							mLikeImage.setImageResource(R.drawable.thumbsupgreen);
							mDislikeImage.setEnabled(true);
							mDislikeImage.setImageResource(R.drawable.thumbsdowngray);
						}else if(obj.Rate.equalsIgnoreCase("down")){
							mDislikeImage.setEnabled(false);
							mDislikeImage.setImageResource(R.drawable.thumbsdownred);
							mLikeImage.setEnabled(true);
							mLikeImage.setImageResource(R.drawable.thumbsupgray);
						}
					}
				}

			}
			if(mReviewDetails.user_id.equalsIgnoreCase(UserDetails.mServiceUserId)){
				Log.i(TAG, "same user");
				mLikeImage.setEnabled(false);
				mDislikeImage.setEnabled(false);
				mLikeImage.setImageResource(R.drawable.thumbsupgray);
				mDislikeImage.setImageResource(R.drawable.thumbsdowngray);
				mRightFooterView.setVisibility(View.GONE);
			}else{
				mRightFooterView.setVisibility(View.VISIBLE);
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
