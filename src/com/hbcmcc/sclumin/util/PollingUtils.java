package com.hbcmcc.sclumin.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class PollingUtils {

	public static void startScLuminService(Context context, int seconds, Class<?> cls, String action) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		long triggerAtTime = System.currentTimeMillis();
		am.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, seconds * 1000, pendingIntent);
	}
	
	public static void stopScLuminService(Context context, Class<?> cls, String action) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		am.cancel(pendingIntent);
	}
	
}
