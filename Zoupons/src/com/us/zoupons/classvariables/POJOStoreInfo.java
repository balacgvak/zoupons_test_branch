package com.us.zoupons.classvariables;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class POJOStoreInfo {
	public String store_id ="";
	public String store_name ="";
	public String short_description ="";
	public String description ="";
	public String first_name ="";	
	public String last_name ="";
	public String email ="";
	public String phone ="";
	public String fax ="";
	public String address_line1 ="";
	public String address_line2 ="";
	public String city ="";
	public String state ="";
	public String country ="";
	public String zip_code ="";
	public String website ="";
	public String qr_code ="";
	public String logo_path ="";
	public String video_title ="";
	public String video_url ="";
	public Bitmap video_thumbnail =null;
	public String store_type ="";
	public Bitmap store_image= null;
	public String total_count="";
	//declaration of location id
	public String location_id="";
	public String status="";
	public String message="";
	
	// Additional parameters
	public String store_category ="";
	public String store_subcategory ="";
	public String store_distance ="0.0";
	public String has_location ="";
	public String has_coupons ="";
	public String has_about_us ="";
	public String has_review_and_rating ="";
	public String has_photos ="";
	public String has_contact_store ="";
	public String has_giftcard ="";
	public String has_videos ="";
	public String has_invoicecenter ="";
	public String has_communication ="";
	public String has_point_of_sale ="";
	public String has_zcards ="";
	public String has_share ="";
	public String has_batchsales="";
	public String has_refund="";
	public String has_mobilepayment="";
	public String has_rewardcards="";
	
	public String mLatitude="";
	public String mLongitude="";	
	//public GeoPoint mGeoPoint=null;
	public LatLng mStoreCoordinates=null;
	
	// Bitmap url
	public String store_image_path= "";
	public String like_count="0";
	
	//Favorite Store tag
	public String favorite_store="";
	public int storesortflag=0;
}
