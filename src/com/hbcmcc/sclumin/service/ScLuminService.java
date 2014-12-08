package com.hbcmcc.sclumin.service;

import com.hbcmcc.sclumin.lock.LockActivity;
import com.hbcmcc.sclumin.loopview.AdsActivity;
import com.hbcmcc.sclumin.util.Config;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ScLuminService extends Service {
	
	public static String id = null;
	public static String pwd = null;
	
	public static final String ACTION = "com.hbcmcc.sclumin.service.ScLuminService";
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		
		BroadcastReceiver screenReceiver = new BroadcastReceiver() {
			Class<?> cls = null;
			
			@Override
			public void onReceive(Context context, Intent intent) {
				if (null != id && null != pwd) {
					cls = AdsActivity.class;
				} else {
					cls = LockActivity.class;
				}
				String action = intent.getAction();
				if (action.equals(Intent.ACTION_SCREEN_ON)) {
					if (Config.DEBUG) Log.e(BroadcastReceiver.class.toString(), "ACTION_SCREEN_ON action received!");		
				} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
					if (Config.DEBUG) Log.e(BroadcastReceiver.class.toString(), "ACTION_SCREEN_OFF action received!");
					Intent intent2 = new Intent(ScLuminService.this, cls);
					intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent2);
				}
			}
		};
		registerReceiver(screenReceiver, filter);
	}

}
