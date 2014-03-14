package com.us.zoupons.storeowner.pointofsale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.zoupons.AmountTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.R;
import com.us.zoupons.android.lib.integrator.IntentIntegrator;
import com.us.zoupons.android.lib.integrator.IntentResult;
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
 * Activity which holds POS functionalities
 *
 */

public class StoreOwner_PointOfSale_Part1 extends Activity {

	// Initializing views and variables
	private String TAG="StoreOwner_PointOfSale_Part1";
	private MyHorizontalScrollView mScrollView;
	private View mApp,mLeftMenu;
	private Header mZouponsheader;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private int mMenuFlag;
	private Button mFreezeView;
	private EditText mAmountValue,mPinValue,mCouponCodeValue;
	private LinearLayout mLeftFooter,mRightFooter;
	private TextView mAddNotesTextView;
	private TextView mLeftFooterText;
	private ImageView mCouponCodeImage;
	static String mQRScannerType="";
	public static String mStoreOwnerNotes="",mAmount="",mCouponCode="";
	//Logout without user interaction after 5 minute
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			// Referencing view from layout xml file
			LayoutInflater inflater = LayoutInflater.from(this);
			mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(mScrollView);
			mApp = inflater.inflate(R.layout.storeowner_pointofsale_part1, null);
			ViewGroup mMiddleView = (ViewGroup) mApp.findViewById(R.id.storeowner_pointofsale_part1_middleview);
			ViewGroup mFooterView = (ViewGroup) mApp.findViewById(R.id.storeowner_pointofsale_part1_footer);
			mFreezeView = (Button) mApp.findViewById(R.id.storeowner_pointofsale_part1_freeze);
			mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreOwner_PointOfSale_Part1.this,mScrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);
			mLeftMenu = mStoreownerLeftmenu.intializeInflater();
			mStoreownerLeftmenu.clickListener(mLeftMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsheader = (Header) mApp.findViewById(R.id.storeowner_pointofsale_part1_header);
			mZouponsheader.intializeInflater(mScrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);
			final View[] children = new View[] { mLeftMenu, mApp};
			/* Scroll to mApp (view[1]) when layout finished.*/
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsheader.mLeftMenuBtnSlide));
			mFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, mMenuFlag, mFreezeView, TAG));
			mNotificationReceiver = new NotifitcationReceiver(mZouponsheader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsheader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsheader,mZouponsheader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsheader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsheader,mZouponsheader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mAmountValue=(EditText) mMiddleView.findViewById(R.id.storeowner_pointofsale_part1_amount_value);
			mAmountValue.setInputType(InputType.TYPE_CLASS_NUMBER);
			mAmountValue.addTextChangedListener(new AmountTextWatcher(mAmountValue));
			mPinValue=(EditText) mMiddleView.findViewById(R.id.storeowner_pointofsale_part1_pin_value);
			mPinValue.setInputType(InputType.TYPE_CLASS_NUMBER);
			mPinValue.setTransformationMethod(PasswordTransformationMethod.getInstance());	
			mCouponCodeValue = (EditText) mMiddleView.findViewById(R.id.couponcode_value);
			mCouponCodeImage = (ImageView) mMiddleView.findViewById(R.id.couponcode_image);
			mAddNotesTextView = (TextView) mMiddleView.findViewById(R.id.pos_add_notes);
			mLeftFooter=(LinearLayout) mFooterView.findViewById(R.id.storeowner_pointofsale_part1_leftFooter);
			mLeftFooterText=(TextView) mFooterView.findViewById(R.id.storeowner_pointofsale_part1_leftFooterText);
			mRightFooter=(LinearLayout) mFooterView.findViewById(R.id.storeowner_pointofsale_part1_rightFooter);

			mAddNotesTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// To show pop up dialog to enter store owner/emlpoyee notes
					final Dialog mNotesDetails = new Dialog(StoreOwner_PointOfSale_Part1.this);
					mNotesDetails.setTitle("Note Details");
					mNotesDetails.setContentView(R.layout.registration_mobiledetails);
					WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
					lWindowParams.copyFrom(mNotesDetails.getWindow().getAttributes());
					lWindowParams.width = WindowManager.LayoutParams.FILL_PARENT;
					lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
					mNotesDetails.getWindow().setAttributes(lWindowParams);
					final EditText mDialogNotesValue = (EditText) mNotesDetails.findViewById(R.id.notes_value);
					mDialogNotesValue.setText(mStoreOwnerNotes); // To set previous note details value
					mDialogNotesValue.setSelection(mStoreOwnerNotes.trim().length());
					Button mOkButton = (Button)mNotesDetails.findViewById(R.id.notes_save_buttonId);
					mNotesDetails.show();
					mOkButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mStoreOwnerNotes = mDialogNotesValue.getText().toString().trim();
							mNotesDetails.dismiss();
						}
					});
				}
			});

			mCouponCodeImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// To open qrcode scanner for scanning Coupon
					mQRScannerType = "CouponBarcode";
					IntentIntegrator.initiateScan(StoreOwner_PointOfSale_Part1.this);
				}
			});

			// Process Payment by Mobile Number
			mLeftFooter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mLeftFooterText.getText().toString().equalsIgnoreCase("By Telephone")){
						if(mAmountValue.getText().toString().trim().length()==0){
							alertBox_service("Information", "Please enter amount to receive");
						}else if(mAmountValue.getText().toString().equalsIgnoreCase("0")){
							alertBox_service("Information", "Please enter valid amount");
						}else if(mPinValue.getText().toString().trim().length()==0){
							alertBox_service("Information", "Please enter pin to proceed the payment");
						}else if(mPinValue.getText().toString().trim().length() < 4){
							alertBox_service("Information", "Please enter four digit PIN to proceed the payment");
						}else{
							mAmount = mAmountValue.getText().toString();
							mCouponCode = mCouponCodeValue.getText().toString();
							//To check employee PIN and proceed payment
							CheckUserPINTask mCheckPIN = new CheckUserPINTask(StoreOwner_PointOfSale_Part1.this,"mobile",mAmountValue.getText().toString(),mCouponCodeValue.getText().toString(),mStoreOwnerNotes);
							mCheckPIN.execute(mPinValue.getText().toString());
						}
					}
				}
			});

			// Process Payment by QR code
			mRightFooter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mAmountValue.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter amount to receive");
					}else if(mAmountValue.getText().toString().equalsIgnoreCase("0")){
						alertBox_service("Information", "Please enter valid amount");
					}else if(mPinValue.getText().toString().trim().length()==0){
						alertBox_service("Information", "Please enter pin to proceed the payment");
					}else if(mPinValue.getText().toString().trim().length()<4){
						alertBox_service("Information", "Please enter four digit PIN to proceed the payment");
					}else{
						mAmount = mAmountValue.getText().toString();
						mCouponCode = mCouponCodeValue.getText().toString();
						//To check employee PIN and proceed payment
						CheckUserPINTask mCheckPIN = new CheckUserPINTask(StoreOwner_PointOfSale_Part1.this,"qr",mAmountValue.getText().toString(),mCouponCodeValue.getText().toString(),mStoreOwnerNotes);
						mCheckPIN.execute(mPinValue.getText().toString());
					}
				}
			});

			mAmountValue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mAmountValue.setSelection(mAmountValue.getText().toString().length());
				}
			});

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_PointOfSale_Part1.this,ZouponsConstants.sStoreModuleFlag);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwner_PointOfSale_Part1.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwner_PointOfSale_Part1.this);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE: //1
			if(resultCode==RESULT_OK){
				IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
				if (scanResult == null) {
					return;
				}
				final String result = scanResult.getContents();
				if(mQRScannerType.equalsIgnoreCase("CouponBarcode")){ // If coupon QR code is scanned
					if(result.contains("-") && result.split("-").length >0){
						mCouponCodeValue.setText(result.split("-")[1]);	
					}
				}else if(mQRScannerType.equalsIgnoreCase("QRCode")){ // If payment QR code is scanned
					SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
					String mLocationId = mPrefs.getString("location_id", "");
					String mUser_Id = mPrefs.getString("user_id", "");
					String mUserType = mPrefs.getString("user_type", "");
					ApprovePaymentUsingQR mPaymentTask = new ApprovePaymentUsingQR(StoreOwner_PointOfSale_Part1.this,mLocationId,mUser_Id,mAmountValue.getText().toString(),mCouponCodeValue.getText().toString(),"qr", result,mStoreOwnerNotes, mUserType);
					mPaymentTask.execute();
				}
			}else if(resultCode==RESULT_CANCELED){}
			break;
		default:
		}
	}

	/* To show alert pop up with respective message */
	private void alertBox_service(String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwner_PointOfSale_Part1.this);
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_PointOfSale_Part1.this);
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