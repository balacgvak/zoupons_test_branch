package com.us.zoupons.android.asyncthread_class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import com.us.zoupons.classvariables.POJOStoreInfo;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.listview_inflater_classes.Favorites_Adapter;
import com.us.zoupons.shopper.store_info.StoreInformation;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to communicate with webservice to fetch favorite store
 *
 */

public class FavouritesTaskLoader extends AsyncTask<String, String, String> {

	private Activity mContext;
	private ListView mFavouritesList;
	private ZouponsWebService mZouponswebservice;
	private ZouponsParsingClass mParsingclass;
	private NetworkCheck mConnectivityCheck;
	private String mFavouriteType;
	public static Favorites_Adapter mFavouriteAdapter;
	private Favorites_Adapter mFriendFavouritesAdapter;
	// for listview scroll with offset
	private String mProgressStatus="";
	private String mCheckRefresh="";
	private View mFooterLayout;
	private String mClassName;
	private GoogleMap mGoogleMap;
	private int storeCount;
	private String mPageFlag;
	private ProgressDialog progressdialog=null;
	public String TAG="FavouritesTaskLoader";
	
	public FavouritesTaskLoader(Activity context, ListView FavoritesListView, View FooterLayout, String progressStatus,String classname,String pageflag) {
		this.mContext = context;
		this.mFavouritesList = FavoritesListView;
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		mZouponswebservice = new ZouponsWebService(context);
		mParsingclass = new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
		this.mClassName=classname;
		mProgressStatus = progressStatus;
		MainMenuActivity.mIsLoadMore = true;
		mFooterLayout = FooterLayout;
		this.mPageFlag=pageflag;
	}

	public FavouritesTaskLoader(Activity context, ListView FavoritesListView, View FooterLayout, String progressStatus,String classname,
			GoogleMap googleMap,double mapviewradius,String pageflag) {
		this.mContext = context;
		this.mFavouritesList = FavoritesListView;
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(false);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		mZouponswebservice = new ZouponsWebService(context);
		mParsingclass = new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
		this.mClassName=classname;
		mProgressStatus = progressStatus;
		MainMenuActivity.mIsLoadMore = true;
		mFooterLayout = FooterLayout;
		this.mGoogleMap = googleMap;
		/*this.mItemizedOverlay=itemizedoverlay;
		this.mMapView=mapview;*/
		//this.animateGeoPoint=geopoint;
		this.mPageFlag=pageflag;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(mProgressStatus.equalsIgnoreCase("NOPROGRESS")){
		}else{
			((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
			//Start a status dialog
			progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		}
	}

	@Override
	protected String doInBackground(String... params) {
		String mresult = "", mParsingResponse = "";mCheckRefresh = params[1];
		try {
			if (mConnectivityCheck.ConnectivityCheck(mContext)) {
				mFavouriteType = params[0];
				String mResponse = mZouponswebservice.GetFavouriteStores(mFavouriteType,MainMenuActivity.mFavouritesStart);
				if (!mResponse.equals("")) {
					if (!mResponse.equals("failure")&& !mResponse.equals("noresponse")) {
						mParsingResponse = mParsingclass.parseFavouriteStores(mResponse,mFavouriteType);
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
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		} else if (result.equalsIgnoreCase("Response Error.")) {
			alertBox_service("Information", "Unable to reach service.");
		} else if (result.equalsIgnoreCase("norecords")) {
			if(mFavouriteType.equalsIgnoreCase("FriendFavouriteStore")){
				mFriendFavouritesAdapter = new Favorites_Adapter(mContext,WebServiceStaticArrays.mFavouriteFriendStoreDetails,this.mClassName,mPageFlag);
				mFavouritesList.setAdapter(mFriendFavouritesAdapter);
				mFavouritesList.setBackgroundResource(0);
				alertBox_service("Information", "None of your friends have stores in their favorites list");
			}else{ //user favorite
				mFavouriteAdapter = new Favorites_Adapter(mContext,WebServiceStaticArrays.mFavouriteStoreDetails,this.mClassName,mPageFlag);
				mFavouritesList.setAdapter(mFavouriteAdapter);
				mFavouritesList.setBackgroundResource(0);
				alertBox_service("Information", "No store in favorites list");
			}
			
		} else if (result.equalsIgnoreCase("Failure")) {
			alertBox_service("Information", "Unable to process.");
		} else if (result.equalsIgnoreCase("success")) {
			if(mFooterLayout != null && mFavouritesList.getFooterViewsCount() == 0){
				mFavouritesList.addFooterView(mFooterLayout);
			}
			if(mFavouriteType.equalsIgnoreCase("FriendFavouriteStore")){
				MainMenuActivity.mIsfriendfavourite = true;
				mContext.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						for(int i=0 ; i< WebServiceStaticArrays.mOffsetFavouriteStores.size() ; i++){
							POJOStoreInfo mStoreinfo = (POJOStoreInfo) WebServiceStaticArrays.mOffsetFavouriteStores.get(i);
							WebServiceStaticArrays.mFavouriteFriendStoreDetails.add(Integer.parseInt(StoreInformation.mFavouritesStart)+i,mStoreinfo);
						}

						if(mCheckRefresh.equalsIgnoreCase("RefreshAdapter") && mFriendFavouritesAdapter != null){
							mFriendFavouritesAdapter.notifyDataSetChanged();
						}else{
							mFriendFavouritesAdapter = new Favorites_Adapter(mContext,WebServiceStaticArrays.mFavouriteFriendStoreDetails,mClassName,mPageFlag);
							mFavouritesList.setAdapter(mFriendFavouritesAdapter);				
						}
					}
				});
				
				if(mFooterLayout != null && mFavouritesList.getFooterViewsCount() !=0 && mFavouritesList.getAdapter() != null){
					mFavouritesList.removeFooterView(mFooterLayout);
				}
				MainMenuActivity.mFavouritesStart = MainMenuActivity.mFavouritesEnd;
				MainMenuActivity.mFavouritesEnd = String.valueOf(Integer.parseInt(MainMenuActivity.mFavouritesEnd)+20);
			}else{ // Giftcard Purchase
				MainMenuActivity.mIsfriendfavourite = false;
				mContext.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						for(int i=0 ; i< WebServiceStaticArrays.mOffsetFavouriteStores.size() ; i++){
							POJOStoreInfo mStoreinfo = (POJOStoreInfo) WebServiceStaticArrays.mOffsetFavouriteStores.get(i);
							WebServiceStaticArrays.mFavouriteStoreDetails.add(Integer.parseInt(StoreInformation.mFavouritesStart)+i,mStoreinfo);
							if(!mClassName.equalsIgnoreCase("Favorites")&&i==0){ // Condition true if control from mobile pay step1 and move the first geopoint to mapview
								//mMapView.getController().animateTo(mStoreinfo.mGeoPoint);
								if(mStoreinfo.mStoreCoordinates != null) 
								mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mStoreinfo.mStoreCoordinates));
							}
						}
							
						if(mCheckRefresh.equalsIgnoreCase("RefreshAdapter") && mFavouriteAdapter != null){
							mFavouriteAdapter.notifyDataSetChanged();
						}else{
							mFavouriteAdapter = new Favorites_Adapter(mContext,WebServiceStaticArrays.mFavouriteStoreDetails,mClassName,mPageFlag);
							mFavouritesList.setAdapter(mFavouriteAdapter);				
						}		
					}
				});

				if(mFooterLayout != null && mFavouritesList.getFooterViewsCount() !=0 && mFavouritesList.getAdapter() != null){
					mFavouritesList.removeFooterView(mFooterLayout);
				}
				MainMenuActivity.mFavouritesStart = MainMenuActivity.mFavouritesEnd;
				MainMenuActivity.mFavouritesEnd = String.valueOf(Integer.parseInt(MainMenuActivity.mFavouritesEnd)+20);
				
				if(this.mClassName.equalsIgnoreCase("CardPurchase")){
					if(this.mConnectivityCheck.ConnectivityCheck(this.mContext)){
			        	mGoogleMap.clear();
			        	for(int i=0;i<WebServiceStaticArrays.mFavouriteStoreDetails.size();i++){
			        		POJOStoreInfo obj = (POJOStoreInfo) WebServiceStaticArrays.mFavouriteStoreDetails.get(i);
			        		if(obj.mStoreCoordinates!=null){
			        			storeCount=storeCount+1;
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
			        		}
			        	}
				        if(storeCount == 0){
				        	Toast.makeText(mContext, "No stores Available", Toast.LENGTH_SHORT).show();
				        }
				      }else{ 
						Toast.makeText(this.mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
					}
				}
			}
			
		}
		progressdialog.dismiss();
	}

	private void alertBox_service(String title, String msg) {
		// TODO Auto-generated method stub
		AlertDialog.Builder service_alert = new AlertDialog.Builder(mContext);
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