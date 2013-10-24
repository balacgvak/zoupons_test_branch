package com.us.zoupons;

import java.util.Comparator;

import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;

public class DistanceSortingClass implements Comparator<StoreLocator_ClassVariables> {

	@Override
	public int compare(StoreLocator_ClassVariables object1, StoreLocator_ClassVariables object2) {
		return Double.valueOf(object1.deviceDistance).compareTo(Double.valueOf(object2.deviceDistance));
	}

}
