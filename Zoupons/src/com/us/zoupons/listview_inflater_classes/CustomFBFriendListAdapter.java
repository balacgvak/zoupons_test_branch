package com.us.zoupons.listview_inflater_classes;


import java.util.ArrayList;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.android.asyncthread_class.ShareStoreAsynchThread;
import com.us.zoupons.classvariables.POJOFBfriendListData;

/**
 * 
 * Adapter to list Gmail friends
 *
 */

public class CustomFBFriendListAdapter extends BaseAdapter {

	private Activity mContext;
	private ArrayList<POJOFBfriendListData> mFBList;
	private String mClassFlag;
	
	public CustomFBFriendListAdapter(Activity context, ArrayList<POJOFBfriendListData> mSearchedFriendList,String classflag) {
		this.mContext = context;
		this.mFBList = mSearchedFriendList;
		this.mClassFlag = classflag;
	}

	public int getCount() {
		if(mFBList.size() >0){
			return mFBList.size();
		}else{
			return 1;
		}
	}

	public Object getItem(int position) {
		return mFBList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if(view == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			view = inflater.inflate(R.layout.fb_friendlist_row, null);
			holder = new ViewHolder();
			holder.mFriendName = (TextView)view.findViewById(R.id.friendnameId);
			holder.mFriendEmail = (TextView)view.findViewById(R.id.friendEmailId);
			holder.mFriendImage = (ImageView)view.findViewById(R.id.friendimageId);
			holder.mZouponseFriendImage = (ImageView)view.findViewById(R.id.zouponsfriendimageId);
			holder.mFriendShareButton = (Button)view.findViewById(R.id.friendlist_share_buttonId);
			view.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		if(mFBList.size()>0){
			final POJOFBfriendListData mfriendInformation = (POJOFBfriendListData) mFBList.get(position);
			if(mfriendInformation != null) {
				holder.mFriendName.setText(mfriendInformation.name.equalsIgnoreCase("") ? mfriendInformation.friend_email : mfriendInformation.name);
				holder.mFriendEmail.setText(mfriendInformation.friend_email);
				if(mfriendInformation.photo_url != null && !mfriendInformation.photo_url.equalsIgnoreCase(""))
				    try{
				    	holder.mFriendImage.setImageURI(Uri.parse(mfriendInformation.photo_url));	
				    }catch (Exception e) {
				    	holder.mFriendImage.setImageResource(R.drawable.profileimage);
					}
				else
					holder.mFriendImage.setImageResource(R.drawable.profileimage);
				if(mfriendInformation.zouponsfriend.equalsIgnoreCase("yes")){
					holder.mZouponseFriendImage.setVisibility(View.VISIBLE);
				}else{
					holder.mZouponseFriendImage.setVisibility(View.GONE);
				} 
			}
			// To show or hide share button in friend list
			if(mClassFlag.toString().equalsIgnoreCase("Social")){ // From Social ... show share button
				holder.mFriendShareButton.setVisibility(View.VISIBLE);
			}else{  // From Friends ... Hide share button
				holder.mFriendShareButton.setVisibility(View.GONE);
			}

			holder.mFriendShareButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ShareStoreAsynchThread sharestoreasynchthread = new ShareStoreAsynchThread(mContext,mfriendInformation.friend_email,mfriendInformation.friend_id);
					sharestoreasynchthread.execute("Email");
				}
			});
			
		}else{
			holder.mFriendShareButton.setVisibility(View.GONE);
			holder.mZouponseFriendImage.setVisibility(View.GONE);
			holder.mFriendName.setText("No friends available");
		}
		return view;
	}

	static class ViewHolder {
		TextView mFriendName,mFriendEmail;
		ImageView mFriendImage,mZouponseFriendImage;
		Button mFriendShareButton;
	}
}