package com.sudosaints.android.mojoscribe.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.model.IdValue;
import com.sudosaints.android.mojoscribe.util.Logger;

public class MultiImageGridAdapter extends BaseAdapter {
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private List<IdValue> images;
	private Context context;
	private Drawable defaultDrawable;
	private Logger logger;
	private DisplayImageOptions options;

	public MultiImageGridAdapter(Context context, List<IdValue> images) {
		this.context = context;
		this.images = images;
		logger = new Logger(context);
		defaultDrawable = this.context.getResources().getDrawable(R.drawable.background);
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.stub_image).showImageForEmptyUri(R.drawable.image_for_empty_url).cacheInMemory().cacheOnDisc().build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.size();
	}

	@Override
	public IdValue getItem(int arg0) {
		// TODO Auto-generated method stub
		return images.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final IdValue option = getItem(position);
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.row_multiphoto_grid_del_item, parent, false);
		}
		final ImageView imageView = (ImageView) convertView.findViewById(R.id.SelImageView);
		imageLoader.displayImage("file://" + option.getName(), imageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(Bitmap loadedImage) {
				Animation anim = AnimationUtils.loadAnimation(context, R.drawable.fade_in);
				imageView.setAnimation(anim);
				anim.start();
			}
		});
		return convertView;
	}

}
