/**
 * 
 */
package com.us.zoupons.ClassVariables;

import android.graphics.Bitmap;

/**
 * @author jacob
 * Class Variables for search result
 */
public class Search_ClassVariables implements Cloneable{
	//Variable to get error message
	public String message="";		
	//Variable to get store search information
	public String storeId="",storeName="";	
	public Bitmap storeImage=null;
	//Variable to get giftcard search information
	public String giftCardId="",giftCardType="",giftCardPrivateUserId="",giftCardDescription="",giftCardStoreId="";
	public String giftCardFaceValue="",giftCardSellPrice="",giftCardCardType="",giftCardStatus="",giftCardAddedBy="";
	public String giftCardModifiedTime="",giftCardModifiedBy="",giftCardVisitorId="";
	//Variable to get coupon search information
	public String couponId="",couponType="",couponCustomerPhoneNumber="",couponTitle="",couponCouponCode="",couponCategory="";
	public String couponCompany="",couponBarcode="",couponStoreId="",couponActivationDate="",couponExpirationDate="";
	public String couponAddedDate="",couponDescription="",couponImage="",couponTags="",couponDiscountType="",couponAddedBy="";
	public String couponAddedVia="",couponVisitorId="",couponStatus="";
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
