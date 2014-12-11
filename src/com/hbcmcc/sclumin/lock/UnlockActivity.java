package com.hbcmcc.sclumin.lock;

import com.hbcmcc.sclumin.R;
import com.hbcmcc.sclumin.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

public class UnlockActivity extends LockActivity {
	
	String action;
	SharedPreferences sharedPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lockButton.setText(getString(R.string.unlock));
		action = getIntent().getAction();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.title_unlock);
		sharedPref = this.getSharedPreferences(Properties.SHAREDPREFERENCES_NAME, MODE_APPEND);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(Activity.RESULT_CANCELED);
			this.finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public boolean startAuth(String id, String pwd) {
		if (id.equals(sharedPref.getString("id", new String())) && 
				pwd.equals(sharedPref.getString("pwd", new String()))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void postExecute(Boolean result) {
		result = true;
		
		if (result) {
			Toast.makeText(UnlockActivity.this, getString(R.string.prompt_unlock_suc), Toast.LENGTH_SHORT).show();
			if (Properties.UNLOCK_ACTION.equalsIgnoreCase(action)) {
				Intent intent = new Intent().putExtra("req", 100);
				setResult(Activity.RESULT_OK, intent);
				finish();
			} else {
				Intent intent = new Intent(UnlockActivity.this, LockActivity.class);
				startActivity(intent);
				finish();
			}
		} else {
			Toast.makeText(UnlockActivity.this, getString(R.string.error_incorrect), Toast.LENGTH_LONG).show();  
			pwdView.requestFocus();
		}
	
	}



	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}
}
