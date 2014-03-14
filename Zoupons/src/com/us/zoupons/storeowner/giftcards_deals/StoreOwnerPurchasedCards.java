package com.us.zoupons.storeowner.giftcards_deals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
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
 * Activity to host customers purchased gitcards/deal cards details
 *
 */

public class StoreOwnerPurchasedCards extends Activity implements OnClickListener{

	private String mTAG,mSelectedFromDate,mSelectedToDate;
	private  MyHorizontalScrollView mScrollView;
	private View mApp;
	private Header mZouponsHeader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private View mRightMenu;
	private View mLeftMenu;
	private int mMenuFlag;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private Button mPurchasedFreezeView;
	private ListView mPurchasedListView;
	private TextView mPurchasedAvailable,mPurchasedPurchased;
	private PurchasedCardAdaper mPurchasedAdapter=null;
	private TextView mStoreDealsMenu;
	private EditText mPurchasedSearchValue,mPurchasedCardFromDate,mPurchasedCardToDate;
	private Button mPurchasedSearchCancel;
	private LinearLayout mMenuSplitter;
	private ImageView mPurchasedCardFromDateContextImage,mPurchasedCardToDateContextImage;
	private ArrayList<Object> mTempPurchasedCardList,mSearchedPurchasedCardList;
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(mScrollView);
		//Call function to get width of the screen
		if(getIntent().getExtras().getString("classname").equalsIgnoreCase("StoreOwnerPurchased_GiftCards")){ // From Giftcars
			mTAG="StoreOwnerPurchased_GiftCards";
		}else if(getIntent().getExtras().getString("classname").equalsIgnoreCase("StoreOwnerPurchased_DealCards")){ // Fron dealcards
			mTAG="StoreOwnerPurchased_DealCards";
		}
		mApp = inflater.inflate(R.layout.storeowner_giftcards_deals_purchased, null);
		ViewGroup mMiddleView = (ViewGroup) mApp.findViewById(R.id.purchased_container);
		ViewGroup mFooterView = (ViewGroup) mApp.findViewById(R.id.purchased_footerLayoutId);
		mPurchasedFreezeView = (Button) mApp.findViewById(R.id.purchased_freeze);
		mPurchasedListView = (ListView) mMiddleView.findViewById(R.id.purchased_listId);
		mPurchasedSearchValue = (EditText) mMiddleView.findViewById(R.id.purchased_searchId);
		mPurchasedSearchCancel = (Button) mMiddleView.findViewById(R.id.purchased_search_buttonId);
		mStoreDealsMenu = (TextView) mFooterView.findViewById(R.id.storeownerdeals_dealsId);
		mPurchasedAvailable = (TextView) mFooterView.findViewById(R.id.purchased_availabletextId);
		mPurchasedPurchased = (TextView) mFooterView.findViewById(R.id.purchased_purchasedtextId);
		mMenuSplitter = (LinearLayout) mFooterView.findViewById(R.id.purchased_menubar_splitter1);
		mPurchasedAvailable.setBackgroundResource(R.drawable.header_2);
		mPurchasedPurchased.setBackgroundResource(R.drawable.footer_dark_blue_new);
		mStoreownerRightmenu = new StoreOwner_RightMenu(StoreOwnerPurchasedCards.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mPurchasedFreezeView, mTAG);
		mRightMenu = mStoreownerRightmenu.intializeInflater();
		mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreOwnerPurchasedCards.this,mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mPurchasedFreezeView, mTAG);
		mLeftMenu = mStoreownerLeftmenu.intializeInflater();
		mStoreownerLeftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
		mStoreownerRightmenu.clickListener(mLeftMenu,mRightMenu);
		/* Header Tab Bar which contains logout,notification and home buttons*/
		mZouponsHeader = (Header) mApp.findViewById(R.id.purchased_header);
		mZouponsHeader.intializeInflater(mScrollView, mLeftMenu, mMenuFlag=1, mPurchasedFreezeView, mTAG);
		final View[] children = new View[] { mLeftMenu, mApp, mRightMenu };
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsHeader.mLeftMenuBtnSlide));
		mPurchasedFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mPurchasedFreezeView, mTAG, mPurchasedSearchValue, true));
		mNotificationReceiver = new NotifitcationReceiver(mZouponsHeader.mTabBarNotificationCountBtn);
		// Notitification pop up layout declaration
		mZouponsHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
		mZouponsHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
		//Purchased card List Date contanier
		mPurchasedCardFromDate = (EditText) mMiddleView.findViewById(R.id.giftcard_dealcardlist_fromdate_value);
		mPurchasedCardFromDate.setTag("FromDate");
		mPurchasedCardFromDate.setOnClickListener(this);
		mPurchasedCardToDate = (EditText) mMiddleView.findViewById(R.id.giftcard_dealcardlist_Todate_value);
		mPurchasedCardToDate.setTag("ToDate");
		mPurchasedCardToDate.setOnClickListener(this);
		mPurchasedCardFromDateContextImage = (ImageView) mMiddleView.findViewById(R.id.giftcard_dealcardlist_fromdate_contextImage);
		mPurchasedCardFromDateContextImage.setTag("FromDate");
		mPurchasedCardFromDateContextImage.setOnClickListener(this);
		mPurchasedCardToDateContextImage = (ImageView) mMiddleView.findViewById(R.id.giftcard_dealcardlist_Todate_contextImage);
		mPurchasedCardToDateContextImage.setTag("ToDate");
		mPurchasedCardToDateContextImage.setOnClickListener(this);
		mTempPurchasedCardList = new ArrayList<Object>();
		mSearchedPurchasedCardList = new ArrayList<Object>();

		if(mTAG.equalsIgnoreCase("StoreOwnerPurchased_GiftCards")){ // To hide deals menu at footer for giftcards purchased list 
			mStoreDealsMenu.setVisibility(View.GONE);
			mMenuSplitter.setVisibility(View.GONE);
		}
		setDefaultPurchasedCardListDate(); // Default set to one month difference
		if(new NetworkCheck().ConnectivityCheck(StoreOwnerPurchasedCards.this)){ // Network connectivity check.
			PurchasedAsyncTask mPurchasedAsyncTask = new PurchasedAsyncTask(StoreOwnerPurchasedCards.this,mTAG,mSelectedFromDate,mSelectedToDate);
			mPurchasedAsyncTask.execute();	
		}else{
			Toast.makeText(StoreOwnerPurchasedCards.this, "Network connection not available", Toast.LENGTH_SHORT).show();
		}

		mStoreDealsMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// To start Deals activity
				Intent store_deals_intent = new Intent(StoreOwnerPurchasedCards.this,StoreOwnerGiftCards.class);
				store_deals_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				store_deals_intent.putExtra("classname", "StoreOwnerDealCards");
				store_deals_intent.putExtra("show_deals", true);
				store_deals_intent.putExtra("show_available_deals", false);
				startActivity(store_deals_intent);
			}
		});

		mPurchasedAvailable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mTAG.equalsIgnoreCase("StoreOwnerPurchased_DealCards")){
					Intent store_deals_intent = new Intent(StoreOwnerPurchasedCards.this,StoreOwnerGiftCards.class);
					store_deals_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					store_deals_intent.putExtra("classname", "StoreOwnerDealCards");
					store_deals_intent.putExtra("show_deals", false);
					store_deals_intent.putExtra("show_available_deals", true);
					startActivity(store_deals_intent);
				}else{
					finish();	
				}

			}
		});

		mPurchasedSearchCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPurchasedSearchValue.getText().clear();
				mPurchasedAdapter = new PurchasedCardAdaper(StoreOwnerPurchasedCards.this,R.layout.purchased_list_row,mTempPurchasedCardList);
				mPurchasedListView.setAdapter(mPurchasedAdapter);
			}
		});

		mPurchasedSearchValue.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!mPurchasedSearchValue.getText().toString().trim().equals("")){
					String searchString=s.toString();
					mSearchedPurchasedCardList.clear();
					for(int i=0;i<mTempPurchasedCardList.size();i++)
					{
						PurchaseCardDetails purchased_card_details = (PurchaseCardDetails) mTempPurchasedCardList.get(i);
						if((purchased_card_details.customer_first_name.toLowerCase() + " " +purchased_card_details.customer_last_name.toLowerCase()).contains(searchString.toLowerCase())){
							mSearchedPurchasedCardList.add(purchased_card_details);
						} 
					}
					mPurchasedAdapter = new PurchasedCardAdaper(StoreOwnerPurchasedCards.this,R.layout.purchased_list_row,mSearchedPurchasedCardList);
					mPurchasedListView.setAdapter(mPurchasedAdapter);
				}else{
					mPurchasedAdapter = new PurchasedCardAdaper(StoreOwnerPurchasedCards.this,R.layout.purchased_list_row,mTempPurchasedCardList);
					mPurchasedListView.setAdapter(mPurchasedAdapter);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void afterTextChanged(Editable s) {}
		});

	}

	// To populate list after fetching data from server
	public void SetArrayPurchasedListAdatpter(ArrayList<Object> purchased_gc_dc_list){
		mTempPurchasedCardList = purchased_gc_dc_list;
		mPurchasedAdapter = new PurchasedCardAdaper(StoreOwnerPurchasedCards.this,R.layout.purchased_list_row,purchased_gc_dc_list);
		mPurchasedListView.setAdapter(mPurchasedAdapter);
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
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwnerPurchasedCards.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		new CheckUserSession(StoreOwnerPurchasedCards.this).checkIfSessionExpires();
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwnerPurchasedCards.this,ZouponsConstants.sStoreModuleFlag);
		mNotificationSync.setRecurringAlarm();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwnerPurchasedCards.this);
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
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mPurchasedListView.setVisibility(View.VISIBLE);
		mPurchasedSearchValue.setFocusable(true);
		mPurchasedSearchValue.setFocusableInTouchMode(true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		openDatePicker(v.getTag().toString());
	}

	// Function to open date picker and update purchased list
	public void openDatePicker(final String dateType){
		Calendar mChoosedCalander;
		if(dateType.equalsIgnoreCase("FromDate")){  // From date
			mChoosedCalander = Calendar.getInstance();
			mChoosedCalander.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mPurchasedCardFromDate.getText().toString().split("/")[1]));
			mChoosedCalander.set(Calendar.MONTH, Integer.parseInt(mPurchasedCardFromDate.getText().toString().split("/")[0])-1);
			mChoosedCalander.set(Calendar.YEAR, Integer.parseInt(mPurchasedCardFromDate.getText().toString().split("/")[2]));
		}else if(mPurchasedCardToDate.getText().toString().trim().length()>0){ // To date 
			mChoosedCalander = Calendar.getInstance();
			mChoosedCalander.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mPurchasedCardToDate.getText().toString().split("/")[1]));
			mChoosedCalander.set(Calendar.MONTH, Integer.parseInt(mPurchasedCardToDate.getText().toString().split("/")[0])-1);
			mChoosedCalander.set(Calendar.YEAR, Integer.parseInt(mPurchasedCardToDate.getText().toString().split("/")[2]));
		}else{ // Empty To date
			mChoosedCalander = Calendar.getInstance();
		}

		DatePickerDialog mDatePicker = new DatePickerDialog(StoreOwnerPurchasedCards.this,new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

				if(dateType.equalsIgnoreCase("FromDate")){ // set selected date in From date field
					Date mSelectedDate = new Date(year, monthOfYear, dayOfMonth);
					if(!isFutureDateSelected(year, monthOfYear, dayOfMonth)){
						mPurchasedCardFromDate.setText(String.format("%02d",(mSelectedDate.getMonth()+1))+"/"+String.format("%02d",mSelectedDate.getDate())+"/"+mSelectedDate.getYear());
						mPurchasedCardToDate.getText().clear();
						mSelectedFromDate = mSelectedDate.getYear()+"-"+String.format("%02d",(mSelectedDate.getMonth()+1))+"-"+String.format("%02d",mSelectedDate.getDate());
						// To clear list
						mPurchasedAdapter = new PurchasedCardAdaper(StoreOwnerPurchasedCards.this,R.layout.purchased_list_row,new ArrayList<Object>());
						mPurchasedListView.setAdapter(mPurchasedAdapter);
						// To set current date in To field
						Calendar originalDateCalendar = Calendar.getInstance();
						mSelectedToDate = originalDateCalendar.get(Calendar.YEAR)+"-"+String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"-"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH));
						mPurchasedCardToDate.setText(String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"/"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH))+"/"+originalDateCalendar.get(Calendar.YEAR));
						mPurchasedSearchValue.getText().clear();
						if(new NetworkCheck().ConnectivityCheck(StoreOwnerPurchasedCards.this)){
							PurchasedAsyncTask mPurchasedAsyncTask = new PurchasedAsyncTask(StoreOwnerPurchasedCards.this,mTAG,mSelectedFromDate,mSelectedToDate);
							mPurchasedAsyncTask.execute();		
						}else{
							Toast.makeText(StoreOwnerPurchasedCards.this, "Network connection not available", Toast.LENGTH_SHORT).show();
						}
					}else{
						AlertDialog.Builder mAlertBox = new AlertDialog.Builder(StoreOwnerPurchasedCards.this);
						mAlertBox.setTitle("Information");
						mAlertBox.setMessage("Please choose previous date");
						mAlertBox.setPositiveButton("ok", null);
						mAlertBox.show();
					}
				}else{  // set selected date in To date field
					if(mPurchasedCardFromDate.getText().toString().length()>0){

						if(isFutureDateSelected(year, monthOfYear, dayOfMonth)){
							AlertDialog.Builder mAlertBox = new AlertDialog.Builder(StoreOwnerPurchasedCards.this);
							mAlertBox.setTitle("Information");
							mAlertBox.setMessage("Please choose previous date");
							mAlertBox.setPositiveButton("ok", null);
							mAlertBox.show();	
						}else{
							String mFromDate = mPurchasedCardFromDate.getText().toString();
							int mDate = Integer.parseInt(mFromDate.split("/")[1]); 
							int mMonth = Integer.parseInt(mFromDate.split("/")[0]);
							int mYear = Integer.parseInt(mFromDate.split("/")[2]);	
							Date mSelectedFromDateObject = new Date(mYear,mMonth-1,mDate);
							Date mSelectedDate = new Date(year, monthOfYear, dayOfMonth);
							if(mSelectedFromDateObject.compareTo(mSelectedDate) <= 0){
								mPurchasedCardToDate.setText(String.format("%02d",(mSelectedDate.getMonth()+1))+"/"+String.format("%02d",mSelectedDate.getDate())+"/"+mSelectedDate.getYear());
								mSelectedToDate = mSelectedDate.getYear()+"-"+String.format("%02d",(mSelectedDate.getMonth()+1))+"-"+String.format("%02d",mSelectedDate.getDate());
								mPurchasedSearchValue.getText().clear();
								if(new NetworkCheck().ConnectivityCheck(StoreOwnerPurchasedCards.this)){
									PurchasedAsyncTask mPurchasedAsyncTask = new PurchasedAsyncTask(StoreOwnerPurchasedCards.this,mTAG,mSelectedFromDate,mSelectedToDate);
									mPurchasedAsyncTask.execute();		
								}else{
									Toast.makeText(StoreOwnerPurchasedCards.this, "Network connection not available", Toast.LENGTH_SHORT).show();
								}
							}else{
								AlertDialog.Builder mAlertBox = new AlertDialog.Builder(StoreOwnerPurchasedCards.this);
								mAlertBox.setTitle("Information");
								mAlertBox.setMessage("Please choose date greater than From date");
								mAlertBox.setPositiveButton("ok", null);
								mAlertBox.show();
							}
						}
					}else{
						AlertDialog.Builder mAlertBox = new AlertDialog.Builder(StoreOwnerPurchasedCards.this);
						mAlertBox.setTitle("Information");
						mAlertBox.setMessage("Please choose From date");
						mAlertBox.setPositiveButton("ok", null);
						mAlertBox.show();
					}
				}	
			}
		}, mChoosedCalander.get(Calendar.YEAR), mChoosedCalander.get(Calendar.MONTH), mChoosedCalander.get(Calendar.DAY_OF_MONTH));
		mDatePicker.show();
	}

	/*To check choosed date is future date or not */

	private boolean isFutureDateSelected(int year,int month,int day){
		Calendar mCurrentTime = Calendar.getInstance();
		int mDate = mCurrentTime.get(Calendar.DAY_OF_MONTH);
		int mMonth = mCurrentTime.get(Calendar.MONTH);
		int mYear = mCurrentTime.get(Calendar.YEAR);	
		Date mCurrentDate = new Date(mYear,mMonth,mDate);
		Date mSelectedDate = new Date(year, month, day);
		if(mCurrentDate.compareTo(mSelectedDate) >= 0){ // Not future date
			return false;
		}else{ // Future date
			return true;	
		}
	}

	// To set default (one month diffenrence) date for filtering list
	public void setDefaultPurchasedCardListDate(){
		// Setting default values to from date and To date
		Calendar originalDateCalendar = Calendar.getInstance();
		mSelectedFromDate = mSelectedToDate = originalDateCalendar.get(Calendar.YEAR)+"-"+String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"-"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH));
		mPurchasedCardFromDate.setText(String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"/"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH))+"/"+originalDateCalendar.get(Calendar.YEAR));
		mPurchasedCardToDate.setText(String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"/"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH))+"/"+originalDateCalendar.get(Calendar.YEAR));
	}

}
