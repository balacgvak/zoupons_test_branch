package com.us.zoupons.android.AsyncThreadClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.MapViewCenter;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class StoreLocatorTask extends AsyncTask<String,String,String>{
	String TAG = "StoreLocator";
	Context ctx;
	String currentLatitude, currentLongtitude ,EventFlag,StoreID;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private ProgressDialog progressdialog=null;
	private NetworkCheck mConnectionAvailabilityChecking = null;
	String mLocatorResponse = "",mLocatorParsingResponse ="";
	int storeCount;
	ListView mLocationListView;
	String mClassName;
	String mPageFlag;

	RelativeLayout mMapViewContainer;
	LinearLayout mListViewHolder;
	ViewGroup mMiddleView;
	TextView mMenuBarListText;
	ImageView mMenuBarListImage;
	int mBackgroundResource;
	String mProgressStatus; 
	private GoogleMap mGoogleMap;

	/*
	 * Constructor for Location
	 * **/
	public StoreLocatorTask(Context context, String mLatitude,
			String mLonggitude, String mStoreId,GoogleMap googlemap,ListView locationlistview,String classname,String pageflag,
			RelativeLayout mapviewcontainer,LinearLayout listview,ViewGroup middleview,TextView menubarlisttext,ImageView menubarlistimage,int backgroundresource,String progressstatus) {
		this.ctx = context;
		currentLatitude = mLatitude;
		currentLongtitude = mLonggitude;
		EventFlag = "";
		StoreID =mStoreId;
		mClassName=classname;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		mProgressStatus = progressstatus;

		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			progressdialog=new ProgressDialog(this.ctx);
			progressdialog.setCancelable(false);
			progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressdialog.setProgress(0);
			progressdialog.setMax(100);
		}
		this.mGoogleMap = googlemap;
		this.mLocationListView=locationlistview;
		this.mPageFlag=pageflag;
		
		mMapViewContainer=mapviewcontainer;
		mListViewHolder=listview;
		mMiddleView=middleview;
		mMenuBarListText=menubarlisttext;
		mMenuBarListImage=menubarlistimage;
		mBackgroundResource=backgroundresource;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);

		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			//Start a status dialog
			progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
			//MapOverlays.mapOverlays.clear();
		}

	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				mLocatorResponse = zouponswebservice.mStoreLocator(params[0],params[1],EventFlag,StoreID,String.valueOf(MapViewCenter.LoginCurrentLocationLatitude),String.valueOf(MapViewCenter.LoginCurrentLocationLongitude),mClassName);
				if(!mLocatorResponse.equals("failure") && !mLocatorResponse.equals("noresponse")){
					mLocatorParsingResponse = parsingclass.ParseLocatorResponse(mLocatorResponse);
					if(mLocatorParsingResponse.equalsIgnoreCase("success")){
						result ="success";				
					}else if(mLocatorParsingResponse.equalsIgnoreCase("failure")){
						Log.i(TAG,"Error");
						result="Response Error.";
					}else if(mLocatorParsingResponse.equalsIgnoreCase("norecords")){
						Log.i(TAG,"No Records");
						result="No Records";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch (Exception e) {
			// TODO: handle exception
			result="Thread Error";
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equals("success")){
			try{
				//Load Current Location while activity loading
				if(this.mConnectionAvailabilityChecking.ConnectivityCheck(this.ctx)){

					mGoogleMap.clear();
					
					if(this.mClassName.equalsIgnoreCase("location")){
						MenuUtilityClass.shopListView(this.ctx, mLocationListView, "location", this.mClassName,"",false,this.mMapViewContainer, this.mListViewHolder, this.mMiddleView, this.mMenuBarListText, this.mMenuBarListImage,mGoogleMap);
					}else{
						//Loading store with different location in listview
						MenuUtilityClass.shopListView(this.ctx, mLocationListView, "location",this.mClassName,"",false,this.mPageFlag);	//last two arguments are used to load listview with offset value
					}
					
					for(int i=0;i<WebServiceStaticArrays.mStoreLocatorList.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(i);
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
							
							if(this.mClassName.equalsIgnoreCase("location")){
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									CameraPosition mSelectedStorePosition =  new CameraPosition.Builder().target(obj.storeCoordinates).zoom(13).build();
									mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(mSelectedStorePosition));
								}
							}
						}else{
							Log.i(TAG,"No GeoPoint StoreName: "+obj.storeName);
						}
					}
					if(storeCount==0){
						mGoogleMap.clear();
						Toast.makeText(ctx, "No stores Available", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(this.ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e){
				Log.i(TAG,"Issue occur while callout loading.");
				e.printStackTrace();
			}
		}
		if(mProgressStatus.equalsIgnoreCase("PROGRESS")){
			progressdialog.dismiss();
		}
		super.onPostExecute(result);
	}

	private void alertBox_service(String title, String msg) {
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