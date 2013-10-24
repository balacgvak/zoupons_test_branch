package com.us.zoupons.android.AsyncThreadClasses;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.ShareStore_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class ShareStoreAsynchThread extends AsyncTask<String, String, String>{

	Context mContext;
	String TAG="SocialAsynchThread";
	public NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse,mToEmailAddress;
	private ProgressDialog progressdialog=null;
	private String mailSubject,mailBody,mailSendTo;
	private TextView mFbCount;
	private ImageView mFbImage;
	private String mGetFbCount="";
	
	public ShareStoreAsynchThread(Context context,TextView fbcount,ImageView fbImage){
		this.mContext=context;
		this.mFbCount=fbcount;
		this.mFbImage=fbImage;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.mContext);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	public ShareStoreAsynchThread(Context context){
		this.mContext=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.mContext);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	//Constructor for social store share to friends...
	public ShareStoreAsynchThread(Context context,String ToEmailAddress){
		this.mContext=context;
		mConnectionAvailabilityChecking= new NetworkCheck();
		zouponswebservice= new ZouponsWebService(context);
		parsingclass= new ZouponsParsingClass(this.mContext);
		progressdialog=new ProgressDialog(context);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
		this.mToEmailAddress = ToEmailAddress;
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		boolean sharestoreflag = false;
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
				
				if(params[0].equals("Email")){
					sharestoreflag=true;
					mGetResponse=zouponswebservice.Share_Store(params[0],mToEmailAddress);
				}else if(params[0].equals("FB")){
					if(params[1].equalsIgnoreCase("AlreadyLogged")){
						sharestoreflag=true;
						mGetResponse=zouponswebservice.Share_Store(params[0],mToEmailAddress);
					}else if(params[1].equalsIgnoreCase("JustLogged")){
						mGetResponse=zouponswebservice.importfriends("Facebook", UserDetails.mServiceFbId, UserDetails.mServiceAccessToken);
						if(!mGetResponse.equals("")){
							if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
								mParsingResponse=parsingclass.parseImportSocialFriends(mGetResponse);
								if(mParsingResponse.equalsIgnoreCase("Success")){
									sharestoreflag=true;
									mGetResponse=zouponswebservice.Share_Store(params[0],mToEmailAddress);
								}else if(mParsingResponse.equalsIgnoreCase("Failure")){
									result="failure";
								}else if(mParsingResponse.equalsIgnoreCase("No Zoupons Account")){
									result="noaccount";
								}else if(mParsingResponse.equalsIgnoreCase("norecords")){
									result="Response Error.";
								}
							}else{
								result="Response Error.";
							}
						}else {
							result="no data";
						}
					}
				}
				
				if(sharestoreflag){
					if(!mGetResponse.equals("")){
						if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
							mParsingResponse=parsingclass.parseShareStore(mGetResponse);
							if(mParsingResponse.equalsIgnoreCase("success")){
								for(int i=0;i<WebServiceStaticArrays.mShareStore.size();i++){
									final ShareStore_ClassVariables parsedobjectvalues = (ShareStore_ClassVariables) WebServiceStaticArrays.mShareStore.get(i);
									if(params[0].equalsIgnoreCase("FB")){
										if(!parsedobjectvalues.mMessage.equals("")){
											if(parsedobjectvalues.mMessage.equals("Successfully Shared via FB")){
												mGetFbCount=parsedobjectvalues.mFbCount;
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
											mailSendTo=UserDetails.mServiceEmail;
											mailSubject=parsedobjectvalues.mSubject;
											mailBody=parsedobjectvalues.mEmailTemplate;
										}
									}
								}
							}else if(mParsingResponse.equalsIgnoreCase("failure")||mParsingResponse.equalsIgnoreCase("norecords")){
								Log.i(TAG,"Error");
								result="Response Error.";
							}
						}else{
							result="Response Error.";
						}
					}else{
						result="no data";
					}
				}else{
					Log.i(TAG,"FBLogin Failure");
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
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")||result.equals("failure")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("no data")){
			alertBox_service("Information", "No data available");
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equals("FbShared")){
			mFbCount.setText(mGetFbCount+" Shares");
			mFbImage.setEnabled(false);
			mFbImage.getBackground().setAlpha(120);
			alertBox_service("Information", "Successfully Shared via FB");
		}else if(result.equals("FbFailedShare")){
			alertBox_service("Information", "Store not shared via FB.Please try again later.");
		}else if(result.equals("Failed Share")){
			alertBox_service("Information", result);
		}else if(result.equals("mailsuccess")){
			alertBox_service("Information", "Successfully shared store to your friend");
		}else if(result.equalsIgnoreCase("Already shared today")){
			alertBox_service("Information", "Already shared today");
		}else {
			alertBox_service("Information", result);
		}
		progressdialog.dismiss();
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

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