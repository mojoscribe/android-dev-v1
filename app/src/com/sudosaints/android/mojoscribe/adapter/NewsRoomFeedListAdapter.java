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
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.SingleFeedActivity;
import com.sudosaints.android.mojoscribe.model.NewsRoomPosts;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;

public class NewsRoomFeedListAdapter extends BaseAdapter {

	private List<NewsRoomPosts> newsRoomPosts;
	private Context context;
	private Drawable defaultDrawable;
	private Typeface typeface;

	public NewsRoomFeedListAdapter(Context context, List<NewsRoomPosts> newsRoomPosts) {
		this.context = context;
		this.newsRoomPosts = newsRoomPosts;
		defaultDrawable = this.context.getResources().getDrawable(R.drawable.background);
		this.typeface = TypefaceLoader.get(context, "fonts/SansRegular.ttf");

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsRoomPosts.size();
	}

	@Override
	public NewsRoomPosts getItem(int arg0) {
		// TODO Auto-generated method stub
		return newsRoomPosts.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final NewsRoomPosts newsRoomPosts = getItem(position);
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.newsroom_video_layout, parent, false);
		}

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, SingleFeedActivity.class);
				intent.putExtra(IntentExtras.INTENT_FEED_ID, newsRoomPosts.getId());
				context.startActivity(intent);
			}
		};
		ImageView feedImage;
		TextView feedTitle, feedDate;

		feedImage = (ImageView) convertView.findViewById(R.id.newsRoomFeedImageView);
		feedTitle = (TextView) convertView.findViewById(R.id.newsRoomFeedTitleTextView);
		feedTitle.setTypeface(typeface);
		feedTitle.setSelected(true);
		feedDate = (TextView) convertView.findViewById(R.id.newsRoomFeedDateTextView);
		feedDate.setTypeface(typeface);
		UrlImageViewHelper.setUrlDrawable(feedImage, newsRoomPosts.getMediaUlr() + "", R.drawable.mojoloading);
		feedTitle.setText(newsRoomPosts.getHeadLine() + "");
		feedDate.setText(newsRoomPosts.getPostDate());
		feedImage.setOnClickListener(onClickListener);
		feedTitle.setOnClickListener(onClickListener);
		feedDate.setOnClickListener(onClickListener);

		return convertView;
	}

}
