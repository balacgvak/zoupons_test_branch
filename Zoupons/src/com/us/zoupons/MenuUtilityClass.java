package com.us.zoupons;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.us.zoupons.classvariables.CardOnFiles_ClassVariables;
import com.us.zoupons.classvariables.POJOFBfriendListData;
import com.us.zoupons.classvariables.POJOStoreInfo;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.StoreLocator_ClassVariables;
import com.us.zoupons.classvariables.StoresLocation_ClassVariables;
import com.us.zoupons.classvariables.StoresQRCode_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.AccountLoginFlag;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.flagclasses.ZPayFlag;
import com.us.zoupons.listview_inflater_classes.CardOnFiles_Adapter;
import com.us.zoupons.listview_inflater_classes.GiftCards_Adapter;
import com.us.zoupons.listview_inflater_classes.LocationShopList_Adapter;
import com.us.zoupons.listview_inflater_classes.ShopNearestList_Adapter;
import com.us.zoupons.mobilepay.MobilePay;
import com.us.zoupons.mobilepay.ZpayStep2SearchEnable;
import com.us.zoupons.shopper.cards.StoreCardDetails;
import com.us.zoupons.shopper.chatsupport.CustomChatScrollListner;
import com.us.zoupons.shopper.chatsupport.TalkToUs_ContactStore_Adapter;
import com.us.zoupons.shopper.coupons.CouponDetail;
import com.us.zoupons.shopper.coupons.CouponListAdapter;
import com.us.zoupons.shopper.friends.Friends;
import com.us.zoupons.shopper.giftcards_deals.MyGiftCards;
import com.us.zoupons.shopper.giftcards_deals.POJOAllGiftCards;
import com.us.zoupons.shopper.giftcards_deals.TransactionHistory_Inflater;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.location.Location;
import com.us.zoupons.storeowner.dashBoard.StoreOwnerDashBoard;
import com.us.zoupons.storeowner.homepage.StoreOwnerHomePage_Adapter;

public class MenuUtilityClass{

	public static String sGiftCardId="",sGiftCardPurchaseId="",sGiftcardPurchaseDate="";
	public static double sGiftCardBalanceAmount,sGiftcardFaceValue;
	public static String sGiftCard_StoreId="",sGiftCard_StoreName="",sGiftCard_Type="",sGiftCardStoreLocationId="";
	public static int sChoosedGiftcardPosition;
	public static ShopNearestList_Adapter sStoreListAdapter;
	public static TalkToUs_ContactStore_Adapter sTalkToUsContactStoreAdapter;
	public static GiftCards_Adapter sMyGiftcardsAdapter = null;

	private MenuUtilityClass(){
	}

	public static void shopListView(final Context context, ListView listView,final String menubarStoreFlag,final String classname,String sourcelist,boolean isForRefreshAdapter,final String mPageFlag) {
		if(menubarStoreFlag.equals("search")){
			//listView.setAdapter(new ShopList_Adapter(context));
		}else if(menubarStoreFlag.equals("currentlocationnearstore")){
			listView.setTag(sourcelist);
			if(isForRefreshAdapter && sStoreListAdapter != null){
				sStoreListAdapter.notifyDataSetChanged();
				Log.i("adapter", "refresh");
			}else{
				Log.i("adapter", "set");
				sStoreListAdapter = new ShopNearestList_Adapter(context);
				listView.setAdapter(sStoreListAdapter);	
			}
		}else if(menubarStoreFlag.equals("qrcodesearch")){
			//listView.setAdapter(new ShopQrCodeList_Adapter(context));
		}else if(menubarStoreFlag.equals("browse")){
			//listView.setAdapter(new ShopBrowseList_Adapter(context));
		}else if(menubarStoreFlag.equals("location")){
			//listView.setAdapter(new LocationShopList_Adapter(context));
		}

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if(!classname.equals("ShopperHomePage")&&!classname.equals("location")){
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
							ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						}else{
							ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}
						OpenMenu.toOpenRightMenu("ShopperHomePage");		// Function to open RightMenu
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

						if(obj.deviceDistance.equalsIgnoreCase("-1")){
							RightMenuStoreId_ClassVariables.mStoreLocation = "online store";
						}else{
							RightMenuStoreId_ClassVariables.mStoreLocation = obj.addressLine1 + "\n" + obj.city+", "+obj.state+" "+obj.zipcode;	
						}

						if(classname.equals("ShopperHomePage")){
							ShopperHomePage.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
							ShopperHomePage.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);
							if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
								ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
							}else{
								ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
							}
							SetRightMenuStatus(obj,context);
							OpenMenu.toOpenRightMenu("ShopperHomePage");		// Function to open RightMenu
						}else{
							if(mPageFlag.equalsIgnoreCase("giftcard")){
								if(obj.rightmenu_giftcards_flag.equalsIgnoreCase("yes")){
									//Move to Giftcards and zcards page
									Intent intent_giftcard_zcard = new Intent(context,StoreCardDetails.class);
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
									Intent intent_step2 = new Intent(context,MobilePay.class);
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
							ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						}else{
							ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}
						OpenMenu.toOpenRightMenu("ShopperHomePage");		// Function to open RightMenu
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
							ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
						}else{
							ShopperHomePage.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
						}

						OpenMenu.toOpenRightMenu("ShopperHomePage");		// Function to open RightMenu
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
				if(!classname.equals("ShopperHomePage")&&!classname.equals("location")){
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

				if(obj.deviceDistance.equalsIgnoreCase("-1")){
					RightMenuStoreId_ClassVariables.mStoreLocation = "online store";
				}else{
					RightMenuStoreId_ClassVariables.mStoreLocation = obj.addressLine1 + "\n" + obj.city+", "+obj.state+" "+obj.zipcode;	
				}

				Location.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
				Location.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);
				if(RightMenuStoreId_ClassVariables.rightmenu_favourite_status.equalsIgnoreCase("yes")){
					Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.removefavorite);
				}else{
					Location.mFavoriteImage_RightMenu.setImageResource(R.drawable.addfavorite);
				}
				SetLocationRightMenuStatus(obj,context);
				OpenMenu.toOpenRightMenu("locations");		// Function to open RightMenu
			}
		});
	}

	// setting giftcard listview adapter and handling list item select
	public static void giftcardsListView(final Activity context,ListView listView, String mCheckAndRefresh,View footerview){
		if(!mCheckAndRefresh.equalsIgnoreCase("")){
			sMyGiftcardsAdapter.notifyDataSetChanged();
		}else{
			sMyGiftcardsAdapter = new GiftCards_Adapter(context);
			listView.setAdapter(sMyGiftcardsAdapter);	
		}
		if(footerview != null && listView.getFooterViewsCount() !=0 && listView.getAdapter() != null){
			listView.removeFooterView(footerview);
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				POJOAllGiftCards obj = (POJOAllGiftCards) WebServiceStaticArrays.mAllGiftCardList.get(position);
				sGiftCardId=obj.mGiftCardId;
				sGiftCardBalanceAmount=Double.parseDouble(obj.mBalanceAmount);
				sGiftCard_StoreId=obj.mStoreId;
				sGiftCardStoreLocationId=obj.mLocationId;
				sGiftCard_StoreName=obj.mStoreName;
				sGiftCard_Type=obj.mCardType;
				sChoosedGiftcardPosition = position;
				// Assign to pass purchase Id to transaction history
				sGiftCardPurchaseId = obj.mGiftcardPurchaseId;
				sGiftcardFaceValue = Double.valueOf(obj.mFaceValue);
				sGiftcardPurchaseDate = obj.mGiftcardPurchaseDate;
				MenuOutClass.GIFTCARDS_MENUOUT=false;
				if(sGiftCardBalanceAmount==0){
					MyGiftCards.mGiftCardsSendToFriend.setEnabled(false);
					MyGiftCards.mGiftCardsSendToFriend.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
					MyGiftCards.mGiftCardsSelfUse.setEnabled(false);
					MyGiftCards.mGiftCardsSelfUse.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
					MyGiftCards.mGiftCardsSendToFriendTxt.setTextColor(Color.GRAY);
					MyGiftCards.mGiftCardsSelfUseTxt.setTextColor(Color.GRAY);
					MyGiftCards.mGiftCardsSelfUseImage.setAlpha(100);
					MyGiftCards.mGiftCardsSendToFriendImage.setAlpha(100);
					
				}else{
					MyGiftCards.mGiftCardsSendToFriend.setEnabled(true);
					MyGiftCards.mGiftCardsSendToFriend.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
					MyGiftCards.mGiftCardsSelfUse.setEnabled(true);
					MyGiftCards.mGiftCardsSelfUse.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
					MyGiftCards.mGiftCardsSendToFriendTxt.setTextColor(Color.WHITE);
					MyGiftCards.mGiftCardsSelfUseTxt.setTextColor(Color.WHITE);
					MyGiftCards.mGiftCardsSelfUseImage.setAlpha(255);
					MyGiftCards.mGiftCardsSendToFriendImage.setAlpha(255);
				}

				OpenMenu.toOpenRightMenu("MyGiftCards");
			}
		});
	}

	// setting wallet listview adapter and handling list item select
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

	// setting coupon listview adapter and handling list item select
	public static void couponsListView(final Context context,ListView listView){
		listView.setAdapter(new CouponListAdapter(context,"StoreCoupon"));
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

	// setting favorite coupon listview adapter and handling list item select
	public static void FavoriteCouponsListView(final Context context,ListView listView){
		listView.setAdapter(new CouponListAdapter(context,"FavoriteCoupon"));
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

	// setting talktous listview adapter and handling list item select
	public static void TalkToUs_ContactStoreListView(final Context context,final ListView listView,boolean isForRefreshAdapter,final String classname,final ViewGroup menuContainer, String Flag,final Button notificationCountButton){
		final int position = WebServiceStaticArrays.mContactUsResponseResult.size();
		Log.i("position", " "+ position);
		if(isForRefreshAdapter && sTalkToUsContactStoreAdapter != null){
			Log.i("first visible position", listView.getLastVisiblePosition()+"");
			listView.setOnScrollListener(null);
			if(position != 0 && Flag.equalsIgnoreCase("scroll")){
				listView.setStackFromBottom(false);
				sTalkToUsContactStoreAdapter.notifyDataSetChanged();
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
						listView.setOnScrollListener(new CustomChatScrollListner(context, listView, classname, false, menuContainer,notificationCountButton));
					}
				}, 1000);

			}else{
				listView.setStackFromBottom(true);
				sTalkToUsContactStoreAdapter.notifyDataSetChanged();
				listView.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						listView.setOnScrollListener(new CustomChatScrollListner(context, listView, classname, false, menuContainer,notificationCountButton));
					}
				});
			}
			Log.i("adapter", "refresh");
		}else{
			Log.i("adapter", "set");
			sTalkToUsContactStoreAdapter = new TalkToUs_ContactStore_Adapter(context);
			listView.setAdapter(sTalkToUsContactStoreAdapter);	
		}
		WebServiceStaticArrays.mContactUsResponseResult.clear();
		//listView.smoothScrollToPosition(position);    
	}

	// setting friends listview adapter and handling list item select
	public static void fbFriendListView(final Activity context,final ListView listView, final HorizontalScrollView scrollview, final View leftmenu, final View rightmenu,final Button freezeview){
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(WebServiceStaticArrays.mSocialNetworkFriendList.size() > 0 || WebServiceStaticArrays.mSearchedFriendList.size() > 0){
					POJOFBfriendListData mFriendDetails = (POJOFBfriendListData) listView.getAdapter().getItem(position);
					// To hide soft keyboard
					InputMethodManager softkeyboardevent = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
					softkeyboardevent.hideSoftInputFromWindow(Friends.mFriendNameSearch.getWindowToken(), 0);
					Friends.mFriendNameSearch.clearFocus();
					Friends.mFriendNameSearch.setFocusable(false);
					Friends.mFriendNameSearch.setFocusableInTouchMode(false);
					Friends.friendName = mFriendDetails.name;
					Friends.friendEmailId = mFriendDetails.friend_email;
					Friends.friendId = mFriendDetails.friend_id;
					Friends.isZouponsFriend = mFriendDetails.zouponsfriend;
					Friends.friendTimeZone = mFriendDetails.timezone;

					if(Friends.isZouponsFriend.equalsIgnoreCase("yes")){
						Friends.mInviteFriendText.setTextColor(Color.GRAY);
						Friends.mInviteFriendImage.setAlpha(100);
						Friends.mInviteFriend.setEnabled(false);
						Friends.mInviteFriend.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
					}else{
						Friends.mInviteFriendText.setTextColor(Color.WHITE);
						Friends.mInviteFriendImage.setAlpha(255);
						Friends.mInviteFriend.setEnabled(true);
						Friends.mInviteFriend.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
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
								bundle.putInt("sRightmenu", right);
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
				int rightmenu = b.getInt("sRightmenu");
				if(key.equalsIgnoreCase("yes")){
					Friends.sScrollView.smoothScrollTo(rightmenu, 0);
				}
			}
		}
	};

	// setting giftcard transaction history listview adapter and handling list item select
	public static void TransactionHistoryListView(final Context context,ListView listView){
		listView.setAdapter(new TransactionHistory_Inflater(context));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
		});
	}

	// setting store homepage listview adapter and handling list item select
	public static void StoreOwnerHomePageListView(final Context context,ListView listView,final String classname, ArrayList<Object> result){
		listView.setAdapter(new StoreOwnerHomePage_Adapter(context,result));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SharedPreferences mUserPrefs = context.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
				UserDetails.mServiceUserId = mUserPrefs.getString("user_id", "");
				UserDetails.mServiceFbId = mUserPrefs.getString("fb_id", "");
				UserDetails.mUserType = mUserPrefs.getString("user_type", "");

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
				dashboard_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(dashboard_intent);
			}
		});
	}

	// Funtion to show alert pop up with respective message
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

	// To enable or disable right menu items	
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

		SharedPreferences mUserDetailsPrefs = context.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");

		// Setting enabled or disabled right menu options
		if(user_login_type.equalsIgnoreCase("customer")){
			if(RightMenuStoreId_ClassVariables.rightmenu_aboutus_flag.equalsIgnoreCase("no")){
				ShopperHomePage.mInfo_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				ShopperHomePage.mInfo_RightMenu.setEnabled(false);
				ShopperHomePage.mInfoText_RightMenu.setTextColor(Color.GRAY);
				ShopperHomePage.mInfoImage_RightMenu.setAlpha(100);
			}else{
				//ShopperHomePage.mInfo_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				ShopperHomePage.mInfo_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				ShopperHomePage.mInfo_RightMenu.setEnabled(true);
				ShopperHomePage.mInfoText_RightMenu.setTextColor(Color.WHITE);
				ShopperHomePage.mInfoImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_pointofsale_flag.equalsIgnoreCase("no")){  
				ShopperHomePage.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				ShopperHomePage.mMobilePay_RightMenu.setEnabled(false);
				ShopperHomePage.mMobilePayText_RightMenu.setTextColor(Color.GRAY);
				ShopperHomePage.mMobilePayImage_RightMenu.setAlpha(100);
			}else{
				//ShopperHomePage.mMobilePay_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				ShopperHomePage.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				ShopperHomePage.mMobilePay_RightMenu.setEnabled(true);
				ShopperHomePage.mMobilePayText_RightMenu.setTextColor(Color.WHITE);
				ShopperHomePage.mMobilePayImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_giftcards_flag.equalsIgnoreCase("no")){  
				ShopperHomePage.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				ShopperHomePage.mGiftCards_RightMenu.setEnabled(false);
				ShopperHomePage.mGiftCardsText_RightMenu.setTextColor(Color.GRAY);
				ShopperHomePage.mGiftCardsImage_RightMenu.setAlpha(100);
			}else{
				//ShopperHomePage.mGiftCards_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				ShopperHomePage.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				ShopperHomePage.mGiftCards_RightMenu.setEnabled(true);
				ShopperHomePage.mGiftCardsText_RightMenu.setTextColor(Color.WHITE);
				ShopperHomePage.mGiftCardsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_zcards_flag.equalsIgnoreCase("no")){
				ShopperHomePage.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				ShopperHomePage.mDeals_RightMenu.setEnabled(false);
				ShopperHomePage.mDealsText_RightMenu.setTextColor(Color.GRAY);
				ShopperHomePage.mDealsImage_RightMenu.setAlpha(100);
			}else{
				//ShopperHomePage.mDeals_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				ShopperHomePage.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				ShopperHomePage.mDeals_RightMenu.setEnabled(true);
				ShopperHomePage.mDealsText_RightMenu.setTextColor(Color.WHITE);
				ShopperHomePage.mDealsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_coupons_flag.equalsIgnoreCase("no")){
				ShopperHomePage.mCoupons_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				ShopperHomePage.mCoupons_RightMenu.setEnabled(false);
				ShopperHomePage.mCouponsText_RightMenu.setTextColor(Color.GRAY);
				ShopperHomePage.mCouponsImage_RightMenu.setAlpha(100);
			}else{
				//ShopperHomePage.mCoupons_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				ShopperHomePage.mCoupons_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				ShopperHomePage.mCoupons_RightMenu.setEnabled(true);
				ShopperHomePage.mCouponsText_RightMenu.setTextColor(Color.WHITE);
				ShopperHomePage.mCouponsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_reviewandrating_flag.equalsIgnoreCase("no")){
				ShopperHomePage.mReviews_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				ShopperHomePage.mReviews_RightMenu.setEnabled(false);
				ShopperHomePage.mReviewsText_RightMenu.setTextColor(Color.GRAY);
				ShopperHomePage.mReviewsImage_RightMenu.setAlpha(100);
			}else{
				//ShopperHomePage.mReviews_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				ShopperHomePage.mReviews_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				ShopperHomePage.mReviews_RightMenu.setEnabled(true);
				ShopperHomePage.mReviewsText_RightMenu.setTextColor(Color.WHITE);
				ShopperHomePage.mReviewsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_location_flag.equalsIgnoreCase("no")){
				ShopperHomePage.mLocations_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				ShopperHomePage.mLocations_RightMenu.setEnabled(false);
				ShopperHomePage.mLocationsText_RightMenu.setTextColor(Color.GRAY);
				ShopperHomePage.mLocationsImage_RightMenu.setAlpha(100);
			}else{
				//ShopperHomePage.mLocations_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				ShopperHomePage.mLocations_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				ShopperHomePage.mLocations_RightMenu.setEnabled(true);
				ShopperHomePage.mLocationsText_RightMenu.setTextColor(Color.WHITE);
				ShopperHomePage.mLocationsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_photos_flag.equalsIgnoreCase("no")){
				ShopperHomePage.mPhotos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				ShopperHomePage.mPhotos_RightMenu.setEnabled(false);
				ShopperHomePage.mPhotosText_RightMenu.setTextColor(Color.GRAY);
				ShopperHomePage.mPhotosImage_RightMenu.setAlpha(100);
			}else{
				//ShopperHomePage.mPhotos_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				ShopperHomePage.mPhotos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				ShopperHomePage.mPhotos_RightMenu.setEnabled(true);
				ShopperHomePage.mPhotosText_RightMenu.setTextColor(Color.WHITE);
				ShopperHomePage.mPhotosImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_videos_flag.equalsIgnoreCase("no")){
				ShopperHomePage.mVideos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				ShopperHomePage.mVideos_RightMenu.setEnabled(false);
				ShopperHomePage.mVideosText_RightMenu.setTextColor(Color.GRAY);
				ShopperHomePage.mVideosImage_RightMenu.setAlpha(100);
			}else{
				//ShopperHomePage.mVideos_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				ShopperHomePage.mVideos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				ShopperHomePage.mVideos_RightMenu.setEnabled(true);
				ShopperHomePage.mVideosText_RightMenu.setTextColor(Color.WHITE);
				ShopperHomePage.mVideosImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_contactstore_flag.equalsIgnoreCase("no")){
				ShopperHomePage.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				ShopperHomePage.mContactStore_RightMenu.setEnabled(false);
				ShopperHomePage.mContactStoreText_RightMenu.setTextColor(Color.GRAY);
				ShopperHomePage.mContactStoreImage_RightMenu.setAlpha(100);
			}else{
				//ShopperHomePage.mContactStore_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				ShopperHomePage.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				ShopperHomePage.mContactStore_RightMenu.setEnabled(true);
				ShopperHomePage.mContactStoreText_RightMenu.setTextColor(Color.WHITE);
				ShopperHomePage.mContactStoreImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_share_flag.equalsIgnoreCase("no")){
				ShopperHomePage.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				ShopperHomePage.mSocial_RightMenu.setEnabled(false);
				ShopperHomePage.mSocialText_RightMenu.setTextColor(Color.GRAY);
				ShopperHomePage.mSocialImage_RightMenu.setAlpha(100);
			}else{
				//ShopperHomePage.mSocial_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				ShopperHomePage.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				ShopperHomePage.mSocial_RightMenu.setEnabled(true);
				ShopperHomePage.mSocialText_RightMenu.setTextColor(Color.WHITE);
				ShopperHomePage.mSocialImage_RightMenu.setAlpha(255);
			}
		}else{ // Guest
			// Mobile Pay
			ShopperHomePage.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			ShopperHomePage.mMobilePay_RightMenu.setEnabled(false);
			ShopperHomePage.mMobilePayText_RightMenu.setTextColor(Color.GRAY);
			ShopperHomePage.mMobilePayImage_RightMenu.setAlpha(100);
			// MyGiftCards
			ShopperHomePage.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			ShopperHomePage.mGiftCards_RightMenu.setEnabled(false);
			ShopperHomePage.mGiftCardsText_RightMenu.setTextColor(Color.GRAY);
			ShopperHomePage.mGiftCardsImage_RightMenu.setAlpha(100);
			// DealCards
			ShopperHomePage.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			ShopperHomePage.mDeals_RightMenu.setEnabled(false);
			ShopperHomePage.mDealsText_RightMenu.setTextColor(Color.GRAY);
			ShopperHomePage.mDealsImage_RightMenu.setAlpha(100);
			// Share
			ShopperHomePage.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			ShopperHomePage.mSocial_RightMenu.setEnabled(false);
			ShopperHomePage.mSocialText_RightMenu.setTextColor(Color.GRAY);
			ShopperHomePage.mSocialImage_RightMenu.setAlpha(100);
			// Contact store
			ShopperHomePage.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			ShopperHomePage.mContactStore_RightMenu.setEnabled(false);
			ShopperHomePage.mContactStoreText_RightMenu.setTextColor(Color.GRAY);
			ShopperHomePage.mContactStoreImage_RightMenu.setAlpha(100);
			// Favorite
			ShopperHomePage.mFavorite_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			ShopperHomePage.mFavorite_RightMenu.setEnabled(false);
			ShopperHomePage.mFavoriteText_RightMenu.setTextColor(Color.GRAY);
			ShopperHomePage.mFavoriteImage_RightMenu.setAlpha(100);
		}

	}

	// To enable or disable right menu items
	public static void SetLocationRightMenuStatus(StoreLocator_ClassVariables obj,Context context){
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

		SharedPreferences mUserDetailsPrefs = context.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		String user_login_type = mUserDetailsPrefs.getString("user_login_type", "");

		// Setting enabled or disabled right menu options
		if(user_login_type.equalsIgnoreCase("customer")){
			if(RightMenuStoreId_ClassVariables.rightmenu_aboutus_flag.equalsIgnoreCase("no")){
				Location.mInfo_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mInfo_RightMenu.setEnabled(false);
				Location.mInfoText_RightMenu.setTextColor(Color.GRAY);
				Location.mInfoImage_RightMenu.setAlpha(100);
			}else{
				//Location.mInfo_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mInfo_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mInfo_RightMenu.setEnabled(true);
				Location.mInfoText_RightMenu.setTextColor(Color.WHITE);
				Location.mInfoImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_pointofsale_flag.equalsIgnoreCase("no")){  
				Location.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mMobilePay_RightMenu.setEnabled(false);
				Location.mMobilePayText_RightMenu.setTextColor(Color.GRAY);
				Location.mMobilePayImage_RightMenu.setAlpha(100);
			}else{
				//Location.mMobilePay_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mMobilePay_RightMenu.setEnabled(true);
				Location.mMobilePayText_RightMenu.setTextColor(Color.WHITE);
				Location.mMobilePayImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_giftcards_flag.equalsIgnoreCase("no")){  
				Location.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mGiftCards_RightMenu.setEnabled(false);
				Location.mGiftCardsText_RightMenu.setTextColor(Color.GRAY);
				Location.mGiftCardsImage_RightMenu.setAlpha(100);
			}else{
				//Location.mGiftCards_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mGiftCards_RightMenu.setEnabled(true);
				Location.mGiftCardsText_RightMenu.setTextColor(Color.WHITE);
				Location.mGiftCardsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_zcards_flag.equalsIgnoreCase("no")){
				Location.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mDeals_RightMenu.setEnabled(false);
				Location.mDealsText_RightMenu.setTextColor(Color.GRAY);
				Location.mDealsImage_RightMenu.setAlpha(100);
			}else{
				//Location.mDeals_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mDeals_RightMenu.setEnabled(true);
				Location.mDealsText_RightMenu.setTextColor(Color.WHITE);
				Location.mDealsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_coupons_flag.equalsIgnoreCase("no")){
				Location.mCoupons_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mCoupons_RightMenu.setEnabled(false);
				Location.mCouponsText_RightMenu.setTextColor(Color.GRAY);
				Location.mCouponsImage_RightMenu.setAlpha(100);
			}else{
				//Location.mCoupons_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mCoupons_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mCoupons_RightMenu.setEnabled(true);
				Location.mCouponsText_RightMenu.setTextColor(Color.WHITE);
				Location.mCouponsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_reviewandrating_flag.equalsIgnoreCase("no")){
				Location.mReviews_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mReviews_RightMenu.setEnabled(false);
				Location.mReviewsText_RightMenu.setTextColor(Color.GRAY);
				Location.mReviewsImage_RightMenu.setAlpha(100);
			}else{
				//Location.mReviews_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mReviews_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mReviews_RightMenu.setEnabled(true);
				Location.mReviewsText_RightMenu.setTextColor(Color.WHITE);
				Location.mReviewsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_location_flag.equalsIgnoreCase("no")){
				Location.mLocations_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mLocations_RightMenu.setEnabled(false);
				Location.mLocationsText_RightMenu.setTextColor(Color.GRAY);
				Location.mLocationsImage_RightMenu.setAlpha(100);
			}else{
				//Location.mLocations_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mLocations_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mLocations_RightMenu.setEnabled(true);
				Location.mLocationsText_RightMenu.setTextColor(Color.WHITE);
				Location.mLocationsImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_photos_flag.equalsIgnoreCase("no")){
				Location.mPhotos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mPhotos_RightMenu.setEnabled(false);
				Location.mPhotosText_RightMenu.setTextColor(Color.GRAY);
				Location.mPhotosImage_RightMenu.setAlpha(100);
			}else{
				//Location.mPhotos_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mPhotos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mPhotos_RightMenu.setEnabled(true);
				Location.mPhotosText_RightMenu.setTextColor(Color.WHITE);
				Location.mPhotosImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_videos_flag.equalsIgnoreCase("no")){
				Location.mVideos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mVideos_RightMenu.setEnabled(false);
				Location.mVideosText_RightMenu.setTextColor(Color.GRAY);
				Location.mVideosImage_RightMenu.setAlpha(100);
			}else{
				//Location.mVideos_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mVideos_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mVideos_RightMenu.setEnabled(true);
				Location.mVideosText_RightMenu.setTextColor(Color.WHITE);
				Location.mVideosImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_contactstore_flag.equalsIgnoreCase("no")){
				Location.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mContactStore_RightMenu.setEnabled(false);
				Location.mContactStoreText_RightMenu.setTextColor(Color.GRAY);
				Location.mContactStoreImage_RightMenu.setAlpha(100);
			}else{
				//Location.mContactStore_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mContactStore_RightMenu.setEnabled(true);
				Location.mContactStoreText_RightMenu.setTextColor(Color.WHITE);
				Location.mContactStoreImage_RightMenu.setAlpha(255);
			}
			if(RightMenuStoreId_ClassVariables.rightmenu_share_flag.equalsIgnoreCase("no")){
				Location.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
				Location.mSocial_RightMenu.setEnabled(false);
				Location.mSocialText_RightMenu.setTextColor(Color.GRAY);
				Location.mSocialImage_RightMenu.setAlpha(100);
			}else{
				//Location.mSocial_RightMenu.setBackgroundResource(R.drawable.gradient_bg);
				Location.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.litegrey));
				Location.mSocial_RightMenu.setEnabled(true);
				Location.mSocialText_RightMenu.setTextColor(Color.WHITE);
				Location.mSocialImage_RightMenu.setAlpha(255);
			}
		}else{ // Guest
			// Mobile Pay
			Location.mMobilePay_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			Location.mMobilePay_RightMenu.setEnabled(false);
			Location.mMobilePayText_RightMenu.setTextColor(Color.GRAY);
			Location.mMobilePayImage_RightMenu.setAlpha(100);
			// MyGiftCards
			Location.mGiftCards_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			Location.mGiftCards_RightMenu.setEnabled(false);
			Location.mGiftCardsText_RightMenu.setTextColor(Color.GRAY);
			Location.mGiftCardsImage_RightMenu.setAlpha(100);
			// DealCards
			Location.mDeals_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			Location.mDeals_RightMenu.setEnabled(false);
			Location.mDealsText_RightMenu.setTextColor(Color.GRAY);
			Location.mDealsImage_RightMenu.setAlpha(100);
			// Share
			Location.mSocial_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			Location.mSocial_RightMenu.setEnabled(false);
			Location.mSocialText_RightMenu.setTextColor(Color.GRAY);
			Location.mSocialImage_RightMenu.setAlpha(100);
			// Contact store
			Location.mContactStore_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			Location.mContactStore_RightMenu.setEnabled(false);
			Location.mContactStoreText_RightMenu.setTextColor(Color.GRAY);
			Location.mContactStoreImage_RightMenu.setAlpha(100);
			// Favorite
			Location.mFavorite_RightMenu.setBackgroundColor(context.getResources().getColor(R.color.translucent_white));
			Location.mFavorite_RightMenu.setEnabled(false);
			Location.mFavoriteText_RightMenu.setTextColor(Color.GRAY);
			Location.mFavoriteImage_RightMenu.setAlpha(100);
		}
	}
}