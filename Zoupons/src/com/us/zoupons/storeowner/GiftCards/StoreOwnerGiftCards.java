package com.us.zoupons.storeowner.GiftCards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.android.AsyncThreadClasses.StoreDetailsAsyncTask;
import com.us.zoupons.cards.StoreGiftCardDetails;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.Deals.StoreOwnerDealCardAdapter;
import com.us.zoupons.storeowner.Deals.StoreOwnerDealCards;
import com.us.zoupons.storeowner.Deals.StoreOwnerDealCardsAsyncTask;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class StoreOwnerGiftCards extends Activity {

	public String TAG,mEventFlag;
	public MyHorizontalScrollView scrollView;
    View app;
    public Typeface mZouponsFont;
    public NetworkCheck mConnectionAvailabilityChecking;
	Header header;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;
	View mLeftMenu;
	int mMenuFlag;
	StoreOwner_LeftMenu storeowner_leftmenu;
	ImageView mRightMenuHolder;
	Button mStoreOwnerGiftCardsFreezeView;
	ListView mStoreOwnerGiftCardsListView,mStoreOwnerDealCardsDealsListView;
	TextView mStoreOwnerGiftCardsAvailable,mStoreOwnerGiftCardsPurchased,mStoreOwnerDealCardsDeals;
	public int mScreenWidth;
	public double mMenuWidth;
	StoreOwnerGiftCardAdapter mStoreOwnerGiftCardAdapter=null;
	public View mFooterLayout;
	private TextView mFooterText;
	private LayoutInflater mInflater;
	TextView mGiftCardsFaceValue,mDealCardsFaceValue,mCardsPrice,mCardsRemaining;
	private LinearLayout mDealcardContainer,mMenuSplitter,mMenuSplitter2,mDealsStatusInfoContainer;
	private EditText mStoreOwnerDealsChargeValue,mStoreOwnerDealsPerWeek;
	private ImageView mStoreOwnerDealsImage;
	private Button mDetailsSaveButton;
	private StoreOwnerDealCardAdapter deals_adapter;
	private RelativeLayout mDealsListHeaderContainer,mDealsDetailContainer;
	public static boolean mIsLoadMore;
	public static String mDealsStart="0",mDealsEndLimit="20",mDealTotalCount = "0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mConnectionAvailabilityChecking = new NetworkCheck();
		if(getIntent().getExtras().getString("classname").equalsIgnoreCase("StoreOwnerGiftCards")){
			TAG="StoreOwnerGiftCards";
			mEventFlag = "Regular";
		}else if(getIntent().getExtras().getString("classname").equalsIgnoreCase("StoreOwnerDealCards")){
			TAG="StoreOwnerDealCards";
			mEventFlag = "Zcard";
		}
		//Call function to get width of the screen
		mScreenWidth=getScreenWidth(); 	
		if(mScreenWidth>0){	//To fix Home Page menubar items width
			mMenuWidth=mScreenWidth/2;
		}
		app = inflater.inflate(R.layout.storeowner_giftcards, null);
		ViewGroup mTitleBar = (ViewGroup) app.findViewById(R.id.storeownergifcards_storename_header);
	    final ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.storeownergifcards_container);
	    ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeownergifcards_footerLayoutId);
	    mRightMenuHolder = (ImageView) mTitleBar.findViewById(R.id.storeownergifcards_rightmenu);
	    mStoreOwnerGiftCardsFreezeView = (Button) app.findViewById(R.id.storeownergifcards_freeze);
	    mDealCardsFaceValue = (TextView) mMiddleView.findViewById(R.id.dealcards_facevaluetextId);
	    mGiftCardsFaceValue = (TextView) mMiddleView.findViewById(R.id.giftcards_facevaluetextId);
	    mCardsPrice = (TextView) mMiddleView.findViewById(R.id.cards_pricetextId);
	    mCardsRemaining = (TextView) mMiddleView.findViewById(R.id.cards_remainingtextid);
	    mStoreOwnerGiftCardsListView = (ListView) mMiddleView.findViewById(R.id.storeownergifcards_storename_header_listId);
	    mStoreOwnerGiftCardsAvailable = (TextView) mFooterView.findViewById(R.id.storeownergifcards_availabletextId);
	    mStoreOwnerGiftCardsPurchased = (TextView) mFooterView.findViewById(R.id.storeownergifcards_purchasedtextId);
	    mStoreOwnerDealCardsDeals = (TextView) mFooterView.findViewById(R.id.storeownerdeals_dealsId);
	    mMenuSplitter = (LinearLayout) mFooterView.findViewById(R.id.menubar_splitter1);
	    mMenuSplitter2 = (LinearLayout) mFooterView.findViewById(R.id.menubar_splitter2);
	    
		mDealcardContainer = (LinearLayout) app.findViewById(R.id.storeownerdeals_middleview);
		mDealsStatusInfoContainer = (LinearLayout) app.findViewById(R.id.infotab);
		mDealsListHeaderContainer = (RelativeLayout) app.findViewById(R.id.dealcard_listheader_container);
		mStoreOwnerDealCardsDealsListView = (ListView) mDealcardContainer.findViewById(R.id.storeownerdeals_ListView);
		mDealsDetailContainer = (RelativeLayout) mDealcardContainer.findViewById(R.id.storeownerdeals_detailsView);
		mStoreOwnerDealsImage = (ImageView) mDealcardContainer.findViewById(R.id.storeownerdeals_details_cardImageId);
		mStoreOwnerDealsChargeValue = (EditText) mDealcardContainer.findViewById(R.id.storeownerdeals_details_chargevalueId);
		mStoreOwnerDealsPerWeek = (EditText) mDealcardContainer.findViewById(R.id.storeownerdeals_details_weeksalevalueId);
		mDetailsSaveButton = (Button) mDealcardContainer.findViewById(R.id.storeonwerdeals_details_save);
		storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwnerGiftCards.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerGiftCardsFreezeView, TAG);
	    mRightMenu = storeowner_rightmenu.intializeInflater();
	    storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwnerGiftCards.this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerGiftCardsFreezeView, TAG);
	    mLeftMenu = storeowner_leftmenu.intializeInflater();
	    storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
	    storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
	    /* Header Tab Bar which contains logout,notification and home buttons*/
	    header = (Header) app.findViewById(R.id.storeownergifcards_header);
	    header.intializeInflater(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerGiftCardsFreezeView, TAG);
	    final View[] children = new View[] { mLeftMenu, app};
	    // Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
		mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwnerGiftCardsFreezeView, TAG));
		mStoreOwnerGiftCardsFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerGiftCardsFreezeView, TAG));
		mDealsStart="0";
		mDealsEndLimit="20";
		mDealTotalCount = "0";
		//For Footer Layout
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
		mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		
		if(TAG.equalsIgnoreCase("StoreOwnerGiftCards")){
			mMiddleView.setVisibility(View.VISIBLE);
			mDealcardContainer.setVisibility(View.GONE);
			mGiftCardsFaceValue.setVisibility(View.VISIBLE);
			mDealCardsFaceValue.setVisibility(View.INVISIBLE);
			mCardsPrice.setVisibility(View.INVISIBLE);
			mCardsRemaining.setVisibility(View.GONE);
			mStoreOwnerDealCardsDeals.setVisibility(View.GONE);
			mMenuSplitter.setVisibility(View.GONE);
			mStoreOwnerGiftCardsAvailable.setBackgroundResource(R.drawable.footer_dark_blue_new);
			mStoreOwnerGiftCardsPurchased.setBackgroundResource(R.drawable.header_2);
			mStoreOwnerGiftCardsListView.addFooterView(mFooterLayout);
			MainMenuActivity.mIsLoadMore = false;
			MainMenuActivity.mGiftCardStart = "0";
			MainMenuActivity.mGiftCardTotalCount = "0";
			MainMenuActivity.mGiftCardEndLimit = "20";
			StoreDetailsAsyncTask mStoreDetailsTask = new StoreDetailsAsyncTask(StoreOwnerGiftCards.this,mEventFlag,mStoreOwnerGiftCardsListView,"PROGRESS");
			mStoreDetailsTask.execute("");
		}else if(TAG.equalsIgnoreCase("StoreOwnerDealCards")){
			mMiddleView.setVisibility(View.GONE);
			mDealcardContainer.setVisibility(View.VISIBLE);
			mGiftCardsFaceValue.setVisibility(View.GONE);
			mDealCardsFaceValue.setVisibility(View.VISIBLE);
			mCardsPrice.setVisibility(View.VISIBLE);
			mCardsRemaining.setVisibility(View.VISIBLE);
			mStoreOwnerDealCardsDeals.setVisibility(View.VISIBLE);
			mMenuSplitter.setVisibility(View.VISIBLE);
			mStoreOwnerDealCardsDeals.setBackgroundResource(R.drawable.footer_dark_blue_new);
			mStoreOwnerGiftCardsAvailable.setBackgroundResource(R.drawable.header_2);
			mStoreOwnerGiftCardsPurchased.setBackgroundResource(R.drawable.header_2);
			StoreOwnerDealCardsAsyncTask mStoreOwnerDealCards = new StoreOwnerDealCardsAsyncTask(StoreOwnerGiftCards.this,"PROGRESS",mStoreOwnerDealCardsDealsListView);
			mStoreOwnerDealCards.execute("");
		}
		mStoreOwnerGiftCardsAvailable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mStoreOwnerGiftCardsPurchased.setBackgroundResource(R.drawable.header_2);
				mStoreOwnerDealCardsDeals.setBackgroundResource(R.drawable.header_2);
				if(TAG.equalsIgnoreCase("StoreOwnerDealCards")){
					mMiddleView.setVisibility(View.VISIBLE);
					mDealcardContainer.setVisibility(View.GONE);
					WebServiceStaticArrays.mStoreZouponsDealCardDetails.clear();
					mStoreOwnerGiftCardsListView.setAdapter(new StoreOwnerGiftCardAdapter(StoreOwnerGiftCards.this,R.layout.cardlist_row,"Zcard",WebServiceStaticArrays.mStoreZouponsDealCardDetails));
					MainMenuActivity.mIsLoadMore = false;
					MainMenuActivity.mDealStart = "0";
					MainMenuActivity.mDealTotalCount = "0";
					MainMenuActivity.mDealEndLimit = "20";
					StoreDetailsAsyncTask mStoreDetailsTask = new StoreDetailsAsyncTask(StoreOwnerGiftCards.this,mEventFlag,mStoreOwnerGiftCardsListView,"PROGRESS");
					mStoreDetailsTask.execute("");
				}
			}
		});
		
		mStoreOwnerGiftCardsPurchased.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mStoreOwnerGiftCardsAvailable.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mStoreOwnerGiftCardsPurchased.setBackgroundResource(R.drawable.header_2);
				mStoreOwnerDealCardsDeals.setBackgroundResource(R.drawable.header_2);
				Intent giftcard_purchase =  new Intent(StoreOwnerGiftCards.this,StoreOwnerPurchasedCards.class);
				if(TAG.equalsIgnoreCase("StoreOwnerGiftCards")){
					giftcard_purchase.putExtra("classname", "StoreOwnerPurchased_GiftCards");
				}else if(TAG.equalsIgnoreCase("StoreOwnerDealCards")){
					giftcard_purchase.putExtra("classname", "StoreOwnerPurchased_DealCards");
				}
				startActivity(giftcard_purchase);
			}
		});
		
		mStoreOwnerDealCardsDeals.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mStoreOwnerDealCardsDeals.getText().toString().equalsIgnoreCase("deals")){ // For deals
					mMiddleView.setVisibility(View.GONE);
					WebServiceStaticArrays.mStoreZouponsDealCardDetails.clear();
					mStoreOwnerGiftCardsListView.setAdapter(new StoreOwnerGiftCardAdapter(StoreOwnerGiftCards.this,R.layout.cardlist_row,"Zcard",WebServiceStaticArrays.mStoreZouponsDealCardDetails));
					mDealcardContainer.setVisibility(View.VISIBLE);
					mStoreOwnerDealCardsDeals.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mStoreOwnerGiftCardsAvailable.setBackgroundResource(R.drawable.header_2);
					mStoreOwnerGiftCardsPurchased.setBackgroundResource(R.drawable.header_2);
					mDealsStart="0";
					mDealsEndLimit="20";
					mDealTotalCount = "0";
					StoreOwnerDealCardsAsyncTask mStoreOwnerDealCards = new StoreOwnerDealCardsAsyncTask(StoreOwnerGiftCards.this,"PROGRESS",mStoreOwnerDealCardsDealsListView);
					mStoreOwnerDealCards.execute("");	
				}else{  // For back
					mStoreOwnerDealCardsDealsListView.setVisibility(View.VISIBLE);
					mDealsListHeaderContainer.setVisibility(View.VISIBLE);
					mDealsStatusInfoContainer.setVisibility(View.VISIBLE);
					mDealsDetailContainer.setVisibility(View.GONE);
					mStoreOwnerDealCardsDeals.setText("Deals");
					mStoreOwnerDealCardsDeals.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.giftcard_white, 0, 0);
					mStoreOwnerGiftCardsPurchased.setVisibility(View.VISIBLE);
					mMenuSplitter2.setVisibility(View.VISIBLE);
					mStoreOwnerGiftCardsAvailable.setVisibility(View.VISIBLE);
				}
			}
		});
						
		mStoreOwnerGiftCardsListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//Check the bottom item is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;	 			
				if(mEventFlag.equalsIgnoreCase("Regular")){
					if(Integer.parseInt(MainMenuActivity.mGiftCardTotalCount) > lastInScreen && Integer.parseInt(MainMenuActivity.mGiftCardTotalCount) >20){					
						if(lastInScreen == totalItemCount && !MainMenuActivity.mIsLoadMore){												
							if(mStoreOwnerGiftCardsListView.getFooterViewsCount() == 0){
								mStoreOwnerGiftCardsListView.addFooterView(mFooterLayout);
							}
							if(Integer.parseInt(MainMenuActivity.mGiftCardTotalCount) > Integer.parseInt(MainMenuActivity.mGiftCardEndLimit)){
								mFooterText.setText("Loading "+MainMenuActivity.mGiftCardEndLimit+" of "+"("+MainMenuActivity.mGiftCardTotalCount+")");
							}else{
								mFooterText.setText("Loading "+MainMenuActivity.mGiftCardTotalCount);
							}
							//Log.i(TAG, "Runn AynckTask To Add More");
							StoreDetailsAsyncTask mStoreDetailsTask = new StoreDetailsAsyncTask(StoreOwnerGiftCards.this,mEventFlag,mStoreOwnerGiftCardsListView,"NOPROGRESS");
							mStoreDetailsTask.execute("RefreshAdapter");
						}
					}else{
						if(MainMenuActivity.mGiftCardTotalCount.equalsIgnoreCase("0")){
						}else if(mFooterLayout != null && mStoreOwnerGiftCardsListView.getFooterViewsCount() !=0 && mStoreOwnerGiftCardsListView.getAdapter() != null){
							Log.i(TAG, "Removed Based on Total Count Footer");
							if(lastInScreen!= totalItemCount){
								mStoreOwnerGiftCardsListView.removeFooterView(mFooterLayout);	
							}
						}

					}
				}else{
					//Deal
					if(Integer.parseInt(MainMenuActivity.mDealTotalCount) > lastInScreen && Integer.parseInt(MainMenuActivity.mDealTotalCount)>20){					
						if(lastInScreen == totalItemCount && !MainMenuActivity.mIsLoadMore){												
							if(mStoreOwnerGiftCardsListView.getFooterViewsCount() == 0){
								mStoreOwnerGiftCardsListView.addFooterView(mFooterLayout);
							}
							if(Integer.parseInt(MainMenuActivity.mDealTotalCount) > Integer.parseInt(MainMenuActivity.mDealEndLimit)){
								mFooterText.setText("Loading "+MainMenuActivity.mDealEndLimit+" of "+"("+MainMenuActivity.mDealTotalCount+")");
							}else{
								mFooterText.setText("Loading "+MainMenuActivity.mDealTotalCount);
							}
							StoreDetailsAsyncTask mStoreDetailsTask = new StoreDetailsAsyncTask(StoreOwnerGiftCards.this,mEventFlag,mStoreOwnerGiftCardsListView,"NOPROGRESS");
							mStoreDetailsTask.execute("RefreshAdapter");

						}
					}else{	 
						if(MainMenuActivity.mDealTotalCount.equalsIgnoreCase("0")){
							
						}else if(mFooterLayout != null && mStoreOwnerGiftCardsListView.getFooterViewsCount() !=0 && mStoreOwnerGiftCardsListView.getAdapter() != null){
							if(lastInScreen!= totalItemCount){
								mStoreOwnerGiftCardsListView.removeFooterView(mFooterLayout);	
							}else{
							}
						}
					}
				}
			}
		});
		
		
		mStoreOwnerDealCardsDealsListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//Check the bottom item is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;	

				if(Integer.parseInt(mDealTotalCount) > lastInScreen && Integer.parseInt(mDealTotalCount)>20){					
					if(lastInScreen == totalItemCount && !mIsLoadMore){												
						Log.i(TAG, "Set Text In The Footer");
						if(mStoreOwnerDealCardsDealsListView.getFooterViewsCount() == 0){
							mStoreOwnerDealCardsDealsListView.addFooterView(mFooterLayout);
						}

						if(Integer.parseInt(mDealTotalCount) > Integer.parseInt(mDealsEndLimit)){
							mFooterText.setText("Loading "+mDealsEndLimit+" of "+"("+mDealTotalCount+")");
						}else{
							mFooterText.setText("Loading "+mDealTotalCount);
						}
						Log.i(TAG, "Runn AynckTask To Add More");
						StoreOwnerDealCardsAsyncTask mStoreOwnerDealCards = new StoreOwnerDealCardsAsyncTask(StoreOwnerGiftCards.this,"NOPROGRESS",mStoreOwnerDealCardsDealsListView);
						mStoreOwnerDealCards.execute("RefreshAdapter");

					}
				}else{
					if(mDealTotalCount.equalsIgnoreCase("0")){
						Log.i(TAG, "Currently No List Item");
					}else if(mFooterLayout != null && mStoreOwnerDealCardsDealsListView.getFooterViewsCount() !=0 && mStoreOwnerDealCardsDealsListView.getAdapter() != null){
						Log.i(TAG, "Remove Footer");
						if(lastInScreen!= totalItemCount){
							Log.i(TAG, "Remove Footer success");
							mStoreOwnerDealCardsDealsListView.removeFooterView(mFooterLayout);	
						}else{
							Log.i(TAG, "Remove Footer wait");
						}
					}
				}

			}
		});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void SetArrayStoreOwnerGiftCardListAdatpter(String mServiceFunction,String mRefreshAdapterStatus){
		if(!mRefreshAdapterStatus.equalsIgnoreCase("")){
			mStoreOwnerGiftCardAdapter.notifyDataSetChanged();
		}else{
			if(mServiceFunction.equalsIgnoreCase("Regular")){
				mStoreOwnerGiftCardAdapter = new StoreOwnerGiftCardAdapter(StoreOwnerGiftCards.this,R.layout.cardlist_row,mServiceFunction,WebServiceStaticArrays.mStoreRegularCardDetails);	
			}else{
				mStoreOwnerGiftCardAdapter = new StoreOwnerGiftCardAdapter(StoreOwnerGiftCards.this,R.layout.cardlist_row,mServiceFunction,WebServiceStaticArrays.mStoreZouponsDealCardDetails);
			}
			mStoreOwnerGiftCardsListView.setAdapter(mStoreOwnerGiftCardAdapter);
		}
		if(mFooterLayout != null && mStoreOwnerGiftCardsListView.getFooterViewsCount() !=0 && mStoreOwnerGiftCardsListView.getAdapter() != null){
			Log.i(TAG, "Both Not NUll");
			mStoreOwnerGiftCardsListView.removeFooterView(mFooterLayout);
		}

	}
	
	public void SetArrayStoreOwnerDealCardsListAdatpter(String mRefreshAdapterStatus){
		if(!mRefreshAdapterStatus.equalsIgnoreCase("")){
			deals_adapter.notifyDataSetChanged();
		}else{
			deals_adapter = new StoreOwnerDealCardAdapter(StoreOwnerGiftCards.this,R.layout.cardlist_row,mDealsStatusInfoContainer,mDealsListHeaderContainer,mStoreOwnerDealCardsDealsListView,mDealsDetailContainer,mStoreOwnerDealCardsDeals,mStoreOwnerGiftCardsAvailable,mMenuSplitter2,mStoreOwnerGiftCardsPurchased);
			mStoreOwnerDealCardsDealsListView.setAdapter(deals_adapter);
		}
		if(mFooterLayout != null && mStoreOwnerDealCardsDealsListView.getFooterViewsCount() !=0 && mStoreOwnerDealCardsDealsListView.getAdapter() != null){
			Log.i(TAG, "Both Not NUll");
			mStoreOwnerDealCardsDealsListView.removeFooterView(mFooterLayout);
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
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwnerGiftCardsListView.setVisibility(View.VISIBLE);
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
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
