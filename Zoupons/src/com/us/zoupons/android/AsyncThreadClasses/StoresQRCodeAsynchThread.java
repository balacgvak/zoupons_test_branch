/**
 * 
 */
package com.us.zoupons.android.AsyncThreadClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.OpenMenu;
import com.us.zoupons.R;
import com.us.zoupons.SlidingView;
import com.us.zoupons.ClassVariables.CurrentLocation;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.cards.StoreGiftCardDetails;
import com.us.zoupons.zpay.Step2_ManageCards;
import com.us.zoupons.zpay.zpay_step1;

/**
 * Class to load qrcode store values from webservice
 *
 */
public class StoresQRCodeAsynchThread extends AsyncTask<String, String, String>{

	Activity ctx;
	String TAG="StoresLocationAsynchThread";
	private ProgressDialog progressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	private GoogleMap mGoogleMap;
	LatLng singleStore;
	ListView mShopListView;
	int storeCount;
	double mMapViewRadius;
	String mClassName;
	String mPageFlag;
	String mDeviceLatitude,mDeviceLongitude;
	
	public StoresQRCodeAsynchThread(Activity context,GoogleMap googleMap,ListView shoplistview,double mapviewradius,String classname,String pageflag,String devicelat,String devicelong){
		this.ctx=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		this.mGoogleMap = googleMap;
		this.mShopListView=shoplistview;
		this.mMapViewRadius=mapviewradius;
		this.mClassName=classname;
		this.mPageFlag=pageflag;
		this.mDeviceLatitude=devicelat;
		this.mDeviceLongitude=devicelong;
	}

	@Override
	protected String doInBackground(String... params) {

		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){

				if(SlidingView.mStoreDetailsStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.mStoresLocator.clear();
				}else if(zpay_step1.mZPAYStoreDetailsStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.mStoresLocator.clear();
				}

				mGetResponse=zouponswebservice.store_locator(CurrentLocation.CurrentLocation_Latitude, CurrentLocation.CurrentLocation_Longitude, "", "", params[0],this.mMapViewRadius,"","0",mDeviceLatitude,mDeviceLongitude);	//new
				
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=parsingclass.parseStore_Locator(mGetResponse,this.mClassName);
					if(mParsingResponse.equalsIgnoreCase("success")){
						Log.i(TAG,"QRCodeStore List Size: "+WebServiceStaticArrays.mStoresLocator.size());
						for(int i=0;i<WebServiceStaticArrays.mOffsetStoreDetails.size();i++){
							StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
							if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("Search type or term not available !")*/){
								Log.i(TAG,"message : "+parsedobjectvalues.message);
								result="No Stores Available.";
							}else{
								Log.i(TAG,"storeName : "+parsedobjectvalues.storeName);
								Log.i(TAG,"addressLine1 : "+parsedobjectvalues.addressLine1);
								Log.i(TAG,"addressLine2 : "+parsedobjectvalues.addressLine2);
								result="success";
							}
						}
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						Log.i(TAG,"Error");
						result="Response Error.";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						Log.i(TAG,"No Records");
						result="No Stores Available.";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			Log.i(TAG,"Thread Error");
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		Log.i(TAG,"Result QrCode Service: "+result);
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", result);
		}else if(result.equals("No Stores Available.")){
			alertBox_service("Information", result);
		}else if(result.equals("success")){
			try{
				//Load Current Location while activity loading
				if(this.mConnectionAvailabilityChecking.ConnectivityCheck(this.ctx)){
					mGoogleMap.clear();
					if(mClassName.equalsIgnoreCase("SlidingView")){
						ctx.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
									StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(SlidingView.mStoreDetailsStart)+i,mStoreinfo);
								}	
							}
						});
					}else if(mClassName.equalsIgnoreCase("zpay_step1")){
						ctx.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
									StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(zpay_step1.mZPAYStoreDetailsStart)+i,mStoreinfo);
								}	
							}
						});
					}

					/*//OverlayItem overlayItem;
					ArrayList<Double> minDistance = new ArrayList<Double>();
					minDistance.clear();
					for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
						if(obj.storeCoordinates!=null){
							storeCount=storeCount+1;
							singleStore=obj.storeCoordinates;
							MarkerOptions mGoogleMapMarkerOptions =  new MarkerOptions();
			        		mGoogleMapMarkerOptions.position(obj.storeCoordinates);
			        		mGoogleMapMarkerOptions.title(obj.storeName);
			        		// Adding position to marker to differentiate while tapping marker
			        		mGoogleMapMarkerOptions.snippet(String.valueOf(i)+",,"+obj.addressLine1);
			        		// for differentiating preffered and un preferred stores
			        		if(obj.rightmenu_pointofsale_flag.equalsIgnoreCase("yes")){
			        			BitmapDescriptor marker_icon= BitmapDescriptorFactory.fromResource(R.drawable.ant3);
			        			mGoogleMapMarkerOptions.icon(marker_icon);
			        		}else{
			        			BitmapDescriptor marker_icon= BitmapDescriptorFactory.fromResource(R.drawable.dot);
			        			mGoogleMapMarkerOptions.icon(marker_icon);
			        		}
			        		mGoogleMap.addMarker(mGoogleMapMarkerOptions);
							//OverlayItem overlayItem = new OverlayItem(obj.geoPoint, obj.storeName, obj.addressLine1+"\n"+obj.addressLine2);
							if(obj.rightmenu_pointofsale_flag.equalsIgnoreCase("yes")){
								Drawable icon = ctx.getResources().getDrawable(R.drawable.ant3);
								int width = icon.getIntrinsicWidth();
								int height = icon.getIntrinsicHeight();
								icon.setBounds(-width / 2, -height, width / 2, 0);
								//overlayItem.setMarker(icon);
								Log.i("store", "preffered added");
							}else{
								Drawable icon = ctx.getResources().getDrawable(R.drawable.dot);
								int width = icon.getIntrinsicWidth();
								int height = icon.getIntrinsicHeight();
								icon.setBounds(-width / 2, -height, width / 2, 0);
								//overlayItem.setMarker(icon);
								Log.i("store", "not preffered added");
							}
							this.mItemizedOverlay.setCallOutBottomOffset(8);
							//this.mItemizedOverlay.addOverlay(overlayItem);
							minDistance.add(Double.parseDouble(obj.deviceDistance));
						}else{
							Log.i(TAG,"No GeoPoint StoreName: "+obj.storeName);
						}
					}

					//To get least distance of store geopoint from current location to animate
					if(minDistance.size()>0&&minDistance!=null){
						Object object = Collections.min(minDistance);
						Log.i(TAG,"MinDistance: "+object);

						for(int j=0;j<WebServiceStaticArrays.mStoresLocator.size();j++){
							StoreLocator_ClassVariables obj1 = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(j);
							if(obj1.distance.equalsIgnoreCase(String.valueOf(object))){
								//this.animateGeoPoint=obj1.geoPoint;
								//Log.i(TAG,"Min Distance: "+obj1.distance);
								//Log.i(TAG,"Min GeoPoint: "+obj1.geoPoint);
							}
						}
					}

					Log.i(TAG,"StoreCount: "+storeCount);*/
					/*if(storeCount>1){
						//here we have to calculate and get nearest store from our current location to shown it in center of the mapview
						MapOverlays.mapOverlays.clear();
						MapOverlays.mapOverlays.add(mItemizedOverlay);
						mMapView.invalidate();

						StoreLocator_ClassVariables obj1 = null;
						if(WebServiceStaticArrays.mStoresLocator.size()>0){
							obj1 = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(0);
						}

						if(mClassName.equals("SlidingView")){
							//Check Right Menu Enable or disable status
							RightMenuStoreId_ClassVariables.mStoreID=obj1.id;
							RightMenuStoreId_ClassVariables.mStoreName=obj1.storeName;
							RightMenuStoreId_ClassVariables.mLocationId=obj1.location_id;
							RightMenuStoreId_ClassVariables.rightmenu_favourite_status = obj1.favorite_store;
							if(obj1.distance.equalsIgnoreCase("-1")){
								RightMenuStoreId_ClassVariables.mStoreLocation = "online";
							}else{
								RightMenuStoreId_ClassVariables.mStoreLocation = obj1.addressLine1 + "\n" + obj1.city+", "+obj1.state+" "+obj1.zipcode;	
							}

							if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
								SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
							}else{
								SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
							}

							MenuUtilityClass.SetRightMenuStatus(obj1, this.ctx);
							OpenMenu.toOpenRightMenu("SlidingView");		// Function to open RightMenu
						}else{
							ZpayStep2SearchEnable.searchEnableFlag = true;
							//To check this store has giftcards
							if(mPageFlag.equalsIgnoreCase("giftcard")){
								if(obj1.rightmenu_giftcards_flag.equalsIgnoreCase("yes")){
									//Move to Giftcards and zcards page
									Intent intent_giftcard_zcard = new Intent(this.ctx,StoreGiftCardDetails.class);
									intent_giftcard_zcard.putExtra("CardType", "Regular");
									intent_giftcard_zcard.putExtra("bothcards", "both");
									this.ctx.startActivity(intent_giftcard_zcard);
								}else{
									// alert shown
									alertBox_service("Information","This Store does't have giftcard.");
								}
							}else{
								if(obj1.rightmenu_pointofsale_flag.equalsIgnoreCase("yes")){
									//Move to step2
									Intent intent_step2 = new Intent(this.ctx,Step2_ManageCards.class);
									intent_step2.putExtra("datasourcename", "zpay");
									this.ctx.startActivity(intent_step2);
								}else{
									// alert shown
									alertBox_service("Information","This Store does't have payment option.");
								}
							}
						}
					}else*/ 
					if(WebServiceStaticArrays.mStoresLocator.size()==1){
						mGoogleMap.clear();
						StoreLocator_ClassVariables store_obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(0);
						if(store_obj.storeCoordinates!=null){
							MarkerOptions mGoogleMapMarkerOptions =  new MarkerOptions();
			        		mGoogleMapMarkerOptions.position(store_obj.storeCoordinates);
			        		mGoogleMapMarkerOptions.title(store_obj.storeName);
			        		// Adding position to marker to differentiate while tapping marker
			        		mGoogleMapMarkerOptions.snippet(String.valueOf(0)+",,"+store_obj.addressLine1);
			        		// for differentiating preffered and un preferred stores
			        		if(store_obj.rightmenu_pointofsale_flag.equalsIgnoreCase("yes")){
			        			BitmapDescriptor marker_icon= BitmapDescriptorFactory.fromResource(R.drawable.ant3);
			        			mGoogleMapMarkerOptions.icon(marker_icon);
			        		}else{
			        			BitmapDescriptor marker_icon= BitmapDescriptorFactory.fromResource(R.drawable.dot);
			        			mGoogleMapMarkerOptions.icon(marker_icon);
			        		}
			        		mGoogleMap.addMarker(mGoogleMapMarkerOptions); 
			        		CameraPosition StoreLocation = new CameraPosition.Builder().target(store_obj.storeCoordinates).zoom(13).build();
			        		mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(StoreLocation));
						}else{
							
						}

						StoreLocator_ClassVariables obj1 = null;
						if(WebServiceStaticArrays.mStoresLocator.size()>0){
							obj1 = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(0);
						}

						if(mClassName.equals("SlidingView")){
							//Check Right Menu Enable or disable status
							RightMenuStoreId_ClassVariables.mStoreID=obj1.id;
							RightMenuStoreId_ClassVariables.mStoreName=obj1.storeName;
							RightMenuStoreId_ClassVariables.mLocationId=obj1.location_id;
							RightMenuStoreId_ClassVariables.rightmenu_favourite_status=obj1.favorite_store;
							if(obj1.distance.equalsIgnoreCase("-1")){
								RightMenuStoreId_ClassVariables.mStoreLocation = "online";
							}else{
								RightMenuStoreId_ClassVariables.mStoreLocation = obj1.addressLine1 + "\n" + obj1.city+", "+obj1.state+" "+obj1.zipcode;	
							}
							if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
								SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
							}else{
								SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
							}
							MenuUtilityClass.SetRightMenuStatus(obj1, this.ctx);
							OpenMenu.toOpenRightMenu("SlidingView");		// Function to open RightMenu
						}else{
							//To check this store has giftcards
							if(mPageFlag.equalsIgnoreCase("giftcard")){
								if(obj1.rightmenu_giftcards_flag.equalsIgnoreCase("yes")){
									//Move to Giftcards and zcards page
									Intent intent_giftcard_zcard = new Intent(this.ctx,StoreGiftCardDetails.class);
									intent_giftcard_zcard.putExtra("CardType", "Regular");
									intent_giftcard_zcard.putExtra("bothcards", "both");
									this.ctx.startActivity(intent_giftcard_zcard);
								}else{
									// alert shown
									alertBox_service("Information","This Store does't have giftcard.");
								}
							}else{
								if(obj1.rightmenu_pointofsale_flag.equalsIgnoreCase("yes")){
									//Move to step2
									Intent intent_step2 = new Intent(this.ctx,Step2_ManageCards.class);
									intent_step2.putExtra("datasourcename", "zpay");
									this.ctx.startActivity(intent_step2);
								}else{
									// alert shown
									alertBox_service("Information","This Store does't have payment option.");
								}
							}
						}
					}else if(WebServiceStaticArrays.mStoresLocator.size()==0){
						alertBox_service("Information", "No Stores Available.");
						mGoogleMap.clear();
						/*MapOverlays.mapOverlays.clear();
						mMapView.invalidate();*/
					}
				}else{
					Toast.makeText(this.ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
				}
				/*// If current location overlay cleared again add to it
				mMapView.getOverlays().add(SlidingView.locationoverlay);
				mMapView.postInvalidate();*/
				MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromQRcode",false,mPageFlag);	//new
			}catch(Exception e){
				Log.i(TAG,"Issue occur while callout loading.");
				e.printStackTrace();
			}
		}
		progressdialog.dismiss();

		//Clone original storelocator arraylist to dummy store locator arraylist to fix the issue while tapping callout
		WebServiceStaticArrays.mDummyStoresLocator.clear();
		for(StoreLocator_ClassVariables obj : WebServiceStaticArrays.mStoresLocator){
			try {
				WebServiceStaticArrays.mDummyStoresLocator.add(obj.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
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