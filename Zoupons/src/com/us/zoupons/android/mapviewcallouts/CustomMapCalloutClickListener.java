package com.us.zoupons.android.mapviewcallouts;

import java.math.BigDecimal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.Marker;
import com.us.zoupons.Location;
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.OpenMenu;
import com.us.zoupons.R;
import com.us.zoupons.SlidingView;
import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.FlagClasses.MenuBarFlag;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.cards.StoreGiftCardDetails;
import com.us.zoupons.zpay.Step2_ManageCards;
import com.us.zoupons.zpay.ZpayStep2SearchEnable;
import com.us.zoupons.zpay.zpay_step1;

public class CustomMapCalloutClickListener implements OnInfoWindowClickListener{

	private Context context;
	private String TAG = "MapView Callout Click",mPageFlag;
	
	
	// For Home page call out Tap
	public CustomMapCalloutClickListener(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	// For ZPay Step1 call out Tap
	public CustomMapCalloutClickListener(Context context,String PageFlag) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mPageFlag = PageFlag;
	}
	
	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		System.out.println("Index : "+marker.getId()+"\n"+"Item Title : "+marker.getTitle());
		if(!this.context.getClass().getSimpleName().equals("zpay_step1")){
			 if(MenuBarFlag.mMenuBarFlag!=0){
					switch (MenuBarFlag.mMenuBarFlag) {
					case 1:
						Log.i(TAG,"Store Image Load from Browse");
						for(int i=0;i<WebServiceStaticArrays.mDummyStoresLocator.size();i++){
							StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mDummyStoresLocator.get(i);
							if(parsedobjectvalues.storeName.equalsIgnoreCase(marker.getTitle()) && i == Integer.valueOf(marker.getSnippet().split(",,")[0])){
								RightMenuStoreId_ClassVariables.mStoreID=parsedobjectvalues.id;
								RightMenuStoreId_ClassVariables.mStoreName=parsedobjectvalues.storeName;
								// Asigning location id to class variables..
								RightMenuStoreId_ClassVariables.mLocationId=parsedobjectvalues.location_id;
								RightMenuStoreId_ClassVariables.rightmenu_favourite_status=parsedobjectvalues.favorite_store;
								
								RightMenuStoreId_ClassVariables.mSelectedStore_lat=parsedobjectvalues.latitude;
								RightMenuStoreId_ClassVariables.mSelectedStore_long=parsedobjectvalues.longitude;
								
								if(parsedobjectvalues.distance.equalsIgnoreCase("-1")){
									RightMenuStoreId_ClassVariables.mStoreLocation = "online";
								}else{
									RightMenuStoreId_ClassVariables.mStoreLocation = parsedobjectvalues.addressLine1 + "\n" + parsedobjectvalues.city+", "+parsedobjectvalues.state+" "+parsedobjectvalues.zipcode;	
								}
								SlidingView.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
								SlidingView.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);
								if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
									SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
								}else{
									SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
								}
								MenuUtilityClass.SetRightMenuStatus(parsedobjectvalues, context);
								break;
							}
						}
						break;
					case 2:
						Log.i(TAG,"Store Image Load from QRCode");
						for(int k=0;k<WebServiceStaticArrays.mStoresLocator.size();k++){
							StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mDummyStoresLocator.get(k);
							if(parsedobjectvalues.storeName.equalsIgnoreCase(marker.getTitle()) && k == Integer.valueOf(marker.getSnippet().split(",,")[0])){
								RightMenuStoreId_ClassVariables.mStoreID=parsedobjectvalues.id;
								RightMenuStoreId_ClassVariables.mStoreName=parsedobjectvalues.storeName;
								// Asigning location id to class variables..
								RightMenuStoreId_ClassVariables.mLocationId=parsedobjectvalues.location_id;
								
								RightMenuStoreId_ClassVariables.mSelectedStore_lat=parsedobjectvalues.latitude;
								RightMenuStoreId_ClassVariables.mSelectedStore_long=parsedobjectvalues.longitude;
								
								RightMenuStoreId_ClassVariables.rightmenu_favourite_status=parsedobjectvalues.favorite_store;
								if(parsedobjectvalues.distance.equalsIgnoreCase("-1")){
									RightMenuStoreId_ClassVariables.mStoreLocation = "online";
								}else{
									RightMenuStoreId_ClassVariables.mStoreLocation = parsedobjectvalues.addressLine1 + "\n" + parsedobjectvalues.city+", "+parsedobjectvalues.state+" "+parsedobjectvalues.zipcode;	
								}
								//UserId=UserDetails.mServiceUserId;
								SlidingView.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
								SlidingView.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);
								if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
									SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
								}else{
									SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
								}
								MenuUtilityClass.SetRightMenuStatus(parsedobjectvalues, context);
								break;
							}
						}
						break;
					case 3:
						Log.i(TAG,"Store Image Load from Search");
						for(int j=0;j<WebServiceStaticArrays.mDummyStoresLocator.size();j++){
							StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mDummyStoresLocator.get(j);
							if(parsedobjectvalues.storeName.equalsIgnoreCase(marker.getTitle()) && j == Integer.valueOf(marker.getSnippet().split(",,")[0])){
								Log.i(TAG,"Don't have image tag default android icon is loaded");
								RightMenuStoreId_ClassVariables.mStoreID=parsedobjectvalues.id;
								RightMenuStoreId_ClassVariables.mStoreName=parsedobjectvalues.storeName;
								// Asigning location id to class variables..
								RightMenuStoreId_ClassVariables.mLocationId=parsedobjectvalues.location_id;
								RightMenuStoreId_ClassVariables.rightmenu_favourite_status=parsedobjectvalues.favorite_store;
								
								RightMenuStoreId_ClassVariables.mSelectedStore_lat=parsedobjectvalues.latitude;
								RightMenuStoreId_ClassVariables.mSelectedStore_long=parsedobjectvalues.longitude;
								
								if(parsedobjectvalues.distance.equalsIgnoreCase("-1")){
									RightMenuStoreId_ClassVariables.mStoreLocation = "online";	
								}else{
									RightMenuStoreId_ClassVariables.mStoreLocation = parsedobjectvalues.addressLine1 + "\n" + parsedobjectvalues.city+", "+parsedobjectvalues.state+" "+parsedobjectvalues.zipcode;	
								}
								//UserId=UserDetails.mServiceUserId;
								SlidingView.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
								SlidingView.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);
								if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
									SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
								}else{
									SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
								}
								MenuUtilityClass.SetRightMenuStatus(parsedobjectvalues, context);
								break;
							}
						}
						break;
					case 4:
						Log.i(TAG,"Store Image Load from Current Location Nearest Store");
						for(int i=0;i<WebServiceStaticArrays.mDummyStoresLocator.size();i++){
							StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mDummyStoresLocator.get(i);
                             if(parsedobjectvalues.storeName.equalsIgnoreCase(marker.getTitle()) && i == Integer.valueOf(marker.getSnippet().split(",,")[0])){
								 
								RightMenuStoreId_ClassVariables.mStoreID=parsedobjectvalues.id;
								RightMenuStoreId_ClassVariables.mStoreName=parsedobjectvalues.storeName;
								// Asigning location id to class variables..
								RightMenuStoreId_ClassVariables.mLocationId=parsedobjectvalues.location_id;
								RightMenuStoreId_ClassVariables.rightmenu_favourite_status=parsedobjectvalues.favorite_store;
								
								RightMenuStoreId_ClassVariables.mSelectedStore_lat=parsedobjectvalues.latitude;
								RightMenuStoreId_ClassVariables.mSelectedStore_long=parsedobjectvalues.longitude;
								
								if(parsedobjectvalues.distance.equalsIgnoreCase("-1")){
									RightMenuStoreId_ClassVariables.mStoreLocation = "online";
								}else{
									RightMenuStoreId_ClassVariables.mStoreLocation = parsedobjectvalues.addressLine1 + "\n" + parsedobjectvalues.city+", "+parsedobjectvalues.state+" "+parsedobjectvalues.zipcode;	
								}
								//UserId=UserDetails.mServiceUserId;
								SlidingView.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
								SlidingView.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);
								if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
									SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
								}else{
									SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
								}
								MenuUtilityClass.SetRightMenuStatus(parsedobjectvalues, context);
								break;
							}
						}
						break;
					case 5:		//Condition true when tapping over Location mapview Shop callout.
						for(int i=0;i<WebServiceStaticArrays.mStoreLocatorList.size();i++){
							StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(i);
							if(parsedobjectvalues.storeName.equalsIgnoreCase(marker.getTitle()) && i == Integer.valueOf(marker.getSnippet().split(",,")[0])){
								RightMenuStoreId_ClassVariables.mStoreID=parsedobjectvalues.id;
								RightMenuStoreId_ClassVariables.mStoreName=parsedobjectvalues.storeName;
								RightMenuStoreId_ClassVariables.rightmenu_favourite_status=parsedobjectvalues.favorite_store;
								// Asigning location id to class variables..
								RightMenuStoreId_ClassVariables.mLocationId=parsedobjectvalues.location_id;
								
								RightMenuStoreId_ClassVariables.mSelectedStore_lat=parsedobjectvalues.latitude;
								RightMenuStoreId_ClassVariables.mSelectedStore_long=parsedobjectvalues.longitude;
								
								if(parsedobjectvalues.distance.equalsIgnoreCase("-1")){
									RightMenuStoreId_ClassVariables.mStoreLocation = "online"; 
								}else{
									RightMenuStoreId_ClassVariables.mStoreLocation = parsedobjectvalues.addressLine1 + "\n" + parsedobjectvalues.city+", "+parsedobjectvalues.state+" "+parsedobjectvalues.zipcode;	
								}
								//UserId=UserDetails.mServiceUserId;
								Location.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
								Location.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
								
								if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
									Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
								}else{
									Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
								}
								
								MenuUtilityClass.SetRightMenuStatus(parsedobjectvalues, context);
								OpenMenu.toOpenRightMenu("locations");
								break;
							}
						}
						break;
					default:
						break;
					}
				}
				if(MenuBarFlag.mMenuBarFlag<5&&MenuBarFlag.mMenuBarFlag!=0){
					/** menu out **/
					OpenMenu.toOpenRightMenu("SlidingView");		// Function to open RightMenu if control from home page
				}
		}else{
			//Toast.makeText(this.context, "ZPay Step2", Toast.LENGTH_SHORT).show();
			// To enable search text in step 2
			ZpayStep2SearchEnable.searchEnableFlag = true;
			// If favorite has tapped
			if(zpay_step1.mMapViewOnScrollViewFlag.equals("favorites")){
				for(int i=0;i<WebServiceStaticArrays.mFavouriteStoreDetails.size();i++){
					POJOStoreInfo parsedobjectvalues = (POJOStoreInfo) WebServiceStaticArrays.mFavouriteStoreDetails.get(i);
					if(parsedobjectvalues.store_name.equals(marker.getTitle())&&i == Integer.valueOf(marker.getSnippet().split(",,")[0])){
						RightMenuStoreId_ClassVariables.mStoreID=parsedobjectvalues.store_id;
						RightMenuStoreId_ClassVariables.mStoreName=parsedobjectvalues.store_name;
						// Asigning location id to class variables..
						RightMenuStoreId_ClassVariables.mLocationId=parsedobjectvalues.location_id;
						RightMenuStoreId_ClassVariables.mSelectedStore_lat=parsedobjectvalues.mLatitude;
						RightMenuStoreId_ClassVariables.mSelectedStore_long=parsedobjectvalues.mLongitude;
						
						if(parsedobjectvalues.store_distance.equalsIgnoreCase("-1")){
							RightMenuStoreId_ClassVariables.mStoreLocation = "online";
						}else{
							RightMenuStoreId_ClassVariables.mStoreLocation = parsedobjectvalues.address_line1 + "\n" + parsedobjectvalues.city+", "+parsedobjectvalues.state+" "+parsedobjectvalues.zip_code;		
						}
						
						if(mPageFlag.equalsIgnoreCase("giftcard")){
			    			if(parsedobjectvalues.has_giftcard.equalsIgnoreCase("yes")){
			    				//Move to Giftcards and zcards page
			    				Intent intent_giftcard_zcard = new Intent(this.context,StoreGiftCardDetails.class);
			    				intent_giftcard_zcard.putExtra("CardType", "Regular");
			    				intent_giftcard_zcard.putExtra("bothcards", "both");
			    				this.context.startActivity(intent_giftcard_zcard);
			    			}else{
			    				// alert shown
			    				alertBox_service("Information","This Store does't have giftcard.");
			    			}
			    		}else{
			    			if(parsedobjectvalues.has_point_of_sale.equalsIgnoreCase("yes")){
			    				//Move to step2
			    				Intent intent_step2 = new Intent(this.context,Step2_ManageCards.class);
			    				intent_step2.putExtra("datasourcename", "zpay");
			    				this.context.startActivity(intent_step2);
			    			}else{
			    				// alert shown
			    				alertBox_service("Information","This Store does't have payment option.");
			    			}
			    		}
					}
				}
			}else{
				for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
					StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
					if(parsedobjectvalues.storeName.equals(marker.getTitle())&&i == Integer.valueOf(marker.getSnippet().split(",,")[0])){
						RightMenuStoreId_ClassVariables.mStoreID=parsedobjectvalues.id;
						RightMenuStoreId_ClassVariables.mStoreName=parsedobjectvalues.storeName;
						RightMenuStoreId_ClassVariables.mLocationId=parsedobjectvalues.location_id;
						RightMenuStoreId_ClassVariables.mSelectedStore_lat=parsedobjectvalues.latitude;
						RightMenuStoreId_ClassVariables.mSelectedStore_long=parsedobjectvalues.longitude;
						if(parsedobjectvalues.distance.equalsIgnoreCase("-1")){
							RightMenuStoreId_ClassVariables.mStoreLocation = "online";
						}else{
							RightMenuStoreId_ClassVariables.mStoreLocation = parsedobjectvalues.addressLine1 + "\n" + parsedobjectvalues.city+", "+parsedobjectvalues.state+" "+parsedobjectvalues.zipcode;	
						}
						if(mPageFlag.equalsIgnoreCase("giftcard")){
			    			if(parsedobjectvalues.rightmenu_giftcards_flag.equalsIgnoreCase("yes")){
			    				//Move to Giftcards and zcards page
			    				Intent intent_giftcard_zcard = new Intent(this.context,StoreGiftCardDetails.class);
			    				intent_giftcard_zcard.putExtra("CardType", "Regular");
			    				intent_giftcard_zcard.putExtra("bothcards", "both");
			    				this.context.startActivity(intent_giftcard_zcard);
			    			}else{
			    				// alert shown
			    				alertBox_service("Information","This Store does't have giftcard.");
			    			}
			    		}else{
			    			if(parsedobjectvalues.rightmenu_pointofsale_flag.equalsIgnoreCase("yes")){
			    				//Move to step2
			    				Intent intent_step2 = new Intent(this.context,Step2_ManageCards.class);
			    				intent_step2.putExtra("datasourcename", "zpay");
			    				this.context.startActivity(intent_step2);
			    			}else{
			    				// alert shown
			    				alertBox_service("Information","This Store does't have payment option.");
			    			}
			    		}
					}
				}
			}
		}
	}
	
	
	private void alertBox_service(String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.context);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}
