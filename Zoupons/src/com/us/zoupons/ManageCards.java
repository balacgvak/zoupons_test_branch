/**
 * 
 */
package com.us.zoupons;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.Toast;

import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.CardId_ClassVariable;
import com.us.zoupons.ClassVariables.LoginUser_ClassVariables;
import com.us.zoupons.ClassVariables.ManageCardAddPin_ClassVariables;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.android.AsyncThreadClasses.CardOnFilesAsynchThread;
import com.us.zoupons.android.AsyncThreadClasses.EditPinAsynchThread;
import com.us.zoupons.android.AsyncThreadClasses.RemoveCardAsynchThread;
import com.us.zoupons.notification.CustomNotificationAdapter;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotificationItemClickListener;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;

/**
 * Class to show,edit and remove credit cards and we can add credit card and we can edit our zoupons pin also.
 */
public class ManageCards extends Activity{
	
	public static MyHorizontalScrollView scrollView;
    public static View leftMenu;
    View app;
    LinearLayout btnSlide;
    public Typeface mZouponsFont;
	public static String TAG="ManageCards";
	public static LinearLayout mManageCardsHome,mManageCardsZpay,mManageCardsInvoiceCenter,mManageCards,mManageCardsGiftcards,mManageCardsReceipts,mManageCardsMyFavourites,mManageCardsMyFriends,mManageCardsChat,mManageCardsRewards,mManageCardsSettings,mManageCardsLogout;
    public static TextView mManageCardsHomeText,mManageCardsZpayText,mManageCardsInvoiceCenterText,mManageCardsText,mManageCardsGiftCardsText,mManageCardsReceiptsText,mManageCardsMyFavoritesText,mManageCardsMyFriendsText,mManageCardsChatText,mManageCardsRewardsText,mManageCardsSettingsText,mManageCardsLogoutText;
    
    public static LinearLayout mManageCards_CardListContainer,mManageCards_LoginCredentialsContainer,mManageCards_EditPinContainer;
    public LinearLayout mManageCards_MenuBarEditPin,mManageCards_MenuBarAddPin,mManageCards_Invisible1,mManageCards_Invisible2,mCustomerManageCardsHeader;
    
    public TextView mManageCards_ManageCardsText;
    public ListView mManageCards_CardsListView;
    
    public TextView mManageCards_Login_HeaderText,mManageCards_Login_UserNameHeader,mManageCards_Login_PasswordHeader;
    public TextView mManageCards_Login_UserNameValue;
    public static EditText mManageCards_Login_PasswordValue;
    public Button mManageCards_Login_Cancel,mManageCards_Login_Ok;
    
    public TextView mManageCards_EditPin_ExistingPinHeader,mManageCards_EditPin_EnterPinHeader,mManageCards_EditPin_ReEnterPinHeader;
    public EditText mManageCards_EditPin_ExistingPinValue;
    public EditText mManageCards_EditPin_EnterPinValue,mManageCards_EditPin_ReEnterPinValue;
    public Button mManageCards_EditPin_Cancel,mManageCards_EditPin_Ok;
    public RelativeLayout btnLogout; 
    public ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
    public Button mNotificationCount;
    //Notification Pop up layout declaration
    private View mPopUpLayout;
    private ListView mNotificationList;
    private ScheduleNotificationSync mNotificationSync;
            
    public static Button mManageCardsFreezeView;
    public int mScreenWidth;
    public double mMenuWidth;
    public NetworkCheck connectionAvailabilityChecking;
	public ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private ProgressDialog mProgressDialog=null;
	public static String mAuthenticateFlag;
	public ImageView mTabBarLoginChoice;
	/**
	 * For store owner Billings header variables declaration
	 */
	Header header;
	StoreOwner_LeftMenu storeowner_leftmenu;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mLeftMenu,mRightMenu;
	int mMenuFlag;
	ImageView mRightMenuHolder;
	TextView mStoreOwnerStoreNameText;
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	public static ScrollView leftMenuScrollView;
	
	int mEditPinCancelFlag=0;
	public static final String PREFENCES_FILE = "UserNamePrefences";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		
		ManageCardAddPin_ClassVariables.mAddPinFlag="false";
		ManageCardAddPin_ClassVariables.mEditCardFlag="false";
		
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		connectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice = new ZouponsWebService(ManageCards.this);
		parsingclass = new ZouponsParsingClass(getApplicationContext());
		
		mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(100);
        
		//code for hiding the keyboard while the activity is loading.
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		app = inflater.inflate(R.layout.managecards, null);
		ViewGroup mTitleBar = (ViewGroup) app.findViewById(R.id.storeownerbilling_storename_header);
		ViewGroup managecardscontainer = (ViewGroup) app.findViewById(R.id.managecards_container);
		ViewGroup managecardsmenubarcontainer = (ViewGroup) app.findViewById(R.id.managecards_menubarcontainer);
		mCustomerManageCardsHeader  =(LinearLayout) app.findViewById(R.id.customer_header);	
        mManageCards_CardListContainer=(LinearLayout) managecardscontainer.findViewById(R.id.managecards_listviewholder);
        mManageCards_LoginCredentialsContainer=(LinearLayout) managecardscontainer.findViewById(R.id.manageCards_editpin_loginholder);
        mManageCards_EditPinContainer=(LinearLayout) managecardscontainer.findViewById(R.id.manageCards_editpinholder);
        
        mManageCards_MenuBarEditPin=(LinearLayout) managecardsmenubarcontainer.findViewById(R.id.menubar_editpin);
        mManageCards_MenuBarEditPin.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
        mManageCards_Invisible1=(LinearLayout) managecardsmenubarcontainer.findViewById(R.id.menubar_invisible1);
        mManageCards_Invisible1.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
        mManageCards_Invisible2=(LinearLayout) managecardsmenubarcontainer.findViewById(R.id.menubar_invisible2);
        mManageCards_Invisible2.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
        mManageCards_MenuBarAddPin=(LinearLayout) managecardsmenubarcontainer.findViewById(R.id.menubar_addcards);
        mManageCards_MenuBarAddPin.setLayoutParams(new LinearLayout.LayoutParams((int)mMenuWidth,LayoutParams.FILL_PARENT,1f));
        
        mManageCards_ManageCardsText=(TextView) managecardscontainer.findViewById(R.id.managecreditcards);
        mManageCards_ManageCardsText.setTypeface(mZouponsFont);
        mManageCards_CardsListView=(ListView) managecardscontainer.findViewById(R.id.cardListItem);
        
        mManageCards_Login_HeaderText=(TextView) managecardscontainer.findViewById(R.id.managecards_login_headerText);
        mManageCards_Login_UserNameHeader=(TextView) managecardscontainer.findViewById(R.id.managecards_login_usernameheader);
        mManageCards_Login_UserNameHeader.setTypeface(mZouponsFont);
        mManageCards_Login_PasswordHeader=(TextView) managecardscontainer.findViewById(R.id.managecards_login_passwordheader);
        mManageCards_Login_PasswordHeader.setTypeface(mZouponsFont);
        mManageCards_Login_UserNameValue=(TextView) managecardscontainer.findViewById(R.id.managecards_login_username);
        SharedPreferences mPrefs = getSharedPreferences(PREFENCES_FILE, MODE_PRIVATE);
        if(!mPrefs.getString("username", "").equals("")) {
        	mManageCards_Login_UserNameValue.setText(mPrefs.getString("username", "").trim());
		}else{
			mManageCards_Login_UserNameValue.setText(UserDetails.mServiceEmail);
		}
        mManageCards_Login_PasswordValue=(EditText) managecardscontainer.findViewById(R.id.managecards_login_password);
        mManageCards_Login_Cancel=(Button) managecardscontainer.findViewById(R.id.managecards_login_cancel);
        mManageCards_Login_Ok=(Button) managecardscontainer.findViewById(R.id.managecards_login_ok);
        
        mManageCards_EditPin_ExistingPinHeader=(TextView) managecardscontainer.findViewById(R.id.managecards_editpin_existingpinheader);
        mManageCards_EditPin_ExistingPinHeader.setTypeface(mZouponsFont);
        mManageCards_EditPin_EnterPinHeader=(TextView) managecardscontainer.findViewById(R.id.managecards_editpin_enterpinheader);
        mManageCards_EditPin_EnterPinHeader.setTypeface(mZouponsFont);
        mManageCards_EditPin_ReEnterPinHeader=(TextView) managecardscontainer.findViewById(R.id.managecards_editpin_reenterpinheader);
        mManageCards_EditPin_ReEnterPinHeader.setTypeface(mZouponsFont);
        mManageCards_EditPin_ExistingPinValue=(EditText) managecardscontainer.findViewById(R.id.managecards_editpin_existingpinvalue);
        mManageCards_EditPin_ExistingPinValue.setText(UserDetails.mServicePin);
        mManageCards_EditPin_EnterPinValue=(EditText) managecardscontainer.findViewById(R.id.managecards_editpin_enterpinvalue);
        mManageCards_EditPin_ReEnterPinValue=(EditText) managecardscontainer.findViewById(R.id.managecards_editpin_reenterpinvalue);
        mManageCards_EditPin_Cancel=(Button) managecardscontainer.findViewById(R.id.managecards_editpin_cancel);
        mManageCards_EditPin_Ok=(Button) managecardscontainer.findViewById(R.id.managecards_editpin_ok);
        
        mManageCardsFreezeView = (Button) app.findViewById(R.id.managecards_freezeview); 
        
        if(getIntent().hasExtra("FromStoreOwnerBilling")){ // for store_owner point of sale
        	storeowner_leftmenu = new StoreOwner_LeftMenu(ManageCards.this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mManageCardsFreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();
			storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			storeowner_rightmenu = new StoreOwner_RightMenu(ManageCards.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mManageCardsFreezeView, TAG);
		    mRightMenu = storeowner_rightmenu.intializeInflater();
		    storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeowner_billings_header);
			header.setVisibility(View.VISIBLE);
			mCustomerManageCardsHeader.setVisibility(View.GONE);
			mTitleBar.setVisibility(View.VISIBLE);
			header.intializeInflater(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mManageCardsFreezeView, TAG);
			final View[] children = new View[] { mLeftMenu, app,mRightMenu};
			/* Scroll to app (view[1]) when layout finished.*/
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
			mManageCardsFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mManageCardsFreezeView, TAG));
			mRightMenuHolder = (ImageView) mTitleBar.findViewById(R.id.storeownerbilling_rightmenu);
		    mStoreOwnerStoreNameText = (TextView) mTitleBar.findViewById(R.id.storeownerbilling_title_textId);
		    mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mManageCardsFreezeView, TAG));
		    SharedPreferences mStoreDetailsPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
			String mStoreName = mStoreDetailsPrefs.getString("store_name", "");
			mStoreOwnerStoreNameText.setText(mStoreName);
			findViewById(R.id.creditcards_activationInfo).setVisibility(View.VISIBLE);
		}else{
			mTitleBar.setVisibility(View.GONE);
			leftMenu = inflater.inflate(R.layout.horz_scroll_menu, null);
			ViewGroup leftMenuItems = (ViewGroup) leftMenu.findViewById(R.id.menuitems);
			final ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.managecards_tabBar);
			leftMenuScrollView = (ScrollView) leftMenu.findViewById(R.id.leftmenu_scrollview);
			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);
			// Notification and log out variables
			btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
			mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
			mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
			btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(ManageCards.this));//ClickListener for Header Home image
			mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
			mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);   
			
			mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
			new LoginChoiceTabBarImage(ManageCards.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
			
			mManageCardsHome = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_home);
	        mManageCardsZpay = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zpay);
	        mManageCardsInvoiceCenter = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_invoicecenter);
	        mManageCards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_managecards);
	        mManageCardsGiftcards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zgiftcards);
	        mManageCardsReceipts = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_receipts);
	        mManageCardsMyFavourites = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfavourites);
	        mManageCardsMyFriends = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfriends);
	        mManageCardsChat = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_customercenter);
	        mManageCardsRewards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_rewards);
	        mManageCardsSettings = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_settings);
	        mManageCardsLogout = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_logout);
			
			mManageCardsHomeText = (TextView) leftMenuItems.findViewById(R.id.menuHome);
	        mManageCardsHomeText.setTypeface(mZouponsFont);
	        mManageCardsZpayText = (TextView) leftMenuItems.findViewById(R.id.menuZpay);
	        mManageCardsZpayText.setTypeface(mZouponsFont);
	        mManageCardsInvoiceCenterText = (TextView) leftMenuItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
	        mManageCardsInvoiceCenterText.setTypeface(mZouponsFont);
	        mManageCardsText = (TextView) leftMenuItems.findViewById(R.id.menuManageCards);
	        mManageCardsText.setTypeface(mZouponsFont);
	        mManageCardsGiftCardsText = (TextView) leftMenuItems.findViewById(R.id.menuGiftCards);
	        mManageCardsGiftCardsText.setTypeface(mZouponsFont);
	        mManageCardsReceiptsText = (TextView) leftMenuItems.findViewById(R.id.menuReceipts);
	        mManageCardsReceiptsText.setTypeface(mZouponsFont);
	        mManageCardsMyFavoritesText = (TextView) leftMenuItems.findViewById(R.id.menufavorites);
	        mManageCardsMyFavoritesText.setTypeface(mZouponsFont);
	        mManageCardsMyFriendsText = (TextView) leftMenuItems.findViewById(R.id.menuMyFriends);
	        mManageCardsMyFriendsText.setTypeface(mZouponsFont);
	        mManageCardsChatText = (TextView) leftMenuItems.findViewById(R.id.menuCustomerCenter);
	        mManageCardsChatText.setTypeface(mZouponsFont);
	        mManageCardsRewardsText = (TextView) leftMenuItems.findViewById(R.id.menuRewards);
	        mManageCardsRewardsText.setTypeface(mZouponsFont);
	        mManageCardsSettingsText = (TextView) leftMenuItems.findViewById(R.id.menuSettings);
	        mManageCardsSettingsText.setTypeface(mZouponsFont);
	        mManageCardsLogoutText = (TextView) leftMenuItems.findViewById(R.id.menuLogout);
	        mManageCardsLogoutText.setTypeface(mZouponsFont);
	        btnSlide = (LinearLayout) tabBar.findViewById(R.id.BtnSlide);
	        btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu,mManageCardsFreezeView));
	        mManageCardsFreezeView.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, mManageCardsFreezeView));
	        mManageCards.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, mManageCardsFreezeView));
			
	        mManageCardsHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
	        mManageCardsZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
	        mManageCardsInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
	        mManageCardsGiftcards.setOnClickListener(new MenuItemClickListener(leftMenuItems,ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
	        mManageCardsReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
	        mManageCardsMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
	        mManageCardsMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
	        mManageCardsChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
	        mManageCardsRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
	        mManageCardsSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
	        mManageCardsLogout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					v.setBackgroundResource(R.drawable.gradient_bg_hover);
					
					if(NormalPaymentAsyncTask.mCountDownTimer!=null){
						NormalPaymentAsyncTask.mCountDownTimer.cancel();
						NormalPaymentAsyncTask.mCountDownTimer = null;
						Log.i(TAG,"Timer Stopped Successfully");
					}
					
					// AsyncTask to call the logout webservice to end the login session
	        		new LogoutSessionTask(ManageCards.this).execute();
					Intent intent_logout = new Intent(ManageCards.this,ZouponsLogin.class);
					intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent_logout);
					finish();
				}
			});
	        
	        final View[] children = new View[] { leftMenu, app };
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

	        // Scroll to app (view[1]) when layout finished.
	        int scrollToViewIdx = 1;
	        scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
	        
	        mNotificationImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// Inflating custom Layout for PopUpWindow.
					mCalloutTriangleImage.setVisibility(View.VISIBLE);
					// Initializing PopUpWindow
					int popupheight = getWindowManager().getDefaultDisplay().getHeight()/2;
					final PopupWindow mPopUpWindow = new PopupWindow(mPopUpLayout,android.view.WindowManager.LayoutParams.WRAP_CONTENT,popupheight,true);
	     			mPopUpWindow.setWidth(tabBar.getWidth()-20);
					mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
					mPopUpWindow.setOutsideTouchable(false);
					mPopUpWindow.showAsDropDown(tabBar, 0, 20);
					mNotificationList.setAdapter(new CustomNotificationAdapter(ManageCards.this));
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
	        		new LogoutSessionTask(ManageCards.this).execute();
					Intent intent_logout = new Intent(ManageCards.this,ZouponsLogin.class);
					intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					finish();
					startActivity(intent_logout);
				}
			});
		}
        
        //Call function to get width of the screen
        mScreenWidth=getScreenWidth(); 	
        if(mScreenWidth>0){	//To fix Home Page menubar items width
        	mMenuWidth=mScreenWidth/4;
        }
        
        if(getIntent().hasExtra("FromStoreOwnerBilling")){  
        	CardOnFilesAsynchThread cardonfilesasynchthread =new CardOnFilesAsynchThread(ManageCards.this,mManageCards_CardsListView,mManageCards_MenuBarEditPin,"store_creditcards");
            cardonfilesasynchthread.execute("","","");
        }else{
        	CardOnFilesAsynchThread cardonfilesasynchthread =new CardOnFilesAsynchThread(ManageCards.this,mManageCards_CardsListView,mManageCards_MenuBarEditPin,"customer_creditcards");
            cardonfilesasynchthread.execute("","","");	
        }
        
        mManageCards_EditPin_EnterPinValue.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(mManageCards_EditPin_EnterPinValue.getText().toString().trim().length()<4&&mEditPinCancelFlag>0){
						alertBox_validation("PIN Number should contain 4 numbers");
					}
				}
			}
		});
        
        mManageCards_MenuBarEditPin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mEditPinCancelFlag=1;
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
				
				mAuthenticateFlag="editpin";
				mManageCards_Login_PasswordValue.setText("");
				mManageCards_CardListContainer.setVisibility(View.GONE);
				mManageCards_LoginCredentialsContainer.setVisibility(View.VISIBLE);
			}
		});
        
        mManageCards_MenuBarAddPin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mEditPinCancelFlag=2;
				mManageCards_MenuBarEditPin.setBackgroundResource(R.drawable.header_2);
				
				ManageCardAddPin_ClassVariables.mAddPinFlag="true";
				ManageCardAddPin_ClassVariables.mEditCardFlag="false";
				Intent intent_addcard = new Intent(ManageCards.this,AddCardInformation.class);
				if(getIntent().hasExtra("FromStoreOwnerBilling")){
					intent_addcard.putExtra("class_name", "StoreOwner_Billing");	
				}else{
					intent_addcard.putExtra("class_name", TAG);	
				}
				startActivity(intent_addcard);
			}
		});
        
        mManageCards_Login_Cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mEditPinCancelFlag=0;
				mManageCards_MenuBarEditPin.setBackgroundResource(R.drawable.header_2);
				if(mManageCards_Login_PasswordValue.getText().toString().trim().length()>0){
					mManageCards_Login_PasswordValue.getText().clear();
				}
				mManageCards_CardListContainer.setVisibility(View.VISIBLE);
				mManageCards_LoginCredentialsContainer.setVisibility(View.GONE);
				mManageCards_EditPin_EnterPinValue.getText().clear();
				mManageCards_EditPin_ReEnterPinValue.getText().clear();
			}
		});
        
        mManageCards_Login_Ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginValidation();
			}
		});

        mManageCards_EditPin_Cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mEditPinCancelFlag=0;
				if(mManageCards_EditPin_EnterPinValue.getText().toString().trim().length()>0){
					mManageCards_EditPin_EnterPinValue.getText().clear();
				}if(mManageCards_EditPin_ReEnterPinValue.getText().toString().trim().length()>0){
					mManageCards_EditPin_ReEnterPinValue.getText().clear();
				}
				
				mManageCards_MenuBarEditPin.setBackgroundResource(R.drawable.header_2);
				mManageCards_CardListContainer.setVisibility(View.VISIBLE);
				mManageCards_LoginCredentialsContainer.setVisibility(View.GONE);
				mManageCards_EditPinContainer.setVisibility(View.GONE);
			}
		});
        
        mManageCards_EditPin_Ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mManageCards_CardListContainer.setVisibility(View.GONE);
				mManageCards_LoginCredentialsContainer.setVisibility(View.GONE);
				mManageCards_EditPinContainer.setVisibility(View.VISIBLE);
				if(mManageCards_EditPin_EnterPinValue.getText().toString().trim().equals("")){
					alertBox_validation("Enter Pin Number");
					mManageCards_EditPin_EnterPinValue.requestFocus();
				}else if(mManageCards_EditPin_ReEnterPinValue.getText().toString().trim().equals("")){
					alertBox_validation("ReEnter Pin Number");
					mManageCards_EditPin_ReEnterPinValue.requestFocus();
				}else if(!mManageCards_EditPin_EnterPinValue.getText().toString().trim().contentEquals(mManageCards_EditPin_ReEnterPinValue.getText().toString().trim())){
					alertBox_validation("Enter the Correct Pin to confirm");
					mManageCards_EditPin_ReEnterPinValue.setText("");
					mManageCards_EditPin_ReEnterPinValue.requestFocus();
				}else{
					new EditPinAsynchThread(ManageCards.this,mManageCards_EditPin_EnterPinValue,mManageCards_EditPin_ReEnterPinValue,mManageCards_MenuBarEditPin).execute(mManageCards_EditPin_ReEnterPinValue.getText().toString().trim(),"","");
				}
			}
		});
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
		new RefreshZoupons().isApplicationGoneBackground(ManageCards.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}


	@Override
	protected void onResume() {
		 registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		 // To start notification sync
		  mNotificationSync = new ScheduleNotificationSync(ManageCards.this);
		  mNotificationSync.setRecurringAlarm();
		  new CheckUserSession(ManageCards.this).checkIfSessionExpires();
		
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(ManageCards.this);
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
	public void onBackPressed() {
		//super.onBackPressed();
	}

	/**
     * Helper for examples with a HSV that should be scrolled by a menu View's width.
     */
    static class ClickListenerForScrolling implements OnClickListener {
        HorizontalScrollView scrollView;
        View leftmenu;
        Button freezeView;
        /**
         * Menu must NOT be out/shown to start with.
         */

        public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu,Button freezeview) {
            super();
            this.scrollView = scrollView;
            this.leftmenu = menu;
            this.freezeView=freezeview;
        }

        @Override
        public void onClick(View v) {
        	
        	leftMenuScrollView.fullScroll(View.FOCUS_UP);
    		leftMenuScrollView.pageScroll(View.FOCUS_UP);
    		
            Context context = leftmenu.getContext();
            String msg = "Slide " + new Date();
            System.out.println(msg);

            int menuWidth = leftmenu.getMeasuredWidth();
            Log.i(TAG,"Menu Width : "+ menuWidth);

            // Ensure menu is visible
            leftmenu.setVisibility(View.VISIBLE);

            InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
			
            if (!MenuOutClass.MANAGECARDS_MENUOUT) {
                // Scroll to 0 to reveal menu
            	int left = 0;
                scrollView.smoothScrollTo(left, 0);
                freezeView.setVisibility(View.VISIBLE);
            } else {
                // Scroll to menuWidth so menu isn't on screen.
            	int left = menuWidth;
                scrollView.smoothScrollTo(left, 0);
                freezeView.setVisibility(View.GONE);
            }
            MenuOutClass.MANAGECARDS_MENUOUT = !MenuOutClass.MANAGECARDS_MENUOUT;
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
    
    public void loginValidation(){
    	if(connectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
    		if(mManageCards_Login_PasswordValue.getText().toString().trim().equals("")){
				alertBox_validation("Enter Password");
				mManageCards_Login_PasswordValue.requestFocus();
			}else{
				mProgressDialog.show();
				final Thread syncThread = new Thread(new Runnable() {

					@Override
					public void run() {
						Bundle bundle = new Bundle();
						String mGetResponse=null;
						Message msg_response = new Message();
						
						try{
							if(connectionAvailabilityChecking.ConnectivityCheck(getApplicationContext())){
								
								mGetResponse=zouponswebservice.login(mManageCards_Login_UserNameValue.getText().toString().trim(), new EncryptionClass().md5(mManageCards_Login_PasswordValue.getText().toString()));	//Get login response values from webservice

								if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
									String mParsingResponse=parsingclass.parseLoginXmlData(mGetResponse,TAG);
									if(mParsingResponse.equalsIgnoreCase("success")){
										for(int i=0;i<WebServiceStaticArrays.mLoginUserList.size();i++){
											LoginUser_ClassVariables parsedobjectvalues = (LoginUser_ClassVariables) WebServiceStaticArrays.mLoginUserList.get(i);
											
											
											if(!parsedobjectvalues.message.equalsIgnoreCase("Your account still not activated")){
												if(parsedobjectvalues.mFlag.equals("no")){
													updateHandler(bundle, msg_response, "Invalid User");	//send update to handler
												}else if(parsedobjectvalues.mFlag.equals("yes")){
													updateHandler(bundle, msg_response, "Valid User");
												}
											}else{
												updateHandler(bundle, msg_response, "InActive User");
											}
										}
									}else if(mParsingResponse.equalsIgnoreCase("failure")){
										updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
										Log.i(TAG,"Error");
									}else if(mParsingResponse.equalsIgnoreCase("norecords")){
										updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
										Log.i(TAG,"No Records");
									}
								}else{
									updateHandler(bundle, msg_response, mGetResponse);	//send update to handler about login response
								}
							}else{
								Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
							}
						}catch(Exception e){
							Log.i(TAG,"Thread Error");
							e.printStackTrace();
						}
						mProgressDialog.setProgress(100);
						mProgressDialog.dismiss();
					}
				});syncThread.start();
			}
		}else{
			Toast.makeText(getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
		}
    }
    
	private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse){
		bundle.putString("returnResponse", mGetResponse);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}
	
	Handler handler_response = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			String key = b.getString("returnResponse");
			if(key.trim().equals("failure")){
				alertBox_validation("Unable to reach Service.");
				mManageCards_Login_PasswordValue.setText("");
			}else if(key.trim().equals("noresponse")||key.trim().equals("norecords")){
				alertBox_validation("No Data Available");
				mManageCards_MenuBarEditPin.setBackgroundResource(R.drawable.header_2);
			}else if(key.trim().equals("Invalid User")){
				alertBox_validation("Invalid User");
				mManageCards_Login_PasswordValue.setText("");
			}else if(key.trim().equals("Valid User")){
				if(mAuthenticateFlag.equals("editpin")){
					mManageCards_CardListContainer.setVisibility(View.GONE);
					mManageCards_LoginCredentialsContainer.setVisibility(View.GONE);
					mManageCards_EditPinContainer.setVisibility(View.VISIBLE);
				}else if(mAuthenticateFlag.equals("editcard")){
					Intent intent_addcard = new Intent(ManageCards.this,AddCardInformation.class);
					if(getIntent().hasExtra("FromStoreOwnerBilling")){
						intent_addcard.putExtra("class_name", "StoreOwner_Billing");	
					}else{
						intent_addcard.putExtra("class_name", TAG);	
					}
					startActivity(intent_addcard);
				}else if(mAuthenticateFlag.equals("removecard")){
					if(getIntent().hasExtra("FromStoreOwnerBilling")){   
						new RemoveCardAsynchThread(ManageCards.this,mManageCards_CardsListView,mManageCards_MenuBarEditPin,"store_creditcards").execute(CardId_ClassVariable.cardid,"","");	
					}else{
						new RemoveCardAsynchThread(ManageCards.this,mManageCards_CardsListView,mManageCards_MenuBarEditPin,"customer_creditcards").execute(CardId_ClassVariable.cardid,"","");
					}
				}
			}else {
				alertBox_validation("No Data Available");
			}
		}
	};
	
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

    private void alertBox_validation(final String msg) {
		AlertDialog.Builder validation_alert = new AlertDialog.Builder(this);
		validation_alert.setTitle("Information");
		validation_alert.setMessage(msg);
		validation_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equalsIgnoreCase("PIN Number should contain 4 numbers")){
					mManageCards_EditPin_EnterPinValue.getText().clear();
					mManageCards_EditPin_EnterPinValue.requestFocus();
				}
				dialog.dismiss();
			}
		});
		validation_alert.show();
	}
}