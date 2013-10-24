package com.us.zoupons.android.AsyncThreadClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.StoreInfo.MainActivity;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.android.listview.inflater.classes.Favorites_Adapter;

public class FavouritesTaskLoader extends AsyncTask<String, String, String> {

	private Activity context;
	private ListView mFavouritesList;
	private ZouponsWebService zouponswebservice;
	private ZouponsParsingClass parsingclass;
	private NetworkCheck mConnectivityCheck;
	private String mFavouriteType;
	public static Favorites_Adapter mFavouriteAdapter;
	private Favorites_Adapter mFriendFavouritesAdapter;
	
	// for listview scroll with offset
	String mProgressStatus="";
	String mCheckRefresh="";
	private View mFooterLayout;
	String mClassName;

	/*MapViewItemizedOverlay mItemizedOverlay;
	TapControlledMapView mMapView;*/
	private GoogleMap mGoogleMap;
	int storeCount;
	double mMapViewRadius;
	String mPageFlag;
	private ProgressDialog progressdialog=null;
	
	public static String TAG="FavouritesTaskLoader";
	
	public FavouritesTaskLoader(Activity context, ListView FavoritesListView, View FooterLayout, String progressStatus,String classname,String pageflag) {
		this.context = context;
		this.mFavouritesList = FavoritesListView;
		progressdialog=new ProgressDialog(this.context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		
		zouponswebservice = new ZouponsWebService(context);
		parsingclass = new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
		this.mClassName=classname;
		mProgressStatus = progressStatus;
		MainMenuActivity.mIsLoadMore = true;
		mFooterLayout = FooterLayout;
		this.mPageFlag=pageflag;
	}

	public FavouritesTaskLoader(Activity context, ListView FavoritesListView, View FooterLayout, String progressStatus,String classname,
			GoogleMap googleMap,double mapviewradius,String pageflag) {
		this.context = context;
		this.mFavouritesList = FavoritesListView;
		progressdialog=new ProgressDialog(this.context);
		progressdialog.setCancelable(false);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		
		zouponswebservice = new ZouponsWebService(context);
		parsingclass = new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
		this.mClassName=classname;
		mProgressStatus = progressStatus;
		MainMenuActivity.mIsLoadMore = true;
		mFooterLayout = FooterLayout;
		this.mGoogleMap = googleMap;
		/*this.mItemizedOverlay=itemizedoverlay;
		this.mMapView=mapview;*/
		//this.animateGeoPoint=geopoint;
		this.mMapViewRadius=mapviewradius;
		this.mPageFlag=pageflag;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(mProgressStatus.equalsIgnoreCase("NOPROGRESS")){
			Log.i("GetStoreReview", "No Progress");
		}else{
			((Activity) context).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);

			//Start a status dialog
			progressdialog = ProgressDialog.show(context,"Loading...","Please Wait!",true);
		}
	}

	@Override
	protected String doInBackground(String... params) {
		String mresult = "", mParsingResponse = "";mCheckRefresh = params[1];
		try {
			if (mConnectivityCheck.ConnectivityCheck(context)) {
				mFavouriteType = params[0];
				String mResponse = zouponswebservice.GetFavouriteStores(mFavouriteType,MainMenuActivity.mFavouritesStart);
				if (!mResponse.equals("")) {
					if (!mResponse.equals("failure")&& !mResponse.equals("noresponse")) {
						mParsingResponse = parsingclass.parseFavouriteStores(mResponse,mFavouriteType);
						if (mParsingResponse.equalsIgnoreCase("success")) {
							mresult = "success";
						} else if (mParsingResponse.equalsIgnoreCase("Failure")) {
							mresult = "failure";
						} else if (mParsingResponse.equalsIgnoreCase("norecords")) {
							mresult = "norecords";
						}
					} else {
						mresult = "Response Error.";
					}
				} else {
					mresult = "nodata";
				}
			} else {
				mresult = "nonetwork";
			}
		} catch (Exception e) {
			mresult = "failure";
			e.printStackTrace();
		}
		return mresult;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		MainMenuActivity.mIsLoadMore = false;
		if (result.equalsIgnoreCase("nonetwork")) {
			Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
		} else if (result.equalsIgnoreCase("Response Error.")) {
			alertBox_service("Information", "Unable to reach service.");
		} else if (result.equalsIgnoreCase("norecords")) {
			if(mFavouriteType.equalsIgnoreCase("FriendFavouriteStore")){
				mFriendFavouritesAdapter = new Favorites_Adapter(context,WebServiceStaticArrays.mFavouriteFriendStoreDetails,this.mClassName,mPageFlag);
				mFavouritesList.setAdapter(mFriendFavouritesAdapter);
				mFavouritesList.setBackgroundResource(0);
			}else{
				mFavouriteAdapter = new Favorites_Adapter(context,WebServiceStaticArrays.mFavouriteStoreDetails,this.mClassName,mPageFlag);
				mFavouritesList.setAdapter(mFavouriteAdapter);
				mFavouritesList.setBackgroundResource(0);	
			}
			alertBox_service("Information", "Favourites stores not available");
		} else if (result.equalsIgnoreCase("Failure")) {
			alertBox_service("Information", "Unable to process.");
		} else if (result.equalsIgnoreCase("success")) {
			
			Log.i("size", WebServiceStaticArrays.mFavouriteFriendStoreDetails.size()+" ");
			if(mFooterLayout != null && mFavouritesList.getFooterViewsCount() == 0){
				mFavouritesList.addFooterView(mFooterLayout);
			}
			
			if(mFavouriteType.equalsIgnoreCase("FriendFavouriteStore")){
				MainMenuActivity.mIsfriendfavourite = true;
				context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						for(int i=0 ; i< WebServiceStaticArrays.mOffsetFavouriteStores.size() ; i++){
							POJOStoreInfo mStoreinfo = (POJOStoreInfo) WebServiceStaticArrays.mOffsetFavouriteStores.get(i);
							WebServiceStaticArrays.mFavouriteFriendStoreDetails.add(Integer.parseInt(MainActivity.mFavouritesStart)+i,mStoreinfo);
						}

						if(mCheckRefresh.equalsIgnoreCase("RefreshAdapter") && mFriendFavouritesAdapter != null){
							Log.i("Adapter", "Refresh");
							mFriendFavouritesAdapter.notifyDataSetChanged();
						}else{
							Log.i("Adapter", "Add Adapter");			
							mFriendFavouritesAdapter = new Favorites_Adapter(context,WebServiceStaticArrays.mFavouriteFriendStoreDetails,mClassName,mPageFlag);
							mFavouritesList.setAdapter(mFriendFavouritesAdapter);				
						}
					}
				});
				
				if(mFooterLayout != null && mFavouritesList.getFooterViewsCount() !=0 && mFavouritesList.getAdapter() != null){
					Log.i("footer", "Remove Footer View");
					mFavouritesList.removeFooterView(mFooterLayout);
					Log.i("footer", "Remove Footer View");
				}
				MainMenuActivity.mFavouritesStart = MainMenuActivity.mFavouritesEnd;
				MainMenuActivity.mFavouritesEnd = String.valueOf(Integer.parseInt(MainMenuActivity.mFavouritesEnd)+20);
			}else{
				MainMenuActivity.mIsfriendfavourite = false;
				context.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						for(int i=0 ; i< WebServiceStaticArrays.mOffsetFavouriteStores.size() ; i++){
							POJOStoreInfo mStoreinfo = (POJOStoreInfo) WebServiceStaticArrays.mOffsetFavouriteStores.get(i);
							WebServiceStaticArrays.mFavouriteStoreDetails.add(Integer.parseInt(MainActivity.mFavouritesStart)+i,mStoreinfo);
							if(!mClassName.equalsIgnoreCase("Favorites")&&i==0){ // Condition true if control from mobile pay step1 and move the first geopoint to mapview
								//mMapView.getController().animateTo(mStoreinfo.mGeoPoint);
								if(mStoreinfo.mStoreCoordinates != null) 
								mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mStoreinfo.mStoreCoordinates));
							}
						}
							
						if(mCheckRefresh.equalsIgnoreCase("RefreshAdapter") && mFavouriteAdapter != null){
							Log.i("Adapter", "Refresh");
							mFavouriteAdapter.notifyDataSetChanged();
						}else{
							Log.i("Adapter", "Add Adapter");			
							mFavouriteAdapter = new Favorites_Adapter(context,WebServiceStaticArrays.mFavouriteStoreDetails,mClassName,mPageFlag);
							mFavouritesList.setAdapter(mFavouriteAdapter);				
						}		
					}
				});

				if(mFooterLayout != null && mFavouritesList.getFooterViewsCount() !=0 && mFavouritesList.getAdapter() != null){
					Log.i("footer", "Remove Footer View");
					mFavouritesList.removeFooterView(mFooterLayout);
					Log.i("footer", "Remove Footer View");
				}
				MainMenuActivity.mFavouritesStart = MainMenuActivity.mFavouritesEnd;
				MainMenuActivity.mFavouritesEnd = String.valueOf(Integer.parseInt(MainMenuActivity.mFavouritesEnd)+20);
				
				if(this.mClassName.equalsIgnoreCase("zpay_step1")){
					if(this.mConnectivityCheck.ConnectivityCheck(this.context)){
						
			        	mGoogleMap.clear();
			        	
			        	/*//OverlayItem overlayItem;
			        	ArrayList<Double> minDistance = new ArrayList<Double>();
			        	minDistance.clear();*/
			        	for(int i=0;i<WebServiceStaticArrays.mFavouriteStoreDetails.size();i++){
			        		POJOStoreInfo obj = (POJOStoreInfo) WebServiceStaticArrays.mFavouriteStoreDetails.get(i);
			        		if(obj.mStoreCoordinates!=null){
			        			storeCount=storeCount+1;
			        			Log.i("Adding marker", storeCount+" ");
			        			MarkerOptions mGoogleMapMarkerOptions =  new MarkerOptions();
			        			mGoogleMapMarkerOptions.position(obj.mStoreCoordinates);
			        			mGoogleMapMarkerOptions.title(obj.store_name);
			        			// Adding position to marker to differentiate while tapping marker
			        			mGoogleMapMarkerOptions.snippet(String.valueOf(i)+",,"+obj.address_line1);
			        			// for differentiating preffered and un preferred stores
			        			if(obj.has_invoicecenter.equalsIgnoreCase("yes")){
			        				BitmapDescriptor marker_icon= BitmapDescriptorFactory.fromResource(R.drawable.ant3);
			        				mGoogleMapMarkerOptions.icon(marker_icon);
			        			}else{
			        				BitmapDescriptor marker_icon= BitmapDescriptorFactory.fromResource(R.drawable.dot);
			        				mGoogleMapMarkerOptions.icon(marker_icon);
			        			}
			        			mGoogleMap.addMarker(mGoogleMapMarkerOptions);
			        		}else{
			        			Log.i(TAG,"No GeoPoint StoreName: "+obj.store_name);
			        		}
			        	}
				        
				       /* //To get least distance of store geopoint from current location to animate
				        if(minDistance.size()>0&&minDistance!=null){
				        	Object object = Collections.min(minDistance);
				        	Log.i(TAG,"MinDistance: "+object);

				        	for(int j=0;j<WebServiceStaticArrays.mFavouriteStoreDetails.size();j++){
				        		POJOStoreInfo obj1 = (POJOStoreInfo) WebServiceStaticArrays.mFavouriteStoreDetails.get(j);
				        		if(obj1.store_distance.equalsIgnoreCase(String.valueOf(object))){
				        			animateGeoPoint=obj1.mGeoPoint;
				        			Log.i(TAG,"Min Distance: "+obj1.store_distance);
				        			Log.i(TAG,"Min GeoPoint: "+obj1.mGeoPoint);
				        		}
				        	}
				        }*/
				        if(storeCount == 0){
				        	Toast.makeText(context, "No Stores Available", Toast.LENGTH_SHORT).show();
				        }
				       /* if(storeCount>1){
				        	//here we have to calculate and get nearest store from our current location to shown it in center of the mapview
				        	MapOverlays.mapOverlays.clear();
				        	MapOverlays.mapOverlays.add(mItemizedOverlay);
				        	mMapView.invalidate();
				        }else if(storeCount==1){
				        	MapOverlays.mapOverlays.clear();
				        	MapOverlays.mapOverlays.add(mItemizedOverlay);
				        	mMapView.invalidate();
				        }else if(storeCount==0){
				        	alertBox_service("Information", "No Stores Available.");
				        	MapOverlays.mapOverlays.clear();
				        	mMapView.invalidate();
				        }
				        // If current location overlay cleared again add to it
				        mMapView.getOverlays().add(SlidingView.locationoverlay);
				        mMapView.postInvalidate();*/
					}else{ 
						Toast.makeText(this.context, "No Network Connection", Toast.LENGTH_SHORT).show();
					}
				}
			}
			
		}
		progressdialog.dismiss();
	}

	private void alertBox_service(String title, String msg) {
		// TODO Auto-generated method stub
		AlertDialog.Builder service_alert = new AlertDialog.Builder(context);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}