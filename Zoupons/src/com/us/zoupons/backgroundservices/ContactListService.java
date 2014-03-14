package com.us.zoupons.backgroundservices;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Photo;
import android.util.Log;
import android.widget.ListView;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.classvariables.POJOFBfriendListData;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.listview_inflater_classes.CustomFBFriendListAdapter;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to call webservice to check contact list with DB 
 *
 */

public class ContactListService extends AsyncTask<String, String, String>{

	private ZouponsWebService mZouponswebservice;
	private ZouponsParsingClass mParsingclass;
	private NetworkCheck mConnectivityCheck;
	private Context mContext;
	private StringBuilder mEmailIds;
	private String mResponse="",mPageFlag;
	private ListView mFriendList;
	private ProgressDialog mProgressDialog;

	public ContactListService(Context context) {
		this.mContext = context;
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
	}

	public ContactListService(Activity context, ListView friendList, String page_flag) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mFriendList = friendList;
		this.mPageFlag = page_flag;
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(context);
		mConnectivityCheck = new NetworkCheck();
	}

	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(mFriendList != null){ // From friends/share call
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setMessage("Please wait for moment");
			mProgressDialog.show();
		}
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String mresult ="",mParsingResponse="";
		try{
			if(mConnectivityCheck.ConnectivityCheck(mContext)){
				//getContactList(); // To get phone contacts which has email id..
				getNameEmailDetails();
				if(WebServiceStaticArrays.mSocialNetworkFriendList.size() == 0){
					mresult="norecords";
				}else{
					mEmailIds = new StringBuilder(); 
					// To append contact email ids with comma seperation to check for zoupons/non zoupons customer
					for(int i=0;i<WebServiceStaticArrays.mSocialNetworkFriendList.size();i++){
						POJOFBfriendListData mFriendDetails  = (POJOFBfriendListData) WebServiceStaticArrays.mSocialNetworkFriendList.get(i);
						mEmailIds.append(mFriendDetails.friend_email);
						if(i!=WebServiceStaticArrays.mSocialNetworkFriendList.size()-1){
							mEmailIds.append(",");
						}
					}
					// To get user id
					SharedPreferences mPrefs = mContext.getSharedPreferences("UserNamePrefences",Context.MODE_PRIVATE);
					String user_id = mPrefs.getString("user_id", "");
					mResponse = mZouponswebservice.checkZouponsCustomer(mEmailIds,user_id);
					if(!mResponse.equalsIgnoreCase("noresponse") && !mResponse.equalsIgnoreCase("failure")){
						mParsingResponse = mParsingclass.parseFriendList(mResponse);
						if(!mParsingResponse.equals("failure") && !mParsingResponse.equals("norecords")){
							mresult = "success";
						}else if(mParsingResponse.equalsIgnoreCase("failure")){
							mresult = "failure";
						}else if(mParsingResponse.equalsIgnoreCase("norecords")){
							mresult="norecords";
						}
					}else {
						mresult="Response Error.";
					}
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
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(mFriendList != null){
			mProgressDialog.dismiss();
			if(result.equalsIgnoreCase("nonetwork")){
				
			}else{
				WebServiceStaticArrays.mSearchedFriendList = new ArrayList<POJOFBfriendListData>(WebServiceStaticArrays.mSocialNetworkFriendList);
				mFriendList.setAdapter(new CustomFBFriendListAdapter((Activity) mContext,WebServiceStaticArrays.mSocialNetworkFriendList,mPageFlag));
			}
		}else{
			Intent stop_contact_service = new Intent(mContext,ContactsLoaderService.class);
			mContext.stopService(stop_contact_service);
			if(result.equalsIgnoreCase("success")){
				WebServiceStaticArrays.mSearchedFriendList = new ArrayList<POJOFBfriendListData>(WebServiceStaticArrays.mSocialNetworkFriendList);
			}
		}
	}

	@SuppressLint("InlinedApi")
	public void getNameEmailDetails() {
		ContentResolver cr = mContext.getContentResolver();
		String[] PROJECTION ;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { // HoneyComb or later
			PROJECTION  = new String[] { ContactsContract.RawContacts._ID, 
					ContactsContract.Contacts.DISPLAY_NAME,
					ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
					ContactsContract.CommonDataKinds.Email.DATA, 
					ContactsContract.CommonDataKinds.Photo.CONTACT_ID}; 	
		}else{
			PROJECTION = new String[] { ContactsContract.RawContacts._ID, 
					ContactsContract.Contacts.DISPLAY_NAME,
					ContactsContract.Contacts.PHOTO_ID,
					ContactsContract.CommonDataKinds.Email.DATA, 
					ContactsContract.CommonDataKinds.Photo.CONTACT_ID};
		}
		String order = "CASE WHEN " 
				+ ContactsContract.Contacts.DISPLAY_NAME 
				+ " NOT LIKE '%@%' THEN 1 ELSE 2 END, " 
				+ ContactsContract.Contacts.DISPLAY_NAME 
				+ ", " 
				+ ContactsContract.CommonDataKinds.Email.DATA
				+ " COLLATE NOCASE";
		String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
		Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, order);
		if (cur.moveToFirst()) {
			do {
				// names comes in hand sometimes
				String name = cur.getString(1);
				String emlAddr = cur.getString(3);
				POJOFBfriendListData mContactsDetails =  new POJOFBfriendListData();
				mContactsDetails.friend_username = (name == null)?"":name;
				mContactsDetails.name = mContactsDetails.friend_username;
				String photo_uri = "";
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { // HoneyComb or later
					 photo_uri = cur.getString(2);
				}else{
				   	final Uri contactUri = Uri.withAppendedPath(Contacts.CONTENT_URI, cur.getString(4));
					java.io.InputStream  mContactsPhotoStream = ContactsContract.Contacts.openContactPhotoInputStream(cr, contactUri);
					if(mContactsPhotoStream == null){
						photo_uri = null;
					}else{
						photo_uri = Uri.withAppendedPath(contactUri, Photo.CONTENT_DIRECTORY).toString();
					}
				}
				mContactsDetails.photo_url = photo_uri;
				if(!emlAddr.equalsIgnoreCase("")){
					mContactsDetails.friend_email = emlAddr;
					if(!WebServiceStaticArrays.mSocialNetworkFriendList.contains(mContactsDetails))
						WebServiceStaticArrays.mSocialNetworkFriendList.add(mContactsDetails);
					Log.i("inserted", "" + WebServiceStaticArrays.mSocialNetworkFriendList.size() + " " + name + " " + emlAddr + " " + photo_uri);
				}

			} while (cur.moveToNext());
		}
		cur.close();
	}

}
