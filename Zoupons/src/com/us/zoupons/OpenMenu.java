/**
 * 
 */
package com.us.zoupons;

import android.util.Log;
import android.view.View;

import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.mobilepay.MobilePay;
import com.us.zoupons.mobilepay.CardPurchase;
import com.us.zoupons.shopper.friends.Friends;
import com.us.zoupons.shopper.giftcards_deals.MyGiftCards;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.invoice.InvoiceApproval;
import com.us.zoupons.shopper.location.Location;
import com.us.zoupons.shopper.receipts.Receipts;
import com.us.zoupons.shopper.settings.NotificationSettings;
import com.us.zoupons.shopper.settings.Settings;
import com.us.zoupons.shopper.wallet.ManageWallets;

/**
 * Class to open right menu
 *
 */
public class OpenMenu {
		
	public OpenMenu(){}
	
	// To open right menu skider
	public static void toOpenRightMenu(String classname){
		/** menu out **/
		if(classname.equals("ShopperHomePage")){
			ShopperHomePage.sRightMenuScrollView.fullScroll(View.FOCUS_UP);
			ShopperHomePage.sRightMenuScrollView.pageScroll(View.FOCUS_UP);
			Log.i("fn", "open right menu");
			int menuWidth = ShopperHomePage.sLeftMenu.getMeasuredWidth();
			//set store name in right menu
			ShopperHomePage.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
			ShopperHomePage.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);
			// Ensure menu is visible
			ShopperHomePage.sRightMenu.setVisibility(View.VISIBLE);
			int right = menuWidth+menuWidth;
			ShopperHomePage.sScrollView.smoothScrollTo(right, 0);
			MenuOutClass.HOMEPAGE_MENUOUT=true;
			if(ShopperHomePage.sSearchStoreName.getVisibility()==View.VISIBLE){
				ShopperHomePage.sSearchStoreName.clearFocus();
				ShopperHomePage.sSearchStoreName.setFocusable(false);
			}
			ShopperHomePage.sHomePageFreezeView.setVisibility(View.VISIBLE);
		}else if(classname.equals("MyGiftCards")){
			int menuWidth = MyGiftCards.sLeftMenu.getMeasuredWidth();
			// Ensure menu is visible
			MyGiftCards.sRightMenu.setVisibility(View.VISIBLE);
			int right = menuWidth+menuWidth;
			MyGiftCards.sScrollView.smoothScrollTo(right, 0);
			MenuOutClass.GIFTCARDS_MENUOUT=true;
			MyGiftCards.mGiftCardsFreezeView.setVisibility(View.VISIBLE);
		}else if(classname.equals("locations")){
			Log.i("fn", "open right menu");
			Location.sLocationrightMenuScrollView.fullScroll(View.FOCUS_UP);
			Location.sLocationrightMenuScrollView.pageScroll(View.FOCUS_UP);
			int menuWidth = Location.sLocationsleftMenu.getMeasuredWidth();
			//set store name in right menu
			Location.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
			Location.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);
			// Ensure menu is visible
			Location.sLocationrightMenu.setVisibility(View.VISIBLE);
			int right = menuWidth+menuWidth;
			Location.sLocationscrollView.smoothScrollTo(right, 0);
			Location.mLocationPageFreezeView.setVisibility(View.VISIBLE);
			MenuOutClass.LOCATION_MENUOUT=true;
		}
	}
	
	// To close opened slider to default position
	public static void toCloseMenu(String Classname){
		if(Classname.equals("ShopperHomePage")){
			//menu out
			int menuWidth = ShopperHomePage.sLeftMenu.getMeasuredWidth();
			// Ensure menu is visible
			ShopperHomePage.sLeftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			ShopperHomePage.sScrollView.smoothScrollTo(left, 0);
			MenuOutClass.HOMEPAGE_MENUOUT=false;
			ShopperHomePage.sHomePageFreezeView.setVisibility(View.GONE);
			if(ShopperHomePage.sSearchStoreName.getVisibility()==View.VISIBLE){
				ShopperHomePage.sSearchStoreName.setFocusable(true);
				ShopperHomePage.sSearchStoreName.setFocusableInTouchMode(true);
			}
		}else if(Classname.equals("CardPurchase")){
			//menu out
			int menuWidth = CardPurchase.sLeftMenu.getMeasuredWidth();
			// Ensure menu is visible
			CardPurchase.sLeftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			CardPurchase.sHorizScrollView.smoothScrollTo(left, 0);
			MenuOutClass.ZPAY_MENUOUT=false;
			CardPurchase.mZpayStep1FreezeView.setVisibility(View.GONE);
		}else if(Classname.equals("Invoice")){
			//menu out
			int menuWidth = InvoiceApproval.sLeftmenu.getMeasuredWidth();
			// Ensure menu is visible
			InvoiceApproval.sLeftmenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			InvoiceApproval.sScrollView.smoothScrollTo(left, 0);
			MenuOutClass.FRIENDS_MENUOUT=false;
			InvoiceApproval.InvoiceFreezeButton.setVisibility(View.GONE);
		}else if(Classname.equals("MainMenuActivity")){
			//menu out
			int menuWidth = MainMenuActivity.sLeftMenu.getMeasuredWidth();
			// Ensure menu is visible
			MainMenuActivity.sLeftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			MainMenuActivity.sScrollView.smoothScrollTo(left, 0);
			MenuOutClass.WHOLE_MENUOUT=false;
			MainMenuActivity.mMainMenuActivityFreezeView.setVisibility(View.GONE);
		}else if(Classname.equals("MainMenuActivity_Transactionhistory")){
			//menu out
			int menuWidth = MainMenuActivity.sLeftMenu.getMeasuredWidth();
			// Ensure menu is visible
			MainMenuActivity.sLeftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			MainMenuActivity.sScrollView.smoothScrollTo(left, 0);
			MenuOutClass.WHOLE_MENUOUT=false;
			MainMenuActivity.mMainMenuActivityFreezeView.setVisibility(View.GONE);
		}else if(Classname.equals("Location")){
			//menu out
			int menuWidth = Location.sLocationsleftMenu.getMeasuredWidth();
			// Ensure menu is visible
			Location.sLocationsleftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			Location.sLocationscrollView.smoothScrollTo(left, 0);
			MenuOutClass.LOCATION_MENUOUT=false;
			Location.mLocationPageFreezeView.setVisibility(View.GONE);
		}else if(Classname.equals("ManageCards")){
			//menu out
			int menuWidth = ManageWallets.sWalletleftMenu.getMeasuredWidth();
			// Ensure menu is visible
			ManageWallets.sWalletleftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			ManageWallets.sWalletScrollView.smoothScrollTo(left, 0);
			MenuOutClass.MANAGECARDS_MENUOUT=false;
			ManageWallets.mManageCardsFreezeView.setVisibility(View.GONE);			
		}else if(Classname.equals("MyFriends_New")){
			//menu out
			int menuWidth = Friends.sLeftmenu.getMeasuredWidth();
			// Ensure menu is visible
			Friends.sLeftmenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			Friends.sScrollView.smoothScrollTo(left, 0);
			MenuOutClass.FRIENDS_MENUOUT=false;
			Friends.FriendsListFreezeButton.setVisibility(View.GONE);			
		}else if(Classname.equals("MainMenuActivity_CustomerCenter")){  
			//menu out
			int menuWidth = MainMenuActivity.sLeftMenu.getMeasuredWidth();
			// Ensure menu is visible
			MainMenuActivity.sLeftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			MainMenuActivity.sScrollView.smoothScrollTo(left, 0);
			MenuOutClass.WHOLE_MENUOUT=false;
			MainMenuActivity.mMainMenuActivityFreezeView.setVisibility(View.GONE);		
		}else if(Classname.equals("Settings")){
			//menu out
			int menuWidth = Settings.sLeftMenu.getMeasuredWidth();
			// Ensure menu is visible
			Settings.sLeftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			Settings.sScrollView.smoothScrollTo(left, 0);
			MenuOutClass.SETTINGS_MENUOUT=false;
			Settings.sSettingsFreezeView.setVisibility(View.GONE);			
		}else if(Classname.equals("MobilePay")){
			//menu out
			int menuWidth = MobilePay.sLeftMenu.getMeasuredWidth();
			// Ensure menu is visible
			MobilePay.sLeftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			MobilePay.sHorizontalScrollView.smoothScrollTo(left, 0);
			MenuOutClass.STEP2_MANAGECARDS_MENUOUT=false;
			MobilePay.mStep2ManageCardsFreezeView.setVisibility(View.GONE);			
		}else if(Classname.equals("MyGiftCards")){
			//menu out
			int menuWidth = MyGiftCards.sLeftMenu.getMeasuredWidth();
			// Ensure menu is visible
			MyGiftCards.sLeftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			MyGiftCards.sScrollView.smoothScrollTo(left, 0);
			MenuOutClass.GIFTCARDS_MENUOUT=false;
			MyGiftCards.mGiftCardsFreezeView.setVisibility(View.GONE);			
		}else if(Classname.equals("NotificationSettings")){
			//menu out
			int menuWidth = NotificationSettings.sLeftMenu.getMeasuredWidth();
			// Ensure menu is visible
			NotificationSettings.sLeftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			NotificationSettings.sScrollView.smoothScrollTo(left, 0);
			MenuOutClass.NOTIFICATIONSETTINGS_MENUOUT=false;
			NotificationSettings.sNotificationSettingsFreezeView.setVisibility(View.GONE);			
		}else if(Classname.equals("Friends")){
			//menu out
			int menuWidth = Friends.sLeftmenu.getMeasuredWidth();
			// Ensure menu is visible
			Friends.sLeftmenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			Friends.sScrollView.smoothScrollTo(left, 0);
			MenuOutClass.FRIENDS_MENUOUT=false;
			Friends.FriendsListFreezeButton.setVisibility(View.GONE);			
		}else if(Classname.equals("Receipts")){
			//menu out
			int menuWidth = Receipts.leftmenu.getMeasuredWidth();
			// Ensure menu is visible
			Receipts.leftmenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			Receipts.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.RECEIPTS_MENUOUT=false;
			Receipts.ReceiptsFreezeButton.setVisibility(View.GONE);			
		}
	}
}