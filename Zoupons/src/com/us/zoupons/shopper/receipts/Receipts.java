package com.us.zoupons.shopper.receipts;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.mobilepay.NormalPaymentAsyncTask;
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
 * Activity which displays and controlls customer receipt details
 *
 */

public class Receipts extends Activity{
	
	// Initializing views and variables
	public static MyHorizontalScrollView scrollView;
	public static View leftmenu,rightmenu;
	private View app;
	private ImageView btnSlide;
	public static Button ReceiptsFreezeButton;
	private Typeface mZouponsFont;
	public static LinearLayout mReceiptsHome,mReceiptsZpay,mReceiptsInvoiceCenter,mReceiptsGiftcards,mReceiptsManageCards,mReceiptsReceipts,mReceiptsMyFavourites,mReceiptsMyFriends,mReceiptsChat,mReceiptsRewards,mReceiptsSettings,
	mReceiptsLogout,mReceiptsLoginLayout,mSendGiftCards,mInviteFriend;
	public static TextView mReceiptsHomeText,mReceiptsZpayText,mReceiptsInvoiceCenterText,mReceiptsGiftCardsText,mReceiptsManageCardsText,mReceiptsReceiptsText,mReceiptsMyFavoritesText,mReceiptsMyFriendsText,mReceiptsChatText,mReceiptsRewardsText,
	mReceiptsSettingsText,mReceiptsLogoutText,mSendGiftCardText,mInviteGiftCardText;
	private RelativeLayout mViewReceiptsContainer;
	private static String TAG = "Receipts";
	private ProgressDialog mProgressDialog= null;
	private ListView mReceiptsList;
	private static EditText mReceiptsNameSearch;
	private Button mSearchCancel;
	private ScrollView mReceiptsDetailsView;
	private TextView mBack,mReceiptStoreName,mReceiptDate,mReceiptTotalAmount,mReceiptTip,mReceiptAmount,mReceiptCardUsed,mReceiptsCreditCardAmount,mReceiptGiftCardUsed,mReceiptGiftCardAmount;
	private RelativeLayout btnLogout; 
	private ImageView mReceiptsMenuImage,mLogoutImage,mNotificationImage,mCalloutTriangleImage,mReceiptsNotesCloseImage;
	private Button mNotificationCount,mReceiptsViewNotesButton;
	private EditText mCustomerReceiptsNotes,mStoreReceiptsNotes;
	//For Dynamic List Update
	private View mFooterLayout;
	private TextView mFooterText;
	public static boolean mIsLoadMore;
	public static String mReceiptStart="0",mReceiptEndLimit="20",mReceiptTotalCount ="0";	
	private CustomReceiptsAdapter mAdapter,mSearchAdapter;
	private LayoutInflater mInflater;
	private View mFirstCardBorder,mSecondCardBorder;
	private LinearLayout mCreditCardAmountLayout,mGiftCardUsedLayout,mGiftCardAmountLayout,mReceiptsDetailsLayout,mReceiptsNotesLayout;
	private ScheduleNotificationSync mNotificationSync;
	public static ScrollView leftMenuScrollView;
	//Logout without user interaction after 5 minute
	private CheckLogoutSession mLogoutSession;
	private  ImageView mTabBarLoginChoice;
	// For store_owner transaction history
	private Header header;
	private StoreOwner_LeftMenu storeowner_leftmenu;
	private StoreOwner_RightMenu storeowner_rightmenu;
	private View mLeftMenu,mRightMenu;
	private int mMenuFlag;
	private RelativeLayout mReceiptsTitleContainer;
	private TextView mReceiptsHeaderText;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		mProgressDialog=new ProgressDialog(this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setProgress(0);
		mProgressDialog.setMax(100);
        app = inflater.inflate(R.layout.receipts, null);
		leftmenu = inflater.inflate(R.layout.shopper_leftmenu, null);
		rightmenu = inflater.inflate(R.layout.friends_rightmenu, null);
		final ViewGroup mReceiptsHeader = (ViewGroup) app.findViewById(R.id.receipts_tabbar);
		final ViewGroup mReceiptsContainer = (ViewGroup) app.findViewById(R.id.receipts_container); 
		final ViewGroup mReceiptsFooterContainer = (ViewGroup) app.findViewById(R.id.receipts_footerLayoutId);
		mViewReceiptsContainer = (RelativeLayout) app.findViewById(R.id.view_receipts_container);
		ReceiptsFreezeButton = (Button) app.findViewById(R.id.receipts_freeze);
		mReceiptsTitleContainer = (RelativeLayout) app.findViewById(R.id.receipts_header_containerlayout);
		mReceiptsMenuImage = (ImageView) app.findViewById(R.id.receipts_rightmenu);
		mReceiptsHeaderText = (TextView) app.findViewById(R.id.receipts_headerText);
		
		if(getIntent().hasExtra("FromStoreOwner_TransactionHistory")){// From customercenter in store module
			storeowner_leftmenu = new StoreOwner_LeftMenu(this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, ReceiptsFreezeButton, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();
			storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			storeowner_rightmenu = new StoreOwner_RightMenu(this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, ReceiptsFreezeButton, TAG);
			mRightMenu = storeowner_rightmenu.intializeCustomerCenterInflater();
			storeowner_rightmenu.customercentermenuClickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeowner_receipts_header);
			header.intializeInflater(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, ReceiptsFreezeButton, TAG);
			final View[] children = new View[] { mLeftMenu, app,mRightMenu};
			/* Scroll to app (view[1]) when layout finished.*/
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
			mReceiptsMenuImage.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag=2, ReceiptsFreezeButton, TAG));
			ReceiptsFreezeButton.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, ReceiptsFreezeButton, TAG));
			mNotificationReceiver = new NotifitcationReceiver(header.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			header.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,header,header.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			header.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,header,header.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
		}else{ // Customer module
			btnSlide = (ImageView)mReceiptsHeader.findViewById(R.id.BtnSlide_receipts);
			// Notification and log out variables
			btnLogout = (RelativeLayout) mReceiptsHeader.findViewById(R.id.zoupons_logout_container);
			mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
			mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
			btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(Receipts.this));//ClickListener for Header Home image
			mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
			mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);

			mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
			new LoginChoiceTabBarImage(Receipts.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();

			// Notitification pop up layout declaration
			mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
			mNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mReceiptsHeader, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
			mNotificationCount.setOnClickListener(new ManageNotificationWindow(this,mReceiptsHeader, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));

			final View[] children = new View[] { leftmenu, app ,rightmenu};
			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx,new SizeCallbackForMenu(btnSlide));
			ViewGroup menuLeftItems = (ViewGroup) leftmenu.findViewById(R.id.menuitems);
			ViewGroup menuRightItems = (ViewGroup) rightmenu.findViewById(R.id.rightmenuitems);

			leftMenuScrollView = (ScrollView) leftmenu.findViewById(R.id.leftmenu_scrollview);
			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);
			
			mReceiptsHome = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_home);
			mReceiptsZpay = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_zpay);
			mReceiptsInvoiceCenter = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_invoicecenter);
			mReceiptsGiftcards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_zgiftcards);
			mReceiptsManageCards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_managecards);
			mReceiptsReceipts = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_receipts);
			mReceiptsMyFavourites = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_myfavourites);
			mReceiptsMyFriends = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_myfriends);
			mReceiptsChat = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_customercenter);
			mReceiptsRewards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_rewards);
			mReceiptsSettings = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_settings);
			mReceiptsLogout = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_logout);

			mReceiptsHomeText = (TextView) menuLeftItems.findViewById(R.id.menuHome);
			mReceiptsHomeText.setTypeface(mZouponsFont);
			mReceiptsZpayText = (TextView) menuLeftItems.findViewById(R.id.menuZpay);
			mReceiptsZpayText.setTypeface(mZouponsFont);
			mReceiptsInvoiceCenterText = (TextView) menuLeftItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
			mReceiptsInvoiceCenterText.setTypeface(mZouponsFont);
			mReceiptsGiftCardsText = (TextView) menuLeftItems.findViewById(R.id.menuGiftCards);
			mReceiptsGiftCardsText.setTypeface(mZouponsFont);
			mReceiptsManageCardsText = (TextView) menuLeftItems.findViewById(R.id.menuManageCards);
			mReceiptsManageCardsText.setTypeface(mZouponsFont);
			mReceiptsReceiptsText = (TextView) menuLeftItems.findViewById(R.id.menuReceipts);
			mReceiptsReceiptsText.setTypeface(mZouponsFont);
			mReceiptsMyFavoritesText = (TextView) menuLeftItems.findViewById(R.id.menufavorites);
			mReceiptsMyFavoritesText.setTypeface(mZouponsFont);
			mReceiptsMyFriendsText = (TextView) menuLeftItems.findViewById(R.id.menuMyFriends);
			mReceiptsMyFriendsText.setTypeface(mZouponsFont);
			mReceiptsChatText = (TextView) menuLeftItems.findViewById(R.id.menuCustomerCenter);
			mReceiptsChatText.setTypeface(mZouponsFont);
			mReceiptsRewardsText = (TextView) menuLeftItems.findViewById(R.id.menuRewards);
			mReceiptsRewardsText.setTypeface(mZouponsFont);
			mReceiptsSettingsText = (TextView) menuLeftItems.findViewById(R.id.menuSettings);
			mReceiptsSettingsText.setTypeface(mZouponsFont);
			mReceiptsLogoutText = (TextView) menuLeftItems.findViewById(R.id.menuLogout);
			mReceiptsLogoutText.setTypeface(mZouponsFont);

			mSendGiftCards = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_sendGiftcard);
			mInviteFriend = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_inviteId);
			mSendGiftCardText = (TextView) menuRightItems.findViewById(R.id.rightmenu_sendGiftcardTextId);
			mSendGiftCardText.setTypeface(mZouponsFont);
			mInviteGiftCardText = (TextView) menuRightItems.findViewById(R.id.rightmenu_inviteTextId);
			mInviteGiftCardText.setTypeface(mZouponsFont);
			ReceiptsFreezeButton.setOnClickListener(new ClickListenerForScrolling(scrollView, leftmenu, rightmenu, 0, ReceiptsFreezeButton));
			btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, leftmenu, rightmenu, 1, ReceiptsFreezeButton));

			mReceiptsHome.setOnClickListener(new MenuItemClickListener(menuLeftItems, Receipts.this,POJOMainMenuActivityTAG.TAG=TAG));
			mReceiptsZpay.setOnClickListener(new MenuItemClickListener(menuLeftItems, Receipts.this,POJOMainMenuActivityTAG.TAG=TAG));
			mReceiptsInvoiceCenter.setOnClickListener(new MenuItemClickListener(menuLeftItems, Receipts.this,POJOMainMenuActivityTAG.TAG));
			mReceiptsGiftcards.setOnClickListener(new MenuItemClickListener(menuLeftItems, Receipts.this,POJOMainMenuActivityTAG.TAG=TAG));
			mReceiptsManageCards.setOnClickListener(new MenuItemClickListener(menuLeftItems, Receipts.this,POJOMainMenuActivityTAG.TAG=TAG));
			mReceiptsReceipts.setOnClickListener(new MenuItemClickListener(menuLeftItems, Receipts.this,POJOMainMenuActivityTAG.TAG=TAG));
			mReceiptsMyFavourites.setOnClickListener(new MenuItemClickListener(menuLeftItems,Receipts.this,POJOMainMenuActivityTAG.TAG=TAG));
			mReceiptsMyFriends.setOnClickListener(new MenuItemClickListener(menuLeftItems,Receipts.this,POJOMainMenuActivityTAG.TAG=TAG));
			mReceiptsChat.setOnClickListener(new MenuItemClickListener(menuLeftItems,Receipts.this,POJOMainMenuActivityTAG.TAG=TAG));
			mReceiptsRewards.setOnClickListener(new MenuItemClickListener(menuLeftItems,Receipts.this,POJOMainMenuActivityTAG.TAG=TAG));
			mReceiptsSettings.setOnClickListener(new MenuItemClickListener(menuLeftItems,Receipts.this,POJOMainMenuActivityTAG.TAG=TAG));
			mReceiptsLogout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					v.setBackgroundResource(R.drawable.gradient_bg_hover);
					
					if(NormalPaymentAsyncTask.mCountDownTimer!=null){
						NormalPaymentAsyncTask.mCountDownTimer.cancel();
						NormalPaymentAsyncTask.mCountDownTimer = null;
					}
					
					// AsyncTask to call the logout webservice to end the login session
					new LogoutSessionTask(Receipts.this,"FromManualLogOut").execute();
				}
			});

			mLogoutImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// AsyncTask to call the logout webservice to end the login session
					new LogoutSessionTask(Receipts.this,"FromManualLogOut").execute();
				}
			});
		}
		mReceiptsList = (ListView) mReceiptsContainer.findViewById(R.id.receipts_listId);
		mReceiptsNameSearch =  (EditText) mReceiptsContainer.findViewById(R.id.receipts_searchId);  
		mSearchCancel = (Button) mReceiptsContainer.findViewById(R.id.receipts_search_buttonId);
		mBack = (TextView) mReceiptsFooterContainer.findViewById(R.id.receipts_leftFooterText);

		mReceiptStoreName = (TextView) mViewReceiptsContainer.findViewById(R.id.view_receipts_storenameId);
		mReceiptsDetailsView = (ScrollView) mViewReceiptsContainer.findViewById(R.id.view_receipts_details);
		mReceiptsDetailsLayout = (LinearLayout) mReceiptsDetailsView.findViewById(R.id.view_receipts_details_container);
		mReceiptDate = (TextView) mReceiptsDetailsLayout.findViewById(R.id.view_receipts_dateId);
		mReceiptAmount = (TextView) mReceiptsDetailsLayout.findViewById(R.id.view_receipts_AmountId);
		mReceiptTip = (TextView) mReceiptsDetailsLayout.findViewById(R.id.view_receipts_tipId);
		mReceiptTotalAmount = (TextView) mReceiptsDetailsLayout.findViewById(R.id.view_receipts_totalChargeId);
		mReceiptCardUsed = (TextView) mReceiptsDetailsLayout.findViewById(R.id.view_receipts_cardTypeId);
		mFirstCardBorder = mReceiptsDetailsLayout.findViewById(R.id.firstcardtype_borderId);
		mSecondCardBorder = mReceiptsDetailsLayout.findViewById(R.id.Secondcardtype_borderId);
		mCreditCardAmountLayout =(LinearLayout) mReceiptsDetailsLayout.findViewById(R.id.creditcardAmountLayoutId);
		mGiftCardUsedLayout = (LinearLayout) mReceiptsDetailsLayout.findViewById(R.id.giftcardUsedLayoutId);
		mGiftCardAmountLayout =(LinearLayout) mReceiptsDetailsLayout.findViewById(R.id.giftcardAmountLayoutId);
		mReceiptsCreditCardAmount = (TextView) mReceiptsDetailsLayout.findViewById(R.id.view_receipts_creditCardAmountId);
		mReceiptGiftCardUsed = (TextView) mReceiptsDetailsLayout.findViewById(R.id.view_receipts_GiftCardId);
		mReceiptGiftCardAmount = (TextView) mReceiptsDetailsLayout.findViewById(R.id.view_receipts_GiftCardAmountId);
		mReceiptsNameSearch.setFocusable(false);
		mReceiptsNameSearch.setFocusableInTouchMode(false);

		mReceiptsNotesLayout = (LinearLayout) mReceiptsDetailsView.findViewById(R.id.view_receipts_notes_container);
		mReceiptsViewNotesButton = (Button) mReceiptsDetailsLayout.findViewById(R.id.receipts_viewnotes);
		mReceiptsNotesCloseImage = (ImageView) mReceiptsNotesLayout.findViewById(R.id.view_recipts_notes_closeImageId);
		mCustomerReceiptsNotes = (EditText) mReceiptsNotesLayout.findViewById(R.id.customer_notes_descriptionId);
		mStoreReceiptsNotes = (EditText) mReceiptsNotesLayout.findViewById(R.id.store_notes_descriptionId);

		//For Footer Layout
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
		mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		mReceiptsList.addFooterView(mFooterLayout);

		mIsLoadMore = false;
		mReceiptStart ="0";
		mReceiptEndLimit ="20";
		mReceiptTotalCount ="0";

		if(getIntent().hasExtra("FromStoreOwner_TransactionHistory")){
			mReceiptsHeader.setVisibility(View.GONE);
			mReceiptsTitleContainer.setVisibility(View.VISIBLE);
			mReceiptsHeaderText.setText(StoreOwner_RightMenu.mCustomer_FirstName+" "+StoreOwner_RightMenu.mCustomer_LastName);
			header.setVisibility(View.VISIBLE);
			mReceiptsNameSearch.setVisibility(View.GONE);
			mSearchCancel.setVisibility(View.GONE);
			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String mStoreLocation_id = mPrefs.getString("location_id", "");
			GetReceiptsDetailsTask mReceiptsTask  = new GetReceiptsDetailsTask(Receipts.this, mReceiptsList,"PROGRESS");
			mReceiptsTask.execute("",StoreOwner_RightMenu.mCustomer_id,mStoreLocation_id);
		}else{
			mReceiptsTitleContainer.setVisibility(View.GONE);
			GetReceiptsDetailsTask mReceiptsTask  = new GetReceiptsDetailsTask(Receipts.this, mReceiptsList,"PROGRESS");
			mReceiptsTask.execute("",UserDetails.mServiceUserId,"");
		}
		
		mReceiptsList.setOnScrollListener(new ReceiptsListScrollListener());
		mReceiptsNameSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!mReceiptsNameSearch.getText().toString().trim().equals("")){
					String searchString=s.toString();
					WebServiceStaticArrays.mSearchedReceiptDetails.clear();
					for(int i=0;i<WebServiceStaticArrays.mReceiptsDetails.size();i++)
					{
						POJOReceiptsDetails ReceiptData = (POJOReceiptsDetails) WebServiceStaticArrays.mReceiptsDetails.get(i);
						String storeName = ReceiptData.store_name;
						if(storeName.toLowerCase().contains(searchString.toLowerCase())){
							POJOReceiptsDetails searchReceiptsdata = new POJOReceiptsDetails();
							searchReceiptsdata.store_name = storeName;
							searchReceiptsdata.card_name = ReceiptData.card_name;
							searchReceiptsdata.card_mask = ReceiptData.card_mask;
							searchReceiptsdata.store_logo = ReceiptData.store_logo;
							searchReceiptsdata.tip_amount = ReceiptData.tip_amount;
							searchReceiptsdata.total_amount = ReceiptData.total_amount;
							searchReceiptsdata.transaction_date = ReceiptData.transaction_date;
							searchReceiptsdata.transaction_type = ReceiptData.transaction_type;
							searchReceiptsdata.CustomerNotes = ReceiptData.CustomerNotes;
							searchReceiptsdata.StoreNotes = ReceiptData.StoreNotes;
							WebServiceStaticArrays.mSearchedReceiptDetails.add(searchReceiptsdata);
						} 
					}
					mReceiptsList.setOnScrollListener(null);
					mSearchAdapter = new CustomReceiptsAdapter(Receipts.this,WebServiceStaticArrays.mSearchedReceiptDetails);
					mReceiptsList.setAdapter(mSearchAdapter);
				}  	
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				if(mReceiptsNameSearch.length() ==0){
					mReceiptsList.setOnScrollListener(new ReceiptsListScrollListener());
					if(mAdapter != null){							   
						if(Integer.parseInt(mReceiptTotalCount)>20 && mReceiptsList.getFooterViewsCount() == 0){
							mReceiptsList.addFooterView(mFooterLayout);
						}
						mAdapter.notifyDataSetChanged();
						mReceiptsList.setAdapter(mAdapter);
					}						   
				}else{
					mReceiptsList.removeFooterView(mFooterLayout);
					mReceiptsList.setOnScrollListener(null);
				}
			}
		});

		mSearchCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mIsLoadMore = false;
				mReceiptStart ="0";
				mReceiptEndLimit ="20";
				mReceiptTotalCount ="0";
				mReceiptsNameSearch.setText("");
				mReceiptsList.setOnScrollListener(new ReceiptsListScrollListener());
				mAdapter = new CustomReceiptsAdapter(Receipts.this,WebServiceStaticArrays.mReceiptsDetails);
				mReceiptsList.setAdapter(mAdapter);	
				
				if(mFooterLayout != null && mReceiptsList.getFooterViewsCount() !=0 &&mReceiptsList.getAdapter() != null){	
						mReceiptsList.removeFooterView(mFooterLayout);
				}
			}
		}); 

		mReceiptsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
				mReceiptsContainer.setVisibility(View.GONE);
				mReceiptsDetailsLayout.setVisibility(View.VISIBLE);
				mReceiptsNotesLayout.setVisibility(View.GONE);
				mViewReceiptsContainer.setVisibility(View.VISIBLE);
				mReceiptsFooterContainer.setVisibility(View.VISIBLE);
				POJOReceiptsDetails receiptdetail = (POJOReceiptsDetails) view.getAdapter().getItem(position);
				mReceiptStoreName.setVisibility(View.GONE);
				mReceiptDate.setText(receiptdetail.transaction_date);
				double total_amount = Double.parseDouble(receiptdetail.total_amount);
				double receiptamount = total_amount -  Double.parseDouble(receiptdetail.tip_amount);
				mReceiptAmount.setText("$"+String.format("%.2f", receiptamount));
				mReceiptTip.setText("$"+receiptdetail.tip_amount);
				mReceiptTotalAmount.setText("$"+String.format("%.2f", total_amount));
				if(receiptdetail.transaction_type.equalsIgnoreCase("Both")){
					mFirstCardBorder.setVisibility(View.VISIBLE);
					mSecondCardBorder.setVisibility(View.VISIBLE);
					mCreditCardAmountLayout.setVisibility(View.VISIBLE);
					mGiftCardUsedLayout.setVisibility(View.VISIBLE);
					mGiftCardAmountLayout.setVisibility(View.VISIBLE);
					mReceiptCardUsed.setText(receiptdetail.card_mask);
					mReceiptsCreditCardAmount.setText("$"+receiptdetail.CreditCardAmount);
					mReceiptGiftCardUsed.setText(receiptdetail.GiftCardUsed);
					mReceiptGiftCardAmount.setText("$"+receiptdetail.GiftCardAmount);
				}else if(receiptdetail.transaction_type.equalsIgnoreCase("Creditcard")){
					mReceiptCardUsed.setText(receiptdetail.card_mask);
					mFirstCardBorder.setVisibility(View.GONE);
					mSecondCardBorder.setVisibility(View.GONE);
					mCreditCardAmountLayout.setVisibility(View.GONE);
					mGiftCardUsedLayout.setVisibility(View.GONE);
					mGiftCardAmountLayout.setVisibility(View.GONE);
				}else{
					mReceiptCardUsed.setText("Giftcard-$"+receiptdetail.GiftCardUsed);
					mFirstCardBorder.setVisibility(View.GONE);
					mSecondCardBorder.setVisibility(View.GONE);
					mCreditCardAmountLayout.setVisibility(View.GONE);
					mGiftCardUsedLayout.setVisibility(View.GONE);
					mGiftCardAmountLayout.setVisibility(View.GONE);
				}
				// customer notes Description
				mCustomerReceiptsNotes.setText(receiptdetail.CustomerNotes);
				// Store notes
				mStoreReceiptsNotes.setText(receiptdetail.StoreNotes);
			}
		});

		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mViewReceiptsContainer.getVisibility() == View.VISIBLE){
					mReceiptsContainer.setVisibility(View.VISIBLE);
					mViewReceiptsContainer.setVisibility(View.GONE);
					mReceiptsFooterContainer.setVisibility(View.GONE);	
				}else{
					finish();
				}
			}
		});

		// To close receipts details view and open notes description
		mReceiptsViewNotesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mReceiptsDetailsLayout.setVisibility(View.GONE);
				mReceiptsNotesLayout.setVisibility(View.VISIBLE);

			}
		});

		// To close receipts notes details view and open amount details
		mReceiptsNotesCloseImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mReceiptsDetailsLayout.setVisibility(View.VISIBLE);
				mReceiptsNotesLayout.setVisibility(View.GONE);
			}
		});   



	}

	public class ReceiptsListScrollListener implements OnScrollListener{

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			//Check the bottom item is visible
			int lastInScreen = firstVisibleItem + visibleItemCount;	
			if(Integer.parseInt(mReceiptTotalCount) > lastInScreen && Integer.parseInt(mReceiptTotalCount)>20){
				if(lastInScreen == totalItemCount && !mIsLoadMore){												
					if(mReceiptsList.getFooterViewsCount() == 0){
						mReceiptsList.addFooterView(mFooterLayout);
					}
					if(Integer.parseInt(mReceiptTotalCount) > Integer.parseInt(mReceiptEndLimit)){
						mFooterText.setText("Loading "+mReceiptEndLimit+" of "+"("+mReceiptTotalCount+")");
					}else{
						mFooterText.setText("Loading "+mReceiptTotalCount);
					}
					if(getIntent().hasExtra("FromStoreOwner_TransactionHistory")){
						SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
						String mStoreLocation_id = mPrefs.getString("location_id", "");
						GetReceiptsDetailsTask mReceiptsTask  = new GetReceiptsDetailsTask(Receipts.this, mReceiptsList,"NOPROGRESS");
						mReceiptsTask.execute("RefreshAdapter",StoreOwner_RightMenu.mCustomer_id,mStoreLocation_id);
					}else{
						GetReceiptsDetailsTask mReceiptsTask  = new GetReceiptsDetailsTask(Receipts.this, mReceiptsList,"NOPROGRESS");
						mReceiptsTask.execute("RefreshAdapter",UserDetails.mServiceUserId,"");
					}
				}
			}else{
				if(mReceiptTotalCount.equalsIgnoreCase("0")){
				}else if(mFooterLayout != null && mReceiptsList.getFooterViewsCount() !=0 &&mReceiptsList.getAdapter() != null){
					if(lastInScreen!= totalItemCount){
						mReceiptsList.removeFooterView(mFooterLayout);	
					}else{
					}
				}
			}

		}

	}

	public void SetReceiptsArrayListAdatpter(String mCheckAndRefresh){

		if(mCheckAndRefresh.equalsIgnoreCase("RefreshAdapter") && mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}else{
			WebServiceStaticArrays.mSearchedReceiptDetails = new ArrayList<Object>(WebServiceStaticArrays.mReceiptsDetails);
			mAdapter = new CustomReceiptsAdapter(Receipts.this,WebServiceStaticArrays.mReceiptsDetails);
			mReceiptsList.setAdapter(mAdapter);
		}

		if(mFooterLayout != null && mReceiptsList.getFooterViewsCount() !=0 &&mReceiptsList.getAdapter() != null){	
			mReceiptsList.removeFooterView(mFooterLayout);
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mReceiptsNameSearch.setFocusable(true);
		mReceiptsNameSearch.setFocusableInTouchMode(true);
		if((scrollView.getLeft() != app.getLeft()) && (scrollView.getTop() != app.getTop()) ){
			scrollView.smoothScrollTo(app.getLeft(), app.getTop());
		}
	}

	/**
	 * 
	 * Helper class for opening sliding menu
	 *
	 */
	class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View leftMenu,rightMenu;
		int menuFlag;
		Button mFreezeView;
		/**
		 * Menu must NOT be out/shown to start with.
		 */

		public ClickListenerForScrolling(HorizontalScrollView scrollView, View leftmenu, View rightmenu,int menuflag,Button freezeview) {
			super();
			this.scrollView = scrollView;
			this.leftMenu = leftmenu;
			this.rightMenu = rightmenu;
			this.menuFlag=menuflag;
			this.mFreezeView = freezeview;
		}

		@Override
		public void onClick(View v) {
			
			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);
						
			Context context = leftMenu.getContext();
			int menuWidth = leftmenu.getMeasuredWidth();
			mReceiptsNameSearch.setFocusable(false);
			mReceiptsNameSearch.setFocusableInTouchMode(false);
			// Ensure menu is visible
			leftMenu.setVisibility(View.VISIBLE);

			InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


			if (!MenuOutClass.RECEIPTS_MENUOUT) {
				if(menuFlag==1){
					// Scroll to open left menu
					int left = 0;
					scrollView.smoothScrollTo(left, 0);
					mFreezeView.setVisibility(View.VISIBLE);
				}else if(menuFlag==2){
					//scroll to open right menu
					int right = menuWidth+menuWidth;
					scrollView.smoothScrollTo(right, 0);
					mFreezeView.setVisibility(View.VISIBLE);
				}
			} else {
				// Scroll to menuWidth so menu isn't on screen.
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);
				mFreezeView.setVisibility(View.GONE);
				mReceiptsNameSearch.setFocusable(true);
				mReceiptsNameSearch.setFocusableInTouchMode(true);
				mReceiptsNameSearch.requestFocus();
			}
			MenuOutClass.RECEIPTS_MENUOUT = !MenuOutClass.RECEIPTS_MENUOUT;
		}
	}

	class SizeCallbackForMenu implements SizeCallback {
		int btnWidth;
		View btnSlide;

		public SizeCallbackForMenu(View btnSlide) {
			super();
			this.btnSlide = btnSlide;
		}

		@Override
		public void onGlobalLayout() {
			btnWidth = (int) (btnSlide.getMeasuredWidth()-10);
			System.out.println("btnWidth=" + btnWidth); //$NON-NLS-1$
		}

		@Override
		public void getViewSize(int idx, int w, int h, int[] dims) {
			dims[0] = w;
			dims[1] = h;
			final int menuIdx = 0;
			if (idx == menuIdx) {
				dims[0] = w - btnWidth;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		scrollView = null; leftmenu = null; rightmenu = null; leftMenuScrollView = null;
		mReceiptsHome= null;mReceiptsZpay= null;mReceiptsInvoiceCenter= null;mReceiptsGiftcards= null;mReceiptsManageCards= null;mReceiptsReceipts= null;mReceiptsMyFavourites= null;mReceiptsMyFriends= null;mReceiptsChat= null;mReceiptsRewards= null;mReceiptsSettings= null;
		mReceiptsLogout= null;mReceiptsLoginLayout= null;mSendGiftCards= null;mInviteFriend = null;
		mReceiptsHomeText= null;mReceiptsZpayText= null;mReceiptsInvoiceCenterText= null;mReceiptsGiftCardsText= null;mReceiptsManageCardsText= null;mReceiptsReceiptsText= null;mReceiptsMyFavoritesText= null;mReceiptsMyFriendsText= null;mReceiptsChatText= null;mReceiptsRewardsText= null;
		mReceiptsSettingsText= null;mReceiptsLogoutText= null;mSendGiftCardText= null;mInviteGiftCardText= null;
		// To notify system that its time to run garbage collector service
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
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(Receipts.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	protected void onResume() {
		super.onResume();
		POJOMainMenuActivityTAG.TAG = TAG;
		// To listen for broadcast receiver to receive notification message
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(Receipts.this,ZouponsConstants.sCustomerModuleFlag);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(Receipts.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(Receipts.this);
		mLogoutSession.setLogoutTimerAlarm();
		
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
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
	public void onBackPressed() {
	}
	
	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(Receipts.this);
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