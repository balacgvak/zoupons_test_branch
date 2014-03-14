/**
 * 
 */
package com.us.zoupons.shopper.webService;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.us.zoupons.EncryptionClass;
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.classvariables.CardId_ClassVariable;
import com.us.zoupons.classvariables.CurrentLocation;
import com.us.zoupons.classvariables.ManageCardAddPin_ClassVariables;
import com.us.zoupons.classvariables.POJOStorePhoto;
import com.us.zoupons.classvariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.AccountLoginFlag;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.login.ZouponsLogin;
import com.us.zoupons.storeowner.videos.CustomMultiPartEntity;

/**
 * Class to invoke all webservice functions
 */
public class ZouponsWebService {

	private String TAG=ZouponsWebService.class.getName();
	public String URL = "http://dev1.zoupons.com/webservices/";
	//private String URL = "https://www.zoupons.com/webservices/";
	private String GET_SECURITY_QUESTIONS="get_security_questions";
	private String FORGOT_PASSWORD="forgot_pwd";
	private String SECURITY_QUESTIONS="security_ques"; 
	private String SIGNUP="signup";
	private String SIGNUP_STEP2="signup_step_two";
	private String ACTIVATIONUSER="activateuser";
	private String LOGIN="login";
	private String FIRST_DATA_GLOBAL_PAYEMENT ="first_data_global_payment";
	private String CHANGE_PASSWORD="change_pwd";
	private String CATEGORIES="categories";
	private String SUBCATEGORIES="subcategories";
	private String SEARCH="search";
	private String STORE_LOCATOR="store_locator";
	private String CHECK_FAVORITE="check_favourites";
	private String ADD_FAVORITE="add_to_favourites";
	private String CARD_ON_FILE="cards_on_file";
	private String UPDATE_USER_PIN="update_user_pin";
	private String REMOVE_CARD="remove_card";
	private String STORE_INFO="stores";
	private String STORE_PHOTOS ="stores_photos";
	private String STORE_TIMINGS ="stores_timings";
	private String ADD_STORE_PHOTOS ="add_store_photo";
	private String STORE_CARD_DETAILS ="gift_cards_stores";
	private String MY_FRIENDS = "my_friends";
	private String GET_STORE_SHARE_LINK= "share_location";
	private String STORE_VIDEO="store_video";
	private String SHARE_STORE="share_store";
	private String FB_LOGIN ="fb_login";
	private String STORE_COUPONS ="coupons_stores";
	private String CHECK_FAVORITE_COUPON = "check_favourite_coupons";
	private String ADD_FAVORITE_COUPON="add_to_favourite_coupons";
	private String SHARE_COUPON="share_coupon";
	private String FAVORITE_COUPONS="fav_coupons";
	private String FAVOURITE_STORES="fav_stores";                  // function to get favourite store
	private String FRIENDFAVOURITE_STORES="friends_fav_stores";
	private String GETCATOGORIES = "categories";
	private String GETNOTIFICATIONS = "get_notifications";
	private String SETNOTIFICATIONS = "set_notifications";
	private String GET_USER_PROFILE ="userprofile";
	private String UPDATE_USER_PROFILE="update_user_info";
	private String GET_USERSECURITY_QUESTIONS="get_security_qa";
	private String ZOUPONS_CHAT_LIST="zoupons_chat_response";
	private String ZOUPONS_CHAT="zoupons_chat";
	private String CHECK_STORE_NAME="check_store";
	private String GET_TERMS_CONDITIONS="terms_and_conditions";
	private String ADD_REWARDS="rewards"; 
	private String INVITE_FRIENDS="invite_friends";
	private String GET_ALL_GIFT_CARDS = "my_all_gift_cards";
	private String REEDEM_GIFT_CARD = "redeem_giftcard";
	private String SEND_GIFT_CARD="send_giftcards";
	private String GET_REVIEWDETAILS = "reviews";
	private String POST_STORE_REVIEW = "post_review";
	private String UPDATE_STORE_REVIEW ="update_review";
	private String GET_STATUS_REVIEW="review_rates";
	private String IN_APPROPRIATE_REVIEW="report_inappropriate";
	private String LIKE_DIALIKE_REVIEW="rate_review";
	private String GIFT_CARD_TRANSACTION_HISTORY ="giftcard_transaction_history";
	private String RECEIPTS ="my_receipts";
	private String GET_INVOICE_LIST="list_invoices";
	private String REJECT_INVOICE ="reject_invoice";
	private String MY_GIFT_CARDS="my_gift_cards";
	private String CHECK_USERPIN="check_user_pin"; 
	private String NORMAL_PAYMENT_URL="customer_initiated_payments";
	private String GETNOTIFICATION = "get_instant_notifications";
	private String CUSTOMER_COMMUNICATED_STORES = "customer_communicated_stores";
	private String GET_REWARDS_ADVERTISENT="get_ad";
	private String LOGOUT="logout";
	private String SOCIAL_LOGIN="import_contacts";
	private String SIGNUP_STEPONE="signup_step_one";
	private String UPDATE_NOTIFICATION_STATUS="update_notification_status";
	private String CHECK_ZOUPONSCUSTOMER="check_users_email";
	private String CHECK_USER_TYPE = "get_user_type";
	private String mReturnValue;
	private HttpClient mHttpClient;
	private HttpPost mHttpPost;
	private HttpResponse mResponse;
	private HttpEntity mEntity;
	private Context mContext;
	private String mTokenId;

	public ZouponsWebService(Context context){
		mContext = context;
		SharedPreferences mTokenIdPrefs = context.getSharedPreferences(ZouponsLogin.TOKENID_PREFENCES_FILE, Context.MODE_PRIVATE);
		if(!mTokenIdPrefs.getString("tokenid", "").equals("")) {
			mTokenId = mTokenIdPrefs.getString("tokenid", "").trim();
		}

		mHttpClient = new DefaultHttpClient();
	}

	/* To get security questions*/
	public String getSecurityQuestions(String emailid,int eventFlag){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_SECURITY_QUESTIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("email", emailid));
			if(eventFlag==0){	//True if called from create account signup
				list.add(new BasicNameValuePair("EventFlag", String.valueOf(eventFlag)));
			}else if(eventFlag==1){	// True if called from forgot password
				list.add(new BasicNameValuePair("EventFlag", String.valueOf(eventFlag)));
			}
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To get forgot password response */
	public String getforgot_pwd(String emailid,String securityquestionid1,String securityquestionid2,String answer1,String answer2){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FORGOT_PASSWORD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("email", emailid));
			list.add(new BasicNameValuePair("SecurityQues1", securityquestionid1));
			list.add(new BasicNameValuePair("SecurityAns1", answer1));
			list.add(new BasicNameValuePair("SecurityQues2", securityquestionid2));
			list.add(new BasicNameValuePair("SecurityAns2", answer2));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To get security questions*/
	public String securityQuestions(){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SECURITY_QUESTIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();

			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for sign up */
	public String signup(String profileimage,String firstname,String lastname,String emailid,String mobilenumber,String mobilecarrier,
			String pswd,String sq1id,String answer1,String sq2id,String answer2,String fbid,String fbaccesstoken, String addPin){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SIGNUP).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("Photo", profileimage));
			list.add(new BasicNameValuePair("account_loggin", AccountLoginFlag.accountsignupflag));
			list.add(new BasicNameValuePair("fb_id", fbid));
			list.add(new BasicNameValuePair("fb_access_token", fbaccesstoken));
			list.add(new BasicNameValuePair("FirstName", firstname));
			list.add(new BasicNameValuePair("LastName", lastname));
			list.add(new BasicNameValuePair("EmailAddress", emailid));
			list.add(new BasicNameValuePair("Password", pswd));
			list.add(new BasicNameValuePair("MobileNumber", mobilenumber));
			list.add(new BasicNameValuePair("MobileCarrier", mobilecarrier));
			list.add(new BasicNameValuePair("SecurityQues1", sq1id));
			list.add(new BasicNameValuePair("SecurityQues2", sq2id));
			list.add(new BasicNameValuePair("SecurityAns1", answer1));
			list.add(new BasicNameValuePair("SecurityAns2", answer2));
			list.add(new BasicNameValuePair("pin", addPin));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for sign up*/
	public String getSignUpStep1_Verify(String eventflag,String userid,String emailid,String verificationcode,String firstname,String lastname,String photo,String mobilenumber,String raisedBy,String locationId,String invoiceAmount,String couponcode,String storeNotes) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SIGNUP_STEPONE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("EventFlag", eventflag));
			list.add(new BasicNameValuePair("create_user_flag", "invoice"));
			list.add(new BasicNameValuePair("EmailAddress", emailid));
			list.add(new BasicNameValuePair("FirstName", firstname));
			list.add(new BasicNameValuePair("LastName", lastname));
			list.add(new BasicNameValuePair("Photo", photo));
			list.add(new BasicNameValuePair("MobileNumber", mobilenumber));
			list.add(new BasicNameValuePair("approved_by", raisedBy));
			list.add(new BasicNameValuePair("location_id", locationId));
			list.add(new BasicNameValuePair("amount_to_pay", invoiceAmount));
			list.add(new BasicNameValuePair("note", storeNotes));
			list.add(new BasicNameValuePair("coupon_code", couponcode));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
			//Log.i(TAG,"SignUpStep Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			//Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;

	}

	/* To call webservice for sign up stpe2*/
	public String signup_Step2(String userId,String pswd,String sq1id,String answer1,String sq2id,String answer2, String addPin){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SIGNUP_STEP2).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id",userId));
			list.add(new BasicNameValuePair("pin", addPin));
			list.add(new BasicNameValuePair("Password", pswd));
			list.add(new BasicNameValuePair("SecurityQues1", sq1id));
			list.add(new BasicNameValuePair("SecurityQues2", sq2id));
			list.add(new BasicNameValuePair("SecurityAns1", answer1));
			list.add(new BasicNameValuePair("SecurityAns2", answer2));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice activating customer */
	public String activateUser(String userid,String activationcode){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ACTIVATIONUSER).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", userid));
			list.add(new BasicNameValuePair("ActivationCode", activationcode));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for LOGIN */
	public String login(String username,String password){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(LOGIN).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("email", username));
			list.add(new BasicNameValuePair("password", password));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		Log.i("login response", mReturnValue);
		return mReturnValue;
	}

	public String checkUserType(String user_id) {
		// TODO Auto-generated method stub
		try{
			mHttpClient = new DefaultHttpClient();
			HttpPost mHttpPostRequest = new HttpPost(URL+CHECK_USER_TYPE);
			List<NameValuePair> mInputParamList = new ArrayList<NameValuePair>();
			mInputParamList.add(new BasicNameValuePair("user_id", user_id));
			mHttpPostRequest.setEntity(new UrlEncodedFormEntity(mInputParamList));
			mResponse = mHttpClient.execute(mHttpPostRequest);
			mEntity = mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch(Exception e){
			e.printStackTrace();
			mReturnValue = "failure";
		}
		Log.i("User type response:", mReturnValue);
		return mReturnValue;
	}


	/* To call webservice for add credit card */
	public String Getfirst_data_global_payment(String userid,String username,String cardnumber,String cvv,
			String expirydatemonth,String expirydateyear,String streetaddress,String zipcode,String cardname/*,String pinnumber*/,String userType, String storeId, boolean isFromStoreOwnerBilling){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FIRST_DATA_GLOBAL_PAYEMENT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", userid));
			list.add(new BasicNameValuePair("Type", "Normal"));    //Normal or Giftcard
			list.add(new BasicNameValuePair("TransactionType", "05"));
			list.add(new BasicNameValuePair("ExpiryDateMonth", expirydatemonth));
			list.add(new BasicNameValuePair("ExpiryDateYear", expirydateyear));
			list.add(new BasicNameValuePair("card_name", cardname));
			list.add(new BasicNameValuePair("CardHoldersName", username));
			list.add(new BasicNameValuePair("CVV", cvv));
			list.add(new BasicNameValuePair("Zipcode", zipcode));
			list.add(new BasicNameValuePair("StreetAddress", streetaddress));
			list.add(new BasicNameValuePair("UserType", userType));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			if(ManageCardAddPin_ClassVariables.mEditCardFlag.equals("true")){
				list.add(new BasicNameValuePair("CardId", CardId_ClassVariable.cardid));
			}else{
				list.add(new BasicNameValuePair("CardNumber", cardnumber));
			}
			if(isFromStoreOwnerBilling){
				list.add(new BasicNameValuePair("CardFlag", "billing"));
				list.add(new BasicNameValuePair("StoreId", storeId));
			}
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
			Log.i(TAG,"Getfirst_data_global_payment : "+mReturnValue);
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for changing password */
	public String Change_Password(String userid,String newpassword){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHANGE_PASSWORD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", userid));
			list.add(new BasicNameValuePair("NewPassword", newpassword));
			list.add(new BasicNameValuePair("EventFlag", "0"));	//EvenFlag "0" for password change.
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for Categories list */
	public String categories(){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CATEGORIES).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for sub categories list */
	public String subcategories(String id){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SUBCATEGORIES).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("parent_id", id));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for search store*/
	public String search(String searchtype,String searchterm,String searchlistingflag){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SEARCH).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("SearchType", searchtype));
			list.add(new BasicNameValuePair("SearchTerm", searchterm));
			list.add(new BasicNameValuePair("StoreListingFlag", searchlistingflag));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String store_locator(String latitude,String longitude,String storeid,String categoryid,String qrcode,double mapviewradius,String storelistingflag,String offset,String devicelat,String devicelong){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_LOCATOR).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			//By default lat & long for store_currentlocation, search stores and browse stores
			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("Latitude", latitude));
			list.add(new BasicNameValuePair("Longitude", longitude));
			list.add(new BasicNameValuePair("device_lat", devicelat));
			list.add(new BasicNameValuePair("device_lon", devicelong));
			//For search stores we have to send (StoreId,EventFlag=Multiple)
			list.add(new BasicNameValuePair("StoreId", storeid));
			list.add(new BasicNameValuePair("EventFlag", "single"));
			list.add(new BasicNameValuePair("CategoryId", categoryid));	//For browse stores we have to send (CategoryId)
			list.add(new BasicNameValuePair("QRCode", qrcode));
			list.add(new BasicNameValuePair("Distance", String.valueOf(mapviewradius)));
			list.add(new BasicNameValuePair("StoreListingFlag", storelistingflag));
			list.add(new BasicNameValuePair("offset", "0"));
			list.add(new BasicNameValuePair("limit", "50"));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Function to get CheckFavorite
	public String checkFavorites(String storeid,String userid){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHECK_FAVORITE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("StoreId", storeid));
			list.add(new BasicNameValuePair("UserId", userid));
			list.add(new BasicNameValuePair("location_id", RightMenuStoreId_ClassVariables.mLocationId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Function to get AddFavorite
	public String addFavorites(String storeid,String userid){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_FAVORITE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("StoreId", storeid));
			list.add(new BasicNameValuePair("UserId", userid));
			list.add(new BasicNameValuePair("location_id", RightMenuStoreId_ClassVariables.mLocationId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Function to get CardOnFiles
	public String cardOnFiles(String userid,String usertype, String pageFlag){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CARD_ON_FILE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", userid));
			list.add(new BasicNameValuePair("UserType", usertype));
			if(pageFlag.equalsIgnoreCase("customer_creditcards")){
				list.add(new BasicNameValuePair("StoreId", ""));	
			}else{
				SharedPreferences mStoreDetailsPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				String mStoreid = mStoreDetailsPrefs.getString("store_id", "");
				Log.i("store_id","From store billing " +mStoreid);
				list.add(new BasicNameValuePair("StoreId", mStoreid));
			}
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
			Log.i(TAG,"CardOnFiles Return Value : "+mReturnValue);
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Function to get update_user_pin
	public String updateuserpin(String pin){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(UPDATE_USER_PIN).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("PIN", new EncryptionClass().md5(pin)));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Function to get RemoveCard
	public String RemoveCard(String cardid){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(REMOVE_CARD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("card_id", cardid));
			list.add(new BasicNameValuePair("UserType", AccountLoginFlag.accountUserTypeflag));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for store location list */
	public String mStoreLocator(String currentLatitude,String currentLongtitude, String eventFlag, String storeID,String devicelat,String devicelong,String classname) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_LOCATOR).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if(classname.equalsIgnoreCase("ShopperHomePage")||classname.equalsIgnoreCase("CardPurchase")){
				list.add(new BasicNameValuePair("Latitude", currentLatitude));
				list.add(new BasicNameValuePair("Longitude",currentLongtitude));
				list.add(new BasicNameValuePair("device_lat", devicelat));
				list.add(new BasicNameValuePair("device_lon",devicelong));
			}else if(classname.equalsIgnoreCase("location")){
				list.add(new BasicNameValuePair("Latitude", devicelat));
				list.add(new BasicNameValuePair("Longitude",devicelong));
			}
			list.add(new BasicNameValuePair("EventFlag", eventFlag));
			list.add(new BasicNameValuePair("StoreId", storeID));
			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for getting store information */
	public String mStoreInformation(String storeID,String location_id) {
		// TODO Auto-generated method stub
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_INFO).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("StoreId", storeID));
			list.add(new BasicNameValuePair("location_id", location_id));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for store photos */
	public String mStorePhotos(String storeID,String location_id) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_PHOTOS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();

			list.add(new BasicNameValuePair("StoreId", storeID));
			list.add(new BasicNameValuePair("location_id", location_id));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
			//Log.i(TAG,"Stores_Info Return Value : "+mReturnValue);
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for fetching store business hours */
	public String mStoreTimings(String storeID, String locationId) {
		// TODO Auto-generated method stub
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_TIMINGS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();

			list.add(new BasicNameValuePair("StoreId", storeID));
			list.add(new BasicNameValuePair("location_id", locationId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
			/*Log.i(TAG,"Stores_Info Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for adding store photos */
	public String AddStorePhoto(String storeId,String location_id){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_STORE_PHOTOS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("StoreId", storeId));
			list.add(new BasicNameValuePair("Photo", POJOStorePhoto.add_photo_data));
			list.add(new BasicNameValuePair("location_id", location_id));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
			/*Log.i(TAG,"Add Photo "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// To add store photo 
	public String AddStorePhoto_Customer(String storeId,String location_id,File photofile){
		try{
			mHttpClient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(new StringBuilder().append(URL).append(ADD_STORE_PHOTOS).toString());
			FileBody filebodyPhoto = new FileBody(photofile);
			StringBody location = null;
			location = new StringBody(location_id, "text/plain", Charset.forName("UTF-8"));
			CustomMultiPartEntity reqEntity = new CustomMultiPartEntity(new CustomMultiPartEntity.ProgressListener() {
				@Override
				public void transferred(long num) {}
			});
			reqEntity.addPart("StoreId", new StringBody(storeId, "text/plain", Charset.forName("UTF-8")));
			reqEntity.addPart("location_id", new StringBody(location_id, "text/plain", Charset.forName("UTF-8")));
			reqEntity.addPart("app_token", new StringBody(mTokenId, "text/plain", Charset.forName("UTF-8")));
			reqEntity.addPart("uploadedfile", filebodyPhoto);
			httppost.setEntity(reqEntity);
			System.out.println( "executing request " + httppost.getRequestLine( ) );
			System.out.println( "executing location id param " + location.getCharset() + " "+location.getMimeType());
			String content = EntityUtils.toString(mHttpClient.execute(httppost).getEntity(), "UTF-8");
			//Log.i("response",content);
			if(!content.equals("")){
				mReturnValue = content;
			}else { // failure
				mReturnValue = "noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for fetching GC/DC details*/
	public String GetStoreCardDetails(String storeId,String cardType,String mStart){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_CARD_DETAILS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();

			if(cardType.equalsIgnoreCase("Zcard")){ // For Deal card we have to send store_id since it is store based
				list.add(new BasicNameValuePair("StoreId", storeId));	
			}		
			list.add(new BasicNameValuePair("CardType", cardType));
			list.add(new BasicNameValuePair("offset", mStart));
			list.add(new BasicNameValuePair("limit", "20"));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
			Log.i(TAG,"CardDetails "+mReturnValue);
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// To get social friend list
	public String getSocialFriends(){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(MY_FRIENDS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;	
	}

	/* To call webservice for fetching store share link for FB share */
	public String getStoreShareLink(){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_STORE_SHARE_LINK).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("location_id", RightMenuStoreId_ClassVariables.mLocationId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
			Log.i(TAG,"Social Like "+mReturnValue);
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;	
	}

	/*Function to get video id*/
	public String mGetVideo(String mStoreId,String location_id) {
		//Request to Store_Video Method 
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_VIDEO).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("StoreId", mStoreId));
			list.add(new BasicNameValuePair("location_id", location_id));
			list.add(new BasicNameValuePair("module_flag", "customer"));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/*Function to get video url from RTSP(Real Time Streaming Protocol) by use of video id*/
	public static String getUrlVideoRTSP(String urlYoutube){
		//Method to Get RTSP url from youtube video id
		try{
			String gdy = urlYoutube;
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			//Method to Get video id from youtube URL
			java.net.URL url = new java.net.URL(gdy);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			Document doc = documentBuilder.parse(connection.getInputStream());
			Element el = doc.getDocumentElement();
			NodeList list = el.getElementsByTagName("media:content");///media:content
			String cursor = urlYoutube;
			for (int i = 0; i < list.getLength(); i++){
				Node node = list.item(i);
				if (node != null){
					NamedNodeMap nodeMap = node.getAttributes();
					HashMap<String, String> maps = new HashMap<String, String>();
					for (int j = 0; j < nodeMap.getLength(); j++){
						Attr att = (Attr) nodeMap.item(j);
						maps.put(att.getName(), att.getValue());
					}
					if (maps.containsKey("yt:format")){
						String f = maps.get("yt:format");
						if (maps.containsKey("url")){
							cursor = maps.get("url");
						}
						if (f.equals("1"))
							return cursor;
					}
				}
			}
			return cursor;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return urlYoutube;

	}

	/*Function to Split video id from youtube url (embed url or watch me url)*/
	protected static String extractYoutubeId(String url) throws MalformedURLException{
		String id = null;
		try{
			String query = new java.net.URL(url).getQuery();
			if (query != null){
				String[] param = query.split("&");
				for (String row : param){
					String[] param1 = row.split("=");
					if (param1[0].equals("v")){
						id = param1[1];
					}
				}
			}else{
				if (url.contains("embed")){
					id = url.substring(url.lastIndexOf("/") + 1);
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return id;
	}

	/*Function to share store via email or facebook*/
	public String Share_Store(String sharetype, String sharemail, String mFriendUserId){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SHARE_STORE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("StoreId", RightMenuStoreId_ClassVariables.mStoreID));
			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("ShareType", sharetype));
			list.add(new BasicNameValuePair("location_id", RightMenuStoreId_ClassVariables.mLocationId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			if(sharetype.equalsIgnoreCase("Email")){
				list.add(new BasicNameValuePair("to_email", sharemail));
				list.add(new BasicNameValuePair("to_user_id", mFriendUserId));
			}
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/*Function to share store via email or facebook*/
	public String Share_Coupon(String sharetype,String couponid){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SHARE_COUPON).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ShareType", sharetype));
			list.add(new BasicNameValuePair("CouponId", couponid));
			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}
	/*Function to confirm fb login into our webservice*/
	public String FBLogin() {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FB_LOGIN).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();

			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("fb_id", UserDetails.mServiceFbId));
			list.add(new BasicNameValuePair("fb_access_token", UserDetails.mServiceAccessToken));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
			/*Log.i(TAG,"FB login "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for fetching store coupon list */
	public String mGetCoupons(String mCouponStart,String type,String location_id) {
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_COUPONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("offset", mCouponStart));
			if(!type.equalsIgnoreCase("")){    
				list.add(new BasicNameValuePair("type", type));
			}else{
				list.add(new BasicNameValuePair("user_id", UserDetails.mServiceUserId));
			}
			Resources resources = mContext.getResources();
			DisplayMetrics metrics = resources.getDisplayMetrics();
			int width_px = (int)(300 * (metrics.densityDpi / 160f));
			int height_px = (int)(100 * (metrics.densityDpi / 160f));  
			Log.i("width and height", width_px + " " + height_px); //900  //300
			list.add(new BasicNameValuePair("qr_code_width", String.valueOf(width_px)));
			list.add(new BasicNameValuePair("qr_code_height", String.valueOf(height_px)));
			list.add(new BasicNameValuePair("location_id", location_id));
			list.add(new BasicNameValuePair("limit", "20"));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);			
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity); 
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		Log.i("coupons list response", mReturnValue);
		return mReturnValue;
	}

	/* To call webservice for checking fav coupon for this User */
	public String mCheckIsFavoriteCoupon(String userid, String mCouponID) {
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHECK_FAVORITE_COUPON).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", userid));	
			list.add(new BasicNameValuePair("coupon_id", mCouponID));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for adding fav coupon */
	public String mAddFavoriteCoupon(String userid, String mCouponID) {
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_FAVORITE_COUPON).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", userid));	
			list.add(new BasicNameValuePair("coupon_id", mCouponID));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		Log.i(TAG,"Add Coupon Message :"+mReturnValue);
		return mReturnValue;
	}

	/* To call webservice for fetching favorite coupon */
	public String FavoriteCoupons(String offset) {
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FAVORITE_COUPONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			Resources resources = mContext.getResources();
			DisplayMetrics metrics = resources.getDisplayMetrics();
			int width_px = (int)(300 * (metrics.densityDpi / 160f));
			int height_px = (int)(100 * (metrics.densityDpi / 160f));  
			Log.i("width and height", width_px + " " + height_px); //900  //300
			list.add(new BasicNameValuePair("qr_code_width",String.valueOf(width_px)));
			list.add(new BasicNameValuePair("qr_code_height",String.valueOf(height_px)));
			list.add(new BasicNameValuePair("offset",offset));
			list.add(new BasicNameValuePair("limit", "20"));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		Log.i(TAG,"FAvorite coupon list :"+mReturnValue);
		return mReturnValue;
	}

	public String GetFavouriteStores(String FavouriteType, String offset) {
		try {
			mHttpClient = new DefaultHttpClient();
			if(FavouriteType.equalsIgnoreCase("FriendFavouriteStore")){ // Friend fav stores
				if(MainMenuActivity.mFavouritesStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.mFavouriteFriendStoreDetails.clear();
				}
				mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FRIENDFAVOURITE_STORES).toString());	
			}else{ // Favorite store
				if(MainMenuActivity.mFavouritesStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.mFavouriteStoreDetails.clear();
				}
				mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FAVOURITE_STORES).toString());
			}
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("offset",offset));
			list.add(new BasicNameValuePair("limit", "20"));
			list.add(new BasicNameValuePair("Latitude", CurrentLocation.CurrentLocation_Latitude));
			list.add(new BasicNameValuePair("Longitude", CurrentLocation.CurrentLocation_Longitude));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);			
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity); 
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for fetching categories list */
	public String GetCatogories() {
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GETCATOGORIES).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);			
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity); 
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for fetching notification details */
	public String GetNotificationDetails() {
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GETNOTIFICATIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("notify_to", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);			
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity); 
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		Log.i("Get Notification Details", mReturnValue);
		return mReturnValue;
	}

	/* To call webservice for setting notification details to user */
	public String SetNotificationDetails(String contactfrequency,String notifyby,String categories) {
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SETNOTIFICATIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("notify_to", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("contact_frequency", contactfrequency));
			list.add(new BasicNameValuePair("category", categories));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);			
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity); 
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for getting user profile */
	public String mGetUserProfile(String mUserId) {
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_USER_PROFILE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", mUserId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for getting security question */
	public String GetUserSecurityQuestions(String mUserId) {
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_USERSECURITY_QUESTIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", mUserId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Update User Profile Details
	public String mUpdateUserProfile(String mUserId, String mUserFirstName,
			String mUserLastName,  String mUserMobileNumber,
			String mUserMobileCarrier, String mUserImage,String eventFlag,String verification_code) {
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(UPDATE_USER_PROFILE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", mUserId));
			list.add(new BasicNameValuePair("FirstName", mUserFirstName));
			list.add(new BasicNameValuePair("LastName", mUserLastName));			
			list.add(new BasicNameValuePair("MobileNumber", mUserMobileNumber));
			list.add(new BasicNameValuePair("MobileCarrier", mUserMobileCarrier));
			list.add(new BasicNameValuePair("Photo", mUserImage));
			list.add(new BasicNameValuePair("EventFlag", eventFlag));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			if(eventFlag.equalsIgnoreCase("verify")){
				list.add(new BasicNameValuePair("VerificationCode", verification_code));
			}
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		Log.i("updating contact info", mReturnValue);
		return mReturnValue;
	}

	public String UpdateUserSecurityQuestions(String securityquestionid1,String securityquestionid2,String answer1,String answer2){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHANGE_PASSWORD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId",UserDetails.mServiceUserId ));
			list.add(new BasicNameValuePair("SecurityQues1", securityquestionid1));
			list.add(new BasicNameValuePair("SecurityAns1", answer1));
			list.add(new BasicNameValuePair("SecurityQues2", securityquestionid2));
			list.add(new BasicNameValuePair("SecurityAns2", answer2));
			list.add(new BasicNameValuePair("EventFlag", "1")); //EvenFlag "1" for change security questions.
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
		}catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Method to Send Message to Talk To Us
	public String mTalkToUs(String mUserId, String mStoreId, String mQuery, String moduleflag, String usertype,String locationid) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ZOUPONS_CHAT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if(moduleflag.equalsIgnoreCase("customer")){
				list.add(new BasicNameValuePair("from_user_id", mUserId));
				list.add(new BasicNameValuePair("from_store_id", ""));
				list.add(new BasicNameValuePair("to_user_id", "0"));
				list.add(new BasicNameValuePair("to_store_id", mStoreId));
				list.add(new BasicNameValuePair("user_type", usertype));
				list.add(new BasicNameValuePair("module_flag", "zoupons_customer"));
				list.add(new BasicNameValuePair("query", mQuery));
			}else if(moduleflag.equalsIgnoreCase("store")){
				list.add(new BasicNameValuePair("from_user_id", mUserId));
				list.add(new BasicNameValuePair("from_store_id", locationid));
				list.add(new BasicNameValuePair("to_user_id", "0"));
				list.add(new BasicNameValuePair("to_store_id", mStoreId));
				list.add(new BasicNameValuePair("user_type", usertype));
				list.add(new BasicNameValuePair("module_flag", "store_zoupons"));
				list.add(new BasicNameValuePair("query", mQuery));
			}
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
			//Log.i(TAG,"Talk to Us Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Method to send Message to Contact Us in Store
	public String mContactToUs(String mUserId, String storeid, String Query,String locationid,String userType,String moduleflag,String storeemployeeid) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ZOUPONS_CHAT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if(moduleflag.equalsIgnoreCase("customer")){ 
				list.add(new BasicNameValuePair("from_user_id", mUserId));	
				list.add(new BasicNameValuePair("from_store_id", ""));	
				list.add(new BasicNameValuePair("to_user_id", ""));
				list.add(new BasicNameValuePair("to_store_id", locationid));
				list.add(new BasicNameValuePair("user_type", userType));
				list.add(new BasicNameValuePair("query", Query));
			}else if(moduleflag.equalsIgnoreCase("store")){
				list.add(new BasicNameValuePair("from_user_id", storeemployeeid));	
				list.add(new BasicNameValuePair("from_store_id", locationid));	
				list.add(new BasicNameValuePair("to_user_id", mUserId));
				list.add(new BasicNameValuePair("to_store_id", ""));
				list.add(new BasicNameValuePair("user_type", userType));
				list.add(new BasicNameValuePair("query", Query));
			}
			list.add(new BasicNameValuePair("module_flag", "store_customer"));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// Get ContactUs and ZouponsSupport Message Response From Service
	public String mContactToUsResponse(String flagactivity,String mUserId, String storeid,String location_id,String user_type,String moduleflag,String flag,String notification_id,String senderFlag) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ZOUPONS_CHAT_LIST).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if(flagactivity.equalsIgnoreCase("ZouponsSupport")){
				list.add(new BasicNameValuePair("user_id", mUserId));
				list.add(new BasicNameValuePair("store_id", storeid));
				list.add(new BasicNameValuePair("user_type", user_type));
				list.add(new BasicNameValuePair("module_flag", moduleflag));
				list.add(new BasicNameValuePair("location_id", location_id));
			}else{
				list.add(new BasicNameValuePair("user_id", mUserId));
				list.add(new BasicNameValuePair("store_id", storeid));
				list.add(new BasicNameValuePair("user_type", user_type));
				list.add(new BasicNameValuePair("module_flag", moduleflag));
				list.add(new BasicNameValuePair("location_id", location_id));
			}
			list.add(new BasicNameValuePair("flag", flag));
			list.add(new BasicNameValuePair("id", notification_id));
			list.add(new BasicNameValuePair("sender_flag", senderFlag));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		Log.i("Chat support response", mReturnValue);
		return mReturnValue;
	}

	// function to get advertisement from zoupons..
	public String GetAdvertisement(String deviceId,String slot_type,String user_id) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_REWARDS_ADVERTISENT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", user_id));	
			list.add(new BasicNameValuePair("device_id",deviceId));
			list.add(new BasicNameValuePair("slot",slot_type));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for check store name availability*/
	public String mCheckStoreName(String mStoreName) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHECK_STORE_NAME).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("store_name", mStoreName));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}
	//Get Terms and conditions
	public String mGetTermsConditions() {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_TERMS_CONDITIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();

			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Add Store in Rewards
	public String mAddReward(String userid, String store_name, String store_manager_name,
			String photo1, String photo2, String address_line1, String address_line2,
			String state, String city, String zipcode, String phone,
			String review_message, String review_rate) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_REWARDS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", userid));	
			list.add(new BasicNameValuePair("store_name", store_name));	
			list.add(new BasicNameValuePair("store_manager_name", store_manager_name));	
			list.add(new BasicNameValuePair("photo1", photo1));	
			list.add(new BasicNameValuePair("photo2", photo2));	
			list.add(new BasicNameValuePair("address_line1", address_line1));	
			list.add(new BasicNameValuePair("address_line2", address_line2));	
			list.add(new BasicNameValuePair("state", state));	
			list.add(new BasicNameValuePair("city", city));	
			list.add(new BasicNameValuePair("zipcode", zipcode));	
			list.add(new BasicNameValuePair("phone", phone));	
			list.add(new BasicNameValuePair("review_message", review_message));	
			list.add(new BasicNameValuePair("review_rate", review_rate));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String inviteFriend(String friendname) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(INVITE_FRIENDS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("friends", friendname));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mGetAllGiftCards(String userId, String mStartLimit) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_ALL_GIFT_CARDS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId",userId));	
			list.add(new BasicNameValuePair("offset",mStartLimit));	
			list.add(new BasicNameValuePair("limit", "20"));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mRedeemGiftCard(String mUserId, String verificationcode) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(REEDEM_GIFT_CARD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", mUserId));	
			list.add(new BasicNameValuePair("verification_code", verificationcode));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mSendGiftCard(String mServiceUserId, String mSend_To,
			String mSend_date, String mGiftCardId, String mMessage, String mEventFlag, String TimeZone, String Friendemail_address) {
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SEND_GIFT_CARD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", mServiceUserId));	
			list.add(new BasicNameValuePair("send_to", mSend_To));
			list.add(new BasicNameValuePair("new_email", Friendemail_address));
			list.add(new BasicNameValuePair("send_date", convertDeviceTimeToServerTime(mSend_date, TimeZone)));
			list.add(new BasicNameValuePair("gift_card_purchase_id", MenuUtilityClass.sGiftCardPurchaseId));
			list.add(new BasicNameValuePair("giftcard_id", mGiftCardId));
			list.add(new BasicNameValuePair("balance_amount", String.valueOf(MenuUtilityClass.sGiftCardBalanceAmount)));
			list.add(new BasicNameValuePair("Message", mMessage));
			list.add(new BasicNameValuePair("card_type", MenuUtilityClass.sGiftCard_Type));
			list.add(new BasicNameValuePair("store_id", MenuUtilityClass.sGiftCard_StoreId));
			list.add(new BasicNameValuePair("location_id", MenuUtilityClass.sGiftCardStoreLocationId));
			list.add(new BasicNameValuePair("EventFlag", mEventFlag));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for fetching review details */
	public String getReviewDetails(String offset,String user_id,String store_id,String locationid) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_REVIEWDETAILS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId",user_id));
			list.add(new BasicNameValuePair("StoreId", store_id));
			list.add(new BasicNameValuePair("location_id", locationid));
			list.add(new BasicNameValuePair("offset",offset));
			list.add(new BasicNameValuePair("limit", "20"));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for posting review to store */
	public String mPostStoreReviews(String userId, String storeId, String ratings,
			String reviews) {
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(POST_STORE_REVIEW).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", userId));	
			list.add(new BasicNameValuePair("StoreId", storeId));	
			list.add(new BasicNameValuePair("location_id", RightMenuStoreId_ClassVariables.mLocationId));
			list.add(new BasicNameValuePair("Rating", ratings));	
			list.add(new BasicNameValuePair("Review", reviews));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for updating store review details */
	public String mUpdateStoreReviews(String reviewid, String rating,
			String review) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(UPDATE_STORE_REVIEW).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("ReviewId", reviewid));	
			list.add(new BasicNameValuePair("Rating", rating));	
			list.add(new BasicNameValuePair("Review", review));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for getting review status */
	public String mGetReviewStatus(String userId, String storeId) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_STATUS_REVIEW).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", userId));	
			list.add(new BasicNameValuePair("StoreId", storeId));	
			list.add(new BasicNameValuePair("location_id", RightMenuStoreId_ClassVariables.mLocationId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for requesting inappropiate review */
	public String mReviewInApproriate(String userId, String reviewid) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(IN_APPROPRIATE_REVIEW).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", userId));	
			list.add(new BasicNameValuePair("ReviewId", reviewid));	
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for like/dislike review */
	public String ReviewLikeDisLike(String userId, String storeid,
			String reviewid, String rate) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(LIKE_DIALIKE_REVIEW).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", userId));	
			list.add(new BasicNameValuePair("StoreId", storeid));
			list.add(new BasicNameValuePair("location_id", RightMenuStoreId_ClassVariables.mLocationId));
			list.add(new BasicNameValuePair("ReviewId", reviewid));	
			list.add(new BasicNameValuePair("Rate", rate));	
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for fetching gitfcard transaction history */
	public String mGetGiftCardTransactionHistory(String mServiceUserId,	String giftcardid) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GIFT_CARD_TRANSACTION_HISTORY).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", mServiceUserId));	
			list.add(new BasicNameValuePair("gift_card_purchase_id", giftcardid));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		Log.i("Transaction History Response", mReturnValue);
		return mReturnValue;
	}

	/* To call webservice for fetching receipts details */
	public String GetReceiptsDetails(String mReceiptStart,String user_id,String location_id){
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(RECEIPTS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", user_id));
			list.add(new BasicNameValuePair("offset",mReceiptStart));	
			list.add(new BasicNameValuePair("limit", "20"));
			list.add(new BasicNameValuePair("location_id", location_id));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
			Log.i(TAG,"receipts list "+mReturnValue);
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;	
	}

	public String mGetInvoiceListStatus(String mUserID) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_INVOICE_LIST).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", mUserID));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
			Log.i(TAG,"Get Invoice List Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for rejecting raised invoice */
	public String mGetRejectInvoiceStatus(String mInvoiceId) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(REJECT_INVOICE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("invoice_id", mInvoiceId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
			Log.i(TAG,"Reject Invoice Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for checking secret PIN */
	public String checkUserPIN(String enterdPIN,String userid,String user_type) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHECK_USERPIN).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", userid));
			list.add(new BasicNameValuePair("PIN", new EncryptionClass().md5(enterdPIN)));
			list.add(new BasicNameValuePair("UserType", user_type));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for fetching gc list */
	public String myGiftCards(String StoreId,String user_id,String user_type) {
		try {			
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(MY_GIFT_CARDS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", user_id));
			list.add(new BasicNameValuePair("location_id", StoreId));
			list.add(new BasicNameValuePair("UserType", user_type));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// for giftcard purchase
	public String purchaseGiftcard(String user_id,String user_type,String mCreditcardId, String mGiftcardId,
			String mCardvalue, String mFriendUserId, String emailaddress,String mFriendNotes,
			String mDate, String mTimeZone, String mIsZouponsfriend,String mInvoiceNote,String mStoreId,String mCardType,String storelocationid,String mFacevalue) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FIRST_DATA_GLOBAL_PAYEMENT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", user_id));
			list.add(new BasicNameValuePair("UserType", user_type));
			list.add(new BasicNameValuePair("Type", "Giftcard"));
			list.add(new BasicNameValuePair("TransactionType", "02"));
			list.add(new BasicNameValuePair("StoreId", mStoreId));
			list.add(new BasicNameValuePair("location_id", storelocationid));
			list.add(new BasicNameValuePair("CardId", mCreditcardId));
			list.add(new BasicNameValuePair("GiftcardId", mGiftcardId));
			if(mCardType.equalsIgnoreCase("Regular")){ // Giftcard
				list.add(new BasicNameValuePair("FaceValue", mCardvalue));
				list.add(new BasicNameValuePair("DollarAmount", mCardvalue));
			}else{ // Deal card
				list.add(new BasicNameValuePair("FaceValue", mFacevalue));
				list.add(new BasicNameValuePair("DollarAmount", mCardvalue));
			}
			list.add(new BasicNameValuePair("CardType", mCardType));
			if(!emailaddress.equalsIgnoreCase("")){ // Send to friend
				list.add(new BasicNameValuePair("send_to", mFriendUserId));
				list.add(new BasicNameValuePair("new_email", emailaddress));
				list.add(new BasicNameValuePair("Message", mFriendNotes));
				String serverDate = convertDeviceTimeToServerTime(mDate, mTimeZone);
				list.add(new BasicNameValuePair("SendDate", serverDate));
				//Log.i(TAG,"Server Date : "+serverDate);
				list.add(new BasicNameValuePair("ZPFriendFlag", mIsZouponsfriend));
				list.add(new BasicNameValuePair("PurchaseFlag", "Friend"));
			}else{ // Self purchase
				list.add(new BasicNameValuePair("PurchaseFlag", "Self"));
			}
			list.add(new BasicNameValuePair("invoice_note", mInvoiceNote));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		Log.i("Giftcard pur/send to frind", mReturnValue);
		return mReturnValue;
	}

	// payment from invoice by using only giftcard	
	public String payInvoiceUsingGiftcard(String mInvoiceId, String mTipAmount,String mTotalAmount,String mChoosedGiftCardPurchaseId,
			String mInvoiceRaisedBy, String mInvoiceRaisedByType, String mStoreID,String storelocationid,String paymentNote) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FIRST_DATA_GLOBAL_PAYEMENT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("InvoiceId", mInvoiceId));
			list.add(new BasicNameValuePair("StoreEmployeeId", mInvoiceRaisedBy));
			list.add(new BasicNameValuePair("RaisedByType", mInvoiceRaisedByType));
			list.add(new BasicNameValuePair("TransactionType", "02"));
			list.add(new BasicNameValuePair("UserType", AccountLoginFlag.accountUserTypeflag));
			list.add(new BasicNameValuePair("Type", "Normal"));
			list.add(new BasicNameValuePair("StoreId", mStoreID));
			list.add(new BasicNameValuePair("location_id", storelocationid));
			list.add(new BasicNameValuePair("tip_amount", mTipAmount));
			list.add(new BasicNameValuePair("DollarAmount", mTotalAmount));
			list.add(new BasicNameValuePair("gift_card_purchase_id", mChoosedGiftCardPurchaseId));
			list.add(new BasicNameValuePair("PurchaseFlag", "Giftcard"));
			list.add(new BasicNameValuePair("invoice_note", paymentNote));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
			Log.i(TAG,"Invoice approval using GC Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	} 	
	// payment from invoice by using only credit card	
	public String payInvoiceUsingCreditCard(String mInvoiceId,String mInvoiceRaisedBy, String mInvoiceRaisedByType,String mCreditcardId, String mStoreID, String mTipAmount,
			String mTotalAmount,String storelocationid,String paymentNote) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FIRST_DATA_GLOBAL_PAYEMENT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("InvoiceId", mInvoiceId));
			list.add(new BasicNameValuePair("UserType", AccountLoginFlag.accountUserTypeflag));
			list.add(new BasicNameValuePair("Type", "Normal"));
			list.add(new BasicNameValuePair("TransactionType", "02"));
			list.add(new BasicNameValuePair("StoreEmployeeId", mInvoiceRaisedBy));
			list.add(new BasicNameValuePair("RaisedByType", mInvoiceRaisedByType));
			list.add(new BasicNameValuePair("StoreId", mStoreID));
			list.add(new BasicNameValuePair("location_id",storelocationid));
			list.add(new BasicNameValuePair("CardId", mCreditcardId));
			list.add(new BasicNameValuePair("tip_amount", mTipAmount));
			list.add(new BasicNameValuePair("DollarAmount", mTotalAmount));
			list.add(new BasicNameValuePair("PurchaseFlag", "Creditcard"));
			list.add(new BasicNameValuePair("invoice_note", paymentNote));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// payment from invoice by using both cards	
	public String payInvoiceUsingBothCard(String mInvoiceId,String mInvoiceRaisedBy, String mInvoiceRaisedByType,String mCreditcardId, String mAmountOnCreditCard, String mStoreID,
			String mAmountOnGiftCard,String mTipAmount,String mChoosedGiftCardPurchaseId,String mTotalAmount,String storelocationid,String paymentNote) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FIRST_DATA_GLOBAL_PAYEMENT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("InvoiceId", mInvoiceId));
			list.add(new BasicNameValuePair("StoreEmployeeId", mInvoiceRaisedBy));
			list.add(new BasicNameValuePair("RaisedByType", mInvoiceRaisedByType));
			list.add(new BasicNameValuePair("TransactionType", "02"));
			list.add(new BasicNameValuePair("UserType", AccountLoginFlag.accountUserTypeflag));
			list.add(new BasicNameValuePair("Type", "Normal"));
			list.add(new BasicNameValuePair("PurchaseFlag", "Both"));
			list.add(new BasicNameValuePair("StoreId", mStoreID));
			list.add(new BasicNameValuePair("location_id", storelocationid));
			list.add(new BasicNameValuePair("CardId", mCreditcardId));
			list.add(new BasicNameValuePair("creditcard_amount", mAmountOnCreditCard));
			list.add(new BasicNameValuePair("gift_card_purchase_id", mChoosedGiftCardPurchaseId));
			list.add(new BasicNameValuePair("giftcard_amount",mAmountOnGiftCard));
			list.add(new BasicNameValuePair("tip_amount", mTipAmount));
			list.add(new BasicNameValuePair("DollarAmount", mTotalAmount));
			list.add(new BasicNameValuePair("invoice_note", paymentNote));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;

	}

	/* To call webservice for Payment using GC */
	public String NormalPaymnetUsingGiftcard(String pageFlag,String user_id,String mChoosedGiftcardId, String mTipAmount,String mTotalAmount,String mChoosedGiftCardPurchaseId, String mNote, String mActualAmount,int mQRwidth,int mQRheight) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(NORMAL_PAYMENT_URL).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if(pageFlag.equalsIgnoreCase("from_customer")){ // Customer module
				list.add(new BasicNameValuePair("user_id", user_id));
			}else{ // Store module
				list.add(new BasicNameValuePair("module_flag", "store"));
				SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				String store_owner_employee_user_id = mPrefs.getString("user_id", "");
				list.add(new BasicNameValuePair("user_id", store_owner_employee_user_id));
				list.add(new BasicNameValuePair("raised_to", user_id));
			}
			list.add(new BasicNameValuePair("EventFlag", "Giftcard"));
			list.add(new BasicNameValuePair("giftcard_id", mChoosedGiftcardId));
			list.add(new BasicNameValuePair("tip_amount", mTipAmount));
			list.add(new BasicNameValuePair("total_amount", mTotalAmount));
			list.add(new BasicNameValuePair("gift_card_purchase_id", mChoosedGiftCardPurchaseId));
			list.add(new BasicNameValuePair("note", mNote));
			list.add(new BasicNameValuePair("actual_amount", mActualAmount));
			list.add(new BasicNameValuePair("qr_code_width", String.valueOf(mQRwidth)));
			list.add(new BasicNameValuePair("qr_code_height", String.valueOf(mQRheight)));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		Log.i("payment return", mReturnValue);
		return mReturnValue;
	} 	

	/* To call webservice for Payment using Credit card */	
	public String NormalPaymnetUsingCreditCard(String pageFlag,String user_id,String mCreditcardId, String mStoreID, String mTipAmount, String mTotalAmount, String mNote, String mActualAmount,String storelocationid,int mQRwidth,int mQRheight) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(NORMAL_PAYMENT_URL).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if(pageFlag.equalsIgnoreCase("from_customer")){ // Customer module
				list.add(new BasicNameValuePair("user_id", user_id));
			}else{ // Store module
				list.add(new BasicNameValuePair("module_flag", "store"));
				SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				String store_owner_employee_user_id = mPrefs.getString("user_id", "");
				list.add(new BasicNameValuePair("user_id", store_owner_employee_user_id));
				list.add(new BasicNameValuePair("raised_to", user_id));
			}
			list.add(new BasicNameValuePair("EventFlag", "Creditcard"));
			list.add(new BasicNameValuePair("card_id", mCreditcardId));
			list.add(new BasicNameValuePair("store_id", mStoreID));
			list.add(new BasicNameValuePair("location_id", storelocationid));
			list.add(new BasicNameValuePair("tip_amount", mTipAmount));
			list.add(new BasicNameValuePair("total_amount", mTotalAmount));
			list.add(new BasicNameValuePair("note", mNote));
			list.add(new BasicNameValuePair("actual_amount", mActualAmount));
			list.add(new BasicNameValuePair("qr_code_width", String.valueOf(mQRwidth)));
			list.add(new BasicNameValuePair("qr_code_height", String.valueOf(mQRheight)));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		Log.i("payment return", mReturnValue);
		return mReturnValue;
	}

	// payment from invoice by using both cards	
	public String NormalPaymnetUsingBothCard(String pageFlag,String user_id,String mCreditcardId, String mAmountOnCreditCard, String mStoreID,
			String mChoosedGiftcardId, String mAmountOnGiftCard,String mTipAmount,String mTotalAmount,String mChoosedGiftCardPurchaseId, String mNote, String mActualAmount,String storelocationid,int mQRwidth,int mQRheight) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(NORMAL_PAYMENT_URL).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			if(pageFlag.equalsIgnoreCase("from_customer")){ // customer module
				list.add(new BasicNameValuePair("user_id", user_id));
			}else{ // store module
				list.add(new BasicNameValuePair("module_flag", "store"));
				SharedPreferences mPrefs = mContext.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
				String store_owner_employee_user_id = mPrefs.getString("user_id", "");
				list.add(new BasicNameValuePair("user_id", store_owner_employee_user_id));
				list.add(new BasicNameValuePair("raised_to", user_id));
			}
			list.add(new BasicNameValuePair("EventFlag", "Both"));
			list.add(new BasicNameValuePair("card_id", mCreditcardId));
			list.add(new BasicNameValuePair("store_id", mStoreID));
			list.add(new BasicNameValuePair("location_id",storelocationid));
			list.add(new BasicNameValuePair("creditcard_amount", mAmountOnCreditCard));
			list.add(new BasicNameValuePair("giftcard_id",mChoosedGiftcardId));
			list.add(new BasicNameValuePair("giftcard_amount",mAmountOnGiftCard));
			list.add(new BasicNameValuePair("tip_amount", mTipAmount));
			list.add(new BasicNameValuePair("total_amount", mTotalAmount));
			list.add(new BasicNameValuePair("gift_card_purchase_id", mChoosedGiftCardPurchaseId));
			list.add(new BasicNameValuePair("note", mNote));
			list.add(new BasicNameValuePair("actual_amount", mActualAmount));
			list.add(new BasicNameValuePair("qr_code_width", String.valueOf(mQRwidth)));
			list.add(new BasicNameValuePair("qr_code_height", String.valueOf(mQRheight)));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		Log.i("payment return", mReturnValue);
		return mReturnValue;

	}

	//To call Notification Service
	public String getNotification(String notify_to_type, String module_flag,String notification_flag, String recentNotificationId,String location_id) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GETNOTIFICATION).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			SharedPreferences mPrefs = mContext.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
			String user_id = mPrefs.getString("user_id", "");
			list.add(new BasicNameValuePair("user_id", user_id));
			list.add(new BasicNameValuePair("module_flag", module_flag));
			list.add(new BasicNameValuePair("flag", notification_flag));
			if(notification_flag.equalsIgnoreCase("refresh") || notification_flag.equalsIgnoreCase("scroll"))
				list.add(new BasicNameValuePair("id", recentNotificationId));
			if(module_flag.equalsIgnoreCase("store"))
				list.add(new BasicNameValuePair("location_id", location_id));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		Log.i("Notifications response", mReturnValue);
		return mReturnValue;
	}

	//To call Notification Service
	public String getCustomerCommunicatedStores(String offset) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CUSTOMER_COMMUNICATED_STORES).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("offset", offset));
			list.add(new BasicNameValuePair("limit", "20"));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}	

	// Function to call webservice to end the login session
	public String endLoginSession(String mServiceUserId,String deviceId) {
		String mReturnValue;
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(LOGOUT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", mServiceUserId));
			list.add(new BasicNameValuePair("device_id", deviceId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// To import friends from social services
	public String importfriends(String provider,String providerId,String accessToken){
		String mReturnValue="";
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SOCIAL_LOGIN).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("provider", provider));
			list.add(new BasicNameValuePair("access_token", accessToken));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			if(provider.equalsIgnoreCase("Facebook")){
				list.add(new BasicNameValuePair("provider_id", providerId));
			}
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
			//Log.i(TAG,"Import friends WS Return Value : "+mReturnValue);
		} catch (Exception e) {
			e.printStackTrace();
			mReturnValue = "failure";
		} 
		return mReturnValue;
	}

	//To call sms Service
	public String getSignUpStep1_Sms(String eventflag,String userid,String firstname,String lastname,String photo,String mobilenumber) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SIGNUP_STEPONE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("EventFlag", eventflag));
			list.add(new BasicNameValuePair("UserId", userid));
			list.add(new BasicNameValuePair("FirstName", firstname));
			list.add(new BasicNameValuePair("LastName", lastname));
			list.add(new BasicNameValuePair("Photo", photo));
			list.add(new BasicNameValuePair("MobileNumber", mobilenumber));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;

	}

	public String getSignUpStep1_Verify(String eventflag,String userid,String emailid,String verificationcode,String firstname,String lastname,String photo,String mobilenumber) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SIGNUP_STEPONE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("EventFlag", eventflag));
			list.add(new BasicNameValuePair("UserId", userid));
			list.add(new BasicNameValuePair("EmailAddress", emailid));
			list.add(new BasicNameValuePair("VerificationCode", verificationcode));
			list.add(new BasicNameValuePair("FirstName", firstname));
			list.add(new BasicNameValuePair("LastName", lastname));
			list.add(new BasicNameValuePair("Photo", photo));
			list.add(new BasicNameValuePair("MobileNumber", mobilenumber));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;

	}

	//To call Update Notification Status
	public String UpdateNotificationStatus(String notificationids) {
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(UPDATE_NOTIFICATION_STATUS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("notification_ids", notificationids));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	/* To call webservice for checking zoupons customer */
	public String checkZouponsCustomer(StringBuilder email_ids,String user_id) {
		Log.i("user id", user_id);
		try{
			mHttpClient = new DefaultHttpClient();
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHECK_ZOUPONSCUSTOMER).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", user_id));
			list.add(new BasicNameValuePair("email_ids", email_ids.toString()));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		Log.i("Email id return value", mReturnValue);
		return mReturnValue;
	}

	/* To conver xmlstring to xmlDoc */
	public Document XMLfromString(String xml){
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);
		}catch(ParserConfigurationException e){
			e.printStackTrace();
			return null;
		}catch(SAXException e){
			e.printStackTrace();
			return null;
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		return doc;
	}

	// To get nodes size 
	public int numResults(Document doc){
		Node results = doc.getDocumentElement();
		int res = -1;
		try{
			res = Integer.valueOf(results.getChildNodes().getLength());
		}catch(Exception e){
			res = -1;
		}
		return res;
	}

	/* To get node value*/
	public static String getValue(Element item,String str){
		NodeList nodelist = item.getElementsByTagName(str);
		return ZouponsWebService.getElementValue(nodelist.item(0));
	}

	/* To get node value*/
	public final static String getElementValue(Node element){
		Node node;
		StringBuilder specialString;
		if(element != null){
			if(element.hasChildNodes()){
				specialString=new StringBuilder();
				for(node = element.getFirstChild();node != null;node = node.getNextSibling()){
					specialString.append(node.getNodeValue());
				}
				return specialString.toString().trim();
			}
		}
		return "";
	}
	/* To convert device time to server time*/
	private String convertDeviceTimeToServerTime(String senddate,String servertimezone){
		try {
			//get the current time and date
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			String hour_minute = hour +":"+ minute;
			senddate = senddate +" "+hour_minute;
			final SimpleDateFormat device_format = new SimpleDateFormat("yyyy-MM-dd H:mm");
			device_format.setTimeZone(TimeZone.getDefault());
			Date dateObj = device_format.parse(senddate);
			SimpleDateFormat server_format = new SimpleDateFormat("yyyy-MM-dd");
			server_format.setTimeZone(TimeZone.getTimeZone(servertimezone));
			return server_format.format(dateObj);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}


}
