package com.hbcmcc.sclumin.lock;

import com.hbcmcc.sclumin.R;
import com.hbcmcc.sclumin.loopview.AdsActivity;
import com.hbcmcc.sclumin.service.ScLuminService;
import com.hbcmcc.sclumin.util.Config;
import com.hbcmcc.sclumin.util.ExitApplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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
	public UserLockTask mAuthTask = null;
	private long exitTime = 0;

	public static final int MIN_PWD_LENGTH = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (Config.DEBUG) {
			Log.e(LockActivity.class.toString(), "LockActivity onCreate() taskid:" + this.getTaskId());
		}
		super.onCreate(savedInstanceState);
		
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
	}
	
	@Override
	protected void onResume() {
		if (Config.DEBUG) {
			Log.e(LockActivity.class.toString(), "LockActivity onResume() taskid:" + this.getTaskId());
		}
		Window window = this.getWindow();
		window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (Config.DEBUG) {
			Log.e(LockActivity.class.toString(), "LockActivity onDestroy()!");
		}
		super.onDestroy();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {  
				this.doubleExit();
			}
			return true;
		}
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
			startAuth(id, pwd);
		}
		
	}
	
	public void startAuth(String id, String pwd) {
		showProgress(true);
		mAuthTask = new UserLockTask(id, pwd);
		mAuthTask.execute((Void) null);
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

	public class UserLockTask extends AsyncTask<Void, Void, Boolean> {
		
		public final String id;
		public final String pwd;
		
		public UserLockTask(String id, String pwd) {
			this.id = id;
			this.pwd = pwd;
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			ScLuminService.id = this.id;
			ScLuminService.pwd = this.pwd;
			return true;
		}
		
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mAuthTask = null;
			showProgress(false);

			if (result) {
				Toast.makeText(LockActivity.this, getString(R.string.prompt_lock_suc), Toast.LENGTH_SHORT).show(); 
				Intent intent = new Intent(LockActivity.this, AdsActivity.class);
				startActivity(intent);
			} 
		}
	}

}
