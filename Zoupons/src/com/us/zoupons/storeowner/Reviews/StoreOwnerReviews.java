package com.us.zoupons.storeowner.Reviews;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.Reviews.GetStoreReviewDetails;
import com.us.zoupons.Reviews.POJOStoreReviewDetails;
import com.us.zoupons.Reviews.PostReviewTask;
import com.us.zoupons.Reviews.StoreReviewListAdapter;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;
import com.us.zoupons.storeowner.communication.StoreOwner_ContactStore;

public class StoreOwnerReviews extends Activity implements OnClickListener{

	public static String TAG="StoreOwnerReviews";

	public static MyHorizontalScrollView scrollView;
	View app;

	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;

	Header header;
	StoreOwner_RightMenu storeowner_rightmenu;
	StoreOwner_LeftMenu storeowner_leftmenu;
	View mRightMenu;
	View mLeftMenu;
	int mMenuFlag;

	private ViewGroup mMiddleViewReviewListContainer,mMiddleViewReviewDetailsContainer,mTitleBar,mFooterView;
	private ScrollView mMiddleViewReviewRespondToFriendContainer;
	ImageView mRightMenuHolder,mStoreOwnerViewReviewRatingOne,mStoreOwnerViewReviewRatingTwo,mStoreOwnerViewReviewRatingThree,mStoreOwnerViewReviewRatingFour,mStoreOwnerViewReviewRatingFive,mStoreOwnerViewReviewThumbsUp,mStoreOwnerViewReviewThumbsDown;
	Button mStoreOwnerReviewsFreezeView,mStoreOwnerViewReviewPreviousButton,mStoreOwnerViewReviewNextButton,mStoreOwnerRate1Button,mStoreOwnerRate2Button,mStoreOwnerRate3Button,mStoreOwnerRate4Button,mStoreOwnerRate5Button,mStoreOwnerRespondToCustomerCancel,mStoreOwnerRespondToCustomerPost;
	ListView mStoreOwnerReviewsListView;
	TextView mStoreOwnerReviewStoreName,mStoreOwnerReviewBack,mStoreOwnerReviewRespondToFriend,mStoreOwnerReviewInAppropriate;
	TextView mStoreOwnerViewReviewStoreName,mStoreOwnerViewReviewPosterName,mStoreOwnerViewReviewPostedDate;
	EditText mStoreOwnerViewReviewMessage,mStoreOwnerRespondToCustomerMessageId;
	public int mScreenWidth;
	public double mMenuWidth;

	public View mFooterLayout;
	private TextView mFooterText;
	LayoutInflater mInflater;

	TextView mStoreOwnermReviewRatingOne,mStoreOwnermReviewRatingTwo,mStoreOwnermReviewRatingThree,mStoreOwnermReviewRatingFour,mStoreOwnermReviewRatingFive,mStoreOwnerViewReviewLikeCount,mStoreOwnerViewReviewDisLikeCount;

	public static String mStoreOwnerReviewsStart="0",mStoreOwnerReviewsEndLimit="20";
	public static String mStoreOwnerReviewsTotalCount = "0";

	public static int UserReviewPosition=-1;
	public static boolean mIsLoadMore=false;
	private int currentPosition=0;
	private String reviewId="",mStoreRating="",mStoreName,mCustomerUserId="",mCustomerName="";

	StoreReviewListAdapter mReviewAdapter;
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			LayoutInflater inflater = LayoutInflater.from(this);
			scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(scrollView);

			mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
			mConnectionAvailabilityChecking = new NetworkCheck();

			//Call function to get width of the screen
			mScreenWidth=getScreenWidth(); 	
			if(mScreenWidth>0){	//To fix Home Page menubar items width
				mMenuWidth=mScreenWidth/3;
				Log.i(TAG,"ScreenWidth : "+mScreenWidth+"\n"+"MenuItemWidth : "+mMenuWidth);
			}

			app = inflater.inflate(R.layout.storeowner_reviews, null);

			mMiddleViewReviewListContainer = (ViewGroup) app.findViewById(R.id.storeownerreview_list_container);
			mMiddleViewReviewRespondToFriendContainer = (ScrollView) app.findViewById(R.id.storeownerreview_respondtofriend_layoutId);
			mMiddleViewReviewDetailsContainer = (ViewGroup) app.findViewById(R.id.storeownerreview_detailslayoutId);
			mTitleBar = (ViewGroup) mMiddleViewReviewListContainer.findViewById(R.id.storeownerreviews_storename_header);
			mFooterView = (ViewGroup) app.findViewById(R.id.storeownerreviews_footerLayoutId);

			mRightMenuHolder = (ImageView) mTitleBar.findViewById(R.id.storeownerreviews_rightmenu);
			mStoreOwnerReviewStoreName = (TextView) mTitleBar.findViewById(R.id.storeownerreviews_store_title_textId);
			mStoreOwnerReviewsFreezeView = (Button) app.findViewById(R.id.storeownerreview_freeze);
			mStoreOwnerReviewsListView = (ListView) mMiddleViewReviewListContainer.findViewById(R.id.storeownerreview_listview);
			mStoreOwnerReviewBack = (TextView) mFooterView.findViewById(R.id.storeownerreviews_back);
			mStoreOwnerReviewBack.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
			mStoreOwnerReviewRespondToFriend = (TextView) mFooterView.findViewById(R.id.storeownerreviews_respondtocustomer);
			mStoreOwnerReviewRespondToFriend.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
			mStoreOwnerReviewInAppropriate = (TextView) mFooterView.findViewById(R.id.storeownerreviews_inappropriate);
			mStoreOwnerReviewInAppropriate.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));

			mStoreOwnermReviewRatingOne = (TextView) mMiddleViewReviewListContainer.findViewById(R.id.mOneStarTextId);
			mStoreOwnermReviewRatingTwo  = (TextView) mMiddleViewReviewListContainer.findViewById(R.id.mTwoStarTextId);
			mStoreOwnermReviewRatingThree = (TextView) mMiddleViewReviewListContainer.findViewById(R.id.mThreeStarTextId);
			mStoreOwnermReviewRatingFour = (TextView) mMiddleViewReviewListContainer.findViewById(R.id.mFourStarTextId);
			mStoreOwnermReviewRatingFive = (TextView) mMiddleViewReviewListContainer.findViewById(R.id.mFiveStarTextId);

			// View Review Details variables
			mStoreOwnerViewReviewStoreName = (TextView)mMiddleViewReviewDetailsContainer. findViewById(R.id.storeownerreview_viewstore_title); 
			mStoreOwnerViewReviewPosterName = (TextView) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_posterName);
			mStoreOwnerViewReviewPostedDate = (TextView) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_date);
			mStoreOwnerViewReviewRatingOne = (ImageView) mMiddleViewReviewDetailsContainer.findViewById(R.id.mStartImage1);
			mStoreOwnerViewReviewRatingTwo = (ImageView) mMiddleViewReviewDetailsContainer.findViewById(R.id.mStartImage2);
			mStoreOwnerViewReviewRatingThree = (ImageView) mMiddleViewReviewDetailsContainer.findViewById(R.id.mStartImage3);
			mStoreOwnerViewReviewRatingFour = (ImageView) mMiddleViewReviewDetailsContainer.findViewById(R.id.mStartImage4);
			mStoreOwnerViewReviewRatingFive = (ImageView) mMiddleViewReviewDetailsContainer.findViewById(R.id.mStartImage5); 
			mStoreOwnerViewReviewMessage =  (EditText) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_descriptionsId);
			mStoreOwnerViewReviewThumbsUp = (ImageView) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_thumbsup);
			mStoreOwnerViewReviewThumbsDown = (ImageView) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_thumbsdown);
			mStoreOwnerViewReviewLikeCount = (TextView) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_likecountId);
			mStoreOwnerViewReviewDisLikeCount = (TextView) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_dislikecountId);
			mStoreOwnerViewReviewPreviousButton = (Button) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_previous);
			mStoreOwnerViewReviewNextButton = (Button) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_next);

			// Respond to review variables
			mStoreOwnerRespondToCustomerMessageId = (EditText) mMiddleViewReviewRespondToFriendContainer.findViewById(R.id.storeownerreview_descriptionId);
			mStoreOwnerRate1Button =  (Button) mMiddleViewReviewRespondToFriendContainer.findViewById(R.id.storeownerreview_rate1ButtonId);
			mStoreOwnerRate2Button =  (Button) mMiddleViewReviewRespondToFriendContainer.findViewById(R.id.storeownerreview_rate2ButtonId);
			mStoreOwnerRate3Button =  (Button) mMiddleViewReviewRespondToFriendContainer.findViewById(R.id.storeownerreview_rate3ButtonId);
			mStoreOwnerRate4Button =  (Button) mMiddleViewReviewRespondToFriendContainer.findViewById(R.id.storeownerreview_rate4ButtonId);
			mStoreOwnerRate5Button =  (Button) mMiddleViewReviewRespondToFriendContainer.findViewById(R.id.storeownerreview_rate5ButtonId);
			mStoreOwnerRespondToCustomerCancel =  (Button) mMiddleViewReviewRespondToFriendContainer.findViewById(R.id.storeownerreview_cancel_buttonId);
			mStoreOwnerRespondToCustomerPost =  (Button) mMiddleViewReviewRespondToFriendContainer.findViewById(R.id.storeownerreview_post_buttonId);
			// Assiging listener for respond to customer rate buttons.........
			mStoreOwnerRate1Button.setOnClickListener(this);
			mStoreOwnerRate2Button.setOnClickListener(this);
			mStoreOwnerRate3Button.setOnClickListener(this);
			mStoreOwnerRate4Button.setOnClickListener(this);
			mStoreOwnerRate5Button.setOnClickListener(this);
			mStoreOwnerRespondToCustomerCancel.setOnClickListener(this);
			mStoreOwnerRespondToCustomerPost.setOnClickListener(this);
			// To set StoreName
			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
			mStoreName = mPrefs.getString("store_name", "");
			mStoreOwnerReviewStoreName.setText(mStoreName);

			storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwnerReviews.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerReviewsFreezeView, TAG);
			mRightMenu = storeowner_rightmenu.intializeInflater();
			storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwnerReviews.this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerReviewsFreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();

			storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);

			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeownerreviews_header);
			header.intializeInflater(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerReviewsFreezeView, TAG);

			final View[] children = new View[] { mLeftMenu, app, mRightMenu };

			/* Scroll to app (view[1]) when layout finished.*/
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));

			WebServiceStaticArrays.GetReviewStatusList.clear();
			mIsLoadMore = false;
			mStoreOwnerReviewsStart ="0";
			mStoreOwnerReviewsEndLimit ="20";
			mStoreOwnerReviewsTotalCount ="0";

			mStoreOwnerReviewBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mMiddleViewReviewListContainer.setVisibility(View.VISIBLE);
					mMiddleViewReviewRespondToFriendContainer.setVisibility(View.GONE);
					mMiddleViewReviewDetailsContainer.setVisibility(View.GONE);
					mFooterView.setVisibility(View.GONE);
				}
			});	

			mStoreOwnerReviewRespondToFriend.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent contact_storeIntent = new Intent(StoreOwnerReviews.this,StoreOwner_ContactStore.class);
					contact_storeIntent.putExtra("class_name", "Contact_store");
					contact_storeIntent.putExtra("source", "reviews");
					contact_storeIntent.putExtra("is_back", true);
					contact_storeIntent.putExtra("customer_id", mCustomerUserId);
					StoreOwner_ContactStore.mCustomerName = mCustomerName;
					startActivity(contact_storeIntent);

				}
			});	

			mStoreOwnerReviewInAppropriate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//call webservice
					PostReviewTask mPostReviewTask = new PostReviewTask(StoreOwnerReviews.this,mMiddleViewReviewListContainer,mMiddleViewReviewRespondToFriendContainer,mStoreOwnerReviewsListView,mStoreOwnerReviewStoreName,mStoreOwnermReviewRatingOne,mStoreOwnermReviewRatingTwo,mStoreOwnermReviewRatingThree,mStoreOwnermReviewRatingFour,mStoreOwnermReviewRatingFour,null);
					mPostReviewTask.execute("INAPPROPRIATE",reviewId);
				}
			});	

			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwnerReviewsFreezeView, TAG));
			mStoreOwnerReviewsFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerReviewsFreezeView, TAG));

			GetStoreReviewDetails reviewDetails = new GetStoreReviewDetails(StoreOwnerReviews.this, mStoreOwnerReviewsListView,mStoreOwnerReviewStoreName,mStoreOwnermReviewRatingOne,mStoreOwnermReviewRatingTwo,mStoreOwnermReviewRatingThree,mStoreOwnermReviewRatingFour,mStoreOwnermReviewRatingFive,"PROGRESS",TAG);
			reviewDetails.execute("");

			//For Footer Layout
			mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
			mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
			mStoreOwnerReviewsListView.addFooterView(mFooterLayout);

			//Scroll listener for review list for setting offset
			mStoreOwnerReviewsListView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					//Check the bottom item is visible
					int lastInScreen = firstVisibleItem + visibleItemCount;	

					if(Integer.parseInt(mStoreOwnerReviewsTotalCount) > lastInScreen && Integer.parseInt(mStoreOwnerReviewsTotalCount)>20){					
						if(lastInScreen == totalItemCount && !mIsLoadMore){												
							Log.i(TAG, "Set Text In The Footer");
							if(mStoreOwnerReviewsListView.getFooterViewsCount() == 0){
								mStoreOwnerReviewsListView.addFooterView(mFooterLayout);
							}
							if(Integer.parseInt(mStoreOwnerReviewsTotalCount) > Integer.parseInt(mStoreOwnerReviewsEndLimit)){
								mFooterText.setText("Loading "+mStoreOwnerReviewsEndLimit+" of "+"("+mStoreOwnerReviewsTotalCount+")");
							}else{
								mFooterText.setText("Loading "+mStoreOwnerReviewsTotalCount);
							}

							Log.i(TAG, "Runn AynckTask To Add More");
							GetStoreReviewDetails reviewDetails = new GetStoreReviewDetails(StoreOwnerReviews.this, mStoreOwnerReviewsListView,mStoreOwnerReviewStoreName,mStoreOwnermReviewRatingOne,mStoreOwnermReviewRatingTwo,mStoreOwnermReviewRatingThree,mStoreOwnermReviewRatingFour,mStoreOwnermReviewRatingFive,"NOPROGRESS",TAG);
							reviewDetails.execute("RefreshAdapter");

						}
					}else{
						if(mStoreOwnerReviewsTotalCount.equalsIgnoreCase("0")){
							Log.i(TAG, "Currently No List Item");
						}else if(mFooterLayout != null && mStoreOwnerReviewsListView.getFooterViewsCount() !=0 &&mStoreOwnerReviewsListView.getAdapter() != null){
							Log.i(TAG, "Remove Footer");
							if(lastInScreen!= totalItemCount){
								Log.i(TAG, "Remove Footer success");
								mStoreOwnerReviewsListView.removeFooterView(mFooterLayout);	
							}else{
								Log.i(TAG, "Remove Footer wait");
							}
						}
					}
				}
			});

			// Click listener for reviews list view..
			mStoreOwnerReviewsListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
					if(WebServiceStaticArrays.mStoreReviewsList.size() > position){
						POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(position);
						currentPosition = position;	
						reviewId = mReviewDetails.review_id;
						mMiddleViewReviewListContainer.setVisibility(View.GONE);
						mMiddleViewReviewRespondToFriendContainer.setVisibility(View.GONE);
						mMiddleViewReviewDetailsContainer.setVisibility(View.VISIBLE);
						mFooterView.setVisibility(View.VISIBLE);
						mStoreOwnerViewReviewPreviousButton.setBackgroundResource(R.drawable.header_2);
						mStoreOwnerViewReviewNextButton.setBackgroundResource(R.drawable.header_2);
						mStoreOwnerViewReviewStoreName.setText(mStoreName);
						mCustomerUserId = mReviewDetails.user_id;
						mCustomerName = mReviewDetails.user_name;
						mStoreOwnerViewReviewPosterName.setText(mReviewDetails.user_name);
						mStoreOwnerViewReviewPostedDate.setText(mReviewDetails.posted_date);
						mStoreOwnerViewReviewMessage.setText(mReviewDetails.message);
						mStoreOwnerViewReviewLikeCount.setText(mReviewDetails.user_likes);
						mStoreOwnerViewReviewDisLikeCount.setText(mReviewDetails.user_dislikes);
						if(position == 0){
							mStoreOwnerViewReviewPreviousButton.setBackgroundResource(R.drawable.button_disabled);
						}
						if(position == WebServiceStaticArrays.mStoreReviewsList.size()-1){
							mStoreOwnerViewReviewNextButton.setBackgroundResource(R.drawable.button_disabled);
						}	
					}else{
						Log.i("Review list", "loading");
					}
				}
			});



			mStoreOwnerViewReviewPreviousButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					currentPosition = currentPosition - 1;
					if(currentPosition >=0){
						mMiddleViewReviewDetailsContainer.scrollTo(0, 0);		
						POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(currentPosition);
						reviewId = mReviewDetails.review_id;
						mStoreOwnerViewReviewStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);
						mStoreOwnerViewReviewPosterName.setText(mReviewDetails.user_name);
						mStoreOwnerViewReviewPostedDate.setText(mReviewDetails.posted_date);
						mStoreOwnerViewReviewMessage.setText(mReviewDetails.message);
						mStoreOwnerViewReviewLikeCount.setText(mReviewDetails.user_likes);
						mStoreOwnerViewReviewDisLikeCount.setText(mReviewDetails.user_dislikes);
						if(currentPosition == 0){
							mStoreOwnerViewReviewPreviousButton.setBackgroundResource(R.drawable.button_disabled);
							mStoreOwnerViewReviewNextButton.setBackgroundResource(R.drawable.header_2);
						}else{
							mStoreOwnerViewReviewPreviousButton.setBackgroundResource(R.drawable.header_2);
							mStoreOwnerViewReviewNextButton.setBackgroundResource(R.drawable.header_2);  
						}
					}else{
						currentPosition = 0;
					}
				}
			});

			mStoreOwnerViewReviewNextButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					currentPosition = currentPosition + 1;
					if(currentPosition <= WebServiceStaticArrays.mStoreReviewsList.size()-1 ){
						mMiddleViewReviewDetailsContainer.scrollTo(0, 0);
						POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(currentPosition);
						reviewId = mReviewDetails.review_id;
						mStoreOwnerViewReviewStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);
						mStoreOwnerViewReviewPosterName.setText(mReviewDetails.user_name);
						mStoreOwnerViewReviewPostedDate.setText(mReviewDetails.posted_date);
						mStoreOwnerViewReviewMessage.setText(mReviewDetails.message);
						mStoreOwnerViewReviewLikeCount.setText(mReviewDetails.user_likes);
						mStoreOwnerViewReviewDisLikeCount.setText(mReviewDetails.user_dislikes);
						if(currentPosition == WebServiceStaticArrays.mStoreReviewsList.size()-1 ){
							mStoreOwnerViewReviewNextButton.setBackgroundResource(R.drawable.button_disabled);
							mStoreOwnerViewReviewPreviousButton.setBackgroundResource(R.drawable.header_2);
						}else{
							mStoreOwnerViewReviewNextButton.setBackgroundResource(R.drawable.header_2);
							mStoreOwnerViewReviewPreviousButton.setBackgroundResource(R.drawable.header_2);  
						}
					}else{
						currentPosition = WebServiceStaticArrays.mStoreReviewsList.size()-1;
					}
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void SetStoreOwnerReviewListAdatpter(String mCheckRefresh) {

		if(mCheckRefresh.equalsIgnoreCase("RefreshAdapter") && mReviewAdapter != null){
			Log.i("Adapter", "Refresh");
			mReviewAdapter.notifyDataSetChanged();
		}else{
			Log.i("Adapter", "Add Adapter");			
			mReviewAdapter = new StoreReviewListAdapter(StoreOwnerReviews.this);
			mStoreOwnerReviewsListView.setAdapter(mReviewAdapter);			
		}

		if(mFooterLayout != null && mStoreOwnerReviewsListView.getFooterViewsCount() !=0 &&mStoreOwnerReviewsListView.getAdapter() != null){
			Log.i(TAG, "Remove Footer View");
			mStoreOwnerReviewsListView.removeFooterView(mFooterLayout);
		}
	}

	/*Get Screen width*/
	public int getScreenWidth(){
		int Measuredwidth = 0;  
		int Measuredheight = 0;  
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Measuredwidth = display.getWidth(); 
		Measuredheight = display.getHeight();
		return Measuredwidth;
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		if(mNotificationSync!=null){
			mNotificationSync.cancelAlarm();
		}
	}
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwnerReviews.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwnerReviews.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwnerReviews.this);
		mLogoutSession.setLogoutTimerAlarm();
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwnerReviewsListView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.storeownerreview_rate1ButtonId:
			mStoreOwnerRate1Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate2Button.setBackgroundResource(R.drawable.button_border);
			mStoreOwnerRate3Button.setBackgroundResource(R.drawable.button_border);
			mStoreOwnerRate4Button.setBackgroundResource(R.drawable.button_border);
			mStoreOwnerRate5Button.setBackgroundResource(R.drawable.button_border);
			mStoreRating="1";
			break;
		case R.id.storeownerreview_rate2ButtonId:
			mStoreOwnerRate1Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate2Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate3Button.setBackgroundResource(R.drawable.button_border);
			mStoreOwnerRate4Button.setBackgroundResource(R.drawable.button_border);
			mStoreOwnerRate5Button.setBackgroundResource(R.drawable.button_border);
			mStoreRating="2";
			break;
		case R.id.storeownerreview_rate3ButtonId:
			mStoreOwnerRate1Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate2Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate3Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate4Button.setBackgroundResource(R.drawable.button_border);
			mStoreOwnerRate5Button.setBackgroundResource(R.drawable.button_border);
			mStoreRating="3";
			break;
		case R.id.storeownerreview_rate4ButtonId:
			mStoreOwnerRate1Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate2Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate3Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate4Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate5Button.setBackgroundResource(R.drawable.button_border);
			mStoreRating="4";
			break;
		case R.id.storeownerreview_rate5ButtonId:
			mStoreOwnerRate1Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate2Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate3Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate4Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate5Button.setBackgroundResource(R.drawable.star);
			mStoreRating="5";
			break;
		case R.id.storeownerreview_cancel_buttonId:
			mMiddleViewReviewListContainer.setVisibility(View.GONE);
			mMiddleViewReviewRespondToFriendContainer.setVisibility(View.GONE);
			mMiddleViewReviewDetailsContainer.setVisibility(View.VISIBLE);
			mFooterView.setVisibility(View.VISIBLE);
			break;
		case R.id.storeownerreview_post_buttonId:
			// Call to webservice to respond to review...
			break;
		default:
			break;
		}
	}

	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(StoreOwnerReviews.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			try{
				Log.i(TAG,"OnReceive");
				if(intent.hasExtra("FromNotification")){
					if(NotificationDetails.notificationcount>0){
						header.mTabBarNotificationCountBtn.setVisibility(View.VISIBLE);
						header.mTabBarNotificationCountBtn.setText(String.valueOf(NotificationDetails.notificationcount));
					}else{
						header.mTabBarNotificationCountBtn.setVisibility(View.GONE);
						Log.i("Notification result", "No new notification");
					}
				}					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};

}
