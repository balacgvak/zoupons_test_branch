package com.us.zoupons.android.lib.integrator;

import android.app.Activity;
import android.content.Intent;

import com.us.zoupons.android.lib.qrcode.CaptureActivity;
import com.us.zoupons.android.lib.qrcode.Intents;
import com.us.zoupons.android.lib.qrcode.config.QRCodeLibConfig;

public final class IntentIntegrator {
    public static final int REQUEST_CODE = 1000;

    private IntentIntegrator() {
    }

    /**
     * start to scan with default formats and character set.
     * 
     * @param activity
     * @see #initiateScan(Activity, String, String)
     */
    public static void initiateScan(Activity activity) {
        initiateScan(activity, null, null, null);
    }

    /**
     * start to scan with default formats and character set.
     * 
     * @param activity
     * @param config can be null
     * @see #initiateScan(Activity, String, String)
     */
    public static void initiateScan(Activity activity, QRCodeLibConfig config) {
        initiateScan(activity, null, null, config);
    }

    public static void initiateScan(Activity activity, String scanFormatsString, String characterSet, QRCodeLibConfig config) {
        Intent intent = new Intent(activity, CaptureActivity.class);
        intent.putExtra(Intents.Scan.FORMATS, scanFormatsString);
        intent.putExtra(Intents.Scan.CHARACTER_SET, characterSet);
        intent.putExtra(QRCodeLibConfig.INTENT_KEY, config);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static IntentResult parseActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra(Intents.Scan.RESULT);
                String formatName = intent.getStringExtra(Intents.Scan.RESULT_FORMAT);
                return new IntentResult(contents, formatName);
            } else {
                return new IntentResult(null, null);
            }
        }
        return null;
    }
}