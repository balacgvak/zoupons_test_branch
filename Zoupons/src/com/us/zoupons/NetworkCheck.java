package com.us.zoupons;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkCheck {
	 //Network information variables
     public ConnectivityManager connection;
	 public NetworkInfo info;
	 public WifiManager wifi;
	 public WifiInfo wifiInfo;
	 public int speed;
	 public Context NetworkContext;
	 public boolean NetworkConnectingFlag=false;
	 
	/**
	 * Function to check network connection 
	 */
	public boolean ConnectivityCheck(Context context) {
		NetworkContext=context;
		//Network connectivity check
		connection = (ConnectivityManager) this.NetworkContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		info = connection.getActiveNetworkInfo();
		wifi = (WifiManager) this.NetworkContext.getSystemService(Context.WIFI_SERVICE);
		wifiInfo = wifi.getConnectionInfo();
		speed=wifiInfo.getLinkSpeed();
		
		if(connection.getActiveNetworkInfo()!=null && connection.getActiveNetworkInfo().isAvailable() && connection.getActiveNetworkInfo().isConnectedOrConnecting()){
			if(connection.getActiveNetworkInfo().isConnected()){
				NetworkConnectingFlag=true;
			}else if(connection.getActiveNetworkInfo().isFailover()){
				NetworkConnectingFlag=false;
			}
		}else{
			NetworkConnectingFlag=false;
		}
		
		return NetworkConnectingFlag;
	}
}




