package com.cmcc.sclumin.util;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ImageView;

public class ViewList {
	
	List<View> vlist;
	
	public ViewList() {
		vlist = new ArrayList<View>();
	}

	public void addView(View view) {
		vlist.add(view);
	}
	
	public View getView(int pos) {
		return vlist.get(pos);
	}
	
	public void setImageResource(int pos, int res) {
		((ImageView)vlist.get(pos)).setImageResource(res);
	}
	
	public int getSize() {
		return vlist.size();
	}
	
	public List<View> getVlist() {
		return vlist;
	}

	public void setVlist(List<View> vlist) {
		this.vlist = vlist;
	}

	
}
