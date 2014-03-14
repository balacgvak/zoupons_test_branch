package com.us.zoupons.shopper.friends;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Async task to communicate with web server for inviting gmail friends to zoupons
 *
 */

public class InviteFriendAsyncTask extends AsyncTask<String, String, String> {

	private ProgressDialog mProgressView;
	private Context mContext;
	private ZouponsWebService mZouponswebservice;
	private ZouponsParsingClass mParsingclass;
	private NetworkCheck mConnectivityCheck;

	public InviteFriendAsyncTask(Context context) {
		this.mContext = context;
		mProgressView = new ProgressDialog(context);
		mProgressView.setTitle("Loading");
		mProgressView.setMessage("please wait..");
		mZouponswebservice = new ZouponsWebService(context);
		mParsingclass = new ZouponsParsingClass(context);
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
			if (mConnectivityCheck.ConnectivityCheck(mContext)) {
				String mResponse = mZouponswebservice.inviteFriend(params[0]);
				if (!mResponse.equals("")) {
					if (!mResponse.equals("failure")&& !mResponse.equals("noresponse")) {
						mParsingResponse = mParsingclass.parseInviteFriendResponse(mResponse);
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
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		} else if (result.equalsIgnoreCase("Response Error.")) {
			alertBox_service("Information", "Unable to reach service.");
		} else if (result.equalsIgnoreCase("norecords")) {
			alertBox_service("Information", "Unable to send request");
		} else if (result.equalsIgnoreCase("Failure")) {
			alertBox_service("Information", "Unable to process.");
		} else if (result.equalsIgnoreCase("success")) {
			if(Friends.invitestatus.equalsIgnoreCase("success")){
				alertBox_service("Information", "Invitation sent to friend");	
			}else{
				alertBox_service("Information", "Unable to send invitation");
			}
		}
	}

	/* To show alert pop up with respective message */
	private void alertBox_service(String title, String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(mContext);
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