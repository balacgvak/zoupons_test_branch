package com.us.zoupons.VideoPlay;

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
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.us.zoupons.R;
import com.us.zoupons.LogoutTimer.CheckLogoutSession;
import com.us.zoupons.SessionTimeOut.CheckUserSession;
import com.us.zoupons.SessionTimeOut.RefreshZoupons;

public class VideoDialogActivity extends Activity{

	VideoView mVideoView;
	String URLValue;
	ProgressDialog mProgressDialog;
	public static String TAG="VideoDialogActivity";
	//Logout without user interaction after 1 minute
	CheckLogoutSession mLogoutSession;
	MediaController mediaController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.videodialog);	
		//Get Values as Extra
		URLValue =getIntent().getStringExtra("VIDEO");				

		mProgressDialog = new ProgressDialog(VideoDialogActivity.this);
		mProgressDialog.setMessage("Buffering Video...");
		mProgressDialog.setCancelable(false);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		mProgressDialog.show();	

		try{
			mVideoView = (VideoView)findViewById(R.id.mPlayVideoDialog);
			mediaController = new MediaController(VideoDialogActivity.this);
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
							Log.i("video current position", " "+mVideoView.getCurrentPosition());
							if(mVideoView.getCurrentPosition() > 0)
								mProgressDialog.dismiss();
						}
					});
					mVideoView.start();
				}
			});

			mVideoView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					Log.i("Dialog", "Dialog Video Completed");
					finish();
				}
			});

			mVideoView.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					Log.i("Dialog", "Dialog Video Error");
					finish();
					return false;
				}
			});
		}catch(Exception e){
			mProgressDialog.dismiss();
			System.out.println("Video Play Erro : "+e.getMessage());
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onUserLeaveHint() {
		new RefreshZoupons().isApplicationGoneBackground(VideoDialogActivity.this);
		mLogoutSession.cancelAlarm();	//To cancel alarm when application goes background
		super.onUserLeaveHint();
	}

	@Override
	protected void onResume() {
		new CheckUserSession(VideoDialogActivity.this).checkIfSessionExpires();
		//To start Logout session
		mLogoutSession = new CheckLogoutSession(VideoDialogActivity.this);
		mLogoutSession.setLogoutTimerAlarm();

		super.onResume();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		Log.i("user interaction", "detected");
		// Cancel and restart alarm since user interaction is detected..
		mLogoutSession.cancelAlarm();
		mLogoutSession.setLogoutTimerAlarm();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(mVideoView.isPlaying()){
			Log.i("Video", "Is Playing");
			mVideoView.stopPlayback();
			finish();
		}
	}
}
