package com.us.zoupons.android.listview.inflater.classes;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.us.zoupons.ClassVariables.Search_ClassVariables;
import com.us.zoupons.WebService.ZouponsParsingClass;
import com.us.zoupons.WebService.ZouponsWebService;

public class CustomStoreAdapter extends ArrayAdapter<Object> implements Filterable{

	Context mContext;
	int mLayout;
	ArrayList<Object> stores;
	CustomFilter itemFilter;
	ZouponsWebService zouponswebservice;
	ZouponsParsingClass parsingclass;
	public static boolean isSearchStringChanged;
	String mSearchClassFlag;

	public CustomStoreAdapter(Context context, int textViewResourceId,String searchclassflag) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		mLayout = textViewResourceId;
		zouponswebservice = new ZouponsWebService(context);
		parsingclass = new ZouponsParsingClass(context);
		stores = new ArrayList<Object>();
		this.mSearchClassFlag=searchclassflag;
	}

	@Override
	public int getCount() {
		return stores.size();
	}

	@Override
	public Object getItem(int position) {
		return stores.get(position);
	}

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

	@Override
	public Filter getFilter() {
		Log.i("override methods", "getFilter");
		if (itemFilter == null) {
			itemFilter = new CustomFilter();
		}
		return itemFilter;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;

		if(view == null)
		{
			view = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
			holder = new ViewHolder();
			holder.sStoreName = (TextView)view.findViewById(android.R.id.text1);
			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)view.getTag();
		}
		Search_ClassVariables store_data = (Search_ClassVariables) getItem(position); 
		holder.sStoreName.setText(store_data.storeName);

		return view;
	}



	private class CustomFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			Log.i("custom filter", "perform Filter");
			FilterResults results = new FilterResults();
			isSearchStringChanged= false;
			try{
				ArrayList<Object> filterstores= new ArrayList<Object>();
				if (prefix == null || prefix.length() == 0) {
					results.values = filterstores;
					results.count = filterstores.size();
				}else{
					String mResponse = zouponswebservice.search("store", prefix.toString(),mSearchClassFlag.equals("home")?"":mSearchClassFlag);
					if(!mResponse.equals("failure") && !mResponse.equals("noresponse")){
						for(Search_ClassVariables obj : parsingclass.parseStoreSearch(mResponse)){
							filterstores.add(obj.clone());
						}
					}

					Search_ClassVariables df;

					results.values = filterstores;
					results.count =filterstores.size();
				}

			}catch(Exception e){
				e.printStackTrace();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence prefix, FilterResults results) {
			if(prefix !=null && prefix.length() != 0){
				Log.i("custom filter", "prefix" + prefix.toString());
			}else{
				Log.i("custom filter", "null");
				prefix = "";
			}

			if(!isSearchStringChanged){
				if(results.count > 0 ){
					Log.i("adapter", "notify data");
					stores = (ArrayList<Object>) results.values;
					notifyDataSetChanged();
				}else{
					Log.i("adapter", " un notify data");
					notifyDataSetInvalidated();
				}
			}else{
				Log.i("adapter", "leave as it is");
			}
		}

	}
	static class ViewHolder
	{
		TextView sStoreName;
	}		
}



