/**
 * 
 */
package com.us.zoupons;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;

/**
 * Class to watch the mobilenumber edittext to add "-" while adding remove " - and its previous number (4-)" while deleting
 */
public class MobileNumberTextWatcher implements TextWatcher{

	private boolean mIsFormatting;
    private boolean mDeletingHyphen;
    private int mHyphenStart;
    private boolean mDeletingBackward;
    
	@Override
	public void afterTextChanged(Editable text) {
		if (mIsFormatting)
            return;

        mIsFormatting = true;

        // If deleting hyphen, also delete character before or after it
        if (mDeletingHyphen && mHyphenStart > 0) {
            if (mDeletingBackward) {
                if (mHyphenStart - 1 < text.length()) {
                    text.delete(mHyphenStart - 1, mHyphenStart);
                }
            } else if (mHyphenStart < text.length()) {
                text.delete(mHyphenStart, mHyphenStart + 1);
            }
        }
        if (text.length() == 3 || text.length() == 7) {
            text.append('-');
        }

        mIsFormatting = false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		if (mIsFormatting)
            return;

        // Make sure user is deleting one char, without a selection
        final int selStart = Selection.getSelectionStart(s);
        final int selEnd = Selection.getSelectionEnd(s);
        if (s.length() > 1 // Can delete another character
                && count == 1 // Deleting only one character
                && after == 0 // Deleting
                && s.charAt(start) == '-' // a hyphen
                && selStart == selEnd) { // no selection
            mDeletingHyphen = true;
            mHyphenStart = start;
            // Check if the user is deleting forward or backward
            if (selStart == start + 1) {
                mDeletingBackward = true;
            } else {
                mDeletingBackward = false;
            }
        } else {
            mDeletingHyphen = false;
        }
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
