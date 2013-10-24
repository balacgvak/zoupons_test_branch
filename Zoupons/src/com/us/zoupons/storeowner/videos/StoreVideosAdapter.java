package com.us.zoupons.storeowner.videos;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.us.zoupons.R;
import com.us.zoupons.ClassVariables.POJOVideoURL;
import com.us.zoupons.cards.ImageLoader;

public class StoreVideosAdapter extends BaseAdapter implements OnCheckedChangeListener{
	Context mContext;
	static int checked_position=-1;
	ArrayList<Object> mAllStoreLocationVideos;
	ImageLoader mImageLoader;

	public StoreVideosAdapter(Context context,ArrayList<Object> AllStoreLocationVideos){
		this.mContext=context;
		this.mAllStoreLocationVideos = AllStoreLocationVideos;
		mImageLoader = new ImageLoader(mContext);
	}

	@Override
	public int getCount() {
		return mAllStoreLocationVideos.size();
	}

	@Override
	public Object getItem(int position) {
		return mAllStoreLocationVideos.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try{
			ViewHolder holder;
			if(convertView==null){
				convertView=LayoutInflater.from(mContext).inflate(R.layout.storeownervideos_listrow, null);
				holder=new ViewHolder();
				holder.mVideoThumbnail=(ImageView) convertView.findViewById(R.id.video_thumbnailId);
				holder.mVideoName=(TextView) convertView.findViewById(R.id.video_nameId);
				holder.mVideoActiveCheckBox=(CheckBox) convertView.findViewById(R.id.video_checkBoxId);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			final POJOVideoURL mVideoDetails = (POJOVideoURL) mAllStoreLocationVideos.get(position);
			mImageLoader.DisplayImage(mVideoDetails.VideoThumbNail, holder.mVideoThumbnail);
			holder.mVideoName.setText(mVideoDetails.VideoTitle);
			holder.mVideoActiveCheckBox.setTag(position);
			if(checked_position != -1){
				if(position == checked_position){
					holder.mVideoActiveCheckBox.setOnCheckedChangeListener(null);
					holder.mVideoActiveCheckBox.setChecked(true);
				}else{
					holder.mVideoActiveCheckBox.setOnCheckedChangeListener(null);
					holder.mVideoActiveCheckBox.setChecked(false);
				}	
			}else{
			}
			holder.mVideoActiveCheckBox.setOnCheckedChangeListener(this);
		}catch(Exception e){
			e.printStackTrace();
		}
		return convertView;
	}

	static class ViewHolder{
		private TextView mVideoName;
		private ImageView mVideoThumbnail;
		private CheckBox mVideoActiveCheckBox;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		checked_position = Integer.valueOf(buttonView.getTag().toString());
		notifyDataSetChanged();
	}
}