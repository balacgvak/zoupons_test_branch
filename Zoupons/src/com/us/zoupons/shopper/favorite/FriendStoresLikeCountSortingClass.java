package com.us.zoupons.shopper.favorite;

import java.util.Comparator;

import com.us.zoupons.classvariables.POJOStoreInfo;

/**
 * Class to sort friends favorite store based upon Zoupons sorting order..
 */

public class FriendStoresLikeCountSortingClass implements Comparator<POJOStoreInfo>{

	@Override
	public int compare(POJOStoreInfo object1, POJOStoreInfo object2) {
		
		if(Integer.valueOf(object1.storesortflag) == Integer.valueOf(object2.storesortflag)){ // To sort within store Types [Preferred/unPreferred/Online]
    		if (Integer.valueOf(object1.like_count) == Integer.valueOf(object2.like_count)){ // If like count is Equal then check for least distance
    			if(Double.valueOf(object1.store_distance) == Double.valueOf(object2.store_distance)){
    				return 0;
    			}else if(Double.valueOf(object1.store_distance) < Double.valueOf(object2.store_distance)){
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
