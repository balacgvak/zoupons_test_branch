package com.us.zoupons.notification;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

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
	    GetNotificationTask mNotificationTask = new GetNotificationTask(NotificationService.this);
	    startMyTask(mNotificationTask);
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
