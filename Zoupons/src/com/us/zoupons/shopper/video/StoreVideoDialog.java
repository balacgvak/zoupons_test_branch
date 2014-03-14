package com.us.zoupons.shopper.video;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.us.zoupons.R;
import com.us.zoupons.loginsession.CheckLogoutSession;
import com.us.zoupons.loginsession.CheckUserSession;
import com.us.zoupons.loginsession.RefreshZoupons;

/**
 * 
 * Activity to stream store video
 *
 */

public class StoreVideoDialog extends Activity{

	// Initializing views and variables
	private VideoView mVideoView;
	private String URLValue;
	private ProgressDialog mProgressDialog;
	//Logout without user interaction after 5 minute
	private CheckLogoutSession mLogoutSession;
	private MediaController mediaController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videodialog);	
		//Get Values as Extra
		URLValue =getIntent().getStringExtra("VIDEO");				
		// To show progess until video starts Buffer
		mProgressDialog = new ProgressDialog(StoreVideoDialog.this);
		mProgressDialog.setMessage("Buffering Video...");
		mProgressDialog.setCancelable(false);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		mProgressDialog.show();	
		try{
			// Referencing view from layout file
			mVideoView = (VideoView)findViewById(R.id.mPlayVideoDialog);
			mediaController = new MediaController(StoreVideoDialog.this);
			mediaController.setAnchorView(mVideoView);
			Uri video = Uri.parse(URLValue);
			mVideoView.setMediaController(mediaController);
			mVideoView.setVideoURI(video);

			mVideoView.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

						@Override
						public void onBufferingUpdate(MediaPlayer mp, int percent) {
							// TODO Auto-generated method stub
							if(mVideoView.getCurrentPosition() > 0)  // If starts Buffer
								mProgressDialog.dismiss();
						}
					});
					mVideoView.start();
				}
			});

			mVideoView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					finish(); // After completion finish the activity
				}
			});

			mVideoView.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					finish(); // If any Error finish the activity
					return false;
				}
			});
		}catch(Exception e){
			mProgressDialog.dismiss();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		new RefreshZoupons().isApplicationGoneBackground(StoreVideoDialog.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
	}

	@Override
	protected void onResume() {
		super.onResume();
		new CheckUserSession(StoreVideoDialog.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(StoreVideoDialog.this);
		mLogoutSession.setLogoutTimerAlarm();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// To notify  system that its time to run garbage collector service
		System.gc();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(mVideoView.isPlaying()){
			mVideoView.stopPlayback();
			finish();
		}
	}
}
