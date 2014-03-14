package com.us.zoupons.android.asyncthread_class;

import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.us.zoupons.LikeCountSortingClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.AddFavorite_ClassVariables;
import com.us.zoupons.classvariables.POJOStoreInfo;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.StoreLocator_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.listview_inflater_classes.Favorites_Adapter;
import com.us.zoupons.listview_inflater_classes.LocationShopList_Adapter;
import com.us.zoupons.listview_inflater_classes.ShopNearestList_Adapter;
import com.us.zoupons.shopper.favorite.FriendStoresLikeCountSortingClass;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.location.Location;
import com.us.zoupons.shopper.location.LocationStoresLikeCountSortingClass;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to communicate with webservice for adding/removing favorite store
 *
 */

public class AddorRemoveFavoriteAsynchThread extends AsyncTask<String, String, String>{

	private Context mContext;
	private ProgressDialog mProgressdialog=null;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private ListView mListView;
	private String mGetResponse=null;
	private String mParsingResponse,mPageFlag;	//flag to differentiate homepage and mainmenu page call

	public AddorRemoveFavoriteAsynchThread(Context context){
		this.mContext=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	
	public AddorRemoveFavoriteAsynchThread(Context context,ListView listview){
		this.mContext=context;
		this.mListView = listview;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
 
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			mPageFlag=params[2];
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				mGetResponse=mZouponswebservice.addFavorites(params[0], params[1]);	//check
				
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=mParsingclass.parseAddFavoriteXmlData(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						for(int i=0;i<WebServiceStaticArrays.mStoresAddFavorite.size();i++){
							final AddFavorite_ClassVariables parsedobjectvalues = (AddFavorite_ClassVariables) WebServiceStaticArrays.mStoresAddFavorite.get(i);
							if(!parsedobjectvalues.mMessage.equals("")){
								//result="No Stores Available.";
								result = parsedobjectvalues.mMessage;
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
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try{
			if(result.equals("nonetwork")){
				Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equals("Response Error.")){
				alertBox_service("Information", "Unable to reach service.",mPageFlag);
			}else if(result.equals("No stores Available.")){
				alertBox_service("Information", result,mPageFlag);
			}else {
				if(mPageFlag.equalsIgnoreCase("Favourites")){ // For user Favourite 
					if(result.equals("Removed")){
						WebServiceStaticArrays.mFavouriteStoreDetails.remove(MainMenuActivity.storeposition);
						FavouritesTaskLoader.mFavouriteAdapter.notifyDataSetChanged();
						MainMenuActivity.storeposition = -1;
						MainMenuActivity.mFavouritesTotalCount = String.valueOf(Integer.parseInt(MainMenuActivity.mFavouritesTotalCount)-1);
						alertBox_service("Information", "Store removed from your favorite list",mPageFlag);
					}
				}else if(mPageFlag.equalsIgnoreCase("FriendFavourite")){ // For Friend Favourite
					if(result.equals("Removed")){
						MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						alertBox_service("Information", "Store removed from your favorite list",mPageFlag);
					}else if(result.equals("Added")){
						MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						alertBox_service("Information", "Store added to your favorite list",mPageFlag);
					}
				}else{
					if(result.equals("Added")){
						// Assigning for use it in other right menu items
						RightMenuStoreId_ClassVariables.rightmenu_favourite_status = "yes";
						if(mPageFlag.equals("ShopperHomePage")){
                              // Internally changing the favorite store status
							for(int i=0;i<WebServiceStaticArrays.mDummyStoresLocator.size();i++){
								StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mDummyStoresLocator.get(i);
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									obj.favorite_store="yes";
									WebServiceStaticArrays.mDummyStoresLocator.set(i, obj);
								}
							}
                            ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						}else if(mPageFlag.equals("Location")){
							// Internally changing the favorite store status
							for(int i=0;i<WebServiceStaticArrays.mStoreLocatorList.size();i++){
								StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(i);
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									obj.favorite_store="yes";
									WebServiceStaticArrays.mStoreLocatorList.set(i, obj);
								}
							}
							Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);						
						}else{
							// Internally changing the favorite store status
							for(int i=0;i<WebServiceStaticArrays.mStaticStoreInfo.size();i++){
								POJOStoreInfo obj = (POJOStoreInfo) WebServiceStaticArrays.mStaticStoreInfo.get(i);
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									obj.favorite_store="yes";
									WebServiceStaticArrays.mStaticStoreInfo.set(i, obj);
								}
							}
							MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						}
						alertBox_service("Information", "Store added to your favorite list",mPageFlag);
					}else if(result.equals("Removed")){
						//Assigning for use it in other right menu items
						RightMenuStoreId_ClassVariables.rightmenu_favourite_status = "no";
						if(mPageFlag.equals("ShopperHomePage")){
							// Internally changing the favorite store status
							for(int i=0;i<WebServiceStaticArrays.mDummyStoresLocator.size();i++){
								StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mDummyStoresLocator.get(i);
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									obj.favorite_store="no";
									WebServiceStaticArrays.mDummyStoresLocator.set(i, obj);
								}
							}

							ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}else if(mPageFlag.equals("Location")){
							// Internally changing the favorite store status
							for(int i=0;i<WebServiceStaticArrays.mStoreLocatorList.size();i++){
								StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(i);
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									obj.favorite_store="no";
									WebServiceStaticArrays.mStoreLocatorList.set(i, obj);
								}
							}

							Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}else{
							// Internally changing the favorite store status
							for(int i=0;i<WebServiceStaticArrays.mStaticStoreInfo.size();i++){
								POJOStoreInfo obj = (POJOStoreInfo) WebServiceStaticArrays.mStaticStoreInfo.get(i);
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									obj.favorite_store="no";
									WebServiceStaticArrays.mStaticStoreInfo.set(i, obj);
								}
							}
							MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}
						alertBox_service("Information", "Store removed from your favorite list",mPageFlag);
					}
				}
			}
			mProgressdialog.dismiss();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* To show alert pop up with respective message */
	public void alertBox_service(final String title,final String msg,final String pageflag){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equalsIgnoreCase("Store removed from your favorite list")&&pageflag.equalsIgnoreCase("Favourites")){
					MenuOutClass.WHOLE_MENUOUT=false;
					int menuWidth = MainMenuActivity.sLeftMenu.getMeasuredWidth();
					// Ensure menu is visible
					MainMenuActivity.sLeftMenu.setVisibility(View.VISIBLE);
					int left = menuWidth;
					MainMenuActivity.sScrollView.smoothScrollTo(left, 0);
					MainMenuActivity.mMainMenuActivityFreezeView.setVisibility(View.GONE);
				}else if(msg.equalsIgnoreCase("Store removed from your favorite list")&&(pageflag.equalsIgnoreCase("ShopperHomePage")||pageflag.equalsIgnoreCase("CardPurchase"))){
					for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
						if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
							obj.favorite_store="No";
							if(obj.storesortflag==1){//Success if Retail Preffered
								obj.like_count=String.valueOf(Integer.parseInt(obj.like_count)-1);
							}
							WebServiceStaticArrays.mStoresLocator.set(i,obj);
						}
					}
					// Sorting store list according to Zoupons sort Order
					Collections.sort(WebServiceStaticArrays.mStoresLocator, new LikeCountSortingClass());
					//Clone mStoresLocator to mDummyStoresLocator to fix the location id issue while selecting listitem after change add or remove any items in listview
					WebServiceStaticArrays.mDummyStoresLocator.clear();
					for(StoreLocator_ClassVariables obj : WebServiceStaticArrays.mStoresLocator){
						try {
							WebServiceStaticArrays.mDummyStoresLocator.add(obj.clone());
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
					
					mListView.setAdapter(new ShopNearestList_Adapter(mContext));
				}else if(msg.equalsIgnoreCase("Store added to your favorite list")&&(pageflag.equalsIgnoreCase("ShopperHomePage")||pageflag.equalsIgnoreCase("CardPurchase"))){
					for(int i=0;i<WebServiceStaticArrays.mStoresLocator.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoresLocator.get(i);
						if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
							obj.favorite_store="Yes";
							if(obj.storesortflag==1){//Success if Retail Preffered
								obj.like_count=String.valueOf(Integer.parseInt(obj.like_count)+1);
							}
							WebServiceStaticArrays.mStoresLocator.set(i,obj);
						}
					}
					// Sorting store list according to Zoupons sort Order
					Collections.sort(WebServiceStaticArrays.mStoresLocator, new LikeCountSortingClass());
					
					//Clone mStoresLocator to mDummyStoresLocator to fix the location id issue while selecting listitem after change add or remove any items in listview
					WebServiceStaticArrays.mDummyStoresLocator.clear();
					for(StoreLocator_ClassVariables obj : WebServiceStaticArrays.mStoresLocator){
						try {
							WebServiceStaticArrays.mDummyStoresLocator.add(obj.clone());
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
					
					mListView.setAdapter(new ShopNearestList_Adapter(mContext));
				}else if(msg.equalsIgnoreCase("Store added to your favorite list")&& pageflag.equalsIgnoreCase("FriendFavourite")){
					for(int i=0;i<WebServiceStaticArrays.mFavouriteFriendStoreDetails.size();i++){
						POJOStoreInfo obj = (POJOStoreInfo) WebServiceStaticArrays.mFavouriteFriendStoreDetails.get(i);
						if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
							obj.favorite_store="Yes";
							if(obj.storesortflag==1){//Success if Retail Preffered
								obj.like_count=String.valueOf(Integer.parseInt(obj.like_count)+1);
							}
							WebServiceStaticArrays.mFavouriteFriendStoreDetails.set(i,obj);
						}
					}
					// Sorting store list according to Zoupons sort Order
					Collections.sort(WebServiceStaticArrays.mFavouriteFriendStoreDetails, new FriendStoresLikeCountSortingClass());
					MainMenuActivity.mFavoritesListView.setAdapter(new Favorites_Adapter(mContext,WebServiceStaticArrays.mFavouriteFriendStoreDetails,"Favorites","Favorites"));
				}else if(msg.equalsIgnoreCase("Store removed from your favorite list")&&pageflag.equalsIgnoreCase("FriendFavourite")){
					for(int i=0;i<WebServiceStaticArrays.mFavouriteFriendStoreDetails.size();i++){
						POJOStoreInfo obj = (POJOStoreInfo) WebServiceStaticArrays.mFavouriteFriendStoreDetails.get(i);
						if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
							obj.favorite_store="No";
							if(obj.storesortflag==1){//Success if Retail Preffered
								obj.like_count=String.valueOf(Integer.parseInt(obj.like_count)-1);
							}
							WebServiceStaticArrays.mFavouriteFriendStoreDetails.set(i,obj);
						}
					}
					// Sorting store list according to Zoupons sort Order
					Collections.sort(WebServiceStaticArrays.mFavouriteFriendStoreDetails, new FriendStoresLikeCountSortingClass());
					MainMenuActivity.mFavoritesListView.setAdapter(new Favorites_Adapter(mContext,WebServiceStaticArrays.mFavouriteFriendStoreDetails,"Favorites","Favorites"));
				}else if(msg.equalsIgnoreCase("Store removed from your favorite list")&&pageflag.equalsIgnoreCase("Location")){
					for(int i=0;i<WebServiceStaticArrays.mStoreLocatorList.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(i);
						if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
							obj.favorite_store="No";
							if(obj.storesortflag==1){//Success if Retail Preffered
								obj.like_count=String.valueOf(Integer.parseInt(obj.like_count)-1);
							}
							WebServiceStaticArrays.mStoreLocatorList.set(i,obj);
						}
					}
					// Sorting store list according to Zoupons sort Order
					Collections.sort(WebServiceStaticArrays.mStoreLocatorList, new LocationStoresLikeCountSortingClass());
					mListView.setAdapter(new LocationShopList_Adapter(mContext));
				}else if(msg.equalsIgnoreCase("Store added to your favorite list")&&pageflag.equalsIgnoreCase("Location")){
					for(int i=0;i<WebServiceStaticArrays.mStoreLocatorList.size();i++){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(i);
						if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
							obj.favorite_store="Yes";
							if(obj.storesortflag==1){//Success if Retail Preffered
								obj.like_count=String.valueOf(Integer.parseInt(obj.like_count)+1);
							}
							WebServiceStaticArrays.mStoreLocatorList.set(i,obj);
						}
					}
					// Sorting store list according to Zoupons sort Order
					Collections.sort(WebServiceStaticArrays.mStoreLocatorList, new LocationStoresLikeCountSortingClass());
					mListView.setAdapter(new LocationShopList_Adapter(mContext));
				}
			}
		});
		service_alert.show();
	}
	
}