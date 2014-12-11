package com.hbcmcc.sclumin.gallery;

import java.util.ArrayList;

import com.hbcmcc.sclumin.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {

//	private Context context;
	private LayoutInflater inflater;
	private ArrayList<GalleryItem> data = new ArrayList<GalleryItem>();
	ImageLoader imageLoader;
	private int num = 0;
	
	private boolean isCheckboxShow;
	
	public GalleryAdapter(Context c, ImageLoader imageLoader) {
		inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		context = c;
		this.imageLoader = imageLoader;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public GalleryItem getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	public void selectAll(boolean selection) {
		for (int i = 0; i < data.size(); i++) {
			data.get(i).isSeleted = selection;

		}
		notifyDataSetChanged();
	}
	
	public boolean isAllSelected() {
		boolean isAllSelected = true;

		for (int i = 0; i < data.size(); i++) {
			if (!data.get(i).isSeleted) {
				isAllSelected = false;
				break;
			}
		}

		return isAllSelected;
	}
	
	public boolean isAnySelected() {
		boolean isAnySelected = false;

		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).isSeleted) {
				isAnySelected = true;
				break;
			}
		}

		return isAnySelected;
	}

	public ArrayList<GalleryItem> getSelected() {
		ArrayList<GalleryItem> dataT = new ArrayList<GalleryItem>();

		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).isSeleted) {
				dataT.add(data.get(i));
			}
		}

		return dataT;
	}
	
	public void addAll(ArrayList<GalleryItem> files) {

		try {
			this.data.clear();
			this.data.addAll(files);

		} catch (Exception e) {
			e.printStackTrace();
		}

		notifyDataSetChanged();
	}
	
	public void removeItem(int pos) {
		if (null != data.get(pos)) {
			data.remove(pos);
		}
		notifyDataSetChanged();
	}
	
	public void setCheckboxShow(boolean isCheckboxShow) {
		this.isCheckboxShow = isCheckboxShow;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.gallery_item_view, null);
			holder = new ViewHolder();
			holder.imgQueue = (ImageView) convertView.findViewById(R.id.imgQueue);

			holder.imgQueueMultiSelected = (ImageView) convertView.findViewById(R.id.imgQueueMultiSelected);
			if (isCheckboxShow) {
				holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
			} else {
				holder.imgQueueMultiSelected.setVisibility(View.GONE);
			}
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imgQueue.setTag(position);

		try {

			imageLoader.displayImage("file://" + data.get(position).sdcardPath,
					holder.imgQueue, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.imgQueue
									.setImageResource(R.drawable.imgbg);
							super.onLoadingStarted(imageUri, view);
						}
					});

			if (isCheckboxShow)
				holder.imgQueueMultiSelected.setSelected(data.get(position).isSeleted);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;
	}

	public int changeSelection(View v, int position) {

		if (data.get(position).isSeleted) {
			data.get(position).isSeleted = false;
			num--;
		} else {
			data.get(position).isSeleted = true;
			num++;
		}

		((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(data.get(position).isSeleted);
		return num;
		
	}
	
	public class ViewHolder {
		ImageView imgQueue;
		ImageView imgQueueMultiSelected;
	}
	
	public void clearCache() {
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();
	}

	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}
	
}
