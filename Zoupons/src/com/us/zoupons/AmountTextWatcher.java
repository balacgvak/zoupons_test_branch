package com.us.zoupons;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class AmountTextWatcher implements TextWatcher{
	
	private EditText mAmountValue;
	
	public AmountTextWatcher(EditText AmoutValue) {
		this.mAmountValue = AmoutValue;
	}

	@Override
	public void afterTextChanged(Editable s) {}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.i("amount text watcher", "on text change"+" "+s);
		String mEnteredAmount = mAmountValue.getText().toString();
		Log.i("amount text watcher", "entered amount"+" "+mEnteredAmount);
		
		if(mEnteredAmount.length()==1){ // Initial formation of string
			mAmountValue.removeTextChangedListener(this);
			mAmountValue.setCursorVisible(false);
			mAmountValue.setText("0.0"+s);
			mAmountValue.setSelection(mAmountValue.getText().toString().length());
			mAmountValue.addTextChangedListener(this);
		}else{
			mAmountValue.removeTextChangedListener(this);
			String computedAmount = s.toString().replace(".", "");
			Log.i("computed amount", computedAmount);
			String mActualAmount = computedAmount.substring(0, computedAmount.length()-2);
			Log.i("actual amount", mActualAmount);
			String Amount = mActualAmount+"."+computedAmount.substring(computedAmount.length()-2,computedAmount.length());
			Log.i("amount", Amount);
			if(Amount.charAt(0) == '0' && Amount.length()>3){ // To replace extra "0" from first position
				Amount = Amount.replaceFirst("0", "");
			}else if(Amount.length() <= 3){
				Amount = String.format("%.2f",Float.valueOf(Amount));	
			}
			mAmountValue.setText(Amount);
			mAmountValue.setSelection(mAmountValue.getText().toString().length());
			mAmountValue.addTextChangedListener(this);
		}
	}
}