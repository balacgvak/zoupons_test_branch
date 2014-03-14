package com.us.zoupons.listview_inflater_classes;

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

import com.us.zoupons.classvariables.Search_ClassVariables;
import com.us.zoupons.shopper.webService.ZouponsParsingClass;
import com.us.zoupons.shopper.webService.ZouponsWebService;

/**
 * 
 * Adapter to list stores while searching..
 *
 */

public class CustomStoreAdapter extends ArrayAdapter<Object> implements Filterable{

	private Context mContext;
	private int mLayout;
	private ArrayList<Object> stores;
	private CustomFilter itemFilter;
	private ZouponsWebService zouponswebservice;
	private ZouponsParsingClass parsingclass;
	public static boolean isSearchStringChanged;
	private String mSearchClassFlag;

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
			}else{
				prefix = "";
			}

			if(!isSearchStringChanged){
				if(results.count > 0 ){
					stores = (ArrayList<Object>) results.values;
					notifyDataSetChanged();
				}else{
					notifyDataSetInvalidated();
				}
			}else{
				
			}
		}

	}
	static class ViewHolder
	{
		TextView sStoreName;
	}		
}



