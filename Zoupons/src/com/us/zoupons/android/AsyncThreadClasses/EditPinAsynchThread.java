package com.us.zoupons.android.AsyncThreadClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.us.zoupons.ManageCards;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class EditPinAsynchThread extends AsyncTask<String, String, String>{

	String TAG="EditPinAsynchThread";
	Context ctx;
	public NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	private ProgressDialog progressdialog=null;
	private EditText mEnterPin,mReEnterPin;
	private String updatedPin="";
	private boolean isFromSettings; 
	private LinearLayout mMenuBarContactInfo,mMenuBarEditPin;
	private ScrollView mContactInfoContainer,mEditPinContainer;
	
	//Consructor for managecards
	public EditPinAsynchThread(Context context,EditText enterpin,EditText reenterpin,LinearLayout menubareditpin){
		this.ctx=context;
		this.mEnterPin=enterpin;
		this.mReEnterPin=reenterpin;
		this.mMenuBarEditPin=menubareditpin;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	//Constructor forSettings
	public EditPinAsynchThread(Context context,EditText enterpin,EditText reenterpin,boolean Fromsettings,LinearLayout menubarcontactinfo,LinearLayout menubareditpin,ScrollView contactinfocontainer,ScrollView editpincontainer){
		this.ctx=context;
		this.mEnterPin=enterpin;
		this.mReEnterPin=reenterpin;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.ctx);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		isFromSettings = Fromsettings;
		
		this.mMenuBarContactInfo=menubarcontactinfo;
		this.mMenuBarEditPin=menubareditpin;
		this.mContactInfoContainer=contactinfocontainer;
		this.mEditPinContainer=editpincontainer;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(ctx)){
				updatedPin=params[0];
				mGetResponse=zouponswebservice.updateuserpin(params[0]);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=parsingclass.parseUpdateUserPin(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("Success")){
						result="Successfully Updated";
					}else if(mParsingResponse.equalsIgnoreCase("Failure")){
						result="Problem in Edit Pin updation.Please Try again later.";
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						Log.i(TAG,"Error");
						result="Response Error.";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						Log.i(TAG,"No Records");
						result="EditPin is not updated";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
			Log.i(TAG,"Thread Error");
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		Log.i(TAG,"result: "+result);
		if(result.equals("nonetwork")){
			Toast.makeText(ctx, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("EditPin is not updated")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equals("Successfully Updated")){
			alertBox_service("Information", "PIN number updated");
		}else if(result.equals("Problem in Edit Pin updation.Please Try again later.")){
			alertBox_service("Information", "Problem in Edit Pin updation.Please Try again later.");
		}
		progressdialog.dismiss();
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		((Activity) ctx).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);

		//Start a status dialog
		progressdialog = ProgressDialog.show(ctx,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.ctx);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equals("PIN number updated")){
					UserDetails.mServicePin=updatedPin;
					mEnterPin.getText().clear();
					mReEnterPin.getText().clear();
					if(!isFromSettings){
						mMenuBarEditPin.setBackgroundResource(R.drawable.header_2);
						ManageCards.mManageCards_EditPinContainer.setVisibility(View.GONE);
						ManageCards.mManageCards_CardListContainer.setVisibility(View.VISIBLE);
					}else{
						mMenuBarContactInfo.setBackgroundResource(R.drawable.footer_dark_blue_new);
						mMenuBarEditPin.setBackgroundResource(R.drawable.header_2);
						
						if(mContactInfoContainer.getVisibility()==View.INVISIBLE||mContactInfoContainer.getVisibility()==View.GONE){
							mContactInfoContainer.setVisibility(View.VISIBLE);
						}
						mEditPinContainer.setVisibility(View.GONE);
					}
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}