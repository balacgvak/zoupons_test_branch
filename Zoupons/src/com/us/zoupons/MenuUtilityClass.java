package com.us.zoupons;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.us.zoupons.ClassVariables.CardOnFiles_ClassVariables;
import com.us.zoupons.ClassVariables.POJOFBfriendListData;
import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.StoreLocator_ClassVariables;
import com.us.zoupons.ClassVariables.StoresLocation_ClassVariables;
import com.us.zoupons.ClassVariables.StoresQRCode_ClassVariables;
import com.us.zoupons.Communication.CustomChatScrollListner;
import com.us.zoupons.Communication.CustomScrollDetectListener;
import com.us.zoupons.Communication.TalkToUs_ContactStore_Adapter;
import com.us.zoupons.Coupons.CouponDetail;
import com.us.zoupons.Coupons.CouponListAdapter;
import com.us.zoupons.FlagClasses.AccountLoginFlag;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.FlagClasses.ZPayFlag;
import com.us.zoupons.GiftCards.GiftCards;
import com.us.zoupons.GiftCards.POJOAllGiftCards;
import com.us.zoupons.GiftCards.TransactionHistory_Inflater;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.android.listview.inflater.classes.CardOnFiles_Adapter;
import com.us.zoupons.android.listview.inflater.classes.GiftCards_Adapter;
import com.us.zoupons.android.listview.inflater.classes.LocationShopList_Adapter;
import com.us.zoupons.android.listview.inflater.classes.ManageCards_Adapter;
import com.us.zoupons.android.listview.inflater.classes.ShopBrowseList_Adapter;
import com.us.zoupons.android.listview.inflater.classes.ShopList_Adapter;
import com.us.zoupons.android.listview.inflater.classes.ShopNearestList_Adapter;
import com.us.zoupons.android.listview.inflater.classes.ShopQrCodeList_Adapter;
import com.us.zoupons.cards.StoreGiftCardDetails;
import com.us.zoupons.friends.Friends;
import com.us.zoupons.storeowner.DashBoard.StoreOwnerDashBoard;
import com.us.zoupons.storeowner.DashBoard.StoreOwnerDashBoard_Adapter;
import com.us.zoupons.storeowner.Employees.StoreOwnerEmployees_Adapter;
import com.us.zoupons.storeowner.Employees.StoreOwner_EmployeeDetails;
import com.us.zoupons.storeowner.HomePage.StoreOwnerHomePage_Adapter;
import com.us.zoupons.storeowner.Locations.StoreOwner_AddLocations;
import com.us.zoupons.storeowner.PointOfSale.PointOfsalePart1Adapter;
import com.us.zoupons.storeowner.StoreInformation.StoreOwnerStoreInformation_Adapter;
import com.us.zoupons.zpay.Step2_ManageCards;
import com.us.zoupons.zpay.ZpayStep2SearchEnable;

public class MenuUtilityClass{
	Context ctx;
	
	public static String mGiftCardId="",mGiftCardPurchaseId="",mGiftcardPurchaseDate="";
	public static double mGiftCardBalanceAmount,mGiftcardFaceValue;
	public static String mGiftCard_StoreId="",mGiftCard_StoreName="",mGiftCard_Type="",mGiftCardStoreLocationId="";
	public static int mChoosedGiftcardPosition;
	public static ShopNearestList_Adapter mStoreListAdapter;
	public static TalkToUs_ContactStore_Adapter mTalkToUsContactStoreAdapter;
	public static GiftCards_Adapter myGiftcardsAdapter = null;

	private MenuUtilityClass(){
	}

	public static void shopListView(final Context context, ListView listView,final String menubarStoreFlag,final String classname,String sourcelist,boolean isForRefreshAdapter,final String mPageFlag) {
		if(menubarStoreFlag.equals("search")){
			listView.setAdapter(new ShopList_Adapter(context));
		}else if(menubarStoreFlag.equals("currentlocationnearstore")){
			listView.setTag(sourcelist);
			if(isForRefreshAdapter && mStoreListAdapter != null){
				mStoreListAdapter.notifyDataSetChanged();
				Log.i("adapter", "refresh");
			}else{
				Log.i("adapter", "set");
				mStoreListAdapter = new ShopNearestList_Adapter(context);
				listView.setAdapter(mStoreListAdapter);	
			}
		}else if(menubarStoreFlag.equals("qrcodesearch")){
			listView.setAdapter(new ShopQrCodeList_Adapter(context));
		}else if(menubarStoreFlag.equals("browse")){
			listView.setAdapter(new ShopBrowseList_Adapter(context));
		}else if(menubarStoreFlag.equals("location")){
			listView.setAdapter(new LocationShopList_Adapter(context));
		}

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if(!classname.equals("SlidingView")&&!classname.equals("location")){
					// To enable search text in step 2
					ZpayStep2SearchEnable.searchEnableFlag = true;
				}
				if(menubarStoreFlag.equals("search")){
					StoresLocation_ClassVariables obj = (StoresLocation_ClassVariables) WebServiceStaticArrays.mStoresLocation.get(position);

					RightMenuStoreId_ClassVariables.mStoreID=obj.id;
					RightMenuStoreId_ClassVariables.mStoreName=obj.storeName;
					RightMenuStoreId_ClassVariables.mStoreLocation = obj.addresssLine1 + "\n" + obj.city+", "+obj.state+" "+obj.zipCode;
					RightMenuStoreId_ClassVariables.rightmenu_favourite_status = obj.favorite_store;
					RightMenuStoreId_ClassVariables.mSelectedStore_lat=obj.latitude;
					RightMenuStoreId_ClassVariables.mSelectedStore_long=obj.longitude;

					
					if(ZPayFlag.getFlag()>0){

						if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
							SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						}else{
							SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}

						OpenMenu.toOpenRightMenu("SlidingView");		// Function to open RightMenu
					}else{
						Toast.makeText(context, "Zpay Flag true", Toast.LENGTH_SHORT).show();
					}
				}else if(menubarStoreFlag.equals("currentlocationnearstore")){
					if(WebServiceStaticArrays.mStoresLocator.size() > position){
						StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mDummyStoresLocator.get(position);

						RightMenuStoreId_ClassVariables.mStoreID=obj.id;
						RightMenuStoreId_ClassVariables.mStoreName=obj.storeName;
						RightMenuStoreId_ClassVariables.mLocationId=obj.location_id;
						RightMenuStoreId_ClassVariables.rightmenu_favourite_status=obj.favorite_store;
						RightMenuStoreId_ClassVariables.mSelectedStore_lat=obj.latitude;
						RightMenuStoreId_ClassVariables.mSelectedStore_long=obj.longitude;

						
						if(obj.distance.equalsIgnoreCase("-1")){
							RightMenuStoreId_ClassVariables.mStoreLocation = "online";
						}else{
							RightMenuStoreId_ClassVariables.mStoreLocation = obj.addressLine1 + "\n" + obj.city+", "+obj.state+" "+obj.zipcode;	
						}

						if(classname.equals("SlidingView")){
							SlidingView.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
							SlidingView.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);

							if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
								SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
							}else{
								SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
							}

							SetRightMenuStatus(obj,context);
							OpenMenu.toOpenRightMenu("SlidingView");		// Function to open RightMenu
						}else{
							if(mPageFlag.equalsIgnoreCase("giftcard")){
								if(obj.rightmenu_giftcards_flag.equalsIgnoreCase("yes")){
									//Move to Giftcards and zcards page
									Intent intent_giftcard_zcard = new Intent(context,StoreGiftCardDetails.class);
									intent_giftcard_zcard.putExtra("CardType", "Regular");
									intent_giftcard_zcard.putExtra("bothcards", "both");
									context.startActivity(intent_giftcard_zcard);
								}else{
									// alert shown
									alertBox_service(context,"Information","This Store does't have giftcard.");
								}
							}else{
								if(obj.rightmenu_pointofsale_flag.equalsIgnoreCase("yes")){
									//Move to step2
									Intent intent_step2 = new Intent(context,Step2_ManageCards.class);
									intent_step2.putExtra("datasourcename", "zpay");
									context.startActivity(intent_step2);
								}else{
									// alert shown
									alertBox_service(context,"Information","This Store does't have payment option.");
								}
							}
						}
					}
				}else if(menubarStoreFlag.equals("qrcodesearch")){
					StoresQRCode_ClassVariables obj = (StoresQRCode_ClassVariables) WebServiceStaticArrays.mStoresQRCode.get(position);

					RightMenuStoreId_ClassVariables.mStoreID=obj.id;
					RightMenuStoreId_ClassVariables.mStoreName=obj.storeName;
					RightMenuStoreId_ClassVariables.mStoreLocation = obj.city;
					RightMenuStoreId_ClassVariables.rightmenu_favourite_status = obj.favorite_store;
					RightMenuStoreId_ClassVariables.mSelectedStore_lat=obj.latitude;
					RightMenuStoreId_ClassVariables.mSelectedStore_long=obj.longitude;

					if(ZPayFlag.getFlag()>0){

						if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
							SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						}else{
							SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}

						OpenMenu.toOpenRightMenu("SlidingView");		// Function to open RightMenu
					}
				}else if(menubarStoreFlag.equals("browse")){
					StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mDummyStoresLocator.get(position);
					Toast.makeText(context, "Store Name : "+obj.storeName+"\n"+"StoreID : "+obj.id, Toast.LENGTH_SHORT).show();

					RightMenuStoreId_ClassVariables.mStoreID=obj.id;
					RightMenuStoreId_ClassVariables.mStoreName=obj.storeName;
					RightMenuStoreId_ClassVariables.mStoreLocation = obj.city;
					RightMenuStoreId_ClassVariables.rightmenu_favourite_status = obj.favorite_store;
					RightMenuStoreId_ClassVariables.mSelectedStore_lat=obj.latitude;
					RightMenuStoreId_ClassVariables.mSelectedStore_long=obj.longitude;

					if(ZPayFlag.getFlag()>0){
						if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
							SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						}else{
							SlidingView.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}

						OpenMenu.toOpenRightMenu("SlidingView");		// Function to open RightMenu
					}
				}
			}
		});
	}

	/* Location Listview Adapter */
	public static void shopListView(final Context context, ListView listView,final String menubarStoreFlag,final String classname,String sourcelist,boolean isForRefreshAdapter,
			final RelativeLayout mapviewcontainer,final LinearLayout listviewholder,final ViewGroup middleview,final TextView menubarlisttext,final ImageView menubarlistimage,final GoogleMap mapview) {

		listView.setAdapter(new LocationShopList_Adapter(context));

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(!classname.equals("SlidingView")&&!classname.equals("location")){
					// To enable search text in step 2
					ZpayStep2SearchEnable.searchEnableFlag = true;
				}
				StoreLocator_ClassVariables obj = (StoreLocator_ClassVariables) WebServiceStaticArrays.mStoreLocatorList.get(position);
				RightMenuStoreId_ClassVariables.mStoreID=obj.id;
				RightMenuStoreId_ClassVariables.mStoreName=obj.storeName;
				RightMenuStoreId_ClassVariables.mLocationId=obj.location_id;
				RightMenuStoreId_ClassVariables.rightmenu_favourite_status=obj.favorite_store;
				RightMenuStoreId_ClassVariables.mSelectedStore_lat=obj.latitude;
				RightMenuStoreId_ClassVariables.mSelectedStore_long=obj.longitude;
				CameraPosition mSelectedStorePosition =  new CameraPosition.Builder().target(obj.storeCoordinates).zoom(13).build();
              	mapview.animateCamera(CameraUpdateFactory.newCameraPosition(mSelectedStorePosition));
              					
				if(obj.distance.equalsIgnoreCase("-1")){
					RightMenuStoreId_ClassVariables.mStoreLocation = "online";
				}else{
					RightMenuStoreId_ClassVariables.mStoreLocation = obj.addressLine1 + "\n" + obj.city+", "+obj.state+" "+obj.zipcode;	
				}

				SlidingView.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
				SlidingView.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);
				if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
					Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
				}else{
					Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
				}
				SetRightMenuStatus(obj,context);
				OpenMenu.toOpenRightMenu("locations");		// Function to open RightMenu
			}
		});
	}

	public static void managecardsListView(final Context context,ListView listView){
		listView.setAdapter(new ManageCards_Adapter(context));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(context, "position", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static void giftcardsListView(final Activity context,ListView listView, String mCheckAndRefresh,View footerview){
		if(!mCheckAndRefresh.equalsIgnoreCase("")){
			myGiftcardsAdapter.notifyDataSetChanged();
		}else{
			myGiftcardsAdapter = new GiftCards_Adapter(context);
			listView.setAdapter(myGiftcardsAdapter);	
		}
		if(footerview != null && listView.getFooterViewsCount() !=0 && listView.getAdapter() != null){
			listView.removeFooterView(footerview);
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				POJOAllGiftCards obj = (POJOAllGiftCards) WebServiceStaticArrays.mAllGiftCardList.get(position);
				mGiftCardId=obj.mGiftCardId;
				mGiftCardBalanceAmount=Double.parseDouble(obj.mBalanceAmount);
				mGiftCard_StoreId=obj.mStoreId;
				mGiftCardStoreLocationId=obj.mLocationId;
				mGiftCard_StoreName=obj.mStoreName;
				mGiftCard_Type=obj.mCardType;
				mChoosedGiftcardPosition = position;
				// Assign to pass purchase Id to transaction history
				mGiftCardPurchaseId = obj.mGiftcardPurchaseId;
				mGiftcardFaceValue = Double.valueOf(obj.mFaceValue);
				mGiftcardPurchaseDate = obj.mGiftcardPurchaseDate;

				MenuOutClass.GIFTCARDS_MENUOUT=false;

				if(mGiftCardBalanceAmount==0){
					GiftCards.mGiftCardsSendToFriend.setEnabled(false);
					GiftCards.mGiftCardsSendToFriend.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
					GiftCards.mGiftCardsSelfUse.setEnabled(false);
					GiftCards.mGiftCardsSelfUse.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				}else{
					GiftCards.mGiftCardsSendToFriend.setEnabled(true);
					GiftCards.mGiftCardsSendToFriend.setBackgroundResource(R.drawable.gradient_bg);
					GiftCards.mGiftCardsSelfUse.setEnabled(true);
					GiftCards.mGiftCardsSelfUse.setBackgroundResource(R.drawable.gradient_bg);
				}

				OpenMenu.toOpenRightMenu("GiftCards");
			}
		});
	}

	public static void favoritesListView(final Context context,final ListView listView){

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(context, "Zpay Favorites", Toast.LENGTH_SHORT).show();
				POJOStoreInfo mStoreInformation = (POJOStoreInfo) listView.getAdapter().getItem(position);
			}
		});
	}

	public static void cardsOnFilesListView(final Context context,ListView listView,String eventFlag){
		listView.setAdapter(new CardOnFiles_Adapter(context,eventFlag));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CardOnFiles_ClassVariables obj = (CardOnFiles_ClassVariables) WebServiceStaticArrays.mCardOnFiles.get(position);
				Toast.makeText(context, "CardName : "+obj.cardName, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static void couponsListView(final Context context,ListView listView){
		listView.setAdapter(new CouponListAdapter(context));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(WebServiceStaticArrays.mStaticCouponsArrayList.size() !=0){
					Intent mCouponDetail = new Intent(context,CouponDetail.class);
					mCouponDetail.putExtra("CouponPosition", String.valueOf(position));
					mCouponDetail.putExtra("Activity", "Activity");
					context.startActivity(mCouponDetail);
				}else{
					Log.i("Coupons List", "No List Item");
				}
			}
		});
	}

	public static void FavoriteCouponsListView(final Context context,ListView listView){
		listView.setAdapter(new CouponListAdapter(context));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i("MenuUntity", "Item Click");
				if(WebServiceStaticArrays.mStaticCouponsArrayList.size() >0 && WebServiceStaticArrays.mStaticCouponsArrayList.size() > position){
					Intent mCouponDetail = new Intent(context,CouponDetail.class);
					mCouponDetail.putExtra("CouponPosition", String.valueOf(position));
					mCouponDetail.putExtra("Activity", "Favorite");
					context.startActivity(mCouponDetail);
				}else{
					Log.i("Coupons List", "No List Item");
				}
			}
		});
	}

	public static void TalkToUs_ContactStoreListView(final Context context,final ListView listView,boolean isForRefreshAdapter,final String classname,final ViewGroup menuContainer, String Flag){
		final int position = WebServiceStaticArrays.mContactUsResponseResult.size();
		Log.i("position", " "+ position);
		if(isForRefreshAdapter && mTalkToUsContactStoreAdapter != null){
			Log.i("first visible position", listView.getLastVisiblePosition()+"");
			listView.setOnScrollListener(null);
			if(position != 0 && Flag.equalsIgnoreCase("scroll")){
				listView.setStackFromBottom(false);
				mTalkToUsContactStoreAdapter.notifyDataSetChanged();
				Log.i("selected positon to set", position + " ");
				listView.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(position == 20)
							   listView.setSelectionFromTop(18, 0);
						else
							   listView.setSelectionFromTop(position, 0);
					}
				});
				listView.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						listView.setOnScrollListener(new CustomChatScrollListner(context, listView, classname, false, menuContainer));
					}
				}, 1000);
				
			}else{
				listView.setStackFromBottom(true);
				mTalkToUsContactStoreAdapter.notifyDataSetChanged();
				listView.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						listView.setOnScrollListener(new CustomChatScrollListner(context, listView, classname, false, menuContainer));
					}
				});
			}
			Log.i("adapter", "refresh");
     	}else{
			Log.i("adapter", "set");
			mTalkToUsContactStoreAdapter = new TalkToUs_ContactStore_Adapter(context);
			listView.setAdapter(mTalkToUsContactStoreAdapter);	
		}
		WebServiceStaticArrays.mContactUsResponseResult.clear();
		//listView.smoothScrollToPosition(position);    
	}

	public static void fbFriendListView(final Activity context,final ListView listView, final HorizontalScrollView scrollview, final View leftmenu, final View rightmenu,final Button freezeview){
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(WebServiceStaticArrays.mSocialNetworkFriendList.size() > 0 || WebServiceStaticArrays.mSearchedFriendList.size() > 0){
					POJOFBfriendListData mFriendDetails = (POJOFBfriendListData) listView.getAdapter().getItem(position);
					
					if(!mFriendDetails.friend_id.equalsIgnoreCase("")){
						
						// To hide soft keyboard
						InputMethodManager softkeyboardevent = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
						softkeyboardevent.hideSoftInputFromWindow(Friends.mFriendNameSearch.getWindowToken(), 0);
						
						Friends.mFriendNameSearch.clearFocus();
						Friends.mFriendNameSearch.setFocusable(false);
						Friends.mFriendNameSearch.setFocusableInTouchMode(false);
						//String closeKeyboard = KeyboardUtil.hideKeyboard(context);
						
						//if(closeKeyboard.equals("success")||closeKeyboard.equals("nofocus")){
							
							Friends.friendName = mFriendDetails.name;
							Friends.friendEmailId = mFriendDetails.friend_email;
							Friends.friendId = mFriendDetails.friend_id;
							Friends.isZouponsFriend = mFriendDetails.zouponsfriend;
							Friends.friendTimeZone = mFriendDetails.timezone;
							
							if(Friends.isZouponsFriend.equalsIgnoreCase("yes")){
								Friends.mInviteFriend.setEnabled(false);
								Friends.mInviteFriend.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
							}else{
								Friends.mInviteFriend.setEnabled(true);
								Friends.mInviteFriend.setBackgroundResource(R.drawable.gradient_bg);
							}
							
							int menuWidth = leftmenu.getMeasuredWidth();	//menu out
							final int right = menuWidth+menuWidth;
							MenuOutClass.FRIENDS_MENUOUT=true;
							freezeview.setVisibility(View.VISIBLE);
							rightmenu.setVisibility(View.VISIBLE);	// Ensure menu is visible
							
							final Bundle bundle = new Bundle();
							final Message msg_response = new Message();
							
							Thread rightMenuOpenThread = new Thread(){
								@Override
								public void run(){
									try{
										//sleep(100);
										bundle.putInt("rightmenu", right);
										bundle.putString("open", "yes");
										msg_response.setData(bundle);
										handler_response.sendMessage(msg_response);
									}catch(Exception e){
										e.printStackTrace();
									}
								}
							};rightMenuOpenThread.start();
						/*}else {
							Log.i("MenuUtility", closeKeyboard);
						}*/
					}else{
					}
				}else{}
			}
		});
	}

	static Handler handler_response = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg!=null){
				Bundle b = msg.getData();
				String key = b.getString("open");
				int rightmenu = b.getInt("rightmenu");
				if(key.equalsIgnoreCase("yes")){
					Friends.scrollView.smoothScrollTo(rightmenu, 0);
				}
			}
		}
	};
	
	public static void TransactionHistoryListView(final Context context,ListView listView){
		listView.setAdapter(new TransactionHistory_Inflater(context));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
		});
	}

	public static void StoreOwnerDashBoardListView(final Context context,ListView listView){
		listView.setAdapter(new StoreOwnerDashBoard_Adapter(context));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// to get user type from preference
				SharedPreferences mPrefs = context.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				String user_type = mPrefs.getString("user_type", "");
				String dashboard_access = mPrefs.getString("dashboard_access", "");
				if(user_type.equalsIgnoreCase("store_employee") && dashboard_access.equalsIgnoreCase("no")){
					Toast.makeText(context, "Not permitted", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context, "position", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public static void StoreOwnerHomePageListView(final Context context,ListView listView,final String classname, ArrayList<Object> result){
		listView.setAdapter(new StoreOwnerHomePage_Adapter(context,result));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AccountLoginFlag.accountUserTypeflag = "store_owner";
				POJOStoreInfo mStoreLocationdetails = (POJOStoreInfo) parent.getItemAtPosition(position);
				//Log.i("list item click", mStoreLocationdetails.location_id);
				// To save store_id in preference
				SharedPreferences mPrefs = context.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				Editor editor = mPrefs.edit();
				editor.putString("location_id",mStoreLocationdetails.location_id);
				editor.putString("store_name",mStoreLocationdetails.store_name);
				editor.putString("store_location_address",mStoreLocationdetails.address_line1 + "\n" + mStoreLocationdetails.city+", "+mStoreLocationdetails.state+" "+mStoreLocationdetails.zip_code);
				editor.commit();			
				Intent dashboard_intent = new Intent(context,StoreOwnerDashBoard.class);
				dashboard_intent.putExtra("classname", classname);
				context.startActivity(dashboard_intent);
			}
		});
	}

	public static void StoreOwnerStoreInformationListView(final Context context,ListView listView,final String className){
		listView.setAdapter(new StoreOwnerStoreInformation_Adapter(context,className));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent addlocations_intent = new Intent(context,StoreOwner_AddLocations.class);
				addlocations_intent.putExtra("footerflag", "yes");
				context.startActivity(addlocations_intent);
			}
		});
	}

	public static void StoreOwnerEmployeesListView(final Context context,ListView listView){
		listView.setAdapter(new StoreOwnerEmployees_Adapter(context));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent generalinfo_intent = new Intent(context,StoreOwner_EmployeeDetails.class);
				context.startActivity(generalinfo_intent);
			}
		});
	}

	public static void StoreOwnerEmployeesDetailsListView(ListAdapter choosedlocationadapter,ListView listView){
		listView.setAdapter(choosedlocationadapter);
		listView.setItemsCanFocus(false);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
		});
	}

	public static void StoreOwnerPointOfSalePart1(final Context context,ListView listView){
		listView.setAdapter(new PointOfsalePart1Adapter(context));
		listView.setItemsCanFocus(false);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
		});
	}

	private static void alertBox_service(Context context,String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(context);
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

	public static void SetRightMenuStatus(StoreLocator_ClassVariables obj,Context context){

		RightMenuStoreId_ClassVariables.rightmenu_location_flag = obj.rightmenu_location_flag;
		RightMenuStoreId_ClassVariables.rightmenu_coupons_flag = obj.rightmenu_coupons_flag ;
		RightMenuStoreId_ClassVariables.rightmenu_aboutus_flag = obj.rightmenu_aboutus_flag;
		RightMenuStoreId_ClassVariables.rightmenu_reviewandrating_flag = obj.rightmenu_reviewandrating_flag;
		RightMenuStoreId_ClassVariables.rightmenu_photos_flag = obj.rightmenu_photos_flag;
		RightMenuStoreId_ClassVariables.rightmenu_contactstore_flag = obj.rightmenu_contactstore_flag;
		RightMenuStoreId_ClassVariables.rightmenu_giftcards_flag = obj.rightmenu_giftcards_flag;
		RightMenuStoreId_ClassVariables.rightmenu_videos_flag = obj.rightmenu_videos_flag;
		RightMenuStoreId_ClassVariables.rightmenu_invoicecenter_flag = obj.rightmenu_invoicecenter_flag;
		RightMenuStoreId_ClassVariables.rightmenu_communication_flag = obj.rightmenu_communication_flag;
		RightMenuStoreId_ClassVariables.rightmenu_pointofsale_flag = obj.rightmenu_pointofsale_flag;
		RightMenuStoreId_ClassVariables.rightmenu_zcards_flag = obj.rightmenu_zcards_flag;

		RightMenuStoreId_ClassVariables.rightmenu_share_flag = obj.rightmenu_share_flag;
		RightMenuStoreId_ClassVariables.rightmenu_batchsales_flag = obj.rightmenu_batchsales_flag;
		RightMenuStoreId_ClassVariables.rightmenu_refund_flag = obj.rightmenu_refund_flag;
		RightMenuStoreId_ClassVariables.rightmenu_mobilepayment_flag = obj.rightmenu_mobilepayment_flag;
		RightMenuStoreId_ClassVariables.rightmenu_rewardcards_flag = obj.rightmenu_rewardcards_flag;

		// Setting enabled or disabled right menu options
		if(RightMenuStoreId_ClassVariables.rightmenu_aboutus_flag.equalsIgnoreCase("no")){
			SlidingView.mInfo_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			SlidingView.mInfo_RightMenu.setEnabled(false);
			SlidingView.mInfoText_RightMenu.setTextColor(Color.GRAY);
			SlidingView.mInfoImage_RightMenu.setAlpha(100);
		}else{
			//SlidingView.mInfo_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
			SlidingView.mInfo_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			SlidingView.mInfo_RightMenu.setEnabled(true);
			SlidingView.mInfoText_RightMenu.setTextColor(Color.WHITE);
			SlidingView.mInfoImage_RightMenu.setAlpha(255);
		}
		if(RightMenuStoreId_ClassVariables.rightmenu_pointofsale_flag.equalsIgnoreCase("no")){  
			SlidingView.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			SlidingView.mMobilePay_RightMenu.setEnabled(false);
			SlidingView.mMobilePayText_RightMenu.setTextColor(Color.GRAY);
			SlidingView.mMobilePayImage_RightMenu.setAlpha(100);
		}else{
			//SlidingView.mMobilePay_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
			SlidingView.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			SlidingView.mMobilePay_RightMenu.setEnabled(true);
			SlidingView.mMobilePayText_RightMenu.setTextColor(Color.WHITE);
			SlidingView.mMobilePayImage_RightMenu.setAlpha(255);
		}
		if(RightMenuStoreId_ClassVariables.rightmenu_giftcards_flag.equalsIgnoreCase("no")){  
			SlidingView.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			SlidingView.mGiftCards_RightMenu.setEnabled(false);
			SlidingView.mGiftCardsText_RightMenu.setTextColor(Color.GRAY);
			SlidingView.mGiftCardsImage_RightMenu.setAlpha(100);
		}else{
			//SlidingView.mGiftCards_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
			SlidingView.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			SlidingView.mGiftCards_RightMenu.setEnabled(true);
			SlidingView.mGiftCardsText_RightMenu.setTextColor(Color.WHITE);
			SlidingView.mGiftCardsImage_RightMenu.setAlpha(255);
		}
		if(RightMenuStoreId_ClassVariables.rightmenu_zcards_flag.equalsIgnoreCase("no")){
			SlidingView.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			SlidingView.mDeals_RightMenu.setEnabled(false);
			SlidingView.mDealsText_RightMenu.setTextColor(Color.GRAY);
			SlidingView.mDealsImage_RightMenu.setAlpha(100);
		}else{
			//SlidingView.mDeals_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
			SlidingView.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			SlidingView.mDeals_RightMenu.setEnabled(true);
			SlidingView.mDealsText_RightMenu.setTextColor(Color.WHITE);
			SlidingView.mDealsImage_RightMenu.setAlpha(255);
		}
		if(RightMenuStoreId_ClassVariables.rightmenu_coupons_flag.equalsIgnoreCase("no")){
			SlidingView.mCoupons_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			SlidingView.mCoupons_RightMenu.setEnabled(false);
			SlidingView.mCouponsText_RightMenu.setTextColor(Color.GRAY);
			SlidingView.mCouponsImage_RightMenu.setAlpha(100);
		}else{
			//SlidingView.mCoupons_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
			SlidingView.mCoupons_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			SlidingView.mCoupons_RightMenu.setEnabled(true);
			SlidingView.mCouponsText_RightMenu.setTextColor(Color.WHITE);
			SlidingView.mCouponsImage_RightMenu.setAlpha(255);
		}
		if(RightMenuStoreId_ClassVariables.rightmenu_reviewandrating_flag.equalsIgnoreCase("no")){
			SlidingView.mReviews_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			SlidingView.mReviews_RightMenu.setEnabled(false);
			SlidingView.mReviewsText_RightMenu.setTextColor(Color.GRAY);
			SlidingView.mReviewsImage_RightMenu.setAlpha(100);
		}else{
			//SlidingView.mReviews_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
			SlidingView.mReviews_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			SlidingView.mReviews_RightMenu.setEnabled(true);
			SlidingView.mReviewsText_RightMenu.setTextColor(Color.WHITE);
			SlidingView.mReviewsImage_RightMenu.setAlpha(255);
		}
		if(RightMenuStoreId_ClassVariables.rightmenu_location_flag.equalsIgnoreCase("no")){
			SlidingView.mLocations_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			SlidingView.mLocations_RightMenu.setEnabled(false);
			SlidingView.mLocationsText_RightMenu.setTextColor(Color.GRAY);
			SlidingView.mLocationsImage_RightMenu.setAlpha(100);
		}else{
			//SlidingView.mLocations_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
			SlidingView.mLocations_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			SlidingView.mLocations_RightMenu.setEnabled(true);
			SlidingView.mLocationsText_RightMenu.setTextColor(Color.WHITE);
			SlidingView.mLocationsImage_RightMenu.setAlpha(255);
		}
		if(RightMenuStoreId_ClassVariables.rightmenu_photos_flag.equalsIgnoreCase("no")){
			SlidingView.mPhotos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			SlidingView.mPhotos_RightMenu.setEnabled(false);
			SlidingView.mPhotosText_RightMenu.setTextColor(Color.GRAY);
			SlidingView.mPhotosImage_RightMenu.setAlpha(100);
		}else{
			//SlidingView.mPhotos_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
			SlidingView.mPhotos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			SlidingView.mPhotos_RightMenu.setEnabled(true);
			SlidingView.mPhotosText_RightMenu.setTextColor(Color.WHITE);
			SlidingView.mPhotosImage_RightMenu.setAlpha(255);
		}
		if(RightMenuStoreId_ClassVariables.rightmenu_videos_flag.equalsIgnoreCase("no")){
			SlidingView.mVideos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			SlidingView.mVideos_RightMenu.setEnabled(false);
			SlidingView.mVideosText_RightMenu.setTextColor(Color.GRAY);
			SlidingView.mVideosImage_RightMenu.setAlpha(100);
		}else{
			//SlidingView.mVideos_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
			SlidingView.mVideos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			SlidingView.mVideos_RightMenu.setEnabled(true);
			SlidingView.mVideosText_RightMenu.setTextColor(Color.WHITE);
			SlidingView.mVideosImage_RightMenu.setAlpha(255);
		}
		if(RightMenuStoreId_ClassVariables.rightmenu_contactstore_flag.equalsIgnoreCase("no")){
			SlidingView.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			SlidingView.mContactStore_RightMenu.setEnabled(false);
			SlidingView.mContactStoreText_RightMenu.setTextColor(Color.GRAY);
			SlidingView.mContactStoreImage_RightMenu.setAlpha(100);
		}else{
			//SlidingView.mContactStore_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
			SlidingView.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			SlidingView.mContactStore_RightMenu.setEnabled(true);
			SlidingView.mContactStoreText_RightMenu.setTextColor(Color.WHITE);
			SlidingView.mContactStoreImage_RightMenu.setAlpha(255);
		}
		if(RightMenuStoreId_ClassVariables.rightmenu_share_flag.equalsIgnoreCase("no")){
			SlidingView.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			SlidingView.mSocial_RightMenu.setEnabled(false);
			SlidingView.mSocialText_RightMenu.setTextColor(Color.GRAY);
			SlidingView.mSocialImage_RightMenu.setAlpha(100);
		}else{
			//SlidingView.mSocial_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
			SlidingView.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
			SlidingView.mSocial_RightMenu.setEnabled(true);
			SlidingView.mSocialText_RightMenu.setTextColor(Color.WHITE);
			SlidingView.mSocialImage_RightMenu.setAlpha(255);
		}
	}
}