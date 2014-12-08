package com.hbcmcc.sclumin.service;

import com.hbcmcc.sclumin.lock.LockActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BootstrapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent scLuminService = new Intent(BootstrapActivity.this, ScLuminService.class);
		startService(scLuminService);
		
		Intent lockActivity = new Intent(BootstrapActivity.this, LockActivity.class);
		startActivity(lockActivity);
		
		finish();
		
	}

}
