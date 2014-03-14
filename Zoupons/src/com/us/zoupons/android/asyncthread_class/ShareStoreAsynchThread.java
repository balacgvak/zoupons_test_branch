package com.us.zoupons.android.asyncthread_class;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.ShareStore_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to communicate with webservice to share store
 *
 */

public class ShareStoreAsynchThread extends AsyncTask<String, String, String>{

	private Activity mContext;
	public NetworkCheck mConnectionAvailabilityChecking;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private String mGetResponse=null;
	private String mParsingResponse,mToEmailAddress;
	private ProgressDialog mProgressdialog=null;
	private String mFriendUserId="",mStoreLocationShareLink="";
	private UiLifecycleHelper mFacebookUILifeCycleHelper;

	public ShareStoreAsynchThread(Activity context,ImageView fbImage){
		this.mContext=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}

	public ShareStoreAsynchThread(Activity context, UiLifecycleHelper FacebookUILifeCycleHelper){
		this.mContext=context;
		this.mFacebookUILifeCycleHelper=FacebookUILifeCycleHelper;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}

	//Constructor for social store share to friends...
	public ShareStoreAsynchThread(Activity context,String ToEmailAddress,String frienduserid){
		this.mContext=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
		this.mToEmailAddress = ToEmailAddress;
		this.mFriendUserId = frienduserid;
	}

	@Override
	protected void onPreExecute() {
		//Start a status dialog
		super.onPreExecute();
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		boolean sharestoreflag = false;
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
				if(params[0].equals("Email")){
					sharestoreflag=true;
					mGetResponse=mZouponswebservice.Share_Store(params[0],mToEmailAddress,mFriendUserId);
				}else if(params[0].equals("FB")){
					mGetResponse = mZouponswebservice.getStoreShareLink();
				}
				if(sharestoreflag){
					if(!mGetResponse.equals("")){
						if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
							mParsingResponse=mParsingclass.parseShareStore(mGetResponse);
							if(mParsingResponse.equalsIgnoreCase("success")){
								for(int i=0;i<WebServiceStaticArrays.mShareStore.size();i++){
									final ShareStore_ClassVariables parsedobjectvalues = (ShareStore_ClassVariables) WebServiceStaticArrays.mShareStore.get(i);
									if(params[0].equalsIgnoreCase("FB")){
										if(!parsedobjectvalues.mMessage.equals("")){
											if(parsedobjectvalues.mMessage.equals("Successfully Shared via FB")){
												result="FbShared";
											}else if(parsedobjectvalues.mMessage.equals("Failed Share")){
												result="FbFailedShare";
											}else{
												result = parsedobjectvalues.mMessage;
											}
										}
									}else if(params[0].equalsIgnoreCase("Email")){
										if(parsedobjectvalues.mMessage.equals("Failed Share")){
											//result="MailFailedShare";
											result="Failed Share";
										}else if(parsedobjectvalues.mMessage.equalsIgnoreCase("Already shared today")){
											result=parsedobjectvalues.mMessage;
										}
										else{
											result="mailsuccess";
										}
									}
								}
							}else if(mParsingResponse.equalsIgnoreCase("failure")||mParsingResponse.equalsIgnoreCase("norecords")){
								result="Response Error.";
							}
						}else{
							result="Response Error.";
						}
					}else{
						result="no data";
					}
				}else{
					mParsingResponse=mParsingclass.parseShareStoreLinkResponse(mGetResponse);
					if(!mParsingResponse.equalsIgnoreCase("norecords") && !mParsingResponse.equalsIgnoreCase("failure")){
						mStoreLocationShareLink = mParsingResponse;
						if(Patterns.WEB_URL.matcher(mStoreLocationShareLink).matches())
							return "FbShared";
						else
							return "FbFailedShare";
					}else{
						return "FbFailedShare";
					}
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
		mProgressdialog.dismiss();
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")||result.equals("failure")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("no data")){
			alertBox_service("Information", "No data available");
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equals("FbShared")){
			shareToFB();
		}else if(result.equals("FbFailedShare")){
			alertBox_service("Information", "Store not shared via FB.Please try again later.");
		}else if(result.equals("Failed Share")){
			alertBox_service("Information", result);
		}else if(result.equals("mailsuccess")){
			alertBox_service("Information", "Store shared with your friend");
		}else if(result.equalsIgnoreCase("Already shared today")){
			alertBox_service("Information", "Store already shared today");
		}else {
			alertBox_service("Information", result);
		}


	}

	 /*To share store via Facebook*/
	private void shareToFB() {
		// TODO Auto-generated method stub
		if (FacebookDialog.canPresentShareDialog(mContext,FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) { // If FB application installed in device
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(mContext)
			.setLink(mStoreLocationShareLink)
			.build();
			mFacebookUILifeCycleHelper.trackPendingDialogCall(shareDialog.present());
		}else{ // Not installed in device
			sharetoFBViaFeedDialog();
		}

	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	 /*To share store via FeedDialog*/
	public void sharetoFBViaFeedDialog(){
		OpenRequest open = new OpenRequest(mContext);
		open.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		open.setPermissions(Arrays.asList(new String[]{"publish_actions"}));

		final Session fb_session = new Session(mContext);
		Session.setActiveSession(fb_session);
		fb_session.addCallback(new Session.StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					Bundle params = new Bundle();
					params.putString("link", mStoreLocationShareLink);
					
					Request request = new Request(Session.getActiveSession(), "me/feed", params, HttpMethod.POST);
					
					request.setCallback(new Request.Callback() {
						
						@Override
					    public void onCompleted(Response response) {
					        if (response.getError() == null) {
					            // Tell the user success!
					        	alertBox_service("Information", "Successfully Shared via FB.");
					        }else{
					        	fb_session.closeAndClearTokenInformation();
					        	sharetoFBViaFeedDialog();
					        }
					    }
						
						
					});
					request.executeAsync();
				}else{
				}
			}
		});
		fb_session.openForPublish(open);
	}

	/* To show alert pop up with respective message */
	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
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