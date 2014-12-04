package com.hbcmcc.sclumin;

import java.util.ArrayList;
import java.util.List;

import com.example.sclumin_test2.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.WindowManager;

public class AdsActivity extends FragmentActivity {
	
	class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;
		  
		public MyPageAdapter(FragmentManager fm) {
		       super(fm);
		       this.fragments = new ArrayList<Fragment>();
		}
		  
		public void addItem(int pid) {
			Fragment myFragment = new FragImageView();
		    Bundle args = new Bundle();
		    args.putInt("pid", pid);
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
	
	private MyPageAdapter pageAdapter = null;
	private AutoLoopViewPager pager = null;
	
	private static final int DEFAULT_AUTOSCROLL_INTERVAL = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		pageAdapter = new MyPageAdapter(getSupportFragmentManager());
		
		int[] picIds = new int[] {
			R.drawable.pic1,
			R.drawable.pic2,
			R.drawable.pic3,
			R.drawable.pic4
		};
		
		pageAdapter.addItem(picIds[picIds.length - 1]);
		for (int i = 1; i < picIds.length + 1; ++i) {
			pageAdapter.addItem(picIds[i - 1]);
		}
		pageAdapter.addItem(picIds[0]);
		
		pager = (AutoLoopViewPager)findViewById(R.id.pager);

		pager.setOffscreenPageLimit(pageAdapter.getCount() - 1);
		pager.setAdapter(pageAdapter);
		pager.setInterval(DEFAULT_AUTOSCROLL_INTERVAL);
		pager.startAutoScroll();
		pager.setOnPageChangeListener(picIds.length);
		pager.setPageTransformer(true, new DepthPageTransformer());
		pager.setCurrentItem(1);
		
	}
}
