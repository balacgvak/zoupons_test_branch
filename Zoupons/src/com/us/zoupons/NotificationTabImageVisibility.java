package com.us.zoupons;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

public class NotificationTabImageVisibility {

	private Context mContext;
	private ImageView mNotificationImage;

	public NotificationTabImageVisibility(Context context,ImageView notificationImage) {
		this.mContext = context;
		this.mNotificationImage = notificationImage;
	}

	public void checkNotificationVisibility(){
		// To get user type from preference file
		SharedPreferences mPrefs = mContext.getSharedPreferences("UserNamePrefences", Context.MODE_PRIVATE);
		String mUser_type = mPrefs.getString("user_login_type", "");
		if(mUser_type.equalsIgnoreCase("Guest")){
			mNotificationImage.setVisibility(View.INVISIBLE);
		}else{
			mNotificationImage.setVisibility(View.VISIBLE);
		}
	}

}
