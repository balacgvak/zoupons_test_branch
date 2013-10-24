/**
 * 
 */
package com.us.zoupons.android.AsyncThreadClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.us.zoupons.MenuBarSearchClickListener;
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.SlidingView;
import com.us.zoupons.ClassVariables.MapViewCenter;
import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.FlagClasses.ZPayFlag;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.zpay.zpay_step1;

/**
 * Class to load stores_location values from webservice
 */
public class StoresLocationAsynchThread extends AsyncTask<String, String, String>{

	Activity ctx;
	String TAG="StoresLocationAsynchThread";
	private ProgressDialog progressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	AnimationSet mShrinksearch;
	LinearLayout mMenuBarSearch=null;
	RelativeLayout mSearchBoxContainer;
	AutoCompleteTextView mSearchBox;
	Button mClearStoreName;
	ViewGroup mSearchBar;
	Button mFreezeHomePage;
	//List<Overlay> mMapOverlays;
	//MapViewItemizedOverlay mItemizedOverlay;
	//TapControlledMapView mMapView;
	//GeoPoint animateGeoPoint,singleStore;
	ListView mShopListView;
	int storeCount;
	double mMapViewRadius;
	String mClassName;
	String mPageFlag;

	String mProgressStatus="";
	View mFoooterView;
	LatLng mGeopointDuringCall = null,animateGeoPoint;
	private boolean mIsTaskRepeated;

	private boolean mSearchAnimateStatus;
	private GoogleMap mGoogleMap;
	String mDeviceLat,mDeviceLong;

	/*Constructor for HomePagee*/
	public StoresLocationAsynchThread(Activity context,LinearLayout menubarsearch,AnimationSet shrinksearch,AutoCompleteTextView searchbox,ViewGroup searchbar,Button freezehomepage
			,GoogleMap googlemap,ListView shoplistview,double mapviewradius,String classname,Button clearstorename,RelativeLayout searchboxcontainer,String pageflag,
			View footer,String progressstatus,boolean searchanimatestatus,String devicelat,String devicelong){
		this.ctx=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		this.mMenuBarSearch=menubarsearch;
		this.mShrinksearch=shrinksearch;
		this.mSearchBox=searchbox;
		this.mClearStoreName=clearstorename;
		this.mSearchBar=searchbar;
		this.mFreezeHomePage=freezehomepage;
		this.mGoogleMap=googlemap;
		this.mShopListView=shoplistview;
		this.mMapViewRadius=mapviewradius;
		this.mClassName=classname;
		this.mSearchBoxContainer=searchboxcontainer;
		this.mPageFlag=pageflag;
		// for listview offset
		SlidingView.mIsLoadMore = true;
		this.mProgressStatus =progressstatus ;
		this.mFoooterView = footer;
		//this.animateGeoPoint=geopoint;

		// For differentiating repeat webservice call during map scroll..

		mGeopointDuringCall = MapViewCenter.mMapViewCenter_GeoPoint;


		this.mSearchAnimateStatus=searchanimatestatus;
		this.mDeviceLat=devicelat;
		this.mDeviceLong=devicelong;
	}

	/*Constructor for Zpay Step1 Page*/
	public StoresLocationAsynchThread(Activity context,AnimationSet shrinksearch,AutoCompleteTextView searchbox,ViewGroup searchbar,Button freezehomepage
			,GoogleMap googleMap,ListView shoplistview,double mapviewradius,String classname,Button clearstorename,RelativeLayout searchboxcontainer,String pageflag
			,View footer,String progressstatus,boolean searchanimatestatus,String devicelat,String devicelong){
		this.ctx=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		this.mShrinksearch=shrinksearch;
		this.mSearchBox=searchbox;
		this.mSearchBar=searchbar;
		this.mFreezeHomePage=freezehomepage;
		this.mGoogleMap=googleMap;
		//this.mMapView=mapview;
		this.mShopListView=shoplistview;
		this.mMapViewRadius=mapviewradius;
		this.mClassName=classname;
		this.mClearStoreName=clearstorename;
		this.mSearchBoxContainer=searchboxcontainer;
		this.mPageFlag=pageflag;
		// for listview offset
		zpay_step1.mIsZPAYLoadMore = true;
		this.mProgressStatus =progressstatus ;
		this.mFoooterView = footer;
		//this.animateGeoPoint=geopoint;

		// For differentiating repeat webservice call during map scroll..
		mGeopointDuringCall = MapViewCenter.mMapViewCenter_GeoPoint;

		this.mSearchAnimateStatus=searchanimatestatus;
		this.mDeviceLat=devicelat;
		this.mDeviceLong=devicelong;
	}

	@Override
	protected String doInBackground(String... params) {
		Log.i("search", "do in background");
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				if(this.mClassName.equalsIgnoreCase("SlidingView")){
					//if(SlidingView.mStoreDetailsStart.equalsIgnoreCase("0")){
						WebServiceStaticArrays.mStoresLocator.clear();
					//}
					/*if(!isCancelled())*/
						mGetResponse=zouponswebservice.mStoreLocator(params[1], params[2], "single", params[0],mDeviceLat,mDeviceLong,mClassName);
				}else if(this.mClassName.equalsIgnoreCase("zpay_step1")){
					if(zpay_step1.mZPAYStoreDetailsStart.equalsIgnoreCase("0")){
						WebServiceStaticArrays.mStoresLocator.clear();
					}
					/*if(!isCancelled())*/
						mGetResponse=zouponswebservice.mStoreLocator(params[1], params[2], "single", params[0],mDeviceLat,mDeviceLong,mClassName);
				}
				
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					/*if(!isCancelled()){*/
						mParsingResponse=parsingclass.parseStore_Locator(mGetResponse,this.mClassName);
						if(mParsingResponse.equalsIgnoreCase("success")){
							Log.i(TAG,"SearchStore List Size: "+WebServiceStaticArrays.mOffsetStoreDetails.size());
							for(int i=0;i<WebServiceStaticArrays.mOffsetStoreDetails.size();i++){
								/*if(!isCancelled()){*/
									final StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("Search type or term not available !")*/){
										//Log.i(TAG,"message : "+parsedobjectvalues.message);
										result ="success";
									}else{
										//Log.i(TAG,"storeName : "+parsedobjectvalues.storeName);
										//Log.i(TAG,"city : "+parsedobjectvalues.city);
										//Log.i(TAG,"country : "+parsedobjectvalues.country);
										result="success";
									}
								//}
							}
						}else if(mParsingResponse.equalsIgnoreCase("failure")){
							Log.i(TAG,"Error");
							result="Response Error.";
						}else if(mParsingResponse.equalsIgnoreCase("norecords")){
							Log.i(TAG,"No Records");
							result="No Stores Available.";
						}
					//}
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
		if(progressdialog.isShowing())
		progressdialog.dismiss();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.i(TAG,"result: "+result);
		SlidingView.mIsLoadMore = false;
		zpay_step1.mIsZPAYLoadMore = false;
		progressdialog.dismiss();
		if(this.mClassName.equalsIgnoreCase("SlidingView")){
			SlidingView.mCurrentLocation.setEnabled(true);
		}else{
			zpay_step1.mCurrentLocation.setEnabled(true);
		}
		
		//Function to close search bar
		closeSearch();
		
		// To check repeat webservice call during map scroll..
		if(mGeopointDuringCall != null && mGeopointDuringCall.latitude == MapViewCenter.mMapViewCenter_GeoPoint.latitude){
			Log.i("repeat call", "false");
			mIsTaskRepeated = false;
		}else{
			mIsTaskRepeated = true;
			Log.i("repeat call", "true");
		}
		if(result.equals("nonetwork") && !mIsTaskRepeated){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.") && !mIsTaskRepeated){
			Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
		}else if(result.equals("No Stores Available.") && !mIsTaskRepeated){
			Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
		}else if(result.equals("success") && !mIsTaskRepeated){
			try{
				//Load Current Location while activity loading
				if(this.mConnectionAvailabilityChecking.ConnectivityCheck(this.ctx)){

					mGoogleMap.clear();
					
					/*ArrayList<Double> minDistance = new ArrayList<Double>();
					minDistance.clear();*/

					if(mClassName.equalsIgnoreCase("SlidingView")){
						ctx.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								WebServiceStaticArrays.mStoresLocator.clear();
								for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
									StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(SlidingView.mStoreDetailsStart)+i,mStoreinfo);
								}	

							}
						});
						if(SlidingView.mStoreDetailsStart.equalsIgnoreCase("0")){
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromSearch",false,mPageFlag);	//new
						}else{
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromSearch",true,mPageFlag);	//new
						}

						if(mFoooterView != null && mShopListView.getFooterViewsCount() !=0 &&mShopListView.getAdapter() != null){
							Log.i(TAG, "Remove Footer View From Coupon List");
							mShopListView.removeFooterView(mFoooterView);
						}
						if(Integer.parseInt(SlidingView.mStoreDetailsCount)>20){
							SlidingView.mStoreDetailsStart = SlidingView.mStoreDetailsEndLimit;
							SlidingView.mStoreDetailsEndLimit = String.valueOf(Integer.parseInt(SlidingView.mStoreDetailsEndLimit)+20);
						}
					}else{
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
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromSearch",false,mPageFlag);	//new
						}else{
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromSearch",true,mPageFlag);	//new
						}

						if(mFoooterView != null && mShopListView.getFooterViewsCount() !=0 &&mShopListView.getAdapter() != null){
							Log.i(TAG, "Remove Footer View From Coupon List");
							mShopListView.removeFooterView(mFoooterView);
						}
						if(Integer.parseInt(zpay_step1.mZPAYStoreDetailsCount)>20){
							zpay_step1.mZPAYStoreDetailsStart= zpay_step1.mZPAYStoreDetailsEndLimit;
							zpay_step1.mZPAYStoreDetailsEndLimit = String.valueOf(Integer.parseInt(zpay_step1.mZPAYStoreDetailsEndLimit)+20);
						}
						
						//mGoogleMap.setOnCameraChangeListener((ZPayStep1)ctx);
					}

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
							if(this.mSearchAnimateStatus){
								//mGoogleMap.setOnCameraChangeListener(null);
								mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(store_obj.storeCoordinates),new CancelableCallback() {
									
									@Override
									public void onFinish() {
										// TODO Auto-generated method stub
										/*if(mClassName.equalsIgnoreCase("SlidingView")){
											mGoogleMap.setOnCameraChangeListener((SlidingView)ctx);									
										}else{
											//mGoogleMap.setOnCameraChangeListener((SlidingView)ctx);
										}*/
									}
									
									@Override
									public void onCancel() {
										// TODO Auto-generated method stub
									}
								});	
							}
						}else{}
					}else{
						mGoogleMap.clear();
					}
					/*for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
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

							//singleStore=obj.geoPoint;
							//OverlayItem overlayItem = new OverlayItem(obj.geoPoint, obj.storeName, obj.addressLine1+"\n"+obj.addressLine2);
							// for differentiating preffered and un preferred stores
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
							//minDistance.add(Double.parseDouble(obj.deviceDistance));
						}else{
							Log.i(TAG,"No GeoPoint StoreName: "+obj.storeName);
							Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show();
						}
					}*/

					/*//To get least distance of store geopoint from current location to animate
					if(minDistance.size()>0&&minDistance!=null){
						Object object = Collections.min(minDistance);
						Log.i(TAG,"MinDistance: "+object);

						for(int j=0;j<WebServiceStaticArrays.mStoresLocator.size();j++){
							StoreLocator_ClassVariables obj1 = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(j);
							if(obj1.distance.equalsIgnoreCase(String.valueOf(object))){
								this.animateGeoPoint=obj1.storeCoordinates;
								Log.i(TAG,"Min Distance: "+obj1.distance);
								//Log.i(TAG,"Min GeoPoint: "+obj1.geoPoint);
							}
						}
					}*/

					/*Log.i(TAG,"StoreCount: "+storeCount);
					if(storeCount>1){
						//here we have to calculate and get nearest store from our current location to shown it in center of the mapview
						MapOverlays.mapOverlays.clear();
						MapOverlays.mapOverlays.add(mItemizedOverlay);
						mMapView.postInvalidate();

						if(this.mSearchAnimateStatus){
							mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(animateGeoPoint));
						}
					}else if(storeCount==1){
						MapOverlays.mapOverlays.clear();
						Log.i(TAG,"ItemizedOverlay Size: "+mItemizedOverlay.size());
						MapOverlays.mapOverlays.add(mItemizedOverlay);
						mMapView.postInvalidate();
						if(this.mSearchAnimateStatus){
							mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(animateGeoPoint));
						}
					}else if(storeCount==0){
						Toast.makeText(ctx, "No Stores Available", Toast.LENGTH_SHORT).show();
						mGoogleMap.clear();
						MapOverlays.mapOverlays.clear();
						mMapView.postInvalidate();
						if(this.mSearchAnimateStatus){
							final MapController mc = mMapView.getController();
							mc.animateTo(this.animateGeoPoint);
						}
					}*/
				}else{
					Toast.makeText(this.ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
				}
				/*// If current location overlay cleared again add to it
				mMapView.getOverlays().add(SlidingView.locationoverlay);
				mMapView.postInvalidate();*/
			}catch(Exception e){
				Log.i(TAG,"Issue occur while callout loading.");
				e.printStackTrace();
			}
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
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);

		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
			//MapOverlays.mapOverlays.clear();
		}
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	protected void closeSearch(){

		InputMethodManager softkeyboard = (InputMethodManager)this.ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		softkeyboard.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);

		if(MenuBarSearchClickListener.CLICKFLAG==true&&MenuOutClass.HOMEPAGE_MENUOUT==false){
			this.mMenuBarSearch.setBackgroundResource(R.drawable.header_2);

			if(ZPayFlag.getFlag()!=1){
			}else{
				ScaleAnimation shrink = new ScaleAnimation(
						1f, 1f, 
						1f, 0f,
						Animation.RELATIVE_TO_PARENT, 100,
						Animation.RELATIVE_TO_PARENT, 0);
				shrink.setStartOffset(100);
				shrink.setDuration(500);
				mShrinksearch.addAnimation(shrink);
				mShrinksearch.setFillAfter(true);
				mShrinksearch.setInterpolator(new AccelerateInterpolator(1.0f));

				this.mSearchBoxContainer.startAnimation(mShrinksearch);
				this.mSearchBoxContainer.postDelayed(new Runnable() {
					@Override
					public void run() {
						mSearchBox.setText("");
						mSearchBox.setVisibility(View.GONE);
						mClearStoreName.setVisibility(View.GONE);
						mSearchBoxContainer.setVisibility(View.GONE);
						mFreezeHomePage.setVisibility(View.GONE);
					}
				}, 400);
			}
			MenuBarSearchClickListener.CLICKFLAG=false;
		}else{
			if(ZPayFlag.getFlag()!=1){
			}else{
				ScaleAnimation shrink = new ScaleAnimation(
						1f, 1f, 
						1f, 0f,
						Animation.RELATIVE_TO_PARENT, 100,
						Animation.RELATIVE_TO_PARENT, 0);
				shrink.setStartOffset(100);
				shrink.setDuration(500);
				mShrinksearch.addAnimation(shrink);
				mShrinksearch.setFillAfter(true);
				mShrinksearch.setInterpolator(new AccelerateInterpolator(1.0f));

				this.mSearchBoxContainer.startAnimation(mShrinksearch);
				this.mSearchBoxContainer.postDelayed(new Runnable() {
					@Override
					public void run() {
						mSearchBox.setText("");
						mSearchBox.setVisibility(View.GONE);
						mClearStoreName.setVisibility(View.GONE);
						mSearchBoxContainer.setVisibility(View.GONE);
						mFreezeHomePage.setVisibility(View.GONE);
					}
				}, 400);
			}
			MenuBarSearchClickListener.ZPAYFLAG=false;
		}
	}
}