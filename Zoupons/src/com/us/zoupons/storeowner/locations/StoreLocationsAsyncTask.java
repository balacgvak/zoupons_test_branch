package com.us.zoupons.storeowner.locations;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.ListView;

import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task to get location list from web server
 *
 */

public class StoreLocationsAsyncTask extends AsyncTask<String, String, ArrayList<Object>>{
	
	private Context mContext;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private ListView mStoreLocationListView;
	private String address1,address2,city,state,zipcode,mobile_number,mSelectedLocationid,mStatus;
	public static String latitude="",longitude="";
	
	// To get all store location details
	public StoreLocationsAsyncTask(Context context,ListView StoreLocationListView) {
		this.mContext = context;
		this.mStoreLocationListView = StoreLocationListView;
        zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
	}
	
	// To add location
	public StoreLocationsAsyncTask(Context context, String address1,String address2, String city, String state, String zipcode,String mobile_number) {
		this.mContext = context;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.mobile_number = mobile_number;
        zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
	}
	
	// To Inactivate Location
	public StoreLocationsAsyncTask(Context context,String locationid,String status) {
		this.mContext = context;
		this.mSelectedLocationid = locationid;
		this.mStatus = status;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected ArrayList<Object> doInBackground(String... params) {
		try{
			SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String mStoreId = mPrefs.getString("store_id", "");
			if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_Locations")){
				String mResponse=zouponswebservice.getAllStoreLocations(mStoreId);
				if(!mResponse.equals("")){
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
						return parsingclass.parseStoreLocations(mResponse);
					}else{ // service issues
						return null;
					}
				}else { // failure
					return null;
				}
			}else if(mSelectedLocationid != null){ // Request for location status change
				String mResponse=zouponswebservice.ChangeStoreLocationsStatus(mSelectedLocationid,mStatus);
				if(!mResponse.equals("")){
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
						return parsingclass.parseLocationStatusChangeResponse(mResponse);
					}else{ // service issues
						return null;
					}
				}else { // failure
					return null;
				}
			}else{  // Add new location
				latitude = "" ; longitude = "";
				String mResponse=zouponswebservice.validateLocationAddress(address1,address2,city,state,zipcode);
				if(!mResponse.equals("")){
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
						String validate_response = parsingclass.parseValidateAddress(mResponse);
						if(validate_response.equalsIgnoreCase("success")){
							String mAddResponse=zouponswebservice.addStoreLocations(mStoreId,address1,address2,city,state,zipcode,mobile_number,latitude,longitude);
							if(!mAddResponse.equals("")){
								if(!mAddResponse.equals("failure") && !mAddResponse.equals("noresponse")){ // Success
									return parsingclass.parseAddStoreLocations(mAddResponse);
								}else{ // service issues
									return null;
								}
							}else { // failure
								return null;
							}
						}else{
							ArrayList<Object> mInvalidAddress = new ArrayList<Object>();
							mInvalidAddress.add("Invalid address");
							return mInvalidAddress;
						}
					}else{ // service issues
						return null;
					}
				}else { // failure
					return null;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		super.onPostExecute(result);
		if(progressdialog.isShowing()){
			progressdialog.dismiss();
		}
		if(result!=null && result.size() >0){ // Success
			if(mContext.getClass().getSimpleName().equalsIgnoreCase("StoreOwner_Locations")){ // To list  
				mStoreLocationListView.setAdapter(new StoreLocationsAdapter(mContext, result));
			}else if(mSelectedLocationid != null){ // For Inactivate
               String message = (String) result.get(0);
               if(message.equalsIgnoreCase("success")){
            	   alertBox_service("Information", "Your request has been successfully posted to zoupons admin.");
               }else{
            	   alertBox_service("Information", "Failure please try after some time");
               }
           	}else{ // For Add
				String message =  (String) result.get(0);
				if(message.equalsIgnoreCase("Invalid address")){
					alertBox_service("Information", "Please enter valid address details");
				}else{
					if(message.equalsIgnoreCase("Successfully inserted store location")){
						alertBox_service("Information", "New location has been successfully added to admin for activation,SEO and QRcode");
					}else{
						alertBox_service("Information", message);	
					}

				}
			}
		}else if(result!=null && result.size() == 0){
			alertBox_service("Information", "No data available");
		}else{
			alertBox_service("Information", "Unable to reach service.");
		}
	}
	
	// To show alert box with respective message
	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equalsIgnoreCase("New location has been successfully added to admin for activation,SEO and QRcode") || msg.equalsIgnoreCase("Your request has been successfully posted to zoupons admin.")){
					Intent intent_locations = new Intent().setClass(mContext.getApplicationContext(),StoreOwner_Locations.class);
					mContext.startActivity(intent_locations);
				}
			}
		});
		service_alert.show();
	}
}
