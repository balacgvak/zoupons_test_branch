package com.us.zoupons.GoogleAccountHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.SlidingView;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class GetAccessTokenClass extends AsyncTask<Void, Void, String>{

	public ProgressDialog mProgressDialog;
	private String code;
	private Activity mContext;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	private String mButtonText;
	
	public GetAccessTokenClass(Activity context,String code,String buttontext) {
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(context);
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setCancelable(false);
		this.code = code;
		this.mContext = context;
		this.mButtonText = buttontext;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(BackGroundAsyncTaskFlagClass.BackGroundAsyncTaskFlag==false){
			alertBox_move("Information", "Importing contacts may take few minutes, do you want to leave from this page.");
		}else{
			mProgressDialog.setMessage("Please wait for a few minutes,Fetching user details...");
			mProgressDialog.show();
		}
	}
	
	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected String doInBackground(Void... params) {
		BackGroundAsyncTaskFlagClass.BackGroundAsyncTaskFlag=true;
		String response_status="";
		
		// Network check
		if(mConnectionAvailabilityChecking.ConnectivityCheck(mContext)){
			// Webservice call to get access token
			String access_token_response = zouponswebservice.getAccessToken(code);
			if(!access_token_response.equals("failure") && !access_token_response.equals("noresponse")){
				// Parsing access token response
				String access_token_parsing_status = parsingclass.parseAccessTokenResponse(access_token_response);
				if(access_token_parsing_status.equalsIgnoreCase("success")){
					if(WebServiceStaticArrays.AccessTokenList.size() > 0){
						AccessTokenDetails access_token_details = (AccessTokenDetails) WebServiceStaticArrays.AccessTokenList.get(0);
						// Webservice call to get user information from access token obtained
						String UserDetailsResponse = zouponswebservice.getUserDetails(access_token_details.access_token);
						if(!UserDetailsResponse.equals("failure") && !UserDetailsResponse.equals("noresponse")){
							// Parsing user info data response
							response_status = parsingclass.parseGmailUserDetails(UserDetailsResponse);
							if(response_status.equalsIgnoreCase("success") && WebServiceStaticArrays.GMailUserDetails.size()>0){
								GmailUserDetails userDetails = (GmailUserDetails) WebServiceStaticArrays.GMailUserDetails.get(0);
								// Webservice call to import gmail friends by using user information and access token.
								
								String ImportFriendsResponse = zouponswebservice.importfriends("Google", userDetails.GmailId, access_token_details.access_token);
								if(!ImportFriendsResponse.equals("failure") && !ImportFriendsResponse.equals("noresponse")){
									// Parsing import friends status report
									response_status = parsingclass.parseImportSocialFriends(ImportFriendsResponse);
								}else{
									response_status="Response Error.";
								}
								
							}else{
								response_status = "failure";
							}
						}else{
							response_status="Response Error.";
						}

					}else{
						response_status = "failure";
					}
				}else{
					response_status = "failure";
				}
			}else{
				response_status="Response Error.";
			}
		}else{
			response_status="no network";
		}
		return response_status;
	}

	@Override
	protected void onPostExecute(String result) {
		
		super.onPostExecute(result);
		
		Log.i("result",result);
		if(result.equalsIgnoreCase("success")){
			if(WebServiceStaticArrays.AccessTokenList.size() > 0){
				AccessTokenDetails access_token_details = (AccessTokenDetails) WebServiceStaticArrays.AccessTokenList.get(0);
			}
			if(WebServiceStaticArrays.GMailUserDetails.size() > 0){
				GmailUserDetails userDetails = (GmailUserDetails) WebServiceStaticArrays.GMailUserDetails.get(0);
			}
			
			if(mButtonText.equalsIgnoreCase("Import")){
				Toast.makeText(mContext, "GMail friends successfully imported", Toast.LENGTH_SHORT).show();
				Intent mReturnImportStatusIntent = new Intent();
				mReturnImportStatusIntent.putExtra("import_status", true);
				mContext.setResult(Activity.RESULT_OK,mReturnImportStatusIntent);
				mContext.finish();
			}else{
				Toast.makeText(mContext, "GMail friends successfully updated", Toast.LENGTH_SHORT).show();
				Intent mReturnImportStatusIntent = new Intent();
				mReturnImportStatusIntent.putExtra("import_status", true);
				mContext.setResult(Activity.RESULT_OK,mReturnImportStatusIntent);
				mContext.finish();
			}
		}else if(result.equals("nonetwork")){
			Toast.makeText(this.mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("failure")){
			alertBox_service("Information", "Unable to fetch data please try after some time.");
		}else if(result.equals("no conctacts")){
			alertBox_service("Information", "No Contacts Available.");
		}
		mProgressDialog.dismiss();
		BackGroundAsyncTaskFlagClass.BackGroundAsyncTaskFlag=false;
	}


	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equalsIgnoreCase("Importing contacts may take few minutes, do you want to leave from this page.")){
					Intent intent_home = new Intent().setClass(mContext.getApplicationContext(),SlidingView.class);
					mContext.startActivity(intent_home);
				}else if(msg.equalsIgnoreCase("No Contacts Available.")){
					mContext.finish();
				}
			}
		});
		service_alert.show();
	}
	
	public void alertBox_move(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setPositiveButton("Wait",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mProgressDialog.setMessage("Please wait for a few minutes,Fetching user details...");
				mProgressDialog.show();
			}
		});
		
		service_alert.setNegativeButton("Leave", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				if(msg.equalsIgnoreCase("Importing contacts may take few minutes, do you want to leave from this page.")){
					mProgressDialog.dismiss();
					BackGroundAsyncTaskFlagClass.BackGroundAsyncTaskFlag=true;
					Intent intent_home = new Intent().setClass(mContext.getApplicationContext(),SlidingView.class);
					mContext.startActivity(intent_home);
				}
			}
		});
		service_alert.show();
	}
}