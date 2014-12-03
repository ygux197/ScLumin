package com.cmcc.sclumin.activity;


import com.cmcc.sclumin.R;
import com.cmcc.sclumin.adapter.ViewPagerAdapter;
import com.cmcc.sclumin.animation.DepthPageTransformer;
import com.cmcc.sclumin.util.AutoLoopViewPager;
//import com.cmcc.sclumin.animation.ZoomOutPageTransformer;
import com.cmcc.sclumin.util.ViewList;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

public class Ads2Activity extends Activity {

	private AutoLoopViewPager vp;
	private ViewList vlist;
	private ViewPagerAdapter mAdapter;
	private int[] picIds;
	
	private static final long DEFAULT_AUTOSCROLL_INTERVAL = 5000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_ads2);
		
		vp = (AutoLoopViewPager) findViewById(R.id.viewPager);
		picIds = new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4};
		vlist = new ViewList();
		for (int i = 0; i < picIds.length + 2; ++i) {
			ImageView iv = new ImageView(this);
			vlist.addView(iv);
		}
		
		mAdapter = new ViewPagerAdapter(vlist, picIds);
		vp.setAdapter(mAdapter);
		vp.setInterval(DEFAULT_AUTOSCROLL_INTERVAL);
		vp.startAutoScroll();
		vp.setOnPageChangeListener(picIds.length);
		vp.setPageTransformer(true, new DepthPageTransformer());
		vp.setCurrentItem(1);
	}
	
}
