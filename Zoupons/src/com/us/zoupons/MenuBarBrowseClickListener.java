package com.us.zoupons;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.classvariables.Categories_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.MenuBarFlag;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Helper class to populate categories when we tap Browse in homepage and zpaystep1 footer view
 *
 */

public class MenuBarBrowseClickListener implements OnClickListener{

	private String mTAG="Browse";
	private Context mMenuBarContext;
	private LinearLayout mQRCode,mSearch,mList;
	private NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private ProgressDialog mProgressDialog=null;
	private LinearLayout mView;
	private boolean mMessageFlag;
	
	public MenuBarBrowseClickListener(Context context,TextView menubarfirsttext,LinearLayout menuqrcode,LinearLayout menusearch,LinearLayout menulist){
		this.mMenuBarContext=context;
    	this.mQRCode=menuqrcode;
		this.mSearch=menusearch;
		this.mList=menulist;
		this.mConnectionAvailabilityChecking= new NetworkCheck();
		this.mZouponswebservice = new ZouponsWebService(context);
		this.mParsingclass = new ZouponsParsingClass(this.mMenuBarContext);
		mProgressDialog=new ProgressDialog(this.mMenuBarContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading Categories...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(100);
	}
	
	@Override
	public void onClick(View v) {
		 MenuBarFlag.mMenuBarFlag=1;
		this.mView=(LinearLayout) v;
		v.setBackgroundResource(R.drawable.footer_dark_blue_new);
		this.mQRCode.setBackgroundResource(R.drawable.header_2);
		this.mSearch.setBackgroundResource(R.drawable.header_2);
		this.mList.setBackgroundResource(R.drawable.header_2);
			mProgressDialog.show();
			final Thread syncThread = new Thread(new Runnable() {

				@Override
				public void run() {
					Bundle bundle = new Bundle();
					String mGetResponse=null;
					Message msg_response = new Message();
					try{
						if(mConnectionAvailabilityChecking.ConnectivityCheck(mMenuBarContext)){
							mGetResponse=mZouponswebservice.categories();
							if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
								String mParsingResponse=mParsingclass.parseCategories(mGetResponse);
								if(mParsingResponse.equalsIgnoreCase("success")){
									for(int i=0;i<WebServiceStaticArrays.mCategoriesList.size();i++){
										Categories_ClassVariables parsedobjectvalues = (Categories_ClassVariables) WebServiceStaticArrays.mCategoriesList.get(i);
										if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("No Categories Available")*/){
											mMessageFlag=true;
											updateHandler(bundle, msg_response, "No Categories Available");	//send update to handlera
										}
									}
									if(!mMessageFlag){
										updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
									}
								}else if(mParsingResponse.equalsIgnoreCase("failure")){
									updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
									Log.i(mTAG,"Error");
								}else if(mParsingResponse.equalsIgnoreCase("norecords")){
									updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
									Log.i(mTAG,"No Records");
								}
							}else{
								updateHandler(bundle, msg_response, mGetResponse);	//send update to handler about emailid validation response
							}
						}else{
							Toast.makeText(mMenuBarContext, "No Network Connection", Toast.LENGTH_SHORT).show();
						}
					}catch(Exception e){
						Log.i(mTAG,"Thread Error");
						e.printStackTrace();
					}
					mProgressDialog.setProgress(100);
					mProgressDialog.dismiss();
				}
			});syncThread.start();
	}
	
	// To send message to handler to update UI elements
	private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse){
		bundle.putString("returnResponse", mGetResponse);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}
	
	// Handler class to update UI items
	Handler handler_response = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			String key = b.getString("returnResponse");
			if(key.trim().equals("success")){
				Log.i(mTAG,"Response Get Successfully");
				ShopperHomePage sv = new ShopperHomePage();
				sv.contextMenuOpen(mView,1);
			}else if(key.trim().equals("No Categories Available")){
				alertBox_service("Information","No Categories Available");
			}else if(key.trim().equals("failure")){
				alertBox_service("Information","Response Error");
			}else if(key.trim().equals("noresponse")||key.trim().equals("norecords")){
				alertBox_service("Information","No Data Available");
			}else{
				alertBox_service("Information","No Data Available");
			}
		}
	};
	
	// Funtion to show alert pop up with respective message
	private void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mMenuBarContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}
