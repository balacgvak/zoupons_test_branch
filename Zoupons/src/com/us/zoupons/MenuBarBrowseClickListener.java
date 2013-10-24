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

import com.us.zoupons.ClassVariables.Categories_ClassVariables;
import com.us.zoupons.FlagClasses.MenuBarFlag;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class MenuBarBrowseClickListener implements OnClickListener{

	private String TAG="Browse";
	Context MenuBarContext;
	TextView menubarBrowseText;
	LinearLayout mQRCode,mSearch,mList;
	private NetworkCheck connectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private ProgressDialog mProgressDialog=null;
	private LinearLayout view;
	private boolean messageFlag;
	
	public MenuBarBrowseClickListener(Context context,TextView menubarfirsttext,LinearLayout menuqrcode,LinearLayout menusearch,LinearLayout menulist){
		this.MenuBarContext=context;
		this.menubarBrowseText=menubarfirsttext;
		this.mQRCode=menuqrcode;
		this.mSearch=menusearch;
		this.mList=menulist;
		this.connectionAvailabilityChecking= new NetworkCheck();
		this.zouponswebservice = new ZouponsWebService(context);
		this.parsingclass = new ZouponsParsingClass(this.MenuBarContext);
		mProgressDialog=new ProgressDialog(this.MenuBarContext);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading Categories...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(100);
	}
	
	@Override
	public void onClick(View v) {
		 MenuBarFlag.mMenuBarFlag=1;
		this.view=(LinearLayout) v;
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
						if(connectionAvailabilityChecking.ConnectivityCheck(MenuBarContext)){

							mGetResponse=zouponswebservice.categories();

							if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
								String mParsingResponse=parsingclass.parseCategories(mGetResponse);
								if(mParsingResponse.equalsIgnoreCase("success")){
									for(int i=0;i<WebServiceStaticArrays.mCategoriesList.size();i++){
										Categories_ClassVariables parsedobjectvalues = (Categories_ClassVariables) WebServiceStaticArrays.mCategoriesList.get(i);
										if(!parsedobjectvalues.message.equals("")/*&&parsedobjectvalues.message.equals("No Categories Available")*/){
											messageFlag=true;
											updateHandler(bundle, msg_response, "No Categories Available");	//send update to handlera
										}else{
											
										}
									}
									if(!messageFlag){
										updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
									}
								}else if(mParsingResponse.equalsIgnoreCase("failure")){
									updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
									Log.i(TAG,"Error");
								}else if(mParsingResponse.equalsIgnoreCase("norecords")){
									updateHandler(bundle, msg_response, mParsingResponse);	//send update to handler
									Log.i(TAG,"No Records");
								}
							}else{
								updateHandler(bundle, msg_response, mGetResponse);	//send update to handler about emailid validation response
							}
						}else{
							Toast.makeText(MenuBarContext, "No Network Connection", Toast.LENGTH_SHORT).show();
						}
					}catch(Exception e){
						Log.i(TAG,"Thread Error");
						e.printStackTrace();
					}
					mProgressDialog.setProgress(100);
					mProgressDialog.dismiss();
				}
			});syncThread.start();
	}
	
	private void updateHandler(Bundle bundle,Message msg_response,String mGetResponse){
		bundle.putString("returnResponse", mGetResponse);
		msg_response.setData(bundle);
		handler_response.sendMessage(msg_response);
	}
	
	Handler handler_response = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Bundle b = msg.getData();
			String key = b.getString("returnResponse");
			if(key.trim().equals("success")){
				Log.i(TAG,"Response Get Successfully");
				SlidingView sv = new SlidingView();
				sv.contextMenuOpen(view,1);
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
	
	private void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.MenuBarContext);
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
