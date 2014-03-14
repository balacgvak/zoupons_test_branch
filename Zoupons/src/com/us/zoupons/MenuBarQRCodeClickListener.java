package com.us.zoupons;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.us.zoupons.android.lib.integrator.IntentIntegrator;
import com.us.zoupons.android.lib.qrcode.config.QRCodeLibConfig;
import com.us.zoupons.flagclasses.MenuBarFlag;

/**
 * 
 * Helper class to open QR code scanner when we tap QR in Footer view
 *
 */

public class MenuBarQRCodeClickListener implements OnClickListener{

	private LinearLayout mBrowse,mSearch,mList;
	private QRCodeLibConfig mQrcodeLibConfig = new QRCodeLibConfig();
	private Activity mSlidingViewContext,mzpay_step1Context;
	private int mSetBackgroundResource;
	private String mLoadingFlag="";
    
	public MenuBarQRCodeClickListener(Context context,Activity slidingview,LinearLayout menubrowse,LinearLayout menusearch,LinearLayout menulist,int backgroundresource){
		this.mSlidingViewContext=slidingview;
		mQrcodeLibConfig.useFrontLight = true;
		this.mBrowse=menubrowse;
		this.mSearch=menusearch;
		this.mList=menulist;
		this.mSetBackgroundResource=backgroundresource;
	}

	public MenuBarQRCodeClickListener(Context context,Activity zpay_step1){
		this.mzpay_step1Context=zpay_step1;
		mQrcodeLibConfig.useFrontLight = true;
		mLoadingFlag="zpay";
	}
	
	@Override
	public void onClick(View v) {
		MenuBarFlag.mMenuBarFlag=2;
		if(!mLoadingFlag.equals("zpay")){
			if(this.mSetBackgroundResource==0){
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
			}
			this.mBrowse.setBackgroundResource(R.drawable.header_2);
			this.mSearch.setBackgroundResource(R.drawable.header_2);
			this.mList.setBackgroundResource(R.drawable.header_2);
			IntentIntegrator.initiateScan(this.mSlidingViewContext, mQrcodeLibConfig);
		}else{
			IntentIntegrator.initiateScan(this.mzpay_step1Context, mQrcodeLibConfig);
		}
	}
}
