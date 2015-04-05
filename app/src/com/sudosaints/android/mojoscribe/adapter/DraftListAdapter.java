package com.sudosaints.android.mojoscribe.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.model.UploadedFeed;
import com.sudosaints.android.mojoscribe.util.Logger;

public class DraftListAdapter extends BaseAdapter {

	List<UploadedFeed> feeds;
	private Context context;
	private Drawable defaultDrawable;
	Logger logger;

	public DraftListAdapter(Context context, List<UploadedFeed> feeds) {
		this.context = context;
		this.feeds = feeds;
		defaultDrawable = this.context.getResources().getDrawable(R.drawable.background);
		logger = new Logger(context);
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
			convertView = li.inflate(R.layout.draft_video_row, parent, false);
		}

		TextView feedTitleTextView, feedUploadDateTextView;
		ImageView feedImageView;
		CheckBox unpublishCheckBox;
		Button publishButton;

		feedTitleTextView = (TextView) convertView.findViewById(R.id.draftFeedTitleTextView);
		feedUploadDateTextView = (TextView) convertView.findViewById(R.id.draftFeedTimeTextView);

		feedImageView = (ImageView) convertView.findViewById(R.id.draftFeedImageView);
		unpublishCheckBox = (CheckBox) convertView.findViewById(R.id.draftFeedCheckBox);

		publishButton = (Button) convertView.findViewById(R.id.draftPublishButton);

		UrlImageViewHelper.setUrlDrawable(feedImageView, feed.getFiles().get(0).getName() + "", defaultDrawable);

		feedTitleTextView.setText(feed.getHeadline());
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
