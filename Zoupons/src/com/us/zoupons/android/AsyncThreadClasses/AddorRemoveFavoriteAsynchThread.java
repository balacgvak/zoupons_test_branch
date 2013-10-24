package com.us.zoupons.android.AsyncThreadClasses;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.FriendStoresLikeCountSortingClass;
import com.us.zoupons.LikeCountSortingClass;
import com.us.zoupons.Location;
import com.us.zoupons.LocationStoresLikeCountSortingClass;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.SlidingView;
import com.us.zoupons.ClassVariables.AddFavorite_ClassVariables;
import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.android.listview.inflater.classes.Favorites_Adapter;
import com.us.zoupons.android.listview.inflater.classes.LocationShopList_Adapter;
import com.us.zoupons.android.listview.inflater.classes.ShopNearestList_Adapter;

public class AddorRemoveFavoriteAsynchThread extends AsyncTask<String, String, String>{

	Context ctx;
	String TAG="CheckFavoriteAsynchThread";
	private ProgressDialog progressdialog=null;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	String pageFlag;	//flag to differentiate homepage and mainmenu page call

	public AddorRemoveFavoriteAsynchThread(Context context){
		this.ctx=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
 
	@Override
	protected void onPostExecute(String result) {
		try{
			if(result.equals("nonetwork")){
				Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equals("Response Error.")){
				alertBox_service("Information", "Unable to reach service.",pageFlag);
			}else if(result.equals("No Stores Available.")){
				alertBox_service("Information", result,pageFlag);
			}else {
				if(pageFlag.equalsIgnoreCase("Favourites")){ // For user Favourite 
					if(result.equals("Removed")){
						WebServiceStaticArrays.mFavouriteStoreDetails.remove(MainMenuActivity.storeposition);
						FavouritesTaskLoader.mFavouriteAdapter.notifyDataSetChanged();
						MainMenuActivity.storeposition = -1;
						MainMenuActivity.mFavouritesTotalCount = String.valueOf(Integer.parseInt(MainMenuActivity.mFavouritesTotalCount)-1);
						alertBox_service("Information", "Successfully removed from favorite store",pageFlag);
					}
				}else if(pageFlag.equalsIgnoreCase("FriendFavourite")){ // For Friend Favourite
					if(result.equals("Removed")){
						MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						alertBox_service("Information", "Successfully removed from favorite store",pageFlag);
					}else if(result.equals("Added")){
						MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						alertBox_service("Information", "Successfully added as favorite store",pageFlag);
					}
				}else{
					if(result.equals("Added")){
						// Assigning for use it in other right menu items
						RightMenuStoreId_ClassVariables.rightmenu_favourite_status = "yes";
						if(pageFlag.equals("SlidingView")){

							for(int i=0;i<WebServiceStaticArrays.mDummyStoresLocator.size();i++){
								StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mDummyStoresLocator.get(i);
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									obj.favorite_store="yes";
									WebServiceStaticArrays.mDummyStoresLocator.set(i, obj);
								}
							}

							SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						}else if(pageFlag.equals("Location")){

							for(int i=0;i<WebServiceStaticArrays.mStoreLocatorList.size();i++){
								StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(i);
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									obj.favorite_store="yes";
									WebServiceStaticArrays.mStoreLocatorList.set(i, obj);
								}
							}

							Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);						
						}else{

							for(int i=0;i<WebServiceStaticArrays.mStaticStoreInfo.size();i++){
								POJOStoreInfo obj = (POJOStoreInfo) WebServiceStaticArrays.mStaticStoreInfo.get(i);
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									obj.favorite_store="yes";
									WebServiceStaticArrays.mStaticStoreInfo.set(i, obj);
								}
							}

							MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						}
						alertBox_service("Information", "Successfully added as favorite store",pageFlag);
					}else if(result.equals("Removed")){
						//Assigning for use it in other right menu items
						RightMenuStoreId_ClassVariables.rightmenu_favourite_status = "no";
						if(pageFlag.equals("SlidingView")){
							for(int i=0;i<WebServiceStaticArrays.mDummyStoresLocator.size();i++){
								StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mDummyStoresLocator.get(i);
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									obj.favorite_store="no";
									WebServiceStaticArrays.mDummyStoresLocator.set(i, obj);
								}
							}

							SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}else if(pageFlag.equals("Location")){

							for(int i=0;i<WebServiceStaticArrays.mStoreLocatorList.size();i++){
								StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(i);
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									obj.favorite_store="no";
									WebServiceStaticArrays.mStoreLocatorList.set(i, obj);
								}
							}

							Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}else{
							for(int i=0;i<WebServiceStaticArrays.mStaticStoreInfo.size();i++){
								POJOStoreInfo obj = (POJOStoreInfo) WebServiceStaticArrays.mStaticStoreInfo.get(i);
								if(obj.location_id.equalsIgnoreCase(RightMenuStoreId_ClassVariables.mLocationId)){
									obj.favorite_store="no";
									WebServiceStaticArrays.mStaticStoreInfo.set(i, obj);
								}
							}
							MainMenuActivity.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}
						alertBox_service("Information", "Successfully removed from favorite store",pageFlag);
					}
				}
			}
			progressdialog.dismiss();
			super.onPostExecute(result);
		}catch (Exception e) {
			e.printStackTrace();
		}
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

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			pageFlag=params[2];
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				mGetResponse=zouponswebservice.addFavorites(params[0], params[1]);	//check
				
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=parsingclass.parseAddFavoriteXmlData(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("success")){
						for(int i=0;i<WebServiceStaticArrays.mStoresAddFavorite.size();i++){
							final AddFavorite_ClassVariables parsedobjectvalues = (AddFavorite_ClassVariables) WebServiceStaticArrays.mStoresAddFavorite.get(i);
							if(!parsedobjectvalues.mMessage.equals("")){
								//result="No Stores Available.";
								result = parsedobjectvalues.mMessage;
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

	public void alertBox_service(final String title,final String msg,final String pageflag){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equalsIgnoreCase("Successfully removed from favorite store")&&pageflag.equalsIgnoreCase("Favourites")){
					MenuOutClass.WHOLE_MENUOUT=false;
					int menuWidth = MainMenuActivity.leftMenu.getMeasuredWidth();
					// Ensure menu is visible
					MainMenuActivity.leftMenu.setVisibility(View.VISIBLE);
					int left = menuWidth;
					MainMenuActivity.scrollView.smoothScrollTo(left, 0);
					MainMenuActivity.mMainMenuActivityFreezeView.setVisibility(View.GONE);
				}else if(msg.equalsIgnoreCase("Successfully removed from favorite store")&&(pageflag.equalsIgnoreCase("SlidingView")||pageflag.equalsIgnoreCase("zpay_step1"))){
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
					
					Collections.sort(WebServiceStaticArrays.mStoresLocator, new LikeCountSortingClass());
					Collections.reverse(WebServiceStaticArrays.mStoresLocator);
					
					//Clone mStoresLocator to mDummyStoresLocator to fix the location id issue while selecting listitem after change add or remove any items in listview
					WebServiceStaticArrays.mDummyStoresLocator.clear();
					for(StoreLocator_ClassVariables obj : WebServiceStaticArrays.mStoresLocator){
						try {
							WebServiceStaticArrays.mDummyStoresLocator.add(obj.clone());
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
					
					SlidingView.mShopListView.setAdapter(new ShopNearestList_Adapter(ctx));
				}else if(msg.equalsIgnoreCase("Successfully added as favorite store")&&(pageflag.equalsIgnoreCase("SlidingView")||pageflag.equalsIgnoreCase("zpay_step1"))){
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
					
					Collections.sort(WebServiceStaticArrays.mStoresLocator, new LikeCountSortingClass());
					Collections.reverse(WebServiceStaticArrays.mStoresLocator);
					
					//Clone mStoresLocator to mDummyStoresLocator to fix the location id issue while selecting listitem after change add or remove any items in listview
					WebServiceStaticArrays.mDummyStoresLocator.clear();
					for(StoreLocator_ClassVariables obj : WebServiceStaticArrays.mStoresLocator){
						try {
							WebServiceStaticArrays.mDummyStoresLocator.add(obj.clone());
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
					
					SlidingView.mShopListView.setAdapter(new ShopNearestList_Adapter(ctx));
				}else if(msg.equalsIgnoreCase("Successfully added as favorite store")&& pageflag.equalsIgnoreCase("FriendFavourite")){
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
					Collections.sort(WebServiceStaticArrays.mFavouriteFriendStoreDetails, new FriendStoresLikeCountSortingClass());
					Collections.reverse(WebServiceStaticArrays.mFavouriteFriendStoreDetails);
					MainMenuActivity.mFavoritesListView.setAdapter(new Favorites_Adapter(ctx,WebServiceStaticArrays.mFavouriteFriendStoreDetails,"Favorites","Favorites"));
				}else if(msg.equalsIgnoreCase("Successfully removed from favorite store")&&pageflag.equalsIgnoreCase("FriendFavourite")){
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
					Collections.sort(WebServiceStaticArrays.mFavouriteFriendStoreDetails, new FriendStoresLikeCountSortingClass());
					Collections.reverse(WebServiceStaticArrays.mFavouriteFriendStoreDetails);
					MainMenuActivity.mFavoritesListView.setAdapter(new Favorites_Adapter(ctx,WebServiceStaticArrays.mFavouriteFriendStoreDetails,"Favorites","Favorites"));
				}else if(msg.equalsIgnoreCase("Successfully removed from favorite store")&&pageflag.equalsIgnoreCase("location")){
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
					Collections.sort(WebServiceStaticArrays.mStoreLocatorList, new LocationStoresLikeCountSortingClass());
					Collections.reverse(WebServiceStaticArrays.mStoreLocatorList);
					Location.mLocationListView.setAdapter(new LocationShopList_Adapter(ctx));
				}else if(msg.equalsIgnoreCase("Successfully added as favorite store")&&pageflag.equalsIgnoreCase("location")){
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
					Collections.sort(WebServiceStaticArrays.mStoreLocatorList, new LocationStoresLikeCountSortingClass());
					Collections.reverse(WebServiceStaticArrays.mStoreLocatorList);
					Location.mLocationListView.setAdapter(new LocationShopList_Adapter(ctx));
				}
			}
		});
		service_alert.show();
	}

	public static void printLikeCount(ArrayList<StoreLocator_ClassVariables> StoreLocator){
		for (StoreLocator_ClassVariables e : StoreLocator) { 
			System.out.println("StoreName->" + e.storeName + " LikeCount-> " + e.like_count + " Distance-> " + e.distance); 
		}
	}
}