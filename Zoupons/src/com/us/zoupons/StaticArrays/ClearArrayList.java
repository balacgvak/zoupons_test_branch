package com.us.zoupons.StaticArrays;

import android.util.Log;

public class ClearArrayList {

	public static void cleararraylist(){
		try{
			WebServiceStaticArrays.mGetSecurityQuestionsEmailValidationList.clear();
			WebServiceStaticArrays.mGetSecurityQuestionsAndIds.clear();
			WebServiceStaticArrays.mSecurityQuestionsList.clear();
			WebServiceStaticArrays.mMobileCarriersList.clear();
			WebServiceStaticArrays.mSignUpList.clear(); 
			WebServiceStaticArrays.mActivateUser.clear(); 
			WebServiceStaticArrays.mForgotPasswordList.clear(); 
			WebServiceStaticArrays.mFirstDataGlobalPaymentList.clear();
			WebServiceStaticArrays.mLoginUserList.clear();
			WebServiceStaticArrays.mChangePasswordList.clear();
			WebServiceStaticArrays.mCategoriesList.clear();
			WebServiceStaticArrays.mSubCategoriesList.clear();
			WebServiceStaticArrays.mStoreCategoriesList.clear();
			WebServiceStaticArrays.mSearchStore.clear();
			WebServiceStaticArrays.mSearchGiftCard.clear();
			WebServiceStaticArrays.mSearchCoupon.clear();
			WebServiceStaticArrays.mStoresLocation.clear();
			WebServiceStaticArrays.mStoresLocator.clear();
			WebServiceStaticArrays.mDummyStoresLocator.clear();
			WebServiceStaticArrays.mStoresQRCode.clear();
			WebServiceStaticArrays.mStoresCheckFavorite.clear();
			WebServiceStaticArrays.mStoresAddFavorite.clear();
			WebServiceStaticArrays.mCardOnFiles.clear();
			WebServiceStaticArrays.mRemoveCard.clear();
			WebServiceStaticArrays.mStoreLocatorList.clear();
			WebServiceStaticArrays.mStaticStoreInfo.clear();
			WebServiceStaticArrays.mStorePhoto.clear();
			WebServiceStaticArrays.mStoreTiming.clear();
			WebServiceStaticArrays.mFullSizeStorePhotoUrl.clear();
			WebServiceStaticArrays.mFullSizeStorePhoto.clear();
			WebServiceStaticArrays.mStoreRegularCardDetails.clear();
			WebServiceStaticArrays.mStoreZouponsDealCardDetails.clear();	
			WebServiceStaticArrays.mSocialNetworkFriendList.clear();
			WebServiceStaticArrays.mStoreLikeList.clear();
			WebServiceStaticArrays.mVideoList.clear();
			WebServiceStaticArrays.mShareStore.clear();
			WebServiceStaticArrays.mShareCoupon.clear();
			WebServiceStaticArrays.mSearchedFriendList.clear();
			WebServiceStaticArrays.mStaticCouponsArrayList.clear();
			WebServiceStaticArrays.mFavouriteStoreDetails.clear();
			WebServiceStaticArrays.mFavouriteFriendStoreDetails.clear();
			WebServiceStaticArrays.categories.clear();
			WebServiceStaticArrays.mNotificationDetails.clear();
			WebServiceStaticArrays.mUserProfileList.clear();
			WebServiceStaticArrays.mUserSecurityQuestionsList.clear();
			WebServiceStaticArrays.mContactUsResponse.clear();
			WebServiceStaticArrays.mAllGiftCardList.clear();
			WebServiceStaticArrays.mStoreReviewsList.clear();
			WebServiceStaticArrays.GetReviewStatusList.clear();
			WebServiceStaticArrays.mGiftCardTransactionHistory.clear();
			WebServiceStaticArrays.mReceiptsDetails.clear();
			WebServiceStaticArrays.mSearchedReceiptDetails.clear();
			WebServiceStaticArrays.mOffsetFavouriteStores.clear();
			WebServiceStaticArrays.mOffsetStoreDetails.clear();
			WebServiceStaticArrays.mInvoiceArrayList.clear();
			WebServiceStaticArrays.mMyGiftCards.clear();
			WebServiceStaticArrays.mAllNotifications.clear();
			WebServiceStaticArrays.mAllNotifications_CustomerService.clear();
			WebServiceStaticArrays.mRewardsAdvertisements.clear();
			WebServiceStaticArrays.mCustomerService.clear();
			WebServiceStaticArrays.AccessTokenList.clear();
			WebServiceStaticArrays.GMailUserDetails.clear();
			WebServiceStaticArrays.mSendMobileVerificationCode.clear();
			WebServiceStaticArrays.StoreEmloyeesList.clear();
			WebServiceStaticArrays.mContactUsResponseList.clear();
			Log.i("ClearArrayList","Array List Cleared");
		}catch(NullPointerException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
