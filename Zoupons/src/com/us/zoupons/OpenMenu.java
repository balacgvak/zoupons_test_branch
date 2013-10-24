/**
 * 
 */
package com.us.zoupons;

import android.util.Log;
import android.view.View;

import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.GiftCards.GiftCards;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.friends.Friends;
import com.us.zoupons.invoice.InvoiceApproval;
import com.us.zoupons.receipts.Receipts;
import com.us.zoupons.zpay.Step2_ManageCards;
import com.us.zoupons.zpay.zpay_step1;

/**
 * Class to open right menu
 *
 */
public class OpenMenu {
	
	static String TAG="OpenMenu";
	
	public OpenMenu(){}
	
	public static void toOpenRightMenu(String classname){
		/** menu out **/
		if(classname.equals("SlidingView")){
			
			SlidingView.rightMenuScrollView.fullScroll(View.FOCUS_UP);
			SlidingView.rightMenuScrollView.pageScroll(View.FOCUS_UP);
			
			Log.i("fn", "open right menu");
			int menuWidth = SlidingView.leftMenu.getMeasuredWidth();
			//set store name in right menu
			SlidingView.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
			SlidingView.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);

			// Ensure menu is visible
			SlidingView.rightMenu.setVisibility(View.VISIBLE);
			int right = menuWidth+menuWidth;
			SlidingView.scrollView.smoothScrollTo(right, 0);
			MenuOutClass.HOMEPAGE_MENUOUT=true;
			
			if(SlidingView.mSearchStoreName.getVisibility()==View.VISIBLE){
				SlidingView.mSearchStoreName.clearFocus();
				SlidingView.mSearchStoreName.setFocusable(false);
			}
			
			SlidingView.mHomePageFreezeView.setVisibility(View.VISIBLE);
			SlidingView.mHomePageFreezeView.setOnClickListener(new FreezeClickListener(SlidingView.scrollView, SlidingView.leftMenu, SlidingView.rightMenu, SlidingView.searchBar, SlidingView.mSearchStoreName, SlidingView.shrinkSearch, 0,
					SlidingView.newContext,SlidingView.mMenuSearch,SlidingView.mHomePageFreezeView,SlidingView.mCancelStoreName,SlidingView.mSearchBoxContainer,SlidingView.mGoogleMap));
			
			
		}else if(classname.equals("GiftCards")){
			int menuWidth = GiftCards.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			GiftCards.rightMenu.setVisibility(View.VISIBLE);
			int right = menuWidth+menuWidth;
			GiftCards.scrollView.smoothScrollTo(right, 0);
			MenuOutClass.GIFTCARDS_MENUOUT=true;
			GiftCards.mGiftCardsFreezeView.setVisibility(View.VISIBLE);
			GiftCards.mGiftCardsFreezeView.setOnClickListener(new GiftCards.ClickListenerForScrolling(GiftCards.scrollView, GiftCards.leftMenu, GiftCards.mGiftCardsFreezeView));
		}else if(classname.equals("locations")){
			Log.i("fn", "open right menu");
			
			Location.rightMenuScrollView.fullScroll(View.FOCUS_UP);
			Location.rightMenuScrollView.pageScroll(View.FOCUS_UP);
			
			int menuWidth = Location.leftMenu.getMeasuredWidth();
			//set store name in right menu
			Location.mStoreName_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreName);
			Location.mStoreLocation_RightMenu.setText(RightMenuStoreId_ClassVariables.mStoreLocation);
			// Ensure menu is visible
			Location.rightMenu.setVisibility(View.VISIBLE);
			int right = menuWidth+menuWidth;
			Location.scrollView.smoothScrollTo(right, 0);
			Location.mLocationPageFreezeView.setVisibility(View.VISIBLE);
			MenuOutClass.LOCATION_MENUOUT=true;
		}
	}
	
	public static void toCloseMenu(String Classname){
		if(Classname.equals("SlidingView")){
			//menu out
			int menuWidth = SlidingView.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			SlidingView.leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			SlidingView.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.HOMEPAGE_MENUOUT=false;
			SlidingView.mHomePageFreezeView.setVisibility(View.GONE);
			if(SlidingView.mSearchStoreName.getVisibility()==View.VISIBLE){
				SlidingView.mSearchStoreName.setFocusable(true);
				SlidingView.mSearchStoreName.setFocusableInTouchMode(true);
			}
		}else if(Classname.equals("zpay_step1")){
			//menu out
			int menuWidth = zpay_step1.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			zpay_step1.leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			zpay_step1.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.ZPAY_MENUOUT=false;
			zpay_step1.mZpayStep1FreezeView.setVisibility(View.GONE);
		}else if(Classname.equals("Invoice")){
			//menu out
			int menuWidth = InvoiceApproval.leftmenu.getMeasuredWidth();

			// Ensure menu is visible
			InvoiceApproval.leftmenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			InvoiceApproval.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.FRIENDS_MENUOUT=false;
			InvoiceApproval.InvoiceFreezeButton.setVisibility(View.GONE);
		}else if(Classname.equals("MainMenuActivity")){
			//menu out
			int menuWidth = MainMenuActivity.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			MainMenuActivity.leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			MainMenuActivity.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.WHOLE_MENUOUT=false;
			MainMenuActivity.mMainMenuActivityFreezeView.setVisibility(View.GONE);
		}else if(Classname.equals("MainMenuActivity_Transactionhistory")){
			//menu out
			int menuWidth = MainMenuActivity.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			MainMenuActivity.leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			MainMenuActivity.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.WHOLE_MENUOUT=false;
			MainMenuActivity.mMainMenuActivityFreezeView.setVisibility(View.GONE);
		}else if(Classname.equals("Location")){
			//menu out
			int menuWidth = Location.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			Location.leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			Location.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.LOCATION_MENUOUT=false;
			Location.mLocationPageFreezeView.setVisibility(View.GONE);
		}else if(Classname.equals("ManageCards")){
			//menu out
			int menuWidth = ManageCards.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			ManageCards.leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			ManageCards.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.MANAGECARDS_MENUOUT=false;
			ManageCards.mManageCardsFreezeView.setVisibility(View.GONE);			
		}else if(Classname.equals("MyFriends_New")){
			//menu out
			int menuWidth = Friends.leftmenu.getMeasuredWidth();

			// Ensure menu is visible
			Friends.leftmenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			Friends.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.FRIENDS_MENUOUT=false;
			Friends.FriendsListFreezeButton.setVisibility(View.GONE);			
		}else if(Classname.equals("MainMenuActivity_CustomerCenter")){
			//menu out
			int menuWidth = MainMenuActivity.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			MainMenuActivity.leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			MainMenuActivity.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.WHOLE_MENUOUT=false;
			MainMenuActivity.mMainMenuActivityFreezeView.setVisibility(View.GONE);		
		}else if(Classname.equals("Settings")){
			//menu out
			int menuWidth = Settings.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			Settings.leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			Settings.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.SETTINGS_MENUOUT=false;
			Settings.mSettingsFreezeView.setVisibility(View.GONE);			
		}else if(Classname.equals("Step2_ManageCards")){
			//menu out
			int menuWidth = Step2_ManageCards.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			Step2_ManageCards.leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			Step2_ManageCards.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.STEP2_MANAGECARDS_MENUOUT=false;
			Step2_ManageCards.mStep2ManageCardsFreezeView.setVisibility(View.GONE);			
		}else if(Classname.equals("GiftCards")){
			//menu out
			int menuWidth = GiftCards.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			GiftCards.leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			GiftCards.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.GIFTCARDS_MENUOUT=false;
			GiftCards.mGiftCardsFreezeView.setVisibility(View.GONE);			
		}else if(Classname.equals("UploadReceipt")){
			/*//menu out
			int menuWidth = UploadReceipt.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			UploadReceipt.leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			UploadReceipt.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.UPLOADRECEIPT_MENUOUT=false;
			UploadReceipt.mUploadReceiptFreezeView.setVisibility(View.GONE);*/			
		}else if(Classname.equals("NotificationSettings")){
			//menu out
			int menuWidth = NotificationSettings.leftMenu.getMeasuredWidth();

			// Ensure menu is visible
			NotificationSettings.leftMenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			NotificationSettings.scrollView.smoothScrollTo(left, 0);
			MenuOutClass.NOTIFICATIONSETTINGS_MENUOUT=false;
			NotificationSettings.mNotificationSettingsFreezeView.setVisibility(View.GONE);			
		}else if(Classname.equals("Friends")){
			//menu out
			int menuWidth = Friends.leftmenu.getMeasuredWidth();

			// Ensure menu is visible
			Friends.leftmenu.setVisibility(View.VISIBLE);
			int left = menuWidth;
			Friends.scrollView.smoothScrollTo(left, 0);
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