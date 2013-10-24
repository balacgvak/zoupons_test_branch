package com.us.zoupons.FlagClasses;

public class ZPayFlag {
	
	public static int mFlag=0;
	
	public static int setFlag(int flag){
		mFlag=flag;
		return mFlag;
	}
	
	public static int getFlag(){
		return mFlag;
	}

}
