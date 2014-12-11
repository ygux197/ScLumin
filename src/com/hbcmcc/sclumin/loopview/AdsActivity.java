package com.hbcmcc.sclumin.loopview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hbcmcc.sclumin.R;
import com.hbcmcc.sclumin.preference.PreferenceActivity;
import com.hbcmcc.sclumin.util.Properties;
import com.hbcmcc.sclumin.util.ExitApplication;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class AdsActivity extends FragmentActivity  {

	private AdsPagerAdapter adsAdapter = null;
	private AutoLoopViewPager pager = null;
	private long exitTime = 0;
	private SharedPreferences sharedPref;
	
	private static final int DEFAULT_AUTOSCROLL_INTERVAL = 2000;
	private static final int TYPE_RES = 0;
	private static final int TYPE_URL = 1;
	private static final int TYPE_URI = 2;
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_ads);
		
		pager = (AutoLoopViewPager)findViewById(R.id.pager);
		adsAdapter = new AdsPagerAdapter(getSupportFragmentManager());
		sharedPref = getSharedPreferences(Properties.SHAREDPREFERENCES_NAME, MODE_APPEND);
		
		Set<String> all_path = sharedPref.getStringSet("uri", new HashSet<String>());
		
		adsAdapter.addItem(Properties.urls[Properties.urls.length - 1], TYPE_URL);
		
		for (int i = 0; i < Properties.pics.length; ++i) {
			adsAdapter.addItem(Properties.pics[i], TYPE_RES);
		}
		
		if (all_path.size() > 0) {
			for (String uri : all_path) {
				adsAdapter.addItem(uri, TYPE_URI);
			}	
		}
		
		for (int i = 0; i < Properties.urls.length; ++i) {
			adsAdapter.addItem(Properties.urls[i], TYPE_URL);
		}
		adsAdapter.addItem(Properties.pics[0], TYPE_RES);
		
		
		pager.setOffscreenPageLimit(adsAdapter.getCount() - 1);
		pager.setAdapter(adsAdapter);
		pager.setInterval(DEFAULT_AUTOSCROLL_INTERVAL);
		pager.startAutoScroll();
		pager.setOnPageChangeListener(adsAdapter.getCount() - 2);
		pager.setPageTransformer(true, new DepthPageTransformer());
		pager.setCurrentItem(1);
		
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
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ads, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
		case R.id.exit_menu:
			new AlertDialog.Builder(this).setTitle(R.string.hint).setMessage(R.string.exit_msg)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						ExitApplication.getInstance().exit();
					}
				}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				}).create().show();
			
			break;
		case R.id.config_menu:
			Intent intent = new Intent(this, PreferenceActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}
	
	@Override
	protected void onResume() {
		if (Properties.DEBUG) {
			Log.e(AdsActivity.class.toString(), "AdsActivity onResume() taskid:" + this.getTaskId());
		}
		Window window = this.getWindow();
		window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		if (Properties.DEBUG) {
			Log.e(AdsActivity.class.toString(), "AdsActivity onDestroy() taskid:" + this.getTaskId());
		}
		super.onDestroy();
	}


	/*
	 * 两次返回退出程序
	 */
	public void doubleExit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {  
		    Toast.makeText(AdsActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
		    exitTime = System.currentTimeMillis();  
		  } else {  
		    ExitApplication.getInstance().exit();
		  }  
	}
	
	class AdsPagerAdapter extends FragmentStatePagerAdapter {

		private List<Fragment> fragments;
		  
		public AdsPagerAdapter(FragmentManager fm) {
		       super(fm);
		       this.fragments = new ArrayList<Fragment>();
		}
		  
		public void addItem(Object params, int type) {
			Fragment myFragment;
		    Bundle args = new Bundle();
		    if (type == TYPE_RES) {
		    	myFragment = new FragImageView();
		    	args.putInt("pid", (int) params);
		    } else if (type == TYPE_URL) {
		    	myFragment = new FragWebView();
		    	args.putString("url", (String) params);
		    } else if (type == TYPE_URI) {
		    	myFragment = new FragImageView();
		    	args.putString("uri", (String)params);
		    } else {
		    	return;
		    }
		    myFragment.setArguments(args);
		    this.fragments.add(myFragment);
		}
		  
		@Override 
		public Fragment getItem(int position) {
		       return this.fragments.get(position);
		}
		  
		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

}
