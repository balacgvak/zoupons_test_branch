/**
 * 
 */
package com.us.zoupons.shopper.webService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.android.gms.maps.model.LatLng;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.ActivateUser_ClassVariables;
import com.us.zoupons.classvariables.AddFavorite_ClassVariables;
import com.us.zoupons.classvariables.CardOnFiles_ClassVariables;
import com.us.zoupons.classvariables.Categories_ClassVariables;
import com.us.zoupons.classvariables.ChangePwd_ClassVariables;
import com.us.zoupons.classvariables.FirstDataGlobalpayment_ClassVariables;
import com.us.zoupons.classvariables.GetForgotPwd_ClassVariables;
import com.us.zoupons.classvariables.GetSecurityQuestions_ClassVariables;
import com.us.zoupons.classvariables.LoginUser_ClassVariables;
import com.us.zoupons.classvariables.MobileCarriers_ClassVariables;
import com.us.zoupons.classvariables.POJOCardDetails;
import com.us.zoupons.classvariables.POJOFBfriendListData;
import com.us.zoupons.classvariables.POJONotificationDetails;
import com.us.zoupons.classvariables.POJOStoreInfo;
import com.us.zoupons.classvariables.POJOStorePhoto;
import com.us.zoupons.classvariables.POJOStoreTiming;
import com.us.zoupons.classvariables.POJOUserProfile;
import com.us.zoupons.classvariables.POJOUserSecurityDetails;
import com.us.zoupons.classvariables.POJOVideoURL;
import com.us.zoupons.classvariables.Search_ClassVariables;
import com.us.zoupons.classvariables.SecurityQuestions_ClassVariables;
import com.us.zoupons.classvariables.SendMobileVerfication_ClassVariables;
import com.us.zoupons.classvariables.ShareCoupon_ClassVariables;
import com.us.zoupons.classvariables.ShareStore_ClassVariables;
import com.us.zoupons.classvariables.SignUp_ClassVariables;
import com.us.zoupons.classvariables.StoreLike_ClassVariables;
import com.us.zoupons.classvariables.StoreLocator_ClassVariables;
import com.us.zoupons.classvariables.StoresQRCode_ClassVariables;
import com.us.zoupons.classvariables.SubCategories_ClassVariables;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.flagclasses.AccountLoginFlag;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.mobilepay.CheckPINClassVariables;
import com.us.zoupons.mobilepay.MyGiftCards_ClassVariables;
import com.us.zoupons.mobilepay.PaymentStatusVariables;
import com.us.zoupons.mobilepay.CardPurchase;
import com.us.zoupons.notification.NotificationDetails;
import com.us.zoupons.shopper.chatsupport.ChatSupportClassVariables;
import com.us.zoupons.shopper.chatsupport.POJOContactUsResponse;
import com.us.zoupons.shopper.coupons.CouponDetail;
import com.us.zoupons.shopper.coupons.POJOCouponsList;
import com.us.zoupons.shopper.friends.Friends;
import com.us.zoupons.shopper.giftcards_deals.GiftCardTransactionHistoryTask;
import com.us.zoupons.shopper.giftcards_deals.POJOAllGiftCards;
import com.us.zoupons.shopper.giftcards_deals.POJOGiftCardTransactionHistory;
import com.us.zoupons.shopper.giftcards_deals.POJOLimit;
import com.us.zoupons.shopper.home.ShopperHomePage;
import com.us.zoupons.shopper.invoice.InvoiceApproval;
import com.us.zoupons.shopper.invoice.POJOInvoiceList;
import com.us.zoupons.shopper.receipts.POJOReceiptsDetails;
import com.us.zoupons.shopper.receipts.Receipts;
import com.us.zoupons.shopper.refer_store.RewardsAdvertisementDetails;
import com.us.zoupons.shopper.reviews.POJOReviewStatus;
import com.us.zoupons.shopper.reviews.POJOStoreReviewDetails;
import com.us.zoupons.shopper.reviews.StoreReviews;
import com.us.zoupons.shopper.settings.Settings;
import com.us.zoupons.storeowner.coupons.StoreOwnerCoupons;
import com.us.zoupons.storeowner.reviews.StoreOwnerReviews;

/**
 * Class to Parse all webservice data.
 */
public class ZouponsParsingClass {

	private Context mContext;
	private Document mDoc;
	private NodeList mNodes,mChildNodes,mParentNode;
	private int mRecordSize;
	private Element mNodeElement,mParentElement,mChildElement;
	public static String SEND_GIFT_CARD_MESSAGE="";
	private String mEmptyValue="";
	private String mIntegerEmptyValue="0";

	public ZouponsParsingClass(Context context){
		this.mContext=context;
	}

	//function calling when registration for parsing GetSecurityQuestions webservice data
	public String parseGetSecurityQuestionsXmlData(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("securtiy_questions");						
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					WebServiceStaticArrays.mGetSecurityQuestionsEmailValidationList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						GetSecurityQuestions_ClassVariables obj = new GetSecurityQuestions_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mGetSecurityQuestions_EmailValidation=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mGetSecurityQuestions_EmailValidation = obj.mGetSecurityQuestions_EmailValidation.equals(null)?mEmptyValue:obj.mGetSecurityQuestions_EmailValidation;
						WebServiceStaticArrays.mGetSecurityQuestionsEmailValidationList.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	///function calling when forgotpassword for parsing GetSecurityQuestions webservice data
	public String parseGetSecurityQuestionsXmlDataNew(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("securtiy_questions");				//function to get value by its security_questions tag name
			mChildNodes = mDoc.getElementsByTagName("question");
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					WebServiceStaticArrays.mGetSecurityQuestionsEmailValidationList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						GetSecurityQuestions_ClassVariables obj = new GetSecurityQuestions_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()==1){
							obj.mGetSecurityQuestions_EmailValidation=ZouponsWebService.getValue(mNodeElement,"message");
							obj.mGetSecurityQuestions_EmailValidation = obj.mGetSecurityQuestions_EmailValidation.equals(null)?mEmptyValue:obj.mGetSecurityQuestions_EmailValidation;
							WebServiceStaticArrays.mGetSecurityQuestionsEmailValidationList.add(obj);
						}else if(mNodeElement.getChildNodes().getLength()>1){
							obj.mGetSecurityQuestions_UserId=ZouponsWebService.getValue(mNodeElement,"user_id");
							obj.mGetSecurityQuestions_UserId = obj.mGetSecurityQuestions_UserId.equals(null)?mEmptyValue:obj.mGetSecurityQuestions_UserId;
							obj.mGetSecurityQuestions_EmailValidation=ZouponsWebService.getValue(mNodeElement,"message");
							obj.mGetSecurityQuestions_EmailValidation = obj.mGetSecurityQuestions_EmailValidation.equals(null)?mEmptyValue:obj.mGetSecurityQuestions_EmailValidation;
							WebServiceStaticArrays.mGetSecurityQuestionsEmailValidationList.add(obj);
							if(mChildNodes!=null){
								WebServiceStaticArrays.mGetSecurityQuestionsAndIds.clear();
								for(int j=0;j<mChildNodes.getLength();j++){
									GetSecurityQuestions_ClassVariables objNew = new GetSecurityQuestions_ClassVariables();
									mChildElement = (Element) mChildNodes.item(j);
									objNew.mGetSecurityQuestions_QuestionId=ZouponsWebService.getValue(mChildElement,"id");
									objNew.mGetSecurityQuestions_QuestionId=objNew.mGetSecurityQuestions_QuestionId.equals(null)?mEmptyValue:objNew.mGetSecurityQuestions_QuestionId;
									objNew.mGetSecurityQuestions_Question=ZouponsWebService.getValue(mChildElement,"security_question");
									objNew.mGetSecurityQuestions_Question=objNew.mGetSecurityQuestions_Question.equals(null)?mEmptyValue:objNew.mGetSecurityQuestions_Question;
									objNew.mGetSecurityQuestions_QuestionNumber=ZouponsWebService.getValue(mChildElement,"question_no");
									objNew.mGetSecurityQuestions_QuestionNumber=objNew.mGetSecurityQuestions_QuestionNumber.equals(null)?mEmptyValue:objNew.mGetSecurityQuestions_QuestionNumber;

									WebServiceStaticArrays.mGetSecurityQuestionsAndIds.add(objNew);
								}
							}
						}
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function called to parse login webservice data 
	public String parseLoginXmlData(String response,String classname) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("validateuser");						//function to get value by its validateuser tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					WebServiceStaticArrays.mLoginUserList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						LoginUser_ClassVariables obj = new LoginUser_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()==1){
							obj.mFlag="no";
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							WebServiceStaticArrays.mLoginUserList.add(obj);
						}else if(mNodeElement.getChildNodes().getLength()>1){
							obj.mFlag="yes";
							obj.authToken=ZouponsWebService.getValue(mNodeElement,"auth_token");
							obj.authToken=obj.authToken.equals(null)?mEmptyValue:obj.authToken;
							obj.userID=ZouponsWebService.getValue(mNodeElement,"id");
							obj.userID=obj.userID.equals(null)?mEmptyValue:obj.userID;
							obj.firstName=ZouponsWebService.getValue(mNodeElement,"first_name");
							obj.firstName=obj.firstName.equals(null)?mEmptyValue:obj.firstName;
							obj.lastName=ZouponsWebService.getValue(mNodeElement,"last_name");
							obj.lastName=obj.lastName.equals(null)?mEmptyValue:obj.lastName;
							obj.emailID=ZouponsWebService.getValue(mNodeElement,"email");
							obj.emailID=obj.emailID.equals(null)?mEmptyValue:obj.emailID;
							obj.fbID=ZouponsWebService.getValue(mNodeElement,"facebook");
							obj.fbID=obj.fbID.equals(null)?mEmptyValue:obj.fbID;
							obj.pin=ZouponsWebService.getValue(mNodeElement,"pin");
							obj.pin=obj.pin.equals(null)?mEmptyValue:obj.pin;
							obj.forgotPwdFlag=ZouponsWebService.getValue(mNodeElement,"forgot_pwd_flag");
							obj.forgotPwdFlag=obj.forgotPwdFlag.equals(null)?mEmptyValue:obj.forgotPwdFlag;
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							obj.usertype=ZouponsWebService.getValue(mNodeElement,"user_type");
							obj.usertype=obj.usertype.equals(null)?mEmptyValue:obj.usertype;
							// To get status of mobile verification
							obj.isMobileVerified=ZouponsWebService.getValue(mNodeElement,"mobile_verified");
							obj.isMobileVerified=obj.isMobileVerified.equals(null)?mEmptyValue:obj.isMobileVerified;
							// To get profile picture path
							obj.mUserProfilePath=ZouponsWebService.getValue(mNodeElement,"profile_picture");
							obj.mUserProfilePath=obj.mUserProfilePath.equals(null)?mEmptyValue:obj.mUserProfilePath;
							// To get store id
							obj.mStoreId=ZouponsWebService.getValue(mNodeElement,"store_id");
							obj.mStoreId=obj.mStoreId.equals(null)?mEmptyValue:obj.mStoreId;
							//Class Variable should assign value when control from zouponslogin page.
							if(classname.equalsIgnoreCase("ZouponsLogin")){
								AccountLoginFlag.accountUserTypeflag = obj.usertype;
							}
							//If user type is store_employee storing his permission details
							obj.information_access=ZouponsWebService.getValue(mNodeElement,"information");
							obj.information_access=obj.information_access.equals(null)?mEmptyValue:obj.information_access;
							obj.gift_cards_access=ZouponsWebService.getValue(mNodeElement,"gift_cards");
							obj.gift_cards_access=obj.gift_cards_access.equals(null)?mEmptyValue:obj.gift_cards_access;
							obj.deal_cards_access=ZouponsWebService.getValue(mNodeElement,"deal_cards");
							obj.deal_cards_access=obj.deal_cards_access.equals(null)?mEmptyValue:obj.deal_cards_access;
							obj.coupons_access=ZouponsWebService.getValue(mNodeElement,"coupons");
							obj.coupons_access=obj.coupons_access.equals(null)?mEmptyValue:obj.coupons_access;
							obj.reviews_access=ZouponsWebService.getValue(mNodeElement,"review_and_rating");
							obj.reviews_access=obj.reviews_access.equals(null)?mEmptyValue:obj.reviews_access;
							obj.photos_access=ZouponsWebService.getValue(mNodeElement,"photos");
							obj.photos_access=obj.photos_access.equals(null)?mEmptyValue:obj.photos_access;
							obj.videos_access=ZouponsWebService.getValue(mNodeElement,"videos");
							obj.videos_access=obj.videos_access.equals(null)?mEmptyValue:obj.videos_access;
							obj.dashboard_access=ZouponsWebService.getValue(mNodeElement,"dashboard");
							obj.dashboard_access=obj.dashboard_access.equals(null)?mEmptyValue:obj.dashboard_access;
							obj.point_of_sale_access=ZouponsWebService.getValue(mNodeElement,"point_of_sale");
							obj.point_of_sale_access=obj.point_of_sale_access.equals(null)?mEmptyValue:obj.point_of_sale_access;
							obj.invoice_center_access=ZouponsWebService.getValue(mNodeElement,"Invoice_center");
							obj.invoice_center_access=obj.invoice_center_access.equals(null)?mEmptyValue:obj.invoice_center_access;
							obj.refund_access=ZouponsWebService.getValue(mNodeElement,"refund");
							obj.refund_access=obj.refund_access.equals(null)?mEmptyValue:obj.refund_access;
							obj.batch_sales_access=ZouponsWebService.getValue(mNodeElement,"batch_sales");
							obj.batch_sales_access=obj.batch_sales_access.equals(null)?mEmptyValue:obj.batch_sales_access;
							obj.customer_center_access=ZouponsWebService.getValue(mNodeElement,"customer_center");
							obj.customer_center_access=obj.customer_center_access.equals(null)?mEmptyValue:obj.customer_center_access;
							obj.communication_access=ZouponsWebService.getValue(mNodeElement,"zoupons_support");
							obj.communication_access=obj.communication_access.equals(null)?mEmptyValue:obj.communication_access;
							obj.location_access=ZouponsWebService.getValue(mNodeElement,"location");
							obj.location_access=obj.location_access.equals(null)?mEmptyValue:obj.location_access;
							obj.employee_access=ZouponsWebService.getValue(mNodeElement,"employees");
							obj.employee_access=obj.employee_access.equals(null)?mEmptyValue:obj.employee_access;
							obj.billing_access=ZouponsWebService.getValue(mNodeElement,"billing");
							obj.billing_access=obj.billing_access.equals(null)?mEmptyValue:obj.billing_access;
							WebServiceStaticArrays.mLoginUserList.add(obj);
						}
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}


	public String parseUserTypeResponse(String mGetResponse) {
		// TODO Auto-generated method stub
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetResponse);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("user_types");				
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					SharedPreferences mPrefs = mContext.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
					Editor mPreferenceEditor = mPrefs.edit();
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						String user_type=ZouponsWebService.getValue(mNodeElement,"user_type");
						user_type=user_type.equals(null)?mEmptyValue:user_type;
						String store_id=ZouponsWebService.getValue(mNodeElement,"store_id");
						store_id=store_id.equals(null)?mEmptyValue:store_id;
						String is_mobile_verified = ZouponsWebService.getValue(mNodeElement,"mobile_verified");
						is_mobile_verified = is_mobile_verified.equals(null)?mEmptyValue:is_mobile_verified;
						String first_name = ZouponsWebService.getValue(mNodeElement,"first_name");
						first_name = first_name.equals(null)?mEmptyValue:first_name;
						String last_name = ZouponsWebService.getValue(mNodeElement,"last_name");
						last_name = last_name.equals(null)?mEmptyValue:last_name;
						String profile_url = ZouponsWebService.getValue(mNodeElement,"profile_picture");
						profile_url = profile_url.equals(null)?mEmptyValue:profile_url;
						mPreferenceEditor.putString("user_type", user_type);
						mPreferenceEditor.putString("store_id", store_id);
						mPreferenceEditor.putString("mobile_verified", is_mobile_verified);
						mPreferenceEditor.putString("first_name", first_name);
						mPreferenceEditor.putString("last_name", last_name);
						mPreferenceEditor.putString("profile_url", profile_url);
						mPreferenceEditor.commit();
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function called to parse forgot_pwd webservice data 
	public String parseGetForgotPwdXmlData(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("forgot_pwd");				//function to get value by its forgot_pwd tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mForgotPasswordList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						GetForgotPwd_ClassVariables obj = new GetForgotPwd_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()==1){
							obj.mFlag="yes";
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							WebServiceStaticArrays.mForgotPasswordList.add(obj);
						}else if(mNodeElement.getChildNodes().getLength()>1){
							obj.mFlag="no";
							obj.userId=ZouponsWebService.getValue(mNodeElement,"id");
							obj.userId=obj.userId.equals(null)?mEmptyValue:obj.userId;
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;

							WebServiceStaticArrays.mForgotPasswordList.add(obj);
						}
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function called to parse forgot_pwd webservice data 
	public String parseChangePwdXmlData(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("change_pwd");				//function to get value by its change_pwd tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mChangePasswordList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						ChangePwd_ClassVariables obj = new ChangePwd_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						//Log.i(TAG,"obj.message : "+obj.mMessage);
						WebServiceStaticArrays.mChangePasswordList.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing SecurityQuestions webservice data
	public String parseSecurityQuestionsXmlData(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("questions");							
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mSecurityQuestionsList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						SecurityQuestions_ClassVariables obj = new SecurityQuestions_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mSecurityQuestios_securityquestionid=ZouponsWebService.getValue(mNodeElement,"id");
						obj.mSecurityQuestios_securityquestionid=obj.mSecurityQuestios_securityquestionid.equals(null)?mEmptyValue:obj.mSecurityQuestios_securityquestionid;
						obj.mSecurityQuestions_securityquestion=ZouponsWebService.getValue(mNodeElement,"question");
						obj.mSecurityQuestions_securityquestion=obj.mSecurityQuestions_securityquestion.equals(null)?mEmptyValue:obj.mSecurityQuestions_securityquestion;
						obj.mSecurityQuestions_securityquestionstatus=ZouponsWebService.getValue(mNodeElement,"question_status");
						obj.mSecurityQuestions_securityquestionstatus=obj.mSecurityQuestions_securityquestionstatus.equals(null)?mEmptyValue:obj.mSecurityQuestions_securityquestionstatus;
						obj.mSecurityQuestions_datecreated=ZouponsWebService.getValue(mNodeElement,"date_created");
						obj.mSecurityQuestions_datecreated=obj.mSecurityQuestions_datecreated.equals(null)?mEmptyValue:obj.mSecurityQuestions_datecreated;
						obj.mSecurityQuestions_questionset=ZouponsWebService.getValue(mNodeElement,"question_set");
						obj.mSecurityQuestions_questionset=obj.mSecurityQuestions_questionset.equals(null)?mEmptyValue:obj.mSecurityQuestions_questionset;
						obj.mIsSelected="no";
						WebServiceStaticArrays.mSecurityQuestionsList.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing MobileCarriers webservice data
	public String parseMobileCarriersXmlData(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("mobile_carrier");					//function to get value by its mobile_carrier tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mMobileCarriersList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						MobileCarriers_ClassVariables obj = new MobileCarriers_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mId=ZouponsWebService.getValue(mNodeElement,"id");
						obj.mId=obj.mId.equals(null)?mEmptyValue:obj.mId;
						obj.mName=ZouponsWebService.getValue(mNodeElement,"name");
						obj.mName=obj.mName.equals(null)?mEmptyValue:obj.mName;
						obj.mSendEmailAt=ZouponsWebService.getValue(mNodeElement,"send_email_at");
						obj.mSendEmailAt=obj.mSendEmailAt.equals(null)?mEmptyValue:obj.mSendEmailAt;
						obj.mStatus=ZouponsWebService.getValue(mNodeElement,"status");
						obj.mStatus=obj.mStatus.equals(null)?mEmptyValue:obj.mStatus;
						WebServiceStaticArrays.mMobileCarriersList.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing SignUp webservice data
	public String parseSignUp(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("signup");							//function to get value by its signup tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mSignUpList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						SignUp_ClassVariables obj = new SignUp_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mId=ZouponsWebService.getValue(mNodeElement,"id");
						obj.mId=obj.mId.equals(null)?mEmptyValue:obj.mId;
						obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						obj.mAuthToken=ZouponsWebService.getValue(mNodeElement,"auth_token");
						obj.mAuthToken=obj.mAuthToken.equals(null)?mEmptyValue:obj.mAuthToken;
						WebServiceStaticArrays.mSignUpList.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing SignUp webservice data
	public String parseActivateuser(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("validateuser");							//function to get value by its validateuser tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mActivateUser.clear();
					for(int i=0;i<mNodes.getLength();i++){
						ActivateUser_ClassVariables obj = new ActivateUser_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()>1){
							obj.userId=ZouponsWebService.getValue(mNodeElement,"id");
							obj.userId=obj.userId.equals(null)?mEmptyValue:obj.userId;
							obj.firstName=ZouponsWebService.getValue(mNodeElement,"first_name");
							obj.firstName=obj.firstName.equals(null)?mEmptyValue:obj.firstName;
							obj.lastName=ZouponsWebService.getValue(mNodeElement,"last_name");
							obj.lastName=obj.lastName.equals(null)?mEmptyValue:obj.lastName;
							obj.email=ZouponsWebService.getValue(mNodeElement,"email");
							obj.email=obj.email.equals(null)?mEmptyValue:obj.email;
							obj.fbId=ZouponsWebService.getValue(mNodeElement,"fb_id");
							obj.fbId=obj.fbId.equals(null)?mEmptyValue:obj.fbId;
							obj.pin=ZouponsWebService.getValue(mNodeElement,"pin");
							obj.pin=obj.pin.equals(null)?mEmptyValue:obj.pin;
							obj.forgorPwdFlag=ZouponsWebService.getValue(mNodeElement,"forgot_pwd_flag");
							obj.forgorPwdFlag=obj.forgorPwdFlag.equals(null)?mEmptyValue:obj.forgorPwdFlag;
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							WebServiceStaticArrays.mActivateUser.add(obj);
						}
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing first_date_global_payment webservice data
	public String parsefirstdataglobalpayment(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("payment");					
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mFirstDataGlobalPaymentList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						FirstDataGlobalpayment_ClassVariables obj = new FirstDataGlobalpayment_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mPin=ZouponsWebService.getValue(mNodeElement,"pin");
						obj.mPin=obj.mPin.equals(null)?mEmptyValue:obj.mPin;
						obj.mAuthorizationNum=ZouponsWebService.getValue(mNodeElement,"Authorization_Num");
						obj.mAuthorizationNum=obj.mAuthorizationNum.equals(null)?mEmptyValue:obj.mAuthorizationNum;
						obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						obj.mCardId=ZouponsWebService.getValue(mNodeElement,"card_id");
						obj.mCardId=obj.mCardId.equals(null)?mEmptyValue:obj.mCardId;
						WebServiceStaticArrays.mFirstDataGlobalPaymentList.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing categories webservice data
	public String parseCategories(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("category");							//function to get value by its category tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mCategoriesList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						Categories_ClassVariables obj = new Categories_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()>1){
							obj.id=ZouponsWebService.getValue(mNodeElement,"id");
							obj.id=obj.id.equals(null)?mEmptyValue:obj.id;
							obj.categoryName=ZouponsWebService.getValue(mNodeElement,"category_name");
							obj.categoryName=obj.categoryName.equals(null)?mEmptyValue:obj.categoryName;
							obj.description=ZouponsWebService.getValue(mNodeElement,"description");
							obj.description=obj.description.equals(null)?mEmptyValue:obj.description;
							obj.type=ZouponsWebService.getValue(mNodeElement,"type");
							obj.type=obj.type.equals(null)?mEmptyValue:obj.type;
							obj.parentId=ZouponsWebService.getValue(mNodeElement,"parent_id");
							obj.parentId=obj.parentId.equals(null)?mEmptyValue:obj.parentId;
							obj.addedBy=ZouponsWebService.getValue(mNodeElement,"added_by");
							obj.addedBy=obj.addedBy.equals(null)?mEmptyValue:obj.addedBy;
							obj.addedTime=ZouponsWebService.getValue(mNodeElement,"added_time");
							obj.addedTime=obj.addedTime.equals(null)?mEmptyValue:obj.addedTime;
							obj.visitorId=ZouponsWebService.getValue(mNodeElement,"visitor_id");
							obj.visitorId=obj.visitorId.equals(null)?mEmptyValue:obj.visitorId;
							obj.subcategoryCount=ZouponsWebService.getValue(mNodeElement,"subcategory_count");
							obj.subcategoryCount=obj.subcategoryCount.equals(null)||obj.subcategoryCount.equals("")?"0":obj.subcategoryCount;
							WebServiceStaticArrays.mCategoriesList.add(obj);
						}else if(mNodeElement.getChildNodes().getLength()==1){
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							//Log.i(TAG,"obj.message : "+obj.message);
							WebServiceStaticArrays.mCategoriesList.add(obj);
						}
					}
					if(mNodes.getLength()>0){
						rtnValue="success";
					}else{
						mNodes = mDoc.getElementsByTagName("categories");						//function to get value by its categories tag name
						if(mNodes!=null&&mNodes.getLength()>0){
							for(int i=0;i<mNodes.getLength();i++){
								Categories_ClassVariables obj = new Categories_ClassVariables();
								mNodeElement = (Element) mNodes.item(i);
								obj.message=ZouponsWebService.getValue(mNodeElement,"message");
								obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
								//Log.i(TAG,"obj.message : "+obj.message);
								WebServiceStaticArrays.mCategoriesList.add(obj);
							}
							rtnValue="success";
						}else{
							rtnValue="norecords";
						}
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing sub categories webservice data
	public String parseSubCategories(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("subcategory");						//function to get value by its sub category tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mSubCategoriesList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						SubCategories_ClassVariables obj = new SubCategories_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()>1){
							obj.id=ZouponsWebService.getValue(mNodeElement,"id");
							obj.id=obj.id.equals(null)?mEmptyValue:obj.id;
							obj.categoryName=ZouponsWebService.getValue(mNodeElement,"category_name");
							obj.categoryName=obj.categoryName.equals(null)?mEmptyValue:obj.categoryName;
							obj.description=ZouponsWebService.getValue(mNodeElement,"description");
							obj.description=obj.description.equals(null)?mEmptyValue:obj.description;
							obj.type=ZouponsWebService.getValue(mNodeElement,"type");
							obj.type=obj.type.equals(null)?mEmptyValue:obj.type;
							obj.parentId=ZouponsWebService.getValue(mNodeElement,"parent_id");
							obj.parentId=obj.parentId.equals(null)?mEmptyValue:obj.parentId;
							obj.addedBy=ZouponsWebService.getValue(mNodeElement,"added_by");
							obj.addedBy=obj.addedBy.equals(null)?mEmptyValue:obj.addedBy;
							obj.addedTime=ZouponsWebService.getValue(mNodeElement,"added_time");
							obj.addedTime=obj.addedTime.equals(null)?mEmptyValue:obj.addedTime;
							obj.visitorId=ZouponsWebService.getValue(mNodeElement,"visitor_id");
							obj.visitorId=obj.visitorId.equals(null)?mEmptyValue:obj.visitorId;
							WebServiceStaticArrays.mSubCategoriesList.add(obj);
						}else if(mNodeElement.getChildNodes().getLength()==1){
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							//Log.i(TAG,"obj.message : "+obj.message);
							WebServiceStaticArrays.mSubCategoriesList.add(obj);
						}
					}
					if(mNodes.getLength()>0){
						rtnValue="success";
					}else{
						mNodes = mDoc.getElementsByTagName("categories");						//function to get value by its categories tag name
						if(mNodes!=null&&mNodes.getLength()>0){
							for(int i=0;i<mNodes.getLength();i++){
								SubCategories_ClassVariables obj = new SubCategories_ClassVariables();
								mNodeElement = (Element) mNodes.item(i);
								obj.message=ZouponsWebService.getValue(mNodeElement,"message");
								obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
								//Log.i(TAG,"obj.message : "+obj.message);
								WebServiceStaticArrays.mSubCategoriesList.add(obj);
							}
							rtnValue="success";
						}else{
							rtnValue="norecords";
						}
					}
				}else{
					mNodes = mDoc.getElementsByTagName("subcategories");						//function to get value by its categories tag name
					if(mNodes!=null&&mNodes.getLength()>0){
						for(int i=0;i<mNodes.getLength();i++){
							SubCategories_ClassVariables obj = new SubCategories_ClassVariables();
							mNodeElement = (Element) mNodes.item(i);
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							//Log.i(TAG,"obj.message : "+obj.message);
							WebServiceStaticArrays.mSubCategoriesList.add(obj);
						}
						rtnValue="success";
					}else{
						rtnValue="norecords";
					}
					//rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing store search webservice data
	public ArrayList<Search_ClassVariables> parseStoreSearch(String response) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		//UrlImage urlimage = new UrlImage();
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("store");							//function to get value by its store tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mSearchStore.clear();
					for(int i=0;i<mNodes.getLength();i++){
						Search_ClassVariables obj = new Search_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);

						if(mNodeElement.getChildNodes().getLength()>1){

							obj.storeId=ZouponsWebService.getValue(mNodeElement,"id");
							obj.storeName=ZouponsWebService.getValue(mNodeElement,"store_name");

						}else if(mNodeElement.getChildNodes().getLength()==1){
							obj.message=ZouponsWebService.getValue(mNodeElement,"message").equals(null)?obj.message:ZouponsWebService.getValue(mNodeElement,"message");
							//Log.i(TAG,"obj.message : "+obj.message);
						}
						WebServiceStaticArrays.mSearchStore.add(obj);
					}
					//Log.i(TAG,"SearchStore List Size: "+WebServiceStaticArrays.mSearchStore.size());
					if(mNodes.getLength()>0){
					}else{
						mNodes = mDoc.getElementsByTagName("stores");						//function to get value by its store tag name
						if(mNodes!=null&&mNodes.getLength()>0){
							for(int i=0;i<mNodes.getLength();i++){
								Search_ClassVariables obj = new Search_ClassVariables();
								mNodeElement = (Element) mNodes.item(i);
								obj.message=ZouponsWebService.getValue(mNodeElement,"message").equals(null)?obj.message:ZouponsWebService.getValue(mNodeElement,"message");
								//Log.i(TAG,"obj.message : "+obj.message);
								WebServiceStaticArrays.mStoreCategoriesList.add(obj);
							}
						}else{
						}
					}
				}else{
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return WebServiceStaticArrays.mSearchStore;
	}

	//function for parsing giftcard search webservice data
	public String parseGiftCardSearch(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("giftcard");							//function to get value by its store tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mSearchGiftCard.clear();
					for(int i=0;i<mNodes.getLength();i++){
						Search_ClassVariables obj = new Search_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()>1){
							obj.giftCardId=ZouponsWebService.getValue(mNodeElement,"id");
							obj.giftCardId=obj.giftCardId.equals(null)?mEmptyValue:obj.giftCardId;
							obj.giftCardType=ZouponsWebService.getValue(mNodeElement,"type");
							obj.giftCardType=obj.giftCardType.equals(null)?mEmptyValue:obj.giftCardType;
							obj.giftCardPrivateUserId=ZouponsWebService.getValue(mNodeElement,"private_user_id");
							obj.giftCardPrivateUserId=obj.giftCardPrivateUserId.equals(null)?mEmptyValue:obj.giftCardPrivateUserId;
							obj.giftCardDescription=ZouponsWebService.getValue(mNodeElement,"description");
							obj.giftCardDescription=obj.giftCardDescription.equals(null)?mEmptyValue:obj.giftCardDescription;
							obj.giftCardStoreId=ZouponsWebService.getValue(mNodeElement,"store_id");
							obj.giftCardStoreId=obj.giftCardStoreId.equals(null)?mEmptyValue:obj.giftCardStoreId;
							obj.giftCardFaceValue=ZouponsWebService.getValue(mNodeElement,"face_value");
							obj.giftCardFaceValue=obj.giftCardFaceValue.equals(null)?mEmptyValue:obj.giftCardFaceValue;
							obj.giftCardSellPrice=ZouponsWebService.getValue(mNodeElement,"sell_price");
							obj.giftCardSellPrice=obj.giftCardSellPrice.equals(null)?mEmptyValue:obj.giftCardSellPrice;
							obj.giftCardCardType=ZouponsWebService.getValue(mNodeElement,"card_type");
							obj.giftCardCardType=obj.giftCardCardType.equals(null)?mEmptyValue:obj.giftCardCardType;
							obj.giftCardStatus=ZouponsWebService.getValue(mNodeElement,"status");
							obj.giftCardStatus=obj.giftCardStatus.equals(null)?mEmptyValue:obj.giftCardStatus;
							obj.giftCardAddedBy=ZouponsWebService.getValue(mNodeElement,"added_by");
							obj.giftCardAddedBy=obj.giftCardAddedBy.equals(null)?mEmptyValue:obj.giftCardAddedBy;
							obj.giftCardModifiedTime=ZouponsWebService.getValue(mNodeElement,"modified_time");
							obj.giftCardModifiedTime=obj.giftCardModifiedTime.equals(null)?mEmptyValue:obj.giftCardModifiedTime;
							obj.giftCardModifiedBy=ZouponsWebService.getValue(mNodeElement,"modified_by");
							obj.giftCardModifiedBy=obj.giftCardModifiedBy.equals(null)?mEmptyValue:obj.giftCardModifiedBy;
							obj.giftCardVisitorId=ZouponsWebService.getValue(mNodeElement,"visitor_id");
							obj.giftCardVisitorId=obj.giftCardVisitorId.equals(null)?mEmptyValue:obj.giftCardVisitorId;
							WebServiceStaticArrays.mSearchGiftCard.add(obj);
						}else if(mNodeElement.getChildNodes().getLength()==1){
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							//Log.i(TAG,"obj.message : "+obj.message);
							WebServiceStaticArrays.mSearchGiftCard.add(obj);
						}
					}
					if(mNodes.getLength()>0){
						rtnValue="success";
					}else{
						mNodes = mDoc.getElementsByTagName("giftcards");						//function to get value by its store tag name
						if(mNodes!=null&&mNodes.getLength()>0){
							for(int i=0;i<mNodes.getLength();i++){
								Search_ClassVariables obj = new Search_ClassVariables();
								mNodeElement = (Element) mNodes.item(i);
								obj.message=ZouponsWebService.getValue(mNodeElement,"message");
								obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
								//Log.i(TAG,"obj.message : "+obj.message);
								WebServiceStaticArrays.mSearchGiftCard.add(obj);
							}
							rtnValue="success";
						}else{
							rtnValue="norecords";
						}
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing coupon search webservice data
	public String parseCouponSearch(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("coupon");							//function to get value by its coupon tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mSearchCoupon.clear();
					for(int i=0;i<mNodes.getLength();i++){
						Search_ClassVariables obj = new Search_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()>1){
							obj.couponId=ZouponsWebService.getValue(mNodeElement,"id");
							obj.couponId=obj.couponId.equals(null)?mEmptyValue:obj.couponId;
							obj.couponType=ZouponsWebService.getValue(mNodeElement,"type");
							obj.couponType=obj.couponType.equals(null)?mEmptyValue:obj.couponType;
							obj.couponCustomerPhoneNumber=ZouponsWebService.getValue(mNodeElement,"customer_phone_number");
							obj.couponCustomerPhoneNumber=obj.couponCustomerPhoneNumber.equals(null)?mEmptyValue:obj.couponCustomerPhoneNumber;
							obj.couponTitle=ZouponsWebService.getValue(mNodeElement,"title");
							obj.couponTitle=obj.couponTitle.equals(null)?mEmptyValue:obj.couponTitle;
							obj.couponCouponCode=ZouponsWebService.getValue(mNodeElement,"coupon_code");
							obj.couponCouponCode=obj.couponCouponCode.equals(null)?mEmptyValue:obj.couponCouponCode;
							obj.couponCategory=ZouponsWebService.getValue(mNodeElement,"category");
							obj.couponCategory=obj.couponCategory.equals(null)?mEmptyValue:obj.couponCategory;
							obj.couponCompany=ZouponsWebService.getValue(mNodeElement,"company");
							obj.couponCompany=obj.couponCompany.equals(null)?mEmptyValue:obj.couponCompany;
							obj.couponBarcode=ZouponsWebService.getValue(mNodeElement,"barcode");
							obj.couponBarcode=obj.couponBarcode.equals(null)?mEmptyValue:obj.couponBarcode;
							obj.couponStoreId=ZouponsWebService.getValue(mNodeElement,"store_id");
							obj.couponStoreId=obj.couponStoreId.equals(null)?mEmptyValue:obj.couponStoreId;
							obj.couponActivationDate=ZouponsWebService.getValue(mNodeElement,"activation_date");
							obj.couponActivationDate=obj.couponActivationDate.equals(null)?mEmptyValue:obj.couponActivationDate;
							obj.couponExpirationDate=ZouponsWebService.getValue(mNodeElement,"expiration_date");
							obj.couponExpirationDate=obj.couponExpirationDate.equals(null)?mEmptyValue:obj.couponExpirationDate;
							obj.couponAddedDate=ZouponsWebService.getValue(mNodeElement,"added_date");
							obj.couponAddedDate=obj.couponAddedDate.equals(null)?mEmptyValue:obj.couponAddedDate;
							obj.couponDescription=ZouponsWebService.getValue(mNodeElement,"description");
							obj.couponDescription=obj.couponDescription.equals(null)?mEmptyValue:obj.couponDescription;
							obj.couponImage=ZouponsWebService.getValue(mNodeElement,"image");
							obj.couponImage=obj.couponImage.equals(null)?mEmptyValue:obj.couponImage;
							obj.couponTags=ZouponsWebService.getValue(mNodeElement,"tags");
							obj.couponTags=obj.couponTags.equals(null)?mEmptyValue:obj.couponTags;
							obj.couponDiscountType=ZouponsWebService.getValue(mNodeElement,"discount_type");
							obj.couponDiscountType=obj.couponDiscountType.equals(null)?mEmptyValue:obj.couponDiscountType;
							obj.couponAddedBy=ZouponsWebService.getValue(mNodeElement,"added_by");
							obj.couponAddedBy=obj.couponAddedBy.equals(null)?mEmptyValue:obj.couponAddedBy;
							obj.couponAddedVia=ZouponsWebService.getValue(mNodeElement,"added_via");
							obj.couponAddedVia=obj.couponAddedVia.equals(null)?mEmptyValue:obj.couponAddedVia;
							obj.couponVisitorId=ZouponsWebService.getValue(mNodeElement,"visitor_id");
							obj.couponVisitorId=obj.couponVisitorId.equals(null)?mEmptyValue:obj.couponVisitorId;
							obj.couponStatus=ZouponsWebService.getValue(mNodeElement,"status");
							obj.couponStatus=obj.couponStatus.equals(null)?mEmptyValue:obj.couponStatus;
							WebServiceStaticArrays.mSearchCoupon.add(obj);
						}else if(mNodeElement.getChildNodes().getLength()==1){
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							//Log.i(TAG,"obj.message : "+obj.message);
							WebServiceStaticArrays.mSearchCoupon.add(obj);
						}
					}
					if(mNodes.getLength()>0){
						rtnValue="success";
					}else{
						mNodes = mDoc.getElementsByTagName("coupons");						//function to get value by its store tag name
						if(mNodes!=null&&mNodes.getLength()>0){
							for(int i=0;i<mNodes.getLength();i++){
								Search_ClassVariables obj = new Search_ClassVariables();
								mNodeElement = (Element) mNodes.item(i);
								obj.message=ZouponsWebService.getValue(mNodeElement,"message");
								obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
								//Log.i(TAG,"obj.message : "+obj.message);
								WebServiceStaticArrays.mSearchCoupon.add(obj);
							}
							rtnValue="success";
						}else{
							rtnValue="norecords";
						}
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing stores_location categories webservice data
	public String parseStore_Locator(String response,String classname) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		//UrlImage urlimage = new UrlImage();
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mParentNode = mDoc.getElementsByTagName("stores");
			mNodes = mDoc.getElementsByTagName("store");							//function to get value by its store tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mParentNode!=null){
					//Log.i("parent node ", "not null"+"\n"+"ParentLength: "+mParentNode.getLength());
					if(mParentNode.getLength() > 0){
						mParentElement = (Element) mParentNode.item(0);
						if(classname.equalsIgnoreCase("ShopperHomePage")){
							ShopperHomePage.sStoreDetailsCount=ZouponsWebService.getValue(mParentElement,"total_count");
							ShopperHomePage.sStoreDetailsCount=ShopperHomePage.sStoreDetailsCount.equals(null)?"0":ShopperHomePage.sStoreDetailsCount;
							if(ShopperHomePage.sStoreDetailsCount.equals("")){
								ShopperHomePage.sStoreDetailsCount="0";
							}
						}else{
							CardPurchase.mZPAYStoreDetailsCount=ZouponsWebService.getValue(mParentElement,"total_count");
							CardPurchase.mZPAYStoreDetailsCount=CardPurchase.mZPAYStoreDetailsCount.equals(null)?"0":CardPurchase.mZPAYStoreDetailsCount;
							if(CardPurchase.mZPAYStoreDetailsCount.equals("")){
								CardPurchase.mZPAYStoreDetailsCount="0";
							}
						}
					}
					if(mNodes!=null&&mNodes.getLength()>0){
						//Log.i(TAG,"Node Length : "+mNodes.getLength());
						WebServiceStaticArrays.mOffsetStoreDetails.clear();
						for(int i=0;i<mNodes.getLength();i++){
							StoreLocator_ClassVariables obj = new StoreLocator_ClassVariables();
							mNodeElement = (Element) mNodes.item(i);
							if(mNodeElement.getChildNodes().getLength()>1){
								obj.id=ZouponsWebService.getValue(mNodeElement,"id");
								obj.id=obj.id.equals(null)?mEmptyValue:obj.id;
								// To assign location id
								obj.location_id=ZouponsWebService.getValue(mNodeElement,"location_id");
								obj.location_id=obj.location_id.equals(null)?mEmptyValue:obj.location_id;
								obj.storeName=ZouponsWebService.getValue(mNodeElement,"store_name");
								obj.storeName=obj.storeName.equals(null)?mEmptyValue:obj.storeName;
								obj.description=ZouponsWebService.getValue(mNodeElement,"description");
								obj.description=obj.description.equals(null)?mEmptyValue:obj.description;
								obj.shortDescription=ZouponsWebService.getValue(mNodeElement,"short_description");
								obj.shortDescription=obj.shortDescription.equals(null)?mEmptyValue:obj.shortDescription;
								obj.addressLine1=ZouponsWebService.getValue(mNodeElement,"address_line1");
								obj.addressLine1=obj.addressLine1.equals(null)?mEmptyValue:obj.addressLine1;
								obj.addressLine2=ZouponsWebService.getValue(mNodeElement,"address_line2");
								obj.addressLine2=obj.addressLine2.equals(null)?mEmptyValue:obj.addressLine2;
								obj.city=ZouponsWebService.getValue(mNodeElement,"city");
								obj.city=obj.city.equals(null)?mEmptyValue:obj.city;
								obj.zipcode=ZouponsWebService.getValue(mNodeElement,"zip_code");
								obj.zipcode=obj.zipcode.equals(null)?mEmptyValue:obj.zipcode;
								obj.state=ZouponsWebService.getValue(mNodeElement,"state");
								obj.state=obj.state.equals(null)?mEmptyValue:obj.state;
								obj.subcategory=ZouponsWebService.getValue(mNodeElement,"subcategory");
								obj.subcategory=obj.subcategory.equals(null)?mEmptyValue:obj.subcategory;
								obj.category=ZouponsWebService.getValue(mNodeElement,"category");
								obj.category=obj.category.equals(null)?mEmptyValue:obj.category;
								obj.logoPath=ZouponsWebService.getValue(mNodeElement,"logo_path");
								obj.logoPath=obj.logoPath.equals(null)?mEmptyValue:obj.logoPath;
								obj.latitude=ZouponsWebService.getValue(mNodeElement,"lat");
								obj.latitude=obj.latitude.equals(null)?mEmptyValue:obj.latitude;
								obj.longitude=ZouponsWebService.getValue(mNodeElement,"lon");
								obj.longitude=obj.longitude.equals(null)?mEmptyValue:obj.longitude;
								obj.distance=ZouponsWebService.getValue(mNodeElement,"distance");
								obj.distance=obj.distance.equals(null)?"0.0":obj.distance;
								obj.deviceDistance=ZouponsWebService.getValue(mNodeElement,"distance_devicelocation");
								obj.deviceDistance=obj.deviceDistance.equals(null)?"0.0":obj.deviceDistance;
								obj.like_count=ZouponsWebService.getValue(mNodeElement,"likes");
								obj.like_count=obj.like_count.equals(null)||obj.like_count.equals("")?mIntegerEmptyValue:obj.like_count;
								obj.invoice_center_ordering=ZouponsWebService.getValue(mNodeElement,"invoice_center_ordering");
								obj.invoice_center_ordering=obj.invoice_center_ordering.equals(null)?mEmptyValue:obj.invoice_center_ordering;
								obj.rightmenu_location_flag=ZouponsWebService.getValue(mNodeElement,"location");
								obj.rightmenu_location_flag=obj.rightmenu_location_flag.equals(null)?mEmptyValue:obj.rightmenu_location_flag;
								obj.rightmenu_coupons_flag=ZouponsWebService.getValue(mNodeElement,"coupons");
								obj.rightmenu_coupons_flag=obj.rightmenu_coupons_flag.equals(null)?mEmptyValue:obj.rightmenu_coupons_flag;
								obj.rightmenu_aboutus_flag=ZouponsWebService.getValue(mNodeElement,"about_us");
								obj.rightmenu_aboutus_flag=obj.rightmenu_aboutus_flag.equals(null)?mEmptyValue:obj.rightmenu_aboutus_flag;
								obj.rightmenu_reviewandrating_flag=ZouponsWebService.getValue(mNodeElement,"review_and_rating");
								obj.rightmenu_reviewandrating_flag=obj.rightmenu_reviewandrating_flag.equals(null)?mEmptyValue:obj.rightmenu_reviewandrating_flag;
								obj.rightmenu_photos_flag=ZouponsWebService.getValue(mNodeElement,"photos");
								obj.rightmenu_photos_flag=obj.rightmenu_photos_flag.equals(null)?mEmptyValue:obj.rightmenu_photos_flag;
								obj.rightmenu_contactstore_flag=ZouponsWebService.getValue(mNodeElement,"contact_store");
								obj.rightmenu_contactstore_flag=obj.rightmenu_contactstore_flag.equals(null)?mEmptyValue:obj.rightmenu_contactstore_flag;
								obj.rightmenu_giftcards_flag=ZouponsWebService.getValue(mNodeElement,"gift_cards");
								obj.rightmenu_giftcards_flag=obj.rightmenu_giftcards_flag.equals(null)?mEmptyValue:obj.rightmenu_giftcards_flag;
								obj.rightmenu_videos_flag=ZouponsWebService.getValue(mNodeElement,"videos");
								obj.rightmenu_videos_flag=obj.rightmenu_videos_flag.equals(null)?mEmptyValue:obj.rightmenu_videos_flag;
								obj.rightmenu_invoicecenter_flag=ZouponsWebService.getValue(mNodeElement,"Invoice_center");
								obj.rightmenu_invoicecenter_flag=obj.rightmenu_invoicecenter_flag.equals(null)?mEmptyValue:obj.rightmenu_invoicecenter_flag;
								obj.rightmenu_communication_flag=ZouponsWebService.getValue(mNodeElement,"communication");
								obj.rightmenu_communication_flag=obj.rightmenu_communication_flag.equals(null)?mEmptyValue:obj.rightmenu_communication_flag;
								obj.rightmenu_pointofsale_flag=ZouponsWebService.getValue(mNodeElement,"point_of_sale");
								obj.rightmenu_pointofsale_flag=obj.rightmenu_pointofsale_flag.equals(null)?mEmptyValue:obj.rightmenu_pointofsale_flag;
								obj.rightmenu_zcards_flag=ZouponsWebService.getValue(mNodeElement,"deal_cards");
								obj.rightmenu_zcards_flag=obj.rightmenu_zcards_flag.equals(null)?mEmptyValue:obj.rightmenu_zcards_flag;
								obj.favorite_store=ZouponsWebService.getValue(mNodeElement,"favorite_store");
								obj.favorite_store=obj.favorite_store.equals(null)?mEmptyValue:obj.favorite_store;
								obj.rightmenu_share_flag=ZouponsWebService.getValue(mNodeElement,"share");
								obj.rightmenu_share_flag=obj.rightmenu_share_flag.equals(null)?mEmptyValue:obj.rightmenu_share_flag;
								obj.rightmenu_batchsales_flag=ZouponsWebService.getValue(mNodeElement,"batch_sales");
								obj.rightmenu_batchsales_flag=obj.rightmenu_batchsales_flag.equals(null)?mEmptyValue:obj.rightmenu_batchsales_flag;
								obj.rightmenu_refund_flag=ZouponsWebService.getValue(mNodeElement,"refund");
								obj.rightmenu_refund_flag=obj.rightmenu_refund_flag.equals(null)?mEmptyValue:obj.rightmenu_refund_flag;
								obj.rightmenu_mobilepayment_flag=ZouponsWebService.getValue(mNodeElement,"mobile_payment");
								obj.rightmenu_mobilepayment_flag=obj.rightmenu_mobilepayment_flag.equals(null)?mEmptyValue:obj.rightmenu_mobilepayment_flag;
								obj.rightmenu_rewardcards_flag=ZouponsWebService.getValue(mNodeElement,"reward_cards");
								obj.rightmenu_rewardcards_flag=obj.rightmenu_rewardcards_flag.equals(null)?mEmptyValue:obj.rightmenu_rewardcards_flag;

								if(!(obj.latitude.equals("")&&obj.longitude.equals(""))){
									obj.storeCoordinates=new LatLng(Double.parseDouble(obj.latitude),Double.parseDouble(obj.longitude));
								}
								//These flag is add for list sorting when add or remove favorite in right navigation
								if(obj.deviceDistance.equalsIgnoreCase("-1")){
									obj.storesortflag=3;	//Success if online
									obj.like_count="0";
								}else{
									if(obj.rightmenu_pointofsale_flag.equalsIgnoreCase("Yes")){
										obj.storesortflag=1;	//Success if Retail Preffered
									}else {
										obj.storesortflag=2;	//Success if Retail
										obj.like_count="0";
									}
								}
								WebServiceStaticArrays.mOffsetStoreDetails.add(obj);
							}else if(mNodeElement.getChildNodes().getLength()==1){
								obj.message=ZouponsWebService.getValue(mNodeElement,"message");
								obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
								//Log.i(TAG,"obj.message : "+obj.message);
								WebServiceStaticArrays.mOffsetStoreDetails.add(obj);
							}
						}
						if(mNodes.getLength()>0){
							rtnValue="success";
						}else{
							if(classname.equalsIgnoreCase("ShopperHomePage")){
								ShopperHomePage.sStoreDetailsCount = "0";	
							}else{
								CardPurchase.mZPAYStoreDetailsCount = "0";
							}
							WebServiceStaticArrays.mOffsetStoreDetails.clear();
							mNodes = mDoc.getElementsByTagName("stores");						//function to get value by its store tag name
							if(mNodes!=null&&mNodes.getLength()>0){
								for(int i=0;i<mNodes.getLength();i++){
									StoreLocator_ClassVariables obj = new StoreLocator_ClassVariables();
									mNodeElement = (Element) mNodes.item(i);
									obj.message=ZouponsWebService.getValue(mNodeElement,"message");
									obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
									//Log.i(TAG,"obj.message : "+obj.message);
									WebServiceStaticArrays.mOffsetStoreDetails.add(obj);
								}
								rtnValue="success";
							}else{
								if(classname.equalsIgnoreCase("ShopperHomePage")){
									ShopperHomePage.sStoreDetailsCount = "0";	
								}else{
									CardPurchase.mZPAYStoreDetailsCount = "0";
								}
								rtnValue="norecords";
							}
						}
					}else{
						WebServiceStaticArrays.mOffsetStoreDetails.clear();
						mNodes = mDoc.getElementsByTagName("stores");						//function to get value by its store tag name
						if(mNodes!=null&&mNodes.getLength()>0){
							for(int i=0;i<mNodes.getLength();i++){
								StoreLocator_ClassVariables obj = new StoreLocator_ClassVariables();
								mNodeElement = (Element) mNodes.item(i);
								obj.message=ZouponsWebService.getValue(mNodeElement,"message");
								obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
								//Log.i(TAG,"obj.message : "+obj.message);
								WebServiceStaticArrays.mOffsetStoreDetails.add(obj);
							}
							rtnValue="success";
						}else{
							if(classname.equalsIgnoreCase("ShopperHomePage")){
								ShopperHomePage.sStoreDetailsCount = "0";	
							}else{
								CardPurchase.mZPAYStoreDetailsCount = "0";
							}
							rtnValue="norecords";
						}
					}
				}else{
					if(classname.equalsIgnoreCase("ShopperHomePage")){
						ShopperHomePage.sStoreDetailsCount = "0";	
					}else{
						CardPurchase.mZPAYStoreDetailsCount = "0";
					}
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing stores_qrcode categories webservice data
	public String parseStore_qrcode(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		//UrlImage urlimage = new UrlImage();
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("store");							//function to get value by its store tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mStoresQRCode.clear();
					for(int i=0;i<mNodes.getLength();i++){
						StoresQRCode_ClassVariables obj = new StoresQRCode_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()>1){
							obj.id=ZouponsWebService.getValue(mNodeElement,"id");
							obj.id=obj.id.equals(null)?mEmptyValue:obj.id;
							obj.storeName=ZouponsWebService.getValue(mNodeElement,"store_name");
							obj.storeName=obj.storeName.equals(null)?mEmptyValue:obj.storeName;
							obj.description=ZouponsWebService.getValue(mNodeElement,"description");
							obj.description=obj.description.equals(null)?mEmptyValue:obj.description;
							obj.shortDescription=ZouponsWebService.getValue(mNodeElement,"short_description");
							obj.shortDescription=obj.shortDescription.equals(null)?mEmptyValue:obj.shortDescription;
							obj.addressLine1=ZouponsWebService.getValue(mNodeElement,"address_line1");
							obj.addressLine1=obj.addressLine1.equals(null)?mEmptyValue:obj.addressLine1;
							obj.addressLine2=ZouponsWebService.getValue(mNodeElement,"address_line2");
							obj.addressLine2=obj.addressLine2.equals(null)?mEmptyValue:obj.addressLine2;
							obj.logoPath=ZouponsWebService.getValue(mNodeElement,"logo_path");
							obj.logoPath=obj.logoPath.equals(null)?mEmptyValue:obj.logoPath;
							if(obj.logoPath.contains("")){
								obj.logoPath = obj.logoPath.replaceAll("\\s","%20");
							}
							obj.city=ZouponsWebService.getValue(mNodeElement,"city");
							obj.city=obj.city.equals(null)?mEmptyValue:obj.city;
							obj.latitude=ZouponsWebService.getValue(mNodeElement,"lat");
							obj.latitude=obj.latitude.equals(null)?mEmptyValue:obj.latitude;
							obj.longitude=ZouponsWebService.getValue(mNodeElement,"lon");
							obj.longitude=obj.longitude.equals(null)?mEmptyValue:obj.longitude;
							obj.favorite_store=ZouponsWebService.getValue(mNodeElement,"favorite_store");
							obj.favorite_store=obj.favorite_store.equals(null)?mEmptyValue:obj.favorite_store;

							if(obj.logoPath.equals("")){
								obj.storeImage=null;
							}else{
								obj.storeImage=getBitmapFromURL(obj.logoPath);
							}


							WebServiceStaticArrays.mStoresQRCode.add(obj);
						}else if(mNodeElement.getChildNodes().getLength()==1){
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							//Log.i(TAG,"obj.message : "+obj.message);
							WebServiceStaticArrays.mStoresQRCode.add(obj);
						}
					}
					if(mNodes.getLength()>0){
						rtnValue="success";
					}else{
						mNodes = mDoc.getElementsByTagName("stores");						//function to get value by its store tag name
						if(mNodes!=null&&mNodes.getLength()>0){
							for(int i=0;i<mNodes.getLength();i++){
								StoresQRCode_ClassVariables obj = new StoresQRCode_ClassVariables();
								mNodeElement = (Element) mNodes.item(i);
								obj.message=ZouponsWebService.getValue(mNodeElement,"message");
								obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
								//Log.i(TAG,"obj.message : "+obj.message);
								WebServiceStaticArrays.mStoresQRCode.add(obj);
							}
							rtnValue="success";
						}else{
							rtnValue="norecords";
						}
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function called to parse forgot_pwd webservice data 
	public String parseCheckFavoriteXmlData(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("user_favourites");					//function to get value by its user_favourites tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mStoresCheckFavorite.clear();
					for(int i=0;i<mNodes.getLength();i++){
						AddFavorite_ClassVariables obj = new AddFavorite_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						//Log.i(TAG,"obj.message : "+obj.mMessage);
						WebServiceStaticArrays.mStoresCheckFavorite.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function called to parse forgot_pwd webservice data 
	public String parseAddFavoriteXmlData(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("user_favourites");					//function to get value by its user_favourites tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mStoresAddFavorite.clear();
					for(int i=0;i<mNodes.getLength();i++){
						AddFavorite_ClassVariables obj = new AddFavorite_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						//Log.i(TAG,"obj.message : "+obj.mMessage);
						WebServiceStaticArrays.mStoresAddFavorite.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing stores_location categories webservice data
	public String parseCard_On_Files(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);

		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("card");							//function to get value by its card tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mCardOnFiles.clear();
					for(int i=0;i<mNodes.getLength();i++){
						CardOnFiles_ClassVariables obj = new CardOnFiles_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()>1){
							obj.cardId=ZouponsWebService.getValue(mNodeElement,"id");
							obj.cardId=obj.cardId.equals(null)?mEmptyValue:obj.cardId;
							obj.cardName=ZouponsWebService.getValue(mNodeElement,"card_name");
							obj.cardName=obj.cardName.equals(null)?mEmptyValue:obj.cardName;
							obj.CardHolderName=ZouponsWebService.getValue(mNodeElement,"card_holder_name");
							obj.CardHolderName=obj.CardHolderName.equals(null)?mEmptyValue:obj.CardHolderName;
							obj.cvv=ZouponsWebService.getValue(mNodeElement,"cvv");
							obj.cvv=obj.cvv.equals(null)?mEmptyValue:obj.cvv;
							obj.expiryMonth=ZouponsWebService.getValue(mNodeElement,"expiry_month");
							obj.expiryMonth=obj.expiryMonth.equals(null)?mEmptyValue:obj.expiryMonth;
							obj.expiryYear=ZouponsWebService.getValue(mNodeElement,"expiry_year");
							obj.expiryYear=obj.expiryYear.equals(null)?mEmptyValue:obj.expiryYear;
							obj.StreatAddress=ZouponsWebService.getValue(mNodeElement,"street_address");
							obj.StreatAddress=obj.StreatAddress.equals(null)?mEmptyValue:obj.StreatAddress;
							obj.zipCode=ZouponsWebService.getValue(mNodeElement,"zip_code");
							obj.zipCode=obj.zipCode.equals(null)?mEmptyValue:obj.zipCode;
							obj.cardType=ZouponsWebService.getValue(mNodeElement,"card_type");
							obj.cardType=obj.cardType.equals(null)?mEmptyValue:obj.cardType;
							obj.cardMask=ZouponsWebService.getValue(mNodeElement,"card_mask");
							obj.cardMask=obj.cardMask.equals(null)?mEmptyValue:obj.cardMask;
							obj.status=ZouponsWebService.getValue(mNodeElement,"status");
							obj.status=obj.status.equals(null)?mEmptyValue:obj.status;
							WebServiceStaticArrays.mCardOnFiles.add(obj);
						}else if(mNodeElement.getChildNodes().getLength()==1){
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							//Log.i(TAG,"obj.message : "+obj.message);
							WebServiceStaticArrays.mCardOnFiles.add(obj);
						}
					}
					if(mNodes.getLength()>0){
						rtnValue="success";
					}else{
						mNodes = mDoc.getElementsByTagName("cards");						//function to get value by its cards tag name
						if(mNodes!=null&&mNodes.getLength()>0){
							for(int i=0;i<mNodes.getLength();i++){
								CardOnFiles_ClassVariables obj = new CardOnFiles_ClassVariables();
								mNodeElement = (Element) mNodes.item(i);
								obj.message=ZouponsWebService.getValue(mNodeElement,"message");
								obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
								//Log.i(TAG,"obj.message : "+obj.message);
								WebServiceStaticArrays.mCardOnFiles.add(obj);
							}
							rtnValue="success";
						}else{
							rtnValue="norecords";
						}
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing updateuserpin categories webservice data
	public String parseUpdateUserPin(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("validatepin");							//function to get value by its card tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						rtnValue=ZouponsWebService.getValue(mNodeElement,"message");
						rtnValue=rtnValue.equals(null)?mEmptyValue:rtnValue;
						//.i(TAG,"ReturnValue : "+rtnValue);
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing remove_card categories webservice data
	public String parseRemoveCard(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("payment");							//function to get value by its payment tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//(TAG,"Node Length : "+mNodes.getLength());
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						rtnValue=ZouponsWebService.getValue(mNodeElement,"message");
						rtnValue=rtnValue.equals(null)?mEmptyValue:rtnValue;
						//Log.i(TAG,"ReturnValue : "+rtnValue);
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	// To parse store locations response
	public String ParseLocatorResponse(String mLocatorResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mLocatorResponse);
			mNodes = mDoc.getElementsByTagName("store");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{

				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mStoreLocatorList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						StoreLocator_ClassVariables obj = new StoreLocator_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.id=ZouponsWebService.getValue(mNodeElement,"id");
						obj.id=obj.id.equals(null)?mEmptyValue:obj.id;		        			
						obj.location_id=ZouponsWebService.getValue(mNodeElement,"location_id");
						obj.location_id=obj.location_id.equals(null)?mEmptyValue:obj.location_id;						
						obj.storeName=ZouponsWebService.getValue(mNodeElement,"store_name");
						obj.storeName=obj.storeName.equals(null)?mEmptyValue:obj.storeName;
						obj.description=ZouponsWebService.getValue(mNodeElement,"description");
						obj.description=obj.description.equals(null)?mEmptyValue:obj.description;
						obj.addressLine1=ZouponsWebService.getValue(mNodeElement,"address_line1");
						obj.addressLine1=obj.addressLine1.equals(null)?mEmptyValue:obj.addressLine1;
						obj.addressLine2=ZouponsWebService.getValue(mNodeElement,"address_line2");
						obj.addressLine2=obj.addressLine2.equals(null)?mEmptyValue:obj.addressLine2;
						obj.city=ZouponsWebService.getValue(mNodeElement,"city");
						obj.city=obj.city.equals(null)?mEmptyValue:obj.city;
						obj.state=ZouponsWebService.getValue(mNodeElement,"state");
						obj.state=obj.state.equals(null)?mEmptyValue:obj.state;
						obj.zipcode=ZouponsWebService.getValue(mNodeElement,"zip_code");
						obj.zipcode=obj.zipcode.equals(null)?mEmptyValue:obj.zipcode;
						obj.logoPath=ZouponsWebService.getValue(mNodeElement,"logo_path");
						obj.logoPath=obj.logoPath.equals(null)?mEmptyValue:obj.logoPath;
						obj.latitude=ZouponsWebService.getValue(mNodeElement,"lat");
						obj.latitude=obj.latitude.equals(null)?mEmptyValue:obj.latitude;
						obj.longitude=ZouponsWebService.getValue(mNodeElement,"lon");
						obj.longitude=obj.longitude.equals(null)?mEmptyValue:obj.longitude;
						obj.distance=ZouponsWebService.getValue(mNodeElement,"distance");
						obj.distance=obj.distance.equals(null)?mIntegerEmptyValue:obj.distance;
						obj.deviceDistance=ZouponsWebService.getValue(mNodeElement,"distance_devicelocation");
						obj.deviceDistance=obj.deviceDistance.equals(null)?mIntegerEmptyValue:obj.deviceDistance;
						if(!(obj.latitude.equals("")&&obj.longitude.equals(""))){
							obj.storeCoordinates=new LatLng(Double.parseDouble(obj.latitude), Double.parseDouble(obj.longitude));
						}
						obj.rightmenu_location_flag=ZouponsWebService.getValue(mNodeElement,"location");
						obj.rightmenu_location_flag=obj.rightmenu_location_flag.equals(null)?mEmptyValue:obj.rightmenu_location_flag;
						obj.rightmenu_coupons_flag=ZouponsWebService.getValue(mNodeElement,"coupons");
						obj.rightmenu_coupons_flag=obj.rightmenu_coupons_flag.equals(null)?mEmptyValue:obj.rightmenu_coupons_flag;
						obj.rightmenu_aboutus_flag=ZouponsWebService.getValue(mNodeElement,"about_us");
						obj.rightmenu_aboutus_flag=obj.rightmenu_aboutus_flag.equals(null)?mEmptyValue:obj.rightmenu_aboutus_flag;
						obj.rightmenu_reviewandrating_flag=ZouponsWebService.getValue(mNodeElement,"review_and_rating");
						obj.rightmenu_reviewandrating_flag=obj.rightmenu_reviewandrating_flag.equals(null)?mEmptyValue:obj.rightmenu_reviewandrating_flag;
						obj.rightmenu_photos_flag=ZouponsWebService.getValue(mNodeElement,"photos");
						obj.rightmenu_photos_flag=obj.rightmenu_photos_flag.equals(null)?mEmptyValue:obj.rightmenu_photos_flag;
						obj.rightmenu_contactstore_flag=ZouponsWebService.getValue(mNodeElement,"contact_store");
						obj.rightmenu_contactstore_flag=obj.rightmenu_contactstore_flag.equals(null)?mEmptyValue:obj.rightmenu_contactstore_flag;
						obj.rightmenu_giftcards_flag=ZouponsWebService.getValue(mNodeElement,"gift_cards");
						obj.rightmenu_giftcards_flag=obj.rightmenu_giftcards_flag.equals(null)?mEmptyValue:obj.rightmenu_giftcards_flag;
						obj.rightmenu_videos_flag=ZouponsWebService.getValue(mNodeElement,"videos");
						obj.rightmenu_videos_flag=obj.rightmenu_videos_flag.equals(null)?mEmptyValue:obj.rightmenu_videos_flag;
						obj.rightmenu_invoicecenter_flag=ZouponsWebService.getValue(mNodeElement,"Invoice_center");
						obj.rightmenu_invoicecenter_flag=obj.rightmenu_invoicecenter_flag.equals(null)?mEmptyValue:obj.rightmenu_invoicecenter_flag;
						obj.rightmenu_communication_flag=ZouponsWebService.getValue(mNodeElement,"communication");
						obj.rightmenu_communication_flag=obj.rightmenu_communication_flag.equals(null)?mEmptyValue:obj.rightmenu_communication_flag;
						obj.rightmenu_pointofsale_flag=ZouponsWebService.getValue(mNodeElement,"point_of_sale");
						obj.rightmenu_pointofsale_flag=obj.rightmenu_pointofsale_flag.equals(null)?mEmptyValue:obj.rightmenu_pointofsale_flag;
						obj.rightmenu_zcards_flag=ZouponsWebService.getValue(mNodeElement,"deal_cards");
						obj.rightmenu_zcards_flag=obj.rightmenu_zcards_flag.equals(null)?mEmptyValue:obj.rightmenu_zcards_flag;
						obj.like_count=ZouponsWebService.getValue(mNodeElement,"likes");
						obj.like_count=obj.like_count.equals(null)||obj.like_count.equals("")?mIntegerEmptyValue:obj.like_count;
						obj.favorite_store=ZouponsWebService.getValue(mNodeElement,"favorite_store");
						obj.favorite_store=obj.favorite_store.equals(null)?mEmptyValue:obj.favorite_store;
						obj.rightmenu_share_flag=ZouponsWebService.getValue(mNodeElement,"share");
						obj.rightmenu_share_flag=obj.rightmenu_share_flag.equals(null)?mEmptyValue:obj.rightmenu_share_flag;
						obj.rightmenu_batchsales_flag=ZouponsWebService.getValue(mNodeElement,"batch_sales");
						obj.rightmenu_batchsales_flag=obj.rightmenu_batchsales_flag.equals(null)?mEmptyValue:obj.rightmenu_batchsales_flag;
						obj.rightmenu_refund_flag=ZouponsWebService.getValue(mNodeElement,"refund");
						obj.rightmenu_refund_flag=obj.rightmenu_refund_flag.equals(null)?mEmptyValue:obj.rightmenu_refund_flag;
						obj.rightmenu_mobilepayment_flag=ZouponsWebService.getValue(mNodeElement,"mobile_payment");
						obj.rightmenu_mobilepayment_flag=obj.rightmenu_mobilepayment_flag.equals(null)?mEmptyValue:obj.rightmenu_mobilepayment_flag;
						obj.rightmenu_rewardcards_flag=ZouponsWebService.getValue(mNodeElement,"reward_cards");
						obj.rightmenu_rewardcards_flag=obj.rightmenu_rewardcards_flag.equals(null)?mEmptyValue:obj.rightmenu_rewardcards_flag;

						//These flag is add for list sorting when add or remove favorite in right navigation
						if(obj.deviceDistance.equalsIgnoreCase("-1")){
							obj.storesortflag=3;	//Success if online
							obj.like_count="0";
						}else{
							if(obj.rightmenu_pointofsale_flag.equalsIgnoreCase("Yes")){
								obj.storesortflag=1;	//Success if Retail Preffered
							}else {
								obj.storesortflag=2;	//Success if Retail
								obj.like_count="0";
							}
						}

						WebServiceStaticArrays.mStoreLocatorList.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch (Exception e) {

			rtnValue="failure";
		}
		return rtnValue;
	}

	// To parse store information response
	public String mParseStoreInfo(String mGetResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("stores");						
			mChildNodes = mDoc.getElementsByTagName("store");
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mStaticStoreInfo.clear();
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						rtnValue = "norecords";
					}else{
						rtnValue = "success";
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								for(int j=0;j<mChildNodes.getLength();j++){	
									POJOStoreInfo obj = new POJOStoreInfo();
									mNodeElement = (Element) mChildNodes.item(i);
									obj.store_id=ZouponsWebService.getValue(mNodeElement,"store_id");
									obj.store_id=obj.store_id.equals(null)?mEmptyValue:obj.store_id;
									obj.store_name=ZouponsWebService.getValue(mNodeElement,"store_name");
									obj.store_name=obj.store_name.equals(null)?mEmptyValue:obj.store_name;
									obj.short_description=ZouponsWebService.getValue(mNodeElement,"short_description");
									obj.short_description=obj.short_description.equals(null)?mEmptyValue:obj.short_description;
									obj.description=ZouponsWebService.getValue(mNodeElement,"description");
									obj.description=obj.description.equals(null)?mEmptyValue:obj.description;
									obj.first_name=ZouponsWebService.getValue(mNodeElement,"first_name");
									obj.first_name=obj.first_name.equals(null)?mEmptyValue:obj.first_name;
									obj.last_name=ZouponsWebService.getValue(mNodeElement,"last_name");
									obj.last_name=obj.last_name.equals(null)?mEmptyValue:obj.last_name;
									obj.email=ZouponsWebService.getValue(mNodeElement,"email");
									obj.email=obj.email.equals(null)?mEmptyValue:obj.email;
									obj.phone=ZouponsWebService.getValue(mNodeElement,"phone");
									obj.phone=obj.phone.equals(null)?mEmptyValue:obj.phone;
									obj.fax=ZouponsWebService.getValue(mNodeElement,"fax");
									obj.fax=obj.fax.equals(null)?mEmptyValue:obj.fax;
									obj.address_line1=ZouponsWebService.getValue(mNodeElement,"address_line1");
									obj.address_line1=obj.address_line1.equals(null)?mEmptyValue:obj.address_line1;
									obj.address_line2=ZouponsWebService.getValue(mNodeElement,"address_line2");
									obj.address_line2=obj.address_line2.equals(null)?mEmptyValue:obj.address_line2;
									obj.city=ZouponsWebService.getValue(mNodeElement,"city");
									obj.city=obj.city.equals(null)?mEmptyValue:obj.state;
									obj.country=ZouponsWebService.getValue(mNodeElement,"country");
									obj.country=obj.country.equals(null)?mEmptyValue:obj.country;
									obj.zip_code=ZouponsWebService.getValue(mNodeElement,"zip_code");
									obj.zip_code=obj.zip_code.equals(null)?mEmptyValue:obj.zip_code;
									obj.website=ZouponsWebService.getValue(mNodeElement,"website");
									obj.website=obj.website.equals(null)?mEmptyValue:obj.website;
									obj.qr_code=ZouponsWebService.getValue(mNodeElement,"qr_code");
									obj.qr_code=obj.qr_code.equals(null)?mEmptyValue:obj.qr_code;		        			
									obj.logo_path=ZouponsWebService.getValue(mNodeElement,"logo_path");
									obj.logo_path=obj.logo_path.equals(null)?mEmptyValue:obj.logo_path;
									obj.video_title=ZouponsWebService.getValue(mNodeElement,"video_title");
									obj.video_title=obj.video_title.equals(null)?mEmptyValue:obj.video_title;
									obj.video_url=ZouponsWebService.getValue(mNodeElement,"video_url");
									obj.video_url=obj.video_url.equals(null)?mEmptyValue:obj.video_url;
									String mVideoThumbnail=ZouponsWebService.getValue(mNodeElement,"video_thumbnail");
									mVideoThumbnail=mVideoThumbnail.equals(null)?mEmptyValue:mVideoThumbnail;
									if(mVideoThumbnail.equals("")){
										obj.video_thumbnail = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.appicon);	
									}else{
										//Log.i("video"," "+ZouponsWebService.getValue(mNodeElement,"video_thumbnail"));
										obj.video_thumbnail=getBitmapFromURL(mVideoThumbnail);
									}
									obj.store_type=ZouponsWebService.getValue(mNodeElement,"store_type");
									obj.store_type=obj.store_type.equals(null)?mEmptyValue:obj.store_type;			        			
									//Log.i(TAG,"obj.mForgotPassword_EmailValidation : "+obj.store_id);
									WebServiceStaticArrays.mStaticStoreInfo.add(obj);
								}
							}

						}
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch (Exception e) {
			rtnValue="failure";
		}
		return rtnValue;
	}

	// To parse store photo
	public String mParseStorePhoto(String mPhotoResponse,int viewpagerwidth,int viewpagerheight) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mPhotoResponse);
			mNodes = mDoc.getElementsByTagName("store");						
			mChildNodes = mDoc.getElementsByTagName("photo");
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					for(int i=0;i<mNodes.getLength();i++){
						if(mChildNodes != null){
							WebServiceStaticArrays.mStorePhoto.clear();
							//WebServiceStaticArrays.mFullSizeStorePhotoUrl.clear();
							//WebServiceStaticArrays.mFullSizeStorePhoto.clear();
							if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
								rtnValue="norecords";
							}else{
								for(int j=0;j<mChildNodes.getLength();j++){
									POJOStorePhoto obj = new POJOStorePhoto();
									mChildElement = (Element) mChildNodes.item(j);
									obj.photo_id = ZouponsWebService.getValue(mChildElement,"id");
									obj.photo_id=obj.photo_id.equals(null)?mEmptyValue:obj.photo_id;
									obj.caption = ZouponsWebService.getValue(mChildElement,"caption");
									obj.caption=obj.caption.equals(null)?mEmptyValue:obj.caption;
									obj.full_size = ZouponsWebService.getValue(mChildElement,"full_size");
									obj.full_size=obj.full_size.equals(null)?mEmptyValue:obj.full_size;
									obj.order = ZouponsWebService.getValue(mChildElement,"order");
									obj.order =obj.order.equals(null)?mEmptyValue:obj.order;
									obj.mOriginalImageWidth = ZouponsWebService.getValue(mChildElement,"img_width");
									obj.mOriginalImageWidth =obj.mOriginalImageWidth.equals(null)?mEmptyValue:obj.mOriginalImageWidth;
									obj.mOriginalImageHeight = ZouponsWebService.getValue(mChildElement,"img_height");
									obj.mOriginalImageHeight =obj.mOriginalImageHeight.equals(null)?mEmptyValue:obj.mOriginalImageHeight;
									WebServiceStaticArrays.mStorePhoto.add(obj);
								}
								rtnValue="success";
							}
						}
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch (Exception e) {

			rtnValue="failure";
		}
		return rtnValue;
	}

	// To parse add store photo response
	public String parseAddStorePhotoResponse(String response){
		String returnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("stores");						
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				returnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					POJOStorePhoto obj = new POJOStorePhoto();
					mChildElement = (Element) mNodes.item(0);
					obj.message = ZouponsWebService.getValue(mChildElement,"message");
					obj.message =obj.message .equals(null)?mEmptyValue:obj.message;
					returnValue=obj.message;
					if(returnValue.equalsIgnoreCase("success")){
						returnValue=obj.message;
					}else{
						returnValue="failure";
					}
				}else{
					returnValue="norecords";
				}
			}
		}catch (Exception e) {

			returnValue="failure";
		}
		return returnValue;
	}

	// To parse store gc/dc card list response
	public String parseCardDetails(String response, String serviceFunction){
		String returnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("giftcards");						
			mChildNodes = mDoc.getElementsByTagName("giftcard");
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				returnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					if(mNodes.getLength() == 1 && mChildNodes.getLength()==0){
						returnValue="norecords";
					}else{
						returnValue="success";						

						for(int i=0;i<mNodes.getLength();i++){
							mParentElement = (Element) mNodes.item(i);
							if(serviceFunction.equalsIgnoreCase("Regular")){
								MainMenuActivity.mGiftCardTotalCount = ZouponsWebService.getValue(mParentElement, "total_count");
								MainMenuActivity.mGiftCardTotalCount = MainMenuActivity.mGiftCardTotalCount.equals(null)||MainMenuActivity.mGiftCardTotalCount.equals("")?"0":MainMenuActivity.mGiftCardTotalCount;
							}else{
								MainMenuActivity.mDealTotalCount = ZouponsWebService.getValue(mParentElement, "total_count");
								MainMenuActivity.mDealTotalCount = MainMenuActivity.mDealTotalCount.equals(null)||MainMenuActivity.mDealTotalCount.equals("")?"0":MainMenuActivity.mDealTotalCount;
							}
							if(mChildNodes != null){

								for(int j=0;j<mChildNodes.getLength();j++){	
									POJOCardDetails mCardDetail = new POJOCardDetails();
									mChildElement = (Element) mChildNodes.item(j);									
									mCardDetail.id = ZouponsWebService.getValue(mChildElement,"id");
									mCardDetail.id =mCardDetail.id.equals(null)?mEmptyValue:mCardDetail.id;
									mCardDetail.card_id = ZouponsWebService.getValue(mChildElement,"card_id");
									mCardDetail.card_id =mCardDetail.card_id.equals(null)?mEmptyValue:mCardDetail.card_id;
									mCardDetail.type = ZouponsWebService.getValue(mChildElement,"type");
									mCardDetail.type=mCardDetail.type.equals(null)?mEmptyValue:mCardDetail.type;
									mCardDetail.facevalue = ZouponsWebService.getValue(mChildElement,"face_value");
									mCardDetail.facevalue=mCardDetail.facevalue.equals(null)||mCardDetail.facevalue.equals("")?mIntegerEmptyValue:mCardDetail.facevalue;
									//Log.i(TAG,"FaceValue: "+mCardDetail.facevalue);
									if(!serviceFunction.equalsIgnoreCase("Regular")){
										mCardDetail.sellprice = ZouponsWebService.getValue(mChildElement,"sell_price");
										mCardDetail.sellprice=mCardDetail.sellprice.equals(null)||mCardDetail.sellprice.equals("")?mIntegerEmptyValue:mCardDetail.sellprice;
										//Log.i(TAG,"SellPrice: "+mCardDetail.sellprice);
									}
									mCardDetail.description = ZouponsWebService.getValue(mChildElement,"description");
									mCardDetail.description=mCardDetail.description.equals(null)?mEmptyValue:mCardDetail.description;
									mCardDetail.imagepath = ZouponsWebService.getValue(mChildElement,"image_path");
									mCardDetail.imagepath=mCardDetail.imagepath.equals(null)?mEmptyValue:mCardDetail.imagepath;
									if(mCardDetail.imagepath.contains("")){
										mCardDetail.imagepath = mCardDetail.imagepath.replaceAll("\\s","%20");
									}
									mCardDetail.remaining_count = ZouponsWebService.getValue(mChildElement,"per_week");
									mCardDetail.remaining_count=mCardDetail.remaining_count.equals(null)||mCardDetail.remaining_count.equals("")?mIntegerEmptyValue:mCardDetail.remaining_count;
									mCardDetail.actual_count_per_week = ZouponsWebService.getValue(mChildElement,"actual_per_week");
									mCardDetail.actual_count_per_week = mCardDetail.actual_count_per_week.equals(null)||mCardDetail.actual_count_per_week.equals("")?mIntegerEmptyValue:mCardDetail.actual_count_per_week;
									if(serviceFunction.equalsIgnoreCase("Regular")){
										WebServiceStaticArrays.mStoreRegularCardDetails.add(mCardDetail); 
									}else{
										WebServiceStaticArrays.mStoreZouponsDealCardDetails.add(mCardDetail);
									}
								}
							}
						}
					}
				}else{
					returnValue="norecords";
				}
			}
		}catch (Exception e) {

			returnValue="failure";
		}
		return returnValue;
	}

	//function for parsing fblike categories webservice data
	public String parseStoreLike(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("likes");							//function to get value by its fb_like tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mStoreLikeList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						StoreLike_ClassVariables obj = new StoreLike_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mSocialFBShareCount=ZouponsWebService.getValue(mNodeElement,"fb_count");
						obj.mSocialFBShareCount=obj.mSocialFBShareCount.equals(null)?mEmptyValue:obj.mSocialFBShareCount;
						obj.mSocialFBShareStatus=ZouponsWebService.getValue(mNodeElement,"fb_share_status");
						obj.mSocialFBShareStatus=obj.mSocialFBShareStatus.equals(null)?mEmptyValue:obj.mSocialFBShareStatus;
						obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						WebServiceStaticArrays.mStoreLikeList.add(obj);
						rtnValue="success";
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	// To parse store business hour details
	public String parseTimeInfo(String mResponse) {
		String returnvalue= " ";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mResponse);
			mNodes = mDoc.getElementsByTagName("store");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if(mRecordSize<=0){
				returnvalue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mStoreTiming.clear();
					if(mNodes.getLength() == 0){
						returnvalue="norecords";
					}else{
						returnvalue="success";
						for(int i=0;i<mNodes.getLength();i++){
							POJOStoreTiming obj = new POJOStoreTiming();
							mNodeElement = (Element) mNodes.item(i);
							obj.monday_from=ZouponsWebService.getValue(mNodeElement,"monday_from");
							obj.monday_from=obj.monday_from.equals(null)?mEmptyValue:convertTime(obj.monday_from);
							obj.monday_to=ZouponsWebService.getValue(mNodeElement,"monday_to");
							obj.monday_to=obj.monday_to.equals(null)?mEmptyValue:convertTime(obj.monday_to);
							obj.tuesday_from=ZouponsWebService.getValue(mNodeElement,"tuesday_from");
							obj.tuesday_from=obj.tuesday_from.equals(null)?mEmptyValue:convertTime(obj.tuesday_from);
							obj.tuesday_to=ZouponsWebService.getValue(mNodeElement,"tuesday_to");
							obj.tuesday_to=obj.tuesday_to.equals(null)?mEmptyValue:convertTime(obj.tuesday_to);
							obj.wednesday_from=ZouponsWebService.getValue(mNodeElement,"wednesday_from");
							obj.wednesday_from=obj.wednesday_from.equals(null)?mEmptyValue:convertTime(obj.wednesday_from);
							obj.wednesday_to=ZouponsWebService.getValue(mNodeElement,"wednesday_to");
							obj.wednesday_to=obj.wednesday_to.equals(null)?mEmptyValue:convertTime(obj.wednesday_to);
							obj.thursday_from=ZouponsWebService.getValue(mNodeElement,"thursday_from");
							obj.thursday_from=obj.thursday_from.equals(null)?mEmptyValue:convertTime(obj.thursday_from);
							obj.thursday_to=ZouponsWebService.getValue(mNodeElement,"thursday_to");
							obj.thursday_to=obj.thursday_to.equals(null)?mEmptyValue:convertTime(obj.thursday_to);
							obj.friday_from=ZouponsWebService.getValue(mNodeElement,"friday_from");
							obj.friday_from=obj.friday_from.equals(null)?mEmptyValue:convertTime(obj.friday_from);
							obj.friday_to=ZouponsWebService.getValue(mNodeElement,"friday_to");
							obj.friday_to=obj.friday_to.equals(null)?mEmptyValue:convertTime(obj.friday_to);
							obj.saturday_from=ZouponsWebService.getValue(mNodeElement,"saturday_from");
							obj.saturday_from=obj.saturday_from.equals(null)?mEmptyValue:convertTime(obj.saturday_from);
							obj.saturday_to=ZouponsWebService.getValue(mNodeElement,"saturday_to");
							obj.saturday_to=obj.saturday_to.equals(null)?mEmptyValue:convertTime(obj.saturday_to);
							obj.sunday_from=ZouponsWebService.getValue(mNodeElement,"sunday_from");
							obj.sunday_from=obj.sunday_from.equals(null)?mEmptyValue:convertTime(obj.sunday_from);						
							obj.sunday_to=ZouponsWebService.getValue(mNodeElement,"sunday_to");
							obj.sunday_to=obj.sunday_to.equals(null)?mEmptyValue:convertTime(obj.sunday_to);
							obj.is_monday_closed=ZouponsWebService.getValue(mNodeElement,"is_monday_closed");
							obj.is_monday_closed=obj.is_monday_closed.equals(null)?mEmptyValue:obj.is_monday_closed;
							obj.is_tuesday_closed=ZouponsWebService.getValue(mNodeElement,"is_tuesday_closed");
							obj.is_tuesday_closed=obj.is_tuesday_closed.equals(null)?mEmptyValue:obj.is_tuesday_closed;
							obj.is_wednesday_closed=ZouponsWebService.getValue(mNodeElement,"is_wednesday_closed");
							obj.is_wednesday_closed=obj.is_wednesday_closed.equals(null)?mEmptyValue:obj.is_wednesday_closed;
							obj.is_thursday_closed=ZouponsWebService.getValue(mNodeElement,"is_thursday_closed");
							obj.is_thursday_closed=obj.is_thursday_closed.equals(null)?mEmptyValue:obj.is_thursday_closed;
							obj.is_friday_closed=ZouponsWebService.getValue(mNodeElement,"is_friday_closed");
							obj.is_friday_closed=obj.is_friday_closed.equals(null)?mEmptyValue:obj.is_friday_closed;
							obj.is_saturday_closed=ZouponsWebService.getValue(mNodeElement,"is_saturday_closed");
							obj.is_saturday_closed=obj.is_saturday_closed.equals(null)?mEmptyValue:obj.is_saturday_closed;
							obj.is_sunday_closed=ZouponsWebService.getValue(mNodeElement,"is_sunday_closed");
							obj.is_sunday_closed=obj.is_sunday_closed.equals(null)?mEmptyValue:obj.is_sunday_closed;
							WebServiceStaticArrays.mStoreTiming.add(obj);	    			 
						}
					}
				}else{
					returnvalue="norecords";
				}
			}
		}catch (Exception e) {

			returnvalue="failure";
		}
		return returnvalue;
	}

	//  To parse freind list
	public String parseFriendList(String response){
		String returnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response.toString());
			mNodes = mDoc.getElementsByTagName("email_ids");
			mChildNodes = mDoc.getElementsByTagName("email_id");
			mRecordSize = zouponswebservice.numResults(mDoc);
			if(mRecordSize<=0){
				returnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					if(mNodes.getLength() == 1 && mChildNodes.getLength()==0){
						returnValue="norecords";
					}else{
						returnValue="success";
						mParentElement = (Element) mNodes.item(0);
						String timezone = ZouponsWebService.getValue(mParentElement, "time_zone");
						timezone=timezone.equals(null)?mEmptyValue:timezone;
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								for(int j=0;j<mChildNodes.getLength();j++){	
									POJOFBfriendListData mFriendData = (POJOFBfriendListData) WebServiceStaticArrays.mSocialNetworkFriendList.get(j);
									mChildElement = (Element) mChildNodes.item(j);
									mFriendData.friend_id = ZouponsWebService.getValue(mChildElement,"user_id");
									mFriendData.friend_id=mFriendData.friend_id.equals(null)?mEmptyValue:mFriendData.friend_id;
									mFriendData.zouponsfriend = ZouponsWebService.getValue(mChildElement,"status");
									mFriendData.zouponsfriend=mFriendData.zouponsfriend.equals(null)?mEmptyValue:mFriendData.zouponsfriend;
									mFriendData.timezone = timezone;
									WebServiceStaticArrays.mSocialNetworkFriendList.set(j, mFriendData);
								}
							}
						}
					}
				}else{
					returnValue="norecords";
				}
			}
		}catch (Exception e) {
			returnValue="failure";
			e.printStackTrace();
		}
		return returnValue;
	}

	/*Function to parse store video response*/
	public String mParseVideoResponse(String mVideoResponse) {
		// Parsing the Store Video Response
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(mVideoResponse);
			mNodes = mDoc.getElementsByTagName("video");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					WebServiceStaticArrays.mVideoList.clear();
					for(int i=0;i<mNodes.getLength();i++){		        		   
						POJOVideoURL obj = new POJOVideoURL();
						mNodeElement = (Element) mNodes.item(i);		        		
						obj.Message=ZouponsWebService.getValue(mNodeElement,"message");
						obj.Message=obj.Message.equals(null)?mEmptyValue:obj.Message;		        			
						obj.VideoId = ZouponsWebService.getValue(mNodeElement,"id");
						obj.VideoId=obj.VideoId.equals(null)?mEmptyValue:obj.VideoId;	
						obj.VideoTitle = ZouponsWebService.getValue(mNodeElement,"title");
						obj.VideoTitle=obj.VideoTitle.equals(null)?mEmptyValue:obj.VideoTitle;
						obj.VideoURL = ZouponsWebService.getValue(mNodeElement,"url");
						obj.VideoURL=obj.VideoURL.equals(null)?mEmptyValue:obj.VideoURL;
						obj.VideoThumbNail = ZouponsWebService.getValue(mNodeElement,"thumbnail");
						obj.VideoThumbNail=obj.VideoThumbNail.equals(null)?mEmptyValue:obj.VideoThumbNail;
						WebServiceStaticArrays.mVideoList.add(obj);
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}

		} catch (Exception e) {

			rtnValue="failure";
		}

		return rtnValue;
	}

	// To parse share store response
	public String parseShareStore(String mResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mResponse);
			mNodes = mDoc.getElementsByTagName("share_store");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					WebServiceStaticArrays.mShareStore.clear();
					for(int i=0;i<mNodes.getLength();i++){
						ShareStore_ClassVariables obj = new ShareStore_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mSubject=ZouponsWebService.getValue(mNodeElement,"subject");
						obj.mSubject=obj.mSubject.equals(null)?mEmptyValue:obj.mSubject;		        			
						obj.mStoreLink=ZouponsWebService.getValue(mNodeElement,"store_link");
						obj.mStoreLink=obj.mStoreLink.equals(null)?mEmptyValue:obj.mStoreLink;
						obj.mEmailTemplate=ZouponsWebService.getValue(mNodeElement,"email_template");
						obj.mEmailTemplate=obj.mEmailTemplate.equals(null)?mEmptyValue:obj.mEmailTemplate;
						obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						obj.mFbCount=ZouponsWebService.getValue(mNodeElement,"fb_count");
						obj.mFbCount=obj.mFbCount.equals(null)?mEmptyValue:obj.mFbCount;
						if(!obj.mStoreLink.equals("")){
							obj.mEmailTemplate = obj.mEmailTemplate.replace("{store_link}", obj.mStoreLink);
						}
						WebServiceStaticArrays.mShareStore.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch (Exception e) {

			rtnValue="failure";
		}
		return rtnValue;
	}

	// To parse share store link response
	public String parseShareStoreLinkResponse(String mResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mResponse);
			mNodes = mDoc.getElementsByTagName("share_location");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						rtnValue=ZouponsWebService.getValue(mNodeElement,"share_link");
						rtnValue=rtnValue.equals(null)?mEmptyValue:rtnValue;		        			
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch (Exception e) {

			rtnValue="failure";
		}
		return rtnValue;
	}

	// To parse share coupon response
	public String parseShareCoupon(String mResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mResponse);
			mNodes = mDoc.getElementsByTagName("share_coupon");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mShareCoupon.clear();
					for(int i=0;i<mNodes.getLength();i++){
						ShareCoupon_ClassVariables obj = new ShareCoupon_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mSubject=ZouponsWebService.getValue(mNodeElement,"subject");
						obj.mSubject=obj.mSubject.equals(null)?mEmptyValue:obj.mSubject;
						obj.mCouponCode=ZouponsWebService.getValue(mNodeElement,"coupon_code");
						obj.mCouponCode=obj.mCouponCode.equals(null)?mEmptyValue:obj.mCouponCode;
						obj.mCouponLink=ZouponsWebService.getValue(mNodeElement,"coupon_link");
						obj.mCouponLink=obj.mCouponLink.equals(null)?mEmptyValue:obj.mCouponLink;
						obj.mEmailTemplate=ZouponsWebService.getValue(mNodeElement,"email_template");
						obj.mEmailTemplate=obj.mEmailTemplate.equals(null)?mEmptyValue:obj.mEmailTemplate;
						obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						if(!obj.mCouponCode.equals("")){
							obj.mEmailTemplate = obj.mEmailTemplate.replace("{coupon_code}", obj.mCouponCode);
						}
						if(!obj.mCouponLink.equals("")){
							obj.mEmailTemplate = obj.mEmailTemplate.replace("{store_link}", obj.mCouponLink);
						}
						WebServiceStaticArrays.mShareCoupon.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch (Exception e) {
			rtnValue="failure";
		}
		return rtnValue;
	}

	// To parse facebook login response
	public String parseFbLogin(String response){
		String returnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("validateuser");						//function to get value by its validateuser tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				returnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					mChildElement = (Element) mNodes.item(0);
					String message="";
					message = ZouponsWebService.getValue(mChildElement,"message");
					message=message.equals(null)?mEmptyValue:message;
					returnValue=message;
				}else{
					returnValue="norecords";
				}
			}
		}catch (Exception e) {
			returnValue="Failure";
		}
		return returnValue;
	}

	// To parse store coupon list response 
	public String mParseCouponResponse(String mCouponResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{		
			mDoc = zouponswebservice.XMLfromString(mCouponResponse);
			mNodes = mDoc.getElementsByTagName("coupons");						
			mChildNodes = mDoc.getElementsByTagName("coupon");
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					mParentElement = (Element) mNodes.item(0);
					String totalcount = ZouponsWebService.getValue(mParentElement, "total_count");
					totalcount = totalcount.equals(null) || totalcount.equals("")?mIntegerEmptyValue:totalcount;
					MainMenuActivity.mCouponTotalCount = totalcount; 
					StoreOwnerCoupons.sCouponTotalCount = totalcount;
					for(int i=0;i<mNodes.getLength();i++){
						if(mChildNodes != null){			    			
							//WebServiceStaticArrays.mStaticCouponsArrayList.clear();
							for(int j=0;j<mChildNodes.getLength();j++){	
								POJOCouponsList obj = new POJOCouponsList();
								mChildElement = (Element) mChildNodes.item(j);
								obj.mCouponTotalCount=totalcount;
								obj.mCouponId = ZouponsWebService.getValue(mChildElement,"id");
								obj.mCouponId=obj.mCouponId.equals(null)?mEmptyValue:obj.mCouponId;
								obj.mCouponTitle = ZouponsWebService.getValue(mChildElement,"title");
								obj.mCouponTitle=obj.mCouponTitle.equals(null)?mEmptyValue:obj.mCouponTitle;
								obj.mCouponType = ZouponsWebService.getValue(mChildElement,"type");
								obj.mCouponType=obj.mCouponType.equals(null)?mEmptyValue:obj.mCouponType;
								obj.mCouponCode = ZouponsWebService.getValue(mChildElement,"coupon_code");
								obj.mCouponCode=obj.mCouponCode.equals(null)?mEmptyValue:obj.mCouponCode;
								obj.mCouponImage = ZouponsWebService.getValue(mChildElement,"image");
								obj.mCouponImage=obj.mCouponImage.equals(null)?mEmptyValue:obj.mCouponImage;
								obj.mCouponDescription = ZouponsWebService.getValue(mChildElement,"description");
								obj.mCouponDescription=obj.mCouponDescription.equals(null)?mEmptyValue:obj.mCouponDescription;
								obj.mCouponExpires = ZouponsWebService.getValue(mChildElement,"expiration_date");
								obj.mCouponExpires = obj.mCouponExpires.equals(null)?mEmptyValue:obj.mCouponExpires;
								if(obj.mCouponExpires!=null && !obj.mCouponExpires.equals("")){
									String[] datesplit = obj.mCouponExpires.split("-");
									obj.mCouponExpires=datesplit[1]+"/"+datesplit[2]+"/"+datesplit[0];  // mm/dd/yyyy
								}
								obj.mCouponFavorite = ZouponsWebService.getValue(mChildElement,"favourite_coupon");
								obj.mCouponFavorite = obj.mCouponFavorite.equals(null)?mEmptyValue:obj.mCouponFavorite;
								//Log.i(TAG,"Coupon Favorite : "+obj.mCouponFavorite);
								obj.mCouponStatus = ZouponsWebService.getValue(mChildElement,"status");
								obj.mCouponStatus = obj.mCouponStatus.equals(null)?mEmptyValue:obj.mCouponStatus;
								obj.mCouponCustomerName = ZouponsWebService.getValue(mChildElement,"user_name");
								obj.mCouponCustomerName = obj.mCouponCustomerName.equals(null)?mEmptyValue:obj.mCouponCustomerName;
								obj.mCouponBarCodeImage = ZouponsWebService.getValue(mChildElement,"bar_code");
								obj.mCouponBarCodeImage = obj.mCouponBarCodeImage.equals(null)?mEmptyValue:obj.mCouponBarCodeImage;
								obj.mCustomerPhone = ZouponsWebService.getValue(mChildElement,"phone");
								obj.mCustomerPhone = obj.mCustomerPhone.equals(null)?mEmptyValue:obj.mCustomerPhone;
								obj.mActivationDate = ZouponsWebService.getValue(mChildElement,"activation_date");
								obj.mActivationDate = obj.mActivationDate.equals(null)?mEmptyValue:obj.mActivationDate;
								obj.mOneTimeUse = ZouponsWebService.getValue(mChildElement,"one_time_use");
								obj.mOneTimeUse = obj.mOneTimeUse.equals(null)?mEmptyValue:obj.mOneTimeUse;
								obj.mPrivateCustomerId = ZouponsWebService.getValue(mChildElement,"user_id");
								obj.mPrivateCustomerId = obj.mPrivateCustomerId.equals(null)?mEmptyValue:obj.mPrivateCustomerId;
								WebServiceStaticArrays.mStaticCouponsArrayList.add(obj);
							}
						}
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch (Exception e) {
			rtnValue="failure";
		}
		return rtnValue;
	}

	// To parse favorite coupon response
	public String mParseCheckIsFavoriteCoupon(String mCheckCouponFavoriteResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(mCheckCouponFavoriteResponse);
			mNodes = mDoc.getElementsByTagName("user_favourites");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					//Log.i(TAG,"Node Length : "+mNodes.getLength());		    		
					for(int i=0;i<mNodes.getLength();i++){		        		   
						mNodeElement = (Element) mNodes.item(i);		        		
						//Log.i(TAG, "TAG NAME: "+mNodeElement.getTagName());			        		 
						CouponDetail.mIsFavoriteCoupons=ZouponsWebService.getValue(mNodeElement,"message");
						CouponDetail.mIsFavoriteCoupons = CouponDetail.mIsFavoriteCoupons.equals(null)?mEmptyValue:CouponDetail.mIsFavoriteCoupons;		        			
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}

		} catch (Exception e) {
			rtnValue="failure";
		}

		return rtnValue;
	}

	// To parse add favorite coupon response
	public String mParseAddFavoriteCoupon(String mAddCouponFavoriteResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(mAddCouponFavoriteResponse);
			mNodes = mDoc.getElementsByTagName("user_favourites");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					//Log.i(TAG,"Node Length : "+mNodes.getLength());		    		
					for(int i=0;i<mNodes.getLength();i++){		        		   
						mNodeElement = (Element) mNodes.item(i);		        		
						//Log.i(TAG, "TAG NAME: "+mNodeElement.getTagName());			        		 
						CouponDetail.mIsCouponAddedAsFavotire=ZouponsWebService.getValue(mNodeElement,"message");
						CouponDetail.mIsCouponAddedAsFavotire=CouponDetail.mIsCouponAddedAsFavotire.equals(null)?mEmptyValue:CouponDetail.mIsCouponAddedAsFavotire;		        			
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}

		} catch (Exception e) {
			rtnValue="failure";
		}

		return rtnValue;
	}

	//function for parsing fav_coupons categories webservice data
	public String parseFavourite_Coupons(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mParentNode = mDoc.getElementsByTagName("fav_coupons");
			mNodes = mDoc.getElementsByTagName("fav_coupon");						//function to get value by its fav_coupons tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					if(mParentNode.getLength() > 0){
						Element totalcountElement = (Element) mParentNode.item(0);
						MainMenuActivity.mCouponTotalCount = ZouponsWebService.getValue(totalcountElement,"total_count");
						MainMenuActivity.mCouponTotalCount  = MainMenuActivity.mCouponTotalCount.equals(null)|| MainMenuActivity.mCouponTotalCount.equals("")?"0":MainMenuActivity.mCouponTotalCount ;
						//Log.i("total count", MainMenuActivity.mCouponTotalCount);
					}
					for(int i=0;i<mNodes.getLength();i++){

						POJOCouponsList obj = new POJOCouponsList();
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()>1){
							obj.mFavorite_CouponId = ZouponsWebService.getValue(mNodeElement,"id");
							obj.mFavorite_CouponId =obj.mFavorite_CouponId .equals(null)?mEmptyValue:obj.mFavorite_CouponId;
							obj.mFavorite_StoreId = ZouponsWebService.getValue(mNodeElement,"store_id");
							obj.mFavorite_StoreId=obj.mFavorite_StoreId.equals(null)?mEmptyValue:obj.mFavorite_StoreId;
							obj.mFavorite_StoreName = ZouponsWebService.getValue(mNodeElement,"store_name");
							obj.mFavorite_StoreName=obj.mFavorite_StoreName.equals(null)?mEmptyValue:obj.mFavorite_StoreName;
							obj.mFavorite_CouponTitle = ZouponsWebService.getValue(mNodeElement,"title");
							obj.mFavorite_CouponTitle=obj.mFavorite_CouponTitle.equals(null)?mEmptyValue:obj.mFavorite_CouponTitle;
							obj.mFavorite_CouponType = ZouponsWebService.getValue(mNodeElement,"type");
							obj.mFavorite_CouponType=obj.mFavorite_CouponType.equals(null)?mEmptyValue:obj.mFavorite_CouponType;
							obj.mFavorite_CouponCode = ZouponsWebService.getValue(mNodeElement,"coupon_code");
							obj.mFavorite_CouponCode=obj.mFavorite_CouponCode.equals(null)?mEmptyValue:obj.mFavorite_CouponCode;
							obj.mFavorite_CouponImage = ZouponsWebService.getValue(mNodeElement,"image");
							obj.mFavorite_CouponImage=obj.mFavorite_CouponImage.equals(null)?mEmptyValue:obj.mFavorite_CouponImage;
							obj.mFavorite_CouponDescription = ZouponsWebService.getValue(mNodeElement,"description");
							obj.mFavorite_CouponDescription=obj.mFavorite_CouponDescription.equals(null)?mEmptyValue:obj.mFavorite_CouponDescription;
							obj.mFavorite_CouponExpires = ZouponsWebService.getValue(mNodeElement,"expiration_date");
							obj.mFavorite_CouponExpires=obj.mFavorite_CouponExpires.equals(null)?mEmptyValue:obj.mFavorite_CouponExpires;
							obj.mFavorite_CouponBarCodeImage=ZouponsWebService.getValue(mNodeElement,"barcode");
							obj.mFavorite_CouponBarCodeImage= obj.mFavorite_CouponBarCodeImage.equals(null)?mEmptyValue:obj.mFavorite_CouponBarCodeImage;
							WebServiceStaticArrays.mStaticCouponsArrayList.add(obj);
						}else if(mNodeElement.getChildNodes().getLength()==1){
							obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
							obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
							WebServiceStaticArrays.mStaticCouponsArrayList.add(obj);
						}
					}
					if(mNodes.getLength()>0){
						rtnValue="success";
					}else{
						mNodes = mDoc.getElementsByTagName("fav_coupons");						//function to get value by its fav_coupons tag name
						if(mNodes!=null&&mNodes.getLength()>0){
							for(int i=0;i<mNodes.getLength();i++){
								POJOCouponsList obj = new POJOCouponsList();
								mNodeElement = (Element) mNodes.item(i);
								obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
								obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
								WebServiceStaticArrays.mStaticCouponsArrayList.add(obj);
							}
							rtnValue="success";
						}else{
							rtnValue="norecords";
						}
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//To parse favouite store details 
	public String parseFavouriteStores(String response, String type) {
		String returnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			if(type.equalsIgnoreCase("FriendFavouriteStore")){
				mNodes = mDoc.getElementsByTagName("friends_fav_stores");
				mChildNodes = mDoc.getElementsByTagName("fav_store");
			}else{
				mNodes = mDoc.getElementsByTagName("fav_stores");
				mChildNodes = mDoc.getElementsByTagName("fav_store");
			}
			mRecordSize = zouponswebservice.numResults(mDoc);					  
			if(mRecordSize<=0){
				returnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						returnValue="norecords";
					}else{
						returnValue="success";
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								WebServiceStaticArrays.mOffsetFavouriteStores.clear();
								Element totalcountElement = (Element) mNodes.item(i);
								MainMenuActivity.mFavouritesTotalCount = ZouponsWebService.getValue(totalcountElement,"total_count");
								MainMenuActivity.mFavouritesTotalCount  = MainMenuActivity.mFavouritesTotalCount .equals(null)|| MainMenuActivity.mFavouritesTotalCount .equals("")?"0":MainMenuActivity.mFavouritesTotalCount ;
								for(int j=0;j<mChildNodes.getLength();j++){	
									POJOStoreInfo mFavouriteStoreInfo = new POJOStoreInfo();
									mChildElement = (Element) mChildNodes.item(j);
									mFavouriteStoreInfo.store_name = ZouponsWebService.getValue(mChildElement,"store_name");
									mFavouriteStoreInfo.store_name=mFavouriteStoreInfo.store_name.equals(null)?mEmptyValue:mFavouriteStoreInfo.store_name;
									mFavouriteStoreInfo.store_id = ZouponsWebService.getValue(mChildElement,"id");
									mFavouriteStoreInfo.store_id=mFavouriteStoreInfo.store_id.equals(null)?mEmptyValue:mFavouriteStoreInfo.store_id;
									mFavouriteStoreInfo.location_id = ZouponsWebService.getValue(mChildElement,"location_id");
									mFavouriteStoreInfo.location_id=mFavouriteStoreInfo.location_id.equals(null)?mEmptyValue:mFavouriteStoreInfo.location_id;
									mFavouriteStoreInfo.address_line1 = ZouponsWebService.getValue(mChildElement,"address_line1");
									mFavouriteStoreInfo.address_line1=mFavouriteStoreInfo.address_line1.equals(null)?mEmptyValue:mFavouriteStoreInfo.address_line1;
									mFavouriteStoreInfo.address_line2 = ZouponsWebService.getValue(mChildElement,"address_line2");
									mFavouriteStoreInfo.address_line2=mFavouriteStoreInfo.address_line2.equals(null)?mEmptyValue:mFavouriteStoreInfo.address_line2;
									mFavouriteStoreInfo.city = ZouponsWebService.getValue(mChildElement,"city");
									mFavouriteStoreInfo.city=mFavouriteStoreInfo.city.equals(null)?mEmptyValue:mFavouriteStoreInfo.city;
									mFavouriteStoreInfo.state = ZouponsWebService.getValue(mChildElement,"state");
									mFavouriteStoreInfo.state=mFavouriteStoreInfo.state.equals(null)?mEmptyValue:mFavouriteStoreInfo.state;
									mFavouriteStoreInfo.zip_code = ZouponsWebService.getValue(mChildElement,"zip_code");
									mFavouriteStoreInfo.zip_code=mFavouriteStoreInfo.zip_code.equals(null)?mEmptyValue:mFavouriteStoreInfo.zip_code;
									// changes
									mFavouriteStoreInfo.mLatitude = ZouponsWebService.getValue(mChildElement,"lat");
									mFavouriteStoreInfo.mLatitude=mFavouriteStoreInfo.mLatitude.equals(null)?mEmptyValue:mFavouriteStoreInfo.mLatitude;
									mFavouriteStoreInfo.mLongitude = ZouponsWebService.getValue(mChildElement,"lon");
									mFavouriteStoreInfo.mLongitude=mFavouriteStoreInfo.mLongitude.equals(null)?mEmptyValue:mFavouriteStoreInfo.mLongitude;
									mFavouriteStoreInfo.store_subcategory = ZouponsWebService.getValue(mChildElement,"subcategory");
									mFavouriteStoreInfo.store_subcategory=mFavouriteStoreInfo.store_subcategory.equals(null)?mEmptyValue:mFavouriteStoreInfo.store_subcategory;
									mFavouriteStoreInfo.store_category = ZouponsWebService.getValue(mChildElement,"category");
									mFavouriteStoreInfo.store_category=mFavouriteStoreInfo.store_category.equals(null)?mEmptyValue:mFavouriteStoreInfo.store_category;
									mFavouriteStoreInfo.store_distance = ZouponsWebService.getValue(mChildElement,"distance");
									mFavouriteStoreInfo.store_distance=mFavouriteStoreInfo.store_distance.equals(null)?mIntegerEmptyValue:mFavouriteStoreInfo.store_distance;
									mFavouriteStoreInfo.has_location = ZouponsWebService.getValue(mChildElement,"location");
									mFavouriteStoreInfo.has_location=mFavouriteStoreInfo.has_location.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_location;
									mFavouriteStoreInfo.has_coupons = ZouponsWebService.getValue(mChildElement,"coupons");
									mFavouriteStoreInfo.has_coupons=mFavouriteStoreInfo.has_coupons.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_coupons;
									mFavouriteStoreInfo.has_about_us = ZouponsWebService.getValue(mChildElement,"about_us");
									mFavouriteStoreInfo.has_about_us=mFavouriteStoreInfo.has_about_us.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_about_us;
									mFavouriteStoreInfo.has_review_and_rating = ZouponsWebService.getValue(mChildElement,"review_and_rating");
									mFavouriteStoreInfo.has_review_and_rating=mFavouriteStoreInfo.has_review_and_rating.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_review_and_rating;
									mFavouriteStoreInfo.has_photos = ZouponsWebService.getValue(mChildElement,"photos");
									mFavouriteStoreInfo.has_photos=mFavouriteStoreInfo.has_photos.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_photos;
									mFavouriteStoreInfo.has_contact_store = ZouponsWebService.getValue(mChildElement,"contact_store");
									mFavouriteStoreInfo.has_contact_store=mFavouriteStoreInfo.has_contact_store.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_contact_store;
									mFavouriteStoreInfo.has_giftcard = ZouponsWebService.getValue(mChildElement,"gift_cards");
									mFavouriteStoreInfo.has_giftcard=mFavouriteStoreInfo.has_giftcard.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_giftcard;
									mFavouriteStoreInfo.has_videos = ZouponsWebService.getValue(mChildElement,"videos");
									mFavouriteStoreInfo.has_videos=mFavouriteStoreInfo.has_videos.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_videos;
									mFavouriteStoreInfo.has_invoicecenter = ZouponsWebService.getValue(mChildElement,"Invoice_center");
									mFavouriteStoreInfo.has_invoicecenter = mFavouriteStoreInfo.has_invoicecenter.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_invoicecenter;
									mFavouriteStoreInfo.has_communication = ZouponsWebService.getValue(mChildElement,"communication");
									mFavouriteStoreInfo.has_communication = mFavouriteStoreInfo.has_communication.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_communication;
									mFavouriteStoreInfo.has_point_of_sale = ZouponsWebService.getValue(mChildElement,"point_of_sale");
									mFavouriteStoreInfo.has_point_of_sale = mFavouriteStoreInfo.has_point_of_sale.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_point_of_sale;
									mFavouriteStoreInfo.has_zcards = ZouponsWebService.getValue(mChildElement,"deal_cards");
									mFavouriteStoreInfo.has_zcards = mFavouriteStoreInfo.has_zcards.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_zcards;
									mFavouriteStoreInfo.store_image_path = ZouponsWebService.getValue(mChildElement,"logo_path");
									mFavouriteStoreInfo.store_image_path = mFavouriteStoreInfo.store_image_path.equals(null)?mEmptyValue:mFavouriteStoreInfo.store_image_path;
									if(mFavouriteStoreInfo.store_image_path.contains("")){
										mFavouriteStoreInfo.store_image_path = mFavouriteStoreInfo.store_image_path.replaceAll("\\s","%20");
									}
									mFavouriteStoreInfo.has_share=ZouponsWebService.getValue(mChildElement,"share");
									mFavouriteStoreInfo.has_share=mFavouriteStoreInfo.has_share.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_share;
									mFavouriteStoreInfo.has_batchsales=ZouponsWebService.getValue(mChildElement,"batch_sales");
									mFavouriteStoreInfo.has_batchsales=mFavouriteStoreInfo.has_batchsales.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_batchsales;
									mFavouriteStoreInfo.has_refund=ZouponsWebService.getValue(mChildElement,"refund");
									mFavouriteStoreInfo.has_refund=mFavouriteStoreInfo.has_refund.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_refund;
									mFavouriteStoreInfo.has_mobilepayment=ZouponsWebService.getValue(mChildElement,"mobile_payment");
									mFavouriteStoreInfo.has_mobilepayment=mFavouriteStoreInfo.has_mobilepayment.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_mobilepayment;
									mFavouriteStoreInfo.has_rewardcards=ZouponsWebService.getValue(mChildElement,"reward_cards");
									mFavouriteStoreInfo.has_rewardcards=mFavouriteStoreInfo.has_rewardcards.equals(null)?mEmptyValue:mFavouriteStoreInfo.has_rewardcards;
									// for like count
									mFavouriteStoreInfo.like_count = ZouponsWebService.getValue(mChildElement,"likes");
									mFavouriteStoreInfo.like_count = mFavouriteStoreInfo.like_count.equals(null)||mFavouriteStoreInfo.like_count.equals("")?mIntegerEmptyValue:mFavouriteStoreInfo.like_count;
									mFavouriteStoreInfo.favorite_store = ZouponsWebService.getValue(mChildElement,"favorite_store");
									mFavouriteStoreInfo.favorite_store = mFavouriteStoreInfo.favorite_store.equals(null)?mEmptyValue:mFavouriteStoreInfo.favorite_store;
									//Log.i(TAG,"Favorite Store : "+mFavouriteStoreInfo.favorite_store);
									if(!(mFavouriteStoreInfo.mLatitude.equals("")&&mFavouriteStoreInfo.mLongitude.equals(""))){
										mFavouriteStoreInfo.mStoreCoordinates=new LatLng(Double.parseDouble(mFavouriteStoreInfo.mLatitude),Double.parseDouble(mFavouriteStoreInfo.mLongitude));
									}
									//These flag is add for list sorting when add or remove favorite in right navigation
									if(mFavouriteStoreInfo.store_distance.equalsIgnoreCase("-1")){
										mFavouriteStoreInfo.storesortflag=3;	//Success if online
										mFavouriteStoreInfo.like_count="0";
									}else{
										if(mFavouriteStoreInfo.has_point_of_sale.equalsIgnoreCase("Yes")){
											mFavouriteStoreInfo.storesortflag=1;	//Success if Retail Preffered
										}else {
											mFavouriteStoreInfo.storesortflag=2;	//Success if Retail
											mFavouriteStoreInfo.like_count="0";
										}
									}
									WebServiceStaticArrays.mOffsetFavouriteStores.add(mFavouriteStoreInfo);
								}
							}
						}
					}	

				}else{
					returnValue="norecords";
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			returnValue="failure";
		}
		return returnValue;	
	}

	// To parse notification categories response 
	public String parseNotificationCategories(String response) {
		String returnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("categories");
			mChildNodes = mDoc.getElementsByTagName("category");
			mRecordSize = zouponswebservice.numResults(mDoc);					  
			if(mRecordSize<=0){
				returnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						returnValue="norecords";
					}else{
						returnValue="success";
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								WebServiceStaticArrays.categories.clear();
								for(int j=0;j<mChildNodes.getLength();j++){
									mChildElement = (Element) mChildNodes.item(j);
									String mCategoryId="",mCategoryName="";
									mCategoryId=ZouponsWebService.getValue(mChildElement,"id");
									mCategoryId=mCategoryId.equals(null)?mEmptyValue:mCategoryId;
									mCategoryName=ZouponsWebService.getValue(mChildElement,"category_name");
									mCategoryName=mCategoryName.equals(null)?mEmptyValue:mCategoryName;
									WebServiceStaticArrays.categories.put(mCategoryId, mCategoryName);
								}
							}
						}
					}	
				}else{
					returnValue="norecords";
				}
			}
		}catch (Exception e) {
			returnValue="failure";
		}
		return returnValue;	
	}

	// To parse notification response
	public String parseNotificationDetails(String response) {
		String returnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("notifications");
			mChildNodes = mDoc.getElementsByTagName("notification");
			mRecordSize = zouponswebservice.numResults(mDoc);					  
			if(mRecordSize<=0){
				returnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						returnValue="norecords";
					}else{
						returnValue="success";
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								WebServiceStaticArrays.mNotificationDetails.clear();
								for(int j=0;j<mChildNodes.getLength();j++){	
									POJONotificationDetails mNotificationDetails = new POJONotificationDetails();
									mChildElement = (Element) mChildNodes.item(j);
									mNotificationDetails.notify_by = ZouponsWebService.getValue(mChildElement,"notify_by");
									mNotificationDetails.notify_by=mNotificationDetails.notify_by.equals(null)?mEmptyValue:mNotificationDetails.notify_by;
									mNotificationDetails.contact_frequency = ZouponsWebService.getValue(mChildElement,"contact_frequency");
									mNotificationDetails.contact_frequency=mNotificationDetails.contact_frequency.equals(null)?mEmptyValue:mNotificationDetails.contact_frequency;
									mNotificationDetails.categories = ZouponsWebService.getValue(mChildElement,"category");
									mNotificationDetails.categories=mNotificationDetails.categories.equals(null)?mEmptyValue:mNotificationDetails.categories;
									WebServiceStaticArrays.mNotificationDetails.add(mNotificationDetails);
								}
							}
						}
					}	
				}else{
					returnValue="norecords";
				}
			}
		}catch (Exception e) {
			returnValue="failure";
		}
		return returnValue;	
	}

	public String parseSetNotificationDetails(String response) {
		String returnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("notifications");
			mRecordSize = zouponswebservice.numResults(mDoc);					  
			if(mRecordSize<=0){
				returnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());

					for(int i=0;i<mNodes.getLength();i++){
						mChildElement = (Element) mNodes.item(i);
						String mStatusValue = ZouponsWebService.getValue(mChildElement,"message");
						if(mStatusValue !=null &&( mStatusValue.equalsIgnoreCase("Updated") || mStatusValue.equalsIgnoreCase("Added")) ){
							returnValue="success";				
						}else{
							returnValue="failure";
						}
					}
				}else{
					returnValue="norecords";
				}
			}
		}catch (Exception e) {
			returnValue="failure";
		}
		return returnValue;	
	}

	// To parse user profile detailss
	public String mParseUserProfile(String mGetUserProfileResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetUserProfileResponse);						
			mNodes = mDoc.getElementsByTagName("userprofile");			
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mNodes!=null&&mNodes.getLength()>0){
				//Log.i(TAG,"Node Length : "+mNodes.getLength());
				WebServiceStaticArrays.mUserProfileList.clear();
				if(mNodes.getLength()>0){
					for(int i=0;i<mNodes.getLength();i++){
						POJOUserProfile obj = new POJOUserProfile();
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()==1){	        				
							obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
							obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
							//Log.i(TAG,"obj.message : "+obj.mMessage);
							WebServiceStaticArrays.mUserProfileList.add(obj);
						}else if(mNodeElement.getChildNodes().getLength()>1){	        				
							obj.mUserId=ZouponsWebService.getValue(mNodeElement,"UserId");
							obj.mUserId=obj.mUserId.equals(null)?mEmptyValue:obj.mUserId;
							obj.mUserFirstName=ZouponsWebService.getValue(mNodeElement,"FirstName");
							obj.mUserFirstName=obj.mUserFirstName.equals(null)?mEmptyValue:obj.mUserFirstName;
							obj.mUserLastName=ZouponsWebService.getValue(mNodeElement,"LastName");
							obj.mUserLastName=obj.mUserLastName.equals(null)?mEmptyValue:obj.mUserLastName;
							obj.mUserEmail=ZouponsWebService.getValue(mNodeElement,"Email");
							obj.mUserEmail=obj.mUserEmail.equals(null)?mEmptyValue:obj.mUserEmail;
							obj.mUserMobile=ZouponsWebService.getValue(mNodeElement,"Mobile");
							obj.mUserMobile=obj.mUserMobile.equals(null)?mEmptyValue:obj.mUserMobile;
							obj.mUserMobileCarrier=ZouponsWebService.getValue(mNodeElement,"MobileCarrier");
							obj.mUserMobileCarrier=obj.mUserMobileCarrier.equals(null)?mEmptyValue:obj.mUserMobileCarrier;
							obj.mUserImage=ZouponsWebService.getValue(mNodeElement,"UserImage");
							obj.mUserImage=obj.mUserImage.equals(null)?mEmptyValue:obj.mUserImage;
							Bitmap mSelectedImage = getBitmapFromURL(obj.mUserImage);
							if(mSelectedImage != null){
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								mSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);    
								byte[] imagedata = baos.toByteArray();
								Settings.mProfilePhoto = com.us.zoupons.Base64.encodeBytes(imagedata);	
							}
							if(obj.mUserImage.contains("")){
								obj.mUserImage = obj.mUserImage.replaceAll("\\s","%20");
							}
							WebServiceStaticArrays.mUserProfileList.add(obj);
						}
					}
				}else{
					if(mNodes.getLength() == 0){ // For verification code check while updating user profile
						POJOUserProfile obj = new POJOUserProfile();
						mNodes = mDoc.getElementsByTagName("user");
						mNodeElement = (Element) mNodes.item(0);
						obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						//Log.i(TAG,"obj.message : "+obj.mMessage);
						WebServiceStaticArrays.mUserProfileList.add(obj);
					}
				}
				rtnValue="success";
			}else if(mNodes.getLength() == 0){
				POJOUserProfile obj = new POJOUserProfile();
				mNodes = mDoc.getElementsByTagName("user");
				mNodeElement = (Element) mNodes.item(0);
				obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
				obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
				WebServiceStaticArrays.mUserProfileList.add(obj);
			}else{
				rtnValue="norecords";
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	public String mParseUserSecurityQuestions(String response) {
		String returnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("users");
			mChildNodes = mDoc.getElementsByTagName("security_qa");
			mRecordSize = zouponswebservice.numResults(mDoc);					  
			if(mRecordSize<=0){
				returnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						returnValue="norecords";
					}else{
						returnValue="success";
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								WebServiceStaticArrays.mUserSecurityQuestionsList.clear();
								for(int j=0;j<mChildNodes.getLength();j++){	
									POJOUserSecurityDetails mSecurityDetails = new POJOUserSecurityDetails();
									mChildElement = (Element) mChildNodes.item(j);
									mSecurityDetails.question1 = ZouponsWebService.getValue(mChildElement,"security_question_1");
									mSecurityDetails.question1=mSecurityDetails.question1.equals(null)?mEmptyValue:mSecurityDetails.question1;
									mSecurityDetails.question2 = ZouponsWebService.getValue(mChildElement,"security_question_2");
									mSecurityDetails.question2=mSecurityDetails.question2.equals(null)?mEmptyValue:mSecurityDetails.question2;
									mSecurityDetails.answer1 = ZouponsWebService.getValue(mChildElement,"security_answer_1");
									mSecurityDetails.answer1=mSecurityDetails.answer1.equals(null)?mEmptyValue:mSecurityDetails.answer1;
									mSecurityDetails.answer2 = ZouponsWebService.getValue(mChildElement,"security_answer_2");
									mSecurityDetails.answer2=mSecurityDetails.answer2.equals(null)?mEmptyValue:mSecurityDetails.answer2;
									WebServiceStaticArrays.mUserSecurityQuestionsList.add(mSecurityDetails);
								}
							}
						}
					}	

				}else{
					returnValue="norecords";
				}
			}
		}catch (Exception e) {
			returnValue="failure";
		}
		return returnValue;	
	}

	// Parse Contact Store Response
	public String mParseContactUsResponse(String mGetResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("contact_store");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					POJOContactUsResponse.mContactUsMessage="";
					WebServiceStaticArrays.mContactUsResponseList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						POJOContactUsResponse obj = new POJOContactUsResponse();
						mChildElement = (Element) mNodes.item(i);		        		
						String ResponseMessage=ZouponsWebService.getValue(mChildElement,"message");
						POJOContactUsResponse.mContactUsMessage=ResponseMessage.equals(null)? mEmptyValue:ResponseMessage;
						obj.mContactUsPostedTime = ZouponsWebService.getValue(mChildElement,"posted_time");
						obj.mContactUsPostedTime=obj.mContactUsPostedTime.equals(null)?mEmptyValue:obj.mContactUsPostedTime;
						obj.mContactUsTimeZone = ZouponsWebService.getValue(mChildElement,"time_zone");
						obj.mContactUsTimeZone=obj.mContactUsTimeZone.equals(null)?mEmptyValue:obj.mContactUsTimeZone;
						obj.mContactUsChatMessage = ZouponsWebService.getValue(mChildElement,"chat_message");
						obj.mContactUsChatMessage=obj.mContactUsChatMessage.equals(null)?mEmptyValue:obj.mContactUsChatMessage;
						obj.mResponseId = ZouponsWebService.getValue(mChildElement,"message_id");
						obj.mResponseId=obj.mResponseId.equals(null)?mEmptyValue:obj.mResponseId;
						obj.mUserType = ZouponsWebService.getValue(mChildElement,"user_type");
						obj.mUserType=obj.mUserType.equals(null)?mEmptyValue:obj.mUserType;
						obj.mUserName = ZouponsWebService.getValue(mChildElement,"user_name");
						obj.mUserName=obj.mUserName.equals(null)?mEmptyValue:obj.mUserName;
						try{		        					
							if(!obj.mContactUsPostedTime.equals("")){
								if(obj.mContactUsPostedTime.contains("0000")){
									obj.mContactUsPostedTime=obj.mContactUsPostedTime;
								}else{
									obj.mContactUsPostedTime = convertTimeToDeviceTime(obj.mContactUsPostedTime, obj.mContactUsTimeZone);
								}
							}
						}catch (Exception e) {
							e.printStackTrace();
						}

						WebServiceStaticArrays.mContactUsResponseList.add(obj);
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}
		} catch (Exception e) {
			rtnValue="failure";
		}

		return rtnValue;
	}
	//Parse Talk To Use Response
	public String mParseTalkToUsResponse(String mGetResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("talk_to_us");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					POJOContactUsResponse.mContactUsMessage="";
					WebServiceStaticArrays.mContactUsResponseList.clear();
					for(int i=0;i<mNodes.getLength();i++){
						POJOContactUsResponse obj = new POJOContactUsResponse();
						mChildElement = (Element) mNodes.item(i);		        		
						//Log.i(TAG, "TAG NAME: "+mChildElement.getTagName());	
						String ResponseMessage=ZouponsWebService.getValue(mChildElement,"message");
						POJOContactUsResponse.mContactUsMessage=ResponseMessage.equals(null)? mEmptyValue:ResponseMessage;

						obj.mContactUsPostedTime = ZouponsWebService.getValue(mChildElement,"posted_time");
						obj.mContactUsPostedTime=obj.mContactUsPostedTime.equals(null)?mEmptyValue:obj.mContactUsPostedTime;
						obj.mContactUsTimeZone = ZouponsWebService.getValue(mChildElement,"time_zone");
						obj.mContactUsTimeZone=obj.mContactUsTimeZone.equals(null)?mEmptyValue:obj.mContactUsTimeZone;
						obj.mContactUsChatMessage = ZouponsWebService.getValue(mChildElement,"chat_message");
						obj.mContactUsChatMessage=obj.mContactUsChatMessage.equals(null)?mEmptyValue:obj.mContactUsChatMessage;

						try{		        					
							if(!obj.mContactUsPostedTime.equals("")){
								if(obj.mContactUsPostedTime.contains("0000")){
									obj.mContactUsPostedTime=obj.mContactUsPostedTime;
								}else{
									obj.mContactUsPostedTime = convertTimeToDeviceTime(obj.mContactUsPostedTime, obj.mContactUsTimeZone);
								}
							}
						}catch (Exception e) {
							e.printStackTrace();
						}

						WebServiceStaticArrays.mContactUsResponseList.add(obj);
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}
		} catch (Exception e) {
			rtnValue="failure";
		}

		return rtnValue;
	}
	//Parse Contact Use Response From Web Service
	public String mParseContactUsStoreResponse(String mGetResponse,String flag) {
		String rtnValue="";		
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetResponse);						 
			mNodes = mDoc.getElementsByTagName("contact_stores");				
			mChildNodes = mDoc.getElementsByTagName("contact_store");
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					mParentElement = (Element) mNodes.item(0);
					String totalcount = ZouponsWebService.getValue(mParentElement, "total_count");
					totalcount = totalcount.equals(null)?mEmptyValue:totalcount;
					if(mChildNodes.getLength() == 0){
						rtnValue="norecords";
					}else{
						for(int i=0;i<mNodes.getLength();i++){	        			
							mNodeElement = (Element) mNodes.item(i);	        			
							if(mChildNodes.getLength() != 0){       
								WebServiceStaticArrays.mContactUsResponseResult.clear();
								//WebServiceStaticArrays.mContactUsResponse.clear();
								if(flag.equalsIgnoreCase("scroll"))
									WebServiceStaticArrays.mContactUsResponseDuringScroll = new ArrayList<Object>(WebServiceStaticArrays.mContactUsResponse);
								for(int j=0;j<mChildNodes.getLength();j++){
									POJOContactUsResponse obj = new POJOContactUsResponse();
									mChildElement = (Element) mChildNodes.item(j);
									obj.mTotalCount=totalcount;
									obj.mResponseId=ZouponsWebService.getValue(mChildElement,"id");
									obj.mResponseId=obj.mResponseId.equals(null)?mEmptyValue:obj.mResponseId;

									obj.mFromUserId=ZouponsWebService.getValue(mChildElement,"from_user_id");
									obj.mFromUserId=obj.mFromUserId.equals(null)?mEmptyValue:obj.mFromUserId;
									obj.mFromStoreId=ZouponsWebService.getValue(mChildElement,"from_store_id");
									obj.mFromStoreId=obj.mFromStoreId.equals(null)?mEmptyValue:obj.mFromStoreId;
									obj.mToUserId=ZouponsWebService.getValue(mChildElement,"to_user_id");
									obj.mToUserId=obj.mToUserId.equals(null)?mEmptyValue:obj.mToUserId;
									obj.mToStoreId=ZouponsWebService.getValue(mChildElement,"to_store_id");
									obj.mToStoreId=obj.mToStoreId.equals(null)?mEmptyValue:obj.mToStoreId;

									obj.mTimeZone=ZouponsWebService.getValue(mChildElement,"time_zone");
									obj.mTimeZone=obj.mTimeZone.equals(null)?mEmptyValue:obj.mTimeZone;
									obj.mPostedTime=ZouponsWebService.getValue(mChildElement,"posted_time");
									obj.mPostedTime=obj.mPostedTime.equals(null)?mEmptyValue:obj.mPostedTime;
									obj.mQuery=ZouponsWebService.getValue(mChildElement,"query");
									obj.mQuery=obj.mQuery.equals(null)?mEmptyValue:obj.mQuery;
									obj.mUserType=ZouponsWebService.getValue(mChildElement,"user_type");
									obj.mUserType=obj.mUserType.equals(null)?mEmptyValue:obj.mUserType;
									obj.mUserName=ZouponsWebService.getValue(mChildElement,"user_name");
									obj.mUserName=obj.mUserName.equals(null)?mEmptyValue:obj.mUserName;
									obj.mStoreName=ZouponsWebService.getValue(mChildElement,"store_name");
									obj.mStoreName=obj.mStoreName.equals(null)?mEmptyValue:obj.mStoreName;

									try{		        					
										if(!obj.mPostedTime.equals("")){
											if(obj.mPostedTime.contains("0000")){
												obj.mPostedTime=obj.mPostedTime;
											}else{
												obj.mPostedTime = convertTimeToDeviceTime(obj.mPostedTime, obj.mTimeZone);
											}
										}

									}catch (Exception e) {
										e.printStackTrace();
									}
									WebServiceStaticArrays.mContactUsResponseResult.add(obj);
								}
							}else{

							}
						}
						if(flag.equalsIgnoreCase("scroll")){
							WebServiceStaticArrays.mContactUsResponse = new ArrayList<Object>(WebServiceStaticArrays.mContactUsResponseResult);
							for(int i=0;i<WebServiceStaticArrays.mContactUsResponseDuringScroll.size();i++){
								POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponseDuringScroll.get(i);
								WebServiceStaticArrays.mContactUsResponse.add(WebServiceStaticArrays.mContactUsResponse.size(),obj);
							}
						}else if(flag.equalsIgnoreCase("refresh")){
							for(int i=0;i<WebServiceStaticArrays.mContactUsResponseResult.size();i++){
								POJOContactUsResponse obj = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponseResult.get(i);
								if(WebServiceStaticArrays.mContactUsResponse.size()>0){
									POJOContactUsResponse checkForDuplicateObject = (POJOContactUsResponse) WebServiceStaticArrays.mContactUsResponse.get(WebServiceStaticArrays.mContactUsResponse.size()-1);
									if(checkForDuplicateObject.mResponseId.equalsIgnoreCase(obj.mResponseId)){
										// Dont add
									}else{
										WebServiceStaticArrays.mContactUsResponse.add(WebServiceStaticArrays.mContactUsResponse.size(),obj);
									}
								}else{
									WebServiceStaticArrays.mContactUsResponse.add(WebServiceStaticArrays.mContactUsResponse.size(),obj);	
								}
							}

						}else{
							WebServiceStaticArrays.mContactUsResponse = new ArrayList<Object>(WebServiceStaticArrays.mContactUsResponseResult);
						}
						rtnValue="success";
					}

				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}
	// function to parse rewards advertisement details
	public String ParseRewardsAdvertisementDetails(String mGetResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("get_ad");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{
				rtnValue="success";
				WebServiceStaticArrays.mRewardsAdvertisements.clear();
				for(int i=0;i<mNodes.getLength();i++){
					RewardsAdvertisementDetails obj = new RewardsAdvertisementDetails();
					mNodeElement = (Element) mNodes.item(i);
					obj.AdType=ZouponsWebService.getValue(mNodeElement,"type");
					obj.AdType=obj.AdType.equals(null)?mEmptyValue:obj.AdType;
					obj.AdURL=ZouponsWebService.getValue(mNodeElement,"url");
					obj.AdURL=obj.AdURL.equals(null)?mEmptyValue:obj.AdURL;
					obj.message=ZouponsWebService.getValue(mNodeElement,"message");
					obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
					WebServiceStaticArrays.mRewardsAdvertisements.add(obj);
				}
			}
		} catch (Exception e) {
			rtnValue="failure";
		}

		return rtnValue;
	}

	// To parse store name availability response
	public String mParseCheckStoreName(String mCheckStoreNameResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(mCheckStoreNameResponse);
			mNodes = mDoc.getElementsByTagName("check_store");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					MainMenuActivity.mCheckStoreMessage="";
					for(int i=0;i<mNodes.getLength();i++){		        		   
						mNodeElement = (Element) mNodes.item(i);		        		
						//Log.i(TAG, "TAG NAME: "+mNodeElement.getTagName());	
						String ResponseMessage=ZouponsWebService.getValue(mNodeElement,"message");
						MainMenuActivity.mCheckStoreMessage=ResponseMessage.equals(null)?mEmptyValue:ResponseMessage;		    			 
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}

		} catch (Exception e) {
			rtnValue="failure";
		}

		return rtnValue;
	}

	//Parse Terms and Conditions Response
	public String mParseTermsCondition(String mGetResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("terms_and_condition");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					MainMenuActivity.mTermsConditionsMessage="";
					for(int i=0;i<mNodes.getLength();i++){		        		   
						mNodeElement = (Element) mNodes.item(i);		        		
						//Log.i(TAG, "TAG NAME: "+mNodeElement.getTagName());	
						String ResponseMessage=ZouponsWebService.getValue(mNodeElement,"description");
						MainMenuActivity.mTermsConditionsMessage=ResponseMessage.equals(null)?mEmptyValue:ResponseMessage;
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}

		} catch (Exception e) {
			rtnValue="failure";
		}

		return rtnValue;
	}

	//Parse Add Rewards Response
	public String mParseAddRewardResponse(String mGetResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("rewards");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					MainMenuActivity.mAddRewardMessage="";
					for(int i=0;i<mNodes.getLength();i++){		        		   
						mNodeElement = (Element) mNodes.item(i);		        		
						String ResponseMessage=ZouponsWebService.getValue(mNodeElement,"message");
						MainMenuActivity.mAddRewardMessage=ResponseMessage.equals(null)?mEmptyValue:ResponseMessage;		    			 
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}

		} catch (Exception e) {
			rtnValue="failure";
		}

		return rtnValue;
	}

	// To parse invite friends response
	public String parseInviteFriendResponse(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("invite_friends");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					Friends.invitestatus = "";
					for(int i=0;i<mNodes.getLength();i++){		        		   
						mNodeElement = (Element) mNodes.item(i);		        		
						String ResponseMessage=ZouponsWebService.getValue(mNodeElement,"message");
						Friends.invitestatus=ResponseMessage.equals(null)?mEmptyValue:ResponseMessage;		    			 
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}

		} catch (Exception e) {
			rtnValue="failure";
		}

		return rtnValue;
	}

	// To parse giftcards list response
	public String mParseAllGiftCardResponse(String mGetResponse) {
		String rtnValue="";		
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetResponse);						 
			mNodes = mDoc.getElementsByTagName("giftcards");				
			mChildNodes = mDoc.getElementsByTagName("giftcard");
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					POJOAllGiftCards.GET_ALL_GIFT_CARDS_MESSAGE="";	
					for(int i=0;i<mNodes.getLength();i++){	        			
						mNodeElement = (Element) mNodes.item(i);	
						String mTotalCountValue = ZouponsWebService.getValue(mNodeElement,"total_count");
						POJOLimit.mTotalCount = (mTotalCountValue.equals(null)||mTotalCountValue.equals(""))?mIntegerEmptyValue:mTotalCountValue;
						POJOAllGiftCards.mTotalCount = (mTotalCountValue.equals(null)||mTotalCountValue.equals(""))?mIntegerEmptyValue:mTotalCountValue;
						if(mChildNodes.getLength() != 0){          				
							for(int j=0;j<mChildNodes.getLength();j++){
								POJOAllGiftCards obj = new POJOAllGiftCards();
								mChildElement = (Element) mChildNodes.item(j);
								obj.mGiftCardId=ZouponsWebService.getValue(mChildElement,"gift_card_id");
								obj.mGiftCardId=obj.mGiftCardId.equals(null)?mEmptyValue:obj.mGiftCardId;
								//Log.i(TAG,"obj.mGiftCardId: "+obj.mGiftCardId);
								obj.mGiftcardPurchaseId=ZouponsWebService.getValue(mChildElement,"gift_card_purchase_id");
								obj.mGiftcardPurchaseId=obj.mGiftcardPurchaseId.equals(null)?mEmptyValue:obj.mGiftcardPurchaseId;
								obj.mStoreId=ZouponsWebService.getValue(mChildElement,"store_id");
								obj.mStoreId=obj.mStoreId.equals(null)?mEmptyValue:obj.mStoreId;
								obj.mStoreName=ZouponsWebService.getValue(mChildElement,"store_name");
								obj.mStoreName=obj.mStoreName.equals(null)?mEmptyValue:obj.mStoreName;
								obj.mFaceValue=ZouponsWebService.getValue(mChildElement,"face_value");
								obj.mFaceValue=obj.mFaceValue.equals(null)||obj.mFaceValue.equals("")?mIntegerEmptyValue:obj.mFaceValue;
								obj.mCardType=ZouponsWebService.getValue(mChildElement,"type");
								obj.mCardType=obj.mCardType.equals(null)?mEmptyValue:obj.mCardType;
								obj.mBalanceAmount=ZouponsWebService.getValue(mChildElement,"balance_amount");
								obj.mBalanceAmount=obj.mBalanceAmount.equals(null)||obj.mBalanceAmount.equals("")?mIntegerEmptyValue:obj.mBalanceAmount;
								obj.mDescription=ZouponsWebService.getValue(mChildElement,"description");
								obj.mDescription=obj.mDescription.equals(null)?mEmptyValue:obj.mDescription;
								obj.mGiftCardImage=ZouponsWebService.getValue(mChildElement,"image_path");
								obj.mGiftCardImage=obj.mGiftCardImage.equals(null)?mEmptyValue:obj.mGiftCardImage;
								if(obj.mGiftCardImage.contains("")){
									obj.mGiftCardImage = obj.mGiftCardImage.replaceAll("\\s","%20");
								}
								obj.mGiftcardPurchaseDateTimeZone=ZouponsWebService.getValue(mChildElement,"time_zone");
								obj.mGiftcardPurchaseDateTimeZone=obj.mGiftcardPurchaseDateTimeZone.equals(null)?mEmptyValue:obj.mGiftcardPurchaseDateTimeZone;
								//timezone
								obj.mGiftcardPurchaseDate=ZouponsWebService.getValue(mChildElement,"created");
								obj.mGiftcardPurchaseDate=obj.mGiftcardPurchaseDate.equals(null)?mEmptyValue:convertTimeToDeviceTime(obj.mGiftcardPurchaseDate,obj.mGiftcardPurchaseDateTimeZone);

								obj.mLocationId=ZouponsWebService.getValue(mChildElement,"location_id");
								obj.mLocationId=obj.mLocationId.equals(null)?mEmptyValue:obj.mLocationId;
								WebServiceStaticArrays.mAllGiftCardList.add(obj);
							}
							POJOAllGiftCards.GET_ALL_GIFT_CARDS_MESSAGE="";
						}else{
							//If web service only returns Message 
							String mResponseMessage=ZouponsWebService.getValue(mNodeElement,"message");
							POJOAllGiftCards.GET_ALL_GIFT_CARDS_MESSAGE=mResponseMessage.equals(null)?mEmptyValue:mResponseMessage;
							//Log.i(TAG,"Contact Us Response Message : "+POJOAllGiftCards.GET_ALL_GIFT_CARDS_MESSAGE);
						}
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	// To parse send giftcard to friend response
	public String mParseSendGiftCardResponse(String mGetResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("send_giftcards");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					SEND_GIFT_CARD_MESSAGE = "";
					for(int i=0;i<mNodes.getLength();i++){		        		   
						mNodeElement = (Element) mNodes.item(i);		        		
						//Log.i(TAG, "TAG NAME: "+mNodeElement.getTagName());	
						String ResponseMessage=ZouponsWebService.getValue(mNodeElement,"message");
						SEND_GIFT_CARD_MESSAGE=ResponseMessage.equals(null)? mEmptyValue:ResponseMessage;		    			 
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}

		} catch (Exception e) {
			rtnValue="failure";
		}
		return rtnValue;
	}

	// To parse store location review list response
	public String parseStoreReviewDetails(String response) {
		String returnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("reviews");
			mChildNodes = mDoc.getElementsByTagName("review");
			mRecordSize = zouponswebservice.numResults(mDoc);					  
			if(mRecordSize<=0){
				returnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						returnValue="norecords";
					}else{
						returnValue="success";
						Element totalcountElement = (Element) mNodes.item(0);
						MainMenuActivity.mReviewTotalCount = ZouponsWebService.getValue(totalcountElement,"total_count");
						StoreOwnerReviews.mStoreOwnerReviewsTotalCount = ZouponsWebService.getValue(totalcountElement,"total_count");
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								for(int j=0;j<mChildNodes.getLength();j++){	
									POJOStoreReviewDetails reviewDetails = new POJOStoreReviewDetails();
									mChildElement = (Element) mChildNodes.item(j);
									reviewDetails.review_id = ZouponsWebService.getValue(mChildElement,"id");
									reviewDetails.review_id = reviewDetails.review_id.equals(null)?mEmptyValue:reviewDetails.review_id;
									reviewDetails.user_id = ZouponsWebService.getValue(mChildElement,"posted_by");
									reviewDetails.user_id =reviewDetails.user_id.equals(null)?mEmptyValue:reviewDetails.user_id;
									reviewDetails.message = ZouponsWebService.getValue(mChildElement,"comment");
									reviewDetails.message = reviewDetails.message.equals(null)?mEmptyValue:reviewDetails.message;
									reviewDetails.rating = ZouponsWebService.getValue(mChildElement,"rating");
									reviewDetails.rating = reviewDetails.rating.equals(null)?mEmptyValue:reviewDetails.rating;
									reviewDetails.posted_date = ZouponsWebService.getValue(mChildElement,"posted_date");
									reviewDetails.posted_date = reviewDetails.posted_date.equals(null)?mEmptyValue:reviewDetails.posted_date;
									if(reviewDetails.posted_date!=null && !reviewDetails.posted_date.equals("")){
										String[] datesplit = reviewDetails.posted_date.split("-");
										reviewDetails.posted_date=datesplit[1]+"/"+datesplit[2]+"/"+datesplit[0];  // mm/dd/yyyy
									}
									reviewDetails.company_id = ZouponsWebService.getValue(mChildElement,"company_id");
									reviewDetails.company_id = reviewDetails.company_id.equals(null)?mEmptyValue:reviewDetails.company_id;
									reviewDetails.status = ZouponsWebService.getValue(mChildElement,"status");
									reviewDetails.status = reviewDetails.status.equals(null)?mEmptyValue:reviewDetails.status;
									reviewDetails.visitor_id  = ZouponsWebService.getValue(mChildElement,"visitor_id");
									reviewDetails.visitor_id = reviewDetails.visitor_id .equals(null)?mEmptyValue:reviewDetails.visitor_id ;
									reviewDetails.user_name = ZouponsWebService.getValue(mChildElement,"fullname");
									reviewDetails.user_name = reviewDetails.user_name.equals(null)?mEmptyValue:reviewDetails.user_name;
									reviewDetails.user_likes = ZouponsWebService.getValue(mChildElement,"likes");
									reviewDetails.user_likes = reviewDetails.user_likes.equals(null)?mEmptyValue:reviewDetails.user_likes;
									reviewDetails.user_dislikes = ZouponsWebService.getValue(mChildElement,"dislikes");
									reviewDetails.user_dislikes =reviewDetails.user_dislikes.equals(null)?mEmptyValue:reviewDetails.user_dislikes;
									WebServiceStaticArrays.mStoreReviewsList.add(reviewDetails);
								}
							}
						}
					}	

				}else{
					returnValue="norecords";
				}
			}
		}catch (Exception e) {
			returnValue="failure";
		}
		return returnValue;	
	}

	// To parse post review response
	public String mParsePostUpdateReviewResponse(String PostReviewResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(PostReviewResponse);
			mNodes = mDoc.getElementsByTagName("review");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					MainMenuActivity.STORE_REVIEW_MESSAGE="";
					for(int i=0;i<mNodes.getLength();i++){		        		   
						mNodeElement = (Element) mNodes.item(i);		        		
						//Log.i(TAG, "TAG NAME: "+mNodeElement.getTagName());	
						String ResponseMessage=ZouponsWebService.getValue(mNodeElement,"message");
						StoreReviews.STORE_REVIEW_MESSAGE=ResponseMessage.equals(null)? mEmptyValue:ResponseMessage;		    			 
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}

		} catch (Exception e) {
			rtnValue="failure";
		}

		return rtnValue;
	}

	// To parse store review status response
	public String mParseReviewStatusResponse(String mGetResponse) {
		String rtnValue="";		
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetResponse);						 
			mNodes = mDoc.getElementsByTagName("review_rates");				
			mChildNodes = mDoc.getElementsByTagName("review_rate");
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					for(int i=0;i<mNodes.getLength();i++){	        			
						mNodeElement = (Element) mNodes.item(i);	        			
						if(mChildNodes.getLength() != 0){        				
							WebServiceStaticArrays.GetReviewStatusList.clear();	
							for(int j=0;j<mChildNodes.getLength();j++){
								POJOReviewStatus obj = new POJOReviewStatus();
								mChildElement = (Element) mChildNodes.item(j);
								obj.ReviewRateId=ZouponsWebService.getValue(mChildElement,"id");
								obj.ReviewRateId=obj.ReviewRateId.equals(null)?mEmptyValue:obj.ReviewRateId;
								obj.UserId=ZouponsWebService.getValue(mChildElement,"user_id");
								obj.UserId=obj.UserId.equals(null)?mEmptyValue:obj.UserId;
								obj.Rate=ZouponsWebService.getValue(mChildElement,"rate");
								obj.Rate=obj.Rate.equals(null)?mEmptyValue:obj.Rate;
								obj.VisitorId=ZouponsWebService.getValue(mChildElement,"visitor_id");
								obj.VisitorId=obj.VisitorId.equals(null)?mEmptyValue:obj.VisitorId;
								obj.ReviewId=ZouponsWebService.getValue(mChildElement,"review_id");
								obj.ReviewId=obj.ReviewId.equals(null)?mEmptyValue:obj.ReviewId;
								obj.Status=ZouponsWebService.getValue(mChildElement,"user_type");
								obj.Status=obj.Status.equals(null)?mEmptyValue:obj.Status;
								obj.AddedTime=ZouponsWebService.getValue(mChildElement,"added_time");
								obj.AddedTime=obj.AddedTime.equals(null)?mEmptyValue:obj.AddedTime;
								obj.ModifiedTime=ZouponsWebService.getValue(mChildElement,"modified_time");
								obj.ModifiedTime=obj.ModifiedTime.equals(null)?mEmptyValue:obj.ModifiedTime;
								//To format the Date
								SimpleDateFormat receivedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								SimpleDateFormat formatedDate = new SimpleDateFormat("yyyy-MM-dd");
								try{		        					
									if(!obj.AddedTime.equals("")){
										String mreformat = formatedDate.format(receivedDate.parse(obj.AddedTime));
										obj.AddedTime = mreformat;
									}

								}catch (Exception e) {
									e.printStackTrace();
								}		        	
								try{		        					
									if(!obj.ModifiedTime.equals("")){
										String mreformat = formatedDate.format(receivedDate.parse(obj.ModifiedTime));
										obj.ModifiedTime = mreformat;
									}

								}catch (Exception e) {
									e.printStackTrace();
								}		        	

								WebServiceStaticArrays.GetReviewStatusList.add(obj);

							}
						}else{
							//If web service only returns Message 
							WebServiceStaticArrays.GetReviewStatusList.clear();	
							POJOReviewStatus obj = new POJOReviewStatus();
							obj.Message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.Message=obj.Message.equals(null)?mEmptyValue:obj.Message;
							WebServiceStaticArrays.GetReviewStatusList.add(obj);
						}
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	// To parse inappropiate review response
	public String mParseInAppropriateReviewResponse(String mGetResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("review_flag");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					StoreReviews.STORE_REVIEW_MESSAGE="";
					for(int i=0;i<mNodes.getLength();i++){		        		   
						mNodeElement = (Element) mNodes.item(i);		        		
						String ResponseMessage=ZouponsWebService.getValue(mNodeElement,"message");
						StoreReviews.STORE_REVIEW_MESSAGE=ResponseMessage.equals(null)? mEmptyValue:ResponseMessage;		    			 
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			rtnValue="failure";
		}

		return rtnValue;
	}

	// To parse giftcard transaction history response
	public String mParseGiftCardTransactionHistoryResponse(String mGetResponse) {
		String rtnValue="";		
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetResponse);						 
			mNodes = mDoc.getElementsByTagName("giftcard_transaction_history");				
			mParentNode = mDoc.getElementsByTagName("root");
			mChildNodes = mDoc.getElementsByTagName("element");
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength()+" "+ "root node  "+mParentNode.getLength());
					GiftCardTransactionHistoryTask.GIFT_CARD_TRANSACTION_HISTORY_MESSAGE="";
					WebServiceStaticArrays.mGiftCardTransactionHistory.clear();
					if(mNodes.getLength() == 1 && mParentNode.getLength() == 0){
						mNodeElement = (Element) mNodes.item(0);
						//If web service only returns Message 
						String mResponseMessage=ZouponsWebService.getValue(mNodeElement,"message");
						GiftCardTransactionHistoryTask.GIFT_CARD_TRANSACTION_HISTORY_MESSAGE=mResponseMessage.equals(null)?mEmptyValue:mResponseMessage;
						//Log.i(TAG,"Transaction history Response Message : "+GiftCardTransactionHistoryTask.GIFT_CARD_TRANSACTION_HISTORY_MESSAGE);
						rtnValue="norecords";
					}else{
						for(int i=0;i<mChildNodes.getLength();i++){	
							POJOGiftCardTransactionHistory obj = new POJOGiftCardTransactionHistory();
							mNodeElement = (Element) mChildNodes.item(i);	        			
							obj.mTotalAmount=ZouponsWebService.getValue(mNodeElement,"amount");
							obj.mTotalAmount=obj.mTotalAmount.equals(null)?mIntegerEmptyValue:obj.mTotalAmount;
							obj.mTimeZone=ZouponsWebService.getValue(mNodeElement,"time_zone");
							obj.mTimeZone=obj.mTimeZone.equals(null)?mEmptyValue:obj.mTimeZone;
							obj.mTransactionDate=ZouponsWebService.getValue(mNodeElement,"created_date");
							obj.mTransactionDate=obj.mTransactionDate.equals(null)?mEmptyValue:convertTimeToDeviceTime(obj.mTransactionDate,obj.mTimeZone);
							if(i==0){
								obj.mStatus = "NotAvailable"; // Use for changing text color in adapter class
							}else{
								obj.mStatus=ZouponsWebService.getValue(mNodeElement,"status");
								obj.mStatus=obj.mStatus.equals(null)?mEmptyValue:obj.mStatus;
							}
							obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
							obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
							WebServiceStaticArrays.mGiftCardTransactionHistory.add(i,obj);
						}
						rtnValue="success";	
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	// to parse receipts details
	public String parseReceiptsList(String response){
		String returnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("my_receipts");
			mChildNodes = mDoc.getElementsByTagName("my_receipt");
			mRecordSize = zouponswebservice.numResults(mDoc);
			if(mRecordSize<=0){
				returnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					if(mNodes.getLength() == 1 && mChildNodes.getLength()==0){
						returnValue="norecords";
					}else{
						returnValue="success";
						mParentElement = (Element) mNodes.item(0);
						String totalcount = ZouponsWebService.getValue(mParentElement, "total_count");
						Receipts.mReceiptTotalCount = totalcount.equals(null)?mIntegerEmptyValue:totalcount;
						String timezone = ZouponsWebService.getValue(mParentElement,"time_zone");
						timezone = timezone.equals(null)?mEmptyValue:timezone;
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								for(int j=0;j<mChildNodes.getLength();j++){	
									POJOReceiptsDetails mReceiptsData = new POJOReceiptsDetails();
									mChildElement = (Element) mChildNodes.item(j);
									mReceiptsData.total_amount = ZouponsWebService.getValue(mChildElement,"total_amount");
									mReceiptsData.total_amount =mReceiptsData.total_amount.equals(null)||mReceiptsData.total_amount.equals("")?mIntegerEmptyValue:mReceiptsData.total_amount;
									mReceiptsData.tip_amount = ZouponsWebService.getValue(mChildElement,"tip_amount");
									mReceiptsData.tip_amount =mReceiptsData.tip_amount.equals(null)||mReceiptsData.tip_amount.equals("")?mIntegerEmptyValue:mReceiptsData.tip_amount;
									mReceiptsData.TimeZone = timezone;
									mReceiptsData.transaction_date = ZouponsWebService.getValue(mChildElement,"transction_date");
									mReceiptsData.transaction_date =mReceiptsData.transaction_date.equals(null)?mEmptyValue:convertTimeToDeviceTime(mReceiptsData.transaction_date,mReceiptsData.TimeZone);

									mReceiptsData.store_name = ZouponsWebService.getValue(mChildElement,"store_name");
									mReceiptsData.store_name =mReceiptsData.store_name.equals(null)?mEmptyValue:mReceiptsData.store_name;
									mReceiptsData.store_logo = ZouponsWebService.getValue(mChildElement,"store_logo");
									mReceiptsData.store_logo =mReceiptsData.store_logo.equals(null)?mEmptyValue:mReceiptsData.store_logo;
									mReceiptsData.card_name = ZouponsWebService.getValue(mChildElement,"card_name");
									mReceiptsData.card_name =mReceiptsData.card_name.equals(null)?mEmptyValue:mReceiptsData.card_name;
									mReceiptsData.card_mask = ZouponsWebService.getValue(mChildElement,"card_mask");
									mReceiptsData.card_mask =mReceiptsData.card_mask.equals(null)?mEmptyValue:mReceiptsData.card_mask;
									mReceiptsData.transaction_type = ZouponsWebService.getValue(mChildElement,"transaction_type");
									mReceiptsData.transaction_type =mReceiptsData.transaction_type.equals(null)?mEmptyValue:mReceiptsData.transaction_type;
									mReceiptsData.CreditCardAmount = ZouponsWebService.getValue(mChildElement,"amount_creditcard");
									mReceiptsData.CreditCardAmount =mReceiptsData.transaction_type.equals(null)?mEmptyValue:mReceiptsData.CreditCardAmount;
									mReceiptsData.GiftCardUsed = ZouponsWebService.getValue(mChildElement,"face_value");
									mReceiptsData.GiftCardUsed =mReceiptsData.transaction_type.equals(null)?mEmptyValue:mReceiptsData.GiftCardUsed;
									mReceiptsData.GiftCardAmount = ZouponsWebService.getValue(mChildElement,"amount_giftcard");
									mReceiptsData.GiftCardAmount =mReceiptsData.transaction_type.equals(null)?mEmptyValue:mReceiptsData.GiftCardAmount;
									mReceiptsData.CustomerNotes = ZouponsWebService.getValue(mChildElement,"customer_note");
									mReceiptsData.CustomerNotes =mReceiptsData.CustomerNotes.equals(null)?mEmptyValue:mReceiptsData.CustomerNotes;
									mReceiptsData.StoreNotes = ZouponsWebService.getValue(mChildElement,"store_note");
									mReceiptsData.StoreNotes =mReceiptsData.StoreNotes.equals(null)?mEmptyValue:mReceiptsData.StoreNotes;
									WebServiceStaticArrays.mReceiptsDetails.add(mReceiptsData);
								}
							}
						}
					}
				}else{
					returnValue="norecords";
				}
			}
		}catch (Exception e) {

			returnValue="failure";
		}
		return returnValue;
	}

	// To parse invoice list response
	public String mParseInvoiceListResponse(String mGetResponse) {
		String rtnValue="";		
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetResponse);						 
			mNodes = mDoc.getElementsByTagName("invoices");				
			mChildNodes = mDoc.getElementsByTagName("invoice");
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					InvoiceApproval.mInvoiceListMessage="";
					for(int i=0;i<mNodes.getLength();i++){	        			
						mNodeElement = (Element) mNodes.item(i);	        			
						if(mChildNodes.getLength() != 0){        				
							WebServiceStaticArrays.mInvoiceArrayList.clear();
							for(int j=0;j<mChildNodes.getLength();j++){
								POJOInvoiceList obj = new POJOInvoiceList();
								mChildElement = (Element) mChildNodes.item(j);
								obj.InvoiceId=ZouponsWebService.getValue(mChildElement,"id");
								obj.InvoiceId=obj.InvoiceId.equals(null)?mEmptyValue:obj.InvoiceId;
								obj.StoreId=ZouponsWebService.getValue(mChildElement,"store_id");
								obj.StoreId=obj.StoreId.equals(null)?mEmptyValue:obj.StoreId;
								obj.LocationId=ZouponsWebService.getValue(mChildElement,"location_id");
								obj.LocationId=obj.LocationId.equals(null)?mEmptyValue:obj.LocationId;
								obj.StoreName=ZouponsWebService.getValue(mChildElement,"store_name");
								obj.StoreName=obj.StoreName.equals(null)?mEmptyValue:obj.StoreName;
								obj.LogoPath=ZouponsWebService.getValue(mChildElement,"logo_path");
								obj.LogoPath=obj.LogoPath.equals(null)?mEmptyValue:obj.LogoPath;
								obj.RaisedBy=ZouponsWebService.getValue(mChildElement,"raised_by");
								obj.RaisedBy=obj.RaisedBy.equals(null)?mEmptyValue:obj.RaisedBy;
								obj.RaisedByType=ZouponsWebService.getValue(mChildElement,"raised_by_type");
								obj.RaisedByType=obj.RaisedByType.equals(null)?mEmptyValue:obj.RaisedByType;
								obj.Amount=ZouponsWebService.getValue(mChildElement,"amount");
								obj.Amount=obj.Amount.equals(null)?mEmptyValue:obj.Amount;
								obj.TimeZone=ZouponsWebService.getValue(mChildElement,"time_zone");
								obj.TimeZone=obj.TimeZone.equals(null)?mEmptyValue:obj.TimeZone;
								//timezone
								obj.CreatedDate=ZouponsWebService.getValue(mChildElement,"created");
								obj.CreatedDate=obj.CreatedDate.equals(null)?mEmptyValue:convertTimeToDeviceTime(obj.CreatedDate,obj.TimeZone);
								obj.Note=ZouponsWebService.getValue(mChildElement,"note");
								obj.Note=obj.Note.equals(null)?mEmptyValue:obj.Note;
								obj.Description = "Please review and approve the invoice sent to you by store for services provided in the amount of $"+
										obj.Amount+". \n"+obj.Note;
								WebServiceStaticArrays.mInvoiceArrayList.add(obj);
							}
						}else{
							//If web service only returns Message 
							WebServiceStaticArrays.mInvoiceArrayList.clear();
							InvoiceApproval.mInvoiceListMessage=ZouponsWebService.getValue(mNodeElement,"message");
							InvoiceApproval.mInvoiceListMessage=InvoiceApproval.mInvoiceListMessage.equals(null)?mEmptyValue:InvoiceApproval.mInvoiceListMessage;
							//Log.i(TAG,"Invoice List Response Message : "+InvoiceApproval.mInvoiceListMessage);
						}
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	// To parse reject invoice response
	public String mParseRejectInvoiceResponse(String mGetResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try {
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("approve_invoice");					
			mRecordSize = zouponswebservice.numResults(mDoc);				
			if (mRecordSize<=0){
				rtnValue = "norecords";
			} else{		    	 
				if (mNodes!=null&&mNodes.getLength()>0) {
					InvoiceApproval.mRejectInvoiceMessage="";
					for(int i=0;i<mNodes.getLength();i++){		        		   
						mNodeElement = (Element) mNodes.item(i);		        		
						InvoiceApproval.mRejectInvoiceMessage=ZouponsWebService.getValue(mNodeElement,"message");
						InvoiceApproval.mRejectInvoiceMessage=InvoiceApproval.mRejectInvoiceMessage.equals(null)? mEmptyValue:InvoiceApproval.mRejectInvoiceMessage;		    			 
					}
					rtnValue="success";
				} else {
					rtnValue="norecords";
				}
			}

		} catch (Exception e) {
			rtnValue="failure";
		}
		return rtnValue;
	}

	//function for parsing categories webservice data
	public String parseMy_Gift_cards(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("giftcard");							//function to get value by its category tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					//Log.i(TAG,"Node Length : "+mNodes.getLength());
					WebServiceStaticArrays.mMyGiftCards.clear();
					for(int i=0;i<mNodes.getLength();i++){
						MyGiftCards_ClassVariables obj = new MyGiftCards_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);

						if(mNodeElement.getChildNodes().getLength()>1){
							obj.id=ZouponsWebService.getValue(mNodeElement,"id");
							obj.id=obj.id.equals(null)?mEmptyValue:obj.id;
							obj.purchaseid=ZouponsWebService.getValue(mNodeElement,"gift_card_purchase_id");
							obj.purchaseid=obj.purchaseid.equals(null)?mEmptyValue:obj.purchaseid;
							obj.face_value=ZouponsWebService.getValue(mNodeElement,"face_value");
							obj.face_value=obj.face_value.equals(null)||obj.face_value.equals("")?mIntegerEmptyValue:obj.face_value;
							obj.balance_amount=ZouponsWebService.getValue(mNodeElement,"balance_amount");
							obj.balance_amount=obj.balance_amount.equals(null)||obj.balance_amount.equals("")?mIntegerEmptyValue:obj.balance_amount;
							obj.description=ZouponsWebService.getValue(mNodeElement,"description");
							obj.description=obj.description.equals(null)?mEmptyValue:obj.description;
							obj.storename=ZouponsWebService.getValue(mNodeElement,"store_name");
							obj.storename=obj.storename.equals(null)?mEmptyValue:obj.storename;	

							WebServiceStaticArrays.mMyGiftCards.add(obj);
						}else if(mNodeElement.getChildNodes().getLength()==1){
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							//Log.i(TAG,"obj.message : "+obj.message);
							WebServiceStaticArrays.mMyGiftCards.add(obj);
						}
					}
					if(mNodes.getLength()>0){
						rtnValue="success";
					}else{
						mNodes = mDoc.getElementsByTagName("giftcards");						//function to get value by its categories tag name
						if(mNodes!=null&&mNodes.getLength()>0){
							for(int i=0;i<mNodes.getLength();i++){
								MyGiftCards_ClassVariables obj = new MyGiftCards_ClassVariables();
								mNodeElement = (Element) mNodes.item(i);
								obj.message=ZouponsWebService.getValue(mNodeElement,"message");
								obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
								//Log.i(TAG,"obj.message : "+obj.message);
								WebServiceStaticArrays.mMyGiftCards.add(obj);
							}
							rtnValue="success";
						}else{
							rtnValue="norecords";
						}
					}
				}else{
					WebServiceStaticArrays.mMyGiftCards.clear();
					mNodes = mDoc.getElementsByTagName("giftcards");						//function to get value by its categories tag name
					if(mNodes!=null&&mNodes.getLength()>0){
						for(int i=0;i<mNodes.getLength();i++){
							MyGiftCards_ClassVariables obj = new MyGiftCards_ClassVariables();
							mNodeElement = (Element) mNodes.item(i);
							obj.message=ZouponsWebService.getValue(mNodeElement,"message");
							obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
							//Log.i(TAG,"obj.message : "+obj.message);
							WebServiceStaticArrays.mMyGiftCards.add(obj);
						}
						rtnValue="success";
					}else{
						rtnValue="norecords";
					}
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	// To parse validation of secret PIN response
	public String parseCheckPIN(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("validatepin");							//function to get value by its category tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "failure";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						CheckPINClassVariables.message=ZouponsWebService.getValue(mNodeElement,"message");
						CheckPINClassVariables.message=CheckPINClassVariables.message.equals(null)?mEmptyValue:CheckPINClassVariables.message;
					}
					rtnValue = "success";
				}else{
					rtnValue="failure";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	// To parse gitcard purchase response
	public String parsePurcahseGiftcardPaymentDetails(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("payment");						
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "failure";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						PaymentStatusVariables.Customer_Reference=ZouponsWebService.getValue(mNodeElement,"Customer_Ref");
						PaymentStatusVariables.Customer_Reference=PaymentStatusVariables.Customer_Reference.equals(null)?mEmptyValue:PaymentStatusVariables.Customer_Reference;
						PaymentStatusVariables.Authorization_Number=ZouponsWebService.getValue(mNodeElement,"Authorization_Num");
						PaymentStatusVariables.Authorization_Number=PaymentStatusVariables.Authorization_Number.equals(null)?mEmptyValue:PaymentStatusVariables.Authorization_Number;
						PaymentStatusVariables.Bank_Message=ZouponsWebService.getValue(mNodeElement,"Bank_Message");
						PaymentStatusVariables.Bank_Message=PaymentStatusVariables.Bank_Message.equals(null)?mEmptyValue:PaymentStatusVariables.Bank_Message;
						PaymentStatusVariables.message=ZouponsWebService.getValue(mNodeElement,"message");
						PaymentStatusVariables.message=PaymentStatusVariables.message.equals(null)?mEmptyValue:PaymentStatusVariables.message;
						PaymentStatusVariables.purchase_message=ZouponsWebService.getValue(mNodeElement,"purchase_message");
						PaymentStatusVariables.purchase_message=PaymentStatusVariables.purchase_message.equals(null)?mEmptyValue:PaymentStatusVariables.purchase_message;
					}
					rtnValue = "success";
				}else{
					rtnValue="failure";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	// To parse payment response
	public String parseNormalPayment(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("initiate_payment");							//function to get value by its category tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "failure";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						PaymentStatusVariables.message=ZouponsWebService.getValue(mNodeElement,"message");
						PaymentStatusVariables.message=PaymentStatusVariables.message.equals(null)?mEmptyValue:PaymentStatusVariables.message;
						PaymentStatusVariables.Qr_url_string=ZouponsWebService.getValue(mNodeElement,"qr_code");
						PaymentStatusVariables.Qr_url_string=PaymentStatusVariables.Qr_url_string.equals(null)?mEmptyValue:PaymentStatusVariables.Qr_url_string;
						PaymentStatusVariables.Qr_expires_in=ZouponsWebService.getValue(mNodeElement,"expired_in");
						PaymentStatusVariables.Qr_expires_in=PaymentStatusVariables.Qr_expires_in.equals(null)?mEmptyValue:PaymentStatusVariables.Qr_expires_in;
						PaymentStatusVariables.Invoice_id=ZouponsWebService.getValue(mNodeElement,"invoice_id");
						PaymentStatusVariables.Invoice_id=PaymentStatusVariables.Invoice_id.equals(null)?mEmptyValue:PaymentStatusVariables.Invoice_id;
					}
					rtnValue = "success";
				}else{
					rtnValue="failure";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	public String parseNotifications(String response,String notification_response_type) {
		String rtnValue="";		
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 
			mNodes = mDoc.getElementsByTagName("notifications");				
			mChildNodes = mDoc.getElementsByTagName("notification");
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					if(mNodes.getLength() == 1 && mChildNodes.getLength()==0){
						rtnValue="norecords";
					}else{
						if(!notification_response_type.equalsIgnoreCase("scroll")){
							Element totalcountElement = (Element) mNodes.item(0);
							String notification_unreadcount = ZouponsWebService.getValue(totalcountElement,"total_unread");
							notification_unreadcount  = notification_unreadcount.equals(null)|| notification_unreadcount.equals("")?"0":notification_unreadcount;
							NotificationDetails.notificationcount = Integer.valueOf(notification_unreadcount);	
						}

						//Log.i("total count", MainMenuActivity.mCouponTotalCount);
						for(int i=0;i<mNodes.getLength();i++){	        			
							mNodeElement = (Element) mNodes.item(i);	        			
							if(mChildNodes.getLength() != 0){   
								//WebServiceStaticArrays.mAllNotifications.clear();
								WebServiceStaticArrays.mNotificationsResponseList.clear();
								for(int j=0;j<mChildNodes.getLength();j++){
									NotificationDetails mNotificationDetails = new NotificationDetails();
									mChildElement = (Element) mChildNodes.item(j);
									mNotificationDetails.notification_locationId=ZouponsWebService.getValue(mChildElement, "location_id");
									mNotificationDetails.notification_locationId=mNotificationDetails.notification_locationId.equals(null)?mEmptyValue:mNotificationDetails.notification_locationId;
									mNotificationDetails.id=ZouponsWebService.getValue(mChildElement,"id");
									mNotificationDetails.id=mNotificationDetails.id.equals(null)?mEmptyValue:mNotificationDetails.id;
									mNotificationDetails.notify_to=ZouponsWebService.getValue(mChildElement,"notify_to");
									mNotificationDetails.notify_to=mNotificationDetails.notify_to.equals(null)?mEmptyValue:mNotificationDetails.notify_to;
									mNotificationDetails.notification_type=ZouponsWebService.getValue(mChildElement,"notification_type");
									mNotificationDetails.notification_type=mNotificationDetails.notification_type.equals(null)?mEmptyValue:mNotificationDetails.notification_type;
									mNotificationDetails.store_logo_path=ZouponsWebService.getValue(mChildElement,"store_logo_path");
									mNotificationDetails.store_logo_path=mNotificationDetails.store_logo_path.equals(null)?mEmptyValue:mNotificationDetails.store_logo_path;
									mNotificationDetails.friend_image_path=ZouponsWebService.getValue(mChildElement,"sender_profile_pic");
									mNotificationDetails.friend_image_path=mNotificationDetails.friend_image_path.equals(null)?mEmptyValue:mNotificationDetails.friend_image_path;
									if(mNotificationDetails.friend_image_path.contains("")){
										mNotificationDetails.friend_image_path = mNotificationDetails.friend_image_path.replaceAll("\\s","%20");
									}
									mNotificationDetails.notification_msg=ZouponsWebService.getValue(mChildElement,"notification_msg");
									mNotificationDetails.notification_msg=mNotificationDetails.notification_msg.equals(null)?mEmptyValue:mNotificationDetails.notification_msg;
									mNotificationDetails.store_id=ZouponsWebService.getValue(mChildElement,"store_id");
									mNotificationDetails.store_id=mNotificationDetails.store_id.equals(null)?mEmptyValue:mNotificationDetails.store_id;
									mNotificationDetails.status=ZouponsWebService.getValue(mChildElement,"status");
									mNotificationDetails.status=mNotificationDetails.status.equals(null)?mEmptyValue:mNotificationDetails.status;
									mNotificationDetails.timeZone=ZouponsWebService.getValue(mChildElement,"time_zone");
									mNotificationDetails.timeZone=mNotificationDetails.timeZone.equals(null)?mEmptyValue:mNotificationDetails.timeZone;
									//timezone
									mNotificationDetails.created=ZouponsWebService.getValue(mChildElement,"created");
									mNotificationDetails.created=mNotificationDetails.created.equals(null)?mEmptyValue:convertTimeToDeviceTime(mNotificationDetails.created,mNotificationDetails.timeZone);
									// New Tags
									mNotificationDetails.store_name=ZouponsWebService.getValue(mChildElement,"store_name");
									mNotificationDetails.store_name=mNotificationDetails.store_name.equals(null)?mEmptyValue:mNotificationDetails.store_name;
									mNotificationDetails.notification_type_id=ZouponsWebService.getValue(mChildElement,"notification_type_id");
									mNotificationDetails.notification_type_id=mNotificationDetails.notification_type_id.equals(null)?mEmptyValue:mNotificationDetails.notification_type_id;
									mNotificationDetails.notification_shortmessage=ZouponsWebService.getValue(mChildElement,"short_msg");
									mNotificationDetails.notification_shortmessage=mNotificationDetails.notification_shortmessage.equals(null)?mEmptyValue:mNotificationDetails.notification_shortmessage;
									mNotificationDetails.notification_longmessage=ZouponsWebService.getValue(mChildElement,"long_msg");
									mNotificationDetails.notification_longmessage=mNotificationDetails.notification_longmessage.equals(null)?mEmptyValue:mNotificationDetails.notification_longmessage;
									mNotificationDetails.chat_group_count=ZouponsWebService.getValue(mChildElement,"group_count");
									mNotificationDetails.chat_group_count=mNotificationDetails.chat_group_count.equals(null)?mEmptyValue:mNotificationDetails.chat_group_count;
									mNotificationDetails.mCustomerID=ZouponsWebService.getValue(mChildElement,"notify_by_customer");
									mNotificationDetails.mCustomerID=mNotificationDetails.mCustomerID.equals(null)?mEmptyValue:mNotificationDetails.mCustomerID;
									mNotificationDetails.mCustomerFirstName=ZouponsWebService.getValue(mChildElement,"first_name");
									mNotificationDetails.mCustomerFirstName=mNotificationDetails.mCustomerFirstName.equals(null)?mEmptyValue:mNotificationDetails.mCustomerFirstName;
									mNotificationDetails.mCustomerLastName=ZouponsWebService.getValue(mChildElement,"last_name");
									mNotificationDetails.mCustomerLastName=mNotificationDetails.mCustomerLastName.equals(null)?mEmptyValue:mNotificationDetails.mCustomerLastName;
									mNotificationDetails.full_name = mNotificationDetails.mCustomerFirstName + " " + mNotificationDetails.mCustomerLastName;
									mNotificationDetails.mIsFavoriteStore=ZouponsWebService.getValue(mChildElement,"is_favorite");
									mNotificationDetails.mIsFavoriteStore=mNotificationDetails.mIsFavoriteStore.equals(null)?mEmptyValue:mNotificationDetails.mIsFavoriteStore;
									WebServiceStaticArrays.mNotificationsResponseList.add(mNotificationDetails);
								}
								//Log.i("size", WebServiceStaticArrays.mAllNotifications.size()+" ");
								SharedPreferences mPreference = mContext.getSharedPreferences("NotificationDetailsPrefences", Context.MODE_PRIVATE);
								Editor mPrefEditor = mPreference.edit();
								mPrefEditor.putString("notification_flag", "refresh");  // No notifications available so dont change the notification flag 
								mPrefEditor.commit();
								if(notification_response_type.equalsIgnoreCase("scroll")){ //result during scroll
									for(int j=0;j<WebServiceStaticArrays.mNotificationsResponseList.size();j++){
										NotificationDetails mNotificationDetails = (NotificationDetails) WebServiceStaticArrays.mNotificationsResponseList.get(j);
										WebServiceStaticArrays.mAllNotifications.add(WebServiceStaticArrays.mAllNotifications.size(),mNotificationDetails);
									}
								}else if(notification_response_type.equalsIgnoreCase("refresh")){ // result during 
									ArrayList<Object> mTempArrayList = new ArrayList<Object>(WebServiceStaticArrays.mNotificationsResponseList);
									for(int m=0;m<mTempArrayList.size();m++){
										NotificationDetails mRecentNotificationDetails = (NotificationDetails) mTempArrayList.get(m);
										for(int n=0;n<WebServiceStaticArrays.mAllNotifications.size();n++){
											NotificationDetails mNotificationDetails = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(n);
											if(mRecentNotificationDetails.notification_type.equalsIgnoreCase("store_message") && mNotificationDetails.notification_type.equalsIgnoreCase("store_message") && mRecentNotificationDetails.notification_locationId.equalsIgnoreCase(mNotificationDetails.notification_locationId)){
												WebServiceStaticArrays.mAllNotifications.remove(n);
											}else if(mRecentNotificationDetails.notification_type.equalsIgnoreCase("customer_message") && mRecentNotificationDetails.notification_locationId.equalsIgnoreCase(mNotificationDetails.notification_locationId) && mRecentNotificationDetails.mCustomerID.equalsIgnoreCase(mNotificationDetails.mCustomerID)){
												WebServiceStaticArrays.mAllNotifications.remove(n);
											}else if(mRecentNotificationDetails.notification_type.equalsIgnoreCase("zoupons_message") && mNotificationDetails.notification_type.equalsIgnoreCase("zoupons_message")){
												WebServiceStaticArrays.mAllNotifications.remove(n);
											}
										}
									}
									for(int l=0;l<WebServiceStaticArrays.mAllNotifications.size();l++){
										NotificationDetails mNotificationDetails = (NotificationDetails) WebServiceStaticArrays.mAllNotifications.get(l);
										if(!mNotificationDetails.id.equalsIgnoreCase("")) 
											mTempArrayList.add(mTempArrayList.size(),mNotificationDetails);
									}
									WebServiceStaticArrays.mAllNotifications = new ArrayList<Object>(mTempArrayList);
									mTempArrayList.clear();
								}else{
									WebServiceStaticArrays.mAllNotifications = new ArrayList<Object>(WebServiceStaticArrays.mNotificationsResponseList); 
								}
							}else{
								//NotificationDetails.notificationcount=0;
							}
							rtnValue="success";
						}
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	/*
	 * This parsing function is used to load before customerservice webservice
	 * */
	public String parseNotifications_CustomerService(String response) {
		String rtnValue="";		
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 
			mNodes = mDoc.getElementsByTagName("notifications");				
			mChildNodes = mDoc.getElementsByTagName("notification");
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){

					for(int i=0;i<mNodes.getLength();i++){	        			
						mNodeElement = (Element) mNodes.item(i);	        			
						if(mChildNodes.getLength() != 0){        				
							WebServiceStaticArrays.mAllNotifications_CustomerService.clear();
							for(int j=0;j<mChildNodes.getLength();j++){
								NotificationDetails mNotificationDetails = new NotificationDetails();
								mChildElement = (Element) mChildNodes.item(j);
								mNotificationDetails.id=ZouponsWebService.getValue(mChildElement,"id");
								mNotificationDetails.id=mNotificationDetails.id.equals(null)?mEmptyValue:mNotificationDetails.id;
								mNotificationDetails.notify_to=ZouponsWebService.getValue(mChildElement,"notify_to");
								mNotificationDetails.notify_to=mNotificationDetails.notify_to.equals(null)?mEmptyValue:mNotificationDetails.notify_to;
								mNotificationDetails.notification_type=ZouponsWebService.getValue(mChildElement,"notification_type");
								mNotificationDetails.notification_type=mNotificationDetails.notification_type.equals(null)?mEmptyValue:mNotificationDetails.notification_type;
								mNotificationDetails.notification_msg=ZouponsWebService.getValue(mChildElement,"notification_msg");
								mNotificationDetails.notification_msg=mNotificationDetails.notification_msg.equals(null)?mEmptyValue:mNotificationDetails.notification_msg;
								mNotificationDetails.store_id=ZouponsWebService.getValue(mChildElement,"store_id");
								mNotificationDetails.store_id=mNotificationDetails.store_id.equals(null)?mEmptyValue:mNotificationDetails.store_id;
								mNotificationDetails.status=ZouponsWebService.getValue(mChildElement,"status");
								mNotificationDetails.status=mNotificationDetails.status.equals(null)?mEmptyValue:mNotificationDetails.status;
								mNotificationDetails.timeZone=ZouponsWebService.getValue(mChildElement,"time_zone");
								mNotificationDetails.timeZone=mNotificationDetails.timeZone.equals(null)?mEmptyValue:mNotificationDetails.timeZone;
								//timezone
								mNotificationDetails.created=ZouponsWebService.getValue(mChildElement,"created");
								mNotificationDetails.created=mNotificationDetails.created.equals(null)?mEmptyValue:convertTimeToDeviceTime(mNotificationDetails.created,mNotificationDetails.timeZone);
								WebServiceStaticArrays.mAllNotifications_CustomerService.add(mNotificationDetails);
							}
							rtnValue="success";
						}
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	public String parseCustomerService(String response) {
		String rtnValue="";		
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("communicated_stores");							//function to get value by its category tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){

					mParentNode = mDoc.getElementsByTagName("store");
					mParentElement = (Element) mParentNode.item(0);

					if(mParentNode!=null && mParentNode.getLength()>0){

						MainMenuActivity.mCustomerCenterTotalCount = ZouponsWebService.getValue(mParentElement, "total_count");
						MainMenuActivity.mCustomerCenterTotalCount = MainMenuActivity.mCustomerCenterTotalCount.equals(null)||MainMenuActivity.mCustomerCenterTotalCount.equals("")?"0":MainMenuActivity.mCustomerCenterTotalCount;

						for(int i=0;i<mParentNode.getLength();i++){

							ChatSupportClassVariables obj = new ChatSupportClassVariables();
							mNodeElement = (Element) mParentNode.item(i);

							if(mNodeElement.getChildNodes().getLength()>0){
								obj.mStoreId=ZouponsWebService.getValue(mNodeElement,"StoreId");
								obj.mStoreId=obj.mStoreId.equals(null)?mEmptyValue:obj.mStoreId;
								obj.mStoreName=ZouponsWebService.getValue(mNodeElement,"StoreName");
								obj.mStoreName=obj.mStoreName.equals(null)?mEmptyValue:obj.mStoreName;
								obj.mStoreLogoPath=ZouponsWebService.getValue(mNodeElement,"StoreLogoPath");
								obj.mStoreLogoPath=obj.mStoreLogoPath.equals(null)?mEmptyValue:obj.mStoreLogoPath;
								obj.mStoreLocationId=ZouponsWebService.getValue(mNodeElement,"location_id");
								obj.mStoreLocationId=obj.mStoreLocationId.equals(null)||obj.mStoreLocationId.equals("")?mEmptyValue:obj.mStoreLocationId;
								obj.mStoreAddressLine1=ZouponsWebService.getValue(mNodeElement,"address_line1");
								obj.mStoreAddressLine1=obj.mStoreAddressLine1.equals(null)||obj.mStoreAddressLine1.equals("")?mEmptyValue:obj.mStoreAddressLine1;
								obj.mStoreAddressLine2=ZouponsWebService.getValue(mNodeElement,"address_line2");
								obj.mStoreAddressLine2=obj.mStoreAddressLine2.equals(null)||obj.mStoreAddressLine2.equals("")?mEmptyValue:obj.mStoreAddressLine2;
								obj.mStoreCity=ZouponsWebService.getValue(mNodeElement,"city");
								obj.mStoreCity=obj.mStoreCity.equals(null)||obj.mStoreCity.equals("")?mEmptyValue:obj.mStoreCity;
								obj.mStoreState=ZouponsWebService.getValue(mNodeElement,"state");
								obj.mStoreState=obj.mStoreState.equals(null)||obj.mStoreState.equals("")?mEmptyValue:obj.mStoreState;
								obj.mStoreZipCode=ZouponsWebService.getValue(mNodeElement,"zip_code");
								obj.mStoreZipCode=obj.mStoreZipCode.equals(null)||obj.mStoreZipCode.equals("")?mEmptyValue:obj.mStoreZipCode;
								obj.mFavoriteStore=ZouponsWebService.getValue(mNodeElement,"favorite_store");
								obj.mFavoriteStore=obj.mFavoriteStore.equals(null)||obj.mFavoriteStore.equals("")?mEmptyValue:obj.mFavoriteStore;

								obj.mStoreLatitude=ZouponsWebService.getValue(mNodeElement,"lat");
								obj.mStoreLatitude=obj.mStoreLatitude.equals(null)||obj.mStoreLatitude.equals("")?mEmptyValue:obj.mStoreLatitude;
								obj.mStoreLongitude=ZouponsWebService.getValue(mNodeElement,"lon");
								obj.mStoreLongitude=obj.mStoreLongitude.equals(null)||obj.mStoreLongitude.equals("")?mEmptyValue:obj.mStoreLongitude;

								WebServiceStaticArrays.mCustomerService.add(obj);
							}else if(mNodeElement.getChildNodes().getLength()==1){
								obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
								obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
								WebServiceStaticArrays.mCustomerService.add(obj);
							}
						}
					}else{
						mNodes = mDoc.getElementsByTagName("communicated_stores");						//function to get value by its categories tag name
						if(mNodes!=null&&mNodes.getLength()>0){
							for(int i=0;i<mNodes.getLength();i++){
								ChatSupportClassVariables obj = new ChatSupportClassVariables();
								mNodeElement = (Element) mNodes.item(i);
								obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
								obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
								WebServiceStaticArrays.mCustomerService.add(obj);
							}
							rtnValue="success";
						}else{
							rtnValue="norecords";
						}
					}

					if(mNodes.getLength()>0){
						rtnValue="success";
					}
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	// parse import friends response...
	public String parseImportSocialFriends(String response) {
		String rtnValue="",importstatus="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("import_contacts");							//function to get value by its social login tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "failure";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						importstatus=ZouponsWebService.getValue(mNodeElement,"message");
					}
					if(importstatus.equalsIgnoreCase("Successfully Imported")){
						rtnValue = "success";
					}else if(importstatus.equalsIgnoreCase("No Contacts Available")){
						rtnValue = "no conctacts";
					}else{
						rtnValue = "import failure";
					}

				}else{
					rtnValue="failure";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing signup_stepone webservice data
	public String parseSignUpStepOne(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("signup_step_one");					//function to get value by its signup_step_one tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					WebServiceStaticArrays.mSendMobileVerificationCode.clear();
					for(int i=0;i<mNodes.getLength();i++){
						SendMobileVerfication_ClassVariables obj = new SendMobileVerfication_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mUserId=ZouponsWebService.getValue(mNodeElement,"id");
						obj.mUserId=obj.mUserId.equals(null)?mEmptyValue:obj.mUserId;
						obj.mFirstName=ZouponsWebService.getValue(mNodeElement,"first_name");
						obj.mFirstName=obj.mFirstName.equals(null)?mEmptyValue:obj.mFirstName;
						obj.mLastName=ZouponsWebService.getValue(mNodeElement,"last_name");
						obj.mLastName=obj.mLastName.equals(null)?mEmptyValue:obj.mLastName;
						obj.mProfilePicture=ZouponsWebService.getValue(mNodeElement,"profile_picture");
						obj.mProfilePicture=obj.mProfilePicture.equals(null)?mEmptyValue:obj.mProfilePicture;
						obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						WebServiceStaticArrays.mSendMobileVerificationCode.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	//function for parsing signup_stepone webservice data
	public String parseSignUpStepOne_Verify(String response) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);						 //function to get parse xml tag
			mNodes = mDoc.getElementsByTagName("signup_step_one");					//function to get value by its signup_step_one tag name
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null&&mNodes.getLength()>0){
					WebServiceStaticArrays.mSendMobileVerificationCode.clear();
					for(int i=0;i<mNodes.getLength();i++){
						SendMobileVerfication_ClassVariables obj = new SendMobileVerfication_ClassVariables();
						mNodeElement = (Element) mNodes.item(i);
						obj.mUserId=ZouponsWebService.getValue(mNodeElement,"id");
						obj.mUserId=obj.mUserId.equals(null)?mEmptyValue:obj.mUserId;
						obj.mFirstName=ZouponsWebService.getValue(mNodeElement,"first_name");
						obj.mFirstName=obj.mFirstName.equals(null)?mEmptyValue:obj.mFirstName;
						obj.mLastName=ZouponsWebService.getValue(mNodeElement,"last_name");
						obj.mLastName=obj.mLastName.equals(null)?mEmptyValue:obj.mLastName;
						obj.mProfilePicture=ZouponsWebService.getValue(mNodeElement,"profile_picture");
						obj.mProfilePicture=obj.mProfilePicture.equals(null)?mEmptyValue:obj.mProfilePicture;
						obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						WebServiceStaticArrays.mSendMobileVerificationCode.add(obj);
					}
					rtnValue="success";
				}else{
					rtnValue="norecords";
				}
			}
		}catch(Exception e){
			rtnValue="failure";
			e.printStackTrace();
		}
		return rtnValue;
	}

	public Bitmap getBitmapFromURL(String src) {
		try {
			Bitmap mBitmap;	
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(src);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(params, 60000); // 1 minute
			request.setParams(params);
			HttpResponse response = httpClient.execute(request);
			byte[] image = EntityUtils.toByteArray(response.getEntity());
			mBitmap =   BitmapFactory.decodeByteArray(image, 0,image.length);
			image = null;
			return mBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}	

	// To convert time format
	private String convertTime(String time){
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
			Date dateObj = sdf.parse(time);
			return new SimpleDateFormat("hh:mm a").format(dateObj);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return " ";
		}
	}

	// To convert time to device time 
	private String convertTimeToDeviceTime(String time,String timezone){
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone(timezone));
			Date dateObj = sdf.parse(time);
			SimpleDateFormat device_format = new SimpleDateFormat("MMM d, yyyy, h:mm a");
			device_format.setTimeZone(TimeZone.getDefault());
			return device_format.format(dateObj);
		} catch (ParseException e) {
			e.printStackTrace();
			return " ";
		}
	}


}
