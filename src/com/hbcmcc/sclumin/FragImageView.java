package com.hbcmcc.sclumin;

import com.example.sclumin_test2.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragImageView extends Fragment {
	
	private ImageView imgView;
	 
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle args = getArguments();
		int pid = args.getInt("pid");
		imgView = (ImageView) getView().findViewById(R.id.imageView);
		imgView.setImageResource(pid);
 	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		
	    View view = inflater.inflate(R.layout.img_view, container, false);
	    return view;		
	}	


}
