package com.hbcmcc.sclumin.lock;

import com.hbcmcc.sclumin.R;
import com.hbcmcc.sclumin.gallery.GalleryMainActivity;
import com.hbcmcc.sclumin.util.Properties;
import com.hbcmcc.sclumin.util.ExitApplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LockActivity extends Activity {
	
	public EditText idView = null;
	public EditText pwdView = null;
	public Button lockButton = null;
	private View progressView = null;
	private View lockFormView = null;
	private long exitTime = 0;
	private String action;
	public ActionBar actionBar;

	public static final int MIN_PWD_LENGTH = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getActionBar();
		actionBar.setTitle(R.string.title_lock);
		actionBar.show();
		setContentView(R.layout.activity_lock);
		ExitApplication.getInstance().addActivity(this);
		
		idView = (EditText) findViewById(R.id.id);
		pwdView = (EditText) findViewById(R.id.password);
		pwdView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent event) {
				if (id == R.id.lock || id == EditorInfo.IME_NULL) {
					attemptLock();
					return true;
				}
				return false;
			}

		});
		
		lockButton = (Button) findViewById(R.id.lock_button);
		lockButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLock();
			}
		});
		
		progressView = findViewById(R.id.lock_progress);
		lockFormView = findViewById(R.id.lock_form);
		action = this.getIntent().getAction();
		if (Properties.LOCK_ACTION.equalsIgnoreCase(action)) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		} else {
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Properties.LOCK_ACTION.equalsIgnoreCase(action) && 
				android.R.id.home == item.getItemId()) {
			setResult(Activity.RESULT_CANCELED);
			this.finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		Window window = this.getWindow();
		window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}
	
	/*
	 * 两次返回退出程序
	 */
	public void doubleExit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {  
		    Toast.makeText(LockActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
		    exitTime = System.currentTimeMillis();  
		  } else {  
		    ExitApplication.getInstance().exit();
		  }  
	}

	private void attemptLock() {
		idView.setError(null);
		pwdView.setError(null);
		
		String id = idView.getText().toString();
		String pwd = pwdView.getText().toString();
		
		boolean cancel = false;
		View focusView = null;
		
		
		if (TextUtils.isEmpty(id)) {
			idView.setError(getString(R.string.error_id_required));
			focusView = idView;
			cancel = true;
		} else if (!isIdValid(id)) {
			idView.setError(getString(R.string.error_invalid_id));
			focusView = idView;
			cancel = true;
		} else if (TextUtils.isEmpty(pwd)) {
			pwdView.setError(getString(R.string.error_pwd_required));
			focusView = pwdView;
			cancel = true;
		} else if (!isPwdValid(pwd)) {
			pwdView.setError(getString(R.string.error_invalid_pwd));
			focusView = pwdView;
			cancel = true;
		}
		
		if (cancel) {
			focusView.requestFocus();
		} else {
			postExecute(startAuth(id, pwd));
		}
		
	}
	
	public boolean startAuth(String id, String pwd) {
		SharedPreferences sharedPref = this.getSharedPreferences(Properties.SHAREDPREFERENCES_NAME, MODE_APPEND);
		Editor editor = sharedPref.edit();
		editor.putString("id", id);
		editor.putString("pwd", pwd);
		editor.commit();
		return true;
	}
	
	public void postExecute(Boolean result) {

		if (result) {
			if (Properties.LOCK_ACTION.equalsIgnoreCase(action)) {
				Intent intent = new Intent().putExtra("req", 100);
				setResult(Activity.RESULT_OK, intent);
				finish();
			} else {
				Toast.makeText(LockActivity.this, getString(R.string.prompt_lock_suc), Toast.LENGTH_SHORT).show(); 
				Intent intent = new Intent(LockActivity.this, GalleryMainActivity.class);
				startActivity(intent);
			}
		} 
	}
	
	public void showProgress(final boolean show) {
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
			lockFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			lockFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter(){
				@Override
				public void onAnimationEnd(Animator animation) {
					lockFormView.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});
			
			progressView.setVisibility(show ? View.VISIBLE : View.GONE);
			progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					progressView.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});
		} else {
			progressView.setVisibility(show ? View.VISIBLE : View.GONE);
			lockFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	private boolean isIdValid(String id) {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean isPwdValid(String pwd) {
		return pwd.length() >= MIN_PWD_LENGTH;
	}

}
