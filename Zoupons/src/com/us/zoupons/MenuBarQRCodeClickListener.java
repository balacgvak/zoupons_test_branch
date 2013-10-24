package com.us.zoupons;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.us.zoupons.FlagClasses.MenuBarFlag;
import com.us.zoupons.android.lib.integrator.IntentIntegrator;
import com.us.zoupons.android.lib.qrcode.config.QRCodeLibConfig;

public class MenuBarQRCodeClickListener implements OnClickListener{

	Context MenuBarContext;
	private LinearLayout mBrowse,mSearch,mList;
	private QRCodeLibConfig qrcodeLibConfig = new QRCodeLibConfig();
	private Activity SlidingView,zpay_step1;
	private Handler handler = new Handler();
	private int setBackgroundResource;
	public static String loadingFlag="";
    
	public MenuBarQRCodeClickListener(Context context,Activity slidingview,LinearLayout menubrowse,LinearLayout menusearch,LinearLayout menulist,int backgroundresource){
		this.MenuBarContext=context;
		this.SlidingView=slidingview;
		qrcodeLibConfig.useFrontLight = true;
		this.mBrowse=menubrowse;
		this.mSearch=menusearch;
		this.mList=menulist;
		this.setBackgroundResource=backgroundresource;
	}

	public MenuBarQRCodeClickListener(Context context,Activity zpay_step1){
		this.MenuBarContext=context;
		this.zpay_step1=zpay_step1;
		qrcodeLibConfig.useFrontLight = true;
		loadingFlag="zpay";
	}
	
	@Override
	public void onClick(View v) {
		MenuBarFlag.mMenuBarFlag=2;
		if(!loadingFlag.equals("zpay")){
			if(this.setBackgroundResource==0){
				v.setBackgroundResource(R.drawable.footer_dark_blue_new);
			}
			this.mBrowse.setBackgroundResource(R.drawable.header_2);
			this.mSearch.setBackgroundResource(R.drawable.header_2);
			this.mList.setBackgroundResource(R.drawable.header_2);
			IntentIntegrator.initiateScan(this.SlidingView, qrcodeLibConfig);
		}else{
			IntentIntegrator.initiateScan(this.zpay_step1, qrcodeLibConfig);
		}
	}
}
