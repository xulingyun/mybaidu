package com.xulingyun.baiduimagesbrowse.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xulingyun.baiduimagesbrowse.R;
import com.xulingyun.baiduimagesbrowse.dao.Photo;

public class MyListAdapter extends BaseAdapter {

	List<Photo> imageInfos;
	Context context;
	LayoutInflater inflater;
	RequestQueue mQueue;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	public MyListAdapter(List<Photo> imageInfos, Context context,
			RequestQueue mQueue) {
		this.imageInfos = imageInfos;
		this.context = context;
		this.mQueue = mQueue;
		inflater = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.loadfail)
				// .bitmapConfig()
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.cacheInMemory(true).cacheOnDisk(true)
				.displayer(new FadeInBitmapDisplayer(1000)).build();
	}

	public int getCount() {
		return imageInfos.size();
	}

	public Object getItem(int arg0) {
		return imageInfos.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.mylist, null);
			holder.bitmap = (ImageView) convertView
					.findViewById(R.id.show_image);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.like = (ImageButton) convertView.findViewById(R.id.like);
			holder.share = (ImageButton) convertView.findViewById(R.id.share);
			holder.comment = (ImageButton) convertView
					.findViewById(R.id.comment);
			holder.bitmap.setTag(imageInfos.get(position).getKind() + "_"
					+ imageInfos.get(position).getName());
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final String imgUrl = "http://10.100.110.62:8080/test/user_images/"
				+ imageInfos.get(position).getKind() + "/"
				+ imageInfos.get(position).getSrc();
		if (imgUrl != null && !imgUrl.equals("")) {
			ImageLoader.getInstance().displayImage(imgUrl, holder.bitmap,options);
		}
		holder.name.setText(imageInfos.get(position).getName());
		return convertView;
	}

	public final class ViewHolder {
		TextView name;
		// NetworkImageView bitmap;
		ImageView bitmap;
		ImageButton like;
		ImageButton share;
		ImageButton comment;
	}

}
