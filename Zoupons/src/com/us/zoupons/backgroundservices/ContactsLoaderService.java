package com.us.zoupons.backgroundservices;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

public class ContactsLoaderService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		ContactListService mGetContacts = new ContactListService(this);
		if(Build.VERSION.SDK_INT >= 11)
			mGetContacts.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
		else
			mGetContacts.execute();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

}
