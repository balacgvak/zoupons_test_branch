/**
 * 
 */
package com.us.zoupons.android.AsyncThreadClasses;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.SlidingView;
import com.us.zoupons.ClassVariables.MapViewCenter;
import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;
import com.us.zoupons.FlagClasses.MenuBarFlag;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.zpay.zpay_step1;

/**
 * class to load store_locator data and update the store values in mapview
 */
public class StoreNearCurrentLocationAsyncThread extends AsyncTask<String, String, String>{

	Activity ctx;
	String TAG="StoreNearCurrentLocationAsyncThread";
	private ProgressDialog progressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	//MapViewItemizedOverlay mItemizedOverlay;
	//TapControlledMapView mMapView;
	GoogleMap mGoogleMap;
	//LatLng animateGeoPoint,singleStore;
	ListView mShopListView;
	int storeCount;
	double mMapViewRadius;
	String mClassName;
	String mPageFlag;

	String mProgressStatus="";
	View mFoooterView;
	LatLng mGeopointDuringCall = null;
	private boolean mIsTaskRepeated,mShouldFreezeViewGone;
	String mDeviceLat="",mDeviceLong="";

	public StoreNearCurrentLocationAsyncThread(Activity context,GoogleMap mGoogleMap,ListView shoplistview,double mapviewradius,String classname,View footer,String progressstatus,String pageflag,String devicelat,String devicelong, boolean shouldFreezeViewGone){
		this.ctx=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(false);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		this.mGoogleMap=mGoogleMap;
		this.mShopListView=shoplistview;
		//this.animateGeoPoint=geopoint;
		this.mMapViewRadius=mapviewradius;
		this.mClassName=classname;
		Log.i(TAG,"MapView Radius : "+this.mMapViewRadius);
		this.mDeviceLat=devicelat;
		this.mDeviceLong=devicelong;
		SlidingView.mIsLoadMore = true;
		zpay_step1.mIsZPAYLoadMore = true;
		this.mProgressStatus = progressstatus;
		this.mFoooterView = footer;
		this.mPageFlag = pageflag;
		// For differentiating repeat webservice call during map scroll..
		mGeopointDuringCall = MapViewCenter.mMapViewCenter_GeoPoint;
		mShouldFreezeViewGone = shouldFreezeViewGone;
		if(mShopListView.getVisibility() == View.VISIBLE){
			Log.i("shop list view", "visible");
		}else{
			Log.i("shop list view", "not visible");
		}
	} 

	@Override
	protected String doInBackground(String... params) {
		Log.i(TAG, "do in background");
		MenuBarFlag.mMenuBarFlag=4;
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				if(!isCancelled()){	
					if(this.mClassName.equalsIgnoreCase("SlidingView")){
						//if(SlidingView.mStoreDetailsStart.equalsIgnoreCase("0")){
							WebServiceStaticArrays.mStoresLocator.clear();
						//}
						if(!isCancelled()){
							mGetResponse=zouponswebservice.store_locator(params[0],params[1],"","","",this.mMapViewRadius,"",SlidingView.mStoreDetailsStart,mDeviceLat,mDeviceLong);
						}else{
							progressdialog.dismiss();
						}
					}else if(this.mClassName.equalsIgnoreCase("giftcard")){
						if(zpay_step1.mZPAYStoreDetailsStart.equalsIgnoreCase("0")){
							WebServiceStaticArrays.mStoresLocator.clear();
						}
						if(!isCancelled()){
							mGetResponse=zouponswebservice.store_locator(params[0],params[1],"","","",this.mMapViewRadius,"giftcard",zpay_step1.mZPAYStoreDetailsStart,mDeviceLat,mDeviceLong);
						}else{
							progressdialog.dismiss();
						}
					}else if(this.mClassName.equalsIgnoreCase("zpay")){
						if(zpay_step1.mZPAYStoreDetailsStart.equalsIgnoreCase("0")){
							WebServiceStaticArrays.mStoresLocator.clear();
						}
						if(!isCancelled()){
							mGetResponse=zouponswebservice.store_locator(params[0],params[1],"","","",this.mMapViewRadius,"zpay",zpay_step1.mZPAYStoreDetailsStart,mDeviceLat,mDeviceLong);
						}else{
							progressdialog.dismiss();
						}
					}

					if(!mGetResponse.equals("")){
						if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
							if(!isCancelled()){
								mParsingResponse=parsingclass.parseStore_Locator(mGetResponse,this.mClassName);
								if(mParsingResponse.equalsIgnoreCase("success")){
									Log.i(TAG,"SearchStore List Size: "+WebServiceStaticArrays.mStoresLocator.size());
									for(int i=0;i<WebServiceStaticArrays.mOffsetStoreDetails.size();i++){
										if(!isCancelled()){
											final StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
											if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("Search type or term not available !")*/){
												//Log.i(TAG,"message : "+parsedobjectvalues.message);
												result ="success";
											}else{
												//Log.i(TAG,"storeName : "+parsedobjectvalues.storeName);
												//Log.i(TAG,"addressLine1 : "+parsedobjectvalues.addressLine1);
												//Log.i(TAG,"addressLine2 : "+parsedobjectvalues.addressLine2);
												result="success";
											}
										}else{
											progressdialog.dismiss();
										}
									}
								}else if(mParsingResponse.equalsIgnoreCase("failure")){
									Log.i(TAG,"Error");
									result="Response Error.";
								}else if(mParsingResponse.equalsIgnoreCase("norecords")){
									Log.i(TAG,"No Records");
									result="no data";
								}
								}else{
								progressdialog.dismiss();
							}
						}else{
							result="Response Error.";
						}
					}else{
						result="no data";
					}
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
	protected void onCancelled(String result) {
		// TODO Auto-generated method stub
		super.onCancelled(result);
		if(progressdialog.isShowing())
		 progressdialog.dismiss();
	}


	@Override
	protected void onPostExecute(String result) {
		Log.i(TAG, "on post execute");
		SlidingView.mIsLoadMore = false;
		zpay_step1.mIsZPAYLoadMore = false;
		
		if(this.mClassName.equalsIgnoreCase("SlidingView")){
			SlidingView.mCurrentLocation.setEnabled(true);
		}else{
			zpay_step1.mCurrentLocation.setEnabled(true);
		}
		
		progressdialog.dismiss();
		
		// check for repeat webservice call during map scroll..
		//Log.i("check LntLng latitudes", mGeopointDuringCall.latitude + " " + MapViewCenter.mMapViewCenter_GeoPoint.latitude);
		if(mGeopointDuringCall != null && mGeopointDuringCall.latitude == MapViewCenter.mMapViewCenter_GeoPoint.latitude){
			Log.i("repeat call", "false");
			mIsTaskRepeated = false;
		}else{
			mIsTaskRepeated = true;
			Log.i("repeat call", "true");
		}
		if(mClassName.equalsIgnoreCase("SlidingView")){
			mGoogleMap.setOnCameraChangeListener((SlidingView)ctx);	
		}else{
			mGoogleMap.setOnCameraChangeListener((zpay_step1)ctx);
		}
		
		if(result.equals("nonetwork") && !mIsTaskRepeated){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
		}else if(result.equals("no data") && !mIsTaskRepeated){
			Toast.makeText(ctx, "No Stores Available", Toast.LENGTH_SHORT).show();
		}else if(result.equals("success") && !mIsTaskRepeated){
			try{
				//Load Current Location while activity loading
				if(this.mConnectionAvailabilityChecking.ConnectivityCheck(this.ctx)){
					Log.i("size check", WebServiceStaticArrays.mOffsetStoreDetails.size() + "");
					if(mClassName.equalsIgnoreCase("SlidingView")){
						mGoogleMap.clear();
						Log.i(TAG,"WebServiceStaticArrays.mStoresLocator Size : "+WebServiceStaticArrays.mStoresLocator.size());
						ctx.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Log.i(TAG,"WebServiceStaticArrays.mOffsetStoreDetails Size : "+WebServiceStaticArrays.mOffsetStoreDetails.size());
								WebServiceStaticArrays.mStoresLocator.clear();
								for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
									StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(SlidingView.mStoreDetailsStart)+i,mStoreinfo);
								}
							}
						});
						if(SlidingView.mStoreDetailsStart.equalsIgnoreCase("0")){
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromCurrentLocation",false,mPageFlag); // false for adding fresh adapter
						}else{
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromCurrentLocation",true,mPageFlag); // true for refreshing adapter
						}
						if(mFoooterView != null && mShopListView.getFooterViewsCount() !=0 &&mShopListView.getAdapter() != null){
							Log.i(TAG, "Remove Footer View From Coupon List");
							mShopListView.removeFooterView(mFoooterView);
						}
						/*if(Integer.parseInt(SlidingView.mStoreDetailsCount)>20){
							SlidingView.mStoreDetailsStart = SlidingView.mStoreDetailsEndLimit;
							SlidingView.mStoreDetailsEndLimit = String.valueOf(Integer.parseInt(SlidingView.mStoreDetailsEndLimit)+20);
						}*/
						if(SlidingView.mMapViewOnScrollViewFlag.equalsIgnoreCase("qrcode")){
							SlidingView.mMapViewOnScrollViewFlag ="currentlocation";
							SlidingView.mLocationGeoCordinates = new LatLng(Double.parseDouble(SlidingView.mDeviceCurrentLocationLatitude),Double.parseDouble(SlidingView.mDeviceCurrentLocationLongitude));
							mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(SlidingView.mLocationGeoCordinates));
						}
					}else{
						mGoogleMap.clear();

						ctx.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								WebServiceStaticArrays.mStoresLocator.clear();
								for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
									StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(zpay_step1.mZPAYStoreDetailsStart)+i,mStoreinfo);
								}
							}
						});

						if(zpay_step1.mZPAYStoreDetailsStart.equalsIgnoreCase("0")){
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromCurrentLocation",false,mPageFlag); // false for adding fresh adapter
						}else{
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromCurrentLocation",true,mPageFlag); // true for refreshing adapter
						}

						if(mFoooterView != null && mShopListView.getFooterViewsCount() !=0 &&mShopListView.getAdapter() != null){
							Log.i(TAG, "Remove Footer View From Coupon List");
							mShopListView.removeFooterView(mFoooterView);
						}
						/*if(Integer.parseInt(zpay_step1.mZPAYStoreDetailsCount)>20){
							zpay_step1.mZPAYStoreDetailsStart= zpay_step1.mZPAYStoreDetailsEndLimit;
							zpay_step1.mZPAYStoreDetailsEndLimit = String.valueOf(Integer.parseInt(zpay_step1.mZPAYStoreDetailsEndLimit)+20);
						}*/
					}

					//OverlayItem overlayItem;
					ArrayList<Double> minDistance = new ArrayList<Double>();
					minDistance.clear();
					for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
						if(obj.storeCoordinates!=null){
							storeCount=storeCount+1;
							//Log.i("Adding marker", storeCount+" ");
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

							//singleStore=obj.geoPoint;
							/*OverlayItem overlayItem = new OverlayItem(obj.geoPoint, obj.storeName, obj.addressLine1+"\n"+obj.addressLine2);
			        		// for differentiating preffered and un preferred stores
			        		if(obj.rightmenu_pointofsale_flag.equalsIgnoreCase("yes")){
			        			Drawable icon = ctx.getResources().getDrawable(R.drawable.ant3);
			        			int width = icon.getIntrinsicWidth();
			        			int height = icon.getIntrinsicHeight();
			        			icon.setBounds(-width / 2, -height, width / 2, 0);
			        			overlayItem.setMarker(icon);
			        			Log.i("store", "preffered added");
			        		}else{
			        			Drawable icon = ctx.getResources().getDrawable(R.drawable.dot);
			        			int width = icon.getIntrinsicWidth();
			        			int height = icon.getIntrinsicHeight();
			        			icon.setBounds(-width / 2, -height, width / 2, 0);
			        			overlayItem.setMarker(icon);
			        			Log.i("store", "not preffered added");
			        		}
			        		this.mItemizedOverlay.setCallOutBottomOffset(25);
		        			this.mItemizedOverlay.addOverlay(overlayItem);*/

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
								//********************************
								//animateGeoPoint=obj1.geoPoint;
								//**************************************
								Log.i(TAG,"Min Distance: "+obj1.distance);
								//Log.i(TAG,"Min GeoPoint: "+obj1.geoPoint);
							}
						}
					}

					Log.i(TAG,"StoreCount: "+storeCount);
					if(storeCount>1){
						//here we have to calculate and get nearest store from our current location to shown it in center of the mapview

						/////////////////////////////////////////////////
						/*MapOverlays.mapOverlays.clear();
			        	MapOverlays.mapOverlays.add(mItemizedOverlay);
			        	mMapView.invalidate();*/
						/////////////////////////////////////////////////
					}else if(storeCount==1){

						///////////////////////////////////////////////////
						/*MapOverlays.mapOverlays.clear();
			        	MapOverlays.mapOverlays.add(mItemizedOverlay);
			        	mMapView.invalidate();*/
						///////////////////////////////////////////
					}else if(storeCount==0){
						Toast.makeText(ctx, "No Stores Available", Toast.LENGTH_SHORT).show();
						///////////////////////////////////////////////////
						/*Toast.makeText(ctx, "No Stores Available", Toast.LENGTH_SHORT).show();
			        	MapOverlays.mapOverlays.clear();
			        	mMapView.invalidate();*/
						////////////////////////////////////////////////////
					}
					///////////////////////////////////////////////
					/* // If current location overlay cleared again add to it
			        mMapView.getOverlays().add(SlidingView.locationoverlay);
			        mMapView.postInvalidate();*/
					////////////////////////////////////////////////
				}else{ 
					Toast.makeText(this.ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				Log.i(TAG,"Issue occur while callout loading.");
				e.printStackTrace();
			}
		}
		
		if(this.ctx.getClass().getSimpleName().equalsIgnoreCase("SlidingView") && mShouldFreezeViewGone){
			SlidingView.mHomePageFreezeView.setVisibility(View.GONE);
		}else if(this.ctx.getClass().getSimpleName().equalsIgnoreCase("zpay_step1") && mShouldFreezeViewGone ){
			zpay_step1.mZpayStep1IntialFreezeView.setVisibility(View.GONE);
		}

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

		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			progressdialog.setMessage("Loading Stores...");
			progressdialog.setCancelable(false);
			progressdialog.show();
			
		}
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}
}