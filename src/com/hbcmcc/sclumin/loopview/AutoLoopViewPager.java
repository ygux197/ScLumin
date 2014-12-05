package com.hbcmcc.sclumin.loopview;

import java.lang.reflect.Field;

import com.hbcmcc.sclumin.loopview.AdsActivity.AdsPagerAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

public class AutoLoopViewPager extends ViewPager {
	
	
	private Handler handler = null;
	private ViewScroller scroller = null;
	private boolean isAutoScroll = false;
	private boolean isStopByTouch = false;
	
	private double swipeDurationFactor = 1.0d;
	private double autoScrollFactor = 1.0d;
	private long interval = 5000;
	
	private static final int SCROLL_WHAT = 0;
	
	
	/*
	 * constructor
	 * @params Context context
	 */
	public AutoLoopViewPager(Context context) {
		super(context);
		init();
	}

	/*
	 * constructor
	 * @params Context context
	 * @params Attributes attrs
	 */
	public AutoLoopViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		handler = new mHandler();
		setViewPagerScroller();
	}
	
	/*
	 * 设置ViewPager的默认scroller为ViewScroller
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	private void setViewPagerScroller() {
		 try {
			 Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
			 scrollerField.setAccessible(true);
			 Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
			 interpolatorField.setAccessible(true);
			 scroller = new ViewScroller(getContext(), (Interpolator)interpolatorField.get(null));
			 scrollerField.set(this, scroller);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}
	
	/*
	 * 开始自动播放
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	public void startAutoScroll() {
		isAutoScroll = true;
		handler.removeMessages(SCROLL_WHAT);
		handler.sendEmptyMessageDelayed(SCROLL_WHAT, interval);
	}
	
	/*
	 * 停止自动播放
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	public void stopAutoScroll() {
		isAutoScroll = false;
		handler.removeMessages(SCROLL_WHAT);
	}
	
	/*
	 * 设置手动切换图片时的动画速度
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	public void setSwipeDurationFactor(double swipeDurationFactor) {
		this.swipeDurationFactor = swipeDurationFactor;
	}
	
	/*
	 * 设置自动切换图片时的动画速度
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	public void setAutoScrollFactor(double autoFactor) {
		this.autoScrollFactor = autoFactor;
	}
	
	/*
	 * 自动播放时，切换一次
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	public void scrollOnce() {
		AdsPagerAdapter adapter = getAdapter();
		int currentItem = getCurrentItem();
		int total;
		if (null == adapter || (total = adapter.getCount()) <= 1) {
			return;
		}
		
		int nextItem = currentItem + 1;
		// 当滚动到倒数第二幅图的时候，切换到第二幅图
		if (nextItem == total - 1) {
			setCurrentItem(1, false);
		} else {
			setCurrentItem(nextItem, true);
		}
	}
	
	/*
	 * 强制转换adapter的类型
	 * @see android.support.v4.view.ViewPager#getAdapter()
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	@Override
	public AdsPagerAdapter getAdapter() {
		return (AdsPagerAdapter) super.getAdapter();
	}
	
	public long getInterval() {
		return interval;
	}


	public void setInterval(long interval) {
		this.interval = interval;
	}
	
	/*
	 * 定义页面切换时的listener
	 * @params viewNum 总图片数
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	public void setOnPageChangeListener(final int viewNum) {
		super.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			/*
			 * 第一页为倒数第二页的镜像，当切换到第一页时自动切换到倒数第二页
			 * 最后一页为第二页的镜像，当切换到最后一页时自动切换到第二页
			 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
			 */
			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					setCurrentItem(viewNum, false);
				} else if (arg0 == viewNum + 1) {
					setCurrentItem(1, false);
				}
			}
		});
	}

	/*
	 * 处理触摸的事件，当按下时停止自动播放，抬起后开始自动播放
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		int action = MotionEventCompat.getActionMasked(ev);
		
		if (action == MotionEvent.ACTION_DOWN && isAutoScroll) {
			isStopByTouch = true;
			stopAutoScroll();
		} else if (action == MotionEvent.ACTION_UP && isStopByTouch) {
			startAutoScroll();
		}
		
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}
	
	/*
	 * 重写Handler类
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	private class mHandler extends Handler {

		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case SCROLL_WHAT:
				scroller.setScrollFactor(autoScrollFactor);
				scrollOnce();
				scroller.setScrollFactor(swipeDurationFactor);
				handler.removeMessages(SCROLL_WHAT);
		        handler.sendEmptyMessageDelayed(SCROLL_WHAT, interval);
			default:
				break;
			}
		}
		
	}
	


}
