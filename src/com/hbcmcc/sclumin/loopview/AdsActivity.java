package com.hbcmcc.sclumin.loopview;

import java.util.ArrayList;
import java.util.List;

import com.hbcmcc.sclumin.R;
import com.hbcmcc.sclumin.lock.UnlockActivity;
import com.hbcmcc.sclumin.util.Config;
import com.hbcmcc.sclumin.util.ExitApplication;

import android.annotation.TargetApi;
import android.content.Intent;
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

	class AdsPagerAdapter extends FragmentStatePagerAdapter {

		private List<Fragment> fragments;
		  
		public AdsPagerAdapter(FragmentManager fm) {
		       super(fm);
		       this.fragments = new ArrayList<Fragment>();
		}
		  
		public void addItem(Object params) {
			Fragment myFragment;
		    Bundle args = new Bundle();
		    String type = params.getClass().getName(); 
		    if (type.equals("java.lang.Integer")) {
		    	myFragment = new FragImageView();
		    	args.putInt("pid", (int) params);
		    } else if (type.equals("java.lang.String")) {
		    	myFragment = new FragWebView();
		    	args.putString("url", (String) params);
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
	
	private AdsPagerAdapter adsAdapter = null;
	private AutoLoopViewPager pager = null;
	private long exitTime = 0;
	
	private static final int DEFAULT_AUTOSCROLL_INTERVAL = 500;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (Config.DEBUG) {
			Log.e(AdsActivity.class.toString(), "AdsActivity onCreate() taskid:" + this.getTaskId());
		}
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.activity_main);
		
		pager = (AutoLoopViewPager)findViewById(R.id.pager);
		adsAdapter = new AdsPagerAdapter(getSupportFragmentManager());
		
		int[] pics = new int[] {
			R.drawable.pic1,
			R.drawable.pic2,
			R.drawable.pic3,
			R.drawable.pic4
		};
		
		String[] urls = new String[] {
			"http://www.hb.10086.cn/pages/wechat/",
			"http://www.hb.10086.cn/pages/kkyl/",
			"http://www.hb.10086.cn/servicenew/wuhan188/showNums.action",
			"http://www.hb.10086.cn/pages/seasonalflowcap/index.html"
		};
		
		if (urls.length == 0) {
			adsAdapter.addItem(pics[pics.length - 1]);
		} else {
			adsAdapter.addItem(urls[urls.length - 1]);
		}
		
		for (int i = 0; i < pics.length; ++i) {
			adsAdapter.addItem(pics[i]);
		}
		for (int i = 0; i < urls.length; ++i) {
			adsAdapter.addItem(urls[i]);
		}
		adsAdapter.addItem(pics[0]);
		
		pager.setOffscreenPageLimit(adsAdapter.getCount() - 1);
		pager.setAdapter(adsAdapter);
		pager.setInterval(DEFAULT_AUTOSCROLL_INTERVAL);
		pager.startAutoScroll();
		pager.setOnPageChangeListener(pics.length + urls.length);
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
			ExitApplication.getInstance().exit();
			break;
		case R.id.unlock_menu:
			Intent intent = new Intent(this, UnlockActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}
	
	@Override
	protected void onResume() {
		if (Config.DEBUG) {
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
		if (Config.DEBUG) {
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

}
