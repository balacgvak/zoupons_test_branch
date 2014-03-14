package com.us.zoupons.storeowner.webservice;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.us.zoupons.classvariables.LoginUser_ClassVariables;
import com.us.zoupons.classvariables.POJOCardDetails;
import com.us.zoupons.classvariables.POJOStoreInfo;
import com.us.zoupons.classvariables.POJOUserProfile;
import com.us.zoupons.classvariables.POJOVideoURL;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.mobilepay.PaymentStatusVariables;
import com.us.zoupons.shopper.cards.StoreCardDetails;
import com.us.zoupons.shopper.invoice.POJOInvoiceList;
import com.us.zoupons.shopper.webService.ZouponsWebService;
import com.us.zoupons.storeowner.batchsales.BatchDetailsClassVariables;
import com.us.zoupons.storeowner.customercenter.FavouriteCustomerDetails;
import com.us.zoupons.storeowner.employees.EmployeePermissionClassVariables;
import com.us.zoupons.storeowner.employees.StoreEmployeesClassVariables;
import com.us.zoupons.storeowner.employees.StoreOwner_Employees;
import com.us.zoupons.storeowner.giftcards_deals.PurchaseCardDetails;
import com.us.zoupons.storeowner.locations.StoreLocationsAsyncTask;

/**
 * 
 * Generic class to parse response from webservice
 *
 */

public class StoreownerParsingclass {
	private Context mContext;
	private Document mDoc;
	private NodeList mNodes,mChildNodes,mSecondChildNodes;
	private int mRecordSize;
	private Element mNodeElement,mParentElement,mChildElement;
	private String mEmptyValue="";
	private String mIntegerEmptyValue="0";

	public StoreownerParsingclass(Context context){
		this.mContext=context;
	}

	/* To parse user details*/
	public ArrayList<Object> parseUserProfile(String mGetUserProfileResponse, String pageflag) {
		ArrayList<Object> userInfo = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetUserProfileResponse);
			if(pageflag.equalsIgnoreCase("PointOfsale")){
				mNodes = mDoc.getElementsByTagName("approve_customer_initiated_payment"); 
			}else{
				mNodes = mDoc.getElementsByTagName("userprofile");
			}
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return userInfo;
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						POJOUserProfile obj = new POJOUserProfile();
						mNodeElement = (Element) mNodes.item(i);
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
						obj.mUserImage=ZouponsWebService.getValue(mNodeElement,"UserImage");
						obj.mUserImage=obj.mUserImage.equals(null)?mEmptyValue:obj.mUserImage;
						if(obj.mUserImage.contains("")){
							obj.mUserImage = obj.mUserImage.replaceAll("\\s","%20");
						}
						obj.mUserType=ZouponsWebService.getValue(mNodeElement,"UserType");
						obj.mUserType=obj.mUserType.equals(null)?mEmptyValue:obj.mUserType;
						obj.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						obj.mStatus=ZouponsWebService.getValue(mNodeElement,"Status");
						obj.mStatus=obj.mStatus.equals(null)?mEmptyValue:obj.mStatus;
						userInfo.add(obj);
					}
					return userInfo;
				}else{
					return null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/* To parse add store coupon result*/
	public String parseAddCoupon(String mCouponResponse, String flag) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		String response_status="";
		try{
			mDoc = zouponswebservice.XMLfromString(mCouponResponse);						
			mNodes = mDoc.getElementsByTagName("create_coupon");			
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				return "no records";
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()==1){
							if(flag.equalsIgnoreCase("generate_qrcode")){
								response_status=ZouponsWebService.getValue(mNodeElement,"bar_code");
								response_status=response_status.equals(null)?response_status:response_status;	
							}else{
								response_status=ZouponsWebService.getValue(mNodeElement,"message");
								response_status=response_status.equals(null)?response_status:response_status;
							}

						}
					}
					return response_status;
				}else{
					return "no records";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return "failure";
		}
	}

	/* To parse store location details response*/
	public ArrayList<Object> parseStoreLocations(String location_response) {
		ArrayList<Object> storeLocations = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(location_response);
			mNodes = mDoc.getElementsByTagName("stores");	
			mChildNodes = mDoc.getElementsByTagName("store");
			mRecordSize = zouponswebservice.numResults(mDoc);
			if(mRecordSize<=0){
				return storeLocations;
			}else{
				if(mNodes!=null){
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						return storeLocations;
					}else{
						if(mChildNodes != null){
							for(int j=0;j<mChildNodes.getLength();j++){	
								POJOStoreInfo obj = new POJOStoreInfo();
								mNodeElement = (Element) mChildNodes.item(j);
								obj.store_id=ZouponsWebService.getValue(mNodeElement,"id");
								obj.store_id=obj.store_id.equals(null)?mEmptyValue:obj.store_id;
								obj.store_name=ZouponsWebService.getValue(mNodeElement,"store_name");
								obj.store_name=obj.store_name.equals(null)?mEmptyValue:obj.store_name;
								obj.address_line1=ZouponsWebService.getValue(mNodeElement,"address_line1");
								obj.address_line1=obj.address_line1.equals(null)?mEmptyValue:obj.address_line1;
								obj.city=ZouponsWebService.getValue(mNodeElement,"city");
								obj.city=obj.city.equals(null)?mEmptyValue:obj.city;
								obj.state=ZouponsWebService.getValue(mNodeElement,"state");
								obj.state=obj.state.equals(null)?mEmptyValue:obj.state;
								obj.country=ZouponsWebService.getValue(mNodeElement,"country");
								obj.country=obj.country.equals(null)?mEmptyValue:obj.country;
								obj.zip_code=ZouponsWebService.getValue(mNodeElement,"zip_code");
								obj.zip_code=obj.zip_code.equals(null)?mEmptyValue:obj.zip_code;
								obj.qr_code=ZouponsWebService.getValue(mNodeElement,"qr_code");
								obj.qr_code=obj.qr_code.equals(null)?mEmptyValue:obj.qr_code;
								obj.location_id=ZouponsWebService.getValue(mNodeElement,"location_id");
								obj.location_id=obj.location_id.equals(null)?mEmptyValue:obj.location_id;
								obj.status=ZouponsWebService.getValue(mNodeElement,"status");
								obj.status=obj.status.equals(null)?mEmptyValue:obj.status;
								obj.address_line2=ZouponsWebService.getValue(mNodeElement,"address_line2");
								obj.address_line2=obj.address_line2.equals(null)?mEmptyValue:obj.address_line2;
								obj.phone=ZouponsWebService.getValue(mNodeElement,"phone");
								obj.phone=obj.phone.equals(null)?mEmptyValue:obj.phone;
								storeLocations.add(obj);
							}
							return storeLocations;
						}else{
							return storeLocations;
						}
					}
				}else{
					return storeLocations;
				}
			}
		}catch (Exception e) {
			return null;
		}
	}

	/* To parse purchased gc/dc list response */
	public ArrayList<Object> parsePurchased_gc_dc_response(	String mResponseStatus) {
		ArrayList<Object> purchased_gc_dc_list = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mResponseStatus);
			mNodes = mDoc.getElementsByTagName("purchased_gc_dcs");	
			mChildNodes = mDoc.getElementsByTagName("purchased_gc_dc");
			mRecordSize = zouponswebservice.numResults(mDoc);
			if(mRecordSize<=0){
				return purchased_gc_dc_list;
			}else{
				if(mNodes!=null){
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						return purchased_gc_dc_list;
					}else{
						if(mChildNodes != null){
							for(int j=0;j<mChildNodes.getLength();j++){	
								PurchaseCardDetails mPurchasedCard =  new PurchaseCardDetails();
								mNodeElement = (Element) mChildNodes.item(j);
								mPurchasedCard.purchase_card_id =ZouponsWebService.getValue(mNodeElement,"giftcard_purchased_id");
								mPurchasedCard.purchase_card_id = mPurchasedCard.purchase_card_id.equals(null)?mEmptyValue:mPurchasedCard.purchase_card_id;
								mPurchasedCard.card_id =ZouponsWebService.getValue(mNodeElement,"giftcard_id");
								mPurchasedCard.card_id = mPurchasedCard.card_id.equals(null)?mEmptyValue:mPurchasedCard.card_id;
								mPurchasedCard.customer_id =ZouponsWebService.getValue(mNodeElement,"user_id");
								mPurchasedCard.customer_id = mPurchasedCard.customer_id.equals(null)?mEmptyValue:mPurchasedCard.customer_id;
								mPurchasedCard.customer_first_name =ZouponsWebService.getValue(mNodeElement,"first_name");
								mPurchasedCard.customer_first_name = mPurchasedCard.customer_first_name.equals(null)?mEmptyValue:mPurchasedCard.customer_first_name;
								mPurchasedCard.customer_last_name =ZouponsWebService.getValue(mNodeElement,"last_name");
								mPurchasedCard.customer_last_name = mPurchasedCard.customer_last_name.equals(null)?mEmptyValue:mPurchasedCard.customer_last_name;
								mPurchasedCard.face_value =ZouponsWebService.getValue(mNodeElement,"face_value");
								mPurchasedCard.face_value = (mPurchasedCard.face_value.equals(null) || mPurchasedCard.face_value.equals(""))?mIntegerEmptyValue:mPurchasedCard.face_value;
								mPurchasedCard.remaining_balance =ZouponsWebService.getValue(mNodeElement,"balance");
								mPurchasedCard.remaining_balance = (mPurchasedCard.remaining_balance.equals(null) || mPurchasedCard.remaining_balance.equals(""))?mIntegerEmptyValue:mPurchasedCard.remaining_balance;
								purchased_gc_dc_list.add(mPurchasedCard);
							}
							return purchased_gc_dc_list;
						}else{
							return purchased_gc_dc_list;
						}
					}
				}else{
					return purchased_gc_dc_list;
				}
			}
		}catch (Exception e) {
			return null;
		}
	}

	/* To parse payment response */
	public String parsePaymentResponse(String mresponse) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		String response_status="";
		PaymentStatusVariables.Customer_Reference = "";
		PaymentStatusVariables.Authorization_Number = "";
		PaymentStatusVariables.Bank_Message = "";
		PaymentStatusVariables.message = "";
		PaymentStatusVariables.purchase_message = "";
		try{
			mDoc = zouponswebservice.XMLfromString(mresponse);      
			mNodes = mDoc.getElementsByTagName("approve_customer_initiated_payment");   
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				response_status = "failure";
			}else{
				if(mNodes!=null){
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
					response_status = "success";
				}else{
					response_status="failure";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			response_status = "failure";
		}
		return response_status;
	}

	/* To parse store employee list response */
	public String parseStoreEmployees(String mGetResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("store_employees");						
			mChildNodes = mDoc.getElementsByTagName("store_employee");
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null){
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						rtnValue = "norecords";
					}else{
						rtnValue = "success";
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								mParentElement = (Element) mNodes.item(0);
								StoreOwner_Employees.sEmployeeTotalCount = ZouponsWebService.getValue(mParentElement,"total_count");
								StoreOwner_Employees.sEmployeeTotalCount = StoreOwner_Employees.sEmployeeTotalCount.equals(null)?"0":StoreOwner_Employees.sEmployeeTotalCount;
								for(int j=0;j<mChildNodes.getLength();j++){	
									StoreEmployeesClassVariables employee_details = new StoreEmployeesClassVariables();
									mNodeElement = (Element) mChildNodes.item(j);
									employee_details.mEmployeeId=ZouponsWebService.getValue(mNodeElement,"id");
									employee_details.mEmployeeId=employee_details.mEmployeeId.equals(null)?mEmptyValue:employee_details.mEmployeeId;
									employee_details.mEmployeeName=ZouponsWebService.getValue(mNodeElement,"user_name");
									employee_details.mEmployeeName=employee_details.mEmployeeName.equals(null)?mEmptyValue:employee_details.mEmployeeName;
									employee_details.mEmployeeFirstName=ZouponsWebService.getValue(mNodeElement,"first_name");
									employee_details.mEmployeeFirstName=employee_details.mEmployeeFirstName.equals(null)?mEmptyValue:employee_details.mEmployeeFirstName;
									employee_details.mEmployeeLastName=ZouponsWebService.getValue(mNodeElement,"last_name");
									employee_details.mEmployeeLastName=employee_details.mEmployeeLastName.equals(null)?mEmptyValue:employee_details.mEmployeeLastName;
									employee_details.mEmployeeProfileImage=ZouponsWebService.getValue(mNodeElement,"profile_picture");
									employee_details.mEmployeeProfileImage=employee_details.mEmployeeProfileImage.equals(null)?mEmptyValue:employee_details.mEmployeeProfileImage;
									if(employee_details.mEmployeeProfileImage.contains("")){
										employee_details.mEmployeeProfileImage = employee_details.mEmployeeProfileImage.replaceAll("\\s","%20");
									}
									employee_details.mMobileNumber=ZouponsWebService.getValue(mNodeElement,"telephone_number");
									employee_details.mMobileNumber=employee_details.mMobileNumber.equals(null)?mEmptyValue:employee_details.mMobileNumber;
									employee_details.mEmailAddress=ZouponsWebService.getValue(mNodeElement,"email");
									employee_details.mEmailAddress=employee_details.mEmailAddress.equals(null)?mEmptyValue:employee_details.mEmailAddress;
									WebServiceStaticArrays.StoreEmloyeesList.add(employee_details);
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
	
	/* To parse verrification of employee code response */
	public ArrayList<Object> parseVerifyEmployeeCode(String response) {
		ArrayList<Object> mVerifyResult = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("add_employee");	
			mChildNodes = mDoc.getElementsByTagName("store_module");
			mRecordSize = zouponswebservice.numResults(mDoc);
			if(mRecordSize<=0){
				return mVerifyResult;
			}else{
				if(mNodes!=null){
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						EmployeePermissionClassVariables mAddEmployee = new EmployeePermissionClassVariables();
						mNodeElement = (Element) mNodes.item(0);
						mAddEmployee.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						mAddEmployee.mMessage=mAddEmployee.mMessage.equals(null)?mEmptyValue:mAddEmployee.mMessage;
						mVerifyResult.add(mAddEmployee);
						return mVerifyResult;
					}else{
						if(mChildNodes != null){
							for(int j=0;j<mChildNodes.getLength();j++){	
								EmployeePermissionClassVariables mAddEmployee = new EmployeePermissionClassVariables();
								mNodeElement = (Element) mChildNodes.item(j);
								mAddEmployee.mPermissionid=ZouponsWebService.getValue(mNodeElement,"id");
								mAddEmployee.mPermissionid=mAddEmployee.mPermissionid.equals(null)?mEmptyValue:mAddEmployee.mPermissionid;
								mAddEmployee.mPermissionName=ZouponsWebService.getValue(mNodeElement,"name");
								mAddEmployee.mPermissionName=mAddEmployee.mPermissionName.equals(null)?mEmptyValue:mAddEmployee.mPermissionName;
								mAddEmployee.mPermissionStatus=ZouponsWebService.getValue(mNodeElement,"status");
								mAddEmployee.mPermissionStatus=mAddEmployee.mPermissionStatus.equals(null)?mEmptyValue:mAddEmployee.mPermissionStatus;
								mAddEmployee.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
								mAddEmployee.mMessage=mAddEmployee.mMessage.equals(null)?mEmptyValue:mAddEmployee.mMessage;
								mVerifyResult.add(mAddEmployee);
							}
							return mVerifyResult;
						}else{
							return mVerifyResult;
						}
					}
				}else{
					return mVerifyResult;
				}
			}
		}catch (Exception e) {
			return null;
		}
	}

	/* To parse employee activation response */
	public ArrayList<Object> parseActivateEmployee(String Response) {
		ArrayList<Object> mActivateEmployeeResult = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(Response);
			mNodes = mDoc.getElementsByTagName("add_employee"); 
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return mActivateEmployeeResult;
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						String message=ZouponsWebService.getValue(mNodeElement,"message");
						message=message.equals(null)?mEmptyValue:message;
						mActivateEmployeeResult.add(message);
					}
					return mActivateEmployeeResult;
				}else{
					return null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/* To parse add store location response */
	public ArrayList<Object> parseAddStoreLocations(String mAddResponse) {
		ArrayList<Object> mAddLocationsResponse = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mAddResponse);
			mNodes = mDoc.getElementsByTagName("stores"); 
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return mAddLocationsResponse;
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						String message=ZouponsWebService.getValue(mNodeElement,"message");
						message=message.equals(null)?mEmptyValue:message;
						mAddLocationsResponse.add(message);
					}
					return mAddLocationsResponse;
				}else{
					return null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/* To parse employee permission response */
	public ArrayList<Object> parseEmployeePermisssions(String response) {
		ArrayList<Object> mEmployeePermissionsList = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("store_permissions");	
			mChildNodes = mDoc.getElementsByTagName("store_module");
			mSecondChildNodes = mDoc.getElementsByTagName("store_location");
			mRecordSize = zouponswebservice.numResults(mDoc);
			if(mRecordSize<=0){
				return mEmployeePermissionsList;
			}else{
				if(mNodes!=null){
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						EmployeePermissionClassVariables mAddEmployee = new EmployeePermissionClassVariables();
						mNodeElement = (Element) mNodes.item(0);
						mAddEmployee.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
						mAddEmployee.mMessage=mAddEmployee.mMessage.equals(null)?mEmptyValue:mAddEmployee.mMessage;
						mEmployeePermissionsList.add(mAddEmployee);
						return mEmployeePermissionsList;
					}else{

						if(mChildNodes != null && mSecondChildNodes !=null){
							for(int j=0;j<mChildNodes.getLength();j++){	
								EmployeePermissionClassVariables mAddEmployee = new EmployeePermissionClassVariables();
								mNodeElement = (Element) mChildNodes.item(j);
								mAddEmployee.mPermissionid=ZouponsWebService.getValue(mNodeElement,"id");
								mAddEmployee.mPermissionid=mAddEmployee.mPermissionid.equals(null)?mEmptyValue:mAddEmployee.mPermissionid;
								mAddEmployee.mPermissionName=ZouponsWebService.getValue(mNodeElement,"name");
								mAddEmployee.mPermissionName=mAddEmployee.mPermissionName.equals(null)?mEmptyValue:mAddEmployee.mPermissionName;
								mAddEmployee.mPermissionStatus=ZouponsWebService.getValue(mNodeElement,"status");
								mAddEmployee.mPermissionStatus=mAddEmployee.mPermissionStatus.equals(null)?mEmptyValue:mAddEmployee.mPermissionStatus;
								mAddEmployee.mMessage=ZouponsWebService.getValue(mNodeElement,"message");
								mAddEmployee.mMessage=mAddEmployee.mMessage.equals(null)?mEmptyValue:mAddEmployee.mMessage;
								mEmployeePermissionsList.add(mAddEmployee);
							}
							EmployeePermissionClassVariables.sStorePermissionTotalIds=mChildNodes.getLength();
							mNodeElement = (Element) mNodes.item(0);
							EmployeePermissionClassVariables.sStorePermissionIds=ZouponsWebService.getValue(mNodeElement,"permission_ids");
							EmployeePermissionClassVariables.sStorePermissionIds=EmployeePermissionClassVariables.sStorePermissionIds.equals(null)?mEmptyValue:EmployeePermissionClassVariables.sStorePermissionIds;
							EmployeePermissionClassVariables.sLocationPermissionIds=ZouponsWebService.getValue(mNodeElement,"location_ids");
							EmployeePermissionClassVariables.sLocationPermissionIds=EmployeePermissionClassVariables.sLocationPermissionIds.equals(null)?mEmptyValue:EmployeePermissionClassVariables.sLocationPermissionIds;
							for(int j=0;j<mSecondChildNodes.getLength();j++){	
								EmployeePermissionClassVariables mAddEmployee = new EmployeePermissionClassVariables();
								mNodeElement = (Element) mSecondChildNodes.item(j);
								mAddEmployee.mLocationPermissionaddress_line1=ZouponsWebService.getValue(mNodeElement,"address_line1");
								mAddEmployee.mLocationPermissionaddress_line1=mAddEmployee.mLocationPermissionaddress_line1.equals(null)?mEmptyValue:mAddEmployee.mLocationPermissionaddress_line1;
								mAddEmployee.mLocationPermissioncity=ZouponsWebService.getValue(mNodeElement,"city");
								mAddEmployee.mLocationPermissioncity=mAddEmployee.mLocationPermissioncity.equals(null)?mEmptyValue:mAddEmployee.mLocationPermissioncity;
								mAddEmployee.mLocationPermissionstate=ZouponsWebService.getValue(mNodeElement,"state");
								mAddEmployee.mLocationPermissionstate=mAddEmployee.mLocationPermissionstate.equals(null)?mEmptyValue:mAddEmployee.mLocationPermissionstate;
								mAddEmployee.mLocationPermissioncountry=ZouponsWebService.getValue(mNodeElement,"country");
								mAddEmployee.mLocationPermissioncountry=mAddEmployee.mLocationPermissioncountry.equals(null)?mEmptyValue:mAddEmployee.mLocationPermissioncountry;
								mAddEmployee.mLocationPermissionzip_code=ZouponsWebService.getValue(mNodeElement,"zip_code");
								mAddEmployee.mLocationPermissionzip_code=mAddEmployee.mLocationPermissionzip_code.equals(null)?mEmptyValue:mAddEmployee.mLocationPermissionzip_code;
								mAddEmployee.mLocationPermissionId=ZouponsWebService.getValue(mNodeElement,"location_id");
								mAddEmployee.mLocationPermissionId=mAddEmployee.mLocationPermissionId.equals(null)?mEmptyValue:mAddEmployee.mLocationPermissionId;
								mEmployeePermissionsList.add(mAddEmployee);
							}
							return mEmployeePermissionsList;
						}else{
							return mEmployeePermissionsList;
						}

					}
				}else{
					return mEmployeePermissionsList;
				}
			}
		}catch (Exception e) {
			return null;
		}

	}

	/* To parse inactivatation of employee response */
	public String parseInactivateEmployee(String response) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		String response_status="";
		try{
			mDoc = zouponswebservice.XMLfromString(response);						
			mNodes = mDoc.getElementsByTagName("inactivate_user");			
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				return "no records";
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()==1){
							response_status=ZouponsWebService.getValue(mNodeElement,"message");
							response_status=response_status.equals(null)?response_status:response_status;
						}
					}
					return response_status;
				}else{
					return "no records";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return "failure";
		}
	}

	/* To parse address validation response */
	public String parseValidateAddress(String response) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		String response_status="",status="",location_type="";
		try{
			mDoc = zouponswebservice.XMLfromString(response);						
			mNodes = mDoc.getElementsByTagName("GeocodeResponse");			
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				return "no records";
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						status = ZouponsWebService.getValue(mNodeElement,"status");
						status=status.equals(null)?mEmptyValue:status;
						location_type = ZouponsWebService.getValue(mNodeElement,"location_type");
						location_type=location_type.equals(null)?mEmptyValue:location_type;
						StoreLocationsAsyncTask.latitude = ZouponsWebService.getValue(mNodeElement,"lat");
						StoreLocationsAsyncTask.latitude=StoreLocationsAsyncTask.latitude.equals(null)?mEmptyValue:StoreLocationsAsyncTask.latitude;
						StoreLocationsAsyncTask.longitude = ZouponsWebService.getValue(mNodeElement,"lng");
						StoreLocationsAsyncTask.longitude = StoreLocationsAsyncTask.longitude.equals(null)?mEmptyValue:StoreLocationsAsyncTask.longitude;
					}
					if(!status.equalsIgnoreCase("") && status.equalsIgnoreCase("OK") && !location_type.equalsIgnoreCase("APPROXIMATE")){
						response_status = "success";
					}else{
						response_status = "failure";
					}
					return response_status;
				}else{
					return "no records";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return "failure";
		}
	}

	/* To parse store location status change response */
	public ArrayList<Object> parseLocationStatusChangeResponse(String Response) {
		ArrayList<Object> mLocationStatusChangeResult = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(Response);
			mNodes = mDoc.getElementsByTagName("store_location_status"); 
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return mLocationStatusChangeResult;
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						String message=ZouponsWebService.getValue(mNodeElement,"message");
						message=message.equals(null)?mEmptyValue:message;
						mLocationStatusChangeResult.add(message);
					}
					return mLocationStatusChangeResult;
				}else{
					return null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/* To parse raise invoice to customer status */
	public String parseRaiseInvoice(String mResponse) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		String response_status="";
		try{
			mDoc = zouponswebservice.XMLfromString(mResponse);						
			mNodes = mDoc.getElementsByTagName("raise_invoice");			
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				return "no records";
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()==1){
							response_status=ZouponsWebService.getValue(mNodeElement,"message");
							response_status=response_status.equals(null)?response_status:response_status;	
						}
					}
					return response_status;
				}else{
					return "no records";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return "failure";
		}
	}

	/* To parse invoice list */
	public ArrayList<Object> parseInvoiceList(String location_response) {
		ArrayList<Object> invoiceList = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(location_response);
			mNodes = mDoc.getElementsByTagName("invoices");	
			mChildNodes = mDoc.getElementsByTagName("invoice");
			mRecordSize = zouponswebservice.numResults(mDoc);
			if(mRecordSize<=0){
				return invoiceList;
			}else{
				if(mNodes!=null){
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						POJOInvoiceList obj = new POJOInvoiceList();
						mNodeElement = (Element) mNodes.item(0);
						obj.message=ZouponsWebService.getValue(mNodeElement,"message");
						obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
						invoiceList.add(obj);
						return invoiceList;
					}else{
						for(int i=0;i<mNodes.getLength();){
							mNodeElement = (Element) mNodes.item(i);	
							String mTimeZone = ZouponsWebService.getValue(mNodeElement,"time_zone");
							mTimeZone=mTimeZone.equals(null)?mEmptyValue:mTimeZone;
							Log.i("timezone", "Timezone is" + mTimeZone);
							if(mChildNodes != null){
								for(int j=0;j<mChildNodes.getLength();j++){	
									POJOInvoiceList obj = new POJOInvoiceList();
									mNodeElement = (Element) mChildNodes.item(j);
									obj.InvoiceId=ZouponsWebService.getValue(mNodeElement,"id");
									obj.InvoiceId=obj.InvoiceId.equals(null)?mEmptyValue:obj.InvoiceId;
									obj.Customer_Image=ZouponsWebService.getValue(mNodeElement,"profile_picture");
									obj.Customer_Image=obj.Customer_Image.equals(null)?mEmptyValue:obj.Customer_Image;
									if(obj.Customer_Image.contains("")){
										obj.Customer_Image = obj.Customer_Image.replaceAll("\\s","%20");
									}
									obj.Customer_Name=ZouponsWebService.getValue(mNodeElement,"user_name");
									obj.Customer_Name=obj.Customer_Name.equals(null)?mEmptyValue:obj.Customer_Name;
									obj.Customer_FirstName=ZouponsWebService.getValue(mNodeElement,"first_name");
									obj.Customer_FirstName=obj.Customer_FirstName.equals(null)?mEmptyValue:obj.Customer_FirstName;
									obj.Customer_LastName=ZouponsWebService.getValue(mNodeElement,"last_name");
									obj.Customer_LastName=obj.Customer_LastName.equals(null)?mEmptyValue:obj.Customer_LastName;
									obj.Amount=ZouponsWebService.getValue(mNodeElement,"amount");
									obj.Amount=obj.Amount.equals(null)?mEmptyValue:obj.Amount;
									obj.CreatedDate=ZouponsWebService.getValue(mNodeElement,"created");
									obj.CreatedDate=obj.CreatedDate.equals(null)||obj.CreatedDate.equalsIgnoreCase("")?mEmptyValue:convertTimeToDeviceTime(obj.CreatedDate,mTimeZone);
									obj.Days=ZouponsWebService.getValue(mNodeElement,"days");
									obj.Days=obj.Days.equals(null)?mEmptyValue:obj.Days;
									obj.Status=ZouponsWebService.getValue(mNodeElement,"status");
									obj.Status=obj.Status.equals(null)?mEmptyValue:obj.Status;
									obj.StoreNote=ZouponsWebService.getValue(mNodeElement,"store_note");
									obj.StoreNote=obj.StoreNote.equals(null)?mEmptyValue:obj.StoreNote; 
									obj.CustomerNote=ZouponsWebService.getValue(mNodeElement,"customer_note");
									obj.CustomerNote=obj.CustomerNote.equals(null)?mEmptyValue:obj.CustomerNote;
									obj.ActualAmount=ZouponsWebService.getValue(mNodeElement,"actual_amount");
									obj.ActualAmount=obj.ActualAmount.equals(null)?mEmptyValue:obj.ActualAmount;
									obj.TipAmount=ZouponsWebService.getValue(mNodeElement,"tip_amount");
									obj.TipAmount=obj.TipAmount.equals(null)?mEmptyValue:obj.TipAmount;
									obj.TotalAmount=ZouponsWebService.getValue(mNodeElement,"total_amount");
									obj.TotalAmount=obj.TotalAmount.equals(null)?mEmptyValue:obj.TotalAmount;
									obj.TransactionDate=ZouponsWebService.getValue(mNodeElement,"transaction_date");
									obj.TransactionDate=obj.TransactionDate.equals(null)||obj.TransactionDate.equalsIgnoreCase("")?mEmptyValue:convertTimeToDeviceTime(obj.TransactionDate,mTimeZone);
									obj.TransactionType=ZouponsWebService.getValue(mNodeElement,"transaction_type");
									obj.TransactionType=obj.TransactionType.equals(null)?mEmptyValue:obj.TransactionType;
									obj.CreditCardMask=ZouponsWebService.getValue(mNodeElement,"card_mask");
									obj.CreditCardMask=obj.CreditCardMask.equals(null)?mEmptyValue:obj.CreditCardMask;
									obj.GiftcardFaceValue=ZouponsWebService.getValue(mNodeElement,"face_value");
									obj.GiftcardFaceValue=obj.GiftcardFaceValue.equals(null)?mEmptyValue:obj.GiftcardFaceValue;
									obj.CreditCardAmount=ZouponsWebService.getValue(mNodeElement,"amount_creditcard");
									obj.CreditCardAmount=obj.CreditCardAmount.equals(null)?mEmptyValue:obj.CreditCardAmount;
									obj.GiftcardAmount=ZouponsWebService.getValue(mNodeElement,"amount_giftcard");
									obj.GiftcardAmount=obj.GiftcardAmount.equals(null)?mEmptyValue:obj.GiftcardAmount;
									invoiceList.add(obj);
								}
								return invoiceList;
							}else{
								return invoiceList;
							}
						}
						return invoiceList;
					}
				}else{
					return invoiceList;
				}
			}
		}catch (Exception e) {
			return null;
		}
	}

	// To parse activate card response
	public ArrayList<Object> parseActivateCreditCardResponse(String Response) {
		ArrayList<Object> mActivateCreditCardResponseResult = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(Response);
			mNodes = mDoc.getElementsByTagName("card_on_file"); 
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return mActivateCreditCardResponseResult;
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						String message=ZouponsWebService.getValue(mNodeElement,"message");
						message=message.equals(null)?mEmptyValue:message;
						mActivateCreditCardResponseResult.add(message);
					}
					return mActivateCreditCardResponseResult;
				}else{
					return null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/* To download bitmap from specified URL */
	public Bitmap getBitmapFromURL(String url) {
		try {
			Bitmap mBitmap;	
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
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

	/* To parse store information updation reponse */
	public ArrayList<Object> parseupdateStoreDetails(String mResponse, String event_flag) {
		ArrayList<Object> mStoreDetailsUpdateResult = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mResponse);
			if(event_flag.equalsIgnoreCase("set_timings")){
				mNodes = mDoc.getElementsByTagName("store_business_hours");
			}else{
				mNodes = mDoc.getElementsByTagName("store_details");	
			}
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return mStoreDetailsUpdateResult;
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						String message=ZouponsWebService.getValue(mNodeElement,"message");
						message=message.equals(null)?mEmptyValue:message;
						mStoreDetailsUpdateResult.add(message);
					}
					return mStoreDetailsUpdateResult;
				}else{
					return null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	// Parse favourite customer details
	public ArrayList<Object> parseFavoriteCustomerDetails(String response) {
		ArrayList<Object> mFavouriteCustomerResult = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("store_favorite_users");	
			mChildNodes = mDoc.getElementsByTagName("store_favorite_user");
			mRecordSize = zouponswebservice.numResults(mDoc);
			if(mRecordSize<=0){
				return mFavouriteCustomerResult;
			}else{
				if(mNodes!=null){
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						return mFavouriteCustomerResult;
					}else{
						if(mChildNodes != null){
							for(int j=0;j<mChildNodes.getLength();j++){	
								FavouriteCustomerDetails mFavouriteCustomer = new FavouriteCustomerDetails();
								mNodeElement = (Element) mChildNodes.item(j);
								mFavouriteCustomer.mCustomerId=ZouponsWebService.getValue(mNodeElement,"id");
								mFavouriteCustomer.mCustomerId=mFavouriteCustomer.mCustomerId.equals(null)?mEmptyValue:mFavouriteCustomer.mCustomerId;
								mFavouriteCustomer.mCustomerName=ZouponsWebService.getValue(mNodeElement,"user_name");
								mFavouriteCustomer.mCustomerName=mFavouriteCustomer.mCustomerName.equals(null)?mEmptyValue:mFavouriteCustomer.mCustomerName;
								mFavouriteCustomer.mCustomerFirstName=ZouponsWebService.getValue(mNodeElement,"first_name");
								mFavouriteCustomer.mCustomerFirstName=mFavouriteCustomer.mCustomerFirstName.equals(null)?mEmptyValue:mFavouriteCustomer.mCustomerFirstName;
								mFavouriteCustomer.mCustomerLastName=ZouponsWebService.getValue(mNodeElement,"last_name");
								mFavouriteCustomer.mCustomerLastName=mFavouriteCustomer.mCustomerLastName.equals(null)?mEmptyValue:mFavouriteCustomer.mCustomerLastName;
								mFavouriteCustomer.mCustomerProfileImage=ZouponsWebService.getValue(mNodeElement,"profile_picture");
								mFavouriteCustomer.mCustomerProfileImage=mFavouriteCustomer.mCustomerProfileImage.equals(null)?mEmptyValue:mFavouriteCustomer.mCustomerProfileImage;
								if(mFavouriteCustomer.mCustomerProfileImage.contains("")){
									mFavouriteCustomer.mCustomerProfileImage = mFavouriteCustomer.mCustomerProfileImage.replaceAll("\\s","%20");
								}
								mFavouriteCustomer.mIsFavouriteStoreRemoved=ZouponsWebService.getValue(mNodeElement,"favorite");
								mFavouriteCustomer.mIsFavouriteStoreRemoved=mFavouriteCustomer.mIsFavouriteStoreRemoved.equals(null)?mEmptyValue:mFavouriteCustomer.mIsFavouriteStoreRemoved;
								mFavouriteCustomer.mTransactionAmount=ZouponsWebService.getValue(mNodeElement,"transaction_amount");
								mFavouriteCustomer.mTransactionAmount=mFavouriteCustomer.mTransactionAmount.equals(null) || mFavouriteCustomer.mTransactionAmount.equals("")?mIntegerEmptyValue:mFavouriteCustomer.mTransactionAmount;
								mFavouriteCustomerResult.add(mFavouriteCustomer);
							}
							return mFavouriteCustomerResult;
						}else{
							return mFavouriteCustomerResult;
						}
					}
				}else{
					return mFavouriteCustomerResult;
				}
			}
		}catch (Exception e) {
			return null;
		}
	}

	/* To parse add favorite user to customer list response */
	public ArrayList<Object> parseAddFavouriteUser(String mResponse) {
		ArrayList<Object> mAddUserDetails = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mResponse);
			mNodes = mDoc.getElementsByTagName("add_user");	
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return mAddUserDetails;
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						String message=ZouponsWebService.getValue(mNodeElement,"message");
						message=message.equals(null)?mEmptyValue:message;
						mAddUserDetails.add(message);
					}
					return mAddUserDetails;
				}else{
					return null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	// Parse store permissions
	public ArrayList<Object> parseStorePermissions(String mResponse) {
		ArrayList<Object> mPermissionDetails = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mResponse);
			mNodes = mDoc.getElementsByTagName("store_user_permissions");	
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return mPermissionDetails;
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						LoginUser_ClassVariables obj = new LoginUser_ClassVariables();
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
						mPermissionDetails.add(obj);
					}
					return mPermissionDetails;
				}else{
					return null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/* To parse send email status */
	public String parseSendMailResponse(String mEmailResponse) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		String response_status="";
		try{
			mDoc = zouponswebservice.XMLfromString(mEmailResponse);						
			mNodes = mDoc.getElementsByTagName("share_store");			
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				return "no records";
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()==1){
							response_status=ZouponsWebService.getValue(mNodeElement,"message");
							response_status=response_status.equals(null)?response_status:response_status;
						}else{
							response_status = "failure";
						}
					}
					return response_status;
				}else{
					return "no records";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return "failure";
		}
	}

	/* To parse store location videos status */
	public ArrayList<Object> parseStoreVideos(String mResponse) {
		ArrayList<Object> mAllStoreVideos = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mResponse);
			mNodes = mDoc.getElementsByTagName("videos");	
			mChildNodes = mDoc.getElementsByTagName("video");
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return mAllStoreVideos;
			}else{
				if(mNodes!=null){
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						return mAllStoreVideos;
					}else{
						for(int i=0;i<mChildNodes.getLength();i++){
							POJOVideoURL obj = new POJOVideoURL();
							mNodeElement = (Element) mChildNodes.item(i);		        		
							obj.VideoId = ZouponsWebService.getValue(mNodeElement,"id");
							obj.VideoId=obj.VideoId.equals(null)?mEmptyValue:obj.VideoId;	
							obj.VideoTitle = ZouponsWebService.getValue(mNodeElement,"title");
							obj.VideoTitle=obj.VideoTitle.equals(null)?mEmptyValue:obj.VideoTitle;
							obj.VideoURL = ZouponsWebService.getValue(mNodeElement,"url");
							obj.VideoURL=obj.VideoURL.equals(null)?mEmptyValue:obj.VideoURL;
							obj.VideoThumbNail = ZouponsWebService.getValue(mNodeElement,"thumbnail");
							obj.VideoThumbNail=obj.VideoThumbNail.equals(null)?mEmptyValue:obj.VideoThumbNail;
							mAllStoreVideos.add(obj);
						}	
						return mAllStoreVideos;
					}
				}else{
					return null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/* To parse uploading video status */
	public String parseUploadVideoStatus(String videoresponse) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		String response_status="";
		try{
			mDoc = zouponswebservice.XMLfromString(videoresponse);						
			mNodes = mDoc.getElementsByTagName("stores");			
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				return "no records";
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()==1){
							response_status=ZouponsWebService.getValue(mNodeElement,"message");
							response_status=response_status.equals(null)?response_status:response_status;
						}else{
							response_status = "failure";
						}
					}
					return response_status;
				}else{
					return "no records";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return "failure";
		}
	}

	/* To parse store deal cards response */
	public String parseStoreDeals(String mGetResponse,String event_flag) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("deals");						
			mChildNodes = mDoc.getElementsByTagName("deal");
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				rtnValue = "norecords";
			}else{
				if(mNodes!=null){
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						if(event_flag.equalsIgnoreCase("list"))
							rtnValue = "norecords";
						else{
							mChildElement = (Element) mNodes.item(0);
							String message = ZouponsWebService.getValue(mChildElement,"message");
							message =message.equals(null)?mEmptyValue:message;
							return message;
						}

					}else{
						rtnValue = "success";
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								mParentElement = (Element) mNodes.item(0);
								StoreCardDetails.mDealTotalCount = ZouponsWebService.getValue(mParentElement,"total_count");
								StoreCardDetails.mDealTotalCount = StoreCardDetails.mDealTotalCount.equals(null)?"0":StoreCardDetails.mDealTotalCount;
								for(int j=0;j<mChildNodes.getLength();j++){	
									POJOCardDetails mCardDetail = new POJOCardDetails();
									mChildElement = (Element) mChildNodes.item(j);
									mCardDetail.id = ZouponsWebService.getValue(mChildElement,"id");
									mCardDetail.id =mCardDetail.id.equals(null)?mEmptyValue:mCardDetail.id;
									mCardDetail.facevalue = ZouponsWebService.getValue(mChildElement,"face_value");
									mCardDetail.facevalue=mCardDetail.facevalue.equals(null)||mCardDetail.facevalue.equals("")?mIntegerEmptyValue:mCardDetail.facevalue;
									mCardDetail.status = ZouponsWebService.getValue(mChildElement,"status");
									mCardDetail.status=mCardDetail.status.equals(null)||mCardDetail.status.equals("")?mEmptyValue:mCardDetail.status;
									mCardDetail.sellprice = ZouponsWebService.getValue(mChildElement,"sale_price");
									mCardDetail.sellprice=mCardDetail.sellprice.equals(null)||mCardDetail.sellprice.equals("")?mIntegerEmptyValue:mCardDetail.sellprice;
									mCardDetail.imagepath = ZouponsWebService.getValue(mChildElement,"image_path");
									mCardDetail.imagepath = mCardDetail.imagepath.equals(null)?mEmptyValue:mCardDetail.imagepath;
									if(mCardDetail.imagepath.contains("")){
										mCardDetail.imagepath = mCardDetail.imagepath.replaceAll("\\s","%20");
									}
									mCardDetail.remaining_count = ZouponsWebService.getValue(mChildElement,"count_per_week");
									mCardDetail.remaining_count=mCardDetail.remaining_count.equals(null)||mCardDetail.remaining_count.equals("")?mIntegerEmptyValue:mCardDetail.remaining_count;
									mCardDetail.actual_count_per_week = ZouponsWebService.getValue(mChildElement,"actual_per_week");
									mCardDetail.actual_count_per_week=mCardDetail.actual_count_per_week.equals(null)||mCardDetail.actual_count_per_week.equals("")?mIntegerEmptyValue:mCardDetail.actual_count_per_week;
									WebServiceStaticArrays.mStoreZouponsDealCardDetails.add(mCardDetail);
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

	/* To parse batch sales response */
	public ArrayList<Object> parseBatchSalesDetais(String response) {
		ArrayList<Object> batchsales_list = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		try{
			mDoc = zouponswebservice.XMLfromString(response);
			mNodes = mDoc.getElementsByTagName("batch_sales");	
			mChildNodes = mDoc.getElementsByTagName("batch_sale");
			mRecordSize = zouponswebservice.numResults(mDoc);
			if(mRecordSize<=0){
				return batchsales_list;
			}else{
				if(mNodes!=null){
					if(mNodes.getLength() == 1 && mChildNodes.getLength() == 0){
						return batchsales_list;
					}else{
						for(int i=0;i<mNodes.getLength();){
							mNodeElement = (Element) mNodes.item(i);	
							String mTimeZone = ZouponsWebService.getValue(mNodeElement,"time_zone");
							mTimeZone=mTimeZone.equals(null)?mEmptyValue:mTimeZone;
							if(mChildNodes != null){
								for(int j=0;j<mChildNodes.getLength();j++){	
									BatchDetailsClassVariables batch_sales_details = new BatchDetailsClassVariables();
									mNodeElement = (Element) mChildNodes.item(j);
									batch_sales_details.customer_name=ZouponsWebService.getValue(mNodeElement,"customer_name");
									batch_sales_details.customer_name= batch_sales_details.customer_name.equals(null)?mEmptyValue:batch_sales_details.customer_name;
									batch_sales_details.employee_name=ZouponsWebService.getValue(mNodeElement,"employee_name");
									batch_sales_details.employee_name= batch_sales_details.employee_name.equals(null)?mEmptyValue:batch_sales_details.employee_name;
									batch_sales_details.amount=ZouponsWebService.getValue(mNodeElement,"amount");
									batch_sales_details.amount= ( batch_sales_details.amount.equals(null)|| batch_sales_details.amount.equals(""))?mIntegerEmptyValue:batch_sales_details.amount;
									batch_sales_details.tip=ZouponsWebService.getValue(mNodeElement,"tip_amount");
									batch_sales_details.tip= ( batch_sales_details.tip.equals(null)|| batch_sales_details.tip.equals(""))?mIntegerEmptyValue:batch_sales_details.tip;
									batch_sales_details.zouponsfee=ZouponsWebService.getValue(mNodeElement,"zoupons_fee");
									batch_sales_details.zouponsfee= ( batch_sales_details.zouponsfee.equals(null)|| batch_sales_details.zouponsfee.equals(""))?mIntegerEmptyValue:batch_sales_details.zouponsfee;
									batch_sales_details.net_amount=ZouponsWebService.getValue(mNodeElement,"total_amount");
									batch_sales_details.net_amount= ( batch_sales_details.net_amount.equals(null)|| batch_sales_details.net_amount.equals(""))?mIntegerEmptyValue:batch_sales_details.net_amount;
									batch_sales_details.transaction_date=ZouponsWebService.getValue(mNodeElement,"updated_date");
									batch_sales_details.transaction_date=batch_sales_details.transaction_date.equals(null)||batch_sales_details.transaction_date.equalsIgnoreCase("")?mEmptyValue:convertTimeToDeviceTime(batch_sales_details.transaction_date,mTimeZone);
									batchsales_list.add(batch_sales_details);
								}

								return batchsales_list;
							}else{
								return batchsales_list;
							}
						}
						return batchsales_list;
					}
				}else{
					return batchsales_list;
				}
			}
		}catch (Exception e) {
			return null;
		}
	}
	
	/* To parse batch sales send email report response */
	public String parseSendEmailSalesStatus(String mResponse) {
		// TODO Auto-generated method stub
		ZouponsWebService zouponswebservice = new ZouponsWebService(mContext);
		String response_status="";
		try{
			mDoc = zouponswebservice.XMLfromString(mResponse);						
			mNodes = mDoc.getElementsByTagName("batch_sales");			
			mRecordSize = zouponswebservice.numResults(mDoc);					   
			if(mRecordSize<=0){
				return "no records";
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						mNodeElement = (Element) mNodes.item(i);
						if(mNodeElement.getChildNodes().getLength()==1){
							response_status=ZouponsWebService.getValue(mNodeElement,"message");
							response_status=response_status.equals(null)?response_status:response_status;
						}else{
							response_status = "failure";
						}
					}
					return response_status;
				}else{
					return "no records";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return "failure";
		}
	}	

	 /* To convert time to current device */  
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