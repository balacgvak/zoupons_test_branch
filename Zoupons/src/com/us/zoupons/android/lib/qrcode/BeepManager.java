package com.us.zoupons.android.lib.qrcode;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

import com.us.zoupons.R;
import com.us.zoupons.android.lib.qrcode.config.QRCodeLibConfig;

public final class BeepManager {

    private static final String TAG              = BeepManager.class.getSimpleName();

    private static final float  BEEP_VOLUME      = 0.10f;
    private static final long   VIBRATE_DURATION = 200L;

    private final Activity      activity;
    private MediaPlayer         mediaPlayer;
    private boolean             playBeep;
    private boolean             vibrate;
    private QRCodeLibConfig      config;

    BeepManager(Activity activity, QRCodeLibConfig config) {
        this.activity = activity;
        this.mediaPlayer = null;
        this.config = config;
        updatePrefs();
    }

    void updatePrefs() {
        playBeep = shouldBeep(config, activity);
        vibrate = config.vibrateOnDecoded;
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it too loud,
            // so we now play on the music stream.
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = buildMediaPlayer(activity);
        }
    }

    void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private static boolean shouldBeep(QRCodeLibConfig config, Context activity) {
        boolean shouldPlayBeep = config.playBeepOnDecoded;
        if (shouldPlayBeep) {
            // See if sound settings overrides this
            AudioManager audioService = (AudioManager) activity
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                shouldPlayBeep = false;
            }
        }
        return shouldPlayBeep;
    }

    private static MediaPlayer buildMediaPlayer(Context activity) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // When the beep has finished playing, rewind to queue up another one.
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                player.seekTo(0);
            }
        });

        AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.qrcodelib_beep);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
                    file.getLength());
            file.close();
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mediaPlayer.prepare();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            mediaPlayer = null;
        }
        return mediaPlayer;
    }
}
