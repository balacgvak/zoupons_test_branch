/**
 * 
 */
package com.us.zoupons.classvariables;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author zoupons
 * Class to hold data of store_locator webservice
 */
public class StoreLocator_ClassVariables implements Cloneable{
	public String id="",location_id="",storeName="",description="",shortDescription="",addressLine1="",addressLine2="",logoPath="";
	public String latitude="",longitude="",distance="0.0",deviceDistance="0.0",message="",totalcount="",category="",subcategory="",state="";
	public String zipcode="",city="",like_count="0",invoice_center_ordering="";
	public int storesortflag=0;
	public Bitmap storeImage=null;
	public LatLng storeCoordinates = null;

	//Variables to enable or disable sRightmenu depends on these variable values
	public String rightmenu_location_flag="",rightmenu_coupons_flag="",rightmenu_aboutus_flag="",rightmenu_reviewandrating_flag="",rightmenu_photos_flag="";
	public String rightmenu_contactstore_flag="",rightmenu_giftcards_flag="",rightmenu_videos_flag="",rightmenu_invoicecenter_flag="",rightmenu_communication_flag="";
	public String rightmenu_pointofsale_flag="",rightmenu_zcards_flag="",rightmenu_share_flag="",rightmenu_batchsales_flag="",rightmenu_refund_flag="",rightmenu_mobilepayment_flag="",rightmenu_rewardcards_flag="";
	
	public String favorite_store="";
	public String store_distance="0.0";
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	};
}
