package com.us.zoupons;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 
 * class used for formatting the amount in standard US Format
 *
 */

public class AmountTextWatcher implements TextWatcher{
	// Initialization of views and variables
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
		String mEnteredAmount = mAmountValue.getText().toString();
		if(!mEnteredAmount.equalsIgnoreCase("")){
			if(mEnteredAmount.length()==1){ // Initial formation of string
				mAmountValue.removeTextChangedListener(this);
				mAmountValue.setCursorVisible(false);
				mAmountValue.setText("0.0"+s);
				mAmountValue.setSelection(mAmountValue.getText().toString().length());
				mAmountValue.addTextChangedListener(this);
			}else{ // Formating amount entered to standard US format.
				mAmountValue.removeTextChangedListener(this);
				String computedAmount = s.toString().replace(".", "");
				String mActualAmount = computedAmount.substring(0, computedAmount.length()-2);
				String Amount = mActualAmount+"."+computedAmount.substring(computedAmount.length()-2,computedAmount.length());
				if(Amount.charAt(0) == '0' && Amount.length()>3){ // To replace extra "0" from first position
					Amount = Amount.replaceFirst("0", "");
				}else if(Amount.length() <= 3){
					Amount = String.format("%.2f",Float.valueOf(Amount));	
				}
				if(Amount.startsWith(".")){
					mAmountValue.setText("0"+Amount);	
				}else{
					mAmountValue.setText(Amount);	
				}
				mAmountValue.setSelection(mAmountValue.getText().toString().length());
				mAmountValue.addTextChangedListener(this);
			}
		}else{
			
		}
	}
}