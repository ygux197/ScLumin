package com.cmcc.sclumin.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AdsFragment extends Fragment {
	
	int num;
	
	public static AdsFragment newInstance(int num) {
		AdsFragment adsFrag = new AdsFragment();
		Bundle args = new Bundle();
		args.putInt("num", num);
		adsFrag.setArguments(args);
		return adsFrag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	
}
