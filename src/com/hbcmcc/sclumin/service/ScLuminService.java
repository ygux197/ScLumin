package com.hbcmcc.sclumin.service;

import java.util.List;

import com.hbcmcc.sclumin.loopview.AdsActivity;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class ScLuminService extends Service{

	public static final String ACTION = "com.hbcmcc.sclumin.service.ScLuminService";

	private String processNameString = "com.hbcmcc.sclumin:guardservice";

	private ScLuminServiceAIDL scSerivceAIDL = new ScLuminServiceAIDL.Stub() {

		@Override
		public void stopService() throws RemoteException {
			Intent intent = new Intent(ScLuminService.this, GuardService.class);
			ScLuminService.this.stopService(intent);
		}

		@Override
		public void startService() throws RemoteException {
			Intent intent = new Intent(ScLuminService.this, GuardService.class);
			ScLuminService.this.startService(intent);
		}
	};

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

		filter.addAction(Intent.ACTION_TIME_TICK);

		BroadcastReceiver screenReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(Intent.ACTION_SCREEN_ON)) {
				} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
					Intent intent2 = new Intent(ScLuminService.this, AdsActivity.class);
					intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent2);
				} else if (action.equals(Intent.ACTION_TIME_TICK)) {
//					String packageName = ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1).get(0).topActivity.getPackageName();
//					Log.e("timetick", packageName);
//					if (packageName.contains("launcher") || packageName.contains("Launcher") || packageName.contains("LAUNCHER")) {
//						tickCount++;
//					} else {
//						tickCount = 0;
//					}
//
//					if (tickCount >= 3) {
//						tickCount = 0;
//						Intent tickIntent = new Intent(ScLuminService.this, AdsActivity.class);
//						tickIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						startActivity(tickIntent);
//					}
				} 
			}
		};
		registerReceiver(screenReceiver, filter);

		new Thread() {
			public void run() {
				while (true) {
					if (isGuardProcessNotRunning(ScLuminService.this, processNameString)) {
						try {
							scSerivceAIDL.startService();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}

	protected boolean isGuardProcessNotRunning(Context context,
			String processNameString2) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo curService : list) {
			Log.e("app", curService.processName);
			if (curService.processName.equals(processNameString)) {
				return false;
			}
		}
		return true;
	}
}
