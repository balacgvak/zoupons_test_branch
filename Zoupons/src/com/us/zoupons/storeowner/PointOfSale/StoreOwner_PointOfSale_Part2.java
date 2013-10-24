package com.us.zoupons.storeowner.PointOfSale;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.Base64;
import com.us.zoupons.DecodeImageWithRotation;
import com.us.zoupons.MobileNumberFilter;
import com.us.zoupons.MobileNumberTextWatcher;
import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.Settings;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.CardId_ClassVariable;
import com.us.zoupons.ClassVariables.EditCardDetails_ClassVariables;
import com.us.zoupons.ClassVariables.ManageCardAddPin_ClassVariables;
import com.us.zoupons.ClassVariables.POJOUserProfile;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.cards.ImageLoader;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class StoreOwner_PointOfSale_Part2 extends Activity {

	public static String TAG="StoreOwner_PointOfSale_Part2";
	
	public static MyHorizontalScrollView scrollView;
    View app;
    public Typeface mZouponsFont;
    Header header;
	StoreOwner_LeftMenu storeowner_leftmenu;
	View mLeftMenu;
	int mMenuFlag;
	
	private Button mFreezeView,mMobileNumberSubmit,mPinUnLock,mAddCard,mAuthorizeButton;
	private EditText mMobileNumberValue,mFirstName,mLastName,mCustomerPin,mCustomerEmailId,mCustomerAmount,mCustomerTipAmount;
	private TextView mCustomerTotalValue,mBack,mFirstNameText,mLastNameText,mAddImageText,mAddedCreditcardMaskNumber;
	private ImageView mCustomerProfileImage;
	private LinearLayout mSplitter1,mAddCardDetailsLayout;
	ViewGroup mMiddleView,mFooterView,mMobileNumberContainer,mCustomerInfoContainer,mZouponCustomerPinContainer,mZouponCustomerEmailIdContainer,mZouponCustomerMobilePayContainer;
	private NetworkCheck mConnectionAvailabilityChecking;
	public String mProfilePhoto;
	private String mZouponsCustomerId="",mZouponsCustomerType="",mZouponsCustomerStatus="",mNonZouponsMemberUserId="",mNonZouponsMemberCardID="",mNonZouponsMemberCardName="",mNonZouponsMemberCardMask="",mNonZouponsMemberCardCVV="",mNonZouponsMemberCardexpiryYear="",mNonZouponsMemberCardexpiryMonth="",mNonZouponsMemberStreetAddress="",mNonZouponsMemberZipcode="";
	private int mSetImageWidth,mSetImageHeight; 
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
		app = inflater.inflate(R.layout.storeowner_pointofsale_part2, null);
		
		mMiddleView = (ViewGroup) app.findViewById(R.id.storeowner_pointofsale_part2_middleview);
	    mFooterView = (ViewGroup) app.findViewById(R.id.storeowner_pointofsale_part2_footer);
	    mMobileNumberContainer = (ViewGroup) mMiddleView.findViewById(R.id.initial_telephone_layout);
	    mCustomerInfoContainer = (ViewGroup) mMiddleView.findViewById(R.id.storeowner_customer_pointofsale_part2_info);
	    mZouponCustomerPinContainer = (ViewGroup) mCustomerInfoContainer.findViewById(R.id.storeowner_customer_pointofsale_part2_pin_container);
	    mZouponCustomerEmailIdContainer = (ViewGroup) mCustomerInfoContainer.findViewById(R.id.storeowner_new_cusotmer_pointofsale_part2_emailidcontainer);
        mAddCardDetailsLayout = (LinearLayout) mCustomerInfoContainer.findViewById(R.id.storeowner_new_cusotmer_pointofsale_part2_addcard_detailContainer);	   
        mAddedCreditcardMaskNumber = (TextView) mCustomerInfoContainer.findViewById(R.id.txtAddedCardMaskNumber);
        mZouponCustomerMobilePayContainer = (ViewGroup) mCustomerInfoContainer.findViewById(R.id.mobilepay_amountdetails);
	    
	    
	    mFreezeView = (Button) app.findViewById(R.id.storeowner_pointofsale_part2_freeze);
	    storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_PointOfSale_Part2.this,scrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);
	    mLeftMenu = storeowner_leftmenu.intializeInflater();
	    storeowner_leftmenu.clickListener(mLeftMenu);
	    
	    /* Header Tab Bar which contains logout,notification and home buttons*/
	    header = (Header) app.findViewById(R.id.storeowner_pointofsale_part2_header);
	    header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mFreezeView, TAG);
	    
	    final View[] children = new View[] { mLeftMenu, app};
	    /* Scroll to app (view[1]) when layout finished.*/
		int scrollToViewIdx = 1;
		scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
		
		mFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, mMenuFlag, mFreezeView, TAG));
		
		mBack=(TextView) mFooterView.findViewById(R.id.storeowner_pointofsale_part2_Back);
		mBack.setVisibility(View.INVISIBLE);
		mSplitter1=(LinearLayout) mFooterView.findViewById(R.id.menubar_splitter1);
		mSplitter1.setVisibility(View.INVISIBLE);
		
		mMobileNumberSubmit=(Button) mMobileNumberContainer.findViewById(R.id.storeowner_pointofsale_part2_phoneNumber_submit);
		mMobileNumberValue=(EditText) mMobileNumberContainer.findViewById(R.id.storeowner_pointofsale_part2_phoneNumberId);
		
		mCustomerProfileImage = (ImageView) mCustomerInfoContainer.findViewById(R.id.storeowner_customer_pointofsale_part2_ImageId);
		mAddImageText =  (TextView) mCustomerInfoContainer.findViewById(R.id.storeowner_customer_pointofsale_part2_ImageText);
		mFirstName=(EditText) mCustomerInfoContainer.findViewById(R.id.storeowner_customer_pointofsale_part2_FirstName);
		mLastName=(EditText) mCustomerInfoContainer.findViewById(R.id.storeowner_customer_pointofsale_part2_LastName);
		mFirstNameText = (TextView) mCustomerInfoContainer.findViewById(R.id.storeowner_customer_pointofsale_part2_FirstName_text);
		mLastNameText = (TextView) mCustomerInfoContainer.findViewById(R.id.storeowner_customer_pointofsale_part2_LastName_text);
		mCustomerEmailId=(EditText) mZouponCustomerEmailIdContainer.findViewById(R.id.storeowner_customer_pointofsale_part2_EmailId);
		mCustomerAmount=(EditText) mZouponCustomerMobilePayContainer.findViewById(R.id.mobilepay_amount_value);
		mCustomerTipAmount=(EditText) mZouponCustomerMobilePayContainer.findViewById(R.id.mobilepay_tip_value);
		mCustomerTotalValue=(TextView) mZouponCustomerMobilePayContainer.findViewById(R.id.mobilepay_total_value);
		mAddCard=(Button) mCustomerInfoContainer.findViewById(R.id.storeowner_pointofsale_part2_addcard);
		mAuthorizeButton=(Button) mCustomerInfoContainer.findViewById(R.id.storeowner_pointofsale_part2_authorizeButtonId);
		
		mCustomerPin=(EditText) mZouponCustomerPinContainer.findViewById(R.id.storeowner_customer_pointofsale_part2_pin_value);
		mCustomerPin.setInputType(InputType.TYPE_CLASS_NUMBER);
		mCustomerPin.setTransformationMethod(PasswordTransformationMethod.getInstance());	
		mPinUnLock=(Button) mZouponCustomerPinContainer.findViewById(R.id.storeowner_pointofsale_part2_unlock_button);
		mConnectionAvailabilityChecking = new NetworkCheck();
		// To validate mobile number and setting the limit...
		mMobileNumberValue.addTextChangedListener(new MobileNumberTextWatcher());
		mMobileNumberValue.setFilters(new InputFilter[]{new MobileNumberFilter(), new InputFilter.LengthFilter(12)});
		// To set default image data
		Bitmap mBitmapProfileImage = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage); 
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		mBitmapProfileImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
		byte[] Photo=stream.toByteArray();
		mProfilePhoto = Base64.encodeBytes(Photo);
				
		
		mMobileNumberSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mMobileNumberValue.getText().toString().trim().length() == 0){
					alertBox_service("Information","Please enter mobile number",mMobileNumberValue);
				}else if(mMobileNumberValue.getText().toString().trim().length() != 12){
					alertBox_service("Information","Please enter valid mobile number",mMobileNumberValue);						
				}else{
					// To hide keyboard
					// To open keyboard default 
					InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(mMobileNumberValue.getWindowToken(), 0);
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
					// To get location id,user_id,user_type from shared preferences... 
					SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
					String mLocationId = mPrefs.getString("location_id", "");
					String mUser_Id = mPrefs.getString("user_id", "");
					String mUserType = mPrefs.getString("user_type", "");
					if(mConnectionAvailabilityChecking.ConnectivityCheck(StoreOwner_PointOfSale_Part2.this)){
						ValidateCustomerUsingMobileNumber mApprove_by_mobile = new ValidateCustomerUsingMobileNumber(StoreOwner_PointOfSale_Part2.this,mMobileNumberValue.getText().toString());
						mApprove_by_mobile.execute("get_userdetails");	
					}else{
						Toast.makeText(StoreOwner_PointOfSale_Part2.this, "No network connection",Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCustomerInfoContainer.setVisibility(View.GONE);
				mMobileNumberContainer.setVisibility(View.VISIBLE);
				mBack.setVisibility(View.INVISIBLE);
				mSplitter1.setVisibility(View.INVISIBLE);
			}
		});
		
		mPinUnLock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mCustomerPin.getText().toString().length() == 0){
					alertBox_service("Information", "Please enter secret PIN ", mFirstName);
				}else if(mCustomerPin.getText().toString().length() < 4){
					alertBox_service("Information", "Please enter four digit secret PIN", mFirstName);
				}else{
					CheckUserPINTask mCheckPIN = new CheckUserPINTask(StoreOwner_PointOfSale_Part2.this);
					mCheckPIN.execute(mCustomerPin.getText().toString(),mZouponsCustomerId,mZouponsCustomerType);
				}
			}
		});
		
		mAddCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mFirstName.getText().toString().length()==0){
					alertBox_service("Information", "Please enter first name", mFirstName);
				}else if(mLastName.getText().toString().length()==0){
					alertBox_service("Information", "Please enter last name", mLastName);
				}else if(mCustomerEmailId.getText().toString().length()==0){
					alertBox_service("Information", "Please enter email id", mCustomerEmailId);
				}else if(!isValidEmail(mCustomerEmailId.getText().toString())){
					alertBox_service("Information", "Please enter valid email id", mCustomerEmailId);
				}else{
					if(mAddCard.getText().toString().equalsIgnoreCase("Edit card")){
						ManageCardAddPin_ClassVariables.mEditCardFlag="true";
						ManageCardAddPin_ClassVariables.mAddPinFlag="false";

						CardId_ClassVariable.cardid = mNonZouponsMemberCardID;
						EditCardDetails_ClassVariables.cardName = mNonZouponsMemberCardName;
						EditCardDetails_ClassVariables.cardNumber = mNonZouponsMemberCardMask;
						EditCardDetails_ClassVariables.cvv = mNonZouponsMemberCardCVV;
						EditCardDetails_ClassVariables.expiryYear = mNonZouponsMemberCardexpiryYear;
						EditCardDetails_ClassVariables.expiryMonth = mNonZouponsMemberCardexpiryMonth;
						EditCardDetails_ClassVariables.StreatAddress = mNonZouponsMemberStreetAddress;
						EditCardDetails_ClassVariables.zipCode = mNonZouponsMemberZipcode;
						ValidateCustomerUsingMobileNumber mUpdateUserTask = new ValidateCustomerUsingMobileNumber(StoreOwner_PointOfSale_Part2.this, mMobileNumberValue.getText().toString(),mZouponsCustomerId, mZouponsCustomerStatus,mFirstName.getText().toString(), mLastName.getText().toString(), mCustomerEmailId.getText().toString(),mProfilePhoto);	
						mUpdateUserTask.execute("update_userdetails");
					}else{
						ValidateCustomerUsingMobileNumber mUpdateUserTask = new ValidateCustomerUsingMobileNumber(StoreOwner_PointOfSale_Part2.this, mMobileNumberValue.getText().toString(),mZouponsCustomerId, mZouponsCustomerStatus,mFirstName.getText().toString(), mLastName.getText().toString(), mCustomerEmailId.getText().toString(),mProfilePhoto);	
						mUpdateUserTask.execute("update_userdetails");
					}	
				}	
							
			}
		});
				
		mCustomerProfileImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openContextMenu(v);
			}
		});
		
		mAuthorizeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			  if(mNonZouponsMemberCardID.equalsIgnoreCase("")){
				  alertBox_service("Information", "Please enter customer details and add credit card for payment", mFirstName);
			  }else{
				  SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				  String mLocationId = mPrefs.getString("location_id", "");
				  String mUser_Id = mPrefs.getString("user_id", "");
				  String mStore_id = mPrefs.getString("store_id", "");
				  NonMemberPaymentTask mNonMemberTask = new NonMemberPaymentTask(StoreOwner_PointOfSale_Part2.this,mNonZouponsMemberCardID, mStore_id, "0.00",StoreOwner_PointOfSale_Part1.mAmount,"",StoreOwner_PointOfSale_Part1.mAmount,mLocationId,mNonZouponsMemberUserId);
				  mNonMemberTask.execute(mCustomerEmailId.getText().toString(),mFirstName.getText().toString(), mLastName.getText().toString(),mProfilePhoto,mMobileNumberValue.getText().toString());
			  }
			}
		});
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateViews(ArrayList<Object> result){
		try{
		POJOUserProfile userprofile = (POJOUserProfile) result.get(0);
		// Setting user details
		mZouponsCustomerId = userprofile.mUserId;
		mZouponsCustomerType = userprofile.mUserType;
		mZouponsCustomerStatus = userprofile.mStatus;
		if(userprofile.mStatus.equalsIgnoreCase("Active")){ // Existing zoupons customer
			mMobileNumberContainer.setVisibility(View.GONE);
			mBack.setVisibility(View.VISIBLE);
			mSplitter1.setVisibility(View.VISIBLE);
			mCustomerInfoContainer.setVisibility(View.VISIBLE);
			mZouponCustomerPinContainer.setVisibility(View.VISIBLE);
			mZouponCustomerEmailIdContainer.setVisibility(View.GONE);
			mZouponCustomerMobilePayContainer.setVisibility(View.GONE);
			mAddCardDetailsLayout.setVisibility(View.GONE);
			mAddCard.setVisibility(View.GONE);
			mAuthorizeButton.setVisibility(View.GONE);
			// Setting customer details
			mFirstNameText.setVisibility(View.VISIBLE);
			mLastNameText.setVisibility(View.VISIBLE);
			mFirstNameText.setText(userprofile.mUserFirstName);
			mLastNameText.setText(userprofile.mUserLastName);
			// Hiding editext 
			mFirstName.setVisibility(View.GONE);
			mLastName.setVisibility(View.GONE);
			mAddImageText.setVisibility(View.GONE);
			// To remove context menu if exists
			unregisterForContextMenu(mCustomerProfileImage);
			ImageLoader mProfileImageLoader = new ImageLoader(StoreOwner_PointOfSale_Part2.this);
			mProfileImageLoader.DisplayImage(userprofile.mUserImage,mCustomerProfileImage);
			mCustomerPin.requestFocus();
			InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.toggleSoftInputFromWindow(mCustomerPin.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
		}else{ // New Customer
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			mMobileNumberContainer.setVisibility(View.GONE);
			mBack.setVisibility(View.VISIBLE);
			mSplitter1.setVisibility(View.VISIBLE);
			mCustomerInfoContainer.setVisibility(View.VISIBLE);
			mFirstNameText.setVisibility(View.GONE);
			mLastNameText.setVisibility(View.GONE);
			// Show editText 
			mFirstName.setVisibility(View.VISIBLE);
			mFirstName.getText().clear();
			mLastName.setVisibility(View.VISIBLE);
			mLastName.getText().clear();
			mCustomerEmailId.getText().clear();
			// to register context menu for adding photo
			registerForContextMenu(mCustomerProfileImage);
			mCustomerProfileImage.setImageResource(R.drawable.profileimage);
			// To set default image data
			Bitmap mBitmapProfileImage = BitmapFactory.decodeResource(getResources(), R.drawable.profileimage); 
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			mBitmapProfileImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
			byte[] Photo=stream.toByteArray();
			mProfilePhoto = Base64.encodeBytes(Photo);
			mZouponCustomerPinContainer.setVisibility(View.GONE);
			mZouponCustomerEmailIdContainer.setVisibility(View.VISIBLE);
			mAddCardDetailsLayout.setVisibility(View.GONE);
			mZouponCustomerMobilePayContainer.setVisibility(View.GONE);
			mAddCard.setVisibility(View.VISIBLE);
			mAuthorizeButton.setVisibility(View.VISIBLE);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Choose From");
		menu.add(1, 0, 0, "Take Picture");
		menu.add(1, 1, 1, "Gallery");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getGroupId()){
		case 1:
			if(item.getItemId() == 0){
				Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent_camera, 200);
			}else if(item.getItemId() == 1){
				Intent mGalleryIntent=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(mGalleryIntent, 100);
			} 
			return true;
		default:
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100 && resultCode == RESULT_OK){ // From camera
			setImageBitmap(data);
		}else if(requestCode == 200 && resultCode == RESULT_OK){ // From gallery
			setImageBitmap(data);
		}else if(requestCode == 300 && resultCode == RESULT_OK){ // From Add card 
			// Returning values for use in edit card details..
			mNonZouponsMemberUserId = data.getExtras().getString("user_id");
			mNonZouponsMemberCardID = data.getExtras().getString("card_id");
			mNonZouponsMemberCardName = data.getExtras().getString("card_name");
			mNonZouponsMemberCardMask = data.getExtras().getString("card_masknumber");
			mNonZouponsMemberCardexpiryMonth = data.getExtras().getString("card_expirymonth");
			mNonZouponsMemberCardexpiryYear = data.getExtras().getString("card_expiryyear");
			mNonZouponsMemberCardCVV = data.getExtras().getString("card_cvv");
			mNonZouponsMemberStreetAddress = data.getExtras().getString("address");
			mNonZouponsMemberZipcode = data.getExtras().getString("zipcode");
			mZouponCustomerEmailIdContainer.setVisibility(View.GONE);
			mAddCardDetailsLayout.setVisibility(View.VISIBLE);
			mAddedCreditcardMaskNumber.setText(mNonZouponsMemberCardMask);
			mAddCard.setText("Edit Card");
			
		}
	}
	
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
			options.inSampleSize = Settings.calculateInSampleSize(options, mCustomerProfileImage.getWidth(), mCustomerProfileImage.getHeight());
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap mSelectedImage = BitmapFactory.decodeFile(selected_file_path, options);
			mSelectedImage = new DecodeImageWithRotation().decodeImage(selected_file_path, mSelectedImage, mSetImageWidth, mSetImageHeight);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);    
			byte[] imagedata = baos.toByteArray();
			mProfilePhoto = com.us.zoupons.Base64.encodeBytes(imagedata);
			mSelectedImage = Bitmap.createScaledBitmap(mSelectedImage, mSetImageWidth,mSetImageHeight , true);
			BitmapDrawable drawable = new BitmapDrawable(mSelectedImage);
			mCustomerProfileImage.setImageDrawable(drawable);
			mAddImageText.setVisibility(View.GONE);
		}catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Please select valid image.", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	protected final static boolean isValidEmail(String mailid){
		try{
			Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
					"\\@" +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
					"(" +
					"\\." +
					"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
					")+");
			return EMAIL_ADDRESS_PATTERN.matcher(mailid).matches();
		}catch(Exception e){
			return false;
		}
	}
		
	
	private void alertBox_service(String title, final String msg,final EditText editText) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwner_PointOfSale_Part2.this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setCancelable(true);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				editText.requestFocus();
			}
		});
		service_alert.show();
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
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_PointOfSale_Part2.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwner_PointOfSale_Part2.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwner_PointOfSale_Part2.this);
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
		mSetImageWidth=mCustomerProfileImage.getWidth();
		mSetImageHeight=mCustomerProfileImage.getHeight();
	}
	
	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_PointOfSale_Part2.this);
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
