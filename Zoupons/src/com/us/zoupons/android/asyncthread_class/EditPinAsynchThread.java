package com.us.zoupons.android.asyncthread_class;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.shopper.wallet.ManageWallets;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to communicate with webservice to change secret PIN number
 *
 */

public class EditPinAsynchThread extends AsyncTask<String, String, String>{

	private Context mContext;
	private NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse;
	private ProgressDialog mProgressdialog=null;
	private EditText mEnterPin,mReEnterPin;
	private String updatedPin="";
	private boolean mIsFromSettings; 
	private LinearLayout mMenuBarContactInfo,mMenuBarEditPin;
	private ScrollView mContactInfoContainer,mEditPinContainer;
	
	//Consructor for managecards
	public EditPinAsynchThread(Context context,EditText enterpin,EditText reenterpin,LinearLayout menubareditpin){
		this.mContext=context;
		this.mEnterPin=enterpin;
		this.mReEnterPin=reenterpin;
		this.mMenuBarEditPin=menubareditpin;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	
	//Constructor forSettings
	public EditPinAsynchThread(Context context,EditText enterpin,EditText reenterpin,boolean Fromsettings,LinearLayout menubarcontactinfo,LinearLayout menubareditpin,ScrollView contactinfocontainer,ScrollView editpincontainer){
		this.mContext=context;
		this.mEnterPin=enterpin;
		this.mReEnterPin=reenterpin;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		mIsFromSettings = Fromsettings;
		
		this.mMenuBarContactInfo=menubarcontactinfo;
		this.mMenuBarEditPin=menubareditpin;
		this.mContactInfoContainer=contactinfocontainer;
		this.mEditPinContainer=editpincontainer;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
				updatedPin=params[0];
				mGetResponse=mZouponswebservice.updateuserpin(params[0]);	
				if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
					mParsingResponse=mParsingclass.parseUpdateUserPin(mGetResponse);
					if(mParsingResponse.equalsIgnoreCase("Success")){
						result="Successfully Updated";
					}else if(mParsingResponse.equalsIgnoreCase("Failure")){
						result="Problem in Edit Pin updation.Please Try again later.";
					}else if(mParsingResponse.equalsIgnoreCase("failure")){
						result="Response Error.";
					}else if(mParsingResponse.equalsIgnoreCase("norecords")){
						result="EditPin is not updated";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch(Exception e){
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
		super.onPostExecute(result);
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
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
		mProgressdialog.dismiss();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	/* To show alert pop up with respective message */
	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equals("PIN number updated")){
					UserDetails.mServicePin=updatedPin;
					mEnterPin.getText().clear();
					mReEnterPin.getText().clear();
					if(!mIsFromSettings){
						mMenuBarEditPin.setBackgroundResource(R.drawable.header_2);
						ManageWallets.mManageCards_EditPinContainer.setVisibility(View.GONE);
						ManageWallets.mManageCards_CardListContainer.setVisibility(View.VISIBLE);
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