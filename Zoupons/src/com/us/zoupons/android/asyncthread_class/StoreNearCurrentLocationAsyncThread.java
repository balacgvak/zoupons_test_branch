/**
 * 
 */
package com.us.zoupons.android.asyncthread_class;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.us.zoupons.classvariables.MapViewCenter;
import com.us.zoupons.classvariables.StoreLocator_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.MenuBarFlag;
import com.us.zoupons.mobilepay.CardPurchase;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * class to load store_locator data and update the store values in mapview
 */
public class StoreNearCurrentLocationAsyncThread extends AsyncTask<String, String, String>{

	private Activity ctx;
	private ProgressDialog progressdialog=null;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse;
	//MapViewItemizedOverlay mItemizedOverlay;
	//TapControlledMapView mMapView;
	private GoogleMap mGoogleMap;
	//LatLng animateGeoPoint,singleStore;
	private ListView mShopListView;
	private int storeCount;
	private double mMapViewRadius;
	private String mClassName;
	private String mPageFlag;
	private String mProgressStatus="";
	private View mFoooterView;
	private LatLng mGeopointDuringCall = null;
	private boolean mIsTaskRepeated,mShouldFreezeViewGone;
	private String mDeviceLat="",mDeviceLong="";
	private Button mCurrentLocation;

	public StoreNearCurrentLocationAsyncThread(Activity context,GoogleMap mGoogleMap,ListView shoplistview,Button currentLocation,double mapviewradius,String classname,View footer,String progressstatus,String pageflag,String devicelat,String devicelong, boolean shouldFreezeViewGone){
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
		this.mDeviceLat=devicelat;
		this.mDeviceLong=devicelong;
		ShopperHomePage.sIsLoadMore = true;
		CardPurchase.mIsZPAYLoadMore = true;
		this.mProgressStatus = progressstatus;
		this.mFoooterView = footer;
		this.mPageFlag = pageflag;
		this.mCurrentLocation = currentLocation;
		// For differentiating repeat webservice call during map scroll..
		mGeopointDuringCall = MapViewCenter.mMapViewCenter_GeoPoint;
		mShouldFreezeViewGone = shouldFreezeViewGone;

	} 

	@Override
	protected String doInBackground(String... params) {
		MenuBarFlag.mMenuBarFlag=4;
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				if(!isCancelled()){	
					if(this.mClassName.equalsIgnoreCase("ShopperHomePage")){
						//if(ShopperHomePage.mStoreDetailsStart.equalsIgnoreCase("0")){
						//WebServiceStaticArrays.mStoresLocator.clear();
						//}
						if(!isCancelled()){
							mGetResponse=zouponswebservice.store_locator(params[0],params[1],"","","",this.mMapViewRadius,"",ShopperHomePage.sStoreDetailsStart,mDeviceLat,mDeviceLong);
						}else{
							progressdialog.dismiss();
						}
					}else if(this.mClassName.equalsIgnoreCase("giftcard")){
						if(CardPurchase.mZPAYStoreDetailsStart.equalsIgnoreCase("0")){
							//WebServiceStaticArrays.mStoresLocator.clear();
						}
						if(!isCancelled()){
							mGetResponse=zouponswebservice.store_locator(params[0],params[1],"","","",this.mMapViewRadius,"giftcard",CardPurchase.mZPAYStoreDetailsStart,mDeviceLat,mDeviceLong);
						}else{
							progressdialog.dismiss();
						}
					}else if(this.mClassName.equalsIgnoreCase("zpay")){
						if(CardPurchase.mZPAYStoreDetailsStart.equalsIgnoreCase("0")){
							//WebServiceStaticArrays.mStoresLocator.clear();
						}
						if(!isCancelled()){
							mGetResponse=zouponswebservice.store_locator(params[0],params[1],"","","",this.mMapViewRadius,"zpay",CardPurchase.mZPAYStoreDetailsStart,mDeviceLat,mDeviceLong);
						}else{
							progressdialog.dismiss();
						}
					}

					if(!mGetResponse.equals("")){
						if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
							if(!isCancelled()){
								mParsingResponse=parsingclass.parseStore_Locator(mGetResponse,this.mClassName);
								if(mParsingResponse.equalsIgnoreCase("success")){
									for(int i=0;i<WebServiceStaticArrays.mOffsetStoreDetails.size();i++){
										if(!isCancelled()){
											final StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
											if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("Search type or term not available !")*/){
												result ="success";
											}else{
												result="success";
											}
										}else{
											progressdialog.dismiss();
										}
									}
								}else if(mParsingResponse.equalsIgnoreCase("failure")){
									result="Response Error.";
								}else if(mParsingResponse.equalsIgnoreCase("norecords")){
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
		super.onPostExecute(result);
		ShopperHomePage.sIsLoadMore = false;
		CardPurchase.mIsZPAYLoadMore = false;

		if(this.mClassName.equalsIgnoreCase("ShopperHomePage")){
			mCurrentLocation.setEnabled(true);
		}else{
			mCurrentLocation.setEnabled(true);
		}
		progressdialog.dismiss();
		if(mGeopointDuringCall != null && mGeopointDuringCall.latitude == MapViewCenter.mMapViewCenter_GeoPoint.latitude){
			mIsTaskRepeated = false;
		}else{
			mIsTaskRepeated = true;
		}
		if(mClassName.equalsIgnoreCase("ShopperHomePage")){
			mGoogleMap.setOnCameraChangeListener((ShopperHomePage)ctx);	
		}else{
			mGoogleMap.setOnCameraChangeListener((CardPurchase)ctx);
		}

		if(result.equals("nonetwork") && !mIsTaskRepeated){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
		}else if(result.equals("no data") && !mIsTaskRepeated){
			Toast.makeText(ctx, "No stores Available", Toast.LENGTH_SHORT).show();
		}else if(result.equals("success") && !mIsTaskRepeated){
			try{
				//Load Current Location while activity loading
				if(this.mConnectionAvailabilityChecking.ConnectivityCheck(this.ctx)){
					if(mClassName.equalsIgnoreCase("ShopperHomePage")){
						mGoogleMap.clear();
						ctx.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								WebServiceStaticArrays.mStoresLocator.clear();
								for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
									StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									WebServiceStaticArrays.mStoresLocator.add(i,mStoreinfo);
								}
							}
						});
						MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromCurrentLocation",false,mPageFlag); // false for adding fresh adapter
						if(mFoooterView != null && mShopListView.getFooterViewsCount() !=0 &&mShopListView.getAdapter() != null){
							mShopListView.removeFooterView(mFoooterView);
						}
						if(ShopperHomePage.sMapViewOnScrollViewFlag.equalsIgnoreCase("") || ShopperHomePage.sMapViewOnScrollViewFlag.equalsIgnoreCase("qrcode") || ShopperHomePage.sMapViewOnScrollViewFlag.equalsIgnoreCase("search")){
							ShopperHomePage.sMapViewOnScrollViewFlag ="currentlocation";
							ShopperHomePage.mLocationGeoCordinates = new LatLng(Double.parseDouble(ShopperHomePage.sDeviceCurrentLocationLatitude),Double.parseDouble(ShopperHomePage.sDeviceCurrentLocationLongitude));
							mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(ShopperHomePage.mLocationGeoCordinates));
						}
					}else{
						mGoogleMap.clear();

						ctx.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								WebServiceStaticArrays.mStoresLocator.clear();
								for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
									StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									WebServiceStaticArrays.mStoresLocator.add(i,mStoreinfo);
								}
							}
						});

						if(CardPurchase.mZPAYStoreDetailsStart.equalsIgnoreCase("0")){
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromCurrentLocation",false,mPageFlag); // false for adding fresh adapter
						}else{
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromCurrentLocation",true,mPageFlag); // true for refreshing adapter
						}

						if(mFoooterView != null && mShopListView.getFooterViewsCount() !=0 &&mShopListView.getAdapter() != null){
							mShopListView.removeFooterView(mFoooterView);
						}

					}

					//OverlayItem overlayItem;
					ArrayList<Double> minDistance = new ArrayList<Double>();
					minDistance.clear();
					for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
						if(obj.storeCoordinates!=null){
							storeCount=storeCount+1;
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
							minDistance.add(Double.parseDouble(obj.deviceDistance));
						}else{
						}
					}

					if(storeCount==0){
						Toast.makeText(ctx, "No stores Available", Toast.LENGTH_SHORT).show();
					}

				}else{ 
					Toast.makeText(this.ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		if(this.ctx.getClass().getSimpleName().equalsIgnoreCase("ShopperHomePage") && mShouldFreezeViewGone){
			ShopperHomePage.sHomePageFreezeView.setVisibility(View.GONE);
		}else if(this.ctx.getClass().getSimpleName().equalsIgnoreCase("CardPurchase") && mShouldFreezeViewGone ){
			CardPurchase.mZpayStep1IntialFreezeView.setVisibility(View.GONE);
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
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			progressdialog.setMessage("Loading Stores...");
			progressdialog.setCancelable(false);
			progressdialog.show();
		}

	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}
}