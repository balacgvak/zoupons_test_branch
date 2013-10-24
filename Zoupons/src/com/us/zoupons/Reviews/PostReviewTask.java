package com.us.zoupons.Reviews;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class PostReviewTask extends AsyncTask<String, String, String>{

	String TAG=PostReviewTask.class.getSimpleName();
	Context ctx;
	private ProgressDialog progressdialog=null;
	private ZouponsWebService zouponswebservice = null;
 	private ZouponsParsingClass parsingclass = null;
 	private NetworkCheck mConnectivityNetworkCheck;
 	String  mGetResponse="",mParseResponse="",FLAGACT="";
 	private ViewGroup mReviewListContainer;
 	private ScrollView mEditReviewDetails;
 	private ListView mReviewDetailsList;
	private TextView mReviewStoreTitle,mReviewRatingOne,mReviewRatingTwo,mReviewRatingThree,mReviewRatingFour,mReviewRatingFive,mFooterLeftText;
 		
	public PostReviewTask(Context context, ViewGroup reviewslistcontainer, ScrollView EditReviewDetails, ListView mReviewList, TextView mReviewStoreTitle, TextView mReviewRatingOne, TextView mReviewRatingTwo, TextView mReviewRatingThree, TextView mReviewRatingFour, TextView mReviewRatingFive, TextView mFooterLeftText) {
		// TODO Auto-generated constructor stub
		this.ctx = context;			
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
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		Reviews.STORE_REVIEW_MESSAGE="";
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
		FLAGACT = params[0];
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(ctx)){	
				if(FLAGACT.equalsIgnoreCase("POST")){
					mGetResponse = zouponswebservice.mPostStoreReviews(UserDetails.mServiceUserId,params[1],params[2],params[3]);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= parsingclass.mParsePostUpdateReviewResponse(mGetResponse);
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
				}else if(FLAGACT.equalsIgnoreCase("UPDATE")){
					mGetResponse = zouponswebservice.mUpdateStoreReviews(params[1],params[2],params[3]);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= parsingclass.mParsePostUpdateReviewResponse(mGetResponse);
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
				}else if(FLAGACT.equalsIgnoreCase("INAPPROPRIATE")){
					if(ctx.getClass().getSimpleName().equalsIgnoreCase("Reviews")){
						mGetResponse = zouponswebservice.mReviewInApproriate(UserDetails.mServiceUserId,params[1]);	
					}else{
						SharedPreferences mPrefs = ctx.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
						String mStoreLocation_id = mPrefs.getString("location_id", "");
						String mUserId = mPrefs.getString("user_id", "");
						mGetResponse = zouponswebservice.mReviewInApproriate(mUserId,params[1]);
					}
						
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParseResponse= parsingclass.mParseInAppropriateReviewResponse(mGetResponse);
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
			if(FLAGACT.equalsIgnoreCase("POST")){
				if(Reviews.STORE_REVIEW_MESSAGE.equalsIgnoreCase("Success")){
					alertBox_service("Information", "Review Posted Successfully.");
				 }else{
					 alertBox_service("Information", "Unable to post review please try after sometime");
				}
			}else if(FLAGACT.equalsIgnoreCase("UPDATE")){
				if(Reviews.STORE_REVIEW_MESSAGE.equalsIgnoreCase("Success")){
					alertBox_service("Information", "Your review has been updated successfully.");
				}else{
					alertBox_service("Information", "Unable to update review please try after sometime");
				}
			}else if(FLAGACT.equalsIgnoreCase("INAPPROPRIATE")){
				if(Reviews.STORE_REVIEW_MESSAGE.equalsIgnoreCase("Flagged Inappropriate")){
					alertBox_service("Information", "Review successfully reported to Zoupons");
				}else{
					alertBox_service("Information", "Unable to post message please try after sometime");
				}
			}

		}
	}
	
	private void alertBox_service(String title, final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			    if(msg.equalsIgnoreCase("Review Posted Successfully.") || msg.equalsIgnoreCase("Your review has been updated successfully.")){
			    	mReviewListContainer.setVisibility(View.VISIBLE);
			    	mEditReviewDetails.setVisibility(View.GONE);
			    	MainMenuActivity.mReviewStart ="0";
			    	MainMenuActivity.mReviewEndLimit="20";
			    	GetStoreReviewDetails reviewDetails = new GetStoreReviewDetails(ctx, mReviewDetailsList,mReviewStoreTitle,mReviewRatingOne,mReviewRatingTwo,mReviewRatingThree,mReviewRatingFour,mReviewRatingFive,mFooterLeftText,"PROGRESS","MainMenuActivity_Reviews");
					reviewDetails.execute("");
			    }
				
				dialog.dismiss();
			}
		});
		service_alert.show();
	}

}
