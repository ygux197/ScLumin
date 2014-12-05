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
	 * ����ViewPager��Ĭ��scrollerΪViewScroller
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
	 * ��ʼ�Զ�����
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	public void startAutoScroll() {
		isAutoScroll = true;
		handler.removeMessages(SCROLL_WHAT);
		handler.sendEmptyMessageDelayed(SCROLL_WHAT, interval);
	}
	
	/*
	 * ֹͣ�Զ�����
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	public void stopAutoScroll() {
		isAutoScroll = false;
		handler.removeMessages(SCROLL_WHAT);
	}
	
	/*
	 * �����ֶ��л�ͼƬʱ�Ķ����ٶ�
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	public void setSwipeDurationFactor(double swipeDurationFactor) {
		this.swipeDurationFactor = swipeDurationFactor;
	}
	
	/*
	 * �����Զ��л�ͼƬʱ�Ķ����ٶ�
	 * @author guyexuan
	 * @date 2014/12/03
	 */
	public void setAutoScrollFactor(double autoFactor) {
		this.autoScrollFactor = autoFactor;
	}
	
	/*
	 * �Զ�����ʱ���л�һ��
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
		// �������������ڶ���ͼ��ʱ���л����ڶ���ͼ
		if (nextItem == total - 1) {
			setCurrentItem(1, false);
		} else {
			setCurrentItem(nextItem, true);
		}
	}
	
	/*
	 * ǿ��ת��adapter������
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
	 * ����ҳ���л�ʱ��listener
	 * @params viewNum ��ͼƬ��
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
			 * ��һҳΪ�����ڶ�ҳ�ľ��񣬵��л�����һҳʱ�Զ��л��������ڶ�ҳ
			 * ���һҳΪ�ڶ�ҳ�ľ��񣬵��л������һҳʱ�Զ��л����ڶ�ҳ
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
	 * ���������¼���������ʱֹͣ�Զ����ţ�̧���ʼ�Զ�����
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
	 * ��дHandler��
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
