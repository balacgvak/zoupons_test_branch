package com.us.zoupons.storeowner.photos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import com.us.zoupons.R;
import com.us.zoupons.android.asyncthread_class.PhotoSwitcherAsynchThread;
import com.us.zoupons.android.asyncthread_class.StoreDetailsAsyncTask;
import com.us.zoupons.classvariables.BroadCastActionClassVariables;
import com.us.zoupons.classvariables.ZouponsConstants;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;
import com.us.zoupons.notification.ManageNotificationWindow;
import com.us.zoupons.notification.NotifitcationReceiver;
import com.us.zoupons.notification.ScheduleNotificationSync;
import com.us.zoupons.shopper.photos.StorePhotoAdapter;
import com.us.zoupons.shopper.photos.StorePhotoGridAdapter;
import com.us.zoupons.shopper.photos.UnderlinePageIndicator;
import com.us.zoupons.storeowner.Header;
import com.us.zoupons.storeowner.StoreOwner_LeftMenu;
import com.us.zoupons.storeowner.StoreOwner_RightMenu;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerClickListenerForScrolling;
import com.us.zoupons.storeowner.generalscrollingclass.StoreOwnerSizeCallBackForMenu;

/**
 * 
 * Activity which displays store Photos in Grid and slider
 *
 */

public class StoreOwner_Photos extends Activity {

	// Initializing views,view groups and variables
	private String TAG="StoreOwner_Photos";
	private MyHorizontalScrollView mScrollView;
	private	View mApp;
	private	Header mZouponsHeader;
	private StoreOwner_RightMenu mStoreownerRightmenu;
	private View mRightMenu;
	private View mLeftMenu;
	private int mMenuFlag;
	private StoreOwner_LeftMenu mStoreownerLeftmenu;
	private Button mStoreOwnerPhotosFreezeView;
	private Button mStoreOwnerPhotosBack,mStoreOwnerPhotosAddDeletePhoto;
	private View mBackSplitter;
	private GridView mStoreOwnerPhotosGridView;
	private ViewPager mImagePager;
	private ImageView mPhotoSwitcherLeftArrow,mPhotoSwitcherRightArrow;
	private UnderlinePageIndicator mIndicator;
	private TextView mStoreNameText;
	private ImageView mRightMenuHolder;
	//Logout without user interaction after 1 minute
	private CheckLogoutSession mLogoutSession;
	private ScheduleNotificationSync mNotificationSync;
	private int mImagePagerWidth,mImagePagerHeight;
	boolean mLeftArrowFlag,mRightArrowFlag;
	private NotifitcationReceiver mNotificationReceiver; // Generic class which receives for notifcation

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			System.gc();
			LayoutInflater inflater = LayoutInflater.from(this);
			mScrollView = (MyHorizontalScrollView) inflater.inflate(R.layout.horz_scroll_with_list_menu, null);
			setContentView(mScrollView);
			mApp = inflater.inflate(R.layout.storeowner_photos, null);
			ViewGroup mMiddleView = (ViewGroup) mApp.findViewById(R.id.storeowner_photos_container);
			ViewGroup mFooterView = (ViewGroup) mApp.findViewById(R.id.storeowner_photos_footer);
			mStoreNameText = (TextView) mApp.findViewById(R.id.storeowner_photos_storename_textId);
			mRightMenuHolder = (ImageView) mMiddleView.findViewById(R.id.storeowner_photos_rightmenu);
			mStoreOwnerPhotosFreezeView = (Button) mApp.findViewById(R.id.storeowner_photos_freeze);
			mStoreOwnerPhotosBack = (Button) mFooterView.findViewById(R.id.storeowner_photos_backId);
			mBackSplitter = (View) mFooterView.findViewById(R.id.back_menubar_splitterId);
			mStoreOwnerPhotosAddDeletePhoto = (Button) mFooterView.findViewById(R.id.storeowner_photos_addphotoId);
			mStoreOwnerPhotosGridView = (GridView) mMiddleView.findViewById(R.id.storeowner_photos_grid_view);
			mImagePager = (ViewPager) mMiddleView.findViewById(R.id.mImagePager);
			/*mImagePager.setPageMargin(-50);
			mImagePager.setHorizontalFadingEdgeEnabled(true);
			mImagePager.setFadingEdgeLength(30);*/
			mPhotoSwitcherLeftArrow = (ImageView) mMiddleView.findViewById(R.id.storeimage_left_arrow);
			mPhotoSwitcherRightArrow = (ImageView) mMiddleView.findViewById(R.id.storeimage_right_arrow);
			mIndicator = (UnderlinePageIndicator) mMiddleView.findViewById(R.id.mIndicator);
			mStoreownerRightmenu = new StoreOwner_RightMenu(StoreOwner_Photos.this,mScrollView, mLeftMenu, mRightMenu, mMenuFlag=1, mStoreOwnerPhotosFreezeView, TAG);
			mRightMenu = mStoreownerRightmenu.intializeInflater();
			mStoreownerLeftmenu = new StoreOwner_LeftMenu(StoreOwner_Photos.this,mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwnerPhotosFreezeView, TAG);
			mLeftMenu = mStoreownerLeftmenu.intializeInflater();
			mStoreownerLeftmenu.clickListener( mLeftMenu /*,mRightMenu*/ );
			mStoreownerRightmenu.clickListener( mLeftMenu, mRightMenu);
			/* Header Tab Bar which contains logout,notification and home buttons*/
			mZouponsHeader = (Header) mApp.findViewById(R.id.storeownerphotos_header);
			mZouponsHeader.intializeInflater(mScrollView, mLeftMenu, mMenuFlag=1, mStoreOwnerPhotosFreezeView, TAG);
			mZouponsHeader.mTabBarNotificationContainer.setVisibility(View.VISIBLE);
			final View[] children = new View[] { mLeftMenu, mApp,mRightMenu};
			// Scroll to app (view[1]) when layout finished.
			int scrollToViewIdx = 1;
			mScrollView.initViews(children, scrollToViewIdx, new StoreOwnerSizeCallBackForMenu(mZouponsHeader.mLeftMenuBtnSlide));
			mRightMenuHolder.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag=2, mStoreOwnerPhotosFreezeView, TAG));
			mNotificationReceiver = new NotifitcationReceiver(mZouponsHeader.mTabBarNotificationCountBtn);
			// Notitification pop up layout declaration
			mZouponsHeader.mTabBarNotificationImage.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mZouponsHeader.mTabBarNotificationCountBtn.setOnClickListener(new ManageNotificationWindow(this,mZouponsHeader,mZouponsHeader.mTabBarNotificationTriangle,ZouponsConstants.sStoreModuleFlag));
			mStoreOwnerPhotosGridView.setVisibility(View.VISIBLE);
			mImagePager.setVisibility(View.INVISIBLE);
			mIndicator.setVisibility(View.INVISIBLE);
			mStoreOwnerPhotosBack.setVisibility(View.INVISIBLE);
			mBackSplitter.setVisibility(View.INVISIBLE);
			SharedPreferences mPrefs = getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String mStoreName = mPrefs.getString("store_name", "");
			mStoreNameText.setText(mStoreName);
			// To fetch store photos from web server
			PhotoSwitcherAsynchThread mPhotoTask = new PhotoSwitcherAsynchThread(StoreOwner_Photos.this,mImagePager.getWidth(),mImagePager.getHeight());
			mPhotoTask.execute("");

			mStoreOwnerPhotosFreezeView.setOnClickListener(new StoreOwnerClickListenerForScrolling(mScrollView, mLeftMenu, /*mRightMenu,*/ mMenuFlag, mStoreOwnerPhotosFreezeView, TAG));

			mImagePager.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

				@Override
				public void onGlobalLayout() {
					mImagePagerWidth = mImagePager.getWidth();
					mImagePagerHeight = mImagePager.getHeight();
					mImagePager.getViewTreeObserver().removeGlobalOnLayoutListener( this );
					mImagePager.setVisibility( View.INVISIBLE );
				}
			});

			mStoreOwnerPhotosBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mStoreOwnerPhotosGridView.setVisibility(View.VISIBLE);
					mImagePager.setVisibility(View.INVISIBLE);
					mIndicator.setVisibility(View.INVISIBLE);
					mStoreOwnerPhotosBack.setVisibility(View.INVISIBLE);
					mBackSplitter.setVisibility(View.INVISIBLE);
					mPhotoSwitcherLeftArrow.setVisibility(View.INVISIBLE);
					mPhotoSwitcherRightArrow.setVisibility(View.INVISIBLE);
					mStoreOwnerPhotosAddDeletePhoto.setVisibility(View.VISIBLE);
					mStoreOwnerPhotosAddDeletePhoto.setText("Add Photo");
				}
			});

			mStoreOwnerPhotosAddDeletePhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mStoreOwnerPhotosAddDeletePhoto.getText().toString().trim().equalsIgnoreCase("Add Photo")){
						Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
						startActivityForResult(cameraIntent, 100);
					}else{
						alertBox_service("Information","Do you want to delete this photo?");
					}
				}
			});

			mStoreOwnerPhotosGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					// To hide store photo grid 
					mStoreOwnerPhotosGridView.setVisibility(View.GONE);
					mImagePager.setVisibility(View.VISIBLE);
					mIndicator.setVisibility(View.VISIBLE);
					mImagePagerWidth = mImagePager.getWidth();
					int widthpropotion = mImagePagerWidth/5;
					mImagePagerHeight = mImagePager.getHeight();
					mImagePager.setAdapter(new StorePhotoAdapter(StoreOwner_Photos.this,mImagePager.getWidth(),mImagePagerHeight));
					mPhotoSwitcherLeftArrow.setVisibility(View.VISIBLE);
					mPhotoSwitcherRightArrow.setVisibility(View.VISIBLE);
					mImagePager.setCurrentItem(position);
					if(position==0){
						if(WebServiceStaticArrays.mStorePhoto.size()==1){ 
							mPhotoSwitcherLeftArrow.setAlpha(100);
							mPhotoSwitcherRightArrow.setAlpha(100);
						}else{
							mPhotoSwitcherLeftArrow.setAlpha(100);
							mPhotoSwitcherRightArrow.setAlpha(255);
						}
					}else if(position==WebServiceStaticArrays.mStorePhoto.size()-1){
						mPhotoSwitcherLeftArrow.setAlpha(255);
						mPhotoSwitcherRightArrow.setAlpha(100);
					}else{
						mPhotoSwitcherLeftArrow.setAlpha(255);
						mPhotoSwitcherRightArrow.setAlpha(255);
					}
					mIndicator.setViewPager(mImagePager);
					mIndicator.setSelectedColor(getResources().getColor(R.color.blue));
					mStoreOwnerPhotosBack.setVisibility(View.VISIBLE);
					mBackSplitter.setVisibility(View.VISIBLE);
					mStoreOwnerPhotosAddDeletePhoto.setVisibility(View.VISIBLE);
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
					if(WebServiceStaticArrays.mStorePhoto.size() > 1){
						mRightArrowFlag=false;
						if((mImagePager.getCurrentItem()-1)==0 || mImagePager.getCurrentItem()==0){
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

				}
			});
			
			mPhotoSwitcherRightArrow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(WebServiceStaticArrays.mStorePhoto.size() > 1){
						mLeftArrowFlag=false;
						if((mImagePager.getCurrentItem()+1)==(WebServiceStaticArrays.mStorePhoto.size()-1)){
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
					}else{

					}

				}
			});

			
			mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					Log.i("ViewPager listener", "On Page Selected");
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					Log.i("ViewPager listener", "On Page Scrolled " + arg0 );
				}
				
				@Override
				public void onPageScrollStateChanged(int position) {
					// TODO Auto-generated method stub
					Log.i("ViewPager listener", "On Page Scroll state changed" + "  Position " + position);
					if(mImagePager.getCurrentItem() == 0){
						mPhotoSwitcherLeftArrow.setAlpha(100);
						mPhotoSwitcherRightArrow.setAlpha(255);
					}else if(mImagePager.getCurrentItem() == WebServiceStaticArrays.mStorePhoto.size()-1){
						mPhotoSwitcherLeftArrow.setAlpha(255);
						mPhotoSwitcherRightArrow.setAlpha(100);
					}else{
						mPhotoSwitcherLeftArrow.setAlpha(255);
						mPhotoSwitcherRightArrow.setAlpha(255);
					}
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
			try{
				String imagepath = getRealPathFromURI(data.getData());
				ExifInterface exif = new ExifInterface(imagepath);
				exif.getAttribute(ExifInterface.TAG_ORIENTATION);
				File mPhotoFile = new File(imagepath);
				if(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1) == ExifInterface.ORIENTATION_NORMAL){ // If captured picture not rotated
					StoreDetailsAsyncTask mStorePhotosTask = new StoreDetailsAsyncTask(StoreOwner_Photos.this,"AddPhoto",null,"PROGRESS",mPhotoFile);
					if(Build.VERSION.SDK_INT >= 11)
						mStorePhotosTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
					else
						mStorePhotosTask.execute("");
				}else{ // If Rotated, revoke to original position...
					System.gc();
					Bitmap bitmap = decodeImage(imagepath);
					ByteArrayOutputStream outputstream=new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG,100,outputstream);
					byte[] imagedata=outputstream.toByteArray();
					bitmap.recycle();
					bitmap = null;
					FileOutputStream mStorePhotoFileStream = new FileOutputStream(mPhotoFile);
					mStorePhotoFileStream.write(imagedata);
					imagedata = null;
					StoreDetailsAsyncTask mStorePhotosTask = new StoreDetailsAsyncTask(StoreOwner_Photos.this,"AddPhoto",null,"PROGRESS",mPhotoFile);
					if(Build.VERSION.SDK_INT >= 11)
						mStorePhotosTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
					else
						mStorePhotosTask.execute("");
					mStorePhotoFileStream.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	// To get Image path
	public String getRealPathFromURI(Uri contentUri) 
	{
		String[] proj = { MediaStore.Audio.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	// To rotate Image if rotated and convert to bitmap 
	private Bitmap decodeImage(String imagepath){
		Bitmap bmp = null;
		int orientation;
		try{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inDither=false;                     //Disable Dithering mode
			options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
			options.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imagepath, options);
			//Decode with inSampleSize
			BitmapFactory.Options options_new = new BitmapFactory.Options();
			int widthpropotion = mImagePager.getWidth()/5;
			mImagePagerHeight = widthpropotion*5;
			options_new.inSampleSize = calculateInSampleSize(options,1000, 800);
			options_new.inJustDecodeBounds=false;
			bmp= BitmapFactory.decodeFile(imagepath,options_new);
			bmp = Bitmap.createScaledBitmap(bmp,1000,800, true);
			ExifInterface exif = new ExifInterface(imagepath);
			orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			Matrix m = new Matrix();
			if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
				m.postRotate(180,0,0);
				bmp = Bitmap.createBitmap(bmp, 0, 0, 1000,	800, m, true);
				return bmp;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				m.postRotate(90,0,0); 
				bmp = Bitmap.createBitmap(bmp, 0, 0, 1000,	800, m, true);
				return bmp;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				m.postRotate(270);
				bmp = Bitmap.createBitmap(bmp, 0, 0, 1000,	800, m, true);
				return bmp;
			} 
			return bmp;
		}catch(Exception e){
			e.printStackTrace();
			return bmp;
		}
	}

	// To calculate In sample size to avoid OOM
	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	/* To show alert pop up with respective message */
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
		mNotificationSync = new ScheduleNotificationSync(StoreOwner_Photos.this,ZouponsConstants.sStoreModuleFlag);
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
		if(WebServiceStaticArrays.mStorePhoto.size() > 0){
			int width=(mStoreOwnerPhotosGridView.getWidth()-20)/3;
			mStoreOwnerPhotosGridView.setAdapter(new StorePhotoGridAdapter(StoreOwner_Photos.this,width));
			mImagePagerWidth = mImagePager.getWidth();
			mImagePagerHeight = mImagePager.getHeight();
		}
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreOwner_Photos.this);
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
