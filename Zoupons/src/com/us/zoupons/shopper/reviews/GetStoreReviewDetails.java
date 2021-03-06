package com.us.zoupons.shopper.reviews;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.shopper.store_info.StoreInformation;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.reviews.StoreOwnerReviews;

/**
 * 
 * Asynctask to communicate with server to get store review details
 *
 */

public class GetStoreReviewDetails extends AsyncTask<String, String, String>{
	
	private Context mContext;
	private ProgressDialog mProgressView;
	private ZouponsWebService zouponswebservice;
	private ZouponsParsingClass parsingclass ;
	private NetworkCheck mConnectivityCheck;
	private String mResponse,mClassName;
	private ListView mReviewDetailsList;
	private TextView mReviewRatingOne,mReviewRatingTwo,mReviewRatingThree,mReviewRatingFour,mReviewRatingFive,mFooterLeftText=null;
	private int rateOne,rateTwo,ratethree,rateFour,rateFive;
	private String mProgressStatus="";
	private String mCheckRefresh="";

	/* Constructor called from customer reviews page */
	public GetStoreReviewDetails(Context context, ListView mReviewlist, TextView ReviewStoreTitle, 
			TextView ReviewRatingOne, TextView ReviewRatingTwo, TextView ReviewRatingThree, TextView ReviewRatingFour, 
			TextView ReviewRatingFive, TextView FooterLeftText,String progressStatus, String classname) {
		this.mContext = context;
		this.mReviewDetailsList= mReviewlist;
		this.mProgressStatus = progressStatus;
		mReviewRatingOne = ReviewRatingOne;
		mReviewRatingTwo = ReviewRatingTwo;
		mReviewRatingThree = ReviewRatingThree;
		mReviewRatingFour = ReviewRatingFour;
		mReviewRatingFive = ReviewRatingFive;
		mFooterLeftText = FooterLeftText;
		this.mClassName = classname;
		mProgressView = new ProgressDialog(context);
		mProgressView.setTitle("Loading");
		mProgressView.setMessage("please wait..");
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
		MainMenuActivity.UserReviewPosition = -1;
		MainMenuActivity.mIsLoadMore = true;    
	}

	/* Constructor called from customer and store owner reviews page */
	public GetStoreReviewDetails(Context context, ListView mReviewlist, TextView ReviewStoreTitle, 
			TextView ReviewRatingOne, TextView ReviewRatingTwo, TextView ReviewRatingThree, TextView ReviewRatingFour, 
			TextView ReviewRatingFive, String progressStatus, String classname) {
		this.mContext = context;
		this.mReviewDetailsList= mReviewlist;
		this.mProgressStatus = progressStatus;
		mReviewRatingOne = ReviewRatingOne;
		mReviewRatingTwo = ReviewRatingTwo;
		mReviewRatingThree = ReviewRatingThree;
		mReviewRatingFour = ReviewRatingFour;
		mReviewRatingFive = ReviewRatingFive;
		this.mClassName = classname;
		mProgressView = new ProgressDialog(context);
		mProgressView.setTitle("Loading");
		mProgressView.setMessage("please wait..");
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
		StoreOwnerReviews.UserReviewPosition = -1;
		StoreOwnerReviews.mIsLoadMore = true;    
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(mProgressStatus.equalsIgnoreCase("NOPROGRESS")){
		}else{
			mProgressView.show();
			if(mFooterLeftText!=null){
				mFooterLeftText.setText("");
			}else{
			}
			mReviewDetailsList.setAdapter(null);
		}
		mReviewRatingOne.setText("");
		mReviewRatingTwo.setText("");
		mReviewRatingThree.setText("");
		mReviewRatingFour.setText("");
		mReviewRatingFive.setText("");
	}

	@Override
	protected String doInBackground(String... params) {
		String mresult ="",mParsingResponse="";
		mCheckRefresh = params[0];
		try{
			if(mConnectivityCheck.ConnectivityCheck(mContext)){
				if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreReviews")){
					if(MainMenuActivity.mReviewStart.equalsIgnoreCase("0")){
						WebServiceStaticArrays.mStoreReviewsList.clear();	
					}
					mResponse=zouponswebservice.getReviewDetails(MainMenuActivity.mReviewStart,UserDetails.mServiceUserId,RightMenuStoreId_ClassVariables.mStoreID,RightMenuStoreId_ClassVariables.mLocationId);	
				}else if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwnerReviews")){
					if(StoreOwnerReviews.mStoreOwnerReviewsStart.equalsIgnoreCase("0")){
						WebServiceStaticArrays.mStoreReviewsList.clear();
					}
					SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
					String mStoreLocation_id = mPrefs.getString("location_id", "");
					String mStoreId = mPrefs.getString("store_id", "");
					String mUserId = mPrefs.getString("user_id", "");
					mResponse=zouponswebservice.getReviewDetails(StoreOwnerReviews.mStoreOwnerReviewsStart,mUserId,mStoreId,mStoreLocation_id);
				}
				if(!mResponse.equals("")){
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){
						mParsingResponse=parsingclass.parseStoreReviewDetails(mResponse);
						if(mParsingResponse.equalsIgnoreCase("Success")){
							mresult="Success";
						}else if(mParsingResponse.equalsIgnoreCase("Failure")){
							mresult="failure";
						}else if(mParsingResponse.equalsIgnoreCase("norecords")){
							mresult="norecords";
						}
					}else{
						mresult="Response Error.";
					}
				}else {
					mresult="no data";
				}
			}else{
				mresult="nonetwork";
			}
		}catch(Exception e){
			mresult ="failure";
		}
		return mresult;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mProgressView.dismiss();
		MainMenuActivity.mIsLoadMore = false;
		StoreOwnerReviews.mIsLoadMore = false;
		if(result.equalsIgnoreCase("success")){
			// Calculating total rating
			for(int i=0;i<WebServiceStaticArrays.mStoreReviewsList.size();i++){
				POJOStoreReviewDetails reviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(i);
				switch(Integer.parseInt(reviewDetails.rating)){
				case 1:
					rateOne+=1;
					break;
				case 2:
					rateTwo+=1;
					break;
				case 3:
					ratethree+=1;
					break;
				case 4:
					rateFour+=1;
					break;
				case 5:
					rateFive+=1;
					break;
				}
				if(reviewDetails.user_id.equalsIgnoreCase(UserDetails.mServiceUserId)){
					StoreInformation.UserReviewPosition = i;
				}
			}
			mReviewRatingOne.setText(String.valueOf(rateOne));
			mReviewRatingTwo.setText(String.valueOf(rateTwo));
			mReviewRatingThree.setText(String.valueOf(ratethree));
			mReviewRatingFour.setText(String.valueOf(rateFour));
			mReviewRatingFive.setText(String.valueOf(rateFive));
			
			if(this.mClassName.equalsIgnoreCase("MainMenuActivity_Reviews")){ // Shopper Review
				if(StoreInformation.UserReviewPosition != -1){
					mFooterLeftText.setText("Edit Review");
					mFooterLeftText.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.editreview,0,0);
				}else{
					mFooterLeftText.setText("Post Review");
					mFooterLeftText.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.postreview,0,0);
				}
				((MainMenuActivity) mContext).SetReviewListAdatpter(mCheckRefresh);
				MainMenuActivity.mReviewStart = MainMenuActivity.mReviewEndLimit;
				MainMenuActivity.mReviewEndLimit = String.valueOf(Integer.parseInt(MainMenuActivity.mReviewEndLimit)+20);
				
				SharedPreferences mUserDetailsPrefs = mContext.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
				String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");
				if(user_login_type.equalsIgnoreCase("customer")){
					mFooterLeftText.setVisibility(View.VISIBLE);
				}else{
					mFooterLeftText.setVisibility(View.INVISIBLE);
				}
			}else if(this.mClassName.equalsIgnoreCase("StoreOwnerReviews")){ // Store module Review
				((StoreOwnerReviews) mContext).SetStoreOwnerReviewListAdatpter(mCheckRefresh);
				StoreOwnerReviews.mStoreOwnerReviewsStart = StoreOwnerReviews.mStoreOwnerReviewsEndLimit;
				StoreOwnerReviews.mStoreOwnerReviewsEndLimit = String.valueOf(Integer.parseInt(StoreOwnerReviews.mStoreOwnerReviewsEndLimit)+20);
			}
		}else if(result.equalsIgnoreCase("failure")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equalsIgnoreCase("norecords")){
			// To set rating value to 0 if no review posted
			mReviewRatingOne.setText(String.valueOf(0));
			mReviewRatingTwo.setText(String.valueOf(0));
			mReviewRatingThree.setText(String.valueOf(0));
			mReviewRatingFour.setText(String.valueOf(0));
			mReviewRatingFive.setText(String.valueOf(0));
			mReviewDetailsList.setBackgroundResource(0);
			if(this.mClassName.equalsIgnoreCase("MainMenuActivity_Reviews")){
				mFooterLeftText.setText("Post Review");
				mFooterLeftText.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.postreview,0,0);
				alertBox_service("Information", "No reviews available.  Be the first to post a one");
			}else{
				alertBox_service("Information", "No reviews available");
			}
		}else if(result.equalsIgnoreCase("Response Error.")){
			alertBox_service("Information", "No reviews available");
		}else if(result.equalsIgnoreCase("no data")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equalsIgnoreCase("nonetwork")){
			Toast.makeText(this.mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}
	}

	/* To show alert pop up with respective message */
	public void alertBox_service(final String title,final String msg){
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
