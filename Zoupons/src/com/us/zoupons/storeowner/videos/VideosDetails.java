package com.us.zoupons.storeowner.videos;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.android.asyncthread_class.PlayVideoClass;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.shopper.video.StoreVideoDialog;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

public class VideosDetails extends Activity {

	// Initializing views and variables
	private String TAG="StoreOwnerVideos";
	private MyHorizontalScrollView mHorizontalScrollView;
	private View mApp,mRightMenu,mLeftMenu;
	private NetworkCheck mConnectionAvailabilityChecking;
	private Header mZouponsHeader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private int mMenuFlag;
	private RelativeLayout mVideodetailscontainer,mStoreDetailsContainer;
	private Button mStoreOwner_Videos_FreezeView,mStoreOwnerUploadVideos;
	private TextView mStoreName;
	private ImageView mVideosRightMenuImageHolder;
	private final int mRecordVideoRequestcode = 1,mGalleryVideoRequestcode = 2; 
	private ImageView mVideoThumbnail,mVideoPlayButton;
	private int imageviewwidth=200,imageviewheight=200;
	//Logout without user interaction after 5 minute
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			// Referencing view from layout file
			LayoutInflater inflater = LayoutInflater.from(this);
			mHorizontalScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(mHorizontalScrollView);
			mConnectionAvailabilityChecking = new NetworkCheck();
			mApp = inflater.inflate(R.layout.storeowner_videos, null);
			mVideodetailscontainer = (RelativeLayout) mApp.findViewById(R.id.storeownervideos_container);
			mStoreDetailsContainer = (RelativeLayout) mVideodetailscontainer.findViewById(R.id.storeownervideos_storename_header);
			mStoreOwner_Videos_FreezeView = (Button) mApp.findViewById(R.id.storeownervideos_freeze);
			mStoreName = (TextView) mStoreDetailsContainer.findViewById(R.id.storeownervideos_store_title_textId);
			mVideosRightMenuImageHolder =  (ImageView) mStoreDetailsContainer.findViewById(R.id.storeownervideos_rightmenu);
			mStoreOwnerUploadVideos = (Button) mVideodetailscontainer.findViewById(R.id.storeowner_uploadvideo);
			mVideoThumbnail = (ImageView) mApp.findViewById(R.id.mVideoImage);
			mVideoPlayButton = (ImageView) mApp.findViewById(R.id.mPlayButton);
			registerForContextMenu(mStoreOwnerUploadVideos);
			mStoreownerRightmenu = new StoreOwner_RightMenu(VideosDetails.this,mHorizontalScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_Videos_FreezeView, TAG);
			mRightMenu = mStoreownerRightmenu.intializeInflater();
			mStoreownerLeftmenu = new StoreOwner_LeftMenu(VideosDetails.this,mHorizontalScrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Videos_FreezeView, TAG);
			mLeftMenu = mStoreownerLeftmenu.intializeInflater();
			mStoreownerLeftmenu.clickListener(mLeftMenu);
			mStoreownerRightmenu.clickListener(mLeftMenu,mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsHeader = (Header) mApp.findViewById(R.id.storeownervideos_header);
			mZouponsHeader.intializeInflater(mHorizontalScrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Videos_FreezeView, TAG);
			final View[] children = new View[] { mLeftMenu, mApp, mRightMenu };
			// Scroll to mApp (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			mHorizontalScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsHeader.mLeftMenuBtnSlide));
			mVideosRightMenuImageHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(mHorizontalScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwner_Videos_FreezeView, TAG));
			mStoreOwner_Videos_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mHorizontalScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_Videos_FreezeView, TAG));
			mNotificationReceiver = new NotifitcationReceiver(mZouponsHeader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			
			// To set StoreName
			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", MODE_PRIVATE);
			String mStoreNameValue = mPrefs.getString("store_name", "");
			final String location_id = mPrefs.getString("location_id", "");
			final String store_id = mPrefs.getString("store_id", "");
			mStoreName.setText(mStoreNameValue);

			if(mConnectionAvailabilityChecking.ConnectivityCheck(VideosDetails.this)){
				// AsynchTask to Get Video URL and Video Thumbnail
				PlayVideoClass mPlayVideoClass = new PlayVideoClass(VideosDetails.this,mStoreName,store_id,mVideoThumbnail,mVideoPlayButton,String.valueOf(imageviewwidth),String.valueOf(imageviewheight),location_id);
				mPlayVideoClass.execute();
			}else{
				Toast.makeText(VideosDetails.this, "Network connection not available", Toast.LENGTH_SHORT).show();
			}

			mVideoPlayButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try{
						if(MainMenuActivity.VIDEOURLVALUE.length()>1){
							// To launch video preview activity
							Intent intentPlay = new Intent(VideosDetails.this,StoreVideoDialog.class);
							intentPlay.putExtra("VIDEO",MainMenuActivity.VIDEOURLVALUE);
							startActivity(intentPlay);
						}else{
							//alertBox_service("Information","Video not available...");
						}
					}catch (Exception e) {
						e.printStackTrace();		   
					}
				}
			});

			mStoreOwnerUploadVideos.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openContextMenu(v);	// To open context menu to show upload options
				}
			});

		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Choose From");
		menu.add(0, 0, 0, "Record Video");
		menu.add(0, 1, 1, "Gallery");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == 0){
			// To record video
			if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
				Intent mVideoCapture_intent = new Intent(VideosDetails.this,CustomVideoRecorder.class);
				startActivity(mVideoCapture_intent);
			}else{
				Toast.makeText(VideosDetails.this, "Please insert sdcard to save video", Toast.LENGTH_SHORT).show();
			}
		}else if(item.getItemId() == 1){
			// To choose video from gallery
			Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
			mediaChooser.setType("video/*");
			startActivityForResult(mediaChooser, 1);
		} 
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			checkAndUploadFile(data.getData());
		}else if(resultCode == RESULT_CANCELED && requestCode == mRecordVideoRequestcode){ 

		}else if(resultCode == RESULT_CANCELED && requestCode == mGalleryVideoRequestcode){

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
		mNotificationSync = new ScheduleNotificationSync(VideosDetails.this,ZouponsConstants.sStoreModuleFlag);
		mNotificationSync.setRecurringAlarm();
		// To check user session
		new CheckUserSession(VideosDetails.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(VideosDetails.this);
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
		imageviewwidth = mVideoThumbnail.getWidth();
		imageviewheight = mVideoThumbnail.getHeight();
	}

	 /* To check selected video size and upload to server */
	public void checkAndUploadFile(Uri uri){
		// specifying column(data) for retrieval
		String[] file_path_columns={MediaStore.Images.Media.DATA};
		// querying content provider to get particular image
		Cursor cursor=getContentResolver().query(uri, file_path_columns, null, null, null);
		cursor.moveToFirst();
		// getting col_index from string file_path_columns[0]--> Data column 
		int column_index=cursor.getColumnIndex(file_path_columns[0]);
		// getting the path from result as /sdcard/DCIM/100ANDRO/file_name
		String selected_file_path=cursor.getString(column_index);
		String filenameArray[] = selected_file_path.split("\\.");
		String extension = filenameArray[filenameArray.length-1];
		cursor.close();
		if(!extension.equalsIgnoreCase("mp4")){
			alertBox_service("Information", "Unsupported video format please choose mp4 videos");
		}else{
			File temp_file = new File(selected_file_path);
			if((temp_file.length()/1024/1024) < 20){
				if(mConnectionAvailabilityChecking.ConnectivityCheck(VideosDetails.this)){
					UploadVideoToServerTask mUploadTask = new UploadVideoToServerTask(VideosDetails.this, temp_file,"Gallery");
					mUploadTask.execute();
				}else{
					Toast.makeText(VideosDetails.this, "Network connection not available", Toast.LENGTH_SHORT).show();	
				}
			}else{
				alertBox_service("Information", "Selected video size is too big please choose video less than 20mb");
			}		
		}
	}

	/* To show alert pop up with respective message */
	private void alertBox_service(String title, final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(VideosDetails.this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setCancelable(true);
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
		new RefreshZoupons().isApplicationGoneBackground(VideosDetails.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
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
	public void onBackPressed() {
		//super.onBackPressed();
	}
	
}