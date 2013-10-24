package com.us.zoupons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MyHorizontalScrollView.SizeCallback;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.POJOMainMenuActivityTAG;
import com.us.zoupons.ClassVariables.POJONotificationDetails;
import com.us.zoupons.FlagClasses.MenuOutClass;
import com.us.zoupons.LogoutSession.LogoutSessionTask;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.notification.CustomNotificationAdapter;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.NotificationItemClickListener;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.zpay.NormalPaymentAsyncTask;

public class NotificationSettings extends Activity implements OnCheckedChangeListener{

	public static MyHorizontalScrollView scrollView;
	public static View leftMenu;
	View app;
	ImageView btnSlide;
	public Typeface mZouponsFont;
	public static LinearLayout mNotificationSettingsHome,mNotificationSettingsZpay,mNotificationSettingsInvoiceCenter,mNotificationSettingsManageCards,mNotificationSettingsReceipts,mNotificationSettingsGiftcards,mNotificationSettingsMyFavourites,mNotificationSettingsMyFriends,mNotificationSettingsChat,mNotificationSettingsRewards,mNotificationSettingsSettings,mNotificationSettingsLogout,mNotificationSettingsLoginLayout;
	public static TextView mNotificationSettingsHomeText,mNotificationSettingsZpayText,mNotificationSettingsInvoiceCenterText,mNotificationSettingsManageCardsText,mNotificationSettingsGiftCardsText,mNotificationSettingsReceiptsText,mNotificationSettingsMyFavoritesText,mNotificationSettingsMyFriendsText,mNotificationSettingsChatText,mNotificationSettingsRewardsText,mNotificationSettingsSettingsText,mNotificationSettingsLogoutText;
	int btnWidth;
	public RelativeLayout btnLogout; 
	public ImageView mLogoutImage,mNotificationImage,mCalloutTriangleImage;
	public Button mNotificationCount;
	public static String TAG = "NotificationSettings";
	private LinearLayout mCategoriesParentLayout;
	private CheckBox mContactFrequencyNoneCheckBox,mContactFrequencyNewCodesCheckBox,mContactFrequencyDailyCheckBox,mContactFrequencyweeklyCheckBox,mContactFrequencyMonthlyCheckBox;
	private CheckBox mNotifyByEmailCheckBox,mNotifyBySMSCheckBox;
	private int mSelectedContactFrequency;
	private String mSelectedNotifyBy="email";
	private ArrayList<String> mUserSelectedCategories = new ArrayList<String>();
	private TextView mNotificationSave,mNotificationBack;
	//Notification Pop up layout declaration
	private View mPopUpLayout;
	private ListView mNotificationList;
	private ScheduleNotificationSync mNotificationSync;
	private boolean mCotactFrequncyCheckFlag,mNotifyByCheckFlag=true;
	private String selectedCategories="";
	public static Button mNotificationSettingsFreezeView; 
	public ImageView mTabBarLoginChoice;
	public static ScrollView leftMenuScrollView;
	
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	
	public ArrayList<String> mCategoriesId = new ArrayList<String>();
	public ArrayList<String> mCategoriesName = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView=(MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);

		mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
		leftMenu = inflater.inflate(R.layout.horz_scroll_menu, null);
		app = inflater.inflate(R.layout.settings_notification, null);

		ViewGroup leftMenuItems = (ViewGroup) leftMenu.findViewById(R.id.menuitems);
		final ViewGroup tabBar = (ViewGroup) app.findViewById(R.id.notification_tabBar);
		
		leftMenuScrollView = (ScrollView) leftMenu.findViewById(R.id.leftmenu_scrollview);
		leftMenuScrollView.fullScroll(View.FOCUS_UP);
		leftMenuScrollView.pageScroll(View.FOCUS_UP);
		
		mNotificationSettingsFreezeView = (Button) app.findViewById(R.id.notification_freeze);

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

		btnSlide = (ImageView) tabBar.findViewById(R.id.BtnSlide);
		btnLogout = (RelativeLayout) tabBar.findViewById(R.id.zoupons_logout_container);
		mLogoutImage = (ImageView) btnLogout.findViewById(R.id.zoupons_logout_btn);
		mNotificationImage = (ImageView) btnLogout.findViewById(R.id.zoupons_notificationImageId);
		btnLogout.findViewById(R.id.zoupons_home).setOnClickListener(new HeaderHomeClickListener(NotificationSettings.this));//ClickListener for Header Home image
		mCalloutTriangleImage = (ImageView) btnLogout.findViewById(R.id.zoupons_callout_triangle);
		mNotificationCount = (Button) btnLogout.findViewById(R.id.zoupons_notification_count);

		mTabBarLoginChoice = (ImageView) btnLogout.findViewById(R.id.store_header_loginchoice);
		new LoginChoiceTabBarImage(NotificationSettings.this,mTabBarLoginChoice).setTabBarLoginChoiceImageVisibility();

		btnSlide.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, mNotificationSettingsFreezeView));
		mNotificationSettingsFreezeView.setOnClickListener(new ClickListenerForScrolling(scrollView, leftMenu, mNotificationSettingsFreezeView));
		final View[] children = new View[] { leftMenu, app };
		LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopUpLayout = mInflater.inflate(R.layout.notification, (ViewGroup) findViewById(R.id.mPopUpParentLayout));
		mNotificationList = (ListView) mPopUpLayout.findViewById(R.id.notification_list);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		int popupLeftPadding = 0;
		if(getWindowManager().getDefaultDisplay().getWidth()<1000){
			popupLeftPadding = 30;
		}else{
			popupLeftPadding = 400;
		}
		mLayoutParams.leftMargin = popupLeftPadding;
		mNotificationList.setLayoutParams(mLayoutParams);
		// Scroll to app (view[1]) when layout finished.
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(btnSlide));

		mCategoriesParentLayout = (LinearLayout) app.findViewById(R.id.notification_categoriescontainer);
		mContactFrequencyMonthlyCheckBox = (CheckBox) app.findViewById(R.id.chkMonthly);
		mContactFrequencyNewCodesCheckBox = (CheckBox) app.findViewById(R.id.chkNewCodesAvailable);
		mContactFrequencyNoneCheckBox = (CheckBox) app.findViewById(R.id.chkNone);
		mContactFrequencyweeklyCheckBox = (CheckBox) app.findViewById(R.id.chkWeekly);
		mNotifyBySMSCheckBox = (CheckBox) app.findViewById(R.id.chkNotifybySms);
		mNotifyByEmailCheckBox = (CheckBox) app.findViewById(R.id.chkNotifybyEmail);
		mNotificationSave = (TextView) app.findViewById(R.id.notification_saveButtonId);
		mNotificationBack = (TextView) app.findViewById(R.id.notification_backButtonId);
		mContactFrequencyMonthlyCheckBox.setOnCheckedChangeListener(this);
		mContactFrequencyNewCodesCheckBox.setOnCheckedChangeListener(this);
		mContactFrequencyNoneCheckBox.setOnCheckedChangeListener(this);
		mContactFrequencyweeklyCheckBox.setOnCheckedChangeListener(this);

		mContactFrequencyweeklyCheckBox.setChecked(true);
		mNotifyByEmailCheckBox.setChecked(true);

		NotificationAsyncTask mNotificationTask = new NotificationAsyncTask(NotificationSettings.this);
		mNotificationTask.execute("GetNotification");

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
					Log.i(TAG,"Timer Stopped Successfully");
				}
				
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(NotificationSettings.this).execute();
				Intent intent_logout = new Intent(NotificationSettings.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent_logout);
				finish();
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
				}else if(!mNotifyByCheckFlag){
					alertBox_service("Information", "Please choose any mode for receiving updates", NotificationSettings.this);
				}else{
					Log.i("selected cat arraylist", mUserSelectedCategories+" ");
					for(int i=0;i<mUserSelectedCategories.size();i++){
						selectedCategories += mUserSelectedCategories.get(i);
						if(i!=mUserSelectedCategories.size()-1){
							selectedCategories+=",";
						}
					}
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

		mNotificationImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Inflating custom Layout for PopUpWindow.
				mCalloutTriangleImage.setVisibility(View.VISIBLE);
				// Initializing PopUpWindow
				int popupheight = getWindowManager().getDefaultDisplay().getHeight()/2;
				final PopupWindow mPopUpWindow = new PopupWindow(mPopUpLayout,android.view.WindowManager.LayoutParams.WRAP_CONTENT,popupheight,true);
				mPopUpWindow.setWidth(tabBar.getWidth()-20);
				mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopUpWindow.setOutsideTouchable(false);
				mPopUpWindow.showAsDropDown(tabBar, 0, 20);
				mNotificationList.setAdapter(new CustomNotificationAdapter(NotificationSettings.this));
				mNotificationList.setOnItemClickListener(new NotificationItemClickListener());

				mPopUpWindow.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						mCalloutTriangleImage.setVisibility(View.INVISIBLE);
					}
				});
				// To dismiss popup window when touch outside..
				final Rect listviewRect = new Rect();
				mNotificationList.getHitRect(listviewRect);
				mPopUpLayout.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (!listviewRect.contains((int)event.getX(), (int)event.getY())) {
							Log.i("touch", "dismiss pop up");
							mPopUpWindow.dismiss();	
						}else{
							Log.i("touch", "dont dismiss");
						}
						return false;
					}
				});

			}
		});

		mLogoutImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// AsyncTask to call the logout webservice to end the login session
				new LogoutSessionTask(NotificationSettings.this).execute();
				Intent intent_logout = new Intent(NotificationSettings.this,ZouponsLogin.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				finish();
				startActivity(intent_logout);
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
				Log.i("chk","none");
				mContactFrequencyNewCodesCheckBox.setChecked(false);
				mContactFrequencyweeklyCheckBox.setChecked(false);
				mContactFrequencyMonthlyCheckBox.setChecked(false);
				mSelectedContactFrequency = 0;
				mCotactFrequncyCheckFlag =true;
				break;
			case R.id.chkNewCodesAvailable:
				mContactFrequencyNoneCheckBox.setChecked(false);
				mContactFrequencyweeklyCheckBox.setChecked(false);
				mContactFrequencyMonthlyCheckBox.setChecked(false);
				mSelectedContactFrequency = 1;
				mCotactFrequncyCheckFlag = true;
				break;
			case R.id.chkWeekly:
				mContactFrequencyNoneCheckBox.setChecked(false);
				mContactFrequencyNewCodesCheckBox.setChecked(false);
				mContactFrequencyMonthlyCheckBox.setChecked(false);
				mSelectedContactFrequency = 2;
				mCotactFrequncyCheckFlag = true;
				break;
			case R.id.chkMonthly:
				mContactFrequencyNoneCheckBox.setChecked(false);
				mContactFrequencyNewCodesCheckBox.setChecked(false);
				mContactFrequencyweeklyCheckBox.setChecked(false);
				mSelectedContactFrequency = 3;
				mCotactFrequncyCheckFlag = true;
				break;	  
			}

		}else{
			mCotactFrequncyCheckFlag = false;
		}
	}

	class NotificationAsyncTask extends AsyncTask<String, String, String>{

		private ProgressDialog mProgressView;
		private Context context;
		private ZouponsWebService zouponswebservice;
		private ZouponsParsingClass parsingclass;
		private NetworkCheck mConnectivityCheck;
		private String mFunction;

		public NotificationAsyncTask(Context context) {
			this.context = context;
			mProgressView = new ProgressDialog(context);
			mProgressView.setMessage("please wait..");
			mProgressView.setCancelable(false);
			zouponswebservice = new ZouponsWebService(context);
			parsingclass = new ZouponsParsingClass(context);
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
						mGetResponse=zouponswebservice.GetCatogories();	
						if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
							mParsingResponse=parsingclass.parseNotificationCategories(mGetResponse);
							if(mParsingResponse.equalsIgnoreCase("success")){
								mNotificationResponse = zouponswebservice.GetNotificationDetails();
								if(!mNotificationResponse.equals("failure") && !mNotificationResponse.equals("noresponse")){
									mNotificationParsingResponse =	parsingclass.parseNotificationDetails(mNotificationResponse);
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
						mGetResponse=zouponswebservice.SetNotificationDetails(String.valueOf(mSelectedContactFrequency),mSelectedNotifyBy,selectedCategories.trim());	
						if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
							mParsingResponse=parsingclass.parseSetNotificationDetails(mGetResponse);
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
						Log.i("categories",mNotification.categories );
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
									Log.i("sub list", "missing item");
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
							Log.i("sub list", "missing item");
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
				alertBox_service("Information", "Notification Details not avilable",context);
			}else if(result.equalsIgnoreCase("update failure")){
				alertBox_service("Information", "Notification Details not updated Please try after some time",context);
			}
		}
	}	

	static class ClickListenerForScrolling implements OnClickListener {
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
			
			leftMenuScrollView.fullScroll(View.FOCUS_UP);
			leftMenuScrollView.pageScroll(View.FOCUS_UP);
			
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

	static class SizeCallbackForMenu implements SizeCallback {
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

	public ArrayList<String> setCategoriesIdArrayList(HashMap<String, String> hashmap){
		ArrayList<String> mCategoriesId = new ArrayList<String>();
		mCategoriesId.clear();
		mCategoriesId=new ArrayList<String>(hashmap.keySet());
		return mCategoriesId;
	}
	
	public ArrayList<String> setCategoriesNameArrayList(HashMap<String, String> hashmap){
		ArrayList<String> mCategoriesName = new ArrayList<String>();
		mCategoriesName.clear();
		mCategoriesName=new ArrayList<String>(hashmap.values());
		return mCategoriesName;
	}
	
	public String getKey(String value){
		String key="";
		for (Entry<String, String> entry : WebServiceStaticArrays.categories.entrySet()) {
            if (entry.getValue().equals(value)) {
                key=entry.getKey();
            }
        }
		return key;
	}
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
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
		if(mNotificationSync!=null){
			mNotificationSync.cancelAlarm();
		}

	}

	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(NotificationSettings.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}

	@Override
	protected void onResume() {
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(NotificationSettings.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(NotificationSettings.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(NotificationSettings.this);
		mLogoutSession.setLogoutTimerAlarm();

		super.onResume();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
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

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			try{
				Log.i(TAG,"OnReceive");
				if(intent.hasExtra("FromNotification")){
					if(NotificationDetails.notificationcount>0){
						mNotificationCount.setVisibility(View.VISIBLE);
						mNotificationCount.setText(String.valueOf(NotificationDetails.notificationcount));
					}else{
						mNotificationCount.setVisibility(View.GONE);
						Log.i("Notification result", "No new notification");
					}
				}					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};	
}