package com.us.zoupons.android.lib.qrcode;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.us.zoupons.R;
import com.us.zoupons.android.lib.qrcode.camera.CameraManager;
import com.us.zoupons.android.lib.qrcode.config.QRCodeLibConfig;

@SuppressWarnings("deprecation")
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback{

    private static final String TAG = CaptureActivity.class.getSimpleName();

    private static final long INTENT_RESULT_DURATION = 1500L;

    private CaptureActivityHandler handler;
    private ViewfinderView         viewfinderView;
    private TextView               statusView;
    private boolean                hasSurface,mIsHandSet,mIsTablet;
    private Vector<BarcodeFormat>  decodeFormats;
    private String                 characterSet;
    private InactivityTimer        inactivityTimer;
    private BeepManager            beepManager;
    private QRCodeLibConfig        config;
    private int CameraPreviewRotation = 0;
    
    ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.qrcodelib_capture);

        Intent intent = getIntent();
        if (intent != null) {
            config = (QRCodeLibConfig) intent.getSerializableExtra(QRCodeLibConfig.INTENT_KEY);
        }
        if (config == null) {
            config = new QRCodeLibConfig();
        }

        boolean xlarge = ((this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		// Check for handset or tablet so that corresponding rotation is set.
		if (xlarge == true || large == true) {
			mIsTablet = true;
		} else {
			mIsHandSet = true;
		}
		
		// Setting the image rotation value depending upon orientation change
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (mIsHandSet) {
				CameraPreviewRotation = 90;
			}
			if (mIsTablet) {
				switch (getWindowManager().getDefaultDisplay().getRotation()) {
				case Surface.ROTATION_90:
					CameraPreviewRotation = 270;
					break;
				case Surface.ROTATION_270:
					CameraPreviewRotation = 90;
					break;
				}
			}
		}else {
			if (mIsHandSet) {
				if (getWindowManager().getDefaultDisplay().getRotation() == 1) {
					CameraPreviewRotation = 0;
				} else {
					CameraPreviewRotation = 180;
				}
			} else {
				switch (getWindowManager().getDefaultDisplay()
						.getRotation()) {
				case Surface.ROTATION_0:
					CameraPreviewRotation = 0;
					break;
				case Surface.ROTATION_90:
					CameraPreviewRotation = 90;
					break;
				case Surface.ROTATION_180:
					CameraPreviewRotation = 180;
					break;
				case Surface.ROTATION_270:
					CameraPreviewRotation = 270;
					break;
				}
			}
		}
        
        CameraManager.init(getApplication(), config, CameraPreviewRotation);
        viewfinderView = (ViewfinderView) findViewById(R.id.qrcodelib_viewfinder_view);
        statusView = (TextView) findViewById(R.id.qrcodelib_status_view);
        handler = null;
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this, config);
    } 

    @Override
    protected void onResume() {
        super.onResume();
        resetStatusView();

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.qrcodelib_preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        Intent intent = getIntent();
        decodeFormats = null;
        characterSet = null;
        if (intent != null) {
            characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);
            // Scan the formats the intent requested, and return the result to the calling activity.
            decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
            if (intent.hasExtra(Intents.Scan.WIDTH) && intent.hasExtra(Intents.Scan.HEIGHT)) {
                int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
                int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
                if (width > 0 && height > 0) {
                    CameraManager.get().setManualFramingRect(width, height);
                }
            }
        }

        beepManager.updatePrefs();
        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
            // Handle these events so they don't launch the Camera app
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     * 
     * @param rawResult The contents of the barcode.
     * @param barcode A greyscale bitmap of the camera data which was decoded.
     */
    @SuppressWarnings("deprecation")
	public void handleDecode(Result rawResult, Bitmap barcode) {
        inactivityTimer.onActivity();

        beepManager.playBeepSoundAndVibrate();
        drawResultPoints(barcode, rawResult);

        viewfinderView.drawResultBitmap(barcode);

        String resultContent = rawResult.toString();
        if (config.copyToClipboard) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(resultContent);
        }

        Intent intent = new Intent(getIntent().getAction());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intents.Scan.RESULT, resultContent);
        intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult.getBarcodeFormat().toString());
        byte[] rawBytes = rawResult.getRawBytes();
        if (rawBytes != null && rawBytes.length > 0) {
            intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
        }
        Message message = Message.obtain(handler, R.id.qrcode_return_scan_result);
        message.obj = intent;
        handler.sendMessageDelayed(message, INTENT_RESULT_DURATION);
    }

    /**
     * Superimpose a line for 1D or dots for 2D to highlight the key features of
     * the barcode.
     * 
     * @param barcode A bitmap of the captured image.
     * @param rawResult The decoded results which contains the points to draw.
     */
    private void drawResultPoints(Bitmap barcode, Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.qrcode_result_image_border));
            paint.setStrokeWidth(3.0f);
            paint.setStyle(Paint.Style.STROKE);
            Rect border = new Rect(2, 2, barcode.getWidth() - 2, barcode.getHeight() - 2);
            canvas.drawRect(border, paint);

            paint.setColor(getResources().getColor(R.color.qrcode_result_points));
            if (points.length == 2) {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1]);
            } else if (points.length == 4
                    && (rawResult.getBarcodeFormat().equals(BarcodeFormat.UPC_A) || rawResult
                            .getBarcodeFormat().equals(BarcodeFormat.EAN_13))) {
                // Hacky special case -- draw two lines, for the barcode and metadata
                drawLine(canvas, paint, points[0], points[1]);
                drawLine(canvas, paint, points[2], points[3]);
            } else {
                paint.setStrokeWidth(10.0f);
                for (ResultPoint point : points) {
                    canvas.drawPoint(point.getX(), point.getY(), paint);
                }
            }
        }
    }

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b) {
        canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            // displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializating camera", e);
            // displayFrameworkBugMessageAndExit();
        }
    }

    private void resetStatusView() {
        statusView.setText(R.string.QRCodeScanner_msg_default_status);
        statusView.setVisibility(View.VISIBLE);
        viewfinderView.setVisibility(View.VISIBLE);
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }
}
