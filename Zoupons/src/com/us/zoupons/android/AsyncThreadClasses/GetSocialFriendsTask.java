package com.us.zoupons.android.AsyncThreadClasses;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.POJOFBfriendListData;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.android.listview.inflater.classes.CustomFBFriendListAdapter;
import com.us.zoupons.friends.SocialNetworkingAdapter;
import com.us.zoupons.friends.SocialNetworkingDetails;

public class GetSocialFriendsTask extends AsyncTask<Void, String, String>{
	
	String TAG = "GetSocialFriendsTask";
	private Activity mContext;
	private ProgressDialog mProgressView;
	private ZouponsWebService zouponswebservice;
	private ZouponsParsingClass parsingclass ;
	private NetworkCheck mConnectivityCheck;
	private String mResponse;
	private ListView mfriendlist,mSocialTypeList;
	private ViewGroup friendlistgroup;
	private View socialfriendsOptionsContainer;
	private TextView mImportFriendMenu;
	private View importmenusplitter;
	String mClassFlag;

	/*
	 * Constructor for Friends
	 * */
	public GetSocialFriendsTask(Activity context, ListView mfriendlist, ViewGroup friendlistgroup, View socialfriendsOptionsContainer, TextView mImportFriendMenu, View importmenusplitter, String classflag) {
		this.mContext = context;
		this.mfriendlist = mfriendlist;
		this.friendlistgroup = friendlistgroup;
		this.socialfriendsOptionsContainer = socialfriendsOptionsContainer;
		this.mImportFriendMenu = mImportFriendMenu;
		this.importmenusplitter = importmenusplitter;
		this.mClassFlag = classflag;
		mProgressView = new ProgressDialog(context);
		mProgressView.setTitle("Loading");
		mProgressView.setMessage("please wait..");
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
	}
	
	/*
	 * Constructor for Social
	 * */
	public GetSocialFriendsTask(Activity context, ListView mfriendlist, ViewGroup friendlistgroup, View socialfriendsOptionsContainer, TextView mImportFriendMenu, View importmenusplitter, String classflag, ListView socialtypelist) {
		this.mContext = context;
		this.mfriendlist = mfriendlist;
		this.mSocialTypeList = socialtypelist;
		this.friendlistgroup = friendlistgroup;
		this.socialfriendsOptionsContainer = socialfriendsOptionsContainer;
		this.mImportFriendMenu = mImportFriendMenu;
		this.importmenusplitter = importmenusplitter;
		this.mClassFlag = classflag;
		mProgressView = new ProgressDialog(context);
		mProgressView.setTitle("Loading");
		mProgressView.setMessage("please wait..");
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressView.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		String mresult ="",mParsingResponse="";
		try{
			if(mConnectivityCheck.ConnectivityCheck(mContext.getApplicationContext())){
				mResponse = zouponswebservice.getSocialFriends();
				if(!mResponse.equalsIgnoreCase("noresponse") && !mResponse.equalsIgnoreCase("failure")){
					mParsingResponse = parsingclass.parseFriendList(mResponse);
					if(!mParsingResponse.equals("failure") && !mParsingResponse.equals("norecords")){
						mresult = "success";
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						mresult = "failure";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						mresult="norecords";
					}
				}else {
					mresult="Response Error.";
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
		if(result.equalsIgnoreCase("success")){
			mImportFriendMenu.setBackgroundResource(R.drawable.header_2);
			WebServiceStaticArrays.mSearchedFriendList = new ArrayList<Object>(WebServiceStaticArrays.mSocialNetworkFriendList);
			mfriendlist.setAdapter(new CustomFBFriendListAdapter(mContext,WebServiceStaticArrays.mSearchedFriendList,mClassFlag));
		}else if(result.equalsIgnoreCase("failure")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equalsIgnoreCase("norecords")||result.equalsIgnoreCase("Response Error.")){
			if(mClassFlag.equalsIgnoreCase("social")){
				friendlistgroup.setVisibility(View.GONE);
				socialfriendsOptionsContainer.setVisibility(View.VISIBLE);
				mImportFriendMenu.setVisibility(View.VISIBLE);
				mImportFriendMenu.setBackgroundResource(R.drawable.footer_dark_blue_new);
				importmenusplitter.setVisibility(View.VISIBLE);

				ArrayList<Object> mSocialDetails = new ArrayList<Object>(2);
				// To display social networking types with count..
				int google_friends_count=0,yahoo_friends_count=0;
				String google_friends_last_updated="",yahoo_friends_last_updated="";
				for(int i=0;i<WebServiceStaticArrays.mSocialNetworkFriendList.size();i++){
					POJOFBfriendListData mFriendData = (POJOFBfriendListData) WebServiceStaticArrays.mSocialNetworkFriendList.get(i);
					if(mFriendData.friend_provider.equalsIgnoreCase("Google")){
						google_friends_count = google_friends_count + 1;
					}else if(mFriendData.friend_provider.equalsIgnoreCase("Yahoo")){
						// count for other social networking friends...
						yahoo_friends_count = yahoo_friends_count + 1;
					}
					google_friends_last_updated = mFriendData.google_friends_updated_time;
				}

				SocialNetworkingDetails mGoogleSocialNetworkDetails = new SocialNetworkingDetails();
				mGoogleSocialNetworkDetails.mFriendsCount = String.valueOf(google_friends_count);
				mGoogleSocialNetworkDetails.mLastUpdatedDate = google_friends_last_updated;
				mSocialDetails.add(0,mGoogleSocialNetworkDetails);

				mSocialTypeList.setAdapter(new SocialNetworkingAdapter(mContext, mSocialDetails));
			}else if(mClassFlag.equalsIgnoreCase("friends")){
				WebServiceStaticArrays.mSearchedFriendList = new ArrayList<Object>(WebServiceStaticArrays.mSocialNetworkFriendList);
				mfriendlist.setAdapter(new CustomFBFriendListAdapter(mContext.getApplicationContext(),WebServiceStaticArrays.mSearchedFriendList,mClassFlag));
			}
		}else if(result.equalsIgnoreCase("no data")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equalsIgnoreCase("noaccount")){
			alertBox_service("Information", "Account doesn't exists");
		}else if(result.equalsIgnoreCase("nonetwork")){
			Toast.makeText(mContext.getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
		}
	}

	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(mContext.getApplicationContext());
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