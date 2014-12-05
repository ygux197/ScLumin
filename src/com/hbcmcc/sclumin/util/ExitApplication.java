package com.hbcmcc.sclumin.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.app.Activity;

public class ExitApplication extends Application {

	private List<Activity> activityList = null;
	private static ExitApplication instance;
	
	public ExitApplication() {
		activityList = new ArrayList<Activity>();
		instance = null;
	}
	
	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;
	}
	
	public void addActivity(Activity act) {
		activityList.add(act);
	}
	
	public void exit() {
		for (Activity act : activityList) {
			act.finish();
		}
		System.exit(0);
	}
	
}
