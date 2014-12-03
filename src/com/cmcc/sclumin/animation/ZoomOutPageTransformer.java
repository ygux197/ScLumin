package com.cmcc.sclumin.animation;

import android.support.v4.view.ViewPager;
import android.view.View;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
	
	private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;
    
	@Override
	public void transformPage(View view, float pos) {
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();
		
		if (pos < -1) {
			view.setAlpha(0);
		} else if (pos <= 1) {
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(pos));
			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			float horzMargin = pageWidth * (1 - scaleFactor) / 2;
			if (pos < 0) {
				view.setTranslationX(horzMargin - vertMargin / 2);
			} else {
				view.setTranslationX(-horzMargin + vertMargin / 2);
			}
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);
			view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
		} else {
			view.setAlpha(0);
		}
		 
	}
	
}
 