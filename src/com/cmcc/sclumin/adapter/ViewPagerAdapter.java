package com.cmcc.sclumin.adapter;

import com.cmcc.sclumin.util.ViewList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ViewPagerAdapter extends PagerAdapter {
	
	ViewList vlist;
	int[] picIds;
	
	public ViewPagerAdapter(ViewList vlist, int[] picIds) {
		this.vlist = vlist;
		this.picIds = picIds;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return vlist.getSize();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		ImageView view = (ImageView) vlist.getView(position);
		container.removeView(view);
		view.setImageBitmap(null);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (position == 0) {
			vlist.setImageResource(position, picIds[picIds.length - 1]);
		} else if (position == vlist.getSize() - 1) {
			vlist.setImageResource(position, picIds[0]);
		} else {
			vlist.setImageResource(position, picIds[position - 1]);
		}
		container.addView(vlist.getView(position));
		return vlist.getView(position);
	}

}
