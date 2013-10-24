package com.us.zoupons.storeowner.videos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.POJOVideoURL;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.VideoPlay.VideoDialogActivity;
import com.us.zoupons.android.AsyncThreadClasses.PlayVideoClass;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class VideosDetails extends Activity {

	public String TAG="StoreOwnerVideos";
	public MyHorizontalScrollView scrollView;
	View app;
	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;
	Header header;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;
	View mLeftMenu;
	int mMenuFlag;
	StoreOwner_LeftMenu storeowner_leftmenu;
	private RelativeLayout mVideodetailscontainer,mStoreDetailsContainer;
	private Button mStoreOwner_Videos_FreezeView,mStoreOwnerUploadVideos,mStoreOwnerActivateVideos;
	private ListView mStoreOwnerVideosList;
	private TextView mStoreName;
	private ImageView mVideosRightMenuImageHolder;
	private final int mRecordVideoRequestcode = 1,mGalleryVideoRequestcode = 2; 
	private ArrayList<Object> mAllStoreLocationVideos;
	private ImageView mVideoThumbnail,mVideoPlayButton;
	private int imageviewwidth=200,imageviewheight=200;
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
			app = inflater.inflate(R.layout.storeowner_videos, null);
			mVideodetailscontainer = (RelativeLayout) app.findViewById(R.id.storeownervideos_container);
			mStoreDetailsContainer = (RelativeLayout) mVideodetailscontainer.findViewById(R.id.storeownervideos_storename_header);
			mStoreOwner_Videos_FreezeView = (Button) app.findViewById(R.id.storeownervideos_freeze);
			mStoreName = (TextView) mStoreDetailsContainer.findViewById(R.id.storeownervideos_store_title_textId);
			mVideosRightMenuImageHolder =  (ImageView) mStoreDetailsContainer.findViewById(R.id.storeownervideos_rightmenu);
			mStoreOwnerUploadVideos = (Button) mVideodetailscontainer.findViewById(R.id.storeowner_uploadvideo);
			mStoreOwnerActivateVideos = (Button) mVideodetailscontainer.findViewById(R.id.storeowner_activatevideo);
			mVideoThumbnail = (ImageView) app.findViewById(R.id.mVideoImage);
			mVideoPlayButton = (ImageView) app.findViewById(R.id.mPlayButton);
			registerForContextMenu(mStoreOwnerUploadVideos);
			mStoreOwnerVideosList = (ListView) mVideodetailscontainer.findViewById(R.id.storeowner_videos_list);
			storeowner_rightmenu = new StoreOwner_RightMenu(VideosDetails.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_Videos_FreezeView, TAG);
			mRightMenu = storeowner_rightmenu.intializeInflater();
			storeowner_leftmenu = new StoreOwner_LeftMenu(VideosDetails.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Videos_FreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();

			storeowner_leftmenu.clickListener(mLeftMenu/*,mRightMenu*/);
			storeowner_rightmenu.clickListener(mLeftMenu,mRightMenu);

			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeownervideos_header);
			header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Videos_FreezeView, TAG);

			final View[] children = new View[] { mLeftMenu, app, mRightMenu };

			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));

			mVideosRightMenuImageHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwner_Videos_FreezeView, TAG));
			mStoreOwner_Videos_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_Videos_FreezeView, TAG));

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
							Intent intentPlay = new Intent(VideosDetails.this,VideoDialogActivity.class);
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
					openContextMenu(v);	
				}
			});

			mStoreOwnerActivateVideos.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(StoreVideosAdapter.checked_position != -1){
						POJOVideoURL mVideoDetail = (POJOVideoURL) mAllStoreLocationVideos.get(StoreVideosAdapter.checked_position);
					}else{}
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
			if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
				Intent mVideoCapture_intent = new Intent(VideosDetails.this,CustomVideoRecorder.class);
				startActivity(mVideoCapture_intent);
			}else{
				Toast.makeText(VideosDetails.this, "Please insert sdcard to save video", Toast.LENGTH_SHORT).show();
			}
		}else if(item.getItemId() == 1){
			Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
			//comma-separated MIME types
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

	public void populateList(ArrayList<Object> result){
		mAllStoreLocationVideos = result;
		mStoreOwnerVideosList.setAdapter(new StoreVideosAdapter(VideosDetails.this,result));
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
		mNotificationSync = new ScheduleNotificationSync(VideosDetails.this);
		mNotificationSync.setRecurringAlarm();
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
		//mStoreOwnerVideosList.setVisibility(View.VISIBLE);
	}

	public boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

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
			if((temp_file.length()/1024/1024) < 100){
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