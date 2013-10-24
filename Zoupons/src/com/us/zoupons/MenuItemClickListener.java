package com.us.zoupons;

import java.util.Timer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.Communication.CommunicationTimerTask;
import com.us.zoupons.Communication.ContactStore;
import com.us.zoupons.Coupons.CouponsActivity;
import com.us.zoupons.CustomerService.customercenter;
import com.us.zoupons.FlagClasses.MenuBarFlag;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.FlagClasses.ZPayFlag;
import com.us.zoupons.GiftCards.TransactionHistory;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.Reviews.Reviews;
import com.us.zoupons.StoreInfo.MainActivity;
import com.us.zoupons.StoreInfo.StorePhotoSwitcher;
import com.us.zoupons.VideoPlay.PlayVideo_New;
import com.us.zoupons.android.AsyncThreadClasses.AddorRemoveFavoriteAsynchThread;
import com.us.zoupons.cards.StoreGiftCardDetails;
import com.us.zoupons.friends.Friends;
import com.us.zoupons.rewards.Rewards;
import com.us.zoupons.social.Social;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;
import com.us.zoupons.zpay.Step2_ManageCards;

public class MenuItemClickListener implements OnClickListener{

	View menuItem;
	Context mActivityContext;
	String mClassName;
	public static String TAG="MenuItemClickListener";
	/*Timer mCommunicationTimer;
	CommunicationTimerTask mCommunicationTimerTask;*/
	
	public MenuItemClickListener(View items,Context context,String classname){
		super();
		this.menuItem=items;
		this.mActivityContext=context;
		this.mClassName=classname;
	}
	
	/*//Constructor called from ContactStore and TalkToUs
	public MenuItemClickListener(View items,Context context,String classname,Timer timer,CommunicationTimerTask timertask){
		super();
		this.menuItem=items;
		this.mActivityContext=context;
		this.mClassName=classname;
		mCommunicationTimer = timer;
		mCommunicationTimerTask = timertask;
	}*/
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.mainmenu_home:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			
			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(TAG,"Timer Stopped Successfully");
			}
			// To cancel Rewards advertisement task
			if(MainMenuActivity.mTimer!=null){
				MainMenuActivity.mTimer.cancel();
				MainMenuActivity.mTimer = null;
				Log.i(TAG,"Rewards Timer Stopped Successfully");
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
			
			stopPhotoLoader();
			
			ZPayFlag.setFlag(0);
			SlidingView.setZpayMenuButtonChange();
			if(!POJOMainMenuActivityTAG.TAG.equals("SlidingView")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_home = new Intent().setClass(mActivityContext.getApplicationContext(),SlidingView.class);
				mActivityContext.startActivity(intent_home);
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
			/*case R.id.mainmenu_zpay:

			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			
			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(TAG,"Timer Stopped Successfully");
			}
			// To cancel Rewards advertisement task
			if(MainMenuActivity.mTimer!=null){
				MainMenuActivity.mTimer.cancel();
				MainMenuActivity.mTimer = null;
				Log.i(TAG,"Rewards Timer Stopped Successfully");
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
				
			stopPhotoLoader();
			
			if(!this.mClassName.equals("zpay_step1")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				// Intent to call mobile pay step-2 
				Intent intent_step2 = new Intent(mActivityContext,Step2_ManageCards.class);
				intent_step2.putExtra("datasourcename", "zpay");
				mActivityContext.startActivity(intent_step2);
				ZpayStep2SearchEnable.searchEnableFlag = false;
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;*/
			/*case R.id.mainmenu_invoicecenter:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);

			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(TAG,"Timer Stopped Successfully");
			}
			// To cancel Rewards advertisement task
			if(MainMenuActivity.mTimer!=null){
				MainMenuActivity.mTimer.cancel();
				MainMenuActivity.mTimer = null;
				Log.i(TAG,"Rewards Timer Stopped Successfully");
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
				
			stopPhotoLoader();
			
			if(!this.mClassName.equals("Invoice")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_menubarzsend = new Intent(mActivityContext.getApplicationContext(),InvoiceApproval.class);
				mActivityContext.startActivity(intent_menubarzsend);
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;*/
			/*case R.id.mainmenu_zgiftcards:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			
			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(TAG,"Timer Stopped Successfully");
			}
			// To cancel Rewards advertisement task
			if(MainMenuActivity.mTimer!=null){
				MainMenuActivity.mTimer.cancel();
				MainMenuActivity.mTimer = null;
				Log.i(TAG,"Rewards Timer Stopped Successfully");
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
				
			stopPhotoLoader();
			SlidingView.setZpayMenuButtonChange();
			//closeMenu(POJOMainMenuActivityTAG.TAG);
			if(!POJOMainMenuActivityTAG.TAG.equals("GiftCards")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_zgiftcards = new Intent().setClass(mActivityContext.getApplicationContext(),GiftCards.class);
				mActivityContext.startActivity(intent_zgiftcards);
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;*/
			/*case R.id.mainmenu_managecards:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			
			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(TAG,"Timer Stopped Successfully");
			}
			// To cancel Rewards advertisement task
			if(MainMenuActivity.mTimer!=null){
				MainMenuActivity.mTimer.cancel();
				MainMenuActivity.mTimer = null;
				Log.i(TAG,"Rewards Timer Stopped Successfully");
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
				
			stopPhotoLoader();
			SlidingView.setZpayMenuButtonChange();
			if(!POJOMainMenuActivityTAG.TAG.equals("ManageCards")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_managecards = new Intent().setClass(mActivityContext.getApplicationContext(),ManageCards.class);
				mActivityContext.startActivity(intent_managecards);
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
			case R.id.mainmenu_receipts:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			
			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(TAG,"Timer Stopped Successfully");
			}
			// To cancel Rewards advertisement task
			if(MainMenuActivity.mTimer!=null){
				MainMenuActivity.mTimer.cancel();
				MainMenuActivity.mTimer = null;
				Log.i(TAG,"Rewards Timer Stopped Successfully");
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
				
			stopPhotoLoader();
			SlidingView.setZpayMenuButtonChange();
			if(!POJOMainMenuActivityTAG.TAG.equals("Receipts")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_receipts = new Intent().setClass(mActivityContext.getApplicationContext(),Receipts.class);
				mActivityContext.startActivity(intent_receipts);
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;*/	
		case R.id.mainmenu_myfavourites:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			
			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(TAG,"Timer Stopped Successfully");
			}
			// To cancel Rewards advertisement task
			if(MainMenuActivity.mTimer!=null){
				MainMenuActivity.mTimer.cancel();
				MainMenuActivity.mTimer = null;
				Log.i(TAG,"Rewards Timer Stopped Successfully");
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
			
			stopPhotoLoader();
			SlidingView.setZpayMenuButtonChange();

			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_Favorites")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_favorites = new Intent().setClass(mActivityContext.getApplicationContext(), Favorites.class);
				mActivityContext.startActivity(intent_favorites);
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.mainmenu_myfriends:
			SlidingView.setZpayMenuButtonChange();
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			
			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(TAG,"Timer Stopped Successfully");
			}
			// To cancel Rewards advertisement task
			if(MainMenuActivity.mTimer!=null){
				MainMenuActivity.mTimer.cancel();
				MainMenuActivity.mTimer = null;
				Log.i(TAG,"Rewards Timer Stopped Successfully");
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
			
			stopPhotoLoader();
			SlidingView.setZpayMenuButtonChange();
			if(!POJOMainMenuActivityTAG.TAG.equals("Friends")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_myfriends = new Intent().setClass(mActivityContext.getApplicationContext(), Friends.class);
				mActivityContext.startActivity(intent_myfriends);
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.mainmenu_customercenter:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			
			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(TAG,"Timer Stopped Successfully");
			}
			// To cancel Rewards advertisement task
			if(MainMenuActivity.mTimer!=null){
				MainMenuActivity.mTimer.cancel();
				MainMenuActivity.mTimer = null;
				Log.i(TAG,"Rewards Timer Stopped Successfully");
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
			
			stopPhotoLoader();
			SlidingView.setZpayMenuButtonChange();
			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_CustomerCenter")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_talktous = new Intent().setClass(mActivityContext.getApplicationContext(), customercenter.class);
				mActivityContext.startActivity(intent_talktous);
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.mainmenu_rewards:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			
			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(TAG,"Timer Stopped Successfully");
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
			
			stopPhotoLoader();
			SlidingView.setZpayMenuButtonChange();
			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_Rewards")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_rewards = new Intent().setClass(mActivityContext.getApplicationContext(), Rewards.class);
				mActivityContext.startActivity(intent_rewards);
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.mainmenu_settings:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			
			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(TAG,"Timer Stopped Successfully");
			}
			// To cancel Rewards advertisement task
			if(MainMenuActivity.mTimer!=null){
				MainMenuActivity.mTimer.cancel();
				MainMenuActivity.mTimer = null;
				Log.i(TAG,"Rewards Timer Stopped Successfully");
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
			
			stopPhotoLoader();
			SlidingView.setZpayMenuButtonChange();
			if(!POJOMainMenuActivityTAG.TAG.equals("Settings")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_settings = new Intent().setClass(mActivityContext.getApplicationContext(),Settings.class);
				mActivityContext.startActivity(intent_settings);
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.mainmenu_logout:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			
			if(NormalPaymentAsyncTask.mCountDownTimer!=null){
				NormalPaymentAsyncTask.mCountDownTimer.cancel();
				NormalPaymentAsyncTask.mCountDownTimer = null;
				Log.i(TAG,"Timer Stopped Successfully");
			}
			// To cancel Rewards advertisement task
			if(MainMenuActivity.mTimer!=null){
				MainMenuActivity.mTimer.cancel();
				MainMenuActivity.mTimer = null;
				Log.i(TAG,"Rewards Timer Stopped Successfully");
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
			
			stopPhotoLoader();
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
			
			stopPhotoLoader();
			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_StoreInfo")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_storeinfo = new Intent().setClass(mActivityContext.getApplicationContext(),MainActivity.class);
				mActivityContext.startActivity(intent_storeinfo);
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
			
			stopPhotoLoader();
			if(!POJOMainMenuActivityTAG.TAG.equals("Step2_ManageCards")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_step2mobilepay = new Intent().setClass(mActivityContext.getApplicationContext(),Step2_ManageCards.class);
				intent_step2mobilepay.putExtra("datasourcename", "zpay");
				mActivityContext.startActivity(intent_step2mobilepay);
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
			
			stopPhotoLoader();
			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_GiftCardDetails")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_storegiftcards = new Intent().setClass(mActivityContext.getApplicationContext(),StoreGiftCardDetails.class);
				intent_storegiftcards.putExtra("CardType", "Regular");
				mActivityContext.startActivity(intent_storegiftcards);
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
			
			stopPhotoLoader();
			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_deals")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_deals = new Intent().setClass(mActivityContext.getApplicationContext(),StoreGiftCardDetails.class);
				intent_deals.putExtra("CardType", "ZCard");
				mActivityContext.startActivity(intent_deals);
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
			
			stopPhotoLoader();
			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_Coupons")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_coupons = new Intent().setClass(mActivityContext.getApplicationContext(),CouponsActivity.class);
				mActivityContext.startActivity(intent_coupons);
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
			
			stopPhotoLoader();
			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_Social")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_social = new Intent().setClass(mActivityContext.getApplicationContext(),Social.class);
				mActivityContext.startActivity(intent_social);
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
			
			stopPhotoLoader();
			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_Reviews")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_reviews = new Intent().setClass(mActivityContext.getApplicationContext(),Reviews.class);
				mActivityContext.startActivity(intent_reviews);
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
			
			stopPhotoLoader();
			MenuBarFlag.mMenuBarFlag=5;	// Set flag for Location Mapview Callout tap.
			if(!POJOMainMenuActivityTAG.TAG.equals("Location")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_location = new Intent().setClass(mActivityContext.getApplicationContext(),Location.class);;
				mActivityContext.startActivity(intent_location);
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
			
			stopPhotoLoader();
			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_StorePhotos")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_photos = new Intent(mActivityContext.getApplicationContext(),StorePhotoSwitcher.class);
				intent_photos.putExtra("position", 0);
				intent_photos.putExtra("Activity", "Photos");
				mActivityContext.startActivity(intent_photos);
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
			
			stopPhotoLoader();
			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_PlayVideo")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_videoplay = new Intent().setClass(mActivityContext.getApplicationContext(),PlayVideo_New.class);
				mActivityContext.startActivity(intent_videoplay);
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_contactstore:
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
			
			stopPhotoLoader();
			if(!POJOMainMenuActivityTAG.TAG.equals("MainMenuActivity_ContactStore")){
				closeMenu(POJOMainMenuActivityTAG.TAG,true);
				Intent intent_videoplay = new Intent().setClass(mActivityContext.getApplicationContext(),ContactStore.class);
				mActivityContext.startActivity(intent_videoplay);
			}else{
				closeMenu(POJOMainMenuActivityTAG.TAG,false);
			}
			break;
		case R.id.rightmenu_favorite:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			
			new AddorRemoveFavoriteAsynchThread(this.mActivityContext).execute(RightMenuStoreId_ClassVariables.mStoreID,UserDetails.mServiceUserId,mClassName);
			break;	
			/*case R.id.rightmenu_sendtofriend:
			closeMenu(POJOMainMenuActivityTAG.TAG,false);
			
			Intent intent_fbfriendslist = new Intent(this.mActivityContext,FBFriendList.class);
			intent_fbfriendslist.putExtra("classname", this.mClassName);
			mActivityContext.startActivity(intent_fbfriendslist);
			break;*/
		case R.id.rightmenu_selfuse:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);

			closeMenu(POJOMainMenuActivityTAG.TAG,false);
			Intent intent_step2 = new Intent(this.mActivityContext,Step2_ManageCards.class);
			intent_step2.putExtra("datasourcename", "zpay"); // flag to set control from giftcard self use menu
			intent_step2.putExtra("card_id", MenuUtilityClass.mGiftCardId); // selected giftcard id
			intent_step2.putExtra("card_purchase_id", MenuUtilityClass.mGiftCardPurchaseId); // selected giftcard purchase id
			intent_step2.putExtra("cardvalue", String.valueOf(MenuUtilityClass.mGiftCardBalanceAmount));	//Selected GiftCard balance amount
			intent_step2.putExtra("cardposition", MenuUtilityClass.mChoosedGiftcardPosition); // selected Giftcard position 
			intent_step2.putExtra("giftcard_storeid", MenuUtilityClass.mGiftCard_StoreId);
			intent_step2.putExtra("giftcard_locationid", MenuUtilityClass.mGiftCardStoreLocationId);
			intent_step2.putExtra("giftcard_storename", MenuUtilityClass.mGiftCard_StoreName);
			mActivityContext.startActivity(intent_step2);
			break;
		case R.id.rightmenu_transactionhistory:
			v.setBackgroundResource(R.drawable.gradient_bg_hover);
			closeMenu(POJOMainMenuActivityTAG.TAG,false);
			mActivityContext.startActivity(new Intent(mActivityContext,TransactionHistory.class));
			break;
		}
	}

	/**
	 * Function to stop photo loading in store photos page if it is running
	 * */
	public void stopPhotoLoader(){
		if(MainMenuActivity.storePhotoLoaderAsyncTask!=null){
			if(MainMenuActivity.storePhotoLoaderAsyncTask.getStatus()==Status.RUNNING){
				MainMenuActivity.storePhotoLoaderAsyncTask.cancel(true);
			}	
			MainMenuActivity.storePhotoLoaderAsyncTask=null;
		}
	}
	
	public void closeMenu(String classname,boolean toCloseMenuFlag){
		if(classname.equals("SlidingView")){
			MenuOutClass.HOMEPAGE_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("SlidingView");
		}if(classname.equals("zpay_step1")){
			MenuOutClass.ZPAY_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("zpay_step1");
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
		}else if(classname.equals("Step2_ManageCards")){
			MenuOutClass.STEP2_MANAGECARDS_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("Step2_ManageCards");
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
		}else if(classname.equals("GiftCards")){
			MenuOutClass.GIFTCARDS_MENUOUT=false;
			if(!toCloseMenuFlag)
				OpenMenu.toCloseMenu("GiftCards");
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