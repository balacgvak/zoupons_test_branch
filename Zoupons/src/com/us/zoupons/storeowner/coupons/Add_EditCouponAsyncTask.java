package com.us.zoupons.storeowner.coupons;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.us.zoupons.storeowner.customercenter.CustomerCenter;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

/**
 * 
 * Asynchronous task which communicates webserver for adding coupon
 *
 */
public class Add_EditCouponAsyncTask extends AsyncTask<String, String, String>{

	private Activity mContext;
	private StoreownerWebserivce mZouponswebservice=null;
	private StoreownerParsingclass mParsingclass=null;
	private ProgressDialog mProgressdialog=null;
	private String mAddCouponFlag="",mCouponType="",mClassname="";
	private ImageView mCouponBarcode;
    private EditText mAddCouponTelephoneNumber,mAddCouponCodeValue;
	private Bitmap mCouponQRcodeBitmap;
	
	// For Add coupons
	public Add_EditCouponAsyncTask(Activity context,ImageView CouponBarcode,String class_name,EditText mAddCouponTelephoneNumber,EditText couponcode) {
		this.mContext = context;
		this.mCouponBarcode = CouponBarcode;
		this.mClassname = class_name;
		this.mAddCouponTelephoneNumber = mAddCouponTelephoneNumber;
		this.mAddCouponCodeValue = couponcode;
		mZouponswebservice= new StoreownerWebserivce(context);
		mParsingclass= new StoreownerParsingclass(this.mContext);
		mProgressdialog=new ProgressDialog(this.mContext);
		mProgressdialog.setCancelable(true);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		mProgressdialog = ProgressDialog.show(mContext,"Loading","Please Wait!",true);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String mresult="";
		mAddCouponFlag = params[0];
		try{
			String mResponse;
			if(params[0].equalsIgnoreCase("generate_qrcode")){ // Generate QR  params[0] --> "generate_qrcode, params[1] --> location id, params[2] --> coupon code
			    mResponse=mZouponswebservice.generateBarcode(params[0], params[1], params[2]);	
			}else{ // Add or Edit coupon
				// params[0] "create_coupon",params[1] = couponType, params[2] = mLocationId, params[3] = mCustomerUserId, params[4] =CouponTitle, params[5] = CouponCode,
				//params[6] = activationDate,params[7] = mExpirationDate, params[8] = CouponOneTimeUse[Yes/No],params[9] = CouponDescription, params[10] = Firstname,params[11] = Lastname,params[12] = mEmail, params[13] = CouponMobileNumber, params[12] = coupon_id[for edit only] 
				mCouponType =  params[1];
				mResponse=mZouponswebservice.addCoupon(params[0], params[1], params[2], params[3],params[4], params[5], params[6], params[7], params[8],params[9],params[10],params[11],params[12],params[13],params[14]);
			}
			if(!mResponse.equalsIgnoreCase("noresponse") && !mResponse.equalsIgnoreCase("failure")){
				String mParsingResponse = mParsingclass.parseAddCoupon(mResponse,params[0]);
				if(!mParsingResponse.equals("failure") && !mParsingResponse.equals("no records")){
					mresult = mParsingResponse;
					if(mAddCouponFlag.equalsIgnoreCase("generate_qrcode")){
						mCouponQRcodeBitmap =  getBitmapFromURL(mresult);
					}
				}else if(mParsingResponse.equalsIgnoreCase("failure")){
					mresult = "failure";
				}else if(mParsingResponse.equalsIgnoreCase("no records")){
					mresult="norecords";
				}
			}else {
				mresult="Response Error.";
			}
		}catch(Exception e){
			mresult = "failure";
		}
		return mresult;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try{
			if(mProgressdialog != null && mProgressdialog.isShowing()){
				mProgressdialog.dismiss();
			}
			if(result.equalsIgnoreCase("failure")){
				alertBox_service("Information", "Unable to reach service.");
			}else if(result.equalsIgnoreCase("norecords")){
				alertBox_service("Information", "No data available");
			}else if(result.equalsIgnoreCase("Response Error.")){
				alertBox_service("Information", "No data available");
			}else if(result.equalsIgnoreCase("no data")){
				alertBox_service("Information", "Unable to reach service.");
			}else if(result.equalsIgnoreCase("nonetwork")){
				Toast.makeText(this.mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
			}else{
				if(mAddCouponFlag.equalsIgnoreCase("generate_qrcode")){ 
					if(mCouponQRcodeBitmap!=null){
						mCouponBarcode.setImageBitmap(mCouponQRcodeBitmap);
					}
				}else{ // Result after adding coupons
					if(result.equalsIgnoreCase("Success")){
						alertBox_service("Information", "Coupon has been successfully added");
					}else if(result.equalsIgnoreCase("updated")){
						alertBox_service("Information", "Coupon has been successfully updated");
					}else if(result.equalsIgnoreCase("Email already exists")){
						mAddCouponTelephoneNumber.getText().clear();
						mAddCouponTelephoneNumber.requestFocus();
						mAddCouponCodeValue.getText().clear();
						alertBox_service("Information", "Email already exists in Zoupons,please provide active customer telephone number or new email address");
					}else if(result.equalsIgnoreCase("Coupon code already exists")){
						alertBox_service("Information", "Coupon code already exits for this location");
					}else{
						alertBox_service("Information", "Failed to add coupon");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
    }

	

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
                if(msg.equalsIgnoreCase("Coupon has been successfully added") || msg.equalsIgnoreCase("Coupon has been successfully updated")){
                 	if(mClassname.equalsIgnoreCase("contact_store")){
                 		mContext.finish();
                 	}else if(mClassname.equalsIgnoreCase("customer_center")){
                 		Intent intent_rightmenuCustomercenter = new Intent().setClass(mContext,CustomerCenter.class);
        				intent_rightmenuCustomercenter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        				mContext.startActivity(intent_rightmenuCustomercenter);	
                 	}else{
                    	Intent intent = new Intent();
                     	intent.putExtra("coupon_type", mCouponType);
                    	mContext.setResult(Activity.RESULT_OK,intent); 
                    	mContext.finish();	
                 	}
                	
                }
			}
		});
		service_alert.show();
	}
	
	// To download bitmap from specified URL 
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
}



