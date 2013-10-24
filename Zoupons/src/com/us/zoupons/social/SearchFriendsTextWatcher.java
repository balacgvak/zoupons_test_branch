package com.us.zoupons.social;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ListView;

import com.us.zoupons.ClassVariables.POJOFBfriendListData;
import com.us.zoupons.StaticArrays.WebServiceStaticArrays;
import com.us.zoupons.android.listview.inflater.classes.CustomFBFriendListAdapter;


public class SearchFriendsTextWatcher implements TextWatcher{

	private Context mContext;
	private ListView mFriendsList;
	String mClassFlag;
	
	public SearchFriendsTextWatcher(Context context, ListView friendslist, String classflag) {
		this.mContext = context;
		this.mFriendsList = friendslist;
		this.mClassFlag = classflag;
	}

	@Override
	public void afterTextChanged(Editable s) {}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String searchString=s.toString();
		WebServiceStaticArrays.mSearchedFriendList.clear();
		for(int i=0;i<WebServiceStaticArrays.mSocialNetworkFriendList.size();i++)
		{
			POJOFBfriendListData friendData = (POJOFBfriendListData) WebServiceStaticArrays.mSocialNetworkFriendList.get(i);
			String friendName = friendData.name;
			String imageURL = friendData.photo_url;
			String zouponsFriend = friendData.zouponsfriend;
			String friendId = friendData.friend_id;
			if(friendName.toLowerCase().contains(searchString.toLowerCase())){
				POJOFBfriendListData searchfrienddata = new POJOFBfriendListData();
				searchfrienddata.friend_id = friendId;
				searchfrienddata.name = friendName;
				searchfrienddata.photo_url = imageURL;
				searchfrienddata.zouponsfriend = zouponsFriend;
				searchfrienddata.friend_email = friendData.friend_email;
				WebServiceStaticArrays.mSearchedFriendList.add(searchfrienddata);
			}
		}
		mFriendsList.setAdapter(new CustomFBFriendListAdapter(mContext,WebServiceStaticArrays.mSearchedFriendList,mClassFlag));
	}

}
