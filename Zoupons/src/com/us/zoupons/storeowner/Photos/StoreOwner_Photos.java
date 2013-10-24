package com.us.zoupons.storeowner.Photos;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.MyHorizontalScrollView;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.BroadCastActionClassVariables;
import com.us.zoupons.ClassVariables.POJOStorePhoto;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.StoreInfo.StorePhotoAdapter;
import com.us.zoupons.StoreInfo.StorePhotoGridAdapter;
import com.us.zoupons.StoreInfo.StorePhotoLoaderAsyncTask;
import com.us.zoupons.StoreInfo.UnderlinePageIndicator;
import com.us.zoupons.android.AsyncThreadClasses.PhotoSwitcherAsynchThread;
import com.us.zoupons.android.AsyncThreadClasses.StoreDetailsAsyncTask;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.GeneralScrollingClass.StoreOwnerSizeCallBackForMenu;

public class StoreOwner_Photos extends Activity {

	public static String TAG="StoreOwner_Photos";

	public static MyHorizontalScrollView scrollView;
	View app;

	public Typeface mZouponsFont;
	public NetworkCheck mConnectionAvailabilityChecking;

	Header header;
	StoreOwner_RightMenu storeowner_rightmenu;
	View mRightMenu;
	View mLeftMenu;
	int mMenuFlag;
	StoreOwner_LeftMenu storeowner_leftmenu;

	Button mStoreOwner_Photos_FreezeView;
	Button mStoreOwner_Photos_Back,mSplitter,mStoreOwner_Photos_AddDeletePhoto;

	View mBackSplitter;
	GridView mStoreOwner_Photos_GridView;
	private ViewPager mImagePager;
	private ImageView mPhotoSwitcherLeftArrow,mPhotoSwitcherRightArrow;
	UnderlinePageIndicator mIndicator;
	private TextView mStoreNameText;
	private ImageView mRightMenuHolder;
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private int mImagePagerWidth,mImagePagerHeight;
	boolean mLeftArrowFlag,mRightArrowFlag;
	public static StorePhotoLoaderAsyncTask storePhotoLoaderAsyncTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			LayoutInflater inflater = LayoutInflater.from(this);
			scrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(scrollView);

			mZouponsFont=Typeface.createFromAsset(getAssets(), "helvetica.ttf");
			mConnectionAvailabilityChecking = new NetworkCheck();

			app = inflater.inflate(R.layout.storeowner_photos, null);

			ViewGroup mMiddleView = (ViewGroup) app.findViewById(R.id.storeowner_photos_container);
			ViewGroup mFooterView = (ViewGroup) app.findViewById(R.id.storeowner_photos_footer);

			mStoreNameText = (TextView) app.findViewById(R.id.storeowner_photos_storename_textId);
			mRightMenuHolder = (ImageView) mMiddleView.findViewById(R.id.storeowner_photos_rightmenu);
			mStoreOwner_Photos_FreezeView = (Button) app.findViewById(R.id.storeowner_photos_freeze);
			mStoreOwner_Photos_Back = (Button) mFooterView.findViewById(R.id.storeowner_photos_backId);
			mBackSplitter = (View) mFooterView.findViewById(R.id.back_menubar_splitterId);
			mStoreOwner_Photos_AddDeletePhoto = (Button) mFooterView.findViewById(R.id.storeowner_photos_addphotoId);
			mStoreOwner_Photos_GridView = (GridView) mMiddleView.findViewById(R.id.storeowner_photos_grid_view);
			mImagePager = (ViewPager) mMiddleView.findViewById(R.id.mImagePager);
			//mImagePager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
			/*mImagePager.setHorizontalFadingEdgeEnabled(true);
			mImagePager.setFadingEdgeLength(30);*/
			mPhotoSwitcherLeftArrow = (ImageView) mMiddleView.findViewById(R.id.storeimage_left_arrow);
			mPhotoSwitcherRightArrow = (ImageView) mMiddleView.findViewById(R.id.storeimage_right_arrow);
			mIndicator = (UnderlinePageIndicator) mMiddleView.findViewById(R.id.mIndicator);


			storeowner_rightmenu = new StoreOwner_RightMenu(StoreOwner_Photos.this,scrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwner_Photos_FreezeView, TAG);
			mRightMenu = storeowner_rightmenu.intializeInflater();
			storeowner_leftmenu = new StoreOwner_LeftMenu(StoreOwner_Photos.this,scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Photos_FreezeView, TAG);
			mLeftMenu = storeowner_leftmenu.intializeInflater();

			storeowner_leftmenu.clickListener( mLeftMenu /*,mRightMenu*/ );
			storeowner_rightmenu.clickListener( mLeftMenu, mRightMenu);

			/* Header Tab Bar which contains logout,notification and home buttons*/
			header = (Header) app.findViewById(R.id.storeownerphotos_header);
			header.intializeInflater(scrollView, mLeftMenu, mMenuFlag=1, mStoreOwner_Photos_FreezeView, TAG);
			header.mTabBarNotificationContainer.setVisibility(View.VISIBLE);

			final View[] children = new View[] { mLeftMenu, app,mRightMenu};

			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			scrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(header.mLeftMenuBtnSlide));
			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwner_Photos_FreezeView, TAG));
			mStoreOwner_Photos_GridView.setVisibility(View.VISIBLE);
			mImagePager.setVisibility(View.INVISIBLE);
			mIndicator.setVisibility(View.INVISIBLE);
			mStoreOwner_Photos_Back.setVisibility(View.INVISIBLE);
			mBackSplitter.setVisibility(View.INVISIBLE);
			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String mStoreName = mPrefs.getString("store_name", "");
			mStoreNameText.setText(mStoreName);

			PhotoSwitcherAsynchThread mPhotoTask = new PhotoSwitcherAsynchThread(StoreOwner_Photos.this,mImagePager.getWidth(),mImagePager.getHeight());
			mPhotoTask.execute("");

			mStoreOwner_Photos_FreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(scrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwner_Photos_FreezeView, TAG));

			mImagePager.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

				@Override
				public void onGlobalLayout() {
					mImagePagerWidth = mImagePager.getWidth();
					mImagePagerHeight = mImagePager.getHeight();
					mImagePager.getViewTreeObserver().removeGlobalOnLayoutListener( this );
					mImagePager.setVisibility( View.INVISIBLE );
				}
			});


			mStoreOwner_Photos_Back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mStoreOwner_Photos_GridView.setVisibility(View.VISIBLE);
					mImagePager.setVisibility(View.INVISIBLE);
					mIndicator.setVisibility(View.INVISIBLE);
					mStoreOwner_Photos_Back.setVisibility(View.INVISIBLE);
					mBackSplitter.setVisibility(View.INVISIBLE);
					mPhotoSwitcherLeftArrow.setVisibility(View.INVISIBLE);
					mPhotoSwitcherRightArrow.setVisibility(View.INVISIBLE);
					mStoreOwner_Photos_AddDeletePhoto.setVisibility(View.VISIBLE);
					mStoreOwner_Photos_AddDeletePhoto.setText("Add Photo");
				}
			});

			mStoreOwner_Photos_AddDeletePhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mStoreOwner_Photos_AddDeletePhoto.getText().toString().trim().equalsIgnoreCase("Add Photo")){
						Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
						startActivityForResult(cameraIntent, 100);
					}else{
						alertBox_service("Information","Do you want to delete this photo?");
					}
				}
			});

			mStoreOwner_Photos_GridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

					mStoreOwner_Photos_GridView.setVisibility(View.GONE);
					mImagePager.setVisibility(View.VISIBLE);
					mIndicator.setVisibility(View.VISIBLE);
					mImagePagerWidth = mImagePager.getWidth();
					int widthpropotion = mImagePagerWidth/5;
					mImagePagerHeight = widthpropotion*4;
					mImagePager.setAdapter(new StorePhotoAdapter(StoreOwner_Photos.this,mImagePager.getWidth(),mImagePagerHeight,mImagePager,mPhotoSwitcherLeftArrow,mPhotoSwitcherRightArrow));
					mPhotoSwitcherLeftArrow.setVisibility(View.VISIBLE);
					mPhotoSwitcherRightArrow.setVisibility(View.VISIBLE);
					mImagePager.setCurrentItem(position);
					if(position==0){
						if(WebServiceStaticArrays.mFullSizeStorePhotoUrl.size()==1){
							mPhotoSwitcherLeftArrow.setAlpha(100);
							mPhotoSwitcherRightArrow.setAlpha(100);
						}else{
							mPhotoSwitcherLeftArrow.setAlpha(100);
							mPhotoSwitcherRightArrow.setAlpha(255);
						}
					}else if(position==WebServiceStaticArrays.mFullSizeStorePhotoUrl.size()-1){
						mPhotoSwitcherLeftArrow.setAlpha(255);
						mPhotoSwitcherRightArrow.setAlpha(100);
					}else{
						mPhotoSwitcherLeftArrow.setAlpha(255);
						mPhotoSwitcherRightArrow.setAlpha(255);
					}
					mIndicator.setViewPager(mImagePager);
					mIndicator.setSelectedColor(getResources().getColor(R.color.blue));
					mStoreOwner_Photos_Back.setVisibility(View.VISIBLE);
					mBackSplitter.setVisibility(View.VISIBLE);
					mStoreOwner_Photos_AddDeletePhoto.setVisibility(View.VISIBLE);
				}
			});

			mImagePager.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

				@Override
				public void onGlobalLayout() {
					mImagePagerWidth = mImagePager.getWidth();
					mImagePagerHeight = mImagePager.getHeight();
					mImagePager.getViewTreeObserver().removeGlobalOnLayoutListener( this );
					mImagePager.setVisibility( View.INVISIBLE );
				}
			});

			
			mPhotoSwitcherLeftArrow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mRightArrowFlag=false;
					if((mImagePager.getCurrentItem()-1)==0){
						mPhotoSwitcherLeftArrow.setAlpha(100);
						mPhotoSwitcherRightArrow.setAlpha(255);
						mLeftArrowFlag=true;
					}else{
						if(mLeftArrowFlag!=true){
							mPhotoSwitcherLeftArrow.setAlpha(255);
							mPhotoSwitcherRightArrow.setAlpha(255);
						}
					}
					mImagePager.setCurrentItem((mImagePager.getCurrentItem()-1), true);
				}
			});
			
			mPhotoSwitcherRightArrow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mLeftArrowFlag=false;
					if((mImagePager.getCurrentItem()+1)==(WebServiceStaticArrays.mFullSizeStorePhotoUrl.size()-1)){
						mPhotoSwitcherLeftArrow.setAlpha(255);
						mPhotoSwitcherRightArrow.setAlpha(100);
						mRightArrowFlag=true;
					}else{
						if(mRightArrowFlag!=true){
							mPhotoSwitcherLeftArrow.setAlpha(255);
							mPhotoSwitcherRightArrow.setAlpha(255);
						}
					}
					mImagePager.setCurrentItem((mImagePager.getCurrentItem()+1), true);
				}
			});
			

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == 100 && resultCode == RESULT_OK){
			POJOStorePhoto.add_photo_data = " ";
			String imagepath = getRealPathFromURI(data.getData());
			File mPhotoFile = new File(imagepath);
			StoreDetailsAsyncTask mStorePhotosTask = new StoreDetailsAsyncTask(StoreOwner_Photos.this,"AddPhoto",null,"PROGRESS",mPhotoFile);
			if(Build.VERSION.SDK_INT >= 11)
				mStorePhotosTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
			else
				mStorePhotosTask.execute("");
		}
	}
	
	public String getRealPathFromURI(Uri contentUri) 
	{
		String[] proj = { MediaStore.Audio.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(StoreOwner_Photos.this);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "Photo deleted successfully", Toast.LENGTH_SHORT).show();
			}
		});

		service_alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

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
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_Photos.this);
		mNotificationSync.setRecurringAlarm();
		new CheckUserSession(StoreOwner_Photos.this).checkIfSessionExpires();

		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreOwner_Photos.this);
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
		if(WebServiceStaticArrays.mFullSizeStorePhotoUrl.size() > 0){
			int width=(mStoreOwner_Photos_GridView.getWidth()-20)/3;
			mStoreOwner_Photos_GridView.setAdapter(new StorePhotoGridAdapter(StoreOwner_Photos.this,width));
			mImagePagerWidth = mImagePager.getWidth();
			mImagePagerHeight = mImagePager.getHeight();
            /*
			if(storePhotoLoaderAsyncTask==null){ 
				storePhotoLoaderAsyncTask = new StorePhotoLoaderAsyncTask(StoreOwner_Photos.this,mImagePagerWidth,mImagePagerHeight);
				storePhotoLoaderAsyncTask.execute(1);
			}*/
		}
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_Photos.this);
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
