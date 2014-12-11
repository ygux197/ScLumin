package com.hbcmcc.sclumin.gallery;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import com.hbcmcc.sclumin.R;
import com.hbcmcc.sclumin.util.ExitApplication;
import com.hbcmcc.sclumin.util.Properties;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

public class GalleryActivity extends Activity {
	
	GridView gridGallery;
	Handler handler;
	GalleryAdapter adapter;

	Button send_btn;

	String action;
	ActionBar actionBar;
	private ImageLoader imageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = getActionBar();
		actionBar.show();
		actionBar.setTitle(R.string.local_img);
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_gallery_choose);
		ExitApplication.getInstance().addActivity(this);

		action = getIntent().getAction();
		if (action == null) {
			finish();
		}
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
		final String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.sclumin_tmp"; 
		new File(CACHE_DIR).mkdirs();
		File cacheFile = StorageUtils.getOwnCacheDirectory(getBaseContext(), CACHE_DIR);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
		
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(getBaseContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCache(new UnlimitedDiscCache(cacheFile))
				.memoryCache(new WeakMemoryCache());
		
		ImageLoaderConfiguration config = builder.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}
	
	private void init() {
		handler = new Handler();
		gridGallery = (GridView) findViewById(R.id.gallery_gv);
		gridGallery.setFastScrollEnabled(true);
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		
		PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, true, true);
		gridGallery.setOnScrollListener(listener);
		if (action.equalsIgnoreCase(Properties.PICK_ACTION)) {
			findViewById(R.id.llBottomContainer).setVisibility(View.VISIBLE);
			gridGallery.setOnItemClickListener(new AdapterView.OnItemClickListener(){
	
				@Override
				public void onItemClick(AdapterView<?> l, View v, int position, long id) {
					int num = adapter.changeSelection(v, position);
					if (num != 0) {
						send_btn.setText("—°‘Ò£®" + num + "£©’≈");
					} else {
						send_btn.setText("—°‘Ò");
					}
				}
				
			});
			adapter.setCheckboxShow(true);
		}
		gridGallery.setAdapter(adapter);
		send_btn = (Button) findViewById(R.id.send_btn);
		send_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ArrayList<GalleryItem> selected = adapter.getSelected();
				String[] allPath = new String[selected.size()];
				for (int i = 0; i < allPath.length; i++) {
					allPath[i] = selected.get(i).sdcardPath;
				}
				Intent data = new Intent().putExtra("all_path", allPath);
				setResult(RESULT_OK, data);
				finish();
			}
		});
		
		new Thread() {

			@Override
			public void run() {
				Looper.prepare();
				handler.post(new Runnable() {
					@Override
					public void run() {
						adapter.addAll(getGalleryPhotos());
					}
				});
				Looper.loop();
			};

		}.start();
	}
	
	private ArrayList<GalleryItem> getGalleryPhotos() {
		
		ArrayList<GalleryItem> galleryList = new ArrayList<GalleryItem>();
		
		try {
			final String[] columns = {MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID};
			final String orderBy = MediaStore.Images.Media._ID;

			Cursor imagecursor;
//			if (android.os.Build.VERSION.SDK_INT <11) {
//				imagecursor= managedQuery(
//								MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
//								null, null, orderBy);
//			} else {
				CursorLoader cursorLoader = new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								columns, null, null, orderBy);
				imagecursor = cursorLoader.loadInBackground();
//			}

			if (imagecursor != null && imagecursor.getCount() > 0) {

				while (imagecursor.moveToNext()) {
					GalleryItem item = new GalleryItem();

					int dataColumnIndex = imagecursor
							.getColumnIndex(MediaStore.Images.Media.DATA);

					item.sdcardPath = imagecursor.getString(dataColumnIndex);

					galleryList.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Collections.reverse(galleryList);
		return galleryList;
	}
	
}
