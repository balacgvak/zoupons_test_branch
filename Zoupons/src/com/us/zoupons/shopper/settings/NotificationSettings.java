package com.us.zoupons.shopper.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.HeaderHomeClickListener;
import com.us.zoupons.LoginChoiceTabBarImage;
import com.us.zoupons.MenuItemClickListener;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.POJOMainMenuActivityTAG;
import com.us.zoupons.classvariables.POJONotificationDetails;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.MenuOutClass;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.LogoutSessionTask;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.mobilepay.NormalPaymentAsyncTask;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

public class NotificationSettings extends Activity implements OnCheckedChangeListener{

	// Initializing views and variables
	public static MyHorizontalScrollView sScrollView;   
	public static View sLeftMenu;
	private View mApp;
	private ImageView mBtnSlide;
	private Typeface mZouponsFont;
	// Leftmenu views and view group initialization
	private  LinearLayout mNotificationSettingsHome,mNotificationSettingsZpay,mNotificationSettingsInvoiceCenter,mNotificationSettingsManageCards,mNotificationSettingsReceipts,mNotificationSettingsGiftcards,mNotificationSettingsMyFavourites,mNotificationSettingsMyFriends,mNotificationSettingsChat,mNotificationSettingsRewards,mNotificationSettingsSettings,mNotificationSettingsLogout;
	private  TextView mNotificationSettingsHomeText,mNotificationSettingsZpayText,mNotificationSettingsInvoiceCenterText,mNotificationSettingsManageCardsText,mNotificationSettingsGiftCardsText,mNotificationSettingsReceiptsText,mNotificationSettingsMyFavoritesText,mNotificationSettingsMyFriendsText,mNotificationSettingsChatText,mNotificationSettingsRewardsText,mNotificationSettingsSettingsText,mNotificationSettingsLogoutText;
	private RelativeLayout mBtnLogout; 
	private ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	private Button mNotificationCount;
	public  String TAG = "NotificationSettings";
	private LinearLayout mCategoriesParentLayout;
	private CheckBox mContactFrequencyNoneCheckBox,mContactFrequencyNewCodesCheckBox,mContactFrequencyweeklyCheckBox,mContactFrequencyMonthlyCheckBox;
	private CheckBox mNotifyByEmailCheckBox,mNotifyBySMSCheckBox;
	private int mSelectedContactFrequency;
	private String mSelectedNotifyBy="email";
	private ArrayList<String> mUserSelectedCategories = new ArrayList<String>();
	private TextView mNotificationSave,mNotificationBack;
	private ScheduleNotificationSync mNotificationSync;
	private boolean mCotactFrequncyCheckFlag,mNotifyByCheckFlag=true;
	private String mSelectedCategories="";
	public static Button sNotificationSettingsFreezeView; 
	private ImageView mTabBarLoginChoice;
	public static ScrollView sleftMenuScrollView;
	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	private ArrayList<String> mCategoriesId = new ArrayList<String>();
	private ArrayList<String> mCategoriesName = new ArrayList<String>();
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		sScrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(sScrollView);
		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		sLeftMenu = inflater.inflate(R.layout.shopper_leftmenu, null);
		mApp = inflater.inflate(R.layout.settings_notification, null);
		ViewGroup leftMenuItems = (ViewGroup) sLeftMenu.findViewById(R.id.menuitems);
		final ViewGroup tabBar = (ViewGroup) mApp.findViewById(R.id.notification_tabBar);
		// Instantiating left menu view groups and views
		sleftMenuScrollView = (ScrollView) sLeftMenu.findViewById(R.id.leftmenu_scrollview);
		sleftMenuScrollView.fullScroll(View.FOCUS_UP);
		sleftMenuScrollView.pageScroll(View.FOCUS_UP);
		sNotificationSettingsFreezeView = (Button) mApp.findViewById(R.id.notification_freeze);
		mNotificationSettingsHome = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_home);
		mNotificationSettingsZpay = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zpay);
		mNotificationSettingsInvoiceCenter = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_invoicecenter);
		mNotificationSettingsManageCards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_managecards);
		mNotificationSettingsGiftcards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_zgiftcards);
		mNotificationSettingsReceipts = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_receipts);
		mNotificationSettingsMyFavourites = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfavourites);
		mNotificationSettingsMyFriends = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_myfriends);
		mNotificationSettingsChat = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_customercenter);
		mNotificationSettingsRewards = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_rewards);
		mNotificationSettingsSettings = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_settings);
		mNotificationSettingsLogout = (LinearLayout) leftMenuItems.findViewById(R.id.mainmenu_logout);
    	mNotificationSettingsHomeText = (TextView) leftMenuItems.findViewById(R.id.menuHome);
		mNotificationSettingsHomeText.setTypeface(mZouponsFont);
		mNotificationSettingsZpayText = (TextView) leftMenuItems.findViewById(R.id.menuZpay);
		mNotificationSettingsZpayText.setTypeface(mZouponsFont);
		mNotificationSettingsInvoiceCenterText = (TextView) leftMenuItems.findViewById(R.id.mainmenu_InvoiceCenter_text);
		mNotificationSettingsInvoiceCenterText.setTypeface(mZouponsFont);
		mNotificationSettingsManageCardsText = (TextView) leftMenuItems.findViewById(R.id.menuManageCards);
		mNotificationSettingsManageCardsText.setTypeface(mZouponsFont);
		mNotificationSettingsGiftCardsText = (TextView) leftMenuItems.findViewById(R.id.menuGiftCards);
		mNotificationSettingsGiftCardsText.setTypeface(mZouponsFont);
		mNotificationSettingsReceiptsText = (TextView) leftMenuItems.findViewById(R.id.menuReceipts);
		mNotificationSettingsReceiptsText.setTypeface(mZouponsFont);
		mNotificationSettingsMyFavoritesText = (TextView) leftMenuItems.findViewById(R.id.menufavorites);
		mNotificationSettingsMyFavoritesText.setTypeface(mZouponsFont);
		mNotificationSettingsMyFriendsText = (TextView) leftMenuItems.findViewById(R.id.menuMyFriends);
		mNotificationSettingsMyFriendsText.setTypeface(mZouponsFont);
		mNotificationSettingsChatText = (TextView) leftMenuItems.findViewById(R.id.menuCustomerCenter);
		mNotificationSettingsChatText.setTypeface(mZouponsFont);
		mNotificationSettingsRewardsText = (TextView) leftMenuItems.findViewById(R.id.menuRewards);
		mNotificationSettingsRewardsText.setTypeface(mZouponsFont);
		mNotificationSettingsSettingsText = (TextView) leftMenuItems.findViewById(R.id.menuSettings);
		mNotificationSettingsSettingsText.setTypeface(mZouponsFont);
		mNotificationSettingsLogoutText = (TextView) leftMenuItems.findViewById(R.id.menuLogout);
		mNotificationSettingsLogoutText.setTypeface(mZouponsFont);
		// Instantiating app view groups and views
		mBtnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide);
		mBtnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_notificationImageId);
		mBtnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(NotificationSettings.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) mBtnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) mBtnLogout.findViewById(R.id.zoupons_notification_count);
		mTabBarLoginChoice = (ImageView) mBtnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(NotificationSettings.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();

		mBtnSlide.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sNotificationSettingsFreezeView));
		sNotificationSettingsFreezeView.setOnClickListener(new ClickListenerForScrolling(sScrollView, sLeftMenu, sNotificationSettingsFreezeView));
		final View[] children = new View[] { sLeftMenu, mApp };
		mNotificationReceiver = new NotifitcationReceiver(mNotificationCount);
		mNotificationImage.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		mNotificationCount.setOnClickListener(new ManageNotificationWindow(this,tabBar, mCalloutTriangleImage,ZouponsConstants.sCustomerModuleFlag));
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		sScrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(mBtnSlide));

		mCategoriesParentLayout = (LinearLayout) mApp.findViewById(R.id.notification_categoriescontainer);
		mContactFrequencyMonthlyCheckBox = (CheckBox) mApp.findViewById(R.id.chkMonthly);
		mContactFrequencyNewCodesCheckBox = (CheckBox) mApp.findViewById(R.id.chkNewCodesAvailable);
		mContactFrequencyNoneCheckBox = (CheckBox) mApp.findViewById(R.id.chkNone);
		mContactFrequencyweeklyCheckBox = (CheckBox) mApp.findViewById(R.id.chkWeekly);
		mNotifyBySMSCheckBox = (CheckBox) mApp.findViewById(R.id.chkNotifybySms);
		mNotifyByEmailCheckBox = (CheckBox) mApp.findViewById(R.id.chkNotifybyEmail);
		mNotificationSave = (TextView) mApp.findViewById(R.id.notification_saveButtonId);
		mNotificationBack = (TextView) mApp.findViewById(R.id.notification_backButtonId);
		mContactFrequencyMonthlyCheckBox.setOnCheckedChangeListener(this);
		mContactFrequencyNewCodesCheckBox.setOnCheckedChangeListener(this);
		mContactFrequencyNoneCheckBox.setOnCheckedChangeListener(this);
		mContactFrequencyweeklyCheckBox.setOnCheckedChangeListener(this);

		mContactFrequencyNewCodesCheckBox.setChecked(true);
		mNotifyByEmailCheckBox.setChecked(true);

		// Execute AsyncTask to get notification details for user
		NotificationAsyncTask mNotificationTask = new NotificationAsyncTask(NotificationSettings.this);
		mNotificationTask.execute("GetNotification");
		
		// Setting click listener for left menu items
		mNotificationSettingsHome.setOnClickListener(new MenuItemClickListener(leftMenuItems, NotificationSettings.this,POJOMainMenuActivityTAG.TAG=TAG));
		mNotificationSettingsZpay.setOnClickListener(new MenuItemClickListener(leftMenuItems, NotificationSettings.this,POJOMainMenuActivityTAG.TAG=TAG));
		mNotificationSettingsInvoiceCenter.setOnClickListener(new MenuItemClickListener(leftMenuItems, NotificationSettings.this,POJOMainMenuActivityTAG.TAG=TAG));
		mNotificationSettingsGiftcards.setOnClickListener(new MenuItemClickListener(leftMenuItems,NotificationSettings.this,POJOMainMenuActivityTAG.TAG=TAG));
		mNotificationSettingsManageCards.setOnClickListener(new MenuItemClickListener(leftMenuItems, NotificationSettings.this,POJOMainMenuActivityTAG.TAG=TAG));
		mNotificationSettingsReceipts.setOnClickListener(new MenuItemClickListener(leftMenuItems, NotificationSettings.this,POJOMainMenuActivityTAG.TAG=TAG));
		mNotificationSettingsMyFavourites.setOnClickListener(new MenuItemClickListener(leftMenuItems,NotificationSettings.this,POJOMainMenuActivityTAG.TAG=TAG));
		mNotificationSettingsMyFriends.setOnClickListener(new MenuItemClickListener(leftMenuItems,NotificationSettings.this,POJOMainMenuActivityTAG.TAG=TAG));
		mNotificationSettingsChat.setOnClickListener(new MenuItemClickListener(leftMenuItems,NotificationSettings.this,POJOMainMenuActivityTAG.TAG=TAG));
		mNotificationSettingsRewards.setOnClickListener(new MenuItemClickListener(leftMenuItems,NotificationSettings.this,POJOMainMenuActivityTAG.TAG=TAG));
		mNotificationSettingsSettings.setOnClickListener(new MenuItemClickListener(leftMenuItems,NotificationSettings.this,POJOMainMenuActivityTAG.TAG=TAG));
		mNotificationSettingsLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.gradient_bg_hover);
				
				if(NormalPaymentAsyncTask.mCountDownTimer!=null){
					NormalPaymentAsyncTask.mCountDownTimer.cancel();
					NormalPaymentAsyncTask.mCountDownTimer = null;
				}
				
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(NotificationSettings.this,"FromManualLogOut").execute();
			}
		});

		mNotificationBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();	
			}
		});

		mNotificationSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!mCotactFrequncyCheckFlag){
					alertBox_service("Information", "Please choose any frequency for email notification", NotificationSettings.this);
				}else if(mUserSelectedCategories!= null && mUserSelectedCategories.size() == 0){
					alertBox_service("Information", "Please choose any category", NotificationSettings.this);
				}else{
					for(int i=0;i<mUserSelectedCategories.size();i++){
						mSelectedCategories += mUserSelectedCategories.get(i);
						if(i!=mUserSelectedCategories.size()-1){
							mSelectedCategories+=",";
						}
					}
					// Execute AsyncTask to save notification details for user
					NotificationAsyncTask mNotificationTask = new NotificationAsyncTask(NotificationSettings.this);
					mNotificationTask.execute("SetNotification");
				}
			}
		});

		mNotifyByEmailCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mNotifyBySMSCheckBox.setChecked(false);
					mSelectedNotifyBy = "email";
					mNotifyByCheckFlag = true;	
				}else{
					mNotifyByCheckFlag = false;
				}
			}
		});

		mNotifyBySMSCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mNotifyByEmailCheckBox.setChecked(false);
					mSelectedNotifyBy = "sms";
					mNotifyByCheckFlag = true;
				}else{
					mNotifyByCheckFlag = false;
				}	
			}
		});

		mLogoutImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(NotificationSettings.this,"FromManualLogOut").execute();
			}
		});
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			switch(buttonView.getId()){
			case R.id.chkNone:
				unCheckFrequency(mContactFrequencyNewCodesCheckBox);
				unCheckFrequency(mContactFrequencyweeklyCheckBox);
				unCheckFrequency(mContactFrequencyMonthlyCheckBox);
				mSelectedContactFrequency = 0;
				mCotactFrequncyCheckFlag =true;
				break;
			case R.id.chkNewCodesAvailable:
				unCheckFrequency(mContactFrequencyNoneCheckBox);
				unCheckFrequency(mContactFrequencyweeklyCheckBox);
				unCheckFrequency(mContactFrequencyMonthlyCheckBox);
				mSelectedContactFrequency = 1;
				mCotactFrequncyCheckFlag = true;
				break;
			case R.id.chkWeekly:
				unCheckFrequency(mContactFrequencyNoneCheckBox);
				unCheckFrequency(mContactFrequencyNewCodesCheckBox);
				unCheckFrequency(mContactFrequencyMonthlyCheckBox);
				mSelectedContactFrequency = 2;
				mCotactFrequncyCheckFlag = true;
				break;
			case R.id.chkMonthly:
				unCheckFrequency(mContactFrequencyNoneCheckBox);
				unCheckFrequency(mContactFrequencyNewCodesCheckBox);
				unCheckFrequency(mContactFrequencyweeklyCheckBox);
				mSelectedContactFrequency = 3;
				mCotactFrequncyCheckFlag = true;
				break;	  
			}

		}else{
			mCotactFrequncyCheckFlag = true; // useful for validation
			buttonView.setChecked(true);
		}
	}

	private void unCheckFrequency(CheckBox frequencyCheckBox){
		frequencyCheckBox.setOnCheckedChangeListener(null);
		frequencyCheckBox.setChecked(false);
		frequencyCheckBox.setOnCheckedChangeListener(this);
	}
	
	
	/**
	 * 
	 * Asynctask to communicate with server to get customer notification details
	 *
	 */
	class NotificationAsyncTask extends AsyncTask<String, String, String>{

		private ProgressDialog mProgressView;
		private Context context;
		private ZouponsWebService mZouponswebservice;
		private ZouponsParsingClass mParsingclass;
		private NetworkCheck mConnectivityCheck;
		private String mFunction;

		public NotificationAsyncTask(Context context) {
			this.context = context;
			mProgressView = new ProgressDialog(context);
			mProgressView.setMessage("please wait..");
			mProgressView.setCancelable(false);
			mZouponswebservice = new ZouponsWebService(context);
			mParsingclass = new ZouponsParsingClass(context);
			mConnectivityCheck = new NetworkCheck();
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressView.show();
		}	

		@Override
		protected String doInBackground(String... params) {
			String result="",mGetResponse="",mParsingResponse="",mNotificationResponse="",mNotificationParsingResponse;
			mFunction = params[0];
			try{
				if(mConnectivityCheck.ConnectivityCheck(this.context)){
					if(mFunction.equalsIgnoreCase("GetNotification")){
						mGetResponse=mZouponswebservice.GetCatogories();	
						if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
							mParsingResponse=mParsingclass.parseNotificationCategories(mGetResponse);
							if(mParsingResponse.equalsIgnoreCase("success")){
								mNotificationResponse = mZouponswebservice.GetNotificationDetails();
								if(!mNotificationResponse.equals("failure") && !mNotificationResponse.equals("noresponse")){
									mNotificationParsingResponse =	mParsingclass.parseNotificationDetails(mNotificationResponse);
									if(!mNotificationParsingResponse.equals("failure") && !mNotificationParsingResponse.equals("norecords")){
										result = "success";
									}else if(mNotificationParsingResponse.equalsIgnoreCase("failure")){
										result = "failure";
									}else if(mNotificationParsingResponse.equalsIgnoreCase("norecords")){
										result="no_notification_records";
									}
								}else{
									result="failure";
								}

							}else if(mParsingResponse.equalsIgnoreCase("failure")){
								result = "failure";
							}else if(mParsingResponse.equalsIgnoreCase("norecords")){
								result="no_category_records";
							}
						}else{
							result="failure";
						}
					}else{
						mGetResponse=mZouponswebservice.SetNotificationDetails(String.valueOf(mSelectedContactFrequency),mSelectedNotifyBy,mSelectedCategories.trim());	
						if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
							mParsingResponse=mParsingclass.parseSetNotificationDetails(mGetResponse);
							if(mParsingResponse.equalsIgnoreCase("success")){
								result="success";
							}else{
								result="update failure";
							}
						}else{
							result="failure";
						}
					}
				}else{
					result="nonetwork";
				}
			}catch(Exception e){
				result="failure";
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(mProgressView != null && mProgressView.isShowing()){
				mProgressView.dismiss();
			}
			mCategoriesId.clear();
			mCategoriesName.clear();
			if(result.equalsIgnoreCase("success")){
				if(mFunction.equalsIgnoreCase("GetNotification")){
					mCategoriesId=setCategoriesIdArrayList(WebServiceStaticArrays.categories);
					mCategoriesName=setCategoriesNameArrayList(WebServiceStaticArrays.categories);
					Collections.sort(mCategoriesName);
					if(WebServiceStaticArrays.mNotificationDetails.size()>0){
						POJONotificationDetails mNotification = (POJONotificationDetails) WebServiceStaticArrays.mNotificationDetails.get(0);
						String notify_by = mNotification.notify_by;
						if(notify_by.equalsIgnoreCase("sms")){
							mNotifyBySMSCheckBox.setChecked(true);
							mNotifyByEmailCheckBox.setChecked(false);
						}else{
							mNotifyByEmailCheckBox.setChecked(true);
							mNotifyBySMSCheckBox.setChecked(false);
						}
						switch(Integer.valueOf(mNotification.contact_frequency)){
						case 0:
							mContactFrequencyNoneCheckBox.setChecked(true);
							break;
						case 1:
							mContactFrequencyNewCodesCheckBox.setChecked(true);
							break;
						case 2:
							mContactFrequencyweeklyCheckBox.setChecked(true);	
							break;
						case 3:
							mContactFrequencyMonthlyCheckBox.setChecked(true);
							break;
						}
						String[] selectedCategories = mNotification.categories.split(",");
						for(int k=0;k<selectedCategories.length;k++){
							mUserSelectedCategories.add(getKey(selectedCategories[k].trim()));
						}
						for(int i=0;i<mCategoriesName.size();i++){
							if(i%2==0){
								if(mCategoriesName.size() >= (i+2)){
									List<String> sublist = mCategoriesName.subList(i, i+2);
									LinearLayout mLinearLayout = new LinearLayout(context);
									LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
									LayoutParams.setMargins(0, 15, 0, 0);
									mLinearLayout.setLayoutParams(LayoutParams);
									for(int j=0;j<sublist.size();j++){
										CheckBox checkbox = new CheckBox(context);
										checkbox.setLayoutParams(new LinearLayout.LayoutParams(0,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,1f));
										checkbox.setText(sublist.get(j));
										checkbox.setTag(getKey(sublist.get(j)));
										for(int k=0;k<selectedCategories.length;k++){
											if(selectedCategories[k].trim().equalsIgnoreCase(sublist.get(j))){
												checkbox.setChecked(true);
											}
										}
										checkbox.setTextColor(getResources().getColor(R.color.black));
										checkbox.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
										checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

											@Override
											public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
												// TODO Auto-generated method stub
												if(isChecked){
													mUserSelectedCategories.add(buttonView.getTag().toString());
												}else{
													if(mUserSelectedCategories.contains(buttonView.getTag().toString())){
														mUserSelectedCategories.remove(buttonView.getTag().toString());
													}
												}
											}
										});
										mLinearLayout.addView(checkbox);
									}
									mCategoriesParentLayout.addView(mLinearLayout);
								}else{
									LinearLayout mLinearLayout = new LinearLayout(context);
									LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT, 0,1f);
									LayoutParams.setMargins(0, 15, 0, 0);
									mLinearLayout.setLayoutParams(LayoutParams);
									CheckBox checkbox = new CheckBox(context);
									checkbox.setLayoutParams(new LinearLayout.LayoutParams(0,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,1f));
									checkbox.setText(mCategoriesName.get(i));
									checkbox.setTag(getKey(mCategoriesName.get(i)));
									checkbox.setTextColor(getResources().getColor(R.color.black));
									checkbox.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
									for(int k=0;k<selectedCategories.length;k++){
										if(selectedCategories[k].trim().equalsIgnoreCase(mCategoriesName.get(i))){
											checkbox.setChecked(true);
										}
									}
									checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

										@Override
										public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
											// TODO Auto-generated method stub
											if(isChecked){
												mUserSelectedCategories.add(buttonView.getTag().toString());
											}else{
												if(mUserSelectedCategories.contains(buttonView.getTag().toString())){
													mUserSelectedCategories.remove(buttonView.getTag().toString());
												}
											}
										}
									});
									mLinearLayout.addView(checkbox);
									mCategoriesParentLayout.addView(mLinearLayout);
								}
							}
						}
					}	
				}else{
					alertBox_service("Information", "Notifications successfully Updated", context);
				}

			}else if(result.equalsIgnoreCase("failure")||result.equalsIgnoreCase("noresponse")){
				alertBox_service("Information", "Unable to reach service.",context);
			}else if(result.equalsIgnoreCase("nonetwork")){
				Toast.makeText(this.context, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equalsIgnoreCase("no_category_records")){
				alertBox_service("Information", "Notification Details not Available Please try after some time",context);
			}else if(result.equalsIgnoreCase("no_notification_records")){
				mCategoriesId=setCategoriesIdArrayList(WebServiceStaticArrays.categories);
				mCategoriesName=setCategoriesNameArrayList(WebServiceStaticArrays.categories);
				
				Collections.sort(mCategoriesName);
				for(int i=0;i<mCategoriesName.size();i++){
					if(i%2==0){
						if(mCategoriesName.size() >= (i+2)){
							// Appending linear layout to parent which contains categories at run time
							List<String> sublist = mCategoriesName.subList(i, i+2);
							LinearLayout mLinearLayout = new LinearLayout(context);
							LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
							LayoutParams.setMargins(0, 15, 0, 0);
							mLinearLayout.setLayoutParams(LayoutParams);
							for(int j=0;j<sublist.size();j++){
								CheckBox checkbox = new CheckBox(context);
								checkbox.setLayoutParams(new LinearLayout.LayoutParams(0,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,1f));
								checkbox.setText(sublist.get(j));
								checkbox.setTag(getKey(sublist.get(j)));
								checkbox.setTextColor(getResources().getColor(R.color.black));
								checkbox.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
								checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

									@Override
									public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
										if(isChecked){
											mUserSelectedCategories.add(buttonView.getTag().toString());
										}else{
											if(mUserSelectedCategories.contains(buttonView.getTag().toString())){
												mUserSelectedCategories.remove(buttonView.getTag().toString());
											}
										}
									}
								});
								mLinearLayout.addView(checkbox);
							}
							mCategoriesParentLayout.addView(mLinearLayout);
						}else{
							LinearLayout mLinearLayout = new LinearLayout(context);
							LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT, 0,1f);
							LayoutParams.setMargins(0, 15, 0, 0);
							mLinearLayout.setLayoutParams(LayoutParams);
							CheckBox checkbox = new CheckBox(context);
							checkbox.setLayoutParams(new LinearLayout.LayoutParams(0,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,1f));
							checkbox.setText(mCategoriesName.get(i));
							checkbox.setTag(getKey(mCategoriesName.get(i)));
							checkbox.setTextColor(getResources().getColor(R.color.black));
							checkbox.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
							checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
									if(isChecked){
										mUserSelectedCategories.add(buttonView.getTag().toString());
									}else{
										if(mUserSelectedCategories.contains(buttonView.getTag().toString())){
											mUserSelectedCategories.remove(buttonView.getTag().toString());
										}
									}
								}
							});
							mLinearLayout.addView(checkbox);
							mCategoriesParentLayout.addView(mLinearLayout);
						}
					}
				}
				alertBox_service("Information", "Notification Details not available",context);
			}else if(result.equalsIgnoreCase("update failure")){
				alertBox_service("Information", "Notification Details not updated Please try after some time",context);
			}
		}
	}	

	/**
	 * 
	 * Helper class to manage menu and app items
	 *
	 */
	class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View menu;
		Button mFreezeView;
		/**
		 * Menu must NOT be out/shown to start with.
		 */

		public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu, Button freezeview) {
			super();
			this.scrollView = scrollView;
			this.menu = menu;
			this.mFreezeView = freezeview;
		}

		@Override
		public void onClick(View v) {
			
			sleftMenuScrollView.fullScroll(View.FOCUS_UP);
			sleftMenuScrollView.pageScroll(View.FOCUS_UP);
			
			Context context = menu.getContext();
			String msg = "Slide " + new Date();
			System.out.println(msg);

			int menuWidth = menu.getMeasuredWidth();

			// Ensure menu is visible
			menu.setVisibility(View.VISIBLE);

			InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

			if (!MenuOutClass.NOTIFICATIONSETTINGS_MENUOUT) {
				// Scroll to 0 to reveal menu
				int left = 0;
				scrollView.smoothScrollTo(left, 0);
				this.mFreezeView.setVisibility(View.VISIBLE);
			} else {
				// Scroll to menuWidth so menu isn't on screen.
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);
				this.mFreezeView.setVisibility(View.GONE);
			}
			MenuOutClass.NOTIFICATIONSETTINGS_MENUOUT = !MenuOutClass.NOTIFICATIONSETTINGS_MENUOUT;
		}
	}

	class SizeCallbackForMenu implements SizeCallback {
		int btnWidth;
		View btnSlide;

		public SizeCallbackForMenu(View btnSlide) {
			super();
			this.btnSlide = btnSlide;
		}

		@Override
		public void onGlobalLayout() {
			btnWidth = (int) (btnSlide.getMeasuredWidth()-10);
			System.out.println("btnWidth=" + btnWidth); //$NON-NLS-1$
		}

		@Override
		public void getViewSize(int idx, int w, int h, int[] dims) {
			dims[0] = w;
			dims[1] = h;
			final int menuIdx = 0;
			if (idx == menuIdx) {
				dims[0] = w - btnWidth;
			}
		}
	}

	// To set Categories id to array list
	public ArrayList<String> setCategoriesIdArrayList(HashMap<String, String> hashmap){
		ArrayList<String> mCategoriesId = new ArrayList<String>();
		mCategoriesId.clear();
		mCategoriesId=new ArrayList<String>(hashmap.keySet());
		return mCategoriesId;
	}
	
	// To set Categories name to array list
	public ArrayList<String> setCategoriesNameArrayList(HashMap<String, String> hashmap){
		ArrayList<String> mCategoriesName = new ArrayList<String>();
		mCategoriesName.clear();
		mCategoriesName=new ArrayList<String>(hashmap.values());
		return mCategoriesName;
	}
	
	// To get hash map key using the hashmap value
	public String getKey(String value){
		String key="";
		for (Entry<String, String> entry : WebServiceStaticArrays.categories.entrySet()) {
            if (entry.getValue().equals(value)) {
                key=entry.getKey();
            }
        }
		return key;
	}
	
	// Funtion to show alert pop up with respective message
	public void alertBox_service(final String title,final String msg,Context context){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(context);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equalsIgnoreCase("Notification Details not Available Please try after some time") || msg.equalsIgnoreCase("Notifications successfully Updated")){
					finish();
				}
			}
		});
		service_alert.show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sScrollView = null;sLeftMenu = null;sNotificationSettingsFreezeView= null;sleftMenuScrollView = null;
		// To notify  system that its time to run garbage collector service
		System.gc();
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mNotificationReceiver);
		if(mNotificationSync!=null){
			mNotificationSync.cancelAlarm();
		}

	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(NotificationSettings.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(NotificationSettings.this,ZouponsConstants.sCustomerModuleFlag);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(NotificationSettings.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(NotificationSettings.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
}