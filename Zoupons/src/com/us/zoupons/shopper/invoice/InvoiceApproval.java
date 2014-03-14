package com.us.zoupons.shopper.invoice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.mobilepay.MobilePay;
import com.us.zoupons.mobilepay.NormalPaymentAsyncTask;
import com.us.zoupons.mobilepay.ZpayStep2SearchEnable;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;

/**
 * 
 * Activity to list invoice and manage the same
 *
 */

public class InvoiceApproval  extends Activity{

	// Initializing views and variables
	public static MyHorizontalScrollView sScrollView;
	public static View sLeftmenu,sRightmenu;
	private View app;
	private ImageView btnSlide;
	public static Button InvoiceFreezeButton;
	private Typeface mZouponsFont;
	public static LinearLayout mInvoiceHome,mInvoiceZpay,mInvoiceInvoiceCenter,mInvoiceGiftcards,mInvoiceManageCards,mInvoiceReceipts,mInvoiceMyFavourites,mInvoiceMyFriends,mInvoiceChat,mInvoiceRewards,mInvoiceSettings,
	mInvoiceLogout;
	public static TextView mInvoiceHomeText,mInvoiceZpayText,mInvoiceCenerText,mInvoiceGiftCardsText,mInvoiceManageCardsText,mInvoiceReceiptsText,mInvoiceMyFavoritesText,mInvoiceMyFriendsText,mInvoiceChatText,mInvoiceRewardsText,
	mInvoiceSettingsText,mInvoiceLogoutText;
	private RelativeLayout mBtnLogout; 
	private ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	private Button mNotificationCount;
	private static String TAG = "Invoice";
	private ListView mInvoiceList;
	private TextView mBackButton,mInvoiceDate,mInvoiceStoreName,mInvoiceAmount;
	private EditText mInvoiceDescription;
	private Button mInvoiceApprove,mInvoiceReject;
	private ScrollView mViewInvoiceContainer;
	public static String mInvoiceListMessage="";
	public static String mRejectInvoiceMessage="";
	private String mInvoiceId="",mStoreId="",mLocationId="",mStoreName="",mRaisedBy="",mRaisedByType="",mAmount="";
	//Notification Pop up layout declaration
	private ScheduleNotificationSync mNotificationSync;
	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	private ImageView mTabBarLoginChoice;
	public static ScrollView leftMenuScrollView;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Referencing view from layout xml file
		LayoutInflater inflater = LayoutInflater.from(this);
		sScrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(sScrollView);
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		sLeftmenu = inflater.inflate(R.layout.shopper_leftmenu, null);
		sRightmenu = inflater.inflate(R.layout.rightmenu, null);
		app = inflater.inflate(R.layout.invoice_approval, null);
		final ViewGroup mInvoiceHeader = (ViewGroup) app.findViewById(R.id.approve_invoice_tabbar);
		final ViewGroup mInvoiceContainer = (ViewGroup) app.findViewById(R.id.approve_invoice_container); 
		final ViewGroup mInvoicefooterContainer = (ViewGroup) app.findViewById(R.id.approve_invoice_footerLayoutId);
		mViewInvoiceContainer =  (ScrollView) app.findViewById(R.id.view_approve_invoice_container);
		mInvoiceDate = (TextView) mViewInvoiceContainer.findViewById(R.id.invoice_dateId);
		mInvoiceStoreName = (TextView) mViewInvoiceContainer.findViewById(R.id.invoice_storenameId);
		mInvoiceAmount = (TextView) mViewInvoiceContainer.findViewById(R.id.invoice_amountId);
		mInvoiceDescription =  (EditText) mViewInvoiceContainer.findViewById(R.id.invoice_descriptionId);
		mInvoiceReject = (Button) mViewInvoiceContainer.findViewById(R.id.reject_invoice_buttonId);
		mInvoiceApprove = (Button) mViewInvoiceContainer.findViewById(R.id.approve_invoice_buttonId);
		btnSlide = (ImageView)mInvoiceHeader.findViewById(R.id.BtnSlide_approve_invoice);
		mBtnLogout = (RelativeLayout) mInvoiceHeader.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_notificationImageId);
		mBtnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(InvoiceApproval.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) mBtnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) mBtnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(InvoiceApproval.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Notitification pop up layout declaration
		mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
		mNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mInvoiceHeader, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		mNotificationCount.setOnClickListener(new ManageNotificationWindow(this,mInvoiceHeader, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		final View[] children = new View[] { sLeftmenu, app };
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx,new SizeCallbackForMenu(btnSlide));
		mInvoiceList = (ListView) mInvoiceContainer.findViewById(R.id.approve_invoice_listId);
		mBackButton = (TextView) mInvoicefooterContainer.findViewById(R.id.approve_invoice_leftFooterText);
		InvoiceFreezeButton = (Button) app.findViewById(R.id.approve_invoice_freeze);
		ViewGroup menuLeftItems = (ViewGroup) sLeftmenu.findViewById(R.id.menuitems);

		leftMenuScrollView = (ScrollView) sLeftmenu.findViewById(R.id.leftmenu_scrollview);
		leftMenuScrollView.fullScroll(View.FOCUS_UP);
		leftMenuScrollView.pageScroll(View.FOCUS_UP);
		// Sliding left menu item initialization
		mInvoiceHome = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_home);
		mInvoiceZpay = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_zpay);
		mInvoiceInvoiceCenter = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_invoicecenter);
		mInvoiceGiftcards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_zgiftcards);
		mInvoiceManageCards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_managecards);
		mInvoiceReceipts = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_receipts);
		mInvoiceMyFavourites = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_myfavourites);
		mInvoiceMyFriends = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_myfriends);
		mInvoiceChat = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_customercenter);
		mInvoiceRewards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_rewards);
		mInvoiceSettings = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_settings);
		mInvoiceLogout = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_logout);
		mInvoiceHomeText = (TextView) menuLeftItems.findViewById(R.id.menuHome);
		mInvoiceHomeText.setTypeface(mZouponsFont);
		mInvoiceZpayText = (TextView) menuLeftItems.findViewById(R.id.menuZpay);
		mInvoiceZpayText.setTypeface(mZouponsFont);
		mInvoiceCenerText = (TextView) menuLeftItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
		mInvoiceCenerText.setTypeface(mZouponsFont);
		mInvoiceGiftCardsText = (TextView) menuLeftItems.findViewById(R.id.menuGiftCards);
		mInvoiceGiftCardsText.setTypeface(mZouponsFont);
		mInvoiceManageCardsText = (TextView) menuLeftItems.findViewById(R.id.menuManageCards);
		mInvoiceManageCardsText.setTypeface(mZouponsFont);
		mInvoiceReceiptsText = (TextView) menuLeftItems.findViewById(R.id.menuReceipts);
		mInvoiceReceiptsText.setTypeface(mZouponsFont);
		mInvoiceMyFavoritesText = (TextView) menuLeftItems.findViewById(R.id.menufavorites);
		mInvoiceMyFavoritesText.setTypeface(mZouponsFont);
		mInvoiceMyFriendsText = (TextView) menuLeftItems.findViewById(R.id.menuMyFriends);
		mInvoiceMyFriendsText.setTypeface(mZouponsFont);
		mInvoiceChatText = (TextView) menuLeftItems.findViewById(R.id.menuCustomerCenter);
		mInvoiceChatText.setTypeface(mZouponsFont);
		mInvoiceRewardsText = (TextView) menuLeftItems.findViewById(R.id.menuRewards);
		mInvoiceRewardsText.setTypeface(mZouponsFont);
		mInvoiceSettingsText = (TextView) menuLeftItems.findViewById(R.id.menuSettings);
		mInvoiceSettingsText.setTypeface(mZouponsFont);
		mInvoiceLogoutText = (TextView) menuLeftItems.findViewById(R.id.menuLogout);
		mInvoiceLogoutText.setTypeface(mZouponsFont);

		//Footer View
		if(getIntent().hasExtra("nofooterview")){
			mInvoicefooterContainer.setVisibility(View.GONE);
		}
		// Registering click listener for Sliding left menu
		mInvoiceHome.setOnClickListener(new MenuItemClickListener(menuLeftItems, InvoiceApproval.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceZpay.setOnClickListener(new MenuItemClickListener(menuLeftItems,InvoiceApproval.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceInvoiceCenter.setOnClickListener(new MenuItemClickListener(menuLeftItems,InvoiceApproval.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceGiftcards.setOnClickListener(new MenuItemClickListener(menuLeftItems, InvoiceApproval.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceManageCards.setOnClickListener(new MenuItemClickListener(menuLeftItems, InvoiceApproval.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceReceipts.setOnClickListener(new MenuItemClickListener(menuLeftItems, InvoiceApproval.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceMyFavourites.setOnClickListener(new MenuItemClickListener(menuLeftItems,InvoiceApproval.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceMyFriends.setOnClickListener(new MenuItemClickListener(menuLeftItems,InvoiceApproval.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceChat.setOnClickListener(new MenuItemClickListener(menuLeftItems,InvoiceApproval.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceRewards.setOnClickListener(new MenuItemClickListener(menuLeftItems,InvoiceApproval.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceSettings.setOnClickListener(new MenuItemClickListener(menuLeftItems,InvoiceApproval.this,POJOMainMenuActivityTAG.TAG=TAG));
		mInvoiceLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);

				if(NormalPaymentAsyncTask.mCountDownTimer!=null){
					NormalPaymentAsyncTask.mCountDownTimer.cancel();
					NormalPaymentAsyncTask.mCountDownTimer = null;
				}
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(InvoiceApproval.this,"FromManualLogOut").execute();
			}
		});

		InvoiceFreezeButton.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftmenu, sRightmenu, 0, InvoiceFreezeButton));
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftmenu, sRightmenu, 1, InvoiceFreezeButton));
		// To list Invoice details
		GetInvoiceListTask mInvoiceTask = new GetInvoiceListTask(InvoiceApproval.this,mInvoiceList,mInvoiceContainer,mViewInvoiceContainer,mNotificationCount);
		mInvoiceTask.execute("LIST");

		mInvoiceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				mInvoiceContainer.setVisibility(View.GONE);
				mViewInvoiceContainer.setVisibility(View.VISIBLE);
				POJOInvoiceList mInvoiceDetails = (POJOInvoiceList) arg0.getAdapter().getItem(arg2);
				mInvoiceId = mInvoiceDetails.InvoiceId;
				mStoreId=mInvoiceDetails.StoreId;
				mLocationId=mInvoiceDetails.LocationId;
				mStoreName=mInvoiceDetails.StoreName;
				mRaisedBy=mInvoiceDetails.RaisedBy;
				mRaisedByType=mInvoiceDetails.RaisedByType;
				mAmount=mInvoiceDetails.Amount;
				mInvoiceStoreName.setText(": "+mInvoiceDetails.StoreName);
				mInvoiceDate.setText(": "+mInvoiceDetails.CreatedDate);
				mInvoiceAmount.setText(": $"+mInvoiceDetails.Amount);
				mInvoiceDescription.setText(mInvoiceDetails.Description);
				mInvoicefooterContainer.setVisibility(View.VISIBLE);
			}
		});

		mBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mInvoiceContainer.setVisibility(View.VISIBLE);
				mViewInvoiceContainer.setVisibility(View.GONE);	
				mInvoicefooterContainer.setVisibility(View.GONE);
			}
		});

		mInvoiceReject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// To reject invoice
				AlertDialog.Builder mAlertBox = new AlertDialog.Builder(InvoiceApproval.this);
				mAlertBox.setTitle("Information");
				mAlertBox.setMessage("Sure you want to reject the invoice");
				mAlertBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						GetInvoiceListTask mInvoiceTask = new GetInvoiceListTask(InvoiceApproval.this,mInvoiceList,mInvoiceContainer,mViewInvoiceContainer,mNotificationCount);
						mInvoiceTask.execute("REJECTINVOICE",mInvoiceId);	
					}
				});
				mAlertBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				mAlertBox.show();
				
			}
		});

		mInvoiceApprove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Move to step 2....
				ZpayStep2SearchEnable.searchEnableFlag=false;
				Intent intent_step2 = new Intent(InvoiceApproval.this,MobilePay.class);
				intent_step2.putExtra("datasourcename", "invoice_approval");
				intent_step2.putExtra("invoice_source", "normal_payment");
				intent_step2.putExtra("payment_storeid", mStoreId);
				intent_step2.putExtra("payment_locationid", mLocationId);
				intent_step2.putExtra("payment_storename", mStoreName);
				intent_step2.putExtra("invoice_Id", mInvoiceId);
				intent_step2.putExtra("store_employeeId",mRaisedBy );
				intent_step2.putExtra("store_employeeType", mRaisedByType);
				intent_step2.putExtra("amount", mAmount);
				startActivity(intent_step2);
			}
		});

		mLogoutImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(InvoiceApproval.this,"FromManualLogOut").execute();
			}
		});


	}

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
			int menuWidth = sLeftmenu.getMeasuredWidth();
			// Ensure menu is visible
			leftMenu.setVisibility(View.VISIBLE);

			InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


			if (!MenuOutClass.FRIENDS_MENUOUT) {
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
			}
			MenuOutClass.FRIENDS_MENUOUT = !MenuOutClass.FRIENDS_MENUOUT;

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

	//Get Screen width
	public int getScreenWidth(){
		int Measuredwidth = 0;  
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Measuredwidth = display.getWidth(); 
		return Measuredwidth;
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sScrollView = null; sLeftmenu = null; sRightmenu = null;
		mInvoiceHome= null;mInvoiceZpay= null;mInvoiceInvoiceCenter= null;mInvoiceGiftcards= null;mInvoiceManageCards= null;mInvoiceReceipts= null;mInvoiceMyFavourites= null;mInvoiceMyFriends= null;mInvoiceChat= null;mInvoiceRewards= null;mInvoiceSettings= null;
		mInvoiceLogout = null;mInvoiceHomeText= null;mInvoiceZpayText= null;mInvoiceCenerText= null;mInvoiceGiftCardsText= null;mInvoiceManageCardsText= null;mInvoiceReceiptsText= null;mInvoiceMyFavoritesText= null;mInvoiceMyFriendsText= null;mInvoiceChatText= null;mInvoiceRewardsText= null;
		mInvoiceSettingsText= null;mInvoiceLogoutText = null;leftMenuScrollView = null;
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
		new RefreshZoupons().isApplicationGoneBackground(InvoiceApproval.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(InvoiceApproval.this,ZouponsConstants.sCustomerModuleFlag);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(InvoiceApproval.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(InvoiceApproval.this);
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


}