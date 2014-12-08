package com.hbcmcc.sclumin.lock;

import com.hbcmcc.sclumin.R;
import com.hbcmcc.sclumin.service.ScLuminService;

import android.os.Bundle;
import android.widget.Toast;

public class UnlockActivity extends LockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lockButton.setText(getString(R.string.unlock));
	}
	
	
	
	@Override
	public void startAuth(String id, String pwd) {
		showProgress(true);
		mAuthTask = new UserUnlockTask(id, pwd);
		mAuthTask.execute((Void) null);
	}



	public class UserUnlockTask extends UserLockTask {
		
		

		public UserUnlockTask(String id, String pwd) {
			super(id, pwd);
		}
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			
			if (id.equals(ScLuminService.id) && pwd.equals(ScLuminService.pwd)) {
				return true;
			} else {
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {

			mAuthTask = null;
			showProgress(false);
			
			if (result) {
				Toast.makeText(UnlockActivity.this, getString(R.string.prompt_unlock_suc), Toast.LENGTH_SHORT).show(); 
			} else {
				Toast.makeText(UnlockActivity.this, getString(R.string.error_incorrect), Toast.LENGTH_LONG).show();  
				pwdView.requestFocus();
			}
		
		}
		
	}
	
	
	
}
