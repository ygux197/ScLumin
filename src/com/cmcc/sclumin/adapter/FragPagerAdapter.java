package com.cmcc.sclumin.adapter;

import java.util.ArrayList;

import com.cmcc.sclumin.util.FragmentList;
import com.cmcc.sclumin.util.ViewList;

import android.support.v4.app.Fragment;  
import android.support.v4.app.FragmentManager;  
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragPagerAdapter extends FragmentPagerAdapter {
	
	FragmentList flist;
	int[] picIds;
	
	public FragPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		return flist.getSize();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		Fragment frag = (Fragment) flist.getFragment(position);
//		container.removeView(view);
//		view.setImageBitmap(null);
	}

//	@Override
	public Object instantiateItem(ViewGroup container, int position) {
//		if (position == 0) {
//			flist.setImageResource(position, picIds[picIds.length - 1]);
//		} else if (position == flist.getSize() - 1) {
//			flist.setImageResource(position, picIds[0]);
//		} else {
//			flist.setImageResource(position, picIds[position - 1]);
//		}
//		container.addView(flist.getView(position));
		return flist.getFragment(position);
	}

	@Override
	public Fragment getItem(int pos) {
		return flist.getFragment(pos);
	}

}
