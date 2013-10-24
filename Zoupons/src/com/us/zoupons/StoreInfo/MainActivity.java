package com.us.zoupons.StoreInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.ClassVariables.POJOStorePhoto;
import com.us.zoupons.ClassVariables.POJOStoreTiming;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class MainActivity extends MainMenuActivity {

	com.us.zoupons.MyHorizontalScrollView scrollView;
	View menu;
	View app;
	LinearLayout btnSlide;
	private TextView mStoreName,mStorePhoneNumber,mStoreWebsite,mStoreDescription,mMondayBusinessStartTime,mTuesdayBusinessStartTime,mWednesdayBusinessStartTime,mThursdayBusinessStartTime,mFridayBusinessStartTime,mSaturdayBusinessStartTime,mSundayBusinessStartTime,
	mMondayBusinessEndTime,mTuesdayBusinessEndTime,mWednesdayBusinessEndTime,mThursdayBusinessEndTime,mFridayBusinessEndTime,mSaturdayBusinessEndTime,mSundayBusinessEndTime,mVideoText;
	private GridView mStorePhotoGrid;
	private ImageView mStoreVideoGrid,mStoreVideoPlayImage;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private ProgressDialog progressdialog=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		storeInfo(R.layout.store_information);
	}

	public class BusinessTimeClass extends AsyncTask<String, String, String>{
		Context ctx;
		String mResponse;
		public NetworkCheck mConnectionAvailabilityChecking;

		public BusinessTimeClass(Context context,TextView MondayBusinessTime, TextView TuesdayBusinessTime,
				TextView WednesdayBusinessTime,TextView ThursdayBusinessTime, TextView FridayBusinessTime,TextView SaturdayBusinessTime, TextView SundayBusinessTime, TextView MondayBusinessEndTime, TextView TuesdayBusinessEndTime, TextView WednesdayBusinessEndTime, TextView ThursdayBusinessEndTime, TextView FridayBusinessEndTime, TextView SaturdayBusinessEndTime, TextView SundayBusinessEndTime) {
			this.ctx = context;
			zouponswebservice= new ZouponsWebService(context);
			parsingclass= new ZouponsParsingClass(this.ctx);
			mConnectionAvailabilityChecking = new NetworkCheck();
			progressdialog=new ProgressDialog(context);
			progressdialog.setCancelable(true);
			progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressdialog.setProgress(0);
			progressdialog.setMax(100);
			mMondayBusinessStartTime = MondayBusinessTime;
			mTuesdayBusinessStartTime = TuesdayBusinessTime;
			mWednesdayBusinessStartTime = WednesdayBusinessTime;
			mThursdayBusinessStartTime = ThursdayBusinessTime;
			mFridayBusinessStartTime = FridayBusinessTime;
			mSaturdayBusinessStartTime = SaturdayBusinessTime;
			mSundayBusinessStartTime = SundayBusinessTime;
			mMondayBusinessEndTime = MondayBusinessEndTime;
			mTuesdayBusinessEndTime = TuesdayBusinessEndTime;
			mWednesdayBusinessEndTime = WednesdayBusinessEndTime;
			mThursdayBusinessEndTime = ThursdayBusinessEndTime;
			mFridayBusinessEndTime = FridayBusinessEndTime;
			mSaturdayBusinessEndTime = SaturdayBusinessEndTime;
			mSundayBusinessEndTime = SundayBusinessEndTime;
		}

		@Override
		protected void onPreExecute() {
			((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
			//Start a status dialog
			progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String mresult ="",mParsingResponse=" ";
			try{
				if(mConnectionAvailabilityChecking.ConnectivityCheck(this.ctx)){
					mResponse = zouponswebservice.mStoreTimings(RightMenuStoreId_ClassVariables.mStoreID,RightMenuStoreId_ClassVariables.mLocationId);
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){
						mParsingResponse = parsingclass.parseTimeInfo(mResponse);
						if(!mParsingResponse.equals("failure") && !mParsingResponse.equals("norecords")){
							mresult = "success";
						}else if(mParsingResponse.equalsIgnoreCase("failure")){
							mresult = "failure";
						}else if(mParsingResponse.equalsIgnoreCase("norecords")){
							mresult="norecords";
						}
					}else{
						mresult ="noresponse";
					}
				}else{
					mresult = "nonetwork";
				}
			}catch(Exception e){
				mresult ="failure";
			}
			return mresult;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.i("result", "result");
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			if(result.equalsIgnoreCase("success")){
				POJOStoreTiming storeTiming =(POJOStoreTiming) WebServiceStaticArrays.mStoreTiming.get(0);

				if(storeTiming.is_monday_closed.equalsIgnoreCase("0")){
					mMondayBusinessStartTime.setText(storeTiming.monday_from);
					mMondayBusinessEndTime.setText("|  "+storeTiming.monday_to);
				}else{
					mMondayBusinessStartTime.setText("Closed");
				}

				if(storeTiming.is_tuesday_closed.equalsIgnoreCase("0")){
					mTuesdayBusinessStartTime.setText(storeTiming.tuesday_from);
					mTuesdayBusinessEndTime.setText("|  "+storeTiming.tuesday_to);
				}else{
					mTuesdayBusinessStartTime.setText("Closed");
				} 

				if(storeTiming.is_wednesday_closed.equalsIgnoreCase("0")){
					mWednesdayBusinessStartTime.setText(storeTiming.wednesday_from);
					mWednesdayBusinessEndTime.setText("|  "+storeTiming.wednesday_to);
				}else{
					mWednesdayBusinessStartTime.setText("Closed");
				}

				if(storeTiming.is_thursday_closed.equalsIgnoreCase("0")){
					mThursdayBusinessStartTime.setText(storeTiming.thursday_from);
					mThursdayBusinessEndTime.setText("|  "+storeTiming.thursday_to);
				}else{
					mThursdayBusinessStartTime.setText("Closed");
				}

				if(storeTiming.is_friday_closed.equalsIgnoreCase("0")){
					mFridayBusinessStartTime.setText(storeTiming.friday_from);
					mFridayBusinessEndTime.setText("|  "+storeTiming.friday_to);
				}else{
					mFridayBusinessStartTime.setText("Closed");
				}

				if(storeTiming.is_saturday_closed.equalsIgnoreCase("0")){
					mSaturdayBusinessStartTime.setText(storeTiming.saturday_from);
					mSaturdayBusinessEndTime.setText("|  "+storeTiming.saturday_to);
				}else{
					mSaturdayBusinessStartTime.setText("Closed");
				}

				if(storeTiming.is_sunday_closed.equalsIgnoreCase("0")){
					mSundayBusinessStartTime.setText(storeTiming.sunday_from);
					mSundayBusinessEndTime.setText("|  "+storeTiming.sunday_to);
				}else{
					mSundayBusinessStartTime.setText("Closed");
				}
			}else if(result.equalsIgnoreCase("failure")||result.equalsIgnoreCase("noresponse")){
				alertBox_service("Information", "Unable to reach service.",ctx);
			}else if(result.equalsIgnoreCase("nonetwork")){
				Toast.makeText(this.ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equalsIgnoreCase("norecords")){
				alertBox_service("Information", "BusinessTime not Available.",ctx);
			}		
		}
	}

	public class StoreInformationClass extends AsyncTask<String, String,String>{

		String TAG ="Store Information";
		Context ctx;
		String mGetResponse=null,mPhotoResponse = null;
		String mParsingResponse,mParsingPhotoResponse;
		public NetworkCheck mConnectionAvailabilityChecking;

		public StoreInformationClass(Context context,TextView storename,TextView storephonenumber,TextView storewebsite,
				TextView storedescription,GridView storephotogrid,ImageView storeVideoGrid, TextView storeVideoInfoText, ImageView storeVideoPlayImage) {
			this.ctx = context;
			zouponswebservice= new ZouponsWebService(context);
			parsingclass= new ZouponsParsingClass(this.ctx);
			mConnectionAvailabilityChecking = new NetworkCheck();
			progressdialog=new ProgressDialog(context);
			progressdialog.setCancelable(true);
			progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressdialog.setProgress(0);
			progressdialog.setMax(100);
			mStoreName=storename;
			mStorePhoneNumber=storephonenumber;
			mStoreWebsite=storewebsite;
			mStoreDescription=storedescription;
			mStorePhotoGrid=storephotogrid;
			mStoreVideoGrid=storeVideoGrid;
			mVideoText = storeVideoInfoText;
			mStoreVideoPlayImage = storeVideoPlayImage;
		}

		@Override
		protected void onPreExecute() {
			((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
			//Start a status dialog
			progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String result="";
			try{
				if(mConnectionAvailabilityChecking.ConnectivityCheck(this.ctx)){
					mGetResponse=zouponswebservice.mStoreInformation(RightMenuStoreId_ClassVariables.mStoreID,RightMenuStoreId_ClassVariables.mLocationId);	
					if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
						mParsingResponse=parsingclass.mParseStoreInfo(mGetResponse);
						if(mParsingResponse.equalsIgnoreCase("success")){
							result = "success";
						}else if(mParsingResponse.equalsIgnoreCase("failure")){
							result = "failure";

						}else if(mParsingResponse.equalsIgnoreCase("norecords")){
							result="norecords";
						}
					}else{
						result ="failure";
					}
				}else{
					result="nonetwork";
				}
			}catch(Exception e){
				result="failure";
				e.printStackTrace();
			}
			return result;
		}


		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			if(result.equalsIgnoreCase("success")){
				POJOStoreInfo mPOJO =(POJOStoreInfo) WebServiceStaticArrays.mStaticStoreInfo.get(0);
				mStoreName.setText(mPOJO.store_name);
				mStorePhoneNumber.setText(mPOJO.phone);
				mStoreWebsite.setText(mPOJO.website);
				mStoreDescription.setText(mPOJO.description);
				if(mPOJO.video_url.equalsIgnoreCase("")){
					mVideoText.setVisibility(View.VISIBLE);
					mStoreVideoGrid.setVisibility(View.GONE);
					mStoreVideoPlayImage.setVisibility(View.GONE);
				}else{
					mVideoText.setVisibility(View.GONE);
					mStoreVideoGrid.setVisibility(View.VISIBLE);
					mStoreVideoPlayImage.setVisibility(View.VISIBLE);
					mStoreVideoGrid.setImageBitmap(mPOJO.video_thumbnail);
				}
				if(WebServiceStaticArrays.mStorePhoto.size() < 3){
					Bitmap mZouponsLogo = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.appicon);
					switch(WebServiceStaticArrays.mStorePhoto.size())
					{
					case 0:
						for(int i=0;i<3;i++){
							POJOStorePhoto obj = new POJOStorePhoto();
							obj.thumb = mZouponsLogo;
							WebServiceStaticArrays.mStorePhoto.add(i,obj);
						}
						break;
					case 1:
						for(int i=0;i<3;i++){
							POJOStorePhoto obj = new POJOStorePhoto();
							obj.thumb = mZouponsLogo;
							WebServiceStaticArrays.mStorePhoto.add(1+i,obj);
						}
						break;
					case 2:
						for(int i=0;i<3;i++){
							POJOStorePhoto obj = new POJOStorePhoto();
							obj.thumb = mZouponsLogo;
							WebServiceStaticArrays.mStorePhoto.add(2+i,obj);
						}
						break;
					}
				}
			}else if(result.equalsIgnoreCase("failure")||result.equalsIgnoreCase("noresponse")){
				alertBox_service("Information", "Unable to reach service.",ctx);
			}else if(result.equalsIgnoreCase("nonetwork")){
				Toast.makeText(this.ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else if(result.equalsIgnoreCase("norecords")){
				alertBox_service("Information", "StoreInformation not Available.",ctx);
			}

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}

	public void alertBox_service(final String title,final String msg,Context context){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(context);
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

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

}
