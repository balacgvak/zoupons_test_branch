package com.us.zoupons;

import java.util.Comparator;

import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;

public class FriendStoresLikeCountSortingClass implements Comparator<POJOStoreInfo>{

	@Override
	public int compare(POJOStoreInfo object1, POJOStoreInfo object2) {
		int likecountsort = Integer.valueOf(object1.like_count).compareTo(Integer.valueOf(object2.like_count));
		if (Integer.parseInt(object1.like_count) == Integer.parseInt(object2.like_count)){
			int likecountvalue = Integer.parseInt(object1.like_count);
			int distancecountsort=0;
			for(int i=0;i<WebServiceStaticArrays.mFavouriteFriendStoreDetails.size();i++){
				POJOStoreInfo obj = (POJOStoreInfo) WebServiceStaticArrays.mFavouriteFriendStoreDetails.get(i);
				if(Integer.parseInt(obj.like_count)==likecountvalue){
					distancecountsort = Double.valueOf(object1.store_distance).compareTo(Double.valueOf(object2.store_distance)) * (-1);
					return distancecountsort;
				}
			}
			return distancecountsort;
		}else{
			return likecountsort;
		}
	}
}
