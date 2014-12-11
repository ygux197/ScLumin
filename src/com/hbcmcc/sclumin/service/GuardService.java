package com.hbcmcc.sclumin.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class GuardService extends Service{
	
	private String processNameString = "com.hbcmcc.sclumin:scluminservice";
	
	private GuardServiceAIDL guardServiceAIDL = new GuardServiceAIDL.Stub() {
		
		@Override
		public void stopService() throws RemoteException {
			Intent intent = new Intent(GuardService.this, ScLuminService.class);
			GuardService.this.stopService(intent);
		}
		
		@Override
		public void startService() throws RemoteException {
			Log.e("1", "ScLuminService invoke");
			Intent intent = new Intent(GuardService.this, ScLuminService.class);
			GuardService.this.startService(intent);
			
		}
	};
	
	public void onCreate() {
		new Thread() {
			public void run() {
				while (true) {
					if (isProcessNotRunning(GuardService.this, processNameString)) {
						try {
							guardServiceAIDL.startService();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}

	protected boolean isProcessNotRunning(Context context,
			String processNameString) {
		// TODO Auto-generated method stub
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo curService : list) {
			if (curService.processName.equals(processNameString)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
