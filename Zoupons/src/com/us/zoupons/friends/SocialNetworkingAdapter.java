package com.us.zoupons.friends;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.R;
import com.us.zoupons.GoogleAccountHelper.GoogleAuthentication;
import com.us.zoupons.cards.ImageLoader;

public class SocialNetworkingAdapter extends BaseAdapter{

	private LayoutInflater SocialNetworkingInflater;
	private Activity SocialNetworkingContext;
	private ArrayList<Object> mSocialNetworkingList;
	public ImageLoader imageLoader; 
	private ViewHolder holder;
	
	public SocialNetworkingAdapter(Activity context, ArrayList<Object> mSocialNetworkingListDetails){
		SocialNetworkingInflater = LayoutInflater.from(context);
		this.SocialNetworkingContext = context;
		this.mSocialNetworkingList = mSocialNetworkingListDetails;
		imageLoader=new ImageLoader(context);
	}
		
	@Override
	public int getCount() {
		return mSocialNetworkingList.size();
	}

	@Override
	public Object getItem(int position) {
		return mSocialNetworkingList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		holder = null;
		Log.i("favourites", "getview");
		if(convertView==null){
			convertView=SocialNetworkingInflater.inflate(R.layout.friends_social_networking_types, null);
			holder=new ViewHolder();
			holder.mFriendsCountTextId=(TextView) convertView.findViewById(R.id.social_netwoking_friends_count_id);
			holder.mLastUpdateDateTextId=(TextView) convertView.findViewById(R.id.social_netwoking_friends_lastupdate_id);
			holder.mSocial_FriendImportButton=(Button) convertView.findViewById(R.id.social_netwoking_friends_importId);
			holder.mSocialNetworkingLogo=(ImageView) convertView.findViewById(R.id.social_netwoking_logo_id);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		SocialNetworkingDetails mSocialNetworkDetails = (SocialNetworkingDetails) getItem(position);
		if(position == 0){
			holder.mSocialNetworkingLogo.setImageResource(R.drawable.google);
		}else if(position == 1){
			holder.mSocialNetworkingLogo.setImageResource(R.drawable.google);
		}
		holder.mFriendsCountTextId.setText("Friends Count : "+mSocialNetworkDetails.mFriendsCount);
		holder.mLastUpdateDateTextId.setText("Last Updated : "+mSocialNetworkDetails.mLastUpdatedDate);
		if(mSocialNetworkDetails.mFriendsCount.equalsIgnoreCase("0")){
			holder.mSocial_FriendImportButton.setText("Import");
		}else{
			holder.mSocial_FriendImportButton.setText("Update");
		}
		holder.mSocial_FriendImportButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(position == 0){
					Intent intent_socialnetworking = new Intent(SocialNetworkingContext,GoogleAuthentication.class);
					intent_socialnetworking.putExtra("buttontext", holder.mSocial_FriendImportButton.getText().toString().trim());
					SocialNetworkingContext.startActivityForResult(intent_socialnetworking, 700);	
				}else if(position == 1){
					Toast.makeText(SocialNetworkingContext, "Currently unavailable", Toast.LENGTH_SHORT).show();
				}else if(position == 2){
					Toast.makeText(SocialNetworkingContext, "Currently unavailable", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return convertView;
	}

	static class ViewHolder{
		private TextView mFriendsCountTextId,mLastUpdateDateTextId;
		private Button mSocial_FriendImportButton;
		private ImageView mSocialNetworkingLogo;
		
	}
}