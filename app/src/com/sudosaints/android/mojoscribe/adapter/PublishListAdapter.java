package com.sudosaints.android.mojoscribe.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.SingleFeedActivity;
import com.sudosaints.android.mojoscribe.model.UploadedFeed;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;

public class PublishListAdapter extends BaseAdapter {

	private List<UploadedFeed> feeds;
	private Context context;
	private Drawable defaultDrawable;
	private Logger logger;
	private Typeface typeface;

	public PublishListAdapter(Context context, List<UploadedFeed> feeds) {
		this.context = context;
		this.feeds = feeds;
		defaultDrawable = this.context.getResources().getDrawable(R.drawable.background);
		logger = new Logger(context);
		this.typeface = TypefaceLoader.get(context, "fonts/SansRegular.ttf");

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return feeds.size();
	}

	@Override
	public UploadedFeed getItem(int arg0) {
		// TODO Auto-generated method stub
		return feeds.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final UploadedFeed feed = getItem(position);
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.post_video_row, parent, false);
		}

		TextView feedTitleTextView, feedViewsTextView, feedUploadDateTextView;
		ImageView feedImageView;
		CheckBox unpublishCheckBox;
		feedTitleTextView = (TextView) convertView.findViewById(R.id.publishPostFeedTitleTextView);
		feedViewsTextView = (TextView) convertView.findViewById(R.id.publishPostFeedRatingTextView);
		feedUploadDateTextView = (TextView) convertView.findViewById(R.id.publishPostFeedDateTextView);
		feedTitleTextView.setTypeface(typeface);
		feedViewsTextView.setTypeface(typeface);
		feedUploadDateTextView.setTypeface(typeface);
		feedImageView = (ImageView) convertView.findViewById(R.id.publishPostFeedImageView);
		unpublishCheckBox = (CheckBox) convertView.findViewById(R.id.publishPostFeedCheckBox);
		unpublishCheckBox.setTypeface(typeface);
		feedImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, SingleFeedActivity.class);
				intent.putExtra(IntentExtras.INTENT_FEED_ID, feeds.get(position).getId());
				context.startActivity(intent);

			}
		});
		UrlImageViewHelper.setUrlDrawable(feedImageView, feed.getFiles().get(0).getName() + "", R.drawable.mojoloading);
		feedTitleTextView.setText(feed.getHeadline());
		feedViewsTextView.setText("NO DATA FROM SERVER");
		feedUploadDateTextView.setText(feed.getUpdatedOn());
		unpublishCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				feed.setChecked(isChecked);
			}
		});
		unpublishCheckBox.setChecked(feed.isChecked());
		return convertView;
	}

}
