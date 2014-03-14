package com.us.zoupons;

import java.util.Comparator;

import com.us.zoupons.classvariables.StoreLocator_ClassVariables;

/**
 * Class used to sort stores based upon distance from current location 
 */

public class DistanceSortingClass implements Comparator<StoreLocator_ClassVariables> {

	@Override
	public int compare(StoreLocator_ClassVariables store_locatorobject1, StoreLocator_ClassVariables store_locatorobject2) {
		return Double.valueOf(store_locatorobject1.deviceDistance).compareTo(Double.valueOf(store_locatorobject2.deviceDistance));
	}

}
