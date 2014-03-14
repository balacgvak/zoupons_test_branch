package com.us.zoupons.android.asyncthread_class;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.POJOFBfriendListData;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.listview_inflater_classes.CustomFBFriendListAdapter;

/**
 * 
 * Asynchronous task to check contact list with Zoupons customer list
 *
 */

public class GetSocialFriendsTask extends AsyncTask<Void, String, String>{

	private Activity mContext;
	private ProgressDialog mProgressView;
	private NetworkCheck mConnectivityCheck;
	private ListView mfriendlist;
	private TextView mImportFriendMenu;
	private String mClassFlag;

	/*
	 * Constructor for Friends
	 * */
	public GetSocialFriendsTask(Activity context, ListView mfriendlist, ViewGroup friendlistgroup, View socialfriendsOptionsContainer, TextView mImportFriendMenu, View importmenusplitter, String classflag) {
		this.mContext = context;
		this.mfriendlist = mfriendlist;
		this.mImportFriendMenu = mImportFriendMenu;
		this.mClassFlag = classflag;
		mProgressView = new ProgressDialog(context);
		mProgressView.setTitle("Loading");
		mProgressView.setMessage("please wait..");
		mConnectivityCheck = new NetworkCheck();
	}

	/*
	 * Constructor for Social
	 * */
	public GetSocialFriendsTask(Activity context, ListView mfriendlist, ViewGroup friendlistgroup, View socialfriendsOptionsContainer, TextView mImportFriendMenu, View importmenusplitter, String classflag, ListView socialtypelist) {
		this.mContext = context;
		this.mfriendlist = mfriendlist;
		this.mImportFriendMenu = mImportFriendMenu;
		this.mClassFlag = classflag;
		mProgressView = new ProgressDialog(context);
		mProgressView.setTitle("Loading");
		mProgressView.setMessage("please wait..");
		mConnectivityCheck = new NetworkCheck();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressView.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		String mresult ="";
		try{
			if(mConnectivityCheck.ConnectivityCheck(mContext.getApplicationContext())){
				getContactList(); // To get phone contacts which has email id..
				if(WebServiceStaticArrays.mSocialNetworkFriendList.size() == 0){
					mresult="norecords";
				}else{
					String email_ids = new String(); 
					
					for(int i=0;i<WebServiceStaticArrays.mSocialNetworkFriendList.size();i++){
						POJOFBfriendListData mFriendDetails  = (POJOFBfriendListData) WebServiceStaticArrays.mSocialNetworkFriendList.get(i);
						if(!email_ids.contains(mFriendDetails.friend_email)){
							email_ids += mFriendDetails.friend_email;
							if(i!=WebServiceStaticArrays.mSocialNetworkFriendList.size()-1){
								email_ids+=",";
							}
						}else{
							WebServiceStaticArrays.mSocialNetworkFriendList.remove(i);
						}
					}
					if(email_ids.charAt(email_ids.length()-1) == ',')
						 email_ids = email_ids.substring(0,email_ids.length()-1);
					}
			}else{
				mresult="nonetwork";
			}
		}catch(Exception e){
			e.printStackTrace();
			mresult ="failure";
		}
		return mresult;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result.equalsIgnoreCase("success")){
			mImportFriendMenu.setBackgroundResource(R.drawable.header_2);
			WebServiceStaticArrays.mSearchedFriendList = new ArrayList<POJOFBfriendListData>(WebServiceStaticArrays.mSocialNetworkFriendList);
			mfriendlist.setAdapter(new CustomFBFriendListAdapter(mContext,WebServiceStaticArrays.mSearchedFriendList,mClassFlag));
		}else if(result.equalsIgnoreCase("failure")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equalsIgnoreCase("norecords")||result.equalsIgnoreCase("Response Error.")){
			WebServiceStaticArrays.mSearchedFriendList = new ArrayList<POJOFBfriendListData>(WebServiceStaticArrays.mSocialNetworkFriendList);
			mfriendlist.setAdapter(new CustomFBFriendListAdapter(mContext,WebServiceStaticArrays.mSearchedFriendList,mClassFlag));
		}else if(result.equalsIgnoreCase("no data")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equalsIgnoreCase("noaccount")){
			alertBox_service("Information", "Account doesn't exists");
		}else if(result.equalsIgnoreCase("nonetwork")){
			Toast.makeText(mContext.getApplicationContext(), "No Network Connection", Toast.LENGTH_SHORT).show();
		}
		mProgressView.dismiss();
		
	}

	@SuppressLint("InlinedApi")
	private void getContactList(){
		WebServiceStaticArrays.mSocialNetworkFriendList.clear();
		Cursor mAllContactsCursor = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);
		if(mAllContactsCursor!= null && mAllContactsCursor.getCount() > 0){
			while(mAllContactsCursor.moveToNext()){
				String contact_id = mAllContactsCursor.getString(mAllContactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
				String contact_name = mAllContactsCursor.getString(mAllContactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String photo_uri = mAllContactsCursor.getString(mAllContactsCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
				Cursor mContactEmailDetailsCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[]{contact_id}, null);
				if(mContactEmailDetailsCursor!= null && mContactEmailDetailsCursor.getCount() > 0){
					while(mContactEmailDetailsCursor.moveToNext()){
						POJOFBfriendListData mContactsDetails =  new POJOFBfriendListData();
						mContactsDetails.friend_username = (contact_name == null)?"":contact_name;
						mContactsDetails.name = mContactsDetails.friend_username;
						mContactsDetails.photo_url = (photo_uri == null)? "": photo_uri;
						String Email = mContactEmailDetailsCursor.getString(mContactEmailDetailsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						if(!Email.equalsIgnoreCase("")){
							mContactsDetails.friend_email = Email;
							if(!WebServiceStaticArrays.mSocialNetworkFriendList.contains(mContactsDetails))
							WebServiceStaticArrays.mSocialNetworkFriendList.add(mContactsDetails);
						}
					}
				}else{
				}
			}
		}else{
		}
	}
	
	/* To show alert pop up with respective message */
	public void alertBox_service(final String title,final String msg){
		AlertDialog.Builder service_alert = new AlertDialog.Builder(mContext);
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