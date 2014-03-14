package com.us.zoupons.login;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * 
 * Class to get user current location using Location Manager
 *
 */

public class MyCurrentLocation {
	private LocationManager mLocationManager;
	private LocationResult mLocationResult;
	private boolean mGps_enabled=false;
	private boolean mNetwork_enabled=false;
	private boolean mReturnValue=false;
	
	public boolean getLocation(Context context,LocationResult result){
		mLocationResult=result;
		if(mLocationManager==null)
			mLocationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		try{
			mGps_enabled=mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			mNetwork_enabled=mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//Listeners are not available if no provider is enabled
		if(!mGps_enabled && !mNetwork_enabled){
			mReturnValue=false;
		}else if(mGps_enabled){
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
			mReturnValue=true;
		}else if(mNetwork_enabled){
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
			mReturnValue=true;
		}
		return mReturnValue;
	}
	
	// interface used to fetch geo coordinates..
	LocationListener locationListenerGps = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		
		@Override
		public void onProviderEnabled(String provider) {
		}
		
		@Override
		public void onProviderDisabled(String provider) {
		}
		
		@Override
		public void onLocationChanged(Location location) {
			mLocationResult.gotLocation(location);
			mLocationManager.removeUpdates(this);
			mLocationManager.removeUpdates(locationListenerNetwork);
		}
	};
	
	LocationListener locationListenerNetwork = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		
		@Override
		public void onProviderEnabled(String provider) {
		}
		
		@Override
		public void onProviderDisabled(String provider) {
		}
		
		@Override
		public void onLocationChanged(Location location) {
			mLocationResult.gotLocation(location);
			mLocationManager.removeUpdates(this);
			mLocationManager.removeUpdates(locationListenerGps);
		}
	};
	
	public static abstract class LocationResult{
		public abstract void gotLocation(Location location);
	}
	
	public void removeUpdates(){
		mLocationManager.removeUpdates(locationListenerNetwork);
	}
}