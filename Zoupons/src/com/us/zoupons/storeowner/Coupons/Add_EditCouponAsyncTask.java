package com.us.zoupons.storeowner.Coupons;

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
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

public class Add_EditCouponAsyncTask extends AsyncTask<String, String, String>{

	private Activity mContext;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String TAG="StoreOwnerAddCouponsAsynchTask";
	private String mAddCouponFlag="",mCouponType="",mClassname="";
	private ImageView mCouponBarcode;
	Bitmap bmp;
	
	// For Add coupons
	public Add_EditCouponAsyncTask(Activity context,ImageView CouponBarcode,String class_name) {
		this.mContext = context;
		this.mCouponBarcode = CouponBarcode;
		this.mClassname = class_name;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(this.mContext);
		progressdialog=new ProgressDialog(this.mContext);
		progressdialog.setCancelable(true);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String mresult="";
		mAddCouponFlag = params[0];
		
		try{
			String mResponse;
			if(params[0].equalsIgnoreCase("generate_qrcode")){ // params[0] --> "generate_qrcode, params[1] --> location id, params[2] --> coupon code
			    mResponse=zouponswebservice.generateBarcode(params[0], params[1], params[2]);	
			}else{
				mCouponType =  params[1];
				mResponse=zouponswebservice.addCoupon(params[0], params[1], params[2], params[3],params[4], params[5], params[6], params[7], params[8],params[9],params[10],params[11],params[12],params[13],params[14],params[15]);
			}
			Log.i("response", mResponse);
			if(!mResponse.equalsIgnoreCase("noresponse") && !mResponse.equalsIgnoreCase("failure")){
				String mParsingResponse = parsingclass.parseAddCoupon(mResponse,params[0]);
				if(!mParsingResponse.equals("failure") && !mParsingResponse.equals("no records")){
					mresult = mParsingResponse;
					if(mAddCouponFlag.equalsIgnoreCase("generate_qrcode")){
						bmp =  getBitmapFromURL(mresult);
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
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
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
					if(bmp!=null){
						mCouponBarcode.setImageBitmap(bmp);
					}
				}else{ // Result after adding coupons
					if(result.equalsIgnoreCase("Success")){
						alertBox_service("Information", "Coupon has been successfully added");
					}else if(result.equalsIgnoreCase("updated")){
						alertBox_service("Information", "Coupon has been successfully updated");
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
	protected void onPreExecute() {
		((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(mContext,"Loading","Please Wait!",true);
		super.onPreExecute();
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
                 	if(mClassname.equalsIgnoreCase("customer_center") || mClassname.equalsIgnoreCase("contact_store")){
                 		mContext.finish();
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



