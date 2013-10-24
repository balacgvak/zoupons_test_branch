package com.us.zoupons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class DateValidator implements TextWatcher{

	private EditText mExpirationDateValue,mCVVvalue;
	private boolean mIsForRemove;
	private Context mContext;


	public DateValidator(Context context,EditText expirationdate , EditText cvvValue) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mExpirationDateValue = expirationdate;
		this.mCVVvalue = cvvValue;
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		Log.i("before s", s.toString()+" " + s.toString().length());
		try{
			if(!s.toString().equalsIgnoreCase("") && !s.toString().startsWith("0")){  // Check for empty to avoid exception while clearing all text

				if((s.toString().length()>2 && s.toString().length()<=5) && (s.toString().contains("/")|| s.toString().contains(""))){
					checkForRemoveAndAssignCharacters(s);
				}else{
					if(s.toString().contains("/") && s.toString().length() > 5){  
						s = s.toString().substring(0, 2)+s.toString().substring(5,s.toString().length());
						mIsForRemove = true; // useful for whether to append '/' or not when length is 2
					}else if(s.toString().contains("/")){ 
						s = s.toString().substring(0, 2);
						mIsForRemove = true; // useful for whether to append '/' or not when length is 2
					}else if(mIsForRemove && s.toString().length() == 2){
						mIsForRemove = true; // useful for whether to append '/' or not when length is 2
					}else{
						mIsForRemove = false; // useful for whether to append '/' or not when length is 2
					}
					Log.i("after s", s.toString()+" " + s.toString().length());
					int enteredvalue = Integer.valueOf(s.toString());
					Log.i("entered value", enteredvalue+"");
					if(enteredvalue <=1){
						mExpirationDateValue.removeTextChangedListener(this);
						mExpirationDateValue.setText(s.toString());
						mExpirationDateValue.setSelection(s.toString().length());
						mExpirationDateValue.setTextColor(mContext.getResources().getColor(R.color.black));
						mExpirationDateValue.setTag("valid");
						mExpirationDateValue.addTextChangedListener(this);
					}else if(enteredvalue > 12 && enteredvalue < 20){
						mExpirationDateValue.removeTextChangedListener(this);
						mExpirationDateValue.setText("1");
						mExpirationDateValue.setSelection(1);
						mExpirationDateValue.setTextColor(mContext.getResources().getColor(R.color.black));
						mExpirationDateValue.setTag("valid");
						mExpirationDateValue.addTextChangedListener(this);
					}else{
						String date = String.format("%02d",enteredvalue);
						Log.i("Before append date", date.toString() + " "+mIsForRemove);
						if(date.length() < 2){
							date = date+" / ";
						}else if(!mIsForRemove && date.length() == 2){
							date = date+" / ";
						}else if(mIsForRemove && date.length() == 2){

						}else{
							date = date.substring(0, 2)+" / "+date.substring(2, date.length());
						}
						if(date.length() == 7){
							checkDate(date);
						}else{
							Log.i("After append date", date.toString());
							mExpirationDateValue.removeTextChangedListener(this);
							mExpirationDateValue.setText(date.toString());
							mExpirationDateValue.setSelection(mExpirationDateValue.getText().toString().length());
							mExpirationDateValue.setTextColor(mContext.getResources().getColor(R.color.black));
							mExpirationDateValue.setTag("valid");
							mExpirationDateValue.addTextChangedListener(this);
						}
					}

				}
			}else if(!s.toString().equalsIgnoreCase("") && s.toString().startsWith("0") && !s.toString().equalsIgnoreCase("00")){
				if((s.toString().length()>2 && s.toString().length()<=5) && (s.toString().contains("/")|| s.toString().contains(""))){
					checkForRemoveAndAssignCharacters(s);
				}else{
					if(mIsForRemove && s.toString().length() == 2){
						mIsForRemove = true; // useful for whether to append '/' or not when length is 2
					}else{
						mIsForRemove = false; // useful for whether to append '/' or not when length is 2
					}
					if(!s.toString().contains("/") && !mIsForRemove && s.toString().length() == 2){
						mExpirationDateValue.removeTextChangedListener(this);
						mExpirationDateValue.setText(s.toString()+" / ");
						mExpirationDateValue.setTextColor(mContext.getResources().getColor(R.color.black));
						mExpirationDateValue.setTag("valid");
						mExpirationDateValue.setSelection(mExpirationDateValue.getText().toString().length());
						mExpirationDateValue.addTextChangedListener(this);
					}
					if(s.toString().length() == 7){
						checkDate(s.toString());
					}else{
						mExpirationDateValue.setTextColor(mContext.getResources().getColor(R.color.black));
						mExpirationDateValue.setTag("valid");
					}
				}
			}else if(!s.toString().equalsIgnoreCase("") && s.toString().equalsIgnoreCase("00")){
				mExpirationDateValue.removeTextChangedListener(this);
				mExpirationDateValue.setText("0");
				mExpirationDateValue.setSelection(1);
				mExpirationDateValue.setTextColor(mContext.getResources().getColor(R.color.black));
				mExpirationDateValue.setTag("valid");
				mExpirationDateValue.addTextChangedListener(this);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkForRemoveAndAssignCharacters(CharSequence s){
		if(s.toString().endsWith(" ") || s.toString().endsWith("/")){
			mExpirationDateValue.removeTextChangedListener(this);
			mExpirationDateValue.setText(s.toString());
			mExpirationDateValue.setTextColor(mContext.getResources().getColor(R.color.black));
			mExpirationDateValue.setTag("valid");
			mExpirationDateValue.setSelection(s.toString().length());
			mExpirationDateValue.addTextChangedListener(this);
			mIsForRemove = true;	
		}else{
			mExpirationDateValue.removeTextChangedListener(this);
			mExpirationDateValue.setText(s.toString().substring(0, 2)+" / "+s.toString().substring(s.toString().length()-1,s.toString().length()));
			mExpirationDateValue.setTextColor(mContext.getResources().getColor(R.color.black));
			mExpirationDateValue.setTag("valid");
			mExpirationDateValue.setSelection(mExpirationDateValue.getText().toString().length());
			mExpirationDateValue.addTextChangedListener(this);
		}
	}

	private void checkDate(String date){
		try{
			Log.i("splitted date", date.split(" ")[0]+date.split(" ")[1]+date.split(" ")[2]);
			Calendar mCurrentCalandar = Calendar.getInstance();
			Calendar mValidityCheckCalandar = Calendar.getInstance();
			Calendar mEnteredCalandar = Calendar.getInstance();
			mValidityCheckCalandar.add(Calendar.YEAR, 15);
			DateFormat dateFormat = new SimpleDateFormat("MM/yy"); 
			Date startDate = dateFormat.parse(date.split(" ")[0]+date.split(" ")[1]+date.split(" ")[2]);
			mEnteredCalandar.setTime(startDate);
			Log.i("cal check", mCurrentCalandar.getTime()+" ");
			Log.i("cal check", mValidityCheckCalandar.getTime()+" ");
			Log.i("Year check", mEnteredCalandar.get(Calendar.YEAR)+" "+mValidityCheckCalandar.get(Calendar.YEAR));
			if((startDate.compareTo(mCurrentCalandar.getTime()) < 0) || (mEnteredCalandar.get(Calendar.YEAR) > mValidityCheckCalandar.get(Calendar.YEAR)) ){
				mExpirationDateValue.setTextColor(mContext.getResources().getColor(R.color.red));
				mExpirationDateValue.setTag("invalid");
				Log.i("color", "red");
			}else{
				mExpirationDateValue.setTextColor(mContext.getResources().getColor(R.color.black));
				mExpirationDateValue.setTag("valid");
				Log.i("color", "black");
				mCVVvalue.requestFocus();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
