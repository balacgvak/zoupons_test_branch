package com.us.zoupons.storeowner.giftcards_deals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.us.zoupons.AmountTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.R;
import com.us.zoupons.android.asyncthread_class.StoreDetailsAsyncTask;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

/**
 * 
 * Activity to display store giftcard/deal cards details
 *
 */

public class StoreOwnerGiftCards extends Activity {

	// Initializing views and variables
	private String mTAG,mEventFlag;
	private MyHorizontalScrollView mScrollView;
	private Header mZouponsHeader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private View mRightMenu,mLeftMenu,mApp;
	private int mMenuFlag;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private ImageView mRightMenuHolder;
	private Button mStoreOwnerGiftCardsFreezeView;
	private ListView mStoreOwnerGiftCardsListView,mStoreOwnerDealCardsDealsListView;
	private TextView mStoreOwnerGiftCardsAvailable,mStoreOwnerGiftCardsPurchased,mStoreOwnerDealCardsDeals;
	private StoreOwnerGiftCardAdapter mStoreOwnerGiftCardAdapter=null;
	private View mFooterLayout;
	private TextView mFooterText;
	private LayoutInflater mInflater;
	private TextView mGiftCardsFaceValue,mDealCardsFaceValue,mCardsPrice,mCardsRemaining,mEditDealsFaceValue;
	private LinearLayout mDealcardContainer,mMenuSplitter,mMenuSplitter2,mDealsStatusInfoContainer;
	private EditText mStoreOwnerDealsChargeValue,mStoreOwnerDealsPerWeek;
	private StoreOwnerDealCardAdapter mDealsAdapter;
	private RelativeLayout mDealsListHeaderContainer;
	private ScrollView mDealsDetailContainer;
	public static boolean mIsLoadMore;
	public static String mDealsStart="0",mDealsEndLimit="20",mDealTotalCount = "0";
	private boolean mShowDealsDefault;
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			// Referencing view from layout xml file
			LayoutInflater inflater = LayoutInflater.from(this);
			mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(mScrollView);
			if(getIntent().getExtras().getString("classname").equalsIgnoreCase("StoreOwnerGiftCards")){ // Giftcards
				mTAG="StoreOwnerGiftCards";
				mEventFlag = "Regular";
			}else if(getIntent().getExtras().getString("classname").equalsIgnoreCase("StoreOwnerDealCards")){ //Deal cards
				mTAG="StoreOwnerDealCards";
				mEventFlag = "Zcard";
				mShowDealsDefault = getIntent().getExtras().getBoolean("show_deals");
			}
			mApp = inflater.inflate(R.layout.storeowner_giftcards, null);
			ViewGroup mTitleBar = (ViewGroup) mApp.findViewById(R.id.storeownergifcards_storename_header);
			final ViewGroup mMiddleView = (ViewGroup) mApp.findViewById(R.id.storeownergifcards_container);
			ViewGroup mFooterView = (ViewGroup) mApp.findViewById(R.id.storeownergifcards_footerLayoutId);
			mRightMenuHolder = (ImageView) mTitleBar.findViewById(R.id.storeownergifcards_rightmenu);
			mStoreOwnerGiftCardsFreezeView = (Button) mApp.findViewById(R.id.storeownergifcards_freeze);
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
			mDealcardContainer = (LinearLayout) mApp.findViewById(R.id.storeownerdeals_middleview);
			mDealsStatusInfoContainer = (LinearLayout) mApp.findViewById(R.id.infotab);
			mDealsListHeaderContainer = (RelativeLayout) mApp.findViewById(R.id.dealcard_listheader_container);
			mStoreOwnerDealCardsDealsListView = (ListView) mDealcardContainer.findViewById(R.id.storeownerdeals_ListView);
			mDealsDetailContainer = (ScrollView) mDealcardContainer.findViewById(R.id.storeownerdeals_detailsView);
			mEditDealsFaceValue = (TextView) mDealcardContainer.findViewById(R.id.storeownerdeals_details_facevalueID);
			mStoreOwnerDealsChargeValue = (EditText) mDealcardContainer.findViewById(R.id.storeownerdeals_details_chargevalueId);
			mStoreOwnerDealsChargeValue.setInputType(InputType.TYPE_CLASS_NUMBER);
			mStoreOwnerDealsChargeValue.addTextChangedListener(new AmountTextWatcher(mStoreOwnerDealsChargeValue));
			mStoreOwnerDealsPerWeek = (EditText) mDealcardContainer.findViewById(R.id.storeownerdeals_details_weeksalevalueId);
			mStoreownerRightmenu = new StoreOwner_RightMenu(StoreOwnerGiftCards.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerGiftCardsFreezeView, mTAG);
			mRightMenu = mStoreownerRightmenu.intializeInflater();
			mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreOwnerGiftCards.this,mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerGiftCardsFreezeView, mTAG);
			mLeftMenu = mStoreownerLeftmenu.intializeInflater();
			mStoreownerLeftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			mStoreownerRightmenu.clickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsHeader = (Header) mApp.findViewById(R.id.storeownergifcards_header);
			mZouponsHeader.intializeInflater(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerGiftCardsFreezeView, mTAG);
			final View[] children = new View[] { mLeftMenu, mApp};
			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsHeader.mLeftMenuBtnSlide));
			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwnerGiftCardsFreezeView, mTAG));
			mStoreOwnerGiftCardsFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerGiftCardsFreezeView, mTAG));
			mNotificationReceiver = new NotifitcationReceiver(mZouponsHeader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			
			mDealsStart="0";
			mDealsEndLimit="20";
			mDealTotalCount = "0";
			//For Footer Layout
			mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
			mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);

			// To show menu bar and other views based upon card type (Giftcards/Dealcards)
			if(mTAG.equalsIgnoreCase("StoreOwnerGiftCards")){  // Giftcards
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
			}else if(mTAG.equalsIgnoreCase("StoreOwnerDealCards")){ //Deal cards
				if(mShowDealsDefault){  // Show deals as default view
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
					StoreOwnerDealCardsAsyncTask mStoreOwnerDealCards = new StoreOwnerDealCardsAsyncTask(StoreOwnerGiftCards.this,"PROGRESS",mStoreOwnerDealCardsDealsListView,"list");
					mStoreOwnerDealCards.execute("");
				}else{ // Show available deals as default view
					mGiftCardsFaceValue.setVisibility(View.GONE);
					mDealCardsFaceValue.setVisibility(View.VISIBLE);
					mCardsPrice.setVisibility(View.VISIBLE);
					mCardsRemaining.setVisibility(View.VISIBLE);
					mStoreOwnerDealCardsDeals.setVisibility(View.VISIBLE);
					mMenuSplitter.setVisibility(View.VISIBLE);
					mMiddleView.setVisibility(View.VISIBLE);
					mDealcardContainer.setVisibility(View.GONE);
					WebServiceStaticArrays.mStoreZouponsDealCardDetails.clear();
					mStoreOwnerGiftCardsListView.setAdapter(new StoreOwnerGiftCardAdapter(StoreOwnerGiftCards.this,R.layout.cardlist_row,"Zcard",WebServiceStaticArrays.mStoreZouponsDealCardDetails));
					MainMenuActivity.mIsLoadMore = false;
					MainMenuActivity.mDealStart = "0";
					MainMenuActivity.mDealTotalCount = "0";
					MainMenuActivity.mDealEndLimit = "20";
					mStoreOwnerGiftCardsAvailable.setBackgroundResource(R.drawable.footer_dark_blue_new);			
					mStoreOwnerDealCardsDeals.setBackgroundResource(R.drawable.header_2);
					mStoreOwnerGiftCardsPurchased.setBackgroundResource(R.drawable.header_2);
					StoreDetailsAsyncTask mStoreDetailsTask = new StoreDetailsAsyncTask(StoreOwnerGiftCards.this,mEventFlag,mStoreOwnerGiftCardsListView,"PROGRESS");
					mStoreDetailsTask.execute("");	
				}

			}

			mStoreOwnerGiftCardsAvailable.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mStoreOwnerGiftCardsPurchased.setBackgroundResource(R.drawable.header_2);
					mStoreOwnerDealCardsDeals.setBackgroundResource(R.drawable.header_2);
					if(mTAG.equalsIgnoreCase("StoreOwnerDealCards")){
						if(mStoreOwnerGiftCardsAvailable.getText().toString().equalsIgnoreCase("available")){ // deals available
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
						}else{ // Inactivate deal card
							StoreOwnerDealCardsAsyncTask mStoreOwnerDealCards = new StoreOwnerDealCardsAsyncTask(StoreOwnerGiftCards.this,"PROGRESS",mStoreOwnerDealCardsDealsListView,"inactivate",mStoreOwnerGiftCardsPurchased.getTag().toString(),"","");
							mStoreOwnerDealCards.execute("");	
						}

					}
				}
			});

			mStoreOwnerGiftCardsPurchased.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if(mStoreOwnerGiftCardsPurchased.getText().toString().equalsIgnoreCase("Purchased")){
						v.setBackgroundResource(R.drawable.footer_dark_blue_new);
						if(mTAG.equalsIgnoreCase("StoreOwnerGiftCards"))
							mStoreOwnerGiftCardsAvailable.setBackgroundResource(R.drawable.footer_dark_blue_new);
						mStoreOwnerGiftCardsPurchased.setBackgroundResource(R.drawable.header_2);
						mStoreOwnerDealCardsDeals.setBackgroundResource(R.drawable.header_2);
						Intent giftcard_purchase =  new Intent(StoreOwnerGiftCards.this,StoreOwnerPurchasedCards.class);
						if(mTAG.equalsIgnoreCase("StoreOwnerGiftCards")){
							giftcard_purchase.putExtra("classname", "StoreOwnerPurchased_GiftCards");
						}else if(mTAG.equalsIgnoreCase("StoreOwnerDealCards")){
							giftcard_purchase.putExtra("classname", "StoreOwnerPurchased_DealCards");
						}
						startActivity(giftcard_purchase);
					}else{ // Save edited deal card
						if(mStoreOwnerDealsChargeValue.getText().toString().equalsIgnoreCase("")){
							alertBox_service("Information", "Please enter deals sell price for the week", mStoreOwnerDealsChargeValue);
						}else if(mStoreOwnerDealsPerWeek.getText().toString().equalsIgnoreCase("") ){
							alertBox_service("Information", "Please enter deals per week count",mStoreOwnerDealsPerWeek);
						}else if(Integer.parseInt(mStoreOwnerDealsPerWeek.getText().toString())==0){
							alertBox_service("Information", "Please enter valid deals per week count value",mStoreOwnerDealsPerWeek);
						}else{
							Double mDealCardFaceValue = Double.valueOf(mStoreOwnerDealsChargeValue.getTag().toString());
							Double mDealCardSellPriceValue = Double.valueOf(mStoreOwnerDealsChargeValue.getText().toString());
							if(mDealCardSellPriceValue == 0){
								alertBox_service("Information", "Please enter valid deal card amount", mStoreOwnerDealsChargeValue);
							}else if(mDealCardSellPriceValue >= mDealCardFaceValue){
								alertBox_service("Information", "Entered amount should be less than the actual deal card amount", mStoreOwnerDealsChargeValue);
							}else if(mDealCardFaceValue/mDealCardSellPriceValue >= 2){ // save changes
								v.setBackgroundResource(R.drawable.footer_dark_blue_new);
								StoreOwnerDealCardsAsyncTask mStoreOwnerDealCards = new StoreOwnerDealCardsAsyncTask(StoreOwnerGiftCards.this,"PROGRESS",mStoreOwnerDealCardsDealsListView,"update",mStoreOwnerGiftCardsPurchased.getTag().toString(),mStoreOwnerDealsChargeValue.getText().toString(),mStoreOwnerDealsPerWeek.getText().toString());
								mStoreOwnerDealCards.execute("");	
							}else{
								AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwnerGiftCards.this);
								service_alert.setTitle("Information");
								service_alert.setMessage("Statistics show that deals of at least 50% Off have the highest success rate in converting new customers.  Would you like to continue or edit your entry?");
								service_alert.setCancelable(false);
								service_alert.setPositiveButton("Edit",new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										mStoreOwnerDealsChargeValue.requestFocus();
										dialog.dismiss();
									}
								});

								service_alert.setNegativeButton("Continue",new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										StoreOwnerDealCardsAsyncTask mStoreOwnerDealCards = new StoreOwnerDealCardsAsyncTask(StoreOwnerGiftCards.this,"PROGRESS",mStoreOwnerDealCardsDealsListView,"update",mStoreOwnerGiftCardsPurchased.getTag().toString(),mStoreOwnerDealsChargeValue.getText().toString(),mStoreOwnerDealsPerWeek.getText().toString());
										mStoreOwnerDealCards.execute("");
									}
								});
								service_alert.show();
							}
						}

					}

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
						StoreOwnerDealCardsAsyncTask mStoreOwnerDealCards = new StoreOwnerDealCardsAsyncTask(StoreOwnerGiftCards.this,"PROGRESS",mStoreOwnerDealCardsDealsListView,"list");
						mStoreOwnerDealCards.execute("");	
					}else{  // For back
						v.setBackgroundResource(R.drawable.footer_dark_blue_new);
						mStoreOwnerDealCardsDealsListView.setVisibility(View.VISIBLE);
						mDealsListHeaderContainer.setVisibility(View.VISIBLE);
						mDealsStatusInfoContainer.setVisibility(View.VISIBLE);
						mDealsDetailContainer.setVisibility(View.GONE);
						mStoreOwnerDealCardsDeals.setText("Deals");
						mStoreOwnerDealCardsDeals.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.giftcard_white, 0, 0);
						mStoreOwnerGiftCardsPurchased.setVisibility(View.VISIBLE);
						mStoreOwnerGiftCardsPurchased.setText("Purchased");
						mStoreOwnerGiftCardsPurchased.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.purchasedgc, 0, 0);
						mMenuSplitter2.setVisibility(View.VISIBLE);
						mStoreOwnerGiftCardsAvailable.setVisibility(View.VISIBLE);
						mStoreOwnerGiftCardsAvailable.setText("Available");
						mStoreOwnerGiftCardsAvailable.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.availablegc, 0, 0);
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
								StoreDetailsAsyncTask mStoreDetailsTask = new StoreDetailsAsyncTask(StoreOwnerGiftCards.this,mEventFlag,mStoreOwnerGiftCardsListView,"NOPROGRESS");
								mStoreDetailsTask.execute("RefreshAdapter");
							}
						}else{
							if(MainMenuActivity.mGiftCardTotalCount.equalsIgnoreCase("0")){
							}else if(mFooterLayout != null && mStoreOwnerGiftCardsListView.getFooterViewsCount() !=0 && mStoreOwnerGiftCardsListView.getAdapter() != null){
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
							// Add Footer view
							if(mStoreOwnerDealCardsDealsListView.getFooterViewsCount() == 0){
								mStoreOwnerDealCardsDealsListView.addFooterView(mFooterLayout);
							}
							if(Integer.parseInt(mDealTotalCount) > Integer.parseInt(mDealsEndLimit)){
								mFooterText.setText("Loading "+mDealsEndLimit+" of "+"("+mDealTotalCount+")");
							}else{
								mFooterText.setText("Loading "+mDealTotalCount);
							}
							StoreOwnerDealCardsAsyncTask mStoreOwnerDealCards = new StoreOwnerDealCardsAsyncTask(StoreOwnerGiftCards.this,"NOPROGRESS",mStoreOwnerDealCardsDealsListView,"list");
							mStoreOwnerDealCards.execute("RefreshAdapter");

						}
					}else{
						if(mDealTotalCount.equalsIgnoreCase("0")){
						}else if(mFooterLayout != null && mStoreOwnerDealCardsDealsListView.getFooterViewsCount() !=0 && mStoreOwnerDealCardsDealsListView.getAdapter() != null){
							if(lastInScreen!= totalItemCount){
								mStoreOwnerDealCardsDealsListView.removeFooterView(mFooterLayout);	
							}else{
							}
						}
					}

				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// To set or refresh Giftcards listview
	public void SetArrayStoreOwnerGiftCardListAdatpter(String mServiceFunction,String mRefreshAdapterStatus){
		if(!mRefreshAdapterStatus.equalsIgnoreCase("")){ // Refresh adaper
			mStoreOwnerGiftCardAdapter.notifyDataSetChanged();
		}else{ // set new adapter
			if(mServiceFunction.equalsIgnoreCase("Regular")){
				mStoreOwnerGiftCardAdapter = new StoreOwnerGiftCardAdapter(StoreOwnerGiftCards.this,R.layout.cardlist_row,mServiceFunction,WebServiceStaticArrays.mStoreRegularCardDetails);	
			}else{
				mStoreOwnerGiftCardAdapter = new StoreOwnerGiftCardAdapter(StoreOwnerGiftCards.this,R.layout.cardlist_row,mServiceFunction,WebServiceStaticArrays.mStoreZouponsDealCardDetails);
			}
			mStoreOwnerGiftCardsListView.setAdapter(mStoreOwnerGiftCardAdapter);
		}
		if(mFooterLayout != null && mStoreOwnerGiftCardsListView.getFooterViewsCount() !=0 && mStoreOwnerGiftCardsListView.getAdapter() != null){
			mStoreOwnerGiftCardsListView.removeFooterView(mFooterLayout);
		}

	}
	
	// To refresh deals list after edit
	public void refreshDealsList() {
		// TODO Auto-generated method stub
		mStoreOwnerDealCardsDeals.setBackgroundResource(R.drawable.footer_dark_blue_new);
		WebServiceStaticArrays.mStoreZouponsDealCardDetails.clear();
		mDealsListHeaderContainer.setVisibility(View.VISIBLE);
		mDealsStatusInfoContainer.setVisibility(View.VISIBLE);
		mStoreOwnerGiftCardsListView.setAdapter(new StoreOwnerGiftCardAdapter(StoreOwnerGiftCards.this,R.layout.cardlist_row,"Zcard",WebServiceStaticArrays.mStoreZouponsDealCardDetails));
		mStoreOwnerDealCardsDealsListView.setVisibility(View.VISIBLE);
		mDealsDetailContainer.setVisibility(View.GONE);
		mStoreOwnerDealCardsDeals.setText("Deals");
		mStoreOwnerDealCardsDeals.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.giftcard_white, 0, 0);
		mStoreOwnerGiftCardsPurchased.setBackgroundResource(R.drawable.header_2);
		mStoreOwnerGiftCardsAvailable.setBackgroundResource(R.drawable.header_2);
		mStoreOwnerGiftCardsPurchased.setVisibility(View.VISIBLE);
		mStoreOwnerGiftCardsPurchased.setText("Purchased");
		mStoreOwnerGiftCardsPurchased.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.purchasedgc, 0, 0);
		mMenuSplitter2.setVisibility(View.VISIBLE);
		mStoreOwnerGiftCardsAvailable.setVisibility(View.VISIBLE);
		mStoreOwnerGiftCardsAvailable.setText("Available");
		mStoreOwnerGiftCardsAvailable.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.availablegc, 0, 0);
		mDealsStart="0";
		mDealsEndLimit="20";
		mDealTotalCount = "0";
		// To call deals list
		StoreOwnerDealCardsAsyncTask mStoreOwnerDealCards = new StoreOwnerDealCardsAsyncTask(StoreOwnerGiftCards.this,"PROGRESS",mStoreOwnerDealCardsDealsListView,"list");
		mStoreOwnerDealCards.execute("");
	}
	
	// To set or refresh Dealcards listview
	public void SetArrayStoreOwnerDealCardsListAdatpter(String mRefreshAdapterStatus){
		if(!mRefreshAdapterStatus.equalsIgnoreCase("")){
			mDealsAdapter.notifyDataSetChanged();
		}else{
			mDealsAdapter = new StoreOwnerDealCardAdapter(StoreOwnerGiftCards.this,R.layout.cardlist_row,mDealsStatusInfoContainer,mDealsListHeaderContainer,mStoreOwnerDealCardsDealsListView,mDealsDetailContainer,mStoreOwnerDealCardsDeals,mStoreOwnerGiftCardsAvailable,mMenuSplitter2,mStoreOwnerGiftCardsPurchased,mStoreOwnerDealsChargeValue,mStoreOwnerDealsPerWeek,mEditDealsFaceValue);
			mStoreOwnerDealCardsDealsListView.setAdapter(mDealsAdapter);
		}
		if(mFooterLayout != null && mStoreOwnerDealCardsDealsListView.getFooterViewsCount() !=0 && mStoreOwnerDealCardsDealsListView.getAdapter() != null){
			mStoreOwnerDealCardsDealsListView.removeFooterView(mFooterLayout);
		}
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
		// To notify  system that its time to run garbage collector service
		System.gc();
	}

	// To show alert box with custom message..
	public void alertBox_service(final String title,final String msg,final EditText edittext){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwnerGiftCards.this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(edittext != null)
					edittext.requestFocus();
				dialog.dismiss();
			}
		});
		service_alert.show();
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
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwnerGiftCards.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
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
		new CheckUserSession(StoreOwnerGiftCards.this).checkIfSessionExpires();
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwnerGiftCards.this,ZouponsConstants.sStoreModuleFlag);
		mNotificationSync.setRecurringAlarm();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwnerGiftCards.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
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
	
}
