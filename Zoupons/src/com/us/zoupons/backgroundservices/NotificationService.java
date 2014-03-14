package com.us.zoupons.backgroundservices;

import com.us.zoupons.notification.GetNotificationTask;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

/**
 * 
 * Background service to communicate with server to get notification list for 30 seconds interval
 *
 */


public class NotificationService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		//Log.i("service","on create");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		//Log.i("service","on start");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Log.i("service","on start commmand");
		try{
			if(intent.hasExtra("notify_to_type") || intent.getExtras().getString("notify_to_type") != null){
				GetNotificationTask mNotificationTask = new GetNotificationTask(NotificationService.this,intent.getExtras().getString("notify_to_type"));
				startMyTask(mNotificationTask);	
			}else{
				stopSelf();
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		//Log.i("service","on destroy");
		super.onDestroy();
	}
	
	void startMyTask(AsyncTask<String, String, String> asyncTask) {
		if(Build.VERSION.SDK_INT >= 11)
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			asyncTask.execute();
	}
}
