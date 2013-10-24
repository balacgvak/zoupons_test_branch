/**
 * 
 */
package com.us.zoupons.WebService;

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
import org.apache.http.client.methods.HttpGet;
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
import android.util.Log;

import com.us.zoupons.EncryptionClass;
import com.us.zoupons.MenuUtilityClass;
import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.ClassVariables.CardId_ClassVariable;
import com.us.zoupons.ClassVariables.CurrentLocation;
import com.us.zoupons.ClassVariables.ManageCardAddPin_ClassVariables;
import com.us.zoupons.ClassVariables.POJOStorePhoto;
import com.us.zoupons.ClassVariables.RightMenuStoreId_ClassVariables;
import com.us.zoupons.ClassVariables.UserDetails;
import com.us.zoupons.FlagClasses.AccountLoginFlag;
import com.us.zoupons.GoogleAccountHelper.OAuth2clientCredentials;
import com.us.zoupons.MainMenu.MainMenuActivity;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.storeowner.videos.CustomMultiPartEntity;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * Class to invoke all webservice functions
 */
public class ZouponsWebService {

	private static String TAG=ZouponsWebService.class.getName();
	private String URL = "https://www.zoupons.com/dev1/webservices/";
	//private String URL = "https://www.zoupons.com/webservices/";
	private final String GET_SECURITY_QUESTIONS="get_security_questions";
	private final String MOBILE_CARRIERS="mobile_carriers";
	private final String FORGOT_PASSWORD="forgot_pwd";
	private final String SECURITY_QUESTIONS="security_ques"; 
	private final String SIGNUP="signup";
	private String SIGNUP_STEP2="signup_step_two";
	private final String ACTIVATIONUSER="activateuser";
	private final String LOGIN="login";
	private final String FIRST_DATA_GLOBAL_PAYEMENT ="first_data_global_payment";
	private final String CHANGE_PASSWORD="change_pwd";
	private final String CATEGORIES="categories";
	private String SUBCATEGORIES="subcategories";
	private String STORECATEGORIES="stores_categories";
	private String SEARCH="search";
	private String STORES_LOCATION="stores_location";
	private String STORE_LOCATOR="store_locator";
	private String STORE_QRCODE="store_qr_code";
	private String CHECK_FAVORITE="check_favourites";
	private String ADD_FAVORITE="add_to_favourites";
	private String CARD_ON_FILE="cards_on_file";
	private String UPDATE_USER_PIN="update_user_pin";
	private String REMOVE_CARD="remove_card";
	private String TEST5="test5";
	private String STORE_INFO="stores";
	private String STORE_PHOTOS ="stores_photos";
	private String STORE_TIMINGS ="stores_timings";
	private String ADD_STORE_PHOTOS ="add_store_photo";
	private String STORE_CARD_DETAILS ="gift_cards_stores";
	private String MY_FRIENDS = "my_friends";
	private String FB_LIKE = "fb_like";
	private String STORE_LIKE = "like";
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
	private String TALK_TO_US="talk_to_us";
	private String CONTACT_TO_STORE="contact_store";
	private String CONTACT_TO_US_RESPONSE="contact_store_response";
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
	private String INVOICE_PAY_USING_GIFTCARD="approve_invoice"; 
	private String NORMAL_PAYMENT_URL="customer_initiated_payments";
	private String GETNOTIFICATION = "get_instant_notifications";
	private String CUSTOMER_COMMUNICATED_STORES = "customer_communicated_stores";
	private String GET_REWARDS_ADVERTISENT="get_ad";
	private String LOGOUT="logout";
	private String SOCIAL_LOGIN="import_contacts";
	private String SIGNUP_STEPONE="signup_step_one";
	private String UPDATE_NOTIFICATION_STATUS="update_notification_status";

	private String mReturnValue;
	private HttpClient mHttpClient;
	private HttpPost mHttpPost;
	private HttpResponse mResponse;
	private HttpEntity mEntity;
	public static String mLimit="20";
	private Context mContext;
	private boolean mIsSSLCertificateCompatible = false;
	private String mTokenId;

	public ZouponsWebService(Context context){
		mContext = context;
		// to get os ssl compatable from preference
		SharedPreferences mPrefs = context.getSharedPreferences(ZouponsLogin.PREFENCES_FILE, Context.MODE_PRIVATE);
		if(mPrefs.getString("ssl_compatable", "").equals("yes")) {
			mIsSSLCertificateCompatible = true;
		}else{
			mIsSSLCertificateCompatible = false;
		}

		SharedPreferences mTokenIdPrefs = context.getSharedPreferences(ZouponsLogin.TOKENID_PREFENCES_FILE, Context.MODE_PRIVATE);
		if(!mTokenIdPrefs.getString("tokenid", "").equals("")) {
			mTokenId = mTokenIdPrefs.getString("tokenid", "").trim();
		}
	}

	public String getSecurityQuestions(String emailid,int eventFlag){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_SECURITY_QUESTIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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

	public String getforgot_pwd(String emailid,String securityquestionid1,String securityquestionid2,String answer1,String answer2){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FORGOT_PASSWORD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String securityQuestions(){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SECURITY_QUESTIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"securityQuestions Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String signup(String profileimage,String firstname,String lastname,String emailid,String mobilenumber,String mobilecarrier,
			String pswd,String sq1id,String answer1,String sq2id,String answer2,String fbid,String fbaccesstoken, String addPin){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SIGNUP).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"securityQuestions Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String getSignUpStep1_Verify(String eventflag,String userid,String emailid,String verificationcode,String firstname,String lastname,String photo,String mobilenumber,String raisedBy,String locationId,String invoiceAmount,String couponcode,String storeNotes) {
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SIGNUP_STEPONE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();

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
			/*Log.i(TAG,"SignUpStep Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;

	}

	public String signup_Step2(String userId,String pswd,String sq1id,String answer1,String sq2id,String answer2, String addPin){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SIGNUP_STEP2).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"securityQuestions Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String activateUser(String userid,String activationcode){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ACTIVATIONUSER).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"ActivateUser Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String login(String username,String password){
		try{
			/*Log.i(TAG,"EmailId: "+username);
			Log.i(TAG,"Password: "+password);*/

			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(LOGIN).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			Log.i(TAG,"Login Return Value : "+mReturnValue);
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String Getfirst_data_global_payment(String userid,String username,String cardnumber,String cvv,
			String expirydatemonth,String expirydateyear,String streetaddress,String zipcode,String cardname/*,String pinnumber*/,String userType){
		/*Log.i("user id", userid + " " + expirydatemonth + " "+expirydateyear);*/
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FIRST_DATA_GLOBAL_PAYEMENT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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

			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			}else{
				mReturnValue="noresponse";
			}
			/*Log.i(TAG,"Getfirst_data_global_payment : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String Change_Password(String userid,String newpassword){
		try{
			/*Log.i(TAG,"UserId: "+userid);
			Log.i(TAG,"NewPassword: "+newpassword);*/
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHANGE_PASSWORD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"ChangePassword Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String categories(){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CATEGORIES).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"categories Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String subcategories(String id){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SUBCATEGORIES).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"SubCategories Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String search(String searchtype,String searchterm,String searchlistingflag){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SEARCH).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Search Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String store_locator(String latitude,String longitude,String storeid,String categoryid,String qrcode,double mapviewradius,String storelistingflag,String offset,String devicelat,String devicelong){
		try{
			/*Log.i(TAG,"User Id "+ UserDetails.mServiceUserId);
			Log.i(TAG,"Latitude: "+latitude);
			Log.i(TAG,"Longitude: "+longitude);
			Log.i(TAG,"Device Latitude: "+devicelat);
			Log.i(TAG,"Device Longitude: "+devicelong);
			Log.i(TAG,"Store Id "+ storeid);
			Log.i(TAG,"EventFlag "+ "single");
			Log.i(TAG,"CategoryId "+ categoryid);
			Log.i(TAG,"QRCode "+ qrcode);
			Log.i(TAG,"Distance: "+mapviewradius);
			Log.i(TAG,"StoreListingFlag "+ storelistingflag);
			Log.i(TAG,"Offset: "+"0");
			Log.i(TAG,"limit "+ "50");
			Log.i(TAG,"app_token "+ mTokenId);*/

			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_LOCATOR).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Store_Locator Return Value : "+mReturnValue);
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Function to get CheckFavorite
	public String checkFavorites(String storeid,String userid){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHECK_FAVORITE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"CheckFavorites Return Value : "+mReturnValue);*/
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
			/*Log.i(TAG,"Location Id : "+RightMenuStoreId_ClassVariables.mLocationId);*/
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_FAVORITE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"AddToFavorites Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Function to get CardOnFiles
	public String cardOnFiles(String userid,String usertype){
		/*Log.i("check", userid+" "+usertype);*/
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CARD_ON_FILE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("UserId", userid));
			/*Log.i(TAG,"UserType: "+UserDetails.mServiceUserId);
			Log.i(TAG,"UserType: "+AccountLoginFlag.accountUserTypeflag);*/
			list.add(new BasicNameValuePair("UserType", usertype));
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
			/*Log.i(TAG,"CardOnFiles Return Value : "+mReturnValue);*/
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
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(UPDATE_USER_PIN).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"UpdateUserPin Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Function to get RemoveCard
	public String RemoveCard(String cardid){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(REMOVE_CARD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"RemoveCard Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mStoreLocator(String currentLatitude,String currentLongtitude, String eventFlag, String storeID,String devicelat,String devicelong,String classname) {
		try{
			/*Log.i(TAG, "Values are :"+currentLatitude+","+currentLongtitude+","+eventFlag+","+storeID+","+UserDetails.mServiceUserId+","+devicelat+","+devicelong);*/

			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_LOCATOR).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			if(classname.equalsIgnoreCase("SlidingView")||classname.equalsIgnoreCase("zpay_step1")){
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
			Log.i(TAG, "URL :"+new StringBuilder().append(URL).append(STORE_LOCATOR).toString());
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
			//Log.i(TAG,"Stores Locator Return Value : "+mReturnValue);
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mStoreInformation(String storeID,String location_id) {
		// TODO Auto-generated method stub
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_INFO).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Stores_Info Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mStorePhotos(String storeID,String location_id) {

		try{
			/*Log.i(TAG,"StoreId: "+storeID);
			Log.i(TAG,"LocationId: "+RightMenuStoreId_ClassVariables.mLocationId);*/

			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_PHOTOS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Stores_Info Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}


	public String mStoreTimings(String storeID, String locationId) {
		// TODO Auto-generated method stub
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_TIMINGS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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

	public String AddStorePhoto(String storeId,String location_id){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_STORE_PHOTOS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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

	public String AddStorePhoto_Customer(String storeId,String location_id,File photofile){
		try{
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
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

	public String GetStoreCardDetails(String storeId,String cardType,String mStart){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_CARD_DETAILS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			if(cardType.equalsIgnoreCase("Zcard")){
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
			/*Log.i(TAG,"CardDetails "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String getSocialFriends(){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(MY_FRIENDS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"FB friendlist "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;	
	}

	public String checkStoreLikeStatus(String likeflag){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_LIKE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("user_id", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("store_id", RightMenuStoreId_ClassVariables.mStoreID));
			list.add(new BasicNameValuePair("location_id", RightMenuStoreId_ClassVariables.mLocationId));
			list.add(new BasicNameValuePair("event_flag", likeflag));
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
			//Log.i(TAG,"Social Like "+mReturnValue);
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
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_VIDEO).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Stores_Video Return Value : "+mReturnValue);*/
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
			String id = extractYoutubeId(urlYoutube);
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
	public String Share_Store(String sharetype, String sharemail){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SHARE_STORE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("StoreId", RightMenuStoreId_ClassVariables.mStoreID));
			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("ShareType", sharetype));
			list.add(new BasicNameValuePair("location_id", RightMenuStoreId_ClassVariables.mLocationId));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			if(sharetype.equalsIgnoreCase("Email")){
				list.add(new BasicNameValuePair("to_email", sharemail));
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
			/*Log.i(TAG,"Share Store "+mReturnValue);*/
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
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SHARE_COUPON).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Share Store "+mReturnValue);*/
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
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FB_LOGIN).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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


	public String test5(){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(TEST5).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Test5 Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mGetCoupons(String mCouponStart,String type,String location_id) {
		//Log.i(TAG,"webservice method");
		//Log.i(TAG,"Location Id : "+location_id);
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(STORE_COUPONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			//list.add(new BasicNameValuePair("StoreId", mStoreId));
			list.add(new BasicNameValuePair("offset", mCouponStart));
			if(!type.equalsIgnoreCase("")){
				list.add(new BasicNameValuePair("type", type));
			}else{
				list.add(new BasicNameValuePair("user_id", UserDetails.mServiceUserId));
			}
			list.add(new BasicNameValuePair("qr_code_width", "200"));
			list.add(new BasicNameValuePair("qr_code_height", "200"));
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
			/*Log.i(TAG,"Coupons Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mCheckIsFavoriteCoupon(String userid, String mCouponID) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHECK_FAVORITE_COUPON).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Coupons Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mAddFavoriteCoupon(String userid, String mCouponID) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_FAVORITE_COUPON).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Coupons Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String FavoriteCoupons(String offset) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FAVORITE_COUPONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("UserId", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("qr_code_width","300"));
			list.add(new BasicNameValuePair("qr_code_height","300"));
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
			/*Log.i(TAG,"FavoriteCoupons Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String GetFavouriteStores(String FavouriteType, String offset) {
		//Log.i("type and offset", FavouriteType+" "+offset);
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			if(FavouriteType.equalsIgnoreCase("FriendFavouriteStore")){
				if(MainMenuActivity.mFavouritesStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.mFavouriteFriendStoreDetails.clear();
				}
				Log.i("Starting OFFSET value","Starts from:"+MainMenuActivity.mFavouritesStart);
				mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FRIENDFAVOURITE_STORES).toString());	
			}else{
				if(MainMenuActivity.mFavouritesStart.equalsIgnoreCase("0")){
					WebServiceStaticArrays.mFavouriteStoreDetails.clear();
				}
				Log.i("Starting OFFSET value","Starts from fav stores:"+MainMenuActivity.mFavouritesStart);
				mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FAVOURITE_STORES).toString());
			}
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"favourite stores Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String GetCatogories() {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GETCATOGORIES).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Notificaitons Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String GetNotificationDetails() {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GETNOTIFICATIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Notification Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String SetNotificationDetails(String contactfrequency,String notifyby,String categories) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SETNOTIFICATIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Set Notification Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mGetUserProfile(String mUserId) {

		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_USER_PROFILE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Coupons Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String GetUserSecurityQuestions(String mUserId) {

		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_USERSECURITY_QUESTIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Security questions Return Value : "+mReturnValue);*/
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
		/*Log.i("user_id", mUserId);*/
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(UPDATE_USER_PROFILE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Update Profile Info Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String UpdateUserSecurityQuestions(String securityquestionid1,String securityquestionid2,String answer1,String answer2){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHANGE_PASSWORD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG," Return Value : "+mReturnValue);*/
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Method to Send Message to Talk To Us
	public String mTalkToUs(String mUserId, String mStoreId, String mQuery, String moduleflag, String usertype,String locationid) {
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ZOUPONS_CHAT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();

			/*Log.i("check", mUserId+" "+mStoreId+" "+usertype+" "+mQuery);*/
			if(moduleflag.equalsIgnoreCase("customer")){
				list.add(new BasicNameValuePair("from_user_id", mUserId));
				list.add(new BasicNameValuePair("from_store_id", ""));
				list.add(new BasicNameValuePair("to_user_id", ""));
				list.add(new BasicNameValuePair("to_store_id", mStoreId));
				list.add(new BasicNameValuePair("user_type", usertype));
				list.add(new BasicNameValuePair("module_flag", "zoupons_customer"));
				list.add(new BasicNameValuePair("query", mQuery));
			}else if(moduleflag.equalsIgnoreCase("store")){
				list.add(new BasicNameValuePair("from_user_id", mUserId));
				list.add(new BasicNameValuePair("from_store_id", locationid));
				list.add(new BasicNameValuePair("to_user_id", ""));
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
			/*Log.i(TAG,"Talk to Us Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	//Method to send Message to Contact Us in Store
	public String mContactToUs(String mUserId, String storeid, String Query,String locationid,String userType,String moduleflag,String storeemployeeid) {
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ZOUPONS_CHAT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();

			if(moduleflag.equalsIgnoreCase("customer")){

				/*Log.i(TAG,"From User Id : "+mUserId);
				Log.i(TAG,"From Store Id : "+"");
				Log.i(TAG,"To User Id : "+"");
				Log.i(TAG,"To Store Id : "+locationid);
				Log.i(TAG,"User Type : "+userType);
				Log.i(TAG,"Query : "+Query);*/

				list.add(new BasicNameValuePair("from_user_id", mUserId));	
				list.add(new BasicNameValuePair("from_store_id", ""));	
				list.add(new BasicNameValuePair("to_user_id", ""));
				list.add(new BasicNameValuePair("to_store_id", locationid));
				list.add(new BasicNameValuePair("user_type", userType));
				list.add(new BasicNameValuePair("query", Query));
			}else if(moduleflag.equalsIgnoreCase("store")){

				/*Log.i(TAG,"From User Id : "+storeemployeeid);
				Log.i(TAG,"From Store Id : "+locationid);
				Log.i(TAG,"To User Id : "+mUserId);
				Log.i(TAG,"To Store Id : "+"");
				Log.i(TAG,"User Type : "+userType);
				Log.i(TAG,"Query : "+Query);*/

				list.add(new BasicNameValuePair("from_user_id", storeemployeeid));	
				list.add(new BasicNameValuePair("from_store_id", locationid));	
				list.add(new BasicNameValuePair("to_user_id", mUserId));
				list.add(new BasicNameValuePair("to_store_id", ""));
				list.add(new BasicNameValuePair("user_type", userType));
				list.add(new BasicNameValuePair("query", Query));
			}else{
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
			/*Log.i(TAG,"Conatct to Store Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// Get ContactUs and TalkToUs Message Response From Service
	public String mContactToUsResponse(String flagactivity,String mUserId, String storeid,String location_id,String user_type,String moduleflag,String flag,String notification_id) {
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ZOUPONS_CHAT_LIST).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
           // Log.i("check for flag and id", flag +" " + notification_id);
			/*Log.i(TAG,"Activity Flag : "+flagactivity);
			Log.i(TAG,"User Id : "+mUserId);
			Log.i(TAG,"Store Id : "+storeid);
			Log.i(TAG,"Location Id : "+location_id);
			Log.i(TAG,"User Type : "+user_type);
			Log.i(TAG,"Module Flag : "+moduleflag);*/

			if(flagactivity.equalsIgnoreCase("TalkToUs")){
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
			/*Log.i(TAG,"Conatct to Store Message Response Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// function to get advertisement from zoupons..
	public String GetAdvertisement(String deviceId,String slot_type,String user_id) {
		/*Log.i("device id", deviceId);
		Log.i("user id", user_id);
		Log.i("ad slot", slot_type);*/
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_REWARDS_ADVERTISENT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Get rewards add Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mCheckStoreName(String mStoreName) {
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHECK_STORE_NAME).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Check Store name Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}
	//Get Terms and conditions
	public String mGetTermsConditions() {
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_TERMS_CONDITIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Terms & Condition Return Value : "+mReturnValue);*/
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
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_REWARDS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Check Store name Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String inviteFriend(String friendname) {
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(INVITE_FRIENDS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			/*Log.i(TAG,"Invite Friends Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mGetAllGiftCards(String userId, String mStartLimit) {
		//Log.i(TAG, "UserId :"+userId+" Start Limit :"+mStartLimit);

		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_ALL_GIFT_CARDS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("UserId",userId));	
			list.add(new BasicNameValuePair("offset",mStartLimit));	
			list.add(new BasicNameValuePair("limit", mLimit));
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
			/*Log.i(TAG,"Get All Gift Cards Response Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mRedeemGiftCard(String mUserId, String verificationcode) {
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(REEDEM_GIFT_CARD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			//Log.i(TAG,"UserId: "+mUserId);
			//Log.i(TAG,"Verification : "+verificationcode);
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
			/*Log.i(TAG,"Redeem Gift Card Response Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mSendGiftCard(String mServiceUserId, String mSend_To,
			String mSend_date, String mGiftCardId, String mMessage, String mEventFlag, String TimeZone) {
		try {
			/*Log.i(TAG,"SendTo : "+mSend_To);
			Log.i(TAG,"SendTimeZone : "+TimeZone);
			Log.i(TAG,"SendDate : "+mSend_date);*/
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SEND_GIFT_CARD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("user_id", mServiceUserId));	
			list.add(new BasicNameValuePair("send_to", mSend_To));
			list.add(new BasicNameValuePair("send_date", convertDeviceTimeToServerTime(mSend_date, TimeZone)));
			list.add(new BasicNameValuePair("gift_card_purchase_id", MenuUtilityClass.mGiftCardPurchaseId));
			list.add(new BasicNameValuePair("giftcard_id", mGiftCardId));
			list.add(new BasicNameValuePair("balance_amount", String.valueOf(MenuUtilityClass.mGiftCardBalanceAmount)));
			list.add(new BasicNameValuePair("Message", mMessage));
			list.add(new BasicNameValuePair("card_type", MenuUtilityClass.mGiftCard_Type));
			list.add(new BasicNameValuePair("store_id", MenuUtilityClass.mGiftCard_StoreId));
			list.add(new BasicNameValuePair("location_id", MenuUtilityClass.mGiftCardStoreLocationId));
			list.add(new BasicNameValuePair("EventFlag", mEventFlag));
			list.add(new BasicNameValuePair("app_token", mTokenId));

			/*Log.i(TAG,"UserId : "+mServiceUserId);
			Log.i(TAG,"SendTo : "+mSend_To);
			Log.i(TAG,"SendDate : "+mSend_date);
			Log.i(TAG,"Server Date : "+convertDeviceTimeToServerTime(mSend_date, TimeZone));
			Log.i(TAG,"GiftCardPurchaseId : "+MenuUtilityClass.mGiftCardPurchaseId);
			Log.i(TAG,"GiftCardId : "+mGiftCardId);
			Log.i(TAG,"GiftCardBalanceAmount : "+MenuUtilityClass.mGiftCardBalanceAmount);
			Log.i(TAG,"Message : "+mMessage);
			Log.i(TAG,"GiftCardType : "+MenuUtilityClass.mGiftCard_Type);
			Log.i(TAG,"GiftCardStoreId : "+MenuUtilityClass.mGiftCard_StoreId);
			Log.i(TAG,"GiftCardStoreLocationId : "+MenuUtilityClass.mGiftCardStoreLocationId);
			Log.i(TAG,"EventFlag : "+mEventFlag);*/

			mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			mResponse=mHttpClient.execute(mHttpPost);
			mEntity=mResponse.getEntity();
			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				mReturnValue=content;
			} else {
				mReturnValue="noresponse";
			}
			/*Log.i(TAG,"Redeem Gift Card Response Return Value : "+mReturnValue);*/
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}


	public String getReviewDetails(String offset,String user_id,String store_id,String locationid) {
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_REVIEWDETAILS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			//StoreId, offset, limit.
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
			//Log.i(TAG,"Review Details Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mPostStoreReviews(String userId, String storeId, String ratings,
			String reviews) {
		try {
			/*Log.i(TAG,"Userid : "+userId);
			Log.i(TAG,"storeId : "+storeId);
			Log.i(TAG,"ratings : "+ratings);
			Log.i(TAG,"reviews : "+reviews);*/
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(POST_STORE_REVIEW).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Post Store Review Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mUpdateStoreReviews(String reviewid, String rating,
			String review) {
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(UPDATE_STORE_REVIEW).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Update Store Review Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mGetReviewStatus(String userId, String storeId) {
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_STATUS_REVIEW).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Get review Review Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mReviewInApproriate(String userId, String reviewid) {
		//Log.i(TAG, "User ID:"+userId+" ReviewId:"+reviewid);
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(IN_APPROPRIATE_REVIEW).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"In Approriate Review Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String ReviewLikeDisLike(String userId, String storeid,
			String reviewid, String rate) {
		//Log.i(TAG,"Review Id:"+reviewid+" rate:"+rate);
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(LIKE_DIALIKE_REVIEW).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Like Dislike Review Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mGetGiftCardTransactionHistory(String mServiceUserId,
			String giftcardid) {
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GIFT_CARD_TRANSACTION_HISTORY).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Gift Card Transaction History Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String GetReceiptsDetails(String mReceiptStart,String user_id,String location_id){
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(RECEIPTS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"receipts list "+mReturnValue);
		}catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;	
	}

	public String mGetInvoiceListStatus(String mUserID) {

		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_INVOICE_LIST).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Get Invoice List Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String mGetRejectInvoiceStatus(String mInvoiceId) {
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(REJECT_INVOICE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Reject Invoice Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String checkUserPIN(String enterdPIN,String userid,String user_type) {

		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHECK_USERPIN).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"check pin Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String myGiftCards(String StoreId,String user_id,String user_type) {
		//Log.i("check", user_id+" "+user_type+" "+StoreId);
		try {			
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(MY_GIFT_CARDS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"My_Gift_Cards Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// for giftcard purchase

	public String purchaseGiftcard(String user_id,String user_type,String mCreditcardId, String mGiftcardId,
			String mCardvalue, String mFacebookId, String mFriendNotes,
			String mDate, String mTimeZone, String mIsZouponsfriend,String mInvoiceNote,String mStoreId,String mCardType,String storelocationid,String mFacevalue) {
		/*Log.i("check", " "+mFacebookId+" "+mFacevalue);
		Log.i(TAG,"Date : "+ mDate);
		Log.i("TAG","TimeZone : " +mTimeZone);*/
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FIRST_DATA_GLOBAL_PAYEMENT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("UserId", user_id));
			list.add(new BasicNameValuePair("UserType", user_type));
			list.add(new BasicNameValuePair("Type", "Giftcard"));
			list.add(new BasicNameValuePair("TransactionType", "02"));
			list.add(new BasicNameValuePair("StoreId", mStoreId));
			list.add(new BasicNameValuePair("location_id", storelocationid));
			list.add(new BasicNameValuePair("CardId", mCreditcardId));
			list.add(new BasicNameValuePair("GiftcardId", mGiftcardId));
			if(mCardType.equalsIgnoreCase("Regular")){
				list.add(new BasicNameValuePair("FaceValue", mCardvalue));
				list.add(new BasicNameValuePair("DollarAmount", mCardvalue));
			}else{
				list.add(new BasicNameValuePair("FaceValue", mFacevalue));
				list.add(new BasicNameValuePair("DollarAmount", mCardvalue));
			}
			list.add(new BasicNameValuePair("CardType", mCardType));
			if(!mFacebookId.equalsIgnoreCase("")){
				list.add(new BasicNameValuePair("send_to", mFacebookId));
				list.add(new BasicNameValuePair("Message", mFriendNotes));
				String serverDate = convertDeviceTimeToServerTime(mDate, mTimeZone);
				list.add(new BasicNameValuePair("SendDate", serverDate));
				Log.i(TAG,"Server Date : "+serverDate);
				list.add(new BasicNameValuePair("ZPFriendFlag", mIsZouponsfriend));
				list.add(new BasicNameValuePair("PurchaseFlag", "Friend"));
			}else{
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
			//Log.i(TAG,"My_Gift_Cards Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// payment from invoice by using only giftcard	
	public String payInvoiceUsingGiftcard(String mInvoiceId, String mTipAmount,String mTotalAmount,String mChoosedGiftCardPurchaseId,
			String mInvoiceRaisedBy, String mInvoiceRaisedByType, String mStoreID,String storelocationid,String paymentNote) {
		/*Log.i("check", mInvoiceId+" "+mTipAmount+" "+mTotalAmount);
		Log.i("location id", storelocationid);*/
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			//mHttpPost = new HttpPost(new StringBuilder().append(URL).append(INVOICE_PAY_USING_GIFTCARD).toString());
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FIRST_DATA_GLOBAL_PAYEMENT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("user_id", UserDetails.mServiceUserId));
			list.add(new BasicNameValuePair("invoice_id", mInvoiceId));
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
			//Log.i(TAG,"Giftcard purchase from invoice Response Return Value : "+mReturnValue);
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
		/*Log.i("check", mInvoiceId+" "+mInvoiceRaisedBy+" "+mInvoiceRaisedByType+" "+mStoreID+" "+mCreditcardId+" ");
		Log.i("location id", storelocationid);*/
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FIRST_DATA_GLOBAL_PAYEMENT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Giftcard purchase from invoice Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// payment from invoice by using both cards	
	public String payInvoiceUsingBothCard(String mInvoiceId,String mInvoiceRaisedBy, String mInvoiceRaisedByType,String mCreditcardId, String mAmountOnCreditCard, String mStoreID,
			String mAmountOnGiftCard,String mTipAmount,String mChoosedGiftCardPurchaseId,String mTotalAmount,String storelocationid,String paymentNote) {
		/*Log.i("check", mInvoiceId+" "+mInvoiceRaisedBy+" "+mInvoiceRaisedByType+" "+mStoreID+" "+mCreditcardId+" ");
		Log.i("location id", storelocationid);*/
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(FIRST_DATA_GLOBAL_PAYEMENT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Giftcard purchase from invoice Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;

	}

	public String NormalPaymnetUsingGiftcard(String pageFlag,String user_id,String mChoosedGiftcardId, String mTipAmount,String mTotalAmount,String mChoosedGiftCardPurchaseId, String mNote, String mActualAmount,int mQRwidth,int mQRheight) {
		/*Log.i("check", mChoosedGiftcardId+" "+mTipAmount+" "+mTotalAmount);
		Log.i("NormalPayment","QRwidth : "+mQRwidth);
		Log.i("NormalPayment","QRheight : "+mQRheight);*/
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(NORMAL_PAYMENT_URL).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			if(pageFlag.equalsIgnoreCase("from_customer")){
				list.add(new BasicNameValuePair("user_id", user_id));
			}else{
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
			//Log.i(TAG," Normal pay Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	} 	

	public String NormalPaymnetUsingCreditCard(String pageFlag,String user_id,String mCreditcardId, String mStoreID, String mTipAmount, String mTotalAmount, String mNote, String mActualAmount,String storelocationid,int mQRwidth,int mQRheight) {
		/*Log.i("check", mCreditcardId+" "+mStoreID+" "+mTipAmount+" "+mTotalAmount);
		Log.i("location id", storelocationid);
		Log.i("NormalPayment","QRwidth : "+mQRwidth);
		Log.i("NormalPayment","QRheight : "+mQRheight);*/
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(NORMAL_PAYMENT_URL).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			if(pageFlag.equalsIgnoreCase("from_customer")){
				list.add(new BasicNameValuePair("user_id", user_id));
			}else{
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
			//Log.i(TAG,"Giftcard purchase from invoice Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// payment from invoice by using both cards	
	public String NormalPaymnetUsingBothCard(String pageFlag,String user_id,String mCreditcardId, String mAmountOnCreditCard, String mStoreID,
			String mChoosedGiftcardId, String mAmountOnGiftCard,String mTipAmount,String mTotalAmount,String mChoosedGiftCardPurchaseId, String mNote, String mActualAmount,String storelocationid,int mQRwidth,int mQRheight) {
		/*Log.i("check", mStoreID+" "+mCreditcardId+" "+" "+mAmountOnCreditCard+" "+mChoosedGiftcardId+" "+mAmountOnGiftCard+" "+mTipAmount+" "+mTotalAmount);
		Log.i("location id", storelocationid);
		Log.i("NormalPayment","QRwidth : "+mQRwidth);
		Log.i("NormalPayment","QRheight : "+mQRheight);*/
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(NORMAL_PAYMENT_URL).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			if(pageFlag.equalsIgnoreCase("from_customer")){
				list.add(new BasicNameValuePair("user_id", user_id));
			}else{
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
			//Log.i(TAG,"Response Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;

	}

	//To call Notification Service
	public String getNotification() {
		//Log.i("notify to user id", UserDetails.mServiceUserId);
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GETNOTIFICATION).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Notification Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;

	}

	//To call Notification Service
	public String getCustomerCommunicatedStores(String offset) {
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CUSTOMER_COMMUNICATED_STORES).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			Log.i(TAG,"UserId : "+UserDetails.mServiceUserId);
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
			//Log.i(TAG,"CustomerService Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;

	}	

	// Function to call webservice to end the login session
	public String endLoginSession(String mServiceUserId,String deviceId) {
		//Log.i("device_id", deviceId);
		String mReturnValue;
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(LOGOUT).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Logout WS Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;

	}

	// To get Access token from google account
	public String getAccessToken(String code){
		String returnValue="";
		try {
			HttpClient mHttpClient = new DefaultHttpClient();
			HttpPost mHttpPost = new HttpPost(OAuth2clientCredentials.GET_ACCESSTOKEN);
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("code", code));
			list.add(new BasicNameValuePair("client_id", OAuth2clientCredentials.CLIENT_ID));
			list.add(new BasicNameValuePair("client_secret", OAuth2clientCredentials.CLIENT_SECRET));
			list.add(new BasicNameValuePair("redirect_uri", OAuth2clientCredentials.REDIRECT_URI));
			list.add(new BasicNameValuePair("grant_type", "authorization_code"));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			mHttpPost.setEntity(new UrlEncodedFormEntity(list));

			HttpResponse mResponse=mHttpClient.execute(mHttpPost);
			HttpEntity mEntity=mResponse.getEntity();

			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				returnValue=content;
			}else{
				returnValue="noresponse";
			}
			//Log.i("Return Value : ",returnValue);
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = "failure";
		} 

		return returnValue;
	}


	// To get User details from access token generated from google account
	public String getUserDetails(String accesstoken){
		String returnValue="";
		try {
			HttpClient mHttpClient = new DefaultHttpClient();
			HttpGet mHttpGet = new HttpGet(OAuth2clientCredentials.GET_USERINFO+accesstoken);
			HttpResponse mResponse=mHttpClient.execute(mHttpGet);
			HttpEntity mEntity=mResponse.getEntity();

			if(mEntity != null){
				String content = EntityUtils.toString(mEntity);
				returnValue=content;
			}else{
				returnValue="noresponse";
			}
			//Log.i("User Info Return Value : ",returnValue);
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = "failure";
		} 
		return returnValue;
	}


	// To import friends from social services
	public String importfriends(String provider,String providerId,String accessToken){
		String mReturnValue="";
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SOCIAL_LOGIN).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SIGNUP_STEPONE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();

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
			//Log.i(TAG,"SignUpStep Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;

	}

	public String getSignUpStep1_Verify(String eventflag,String userid,String emailid,String verificationcode,String firstname,String lastname,String photo,String mobilenumber) {
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SIGNUP_STEPONE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();

			/*Log.i(TAG,"EventFlag : "+eventflag);
			Log.i(TAG,"UserId : "+userid);
			Log.i(TAG,"EmailAddress : "+emailid);
			Log.i(TAG,"VerificationCode : "+verificationcode);
			Log.i(TAG,"FirstName : "+firstname);
			Log.i(TAG,"LastName : "+lastname);
			Log.i(TAG,"Photo : "+photo);
			Log.i(TAG,"MobileNumber : "+mobilenumber);*/

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
			//Log.i(TAG,"SignUpStep Return Value : "+mReturnValue);
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
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(UPDATE_NOTIFICATION_STATUS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
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
			//Log.i(TAG,"Unread To Read Return Value : "+mReturnValue);
		} catch (Exception e) {
			mReturnValue = "failure";
			Log.i(TAG,"Throwable Message :"+mReturnValue);
			e.printStackTrace();
		}
		return mReturnValue;
	}

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

	public int numResults(Document doc){
		Node results = doc.getDocumentElement();
		//Log.i(TAG,"Node Result : "+results);
		int res = -1;
		try{
			res = Integer.valueOf(results.getChildNodes().getLength());
			//Log.i(TAG,"Result Length : "+res);
		}catch(Exception e){
			res = -1;
		}
		return res;
	}

	public static String getValue(Element item,String str){
		String tagvalue="";
		NodeList nodelist = item.getElementsByTagName(str);
		return ZouponsWebService.getElementValue(nodelist.item(0));
	}

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

	private String convertDeviceTimeToServerTime(String senddate,String servertimezone){
		try {
			/*Log.i(TAG,"Function date : "+senddate);
			Log.i(TAG,"Function timezone : "+servertimezone);*/

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
