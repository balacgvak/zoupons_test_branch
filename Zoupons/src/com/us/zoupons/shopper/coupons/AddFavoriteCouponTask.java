package com.us.zoupons.shopper.coupons;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.NetworkCheck;
import com.us.zoupons.R;
import com.us.zoupons.classvariables.UserDetails;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.generic_activity.MainMenuActivity;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Asynchronous task to add store coupon as favorite coupon
 *
 */

public class AddFavoriteCouponTask extends AsyncTask<String, String,String>{

	private String TAG ="AddFavoriteTask";
	private Context mContext;
	private TextView mMenuFavoriteText;
	private ImageView mMenuFavoriteImage;
	private LinearLayout mMenuFavoriteLayout;
	private ProgressDialog mProgressdialog=null;
	private ZouponsWebService mZouponswebservice = null;
	private ZouponsParsingClass mParsingclass = null;
	private NetworkCheck mConnectivityNetworkCheck;
	private String mAddCouponFavoriteResponse,mParsedAddCouponFavoriteResponse;
	private String mCouponID;
	private ProgressBar mProgressBar;
	//For Favorite Coupon
	private TextView CouponDetailName,CouponDetailCode,CouponDetailExpires,mNoBarCodeImage;
	private EditText CouponDescription;
	private ImageView CouponBarCode;
	private String FromActivity="";
	private int CouponPosition;
	private Button CouponDetailNext,CouponDetailPrev;

	//Constructor to ADD/REMOVE coupons as Favorite
	public AddFavoriteCouponTask(Context context,String mCouponId, TextView mMenuBarFavCouponsText,
			LinearLayout mMenubarFavorite,ImageView mMenuBarFavCouponsImage) {
		this.mContext = context;		
		this.mCouponID = mCouponId;
		this.mMenuFavoriteText = mMenuBarFavCouponsText;
		this.mMenuFavoriteImage = mMenuBarFavCouponsImage;
		this.mMenuFavoriteLayout = mMenubarFavorite;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
		mProgressdialog.setCancelable(false);
		mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressdialog.setProgress(0);
		mProgressdialog.setMax(100);
	}

	// Constructor to Remove Coupon Favorite and Show Next Favorite Coupon 
	public AddFavoriteCouponTask(Context context,
			String mCouponId, TextView mMenuBarFavCouponsText,
			LinearLayout mMenubarFavorite, TextView mCouponDetailName,
			TextView mCouponDetailCode, TextView mCouponDetailExpires,
			EditText mCouponDescription, ImageView mCouponBarCode,String mActivity,
			int mCouponPosition,Button mCouponDetailNext,Button mCouponDetailPrev,TextView mNoBarCode,ImageView mMenuBarFavCouponsImage,ProgressBar progressbar) {
		this.mContext = context;		
		this.mCouponID = mCouponId;
		this.mMenuFavoriteText = mMenuBarFavCouponsText;
		this.mMenuFavoriteImage = mMenuBarFavCouponsImage;
		this.mMenuFavoriteLayout = mMenubarFavorite;
		this.CouponDetailName = mCouponDetailName;
		this.CouponDetailCode = mCouponDetailCode;
		this.CouponDetailExpires = mCouponDetailExpires;
		this.CouponDescription = mCouponDescription;
		this.CouponBarCode = mCouponBarCode;
		this.FromActivity = mActivity;
		this.CouponPosition = mCouponPosition;
		this.CouponDetailNext = mCouponDetailNext;
		this.CouponDetailPrev = mCouponDetailPrev;
		this.mNoBarCodeImage = mNoBarCode;
		this.mProgressBar = progressbar;
		mConnectivityNetworkCheck = new NetworkCheck();
		mZouponswebservice= new ZouponsWebService(context);
		mParsingclass= new ZouponsParsingClass(this.mContext);
		mProgressdialog=new ProgressDialog(context);
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
		mProgressdialog = ProgressDialog.show(mContext,"Loading...","Please Wait!",true);
	}

	@Override
	protected String doInBackground(String... params) {
		String result="";
		try{			
			if(mConnectivityNetworkCheck.ConnectivityCheck(mContext)){
				mAddCouponFavoriteResponse = mZouponswebservice.mAddFavoriteCoupon(UserDetails.mServiceUserId,mCouponID);	
				if(!mAddCouponFavoriteResponse.equals("failure") && !mAddCouponFavoriteResponse.equals("noresponse")){
					mParsedAddCouponFavoriteResponse= mParsingclass.mParseAddFavoriteCoupon(mAddCouponFavoriteResponse);
					if(mParsedAddCouponFavoriteResponse.equalsIgnoreCase("success")){			
						result ="success";				
					}else if(mParsedAddCouponFavoriteResponse.equalsIgnoreCase("failure")){
						Log.i(TAG,"Error");
						result="Response Error.";
					}else if(mParsedAddCouponFavoriteResponse.equalsIgnoreCase("norecords")){
						Log.i(TAG,"No Records");
						result="No Records";
					}
				}else{
					result="Response Error.";
				}
			}else{
				result="nonetwork";
			}
		}catch (Exception e) {
			Log.i(TAG,"Thread Error");
			result="Thread Error";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);		
		if(mProgressdialog != null && mProgressdialog.isShowing()){
			mProgressdialog.dismiss();
		}
		mMenuFavoriteLayout.setBackgroundResource(R.drawable.header_2);
		Log.i("Task", "onPOstExecute");			
		if(result.equals("nonetwork")){
			Toast.makeText(mContext, "No Network Connection", Toast.LENGTH_SHORT).show();
		}else if(result.equals("Response Error.")){
			alertBox_service("Information", "Unable to reach service.");
		}else if(result.equals("No Records")){
			alertBox_service("Information", result);
		}else if(result.equals("Thread Error")){
			alertBox_service("Information", "Unable to process.");
		}else{
			LoadCouponQRcodeTask mLoadCoupon = null;
			Log.i(TAG,"Success: ");	
			//From User Favorite Coupons
			if(FromActivity.equalsIgnoreCase("Favorite")){	
				//Flag to refresh adapter after remove list item
				MainMenuActivity.mIsRefreshAdapter = true;
				if(WebServiceStaticArrays.mStaticCouponsArrayList.size()-1>CouponPosition){
					CouponDetail.mCouponPosition = CouponPosition;
					POJOCouponsList mList = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(CouponPosition+1);
					CouponDetailName.setText(mList.mFavorite_CouponTitle);		
					CouponDetailCode.setText(mList.mFavorite_CouponCode);		
					CouponDetailExpires.setText(mList.mFavorite_CouponExpires);		
					CouponDescription.setText(mList.mFavorite_CouponDescription);		
					CouponBarCode.setImageBitmap(mList.mFavorite_CouponBarCode);
					if(mList.mFavorite_CouponBarCode != null){
						CouponBarCode.setImageBitmap(mList.mFavorite_CouponBarCode);
						CouponBarCode.setVisibility(View.VISIBLE);
						mNoBarCodeImage.setVisibility(View.GONE);
					}else{
						CouponBarCode.setVisibility(View.GONE);
						mNoBarCodeImage.setVisibility(View.VISIBLE);
					}
					WebServiceStaticArrays.mStaticCouponsArrayList.remove(CouponPosition);
					// To load coupon QR code
					mLoadCoupon = new LoadCouponQRcodeTask(CouponBarCode, mProgressBar,mNoBarCodeImage);
					mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					
					//If it's last position disable next button
					if(WebServiceStaticArrays.mStaticCouponsArrayList.size()-1 == CouponPosition){
						if(CouponPosition!=0){ // If we remove last before position
							CouponDetailNext.setEnabled(false);
							CouponDetailNext.setBackgroundResource(R.drawable.next_circles);
							CouponDetailNext.getBackground().setAlpha(100);
							CouponDetailPrev.getBackground().setAlpha(255);
						}else{ // List has only one value
							CouponDetailNext.setEnabled(false);
							CouponDetailNext.setBackgroundResource(R.drawable.next_circles);
							CouponDetailNext.getBackground().setAlpha(100);
							CouponDetailPrev.setEnabled(false);
							CouponDetailPrev.setBackgroundResource(R.drawable.next_circles);
							CouponDetailPrev.getBackground().setAlpha(100);
						}
					}else if(CouponPosition == 0){ //If it's first position disable prev button
						CouponDetailPrev.setEnabled(false);
						CouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
						CouponDetailPrev.getBackground().setAlpha(100);
						CouponDetailNext.getBackground().setAlpha(255);
					}else{//If it has last and first record enable both button's
						CouponDetailNext.setEnabled(true);
						CouponDetailNext.setBackgroundResource(R.drawable.next_circles);
						CouponDetailNext.getBackground().setAlpha(255);
						CouponDetailPrev.setEnabled(true);
						CouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
						CouponDetailPrev.getBackground().setAlpha(255);
					}
				}else if(WebServiceStaticArrays.mStaticCouponsArrayList.size()-1 == CouponPosition){
					//If the last record removed then check the list and show the First FavoriteCoupon
					WebServiceStaticArrays.mStaticCouponsArrayList.remove(CouponPosition);
					if(WebServiceStaticArrays.mStaticCouponsArrayList.size() != 0){
						Log.i(TAG, "Show First Record In the ArrayList");
						CouponDetail.mCouponPosition = 0;
						POJOCouponsList mList = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(0);
						CouponDetailName.setText(mList.mFavorite_CouponTitle);		
						CouponDetailCode.setText(mList.mFavorite_CouponCode);		
						CouponDetailExpires.setText(mList.mFavorite_CouponExpires);		
						CouponDescription.setText(mList.mFavorite_CouponDescription);		
						if(mList.mFavorite_CouponBarCode != null){
							CouponBarCode.setImageBitmap(mList.mFavorite_CouponBarCode);
							CouponBarCode.setVisibility(View.VISIBLE);
							mNoBarCodeImage.setVisibility(View.GONE);
						}else{
							CouponBarCode.setVisibility(View.GONE);
							mNoBarCodeImage.setVisibility(View.VISIBLE);
						}
						//Records Removed In the ArrayList and the Size is 1
						if(WebServiceStaticArrays.mStaticCouponsArrayList.size()-1 == 0){
							Log.i(TAG, "Records Removed In the ArrayList");							
							CouponDetailNext.setEnabled(false);
							CouponDetailNext.setBackgroundResource(R.drawable.next_circles);
							CouponDetailNext.getBackground().setAlpha(100);
							CouponDetailPrev.setEnabled(false);
							CouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
							CouponDetailPrev.getBackground().setAlpha(100);
						}else{
							Log.i(TAG, "ArrayList has Record");
							CouponDetailNext.setEnabled(true);
							CouponDetailNext.setBackgroundResource(R.drawable.next_circles);
							CouponDetailNext.getBackground().setAlpha(255);
							CouponDetailPrev.setEnabled(false);
							CouponDetailPrev.setBackgroundResource(R.drawable.prev_circles);
							CouponDetailPrev.getBackground().setAlpha(100);
						}
						
						mLoadCoupon = new LoadCouponQRcodeTask(CouponBarCode, mProgressBar,mNoBarCodeImage);
						mLoadCoupon.execute(mList.mFavorite_CouponBarCodeImage);
					}else{
						//If the last item in the arraylist go back to the list Activity
						Log.i(TAG, "No Records In the ArrayList");
						((Activity) mContext).finish();
					}
				}
			}else{	
				//From Store Coupon List
				if(CouponDetail.mIsCouponAddedAsFavotire.equalsIgnoreCase("Added")){
					mMenuFavoriteText.setText("Favorite");
					mMenuFavoriteImage.setImageResource(R.drawable.remove_from_favorite);
					mMenuFavoriteImage.setTag("Remove Favorite");
					POJOCouponsList obj = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(CouponDetail.mCouponPosition);
					obj.mCouponFavorite="Yes";
					WebServiceStaticArrays.mStaticCouponsArrayList.set(CouponDetail.mCouponPosition, obj);
				}else if(CouponDetail.mIsCouponAddedAsFavotire.equalsIgnoreCase("Removed")){
					mMenuFavoriteText.setText("Favorite");
					mMenuFavoriteImage.setImageResource(R.drawable.add_to_favorite);
					mMenuFavoriteImage.setTag("Add Favorite");
					POJOCouponsList obj = (POJOCouponsList) WebServiceStaticArrays.mStaticCouponsArrayList.get(CouponDetail.mCouponPosition);
					obj.mCouponFavorite="No";
					WebServiceStaticArrays.mStaticCouponsArrayList.set(CouponDetail.mCouponPosition, obj);
				}
			}
		}
	}

	/* To show alert pop up with respective message */
	private void alertBox_service(String title, String msg) {
		// TODO Auto-generated method stub
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.mContext);
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
