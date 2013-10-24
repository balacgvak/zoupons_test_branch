package com.us.zoupons.android.listview.inflater.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.us.zoupons.R;

public class ManageCards_Adapter extends BaseAdapter{

	private LayoutInflater ManageCardsInflater;
	Context SettingsContext;
	
	public ManageCards_Adapter(Context context){
		ManageCardsInflater=LayoutInflater.from(context);
		this.SettingsContext=context;
	}
	
	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if(convertView==null){
			convertView = ManageCardsInflater.inflate(R.layout.managecards_listview1, null);
			holder=new ViewHolder();
			holder.manageCardDelete=(Button)convertView.findViewById(R.id.btnDelete);
			holder.manageCardDelete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(SettingsContext, "Item Positon : "+ position, Toast.LENGTH_SHORT).show();
				}
			});
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	static class ViewHolder{
		private ImageView managecardImage;
		private TextView managecardNumber;
		private Button manageCardDelete;
	}
}
