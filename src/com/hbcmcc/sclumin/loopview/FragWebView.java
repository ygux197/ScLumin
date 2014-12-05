package com.hbcmcc.sclumin.loopview;

import com.hbcmcc.sclumin.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FragWebView extends Fragment {
	
	private WebView webView;
	 
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		webView = (WebView) getView().findViewById(R.id.webView);
		Bundle args = getArguments();
		String url = args.getString("url");
		webView.setWebViewClient(new WebBrowser());
		WebSettings settings = webView.getSettings();
		settings.setLoadsImagesAutomatically(true);
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.loadUrl(url);
 	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.web_view, container, false);
	    return view;		
	}	

	private class WebBrowser extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		
	}
	
}
