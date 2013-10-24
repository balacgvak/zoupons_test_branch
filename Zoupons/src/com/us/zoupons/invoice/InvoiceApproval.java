package com.us.zoupons.invoice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.R;
import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.notification.CustomNotificationAdapter;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotificationItemClickListener;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;
import com.us.zoupons.zpay.Step2_ManageCards;
import com.us.zoupons.zpay.ZpayStep2SearchEnable;

public class InvoiceApproval  extends Activity{

	public static MyHorizontalScrollView scrollView;
	public static View leftmenu,rightmenu;
	View app;
	ImageView btnSlide;
	public static Button InvoiceFreezeButton;
	public Typeface mZouponsFont;
	public static LinearLayout mInvoiceHome,mInvoiceZpay,mInvoiceInvoiceCenter,mInvoiceGiftcards,mInvoiceManageCards,mInvoiceReceipts,mInvoiceMyFavourites,mInvoiceMyFriends,mInvoiceChat,mInvoiceRewards,mInvoiceSettings,
	mInvoiceLogout;
	public static TextView mInvoiceHomeText,mInvoiceZpayText,mInvoiceCenerText,mInvoiceGiftCardsText,mInvoiceManageCardsText,mInvoiceReceiptsText,mInvoiceMyFavoritesText,mInvoiceMyFriendsText,mInvoiceChatText,mInvoiceRewardsText,
	mInvoiceSettingsText,mInvoiceLogoutText;
	public RelativeLayout btnLogout; 
	public ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	public Button mNotificationCount;
	int btnWidth;
	public int mScreenWidth;
	public double mInvoiceMenuWidth;
	public static String TAG = "Invoice";
	private ListView mInvoiceList;
	private TextView mBackButton,mInvoiceDate,mInvoiceStoreName,mInvoiceAmount;
	private EditText mInvoiceDescription;
	private Button mInvoiceApprove,mInvoiceReject;
	private ScrollView mViewInvoiceContainer;
	public static String mInvoiceListMessage="";
	public static String mRejectInvoiceMessage="";
	private String mInvoiceId="",mStoreId="",mLocationId="",mStoreName="",mLogoPath="",mRaisedBy="",mRaisedByType="",mAmount="",mGiftCardId="";
	private String mFaceValue="",mTipAmount="",mCreatedDate="",mNote="",mDescription="";
	//Notification Pop up layout declaration
	private View mPopUpLayout;
	private ListView mNotificationList;
	private ScheduleNotificationSync mNotificationSync;

	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	public ImageView mTabBarLoginChoice;
	public static ScrollView leftMenuScrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);

		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");

		leftmenu = inflater.inflate(R.layout.horz_scroll_menu, null);
		rightmenu = inflater.inflate(R.layout.rightmenu, null);
		app = inflater.inflate(R.layout.invoice_approval, null);

		final ViewGroup mInvoiceHeader = (ViewGroup) app.findViewById(R.id.approve_invoice_tabbar);
		final ViewGroup mInvoiceContainer = (ViewGroup) app.findViewById(R.id.approve_invoice_container); 
		ViewGroup mInvoicefooterContainer = (ViewGroup) app.findViewById(R.id.approve_invoice_footerLayoutId);
		mViewInvoiceContainer =  (ScrollView) app.findViewById(R.id.view_approve_invoice_container);
		mInvoiceDate = (TextView) mViewInvoiceContainer.findViewById(R.id.invoice_dateId);
		mInvoiceStoreName = (TextView) mViewInvoiceContainer.findViewById(R.id.invoice_storenameId);
		mInvoiceAmount = (TextView) mViewInvoiceContainer.findViewById(R.id.invoice_amountId);
		mInvoiceDescription =  (EditText) mViewInvoiceContainer.findViewById(R.id.invoice_descriptionId);
		mInvoiceReject = (Button) mViewInvoiceContainer.findViewById(R.id.reject_invoice_buttonId);
		mInvoiceApprove = (Button) mViewInvoiceContainer.findViewById(R.id.approve_invoice_buttonId);
		btnSlide = (ImageView)mInvoiceHeader.findViewById(R.id.BtnSlide_approve_invoice);
		btnLogout = (RelativeLayout) mInvoiceHeader.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(InvoiceApproval.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(InvoiceApproval.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Notitification pop up layout declaration
		LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopUpLayout = mInflater.inflate(R.layout.notification, (ViewGroup) findViewById(R.id.mPopUpParentLayout));
		mNotificationList = (ListView) mPopUpLayout.findViewById(R.id.notification_list);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);

		int popupLeftPadding = 0;
		if(getWindowManager().getDefaultDisplay().getWidth()<1000){
			popupLeftPadding = 30;
		}else{
			popupLeftPadding = 400;
		}
		mLayoutParams.leftMargin = popupLeftPadding;
		mNotificationList.setLayoutParams(mLayoutParams);

		final View[] children = new View[] { leftmenu, app };
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx,new SizeCallbackForMenu(btnSlide));
		//Call function to get width of the screen
		mScreenWidth=getScreenWidth(); 	
		if(mScreenWidth>0){	//To fix Home Page menubar items width
			mInvoiceMenuWidth=mScreenWidth/4;
			Log.i(TAG,"ScreenWidth : "+mScreenWidth+"\n"+"MenuItemWidth : "+mInvoiceMenuWidth);
		}
		mInvoiceList = (ListView) mInvoiceContainer.findViewById(R.id.approve_invoice_listId);
		mBackButton = (TextView) mInvoicefooterContainer.findViewById(R.id.approve_invoice_leftFooterText);
		InvoiceFreezeButton = (Button) app.findViewById(R.id.approve_invoice_freeze);
		ViewGroup menuLeftItems = (ViewGroup) leftmenu.findViewById(R.id.menuitems);

		leftMenuScrollView = (ScrollView) leftmenu.findViewById(R.id.leftmenu_scrollview);
		leftMenuScrollView.fullScroll(View.FOCUS_UP);
		leftMenuScrollView.pageScroll(View.FOCUS_UP);
		
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
					Log.i(TAG,"Timer Stopped Successfully");
				}
				
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(InvoiceApproval.this).execute();
				Intent intent_logout = new Intent(InvoiceApproval.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_logout);
				finish();
			}
		});
		InvoiceFreezeButton.setOnClickListener(new ClickListenerForScrolling(scrollView, leftmenu, rightmenu, 0, InvoiceFreezeButton));
		btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, leftmenu, rightmenu, 1, InvoiceFreezeButton));
		GetInvoiceListTask mInvoiceTask = new GetInvoiceListTask(InvoiceApproval.this,mInvoiceList,mInvoiceContainer,mViewInvoiceContainer);
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
				mLogoPath=mInvoiceDetails.LogoPath;
				mRaisedBy=mInvoiceDetails.RaisedBy;
				mRaisedByType=mInvoiceDetails.RaisedByType;
				mAmount=mInvoiceDetails.Amount;
				mCreatedDate=mInvoiceDetails.CreatedDate;
				mNote=mInvoiceDetails.Note;
				mDescription=mInvoiceDetails.Description;
				mInvoiceStoreName.setText(": "+mInvoiceDetails.StoreName);
				mInvoiceDate.setText(": "+mInvoiceDetails.CreatedDate);
				mInvoiceAmount.setText(": $"+mInvoiceDetails.Amount);
				mInvoiceDescription.setText(mInvoiceDetails.Description);
			}
		});

		mBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mViewInvoiceContainer.getVisibility() == View.VISIBLE){
					mInvoiceContainer.setVisibility(View.VISIBLE);
					mViewInvoiceContainer.setVisibility(View.GONE);	
				}else{
					finish();
				}
			}
		});

		mInvoiceReject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				GetInvoiceListTask mInvoiceTask = new GetInvoiceListTask(InvoiceApproval.this,mInvoiceList,mInvoiceContainer,mViewInvoiceContainer);
				mInvoiceTask.execute("REJECTINVOICE",mInvoiceId);
			}
		});

		mInvoiceApprove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Move to step 2....
				ZpayStep2SearchEnable.searchEnableFlag=false;
				Intent intent_step2 = new Intent(InvoiceApproval.this,Step2_ManageCards.class);
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

		mNotificationImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Inflating custom Layout for PopUpWindow.
				mCalloutTriangleImage.setVisibility(View.VISIBLE);
				// Initializing PopUpWindow
				int popupheight = getWindowManager().getDefaultDisplay().getHeight()/2;
				final PopupWindow mPopUpWindow = new PopupWindow(mPopUpLayout,android.view.WindowManager.LayoutParams.WRAP_CONTENT,popupheight,true);
				mPopUpWindow.setWidth(mInvoiceHeader.getWidth()-20);
				mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopUpWindow.setOutsideTouchable(false);
				mPopUpWindow.showAsDropDown(mInvoiceHeader, 0, 20);
				mNotificationList.setAdapter(new CustomNotificationAdapter(InvoiceApproval.this));
				mNotificationList.setOnItemClickListener(new NotificationItemClickListener());

				mPopUpWindow.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						mCalloutTriangleImage.setVisibility(View.INVISIBLE);
					}
				});
				// To dismiss popup window when touch outside..
				final Rect listviewRect = new Rect();
				mNotificationList.getHitRect(listviewRect);
				mPopUpLayout.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (!listviewRect.contains((int)event.getX(), (int)event.getY())) {
							Log.i("touch", "dismiss pop up");
							mPopUpWindow.dismiss();	
						}else{
							Log.i("touch", "dont dismiss");
						}
						return false;
					}
				});

			}
		});

		mLogoutImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(InvoiceApproval.this).execute();
				Intent intent_logout = new Intent(InvoiceApproval.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				finish();
				startActivity(intent_logout);
			}
		});


	}

	static class ClickListenerForScrolling implements OnClickListener {
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
			Log.i(TAG,"Left Menu Width : "+ menuWidth+" "+menuFlag+" "+MenuOutClass.WHOLE_MENUOUT);
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

	static class SizeCallbackForMenu implements SizeCallback {
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
		int Measuredheight = 0;  
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Measuredwidth = display.getWidth(); 
		Measuredheight = display.getHeight();
		return Measuredwidth;
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
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
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(InvoiceApproval.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}

	@Override
	protected void onResume() {
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(InvoiceApproval.this);
		mNotificationSync.setRecurringAlarm();
		
		new CheckUserSession(InvoiceApproval.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(InvoiceApproval.this);
		mLogoutSession.setLogoutTimerAlarm();

		super.onResume();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
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
	public void onBackPressed() {}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			try{
				Log.i(TAG,"OnReceive");
				if(intent.hasExtra("FromNotification")){
					if(NotificationDetails.notificationcount>0){
						mNotificationCount.setVisibility(View.VISIBLE);
						mNotificationCount.setText(String.valueOf(NotificationDetails.notificationcount));
					}else{
						mNotificationCount.setVisibility(View.GONE);
						Log.i("Notification result", "No new notification");
					}
				}					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};	
}