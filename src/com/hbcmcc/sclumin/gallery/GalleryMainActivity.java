package com.hbcmcc.sclumin.gallery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbcmcc.sclumin.R;
import com.hbcmcc.sclumin.loopview.AdsActivity;
import com.hbcmcc.sclumin.util.ExitApplication;
import com.hbcmcc.sclumin.util.Properties;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class GalleryMainActivity extends Activity {

	GridView gridGallery;
	Handler handler;
	GalleryAdapter adapter;
	LayoutInflater layoutInflator;
	Button imgpick_btn;
	Button skip_btn;
	ImageLoader imageLoader;
	ImageView preview_view;
	Context context;
	Button repick_btn;
	Button pickok_btn;
	TextView sub_title;
	String[] all_path;
	String action;
	SharedPreferences sharedPrefs;
	Editor editor;
	ActionBar actionBar;

	private static final int REQUEST_CODE = 100;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getActionBar();
		actionBar.show();
		actionBar.setTitle(R.string.local_img);
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_gallery);
		ExitApplication.getInstance().addActivity(this);

		initImageLoader();
		init();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(Activity.RESULT_CANCELED);
			this.finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				this).defaultDisplayImageOptions(defaultOptions).memoryCache(
				new WeakMemoryCache());

		ImageLoaderConfiguration config = builder.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}

	private void init() {

		handler = new Handler();
		layoutInflator = LayoutInflater.from(this);
		gridGallery = (GridView) findViewById(R.id.img_gv);
		gridGallery.setFastScrollEnabled(true);
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		adapter.setCheckboxShow(false);
		gridGallery.setAdapter(adapter);
		action = this.getIntent().getAction();
		sharedPrefs = getSharedPreferences(Properties.SHAREDPREFERENCES_NAME, Context.MODE_APPEND);
		editor = sharedPrefs.edit();
		imgpick_btn = (Button) findViewById(R.id.imgpick_btn);
		imgpick_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Properties.PICK_ACTION);
				startActivityForResult(i, REQUEST_CODE);
			}
		});

		skip_btn = (Button) findViewById(R.id.skip_btn);
		skip_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				editor.remove("uri");
				editor.commit();
				if (Properties.CHANGE_LOCAL_IMG_ACTION.equalsIgnoreCase(action)) {
					Intent i = new Intent();
					setResult(RESULT_OK, i);
					finish();
				} else {
					Intent i = new Intent(GalleryMainActivity.this, AdsActivity.class);
					startActivity(i);
					finish();
				}
			}
		});

		repick_btn = (Button) findViewById(R.id.repick_btn);
		repick_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Properties.PICK_ACTION);
				startActivityForResult(i, REQUEST_CODE);
			}
		});

		pickok_btn = (Button) findViewById(R.id.pickok_btn);
		pickok_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Set<String> uris = new HashSet<String>();
				for (int i = 0; i < all_path.length; ++i) {
					uris.add(all_path[i]);
				}
				editor.putStringSet("uri", uris);
				editor.commit();
				if (Properties.CHANGE_LOCAL_IMG_ACTION.equalsIgnoreCase(action)) {
					Intent i = new Intent();
					setResult(RESULT_OK, i);
					finish();
				} else {
					Intent intent = new Intent(GalleryMainActivity.this,
							AdsActivity.class);
					startActivity(intent);
				}
			}
		});
		
		sub_title = (TextView) findViewById(R.id.sub_title);
		sub_title.setVisibility(View.GONE);

		context = this;
		gridGallery.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("InflateParams")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int pos, long id) {
				preview_view = (ImageView) layoutInflator.inflate(
						R.layout.img_view, null);
				imageLoader.displayImage("file://"
						+ adapter.getItem(pos).sdcardPath, preview_view,
						new SimpleImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								preview_view.setImageResource(R.drawable.imgbg);
								super.onLoadingStarted(imageUri, view);
							}
						});

				new AlertDialog.Builder(context)
						.setTitle("‘§¿¿")
						.setView(preview_view)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {

									}
								})
						.setNegativeButton(R.string.cancel_pick,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										adapter.removeItem(pos);
									}
								}).show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			all_path = data.getStringArrayExtra("all_path");

			ArrayList<GalleryItem> dataT = new ArrayList<GalleryItem>();

			for (String string : all_path) {
				GalleryItem item = new GalleryItem();
				item.sdcardPath = string;

				dataT.add(item);
			}
			if (dataT.size() > 0) {
				repick_btn.setVisibility(View.VISIBLE);
				imgpick_btn.setVisibility(View.GONE);
				pickok_btn.setVisibility(View.VISIBLE);
				sub_title.setVisibility(View.VISIBLE);
			} else {
				repick_btn.setVisibility(View.GONE);
				imgpick_btn.setVisibility(View.VISIBLE);
				pickok_btn.setVisibility(View.GONE);
				sub_title.setVisibility(View.GONE);
			}
			adapter.clear();
			adapter.addAll(dataT);
		} else if (resultCode == Activity.RESULT_CANCELED) {
			if (adapter.getCount() > 0) {
				repick_btn.setVisibility(View.VISIBLE);
				imgpick_btn.setVisibility(View.GONE);
				pickok_btn.setVisibility(View.VISIBLE);
				sub_title.setVisibility(View.VISIBLE);
			} else {
				repick_btn.setVisibility(View.GONE);
				imgpick_btn.setVisibility(View.VISIBLE);
				pickok_btn.setVisibility(View.GONE);
				sub_title.setVisibility(View.GONE);
			}
		}
	}
}
