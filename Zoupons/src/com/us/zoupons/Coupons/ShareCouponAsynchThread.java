package com.us.zoupons.Coupons;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.ClassVariables.ShareCoupon_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class ShareCouponAsynchThread extends AsyncTask<String, String, String>{
	Context mContext;
	String TAG="ShareCouponAsynchThread";
	public NetworkCheck mConnectionAvailabilityChecking;
    private ZouponsWebService zouponswebservice = null;
	private ZouponsParsingClass parsingclass = null;
	String mGetResponse=null;
	String mParsingResponse;
	private ProgressDialog progressdialog=null;
	private String mailSubject,mailBody,mailSendTo;
	
	public ShareCouponAsynchThread(Context context){
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
	
	@Override
	protected String doInBackground(String... params) {
		String result="";
		boolean sharecouponflag = false;
		try{
			if(mConnectionAvailabilityChecking.ConnectivityCheck(this.mContext)){
				
				if(params[0].equals("Email")){
					sharecouponflag=true;
					mGetResponse=zouponswebservice.Share_Coupon(params[0],params[2]);
				}else if(params[0].equals("FB")){
					if(params[1].equalsIgnoreCase("AlreadyLogged")){
						sharecouponflag=true;
						mGetResponse=zouponswebservice.Share_Coupon(params[0],params[2]);
					}else if(params[1].equalsIgnoreCase("JustLogged")){
						//Call FBLogin Service to confirm fblogin details to our webservice
						mGetResponse=zouponswebservice.FBLogin();
						if(!mGetResponse.equals("")){
							if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
								mParsingResponse=parsingclass.parseFbLogin(mGetResponse);
								if(mParsingResponse.equalsIgnoreCase("Success")){
									sharecouponflag=true;
									mGetResponse=zouponswebservice.Share_Coupon(params[0],params[2]);
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
				
				if(sharecouponflag){
					
					if(!mGetResponse.equals("")){
						if(!mGetResponse.equals("failure") && !mGetResponse.equals("noresponse")){
							mParsingResponse=parsingclass.parseShareCoupon(mGetResponse);
							if(mParsingResponse.equalsIgnoreCase("success")){
								Log.i(TAG,"ShareCoupon List Size: "+WebServiceStaticArrays.mShareCoupon.size());
								for(int i=0;i<WebServiceStaticArrays.mShareCoupon.size();i++){
									final ShareCoupon_ClassVariables parsedobjectvalues = (ShareCoupon_ClassVariables) WebServiceStaticArrays.mShareCoupon.get(i);
									Log.i(TAG,"message : "+parsedobjectvalues.mMessage);
									if(params[0].equalsIgnoreCase("FB")){
										if(!parsedobjectvalues.mMessage.equals("")){
											if(parsedobjectvalues.mMessage.equals("Successfully Shared via FB")){
												result="FbShared";
											}else if(parsedobjectvalues.mMessage.equals("Failed to Share via FB")){
												result="FbFailedShare";
											}
										}
									}else if(params[0].equalsIgnoreCase("Email")){
										if(parsedobjectvalues.mMessage.equals("Failed Share")){
											result="MailFailedShare";
										}else{
											result="mailsuccess";
											Log.i(TAG,"UserMailIddress:"+UserDetails.mServiceEmail);
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
		Log.i(TAG,"result: "+result);
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")||result.equals("failure")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("no data")){
			alertBox_service("Information", "No data available");
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else if(result.equals("FbShared")){
			alertBox_service("Information", "Successfully Shared via FB");
		}else if(result.equals("FbFailedShare")){
			alertBox_service("Information", "Coupon not shared via FB.Please try again later.");
		}else if(result.equals("MailFailedShare")){
			alertBox_service("Information", "Coupon not shared via Mail.Please try again later.");
		}else if(result.equals("mailsuccess")){
			String[] sendTo={mailSendTo};
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setType("text/html");
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,mailSubject );
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mailBody );
			this.mContext.startActivity(Intent.createChooser(emailIntent, "Send mail via"));
			Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
		}
		progressdialog.dismiss();
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);

		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
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