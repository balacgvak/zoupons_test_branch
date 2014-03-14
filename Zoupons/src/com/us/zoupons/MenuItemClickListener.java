package com.us.zoupons;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.us.zoupons.android.asyncthread_class.AddorRemoveFavoriteAsynchThread;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.flagclasses.MenuBarFlag;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.flagclasses.ZPayFlag;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.mobilepay.NormalPaymentAsyncTask;
import com.us.zoupons.mobilepay.MobilePay;
import com.us.zoupons.mobilepay.ZpayStep2SearchEnable;
import com.us.zoupons.shopper.cards.FriendList;
import com.us.zoupons.shopper.cards.StoreCardDetails;
import com.us.zoupons.shopper.chatsupport.ChatSupport;
import com.us.zoupons.shopper.chatsupport.ContactStore;
import com.us.zoupons.shopper.coupons.StoreCoupons;
import com.us.zoupons.shopper.favorite.Favorites;
import com.us.zoupons.shopper.friends.Friends;
import com.us.zoupons.shopper.giftcards_deals.MyGiftCards;
import com.us.zoupons.shopper.giftcards_deals.TransactionHistory;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.invoice.InvoiceApproval;
import com.us.zoupons.shopper.location.Location;
import com.us.zoupons.shopper.photos.StorePhotoSwitcher;
import com.us.zoupons.shopper.receipts.Receipts;
import com.us.zoupons.shopper.refer_store.ReferStore;
import com.us.zoupons.shopper.reviews.StoreReviews;
import com.us.zoupons.shopper.settings.Settings;
import com.us.zoupons.shopper.share.ShareStore;
import com.us.zoupons.shopper.store_info.StoreInformation;
import com.us.zoupons.shopper.video.StoreVideos;
import com.us.zoupons.shopper.wallet.ManageWallets;

/**
 * 
 * Class to listen and open respective activity when we choose left and right menu
 *
 */


public class MenuItemClickListener implements OnClickListener{

	private Activity mActivityContext;
	private String mClassName;
	private ListView mListview;
	public String mTAG="MenuItemClickListener";
		
	public MenuItemClickListener(View items,Activity context,String classname){
		super();
		this.mActivityContext=context;
		this.mClassName=classname;
	}
	
	public MenuItemClickListener(View items,Activity context,String classname,ListView listview){
		super();
		this.mActivityContext=context;
		this.mClassName=classname;
		this.mListview = listview;
	}
		
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.mainmenu_home:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			stopTimersIfAnyRunning();
			ZPayFlag.setFlag(0);
    
			if(!POJOMainMenuActivityTAG.TAG.equals("ShopperHomePage")){ // Launch home activity if we click HOME from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_home = new Intent().setClass(mActivityContext.getApplicationContext(),ShopperHomePage.class);
				intent_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_home);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.mainmenu_zpay:
			/*v.setBackgroundResource(R.drawable.gradient_bg_hover);
			stopTimersIfAnyRunning();

			if(!POJOMainMenuActivityTAG.TAG.equals("MobilePay")){ // Launch Mobile activity if we click MobilePay from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				// Intent to call mobile pay step-2 
				Intent intent_step2 = new Intent(mActivityContext,MobilePay.class);
				intent_step2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent_step2.putExtra("datasourcename", "zpay");
				mActivityContext.startActivity(intent_step2);
				ZpayStep2SearchEnable.searchEnableFlag = false;
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}*/
			break;

	   case R.id.mainmenu_invoicecenter:
		   /*v.setBackgroundResource(R.drawable.gradient_bg_hover);

			stopTimersIfAnyRunning();

			if(!this.mClassName.equals("Invoice")){ // Launch Invoice if we click INVOICE_CENTER from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_invoice_center = new Intent(mActivityContext.getApplicationContext(),InvoiceApproval.class);
				intent_invoice_center.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_invoice_center);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}*/
			break;
		case R.id.mainmenu_zgiftcards:
			/*v.setBackgroundResource(R.drawable.gradient_bg_hover);
			stopTimersIfAnyRunning();
			
			if(!POJOMainMenuActivityTAG.TAG.equals("MyGiftCards")){ // Launch my giftcards activity if we click GIFTCARD from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_zgiftcards = new Intent().setClass(mActivityContext.getApplicationContext(),MyGiftCards.class);
				intent_zgiftcards.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_zgiftcards);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}*/
			break;
		case R.id.mainmenu_managecards:
			/*v.setBackgroundResource(R.drawable.gradient_bg_hover);
			stopTimersIfAnyRunning();

			if(!POJOMainMenuActivityTAG.TAG.equals("ManageCards")){ // Launch wallet activity if we click WALLET from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_managecards = new Intent().setClass(mActivityContext.getApplicationContext(),ManageWallets.class);
				intent_managecards.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_managecards);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}*/
			break;
		case R.id.mainmenu_receipts:
			/*v.setBackgroundResource(R.drawable.gradient_bg_hover);
			stopTimersIfAnyRunning();

			if(!POJOMainMenuActivityTAG.TAG.equals("Receipts")){ // Launch receipts activity if we click RECEIPTS from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_receipts = new Intent().setClass(mActivityContext.getApplicationContext(),Receipts.class);
				intent_receipts.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_receipts);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}*/
			break;
		case R.id.mainmenu_myfavourites:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			stopTimersIfAnyRunning();			

			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_Favorites")){ // Launch Favorites activity if we click FAVORITE from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_favorites = new Intent().setClass(mActivityContext.getApplicationContext(), Favorites.class);
				intent_favorites.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_favorites);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.mainmenu_myfriends:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			stopTimersIfAnyRunning();

			if(!POJOMainMenuActivityTAG.TAG.equals("Friends")){ // Launch friends activity if we click FRIEND from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_myfriends = new Intent().setClass(mActivityContext.getApplicationContext(), Friends.class);
				intent_myfriends.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_myfriends);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.mainmenu_customercenter:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			stopTimersIfAnyRunning();
            Log.i("TAG Value", POJOMainMenuActivityTAG.TAG);
			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_CustomerCenter")){ // Launch Customercenter activity if we click CUSTOMERCENTER from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_talktous = new Intent().setClass(mActivityContext.getApplicationContext(), ChatSupport.class);
				intent_talktous.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_talktous);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.mainmenu_rewards:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);

			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(mTAG,"Timer Stopped Successfully");
			}

			try{
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_Rewards")){ // Launch rewards activity if we click REWARDS from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_rewards = new Intent().setClass(mActivityContext.getApplicationContext(), ReferStore.class);
				intent_rewards.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_rewards);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.mainmenu_settings:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			stopTimersIfAnyRunning();

			if(!POJOMainMenuActivityTAG.TAG.equals("Settings")){ // Launch settings activity if we click SETTING from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_settings = new Intent().setClass(mActivityContext.getApplicationContext(),Settings.class);
				intent_settings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_settings);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.mainmenu_logout:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			stopTimersIfAnyRunning();
			closeMenu(POJOMainMenuActivityTAG.TAG,true);
			break;
		case R.id.rightmenu_info:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);

			try{
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_StoreInfo")){ // Launch Store information activity if we click INFO from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_storeinfo = new Intent().setClass(mActivityContext.getApplicationContext(),StoreInformation.class);
				intent_storeinfo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_storeinfo);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_mobilepay:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);

			try{
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			if(!POJOMainMenuActivityTAG.TAG.equals("MobilePay")){ // Launch mobilepay activity if we click MOBILEPAY from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_step2mobilepay = new Intent().setClass(mActivityContext.getApplicationContext(),MobilePay.class);
				intent_step2mobilepay.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent_step2mobilepay.putExtra("datasourcename", "zpay");
				mActivityContext.startActivity(intent_step2mobilepay);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_giftcards:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			try{
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_GiftCardDetails")){ // Launch store giftcards activity if we click GIFTCARDS from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_storegiftcards = new Intent().setClass(mActivityContext.getApplicationContext(),StoreCardDetails.class);
				intent_storegiftcards.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent_storegiftcards.putExtra("CardType", "Regular");
				mActivityContext.startActivity(intent_storegiftcards);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_deals:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);

			try{
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_deals")){ // Launch store deals activity if we click DEALS from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_deals = new Intent().setClass(mActivityContext.getApplicationContext(),StoreCardDetails.class);
				intent_deals.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent_deals.putExtra("CardType", "ZCard");
				mActivityContext.startActivity(intent_deals);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_coupons:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);

			try{
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_Coupons")){ // Launch coupons activity if we click COUPONS from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_coupons = new Intent().setClass(mActivityContext.getApplicationContext(),StoreCoupons.class);
				intent_coupons.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_coupons);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_social:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);

			try{
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_Social")){ // Launch social activity if we click SOCIAL from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_social = new Intent().setClass(mActivityContext.getApplicationContext(),ShareStore.class);
				intent_social.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_social);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_reviews:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);

			try{
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_Reviews")){ // Launch reviews activity if we click REVIEWS from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_reviews = new Intent().setClass(mActivityContext.getApplicationContext(),StoreReviews.class);
				intent_reviews.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_reviews);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_locations:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);

			try{
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			MenuBarFlag.mMenuBarFlag=5;	// Set flag for Location Mapview Callout tap.
			if(!POJOMainMenuActivityTAG.TAG.equals("Location")){ // Launch store Location activity if we click LOCATION from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_location = new Intent().setClass(mActivityContext.getApplicationContext(),Location.class);;
				intent_location.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_location);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_photos:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			try{
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_StorePhotos")){ // Launch store photos activity if we click PHOTOS from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_photos = new Intent(mActivityContext.getApplicationContext(),StorePhotoSwitcher.class);
				intent_photos.putExtra("position", 0);
				intent_photos.putExtra("Activity", "Photos");
				intent_photos.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_photos);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_videos:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			try{
				if(MainMenuActivity.mCommunicationTimer!=null){
					MainMenuActivity.mCommunicationTimer.cancel();
					MainMenuActivity.mCommunicationTimer=null;
				}if(MainMenuActivity.mCommunicationTimerTask!=null){
					MainMenuActivity.mCommunicationTimerTask.cancel();
					MainMenuActivity.mCommunicationTimerTask=null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_PlayVideo")){ // Launch Store Video activity if we click VIDEOS from other activitys
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_videoplay = new Intent().setClass(mActivityContext.getApplicationContext(),StoreVideos.class);
				intent_videoplay.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_videoplay);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_contactstore:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);

			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_ContactStore")){
				try{
					if(MainMenuActivity.mCommunicationTimer!=null){
						MainMenuActivity.mCommunicationTimer.cancel();
						MainMenuActivity.mCommunicationTimer=null;
					}if(MainMenuActivity.mCommunicationTimerTask!=null){
						MainMenuActivity.mCommunicationTimerTask.cancel();
						MainMenuActivity.mCommunicationTimerTask=null;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_videoplay = new Intent().setClass(mActivityContext.getApplicationContext(),ContactStore.class);
				intent_videoplay.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivityContext.startActivity(intent_videoplay);
				mActivityContext.finish();
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_favorite:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			if(mListview!= null){ // For shopperHomePage,Locations,CardPurchase
				new AddorRemoveFavoriteAsynchThread(this.mActivityContext,mListview).execute(RightMenuStoreId_ClassVariables.mStoreID,UserDetails.mServiceUserId,mClassName);
			}else {
				new AddorRemoveFavoriteAsynchThread(this.mActivityContext).execute(RightMenuStoreId_ClassVariables.mStoreID,UserDetails.mServiceUserId,POJOMainMenuActivityTAG.TAG);
			}
			break;	
		case R.id.rightmenu_sendtofriend:
			closeMenu(POJOMainMenuActivityTAG.TAG,false);

			Intent intent_fbfriendslist = new Intent(this.mActivityContext,FriendList.class);
			intent_fbfriendslist.putExtra("classname", this.mClassName);
			mActivityContext.startActivity(intent_fbfriendslist);
			break;
		case R.id.rightmenu_selfuse:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);

			closeMenu(POJOMainMenuActivityTAG.TAG,false);
			Intent intent_step2 = new Intent(this.mActivityContext,MobilePay.class);
			intent_step2.putExtra("datasourcename", "zpay"); // flag to set control from giftcard self use menu
			intent_step2.putExtra("card_id", MenuUtilityClass.sGiftCardId); // selected giftcard id
			intent_step2.putExtra("card_purchase_id", MenuUtilityClass.sGiftCardPurchaseId); // selected giftcard purchase id
			intent_step2.putExtra("cardvalue", String.valueOf(MenuUtilityClass.sGiftCardBalanceAmount));	//Selected GiftCard balance amount
			intent_step2.putExtra("cardposition", MenuUtilityClass.sChoosedGiftcardPosition); // selected Giftcard position 
			intent_step2.putExtra("giftcard_storeid", MenuUtilityClass.sGiftCard_StoreId);
			intent_step2.putExtra("giftcard_locationid", MenuUtilityClass.sGiftCardStoreLocationId);
			intent_step2.putExtra("giftcard_storename", MenuUtilityClass.sGiftCard_StoreName);
			intent_step2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			mActivityContext.startActivity(intent_step2);
			break;
		case R.id.rightmenu_transactionhistory:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			closeMenu(POJOMainMenuActivityTAG.TAG,false);
			mActivityContext.startActivity(new Intent(mActivityContext,TransactionHistory.class));
			break;
		}
	}

	private void stopTimersIfAnyRunning() {
		// TODO Auto-generated method stub
		if(NormalPaymentAsyncTask.mCountDownTimer!=null){
			NormalPaymentAsyncTask.mCountDownTimer.cancel();
			NormalPaymentAsyncTask.mCountDownTimer = null;
			Log.i(mTAG,"Timer Stopped Successfully");
		}
		// To cancel Rewards advertisement task
		if(MainMenuActivity.mTimer!=null){
			MainMenuActivity.mTimer.cancel();
			MainMenuActivity.mTimer = null;
			Log.i(mTAG,"Rewards Timer Stopped Successfully");
		}
		
		try{
			if(MainMenuActivity.mCommunicationTimer!=null){
				MainMenuActivity.mCommunicationTimer.cancel();
				MainMenuActivity.mCommunicationTimer=null;
			}if(MainMenuActivity.mCommunicationTimerTask!=null){
				MainMenuActivity.mCommunicationTimerTask.cancel();
				MainMenuActivity.mCommunicationTimerTask=null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	// To close sliding menu
	public void closeMenu(String classname,boolean toCloseMenuFlag){
		if(classname.equals("ShopperHomePage")){
			MenuOutClass.HOMEPAGE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("ShopperHomePage");
		}if(classname.equals("CardPurchase")){
			MenuOutClass.ZPAY_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("CardPurchase");
		}else if(classname.equals("Invoice")){
			MenuOutClass.FRIENDS_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("Invoice");
		}else if(classname.equals("MainMenuActivity_")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_Rewards")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_StoreInfo")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_StorePhotos")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_GiftCardDetails")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_deals")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_FbFriendsList")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_FbNotesDescription")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_Favorites")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_Social")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_PlayVideo")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_Coupons")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_CouponDetail")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_Transactionhistory")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity_Transactionhistory");
		}else if(classname.equals("Location")){
			MenuOutClass.LOCATION_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("Location");
		}else if(classname.equals("ManageCards")){
			MenuOutClass.MANAGECARDS_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("ManageCards");
		}else if(classname.equals("Friends")){
			MenuOutClass.FRIENDS_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("Friends");
		}else if(classname.equals("MainMenuActivity_CustomerCenter")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity_CustomerCenter");
		}else if(classname.equals("Settings")){
			MenuOutClass.SETTINGS_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("Settings");
		}else if(classname.equals("NotificationSettings")){
			MenuOutClass.NOTIFICATIONSETTINGS_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("NotificationSettings");
		}else if(classname.equals("MobilePay")){
			MenuOutClass.STEP2_MANAGECARDS_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MobilePay");
		}else if(classname.equals("MainMenuActivity_Reviews")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("MainMenuActivity_ContactStore")){
			MenuOutClass.WHOLE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MainMenuActivity");
		}else if(classname.equals("AddCreditCard")){
			MenuOutClass.ADDCREDITCARD_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("AddCreditCard");
		}else if(classname.equals("Approve")){
			MenuOutClass.APPROVE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("Approve");
		}else if(classname.equals("MyGiftCards")){
			MenuOutClass.GIFTCARDS_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("MyGiftCards");
		}else if(classname.equals("UploadReceipt")){
			MenuOutClass.UPLOADRECEIPT_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("UploadReceipt");
		}else if(classname.equals("Receipts")){
			MenuOutClass.RECEIPTS_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("Receipts");
		}
	}
}