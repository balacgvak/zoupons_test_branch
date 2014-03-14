package com.us.zoupons;

import java.util.Comparator;

import com.us.zoupons.classvariables.StoreLocator_ClassVariables;

/**
 * 
 * Class to sort preferred stores based upon like count
 *
 */

public class LikeCountSortingClass implements Comparator<StoreLocator_ClassVariables> {

	@Override
	public int compare(StoreLocator_ClassVariables object1, StoreLocator_ClassVariables object2) {

		if(Integer.valueOf(object1.storesortflag) == Integer.valueOf(object2.storesortflag)){
			if (Integer.valueOf(object1.like_count) == Integer.valueOf(object2.like_count)){ // To sort within store Types [Preferred/unPreferred/Online]
				if(Double.valueOf(object1.deviceDistance) == Double.valueOf(object2.deviceDistance)){// If like count is Equal then check for least distance
					return 0;
				}else if(Double.valueOf(object1.deviceDistance) < Double.valueOf(object2.deviceDistance)){
					return -1;
				}else{
					return 1;
				}
			}else if(Integer.valueOf(object1.like_count) < Integer.valueOf(object2.like_count)){
				return 1;
			}else{
				return -1;
			}
		}else{
			return 0;
		}

	}
}
