package com.hbcmcc.sclumin.loopview;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ViewScroller extends Scroller {
	
	 private double scrollFactor = 1;

	 private static final int ANIM_DURATION = 100;  
	
	 public ViewScroller(Context context) {  
		 super(context);  
	 }  
	  
	 public ViewScroller(Context context, Interpolator interpolator) {  
		 super(context, interpolator);  
	 }  
	  
	 public ViewScroller(Context context, Interpolator interpolator, boolean flywheel) {  
		 super(context, interpolator, flywheel);  
	 }  
	 
	 public void setScrollFactor(double scrollFactor) {
		 this.scrollFactor = scrollFactor;
	 }
	  
	 @Override  
	 public void startScroll(int startX, int startY, int dx, int dy, int duration) {  
		 super.startScroll(startX, startY, dx, dy, (int)(ANIM_DURATION * scrollFactor));  
	 }  
	 
	 @Override  
	 public void startScroll(int startX, int startY, int dx, int dy) {  
		 super.startScroll(startX, startY, dx, dy, (int)(ANIM_DURATION * scrollFactor));  
	 }  

}
