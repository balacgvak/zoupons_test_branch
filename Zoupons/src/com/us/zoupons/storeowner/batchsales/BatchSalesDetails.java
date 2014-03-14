package com.us.zoupons.storeowner.batchsales;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

/**
 * 
 * Activity which displays batch sales of store location
 *
 */

public class BatchSalesDetails extends Activity implements OnClickListener{

	// Initializing views and variables
	private String TAG="StoreOwnerBatchSales";
	private MyHorizontalScrollView scrollView;
	private NetworkCheck mConnectionAvailabilityChecking;
	private View mApp;
	private Header mZouponsHeader;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private View mLeftMenu;
	private int mMenuFlag;
	private Button mStoreOwnerBatchSaleFreezeView;
	private ImageView mStartDateContextImage,mEndDateContextImage;
	private EditText mStartDate,mEndDate;
	private ListView mBatchSalesList;
	private TextView mTotalAmountText,mTotalTipText,mTotalZouponsFeeText,mTotalNetAmountText,mSortByDateMenu,mSortByEmployeeMenu,mSendemailReportMenu;
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private String mSelectedFromDate="",mSelectedToDate="",mEventFlag="list";
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Referencing view from layout file
		LayoutInflater inflater = LayoutInflater.from(this);
		scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
		setContentView(scrollView);
		mConnectionAvailabilityChecking = new NetworkCheck();
		mApp = inflater.inflate(R.layout.storeowner_batchsales, null);
		mStoreOwnerBatchSaleFreezeView = (Button) mApp.findViewById(R.id.storeowner_batchsale_freezeview);
		mStoreownerLeftmenu = new StoreOwner_LeftMenu(BatchSalesDetails.this,scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerBatchSaleFreezeView, TAG);
		mLeftMenu = mStoreownerLeftmenu.intializeInflater();
		mStoreownerLeftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
		/* Header Tab Bar which contains logout,notification and home buttons*/
		mZouponsHeader = (Header) mApp.findViewById(R.id.storeowner_batchsales_header);
		mZouponsHeader.intializeInflater(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=1, mStoreOwnerBatchSaleFreezeView, TAG);
		final View[] children = new View[] { mLeftMenu, mApp};
		/* Scroll to app (view[1]) when layout finished.*/
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsHeader.mLeftMenuBtnSlide));
		mStoreOwnerBatchSaleFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerBatchSaleFreezeView, TAG));
		// Instantiating views and view groups
		// Date fields
		mStartDate = (EditText) findViewById(R.id.batch_sales_fromdate_value);
		mEndDate = (EditText) findViewById(R.id.batch_sales_enddate_value);
		mStartDateContextImage = (ImageView) findViewById(R.id.batch_sales_fromdate_contextImage);
		mEndDateContextImage = (ImageView) findViewById(R.id.batch_sales_enddate_contextImage);
		// Batchsales list
		mBatchSalesList = (ListView) findViewById(R.id.batch_sales_list);
		// Total Amount details views
		mTotalAmountText = (TextView) findViewById(R.id.batchsales_total_amount_textId);
		mTotalTipText = (TextView) findViewById(R.id.batchsales_total_tip_textId);
		mTotalZouponsFeeText = (TextView) findViewById(R.id.batchsales_total_zouponsFee_textId);
		mTotalNetAmountText = (TextView) findViewById(R.id.batchsales_total_netamount_textId);
		// Footer menu views
		mSortByDateMenu = (TextView) findViewById(R.id.storeowner_batchsales_datebased_menu);
		mSortByEmployeeMenu = (TextView) findViewById(R.id.storeowner_batchsales_employeebased_menu);
		mSendemailReportMenu = (TextView) findViewById(R.id.storeowner_batchsales_sendemailreport_menu);
		// To set Default dates in Start and End fields
		setDefaultBatchSalesListDate();
		mSortByDateMenu.setBackgroundResource(R.drawable.footer_dark_blue_new);
		// Call webservice to fetch batchsales details
		if(mConnectionAvailabilityChecking.ConnectivityCheck(this)){ // Network Connectivity check 
			GetBatchSalesTask mGetBatchSalesDetails = new GetBatchSalesTask(BatchSalesDetails.this,mEventFlag,mSelectedFromDate,mSelectedToDate,mTotalAmountText,mTotalTipText,mTotalZouponsFeeText,mTotalNetAmountText,mBatchSalesList);
			mGetBatchSalesDetails.execute();
		}else{
			Toast.makeText(BatchSalesDetails.this,"No Network connection", Toast.LENGTH_SHORT).show();
		}
		// Setting tag object and registering for click listner
		mStartDate.setTag("FromDate");
		mEndDate.setTag("ToDate");
		mStartDateContextImage.setTag("FromDate");
		mEndDateContextImage.setTag("ToDate");
		mStartDate.setOnClickListener(this);
		mEndDate.setOnClickListener(this);
		mStartDateContextImage.setOnClickListener(this);
		mEndDateContextImage.setOnClickListener(this);
		
		mNotificationReceiver = new NotifitcationReceiver(mZouponsHeader.mTabBarNotificationCountBtn);
		// Notitification pop up layout declaration
		mZouponsHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
		mZouponsHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
		
		mSortByDateMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!mEventFlag.equalsIgnoreCase("list")){
					v.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mSendemailReportMenu.setBackgroundResource(R.drawable.header_2);
					mSortByEmployeeMenu.setBackgroundResource(R.drawable.header_2);
					setDefaultBatchSalesListDate();
					mEventFlag = "list";
					clearTotalTransactionAmountFields();
					// To clear list
					mBatchSalesList.setAdapter(new CustomBatchSalesAdapter(BatchSalesDetails.this,new ArrayList<Object>(),mEventFlag));
					if(mConnectionAvailabilityChecking.ConnectivityCheck(BatchSalesDetails.this)){ // Network Connectivity check 
						GetBatchSalesTask mGetBatchSalesDetails = new GetBatchSalesTask(BatchSalesDetails.this,mEventFlag,mSelectedFromDate,mSelectedToDate,mTotalAmountText,mTotalTipText,mTotalZouponsFeeText,mTotalNetAmountText,mBatchSalesList);
						mGetBatchSalesDetails.execute();
					}else{
						Toast.makeText(BatchSalesDetails.this,"No Network connection", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		mSendemailReportMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(mBatchSalesList.getAdapter() == null || mBatchSalesList.getAdapter().getCount() == 0){
					AlertDialog.Builder mAlertBox = new AlertDialog.Builder(BatchSalesDetails.this);
					mAlertBox.setTitle("Information");
					mAlertBox.setMessage("Reports not available.");
					mAlertBox.setPositiveButton("ok", null);
					mAlertBox.show();
				}else{
					if(mConnectionAvailabilityChecking.ConnectivityCheck(BatchSalesDetails.this)){ // Network Connectivity check
						SendEmailSalesReportTask mSendEmailSalesReport = new SendEmailSalesReportTask(BatchSalesDetails.this,mEventFlag,mSelectedFromDate,mSelectedToDate);
						mSendEmailSalesReport.execute();
					}else{
						Toast.makeText(BatchSalesDetails.this,"No Network connection", Toast.LENGTH_SHORT).show();
					}	
				}
			}
		});

		mSortByEmployeeMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!mEventFlag.equalsIgnoreCase("employee")){
					v.setBackgroundResource(R.drawable.footer_dark_blue_new);
					mSortByDateMenu.setBackgroundResource(R.drawable.header_2);
					mSendemailReportMenu.setBackgroundResource(R.drawable.header_2);
					clearTotalTransactionAmountFields();
					setDefaultBatchSalesListDate();
					mEventFlag = "employee";
					// To clear list
					mBatchSalesList.setAdapter(new CustomBatchSalesAdapter(BatchSalesDetails.this,new ArrayList<Object>(),mEventFlag));
					if(mConnectionAvailabilityChecking.ConnectivityCheck(BatchSalesDetails.this)){ // Network Connectivity check 
						GetBatchSalesTask mGetBatchSalesDetails = new GetBatchSalesTask(BatchSalesDetails.this,mEventFlag,mSelectedFromDate,mSelectedToDate,mTotalAmountText,mTotalTipText,mTotalZouponsFeeText,mTotalNetAmountText,mBatchSalesList);
						mGetBatchSalesDetails.execute();
					}else{
						Toast.makeText(BatchSalesDetails.this,"No Network connection", Toast.LENGTH_SHORT).show();
					}
				}
			}

			
		});

	}

	private void clearTotalTransactionAmountFields() {
		// TODO Auto-generated method stub
		mTotalAmountText.setText("$0.00");
		mTotalTipText.setText("$0.00");
		mTotalZouponsFeeText.setText("$0.00");
		mTotalNetAmountText.setText("$0.00");
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	// To set One Month difference in TO and FROM Date as Default
	public void setDefaultBatchSalesListDate(){
		// Setting default values to from date and To date
		Calendar originalDateCalendar = Calendar.getInstance();
		mSelectedFromDate = mSelectedToDate = originalDateCalendar.get(Calendar.YEAR)+"-"+String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"-"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH));
		mStartDate.setText(String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"/"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH))+"/"+originalDateCalendar.get(Calendar.YEAR));
		mEndDate.setText(String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"/"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH))+"/"+originalDateCalendar.get(Calendar.YEAR));
	}

	// To Open Date picker view
	public void openDatePicker(final String dateType){
		Calendar mChoosedCalander;
		if(dateType.equalsIgnoreCase("FromDate")){  // From date
			mChoosedCalander = Calendar.getInstance();
			mChoosedCalander.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mStartDate.getText().toString().split("/")[1]));
			mChoosedCalander.set(Calendar.MONTH, Integer.parseInt(mStartDate.getText().toString().split("/")[0])-1);
			mChoosedCalander.set(Calendar.YEAR, Integer.parseInt(mStartDate.getText().toString().split("/")[2]));
		}else if(mEndDate.getText().toString().trim().length()>0){ // To date 
			mChoosedCalander = Calendar.getInstance();
			mChoosedCalander.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mEndDate.getText().toString().split("/")[1]));
			mChoosedCalander.set(Calendar.MONTH, Integer.parseInt(mEndDate.getText().toString().split("/")[0])-1);
			mChoosedCalander.set(Calendar.YEAR, Integer.parseInt(mEndDate.getText().toString().split("/")[2]));
		}else{ // Empty To date
			mChoosedCalander = Calendar.getInstance();
		}
		DatePickerDialog mDatePicker = new DatePickerDialog(BatchSalesDetails.this,new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

				if(dateType.equalsIgnoreCase("FromDate")){ // set selected date in From date field
					Date mSelectedDate = new Date(year, monthOfYear, dayOfMonth);
					if(!isFutureDateSelected(year, monthOfYear, dayOfMonth)){
						mStartDate.setText(String.format("%02d",(mSelectedDate.getMonth()+1))+"/"+String.format("%02d",mSelectedDate.getDate())+"/"+mSelectedDate.getYear());
						mEndDate.getText().clear();
						mSelectedFromDate = mSelectedDate.getYear()+"-"+String.format("%02d",(mSelectedDate.getMonth()+1))+"-"+String.format("%02d",mSelectedDate.getDate());
						clearTotalTransactionAmountFields();
						// To clear list
						mBatchSalesList.setAdapter(new CustomBatchSalesAdapter(BatchSalesDetails.this,new ArrayList<Object>(),mEventFlag));
						Calendar originalDateCalendar = Calendar.getInstance();
						mSelectedToDate = originalDateCalendar.get(Calendar.YEAR)+"-"+String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"-"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH));
						mEndDate.setText(String.format("%02d",(originalDateCalendar.get(Calendar.MONTH)+1))+"/"+String.format("%02d",originalDateCalendar.get(Calendar.DAY_OF_MONTH))+"/"+originalDateCalendar.get(Calendar.YEAR));
						if(new NetworkCheck().ConnectivityCheck(BatchSalesDetails.this)){
							// call webservice
							GetBatchSalesTask mGetBatchSalesDetails = new GetBatchSalesTask(BatchSalesDetails.this,mEventFlag,mSelectedFromDate,mSelectedToDate,mTotalAmountText,mTotalTipText,mTotalZouponsFeeText,mTotalNetAmountText,mBatchSalesList);
							mGetBatchSalesDetails.execute();
						}else{
							Toast.makeText(BatchSalesDetails.this, "Network connection not available", Toast.LENGTH_SHORT).show();
						}
					}else{
						AlertDialog.Builder mAlertBox = new AlertDialog.Builder(BatchSalesDetails.this);
						mAlertBox.setTitle("Information");
						mAlertBox.setMessage("Please choose previous date");
						mAlertBox.setPositiveButton("ok", null);
						mAlertBox.show();
					}
				}else{  // set selected date in To date field
					if(mStartDate.getText().toString().length()>0){
						if(isFutureDateSelected(year, monthOfYear, dayOfMonth)){
							AlertDialog.Builder mAlertBox = new AlertDialog.Builder(BatchSalesDetails.this);
							mAlertBox.setTitle("Information");
							mAlertBox.setMessage("Please choose previous date");
							mAlertBox.setPositiveButton("ok", null);
							mAlertBox.show();	
						}else{
							String mFromDate = mStartDate.getText().toString();
							int mDate = Integer.parseInt(mFromDate.split("/")[1]); 
							int mMonth = Integer.parseInt(mFromDate.split("/")[0]);
							int mYear = Integer.parseInt(mFromDate.split("/")[2]);	
							Date mSelectedFromDateObject = new Date(mYear,mMonth-1,mDate);
							Date mSelectedDate = new Date(year, monthOfYear, dayOfMonth);
							if(mSelectedFromDateObject.compareTo(mSelectedDate) <= 0){
								mEndDate.setText(String.format("%02d",(mSelectedDate.getMonth()+1))+"/"+String.format("%02d",mSelectedDate.getDate())+"/"+mSelectedDate.getYear());
								mSelectedToDate = mSelectedDate.getYear()+"-"+String.format("%02d",(mSelectedDate.getMonth()+1))+"-"+String.format("%02d",mSelectedDate.getDate());
								if(new NetworkCheck().ConnectivityCheck(BatchSalesDetails.this)){
									// call webservice
									GetBatchSalesTask mGetBatchSalesDetails = new GetBatchSalesTask(BatchSalesDetails.this,mEventFlag,mSelectedFromDate,mSelectedToDate,mTotalAmountText,mTotalTipText,mTotalZouponsFeeText,mTotalNetAmountText,mBatchSalesList);
									mGetBatchSalesDetails.execute();
								}else{
									Toast.makeText(BatchSalesDetails.this, "Network connection not available", Toast.LENGTH_SHORT).show();
								}
							}else{
								AlertDialog.Builder mAlertBox = new AlertDialog.Builder(BatchSalesDetails.this);
								mAlertBox.setTitle("Information");
								mAlertBox.setMessage("Please choose date greater than From date");
								mAlertBox.setPositiveButton("ok", null);
								mAlertBox.show();
							}
						}
					}else{
						AlertDialog.Builder mAlertBox = new AlertDialog.Builder(BatchSalesDetails.this);
						mAlertBox.setTitle("Information");
						mAlertBox.setMessage("Please choose From date first");
						mAlertBox.setPositiveButton("ok", null);
						mAlertBox.show();
					}
				}	
			}
		}, mChoosedCalander.get(Calendar.YEAR), mChoosedCalander.get(Calendar.MONTH), mChoosedCalander.get(Calendar.DAY_OF_MONTH));
		mDatePicker.show();
	}
	
	private boolean isFutureDateSelected(int year,int month,int day){
		Calendar mCurrentTime = Calendar.getInstance();
		int mDate = mCurrentTime.get(Calendar.DAY_OF_MONTH);
		int mMonth = mCurrentTime.get(Calendar.MONTH);
		int mYear = mCurrentTime.get(Calendar.YEAR);	
		Date mCurrentDate = new Date(mYear,mMonth,mDate);
		Date mSelectedDate = new Date(year, month, day);
		if(mCurrentDate.compareTo(mSelectedDate) >= 0){ // Not future date
			return false;
		}else{ // Future date
			return true;	
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// To notify system that its time to run garbage collector service
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
		// TODO Auto-generated method stub
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(BatchSalesDetails.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mNotificationReceiver, new IntentFilter(BroadCastActionClassVariables.ACTION));
		new CheckUserSession(BatchSalesDetails.this).checkIfSessionExpires();
		// To start notification sync
		mNotificationSync = new ScheduleNotificationSync(BatchSalesDetails.this,ZouponsConstants.sStoreModuleFlag);
		mNotificationSync.setRecurringAlarm();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(BatchSalesDetails.this);
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		openDatePicker(v.getTag().toString());
	}

}


