package com.us.zoupons.storeowner.PointOfSale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.us.zoupons.AmountTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.android.lib.integrator.IntentIntegrator;
import com.us.zoupons.android.lib.integrator.IntentResult;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

/**
 * Class to have Enter Amount page and Enter Cards to Process page
 * **/
public class StoreOwner_PointOfSale_Part1 extends Activity {

	public static String TAG="StoreOwner_PointOfSale_Part1";

	public static MyHorizontalScrollView scrollView;
	View app;
	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	Header header;
	StoreOwner_LeftMenu storeowner_leftmenu;
	View mLeftMenu;
	int mMenuFlag;

	private Button mFreezeView,mAmountSubmit,mAddCard;
	private EditText mAmountValue,mPinValue,mCouponCodeValue;
	private LinearLayout mLeftFooter,mSplitter1,mSplitter2,mSplitter3,mInvisible1,mInvisible2,mRightFooter;
	private ListView mListView;
	private TextView mAmountValueText,mAmountRemainingValueText,mTipAmountValueText,mTipPercentageText,mTotalAmountValueText,mAddNotesTextView;
	private TextView mLeftFooterText,mRightFooterText;
	private ImageView mCouponCodeImage;
	static String mQRScannerType="";
	public static String mStoreOwnerNotes="",mAmount="",mCouponCode="";
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
			app = inflater.inflate(R.layout.storeowner_pointofsale_part1, null);

			ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.storeowner_pointofsale_part1_middleview);
			ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeowner_pointofsale_part1_footer);
			final ViewGroup mAmountContainer = (ViewGroup) app.findViewById(R.id.storeowner_pointofsale_part1_amountcontainer);
			final ViewGroup mEnterCardContainer = (ViewGroup) app.findViewById(R.id.storeowner_pointofsale_part1_entercardcontainer);

			mFreezeView = (Button) app.findViewById(R.id.storeowner_pointofsale_part1_freeze);
			storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_PointOfSale_Part1.this,scrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();
			storeowner_leftmenu.clickListener(mLeftMenu);

			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeowner_pointofsale_part1_header);
			header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);

			final View[] children = new View[] { mLeftMenu, app};
			/* Scroll to app (view[1]) when layout finished.*/
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));

			mFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag, mFreezeView, TAG));

			mAmountValue=(EditText) mMiddleView.findViewById(R.id.storeowner_pointofsale_part1_amount_value);
			mAmountValue.setInputType(InputType.TYPE_CLASS_NUMBER);
			mAmountValue.addTextChangedListener(new AmountTextWatcher(mAmountValue));
			mPinValue=(EditText) mMiddleView.findViewById(R.id.storeowner_pointofsale_part1_pin_value);
			mPinValue.setInputType(InputType.TYPE_CLASS_NUMBER);
			mPinValue.setTransformationMethod(PasswordTransformationMethod.getInstance());	
			mCouponCodeValue = (EditText) mMiddleView.findViewById(R.id.couponcode_value);
			mCouponCodeImage = (ImageView) mMiddleView.findViewById(R.id.couponcode_image);
			mAddNotesTextView = (TextView) mMiddleView.findViewById(R.id.pos_add_notes);
			mAmountSubmit=(Button) mMiddleView.findViewById(R.id.storeowner_pointofsale_part1_submit);
			mLeftFooter=(LinearLayout) mFooterView.findViewById(R.id.storeowner_pointofsale_part1_leftFooter);
			mLeftFooterText=(TextView) mFooterView.findViewById(R.id.storeowner_pointofsale_part1_leftFooterText);
			mRightFooter=(LinearLayout) mFooterView.findViewById(R.id.storeowner_pointofsale_part1_rightFooter);
			mSplitter1=(LinearLayout) mFooterView.findViewById(R.id.menubar_splitter1);
			mSplitter2=(LinearLayout) mFooterView.findViewById(R.id.menubar_splitter2);
			mSplitter3=(LinearLayout) mFooterView.findViewById(R.id.menubar_splitter3);
			mInvisible1 = (LinearLayout) mFooterView.findViewById(R.id.storeowner_pointofsale_part1_invisible1);
			mInvisible2 = (LinearLayout) mFooterView.findViewById(R.id.storeowner_pointofsale_part1_invisible2);

			mAddCard=(Button) mMiddleView.findViewById(R.id.storeowner_pointofsale_part1_addcustomercard);
			mListView=(ListView) mMiddleView.findViewById(R.id.storeowner_pointofsale_part1_list);

			mAmountValueText=(TextView) mMiddleView.findViewById(R.id.storeowner_pointofsale_part1_amountvalue);
			mAmountRemainingValueText=(TextView) mMiddleView.findViewById(R.id.storeowner_pointofsale_part1_amountremainingvalue);
			mTipAmountValueText=(TextView) mMiddleView.findViewById(R.id.storeowner_pointofsale_part1_tiptextvalue);
			mTipPercentageText=(TextView) mMiddleView.findViewById(R.id.storeowner_pointofsale_part1_tippercentage);
			mTotalAmountValueText=(TextView) mMiddleView.findViewById(R.id.storeowner_pointofsale_part1_totaltextvalue);

			mAddNotesTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					final Dialog mNotesDetails = new Dialog(StoreOwner_PointOfSale_Part1.this);
					mNotesDetails.setTitle("Note Details");
					mNotesDetails.setContentView(R.layout.registration_mobiledetails);
					WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
					lWindowParams.copyFrom(mNotesDetails.getWindow().getAttributes());
					lWindowParams.width = WindowManager.LayoutParams.FILL_PARENT;
					lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
					mNotesDetails.getWindow().setAttributes(lWindowParams);
					final EditText mDialogNotesValue = (EditText) mNotesDetails.findViewById(R.id.notes_value);
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
					mQRScannerType = "CouponBarcode";
					IntentIntegrator.initiateScan(StoreOwner_PointOfSale_Part1.this);
				}
			});

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
							CheckUserPINTask mCheckPIN = new CheckUserPINTask(StoreOwner_PointOfSale_Part1.this,"mobile",mAmountValue.getText().toString(),mCouponCodeValue.getText().toString(),mStoreOwnerNotes);
							mCheckPIN.execute(mPinValue.getText().toString());
						}
					}else{}
				}
			});

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

			mAddCard.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent_pointofsale_part2 = new Intent(StoreOwner_PointOfSale_Part1.this,StoreOwner_PointOfSale_Part2.class);
					startActivity(intent_pointofsale_part2);
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
		}
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
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_PointOfSale_Part1.this);
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
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mListView.setVisibility(View.VISIBLE);
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
				if(mQRScannerType.equalsIgnoreCase("CouponBarcode")){

					if(result.contains("-") && result.split("-").length >0){
						mCouponCodeValue.setText(result.split("-")[1]);	
					}

				}else if(mQRScannerType.equalsIgnoreCase("QRCode")){
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
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_PointOfSale_Part1.this);
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