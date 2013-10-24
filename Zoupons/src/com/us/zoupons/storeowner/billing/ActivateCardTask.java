package com.us.zoupons.storeowner.billing;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Window;

import com.us.zoupons.android.listview.inflater.classes.CardOnFiles_Adapter;
import com.us.zoupons.storeowner.webservice.StoreownerParsingclass;
import com.us.zoupons.storeowner.webservice.StoreownerWebserivce;

public class ActivateCardTask extends AsyncTask<String, String, ArrayList<Object>>{

	private Context context;
	private StoreownerWebserivce zouponswebservice=null;
	private StoreownerParsingclass parsingclass=null;
	private ProgressDialog progressdialog=null;
	private String mCreditcardId;
	private CardOnFiles_Adapter mAdapter;
	private int checked_position;
	
	public ActivateCardTask(Context context,String card_id, CardOnFiles_Adapter cardOnFiles_Adapter, Integer checked_position) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mCreditcardId = card_id;
		this.checked_position = checked_position;
		this.mAdapter = cardOnFiles_Adapter;
		zouponswebservice= new StoreownerWebserivce(context);
		parsingclass= new StoreownerParsingclass(context);
		progressdialog=new ProgressDialog(this.context);
		progressdialog.setCancelable(false);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setProgress(0);
		progressdialog.setMax(100);
	}
		
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		((Activity) context).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,0);
		//Start a status dialog
		progressdialog = ProgressDialog.show(context,"Loading..","Please Wait!",true);
		super.onPreExecute();
	}
	
	@Override
	protected ArrayList<Object> doInBackground(String... params) {
		// TODO Auto-generated method stub
		try{
			SharedPreferences mPrefs = context.getSharedPreferences("StoreDetailsPrefences", Context.MODE_PRIVATE);
			String mUser_id = mPrefs.getString("user_id", "");
			String mResponse=zouponswebservice.activateCardForBilling(mUser_id, mCreditcardId);
			if(!mResponse.equals("")){
				if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){ // Success
					return parsingclass.parseActivateCreditCardResponse(mResponse);
				}else{ // service issues
					return null;
				}
			}else { // failure
				return null;
			}	
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		try{
			if(progressdialog != null && progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			if(result!=null && result.size() >0){
				String response =  (String) result.get(0);
				if(response.equalsIgnoreCase("Credit card Activated.")){
					alertBox_service("Information", "Credit card has been successfully activated");
				}else{
					alertBox_service("Information", "Credit card not activated,please try after sometime");
				}
			}else if(result!=null && result.size() == 0){
				alertBox_service("Information", "No data available");
			}else{
				alertBox_service("Information", "Unable to reach service.");
			}
		}catch(Exception e){
			
		}
			
	}
	
	private void alertBox_service(String title,final String msg) {
		AlertDialog.Builder service_alert = new AlertDialog.Builder(this.context);
		service_alert.setTitle(title);
		service_alert.setMessage(msg);
		service_alert.setCancelable(false);
		service_alert.setNeutralButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(msg.equalsIgnoreCase("Credit card has been successfully activated")){
					CardOnFiles_Adapter.checked_position = checked_position;
					mAdapter.notifyDataSetChanged();
				}
				dialog.dismiss();
			}
		});
		service_alert.show();
	}

}
