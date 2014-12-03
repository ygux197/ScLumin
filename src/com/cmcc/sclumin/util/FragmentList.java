package com.cmcc.sclumin.util;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;

public class FragmentList {

	List<Fragment> flist;
	
	public FragmentList() {
		flist = new ArrayList<Fragment>();
	}
	
	public void addFragment(Fragment fragment) {
		flist.add(fragment);
	}
	
	public Fragment getFragment(int pos) {
		return flist.get(pos);
	}
	
	public int getSize() {
		return flist.size();
	}
	
}
