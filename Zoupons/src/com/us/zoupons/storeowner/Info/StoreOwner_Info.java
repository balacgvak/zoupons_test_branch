package com.us.zoupons.storeowner.Info;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.us.zoupons.Settings;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.ClassVariables.POJOStoreTiming;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class StoreOwner_Info extends Activity implements OnClickListener,OnCheckedChangeListener{

	public static String TAG="StoreOwner_Info";
	public static MyHorizontalScrollView scrollView;
	View app;

	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	public int mScreenWidth;
	public double mMenuWidth;
	
	Header header;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;
	View mLeftMenu;
	int mMenuFlag;
	StoreOwner_LeftMenu storeowner_leftmenu;

	Button mStoreOwner_Info_FreezeView;
	ImageView mStoreOwner_Info_RightMenuHolder;
	TextView mStoreOwner_Info_StoreName;
	//TextView mStoreOwner_Info_Footer_AboutStore,mStoreOwner_Info_Footer_BusinessHours;
	LinearLayout mStoreOwner_Info_Footer_AboutStore,mStoreOwner_Info_Footer_BusinessHours,/*mStoreOwner_Info_Footer_Invisible1,*/mStoreOwner_Info_Footer_GeneralInfo;
	Button mStoreOwner_Info_BusinessHours_Save,mStoreOwner_Info_AboutStore_Save;
	

	/*General Info */
	ScrollView mStoreOwner_GeneralInfo_Container;
	private Button mStoreOwner_GeneralInfo_Save;
	private ImageView mStoreOwner_GeneralInfo_ChangeLogo;
	private EditText mStoreOwner_GeneralInfo_WebSite_Value;
	private ProgressBar mProgressBar;
	
	public static int ONACTIVITYRESULTFLAG=0;
	public byte[] mProfileImage;
	public Bitmap mBitmapProfileImage;
	private String mStoreLogoData;
	
	/*About Store*/
	EditText mStoreOwner_Info_AboutStore;
	
	/*Business Hours*/
	EditText mStoreOwner_BusinessHour_MondayStartTime,mStoreOwner_BusinessHour_MondayEndTime,mStoreOwner_BusinessHour_TuesdayStartTime,mStoreOwner_BusinessHour_TuesdayEndTime,mStoreOwner_BusinessHour_WednesdayStartTime,mStoreOwner_BusinessHour_WednesdayEndTime,
	            mStoreOwner_BusinessHour_ThursdayStartTime,mStoreOwner_BusinessHour_ThursdayEndTime,mStoreOwner_BusinessHour_FridayStartTime,mStoreOwner_BusinessHour_FridayEndTime,mStoreOwner_BusinessHour_SaturdayTime,mStoreOwner_BusinessHour_SaturdayEndTime,mStoreOwner_BusinessHour_SundayStartTime,mStoreOwner_BusinessHour_SundayEndTime;
    ImageView mStoreOwner_BusinessHour_MondayStartTime_Dropdown,mStoreOwner_BusinessHour_MondayEndTime_Dropdown,mStoreOwner_BusinessHour_TuesdayStartTime_Dropdown,mStoreOwner_BusinessHour_TuesdayEndTime_Dropdown,mStoreOwner_BusinessHour_WednesdayStartTime_Dropdown,mStoreOwner_BusinessHour_WednesdayEndTime_Dropdown,
    			mStoreOwner_BusinessHour_ThursdayStartTime_Dropdown,mStoreOwner_BusinessHour_ThursdayEndTime_Dropdown,mStoreOwner_BusinessHour_FridayStartTime_Dropdown,mStoreOwner_BusinessHour_FridayEndTime_Dropdown,mStoreOwner_BusinessHour_SaturdayTime_Dropdown,mStoreOwner_BusinessHour_SaturdayEndTime_Dropdown,mStoreOwner_BusinessHour_SundayStartTime_Dropdown,mStoreOwner_BusinessHour_SundayEndTime_Dropdown;
	CheckBox mStoreOwner_BusinessHour_MondayCheckBox,mStoreOwner_BusinessHour_TuesdayCheckBox,mStoreOwner_BusinessHour_WednesdayCheckBox,mStoreOwner_BusinessHour_ThursdayCheckBox,
    			mStoreOwner_BusinessHour_FridayCheckBox,mStoreOwner_BusinessHour_SaturdayCheckBox,mStoreOwner_BusinessHour_SundayCheckBox;
    String is_Monday_Closed="0",is_Tuesday_Closed="0",is_Wednesday_Closed="0",is_Thursday_Closed="0",is_Friday_Closed="0",is_Saturday_Closed="0",is_Sunday_Closed="0";
    
    //Logout without user interaction after 1 minute
  	CheckLogoutSession mLogoutSession;
  	private ScheduleNotificationSync mNotificationSync;
  	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			LayoutInflater inflater = LayoutInflater.from(this);
			scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(scrollView);
			mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
			mConnectionAvailabilityChecking = new NetworkCheck();
			app = inflater.inflate(R.layout.storeowner_info, null);
			final ViewGroup mInfoBusinessHours = (ViewGroup) app.findViewById(R.id.storeownerinfo_container);
			final ViewGroup mInfoAboutStore = (ViewGroup) app.findViewById(R.id.businesshours_aboutstore_container);
			final ViewGroup mInfoStoreNameHeader = (ViewGroup) app.findViewById(R.id.storeownerinfo_storename_header);
			final ViewGroup mInfoBusinessHoursHeader = (ViewGroup) app.findViewById(R.id.storeownerinfo_businesshours_header);
			final ViewGroup mInfoFooter = (ViewGroup) app.findViewById(R.id.storeownerinfo_footer);

			mStoreOwner_Info_FreezeView = (Button) app.findViewById(R.id.storeowner_info_freeze);

			// General info
			mStoreOwner_GeneralInfo_Container = (ScrollView) app.findViewById(R.id.storeownergeneralinfo_middleview);
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
			mStoreOwner_BusinessHour_SaturdayTime = (EditText) mInfoBusinessHours.findViewById(R.id.businesshour_saturday_open_value);
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
			// set click Listner
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
			mStoreOwner_BusinessHour_SaturdayTime.setOnClickListener(this);
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

			storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwner_Info.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_Info_FreezeView, TAG);
			mRightMenu = storeowner_rightmenu.intializeInflater();
			storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_Info.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Info_FreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();

			storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);

			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeownerinfo_header);
			header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Info_FreezeView, TAG);
			final View[] children = new View[] { mLeftMenu, app, mRightMenu };

			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));

			mStoreOwner_Info_RightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwner_Info_FreezeView, TAG,mStoreOwner_GeneralInfo_WebSite_Value,mStoreOwner_Info_AboutStore,false));
			mStoreOwner_Info_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_Info_FreezeView, TAG,mStoreOwner_GeneralInfo_WebSite_Value,mStoreOwner_Info_AboutStore,true));

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
					if(is_Monday_Closed.equalsIgnoreCase("0") && mStoreOwner_BusinessHour_MondayStartTime.getText().toString().length() == 0 && mStoreOwner_BusinessHour_MondayEndTime.getText().toString().length() == 0){
						alertBox_service("Information", "Please choose monday business hour details");
					}else if(is_Tuesday_Closed.equalsIgnoreCase("0") && mStoreOwner_BusinessHour_TuesdayStartTime.getText().toString().length() == 0 && mStoreOwner_BusinessHour_TuesdayEndTime.getText().toString().length() == 0){
						alertBox_service("Information", "Please choose tuesday business hour details");
					}else if(is_Wednesday_Closed.equalsIgnoreCase("0") && mStoreOwner_BusinessHour_WednesdayStartTime.getText().toString().length() == 0 && mStoreOwner_BusinessHour_WednesdayEndTime.getText().toString().length() == 0){
						alertBox_service("Information", "Please choose wednesday business hour details");
					}else if(is_Thursday_Closed.equalsIgnoreCase("0") && mStoreOwner_BusinessHour_ThursdayStartTime.getText().toString().length() == 0 && mStoreOwner_BusinessHour_ThursdayEndTime.getText().toString().length() == 0){
						alertBox_service("Information", "Please choose thursday business hour details");
					}else if(is_Friday_Closed.equalsIgnoreCase("0") && mStoreOwner_BusinessHour_FridayStartTime.getText().toString().length() == 0 && mStoreOwner_BusinessHour_FridayEndTime.getText().toString().length() == 0){
						alertBox_service("Information", "Please choose friday business hour details");
					}else if(is_Saturday_Closed.equalsIgnoreCase("0")&& mStoreOwner_BusinessHour_SaturdayTime.getText().toString().length() == 0 && mStoreOwner_BusinessHour_SaturdayEndTime.getText().toString().length() == 0){
						alertBox_service("Information", "Please choose saturday business hour details");
					}else if(is_Sunday_Closed.equalsIgnoreCase("0") && mStoreOwner_BusinessHour_SundayStartTime.getText().toString().length() == 0 && mStoreOwner_BusinessHour_SundayEndTime.getText().toString().length() == 0){
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
									convertTime(mStoreOwner_BusinessHour_SaturdayTime.getText().toString()),
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
		if(v.equals(mStoreOwner_BusinessHour_MondayStartTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_MondayStartTime,mStoreOwner_BusinessHour_MondayStartTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_MondayEndTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_MondayEndTime,mStoreOwner_BusinessHour_MondayEndTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_TuesdayStartTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_TuesdayStartTime,mStoreOwner_BusinessHour_TuesdayStartTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_TuesdayEndTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_TuesdayEndTime,mStoreOwner_BusinessHour_TuesdayEndTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_WednesdayStartTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_WednesdayStartTime,mStoreOwner_BusinessHour_WednesdayStartTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_WednesdayEndTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_WednesdayEndTime,mStoreOwner_BusinessHour_WednesdayEndTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_ThursdayStartTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_ThursdayStartTime,mStoreOwner_BusinessHour_ThursdayStartTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_ThursdayEndTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_ThursdayEndTime,mStoreOwner_BusinessHour_ThursdayEndTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_FridayStartTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_FridayStartTime,mStoreOwner_BusinessHour_FridayStartTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_FridayEndTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_FridayEndTime,mStoreOwner_BusinessHour_FridayEndTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_SaturdayTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_SaturdayTime,mStoreOwner_BusinessHour_SaturdayTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_SaturdayEndTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_SaturdayEndTime,mStoreOwner_BusinessHour_SaturdayEndTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_SundayStartTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_SundayStartTime,mStoreOwner_BusinessHour_SundayStartTime.getText().toString());
	   }else if(v.equals(mStoreOwner_BusinessHour_SundayEndTime_Dropdown)){
		   openTimePickerDialog(mStoreOwner_BusinessHour_SundayEndTime,mStoreOwner_BusinessHour_SundayEndTime.getText().toString());
	   }else{ // For all editTexts....
		   openTimePickerDialog((EditText)v,((EditText)v).getText().toString());
	   }
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		ManageCheckBoxClick(buttonView, isChecked);
	}
	
	public void ManageCheckBoxClick(View buttonView , boolean isChecked){
		if(buttonView.equals(mStoreOwner_BusinessHour_MondayCheckBox)){
			
			is_Monday_Closed = (isChecked)?"1":"0"; 
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_MondayStartTime, mStoreOwner_BusinessHour_MondayEndTime, mStoreOwner_BusinessHour_MondayStartTime_Dropdown, mStoreOwner_BusinessHour_MondayEndTime_Dropdown, isChecked);
		}else if(buttonView.equals(mStoreOwner_BusinessHour_TuesdayCheckBox)){
			is_Tuesday_Closed = (isChecked)?"1":"0";
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_TuesdayStartTime, mStoreOwner_BusinessHour_TuesdayEndTime, mStoreOwner_BusinessHour_TuesdayStartTime_Dropdown, mStoreOwner_BusinessHour_TuesdayEndTime_Dropdown, isChecked);
		}else if(buttonView.equals(mStoreOwner_BusinessHour_WednesdayCheckBox)){
			is_Wednesday_Closed = (isChecked)?"1":"0";
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_WednesdayStartTime, mStoreOwner_BusinessHour_WednesdayEndTime, mStoreOwner_BusinessHour_WednesdayStartTime_Dropdown, mStoreOwner_BusinessHour_WednesdayEndTime_Dropdown, isChecked);
		}else if(buttonView.equals(mStoreOwner_BusinessHour_ThursdayCheckBox)){
			is_Thursday_Closed = (isChecked)?"1":"0";
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_ThursdayStartTime, mStoreOwner_BusinessHour_ThursdayEndTime, mStoreOwner_BusinessHour_ThursdayStartTime_Dropdown, mStoreOwner_BusinessHour_ThursdayEndTime_Dropdown, isChecked);
		}else if(buttonView.equals(mStoreOwner_BusinessHour_FridayCheckBox)){
			is_Friday_Closed = (isChecked)?"1":"0";
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_FridayStartTime, mStoreOwner_BusinessHour_FridayEndTime, mStoreOwner_BusinessHour_FridayStartTime_Dropdown, mStoreOwner_BusinessHour_FridayEndTime_Dropdown, isChecked);
		}else if(buttonView.equals(mStoreOwner_BusinessHour_SaturdayCheckBox)){
			is_Saturday_Closed = (isChecked)?"1":"0";
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_SaturdayTime, mStoreOwner_BusinessHour_SaturdayEndTime, mStoreOwner_BusinessHour_SaturdayTime_Dropdown, mStoreOwner_BusinessHour_SaturdayEndTime_Dropdown, isChecked);
		}else if(buttonView.equals(mStoreOwner_BusinessHour_SundayCheckBox)){
			is_Sunday_Closed = (isChecked)?"1":"0";
			clearTimeFieldsIfClosed(mStoreOwner_BusinessHour_SundayStartTime, mStoreOwner_BusinessHour_SundayEndTime, mStoreOwner_BusinessHour_SundayStartTime_Dropdown, mStoreOwner_BusinessHour_SundayEndTime_Dropdown, isChecked);
		}
	}
	
	public void clearTimeFieldsIfClosed(EditText mStartTime,EditText mEndTime,ImageView startTimeDropdown,ImageView endTimeDropdown,boolean isClosed){
	   	if(isClosed){
	   		mStartTime.getText().clear();
	   		mEndTime.getText().clear();
	   		mStartTime.setEnabled(false);
	   		mEndTime.setEnabled(false);
	   		startTimeDropdown.setEnabled(false);
	   		endTimeDropdown.setEnabled(false);
	   	}else{
	   		mStartTime.setEnabled(true);
	   		mEndTime.setEnabled(true);
	   		startTimeDropdown.setEnabled(true);
	   		endTimeDropdown.setEnabled(true);
	   	}
	}

	
	public void updateViews(ArrayList<Object> result){
		POJOStoreInfo mStoreDetails = (POJOStoreInfo) result.get(0);
		mStoreOwner_GeneralInfo_WebSite_Value.setText(mStoreDetails.website);
		LoadStoreLogoTask loadStorelogo = new LoadStoreLogoTask(mStoreOwner_GeneralInfo_ChangeLogo,mProgressBar);
		loadStorelogo.execute(mStoreDetails.logo_path+"&w=250&h=150&zc=0");
		mStoreOwner_Info_StoreName.setText(mStoreDetails.store_name);
		mStoreOwner_Info_AboutStore.setText(mStoreDetails.description);   	    	
	}
	
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
		mStoreOwner_BusinessHour_SaturdayTime.setText(mStoreTimingDetails.saturday_from);
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
		ManageCheckBoxClick(mStoreOwner_BusinessHour_MondayCheckBox,(mStoreTimingDetails.is_monday_closed.equalsIgnoreCase("1"))?true:false);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_TuesdayCheckBox,(mStoreTimingDetails.is_tuesday_closed.equalsIgnoreCase("1"))?true:false);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_WednesdayCheckBox,(mStoreTimingDetails.is_wednesday_closed.equalsIgnoreCase("1"))?true:false);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_ThursdayCheckBox,(mStoreTimingDetails.is_thursday_closed.equalsIgnoreCase("1"))?true:false);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_FridayCheckBox,(mStoreTimingDetails.is_friday_closed.equalsIgnoreCase("1"))?true:false);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_SaturdayCheckBox,(mStoreTimingDetails.is_saturday_closed.equalsIgnoreCase("1"))?true:false);
		ManageCheckBoxClick(mStoreOwner_BusinessHour_SundayCheckBox,(mStoreTimingDetails.is_sunday_closed.equalsIgnoreCase("1"))?true:false);
	}
	
	private void ManageCheckBox(CheckBox checkbox,String is_closed) {
		if(is_closed.equalsIgnoreCase("0")){
			checkbox.setChecked(false);
		}else{
			checkbox.setChecked(true);
		}
		
	}
	
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
			if(item.getItemId() == 0){
				ONACTIVITYRESULTFLAG=1;
				Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent_camera, 1);
			}else if(item.getItemId() == 1){
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
	
	public void setImageBitmap(Intent data){
		try{
			Uri uri=data.getData();
			Log.i("uri"," "+uri);
			// specifying column(data) for retrieval
			String[] file_path_columns={MediaStore.Images.Media.DATA};
			// querying content provider to get particular image
			Cursor cursor=getContentResolver().query(uri, file_path_columns, null, null, null);
			cursor.moveToFirst();
			// getting col_index from string file_path_columns[0]--> Data column 
			int column_index=cursor.getColumnIndex(file_path_columns[0]);
			// getting the path from result as /sdcard/DCIM/100ANDRO/file_name
			String selected_file_path=cursor.getString(column_index);
			Log.i("selected path"," "+selected_file_path);	
			cursor.close();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(selected_file_path, options);
			// Calculate inSampleSize
			options.inSampleSize = Settings.calculateInSampleSize(options, getApplicationContext().getResources().getDimensionPixelSize(R.dimen.generalinfo_storelogo_width), getApplicationContext().getResources().getDimensionPixelSize(R.dimen.generalinfo_storelogo_width));
			Log.i("sample size", options.inSampleSize+" ");
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap mSelectedImage = BitmapFactory.decodeFile(selected_file_path, options);
			mSelectedImage = new DecodeImageWithRotation().decodeImage(selected_file_path, mSelectedImage, getApplicationContext().getResources().getDimensionPixelSize(R.dimen.generalinfo_storelogo_width), getApplicationContext().getResources().getDimensionPixelSize(R.dimen.generalinfo_storelogo_height));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);    
			byte[] imagedata = baos.toByteArray();
			mStoreLogoData = com.us.zoupons.Base64.encodeBytes(imagedata);
			Bitmap logo=Bitmap.createScaledBitmap(mSelectedImage, getApplicationContext().getResources().getDimensionPixelSize(R.dimen.generalinfo_storelogo_width), getApplicationContext().getResources().getDimensionPixelSize(R.dimen.generalinfo_storelogo_height), true);
			mStoreOwner_GeneralInfo_ChangeLogo.setImageBitmap(logo);
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Please select valid image.", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	public void openTimePickerDialog(final EditText editText,String previousTime){
		
		int hour=0,minute=0;
		if(!previousTime.equalsIgnoreCase("")){
			String convertedTime  = convertTime(previousTime);
			hour = Integer.parseInt(convertedTime.split(":")[0]);
			minute = Integer.parseInt(convertedTime.split(":")[1]);
		}else{
			hour = 9;
			minute = 0;
		}
		TimePickerDialog mTimePicker = new TimePickerDialog(StoreOwner_Info.this,new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				updateTime(hourOfDay, minute, editText);
			}
		},hour, minute,false);
		mTimePicker.show();
	}
	

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
	
	/*Get Screen width*/
	public int getScreenWidth(){
		int Measuredwidth = 0;  
		int Measuredheight = 0;  
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Measuredwidth = display.getWidth(); 
		Measuredheight = display.getHeight();
		return Measuredwidth;
	}
	
	@Override
	protected void onDestroy() {
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
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_Info.this);
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
		mStoreOwner_BusinessHour_SaturdayTime.getText().clear();
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
	
	public class LoadStoreLogoTask extends AsyncTask<String, String, String>{

		private Bitmap mStoreLogoBitmap;
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
			Log.i("image view height and width",mImageView.getWidth() +" "+mImageView.getHeight());
		}

		@Override
		protected String doInBackground(String... params) {
			try{
			mStoreLogoBitmap = getBitmapFromURL(params[0]);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			mStoreLogoBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
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
			if(mStoreLogoBitmap != null){
				mImageView.setVisibility(View.VISIBLE);
				mImageView.setImageBitmap(mStoreLogoBitmap);
			}else{
			}
		}
	}
		
	private String convertTime(String time){
		try {
			if(!time.equalsIgnoreCase("")){
    			final SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
				Date dateObj = sdf.parse(time);
				Log.i("converted time", new SimpleDateFormat("H:mm:ss").format(dateObj));
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
	public Bitmap getBitmapFromURL(String src) {
		try {
			Log.i(TAG,"URL : "+src);
			Bitmap mBitmap;	
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(src);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(params, 60000); // 1 minute
			request.setParams(params);
			HttpResponse response = httpClient.execute(request);
			byte[] image = EntityUtils.toByteArray(response.getEntity());
			mBitmap =   BitmapFactory.decodeByteArray(image, 0,image.length);
			image = null;
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
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_Info.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}
	
	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			try{
				Log.i(TAG,"OnReceive");
				if(intent.hasExtra("FromNotification")){
					if(NotificationDetails.notificationcount>0){
						header.mTabBarNotificationCountBtn.setVisibility(View.VISIBLE);
						header.mTabBarNotificationCountBtn.setText(String.valueOf(NotificationDetails.notificationcount));
					}else{
						header.mTabBarNotificationCountBtn.setVisibility(View.GONE);
						Log.i("Notification result", "No new notification");
					}
				}					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};
}
