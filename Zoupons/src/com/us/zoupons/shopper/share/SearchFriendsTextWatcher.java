package com.us.zoupons.shopper.share;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;

import com.us.zoupons.classvariables.POJOFBfriendListData;
import com.us.zoupons.collections.WebServiceStaticArrays;
import com.us.zoupons.listview_inflater_classes.CustomFBFriendListAdapter;

/**
 * 
 * Custom text watcher which listens for friend name and updates friends list
 *
 */

public class SearchFriendsTextWatcher implements TextWatcher{

	private Activity mContext;
	private ListView mFriendsList;
	private String mClassFlag;
	
	public SearchFriendsTextWatcher(Activity context, ListView friendslist, String classflag) {
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
		// Set searched friend list
		mFriendsList.setAdapter(new CustomFBFriendListAdapter(mContext,WebServiceStaticArrays.mSearchedFriendList,mClassFlag));
	}

}
