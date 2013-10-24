/**
 * 
 */
package com.us.zoupons.StaticArrays;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;

import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.ClassVariables.Search_ClassVariables;
import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;

/**
 * @author zoupons
 * Class to add webservice values object
 */
public class WebServiceStaticArrays {
	public static ArrayList<Object> mGetSecurityQuestionsEmailValidationList = new ArrayList<Object>();	//ArrayList for forgotpassword
	public static ArrayList<Object> mGetSecurityQuestionsAndIds = new ArrayList<Object>();	//ArrayList for forgotpassword questions and its id
	public static ArrayList<Object> mSecurityQuestionsList = new ArrayList<Object>();	//ArrayList for Security Questions
	public static  ArrayList<Object> mMobileCarriersList = new ArrayList<Object>();	//ArrayList for Mobile Carriers
	public static  ArrayList<Object> mSignUpList = new ArrayList<Object>();	//ArrayList for SignUp 
	public static  ArrayList<Object> mActivateUser = new ArrayList<Object>();	//ArrayList for Activate User 
	public static  ArrayList<Object> mForgotPasswordList = new ArrayList<Object>();	//ArrayList for SignUp 
	public static  ArrayList<Object> mFirstDataGlobalPaymentList = new ArrayList<Object>();	//ArrayList for first_data_global_payment
	public static  ArrayList<Object> mLoginUserList = new ArrayList<Object>();	//ArrayList for login user
	public static  ArrayList<Object> mChangePasswordList = new ArrayList<Object>();	//ArrayList for change_pwd user
	public static  ArrayList<Object> mCategoriesList = new ArrayList<Object>();	//ArrayList for categories
	public static  ArrayList<Object> mSubCategoriesList = new ArrayList<Object>();	//ArrayList for subcategories
	public static  ArrayList<Object> mStoreCategoriesList = new ArrayList<Object>();	//ArrayList for storecategories
	public static  ArrayList<Search_ClassVariables> mSearchStore = new ArrayList<Search_ClassVariables>();	//ArrayList for search store
	public static  ArrayList<Object> mSearchGiftCard = new ArrayList<Object>();	//ArrayList for search giftcard
	public static  ArrayList<Object> mSearchCoupon = new ArrayList<Object>();	//ArrayList for search coupon
	public static  ArrayList<Object> mStoresLocation = new ArrayList<Object>();	//ArrayList for search storesLocation
	public static  ArrayList<StoreLocator_ClassVariables> mStoresLocator = new ArrayList<StoreLocator_ClassVariables>();	//ArrayList for search storesLocator
	public static ArrayList<Object> mDummyStoresLocator = new ArrayList<Object>();	//ArrayList for search dummy storesLocator
	public static  ArrayList<Object> mStoresQRCode = new ArrayList<Object>();	//ArrayList for search storeQRCode
	public static  ArrayList<Object> mStoresCheckFavorite = new ArrayList<Object>();	//ArrayList for search storeAddFavorite
	public static  ArrayList<Object> mStoresAddFavorite = new ArrayList<Object>();	//ArrayList for search storeAddFavorite
	public static  ArrayList<Object> mCardOnFiles = new ArrayList<Object>();	//ArrayList for CardOnFiles
	public static  ArrayList<Object> mRemoveCard = new ArrayList<Object>();	//ArrayList for RemoveCard
	//public static ArrayList<Object> mStoreLocatorList = new ArrayList<Object>(); //ArrrayList for Location
	public static ArrayList<StoreLocator_ClassVariables> mStoreLocatorList = new ArrayList<StoreLocator_ClassVariables>(); //ArrrayList for Location
	public static ArrayList<Object> mStaticStoreInfo = new ArrayList<Object>();	//Array List for storeInfo
	public static ArrayList<Object> mStorePhoto = new ArrayList<Object>();	//Array List for storeInfo
	public static ArrayList<Object> mStoreTiming = new ArrayList<Object>();	//Array List for storeInfo
	public static ArrayList<String> mFullSizeStorePhotoUrl = new ArrayList<String>();	//Image Url Array List for StoreInfo photoview
	public static ArrayList<Bitmap> mFullSizeStorePhoto = new ArrayList<Bitmap>();	//Bitmap Array List for StoreInfo photoview
	public static ArrayList<Object> mStoreRegularCardDetails = new ArrayList<Object>();	//Array List for GiftCards
	public static ArrayList<Object> mStoreZouponsDealCardDetails = new ArrayList<Object>();	//Array List for Deals	
	public static ArrayList<Object> mSocialNetworkFriendList = new ArrayList<Object>();	// Array List for FB friends
	public static ArrayList<Object> mStoreLikeList = new ArrayList<Object>();	// Array List for FB Like
	public static ArrayList<Object> mVideoList = new ArrayList<Object>();	// Array List for VideoPlay
	public static ArrayList<Object> mShareStore = new ArrayList<Object>();	// Array List for Share_Store
	public static ArrayList<Object> mShareCoupon = new ArrayList<Object>();	// Array List for Share_Coupon
	public static ArrayList<Object> mSearchedFriendList = new ArrayList<Object>();	// Array List for Share_Store
	public static ArrayList<Object> mStaticCouponsArrayList = new ArrayList<Object>();	//Array List for coupons
	public static ArrayList<POJOStoreInfo> mFavouriteStoreDetails = new ArrayList<POJOStoreInfo>();	//Array List for Favorite store details
	public static ArrayList<POJOStoreInfo> mFavouriteFriendStoreDetails = new ArrayList<POJOStoreInfo>();	//Array List for Favorite Friends Store details
	public static HashMap<String, String> categories = new HashMap<String,String>();
    public static ArrayList<Object> mNotificationDetails = new ArrayList<Object>();
    public static ArrayList<Object> mUserProfileList = new ArrayList<Object>();
    public static ArrayList<Object> mUserSecurityQuestionsList = new ArrayList<Object>();
    //Contact store and talktous 
    public static ArrayList<Object> mContactUsResponse = new ArrayList<Object>();
    public static ArrayList<Object> mContactUsResponseDuringScroll = new ArrayList<Object>();
    public static ArrayList<Object> mContactUsResponseResult = new ArrayList<Object>();
    //GiftCards
    public static ArrayList<Object> mAllGiftCardList = new ArrayList<Object>();
    // Reviews
    public static ArrayList<Object> mStoreReviewsList = new ArrayList<Object>();
    public static ArrayList<Object> GetReviewStatusList = new ArrayList<Object>();
    public static ArrayList<Object> mGiftCardTransactionHistory = new ArrayList<Object>();
    // Receipts
    public static ArrayList<Object> mReceiptsDetails = new ArrayList<Object>();
    public static ArrayList<Object> mSearchedReceiptDetails = new ArrayList<Object>();
    
    // To add favourites based on offset  
    public static ArrayList<Object> mOffsetFavouriteStores = new ArrayList<Object>();
    
    // Temp array for store list home page
    public static ArrayList<Object> mOffsetStoreDetails = new ArrayList<Object>();
    
    // Invoice approval
    public static ArrayList<Object> mInvoiceArrayList = new ArrayList<Object>();
    
    // Step2 Initial store giftcard sync 
    public static ArrayList<Object> mMyGiftCards = new ArrayList<Object>();
    
    // For Notifications 
    public static ArrayList<Object> mAllNotifications = new ArrayList<Object>();
    public static ArrayList<Object> mAllNotifications_CustomerService = new ArrayList<Object>();
    
    // To store rewards advertisement details
    public static ArrayList<Object> mRewardsAdvertisements = new ArrayList<Object>();
    
    //To Store CustomerService details
    public static ArrayList<Object> mCustomerService = new ArrayList<Object>();
    
    // To Store Google Account Credentials
    public static ArrayList<Object> AccessTokenList = new ArrayList<Object>();
	public static ArrayList<Object> GMailUserDetails = new ArrayList<Object>();
	
	// To Store signupstep1 sendmobileverfication code
    public static ArrayList<Object> mSendMobileVerificationCode = new ArrayList<Object>();
    
    //TO store owner module employee list
    public static ArrayList<Object> StoreEmloyeesList = new ArrayList<Object>();
    
    //TO contact us reponse list
    public static ArrayList<Object> mContactUsResponseList = new ArrayList<Object>();
    
}	
