/**
 * 
 */
package com.us.zoupons.android.asyncthread_class;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import com.us.zoupons.OpenMenu;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.MapViewCenter;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.StoreLocator_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.flagclasses.ZPayFlag;
import com.us.zoupons.mobilepay.CardPurchase;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * Class to load stores_location values from webservice
 */
public class StoresLocationAsynchThread extends AsyncTask<String, String, String>{

	private Activity ctx;
	private ProgressDialog progressdialog=null;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse;
	private AnimationSet mShrinksearch;
	//private LinearLayout mMenuBarSearch=null;
	private RelativeLayout mSearchBoxContainer;
	private AutoCompleteTextView mSearchBox;
	private Button mClearStoreName;
	private Button mFreezeHomePage;
	private ListView mShopListView;
	private String mClassName;
	private String mPageFlag;
	private String mProgressStatus="";
	private View mFoooterView;
	private LatLng mGeopointDuringCall = null;
	private boolean mIsTaskRepeated;
	private boolean mSearchAnimateStatus;
	private GoogleMap mGoogleMap;
	private Button mCurrentLocation;
	private String mDeviceLat,mDeviceLong;

	/*Constructor for HomePagee*/
	public StoresLocationAsynchThread(Activity context,LinearLayout menubarsearch,AnimationSet shrinksearch,AutoCompleteTextView searchbox,ViewGroup searchbar,Button freezehomepage
			,GoogleMap googlemap,ListView shoplistview,Button currentLocation,double mapviewradius,String classname,Button clearstorename,RelativeLayout searchboxcontainer,String pageflag,
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
		//this.mMenuBarSearch=menubarsearch;
		this.mShrinksearch=shrinksearch;
		this.mSearchBox=searchbox;
		this.mClearStoreName=clearstorename;
		this.mFreezeHomePage=freezehomepage;
		this.mGoogleMap=googlemap;
		this.mShopListView=shoplistview;
		this.mClassName=classname;
		this.mSearchBoxContainer=searchboxcontainer;
		this.mPageFlag=pageflag;
		this.mCurrentLocation = currentLocation;
		// for listview offset
		ShopperHomePage.sIsLoadMore = true;
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
			,GoogleMap googleMap,ListView shoplistview,Button currentLocation,double mapviewradius,String classname,Button clearstorename,RelativeLayout searchboxcontainer,String pageflag
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
		this.mFreezeHomePage=freezehomepage;
		this.mGoogleMap=googleMap;
		//this.mMapView=mapview;
		this.mShopListView=shoplistview;
		this.mClassName=classname;
		this.mClearStoreName=clearstorename;
		this.mSearchBoxContainer=searchboxcontainer;
		this.mPageFlag=pageflag;
		// for listview offset
		CardPurchase.mIsZPAYLoadMore = true;
		this.mProgressStatus =progressstatus ;
		this.mFoooterView = footer;
		//this.animateGeoPoint=geopoint;
		this.mCurrentLocation = currentLocation;
		// For differentiating repeat webservice call during map scroll..
		mGeopointDuringCall = MapViewCenter.mMapViewCenter_GeoPoint;

		this.mSearchAnimateStatus=searchanimatestatus;
		this.mDeviceLat=devicelat;
		this.mDeviceLong=devicelong;
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				if(this.mClassName.equalsIgnoreCase("ShopperHomePage")){
						mGetResponse=zouponswebservice.mStoreLocator(params[1], params[2], "single", params[0],mDeviceLat,mDeviceLong,mClassName);
				}else if(this.mClassName.equalsIgnoreCase("CardPurchase")){
						mGetResponse=zouponswebservice.mStoreLocator(params[1], params[2], "single", params[0],mDeviceLat,mDeviceLong,mClassName);
				}
				
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParsingResponse=parsingclass.parseStore_Locator(mGetResponse,this.mClassName);
						if(mParsingResponse.equalsIgnoreCase("success")){
							for(int i=0;i<WebServiceStaticArrays.mOffsetStoreDetails.size();i++){
									final StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("Search type or term not available !")*/){
										result ="success";
									}else{
										result="success";
									}
							}
						}else if(mParsingResponse.equalsIgnoreCase("failure")){
							result="Response Error.";
						}else if(mParsingResponse.equalsIgnoreCase("norecords")){
							result="No stores Available.";
						}
				}else{
					result="Response Error.";
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
	protected void onCancelled() {
		super.onCancelled();
		if(progressdialog.isShowing())
		progressdialog.dismiss();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		ShopperHomePage.sIsLoadMore = false;
		CardPurchase.mIsZPAYLoadMore = false;
		progressdialog.dismiss();
		if(this.mClassName.equalsIgnoreCase("ShopperHomePage")){
			mCurrentLocation.setEnabled(true);
		}else{
			mCurrentLocation.setEnabled(true);
		}
		
		//Function to close search bar
		closeSearch();
		
		// To check repeat webservice call during map scroll..
		if(mGeopointDuringCall != null && mGeopointDuringCall.latitude == MapViewCenter.mMapViewCenter_GeoPoint.latitude){
			mIsTaskRepeated = false;
		}else{
			mIsTaskRepeated = true;
		}
		if(result.equals("nonetwork") && !mIsTaskRepeated){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.") && !mIsTaskRepeated){
			Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
		}else if(result.equals("No stores Available.") && !mIsTaskRepeated){
			Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
		}else if(result.equals("success") && !mIsTaskRepeated){
			try{
				//Load Current Location while activity loading
				if(this.mConnectionAvailabilityChecking.ConnectivityCheck(this.ctx)){

					mGoogleMap.clear();

					if(mClassName.equalsIgnoreCase("ShopperHomePage")){
						ctx.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								WebServiceStaticArrays.mStoresLocator.clear();
								for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
									StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(ShopperHomePage.sStoreDetailsStart)+i,mStoreinfo);
								}	

							}
						});
						if(ShopperHomePage.sStoreDetailsStart.equalsIgnoreCase("0")){
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromSearch",false,mPageFlag);	//new
						}else{
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromSearch",true,mPageFlag);	//new
						}

						if(mFoooterView != null && mShopListView.getFooterViewsCount() !=0 &&mShopListView.getAdapter() != null){
							mShopListView.removeFooterView(mFoooterView);
						}
						if(Integer.parseInt(ShopperHomePage.sStoreDetailsCount)>20){
							ShopperHomePage.sStoreDetailsStart = ShopperHomePage.sStoreDetailsEndLimit;
							ShopperHomePage.sStoreDetailsEndLimit = String.valueOf(Integer.parseInt(ShopperHomePage.sStoreDetailsEndLimit)+20);
						}
						StoreLocator_ClassVariables obj1 = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(0);
						if(obj1.deviceDistance.equalsIgnoreCase("-1")){
							RightMenuStoreId_ClassVariables.mStoreID=obj1.id;
							RightMenuStoreId_ClassVariables.mStoreName=obj1.storeName;
							RightMenuStoreId_ClassVariables.mLocationId=obj1.location_id;
							RightMenuStoreId_ClassVariables.rightmenu_favourite_status=obj1.favorite_store;
							RightMenuStoreId_ClassVariables.mStoreLocation = "online store";
							
							if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
								ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
							}else{
								ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
							}
							MenuUtilityClass.SetRightMenuStatus(obj1, this.ctx);
							OpenMenu.toOpenRightMenu("ShopperHomePage");		// Function to open RightMenu
						}else{
							
						}
										
					}else{
						ctx.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								WebServiceStaticArrays.mStoresLocator.clear();
								for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
									StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);

									WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(CardPurchase.mZPAYStoreDetailsStart)+i,mStoreinfo);
								}	

							}
						});
						if(CardPurchase.mZPAYStoreDetailsStart.equalsIgnoreCase("0")){
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromSearch",false,mPageFlag);	//new
						}else{
							MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromSearch",true,mPageFlag);	//new
						}

						if(mFoooterView != null && mShopListView.getFooterViewsCount() !=0 &&mShopListView.getAdapter() != null){
							mShopListView.removeFooterView(mFoooterView);
						}
						if(Integer.parseInt(CardPurchase.mZPAYStoreDetailsCount)>20){
							CardPurchase.mZPAYStoreDetailsStart= CardPurchase.mZPAYStoreDetailsEndLimit;
							CardPurchase.mZPAYStoreDetailsEndLimit = String.valueOf(Integer.parseInt(CardPurchase.mZPAYStoreDetailsEndLimit)+20);
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
					
				}else{
					Toast.makeText(this.ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
				}
				
			}catch(Exception e){
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
		super.onPreExecute();
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
			//MapOverlays.mapOverlays.clear();
		}
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	protected void closeSearch(){

		InputMethodManager softkeyboard = (InputMethodManager)this.ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		softkeyboard.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 0);

		if(MenuBarSearchClickListener.sCLICKFLAG==true&&MenuOutClass.HOMEPAGE_MENUOUT==false){
			
			if(ZPayFlag.getFlag()!=1){
			}else{
				//this.mMenuBarSearch.setBackgroundResource(R.drawable.header_2);
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
			MenuBarSearchClickListener.sCLICKFLAG=false;
		}else{
			if(ZPayFlag.getFlag()!=1){
			}else{
				//this.mMenuBarSearch.setBackgroundResource(R.drawable.header_2);
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
			MenuBarSearchClickListener.sZPAYFLAG=false;
		}
	}
}