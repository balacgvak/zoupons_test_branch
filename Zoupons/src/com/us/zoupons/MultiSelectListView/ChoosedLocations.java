/**
 * 
 */
package com.us.zoupons.MultiSelectListView;

/**
 * @author zoupons
 *
 */
public class ChoosedLocations {
	private String mChoosedLocations_Address;

    public  ChoosedLocations(String storeaddress)
    {
    	this.mChoosedLocations_Address=storeaddress;
    }

    public String getChoosedLocation_Address() {
        return mChoosedLocations_Address;
    }

}
