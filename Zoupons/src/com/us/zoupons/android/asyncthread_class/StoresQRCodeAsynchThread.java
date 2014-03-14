/**
 * 
 */
package com.us.zoupons.android.asyncthread_class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.OpenMenu;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.CurrentLocation;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.StoreLocator_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.mobilepay.CardPurchase;
import com.us.zoupons.mobilepay.MobilePay;
import com.us.zoupons.shopper.cards.StoreCardDetails;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * Class to load qrcode store values from webservice
 *
 */
public class StoresQRCodeAsynchThread extends AsyncTask<String, String, String>{

	private Activity ctx;
	private ProgressDialog progressdialog=null;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse;
	private GoogleMap mGoogleMap;
	private ListView mShopListView;
	private double mMapViewRadius;
	private String mClassName;
	private String mPageFlag;
	private String mDeviceLatitude,mDeviceLongitude;
	
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

				if(ShopperHomePage.sStoreDetailsStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.mStoresLocator.clear();
				}else if(CardPurchase.mZPAYStoreDetailsStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.mStoresLocator.clear();
				}

				mGetResponse=zouponswebservice.store_locator(CurrentLocation.CurrentLocation_Latitude, CurrentLocation.CurrentLocation_Longitude, "", "", params[0],this.mMapViewRadius,"","0",mDeviceLatitude,mDeviceLongitude);	//new
				
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=parsingclass.parseStore_Locator(mGetResponse,this.mClassName);
					if(mParsingResponse.equalsIgnoreCase("success")){
						for(int i=0;i<WebServiceStaticArrays.mOffsetStoreDetails.size();i++){
							StoreLocator_ClassVariables parsedobjectvalues = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
							if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("Search type or term not available !")*/){
								result="No stores Available.";
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
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", result);
		}else if(result.equals("No stores Available.")){
			alertBox_service("Information", result);
		}else if(result.equals("success")){
			try{
				//Load Current Location while activity loading
				if(this.mConnectionAvailabilityChecking.ConnectivityCheck(this.ctx)){
					mGoogleMap.clear();
					if(mClassName.equalsIgnoreCase("ShopperHomePage")){
						ctx.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
									StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(ShopperHomePage.sStoreDetailsStart)+i,mStoreinfo);
								}	
							}
						});
					}else if(mClassName.equalsIgnoreCase("CardPurchase")){
						ctx.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								for(int i = 0 ; i< WebServiceStaticArrays.mOffsetStoreDetails.size() ; i++){
									StoreLocator_ClassVariables mStoreinfo = (StoreLocator_ClassVariables) WebServiceStaticArrays.mOffsetStoreDetails.get(i);
									WebServiceStaticArrays.mStoresLocator.add(Integer.parseInt(CardPurchase.mZPAYStoreDetailsStart)+i,mStoreinfo);
								}	
							}
						});
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
			        		CameraPosition StoreLocation = new CameraPosition.Builder().target(store_obj.storeCoordinates).zoom(13).build();
			        		mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(StoreLocation));
						}else{
							
						}

						StoreLocator_ClassVariables obj1 = null;
						if(WebServiceStaticArrays.mStoresLocator.size()>0){
							obj1 = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(0);
						}

						if(mClassName.equals("ShopperHomePage")){
							//Check Right Menu Enable or disable status
							RightMenuStoreId_ClassVariables.mStoreID=obj1.id;
							RightMenuStoreId_ClassVariables.mStoreName=obj1.storeName;
							RightMenuStoreId_ClassVariables.mLocationId=obj1.location_id;
							RightMenuStoreId_ClassVariables.rightmenu_favourite_status=obj1.favorite_store;
							if(obj1.deviceDistance.equalsIgnoreCase("-1")){
								RightMenuStoreId_ClassVariables.mStoreLocation = "online store";
							}else{
								RightMenuStoreId_ClassVariables.mStoreLocation = obj1.addressLine1 + "\n" + obj1.city+", "+obj1.state+" "+obj1.zipcode;	
							}
							if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
								ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
							}else{
								ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
							}
							MenuUtilityClass.SetRightMenuStatus(obj1, this.ctx);
							OpenMenu.toOpenRightMenu("ShopperHomePage");		// Function to open RightMenu
						}else{
							//To check this store has giftcards
							if(mPageFlag.equalsIgnoreCase("giftcard")){
								if(obj1.rightmenu_giftcards_flag.equalsIgnoreCase("yes")){
									//Move to Giftcards and zcards page
									Intent intent_giftcard_zcard = new Intent(this.ctx,StoreCardDetails.class);
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
									Intent intent_step2 = new Intent(this.ctx,MobilePay.class);
									intent_step2.putExtra("datasourcename", "zpay");
									this.ctx.startActivity(intent_step2);
								}else{
									// alert shown
									alertBox_service("Information","This Store does't have payment option.");
								}
							}
						}
					}else if(WebServiceStaticArrays.mStoresLocator.size()==0){
						alertBox_service("Information", "No stores Available.");
						mGoogleMap.clear();
					}
				}else{
					Toast.makeText(this.ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
				}
				MenuUtilityClass.shopListView(this.ctx, mShopListView, "currentlocationnearstore",this.mClassName,"FromQRcode",false,mPageFlag);	//new
			}catch(Exception e){
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

	
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		
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