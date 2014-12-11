package com.hbcmcc.sclumin.preference;

import com.hbcmcc.sclumin.R;
import com.hbcmcc.sclumin.loopview.AdsActivity;
import com.hbcmcc.sclumin.util.ExitApplication;
import com.hbcmcc.sclumin.util.Properties;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import android.widget.Toast;

public class PreferenceActivity extends Activity {
	
	private ActionBar actionBar;

	public static boolean hasChangedImg = false;
	public static boolean hasChangedUrl = false;

	@Override  
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		actionBar = getActionBar();
		actionBar.setTitle(R.string.config);
		actionBar.setDisplayHomeAsUpEnabled(true);
		ExitApplication.getInstance().addActivity(this);
		getFragmentManager().beginTransaction().add(android.R.id.content, new PrefsFragement()).commit();  
	}  


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (hasChangedImg || hasChangedUrl) {
				Intent i = new Intent(this, AdsActivity.class);
				startActivity(i);
			}
			this.finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	public static class PrefsFragement extends PreferenceFragment implements OnPreferenceClickListener {  

		Preference adsPref;
		Preference lockPref;
		static SharedPreferences sharedPref;
		static Preference pref;
		@Override  
		public void onCreate(Bundle savedInstanceState) {  
			// TODO Auto-generated method stub  
			super.onCreate(savedInstanceState);  
			addPreferencesFromResource(R.xml.preferences); 
			adsPref = this.findPreference("ads_pref");
			adsPref.setOnPreferenceClickListener(this);
			lockPref = this.findPreference("lock_pref");
			lockPref.setOnPreferenceClickListener(this);
			sharedPref = getActivity().getSharedPreferences(Properties.SHAREDPREFERENCES_NAME, MODE_APPEND);
			lockPref.setSummary("当前锁定工号：" + sharedPref.getString("id", new String()));
		}

		@Override
		public boolean onPreferenceClick(Preference pref) {
			PrefsFragement.pref = pref;
			String key = pref.getKey();
			if ("ads_pref".equals(key)) {
				Intent intent = new Intent(Properties.UNLOCK_ACTION);
				getActivity().startActivityForResult(intent, 100);
			} else if ("lock_pref".equals(key)) {
				Intent intent = new Intent(Properties.UNLOCK_ACTION);
				getActivity().startActivityForResult(intent, 101);
			}
			return false;
		}  
	}  

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			Intent intent = new Intent(Properties.CHANGE_LOCAL_IMG_ACTION);
			startActivityForResult(intent, 200);
		} else if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
			Intent intent = new Intent(Properties.LOCK_ACTION);
			startActivityForResult(intent, 201);
		} else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
			hasChangedImg = true;
			Toast.makeText(PreferenceActivity.this, "修改本地展示图片成功", Toast.LENGTH_SHORT).show();  
		} else if (requestCode == 201 && resultCode == Activity.RESULT_OK) {
			Preference pref = PrefsFragement.pref;
			pref.setSummary("当前锁定工号：" + PrefsFragement.sharedPref.getString("id", new String()));
			Toast.makeText(PreferenceActivity.this, "变更锁定工号成功", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onBackPressed() {
		if (hasChangedImg) {
			Intent i = new Intent(this, AdsActivity.class);
			startActivity(i);
			finish();
		} else {
			super.onBackPressed();
		}
	}
}  
