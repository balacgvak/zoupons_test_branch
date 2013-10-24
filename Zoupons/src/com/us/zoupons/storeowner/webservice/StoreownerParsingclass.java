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

import com.us.zoupons.ClassVariables.POJOCardDetails;
import com.us.zoupons.ClassVariables.POJOStoreInfo;
import com.us.zoupons.ClassVariables.POJOUserProfile;
import com.us.zoupons.ClassVariables.POJOVideoURL;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.WebService.ZouponsWebService;
import com.us.zoupons.cards.StoreGiftCardDetails;
import com.us.zoupons.invoice.POJOInvoiceList;
import com.us.zoupons.storeowner.AddEmployee.EmployeePermissionClassVariables;
import com.us.zoupons.storeowner.Employees.StoreEmployeesClassVariables;
import com.us.zoupons.storeowner.Employees.StoreOwner_Employees;
import com.us.zoupons.storeowner.Locations.StoreLocationsAsyncTask;
import com.us.zoupons.storeowner.customercenter.FavouriteCustomerDetails;
import com.us.zoupons.zpay.PaymentStatusVariables;

public class StoreownerParsingclass {
	Context ctx;
	private String TAG="ZouponsStoreownerParsingClass";
	private Document mDoc;
	private NodeList mNodes,mChildNodes,mParentNode,mSecondChildNodes;
	private int mRecordSize;
	private Element e,parent_element,child_element;
	private String mEmptyValue="";
	private String mIntegerEmptyValue="0";

	public StoreownerParsingclass(Context context){
		this.ctx=context;
	}

	public ArrayList<Object> parseUserProfile(String mGetUserProfileResponse, String pageflag) {
		ArrayList<Object> userInfo = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						e = (Element) mNodes.item(i);
						obj.mUserId=ZouponsWebService.getValue(e,"UserId");
						obj.mUserId=obj.mUserId.equals(null)?mEmptyValue:obj.mUserId;
						obj.mUserFirstName=ZouponsWebService.getValue(e,"FirstName");
						obj.mUserFirstName=obj.mUserFirstName.equals(null)?mEmptyValue:obj.mUserFirstName;
						obj.mUserLastName=ZouponsWebService.getValue(e,"LastName");
						obj.mUserLastName=obj.mUserLastName.equals(null)?mEmptyValue:obj.mUserLastName;
						obj.mUserEmail=ZouponsWebService.getValue(e,"Email");
						obj.mUserEmail=obj.mUserEmail.equals(null)?mEmptyValue:obj.mUserEmail;
						obj.mUserMobile=ZouponsWebService.getValue(e,"Mobile");
						obj.mUserMobile=obj.mUserMobile.equals(null)?mEmptyValue:obj.mUserMobile;
						obj.mUserImage=ZouponsWebService.getValue(e,"UserImage");
						obj.mUserImage=obj.mUserImage.equals(null)?mEmptyValue:obj.mUserImage;
						if(obj.mUserImage.contains("")){
							obj.mUserImage = obj.mUserImage.replaceAll("\\s","%20");
						}
						obj.mUserType=ZouponsWebService.getValue(e,"UserType");
						obj.mUserType=obj.mUserType.equals(null)?mEmptyValue:obj.mUserType;
						obj.mMessage=ZouponsWebService.getValue(e,"message");
						obj.mMessage=obj.mMessage.equals(null)?mEmptyValue:obj.mMessage;
						obj.mStatus=ZouponsWebService.getValue(e,"Status");
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


	public String parseAddCoupon(String mCouponResponse, String flag) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						e = (Element) mNodes.item(i);
						if(e.getChildNodes().getLength()==1){
							if(flag.equalsIgnoreCase("generate_qrcode")){
								response_status=ZouponsWebService.getValue(e,"bar_code");
								response_status=response_status.equals(null)?response_status:response_status;	
							}else{
								response_status=ZouponsWebService.getValue(e,"message");
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

	// Parse store location details response
	public ArrayList<Object> parseStoreLocations(String location_response) {
		ArrayList<Object> storeLocations = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						/*POJOStoreInfo obj = new POJOStoreInfo();
						e = (Element) mNodes.item(0);
						obj.message=ZouponsWebService.getValue(e,"message");
						obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
						storeLocations.add(obj);*/
						return storeLocations;
					}else{
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								for(int j=0;j<mChildNodes.getLength();j++){	
									POJOStoreInfo obj = new POJOStoreInfo();
									e = (Element) mChildNodes.item(j);
									obj.store_id=ZouponsWebService.getValue(e,"id");
									obj.store_id=obj.store_id.equals(null)?mEmptyValue:obj.store_id;
									obj.store_name=ZouponsWebService.getValue(e,"store_name");
									obj.store_name=obj.store_name.equals(null)?mEmptyValue:obj.store_name;
									obj.address_line1=ZouponsWebService.getValue(e,"address_line1");
									obj.address_line1=obj.address_line1.equals(null)?mEmptyValue:obj.address_line1;
									obj.city=ZouponsWebService.getValue(e,"city");
									obj.city=obj.city.equals(null)?mEmptyValue:obj.city;
									obj.state=ZouponsWebService.getValue(e,"state");
									obj.state=obj.state.equals(null)?mEmptyValue:obj.state;
									obj.country=ZouponsWebService.getValue(e,"country");
									obj.country=obj.country.equals(null)?mEmptyValue:obj.country;
									obj.zip_code=ZouponsWebService.getValue(e,"zip_code");
									obj.zip_code=obj.zip_code.equals(null)?mEmptyValue:obj.zip_code;
									obj.qr_code=ZouponsWebService.getValue(e,"qr_code");
									obj.qr_code=obj.qr_code.equals(null)?mEmptyValue:obj.qr_code;
									obj.location_id=ZouponsWebService.getValue(e,"location_id");
									obj.location_id=obj.location_id.equals(null)?mEmptyValue:obj.location_id;
									obj.status=ZouponsWebService.getValue(e,"status");
									obj.status=obj.status.equals(null)?mEmptyValue:obj.status;
									obj.address_line2=ZouponsWebService.getValue(e,"address_line2");
									obj.address_line2=obj.address_line2.equals(null)?mEmptyValue:obj.address_line2;
									obj.phone=ZouponsWebService.getValue(e,"phone");
									obj.phone=obj.phone.equals(null)?mEmptyValue:obj.phone;
									storeLocations.add(obj);
								}
								return storeLocations;
							}else{
								return storeLocations;
							}
						}
						return storeLocations;
					}
				}else{
					return storeLocations;
				}
			}
		}catch (Exception e) {
			return null;
		}
	}

	public String parsePaymentResponse(String mresponse) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
		String response_status="";
		PaymentStatusVariables.Customer_Reference = "";
		PaymentStatusVariables.Authorization_Number = "";
		PaymentStatusVariables.Bank_Message = "";
		PaymentStatusVariables.message = "";
		PaymentStatusVariables.purchase_message = "";
		try{
			mDoc = zouponswebservice.XMLfromString(mresponse);      
			mNodes = mDoc.getElementsByTagName("approve_customer_initiated_payment");   
			mRecordSize = zouponswebservice.numResults(mDoc);        //function to get length of the records
			if(mRecordSize<=0){
				response_status = "failure";
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						e = (Element) mNodes.item(i);
						PaymentStatusVariables.Customer_Reference=ZouponsWebService.getValue(e,"Customer_Ref");
						PaymentStatusVariables.Customer_Reference=PaymentStatusVariables.Customer_Reference.equals(null)?mEmptyValue:PaymentStatusVariables.Customer_Reference;
						PaymentStatusVariables.Authorization_Number=ZouponsWebService.getValue(e,"Authorization_Num");
						PaymentStatusVariables.Authorization_Number=PaymentStatusVariables.Authorization_Number.equals(null)?mEmptyValue:PaymentStatusVariables.Authorization_Number;
						PaymentStatusVariables.Bank_Message=ZouponsWebService.getValue(e,"Bank_Message");
						PaymentStatusVariables.Bank_Message=PaymentStatusVariables.Bank_Message.equals(null)?mEmptyValue:PaymentStatusVariables.Bank_Message;
						PaymentStatusVariables.message=ZouponsWebService.getValue(e,"message");
						PaymentStatusVariables.message=PaymentStatusVariables.message.equals(null)?mEmptyValue:PaymentStatusVariables.message;
						PaymentStatusVariables.purchase_message=ZouponsWebService.getValue(e,"purchase_message");
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

	public String parseStoreEmployees(String mGetResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("store_employees");						//function to get value by its vehicle tag name
			mChildNodes = mDoc.getElementsByTagName("store_employee");
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
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
								parent_element = (Element) mNodes.item(0);
								StoreOwner_Employees.mEmployeeTotalCount = ZouponsWebService.getValue(parent_element,"total_count");
								StoreOwner_Employees.mEmployeeTotalCount = StoreOwner_Employees.mEmployeeTotalCount.equals(null)?"0":StoreOwner_Employees.mEmployeeTotalCount;
								for(int j=0;j<mChildNodes.getLength();j++){	
									StoreEmployeesClassVariables employee_details = new StoreEmployeesClassVariables();
									e = (Element) mChildNodes.item(j);
									employee_details.mEmployeeId=ZouponsWebService.getValue(e,"id");
									employee_details.mEmployeeId=employee_details.mEmployeeId.equals(null)?mEmptyValue:employee_details.mEmployeeId;
									employee_details.mEmployeeName=ZouponsWebService.getValue(e,"user_name");
									employee_details.mEmployeeName=employee_details.mEmployeeName.equals(null)?mEmptyValue:employee_details.mEmployeeName;
									employee_details.mEmployeeFirstName=ZouponsWebService.getValue(e,"first_name");
									employee_details.mEmployeeFirstName=employee_details.mEmployeeFirstName.equals(null)?mEmptyValue:employee_details.mEmployeeFirstName;
									employee_details.mEmployeeLastName=ZouponsWebService.getValue(e,"last_name");
									employee_details.mEmployeeLastName=employee_details.mEmployeeLastName.equals(null)?mEmptyValue:employee_details.mEmployeeLastName;
									employee_details.mEmployeeProfileImage=ZouponsWebService.getValue(e,"profile_picture");
									employee_details.mEmployeeProfileImage=employee_details.mEmployeeProfileImage.equals(null)?mEmptyValue:employee_details.mEmployeeProfileImage;
									if(employee_details.mEmployeeProfileImage.contains("")){
										employee_details.mEmployeeProfileImage = employee_details.mEmployeeProfileImage.replaceAll("\\s","%20");
									}
									employee_details.mMobileNumber=ZouponsWebService.getValue(e,"telephone_number");
									employee_details.mMobileNumber=employee_details.mMobileNumber.equals(null)?mEmptyValue:employee_details.mMobileNumber;
									employee_details.mEmailAddress=ZouponsWebService.getValue(e,"email");
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
	// Parse store details
	public ArrayList<Object> parseVerifyEmployeeCode(String response) {
		ArrayList<Object> mVerifyResult = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						e = (Element) mNodes.item(0);
						mAddEmployee.mMessage=ZouponsWebService.getValue(e,"message");
						mAddEmployee.mMessage=mAddEmployee.mMessage.equals(null)?mEmptyValue:mAddEmployee.mMessage;
						mVerifyResult.add(mAddEmployee);
						return mVerifyResult;
					}else{
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								for(int j=0;j<mChildNodes.getLength();j++){	
									EmployeePermissionClassVariables mAddEmployee = new EmployeePermissionClassVariables();
									e = (Element) mChildNodes.item(j);
									mAddEmployee.mPermissionid=ZouponsWebService.getValue(e,"id");
									mAddEmployee.mPermissionid=mAddEmployee.mPermissionid.equals(null)?mEmptyValue:mAddEmployee.mPermissionid;
									mAddEmployee.mPermissionName=ZouponsWebService.getValue(e,"name");
									mAddEmployee.mPermissionName=mAddEmployee.mPermissionName.equals(null)?mEmptyValue:mAddEmployee.mPermissionName;
									mAddEmployee.mPermissionStatus=ZouponsWebService.getValue(e,"status");
									mAddEmployee.mPermissionStatus=mAddEmployee.mPermissionStatus.equals(null)?mEmptyValue:mAddEmployee.mPermissionStatus;
									mAddEmployee.mMessage=ZouponsWebService.getValue(e,"message");
									mAddEmployee.mMessage=mAddEmployee.mMessage.equals(null)?mEmptyValue:mAddEmployee.mMessage;
									mVerifyResult.add(mAddEmployee);
								}
								return mVerifyResult;
							}else{
								return mVerifyResult;
							}
						}
						return mVerifyResult;
					}
				}else{
					return mVerifyResult;
				}
			}
		}catch (Exception e) {
			return null;
		}
	}

	public ArrayList<Object> parseActivateEmployee(String Response) {
		ArrayList<Object> mActivateEmployeeResult = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
		try{
			mDoc = zouponswebservice.XMLfromString(Response);
			mNodes = mDoc.getElementsByTagName("add_employee"); 
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return mActivateEmployeeResult;
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						e = (Element) mNodes.item(i);
						String message=ZouponsWebService.getValue(e,"message");
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

	// Parse store details
	public ArrayList<Object> parseEmployeePermisssions(String response) {
		ArrayList<Object> mEmployeePermissionsList = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						e = (Element) mNodes.item(0);
						mAddEmployee.mMessage=ZouponsWebService.getValue(e,"message");
						mAddEmployee.mMessage=mAddEmployee.mMessage.equals(null)?mEmptyValue:mAddEmployee.mMessage;
						mEmployeePermissionsList.add(mAddEmployee);
						return mEmployeePermissionsList;
					}else{
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null && mSecondChildNodes !=null){
								for(int j=0;j<mChildNodes.getLength();j++){	
									EmployeePermissionClassVariables mAddEmployee = new EmployeePermissionClassVariables();
									e = (Element) mChildNodes.item(j);
									mAddEmployee.mPermissionid=ZouponsWebService.getValue(e,"id");
									mAddEmployee.mPermissionid=mAddEmployee.mPermissionid.equals(null)?mEmptyValue:mAddEmployee.mPermissionid;
									mAddEmployee.mPermissionName=ZouponsWebService.getValue(e,"name");
									mAddEmployee.mPermissionName=mAddEmployee.mPermissionName.equals(null)?mEmptyValue:mAddEmployee.mPermissionName;
									mAddEmployee.mPermissionStatus=ZouponsWebService.getValue(e,"status");
									mAddEmployee.mPermissionStatus=mAddEmployee.mPermissionStatus.equals(null)?mEmptyValue:mAddEmployee.mPermissionStatus;
									mAddEmployee.mMessage=ZouponsWebService.getValue(e,"message");
									mAddEmployee.mMessage=mAddEmployee.mMessage.equals(null)?mEmptyValue:mAddEmployee.mMessage;
									mEmployeePermissionsList.add(mAddEmployee);
								}
								EmployeePermissionClassVariables.mStorePermissionTotalIds=mChildNodes.getLength();
								e = (Element) mNodes.item(0);
								EmployeePermissionClassVariables.mStorePermissionIds=ZouponsWebService.getValue(e,"permission_ids");
								EmployeePermissionClassVariables.mStorePermissionIds=EmployeePermissionClassVariables.mStorePermissionIds.equals(null)?mEmptyValue:EmployeePermissionClassVariables.mStorePermissionIds;
								EmployeePermissionClassVariables.mLocationPermissionIds=ZouponsWebService.getValue(e,"location_ids");
								EmployeePermissionClassVariables.mLocationPermissionIds=EmployeePermissionClassVariables.mLocationPermissionIds.equals(null)?mEmptyValue:EmployeePermissionClassVariables.mLocationPermissionIds;
								for(int j=0;j<mSecondChildNodes.getLength();j++){	
									EmployeePermissionClassVariables mAddEmployee = new EmployeePermissionClassVariables();
									e = (Element) mSecondChildNodes.item(j);
									mAddEmployee.mLocationPermissionaddress_line1=ZouponsWebService.getValue(e,"address_line1");
									mAddEmployee.mLocationPermissionaddress_line1=mAddEmployee.mLocationPermissionaddress_line1.equals(null)?mEmptyValue:mAddEmployee.mLocationPermissionaddress_line1;
									mAddEmployee.mLocationPermissioncity=ZouponsWebService.getValue(e,"city");
									mAddEmployee.mLocationPermissioncity=mAddEmployee.mLocationPermissioncity.equals(null)?mEmptyValue:mAddEmployee.mLocationPermissioncity;
									mAddEmployee.mLocationPermissionstate=ZouponsWebService.getValue(e,"state");
									mAddEmployee.mLocationPermissionstate=mAddEmployee.mLocationPermissionstate.equals(null)?mEmptyValue:mAddEmployee.mLocationPermissionstate;
									mAddEmployee.mLocationPermissioncountry=ZouponsWebService.getValue(e,"country");
									mAddEmployee.mLocationPermissioncountry=mAddEmployee.mLocationPermissioncountry.equals(null)?mEmptyValue:mAddEmployee.mLocationPermissioncountry;
									mAddEmployee.mLocationPermissionzip_code=ZouponsWebService.getValue(e,"zip_code");
									mAddEmployee.mLocationPermissionzip_code=mAddEmployee.mLocationPermissionzip_code.equals(null)?mEmptyValue:mAddEmployee.mLocationPermissionzip_code;
									mAddEmployee.mLocationPermissionId=ZouponsWebService.getValue(e,"location_id");
									mAddEmployee.mLocationPermissionId=mAddEmployee.mLocationPermissionId.equals(null)?mEmptyValue:mAddEmployee.mLocationPermissionId;
									mEmployeePermissionsList.add(mAddEmployee);
								}
								return mEmployeePermissionsList;
							}else{
								return mEmployeePermissionsList;
							}
						}
						return mEmployeePermissionsList;
					}
				}else{
					return mEmployeePermissionsList;
				}
			}
		}catch (Exception e) {
			return null;
		}

	}

	public String parseInactivateEmployee(String response) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						e = (Element) mNodes.item(i);
						if(e.getChildNodes().getLength()==1){
							response_status=ZouponsWebService.getValue(e,"message");
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

	public String parseValidateAddress(String response) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						e = (Element) mNodes.item(i);
						status = ZouponsWebService.getValue(e,"status");
						status=status.equals(null)?mEmptyValue:status;
						location_type = ZouponsWebService.getValue(e,"location_type");
						location_type=location_type.equals(null)?mEmptyValue:location_type;
						StoreLocationsAsyncTask.latitude = ZouponsWebService.getValue(e,"lat");
						StoreLocationsAsyncTask.latitude=StoreLocationsAsyncTask.latitude.equals(null)?mEmptyValue:StoreLocationsAsyncTask.latitude;
						StoreLocationsAsyncTask.longitude = ZouponsWebService.getValue(e,"lng");
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

	public ArrayList<Object> parseLocationStatusChangeResponse(String Response) {
		ArrayList<Object> mLocationStatusChangeResult = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
		try{
			mDoc = zouponswebservice.XMLfromString(Response);
			mNodes = mDoc.getElementsByTagName("store_location_status"); 
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return mLocationStatusChangeResult;
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						e = (Element) mNodes.item(i);
						String message=ZouponsWebService.getValue(e,"message");
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

	public String parseRaiseInvoice(String mResponse) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						e = (Element) mNodes.item(i);
						if(e.getChildNodes().getLength()==1){
							response_status=ZouponsWebService.getValue(e,"message");
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

	// Parse Invoice list 
	public ArrayList<Object> parseInvoiceList(String location_response) {
		ArrayList<Object> invoiceList = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						e = (Element) mNodes.item(0);
						obj.message=ZouponsWebService.getValue(e,"message");
						obj.message=obj.message.equals(null)?mEmptyValue:obj.message;
						invoiceList.add(obj);
						return invoiceList;
					}else{
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								for(int j=0;j<mChildNodes.getLength();j++){	
									POJOInvoiceList obj = new POJOInvoiceList();
									e = (Element) mChildNodes.item(j);
									obj.InvoiceId=ZouponsWebService.getValue(e,"id");
									obj.InvoiceId=obj.InvoiceId.equals(null)?mEmptyValue:obj.InvoiceId;
									obj.Customer_Image=ZouponsWebService.getValue(e,"profile_picture");
									obj.Customer_Image=obj.Customer_Image.equals(null)?mEmptyValue:obj.Customer_Image;
									if(obj.Customer_Image.contains("")){
										obj.Customer_Image = obj.Customer_Image.replaceAll("\\s","%20");
									}
									obj.Customer_Name=ZouponsWebService.getValue(e,"user_name");
									obj.Customer_Name=obj.Customer_Name.equals(null)?mEmptyValue:obj.Customer_Name;
									obj.Customer_FirstName=ZouponsWebService.getValue(e,"first_name");
									obj.Customer_FirstName=obj.Customer_FirstName.equals(null)?mEmptyValue:obj.Customer_FirstName;
									obj.Customer_LastName=ZouponsWebService.getValue(e,"last_name");
									obj.Customer_LastName=obj.Customer_LastName.equals(null)?mEmptyValue:obj.Customer_LastName;
									obj.Amount=ZouponsWebService.getValue(e,"amount");
									obj.Amount=obj.Amount.equals(null)?mEmptyValue:obj.Amount;
									obj.TimeZone=ZouponsWebService.getValue(e,"time_zone");
									obj.TimeZone=obj.TimeZone.equals(null)?mEmptyValue:obj.TimeZone;
									obj.CreatedDate=ZouponsWebService.getValue(e,"created");
									obj.CreatedDate=obj.CreatedDate.equals(null)||obj.CreatedDate.equalsIgnoreCase("")?mEmptyValue:convertTimeToDeviceTime(obj.CreatedDate,obj.TimeZone);
									obj.Days=ZouponsWebService.getValue(e,"days");
									obj.Days=obj.Days.equals(null)?mEmptyValue:obj.Days;
									obj.Status=ZouponsWebService.getValue(e,"status");
									obj.Status=obj.Status.equals(null)?mEmptyValue:obj.Status;
									obj.StoreNote=ZouponsWebService.getValue(e,"store_note");
									obj.StoreNote=obj.StoreNote.equals(null)?mEmptyValue:obj.StoreNote; 
									obj.CustomerNote=ZouponsWebService.getValue(e,"customer_note");
									obj.CustomerNote=obj.CustomerNote.equals(null)?mEmptyValue:obj.CustomerNote;
									obj.ActualAmount=ZouponsWebService.getValue(e,"actual_amount");
									obj.ActualAmount=obj.ActualAmount.equals(null)?mEmptyValue:obj.ActualAmount;
									obj.TipAmount=ZouponsWebService.getValue(e,"tip_amount");
									obj.TipAmount=obj.TipAmount.equals(null)?mEmptyValue:obj.TipAmount;
									obj.TotalAmount=ZouponsWebService.getValue(e,"total_amount");
									obj.TotalAmount=obj.TotalAmount.equals(null)?mEmptyValue:obj.TotalAmount;
									obj.TransactionDate=ZouponsWebService.getValue(e,"transaction_date");
									obj.TransactionDate=obj.TransactionDate.equals(null)||obj.TransactionDate.equalsIgnoreCase("")?mEmptyValue:convertTimeToDeviceTime(obj.TransactionDate,obj.TimeZone);
									obj.TransactionType=ZouponsWebService.getValue(e,"transaction_type");
									obj.TransactionType=obj.TransactionType.equals(null)?mEmptyValue:obj.TransactionType;
									obj.CreditCardMask=ZouponsWebService.getValue(e,"card_mask");
									obj.CreditCardMask=obj.CreditCardMask.equals(null)?mEmptyValue:obj.CreditCardMask;
									obj.GiftcardFaceValue=ZouponsWebService.getValue(e,"face_value");
									obj.GiftcardFaceValue=obj.GiftcardFaceValue.equals(null)?mEmptyValue:obj.GiftcardFaceValue;
									obj.CreditCardAmount=ZouponsWebService.getValue(e,"amount_creditcard");
									obj.CreditCardAmount=obj.CreditCardAmount.equals(null)?mEmptyValue:obj.CreditCardAmount;
									obj.GiftcardAmount=ZouponsWebService.getValue(e,"amount_giftcard");
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
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
		try{
			mDoc = zouponswebservice.XMLfromString(Response);
			mNodes = mDoc.getElementsByTagName("card_on_file"); 
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return mActivateCreditCardResponseResult;
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						e = (Element) mNodes.item(i);
						String message=ZouponsWebService.getValue(e,"message");
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

	public ArrayList<Object> parseupdateStoreDetails(String mResponse, String event_flag) {
		ArrayList<Object> mStoreDetailsUpdateResult = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						e = (Element) mNodes.item(i);
						String message=ZouponsWebService.getValue(e,"message");
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
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						for(int i=0;i<mNodes.getLength();i++){
							if(mChildNodes != null){
								for(int j=0;j<mChildNodes.getLength();j++){	
									FavouriteCustomerDetails mFavouriteCustomer = new FavouriteCustomerDetails();
									e = (Element) mChildNodes.item(j);
									mFavouriteCustomer.mCustomerId=ZouponsWebService.getValue(e,"id");
									mFavouriteCustomer.mCustomerId=mFavouriteCustomer.mCustomerId.equals(null)?mEmptyValue:mFavouriteCustomer.mCustomerId;
									mFavouriteCustomer.mCustomerName=ZouponsWebService.getValue(e,"user_name");
									mFavouriteCustomer.mCustomerName=mFavouriteCustomer.mCustomerName.equals(null)?mEmptyValue:mFavouriteCustomer.mCustomerName;
									mFavouriteCustomer.mCustomerFirstName=ZouponsWebService.getValue(e,"first_name");
									mFavouriteCustomer.mCustomerFirstName=mFavouriteCustomer.mCustomerFirstName.equals(null)?mEmptyValue:mFavouriteCustomer.mCustomerFirstName;
									mFavouriteCustomer.mCustomerLastName=ZouponsWebService.getValue(e,"last_name");
									mFavouriteCustomer.mCustomerLastName=mFavouriteCustomer.mCustomerLastName.equals(null)?mEmptyValue:mFavouriteCustomer.mCustomerLastName;
									mFavouriteCustomer.mCustomerProfileImage=ZouponsWebService.getValue(e,"profile_picture");
									mFavouriteCustomer.mCustomerProfileImage=mFavouriteCustomer.mCustomerProfileImage.equals(null)?mEmptyValue:mFavouriteCustomer.mCustomerProfileImage;
									if(mFavouriteCustomer.mCustomerProfileImage.contains("")){
										mFavouriteCustomer.mCustomerProfileImage = mFavouriteCustomer.mCustomerProfileImage.replaceAll("\\s","%20");
									}
									mFavouriteCustomer.mIsFavouriteStoreRemoved=ZouponsWebService.getValue(e,"favorite");
									mFavouriteCustomer.mIsFavouriteStoreRemoved=mFavouriteCustomer.mIsFavouriteStoreRemoved.equals(null)?mEmptyValue:mFavouriteCustomer.mIsFavouriteStoreRemoved;
									mFavouriteCustomer.mTransactionAmount=ZouponsWebService.getValue(e,"transaction_amount");
									mFavouriteCustomer.mTransactionAmount=mFavouriteCustomer.mTransactionAmount.equals(null) || mFavouriteCustomer.mTransactionAmount.equals("")?mIntegerEmptyValue:mFavouriteCustomer.mTransactionAmount;
									mFavouriteCustomerResult.add(mFavouriteCustomer);
								}
								return mFavouriteCustomerResult;
							}else{
								return mFavouriteCustomerResult;
							}
						}
						return mFavouriteCustomerResult;
					}
				}else{
					return mFavouriteCustomerResult;
				}
			}
		}catch (Exception e) {
			return null;
		}
	}

	public ArrayList<Object> parseAddFavouriteUser(String mResponse) {
		ArrayList<Object> mAddUserDetails = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
		try{
			mDoc = zouponswebservice.XMLfromString(mResponse);
			mNodes = mDoc.getElementsByTagName("add_user");	
			mRecordSize = zouponswebservice.numResults(mDoc);        
			if(mRecordSize<=0){
				return mAddUserDetails;
			}else{
				if(mNodes!=null){
					for(int i=0;i<mNodes.getLength();i++){
						e = (Element) mNodes.item(i);
						String message=ZouponsWebService.getValue(e,"message");
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

	public String parseSendMailResponse(String mEmailResponse) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						e = (Element) mNodes.item(i);
						if(e.getChildNodes().getLength()==1){
							response_status=ZouponsWebService.getValue(e,"message");
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

	public ArrayList<Object> parseStoreVideos(String mResponse) {
		ArrayList<Object> mAllStoreVideos = new ArrayList<Object>();
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
							e = (Element) mChildNodes.item(i);		        		
							obj.VideoId = ZouponsWebService.getValue(e,"id");
							obj.VideoId=obj.VideoId.equals(null)?mEmptyValue:obj.VideoId;	
							obj.VideoTitle = ZouponsWebService.getValue(e,"title");
							obj.VideoTitle=obj.VideoTitle.equals(null)?mEmptyValue:obj.VideoTitle;
							obj.VideoURL = ZouponsWebService.getValue(e,"url");
							obj.VideoURL=obj.VideoURL.equals(null)?mEmptyValue:obj.VideoURL;
							obj.VideoThumbNail = ZouponsWebService.getValue(e,"thumbnail");
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

	public String parseUploadVideoStatus(String videoresponse) {
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
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
						e = (Element) mNodes.item(i);
						if(e.getChildNodes().getLength()==1){
							response_status=ZouponsWebService.getValue(e,"message");
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

	public String parseStoreDeals(String mGetResponse) {
		String rtnValue="";
		ZouponsWebService zouponswebservice = new ZouponsWebService(ctx);
		try{
			mDoc = zouponswebservice.XMLfromString(mGetResponse);
			mNodes = mDoc.getElementsByTagName("deals");						//function to get value by its vehicle tag name
			mChildNodes = mDoc.getElementsByTagName("deal");
			mRecordSize = zouponswebservice.numResults(mDoc);					   //function to get length of the records
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
								parent_element = (Element) mNodes.item(0);
								StoreGiftCardDetails.mDealTotalCount = ZouponsWebService.getValue(parent_element,"total_count");
								StoreGiftCardDetails.mDealTotalCount = StoreGiftCardDetails.mDealTotalCount.equals(null)?"0":StoreGiftCardDetails.mDealTotalCount;
								for(int j=0;j<mChildNodes.getLength();j++){	
									POJOCardDetails mCardDetail = new POJOCardDetails();
									child_element = (Element) mChildNodes.item(j);
									mCardDetail.id = ZouponsWebService.getValue(child_element,"id");
									mCardDetail.id =mCardDetail.id.equals(null)?mEmptyValue:mCardDetail.id;
									mCardDetail.facevalue = ZouponsWebService.getValue(child_element,"face_value");
									mCardDetail.facevalue=mCardDetail.facevalue.equals(null)||mCardDetail.facevalue.equals("")?mIntegerEmptyValue:mCardDetail.facevalue;
									mCardDetail.status = ZouponsWebService.getValue(child_element,"status");
									mCardDetail.status=mCardDetail.status.equals(null)||mCardDetail.status.equals("")?mEmptyValue:mCardDetail.status;
									mCardDetail.sellprice = ZouponsWebService.getValue(child_element,"sale_price");
									mCardDetail.sellprice=mCardDetail.sellprice.equals(null)||mCardDetail.sellprice.equals("")?mIntegerEmptyValue:mCardDetail.sellprice;
									mCardDetail.imagepath = ZouponsWebService.getValue(child_element,"image_path");
									mCardDetail.imagepath = mCardDetail.imagepath.equals(null)?mEmptyValue:mCardDetail.imagepath;
									mCardDetail.remaining_count = ZouponsWebService.getValue(child_element,"count_per_week");
									mCardDetail.remaining_count=mCardDetail.remaining_count.equals(null)||mCardDetail.remaining_count.equals("")?mIntegerEmptyValue:mCardDetail.remaining_count;
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