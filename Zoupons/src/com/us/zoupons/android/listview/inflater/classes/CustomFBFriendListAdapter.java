package com.us.zoupons.android.listview.inflater.classes;


import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.POJOFBfriendListData;
import com.us.zoupons.android.AsyncThreadClasses.ShareStoreAsynchThread;
import com.us.zoupons.cards.ImageLoader;

public class CustomFBFriendListAdapter extends BaseAdapter {

	private Context context;
	public ImageLoader imageLoader; 
	private ArrayList<Object> mFBList;
	private String mClassFlag;
	
	public CustomFBFriendListAdapter(Context context, ArrayList<Object> mSearchedFriendList,String classflag) {
		Log.i("adapter", "constructor");
		this.context = context;
		this.mFBList = mSearchedFriendList;
		imageLoader = new ImageLoader(context);
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
		Log.i("view", "getview");
		View view = convertView;
		ViewHolder holder;
		if(view == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.fb_friendlist_row, null);
			holder = new ViewHolder();
			holder.mFriendName = (TextView)view.findViewById(R.id.friendnameId);
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
				imageLoader.DisplayImage(mfriendInformation.photo_url,holder.mFriendImage);
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
					ShareStoreAsynchThread sharestoreasynchthread = new ShareStoreAsynchThread(context,mfriendInformation.friend_email);
					sharestoreasynchthread.execute("Email");
				}
			});
		}else{
			holder.mFriendShareButton.setVisibility(View.GONE);
			holder.mZouponseFriendImage.setVisibility(View.GONE);
			holder.mFriendName.setText("No Friends Available...");
		}
		return view;
	}

	static class ViewHolder {
		TextView mFriendName;
		ImageView mFriendImage,mZouponseFriendImage;
		Button mFriendShareButton;
	}
}