package com.us.zoupons.notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.mobilepay.NormalPaymentAsyncTask;
import com.us.zoupons.shopper.chatsupport.ChatSupport;
import com.us.zoupons.shopper.chatsupport.ZouponsSupport;
import com.us.zoupons.shopper.favorite.Favorites;
import com.us.zoupons.shopper.giftcards_deals.MyGiftCards;
import com.us.zoupons.shopper.invoice.InvoiceApproval;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.customercenter.StoreOwner_chatsupport;

/**
 * 
 * Custom Notification list item click listener
 *
 */

public class NotificationItemClickListener implements OnItemClickListener{

	private String TAG="NotificationItemClickListener",mModuleFlag;

	public NotificationItemClickListener(String mModuleFlag) {
		// TODO Auto-generated constructor stub
		this.mModuleFlag = mModuleFlag;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterview, View arg1, int arg2, long arg3) {
		try{
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

		NotificationDetails mNotificationDetails = (NotificationDetails) adapterview.getItemAtPosition(arg2);
		if(mNotificationDetails!= null && !mNotificationDetails.notification_shortmessage.equals("")){
			
			if(mNotificationDetails.notification_type.equalsIgnoreCase("store_message")){ // If store sends a message
				MenuOutClass.WHOLE_MENUOUT=false; // To clear sliding menu variable
				Intent intent_customer_service = new Intent().setClass(arg1.getContext(),ChatSupport.class);
				intent_customer_service.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				arg1.getContext().startActivity(intent_customer_service);	
			}else if(mNotificationDetails.notification_type.equalsIgnoreCase("customer_message")){ // Customer sends a message
				
				StoreOwner_RightMenu.mCustomer_id = mNotificationDetails.mCustomerID;
				StoreOwner_chatsupport.sCustomerName = mNotificationDetails.full_name;
				StoreOwner_RightMenu.mCustomer_FirstName = mNotificationDetails.mCustomerFirstName;
				StoreOwner_RightMenu.mCustomer_LastName = mNotificationDetails.mCustomerLastName;
				StoreOwner_RightMenu.mCustomer_ProfileImage = mNotificationDetails.friend_image_path;
				// To save email enable status and use it in other pages while opening customercenter right menu...
				SharedPreferences mPrefs = arg1.getContext().getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				Editor editor = mPrefs.edit();
				editor.putString("email_status",mNotificationDetails.mIsFavoriteStore);
				editor.commit();
				
				Intent intent_talktous = new Intent(arg1.getContext(),StoreOwner_chatsupport.class);
				intent_talktous.putExtra("class_name","Contact_store");
				intent_talktous.putExtra("source", "customercenter");
				intent_talktous.putExtra("customer_id",StoreOwner_RightMenu.mCustomer_id);
				arg1.getContext().startActivity(intent_talktous);
			}else if(mNotificationDetails.notification_type.equalsIgnoreCase("zoupons_message")){ // Zoupons sends a message
				if(mModuleFlag.equalsIgnoreCase("customer")){ // Message to customer
					MenuOutClass.WHOLE_MENUOUT=false;
					Intent intent_talktous = new Intent(arg1.getContext(),ZouponsSupport.class);
					intent_talktous.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					intent_talktous.putExtra("hide_back", true);
					arg1.getContext().startActivity(intent_talktous);
				}else{ // Message to store module
					Intent intent_talktous = new Intent(arg1.getContext(),StoreOwner_chatsupport.class);
					intent_talktous.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					intent_talktous.putExtra("source", "talk_to_us");
					intent_talktous.putExtra("class_name", "talk_to_us");
					arg1.getContext().startActivity(intent_talktous);
				}
			}else if(mNotificationDetails.notification_type.equalsIgnoreCase("invoice_received")){ // If Invoice received from store  
				MenuOutClass.WHOLE_MENUOUT=false;
				Intent intent_invoice_center = new Intent(arg1.getContext(),InvoiceApproval.class);
				intent_invoice_center.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				arg1.getContext().startActivity(intent_invoice_center);
			}else if(mNotificationDetails.notification_type.equalsIgnoreCase("giftcard_received")){ // If giftcard sent from friend
				MenuOutClass.WHOLE_MENUOUT=false;
				Intent intent_zgiftcards = new Intent().setClass(arg1.getContext(),MyGiftCards.class);
				intent_zgiftcards.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				arg1.getContext().startActivity(intent_zgiftcards);
			}else if(mNotificationDetails.notification_type.equalsIgnoreCase("private_coupon_recieved")){ //If private coupon received
				MenuOutClass.WHOLE_MENUOUT=false;
				Intent intent_favorites = new Intent().setClass(arg1.getContext().getApplicationContext(), Favorites.class);
				intent_favorites.putExtra("FromNotifications", true);
				intent_favorites.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				arg1.getContext().startActivity(intent_favorites);
			}
					
		}
	}
}
