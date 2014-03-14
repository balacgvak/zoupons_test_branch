package com.us.zoupons.shopper.reviews;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynctask to communicate with server to get store location's reviews
 *
 */

public class PostReviewTask extends AsyncTask<String, String, String>{
	private Context mContext;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponswebservice = null;
 	private ZouponsParsingClass mParsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	private String  mGetResponse="",mParseResponse="",FLAGACT="";
 	private ViewGroup mReviewListContainer;
 	private ScrollView mEditReviewDetails;
 	private ListView mReviewDetailsList;
	private TextView mReviewStoreTitle,mReviewRatingOne,mReviewRatingTwo,mReviewRatingThree,mReviewRatingFour,mReviewRatingFive,mFooterLeftText;
 		
	public PostReviewTask(Context context, ViewGroup reviewslistcontainer, ScrollView EditReviewDetails, ListView mReviewList, TextView mReviewStoreTitle, TextView mReviewRatingOne, TextView mReviewRatingTwo, TextView mReviewRatingThree, TextView mReviewRatingFour, TextView mReviewRatingFive, TextView mFooterLeftText) {
		// TODO Auto-generated constructor stub
		this.mContext = context;			
		this.mReviewListContainer = reviewslistcontainer;
		this.mEditReviewDetails = EditReviewDetails;
		this.mReviewDetailsList= mReviewList;
		this.mReviewStoreTitle = mReviewStoreTitle;
		this.mReviewRatingOne = mReviewRatingOne;
		this.mReviewRatingTwo = mReviewRatingTwo;
		this.mReviewRatingThree = mReviewRatingThree;
		this.mReviewRatingFour = mReviewRatingFour;
		this.mReviewRatingFive = mReviewRatingFive;
		this.mFooterLeftText = mFooterLeftText;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		StoreReviews.STORE_REVIEW_MESSAGE="";
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result ="";
		FLAGACT = params[0];
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){	
				if(FLAGACT.equalsIgnoreCase("POST")){ // Post review
					mGetResponse = mZouponswebservice.mPostStoreReviews(UserDetails.mServiceUserId,params[1],params[2],params[3]);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= mParsingclass.mParsePostUpdateReviewResponse(mGetResponse);
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
				}else if(FLAGACT.equalsIgnoreCase("UPDATE")){ // Update review details
					mGetResponse = mZouponswebservice.mUpdateStoreReviews(params[1],params[2],params[3]);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= mParsingclass.mParsePostUpdateReviewResponse(mGetResponse);
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
				}else if(FLAGACT.equalsIgnoreCase("INAPPROPRIATE")){ // Mark as inappropiate
					if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreReviews")){
						mGetResponse = mZouponswebservice.mReviewInApproriate(UserDetails.mServiceUserId,params[1]);	
					}else{
						SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
						String mUserId = mPrefs.getString("user_id", "");
						mGetResponse = mZouponswebservice.mReviewInApproriate(mUserId,params[1]);
					}
						
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= mParsingclass.mParseInAppropriateReviewResponse(mGetResponse);
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
			if(FLAGACT.equalsIgnoreCase("POST")){
				if(StoreReviews.STORE_REVIEW_MESSAGE.equalsIgnoreCase("Success")){
					alertBox_service("Information", "Review posted");
				 }else{
					 alertBox_service("Information", "Unable to post review please try after sometime");
				}
			}else if(FLAGACT.equalsIgnoreCase("UPDATE")){
				if(StoreReviews.STORE_REVIEW_MESSAGE.equalsIgnoreCase("Success")){
					alertBox_service("Information", "Review updated");
				}else{
					alertBox_service("Information", "Unable to update review please try after sometime");
				}
			}else if(FLAGACT.equalsIgnoreCase("INAPPROPRIATE")){
				if(StoreReviews.STORE_REVIEW_MESSAGE.equalsIgnoreCase("Flagged Inappropriate")){
					alertBox_service("Information", "Review reported");
				}else{
					alertBox_service("Information", "Unable to post message please try after sometime");
				}
			}

		}
	}
	
	/* To show alert pop up with respective message */
	private void alertBox_service(String title, final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			    if(msg.equalsIgnoreCase("Review posted") || msg.equalsIgnoreCase("Review updated")){
			    	mReviewListContainer.setVisibility(View.VISIBLE);
			    	mEditReviewDetails.setVisibility(View.GONE);
			    	mFooterLeftText.setBackgroundResource(R.drawable.header_2);
			    	MainMenuActivity.mReviewStart ="0";
			    	MainMenuActivity.mReviewEndLimit="20";
			    	// To refresh review list
			    	GetStoreReviewDetails reviewDetails = new GetStoreReviewDetails(mContext, mReviewDetailsList,mReviewStoreTitle,mReviewRatingOne,mReviewRatingTwo,mReviewRatingThree,mReviewRatingFour,mReviewRatingFive,mFooterLeftText,"PROGRESS","MainMenuActivity_Reviews");
					reviewDetails.execute("");
			    }
				
				dialog.dismiss();
			}
		});
		service_alert.show();
	}

}
