package com.us.zoupons;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyCurrentLocation {
	LocationManager mLocationManager;
	LocationResult mLocationResult;
	boolean gps_enabled=false;
	boolean network_enabled=false;
	boolean returnValue=false;
	
	public boolean getLocation(Context context,LocationResult result){
		mLocationResult=result;
		if(mLocationManager==null)
			mLocationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		try{
			gps_enabled=mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			network_enabled=mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//Listeners are not available if no provider is enabled
		if(!gps_enabled && !network_enabled){
			returnValue=false;
		}else if(gps_enabled){
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
			returnValue=true;
		}else if(network_enabled){
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
			returnValue=true;
		}
		return returnValue;
	}
	
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