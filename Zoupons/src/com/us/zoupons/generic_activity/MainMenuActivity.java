/**
 * 
 */
package com.us.zoupons.generic_activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.us.zoupons.AdvertisementTimerTask;
import com.us.zoupons.DecodeImageWithRotation;
import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.MobileNumberFilter;
import com.us.zoupons.MobileNumberTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NotificationTabImageVisibility;
import com.us.zoupons.R;
import com.us.zoupons.android.asyncthread_class.AddorRemoveFavoriteAsynchThread;
import com.us.zoupons.android.asyncthread_class.FavoriteCouponsAsynchThread;
import com.us.zoupons.android.asyncthread_class.FavouritesTaskLoader;
import com.us.zoupons.android.asyncthread_class.GetSocialFriendsTask;
import com.us.zoupons.android.asyncthread_class.PhotoSwitcherAsynchThread;
import com.us.zoupons.android.asyncthread_class.PlayVideoClass;
import com.us.zoupons.android.asyncthread_class.ShareStoreAsynchThread;
import com.us.zoupons.android.asyncthread_class.StoreDetailsAsyncTask;
import com.us.zoupons.backgroundservices.ContactListService;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.POJOFBfriendListData;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.POJOStoreInfo;
import com.us.zoupons.classvariables.POJOStorePhoto;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.listview_inflater_classes.CustomCardDetailsAdapter;
import com.us.zoupons.listview_inflater_classes.CustomFBFriendListAdapter;
import com.us.zoupons.listview_inflater_classes.Favorites_Adapter;
import com.us.zoupons.login.ZouponsLogin;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.mobilepay.MobilePay;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.notification.UnreadToReadMessage;
import com.us.zoupons.shopper.cards.FBNotesDescription;
import com.us.zoupons.shopper.cards.FriendList;
import com.us.zoupons.shopper.cards.StoreCardDetails;
import com.us.zoupons.shopper.chatsupport.ChatSupportListAdapter;
import com.us.zoupons.shopper.chatsupport.ChatSupportNotificationsAdapter;
import com.us.zoupons.shopper.chatsupport.CommunicationTimerTask;
import com.us.zoupons.shopper.chatsupport.ContactUsResponseTask;
import com.us.zoupons.shopper.chatsupport.ContactUsTask;
import com.us.zoupons.shopper.chatsupport.CustomChatScrollListner;
import com.us.zoupons.shopper.chatsupport.GetCustomerCommunicatedStoreAsyncTask;
import com.us.zoupons.shopper.chatsupport.ZouponsSupport;
import com.us.zoupons.shopper.coupons.CouponListAdapter;
import com.us.zoupons.shopper.coupons.CouponsTask;
import com.us.zoupons.shopper.friends.Friends;
import com.us.zoupons.shopper.giftcards_deals.GiftCardTransactionHistoryTask;
import com.us.zoupons.shopper.giftcards_deals.SendGiftCardTask;
import com.us.zoupons.shopper.photos.StorePhotoAdapter;
import com.us.zoupons.shopper.photos.StorePhotoGridAdapter;
import com.us.zoupons.shopper.photos.StorePhotoSwitcher;
import com.us.zoupons.shopper.photos.UnderlinePageIndicator;
import com.us.zoupons.shopper.refer_store.AddRewardsTask;
import com.us.zoupons.shopper.refer_store.CheckStoreNameTask;
import com.us.zoupons.shopper.refer_store.RewardsAdvertisementTask;
import com.us.zoupons.shopper.refer_store.TermsAndConditions;
import com.us.zoupons.shopper.reviews.GetReviewRatesStatusTask;
import com.us.zoupons.shopper.reviews.GetStoreReviewDetails;
import com.us.zoupons.shopper.reviews.POJOStoreReviewDetails;
import com.us.zoupons.shopper.reviews.PostReviewTask;
import com.us.zoupons.shopper.reviews.RatingReviewLikeDisLikeTask;
import com.us.zoupons.shopper.reviews.StoreReviewListAdapter;
import com.us.zoupons.shopper.share.SearchFriendsTextWatcher;
import com.us.zoupons.shopper.store_info.StoreInformation;
import com.us.zoupons.shopper.store_info.StoreInformation.BusinessTimeClass;
import com.us.zoupons.shopper.store_info.StoreInformation.StoreInformationClass;
import com.us.zoupons.shopper.video.StoreVideoDialog;

/**
 *	Activity to Load Left Navigation Main Menu bar.
 */
public class MainMenuActivity extends Activity {

	public static MyHorizontalScrollView sScrollView;
	public static View sLeftMenu,sRightMenu;
	public static Button mMainMenuActivityFreezeView;
	private View app;
	private ImageView btnSlide;
	private Typeface mZouponsFont;
	public static String TAGG = "MainMenuActivity_";
	private ProgressDialog progressdialog=null;
	private Thread loadThread=null;
	private int MenuFlag_Favorites=0;
	public View[] children;
	private LayoutInflater inflater;
	public static LinearLayout mHome,mZpay,mInvoiceCenter,mZGiftcards,mManageCards,mReceipts,mMyFavourites,mMyFriends,mChat,mRewards,mSettings,mLogout;
	public static TextView mHomeText,mZpayText,mInvoiceCenterText,mGiftCardsText,mManageCardsText,mReceiptsText,mMyFavoritesText,mMyFriendsText,mChatText,mRewardsText,mSettingsText,mLogoutText;
	public static ImageView mHomeImage,mZpayImage,mInvoiceCenterImage,mGiftCardsImage,mManageCardsImage,mReceiptsImage,mMyFavoritesImage,mMyFriendsImage,mChatImage,mRewardsImage,mSettingsImage;
	public static LinearLayout mStoreImage;
	public static LinearLayout mInfo_RightMenu,mMobilePay_RightMenu,mGiftCards_RightMenu,mDeals_RightMenu,mCoupons_RightMenu,mSocial_RightMenu,mReviews_RightMenu,mLocations_RightMenu,mPhotos_RightMenu,mVideos_RightMenu,mContactStore_RightMenu,mFavorite_RightMenu;
	public static TextView mStoreName_RightMenu,mStoreLocation_RightMenu,mInfoText_RightMenu,mMobilePayText_RightMenu,mGiftCardsText_RightMenu,mDealsText_RightMenu,mCouponsText_RightMenu,mSocialText_RightMenu,mReviewsText_RightMenu,mLocationsText_RightMenu,mPhotosText_RightMenu,mVideosText_RightMenu,mContactStoreText_RightMenu,mFavoriteText_RightMenu;
	public static ImageView /*mStoreImage_RightMenu,*/mStoreInformation_RightMenu;
	public static ImageView mInfoImage_RightMenu,mMobilePayImage_RightMenu,mGiftCardsImage_RightMenu,mDealsImage_RightMenu,mCouponsImage_RightMenu,mSocialImage_RightMenu,mReviewsImage_RightMenu,mLocationsImage_RightMenu,mPhotosImage_RightMenu,mVideosImage_RightMenu,mContactStoreImage_RightMenu,mFavoriteImage_RightMenu;
	private Bundle bundle_mainactivity;

	/**
	 * Favorites variables intialization
	 * */
	private int mFavoritesScreenWidth;
	private double mFavoritesMenuWidth;
	private LinearLayout mMenuBarFavStores,mMenuBarFavCoupons,mMenuBarFriendsFavStores;
	private TextView mMenuBarFavStoresText,mMenuBarFavCouponsText,mMenuBarFriendsFavStoresText;
	public static ListView mFavoritesListView;
	private ListView mCouponsListView; 
	public static int storeposition = -1;
	public static boolean mIsfriendfavourite;
	// favourite listview offset parameters
	public static String mFavouritesStart= "0",mFavouritesEnd="20",mFavouritesTotalCount = "0";
	private boolean mIsFavouriteStore;

	/**
	 * Rewards Variables intialization
	 * */
	private Button mFirstRateButton,mSecondRateButton,mThirdRateButton,mFourthRateButton,mFifthRateButton; 
	private TextView mLeftFoooterView,mRightFooterView,mNameDetails,mAddressDetails,mPhotoDetails,mReviewDetails,mZouponsTermsAndConditions;
	private ImageView mVideoThumbnail,mVideoPlay,mStateContextMenuImage,mStorePhoto1,mStorePhoto2,mStoreAddPhoto1,mStoreAddPhoto2;
	private RelativeLayout mRewardsStartUpLayout,mRewardsFooterLayout,mRewardsStoreDetailsContainer,mZouponsTermsAndConditionsLayout,
	mRewardsVideoViewLayout,mRewardsStorePhoto1Container,mRewardsStorePhoto2Container;
	private LinearLayout mRewardsStepLayout,mRewardsStoreDetails,mRewardsStoreAddressDetails,mRewardsStorePhotoDetails,mRewardsStoreReviewDetails,
	mRewardsStoreReviewRateLayout,mRewardsStoreStateZipContainer;
	private FrameLayout mRewardsStoreStateLayout;
	private EditText mRewardsStoreName,mRewardsStoreManagerName,mRewardsTelephoneNumber,mRewardsStoreAddress1,mRewardsStoreAddress2,mRewardsStoreCity,
	mRewardsStoreState,mRewardsStoreZipcode,mStoreReviewDescription;
	private CheckBox mZouponsTermsAndConditionsCheckBox;
	private ScrollView mRewardsStoreDetialsScrollContainer;
	private String mPreviousStoreName,mChangedStoreName,mStorePhoto1Base64,mStorePhoto2Base64;
	public static String mTermsConditionsMessage,mAddRewardMessage,mCheckStoreMessage,mRewardsVideoUrl;
	private String[] statecodes = new String[]{"AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"};
	private String mStoreRating = "0";
	private View mLeftMenuBarSplitter;
	private int mImageViewWidth,mImageViewHeight;
	public static int mAdSlotImageViewWidth;
	private ProgressBar mAd_ProgressBar;
	public static Timer mTimer;
	private AdvertisementTimerTask mTimerTask;

	/**
	 * StoreInformation Variables intialization
	 * */
	private Button mAddStorePhotoButton;
	private TextView mStoreName,mStorePhoneNumber,mStoreWebsite,mStoreDescription,mBusiness_Time,
	mMondayBusinessStartTime,mTuesdayBusinessStartTime,mWednesdayBusinessStartTime,mThursdayBusinessStartTime,mFridayBusinessStartTime,mSaturdayBusinessStartTime,mSundayBusinessStartTime,
	mMondayBusinessEndTime,mTuesdayBusinessEndTime,mWednesdayBusinessEndTime,mThursdayBusinessEndTime,mFridayBusinessEndTime,mSaturdayBusinessEndTime,mSundayBusinessEndTime;
	private ImageView mBusinessTimeCloseButton,storeinfo_rightmenu;                                        
	private GridView mStorePhotoGrid;
	private LinearLayout mStoreGalleryInfoLayout,mBuisnessLayout;
	private ViewPager mImagePager;
	private ImageView mPhotoSwitcherLeftArrow,mPhotoSwitcherRightArrow;
	private UnderlinePageIndicator mIndicator;
	private int position;
	// for store photo grids
	private GridView mStoreAllPhotos;
	private Button mStorePhotosBackButton;
	private View mStorePhotosBackMenuSplitter;
	private int mImagePagerWidth,mImagePagerHeight;
	private boolean mLeftArrowFlag,mRightArrowFlag;

	/**
	 * GiftCardDetails Variables intialization
	 * */
	private LinearLayout mChooseCardContainer;
	private CheckBox mGiftcardCheckBox,mZCardCheckBox;
	private TextView mRemainingHeaderId,mStoreCardsFaceValueHeader,mStoreCardsSellPriceHeader;
	private View mFooterLayout;
	private TextView mFooterText;
	private ProgressBar mFooterProgress;
	public static boolean mIsLoadMore;
	public static String mGiftCardStart="0",mGiftCardEndLimit="20";
	public static String mGiftCardTotalCount = "0";	
	public static String mDealStart="0",mDealEndLimit="20",mDealTotalCount = "0";	
	private LayoutInflater mInflater;
    private	String mExtra="";
	private CustomCardDetailsAdapter mGiftCardAdapter,mDealAdapter;

	/**
	 * StoreCard Details Variables intialization
	 * */
	private ListView mCardDetailsList;
	private TextView mStoreNameHeader,mBackText;

	/**
	 * FB friendlist Variables intialization
	 * */
	private EditText mFriendlistSearch;   
	private ListView mfriendlist,mSocialTypesList;
	private View SocialfriendsOptionsContainer;
	private Button mSearchCancel;
	private String mClassName,mFaceValue;
	private View importmenusplitter;
	private ViewGroup friendlistgroup;
	private TextView mImportFriendMenu;

	/**
	 * FB friend Note description
	 * */
	private TextView mNotesFriendName,mNotesBack;
	private EditText mNotesDescription,mNotesDateText;
	private ImageView mNotesDateField;
	private Button mNotesCancelButton,mNotesSendButton;
	private DatePickerDialog mDatePicker;

	/**
	 * Social Variables intialization
	 * */
	private ImageView mSocialRightMenuHolder,mSocialFBLike;
	private TextView mSocialStoreName,mSocialBackMenu;
	//private Button mSocialShare;
	private CheckBox mSocialShareCheckBox,mEmailShareCheckBox;
	private RelativeLayout mSocialFooterLayout,mSocialShareOptionsLayout;
	private View mBackMenuSplitter;
	private UiLifecycleHelper mFacebookUILifeCycleHelper;

	/**
	 * Video Play Variables intialization
	 * */
	private ImageView mVideoImage,mVideoPlayImage;
	public static String VIDEOURLVALUE;
	private TextView mVideoStoreName;

	/**
	 * Coupons Activity
	 * */
	private TextView mCouponsStoreName;
	private ListView CouponsList;

	/**
	 * Favorite Coupon Detail    
	 */
	public static boolean mIsRefreshAdapter = false;
	//For Coupons
	public static String mCouponStart="0",mCouponEndLimit="20",mCouponTotalCount = "0";	
	private CouponListAdapter mCouponAdapter;

	/**
	 * ContactStore Variables intialization
	 * **/
	private ImageView mContactStore_RightMenuOpen;
	private TextView mContactStore_StoreName;
	private ViewGroup menubarcontainer;
	private ListView mListView;
	private EditText mAddMessage;
	private Button mSend;
	public static View mHeaderView;
	public static ProgressBar mHeaderProgressBar;
	public static TextView mHeaderLoadingText;
	public static Timer mCommunicationTimer;
	public static CommunicationTimerTask mCommunicationTimerTask;
	
	/**
	 * StoreReviews Activity
	 * */
	private Button mPreviousButton,mNextButton,mRateOnebutton,mRateTwobutton,mRateThreebutton,mRateFourbutton,mRateFivebutton,mCancel,mPost;
	private ImageView mRightMenuReview,mViewReviewStarOne,mViewReviewStarTwo,mViewReviewStarThree,mViewReviewStarFour,mViewReviewStarFive,mLikeImage,mDislikeImage;
	private TextView mReviewStoreTitle,mReviewRatingOne,mReviewRatingTwo,mReviewRatingThree,mReviewRatingFour,mReviewRatingFive,
	mViewReviewStoreTitle,mViewReviewName,mViewReviewPostedDate,mLikeCount,mDislikeCount,mFooterLeftText,mFooterRightText;
	private ListView mReviewList;
	private LinearLayout mReviewRatingContainer,mViewReviewDetailsContainer,mViewReviewRatingContainer,mUpdateReviewLayout;
	private RelativeLayout mReviewFooterContainer,mViewReviewDetials,mReViewLikeDislikeContainer,mviewReviewButtonContainer,mupdateReviewButtonContainer;
	private EditText mViewReviewMessage,mUpdateReviewMessage;
	private ScrollView mEditReviewDetails,mViewReviewDetails;
	private String reviewId="";
	public static String STORE_REVIEW_MESSAGE="";
	public static int UserReviewPosition=-1;
	private int currentPosition;

	//For StoreReviews
	public static String mReviewStart="0",mReviewEndLimit="20",mReviewTotalCount = "0";	
	private StoreReviewListAdapter mReviewAdapter;

	/**
	 * TransactionHistory
	 */
	private ListView mTransactionDetailsListView;
	private TextView mTransactionHistoryBack;

	// For notifications and log out
	private RelativeLayout btnLogout; 
	private ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	private Button mNotificationCount;
	private TextView mTalkToUsContactStoreBack;
    private ScheduleNotificationSync mNotificationSync;

	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;

	/**
	 * CustomerCenter
	 */
	//For CustomerCenter
	public static String mCustomerCenterStart="0";
	public static String mCustomerCenterEndLimit="20";
	public static String mCustomerCenterTotalCount = "0";	
	
	private LinearLayout mCustomerCenter_ZouponsSupportButton;
	private TextView mCustomerCenter_ZouponsSupportCount;
	private ListView mCustomerCenter_StoreListView;
	private ChatSupportListAdapter mCustomerServiceListAdaper;

	/* LoginChoice TabBar header */
	private ImageView mTabBarLoginChoice;
	public static ScrollView leftMenuScrollView,rightMenuScrollView;
    private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		sScrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(sScrollView);
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		progressdialog=new ProgressDialog(this);
		progressdialog.setCancelable(false);
		progressdialog.setMessage("Loading....");
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		sLeftMenu = inflater.inflate(R.layout.shopper_leftmenu, null);
		sRightMenu = inflater.inflate(R.layout.rightmenu, null);
		ViewGroup menuLeftItems = (ViewGroup) sLeftMenu.findViewById(R.id.menuitems);
		ViewGroup menuRightItems = (ViewGroup) sRightMenu.findViewById(R.id.rightmenuitems);
		leftMenuScrollView = (ScrollView) sLeftMenu.findViewById(R.id.leftmenu_scrollview);
		leftMenuScrollView.fullScroll(View.FOCUS_UP);
		leftMenuScrollView.pageScroll(View.FOCUS_UP);

		rightMenuScrollView = (ScrollView) sRightMenu.findViewById(R.id.rightmenu_scrollview);
		rightMenuScrollView.fullScroll(View.FOCUS_UP);
		rightMenuScrollView.pageScroll(View.FOCUS_UP);

		mHome = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_home);
		mZpay = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_zpay);
		mInvoiceCenter = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_invoicecenter);
		mZGiftcards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_zgiftcards);
		mManageCards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_managecards);
		mReceipts = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_receipts);
		mMyFavourites = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_myfavourites);
		mMyFriends = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_myfriends);
		mChat = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_customercenter);
		mRewards = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_rewards);
		mSettings = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_settings);
		mLogout = (LinearLayout) menuLeftItems.findViewById(R.id.mainmenu_logout);

		mHomeText = (TextView) menuLeftItems.findViewById(R.id.menuHome);
		mHomeText.setTypeface(mZouponsFont);
		mZpayText = (TextView) menuLeftItems.findViewById(R.id.menuZpay);
		mZpayText.setTypeface(mZouponsFont);
		mInvoiceCenterText = (TextView) menuLeftItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
		mInvoiceCenterText.setTypeface(mZouponsFont);
		mGiftCardsText = (TextView) menuLeftItems.findViewById(R.id.menuGiftCards);
		mGiftCardsText.setTypeface(mZouponsFont);
		mManageCardsText = (TextView) menuLeftItems.findViewById(R.id.menuManageCards);
		mManageCardsText.setTypeface(mZouponsFont);
		mReceiptsText = (TextView) menuLeftItems.findViewById(R.id.menuReceipts);
		mReceiptsText.setTypeface(mZouponsFont);
		mMyFavoritesText = (TextView) menuLeftItems.findViewById(R.id.menufavorites);
		mMyFavoritesText.setTypeface(mZouponsFont);
		mMyFriendsText = (TextView) menuLeftItems.findViewById(R.id.menuMyFriends);
		mMyFriendsText.setTypeface(mZouponsFont);
		mChatText = (TextView) menuLeftItems.findViewById(R.id.menuCustomerCenter);
		mChatText.setTypeface(mZouponsFont);
		mRewardsText = (TextView) menuLeftItems.findViewById(R.id.menuRewards);
		mRewardsText.setTypeface(mZouponsFont);
		mSettingsText = (TextView) menuLeftItems.findViewById(R.id.menuSettings);
		mSettingsText.setTypeface(mZouponsFont);
		mLogoutText = (TextView) menuLeftItems.findViewById(R.id.menuLogout);
		mLogoutText.setTypeface(mZouponsFont);
		
		mHomeImage = (ImageView) menuLeftItems.findViewById(R.id.menuHomeimage);
		mZpayImage = (ImageView) menuLeftItems.findViewById(R.id.menuZpayimage);
		mInvoiceCenterImage = (ImageView) menuLeftItems.findViewById(R.id.menuInvoiceCenterimage);
		mGiftCardsImage = (ImageView) menuLeftItems.findViewById(R.id.menuGiftcardsimage);
		mManageCardsImage = (ImageView) menuLeftItems.findViewById(R.id.menuManageCardsimage);
		mReceiptsImage = (ImageView) menuLeftItems.findViewById(R.id.menuReceiptsimage);
		mMyFavoritesImage = (ImageView) menuLeftItems.findViewById(R.id.menuFavoriteimage);
		mMyFriendsImage = (ImageView) menuLeftItems.findViewById(R.id.menuFriendsimage);
		mChatImage = (ImageView) menuLeftItems.findViewById(R.id.menuCustomerCenterimage);
		mRewardsImage = (ImageView) menuLeftItems.findViewById(R.id.menuRewardsimage);
		mSettingsImage = (ImageView) menuLeftItems.findViewById(R.id.menuSettingsimage);

		/*Right Navigation*/
		mInfo_RightMenu = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_info);
		mMobilePay_RightMenu = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_mobilepay);
		mGiftCards_RightMenu = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_giftcards);
		mDeals_RightMenu=(LinearLayout) menuRightItems.findViewById(R.id.rightmenu_deals);
		mCoupons_RightMenu = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_coupons);
		mSocial_RightMenu = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_social);
		mReviews_RightMenu = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_reviews);
		mLocations_RightMenu = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_locations);
		mPhotos_RightMenu = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_photos);
		mVideos_RightMenu = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_videos);
		mContactStore_RightMenu = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_contactstore);
		mFavorite_RightMenu = (LinearLayout) menuRightItems.findViewById(R.id.rightmenu_favorite);

		/*mStoreImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_storeimage);*/
		mFavoriteImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_favoriteimage);
		mStoreName_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_storename);
		mStoreName_RightMenu.setTypeface(mZouponsFont);
		mStoreLocation_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_storelocation);
		mStoreLocation_RightMenu.setTypeface(mZouponsFont);
		mInfoText_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_infotext);
		mInfoText_RightMenu.setTypeface(mZouponsFont);
		mMobilePayText_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_mobilepaytext);
		mMobilePayText_RightMenu.setTypeface(mZouponsFont);
		mGiftCardsText_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_giftcardstext);
		mGiftCardsText_RightMenu.setTypeface(mZouponsFont);
		mDealsText_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_dealstext);
		mDealsText_RightMenu.setTypeface(mZouponsFont);
		mCouponsText_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_couponstext);
		mCouponsText_RightMenu.setTypeface(mZouponsFont);
		mSocialText_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_socialtext);
		mSocialText_RightMenu.setTypeface(mZouponsFont);
		mReviewsText_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_reviewstext);
		mReviewsText_RightMenu.setTypeface(mZouponsFont);
		mLocationsText_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_locationstext);
		mLocationsText_RightMenu.setTypeface(mZouponsFont);
		mPhotosText_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_photostext);
		mPhotosText_RightMenu.setTypeface(mZouponsFont);
		mVideosText_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_videostext);
		mVideosText_RightMenu.setTypeface(mZouponsFont);
		mContactStoreText_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_contactstoretext);
		mContactStoreText_RightMenu.setTypeface(mZouponsFont);
		mFavoriteText_RightMenu = (TextView) menuRightItems.findViewById(R.id.rightmenu_favoritetext);
		mFavoriteText_RightMenu.setTypeface(mZouponsFont);

		mInfoImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_infoimage);
		mMobilePayImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_zpayimage);
		mGiftCardsImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_giftcardsimage);
		mDealsImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_dealsimage);
		mCouponsImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_couponsimage);
		mSocialImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_socialimage);
		mReviewsImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_reviewsimage);
		mLocationsImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_locationsimage);
		mPhotosImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_photosimage);
		mVideosImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_videoimage);
		mContactStoreImage_RightMenu = (ImageView) menuRightItems.findViewById(R.id.rightmenu_contactstoreimage);
		
		mHome.setOnClickListener(new MenuItemClickListener(menuLeftItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mZpay.setOnClickListener(new MenuItemClickListener(menuLeftItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mInvoiceCenter.setOnClickListener(new MenuItemClickListener(menuLeftItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mZGiftcards.setOnClickListener(new MenuItemClickListener(menuLeftItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mManageCards.setOnClickListener(new MenuItemClickListener(menuLeftItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mReceipts.setOnClickListener(new MenuItemClickListener(menuLeftItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mMyFavourites.setOnClickListener(new MenuItemClickListener(menuLeftItems,MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mMyFriends.setOnClickListener(new MenuItemClickListener(menuLeftItems,MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mChat.setOnClickListener(new MenuItemClickListener(menuLeftItems,MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mRewards.setOnClickListener(new MenuItemClickListener(menuLeftItems,MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mSettings.setOnClickListener(new MenuItemClickListener(menuLeftItems,MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				try{
					if(mCommunicationTimer!=null){
						mCommunicationTimer.cancel();
						mCommunicationTimer=null;
					}if(mCommunicationTimerTask!=null){
						mCommunicationTimerTask.cancel();
						mCommunicationTimerTask=null;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(MainMenuActivity.this,"FromManualLogOut").execute();
			}
		});

		/*ClickListeners for all sRightmenu items*/
		mInfo_RightMenu.setOnClickListener(new MenuItemClickListener(menuRightItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mMobilePay_RightMenu.setOnClickListener(new MenuItemClickListener(menuRightItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mGiftCards_RightMenu.setOnClickListener(new MenuItemClickListener(menuRightItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mDeals_RightMenu.setOnClickListener(new MenuItemClickListener(menuRightItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mCoupons_RightMenu.setOnClickListener(new MenuItemClickListener(menuRightItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mSocial_RightMenu.setOnClickListener(new MenuItemClickListener(menuRightItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mReviews_RightMenu.setOnClickListener(new MenuItemClickListener(menuRightItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mLocations_RightMenu.setOnClickListener(new MenuItemClickListener(menuRightItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mPhotos_RightMenu.setOnClickListener(new MenuItemClickListener(menuRightItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mVideos_RightMenu.setOnClickListener(new MenuItemClickListener(menuRightItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mContactStore_RightMenu.setOnClickListener(new MenuItemClickListener(menuRightItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));
		mFavorite_RightMenu.setOnClickListener(new MenuItemClickListener(menuRightItems, MainMenuActivity.this,POJOMainMenuActivityTAG.TAG));

		//Function to set right menu items status
		SetRightMenuStatus(MainMenuActivity.this);
	}

	protected void setRewardsView(int bodyLayout){

		POJOMainMenuActivityTAG.TAG="MainMenuActivity_Rewards";
		app = inflater.inflate(bodyLayout, null);
		int MenuFlag =0;
		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.rewards_tabBar);

		// Notification and log out variables
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		mMainMenuActivityFreezeView = (Button) app.findViewById(R.id.rewards_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_rewards);

		mRewardsStartUpLayout = (RelativeLayout) app.findViewById(R.id.rewards_container);
		mRewardsVideoViewLayout = (RelativeLayout) mRewardsStartUpLayout.findViewById(R.id.rewards_videoview_container); 
		mRewardsFooterLayout = (RelativeLayout) app.findViewById(R.id.rewards_footerLayoutId);
		mRewardsStoreDetailsContainer = (RelativeLayout) app.findViewById(R.id.rewards_store_container);
		mRewardsStepLayout = (LinearLayout) mRewardsStoreDetailsContainer.findViewById(R.id.rewards_stepsLayoutId);
		mRewardsStoreDetialsScrollContainer = (ScrollView) mRewardsStoreDetailsContainer.findViewById(R.id.rewards_storedetailsScrollId);
		mRewardsStoreDetails = (LinearLayout) mRewardsStoreDetialsScrollContainer.findViewById(R.id.rewards_storedetailsId);
		mRewardsStoreAddressDetails = (LinearLayout) mRewardsStoreDetialsScrollContainer.findViewById(R.id.rewards_storeAddressdetailsId);
		mRewardsStorePhotoDetails = (LinearLayout) mRewardsStoreDetialsScrollContainer.findViewById(R.id.rewards_storephotodetailsId);
		mRewardsStorePhoto1Container = (RelativeLayout) mRewardsStorePhotoDetails.findViewById(R.id.rewards_storePhoto1Container);
		mRewardsStorePhoto2Container = (RelativeLayout) mRewardsStorePhotoDetails.findViewById(R.id.rewards_storePhoto2Container);
		mRewardsStoreReviewDetails = (LinearLayout) mRewardsStoreDetialsScrollContainer.findViewById(R.id.rewards_storereviewdetailsId);
		mRewardsStoreReviewRateLayout = (LinearLayout) mRewardsStoreReviewDetails.findViewById(R.id.rewards_storereviewrateLayoutId);
		mZouponsTermsAndConditionsLayout = (RelativeLayout) mRewardsStoreReviewDetails.findViewById(R.id.zoupons_termsandConditionsLayout);
		mRewardsStoreStateZipContainer = (LinearLayout) mRewardsStoreAddressDetails.findViewById(R.id.rewards_state_zipcodeContainer);
		mRewardsStoreStateLayout = (FrameLayout) mRewardsStoreStateZipContainer.findViewById(R.id.store_state_container); 
		mFirstRateButton = (Button) mRewardsStoreReviewRateLayout.findViewById(R.id.store_rate1ButtonId);
		mSecondRateButton = (Button) mRewardsStoreReviewRateLayout.findViewById(R.id.store_rate2ButtonId); 
		mThirdRateButton = (Button) mRewardsStoreReviewRateLayout.findViewById(R.id.store_rate3ButtonId);
		mFourthRateButton = (Button) mRewardsStoreReviewRateLayout.findViewById(R.id.store_rate4ButtonId);
		mFifthRateButton = (Button) mRewardsStoreReviewRateLayout.findViewById(R.id.store_rate5ButtonId);
		mLeftFoooterView = (TextView) mRewardsFooterLayout.findViewById(R.id.rewards_leftFooterText);
		mRightFooterView = (TextView) mRewardsFooterLayout.findViewById(R.id.rewards_rightFooterText);
		mLeftMenuBarSplitter = mRewardsFooterLayout.findViewById(R.id.reward_leftmenubar_splitter);
		mNameDetails = (TextView) mRewardsStepLayout.findViewById(R.id.rewards_nameStepId);
		mAddressDetails = (TextView) mRewardsStepLayout.findViewById(R.id.rewards_addressStepId);
		mPhotoDetails = (TextView) mRewardsStepLayout.findViewById(R.id.rewards_photoStepId);
		mReviewDetails =(TextView) mRewardsStepLayout.findViewById(R.id.rewards_reviewStepId);
		mZouponsTermsAndConditions = (TextView) mZouponsTermsAndConditionsLayout.findViewById(R.id.zoupons_termsandconditionsId);
		mVideoThumbnail =  (ImageView) mRewardsVideoViewLayout.findViewById(R.id.rewards_videothumbnailId);
		mAd_ProgressBar = (ProgressBar) mRewardsVideoViewLayout.findViewById(R.id.mRewardsProgressBar);

		mTimer = new Timer();
		mTimerTask = new AdvertisementTimerTask(MainMenuActivity.this, mVideoThumbnail, mAd_ProgressBar,"rewards");
		mVideoPlay = (ImageView) mRewardsVideoViewLayout.findViewById(R.id.rewards_videoPlayButtonId);
		mStateContextMenuImage = (ImageView) mRewardsStoreStateZipContainer.findViewById(R.id.store_state_contextmenu);
		mStorePhoto1 = (ImageView) mRewardsStorePhoto1Container.findViewById(R.id.rewards_store_photo1);
		mStorePhoto2 = (ImageView) mRewardsStorePhoto2Container.findViewById(R.id.rewards_store_photo2);
		mStoreAddPhoto1 = (ImageView) mRewardsStorePhoto1Container.findViewById(R.id.rewards_store_addphoto1Id);
		mStoreAddPhoto2 = (ImageView) mRewardsStorePhoto2Container.findViewById(R.id.rewards_store_addphoto2Id); 
		mRewardsStoreName = (EditText) mRewardsStoreDetails.findViewById(R.id.rewards_storeNameId);
		mRewardsStoreManagerName = (EditText) mRewardsStoreDetails.findViewById(R.id.rewards_storeManagerNameId);
		mRewardsTelephoneNumber = (EditText) mRewardsStoreDetails.findViewById(R.id.rewards_storeTelephoneNumberId);
		mRewardsStoreAddress1 = (EditText) mRewardsStoreAddressDetails.findViewById(R.id.rewards_storeAddress1Id);
		mRewardsStoreAddress2 = (EditText) mRewardsStoreAddressDetails.findViewById(R.id.rewards_storeAddress2Id);
		mRewardsStoreCity = (EditText) mRewardsStoreAddressDetails.findViewById(R.id.rewards_storeCityId);
		mRewardsStoreState = (EditText) mRewardsStoreStateLayout.findViewById(R.id.store_state_value);
		mRewardsStoreState.setLongClickable(false); // To disable cut/copy/paste options..
		mRewardsStoreZipcode = (EditText) mRewardsStoreStateZipContainer.findViewById(R.id.store_zipcodeId);
		mStoreReviewDescription = (EditText) mRewardsStoreReviewDetails.findViewById(R.id.store_review_descriptionId);
		mZouponsTermsAndConditionsCheckBox = (CheckBox) mZouponsTermsAndConditionsLayout.findViewById(R.id.mStoreTermsConditions);
		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));
		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView));

		View[] children = new View[] { sLeftMenu, app, sRightMenu };
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

		mNameDetails.setClickable(false);
		mAddressDetails.setClickable(false);
		mPhotoDetails.setClickable(false);
		mReviewDetails.setClickable(false);

		mRewardsTelephoneNumber.addTextChangedListener(new MobileNumberTextWatcher());
		mRewardsTelephoneNumber.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});
		registerForContextMenu(mStoreAddPhoto1);
		registerForContextMenu(mStoreAddPhoto2);
		

		SharedPreferences mPrefs = getSharedPreferences(ZouponsLogin.PREFENCES_FILE, Context.MODE_PRIVATE);

		RewardsAdvertisementTask mVideoTask = new RewardsAdvertisementTask(MainMenuActivity.this, mVideoThumbnail,mVideoPlay,mImageViewWidth,mImageViewHeight,mTimerTask,mTimer,mAd_ProgressBar);
		mVideoTask.execute(mPrefs.getString("device_id",""));       

		mVideoPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentPlay = new Intent(MainMenuActivity.this,StoreVideoDialog.class);
				intentPlay.putExtra("VIDEO",mRewardsVideoUrl);
				startActivity(intentPlay);
			}
		});

		mStoreAddPhoto1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openContextMenu(v);
			}
		});

		mStoreAddPhoto2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openContextMenu(v);
			}
		});

		mRewardsStoreState.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				contextMenuOpen(mStateContextMenuImage);
			}
		});

		mStateContextMenuImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				contextMenuOpen(mStateContextMenuImage);
			}
		});

		mRightFooterView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(mRightFooterView.getText().toString().equalsIgnoreCase("start")){
					// To cancel advertisement timer and task
					if(mTimer != null){
						mTimer.cancel();
						mTimer = null;
					}
					if(mTimerTask != null){
						mTimerTask.cancel();
					}

					mRewardsStartUpLayout.setVisibility(View.GONE);
					mRewardsStoreDetailsContainer.setVisibility(View.VISIBLE);
					mRewardsStoreDetails.setVisibility(View.VISIBLE);
					mRewardsStoreAddressDetails.setVisibility(View.GONE);
					mRewardsStorePhotoDetails.setVisibility(View.GONE);
					mRewardsStoreReviewDetails.setVisibility(View.GONE);
					mNameDetails.setTextColor(getResources().getColor(R.color.blue));
					mAddressDetails.setTextColor(getResources().getColor(R.color.darkgrey));
					mPhotoDetails.setTextColor(getResources().getColor(R.color.darkgrey));
					mReviewDetails.setTextColor(getResources().getColor(R.color.darkgrey));
					mLeftFoooterView.setVisibility(View.VISIBLE);
					mLeftMenuBarSplitter.setVisibility(View.VISIBLE);
					mRightFooterView.setText("Next");
					mRightFooterView.setTag("storedetails");
					mNameDetails.setClickable(false);
					mAddressDetails.setClickable(false);
					mPhotoDetails.setClickable(false);
					mReviewDetails.setClickable(false);
					//To clear the EditText
					mRewardsStoreName.setText("");
					mRewardsStoreManagerName.setText("");
					mRewardsTelephoneNumber.setText("");
					mRightFooterView.setEnabled(false);
					mRightFooterView.setBackgroundColor(Color.parseColor("#81BEF7"));
					clearAllStoreDetials();
				}else if(mRightFooterView.getText().toString().equalsIgnoreCase("Next") && mRightFooterView.getTag().toString().equalsIgnoreCase("storedetails")){
					//Validation for Store Details				 
					if(mRewardsStoreName.getText().toString().trim().equalsIgnoreCase("")){
						mRewardsStoreName.requestFocus();
						mRightFooterView.setEnabled(false);
						mRightFooterView.setBackgroundColor(Color.parseColor("#81BEF7"));
						alertBox_service("Information", "Enter store name.");
					}else if(mRewardsTelephoneNumber.getText().toString().trim().equalsIgnoreCase("")||mRewardsTelephoneNumber.getText().toString().trim().length()<12){
						mRewardsTelephoneNumber.requestFocus();					 
						alertBox_service("Information", "Enter telephone number");
					}else{					  
						mRewardsStoreDetails.setVisibility(View.GONE);
						mRewardsStoreAddressDetails.setVisibility(View.VISIBLE);
						mNameDetails.setTextColor(getResources().getColor(R.color.blue));
						mAddressDetails.setTextColor(getResources().getColor(R.color.blue));
						mPhotoDetails.setTextColor(getResources().getColor(R.color.darkgrey));
						mReviewDetails.setTextColor(getResources().getColor(R.color.darkgrey));
						mRightFooterView.setTag("storeaddressdetails");
						mNameDetails.setClickable(true);
						mAddressDetails.setClickable(false);
						mPhotoDetails.setClickable(false);
						mReviewDetails.setClickable(false);
					}

				}else if(mRightFooterView.getText().toString().equalsIgnoreCase("Next") && mRightFooterView.getTag().toString().equalsIgnoreCase("storeaddressdetails")){
					//Validation for Store address details
					if(mRewardsStoreAddress1.getText().toString().trim().equalsIgnoreCase("")){
						mRewardsStoreAddress1.requestFocus();
						alertBox_service("Information", "Enter store address.");
					}else if(mRewardsStoreCity.getText().toString().trim().equalsIgnoreCase("")){
						mRewardsStoreCity.requestFocus();
						alertBox_service("Information", "Enter city.");
					}else if(mRewardsStoreState.getText().toString().trim().equalsIgnoreCase("")){
						mRewardsStoreState.requestFocus();
						alertBox_service("Information", "Choose state.");
					}else if(mRewardsStoreZipcode.getText().toString().trim().equalsIgnoreCase("") || mRewardsStoreZipcode.getText().toString().length()!=5){
						mRewardsStoreZipcode.requestFocus();
						alertBox_service("Information", "Enter zipcode.");
					}else{
						mRewardsStoreAddressDetails.setVisibility(View.GONE);
						mRewardsStorePhotoDetails.setVisibility(View.VISIBLE);
						mNameDetails.setTextColor(getResources().getColor(R.color.blue));
						mAddressDetails.setTextColor(getResources().getColor(R.color.blue));
						mPhotoDetails.setTextColor(getResources().getColor(R.color.blue));
						mReviewDetails.setTextColor(getResources().getColor(R.color.darkgrey));
						mRightFooterView.setTag("storephotodetails");
						mNameDetails.setClickable(true);
						mAddressDetails.setClickable(true);
						mPhotoDetails.setClickable(false);
						mReviewDetails.setClickable(false);
						//Clear the Static String Values
						mStorePhoto1Base64="";
						mStorePhoto2Base64="";
					}
				}else if(mRightFooterView.getText().toString().equalsIgnoreCase("Next") && mRightFooterView.getTag().toString().equalsIgnoreCase("storephotodetails")){
					//Validation for store photo
					mRewardsStorePhotoDetails.setVisibility(View.GONE);
					mRewardsStoreReviewDetails.setVisibility(View.VISIBLE);
					mNameDetails.setTextColor(getResources().getColor(R.color.blue));
					mAddressDetails.setTextColor(getResources().getColor(R.color.blue));
					mPhotoDetails.setTextColor(getResources().getColor(R.color.blue));
					mReviewDetails.setTextColor(getResources().getColor(R.color.blue));
					mRightFooterView.setText("Submit");
					mRightFooterView.setTag("storereviewdetails");
					mNameDetails.setClickable(true);
					mAddressDetails.setClickable(true);
					mPhotoDetails.setClickable(true);
					mReviewDetails.setClickable(false);  
					//}
				}else if(mRightFooterView.getText().toString().equalsIgnoreCase("Submit")){
					if(mStoreReviewDescription.getText().toString().trim().equalsIgnoreCase("")){
						alertBox_service("Information", "Enter a review.");
					}else if(mStoreRating.equalsIgnoreCase("0")){
						alertBox_service("Information", "Rate store.");
					}else if(!mZouponsTermsAndConditionsCheckBox.isChecked()){
						alertBox_service("Information", "Please accept the terms and conditions of Zoupons to proceed.");
					}else{
						AddRewardsTask mAddRewards = new AddRewardsTask(MainMenuActivity.this);
						mAddRewards.execute(UserDetails.mServiceUserId,mRewardsStoreName.getText().toString(),mRewardsStoreManagerName.getText().toString(),mStorePhoto1Base64,mStorePhoto2Base64,
								mRewardsStoreAddress1.getText().toString(),mRewardsStoreAddress2.getText().toString(),mRewardsStoreState.getText().toString(),mRewardsStoreCity.getText().toString(),
								mRewardsStoreZipcode.getText().toString(),mRewardsTelephoneNumber.getText().toString(),mStoreReviewDescription.getText().toString(),mStoreRating);
					}
				}
			}
		});

		mLeftFoooterView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mRightFooterView.getTag().toString().equalsIgnoreCase("storedetails")){
					mRightFooterView.setTag("start");
					mRewardsStartUpLayout.setVisibility(View.VISIBLE);
					mRewardsStoreDetailsContainer.setVisibility(View.GONE);
					mLeftFoooterView.setVisibility(View.GONE);
					mLeftMenuBarSplitter.setVisibility(View.GONE);
					mRightFooterView.setText("Start");
					mRightFooterView.setEnabled(true);
					mRightFooterView.setBackgroundColor(0);
					if(mTimer == null){
						mTimer = new Timer();
						mTimerTask = new AdvertisementTimerTask(MainMenuActivity.this, mVideoThumbnail, mAd_ProgressBar,"rewards");
					}
					SharedPreferences mPrefs = getSharedPreferences(ZouponsLogin.PREFENCES_FILE, Context.MODE_PRIVATE);
					RewardsAdvertisementTask mVideoTask = new RewardsAdvertisementTask(MainMenuActivity.this, mVideoThumbnail,mVideoPlay,mImageViewWidth,mImageViewHeight,mTimerTask,mTimer,mAd_ProgressBar);
					mVideoTask.execute(mPrefs.getString("device_id",""));
				}else if(mRightFooterView.getTag().toString().equalsIgnoreCase("storeaddressdetails")){	//Name
					// Control move to "Name"
					mRewardsStoreDetails.setVisibility(View.VISIBLE);
					mRewardsStoreAddressDetails.setVisibility(View.GONE);
					mRewardsStorePhotoDetails.setVisibility(View.GONE);
					mRewardsStoreReviewDetails.setVisibility(View.GONE);
					mNameDetails.setTextColor(getResources().getColor(R.color.blue));
					mAddressDetails.setTextColor(getResources().getColor(R.color.darkgrey));
					mPhotoDetails.setTextColor(getResources().getColor(R.color.darkgrey));
					mReviewDetails.setTextColor(getResources().getColor(R.color.darkgrey));
					mLeftFoooterView.setVisibility(View.VISIBLE);
					mLeftMenuBarSplitter.setVisibility(View.VISIBLE);
					mRightFooterView.setText("Next");
					mRightFooterView.setTag("storedetails");
					clearAllStoreDetials();
				}else if(mRightFooterView.getTag().toString().equalsIgnoreCase("storephotodetails")){	//Address
					//Control move to "Address"
					mRewardsStoreDetails.setVisibility(View.GONE);
					mRewardsStoreAddressDetails.setVisibility(View.VISIBLE);
					mRewardsStorePhotoDetails.setVisibility(View.GONE);
					mRewardsStoreReviewDetails.setVisibility(View.GONE);
					mNameDetails.setTextColor(getResources().getColor(R.color.blue));
					mAddressDetails.setTextColor(getResources().getColor(R.color.blue));
					mPhotoDetails.setTextColor(getResources().getColor(R.color.darkgrey));
					mReviewDetails.setTextColor(getResources().getColor(R.color.darkgrey));
					mRightFooterView.setText("Next");
					mRightFooterView.setTag("storeaddressdetails");
					clearStorePhotoReviewData();
				}else if(mRightFooterView.getTag().toString().equalsIgnoreCase("storereviewdetails")){	//Photo
					//Control move to "Photo"
					mRewardsStoreDetails.setVisibility(View.GONE);
					mRewardsStoreAddressDetails.setVisibility(View.GONE);
					mRewardsStorePhotoDetails.setVisibility(View.VISIBLE);
					mRewardsStoreReviewDetails.setVisibility(View.GONE);
					mNameDetails.setTextColor(getResources().getColor(R.color.blue));
					mAddressDetails.setTextColor(getResources().getColor(R.color.blue));
					mPhotoDetails.setTextColor(getResources().getColor(R.color.blue));
					mReviewDetails.setTextColor(getResources().getColor(R.color.darkgrey));
					mRightFooterView.setText("Next");
					mRightFooterView.setTag("storephotodetails");
					clearStoreReviewData();
				}
			}
		});

		mNameDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRewardsStoreDetails.setVisibility(View.VISIBLE);
				mRewardsStoreAddressDetails.setVisibility(View.GONE);
				mRewardsStorePhotoDetails.setVisibility(View.GONE);
				mRewardsStoreReviewDetails.setVisibility(View.GONE);
				mNameDetails.setTextColor(getResources().getColor(R.color.blue));
				mAddressDetails.setTextColor(getResources().getColor(R.color.darkgrey));
				mPhotoDetails.setTextColor(getResources().getColor(R.color.darkgrey));
				mReviewDetails.setTextColor(getResources().getColor(R.color.darkgrey));
				mLeftFoooterView.setVisibility(View.VISIBLE);
				mLeftMenuBarSplitter.setVisibility(View.VISIBLE);
				mRightFooterView.setText("Next");
				mRightFooterView.setTag("storedetails");
				clearAllStoreDetials();
			}
		});

		mAddressDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRewardsStoreDetails.setVisibility(View.GONE);
				mRewardsStoreAddressDetails.setVisibility(View.VISIBLE);
				mRewardsStorePhotoDetails.setVisibility(View.GONE);
				mRewardsStoreReviewDetails.setVisibility(View.GONE);
				mNameDetails.setTextColor(getResources().getColor(R.color.blue));
				mAddressDetails.setTextColor(getResources().getColor(R.color.blue));
				mPhotoDetails.setTextColor(getResources().getColor(R.color.darkgrey));
				mReviewDetails.setTextColor(getResources().getColor(R.color.darkgrey));
				mRightFooterView.setText("Next");
				mRightFooterView.setTag("storeaddressdetails");
				clearStorePhotoReviewData();
			}
		});

		mPhotoDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRewardsStoreDetails.setVisibility(View.GONE);
				mRewardsStoreAddressDetails.setVisibility(View.GONE);
				mRewardsStorePhotoDetails.setVisibility(View.VISIBLE);
				mRewardsStoreReviewDetails.setVisibility(View.GONE);
				mNameDetails.setTextColor(getResources().getColor(R.color.blue));
				mAddressDetails.setTextColor(getResources().getColor(R.color.blue));
				mPhotoDetails.setTextColor(getResources().getColor(R.color.blue));
				mReviewDetails.setTextColor(getResources().getColor(R.color.darkgrey));
				mRightFooterView.setText("Next");
				mRightFooterView.setTag("storephotodetails");
				clearStoreReviewData();
			}
		});

		mReviewDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRewardsStoreDetails.setVisibility(View.GONE);
				mRewardsStoreAddressDetails.setVisibility(View.GONE);
				mRewardsStorePhotoDetails.setVisibility(View.GONE);
				mRewardsStoreReviewDetails.setVisibility(View.VISIBLE);
				mNameDetails.setTextColor(getResources().getColor(R.color.blue));
				mAddressDetails.setTextColor(getResources().getColor(R.color.blue));
				mPhotoDetails.setTextColor(getResources().getColor(R.color.blue));
				mReviewDetails.setTextColor(getResources().getColor(R.color.blue));
				mRightFooterView.setText("Submit");
			}
		});

		mRewardsStoreName.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					mPreviousStoreName = mRewardsStoreName.getText().toString();
					if(!mRewardsStoreName.getText().toString().trim().equalsIgnoreCase("") && (!mPreviousStoreName.equalsIgnoreCase(mChangedStoreName))){
						mChangedStoreName = mRewardsStoreName.getText().toString();
						CheckStoreNameTask mCheckStore = new CheckStoreNameTask(MainMenuActivity.this,mRewardsStoreName,mRightFooterView);
						mCheckStore.execute("CheckStore",mRewardsStoreName.getText().toString());	
					}
				}
			}
		});

		mZouponsTermsAndConditions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainMenuActivity.this,TermsAndConditions.class));	
			}
		});

		mFirstRateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFirstRateButton.setBackgroundResource(R.drawable.star);
				mSecondRateButton.setBackgroundResource(R.drawable.button_border);
				mThirdRateButton.setBackgroundResource(R.drawable.button_border);
				mFourthRateButton.setBackgroundResource(R.drawable.button_border);
				mFifthRateButton.setBackgroundResource(R.drawable.button_border);
				mStoreRating="1";
			}
		});

		mSecondRateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFirstRateButton.setBackgroundResource(R.drawable.star);
				mSecondRateButton.setBackgroundResource(R.drawable.star);
				mThirdRateButton.setBackgroundResource(R.drawable.button_border);
				mFourthRateButton.setBackgroundResource(R.drawable.button_border);
				mFifthRateButton.setBackgroundResource(R.drawable.button_border);
				mStoreRating="2";
			}
		});

		mThirdRateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFirstRateButton.setBackgroundResource(R.drawable.star);
				mSecondRateButton.setBackgroundResource(R.drawable.star);
				mThirdRateButton.setBackgroundResource(R.drawable.star);
				mFourthRateButton.setBackgroundResource(R.drawable.button_border);
				mFifthRateButton.setBackgroundResource(R.drawable.button_border);
				mStoreRating="3";
			}
		});

		mFourthRateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFirstRateButton.setBackgroundResource(R.drawable.star);
				mSecondRateButton.setBackgroundResource(R.drawable.star);
				mThirdRateButton.setBackgroundResource(R.drawable.star);
				mFourthRateButton.setBackgroundResource(R.drawable.star);
				mFifthRateButton.setBackgroundResource(R.drawable.button_border);
				mStoreRating="4";
			}
		});

		mFifthRateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFirstRateButton.setBackgroundResource(R.drawable.star);
				mSecondRateButton.setBackgroundResource(R.drawable.star);
				mThirdRateButton.setBackgroundResource(R.drawable.star);
				mFourthRateButton.setBackgroundResource(R.drawable.star);
				mFifthRateButton.setBackgroundResource(R.drawable.star);
				mStoreRating="5";
			}
		});
	}

	public void clearAllStoreDetials(){
		mRewardsStoreAddress1.getText().clear();
		mRewardsStoreAddress2.getText().clear();
		mRewardsStoreCity.getText().clear();
		mRewardsStoreState.getText().clear();
		mRewardsStoreZipcode.getText().clear();
		mStorePhoto1.setImageBitmap(null);
		mStorePhoto2.setImageBitmap(null);
		mStorePhoto1Base64 = "";
		mStorePhoto2Base64 = "";
		mStoreRating="0";
		mStoreReviewDescription.getText().clear(); 
		mFirstRateButton.setBackgroundResource(R.drawable.button_border);
		mSecondRateButton.setBackgroundResource(R.drawable.button_border);
		mThirdRateButton.setBackgroundResource(R.drawable.button_border);
		mFourthRateButton.setBackgroundResource(R.drawable.button_border);
		mFifthRateButton.setBackgroundResource(R.drawable.button_border);
		mZouponsTermsAndConditionsCheckBox.setChecked(false);
		mNameDetails.setClickable(false);
		mAddressDetails.setClickable(false);
		mPhotoDetails.setClickable(false);
		mReviewDetails.setClickable(false);
	}

	public void clearStorePhotoReviewData(){
		mStorePhoto1.setImageBitmap(null);
		mStorePhoto2.setImageBitmap(null);
		mStorePhoto1Base64 = "";
		mStorePhoto2Base64 = "";
		mStoreReviewDescription.getText().clear(); 
		mFirstRateButton.setBackgroundResource(R.drawable.button_border);
		mSecondRateButton.setBackgroundResource(R.drawable.button_border);
		mThirdRateButton.setBackgroundResource(R.drawable.button_border);
		mFourthRateButton.setBackgroundResource(R.drawable.button_border);
		mFifthRateButton.setBackgroundResource(R.drawable.button_border);
		mStoreRating="0";
		mZouponsTermsAndConditionsCheckBox.setChecked(false);
		mNameDetails.setClickable(true);
		mAddressDetails.setClickable(true);
		mPhotoDetails.setClickable(false);
		mReviewDetails.setClickable(false);
	}

	public void clearStoreReviewData(){
		mStoreReviewDescription.getText().clear(); 
		mFirstRateButton.setBackgroundResource(R.drawable.button_border);
		mSecondRateButton.setBackgroundResource(R.drawable.button_border);
		mThirdRateButton.setBackgroundResource(R.drawable.button_border);
		mFourthRateButton.setBackgroundResource(R.drawable.button_border);
		mFifthRateButton.setBackgroundResource(R.drawable.button_border);
		mStoreRating="0";
		mZouponsTermsAndConditionsCheckBox.setChecked(false);
		mNameDetails.setClickable(true);
		mAddressDetails.setClickable(true);
		mPhotoDetails.setClickable(true);
		mReviewDetails.setClickable(false);
	}

	/*
	 * Info Code 
	 * **/
	protected void storeInfo(int bodyLayout){
		POJOMainMenuActivityTAG.TAG="MainMenuActivity_StoreInfo";
		app = inflater.inflate(bodyLayout, null);
		int MenuFlag =0;

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.store_info_tabbar);
		ViewGroup storeinfocontainer = (ViewGroup) app.findViewById(R.id.store_info_container);

		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.store_info_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_storeinfo);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));
		// Notification and log out variables
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Check whether to show Notification image in zoupons header
		new NotificationTabImageVisibility(MainMenuActivity.this, mNotificationImage).checkNotificationVisibility();
		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView));
		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		View[] children = new View[] { sLeftMenu, app, sRightMenu };

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

		mStoreName = (TextView) app.findViewById(R.id.store_title_textId);
		mStorePhoneNumber  = (TextView) app.findViewById(R.id.store_phonenumberId);
		mStoreWebsite = (TextView) app.findViewById(R.id.store_websiteId);
		mStoreDescription = (TextView) app.findViewById(R.id.store_description);
		mStorePhotoGrid = (GridView) app.findViewById(R.id.store_photos_gridId);
		mBusiness_Time = (TextView) app.findViewById(R.id.mBusiness_Time);
		mStoreGalleryInfoLayout = (LinearLayout) app.findViewById(R.id.mGallaryView);
		mBuisnessLayout = (LinearLayout) app.findViewById(R.id.mBusinessTimeView);
		storeinfo_rightmenu = (ImageView) storeinfocontainer.findViewById(R.id.store_info_rightmenu);

		mMondayBusinessStartTime = (TextView) app.findViewById(R.id.monday_businesstime_starttextId);
		mTuesdayBusinessStartTime = (TextView) app.findViewById(R.id.tuesday_businesstime_starttextId);
		mWednesdayBusinessStartTime = (TextView) app.findViewById(R.id.wednesday_businesstime_starttextId);
		mThursdayBusinessStartTime = (TextView) app.findViewById(R.id.thursday_businesstime_starttextId);
		mFridayBusinessStartTime = (TextView) app.findViewById(R.id.friday_businesstime_starttextId);
		mSaturdayBusinessStartTime = (TextView) app.findViewById(R.id.saturday_businesstime_starttextId);
		mSundayBusinessStartTime = (TextView) app.findViewById(R.id.sunday_businesstime_starttextId);
		mMondayBusinessEndTime = (TextView) app.findViewById(R.id.monday_businesstime_endtextId);
		mTuesdayBusinessEndTime = (TextView) app.findViewById(R.id.tuesday_businesstime_endtextId);
		mWednesdayBusinessEndTime = (TextView) app.findViewById(R.id.wednesday_businesstime_endtextId);
		mThursdayBusinessEndTime = (TextView) app.findViewById(R.id.thursday_businesstime_endtextId);
		mFridayBusinessEndTime = (TextView) app.findViewById(R.id.friday_businesstime_endtextId);
		mSaturdayBusinessEndTime = (TextView) app.findViewById(R.id.saturday_businesstime_endtextId);
		mSundayBusinessEndTime = (TextView) app.findViewById(R.id.sunday_businesstime_endtextId);
		mBusinessTimeCloseButton = (ImageView) app.findViewById(R.id.store_businesstime_closeId);
		storeinfo_rightmenu.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag=2, mMainMenuActivityFreezeView));

		final StoreInformation ma= new StoreInformation();

		StoreInformationClass mStoreInfo =ma.new StoreInformationClass(MainMenuActivity.this,mStoreName,mStorePhoneNumber,mStoreWebsite,mStoreDescription);
		mStoreInfo.execute();

		mBusiness_Time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mStoreGalleryInfoLayout.setVisibility(View.GONE);
				mBuisnessLayout.setVisibility(View.VISIBLE);
				BusinessTimeClass mBusinessObj = ma.new BusinessTimeClass(MainMenuActivity.this,mMondayBusinessStartTime,mTuesdayBusinessStartTime,mWednesdayBusinessStartTime,mThursdayBusinessStartTime,mFridayBusinessStartTime,mSaturdayBusinessStartTime,mSundayBusinessStartTime,
						mMondayBusinessEndTime,mTuesdayBusinessEndTime,mWednesdayBusinessEndTime,mThursdayBusinessEndTime,mFridayBusinessEndTime,mSaturdayBusinessEndTime,mSundayBusinessEndTime);
				mBusinessObj.execute();
			}
		});

		mBusinessTimeCloseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mStoreGalleryInfoLayout.setVisibility(View.VISIBLE);
				mBuisnessLayout.setVisibility(View.GONE);
			}
		});

		mStorePhotoGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if(arg2 == 0){
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
					startActivityForResult(cameraIntent, 100); 
				}else{
					if(WebServiceStaticArrays.mStorePhoto.size() >= arg2){
						Intent mStorePhotoViewSwitcher = new Intent(MainMenuActivity.this,StorePhotoSwitcher.class);
						mStorePhotoViewSwitcher.putExtra("position", arg2);
						mStorePhotoViewSwitcher.putExtra("Activity", "StoreInfo");
						startActivity(mStorePhotoViewSwitcher);	
					}

				}	
			}

		});

	}

	protected void viewStorePhotos(int layout){
		POJOMainMenuActivityTAG.TAG="MainMenuActivity_StorePhotos";
		app = inflater.inflate(layout, null);
		int MenuFlag =0;

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.store_info_tabbar);
		ViewGroup storeinfocontainer = (ViewGroup) app.findViewById(R.id.store_info_container);
		ViewGroup addphotoFooter = (ViewGroup) app.findViewById(R.id.store_photo_footer);
		mStoreName = (TextView) app.findViewById(R.id.store_title_textId);
		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.store_info_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_storeinfo);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));

		// Notification and log out variables
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Check whether to show Notification image in zoupons header
		new NotificationTabImageVisibility(MainMenuActivity.this, mNotificationImage).checkNotificationVisibility();
		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView));
		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		View[] children = new View[] { sLeftMenu, app, sRightMenu };

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
		storeinfo_rightmenu = (ImageView) storeinfocontainer.findViewById(R.id.store_info_rightmenu);
		storeinfo_rightmenu.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag=2, mMainMenuActivityFreezeView));

		mIndicator = (UnderlinePageIndicator)storeinfocontainer.findViewById(R.id.indicator);
		mAddStorePhotoButton = (Button) addphotoFooter.findViewById(R.id.store_addphotoId);
		if(getIntent().getExtras()!=null){
			position = getIntent().getExtras().getInt("position");
		}
		mStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);
		////////////////////////////////////////////////////////////////////
		mStoreAllPhotos = (GridView) storeinfocontainer.findViewById(R.id.storephotos_grid_view);
		mStorePhotosBackButton = (Button) addphotoFooter.findViewById(R.id.store_photo_backId);
		mStorePhotosBackMenuSplitter =  addphotoFooter.findViewById(R.id.back_menubar_splitterId);
		////////////////////////////////////////////////////////////////////

		mImagePager = (ViewPager) storeinfocontainer.findViewById(R.id.mImagePager);
		mPhotoSwitcherLeftArrow = (ImageView) storeinfocontainer.findViewById(R.id.storeimage_left_arrow);
		mPhotoSwitcherRightArrow = (ImageView) storeinfocontainer.findViewById(R.id.storeimage_right_arrow);

		mImagePager.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				mImagePagerWidth = mImagePager.getWidth();
				mImagePagerHeight = mImagePager.getHeight();
				mImagePager.getViewTreeObserver().removeGlobalOnLayoutListener( this );
				mImagePager.setVisibility( View.INVISIBLE );
			}
		});

		if(getIntent().getExtras().getString("Activity").equalsIgnoreCase("Photos")){
			mStoreAllPhotos.setVisibility(View.VISIBLE);
			mImagePager.setVisibility(View.INVISIBLE);
			mIndicator.setVisibility(View.INVISIBLE);
			mStorePhotosBackButton.setVisibility(View.INVISIBLE);
			mStorePhotosBackMenuSplitter.setVisibility(View.INVISIBLE);
			PhotoSwitcherAsynchThread mPhotoTask = new PhotoSwitcherAsynchThread(MainMenuActivity.this,mImagePagerWidth,mImagePagerHeight);
			mPhotoTask.execute("");
		}
		
		SharedPreferences mUserDetailsPrefs = getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");
		if(user_login_type.equalsIgnoreCase("customer")){  // Customer
			mAddStorePhotoButton.setVisibility(View.VISIBLE);
			mAddStorePhotoButton.setVisibility(View.VISIBLE);
		}else{
			mAddStorePhotoButton.setVisibility(View.INVISIBLE);
			mAddStorePhotoButton.setVisibility(View.INVISIBLE);
		}		
		

		mAddStorePhotoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// open file O/P stream to write the image 
				/*FileOutputStream op_stream = null;*/
				try {
					// to put captured image to uri specified... */
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					//cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(cameraIntent, 100);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		mStoreAllPhotos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				mStoreAllPhotos.setVisibility(View.GONE);
				mImagePager.setVisibility(View.VISIBLE);
				mIndicator.setVisibility(View.VISIBLE);
				mImagePagerWidth = mImagePager.getWidth();
				int widthpropotion = mImagePagerWidth/5;
				mImagePagerHeight = mImagePager.getHeight();
				//Log.i("window size", getWindowManager().getDefaultDisplay().getWidth()+" "+ getWindowManager().getDefaultDisplay().getHeight());
				mImagePager.setAdapter(new StorePhotoAdapter(MainMenuActivity.this,mImagePager.getWidth(),mImagePagerHeight));
				mImagePager.setCurrentItem(position);

				mPhotoSwitcherLeftArrow.setVisibility(View.VISIBLE);
				mPhotoSwitcherRightArrow.setVisibility(View.VISIBLE);

				if(position==0){
					if(WebServiceStaticArrays.mStorePhoto.size()==1){
						mPhotoSwitcherLeftArrow.setAlpha(100);
						mPhotoSwitcherRightArrow.setAlpha(100);
					}else{
						mPhotoSwitcherLeftArrow.setAlpha(100);
						mPhotoSwitcherRightArrow.setAlpha(255);
					}
				}else if(position==WebServiceStaticArrays.mStorePhoto.size()-1){
					mPhotoSwitcherLeftArrow.setAlpha(255);
					mPhotoSwitcherRightArrow.setAlpha(100);
				}else{
					mPhotoSwitcherLeftArrow.setAlpha(255);
					mPhotoSwitcherRightArrow.setAlpha(255);
				}

				mIndicator.setViewPager(mImagePager);
				mIndicator.setCurrentItem(position);
				mIndicator.setSelectedColor(getResources().getColor(R.color.blue));
				mStorePhotosBackButton.setVisibility(View.VISIBLE);
				mStorePhotosBackMenuSplitter.setVisibility(View.VISIBLE);
			}
		});

		mPhotoSwitcherLeftArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(WebServiceStaticArrays.mStorePhoto.size() > 1){
					mRightArrowFlag=false;
					if((mImagePager.getCurrentItem()-1)==0 || mImagePager.getCurrentItem()==0){
						mPhotoSwitcherLeftArrow.setAlpha(100);
						mPhotoSwitcherRightArrow.setAlpha(255);
						mLeftArrowFlag=true;
					}else{
						if(mLeftArrowFlag!=true){
							mPhotoSwitcherLeftArrow.setAlpha(255);
							mPhotoSwitcherRightArrow.setAlpha(255);
						}
					}
					mImagePager.setCurrentItem((mImagePager.getCurrentItem()-1), true);	
				}				
			}
		});

		mPhotoSwitcherRightArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(WebServiceStaticArrays.mStorePhoto.size() > 1){
					mLeftArrowFlag=false;
					if((mImagePager.getCurrentItem()+1)==(WebServiceStaticArrays.mStorePhoto.size()-1)){
						mPhotoSwitcherLeftArrow.setAlpha(255);
						mPhotoSwitcherRightArrow.setAlpha(100);
						mRightArrowFlag=true;
					}else{
						if(mRightArrowFlag!=true){
							mPhotoSwitcherLeftArrow.setAlpha(255);
							mPhotoSwitcherRightArrow.setAlpha(255);
						}
					}
					mImagePager.setCurrentItem((mImagePager.getCurrentItem()+1), true);
				}
			}
		});

		mStorePhotosBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(getIntent().getExtras().getString("Activity").equalsIgnoreCase("Photos")){
					mStoreAllPhotos.setVisibility(View.VISIBLE);
					mImagePager.setVisibility(View.INVISIBLE);
					mIndicator.setVisibility(View.INVISIBLE);
					mPhotoSwitcherLeftArrow.setVisibility(View.INVISIBLE);
					mPhotoSwitcherRightArrow.setVisibility(View.INVISIBLE);
					mStorePhotosBackButton.setVisibility(View.INVISIBLE);
					mStorePhotosBackMenuSplitter.setVisibility(View.INVISIBLE);
				}else{}
			}
		});
		
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				Log.i("ViewPager listener", "On Page Selected");
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				Log.i("ViewPager listener", "On Page Scrolled " + arg0 );
			}
			
			@Override
			public void onPageScrollStateChanged(int position) {
				// TODO Auto-generated method stub
				Log.i("ViewPager listener", "On Page Scroll state changed" + "  Position " + position);
				if(mImagePager.getCurrentItem() == 0){
					mPhotoSwitcherLeftArrow.setAlpha(100);
					mPhotoSwitcherRightArrow.setAlpha(255);
				}else if(mImagePager.getCurrentItem() == WebServiceStaticArrays.mStorePhoto.size()-1){
					mPhotoSwitcherLeftArrow.setAlpha(255);
					mPhotoSwitcherRightArrow.setAlpha(100);
				}else{
					mPhotoSwitcherLeftArrow.setAlpha(255);
					mPhotoSwitcherRightArrow.setAlpha(255);
				}
			}
		});

	}

	protected void GiftCardDetails(int layout){
     	app = inflater.inflate(layout, null);
		int MenuFlag =0;

		mExtra = getIntent().getExtras().getString("CardType"); //GiftCard - Regular / Deal - ZCard
		mGiftCardStart ="0";
		mGiftCardEndLimit ="20";
		mGiftCardTotalCount ="0";
		mDealStart ="0";
		mDealEndLimit ="20";
		mDealTotalCount ="0";

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.store_info_tabbar);
		ViewGroup storeinfocontainer = (ViewGroup) app.findViewById(R.id.store_info_container);
		ViewGroup mGiftcardFooterLayout = (ViewGroup) app.findViewById(R.id.giftcards_footerLayoutId);

		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.store_info_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_storeinfo);
		// Notification and log out variables
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Check whether to show Notification image in zoupons header
		new NotificationTabImageVisibility(MainMenuActivity.this, mNotificationImage).checkNotificationVisibility();
		mStoreCardsSellPriceHeader = (TextView) app.findViewById(R.id.cards_priceId);
		mStoreCardsFaceValueHeader =(TextView) app.findViewById(R.id.facevalue_textId);
		mChooseCardContainer = (LinearLayout) storeinfocontainer.findViewById(R.id.choose_card_layout);
		mGiftcardCheckBox = (CheckBox) mChooseCardContainer.findViewById(R.id.giftcards_checkboxId);
		mZCardCheckBox = (CheckBox) mChooseCardContainer.findViewById(R.id.zcards_checkboxId);
		mStoreNameHeader = (TextView) storeinfocontainer.findViewById(R.id.store_title_textId);
		mStoreNameHeader.setText(RightMenuStoreId_ClassVariables.mStoreName);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));

		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView));
		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		View[] children = new View[] { sLeftMenu, app, sRightMenu };
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

		storeinfo_rightmenu = (ImageView) storeinfocontainer.findViewById(R.id.store_info_rightmenu);
		storeinfo_rightmenu.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag=2, mMainMenuActivityFreezeView));
		mCardDetailsList = (ListView) storeinfocontainer.findViewById(R.id.gift_card_listId);
		mBackText = (TextView) mGiftcardFooterLayout.findViewById(R.id.giftcards_backtextId);

		if(getIntent().hasExtra("bothcards")){ // If for displaying both Giftcards and Deal cards
			mRemainingHeaderId = (TextView) storeinfocontainer.findViewById(R.id.store_giftcardcard_remainingid);
			mChooseCardContainer.setVisibility(View.VISIBLE);
			mGiftcardCheckBox.setChecked(true);
			mStoreNameHeader.setVisibility(View.GONE);
			storeinfo_rightmenu.setVisibility(View.GONE);
			mGiftcardFooterLayout.setVisibility(View.VISIBLE);
			if(mRemainingHeaderId!=null){
				mRemainingHeaderId.setVisibility(View.GONE);
			}
		}else{ // Either Giftcard or deal card

			/*
			 * To Clear FB friend information after sending giftcard to friend
			 * **/
			Friends.friendName=""; 
			Friends.friendId="";
			Friends.isZouponsFriend="";
			Friends.friendEmailId ="";

			mChooseCardContainer.setVisibility(View.INVISIBLE);
			mGiftcardCheckBox.setChecked(false);
			mStoreNameHeader.setVisibility(View.VISIBLE);
			storeinfo_rightmenu.setVisibility(View.VISIBLE);
			mGiftcardFooterLayout.setVisibility(View.GONE);
		}

		//For Footer Layout
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
		mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		mFooterProgress = (ProgressBar) mFooterLayout.findViewById(R.id.mProgressBar);
		mCardDetailsList.addFooterView(mFooterLayout);

		StoreDetailsAsyncTask mStoreDetailsTask = new StoreDetailsAsyncTask(MainMenuActivity.this,mExtra,mCardDetailsList,"PROGRESS");
		mStoreDetailsTask.execute("");

		mCardDetailsList.setOnScrollListener(new CardDetailsListScrollListener());

		mBackText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();	
			}
		});


		mGiftcardCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mGiftCardStart ="0";
					mGiftCardEndLimit ="20";
					mGiftCardTotalCount ="0";

					mExtra = "Regular";
					if(mCardDetailsList.getFooterViewsCount() == 0){
						mCardDetailsList.addFooterView(mFooterLayout);
						mFooterText.setVisibility(View.INVISIBLE);
						mFooterProgress.setVisibility(View.INVISIBLE);
					}
					// To set visibility of sell Price 
					mStoreCardsSellPriceHeader.setVisibility(View.GONE);
					mStoreCardsFaceValueHeader.setPadding(0, 0, 0, 0);
					mStoreCardsFaceValueHeader.setText("Face Value");
					mZCardCheckBox.setChecked(false);
					mCardDetailsList.setAdapter(null);
					mCardDetailsList.setBackgroundResource(0);
					if(mRemainingHeaderId!=null){
						mRemainingHeaderId.setVisibility(View.GONE);
					}
					StoreDetailsAsyncTask mStoreDetailsTask = new StoreDetailsAsyncTask(MainMenuActivity.this,"Regular",mCardDetailsList,"PROGRESS");
					mStoreDetailsTask.execute("");
				}				
			}
		});

		mZCardCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mDealStart ="0";
					mDealEndLimit ="20";
					mDealTotalCount ="0";

					mExtra = "Zcard";
					if(mCardDetailsList.getFooterViewsCount() == 0){
						mCardDetailsList.addFooterView(mFooterLayout); 
						mFooterText.setVisibility(View.INVISIBLE);
						mFooterProgress.setVisibility(View.INVISIBLE);
					}
					// To set visibility of sell Price 
					mStoreCardsSellPriceHeader.setVisibility(View.VISIBLE);
					mStoreCardsFaceValueHeader.setPadding(0, 0, 80, 0);
					mStoreCardsFaceValueHeader.setText("Face Value |");
					mGiftcardCheckBox.setChecked(false);
					mCardDetailsList.setAdapter(null);
					mCardDetailsList.setBackgroundResource(0);
					if(mRemainingHeaderId!=null){
						mRemainingHeaderId.setVisibility(View.VISIBLE);
					}
					StoreDetailsAsyncTask mStoreDetailsTask = new StoreDetailsAsyncTask(MainMenuActivity.this,"ZCard",mCardDetailsList,"PROGRESS");
					mStoreDetailsTask.execute("");
				}				
			}
		});
	}

	public class CardDetailsListScrollListener implements OnScrollListener{

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			//Check the bottom item is visible
			int lastInScreen = firstVisibleItem + visibleItemCount;	 			
			if(mExtra.equalsIgnoreCase("Regular")){

				if(Integer.parseInt(mGiftCardTotalCount) > lastInScreen && Integer.parseInt(mGiftCardTotalCount) >20){					
					if(lastInScreen == totalItemCount && !mIsLoadMore){												
						if(mCardDetailsList.getFooterViewsCount() == 0){
							mCardDetailsList.addFooterView(mFooterLayout);
						}
						mFooterText.setVisibility(View.VISIBLE);
						mFooterProgress.setVisibility(View.VISIBLE);
						if(Integer.parseInt(mGiftCardTotalCount) > Integer.parseInt(mGiftCardEndLimit)){
							mFooterText.setText("Loading "+mGiftCardEndLimit+" of "+"("+mGiftCardTotalCount+")");
						}else{
							mFooterText.setText("Loading "+mGiftCardTotalCount);
						}
						System.out.println("From :"+mExtra);
						StoreDetailsAsyncTask mStoreDetailsTask = new StoreDetailsAsyncTask(MainMenuActivity.this,mExtra,mCardDetailsList,"NOPROGRESS");
						mStoreDetailsTask.execute("RefreshAdapter");
					}
				}else{
					if(mGiftCardTotalCount.equalsIgnoreCase("0")){
					}else if(mFooterLayout != null && mCardDetailsList.getFooterViewsCount() !=0 && mCardDetailsList.getAdapter() != null){
						if(lastInScreen!= totalItemCount){
							Log.i(TAGG, "Remove Footer success");
							mCardDetailsList.removeFooterView(mFooterLayout);	
						}else{
							Log.i(TAGG, "Remove Footer wait");
						}
					}
				}
			}else{
				//Deal
				if(Integer.parseInt(mDealTotalCount) > lastInScreen && Integer.parseInt(mDealTotalCount)>20){					
					if(lastInScreen == totalItemCount && !mIsLoadMore){												
						if(mCardDetailsList.getFooterViewsCount() == 0){
							mCardDetailsList.addFooterView(mFooterLayout);
						}
						mFooterText.setVisibility(View.VISIBLE);
						mFooterProgress.setVisibility(View.VISIBLE);
						if(Integer.parseInt(mDealTotalCount) > Integer.parseInt(mDealEndLimit)){
							mFooterText.setText("Loading "+mDealEndLimit+" of "+"("+mDealTotalCount+")");
						}else{
							mFooterText.setText("Loading "+mDealTotalCount);
						}

						System.out.println("From :"+mExtra);
						StoreDetailsAsyncTask mStoreDetailsTask = new StoreDetailsAsyncTask(MainMenuActivity.this,mExtra,mCardDetailsList,"NOPROGRESS");
						mStoreDetailsTask.execute("RefreshAdapter");
					}
				}else{	 				
					if(mDealTotalCount.equalsIgnoreCase("0")){
					}else if(mFooterLayout != null && mCardDetailsList.getFooterViewsCount() !=0 && mCardDetailsList.getAdapter() != null){
						if(lastInScreen!= totalItemCount){
							Log.i(TAGG, "Remove Footer success");
							mCardDetailsList.removeFooterView(mFooterLayout);	
						}else{
							Log.i(TAGG, "Remove Footer wait");
						}
					}

				}
			}
		}
	}

	public void SetArrayGiftCardDealListAdatpter(String mServiceFunction,ArrayList<Object> mStoreRegularCardDetails,String mRefreshAdapterStatus) {
		String FriendName = "",mFriendId="",mIsZouponsFriend="",mFriendEmailID="";

		if(!Friends.friendEmailId.equals("")){
			FriendName = Friends.friendName; 
			mFriendId = Friends.friendId;
			mIsZouponsFriend = Friends.isZouponsFriend;
			mFriendEmailID = Friends.friendEmailId;
		}else{
			Friends.friendName=""; 
			Friends.friendId="";
			Friends.isZouponsFriend="";
			Friends.friendEmailId ="";
		}

		if(mServiceFunction.equalsIgnoreCase("Regular")){			

			if(mRefreshAdapterStatus.equalsIgnoreCase("RefreshAdapter") && mGiftCardAdapter != null){
				mGiftCardAdapter.notifyDataSetChanged();
			}else {
				Log.i(TAGG, "Set List Adapter");
				mGiftCardAdapter = new CustomCardDetailsAdapter(MainMenuActivity.this,R.layout.cardlist_row,mServiceFunction,WebServiceStaticArrays.mStoreRegularCardDetails,FriendName,mFriendId,mIsZouponsFriend,mFriendEmailID);
				mCardDetailsList.setAdapter(mGiftCardAdapter);
			}

			if(mFooterLayout != null && mCardDetailsList.getFooterViewsCount() !=0 && mCardDetailsList.getAdapter() != null){
				Log.i(TAGG, "Both Not NUll");
				mCardDetailsList.removeFooterView(mFooterLayout);
			}
		}else{
			if(mRefreshAdapterStatus.equalsIgnoreCase("RefreshAdapter") && mDealAdapter != null){
				mDealAdapter.notifyDataSetChanged();
			}else{
				mDealAdapter = new CustomCardDetailsAdapter(MainMenuActivity.this,R.layout.cardlist_row,mServiceFunction,WebServiceStaticArrays.mStoreZouponsDealCardDetails,FriendName,mFriendId,mIsZouponsFriend,mFriendEmailID);
				mCardDetailsList.setAdapter(mDealAdapter);
			}

			if(mFooterLayout != null && mCardDetailsList.getFooterViewsCount() !=0 && mCardDetailsList.getAdapter() != null){
				Log.i(TAGG, "Both Not NUll");
				mCardDetailsList.removeFooterView(mFooterLayout);
			}
		}
	}

	protected void fbFriendList(int layout){

		bundle_mainactivity=this.getIntent().getExtras();
		if(bundle_mainactivity!=null){
			mClassName = bundle_mainactivity.getString("classname");
			mFaceValue = bundle_mainactivity.getString("facevalue");
		}else{
			mClassName = "";
			mFaceValue = "";
		}

		POJOMainMenuActivityTAG.TAG="MainMenuActivity_FbFriendsList";
		app = inflater.inflate(layout, null);
		int MenuFlag =0;

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.store_info_tabbar);
		ViewGroup friends_search_container = (ViewGroup) app.findViewById(R.id.fb_friendsearch_layout);
		friendlistgroup = (ViewGroup) app.findViewById(R.id.store_info_container);
		SocialfriendsOptionsContainer = (ViewGroup) app.findViewById(R.id.social_import_friend_container);
		mSocialTypesList = (ListView) SocialfriendsOptionsContainer.findViewById(R.id.social_listId);
		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.store_info_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_storeinfo);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count); 
		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView));
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();

		View[] children = new View[] { sLeftMenu, app };
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
		mFriendlistSearch = (EditText) friends_search_container.findViewById(R.id.fb_friendlist_searchId);  
		mfriendlist = (ListView) friendlistgroup.findViewById(R.id.fb_friendlistId);
		mSearchCancel = (Button) friendlistgroup.findViewById(R.id.fb_friendsearch_buttonId);

		ViewGroup friendsfooterContainer = (ViewGroup) app.findViewById(R.id.friends_footerLayoutId); 
		mBackText = (TextView) friendsfooterContainer.findViewById(R.id.friends_leftFooterText);
		importmenusplitter = friendsfooterContainer.findViewById(R.id.friendlist_importmenusplitter);
		mImportFriendMenu = (TextView) friendsfooterContainer.findViewById(R.id.friends_import_friends_menuId);
		
		if(WebServiceStaticArrays.mSocialNetworkFriendList.size() > 0){
			mfriendlist.setAdapter(new CustomFBFriendListAdapter(this,WebServiceStaticArrays.mSocialNetworkFriendList,"friends"));
		}else{
			// To load contact friend list
			ContactListService mGetContacts = new ContactListService(this,mfriendlist,"friends");
			mGetContacts.execute();
		}
		
		/*GetSocialFriendsTask mFriendTask = new GetSocialFriendsTask(MainMenuActivity.this,mfriendlist,friendlistgroup,SocialfriendsOptionsContainer,mImportFriendMenu,importmenusplitter,"friends");
		mFriendTask.execute();*/
		mBackText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();	
			}
		});
		// To add text watcher for searching friend list
		mFriendlistSearch.addTextChangedListener(new SearchFriendsTextWatcher(MainMenuActivity.this, mfriendlist,"friends"));

		mSearchCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFriendlistSearch.getText().clear();
				mfriendlist.setAdapter(new CustomFBFriendListAdapter(MainMenuActivity.this,WebServiceStaticArrays.mSocialNetworkFriendList,"friends"));
				// To hide soft keyboard
				InputMethodManager softkeyboardevent = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				softkeyboardevent.hideSoftInputFromWindow(mFriendlistSearch.getWindowToken(), 0);
			}
		});

		mfriendlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View arg1, int position,long arg3) {
				POJOFBfriendListData mFriendData = (POJOFBfriendListData) adapterview.getAdapter().getItem(position);
				Intent friendnotecall = new Intent(MainMenuActivity.this,FBNotesDescription.class);
				friendnotecall.putExtra("name",mFriendData.name);
				friendnotecall.putExtra("classname",mClassName);
				friendnotecall.putExtra("sendto", mFriendData.friend_id);
				friendnotecall.putExtra("friend_emailaddress", mFriendData.friend_email);
				friendnotecall.putExtra("eventflag", mFriendData.zouponsfriend);
				friendnotecall.putExtra("card_id", getIntent().getExtras().getString("card_id"));
				friendnotecall.putExtra("cardvalue", getIntent().getExtras().getString("cardvalue"));
				friendnotecall.putExtra("facevalue", mFaceValue);
				friendnotecall.putExtra("timezone", mFriendData.timezone);
				startActivityForResult(friendnotecall,200);
			}
		});

		
	}

	protected void FBNotesDescription(int layout) {
		POJOMainMenuActivityTAG.TAG="MainMenuActivity_FbNotesDescription";
		app = inflater.inflate(layout, null);
		int MenuFlag =0;

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.store_info_tabbar);
		ViewGroup storeinfocontainer = (ViewGroup) app.findViewById(R.id.store_info_container);
		ViewGroup datelayoutcontainer = (ViewGroup) app.findViewById(R.id.friend_notesDateFieldLayoutId);
		ViewGroup footercontainer = (ViewGroup) app.findViewById(R.id.notes_footer);
		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.store_info_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_storeinfo);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();

		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView));

		View[] children = new View[] { sLeftMenu, app};
		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
		mNotesFriendName = (TextView) storeinfocontainer.findViewById(R.id.notes_friendname);
		mNotesDescription = (EditText) storeinfocontainer.findViewById(R.id.friend_notesdescription);
		mNotesDateField =  (ImageView) datelayoutcontainer.findViewById(R.id.friend_notesDateFieldId);
		mNotesCancelButton = (Button) storeinfocontainer.findViewById(R.id.friend_notes_cancelbuttonId);
		mNotesSendButton = (Button) storeinfocontainer.findViewById(R.id.friend_notes_sendbuttonId);
		mNotesDateText = (EditText) storeinfocontainer.findViewById(R.id.friend_notesDateId);
		mNotesBack = (TextView) footercontainer.findViewById(R.id.friend_notes_backId);

		mNotesFriendName.setText(getIntent().getExtras().getString("name"));

		mNotesDateText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fbnotes_datepicker();	
			}
		});

		mNotesDateField.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fbnotes_datepicker();
			}
		});

		mNotesCancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();	
			}
		});

		mNotesSendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mNotesDateText.getText().toString().trim().equals("")){
					alertBox_service("Information", "Please Select date to proceed");
				}else{
					String mEventFlag;
					if(getIntent().getExtras().getString("classname").equals("MyGiftCards")){
						if(getIntent().getExtras().getString("eventflag").equalsIgnoreCase("yes")){
							mEventFlag="Yes";
						}else {
							mEventFlag="No";
						}
						String mSendTo = getIntent().getExtras().getString("sendto");
						String mDate[] = mNotesDateText.getText().toString().trim().split("/");
						String mMonth = pad(Integer.parseInt(mDate[0]));
						String mDay = pad(Integer.parseInt(mDate[1]));
						String mYear = mDate[2];
						String mSendDate = mYear+"-"+mMonth+"-"+mDay;
						String mFriendEmailAddress = getIntent().getExtras().getString("friend_emailaddress");
						SendGiftCardTask sendgiftcardtask = new SendGiftCardTask(MainMenuActivity.this,getIntent().getExtras().getString("timezone"));
						sendgiftcardtask.execute(mSendTo,mSendDate,MenuUtilityClass.sGiftCardId,mNotesDescription.getText().toString().trim(),mEventFlag,mFriendEmailAddress);
					}else{
						Intent intent_step2 = new Intent(MainMenuActivity.this,MobilePay.class);
						intent_step2.putExtra("datasourcename", "cards_sendtofriend"); //flag to set control from cards self use option.
						//Selected Card [Giftcard/zcard] Information
						intent_step2.putExtra("card_id", getIntent().getExtras().getString("card_id")); // selected giftcard id
						intent_step2.putExtra("cardvalue", getIntent().getExtras().getString("cardvalue"));	//Selected GiftCard facevalue
						intent_step2.putExtra("facevalue", getIntent().getExtras().getString("facevalue"));
						// Selected Friend Information 
						intent_step2.putExtra("friend_name",getIntent().getExtras().getString("name"));
						intent_step2.putExtra("friend_id", getIntent().getExtras().getString("sendto"));
						intent_step2.putExtra("friend_email", getIntent().getExtras().getString("friend_emailaddress"));
						intent_step2.putExtra("is_zouponsfriend", getIntent().getExtras().getString("eventflag"));
						// Add Notes Information
						if(mNotesDescription.getText().toString().trim().length()==0){
							intent_step2.putExtra("notes_description", "");	
						}else{
							intent_step2.putExtra("notes_description", mNotesDescription.getText().toString());
						}
						// to format date to send to webservice
						String[] dateString = mNotesDateText.getText().toString().split("/");
						Log.i("choosed date", dateString[2]+"-"+pad((Integer.parseInt(dateString[0])))+"-"+pad(Integer.parseInt(dateString[1])));
						intent_step2.putExtra("date",dateString[2]+"-"+pad((Integer.parseInt(dateString[0])))+"-"+pad(Integer.parseInt(dateString[1])));
						//date time zone
						intent_step2.putExtra("timezone", getIntent().getExtras().getString("timezone"));
						// Extra friend and note info have to be passed...
						startActivity(intent_step2);
						StoreCardDetails.finish_handler.sendEmptyMessage(0);
						if(FriendList.finish_handler!=null){
							FriendList.finish_handler.sendEmptyMessage(0);
						}
						MainMenuActivity.this.finish();
					}
				}
			}
		});

		mNotesBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});

	}

	public void fbnotes_datepicker(){
		mDatePicker = new DatePickerDialog(MainMenuActivity.this,new OnDateSetListener() {
			boolean fired = false;	
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				if(fired==true){
					Log.i(TAGG,"Alert box has showned already");
				}else {
					fired = true;
					Calendar mCurrentTime = Calendar.getInstance();
					int mDate = mCurrentTime.get(Calendar.DAY_OF_MONTH);
					int mMonth = mCurrentTime.get(Calendar.MONTH);
					int mYear = mCurrentTime.get(Calendar.YEAR);
					Date mCurrentDate = new Date(mYear,mMonth,mDate);
					Date mSelectedDate = new Date(year, monthOfYear, dayOfMonth);
					if(mCurrentDate.compareTo(mSelectedDate) <= 0){
						//mNotesDateText.setText(mSelectedDate.getDate()+"/"+mSelectedDate.getMonth()+"/"+mSelectedDate.getYear());
						mNotesDateText.setText((mSelectedDate.getMonth()+1)+"/"+mSelectedDate.getDate()+"/"+mSelectedDate.getYear());
					}else{
						AlertDialog.Builder mAlertBox = new AlertDialog.Builder(MainMenuActivity.this);
						mAlertBox.setTitle("Information");
						mAlertBox.setMessage("Please choose Future date");
						mAlertBox.setPositiveButton("ok", null);
						mAlertBox.show();
					}
				}
			}	
		}, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		mDatePicker.show();
	}

	public void loginToFacebook(final String type){
		OpenRequest open = new OpenRequest(this);
		open.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		open.setPermissions(Arrays.asList(new String[]{"email", "publish_actions"}));

		final Session fb_session = new Session(this);
		Session.setActiveSession(fb_session);
		fb_session.addCallback(new Session.StatusCallback() {

			@SuppressWarnings("deprecation")
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					UserDetails.mServiceAccessToken=session.getAccessToken();
					// make request to the /me API
					com.facebook.Request.executeMeRequestAsync(session, new com.facebook.Request.GraphUserCallback() {

						@Override
						public void onCompleted(GraphUser user,
								Response response) {
							if (user != null) {
								UserDetails.mServiceFbId=user.getId();
								fb_session.close();
								fb_session.closeAndClearTokenInformation();
								Message msg = new Message();
								msg.obj=type;
								handler_facebook.sendMessage(msg); 
							}else{
								Log.i("User details", "null");
							}
						}
					});
				}else{
					Log.i("session staus", "not opened");
				}
			}
		});
		fb_session.openForPublish(open);

	}

	private Handler handler_facebook = new Handler(){
		public void handleMessage(Message msg) {
			progressdialog.dismiss();
			if(msg.obj.equals("cancel")){
				alertBox_service("Information", "You have to loggin to Facebook for further process.");
			}else if(msg.obj.equals("sharestore")){
				ShareStoreAsynchThread sharestoreasynchthread = new ShareStoreAsynchThread(MainMenuActivity.this,mSocialFBLike);
				sharestoreasynchthread.execute("FB","JustLogged");
			}
		}
	};

	public String getRealPathFromURI(Uri contentUri) 
	{
		String[] proj = { MediaStore.Audio.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private Bitmap decodeImage(String imagepath){
		Bitmap bmp = null;
		int orientation;
		try{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inDither=false;                     //Disable Dithering mode
			options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
			options.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imagepath, options);
			//Decode with inSampleSize
			BitmapFactory.Options options_new = new BitmapFactory.Options();
			long heapSize = (Runtime.getRuntime().maxMemory()/1024)/1024;
			Log.i("heap", heapSize+"");
			int widthpropotion = mImagePager.getWidth()/5;
			mImagePagerHeight = widthpropotion*4;
			Log.i("size", mImagePager.getWidth() + " "+ mImagePagerHeight + " " + mImagePager.getHeight());
			options_new.inSampleSize = calculateInSampleSize(options,1000, 800);
			Log.i("sampple size",options_new.inSampleSize + " ");
			options_new.inJustDecodeBounds=false;
			bmp= BitmapFactory.decodeFile(imagepath,options_new);
			bmp = Bitmap.createScaledBitmap(bmp,1000,800, true);
			ExifInterface exif = new ExifInterface(imagepath);
			orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			//exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);
			Matrix m = new Matrix();
			if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
				
				m.postRotate(180,0,0);
				//m.postScale((float) bm.getWidth(), (float) bm.getHeight());
				// if(m.preRotate(90)){
				bmp = Bitmap.createBitmap(bmp, 0, 0, 1000,	800, m, true);
				return bmp;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				m.postRotate(90,0,0); 
				bmp = Bitmap.createBitmap(bmp, 0, 0, 1000,	800, m, true);
				return bmp;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				m.postRotate(270);
				bmp = Bitmap.createBitmap(bmp, 0, 0, 1000,	800, m, true);
				return bmp;
			} 

			return bmp;
		}catch(Exception e){
			e.printStackTrace();
			return bmp;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		try{
			if(requestCode == 100 && resultCode == RESULT_OK){

				POJOStorePhoto.add_photo_data = " ";

				String imagepath = getRealPathFromURI(data.getData());
				ExifInterface exif = new ExifInterface(imagepath);
				exif.getAttribute(ExifInterface.TAG_ORIENTATION);
				Log.i("orientation check", exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)+"");
				File mPhotoFile = new File(imagepath);
				if(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1) == ExifInterface.ORIENTATION_NORMAL){
					StoreDetailsAsyncTask mStorePhotosTask = new StoreDetailsAsyncTask(MainMenuActivity.this,"AddPhoto",null,"PROGRESS",mPhotoFile);
					startMyTask(mStorePhotosTask);
				}else{
					System.gc();
					Bitmap bitmap = decodeImage(imagepath);
					ByteArrayOutputStream outputstream=new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG,100,outputstream);
					byte[] imagedata=outputstream.toByteArray();
					bitmap.recycle();
					bitmap = null;
					FileOutputStream mStorePhotoFileStream = new FileOutputStream(mPhotoFile);
	                mStorePhotoFileStream.write(imagedata);
					imagedata = null;
	         		StoreDetailsAsyncTask mStorePhotosTask = new StoreDetailsAsyncTask(MainMenuActivity.this,"AddPhoto",null,"PROGRESS",mPhotoFile);
					startMyTask(mStorePhotosTask);
					mStorePhotoFileStream.close();
				}
					
			}else if(requestCode==200 && resultCode == RESULT_OK){
				Log.i("activity","onresult");
				finish();
			}else if(requestCode==300 && resultCode == RESULT_OK){
				setScaledBitmap(data, mStorePhoto1, true);
			}else if(requestCode==400 && resultCode == RESULT_OK){
				setScaledBitmap(data, mStorePhoto1, true);
			}else if(requestCode==500 && resultCode == RESULT_OK){
				setScaledBitmap(data, mStorePhoto2, false);
			}else if(requestCode==600 && resultCode == RESULT_OK){
				setScaledBitmap(data, mStorePhoto2, false);
			}else if(requestCode==700 && resultCode == RESULT_OK){
				if(data.hasExtra("import_status") && data.getExtras().getBoolean("import_status")){
					SocialfriendsOptionsContainer.setVisibility(View.GONE);
					if(!getClass().getSimpleName().toString().equalsIgnoreCase("ShareStore")){ // From friends
						friendlistgroup.setVisibility(View.VISIBLE);
						GetSocialFriendsTask mFriendListTask = new GetSocialFriendsTask(MainMenuActivity.this,mfriendlist,friendlistgroup,SocialfriendsOptionsContainer,mImportFriendMenu,importmenusplitter,"friends");
						startMyAsyncTask(mFriendListTask);
					}else{ // From Social
						friendlistgroup.setVisibility(View.VISIBLE);
						mSocialBackMenu.setVisibility(View.INVISIBLE);
						mBackMenuSplitter.setVisibility(View.INVISIBLE);

						GetSocialFriendsTask mFriendListTask = new GetSocialFriendsTask(MainMenuActivity.this,mfriendlist,friendlistgroup,SocialfriendsOptionsContainer,mImportFriendMenu,importmenusplitter,"social",mSocialTypesList);
						startMyAsyncTask(mFriendListTask);
					}
					mImportFriendMenu.setVisibility(View.VISIBLE);
					importmenusplitter.setVisibility(View.VISIBLE);

				}else if(data.hasExtra("import_status") && !data.getExtras().getBoolean("import_status")){
					SocialfriendsOptionsContainer.setVisibility(View.GONE);
					if(!getClass().getSimpleName().toString().equalsIgnoreCase("ShareStore")){ // From friends
						friendlistgroup.setVisibility(View.VISIBLE);	
					}else{ // From Social
						friendlistgroup.setVisibility(View.VISIBLE);
						mSocialBackMenu.setVisibility(View.INVISIBLE);
						mBackMenuSplitter.setVisibility(View.INVISIBLE);
					}
					mImportFriendMenu.setVisibility(View.VISIBLE);
					importmenusplitter.setVisibility(View.VISIBLE);
				}
			}
			if(mFacebookUILifeCycleHelper!= null){
				mFacebookUILifeCycleHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
					@Override
					public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
						Log.e("FB Share", String.format("Error: %s", error.toString()));
						if(error.toString().contains("User canceled login")){
							
						}else{
							alertBox_service("Information", "Failed to share to FB");	
						}
					}

					@Override
					public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
						String status = FacebookDialog.getNativeDialogCompletionGesture(data);
						Log.i("FBShare", "Success!" + "  status: "+FacebookDialog.getNativeDialogDidComplete(data) + "" +data.toString());
						if(!status.equalsIgnoreCase("cancel"))
							alertBox_service("Information", "Successfully Shared via FB");
					}
				});
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setScaledBitmap(Intent data,ImageView mStorePhoto,boolean isStorePhoto1){
		try{
			Uri uri=data.getData();
			// specifying column(data) for retrieval
			String[] file_path_columns={MediaStore.Images.Media.DATA};
			// querying content provider to get particular image
			Cursor cursor=getContentResolver().query(uri, file_path_columns, null, null, null);
			cursor.moveToFirst();
			// getting col_index from string file_path_columns[0]--> Data column 
			int column_index=cursor.getColumnIndex(file_path_columns[0]);
			// getting the path from result as /sdcard/DCIM/100ANDRO/file_name
			String selected_file_path=cursor.getString(column_index);
			cursor.close();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(selected_file_path, options);
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, mStorePhoto.getWidth(),mStorePhoto.getHeight());
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap mSelectedImage = BitmapFactory.decodeFile(selected_file_path, options);
			mSelectedImage = new DecodeImageWithRotation().decodeImage(selected_file_path, mSelectedImage, mStorePhoto.getWidth(), mStorePhoto.getHeight());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);    
			byte[] imagedata = baos.toByteArray();
			if(isStorePhoto1)
				mStorePhoto1Base64= com.us.zoupons.Base64.encodeBytes(imagedata);
			else
				mStorePhoto2Base64= com.us.zoupons.Base64.encodeBytes(imagedata);
			mStorePhoto.setImageBitmap(mSelectedImage);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	
	void startMyAsyncTask(AsyncTask<Void, String, String> asyncTask) {
		if(Build.VERSION.SDK_INT >= 11)
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			asyncTask.execute();
	}

	protected void favorites(int bodyLayout){
		POJOMainMenuActivityTAG.TAG="MainMenuActivity_Favorites";
		mFavoritesScreenWidth=getScreenWidth(); 	
		if(mFavoritesScreenWidth>0){	//To fix Home Page menubar items width
			mFavoritesMenuWidth=mFavoritesScreenWidth/3;
		}

		app = inflater.inflate(bodyLayout, null);
		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.favorites_tabBar);
		ViewGroup favoritescontainer = (ViewGroup) app.findViewById(R.id.favorites_container);
		ViewGroup favoritesmenubarcontainer = (ViewGroup) app.findViewById(R.id.menubarcontainer_favorites);
		ViewGroup favoriteslistviewcontainer = (ViewGroup) favoritescontainer.findViewById(R.id.favorites_listview_container);

		mMenuBarFavStores=(LinearLayout) favoritesmenubarcontainer.findViewById(R.id.menubar_favstores);
		mMenuBarFavStores.setLayoutParams(new LinearLayout.LayoutParams((int)mFavoritesMenuWidth,LayoutParams.FILL_PARENT,1f));
		mMenuBarFavCoupons=(LinearLayout) favoritesmenubarcontainer.findViewById(R.id.menubar_favcoupons);
		mMenuBarFavCoupons.setLayoutParams(new LinearLayout.LayoutParams((int)mFavoritesMenuWidth,LayoutParams.FILL_PARENT,1f));
		mMenuBarFriendsFavStores=(LinearLayout) favoritesmenubarcontainer.findViewById(R.id.menubar_friendsfavstores);
		mMenuBarFriendsFavStores.setLayoutParams(new LinearLayout.LayoutParams((int)mFavoritesMenuWidth,LayoutParams.FILL_PARENT,1f));

		mMenuBarFavStoresText=(TextView) favoritesmenubarcontainer.findViewById(R.id.menubar_favstores_text);
		mMenuBarFavStoresText.setTypeface(mZouponsFont);
		mMenuBarFavCouponsText=(TextView) favoritesmenubarcontainer.findViewById(R.id.menubar_favcoupons_text);
		mMenuBarFavCouponsText.setTypeface(mZouponsFont);
		mMenuBarFriendsFavStoresText=(TextView) favoritesmenubarcontainer.findViewById(R.id.menubar_friendsfavstores_text);
		mMenuBarFriendsFavStoresText.setTypeface(mZouponsFont);

		mFavoritesListView=(ListView) favoriteslistviewcontainer.findViewById(R.id.favoriteslistview);
		mCouponsListView = (ListView) favoriteslistviewcontainer.findViewById(R.id.couponslistview);
		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.freezefavorites);
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		storeposition = -1;    
		//MenuUtilityClass.favoritesListView(this, mFavoritesListView, sScrollView, sLeftMenu, sRightMenu, MenuFlag_Favorites=2, mMainMenuActivityFreezeView);	// Call ListView Adapter class to inflate list item
		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_favorites);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag_Favorites=1, mMainMenuActivityFreezeView));
		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag_Favorites, mMainMenuActivityFreezeView));

		View[] children = new View[] { sLeftMenu, app, sRightMenu };

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

		// for list view offset values
		mIsLoadMore = false;
		mFavouritesStart ="0";
		mFavouritesEnd ="20";
		mFavouritesTotalCount ="0";
		mIsFavouriteStore = true;
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
		mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		mCouponsListView.addFooterView(mFooterLayout);

		if(getIntent().hasExtra("FromNotifications")){ // From private coupon message from notification
			mIsFavouriteStore = false;
			mFavoritesListView.setVisibility(View.GONE);
			mCouponStart="0";
			mCouponEndLimit="20";
			mCouponTotalCount = "0";
			if(mCouponsListView.getFooterViewsCount() == 0){
				mCouponsListView.addFooterView(mFooterLayout);	
			}
			mMenuBarFavCoupons.setBackgroundResource(R.drawable.footer_dark_blue_new);
			mMenuBarFavStores.setBackgroundResource(R.drawable.header_2);
			mMenuBarFriendsFavStores.setBackgroundResource(R.drawable.header_2);
			FavoriteCouponsAsynchThread favoritecoupontask = new FavoriteCouponsAsynchThread(MainMenuActivity.this,mCouponsListView,"PROGRESS",mNotificationCount);
			favoritecoupontask.execute("");
		}else{
			FavouritesTaskLoader mFavouritesTask = new FavouritesTaskLoader(MainMenuActivity.this,mFavoritesListView,mFooterLayout,"PROGRESS","Favorites","Favorites");
			mFavouritesTask.execute("FavouriteStore","");
			mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);	
		}
	
		mMenuBarFavStores.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// hide coupons list
				mCouponsListView.setVisibility(View.GONE);
				mFavoritesListView.setVisibility(View.VISIBLE);
				WebServiceStaticArrays.mFavouriteStoreDetails.clear();
				mFavoritesListView.setAdapter(new Favorites_Adapter(MainMenuActivity.this,WebServiceStaticArrays.mFavouriteStoreDetails,"Favorites","Favorites"));
				mIsFavouriteStore = true;
				mIsLoadMore = false;
				mFavouritesStart ="0";
				mFavouritesEnd ="20";
				mFavouritesTotalCount ="0";
				if(mFooterLayout != null && mFavoritesListView.getFooterViewsCount() !=0 ){
					Log.i(TAGG + "friends", "Remove Footer");
					mFavoritesListView.removeFooterView(mFooterLayout);
				}
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mMenuBarFavCoupons.setBackgroundResource(R.drawable.header_2);
				mMenuBarFriendsFavStores.setBackgroundResource(R.drawable.header_2);
				mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
				FavouritesTaskLoader mFavouritesTask = new FavouritesTaskLoader(MainMenuActivity.this,mFavoritesListView,mFooterLayout,"PROGRESS","Favorites","Favorites");
				mFavouritesTask.execute("FavouriteStore","");
			}
		});

		mMenuBarFavCoupons.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// hide favourte listview
				mIsFavouriteStore = false;
				mFavoritesListView.setVisibility(View.GONE);
				mCouponStart="0";
				mCouponEndLimit="20";
				mCouponTotalCount = "0";
				if(mCouponsListView.getFooterViewsCount() == 0){
					mCouponsListView.addFooterView(mFooterLayout);	
				}
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mMenuBarFavStores.setBackgroundResource(R.drawable.header_2);
				mMenuBarFriendsFavStores.setBackgroundResource(R.drawable.header_2);
				FavoriteCouponsAsynchThread favoritecoupontask = new FavoriteCouponsAsynchThread(MainMenuActivity.this,mCouponsListView,"PROGRESS",mNotificationCount);
				favoritecoupontask.execute("");
			}
		});

		mMenuBarFriendsFavStores.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mIsFavouriteStore = false;
				mCouponsListView.setVisibility(View.GONE);
				mFavoritesListView.setVisibility(View.VISIBLE);
				WebServiceStaticArrays.mFavouriteFriendStoreDetails.clear();
				mFavoritesListView.setAdapter(new Favorites_Adapter(MainMenuActivity.this,WebServiceStaticArrays.mFavouriteFriendStoreDetails,"Favorites","Favorites"));
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
				mMenuBarFavStores.setBackgroundResource(R.drawable.header_2);
				mMenuBarFavCoupons.setBackgroundResource(R.drawable.header_2);
				mIsLoadMore = false;
				mFavouritesStart ="0";
				mFavouritesEnd ="20";
				mFavouritesTotalCount ="0";
				if(mFooterLayout != null && mFavoritesListView.getFooterViewsCount() !=0 ){
					mFavoritesListView.removeFooterView(mFooterLayout);
				}
				FavouritesTaskLoader mFavouritesTask = new FavouritesTaskLoader(MainMenuActivity.this,mFavoritesListView,mFooterLayout,"PROGRESS","Favorites","Favorites");
				mFavouritesTask.execute("FriendFavouriteStore","");

			}
		});

		mFavorite_RightMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.i("position", storeposition+" " +" "+WebServiceStaticArrays.mFavouriteStoreDetails.size());
				if(mIsfriendfavourite){
					v.setBackgroundResource(R.drawable.gradient_bg_hover);
					new AddorRemoveFavoriteAsynchThread(MainMenuActivity.this).execute(RightMenuStoreId_ClassVariables.mStoreID,UserDetails.mServiceUserId,"FriendFavourite");
				}else{
					if(storeposition < WebServiceStaticArrays.mFavouriteStoreDetails.size() && storeposition!=-1){
						POJOStoreInfo mFavouriteStoreDetails = (POJOStoreInfo) WebServiceStaticArrays.mFavouriteStoreDetails.get(storeposition);
						AddorRemoveFavoriteAsynchThread mremoveFavourite = new AddorRemoveFavoriteAsynchThread(MainMenuActivity.this);
						mremoveFavourite.execute(mFavouriteStoreDetails.store_id,UserDetails.mServiceUserId,"Favourites");
					}	
				}
			}
		});

		// for list view scroll
		mFavoritesListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				//Check the bottom item is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;	
				if(mFavoritesListView.getFooterViewsCount() == 0){
					totalItemCount = totalItemCount + 1;
				}
				if(Integer.parseInt(mFavouritesTotalCount) >= totalItemCount && Integer.parseInt(mFavouritesTotalCount)> 20){					
					if(lastInScreen == totalItemCount && !mIsLoadMore){												
						if(Integer.parseInt(mFavouritesTotalCount) > Integer.parseInt(mFavouritesEnd)){
							mFooterText.setText("Loading "+mFavouritesEnd+" of "+"("+mFavouritesTotalCount+")");
						}else{
							mFooterText.setText("Loading "+mFavouritesTotalCount);
						}
						if(mIsFavouriteStore){
							FavouritesTaskLoader mFavouritesTask = new FavouritesTaskLoader(MainMenuActivity.this,mFavoritesListView,mFooterLayout,"NOPROGRESS","Favorites","Favorites");
							mFavouritesTask.execute("FavouriteStore","RefreshAdapter");
						}else{
							FavouritesTaskLoader mFavouritesTask = new FavouritesTaskLoader(MainMenuActivity.this,mFavoritesListView,mFooterLayout,"NOPROGRESS","Favorites","Favorites");
							mFavouritesTask.execute("FriendFavouriteStore","RefreshAdapter");	
						}
					}
				}else{
					if(mFavouritesTotalCount.equalsIgnoreCase("0")){
						Log.i(TAGG, "Currently No List Item");
					}else if(mFooterLayout != null && mFavoritesListView.getFooterViewsCount() !=0 &&mFavoritesListView.getAdapter() != null ){
						Log.i(TAGG, "Remove Footer");
						mFavoritesListView.removeFooterView(mFooterLayout);
					}
				}
			}
		});

		// for favourite coupons list view
		mCouponsListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//Check the bottom item is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;	

				if(Integer.parseInt(mCouponTotalCount) > lastInScreen && Integer.parseInt(mCouponTotalCount)> 20){					
					if(lastInScreen == totalItemCount && !mIsLoadMore){												

						// changes
						if(mCouponsListView.getFooterViewsCount() == 0){
							mCouponsListView.addFooterView(mFooterLayout);
						}

						//
						if(Integer.parseInt(mCouponTotalCount) > Integer.parseInt(mCouponEndLimit)){
							mFooterText.setText("Loading "+mCouponEndLimit+" of "+"("+mCouponTotalCount+")");
						}else{
							mFooterText.setText("Loading "+mCouponTotalCount);
						}

						FavoriteCouponsAsynchThread favoritecoupontask = new FavoriteCouponsAsynchThread(MainMenuActivity.this,mCouponsListView,"NOPROGRESS",mNotificationCount);
						favoritecoupontask.execute("RefreshAdapter");

					}
				}else{
					if(mCouponTotalCount.equalsIgnoreCase("0")){
						Log.i(TAGG, "Currently No List Item");
					}else if(mFooterLayout != null && mCouponsListView.getFooterViewsCount() !=0 && mCouponsListView.getAdapter() != null){
						Log.i(TAGG, "Remove Footer");

						// changes
						if(lastInScreen!= totalItemCount){
							mCouponsListView.removeFooterView(mFooterLayout);	
						}else{
							Log.i(TAGG, "Remove Footer wait");
						}
						// changes
					}
				}
			}
		});
	}

	public void SetFavouriteCouponListAdatpter(String mCheckRefresh) {

		if(mCheckRefresh.equalsIgnoreCase("RefreshAdapter") && mCouponAdapter != null){
			mCouponAdapter.notifyDataSetChanged();
		}else{
			mCouponAdapter = new CouponListAdapter(MainMenuActivity.this,"FavoriteCoupon");
			mCouponsListView.setAdapter(mCouponAdapter);			
		}

		if(WebServiceStaticArrays.mStaticCouponsArrayList.size() >0 && Integer.parseInt(MainMenuActivity.mCouponTotalCount)>20){
			Log.i(TAGG, "ArrayList has value");
		}else if(mFooterLayout != null && mCouponsListView.getFooterViewsCount() !=0 &&mCouponsListView.getAdapter() != null){
			Log.i(TAGG, "Remove Footer View From Coupon List");
			mCouponsListView.removeFooterView(mFooterLayout);
		}

		MenuUtilityClass.FavoriteCouponsListView(MainMenuActivity.this, mCouponsListView);
	}

	protected void social(int bodyLayout,Bundle savedInstanceState){
		POJOMainMenuActivityTAG.TAG="MainMenuActivity_Social";
		int MenuFlag=0;
		app = inflater.inflate(bodyLayout, null);
		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.social_tabbar);
		ViewGroup socialcontainer = (ViewGroup) app.findViewById(R.id.social_container);
		mSocialRightMenuHolder = (ImageView) socialcontainer.findViewById(R.id.social_rightmenu);
		mSocialStoreName = (TextView) socialcontainer.findViewById(R.id.social_title_textId);
		mSocialStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);

		mMainMenuActivityFreezeView = (Button) app.findViewById(R.id.social_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_social);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));
		// Notification and log out variables
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Check whether to show Notification image in zoupons header
		new NotificationTabImageVisibility(MainMenuActivity.this, mNotificationImage).checkNotificationVisibility();
		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		View[] children = new View[] { sLeftMenu, app, sRightMenu };

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

		//////////////////////////////////////////////////////
		mSocialShareCheckBox = (CheckBox) findViewById(R.id.social_share_checkboxId);
		mEmailShareCheckBox = (CheckBox) findViewById(R.id.email_share_checkboxId);
		mSocialShareOptionsLayout = (RelativeLayout) findViewById(R.id.social_share_types_Id); 
		//mSocialLikecontainer = (LinearLayout)mSocialShareOptionsLayout. findViewById(R.id.social_like_container); 
		mSocialFBLike = (ImageView) app.findViewById(R.id.Social_fb_share);
		mfriendlist = (ListView) findViewById(R.id.email_friendlistId);
		mSocialTypesList = (ListView) findViewById(R.id.social_listId);
		mSocialFooterLayout = (RelativeLayout) findViewById(R.id.social_friends_footerLayoutId);
		mSocialBackMenu = (TextView) findViewById(R.id.friends_leftFooterText);
		mBackMenuSplitter = findViewById(R.id.friendlist_backmenusplitter);
		mImportFriendMenu = (TextView) findViewById(R.id.friends_import_friends_menuId);
		importmenusplitter = findViewById(R.id.friendlist_importmenusplitter);
		SocialfriendsOptionsContainer = (RelativeLayout) findViewById(R.id.social_import_friend_container);
		friendlistgroup = (ViewGroup) app.findViewById(R.id.friendsearch_layout);
		mFriendlistSearch = (EditText) friendlistgroup.findViewById(R.id.friendlist_searchId);  
		mSearchCancel = (Button) friendlistgroup.findViewById(R.id.friendsearch_buttonId);
		mSocialRightMenuHolder.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag=2, mMainMenuActivityFreezeView,mFriendlistSearch));
		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView,mFriendlistSearch));
		// To add text watcher for searching friend list
		mFriendlistSearch.addTextChangedListener(new SearchFriendsTextWatcher(MainMenuActivity.this, mfriendlist,"social"));
		
		// Intantiate facebook ui handler lifecycle object
		mFacebookUILifeCycleHelper = new UiLifecycleHelper(MainMenuActivity.this, new StatusCallback() {
			
			@Override
			public void call(Session session, SessionState sessionstate, Exception exception) {
				// TODO Auto-generated method stub
				
				
			}
		});
		mFacebookUILifeCycleHelper.onCreate(savedInstanceState);
		
		//Checked change listener for share via social..
		mSocialShareCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked){
					mEmailShareCheckBox.setChecked(false);
					friendlistgroup.setVisibility(View.GONE);
					mSocialFooterLayout.setVisibility(View.GONE);
					mSocialShareOptionsLayout.setVisibility(View.VISIBLE);
					SocialfriendsOptionsContainer.setVisibility(View.GONE);
				}else{
					if(!mEmailShareCheckBox.isChecked()){
						mSocialShareCheckBox.setChecked(true);
					}
				}
			}
		});

		//Checked change listener for share via email..
		mEmailShareCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mSocialShareCheckBox.setChecked(false);
					friendlistgroup.setVisibility(View.VISIBLE);
					mSocialFooterLayout.setVisibility(View.GONE);
					mSocialBackMenu.setVisibility(View.INVISIBLE);
					mBackMenuSplitter.setVisibility(View.INVISIBLE);
					mSocialShareOptionsLayout.setVisibility(View.GONE);

					mImportFriendMenu.setVisibility(View.VISIBLE);
					importmenusplitter.setVisibility(View.VISIBLE);

					if(WebServiceStaticArrays.mSocialNetworkFriendList.size()>0){
						mfriendlist.setAdapter(new CustomFBFriendListAdapter(MainMenuActivity.this,WebServiceStaticArrays.mSocialNetworkFriendList,"social"));
					}else{
						// To load contact friend list
						ContactListService mGetContacts = new ContactListService(MainMenuActivity.this,mfriendlist,"social");
						mGetContacts.execute();
					}
									
				}else{
					if(!mSocialShareCheckBox.isChecked()){
						mEmailShareCheckBox.setChecked(true);
					}
				}
			}
		});

		// Default checked to share via social...
		mSocialShareCheckBox.setChecked(true);
		mSocialBackMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				friendlistgroup.setVisibility(View.VISIBLE);
				SocialfriendsOptionsContainer.setVisibility(View.GONE);
				mSocialBackMenu.setVisibility(View.INVISIBLE);
				mBackMenuSplitter.setVisibility(View.INVISIBLE);
				mImportFriendMenu.setVisibility(View.VISIBLE);
				importmenusplitter.setVisibility(View.VISIBLE);
			}
		});

		mSearchCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFriendlistSearch.getText().clear();
				mfriendlist.setAdapter(new CustomFBFriendListAdapter(MainMenuActivity.this,WebServiceStaticArrays.mSocialNetworkFriendList,"social"));
				// To hide soft keyboard
				InputMethodManager softkeyboardevent = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				softkeyboardevent.hideSoftInputFromWindow(mFriendlistSearch.getWindowToken(), 0);
			}
		});

		mSocialFBLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShareStoreAsynchThread sharestoreasynchthread = new ShareStoreAsynchThread(MainMenuActivity.this,mFacebookUILifeCycleHelper);
				sharestoreasynchthread.execute("FB","AlreadyLogged");
			}
		});
	} 

	public void contextMenuOpen(View sender){
		sender.setLongClickable(false);
		registerForContextMenu(sender);
		openContextMenu(sender);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getGroupId()){
		case 1:
			return true;
		case 2:
			if(item.getTitle().equals("Take Photo")){
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
				startActivityForResult(cameraIntent, 300);
				return true;
			}else{
				Intent mGalleryIntent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(mGalleryIntent, 400);
				return true;
			}
		case 3:
			if(item.getTitle().equals("Take Photo")){
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
				startActivityForResult(cameraIntent, 500);
				return true;
			}else{
				Intent mGalleryIntent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(mGalleryIntent, 600);
				return true;
			}
		case 4:
			mRewardsStoreState.setText(item.getTitle());	
		default :
			return super.onContextItemSelected(item);
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if(v.equals(mStateContextMenuImage)==true){
			menu.setHeaderTitle("Choose Any State");
			for(int i=0 ; i< statecodes.length ;i++){
				menu.add(4, v.getId(), 0, statecodes[i]);	
			}
		}else if(v.equals(mStoreAddPhoto1)==true){
			menu.setHeaderTitle("Choose from");
			menu.add(2, v.getId(), 0, "Take Photo");
			menu.add(2, v.getId(), 0, "Gallery");
		}else if(v.equals(mStoreAddPhoto2)==true){
			menu.setHeaderTitle("Choose from");
			menu.add(3, v.getId(), 0, "Take Photo");
			menu.add(3, v.getId(), 0, "Gallery");
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	protected void playVideo(int layout){
		POJOMainMenuActivityTAG.TAG="MainMenuActivity_PlayVideo";
		// Store Vieo Screen
		app = inflater.inflate(layout, null);
		int MenuFlag =0;

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.video_tabbar);
		ViewGroup videocontainer = (ViewGroup) app.findViewById(R.id.video_container);

		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.video_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_video);
		mVideoImage = (ImageView) app.findViewById(R.id.mVideoImage);

		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Check whether to show Notification image in zoupons header
		new NotificationTabImageVisibility(MainMenuActivity.this, mNotificationImage).checkNotificationVisibility();
		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView));
		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		View[] children = new View[] { sLeftMenu, app, sRightMenu };

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
		storeinfo_rightmenu = (ImageView) videocontainer.findViewById(R.id.video_rightmenu);
		storeinfo_rightmenu.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag=2, mMainMenuActivityFreezeView));
		mVideoStoreName = (TextView) app.findViewById(R.id.video_title_textId);
		mVideoStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);
		mVideoPlayImage = (ImageView) app.findViewById(R.id.mPlayButton);

		ViewTreeObserver vto = mVideoImage.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				mImageViewHeight = mVideoImage.getMeasuredHeight();
				mImageViewWidth = mVideoImage.getMeasuredWidth();
				return true;
			}
		});

		// AsynchTask to Get Video URL and Video Thumbnail
		PlayVideoClass mPlayVideoClass = new PlayVideoClass(MainMenuActivity.this,mStoreName,RightMenuStoreId_ClassVariables.mStoreID,mVideoImage,mVideoPlayImage,String.valueOf(mImageViewWidth),String.valueOf(mImageViewHeight),RightMenuStoreId_ClassVariables.mLocationId);
		mPlayVideoClass.execute();

		//Video Play Button Click Action    
		mVideoPlayImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.i(TAGG, "Video Starts Here");
				try{
					if(VIDEOURLVALUE.length()>1){
						Intent intentPlay = new Intent(MainMenuActivity.this,StoreVideoDialog.class);
						intentPlay.putExtra("VIDEO",MainMenuActivity.VIDEOURLVALUE);/*"http://cf9c36303a9981e3e8cc-31a5eb2af178214dc2ca6ce50f208bb5.r97.cf1.rackcdn.com/bigger_badminton_600.mp4"*/
						startActivity(intentPlay);
					}else{}
				}catch (Exception e) {
					Log.i(TAGG, "Exception Occured");		   
				}
			}
		});
	}

	protected void mCouponsActivity(int couponsact) {
		POJOMainMenuActivityTAG.TAG="MainMenuActivity_Coupons";
		app = inflater.inflate(couponsact, null);
		int MenuFlag =0;		

		mIsLoadMore = false;
		mCouponStart="0";
		mCouponEndLimit="20";
		mCouponTotalCount = "0";

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.coupons_tabbar);
		ViewGroup couponscontainer = (ViewGroup) app.findViewById(R.id.coupons_container);

		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.coupons_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_coupons);		
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Check whether to show Notification image in zoupons header
		new NotificationTabImageVisibility(MainMenuActivity.this, mNotificationImage).checkNotificationVisibility();
		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView));
		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		View[] children = new View[] { sLeftMenu, app, sRightMenu };

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
		storeinfo_rightmenu = (ImageView) couponscontainer.findViewById(R.id.coupons_rightmenu);
		storeinfo_rightmenu.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag=2, mMainMenuActivityFreezeView));
		mCouponsStoreName = (TextView) app.findViewById(R.id.coupons_title_textId);
		mCouponsStoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);
		CouponsList = (ListView) app.findViewById(R.id.mCouponsList);

		//For Footer Layout
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
		mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		CouponsList.addFooterView(mFooterLayout);

		CouponsTask mCouponsTask = new CouponsTask(MainMenuActivity.this,"PROGRESS",mNotificationCount);
		mCouponsTask.execute("");

		CouponsList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//Check the bottom item is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;	


				if(Integer.parseInt(mCouponTotalCount) > lastInScreen && Integer.parseInt(mCouponTotalCount)>20){					
					if(lastInScreen == totalItemCount && !mIsLoadMore){												
						Log.i(TAGG, "Set Text In The Footer");
						if(CouponsList.getFooterViewsCount() == 0){
							CouponsList.addFooterView(mFooterLayout);
						}

						if(Integer.parseInt(mCouponTotalCount) > Integer.parseInt(mCouponEndLimit)){
							mFooterText.setText("Loading "+mCouponEndLimit+" of "+"("+mCouponTotalCount+")");
						}else{
							mFooterText.setText("Loading "+mCouponTotalCount);
						}

						Log.i(TAGG, "Runn AynckTask To Add More");
						CouponsTask mCouponsTask = new CouponsTask(MainMenuActivity.this,"NOPROGRESS",mNotificationCount);
						mCouponsTask.execute("RefreshAdapter");

					}
				}else{
					if(mCouponTotalCount.equalsIgnoreCase("0")){
						Log.i(TAGG, "Currently No List Item");
					}else if(mFooterLayout != null && CouponsList.getFooterViewsCount() !=0 && CouponsList.getAdapter() != null){
						Log.i(TAGG, "Remove Footer");
						if(lastInScreen!= totalItemCount){
							Log.i(TAGG, "Remove Footer success");
							CouponsList.removeFooterView(mFooterLayout);	
						}else{
							Log.i(TAGG, "Remove Footer wait");
						}
					}
				}

			}
		});
	}

	public void SetCouponListAdatpter(ArrayList<Object> mStoreRegularCardDetails, String mCheckRefresh) {
		// TODO Auto-generated method stub

		if(mCheckRefresh.equalsIgnoreCase("RefreshAdapter") && mCouponAdapter != null){
			Log.i("Adapter", "Refresh");
			mCouponAdapter.notifyDataSetChanged();
		}else{
			Log.i("Adapter", "Add Adapter");			
			mCouponAdapter = new CouponListAdapter(MainMenuActivity.this,"StoreCoupon");
			CouponsList.setAdapter(mCouponAdapter);			
		}

		if(mFooterLayout != null && CouponsList.getFooterViewsCount() !=0 &&CouponsList.getAdapter() != null){
			Log.i(TAGG, "Remove Footer View From Coupon List");
			CouponsList.removeFooterView(mFooterLayout);
		}
		MenuUtilityClass.couponsListView(MainMenuActivity.this, CouponsList);
	}

	protected void ContactStore(int bodyLayout,final String classname){
		View[] children;
		if(classname.equalsIgnoreCase("ContactStore")){
			POJOMainMenuActivityTAG.TAG="MainMenuActivity_ContactStore";
		}else{
			POJOMainMenuActivityTAG.TAG="MainMenuActivity_TalkToUs";
		} 
		app = inflater.inflate(bodyLayout, null);
		int MenuFlag =0;

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.talktous_contactstore_tabBar);
		ViewGroup talktous_contactstore_container = (ViewGroup) app.findViewById(R.id.talktous_contactstore_container);
		ViewGroup talktous_contactstore_righmenuholder = (ViewGroup) app.findViewById(R.id.talktous_contactstore_rightmenuholder);
		menubarcontainer =(ViewGroup) talktous_contactstore_container.findViewById(R.id.menubarcontainer_talktous_contactstore);
		ViewGroup talktous_contactstore_footer = (ViewGroup) app.findViewById(R.id.talktous_contactstore_footerLayoutId);

		mListView = (ListView) app.findViewById(R.id.talktous_contactstore_listview);
		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.talktous_contactstore_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_talktous_contactstore);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView, mAddMessage));
		mAddMessage = (EditText) menubarcontainer.findViewById(R.id.talktous_contactstore_newmsg);
		mSend = (Button) menubarcontainer.findViewById(R.id.talktous_contactstore_send);
		// Notification and log out variables
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		mTalkToUsContactStoreBack = (TextView) talktous_contactstore_footer.findViewById(R.id.talktous_contactstore_backtextId);
		
		mIsLoadMore = false;
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHeaderView = mInflater.inflate(R.layout.footerlayout, null, false);
		mHeaderLoadingText = (TextView) mHeaderView.findViewById(R.id.footerText);
		mHeaderProgressBar = (ProgressBar) mHeaderView.findViewById(R.id.mProgressBar);
		mListView.addHeaderView(mHeaderView);
				
		if(classname.equalsIgnoreCase("ContactStore")){
			talktous_contactstore_righmenuholder.setVisibility(View.VISIBLE);
			if(getIntent().hasExtra("isback")){
				talktous_contactstore_footer.setVisibility(View.VISIBLE);
			}else{
				talktous_contactstore_footer.setVisibility(View.GONE);
			}

			mContactStore_RightMenuOpen=(ImageView) talktous_contactstore_container.findViewById(R.id.talktous_contactstore_rightmenu);
			mContactStore_RightMenuOpen.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag=2, mMainMenuActivityFreezeView,mAddMessage));

			mContactStore_StoreName = (TextView) talktous_contactstore_container.findViewById(R.id.talktous_contactstore_storename_textId);
			mContactStore_StoreName.setText(RightMenuStoreId_ClassVariables.mStoreName);
		}else{
			if(getIntent().hasExtra("hide_back")){
				talktous_contactstore_footer.setVisibility(View.GONE);
			}else{
				talktous_contactstore_footer.setVisibility(View.VISIBLE);
			}
			talktous_contactstore_righmenuholder.setVisibility(View.GONE);
		}

		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView, mAddMessage));

		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();

		if(classname.equalsIgnoreCase("ContactStore")){
			children = new View[] { sLeftMenu, app, sRightMenu };
		}else{
			children = new View[] { sLeftMenu, app };
		}

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

		final boolean mIsFromNotifications = getIntent().hasExtra("isback") ? true : false;

		if(mCommunicationTimer != null && mCommunicationTimerTask != null){
			mCommunicationTimer.cancel();
			mCommunicationTimer=null;
			mCommunicationTimerTask.cancel();
			mCommunicationTimerTask=null;
		}
		
		mCommunicationTimer = new Timer();
		if(classname.equalsIgnoreCase("ContactStore")){
			mCommunicationTimerTask = new CommunicationTimerTask(MainMenuActivity.this, classname, menubarcontainer, mListView, mIsFromNotifications, "store_customer",mNotificationCount);
		}else{
			mCommunicationTimerTask = new CommunicationTimerTask(MainMenuActivity.this, classname, menubarcontainer, mListView, mIsFromNotifications, "zoupons_customer",mNotificationCount);
		}

		if(classname.equalsIgnoreCase("ContactStore")){
			ContactUsResponseTask contactusresponse = new ContactUsResponseTask(MainMenuActivity.this,menubarcontainer,mListView,mIsFromNotifications,"store_customer",true,false,"normal","0","customer",mNotificationCount);
			contactusresponse.execute("ContactStore",RightMenuStoreId_ClassVariables.mStoreID,UserDetails.mServiceUserId,RightMenuStoreId_ClassVariables.mLocationId,"shopper");
		}else{
			ContactUsResponseTask contactusresponse = new ContactUsResponseTask(MainMenuActivity.this,menubarcontainer,mListView,mIsFromNotifications,"zoupons_customer",true,false,"normal","0","customer",mNotificationCount);
			contactusresponse.execute("ZouponsSupport","",UserDetails.mServiceUserId,"","shopper");
		}
		
		mListView.setOnScrollListener(new CustomChatScrollListner(MainMenuActivity.this,mListView,classname, mIsFromNotifications, menubarcontainer,mNotificationCount));
		
		mSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if(!mAddMessage.getText().toString().trim().equalsIgnoreCase("")){
					
					if(mCommunicationTimer!=null){
						mCommunicationTimer.cancel();
						mCommunicationTimer=null;
					}if(mCommunicationTimerTask!=null){
						mCommunicationTimerTask.cancel();
						mCommunicationTimerTask=null;
					}
					
					InputMethodManager softkeyboardevent = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					softkeyboardevent.hideSoftInputFromWindow(mAddMessage.getWindowToken(), 0);
					if(classname.equalsIgnoreCase("ContactStore")){
						ContactUsTask contactustask = new ContactUsTask(MainMenuActivity.this,menubarcontainer,mListView,mAddMessage,mIsFromNotifications,"","customer",mNotificationCount);
						contactustask.execute("ContactStore",RightMenuStoreId_ClassVariables.mStoreID,mAddMessage.getText().toString().trim(),RightMenuStoreId_ClassVariables.mLocationId,"shopper",UserDetails.mServiceUserId);
					}else{
						ContactUsTask contactustask = new ContactUsTask(MainMenuActivity.this,menubarcontainer,mListView,mAddMessage,mIsFromNotifications,"","customer",mNotificationCount);
						contactustask.execute("ZouponsSupport","0",mAddMessage.getText().toString().trim(),"","shopper",UserDetails.mServiceUserId);
					}
				}else{
					//alertBox_service("Information", "Please enter message");
				}
			}
		});

		mTalkToUsContactStoreBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try{
					if(mCommunicationTimer!=null){
						mCommunicationTimer.cancel();
						mCommunicationTimer=null;
					}if(mCommunicationTimerTask!=null){
						mCommunicationTimerTask.cancel();
						mCommunicationTimerTask=null;
					}
					POJOMainMenuActivityTAG.ChatFlag = "";
					if(classname.equalsIgnoreCase("ContactStore")){
						Log.i(TAGG,"Contact Store Back");
						POJOMainMenuActivityTAG.TAG="MainMenuActivity_CustomerCenter";
						finish();
					}else{
						Log.i(TAGG,"ZouponsSupport Back");
						POJOMainMenuActivityTAG.TAG="MainMenuActivity_CustomerCenter";
						finish();
					}

				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
				
	}

	protected void setReviews(int reviews) {

		POJOMainMenuActivityTAG.TAG="MainMenuActivity_Reviews";
		app = inflater.inflate(reviews, null);
		int MenuFlag =0;	
		mIsLoadMore = false;
		mReviewStart ="0";
		mReviewEndLimit ="20";
		mReviewTotalCount ="0";

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.reviews_tabbar);
		final ViewGroup reviewslistcontainer = (ViewGroup) app.findViewById(R.id.review_list_container);

		mEditReviewDetails = (ScrollView) app.findViewById(R.id.reviews_edit_layoutId);
		mViewReviewDetails = (ScrollView) app.findViewById(R.id.reviews_viewdetailslayoutId);
		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.review_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_review);		
		mRightMenuReview = (ImageView) reviewslistcontainer.findViewById(R.id.reviews_rightmenu);

		mRightMenuReview.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag=2, mMainMenuActivityFreezeView));
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));
		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView));

		// Notification and log out variables
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		// Check whether to show Notification image in zoupons header
		new NotificationTabImageVisibility(MainMenuActivity.this, mNotificationImage).checkNotificationVisibility();
		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		View[] children = new View[] { sLeftMenu, app, sRightMenu};
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

		mReviewRatingContainer = (LinearLayout) reviewslistcontainer.findViewById(R.id.review_rate_container);
		mReviewFooterContainer = (RelativeLayout) app.findViewById(R.id.reviews_footerLayoutId);
		mReviewRatingOne = (TextView) mReviewRatingContainer.findViewById(R.id.mOneStarTextId);
		mReviewRatingTwo  = (TextView) mReviewRatingContainer.findViewById(R.id.mTwoStarTextId);
		mReviewRatingThree = (TextView) mReviewRatingContainer.findViewById(R.id.mThreeStarTextId);
		mReviewRatingFour = (TextView) mReviewRatingContainer.findViewById(R.id.mFourStarTextId);
		mReviewRatingFive = (TextView) mReviewRatingContainer.findViewById(R.id.mFiveStarTextId);
		mFooterLeftText = (TextView) mReviewFooterContainer.findViewById(R.id.reviews_leftFooterText);
		mFooterRightText = (TextView) mReviewFooterContainer.findViewById(R.id.reviews_rightFooterText);
		mReviewStoreTitle = (TextView) reviewslistcontainer.findViewById(R.id.reviews_store_title_textId);
		mReviewList = (ListView)reviewslistcontainer.findViewById(R.id.review_listview);
		mViewReviewDetailsContainer = (LinearLayout) mViewReviewDetails.findViewById(R.id.view_review_details_container);
		mViewReviewStoreTitle = (TextView) mViewReviewDetailsContainer.findViewById(R.id.reviews_viewstore_title);
		mViewReviewDetials = (RelativeLayout) mViewReviewDetailsContainer.findViewById(R.id.review_name_date_layout);
		mViewReviewName = (TextView) mViewReviewDetials.findViewById(R.id.review_posterName);
		mViewReviewPostedDate = (TextView) mViewReviewDetials.findViewById(R.id.review_date);
		mViewReviewRatingContainer = (LinearLayout) mViewReviewDetailsContainer.findViewById(R.id.review_rating_layout);
		mViewReviewStarOne = (ImageView) mViewReviewRatingContainer.findViewById(R.id.mStartImage1);
		mViewReviewStarTwo = (ImageView) mViewReviewRatingContainer.findViewById(R.id.mStartImage2);
		mViewReviewStarThree = (ImageView) mViewReviewRatingContainer.findViewById(R.id.mStartImage3);
		mViewReviewStarFour = (ImageView) mViewReviewRatingContainer.findViewById(R.id.mStartImage4);
		mViewReviewStarFive = (ImageView) mViewReviewRatingContainer.findViewById(R.id.mStartImage5);
		mViewReviewMessage = (EditText) mViewReviewDetailsContainer.findViewById(R.id.store_review_descriptionsId);
		mReViewLikeDislikeContainer = (RelativeLayout) mViewReviewDetailsContainer.findViewById(R.id.review_like_dislike_layout);
		mLikeImage = (ImageView) mReViewLikeDislikeContainer.findViewById(R.id.review_thumbsup);
		mDislikeImage = (ImageView) mReViewLikeDislikeContainer.findViewById(R.id.review_thumbsdown);
		mLikeCount = (TextView) mReViewLikeDislikeContainer.findViewById(R.id.review_likecountId);
		mDislikeCount = (TextView) mReViewLikeDislikeContainer.findViewById(R.id.review_dislikecountId);
		mviewReviewButtonContainer = (RelativeLayout) mViewReviewDetailsContainer.findViewById(R.id.viewreview_button_layout);
		mPreviousButton = (Button)mviewReviewButtonContainer.findViewById(R.id.review_previous);
		mNextButton = (Button)mviewReviewButtonContainer.findViewById(R.id.review_next);
		mUpdateReviewLayout = (LinearLayout) mEditReviewDetails.findViewById(R.id.UpdateReviewLayout);
		mUpdateReviewMessage = (EditText) mUpdateReviewLayout.findViewById(R.id.store_review_descriptionId);
		mReviewRatingContainer = (LinearLayout) mUpdateReviewLayout.findViewById(R.id.reviewrateLayoutId);
		mRateOnebutton = (Button) mReviewRatingContainer.findViewById(R.id.store_rate1ButtonId);
		mRateTwobutton = (Button) mReviewRatingContainer.findViewById(R.id.store_rate2ButtonId);
		mRateThreebutton = (Button) mReviewRatingContainer.findViewById(R.id.store_rate3ButtonId);
		mRateFourbutton = (Button) mReviewRatingContainer.findViewById(R.id.store_rate4ButtonId);
		mRateFivebutton = (Button) mReviewRatingContainer.findViewById(R.id.store_rate5ButtonId);
		mupdateReviewButtonContainer = (RelativeLayout) mUpdateReviewLayout.findViewById(R.id.updatebuttonlayoutId);
		mCancel = (Button) mupdateReviewButtonContainer.findViewById(R.id.cancel_buttonId);
		mPost = (Button) mupdateReviewButtonContainer.findViewById(R.id.post_buttonId);
		mReviewStoreTitle.setText(RightMenuStoreId_ClassVariables.mStoreName);

		GetStoreReviewDetails reviewDetails = new GetStoreReviewDetails(MainMenuActivity.this, mReviewList,mReviewStoreTitle,mReviewRatingOne,mReviewRatingTwo,mReviewRatingThree,mReviewRatingFour,mReviewRatingFive,mFooterLeftText,"PROGRESS",POJOMainMenuActivityTAG.TAG);
		reviewDetails.execute("");

		mStoreRating="0";
		WebServiceStaticArrays.GetReviewStatusList.clear();
		//For Footer Layout
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
		mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		mReviewList.addFooterView(mFooterLayout);

		SharedPreferences mUserDetailsPrefs = getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		final String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");
		if(user_login_type.equalsIgnoreCase("customer")){  // Customer
			mReviewFooterContainer.setVisibility(View.VISIBLE);
		}else{
			mReviewFooterContainer.setVisibility(View.GONE);
		}
		
		mReviewList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {

				if(WebServiceStaticArrays.mStoreReviewsList.size() > arg2){
					POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(arg2);
					currentPosition = arg2;	
					reviewId = mReviewDetails.review_id;
					reviewslistcontainer.setVisibility(View.GONE);
					mEditReviewDetails.setVisibility(View.GONE);
					mViewReviewDetails.setVisibility(View.VISIBLE);
					mReviewFooterContainer.setVisibility(View.VISIBLE);
					mFooterLeftText.setVisibility(View.VISIBLE);
					mFooterLeftText.setText("Back");
					mFooterLeftText.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.back, 0, 0);
					mPreviousButton.setBackgroundResource(R.drawable.header_2);
					mNextButton.setBackgroundResource(R.drawable.header_2);
					mViewReviewStoreTitle.setText(RightMenuStoreId_ClassVariables.mStoreName);
					GetReviewRatesStatusTask mReviewStatus = new GetReviewRatesStatusTask(MainMenuActivity.this,mViewReviewName,mViewReviewPostedDate,mViewReviewMessage,mLikeCount,mDislikeCount,
							mLikeImage,mDislikeImage,mViewReviewStarOne,mViewReviewStarTwo,mViewReviewStarThree,mViewReviewStarFour,mViewReviewStarFive,mFooterRightText,arg2);
					mReviewStatus.execute(RightMenuStoreId_ClassVariables.mStoreID);
					if(arg2 == 0){
						mPreviousButton.setBackgroundResource(R.drawable.button_disabled);
					}
					if(arg2 == WebServiceStaticArrays.mStoreReviewsList.size()-1){
						mNextButton.setBackgroundResource(R.drawable.button_disabled);
					}	
				}else{
					Log.i("Review list", "loading");
				}
			}
		});

		mReviewList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//Check the bottom item is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;			

				if(Integer.parseInt(mReviewTotalCount) > lastInScreen && Integer.parseInt(mReviewTotalCount)>20){					
					if(lastInScreen == totalItemCount && !mIsLoadMore){												
						Log.i(TAGG, "Set Text In The Footer");
						if(mReviewList.getFooterViewsCount() == 0){
							mReviewList.addFooterView(mFooterLayout);
						}
						if(Integer.parseInt(mReviewTotalCount) > Integer.parseInt(mReviewEndLimit)){
							mFooterText.setText("Loading "+mReviewEndLimit+" of "+"("+mReviewTotalCount+")");
						}else{
							mFooterText.setText("Loading "+mReviewTotalCount);
						}

						Log.i(TAGG, "Runn AynckTask To Add More");
						GetStoreReviewDetails reviewDetails = new GetStoreReviewDetails(MainMenuActivity.this, mReviewList,mReviewStoreTitle,mReviewRatingOne,mReviewRatingTwo,mReviewRatingThree,mReviewRatingFour,mReviewRatingFive,mFooterLeftText,"NOPROGRESS",POJOMainMenuActivityTAG.TAG);
						reviewDetails.execute("RefreshAdapter");

					}
				}else{
					if(mReviewTotalCount.equalsIgnoreCase("0")){
						Log.i(TAGG, "Currently No List Item");
					}else if(mFooterLayout != null && mReviewList.getFooterViewsCount() !=0 &&mReviewList.getAdapter() != null){
						Log.i(TAGG, "Remove Footer");
						if(lastInScreen!= totalItemCount){
							Log.i(TAGG, "Remove Footer success");
							mReviewList.removeFooterView(mFooterLayout);	
						}else{
							Log.i(TAGG, "Remove Footer wait");
						}
					}
				}

			}
		});

		mLikeImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RatingReviewLikeDisLikeTask mLikeDislikeTask = new RatingReviewLikeDisLikeTask(MainMenuActivity.this,mLikeCount,mDislikeCount,mLikeImage,mDislikeImage);
				mLikeDislikeTask.execute(RightMenuStoreId_ClassVariables.mStoreID,reviewId,"up");
			}
		});

		mDislikeImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RatingReviewLikeDisLikeTask mLikeDislikeTask = new RatingReviewLikeDisLikeTask(MainMenuActivity.this,mLikeCount,mDislikeCount,mLikeImage,mDislikeImage);
				mLikeDislikeTask.execute(RightMenuStoreId_ClassVariables.mStoreID,reviewId,"down");
			}
		});

		mPreviousButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				currentPosition = currentPosition - 1;
				if(currentPosition >=0){
					mViewReviewDetails.scrollTo(0, 0);		
					GetReviewRatesStatusTask mReviewStatus = new GetReviewRatesStatusTask(MainMenuActivity.this,mViewReviewName,mViewReviewPostedDate,mViewReviewMessage,mLikeCount,mDislikeCount,
							mLikeImage,mDislikeImage,mViewReviewStarOne,mViewReviewStarTwo,mViewReviewStarThree,mViewReviewStarFour,mViewReviewStarFive,mFooterRightText,currentPosition);
					mReviewStatus.execute(RightMenuStoreId_ClassVariables.mStoreID);
					POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(currentPosition);
					reviewId = mReviewDetails.review_id;
					if(currentPosition == 0){
						mPreviousButton.setBackgroundResource(R.drawable.button_disabled);
						mNextButton.setBackgroundResource(R.drawable.header_2);
					}else{
						mPreviousButton.setBackgroundResource(R.drawable.header_2);
						mNextButton.setBackgroundResource(R.drawable.header_2);  
					}
				}else{
					currentPosition = 0;

				}
			}
		});

		mNextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				currentPosition = currentPosition + 1;
				if(currentPosition <= WebServiceStaticArrays.mStoreReviewsList.size()-1 ){
					mViewReviewDetails.scrollTo(0, 0);
					GetReviewRatesStatusTask mReviewStatus = new GetReviewRatesStatusTask(MainMenuActivity.this,mViewReviewName,mViewReviewPostedDate,mViewReviewMessage,mLikeCount,mDislikeCount,
							mLikeImage,mDislikeImage,mViewReviewStarOne,mViewReviewStarTwo,mViewReviewStarThree,mViewReviewStarFour,mViewReviewStarFive,mFooterRightText,currentPosition);
					mReviewStatus.execute(RightMenuStoreId_ClassVariables.mStoreID);
					POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(currentPosition);
					reviewId = mReviewDetails.review_id;
					if(currentPosition == WebServiceStaticArrays.mStoreReviewsList.size()-1 ){
						mNextButton.setBackgroundResource(R.drawable.button_disabled);
						mPreviousButton.setBackgroundResource(R.drawable.header_2);
					}else{
						mNextButton.setBackgroundResource(R.drawable.header_2);
						mPreviousButton.setBackgroundResource(R.drawable.header_2);  
					}
				}else{
					currentPosition = WebServiceStaticArrays.mStoreReviewsList.size()-1;
				}
			}
		});

		mFooterRightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PostReviewTask mPostReviewTask = new PostReviewTask(MainMenuActivity.this,reviewslistcontainer,mEditReviewDetails,mReviewList,mReviewStoreTitle,mReviewRatingOne,mReviewRatingTwo,mReviewRatingThree,mReviewRatingFour,mReviewRatingFive,mFooterLeftText);
				mPostReviewTask.execute("INAPPROPRIATE",reviewId);
			}
		});

		mFooterLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mFooterLeftText.getText().toString().equalsIgnoreCase("back")){
					reviewslistcontainer.setVisibility(View.VISIBLE);
					mViewReviewDetails.setVisibility(View.GONE);
					mEditReviewDetails.setVisibility(View.GONE);
					if(user_login_type.equalsIgnoreCase("customer")){  // Customer
						mReviewFooterContainer.setVisibility(View.VISIBLE);
						mFooterRightText.setVisibility(View.GONE);
					}else{
						mReviewFooterContainer.setVisibility(View.GONE);
					}
					WebServiceStaticArrays.GetReviewStatusList.clear();
					MainMenuActivity.mReviewStart ="0";
					MainMenuActivity.mReviewEndLimit="20";
					if(mReviewList.getFooterViewsCount() == 0){
						mReviewList.addFooterView(mFooterLayout);
						mFooterText.setText("");
					}
					GetStoreReviewDetails reviewDetails = new GetStoreReviewDetails(MainMenuActivity.this, mReviewList,mReviewStoreTitle,mReviewRatingOne,mReviewRatingTwo,mReviewRatingThree,mReviewRatingFour,mReviewRatingFive,mFooterLeftText,"PROGRESS",POJOMainMenuActivityTAG.TAG);
					reviewDetails.execute("OFFSET");
				}else if(mFooterLeftText.getText().toString().equalsIgnoreCase("Edit Review")){
					clearReviewDetails();
					reviewslistcontainer.setVisibility(View.GONE);
					mViewReviewDetails.setVisibility(View.GONE);
					mEditReviewDetails.setVisibility(View.VISIBLE);
					mStoreRating="0";
					mFooterLeftText.setText("Edit Review");
					mFooterLeftText.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.editreview,0,0);
					mFooterLeftText.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mFooterRightText.setVisibility(View.GONE);
					if(UserReviewPosition !=-1  && WebServiceStaticArrays.mStoreReviewsList.size()>0){
						POJOStoreReviewDetails mReviewDetails = (POJOStoreReviewDetails) WebServiceStaticArrays.mStoreReviewsList.get(UserReviewPosition);
						reviewId = mReviewDetails.review_id;
						mUpdateReviewMessage.setText(mReviewDetails.message);
						mStoreRating = mReviewDetails.rating;
						switch(Integer.parseInt(mReviewDetails.rating)){
						case 1:
							mRateOnebutton.setBackgroundResource(R.drawable.star);
							break;
						case 2:
							mRateOnebutton.setBackgroundResource(R.drawable.star);
							mRateTwobutton.setBackgroundResource(R.drawable.star);
							break;
						case 3:
							mRateOnebutton.setBackgroundResource(R.drawable.star);
							mRateTwobutton.setBackgroundResource(R.drawable.star);
							mRateThreebutton.setBackgroundResource(R.drawable.star);
							break;
						case 4:
							mRateOnebutton.setBackgroundResource(R.drawable.star);
							mRateTwobutton.setBackgroundResource(R.drawable.star);
							mRateThreebutton.setBackgroundResource(R.drawable.star);
							mRateFourbutton.setBackgroundResource(R.drawable.star);
							break;
						case 5:
							mRateOnebutton.setBackgroundResource(R.drawable.star);
							mRateTwobutton.setBackgroundResource(R.drawable.star);
							mRateThreebutton.setBackgroundResource(R.drawable.star);
							mRateFourbutton.setBackgroundResource(R.drawable.star);
							mRateFivebutton.setBackgroundResource(R.drawable.star);
							break;
						}
					}
				}else if(mFooterLeftText.getText().toString().equalsIgnoreCase("Post Review")){
					
					clearReviewDetails();
					reviewslistcontainer.setVisibility(View.GONE);
					mViewReviewDetails.setVisibility(View.GONE);
					mEditReviewDetails.setVisibility(View.VISIBLE);
					mStoreRating="0";
					mFooterLeftText.setText("Post Review");
					mFooterLeftText.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.postreview,0,0);
					mFooterLeftText.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mFooterRightText.setVisibility(View.GONE);
				}
			}
		});

		mRateOnebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRateOnebutton.setBackgroundResource(R.drawable.star);
				mRateTwobutton.setBackgroundResource(R.drawable.button_border);
				mRateThreebutton.setBackgroundResource(R.drawable.button_border);
				mRateFourbutton.setBackgroundResource(R.drawable.button_border);
				mRateFivebutton.setBackgroundResource(R.drawable.button_border);
				mStoreRating="1";
			}
		});

		mRateTwobutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRateOnebutton.setBackgroundResource(R.drawable.star);
				mRateTwobutton.setBackgroundResource(R.drawable.star);
				mRateThreebutton.setBackgroundResource(R.drawable.button_border);
				mRateFourbutton.setBackgroundResource(R.drawable.button_border);
				mRateFivebutton.setBackgroundResource(R.drawable.button_border);
				mStoreRating="2";
			}
		});

		mRateThreebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRateOnebutton.setBackgroundResource(R.drawable.star);
				mRateTwobutton.setBackgroundResource(R.drawable.star);
				mRateThreebutton.setBackgroundResource(R.drawable.star);
				mRateFourbutton.setBackgroundResource(R.drawable.button_border);
				mRateFivebutton.setBackgroundResource(R.drawable.button_border);
				mStoreRating="3";
			}
		});

		mRateFourbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRateOnebutton.setBackgroundResource(R.drawable.star);
				mRateTwobutton.setBackgroundResource(R.drawable.star);
				mRateThreebutton.setBackgroundResource(R.drawable.star);
				mRateFourbutton.setBackgroundResource(R.drawable.star);
				mRateFivebutton.setBackgroundResource(R.drawable.button_border);
				mStoreRating="4";
			}
		});

		mRateFivebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mRateOnebutton.setBackgroundResource(R.drawable.star);
				mRateTwobutton.setBackgroundResource(R.drawable.star);
				mRateThreebutton.setBackgroundResource(R.drawable.star);
				mRateFourbutton.setBackgroundResource(R.drawable.star);
				mRateFivebutton.setBackgroundResource(R.drawable.star);
				mStoreRating="5";
			}
		});

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				reviewslistcontainer.setVisibility(View.VISIBLE);
				mViewReviewDetails.setVisibility(View.GONE);
				mEditReviewDetails.setVisibility(View.GONE);
				WebServiceStaticArrays.GetReviewStatusList.clear();
				mFooterLeftText.setBackgroundResource(R.drawable.header_2);
			}
		});

		mPost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mFooterLeftText.getText().toString().equalsIgnoreCase("Edit Review")){
					if(mUpdateReviewMessage.getText().toString().trim().equalsIgnoreCase("")){
						alertBox_service("Information", "Please enter a review message");
					}else if(mStoreRating.equalsIgnoreCase("0")){
						alertBox_service("Information", "Rate store");
					}else{
						if(mReviewList.getFooterViewsCount() == 0){
							mReviewList.addFooterView(mFooterLayout);
							mFooterText.setText("");
						}
						PostReviewTask mPostReviewTask = new PostReviewTask(MainMenuActivity.this,reviewslistcontainer,mEditReviewDetails,mReviewList,mReviewStoreTitle,mReviewRatingOne,mReviewRatingTwo,mReviewRatingThree,mReviewRatingFour,mReviewRatingFive,mFooterLeftText);
						mPostReviewTask.execute("UPDATE",reviewId,mStoreRating,mUpdateReviewMessage.getText().toString());
					}
				}else if(mFooterLeftText.getText().toString().equalsIgnoreCase("Post Review")){
					if(mUpdateReviewMessage.getText().toString().trim().equalsIgnoreCase("")){
						alertBox_service("Information", "Please enter a review message");
					}else if(mStoreRating.equalsIgnoreCase("0")){
						alertBox_service("Information", "Rate store");
					}else{
						Log.i("post", "post");
						if(mReviewList.getFooterViewsCount() == 0){
							mReviewList.addFooterView(mFooterLayout);
							mFooterText.setText("");
						}
						PostReviewTask mPostReviewTask = new PostReviewTask(MainMenuActivity.this,reviewslistcontainer,mEditReviewDetails,mReviewList,mReviewStoreTitle,mReviewRatingOne,mReviewRatingTwo,mReviewRatingThree,mReviewRatingFour,mReviewRatingFive,mFooterLeftText);
						mPostReviewTask.execute("POST",RightMenuStoreId_ClassVariables.mStoreID,mStoreRating,mUpdateReviewMessage.getText().toString());
					}
				}
			}
		});
	}

	//Function to create Customer Center page
	protected void setCustomerCenter(int customercenter){
		POJOMainMenuActivityTAG.TAG="MainMenuActivity_CustomerCenter";
		POJOMainMenuActivityTAG.ChatFlag="";
		app = inflater.inflate(customercenter, null);
		int MenuFlag =0;	

		mIsLoadMore = false;
		mCustomerCenterStart ="0";
		mCustomerCenterEndLimit ="20";
		mCustomerCenterTotalCount ="0";

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.customercenter_tabbar);
		ViewGroup customercenter_container1 = (ViewGroup) app.findViewById(R.id.customercenter_container1);
		ViewGroup customercenter_container2 = (ViewGroup) app.findViewById(R.id.customercenter_container2);

		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_customercenter);		
		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.customercenter_freeze);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));
		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView));

		// Notification and log out variables
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		mNotificationImage.setAlpha(100);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mNotificationCount.setVisibility(View.INVISIBLE);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();

		//To Initialize pop up layout
		//InitializePopUpLayout(tabBar);

		//Initialize logout click listener
		InitializeLogoutClickListener();
		View[] children = new View[] { sLeftMenu, app};

		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

		mCustomerCenter_ZouponsSupportButton = (LinearLayout) customercenter_container1.findViewById(R.id.customercenter_customerstore);
		mCustomerCenter_ZouponsSupportCount = (TextView) customercenter_container1.findViewById(R.id.customercenter_customerstorecount);
		mCustomerCenter_StoreListView = (ListView) customercenter_container2.findViewById(R.id.customercenter_storelistview);

		GetCustomerCommunicatedStoreAsyncTask customerserviceasynctask = new GetCustomerCommunicatedStoreAsyncTask(MainMenuActivity.this, mCustomerCenter_StoreListView, "PROGRESS",mCustomerCenter_ZouponsSupportCount);
		customerserviceasynctask.execute("");

		//For Footer Layout
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterLayout = mInflater.inflate(R.layout.footerlayout, null, false);
		mFooterText = (TextView) mFooterLayout.findViewById(R.id.footerText);
		mCustomerCenter_StoreListView.addFooterView(mFooterLayout);

		mCustomerCenter_StoreListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				//Check the bottom item is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;		

				if(Integer.parseInt(mCustomerCenterTotalCount) > lastInScreen && Integer.parseInt(mCustomerCenterTotalCount)>20){					
					if(lastInScreen == totalItemCount && !mIsLoadMore){												
						
						if(mCustomerCenter_StoreListView.getFooterViewsCount() == 0){
							mCustomerCenter_StoreListView.addFooterView(mFooterLayout);
						}
						if(Integer.parseInt(mCustomerCenterTotalCount) > Integer.parseInt(mCustomerCenterEndLimit)){
							mFooterText.setText("Loading "+mCustomerCenterEndLimit+" of "+"("+mCustomerCenterTotalCount+")");
						}else{
							mFooterText.setText("Loading "+mCustomerCenterTotalCount);
						}
						GetCustomerCommunicatedStoreAsyncTask customerserviceasynctask = new GetCustomerCommunicatedStoreAsyncTask(MainMenuActivity.this, mCustomerCenter_StoreListView, "NOPROGRESS",mCustomerCenter_ZouponsSupportCount);
						customerserviceasynctask.execute("RefreshAdapter");
					}
				}else{
					if(mCustomerCenterTotalCount.equalsIgnoreCase("0")){
					}else if(mFooterLayout != null && mCustomerCenter_StoreListView.getFooterViewsCount() !=0 &&mCustomerCenter_StoreListView.getAdapter() != null){
	   					if(lastInScreen!= totalItemCount){
							mCustomerCenter_StoreListView.removeFooterView(mFooterLayout);	
						}else{
							
						}
					}
				}

			}
		});

	  mCustomerCenter_ZouponsSupportButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!mCustomerCenter_ZouponsSupportCount.getText().toString().trim().equalsIgnoreCase("0")){
					mCustomerCenter_ZouponsSupportCount.setText("0");
				}
				Intent intent_talktous = new Intent(MainMenuActivity.this,ZouponsSupport.class);
				startActivity(intent_talktous);
			}
		});
	}

	public void SetCustomerServiceArrayListAdatpter(String mCheckRefresh){
		if(mCheckRefresh.equalsIgnoreCase("RefreshAdapter") && mReviewAdapter != null){
			Log.i("Adapter", "Refresh");
			mReviewAdapter.notifyDataSetChanged();
		}else{
			Log.i("Adapter", "Add Adapter");			
			mCustomerServiceListAdaper = new ChatSupportListAdapter(MainMenuActivity.this);
			mCustomerCenter_StoreListView.setAdapter(mCustomerServiceListAdaper);			
		}

		if(mFooterLayout != null && mCustomerCenter_StoreListView.getFooterViewsCount() !=0 &&mCustomerCenter_StoreListView.getAdapter() != null){
			Log.i(TAGG, "Remove Footer View");
			mCustomerCenter_StoreListView.removeFooterView(mFooterLayout);
		}
	}

	public void SetReviewListAdatpter(String mCheckRefresh) {
		// TODO Auto-generated method stub

		if(mCheckRefresh.equalsIgnoreCase("RefreshAdapter") && mReviewAdapter != null){
			Log.i("Adapter", "Refresh");
			mReviewAdapter.notifyDataSetChanged();
		}else{
			Log.i("Adapter", "Add Adapter");			
			mReviewAdapter = new StoreReviewListAdapter(MainMenuActivity.this);
			mReviewList.setAdapter(mReviewAdapter);			
		}

		if(mFooterLayout != null && mReviewList.getFooterViewsCount() !=0 &&mReviewList.getAdapter() != null){
			Log.i(TAGG, "Remove Footer View");
			mReviewList.removeFooterView(mFooterLayout);
		}
	}

	private void clearReviewDetails() {
		mUpdateReviewMessage.getText().clear();
		mRateOnebutton.setBackgroundResource(R.drawable.button_border);
		mRateTwobutton.setBackgroundResource(R.drawable.button_border);
		mRateThreebutton.setBackgroundResource(R.drawable.button_border);
		mRateFourbutton.setBackgroundResource(R.drawable.button_border);
		mRateFivebutton.setBackgroundResource(R.drawable.button_border);
	}

	protected void setTransactionDetails(int layout){
		POJOMainMenuActivityTAG.TAG="MainMenuActivity_Transactionhistory";
		app = inflater.inflate(layout, null);
		int MenuFlag =0;

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.transaction_history_tabbar);
		ViewGroup menubar = (ViewGroup) app.findViewById(R.id.transaction_history_menubar);
		ViewGroup middleview = (ViewGroup) app.findViewById(R.id.transaction_history_list_container);

		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_transaction_history);
		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.transaction_history_freeze);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));
		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView));
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		View[] children = new View[] { sLeftMenu, app};
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

		mTransactionHistoryBack =(TextView) menubar.findViewById(R.id.transaction_history_back);
		mTransactionDetailsListView =(ListView) middleview.findViewById(R.id.transaction_listview);

		mTransactionHistoryBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		GiftCardTransactionHistoryTask mHistroyTask = new GiftCardTransactionHistoryTask(MainMenuActivity.this,mTransactionDetailsListView);
		mHistroyTask.execute(MenuUtilityClass.sGiftCardPurchaseId);
	}

	// To display notification messages in customer center 
	protected void customercenternotifications(int customercenterNotifications) {

		View[] children;
		POJOMainMenuActivityTAG.TAG="MainMenuActivity_CustomercenterNotifications";
		app = inflater.inflate(customercenterNotifications, null);
		int MenuFlag =0;
		String notificationIds;

		ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.customercenter_notifications_tabBar);
		ViewGroup customercenter_notifications_footer = (ViewGroup) app.findViewById(R.id.customercenter_notifications_footerLayoutId);

		mListView = (ListView) app.findViewById(R.id.customercenter_notifications_listview);
		mMainMenuActivityFreezeView=(Button) app.findViewById(R.id.customercenter_notifications_freeze);
		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide_customercenter_notifications);
		btnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu,MenuFlag=1,mMainMenuActivityFreezeView));

		// Notification and log out variables
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(MainMenuActivity.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(MainMenuActivity.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();
		TextView mCusotmercenterNotificationBack = (TextView) customercenter_notifications_footer.findViewById(R.id.customercenter_notifications_backtextId);
		mMainMenuActivityFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sRightMenu, MenuFlag, mMainMenuActivityFreezeView, mAddMessage));
		//To Initialize pop up layout
		InitializePopUpLayout(tabBar);
		//Initialize logout click listener
		InitializeLogoutClickListener();
		children = new View[] { sLeftMenu, app };
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));
		StringBuilder sb = new StringBuilder();
		ArrayList<Object> mNotifications = new ArrayList<Object>();
		for(int i=0 ;i < WebServiceStaticArrays.mAllNotifications.size();i++){
			NotificationDetails mNotificationDetails = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(i);
			if(!mNotificationDetails.notification_type.equalsIgnoreCase("store_message") && !mNotificationDetails.notification_type.equalsIgnoreCase("zoupons_message")&& !mNotificationDetails.notification_type.equalsIgnoreCase("") ){
				sb.append(mNotificationDetails.id+","); // value with comma
				mNotifications.add(mNotificationDetails); 
			}
		}
		sb.delete(sb.length()-1, sb.length()); // remove last character, which is the comma.
		notificationIds = sb.toString(); // get the result string.

		UnreadToReadMessage unreadtoreadtask = new UnreadToReadMessage(MainMenuActivity.this);
		unreadtoreadtask.execute(notificationIds);

		mListView.setAdapter(new ChatSupportNotificationsAdapter(MainMenuActivity.this,mNotifications));

		// click listener for back button
		mCusotmercenterNotificationBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * Helper for examples with a HSV that should be scrolled by a menu View's width.
	 */
	class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View leftMenu,rightMenu;
		int menuFlag;
		Button mFreezeView;
		EditText mAddText;
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

		public ClickListenerForScrolling(HorizontalScrollView scrollView, View leftmenu, View rightmenu,int menuflag,Button freezeview,EditText addtext) {
			super();
			this.scrollView = scrollView;
			this.leftMenu = leftmenu;
			this.rightMenu = rightmenu;
			this.menuFlag=menuflag;
			this.mFreezeView = freezeview;
			this.mAddText=addtext;
		}

		@Override
		public void onClick(View v) {
			
			if(mHeaderProgressBar != null && mHeaderLoadingText != null){ // To hide loading progress in header view in communication while opening menu
				mHeaderProgressBar.setVisibility(View.GONE);
				mHeaderLoadingText.setVisibility(View.GONE);
			}
			if(mAddText!=null){
				mAddText.clearFocus();
			}
			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);

			rightMenuScrollView.fullScroll(View.FOCUS_UP);
			rightMenuScrollView.pageScroll(View.FOCUS_UP);

			Context context = leftMenu.getContext();
			int menuWidth = leftMenu.getMeasuredWidth();
			
			// Ensure menu is visible
			leftMenu.setVisibility(View.VISIBLE);

			InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

			//getCurrentFocus().clearFocus();

			if (!MenuOutClass.WHOLE_MENUOUT) {
				if(menuFlag==1){
					// To enable/disable left menu items based upon user login type [Customer or Guest]
					setLeftMenuRightMenuItemStatus(context);
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
			MenuOutClass.WHOLE_MENUOUT = !MenuOutClass.WHOLE_MENUOUT;
		}
	}
	
	public static void setLeftMenuRightMenuItemStatus(Context context){
		SharedPreferences mUserDetailsPrefs = context.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");
		if(user_login_type.equalsIgnoreCase("customer")){  // Customer
			// Zpay LeftMenu 
			/*mZpay.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mZpay.setEnabled(true);
			mZpayText.setTextColor(Color.WHITE);
			mZpayImage.setAlpha(255);*/
			mZpay.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mZpay.setEnabled(false);
			mZpayText.setTextColor(Color.GRAY);
			mZpayImage.setAlpha(100);
			// InvoiceCenter LeftMenu 
			/*mInvoiceCenter.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mInvoiceCenter.setEnabled(true);
			mInvoiceCenterText.setTextColor(Color.WHITE);
			mInvoiceCenterImage.setAlpha(255);*/
			mInvoiceCenter.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mInvoiceCenter.setEnabled(false);
			mInvoiceCenterText.setTextColor(Color.GRAY);
			mInvoiceCenterImage.setAlpha(100);
			// Gitcards LeftMenu 
			/*mZGiftcards.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mZGiftcards.setEnabled(true);
			mGiftCardsText.setTextColor(Color.WHITE);
			mGiftCardsImage.setAlpha(255);*/
			mZGiftcards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mZGiftcards.setEnabled(false);
			mGiftCardsText.setTextColor(Color.GRAY);
			mGiftCardsImage.setAlpha(100);
			// ManageCards LeftMenu 
			/*mManageCards.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mManageCards.setEnabled(true);
			mManageCardsText.setTextColor(Color.WHITE);
			mManageCardsImage.setAlpha(255);*/
			mManageCards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mManageCards.setEnabled(false);
			mManageCardsText.setTextColor(Color.GRAY);
			mManageCardsImage.setAlpha(100);
			// Receipts LeftMenu 
			/*mReceipts.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mReceipts.setEnabled(true);
			mReceiptsText.setTextColor(Color.WHITE);
			mReceiptsImage.setAlpha(255);*/
			mReceipts.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mReceipts.setEnabled(false);
			mReceiptsText.setTextColor(Color.GRAY);
			mReceiptsImage.setAlpha(100);
			// Favorites LeftMenu 
			mMyFavourites.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mMyFavourites.setEnabled(true);
			mMyFavoritesText.setTextColor(Color.WHITE);
			mMyFavoritesImage.setAlpha(255);
			// Friends LeftMenu 
			mMyFriends.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mMyFriends.setEnabled(true);
			mMyFriendsText.setTextColor(Color.WHITE);
			mMyFriendsImage.setAlpha(255);
			// CustomerCenter LeftMenu 
			mChat.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mChat.setEnabled(true);
			mChatText.setTextColor(Color.WHITE);
			mChatImage.setAlpha(255);
			// ReferStore LeftMenu 
			mRewards.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mRewards.setEnabled(true);
			mRewardsText.setTextColor(Color.WHITE);
			mRewardsImage.setAlpha(255);
			// Settings LeftMenu 
			mSettings.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			mSettings.setEnabled(true);
			mSettingsText.setTextColor(Color.WHITE);
			mSettingsImage.setAlpha(255);
		}else{  // Guest
			// Zpay LeftMenu
			mZpay.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mZpay.setEnabled(false);
			mZpayText.setTextColor(Color.GRAY);
			mZpayImage.setAlpha(100);
			// InvoiceCenter LeftMenu
			mInvoiceCenter.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mInvoiceCenter.setEnabled(false);
			mInvoiceCenterText.setTextColor(Color.GRAY);
			mInvoiceCenterImage.setAlpha(100);
			// Gitcards LeftMenu
			mZGiftcards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mZGiftcards.setEnabled(false);
			mGiftCardsText.setTextColor(Color.GRAY);
			mGiftCardsImage.setAlpha(100);
			// ManageCards LeftMenu
			mManageCards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mManageCards.setEnabled(false);
			mManageCardsText.setTextColor(Color.GRAY);
			mManageCardsImage.setAlpha(100);
			// Receipts LeftMenu
			mReceipts.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mReceipts.setEnabled(false);
			mReceiptsText.setTextColor(Color.GRAY);
			mReceiptsImage.setAlpha(100);
			// Favorites LeftMenu 
			mMyFavourites.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mMyFavourites.setEnabled(false);
			mMyFavoritesText.setTextColor(Color.GRAY);
			mMyFavoritesImage.setAlpha(100);
			// Friends LeftMenu
			mMyFriends.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mMyFriends.setEnabled(false);
			mMyFriendsText.setTextColor(Color.GRAY);
			mMyFriendsImage.setAlpha(100);
			// CustomerCenter LeftMenu 
			mChat.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mChat.setEnabled(false);
			mChatText.setTextColor(Color.GRAY);
			mChatImage.setAlpha(100);
			// ReferStore LeftMenu 
			mRewards.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mRewards.setEnabled(false);
			mRewardsText.setTextColor(Color.GRAY);
			mRewardsImage.setAlpha(100);
			// Settings LeftMenu
			mSettings.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mSettings.setEnabled(false);
			mSettingsText.setTextColor(Color.GRAY);
			mSettingsImage.setAlpha(100);
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

	private static String pad(int c){
		if(c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	//Get Screen width
	public int getScreenWidth(){
		int Measuredwidth = 0;  
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Measuredwidth = display.getWidth(); 
		return Measuredwidth;
	}

	private void InitializePopUpLayout(final ViewGroup tabBar){
		// Notitification pop up layout declaration
		mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
		mNotificationImage.setOnClickListener(new ManageNotificationWindow(MainMenuActivity.this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		mNotificationCount.setOnClickListener(new ManageNotificationWindow(MainMenuActivity.this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
	}

	private void InitializeLogoutClickListener(){
		mLogoutImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(MainMenuActivity.this,"FromManualLogOut").execute();
			}
		});
	}

	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(MainMenuActivity.this);
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

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			if(POJOMainMenuActivityTAG.TAG.equalsIgnoreCase("MainMenuActivity_StorePhotos") && WebServiceStaticArrays.mStorePhoto.size() > 0){
				if(getIntent().getExtras().getString("Activity").equalsIgnoreCase("Photos")){
					int width=(mStoreAllPhotos.getWidth()-20)/3;
					mStoreAllPhotos.setAdapter(new StorePhotoGridAdapter(MainMenuActivity.this,width));

					mImagePagerWidth = mImagePager.getWidth();
					int widthpropotion = mImagePagerWidth/5;
					mImagePagerHeight = widthpropotion*4;
					//mImagePagerHeight = mImagePager.getHeight();
				}else {
					mImagePager.setAdapter(new StorePhotoAdapter(MainMenuActivity.this,mImagePager.getWidth(),mImagePager.getHeight()));
					mImagePager.setCurrentItem(position-1);
					mIndicator.setViewPager(mImagePager);
					mIndicator.setCurrentItem(position-1);
					mIndicator.setSelectedColor(getResources().getColor(R.color.blue));	
					mStorePhotosBackButton.setVisibility(View.INVISIBLE);
					mStorePhotosBackMenuSplitter.setVisibility(View.INVISIBLE);
				}
			}else if(POJOMainMenuActivityTAG.TAG.equalsIgnoreCase("MainMenuActivity_StoreInfo")){
			}else if(POJOMainMenuActivityTAG.TAG.equalsIgnoreCase("MainMenuActivity_ContactStore")||POJOMainMenuActivityTAG.TAG.equalsIgnoreCase("MainMenuActivity_TalkToUs")){
				mAddMessage.setFocusable(true);
				mAddMessage.setFocusableInTouchMode(true);
			}else if(POJOMainMenuActivityTAG.TAG.equalsIgnoreCase("MainMenuActivity_Rewards")){
				if(mVideoPlay!=null){
					mImageViewWidth=mVideoPlay.getWidth();
					mImageViewHeight=mVideoPlay.getHeight();
				}
				if(mVideoThumbnail!=null){
					MainMenuActivity.mAdSlotImageViewWidth=mVideoThumbnail.getWidth();
				}
			}else if(POJOMainMenuActivityTAG.TAG.equalsIgnoreCase("MainMenuActivity_PlayVideo")){
				if(mVideoImage!=null){
					mImageViewWidth=mVideoImage.getWidth();
					mImageViewHeight=mVideoImage.getHeight();
				}
			}
		}
	}

	void startMyTask(AsyncTask<String, String, String> asyncTask) {
		if(Build.VERSION.SDK_INT >= 11)
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
		else
			asyncTask.execute("");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try{
			Log.i(TAGG,"onDestroy");
			if(progressdialog!=null){
				progressdialog.dismiss();
			}
			if(mFacebookUILifeCycleHelper!=null)
				mFacebookUILifeCycleHelper.onDestroy();
			// To notify system that its time to run garbage collector service
			System.gc();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(TAGG, "onRestart");
		if(POJOMainMenuActivityTAG.TAG.equalsIgnoreCase("MainMenuActivity_CustomerCenter"))
				POJOMainMenuActivityTAG.ChatFlag = "";
		//If it's from Favorite Screen Then Need to Referesh Adapter Coupons List Adapter
		if(mIsRefreshAdapter){
			mIsRefreshAdapter = false;
			CouponListAdapter mAdapter = new CouponListAdapter(MainMenuActivity.this,"FavoriteCoupon");
			mAdapter.notifyDataSetChanged();
			mCouponsListView.setAdapter(mAdapter);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAGG,"onPause");
		if(mNotificationReceiver != null && !getUserType().equalsIgnoreCase("Guest")){
			unregisterReceiver(mNotificationReceiver);
			if(mNotificationSync!=null/* && !POJOMainMenuActivityTAG.TAG.equalsIgnoreCase("MainMenuActivity_CustomerCenter")*/){
				mNotificationSync.cancelAlarm();
			}	
		}
		if(mFacebookUILifeCycleHelper!=null)
			mFacebookUILifeCycleHelper.onPause();
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(MainMenuActivity.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkIfUserDetailsVariableCleared();
		if(mFacebookUILifeCycleHelper!=null)
			mFacebookUILifeCycleHelper.onResume();
		if(!getUserType().equalsIgnoreCase("Guest")){
			registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
			// To start notification sync
			mNotificationSync = new ScheduleNotificationSync(MainMenuActivity.this,ZouponsConstants.sCustomerModuleFlag);
			mNotificationSync.setRecurringAlarm();
		}
		new CheckUserSession(MainMenuActivity.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(MainMenuActivity.this);
		mLogoutSession.setLogoutTimerAlarm();
		
	}

	private void checkIfUserDetailsVariableCleared() { // If facebook application uninstalled then all static variables will be cleared so navigate to login page
		// TODO Auto-generated method stub
		SharedPreferences mPrefs = getSharedPreferences("UserNamePrefences", MODE_PRIVATE);
		if(UserDetails.mServiceUserId.equalsIgnoreCase("") && !mPrefs.getString("user_login_type", "").equalsIgnoreCase("Guest")){
			// To launch Login activity
			Intent loginIntent= new Intent(MainMenuActivity.this,ZouponsLogin.class);
			loginIntent.putExtra("FromManualLogOut", true);
			loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(loginIntent);
		}
	}

	private String getUserType(){
		SharedPreferences mPrefs = getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		return mPrefs.getString("user_login_type", "");
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
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if(mFacebookUILifeCycleHelper!=null)
			mFacebookUILifeCycleHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		if(loadThread!=null){
			loadThread.interrupt();
		}
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	public static void SetRightMenuStatus(Context context){

		// To set store name in right menu..
		mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
		mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);

		if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("no")){  
			MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
		}else{
			MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
		}
		
		SharedPreferences mUserDetailsPrefs = context.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");
		if(user_login_type.equalsIgnoreCase("customer")){  // Customer
			/*
			 * RightMenu Zpay Enable or disable
			 * */
			if(RightMenuStoreId_ClassVariables.rightmenu_aboutus_flag.equalsIgnoreCase("no")){
				MainMenuActivity.mInfo_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				MainMenuActivity.mInfo_RightMenu.setEnabled(false);
				MainMenuActivity.mInfoText_RightMenu.setTextColor(Color.GRAY);
				MainMenuActivity.mInfoImage_RightMenu.setAlpha(100);
				
			}else{
				//MainMenuActivity.mInfo_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				MainMenuActivity.mInfo_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				MainMenuActivity.mInfo_RightMenu.setEnabled(true);
				MainMenuActivity.mInfoText_RightMenu.setTextColor(Color.WHITE);
				MainMenuActivity.mInfoImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_pointofsale_flag.equalsIgnoreCase("no")){  
				MainMenuActivity.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				MainMenuActivity.mMobilePay_RightMenu.setEnabled(false);
				MainMenuActivity.mMobilePayText_RightMenu.setTextColor(Color.GRAY);
				MainMenuActivity.mMobilePayImage_RightMenu.setAlpha(100);
			}else{
				//MainMenuActivity.mMobilePay_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				MainMenuActivity.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				MainMenuActivity.mMobilePay_RightMenu.setEnabled(true);
				MainMenuActivity.mMobilePayText_RightMenu.setTextColor(Color.WHITE);
				MainMenuActivity.mMobilePayImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_giftcards_flag.equalsIgnoreCase("no")){  
				MainMenuActivity.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				MainMenuActivity.mGiftCards_RightMenu.setEnabled(false);
				MainMenuActivity.mGiftCardsText_RightMenu.setTextColor(Color.GRAY);
				MainMenuActivity.mGiftCardsImage_RightMenu.setAlpha(100);
			}else{
				//MainMenuActivity.mGiftCards_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				MainMenuActivity.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				MainMenuActivity.mGiftCards_RightMenu.setEnabled(true);
				MainMenuActivity.mGiftCardsText_RightMenu.setTextColor(Color.WHITE);
				MainMenuActivity.mGiftCardsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_zcards_flag.equalsIgnoreCase("no")){
				MainMenuActivity.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				MainMenuActivity.mDeals_RightMenu.setEnabled(false);
				MainMenuActivity.mDealsText_RightMenu.setTextColor(Color.GRAY);
				MainMenuActivity.mDealsImage_RightMenu.setAlpha(100);
			}else{
				//MainMenuActivity.mDeals_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				MainMenuActivity.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				MainMenuActivity.mDeals_RightMenu.setEnabled(true);
				MainMenuActivity.mDealsText_RightMenu.setTextColor(Color.WHITE);
				MainMenuActivity.mDealsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_coupons_flag.equalsIgnoreCase("no")){
				MainMenuActivity.mCoupons_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				MainMenuActivity.mCoupons_RightMenu.setEnabled(false);
				MainMenuActivity.mCouponsText_RightMenu.setTextColor(Color.GRAY);
				MainMenuActivity.mCouponsImage_RightMenu.setAlpha(100);
			}else{
				//MainMenuActivity.mCoupons_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				MainMenuActivity.mCoupons_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				MainMenuActivity.mCoupons_RightMenu.setEnabled(true);
				MainMenuActivity.mCouponsText_RightMenu.setTextColor(Color.WHITE);
				MainMenuActivity.mCouponsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_reviewandrating_flag.equalsIgnoreCase("no")){
				MainMenuActivity.mReviews_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				MainMenuActivity.mReviews_RightMenu.setEnabled(false);
				MainMenuActivity.mReviewsText_RightMenu.setTextColor(Color.GRAY);
				MainMenuActivity.mReviewsImage_RightMenu.setAlpha(100);
			}else{
				//MainMenuActivity.mReviews_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				MainMenuActivity.mReviews_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				MainMenuActivity.mReviews_RightMenu.setEnabled(true);
				MainMenuActivity.mReviewsText_RightMenu.setTextColor(Color.WHITE);
				MainMenuActivity.mReviewsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_location_flag.equalsIgnoreCase("no")){
				MainMenuActivity.mLocations_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				MainMenuActivity.mLocations_RightMenu.setEnabled(false);
				MainMenuActivity.mLocationsText_RightMenu.setTextColor(Color.GRAY);
				MainMenuActivity.mLocationsImage_RightMenu.setAlpha(100);
			}else{
				//MainMenuActivity.mLocations_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				MainMenuActivity.mLocations_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				MainMenuActivity.mLocations_RightMenu.setEnabled(true);
				MainMenuActivity.mLocationsText_RightMenu.setTextColor(Color.WHITE);
				MainMenuActivity.mLocationsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_photos_flag.equalsIgnoreCase("no")){
				MainMenuActivity.mPhotos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				MainMenuActivity.mPhotos_RightMenu.setEnabled(false);
				MainMenuActivity.mPhotosText_RightMenu.setTextColor(Color.GRAY);
				MainMenuActivity.mPhotosImage_RightMenu.setAlpha(100);
			}else{
				//MainMenuActivity.mPhotos_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				MainMenuActivity.mPhotos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				MainMenuActivity.mPhotos_RightMenu.setEnabled(true);
				MainMenuActivity.mPhotosText_RightMenu.setTextColor(Color.WHITE);
				MainMenuActivity.mPhotosImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_videos_flag.equalsIgnoreCase("no")){
				MainMenuActivity.mVideos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				MainMenuActivity.mVideos_RightMenu.setEnabled(false);
				MainMenuActivity.mVideosText_RightMenu.setTextColor(Color.GRAY);
				MainMenuActivity.mVideosImage_RightMenu.setAlpha(100);
			}else{
				//MainMenuActivity.mVideos_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				MainMenuActivity.mVideos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				MainMenuActivity.mVideos_RightMenu.setEnabled(true);
				MainMenuActivity.mVideosText_RightMenu.setTextColor(Color.WHITE);
				MainMenuActivity.mVideosImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_contactstore_flag.equalsIgnoreCase("no")){
				MainMenuActivity.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				MainMenuActivity.mContactStore_RightMenu.setEnabled(false);
				MainMenuActivity.mContactStoreText_RightMenu.setTextColor(Color.GRAY);
				MainMenuActivity.mContactStoreImage_RightMenu.setAlpha(100);
			}else{
				//MainMenuActivity.mContactStore_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				MainMenuActivity.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				MainMenuActivity.mContactStore_RightMenu.setEnabled(true);
				MainMenuActivity.mContactStoreText_RightMenu.setTextColor(Color.WHITE);
				MainMenuActivity.mContactStoreImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_share_flag.equalsIgnoreCase("no")){
				MainMenuActivity.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				MainMenuActivity.mSocial_RightMenu.setEnabled(false);
				MainMenuActivity.mSocialText_RightMenu.setTextColor(Color.GRAY);
				MainMenuActivity.mSocialImage_RightMenu.setAlpha(100);
			}else{
				//MainMenuActivity.mSocial_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				MainMenuActivity.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				MainMenuActivity.mSocial_RightMenu.setEnabled(true);
				MainMenuActivity.mSocialText_RightMenu.setTextColor(Color.WHITE);
				MainMenuActivity.mSocialImage_RightMenu.setAlpha(255);
			}
		}else{
			// Mobile Pay
			mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mMobilePay_RightMenu.setEnabled(false);
			mMobilePayText_RightMenu.setTextColor(Color.GRAY);
			mMobilePayImage_RightMenu.setAlpha(100);
			// MyGiftCards
			mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mGiftCards_RightMenu.setEnabled(false);
			mGiftCardsText_RightMenu.setTextColor(Color.GRAY);
			mGiftCardsImage_RightMenu.setAlpha(100);
			// DealCards
			mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mDeals_RightMenu.setEnabled(false);
			mDealsText_RightMenu.setTextColor(Color.GRAY);
			mDealsImage_RightMenu.setAlpha(100);
			// Share
			mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mSocial_RightMenu.setEnabled(false);
			mSocialText_RightMenu.setTextColor(Color.GRAY);
			mSocialImage_RightMenu.setAlpha(100);
			// Contact store
			mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mContactStore_RightMenu.setEnabled(false);
			mContactStoreText_RightMenu.setTextColor(Color.GRAY);
			mContactStoreImage_RightMenu.setAlpha(100);
			// Favorite
			mFavorite_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			mFavorite_RightMenu.setEnabled(false);
			mFavoriteText_RightMenu.setTextColor(Color.GRAY);
			mFavoriteImage_RightMenu.setAlpha(100);
		}
			
	}
	
	
}
