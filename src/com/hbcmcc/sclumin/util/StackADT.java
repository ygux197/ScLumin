package com.hbcmcc.sclumin.util;

import android.app.Activity;

public interface StackADT {
	
	public void push(Activity obj);
	public Activity pop();
	public boolean isEmpty();
	public int size();
	public Activity peek();
}
