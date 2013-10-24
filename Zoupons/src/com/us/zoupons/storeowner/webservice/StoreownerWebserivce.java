package com.us.zoupons.storeowner.webservice;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.us.zoupons.EncryptionClass;
import com.us.zoupons.ZouponsLogin;
import com.us.zoupons.WebService.MyHttpClient;

public class StoreownerWebserivce {

	private Context mContext;
	private boolean mIsSSLCertificateCompatible = false;
	private String mReturnValue;
	private HttpClient mHttpClient;
	private HttpPost mHttpPost;
	private HttpResponse mResponse;
	private HttpEntity mEntity;

	private String TAG = getClass().getSimpleName();
	public static String URL = "https://www.zoupons.com/dev1/webservices/";
	//public static String URL = "https://www.zoupons.com/webservices/";
	private String GET_USER_PROFILE = "userprofile";
	private String ADD_COUPON = "create_coupon";
	private String GET_STORE_LOCATIONS = "stores_location";
	private String PROCESS_BY_QR = "approve_customer_initiated_payment";
	private String GET_STORE_GENERALINFO = "store_general_info";
	private String GET_STORE_EMPLOYEES = "store_employees";
	private String ADD_EMPLOYEE = "add_employee";
	private String GET_SET_EMPLOYEEPERMISSIONS = "store_permissions";
	private String INACTIVATE_EMPLOYEE = "inactivate_user";
	private String Get_ALL_STORE_LOCATIONS = "store_locations";
	private String CHANGE_STORELOCATION_STATUSREQUEST = "request_store_location_status_update";
	private String RAISE_INVOICE = "raise_invoice";
	private String INVOICE_LIST = "invoices";
	private String UPDATE_STORE_DETAILS = "saveStoreDetails";
	private String UPDATE_BUSSINESS_TIMINGS = "saveStoreBusinessHours";
	private String GET_FAVOURITE_USERS = "store_favorite_users";
	private String ADD_FAVOURITE_USERS = "add_user";
	private String SEND_EMAIL = "share_store";	
	private String GET_ALL_STOREVIDEOS = "store_video";
	public static String UPLOAD_VIDEO = "add_store_video";
	private String ACTIVATE_CARD = "activate_card_on_file";
	private String GET_ZOUPONS_DEALS = "store_deals_manager";
	private String mTokenId=""; 
	public StoreownerWebserivce(Context context){
		mContext = context;
		// to get os ssl compatabile from shared preference
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

	// To get user profile
	public String mGetUserProfile(String mobileNumber,String pageFlag) {

		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			if(mobileNumber.contains("-")){
				mobileNumber = mobileNumber.replaceAll("-", "");
			}
			if(!pageFlag.equalsIgnoreCase("Employee")){
				mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_USER_PROFILE).toString());
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.clear();
				list.add(new BasicNameValuePair("MobileNo", mobileNumber));	
				list.add(new BasicNameValuePair("event_flag", "store_owner"));	
				list.add(new BasicNameValuePair("app_token", mTokenId));
				mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			}else{
				mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_EMPLOYEE).toString());
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.clear();
				list.add(new BasicNameValuePair("event_flag", "check"));
				list.add(new BasicNameValuePair("telephone_number", mobileNumber));
				list.add(new BasicNameValuePair("app_token", mTokenId));
				mHttpPost.setEntity(new UrlEncodedFormEntity(list));
			}
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

	// To add coupon profile
	public String addCoupon(String eventFlag,String coupontype,String location_id,String user_id,String coupon_title,String couponcode,String activation_date,String expiration_date,String one_time_use,String description,String photo,String firstname,String lastname,String email,String phone,String couponId) {

		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_COUPON).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();

			list.add(new BasicNameValuePair("event_flag", eventFlag));  
			if(eventFlag.equalsIgnoreCase("update_coupon")){
				list.add(new BasicNameValuePair("coupon_id",couponId));
			}
			list.add(new BasicNameValuePair("type", coupontype));
			list.add(new BasicNameValuePair("location_id", location_id));
			list.add(new BasicNameValuePair("user_id", user_id));
			list.add(new BasicNameValuePair("title", coupon_title));
			list.add(new BasicNameValuePair("coupon_code", couponcode));
			list.add(new BasicNameValuePair("activation_date", activation_date));
			list.add(new BasicNameValuePair("expiration_date", expiration_date));
			list.add(new BasicNameValuePair("one_time_use", one_time_use));
			list.add(new BasicNameValuePair("description", description));
			list.add(new BasicNameValuePair("app_token", mTokenId));
			if(!firstname.equalsIgnoreCase("")){
				list.add(new BasicNameValuePair("first_name", firstname));
				list.add(new BasicNameValuePair("last_name", lastname));
				list.add(new BasicNameValuePair("email_address", email));
				list.add(new BasicNameValuePair("phone_number", phone));
				list.add(new BasicNameValuePair("photo", photo));
			}else{
				
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
		return mReturnValue;
	}

	public String generateBarcode(String eventFlag, String locationId, String couponcode) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_COUPON).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("event_flag", eventFlag));
			list.add(new BasicNameValuePair("location_id", locationId));
			list.add(new BasicNameValuePair("coupon_code", couponcode));
			list.add(new BasicNameValuePair("qr_code_width", "250"));
			list.add(new BasicNameValuePair("qr_code_height", "250"));
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

	public String getStoreLocations(String storeId,String user_id) {

		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			if(user_id.equalsIgnoreCase("")){ // For store Owner
				mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_STORE_LOCATIONS).toString());	
			}else{// For store Employee
				mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_STORE_LOCATIONS).toString());
			}
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			if(user_id.equalsIgnoreCase("")){ // For store Owner
				list.add(new BasicNameValuePair("StoreId", storeId));
			}else{// For store Employee
				list.add(new BasicNameValuePair("StoreId", storeId));
				list.add(new BasicNameValuePair("user_id", user_id));
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
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// To get user profile
	public String approvebyQR(String store_locationId,String employeeId,String amount,String couponcode,String eventFlag,String qrcode,String store_note,String raised_by_type) {
		
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext); 
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(PROCESS_BY_QR).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("location_id", store_locationId));
			list.add(new BasicNameValuePair("approved_by", employeeId));
			list.add(new BasicNameValuePair("amount", amount));
			list.add(new BasicNameValuePair("coupon_code", couponcode));
			list.add(new BasicNameValuePair("event_flag", eventFlag));
			list.add(new BasicNameValuePair("qr_code", qrcode));
			list.add(new BasicNameValuePair("store_note", store_note));
			list.add(new BasicNameValuePair("raised_by_type", raised_by_type));
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

	// To get user profile
	public String approvebyMobileNumber(String store_locationId,String employeeId,String amount,String couponcode,String eventFlag,String mobile_number,String raised_by_type, String customeruser_id, String user_status, String firstname, String lastname, String email, String photo) {
		
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext); 
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(PROCESS_BY_QR).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("location_id", store_locationId));
			list.add(new BasicNameValuePair("approved_by", employeeId));
			list.add(new BasicNameValuePair("amount", amount));
			list.add(new BasicNameValuePair("coupon_code", couponcode));
			list.add(new BasicNameValuePair("event_flag", eventFlag));
			list.add(new BasicNameValuePair("mobile_no", mobile_number));
			list.add(new BasicNameValuePair("raised_by_type", raised_by_type));
			list.add(new BasicNameValuePair("profile_status", user_status));
			list.add(new BasicNameValuePair("user_id", customeruser_id));
			list.add(new BasicNameValuePair("non_member_email", email));
			list.add(new BasicNameValuePair("first_name", firstname));
			list.add(new BasicNameValuePair("last_name",lastname));
			list.add(new BasicNameValuePair("photo", photo));
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

	// To get user profile
	public String processByMobileNumber(String store_locationId,String employeeId,String amount,String couponcode,String eventFlag,String invoice_id,String store_note,String raised_by_type,String raised_to) {
		
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext); 
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(PROCESS_BY_QR).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("location_id", store_locationId));
			list.add(new BasicNameValuePair("approved_by", employeeId));
			list.add(new BasicNameValuePair("amount", amount));
			list.add(new BasicNameValuePair("coupon_code", couponcode));
			list.add(new BasicNameValuePair("event_flag", eventFlag));
			list.add(new BasicNameValuePair("invoice_id_member", invoice_id));
			list.add(new BasicNameValuePair("store_note", store_note));
			list.add(new BasicNameValuePair("raised_by_type", raised_by_type));
			list.add(new BasicNameValuePair("raised_to", raised_to));
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

	// To get store information
	public String get_setStoreInfo(String store_id,String event_flag, String logoData, String webAddress) {

		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_STORE_GENERALINFO).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("store_id", store_id));	
			list.add(new BasicNameValuePair("event_flag", event_flag));	
			if(event_flag.equalsIgnoreCase("set")){
				list.add(new BasicNameValuePair("logo", logoData));	
				list.add(new BasicNameValuePair("website", webAddress));	
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
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// To get Employee list
	public String getStoreEmployees(String storeId,String offset) {

		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_STORE_EMPLOYEES).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("store_id", storeId));
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
			e.printStackTrace();
		}
		return mReturnValue;
	}

	// For verifying employee code
	public String verifyEmployeeCode(String employment_code, String Employee_id, String StoreId) {
		try {

			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_EMPLOYEE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("employment_code", employment_code));
			list.add(new BasicNameValuePair("employee_id", Employee_id));
			list.add(new BasicNameValuePair("event_flag", "verify"));
			list.add(new BasicNameValuePair("store_id", StoreId));
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

	// For activating user
	public String activateEmployee(String user_id, String Employee_id, String StoreId,String choosedlocations,String choosedModules) {
		try {

			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_EMPLOYEE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("user_id", user_id)); 
			list.add(new BasicNameValuePair("employee_id", Employee_id));
			list.add(new BasicNameValuePair("event_flag", "activate"));
			list.add(new BasicNameValuePair("store_id", StoreId));
			list.add(new BasicNameValuePair("permission_ids", choosedModules));
			list.add(new BasicNameValuePair("location_ids", choosedlocations));
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

	// To get Employee Permissions
	public String get_setEmployeesPermission(String storeId,String userId,String employeeId,String eventFlag,String permissionIds,String locationIds) {

		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_SET_EMPLOYEEPERMISSIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("store_id", storeId));
			list.add(new BasicNameValuePair("event_flag", eventFlag));
			list.add(new BasicNameValuePair("employee_id", employeeId));
			list.add(new BasicNameValuePair("user_id", userId));
			if(eventFlag.equalsIgnoreCase("set")){
				list.add(new BasicNameValuePair("permission_ids", permissionIds));
				list.add(new BasicNameValuePair("location_ids", locationIds));
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
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String inactivateEmployee(String mEmployeeId) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(INACTIVATE_EMPLOYEE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", mEmployeeId));
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

	public String getAllStoreLocations(String store_id) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(Get_ALL_STORE_LOCATIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("event_flag","get"));
			list.add(new BasicNameValuePair("store_id",store_id));
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

	public String validateLocationAddress(String address1, String address2,String city, String state, String zipcode) {
		try {
			mHttpClient = new DefaultHttpClient();
			String url = "http://maps.googleapis.com/maps/api/geocode/xml?address="+address1+", "+city+", "+state+" "+zipcode+"&sensor=false";
			url = url.replace(" ", "+");
			HttpGet  httpget= new HttpGet(url);
			mResponse=mHttpClient.execute(httpget);
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

	public String addStoreLocations(String store_id, String address1, String address2, String city, String state, String zipcode, String mobile_number,String lati,String longi) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(Get_ALL_STORE_LOCATIONS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("event_flag","set"));
			list.add(new BasicNameValuePair("store_id",store_id));
			list.add(new BasicNameValuePair("address1",address1));
			list.add(new BasicNameValuePair("address2",address2));
			list.add(new BasicNameValuePair("state",state));
			list.add(new BasicNameValuePair("city",city));
			list.add(new BasicNameValuePair("zip_code",zipcode));
			list.add(new BasicNameValuePair("phone",mobile_number));
			list.add(new BasicNameValuePair("lat",lati));
			list.add(new BasicNameValuePair("lon",longi));
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

	public String ChangeStoreLocationsStatus(String mSelectedLocationid,String mStatus) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(CHANGE_STORELOCATION_STATUSREQUEST).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("location_id", mSelectedLocationid));
			list.add(new BasicNameValuePair("status", mStatus));
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

	public String raise_Invoice(String user_id,String PIN,String user_type,String Customer_userId,String location_id,String amount,String stores_notes, String couponcode) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(RAISE_INVOICE).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("raised_by", user_id));
			list.add(new BasicNameValuePair("PIN", new EncryptionClass().md5(PIN)));
			list.add(new BasicNameValuePair("raised_by_type", user_type));
			list.add(new BasicNameValuePair("raised_to", Customer_userId));
			list.add(new BasicNameValuePair("location_id", location_id));
			list.add(new BasicNameValuePair("amount", amount));
			list.add(new BasicNameValuePair("note", stores_notes));
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
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String getAllInvoices(String location_id,String eventFlag, String SelectedFromDate, String SelectedToDate) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(INVOICE_LIST).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("event_flag",eventFlag));
			list.add(new BasicNameValuePair("location_id",location_id));
			list.add(new BasicNameValuePair("start_date",SelectedFromDate));
			list.add(new BasicNameValuePair("end_date",SelectedToDate));
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

	public String updateStoreInfo(String eventFlag,String storeId,String mStoreLocation_id, String mWebAddress, String mLogoData,String mAboutStore) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(UPDATE_STORE_DETAILS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("store_id",storeId));
			list.add(new BasicNameValuePair("location_id",mStoreLocation_id));
			list.add(new BasicNameValuePair("detailstosave",eventFlag));
			list.add(new BasicNameValuePair("logo",mLogoData));
			list.add(new BasicNameValuePair("website",mWebAddress));
			if(eventFlag.equalsIgnoreCase("aboutus")){
				list.add(new BasicNameValuePair("about_store",mAboutStore));	
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
		} catch (Exception e) {
			mReturnValue = "failure";
			e.printStackTrace();
		}
		return mReturnValue;
	}

	public String update_StoreTimings(String monday_startTime, String monday_endTime,String tuesday_startTime, String tuesday_endTime, String wednesday_startTime, String wednesday_endTime,
			String thursday_startTime, String thursday_endTime, String friday_startTime,String friday_endTime, String saturday_startTime, String saturday_endTime,
			String sunday_startTime, String sunday_endTime, String isMondayClosed,String isTuesdayClosed, String isWednesdayClosed, String isThursdayClosed,
			String isFridayClosed, String isSaturdayClosed, String isSundayClosed,String location_id) {
		try{
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(UPDATE_BUSSINESS_TIMINGS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("location_id",location_id));
			list.add(new BasicNameValuePair("monday_from",monday_startTime));
			list.add(new BasicNameValuePair("monday_to",monday_endTime));
			list.add(new BasicNameValuePair("tuesday_from",tuesday_startTime));
			list.add(new BasicNameValuePair("tuesday_to",tuesday_endTime));
			list.add(new BasicNameValuePair("wednesday_from",wednesday_startTime));
			list.add(new BasicNameValuePair("wednesday_to",wednesday_endTime));
			list.add(new BasicNameValuePair("thursday_from",thursday_startTime));
			list.add(new BasicNameValuePair("thursday_to",thursday_endTime));
			list.add(new BasicNameValuePair("friday_from",friday_startTime));
			list.add(new BasicNameValuePair("friday_to",friday_endTime));
			list.add(new BasicNameValuePair("saturday_from",saturday_startTime));
			list.add(new BasicNameValuePair("saturday_to",saturday_endTime));
			list.add(new BasicNameValuePair("sunday_from",sunday_startTime));
			list.add(new BasicNameValuePair("sunday_to",sunday_endTime));
			list.add(new BasicNameValuePair("is_monday_closed",isMondayClosed));
			list.add(new BasicNameValuePair("is_tuesday_closed",isTuesdayClosed));
			list.add(new BasicNameValuePair("is_wednesday_closed",isWednesdayClosed));
			list.add(new BasicNameValuePair("is_thursday_closed",isThursdayClosed));
			list.add(new BasicNameValuePair("is_friday_closed",isFridayClosed));
			list.add(new BasicNameValuePair("is_saturday_closed",isSaturdayClosed));
			list.add(new BasicNameValuePair("is_sunday_closed",isSundayClosed));
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

	public String getRecentCommunicatedUsers(String mLocationId) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_FAVOURITE_USERS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("location_id", mLocationId));
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

	public String addStoreCustomer(String mUsedId, String mLocationId,String first_name, String last_name, String email) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ADD_FAVOURITE_USERS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id",mUsedId));
			list.add(new BasicNameValuePair("store_id", mLocationId));
			list.add(new BasicNameValuePair("first_name", first_name));
			list.add(new BasicNameValuePair("last_name", last_name));
			list.add(new BasicNameValuePair("email", email));
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

	public String sendEmail(String mUserID,String mLocationId,String subject,String message) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(SEND_EMAIL).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("UserId",mUserID));
			list.add(new BasicNameValuePair("location_id", mLocationId));
			list.add(new BasicNameValuePair("ShareType", "Sendmail"));
			list.add(new BasicNameValuePair("message", message));
			list.add(new BasicNameValuePair("subject", subject));
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

	public String getAllStoreVideos(String location_id) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_ALL_STOREVIDEOS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("location_id", location_id));
			list.add(new BasicNameValuePair("module_flag", "store"));
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

	// To activate credit card for billing
	public String activateCardForBilling(String user_id,String card_id) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(ACTIVATE_CARD).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("user_id", user_id));
			list.add(new BasicNameValuePair("card_on_file_id", card_id));
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

	// To get Employee list
	public String getZouponsDeals(String storeId,String user_id,String offset) {
		try {
			//check for ssl certificate compatibility 
			if(mIsSSLCertificateCompatible){
				mHttpClient = new MyHttpClient(mContext);	
			}else{
				mHttpClient = new DefaultHttpClient();
			}
			mHttpPost = new HttpPost(new StringBuilder().append(URL).append(GET_ZOUPONS_DEALS).toString());
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.clear();
			list.add(new BasicNameValuePair("storeid", storeId));
			list.add(new BasicNameValuePair("employeeid", user_id));
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
			e.printStackTrace();
		}
		return mReturnValue;
	}
	
	
	
	public String sendFile(String location_id,File file) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL+UPLOAD_VIDEO);
		FileBody filebodyVideo = new FileBody(file);
		StringBody location = null;
		try {
			location = new StringBody(location_id, "text/plain", Charset.forName("UTF-8"));
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("location_id", new StringBody(location_id, "text/plain", Charset.forName("UTF-8")));
			reqEntity.addPart("app_token", new StringBody(mTokenId, "text/plain", Charset.forName("UTF-8")));
			reqEntity.addPart("uploadedfile", filebodyVideo);
			httppost.setEntity(reqEntity);
			// DEBUG
			//System.out.println( "executing request " + httppost.getRequestLine( ) );
			//System.out.println( "executing location id param " + location.getCharset() + " "+location.getMimeType());
			String content = EntityUtils.toString(httpclient.execute(httppost).getEntity(), "UTF-8");
			mReturnValue=content;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mReturnValue;
	}


	public String uploadFileToServer(String location_id,File file) {
		String response = "error";
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		String pathToOurFile = file.getPath();
		String urlServer = URL+UPLOAD_VIDEO;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			java.net.URL url = new java.net.URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setChunkedStreamingMode(1024);
			// Enable POST method
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);

			outputStream.writeBytes("Content-Disposition: form-data; name=\"location_id\"" + lineEnd);
			outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
			outputStream.writeBytes("Content-Length: " + location_id.length() + lineEnd);
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(location_id + lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);

			String connstr = null;
			connstr = "Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
					+ pathToOurFile + "\"" + lineEnd;

			outputStream.writeBytes(connstr);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			//System.out.println("Image length " + bytesAvailable + "");
			try {
				while (bytesRead > 0) {
					try {
						outputStream.write(buffer, 0, bufferSize);
					} catch (OutOfMemoryError e) {
						e.printStackTrace();
						response = "outofmemoryerror";
						return response;
					}
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
			} catch (Exception e) {
				e.printStackTrace();
				response = "error";
				return response;
			}
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);
			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();
			String serverResponseMessage = connection.getResponseMessage();
			//System.out.println("Server Response Code " + " " + serverResponseCode);
			//System.out.println("Server Response Message "+ serverResponseMessage);

			if (serverResponseCode == 200) {
				response = "true";
			}else
			{
				response = "false";
			}

			fileInputStream.close();
			outputStream.flush();

			connection.getInputStream();
			java.io.InputStream is = connection.getInputStream();

			int ch;
			StringBuffer b = new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
			}

			String responseString = b.toString();
			//System.out.println("response string is" + responseString); //Here is the actual output

			outputStream.close();
			outputStream = null;

		} catch (Exception ex) {
			// Exception handling
			response = "error";
			//System.out.println("Send file Exception" + ex.getMessage() + "");
			ex.printStackTrace();
		}
		return response;
	}

	public int uploadFile(File sourceFile,String pathToOurFile) {

		int serverResponseCode = 0;
		String fileName = sourceFile.getPath();

		HttpURLConnection conn = null;
		DataOutputStream dos = null;  
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024; 

		try{
			// open a URL connection to the Servlet
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			java.net.URL url = new java.net.URL(URL+UPLOAD_VIDEO);

			// Open a HTTP  connection to  the URL
			conn = (HttpURLConnection) url.openConnection(); 
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("location_id", "248");
			conn.setRequestProperty("uploadedfile", fileName); 

			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd); 
			String connstr = "Content-Disposition: form-data; name=\"uploadedfile\";filename=\""+ pathToOurFile + "\"" + lineEnd;
			dos.writeBytes(connstr);
			dos.writeBytes(lineEnd);

			// create a buffer of  maximum size
			bytesAvailable = fileInputStream.available(); 

			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);  

			while (bytesRead > 0) {

				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);   

			}

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Responses from the server (code and message)
			serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();

			if(serverResponseCode == 200){}    

			//close the streams //
			fileInputStream.close();
			dos.flush();
			dos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return serverResponseCode; 
	}
}