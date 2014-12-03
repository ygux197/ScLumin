package com.cmcc.sclumin;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;


public class AdsActivity extends Activity implements android.view.GestureDetector.OnGestureListener {
	
	private ViewFlipper ads;
	private GestureDetector gest;
	private int[] adsIds;
	private Activity mcontext;
	
	private static final int INTERVAL = 3000;
	private static final int MINSHIFT = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        mcontext = this;
        adsIds = new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4};
        ads = (ViewFlipper) findViewById(R.id.ads);
        gest = new GestureDetector(this, this);
        for (int i = 0; i < adsIds.length; ++i) {
        	ImageView iv = new ImageView(this);
        	iv.setImageResource(adsIds[i]);
        	iv.setScaleType(ImageView.ScaleType.FIT_XY);
        	ads.addView(iv);
        }
        
        startAutoFlipping();
    }

    private void startAutoFlipping() {
    	ads.setAutoStart(true);
        ads.setFlipInterval(INTERVAL);
        if (ads.isAutoStart() && !ads.isFlipping()) {
        	ads.startFlipping();
        }
    }
   
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		ads.stopFlipping();
		ads.setAutoStart(false);
		return gest.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float vx,
			float vy) {
		if (e2.getX() - e1.getX() > MINSHIFT) {
			ads.setInAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.left_in));
			ads.setOutAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.right_out));
			ads.showPrevious();
			startAutoFlipping();
			return true;
		} else if (e1.getX() - e2.getX() > MINSHIFT){
			ads.setInAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.right_in));
			ads.setOutAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.left_out));
			ads.showNext();
			startAutoFlipping();
			return true;
		}
		startAutoFlipping();
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}


}
