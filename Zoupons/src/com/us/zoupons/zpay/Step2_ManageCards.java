/**
 * 
 */
package com.us.zoupons.zpay;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.AddCardInformation;
import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.KeyboardUtil;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.Messages;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.R;
import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.CardOnFiles_ClassVariables;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.FlagClasses.AccountLoginFlag;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.GiftCards.POJOAllGiftCards;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.android.AsyncThreadClasses.CardOnFilesAsynchThread;
import com.us.zoupons.android.AsyncThreadClasses.Step2StoreGiftCardsAsynchThread;
import com.us.zoupons.android.listview.inflater.classes.CustomCardDetailsAdapter;
import com.us.zoupons.invoice.GetInvoiceListTask;
import com.us.zoupons.notification.CustomNotificationAdapter;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotificationItemClickListener;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;
import com.us.zoupons.storeowner.PointOfSale.StoreOwner_PointOfSale_Part1;

/**
 * class to initiate mobile payment
 */
public class Step2_ManageCards extends Activity implements TextWatcher{

	public static MyHorizontalScrollView scrollView;
	public static View leftMenu;
	View app;
	ImageView btnSlide,mTipContextImage,mOnlyCreditCardContextImage,mPaymentOptionsCardContextImage,mBothCardOptionContextImage,mCreditCardPaymentContextImage;
	public RelativeLayout btnLogout,mQRCodecontainer; 
	public ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage,mQRCodeImage;
	public Button mNotificationCount,mQRCodeDestructButton;
	public Typeface mZouponsFont;
	public static LinearLayout mStep2ManageCardsHome,mStep2ManageCardsZpay,mStep2ManageCardsInvoiceCenter,mStep2ManageCardsManageCards,mStep2ManageCardsGiftcards,mStep2ManageCardsReceipts,mStep2ManageCardsMyFavourites,mStep2ManageCardsMyFriends,mStep2ManageCardsChat,mStep2ManageCardsRewards,mStep2ManageCardsSettings,mStep2ManageCardsLogout;
	public static TextView mStep2ManageCardsHomeText,mStep2ManageCardsZpayText,mStep2ManageCardsInvoiceCenterText,mStep2ManageCardsManageCardsText,mStep2ManageCardsGiftCardsText,mStep2ManageCardsReceiptsText,mStep2ManageCardsMyFavoritesText,mStep2ManageCardsMyFriendsText,mStep2ManageCardsChatText,mStep2ManageCardsRewardsText,mStep2ManageCardsSettingsText,mStep2ManageCardsLogoutText;
	boolean menuOut = false;
	int btnWidth;
	public static String TAG = "Step2_ManageCards";

	public TextView mTotalAmountText,mTipAmountText,mPaymentOptionsText;
	public static TextView mBothCardOptionText;
	public TextView mBalanceOnCreditCardValue;
	public TextView mMenuBarNext,mMenuBarStep3Back;
	public View mMenuBarStep3Splitter;
	public TextView mChoosedAmountValue;
	public TextView mQRCodeExpiryText;
	public static EditText mAmountValue;
	public EditText mTipValue;
	public static EditText mOnlyCreditCardValue;
	public static EditText mPaymentOptionsCardValue;
	public static EditText mBothCardOptionValue;
	public EditText mAmountOnGiftcardValue;
	public static EditText mCreditCardPaymentValue;
	public Button mAddCardButton;
	public static Button mStep2ManageCardsFreezeView;
	private LinearLayout mChooseCardAmountDetailsLayout;
	private static LinearLayout mAddCardLayout;
	private static LinearLayout mBothCardPaymentLayout;
	private LinearLayout mAmountOnGiftcardLayout;
	private LinearLayout mBalanceOnCreditCardLayout;
	private static LinearLayout mCreditCardOnlyContainer; 

	//Data Source
	public String mStoreName="",mStoreID="",mStoreLocationId="",mGiftCardId="",mGiftCardValue="",mTipAmount="",mGiftCardPurchaseId="";
	private String mFriendName="",mFriendId="",mIsZouponsFriend="",mFriendNotes="",mDate="",mTimeZone="";
	private static String mSelectedCreditCardId="";
	private FrameLayout mTipValueContainer,mPaymentOptionsCardContainer;
	private static FrameLayout mBothCardOptionsContainer;
	private FrameLayout mCreditCardPaymentContainer;
	private ScrollView mChooseCardContainer;
	public static String mGetBundleValue,mInvoiceSourceBundle,mChoosedGiftcardBalanceValue="0",mChoosedGiftcardID="",mChoosedGiftCardPurchaseID="",mChoosedStoreName="";
	private static boolean hasGiftCardId = false;
	private int mChoosedTipAmount = 0;
	private static int choosedcardposition = 0;
	private String mFaceValue="0";

	//Step3 variables
	public ScrollView mStep3Container;
	public ColorStateList oldColors;
	public LinearLayout mStep3_AmountContainer,mStep3_TipContainer,mStep3_TotalChargeContainer,mStep3_CardUsedContainer,mStep3_TotalOnCreditCardContainer;
	public LinearLayout mStep3_GiftCardUsedContainer,mStep3_TotalOnGiftCardContainer,mStep3_AddNoteContainer,mStep3_PinContainer;
	public View mStep3_FirstBorderId,mStep3_SecondBorderId;
	public TextView mStep3_AmountValue,mStep3_TipValue,mStep3_TotalChargeValue,mStep3_CardUsedValue,mStep3_TotalOnCreditCardValue;
	public TextView mStep3_GiftCardUsedValue,mStep3_TotalOnGiftCardValue,mDollarText,mStep3_AddNote;
	public EditText mStep3_AddNoteValue,mStep3_PinValue;
	public String mStep3Notes="";
	public Button mGenerateQR;

	//for Invoice details
	private String mInvoiceId="",mInvoiceRaisedBy="",mInvoiceRaisedByType="",mInvoiceAmount="",/*mInvoiceTipAmount="",*/mmInvoiceCreatedDate="",mmInvoiceStatement="",mmInvoiceDescription="";
	private static String mZpayPaymentType="";
	private static String mInvoicePaymentType="";
	//Notification Pop up layout declaration
	private View mPopUpLayout;
	private ListView mNotificationList;
	private ScheduleNotificationSync mNotificationSync;

	private Button mReviewPayment;

	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	public ImageView mTabBarLoginChoice;
	/**
	 * For store owner header variables declaration
	 */
	Header header;
	StoreOwner_LeftMenu storeowner_leftmenu;
	View mLeftMenu;
	int mMenuFlag;

	//ImageView width and height
	int imageviewWidth=0,imageviewHeight=0;
	public static ScrollView leftMenuScrollView;
	//Step 4 progress bar
	ProgressBar mStep4Progressbar;

	//Array List for append giftcard and creditcard values
	static ArrayList<Object> mTotalCards = new ArrayList<Object>();
	int mDifferntiateAlertMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");

			LayoutInflater inflater = LayoutInflater.from(this);
			scrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(scrollView);

			app = inflater.inflate(R.layout.mobile_pay_steps, null);
			mStep2ManageCardsFreezeView=(Button) app.findViewById(R.id.mobilepay_Steps_freeze);

			ViewGroup mManageCardsStep2= (ViewGroup) app.findViewById(R.id.mobilepay_steps_container);
			final ViewGroup footerlayout = (ViewGroup) app.findViewById(R.id.mobilepay_steps_footerLayout);

			mChooseCardAmountDetailsLayout = (LinearLayout) mManageCardsStep2.findViewById(R.id.mobilepay_amountdetails);
			mAmountValue = (EditText) mChooseCardAmountDetailsLayout.findViewById(R.id.mobilepay_amount_value);
			mAmountValue.setInputType(InputType.TYPE_CLASS_NUMBER);
					
			mChoosedAmountValue = (TextView) mChooseCardAmountDetailsLayout.findViewById(R.id.mobilepay_amount_valueTextId);
			mReviewPayment = (Button) mManageCardsStep2.findViewById(R.id.mobilepay_review_paymentButton);

			// For step2 Amount Details
			mDollarText = (TextView) mChooseCardAmountDetailsLayout.findViewById(R.id.dollar_text);
			mTipValue = (EditText) mChooseCardAmountDetailsLayout.findViewById(R.id.mobilepay_tip_value);
			mTipValueContainer = (FrameLayout) mChooseCardAmountDetailsLayout.findViewById(R.id.mobilepay_tip_container);
			mTipContextImage = (ImageView) mTipValueContainer.findViewById(R.id.mobilepay_tip_contextImage);
			mTipAmountText = (TextView) mChooseCardAmountDetailsLayout.findViewById(R.id.mobilepay_tip_textId);
			mTotalAmountText = (TextView) mChooseCardAmountDetailsLayout.findViewById(R.id.mobilepay_total_value);
			// For step2 Card Details
			mChooseCardContainer = (ScrollView) mManageCardsStep2.findViewById(R.id.mobilepay_choosecard_container);
			// For Add card Layout
			mAddCardLayout = (LinearLayout) mChooseCardAmountDetailsLayout.findViewById(R.id.addcardlayout_id);
			mAddCardButton = (Button) mAddCardLayout.findViewById(R.id.mobilepay_addcard);
			// For only credit card layout
			mCreditCardOnlyContainer = (LinearLayout) mChooseCardContainer.findViewById(R.id.mobilepay_creditcard_container);
			mOnlyCreditCardValue = (EditText) mCreditCardOnlyContainer.findViewById(R.id.mobilepay_creditcard_value);
			mOnlyCreditCardContextImage = (ImageView) mCreditCardOnlyContainer.findViewById(R.id.mobilepay_creditcard_contextImage);

			// For Both Card Payment options
			mBothCardPaymentLayout = (LinearLayout) mChooseCardContainer.findViewById(R.id.mobilepay_bothcard_payment_container);

			mPaymentOptionsText = (TextView) mBothCardPaymentLayout.findViewById(R.id.paymentoptionsTextID);
			mPaymentOptionsCardContainer = (FrameLayout) mBothCardPaymentLayout.findViewById(R.id.mobilepay_paymentoptions_container);
			mPaymentOptionsCardValue = (EditText) mPaymentOptionsCardContainer.findViewById(R.id.mobilepay_payment_options);
			mPaymentOptionsCardContextImage = (ImageView) mPaymentOptionsCardContainer.findViewById(R.id.mobilepay_payment_options_contextImage);
			mBothCardOptionText = (TextView) mChooseCardAmountDetailsLayout.findViewById(R.id.CardOptionTextID);
			mBothCardOptionsContainer = (FrameLayout) mChooseCardAmountDetailsLayout.findViewById(R.id.mobilepay_cards_container);
			mBothCardOptionValue = (EditText) mChooseCardAmountDetailsLayout.findViewById(R.id.mobilepay_choosedcard);
			mBothCardOptionContextImage = (ImageView) mChooseCardAmountDetailsLayout.findViewById(R.id.mobilepay_choosecard_contextImage);

			mAmountOnGiftcardLayout = (LinearLayout) mBothCardPaymentLayout.findViewById(R.id.giftcard_amount_detailsId);
			mAmountOnGiftcardValue = (EditText) mAmountOnGiftcardLayout.findViewById(R.id.mobilepay_giftcard_amount);

			mCreditCardPaymentContainer = (FrameLayout) mBothCardPaymentLayout.findViewById(R.id.mobilepay_card_container);
			mCreditCardPaymentValue = (EditText) mCreditCardPaymentContainer.findViewById(R.id.mobilepay_choosedcreditcard_value);
			mCreditCardPaymentContextImage = (ImageView) mCreditCardPaymentContainer.findViewById(R.id.mobilepay_choosedcreditcard_contextImage);
			mBalanceOnCreditCardLayout = (LinearLayout) mBothCardPaymentLayout.findViewById(R.id.balanceOnCreditCardLayoutId);
			mBalanceOnCreditCardValue = (TextView) mBalanceOnCreditCardLayout.findViewById(R.id.mobilepay_creditcard_amount);
			mMenuBarNext = (TextView) footerlayout.findViewById(R.id.mobilepay_steps_FooterText);
			mMenuBarStep3Back = (TextView) footerlayout.findViewById(R.id.mobilepay_steps_BackFooterText);
			mMenuBarStep3Splitter = (View) footerlayout.findViewById(R.id.mobilepay_steps_backmenusplitter);
			
			/**
			 * Initiliazation of step 3 views
			 **/
			mStep3Container = (ScrollView) mManageCardsStep2.findViewById(R.id.mobilepay_approve_container);
			mStep3_AmountContainer = (LinearLayout) mStep3Container.findViewById(R.id.step3_amount_container);
			mStep3_TipContainer = (LinearLayout) mStep3Container.findViewById(R.id.step3_tip_container);
			mStep3_TotalChargeContainer = (LinearLayout) mStep3Container.findViewById(R.id.step3_totalcharge_container);
			mStep3_CardUsedContainer = (LinearLayout) mStep3Container.findViewById(R.id.step3_cardused_container); 
			mStep3_TotalOnCreditCardContainer = (LinearLayout) mStep3Container.findViewById(R.id.step3_totaloncreditcard_container);
			mStep3_GiftCardUsedContainer = (LinearLayout) mStep3Container.findViewById(R.id.step3_giftcardused_container); 
			mStep3_TotalOnGiftCardContainer = (LinearLayout) mStep3Container.findViewById(R.id.step3_totalongiftcard_container);
			mStep3_AddNoteContainer = (LinearLayout) mStep3Container.findViewById(R.id.step3_addnote_container);
			mStep3_AddNote = (TextView) mStep3Container.findViewById(R.id.step3_addnote);
			mStep3_PinContainer = (LinearLayout) mStep3Container.findViewById(R.id.step3_pin_container);
			mStep3_FirstBorderId = (View) mStep3Container.findViewById(R.id.firstcardtype_borderId);
			mStep3_SecondBorderId = (View) mStep3Container.findViewById(R.id.Secondcardtype_borderId);

			mStep3_AmountValue = (TextView) mStep3Container.findViewById(R.id.mobilepay_approve_amountvalue);
			mStep3_TipValue = (TextView) mStep3Container.findViewById(R.id.mobilepay_approve_tipvalue);
			mStep3_TotalChargeValue = (TextView) mStep3Container.findViewById(R.id.mobilepay_approve_totalvalue);
			mStep3_CardUsedValue = (TextView) mStep3Container.findViewById(R.id.mobilepay_approve_cardTypeId); 
			mStep3_TotalOnCreditCardValue = (TextView) mStep3Container.findViewById(R.id.mobilepay_approve_creditCardAmountId);
			mStep3_GiftCardUsedValue = (TextView) mStep3Container.findViewById(R.id.mobilepay_approve_GiftCardId); 
			mStep3_TotalOnGiftCardValue = (TextView) mStep3Container.findViewById(R.id.mobilepay_approve_GiftCardAmountId);
			mStep3_AddNoteValue = (EditText) mStep3Container.findViewById(R.id.mobilepay_approve_addnote);
			mStep3_PinValue = (EditText) mStep3Container.findViewById(R.id.mobilepay_approve_pinvalue);
			mGenerateQR = (Button) mStep3Container.findViewById(R.id.mobilepay_approve_generateqr);

			// Initialising step-4 QR code details
			mQRCodecontainer = (RelativeLayout) mManageCardsStep2.findViewById(R.id.mobilepay_step4_QRContainer);
			mStep4Progressbar = (ProgressBar) mQRCodecontainer.findViewById(R.id.step4_qr_progressbar);
			mQRCodeImage = (ImageView) mQRCodecontainer.findViewById(R.id.step4_qr_imageId);
			mQRCodeDestructButton = (Button) mQRCodecontainer.findViewById(R.id.step4_qr_destructId);
			mQRCodeExpiryText = (TextView) mQRCodecontainer.findViewById(R.id.step4_qr_expirytimevalue);

			if(getIntent().hasExtra("FromPointOfSale")){ // for store_owner point of sale
				storeowner_leftmenu = new StoreOwner_LeftMenu(Step2_ManageCards.this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStep2ManageCardsFreezeView, TAG);
				mLeftMenu = storeowner_leftmenu.intializeInflater();
				storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
				/* Header Tab Bar which contains logout,notification and home buttons*/
				header = (Header) app.findViewById(R.id.storeowner_mobilepay_header);
				header.intializeInflater(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStep2ManageCardsFreezeView, TAG);
				final View[] children = new View[] { mLeftMenu, app};
				/* Scroll to app (view[1]) when layout finished.*/
				int scrollToViewIdx = 1;
				scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
				mStep2ManageCardsFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStep2ManageCardsFreezeView, TAG,mAmountValue,true));
			}else{ // for customer mobile pay
				leftMenu = inflater.inflate(R.layout.horz_scroll_menu, null);
				ViewGroup leftMenuItems = (ViewGroup) leftMenu.findViewById(R.id.menuitems);
				final ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.mobilepay_steps_tabbar);

				leftMenuScrollView = (ScrollView) leftMenu.findViewById(R.id.leftmenu_scrollview);
				leftMenuScrollView.fullScroll(View.FOCUS_UP);
				leftMenuScrollView.pageScroll(View.FOCUS_UP);

				btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_mobilepay_steps);
				btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
				mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
				mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
				btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(Step2_ManageCards.this));//ClickListener for Header Home image
				mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
				mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);

				mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
				new LoginChoiceTabBarImage(Step2_ManageCards.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();

				btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu,mStep2ManageCardsFreezeView,mAmountValue));

				final View[] children = new View[] { leftMenu, app };

				// Scroll to app (view[1]) when layout finished.
				int scrollToViewIdx = 1;
				scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

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

				//LeftMenu
				mStep2ManageCardsHome = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_home);
				mStep2ManageCardsZpay = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zpay);
				mStep2ManageCardsInvoiceCenter = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_invoicecenter);
				mStep2ManageCardsManageCards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_managecards);
				mStep2ManageCardsGiftcards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zgiftcards);
				mStep2ManageCardsReceipts = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_receipts);
				mStep2ManageCardsMyFavourites = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfavourites);
				mStep2ManageCardsMyFriends = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfriends);
				mStep2ManageCardsChat = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_customercenter);
				mStep2ManageCardsRewards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_rewards);
				mStep2ManageCardsSettings = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_settings);
				mStep2ManageCardsLogout = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_logout);

				mStep2ManageCardsHomeText = (TextView) leftMenuItems.findViewById(R.id.menuHome);
				mStep2ManageCardsHomeText.setTypeface(mZouponsFont);
				mStep2ManageCardsZpayText = (TextView) leftMenuItems.findViewById(R.id.menuZpay);
				mStep2ManageCardsZpayText.setTypeface(mZouponsFont);
				mStep2ManageCardsInvoiceCenterText = (TextView) leftMenuItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
				mStep2ManageCardsInvoiceCenterText.setTypeface(mZouponsFont);
				mStep2ManageCardsManageCardsText = (TextView) leftMenuItems.findViewById(R.id.menuManageCards);
				mStep2ManageCardsManageCardsText.setTypeface(mZouponsFont);
				mStep2ManageCardsGiftCardsText = (TextView) leftMenuItems.findViewById(R.id.menuGiftCards);
				mStep2ManageCardsGiftCardsText.setTypeface(mZouponsFont);
				mStep2ManageCardsReceiptsText = (TextView) leftMenuItems.findViewById(R.id.menuReceipts);
				mStep2ManageCardsReceiptsText.setTypeface(mZouponsFont);
				mStep2ManageCardsMyFavoritesText = (TextView) leftMenuItems.findViewById(R.id.menufavorites);
				mStep2ManageCardsMyFavoritesText.setTypeface(mZouponsFont);
				mStep2ManageCardsMyFriendsText = (TextView) leftMenuItems.findViewById(R.id.menuMyFriends);
				mStep2ManageCardsMyFriendsText.setTypeface(mZouponsFont);
				mStep2ManageCardsChatText = (TextView) leftMenuItems.findViewById(R.id.menuCustomerCenter);
				mStep2ManageCardsChatText.setTypeface(mZouponsFont);
				mStep2ManageCardsRewardsText = (TextView) leftMenuItems.findViewById(R.id.menuRewards);
				mStep2ManageCardsRewardsText.setTypeface(mZouponsFont);
				mStep2ManageCardsSettingsText = (TextView) leftMenuItems.findViewById(R.id.menuSettings);
				mStep2ManageCardsSettingsText.setTypeface(mZouponsFont);
				mStep2ManageCardsLogoutText = (TextView) leftMenuItems.findViewById(R.id.menuLogout);
				mStep2ManageCardsLogoutText.setTypeface(mZouponsFont);

				mStep2ManageCardsFreezeView.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, mStep2ManageCardsFreezeView,mAmountValue));

				mStep2ManageCardsHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, Step2_ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
				mStep2ManageCardsZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, Step2_ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
				mStep2ManageCardsInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, Step2_ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
				mStep2ManageCardsGiftcards.setOnClickListener(new MenuItemClickListener(leftMenuItems,Step2_ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
				mStep2ManageCardsManageCards.setOnClickListener(new MenuItemClickListener(leftMenuItems, Step2_ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
				mStep2ManageCardsReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, Step2_ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
				mStep2ManageCardsMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,Step2_ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
				mStep2ManageCardsMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,Step2_ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
				mStep2ManageCardsChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,Step2_ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
				mStep2ManageCardsRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,Step2_ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
				mStep2ManageCardsSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,Step2_ManageCards.this,POJOMainMenuActivityTAG.TAG=TAG));
				mStep2ManageCardsLogout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						v.setBackgroundResource(R.drawable.gradient_bg_hover);

						if(NormalPaymentAsyncTask.mCountDownTimer!=null){
							NormalPaymentAsyncTask.mCountDownTimer.cancel();
							NormalPaymentAsyncTask.mCountDownTimer = null;
							Log.i(TAG,"Timer Stopped Successfully");
						}

						// AsyncTask to call the logout webservice to end the login session
						new LogoutSessionTask(Step2_ManageCards.this).execute();
						Intent intent_logout = new Intent(Step2_ManageCards.this,ZouponsLogin.class);
						intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent_logout);
						finish();
					}
				});

				mLogoutImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						v.setBackgroundResource(R.drawable.gradient_bg_hover);
						Intent intent_logout = new Intent(Step2_ManageCards.this,ZouponsLogin.class);
						intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent_logout);
						finish();
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
						mPopUpWindow.setWidth(tabBar.getWidth()-20);
						mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
						mPopUpWindow.setOutsideTouchable(false);
						mPopUpWindow.showAsDropDown(tabBar, 0, 20);
						mNotificationList.setAdapter(new CustomNotificationAdapter(Step2_ManageCards.this));
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
			}

			/**
			 * Below EditText Code to show number keypad in password mode
			 * */
			mStep3_PinValue.setInputType(InputType.TYPE_CLASS_NUMBER);
			mStep3_PinValue.setTransformationMethod(PasswordTransformationMethod.getInstance());	

			mGetBundleValue = getIntent().getExtras().getString("datasourcename")!=null?getIntent().getExtras().getString("datasourcename"):"";
			//mInvoiceBundle = getIntent().getExtras().getString("datasourcename") !=null?getIntent().getExtras().getString("datasourcename"):"";
			mInvoiceSourceBundle = getIntent().getExtras().getString("invoice_source") !=null?getIntent().getExtras().getString("invoice_source"):"";
			hasGiftCardId = getIntent().hasExtra("card_id") ? true : false; 

			if(getIntent().getExtras().getString("datasourcename").equals("zpay") && !hasGiftCardId){	// Loop success if control from righnavigation zpay or step1 with store mobile payment
				mStoreName="";
				mStoreID="";
				mStoreLocationId="";
				Log.i("Data source", "ZPAY");
				// To set tip layout visible or not
				tipviewstatus("enable");
			 }else if(getIntent().getExtras().getString("datasourcename").equals("zpay") && hasGiftCardId){	// Loop success if control from righnavigation selfuse in Giftcards/Deals
				mStoreID=getIntent().getExtras().getString("giftcard_storeid");
				mStoreLocationId=getIntent().getExtras().getString("giftcard_locationid");
				mStoreName=getIntent().getExtras().getString("giftcard_storename");
				mChoosedStoreName=mStoreName;
				mGiftCardId=getIntent().getExtras().getString("card_id");
				mGiftCardPurchaseId=getIntent().getExtras().getString("card_purchase_id");
				mChoosedGiftcardBalanceValue = getIntent().getExtras().getString("cardvalue");
				mChoosedGiftcardID = mGiftCardId;
				mChoosedGiftCardPurchaseID = mGiftCardPurchaseId;
				choosedcardposition = getIntent().getExtras().getInt("cardposition");
				// To set tip layout visible or not
				tipviewstatus("enable");
				mAmountValue.setFocusable(true);
				mAmountValue.setFocusableInTouchMode(true);
			}else if(getIntent().getExtras().getString("datasourcename").equals("cards_selfuse")){	//Loop success if control from RightNavigation GiftCards or Deals SelfUse menu.
				mStoreID=getIntent().getExtras().getString("giftcard_storeid");
				mStoreLocationId=getIntent().getExtras().getString("giftcard_locationid");
				mStoreName=getIntent().getExtras().getString("giftcard_storename");
				mChoosedStoreName=mStoreName;
				mGiftCardId=getIntent().getExtras().getString("card_id");
				mGiftCardValue=getIntent().getExtras().getString("cardvalue");
				mFaceValue=getIntent().getExtras().getString("facevalue");
				// To set tip layout visible or not
				tipviewstatus("disable");
				mAmountValue.setVisibility(View.GONE);
				mDollarText.setVisibility(View.GONE);
				mChoosedAmountValue.setVisibility(View.VISIBLE);
				mChoosedAmountValue.setText("$"+mGiftCardValue);
				mTotalAmountText.setText("$"+mGiftCardValue);
				//To enable Next button at footer view
				mMenuBarNext.setBackgroundResource(R.drawable.header_2);
			}else if(getIntent().getExtras().getString("datasourcename").equals("cards_sendtofriend")){ //Loop success if control from RightNavigation Friend Notes Description Notes Send Button.
				mStoreID=RightMenuStoreId_ClassVariables.mStoreID;
				mStoreName=RightMenuStoreId_ClassVariables.mStoreName;
				mChoosedStoreName=mStoreName;
				mStoreLocationId = RightMenuStoreId_ClassVariables.mLocationId;
				mGiftCardId=getIntent().getExtras().getString("card_id");
				mGiftCardValue=getIntent().getExtras().getString("cardvalue");
				mFriendName=getIntent().getExtras().getString("friend_name");
				mFriendId=getIntent().getExtras().getString("friend_id");
				mIsZouponsFriend=getIntent().getExtras().getString("is_zouponsfriend");
				mFriendNotes=getIntent().getExtras().getString("notes_description");
				mDate=getIntent().getExtras().getString("date");
				mTimeZone=getIntent().getExtras().getString("timezone");
				mFaceValue=getIntent().getExtras().getString("facevalue");

				// To set tip layout visible or not
				tipviewstatus("disable");
				// To set Giftcard Amount details
				mAmountValue.setVisibility(View.GONE);
				mDollarText.setVisibility(View.GONE);
				mChoosedAmountValue.setVisibility(View.VISIBLE);
				mChoosedAmountValue.setText("$"+mGiftCardValue);
				mTotalAmountText.setText("$"+mGiftCardValue);
				//To enable Next button at footer view
				mMenuBarNext.setBackgroundResource(R.drawable.header_2);
			}else if(getIntent().getExtras().getString("datasourcename").equalsIgnoreCase("invoice_approval")){ //Loop success if control from invoice approval 

				if(getIntent().getExtras().getString("invoice_source").equalsIgnoreCase("normal_payment")){ // For Normal Payment
					mInvoicePaymentType = "normal_payment";
					mStoreID=getIntent().getExtras().getString("payment_storeid");
					mStoreLocationId=getIntent().getExtras().getString("payment_locationid");
					mStoreName=getIntent().getExtras().getString("payment_storename");
					mChoosedStoreName=mStoreName;
					mInvoiceId=getIntent().getExtras().getString("invoice_Id");
					mInvoiceRaisedBy=getIntent().getExtras().getString("store_employeeId");
					mInvoiceRaisedByType=getIntent().getExtras().getString("store_employeeType");
					mInvoiceAmount=getIntent().getExtras().getString("amount");
					// To set details from invoice
					mAmountValue.setVisibility(View.GONE);
					mDollarText.setVisibility(View.GONE);
					mChoosedAmountValue.setVisibility(View.VISIBLE);
					mTotalAmountText.setText("$"+String.format("%.2f",Double.parseDouble(mInvoiceAmount)));
					double mAmount = Double.parseDouble(mInvoiceAmount) /*- Double.parseDouble(mInvoiceTipAmount)*/;
					mChoosedAmountValue.setText("$"+String.format("%.2f",mAmount));
					// To set tip layout visible or not
					mTipValueContainer.setVisibility(View.VISIBLE);
					mTipAmountText.setVisibility(View.GONE);

					//To enable Next button at footer view
					mMenuBarNext.setBackgroundResource(R.drawable.header_2);
				}
			}else if(getIntent().getExtras().getString("datasourcename").equalsIgnoreCase("point_of_sale")){
				mStoreID=getIntent().getExtras().getString("payment_storeid");
				mStoreLocationId=getIntent().getExtras().getString("payment_locationid");
				mStoreName=getIntent().getExtras().getString("payment_storename");
				mChoosedStoreName=mStoreName;
				// To set tip layout visible or not
				tipviewstatus("enable");
				mAmountValue.setVisibility(View.GONE);
				mDollarText.setVisibility(View.GONE);
				mChoosedAmountValue.setVisibility(View.VISIBLE);
				mChoosedAmountValue.setText("$"+StoreOwner_PointOfSale_Part1.mAmount);
				mTotalAmountText.setText("$"+StoreOwner_PointOfSale_Part1.mAmount);
				//To enable Next button at footer view
				mMenuBarNext.setBackgroundResource(R.drawable.header_2);
			}

			/*
			 * If control from rightnavigation we have't shown search text with clickable otherwise we have to shown search text with clickable option in this page header
			 * */
			if(ZpayStep2SearchEnable.searchEnableFlag==true){
				ZpayStep2SearchEnable.searchEnableFlag=false;
			}

			//Initial sync to check giftcards and credit card availability
			if(mGetBundleValue.equalsIgnoreCase("zpay") || (mGetBundleValue.equalsIgnoreCase("invoice_approval") && getIntent().getExtras().getString("invoice_source").equalsIgnoreCase("normal_payment"))||mGetBundleValue.equalsIgnoreCase("point_of_sale") ){ // If for mobile payment check for both cards
				if(mGetBundleValue.equalsIgnoreCase("point_of_sale")){
					Step2StoreGiftCardsAsynchThread step2storegiftcardsasynchthread = new Step2StoreGiftCardsAsynchThread(Step2_ManageCards.this,mStoreLocationId,getIntent().getExtras().getString("user_id"),mStoreID,getIntent().getExtras().getString("datasourcename"),hasGiftCardId,getIntent().getExtras().getString("user_type"));
					step2storegiftcardsasynchthread.execute("","","");
				}else{
					Step2StoreGiftCardsAsynchThread step2storegiftcardsasynchthread = new Step2StoreGiftCardsAsynchThread(Step2_ManageCards.this,mStoreLocationId,UserDetails.mServiceUserId,mStoreID,getIntent().getExtras().getString("datasourcename"),hasGiftCardId);
					step2storegiftcardsasynchthread.execute("","","");
				}
			}else{  // If for buying giftcard check only credit card availability
				WebServiceStaticArrays.mAllGiftCardList.clear();
				WebServiceStaticArrays.mMyGiftCards.clear();

				//Start Credit Card availability check asynchthread
				CardOnFilesAsynchThread cardonfilesasynchthread = new CardOnFilesAsynchThread(Step2_ManageCards.this);
				cardonfilesasynchthread.execute("","","");
			}

			mTipValue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					tippicker();
				}
			}); 

			mTipContextImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					tippicker();
				}
			});
			
			mPaymentOptionsCardValue.setText("CreditCard");
			mBothCardOptionValue.setHint("Select card");
			
			mPaymentOptionsCardValue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					paymentoptioncardpicker();
				}
			});
			
			mPaymentOptionsCardContextImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					paymentoptioncardpicker();
				}
			});

			mBothCardOptionValue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					contextMenuOpen(mBothCardOptionContextImage);
				}
			});

			mBothCardOptionContextImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					contextMenuOpen(mBothCardOptionContextImage);
				}
			});
			
			mCreditCardPaymentValue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					creditcardpaymentvaluepicker();
				}
			});

			mCreditCardPaymentContextImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					creditcardpaymentvaluepicker();
				}
			});
			
			mAddCardButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent_addcard = new Intent(Step2_ManageCards.this,AddCardInformation.class);
					if(mGetBundleValue.equalsIgnoreCase("point_of_sale")){
						intent_addcard.putExtra("class_name", "StoreOwner_PointOfSale");
						intent_addcard.putExtra("user_id", getIntent().getExtras().getString("user_id"));
					}else{
						intent_addcard.putExtra("class_name", TAG);
					}
					startActivityForResult(intent_addcard,100);
				}
			});

			mOnlyCreditCardValue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onlycreditcardpicker();
				}
			});

			mOnlyCreditCardContextImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onlycreditcardpicker();
				}
			});
			
			mAmountValue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mAmountValue.setSelection(mAmountValue.getText().toString().length());
				}
			});

			mAmountOnGiftcardValue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mAmountOnGiftcardValue.setSelection(mAmountOnGiftcardValue.getText().toString().length());
				}
			});

			mAmountValue.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				@Override
				public void afterTextChanged(Editable s) {
					if(s.toString()!=null && s.toString().length()>0){
						if(s.toString().length()==1){ // Initial formation of string
							mAmountValue.removeTextChangedListener(this);
							mAmountValue.setCursorVisible(false);
							mAmountValue.setText("0.0"+s);
							mAmountValue.setSelection(mAmountValue.getText().toString().length());
							mAmountValue.addTextChangedListener(this);
						}else{
							mAmountValue.removeTextChangedListener(this);
							String computedAmount = s.toString().replace(".", "");
							String mActualAmount = computedAmount.substring(0, computedAmount.length()-2);
							String Amount = mActualAmount+"."+computedAmount.substring(computedAmount.length()-2,computedAmount.length());
							if(Amount.charAt(0) == '0' && Amount.length()>3){ // To replace extra "0" from first position
								Amount = Amount.replaceFirst("0", "");
							}else if(Amount.length() <= 3){
								Amount = String.format("%.2f",Float.valueOf(Amount));	
							}
							mAmountValue.setText(Amount);
							mAmountValue.setSelection(mAmountValue.getText().toString().length());
							mAmountValue.addTextChangedListener(this);
						}

						if((mAmountValue.getText().toString().trim().length()>0) && !mAmountValue.getText().toString().trim().equalsIgnoreCase("0.00")){
							// To clear selected giftcard values when amount is changed...
							/*if(mPaymentOptionsCardValue.getText().toString().equalsIgnoreCase("GiftCard/DealCard")){
								mBothCardOptionValue.getText().clear();

								//Set Value to balance amount
								mBalanceOnCreditCardValue.setText(mTotalAmountText.getText().toString().trim());
								mTotalAmountText.setText("$"+String.format("%.2f", computeTotalAmount(mChoosedTipAmount)));
								//To enable Next button at footer view
								mMenuBarNext.setBackgroundResource(R.drawable.header_2);
							}else{*/
								if(mAmountValue.isFocused() && mBothCardOptionValue.getText().toString().trim().length() == 0 && mAmountValue.getText().toString().trim().length()>0 && !mAmountValue.getText().toString().trim().equalsIgnoreCase("0.00")){ // If no giftcard choosed 
									alertBox_service("Information","Please choose a card for payment.");
								}else{ // If giftcard choosed and check with the avialable balance
									if(!mChoosedGiftcardBalanceValue.equalsIgnoreCase("0") && (Double.valueOf(computeTotalAmount(mChoosedTipAmount))>Double.valueOf(mChoosedGiftcardBalanceValue))){
										mDifferntiateAlertMessage=0;
										alertBox_service("Information",getResources().getString(R.string.zpay_step2_giftcard_amount_alertmsg_new));
									}else{
										mTotalAmountText.setText("$"+String.format("%.2f", computeTotalAmount(mChoosedTipAmount)));
									}
								}
							//}
						}else{
							mTotalAmountText.setText("$"+String.format("%.2f", computeTotalAmount(mChoosedTipAmount)));
						}
					}else{
					}
				}
			});

			mAmountOnGiftcardValue.addTextChangedListener(this);

			mReviewPayment.setOnClickListener(new OnClickListener() { 

				@Override
				public void onClick(View v) {
					try{
						if(mAddCardLayout.getVisibility() == View.VISIBLE){
							alertBox_service("Information", "Please add credit card to proceed the payment");
						}else if(mBothCardOptionValue.getVisibility() == View.VISIBLE){ // If both card available
							if(mBothCardOptionValue.getText().toString().trim().length() == 0){
								alertBox_service("Information", "Please choose any card to proceed the payment");
							}else if(mAmountValue.getVisibility()==View.VISIBLE && mAmountValue.getText().toString().trim().equalsIgnoreCase("")||mAmountValue.getText().toString().trim().equalsIgnoreCase("0.00")){
								alertBox_service("Information", "Please enter the amount for payment");
							}else{
								mChooseCardContainer.setVisibility(View.GONE);
								mChooseCardAmountDetailsLayout.setVisibility(View.GONE);
								mStep3Container.setVisibility(View.VISIBLE);
								mReviewPayment.setVisibility(View.GONE);
								mMenuBarStep3Back.setVisibility(View.VISIBLE);
								mMenuBarStep3Splitter.setVisibility(View.VISIBLE);

								setMenuBarText();


								if(mAmountValue.getVisibility()==View.VISIBLE){
									mStep3_AmountValue.setText("$"+String.format("%.2f", Double.parseDouble(mAmountValue.getText().toString())));
									double tip_amount = Double.parseDouble(mTotalAmountText.getText().toString().substring(1,mTotalAmountText.getText().toString().length())) - Double.parseDouble(mAmountValue.getText().toString());
									mTipAmount = String.format("%.2f", tip_amount);
								}else{
									if(mChoosedAmountValue.getText().toString().contains("$")){
										mStep3_AmountValue.setText("$"+String.format("%.2f", Double.parseDouble(mChoosedAmountValue.getText().toString().substring(1,mChoosedAmountValue.getText().toString().length()))));
									}else{
										mStep3_AmountValue.setText("$"+String.format("%.2f", Double.parseDouble(mChoosedAmountValue.getText().toString())));
									}
									double tip_amount = Double.parseDouble(mTotalAmountText.getText().toString().substring(1,mTotalAmountText.getText().toString().length())) - Double.parseDouble(mChoosedAmountValue.getText().toString().substring(1,mChoosedAmountValue.getText().toString().length()));
									mTipAmount = String.format("%.2f", tip_amount);
								}
								mStep3_TipValue.setText("$"+mTipAmount+"("+mTipValue.getText().toString()+")");
								mStep3_TotalChargeValue.setText(mTotalAmountText.getText());
								mStep3_FirstBorderId.setVisibility(View.GONE);
								mStep3_PinValue.getText().clear();
								mStep3_TotalOnGiftCardContainer.setVisibility(View.GONE);
								mStep3_TotalOnCreditCardContainer.setVisibility(View.GONE);
								if(mZpayPaymentType.equalsIgnoreCase("giftcard")){
									mStep3_CardUsedContainer.setVisibility(View.GONE);
									mStep3_SecondBorderId.setVisibility(View.GONE);
									mStep3_GiftCardUsedContainer.setVisibility(View.VISIBLE);
									mStep3_GiftCardUsedValue.setText(mBothCardOptionValue.getText().toString().trim());
								}else{
									mStep3_CardUsedValue.setText(mBothCardOptionValue.getText().toString());
									mStep3_SecondBorderId.setVisibility(View.GONE);
									mStep3_GiftCardUsedContainer.setVisibility(View.GONE);
								}

							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});

			mStep3_AddNote.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final Dialog mNotesDetails = new Dialog(Step2_ManageCards.this);
					mNotesDetails.setTitle("Note Details");
					mNotesDetails.setContentView(R.layout.registration_mobiledetails);
					WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
					lWindowParams.copyFrom(mNotesDetails.getWindow().getAttributes());
					lWindowParams.width = WindowManager.LayoutParams.FILL_PARENT;
					lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
					mNotesDetails.getWindow().setAttributes(lWindowParams);
					final EditText mDialogNotesValue = (EditText) mNotesDetails.findViewById(R.id.notes_value);
					Button mOkButton = (Button)mNotesDetails.findViewById(R.id.notes_save_buttonId);
					mNotesDetails.show();
					mOkButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mStep3Notes = mDialogNotesValue.getText().toString().trim();
							mNotesDetails.dismiss();
						}
					});
				}
			});

			mMenuBarStep3Back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mChooseCardContainer.setVisibility(View.GONE);
					mChooseCardAmountDetailsLayout.setVisibility(View.VISIBLE);
					mStep3Container.setVisibility(View.GONE);
					mReviewPayment.setVisibility(View.VISIBLE);
					mMenuBarStep3Back.setVisibility(View.GONE);
					mMenuBarStep3Splitter.setVisibility(View.GONE);
					mMenuBarNext.setVisibility(View.VISIBLE);
					mMenuBarNext.setText("Add Card");
					mMenuBarNext.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.add_card), null, null);
				}
			});

			mGenerateQR.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mStep3_PinValue.getText().toString().length() == 0){
						alertBox_service("Information", "Please enter the security pin to proceed the payment");
					}else if(mStep3_PinValue.getText().toString().length() < 4){
						alertBox_service("Information", "Please enter four digit security pin to proceed the payment");
					}else{
						
						KeyboardUtil.hideKeyboard(Step2_ManageCards.this);
						
						if(mGenerateQR.getText().toString().equalsIgnoreCase("Generate QR")){ // Generare Qr code
							if(mZpayPaymentType.equalsIgnoreCase("creditcard")){
								NormalPaymentAsyncTask mNormalPayment = new NormalPaymentAsyncTask(Step2_ManageCards.this,mSelectedCreditCardId,mStoreID,mTipAmount,mStep3_TotalChargeValue.getText().toString().substring(1,mStep3_TotalChargeValue.getText().toString().length()),mStep3Notes, mStep3_AmountValue.getText().toString().trim().substring(1,mStep3_AmountValue.getText().toString().length()),mStoreLocationId,"",mStep3Container,mQRCodecontainer,mQRCodeImage/*,mHeaderOptionsLayout*/,mQRCodeExpiryText,footerlayout/*,mMobilePayStoreName*/,imageviewWidth,imageviewHeight,mStep4Progressbar);
								mNormalPayment.execute(mStep3_PinValue.getText().toString(),mZpayPaymentType,UserDetails.mServiceUserId,AccountLoginFlag.accountUserTypeflag,"from_customer");
							}else if(mZpayPaymentType.equalsIgnoreCase("giftcard")){
								NormalPaymentAsyncTask mNormalPayment = new NormalPaymentAsyncTask(Step2_ManageCards.this,mChoosedGiftcardID,mTipAmount,mStep3_TotalChargeValue.getText().toString().substring(1,mStep3_TotalChargeValue.getText().toString().length()),mChoosedGiftCardPurchaseID,"",mStep3Notes, mStep3_AmountValue.getText().toString().trim().substring(1,mStep3_AmountValue.getText().toString().length()),mStep3Container,mQRCodecontainer,mQRCodeImage/*,mHeaderOptionsLayout*/,mQRCodeExpiryText,footerlayout/*,mMobilePayStoreName*/,imageviewWidth,imageviewHeight,mStep4Progressbar);
								mNormalPayment.execute(mStep3_PinValue.getText().toString(),mZpayPaymentType,UserDetails.mServiceUserId,AccountLoginFlag.accountUserTypeflag,"from_customer");
							}	
						}else{ // Authoize payment
							if(getIntent().getExtras().getString("datasourcename").equals("cards_selfuse")){
								PurchaseGiftCardAsyncTask mGiftcardPurchase = new PurchaseGiftCardAsyncTask(Step2_ManageCards.this,mSelectedCreditCardId, mGiftCardId, mGiftCardValue, mStep3Notes,mStoreID,CustomCardDetailsAdapter.mSendGiftCardType,mStoreLocationId,mFaceValue);
								mGiftcardPurchase.execute(mStep3_PinValue.getText().toString());
							}else if( getIntent().getExtras().getString("datasourcename").equals("cards_sendtofriend")){
								PurchaseGiftCardAsyncTask mGiftcardPurchase = new PurchaseGiftCardAsyncTask(Step2_ManageCards.this,mSelectedCreditCardId, mGiftCardId, mGiftCardValue,mFriendId,mFriendNotes,mDate,mTimeZone,mIsZouponsFriend,mStep3Notes,mStoreID,CustomCardDetailsAdapter.mSendGiftCardType,mStoreLocationId,mFaceValue);
								mGiftcardPurchase.execute(mStep3_PinValue.getText().toString());
							}else if( getIntent().getExtras().getString("datasourcename").equals("invoice_approval") && getIntent().getExtras().getString("invoice_source").equals("normal_payment")){
								if(mZpayPaymentType.equalsIgnoreCase("creditcard")){
									InvoiceNormalPaymentAsyncTask mNormalPayment = new InvoiceNormalPaymentAsyncTask(Step2_ManageCards.this,mInvoiceId,mInvoiceRaisedBy,mInvoiceRaisedByType,mSelectedCreditCardId,mStoreID,mTipAmount,mStep3_TotalChargeValue.getText().toString().substring(1,mStep3_TotalChargeValue.getText().toString().length()),mStoreLocationId,mStep3Notes);
									mNormalPayment.execute(mStep3_PinValue.getText().toString(),mZpayPaymentType);
								}else if(mZpayPaymentType.equalsIgnoreCase("giftcard")){
									InvoiceNormalPaymentAsyncTask mNormalPayment = new InvoiceNormalPaymentAsyncTask(Step2_ManageCards.this,mInvoiceId,mTipAmount,mStep3_TotalChargeValue.getText().toString().substring(1,mStep3_TotalChargeValue.getText().toString().length()),mChoosedGiftCardPurchaseID,mInvoiceRaisedBy,mInvoiceRaisedByType,mStoreID,"",mStoreLocationId,mStep3Notes);
									mNormalPayment.execute(mStep3_PinValue.getText().toString(),mZpayPaymentType);
								}
							}else if(getIntent().getExtras().getString("datasourcename").equals("zpay")){ // For customer module normal payment

							}else if(getIntent().hasExtra("FromPointOfSale")){ // From store owner module normal payment 
								if(mZpayPaymentType.equalsIgnoreCase("creditcard")){
									NormalPaymentAsyncTask mNormalPayment = new NormalPaymentAsyncTask(Step2_ManageCards.this,mSelectedCreditCardId,mStoreID,mTipAmount,mStep3_TotalChargeValue.getText().toString().substring(1,mStep3_TotalChargeValue.getText().toString().length()),mStep3Notes, mStep3_AmountValue.getText().toString().trim().substring(1,mStep3_AmountValue.getText().toString().length()),mStoreLocationId,"",mStep3Container,mQRCodecontainer,mQRCodeImage/*,mHeaderOptionsLayout*/,mQRCodeExpiryText,footerlayout/*,mMobilePayStoreName*/,imageviewWidth,imageviewHeight,mStep4Progressbar);
									mNormalPayment.execute(mStep3_PinValue.getText().toString(),mZpayPaymentType,getIntent().getExtras().getString("user_id"),getIntent().getExtras().getString("user_type"),"from_storemodule"); 
								}else if(mZpayPaymentType.equalsIgnoreCase("giftcard")){
									NormalPaymentAsyncTask mNormalPayment = new NormalPaymentAsyncTask(Step2_ManageCards.this,mChoosedGiftcardID,mTipAmount,mStep3_TotalChargeValue.getText().toString().substring(1,mStep3_TotalChargeValue.getText().toString().length()),mChoosedGiftCardPurchaseID,"",mStep3Notes, mStep3_AmountValue.getText().toString().trim().substring(1,mStep3_AmountValue.getText().toString().length()),mStep3Container,mQRCodecontainer,mQRCodeImage/*,mHeaderOptionsLayout*/,mQRCodeExpiryText,footerlayout/*,mMobilePayStoreName*/,imageviewWidth,imageviewHeight,mStep4Progressbar);
									mNormalPayment.execute(mStep3_PinValue.getText().toString(),mZpayPaymentType,getIntent().getExtras().getString("user_id"),getIntent().getExtras().getString("user_type"),"from_storemodule");
								}
							}
						}
					}

				}
			});

			mMenuBarNext.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mMenuBarNext.getText().toString().trim().equalsIgnoreCase("Add Card")){	//Loop for step2 next button
						Intent intent_addcard = new Intent(Step2_ManageCards.this,AddCardInformation.class);
						if(mGetBundleValue.equalsIgnoreCase("point_of_sale")){
							intent_addcard.putExtra("class_name", "StoreOwner_PointOfSale");
							intent_addcard.putExtra("user_id", getIntent().getExtras().getString("user_id"));
						}else{
							intent_addcard.putExtra("class_name", TAG);
						}
						startActivityForResult(intent_addcard,100);
					}else{
						if(mStep3_PinValue.getText().toString().length()>0){
							if(getIntent().getExtras().getString("datasourcename").equals("cards_selfuse")){
								PurchaseGiftCardAsyncTask mGiftcardPurchase = new PurchaseGiftCardAsyncTask(Step2_ManageCards.this,mSelectedCreditCardId, mGiftCardId, mGiftCardValue, mStep3Notes,mStoreID,CustomCardDetailsAdapter.mSendGiftCardType,mStoreLocationId,mFaceValue);
								mGiftcardPurchase.execute(mStep3_PinValue.getText().toString());
							}else if( getIntent().getExtras().getString("datasourcename").equals("cards_sendtofriend")){
								PurchaseGiftCardAsyncTask mGiftcardPurchase = new PurchaseGiftCardAsyncTask(Step2_ManageCards.this,mSelectedCreditCardId, mGiftCardId, mGiftCardValue,mFriendId,mFriendNotes,mDate,mTimeZone,mIsZouponsFriend,mStep3Notes,mStoreID,CustomCardDetailsAdapter.mSendGiftCardType,mStoreLocationId,mFaceValue);
								mGiftcardPurchase.execute(mStep3_PinValue.getText().toString());
							}else if( getIntent().getExtras().getString("datasourcename").equals("invoice_approval") && getIntent().getExtras().getString("invoice_source").equals("normal_payment")){
								if(mZpayPaymentType.equalsIgnoreCase("creditcard")){
									InvoiceNormalPaymentAsyncTask mNormalPayment = new InvoiceNormalPaymentAsyncTask(Step2_ManageCards.this,mInvoiceId,mInvoiceRaisedBy,mInvoiceRaisedByType,mSelectedCreditCardId,mStoreID,mTipAmount,mStep3_TotalChargeValue.getText().toString().substring(1,mStep3_TotalChargeValue.getText().toString().length()),mStoreLocationId,mStep3Notes);
									mNormalPayment.execute(mStep3_PinValue.getText().toString(),mZpayPaymentType);
								}else if(mZpayPaymentType.equalsIgnoreCase("giftcard")){
									InvoiceNormalPaymentAsyncTask mNormalPayment = new InvoiceNormalPaymentAsyncTask(Step2_ManageCards.this,mInvoiceId,mTipAmount,mStep3_TotalChargeValue.getText().toString().substring(1,mStep3_TotalChargeValue.getText().toString().length()),mChoosedGiftCardPurchaseID,mInvoiceRaisedBy,mInvoiceRaisedByType,mStoreID,"",mStoreLocationId,mStep3Notes);
									mNormalPayment.execute(mStep3_PinValue.getText().toString(),mZpayPaymentType);
								}
							}else if(getIntent().getExtras().getString("datasourcename").equals("zpay")){ // For customer module normal payment

							}else if(getIntent().hasExtra("FromPointOfSale")){ // From store owner module normal payment 
								if(mZpayPaymentType.equalsIgnoreCase("creditcard")){
									NormalPaymentAsyncTask mNormalPayment = new NormalPaymentAsyncTask(Step2_ManageCards.this,mSelectedCreditCardId,mStoreID,mTipAmount,mStep3_TotalChargeValue.getText().toString().substring(1,mStep3_TotalChargeValue.getText().toString().length()),mStep3Notes, mStep3_AmountValue.getText().toString().trim().substring(1,mStep3_AmountValue.getText().toString().length()),mStoreLocationId,"",mStep3Container,mQRCodecontainer,mQRCodeImage/*,mHeaderOptionsLayout*/,mQRCodeExpiryText,footerlayout/*,mMobilePayStoreName*/,imageviewWidth,imageviewHeight,mStep4Progressbar);
									mNormalPayment.execute(mStep3_PinValue.getText().toString(),mZpayPaymentType,getIntent().getExtras().getString("user_id"),getIntent().getExtras().getString("user_type"),"from_storemodule"); 
								}else if(mZpayPaymentType.equalsIgnoreCase("giftcard")){
									NormalPaymentAsyncTask mNormalPayment = new NormalPaymentAsyncTask(Step2_ManageCards.this,mChoosedGiftcardID,mTipAmount,mStep3_TotalChargeValue.getText().toString().substring(1,mStep3_TotalChargeValue.getText().toString().length()),mChoosedGiftCardPurchaseID,"",mStep3Notes, mStep3_AmountValue.getText().toString().trim().substring(1,mStep3_AmountValue.getText().toString().length()),mStep3Container,mQRCodecontainer,mQRCodeImage/*,mHeaderOptionsLayout*/,mQRCodeExpiryText,footerlayout/*,mMobilePayStoreName*/,imageviewWidth,imageviewHeight,mStep4Progressbar);
									mNormalPayment.execute(mStep3_PinValue.getText().toString(),mZpayPaymentType,getIntent().getExtras().getString("user_id"),getIntent().getExtras().getString("user_type"),"from_storemodule");
								}
							}
						}else{
							alertBox_service("Information", "Please enter the security pin to proceed the payment");
						}

					}
				}
			});

			mQRCodeDestructButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(NormalPaymentAsyncTask.mCountDownTimer!=null){
						NormalPaymentAsyncTask.mCountDownTimer.cancel();
						NormalPaymentAsyncTask.mCountDownTimer = null;
					}
					GetInvoiceListTask mInvoiceTask = new GetInvoiceListTask(Step2_ManageCards.this);
					mInvoiceTask.execute("REJECTINVOICE",PaymentStatusVariables.Invoice_id);
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void tippicker(){
		if(mAmountValue.getVisibility()==View.VISIBLE){
			if(mAmountValue.getText().toString().trim().length()>0 && !mAmountValue.getText().toString().trim().equalsIgnoreCase("0.00")){
				contextMenuOpen(mTipContextImage);
			 }
		}else{
			if(mChoosedAmountValue.getText().toString().trim().length()>0 && mChoosedAmountValue.getText().toString().contains("$")){
				if(!mChoosedAmountValue.getText().toString().trim().equalsIgnoreCase("$0.00") || !mChoosedAmountValue.getText().toString().trim().equalsIgnoreCase("$0")){
					contextMenuOpen(mTipContextImage);
				}
			}else{
				if(!mChoosedAmountValue.getText().toString().trim().equalsIgnoreCase("0.00") || !mChoosedAmountValue.getText().toString().trim().equalsIgnoreCase("0")){
					contextMenuOpen(mTipContextImage);
				}
			}
		}
	}
	
	private void creditcardpaymentvaluepicker(){
		if(mChoosedAmountValue.getVisibility()== View.GONE && mAmountValue.getText().toString().trim().length()>0){
			contextMenuOpen(mCreditCardPaymentContextImage);	
		}else if(mAmountValue.getVisibility() ==  View.GONE && mChoosedAmountValue.getVisibility()== View.VISIBLE){
			contextMenuOpen(mCreditCardPaymentContextImage);
		}else{
			alertBox_service("Information", "Please enter the transaction amount");
		}
	}
	
	private void onlycreditcardpicker(){
		if(mChoosedAmountValue.getVisibility()== View.GONE && mAmountValue.getText().toString().trim().length()>0){
			contextMenuOpen(mOnlyCreditCardContextImage);	
		}else if(mAmountValue.getVisibility() ==  View.GONE && mChoosedAmountValue.getVisibility()== View.VISIBLE){
			contextMenuOpen(mOnlyCreditCardContextImage);
		}else{
			alertBox_service("Information", "Please enter the transaction amount");
		}
	}
	
	private void tipviewstatus(String enablestatus){
		if(enablestatus.equalsIgnoreCase("enable")){
			mTipValueContainer.setVisibility(View.VISIBLE);
			mTipAmountText.setVisibility(View.GONE);
		}else if(enablestatus.equalsIgnoreCase("disable")){
			mTipValueContainer.setVisibility(View.GONE);
			mTipAmountText.setVisibility(View.VISIBLE);
			mTipAmountText.setText("0%");
		}
	}

	private void paymentoptioncardpicker(){
		if(mChoosedAmountValue.getVisibility()== View.GONE && mAmountValue.getText().toString().trim().length()>0){
			contextMenuOpen(mPaymentOptionsCardContextImage);	
		}else if(mAmountValue.getVisibility() ==  View.GONE && mChoosedAmountValue.getVisibility()== View.VISIBLE){
			contextMenuOpen(mPaymentOptionsCardContextImage);
		}else{
			alertBox_service("Information", "Please enter the transaction amount");
		}
	}
	
	private double computeTotalAmount(int tipamount){

		double enteredAmount;
		if(mAmountValue.getVisibility() == View.VISIBLE && mAmountValue.getText().toString().trim().length() > 0){
			enteredAmount = Double.parseDouble(mAmountValue.getText().toString()); 
		}else if(mChoosedAmountValue.getVisibility() == View.VISIBLE){
			enteredAmount = Double.parseDouble(mChoosedAmountValue.getText().toString().substring(1, mChoosedAmountValue.getText().toString().length()));
		}else{	
			enteredAmount = 0;
		}
		return enteredAmount+((enteredAmount*tipamount)/100);
	}

	private double computePercentageDifference(double totalamount,double mAmount){
		double percentage_difference=0;
		percentage_difference= (totalamount/mAmount)-1;
		return percentage_difference*100;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 100){
			if(mGetBundleValue.equalsIgnoreCase("zpay")){ // If for mobile payment check for both cards
				Step2StoreGiftCardsAsynchThread step2storegiftcardsasynchthread = new Step2StoreGiftCardsAsynchThread(Step2_ManageCards.this,mStoreLocationId,UserDetails.mServiceUserId,mStoreID,getIntent().getExtras().getString("datasourcename"),hasGiftCardId);
				step2storegiftcardsasynchthread.execute("","","");	
			}else{  // If for buying giftcard check only credit card availability
				//Start Credit Card availability check asynchthread
				CardOnFilesAsynchThread cardonfilesasynchthread = new CardOnFilesAsynchThread(Step2_ManageCards.this);
				cardonfilesasynchthread.execute("","","");
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		try{
			switch(item.getGroupId()){
			case 0:	//Tips selection

				mTipValue.setText(item.getTitle());
				mChoosedTipAmount = item.getItemId();

				if(mAmountValue.getText().toString().trim().length() > 0 && !mChoosedGiftcardBalanceValue.equalsIgnoreCase("0") && (Double.valueOf(computeTotalAmount(mChoosedTipAmount))>Double.valueOf(mChoosedGiftcardBalanceValue))){
					mDifferntiateAlertMessage=1;
					alertBox_service("Information",getResources().getString(R.string.zpay_step2_giftcard_amount_alertmsg));
				}else{
					mTotalAmountText.setText("$"+String.format("%.2f", computeTotalAmount(mChoosedTipAmount)));
				}
				return true;
			case 1:	//Payment option selection
				mAmountValue.setFocusable(true);
                mAmountValue.setFocusableInTouchMode(true);
				mPaymentOptionsCardValue.setText(item.getTitle());
				mCreditCardPaymentValue.getText().clear();
				mBothCardOptionValue.getText().clear();
				if(mPaymentOptionsCardValue.getText().toString().trim().equals("Both")){/*(remove both card option)*/
					mAmountOnGiftcardLayout.setVisibility(View.VISIBLE);
					mCreditCardPaymentContainer.setVisibility(View.VISIBLE);
					mBalanceOnCreditCardLayout.setVisibility(View.VISIBLE);
					mAmountOnGiftcardValue.setEnabled(true);
					mAmountOnGiftcardValue.setFocusable(true);
					mAmountOnGiftcardValue.setFocusableInTouchMode(true);
					mBothCardOptionValue.setHint("Select Gift Card");
					mBalanceOnCreditCardValue.setText(mTotalAmountText.getText().toString().trim());
					// To set default giftcard card as selected if only one giftcard presents with balance greater than "0"
					if((mGetBundleValue.equalsIgnoreCase("zpay")&&hasGiftCardId==true) || (mGetBundleValue.equalsIgnoreCase("invoice_approval") && mInvoiceSourceBundle.equalsIgnoreCase("normal_payment"))||mInvoiceSourceBundle.equalsIgnoreCase("invoice_source")||mGetBundleValue.equalsIgnoreCase("point_of_sale")){
						// check for the available giftcard size whose balance is greater than "0"
						if(getGiftcardList().size()==1){
							final MyGiftCards_ClassVariables parsedobjectvalues = (MyGiftCards_ClassVariables)getGiftcardList().get(0);
							mChoosedGiftcardBalanceValue = parsedobjectvalues.balance_amount;
							mChoosedGiftcardID = parsedobjectvalues.id;
							mChoosedGiftCardPurchaseID = parsedobjectvalues.purchaseid;
							mBothCardOptionValue.setText(parsedobjectvalues.storename+" - "+"$"+parsedobjectvalues.balance_amount);
						}
					}else{
						// check for the available giftcard size whose balance is greater than "0"
						if(getGiftcardList().size()==1){
							final POJOAllGiftCards parsedobjectvalues = (POJOAllGiftCards) getGiftcardList().get(0);
							mChoosedGiftcardBalanceValue = parsedobjectvalues.mBalanceAmount;
							mChoosedGiftcardID = parsedobjectvalues.mGiftCardId;
							mChoosedGiftCardPurchaseID = parsedobjectvalues.mGiftcardPurchaseId;
							mBothCardOptionValue.setText(parsedobjectvalues.mStoreName+" - "+"$"+parsedobjectvalues.mBalanceAmount);
						}
					}
					// To set first credit card as selected if only one credit card exists... 
					if(WebServiceStaticArrays.mCardOnFiles.size()==1){
						final CardOnFiles_ClassVariables parsedobjectvalues = (CardOnFiles_ClassVariables) WebServiceStaticArrays.mCardOnFiles.get(0);
						mCreditCardPaymentValue.setText(parsedobjectvalues.cardMask);
						mSelectedCreditCardId = String.valueOf(parsedobjectvalues.cardId);
					}
				}else {/*(remove both card option)*/
					if(mPaymentOptionsCardValue.getText().toString().trim().equals("GiftCard/DealCard")){

						// To set default giftcard card as selected if only one giftcard presents with balance greater than "0"
						if((mGetBundleValue.equalsIgnoreCase("zpay")&&hasGiftCardId==true) || (mGetBundleValue.equalsIgnoreCase("invoice_approval") && mInvoiceSourceBundle.equalsIgnoreCase("normal_payment"))||mInvoiceSourceBundle.equalsIgnoreCase("invoice_source")||mGetBundleValue.equalsIgnoreCase("point_of_sale")){
							// check for the available giftcard size whose balance is greater than "0"
							if(getGiftcardList().size()==1){
								final MyGiftCards_ClassVariables parsedobjectvalues = (MyGiftCards_ClassVariables)getGiftcardList().get(0);
								if(Double.parseDouble(parsedobjectvalues.balance_amount)<Double.parseDouble(mTotalAmountText.getText().toString().substring(1,mTotalAmountText.getText().toString().trim().length()))){
									alertBox_service("Information", getResources().getString(R.string.zpay_step2_giftcard_alertmsg));
								}else{
									mChoosedGiftcardBalanceValue = parsedobjectvalues.balance_amount;
									mChoosedGiftcardID = parsedobjectvalues.id;
									mChoosedGiftCardPurchaseID = parsedobjectvalues.purchaseid;
									mBothCardOptionValue.setText(parsedobjectvalues.storename+" - "+"$"+parsedobjectvalues.balance_amount);	
								}
							}
						}else{
							// check for the available giftcard size whose balance is greater than "0"
							if(getGiftcardList().size()==1){
								final POJOAllGiftCards parsedobjectvalues = (POJOAllGiftCards) getGiftcardList().get(0);
								if(Double.parseDouble(parsedobjectvalues.mBalanceAmount)<Double.parseDouble(mTotalAmountText.getText().toString().substring(1,mTotalAmountText.getText().toString().trim().length()))){
									alertBox_service("Information", getResources().getString(R.string.zpay_step2_giftcard_alertmsg));
								}else{
									mChoosedGiftcardBalanceValue = parsedobjectvalues.mBalanceAmount;
									mChoosedGiftcardID = parsedobjectvalues.mGiftCardId;
									mChoosedGiftCardPurchaseID = parsedobjectvalues.mGiftcardPurchaseId;
									mBothCardOptionValue.setText(parsedobjectvalues.mStoreName+" - "+"$"+parsedobjectvalues.mBalanceAmount);	
								}
							}
						}
					}else {
						// To set first credit card as selected if only one credit card exists...
						if(WebServiceStaticArrays.mCardOnFiles.size()==1){
							final CardOnFiles_ClassVariables parsedobjectvalues = (CardOnFiles_ClassVariables) WebServiceStaticArrays.mCardOnFiles.get(0);
							mBothCardOptionValue.setText(parsedobjectvalues.cardMask);
							mSelectedCreditCardId = String.valueOf(parsedobjectvalues.cardId);
						}
					}
					mAmountOnGiftcardLayout.setVisibility(View.GONE);
					mCreditCardPaymentContainer.setVisibility(View.GONE);
					mBalanceOnCreditCardLayout.setVisibility(View.GONE);
				}/*(remove both card option)*/	
				return true;
			case 2:	//Credit Card selection
				mAmountValue.setFocusable(true);
                mAmountValue.setFocusableInTouchMode(true);
				mCreditCardPaymentValue.setText(item.getTitle());
				mSelectedCreditCardId = String.valueOf(item.getItemId());
				return true;
			case 3:	//Gift Card selection
                mAmountValue.setFocusable(true);
                mAmountValue.setFocusableInTouchMode(true);
				mDifferntiateAlertMessage=0;
				final Step2AvailableCards parsedobjectvalues = (Step2AvailableCards) mTotalCards.get(item.getItemId());
				mChoosedGiftcardBalanceValue = parsedobjectvalues.mGiftCardBalanceAmount;
				mChoosedGiftcardID = parsedobjectvalues.mGiftCardId;
				mChoosedGiftCardPurchaseID = parsedobjectvalues.mGiftCardPurchaseId;
				mZpayPaymentType = "giftcard";

				String totalamount = mTotalAmountText.getText().toString().substring(1, mTotalAmountText.getText().toString().trim().length());
				if(Double.parseDouble(totalamount)<=Double.parseDouble(mChoosedGiftcardBalanceValue)){
					mBothCardOptionValue.setText(item.getTitle());
				}else{
					mChoosedGiftcardBalanceValue = "0";
					mChoosedGiftcardID = ""; 
					mChoosedGiftCardPurchaseID = "";
					mZpayPaymentType = "";
					alertBox_service("Information", getResources().getString(R.string.zpay_step2_giftcard_alertmsg));
				}

				if(mAmountValue.getText().toString().trim().length()>0 && mAmountValue.getText().toString().trim().equalsIgnoreCase("0.00")){
					mTipValue.setText("0%");
					mChoosedTipAmount=0;
				}
				return true;
			case 4:	// credit card option in payment card selection
				mAmountValue.setFocusable(true);
                mAmountValue.setFocusableInTouchMode(true);
				mBothCardOptionValue.setText(item.getTitle());
				mSelectedCreditCardId = String.valueOf(item.getItemId());
				mZpayPaymentType = "creditcard";

				mChoosedGiftcardBalanceValue = "0";
				mChoosedGiftcardID = ""; 
				mChoosedGiftCardPurchaseID = "";

				if(mAmountValue.getText().toString().trim().length()>0 && mAmountValue.getText().toString().trim().equalsIgnoreCase("0.00")){
					mTipValue.setText("0%");
					mChoosedTipAmount=0;
				}

				return true;
			case 5: // for only credit card option
				mAmountValue.setFocusable(true);
                mAmountValue.setFocusableInTouchMode(true);
				mOnlyCreditCardValue.setText(item.getTitle());
				mSelectedCreditCardId = String.valueOf(item.getItemId());
				return true;
			default :
				return super.onContextItemSelected(item);
			}
		}catch(NumberFormatException e){
			e.printStackTrace();
			return super.onContextItemSelected(item);
		}catch(Exception e){
			e.printStackTrace();
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.clear();
		if(v.equals(mTipContextImage)==true){
			menu.setHeaderTitle("Tips");
			menu.add(0, 0, 0, "0%");
			menu.add(0, 5, 0, "5%");
			menu.add(0, 10, 0, "10%");
			menu.add(0, 15, 0, "15%");
			menu.add(0, 20, 0, "20%");
			menu.add(0, 25, 0, "25%");
		}else if(v.equals(mPaymentOptionsCardContextImage)==true){
			menu.setHeaderTitle("Payment Options");
			menu.add(1, v.getId(), 0, "CreditCard");
			menu.add(1, v.getId(), 0, "GiftCard/DealCard");
			//menu.add(1, v.getId(), 0, "Both");
		}else if(v.equals(mBothCardOptionContextImage)==true){

			menu.setHeaderTitle("Select Card");
			if(mTotalCards.size()>0){
				for(int i=0;i<mTotalCards.size();i++){
					final Step2AvailableCards parsedobjectvalues = (Step2AvailableCards) mTotalCards.get(i);
					if(!parsedobjectvalues.mCreditCardMask.equalsIgnoreCase("")){
						menu.add(4, Integer.parseInt(parsedobjectvalues.mCreditCardId), 0, parsedobjectvalues.mCreditCardMask);
					}else{
						if(!parsedobjectvalues.mGiftCardBalanceAmount.equalsIgnoreCase("0")){
							menu.add(3, i, 0, parsedobjectvalues.mGiftCard);
						}
					}
				}
			}else{
				Toast.makeText(getApplicationContext(), "No Credit Cards Available", Toast.LENGTH_SHORT).show();
			}
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	private void contextMenuOpen(View sender){
		sender.setLongClickable(false);
		registerForContextMenu(sender);
		openContextMenu(sender);
	}

	//Function to Change View depends on initial sync of giftcards and credit card
	public static void ViewHandler(){
		mTotalCards.clear();
		String CreditcardAvailableStatus = GetCreditCardAvailableStatus();
		String GiftcardAvailableStatus = GetGiftCardAvailableStatus();
		mAmountValue.setFocusable(true);
        mAmountValue.setFocusableInTouchMode(true);
		if(mGetBundleValue.equalsIgnoreCase("zpay") ||(mGetBundleValue.equalsIgnoreCase("invoice_approval") && mInvoicePaymentType.equalsIgnoreCase("normal_payment"))||mGetBundleValue.equalsIgnoreCase("point_of_sale")){ // If for mobile payment 
			if(GiftcardAvailableStatus.equalsIgnoreCase("GiftCards Available") && CreditcardAvailableStatus.equalsIgnoreCase("CreditCards Available")){
				// Both giftcards and credit cards available	
				mAddCardLayout.setVisibility(View.GONE);
				mCreditCardOnlyContainer.setVisibility(View.GONE);
				mBothCardPaymentLayout.setVisibility(View.VISIBLE);

				//Visible new cardoption textview and dropdown box in mobile pay step2
				mBothCardOptionsContainer.setVisibility(View.VISIBLE);
				mBothCardOptionText.setVisibility(View.VISIBLE);

				if(hasGiftCardId){ // If control from leftmenu giftcards--> selfUse --> should show selected giftcard
					mPaymentOptionsCardValue.setText("GiftCard/DealCard");
					mBothCardOptionValue.setText(mChoosedStoreName+" - "+"$"+mChoosedGiftcardBalanceValue);
					
				}
			}else if(GiftcardAvailableStatus.equalsIgnoreCase("GiftCards Available") && CreditcardAvailableStatus.equalsIgnoreCase("CreditCards Not Available")){
				// Only Giftcards available
				mAddCardLayout.setVisibility(View.GONE);
				mCreditCardOnlyContainer.setVisibility(View.GONE);
				mBothCardPaymentLayout.setVisibility(View.VISIBLE);

				//Visible new cardoption textview and dropdown box in mobile pay step2
				mBothCardOptionsContainer.setVisibility(View.VISIBLE);
				mBothCardOptionText.setVisibility(View.VISIBLE);
				
				if(hasGiftCardId){
					mPaymentOptionsCardValue.setText("GiftCard/DealCard");
					mBothCardOptionValue.setText(mChoosedStoreName+" - "+"$"+mChoosedGiftcardBalanceValue);
				}else if(mTotalCards.size()==1){
					final Step2AvailableCards parsedobjectvalues = (Step2AvailableCards) mTotalCards.get(0);
					mBothCardOptionValue.setText(parsedobjectvalues.mGiftCard);
					mChoosedGiftcardBalanceValue = parsedobjectvalues.mGiftCardBalanceAmount;
					mChoosedGiftcardID = parsedobjectvalues.mGiftCardId;
					mChoosedGiftCardPurchaseID = parsedobjectvalues.mGiftCardPurchaseId;
					mZpayPaymentType = "giftcard";
				}
			}else if(GiftcardAvailableStatus.equalsIgnoreCase("GiftCards Not Available") && CreditcardAvailableStatus.equalsIgnoreCase("CreditCards Available")){
				// Only Creditcards available	
				if(mTotalCards.size()==1){
					final Step2AvailableCards parsedobjectvalues = (Step2AvailableCards) mTotalCards.get(0);
					mBothCardOptionValue.setText(parsedobjectvalues.mCreditCardMask);
					mSelectedCreditCardId = String.valueOf(parsedobjectvalues.mCreditCardId);
					mZpayPaymentType = "creditcard";
				}

				mAddCardLayout.setVisibility(View.GONE);
				mCreditCardOnlyContainer.setVisibility(View.VISIBLE);
				mBothCardPaymentLayout.setVisibility(View.GONE);

				//Visible new card option textview and dropdown box in mobile pay step2
				mBothCardOptionsContainer.setVisibility(View.VISIBLE);
				mBothCardOptionText.setVisibility(View.VISIBLE);
			}else if(GiftcardAvailableStatus.equalsIgnoreCase("GiftCards Not Available") && CreditcardAvailableStatus.equalsIgnoreCase("CreditCards Not Available")){
				// No cards available	
				mAddCardLayout.setVisibility(View.VISIBLE);
				mCreditCardOnlyContainer.setVisibility(View.GONE);
				mBothCardPaymentLayout.setVisibility(View.GONE);

				//Gone new cardoption textview and dropdown box in mobile pay step2
				mBothCardOptionsContainer.setVisibility(View.GONE);
				mBothCardOptionText.setVisibility(View.GONE);
			}
		}else{ // For buying giftcard
			if(CreditcardAvailableStatus.equalsIgnoreCase("CreditCards Available")){
				// Only CreditCard available	
				if(mTotalCards.size()==1){
					final Step2AvailableCards parsedobjectvalues = (Step2AvailableCards) mTotalCards.get(0);
					mBothCardOptionValue.setText(parsedobjectvalues.mCreditCardMask);
					mSelectedCreditCardId = String.valueOf(parsedobjectvalues.mCreditCardId);
					mZpayPaymentType = "creditcard";
				}

				mAddCardLayout.setVisibility(View.GONE);
				mCreditCardOnlyContainer.setVisibility(View.VISIBLE);
				mBothCardPaymentLayout.setVisibility(View.GONE);

				//Visible new cardoption textview and dropdown box in mobile pay step2
				mBothCardOptionsContainer.setVisibility(View.VISIBLE);
				mBothCardOptionText.setVisibility(View.VISIBLE);

			}else if(CreditcardAvailableStatus.equalsIgnoreCase("CreditCards Not Available")){
				// Credit card not available
				mAddCardLayout.setVisibility(View.VISIBLE);
				mCreditCardOnlyContainer.setVisibility(View.GONE);
				mBothCardPaymentLayout.setVisibility(View.GONE);

				//Gone new cardoption textview and dropdown box in mobile pay step2
				mBothCardOptionsContainer.setVisibility(View.GONE);
				mBothCardOptionText.setVisibility(View.GONE);
			}
		}
	}

	private static String GetGiftCardAvailableStatus(){
		String mGiftCardAvailableStatus = "";
		boolean balanceCheck = false;
		try{
			if((mGetBundleValue.equalsIgnoreCase("zpay")&&hasGiftCardId==true) || (mGetBundleValue.equalsIgnoreCase("invoice_approval") && mInvoiceSourceBundle.equalsIgnoreCase("normal_payment"))||mInvoiceSourceBundle.equalsIgnoreCase("invoice_source")||mGetBundleValue.equalsIgnoreCase("point_of_sale")){
				for(int i=0;i<WebServiceStaticArrays.mMyGiftCards.size();i++){
					final MyGiftCards_ClassVariables mygiftcard_details = (MyGiftCards_ClassVariables) WebServiceStaticArrays.mMyGiftCards.get(i);
					if(!mygiftcard_details.message.equals("")/*&&parsedobjectvalues.message.equals("Search type or term not available !")*/){
						mGiftCardAvailableStatus = "GiftCards Not Available";
					}else{
						if(Double.parseDouble(mygiftcard_details.balance_amount) > 0){
							balanceCheck = true;
						}
						mGiftCardAvailableStatus = "GiftCards Available";
						Step2AvailableCards obj = new Step2AvailableCards();
						obj.mGiftCard = mygiftcard_details.storename+" - $"+mygiftcard_details.balance_amount;
						obj.mGiftCardId = mygiftcard_details.id;
						obj.mGiftCardPurchaseId = mygiftcard_details.purchaseid;
						obj.mGiftCardBalanceAmount = mygiftcard_details.balance_amount;
						mTotalCards.add((mTotalCards.size()),obj);
					}
				}
				if(balanceCheck == false || WebServiceStaticArrays.mMyGiftCards.size()==0){
					mGiftCardAvailableStatus = "GiftCards Not Available";
				}			
			}else{
				for(int i=0;i<WebServiceStaticArrays.mAllGiftCardList.size();i++){
					final POJOAllGiftCards getallgiftcards = (POJOAllGiftCards) WebServiceStaticArrays.mAllGiftCardList.get(i);
					if(!POJOAllGiftCards.GET_ALL_GIFT_CARDS_MESSAGE.equals("")/*&&parsedobjectvalues.message.equals("Search type or term not available !")*/){
						mGiftCardAvailableStatus = "GiftCards Not Available";
					}else{
						if(Double.parseDouble(getallgiftcards.mBalanceAmount) > 0){
							balanceCheck = true;
						}
						mGiftCardAvailableStatus = "GiftCards Available";
						Step2AvailableCards obj = new Step2AvailableCards();
						obj.mGiftCard = getallgiftcards.mStoreName+" - $"+getallgiftcards.mBalanceAmount;
						obj.mGiftCardId = getallgiftcards.mGiftCardId;
						obj.mGiftCardPurchaseId = getallgiftcards.mGiftcardPurchaseId;
						obj.mGiftCardBalanceAmount = getallgiftcards.mBalanceAmount;
						if(!obj.mGiftCardBalanceAmount.equalsIgnoreCase("0")){
							mTotalCards.add((mTotalCards.size()),obj);
						}
					}
				}
				if(balanceCheck == false || WebServiceStaticArrays.mMyGiftCards.size()==0){
					mGiftCardAvailableStatus = "GiftCards Not Available";
				}
			}
		}catch (Exception e) {
			mGiftCardAvailableStatus = "GiftCards Not Available";
			e.printStackTrace();
		}
		return mGiftCardAvailableStatus;
	}

	private static String GetCreditCardAvailableStatus(){
		String mCreditCardAvailableStatus = "";
		try{
			for(int i=0;i<WebServiceStaticArrays.mCardOnFiles.size();i++){
				final CardOnFiles_ClassVariables parsedobjectvalues = (CardOnFiles_ClassVariables) WebServiceStaticArrays.mCardOnFiles.get(i);
				if(!parsedobjectvalues.message.equals("")){
					mCreditCardAvailableStatus ="CreditCards Not Available";
				}else{
					mCreditCardAvailableStatus="CreditCards Available";
					Step2AvailableCards obj = new Step2AvailableCards();
					obj.mCreditCardId = parsedobjectvalues.cardId;
					obj.mCreditCardMask = parsedobjectvalues.cardMask;
					mTotalCards.add(obj);
				}
			}
			if(WebServiceStaticArrays.mCardOnFiles.size()==0){
				mCreditCardAvailableStatus ="CreditCards Not Available";
			}
			return mCreditCardAvailableStatus;
		}catch(Exception e){
			e.printStackTrace();
			return mCreditCardAvailableStatus="CreditCards Not Available";
		}
	}

	private void setMenuBarText(){
		mMenuBarNext.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.skip), null, null);
		if(getIntent().getExtras().getString("datasourcename").equals("zpay")){	// Loop success if control from righnavigation selfuse in Giftcards/Deals
			mMenuBarNext.setText("Generate QR");
			mMenuBarNext.setVisibility(View.INVISIBLE);
			mGenerateQR.setVisibility(View.VISIBLE);
		}else {
			mMenuBarNext.setVisibility(View.INVISIBLE);
			mGenerateQR.setVisibility(View.VISIBLE);
			mMenuBarNext.setText("Authorize");
			mGenerateQR.setText("Authorize");
		}
	}

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's width.
	 */
	static class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View menu;
		Button mFreezeView;
		EditText mAmountField;
		/**
		 * Menu must NOT be out/shown to start with.
		 */

		public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu,Button freezeview , EditText AmountField) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
			this.mFreezeView=freezeview;
			this.mAmountField = AmountField;
		}

		@Override
		public void onClick(View v) {

			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);

			Context context = menu.getContext();
			String msg = "Slide " + new Date();
			System.out.println(msg);

			int menuWidth = menu.getMeasuredWidth();
			Log.i(TAG,"Menu Width : "+ menuWidth);

			// Ensure menu is visible
			menu.setVisibility(View.VISIBLE);

			InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


			// To clear focus of Amount Field while opening left menu
			if(mAmountField != null){
				mAmountField.clearFocus();
			}

			if (!MenuOutClass.STEP2_MANAGECARDS_MENUOUT) {
				// Scroll to 0 to reveal menu
				int left = 0;
				this.mFreezeView.setVisibility(View.VISIBLE);
				scrollView.smoothScrollTo(left, 0);
			} else {
				// Scroll to menuWidth so menu isn't on screen.
				int left = menuWidth;
				this.mFreezeView.setVisibility(View.GONE);
				scrollView.smoothScrollTo(left, 0);
			}
			MenuOutClass.STEP2_MANAGECARDS_MENUOUT = !MenuOutClass.STEP2_MANAGECARDS_MENUOUT;
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

	// To check the size of giftcards available whose balance is greater than "0"
	public ArrayList<Object> getGiftcardList(){
		ArrayList<Object> mTempGiftcardArray = new ArrayList<Object>();
		if((mGetBundleValue.equalsIgnoreCase("zpay")&&hasGiftCardId==true) || (mGetBundleValue.equalsIgnoreCase("invoice_approval") && mInvoiceSourceBundle.equalsIgnoreCase("normal_payment"))||mInvoiceSourceBundle.equalsIgnoreCase("invoice_source")||mGetBundleValue.equalsIgnoreCase("point_of_sale")){
			for(int i=0;i<WebServiceStaticArrays.mMyGiftCards.size();i++){
				final MyGiftCards_ClassVariables parsedobjectvalues = (MyGiftCards_ClassVariables) WebServiceStaticArrays.mMyGiftCards.get(i);
				if(!parsedobjectvalues.message.equals("")){
					Toast.makeText(getApplicationContext(), "No Gift Cards Available", Toast.LENGTH_SHORT).show();
				}else{
					if(!parsedobjectvalues.balance_amount.equalsIgnoreCase("0")/*||!parsedobjectvalues.balance_amount.equalsIgnoreCase("0.00")||!parsedobjectvalues.balance_amount.equalsIgnoreCase("0.0")||!parsedobjectvalues.balance_amount.equalsIgnoreCase("00.00")*/){
						mTempGiftcardArray.add(parsedobjectvalues);
					}
				}
			}
		}else{
			for(int i=0;i<WebServiceStaticArrays.mAllGiftCardList.size();i++){
				final POJOAllGiftCards parsedobjectvalues = (POJOAllGiftCards) WebServiceStaticArrays.mAllGiftCardList.get(i);
				if(POJOAllGiftCards.GET_ALL_GIFT_CARDS_MESSAGE.length()>1){
					Toast.makeText(getApplicationContext(), "No Gift Cards Available", Toast.LENGTH_SHORT).show();
				}else{
					if(!parsedobjectvalues.mBalanceAmount.equalsIgnoreCase("0")/*||!parsedobjectvalues.mBalanceAmount.equalsIgnoreCase("0.00")||!parsedobjectvalues.mBalanceAmount.equalsIgnoreCase("0.0")||!parsedobjectvalues.mBalanceAmount.equalsIgnoreCase("00.00")*/){
						mTempGiftcardArray.add(parsedobjectvalues);
					}
				}
			}
		}
		return mTempGiftcardArray;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		/*if(hasFocus){
			mAmountValue.setFocusable(false);
			mAmountValue.setFocusableInTouchMode(true);
			//mAmountValue.requestFocus();
		}*/

		imageviewWidth = mQRCodeImage.getWidth();
		imageviewHeight = mQRCodeImage.getHeight();
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		finish();
		Log.i(TAG,Messages.getString("SlidingView.21")); //$NON-NLS-1$
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
		new RefreshZoupons().isApplicationGoneBackground(Step2_ManageCards.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}

	@Override
	protected void onResume() {
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(Step2_ManageCards.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(Step2_ManageCards.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(Step2_ManageCards.this);
		mLogoutSession.setLogoutTimerAlarm();

		if(mAmountValue.getVisibility()!=View.VISIBLE){
			//code for hiding the keyboard while activity is loading.
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		}
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
	public void onRestart() {
		super.onRestart();
		Log.i(TAG,Messages.getString("SlidingView.24")); //$NON-NLS-1$
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i(TAG,Messages.getString("SlidingView.25")); //$NON-NLS-1$
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i(TAG,Messages.getString("SlidingView.26")); //$NON-NLS-1$
	}

	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(Step2_ManageCards.this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);

		if(msg.equalsIgnoreCase("Please add a credit card for payment.")){
			service_alert.setPositiveButton("Add Card", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent_addcard = new Intent(Step2_ManageCards.this,AddCardInformation.class);
					intent_addcard.putExtra("class_name", TAG);
					startActivityForResult(intent_addcard,100);
				};
			});
			service_alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				};
			});

		}else{
			service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

					if(msg.equalsIgnoreCase("The entered amount is not valid for payment process")){
						//To close keyboard if it is in open state
						InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
						inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

						mChoosedTipAmount = 0;
						mTipValue.setText("0%");
						mTotalAmountText.setText("$0.00");
					}else if(msg.equalsIgnoreCase("Please choose a card for payment.")){
						mAmountValue.getText().clear();
						mAmountValue.setText("00.00");
					}else if(msg.equalsIgnoreCase("Please enter the transaction amount")){
						//To close keyboard if it is in open state
						InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
						inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

						mAmountValue.requestFocus();
					}else if(msg.equalsIgnoreCase("Please select a Giftcard for payment.")){
						//To close keyboard if it is in open state
						InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
						inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

						contextMenuOpen(mBothCardOptionValue);
						mAmountOnGiftcardValue.removeTextChangedListener(Step2_ManageCards.this);
						mAmountOnGiftcardValue.getText().clear();
						mAmountOnGiftcardValue.addTextChangedListener(Step2_ManageCards.this);
						mAmountOnGiftcardValue.requestFocus();
					}else if(msg.equalsIgnoreCase(getResources().getString(R.string.zpay_step2_giftcard_amount_alertmsg))){
						//To close keyboard if it is in open state
						InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
						inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
						if(mDifferntiateAlertMessage==0){
							mAmountValue.setText("00.00");
							mAmountValue.requestFocus();
							mTipValue.setText("0%");
							mTotalAmountText.setText("$0.00");
							mChoosedTipAmount=0;
						}else{
							mTipValue.setText("0%");
							mChoosedTipAmount=0;
						}
					}else if(msg.equalsIgnoreCase(getResources().getString(R.string.zpay_step2_giftcard_amount_alertmsg_new))){
						//To close keyboard if it is in open state
						InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
						inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

						mAmountOnGiftcardValue.removeTextChangedListener(Step2_ManageCards.this);
						mAmountOnGiftcardValue.getText().clear();
						mAmountOnGiftcardValue.addTextChangedListener(Step2_ManageCards.this);
						mAmountOnGiftcardValue.requestFocus();

					}else if(msg.equalsIgnoreCase("You have sufficient balance in your GiftCard, so you don't need to use Credit card for payment.")){
						mAmountOnGiftcardValue.removeTextChangedListener(Step2_ManageCards.this);
						mAmountOnGiftcardValue.getText().clear();
						mAmountOnGiftcardValue.addTextChangedListener(Step2_ManageCards.this);
						mAmountOnGiftcardValue.requestFocus();
					}
				}
			});
		}
		service_alert.show();
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			try{
				Log.i(TAG,"OnReceive");
				if(intent.hasExtra("FromNotification")){
					if(NotificationDetails.notificationcount>0){
						if(header != null && header.mTabBarNotificationCountBtn != null){
							header.mTabBarNotificationCountBtn.setVisibility(View.VISIBLE);
							header.mTabBarNotificationCountBtn.setText(String.valueOf(NotificationDetails.notificationcount));	
						}else{
							mNotificationCount.setVisibility(View.VISIBLE);
							mNotificationCount.setText(String.valueOf(NotificationDetails.notificationcount));
						}
					}else{
						if(header != null && header.mTabBarNotificationCountBtn != null){
							header.mTabBarNotificationCountBtn.setVisibility(View.GONE);
							Log.i("Notification result", "No new notification");
						}else{
							mNotificationCount.setVisibility(View.GONE);
							Log.i("Notification result", "No new notification");	
						}
					}
				}					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};

	@Override
	public void afterTextChanged(Editable s) {}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		try{
			if(s.toString().length()==1){ // Initial formation of string
				mAmountOnGiftcardValue.removeTextChangedListener(this);
				mAmountOnGiftcardValue.setCursorVisible(false);
				mAmountOnGiftcardValue.setText("0.0"+s);
				mAmountOnGiftcardValue.setSelection(mAmountOnGiftcardValue.getText().toString().length());
				mAmountOnGiftcardValue.addTextChangedListener(this);
			}else{
				mAmountOnGiftcardValue.removeTextChangedListener(this);
				String computedAmount = s.toString().replace(".", "");
				String mActualAmount = computedAmount.substring(0, computedAmount.length()-2);
				String Amount = mActualAmount+"."+computedAmount.substring(computedAmount.length()-2,computedAmount.length());
				if(Amount.charAt(0) == '0' && Amount.length()>3){ // To replace extra "0" from first position
					Amount = Amount.replaceFirst("0", "");
				}else if(Amount.length() <= 3){
					Amount = String.format("%.2f",Float.valueOf(Amount));	
				}
				mAmountOnGiftcardValue.setText(Amount);
				mAmountOnGiftcardValue.setSelection(mAmountOnGiftcardValue.getText().toString().length());
				mAmountOnGiftcardValue.addTextChangedListener(this);
			}
			if(mAmountValue.isFocused() && mBothCardOptionValue.getText().toString().trim().length() == 0 && mAmountValue.getText().toString().trim().length()>0 && !mAmountValue.getText().toString().trim().equalsIgnoreCase("0.00")){ // If no giftcard choosed 
				alertBox_service("Information","Please choose a card for payment.");
			}else{ // If giftcard choosed and check with the avialable balance
				if(mAmountValue.getText().toString().trim().length() > 0 && !mChoosedGiftcardBalanceValue.equalsIgnoreCase("0") && (Double.valueOf(mTotalAmountText.getText().toString().substring(1,mTotalAmountText.getText().toString().trim().length()).trim())>Double.valueOf(mChoosedGiftcardBalanceValue))){
					alertBox_service("Information",getResources().getString(R.string.zpay_step2_giftcard_amount_alertmsg_new));
				}else{
				}	
			}
		}catch(NumberFormatException e){
			e.printStackTrace();
			Log.i(TAG,"Number Format Exception");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
