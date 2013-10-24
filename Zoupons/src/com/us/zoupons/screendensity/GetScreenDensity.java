/**
 * 
 */
package com.us.zoupons.screendensity;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Class to get screen density
 */
public class GetScreenDensity {

	Activity context;
	public GetScreenDensity(Activity context){
		this.context=context;
	}

	/*
	 * Function to get listview item image size depends on screen density
	 * */
	public int[] getListViewSize(){
		int density[]=new int[2];
		DisplayMetrics metrics = new DisplayMetrics();
		this.context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		switch(metrics.densityDpi){
		case DisplayMetrics.DENSITY_LOW:
			density[0]=38;
			density[1]=23;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			density[0]=50;
			density[1]=30;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			density[0]=75;
			density[1]=45;
			break;
		default:
			density[0] = 150;
			density[1] = 100;      
		}
		return density;
	}

	public int[] getListViewSize_MyGiftCards(){
		int density[]=new int[2];
		DisplayMetrics metrics = new DisplayMetrics();
		this.context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		switch(metrics.densityDpi){
		case DisplayMetrics.DENSITY_LOW:
			density[0]=53;
			density[1]=33;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			density[0]=70;
			density[1]=45;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			density[0]=105;
			density[1]=67;
			break;
		default:
			density[0] = 150;
			density[1] = 100;
		}
		return density;
	}
}
