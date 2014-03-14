package com.us.zoupons.storeowner.reviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.shopper.reviews.GetStoreReviewDetails;
import com.us.zoupons.shopper.reviews.POJOStoreReviewDetails;
import com.us.zoupons.shopper.reviews.PostReviewTask;
import com.us.zoupons.shopper.reviews.StoreReviewListAdapter;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.customercenter.StoreOwner_chatsupport;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

/**
 * 
 * Activity to display store review details
 *
 */

public class StoreOwnerReviews extends Activity implements OnClickListener{
	// Initializing views and variables
	private String TAG="StoreOwnerReviews";
	private MyHorizontalScrollView mScrollView;
	private View mApp,mRightMenu,mLeftMenu,mFooterLayout;
	private Header mZouponsHeader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private int mMenuFlag;
	private ViewGroup mMiddleViewReviewListContainer,mMiddleViewReviewDetailsContainer,mTitleBar,mFooterView;
	private ScrollView mMiddleViewReviewRespondToFriendContainer;
	private ImageView mRightMenuHolder,mViewReviewStarOne,mViewReviewStarTwo,mViewReviewStarThree,mViewReviewStarFour,mViewReviewStarFive;
	private Button mStoreOwnerReviewsFreezeView,mStoreOwnerViewReviewPreviousButton,mStoreOwnerViewReviewNextButton,mStoreOwnerRate1Button,mStoreOwnerRate2Button,mStoreOwnerRate3Button,mStoreOwnerRate4Button,mStoreOwnerRate5Button,mStoreOwnerRespondToCustomerCancel,mStoreOwnerRespondToCustomerPost;
	private ListView mStoreOwnerReviewsListView;
	private TextView mStoreOwnerReviewStoreName,mStoreOwnerReviewBack,mStoreOwnerReviewRespondToFriend,mStoreOwnerReviewInAppropriate;
	private TextView mStoreOwnerViewReviewStoreName,mStoreOwnerViewReviewPosterName,mStoreOwnerViewReviewPostedDate;
	private EditText mStoreOwnerViewReviewMessage;
	private double mMenuWidth;
	private TextView mFooterText;
	private LayoutInflater mInflater;
	private	TextView mStoreOwnermReviewRatingOne,mStoreOwnermReviewRatingTwo,mStoreOwnermReviewRatingThree,mStoreOwnermReviewRatingFour,mStoreOwnermReviewRatingFive,mStoreOwnerViewReviewLikeCount,mStoreOwnerViewReviewDisLikeCount;
	public static String mStoreOwnerReviewsStart="0",mStoreOwnerReviewsEndLimit="20";
	public static String mStoreOwnerReviewsTotalCount = "0";
	public static int UserReviewPosition=-1;
	public static boolean mIsLoadMore=false;
	private int currentPosition=0;
	private String reviewId="",mStoreName,mCustomerUserId="",mCustomerName="";
	private StoreReviewListAdapter mReviewAdapter;
	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			// Referencing view from layout file
			LayoutInflater inflater = LayoutInflater.from(this);
			mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(mScrollView);
			mApp = inflater.inflate(R.layout.storeowner_reviews, null);
			mMiddleViewReviewListContainer = (ViewGroup) mApp.findViewById(R.id.storeownerreview_list_container);
			mMiddleViewReviewRespondToFriendContainer = (ScrollView) mApp.findViewById(R.id.storeownerreview_respondtofriend_layoutId);
			mMiddleViewReviewDetailsContainer = (ViewGroup) mApp.findViewById(R.id.storeownerreview_detailslayoutId);
			mTitleBar = (ViewGroup) mMiddleViewReviewListContainer.findViewById(R.id.storeownerreviews_storename_header);
			mFooterView = (ViewGroup) mApp.findViewById(R.id.storeownerreviews_footerLayoutId);
			mRightMenuHolder = (ImageView) mTitleBar.findViewById(R.id.storeownerreviews_rightmenu);
			mStoreOwnerReviewStoreName = (TextView) mTitleBar.findViewById(R.id.storeownerreviews_store_title_textId);
			mStoreOwnerReviewsFreezeView = (Button) mApp.findViewById(R.id.storeownerreview_freeze);
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
			mViewReviewStarOne = (ImageView) mMiddleViewReviewDetailsContainer.findViewById(R.id.mStartImage1);
			mViewReviewStarTwo = (ImageView) mMiddleViewReviewDetailsContainer.findViewById(R.id.mStartImage2);
			mViewReviewStarThree = (ImageView) mMiddleViewReviewDetailsContainer.findViewById(R.id.mStartImage3);
			mViewReviewStarFour = (ImageView) mMiddleViewReviewDetailsContainer.findViewById(R.id.mStartImage4);
			mViewReviewStarFive = (ImageView) mMiddleViewReviewDetailsContainer.findViewById(R.id.mStartImage5);
			mStoreOwnerViewReviewMessage =  (EditText) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_descriptionsId);
			mStoreOwnerViewReviewLikeCount = (TextView) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_likecountId);
			mStoreOwnerViewReviewDisLikeCount = (TextView) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_dislikecountId);
			mStoreOwnerViewReviewPreviousButton = (Button) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_previous);
			mStoreOwnerViewReviewNextButton = (Button) mMiddleViewReviewDetailsContainer.findViewById(R.id.storeownerreview_next);
			// Respond to review variables
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
			mStoreownerRightmenu = new StoreOwner_RightMenu(StoreOwnerReviews.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerReviewsFreezeView, TAG);
			mRightMenu = mStoreownerRightmenu.intializeInflater();
			mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreOwnerReviews.this,mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerReviewsFreezeView, TAG);
			mLeftMenu = mStoreownerLeftmenu.intializeInflater();
			mStoreownerLeftmenu.clickListener(mLeftMenu);
			mStoreownerRightmenu.clickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsHeader = (Header) mApp.findViewById(R.id.storeownerreviews_header);
			mZouponsHeader.intializeInflater(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerReviewsFreezeView, TAG);
			final View[] children = new View[] { mLeftMenu, mApp, mRightMenu };
			/* Scroll to mApp (view[1]) when layout finished.*/
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsHeader.mLeftMenuBtnSlide));
			mNotificationReceiver = new NotifitcationReceiver(mZouponsHeader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			WebServiceStaticArrays.GetReviewStatusList.clear();
			mIsLoadMore = false;
			mStoreOwnerReviewsStart ="0";
			mStoreOwnerReviewsEndLimit ="20";
			mStoreOwnerReviewsTotalCount ="0";
			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwnerReviewsFreezeView, TAG));
			mStoreOwnerReviewsFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerReviewsFreezeView, TAG));
			// To communicate with webservice to get store reviews
			GetStoreReviewDetails reviewDetails = new GetStoreReviewDetails(StoreOwnerReviews.this, mStoreOwnerReviewsListView,mStoreOwnerReviewStoreName,mStoreOwnermReviewRatingOne,mStoreOwnermReviewRatingTwo,mStoreOwnermReviewRatingThree,mStoreOwnermReviewRatingFour,mStoreOwnermReviewRatingFive,"PROGRESS",TAG);
			reviewDetails.execute("");
			//For Footer Layout
			mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
			mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
			mStoreOwnerReviewsListView.addFooterView(mFooterLayout);

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
					// To lauch chat support activity to respond to customer
					Intent contact_storeIntent = new Intent(StoreOwnerReviews.this,StoreOwner_chatsupport.class);
					contact_storeIntent.putExtra("class_name", "Contact_store");
					contact_storeIntent.putExtra("source", "reviews");
					contact_storeIntent.putExtra("is_back", true);
					contact_storeIntent.putExtra("customer_id", mCustomerUserId);
					StoreOwner_chatsupport.sCustomerName = mCustomerName;
					startActivity(contact_storeIntent);

				}
			});	

			mStoreOwnerReviewInAppropriate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//call webservice to make review to inappropiate
					PostReviewTask mPostReviewTask = new PostReviewTask(StoreOwnerReviews.this,mMiddleViewReviewListContainer,mMiddleViewReviewRespondToFriendContainer,mStoreOwnerReviewsListView,mStoreOwnerReviewStoreName,mStoreOwnermReviewRatingOne,mStoreOwnermReviewRatingTwo,mStoreOwnermReviewRatingThree,mStoreOwnermReviewRatingFour,mStoreOwnermReviewRatingFour,null);
					mPostReviewTask.execute("INAPPROPRIATE",reviewId);
				}
			});	

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
							if(mStoreOwnerReviewsListView.getFooterViewsCount() == 0){
								mStoreOwnerReviewsListView.addFooterView(mFooterLayout);
							}
							if(Integer.parseInt(mStoreOwnerReviewsTotalCount) > Integer.parseInt(mStoreOwnerReviewsEndLimit)){
								mFooterText.setText("Loading "+mStoreOwnerReviewsEndLimit+" of "+"("+mStoreOwnerReviewsTotalCount+")");
							}else{
								mFooterText.setText("Loading "+mStoreOwnerReviewsTotalCount);
							}
							GetStoreReviewDetails reviewDetails = new GetStoreReviewDetails(StoreOwnerReviews.this, mStoreOwnerReviewsListView,mStoreOwnerReviewStoreName,mStoreOwnermReviewRatingOne,mStoreOwnermReviewRatingTwo,mStoreOwnermReviewRatingThree,mStoreOwnermReviewRatingFour,mStoreOwnermReviewRatingFive,"NOPROGRESS",TAG);
							reviewDetails.execute("RefreshAdapter");
						}
					}else{
						if(mStoreOwnerReviewsTotalCount.equalsIgnoreCase("0")){
						}else if(mFooterLayout != null && mStoreOwnerReviewsListView.getFooterViewsCount() !=0 &&mStoreOwnerReviewsListView.getAdapter() != null){
							if(lastInScreen!= totalItemCount){
								mStoreOwnerReviewsListView.removeFooterView(mFooterLayout);	
							}else{
							}
						}
					}
				}
			});

			// Click listener for reviews list view..
			mStoreOwnerReviewsListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
					if(WebServiceStaticArrays.mStoreReviewsList.size() > position){ // To show review detail view
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
						setReviewRating(mReviewDetails.rating);
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
					}
				}
			});

			mStoreOwnerViewReviewPreviousButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					currentPosition = currentPosition - 1;
					if(currentPosition >=0){ //Show previous review detail
						mMiddleViewReviewDetailsContainer.scrollTo(0, 0);		
						POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(currentPosition);
						reviewId = mReviewDetails.review_id;
						mCustomerUserId = mReviewDetails.user_id;
						mCustomerName = mReviewDetails.user_name;
						mStoreOwnerViewReviewStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);
						mStoreOwnerViewReviewPosterName.setText(mReviewDetails.user_name);
						mStoreOwnerViewReviewPostedDate.setText(mReviewDetails.posted_date);
						setReviewRating(mReviewDetails.rating);
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
					if(currentPosition <= WebServiceStaticArrays.mStoreReviewsList.size()-1 ){ // To show next review details
						mMiddleViewReviewDetailsContainer.scrollTo(0, 0);
						POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(currentPosition);
						reviewId = mReviewDetails.review_id;
						mCustomerUserId = mReviewDetails.user_id;
						mCustomerName = mReviewDetails.user_name;
						mStoreOwnerViewReviewStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);
						mStoreOwnerViewReviewPosterName.setText(mReviewDetails.user_name);
						mStoreOwnerViewReviewPostedDate.setText(mReviewDetails.posted_date);
						setReviewRating(mReviewDetails.rating);
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
	
	// To set review rating
	public void setReviewRating(String rating){
		switch(Integer.parseInt(rating)){
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
	}

	// To set or refresh review adapter
	public void SetStoreOwnerReviewListAdatpter(String mCheckRefresh) {
		if(mCheckRefresh.equalsIgnoreCase("RefreshAdapter") && mReviewAdapter != null){
			mReviewAdapter.notifyDataSetChanged();
		}else{
			mReviewAdapter = new StoreReviewListAdapter(StoreOwnerReviews.this);
			mStoreOwnerReviewsListView.setAdapter(mReviewAdapter);			
		}
		if(mFooterLayout != null && mStoreOwnerReviewsListView.getFooterViewsCount() !=0 &&mStoreOwnerReviewsListView.getAdapter() != null){
			mStoreOwnerReviewsListView.removeFooterView(mFooterLayout);
		}
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// To notify  system that its time to run garbage collector service
		System.gc();
	}
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mNotificationReceiver);
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
		// To listen for broadcast receiver to receive notification message
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwnerReviews.this,ZouponsConstants.sStoreModuleFlag);
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
			break;
		case R.id.storeownerreview_rate2ButtonId:
			mStoreOwnerRate1Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate2Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate3Button.setBackgroundResource(R.drawable.button_border);
			mStoreOwnerRate4Button.setBackgroundResource(R.drawable.button_border);
			mStoreOwnerRate5Button.setBackgroundResource(R.drawable.button_border);
			break;
		case R.id.storeownerreview_rate3ButtonId:
			mStoreOwnerRate1Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate2Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate3Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate4Button.setBackgroundResource(R.drawable.button_border);
			mStoreOwnerRate5Button.setBackgroundResource(R.drawable.button_border);

			break;
		case R.id.storeownerreview_rate4ButtonId:
			mStoreOwnerRate1Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate2Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate3Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate4Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate5Button.setBackgroundResource(R.drawable.button_border);

			break;
		case R.id.storeownerreview_rate5ButtonId:
			mStoreOwnerRate1Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate2Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate3Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate4Button.setBackgroundResource(R.drawable.star);
			mStoreOwnerRate5Button.setBackgroundResource(R.drawable.star);

			break;
		case R.id.storeownerreview_cancel_buttonId:
			mMiddleViewReviewListContainer.setVisibility(View.GONE);
			mMiddleViewReviewRespondToFriendContainer.setVisibility(View.GONE);
			mMiddleViewReviewDetailsContainer.setVisibility(View.VISIBLE);
			mFooterView.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwnerReviews.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

}
