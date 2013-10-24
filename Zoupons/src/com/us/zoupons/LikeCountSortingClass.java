package com.us.zoupons;

import java.util.Comparator;

import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;

public class LikeCountSortingClass implements Comparator<StoreLocator_ClassVariables> {

	@Override
	public int compare(StoreLocator_ClassVariables object1, StoreLocator_ClassVariables object2) {
		int likecountsort = Integer.valueOf(object1.like_count).compareTo(Integer.valueOf(object2.like_count));
		if (Integer.parseInt(object1.like_count) == Integer.parseInt(object2.like_count)){
			int likecountvalue = Integer.parseInt(object1.like_count);
			int distancecountsort=0;
			for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
				StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
				if(Integer.parseInt(obj.like_count)==likecountvalue){
					distancecountsort = Double.valueOf(object1.deviceDistance).compareTo(Double.valueOf(object2.deviceDistance)) * (-1);
					return distancecountsort;
				}
			}
			return distancecountsort;
		}else{
			return likecountsort;
		}	
	}
}
