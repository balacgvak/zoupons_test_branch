package com.us.zoupons.friends;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class InviteFriendAsyncTask extends AsyncTask<String, String, String> {

	private ProgressDialog mProgressView;
	private Context context;
	private ZouponsWebService zouponswebservice;
	private ZouponsParsingClass parsingclass;
	private NetworkCheck mConnectivityCheck;

	public InviteFriendAsyncTask(Context context) {
		this.context = context;
		mProgressView = new ProgressDialog(context);
		mProgressView.setTitle("Loading");
		mProgressView.setMessage("please wait..");
		zouponswebservice = new ZouponsWebService(context);
		parsingclass = new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressView.show();
	}

	@Override
	protected String doInBackground(String... params) {
		String mresult = "", mParsingResponse = "";
		try {
			if (mConnectivityCheck.ConnectivityCheck(context)) {
				String mResponse = zouponswebservice.inviteFriend(params[0]);
				if (!mResponse.equals("")) {
					if (!mResponse.equals("failure")&& !mResponse.equals("noresponse")) {
						mParsingResponse = parsingclass.parseInviteFriendResponse(mResponse);
						if (mParsingResponse.equalsIgnoreCase("success")) {
							mresult = "success";
						} else if (mParsingResponse.equalsIgnoreCase("Failure")) {
							mresult = "failure";
						} else if (mParsingResponse.equalsIgnoreCase("norecords")) {
							mresult = "norecords";
						}
					} else {
						mresult = "Response Error.";
					}
				} else {
					mresult = "nodata";
				}
			} else {
				mresult = "nonetwork";
			}
		} catch (Exception e) {

		}
		return mresult;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mProgressView.dismiss();
		if (result.equalsIgnoreCase("nonetwork")) {
			Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
		} else if (result.equalsIgnoreCase("Response Error.")) {
			alertBox_service("Information", "Unable to reach service.");
		} else if (result.equalsIgnoreCase("norecords")) {
			alertBox_service("Information", "Unable to send request");
		} else if (result.equalsIgnoreCase("Failure")) {
			alertBox_service("Information", "Unable to process.");
		} else if (result.equalsIgnoreCase("success")) {
			if(Friends.invitestatus.equalsIgnoreCase("success")){
				alertBox_service("Information", "Invitation has been successfully send to mail");	
			}else{
				alertBox_service("Information", "Unable to send invitation");
			}
		}
	}

	private void alertBox_service(String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(context);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		service_alert.show();
	}
}