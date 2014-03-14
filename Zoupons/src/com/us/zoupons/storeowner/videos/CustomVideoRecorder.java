package com.us.zoupons.storeowner.videos;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;

/**
 * 
 * Activity which displays custom camera preview to record store videos
 *
 */

public class CustomVideoRecorder extends Activity implements SurfaceHolder.Callback{

	// Initializing views and variables
	private Camera mCamera;
	private MediaRecorder mMediarecoder;
	private SurfaceView mPreviewView;
	private SurfaceHolder mPreviewHolder;
	private Button mRecord_StopButton,mPause_Resume_PlayButton,mUploadButton;
	private TextView mCountDownTimerTextview;
	private ImageView mVideoThumbnail,mVideoPlay;
	private String mTimeStamp ="";
	private RelativeLayout mVideoPreviewContainer;
	private LinearLayout mFooterContainer;
	private VideoView mVideoPlaybackView;
	private MediaController mediaController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videorecorder);
		try{
			// Referencing view from layout file
			mPreviewView = (SurfaceView)findViewById(R.id.mSurfaceViewId);
			mCountDownTimerTextview = (TextView) findViewById(R.id.mCameraPreviewTimerId);
			mPreviewHolder = mPreviewView.getHolder();
			mPreviewHolder.addCallback(this);
			mPreviewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			mFooterContainer = (LinearLayout) findViewById(R.id.mCameraPreviewFooterLayout);
			mRecord_StopButton = (Button) findViewById(R.id.record_stop_buttonId);
			mPause_Resume_PlayButton = (Button) findViewById(R.id.pause_buttonId);
			mUploadButton = (Button) findViewById(R.id.play_buttonId);
			mVideoPreviewContainer = (RelativeLayout) findViewById(R.id.videopreview_container);
			mVideoThumbnail = (ImageView) findViewById(R.id.video_thumbnailId);
			mVideoPlay = (ImageView) findViewById(R.id.mPlayButton);
			mVideoPlaybackView = (VideoView) findViewById(R.id.mPlayVideoDialog);
			mediaController = new MediaController(CustomVideoRecorder.this);
			mediaController.setAnchorView(mVideoPlaybackView);
			mVideoPlaybackView.setMediaController(mediaController);

			mRecord_StopButton.setOnClickListener(new OnClickListener() {

				@SuppressLint("InlinedApi")
				@Override
				public void onClick(View v) {
					try{
						if(mRecord_StopButton.getText().toString().equalsIgnoreCase("Record")){ // Record
							if (mCamera == null)
								mCamera = Camera.open();  // To open global Camera
							if(mVideoPreviewContainer.getVisibility() == View.VISIBLE){ // To hide preview container  
								mPreviewView.setVisibility(View.VISIBLE);
								mCountDownTimerTextview.setVisibility(View.VISIBLE);
								mCountDownTimerTextview.setText("00:00");
								mVideoPreviewContainer.setVisibility(View.GONE);
								mFooterContainer.setBackgroundColor(0);
								File file = new File("/sdcard/vid_"+mTimeStamp+".mp4");
								if(file != null && file.exists())
									file.delete();
								mUploadButton.setVisibility(View.INVISIBLE);
							}else{ // To start recording
								if(mPreviewView.getVisibility() == View.GONE){
									mPreviewView.setVisibility(View.VISIBLE);
									mCountDownTimerTextview.setVisibility(View.VISIBLE);
									mVideoPreviewContainer.setVisibility(View.GONE);
									mFooterContainer.setBackgroundColor(0);
									File file = new File("/sdcard/vid_"+mTimeStamp+".mp4");
									if(file != null && file.exists())
										file.delete();
								}
								mMediarecoder = new MediaRecorder();
								// Step 1: Unlock and set camera to MediaRecorder
								mCamera.stopPreview();
								mCamera.unlock();
								// Step 2: Set sources
								mMediarecoder.setCamera(mCamera);

								mMediarecoder.setAudioSource(MediaRecorder.AudioSource.MIC);
								mMediarecoder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
								//Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
								/*if(android.os.Build.VERSION.SDK_INT < 11){
									mMediarecoder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
								}else{*/
									if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P)) {
										mMediarecoder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
									} else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_CIF)) {
										mMediarecoder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_CIF));
									} else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_QCIF)) {
										mMediarecoder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QCIF));
									}else{
										mMediarecoder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
									}
								//}
								mTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
								// Step 4: Set output file
								mMediarecoder.setOutputFile("/sdcard/vid_"+mTimeStamp+".mp4");
								// Step 5: Set the preview output
								mMediarecoder.setPreviewDisplay(mPreviewHolder.getSurface());
								// Step 6: Prepare configured MediaRecorder
								try {
									mMediarecoder.prepare();
									mCountDownTimer.start();
								} catch (IllegalStateException e) {
									e.printStackTrace();
									releaseMediaRecorder();
								} catch (IOException e) {
									e.printStackTrace();
									releaseMediaRecorder();
								}
								mMediarecoder.start();
								mRecord_StopButton.setText("Stop");
								mUploadButton.setVisibility(View.INVISIBLE);
							}
						}else{ // stop videos
							StopRecording();
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});

			mPause_Resume_PlayButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mPause_Resume_PlayButton.getText().toString().equalsIgnoreCase("Pause")){
						mPause_Resume_PlayButton.setText("Resume");
					}else{
						mPause_Resume_PlayButton.setText("Pause");
					}
				}
			});

			mUploadButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					if(new NetworkCheck().ConnectivityCheck(CustomVideoRecorder.this)){ // network check..
						UploadVideoToServerTask mUploadTask = new UploadVideoToServerTask(CustomVideoRecorder.this, new File("/sdcard/vid_"+mTimeStamp+".mp4"),"Camera");
						mUploadTask.execute();	
					}else{
						Toast.makeText(CustomVideoRecorder.this, "Network connection not available", Toast.LENGTH_SHORT).show();
					}
				}
			});

			mVideoPlay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mVideoPlaybackView.setVisibility(View.VISIBLE);
					mVideoThumbnail.setVisibility(View.GONE);
					mVideoPlay.setVisibility(View.GONE);
					mVideoPlaybackView.setVideoURI(Uri.parse("/sdcard/vid_"+mTimeStamp+".mp4"));
					mVideoPlaybackView.start();
				}
			});

			mVideoPlaybackView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					mVideoPlaybackView.setVisibility(View.GONE);
					mVideoThumbnail.setVisibility(View.VISIBLE);
					mVideoPlay.setVisibility(View.VISIBLE);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	// To start countdown timer for 10 seconds 
	CountDownTimer mCountDownTimer = new CountDownTimer(11000, 1000) {
		@Override
		public void onFinish() {
			StopRecording();
		}

		public void onTick(long millisUntilFinished) {
			try{
				String hms = String.format("%02d:%02d",TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
						TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
				mCountDownTimerTextview.setText(hms);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		// Initializing Camera class
		if (mCamera == null && mPreviewView.getVisibility() == View.VISIBLE) {
			mCamera = Camera.open();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// If Camera is in use, then release camera
		if(mCamera!=null && mPreviewView.getVisibility() == View.VISIBLE)
		{
			releaseMediaRecorder();
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// If Camera is in use, then release camera
		if(mCamera!=null && mPreviewView.getVisibility() == View.VISIBLE){
			releaseMediaRecorder();
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
		if(mCountDownTimer != null)
			mCountDownTimer.cancel();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// To notify  system that its time to run garbage collector service
		System.gc();
	}

	private void releaseMediaRecorder(){
		if (mMediarecoder != null) {
			mMediarecoder.reset();   // clear recorder configuration
			mMediarecoder.release(); // release the recorder object
			mMediarecoder = null;
			mCamera.lock();           // lock camera for later use
		}
	}

	/*To stop video recording and release camera resource */
	private void StopRecording(){
		try{
			mMediarecoder.stop();
			releaseMediaRecorder();
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
			// To broadcast action to system for updating Gallery.			
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
			mRecord_StopButton.setText("Record");
			mCountDownTimer.cancel();
			mPause_Resume_PlayButton.setVisibility(View.GONE);
			mUploadButton.setVisibility(View.VISIBLE);
			mPreviewView.setVisibility(View.GONE);
			mCountDownTimerTextview.setVisibility(View.GONE);
			mVideoPreviewContainer.setVisibility(View.VISIBLE);
			mFooterContainer.setBackgroundColor(getResources().getColor(R.color.custombackground));
			// To create video thumbnail
			Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail("/sdcard/vid_"+mTimeStamp+".mp4",Thumbnails.FULL_SCREEN_KIND);
			mVideoThumbnail.setImageBitmap(bmThumbnail);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	@SuppressLint("InlinedApi")
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		try{
			if (mPreviewHolder.getSurface() == null) {
				return;
			}
			// Setting supported preview size.
			if(mCamera!=null && mPreviewView.getVisibility() == View.VISIBLE){
				Camera.Parameters mCameraParameters = mCamera.getParameters();
				List<String> focusModes = mCameraParameters.getSupportedFocusModes();
				if (focusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
					mCameraParameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
				Camera.Size mCameraSize = getBestSize(width, height, mCameraParameters);
				if (mCameraSize != null) {
					mCameraParameters.setPreviewSize(mCameraSize.width,mCameraSize.height);
					mCamera.setParameters(mCameraParameters);
					mCamera.startPreview();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if(mCamera!=null && mPreviewView.getVisibility() == View.VISIBLE)
				mCamera.setPreviewDisplay(mPreviewHolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	// To get best preview screen size.
	private Size getBestSize(int width, int height, Parameters mCameraParameters) {
		Camera.Size result = null;
		for (Camera.Size size : mCameraParameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;
					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}
		return result;
	}
}