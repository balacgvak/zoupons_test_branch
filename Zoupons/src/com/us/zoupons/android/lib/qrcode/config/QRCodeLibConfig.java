package com.us.zoupons.android.lib.qrcode.config;

import java.io.Serializable;

public class QRCodeLibConfig implements Serializable{
	
    private static final long  serialVersionUID = -718816522243056216L;

    public static final String INTENT_KEY = "zxingLibConfig";

    public boolean copyToClipboard = false;
    public boolean vibrateOnDecoded = false;
    public boolean playBeepOnDecoded = true;
    public boolean useFrontLight = false;
    public boolean reverseImage = false;
}