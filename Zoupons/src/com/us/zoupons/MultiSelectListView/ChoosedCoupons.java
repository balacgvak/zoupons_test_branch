package com.us.zoupons.MultiSelectListView;

public class ChoosedCoupons {
	private String mChoosedCoupons;

    public ChoosedCoupons(String storeaddress)
    {
    	this.mChoosedCoupons=storeaddress;
    }

    public String getChoosedCoupons() {
        return mChoosedCoupons;
    }
}
