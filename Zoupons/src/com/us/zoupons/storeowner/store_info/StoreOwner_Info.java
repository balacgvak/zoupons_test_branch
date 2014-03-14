package com.us.zoupons.storeowner.store_info;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;


import android.util.Log;
import android.util.Patterns;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.us.zoupons.Base64;
import com.us.zoupons.DecodeImageWithRotation;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.POJOStoreInfo;
import com.us.zoupons.classvariables.POJOStoreTiming;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

/**
 * 
 * Activity which hosts Store general info and business hours details
 *
 */

public class StoreOwner_Info extends Activity implements OnClickListener,OnCheckedChangeListener{

	// Initializing views and variables
	private String TAG="StoreOwner_Info";
	private MyHorizontalScrollView mScrollView;
	private View mApp,mRightMenu,mLeftMenu;
	private	Header mZouponsHeader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private int mMenuFlag;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private	Button mStoreOwner_Info_FreezeView;
	private ImageView mStoreOwner_Info_RightMenuHolder;
	private TextView mStoreOwner_Info_StoreName;
	private LinearLayout mStoreOwner_Info_Footer_AboutStore,mStoreOwner_Info_Footer_BusinessHours,mStoreOwner_Info_Footer_GeneralInfo;
	private Button mStoreOwner_Info_BusinessHours_Save,mStoreOwner_Info_AboutStore_Save;
	/*General Info */
	private ScrollView mStoreOwner_GeneralInfo_Container;
	private Button mStoreOwner_GeneralInfo_Save;
	private ImageView mStoreOwner_GeneralInfo_ChangeLogo;
	private EditText mStoreOwner_GeneralInfo_WebSite_Value;
	private ProgressBar mProgressBar;
	private int ONACTIVITYRESULTFLAG=0;
	public static byte[] mProfileImage;
	private Bitmap mBitmapProfileImage;
	private String mStoreLogoData;
	/*About Store*/
	private EditText mStoreOwner_Info_AboutStore;
	/*Business Hours*/
	private EditText mStoreOwner_BusinessHour_MondayStartTime,mStoreOwner_BusinessHour_MondayEndTime,mStoreOwner_BusinessHour_TuesdayStartTime,mStoreOwner_BusinessHour_TuesdayEndTime,mStoreOwner_BusinessHour_WednesdayStartTime,mStoreOwner_BusinessHour_WednesdayEndTime,
	mStoreOwner_BusinessHour_ThursdayStartTime,mStoreOwner_BusinessHour_ThursdayEndTime,mStoreOwner_BusinessHour_FridayStartTime,mStoreOwner_BusinessHour_FridayEndTime,mStoreOwner_BusinessHour_SaturdayStartTime,mStoreOwner_BusinessHour_SaturdayEndTime,mStoreOwner_BusinessHour_SundayStartTime,mStoreOwner_BusinessHour_SundayEndTime;
	private ImageView mStoreOwner_BusinessHour_MondayStartTime_Dropdown,mStoreOwner_BusinessHour_MondayEndTime_Dropdown,mStoreOwner_BusinessHour_TuesdayStartTime_Dropdown,mStoreOwner_BusinessHour_TuesdayEndTime_Dropdown,mStoreOwner_BusinessHour_WednesdayStartTime_Dropdown,mStoreOwner_BusinessHour_WednesdayEndTime_Dropdown,
	mStoreOwner_BusinessHour_ThursdayStartTime_Dropdown,mStoreOwner_BusinessHour_ThursdayEndTime_Dropdown,mStoreOwner_BusinessHour_FridayStartTime_Dropdown,mStoreOwner_BusinessHour_FridayEndTime_Dropdown,mStoreOwner_BusinessHour_SaturdayTime_Dropdown,mStoreOwner_BusinessHour_SaturdayEndTime_Dropdown,mStoreOwner_BusinessHour_SundayStartTime_Dropdown,mStoreOwner_BusinessHour_SundayEndTime_Dropdown;
	private CheckBox mStoreOwner_BusinessHour_MondayCheckBox,mStoreOwner_BusinessHour_TuesdayCheckBox,mStoreOwner_BusinessHour_WednesdayCheckBox,mStoreOwner_BusinessHour_ThursdayCheckBox,
	mStoreOwner_BusinessHour_FridayCheckBox,mStoreOwner_BusinessHour_SaturdayCheckBox,mStoreOwner_BusinessHour_SundayCheckBox;
	private String is_Monday_Closed="0",is_Tuesday_Closed="0",is_Wednesday_Closed="0",is_Thursday_Closed="0",is_Friday_Closed="0",is_Saturday_Closed="0",is_Sunday_Closed="0";
	//Logout without user interaction after 5 minute
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation
	private String START_TIME_VALIDATION= "start_time",END_TIME_VALIDATION = "end_time";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			LayoutInflater inflater = LayoutInflater.from(this);
			mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(mScrollView);
			mApp = inflater.inflate(R.layout.storeowner_info, null);
			final ViewGroup mInfoBusinessHours = (ViewGroup) mApp.findViewById(R.id.storeownerinfo_container);
			final ViewGroup mInfoAboutStore = (ViewGroup) mApp.findViewById(R.id.businesshours_aboutstore_container);
			final ViewGroup mInfoStoreNameHeader = (ViewGroup) mApp.findViewById(R.id.storeownerinfo_storename_header);
			final ViewGroup mInfoBusinessHoursHeader = (ViewGroup) mApp.findViewById(R.id.storeownerinfo_businesshours_header);
			final ViewGroup mInfoFooter = (ViewGroup) mApp.findViewById(R.id.storeownerinfo_footer);
			mStoreOwner_Info_FreezeView = (Button) mApp.findViewById(R.id.storeowner_info_freeze);
			// General info
			mStoreOwner_GeneralInfo_Container = (ScrollView) mApp.findViewById(R.id.storeownergeneralinfo_middleview);
			mStoreOwner_GeneralInfo_Save = (Button) mStoreOwner_GeneralInfo_Container.findViewById(R.id.storeownergeneralinfo_save);
			mStoreOwner_GeneralInfo_ChangeLogo = (ImageView) mStoreOwner_GeneralInfo_Container.findViewById(R.id.storeownergeneralinfo_storelogo);
			mStoreOwner_GeneralInfo_WebSite_Value = (EditText) mStoreOwner_GeneralInfo_Container.findViewById(R.id.storeownergeneralinfo_website_value);
			mProgressBar = (ProgressBar) mStoreOwner_GeneralInfo_Container.findViewById(R.id.progess_bar);				
			mStoreOwner_Info_Footer_AboutStore=(LinearLayout) mInfoFooter.findViewById(R.id.storeownerinfo_footer_aboutstore);
			// About store
			mStoreOwner_Info_AboutStore = (EditText) mInfoAboutStore.findViewById(R.id.businesshours_aboutstore);
			mStoreOwner_Info_AboutStore_Save = (Button) mInfoAboutStore.findViewById(R.id.businesshours_aboutstore_save);
			//Business Hours
			mStoreOwner_BusinessHour_MondayStartTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_monday_open_value);
			mStoreOwner_BusinessHour_MondayEndTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_monday_close_value);
			mStoreOwner_BusinessHour_TuesdayStartTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_tuesday_open_value);
			mStoreOwner_BusinessHour_TuesdayEndTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_tuesday_close_value);
			mStoreOwner_BusinessHour_WednesdayStartTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_wednesday_open_value);
			mStoreOwner_BusinessHour_WednesdayEndTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_wednesday_close_value);
			mStoreOwner_BusinessHour_ThursdayStartTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_thursday_open_value);
			mStoreOwner_BusinessHour_ThursdayEndTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_thursday_close_value);
			mStoreOwner_BusinessHour_FridayStartTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_friday_open_value);
			mStoreOwner_BusinessHour_FridayEndTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_friday_close_value);
			mStoreOwner_BusinessHour_SaturdayStartTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_saturday_open_value);
			mStoreOwner_BusinessHour_SaturdayEndTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_saturday_close_value);
			mStoreOwner_BusinessHour_SundayStartTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_sunday_open_value);
			mStoreOwner_BusinessHour_SundayEndTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_sunday_close_value);
			mStoreOwner_BusinessHour_MondayStartTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_monday_open_dropdown);
			mStoreOwner_BusinessHour_MondayEndTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_monday_close_dropdown);
			mStoreOwner_BusinessHour_TuesdayStartTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_tuesday_open_dropdown);
			mStoreOwner_BusinessHour_TuesdayEndTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_tuesday_close_dropdown);
			mStoreOwner_BusinessHour_WednesdayStartTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_wednesday_open_dropdown);
			mStoreOwner_BusinessHour_WednesdayEndTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_wednesday_close_dropdown);
			mStoreOwner_BusinessHour_ThursdayStartTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_thursday_open_dropdown);
			mStoreOwner_BusinessHour_ThursdayEndTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_thursday_close_dropdown);
			mStoreOwner_BusinessHour_FridayStartTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_friday_open_dropdown);
			mStoreOwner_BusinessHour_FridayEndTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_friday_close_dropdown);
			mStoreOwner_BusinessHour_SaturdayTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_saturday_open_dropdown);
			mStoreOwner_BusinessHour_SaturdayEndTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_saturday_close_dropdown);
			mStoreOwner_BusinessHour_SundayStartTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_sunday_open_dropdown);
			mStoreOwner_BusinessHour_SundayEndTime_Dropdown = (ImageView) mInfoBusinessHours.findViewById(R.id.businesshour_sunday_close_dropdown);
			mStoreOwner_BusinessHour_MondayCheckBox = (CheckBox) mInfoBusinessHours.findViewById(R.id.businesshours_monday_checkbox);
			mStoreOwner_BusinessHour_TuesdayCheckBox = (CheckBox) mInfoBusinessHours.findViewById(R.id.businesshours_tuesday_checkbox);
			mStoreOwner_BusinessHour_WednesdayCheckBox = (CheckBox) mInfoBusinessHours.findViewById(R.id.businesshours_wednesday_checkbox);
			mStoreOwner_BusinessHour_ThursdayCheckBox = (CheckBox) mInfoBusinessHours.findViewById(R.id.businesshours_thursday_checkbox);
			mStoreOwner_BusinessHour_FridayCheckBox = (CheckBox) mInfoBusinessHours.findViewById(R.id.businesshours_friday_checkbox);
			mStoreOwner_BusinessHour_SaturdayCheckBox = (CheckBox) mInfoBusinessHours.findViewById(R.id.businesshours_saturday_checkbox);
			mStoreOwner_BusinessHour_SundayCheckBox = (CheckBox) mInfoBusinessHours.findViewById(R.id.businesshours_sunday_checkbox);
			mStoreOwner_Info_BusinessHours_Save = (Button) mInfoBusinessHours.findViewById(R.id.businesshours_save);
			// set click Listner for business hour start and end time
			mStoreOwner_BusinessHour_MondayStartTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_MondayEndTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_TuesdayStartTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_TuesdayEndTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_WednesdayStartTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_WednesdayEndTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_ThursdayStartTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_ThursdayEndTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_FridayStartTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_FridayEndTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_SaturdayStartTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_SaturdayEndTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_SundayStartTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_SundayEndTime.setOnClickListener(this);
			mStoreOwner_BusinessHour_MondayStartTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_MondayEndTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_TuesdayStartTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_TuesdayEndTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_WednesdayStartTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_WednesdayEndTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_ThursdayStartTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_ThursdayEndTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_FridayStartTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_FridayEndTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_SaturdayTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_SaturdayEndTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_SundayStartTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_SundayEndTime_Dropdown.setOnClickListener(this);
			mStoreOwner_BusinessHour_MondayCheckBox.setOnCheckedChangeListener(this);
			mStoreOwner_BusinessHour_TuesdayCheckBox.setOnCheckedChangeListener(this);
			mStoreOwner_BusinessHour_WednesdayCheckBox.setOnCheckedChangeListener(this);
			mStoreOwner_BusinessHour_ThursdayCheckBox.setOnCheckedChangeListener(this);
			mStoreOwner_BusinessHour_FridayCheckBox.setOnCheckedChangeListener(this);
			mStoreOwner_BusinessHour_SaturdayCheckBox.setOnCheckedChangeListener(this);
			mStoreOwner_BusinessHour_SundayCheckBox.setOnCheckedChangeListener(this);

			mStoreOwner_Info_Footer_GeneralInfo=(LinearLayout) mInfoFooter.findViewById(R.id.storeownerinfo_footer_GeneralInfo);
			mStoreOwner_Info_Footer_BusinessHours=(LinearLayout) mInfoFooter.findViewById(R.id.storeownerinfo_footer_businesshours);
			mStoreOwner_Info_Footer_GeneralInfo.setBackgroundResource(R.drawable.footer_dark_blue_new);
			mStoreOwner_Info_StoreName = (TextView) mInfoStoreNameHeader.findViewById(R.id.storeowner_info_storename);
			mStoreOwner_Info_RightMenuHolder = (ImageView) mInfoStoreNameHeader.findViewById(R.id.storeowner_info_rightmenu);
			mStoreownerRightmenu = new StoreOwner_RightMenu(StoreOwner_Info.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_Info_FreezeView, TAG);
			mRightMenu = mStoreownerRightmenu.intializeInflater();
			mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreOwner_Info.this,mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Info_FreezeView, TAG);
			mLeftMenu = mStoreownerLeftmenu.intializeInflater();
			mStoreownerLeftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			mStoreownerRightmenu.clickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsHeader = (Header) mApp.findViewById(R.id.storeownerinfo_header);
			mZouponsHeader.intializeInflater(mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Info_FreezeView, TAG);
			final View[] children = new View[] { mLeftMenu, mApp, mRightMenu };
			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsHeader.mLeftMenuBtnSlide));
			mStoreOwner_Info_RightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwner_Info_FreezeView, TAG,mStoreOwner_GeneralInfo_WebSite_Value,mStoreOwner_Info_AboutStore,false));
			mStoreOwner_Info_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_Info_FreezeView, TAG,mStoreOwner_GeneralInfo_WebSite_Value,mStoreOwner_Info_AboutStore,true));
			mNotificationReceiver = new NotifitcationReceiver(mZouponsHeader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));

			if(new NetworkCheck().ConnectivityCheck(this)){ // Connectivity check
				// To get store details async task
				Get_UpdateStoreGeneralInfoTask mGet_Update_storeInfo = new Get_UpdateStoreGeneralInfoTask(this,"Get");
				mGet_Update_storeInfo.execute();	
			}else{
				Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show();
			}
			// To set StoreName
			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
			String mStoreName = mPrefs.getString("store_name", "");
			mStoreOwner_Info_StoreName.setText(mStoreName);
			mStoreOwner_GeneralInfo_ChangeLogo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try{
						contextMenuOpen(mStoreOwner_GeneralInfo_ChangeLogo);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});

			mStoreOwner_GeneralInfo_Save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mStoreOwner_GeneralInfo_WebSite_Value.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter web address");
					}else if(!Patterns.WEB_URL.matcher(mStoreOwner_GeneralInfo_WebSite_Value.getText().toString().trim()).matches()){
						alertBox_service("Information", "Please enter valid web address");
					}else{
						if(new NetworkCheck().ConnectivityCheck(StoreOwner_Info.this)){ // Connectivity check
							Get_UpdateStoreGeneralInfoTask mGet_Update_storeInfo = new Get_UpdateStoreGeneralInfoTask(StoreOwner_Info.this,"Set",mStoreOwner_GeneralInfo_WebSite_Value.getText().toString(),mStoreLogoData,mStoreOwner_Info_AboutStore.getText().toString());
							mGet_Update_storeInfo.execute();
						}else{
							Toast.makeText(StoreOwner_Info.this, "No Network Connection", Toast.LENGTH_SHORT).show();
						}

					}
				}
			});

			mStoreOwner_Info_Footer_GeneralInfo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mStoreOwner_Info_Footer_BusinessHours.setBackgroundResource(R.drawable.header_2);
					mStoreOwner_Info_Footer_AboutStore.setBackgroundResource(R.drawable.header_2);
					v.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mStoreOwner_GeneralInfo_Container.setVisibility(View.VISIBLE);
					mInfoBusinessHours.setVisibility(View.GONE);
					mInfoBusinessHoursHeader.setVisibility(View.GONE);
					mInfoAboutStore.setVisibility(View.GONE);
				}
			});


			mStoreOwner_Info_Footer_BusinessHours.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mInfoBusinessHours.getVisibility()!=View.VISIBLE){
						mStoreOwner_Info_Footer_AboutStore.setBackgroundResource(R.drawable.header_2);
						mStoreOwner_Info_Footer_GeneralInfo.setBackgroundResource(R.drawable.header_2);
						v.setBackgroundResource(R.drawable.footer_dark_blue_new);
						mInfoBusinessHours.setVisibility(View.VISIBLE);
						mInfoBusinessHoursHeader.setVisibility(View.VISIBLE);
						mStoreOwner_GeneralInfo_Container.setVisibility(View.GONE);
						mInfoAboutStore.setVisibility(View.GONE);
						clearTimingFields();
						if(new NetworkCheck().ConnectivityCheck(StoreOwner_Info.this)){ // Connectivity check
							// To get store details async task
							Get_UpdateStoreGeneralInfoTask mGet_Update_storeInfo = new Get_UpdateStoreGeneralInfoTask(StoreOwner_Info.this,"Get_Timings");
							mGet_Update_storeInfo.execute();	
						}else{
							Toast.makeText(StoreOwner_Info.this, "No Network Connection", Toast.LENGTH_SHORT).show();
						}
					}
				}
			});

			mStoreOwner_Info_Footer_AboutStore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mInfoAboutStore.getVisibility()!=View.VISIBLE){
						mStoreOwner_Info_Footer_BusinessHours.setBackgroundResource(R.drawable.header_2);
						mStoreOwner_Info_Footer_GeneralInfo.setBackgroundResource(R.drawable.header_2);
						v.setBackgroundResource(R.drawable.footer_dark_blue_new);
						mInfoAboutStore.setVisibility(View.VISIBLE);
						mStoreOwner_GeneralInfo_Container.setVisibility(View.GONE);
						mInfoBusinessHours.setVisibility(View.GONE);
						mInfoBusinessHoursHeader.setVisibility(View.GONE);
					}
				}
			});

			mStoreOwner_Info_AboutStore_Save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(new NetworkCheck().ConnectivityCheck(StoreOwner_Info.this)){ // Connectivity check
						Get_UpdateStoreGeneralInfoTask mGet_Update_storeInfo = new Get_UpdateStoreGeneralInfoTask(StoreOwner_Info.this,"set_Aboutstore",mStoreOwner_GeneralInfo_WebSite_Value.getText().toString(),mStoreLogoData,mStoreOwner_Info_AboutStore.getText().toString());
						mGet_Update_storeInfo.execute();
					}else{
						Toast.makeText(StoreOwner_Info.this, "No Network Connection", Toast.LENGTH_SHORT).show();
					}
				}
			});

			mStoreOwner_Info_BusinessHours_Save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(is_Monday_Closed.equalsIgnoreCase("0") && (mStoreOwner_BusinessHour_MondayStartTime.getText().toString().length() == 0 || mStoreOwner_BusinessHour_MondayEndTime.getText().toString().length() == 0)){
						alertBox_service("Information", "Please choose monday business hour details");
					}else if(is_Tuesday_Closed.equalsIgnoreCase("0") && (mStoreOwner_BusinessHour_TuesdayStartTime.getText().toString().length() == 0 || mStoreOwner_BusinessHour_TuesdayEndTime.getText().toString().length() == 0)){
						alertBox_service("Information", "Please choose tuesday business hour details");
					}else if(is_Wednesday_Closed.equalsIgnoreCase("0") && (mStoreOwner_BusinessHour_WednesdayStartTime.getText().toString().length() == 0 || mStoreOwner_BusinessHour_WednesdayEndTime.getText().toString().length() == 0)){
						alertBox_service("Information", "Please choose wednesday business hour details");
					}else if(is_Thursday_Closed.equalsIgnoreCase("0") && (mStoreOwner_BusinessHour_ThursdayStartTime.getText().toString().length() == 0 || mStoreOwner_BusinessHour_ThursdayEndTime.getText().toString().length() == 0)){
						alertBox_service("Information", "Please choose thursday business hour details");
					}else if(is_Friday_Closed.equalsIgnoreCase("0") && (mStoreOwner_BusinessHour_FridayStartTime.getText().toString().length() == 0 || mStoreOwner_BusinessHour_FridayEndTime.getText().toString().length() == 0)){
						alertBox_service("Information", "Please choose friday business hour details");
					}else if(is_Saturday_Closed.equalsIgnoreCase("0")&& (mStoreOwner_BusinessHour_SaturdayStartTime.getText().toString().length() == 0 || mStoreOwner_BusinessHour_SaturdayEndTime.getText().toString().length() == 0)){
						alertBox_service("Information", "Please choose saturday business hour details");
					}else if(is_Sunday_Closed.equalsIgnoreCase("0") && (mStoreOwner_BusinessHour_SundayStartTime.getText().toString().length() == 0 || mStoreOwner_BusinessHour_SundayEndTime.getText().toString().length() == 0)){
						alertBox_service("Information", "Please choose sunday business hour details");
					}else{
						if(new NetworkCheck().ConnectivityCheck(StoreOwner_Info.this)){ // Connectivity check
							Get_UpdateStoreGeneralInfoTask mGet_Update_storeInfo = new Get_UpdateStoreGeneralInfoTask(StoreOwner_Info.this,"set_timings");
							mGet_Update_storeInfo.execute(convertTime(mStoreOwner_BusinessHour_MondayStartTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_MondayEndTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_TuesdayStartTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_TuesdayEndTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_WednesdayStartTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_WednesdayEndTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_ThursdayStartTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_ThursdayEndTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_FridayStartTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_FridayEndTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_SaturdayStartTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_SaturdayEndTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_SundayStartTime.getText().toString()),
									convertTime(mStoreOwner_BusinessHour_SundayEndTime.getText().toString()),
									is_Monday_Closed,is_Tuesday_Closed,is_Wednesday_Closed,is_Thursday_Closed,is_Friday_Closed,is_Saturday_Closed,is_Sunday_Closed);
						}else{
							Toast.makeText(StoreOwner_Info.this, "No Network Connection", Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onClick(View v) {
		if(v.equals(mStoreOwner_BusinessHour_MondayStartTime)){ // Monday Start Time
			validateBusinessStartTime(START_TIME_VALIDATION,mStoreOwner_BusinessHour_MondayStartTime,mStoreOwner_BusinessHour_MondayEndTime,"monday");
		}else if(v.equals(mStoreOwner_BusinessHour_MondayEndTime)){ // Monday End Time
			validateBusinessStartTime(END_TIME_VALIDATION,mStoreOwner_BusinessHour_MondayStartTime,mStoreOwner_BusinessHour_MondayEndTime,"monday");
		}else if(v.equals(mStoreOwner_BusinessHour_TuesdayStartTime)){
			validateBusinessStartTime(START_TIME_VALIDATION,mStoreOwner_BusinessHour_TuesdayStartTime,mStoreOwner_BusinessHour_TuesdayEndTime,"tuesday");
		}else if(v.equals(mStoreOwner_BusinessHour_TuesdayEndTime)){
			validateBusinessStartTime(END_TIME_VALIDATION,mStoreOwner_BusinessHour_TuesdayStartTime,mStoreOwner_BusinessHour_TuesdayEndTime,"tuesday");
		}else if(v.equals(mStoreOwner_BusinessHour_WednesdayStartTime)){
			validateBusinessStartTime(START_TIME_VALIDATION,mStoreOwner_BusinessHour_WednesdayStartTime,mStoreOwner_BusinessHour_WednesdayEndTime,"wednesday");
		}else if(v.equals(mStoreOwner_BusinessHour_WednesdayEndTime)){
			validateBusinessStartTime(END_TIME_VALIDATION,mStoreOwner_BusinessHour_WednesdayStartTime,mStoreOwner_BusinessHour_WednesdayEndTime,"wednesday");
		}else if(v.equals(mStoreOwner_BusinessHour_ThursdayStartTime)){
			validateBusinessStartTime(START_TIME_VALIDATION,mStoreOwner_BusinessHour_ThursdayStartTime,mStoreOwner_BusinessHour_ThursdayEndTime,"thursday");
		}else if(v.equals(mStoreOwner_BusinessHour_ThursdayEndTime)){
			validateBusinessStartTime(END_TIME_VALIDATION,mStoreOwner_BusinessHour_ThursdayStartTime,mStoreOwner_BusinessHour_ThursdayEndTime,"thursday");
		}else if(v.equals(mStoreOwner_BusinessHour_FridayStartTime)){
			validateBusinessStartTime(START_TIME_VALIDATION,mStoreOwner_BusinessHour_FridayStartTime,mStoreOwner_BusinessHour_FridayEndTime,"friday");
		}else if(v.equals(mStoreOwner_BusinessHour_FridayEndTime)){
			validateBusinessStartTime(END_TIME_VALIDATION,mStoreOwner_BusinessHour_FridayStartTime,mStoreOwner_BusinessHour_FridayEndTime,"friday");
		}else if(v.equals(mStoreOwner_BusinessHour_SaturdayStartTime)){
			validateBusinessStartTime(START_TIME_VALIDATION,mStoreOwner_BusinessHour_SaturdayStartTime,mStoreOwner_BusinessHour_SaturdayEndTime,"saturday");
		}else if(v.equals(mStoreOwner_BusinessHour_SaturdayEndTime)){
			validateBusinessStartTime(END_TIME_VALIDATION,mStoreOwner_BusinessHour_SaturdayStartTime,mStoreOwner_BusinessHour_SaturdayEndTime,"saturday");
		}else if(v.equals(mStoreOwner_BusinessHour_SundayStartTime)){
			validateBusinessStartTime(START_TIME_VALIDATION,mStoreOwner_BusinessHour_SundayStartTime,mStoreOwner_BusinessHour_SundayEndTime,"sunday");
		}else if(v.equals(mStoreOwner_BusinessHour_SundayEndTime)){
			validateBusinessStartTime(END_TIME_VALIDATION,mStoreOwner_BusinessHour_SundayStartTime,mStoreOwner_BusinessHour_SundayEndTime,"sunday");
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		ManageCheckBoxClick(buttonView, isChecked,true);
	}

	// To set business day closed or not 
	public void ManageCheckBoxClick(View buttonView , boolean isChecked,boolean should_showDefaultTime){
		if(buttonView.equals(mStoreOwner_BusinessHour_MondayCheckBox)){
			is_Monday_Closed = (isChecked)?"1":"0"; 
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_MondayStartTime, mStoreOwner_BusinessHour_MondayEndTime, mStoreOwner_BusinessHour_MondayStartTime_Dropdown, mStoreOwner_BusinessHour_MondayEndTime_Dropdown, isChecked,should_showDefaultTime);
		}else if(buttonView.equals(mStoreOwner_BusinessHour_TuesdayCheckBox)){
			is_Tuesday_Closed = (isChecked)?"1":"0";
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_TuesdayStartTime, mStoreOwner_BusinessHour_TuesdayEndTime, mStoreOwner_BusinessHour_TuesdayStartTime_Dropdown, mStoreOwner_BusinessHour_TuesdayEndTime_Dropdown, isChecked,should_showDefaultTime);
		}else if(buttonView.equals(mStoreOwner_BusinessHour_WednesdayCheckBox)){
			is_Wednesday_Closed = (isChecked)?"1":"0";
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_WednesdayStartTime, mStoreOwner_BusinessHour_WednesdayEndTime, mStoreOwner_BusinessHour_WednesdayStartTime_Dropdown, mStoreOwner_BusinessHour_WednesdayEndTime_Dropdown, isChecked,should_showDefaultTime);
		}else if(buttonView.equals(mStoreOwner_BusinessHour_ThursdayCheckBox)){
			is_Thursday_Closed = (isChecked)?"1":"0";
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_ThursdayStartTime, mStoreOwner_BusinessHour_ThursdayEndTime, mStoreOwner_BusinessHour_ThursdayStartTime_Dropdown, mStoreOwner_BusinessHour_ThursdayEndTime_Dropdown, isChecked,should_showDefaultTime);
		}else if(buttonView.equals(mStoreOwner_BusinessHour_FridayCheckBox)){
			is_Friday_Closed = (isChecked)?"1":"0";
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_FridayStartTime, mStoreOwner_BusinessHour_FridayEndTime, mStoreOwner_BusinessHour_FridayStartTime_Dropdown, mStoreOwner_BusinessHour_FridayEndTime_Dropdown, isChecked,should_showDefaultTime);
		}else if(buttonView.equals(mStoreOwner_BusinessHour_SaturdayCheckBox)){
			is_Saturday_Closed = (isChecked)?"1":"0";
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_SaturdayStartTime, mStoreOwner_BusinessHour_SaturdayEndTime, mStoreOwner_BusinessHour_SaturdayTime_Dropdown, mStoreOwner_BusinessHour_SaturdayEndTime_Dropdown, isChecked,should_showDefaultTime);
		}else if(buttonView.equals(mStoreOwner_BusinessHour_SundayCheckBox)){
			is_Sunday_Closed = (isChecked)?"1":"0";
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_SundayStartTime, mStoreOwner_BusinessHour_SundayEndTime, mStoreOwner_BusinessHour_SundayStartTime_Dropdown, mStoreOwner_BusinessHour_SundayEndTime_Dropdown, isChecked,should_showDefaultTime);
		}
	}

	// To clear edittext(time) when closed checkbox is checked...
	public void clearTimeFieldsIfClosed(EditText mStartTime,EditText mEndTime,ImageView startTimeDropdown,ImageView endTimeDropdown,boolean isClosed,boolean should_showDefaultTime){
		if(isClosed){
			mStartTime.getText().clear();
			mEndTime.getText().clear();
			mStartTime.setEnabled(false);
			mEndTime.setEnabled(false);
			startTimeDropdown.setEnabled(false);
			endTimeDropdown.setEnabled(false);
		}else{
			if(should_showDefaultTime){
				mStartTime.setText("09:00 AM");
				mEndTime.setText("5:00 PM");	
			}
			mStartTime.setEnabled(true);
			mEndTime.setEnabled(true);
			startTimeDropdown.setEnabled(true);
			endTimeDropdown.setEnabled(true);
		}
	}

	// After fetching info from server, updating the  general info views
	public void updateViews(ArrayList<Object> result){
		POJOStoreInfo mStoreDetails = (POJOStoreInfo) result.get(0);
		mStoreOwner_GeneralInfo_WebSite_Value.setText(mStoreDetails.website);
		LoadStoreLogoTask loadStorelogo = new LoadStoreLogoTask(mStoreOwner_GeneralInfo_ChangeLogo,mProgressBar);
		loadStorelogo.execute(mStoreDetails.logo_path+"&w=250&h=150&zc=0");
		mStoreOwner_Info_StoreName.setText(mStoreDetails.store_name);
		mStoreOwner_Info_AboutStore.setText(mStoreDetails.description);   	    	
	}

	// After fetching info from server, updating the  Business hours views
	public void updateBusinessHoursView(ArrayList<Object> result) {
		POJOStoreTiming mStoreTimingDetails = (POJOStoreTiming) result.get(0);
		mStoreOwner_BusinessHour_MondayStartTime.setText(mStoreTimingDetails.monday_from);
		mStoreOwner_BusinessHour_MondayEndTime.setText(mStoreTimingDetails.monday_to);
		mStoreOwner_BusinessHour_TuesdayStartTime.setText(mStoreTimingDetails.tuesday_from);
		mStoreOwner_BusinessHour_TuesdayEndTime.setText(mStoreTimingDetails.tuesday_to);
		mStoreOwner_BusinessHour_WednesdayStartTime.setText(mStoreTimingDetails.wednesday_from);
		mStoreOwner_BusinessHour_WednesdayEndTime.setText(mStoreTimingDetails.wednesday_to);
		mStoreOwner_BusinessHour_ThursdayStartTime.setText(mStoreTimingDetails.thursday_from);
		mStoreOwner_BusinessHour_ThursdayEndTime.setText(mStoreTimingDetails.thursday_to);
		mStoreOwner_BusinessHour_FridayStartTime.setText(mStoreTimingDetails.friday_from);
		mStoreOwner_BusinessHour_FridayEndTime.setText(mStoreTimingDetails.friday_to);
		mStoreOwner_BusinessHour_SaturdayStartTime.setText(mStoreTimingDetails.saturday_from);
		mStoreOwner_BusinessHour_SaturdayEndTime.setText(mStoreTimingDetails.saturday_to);
		mStoreOwner_BusinessHour_SundayStartTime.setText(mStoreTimingDetails.sunday_from);
		mStoreOwner_BusinessHour_SundayEndTime.setText(mStoreTimingDetails.sunday_to);
		ManageCheckBox(mStoreOwner_BusinessHour_MondayCheckBox,mStoreTimingDetails.is_monday_closed);
		ManageCheckBox(mStoreOwner_BusinessHour_TuesdayCheckBox,mStoreTimingDetails.is_tuesday_closed);
		ManageCheckBox(mStoreOwner_BusinessHour_WednesdayCheckBox,mStoreTimingDetails.is_wednesday_closed);
		ManageCheckBox(mStoreOwner_BusinessHour_ThursdayCheckBox,mStoreTimingDetails.is_thursday_closed);
		ManageCheckBox(mStoreOwner_BusinessHour_FridayCheckBox,mStoreTimingDetails.is_friday_closed);
		ManageCheckBox(mStoreOwner_BusinessHour_SaturdayCheckBox,mStoreTimingDetails.is_saturday_closed);
		ManageCheckBox(mStoreOwner_BusinessHour_SundayCheckBox,mStoreTimingDetails.is_sunday_closed);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_MondayCheckBox,(mStoreTimingDetails.is_monday_closed.equalsIgnoreCase("1"))?true:false,false);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_TuesdayCheckBox,(mStoreTimingDetails.is_tuesday_closed.equalsIgnoreCase("1"))?true:false,false);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_WednesdayCheckBox,(mStoreTimingDetails.is_wednesday_closed.equalsIgnoreCase("1"))?true:false,false);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_ThursdayCheckBox,(mStoreTimingDetails.is_thursday_closed.equalsIgnoreCase("1"))?true:false,false);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_FridayCheckBox,(mStoreTimingDetails.is_friday_closed.equalsIgnoreCase("1"))?true:false,false);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_SaturdayCheckBox,(mStoreTimingDetails.is_saturday_closed.equalsIgnoreCase("1"))?true:false,false);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_SundayCheckBox,(mStoreTimingDetails.is_sunday_closed.equalsIgnoreCase("1"))?true:false,false);
	}

	// To Check/Uncheck checkbox based upon close 
	private void ManageCheckBox(CheckBox checkbox,String is_closed) {
		if(is_closed.equalsIgnoreCase("0")){
			checkbox.setChecked(false);
		}else{
			checkbox.setOnCheckedChangeListener(null);
			checkbox.setChecked(true);
			checkbox.setOnCheckedChangeListener(this);
		}

	}

	// To open context menu
	private void contextMenuOpen(View sender){
		sender.setLongClickable(false);
		registerForContextMenu(sender);
		openContextMenu(sender);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if(v.equals(mStoreOwner_GeneralInfo_ChangeLogo)){
			menu.setHeaderTitle("Choose Image From");
			menu.add(3, 0, 0, "Take Picture");
			menu.add(3, 1, 1, "Gallery");
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch(item.getGroupId()){
		case 3:  // for adding user image from camera or gallery
			if(item.getItemId() == 0){ // To launch camera intent
				ONACTIVITYRESULTFLAG=1;
				Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent_camera, 1);
			}else if(item.getItemId() == 1){ // To launch gallery intent
				ONACTIVITYRESULTFLAG=1;
				Intent mGalleryIntent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(mGalleryIntent, 2);
			}
			return true;	
		default :
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(ONACTIVITYRESULTFLAG==1){
			if(resultCode!=0){
				if(requestCode==1){
					setImageBitmap(data);
				}else if(requestCode == 2){
					setImageBitmap(data);
				}
			}
		}
	}



	// To download image from url and set it in image view
	public void setImageBitmap(Intent data){
		try{
			Uri uri=data.getData();
			// specifying column(data) for retrieval
			String[] file_path_columns={MediaStore.Images.Media.DATA};
			// querying content provider to get particular image
			Cursor cursor=getContentResolver().query(uri, file_path_columns, null, null, null);
			cursor.moveToFirst();
			// getting col_index from string file_path_columns[0]--> Data column 
			int column_index=cursor.getColumnIndex(file_path_columns[0]);
			// getting the path from result as /sdcard/DCIM/100ANDRO/file_name
			String selected_file_path=cursor.getString(column_index);
			cursor.close();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(selected_file_path, options);
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, getApplicationContext().getResources().getDimensionPixelSize(R.dimen.generalinfo_storelogo_width), getApplicationContext().getResources().getDimensionPixelSize(R.dimen.generalinfo_storelogo_width));
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			mBitmapProfileImage = BitmapFactory.decodeFile(selected_file_path, options);
			mBitmapProfileImage = new DecodeImageWithRotation().decodeImage(selected_file_path, mBitmapProfileImage, getApplicationContext().getResources().getDimensionPixelSize(R.dimen.generalinfo_storelogo_width), getApplicationContext().getResources().getDimensionPixelSize(R.dimen.generalinfo_storelogo_height));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mBitmapProfileImage.compress(Bitmap.CompressFormat.PNG, 100, baos);    
			mProfileImage = baos.toByteArray();
			mStoreLogoData = com.us.zoupons.Base64.encodeBytes(mProfileImage);
			mBitmapProfileImage=Bitmap.createScaledBitmap(mBitmapProfileImage, getApplicationContext().getResources().getDimensionPixelSize(R.dimen.generalinfo_storelogo_width), getApplicationContext().getResources().getDimensionPixelSize(R.dimen.generalinfo_storelogo_height), true);
			mStoreOwner_GeneralInfo_ChangeLogo.setImageBitmap(mBitmapProfileImage);
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Please select valid image.", Toast.LENGTH_SHORT).show();
		}
	}

	// To calculate respective sample size for decoding Bimtap to avoid OOM Exception
	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	// To update business hour in selected edittext  
	private void updateTime(int hours, int mins,EditText edittext) {
		try{
			String timeSet = "";
			if (hours > 12) {
				hours -= 12;
				timeSet = "PM";
			} else if (hours == 0) {
				hours += 12;
				timeSet = "AM";
			} else if (hours == 12)
				timeSet = "PM";
			else
				timeSet = "AM";
			// Append in a StringBuilder
			String aTime = new StringBuilder().append(String.format("%02d",hours)).append(':')
					.append(String.format("%02d",mins)).append(" ").append(timeSet).toString();
			edittext.setText(aTime);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void validateBusinessStartTime(final String validate_for,final EditText startTimevalue,final EditText endTimevalue,final String day){

			int hour=0,minute=0;
			String previousTime = startTimevalue.getText().toString();
			if(validate_for.equalsIgnoreCase(START_TIME_VALIDATION)){ // For start time validation
				previousTime = startTimevalue.getText().toString();
			}else{
				previousTime = endTimevalue.getText().toString();
			}
			
			if(!previousTime.equalsIgnoreCase("")){
				String convertedTime  = convertTime(previousTime);
				hour = Integer.parseInt(convertedTime.split(":")[0]);
				minute = Integer.parseInt(convertedTime.split(":")[1]);
			}else{
				hour = 9; // Default time
				minute = 0;
			}
			TimePickerDialog mTimePicker = new TimePickerDialog(StoreOwner_Info.this,new OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

					if(validate_for.equalsIgnoreCase(START_TIME_VALIDATION)){ // For start time validation

						if(endTimevalue.getText().toString().length() > 0){
							try {
								String mBusinessEndTime = endTimevalue.getText().toString();
								SimpleDateFormat mBusinessTimeFormat = new SimpleDateFormat("hh:mm a");
								Date endTime = mBusinessTimeFormat.parse(mBusinessEndTime);
								Calendar mCurrentDate = Calendar.getInstance();
								endTime.setYear(mCurrentDate.get(Calendar.YEAR));
								endTime.setDate(mCurrentDate.get(Calendar.DAY_OF_MONTH));
								endTime.setMonth(mCurrentDate.get(Calendar.MONTH));
								Date startTime = new Date(mCurrentDate.get(Calendar.YEAR), mCurrentDate.get(Calendar.MONTH), mCurrentDate.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
								Log.i("start date", startTime.toString() + " " + startTime.getHours() + " " + startTime.getMinutes());
								Log.i("end date", endTime.toString()+ " " + endTime.getHours() + " " + endTime.getMinutes());
								if(startTime.compareTo(endTime) >= 0){
									endTimevalue.getText().clear();
								}
     							updateTime(hourOfDay, minute,startTimevalue);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}   
						}else{
							updateTime(hourOfDay, minute,startTimevalue);
						}


					}else{ // For end Time validation
						if(startTimevalue.getText().toString().length() > 0){ // If start time is selected
							try {
								String mBusinessStartTime = startTimevalue.getText().toString();
								SimpleDateFormat mBusinessTimeFormat = new SimpleDateFormat("hh:mm a");
								Date startTime = mBusinessTimeFormat.parse(mBusinessStartTime);
								Calendar mCurrentDate = Calendar.getInstance();
								startTime.setYear(mCurrentDate.get(Calendar.YEAR));
								startTime.setDate(mCurrentDate.get(Calendar.DAY_OF_MONTH));
								startTime.setMonth(mCurrentDate.get(Calendar.MONTH));
								Date endTime = new Date(mCurrentDate.get(Calendar.YEAR), mCurrentDate.get(Calendar.MONTH), mCurrentDate.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
								Log.i("start date", startTime.toString() + " " + startTime.getHours() + " " + startTime.getMinutes());
								Log.i("end date", endTime.toString()+ " " + endTime.getHours() + " " + endTime.getMinutes());
								if(endTime.compareTo(startTime) > 0){
									updateTime(hourOfDay, minute,endTimevalue);
								}else{
									endTimevalue.getText().clear();
									//endTimevalue.setHint("00.00");
									alertBox_service("Information", "Please select End time greater than Start time for " + day);
								}
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
						}else{ // If start time is not selected
							alertBox_service("Information", "Please choose start time first");
						}
					}
				}
			},hour, minute,false);
			mTimePicker.show();
		
	}




	/* To show alert pop up with respective message */
	private void alertBox_service(String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwner_Info.this);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mStoreLogoData = null;
		mProfileImage = null;
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
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// To listen for broadcast receiver to receive notification message
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_Info.this,ZouponsConstants.sStoreModuleFlag);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwner_Info.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwner_Info.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mStoreOwner_GeneralInfo_WebSite_Value.setFocusable(true);
		mStoreOwner_GeneralInfo_WebSite_Value.setFocusableInTouchMode(true);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	/* To clear business hour fields */
	public void clearTimingFields(){
		mStoreOwner_BusinessHour_MondayStartTime.getText().clear();
		mStoreOwner_BusinessHour_MondayEndTime.getText().clear();
		mStoreOwner_BusinessHour_TuesdayStartTime.getText().clear();
		mStoreOwner_BusinessHour_TuesdayEndTime.getText().clear();
		mStoreOwner_BusinessHour_WednesdayStartTime.getText().clear();
		mStoreOwner_BusinessHour_WednesdayEndTime.getText().clear();
		mStoreOwner_BusinessHour_ThursdayStartTime.getText().clear();
		mStoreOwner_BusinessHour_ThursdayEndTime.getText().clear();
		mStoreOwner_BusinessHour_FridayStartTime.getText().clear();
		mStoreOwner_BusinessHour_FridayEndTime.getText().clear();
		mStoreOwner_BusinessHour_SaturdayStartTime.getText().clear();
		mStoreOwner_BusinessHour_SaturdayEndTime.getText().clear();
		mStoreOwner_BusinessHour_SundayStartTime.getText().clear();
		mStoreOwner_BusinessHour_SundayEndTime.getText().clear();
		mStoreOwner_BusinessHour_MondayCheckBox.setChecked(false);
		mStoreOwner_BusinessHour_TuesdayCheckBox.setChecked(false);
		mStoreOwner_BusinessHour_WednesdayCheckBox.setChecked(false);
		mStoreOwner_BusinessHour_ThursdayCheckBox.setChecked(false);
		mStoreOwner_BusinessHour_FridayCheckBox.setChecked(false);
		mStoreOwner_BusinessHour_SaturdayCheckBox.setChecked(false);
		mStoreOwner_BusinessHour_SundayCheckBox.setChecked(false);
	}

	// Asynchronous task to load store logo
	public class LoadStoreLogoTask extends AsyncTask<String, String, String>{

		private ImageView mImageView;
		private ProgressBar mProgressView;

		public LoadStoreLogoTask(ImageView imageview, ProgressBar ProgressView) {
			this.mImageView = imageview;
			mProgressView = ProgressView;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressView.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			try{
				mBitmapProfileImage = getBitmapFromURL(params[0]);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				mBitmapProfileImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
				mProfileImage=stream.toByteArray();
				mStoreLogoData=Base64.encodeBytes(mProfileImage);
				return "success";
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "failure";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mProgressView.setVisibility(View.GONE);
			if(mBitmapProfileImage != null){
				mImageView.setVisibility(View.VISIBLE);
				mImageView.setImageBitmap(mBitmapProfileImage);
			}
		}
	}

	// Format Time
	private String convertTime(String time){
		try {
			if(!time.equalsIgnoreCase("")){
				final SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
				Date dateObj = sdf.parse(time);
				return new SimpleDateFormat("H:mm:ss").format(dateObj);	
			}else{
				return "";
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return " ";
		}
	}

	// To get Bitmap from url
	public Bitmap getBitmapFromURL(String src) {
		try {
			Bitmap mBitmap;	
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(src);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(params, 60000); // 1 minute
			request.setParams(params);
			HttpResponse response = httpClient.execute(request);
			mProfileImage = EntityUtils.toByteArray(response.getEntity());
			mBitmap =   BitmapFactory.decodeByteArray(mProfileImage, 0,mProfileImage.length);
			mProfileImage = null;
			return mBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_Info.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

}
