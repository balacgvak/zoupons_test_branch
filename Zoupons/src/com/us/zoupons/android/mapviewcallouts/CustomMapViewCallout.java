package com.us.zoupons.android.mapviewcallouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.us.zoupons.R;

/**
 * 
 * Customized Map Callout(Info Window)
 *
 */

public class CustomMapViewCallout implements InfoWindowAdapter{

	private  View convertView;
	
	public CustomMapViewCallout(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.callout_overlay, null);
	}
		
	@Override
	public View getInfoContents(Marker marker) {
		// TODO Auto-generated method stub
		TextView mCallOutShopName = (TextView) convertView.findViewById(R.id.callout_item_shopname);
		TextView mCallOutShopAddress = (TextView) convertView.findViewById(R.id.callout_shop_address);
		mCallOutShopName.setText(marker.getTitle());
		mCallOutShopAddress.setText(marker.getSnippet().split(",,")[1]);
		return convertView;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
